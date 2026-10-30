#include "timed_switch.h"
#include "fire_magic.h"
#include <cstring>
namespace soul {
namespace {
bool touches(const std::array<int,4>& a,const std::array<int,4>& b){return (a[0]!=a[2] || a[1]!=a[3]) && (b[0]!=b[2] || b[1]!=b[3]) && a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1];}
}
TimedSwitch::TimedSwitch(const BSprite& sprite,const ActorRecord& r):sprite_(sprite),renderer_(sprite){
    const auto& p=r.parameters;
    if(r.count!=9 || p[0]!=89 || p[6]<0 || p[6]>1 || p[7]<-1 || p[4]<0 || std::size_t(p[4])>=sprite.animations().size() || sprite.animations().size()<28 || (p[5]&~(1|2|8|16|32|64|128|256|1024|16384)))throw FormatError("Unsupported timed switch record/assets");
    uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);flags=p[5]&~64;animation=p[4];mode=p[6];duration=p[7];targetUid=p[8];variant=animation==25?0:1;refreshBounds();
}
bool TimedSwitch::struckBy(const Player& p,const FireMagic* fire)const{
    if(p.activeWeapon==0 && p.meleeHits(collision))return true;
    if(fire)for(const auto& pulse:fire->pulses)if(pulse.alive && touches(pulse.collision,collision))return true;
    return false;
}
bool TimedSwitch::ending()const{const auto& a=sprite_.animations().at(animation);const int d=sprite_.animationFrames().at(a.start+step).duration;return step==a.count-1 && (d<=1 || tick==d-1);}
void TimedSwitch::request(int id){if(id!=animation){pending=id;flags&=~64;refreshBounds();}}
void TimedSwitch::change(int next){if(variant==0)request(next==101?26:25);action=next;}
void TimedSwitch::update(bool struck,const std::function<const int*(int)>& resolve,bool paused){
    if(paused || (flags&(1024|16384)))return;
    const int* target=resolve(targetUid);
    const auto bits=static_cast<std::uint32_t>(timer)-83u;std::memcpy(&timer,&bits,4);
    switch(action){
    case 0:
        if(ending() && animation==27 && variant==0)request(25);
        if(struck){change(101);timer=duration;}return;
    case 101:
        if(struck){if(variant==0)request(26);timer=duration;return;}
        if(duration==-1)return;
        if(mode==1 && !target)throw FormatError("Timed switch requires its linked gate");
        if(mode==0 || *target==102 || *target==105){
            if(timer<=0){change(102);return;}
            if(timer<=1000){if(variant==0)request(27);}
            else if(ending() && variant==0)request(26);
        }else{action=0;if(variant==0)request(27);}return;
    case 102:if(ending()){if(variant==0)request(25);action=0;}return;
    default:throw FormatError("Unsupported timed switch action");
    }
}
void TimedSwitch::refreshBounds(){
    const auto& a=sprite_.animations().at(animation);const auto& af=sprite_.animationFrames().at(a.start+step);
    const auto rect=[&](int index){auto r=sprite_.frameBounds(af.frame,index,(flags^af.flags)&3);const bool present=index<0 || index<sprite_.frames().at(af.frame).rectCount;const int dx=present?((flags&1)?-af.x:af.x):0,dy=present?((flags&2)?-af.y:af.y):0;for(int i=0;i<4;++i)r[i]+=(i%2?y+dy:x+dx);return r;};
    collision=rect(0);attack=rect(1);visual=rect(-1);if(collision[0]==collision[2] && collision[1]==collision[3])collision=visual;
}
void TimedSwitch::advanceAnimation(){
    if(flags&1024)return;refreshBounds();
    if(pending!=-1){animation=pending;pending=-1;step=tick=0;}
    else if(!(flags&64)){const auto& a=sprite_.animations().at(animation);const int d=sprite_.animationFrames().at(a.start+step).duration;if(d && ++tick>=d){tick=0;if(++step>=a.count)step=0;}}
}
void TimedSwitch::draw(Canvas& c,int cx,int cy){if(!(flags&(1024|128)))renderer_.drawAnimation(c,animation,step,x-cx,y-cy,{0,static_cast<unsigned>(flags)&3,{}});}
void TimedSwitch::write(std::ostream& out)const{for(int n:{uid,x,y,flags,animation,pending,step,tick,action,mode,variant,duration,targetUid,timer})out<<'\t'<<n;for(const auto& r:{collision,attack,visual})for(int n:r)out<<'\t'<<n;}
}
