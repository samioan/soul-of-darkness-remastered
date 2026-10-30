#include "enemy_type3_ai.h"
#include <iostream>
#include <stdexcept>
namespace {
int checks=0;
void check(bool value,const char* message){++checks;if(!value)throw std::runtime_error(message);}
soul::Type3DecisionInput input(){soul::Type3DecisionInput in;in.variant=1;in.fixedX=25600;in.fixedY=199*256;in.playerFixedX=35600;in.playerFixedY=199*256;in.x=100;in.playerX=139;in.grounded=true;return in;}
}
int main(){try{
    using namespace soul;
    auto in=input();Type3DecisionState s;s.action=105;s.timer=1500;s.vx=321;
    in.attachment=Type3Attachment::HeldShot;in.playerFixedX=25600+65*256;in.playerX=165;
    auto e=decideType3(s,in);check(s.action==106 && s.throwX==1024 && e.animation==14 && s.vx==0,"lower aim threshold accepts a throw and stops the actor");
    s.action=105;in.playerFixedX=25600+140*256;in.playerX=240;e=decideType3(s,in);check(s.action==106 && s.throwX==2304,"upper aim threshold accepts a throw");
    s.action=105;in.playerFixedX=25600+141*256;in.playerX=241;e=decideType3(s,in);check(s.action==105 && s.vx==512 && s.timer==1500 && e.animation==10,"out-of-range target makes the thrower approach without spending its timer");
    in.playerFixedX=25600+64*256;in.playerX=164;s.timer=84;e=decideType3(s,in);check(s.action==105 && s.vx==-512 && s.timer==1 && e.animation==11,"close target makes the thrower retreat and spend one 83-ms tick");
    e=decideType3(s,in);check(s.action==106 && s.throwX==1024 && s.timer==-82,"close-target timeout uses the facing-dependent minimum speed");
    in.animationEnding=false;e=decideType3(s,in);check(s.action==106 && !e.releaseShot,"throw waits for the final animation tick");
    in.animationEnding=true;e=decideType3(s,in);check(e.releaseShot && e.releaseX==1024 && e.releaseY==-3584 && s.action==107 && s.timer==1800,"release emits the original fixed-point arc and enters recovery");
    in.attachment=Type3Attachment::OtherShot;s.timer=0;e=decideType3(s,in);check(s.action==107 && !e.spawnShot && s.timer==-83,"an attached non-held shot blocks replacement");
    in.attachment=Type3Attachment::None;e=decideType3(s,in);check(s.action==105 && s.timer==1500 && e.spawnShot,"empty recovery spawns a replacement and restores the aim timer");
    s.vx=555;e=enterType3Action(s,105,in);check(!e.animation && s.vx==555 && s.timer==1500,"same action does not restart animation or timer");
    s.action=0;s.timer=99;s.vx=777;e=enterType3Action(s,105,in);check(s.vx==777 && s.timer==1500,"entering aim preserves existing velocity");
    s.action=105;s.timer=200;s.vx=321;in.attachment=Type3Attachment::HeldShot;in.playerFixedX=25600;in.playerX=100;e=decideType3(s,in);check(s.action==105 && s.timer==200 && s.vx==321 && !e.animation,"zero aim result preserves movement and the aim timer");
    check(type3ThrowSpeed(0,0,100,20000)==0,"unreachable ballistic target yields zero");
    in=input();in.variant=0;in.activated=false;s={};e=decideType3(s,in);check(s.action==0 && e.spawnShot,"inactive charger still requests its first held shot");
    in.activated=true;e=decideType3(s,in);check(s.action==101 && e.animation==10,"activation enters charger pursuit");
    in.chargeReachRight=true;s.facingLeft=true;e=decideType3(s,in);check(s.action==102 && s.timer==700 && s.vx==0 && !s.facingLeft,"pursuit turns before testing charge reach and starting the stationary windup");
    in.animationEnding=true;s.timer=83;e=decideType3(s,in);check(s.action==103 && s.timer==1000 && s.vx==2048,"windup expiry begins the charge");
    s.timer=83;e=decideType3(s,in);check(s.action==104 && s.timer==500 && s.vx==0,"charge expiry starts the stationary rest");
    s.timer=83;e=decideType3(s,in);check(s.action==101 && e.animation==10,"rest expiry resumes pursuit");
    in=input();in.variant=2;in.runnerLeft=true;s={};e=enterType3Action(s,108,in);check(s.facingLeft && s.vx==-1024,"runner entry uses its saved direction");
    in.wall=true;e=decideType3(s,in);check(s.action==109 && !s.facingLeft && s.vx==0 && in.runnerLeft,"wall stops the runner and reverses its facing without changing saved direction");
    e=decideType3(s,in);check(s.action==109 && !e.animation,"stopped runner has no automatic return to running");
    in=input();s.action=0;e=enterType3Action(s,2,in);check(e.animation==13 && e.restartAnimation && s.vx==-256,"hurt entry restarts pose 13 and recoils from the player");
    e=enterType3Action(s,3,in);check(e.deathEffect,"death entry requests the owner's effect");
    in.attachment=Type3Attachment::Chest;e=decideType3(s,in);check(e.dropChest && e.resetAfterDeath && !e.releaseShot,"death requests chest drop before owner reset");
    in.attachment=Type3Attachment::HeldShot;e=decideType3(s,in);check(e.releaseShot && e.releaseX==0 && e.releaseY==0 && e.resetAfterDeath,"death drops a held shot at zero velocity");
    std::cout<<"PASS: "<<checks<<" type-3 decision assertions\n";return 0;
}catch(const std::exception& e){std::cerr<<e.what()<<'\n';return 1;}}
