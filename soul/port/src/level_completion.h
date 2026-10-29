#pragma once
#include "player.h"
namespace soul {
// State-15 UI ownership only. State 19 is a handoff, not finishLevel progression.
struct LevelCompletion {
    int state=8,bars=0,target=0,delay=2,starts=0,acknowledgements=0,ticks=0;
    bool start();
    bool update(unsigned pressed);
    void entryFrame();
    bool active()const{return state==15;}
    bool ready()const{return active() && bars==target;}
    void write(std::ostream&)const;
private:
    bool entryPending_=false;
    void advanceBars();
};
class ExitTrigger {
public:
    static bool supported(const ActorRecord&);
    explicit ExitTrigger(const ActorRecord&);
    bool update(const Player&,LevelCompletion&,bool paused=false);
    int uid=0,x=0,y=0,flags=0,bucket=0;
    std::array<int,4> collision{};
};
}
