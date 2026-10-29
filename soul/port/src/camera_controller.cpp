#include "camera_controller.h"
#include <algorithm>
#include <cstdlib>
namespace soul {
namespace {
int ease(int speed,int delta,int limit,int acceleration){
    speed+=delta<0?-acceleration:acceleration;delta/=2;
    if(std::abs(speed)<std::abs(delta))delta=speed;
    return std::clamp(delta,-limit,limit);
}
bool strict(const std::array<int,4>& a,const std::array<int,4>& b){return (a[0]!=a[2] || a[1]!=a[3]) && (b[0]!=b[2] || b[1]!=b[3]) && a[0]<b[2] && a[2]>b[0] && a[1]<b[3] && a[3]>b[1];}
}
void ControlledCamera::advance(const Player& p,int mw,int mh){
    if(uid==-1)return;
    if(mode==1)targetX=p.facingLeft?p.x-160:p.x-80;
    vx=ease(vx,targetX-x,20,2);vy=ease(vy,targetY-y,32,2);
    if(mode==1)vy=ease(vy,targetY-y,40,15);
    x+=vx;y+=vy;
    if(bounds[0]>0 && x<bounds[0])x=bounds[0];else if(bounds[2]>0 && x>bounds[2]-240)x=bounds[2]-240;
    if(bounds[1]>0 && y<bounds[1])y=bounds[1];else if(bounds[3]>0 && y>bounds[3]-320)y=bounds[3]-320;
    x=std::clamp(x,0,std::max(0,mw-240));y=std::clamp(y,0,std::max(0,mh-320));
}
void ControlledCamera::write(std::ostream& out)const{
    out<<'\t'<<std::boolalpha<<active;for(int n:{uid,mode,x,y,targetX,targetY,vx,vy})out<<'\t'<<n;for(int n:bounds)out<<'\t'<<n;
}
bool CameraController::supported(const ActorRecord& r){const auto& p=r.parameters;
    return r.count==14 && p[0]==49 && (p[5]&8) && !(p[5]&~(8|16|32|128|256|512|1024|16384)) && p[8]>0 && p[9]>0 &&
        (p[10]==0 || p[10]==1) && p[11]>=-1 && p[12]==0 && p[13]==0;
}
CameraController::CameraController(const ActorRecord& r){
    if(!supported(r))throw FormatError("Unsupported camera controller");const auto& p=r.parameters;
    uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);flags=p[5];mode=p[10];sourceUid=p[11];mask=p[12];
    collision={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};
}
bool CameraController::eligible(const Player& p)const{
    if(!alive)return false;if(flags&16)return true;if(flags&(256|32))return false;
    const auto& a=p.collision;const auto& b=collision;
    return bucket<7 || ((a[0]!=a[2] || a[1]!=a[3]) && a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1]);
}
void CameraController::remove(ControlledCamera& c){source_={};sourceUid=-1;alive=false;collision={};c.active=false;c.uid=-1;}
void CameraController::update(Player& p,ControlledCamera& c,const std::function<CameraSource(int)>& resolve,bool changed,bool paused){
    if(!alive || paused || (flags&(1024|16384)) || changed)return;
    if(sourceUid!=-1 && !source_)source_=resolve(sourceUid);
    if(source_ && !*source_.alive){remove(c);return;}
    if(strict(p.collision,collision)){c.active=true;flags|=16;}
    else{if(c.uid!=uid && c.active)return;c.active=false;if(c.uid==uid)c.uid=-1;flags&=~16;}
    c.mode=mode;
    if(mode==0 && !source_){c.active=false;c.uid=-1;}
    if(!c.active){c.uid=-1;return;}
    if(mode==0){
        source_.refresh();
        const int left=std::min(p.x-10,*source_.x-10),right=std::max(p.x+10,*source_.x+10);
        const int top=std::min(p.y-50,*source_.y-50),bottom=std::max(p.y,*source_.y);
        if(p.vx!=0 || p.vy!=0){c.targetX=left+(right-left)/2-120;c.targetY=top+(bottom-top)/2-160;}
        if(c.targetX+240>p.x+10 && c.targetX<p.x-10 && c.targetY+320>p.y && c.targetY<p.y-50)c.uid=uid;
        else{c.active=false;c.uid=-1;}
    }else{
        if(p.vy>0){c.targetY=p.y-160;c.uid=uid;}else{c.active=false;c.uid=-1;}
        // Original case 1 deliberately falls through to case 2, even on release.
        p.x=std::clamp(p.x,collision[0],collision[2]);
    }
}
void CameraController::write(std::ostream& out)const{
    out<<'\t'<<std::boolalpha<<alive<<'\t'<<flags<<'\t'<<sourceUid<<'\t'<<(source_?1:0);for(int n:collision)out<<'\t'<<n;
}
}
