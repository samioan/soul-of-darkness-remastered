#pragma once
#include "player.h"
#include "combat.h"
namespace soul {
// Ground enemy AI and common damage/lifecycle helpers shared by flying enemies.
struct PlayerMeters;
struct CombatExchange {
    Player* player; PlayerDamage* damage; PlayerMeters* meters; JavaRandom* random;
    std::vector<CombatEffectSpawn>* effects; int* playerHits; int* enemyHits;
};
struct EnemyLootSpawn { int animation=0,x=0,y=0,flags=0; };
struct EnemyTarget { int x=0,fixedX=0; std::array<int,4> collision{},attack{}; int fixedY=0; };
class EnemyType1 {
public:
    EnemyType1(const BSprite&,const LevelMaps&,const ActorRecord&,const std::vector<int>& statRow);
    EnemyType1(BSprite&&,const LevelMaps&,const ActorRecord&,const std::vector<int>&)=delete;
    EnemyType1(const BSprite&&,const LevelMaps&,const ActorRecord&,const std::vector<int>&)=delete;
    EnemyType1(const BSprite&,const LevelMaps&&,const ActorRecord&,const std::vector<int>&)=delete;
    EnemyType1(const BSprite&,LevelMaps&&,const ActorRecord&,const std::vector<int>&)=delete;
    int type=1;
    int x=0,y=0,fixedX=0,fixedY=0,vx=0,vy=0,ax=0,ay=0,flags=0,uid=0;
    int animation=0,pending=-1,step=0,tick=0,action=0,health=0,timer=0,paletteTimer=0,damageCooldown=0;
    int respawnCount=0; bool alive=true,enabled=true;
    std::vector<EnemyLootSpawn> loot;
    void dropLoot(JavaRandom&,const PlayerMeters&);
    void resetForRegionExit(); // Non-death reset: no loot or respawn-count decrement.
    bool regionRespawnable() const { return respawnable(); }
    bool grounded=false,ceiling=false,wallLeft=false,wallRight=false,facingLeft=false,touched=false;
    std::array<int,4> collision{},attack{},visual{},activation{},interaction{};
    void update(const EnemyTarget&,bool paused=false,JavaRandom* random=nullptr,const PlayerMeters* meters=nullptr,CombatExchange* exchange=nullptr);
    // Explicit damage entry; effects are queued in original spawn order.
    virtual ~EnemyType1()=default;
    virtual void applyDamage(int amount,bool reaction,const EnemyTarget&,const Weapon0&,JavaRandom&);
    bool receiveWeapon(const EnemyTarget&,const Weapon0&,JavaRandom&);
    std::vector<CombatEffectSpawn> effects;
    int contactDamage=0;
    void advanceAnimation();
    void refreshBounds();
    void scriptAnimation(int id);
    // Script command 106 on a basic enemy enters the ordinary action-change entry (no-op for the current state).
    void scriptAction(int id){change(id);}
    void draw(Canvas&,int cameraX,int cameraY);
    ContactBody body() const;
    void applyContact(const ContactBody&);
    void write(std::ostream&) const;
protected:
    EnemyType1(const BSprite&,const LevelMaps&,const ActorRecord&,const std::vector<int>&,int actorType);
    const BSprite& sprite_; const LevelMaps& maps_; SpriteRenderer renderer_;
    // Type 1 links (record slot 12 > 0) are never respawned; the other enemy types always are.
    bool respawnable() const { return type!=1 || spawnRecord_.parameters[12]<=0; }
    // Called after cleanup/region reset reloads the spawn record, before bounds are final.
    virtual void finishReinit() {}
    int hurtAnimation_=3,deathOffset_=20;
    int variant_=0,head_=0,belowHead_=0,feet_=0,aboveFeet_=0,belowFeet_=0,leftFeet_=0,rightFeet_=0;
    int leftSide_=0,rightSide_=0,centerX_=0,centerY_=0;
    bool vertical_=false,dirty_=false;
    ActorRecord spawnRecord_; int spawnHealth_=0,spawnRespawnCount_=0;
    void cleanup(JavaRandom&,const PlayerMeters&);
    bool respawnFade(const EnemyTarget&);
    int tile(int,int) const; int probe(); void resolve(); void slope(); void fall();
    bool ending() const; void request(int); virtual void change(int); void interactionBounds();
};
}
