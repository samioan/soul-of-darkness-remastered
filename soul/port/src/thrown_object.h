#pragma once
#include "enemy_type1.h"
#include <functional>
#include <list>
namespace soul {
class ThrownObject;
// Stable attachment identity. The holder must outlive its borrowed cooldown field.
class ThrowAttachment {
public:
    explicit ThrowAttachment(int& cooldown):cooldown_(&cooldown){}
    ~ThrowAttachment();
    ThrowAttachment(const ThrowAttachment&)=delete;ThrowAttachment& operator=(const ThrowAttachment&)=delete;
    void clear();ThrownObject* object=nullptr;
private:
    friend class ThrownObject;int* cooldown_;
};
class ThrownObject {
public:
    ThrownObject(const BSprite&,const LevelMaps&,int level,int x,int y,bool left=false);
    ThrownObject(BSprite&&,const LevelMaps&,int,int,int,bool=false)=delete;
    ThrownObject(const BSprite&&,const LevelMaps&,int,int,int,bool=false)=delete;
    ThrownObject(const BSprite&,LevelMaps&&,int,int,int,bool=false)=delete;
    ThrownObject(const BSprite&,const LevelMaps&&,int,int,int,bool=false)=delete;
    ~ThrownObject();ThrownObject(const ThrownObject&)=delete;ThrownObject& operator=(const ThrownObject&)=delete;
    void attach(ThrowAttachment&);void detach();void placeHeld(int,int,bool);
    void release(int vx,int vy);void explode();void reflect(const EnemyTarget&);void remove(bool notify=true);
    void update(const EnemyTarget&,const std::function<void(ThrownObject&)>& hitActors={},const std::function<void(ThrownObject&)>& hitPlayer={},bool paused=false,bool pairedPlayerContact=false);
    void advanceAnimation();void refreshBounds();void draw(Canvas&,int,int);void write(std::ostream&)const;
    bool ending()const;bool hasHolder()const{return owner_!=nullptr;}
    int fixedX=0,fixedY=0,x=0,y=0,vx=0,vy=0,ax=0,ay=0,flags=16;
    int animation=0,pending=-1,step=0,tick=0,action=101,timer=0,aux=0,damageCooldown=0;
    bool alive=true,grounded=false,ceiling=false,wallLeft=false,wallRight=false,facingLeft=false;
    std::array<int,4> collision{},attack{},visual{};
private:
    const BSprite& sprite_;const LevelMaps& maps_;SpriteRenderer renderer_;int level_;
    ThrowAttachment* owner_=nullptr;bool dirty_=false,vertical_=false;
    int head_=0,belowHead_=0,feet_=0,aboveFeet_=0,belowFeet_=0,leftFeet_=0,rightFeet_=0,leftSide_=0,rightSide_=0;
    int tile(int,int)const;int probe();void resolveHorizontal();void bounce();void reflectContacts(const std::function<void(ThrownObject&)>&);
    void request(int);void change(int,const EnemyTarget* =nullptr);
};
class ThrownObjects {
public:
    ThrownObjects(const BSprite&,const LevelMaps&,int level);
    ThrownObjects(BSprite&&,const LevelMaps&,int)=delete;
    ThrownObjects(const BSprite&&,const LevelMaps&,int)=delete;
    ThrownObjects(const BSprite&,LevelMaps&&,int)=delete;
    ThrownObjects(const BSprite&,const LevelMaps&&,int)=delete;
    ~ThrownObjects();ThrownObjects(const ThrownObjects&)=delete;ThrownObjects& operator=(const ThrownObjects&)=delete;
    ThrownObject& spawn(int x,int y,bool left=false);
    ThrownObject& spawnHeld(ThrowAttachment&,int parentX,int parentY,bool left);
    void clear();void advanceAnimation();void draw(Canvas&,int,int);
    std::list<ThrownObject> objects;
private:
    const BSprite& sprite_;const LevelMaps& maps_;int level_;
};
}
