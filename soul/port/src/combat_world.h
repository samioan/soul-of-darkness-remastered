#pragma once
#include "enemy_type1.h"
#include "pickup.h"
#include "moving_platform.h"
#include "breakable_bridge.h"
#include "linked_area_trigger.h"
#include "linked_gate.h"
#include "actor_script.h"
#include "removed_script_trigger.h"
#include "container.h"
#include "linked_passage.h"
#include "player_enable_trigger.h"
#include "enemy_type2.h"
#include "enemy_type3.h"
#include "removed_enemy_trigger.h"
#include "fire_magic.h"
#include "ice_magic.h"
#include "checkpoint.h"
#include "beam_emitter.h"
#include "placed_prompt.h"
#include "intro_cutscene.h"
#include "chandelier_script.h"
#include "prompt_removal_trigger.h"
#include "camera_controller.h"
#include "level_completion.h"
#include "timed_switch.h"
#include "barrier_trigger.h"
#include "thrown_object.h"
#include <memory>
#include <list>
#include <deque>
namespace soul {
// First-level ordinary enemies. Borrows immutable assets/maps and the host player/meters.
class CombatWorld {
public:
    CombatWorld(const BSprite&,const BSprite&,const LevelMaps&,Player&,PlayerMeters&,const std::vector<ActorRecord>&,const std::vector<int>&);
    CombatWorld(BSprite&&,const BSprite&,const LevelMaps&,Player&,PlayerMeters&,const std::vector<ActorRecord>&,const std::vector<int>&)=delete;
    CombatWorld(const BSprite&,BSprite&&,const LevelMaps&,Player&,PlayerMeters&,const std::vector<ActorRecord>&,const std::vector<int>&)=delete;
    CombatWorld(const BSprite&,const BSprite&,LevelMaps&&,Player&,PlayerMeters&,const std::vector<ActorRecord>&,const std::vector<int>&)=delete;
    CombatWorld(const BSprite&&,const BSprite&,const LevelMaps&,Player&,PlayerMeters&,const std::vector<ActorRecord>&,const std::vector<int>&)=delete;
    CombatWorld(const BSprite&,const BSprite&&,const LevelMaps&,Player&,PlayerMeters&,const std::vector<ActorRecord>&,const std::vector<int>&)=delete;
    CombatWorld(const BSprite&,const BSprite&,const LevelMaps&&,Player&,PlayerMeters&,const std::vector<ActorRecord>&,const std::vector<int>&)=delete;
    CombatWorld(const CombatWorld&)=delete; CombatWorld& operator=(const CombatWorld&)=delete;
    std::unique_ptr<ChandelierScript> chandelierScript;
    std::vector<ChandelierTrigger> chandelierTriggers;
    void loadChandelierScript(ByteView,const std::vector<ActorRecord>&);
    std::unique_ptr<IntroCutscene> intro;
    std::vector<IntroTrigger> introTriggers;
    bool introInputReset=false;
    void loadIntro(ByteView,const std::vector<ActorRecord>&);
    void updateIntro(unsigned pressed,int dialoguePages);
    void animateIntro();
    LevelCompletion completion;
    std::vector<ExitTrigger> exits;
    ControlledCamera camera;
    std::vector<CameraController> cameraControllers;
    std::vector<PromptRemovalTrigger> promptRemovalTriggers;
    std::vector<PlacedPrompt> prompts;
    int promptsRemoved=0,recentPromptMagic=-1,promptMagicTicks=0,promptSwitchEvent=0;
    void loadPrompts(const BSprite&,const std::vector<ActorRecord>&);
    void loadPrompts(BSprite&&,const std::vector<ActorRecord>&)=delete;
    void loadPrompts(const BSprite&&,const std::vector<ActorRecord>&)=delete;
    void drawPrompts(Canvas&,int,int,BitmapFont&);
    int visiblePrompts()const;
    std::vector<BeamEmitter> beamEmitters;
    void loadBeamEmitters(const BSprite&,const std::vector<ActorRecord>&,const std::vector<int>&,const std::array<int,256>&);
    void loadBeamEmitters(BSprite&&,const std::vector<ActorRecord>&,const std::vector<int>&,const std::array<int,256>&)=delete;
    int beamHits=0;
    std::vector<EnergyBarrier> energyBarriers;
    std::vector<BarrierTrigger> barrierTriggers;
    void loadEnergyBarriers(const BSprite&,const std::vector<ActorRecord>&);
    void loadEnergyBarriers(BSprite&&,const std::vector<ActorRecord>&)=delete;
    void loadEnergyBarriers(const BSprite&&,const std::vector<ActorRecord>&)=delete;
    std::vector<TimedSwitch> timedSwitches;
    void loadTimedSwitches(const BSprite&,const std::vector<ActorRecord>&);
    void loadTimedSwitches(BSprite&&,const std::vector<ActorRecord>&)=delete;
    void loadTimedSwitches(const BSprite&&,const std::vector<ActorRecord>&)=delete;
    std::vector<LinkedPassage> passages;
    int passageTransfers=0,lastPassageUid=-1;
    std::vector<EnemyType1> enemies;
    std::vector<EnemyType2> flyingEnemies;
    // Ground throwers/chargers/runners. A deque keeps each holder stable for its shot/chest links.
    std::deque<EnemyType3> type3Enemies;
    // Requires loadThrownObjects first; mode-3 (paired) levels are rejected by the actor itself.
    void loadType3Enemies(const BSprite&,const std::vector<ActorRecord>&,const std::vector<int>&);
    void loadType3Enemies(BSprite&&,const std::vector<ActorRecord>&,const std::vector<int>&)=delete;
    void loadType3Enemies(const BSprite&&,const std::vector<ActorRecord>&,const std::vector<int>&)=delete;
    std::vector<RemovedEnemyTrigger> removedEnemyTriggers;
    int removedEnemyActivations=0;
    int containerEnables=0;
    std::unique_ptr<ThrownObjects> thrownObjects;
    void loadThrownObjects(const BSprite&);
    void loadThrownObjects(BSprite&&)=delete;
    void loadThrownObjects(const BSprite&&)=delete;
    std::unique_ptr<FireMagic> fireMagic;
    std::unique_ptr<IceMagic> iceMagic;
    int weaponSwitches=0;
    void loadFireMagic(const std::array<int,256>&);
    void chargeFireMagic();
    // Bind the completed host-owned vector once; its elements must remain stable.
    void bindContainers(std::vector<Container>&);
    bool containerQueued(int uid) const;
    void loadFlyingEnemies(const BSprite&,const std::vector<ActorRecord>&,const std::vector<int>&);
    void loadFlyingEnemies(BSprite&&,const std::vector<ActorRecord>&,const std::vector<int>&)=delete;
    void loadFlyingEnemies(const BSprite&&,const std::vector<ActorRecord>&,const std::vector<int>&)=delete;
    std::list<MovingPlatform> platforms;
    std::vector<Checkpoint> checkpoints;
    void loadCheckpoints(const std::vector<ActorRecord>&,std::function<void(const Checkpoint&)>);
    std::vector<RemovedCheckpointActor> removedCheckpointActors()const;
    void restoreCheckpointActors(const CheckpointSnapshot&,const std::vector<ActorRecord>&);
    std::list<BreakableBridge> bridges;
    std::vector<LinkedAreaTrigger> linkedAreaTriggers;
    std::vector<PlayerEnableTrigger> playerEnableTriggers;
    int playerEnableActivations=0;
    std::list<LinkedGate> gates;
    void loadGates(const BSprite&,LevelMaps&,const std::vector<ActorRecord>&);
    void loadGateScripts(ByteView);
    void startGateScript(int uid);
    int gateScriptTicks() const {return gateScript_?gateScript_->ticks():-1;}
    bool gateScriptLoaded() const {return bool(gateScript_);}
    std::vector<RemovedScriptTrigger> removedScriptTriggers;
    // Source trigger runs after its separately owned container handler.
    void finishContainerSources(const std::vector<Container>&,bool paused=false);
    void loadGates(BSprite&&,LevelMaps&,const std::vector<ActorRecord>&)=delete;
    void loadGates(const BSprite&&,LevelMaps&,const std::vector<ActorRecord>&)=delete;
    void loadBridges(const BSprite&,LevelMaps&,const std::vector<ActorRecord>&);
    void loadBridges(BSprite&&,LevelMaps&,const std::vector<ActorRecord>&)=delete;
    void loadBridges(const BSprite&&,LevelMaps&,const std::vector<ActorRecord>&)=delete;
    void loadPlatforms(const BSprite&,const std::vector<ActorRecord>&,const std::array<int,256>* trig=nullptr,int contactDamage=0);
    void loadPlatforms(BSprite&&,const std::vector<ActorRecord>&,const std::array<int,256>* =nullptr,int=0)=delete;
    void loadPlatforms(const BSprite&&,const std::vector<ActorRecord>&,const std::array<int,256>* =nullptr,int=0)=delete;
    ~CombatWorld();
    void detach();
    bool detached()const{return detached_;}
    int levelIndex()const{return player_.level();}
    std::list<CombatEffect> effects;
    std::vector<EnemyLootSpawn> loot;
    PlayerDamage damage; JavaRandom random;
    int dormant=0,playerHits=0,enemyHits=0,kills=0,effectsSpawned=0,lootSpawned=0;
    // prepare() supplies the previous tick's bodies; finishPlayer() copies corrections back.
    const std::vector<ContactBody*>& prepare(bool playerPaused=false);
    void updatePlayer(unsigned pressed,unsigned held,int frame,bool paused=false);
    // True when the region cleanup pass ran; external pickup/effect owners must
    // sweep existing transients before processing handlers or this tick's births.
    bool finishPlayer(bool paused=false);
    void advance();
    void drawEnemies(Canvas&,int cameraX,int cameraY);
    void drawPlatforms(Canvas&,int cameraX,int cameraY);
    void drawEffects(Canvas&,int cameraX,int cameraY,BitmapFont&);
    int active() const;
    // Controlled region-selection/lifecycle boundary; also used by the preview.
    void refreshRegionSelection();
    void applyRegionTransition();
    int currentRegion() const { return region_; }
    int previousRegion() const { return previousRegion_; }
    bool regionTransitionPending() const { return regionChanged_; }
    const std::array<int,4>& regionCameraBounds() const { return regionCameraBounds_; }
    std::vector<int> updateQueueUids() const;
private:
    bool detached_=false;
    const BSprite* promptSprite_=nullptr;
    std::unique_ptr<SpriteRenderer> promptRenderer_;
    const BSprite& effectSprite_; const LevelMaps& maps_; Player& player_; PlayerMeters& meters_;
    std::unique_ptr<GateScript> gateScript_;
    std::vector<int> placedUids_;
    std::vector<bool> placedRemoved_;
    bool containerSourcesPending_=false;
    std::vector<Container>* containers_=nullptr;
    std::vector<int> containerQueue_;
    struct Region { int uid,x,y,flags;std::array<int,4> bounds; };
    std::vector<Region> regions_; int region_=-1,previousRegion_=-1;
    std::array<int,4> regionCameraBounds_{};
    bool regionChanged_=false;
    std::vector<int> queue_,buckets_,type3Buckets_;
    // Live placed enemy (types 1/2/3) -> persistence index, for removed-source triggers; nullopt when absent or removed.
    std::optional<int> liveEnemyIndex(int uid) const;
    // Enemy/chest enable targets shared by removed-source triggers.
    EnableEnemyTarget enemyEnableTarget(int uid);
    Type3Services type3Services();
    void shotHitsActors(ThrownObject&,const EnemyTarget&);
    void shotHitsPlayer(ThrownObject&);
    enum class ActorKind { EnergyBarrier, BarrierTrigger, TimedSwitch, ExitTrigger, CameraController, PromptRemovalTrigger, Enemy,Platform,Bridge,Region,LinkedAreaTrigger,Gate,RemovedScriptTrigger,Passage,PlayerEnableTrigger,FlyingEnemy,Type3Enemy,RemovedEnemyTrigger,Checkpoint,BeamEmitter,Prompt,IntroTrigger,ChandelierTrigger };
    struct ActorSlot { ActorKind kind;std::size_t index; };
    std::vector<ActorSlot> order_,updateQueue_,contactQueue_;
    EnemyType1& enemyAt(const ActorSlot&);
    std::vector<MovingPlatform*> platformIndex_;
    std::vector<BreakableBridge*> bridgeIndex_;
    std::vector<LinkedGate*> gateIndex_;
    IntroActor introActor(int uid);
    const int* resolveAction(int uid) const;
    void rebuildOrder(const std::vector<ActorRecord>&);
    bool eligible(const ActorSlot&) const;
    void updateRegion(std::size_t);
    std::vector<ContactBody> bodies_; std::vector<ContactBody*> bodyPointers_;
    std::vector<CombatEffectSpawn> playerEffects_;
    int worldFrame_=0;
    std::pair<int,int> referenceCamera() const;
    void cacheBuckets();
    void updateThrownObjects(bool paused);
    void hitIceTargets(IceProjectile&);
    void updateAttachedIce(MovingPlatform&,bool paused);
    std::function<void(const Checkpoint&)> checkpointActivated_;
};
}
