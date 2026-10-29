#include "player_probe.h"
#include "linked_area_trigger.h"
#include "combat_world.h"
#include <iostream>
namespace {
std::array<int,4> region(const std::string& s,int f) {
    const bool edge=s=="left-edge" || s=="right-edge";
    if(s=="outside" || (f>=8 && f<16 && !edge && s!="corner" && s!="miss"))return {0,0,199,200};
    if(s=="left-edge")return {0,0,200,200};if(s=="right-edge")return {223,0,400,200};
    if(s=="corner")return {223,223,400,400};if(s=="miss")return {224,223,400,400};return {0,0,400,400};
}
}
void emitLinkedTriggerManifest(const std::filesystem::path& root) {
    for(const std::string s:{"overlap","outside","left-edge","right-edge","corner","miss","paused","hidden","frozen","late","missing","preserved"}) {
        soul::ActorRecord r{};r.count=19;r.parameters={40,182,200,200,0,8,0,0,23,23,0,0,2,-1,-1,-1,25,177,-1};
        soul::LinkedAreaTrigger t(r);if(s=="hidden")t.flags=1160;if(s=="frozen")t.flags=16520;
        int x=s=="preserved"?48:160,y=17760;
        for(int f=0;f<32;++f) {
            auto bounds=region(s,f);
            t.update(&bounds,[&](int uid)->int*{if(s=="missing" || (s=="late" && f<10))return nullptr;return uid==25?&x:uid==177?&y:nullptr;},s=="paused" && f>=4 && f<12);
            std::cout<<"T\t"<<s<<'\t'<<f;t.write(std::cout);std::cout<<'\t'<<x<<'\t'<<y<<'\n';
        }
    }
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto ps=soul::BSprite::parse(pack.resource(0).value()),es=soul::BSprite::parse(pack.resource(2).value()),fx=soul::BSprite::parse(pack.resource(1).value()),ms=soul::BSprite::parse(pack.resource(28).value());
    const auto trig=soul::loadPlatformTrig(root);
    for(const std::string s:{"region-first","trigger-first","platform-first","world-paused"}) {
        soul::LevelMaps maps;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int i=400;i<800;++i)g.cells[i]=24;maps.collision=g;maps.layers[1].width=40;maps.layers[1].height=20;
        soul::ActorRecord pr{},a{},b{},t{},x{},y{};pr.count=6;pr.parameters={0,186,100,199,0,0};
        a.count=b.count=x.count=y.count=12;t.count=19;
        a.parameters={22,210,0,0,0,8,0,0,200,400,0,-1};b.parameters={22,211,400,0,0,8,0,0,400,400,0,-1};
        t.parameters={40,182,100,200,0,8,0,0,23,23,0,0,2,-1,-1,-1,25,177,-1};
        x.parameters={50,25,600,199,5,16416,-100,256,16,30,1,-1};y=x;y.parameters[1]=177;y.parameters[2]=650;
        const std::vector<soul::ActorRecord> records=s=="trigger-first"?std::vector<soul::ActorRecord>{t,a,b,x,y}:s=="platform-first"?std::vector<soul::ActorRecord>{x,y,a,b,t}:std::vector<soul::ActorRecord>{a,b,t,x,y};
        soul::Player p(ps,maps,pr);p.flags=16384;soul::PlayerMeters meters;soul::CombatWorld world(es,fx,maps,p,meters,records,{10,10,0});
        world.loadPlatforms(ms,records,&trig);world.refreshRegionSelection();world.applyRegionTransition();
        for(int f=0;f<32;++f) {
            const bool paused=s=="world-paused" && f>=8 && f<12;
            p.x=f/8%2==0?100:600;p.fixedX=p.x*256;p.refreshBounds();world.updatePlayer(0,0,f,paused);world.finishPlayer(paused);
            std::cout<<"Q\t"<<s<<'\t'<<f<<'\t'<<world.currentRegion()<<'\t'<<std::boolalpha<<world.regionTransitionPending();world.linkedAreaTriggers[0].write(std::cout);
            for(const auto& platform:world.platforms)std::cout<<'\t'<<platform.flags;
            const auto queue=world.updateQueueUids();std::cout<<'\t'<<queue.size();for(int uid:queue)std::cout<<'\t'<<uid;std::cout<<'\n';
        }
    }
}
