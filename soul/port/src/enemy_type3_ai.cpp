#include "enemy_type3_ai.h"
#include <cstdint>
#include <cstring>
namespace soul {
namespace {
int jint(std::uint32_t u){std::int32_t i;std::memcpy(&i,&u,sizeof(i));return i;}
int add(int a,int b){return jint(std::uint32_t(a)+std::uint32_t(b));}
int sub(int a,int b){return jint(std::uint32_t(a)-std::uint32_t(b));}
int neg(int a){return sub(0,a);}
int absJava(int a){return a<0?neg(a):a;}
int toward(const Type3DecisionInput& in,int speed){return sub(in.fixedX,in.playerFixedX)<0?speed:-speed;}
int deadZone(const Type3DecisionInput& in,int speed){return absJava(sub(in.fixedX,in.playerFixedX))>absJava(speed)?toward(in,speed):0;}
std::int64_t sqrtFloor(std::int64_t value){
    // The specialized discriminant is at most 3584^2+1024*INT_MAX.
    std::int64_t lo=0,hi=2097152;
    while(lo+1<hi){const auto mid=(lo+hi)/2;if(mid*mid<=value)lo=mid;else hi=mid;}
    return lo;
}
void stop(Type3DecisionState& s){s.vx=s.vy=s.ax=s.ay=0;}
int resumedAction(const Type3DecisionInput& in){return in.variant==0?101:in.variant==1?(in.attachment==Type3Attachment::None?107:105):108;}
}
int type3ThrowSpeed(int startX,int startY,int targetX,int targetY){
    const std::int64_t dy=sub(startY,targetY);
    const std::int64_t discriminant=3584LL*3584+1024*dy;
    if(discriminant<0)return 0;
    const auto time=(-3584-sqrtFloor(discriminant))/-512;
    return time==0?0:static_cast<int>(std::int64_t(sub(targetX,startX))/time);
}
Type3DecisionEvents enterType3Action(Type3DecisionState& s,int next,const Type3DecisionInput& in){
    Type3DecisionEvents e;if(s.action==next)return e;
    switch(next){
    case 2:e.animation=13;e.restartAnimation=true;s.facingLeft=in.playerX<in.x;stop(s);s.vx=-deadZone(in,256);break;
    case 3:e.deathEffect=true;break;
    case 101:s.facingLeft=sub(in.fixedX,in.playerFixedX)>0;e.animation=10;break;
    case 102:e.animation=15;s.timer=700;stop(s);break;
    case 103:e.animation=12;s.vx=s.facingLeft?-2048:2048;s.timer=1000;break;
    case 104:e.animation=9;s.timer=500;stop(s);break;
    case 105:e.animation=10;s.timer=1500;break;
    case 106:e.animation=14;stop(s);break;
    case 107:e.animation=9;s.timer=1800;stop(s);break;
    case 108:e.animation=12;s.facingLeft=in.runnerLeft;s.vx=in.runnerLeft?-1024:1024;break;
    case 109:e.animation=9;s.facingLeft=!in.runnerLeft;stop(s);break;
    }
    s.action=next;return e;
}
Type3DecisionEvents decideType3(Type3DecisionState& s,const Type3DecisionInput& in){
    Type3DecisionEvents e;
    // Keep attachment operations visible to the owner; they are not world ticks.
    auto attachment=in.attachment;
    const auto spawn=[&]{if(attachment==Type3Attachment::None){e.spawnShot=true;e.spawnFacingLeft=s.facingLeft;e.spawnOffsetX=toward(in,5);attachment=Type3Attachment::HeldShot;}};
    const auto change=[&](int next){auto entry=enterType3Action(s,next,in);if(entry.animation)e.animation=entry.animation;e.restartAnimation=entry.restartAnimation;e.deathEffect=entry.deathEffect;};
    const auto decrement=[&]{s.timer=sub(s.timer,83);};
    switch(s.action){
    case 0:spawn();if(in.activated)change(in.variant==0?101:in.variant==1?105:108);break;
    case 1:s.ay=768;if(in.grounded)change(resumedAction(in));break;
    case 2:decrement();if(in.animationEnding)change(resumedAction(in));break;
    case 3:e.dropChest=attachment==Type3Attachment::Chest;e.releaseShot=attachment==Type3Attachment::HeldShot;e.resetAfterDeath=true;break;
    case 101:s.vx=deadZone(in,512);if(absJava(sub(in.playerFixedX,in.fixedX))>512)s.facingLeft=in.playerX<in.x;if(s.facingLeft?in.chargeReachLeft:in.chargeReachRight)change(102);break;
    case 102:if(in.animationEnding)e.animation=12;decrement();if(s.timer<=0)change(103);break;
    case 103:decrement();if(s.timer<=0)change(104);break;
    case 104:decrement();if(s.timer<=0)change(101);break;
    case 105:{
        spawn();s.facingLeft=in.playerX<in.x;
        const int startX=add(in.fixedX,jint(std::uint32_t(deadZone(in,5))<<8));
        const int speed=type3ThrowSpeed(startX,add(neg(in.fixedY),7936),in.playerFixedX,neg(in.playerFixedY));
        if(attachment==Type3Attachment::HeldShot && speed!=0){
            if(absJava(speed)>2304){s.vx=toward(in,512);e.animation=10;}
            else if(absJava(speed)<1024){s.vx=-toward(in,512);e.animation=11;decrement();if(s.timer<=0){s.throwX=s.facingLeft?-1024:1024;change(106);}}
            else{s.throwX=speed;change(106);}
        }
        break;
    }
    case 106:if(in.animationEnding){e.releaseShot=attachment==Type3Attachment::HeldShot;e.releaseX=s.throwX;e.releaseY=-3584;change(107);}break;
    case 107:s.facingLeft=in.playerX<in.x;decrement();if(attachment==Type3Attachment::None && s.timer<=0){spawn();change(105);}break;
    case 108:s.vx=in.runnerLeft?-1024:1024;if(in.wall)change(109);break;
    }
    return e;
}
}
