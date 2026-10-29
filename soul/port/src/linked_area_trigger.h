#pragma once
#include "player.h"
#include <functional>
namespace soul {
// Type 40, condition 0 / actor mode 0 / operation 2. Borrows stable target flags.
class LinkedAreaTrigger {
public:
    static bool supported(const ActorRecord&);
    explicit LinkedAreaTrigger(const ActorRecord&);
    int uid=0,x=0,y=0,flags=0,bucket=0;
    std::array<int,4> collision{};
    void update(const std::array<int,4>* activeRegion,const std::function<int*(int)>& resolve,bool paused=false);
    void write(std::ostream&) const;
private:
    std::array<int,3> uids_{};
    std::array<int*,3> targets_{};
};
}
