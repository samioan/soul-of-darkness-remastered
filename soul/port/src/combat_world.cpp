#include "combat_world.h"
#include "bitmap_font.h"
#include "world_transients.h"
#include <algorithm>
#include <cstdlib>
namespace soul {
namespace {
bool overlap(const std::array<int,4>& a,const std::array<int,4>& b) { return a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1]; }
bool barrierTriggerRecord(const ActorRecord& r,const std::vector<ActorRecord>& records){
    if(!BarrierTrigger::supported(r))return false;
    for(int slot=16;slot<=18;++slot)if(std::any_of(records.begin(),records.end(),[&](const auto& a){return a.parameters[0]==80 && a.parameters[1]==r.parameters[slot];}))return true;
    return false;
}

// Removed-source script trigger owners: 1 = one placed container (deferred behind the container handler),
// 2 = placed enemies of type 1/2/3 (runs in its own queue slot), 0 = not routed.
int removedScriptSourceKind(const ActorRecord& r,const std::vector<ActorRecord>& records){
    const auto& p=r.parameters;
    const auto typeOf=[&](int uid)->int{for(const auto& row:records)if(row.parameters[0]!=0 && row.parameters[1]==uid)return row.parameters[0];return -1;};
    bool containers=false,enemies=false;
    for(int slot=13;slot<=15;++slot)if(p[slot]!=-1){
        const int type=typeOf(p[slot]);
        if(type==52)containers=true;else if(type==1 || type==2 || type==3)enemies=true;else return 0;
    }
    if(containers==enemies)return 0;
    if(containers && (p[10]!=3 || p[14]!=-1 || p[15]!=-1))return 0;
    return containers?1:2;
}
// Removed-source enable triggers: placed enemy sources and enemy/chest targets that already have native owners.
bool removedEnemyTriggerRecords(const ActorRecord& r,const std::vector<ActorRecord>& records){
    const auto& p=r.parameters;
    const auto typeOf=[&](int uid)->int{for(const auto& row:records)if(row.parameters[0]!=0 && row.parameters[1]==uid)return row.parameters[0];return -1;};
    for(int slot=13;slot<=14;++slot){const int type=typeOf(p[slot]);if(type!=1 && type!=2 && type!=3)return false;}
    for(int slot=16;slot<=18;++slot)if(p[slot]!=-1){
        const auto target=std::find_if(records.begin(),records.end(),[&](const auto& row){return row.parameters[0]!=0 && row.parameters[1]==p[slot];});
        if(target==records.end())return false;
        const int type=target->parameters[0];
        if(type!=1 && type!=2 && type!=3 && type!=52)return false;
        if(type!=52 && (target->parameters[5]&256))return false;
    }
    return true;
}
}
CombatWorld::CombatWorld(const BSprite& enemySprite,const BSprite& effectSprite,const LevelMaps& maps,Player& player,PlayerMeters& meters,const std::vector<ActorRecord>& records,const std::vector<int>& stats)
    :effectSprite_(effectSprite),maps_(maps),player_(player),meters_(meters) {
    enemies.reserve(records.size());passages.reserve(records.size());
    for(const auto& r:records) {
        const auto& p=r.parameters;
        if(p[0]!=0)placedUids_.push_back(p[1]);
        if(RemovedScriptTrigger::supported(r)){
            const int kind=removedScriptSourceKind(r,records);
            if(kind==1){
                const auto source=std::find_if(records.begin(),records.end(),[&](const auto& row){return row.parameters[0]==52 && row.parameters[1]==p[13];});
                if(source==records.end() || &*source>=&r)throw FormatError("Deferred source trigger requires its earlier placed container");
            }
            if(kind!=0){removedScriptTriggers.emplace_back(r);removedScriptTriggers.back().deferred=kind==1;}
        }
        if(RemovedEnemyTrigger::supported(r)){
            if(removedEnemyTriggerRecords(r,records))removedEnemyTriggers.emplace_back(r);
        }
        if(p[0]==59)passages.emplace_back(r);
        if(PlayerEnableTrigger::supported(r) && !barrierTriggerRecord(r,records)){
            if(p[13]!=player_.uid)throw FormatError("Enemy enable trigger requires the owned player source");
            for(int slot=16;slot<=18;++slot)if(p[slot]!=-1){
                const auto target=std::find_if(records.begin(),records.end(),[&](const auto& row){return row.parameters[1]==p[slot];});
                if(target==records.end() || (target->parameters[0]!=1 && target->parameters[0]!=3) || (target->parameters[5]&256))throw FormatError("Player enable targets require supported placed type-1 or type-3 enemies");
            }
            playerEnableTriggers.emplace_back(r);
        }
        if(PromptRemovalTrigger::supported(r)){
            const auto has=[&](int uid,int type){return std::any_of(records.begin(),records.end(),[&](const auto& a){return a.parameters[1]==uid && a.parameters[0]==type;});};
            if(has(p[13],50) && has(p[16],21))promptRemovalTriggers.emplace_back(r);
        }
        if(ExitTrigger::supported(r))exits.emplace_back(r);
        if(CameraController::supported(r))cameraControllers.emplace_back(r);
        if(LinkedAreaTrigger::supported(r))linkedAreaTriggers.emplace_back(r);
        if(p[0]==22) {
            if(r.count<12 || (p[5]&~(8|32|256|1024|16384)) || !(p[5]&8) || p[8]<=0 || p[9]<=0)throw FormatError("Unsupported camera-region record");
            const int x=static_cast<std::uint16_t>(p[2]),y=static_cast<std::uint16_t>(p[3]);
            regions_.push_back({p[1],x,y,p[5]|16,{x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]}});
        }
        if(p[0]!=1) continue;
        if(p[5]&256) { ++dormant; continue; }
        enemies.emplace_back(enemySprite,maps,r,stats);
    }
    placedRemoved_.resize(placedUids_.size());rebuildOrder(records);buckets_.resize(enemies.size()); cacheBuckets();
}
CombatWorld::~CombatWorld(){detach();}
void CombatWorld::detach(){
    if(detached_)return;detached_=true;
    // Remove borrowers and reciprocal projectile links while every target is alive.
    player_.platform=nullptr;checkpointActivated_={};containers_=nullptr;containerSourcesPending_=false;
    if(thrownObjects)thrownObjects->clear();
    if(iceMagic)iceMagic->clear();if(fireMagic)fireMagic->clear();
    intro.reset();chandelierScripts.clear();selected_=SelectedScriptSlots{};gateScript_.reset();
    barrierTriggers.clear();
    linkedAreaTriggers.clear();playerEnableTriggers.clear();removedEnemyTriggers.clear();removedScriptTriggers.clear();
    promptRemovalTriggers.clear();cameraControllers.clear();introTriggers.clear();chandelierTriggers.clear();
    bodyPointers_.clear();bodies_.clear();playerEffects_.clear();order_.clear();updateQueue_.clear();contactQueue_.clear();queue_.clear();containerQueue_.clear();
    platformIndex_.clear();bridgeIndex_.clear();gateIndex_.clear();
    thrownObjects.reset();iceMagic.reset();fireMagic.reset();gates.clear();timedSwitches.clear();energyBarriers.clear();platforms.clear();bridges.clear();
    enemies.clear();flyingEnemies.clear();type3Enemies.clear();type3Buckets_.clear();prompts.clear();beamEmitters.clear();checkpoints.clear();exits.clear();passages.clear();effects.clear();loot.clear();
    promptRenderer_.reset();promptSprite_=nullptr;placedUids_.clear();placedRemoved_.clear();buckets_.clear();regions_.clear();
    region_=previousRegion_=-1;regionChanged_=false;regionCameraBounds_={};camera={};completion={};
}

void CombatWorld::loadCheckpoints(const std::vector<ActorRecord>& records,std::function<void(const Checkpoint&)> activated){
    if(!checkpoints.empty() || checkpointActivated_)throw FormatError("Checkpoints already loaded");
    for(const auto& r:records)if(r.parameters[0]==23)checkpoints.emplace_back(r);
    checkpointActivated_=std::move(activated);rebuildOrder(records);const auto [cx,cy]=referenceCamera();for(auto& c:checkpoints)c.bucket=std::abs(c.x-(cx+120))/120+std::abs(c.y-(cy+160))/160;
}
std::vector<RemovedCheckpointActor> CombatWorld::removedCheckpointActors()const{
    std::vector<RemovedCheckpointActor> result;
    const auto add=[&](const auto& rows){for(const auto& a:rows)if(!a.alive)result.push_back({a.uid,a.flags});};
    add(energyBarriers);add(barrierTriggers);add(cameraControllers);add(promptRemovalTriggers);add(chandelierTriggers);add(introTriggers);add(prompts);add(enemies);add(flyingEnemies);add(type3Enemies);add(checkpoints);add(bridges);add(removedEnemyTriggers);add(removedScriptTriggers);add(playerEnableTriggers);return result;
}
void CombatWorld::restoreCheckpointActors(const CheckpointSnapshot& saved,const std::vector<ActorRecord>& records){
    if(saved.level!=player_.level())throw FormatError("Checkpoint actor state belongs to another level");
    if(worldFrame_!=0 || !updateQueue_.empty())throw FormatError("Checkpoint restore requires a reconstructed world");
    const auto skip=[&](int uid){const auto r=std::find_if(records.begin(),records.end(),[&](const auto& a){return a.parameters[1]==uid;});return r!=records.end() && saved.skips(*r);};
    const auto remove=[&](auto& rows){for(auto& a:rows)if(skip(a.uid))a.alive=false;};
    remove(energyBarriers);remove(barrierTriggers);remove(cameraControllers);remove(promptRemovalTriggers);remove(chandelierTriggers);remove(introTriggers);remove(prompts);remove(enemies);remove(flyingEnemies);remove(type3Enemies);remove(checkpoints);remove(bridges);remove(removedEnemyTriggers);remove(removedScriptTriggers);remove(playerEnableTriggers);
    for(std::size_t i=0;i<placedUids_.size();++i)placedRemoved_[i]=skip(placedUids_[i]);
    region_=-1;previousRegion_=-1;regionChanged_=false;refreshRegionSelection();cacheBuckets();
}
void CombatWorld::loadThrownObjects(const BSprite& sprite){if(detached_ || thrownObjects)throw FormatError("Thrown objects need one live world owner");thrownObjects=std::make_unique<ThrownObjects>(sprite,maps_,player_.level());}
void CombatWorld::loadFireMagic(const std::array<int,256>& trig){if(fireMagic)throw FormatError("Fire magic already loaded");fireMagic=std::make_unique<FireMagic>(effectSprite_,trig);iceMagic=std::make_unique<IceMagic>(effectSprite_,maps_,player_);}
void CombatWorld::chargeFireMagic(){
    const int fire=fireMagic?fireMagic->casts:0,ice=iceMagic?iceMagic->casts:0;
    if(fireMagic)fireMagic->charge(player_,meters_,random);if(iceMagic)iceMagic->charge(meters_,random,fireMagic->regenerationMs);
    if(fireMagic && fireMagic->casts!=fire){recentPromptMagic=0;promptMagicTicks=2;}
    if(iceMagic && iceMagic->casts!=ice){recentPromptMagic=1;promptMagicTicks=2;}
}
void CombatWorld::bindContainers(std::vector<Container>& containers){
    if(containers_)throw FormatError("Container owner already bound");containers_=&containers;
}
bool CombatWorld::containerQueued(int uid)const{return std::find(containerQueue_.begin(),containerQueue_.end(),uid)!=containerQueue_.end();}
void CombatWorld::loadFlyingEnemies(const BSprite& sprite,const std::vector<ActorRecord>& records,const std::vector<int>& stats,const std::array<int,256>* trig){
    if(!flyingEnemies.empty())throw FormatError("Flying enemies already loaded");flyingEnemies.reserve(records.size());
    for(const auto& r:records)if(r.parameters[0]==2)flyingEnemies.emplace_back(sprite,maps_,r,stats,trig);
    rebuildOrder(records);cacheBuckets();
}
void CombatWorld::loadType3Enemies(const BSprite& sprite,const std::vector<ActorRecord>& records,const std::vector<int>& stats){
    if(!type3Enemies.empty())throw FormatError("Type-3 enemies already loaded");
    if(detached_ || !thrownObjects)throw FormatError("Type-3 enemies need a live world with thrown objects");
    for(const auto& r:records)if(r.parameters[0]==3)type3Enemies.emplace_back(sprite,maps_,r,stats,player_.level());
    type3Buckets_.assign(type3Enemies.size(),0);rebuildOrder(records);cacheBuckets();
}
EnemyType1& CombatWorld::enemyAt(const ActorSlot& slot){
    if(slot.kind==ActorKind::Enemy)return enemies[slot.index];
    if(slot.kind==ActorKind::Type3Enemy)return type3Enemies[slot.index];
    return flyingEnemies[slot.index];
}
Type3Services CombatWorld::type3Services(){
    Type3Services s;s.shots=thrownObjects.get();
    s.findChest=[this](int uid)->Container*{if(containers_)for(auto& c:*containers_)if(c.uid==uid && c.alive)return &c;return nullptr;};
    s.hitActors=[this](ThrownObject& shot){shotHitsActors(shot,EnemyTarget{player_.x,player_.fixedX,player_.collision,player_.attack,player_.fixedY});};
    s.hitPlayer=[this](ThrownObject& shot){shotHitsPlayer(shot);};
    return s;
}
void CombatWorld::loadPlatforms(const BSprite& sprite,const std::vector<ActorRecord>& records,const std::array<int,256>* trig,int contactDamage) {
    if(!platforms.empty())throw FormatError("Platforms already loaded");
    for(const auto& r:records)if(r.parameters[0]==33){platforms.emplace_back(sprite,maps_,r);platformIndex_.push_back(&platforms.back());}
    for(const auto& r:records)if(r.parameters[0]==50){platforms.emplace_back(sprite,maps_,r,trig);platforms.back().contactDamage=contactDamage;platformIndex_.push_back(&platforms.back());}
    rebuildOrder(records);
    cacheBuckets();
}
void CombatWorld::loadBridges(const BSprite& sprite,LevelMaps& maps,const std::vector<ActorRecord>& records) {
    if(&maps!=&maps_ || !bridges.empty())throw FormatError("Bridge owner needs the shared collision map and a single load");
    for(const auto& r:records)if(r.parameters[0]==45){bridges.emplace_back(sprite,maps,r);bridgeIndex_.push_back(&bridges.back());}
    rebuildOrder(records);cacheBuckets();
}
IntroActor CombatWorld::introActor(int uid){
    if(player_.uid==uid)return {&player_.x,&player_.y,&player_.flags,&player_.facingLeft,[this](int n){player_.scriptAnimation(n);},[this]{player_.refreshBounds();return player_.collision[1];},[this]{player_.refreshBounds();},[]{throw FormatError("Intro cannot remove player");}};
    const auto enemy=[this](EnemyType1& e)->IntroActor{return {&e.x,&e.y,&e.flags,&e.facingLeft,[&e](int n){e.scriptAnimation(n);},[&e]{e.refreshBounds();return e.collision[1];},[&e]{e.refreshBounds();},[this,&e]{e.alive=false;e.collision=e.attack=e.visual={};for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==e.uid)placedRemoved_[i]=true;}};};
    for(auto& e:enemies)if(e.uid==uid && e.alive)return enemy(e);
    for(auto& e:flyingEnemies)if(e.uid==uid && e.alive)return enemy(e);
    for(auto& p:prompts)if(p.uid==uid && p.alive)return {&p.x,&p.y,&p.flags,&p.facingLeft,[&p](int n){if(n!=p.animation){p.animation=n;p.step=p.tick=0;p.flags&=~64;}},[&p]{return p.collision[1];},[]{},[this,&p]{p.alive=false;for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==p.uid)placedRemoved_[i]=true;}};
    return {};
}
void CombatWorld::loadChandelierScript(ByteView bytes,const std::vector<ActorRecord>& records){
    if(!chandelierScripts.empty())throw FormatError("Chandelier scripts already loaded");
    auto decoded=decodeScripts(bytes);
    for(const auto& r:records)if(ChandelierTrigger::supported(r)){
        if(r.parameters[13]!=player_.uid)throw FormatError("Chandelier script trigger needs the owned player");
        const int uid=r.parameters[16];
        if(std::any_of(chandelierScripts.begin(),chandelierScripts.end(),[&](const auto& c){return c->uid()==uid;}))throw FormatError("Chandelier script selected by two triggers");
        const auto data=std::find_if(decoded.begin(),decoded.end(),[&](const auto& d){return d.uid==uid;});
        if(data==decoded.end())throw FormatError("Missing chandelier script");
        chandelierScripts.push_back(std::make_unique<ChandelierScript>(*data));chandelierTriggers.emplace_back(r);
    }
    if(chandelierTriggers.empty())throw FormatError("Expected a chandelier script trigger");
    rebuildOrder(records);cacheBuckets();
}
ChandelierScript* CombatWorld::chandelierScriptFor(int uid){for(auto& c:chandelierScripts)if(c->uid()==uid)return c.get();return nullptr;}
void CombatWorld::loadIntro(ByteView scripts,const std::vector<ActorRecord>& records){
    if(intro)throw FormatError("Intro already loaded");
    for(auto& s:decodeScripts(scripts))if(s.uid==76)intro=std::make_unique<IntroCutscene>(std::move(s),maps_.width(),maps_.height());
    if(!intro)throw FormatError("Missing intro script 76");
    for(const auto& r:records)if(r.parameters[0]==20 && r.parameters[4]==8 && r.parameters[10]==76)introTriggers.emplace_back(r);
    if(introTriggers.size()!=1)throw FormatError("Expected one intro trigger");
    rebuildOrder(records);cacheBuckets();
}
void CombatWorld::updateIntro(unsigned pressed,int pages){
    if(!intro || !intro->active())return;
    if((pressed&131072) && intro->skipAllowed){intro->skip([this](int uid){return introActor(uid);});introInputReset=true;}
    else if(intro->dialogue>=0){if(pressed&(65536|32768)){if(intro->page+1<pages)++intro->page;else intro->dismissDialogue();introInputReset=true;}}
    else intro->step([this](int uid){return introActor(uid);});
    if(!intro->active()){introInputReset=true;refreshRegionSelection();}
    intro->presentationTick();
}
void CombatWorld::animateIntro(){player_.refreshBounds();player_.advanceAnimation();advance();}
void CombatWorld::loadPrompts(const BSprite& sprite,const std::vector<ActorRecord>& records){
    if(promptSprite_)throw FormatError("Prompts already loaded");
    for(const auto& r:records)if(r.parameters[0]==21){if(r.parameters[4]<0 || std::size_t(r.parameters[4])>=sprite.animations().size())throw FormatError("Missing prompt animation");prompts.emplace_back(r);}
    promptSprite_=&sprite;promptRenderer_=std::make_unique<SpriteRenderer>(sprite);rebuildOrder(records);cacheBuckets();
}
int CombatWorld::visiblePrompts()const{const auto [cx,cy]=referenceCamera();int count=0;for(const auto& p:prompts)if(p.bucket<=1 && p.visible(player_,{cx,cy,cx+240,cy+320}))++count;return count;}
void CombatWorld::drawPrompts(Canvas& c,int cx,int cy,BitmapFont& font){
    const auto clip=c.clip();if(clip.right<=clip.left || clip.bottom<=clip.top)return;
    for(const auto& p:prompts)if(!(p.flags&128) && p.visible(player_,{cx,cy,cx+c.width(),cy+c.height()})){
        if(p.animation==14){promptRenderer_->drawAnimation(c,p.animation,p.step,p.x-cx,p.y-cy,{0,unsigned(p.flags)&3,{}});continue;}
        const auto text=desktopPromptText(p.animation);const int width=font.measure(text),height=font.height();
        const int low=clip.left+width/2+4,high=clip.right-(width+1)/2-4;const int x=low<=high?std::clamp(p.x-cx,low,high):(clip.left+clip.right)/2;
        const int y=std::clamp(p.y-cy-height/2,clip.top,std::max(clip.top,clip.bottom-height-6));
        c.blit(Pixels(std::size_t(width+8)*(height+6),0xcc000000),width+8,height+6,x-width/2-4,y-3);font.draw(c,text,x,y,1);
    }
}
void CombatWorld::loadBeamEmitters(const BSprite& sprite,const std::vector<ActorRecord>& records,const std::vector<int>& stats,const std::array<int,256>& trig){
    if(!beamEmitters.empty())throw FormatError("Beam emitters already loaded");
    for(const auto& r:records)if(r.parameters[0]==10)beamEmitters.emplace_back(sprite,effectSprite_,maps_,r,stats,trig);
    rebuildOrder(records);cacheBuckets();
}
void CombatWorld::loadEnergyBarriers(const BSprite& sprite,const std::vector<ActorRecord>& records){
    if(detached_ || !energyBarriers.empty() || !barrierTriggers.empty())throw FormatError("Energy barriers require a live world and a single load");
    energyBarriers.reserve(records.size());barrierTriggers.reserve(records.size());
    for(const auto& r:records)if(r.parameters[0]==80)energyBarriers.emplace_back(sprite,r);
    for(const auto& r:records)if(barrierTriggerRecord(r,records))barrierTriggers.emplace_back(r);
    rebuildOrder(records);cacheBuckets();
}
void CombatWorld::loadTimedSwitches(const BSprite& sprite,const std::vector<ActorRecord>& records){
    if(detached_ || !timedSwitches.empty())throw FormatError("Timed switches require a live world and a single load");
    timedSwitches.reserve(records.size());for(const auto& r:records)if(r.parameters[0]==89)timedSwitches.emplace_back(sprite,r);
    rebuildOrder(records);cacheBuckets();
}
void CombatWorld::loadGates(const BSprite& sprite,LevelMaps& maps,const std::vector<ActorRecord>& records) {
    if(&maps!=&maps_ || !gates.empty())throw FormatError("Gate owner needs the shared collision map and a single load");
    for(const auto& r:records)if(r.parameters[0]==31){gates.emplace_back(sprite,maps,r);gateIndex_.push_back(&gates.back());}
    rebuildOrder(records);cacheBuckets();
}
const int* CombatWorld::resolveAction(int uid) const {
    if(player_.uid==uid)return &player_.action;
    for(const auto& slot:order_)switch(slot.kind) {
    case ActorKind::EnergyBarrier:if(energyBarriers[slot.index].uid==uid && energyBarriers[slot.index].alive)return &energyBarriers[slot.index].action;break;
    case ActorKind::TimedSwitch:if(timedSwitches[slot.index].uid==uid)return &timedSwitches[slot.index].action;break;
    case ActorKind::FlyingEnemy:if(flyingEnemies[slot.index].uid==uid && flyingEnemies[slot.index].alive)return &flyingEnemies[slot.index].action;break;
    case ActorKind::Enemy:if(enemies[slot.index].uid==uid && enemies[slot.index].alive)return &enemies[slot.index].action;break;
    case ActorKind::Platform:if(platformIndex_[slot.index]->uid==uid)return &platformIndex_[slot.index]->action;break;
    case ActorKind::Bridge:if(bridgeIndex_[slot.index]->uid==uid && bridgeIndex_[slot.index]->alive)return &bridgeIndex_[slot.index]->action;break;
    case ActorKind::Gate:if(gateIndex_[slot.index]->uid==uid)return &gateIndex_[slot.index]->action;break;
    default:break;
    }
    return nullptr;
}
void CombatWorld::loadGateScripts(ByteView bytes) {
    if(gateScript_)throw FormatError("Gate scripts already loaded");
    auto scripts=decodeScripts(bytes);
    // The selected script is the one named by the placed removed-source triggers (165 when none are placed).
    int wanted=165;
    for(const auto& t:removedScriptTriggers){if(&t!=&removedScriptTriggers.front() && t.scriptUid!=wanted)throw FormatError("Only one selected gate script is supported");wanted=t.scriptUid;}
    for(auto& script:scripts)if(script.uid==wanted){if(gateScript_)throw FormatError("Duplicate gate script UID");gateScript_=std::make_unique<GateScript>(std::move(script));}
}
void CombatWorld::startGateScript(int uid) {
    if(!gateScript_ || gateScript_->uid()!=uid)throw FormatError("Unsupported gate script UID");
    gateScript_->start();
}
std::optional<int> CombatWorld::liveEnemyIndex(int uid) const {
    bool alive=false;
    for(const auto& e:enemies)if(e.uid==uid && e.alive)alive=true;
    for(const auto& e:flyingEnemies)if(e.uid==uid && e.alive)alive=true;
    for(const auto& e:type3Enemies)if(e.uid==uid && e.alive)alive=true;
    if(alive)for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==uid)return static_cast<int>(i);
    return std::nullopt;
}
EnableEnemyTarget CombatWorld::enemyEnableTarget(int uid){
    for(auto& e:enemies)if(e.uid==uid && e.alive)return {&e.flags,&e.action};
    for(auto& e:flyingEnemies)if(e.uid==uid && e.alive)return {&e.flags,&e.action};
    for(auto& e:type3Enemies)if(e.uid==uid && e.alive)return {&e.flags,&e.action};
    if(containers_)for(auto& c:*containers_)if(c.uid==uid && c.alive)return {&c.flags,&c.action,[this,&c]{effects.emplace_back(effectSprite_,CombatEffectSpawn{28,c.x,c.y,c.flags&1});++effectsSpawned;++containerEnables;}};
    return {};
}
void CombatWorld::finishContainerSources(const std::vector<Container>& containers,bool paused) {
    if(!containerSourcesPending_)return;
    containerSourcesPending_=false;
    for(const auto& c:containers)if(!c.alive)for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==c.uid)placedRemoved_[i]=true;
    for(const auto& slot:updateQueue_)if(slot.kind==ActorKind::RemovedScriptTrigger) {
        auto& t=removedScriptTriggers[slot.index];std::optional<int> source;if(!t.deferred)continue;
        for(const auto& c:containers)if(c.uid==t.sourceUid && c.alive)for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==c.uid){source=static_cast<int>(i);break;}
        if(gateScript_){t.update(source,placedRemoved_,*gateScript_,paused);if(!t.alive)for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==t.uid)placedRemoved_[i]=true;}
    }
}
void CombatWorld::rebuildOrder(const std::vector<ActorRecord>& records) {
    order_.clear();
    for(const auto& r:records){
        const auto& p=r.parameters;
        if(p[0]==80)for(std::size_t i=0;i<energyBarriers.size();++i)if(energyBarriers[i].uid==p[1])order_.push_back({ActorKind::EnergyBarrier,i});
        if(p[0]==40)for(std::size_t i=0;i<barrierTriggers.size();++i)if(barrierTriggers[i].uid==p[1])order_.push_back({ActorKind::BarrierTrigger,i});
        if(p[0]==89)for(std::size_t i=0;i<timedSwitches.size();++i)if(timedSwitches[i].uid==p[1])order_.push_back({ActorKind::TimedSwitch,i});
        if(p[0]==20)for(std::size_t i=0;i<exits.size();++i)if(exits[i].uid==p[1])order_.push_back({ActorKind::ExitTrigger,i});
        if(p[0]==49)for(std::size_t i=0;i<cameraControllers.size();++i)if(cameraControllers[i].uid==p[1])order_.push_back({ActorKind::CameraController,i});
        if(p[0]==40)for(std::size_t i=0;i<promptRemovalTriggers.size();++i)if(promptRemovalTriggers[i].uid==p[1])order_.push_back({ActorKind::PromptRemovalTrigger,i});
        if(p[0]==40)for(std::size_t i=0;i<chandelierTriggers.size();++i)if(chandelierTriggers[i].uid==p[1])order_.push_back({ActorKind::ChandelierTrigger,i});
        if(p[0]==20)for(std::size_t i=0;i<introTriggers.size();++i)if(introTriggers[i].uid==p[1])order_.push_back({ActorKind::IntroTrigger,i});
        if(p[0]==21)for(std::size_t i=0;i<prompts.size();++i)if(prompts[i].uid==p[1])order_.push_back({ActorKind::Prompt,i});
        if(p[0]==10)for(std::size_t i=0;i<beamEmitters.size();++i)if(beamEmitters[i].uid==p[1])order_.push_back({ActorKind::BeamEmitter,i});
        if(p[0]==23)for(std::size_t i=0;i<checkpoints.size();++i)if(checkpoints[i].uid==p[1])order_.push_back({ActorKind::Checkpoint,i});
        if(p[0]==1 && !(p[5]&256))for(std::size_t i=0;i<enemies.size();++i)if(enemies[i].uid==p[1])order_.push_back({ActorKind::Enemy,i});
        if(p[0]==2)for(std::size_t i=0;i<flyingEnemies.size();++i)if(flyingEnemies[i].uid==p[1])order_.push_back({ActorKind::FlyingEnemy,i});
        if(p[0]==3)for(std::size_t i=0;i<type3Enemies.size();++i)if(type3Enemies[i].uid==p[1])order_.push_back({ActorKind::Type3Enemy,i});
        if(p[0]==40)for(std::size_t i=0;i<removedEnemyTriggers.size();++i)if(removedEnemyTriggers[i].uid==p[1])order_.push_back({ActorKind::RemovedEnemyTrigger,i});
        if(p[0]==33 || p[0]==50)for(std::size_t i=0;i<platformIndex_.size();++i)if(platformIndex_[i]->uid==p[1])order_.push_back({ActorKind::Platform,i});
        if(p[0]==45)for(std::size_t i=0;i<bridgeIndex_.size();++i)if(bridgeIndex_[i]->uid==p[1])order_.push_back({ActorKind::Bridge,i});
        if(p[0]==22)for(std::size_t i=0;i<regions_.size();++i)if(regions_[i].uid==p[1])order_.push_back({ActorKind::Region,i});
        if(p[0]==40)for(std::size_t i=0;i<linkedAreaTriggers.size();++i)if(linkedAreaTriggers[i].uid==p[1])order_.push_back({ActorKind::LinkedAreaTrigger,i});
        if(p[0]==40)for(std::size_t i=0;i<playerEnableTriggers.size();++i)if(playerEnableTriggers[i].uid==p[1])order_.push_back({ActorKind::PlayerEnableTrigger,i});
        if(p[0]==59)for(std::size_t i=0;i<passages.size();++i)if(passages[i].uid==p[1])order_.push_back({ActorKind::Passage,i});
        if(p[0]==31)for(std::size_t i=0;i<gateIndex_.size();++i)if(gateIndex_[i]->uid==p[1])order_.push_back({ActorKind::Gate,i});
        if(p[0]==40)for(std::size_t i=0;i<removedScriptTriggers.size();++i)if(removedScriptTriggers[i].uid==p[1])order_.push_back({ActorKind::RemovedScriptTrigger,i});
    }
}
std::pair<int,int> CombatWorld::referenceCamera() const {
    if(intro && intro->active())return {intro->cameraX,intro->cameraY};
    if(camera.uid!=-1)return {camera.x,camera.y};
    return {std::clamp(player_.x-120,0,std::max(0,maps_.layers[1].width*20-240)),
        std::clamp(player_.y-220,0,std::max(0,maps_.layers[1].height*20-320))};
}
void CombatWorld::cacheBuckets() {
    const auto [cx,cy]=referenceCamera();
    for(std::size_t i=0;i<enemies.size();++i) buckets_[i]=std::abs(enemies[i].x-(cx+120))/120+std::abs(enemies[i].y-(cy+160))/160;
    for(auto& t:energyBarriers)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& t:barrierTriggers)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& t:timedSwitches)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& e:flyingEnemies)e.bucket=std::abs(e.x-(cx+120))/120+std::abs(e.y-(cy+160))/160;
    type3Buckets_.resize(type3Enemies.size());for(std::size_t i=0;i<type3Enemies.size();++i)type3Buckets_[i]=std::abs(type3Enemies[i].x-(cx+120))/120+std::abs(type3Enemies[i].y-(cy+160))/160;
    for(auto& t:removedEnemyTriggers)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& p:platforms)p.bucket=std::abs(p.x-(cx+120))/120+std::abs(p.y-(cy+160))/160;
    for(auto& b:bridges)b.bucket=std::abs(b.x-(cx+120))/120+std::abs(b.y-(cy+160))/160;
    for(auto& t:linkedAreaTriggers)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& g:gates)g.bucket=std::abs(g.x-(cx+120))/120+std::abs(g.y-(cy+160))/160;
    for(auto& t:passages)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& t:playerEnableTriggers)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& t:exits)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& t:cameraControllers)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& t:promptRemovalTriggers)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& t:chandelierTriggers)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& t:introTriggers)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& p:prompts)p.bucket=std::abs(p.x-(cx+120))/120+std::abs(p.y-(cy+160))/160;
    for(auto& e:beamEmitters)e.bucket=std::abs(e.x-(cx+120))/120+std::abs(e.y-(cy+160))/160;
    for(auto& t:checkpoints)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& t:removedScriptTriggers)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
}
const std::vector<ContactBody*>& CombatWorld::prepare(bool playerPaused) {
    if(detached_)throw FormatError("Detached world cannot prepare actors");
    loot.clear(); playerEffects_.clear(); bodies_.clear(); bodyPointers_.clear();
    for(auto& e:enemies) { e.effects.clear(); e.loot.clear(); }
    for(auto& e:flyingEnemies){e.effects.clear();e.loot.clear();}
    for(auto& e:type3Enemies){e.effects.clear();e.loot.clear();}
    for(const auto& slot:contactQueue_){
        if(slot.kind==ActorKind::EnergyBarrier){bodies_.push_back(energyBarriers[slot.index].body());continue;}
        auto& e=enemyAt(slot);
        if(e.alive && !playerPaused && !(player_.flags&(1024|16384)) && player_.animation!=9 && meters_.health>0)e.refreshBounds();bodies_.push_back(e.body());
    }
    for(auto& b:bodies_) bodyPointers_.push_back(&b);
    player_.deathRequested=meters_.health<=0;
    const auto [cx,cy]=referenceCamera(); player_.damageView={cx,cy,cx+240,cy+320};
    return bodyPointers_;
}
void CombatWorld::updatePlayer(unsigned pressed,unsigned held,int frame,bool paused) {
    if(detached_)throw FormatError("Detached world cannot update player");
    if(!paused){if(promptMagicTicks>0)--promptMagicTicks;else recentPromptMagic=-1;}
    if(gateScript_){const auto [cx,cy]=referenceCamera();gateScript_->update([&](int uid)->LinkedGate*{for(auto& g:gates)if(g.uid==uid)return &g;return nullptr;},cx,cy,paused);}
    if(!chandelierScripts.empty()){const auto [cx,cy]=referenceCamera();
        // Original slot semantics: a finishing script frees the LAST slot (cj--), so a script that outlives an earlier one would run
        // as a world-freezing background script. That order cannot be reproduced here and is rejected rather than approximated.
        for(const auto& c:chandelierScripts)if(c->ticks>=0 && !selected_.contains(c->uid()))throw FormatError("Selected-script slot order would run a chandelier script as a background script");
        for(int slot=0;slot<selected_.count;++slot){auto* script=chandelierScriptFor(selected_.uids[slot]);if(!script)continue;script->update([&](int uid)->ChandelierActor{
        for(std::size_t i=0;i<enemies.size();++i){auto& e=enemies[i];if(e.uid==uid && e.alive)return {&e.x,&e.y,&e.flags,&e.action,&buckets_[i],[&e](int n){e.scriptAnimation(n);},[&e]{e.refreshBounds();},[&e](int n){e.scriptAction(n);},&e.facingLeft};}
        for(auto& p:platforms)if(p.uid==uid)return {&p.x,&p.y,&p.flags,&p.action,&p.bucket,[&p](int n){p.scriptAnimation(n);},[&p]{p.refreshBounds();}};
        return {};
    },cx,cy,paused);}}
    const auto& bodies=prepare(paused);
    if(fireMagic && !paused && !(player_.flags&(1024|16384)) && !player_.stoppedByHazard()){
        fireMagic->regenerate(meters_,player_.action);player_.availableMagic=meters_.magic;player_.fireOccupied=fireMagic->occupied();
    }
    player_.iceOwned=meters_.iceUnlocked;
    PlayerCombatContext combat{damage,meters_.health,meters_.maxHealth,random,playerEffects_,playerHits};
    player_.update(pressed,held,frame,paused,bodies,&combat);
    if(player_.weaponSwitched){++weaponSwitches;promptSwitchEvent=1;}
    if(fireMagic && player_.insufficientMagic && fireMagic->warningMs<=0)fireMagic->warningMs=1000;
}
void CombatWorld::updateRegion(std::size_t i) {
    const auto& region=regions_[i];const auto& r=region.bounds;
    if(region.flags&(1024|16384))return;
    if(player_.x>r[0] && player_.x<=r[2] && player_.y>r[1] && player_.y<=r[3]) {
        regionChanged_=false;
        if(static_cast<int>(i)!=region_) {previousRegion_=region_;region_=static_cast<int>(i);regionChanged_=true;}
        regionCameraBounds_=r;if(!camera.active)camera.bounds=r;
    }
}
void CombatWorld::refreshRegionSelection() {
    // Explicit Game.k refresh remains available for controlled loading/reset paths.
    for(std::size_t i=0;i<regions_.size();++i)if(static_cast<int>(i)!=region_) {
        updateRegion(i);if(regionChanged_)return;
    }
}
bool CombatWorld::eligible(const ActorSlot& slot) const {
    int flags=0,bucket=0,radius=2;
    switch(slot.kind) {
    case ActorKind::EnergyBarrier:{const auto& t=energyBarriers[slot.index];if(!t.alive)return false;flags=t.flags;bucket=t.bucket;break;}
    case ActorKind::BarrierTrigger:{const auto& t=barrierTriggers[slot.index];if(!t.alive)return false;flags=t.flags;bucket=t.bucket;radius=7;break;}
    case ActorKind::TimedSwitch:{const auto& t=timedSwitches[slot.index];flags=t.flags;bucket=t.bucket;break;}
    case ActorKind::Enemy: {const auto& e=enemies[slot.index];if(!e.alive)return false;flags=e.flags;bucket=buckets_[slot.index];break;}
    case ActorKind::FlyingEnemy:{const auto& e=flyingEnemies[slot.index];if(!e.alive)return false;flags=e.flags;bucket=e.bucket;break;}
    case ActorKind::Type3Enemy:{const auto& e=type3Enemies[slot.index];if(!e.alive)return false;flags=e.flags;bucket=type3Buckets_[slot.index];break;}
    case ActorKind::RemovedEnemyTrigger:{const auto& t=removedEnemyTriggers[slot.index];if(!t.alive)return false;flags=t.flags;bucket=t.bucket;radius=7;break;}
    case ActorKind::Platform: {const auto& p=*platformIndex_[slot.index];flags=p.flags;bucket=p.bucket;break;}
    case ActorKind::Bridge: {const auto& b=*bridgeIndex_[slot.index];if(!b.alive)return false;flags=b.flags;bucket=b.bucket;radius=4;break;}
    case ActorKind::ExitTrigger:{const auto& t=exits[slot.index];flags=t.flags;bucket=t.bucket;break;}
    case ActorKind::CameraController:return cameraControllers[slot.index].eligible(player_);
    case ActorKind::PromptRemovalTrigger:{const auto& t=promptRemovalTriggers[slot.index];if(!t.alive)return false;flags=t.flags;bucket=t.bucket;radius=7;break;}
    case ActorKind::ChandelierTrigger:{const auto& t=chandelierTriggers[slot.index];if(!t.alive)return false;flags=t.flags;bucket=t.bucket;radius=7;break;}
    case ActorKind::IntroTrigger:{const auto& t=introTriggers[slot.index];if(!t.alive)return false;flags=t.flags;bucket=t.bucket;break;}
    case ActorKind::Prompt:{const auto& p=prompts[slot.index];if(!p.alive)return false;flags=p.flags;bucket=p.bucket;break;}
    case ActorKind::BeamEmitter:{const auto& e=beamEmitters[slot.index];flags=e.flags;bucket=e.bucket;break;}
    case ActorKind::Checkpoint:{const auto& c=checkpoints[slot.index];if(!c.alive)return false;flags=c.flags;bucket=c.bucket;break;}
    case ActorKind::Region:flags=regions_[slot.index].flags;break;
    case ActorKind::RemovedScriptTrigger: {const auto& t=removedScriptTriggers[slot.index];if(!t.alive)return false;flags=t.flags;bucket=t.bucket;radius=7;break;}
    case ActorKind::PlayerEnableTrigger: {const auto& t=playerEnableTriggers[slot.index];if(!t.alive)return false;flags=t.flags;bucket=t.bucket;radius=7;break;}
    case ActorKind::Passage: {const auto& t=passages[slot.index];flags=t.flags;bucket=t.bucket;break;}
    case ActorKind::Gate: {const auto& g=*gateIndex_[slot.index];flags=g.flags;bucket=g.bucket;break;}
    case ActorKind::LinkedAreaTrigger: {const auto& t=linkedAreaTriggers[slot.index];flags=t.flags;bucket=t.bucket;radius=7;break;}
    }
    if(flags&16)return true;
    if(flags&(256|32))return false;
    return bucket<radius;
}
std::vector<int> CombatWorld::updateQueueUids() const {
    std::vector<int> result;
    for(const auto& slot:updateQueue_)switch(slot.kind) {
    case ActorKind::EnergyBarrier:result.push_back(energyBarriers[slot.index].alive?energyBarriers[slot.index].uid:-999);break;
    case ActorKind::BarrierTrigger:result.push_back(barrierTriggers[slot.index].alive?barrierTriggers[slot.index].uid:-999);break;
    case ActorKind::TimedSwitch:result.push_back(timedSwitches[slot.index].uid);break;
    case ActorKind::FlyingEnemy:result.push_back(flyingEnemies[slot.index].uid);break;
    case ActorKind::Type3Enemy:result.push_back(type3Enemies[slot.index].uid);break;
    case ActorKind::RemovedEnemyTrigger:result.push_back(removedEnemyTriggers[slot.index].alive?removedEnemyTriggers[slot.index].uid:-999);break;
    case ActorKind::Enemy:result.push_back(enemies[slot.index].uid);break;
    case ActorKind::Platform:result.push_back(platformIndex_[slot.index]->uid);break;
    case ActorKind::Bridge:result.push_back(bridgeIndex_[slot.index]->uid);break;
    case ActorKind::ExitTrigger:result.push_back(exits[slot.index].uid);break;
    case ActorKind::CameraController:result.push_back(cameraControllers[slot.index].alive?cameraControllers[slot.index].uid:-999);break;
    case ActorKind::PromptRemovalTrigger:result.push_back(promptRemovalTriggers[slot.index].alive?promptRemovalTriggers[slot.index].uid:-999);break;
    case ActorKind::ChandelierTrigger:result.push_back(chandelierTriggers[slot.index].uid);break;
    case ActorKind::IntroTrigger:result.push_back(introTriggers[slot.index].uid);break;
    case ActorKind::Prompt:result.push_back(prompts[slot.index].alive?prompts[slot.index].uid:-999);break;
    case ActorKind::BeamEmitter:result.push_back(beamEmitters[slot.index].uid);break;
    case ActorKind::Checkpoint:result.push_back(checkpoints[slot.index].uid);break;
    case ActorKind::Region:result.push_back(regions_[slot.index].uid);break;
    case ActorKind::RemovedScriptTrigger:result.push_back(removedScriptTriggers[slot.index].uid);break;
    case ActorKind::PlayerEnableTrigger:result.push_back(playerEnableTriggers[slot.index].alive?playerEnableTriggers[slot.index].uid:-999);break;
    case ActorKind::Passage:result.push_back(passages[slot.index].uid);break;
    case ActorKind::Gate:result.push_back(gateIndex_[slot.index]->uid);break;
    case ActorKind::LinkedAreaTrigger:result.push_back(linkedAreaTriggers[slot.index].uid);break;
    }
    return result;
}
void CombatWorld::applyRegionTransition() {
    if(!regionChanged_)return;
    for(auto& e:enemies)if(e.alive) {
        // New-region membership wins when an enemy straddles the boundary.
        if(e.regionRespawnable()) {
            if(overlap(e.collision,regions_[region_].bounds)) e.flags&=~1024;
            else if(previousRegion_>=0 && overlap(e.collision,regions_[previousRegion_].bounds)) e.resetForRegionExit();
        } else if(e.action!=0) e.resetForRegionExit();
    }
    for(auto& e:flyingEnemies)if(e.alive){
        if(e.regionRespawnable()){
            if(overlap(e.collision,regions_[region_].bounds))e.flags&=~1024;
            else if(previousRegion_>=0 && overlap(e.collision,regions_[previousRegion_].bounds))e.resetForRegionExit();
        }else if(e.action!=0)e.resetForRegionExit();
    }
    for(auto& e:type3Enemies)if(e.alive){
        if(e.regionRespawnable()){
            if(overlap(e.collision,regions_[region_].bounds))e.flags&=~1024;
            else if(previousRegion_>=0 && overlap(e.collision,regions_[previousRegion_].bounds)){e.resetForRegionExit();if(!e.alive)e.removeAttachments();}
        }else if(e.action!=0){e.resetForRegionExit();if(!e.alive)e.removeAttachments();}
    }
    for(auto& b:energyBarriers)if(b.alive && (b.flags&8192)){
        if(overlap(b.collision,regions_[region_].bounds))b.flags&=~1024;
        else if(previousRegion_>=0 && overlap(b.collision,regions_[previousRegion_].bounds))b.resetForRegionExit();
    }
    for(auto& t:barrierTriggers)if(t.alive && (t.flags&8192)){
        if(overlap(t.collision,regions_[region_].bounds))t.flags&=~1024;
        else if(previousRegion_>=0 && overlap(t.collision,regions_[previousRegion_].bounds))t.resetForRegionExit();
    }
    if(thrownObjects)thrownObjects->clear();
    // Ordinary transient effects are discarded by Game.W on a region change.
    effects.remove_if([](const auto& e){return removeTransientOnRegionChange(39,-1,e.animation);});
    if(fireMagic)fireMagic->clear();
    if(iceMagic)iceMagic->clear();
    regionChanged_=false;
}
bool CombatWorld::finishPlayer(bool paused) {
    if(detached_)throw FormatError("Detached world cannot dispatch actors");
    containerSourcesPending_=false;
    if(iceMagic)for(auto& e:iceMagic->projectiles)e.updatedThisTick=false;
    for(std::size_t i=0;i<contactQueue_.size();++i){const auto& slot=contactQueue_[i];if(slot.kind==ActorKind::EnergyBarrier){energyBarriers[slot.index].applyContact(bodies_[i]);continue;}auto& e=enemyAt(slot);e.applyContact(bodies_[i]);}
    if(player_.animation==9 || player_.deathComplete || player_.stoppedByHazard()) return false;
    // Player-spawned blood exists before Game rebuilds the nonplayer queue,
    // so its first handler runs this tick. Enemy-spawned effects join later.
    for(const auto& spawn:playerEffects_) { effects.emplace_back(effectSprite_,spawn); ++effectsSpawned; }
    playerEffects_.clear();
    if(camera.uid==-1){const auto [cx,cy]=referenceCamera();camera.x=camera.targetX=cx;camera.y=camera.targetY=cy;camera.vx=camera.vy=0;}
    const bool regionCleanup=regionChanged_;
    applyRegionTransition();
    for(auto& effect:effects) effect.update(paused);
    containerQueue_.clear();
    if(containers_)for(const auto& c:*containers_)if(c.alive && (!(c.flags&32) || (c.flags&16)))containerQueue_.push_back(c.uid);
    queue_.clear();updateQueue_.clear();contactQueue_.clear();
    for(const auto& slot:order_)if(eligible(slot)) {
        updateQueue_.push_back(slot);
        if(slot.kind==ActorKind::Enemy || slot.kind==ActorKind::FlyingEnemy || slot.kind==ActorKind::Type3Enemy || slot.kind==ActorKind::EnergyBarrier)contactQueue_.push_back(slot);
        if(slot.kind==ActorKind::Enemy)queue_.push_back(static_cast<int>(slot.index));
    }
    EnemyTarget target{player_.x,player_.fixedX,player_.collision,player_.attack,player_.fixedY};
    CombatExchange exchange{&player_,&damage,&meters_,&random,&playerEffects_,&playerHits,&enemyHits};
    std::vector<FallingTarget> targets;
    for(const auto& slot:updateQueue_) {
        if(slot.kind==ActorKind::Bridge)targets.emplace_back(bridgeIndex_[slot.index]);
        else if(slot.kind==ActorKind::FlyingEnemy)targets.emplace_back(static_cast<EnemyType1*>(&flyingEnemies[slot.index]));
        else if(slot.kind==ActorKind::Enemy)targets.emplace_back(&enemies[slot.index]);
        else if(slot.kind==ActorKind::Type3Enemy)targets.emplace_back(static_cast<EnemyType1*>(&type3Enemies[slot.index]));
    }
    for(auto& p:platforms)if(p.type==33 && player_.platform==&p.surface && player_.ceiling)p.vy=0;
    for(const auto& slot:updateQueue_) {
        if(slot.kind==ActorKind::EnergyBarrier){auto& b=energyBarriers[slot.index];b.update(paused);if(!b.alive)for(std::size_t n=0;n<placedUids_.size();++n)if(placedUids_[n]==b.uid)placedRemoved_[n]=true;continue;}
        if(slot.kind==ActorKind::BarrierTrigger){auto& t=barrierTriggers[slot.index];t.update([&](int uid)->BarrierSignal{
            for(auto& e:enemies)if(e.uid==uid && e.alive)return {&e.flags,&e.collision,[&e]{e.refreshBounds();}};
            for(auto& e:flyingEnemies)if(e.uid==uid && e.alive)return {&e.flags,&e.collision,[&e]{e.refreshBounds();}};
            return {};
        },[&](int uid)->BarrierTarget{
            for(auto& b:energyBarriers)if(b.uid==uid && b.alive)return {&b.flags,&b.action,[&b]{b.beginRelease();}};
            for(auto& linked:barrierTriggers)if(linked.uid==uid && linked.alive)return {&linked.flags,nullptr,{}};
            return {};
        },paused);continue;}
        if(slot.kind==ActorKind::TimedSwitch){auto& t=timedSwitches[slot.index];t.update(t.struckBy(player_,fireMagic.get()),[&](int uid){return resolveAction(uid);},paused);continue;}
        if(slot.kind==ActorKind::ExitTrigger){exits[slot.index].update(player_,completion,paused);continue;}
        if(slot.kind==ActorKind::CameraController){auto& c=cameraControllers[slot.index];
            c.update(player_,camera,[&](int uid)->CameraSource{for(auto& e:enemies)if(e.uid==uid && e.alive)return {&e.x,&e.y,&e.alive,[&e]{e.refreshBounds();}};return {};},regionChanged_,paused);
            if(!c.alive)for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==c.uid)placedRemoved_[i]=true;
            continue;
        }
        if(slot.kind==ActorKind::PromptRemovalTrigger){auto& t=promptRemovalTriggers[slot.index];
            t.update([&](int uid)->MovingPlatform*{for(auto& p:platforms)if(p.uid==uid)return &p;return nullptr;},
                [&](int uid)->PlacedPrompt*{for(auto& p:prompts)if(p.uid==uid && p.alive)return &p;return nullptr;},paused);
            if(!t.alive)for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==t.uid || placedUids_[i]==t.targetUid){
                // A missing target is not marked removed merely because the trigger fired.
                if(placedUids_[i]==t.uid || std::any_of(prompts.begin(),prompts.end(),[&](const auto& p){return p.uid==t.targetUid && !p.alive;}))placedRemoved_[i]=true;
            }
            continue;
        }
        if(slot.kind==ActorKind::ChandelierTrigger){auto& t=chandelierTriggers[slot.index];auto* script=chandelierScriptFor(t.scriptUid);if(!script)throw FormatError("Chandelier trigger script is not loaded");const bool wasAlive=t.alive;const int starts=script->starts;t.update(&player_,*script,paused);if(script->starts!=starts)selected_.select(script->uid());if(wasAlive && !t.alive)selected_.release();if(!t.alive)for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==t.uid)placedRemoved_[i]=true;continue;}
        if(slot.kind==ActorKind::IntroTrigger){auto& t=introTriggers[slot.index];if(t.update(player_,paused)){
            const auto [cx,cy]=referenceCamera();intro->start(cx,cy,t.skipAllowed);introInputReset=true;player_.facingLeft=(t.flags&1)!=0;player_.flags=(player_.flags&~1)|int(player_.facingLeft);player_.vx=player_.vy=0;player_.weaponSwitchCooldown=0;
            if(fireMagic)fireMagic->clear();if(iceMagic)iceMagic->clear();effects.clear();
            for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==t.uid)placedRemoved_[i]=true;
        }continue;}
        if(slot.kind==ActorKind::Checkpoint){auto& c=checkpoints[slot.index];if(c.update(player_,paused)){for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==c.uid)placedRemoved_[i]=true;if(checkpointActivated_)checkpointActivated_(c);}continue;}
        if(slot.kind==ActorKind::Passage){
            auto& passage=passages[slot.index];
            if(passage.update(player_,[&](int uid)->const LinkedPassage*{for(const auto& t:passages)if(t.uid==uid)return &t;return nullptr;},paused)){
                ++passageTransfers;lastPassageUid=passage.uid;camera.active=false;camera.uid=-1;camera.vx=camera.vy=0;
                if(!regionChanged_)refreshRegionSelection();
                target={player_.x,player_.fixedX,player_.collision,player_.attack,player_.fixedY};
            }
            continue;
        }
        if(slot.kind==ActorKind::PlayerEnableTrigger){
            auto& t=playerEnableTriggers[slot.index];
            if(t.update(&player_,[&](int uid)->EnableEnemyTarget{for(auto& e:enemies)if(e.uid==uid && e.alive)return {&e.flags,&e.action};for(auto& e:type3Enemies)if(e.uid==uid && e.alive)return {&e.flags,&e.action};return {};},paused)){
                ++playerEnableActivations;for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==t.uid)placedRemoved_[i]=true;
            }
            target={player_.x,player_.fixedX,player_.collision,player_.attack,player_.fixedY};continue;
        }
        if(slot.kind==ActorKind::RemovedEnemyTrigger){
            auto& t=removedEnemyTriggers[slot.index];
            if(t.update([this](int uid){return liveEnemyIndex(uid);},placedRemoved_,[this](int uid){return enemyEnableTarget(uid);},paused)){
                ++removedEnemyActivations;for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==t.uid)placedRemoved_[i]=true;
            }
            continue;
        }
        if(slot.kind==ActorKind::Prompt){auto& p=prompts[slot.index];if(p.update(player_,recentPromptMagic,promptSwitchEvent,paused)){++promptsRemoved;for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==p.uid)placedRemoved_[i]=true;}continue;}
        if(slot.kind==ActorKind::BeamEmitter){auto& e=beamEmitters[slot.index];e.update(player_,paused);
            if(e.crossing){const int source=e.x+(!e.variant && e.direction<8?(e.direction==1?15:-15):0);if(player_.receiveBeamHit(e.damage(),source,e.pushesWhileInvulnerable(),damage,meters_.health,meters_.maxHealth,random,playerEffects_)){++playerHits;++beamHits;}}
            continue;
        }
        if(slot.kind==ActorKind::RemovedScriptTrigger){
            auto& t=removedScriptTriggers[slot.index];
            if(!t.deferred && gateScript_){
                t.updateSources(RemovedScriptTrigger::SourceLookup([this](int uid){return liveEnemyIndex(uid);}),placedRemoved_,*gateScript_,paused);
                if(!t.alive)for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==t.uid)placedRemoved_[i]=true;
            }
            continue;
        }
        if(slot.kind==ActorKind::Gate){gateIndex_[slot.index]->update([&](int uid){return resolveAction(uid);},paused);continue;}
        if(slot.kind==ActorKind::LinkedAreaTrigger){linkedAreaTriggers[slot.index].update(region_<0?nullptr:&regions_[region_].bounds,[&](int uid)->int* {
            for(auto p:platformIndex_)if(p->uid==uid)return &p->flags;
            return nullptr;
        },paused);continue;}
        if(slot.kind==ActorKind::Region){if(!paused && !regionChanged_)updateRegion(slot.index);continue;}
        if(slot.kind==ActorKind::Bridge){bridgeIndex_[slot.index]->update(paused);continue;}
        if(slot.kind==ActorKind::Platform){auto& p=*platformIndex_[slot.index];const int oldKills=p.enemyKills;p.update(player_,paused,&exchange,targets);updateAttachedIce(p,paused);kills+=p.enemyKills-oldKills;continue;}
        const bool flying=slot.kind==ActorKind::FlyingEnemy,thrower=slot.kind==ActorKind::Type3Enemy;
        auto& e=enemyAt(slot); const int health=e.health;
        const bool ran=e.alive && !paused && !(e.flags&(1024|16384));
        if(flying){auto& f=flyingEnemies[slot.index];f.activeRegion=region_>=0?&regions_[region_].bounds:nullptr;f.update(target,paused,&random,&meters_,&exchange);}
        else if(thrower){
            auto& caster=type3Enemies[slot.index];const auto services=type3Services();
            caster.update(target,paused,&random,&meters_,&exchange,services);
            if(caster.attachedType()!=0)caster.dispatchAttached(target,paused,services);
        }else e.update(target,paused,&random,&meters_,&exchange);
        if(health>0 && e.health<=0) ++kills;
        if(!e.alive)for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==e.uid)placedRemoved_[i]=true;
        if(ran && e.alive && region_>=0) {
            const auto& r=regions_[region_].bounds;
            if(flying)flyingEnemies[slot.index].clampToRegion(r);
            else {
            if((r[0]+12>e.collision[0] && e.vx<=0) || (r[2]-12<e.collision[2] && e.vx>=0)) e.vx=0;
            if((r[1]+12>e.collision[1] && e.vy<=0) || (r[3]-12<e.collision[3] && e.vy>=0)) e.vy=0;
            }
        }
        for(const auto& spawn:e.effects) { effects.emplace_back(effectSprite_,spawn); ++effectsSpawned; }
        for(const auto& spawn:e.loot) { loot.push_back(spawn); ++lootSpawned; }
    }
    updateThrownObjects(paused);
    if(fireMagic)fireMagic->update(player_,[&](const FirePulse& pulse){
        if(thrownObjects){const EnemyTarget playerTarget{player_.x,player_.fixedX,player_.collision,player_.attack,player_.fixedY};const auto touches=[](const auto& a,const auto& b){return (a[0]!=a[2] || a[1]!=a[3]) && (b[0]!=b[2] || b[1]!=b[3]) && overlap(a,b);};
            for(auto& shot:thrownObjects->objects)if(shot.alive && !(shot.flags&1024) && (shot.action==103 || (shot.action==102 && shot.timer<=0)) && (touches(shot.collision,pulse.collision) || touches(shot.collision,pulse.attack)))shot.reflect(playerTarget);
        }
        if(pulse.flags&128)return;
        const auto touches=[](const std::array<int,4>& a,const std::array<int,4>& b){return (a[0]!=a[2] || a[1]!=a[3]) && (b[0]!=b[2] || b[1]!=b[3]) && overlap(a,b);};
        for(const auto& targetSlot:updateQueue_){
            if(targetSlot.kind==ActorKind::Bridge){auto& b=*bridgeIndex_[targetSlot.index];if(b.animation==0 && b.alive && b.action==0 && touches(b.collision,pulse.collision))b.activate(pulse.x);continue;}
            if(targetSlot.kind!=ActorKind::Enemy && targetSlot.kind!=ActorKind::FlyingEnemy && targetSlot.kind!=ActorKind::Type3Enemy)continue;
            auto& e=enemyAt(targetSlot);
            if(!e.alive || (e.flags&(1024|16384)) || e.action==3 || e.action==5 || e.action==6 || e.action==7 || e.damageCooldown>0 || (!touches(e.collision,pulse.collision) && !touches(e.collision,pulse.attack)))continue;
            const auto previousEffects=e.effects.size();const int health=e.health;
            const EnemyTarget fireTarget{player_.x,player_.fixedX,player_.collision,player_.attack,player_.fixedY};
            e.applyDamage(15+5*fireMagic->rank,true,fireTarget,{},random);++fireMagic->hits;if(health>0 && e.health<=0)++kills;
            for(std::size_t i=previousEffects;i<e.effects.size();++i){effects.emplace_back(effectSprite_,e.effects[i]);++effectsSpawned;}
        }
    },paused);
    if(iceMagic){const auto [cx,cy]=referenceCamera();const std::array<int,4> view{cx,cy,cx+240,cy+320},region=region_>=0?regions_[region_].bounds:std::array<int,4>{0,0,maps_.width(),maps_.height()};
        iceMagic->update(view,region,[this](IceProjectile& shot){hitIceTargets(shot);},paused);
    }
    for(const auto& spawn:playerEffects_) { effects.emplace_back(effectSprite_,spawn); ++effectsSpawned; }
    containerSourcesPending_=true;return regionCleanup;
}
void CombatWorld::advance() {
    if(detached_)throw FormatError("Detached world cannot advance animations");
    if(thrownObjects)thrownObjects->advanceAnimation();
    if(fireMagic)fireMagic->advance();
    if(iceMagic)iceMagic->advance();
    damage.tick();
    const auto [cx,cy]=referenceCamera(); const std::array<int,4> view{cx,cy,cx+240,cy+320};
    for(std::size_t i=0;i<enemies.size();++i) {
        auto& e=enemies[i]; if(!e.alive || (e.flags&1024)) continue;
        // Actor.isVisible refreshes nearby bounds even if the actor is waiting
        // outside the frozen update queue and ultimately misses the viewport.
        const bool nearby=std::abs(e.x-(cx+120))/120+std::abs(e.y-(cy+160))/160<=1;
        if(nearby)e.refreshBounds();
        if(std::find(queue_.begin(),queue_.end(),static_cast<int>(i))!=queue_.end() || (nearby && overlap(e.visual,view))) {
            if(!nearby)e.refreshBounds();e.advanceAnimation();
        }
    }
    for(std::size_t i=0;i<flyingEnemies.size();++i){
        auto& e=flyingEnemies[i];if(!e.alive || (e.flags&1024))continue;
        const bool nearby=std::abs(e.x-(cx+120))/120+std::abs(e.y-(cy+160))/160<=1;
        if(nearby)e.refreshBounds();
        const bool queued=std::any_of(contactQueue_.begin(),contactQueue_.end(),[&](const auto& slot){return slot.kind==ActorKind::FlyingEnemy && slot.index==i;});
        if(queued || (nearby && overlap(e.visual,view))){if(!nearby)e.refreshBounds();e.advanceAnimation();}
    }
    for(std::size_t i=0;i<type3Enemies.size();++i){
        auto& e=type3Enemies[i];if(!e.alive || (e.flags&1024))continue;
        const bool nearby=std::abs(e.x-(cx+120))/120+std::abs(e.y-(cy+160))/160<=1;
        if(nearby)e.refreshBounds();
        const bool queued=std::any_of(contactQueue_.begin(),contactQueue_.end(),[&](const auto& slot){return slot.kind==ActorKind::Type3Enemy && slot.index==i;});
        if(queued || (nearby && overlap(e.visual,view))){if(!nearby)e.refreshBounds();e.advanceAnimation();}
    }
    for(std::size_t i=0;i<beamEmitters.size();++i){auto& e=beamEmitters[i];const bool queued=std::any_of(updateQueue_.begin(),updateQueue_.end(),[&](const auto& s){return s.kind==ActorKind::BeamEmitter && s.index==i;});if(!(e.flags&1024) && (queued || (e.bucket<=1 && overlap(e.visual,view))))e.advanceAnimation();}
    if(promptSprite_)for(auto& p:prompts)if(p.alive && !(p.flags&1024) && ((p.flags&16) || (!(p.flags&(256|32)) && p.bucket<2) || (p.bucket<=1 && p.visible(player_,view))))p.advanceAnimation(*promptSprite_);
    for(auto& effect:effects) effect.advanceAnimation();
    for(auto& b:energyBarriers){if(b.alive && !(b.flags&1024) && ((b.flags&16) || (!(b.flags&(256|32)) && b.bucket<2) || (b.bucket<=1 && overlap(b.collision,view))))b.advanceAnimation();b.advanceVisual();}
    for(auto& t:timedSwitches)if(!(t.flags&1024) && ((t.flags&16) || (!(t.flags&(256|32)) && t.bucket<2) || (t.bucket<=1 && overlap(t.visual,view))))t.advanceAnimation();
    for(auto& g:gates)if(!(g.flags&1024) && ((g.flags&16) || (!(g.flags&(256|32)) && g.bucket<2) || (g.bucket<=1 && overlap(g.visual,view))))g.advanceAnimation();
    for(auto& b:bridges)if(b.alive && !(b.flags&1024) && (b.bucket<4 || (b.bucket<=4 && overlap(b.collision,view))))b.advanceAnimation();
    for(auto& p:platforms)if(!(p.flags&1024) && ((p.flags&16) || (!(p.flags&(256|32)) && p.bucket<2) || ((p.type!=50 || p.bucket<=4) && overlap(p.type==50?p.limits:p.visual,view)))){p.refreshBounds();p.advanceAnimation();}
    effects.remove_if([](const auto& effect){return !effect.alive;});
    if(!intro || !intro->active())camera.advance(player_,maps_.width(),maps_.height());
    completion.entryFrame();cacheBuckets();++worldFrame_;
}
void CombatWorld::drawEnemies(Canvas& c,int cx,int cy) { for(auto& e:beamEmitters)e.draw(c,cx,cy); for(auto& e:type3Enemies)e.draw(c,cx,cy); for(auto& e:enemies) e.draw(c,cx,cy);for(auto& e:flyingEnemies)e.draw(c,cx,cy); }
void CombatWorld::drawPlatforms(Canvas& c,int cx,int cy) {
    for(auto& g:gates)g.draw(c,cx,cy);for(auto& t:timedSwitches)t.draw(c,cx,cy);for(auto& b:energyBarriers)b.draw(c,cx,cy);
    for(const auto& slot:order_) {
        if(slot.kind==ActorKind::Bridge)bridgeIndex_[slot.index]->draw(c,cx,cy);
        else if(slot.kind==ActorKind::Platform && platformIndex_[slot.index]->type==50)platformIndex_[slot.index]->draw(c,cx,cy,std::max(0,worldFrame_-1));
    }
    for(auto& p:platforms)if(p.type==33)p.draw(c,cx,cy,std::max(0,worldFrame_-1));
}
void CombatWorld::drawEffects(Canvas& c,int cx,int cy,BitmapFont& font) {
    for(auto& e:beamEmitters)e.drawTip(c,cx,cy); if(fireMagic)fireMagic->draw(c,cx,cy);if(iceMagic)iceMagic->draw(c,cx,cy);for(auto& effect:effects) effect.draw(c,cx,cy,&font);if(thrownObjects)thrownObjects->draw(c,cx,cy); }
int CombatWorld::active() const { return static_cast<int>(std::count_if(enemies.begin(),enemies.end(),[](const auto& e){return e.alive && !(e.flags&1024);})); }
void CombatWorld::shotHitsActors(ThrownObject& shot,const EnemyTarget& target){
    for(const auto& slot:updateQueue_){
        if(slot.kind==ActorKind::Bridge){bridgeIndex_[slot.index]->hitByThrown(shot.collision);continue;}
        if(slot.kind!=ActorKind::Enemy && slot.kind!=ActorKind::FlyingEnemy && slot.kind!=ActorKind::Type3Enemy)continue;
        auto& enemy=enemyAt(slot);
        if(!enemy.alive || (enemy.flags&(1024|16384)) || enemy.action==3 || enemy.action==5 || enemy.action==6 || enemy.action==7 || (enemy.collision[0]==enemy.collision[2] && enemy.collision[1]==enemy.collision[3]) || (shot.collision[0]==shot.collision[2] && shot.collision[1]==shot.collision[3]) || !overlap(enemy.collision,shot.collision))continue;
        const auto oldEffects=enemy.effects.size();const int oldHealth=enemy.health;enemy.applyDamage(500,true,target,{},random);++enemyHits;if(oldHealth>0 && enemy.health<=0)++kills;shot.explode();
        for(std::size_t n=oldEffects;n<enemy.effects.size();++n){effects.emplace_back(effectSprite_,enemy.effects[n]);++effectsSpawned;}
    }
}
void CombatWorld::shotHitsPlayer(ThrownObject& shot){if(player_.receiveBeamHit(15,shot.x,false,damage,meters_.health,meters_.maxHealth,random,playerEffects_))++playerHits;}
void CombatWorld::updateThrownObjects(bool paused){
    if(!thrownObjects)return;
    for(auto& object:thrownObjects->objects){
        // A shot born during this tick's handlers joins the original update queue on the next tick.
        if(object.fresh){object.fresh=false;continue;}
        const EnemyTarget target{player_.x,player_.fixedX,player_.collision,player_.attack,player_.fixedY};
        object.update(target,[&](ThrownObject& shot){shotHitsActors(shot,target);},[&](ThrownObject& shot){shotHitsPlayer(shot);},paused);
    }
}
void CombatWorld::hitIceTargets(IceProjectile& shot){
            for(const auto& slot:updateQueue_){
                if(slot.kind==ActorKind::Platform){shot.tryAttach(*platformIndex_[slot.index]);continue;}
                if(shot.animation!=7 || (slot.kind!=ActorKind::Enemy && slot.kind!=ActorKind::FlyingEnemy && slot.kind!=ActorKind::Type3Enemy))continue;
                auto& e=enemyAt(slot);
                const auto touches=[](const auto& a,const auto& b){return (a[0]!=a[2] || a[1]!=a[3]) && (b[0]!=b[2] || b[1]!=b[3]) && overlap(a,b);};
                if(!e.alive || (e.flags&(1024|16384)) || e.action==3 || e.action==5 || e.action==6 || e.action==7 || e.damageCooldown>0 || (!touches(e.collision,shot.collision) && !touches(e.collision,shot.attack)))continue;
                const auto previous=e.effects.size();const int health=e.health;const EnemyTarget target{player_.x,player_.fixedX,player_.collision,player_.attack,player_.fixedY};
                e.applyDamage(15+5*iceMagic->rank,true,target,{},random);++iceMagic->hits;if(health>0 && e.health<=0)++kills;shot.request(1);shot.vx=0;
                for(std::size_t i=previous;i<e.effects.size();++i){effects.emplace_back(effectSprite_,e.effects[i]);++effectsSpawned;}
            }

}
void CombatWorld::updateAttachedIce(MovingPlatform& p,bool paused){
    if(!iceMagic || !p.attachedIce || p.attachedIce->updatedThisTick)return;
    const auto [cx,cy]=referenceCamera();const std::array<int,4> view{cx,cy,cx+240,cy+320},region=region_>=0?regions_[region_].bounds:std::array<int,4>{0,0,maps_.width(),maps_.height()};
    iceMagic->updateOne(*p.attachedIce,view,region,[this](IceProjectile& shot){hitIceTargets(shot);},paused);
}
}
