#pragma once
#include "player.h"
namespace soul {
class BreakableBridge {
public:
    BreakableBridge(const BSprite&,LevelMaps&,const ActorRecord&);
    BreakableBridge(BSprite&&,LevelMaps&,const ActorRecord&)=delete;
    BreakableBridge(const BSprite&&,LevelMaps&,const ActorRecord&)=delete;
    BreakableBridge(const BreakableBridge&)=delete;
    BreakableBridge& operator=(const BreakableBridge&)=delete;
    int uid=0,x=0,y=0,flags=0,animation=0,pending=-1,step=0,tick=0,action=0;
    int timer=0,idleTimer=0,impactX=0,bucket=0;
    bool alive=true;
    std::array<int,4> collision{};
    void update(bool paused=false);
    void advanceAnimation();
    bool hitByChandelier(const std::array<int,4>&,int sourceX);
    void activate(int sourceX);
    void draw(Canvas&,int cameraX,int cameraY);
    void write(std::ostream&) const;
private:
    const BSprite& sprite_;LevelMaps& maps_;SpriteRenderer renderer_;
};
}
