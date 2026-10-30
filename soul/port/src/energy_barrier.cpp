#include "energy_barrier.h"
#include <algorithm>
#include <cstdlib>
namespace soul {
std::vector<LightningLine> lightningLines(JavaRandom& random,int top,int height,int x,int width){
    std::vector<LightningLine> lines;int bottom=top+height;
    if(top>320 || bottom<0 || x<=0 || x>width)return lines;
    top=std::max(top,0);bottom=std::min(bottom,320);
    constexpr int scales[]{300,350,400,500,600,700,800,900,1000,1200};
    constexpr std::uint32_t colors[]{0xffff0000u,0xddffffffu,0xdda80010u};
    for(int strand=0;strand<3;++strand){
        const auto color=colors[random.range(0,3)];const int sign=random.range(0,2)==0?-1:1;
        const int length=random.range(5,7),spread=random.range(2,4);int y0=top,x0=x-random.range(0,3),x1=0,y1=top;
        while(y0<bottom-5 || y1<bottom-5){
            y1=y0+length*scales[random.range(0,10)]/1000;x1=x+sign*spread+random.range(-1,2);lines.push_back({color,x0,y0,x1,y1});
            if(random.range(0,4)==0){lines.push_back({color,x1,y1,x1,y1+1});++y1;}
            y0=y1+length*scales[random.range(0,10)]/1000;x0=x-sign*spread+random.range(-1,2);lines.push_back({color,x1,y1,x0,y0});
            if(random.range(0,4)==0){lines.push_back({color,x0,y0,x0,y0+1});++y0;}
        }
    }
    return lines;
}
EnergyBarrier::EnergyBarrier(const BSprite& sprite,const ActorRecord& r):sprite_(sprite),renderer_(sprite),source_(r),visualRandom_(static_cast<unsigned short>(r.parameters[1])){
    const auto& p=r.parameters;
    if(r.count!=7 || p[0]!=80 || p[6]<0 || p[4]<0 || std::size_t(p[4])>=sprite.animations().size() || sprite.animations().size()<32 || (p[5]&~(1|2|8|16|32|64|128|256|512|1024|8192|16384)))throw FormatError("Unsupported energy barrier record/assets");
    initialize();
}
void EnergyBarrier::initialize(bool fresh){
    const auto& p=source_.parameters;uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);flags=p[5];height=p[6];action=0;alive=true;lines_.clear();
    if(fresh || animation!=p[4]){animation=p[4];pending=-1;step=tick=0;flags&=~64;}
    if(fresh)separates=enabled=pushLocked=touched=false;
    const auto& a=sprite_.animations().at(animation);const auto& af=sprite_.animationFrames().at(a.start+step);
    const auto rect=[&](int index){auto r=sprite_.frameBounds(af.frame,index,(flags^af.flags)&3);const bool present=index<0 || index<sprite_.frames().at(af.frame).rectCount;for(int i=0;i<4;++i)r[i]+=i%2?y+(present?((flags&2)?-af.y:af.y):0):x+(present?((flags&1)?-af.x:af.x):0);return r;};
    collision=rect(0);attack=rect(1);visual=rect(-1);if(collision[0]==collision[2] && collision[1]==collision[3])collision=visual;limits=collision;limits[3]+=height;flags|=128;refreshBounds();
}
void EnergyBarrier::refreshBounds(){collision=limits;}
bool EnergyBarrier::ending()const{const auto& a=sprite_.animations().at(animation);const int d=sprite_.animationFrames().at(a.start+step).duration;return step==a.count-1 && (d<=1 || tick==d-1);}
void EnergyBarrier::request(int id){if(id!=animation){pending=id;flags&=~64;refreshBounds();}}
void EnergyBarrier::enable(){flags&=~32;if(action!=5)flags&=~128;}
void EnergyBarrier::clearRespawn(){flags&=~8192;}
void EnergyBarrier::beginRelease(){action=101;request(31);}
void EnergyBarrier::resetForRegionExit(){action=0;enabled=false;const bool keep=(flags&512)!=0;if(flags&8192){initialize(false);if(keep)flags|=512;}else{alive=false;collision=attack=visual={};}flags|=1024;}
void EnergyBarrier::update(bool paused){
    if(!alive || paused || (flags&(1024|16384)))return;
    if(action==0){separates=enabled=pushLocked=true;if(ending()){action=102;request(14);}}
    else if(action==101 && ending()){
        if(flags&8192)flags|=1024;else{alive=false;collision=attack=visual={};}
        separates=enabled=pushLocked=false;lines_.clear();
    }
}
ContactBody EnergyBarrier::body()const{ContactBody b;b.type=80;b.action=action;b.animation=animation;b.flags=flags;b.x=x;b.y=y;b.enabled=enabled && alive;b.pushLocked=pushLocked;b.separates=separates && alive;b.touched=touched;b.absoluteBounds=true;b.localBounds=b.collision=collision;return b;}
void EnergyBarrier::applyContact(const ContactBody& b){x=b.x;y=b.y;touched=b.touched;}
void EnergyBarrier::advanceAnimation(){
    if(!alive || (flags&1024))return;refreshBounds();
    if(pending!=-1){animation=pending;pending=-1;step=tick=0;}
    else if(!(flags&64)){const auto& a=sprite_.animations().at(animation);const int d=sprite_.animationFrames().at(a.start+step).duration;if(d && ++tick>=d){tick=0;if(++step>=a.count)step=0;}}
}
void EnergyBarrier::advanceVisual(bool paused){
    if(paused || !alive || (flags&(1024|128)))return;
    // Cosmetic geometry is cached on ticks; presentation and viewport width never consume gameplay RNG.
    lines_=lightningLines(visualRandom_,0,height,8);for(auto& l:lines_){l.x0-=8;l.x1-=8;}
}
void EnergyBarrier::draw(Canvas& c,int cx,int cy){
    if(!alive || (flags&(1024|128)))return;
    for(const auto& l:lines_){int x0=x-cx+l.x0,y0=y-cy+l.y0,x1=x-cx+l.x1,y1=y-cy+l.y1;const int dx=std::abs(x1-x0),sx=x0<x1?1:-1,dy=-std::abs(y1-y0),sy=y0<y1?1:-1;int error=dx+dy;const Pixels pixel{l.color};
        for(;;){c.blit(pixel,1,1,x0,y0);if(x0==x1 && y0==y1)break;const int twice=2*error;if(twice>=dy){error+=dy;x0+=sx;}if(twice<=dx){error+=dx;y0+=sy;}}
    }
    renderer_.drawAnimation(c,animation,step,x-cx,y-cy+height,{0,static_cast<unsigned>(flags)&3,{}});
    renderer_.drawAnimation(c,animation,step,x-cx,y-cy,{0,static_cast<unsigned>(flags)&3,{}});
}
void EnergyBarrier::write(std::ostream& out)const{for(int n:{uid,x,y,flags,animation,pending,step,tick,action,height,int(alive),int(separates),int(enabled),int(pushLocked)})out<<'\t'<<n;for(const auto& r:{collision,attack,visual,limits})for(int n:r)out<<'\t'<<n;}
}
