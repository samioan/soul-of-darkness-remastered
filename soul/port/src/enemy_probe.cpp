#include "player_probe.h"
#include "enemy_type1.h"
#include <iostream>
void emitEnemyManifest(const std::filesystem::path& root) {
    const auto sprites=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto sprite=soul::BSprite::parse(sprites.resource(2).value());
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"18"));
    soul::BinaryReader reader(pack.resource(0).value(),"actor stats");
    const int count=reader.i32be(); if(count!=20) throw soul::FormatError("Unexpected stat table count");
    std::vector<int> stats;
    for(int row=0;row<count;++row) {
        const int n=reader.i32be(); if(n<0 || n>10000) throw soul::FormatError("Invalid stat row");
        for(int i=0;i<n;++i) { const int value=reader.i32be(); if(row==0) stats.push_back(value); }
    }
    reader.done();
    for(const std::string s:{"idle","pursue","right","attack0","attack1","attack2","attack3","fall","ledge0","ledge1","wall","slope16","slope17","slope18","slope19","slope20","slope21","paused","inactive","flag32","touched"}) {
        soul::LevelMaps maps; soul::TileGrid grid; grid.width=40; grid.height=20; grid.cells.resize(800);
        for(int i=400;i<800;++i) grid.cells[i]=24;
        if(s.rfind("ledge",0)==0) for(int y=10;y<20;++y) for(int x=0;x<7;++x) grid.cells[y*40+x]=0;
        if(s=="wall") for(int y=0;y<10;++y) grid.cells[y*40+7]=24;
        if(s.rfind("slope",0)==0) for(int x=0;x<40;++x) grid.cells[400+x]=static_cast<std::uint8_t>(std::stoi(s.substr(5)));
        maps.collision=grid; maps.layers[1].width=40; maps.layers[1].height=20;
        soul::ActorRecord record{}; record.count=13;
        record.parameters={1,19,180,199,0,9,-100,-125,200,125,0,0,-1};
        record.parameters[10]=s.rfind("attack",0)==0?static_cast<std::int16_t>(s[6]-'0'):s=="ledge1"?1:0;
        if(s=="fall") record.parameters[3]=119;
        if(s=="inactive") record.parameters[5]|=1024;
        if(s=="flag32") record.parameters[5]|=32;
        soul::EnemyType1 enemy(sprite,maps,record,stats);
        for(int frame=0;frame<96;++frame) {
            const bool paused=s=="paused" && frame>=6 && frame<20;
            soul::EnemyTarget p; p.x=s=="idle"?600:s=="right"?260:100;
            const int py=s=="fall"?100:130; p.collision={p.x-5,py,p.x+5,py+1};
            if(s.rfind("attack",0)==0 && frame>=12) {
                p.x=enemy.x-30; const auto& c=enemy.collision; p.collision={c[0]-2,c[1]-9,c[0]-1,c[1]-8};
            }
            p.fixedX=p.x*256;
            if(s=="touched" && frame==10) enemy.touched=true;
            enemy.update(p,paused);
            auto row=[&](int phase) { std::cout << "E\t" << s << '\t' << frame << '\t' << phase; enemy.write(std::cout); std::cout << '\n'; };
            row(0);
            if(!paused && !(enemy.flags&1024)) { enemy.refreshBounds(); enemy.advanceAnimation(); }
            row(1);
        }
    }
}
