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
void EnemyType2::changeFlying(int next){
    if(next==action)return;
    switch(next){
    case 101:request(2);break;
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
    case 0:if(overlap(p.collision,activation))changeFlying(101);break;
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
    case 2:timer=add(timer,-83);if(ending())changeFlying(101);break;
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
