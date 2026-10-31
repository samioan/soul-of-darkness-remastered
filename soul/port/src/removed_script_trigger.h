#pragma once
#include "actor_script.h"
#include <functional>
#include <optional>
namespace soul {
// Type 40, action 0 with the script flag: removed placed sources (condition 3 = any, condition 4 = all)
// start a selected one-shot gate script. Up to three sources are tracked with the original per-slot cache.
class RemovedScriptTrigger {
public:
    static bool supported(const ActorRecord&);
    explicit RemovedScriptTrigger(const ActorRecord&);
    using SourceLookup=std::function<std::optional<int>(int)>;
    int uid=0,x=0,y=0,flags=0,bucket=0,sourceUid=-1,scriptUid=-1,sourceIndex=0,latch=-1,mode=3;
    bool cached=false,alive=true,deferred=false;
    std::array<int,4> collision{};
    std::array<int,3> sourceUids{-1,-1,-1},indices{0,0,0};
    // Single-source form used by container-owned sources: the live lookup result for the first slot.
    void update(std::optional<int> liveSource,const std::vector<bool>& removed,GateScript&,bool paused=false);
    void updateSources(const SourceLookup& source,const std::vector<bool>& removed,GateScript&,bool paused=false);
    void write(std::ostream&) const;
    void writeSources(std::ostream&) const;
    bool sourceCached(std::size_t i) const {return cached_.at(i);}
private:
    std::array<bool,3> cached_{};
    bool slotCondition(std::size_t,const std::vector<bool>&) const;
};
}
