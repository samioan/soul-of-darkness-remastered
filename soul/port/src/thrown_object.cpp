#include "thrown_object.h"
#include "level_descriptor.h"
#include <cstring>
#include <algorithm>
#include <cstdlib>
namespace soul {
namespace {
int jint(std::uint32_t n){std::int32_t r;std::memcpy(&r,&n,4);return r;}
int add(int a,int b){return jint(std::uint32_t(a)+std::uint32_t(b));}
int sub(int a,int b){return jint(std::uint32_t(a)-std::uint32_t(b));}
int mul(int a,int b){return jint(std::uint32_t(a)*std::uint32_t(b));}
int sar(int a,unsigned n){return jint((std::uint32_t(a)>>n)|(a<0?(~0u<<(32-n)):0));}
int shift(int a){return jint(std::uint32_t(a)<<8);}
int absJava(int a){return a<0?sub(0,a):a;}
int length(int x,int y){x=absJava(x);y=absJava(y);const int lo=std::min(x,y),hi=std::max(x,y);int n=add(mul(hi,1007),mul(lo,441));if(hi<jint(std::uint32_t(lo)<<4))n=sub(n,mul(hi,40));return sar(add(n,512),10);}
bool overlap(const std::array<int,4>& a,const std::array<int,4>& b){return (a[0]!=a[2] || a[1]!=a[3]) && (b[0]!=b[2] || b[1]!=b[3]) && a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1];}
}
ThrowAttachment::~ThrowAttachment(){clear();}
void ThrowAttachment::clear(){if(object)object->detach();}
ThrownObject::ThrownObject(const BSprite& s,const LevelMaps& maps,int level,int px,int py,bool left):x(px),y(py),facingLeft(left),sprite_(s),maps_(maps),renderer_(s),level_(level){
    LevelDescriptor::get(level);
    if(!maps.collision || maps.collision->width<=0 || maps.collision->height<=0 || maps.layers[1].height<=0 || maps.collision->width!=maps.layers[1].width || maps.collision->cells.size()!=std::size_t(maps.collision->width)*maps.collision->height || s.animations().size()<4)throw FormatError("Invalid thrown-object assets/maps");
    if(left)flags|=1;refreshBounds();
}
ThrownObject::~ThrownObject(){detach();}
void ThrownObject::attach(ThrowAttachment& owner){detach();owner.clear();owner_=&owner;owner.object=this;flags|=256;}
void ThrownObject::detach(){if(owner_ && owner_->object==this)owner_->object=nullptr;owner_=nullptr;flags&=~256;}
void ThrownObject::placeHeld(int px,int py,bool left){x=px;y=py;facingLeft=left;}
void ThrownObject::remove(bool notify){if(owner_){auto* holder=owner_;owner_=nullptr;if(holder->object==this){holder->object=nullptr;if(notify)*holder->cooldown_=1000;}}alive=false;collision=attack=visual={};}
void ThrownObject::request(int id){if(id!=animation){pending=id;flags&=~64;dirty_=true;}}
bool ThrownObject::ending()const{const auto& a=sprite_.animations().at(animation);const int d=sprite_.animationFrames().at(a.start+step).duration;return step==a.count-1 && (d<=1 || tick==d-1);}
void ThrownObject::change(int next,const EnemyTarget* p){
    if(action==next)return;
    switch(next){
    case 101:break;
    case 102:request(1);timer=800;break;
    case 103:request(2);timer=3000;break;
    case 104:if(!p)throw FormatError("Thrown reflection requires player position");request(2);vx=sub(0,sub(fixedX,p->fixedX)<0?3328:-3328);vy=-3328;break;
    case 105:if(animation!=3){animation=3;pending=-1;step=tick=0;flags&=~64;}vx=vy=ax=ay=0;aux=0;break;
    default:throw FormatError("Unsupported thrown-object action");
    }action=next;
}
void ThrownObject::release(int dx,int dy){if(action!=101)return;change(102);vx=dx;vy=dy;ax=ay=0;flags|=16;detach();}
void ThrownObject::explode(){change(105);}
void ThrownObject::reflect(const EnemyTarget& p){change(104,&p);}
int ThrownObject::tile(int tx,int ty)const{
    const int mode=LevelDescriptor::get(level_).mode;
    if(tx>=0 && tx<maps_.layers[1].width && ty<maps_.layers[1].height){if(ty<0)return mode==3?0:24;const auto i=std::size_t(ty)*maps_.layers[1].width+tx;if(i>=maps_.collision->cells.size())throw FormatError("Thrown collision lookup exceeds stored grid");return static_cast<std::int8_t>(maps_.collision->cells[i]);}
    return mode==2?0:24;
}
void ThrownObject::refreshBounds(){
    dirty_=false;const auto& a=sprite_.animations().at(animation);const auto& af=sprite_.animationFrames().at(a.start+step);
    const auto rect=[&](int index){auto r=sprite_.frameBounds(af.frame,index,(flags^af.flags)&3);const bool present=index<0 || index<sprite_.frames().at(af.frame).rectCount;for(int i=0;i<4;++i)r[i]=add(r[i],i%2?add(y,present?((flags&2)?-af.y:af.y):0):add(x,present?((flags&1)?-af.x:af.x):0));return r;};
    collision=rect(0);attack=rect(1);visual=rect(-1);if(collision[0]==collision[2] && collision[1]==collision[3])collision=visual;
}
int ThrownObject::probe() {
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
void ThrownObject::resolveHorizontal() {
    probe();
    int left=collision[0]-1,right=collision[2]+1,top=collision[1],bottom=std::max(top,collision[3]-10);
    wallLeft=wallRight=ceiling=false; leftSide_=rightSide_=0;
    refreshBounds(); left=collision[0]-1; right=collision[2]+1; top=collision[1]; bottom=collision[3]-10;
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
    refreshBounds();
}

void ThrownObject::bounce(){
    ax=facingLeft?32:-32;if(vy<3584)ay=512;else vy=3584;
    if((facingLeft && vx>0) || (!facingLeft && vx<0))vx=0;
    if((vx>0 && wallRight) || (vx<0 && wallLeft)){facingLeft=!facingLeft;vx=sub(0,vx)/2;}
    resolveHorizontal();
    if(vy<0){if(head_>=24){y=add(y,21-collision[1]%20);vy=sub(0,vy)/2;}return;}
    int value=tile(x/20,y/20);if(LevelDescriptor::get(level_).mode==3 && y>=maps_.height())value=0;
    if(value>=15){refreshBounds();y=(collision[3]+1)/20*20-1;while(tile(x/20,y/20)>=24)y-=20;
        if(vy>2048)vy=sub(0,vy)/2;
        else if(length(vx,vy)<1280){vx=vy=ax=ay=0;grounded=true;change(103);}else vy=0;
        if(length(vx,vy)<1280){int delta=0,u=(x%20)>>1;if(feet_==18)delta=-u;else if(feet_==19)delta=-(u+10);else if(feet_==20)delta=-(20-u);else if(feet_==21)delta=-(10-u);if(feet_==16)delta-=x%20;else if(feet_==17)delta-=20-x%20;y-=delta;vx=vy=ax=ay=0;grounded=true;change(103);}
    }
}
void ThrownObject::reflectContacts(const std::function<void(ThrownObject&)>& hit){
    if(action!=105){resolveHorizontal();if(head_>=24 || feet_>=24 || leftSide_>=24 || rightSide_>=24){const int delta=probe();if(vy>=0 && feet_>=15)y-=delta;change(105);}}
    if(hit)hit(*this);
}
void ThrownObject::update(const EnemyTarget& p,const std::function<void(ThrownObject&)>& hitActors,const std::function<void(ThrownObject&)>& hitPlayer,bool paused,bool paired){
    if(!alive || paused || (flags&(1024|16384)))return;
    fixedX=add(add(fixedX,shift(sub(x,sar(fixedX,8)))),vx);fixedY=add(add(fixedY,shift(sub(y,sar(fixedY,8)))),vy);vx=add(vx,ax);vy=add(vy,ay);ax=ay=0;x=sar(fixedX,8);y=sar(fixedY,8);if(damageCooldown>=0)damageCooldown=sub(damageCooldown,83);
    switch(action){
    case 101:break;
    case 102:timer=sub(timer,83);bounce();if(overlap(p.collision,collision))change(105);else if(timer<=0 && overlap(p.attack,collision))change(104,&p);break;
    case 103:timer=sub(timer,83);if(timer<=0 || overlap(p.collision,collision))change(105);else if(overlap(p.attack,collision))change(104,&p);break;
    case 104:ax=facingLeft?32:-32;if(absJava(vy)<3584)ay=512;reflectContacts(hitActors);break;
    case 105:if(ending())remove();else{if(overlap(p.collision,collision) && hitPlayer)hitPlayer(*this);reflectContacts(hitActors);}break;
    default:throw FormatError("Unsupported thrown-object action");
    }
    if(paired && LevelDescriptor::get(level_).mode==3 && alive && overlap(p.collision,collision))change(105);
    if(alive && dirty_)refreshBounds();if(facingLeft)flags|=1;else flags&=~1;
}
void ThrownObject::advanceAnimation(){if(!alive || (flags&1024))return;refreshBounds();if(pending!=-1){animation=pending;pending=-1;step=tick=0;}else if(!(flags&64)){const auto& a=sprite_.animations().at(animation);const int d=sprite_.animationFrames().at(a.start+step).duration;if(d && ++tick>=d){tick=0;if(++step>=a.count)step=0;}}}
void ThrownObject::draw(Canvas& c,int cx,int cy){if(alive && !(flags&(128|1024)))renderer_.drawAnimation(c,animation,step,x-cx,y-cy,{0,static_cast<unsigned>(flags)&3,{}});}
void ThrownObject::write(std::ostream& out)const{for(int n:{fixedX,fixedY,x,y,vx,vy,ax,ay,flags,animation,pending,step,tick,action,timer,aux,damageCooldown,int(alive),int(grounded),int(ceiling),int(wallLeft),int(wallRight),int(facingLeft)})out<<'\t'<<n;for(const auto& r:{collision,attack,visual})for(int n:r)out<<'\t'<<n;}
ThrownObjects::ThrownObjects(const BSprite& s,const LevelMaps& m,int level):sprite_(s),maps_(m),level_(level){}
ThrownObjects::~ThrownObjects(){clear();}
ThrownObject& ThrownObjects::spawn(int x,int y,bool left){objects.emplace_back(sprite_,maps_,level_,x,y,left);return objects.back();}
ThrownObject& ThrownObjects::spawnHeld(ThrowAttachment& holder,int x,int y,bool left){auto& child=spawn(x,y,left);child.attach(holder);child.placeHeld(x+(left?-5:5),y-31,left);return child;}
void ThrownObjects::clear(){for(auto& object:objects)object.remove(false);objects.clear();}
void ThrownObjects::advanceAnimation(){for(auto& object:objects)object.advanceAnimation();objects.remove_if([](const auto& o){return !o.alive;});}
void ThrownObjects::draw(Canvas& c,int cx,int cy){for(auto& object:objects)object.draw(c,cx,cy);}
}
