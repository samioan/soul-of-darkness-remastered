#include "player_probe.h"
#include "breakable_bridge.h"
#include "moving_platform.h"
#include <iostream>
#include <memory>
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
}
