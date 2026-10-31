#include "removed_enemy_trigger.h"
namespace soul {
bool RemovedEnemyTrigger::supported(const ActorRecord& r){const auto& p=r.parameters;return r.count==19 && p[0]==40 && (p[5]&8) && !(p[5]&~(4|8|16|32|128|256|1024|16384)) && p[8]>0 && p[9]>0 && p[10]==4 && p[11]==0 && p[12]==0 && p[13]>=0 && p[14]>=0 && p[15]==-1 && p[16]>=-1 && p[17]>=-1 && p[18]>=-1 && (p[16]>=0 || p[17]>=0 || p[18]>=0);}
RemovedEnemyTrigger::RemovedEnemyTrigger(const ActorRecord& r){
    if(!supported(r))throw FormatError("Unsupported removed-source enemy trigger");const auto& p=r.parameters;
    uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);flags=p[5]|128;
    collision={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};sourceUids={p[13],p[14],p[15]};targetUids_={p[16],p[17],p[18]};
}
bool RemovedEnemyTrigger::update(const std::function<std::optional<int>(int)>& source,const std::vector<bool>& removed,const std::function<EnableEnemyTarget(int)>& target,bool paused){
    if(!alive || paused || (flags&(1024|16384)))return false;
    for(std::size_t i=0;i<3;++i)if(!cached_[i]){
        indices[i]=-1;
        if(sourceUids[i]!=-1){const auto index=source(sourceUids[i]);if(index){indices[i]=*index;cached_[i]=true;}else if(i==0)sourceUids[i]=-1;}
    }
    for(std::size_t i=0;i<3;++i)if(targetUids_[i]!=-1 && !targets_[i].flags){const auto t=target(targetUids_[i]);if(t.flags && !t.action)throw FormatError("Removed-source enable target needs action");targets_[i]=t;}
    for(std::size_t i=0;i<3;++i)if(sourceUids[i]!=-1 && indices[i]!=0 && (indices[i]==-1 || !removed.at(indices[i])))return false;
    for(auto t:targets_)if(t.flags){*t.flags&=~32;if(*t.action!=5)*t.flags&=~128;if(t.enabled)t.enabled();}
    alive=false;cached_={};targets_={};collision={};return true;
}
void RemovedEnemyTrigger::write(std::ostream& out)const{
    for(int n:{uid,x,y,flags,int(alive)})out<<'\t'<<n;for(int n:collision)out<<'\t'<<n;for(int n:sourceUids)out<<'\t'<<n;for(int n:indices)out<<'\t'<<n;for(bool b:cached_)out<<'\t'<<int(b);for(auto t:targets_)out<<'\t'<<(t.flags?1:0);
}
}
