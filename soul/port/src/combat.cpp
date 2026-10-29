#include "combat.h"
#include "bitmap_font.h"
#include <cstring>
#include <ostream>
namespace soul {
namespace {
int jint(std::uint32_t u) { std::int32_t i; std::memcpy(&i,&u,4); return i; }
int add(int a,int b) { return jint(std::uint32_t(a)+std::uint32_t(b)); }
int mul(int a,int b) { return jint(std::uint32_t(a)*std::uint32_t(b)); }
int sar(int a,unsigned n) { return jint((std::uint32_t(a)>>n)|(a<0?(~0u<<(32-n)):0)); }
}
int JavaRandom::nextInt() { state_=(state_*0x5deece66dULL+11)&mask_; return jint(static_cast<std::uint32_t>(state_>>16)); }
int JavaRandom::range(int low,int high) {
    if(low==high) return high;
    int value=nextInt(); if(value<0) value=jint(0u-std::uint32_t(value));
    const int divisor=add(high,jint(0u-std::uint32_t(low)));
    if(!divisor) throw FormatError("Random range division by zero");
    // Java specifies MIN_VALUE % -1 as zero; C++'s division would overflow.
    const int remainder=divisor==-1?0:value%divisor;
    return add(low,remainder);
}
int Weapon0::roll(JavaRandom& random) const {
    constexpr int ranks[]{10,13,15,20};
    if(damageRank<0 || damageRank>3) throw FormatError("Invalid weapon-0 damage rank");
    return mul(ranks[damageRank],random.range(9,12))/10;
}
int Weapon0::adjusted(int damage) const { return mul(damage,100-(damageRank==3?10:0)-(secondaryRank==4?15:0))/100; }
bool PlayerDamage::apply(int& health,int maximum,int damage,bool shield) {
    if(invulnerability>0 && damage<500) return false;
    invulnerability=shield?1200:600;
    if(health>0) {
        const int scale=add(jint(std::uint32_t(add(maximum,-60))<<7)/60,256);
        health=add(health,jint(0u-std::uint32_t(sar(mul(damage,scale),8))));
        if(health<0) health=0;
    }
    return true;
}
void PlayerDamage::tick(bool paused) { if(!paused && invulnerability>0) invulnerability=add(invulnerability,-83); }
CombatEffect::CombatEffect(const BSprite& s,CombatEffectSpawn spawn)
    :animation(spawn.animation),x(spawn.x),y(spawn.y),flags(spawn.flags),timer(spawn.timer),amount(spawn.amount),sprite_(s),renderer_(s) {
    if((animation!=2 && animation!=3 && animation!=28 && animation!=29) || std::size_t(animation)>=s.animations().size() || flags<0 || flags>1)
        throw FormatError("Unsupported combat effect");
}
void CombatEffect::update(bool paused) {
    if(!alive || paused) return;
    fixedX=jint(std::uint32_t(x)<<8);
    fixedY=add(add(fixedY,jint(std::uint32_t(add(y,-sar(fixedY,8)))<<8)),vy);
    y=sar(fixedY,8); timer=add(timer,-83);
    if(animation==29) { timer=add(timer,-83); vy=-1024; if(timer<0) alive=false; }
    else {
        const auto& a=sprite_.animations().at(animation); const int duration=sprite_.animationFrames().at(a.start+step).duration;
        if(step==a.count-1 && (duration<=1 || tick==duration-1)) alive=false;
    }
}
void CombatEffect::advanceAnimation() {
    if(!alive || (flags&64) || (animation==29 && amount>=100)) return;
    const auto& a=sprite_.animations().at(animation); const int d=sprite_.animationFrames().at(a.start+step).duration;
    if(d && ++tick>=d) { tick=0; if(++step>=a.count) step=0; }
}
void CombatEffect::draw(Canvas& c,int cx,int cy,BitmapFont* font) {
    if(!alive) return;
    if(animation==29) {
        if(amount>=100) return;
        if(!font) throw FormatError("Damage-number text needs the world's font renderer");
        auto number=[](int n) { const auto value=std::to_string(n); return std::u16string(value.begin(),value.end()); };
        int px=x-cx; font->draw(c,u"-",px,y-cy);
        if(amount/10!=0) { px+=5; font->draw(c,number(amount/10),px,y-cy); }
        font->draw(c,number(amount%10),px+5,y-cy); return;
    }
    renderer_.drawAnimation(c,animation,step,x-cx,y-cy,{0,static_cast<unsigned>(flags)&3,{}});
}
void CombatEffect::write(std::ostream& out) const {
    for(int n:{fixedX,fixedY,x,y,vy,flags,animation,step,tick,timer,amount,int(alive)}) out << '\t' << n;
}
}
