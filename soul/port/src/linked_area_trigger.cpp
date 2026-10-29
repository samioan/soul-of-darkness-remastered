#include "linked_area_trigger.h"
namespace soul {
bool LinkedAreaTrigger::supported(const ActorRecord& r) {
    const auto& p=r.parameters;
    return r.count==19 && p[0]==40 && (p[5]&8) && !(p[5]&~(8|16|32|128|256|1024|16384)) && p[8]>0 && p[9]>0 &&
        p[10]==0 && p[11]==0 && p[12]==2 && p[13]==-1 && p[14]==-1 && p[15]==-1;
}
LinkedAreaTrigger::LinkedAreaTrigger(const ActorRecord& r) {
    if(!supported(r))throw FormatError("Unsupported linked area trigger record");
    const auto& p=r.parameters;uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);flags=p[5]|128;
    collision={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};uids_={p[16],p[17],p[18]};
}
void LinkedAreaTrigger::update(const std::array<int,4>* region,const std::function<int*(int)>& resolve,bool paused) {
    if(paused || (flags&(1024|16384)) || !region)return;
    for(std::size_t i=0;i<3;++i)if(uids_[i]!=-1 && !targets_[i])targets_[i]=resolve(uids_[i]);
    const bool overlap=collision[0]<=(*region)[2] && collision[2]>=(*region)[0] && collision[1]<=(*region)[3] && collision[3]>=(*region)[1];
    if(overlap)flags|=16;else flags&=~16;
    for(auto target:targets_)if(target) {
        if(overlap)*target=(*target&~32)|16;
        else *target=(*target|32)&~16;
    }
}
void LinkedAreaTrigger::write(std::ostream& out) const {
    out<<'\t'<<uid<<'\t'<<x<<'\t'<<y<<'\t'<<flags;
    for(int n:collision)out<<'\t'<<n;
    for(auto target:targets_)out<<'\t'<<(target?1:0);
}
}
