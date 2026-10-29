#include "ability_pickup.h"
#include <algorithm>
#include <cstring>
namespace soul {
namespace {
int jint(std::uint32_t u){std::int32_t i;std::memcpy(&i,&u,4);return i;}
int add(int a,int b){return jint(std::uint32_t(a)+std::uint32_t(b));}
int sub(int a,int b){return jint(std::uint32_t(a)-std::uint32_t(b));}
int mul(int a,int b){return jint(std::uint32_t(a)*std::uint32_t(b));}
int shift(int a){return jint(std::uint32_t(a)<<8);}
int sar(int a,unsigned n){return jint((std::uint32_t(a)>>n)|(a<0?(~0u<<(32-n)):0));}
int absJava(int a){return a<0?sub(0,a):a;}
int length(int x,int y){x=absJava(x);y=absJava(y);const int lo=std::min(x,y),hi=std::max(x,y);int v=add(mul(hi,1007),mul(lo,441));if(hi<jint(std::uint32_t(lo)<<4))v=sub(v,mul(hi,40));return sar(add(v,512),10);}
bool overlap(const std::array<int,4>& a,const std::array<int,4>& b){return a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1];}
}
void AbilityInteraction::advance(unsigned pressed){
    if(!active)return;
    if(mode==3){
        clock=++beamTicks;
        if(beamTicks==39){
            if(tutorial>=0){mode=14;tutorialFrames=1;clock=1;}
            else {active=false;mode=-1;inputBlocked=false;}
        }
    }else if(mode==14){
        if(tutorial<0){active=false;mode=-1;inputBlocked=false;}
        else {if(tutorialFrames>8 && (pressed& (327712u|33024u)))tutorial=-1;clock=++tutorialFrames;}
    }
}
AbilityPickup::AbilityPickup(const BSprite& s,int px,int py,bool facing,int anim):x(px),y(py),flags(facing?1:0),animation(anim),facingLeft(facing),sprite_(s),renderer_(s){
    if((animation!=2 && animation!=35) || s.animations().size()<=static_cast<std::size_t>(animation) || !s.animations()[animation].count)throw FormatError("Missing magic ability animation");
    refreshBounds();
}
void AbilityPickup::refreshBounds(){
    if(!alive){collision={};return;}
    const auto& a=sprite_.animations().at(animation);const auto& af=sprite_.animationFrames().at(a.start+step);
    const auto rect=[&](int index){auto r=sprite_.frameBounds(af.frame,index,(flags^af.flags)&3);const bool present=index<0 || index<sprite_.frames().at(af.frame).rectCount;
        const int dx=present?((flags&1)?-af.x:af.x):0,dy=present?((flags&2)?-af.y:af.y):0;
        for(int i=0;i<4;++i)r[i]=add(r[i],add(i%2?y:x,i%2?dy:dx));return r;};
    collision=rect(0);if(collision[0]==collision[2] && collision[1]==collision[3])collision=rect(-1);
}
void AbilityPickup::update(Player& p,PlayerMeters& m,AbilityInteraction& ui,int level,bool paused){
    if(!alive || paused || (ui.active && ui.mode==14) || (flags&(1024|16384)))return;
    fixedX=add(add(fixedX,shift(sub(x,sar(fixedX,8)))),vx);fixedY=add(add(fixedY,shift(sub(y,sar(fixedY,8)))),vy);
    vx=add(vx,ax);vy=add(vy,ay);ax=ay=0;x=sar(fixedX,8);y=sar(fixedY,8);
    switch(action){
    case 0:
        if(overlap(p.collision,collision)){
            action=102;
            if(animation==35 && !m.iceUnlocked){m.iceUnlocked=true;ui.tutorial=53;}
            if(animation==2 && !m.magicUnlocked && level==0){ui.tutorial=61;m.magicUnlocked=true;m.magic=m.maxMagic;}
            ui.tint=1;p.beginAbilityPickup();ui.inputBlocked=true;ui.weaponHidden=true;
        }
        break;
    case 102:
        distance=length(sub(p.fixedX,fixedX),sub(p.fixedY,fixedY));
        if(distance<=40960){vx=distance?mul(sub(p.fixedX,fixedX),2048)/distance:0;vy=distance?mul(sub(p.fixedY,fixedY),2048)/distance:0;}
        if(distance<2048){
            x=p.x;y=p.y;vx=vy=0;facingLeft=p.facingLeft;ui.beamX=p.x;ui.beamY=p.y;
            if(!ui.active){ui.active=true;ui.mode=3;ui.beamTicks=ui.clock=0;}
            action=100;ui.weaponHidden=true;
        }
        break;
    case 100:{
        vy=-2048;const int top=p.y-p.collision[3]+p.collision[1]-15;
        if(y<top){y=top;vy=0;}
        if(!ui.active){alive=false;collision={};}
        break;
    }
    default:throw FormatError("Unsupported magic ability action");
    }
    if(facingLeft)flags|=1;else flags&=~1;
}
void AbilityPickup::advanceAnimation(){
    if(!alive || (flags&64))return;const auto& a=sprite_.animations().at(animation);const int duration=sprite_.animationFrames().at(a.start+step).duration;
    if(duration && ++tick>=duration){tick=0;if(++step>=a.count)step=0;}
}
void AbilityPickup::draw(Canvas& c,int cx,int cy){if(alive && !(flags&(128|1024)))renderer_.drawAnimation(c,animation,step,x-cx,y-cy,{0,static_cast<unsigned>(flags)&3,{}});}
void AbilityPickup::write(std::ostream& out)const{
    for(int n:{fixedX,fixedY,x,y,vx,vy,ax,ay,flags,step,tick,action,distance,int(facingLeft),int(alive)})out<<'\t'<<n;
    for(int n:collision)out<<'\t'<<n;
}
}
