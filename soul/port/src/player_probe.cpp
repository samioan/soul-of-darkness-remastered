#include "player_probe.h"
#include "player.h"
#include "pickup.h"
#include "container.h"
#include "crystal_reward.h"
#include "collection_effect.h"
#include <iostream>
#include <algorithm>
void emitPlayerManifest(const std::filesystem::path& root) {
    const auto sprites=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto sprite=soul::BSprite::parse(sprites.resource(0).value());
    for (const std::string scenario:{"walk","jump","crouch","wall","pause","ceiling"}) {
        soul::LevelMaps maps; soul::TileGrid collision; collision.width=40; collision.height=20; collision.cells.resize(800);
        for (int y=10;y<20;++y) for (int x=0;x<40;++x) collision.cells[y*40+x]=24;
        if (scenario=="wall") for (int y=0;y<10;++y) collision.cells[y*40+9]=24;
        if (scenario=="ceiling") for (int x=0;x<40;++x) collision.cells[3*40+x]=24;
        maps.layers[1].width=40; maps.layers[1].height=20; maps.collision=std::move(collision);
        soul::ActorRecord record{}; record.count=6; record.parameters[1]=186; record.parameters[2]=100; record.parameters[3]=199;
        soul::Player player(sprite,maps,record);
        for (int frame=0;frame<100;++frame) {
            unsigned held=0,pressed=0;
            if (scenario=="walk" || scenario=="wall") held=frame<60?8256:4112;
            if ((scenario=="jump" || scenario=="ceiling") && frame==4) pressed=16388;
            if (scenario=="crouch" && frame>=4 && frame<20) held=33024;
            if (scenario=="crouch" && frame==4) pressed=33024;
            const bool paused=scenario=="pause" && frame<50;
            player.update(pressed,held,frame,paused);
            auto row=[&](int phase) { std::cout << "P\t" << scenario << '\t' << frame << '\t' << phase; player.write(std::cout); std::cout << '\n'; };
            row(0); if (!paused) player.advanceAnimation(); row(1);
        }
    }
    for (int level=0;level<9;++level) {
        const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/std::to_string(level+3)));
        const auto records=soul::actorRecords(pack.resource(0).value());
        for (const auto& r:records) {
            std::cout << "R\t" << level << '\t' << r.offset << '\t' << r.count;
            for (auto n:r.parameters) std::cout << '\t' << n; std::cout << '\n';
        }
    }
}

void emitPickupManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto playerSprite=soul::BSprite::parse(pack.resource(0).value());
    const auto sprite=soul::BSprite::parse(pack.resource(33).value());
    const auto level=soul::ResourcePack::numeric(soul::readAssetFile(root/"3"));
    const auto records=soul::actorRecords(level.resource(0).value());
    auto record=*std::find_if(records.begin(),records.end(),[](const auto& r){return r.parameters[0]==35;});
    record.parameters[2]=180; record.parameters[3]=199;
    for(const std::string scenario:{"health","magic","cap","far","moving","paused","near","dropped","frozen"}) {
        soul::LevelMaps maps; soul::TileGrid grid; grid.width=40; grid.height=20; grid.cells.resize(800);
        maps.collision=grid; maps.layers[1].width=40; maps.layers[1].height=20;
        soul::ActorRecord p{}; p.count=6; p.parameters[1]=186; p.parameters[2]=100; p.parameters[3]=199;
        soul::Player player(playerSprite,maps,p);
        record.parameters[4]=scenario=="magic"?2:0;
        auto row=record;
        if(scenario=="near" || scenario=="moving") { row.parameters[2]=120; row.parameters[3]=180; }
        if(scenario=="dropped") row.parameters[1]=-1;
        soul::Pickup pickup(sprite,row); if(scenario=="frozen") pickup.flags=64;
        soul::PlayerMeters meters; meters.health=scenario=="cap"?59:30; meters.magic=30;
        for(int frame=0;frame<80;++frame) {
            if(scenario!="far" && frame==3) { player.x=180; for(int i:{0,2}) player.collision[i]+=80; }
            if(scenario=="moving") player.vx=1280;
            const bool paused=scenario=="paused" && frame<10;
            pickup.update(player,meters,paused); if(!paused) pickup.advanceAnimation();
            std::cout << "K\t" << scenario << '\t' << frame; pickup.write(std::cout);
            std::cout << '\t' << meters.health << '\t' << meters.magic << '\n'; if(!pickup.alive) break;
        }
    }
}

void emitContainerManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto playerSprite=soul::BSprite::parse(pack.resource(0).value());
    const auto sprite=soul::BSprite::parse(pack.resource(34).value());
    const auto promptSprite=soul::BSprite::parse(pack.resource(18).value());
    const auto level=soul::ResourcePack::numeric(soul::readAssetFile(root/"3"));
    const auto records=soul::actorRecords(level.resource(0).value());
    const auto record=*std::find_if(records.begin(),records.end(),[](const auto& r){return r.parameters[0]==52;});
    for(const std::string scenario:{"closed","open","leave","magic","fall","paused","early","locked"}) {
        soul::LevelMaps maps; soul::TileGrid grid; grid.width=40; grid.height=20; grid.cells.resize(800);
        for(int i=400;i<800;++i) grid.cells[i]=24;
        maps.collision=grid; maps.layers[1].width=40; maps.layers[1].height=20;
        soul::ActorRecord p{}; p.count=6; p.parameters[1]=186; p.parameters[2]=100; p.parameters[3]=199;
        soul::Player player(playerSprite,maps,p);
        auto row=record; row.parameters[2]=180; row.parameters[3]=scenario=="fall"?119:199;
        if(scenario=="magic") row.parameters[6]=1;
        if(scenario=="locked") row.parameters[5]=2216;
        soul::Container chest(sprite,promptSprite,maps,row);
        for(int frame=0;frame<100;++frame) {
            if(scenario!="closed" && scenario!="fall" && frame==3) { player.x=180; for(int i:{0,2}) player.collision[i]+=80; }
            if((scenario=="leave" && frame==4) || frame==6) { const int dx=100-player.x; player.x=100; for(int i:{0,2}) player.collision[i]+=dx; }
            const bool paused=scenario=="paused" && frame<10;
            chest.update(player,frame==(scenario=="early"?3:5)?65536:0,paused);
            if(!paused) chest.advanceAnimation();
            std::cout << "C\t" << scenario << '\t' << frame; chest.write(std::cout); std::cout << '\n';
            for(const auto& drop:chest.drops) std::cout << "D\t" << scenario << '\t' << frame << '\t' << drop.animation << '\t' << drop.x << '\t' << drop.y << '\t' << drop.flags << '\n';
            if(!chest.alive) break;
        }
    }
    const auto pickupSprite=soul::BSprite::parse(pack.resource(33).value());
    for(const std::string scenario:{"health_drop","magic_drop","health_crystal","magic_crystal","expired"}) {
        soul::LevelMaps maps; soul::TileGrid grid; grid.width=40; grid.height=20; grid.cells.resize(800);
        maps.collision=grid; maps.layers[1].width=40; maps.layers[1].height=20;
        soul::ActorRecord p{}; p.count=6; p.parameters[1]=186; p.parameters[2]=scenario=="expired"?600:100; p.parameters[3]=199;
        soul::Player player(playerSprite,maps,p); soul::PlayerMeters meters; meters.health=meters.magic=30;
        const int animation=scenario=="magic_drop"?2:scenario=="health_crystal"?1:scenario=="magic_crystal"?3:0;
        auto item=soul::Pickup::spawned(pickupSprite,animation,180,199,1);
        for(int frame=0;frame<120;++frame) {
            item.update(player,meters); item.advanceAnimation();
            std::cout << "S\t" << scenario << '\t' << frame; item.write(std::cout);
            std::cout << '\t' << item.remainingLifetime() << '\t' << meters.health << '\t' << meters.magic << '\t' << player.pending << '\t' << player.vx << '\t' << player.vy << '\n';
            if(!item.alive || item.upgradeBoundary) break;
        }
    }

}

void emitCrystalManifest(const std::filesystem::path&) {
    for(const std::string scenario:{"health","fourth","cap","magic_locked","magic_unlocked","magic_fourth","early","held"}) {
        const int kind=scenario.find("magic")==0?1:0;
        soul::PlayerMeters m; m.health=m.magic=30; m.magicUnlocked=scenario=="magic_unlocked";
        m.healthFragments=scenario=="fourth" || scenario=="cap"?3:0;
        m.magicFragments=scenario=="magic_fourth"?3:0;
        if(scenario=="cap") m.maxHealth=120;
        soul::CrystalReward reward; int state=8,beam=0; bool cinematic=true;
        for(int tick=1;tick<=56;++tick) {
            const bool press=scenario!="held" && tick==(scenario=="early"?42:51);
            if(state==8 && cinematic) { if(++beam==39) { cinematic=false; state=32; reward.begin(m,kind); } }
            else if(state==32 && reward.update(m,press)) state=8;
            std::cout << "Q\t" << scenario << '\t' << tick;
            for(int n:{state,beam,reward.frames,beam>=39?kind:0,m.healthFragments,m.magicFragments,m.health,m.maxHealth,m.magic,m.maxMagic,int(reward.upgraded),int(cinematic)}) std::cout << '\t' << n;
            std::cout << '\n';
        }
    }
}
void emitEffectManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto sprite=soul::BSprite::parse(pack.resource(1).value());
    for(int source=0;source<4;++source) for(int left=0;left<2;++left)
        for(const std::string scenario:{"normal","paused","frozen"}) {
            soul::CollectionEffect effect(sprite,{11+(source>>1),100,199,left});
            if(scenario=="frozen") effect.flags|=64;
            for(int frame=-1;frame<40;++frame) {
                const bool paused=scenario=="paused" && frame>=0 && frame<5;
                if(frame>=0) effect.update(paused);
                if(!paused) effect.advanceAnimation();
                std::cout << "E\t" << source << '\t' << left << '\t' << scenario << '\t' << frame;
                effect.write(std::cout); std::cout << '\n';
                if(!effect.alive) break;
            }
        }
}
