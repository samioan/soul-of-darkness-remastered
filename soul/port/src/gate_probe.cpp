#include "player_probe.h"
#include "linked_gate.h"
#include <iostream>
namespace {
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
}
