#pragma once
#include "sprite_renderer.h"
namespace soul {
// Per-tick body snapshot. Owners retain fixed coordinates, animation and links.
// localBounds includes the current animation frame's offsets and flips.
struct ContactBody {
    int type=1,action=0,animation=0,flags=0,x=0,y=0,vx=0,vy=0;
    int platformLatch=0; // Type-50 moduleMask[11], reset on first landing.
    bool enabled=false,pushLocked=false,touched=false,leftContact=false,rightContact=false;
    bool grounded=false,wallLeft=false,wallRight=false,separates=false,absoluteBounds=false;
    std::array<int,4> localBounds{},collision{};
    void refreshBounds();
};
std::array<int,4> contactBounds(const BSprite&,int animation,int step,int flags);
// Actor.x(): priority 37/80 pass followed by ordinary bodies, both in reverse.
// Type 14 poses 2/3 require the separate platform/pushActor path and fail explicitly.
void resolvePlayerContacts(ContactBody& player,const std::vector<ContactBody*>& queue);
// Player branch of Actor.ai(); supporting/platform ownership remains with the world.
void separatePlayerContacts(ContactBody& player,const std::vector<ContactBody*>& queue,const ContactBody* platform=nullptr);
}
