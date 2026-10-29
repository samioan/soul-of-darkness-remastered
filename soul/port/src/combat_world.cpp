#include "combat_world.h"
#include "bitmap_font.h"
#include "world_transients.h"
#include <algorithm>
#include <cstdlib>
namespace soul {
namespace {
bool overlap(const std::array<int,4>& a,const std::array<int,4>& b) { return a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1]; }
}
CombatWorld::CombatWorld(const BSprite& enemySprite,const BSprite& effectSprite,const LevelMaps& maps,Player& player,PlayerMeters& meters,const std::vector<ActorRecord>& records,const std::vector<int>& stats)
    :effectSprite_(effectSprite),maps_(maps),player_(player),meters_(meters) {
    enemies.reserve(records.size());passages.reserve(records.size());
    for(const auto& r:records) {
        const auto& p=r.parameters;
        if(p[0]!=0)placedUids_.push_back(p[1]);
        if(RemovedScriptTrigger::supported(r)){
            const auto source=std::find_if(records.begin(),records.end(),[&](const auto& row){return row.parameters[0]==52 && row.parameters[1]==p[13];});
            if(source==records.end() || &*source>=&r)throw FormatError("Deferred source trigger requires its earlier placed container");
            removedScriptTriggers.emplace_back(r);
        }
        if(RemovedEnemyTrigger::supported(r)){
            const auto find=[&](int uid,int type){return std::any_of(records.begin(),records.end(),[&](const auto& row){return row.parameters[1]==uid && row.parameters[0]==type;});};
            if((find(p[13],1) && find(p[14],1) && find(p[17],2) && find(p[18],2)) || (find(p[13],2) && find(p[14],2) && find(p[17],52) && find(p[18],52)))removedEnemyTriggers.emplace_back(r);
        }
        if(p[0]==59)passages.emplace_back(r);
        if(PlayerEnableTrigger::supported(r)){
            if(p[13]!=player_.uid)throw FormatError("Enemy enable trigger requires the owned player source");
            for(int slot=16;slot<=18;++slot)if(p[slot]!=-1){
                const auto target=std::find_if(records.begin(),records.end(),[&](const auto& row){return row.parameters[1]==p[slot];});
                if(target==records.end() || target->parameters[0]!=1 || (target->parameters[5]&256))throw FormatError("Player enable targets require supported placed type-1 enemies");
            }
            playerEnableTriggers.emplace_back(r);
        }
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
CombatWorld::~CombatWorld() { if(iceMagic)iceMagic->clear(); for(auto& p:platforms)if(player_.platform==&p.surface)player_.platform=nullptr; }
void CombatWorld::loadCheckpoints(const std::vector<ActorRecord>& records,std::function<void(const Checkpoint&)> activated){
    if(!checkpoints.empty() || checkpointActivated_)throw FormatError("Checkpoints already loaded");
    for(const auto& r:records)if(r.parameters[0]==23)checkpoints.emplace_back(r);
    checkpointActivated_=std::move(activated);rebuildOrder(records);const auto [cx,cy]=referenceCamera();for(auto& c:checkpoints)c.bucket=std::abs(c.x-(cx+120))/120+std::abs(c.y-(cy+160))/160;
}
std::vector<RemovedCheckpointActor> CombatWorld::removedCheckpointActors()const{
    std::vector<RemovedCheckpointActor> result;
    const auto add=[&](const auto& rows){for(const auto& a:rows)if(!a.alive)result.push_back({a.uid,a.flags});};
    add(prompts);add(enemies);add(flyingEnemies);add(checkpoints);add(bridges);add(removedEnemyTriggers);add(removedScriptTriggers);add(playerEnableTriggers);return result;
}
void CombatWorld::restoreCheckpointActors(const CheckpointSnapshot& saved,const std::vector<ActorRecord>& records){
    if(worldFrame_!=0 || !updateQueue_.empty())throw FormatError("Checkpoint restore requires a reconstructed world");
    const auto skip=[&](int uid){const auto r=std::find_if(records.begin(),records.end(),[&](const auto& a){return a.parameters[1]==uid;});return r!=records.end() && saved.skips(*r);};
    const auto remove=[&](auto& rows){for(auto& a:rows)if(skip(a.uid))a.alive=false;};
    remove(prompts);remove(enemies);remove(flyingEnemies);remove(checkpoints);remove(bridges);remove(removedEnemyTriggers);remove(removedScriptTriggers);remove(playerEnableTriggers);
    for(std::size_t i=0;i<placedUids_.size();++i)placedRemoved_[i]=skip(placedUids_[i]);
    region_=-1;previousRegion_=-1;regionChanged_=false;refreshRegionSelection();cacheBuckets();
}
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
void CombatWorld::loadFlyingEnemies(const BSprite& sprite,const std::vector<ActorRecord>& records,const std::vector<int>& stats){
    if(!flyingEnemies.empty())throw FormatError("Flying enemies already loaded");flyingEnemies.reserve(records.size());
    for(const auto& r:records)if(r.parameters[0]==2)flyingEnemies.emplace_back(sprite,maps_,r,stats);
    rebuildOrder(records);cacheBuckets();
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
void CombatWorld::loadGates(const BSprite& sprite,LevelMaps& maps,const std::vector<ActorRecord>& records) {
    if(&maps!=&maps_ || !gates.empty())throw FormatError("Gate owner needs the shared collision map and a single load");
    for(const auto& r:records)if(r.parameters[0]==31){gates.emplace_back(sprite,maps,r);gateIndex_.push_back(&gates.back());}
    rebuildOrder(records);cacheBuckets();
}
const int* CombatWorld::resolveAction(int uid) const {
    if(player_.uid==uid)return &player_.action;
    for(const auto& slot:order_)switch(slot.kind) {
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
    for(auto& script:scripts)if(script.uid==165){if(gateScript_)throw FormatError("Duplicate gate script UID");gateScript_=std::make_unique<GateScript>(std::move(script));}
}
void CombatWorld::startGateScript(int uid) {
    if(!gateScript_ || gateScript_->uid()!=uid)throw FormatError("Unsupported gate script UID");
    gateScript_->start();
}
void CombatWorld::finishContainerSources(const std::vector<Container>& containers,bool paused) {
    if(!containerSourcesPending_)return;
    containerSourcesPending_=false;
    for(const auto& c:containers)if(!c.alive)for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==c.uid)placedRemoved_[i]=true;
    for(const auto& slot:updateQueue_)if(slot.kind==ActorKind::RemovedScriptTrigger) {
        auto& t=removedScriptTriggers[slot.index];std::optional<int> source;
        for(const auto& c:containers)if(c.uid==t.sourceUid && c.alive)for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==c.uid){source=static_cast<int>(i);break;}
        if(gateScript_){t.update(source,placedRemoved_,*gateScript_,paused);if(!t.alive)for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==t.uid)placedRemoved_[i]=true;}
    }
}
void CombatWorld::rebuildOrder(const std::vector<ActorRecord>& records) {
    order_.clear();
    for(const auto& r:records){
        const auto& p=r.parameters;
        if(p[0]==21)for(std::size_t i=0;i<prompts.size();++i)if(prompts[i].uid==p[1])order_.push_back({ActorKind::Prompt,i});
        if(p[0]==10)for(std::size_t i=0;i<beamEmitters.size();++i)if(beamEmitters[i].uid==p[1])order_.push_back({ActorKind::BeamEmitter,i});
        if(p[0]==23)for(std::size_t i=0;i<checkpoints.size();++i)if(checkpoints[i].uid==p[1])order_.push_back({ActorKind::Checkpoint,i});
        if(p[0]==1 && !(p[5]&256))for(std::size_t i=0;i<enemies.size();++i)if(enemies[i].uid==p[1])order_.push_back({ActorKind::Enemy,i});
        if(p[0]==2)for(std::size_t i=0;i<flyingEnemies.size();++i)if(flyingEnemies[i].uid==p[1])order_.push_back({ActorKind::FlyingEnemy,i});
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
    return {std::clamp(player_.x-120,0,std::max(0,maps_.layers[1].width*20-240)),
        std::clamp(player_.y-220,0,std::max(0,maps_.layers[1].height*20-320))};
}
void CombatWorld::cacheBuckets() {
    const auto [cx,cy]=referenceCamera();
    for(std::size_t i=0;i<enemies.size();++i) buckets_[i]=std::abs(enemies[i].x-(cx+120))/120+std::abs(enemies[i].y-(cy+160))/160;
    for(auto& e:flyingEnemies)e.bucket=std::abs(e.x-(cx+120))/120+std::abs(e.y-(cy+160))/160;
    for(auto& t:removedEnemyTriggers)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& p:platforms)p.bucket=std::abs(p.x-(cx+120))/120+std::abs(p.y-(cy+160))/160;
    for(auto& b:bridges)b.bucket=std::abs(b.x-(cx+120))/120+std::abs(b.y-(cy+160))/160;
    for(auto& t:linkedAreaTriggers)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& g:gates)g.bucket=std::abs(g.x-(cx+120))/120+std::abs(g.y-(cy+160))/160;
    for(auto& t:passages)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& t:playerEnableTriggers)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& p:prompts)p.bucket=std::abs(p.x-(cx+120))/120+std::abs(p.y-(cy+160))/160;
    for(auto& e:beamEmitters)e.bucket=std::abs(e.x-(cx+120))/120+std::abs(e.y-(cy+160))/160;
    for(auto& t:checkpoints)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
    for(auto& t:removedScriptTriggers)t.bucket=std::abs(t.x-(cx+120))/120+std::abs(t.y-(cy+160))/160;
}
const std::vector<ContactBody*>& CombatWorld::prepare(bool playerPaused) {
    loot.clear(); playerEffects_.clear(); bodies_.clear(); bodyPointers_.clear();
    for(auto& e:enemies) { e.effects.clear(); e.loot.clear(); }
    for(auto& e:flyingEnemies){e.effects.clear();e.loot.clear();}
    for(const auto& slot:contactQueue_){
        auto& e=slot.kind==ActorKind::Enemy?enemies[slot.index]:static_cast<EnemyType1&>(flyingEnemies[slot.index]);
        if(e.alive && !playerPaused && !(player_.flags&(1024|16384)) && player_.animation!=9 && meters_.health>0)e.refreshBounds();bodies_.push_back(e.body());
    }
    for(auto& b:bodies_) bodyPointers_.push_back(&b);
    player_.deathRequested=meters_.health<=0;
    const auto [cx,cy]=referenceCamera(); player_.damageView={cx,cy,cx+240,cy+320};
    return bodyPointers_;
}
void CombatWorld::updatePlayer(unsigned pressed,unsigned held,int frame,bool paused) {
    if(!paused){if(promptMagicTicks>0)--promptMagicTicks;else recentPromptMagic=-1;}
    if(gateScript_){const auto [cx,cy]=referenceCamera();gateScript_->update([&](int uid)->LinkedGate*{for(auto& g:gates)if(g.uid==uid)return &g;return nullptr;},cx,cy,paused);}
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
        regionCameraBounds_=r;
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
    case ActorKind::Enemy: {const auto& e=enemies[slot.index];if(!e.alive)return false;flags=e.flags;bucket=buckets_[slot.index];break;}
    case ActorKind::FlyingEnemy:{const auto& e=flyingEnemies[slot.index];if(!e.alive)return false;flags=e.flags;bucket=e.bucket;break;}
    case ActorKind::RemovedEnemyTrigger:{const auto& t=removedEnemyTriggers[slot.index];if(!t.alive)return false;flags=t.flags;bucket=t.bucket;radius=7;break;}
    case ActorKind::Platform: {const auto& p=*platformIndex_[slot.index];flags=p.flags;bucket=p.bucket;break;}
    case ActorKind::Bridge: {const auto& b=*bridgeIndex_[slot.index];if(!b.alive)return false;flags=b.flags;bucket=b.bucket;radius=4;break;}
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
    case ActorKind::FlyingEnemy:result.push_back(flyingEnemies[slot.index].uid);break;
    case ActorKind::RemovedEnemyTrigger:result.push_back(removedEnemyTriggers[slot.index].alive?removedEnemyTriggers[slot.index].uid:-999);break;
    case ActorKind::Enemy:result.push_back(enemies[slot.index].uid);break;
    case ActorKind::Platform:result.push_back(platformIndex_[slot.index]->uid);break;
    case ActorKind::Bridge:result.push_back(bridgeIndex_[slot.index]->uid);break;
    case ActorKind::Prompt:result.push_back(prompts[slot.index].uid);break;
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
    // Ordinary transient effects are discarded by Game.W on a region change.
    effects.remove_if([](const auto& e){return removeTransientOnRegionChange(39,-1,e.animation);});
    if(fireMagic)fireMagic->clear();
    if(iceMagic)iceMagic->clear();
    regionChanged_=false;
}
bool CombatWorld::finishPlayer(bool paused) {
    containerSourcesPending_=false;
    if(iceMagic)for(auto& e:iceMagic->projectiles)e.updatedThisTick=false;
    for(std::size_t i=0;i<contactQueue_.size();++i){const auto& slot=contactQueue_[i];auto& e=slot.kind==ActorKind::Enemy?enemies[slot.index]:static_cast<EnemyType1&>(flyingEnemies[slot.index]);e.applyContact(bodies_[i]);}
    if(player_.animation==9 || player_.deathComplete || player_.stoppedByHazard()) return false;
    // Player-spawned blood exists before Game rebuilds the nonplayer queue,
    // so its first handler runs this tick. Enemy-spawned effects join later.
    for(const auto& spawn:playerEffects_) { effects.emplace_back(effectSprite_,spawn); ++effectsSpawned; }
    playerEffects_.clear();
    const bool regionCleanup=regionChanged_;
    applyRegionTransition();
    for(auto& effect:effects) effect.update(paused);
    containerQueue_.clear();
    if(containers_)for(const auto& c:*containers_)if(c.alive && (!(c.flags&32) || (c.flags&16)))containerQueue_.push_back(c.uid);
    queue_.clear();updateQueue_.clear();contactQueue_.clear();
    for(const auto& slot:order_)if(eligible(slot)) {
        updateQueue_.push_back(slot);
        if(slot.kind==ActorKind::Enemy || slot.kind==ActorKind::FlyingEnemy)contactQueue_.push_back(slot);
        if(slot.kind==ActorKind::Enemy)queue_.push_back(static_cast<int>(slot.index));
    }
    EnemyTarget target{player_.x,player_.fixedX,player_.collision,player_.attack,player_.fixedY};
    CombatExchange exchange{&player_,&damage,&meters_,&random,&playerEffects_,&playerHits,&enemyHits};
    std::vector<FallingTarget> targets;
    for(const auto& slot:updateQueue_) {
        if(slot.kind==ActorKind::Bridge)targets.emplace_back(bridgeIndex_[slot.index]);
        else if(slot.kind==ActorKind::FlyingEnemy)targets.emplace_back(static_cast<EnemyType1*>(&flyingEnemies[slot.index]));
        else if(slot.kind==ActorKind::Enemy)targets.emplace_back(&enemies[slot.index]);
    }
    for(auto& p:platforms)if(p.type==33 && player_.platform==&p.surface && player_.ceiling)p.vy=0;
    for(const auto& slot:updateQueue_) {
        if(slot.kind==ActorKind::Checkpoint){auto& c=checkpoints[slot.index];if(c.update(player_,paused)){for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==c.uid)placedRemoved_[i]=true;if(checkpointActivated_)checkpointActivated_(c);}continue;}
        if(slot.kind==ActorKind::Passage){
            auto& passage=passages[slot.index];
            if(passage.update(player_,[&](int uid)->const LinkedPassage*{for(const auto& t:passages)if(t.uid==uid)return &t;return nullptr;},paused)){
                ++passageTransfers;lastPassageUid=passage.uid;
                if(!regionChanged_)refreshRegionSelection();
                target={player_.x,player_.fixedX,player_.collision,player_.attack,player_.fixedY};
            }
            continue;
        }
        if(slot.kind==ActorKind::PlayerEnableTrigger){
            auto& t=playerEnableTriggers[slot.index];
            if(t.update(&player_,[&](int uid)->EnableEnemyTarget{for(auto& e:enemies)if(e.uid==uid && e.alive)return {&e.flags,&e.action};return {};},paused)){
                ++playerEnableActivations;for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==t.uid)placedRemoved_[i]=true;
            }
            target={player_.x,player_.fixedX,player_.collision,player_.attack,player_.fixedY};continue;
        }
        if(slot.kind==ActorKind::RemovedEnemyTrigger){
            auto& t=removedEnemyTriggers[slot.index];
            if(t.update([&](int uid)->std::optional<int>{bool alive=false;for(const auto& e:enemies)if(e.uid==uid && e.alive)alive=true;for(const auto& e:flyingEnemies)if(e.uid==uid && e.alive)alive=true;if(alive)for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==uid)return static_cast<int>(i);return std::nullopt;},placedRemoved_,[&](int uid)->EnableEnemyTarget{for(auto& e:flyingEnemies)if(e.uid==uid && e.alive)return {&e.flags,&e.action};
                if(containers_)for(auto& c:*containers_)if(c.uid==uid && c.alive)return {&c.flags,&c.action,[this,&c]{effects.emplace_back(effectSprite_,CombatEffectSpawn{28,c.x,c.y,c.flags&1});++effectsSpawned;++containerEnables;}};return {};},paused)){
                ++removedEnemyActivations;for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==t.uid)placedRemoved_[i]=true;
            }
            continue;
        }
        if(slot.kind==ActorKind::Prompt){auto& p=prompts[slot.index];if(p.update(player_,recentPromptMagic,promptSwitchEvent,paused)){++promptsRemoved;for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==p.uid)placedRemoved_[i]=true;}continue;}
        if(slot.kind==ActorKind::BeamEmitter){auto& e=beamEmitters[slot.index];e.update(player_,paused);
            if(e.crossing){const int source=e.x+(!e.variant && e.direction<8?(e.direction==1?15:-15):0);if(player_.receiveBeamHit(e.damage(),source,e.pushesWhileInvulnerable(),damage,meters_.health,meters_.maxHealth,random,playerEffects_)){++playerHits;++beamHits;}}
            continue;
        }
        if(slot.kind==ActorKind::RemovedScriptTrigger)continue;
        if(slot.kind==ActorKind::Gate){gateIndex_[slot.index]->update([&](int uid){return resolveAction(uid);},paused);continue;}
        if(slot.kind==ActorKind::LinkedAreaTrigger){linkedAreaTriggers[slot.index].update(region_<0?nullptr:&regions_[region_].bounds,[&](int uid)->int* {
            for(auto p:platformIndex_)if(p->uid==uid)return &p->flags;
            return nullptr;
        },paused);continue;}
        if(slot.kind==ActorKind::Region){if(!paused && !regionChanged_)updateRegion(slot.index);continue;}
        if(slot.kind==ActorKind::Bridge){bridgeIndex_[slot.index]->update(paused);continue;}
        if(slot.kind==ActorKind::Platform){auto& p=*platformIndex_[slot.index];const int oldKills=p.enemyKills;p.update(player_,paused,&exchange,targets);updateAttachedIce(p,paused);kills+=p.enemyKills-oldKills;continue;}
        const bool flying=slot.kind==ActorKind::FlyingEnemy;
        auto& e=flying?static_cast<EnemyType1&>(flyingEnemies[slot.index]):enemies[slot.index]; const int health=e.health;
        const bool ran=e.alive && !paused && !(e.flags&(1024|16384));
        if(flying)flyingEnemies[slot.index].update(target,paused,&random,&meters_,&exchange);else e.update(target,paused,&random,&meters_,&exchange);
        if(health>0 && e.health<=0) ++kills;
        if(!e.alive)for(std::size_t i=0;i<placedUids_.size();++i)if(placedUids_[i]==e.uid)placedRemoved_[i]=true;
        if(ran && e.alive && region_>=0) {
            const auto& r=regions_[region_].bounds;
            if((r[0]+12>e.collision[0] && e.vx<=0) || (r[2]-12<e.collision[2] && e.vx>=0)) e.vx=0;
            if((r[1]+12>e.collision[1] && e.vy<=0) || (r[3]-12<e.collision[3] && e.vy>=0)) e.vy=0;
        }
        for(const auto& spawn:e.effects) { effects.emplace_back(effectSprite_,spawn); ++effectsSpawned; }
        for(const auto& spawn:e.loot) { loot.push_back(spawn); ++lootSpawned; }
    }
    if(fireMagic)fireMagic->update(player_,[&](const FirePulse& pulse){
        if(pulse.flags&128)return;
        const auto touches=[](const std::array<int,4>& a,const std::array<int,4>& b){return (a[0]!=a[2] || a[1]!=a[3]) && (b[0]!=b[2] || b[1]!=b[3]) && overlap(a,b);};
        for(const auto& targetSlot:updateQueue_){
            if(targetSlot.kind==ActorKind::Bridge){auto& b=*bridgeIndex_[targetSlot.index];if(b.animation==0 && b.alive && b.action==0 && touches(b.collision,pulse.collision))b.activate(pulse.x);continue;}
            if(targetSlot.kind!=ActorKind::Enemy && targetSlot.kind!=ActorKind::FlyingEnemy)continue;
            auto& e=targetSlot.kind==ActorKind::Enemy?enemies[targetSlot.index]:static_cast<EnemyType1&>(flyingEnemies[targetSlot.index]);
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
    for(std::size_t i=0;i<beamEmitters.size();++i){auto& e=beamEmitters[i];const bool queued=std::any_of(updateQueue_.begin(),updateQueue_.end(),[&](const auto& s){return s.kind==ActorKind::BeamEmitter && s.index==i;});if(!(e.flags&1024) && (queued || (e.bucket<=1 && overlap(e.visual,view))))e.advanceAnimation();}
    if(promptSprite_)for(auto& p:prompts)if(p.alive && !(p.flags&1024) && ((p.flags&16) || (!(p.flags&(256|32)) && p.bucket<2) || (p.bucket<=1 && p.visible(player_,view))))p.advanceAnimation(*promptSprite_);
    for(auto& effect:effects) effect.advanceAnimation();
    for(auto& g:gates)if(!(g.flags&1024) && ((g.flags&16) || (!(g.flags&(256|32)) && g.bucket<2) || (g.bucket<=1 && overlap(g.visual,view))))g.advanceAnimation();
    for(auto& b:bridges)if(b.alive && !(b.flags&1024) && (b.bucket<4 || (b.bucket<=4 && overlap(b.collision,view))))b.advanceAnimation();
    for(auto& p:platforms)if(!(p.flags&1024) && ((p.flags&16) || (!(p.flags&(256|32)) && p.bucket<2) || ((p.type!=50 || p.bucket<=4) && overlap(p.type==50?p.limits:p.visual,view)))){p.refreshBounds();p.advanceAnimation();}
    effects.remove_if([](const auto& effect){return !effect.alive;}); cacheBuckets();++worldFrame_;
}
void CombatWorld::drawEnemies(Canvas& c,int cx,int cy) { for(auto& e:beamEmitters)e.draw(c,cx,cy); for(auto& e:enemies) e.draw(c,cx,cy);for(auto& e:flyingEnemies)e.draw(c,cx,cy); }
void CombatWorld::drawPlatforms(Canvas& c,int cx,int cy) {
    for(auto& g:gates)g.draw(c,cx,cy);
    for(const auto& slot:order_) {
        if(slot.kind==ActorKind::Bridge)bridgeIndex_[slot.index]->draw(c,cx,cy);
        else if(slot.kind==ActorKind::Platform && platformIndex_[slot.index]->type==50)platformIndex_[slot.index]->draw(c,cx,cy,std::max(0,worldFrame_-1));
    }
    for(auto& p:platforms)if(p.type==33)p.draw(c,cx,cy,std::max(0,worldFrame_-1));
}
void CombatWorld::drawEffects(Canvas& c,int cx,int cy,BitmapFont& font) {
    for(auto& e:beamEmitters)e.drawTip(c,cx,cy); if(fireMagic)fireMagic->draw(c,cx,cy);if(iceMagic)iceMagic->draw(c,cx,cy);for(auto& effect:effects) effect.draw(c,cx,cy,&font); }
int CombatWorld::active() const { return static_cast<int>(std::count_if(enemies.begin(),enemies.end(),[](const auto& e){return e.alive && !(e.flags&1024);})); }
void CombatWorld::hitIceTargets(IceProjectile& shot){
            for(const auto& slot:updateQueue_){
                if(slot.kind==ActorKind::Platform){shot.tryAttach(*platformIndex_[slot.index]);continue;}
                if(shot.animation!=7 || (slot.kind!=ActorKind::Enemy && slot.kind!=ActorKind::FlyingEnemy))continue;
                auto& e=slot.kind==ActorKind::Enemy?enemies[slot.index]:static_cast<EnemyType1&>(flyingEnemies[slot.index]);
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
