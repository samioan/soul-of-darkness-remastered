#include "moving_platform.h"
#include "ice_magic.h"
#include "breakable_bridge.h"
#include "pickup.h"
#include <algorithm>
#include <cstring>
namespace soul {
namespace {
int javaInt(std::uint32_t value) { std::int32_t result; std::memcpy(&result,&value,4); return result; }
int add(int a,int b) { return javaInt(static_cast<std::uint32_t>(a)+static_cast<std::uint32_t>(b)); }
int shift(int value) { return javaInt(static_cast<std::uint32_t>(value)<<8); }
int sar(int value,unsigned bits) {
    const auto u=static_cast<std::uint32_t>(value);
    return javaInt((u>>bits)|(value<0 ? (~0u<<(32-bits)) : 0));
}
}
int MovingPlatform::tile(int tx,int ty) const {
    if(tx<0 || ty<0 || tx>=maps_.layers[1].width || ty>=maps_.layers[1].height) return 24;
    const auto index=std::size_t(ty)*maps_.layers[1].width+tx;
    if(index>=maps_.collision->cells.size()) throw FormatError("Enemy collision lookup exceeds stored grid");
    return static_cast<std::int8_t>(maps_.collision->cells[index]);
}
void MovingPlatform::refreshBounds() {
    dirty_=false;
    const auto& a=sprite_.animations().at(animation); const auto& af=sprite_.animationFrames().at(a.start+step);
    auto rect=[&](int index) {
        auto r=sprite_.frameBounds(af.frame,index,(flags^af.flags)&3);
        const bool present=index<0 || index<sprite_.frames().at(af.frame).rectCount;
        const int dx=present ? ((flags&1)?-af.x:af.x) : 0,dy=present ? ((flags&2)?-af.y:af.y) : 0;
        for (int i=0;i<4;++i) r[i]=add(r[i],add(i%2?y:x,i%2?dy:dx));
        return r;
    };
    collision=rect(0); attack=rect(1); visual=rect(-1);
    if (collision[0]==collision[2] && collision[1]==collision[3]) collision=visual;
}
int MovingPlatform::probe() {
    refreshBounds(); int correction=0; const int top=collision[1],bottom=collision[3]+1,tx=x/20;
    grounded=false; vertical_=vy!=0;
    head_=tile(tx,top/20); belowHead_=tile(tx,top/20+1); feet_=tile(tx,bottom/20); aboveFeet_=tile(tx,bottom/20-1);
    belowFeet_=tile(tx,bottom/20+1); leftFeet_=tile(tx-1,bottom/20); rightFeet_=tile(tx+1,bottom/20);
    if (vy<=-5120 && belowHead_>15 && head_<=15) head_=belowHead_;
    if (head_>=15) vertical_=true;
    if (feet_>=15 || (feet_==0 && aboveFeet_>=24 && bottom-top>=20) ||
        (collision[2]-collision[0]>20 && leftFeet_>=15 && rightFeet_>=15)) {
        correction=bottom%20;
        if (aboveFeet_>=15) { correction+=20; belowFeet_=feet_; feet_=aboveFeet_; }
        if (correction>0) vertical_=true;
        if (head_>=24 && belowHead_>=24) vertical_=false;
        grounded=true;
        if (feet_>=18 && feet_<=21) {
            const int u=sar(x%20,1);
            correction-=feet_==18 ? u : feet_==19 ? u+10 : feet_==20 ? 20-u : 10-u; vertical_=true;
        } else if (feet_==16 || feet_==17) { correction-=feet_==16 ? x%20 : 20-x%20; vertical_=true; }
    }
    return correction;
}
void MovingPlatform::resolve(bool horizontal) {
    const int correction=probe();
    int left=collision[0]-1,right=collision[2]+1,top=collision[1],bottom=std::max(top,collision[3]-10);
    wallLeft=wallRight=ceiling=false; leftSide_=rightSide_=0;
    if (vertical_) {
        if (vy>=0 && feet_>=15) y-=correction;
        else if (head_>=24) { ceiling=true; y+=20-top%20+1; }
    }
    refreshBounds(); left=collision[0]-1; right=collision[2]+1; top=collision[1]; bottom=collision[3]-10;
    if(!horizontal){centerX_=sar(collision[0]+collision[2],1);centerY_=sar(collision[1]+collision[3],1);return;}
    const int innerLeft=tile(left/20+1,bottom/20),innerRight=tile(right/20-1,bottom/20);
    for (int row=top/20;row<=bottom/20;++row) {
        if (left<0) { leftSide_=24; wallLeft=true; break; }
        int value=tile(left/20,row);
        if (value>leftSide_) { leftSide_=value; if (value>=24) { wallLeft=true;
            if (row==bottom/20 && (innerLeft==16 || innerLeft==18 || innerLeft==18)) wallLeft=false; } }
        value=tile(right/20,row);
        if (value>rightSide_) { rightSide_=value; if (value>=24) { wallRight=true;
            if (row==bottom/20 && (innerRight==17 || innerRight==20 || innerRight==21)) wallRight=false; } }
        if (wallLeft || wallRight) break;
    }
    refreshBounds();
    if (wallLeft==wallRight) wallLeft=wallRight=false;
    else if (vx<=0) { if (wallLeft) x+=20-(left+20)%20-1; else x-=right%20; probe(); }
    else { if (wallRight) x-=right%20; else x+=20-(left+20)%20-1; probe(); }
    refreshBounds(); centerX_=sar(collision[0]+collision[2],1); centerY_=sar(collision[1]+collision[3],1);
}

MovingPlatform::MovingPlatform(const BSprite& sprite,const LevelMaps& maps,const ActorRecord& r,const std::array<int,256>* trig)
    :sprite_(sprite),maps_(maps),renderer_(sprite) {
    const auto& p=r.parameters;
    type=p[0];
    if(r.count>20 || (type!=33 && type!=50) || (p[5]&~(1|2|8|16|32|64|128|256|1024|16384)) || p[4]<0 || std::size_t(p[4])>=sprite.animations().size() || sprite.animations().size()<12 || !maps.collision || maps.collision->width<=0 || maps.collision->height<=0 || maps.layers[1].width!=maps.collision->width || maps.collision->cells.size()!=std::size_t(maps.collision->width)*maps.collision->height)
        throw FormatError("Unsupported platform record/assets");
    if(type==33 && (r.count<13 || !(p[5]&8) || p[8]<=0 || p[9]<=0 || p[10]<0 || p[10]>8 || p[11]<0))throw FormatError("Unsupported type-33 path");
    if(type==50 && (r.count<12 || !trig || sprite.animations().size()<14 || p[6]>=0 || p[7]<0 || p[8]<0 || p[9]<0 || p[9]>80 || p[10]<0 || p[10]>1 || (p[11]!=-1 && p[11]!=1)))throw FormatError("Unsupported type-50 pendulum");
    uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);fixedX=shift(x);fixedY=shift(y);flags=p[5]&~64;animation=p[4];
    if(type==33){direction=p[10];timer=delay=p[12];speed=shift(p[11]);limits={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};}
    else {
        trig_=*trig;ropeLength=p[6];pivotX=x;pivotY=y+ropeLength;mode=p[10];action=mode==1?101:0;
        swing[4]=swing[5]=p[7];swing[0]=swing[1]=p[9];swing[2]=swing[3]=p[8];
        swing[6]=angularDirection=p[11];swing[7]=angularSpeed=swing[4];
    }
    surface.type=type;refreshBounds();
    if(type==50)limits={collision[0]-120,pivotY,collision[2]+120,collision[3]};
}
MovingPlatform::~MovingPlatform(){while(!iceBacklinks.empty())iceBacklinks.back()->detach();}
void MovingPlatform::scriptAnimation(int id){if(id<0 || std::size_t(id)>=sprite_.animations().size())throw FormatError("Missing scripted platform pose");if(animation!=id){animation=id;pending=-1;step=tick=0;flags&=~64;}}
void MovingPlatform::request(int id) { if(animation!=id){pending=id;flags&=~64;} }
void MovingPlatform::contact(Player& p) {
    refreshBounds();surface.x=x;surface.y=y;surface.vx=vx;surface.vy=vy;surface.flags=flags;
    surface.localBounds=contactBounds(sprite_,animation,step,flags);
    updatePlatformContact(surface,p,type==50?1:0);
    if(type==50)swing[11]=surface.platformLatch;
}
void MovingPlatform::update(Player& p,bool paused,CombatExchange* combat,const std::vector<FallingTarget>& targets) {
    if(paused)return;
    if(p.platform==&surface){p.x=add(p.x,sar(vx,8));p.y=add(p.y,sar(vy,8));}
    if(flags&(1024|16384))return;
    fixedX=add(add(fixedX,shift(add(x,-sar(fixedX,8)))),vx);fixedY=add(add(fixedY,shift(add(y,-sar(fixedY,8)))),vy);
    vx=add(vx,ax);vy=add(vy,ay);ax=ay=0;x=sar(fixedX,8);y=sar(fixedY,8);
    if(type==50){updateSwing(p,combat,targets);return;}
    contact(p);
    if(direction<8) {
        if(action==0) {
            timer=add(timer,-83);
            if(timer<=0) {
                action=105;
                if(direction<2)vx=(1-direction*2)*speed;
                else if(direction<4)vy=(1-(3-direction)*2)*speed;
                else {
                    const int sign=direction<6?1-(direction-4)*2:1-(7-direction)*2;
                    vx=sign*speed;
                    const int product=javaInt(std::uint32_t(speed)*std::uint32_t(limits[2]-limits[0]));
                    const int height=limits[3]-limits[1];if(!height)throw FormatError("Zero platform path height");
                    vy=(direction<6?shift(product):product)/height;
                    vy=javaInt(std::uint32_t(direction<6?1-(direction-4)*2:1-(7-direction)*2)*std::uint32_t(vy));
                }
            }
        } else if(action==105) {
            const bool end=direction==0?collision[2]>=limits[2]:direction==1?collision[0]<=limits[0]:direction==2?collision[1]<=limits[1]:direction==3?collision[3]>=limits[3]:
                direction==4?(collision[2]>=limits[2] || collision[3]>=limits[3]):direction==5?(collision[0]<=limits[0] || collision[1]<=limits[1]):
                direction==6?(collision[2]>=limits[2] || collision[1]<=limits[1]):(collision[0]<=limits[0] || collision[3]>=limits[3]);
            if(end){action=0;vx=vy=0;timer=delay;direction^=1;}
        }
    } else {
        resolve();timer=add(timer,-83);
        if(!p.platform) {
            request(8);const int h=collision[3]-collision[1];
            if(p.y>=limits[1]+h && p.y<=limits[3]-h)vy=javaInt(std::uint32_t(p.y-y+h+10)<<4);
            else if(p.y<limits[1]+h){vx=0;vy=y>limits[1]+h?-512:0;}
            else if(p.y>limits[3]-h){vx=0;vy=y<limits[3]?512:0;}
        } else if(p.platform==&surface) {
            if(action==0)action=102;
            else if(action==102){ay=-256;if(vy<=-speed)vy=-speed;if(p.animation==10)action=104;}
            else if(action==104){ay=256;if(vy>=speed)vy=speed;if(p.animation!=10)action=102;}
        }
        if(vx || vy) {
            if((collision[1]<=limits[1] && vy<0) || (collision[3]>=limits[3] && vy>0))vy=0;
            if(p.platform==&surface) {
                if(p.x<=x-10){vx=-512;request(10);}else if(p.x>=x+10){vx=512;request(11);}else{vx=0;request(9);}
            }
            if((collision[2]>limits[2] || wallRight) && vx>0)vx=0;
            else if((collision[0]<limits[0] || wallLeft) && vx<0)vx=0;
            if(animation==10 || animation==11)vy=sar(vy,1);
        }
    }
    if(animation==0 && p.collision[0]<=collision[2] && p.collision[2]>=collision[0] && p.collision[1]<=collision[3] && p.collision[3]>=collision[1]) {
        if(!combat)throw FormatError("Damaging platform needs combat ownership");
        if(p.receiveHit(15,x,*combat->damage,combat->meters->health,combat->meters->maxHealth,*combat->random,*combat->effects))++*combat->playerHits;
    }
    if(animation==0 && attachedIce){attachedIce->x=collision[(attachedIce->flags&1)?2:0];attachedIce->y=y+attachmentOffset;}
    contact(p);
}
void MovingPlatform::advanceAnimation() {
    if(pending!=-1){animation=pending;pending=-1;step=tick=0;}
    else if(!(flags&64)){const auto& a=sprite_.animations().at(animation);const int d=sprite_.animationFrames().at(a.start+step).duration;if(d && ++tick>=d){tick=0;if(++step>=a.count)step=0;}}
}
void MovingPlatform::draw(Canvas& c,int cx,int cy,int stateFrame) {
    if(flags&1024)return;
    if(type==50){drawChain(c,cx,cy,stateFrame);if(action==104)return;}
    if(!(flags&128))renderer_.drawAnimation(c,animation,step,x-cx,y-cy,{0,static_cast<unsigned>(flags)&3,{}});
}
void MovingPlatform::write(std::ostream& out) const {
    for(int n:{fixedX,fixedY,x,y,vx,vy,ax,ay,flags,animation,pending,step,tick,action})out<<'\t'<<n;
    for(bool b:{grounded,ceiling,wallLeft,wallRight})out<<'\t'<<std::boolalpha<<b;
    for(const auto& r:{collision,attack,visual})for(int n:r)out<<'\t'<<n;
    out<<'\t'<<direction<<'\t'<<timer;
}

std::array<int,256> loadPlatformTrig(const std::filesystem::path& root) {
    const auto pack=ResourcePack::numeric(readAssetFile(root/"17"));BinaryReader r(pack.resource(0).value());
    std::array<int,256> table;for(auto& n:table)n=r.i32le();r.done();
    for(int n:table)if(n< -256 || n>256)throw FormatError("Invalid platform trigonometry table");
    return table;
}
int MovingPlatform::cosine(int degrees) const {degrees%=360;if(degrees<0)degrees+=360;return trig_[degrees*256/360];}
void MovingPlatform::swingStep() {
    const int angularAdvance=std::max(0,angularSpeed);
    angle=add(angle,angularDirection==1?shift(angularAdvance):-shift(angularAdvance));
    if(angle==0 && swing[8]!=1){angularSpeed=swing[4];angularSign=-angularSign;swing[9]=swing[8]=1;}
    if(angle>=javaInt(std::uint32_t(swing[0])<<16) || angularSpeed<=0 || angle<=-javaInt(std::uint32_t(swing[0])<<16)){
        angularDirection=-angularDirection;angularSign=-angularSign;if(angularSpeed>0)swing[9]=1;swing[8]=0;
    }
    if(swing[9]!=1)angularSpeed=add(angularSpeed,-angularSign*swing[2]);else swing[9]=0;
}
void MovingPlatform::resetSwing() {
    action=0;swing[0]=swing[1];swing[2]=swing[3];swing[4]=swing[5];x=pivotX;y=pivotY-ropeLength;
    angularSign=1;angle=0;angularSpeed=swing[7];angularDirection=swing[6];swing[11]=surface.platformLatch=1;vx=vy=ax=ay=0;
}
void MovingPlatform::updateSwing(Player& p,CombatExchange* combat,const std::vector<FallingTarget>& targets) {
    auto overlap=[](const auto& a,const auto& b){return a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1];};
    auto cut=[&]{
        const std::array<int,4> chain{x-3,pivotY,x+3,y};
        if(mode==0 && p.animation>=13 && p.animation<=15 && overlap(p.attack,chain))action=102;
    };
    if(action!=103 && action!=104)contact(p);
    switch(action){
    case 0:
        if(p.platform==&surface && swing[11]!=1){action=101;swing[10]=2000;}
        cut();return;
    case 101:{
        swing[10]=add(swing[10],-83);swingStep();const int length=std::abs(ropeLength),degrees=sar(angle,16);
        const int nextY=sar(add(shift(pivotY),javaInt(std::uint32_t(length)*std::uint32_t(cosine(degrees)))),8);
        const int nextX=sar(add(shift(pivotX),javaInt(std::uint32_t(length)*std::uint32_t(cosine(90-degrees)))),8);
        vy=shift(nextY-y);vx=shift(nextX-x);
        if(p.platform!=&surface && angle==0 && mode!=1)resetSwing();
        if(swing[10]<0 && angle==0 && mode!=1)resetSwing();
        cut();return;
    }
    case 102:
        ay=768;if(vy>=4608){vy=4608;ay=0;}if(angle!=0)swingStep();
        for(const auto& target:targets) {
          if(target.bridge){if(target.bridge->hitByChandelier(collision,x))break;continue;}
          auto* e=target.enemy;
          if(e && e->alive && !(e->flags&(1024|16384)) && e->action!=3 && e->action!=5 && e->action!=6 && e->action!=7 && overlap(e->collision,collision) && y<e->y-25){
            if(!combat)throw FormatError("Falling platform needs combat ownership");
            const int before=e->health;const auto effectCount=e->effects.size();
            e->applyDamage(100,true,{p.x,p.fixedX,p.collision,p.attack},Weapon0{},*combat->random);
            combat->effects->insert(combat->effects->end(),e->effects.begin()+effectCount,e->effects.end());e->effects.resize(effectCount);
            if(before>0 && e->health<=0)++enemyKills;vx=0;break;
          }
        }
        resolve(false);
        if(grounded){action=103;if(animation!=12){animation=12;pending=-1;step=tick=0;flags&=~64;}ay=vy=0;}
        if(overlap(p.collision,collision) && y<p.y-25){
            if(!combat)throw FormatError("Falling platform needs player damage ownership");
            if(p.receiveHit(contactDamage,x,*combat->damage,combat->meters->health,combat->meters->maxHealth,*combat->random,*combat->effects))++*combat->playerHits;
        }
        return;
    case 103:{
        p.platform=nullptr;ay=vy=0;
        const auto& a=sprite_.animations().at(animation);const int duration=sprite_.animationFrames().at(a.start+step).duration;
        if(step==a.count-1 && (duration<=1 || tick==duration-1))action=104;
        if(angle!=0)swingStep();return;
    }
    case 104:y=pivotY;return;
    default:throw FormatError("Unsupported swinging platform state");
    }
}
void MovingPlatform::drawChain(Canvas& c,int cx,int cy,int stateFrame) {
    const int degrees=sar(angle,16),top=pivotY-cy;
    const int end=action>=102?top+std::abs(sar(ropeLength,1)):y-cy-1;
    const int middle=add(shift(top),std::abs(sar(ropeLength,1))*cosine(degrees));
    const int dx=6*cosine(90-degrees),dy=6*cosine(degrees);
    if(dy<=0)throw FormatError("Unsupported chain draw angle");
    int xx=shift(pivotX-cx),yy=shift(top);bool spark=false;
    while(yy<shift(end)){
        const int px=sar(xx,8),py=sar(yy,8);xx=add(xx,dx);const int oldY=yy;yy=add(yy,dy);
        if(mode==0 && oldY>=middle && !spark){const int n=stateFrame%14;renderer_.drawAnimation(c,13,n<=9?0:n-9,px,py+3);spark=true;++yy;}
        else {
            for(int col=0;col<6;++col){
                const int offsets[]{0,-2,1,-1,-3,2},ys[]{0,2,2,0,2,2};
                const std::uint32_t colors[]{0xffffffff,0xffc6c6c6,0xffc6c6c6,0xff747474,0xff646464,0xff646464};
                const int x0=px+offsets[col],y0=py+ys[col];
                if(c.visible(x0,y0,1,5))c.blit(Pixels(5,colors[col]),1,5,x0,y0);
            }
        }
    }
}
void MovingPlatform::writeSwing(std::ostream& out) const {
    // ActorCheck core snapshot, followed by original ar/as/at/au/av/aw/ax/ay and mask.
    for(int n:{fixedX,fixedY,x,y,vx,vy,ax,ay,flags,animation,pending,step,tick,action})out<<'\t'<<n;
    for(bool b:{grounded,ceiling,wallLeft,wallRight})out<<'\t'<<std::boolalpha<<b;
    for(const auto& r:{collision,attack,visual})for(int n:r)out<<'\t'<<n;
    for(int n:{ropeLength,pivotX,angularSpeed,angularSign,pivotY,angle,angularDirection,mode})out<<'\t'<<n;
    for(int n:swing)out<<'\t'<<n;
}

}
