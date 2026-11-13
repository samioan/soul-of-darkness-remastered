#include "player_probe.h"
#include "linked_gate.h"
#include "timed_switch.h"
#include "fire_magic.h"
#include "tile_world.h"
#include <algorithm>
#include <iostream>
#include <memory>
namespace {
bool chainStrike(int gate,std::string mode,int uid,int f){
    if(mode=="pause")mode=gate==72?"hit":"same";
    if(mode=="hit" || mode=="miss")return uid==71 && f==2;
    if(mode=="repeat")return uid==71 && (f==2 || f==15 || f==25);
    if(mode=="one")return uid==275 && f==2;
    if(mode=="both")return (uid==275 && f==2) || (uid==277 && f==6);
    if(mode=="late")return (uid==275 && f==2) || (uid==277 && f==30);
    if(mode=="second")return uid==277 && f==2;
    if(mode=="same")return (uid==275 || uid==277) && f==2;
    return false;
}
std::uint32_t crc(const soul::Bytes& bytes){std::uint32_t n=0xffffffff;for(auto b:bytes){n^=b;for(int i=0;i<8;++i)n=(n>>1)^((n&1)?0xedb88320:0);}return n^0xffffffff;}
}
void emitGateManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));const auto sprite=soul::BSprite::parse(pack.resource(28).value());
    for(const std::string s:{"idle","up","down","left","right","mode9","clear","hold","two-links","late","missing","pause","hidden","frozen","long","repeat"}) {
        soul::LevelMaps maps;soul::TileGrid grid;grid.width=40;grid.height=20;grid.cells.resize(800);maps.collision=grid;maps.layers[1].width=40;maps.layers[1].height=20;
        const int dir=s=="left"?1:s=="right"?2:s=="down"?8:s=="mode9"?9:4;
        soul::ActorRecord r{};r.count=9;r.parameters={31,154,208,120,static_cast<std::int16_t>(s=="long"?5:4),0,static_cast<std::int16_t>(dir),25,static_cast<std::int16_t>(s=="two-links"?177:-1)};
        soul::LinkedGate g(sprite,maps,r);if(s=="hidden")g.flags=1024;if(s=="frozen")g.flags=16384;
        int x=0,y=0;
        for(int f=0;f<72;++f) {
            const bool paused=s=="pause" && f>=7 && f<14;
            x=((f>=2 && f<36) || (s=="repeat" && f>=50)) && s!="idle"?101:0;y=f>=8 && f<36?101:0;
            if(f==2 && (s=="clear" || s=="hold"))g.action=s=="clear"?103:104;
            if(s=="mode9" && (f==5 || f==20))g.y=f==5?80:120;
            g.update([&](int uid)->const int*{if(s=="missing" || (s=="late" && f<10))return nullptr;return uid==25?&x:uid==177?&y:nullptr;},paused);
            for(int phase=0;phase<2;++phase) {
                if(phase==1 && !paused)g.advanceAnimation();
                std::cout<<"G\t"<<s<<'\t'<<f<<'\t'<<phase;g.write(std::cout);std::cout<<'\t'<<crc(maps.collision->cells)<<'\n';
                if(phase==1 && (f==0 || f==10 || f==23 || f==38 || f==55))for(int width:{240,569}) {
                    soul::Canvas canvas(width,320,0xff102030);g.draw(canvas,80,0);
                    std::cout<<"R\t"<<s<<'\t'<<f<<'\t'<<width<<'\t'<<canvas.checksum()<<'\n';
                }
            }
        }
    }
    // Shipped second-level gates 72/128 with their real type-89 switches on the real pack-4 collision map.
    const auto playerSprite=soul::BSprite::parse(pack.resource(0).value()),effectSprite=soul::BSprite::parse(pack.resource(1).value());
    const auto levelPack=soul::ResourcePack::numeric(soul::readAssetFile(root/"4"));
    const auto records=soul::actorRecords(levelPack.resource(0).value());
    const auto scene=soul::TileScene::load(root,1);
    const auto find=[&](int uid){const auto it=std::find_if(records.begin(),records.end(),[&](const auto& r){return r.parameters[1]==uid;});if(it==records.end())throw soul::FormatError("Missing level-1 gate fixture record");return *it;};
    for(const std::array<int,3> chain:{std::array<int,3>{72,71,-1},{128,275,277}}) {
        const std::vector<std::string> modes=chain[0]==72?std::vector<std::string>{"idle","hit","repeat","miss","pause"}:std::vector<std::string>{"idle","one","both","late","second","same","pause"};
        const int count=chain[2]<0?2:3;
        for(const auto& mode:modes) {
            const std::string s="level1-"+std::to_string(chain[0])+"-"+mode;
            auto maps=scene.maps();
            soul::ActorRecord pr{};pr.count=6;pr.parameters={0,186,2200,200,0,0};soul::Player player(playerSprite,maps,pr,1);
            std::array<int,256> trig{};soul::FireMagic fire(effectSprite,trig);
            const auto gateRecord=find(chain[0]);soul::LinkedGate gate(sprite,maps,gateRecord);
            std::vector<std::unique_ptr<soul::TimedSwitch>> switches;
            for(int i=1;i<count;++i){switches.push_back(std::make_unique<soul::TimedSwitch>(sprite,find(chain[i])));switches.back()->refreshBounds();}
            const auto resolveSwitch=[&](int uid)->const int*{for(const auto& sw:switches)if(sw->uid==uid)return &sw->action;return nullptr;};
            const auto resolveGate=[&](int uid)->const int*{return uid==gate.uid?&gate.action:nullptr;};
            for(int f=0;f<120;++f) {
                const bool paused=mode=="pause" && f>=5 && f<15;
                gate.update(resolveSwitch,paused);
                for(auto& sw:switches) {
                    const bool hit=chainStrike(chain[0],mode,sw->uid,f);auto bounds=sw->collision;if(mode=="miss")bounds={0,0,1,1};
                    player.animation=hit?13:0;player.attack=bounds;fire.clear();
                    sw->update(sw->struckBy(player,&fire),resolveGate,paused);
                }
                for(int phase=0;phase<2;++phase) {
                    if(phase==1 && !paused){gate.advanceAnimation();for(auto& sw:switches){sw->refreshBounds();sw->advanceAnimation();}}
                    std::cout<<"G\t"<<s<<'\t'<<f<<'\t'<<phase;gate.write(std::cout);std::cout<<'\t'<<crc(maps.collision->cells)<<'\n';
                    for(const auto& sw:switches){std::cout<<"T\t"<<s<<'\t'<<f<<'\t'<<phase;sw->write(std::cout);std::cout<<'\n';}
                    if(phase==1 && (f==0 || f==10 || f==23 || f==38 || f==55))for(int width:{240,569}) {
                        soul::Canvas canvas(width,320,0xff102030);const int cameraX=gate.x-80,cameraY=gate.y-100;gate.draw(canvas,cameraX,cameraY);
                        std::cout<<"R\t"<<s<<'\t'<<f<<'\t'<<width<<'\t'<<canvas.checksum()<<'\t'<<cameraX<<'\t'<<cameraY<<'\n';
                    }
                }
            }
        }
    }
}
