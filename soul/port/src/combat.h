#pragma once
#include "sprite_renderer.h"
#include <iosfwd>
namespace soul {
class BitmapFont;
// java.util.Random's 48-bit stream, including Game.randomRange's signed remainder.
class JavaRandom {
public:
    explicit JavaRandom(std::uint64_t seed=12345):state_((seed^0x5deece66dULL)&mask_) {}
    int nextInt(); int range(int minimum,int maximum);
private:
    static constexpr std::uint64_t mask_=(1ULL<<48)-1;
    std::uint64_t state_;
};
struct Weapon0 { int damageRank=0,secondaryRank=1; int roll(JavaRandom&) const; int adjusted(int damage) const; };
struct PlayerDamage {
    int invulnerability=0;
    bool apply(int& health,int maximum,int damage,bool shield=false);
    void tick(bool paused=false);
};
struct CombatEffectSpawn { int animation=2,x=0,y=0,flags=0,timer=0,amount=0; };
// Type-39 blood, death burst and floating damage number in level mode 0.
class CombatEffect {
public:
    CombatEffect(const BSprite&,CombatEffectSpawn);
    CombatEffect(BSprite&&,CombatEffectSpawn)=delete;
    CombatEffect(const BSprite&&,CombatEffectSpawn)=delete;
    int animation=0,x=0,y=0,fixedX=0,fixedY=0,vy=0,flags=0,step=0,tick=0,timer=0,amount=0;
    bool alive=true;
    void update(bool paused=false); void advanceAnimation();
    void draw(Canvas&,int cameraX,int cameraY,BitmapFont* font=nullptr);
    void write(std::ostream&) const;
private:
    const BSprite& sprite_; SpriteRenderer renderer_;
};
}
