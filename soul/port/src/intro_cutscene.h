#pragma once
#include "actor_script.h"
#include "player.h"
#include <string_view>
namespace soul {
struct IntroActor {
    int *x=nullptr,*y=nullptr,*flags=nullptr;bool* facing=nullptr;
    std::function<void(int)> animate;
    std::function<int()> top;
    std::function<void()> moved,remove;
    explicit operator bool()const{return x && y && flags && facing && animate && top && moved && remove;}
};
class IntroTrigger {
public:
    explicit IntroTrigger(const ActorRecord&);
    bool update(const Player&,bool paused=false);
    int uid=0,x=0,y=0,flags=0,bucket=0;bool alive=true,skipAllowed=true;
    std::array<int,4> collision{};
};
class IntroCutscene {
public:
    explicit IntroCutscene(ScriptData,int mapWidth,int mapHeight);
    void start(int cameraX,int cameraY,bool allowSkip=true);
    void step(const std::function<IntroActor(int)>& resolve);
    void skip(const std::function<IntroActor(int)>& resolve);
    void dismissDialogue(){dialogue=-1;dialogueFrames=page=0;}
    bool active()const{return ticks>=0;}
    void presentationTick();
    int ticks=-1,cameraX=0,cameraY=0,dialogue=-1,speaker=-1,anchorX=0,anchorY=0;
    int dialogueFrames=0,page=0,bars=0,starts=0,finishes=0,skips=0,dialoguesOpened=0;
    bool skipAllowed=true;
    const std::vector<int>& offsets()const{return offsets_;}
private:
    ScriptData data_;int width_,height_;std::vector<int> offsets_;
    void advance(const std::function<IntroActor(int)>&,bool ignoreDialogue);
};
// Expand a canonical scripted focus into the actual desktop viewport.
std::pair<int,int> introView(int cameraX,int cameraY,int viewportWidth,int mapWidth,int mapHeight);
std::u16string_view introText(int);
std::u16string_view introSpeaker(int);
}
