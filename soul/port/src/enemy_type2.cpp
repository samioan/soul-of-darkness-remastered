#include "enemy_type2.h"
#include "pickup.h"
#include <algorithm>
#include <cstring>
namespace soul {
namespace {
int jint(std::uint32_t u){std::int32_t n;std::memcpy(&n,&u,4);return n;}
int add(int a,int b){return jint(std::uint32_t(a)+std::uint32_t(b));}
int sub(int a,int b){return jint(std::uint32_t(a)-std::uint32_t(b));}
int mul(int a,int b){return jint(std::uint32_t(a)*std::uint32_t(b));}
int sar(int a,unsigned n){return jint((std::uint32_t(a)>>n)|(a<0?(~0u<<(32-n)):0));}
int shift(int a){return jint(std::uint32_t(a)<<8);}
int absJava(int a){return a<0?sub(0,a):a;}
int divide(int a,int b){return b==-1?sub(0,a):a/b;}
int length(int x,int y){x=absJava(x);y=absJava(y);const int lo=std::min(x,y),hi=std::max(x,y);int n=add(mul(hi,1007),mul(lo,441));if(hi<jint(std::uint32_t(lo)<<4))n=sub(n,mul(hi,40));return sar(add(n,512),10);}
bool overlap(const std::array<int,4>& a,const std::array<int,4>& b){if((a[0]==a[2] && a[1]==a[3]) || (b[0]==b[2] && b[1]==b[3]))return false;return a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1];}
}
EnemyType2::EnemyType2(const BSprite& sprite,const LevelMaps& maps,const ActorRecord& record,const std::vector<int>& stats,const std::array<int,256>* trig)
    :EnemyType1(sprite,maps,record,stats,2),originY(y),behavior_(record.parameters[10]==49?6:0),trig_(trig){
    if(behavior_==6){if(!trig_)throw FormatError("Behavior-6 flyer needs the trigonometry table");hurtAnimation_=8;}
}
// Re-initialization from the record also reloads the vertical origin (Actor.initializeFromRecord type 2); originX is not reloaded.
void EnemyType2::finishReinit(){originY=y;}
void EnemyType2::clampToRegion(const std::array<int,4>& r){
    if(behavior_==6){if(!overlap(collision,r))resetForRegionExit();return;}
    if((r[0]+12>collision[0] && vx<=0) || (r[2]-12<collision[2] && vx>=0))vx=0;
    if((r[1]+12>collision[1] && vy<=0) || (r[3]-12<collision[3] && vy>=0))vy=0;
}
int EnemyType2::sine(int degrees)const{int d=sub(90,degrees);while(d<0)d=add(d,360);return trig_->at(std::size_t(d%360*256/360));}
void EnemyType2::changeFlying(int next){
    if(next==action)return;
    switch(next){
    case 101:request(2);break;
    case 138:if(animation!=2){animation=2;pending=-1;step=tick=0;flags&=~64;}break;
    case 139:if(animation!=4){animation=4;pending=-1;step=tick=0;flags&=~64;}break;
    case 200:request(2);vx=vy=ax=ay=0;timer=500;break;
    case 102:request(4);vx=vy=ax=ay=0;break;
    case 103:request(2);timer=500;break;
    default:throw FormatError("Unsupported flying enemy action entry");
    }
    action=next;
}
void EnemyType2::update(const EnemyTarget& p,bool paused,JavaRandom* random,const PlayerMeters* meters,CombatExchange* exchange){
    loot.clear();if(!alive || paused || (flags&(1024|16384)))return;
    fixedX=add(add(fixedX,shift(sub(x,sar(fixedX,8)))),vx);fixedY=add(add(fixedY,shift(sub(y,sar(fixedY,8)))),vy);
    vx=add(vx,ax);vy=add(vy,ay);ax=ay=0;x=sar(fixedX,8);y=sar(fixedY,8);
    if(damageCooldown>=0)damageCooldown=add(damageCooldown,-83);
    switch(action){
    case 0:if(overlap(p.collision,activation)){
        if(behavior_==6){changeFlying(138);originX=x;originY=y;direction=facingLeft?2:1;flags|=16;}else changeFlying(101);
    }break;
    case 101:{
        facingLeft=sub(fixedX,p.fixedX)>0;
        const int dx=sub(p.fixedX,fixedX),dy=sub(sub(p.fixedY,6400),fixedY);distance=length(dx,dy);
        vx=distance?divide(mul(dx,768),distance):0;vy=distance?divide(mul(dy,768),distance):0;
        if(facingLeft){interaction[2]=collision[0]-15;interaction[0]=interaction[2]-10;}
        else {interaction[0]=collision[2]+15;interaction[2]=interaction[0]+10;}
        interaction[1]=collision[1]+15;interaction[3]=collision[3];
        if(overlap(p.collision,interaction))changeFlying(200);break;
    }
    case 200:timer=add(timer,-83);if(timer<=0)changeFlying(102);break;
    case 102:if(ending())changeFlying(103);break;
    case 103:timer=add(timer,-83);if(timer<=0)changeFlying(101);break;
    case 2:timer=add(timer,-83);if(ending()){if(behavior_==6){originX=x;originY=y;changeFlying(138);}else changeFlying(101);}break;
    case 138:case 139:{
        timer=add(timer,-83);
        if(action==138){
            if(facingLeft){interaction[2]=collision[0]-15;interaction[0]=interaction[2]-10;}
            else {interaction[0]=collision[2]+15;interaction[2]=interaction[0]+10;}
            interaction[1]=collision[1]+15;interaction[3]=collision[3];
            if(overlap(p.collision,interaction))changeFlying(139);
        }else if(action==139 && ending())changeFlying(138);
        if(behavior_!=6)throw FormatError("Flying enemy action outside behavior-zero subset");
        vx=direction==1?768:-768;
        y=add(sar(mul(30,sine(mul(2,sub(originX,x)))),8),originY);
        // Game.au is the active camera region (not the player); leaving it resets the actor without loot or a life.
        if(!activeRegion)throw FormatError("Behavior-6 flyer needs an active camera region");
        if(!overlap(*activeRegion,collision)){flags&=~16;resetForRegionExit();return;}
        break;
    }
    case 3:
        if(!random || !meters)throw FormatError("Flying enemy death requires world random and meters");
        cleanup(*random,*meters);if(facingLeft)flags|=1;else flags&=~1;return;
    case 5:case 6:case 7:break;
    default:throw FormatError("Flying enemy action outside behavior-zero subset");
    }
    if(!respawnFade(p)){
        const bool attacked=overlap(p.attack,collision);
        if(exchange){
            if(receiveWeapon(p,{},*exchange->random))++*exchange->enemyHits;
            if(!attacked && (overlap(p.collision,collision) || overlap(p.collision,attack)) && exchange->player->receiveHit(contactDamage,x,*exchange->damage,exchange->meters->health,exchange->meters->maxHealth,*exchange->random,*exchange->effects))++*exchange->playerHits;
        }else if(attacked || overlap(p.collision,collision) || overlap(p.collision,attack))throw FormatError("Flying enemy contact requires world combat context");
    }
    if(dirty_ && alive)refreshBounds();if(facingLeft)flags|=1;else flags&=~1;
}
}
