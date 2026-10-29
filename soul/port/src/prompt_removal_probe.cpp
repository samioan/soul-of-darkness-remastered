#include "player_probe.h"
#include "combat_world.h"
#include <algorithm>
#include <iostream>
void emitPromptRemovalManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2")),level=soul::ResourcePack::numeric(soul::readAssetFile(root/"3"));
    const auto ms=soul::BSprite::parse(pack.resource(28).value());auto scene=soul::TileScene::load(root,0);const auto records=soul::actorRecords(level.resource(0).value());const auto trig=soul::loadPlatformTrig(root);
    const auto record=[&](int uid){return *std::find_if(records.begin(),records.end(),[&](const auto& r){return r.parameters[1]==uid;});};
    for(int uid:{43,61})for(const std::string name:{"normal","outside","edge","miss","source-hidden","pause","hidden","frozen","late-source","late-target","missing-source","missing-target","target-removed"}){
        auto tr=record(uid);soul::PromptRemovalTrigger t(tr);soul::MovingPlatform m(ms,scene.maps(),record(t.sourceUid),&trig);soul::PlacedPrompt p(record(t.targetUid));
        bool sourceLive=name!="late-source" && name!="missing-source",targetLive=name!="late-target" && name!="missing-target";
        if(name=="hidden"||name=="frozen")t.flags=136|(name=="hidden"?1024:16384);if(name=="source-hidden")m.flags=1024;
        for(int f=0;f<12;++f){
            if(f==6 && name=="late-source")sourceLive=true;if(f==6 && name=="late-target")targetLive=true;
            if(f==2 && name=="target-removed"){p.alive=false;p.collision={};targetLive=false;}
            m.refreshBounds();int dx=tr.parameters[2]+tr.parameters[6]-(name=="edge"||name=="miss"?m.collision[2]:m.collision[0]),dy=tr.parameters[3]+tr.parameters[7]-m.collision[1];
            if(f<4||name=="outside")dx+=500;if(name=="miss")--dx;m.x+=dx;m.y+=dy;
            t.update([&](int)->soul::MovingPlatform*{return sourceLive?&m:nullptr;},[&](int)->soul::PlacedPrompt*{return targetLive?&p:nullptr;},name=="pause" && f>=3 && f<7);
            if(!p.alive)targetLive=false;
            std::cout<<"T\t"<<uid<<'\t'<<name<<'\t'<<f;t.write(std::cout);std::cout<<'\t'<<std::boolalpha<<targetLive;for(int n:p.collision)std::cout<<'\t'<<n;std::cout<<'\n';
        }
    }
    const auto ps=soul::BSprite::parse(pack.resource(0).value()),es=soul::BSprite::parse(pack.resource(2).value()),fx=soul::BSprite::parse(pack.resource(1).value()),promptSprite=soul::BSprite::parse(pack.resource(18).value());
    for(const std::string name:{"prompt-first","trigger-first","world-pause","disabled-target"}){
        soul::ActorRecord region{},platform{},prompt{},trigger{},pr{};region.count=platform.count=12;prompt.count=10;trigger.count=19;pr.count=6;
        region.parameters={22,210,0,0,0,8,0,0,800,400,0,-1};platform.parameters={50,34,500,199,5,16384,-140,2,1,30,1,-1};prompt.parameters={21,59,110,199,14,8,-60,0,120,120};trigger.parameters={40,61,100,199,0,8,0,0,80,120,1,0,5,34,-1,-1,59,-1,-1};pr.parameters={0,186,100,199,0,0};if(name=="disabled-target")prompt.parameters[5]|=32;
        const std::vector<soul::ActorRecord> rows=name=="trigger-first"?std::vector<soul::ActorRecord>{region,trigger,prompt,platform}:std::vector<soul::ActorRecord>{region,prompt,trigger,platform};
        soul::LevelMaps maps;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int n=400;n<800;++n)g.cells[n]=24;maps.collision=g;maps.layers[1].width=40;maps.layers[1].height=20;
        soul::Player player(ps,maps,pr);player.flags=16384;soul::PlayerMeters meters;soul::CombatWorld world(es,fx,maps,player,meters,rows,{10,10,0});world.loadPlatforms(ms,rows,&trig);world.loadPrompts(promptSprite,rows);world.refreshRegionSelection();world.applyRegionTransition();
        for(int f=0;f<10;++f){const bool paused=name=="world-pause" && f>=3 && f<7;
            if(f==4){auto& m=world.platforms.front();m.refreshBounds();m.x+=100-m.collision[0];m.y+=199-m.collision[1];}
            world.updatePlayer(0,0,f,paused);world.finishPlayer(paused);
            std::cout<<"W\t"<<name<<'\t'<<f<<'\t'<<std::boolalpha<<world.promptRemovalTriggers.front().alive<<'\t'<<world.prompts.front().alive<<'\t'<<world.prompts.front().status;
            auto q=world.updateQueueUids();std::cout<<'\t'<<q.size();for(int n:q)std::cout<<'\t'<<n;std::cout<<'\n';
        }
    }

}
