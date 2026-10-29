#include "linked_gate.h"
#include <cstring>
namespace soul {
namespace {
int jint(std::uint32_t u){std::int32_t i;std::memcpy(&i,&u,4);return i;}
int add(int a,int b){return jint(std::uint32_t(a)+std::uint32_t(b));}
int shift(int a){return jint(std::uint32_t(a)<<8);}
int sar(int a){return jint((std::uint32_t(a)>>8)|(a<0?0xff000000u:0));}
}
LinkedGate::LinkedGate(const BSprite& sprite,LevelMaps& maps,const ActorRecord& r):sprite_(sprite),maps_(maps),renderer_(sprite) {
    const auto& p=r.parameters;
    if(r.count!=9 || p[0]!=31 || (p[5]&~(1|2|16|32|64|128|256|1024|16384)) || p[4]<0 || std::size_t(p[4])>=sprite.animations().size() ||
        (p[6]!=1 && p[6]!=2 && p[6]!=4 && p[6]!=8 && p[6]!=9))throw FormatError("Unsupported linked gate record/assets");
    uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);fixedX=shift(x);fixedY=shift(y);flags=p[5]&~64;animation=p[4];direction=p[6];
    variant=animation==4 || animation==14?4:5;remaining=20*variant;originX=x;originY=y;links_={p[7],p[8]};
    refreshBounds();doorway=collision;if(direction==9){action=104;originY+=collision[3]-collision[1];}
    maps_.fillCollisionRect(doorway,24);
}
void LinkedGate::refreshBounds() {
    const auto& a=sprite_.animations().at(animation);const auto& af=sprite_.animationFrames().at(a.start+step);
    auto rect=[&](int index){
        auto r=sprite_.frameBounds(af.frame,index,(flags^af.flags)&3);
        const bool present=index<0 || index<sprite_.frames().at(af.frame).rectCount;
        const int dx=present?((flags&1)?-af.x:af.x):0,dy=present?((flags&2)?-af.y:af.y):0;
        for(int i=0;i<4;++i)r[i]+=i%2?y+dy:x+dx;return r;
    };
    collision=rect(0);visual=rect(-1);if(collision[0]==collision[2] && collision[1]==collision[3])collision=visual;
}
void LinkedGate::immediate(int id) {
    if(id<0 || std::size_t(id)>=sprite_.animations().size())throw FormatError("Gate script animation is out of range");
    if(id!=animation){animation=id;step=tick=0;pending=-1;flags&=~64;}
}
void LinkedGate::fillDoorway() {
    refreshBounds();auto r=collision;
    if(direction==1)r[0]=doorway[0];if(direction==2)r[2]=doorway[2];if(direction==4)r[1]=doorway[1];if(direction==8)r[3]=doorway[3];
    if(direction!=9)maps_.fillCollisionRect(doorway,0);
    maps_.fillCollisionRect(r,24);
}
void LinkedGate::update(const std::function<const int*(int)>& resolve,bool paused) {
    if(paused || (flags&(1024|16384)))return;
    fixedX=add(add(fixedX,shift(add(x,-sar(fixedX)))),vx);fixedY=add(add(fixedY,shift(add(y,-sar(fixedY)))),vy);x=sar(fixedX);y=sar(fixedY);
    for(std::size_t i=0;i<2;++i)if(links_[i]!=-1 && !targets_[i])targets_[i]=resolve(links_[i]);
    switch(action) {
    case 0:if(targets_[0] && *targets_[0]==101 && (!targets_[1] || *targets_[1]==101))action=102;break;
    case 102:
        moving=collisionMode=true;fillDoorway();
        if(remaining>0){remaining-=4;if(direction>=4)vy=direction==4?-1024:1024;else vx=direction==1?-1024:1024;}
        else{vx=vy=0;action=105;}break;
    case 103:vx=vy=0;maps_.fillCollisionRect(doorway,0);break;
    case 104:vx=vy=0;fillDoorway();break;
    case 105:if(targets_[0] && (*targets_[0]!=101 || (targets_[1] && *targets_[1]!=101)))action=106;break;
    case 106:
        if(direction>=4){if(y!=originY){y=originY;remaining=20*variant;action=107;}}
        else if(x!=originX){x=originX;remaining=20*variant;action=107;}
        if(y==originY && x==originX)action=107;
        fillDoorway();break;
    case 107:action=0;moving=false;break;
    default:throw FormatError("Unsupported linked gate action");
    }
}
void LinkedGate::advanceAnimation() {
    if(flags&1024)return;
    refreshBounds();
    if(pending!=-1){animation=pending;pending=-1;step=tick=0;}
    else if(!(flags&64)){const auto& a=sprite_.animations().at(animation);const int d=sprite_.animationFrames().at(a.start+step).duration;if(d && ++tick>=d){tick=0;if(++step>=a.count)step=0;}}
}
void LinkedGate::draw(Canvas& c,int cx,int cy) {
    if(flags&(1024|128))return;
    const auto clip=c.clip();const int d=direction==9?4:direction;
    const int boundary=(d<=2?originX:originY)+((d==2 || d==8)?20*variant:0);
    if(d==1)c.intersectClip(boundary-cx,clip.top,clip.right-(boundary-cx),clip.bottom-clip.top);
    if(d==2)c.intersectClip(clip.left,clip.top,boundary-cx-clip.left,clip.bottom-clip.top);
    if(d==4)c.intersectClip(clip.left,boundary-cy,clip.right-clip.left,clip.bottom-(boundary-cy));
    if(d==8)c.intersectClip(clip.left,clip.top,clip.right-clip.left,boundary-cy-clip.top);
    renderer_.drawAnimation(c,animation,step,x-cx,y-cy,{0,static_cast<unsigned>(flags)&3,{}});
    c.setClip(clip.left,clip.top,clip.right-clip.left,clip.bottom-clip.top);
}
void LinkedGate::write(std::ostream& out) const {
    for(int n:{uid,x,y,fixedX,fixedY,vx,vy,flags,animation,pending,step,tick,action,direction,variant,remaining,originX,originY})out<<'\t'<<n;
    out<<'\t'<<std::boolalpha<<moving<<'\t'<<collisionMode;
    for(int n:collision)out<<'\t'<<n;for(int n:doorway)out<<'\t'<<n;for(auto target:targets_)out<<'\t'<<(target?1:0);
}
}
