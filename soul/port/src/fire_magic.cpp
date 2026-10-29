#include "fire_magic.h"
#include <algorithm>
namespace soul {
FirePulse::FirePulse(const BSprite& s,const Player& p,int i,int n):x(p.x),y(p.y-22),flags(4112|int(p.facingLeft)),index(i),count(n),sprite_(s),renderer_(s){refreshBounds();}
bool FirePulse::ending()const{const auto& a=sprite_.animations().at(animation);const int d=sprite_.animationFrames().at(a.start+step).duration;return step==a.count-1 && (d<=1 || tick==d-1);}
void FirePulse::refreshBounds(){
    const auto& a=sprite_.animations().at(animation);const auto& af=sprite_.animationFrames().at(a.start+step);
    auto rect=[&](int i){auto r=sprite_.frameBounds(af.frame,i,(flags^af.flags)&3);const bool present=i<0 || i<sprite_.frames().at(af.frame).rectCount;const int dx=present?((flags&1)?-af.x:af.x):0,dy=present?((flags&2)?-af.y:af.y):0;for(int k=0;k<4;++k)r[k]+=k%2?y+dy:x+dx;return r;};
    collision=rect(0);attack=rect(1);visual=rect(-1);
    if(collision[0]==collision[2] && collision[1]==collision[3])collision=visual;
}
void FirePulse::update(const Player& p,const std::array<int,256>& trig,const std::function<void(const FirePulse&)>& hit){
    if(!alive || (flags&(1024|16384)))return;
    fixedX=x*256;fixedY=y*256;timer-=83;refreshBounds();
    if(animation==16){
        if(radius<30){radius=30;angle+=10;timer=0;}else{radius+=3;angle+=3;}
        const int degrees=index*360/count+angle;
        const auto cosine=[&](int d){d%=360;if(d<0)d+=360;return trig[d*256/360];};
        x=p.x+(radius*cosine(degrees)>>8);y=p.y+(radius*cosine(90-degrees)>>8)-22;
        if(timer<=0){pending=17;flags&=~(64|128);}
        hit(*this);refreshBounds();
    }else if(animation==17){if(ending()){alive=false;collision={};attack={};visual={};}else hit(*this);}
    else throw FormatError("Unsupported player fire pulse animation");
}
void FirePulse::advance(){if(alive)refreshBounds();if(!alive || (flags&(64|1024)))return;if(pending!=-1){animation=pending;pending=-1;step=tick=0;}else{const auto& a=sprite_.animations().at(animation);const int d=sprite_.animationFrames().at(a.start+step).duration;if(d && ++tick>=d){tick=0;if(++step>=a.count)step=0;}}}
void FirePulse::draw(Canvas& c,int cx,int cy){if(alive && !(flags&(128|1024)))renderer_.drawAnimation(c,animation,step,x-cx,y-cy,{0,static_cast<unsigned>(flags)&3,{}});}
void FirePulse::write(std::ostream& out)const{for(int n:{fixedX,fixedY,x,y,flags,animation,pending,step,tick,timer,index,radius,angle,int(alive)})out<<'\t'<<n;for(const auto& r:{collision,attack,visual})for(int n:r)out<<'\t'<<n;}
FireMagic::FireMagic(const BSprite& s,const std::array<int,256>& trig):sprite_(s),trig_(trig){if(s.animations().size()<=17)throw FormatError("Missing fire spell sprite animations");}
void FireMagic::regenerate(PlayerMeters& m,int action){
    if(warningMs>0)warningMs-=83;
    if(!m.magicUnlocked){m.magic=0;return;}regenerationMs+=83;
    if((action==0 || action==2) && m.magic!=m.maxMagic && regenerationMs>=5000){regenerationMs=0;if(m.magic<m.maxMagic)m.magic=std::min(m.maxMagic,m.magic+10);}
}
bool FireMagic::occupied()const{return std::any_of(pulses.begin(),pulses.end(),[](const auto& p){return p.index==0 && p.alive;});}
void FireMagic::charge(Player& p,PlayerMeters& m,JavaRandom& random){
    if(p.animation!=18 || p.magicCharge<0)return;
    if(p.magicCharge<=240){p.magicCharge+=80;const int high=((240-p.magicCharge)>>2)>>1;for(int i=0;i<20;++i)random.range(0,high);return;}
    if(rank<0 || rank>2)throw FormatError("Unsupported fire magic rank");
    if(m.magic>=15){m.magic-=15;regenerationMs=0;p.beginFireBurst();const int count=rank==0?6:9;for(int i=0;i<count;++i)pulses.emplace_back(sprite_,p,i,count);++casts;}
    p.magicCharge=-1;
}
void FireMagic::update(const Player& p,const std::function<void(const FirePulse&)>& hit,bool paused){if(!paused)for(auto& e:pulses)e.update(p,trig_,hit);}
void FireMagic::advance(){for(auto& p:pulses)p.advance();pulses.remove_if([](const auto& p){return !p.alive;});}
void FireMagic::clear(){for(const auto& p:pulses)if(p.alive)++regionRemoved;pulses.clear();}
void FireMagic::draw(Canvas& c,int cx,int cy){for(auto& p:pulses)p.draw(c,cx,cy);}
void FireMagic::drawCharge(Canvas& c,const Player& p,int cx,int cy)const{
    if((p.animation!=18 && p.animation!=23) || p.magicCharge<0 || p.magicCharge>=240)return;
    const int r=240-p.magicCharge,diameter=r*2+1;Pixels pixels(static_cast<std::size_t>(diameter)*diameter);
    for(int y=-r;y<=r;++y)for(int x=-r;x<=r;++x){const int d=x*x+y*y;if(d<=r*r && d>=(r-3)*(r-3))pixels[static_cast<std::size_t>(y+r)*diameter+x+r]=p.animation==23?0xb060d8ff:0xb0ffc050;}
    c.blit(pixels,diameter,diameter,p.x-cx-r+(p.animation==23?(p.facingLeft?-40:40):0),p.y-cy-(p.animation==23?50:30)-r);
}
}
