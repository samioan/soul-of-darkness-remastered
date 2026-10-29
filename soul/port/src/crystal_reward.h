#pragma once
#include "pickup.h"
namespace soul {
// Pure tick state extracted from Game.l/G; drawing never awards a reward.
struct CrystalReward {
    bool active=false,upgraded=false; int kind=0,frames=0;
    void begin(PlayerMeters&,int kind);
    bool update(PlayerMeters&,bool pressed);
    int displayed(const PlayerMeters&) const;
};
}
