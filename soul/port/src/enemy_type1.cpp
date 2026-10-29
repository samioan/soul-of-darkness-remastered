#include "enemy_type1.h"
#include "pickup.h"
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
bool overlap(const std::array<int,4>& a,const std::array<int,4>& b) {
    if((a[0]==a[2] && a[1]==a[3]) || (b[0]==b[2] && b[1]==b[3])) return false;
    return a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1];
}
}
EnemyType1::EnemyType1(const BSprite& sprite,const LevelMaps& maps,const ActorRecord& r,const std::vector<int>& stats)
    :EnemyType1(sprite,maps,r,stats,1) {}
EnemyType1::EnemyType1(const BSprite& sprite,const LevelMaps& maps,const ActorRecord& r,const std::vector<int>& stats,int actorType)
    :type(actorType),sprite_(sprite),maps_(maps),renderer_(sprite) {
    const auto& p=r.parameters;
    if(r.count<13 || r.count>20 || p[0]!=type || !(p[5]&8) || (p[5]&~(1|2|4|8|16|32|64|1024|2048|16384|(type==2?128:0))) ||
        (type==1?(p[10]<0 || p[10]>3):(p[10]!=4 || p[12]!=-1)) || p[11]<0 || std::size_t(p[11])*3+2>=stats.size() ||
        p[4]<0 || std::size_t(p[4])>=sprite.animations().size() || sprite.animations().size()<std::size_t(type==2?8:4) ||
        !maps.collision || maps.collision->width<=0 || maps.collision->height<=0 || maps.layers[1].height<=0 ||
        maps.collision->width!=maps.layers[1].width || maps.collision->cells.size()!=std::size_t(maps.collision->width)*maps.collision->height)
        throw FormatError("Unsupported enemy record/assets (alternate/linked variants pending)");
    uid=p[1]; x=static_cast<std::uint16_t>(p[2]); y=static_cast<std::uint16_t>(p[3]);
    fixedX=shift(x); fixedY=shift(y); flags=p[4]==0?p[5]:p[5]&~64; facingLeft=(flags&1)!=0; animation=p[4];
    variant_=type==1?p[10]:0; health=stats[p[11]*3]; respawnCount=stats[p[11]*3+2];
    spawnRecord_=r; spawnHealth_=health; spawnRespawnCount_=respawnCount; contactDamage=stats[p[11]*3+1];
    activation={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]}; refreshBounds();
    if(type==2 && respawnCount!=0 && (flags&128)){action=5;flags&=~1024;timer=1500;} 
}
int EnemyType1::tile(int tx,int ty) const {
    if(tx<0 || ty<0 || tx>=maps_.layers[1].width || ty>=maps_.layers[1].height) return 24;
    const auto index=std::size_t(ty)*maps_.layers[1].width+tx;
    if(index>=maps_.collision->cells.size()) throw FormatError("Enemy collision lookup exceeds stored grid");
    return static_cast<std::int8_t>(maps_.collision->cells[index]);
}
void EnemyType1::scriptAnimation(int id){if(id<0 || std::size_t(id)>=sprite_.animations().size())throw FormatError("Missing scripted enemy pose");if(id!=animation){animation=id;pending=-1;step=tick=0;flags&=~64;}refreshBounds();}
void EnemyType1::refreshBounds() {
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
int EnemyType1::probe() {
    refreshBounds(); int correction=0; const int top=collision[1],bottom=collision[3]+1,tx=x/20;
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
void EnemyType1::resolve() {
    const int correction=probe();
    int left=collision[0]-1,right=collision[2]+1,top=collision[1],bottom=std::max(top,collision[3]-10);
    wallLeft=wallRight=ceiling=false; leftSide_=rightSide_=0;
    if (vertical_) {
        if (vy>=0 && feet_>=15) y-=correction;
        else if (head_>=24) { ceiling=true; y+=20-top%20+1; }
    }
    refreshBounds(); left=collision[0]-1; right=collision[2]+1; top=collision[1]; bottom=collision[3]-10;
    const int innerLeft=tile(left/20+1,bottom/20),innerRight=tile(right/20-1,bottom/20);
    for (int row=top/20;row<=bottom/20;++row) {
        if (left<0) { leftSide_=24; wallLeft=true; break; }
        int value=tile(left/20,row);
        if (value>leftSide_) { leftSide_=value; if (value>=24) { wallLeft=true;
            if (row==bottom/20 && (innerLeft==16 || innerLeft==18 || innerLeft==18)) wallLeft=false; } }
        value=tile(right/20,row);
        if (value>rightSide_) { rightSide_=value; if (value>=24) { wallRight=true;
            if (row==bottom/20 && (innerRight==17 || innerRight==20 || innerRight==21)) wallRight=false; } }
        if (wallLeft || wallRight) break;
    }
    refreshBounds();
    if (wallLeft==wallRight) wallLeft=wallRight=false;
    else if (vx<=0) { if (wallLeft) x+=20-(left+20)%20-1; else x-=right%20; probe(); }
    else { if (wallRight) x-=right%20; else x+=20-(left+20)%20-1; probe(); }
    refreshBounds(); centerX_=sar(collision[0]+collision[2],1); centerY_=sar(collision[1]+collision[3],1);
}

void EnemyType1::slope() {
    if (feet_==18 || feet_==19) vy=sar(vx,1);
    else if (feet_==20 || feet_==21) vy=sar(-vx,1);
    else if (feet_==16) vy=vx; else if (feet_==17) vy=-vx; else return;
    if (vy<0) vy=0;
}

bool EnemyType1::ending() const {
    const auto& a=sprite_.animations().at(animation); const auto d=sprite_.animationFrames().at(a.start+step).duration;
    return step==a.count-1 && (d<=1 || tick==d-1);
}
void EnemyType1::request(int id) { if(id!=animation) { pending=id; flags&=~64; dirty_=true; } }
void EnemyType1::change(int id) {
    if(action==id) return;
    switch(id) {
    case 1: case 101: request(1); break;
    case 102: if((variant_>>1)==0) request(2); vx=0; break;
    case 103: request(0); vx=vy=ax=ay=0; timer=(variant_>>1)==1?500:400; break;
    default: throw FormatError("Enemy action outside AI subset");
    }
    action=id;
}
void EnemyType1::fall() {
    request(animation); refreshBounds(); y+=centerY_-sar(collision[1]+collision[3],1);
    ay=768; if(action!=2) change(1);
}
void EnemyType1::interactionBounds() {
    const int reach=(variant_>>1)==1?50:13;
    if(facingLeft) { interaction[2]=collision[0]; interaction[0]=interaction[2]-reach; }
    else { interaction[0]=collision[2]; interaction[2]=interaction[0]+reach; }
    interaction[1]=collision[1]-10; interaction[3]=collision[3]+10;
}
void EnemyType1::update(const EnemyTarget& player,bool paused,JavaRandom* random,const PlayerMeters* meters,CombatExchange* exchange) {
    loot.clear();
    if(!alive || paused || (flags&(1024|16384))) return;
    fixedX=add(add(fixedX,shift(add(x,-sar(fixedX,8)))),vx);
    fixedY=add(add(fixedY,shift(add(y,-sar(fixedY,8)))),vy);
    vx=add(vx,ax); vy=add(vy,ay); ax=ay=0; x=sar(fixedX,8); y=sar(fixedY,8);
    if(damageCooldown>=0) damageCooldown=add(damageCooldown,-83);
    switch(action) {
    case 0: if(overlap(player.collision,activation)) change(101); break;
    case 1: ay=768; if(grounded) { vy=0; change(101); } break;
    case 101: {
        const int delta=add(fixedX,-player.fixedX);
        if(delta>256 || delta< -256) facingLeft=player.x<x;
        vx=delta>256?-256:delta< -256?256:0;
        interactionBounds(); if(overlap(player.collision,interaction)) change(102); break;
    }
    case 3:
        if(!random || !meters) throw FormatError("Enemy death cleanup needs the world random stream and meters");
        cleanup(*random,*meters);
        if(facingLeft) flags|=1; else flags&=~1;
        return;
    case 5: case 6: case 7: break;
    case 2: timer=add(timer,-83); if(ending()) change(101); break;
    case 102: if(ending()) change(103); break;
    case 103: timer=add(timer,-83); if(timer<=0) change(101); break;
    default: throw FormatError("Enemy action outside the ordinary type-1 subset");
    }
    if(respawnFade(player)) {
        if(dirty_ && alive) refreshBounds();
        if(facingLeft) flags|=1; else flags&=~1;
        return;
    }
    if(action!=0) paletteTimer=add(paletteTimer,-83);
    // No non-player separation is needed with an empty actor queue. Live queue
    // ownership/support and combat are explicit boundaries of this AI slice.
    if(touched) { vx=0; touched=false; }
    if(action!=0) {
        resolve(); const bool incline=feet_>=16 && feet_<=21; slope();
        if(!incline && ((vx<0 && leftFeet_<15) || (vx>0 && rightFeet_<15))) {
            if(!(variant_&1)) vx=0; else if(!grounded) fall();
        }
        if(vy==0 && !grounded) fall();
    }
    if(exchange) {
        if(receiveWeapon(player,{},*exchange->random)) ++*exchange->enemyHits;
        else if(overlap(player.collision,attack) && exchange->player->receiveHit(contactDamage,x,*exchange->damage,
            exchange->meters->health,exchange->meters->maxHealth,*exchange->random,*exchange->effects)) ++*exchange->playerHits;
    } else if(overlap(player.attack,collision) || overlap(player.collision,attack))
        throw FormatError("Live enemy/player combat dispatch is not integrated");
    if(dirty_) refreshBounds();
    if(facingLeft) flags|=1; else flags&=~1;
}
void EnemyType1::dropLoot(JavaRandom& random,const PlayerMeters& m) {
    if(m.maxHealth<=0 || m.maxMagic<=0) throw FormatError("Invalid loot meter maxima");
    auto mul=[](int a,int b) { return javaInt(std::uint32_t(a)*std::uint32_t(b)); };
    int percent=mul(m.health,100)/m.maxHealth;
    const int chance=add(add(10000,mul(percent,percent)),-sar(mul(percent,1500),3));
    if(random.range(0,10000)<chance) {
        const int dx=random.range(-40,40),dy=random.range(-20,-100);
        loot.push_back({0,add(x,dx),add(y,dy),int(facingLeft)});
    }
    if(m.magic<=mul(m.maxMagic,40)/100 && m.magicUnlocked) {
        percent=add(100,-mul(m.magic,60)/m.maxMagic);
        if(random.range(0,100)<percent) {
            const int dx=random.range(-40,40),dy=random.range(-20,-100);
            loot.push_back({2,add(x,dx),add(y,dy),int(facingLeft)});
        }
    }
}
void EnemyType1::resetForRegionExit() {
    enabled=false; action=0; vx=vy=ax=ay=0;
    const bool keepFlag=(flags&512)!=0;
    const auto& p=spawnRecord_.parameters;
    if(uid>=0 && p[12]<=0) {
        x=static_cast<std::uint16_t>(p[2]); y=static_cast<std::uint16_t>(p[3]); fixedX=shift(x); fixedY=shift(y);
        facingLeft=(p[5]&1)!=0; flags=p[5];
        if(animation!=p[4]) { animation=p[4]; step=tick=0; pending=-1; flags&=~64; }
        health=spawnHealth_; respawnCount=spawnRespawnCount_; enabled=true;
        activation={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]}; interaction={}; refreshBounds();
    } else { alive=false; collision={}; attack={}; visual={}; }
    if(keepFlag) flags|=512;
    flags|=1024;
}
void EnemyType1::cleanup(JavaRandom& random,const PlayerMeters& meters) {
    dropLoot(random,meters); enabled=false; respawnCount=add(respawnCount,-1);
    const int remaining=std::max(-1,respawnCount);
    const bool fade=(flags&4) || remaining>=0,keepFlag=(flags&512)!=0;
    action=0; vx=vy=ax=ay=0;
    const auto& p=spawnRecord_.parameters;
    if(uid>=0 && p[12]<=0) {
        x=static_cast<std::uint16_t>(p[2]); y=static_cast<std::uint16_t>(p[3]); fixedX=shift(x); fixedY=shift(y);
        facingLeft=(p[5]&1)!=0; flags=p[5];
        if(animation!=p[4]) { animation=p[4]; step=tick=0; pending=-1; flags&=~64; }
        health=spawnHealth_; respawnCount=remaining; enabled=true;
        activation={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]}; interaction={}; refreshBounds();
        if(fade) flags&=~32;
    } else { alive=false; collision={}; attack={}; visual={}; }
    if(keepFlag) flags|=512;
    flags|=1024;
    if((flags&2048) && !fade){alive=false;collision={};attack={};visual={};}
    if(fade) { action=5; flags&=~1024; flags|=128; timer=1500; }
}
bool EnemyType1::respawnFade(const EnemyTarget& player) {
    switch(action) {
    case 5: timer=add(timer,-83); if(timer<=800) action=6; return true;
    case 6:
        effects.push_back({28,x,y-(collision[3]-collision[1])/2,int(facingLeft),0,0}); action=7; return true;
    case 7:
        timer=add(timer,-83); if(timer<=0) { activation=player.collision; action=0; flags&=~128; }
        return true;
    default: return false;
    }
}
void EnemyType1::applyDamage(int amount,bool reaction,const EnemyTarget& player,const Weapon0& weapon,JavaRandom& random) {
    effects.push_back({29,x,y-50,int(facingLeft),1000,amount});
    const int adjusted=weapon.adjusted(amount); health=add(health,javaInt(0u-std::uint32_t(adjusted)));
    effects.push_back({2,x,y-random.range(10,50),int(!facingLeft),0,0});
    if(health>0) {
        if(reaction) {
            if(animation==(type==2?7:3)) { pending=type==2?7:3; flags&=~64; dirty_=true; } else request(type==2?7:3);
            facingLeft=player.x<x; vx=vy=ax=ay=0;
            const int delta=add(fixedX,javaInt(0u-std::uint32_t(player.fixedX)));
            vx=delta>256?256:delta< -256?-256:0; action=2;
        }
        damageCooldown=200;
    } else {
        if(action!=3) effects.push_back({3,x,y-(type==2?5:20),0,0,0});
        action=3;
    }
}
bool EnemyType1::receiveWeapon(const EnemyTarget& player,const Weapon0& weapon,JavaRandom& random) {
    if(action==3 || damageCooldown>0 || (flags&(16384|1024)) || !overlap(player.attack,collision)) return false;
    applyDamage(weapon.roll(random),true,player,weapon,random); return true;
}
void EnemyType1::advanceAnimation() {
    if(pending!=-1) { animation=pending; pending=-1; step=tick=0; }
    else if(!(flags&64)) {
        const auto& a=sprite_.animations().at(animation); const int d=sprite_.animationFrames().at(a.start+step).duration;
        if(d && ++tick>=d) { tick=0; if(++step>=a.count) step=0; }
    }
}
ContactBody EnemyType1::body() const {
    ContactBody b; b.type=type; b.action=action; b.animation=animation; b.flags=flags; b.x=x; b.y=y; b.vx=vx;
    b.enabled=enabled && alive; b.touched=touched; b.grounded=grounded; b.wallLeft=wallLeft; b.wallRight=wallRight;
    b.localBounds=contactBounds(sprite_,animation,step,flags); b.collision=collision; return b;
}
void EnemyType1::applyContact(const ContactBody& b) { x=b.x; y=b.y; touched=b.touched; collision=b.collision; }
void EnemyType1::draw(Canvas& c,int cx,int cy) {
    if(alive && !(flags&(128|1024))) renderer_.drawAnimation(c,animation,step,x-cx,y-cy,{0,static_cast<unsigned>(flags)&3,{}});
}
void EnemyType1::write(std::ostream& out) const {
    for(int n:{fixedX,fixedY,x,y,vx,vy,ax,ay,flags,animation,pending,step,tick,action}) out << '\t' << n;
    for(bool b:{grounded,ceiling,wallLeft,wallRight}) out << '\t' << (b?"true":"false");
    for(const auto& r:{collision,attack,visual}) for(int n:r) out << '\t' << n;
    for(int n:{timer,paletteTimer,damageCooldown,health,int(facingLeft),int(touched)}) out << '\t' << n;
    for(const auto& r:{activation,interaction}) for(int n:r) out << '\t' << n;
}
}
