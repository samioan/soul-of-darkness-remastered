#include "player_probe.h"
#include "ice_magic.h"
#include "moving_platform.h"
#include <sstream>
#include <iostream>
void emitIceAttachmentManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));const auto ps=soul::BSprite::parse(pack.resource(0).value()),fx=soul::BSprite::parse(pack.resource(1).value()),ms=soul::BSprite::parse(pack.resource(28).value());
    for(const std::string s:{"natural","natural-left","natural-hidden","natural-locked","natural-pose","natural-late","linked","linked-left","linked-vertical","linked-late","linked-paused","linked-melt","linked-replace","linked-rider","linked-expiry","linked-offset","linked-hidden"}){
        const auto ends=[&](const std::string& tail){return s.size()>=tail.size() && s.compare(s.size()-tail.size(),tail.size(),tail)==0;};
        soul::LevelMaps maps;soul::TileGrid grid;grid.width=40;grid.height=20;grid.cells.resize(800);for(int i=400;i<800;++i)grid.cells[i]=24;maps.collision=grid;maps.layers[1].width=40;maps.layers[1].height=20;
        soul::ActorRecord pr{},mr{};pr.count=6;pr.parameters={0,186,static_cast<std::int16_t>(ends("left")?220:100),199,0,0};mr.count=13;mr.parameters={33,64,160,static_cast<std::int16_t>(ends("offset")?159:149),static_cast<std::int16_t>(ends("pose")?9:0),static_cast<std::int16_t>(8|(s=="natural-hidden"?1024:s=="natural-locked"?32:0)),-60,-60,120,120,static_cast<std::int16_t>(ends("vertical")?3:0),1,20};
        soul::Player p(ps,maps,pr);if(ends("left")){p.flags=1;p.facingLeft=true;}soul::MovingPlatform m(ms,maps,mr);soul::IceProjectile e(fx,maps,p);
        soul::PlayerDamage damage;damage.invulnerability=60000;soul::PlayerMeters meters;soul::JavaRandom random;std::vector<soul::CombatEffectSpawn> effects;int hits=0,enemyHits=0;soul::CombatExchange combat{&p,&damage,&meters,&random,&effects,&hits,&enemyHits};
        if(s.rfind("linked",0)==0){e.collision=e.attack=m.collision;if(!e.tryAttach(m))throw soul::FormatError("Missing controlled ice link");}
        auto row=[&](int f,int phase){
            const auto links=[&](bool support,bool alive){std::cout<<'\t'<<int(m.attachedIce==&e)<<'\t'<<int(e.attachedTo==&m)<<'\t'<<int(support)<<'\t'<<int(alive)<<'\n';};
            std::cout<<"P\t"<<s<<'\t'<<f<<'\t'<<phase;p.write(std::cout);std::cout<<"\t0\t0";links(false,false);
            std::ostringstream state;m.write(state);const auto text=state.str();const auto end=text.rfind('\t'),start=text.rfind('\t',end-1);
            std::cout<<"M\t"<<s<<'\t'<<f<<'\t'<<phase<<text.substr(0,start)<<'\t'<<m.timer<<'\t'<<m.attachmentOffset;links(p.platform==&m.surface,true);
            std::cout<<"I\t"<<s<<'\t'<<f<<'\t'<<phase;
            for(int n:{e.fixedX,e.fixedY,e.x,e.y,e.vx,0,0,0,e.flags,e.animation,e.pending,e.step,e.tick,0})std::cout<<'\t'<<n;
            for(bool b:{e.grounded,e.ceiling,e.wallLeft,e.wallRight})std::cout<<'\t'<<std::boolalpha<<b;
            for(const auto& r:{e.collision,e.attack,e.visual})for(int n:r)std::cout<<'\t'<<n;
            std::cout<<'\t'<<e.timer<<'\t'<<e.attachmentOffset;links(p.platform==&e.surface,e.alive);
        };
        row(-1,0);
        for(int f=0;f<180;++f){const bool paused=ends("paused") && f>=12 && f<22;
            if(s=="linked-hidden" && f==20)m.flags=1032;
            if((ends("melt") || ends("replace")) && f==4){e.request(10);e.timer=1000;}
            if(ends("rider") && f==6){p.x=e.x;p.y=e.collision[1]-2;p.fixedX=p.x*256;p.fixedY=p.y*256;p.vy=512;p.refreshBounds();}
            e.updatedThisTick=false;p.update(0,0,f,paused);
            const auto shot=[&]{e.update(p,{0,0,240,320},{0,0,800,400},!(ends("replace") && f>=4),[&](auto& ice){if(!(m.flags&32))ice.tryAttach(m);},paused);};
            const auto parent=[&]{if(!(m.flags&32)){m.update(p,paused,&combat);if(m.attachedIce && !m.attachedIce->updatedThisTick)shot();}};
            if(ends("late")){shot();parent();}else{parent();shot();}
            for(int phase=0;phase<2;++phase){if(phase==1 && !paused){damage.tick();p.refreshBounds();p.advanceAnimation();if(!(m.flags&1024)){m.refreshBounds();m.advanceAnimation();}e.advance();}row(f,phase);}
        }
    }
}
