#include "container.h"
#include "combat.h"
#include <algorithm>
#include <cstring>
namespace soul {
namespace {
int jint(std::uint32_t u) { std::int32_t i; std::memcpy(&i,&u,4); return i; }
int add(int a,int b) { return jint(std::uint32_t(a)+std::uint32_t(b)); }
int shl(int a) { return jint(std::uint32_t(a)<<8); }
int sar(int a) { return jint((std::uint32_t(a)>>8)|(a<0?0xff000000u:0)); }
bool overlap(const std::array<int,4>& a,const std::array<int,4>& b) {
    return a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1];
}
void clock(const BSprite& s,int animation,int& step,int& tick) {
    const auto& a=s.animations().at(animation); const int duration=s.animationFrames().at(a.start+step).duration;
    if(duration && ++tick>=duration) { tick=0; if(++step>=a.count) step=0; }
}
}
Container::Container(const BSprite& sprite,const BSprite& promptSprite,const LevelMaps& maps,const ActorRecord& r)
    :sprite_(sprite),promptSprite_(promptSprite),maps_(maps),renderer_(sprite),promptRenderer_(promptSprite) {
    const auto& p=r.parameters;
    if(r.count<6 || r.count>20 || p[0]!=52 || p[4]<0 || p[4]>3 || sprite.animations().size()<4 ||
        promptSprite.animations().size()<=25 || !maps.collision || maps.collision->width<=0 || maps.collision->height<=0 ||
        maps.layers[1].height<=0 || maps.collision->width!=maps.layers[1].width ||
        maps.collision->cells.size()!=static_cast<std::size_t>(maps.collision->width)*maps.collision->height)
        throw FormatError("Unsupported level-0 container record or assets");
    uid=p[1]; x=static_cast<std::uint16_t>(p[2]); y=static_cast<std::uint16_t>(p[3]);
    fixedX=shl(x); fixedY=shl(y); animation=p[4]; flags=p[5]&~64; reward=p[6];
    if(reward>=0 && reward<=1 && animation==0) flags|=512;
    bounds();
}
void Container::bounds() {
    const auto& a=sprite_.animations().at(animation); const auto& af=sprite_.animationFrames().at(a.start+step);
    auto rect=[&](int index) {
        auto r=sprite_.frameBounds(af.frame,index,(flags^af.flags)&3);
        const bool present=index<0 || index<sprite_.frames().at(af.frame).rectCount;
        const int dx=present?((flags&1)?-af.x:af.x):0,dy=present?((flags&2)?-af.y:af.y):0;
        for(int i=0;i<4;++i) r[i]=add(r[i],add(i%2?y:x,i%2?dy:dx)); return r;
    };
    collision=rect(0); if(collision[0]==collision[2] && collision[1]==collision[3]) collision=rect(-1);
}
int Container::tile(int tx,int ty) const {
    if(tx<0 || tx>=maps_.layers[1].width || ty>=maps_.layers[1].height) return 24;
    if(ty<0) return 24;
    const auto index=static_cast<std::size_t>(ty)*maps_.layers[1].width+tx;
    if(index>=maps_.collision->cells.size()) throw FormatError("Container collision lookup exceeds stored grid");
    return static_cast<std::int8_t>(maps_.collision->cells[index]);
}
void Container::probe() {
    bounds(); const int top=collision[1],bottom=collision[3]+1,tx=x/20;
    const int above=tile(tx,bottom/20-1),left=tile(tx-1,bottom/20),right=tile(tx+1,bottom/20);
    feet_=tile(tx,bottom/20); belowFeet_=tile(tx,bottom/20+1);
    grounded=feet_>=15 || (feet_==0 && above>=24 && bottom-top>=20) ||
        (collision[2]-collision[0]>20 && left>=15 && right>=15);
    if(grounded && above>=15) { belowFeet_=feet_; feet_=above; }
}
bool Container::ending() const {
    const auto& a=sprite_.animations().at(animation); const int duration=sprite_.animationFrames().at(a.start+step).duration;
    return step==a.count-1 && (duration<=1 || tick==duration-1);
}
void Container::request(int id) { if(id!=animation) { pending=id; flags&=~64; } }
void Container::stop() { vx=vy=ax=ay=0; }
void Container::clearPrompt(const Player& p) {
    if(prompt && (!overlap(p.collision,collision) || p.animation>1)) { prompt=false; action=0; }
}
void Container::update(const Player& player,unsigned pressed,bool paused,bool magicUnlocked,JavaRandom* random,bool iceUnlocked) {
    drops.clear();
    if(!alive || paused || (flags&(1024|16384)) || ((flags&32) && !(flags&16))) return;
    fixedX=add(add(fixedX,shl(x-sar(fixedX))),vx); fixedY=add(add(fixedY,shl(y-sar(fixedY))),vy);
    vx=add(vx,ax); vy=add(vy,ay); ax=ay=0; x=sar(fixedX); y=sar(fixedY); bounds();
    if(flags&256) throw FormatError("Script-driven container variant is not ported");
    switch(action) {
    case 0:
        probe(); if(!grounded) action=103;
        if(animation==0 && overlap(player.collision,collision) && player.animation<=1) {
            if(!prompt) { prompt=true; promptX_=x; promptY_=y-80; promptStep_=promptTick_=0; }
            action=106;
        }
        clearPrompt(player); break;
    case 106:
        if(pressed&65568) {
            if((reward<0 && reward!=-2 && reward!=-5) || reward>1) throw FormatError("Unsupported container reward");
            if(animation==0) request(1); action=101; prompt=false;
        }
        clearPrompt(player); break;
    case 101:
        if(ending()) {
            if(animation!=1 || (reward<0 && reward!=-2 && reward!=-5) || reward>1) throw FormatError("Container burst-loot branch is not ported");
            if((reward==-2 && magicUnlocked) || (reward==-5 && iceUnlocked)){
                if(!random)throw FormatError("Repeated ability reward needs the shared random stream");
                reward=0;const int dx=random->range(-40,40),dy=random->range(-70,0);
                drops.push_back({0,x+dx,y+dy,flags&1});
            }else if(reward==-2)drops.push_back({2,x,y,flags&1,48,27});
            else if(reward==-5)drops.push_back({35,x,y,flags&1,48,18});
            else drops.push_back({1+2*reward,x,y,flags&1});
            request(2); timer=1000; action=102; flags|=16;
        }
        break;
    case 102:
        timer-=83;
        if(animation==2) { if(timer<0) request(3); }
        else if(ending()) { alive=false; prompt=false; return; }
        break;
    case 103:
        timer-=83;
        if(grounded) { stop(); if(timer<=0) action=0; }
        else {
            ay=768; probe(); if(vy>2560) { vy=2560; ay=0; }
            if(vy>0 && feet_>=15) {
                y=(collision[3]+1)/20*20-1;
                if(feet_<24 && feet_!=15 && belowFeet_>=24) y+=20;
                stop();
            }
        }
        break;
    default: throw FormatError("Container action outside prototype subset");
    }
    bounds();
}
void Container::advanceAnimation() {
    if(!alive || ((flags&32) && !(flags&16))) return;
    if(pending!=-1) { animation=pending; pending=-1; step=tick=0; }
    else if(!(flags&64)) clock(sprite_,animation,step,tick);
    if(prompt) clock(promptSprite_,25,promptStep_,promptTick_);
}
void Container::draw(Canvas& c,int cx,int cy) {
    if(alive && !(flags&(128|1024))) renderer_.drawAnimation(c,animation,step,x-cx,y-cy,{0,static_cast<unsigned>(flags)&3,{}});
}
void Container::drawPrompt(Canvas& c,int cx,int cy) {
    if(alive && prompt) promptRenderer_.drawAnimation(c,25,promptStep_,promptX_-cx,promptY_-cy);
}
void Container::write(std::ostream& out) const {
    for(int n:{fixedX,fixedY,x,y,vx,vy,ax,ay,flags,animation,pending,step,tick,action,reward,timer,int(grounded),int(prompt),int(alive)}) out << '\t' << n;
    for(int n:collision) out << '\t' << (alive?n:0);
}
}
