#pragma once
#include "pickup.h"
namespace soul {
struct ContainerDrop { int animation,x,y,flags; int type=35,sprite=33; };
// Level-0 type 52: closed chests, falling and single health/magic crystals.
// Locked records wait for their enable trigger. Magic/ice drops are supported; general burst loot remains pending.
class Container {
public:
    Container(const BSprite&,const BSprite& promptSprite,const LevelMaps&,const ActorRecord&);
    Container(BSprite&&,const BSprite&,const LevelMaps&,const ActorRecord&)=delete;
    Container(const BSprite&&,const BSprite&,const LevelMaps&,const ActorRecord&)=delete;
    Container(const BSprite&,BSprite&&,const LevelMaps&,const ActorRecord&)=delete;
    Container(const BSprite&,const BSprite&&,const LevelMaps&,const ActorRecord&)=delete;
    Container(const BSprite&,const BSprite&,LevelMaps&&,const ActorRecord&)=delete;
    Container(const BSprite&,const BSprite&,const LevelMaps&&,const ActorRecord&)=delete;
    void update(const Player&,unsigned pressed,bool paused=false,bool magicUnlocked=false,JavaRandom* random=nullptr,bool iceUnlocked=false);
    void advanceAnimation();
    void draw(Canvas&,int cameraX,int cameraY);
    void drawPrompt(Canvas&,int cameraX,int cameraY);
    void write(std::ostream&) const;
    bool alive=true,prompt=false,grounded=false;
    int uid=0,x=0,y=0,fixedX=0,fixedY=0,vx=0,vy=0,ax=0,ay=0,flags=0;
    int animation=0,pending=-1,step=0,tick=0,action=0,reward=0,timer=0;
    std::array<int,4> collision{};
    std::vector<ContainerDrop> drops;
private:
    const BSprite& sprite_; const BSprite& promptSprite_; const LevelMaps& maps_;
    SpriteRenderer renderer_,promptRenderer_;
    int feet_=0,belowFeet_=0,promptX_=0,promptY_=0,promptStep_=0,promptTick_=0;
    void bounds(); void probe(); int tile(int,int) const; bool ending() const;
    void request(int); void clearPrompt(const Player&); void stop();
};
}
