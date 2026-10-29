#pragma once
#include "player.h"
#include <string_view>
namespace soul {
// Placed type-21 volumes; dynamic camera/player-following prompts are separate owners.
class PlacedPrompt {
public:
    explicit PlacedPrompt(const ActorRecord&);
    bool update(const Player&,int recentMagic,int& switchEvent,bool paused=false);
    bool visible(const Player&,const std::array<int,4>& view)const;
    void advanceAnimation(const BSprite&);
    int uid=0,x=0,y=0,flags=0,bucket=0,animation=0,step=0,tick=0,status=0,delay=0;
    bool alive=true,facingLeft=false;
    std::array<int,4> collision{};
};
std::u16string_view desktopPromptText(int animation);
}
