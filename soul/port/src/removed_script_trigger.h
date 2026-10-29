#pragma once
#include "actor_script.h"
#include <optional>
namespace soul {
// Type 40: one removed placed source starts a selected one-shot gate script.
class RemovedScriptTrigger {
public:
    static bool supported(const ActorRecord&);
    explicit RemovedScriptTrigger(const ActorRecord&);
    int uid=0,x=0,y=0,flags=0,bucket=0,sourceUid=-1,scriptUid=-1,sourceIndex=0,latch=-1;
    bool cached=false,alive=true;
    std::array<int,4> collision{};
    void update(std::optional<int> liveSource,const std::vector<bool>& removed,GateScript&,bool paused=false);
    void write(std::ostream&) const;
};
}
