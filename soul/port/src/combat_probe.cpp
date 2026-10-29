#include "player_probe.h"
#include "combat.h"
#include "enemy_type1.h"
#include "player.h"
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
void events(const std::vector<soul::CombatEffectSpawn>& list,const std::string& s,int frame) {
    for(std::size_t i=0;i<list.size();++i) { const auto& e=list[i];
        std::cout << "S\t" << s << '\t' << frame << '\t' << i;
        for(int n:{e.animation,e.x,e.y,e.flags,e.timer,e.amount}) std::cout << '\t' << n; std::cout << '\n';
    }
}
void enemyRow(const soul::EnemyType1& e,const std::string& s,int frame,int phase) {
    std::cout << "E\t" << s << '\t' << frame << '\t' << phase; e.write(std::cout); std::cout << '\n';
}
}
void emitCombatManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto playerSprite=soul::BSprite::parse(pack.resource(0).value());
    const auto enemySprite=soul::BSprite::parse(pack.resource(2).value());
    const auto effectSprite=soul::BSprite::parse(pack.resource(1).value());
    for(int rank=0;rank<4;++rank) for(int seed:{0,12345}) {
        soul::JavaRandom random(seed); soul::Weapon0 weapon{rank,1};
        for(int i=0;i<32;++i) std::cout << "W\t" << rank << '\t' << seed << '\t' << i << '\t' << weapon.roll(random) << '\n';
    }
    for(std::uint64_t seed:{0ULL,12345ULL,247775869193412ULL}) {
        soul::JavaRandom random(seed);
        constexpr int ranges[][2]={{9,12},{10,50},{-40,40},{-20,-100},{7,7},{(-2147483647-1),2147483647}};
        for(int i=0;i<60;++i) std::cout << "R\t" << seed << '\t' << i << '\t' << random.range(ranges[i%6][0],ranges[i%6][1]) << '\n';
    }
    int index=0;
    for(int maximum:{60,90,120}) for(int initial:{0,1,60}) for(int inv:{0,1,600}) for(int amount:{-1,0,1,499,500,1000}) for(bool shield:{false,true}) {
        int health=initial; soul::PlayerDamage damage; damage.invulnerability=inv;
        const bool accepted=damage.apply(health,maximum,amount,shield);
        std::cout << "D\t" << index++ << '\t' << (accepted?"true":"false") << '\t' << health << '\t' << damage.invulnerability << '\n';
    }
    index=0;
    const std::vector<int> stats{10,4,0};
    for(int amount:{-1,0,1,9,10,100}) for(int rank:{0,3}) for(int secondary:{1,4}) for(bool reaction:{false,true}) for(bool left:{false,true}) {
        const auto m=arena(); soul::EnemyType1 e(enemySprite,m,record(1,180,199),stats); e.health=50; e.facingLeft=left;
        soul::JavaRandom random; soul::EnemyTarget target{100,25600,{},{}}; const std::string s="direct"+std::to_string(index++);
        e.applyDamage(amount,reaction,target,{rank,secondary},random); enemyRow(e,s,-1,0); events(e.effects,s,-1);
        std::cout << "N\t" << s << '\t' << random.nextInt() << '\n';
    }
    index=0;
    for(int flags:{0,1024,16384}) for(int cooldown:{-1,0,1,200}) for(int action:{0,3}) for(bool overlap:{false,true}) {
        const auto m=arena(); soul::EnemyType1 e(enemySprite,m,record(1,180,199),stats); e.health=50; e.flags=flags|8; e.damageCooldown=cooldown; e.action=action;
        soul::EnemyTarget p{100,25600,{},{}}; if(overlap) p.attack={170,150,190,199}; soul::JavaRandom random;
        const bool accepted=e.receiveWeapon(p,{},random); const std::string s="guard"+std::to_string(index++);
        std::cout << "G\t" << s << '\t' << (accepted?"true":"false") << '\n'; enemyRow(e,s,-1,0); events(e.effects,s,-1);
        std::cout << "N\t" << s << '\t' << random.nextInt() << '\n';
    }
    for(const std::string s:{"hurt","repeat","air_hurt"}) {
        const auto m=arena(); soul::EnemyType1 e(enemySprite,m,record(1,180,s=="air_hurt"?119:199),stats); e.health=50;
        soul::JavaRandom random; soul::EnemyTarget target{100,25600,{95,100,105,101},{}};
        for(int frame=0;frame<60;++frame) {
            e.effects.clear(); if(frame==4 || s=="repeat" && frame==5) e.applyDamage(1,true,target,{},random);
            e.update(target); events(e.effects,s,frame); enemyRow(e,s,frame,0);
            e.refreshBounds(); e.advanceAnimation(); enemyRow(e,s,frame,1);
        }
    }
    for(const std::string s:{"player_hit","player_left","player_air","player_wall","player_death","player_repeat"}) {
        const auto m=arena(); soul::Player p(playerSprite,m,record(0,100,s=="player_air"?119:199));
        if(s=="player_wall") p.wallLeft=true;
        soul::JavaRandom random; soul::PlayerDamage damage; int health=60;
        for(int frame=0;frame<60;++frame) {
            std::vector<soul::CombatEffectSpawn> spawns;
            if(frame==4 || s=="player_repeat" && (frame==5 || frame==12))
                p.receiveHit(s=="player_death"?100:4,s=="player_left"?60:180,damage,health,60,random,spawns);
            p.update(0,0,frame); events(spawns,s,frame);
            auto row=[&](int phase) { std::cout << "P\t" << s << '\t' << frame << '\t' << phase; p.write(std::cout);
                std::cout << '\t' << health << '\t' << damage.invulnerability << '\t' << (p.deathComplete?12:8) << '\n'; };
            row(0); damage.tick();
            // Original paint refreshes bounds before advancing animation.
            p.refreshBounds(); p.advanceAnimation(); row(1); if(p.deathComplete) break;
        }
    }
    for(int animation:{2,3,29}) for(int left=0;left<2;++left) for(const std::string s:{"normal","paused","frozen"}) {
        soul::CombatEffect effect(effectSprite,{animation,100,179,left,animation==29?1000:0,12});
        if(s=="frozen") effect.flags|=64;
        const std::string key=std::to_string(animation)+"_"+std::to_string(left)+"_"+s;
        for(int frame=-1;frame<40;++frame) {
            const bool paused=s=="paused" && frame>=0 && frame<5;
            if(frame>=0) { effect.update(paused); if(!paused) effect.advanceAnimation(); }
            std::cout << "F\t" << key << '\t' << frame; effect.write(std::cout); std::cout << '\n'; if(!effect.alive) break;
        }
    }
}
