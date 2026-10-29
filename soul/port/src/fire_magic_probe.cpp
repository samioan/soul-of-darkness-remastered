#include "player_probe.h"
#include "combat_world.h"
#include <iostream>
void emitFireMagicManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));const auto ps=soul::BSprite::parse(pack.resource(0).value()),es=soul::BSprite::parse(pack.resource(2).value()),fs=soul::BSprite::parse(pack.resource(3).value()),fx=soul::BSprite::parse(pack.resource(1).value());const auto trig=soul::loadPlatformTrig(root);
    for(const std::string s:{"standing","left","held","recast","low","locked","crouch","air","pause","regen","hit-ground","hit-fly","rank1","rank2"}){
        soul::LevelMaps maps;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int i=400;i<800;++i)g.cells[i]=24;maps.collision=g;maps.layers[1].width=40;maps.layers[1].height=20;
        soul::ActorRecord pr{},er{};pr.count=6;pr.parameters={0,186,100,199,0,0};er.count=13;const int type=s=="hit-fly"?2:1;er.parameters={static_cast<std::int16_t>(type),19,140,static_cast<std::int16_t>(type==2?174:199),0,8,0,0,0,0,static_cast<std::int16_t>(type==2?4:0),0,-1};const bool hasEnemy=s=="hit-fly" || s=="hit-ground";const std::vector<soul::ActorRecord> records=hasEnemy?std::vector<soul::ActorRecord>{er}:std::vector<soul::ActorRecord>{};
        soul::Player p(ps,maps,pr);if(s=="left"){p.facingLeft=true;p.flags=1;}soul::PlayerMeters m;m.magicUnlocked=s!="locked";m.magic=s=="low"?14:s=="regen"?30:60;soul::CombatWorld w(es,fx,maps,p,m,records,{80,10,0});w.loadFlyingEnemies(fs,records,{80,10,0});w.loadFireMagic(trig);w.fireMagic->rank=s=="rank1"?1:s=="rank2"?2:0;w.damage.invulnerability=60000;
        for(int f=0;f<120;++f){
            const bool paused=s=="pause" && f>=8 && f<15;unsigned pressed=s!="regen" && (f==4 || (s=="recast" && (f==10 || f==40)) || (s=="low" && f==74))?1:0,held=s=="held" && f>=4 && f<70?1:s=="crouch" && f>=2 && f<30?32768:0;if(s=="air" && f==2)pressed=16384;
            w.updatePlayer(pressed,held,f,paused);w.finishPlayer(paused);
            for(int phase=0;phase<2;++phase){
                if(phase==1 && !paused){w.chargeFireMagic();p.refreshBounds();p.advanceAnimation();w.advance();}
                std::cout<<"P\t"<<s<<'\t'<<f<<'\t'<<phase;p.write(std::cout);std::cout<<'\t'<<m.magic<<'\t'<<p.magicCharge<<'\t'<<w.fireMagic->regenerationMs<<'\n';
                for(const auto& e:w.fireMagic->pulses)if(e.alive){std::cout<<"B\t"<<s<<'\t'<<f<<'\t'<<phase<<'\t'<<e.index;e.write(std::cout);std::cout<<'\n';}
                if(hasEnemy){const soul::EnemyType1& e=type==2?static_cast<const soul::EnemyType1&>(w.flyingEnemies[0]):w.enemies[0];std::cout<<"E\t"<<s<<'\t'<<f<<'\t'<<phase;for(int n:{e.fixedX,e.fixedY,e.x,e.y,e.vx,e.vy,e.ax,e.ay,e.flags,e.animation,e.pending,e.step,e.tick,e.action})std::cout<<'\t'<<n;for(bool b:{e.grounded,e.ceiling,e.wallLeft,e.wallRight})std::cout<<'\t'<<(b?"true":"false");for(const auto& r:{e.collision,e.attack,e.visual})for(int n:r)std::cout<<'\t'<<n;std::cout<<'\t'<<e.health<<'\t'<<e.damageCooldown<<'\n';}
            }
        }
        std::cout<<"N\t"<<s<<'\t'<<w.random.nextInt()<<'\n';
    }
}
