#include "linked_passage.h"
namespace soul {
namespace {
bool overlap(const std::array<int,4>& a,const std::array<int,4>& b){return a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1];}
}
LinkedPassage::LinkedPassage(const ActorRecord& r){
    const auto& p=r.parameters;
    if(r.count!=12 || p[0]!=59 || !(p[5]&8) || (p[5]&~(1|2|4|8|16|32|128|256|1024|16384)) ||
        p[8]<=0 || p[8]>1024 || p[9]<=0 || p[9]>4096 || p[10]<0 || (p[11]!=1 && p[11]!=2))
        throw FormatError("Unsupported horizontal passage record");
    uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);flags=p[5]|128;targetUid=p[10];direction=p[11];
    collision={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};
}
bool LinkedPassage::update(Player& p,const std::function<const LinkedPassage*(int)>& resolve,bool paused){
    if(paused || (flags&(1024|16384)))return false;
    if(!linked_)linked_=resolve(targetUid);
    if(!overlap(p.collision,collision) || (direction==1?(!p.facingLeft || p.vx>=0):(p.facingLeft || p.vx<=0)))return false;
    if(!linked_)throw FormatError("Horizontal passage target is missing");
    const auto& target=*linked_;
    p.x=target.x;
    // Original refreshes bounds at every one-pixel exit step, then changes Y
    // without another refresh. Fixed coordinates are resynchronized next tick.
    int steps=0;
    for(;;){
        p.refreshBounds();if(!overlap(p.collision,target.collision))break;
        if(++steps>4096)throw FormatError("Passage exit exceeds supported player bounds");
        p.x+=direction==1?-1:1;
    }
    const int fraction=((collision[3]-p.y)*256)/(collision[3]-collision[1]);
    p.y=target.collision[3]-((target.collision[3]-target.collision[1])*fraction>>8);
    p.platform=nullptr;
    return true;
}
}
