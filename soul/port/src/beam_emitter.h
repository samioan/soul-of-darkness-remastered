#pragma once
#include "player.h"
#include <functional>
namespace soul {
std::array<int,2> beamRaycast(const LevelMaps&,int,int,int,int);
bool beamCrossesRect(int,int,int,int,const std::array<int,4>&);
class BeamEmitter {
public:
    BeamEmitter(const BSprite&,const BSprite&,const LevelMaps&,const ActorRecord&,const std::vector<int>&,const std::array<int,256>&);
    BeamEmitter(BSprite&&,const BSprite&,const LevelMaps&,const ActorRecord&,const std::vector<int>&,const std::array<int,256>&)=delete;
    BeamEmitter(const BSprite&&,const BSprite&,const LevelMaps&,const ActorRecord&,const std::vector<int>&,const std::array<int,256>&)=delete;
    BeamEmitter(const BSprite&,BSprite&&,const LevelMaps&,const ActorRecord&,const std::vector<int>&,const std::array<int,256>&)=delete;
    BeamEmitter(const BSprite&,const BSprite&&,const LevelMaps&,const ActorRecord&,const std::vector<int>&,const std::array<int,256>&)=delete;
    BeamEmitter(const BSprite&,const BSprite&,LevelMaps&&,const ActorRecord&,const std::vector<int>&,const std::array<int,256>&)=delete;
    BeamEmitter(const BSprite&,const BSprite&,const LevelMaps&&,const ActorRecord&,const std::vector<int>&,const std::array<int,256>&)=delete;
    void update(const Player&,bool paused=false);
    void advanceAnimation();
    void draw(Canvas&,int,int);
    void drawTip(Canvas&,int,int);
    int uid=0,x=0,y=0,flags=0,bucket=0,animation=0,pending=-1,step=0,tick=0,action=0,timer=0;
    int endX=0,endY=0,baseX=0,baseY=0,direction=0,variant=0;
    bool active=false,crossing=false;
    std::array<int,4> activation{},visual{};
    int damage()const{return stats_[0];}
    bool pushesWhileInvulnerable()const{return stats_[2]==0 && direction>=4;}
private:
    const BSprite& sprite_;const BSprite& effect_;const LevelMaps& maps_;const std::array<int,256> trig_;
    SpriteRenderer renderer_,effectRenderer_;std::array<int,4> stats_{};
    int capStep_=0,capTick_=0;
    bool ending()const;void change(int);void request(int);void bounds();
};
}
