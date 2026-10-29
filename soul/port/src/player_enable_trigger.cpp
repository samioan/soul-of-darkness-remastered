#include "player_enable_trigger.h"
namespace soul {
bool PlayerEnableTrigger::supported(const ActorRecord& r){
    const auto& p=r.parameters;
    return r.count==19 && p[0]==40 && (p[5]&8) && !(p[5]&~(4|8|16|32|128|256|1024|16384)) && p[8]>0 && p[9]>0 &&
        p[10]==1 && p[11]==0 && p[12]==0 && p[13]>=0 && p[14]==-1 && p[15]==-1;
}
PlayerEnableTrigger::PlayerEnableTrigger(const ActorRecord& r){
    if(!supported(r))throw FormatError("Unsupported player-overlap enable trigger");
    const auto& p=r.parameters;uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);flags=p[5]|128;sourceUid=p[13];uids_={p[16],p[17],p[18]};
    collision={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};
}
bool PlayerEnableTrigger::update(Player* source,const std::function<EnableEnemyTarget(int)>& resolve,bool paused){
    if(!alive || paused || (flags&(1024|16384)))return false;
    if(!source_)source_=source;
    for(std::size_t i=0;i<3;++i)if(uids_[i]!=-1 && !targets_[i].flags){
        const auto t=resolve(uids_[i]);if(t.flags && !t.action)throw FormatError("Enemy enable target needs action state");targets_[i]=t;
    }
    if(!source_)return false;
    source_->refreshBounds();const auto& bounds=source_->collision;
    if(collision[0]>bounds[2] || collision[2]<bounds[0] || collision[1]>bounds[3] || collision[3]<bounds[1])return false;
    for(auto t:targets_)if(t.flags){*t.flags&=~32;if(*t.action!=5)*t.flags&=~128;}
    // An unresolved target does not keep the original one-shot trigger alive.
    alive=false;source_=nullptr;targets_={};collision={};return true;
}
void PlayerEnableTrigger::write(std::ostream& out)const{
    for(int n:{uid,x,y,flags,int(alive)})out<<'\t'<<n;
    for(int n:collision)out<<'\t'<<n;out<<'\t'<<(source_?1:0);for(auto t:targets_)out<<'\t'<<(t.flags?1:0);
}
}
