#include "player.h"
#include "combat.h"
#include <algorithm>
#include <cstring>

namespace soul {
namespace {
int javaInt(std::uint32_t value) { std::int32_t result; std::memcpy(&result,&value,4); return result; }
int add(int a,int b) { return javaInt(static_cast<std::uint32_t>(a)+static_cast<std::uint32_t>(b)); }
int shift(int value) { return javaInt(static_cast<std::uint32_t>(value)<<8); }
int sar(int value,unsigned bits) {
    const auto u=static_cast<std::uint32_t>(value);
    return javaInt((u>>bits)|(value<0 ? (~0u<<(32-bits)) : 0));
}
}
std::vector<ActorRecord> actorRecords(ByteView bytes) {
    BinaryReader reader(bytes,"actor records"); std::vector<ActorRecord> result; std::array<std::int16_t,20> shared{};
    while (reader.remaining()) {
        const int count=reader.i8(); if (count<6 || count>20) throw FormatError("Invalid actor record short count");
        const auto offset=reader.position();
        for (int i=0;i<count;++i) shared[i]=reader.i16le();
        if (shared[0]<0 || shared[0]>100) throw FormatError("Invalid actor record type");
        result.push_back({offset,count,shared});
    }
    reader.done(); return result;
}
const ActorRecord& playerRecord(const std::vector<ActorRecord>& records) {
    const ActorRecord* found=nullptr;
    for (const auto& r:records) if (r.parameters[0]==0) {
        if (found) throw FormatError("Multiple player records"); found=&r;
    }
    if (!found) throw FormatError("Missing player record"); return *found;
}
Player::Player(const BSprite& sprite,const LevelMaps& maps,const ActorRecord& record,int level)
    : sprite_(sprite),maps_(maps),renderer_(sprite),level_(level) {
    if (level<0 || level>8 || !maps.collision || maps.collision->width<=0 || maps.collision->height<=0 ||
        maps.layers[1].width<=0 || maps.layers[1].height<=0 || maps.collision->cells.size()>16000000 ||
        maps.collision->width!=maps.layers[1].width ||
        maps.collision->cells.size()!=static_cast<std::size_t>(maps.collision->width)*maps.collision->height)
        throw FormatError("Invalid movement collision map");
    const auto& p=record.parameters;
    if (record.count<6 || record.count>20 || p[0]!=0 || (p[5]&8) || p[4]<0 || p[4]>12 ||
        sprite.animations().size()<13) throw FormatError("Unsupported player record/sprite for movement subset");
    uid=p[1]; x=static_cast<std::uint16_t>(p[2]); y=static_cast<std::uint16_t>(p[3]); fixedX=shift(x); fixedY=shift(y);
    flags=p[5]; facingLeft=(flags&1)!=0; animation=-1; immediate(p[4]); bounds();
}
int Player::tile(int tx,int ty) const {
    constexpr int modes[]{0,0,0,3,0,2,0,0,0}; const int mode=modes[level_];
    const auto& grid=*maps_.collision;
    if (tx>=0 && tx<maps_.layers[1].width && ty<maps_.layers[1].height) {
        if (ty<0) return mode==3 ? 0 : 24;
        const auto index=static_cast<std::size_t>(ty)*maps_.layers[1].width+tx;
        // Preserve the original last-row failure instead of padding pack 9.
        if (index>=grid.cells.size()) throw FormatError("Collision lookup exceeds stored grid (pack 9 dimension mismatch)");
        return static_cast<std::int8_t>(grid.cells[index]);
    }
    return mode==2 ? 0 : 24;
}
void Player::bounds() {
    dirty_=false;
    const auto& a=sprite_.animations().at(animation); const auto& af=sprite_.animationFrames().at(a.start+step);
    auto rect=[&](int index) {
        auto r=sprite_.frameBounds(af.frame,index,(flags^af.flags)&3);
        const bool present=index<0 || index<sprite_.frames().at(af.frame).rectCount;
        const int dx=present ? ((flags&1)?-af.x:af.x) : 0,dy=present ? ((flags&2)?-af.y:af.y) : 0;
        for (int i=0;i<4;++i) r[i]=add(r[i],add(i%2?y:x,i%2?dy:dx));
        return r;
    };
    collision=rect(0); attack=rect(1); visual=rect(-1);
    if (collision[0]==collision[2] && collision[1]==collision[3]) collision=visual;
}
void Player::immediate(int id) {
    if (id!=animation) { previousAnimation_=animation; animation=id; step=animationTick=0; pending=-1; flags&=~64; }
}
void Player::request(int id) {
    if (id==4) fallStart_=y;
    if (id!=animation) { previousAnimation_=animation; pending=id; flags&=~64; dirty_=true; }
}
bool Player::ending() const {
    const auto& a=sprite_.animations().at(animation); const auto duration=sprite_.animationFrames().at(a.start+step).duration;
    return step==a.count-1 && (duration<=1 || animationTick==duration-1);
}
void Player::advanceAnimation() {
    if (pending!=-1) { animation=pending; pending=-1; step=animationTick=0; }
    else if (!(flags&64)) {
        const auto& a=sprite_.animations().at(animation); const auto duration=sprite_.animationFrames().at(a.start+step).duration;
        if (duration && ++animationTick>=duration) { animationTick=0; if (++step>=a.count) step=0; }
    }
}
int Player::probe() {
    bounds(); int correction=0; const int top=collision[1],bottom=collision[3]+1,tx=x/20;
    grounded=false; vertical_=vy!=0;
    head_=tile(tx,top/20); belowHead_=tile(tx,top/20+1); feet_=tile(tx,bottom/20); aboveFeet_=tile(tx,bottom/20-1);
    belowFeet_=tile(tx,bottom/20+1); leftFeet_=tile(tx-1,bottom/20); rightFeet_=tile(tx+1,bottom/20);
    if (vy<=-5120 && belowHead_>15 && head_<=15) head_=belowHead_;
    if (head_>=15) vertical_=true;
    if (feet_>=15 || (feet_==0 && aboveFeet_>=24 && bottom-top>=20) ||
        (collision[2]-collision[0]>20 && leftFeet_>=15 && rightFeet_>=15)) {
        correction=bottom%20;
        if (aboveFeet_>=15) { correction+=20; belowFeet_=feet_; feet_=aboveFeet_; }
        if (correction>0) vertical_=true;
        if (head_>=24 && belowHead_>=24) vertical_=false;
        grounded=true;
        if (feet_>=18 && feet_<=21) {
            const int u=sar(x%20,1);
            correction-=feet_==18 ? u : feet_==19 ? u+10 : feet_==20 ? 20-u : 10-u; vertical_=true;
        } else if (feet_==16 || feet_==17) { correction-=feet_==16 ? x%20 : 20-x%20; vertical_=true; }
    }
    return correction;
}
void Player::resolve() {
    const int correction=probe();
    int left=collision[0]-1,right=collision[2]+1,top=collision[1],bottom=std::max(top,collision[3]-10);
    wallLeft=wallRight=ceiling=false; leftSide_=rightSide_=0;
    if (vertical_) {
        if (vy>=0 && feet_>=15) y-=correction;
        else if (head_>=24) { ceiling=true; y+=20-top%20+1; }
    }
    bounds(); left=collision[0]-1; right=collision[2]+1; top=collision[1]; bottom=collision[3]-10;
    const int innerLeft=tile(left/20+1,bottom/20),innerRight=tile(right/20-1,bottom/20);
    for (int row=top/20;row<=bottom/20;++row) {
        if (left<0 && level_!=5) { leftSide_=24; wallLeft=true; break; }
        int value=tile(left/20,row);
        if (value>leftSide_) { leftSide_=value; if (value>=24) { wallLeft=true;
            if (row==bottom/20 && (innerLeft==16 || innerLeft==18 || innerLeft==18)) wallLeft=false; } }
        value=tile(right/20,row);
        if (value>rightSide_) { rightSide_=value; if (value>=24) { wallRight=true;
            if (row==bottom/20 && (innerRight==17 || innerRight==20 || innerRight==21)) wallRight=false; } }
        if (wallLeft || wallRight) break;
    }
    bounds();
    if (wallLeft==wallRight) wallLeft=wallRight=false;
    else if (vx<=0) { if (wallLeft) x+=20-(left+20)%20-1; else x-=right%20; probe(); }
    else { if (wallRight) x-=right%20; else x+=20-(left+20)%20-1; probe(); }
    bounds(); centerX_=sar(collision[0]+collision[2],1); centerY_=sar(collision[1]+collision[3],1);
}
void Player::alignFall(bool horizontal) {
    request(4); bounds();
    if (horizontal) x+=centerX_-sar(collision[0]+collision[2],1);
    y+=centerY_-sar(collision[1]+collision[3],1);
}
void Player::fall(int velocity) { alignFall(false); vy=velocity; ay=768; }
void Player::land() { y=(collision[3]+1)/20*20-1; vy=256; request(y-fallStart_>85?7:6); diagonal_=false; shortJump=false; vx=0; }
void Player::slope() {
    if (feet_==18 || feet_==19) vy=sar(vx,1);
    else if (feet_==20 || feet_==21) vy=sar(-vx,1);
    else if (feet_==16) vy=vx; else if (feet_==17) vy=-vx; else return;
    if (vy<0) vy=0;
}
bool Player::headroom(int mask) const {
    int left=collision[0]+1,right=collision[2]-1,maximum=0;
    if (!(mask&1)) left=x; else if (!(mask&2)) right=x;
    for (int col=left/20;col<=right/20;++col) maximum=std::max(maximum,tile(col,collision[1]/20-1));
    return maximum<=15;
}
void Player::idle(unsigned pressed,unsigned held,int frame) {
    if (!grounded && !platform) { fall(); attackAllowed_=false; return; }
    if (animation==10) {
        vx=0;
        if ((held|pressed)&16388) { if (headroom(3)) request(11); clearInputRequested=true; }
        else if (((held|pressed)&33024) && feet_==15 && !platform) { fall(); y+=20; }
    } else if (animation==12) { vx=0; if (ending()) request(10); }
    else if (animation==11) { vx=0; if (ending()) request(0); }
    else if ((held|pressed)&33024) { request(12); clearInputRequested=true; }
    else if (animation==0 && frame%100==0) request(0);
    else if (animation==0) { if (vx) slope(); if (ending()) request(0); }
    else request(0);
}
void Player::groundInput(unsigned pressed,unsigned held,int frame) {
    attackAllowed_=true;
    if (animation==0) vx=vy=0;
    const bool left=(held&4112)!=0,right=(held&8256)!=0;
    if (left || right) {
        const bool direction=left;
        if (facingLeft==direction) {
            if ((animation==10 || animation==12) && !headroom(left?1:2)) { vx=0; return; }
            vy=0; if (!grounded && !platform) { fall(); attackAllowed_=false; return; }
            if (animation!=1) walkTicks_=0;
            request(1); int speed=vx<0?javaInt(0u-static_cast<std::uint32_t>(vx)):vx;
            speed=speed<1280?1280:speed<2560?std::min(2560,speed+320):2560;
            vx=facingLeft?-speed:speed; slope(); return;
        }
        facingLeft=direction;
    } else if (animation==1 && walkTicks_>=8) { request(0); vx=0; return; }
    idle(pressed,held,frame);
}
void Player::update(unsigned pressed,unsigned held,int frame,bool paused,const std::vector<ContactBody*>& bodies,PlayerCombatContext* combat) {
    fireRequested=insufficientMagic=weaponSwitched=false;
    clearInputRequested=false;
    if (paused || (flags&(1024|16384)) || hazard_) return;
    if(weaponSwitchCooldown>0)weaponSwitchCooldown-=83;
    fixedX=add(fixedX,shift(add(x,-sar(fixedX,8)))); fixedY=add(fixedY,shift(add(y,-sar(fixedY,8))));
    fixedX=add(fixedX,vx); fixedY=add(fixedY,vy); vx=add(vx,ax); vy=add(vy,ay); ax=ay=0;
    x=sar(fixedX,8); y=sar(fixedY,8); if (vy>4608) vy=4608;
    if(deathRequested && animation!=9) { action=0; immediate(9); }
    const auto bodySnapshot=[&] {
        ContactBody p; p.type=0; p.x=x; p.y=y; p.vx=vx; p.grounded=grounded;
        p.wallLeft=wallLeft; p.wallRight=wallRight; p.leftContact=contactLeft_; p.rightContact=contactRight_;
        p.localBounds=contactBounds(sprite_,animation,step,flags); p.collision=collision; return p;
    };
    if(animation!=9 && !bodies.empty()) {
        bounds(); auto p=bodySnapshot(); resolvePlayerContacts(p,bodies);
        x=p.x; contactLeft_=p.leftContact; contactRight_=p.rightContact;
    }
    resolve();
    const bool lethalTile=feet_==2 || head_==2 || aboveFeet_==2;
    const bool damageTile=feet_==3 || head_==3 || aboveFeet_==3;
    if ((lethalTile || damageTile) && !combat) { hazard_=true; return; }
    if (lethalTile) immediate(9);
    else if (damageTile && receiveHit(1,x,combat->damage,combat->health,combat->maximum,combat->random,combat->effects)) ++combat->hits;
    if(animation==25) { updateCrystalPose(false); return; }
    bool jump=false; attackAllowed_=false;
    switch(animation) {
    case 1: ++walkTicks_; [[fallthrough]];
    case 0: case 10: case 11: case 12: groundInput(pressed,held,frame); jump=grounded || platform; break;
    case 2: case 3:
        attackAllowed_=true;
        ay=shortJump?1536:768; if (animation==2) vx=facingLeft?-(shortJump?1536:2048):(shortJump?1536:2048);
        if (head_>=24 && vy<0) fall();
        if (vy>0 && (feet_>=15 || platform) && (!platform || platform->type!=50)) land();
        if (vx && animation!=2) request(2); else if (!vx && animation!=3) request(3);
        if (ending()) { if (vy+ay>0) { alignFall(true); resolve(); } else request(animation); } break;
    case 4: case 5:
        attackAllowed_=true;
        if (animation==4 && ending()) { const int previous=previousAnimation_; request(5); previousAnimation_=previous; }
        if (previousAnimation_==8) ax=1024*(vx>0?-1:1);
        if (feet_<15 && !platform) ay=768; else land(); break;
    case 6: case 7:
        vx=vy=ay=0;
        if (pressed&16388) request(3); else if ((facingLeft && (pressed&2)) || (!facingLeft && (pressed&8))) request(2);
        if (held&127358) { groundInput(pressed,held,frame); jump=grounded || platform; }
        else if (ending()) request(0); break;
    case 8:
        if(vx) ax=1024*(vx>0?-1:1);
        vy=ay=0;
        if(!grounded && !platform) fall();
        if((facingLeft?rightSide_:leftSide_)>=24) vx=0;
        if(ending()) { groundInput(pressed,held,frame); jump=grounded || platform; }
        if(vx>0 && collision[2]>=damageView[2]) { vx=0; x-=collision[2]-damageView[2]; }
        else if(vx<0 && collision[0]<=damageView[0]) { vx=0; x+=damageView[0]-collision[0]; }
        break;
    case 9:
        if(combat && step==1 && animationTick==0) combat->health=0;
        vx=sar(vx,2); vy=ay=0;
        if(ending()) { flags|=64; deathComplete=true; }
        return;
    case 15: case 21:
        if(ending()) { meleeRecovery=0; fall(); ay=768; }
        else vx=vy=0;
        if(grounded) { land(); break; }
        [[fallthrough]];
    case 13: case 14: case 19: case 20: {
        meleeTimer=add(meleeTimer,-83); attackAllowed_=false;
        if(queuedCombo==-1 && (pressed&65568) && ((activeWeapon==0 && (grounded || platform) && animation==13) || (activeWeapon==1 && animation==19)))queuedCombo=activeWeapon==0?14:20;
        const auto& a=sprite_.animations().at(animation);
        if(grounded || platform) {
            if(queuedCombo!=-1) {
                if(step>=a.count-1) { meleeRecovery=-1; request(queuedCombo); queuedCombo=-1; }
            } else if(ending()) { meleeRecovery=0; groundInput(pressed,held,frame); jump=grounded || platform; }
        } else if(step>=a.count-1) {
            meleeRecovery=0;
            if(queuedCombo!=-1) { meleeRecovery=-1; request(queuedCombo); queuedCombo=-1; }
            else { groundInput(pressed,held,frame); jump=grounded || platform; }
        }
        break;
    }
    case 16: case 18: case 22: case 23:
        attackAllowed_=false;
        if(magicCharge<0 && ending()) {groundInput(pressed,held,frame);jump=grounded || platform;}
        vx=0;break;
    default: throw FormatError("Player animation outside movement subset");
    }
    if (clearInputRequested) pressed=held=0;
    if(meleeRecovery!=-1) {
        if(meleeRecovery<((animation==4 || animation==5)?8:3)) { ++meleeRecovery; attackAllowed_=false; }
        else { meleeRecovery=-1; attackAllowed_=true; }
    }
    if(attackAllowed_ && (pressed&65568)) {
        if(sprite_.animations().size()<static_cast<std::size_t>(activeWeapon==0?16:22)) throw FormatError("Missing equipped weapon melee poses");
        if(!grounded && !platform) { request(activeWeapon==0?15:21); vx=vy=0; }
        else if(animation==10 && !headroom(3)) clearInputRequested=true;
        else { request(activeWeapon==0?13:19); vx=vy=0; }
    }else if(attackAllowed_ && (pressed&1) && animation!=10){
        // Magic retains input priority over switching even when its cast is gated.
        if(activeWeapon==0 && !fireOccupied && (grounded || platform)){
            if(availableMagic>=15){request(18);vx=vy=0;magicCharge=0;fireRequested=true;}
            else insufficientMagic=true;
        }else if(activeWeapon==1 && (grounded || platform)){
            if(availableMagic>=15){request(23);vx=vy=0;magicCharge=0;}
            else insufficientMagic=true;
        }
    }else if(attackAllowed_ && (pressed&512) && weaponSwitchCooldown<=0){
        if(activeWeapon==1 || iceOwned){activeWeapon=1-activeWeapon;weaponSwitchCooldown=450;queuedCombo=-1;weaponSwitched=true;}
    }
    if(clearInputRequested) pressed=held=0;
    if (jump && (pressed&16398)) {
        if (animation==10 && !headroom(3)) return;
        shortJump=false;
        if (pressed&16388) { diagonal_=false; request(3); vx=0; vy=-4608; }
        else if (pressed&10) { diagonal_=true; request(2); vx=(pressed&2)?-1536:1536; vy=-4608; }
        if (!(pressed&16388)) facingLeft=!(pressed&8);
        platform=nullptr;
    }
    if (((pressed|held)&12378) && !grounded && !platform && !((animation>=13 && animation<=15) || (animation>=19 && animation<=21))) {
        if ((pressed|held)&4114) { vx=-2048; if (!facingLeft) { facingLeft=true; diagonal_=false; } }
        else if ((pressed|held)&8264) { vx=2048; if (facingLeft) { facingLeft=false; diagonal_=false; } }
    }
    if (!grounded && !diagonal_ && !(held&4114) && !(held&8264)) vx=0;
    if(!bodies.empty()) { auto p=bodySnapshot(); separatePlayerContacts(p,bodies,platform); x=p.x; }
    contactLeft_=contactRight_=false;
    if (dirty_) bounds(); if (facingLeft) flags|=1; else flags&=~1;
}
bool Player::receiveBeamHit(int damage,int sourceX,bool forcePush,PlayerDamage& state,int& health,int maximum,JavaRandom& random,std::vector<CombatEffectSpawn>& effects){
    if(animation==9)return false;
    const bool dealt=animation!=8 && state.apply(health,maximum,damage);
    if(!dealt && !forcePush)return false;
    if(action==0 || action==2)immediate(8);
    if(dealt){const int offset=random.range(10,50);const bool flip=random.nextInt()%2==0;effects.push_back({2,x,y-offset,int(flip?!facingLeft:facingLeft),0,0});}
    facingLeft=sourceX<x;vx=facingLeft?3072:-3072;if(facingLeft?wallRight:wallLeft)vx=0;ax=1024*(vx>0?-1:1);deathRequested=health<=0;return dealt;
}
bool Player::receiveHit(int damage,int sourceX,PlayerDamage& state,int& health,int maximum,JavaRandom& random,std::vector<CombatEffectSpawn>& effects) {
    if(animation==9 || animation==8) return false;
    if(action!=0 && action!=2) throw FormatError("Player hit mode is outside supported combat subset");
    if(!state.apply(health,maximum,damage)) return false;
    immediate(8);
    const int offset=random.range(10,50); const bool flip=random.nextInt()%2==0;
    effects.push_back({2,x,y-offset,int(flip?!facingLeft:facingLeft),0,0});
    facingLeft=sourceX<x; vx=facingLeft?3072:-3072;
    if(facingLeft?wallRight:wallLeft) vx=0;
    ax=1024*(vx>0?-1:1); deathRequested=health<=0; return true;
}
void Player::draw(Canvas& target,int cameraX,int cameraY) {
    renderer_.drawAnimation(target,animation,step,x-cameraX,y-cameraY,{0,static_cast<unsigned>(flags)&3,{}});
}
bool Player::meleeHits(const std::array<int,4>& target) const {
    return ((activeWeapon==0 && animation>=13 && animation<=15) || (activeWeapon==1 && animation>=19 && animation<=21)) && attack[0]<=target[2] && attack[2]>=target[0] &&
        attack[1]<=target[3] && attack[3]>=target[1] && (attack[0]!=attack[2] || attack[1]!=attack[3]) &&
        (target[0]!=target[2] || target[1]!=target[3]);
}
void Player::updateCrystalPose(bool cinematic) {
    if(animation!=25) return;
    if(ending()) { flags|=64; if(!cinematic) request(0); vx=vy=ax=ay=0; }
    bounds();
}
void Player::beginAbilityPickup() {
    if(sprite_.animations().size()<=25)throw FormatError("Missing ability pickup pose");
    if(action==0 || action==2)immediate(25);
    vx=vy=ax=ay=0;
}
void Player::beginFireBurst(){if(sprite_.animations().size()<=18)throw FormatError("Missing fire casting poses");immediate(16);}
void Player::beginIceShot(){if(sprite_.animations().size()<=23)throw FormatError("Missing ice casting poses");immediate(22);}
void Player::beginCrystalPickup() {
    if (sprite_.animations().size()<=25) throw FormatError("Missing crystal pickup pose");
    if (action==0 || action==2) request(25);
    vx=vy=ax=ay=0;
}
void Player::write(std::ostream& out) const {
    for (int value:{fixedX,fixedY,x,y,vx,vy,ax,ay,flags,animation,pending,step,animationTick,action}) out << '\t' << value;
    out << '\t' << std::boolalpha << grounded << '\t' << ceiling << '\t' << wallLeft << '\t' << wallRight;
    for (const auto* rect:{&collision,&attack,&visual}) for (int value:*rect) out << '\t' << value;
}
void MovementInput::press(unsigned bit) {
    pending|=bit;
    if (bit&4112) pending&=~(8256u|16388u|33024u);
    if (bit&8256) pending&=~(4112u|16388u|33024u);
    if (bit&16388) pending&=~(4112u|8256u|33024u);
    if (bit&33024) pending&=~(4112u|8256u|16388u);
    if (bit&65568) pending&=~(4112u|8256u|16388u|33024u);
    frameHeld|=pending; held|=pending;
}
void MovementInput::latch(Player& player) {
    if (released) heldFrames=-1;
    if (frameHeld!=held || !frameHeld) {
        if ((player.animation==2 || player.animation==3) && heldFrames>=0) player.shortJump=heldFrames<=2;
        heldFrames=-1;
    }
    if (frameHeld) ++heldFrames;
    frameHeld=held; pressed=pending; released=releases; pending=releases=0;
}
}
