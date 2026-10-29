#pragma once
#include "actor_script.h"
#include "player.h"
namespace soul {
struct ChandelierActor {
    int *x=nullptr,*y=nullptr,*flags=nullptr,*action=nullptr,*bucket=nullptr;
    std::function<void(int)> animate;
    std::function<void()> refresh;
    explicit operator bool()const{return x && y && flags && action && bucket && animate && refresh;}
};
// Selected script 160: existing enemy 205 and chandelier 34. No camera/input lock.
class ChandelierScript {
public:
    explicit ChandelierScript(ScriptData);
    int ticks=-1,starts=0,finishes=0;
    void start();void stop();
    void update(const std::function<ChandelierActor(int)>&,int cameraX,int cameraY,bool paused=false);
    const std::vector<int>& offsets()const{return offsets_;}
private:
    ScriptData data_;std::vector<int> offsets_;
};
class ChandelierTrigger {
public:
    static bool supported(const ActorRecord&);
    explicit ChandelierTrigger(const ActorRecord&);
    int uid=0,x=0,y=0,flags=0,bucket=0,latch=-1,sourceUid=0;
    bool alive=true;
    std::array<int,4> collision{};
    void update(Player*,ChandelierScript&,bool paused=false);
};
}
