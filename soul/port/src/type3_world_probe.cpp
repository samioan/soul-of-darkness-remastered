#include "combat_world.h"
#include "level_data.h"
#include "player_probe.h"
#include "timing.h"
#include <algorithm>
#include <iostream>
#include <memory>
// Controlled selected-level fixtures: every shipped type-3 record of levels 1 and 2 is constructed through
// CombatWorld with the player parked in its activation box. These are not route tests.
namespace {
// Other level-1/2 owners (type-1 variants, platforms, switches) are not part of this fixture.
std::vector<soul::ActorRecord> ownedRecords(const std::vector<soul::ActorRecord>& all){
    std::vector<soul::ActorRecord> subset;
    for(const auto& r:all)if(r.parameters[0]==0 || r.parameters[0]==3 || r.parameters[0]==52 || (r.parameters[0]==40 && r.parameters[1]==308))subset.push_back(r);
    return subset;
}
}
void emitType3WorldManifest(const std::filesystem::path& root){
    using namespace soul;
    for(const int level:{1,2}){
        const auto probeData=LevelData::load(root,level);
        std::vector<int> uids;for(const auto& r:probeData.records)if(r.parameters[0]==3)uids.push_back(r.parameters[1]);
        for(const int uid:uids)for(const int mode:{0,1,2}){
            // mode 0: observe; 1: kill after 20 ticks; 2: hide the player (frozen) so only idle ownership runs.
            auto data=LevelData::load(root,level);
            const auto owned=ownedRecords(data.records);
            Player player(data.sprite(data.level.playerSprite),data.scene.maps(),playerRecord(data.records),level);PlayerMeters meters;
            CombatWorld world(data.sprite(data.requiredSprites[data.level.enemySprite]?data.level.enemySprite:data.level.flyingSprite),data.sprite(data.level.effectSprite),data.scene.maps(),player,meters,owned,data.stats(0));
            world.loadThrownObjects(data.sprite(data.level.thrownSprite));
            world.loadType3Enemies(data.sprite(data.level.flyingSprite),owned,data.stats(3));
            std::vector<Container> containers;for(const auto& r:owned)if(r.parameters[0]==52)containers.emplace_back(data.sprite(data.level.containerSprite),data.sprite(data.level.promptSprite),data.scene.maps(),r);
            world.bindContainers(containers);
            auto& actor=*std::find_if(world.type3Enemies.begin(),world.type3Enemies.end(),[&](const auto& e){return e.uid==uid;});
            // Only the selected actor runs: other type-3 owners are frozen the way the first-level fixtures freeze neighbours.
            for(auto& other:world.type3Enemies)if(other.uid!=uid)other.flags|=16384;
            actor.flags&=~(32|1024);
            const auto& p=actor.activation;
            player.x=actor.behavior()==2?(p[0]+p[2])/2:actor.x-100;player.y=actor.y;player.fixedX=player.x*256;player.fixedY=player.y*256;player.flags=16384;player.refreshBounds();
            world.damage.invulnerability=60000;
            if(mode==2){player.x=10;player.y=10;player.fixedX=player.x*256;player.fixedY=player.y*256;player.refreshBounds();}
            world.advance(); // refresh cached camera distances for the final parked player position
            int actionMask=0,shotsBorn=0,released=0,exploded=0,maxShots=0,carriedTicks=0,dropTicks=0,deathTick=-1,respawnTick=-1;bool chestSeen=false;int chestUid=actor.linkUid();
            std::size_t lastShots=0;std::vector<int> seen;
            for(int tick=0;tick<200;++tick){
                world.refreshRegionSelection();
                if(mode==1 && tick==20 && actor.alive){EnemyTarget target{player.x,player.fixedX,player.collision,player.attack,player.fixedY};actor.applyDamage(1000,true,target,{},world.random);++world.kills;}
                world.updatePlayer(0,0,tick);world.finishPlayer();
                for(auto& c:containers)if(c.alive && (!(c.flags&32) || (c.flags&16)))c.update(player,0,false,false,&world.random,false);
                world.advance();
                for(auto& c:containers)c.advanceAnimation();
                const int bit=actor.action>=100?actor.action-100+8:actor.action;if(bit>=0 && bit<31)actionMask|=1<<bit;
                const std::size_t count=world.thrownObjects->objects.size();maxShots=std::max<int>(maxShots,int(count));
                for(const auto& shot:world.thrownObjects->objects){if(shot.action==102 && shot.timer==800-83)++released;if(shot.action==105 && shot.aux==0 && shot.animation==3 && shot.step==0 && shot.tick==0)++exploded;}
                if(count>lastShots)shotsBorn+=int(count-lastShots);lastShots=count;
                if(chestUid!=-1)for(const auto& c:containers)if(c.uid==chestUid){chestSeen=true;if(c.flags&32)++carriedTicks;else if(actor.chest==nullptr && actor.attachedType()==0 && tick>=20)++dropTicks;}
                if(actor.health<=0 && deathTick<0)deathTick=tick;
                if(deathTick>=0 && actor.health>0 && respawnTick<0)respawnTick=tick;
            }
            const Container* chest=nullptr;for(const auto& c:containers)if(c.uid==chestUid)chest=&c;
            std::cout<<"W\t"<<level<<'\t'<<uid<<'\t'<<mode<<'\t'<<actor.behavior()<<'\t'<<actionMask<<'\t'<<shotsBorn<<'\t'<<maxShots<<'\t'<<world.thrownObjects->objects.size()<<'\t'<<world.kills<<'\t'<<deathTick<<'\t'<<int(actor.alive)<<'\t'<<actor.health<<'\t'<<actor.respawnCount<<'\t'<<actor.attachedType()<<'\t'<<(chest?chest->flags:-1)<<'\t'<<(chest?chest->action:-1)<<'\t'<<(chest?int(chest->grounded):-1)<<'\t'<<carriedTicks<<'\t'<<actor.x<<'\t'<<actor.y<<'\t'<<world.effectsSpawned<<'\t'<<world.lootSpawned<<'\t'<<world.random.nextInt()<<'\n';
            world.detach();
        }
    }
    // Shipped level-2 trigger 308 (player overlap) enables type-3 actors 62 and 74 through the world's lazy lookup.
    {
        auto data=LevelData::load(root,2);const auto owned=ownedRecords(data.records);
        Player player(data.sprite(data.level.playerSprite),data.scene.maps(),playerRecord(data.records),2);PlayerMeters meters;
        CombatWorld world(data.sprite(data.requiredSprites[data.level.enemySprite]?data.level.enemySprite:data.level.flyingSprite),data.sprite(data.level.effectSprite),data.scene.maps(),player,meters,owned,data.stats(0));
        world.loadThrownObjects(data.sprite(data.level.thrownSprite));world.loadType3Enemies(data.sprite(data.level.flyingSprite),owned,data.stats(3));
        const auto find=[&](int uid)->EnemyType3&{return *std::find_if(world.type3Enemies.begin(),world.type3Enemies.end(),[&](const auto& e){return e.uid==uid;});};
        for(auto& other:world.type3Enemies)if(other.uid!=62 && other.uid!=74)other.flags|=16384;
        const int before=find(62).flags&find(74).flags&32;
        player.x=2250;player.y=1400;player.fixedX=player.x*256;player.fixedY=player.y*256;player.flags=16384;player.refreshBounds();world.damage.invulnerability=60000;world.advance();
        int activationTick=-1;
        for(int tick=0;tick<80;++tick){world.updatePlayer(0,0,tick);world.finishPlayer();world.advance();if(world.playerEnableActivations && activationTick<0)activationTick=tick;}
        std::cout<<"E\t"<<world.playerEnableTriggers.size()<<'\t'<<before<<'\t'<<world.playerEnableActivations<<'\t'<<activationTick<<'\t'<<find(62).flags<<'\t'<<find(74).flags<<'\t'<<find(62).action<<'\t'<<find(74).action<<'\t'<<int(world.playerEnableTriggers[0].alive)<<'\n';
        world.detach();
    }
    // Presentation fixture: width and drawing must not change simulation, RNG or ownership.
    for(const int width:{240,569})for(const bool extraRenders:{false,true}){
        auto data=LevelData::load(root,1);
        const auto owned=ownedRecords(data.records);
        Player player(data.sprite(data.level.playerSprite),data.scene.maps(),playerRecord(data.records),1);PlayerMeters meters;
        CombatWorld world(data.sprite(data.requiredSprites[data.level.enemySprite]?data.level.enemySprite:data.level.flyingSprite),data.sprite(data.level.effectSprite),data.scene.maps(),player,meters,owned,data.stats(0));
        world.loadThrownObjects(data.sprite(data.level.thrownSprite));
        world.loadType3Enemies(data.sprite(data.level.flyingSprite),owned,data.stats(3));
        for(auto& other:world.type3Enemies)if(other.uid!=7)other.flags|=16384;
        auto& actor=*std::find_if(world.type3Enemies.begin(),world.type3Enemies.end(),[](const auto& e){return e.uid==7;});actor.flags&=~(32|1024);
        player.x=actor.x-100;player.y=actor.y;player.fixedX=player.x*256;player.fixedY=player.y*256;player.flags=16384;player.refreshBounds();world.damage.invulnerability=60000;world.advance();
        std::uint32_t middle=0;
        const auto cx=std::max(0,actor.x-width/2),cy=std::max(0,actor.y-200);
        const auto draw=[&]{Canvas c(width,320,0xff101010);world.drawEnemies(c,cx,cy);if(world.thrownObjects)world.thrownObjects->draw(c,cx,cy);return c;};
        for(int tick=0;tick<120;++tick){
            world.updatePlayer(0,0,tick);world.finishPlayer();world.advance();
            if(tick==60)middle=draw().checksum();
            if(extraRenders){auto rng=world.random;const auto image=draw();if(image.pixels()!=draw().pixels())throw FormatError("Type-3 presentation changed state");auto after=world.random;if(rng.nextInt()!=after.nextInt())throw FormatError("Type-3 draw consumed RNG");}
        }
        auto rng=world.random;
        std::cout<<"H\t"<<width<<'\t'<<int(extraRenders)<<'\t'<<actor.action<<'\t'<<actor.x<<'\t'<<actor.y<<'\t'<<world.thrownObjects->objects.size()<<'\t'<<world.effectsSpawned<<'\t'<<middle<<'\t'<<draw().checksum()<<'\t'<<rng.nextInt()<<'\n';
        world.detach();
    }
}
