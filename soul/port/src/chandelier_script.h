#pragma once
#include "actor_script.h"
#include <algorithm>
#include "player.h"
namespace soul {
struct ChandelierActor {
    int *x=nullptr,*y=nullptr,*flags=nullptr,*action=nullptr,*bucket=nullptr;
    std::function<void(int)> animate;
    std::function<void()> refresh;
    // Optional: enemies take script command 106 through their action-change entry, platforms assign it directly.
    std::function<void(int)> scriptAction;
    // Optional: only enemies have a facing flag for commands 23/24.
    bool* facingLeft=nullptr;
    explicit operator bool()const{return x && y && flags && action && bucket && animate && refresh;}
};
// Selected scripts 160 (level 0) and 23/24/114 (level 1): one existing enemy track and one chandelier track.
// No camera/input lock; every opcode outside the shipped set fails explicitly.
class ChandelierScript {
public:
    explicit ChandelierScript(ScriptData);
    static bool supportedUid(int uid){return uid==160 || uid==23 || uid==24 || uid==114;}
    int uid()const{return data_.uid;}
    int enemyUid()const{return data_.tracks.at(0).target;}
    int chandelierUid()const{return data_.tracks.at(1).target;}
    int ticks=-1,starts=0,finishes=0;
    void start();void stop();
    void update(const std::function<ChandelierActor(int)>&,int cameraX,int cameraY,bool paused=false);
    const std::vector<int>& offsets()const{return offsets_;}
private:
    ScriptData data_;std::vector<int> offsets_;
};
// The original three-slot selected-script array (Game.ck/cj). Finishing frees the LAST slot (cj--) without compaction, so a
// stale earlier entry can survive; the world refuses the resulting background-script order instead of approximating it.
struct SelectedScriptSlots {
    std::array<int,3> uids{-1,-1,-1};int count=0;
    void select(int uid){if(count>=3)throw FormatError("More than three selected scripts");uids[count++]=uid;}
    void release(){if(count<=0)throw FormatError("No selected script slot to release");--count;}
    bool contains(int uid)const{return std::find(uids.begin(),uids.begin()+count,uid)!=uids.begin()+count;}
};
class ChandelierTrigger {
public:
    static bool supported(const ActorRecord&);
    explicit ChandelierTrigger(const ActorRecord&);
    int uid=0,x=0,y=0,flags=0,bucket=0,latch=-1,sourceUid=0,scriptUid=0;
    bool alive=true;
    std::array<int,4> collision{};
    void update(Player*,ChandelierScript&,bool paused=false);
};
}
