#include "player_probe.h"
#include "combat_world.h"
#include <iostream>
void emitIceMagicManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));const auto ps=soul::BSprite::parse(pack.resource(0).value()),es=soul::BSprite::parse(pack.resource(2).value()),fs=soul::BSprite::parse(pack.resource(3).value()),fx=soul::BSprite::parse(pack.resource(1).value());const auto trig=soul::loadPlatformTrig(root);
    for(const std::string s:{"standing","left","held","recast","low","locked","crouch","air","pause","regen","hit-ground","hit-fly","rank1","rank2","wall","left-wall","replaced","ride","jump","remove-wall","ride-replaced"}){
        soul::LevelMaps maps;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int i=400;i<800;++i)g.cells[i]=24;maps.collision=g;maps.layers[1].width=40;maps.layers[1].height=20;
        soul::ActorRecord pr{},er{};pr.count=6;pr.parameters={0,186,100,199,0,0};er.count=13;const int type=s=="hit-fly"?2:1;er.parameters={static_cast<std::int16_t>(type),19,140,static_cast<std::int16_t>(type==2?174:199),0,8,0,0,0,0,static_cast<std::int16_t>(type==2?4:0),0,-1};const bool hasEnemy=s=="hit-fly" || s=="hit-ground";const std::vector<soul::ActorRecord> records=hasEnemy?std::vector<soul::ActorRecord>{er}:std::vector<soul::ActorRecord>{};
        if(s=="left-wall")pr.parameters[2]=180;
        if(s=="wall" || s=="left-wall" || s=="replaced" || s=="ride" || s=="ride-replaced" || s=="jump" || s=="remove-wall"){int col=s=="left-wall"?3:10;for(int row=0;row<20;++row)maps.collision->cells[row*40+col]=24;}
        soul::Player p(ps,maps,pr);p.activeWeapon=1;if(s=="left" || s=="left-wall"){p.facingLeft=true;p.flags=1;}soul::PlayerMeters m;m.iceUnlocked=true;m.magicUnlocked=s!="locked";m.magic=s=="low"?14:s=="regen"?30:60;soul::CombatWorld w(es,fx,maps,p,m,records,{80,10,0});w.loadFlyingEnemies(fs,records,{80,10,0});w.loadFireMagic(trig);w.iceMagic->rank=s=="rank1"?1:s=="rank2"?2:0;w.damage.invulnerability=60000;
        for(int f=0;f<180;++f){
            const bool paused=s=="pause" && f>=8 && f<15;unsigned pressed=s!="regen" && (f==4 || (s=="recast" && (f==10 || f==40)) || (s=="low" && f==74) || (s=="replaced" && (f==35 || f==65)) || (s=="ride-replaced" && f==55))?1:0,held=s=="held" && f>=4 && f<70?1:s=="crouch" && f>=2 && f<30?32768:0;if(s=="air" && f==2)pressed=16384;
            if((s=="ride" || s=="ride-replaced" || s=="jump") && f==40){auto* shot=w.iceMagic->slots[0];if(!shot || shot->animation!=9)throw soul::FormatError("Missing native ride platform");p.x=shot->x;p.y=shot->collision[1]-8;p.fixedX=p.x*256;p.fixedY=p.y*256;p.vy=512;p.animation=4;p.pending=-1;p.step=p.animationTick=0;p.flags&=~64;p.refreshBounds();}
            if(s=="remove-wall" && f==60)for(int row=0;row<20;++row)maps.collision->cells[row*40+10]=0;
            if(s=="jump" && f==60)pressed=16384;
            w.updatePlayer(pressed,held,f,paused);w.finishPlayer(paused);
            for(int phase=0;phase<2;++phase){
                if(phase==1 && !paused){w.chargeFireMagic();p.refreshBounds();p.advanceAnimation();w.advance();}
                std::cout<<"P\t"<<s<<'\t'<<f<<'\t'<<phase;p.write(std::cout);std::cout<<'\t'<<m.magic<<'\t'<<p.magicCharge<<'\t'<<w.fireMagic->regenerationMs<<'\n';
                for(std::size_t i=0;i<2;++i)if(const auto* e=w.iceMagic->slots[i]){std::cout<<"J\t"<<s<<'\t'<<f<<'\t'<<phase<<'\t'<<i;e->write(std::cout);for(int n:{p.x,p.y,p.vx,p.vy,p.pending,int(p.platform==&e->surface)})std::cout<<'\t'<<n;std::cout<<'\n';}
                if(hasEnemy){const soul::EnemyType1& e=type==2?static_cast<const soul::EnemyType1&>(w.flyingEnemies[0]):w.enemies[0];std::cout<<"E\t"<<s<<'\t'<<f<<'\t'<<phase;for(int n:{e.fixedX,e.fixedY,e.x,e.y,e.vx,e.vy,e.ax,e.ay,e.flags,e.animation,e.pending,e.step,e.tick,e.action})std::cout<<'\t'<<n;for(bool b:{e.grounded,e.ceiling,e.wallLeft,e.wallRight})std::cout<<'\t'<<(b?"true":"false");for(const auto& r:{e.collision,e.attack,e.visual})for(int n:r)std::cout<<'\t'<<n;std::cout<<'\t'<<e.health<<'\t'<<e.damageCooldown<<'\n';}
            }
        }
        std::cout<<"N\t"<<s<<'\t'<<w.random.nextInt()<<'\n';
    }
}
