#pragma once
#include "energy_barrier.h"
#include <functional>
namespace soul {
struct BarrierSignal {int* flags=nullptr;const std::array<int,4>* bounds=nullptr;std::function<void()> refresh;};
struct BarrierTarget {int* flags=nullptr;int* action=nullptr;std::function<void()> release;};
// Selected type-40 overlap/inactive-source family controlling type-80 barriers.
class BarrierTrigger {
public:
    static bool supported(const ActorRecord&);
    explicit BarrierTrigger(const ActorRecord&);
    void update(const std::function<BarrierSignal(int)>&,const std::function<BarrierTarget(int)>&,bool paused=false);
    void resetForRegionExit();
    void detach();void write(std::ostream&)const;
    int uid=0,x=0,y=0,flags=0,condition=0,operation=0,sourceUid=-1,bucket=0;
    bool alive=true;std::array<int,4> collision{};std::array<int,3> targetUids{};
private:
    int originalFlags_=0;
    BarrierSignal source_;std::array<BarrierTarget,3> targets_{};
};
}
