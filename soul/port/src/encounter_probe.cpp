#include "combat_world.h"
#include "level_data.h"
#include "player_probe.h"
#include "removed_script_trigger.h"
#include <algorithm>
#include <iostream>
#include <map>
#include <memory>
// Second-level first encounter: player-overlap trigger 100 enables enemies 96 (type 1) and 97 (type 3), removal trigger
// 104 enables flying enemies 98/102, and trigger 62 (chest 59) plus script trigger 103 (script 95 -> gate 87) follow
// their removal. The gate-trigger manifest is compared with original bytecode; the world manifest is a controlled fixture.
namespace {
std::uint32_t crc(const soul::Bytes& b){std::uint32_t n=0xffffffff;for(auto v:b){n^=v;for(int j=0;j<8;++j)n=(n>>1)^((n&1)?0xedb88320:0);}return n^0xffffffff;}
const char* flag(bool b){return b?"true":"false";}
}
void emitEncounterGateManifest(const std::filesystem::path& root) {
    const auto sprites=soul::ResourcePack::numeric(soul::readAssetFile(root/"2")),pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"4"));const auto sprite=soul::BSprite::parse(sprites.resource(28).value());
    for(const std::string s:{"normal","reverse","one-left","missing-first","missing-second","early-remove","index-zero","pause","hidden","frozen","source-hidden"}) {
        soul::ScriptData data;for(auto& script:soul::decodeScripts(pack.resource(1).value()))if(script.uid==95)data=std::move(script);soul::GateScript script(std::move(data));
        auto maps=soul::LevelMaps::load(pack,true);soul::ActorRecord gr{},tr{};gr.count=9;gr.parameters={31,87,847,380,4,0,4,-1,-1};soul::LinkedGate gate(sprite,maps,gr);
        tr.count=19;tr.parameters={40,103,658,377,0,8,0,0,30,30,4,1,0,98,102,-1,95,-1,-1};soul::RemovedScriptTrigger trigger(tr);
        if(s=="hidden" || s=="frozen")trigger.flags=140|(s=="hidden"?1024:16384);
        const int index[2]={s=="index-zero"?0:5,s=="index-zero"?0:6};std::vector<bool> removed(200);
        bool alive[2]={s!="missing-first" && s!="early-remove",s!="missing-second" && s!="early-remove"};
        if(s=="early-remove"){removed[index[0]]=true;removed[index[1]]=true;}
        const auto lookup=[&](int uid)->std::optional<int>{if(uid==98 && alive[0])return index[0];if(uid==102 && alive[1])return index[1];return std::nullopt;};
        const auto remove=[&](int n){alive[n]=false;removed[index[n]]=true;};
        for(int f=0;f<48;++f) {
            const bool paused=s=="pause" && f>=3 && f<10;
            script.update([&](int uid)->soul::LinkedGate*{return uid==87?&gate:nullptr;},720,240,paused);
            if(s!="early-remove" && s!="index-zero" && s!="missing-first" && s!="missing-second"){
                const int first=s=="reverse"?1:0;
                if(f==4)remove(first);
                if(f==8 && s!="one-left")remove(1-first);
            }
            if(s=="missing-first" && f==4)remove(1);
            if(s=="missing-second" && f==4)remove(0);
            gate.update([](int)->const int*{return nullptr;},paused);trigger.updateSources(lookup,removed,script,paused);
            for(int phase=0;phase<2;++phase) {
                if(phase==1 && !paused)gate.advanceAnimation();
                std::cout<<"T\t"<<s<<'\t'<<f<<'\t'<<phase;
                for(int n:{trigger.uid,trigger.x,trigger.y,trigger.flags,trigger.indices[0],trigger.indices[1],trigger.latch,trigger.sourceUids[0],trigger.sourceUids[1]})std::cout<<'\t'<<n;
                std::cout<<'\t'<<flag(trigger.sourceCached(0))<<'\t'<<flag(trigger.sourceCached(1))<<'\t'<<flag(trigger.alive);for(int n:trigger.collision)std::cout<<'\t'<<n;
                std::cout<<'\t'<<script.ticks()<<'\t'<<(trigger.latch==1?1:0);for(int n:script.offsets())std::cout<<'\t'<<n;
                std::cout<<'\t'<<flag(removed[index[0]])<<'\t'<<flag(removed[index[1]])<<'\n';
                std::cout<<"G\t"<<s<<'\t'<<f<<'\t'<<phase;gate.write(std::cout);std::cout<<'\t'<<crc(maps.collision->cells)<<'\n';
            }
        }
    }
}
namespace {
struct Kill{int tick;int uid;};
struct Scenario{const char* name;bool overlap;int pausedTicks;std::vector<Kill> kills;int ticks;};
}
// Controlled world fixtures: only the records of this chain are constructed (player, enemies 96/97/98/102, chest 59,
// gate 87, triggers 100/104/62/103) and enemies are removed by direct lethal damage. They are not a level route.
void emitEncounterWorldManifest(const std::filesystem::path& root){
    using namespace soul;
    const std::vector<Scenario> scenarios{
        {"chain",true,0,{{40,96},{40,97},{100,98},{100,102}},260},
        {"reverse",true,0,{{40,97},{50,96},{100,102},{110,98}},260},
        {"paused",true,30,{{70,96},{70,97},{130,98},{130,102}},300},
        {"first-wave-partial",true,0,{{40,96}},200},
        {"second-wave-partial",true,0,{{40,96},{40,97},{100,98}},220},
        {"no-overlap",false,0,{},200}};
    for(const auto& sc:scenarios){
        auto data=LevelData::load(root,1);
        std::vector<ActorRecord> owned;
        for(const auto& r:data.records){
            const int type=r.parameters[0],uid=r.parameters[1];
            if(type==0 || uid==96 || uid==97 || uid==98 || uid==102 || uid==59 || uid==87 || uid==100 || uid==104 || uid==103 || uid==62)owned.push_back(r);
        }
        Player player(data.sprite(data.level.playerSprite),data.scene.maps(),playerRecord(data.records),1);PlayerMeters meters;
        CombatWorld world(data.sprite(data.level.enemySprite),data.sprite(data.level.effectSprite),data.scene.maps(),player,meters,owned,data.stats(0));
        world.loadFlyingEnemies(data.sprite(data.level.flyingSprite),owned,data.stats(2),&data.trig);
        world.loadThrownObjects(data.sprite(data.level.thrownSprite));
        world.loadType3Enemies(data.sprite(data.level.flyingSprite),owned,data.stats(3));
        world.loadGates(data.sprite(data.level.platformSprite),data.scene.mutableMaps(),owned);
        std::vector<Container> containers;for(const auto& r:owned)if(r.parameters[0]==52)containers.emplace_back(data.sprite(data.level.containerSprite),data.sprite(data.level.promptSprite),data.scene.maps(),r);
        world.bindContainers(containers);
        world.loadGateScripts(ByteView(data.scriptBytes));
        const auto enemy=[&](int uid)->EnemyType1*{
            for(auto& e:world.enemies)if(e.uid==uid)return &e;
            for(auto& e:world.flyingEnemies)if(e.uid==uid)return &e;
            for(auto& e:world.type3Enemies)if(e.uid==uid)return &e;
            return nullptr;
        };
        const std::size_t triggerCount=world.playerEnableTriggers.size()+world.removedEnemyTriggers.size()+world.removedScriptTriggers.size();
        const int initial=(enemy(96)->flags&32)+(enemy(97)->flags&32)+(enemy(98)->flags&32)+(enemy(102)->flags&32);
        const bool deferred=world.removedScriptTriggers.empty()?true:world.removedScriptTriggers[0].deferred;
        // Observe the whole first-wave area; enemies are frozen until their trigger enables them.
        if(sc.overlap){player.x=700;player.y=440;}else{player.x=10;player.y=10;}
        player.fixedX=player.x*256;player.fixedY=player.y*256;player.flags=16384;player.refreshBounds();world.damage.invulnerability=60000;world.advance();
        std::map<int,int> strikes;
        int enableTick=-1,waveTick=-1,chestTick=-1,scriptStart=-1,scriptEnd=-1,secondTick=-1;
        for(int tick=0;tick<sc.ticks;++tick){
            const bool paused=tick<sc.pausedTicks;
            world.refreshRegionSelection();
            // A kill entry means "from this tick on, strike the enemy whenever it is alive, enabled and not fading or dying": the shipped
            // enemies 96/97 respawn once behind a fade, so each wave takes two strikes.
            for(const auto& k:sc.kills)if(k.tick<=tick){
                auto* e=enemy(k.uid);
                if(e->alive && e->health>0 && e->action!=3 && e->action!=5 && e->action!=6 && e->action!=7 && !(e->flags&(32|128|1024))){EnemyTarget target{player.x,player.fixedX,player.collision,player.attack,player.fixedY};e->applyDamage(1000,true,target,{},world.random);++world.kills;++strikes[k.uid];}
            }
            world.updatePlayer(0,0,tick,paused);world.finishPlayer(paused);
            for(auto& c:containers)if(world.containerQueued(c.uid))c.update(player,0,paused,false,&world.random,false);
            world.finishContainerSources(containers,paused);
            world.advance();for(auto& c:containers)c.advanceAnimation();
            if(world.playerEnableActivations && enableTick<0)enableTick=tick;
            if(world.removedEnemyActivations>=1 && waveTick<0)waveTick=tick;
            if(world.removedEnemyActivations>=2 && secondTick<0)secondTick=tick;
            if(world.containerEnables && chestTick<0)chestTick=tick;
            if(world.gateScriptTicks()>=0 && scriptStart<0)scriptStart=tick;
            if(world.gateScriptTicks()==-2 && scriptEnd<0 && scriptStart>=0)scriptEnd=tick;
        }
        const auto& gate=*std::find_if(world.gates.begin(),world.gates.end(),[](const auto& g){return g.uid==87;});
        const Container& chest=containers.front();
        std::cout<<"W\t"<<sc.name<<'\t'<<triggerCount<<'\t'<<int(deferred)<<'\t'<<initial<<'\t'<<enableTick<<'\t'<<waveTick<<'\t'<<secondTick<<'\t'<<chestTick<<'\t'<<scriptStart<<'\t'<<scriptEnd;
        for(int uid:{96,97,98,102}){auto* e=enemy(uid);std::cout<<'\t'<<int(e->alive)<<'\t'<<(e->flags&(32|128));}
        std::cout<<'\t'<<chest.flags<<'\t'<<chest.action<<'\t'<<int(chest.alive)<<'\t'<<gate.y<<'\t'<<gate.action<<'\t'<<world.removedEnemyActivations<<'\t'<<world.playerEnableActivations<<'\t'<<world.containerEnables<<'\t'<<int(world.removedScriptTriggers[0].alive)<<'\t'<<world.gateScriptTicks()<<'\t'<<strikes[96]<<'\t'<<strikes[97]<<'\t'<<strikes[98]<<'\t'<<strikes[102]<<'\t'<<world.random.nextInt()<<'\n';
        world.detach();
    }
}
