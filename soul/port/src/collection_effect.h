#pragma once
#include "sprite_renderer.h"
namespace soul {
struct CollectionEffectSpawn { int animation,x,y,flags; };
// Type 39 animations 11/12 only. The sprite must outlive this object.
class CollectionEffect {
public:
    CollectionEffect(const BSprite&,CollectionEffectSpawn);
    CollectionEffect(BSprite&&,CollectionEffectSpawn)=delete;
    CollectionEffect(const BSprite&&,CollectionEffectSpawn)=delete;
    void update(bool paused=false);
    void advanceAnimation();
    void draw(Canvas&,int cameraX,int cameraY);
    int diameter() const;
    void write(std::ostream&) const;
    bool alive=true;
    int animation,x,y,flags,step=0,tick=0,timer=0,fixedX=0,fixedY=0;
private:
    const BSprite& sprite_; SpriteRenderer renderer_;
};
}
