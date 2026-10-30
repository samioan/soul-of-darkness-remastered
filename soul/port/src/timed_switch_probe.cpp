#include "timed_switch.h"
#include "fire_magic.h"
#include "player_probe.h"
#include <algorithm>
#include <iostream>
namespace {
bool hit(const std::string& s,int f){return s!="idle" && (s=="latehit"?f==25:s=="held"?f>=2&&f<10:s=="repeat"?f==2||f==15||f==25:f==2);}
}
void emitTimedSwitchManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto sprite=soul::BSprite::parse(pack.resource(28).value()),playerSprite=soul::BSprite::parse(pack.resource(0).value()),effectSprite=soul::BSprite::parse(pack.resource(1).value());
    soul::LevelMaps maps;soul::TileGrid grid;grid.width=40;grid.height=20;grid.cells.resize(800);maps.collision=grid;maps.layers[1].width=40;maps.layers[1].height=20;
    soul::ActorRecord playerRecord{};playerRecord.count=6;playerRecord.parameters={0,186,100,199,0,0};
    for(const std::string s:{"idle","hit","repeat","held","fire","fire-hidden","ice","edge","point","miss","pause","hidden","frozen","mode1","mode1-closed","mode1-replaced","sticky","short","variant","latehit","initial-warning","stopped","mode1-infinite"}){
        soul::ActorRecord r{};r.count=9;r.parameters={89,71,180,199,static_cast<short>(s=="variant"?24:25),8,static_cast<short>(s.rfind("mode1",0)==0?1:0),static_cast<short>(s=="sticky" || s=="mode1-infinite"?-1:s=="short"?83:3000),72};
        soul::TimedSwitch sw(sprite,r);soul::Player player(playerSprite,maps,playerRecord);std::array<int,256> trig{};soul::FireMagic fire(effectSprite,trig);
        if(s=="hidden")sw.flags=1032;if(s=="frozen")sw.flags=16392;if(s=="stopped")sw.flags=72;if(s=="initial-warning"){sw.animation=27;sw.step=sw.tick=0;sw.refreshBounds();}
        int target=102;
        for(int f=0;f<90;++f){const bool paused=s=="pause" && f>=5 && f<15;target=s=="mode1-closed" || (s=="mode1-replaced" && f>=10)?0:f>=20?105:102;
            player.activeWeapon=s=="ice"?1:0;player.animation=hit(s,f)?s=="ice"?19:13:0;auto bounds=sw.collision;
            if(s=="edge")bounds={bounds[0]-10,bounds[1],bounds[0],bounds[3]};if(s=="point")bounds={bounds[0],bounds[1],bounds[0],bounds[1]};if(s=="miss")bounds={0,0,1,1};
            player.attack=bounds;fire.clear();if(s.rfind("fire",0)==0){player.animation=0;if(hit(s,f)){fire.pulses.emplace_back(effectSprite,player,0,6);fire.pulses.back().collision=bounds;if(s=="fire-hidden")fire.pulses.back().flags=128;}}
            sw.update(sw.struckBy(player,&fire),[&](int uid)->const int*{return uid==72?&target:nullptr;},paused);
            for(int phase=0;phase<2;++phase){if(phase==1 && !paused)sw.advanceAnimation();std::cout<<"T\t"<<s<<'\t'<<f<<'\t'<<phase;sw.write(std::cout);std::cout<<'\n';}
        }
    }
    for(int level:{1,4}){const auto placements=soul::ResourcePack::numeric(soul::readAssetFile(root/std::to_string(level+3)));for(const auto& r:soul::actorRecords(placements.resource(0).value()))if(r.parameters[0]==89){soul::TimedSwitch sw(sprite,r);std::cout<<"T\tplaced-"<<level<<'\t'<<sw.uid<<"\t0";sw.write(std::cout);std::cout<<'\n';}}

}
