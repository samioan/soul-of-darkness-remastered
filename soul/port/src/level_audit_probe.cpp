#include "combat_world.h"
#include "level_data.h"
#include "player_probe.h"
#include <functional>
#include <iostream>
#include <set>
// Per-level construction audit: runs the native loaders in the preview's order and reports the first unsupported record of each.
// It is a progress map, not a behavior check: a loader that succeeds only proves the records fit its supported envelope.
void emitLevelAuditManifest(const std::filesystem::path& root){
    using namespace soul;
    for(int level=0;level<=8;++level){
        auto data=LevelData::load(root,level);
        Player player(data.sprite(data.level.playerSprite),data.scene.maps(),playerRecord(data.records),level);PlayerMeters meters;
        std::unique_ptr<CombatWorld> world;
        const auto step=[&](const char* name,const std::function<void()>& fn){
            std::string result="OK";
            try{fn();}catch(const std::exception& e){result=e.what();}
            std::cout<<level<<'\t'<<name<<'\t'<<result<<'\n';
        };
        const auto& records=data.records;
        step("world",[&]{world=std::make_unique<CombatWorld>(data.sprite(data.level.enemySprite),data.sprite(data.level.effectSprite),data.scene.maps(),player,meters,records,data.stats(0));});
        if(!world)continue;
        step("flying",[&]{world->loadFlyingEnemies(data.sprite(data.level.flyingSprite),records,data.stats(2),&data.trig);});
        step("thrown",[&]{world->loadThrownObjects(data.sprite(data.level.thrownSprite));});
        step("type3",[&]{world->loadType3Enemies(data.sprite(data.level.flyingSprite),records,data.stats(3));});
        step("prompts",[&]{world->loadPrompts(data.sprite(data.level.promptSprite),records);});
        step("beams",[&]{world->loadBeamEmitters(data.sprite(data.level.beamSprite),records,data.stats(9),data.trig);});
        step("platforms",[&]{world->loadPlatforms(data.sprite(data.level.platformSprite),records,&data.trig,data.stats(0).at(1));});
        step("bridges",[&]{world->loadBridges(data.sprite(data.level.bridgeSprite),data.scene.mutableMaps(),records);});
        step("gates",[&]{world->loadGates(data.sprite(data.level.platformSprite),data.scene.mutableMaps(),records);});
        step("switches",[&]{world->loadTimedSwitches(data.sprite(data.level.platformSprite),records);});
        step("barriers",[&]{world->loadEnergyBarriers(data.sprite(data.level.effectSprite),records);});
        step("scripts",[&]{world->loadGateScripts(ByteView(data.scriptBytes));});
        step("chandeliers",[&]{if(std::any_of(records.begin(),records.end(),[](const auto& r){return ChandelierTrigger::supported(r);}))world->loadChandelierScript(ByteView(data.scriptBytes),records);});
        step("containers",[&]{for(const auto& r:records)if(r.parameters[0]==52)Container(data.sprite(data.level.containerSprite),data.sprite(data.level.promptSprite),data.scene.maps(),r);});
        step("pickups",[&]{for(const auto& r:records)if(r.parameters[0]==35)Pickup(data.sprite(data.level.pickupSprite),r);});
        // Smoke run: park the frozen, invulnerable player on every placed record for 40 ticks. A throw means a handler met a state
        // outside its verified subset; success only means this scripted sweep stayed inside it.
        std::set<int> waved;
        step("smoke",[&]{
            std::vector<Container> bound;for(const auto& r:records)if(r.parameters[0]==52)bound.emplace_back(data.sprite(data.level.containerSprite),data.sprite(data.level.promptSprite),data.scene.maps(),r);
            world->bindContainers(bound);world->damage.invulnerability=60000;player.flags=16384;int tick=0;
            for(const auto& r:records){
                const int type=r.parameters[0];if(type==0 || type==40)continue;
                player.x=static_cast<std::uint16_t>(r.parameters[2]);player.y=static_cast<std::uint16_t>(r.parameters[3]);player.fixedX=player.x*256;player.fixedY=player.y*256;player.refreshBounds();
                for(int i=0;i<40;++i,++tick){world->refreshRegionSelection();world->updatePlayer(0,0,tick,false);world->finishPlayer(false);
                    for(auto& c:bound)if(world->containerQueued(c.uid))c.update(player,0,false,false,&world->random,false);
                    world->finishContainerSources(bound,false);world->advance();for(auto& c:bound)c.advanceAnimation();
                    for(const auto& f:world->flyingEnemies)if(f.behavior()==6 && (f.action==138 || f.action==139))waved.insert(f.uid);}
            }
        });
        if(level==1)std::cout<<level<<'\t'<<"smoke-sine-flyers"<<'\t'<<waved.size()<<'\n';
        world->detach();
    }
}
