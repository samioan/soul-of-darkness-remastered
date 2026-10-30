#include "barrier_trigger.h"
namespace soul {
bool BarrierTrigger::supported(const ActorRecord& r){const auto& p=r.parameters;return r.count==19 && p[0]==40 && (p[5]&8) && !(p[5]&~(8|16|32|128|256|512|1024|8192|16384)) && p[8]>0 && p[9]>0 && p[11]==0 && p[13]>=0 && p[14]==-1 && p[15]==-1 && ((p[10]==1 && p[12]==0) || (p[10]==5 && (p[12]==6 || p[12]==7)));}
BarrierTrigger::BarrierTrigger(const ActorRecord& r){if(!supported(r))throw FormatError("Unsupported barrier trigger record");const auto& p=r.parameters;uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);originalFlags_=p[5];flags=p[5]|128;condition=p[10];operation=p[12];sourceUid=p[13];targetUids={p[16],p[17],p[18]};collision={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};}
void BarrierTrigger::resetForRegionExit(){if(flags&8192)flags=(originalFlags_|128|1024)|(flags&512);else{alive=false;collision={};detach();}}
void BarrierTrigger::detach(){source_={};targets_={};}
void BarrierTrigger::update(const std::function<BarrierSignal(int)>& source,const std::function<BarrierTarget(int)>& target,bool paused){
    if(!alive || paused || (flags&(1024|16384)))return;
    if(!source_.flags)source_=source(sourceUid);
    for(std::size_t i=0;i<3;++i)if(targetUids[i]!=-1 && !targets_[i].flags)targets_[i]=target(targetUids[i]);
    if(!source_.flags)return;
    bool active=(*source_.flags&1024)!=0;
    if(condition==1){if(source_.refresh)source_.refresh();if(!source_.bounds)return;const auto& b=*source_.bounds;active=(b[0]!=b[2] || b[1]!=b[3]) && b[0]<=collision[2] && b[2]>=collision[0] && b[1]<=collision[3] && b[3]>=collision[1];}
    if(!active)return;
    for(auto& t:targets_)if(t.flags){if(operation==0){*t.flags&=~32;if(!t.action || *t.action!=5)*t.flags&=~128;}else if(operation==6)*t.flags&=~8192;else if(t.release)t.release();else throw FormatError("Barrier release target has no animation owner");}
    if(operation!=7){detach();if(flags&8192)flags|=1024;else{alive=false;collision={};}}
}
void BarrierTrigger::write(std::ostream& out)const{out<<'\t'<<uid<<'\t'<<flags<<'\t'<<int(alive);for(int n:collision)out<<'\t'<<n;out<<'\t'<<int(source_.flags!=nullptr);for(const auto& t:targets_)out<<'\t'<<int(t.flags!=nullptr);}
}
