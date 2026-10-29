#include "collection_effect.h"
#include <ostream>
namespace soul {
CollectionEffect::CollectionEffect(const BSprite& s,CollectionEffectSpawn spawn)
    :animation(spawn.animation),x(spawn.x),y(spawn.y),flags(spawn.flags),sprite_(s),renderer_(s) {
    if((animation!=11 && animation!=12) || flags<0 || flags>1 ||
        static_cast<std::size_t>(animation)>=s.animations().size() || s.animations()[animation].count!=1)
        throw FormatError("Unsupported collection effect");
}
void CollectionEffect::update(bool paused) {
    if(!alive || paused) return;
    fixedX=x*256; fixedY=y*256; timer-=83;
    const auto& a=sprite_.animations().at(animation);
    const int duration=sprite_.animationFrames().at(a.start+step).duration;
    if(step==a.count-1 && (duration<=1 || tick==duration-1)) alive=false;
}
void CollectionEffect::advanceAnimation() {
    if(!alive || (flags&64)) return;
    const auto& a=sprite_.animations().at(animation);
    const int duration=sprite_.animationFrames().at(a.start+step).duration;
    if(duration && ++tick>=duration) { tick=0; if(++step>=a.count) step=0; }
}
int CollectionEffect::diameter() const { return step*30/sprite_.animations().at(animation).count; }
void CollectionEffect::draw(Canvas& target,int cameraX,int cameraY) {
    if(!alive) return;
    renderer_.drawAnimation(target,animation,step,x-cameraX,y-cameraY,{0,static_cast<unsigned>(flags)&3,{}});
    // Both shipped animations have one frame: the original step*30/count
    // drawArc diameter is always zero. Alternate multi-frame assets are rejected.
}
void CollectionEffect::write(std::ostream& out) const {
    for(int n:{fixedX,fixedY,x,y,flags,animation,step,tick,timer,int(alive),diameter()}) out << '\t' << n;
}
}
