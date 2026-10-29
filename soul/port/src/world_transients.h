#pragma once
#include "pickup.h"
#include <list>
namespace soul {
// Game.W applies this only after respawnable/basic-enemy ownership branches.
bool removeTransientOnRegionChange(int type,int uid,int animation,bool respawnable=false);
struct TransientCleanup { int pickups=0,collectionEffects=0; };
// Retain pickup slots: placed/dropped indices and a displayed crystal can refer to them.
TransientCleanup discardRegionTransients(std::vector<Pickup>&,std::list<CollectionEffect>&);
}
