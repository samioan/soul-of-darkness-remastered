#include "moving_platform.h"
#include "player_probe.h"
#include "pickup.h"
#include "tile_world.h"
#include <algorithm>
#include <iostream>
#include <sstream>
void emitMovingPlatformManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto ps=soul::BSprite::parse(pack.resource(0).value()),ms=soul::BSprite::parse(pack.resource(28).value());
    const auto levelPack=soul::ResourcePack::numeric(soul::readAssetFile(root/"4"));
    const auto records=soul::actorRecords(levelPack.resource(0).value());
    const auto scene=soul::TileScene::load(root,1);
    std::vector<std::string> cases={"rail0","rail1","rail2","rail3","rail4","rail5","rail6","rail7","rider","crouch","left","right","jump","high","low","pause","wall","ceiling"};
    for(int uid:{219,241,288})for(const std::string mode:{"rider","crouch","left","right","jump","high","low","pause"})cases.push_back("level1-"+std::to_string(uid)+"-"+mode);
    for(const std::string& s:cases) {
        const bool real=s.rfind("level1-",0)==0;
        const auto mode=real?s.substr(s.find_last_of('-')+1):s;
        soul::LevelMaps maps;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int i=720;i<800;++i)g.cells[i]=24;
        if(s=="wall")for(int r=0;r<18;++r)g.cells[r*40+16]=24;
        if(s=="ceiling")for(int i=160;i<200;++i)g.cells[i]=24;
        maps.collision=g;maps.layers[1].width=40;maps.layers[1].height=20;
        soul::ActorRecord pr{};pr.count=6;pr.parameters={0,186,static_cast<std::int16_t>(s.rfind("rail",0)==0?100:300),static_cast<std::int16_t>(s=="high"?80:s=="low"?340:200),0,0};
        soul::ActorRecord mr{};mr.count=13;mr.parameters={33,64,300,280,9,8,-120,-180,240,220,static_cast<std::int16_t>(s.rfind("rail",0)==0?s[4]-'0':8),5,20};
        if(real){
            const int uid=std::stoi(s.substr(7));
            const auto found=std::find_if(records.begin(),records.end(),[&](const auto& r){return r.parameters[0]==33 && r.parameters[1]==uid;});
            if(found==records.end())throw soul::FormatError("Missing level-1 moving-platform fixture record");
            mr=*found;maps=scene.maps();
            pr.parameters[2]=mr.parameters[2];
            pr.parameters[3]=mode=="high"?mr.parameters[3]+mr.parameters[7]-60:mode=="low"?mr.parameters[3]+mr.parameters[7]+mr.parameters[9]+60:mr.parameters[3]-80;
        }
        soul::Player p(ps,maps,pr,real?1:0);soul::MovingPlatform m(ms,maps,mr);soul::PlayerDamage damage;soul::PlayerMeters meters;soul::JavaRandom random;
        std::vector<soul::CombatEffectSpawn> effects;int hits=0,enemyHits=0;soul::CombatExchange combat{&p,&damage,&meters,&random,&effects,&hits,&enemyHits};
        for(int f=0;f<160;++f) {
            const bool paused=mode=="pause" && f>=30 && f<40;
            const unsigned pressed=mode=="jump" && f==40?16388:0;
            const unsigned held=mode=="crouch" && f>=30 && f<75?33024:mode=="left" && f>=30 && f<38?4112:mode=="right" && f>=30 && f<38?8256:0;
            p.update(pressed,held,f,paused);if(p.platform==&m.surface && p.ceiling)m.vy=0;m.update(p,paused,&combat);
            for(int phase=0;phase<2;++phase){
                if(phase==1 && !paused){p.refreshBounds();p.advanceAnimation();m.refreshBounds();m.advanceAnimation();}
                std::cout<<"P\t"<<s<<'\t'<<f<<'\t'<<phase;p.write(std::cout);std::cout<<'\t'<<(p.platform==&m.surface?1:0)<<'\t'<<meters.health<<'\n';
                std::cout<<"M\t"<<s<<'\t'<<f<<'\t'<<phase;m.write(std::cout);std::cout<<'\n';
            }
            if(real && f%20==0){
                std::ostringstream before;m.write(before);p.write(before);
                for(int width:{240,569}){
                    const int extra=(width-240)/2;
                    soul::Canvas canvas(width,320,0xff102030),repeat(width,320,0xff102030),center(240,320);
                    m.draw(canvas,m.x-120-extra,m.y-160,f);m.draw(repeat,m.x-120-extra,m.y-160,f);
                    std::ostringstream after;m.write(after);p.write(after);
                    if(before.str()!=after.str() || canvas.pixels()!=repeat.pixels())throw soul::FormatError("Platform drawing changed state or pixels");
                    for(int y=0;y<320;++y)std::copy_n(canvas.pixels().begin()+y*width+extra,240,center.pixels().begin()+y*240);
                    std::cout<<"R\t"<<s<<'\t'<<f<<'\t'<<width<<'\t'<<canvas.checksum()<<'\t'<<center.checksum()<<'\n';
                }
            }
        }
    }
}
