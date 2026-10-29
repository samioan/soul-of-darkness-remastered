#pragma once
#include "player.h"
#include <functional>
namespace soul {
struct EnableEnemyTarget {int* flags=nullptr;const int* action=nullptr;std::function<void()> enabled;};
// Type 40: one player-overlap source, actor mode, one-shot enemy enable.
class PlayerEnableTrigger {
public:
    static bool supported(const ActorRecord&);
    explicit PlayerEnableTrigger(const ActorRecord&);
    bool update(Player* source,const std::function<EnableEnemyTarget(int)>&,bool paused=false);
    void write(std::ostream&)const;
    int uid=0,x=0,y=0,flags=0,bucket=0,sourceUid=-1;
    bool alive=true;
    std::array<int,4> collision{};
private:
    Player* source_=nullptr;
    std::array<int,3> uids_{};
    std::array<EnableEnemyTarget,3> targets_{};
};
}
