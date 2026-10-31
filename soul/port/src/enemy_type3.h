#pragma once
#include "container.h"
#include "enemy_type1.h"
#include "enemy_type3_ai.h"
#include "thrown_object.h"
#include <functional>
namespace soul {
// Owner-supplied world services for one type-3 update: the shot collection, linked-chest
// lookup (live containers only) and the contact handlers a dispatched child needs.
struct Type3Services {
    ThrownObjects* shots=nullptr;
    std::function<Container*(int)> findChest;
    std::function<void(ThrownObject&)> hitActors,hitPlayer;
};
// Ground thrower/charger/runner. Shares lifecycle, hurt and loot with EnemyType1; the AI
// decisions come from enemy_type3_ai. Paired-player level mode 3 is rejected at construction.
class EnemyType3 : public EnemyType1 {
public:
    EnemyType3(const BSprite&,const LevelMaps&,const ActorRecord&,const std::vector<int>& statRow,int level);
    EnemyType3(BSprite&&,const LevelMaps&,const ActorRecord&,const std::vector<int>&,int)=delete;
    EnemyType3(const BSprite&&,const LevelMaps&,const ActorRecord&,const std::vector<int>&,int)=delete;
    EnemyType3(const BSprite&,LevelMaps&&,const ActorRecord&,const std::vector<int>&,int)=delete;
    EnemyType3(const BSprite&,const LevelMaps&&,const ActorRecord&,const std::vector<int>&,int)=delete;
    EnemyType3(const EnemyType3&)=delete; EnemyType3& operator=(const EnemyType3&)=delete;
    // Handler for one ordinary queue slot. The parent's attached child is dispatched separately.
    void update(const EnemyTarget&,bool paused,JavaRandom*,const PlayerMeters*,CombatExchange*,const Type3Services&);
    // Original Game loop: after the parent update, its attached actor updates once if it has not yet.
    void dispatchAttached(const EnemyTarget&,bool paused,const Type3Services&);
    void applyDamage(int amount,bool reaction,const EnemyTarget&,const Weapon0&,JavaRandom&) override;
    // Original n(0,0): drop a held shot with no velocity (death/damage paths).
    void releaseHeldShot();
    int linkUid() const { return linkUid_; }
    int behavior() const { return (variant_>>1)&15; }
    int attachedType() const { return attachment.object?16:chest?52:0; }
    void write(std::ostream&) const;
    int throwX=0;
    ThrowAttachment attachment;
    Container* chest=nullptr;
    void detachChest();
    // Game.removeActor on this holder: attached shot/chest lose flag 256 and the link, with no notification.
    void removeAttachments();
protected:
    void change(int) override;
    void finishReinit() override;
private:
    int linkUid_=-1; bool runnerLeft_=false;
    const EnemyTarget* target_=nullptr;
    Type3DecisionState state() const;
    void store(const Type3DecisionState&);
    Type3DecisionInput input(const EnemyTarget&) const;
    bool applyEventsAndReportDeath(const Type3DecisionEvents&,const Type3Services&);
    void snapDown();
    void gravity(bool flagBit);
    void linkChest(const Type3Services&);
};
}
