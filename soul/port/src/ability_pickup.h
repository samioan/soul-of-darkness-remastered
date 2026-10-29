#pragma once
#include "pickup.h"
namespace soul {
struct AbilityInteraction {
    bool active=false,inputBlocked=false,weaponHidden=false;
    int mode=-1,tint=0,tutorial=-1,beamX=0,beamY=0,beamTicks=0,tutorialFrames=0,clock=0;
    void advance(unsigned pressed);
};
// Type 48: magic (sprite 27 / animation 2) and ice (sprite 18 / animation 35).
class AbilityPickup {
public:
    AbilityPickup(const BSprite&,int x,int y,bool facingLeft=false,int animation=2);
    AbilityPickup(BSprite&&,int,int,bool=false,int=2)=delete;
    AbilityPickup(const BSprite&&,int,int,bool=false,int=2)=delete;
    void update(Player&,PlayerMeters&,AbilityInteraction&,int level=0,bool paused=false);
    void refreshBounds(); void advanceAnimation(); void draw(Canvas&,int cameraX,int cameraY);
    void write(std::ostream&) const;
    int fixedX=0,fixedY=0,x=0,y=0,vx=0,vy=0,ax=0,ay=0,flags=0,step=0,tick=0,action=0,distance=0,animation=2;
    bool alive=true,facingLeft=false;
    std::array<int,4> collision{};
private:
    const BSprite& sprite_; SpriteRenderer renderer_;
};
}
