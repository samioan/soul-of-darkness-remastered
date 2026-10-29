#include "player_probe.h"
#include "combat_world.h"
#include <iostream>
namespace {
void row(const soul::CombatWorld& w,const std::string& key,int frame,int phase) {
    const auto& e=w.enemies[0];
    std::cout << "R\t" << key << '\t' << frame << '\t' << phase << '\t' << w.currentRegion() << '\t' << w.previousRegion() << '\t' << std::boolalpha << w.regionTransitionPending();
    for(int n:{e.fixedX,e.fixedY,e.x,e.y,e.vx,e.vy,e.ax,e.ay,e.flags,e.animation,e.pending,e.step,e.tick,e.action})std::cout << '\t' << n;
    for(bool b:{e.grounded,e.ceiling,e.wallLeft,e.wallRight})std::cout << '\t' << b;
    for(const auto& r:{e.collision,e.attack,e.visual})for(int n:r)std::cout << '\t' << n;
    std::cout << '\t' << e.health << '\t' << e.respawnCount << '\t' << e.enabled << '\t' << e.damageCooldown << '\t' << e.timer << '\t' << e.facingLeft;
    for(const auto& r:{e.activation,e.interaction})for(int n:r)std::cout << '\t' << n;
    std::cout << '\n';
}
}
void emitRegionManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto ps=soul::BSprite::parse(pack.resource(0).value()),es=soul::BSprite::parse(pack.resource(2).value()),fx=soul::BSprite::parse(pack.resource(1).value());
    for(const std::string key:{"outgoing","incoming","straddle","unrelated","hidden","keepflag","sameanim","count"}) {
        soul::LevelMaps maps;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int i=400;i<800;++i)g.cells[i]=24;maps.collision=g;maps.layers[1].width=40;maps.layers[1].height=20;
        soul::ActorRecord pr{};pr.count=6;pr.parameters={0,186,0,199,0,0};
        soul::ActorRecord ra{};ra.count=12;ra.parameters={22,210,0,0,0,8,0,0,200,400,0,-1};auto rb=ra;rb.parameters[1]=211;rb.parameters[2]=200;
        const int x=key=="incoming"?300:key=="straddle"?200:key=="unrelated"?600:100;
        soul::ActorRecord er{};er.count=13;er.parameters={1,19,static_cast<std::int16_t>(x),199,0,9,-100,-125,200,125,0,0,-1};
        soul::Player p(ps,maps,pr);soul::PlayerMeters m;soul::CombatWorld w(es,fx,maps,p,m,{ra,rb,er},{10,10,key=="count"?2:0});
        auto& e=w.enemies[0];e.health=-5;e.action=101;e.x=x+5;e.vx=512;e.vy=256;e.ax=12;e.ay=34;e.damageCooldown=117;e.timer=913;
        if(key!="sameanim"){e.animation=1;e.pending=-1;e.step=e.tick=0;e.flags&=~64;}
        if(key=="hidden")e.flags=1033;if(key=="keepflag")e.flags=521;e.refreshBounds();
        int frame=0;for(int px:{0,100,200,201,350,600,200,100,0,201,100}) {
            p.x=px;w.refreshRegionSelection();row(w,key,frame,0);w.applyRegionTransition();row(w,key,frame++,1);
        }
    }
}
