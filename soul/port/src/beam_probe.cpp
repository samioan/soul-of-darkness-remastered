#include "player_probe.h"
#include "beam_emitter.h"
#include "moving_platform.h"
#include "combat.h"
#include <iostream>
#include <algorithm>
void emitBeamManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2")),level=soul::ResourcePack::numeric(soul::readAssetFile(root/"3")),stat=soul::ResourcePack::numeric(soul::readAssetFile(root/"18"));
    const auto sprite=soul::BSprite::parse(pack.resource(10).value()),effect=soul::BSprite::parse(pack.resource(1).value()),ps=soul::BSprite::parse(pack.resource(0).value());
    auto scene=soul::TileScene::load(root,0);const auto records=soul::actorRecords(level.resource(0).value());std::vector<soul::ActorRecord> emitters;for(const auto& r:records)if(r.parameters[0]==10)emitters.push_back(r);
    soul::BinaryReader reader(stat.resource(0).value());const int count=reader.i32be();std::vector<int> stats;for(int t=0;t<count;++t){const int n=reader.i32be();for(int j=0;j<n;++j){const int v=reader.i32be();if(t==9)stats.push_back(v);}}reader.done();const auto trig=soul::loadPlatformTrig(root);
    for(int scenario=0;scenario<12;++scenario){auto r=emitters.at(scenario<6?scenario:0);auto table=stats;
        if(scenario>=6 && scenario<=9){r.parameters[4]=scenario==6?0:scenario==7?8:4;r.parameters[5]=8|(scenario==9?1:0);r.parameters[12]=scenario==8?125:scenario==9?-125:0;r.parameters[13]=scenario==6?125:scenario==7?-125:0;}
        if(scenario==10)table[r.parameters[11]*4+2]=0;
        soul::BeamEmitter e(sprite,effect,scene.maps(),r,table,trig);soul::Player player(ps,scene.maps(),soul::playerRecord(records));
        for(int frame=0;frame<360;++frame){const bool paused=scenario==11 && frame>=20 && frame<40;const auto box=e.activation;player.collision=frame==0?std::array<int,4>{box[0]+1,box[1]+1,box[0]+2,box[1]+2}:std::array<int,4>{-1000,-1000,-999,-999};e.update(player,paused);
            for(int phase=0;phase<2;++phase){if(phase && !paused)e.advanceAnimation();std::cout<<"B\tbeam"<<scenario<<'\t'<<frame<<'\t'<<phase;for(int n:{e.action,e.timer,int(e.active),e.endX,e.endY,e.animation,e.pending,e.step,e.tick,e.flags,int(e.active)})std::cout<<'\t'<<n;std::cout<<'\n';}
        }
    }

    int id=0;
    for(int direction:{8,4,1,2})for(int variant=0;variant<2;++variant)for(int permanent=0;permanent<2;++permanent)for(int pose:{0,8,9})for(int immune=0;immune<2;++immune)for(int wall=0;wall<2;++wall){
        soul::Player player(ps,scene.maps(),soul::playerRecord(records));player.x=100;player.y=199;player.animation=pose;player.step=player.animationTick=player.flags=0;player.pending=-1;player.wallLeft=player.wallRight=wall!=0;
        soul::PlayerDamage state;state.invulnerability=immune?500:0;soul::JavaRandom random;std::vector<soul::CombatEffectSpawn> effects;int health=23;
        int source=direction==1?95:105;if(!variant && direction<8)source+=direction==1?15:-15;
        player.receiveBeamHit(10,source,permanent && direction>=4,state,health,80,random,effects);
        std::cout<<"D\t"<<id++;for(int n:{player.animation,player.pending,player.step,player.animationTick,player.flags,player.vx,player.ax,int(player.facingLeft),health,state.invulnerability,static_cast<int>(effects.size())})std::cout<<'\t'<<n;
        for(const auto& e:effects)for(int n:{e.animation,e.x,e.y,e.flags})std::cout<<'\t'<<n;std::cout<<'\t'<<random.nextInt()<<'\n';
    }
}
