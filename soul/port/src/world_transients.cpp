#include "world_transients.h"
namespace soul {
bool removeTransientOnRegionChange(int type,int uid,int animation,bool respawnable) {
    return !respawnable && uid==-1 && type!=84 && type!=85 && (type!=39 || animation!=13);
}
TransientCleanup discardRegionTransients(std::vector<Pickup>& pickups,std::list<CollectionEffect>& effects) {
    TransientCleanup removed;
    for(auto& p:pickups)if(p.alive && removeTransientOnRegionChange(35,p.uid,p.animation,(p.flags&8192)!=0)) {
        p.alive=false;p.collision={};p.upgradeBoundary=false;p.effects.clear();++removed.pickups;
    }
    // This owner constructs only UID-minus-one type-39 animations 11 and 12.
    for(const auto& effect:effects)if(effect.alive)++removed.collectionEffects;
    effects.clear();
    return removed;
}
}
