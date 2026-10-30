#include "breakable_bridge.h"
#include <cstdlib>
namespace soul {
BreakableBridge::BreakableBridge(const BSprite& sprite,LevelMaps& maps,const ActorRecord& r)
    :sprite_(sprite),maps_(maps),renderer_(sprite) {
    const auto& p=r.parameters;
    if(r.count<10 || r.count>20 || p[0]!=45 || p[5]!=8 || p[4]<0 || p[4]>1 || p[8]<40 || p[8]%40 || p[9]<=0 || sprite.animations().size()<2 || sprite.animations()[1].count<5)
        throw FormatError("Unsupported breakable bridge record/assets");
    uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);flags=p[5];animation=p[4];
    collision={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};
}
void BreakableBridge::update(bool paused) {
    if(!alive || paused || (flags&(1024|16384)))return;
    switch(action) {
    case 0:idleTimer+=83;if(idleTimer>=1660)idleTimer=0;maps_.fillCollisionRect(collision,24);return;
    case 100:timer+=83;if(timer>=(animation==0?498:1660))action=101;return;
    case 101:alive=false;collision={};return;
    default:throw FormatError("Unsupported breakable bridge action");
    }
}
void BreakableBridge::activate(int sourceX) {
    if(!alive || (flags&1024) || action!=0)return;
    impactX=sourceX;maps_.fillCollisionRect(collision,0);action=100;
}
bool BreakableBridge::hitByChandelier(const std::array<int,4>& r,int sourceX) {
    if(!alive || (flags&1024) || action!=0 || r[0]>collision[2] || r[2]<collision[0] || r[1]>collision[3] || r[3]<collision[1]-17)return false;
    activate(sourceX);return true;
}
bool BreakableBridge::hitByThrown(const std::array<int,4>& r){
    if(!alive || (flags&1024) || animation!=0 || (r[0]==r[2] && r[1]==r[3]) || r[0]>collision[2] || r[2]<collision[0] || r[1]>collision[3] || r[3]<collision[1])return false;
    maps_.fillCollisionRect(collision,0);action=100;return true;
}
void BreakableBridge::advanceAnimation() {
    if(!alive || (flags&1024))return;
    if(pending!=-1){animation=pending;pending=-1;step=tick=0;}
    else if(!(flags&64)){const auto& a=sprite_.animations().at(animation);const int d=sprite_.animationFrames().at(a.start+step).duration;if(d && ++tick>=d){tick=0;if(++step>=a.count)step=0;}}
}
void BreakableBridge::draw(Canvas& canvas,int cx,int cy) {
    if(!alive || (flags&(1024|128)) || action==101)return;
    for(int i=0;i<(collision[2]-collision[0])/40;++i) {
        const int px=x-cx+40*i,py=y-cy;
        if(action==0){renderer_.drawAnimation(canvas,animation,0,px,py,{0,static_cast<unsigned>(flags)&3,{}});continue;}
        const int distance=std::abs(impactX-(collision[0]+40*i))/40;
        int phase=(timer/83)>>1,dy=0;
        if(phase-distance<0)renderer_.drawAnimation(canvas,animation,0,px,py,{0,static_cast<unsigned>(flags)&3,{}});
        else phase-=distance;
        if(phase>=4){dy=(phase-4)*20+100;phase=(phase-4)%3+2;}
        renderer_.drawAnimation(canvas,1,phase,px,py+dy);
    }
}
void BreakableBridge::write(std::ostream& out) const {
    for(int n:{uid,x,y,flags,animation,pending,step,tick,action,timer,idleTimer,impactX})out<<'\t'<<n;
    out<<'\t'<<int(alive);for(int n:collision)out<<'\t'<<n;
}
}
