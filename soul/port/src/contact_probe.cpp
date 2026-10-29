#include "player_probe.h"
#include "player.h"
#include <iostream>
namespace {
void row(const soul::ContactBody& b,int id,int phase,int who) {
    std::cout << "C\t" << id << '\t' << phase << '\t' << who;
    for(int n:{b.x,b.y,b.vx,int(b.touched),int(b.leftContact),int(b.rightContact),int(b.grounded),int(b.wallLeft),int(b.wallRight)}) std::cout << '\t' << n;
    for(int n:b.collision) std::cout << '\t' << n;
    std::cout << '\n';
}
}
void emitContactManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto sprite=soul::BSprite::parse(pack.resource(0).value());
    for(int resource:{0,2,3}) {
        const auto image=soul::BSprite::parse(pack.resource(resource).value());
        for(std::size_t animation=0;animation<image.animations().size();++animation)
            for(int frame=0;frame<image.animations()[animation].count;++frame) for(int flags=0;flags<4;++flags) {
                std::cout << "B\t" << resource << '\t' << animation << '\t' << frame << '\t' << flags;
                for(int n:soul::contactBounds(image,static_cast<int>(animation),frame,flags)) std::cout << '\t' << n;
                std::cout << '\n';
            }
    }
    const auto local=soul::contactBounds(sprite,0,0,0);
    for(int id=0;id<7056;++id) {
        soul::ContactBody p,b,c; p.type=0; p.x=100; b.x=108; c.x=92;
        p.y=b.y=c.y=199; p.localBounds=b.localBounds=c.localBounds=local;
        b.enabled=b.separates=true;
        if(id<6912) {
            int bits=id; p.x=100+(bits%3-1)*8; bits/=3;
            p.vx=(bits%3-1)*2560; bits/=3; b.vx=(bits%3-1)*768; bits/=3;
            p.grounded=bits%2!=0; bits/=2;
            p.wallLeft=(bits&1)!=0; p.wallRight=(bits&2)!=0; bits/=4;
            b.wallLeft=(bits&1)!=0; b.wallRight=(bits&2)!=0; bits/=4;
            b.pushLocked=bits%2!=0; bits/=2;
            p.leftContact=(bits&1)!=0; p.rightContact=(bits&2)!=0; b.x=100;
        } else if(id<7040) {
            const int bits=id-6912; constexpr int types[]{1,37,80,82}; b.type=types[bits%4];
            b.flags=(bits&4)?1024:0; b.enabled=(bits&8)==0; b.action=bits/16; b.pushLocked=true;
        } else {
            const int bits=id-7040; b.type=(bits&1)?37:1; c.type=(bits&2)?80:1;
            b.pushLocked=(bits&4)!=0; c.pushLocked=(bits&8)!=0;
            p.grounded=true; c.enabled=c.separates=true;
        }
        p.refreshBounds(); b.refreshBounds(); c.refreshBounds();
        const std::vector<soul::ContactBody*> queue{&b,&p,nullptr,&c};
        for(int phase=0;phase<2;++phase) {
            if(!phase) soul::resolvePlayerContacts(p,queue);
            else { p.refreshBounds(); soul::separatePlayerContacts(p,queue); }
            row(p,id,phase,0); row(b,id,phase,1); row(c,id,phase,2);
        }
    }
    for(const std::string scenario:{"locked_right","locked_left","push_right","push_left","paused","air"}) {
        soul::LevelMaps maps; soul::TileGrid grid; grid.width=40; grid.height=20; grid.cells.resize(800);
        for(int i=400;i<800;++i) grid.cells[i]=24;
        maps.collision=grid; maps.layers[1].width=40; maps.layers[1].height=20;
        soul::ActorRecord record{}; record.count=6; record.parameters[1]=186; record.parameters[2]=100; record.parameters[3]=199;
        soul::Player p(sprite,maps,record); soul::ContactBody b;
        const bool left=scenario.size()>=4 && scenario.substr(scenario.size()-4)=="left";
        b.x=left?70:130; b.y=199; b.localBounds=local; b.enabled=true; b.pushLocked=scenario.substr(0,4)!="push"; b.refreshBounds();
        for(int tick=0;tick<64;++tick) {
            const bool paused=scenario=="paused" && tick<10;
            p.update(scenario=="air" && tick==5?16388:0,tick<30?(left?4112:8256):0,tick,paused,{&b});
            for(int phase=0;phase<2;++phase) {
                if(phase==1 && !paused) p.advanceAnimation();
                std::cout << "U\t" << scenario << '\t' << tick << '\t' << phase; p.write(std::cout);
                std::cout << '\t' << b.x << '\t' << int(b.touched) << '\n';
            }
        }
    }
}
