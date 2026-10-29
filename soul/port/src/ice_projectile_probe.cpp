#include "player_probe.h"
#include "ice_magic.h"
#include <iostream>
void emitIceProjectileManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));const auto ps=soul::BSprite::parse(pack.resource(0).value()),fx=soul::BSprite::parse(pack.resource(1).value());
    for(const std::string s:{"flight","left","wall","left-wall","old-wall","pause","hidden","frozen","remove-wall","ride","walkoff","jump","slope16","slope17","slope18","slope19","slope20","slope21","edge"}){
        soul::LevelMaps maps;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int i=400;i<800;++i)g.cells[i]=24;maps.collision=g;maps.layers[1].width=40;maps.layers[1].height=20;
        if(s!="flight" && s!="left" && s!="edge"){const int col=s=="left-wall"?3:10,value=s.rfind("slope",0)==0?std::stoi(s.substr(5)):24;for(int row=0;row<20;++row)maps.collision->cells[row*40+col]=static_cast<std::uint8_t>(value);}
        const bool left=s.rfind("left",0)==0;soul::ActorRecord pr{};pr.count=6;pr.parameters={0,186,static_cast<std::int16_t>(left?180:100),199,0,0};soul::Player p(ps,maps,pr);if(left){p.facingLeft=true;p.flags=1;}
        soul::IceProjectile e(fx,maps,p);if(s=="hidden" || s=="frozen")e.flags|=s=="hidden"?1024:16384;
        for(int f=0;f<180;++f){const bool paused=s=="pause" && f>=8 && f<18;
            if(s=="remove-wall" && f==40)for(int row=0;row<20;++row)maps.collision->cells[row*40+10]=0;
            if((s=="ride" || s=="walkoff" || s=="jump") && f==20){p.x=e.x;p.y=e.collision[1]-2;p.vy=512;p.refreshBounds();}
            if(s=="walkoff" && f==30)p.x+=100;
            if(s=="jump" && f==30){p.y-=20;p.vy=-4608;}
            e.update(p,{0,0,240,320},{0,0,800,400},s!="old-wall",[](soul::IceProjectile&){},paused);
            for(int phase=0;phase<2;++phase){if(phase==1 && !paused)e.advance();std::cout<<"I\t"<<s<<'\t'<<f<<'\t'<<phase;e.write(std::cout);for(int n:{p.x,p.y,p.vx,p.vy,p.pending,int(p.platform==&e.surface)})std::cout<<'\t'<<n;std::cout<<'\n';}
        }
    }
}
