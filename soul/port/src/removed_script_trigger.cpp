#include "removed_script_trigger.h"
namespace soul {
bool RemovedScriptTrigger::supported(const ActorRecord& r) {
    const auto& p=r.parameters;
    return r.count==19 && p[0]==40 && (p[5]&8) && !(p[5]&~(4|8|16|32|128|256|1024|16384)) && p[8]>0 && p[9]>0 &&
        p[10]==3 && p[11]==1 && p[12]==0 && p[13]>=0 && p[14]==-1 && p[15]==-1 && p[16]==165 && p[17]==-1 && p[18]==-1;
}
RemovedScriptTrigger::RemovedScriptTrigger(const ActorRecord& r) {
    if(!supported(r))throw FormatError("Unsupported removed-source script trigger");
    const auto& p=r.parameters;uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);flags=p[5]|128;sourceUid=p[13];scriptUid=p[16];
    collision={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};
}
void RemovedScriptTrigger::update(std::optional<int> source,const std::vector<bool>& removed,GateScript& script,bool paused) {
    if(!alive || paused || (flags&(1024|16384)))return;
    if(script.uid()!=scriptUid)throw FormatError("Removed-source trigger needs its selected gate script");
    if(!cached){sourceIndex=source.value_or(-1);cached=source.has_value();}
    // Preserve the original persistence-index-zero shortcut, including a live source.
    const bool condition=sourceIndex==0 || (sourceIndex!=-1 && removed.at(sourceIndex));
    if(condition && latch==-1){script.start();latch=1;}
    if(latch==1 && script.ticks()==-2){script.stop();latch=-1;alive=false;cached=false;collision={};}
}
void RemovedScriptTrigger::write(std::ostream& out) const {
    for(int n:{uid,x,y,flags,sourceIndex,latch})out<<'\t'<<n;
    out<<'\t'<<std::boolalpha<<cached<<'\t'<<alive;for(int n:collision)out<<'\t'<<n;
}
}
