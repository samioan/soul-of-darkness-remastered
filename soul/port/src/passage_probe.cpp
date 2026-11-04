#include "player_probe.h"
#include "linked_passage.h"
#include "tile_world.h"
#include "gameplay_view.h"
#include <iostream>
#include <algorithm>
namespace {
void levelOne(const std::filesystem::path& root,const soul::BSprite& sprite){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"4"));
    const auto records=soul::actorRecords(pack.resource(0).value());
    const auto maps=soul::LevelMaps::load(pack,true);
    const auto scene=soul::TileScene::load(root,1);
    for(const auto& source:records)if(source.parameters[0]==59)for(const std::string mode:{"cross","wrong-facing","stationary","wrong-motion","outside","edge","paused","hidden","frozen","late","target-hidden"}){
        const auto found=std::find_if(records.begin(),records.end(),[&](const auto& r){return r.parameters[0]==59 && r.parameters[1]==source.parameters[10];});
        if(found==records.end())throw soul::FormatError("Missing second-level passage destination");
        soul::LinkedPassage a(source),b(*found);
        const int px=a.collision[0]+source.parameters[8]/2,py=a.collision[1]+source.parameters[9]/2;
        auto pr=soul::playerRecord(records);pr.parameters[2]=static_cast<std::int16_t>(px);pr.parameters[3]=static_cast<std::int16_t>(py);
        soul::Player p(sprite,maps,pr,1);
        p.facingLeft=(a.direction==1) != (mode=="wrong-facing");p.flags=p.facingLeft?1:0;
        p.vx=mode=="stationary"?0:(a.direction==1?-2048:2048)*(mode=="wrong-motion"?-1:1);
        if(mode=="outside" || mode=="late"){p.x=-1000;p.fixedX=-256000;}
        p.refreshBounds();
        if(mode=="edge")p.collision={a.collision[0]-2,py,a.collision[0],py+1};
        if(mode=="hidden" || mode=="frozen")a.flags|=mode=="hidden"?1024:16384;
        if(mode=="target-hidden")b.flags|=1024;
        bool cameraActive=true;int cameraUid=777;
        for(int f=0;f<8;++f){
            if(mode=="late" && f==2){p.x=px;p.fixedX=px*256;p.refreshBounds();}
            if(a.update(p,[&](int uid)->const soul::LinkedPassage*{return uid==b.uid && (mode!="late" || f>=2)?&b:nullptr;},mode=="paused" && f<3)){cameraActive=false;cameraUid=-1;}
            for(int phase=0;phase<2;++phase){
                if(phase==1)p.refreshBounds();
                std::cout<<"P\tlevel1-"<<a.uid<<'-'<<mode<<'\t'<<f<<'\t'<<phase;
                for(int n:{p.fixedX,p.fixedY,p.x,p.y,p.vx,p.vy,p.flags,p.animation,p.step,p.animationTick})std::cout<<'\t'<<n;
                for(int n:p.collision)std::cout<<'\t'<<n;
                std::cout<<'\t'<<std::boolalpha<<a.linked()<<'\t'<<cameraActive<<'\t'<<cameraUid<<'\n';
            }
            if(f==0 && mode=="cross"){
                std::array<int,4> room{};int regionUid=-1;
                for(const auto& r:records)if(r.parameters[0]==22 && !(r.parameters[5]&(1024|16384))){
                    const auto& q=r.parameters;
                    const int x=static_cast<std::uint16_t>(q[2])+q[6],y=static_cast<std::uint16_t>(q[3])+q[7];
                    // Select the destination volume's room explicitly. The transferred
                    // player can sit exactly on its strict left boundary for one tick.
                    const int tx=(b.collision[0]+b.collision[2])/2,ty=(b.collision[1]+b.collision[3])/2;
                    if(tx>x && tx<=x+q[8] && ty>y && ty<=y+q[9]){room={x,y,x+q[8],y+q[9]};regionUid=q[1];break;}
                }
                if(regionUid<0)throw soul::FormatError("Passage destination has no room");
                const auto draw=[&](int width){
                    const auto view=soul::gameplayView(p.x,p.y,width,320,maps.width(),maps.height(),&room);
                    soul::Canvas canvas(width,320,0xff102030),repeat(width,320,0xff102030);
                    for(auto* c:{&canvas,&repeat}){c->setClip(view.left,view.top,view.right-view.left,view.bottom-view.top);scene.draw(*c,view.x,view.y);}
                    if(canvas.pixels()!=repeat.pixels())throw soul::FormatError("Passage destination drawing is not repeatable");
                    for(int y=0;y<320;++y)for(int x=0;x<width;++x)
                        if((x<view.left || x>=view.right || y<view.top || y>=view.bottom) && canvas.pixels()[y*width+x]!=0xff102030)throw soul::FormatError("Packed room leaked outside passage viewport");
                    std::cout<<"R\t"<<a.uid<<'\t'<<regionUid<<'\t'<<width<<'\t'<<canvas.checksum()<<'\n';
                    return std::make_pair(view,std::move(canvas));
                };
                const int beforeX=p.x,beforeY=p.y,beforeFixed=p.fixedX;const auto beforeBounds=p.collision;
                const auto narrow=draw(240),wide=draw(569);
                const auto& n=narrow.first;const auto& w=wide.first;
                for(int y=n.top;y<n.bottom;++y)for(int x=n.left;x<n.right;++x){
                    const int wx=n.x+x-w.x,wy=n.y+y-w.y;
                    if(wx>=w.left && wx<w.right && wy>=w.top && wy<w.bottom && narrow.second.pixels()[y*240+x]!=wide.second.pixels()[wy*569+wx])throw soul::FormatError("Passage room pixels differ by viewport width");
                }
                if(p.x!=beforeX || p.y!=beforeY || p.fixedX!=beforeFixed || p.collision!=beforeBounds)throw soul::FormatError("Passage presentation changed player state");
            }
        }
    }
}
}
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
    levelOne(root,sprite);
}
