#pragma once
#include "player.h"
namespace soul {
// Type 28 weapon-0 presentation actor. Sprite 2:25 must outlive the actor.
class MeleeWeapon {
public:
    MeleeWeapon(const BSprite&,const ActorRecord&);
    MeleeWeapon(BSprite&&,const ActorRecord&)=delete;
    MeleeWeapon(const BSprite&&,const ActorRecord&)=delete;
    void syncPlayer(const Player&);
    void update(const Player&,bool paused=false);
    void advanceAnimation();
    void hide() { flags|=128|1024; }
    void draw(Canvas&,int cameraX,int cameraY);
    void write(std::ostream&) const;
    bool visible() const { return !(flags&(128|1024)); }
    int uid,x,y,fixedX,fixedY,flags,animation,step=0,tick=0;
private:
    const BSprite& sprite_; SpriteRenderer renderer_;
    bool ending() const;
};
}
