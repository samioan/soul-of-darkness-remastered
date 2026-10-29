#include "player_probe.h"
#include "camera_controller.h"
#include "combat_world.h"
#include "enemy_type1.h"
#include "tile_world.h"
#include <algorithm>
#include <iostream>
void emitCameraControllerManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2")),level=soul::ResourcePack::numeric(soul::readAssetFile(root/"3"));
    const auto ps=soul::BSprite::parse(pack.resource(0).value()),es=soul::BSprite::parse(pack.resource(2).value());auto scene=soul::TileScene::load(root,0);const auto records=soul::actorRecords(level.resource(0).value());
    const auto record=[&](int uid){return *std::find_if(records.begin(),records.end(),[&](const auto& r){return r.parameters[1]==uid;});};
    for(int uid:{63,167})for(const std::string name:{"normal","stationary","outside","edge","point","missing","late","removed","source-hidden","pause","hidden","frozen","region-change","other-active","far-source","upward"}){
        const auto tr=record(uid);soul::CameraController t(tr);auto pr=record(186);pr.parameters[2]=uid==63?1650:1300;pr.parameters[3]=uid==63?335:800;soul::Player p(ps,scene.maps(),pr);soul::EnemyType1 source(es,scene.maps(),record(202),{10,10,0});
        bool sourceLive=name!="missing" && name!="late";soul::ControlledCamera c;c.active=name=="other-active";c.uid=c.active?999:-1;c.x=c.targetX=p.x-120;c.y=c.targetY=p.y-220;c.bounds={0,0,1820,1680};
        if(name=="source-hidden")source.flags=1033;if(name=="far-source"){source.x=100;source.y=1500;}
        for(int f=0;f<20;++f){const bool paused=name=="pause" && f>=3 && f<7,changed=name=="region-change" && f<7;
            if(f==4 && name=="late")sourceLive=true;if(f==6 && name=="removed"){sourceLive=false;source.alive=false;}
            p.x=uid==63?1650:1300;p.y=(uid==63?335:800)+f*2;if(name=="outside"||name=="other-active"||name=="normal" && f>=10 && f<14)p.x=500;
            p.vx=name=="stationary"?0:256;p.vy=name=="stationary"?0:name=="upward" && f<10?-512:512;p.refreshBounds();
            if(name=="edge")p.collision={tr.parameters[2]-10,tr.parameters[3],tr.parameters[2],tr.parameters[3]+5};
            if(name=="point")p.collision={tr.parameters[2]+1,tr.parameters[3]+1,tr.parameters[2]+1,tr.parameters[3]+1};
            if(name=="hidden"||name=="frozen")t.flags=8|(f<7?(name=="hidden"?1024:16384):0);
            t.update(p,c,[&](int)->soul::CameraSource{if(!sourceLive)return {};return {&source.x,&source.y,&source.alive,[&]{source.refreshBounds();}};},changed,paused);
            for(int phase=0;phase<2;++phase){if(phase==1&&!paused)c.advance(p,1820,1680);
                std::cout<<"C\t"<<uid<<'\t'<<name<<'\t'<<f<<'\t'<<phase;t.write(std::cout);c.write(std::cout);std::cout<<'\t'<<p.x<<'\n';
            }
        }
    }
    const auto fx=soul::BSprite::parse(pack.resource(1).value());
    for(const std::string name:{"linked-first","falling-first","world-pause","source-removed"}){
        soul::ActorRecord region{},source{},linked{},falling{},pr{};region.count=12;source.count=13;linked.count=falling.count=14;pr.count=6;
        region.parameters={22,210,0,0,0,8,0,0,800,400,0,-1};source.parameters={1,202,200,199,0,16392,-50,-75,80,60,0,0,-1};linked.parameters={49,63,90,190,0,8,0,0,200,80,0,202,0,0};falling.parameters={49,167,90,190,0,8,0,0,200,80,1,-1,0,0};pr.parameters={0,186,150,199,0,0};
        auto rows=name=="falling-first"?std::vector<soul::ActorRecord>{region,falling,linked,source}:std::vector<soul::ActorRecord>{region,linked,falling,source};
        soul::LevelMaps maps;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int n=400;n<800;++n)g.cells[n]=24;maps.collision=g;maps.layers[1].width=40;maps.layers[1].height=20;
        soul::Player p(ps,maps,pr);p.flags=16384;p.vx=256;p.vy=512;soul::PlayerMeters meters;soul::CombatWorld world(es,fx,maps,p,meters,rows,{10,10,0});world.refreshRegionSelection();world.applyRegionTransition();
        for(int f=0;f<10;++f){const bool paused=name=="world-pause" && f>=3 && f<7;
            if(f==4 && name=="source-removed")world.enemies.front().alive=false;
            world.updatePlayer(0,0,f,paused);world.finishPlayer(paused);
            for(int phase=0;phase<2;++phase){if(phase==1&&!paused)world.advance();
                std::cout<<"W\t"<<name<<'\t'<<f<<'\t'<<phase;
                for(int uid:{63,167})for(const auto& c:world.cameraControllers)if(c.uid==uid)std::cout<<'\t'<<std::boolalpha<<c.alive<<'\t'<<c.flags;
                world.camera.write(std::cout);auto q=world.updateQueueUids();std::cout<<'\t'<<q.size();for(int n:q)std::cout<<'\t'<<n;std::cout<<'\n';
            }
        }
    }

    for(int i=0;i<8;++i){const int flags[]{8,8,8,40,56,264,280,8},bucket[]{6,7,30,0,30,0,30,30};soul::CameraController c(record(63));c.flags=flags[i];c.bucket=bucket[i];soul::Player p(ps,scene.maps(),record(186));
        p.collision=i<2?std::array<int,4>{0,0,5,5}:i==7?std::array<int,4>{1591,322,1591,322}:std::array<int,4>{1590,321,1595,325};
        std::cout<<"E\t"<<i<<'\t'<<std::boolalpha<<c.eligible(p)<<'\n';
    }

}
