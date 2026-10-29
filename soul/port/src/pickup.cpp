#include "pickup.h"
#include <algorithm>
#include <cstring>
namespace soul {
namespace {
int jint(std::uint32_t u) { std::int32_t i; std::memcpy(&i,&u,4); return i; }
int add(int a,int b) { return jint(std::uint32_t(a)+std::uint32_t(b)); }
int sub(int a,int b) { return jint(std::uint32_t(a)-std::uint32_t(b)); }
int mul(int a,int b) { return jint(std::uint32_t(a)*std::uint32_t(b)); }
int divide(int a,int b) { return b==-1?sub(0,a):a/b; }
int shl(int a,unsigned b) { return jint(std::uint32_t(a)<<b); }
int sar(int a,unsigned b) { return jint((std::uint32_t(a)>>b)|(a<0?(~0u<<(32-b)):0)); }
int absJava(int a) { return a<0?sub(0,a):a; }
int approximateDistance(int x,int y) {
    x=absJava(x); y=absJava(y); const int lo=std::min(x,y),hi=std::max(x,y);
    int value=add(mul(hi,1007),mul(lo,441));
    if (hi<shl(lo,4)) value=sub(value,mul(hi,40));
    return sar(add(value,512),10);
}
bool sameSign(int a,int b) { return (a>=0 && b>=0)||(a<=0 && b<=0); }
bool overlaps(const std::array<int,4>& a,const std::array<int,4>& b) {
    return a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1];
}
}
Pickup::Pickup(const BSprite& sprite,const ActorRecord& record):Pickup(sprite,record,false) {}
Pickup::Pickup(const BSprite& sprite,const ActorRecord& record,bool spawned):sprite_(sprite),renderer_(sprite),spawned_(spawned),lifetime_(spawned?6000:0) {
    const auto& p=record.parameters;
    if (record.count<6 || record.count>20 || p[0]!=35 || (p[5]&~67) ||
        (spawned?(p[4]<0 || p[4]>3):(p[4]!=0 && p[4]!=2)) ||
        static_cast<std::size_t>(p[4])>=sprite.animations().size()) throw FormatError("Unsupported health/magic pickup record");
    uid=p[1]; x=static_cast<std::uint16_t>(p[2]); y=static_cast<std::uint16_t>(p[3]);
    fixedX=shl(x,8); fixedY=shl(y,8); flags=p[5]&~64; animation=p[4]; bounds();
}
Pickup Pickup::spawned(const BSprite& sprite,int animation,int x,int y,int flags) {
    if(x<0 || x>65535 || y<0 || y>65535 || flags<0 || flags>1) throw FormatError("Unsupported spawned pickup position/flags");
    ActorRecord record{}; record.count=6; record.parameters={35,-1,static_cast<std::int16_t>(x),static_cast<std::int16_t>(y),static_cast<std::int16_t>(animation),static_cast<std::int16_t>(flags)};
    Pickup result(sprite,record,true); result.fixedX=result.fixedY=0; return result;
}
Pickup Pickup::enemyDrop(const BSprite& sprite,int animation,int x,int y,int flags) {
    if(animation!=0 && animation!=2) throw FormatError("Unsupported enemy loot animation");
    auto result=spawned(sprite,animation,0,0,flags);
    result.x=x; result.y=y; result.enemyDrop_=true; result.action=106; result.flags|=128; result.spawnDelay=1162; result.bounds(); return result;
}
void Pickup::bounds() {
    const auto& a=sprite_.animations().at(animation); const auto& af=sprite_.animationFrames().at(a.start+step);
    auto rect=[&](int index) {
        auto r=sprite_.frameBounds(af.frame,index,(flags^af.flags)&3);
        const bool present=index<0 || index<sprite_.frames().at(af.frame).rectCount;
        const int dx=present?((flags&1)?-af.x:af.x):0,dy=present?((flags&2)?-af.y:af.y):0;
        for(int i=0;i<4;++i) r[i]=add(r[i],add(i%2?y:x,i%2?dy:dx)); return r;
    };
    collision=rect(0); if(collision[0]==collision[2] && collision[1]==collision[3]) collision=rect(-1);
}
bool Pickup::ending() const {
    const auto& a=sprite_.animations().at(animation); const int duration=sprite_.animationFrames().at(a.start+step).duration;
    return step==a.count-1 && (duration<=1 || tick==duration-1);
}
void Pickup::update(Player& player,PlayerMeters& meters,bool paused) {
    effects.clear();
    if (!alive || paused || upgradeBoundary || (flags&(1024|16384))) return;
    // Keep the previous pose's collision rectangle through the handler, as Actor.update does.
    fixedX=add(add(fixedX,shl(sub(x,sar(fixedX,8)),8)),vx);
    fixedY=add(add(fixedY,shl(sub(y,sar(fixedY,8)),8)),vy);
    x=sar(fixedX,8); y=sar(fixedY,8);
    if(spawnDelay>=0) spawnDelay=add(spawnDelay,-83);
    if(action==106) {
        spawnDelay=add(spawnDelay,-83); if(spawnDelay<=0) { action=0; flags&=~128; }
        return;
    }
    if (action==0) {
        const int halfHeight=shl(sub(player.collision[3],player.collision[1]),7);
        const int dx=sub(player.fixedX,fixedX),dy=sub(sub(player.fixedY,halfHeight),fixedY);
        distance=approximateDistance(dx,sub(dy,1));
        if (distance<=(uid==-1?40960:10240)) {
            vx=distance==0?0:divide(mul(dx,1536),distance); vy=distance==0?0:divide(mul(dy,1536),distance);
            if (player.vx && sameSign(player.vx,sub(player.x,x))) vx=add(vx,player.vx);
            if (player.vy && sameSign(player.vy,sub(player.y,y))) vy=add(vy,player.vy);
        }
        if (overlaps(player.collision,collision)) {
            if (animation==1 || animation==3) {
                action=103; upgradeBoundary=true; player.beginCrystalPickup();
                if(spawned_ && !enemyDrop_) flags&=~1;
                bounds(); return;
            }
            collect();
        }
        if(spawned_) { lifetime_-=83; if(lifetime_<=0) collect(); }
    } else if (action==100) {
        timer=add(timer,83);
        if (timer>=830 || step==1) {
            if((animation==0 || animation==2) && (!spawned_ || lifetime_>0)) {
                int& value=animation==0?meters.health:meters.magic;
                value=std::min(add(value,6),animation==0?meters.maxHealth:meters.maxMagic);
                ++meters.collected;
            }
            action=101;
        }
    } else if (action==104) {
        collect(); timer=0; vx=vy=0;
    } else if (action==101 && ending()) {
        alive=false; if(flags&8192) flags|=1024; return;
    }
    if(spawned_ && !enemyDrop_) flags&=~1;
    bounds();
}
void Pickup::collect() {
    action=100;
    effects.push_back({11+(animation>>1),x,y,spawned_ && !enemyDrop_?0:(flags&1)});
}
void Pickup::beginCrystalDisplay(const Player& p) {
    if(!upgradeBoundary || action!=103) throw FormatError("No crystal display to begin");
    fixedX=add(add(fixedX,shl(sub(x,sar(fixedX,8)),8)),vx);
    fixedY=add(add(fixedY,shl(sub(y,sar(fixedY,8)),8)),vy);
    x=p.x; y=p.y-(p.collision[3]-p.collision[1])-20; action=104; bounds();
}
void Pickup::finishCrystal() {
    if(!upgradeBoundary || action!=104) throw FormatError("No crystal to resume");
    // Action 104 integrates and clears motion on the next gameplay tick.
    upgradeBoundary=false;
}
void Pickup::advanceAnimation() {
    if (!alive || (flags&(64|1024|16384))) return;
    const auto& a=sprite_.animations().at(animation); const int duration=sprite_.animationFrames().at(a.start+step).duration;
    if(duration && ++tick>=duration) { tick=0; if(++step>=a.count) step=0; }
}
void Pickup::draw(Canvas& target,int cameraX,int cameraY,int stateFrame) {
    if(alive && action!=100 && action!=101 && !(flags&(128|1024))) renderer_.drawAnimation(target,animation,step,x-cameraX+(stateFrame%2==0?1:-1),y-cameraY,{0,static_cast<unsigned>(flags)&3,{}});
}
void Pickup::write(std::ostream& out) const {
    for(int n:{fixedX,fixedY,x,y,vx,vy,flags,animation,step,tick,action,timer,distance,int(alive)}) out << '\t' << n;
    for(int n:collision) out << '\t' << (alive?n:0);
}
}
