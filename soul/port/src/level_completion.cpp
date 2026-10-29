#include "level_completion.h"
namespace soul {
bool LevelCompletion::start(){
    if(state!=8)return false;
    state=15;target=160;bars=1;entryPending_=true;++starts;return true;
}
void LevelCompletion::advanceBars(){
    if(delay<0 && bars!=target){const int delta=target-bars;if(delta>-20 && delta<20)bars=target;else bars+=delta>0?20:-20;}
    if(bars>0)--delay;else delay=2;
}
void LevelCompletion::entryFrame(){if(entryPending_){entryPending_=false;advanceBars();}}
bool LevelCompletion::update(unsigned pressed){
    if(!active())return false;++ticks;entryPending_=false;advanceBars();
    // updateLevelComplete draws the world once more after the bars meet.
    if(ready())advanceBars();
    if(ready() && (pressed&327712)){bars=target=0;state=19;++acknowledgements;return true;}
    return false;
}
void LevelCompletion::write(std::ostream& out)const{for(int n:{state,bars,target,delay,starts,acknowledgements})out<<'\t'<<n;}
bool ExitTrigger::supported(const ActorRecord& r){const auto& p=r.parameters;
    return r.count==15 && p[0]==20 && p[4]==4 && (p[5]&8) && !(p[5]&~(8|16|32|128|256|512|1024|16384)) && p[8]>0 && p[9]>0 && p[10]==0 && p[11]==1 && p[12]==-1 && p[13]==0 && p[14]==0;
}
ExitTrigger::ExitTrigger(const ActorRecord& r){
    if(!supported(r))throw FormatError("Unsupported level-zero exit trigger");const auto& p=r.parameters;
    uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);flags=p[5]|128;
    collision={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};
}
bool ExitTrigger::update(const Player& p,LevelCompletion& complete,bool paused){
    const auto& a=p.collision;const auto& b=collision;
    if(paused || (flags&(1024|16384)) || p.animation==9 || (a[0]==a[2] && a[1]==a[3]) || a[0]>b[2] || a[2]<b[0] || a[1]>b[3] || a[3]<b[1])return false;
    return complete.start();
}
}
