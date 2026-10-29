#include "player_probe.h"
#include "combat_world.h"
#include <iostream>
namespace {
bool overlapping(const std::string& s){return s.rfind("overlap",0)==0;}
int playerX(const std::string& s,int f) {
    if(overlapping(s))return 150;
    if(s=="gap"){const int x[]{100,180,200,220,221,300,100};return x[std::min(6,f/6)];}
    if(s=="edge"){const int x[]{1,200,201,400,0};return x[f/8];}
    if(s=="distant")return f<8?700:100;
    return f/8%2==0?100:350;
}
std::vector<soul::ActorRecord> records(const std::string& s) {
    soul::ActorRecord a{},b{},c{},e{},f{};a.count=b.count=c.count=12;e.count=f.count=13;
    a.parameters={22,210,0,0,0,static_cast<std::int16_t>(s=="hidden-region"?1032:s=="flags-region"?40:8),0,0,static_cast<std::int16_t>(s=="gap"?180:overlapping(s)?300:200),400,0,-1};
    b.parameters={22,211,static_cast<std::int16_t>(s=="gap"?220:overlapping(s)?100:200),0,0,8,0,0,200,400,0,-1};
    c.parameters={22,212,50,0,0,8,0,0,350,400,0,-1};
    e.parameters={1,19,195,199,0,9,-100,-125,200,125,0,0,-1};f=e;f.parameters[1]=20;f.parameters[2]=300;
    if(s=="no-region")return {e,f};if(s=="enemy-first")return {e,f,a,b};
    if(s=="interleaved" || s=="paused")return {e,a,f,b};if(s=="overlap-last")return {e,a,f,b,c};
    if(overlapping(s))return {a,b,c,e,f};return {a,b,e,f};
}
void rows(const soul::Player& p,const soul::CombatWorld& world,const soul::PlayerMeters& meters,const std::string& s,int frame,int phase,bool cleanup) {
    std::cout<<"P\t"<<s<<'\t'<<frame<<'\t'<<phase;p.write(std::cout);std::cout<<'\n';
    for(std::size_t i=0;i<world.enemies.size();++i){std::cout<<(i==0?"E":"E2")<<'\t'<<s<<'\t'<<frame<<'\t'<<phase;world.enemies[i].write(std::cout);std::cout<<'\n';}
    std::cout<<"W\t"<<s<<'\t'<<frame<<'\t'<<phase<<'\t'<<world.currentRegion()<<'\t'<<world.previousRegion()<<'\t'<<std::boolalpha<<world.regionTransitionPending()<<'\t'<<cleanup;
    for(int n:world.regionCameraBounds())std::cout<<'\t'<<n;
    const auto queue=world.updateQueueUids();std::cout<<'\t'<<meters.health<<'\t'<<world.damage.invulnerability<<'\t'<<queue.size();
    for(int uid:queue)std::cout<<'\t'<<uid;std::cout<<'\n';
}
}
void emitWorldOrderManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto ps=soul::BSprite::parse(pack.resource(0).value()),es=soul::BSprite::parse(pack.resource(2).value()),fx=soul::BSprite::parse(pack.resource(1).value());
    for(const std::string s:{"region-first","enemy-first","interleaved","overlap-first","overlap-last","gap","paused","no-region","edge","hidden-region","flags-region","distant"}) {
        soul::LevelMaps maps;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int i=400;i<800;++i)g.cells[i]=24;maps.collision=g;maps.layers[1].width=40;maps.layers[1].height=20;
        soul::ActorRecord pr{};pr.count=6;pr.parameters={0,186,static_cast<std::int16_t>(playerX(s,0)),199,0,0};
        soul::Player p(ps,maps,pr);p.flags=16384;soul::PlayerMeters meters;soul::CombatWorld world(es,fx,maps,p,meters,records(s),{10,10,0});world.damage.invulnerability=60000;
        for(auto& e:world.enemies){e.action=101;if(s=="distant")e.flags|=64;}
        for(int frame=0;frame<40;++frame) {
            const bool paused=s=="paused" && frame>=8 && frame<12;
            p.x=playerX(s,frame);p.fixedX=p.x*256;p.refreshBounds();world.updatePlayer(0,0,frame,paused);const bool cleanup=world.finishPlayer(paused);
            rows(p,world,meters,s,frame,0,cleanup);
            if(!paused){p.refreshBounds();p.advanceAnimation();world.advance();}
            rows(p,world,meters,s,frame,1,cleanup);
        }
    }
}
