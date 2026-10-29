#pragma once
#include "player.h"
#include "collection_effect.h"
namespace soul {
struct PlayerMeters { int health=60,maxHealth=60,magic=60,maxMagic=60,collected=0,healthFragments=0,magicFragments=0; bool magicUnlocked=false,iceUnlocked=false; };
// Type 35 health/magic and crystal lifecycle subset. No enemy attraction or saves.
class Pickup {
public:
    Pickup(const BSprite&,const ActorRecord&);
    Pickup(BSprite&&,const ActorRecord&)=delete;
    Pickup(const BSprite&&,const ActorRecord&)=delete;
    static Pickup spawned(const BSprite&,int animation,int x,int y,int flags);
    static Pickup spawned(BSprite&&,int,int,int,int)=delete;
    static Pickup spawned(const BSprite&&,int,int,int,int)=delete;
    static Pickup enemyDrop(const BSprite&,int animation,int x,int y,int flags);
    static Pickup enemyDrop(BSprite&&,int,int,int,int)=delete;
    static Pickup enemyDrop(const BSprite&&,int,int,int,int)=delete;
    int spawnDelay=0;
    void refreshBounds() { bounds(); }
    void update(Player&,PlayerMeters&,bool paused=false);
    void beginCrystalDisplay(const Player&);
    void finishCrystal();
    void advanceAnimation();
    void draw(Canvas&,int cameraX,int cameraY,int stateFrame);
    void write(std::ostream&) const;
    int remainingLifetime() const { return lifetime_; }
    bool alive=true,upgradeBoundary=false;
    int uid=0,x=0,y=0,fixedX=0,fixedY=0,vx=0,vy=0,flags=0,animation=0,step=0,tick=0,action=0;
    int timer=0,distance=0;
    std::array<int,4> collision{};
    // Per-update events, consumed by the world after this actor's handler.
    std::vector<CollectionEffectSpawn> effects;
private:
    Pickup(const BSprite&,const ActorRecord&,bool spawned);
    const BSprite& sprite_; SpriteRenderer renderer_;
    bool spawned_=false,enemyDrop_=false; int lifetime_=0;
    void bounds(); bool ending() const;
    void collect();
};
}
