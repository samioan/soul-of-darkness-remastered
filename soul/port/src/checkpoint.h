#pragma once
#include "pickup.h"
namespace soul {
// Type 23's invisible rectangular trigger; saving is supplied by its world owner.
class Checkpoint {
public:
    explicit Checkpoint(const ActorRecord&);
    bool update(const Player&,bool paused=false);
    int uid=0,x=0,y=0,flags=0,bucket=0;
    bool alive=true;
    std::array<int,4> collision{};
};
struct RemovedCheckpointActor {int uid,flags;};
// First-level, in-session semantic snapshot. This is not the original RMS record.
struct CheckpointSnapshot {
    int uid=-1,x=0,y=0,activeWeapon=0;
    bool facingLeft=false;
    PlayerMeters meters;
    std::vector<RemovedCheckpointActor> removed;
    static CheckpointSnapshot capture(const Checkpoint&,const Player&,const PlayerMeters&,std::vector<RemovedCheckpointActor>);
    bool skips(const ActorRecord&)const;
    CheckpointSnapshot forContinue() const;
    void restore(Player&,PlayerMeters&)const;
};
}
