#pragma once
#include "moving_platform.h"
#include "placed_prompt.h"
#include <functional>
namespace soul {
// Type 40: one overlap source, one actor target, remove-once operation.
class PromptRemovalTrigger {
public:
    static bool supported(const ActorRecord&);
    explicit PromptRemovalTrigger(const ActorRecord&);
    bool update(const std::function<MovingPlatform*(int)>&,
                const std::function<PlacedPrompt*(int)>&,bool paused=false);
    int uid=0,x=0,y=0,flags=0,bucket=0,sourceUid=-1,targetUid=-1;
    bool alive=true;
    std::array<int,4> collision{};
    void write(std::ostream&)const;
private:
    MovingPlatform* source_=nullptr;
    PlacedPrompt* target_=nullptr;
};
}
