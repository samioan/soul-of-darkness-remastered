#include "player_probe.h"
#include "player.h"
#include <iostream>
namespace {
void row(const soul::Player& p,const soul::ContactBody& surface,const std::string& name,int frame,int phase) {
    std::cout << "P\t" << name << '\t' << frame << '\t' << phase;p.write(std::cout);
    std::cout << '\t' << (!p.platform?0:p.platform==&surface?1:2) << '\t' << surface.platformLatch << '\n';
}
std::array<int,4> rect(const std::string& s,int frame) {
    const int dy=s=="rise"?-(frame/3)%30:s=="sink"?(frame/3)%30:0,dx=s=="slide"?frame/2:0;
    return {60+dx,160+dy,140+dx,170+dy};
}
}
void emitPlatformManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto sprite=soul::BSprite::parse(pack.resource(0).value());
    soul::LevelMaps maps;soul::TileGrid grid;grid.width=40;grid.height=20;grid.cells.resize(800);maps.collision=grid;maps.layers[1].width=40;maps.layers[1].height=20;
    soul::ActorRecord record{};record.count=6;record.parameters={0,186,100,120,0,0};
    for(int id=0;id<4320;++id) {
        int bits=id;soul::ContactBody surface,other;surface.platformLatch=1;surface.type=bits%2?50:33;bits/=2;
        const int xs[]{59,60,100,140,141},ys[]{154,159,165,166},velocities[]{-512,0,256,1536},sv[]{-512,0,512},poses[]{0,4,3};
        record.parameters[2]=static_cast<std::int16_t>(xs[bits%5]);bits/=5;
        record.parameters[3]=static_cast<std::int16_t>(ys[bits%4]);bits/=4;
        const int vy=velocities[bits%4];bits/=4;surface.vy=sv[bits%3];bits/=3;
        const int link=bits%3;bits/=3;record.parameters[4]=static_cast<std::int16_t>(poses[bits%3]);
        soul::Player p(sprite,maps,record);p.vy=vy;p.platform=link==0?nullptr:link==1?&surface:&other;
        surface.absoluteBounds=true;surface.localBounds={60,160,140,170};
        for(int phase=0;phase<2;++phase){soul::updatePlatformContact(surface,p,id%3-1);row(p,surface,"matrix",id,phase);}
    }
    record.parameters={0,186,100,120,0,0};
    for(const std::string s:{"stand","jump","walk","crouch","melee","combo","rise","sink","slide","pause","swing","swing_jump"}) {
        soul::Player p(sprite,maps,record);soul::ContactBody surface;surface.platformLatch=1;surface.type=s.rfind("swing",0)==0?50:33;surface.absoluteBounds=true;
        for(int frame=0;frame<96;++frame) {
            const bool paused=s=="pause" && frame>=20 && frame<30;
            const unsigned pressed=(s=="jump" || s=="swing_jump") && frame==20?16388:
                (s=="melee" || s=="combo") && (frame==20 || s=="combo" && frame==22)?65568:0;
            const unsigned held=s=="walk" && frame>=20 && frame<40?8256:s=="crouch" && frame>=20 && frame<40?33024:0;
            p.update(pressed,held,frame,paused);
            surface.localBounds=rect(s,frame);if(!paused)soul::updatePlatformContact(surface,p,s.rfind("swing",0)==0?1:0);
            row(p,surface,s,frame,0);
            if(!paused){p.refreshBounds();p.advanceAnimation();}
            row(p,surface,s,frame,1);
        }
    }
}
