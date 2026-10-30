#pragma once
#include "player.h"
#include <functional>
namespace soul {
class FireMagic;
// Type 89. Target action is looked up afresh on each handler call.
class TimedSwitch {
public:
    TimedSwitch(const BSprite&,const ActorRecord&);
    TimedSwitch(BSprite&&,const ActorRecord&)=delete;
    TimedSwitch(const BSprite&&,const ActorRecord&)=delete;
    bool struckBy(const Player&,const FireMagic* =nullptr)const;
    void update(bool struck,const std::function<const int*(int)>&,bool paused=false);
    void advanceAnimation();void refreshBounds();void draw(Canvas&,int,int);
    void write(std::ostream&)const;
    int uid=0,x=0,y=0,flags=0,animation=0,pending=-1,step=0,tick=0,action=0;
    int mode=0,variant=0,duration=0,targetUid=-1,timer=0,bucket=0;
    std::array<int,4> collision{},attack{},visual{};
private:
    const BSprite& sprite_;SpriteRenderer renderer_;
    bool ending()const;void request(int);void change(int);
};
}
