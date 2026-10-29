#pragma once
#include "player.h"
#include <functional>
namespace soul {
class LinkedGate {
public:
    LinkedGate(const BSprite&,LevelMaps&,const ActorRecord&);
    LinkedGate(BSprite&&,LevelMaps&,const ActorRecord&)=delete;
    LinkedGate(const BSprite&&,LevelMaps&,const ActorRecord&)=delete;
    LinkedGate(const LinkedGate&)=delete;LinkedGate& operator=(const LinkedGate&)=delete;
    int uid=0,x=0,y=0,fixedX=0,fixedY=0,vx=0,vy=0,flags=0,animation=0,pending=-1,step=0,tick=0,action=0;
    int direction=0,variant=0,remaining=0,originX=0,originY=0,bucket=0;
    bool moving=false,collisionMode=false;
    std::array<int,4> collision{},visual{},doorway{};
    void update(const std::function<const int*(int)>& resolve,bool paused=false);
    void refreshBounds();
    void immediate(int animationId);
    void advanceAnimation();
    void draw(Canvas&,int cameraX,int cameraY);
    void write(std::ostream&) const;
private:
    const BSprite& sprite_;LevelMaps& maps_;SpriteRenderer renderer_;
    std::array<int,2> links_{};std::array<const int*,2> targets_{};
    void fillDoorway();
};
}
