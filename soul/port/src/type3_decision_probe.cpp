#include "enemy_type3_ai.h"
#include "player_probe.h"
#include <cstdint>
#include <cstring>
#include <iostream>
#include <limits>
namespace {
const int actions[]={0,1,2,3,101,102,103,104,105,106,107,108,109};
int wrapAdd(int a,int b){std::uint32_t u=std::uint32_t(a)+std::uint32_t(b);std::int32_t s;std::memcpy(&s,&u,4);return s;}
int sign(int n){return n<0?-1:n>0?1:0;}
int pending(const soul::Type3DecisionEvents& e){return e.animation && *e.animation!=9?*e.animation:-1;}
}
void emitType3DecisionManifest(const std::filesystem::path&){
    using namespace soul;
    for(int sx:{0,25600,std::numeric_limits<int>::max(),std::numeric_limits<int>::min()})for(int sy:{0,-43008,std::numeric_limits<int>::max(),std::numeric_limits<int>::min()})
    for(int dx:{-200000,-64000,-28672,-14336,-1,0,1,14336,28672,64000,200000,std::numeric_limits<int>::min(),std::numeric_limits<int>::max()})for(int dy:{-200000,-7936,0,7936,200000,std::numeric_limits<int>::min(),std::numeric_limits<int>::max()})
        std::cout<<"P\t"<<sx<<'\t'<<sy<<'\t'<<dx<<'\t'<<dy<<'\t'<<type3ThrowSpeed(sx,sy,wrapAdd(sx,dx),wrapAdd(sy,dy))<<'\n';
    for(int current:actions)for(int next:actions)for(int side:{0,1})for(int distance:{-200,-1,0,1,200}){
        Type3DecisionState s;s.action=current;s.timer=123;s.throwX=333;s.vx=103;s.vy=-203;s.ax=17;s.ay=19;s.facingLeft=side!=0;
        Type3DecisionInput in;in.fixedX=25600;in.playerFixedX=25600+distance;in.x=100;in.playerX=100+sign(distance);in.runnerLeft=side!=0;
        const auto e=enterType3Action(s,next,in);
        std::cout<<"E\t"<<current<<'\t'<<next<<'\t'<<side<<'\t'<<distance<<'\t'<<s.action<<'\t'<<s.timer<<'\t'<<s.throwX<<'\t'<<s.vx<<'\t'<<s.vy<<'\t'<<s.ax<<'\t'<<s.ay<<'\t'<<int(s.facingLeft)<<"\t9\t"<<pending(e)<<'\t'<<(e.animation && (*e.animation!=9 || e.restartAnimation)?0:64)<<'\t'<<int(e.deathEffect)<<'\n';
    }
    for(int variant:{0,1,2})for(int action:actions){if(action==3)continue;
    for(int side:{0,1})for(int distance:{-600,-200,-80,-2,0,2,80,200,600})for(int timer:{83,84})for(int mode:{0,1,2,3,4,5}){
        Type3DecisionState s;s.action=action;s.timer=timer;s.throwX=333;s.facingLeft=side!=0;
        Type3DecisionInput in;in.variant=variant;in.x=100;in.fixedX=25600;in.fixedY=199*256;in.playerFixedX=25600+distance*256;in.playerFixedY=199*256;in.playerX=100+distance;in.runnerLeft=side!=0;in.grounded=true;in.activated=mode!=4;in.wall=mode==3;in.animationEnding=mode==1 || mode==3;
        const bool reachLeft=distance < -2 || (distance>=-2 && distance<=2 && side!=0);
        in.chargeReachLeft=mode==5 && reachLeft;in.chargeReachRight=mode==5 && !reachLeft;
        in.attachment=mode==0 || mode==4?Type3Attachment::None:mode==1 || mode==5?Type3Attachment::HeldShot:mode==2?Type3Attachment::OtherShot:Type3Attachment::Chest;
        const auto e=decideType3(s,in);
        const bool child=(mode!=0 && mode!=4) || e.spawnShot;
        const int childAction=e.releaseShot?102:mode==0 || mode==4?(e.spawnShot?101:-1):mode==2?102:101;
        std::cout<<"D\t"<<variant<<'\t'<<action<<'\t'<<side<<'\t'<<distance<<'\t'<<timer<<'\t'<<mode<<'\t'<<s.action<<'\t'<<s.timer<<'\t'<<s.throwX<<'\t'<<s.vx<<'\t'<<int(s.facingLeft)<<'\t'<<pending(e)<<'\t'<<int(child && !e.releaseShot)<<'\t'<<childAction<<'\t'<<(child?(e.releaseShot?e.releaseX:0):-1)<<'\t'<<(child?(e.releaseShot?e.releaseY:0):-1)<<'\n';
    }}
}
