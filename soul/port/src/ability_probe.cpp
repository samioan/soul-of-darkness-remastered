#include "player_probe.h"
#include "ability_pickup.h"
#include "container.h"
#include "combat.h"
#include <iostream>
namespace {
soul::LevelMaps arena(){soul::LevelMaps m;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int i=400;i<800;++i)g.cells[i]=24;m.collision=g;m.layers[1].width=40;m.layers[1].height=20;return m;}
}
void emitAbilityManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto sprite=soul::BSprite::parse(pack.resource(27).value()),playerSprite=soul::BSprite::parse(pack.resource(0).value());
    const auto chestSprite=soul::BSprite::parse(pack.resource(34).value()),promptSprite=soul::BSprite::parse(pack.resource(18).value());
    for(const std::string name:{"ability-chest","repeat-chest"}){
        auto maps=arena();soul::ActorRecord p{},r{};p.count=6;p.parameters={0,186,100,199,0,0};r.count=7;r.parameters={52,57,180,199,0,1,-2};
        soul::Player player(playerSprite,maps,p);soul::Container chest(chestSprite,promptSprite,maps,r);soul::JavaRandom random;
        for(int f=0;f<100;++f){
            if(f==3 || f==6){player.x=f==3?180:100;player.refreshBounds();}
            chest.update(player,f==5?65536:0,false,name=="repeat-chest",&random);chest.advanceAnimation();
            std::cout<<"C\t"<<name<<'\t'<<f;chest.write(std::cout);std::cout<<'\n';
            for(const auto& d:chest.drops)std::cout<<"D\t"<<name<<'\t'<<f<<'\t'<<d.type<<'\t'<<d.sprite<<'\t'<<d.animation<<'\t'<<d.x<<'\t'<<d.y<<'\t'<<d.flags<<'\n';
            if(!chest.alive)break;
        }
    }
    for(const std::string name:{"normal","left","offset","already","other-level","outside","pause","hidden","frozen","chase-range"}){
        auto maps=arena();soul::ActorRecord pr{};pr.count=6;pr.parameters={0,186,static_cast<std::int16_t>(name=="outside"?500:180),199,0,0};
        soul::Player p(playerSprite,maps,pr);if(name=="left"){p.facingLeft=true;p.flags=1;p.action=2;}
        soul::AbilityPickup item(sprite,name=="offset"?190:name=="chase-range"?0:180,name=="offset"?180:199);
        if(name=="chase-range"){item.action=102;item.vx=256;}
        if(name=="hidden" || name=="frozen")item.flags=name=="hidden"?1024:16384;
        soul::PlayerMeters m;m.magic=30;m.magicUnlocked=name=="already";soul::AbilityInteraction ui;
        for(int f=0;f<72;++f){
            const bool paused=name=="pause" && f>=4 && f<11;
            if(f==60){ui.active=false;ui.mode=-1;ui.tutorial=-1;}
            item.update(p,m,ui,name=="other-level"?1:0,paused);
            for(int phase=0;phase<2;++phase){
                if(phase==1 && item.alive){item.refreshBounds();if(!paused && !(item.flags&1024))item.advanceAnimation();}
                std::cout<<"A\t"<<name<<'\t'<<f<<'\t'<<phase;item.write(std::cout);
                for(int n:{int(m.magicUnlocked),m.magic,int(ui.active),ui.mode,ui.tint,int(ui.tutorial>=0),ui.beamX,ui.beamY,ui.weaponHidden?1152:0,p.animation,p.pending,p.vx,p.vy,p.ax,p.ay,p.flags})std::cout<<'\t'<<n;
                std::cout<<'\n';
            }
        }
    }
    for(const std::string name:{"normal","early","held","no-text","pause","down","cancel"}){
        soul::AbilityInteraction ui;ui.active=true;ui.mode=3;ui.tint=1;ui.tutorial=name=="no-text"?-1:61;
        for(int f=1;f<=72;++f){
            const bool paused=name=="pause" && f>=12 && f<18;
            const unsigned press=name=="held"?0:f==(name=="early"?42:51)?(name=="down"?32768:65536):0;
            if(name=="cancel" && f==45)ui.tutorial=-1;
            if(!paused)ui.advance(press);
            std::cout<<"M\t"<<name<<'\t'<<f<<'\t'<<int(ui.active)<<'\t'<<ui.mode<<'\t'<<ui.clock<<'\t'<<int(ui.tutorial>=0)<<'\n';
        }
    }

}
