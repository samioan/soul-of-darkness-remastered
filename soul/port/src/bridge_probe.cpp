#include "player_probe.h"
#include "breakable_bridge.h"
#include "moving_platform.h"
#include "tile_world.h"
#include <algorithm>
#include <iostream>
#include <memory>
#include <sstream>
namespace {
std::uint32_t checksum(const soul::Bytes& bytes) {
    std::uint32_t crc=0xffffffff;for(auto b:bytes){crc^=b;for(int i=0;i<8;++i)crc=(crc>>1)^((crc&1)?0xedb88320:0);}return crc^0xffffffff;
}
}
void emitBridgeManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto ps=soul::BSprite::parse(pack.resource(0).value()),ms=soul::BSprite::parse(pack.resource(28).value()),bs=soul::BSprite::parse(pack.resource(36).value());
    const auto trig=soul::loadPlatformTrig(root);
    for(const std::string s:{"idle","activate","late","pause","long","fall","fall-before","fall-long","miss","edge","hidden"}) {
        soul::LevelMaps maps;soul::TileGrid grid;grid.width=40;grid.height=20;grid.cells.resize(800);for(int i=720;i<800;++i)grid.cells[i]=24;maps.collision=grid;maps.layers[1].width=40;maps.layers[1].height=20;
        soul::ActorRecord pr{};pr.count=6;pr.parameters={0,186,100,199,0,0};soul::Player p(ps,maps,pr);
        soul::ActorRecord r{};r.count=10;r.parameters={45,42,200,320,static_cast<std::int16_t>(s=="long" || s=="fall-long"?1:0),8,0,0,200,20};soul::BreakableBridge bridge(bs,maps,r);
        const bool falling=s.rfind("fall",0)==0 || s=="miss" || s=="edge" || s=="hidden";
        std::unique_ptr<soul::MovingPlatform> m;
        if(falling){soul::ActorRecord mr{};mr.count=12;mr.parameters={50,3,static_cast<std::int16_t>(s=="miss"?500:s=="edge"?400:300),280,5,0,-100,256,16,30,0,-1};m=std::make_unique<soul::MovingPlatform>(ms,maps,mr,&trig);}
        if(s=="hidden")bridge.flags=1032;
        const std::vector<soul::FallingTarget> targets{&bridge};
        for(int f=0;f<60;++f) {
            const bool paused=s=="pause" && f>=4 && f<10;
            if(f==(s=="late"?25:3) && s!="idle"){if(m)m->action=102;else bridge.activate(300);}
            if(m && s!="fall-before")m->update(p,paused,nullptr,targets);
            bridge.update(paused);
            if(m && s=="fall-before")m->update(p,paused,nullptr,targets);
            for(int phase=0;phase<2;++phase) {
                if(phase==1 && !paused){bridge.advanceAnimation();if(m){m->refreshBounds();m->advanceAnimation();}}
                std::cout<<"B\t"<<s<<'\t'<<f<<'\t'<<phase;bridge.write(std::cout);std::cout<<'\t'<<checksum(maps.collision->cells)<<'\n';
                if(m){std::cout<<"S\t"<<s<<'\t'<<f<<'\t'<<phase;m->writeSwing(std::cout);std::cout<<'\n';}
            }
        }
    }
    // Shipped second-level bridges on the real pack-4 collision map, with the chandeliers above them.
    const auto levelPack=soul::ResourcePack::numeric(soul::readAssetFile(root/"4"));
    const auto records=soul::actorRecords(levelPack.resource(0).value());
    const auto scene=soul::TileScene::load(root,1);
    const auto find=[&](int uid){const auto it=std::find_if(records.begin(),records.end(),[&](const auto& r){return r.parameters[1]==uid;});if(it==records.end())throw soul::FormatError("Missing level-1 bridge fixture record");return *it;};
    for(const std::array<int,2> pair:{std::array<int,2>{107,89},{108,90},{109,91},{197,190}})for(const std::string mode:{"idle","activate","fall","pause","hidden","miss"}) {
        const std::string s="level1-"+std::to_string(pair[0])+"-"+mode;
        auto maps=scene.maps();
        soul::ActorRecord pr{};pr.count=6;pr.parameters={0,186,2000,700,0,0};soul::Player p(ps,maps,pr,1);
        const auto r=find(pair[0]);soul::BreakableBridge bridge(bs,maps,r);
        const bool chandelier=mode=="fall" || mode=="hidden" || mode=="miss";
        std::unique_ptr<soul::MovingPlatform> m;
        if(chandelier){auto mr=find(pair[1]);if(mode=="miss")mr.parameters[2]=static_cast<std::int16_t>(mr.parameters[2]+300);m=std::make_unique<soul::MovingPlatform>(ms,maps,mr,&trig);}
        if(mode=="hidden")bridge.flags=1032;
        const std::vector<soul::FallingTarget> targets{&bridge};
        for(int f=0;f<60;++f) {
            const bool paused=mode=="pause" && f>=4 && f<10;
            if(f==3 && mode!="idle"){if(m)m->action=102;else bridge.activate(300);}
            if(m)m->update(p,paused,nullptr,targets);
            bridge.update(paused);
            for(int phase=0;phase<2;++phase) {
                if(phase==1 && !paused){bridge.advanceAnimation();if(m){m->refreshBounds();m->advanceAnimation();}}
                std::cout<<"B\t"<<s<<'\t'<<f<<'\t'<<phase;bridge.write(std::cout);std::cout<<'\t'<<checksum(maps.collision->cells)<<'\n';
                if(m){std::cout<<"S\t"<<s<<'\t'<<f<<'\t'<<phase;m->writeSwing(std::cout);std::cout<<'\n';}
            }
            if(f%10==0) {
                std::ostringstream before;bridge.write(before);
                for(int width:{240,569}) {
                    const int extra=(width-240)/2,cameraX=bridge.x+r.parameters[8]/2-120-extra,cameraY=bridge.y-160;
                    soul::Canvas canvas(width,320,0xff102030),repeat(width,320,0xff102030),center(240,320);
                    bridge.draw(canvas,cameraX,cameraY);bridge.draw(repeat,cameraX,cameraY);
                    std::ostringstream after;bridge.write(after);
                    if(before.str()!=after.str() || canvas.pixels()!=repeat.pixels())throw soul::FormatError("Bridge drawing changed state or pixels");
                    for(int y=0;y<320;++y)std::copy_n(canvas.pixels().begin()+y*width+extra,240,center.pixels().begin()+y*240);
                    std::cout<<"R\t"<<s<<'\t'<<f<<'\t'<<width<<'\t'<<canvas.checksum()<<'\t'<<center.checksum()<<'\n';
                }
            }
        }
    }
}
