#pragma once
#include "player.h"
#include <functional>
namespace soul {
// Type 59's horizontal passages: invisible links between map sections.
class LinkedPassage {
public:
    explicit LinkedPassage(const ActorRecord&);
    bool update(Player&,const std::function<const LinkedPassage*(int)>&,bool paused=false);
    int uid=0,x=0,y=0,flags=0,targetUid=-1,direction=0,bucket=0;
    std::array<int,4> collision{};
    bool linked()const{return linked_!=nullptr;}
private:
    const LinkedPassage* linked_=nullptr;
};
}
