#include "ice_magic.h"
#include "moving_platform.h"
#include <algorithm>
namespace soul {
namespace {bool overlap(const std::array<int,4>& a,const std::array<int,4>& b){return a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1] && (a[0]!=a[2] || a[1]!=a[3]) && (b[0]!=b[2] || b[1]!=b[3]);}}
IceProjectile::IceProjectile(const BSprite& s,const LevelMaps& maps,const Player& p):x(p.x+(p.facingLeft?10:-10)),y(p.y-50),vx(p.facingLeft?-2560:2560),flags(16|int(p.facingLeft)),sprite_(s),maps_(maps),renderer_(s){
    if(s.animations().size()<=10 || !maps.collision)throw FormatError("Missing ice projectile assets/map");refreshBounds();
}
bool IceProjectile::ending()const{const auto& a=sprite_.animations().at(animation);const int d=sprite_.animationFrames().at(a.start+step).duration;return step==a.count-1 && (d<=1 || tick==d-1);}
IceProjectile::~IceProjectile(){detach();}
void IceProjectile::detach(){if(attachedTo){
    if(attachedTo->attachedIce){attachedTo->attachedIce->flags&=~256;attachedTo->attachedIce=nullptr;}
    auto& links=attachedTo->iceBacklinks;links.erase(std::remove(links.begin(),links.end(),this),links.end());attachedTo=nullptr;
}}
bool IceProjectile::tryAttach(MovingPlatform& p){
    if(p.type!=33 || p.animation!=0 || (p.flags&1024) || p.action==5 || p.action==6 || p.action==7 || !overlap(p.collision,attack))return false;
    if(p.attachedIce)p.attachedIce->flags&=~256;
    if(attachedTo && attachedTo!=&p)detach();
    animation=8;pending=-1;step=tick=0;flags=(flags&~64)|256;attachedTo=&p;p.attachedIce=this;
    if(std::find(p.iceBacklinks.begin(),p.iceBacklinks.end(),this)==p.iceBacklinks.end())p.iceBacklinks.push_back(this);
    x=p.collision[(flags&1)?2:0];attachmentOffset=y-p.y;vx=0;return true;
}
void IceProjectile::request(int n){if(n!=animation){pending=n;flags&=~64;}}
void IceProjectile::refreshBounds(){
    const auto& a=sprite_.animations().at(animation);const auto& af=sprite_.animationFrames().at(a.start+step);
    auto rect=[&](int i){auto r=sprite_.frameBounds(af.frame,i,(flags^af.flags)&3);const bool present=i<0 || i<sprite_.frames().at(af.frame).rectCount;const int dx=present?((flags&1)?-af.x:af.x):0,dy=present?((flags&2)?-af.y:af.y):0;for(int k=0;k<4;++k)r[k]+=k%2?y+dy:x+dx;return r;};
    visual=rect(-1);collision=rect(0);if(collision[0]==collision[2] && collision[1]==collision[3])collision=visual;attack=rect(1);
    surface.type=39;surface.x=x;surface.y=y;surface.vx=vx;surface.vy=0;surface.flags=flags;surface.animation=animation;surface.absoluteBounds=true;surface.localBounds=surface.collision=collision;
}
int IceProjectile::tile(int tx,int ty)const{
    if(tx<0 || ty<0 || tx>=maps_.layers[1].width || ty>=maps_.layers[1].height)return 24;
    const auto i=std::size_t(ty)*maps_.layers[1].width+tx;if(i>=maps_.collision->cells.size())throw FormatError("Ice collision lookup exceeds stored grid");return static_cast<std::int8_t>(maps_.collision->cells[i]);
}
bool IceProjectile::forward(){
    const int edge=(flags&1)?collision[0]-1:collision[2]+1,u=y%20;const int value=tile(edge/20,y/20);
    switch(value){case 15:case 24:return true;case 16:x=edge/20*20+u;return true;case 17:x=edge/20*20+20-u;return true;
    case 18:x=edge/20*20+u*2;return true;case 19:if(u>10)return false;x=edge/20*20+(u-10)*2;return true;
    case 20:if(u<10)return false;x=edge/20*20+(20-u)*2;return true;case 21:x=edge/20*20+(10-u)*2;return true;default:return false;}
}
void IceProjectile::resolveHorizontal(){
    refreshBounds();const int tx=x/20,top=collision[1],bottom=collision[3]+1,above=tile(tx,bottom/20-1),feet=tile(tx,bottom/20);
    grounded=feet>=15 || (feet==0 && above>=24 && bottom-top>=20) || (collision[2]-collision[0]>20 && tile(tx-1,bottom/20)>=15 && tile(tx+1,bottom/20)>=15);
    wallLeft=wallRight=ceiling=false;const int left=collision[0]-1,right=collision[2]+1,scanBottom=std::max(top,collision[3]-10);
    const int innerLeft=tile(left/20+1,scanBottom/20),innerRight=tile(right/20-1,scanBottom/20);
    for(int row=top/20;row<=scanBottom/20;++row){
        if(left<0){wallLeft=true;break;}
        if(tile(left/20,row)>=24){wallLeft=true;if(row==scanBottom/20 && (innerLeft==16 || innerLeft==18))wallLeft=false;}
        if(tile(right/20,row)>=24){wallRight=true;if(row==scanBottom/20 && (innerRight==17 || innerRight==20 || innerRight==21))wallRight=false;}
        if(wallLeft || wallRight)break;
    }
    if(wallLeft==wallRight)wallLeft=wallRight=false;
    else if(vx<=0){if(wallLeft)x+=20-(left+20)%20-1;else x-=right%20;}
    else {if(wallRight)x-=right%20;else x+=20-(left+20)%20-1;}
    refreshBounds();
    if(wallLeft || wallRight){const int cx=x/20,b=collision[3]+1,a=tile(cx,b/20-1),f=tile(cx,b/20);grounded=f>=15 || (f==0 && a>=24 && b-collision[1]>=20) || (collision[2]-collision[0]>20 && tile(cx-1,b/20)>=15 && tile(cx+1,b/20)>=15);}
}
void IceProjectile::remove(Player& p){if(p.platform==&surface)p.platform=nullptr;detach();alive=false;collision={};attack={};visual={};surface.localBounds=surface.collision={};}
void IceProjectile::update(Player& p,const std::array<int,4>& view,const std::array<int,4>& region,bool primary,const std::function<void(IceProjectile&)>& hit,bool paused){
    if(!alive || paused || (flags&(1024|16384)))return;
    updatedThisTick=true;
    fixedX=x*256+vx;fixedY=y*256;x=fixedX>>8;y=fixedY>>8;timer-=83;refreshBounds();
    switch(animation){
    case 7:
        hit(*this);
        if(!overlap(collision,view)){remove(p);return;}
        if(collision[0]>region[0]+1){
            if(collision[2]>=region[2]-1)return;
            if(forward() || (wallLeft && (flags&1)) || (wallRight && !(flags&1))){if(primary){request(8);vx=0;}else remove(p);}
            resolveHorizontal();return;
        }break;
    case 8:if(ending()){request(9);timer=10000;}break;
    case 9:
        if(timer<0 || (!forward() && (!wallLeft || !(flags&1)) && (!wallRight || (flags&1)))){request(10);timer=1000;}
        refreshBounds();{const bool continuing=p.platform==&surface;updatePlatformContact(surface,p);if(continuing && p.platform==&surface && attachedTo)p.vx+=attachedTo->vx;}resolveHorizontal();return;
    case 10:
        detach();
        if(p.platform==&surface)p.platform=nullptr;
        if(timer<0 && ending()){remove(p);return;}break;
    default:if(ending()){remove(p);return;}break;
    }
    if(pending!=-1)refreshBounds();
}
void IceProjectile::advance(){if(!alive || (flags&1024))return;refreshBounds();if(pending!=-1){animation=pending;pending=-1;step=tick=0;}else if(!(flags&64)){const auto& a=sprite_.animations().at(animation);const int d=sprite_.animationFrames().at(a.start+step).duration;if(d && ++tick>=d){tick=0;if(++step>=a.count)step=0;}}}
void IceProjectile::draw(Canvas& c,int cx,int cy){if(alive && !(flags&(128|1024)))renderer_.drawAnimation(c,animation,step,x-cx,y-cy,{0,static_cast<unsigned>(flags)&3,{}});}
void IceProjectile::write(std::ostream& out)const{for(int n:{fixedX,fixedY,x,y,vx,flags,animation,pending,step,tick,timer,int(alive),int(grounded),int(ceiling),int(wallLeft),int(wallRight)})out<<'\t'<<n;for(const auto& r:{collision,attack,visual})for(int n:r)out<<'\t'<<n;}
IceMagic::IceMagic(const BSprite& s,const LevelMaps& maps,Player& p):sprite_(s),maps_(maps),player_(p){}
IceMagic::~IceMagic(){clear();}
void IceMagic::charge(PlayerMeters& m,JavaRandom& random,int& regenerationMs){
    if(player_.animation!=23 || player_.magicCharge<0)return;
    if(player_.magicCharge<=240){player_.magicCharge+=80;const int high=((240-player_.magicCharge)>>2)>>1;for(int i=0;i<20;++i)random.range(0,high);return;}
    if(m.magic>=15){m.magic-=15;regenerationMs=0;player_.beginIceShot();
        if(slots[0]){slots[1]=slots[0];if(slots[1]->animation>=8 && slots[1]->animation<=10)slots[1]->request(10);slots[1]->timer=1000;}
        projectiles.emplace_back(sprite_,maps_,player_);slots[0]=&projectiles.back();++casts;
    }player_.magicCharge=-1;
}
void IceMagic::releaseRemoved(){for(auto& slot:slots)if(slot && !slot->alive)slot=nullptr;projectiles.remove_if([](const auto& p){return !p.alive;});}
void IceMagic::update(const std::array<int,4>& view,const std::array<int,4>& region,const std::function<void(IceProjectile&)>& hit,bool paused){
    for(auto& e:projectiles)updateOne(e,view,region,hit,paused);releaseRemoved();
}
void IceMagic::updateOne(IceProjectile& e,const std::array<int,4>& view,const std::array<int,4>& region,const std::function<void(IceProjectile&)>& hit,bool paused){const int old=e.animation,queued=e.pending;e.update(player_,view,region,slots[0]==&e,hit,paused);if(old==8 && queued!=9 && e.pending==9)++platformsCreated;}
void IceMagic::advance(){for(auto& p:projectiles)p.advance();releaseRemoved();}
void IceMagic::clear(){for(auto& p:projectiles){if(p.alive)++regionRemoved;p.remove(player_);}slots={};projectiles.clear();}
void IceMagic::draw(Canvas& c,int cx,int cy){for(auto& p:projectiles)p.draw(c,cx,cy);}
}
