#include "checkpoint.h"
#include <algorithm>
namespace soul {
Checkpoint::Checkpoint(const ActorRecord& r){
    const auto& p=r.parameters;
    if(r.count<10 || r.count>20 || p[0]!=23 || !(p[5]&8) || (p[5]&~(1|8|16|32|128|256|1024|16384)) || p[8]<=0 || p[9]<=0)throw FormatError("Unsupported checkpoint record");
    uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);flags=p[5];collision={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};
}
bool Checkpoint::update(const Player& p,bool paused){
    const auto& a=p.collision;const auto& b=collision;
    if(!alive || paused || (flags&(1024|16384)) || (a[0]==a[2] && a[1]==a[3]) || a[0]>b[2] || a[2]<b[0] || a[1]>b[3] || a[3]<b[1])return false;
    alive=false;return true;
}
CheckpointSnapshot CheckpointSnapshot::capture(const Checkpoint& c,const Player& p,const PlayerMeters& m,std::vector<RemovedCheckpointActor> removedActors){
    CheckpointSnapshot saved;saved.level=p.level();saved.uid=c.uid;saved.x=static_cast<std::int16_t>(p.x);saved.y=static_cast<std::int16_t>(p.y);saved.facingLeft=p.facingLeft;saved.activeWeapon=p.activeWeapon;saved.meters=m;
    for(const auto& a:removedActors)if(!(a.flags&8192) && std::none_of(saved.removed.begin(),saved.removed.end(),[&](const auto& old){return old.uid==a.uid;}))saved.removed.push_back(a);
    if(std::none_of(saved.removed.begin(),saved.removed.end(),[&](const auto& a){return a.uid==c.uid;}))saved.removed.push_back({c.uid,c.flags});return saved;
}
bool CheckpointSnapshot::skips(const ActorRecord& r)const{
    const auto a=std::find_if(removed.begin(),removed.end(),[&](const auto& old){return old.uid==r.parameters[1];});if(a==removed.end())return false;
    const int type=r.parameters[0];return (a->flags&512) || type==35 || type==23 || type==48 || type==82;
}
CheckpointSnapshot CheckpointSnapshot::forContinue()const{
    auto saved=*this;
    // The original Continue construction clears per-record persistence, retaining UID removals.
    saved.removed.erase(std::remove_if(saved.removed.begin(),saved.removed.end(),[](const auto& r){return !(r.flags&512);}),saved.removed.end());return saved;
}
void CheckpointSnapshot::restore(Player& p,PlayerMeters& m)const{
    if(level!=p.level())throw FormatError("Checkpoint belongs to another selected level");
    m=meters;m.health=m.maxHealth;p.x=x;p.y=y;p.fixedX=x*256;p.fixedY=y*256;p.facingLeft=facingLeft;p.flags=int(facingLeft);
    p.vx=p.vy=p.ax=p.ay=0;p.animation=0;p.pending=-1;p.step=p.animationTick=0;p.platform=nullptr;p.activeWeapon=activeWeapon;p.iceOwned=m.iceUnlocked;p.refreshBounds();
}
}
