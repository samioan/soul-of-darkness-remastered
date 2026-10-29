#include "beam_emitter.h"
#include <algorithm>
#include <cstdlib>
#include <cstring>
namespace soul {
namespace {
int wrap(std::int64_t v){std::uint32_t u=static_cast<std::uint32_t>(v);std::int32_t n;std::memcpy(&n,&u,4);return n;}
bool same(int a,int b){return (a>=0 && b>=0)||(a<=0 && b<=0);}
int det(int ax,int ay,int bx,int by){return wrap(-std::int64_t(ax)*by+std::int64_t(ay)*bx);}
bool intersect(int x,int y,int ex,int ey,int a,int b,int c,int d,std::array<int,2>* out){
    const int den=det(ex-x,ey-y,c-a,d-b),t=det(a-x,b-y,c-a,d-b),u=wrap(std::int64_t(ex-x)*(b-y)-std::int64_t(ey-y)*(a-x));
    if(!same(den,t) || !same(den,u) || std::abs(u)>std::abs(den) || (!out && (den==0 || std::abs(t)>std::abs(den))))return false;
    if(out){if(!den)throw FormatError("Original beam ray division by zero");(*out)={x+wrap(std::int64_t(t)*(ex-x))/den,y+wrap(std::int64_t(t)*(ey-y))/den};}return true;
}
bool overlap(const std::array<int,4>& a,const std::array<int,4>& b){return a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1];}
void line(Canvas& c,int x,int y,int ex,int ey,std::uint32_t color){
    const int dx=std::abs(ex-x),dy=-std::abs(ey-y),sx=x<ex?1:-1,sy=y<ey?1:-1;int err=dx+dy;
    for(;;){const auto clip=c.clip();if(x>=clip.left && x<clip.right && y>=clip.top && y<clip.bottom)c.pixels()[std::size_t(y)*c.width()+x]=color;if(x==ex && y==ey)break;const int e=2*err;if(e>=dy){err+=dy;x+=sx;}if(e<=dx){err+=dx;y+=sy;}}
}
}
bool beamCrossesRect(int x,int y,int ex,int ey,const std::array<int,4>& r){
    return intersect(x,y,ex,ey,r[0],r[1],r[2],r[1],nullptr)||intersect(x,y,ex,ey,r[0],r[1],r[0],r[3],nullptr)||intersect(x,y,ex,ey,r[2],r[3],r[2],r[1],nullptr)||intersect(x,y,ex,ey,r[2],r[3],r[0],r[3],nullptr);
}
std::array<int,2> beamRaycast(const LevelMaps& maps,int x,int y,int ex,int ey){
    const auto tile=[&](int tx,int ty){if(tx<0 || ty<0 || tx>=maps.layers[1].width || ty>=maps.layers[1].height)return 24;return int(static_cast<std::int8_t>(maps.collision->cells.at(std::size_t(ty)*maps.layers[1].width+tx)));};
    std::array<int,2> out{ex,ey};int tx=x/20,ty=y/20,value=tile(tx,ty);
    if(value>=24 && value!=15)return {x,y};
    for(int i=0;i<=56;++i){bool hit=false;
        if(x>ex){if(intersect(x,y,ex,ey,tx*20,ty*20,tx*20,(ty+1)*20,&out)){hit=true;value=tile(--tx,ty);}}
        else if(intersect(x,y,ex,ey,(tx+1)*20,ty*20,(tx+1)*20,(ty+1)*20,&out)){hit=true;value=tile(++tx,ty);}
        if(!hit){if(y>ey){if(intersect(x,y,ex,ey,tx*20,ty*20,(tx+1)*20,ty*20,&out)){hit=true;value=tile(tx,--ty);}}
            else if(intersect(x,y,ex,ey,tx*20,(ty+1)*20,(tx+1)*20,(ty+1)*20,&out)){hit=true;value=tile(tx,++ty);}}
        if((value>=15 && value!=15)||!hit)return out;
    }return out;
}
BeamEmitter::BeamEmitter(const BSprite& s,const BSprite& effect,const LevelMaps& maps,const ActorRecord& r,const std::vector<int>& stats,const std::array<int,256>& trig):sprite_(s),effect_(effect),maps_(maps),trig_(trig),renderer_(s),effectRenderer_(effect){
    const auto& p=r.parameters;
    if(r.count!=14 || p[0]!=10 || p[8]<=0 || p[9]<=0 || !(p[5]&8) || (p[5]&~(1|2|4|8|16|32|64|128|256|1024|16384)) || (p[4]!=0 && p[4]!=4 && p[4]!=8) || (p[10]!=22 && p[10]!=23) || p[11]<0 || std::size_t(p[11])*4+3>=stats.size() || !maps.collision || s.animations().size()<12 || effect.animations().size()<=13)throw FormatError("Unsupported beam emitter record/assets");
    uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);animation=p[4];flags=animation==0?p[5]:p[5]&~64;variant=p[10]==23?1:0;direction=animation==0?8:animation==8?4:(flags&1?2:1);
    activation={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};baseX=endX=x+p[12];baseY=endY=y+p[13];for(int i=0;i<4;++i)stats_[i]=stats[p[11]*4+i];
    if(stats_[1]<=0)throw FormatError("Beam sweep/cycle duration must be positive");bounds();
}
void BeamEmitter::bounds(){const auto& a=sprite_.animations().at(animation);const auto& af=sprite_.animationFrames().at(a.start+step);visual=sprite_.frameBounds(af.frame,-1,(flags^af.flags)&3);for(int i=0;i<4;++i)visual[i]+=(i%2?y+((flags&2)?-af.y:af.y):x+((flags&1)?-af.x:af.x));}
bool BeamEmitter::ending()const{const auto& a=sprite_.animations().at(animation);const int d=sprite_.animationFrames().at(a.start+step).duration;return step==a.count-1 && (d<=1 || tick==d-1);}
void BeamEmitter::request(int n){if(n!=animation){pending=n;flags&=~64;}}
void BeamEmitter::change(int n){if(action==n)return;const int offset=direction==8?0:direction==4?8:4;switch(n){
case 101:timer=stats_[3];break;case 102:request(offset+1);break;case 103:request(offset+2);timer=500;break;case 104:request(offset+3);timer=stats_[1];active=true;break;case 105:request(offset+1);active=false;break;case 106:request(offset);timer=stats_[2];break;case 107:case 109:request(offset+3);timer=stats_[1];break;case 108:case 110:timer=stats_[2];break;default:throw FormatError("Unsupported beam action");}action=n;}
void BeamEmitter::update(const Player& player,bool paused){
    crossing=false;if(paused || (flags&(1024|16384)))return;const bool wasActive=active;
    switch(action){
    case 0:if(overlap(player.collision,activation))change(variant?102:101);break;
    case 101:timer-=83;if(timer<=0)change(102);break;
    case 102:if(ending())change(103);break;
    case 103:timer-=83;if(timer<=0){change(variant?107:104);if(variant)active=true;}break;
    case 104:if(stats_[2]>0){timer-=83;if(timer<=0)change(105);}break;
    case 105:if(ending())change(106);break;
    case 106:timer-=83;if(timer<=0)change(102);break;
    case 107:case 109:{timer=std::max(0,timer-83);const int angle=stats_[3]*(action==107?stats_[1]-timer:timer)/stats_[1];const int degrees=(angle%360+360)%360;const int co=trig_[degrees*256/360],si=trig_[((90-degrees+360)%360)*256/360];const int dx=(baseX-x)*256,dy=(baseY-y)*256;
        endX=x+(wrap(std::int64_t(dx)*co-std::int64_t(dy)*si)>>8)/256;endY=y+(wrap(std::int64_t(dx)*si+std::int64_t(dy)*co)>>8)/256;if(timer<=0)change(action==107?108:110);break;}
    case 108:case 110:timer-=83;if(timer<=0)change(action==108?109:107);break;
    }
    if(active){const auto end=beamRaycast(maps_,x,y,endX,endY);endX=end[0];endY=end[1];crossing=beamCrossesRect(x,y,endX,endY,player.collision);}
    if(active && !wasActive)capStep_=capTick_=0;
}
void BeamEmitter::advanceAnimation(){
    if(pending!=-1){animation=pending;pending=-1;step=tick=0;}else if(!(flags&64)){const auto& a=sprite_.animations().at(animation);const int d=sprite_.animationFrames().at(a.start+step).duration;if(d && ++tick>=d){tick=0;if(++step>=a.count)step=0;}}
    bounds();if(active){const auto& a=effect_.animations().at(13);const int d=effect_.animationFrames().at(a.start+capStep_).duration;if(d && ++capTick_>=d){capTick_=0;if(++capStep_>=a.count)capStep_=0;}}
}
void BeamEmitter::draw(Canvas& c,int cx,int cy){if((flags&1024) || !c.visible(visual[0]-cx,visual[1]-cy,visual[2]-visual[0],visual[3]-visual[1]))return;renderer_.drawAnimation(c,animation,step,x-cx,y-cy,{0,unsigned(flags)&3,{}});if(active){for(int i=-1;i<=1;++i){const bool horizontal=std::abs(x-endX)>std::abs(y-endY);line(c,x-cx+(horizontal?0:i),y-cy+(horizontal?i:0),endX-cx+(horizontal?0:i),endY-cy+(horizontal?i:0),i==0?0xffffffff:0xffff0c00);}}}
void BeamEmitter::drawTip(Canvas& c,int cx,int cy){if(active)effectRenderer_.drawAnimation(c,13,capStep_,endX-cx,endY-cy);}
}
