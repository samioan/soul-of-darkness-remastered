#pragma once
#include "player.h"
#include "enemy_type1.h"
namespace soul {
class BreakableBridge;
class IceProjectile;
struct FallingTarget {
    EnemyType1* enemy=nullptr;BreakableBridge* bridge=nullptr;
    FallingTarget(EnemyType1* p):enemy(p){}
    FallingTarget(BreakableBridge* p):bridge(p){}
};
// Types 33/50, level-0 collision policy. Stable body address is support identity.
class MovingPlatform {
public:
    MovingPlatform(const BSprite&,const LevelMaps&,const ActorRecord&,const std::array<int,256>* trig=nullptr);
    ~MovingPlatform();
    MovingPlatform(const MovingPlatform&)=delete; MovingPlatform& operator=(const MovingPlatform&)=delete;
    MovingPlatform(BSprite&&,const LevelMaps&,const ActorRecord&,const std::array<int,256>* =nullptr)=delete;
    MovingPlatform(const BSprite&&,const LevelMaps&,const ActorRecord&,const std::array<int,256>* =nullptr)=delete;
    MovingPlatform(const BSprite&,LevelMaps&&,const ActorRecord&,const std::array<int,256>* =nullptr)=delete;
    MovingPlatform(const BSprite&,const LevelMaps&&,const ActorRecord&,const std::array<int,256>* =nullptr)=delete;
    int x=0,y=0,fixedX=0,fixedY=0,vx=0,vy=0,ax=0,ay=0,flags=0,uid=0;
    int animation=0,pending=-1,step=0,tick=0,action=0,direction=0,timer=0,delay=0,speed=0,bucket=0;
    bool grounded=false,ceiling=false,wallLeft=false,wallRight=false;
    std::array<int,4> collision{},attack{},visual{},limits{};
    ContactBody surface;
    IceProjectile* attachedIce=nullptr;int attachmentOffset=0;
    std::vector<IceProjectile*> iceBacklinks;
    int type=33,ropeLength=0,pivotX=0,pivotY=0,angle=0,angularSpeed=0,angularDirection=0,angularSign=1,mode=0,contactDamage=0,enemyKills=0;
    std::array<int,12> swing{};
    void update(Player&,bool paused=false,CombatExchange* combat=nullptr,const std::vector<FallingTarget>& targets={});
    void scriptAnimation(int);
    void refreshBounds(); void advanceAnimation(); void draw(Canvas&,int,int,int stateFrame=0);
    void write(std::ostream&) const;
    void writeSwing(std::ostream&) const;
    void resetSwing(); // Original Actor.T; used by controlled reset fixtures.
private:
    const BSprite& sprite_; const LevelMaps& maps_; SpriteRenderer renderer_;
    int head_=0,belowHead_=0,feet_=0,aboveFeet_=0,belowFeet_=0,leftFeet_=0,rightFeet_=0;
    int leftSide_=0,rightSide_=0,centerX_=0,centerY_=0;
    bool vertical_=false,dirty_=false;
    std::array<int,256> trig_{};
    int tile(int,int) const; int probe(); void resolve(bool horizontal=true); void request(int);
    void contact(Player&);
    int cosine(int) const; void swingStep();
    void updateSwing(Player&,CombatExchange*,const std::vector<FallingTarget>&);
    void drawChain(Canvas&,int,int,int);
};
std::array<int,256> loadPlatformTrig(const std::filesystem::path&);
}
