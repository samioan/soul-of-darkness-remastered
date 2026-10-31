#include "removed_script_trigger.h"
namespace soul {
bool RemovedScriptTrigger::supported(const ActorRecord& r) {
    const auto& p=r.parameters;
    return r.count==19 && p[0]==40 && (p[5]&8) && !(p[5]&~(4|8|16|32|128|256|1024|16384)) && p[8]>0 && p[9]>0 &&
        (p[10]==3 || p[10]==4) && p[11]==1 && p[12]==0 && p[13]>=0 && p[14]>=-1 && p[15]>=-1 && p[16]>=0 && p[17]==-1 && p[18]==-1;
}
RemovedScriptTrigger::RemovedScriptTrigger(const ActorRecord& r) {
    if(!supported(r))throw FormatError("Unsupported removed-source script trigger");
    const auto& p=r.parameters;uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);flags=p[5]|128;sourceUid=p[13];scriptUid=p[16];mode=p[10];
    sourceUids={p[13],p[14],p[15]};
    collision={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};
}
// Original `Actor.a(int,int,boolean)`: an unused slot yields the mode's neutral value (false for any, true for all);
// otherwise a removed index or the persistence-index-zero shortcut satisfies the slot.
bool RemovedScriptTrigger::slotCondition(std::size_t i,const std::vector<bool>& removed) const {
    if(sourceUids[i]==-1)return mode==4;
    return (indices[i]!=-1 && removed.at(static_cast<std::size_t>(indices[i]))) || indices[i]==0;
}
void RemovedScriptTrigger::update(std::optional<int> source,const std::vector<bool>& removed,GateScript& script,bool paused) {
    if(sourceUids[1]!=-1 || sourceUids[2]!=-1)throw FormatError("Single-source update needs a one-source trigger");
    updateSources(SourceLookup([&](int){return source;}),removed,script,paused);
}
void RemovedScriptTrigger::updateSources(const SourceLookup& source,const std::vector<bool>& removed,GateScript& script,bool paused) {
    if(!alive || paused || (flags&(1024|16384)))return;
    if(script.uid()!=scriptUid)throw FormatError("Removed-source trigger needs its selected gate script");
    for(std::size_t i=0;i<3;++i)if(!cached_[i]){
        indices[i]=-1;
        if(sourceUids[i]!=-1){
            const auto index=source(sourceUids[i]);
            if(index){indices[i]=*index;cached_[i]=true;}
            // Condition 4 drops an unresolved first source for good; every other miss is retried.
            else if(i==0 && mode==4)sourceUids[0]=-1;
        }
    }
    // Preserve the original persistence-index-zero shortcut, including a live source.
    const bool condition=mode==4?(slotCondition(0,removed) && slotCondition(1,removed) && slotCondition(2,removed))
                                :(slotCondition(0,removed) || slotCondition(1,removed) || slotCondition(2,removed));
    if(condition && latch==-1){script.start();latch=1;}
    if(latch==1 && script.ticks()==-2){script.stop();latch=-1;alive=false;cached_={};collision={};}
    sourceIndex=indices[0];cached=cached_[0];
}
void RemovedScriptTrigger::write(std::ostream& out) const {
    for(int n:{uid,x,y,flags,sourceIndex,latch})out<<'\t'<<n;
    out<<'\t'<<std::boolalpha<<cached<<'\t'<<alive;for(int n:collision)out<<'\t'<<n;
}
void RemovedScriptTrigger::writeSources(std::ostream& out) const {
    for(int n:sourceUids)out<<'\t'<<n;for(int n:indices)out<<'\t'<<n;for(bool b:cached_)out<<'\t'<<int(b);
}
}
