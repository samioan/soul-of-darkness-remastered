#include "moving_platform.h"
#include "player_probe.h"
#include "pickup.h"
#include <iostream>
#include <memory>
void emitSwingingPlatformManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto ps=soul::BSprite::parse(pack.resource(0).value()),ms=soul::BSprite::parse(pack.resource(28).value());
    const auto trig=soul::loadPlatformTrig(root);
    const auto statsPack=soul::ResourcePack::numeric(soul::readAssetFile(root/"18"));soul::BinaryReader stat(statsPack.resource(0).value());
    stat.i32be();const int statCount=stat.i32be();std::vector<int> stats;for(int i=0;i<statCount;++i)stats.push_back(stat.i32be());const int contactDamage=stats.at(1);
    const auto es=soul::BSprite::parse(pack.resource(2).value());
    for(const std::string s:{"idle","rider","always-left","always-right","slow","jump","pause","timeout","reset","cut","crush","no-cut","enemy-kill","enemy-hurt","enemy-protected"}) {
        soul::LevelMaps maps;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int i=720;i<800;++i)g.cells[i]=24;
        maps.collision=g;maps.layers[1].width=40;maps.layers[1].height=20;
        const bool enemy=s.rfind("enemy",0)==0;
        const bool far=s=="idle" || s.rfind("always",0)==0 || enemy || s=="cut" || s=="crush" || s=="no-cut";
        soul::ActorRecord pr{};pr.count=6;pr.parameters={0,186,static_cast<std::int16_t>(far?100:300),200,0,0};
        soul::ActorRecord mr{};mr.count=12;mr.parameters={50,3,300,280,5,0,-100,static_cast<std::int16_t>(s=="slow"?3:256),static_cast<std::int16_t>(s=="slow"?1:16),30,static_cast<std::int16_t>(s.rfind("always",0)==0 || s=="no-cut"?1:0),static_cast<std::int16_t>(s=="always-right"?1:-1)};
        soul::Player p(ps,maps,pr);soul::MovingPlatform m(ms,maps,mr,&trig);m.contactDamage=contactDamage;
        soul::PlayerDamage damage;soul::PlayerMeters meters;soul::JavaRandom random;std::vector<soul::CombatEffectSpawn> effects;int hits=0,enemyHits=0;
        soul::CombatExchange combat{&p,&damage,&meters,&random,&effects,&hits,&enemyHits};
        std::unique_ptr<soul::EnemyType1> e;std::vector<soul::FallingTarget> targets;
        if(enemy){soul::ActorRecord er{};er.count=13;er.parameters={1,19,300,359,0,9,-100,-125,200,125,0,0,-1};e=std::make_unique<soul::EnemyType1>(es,maps,er,stats);e->health=s=="enemy-hurt"?150:50;if(s=="enemy-protected")e->flags=16393;targets.push_back(e.get());}
        for(int f=0;f<240;++f){
            const bool paused=s=="pause" && f>=30 && f<40;
            p.update(s=="jump" && f==40?16388:0,0,f,paused);
            if((s=="cut" || s=="crush" || s=="no-cut" || enemy) && f==10){
                p.animation=13;p.step=p.animationTick=0;p.attack={297,180,303,280};
                if(s=="crush"){p.x=300;p.y=359;p.refreshBounds();p.attack={297,180,303,280};}
            }
            if(s=="reset" && f==60)m.resetSwing();
            if(s=="timeout" && f==60){m.action=101;m.angle=m.angularSpeed=0;m.swing[8]=1;m.swing[2]=0;m.swing[10]=-1;p.platform=&m.surface;}
            m.update(p,paused,&combat,targets);
            for(int phase=0;phase<2;++phase){
                if(phase==1 && !paused){p.refreshBounds();p.advanceAnimation();m.refreshBounds();m.advanceAnimation();}
                std::cout<<"P\t"<<s<<'\t'<<f<<'\t'<<phase;p.write(std::cout);std::cout<<'\t'<<(p.platform==&m.surface?1:0)<<'\t'<<meters.health<<'\n';
                std::cout<<"S\t"<<s<<'\t'<<f<<'\t'<<phase;m.writeSwing(std::cout);std::cout<<'\n';
                if(e){
                    std::cout<<"E\t"<<s<<'\t'<<f<<'\t'<<phase;
                    for(int n:{e->fixedX,e->fixedY,e->x,e->y,e->vx,e->vy,e->ax,e->ay,e->flags,e->animation,e->pending,e->step,e->tick,e->action})std::cout<<'\t'<<n;
                    for(bool b:{e->grounded,e->ceiling,e->wallLeft,e->wallRight})std::cout<<'\t'<<std::boolalpha<<b;
                    for(const auto& r:{e->collision,e->attack,e->visual})for(int n:r)std::cout<<'\t'<<n;
                    std::cout<<'\t'<<e->health<<'\t'<<e->damageCooldown<<'\t'<<e->facingLeft<<'\n';
                }
            }
        }
    }
}
