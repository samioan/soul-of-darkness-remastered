#pragma once
#include "pickup.h"
#include "combat.h"
#include <functional>
#include <list>
namespace soul {
class FirePulse {
public:
    FirePulse(const BSprite&,const Player&,int index,int count);
    FirePulse(BSprite&&,const Player&,int,int)=delete;
    FirePulse(const BSprite&&,const Player&,int,int)=delete;
    void update(const Player&,const std::array<int,256>&,const std::function<void(const FirePulse&)>&);
    void advance();void draw(Canvas&,int,int);void refreshBounds();void write(std::ostream&)const;
    int fixedX=0,fixedY=0,x=0,y=0,flags=0,animation=16,pending=-1,step=0,tick=0,timer=600,index=0,count=6,radius=0,angle=0;
    bool alive=true;
    std::array<int,4> collision{},attack{},visual{};
private:
    const BSprite& sprite_;SpriteRenderer renderer_;
    bool ending()const;
};
class FireMagic {
public:
    FireMagic(const BSprite&,const std::array<int,256>&);
    FireMagic(BSprite&&,const std::array<int,256>&)=delete;
    FireMagic(const BSprite&&,const std::array<int,256>&)=delete;
    void regenerate(PlayerMeters&,int playerAction);
    void charge(Player&,PlayerMeters&,JavaRandom&);
    void update(const Player&,const std::function<void(const FirePulse&)>&,bool paused=false);
    void advance();void draw(Canvas&,int,int);void drawCharge(Canvas&,const Player&,int,int)const;
    bool occupied()const;
    void clear();
    int rank=0,regenerationMs=0,warningMs=0,casts=0,hits=0,regionRemoved=0;
    std::list<FirePulse> pulses;
private:
    const BSprite& sprite_;std::array<int,256> trig_;
};
}
