#include "energy_barrier.h"
#include "barrier_trigger.h"
#include "player_probe.h"
#include "level_data.h"
#include "combat_world.h"
#include "timing.h"
#include <cmath>
#include <iostream>
namespace {
soul::ActorRecord record(const std::string& s){soul::ActorRecord r{};r.count=7;r.parameters={80,32,180,100,static_cast<short>(s=="spawn"?15:14),static_cast<short>(s.find("respawn")!=std::string::npos || s.find("reset")!=std::string::npos || s=="cleared-release"?8224:32),60};return r;}
}
void emitEnergyBarrierManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));const auto sprite=soul::BSprite::parse(pack.resource(1).value());
    for(const std::string s:{"idle","spawn","enable","release","respawn-release","cleared-release","pause","hidden","frozen","stopped","reset","cleared-reset","persistent-reset","sword","ice"}){
        soul::EnergyBarrier b(sprite,record(s));if(s=="hidden")b.flags=1184;if(s=="frozen")b.flags=16544;if(s=="stopped")b.flags=224;
        for(int f=0;f<80;++f){const bool paused=s=="pause" && f>=15 && f<25;
            if(f==3 && s!="idle" && s!="hidden" && s!="frozen" && s!="stopped")b.enable();
            if(f==12 && (s.find("release")!=std::string::npos || s=="pause")){if(s=="cleared-release")b.clearRespawn();b.beginRelease();}
            if(f==18 && s.find("reset")!=std::string::npos){if(s=="cleared-reset")b.clearRespawn();if(s=="persistent-reset")b.flags|=512;b.resetForRegionExit();}
            b.update(paused);
            for(int phase=0;phase<2;++phase){if(phase==1 && !paused)b.advanceAnimation();std::cout<<"E\t"<<s<<'\t'<<f<<'\t'<<phase;b.write(std::cout);std::cout<<'\n';}
        }
    }
    for(int level=0;level<9;++level){const auto levelPack=soul::ResourcePack::numeric(soul::readAssetFile(root/std::to_string(level+3)));for(const auto& r:soul::actorRecords(levelPack.resource(0).value()))if(r.parameters[0]==80){soul::EnergyBarrier b(sprite,r);std::cout<<"E\tplaced-"<<level<<'\t'<<b.uid<<"\t0";b.write(std::cout);std::cout<<'\n';}}
    const auto placements=soul::ResourcePack::numeric(soul::readAssetFile(root/"4"));const auto rows=soul::actorRecords(placements.resource(0).value());
    const auto find=[&](int uid)->const soul::ActorRecord&{for(const auto& r:rows)if(r.parameters[1]==uid)return r;throw soul::FormatError("Missing barrier chain record");};
    soul::EnergyBarrier barrier(sprite,find(32));std::vector<soul::BarrierTrigger> triggers;for(int uid:{354,124,356})triggers.emplace_back(find(uid));
    int sourceFlags=0,sourceX=1800;std::array<int,4> sourceBounds{};
    for(int f=0;f<80;++f){if(f==5)sourceX=1520;if(f==25)sourceFlags=1024;
        for(auto& t:triggers)t.update([&](int uid)->soul::BarrierSignal{if(uid!=352)return {};return {&sourceFlags,&sourceBounds,[&]{sourceBounds={sourceX-9,1181,sourceX+9,1240};}};},[&](int uid)->soul::BarrierTarget{if(uid==32)return {&barrier.flags,&barrier.action,[&]{barrier.beginRelease();}};for(auto& t:triggers)if(t.uid==uid)return {&t.flags,nullptr,{}};return {};});
        barrier.update();for(int phase=0;phase<2;++phase){if(phase==1)barrier.advanceAnimation();for(auto& t:triggers){std::cout<<"C\t"<<f<<'\t'<<phase;t.write(std::cout);std::cout<<'\n';}std::cout<<"E\tchain\t"<<f<<'\t'<<phase;barrier.write(std::cout);std::cout<<'\n';}
    }
    for(int seed:{0,1,12345})for(const std::array<int,3> p:std::initializer_list<std::array<int,3>>{{0,60,100},{-20,80,100},{290,80,100},{0,0,100},{321,60,100},{-100,60,100},{0,60,0},{0,60,240},{0,60,241}}){soul::JavaRandom random(seed);const auto lines=soul::lightningLines(random,p[0],p[1],p[2]);std::size_t index=0;for(const auto& l:lines)std::cout<<"L\t"<<seed<<'\t'<<p[0]<<'\t'<<p[1]<<'\t'<<p[2]<<'\t'<<index++<<'\t'<<static_cast<std::int32_t>(l.color)<<'\t'<<l.x0<<'\t'<<l.y0<<'\t'<<l.x1<<'\t'<<l.y1<<'\n';std::cout<<"R\t"<<seed<<'\t'<<p[0]<<'\t'<<p[1]<<'\t'<<p[2]<<'\t'<<random.nextInt()<<'\n';}

    for(int width:{240,569})for(int fps:{30,60,144})for(bool extraRenders:{false,true}){
        auto data=soul::LevelData::load(root,1);soul::Player player(data.sprite(0),data.scene.maps(),soul::playerRecord(data.records),1);player.x=1450;player.y=1250;player.fixedX=player.x*256;player.fixedY=player.y*256;player.flags=16384;player.refreshBounds();soul::PlayerMeters meters;
        const std::vector<soul::ActorRecord> selected{find(32)};soul::CombatWorld world(data.sprite(2),data.sprite(1),data.scene.maps(),player,meters,selected,data.stats(0));world.loadEnergyBarriers(data.sprite(1),selected);world.energyBarriers[0].enable();
        soul::Clock clock;std::uint32_t middle=0;
        const auto drawCanvas=[&]{soul::Canvas c(width,320,0xff101010);world.drawPlatforms(c,1447,1151);return c;};
        const auto draw=[&]{return drawCanvas().checksum();};
        for(int frame=0;frame<static_cast<int>(std::ceil(8.3*fps));++frame){clock.advance(1.0/fps,[&]{const int tick=static_cast<int>(clock.ticks);if(tick==10)world.energyBarriers[0].clearRespawn();if(tick==20)world.energyBarriers[0].beginRelease();world.updatePlayer(0,0,tick);world.finishPlayer();world.advance();if(tick==10)middle=draw();});if(extraRenders){const auto rng=world.random;const auto image=drawCanvas();if(image.pixels()!=drawCanvas().pixels())throw soul::FormatError("Presentation changed cached barrier geometry");auto copy=rng,after=world.random;if(copy.nextInt()!=after.nextInt())throw soul::FormatError("Barrier draw consumed gameplay RNG");}}
        auto rng=world.random;std::cout<<"H\t"<<width<<'\t'<<fps<<'\t'<<int(extraRenders)<<'\t'<<clock.ticks<<'\t'<<middle<<'\t'<<draw()<<'\t'<<rng.nextInt();world.energyBarriers[0].write(std::cout);std::cout<<'\n';world.detach();
    }

}
