#pragma once
#include "fire_magic.h"
namespace soul {
class MovingPlatform;
class IceProjectile {
public:
    IceProjectile(const BSprite&,const LevelMaps&,const Player&);
    ~IceProjectile();
    IceProjectile(const IceProjectile&)=delete;IceProjectile& operator=(const IceProjectile&)=delete;
    IceProjectile(const BSprite&&,const LevelMaps&,const Player&)=delete;
    IceProjectile(BSprite&&,const LevelMaps&,const Player&)=delete;
    IceProjectile(const BSprite&,LevelMaps&&,const Player&)=delete;
    IceProjectile(const BSprite&,const LevelMaps&&,const Player&)=delete;
    void update(Player&,const std::array<int,4>& view,const std::array<int,4>& region,bool primary,const std::function<void(IceProjectile&)>&,bool paused=false);
    void advance();void refreshBounds();void draw(Canvas&,int,int);void write(std::ostream&)const;
    void request(int);void remove(Player&);
    bool tryAttach(MovingPlatform&);void detach();
    MovingPlatform* attachedTo=nullptr;
    int attachmentOffset=0;bool updatedThisTick=false;
    int fixedX=0,fixedY=0,x=0,y=0,vx=0,flags=16,animation=7,pending=-1,step=0,tick=0,timer=0;
    bool alive=true,grounded=false,ceiling=false,wallLeft=false,wallRight=false;
    std::array<int,4> collision{},attack{},visual{};
    ContactBody surface;
private:
    const BSprite& sprite_;const LevelMaps& maps_;SpriteRenderer renderer_;
    int tile(int,int)const;void resolveHorizontal();bool forward();bool ending()const;
};
class IceMagic {
public:
    IceMagic(const BSprite&,const LevelMaps&,Player&);
    IceMagic(BSprite&&,const LevelMaps&,Player&)=delete;
    IceMagic(const BSprite&&,const LevelMaps&,Player&)=delete;
    IceMagic(const BSprite&,LevelMaps&&,Player&)=delete;
    IceMagic(const BSprite&,const LevelMaps&&,Player&)=delete;
    ~IceMagic();
    IceMagic(const IceMagic&)=delete;IceMagic& operator=(const IceMagic&)=delete;
    void charge(PlayerMeters&,JavaRandom&,int& regenerationMs);
    void update(const std::array<int,4>& view,const std::array<int,4>& region,const std::function<void(IceProjectile&)>&,bool paused=false);
    void updateOne(IceProjectile&,const std::array<int,4>& view,const std::array<int,4>& region,const std::function<void(IceProjectile&)>&,bool paused=false);
    void advance();void clear();void draw(Canvas&,int,int);
    int rank=0,casts=0,hits=0,platformsCreated=0,regionRemoved=0;
    std::list<IceProjectile> projectiles;
    std::array<IceProjectile*,2> slots{};
private:
    const BSprite& sprite_;const LevelMaps& maps_;Player& player_;
    void releaseRemoved();
};
}
