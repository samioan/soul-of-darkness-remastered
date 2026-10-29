#include "player_probe.h"
#include "chandelier_script.h"
#include "moving_platform.h"
#include "pickup.h"
#include "tile_world.h"
#include <iostream>
#include <algorithm>
namespace {
template<class A> void row(const A& a){for(int n:{a.fixedX,a.fixedY,a.x,a.y,a.vx,a.vy,a.ax,a.ay,a.flags,a.animation,a.pending,a.step,a.tick,a.action})std::cout<<'\t'<<n;for(bool b:{a.grounded,a.ceiling,a.wallLeft,a.wallRight})std::cout<<'\t'<<std::boolalpha<<b;for(const auto& r:{a.collision,a.attack,a.visual})for(int n:r)std::cout<<'\t'<<n;std::cout<<'\n';}
}
void emitChandelierManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2")),level=soul::ResourcePack::numeric(soul::readAssetFile(root/"3")),statPack=soul::ResourcePack::numeric(soul::readAssetFile(root/"18"));
    const auto ps=soul::BSprite::parse(pack.resource(0).value()),es=soul::BSprite::parse(pack.resource(2).value()),ms=soul::BSprite::parse(pack.resource(28).value());auto scene=soul::TileScene::load(root,0);const auto records=soul::actorRecords(level.resource(0).value());const auto scripts=soul::decodeScripts(level.resource(1).value());const auto data=std::find_if(scripts.begin(),scripts.end(),[](const auto& s){return s.uid==160;});const auto trig=soul::loadPlatformTrig(root);
    soul::BinaryReader stat(statPack.resource(0).value());stat.i32be();const int count=stat.i32be();std::vector<int> stats;for(int i=0;i<count;++i)stats.push_back(stat.i32be());
    auto record=[&](int uid){return *std::find_if(records.begin(),records.end(),[&](const auto& r){return r.parameters[1]==uid;});};
    for(const std::string name:{"normal","leave","outside","edge","missing-source","pause","hidden","frozen","source-hidden","completion-hidden"}){
        auto pr=record(186);pr.parameters[2]=name=="outside"?300:name=="edge"?158:100;pr.parameters[3]=1100;soul::Player p(ps,scene.maps(),pr);soul::EnemyType1 e(es,scene.maps(),record(205),stats);soul::MovingPlatform m(ms,scene.maps(),record(34),&trig);auto tr=record(161);if(name=="missing-source")tr.parameters[13]=999;soul::ChandelierTrigger t(tr);soul::ChandelierScript script(*data);int enemyBucket=0;
        if(name=="source-hidden")p.flags=1024;if(name=="hidden"||name=="frozen")t.flags=136|(name=="hidden"?1024:16384);
        soul::PlayerDamage damage;soul::PlayerMeters meters;soul::JavaRandom random(12345);std::vector<soul::CombatEffectSpawn> effects;int hits=0,enemyHits=0;soul::CombatExchange combat{&p,&damage,&meters,&random,&effects,&hits,&enemyHits};m.contactDamage=stats.at(1);
        const auto resolve=[&](int uid)->soul::ChandelierActor{if(uid==205)return {&e.x,&e.y,&e.flags,&e.action,&enemyBucket,[&](int n){e.scriptAnimation(n);},[&]{e.refreshBounds();}};if(uid==34)return {&m.x,&m.y,&m.flags,&m.action,&m.bucket,[&](int n){m.scriptAnimation(n);},[&]{m.refreshBounds();}};return {};};
        for(int f=0;f<50;++f){const bool paused=name=="pause"&&f>=4&&f<10;if(name=="leave"&&f==2)p.x=300;if(name=="completion-hidden"&&f==5)t.flags=1160;if(name=="completion-hidden"&&f==16)t.flags=136;
            script.update(resolve,0,880,paused);e.update({p.x,p.fixedX,p.collision,p.attack,p.fixedY},paused,&random,&meters,&combat);m.update(p,paused,&combat);t.update(name=="missing-source"?nullptr:&p,script,paused);
            for(int phase=0;phase<2;++phase){if(phase==1&&!paused){e.refreshBounds();e.advanceAnimation();m.refreshBounds();m.advanceAnimation();}
                std::cout<<"S\t"<<name<<'\t'<<f<<'\t'<<phase<<'\t'<<script.ticks<<'\t'<<(t.latch==1?1:0)<<'\t'<<t.latch<<'\t'<<std::boolalpha<<t.alive;for(int n:script.offsets())std::cout<<'\t'<<n;std::cout<<'\n';
                std::cout<<"A\t"<<name<<'\t'<<f<<'\t'<<phase<<"\t0";row(e);std::cout<<"A\t"<<name<<'\t'<<f<<'\t'<<phase<<"\t1";row(m);
            }
        }
    }
}
