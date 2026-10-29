#include "melee_weapon.h"
#include <cstring>
namespace soul {
namespace {
int fixed(int value) { const auto bits=std::uint32_t(value)<<8; std::int32_t result; std::memcpy(&result,&bits,4); return result; }
}
MeleeWeapon::MeleeWeapon(const BSprite& s,const ActorRecord& r):sprite_(s),renderer_(s) {
    const auto& p=r.parameters;
    if(r.count<6 || r.count>20 || p[0]!=28 || p[4]!=0 || (p[5]&~19) || s.animations().size()<3)
        throw FormatError("Unsupported weapon-0 anchor record/sprite");
    uid=p[1]; x=static_cast<std::uint16_t>(p[2]); y=static_cast<std::uint16_t>(p[3]);
    fixedX=fixed(x); fixedY=fixed(y); flags=p[5]|128; animation=0;
}
bool MeleeWeapon::ending() const {
    const auto& a=sprite_.animations().at(animation);
    const int duration=sprite_.animationFrames().at(a.start+step).duration;
    return step==a.count-1 && (duration<=1 || tick==duration-1);
}
void MeleeWeapon::syncPlayer(const Player& p) {
    if(p.activeWeapon==0 && p.animation>=13 && p.animation<=15) {
        if(p.step==0 && p.animationTick==0) {
            const int next=p.animation-13;
            if(next!=animation) { animation=next; flags&=~64; }
            step=tick=0; flags&=~(128|1024); x=p.x; y=p.y;
        } else if(ending()) hide();
    }
    if(!(flags&128) && ending()) hide();
}
void MeleeWeapon::update(const Player& p,bool paused) {
    if(paused || (flags&(1024|16384))) return;
    // This actor has no velocity; followPlayer changes pixel coordinates after integration.
    fixedX=fixed(x); fixedY=fixed(y); x=p.x; y=p.y;
    if(p.facingLeft) flags|=1; else flags&=~1;
}
void MeleeWeapon::advanceAnimation() {
    if(flags&(64|1024|16384)) return;
    const auto& a=sprite_.animations().at(animation);
    const int duration=sprite_.animationFrames().at(a.start+step).duration;
    if(duration && ++tick>=duration) { tick=0; if(++step>=a.count) step=0; }
}
void MeleeWeapon::draw(Canvas& target,int cameraX,int cameraY) {
    if(visible()) renderer_.drawAnimation(target,animation,step,x-cameraX,y-cameraY,{0,static_cast<unsigned>(flags)&3,{}});
}
void MeleeWeapon::write(std::ostream& out) const {
    for(int n:{fixedX,fixedY,x,y,flags,animation,step,tick}) out << '\t' << n;
}
}
