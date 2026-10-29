#include "player_probe.h"
#include "enemy_type1.h"
#include "pickup.h"
#include <iostream>
namespace {
soul::LevelMaps arena() {
    soul::LevelMaps m; soul::TileGrid g; g.width=40; g.height=20; g.cells.resize(800);
    for(int i=400;i<800;++i) g.cells[i]=24; m.collision=g; m.layers[1].width=40; m.layers[1].height=20; return m;
}
soul::ActorRecord record(int type,int x,int y) {
    soul::ActorRecord r{}; r.count=type?13:6;
    r.parameters={static_cast<std::int16_t>(type),19,static_cast<std::int16_t>(x),static_cast<std::int16_t>(y),0,static_cast<std::int16_t>(type?9:0),-100,-125,200,125,0,0,-1}; return r;
}
void events(const soul::EnemyType1& e,const std::string& key,int frame) {
    int i=0;
    for(const auto& l:e.loot) std::cout << "L\t" << key << '\t' << frame << '\t' << i++ << "\t35\t" << l.animation << '\t' << l.x << '\t' << l.y << '\t' << (l.flags|128) << "\t106\t1162\t6000\t308\n";
    for(const auto& f:e.effects) std::cout << "L\t" << key << '\t' << frame << '\t' << i++ << "\t39\t" << f.animation << '\t' << f.x << '\t' << f.y << '\t' << f.flags << "\t0\t0\t" << f.amount << "\t302\n";
}
void row(const soul::EnemyType1& e,const std::string& key,int frame,int phase) {
    std::cout << "E\t" << key << '\t' << frame << '\t' << phase;
    for(int n:{e.fixedX,e.fixedY,e.x,e.y,e.vx,e.vy,e.ax,e.ay,e.flags,e.animation,e.pending,e.step,e.tick,e.action}) std::cout << '\t' << n;
    for(bool b:{e.grounded,e.ceiling,e.wallLeft,e.wallRight}) std::cout << '\t' << (b?"true":"false");
    for(const auto& r:{e.collision,e.attack,e.visual}) for(int n:r) std::cout << '\t' << n;
    for(int n:{e.timer,e.paletteTimer,e.damageCooldown,e.health,e.respawnCount}) std::cout << '\t' << n;
    for(bool b:{e.enabled,e.facingLeft}) std::cout << '\t' << (b?"true":"false");
    for(const auto& r:{e.activation,e.interaction}) for(int n:r) std::cout << '\t' << n;
    std::cout << '\t' << int(e.alive) << '\n';
}
}
void emitDeathManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto sprite=soul::BSprite::parse(pack.resource(2).value());
    const auto ps=soul::BSprite::parse(pack.resource(0).value());
    const auto pickups=soul::BSprite::parse(pack.resource(33).value());
    int index=0;
    for(int health:{0,30,60}) for(int magic:{0,24,25,60}) for(bool unlocked:{false,true}) for(bool left:{false,true}) for(int seed:{0,1,12345}) {
        const auto m=arena(); soul::EnemyType1 e(sprite,m,record(1,180,199),{10,4,0}); e.facingLeft=left;
        soul::PlayerMeters meters; meters.health=health; meters.magic=magic; meters.magicUnlocked=unlocked;
        soul::JavaRandom random(seed); const std::string key="loot"+std::to_string(index++);
        e.dropLoot(random,meters); events(e,key,-1); std::cout << "N\t" << key << '\t' << random.nextInt() << '\n';
    }
    for(const std::string key:{"placed","spawned","linked","repeat","count1","count2","keepflag","flag32","sameanim","moving","paused"}) {
        const auto m=arena(); auto r=record(1,180,199);
        r.parameters[1]=key=="spawned"?-1:19; r.parameters[4]=key=="sameanim"?1:0;
        r.parameters[5]=key=="repeat"?13:key=="flag32"?45:9; r.parameters[12]=key=="linked"?7:-1;
        soul::EnemyType1 e(sprite,m,r,{10,4,key=="count1"?1:key=="count2"?2:0}); e.action=3; e.health=-5;
        if(key=="keepflag") e.flags=521;
        if(key=="sameanim") { e.step=1; e.pending=2; }
        if(key=="moving") { e.x=187; e.vx=512; e.vy=256; e.ax=12; e.ay=34; }
        e.refreshBounds(); soul::JavaRandom random; soul::PlayerMeters meters;
        soul::EnemyTarget p{600,153600,{595,100,605,101},{}};
        for(int frame=0;frame<80;++frame) {
            const bool paused=key=="paused" && frame<5; e.effects.clear(); e.loot.clear();
            if(frame==40 && (key=="repeat" || key=="count1" || key=="count2")) { e.action=3; e.health=-5; }
            e.update(p,paused,&random,&meters); events(e,key,frame); row(e,key,frame,0);
            if(!paused && e.alive && !(e.flags&1024)) { e.refreshBounds(); e.advanceAnimation(); } row(e,key,frame,1);
        }
        std::cout << "N\t" << key << '\t' << random.nextInt() << '\n';
    }
    for(const std::string key:{"health","magic","left","negative","paused","frozen","collect"}) {
        const auto m=arena(); soul::Player p(ps,m,record(0,key=="collect"?180:600,199));
        auto e=soul::Pickup::enemyDrop(pickups,key=="magic"?2:0,key=="negative"?-10:180,key=="negative"?-20:199,key=="left"?1:0);
        if(key=="frozen") e.flags=192; soul::PlayerMeters meters; meters.health=meters.magic=30;
        for(int frame=-1;frame<95;++frame) {
            const bool paused=key=="paused" && frame>=0 && frame<5;
            if(frame>=0) { e.update(p,meters,paused); if(e.alive && !paused) { e.refreshBounds(); e.advanceAnimation(); } }
            for(std::size_t i=0;i<e.effects.size();++i) {
                const auto& effect=e.effects[i]; std::cout << "L\tdrop_" << key << '\t' << frame << '\t' << i << "\t39\t" << effect.animation << '\t' << effect.x << '\t' << effect.y << '\t' << effect.flags << "\t0\t0\t0\t308\n";
            }
            std::cout << "K\t" << key << '\t' << frame; e.write(std::cout);
            std::cout << '\t' << e.spawnDelay << '\t' << e.remainingLifetime() << '\t' << meters.health << '\t' << meters.magic << '\n'; if(!e.alive) break;
        }
    }
    const auto effects=soul::BSprite::parse(pack.resource(1).value());
    for(const std::string key:{"normal","paused","frozen"}) {
        soul::CombatEffect e(effects,{28,100,179,0,0,0}); if(key=="frozen") e.flags=64;
        for(int frame=-1;frame<40;++frame) {
            const bool paused=key=="paused" && frame>=0 && frame<5;
            if(frame>=0) { e.update(paused); if(!paused) e.advanceAnimation(); }
            std::cout << "F\t" << key << '\t' << frame; e.write(std::cout); std::cout << '\n'; if(!e.alive) break;
        }
    }

}
