#include "thrown_object.h"
#include "player_probe.h"
#include "pickup.h"
#include "breakable_bridge.h"
#include <memory>
#include "level_data.h"
#include "combat_world.h"
#include "timing.h"
#include <cmath>
#include <iostream>
namespace {
int level(const std::string& s){return s=="mode2"?5:s=="mode3" || s=="paired"?3:1;}
}
void emitThrownObjectManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));const auto sprite=soul::BSprite::parse(pack.resource(4).value()),playerSprite=soul::BSprite::parse(pack.resource(0).value()),enemySprite=soul::BSprite::parse(pack.resource(2).value()),bridgeSprite=soul::BSprite::parse(pack.resource(36).value());
    for(const std::string s:{"held","flight","left","wall","ceiling","land","reflect","early-sword","player","blast-player","held-blast","pause","hidden","frozen","slope16","slope17","slope18","slope19","slope20","slope21","mode2","mode3","paired","enemy","enemy-frozen","bridge","bridge-hidden"}){
        soul::LevelMaps maps;soul::TileGrid grid;grid.width=40;grid.height=20;grid.cells.resize(800);for(int i=400;i<800;++i)grid.cells[i]=24;
        if(s=="ceiling")for(int i=80;i<120;++i)grid.cells[i]=24;if(s=="wall")for(int y=0;y<10;++y)grid.cells[y*40+35]=24;
        if(s.rfind("slope",0)==0)for(int i=400;i<440;++i)grid.cells[i]=static_cast<std::uint8_t>(std::stoi(s.substr(5)));if(s=="mode2" || s=="mode3")std::fill(grid.cells.begin(),grid.cells.end(),std::uint8_t{0});
        maps.collision=std::move(grid);maps.layers[1].width=40;maps.layers[1].height=20;
        soul::ActorRecord pr{};pr.count=6;pr.parameters={0,186,700,199,0,0};soul::Player player(playerSprite,maps,pr,level(s));soul::PlayerMeters meters;soul::PlayerDamage damage;soul::JavaRandom random;std::vector<soul::CombatEffectSpawn> effects;
        int cooldown=0;soul::ThrowAttachment holder(cooldown);soul::ThrownObjects objects(sprite,maps,level(s));const int x=s=="wall"?650:s.rfind("slope",0)==0?115:100,y=s=="held" || s=="flight" || s=="left" || s=="mode2" || s=="mode3" || s=="paired"?100:190;
        auto& object=objects.spawnHeld(holder,x,y,s=="left");if(s=="hidden")object.flags|=1024;if(s=="frozen")object.flags|=16384;
        std::unique_ptr<soul::EnemyType1> enemy;std::unique_ptr<soul::BreakableBridge> bridge;
        if(s.rfind("enemy",0)==0){soul::ActorRecord er{};er.count=13;er.parameters={1,19,105,159,0,9,-100,-125,200,125,0,0,-1};enemy=std::make_unique<soul::EnemyType1>(enemySprite,maps,er,std::vector<int>{50,0,0});if(s=="enemy-frozen")enemy->flags|=16384;}
        if(s.rfind("bridge",0)==0){soul::ActorRecord br{};br.count=10;br.parameters={45,42,100,159,0,8,0,0,40,40};bridge=std::make_unique<soul::BreakableBridge>(bridgeSprite,maps,br);if(s=="bridge-hidden")bridge->flags=1032;}
        const auto overlap=[](const auto& a,const auto& b){return (a[0]!=a[2] || a[1]!=a[3]) && (b[0]!=b[2] || b[1]!=b[3]) && a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1];};
        for(int f=0;f<120;++f){const bool paused=s=="pause" && f>=5 && f<15;
            if(f==0 && s!="held" && s!="held-blast" && s!="hidden" && s!="frozen" && s!="paired")object.release(s=="wall"?4096:s=="flight"?2048:s=="left"?-2048:0,s=="ceiling"?-6000:s=="flight" || s=="left"?-3584:0);
            if((f==0 && s=="blast-player") || (f==3 && s=="held-blast"))object.explode();
            const bool body=(s=="player" && f>=8 && f<20) || (s=="blast-player" && f<10) || (s=="paired" && f==0);
            const auto far=std::array<int,4>{700,0,701,1};soul::EnemyTarget target{player.x,player.fixedX,body?object.collision:far,((s=="reflect" && f==15) || (s=="early-sword" && f==3))?object.collision:far};
            if(f==3 && s.rfind("enemy",0)==0)object.reflect(target);
            if(f==3 && s.rfind("bridge",0)==0)object.explode();
            object.update(target,[&](auto& shot){if(enemy && !(enemy->flags&(1024|16384)) && enemy->action!=3 && enemy->action!=5 && enemy->action!=6 && enemy->action!=7 && overlap(enemy->collision,shot.collision)){enemy->applyDamage(500,true,target,{},random);shot.explode();}if(bridge && !(bridge->flags&1024) && overlap(bridge->collision,shot.collision))bridge->activate(0);},[&](auto& shot){player.receiveBeamHit(15,shot.x,false,damage,meters.health,meters.maxHealth,random,effects);},paused,s=="paired");
            for(int phase=0;phase<2;++phase){if(phase==1 && !paused)object.advanceAnimation();std::cout<<"T\t"<<s<<'\t'<<f<<'\t'<<phase;object.write(std::cout);std::cout<<'\t'<<cooldown<<'\t'<<int(holder.object==&object)<<'\t'<<meters.health<<'\t'<<damage.invulnerability<<'\t'<<player.animation<<'\t'<<player.vx<<'\t'<<player.ax<<'\t'<<int(player.facingLeft)<<'\t'<<(enemy?enemy->health:-1)<<'\t'<<(enemy?enemy->action:-1)<<'\t'<<(bridge?bridge->action:-1)<<'\n';}
        }
        std::cout<<"R\t"<<s<<'\t'<<random.nextInt()<<'\n';
    }
    for(int width:{240,569})for(int fps:{30,60,144})for(bool extraRenders:{false,true}){
        auto data=soul::LevelData::load(root,1);soul::Player player(data.sprite(data.level.playerSprite),data.scene.maps(),soul::playerRecord(data.records),1);player.flags=16384;soul::PlayerMeters meters;
        soul::CombatWorld world(data.sprite(data.level.enemySprite),data.sprite(data.level.effectSprite),data.scene.maps(),player,meters,{},data.stats(0));world.loadThrownObjects(data.sprite(data.level.thrownSprite));int cooldown=0;soul::ThrowAttachment holder(cooldown);world.thrownObjects->spawnHeld(holder,1480,810,false);
        soul::Clock clock;std::uint32_t middle=0;const auto canvas=[&]{soul::Canvas c(width,320,0xff101010);world.thrownObjects->draw(c,1271,596);return c;};
        for(int frame=0;frame<static_cast<int>(std::ceil(8.3*fps));++frame){clock.advance(1.0/fps,[&]{const int tick=static_cast<int>(clock.ticks);if(tick==3 && holder.object)holder.object->release(1536,-3584);world.updatePlayer(0,0,tick);world.finishPlayer();world.advance();if(tick==2)middle=canvas().checksum();});if(extraRenders){auto rng=world.random;const auto image=canvas();if(image.pixels()!=canvas().pixels())throw soul::FormatError("Thrown presentation changed state");auto after=world.random;if(rng.nextInt()!=after.nextInt())throw soul::FormatError("Thrown draw consumed RNG");}}
        auto rng=world.random;std::cout<<"H\t"<<width<<'\t'<<fps<<'\t'<<int(extraRenders)<<'\t'<<clock.ticks<<'\t'<<world.thrownObjects->objects.size()<<'\t'<<int(holder.object!=nullptr)<<'\t'<<cooldown<<'\t'<<meters.health<<'\t'<<world.playerHits<<'\t'<<middle<<'\t'<<canvas().checksum()<<'\t'<<rng.nextInt()<<'\n';world.detach();
    }

}
