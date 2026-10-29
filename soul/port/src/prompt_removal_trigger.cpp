#include "prompt_removal_trigger.h"
namespace soul {
bool PromptRemovalTrigger::supported(const ActorRecord& r){
    const auto& p=r.parameters;
    return r.count==19 && p[0]==40 && (p[5]&8) && !(p[5]&~(8|16|32|128|256|512|1024|16384)) &&
        p[8]>0 && p[9]>0 && p[10]==1 && p[11]==0 && p[12]==5 &&
        p[13]>=0 && p[14]==-1 && p[15]==-1 && p[16]>=0 && p[17]==-1 && p[18]==-1;
}
PromptRemovalTrigger::PromptRemovalTrigger(const ActorRecord& r){
    if(!supported(r))throw FormatError("Unsupported prompt-removal trigger");
    const auto& p=r.parameters;uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);flags=p[5]|128;
    sourceUid=p[13];targetUid=p[16];collision={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};
}
bool PromptRemovalTrigger::update(const std::function<MovingPlatform*(int)>& source,const std::function<PlacedPrompt*(int)>& target,bool paused){
    if(!alive || paused || (flags&(1024|16384)))return false;
    if(!source_)source_=source(sourceUid);
    if(!target_)target_=target(targetUid);
    if(!source_)return false;
    source_->refreshBounds();const auto& b=source_->collision;
    if((b[0]==b[2] && b[1]==b[3]) || b[0]>collision[2] || b[2]<collision[0] || b[1]>collision[3] || b[3]<collision[1])return false;
    if(target_){target_->alive=false;target_->collision={};}
    alive=false;collision={};source_=nullptr;target_=nullptr;return true;
}
void PromptRemovalTrigger::write(std::ostream& out)const{
    out<<'\t'<<std::boolalpha<<alive<<'\t'<<flags;for(int n:collision)out<<'\t'<<n;
    out<<'\t'<<(source_?1:0)<<'\t'<<(target_?1:0);
}
}
