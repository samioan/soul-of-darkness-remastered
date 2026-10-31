#include "enemy_type3.h"
#include "level_descriptor.h"
#include "pickup.h"
#include <algorithm>
#include <cstring>
namespace soul {
namespace {
int jint(std::uint32_t value) { std::int32_t result; std::memcpy(&result,&value,4); return result; }
int add(int a,int b) { return jint(std::uint32_t(a)+std::uint32_t(b)); }
int shift(int value) { return jint(std::uint32_t(value)<<8); }
int sar(int value,unsigned bits) {
    const auto u=static_cast<std::uint32_t>(value);
    return jint((u>>bits)|(value<0 ? (~0u<<(32-bits)) : 0));
}
bool overlap(const std::array<int,4>& a,const std::array<int,4>& b) {
    if((a[0]==a[2] && a[1]==a[3]) || (b[0]==b[2] && b[1]==b[3])) return false;
    return a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1];
}
// Actor.buildInteractionBounds(bY): {0,80,10,10} for the requested orientation.
std::array<int,4> reach(const std::array<int,4>& c,bool left) {
    std::array<int,4> b{};
    if(left) { b[2]=c[0]; b[0]=b[2]-80; } else { b[0]=c[2]; b[2]=b[0]+80; }
    b[1]=c[1]-10; b[3]=c[3]+10; return b;
}
// Actor.cE[] entries for the shipped type-3 variant slots: charger, thrower, runner.
int variantData(int slot) { return slot==7?0:slot==9?2:5; }
}
EnemyType3::EnemyType3(const BSprite& sprite,const LevelMaps& maps,const ActorRecord& r,const std::vector<int>& stats,int level)
    :EnemyType1(sprite,maps,r,stats,3),attachment(throwX) {
    if(LevelDescriptor::get(level).mode==3) throw FormatError("Type-3 actors in paired-player level mode are not ported");
    const auto& p=r.parameters;
    linkUid_=p[12]; variant_=variantData(p[10]); runnerLeft_=!facingLeft;
    snapDown();
    activation={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]}; refreshBounds();
}
void EnemyType3::snapDown() {
    // Actor.snapDownToGround(): probe one fixed unit down, moving whole tiles until grounded.
    for(;;) {
        vy=256; refreshBounds(); const int correction=probe(); const int top=collision[1];
        if(vertical_) {
            if(vy>=0 && feet_>=15) y-=correction;
            else if(head_>=24) { ceiling=true; y+=20-top%20+1; }
        }
        refreshBounds(); vy=0;
        if(grounded) return;
        y+=20;
    }
}
void EnemyType3::finishReinit() {
    const auto& p=spawnRecord_.parameters;
    runnerLeft_=!facingLeft; snapDown();
    activation={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]}; refreshBounds();
    // initializeFromRecord re-enters the respawn fade whenever the stat row allows respawns and the record has flag 128.
    if(spawnRespawnCount_!=0 && (flags&128)) { action=5; flags&=~1024; flags|=128; timer=1500; }
}
void EnemyType3::removeAttachments() {
    // Game.removeActor clears the attached actor without any holder notification.
    attachment.clear();
    if(chest) { chest->flags&=~256; chest=nullptr; }
}
Type3DecisionState EnemyType3::state() const {
    Type3DecisionState s; s.action=action; s.timer=timer; s.throwX=throwX; s.vx=vx; s.vy=vy; s.ax=ax; s.ay=ay; s.facingLeft=facingLeft; return s;
}
void EnemyType3::store(const Type3DecisionState& s) {
    action=s.action; timer=s.timer; throwX=s.throwX; vx=s.vx; vy=s.vy; ax=s.ax; ay=s.ay; facingLeft=s.facingLeft;
}
Type3DecisionInput EnemyType3::input(const EnemyTarget& p) const {
    Type3DecisionInput in;
    in.variant=behavior(); in.fixedX=fixedX; in.fixedY=fixedY; in.playerFixedX=p.fixedX; in.playerFixedY=p.fixedY;
    in.x=x; in.playerX=p.x; in.runnerLeft=runnerLeft_; in.grounded=grounded; in.wall=wallLeft || wallRight; in.animationEnding=ending();
    in.activated=overlap(p.collision,activation);
    in.chargeReachLeft=overlap(p.collision,reach(collision,true)); in.chargeReachRight=overlap(p.collision,reach(collision,false));
    in.attachment=attachment.object ? (attachment.object->action==101 ? Type3Attachment::HeldShot : Type3Attachment::OtherShot)
        : chest ? Type3Attachment::Chest : Type3Attachment::None;
    return in;
}
void EnemyType3::change(int next) {
    if(action==next) return;
    if(!target_) throw FormatError("Type-3 action entry needs the player target");
    auto s=state(); const auto e=enterType3Action(s,next,input(*target_)); store(s);
    if(e.animation) {
        if(e.restartAnimation && *e.animation==animation) { pending=*e.animation; flags&=~64; dirty_=true; }
        else request(*e.animation);
    }
}
void EnemyType3::linkChest(const Type3Services& svc) {
    if(linkUid_==-1 || attachment.object || chest) return;
    Container* found=svc.findChest ? svc.findChest(linkUid_) : nullptr;
    if(!found) return;
    chest=found; chest->flags|=256;
    if(chest->flags&512) flags|=512;
    chest->flags|=32;
}
void EnemyType3::detachChest() { chest=nullptr; }
void EnemyType3::releaseHeldShot() {
    if(attachment.object && attachment.object->action==101) attachment.object->release(0,0);
}
bool EnemyType3::applyEventsAndReportDeath(const Type3DecisionEvents& e,const Type3Services& svc) {
    if(e.animation) {
        if(e.restartAnimation && *e.animation==animation) { pending=*e.animation; flags&=~64; dirty_=true; }
        else request(*e.animation);
    }
    if(e.spawnShot) {
        if(!svc.shots) throw FormatError("Type-3 held shot needs the world shot collection");
        svc.shots->spawnHeld(attachment,x,y,e.spawnFacingLeft,e.spawnOffsetX,true);
        flags&=~16;
    }
    if(e.releaseShot && attachment.object && attachment.object->action==101) attachment.object->release(e.releaseX,e.releaseY);
    if(e.dropChest && chest) { chest->release(); chest=nullptr; }
    return e.resetAfterDeath;
}
void EnemyType3::update(const EnemyTarget& player,bool paused,JavaRandom* random,const PlayerMeters* meters,CombatExchange* exchange,const Type3Services& svc) {
    loot.clear();
    if(!alive || paused || (flags&(1024|16384))) return;
    fixedX=add(add(fixedX,shift(add(x,-sar(fixedX,8)))),vx);
    fixedY=add(add(fixedY,shift(add(y,-sar(fixedY,8)))),vy);
    vx=add(vx,ax); vy=add(vy,ay); ax=ay=0; x=sar(fixedX,8); y=sar(fixedY,8);
    if(damageCooldown>=0) damageCooldown=add(damageCooldown,-83);
    target_=&player;
    const auto finish=[&] { if(dirty_ && alive) refreshBounds(); if(facingLeft) flags|=1; else flags&=~1; };
    linkChest(svc);
    const int before=action; auto s=state(); const auto events=decideType3(s,input(player)); store(s);
    if(before==101) interaction=reach(collision,facingLeft);
    if(applyEventsAndReportDeath(events,svc)) {
        if(!random || !meters) throw FormatError("Enemy death cleanup needs the world random stream and meters");
        cleanup(*random,*meters); if(!alive) removeAttachments(); finish(); return;
    }
    if(respawnFade(player)) { finish(); return; }
    // Non-player actor separation (Actor.resolveActorSeparation) is not owned natively; see NATIVE_TYPE3_ACTOR.md.
    const int side=facingLeft?-5:5;
    if(attachment.object && attachment.object->action==101) attachment.object->placeHeld(x+side,y-31,facingLeft);
    else if(chest) chest->carry(x+side,y-31,facingLeft);
    gravity(action!=0 && (variant_&1)==1 ? true : grounded ? false : true);
    if(behavior()==0 && overlap(player.collision,collision)) {
        if(attachment.object && attachment.object->action==101) attachment.object->explode();
        if(!random || !meters) throw FormatError("Enemy death cleanup needs the world random stream and meters");
        cleanup(*random,*meters); if(!alive) removeAttachments();
    } else if(exchange) {
        if(receiveWeapon(player,{},*exchange->random)) ++*exchange->enemyHits;
        else if(overlap(player.collision,attack) && exchange->player->receiveHit(contactDamage,x,*exchange->damage,
            exchange->meters->health,exchange->meters->maxHealth,*exchange->random,*exchange->effects)) ++*exchange->playerHits;
    } else if(overlap(player.attack,collision) || overlap(player.collision,attack))
        throw FormatError("Live enemy/player combat dispatch is not integrated");
    finish();
}
void EnemyType3::gravity(bool flagBit) {
    resolve(); const bool incline=feet_>=16 && feet_<=21; slope();
    if(!incline && ((vx<0 && leftFeet_<15) || (vx>0 && rightFeet_<15))) {
        if(!flagBit) vx=0; else if(!grounded) fall();
    }
    if(vy==0 && !grounded) fall();
}
void EnemyType3::dispatchAttached(const EnemyTarget& player,bool paused,const Type3Services& svc) {
    if(attachment.object) attachment.object->update(player,svc.hitActors,svc.hitPlayer,paused);
    if(chest) chest->carriedUpdate(paused);
}
void EnemyType3::applyDamage(int amount,bool reaction,const EnemyTarget& player,const Weapon0& weapon,JavaRandom& random) {
    target_=&player;
    EnemyType1::applyDamage(amount,reaction,player,weapon,random);
    if(health<=0) releaseHeldShot();
}
void EnemyType3::write(std::ostream& out) const {
    for(int n:{fixedX,fixedY,x,y,vx,vy,ax,ay,flags,animation,pending,step,tick,action}) out << '\t' << n;
    for(bool b:{grounded,ceiling,wallLeft,wallRight}) out << '\t' << (b?"true":"false");
    for(const auto& r:{collision,attack,visual}) for(int n:r) out << '\t' << n;
    for(int n:{timer,throwX,damageCooldown,health,int(facingLeft),int(touched)}) out << '\t' << n;
    for(const auto& r:{activation,interaction}) for(int n:r) out << '\t' << n;
    out << '\t' << respawnCount << '\t' << attachedType() << '\t' << int(alive);
}
}
