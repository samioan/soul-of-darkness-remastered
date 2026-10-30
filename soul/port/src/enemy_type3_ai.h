#pragma once
#include <optional>
namespace soul {
// Decision state only. The actor owner must supply movement, animation clocks,
// attachment operations, fade/death/loot, contacts and ordered world dispatch.
struct Type3DecisionState {
    int action=0, timer=0, throwX=0;
    int vx=0, vy=0, ax=0, ay=0;
    bool facingLeft=false;
};
enum class Type3Attachment { None, HeldShot, OtherShot, Chest };
struct Type3DecisionInput {
    int variant=0;
    int fixedX=0, fixedY=0, playerFixedX=0, playerFixedY=0;
    int x=0, playerX=0;
    bool runnerLeft=false, grounded=false, wall=false, animationEnding=false;
    // Reach is supplied for both orientations: pursuit may turn before testing it.
    bool activated=false, chargeReachLeft=false, chargeReachRight=false;
    Type3Attachment attachment=Type3Attachment::None;
};
struct Type3DecisionEvents {
    std::optional<int> animation;
    bool restartAnimation=false, deathEffect=false;
    bool spawnShot=false, releaseShot=false, dropChest=false, resetAfterDeath=false;
    int releaseX=0, releaseY=0;
};
// Original six-int ballistic helper specialized to type 3's +3584/-512 arc.
// Coordinates and result use the original 8-bit fixed-point representation.
int type3ThrowSpeed(int startX,int startY,int targetX,int targetY);
Type3DecisionEvents enterType3Action(Type3DecisionState&,int,const Type3DecisionInput&);
Type3DecisionEvents decideType3(Type3DecisionState&,const Type3DecisionInput&);
}
