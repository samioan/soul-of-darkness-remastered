#include "moving_platform.h"
#include "player_probe.h"
#include "pickup.h"
#include <iostream>
void emitMovingPlatformManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto ps=soul::BSprite::parse(pack.resource(0).value()),ms=soul::BSprite::parse(pack.resource(28).value());
    for(const std::string s:{"rail0","rail1","rail2","rail3","rail4","rail5","rail6","rail7","rider","crouch","left","right","jump","high","low","pause","wall","ceiling"}) {
        soul::LevelMaps maps;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int i=720;i<800;++i)g.cells[i]=24;
        if(s=="wall")for(int r=0;r<18;++r)g.cells[r*40+16]=24;
        if(s=="ceiling")for(int i=160;i<200;++i)g.cells[i]=24;
        maps.collision=g;maps.layers[1].width=40;maps.layers[1].height=20;
        soul::ActorRecord pr{};pr.count=6;pr.parameters={0,186,static_cast<std::int16_t>(s.rfind("rail",0)==0?100:300),static_cast<std::int16_t>(s=="high"?80:s=="low"?340:200),0,0};
        soul::ActorRecord mr{};mr.count=13;mr.parameters={33,64,300,280,9,8,-120,-180,240,220,static_cast<std::int16_t>(s.rfind("rail",0)==0?s[4]-'0':8),5,20};
        soul::Player p(ps,maps,pr);soul::MovingPlatform m(ms,maps,mr);soul::PlayerDamage damage;soul::PlayerMeters meters;soul::JavaRandom random;
        std::vector<soul::CombatEffectSpawn> effects;int hits=0,enemyHits=0;soul::CombatExchange combat{&p,&damage,&meters,&random,&effects,&hits,&enemyHits};
        for(int f=0;f<160;++f) {
            const bool paused=s=="pause" && f>=30 && f<40;
            const unsigned pressed=s=="jump" && f==40?16388:0;
            const unsigned held=s=="crouch" && f>=30 && f<75?33024:s=="left" && f>=30 && f<38?4112:s=="right" && f>=30 && f<38?8256:0;
            p.update(pressed,held,f,paused);if(p.platform==&m.surface && p.ceiling)m.vy=0;m.update(p,paused,&combat);
            for(int phase=0;phase<2;++phase){
                if(phase==1 && !paused){p.refreshBounds();p.advanceAnimation();m.refreshBounds();m.advanceAnimation();}
                std::cout<<"P\t"<<s<<'\t'<<f<<'\t'<<phase;p.write(std::cout);std::cout<<'\t'<<(p.platform==&m.surface?1:0)<<'\t'<<meters.health<<'\n';
                std::cout<<"M\t"<<s<<'\t'<<f<<'\t'<<phase;m.write(std::cout);std::cout<<'\n';
            }
        }
    }
}
