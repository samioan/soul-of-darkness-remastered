#pragma once
#include "tile_world.h"
#include "actor_contacts.h"
#include <ostream>
namespace soul {
class JavaRandom; struct PlayerDamage; struct CombatEffectSpawn;
class Player;
// Actor.d(int,int[]) subset for moving (33) and swinging (50) surfaces.
// The owner must keep this body at a stable address and detach before removal.
void updatePlatformContact(ContactBody& surface,Player& player,int offset=0);
struct PlayerCombatContext {
    PlayerDamage& damage; int& health; int maximum; JavaRandom& random;
    std::vector<CombatEffectSpawn>& effects; int& hits;
};
struct ActorRecord { std::size_t offset; int count; std::array<std::int16_t,20> parameters; };
std::vector<ActorRecord> actorRecords(ByteView data);
const ActorRecord& playerRecord(const std::vector<ActorRecord>& records);
const ActorRecord& playerRecord(std::vector<ActorRecord>&&)=delete;
// Movement, weapon-0 poses and controlled hit/death response; no world scripts.
class Player {
public:
    Player(const BSprite& sprite,const LevelMaps& maps,const ActorRecord& record,int level=0);
    Player(BSprite&&,const LevelMaps&,const ActorRecord&,int=0)=delete;
    Player(const BSprite&&,const LevelMaps&,const ActorRecord&,int=0)=delete;
    Player(const BSprite&,LevelMaps&&,const ActorRecord&,int=0)=delete;
    Player(const BSprite&,const LevelMaps&&,const ActorRecord&,int=0)=delete;
    int x=0,y=0,fixedX=0,fixedY=0,vx=0,vy=0,ax=0,ay=0,flags=0;
    int animation=0,pending=-1,step=0,animationTick=0,action=0,uid=0;
    int queuedCombo=-1,meleeRecovery=0,meleeTimer=0;
    int availableMagic=0,magicCharge=-1;
    int activeWeapon=0,weaponSwitchCooldown=0;
    bool iceOwned=false,weaponSwitched=false;
    bool fireOccupied=false,fireRequested=false,insufficientMagic=false;
    bool grounded=false,ceiling=false,wallLeft=false,wallRight=false,facingLeft=false;
    bool shortJump=false,clearInputRequested=false;
    bool deathRequested=false,deathComplete=false;
    const ContactBody* platform=nullptr;
    std::array<int,4> damageView{0,0,240,320};
    std::array<int,4> collision{},attack{},visual{};
    void update(unsigned pressed,unsigned held,int stateFrame,bool paused=false,const std::vector<ContactBody*>& bodies={},PlayerCombatContext* combat=nullptr);
    void advanceAnimation();
    void refreshBounds() { bounds(); }
    void scriptAnimation(int id);
    bool receiveBeamHit(int damage,int sourceX,bool forcePush,PlayerDamage&,int& health,int maximum,JavaRandom&,std::vector<CombatEffectSpawn>&);
    bool receiveHit(int damage,int sourceX,PlayerDamage&,int& health,int maximum,JavaRandom&,std::vector<CombatEffectSpawn>&);
    // Crystal contact enters pose 25; the host controls the following cinematic/reward ticks.
    void beginCrystalPickup();
    void beginAbilityPickup();
    void beginFireBurst();
    void beginIceShot();
    void updateCrystalPose(bool cinematic);
    void draw(Canvas&,int cameraX,int cameraY);
    void write(std::ostream& out) const;
    bool meleeHits(const std::array<int,4>& target) const;
    bool stoppedByHazard() const { return hazard_; }
private:
    friend void updatePlatformContact(ContactBody&,Player&,int);
    const BSprite& sprite_; const LevelMaps& maps_; SpriteRenderer renderer_;
    int level_=0,head_=0,belowHead_=0,feet_=0,aboveFeet_=0,belowFeet_=0,leftFeet_=0,rightFeet_=0;
    int leftSide_=0,rightSide_=0,centerX_=0,centerY_=0,walkTicks_=0,fallStart_=0,previousAnimation_=-1;
    bool vertical_=false,dirty_=false,diagonal_=false,hazard_=false;
    bool contactLeft_=false,contactRight_=false;
    bool attackAllowed_=false;
    int tile(int x,int y) const;
    void bounds(); void request(int animation); void immediate(int animation); bool ending() const;
    int probe(); void resolve(); void fall(int velocity=0); void land();
    void alignFall(bool centerX); void slope(); bool headroom(int mask) const;
    void groundInput(unsigned pressed,unsigned held,int stateFrame); void idle(unsigned pressed,unsigned held,int stateFrame);
};
struct MovementInput {
    unsigned pending=0,pressed=0,releases=0,released=0,held=0,frameHeld=0;
    int heldFrames=0;
    void press(unsigned bit);
    void release(unsigned bit) { releases|=bit; held&=~releases; frameHeld&=~releases; }
    void clear() { pending=pressed=releases=released=held=frameHeld=0; }
    void latch(Player& player);
};
}
