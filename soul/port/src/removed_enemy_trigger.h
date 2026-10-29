#pragma once
#include "player_enable_trigger.h"
#include <optional>
namespace soul {
// Type 40: both placed sources removed (condition 4), then enable enemy/chest targets.
class RemovedEnemyTrigger {
public:
    static bool supported(const ActorRecord&);
    explicit RemovedEnemyTrigger(const ActorRecord&);
    bool update(const std::function<std::optional<int>(int)>& source,const std::vector<bool>& removed,const std::function<EnableEnemyTarget(int)>& target,bool paused=false);
    void write(std::ostream&)const;
    int uid=0,x=0,y=0,flags=0,bucket=0;
    bool alive=true;
    std::array<int,4> collision{};
    std::array<int,3> sourceUids{},indices{0,0,0};
private:
    std::array<int,3> targetUids_{};
    std::array<bool,3> cached_{};
    std::array<EnableEnemyTarget,3> targets_{};
};
}
