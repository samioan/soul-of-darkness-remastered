#include "player_probe.h"
#include "linked_passage.h"
#include <iostream>
void emitPassageManifest(const std::filesystem::path& root){
    const auto sprites=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));const auto sprite=soul::BSprite::parse(sprites.resource(0).value());
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"3"));const auto records=soul::actorRecords(pack.resource(0).value());
    std::vector<std::string> cases{"right","left","scaled","wrong-facing","stationary","wrong-motion","outside","edge","paused","hidden","frozen","late","target-hidden"};
    for(const auto& r:records)if(r.parameters[0]==59)cases.push_back("level-"+std::to_string(r.parameters[1]));
    for(const auto& name:cases){
        soul::ActorRecord source{},target{},pr{};source.count=target.count=12;pr.count=6;pr.parameters={0,186,180,199,0,0};
        source.parameters={59,10,180,150,0,8,0,0,10,80,11,2};target.parameters={59,11,500,250,0,8,-3,-10,10,80,10,1};
        if(name=="left")source.parameters[11]=1;
        if(name=="scaled")target.parameters[9]=30;
        if(name=="outside")pr.parameters[3]=80;
        if(name=="edge")pr.parameters[2]=173;
        if(name=="late")pr.parameters[2]=100;
        if(name=="hidden" || name=="frozen")source.parameters[5]|=name=="hidden"?1024:16384;
        if(name=="target-hidden")target.parameters[5]|=1024;
        const bool placed=name.rfind("level-",0)==0;
        if(placed){
            for(const auto& r:records)if(r.parameters[0]==59 && name=="level-"+std::to_string(r.parameters[1]))source=r;
            for(const auto& r:records)if(r.parameters[0]==59 && r.parameters[1]==source.parameters[10])target=r;
            pr.parameters[2]=static_cast<std::int16_t>(source.parameters[2]+source.parameters[6]+source.parameters[8]/2);
            pr.parameters[3]=static_cast<std::int16_t>(source.parameters[3]+source.parameters[7]+source.parameters[9]/2);
        }
        auto maps=soul::LevelMaps::load(pack,true);soul::Player player(sprite,maps,pr);
        player.vx=name=="stationary"?0:name=="left" || name=="wrong-motion" || (placed && source.parameters[11]==1)?-2048:2048;
        player.facingLeft=name=="left" || name=="wrong-facing" || (placed && source.parameters[11]==1);player.flags=player.facingLeft?1:0;player.refreshBounds();
        soul::LinkedPassage a(source),b(target);bool cameraActive=true;int cameraUid=777;
        for(int f=0;f<8;++f){
            if(name=="late" && f==2){player.x=180;player.fixedX=180*256;player.refreshBounds();}
            const bool moved=a.update(player,[&](int uid)->const soul::LinkedPassage*{return uid==b.uid && (name!="late" || f>=2)?&b:nullptr;},name=="paused" && f<3);
            if(moved){cameraActive=false;cameraUid=-1;}
            for(int phase=0;phase<2;++phase){
                if(phase==1)player.refreshBounds();
                std::cout<<"P\t"<<name<<'\t'<<f<<'\t'<<phase;
                for(int n:{player.fixedX,player.fixedY,player.x,player.y,player.vx,player.vy,player.flags,player.animation,player.step,player.animationTick})std::cout<<'\t'<<n;
                for(int n:player.collision)std::cout<<'\t'<<n;
                std::cout<<'\t'<<std::boolalpha<<a.linked()<<'\t'<<cameraActive<<'\t'<<cameraUid<<'\n';
            }
        }
    }
}
