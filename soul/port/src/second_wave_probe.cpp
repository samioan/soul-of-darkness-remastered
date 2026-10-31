#include "player_probe.h"
#include "combat_world.h"
#include <iostream>
namespace {
soul::LevelMaps arena(){soul::LevelMaps m;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int i=400;i<800;++i)g.cells[i]=24;m.collision=g;m.layers[1].width=40;m.layers[1].height=20;return m;}
void enemyRow(const soul::EnemyType1& e,const char* tag,const std::string& s,int f,int phase){
    std::cout<<tag<<'\t'<<s<<'\t'<<f<<'\t'<<phase;
    for(int n:{e.fixedX,e.fixedY,e.x,e.y,e.vx,e.vy,e.ax,e.ay,e.flags,e.animation,e.pending,e.step,e.tick,e.action})std::cout<<'\t'<<n;
    for(bool b:{e.grounded,e.ceiling,e.wallLeft,e.wallRight})std::cout<<'\t'<<(b?"true":"false");
    for(const auto& r:{e.collision,e.attack,e.visual})for(int n:r)std::cout<<'\t'<<n;std::cout<<'\t'<<e.health<<'\t'<<e.damageCooldown<<'\n';
}
}
void emitFlyingWorldManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));const auto ps=soul::BSprite::parse(pack.resource(0).value()),es=soul::BSprite::parse(pack.resource(2).value()),fly=soul::BSprite::parse(pack.resource(3).value()),fx=soul::BSprite::parse(pack.resource(1).value()),items=soul::BSprite::parse(pack.resource(33).value());
    for(const std::string s:{"fight","contact","left","mixed"}){
        auto maps=arena();soul::ActorRecord pr{},er{},other{};pr.count=6;er.count=other.count=13;pr.parameters={0,186,static_cast<std::int16_t>(s=="left"?240:100),199,0,0};er.parameters={2,19,180,174,0,8,-200,-200,400,400,4,0,-1};other.parameters={1,20,220,199,0,9,-100,-125,200,125,0,0,7};
        const std::vector<soul::ActorRecord> records=s=="mixed"?std::vector<soul::ActorRecord>{er,other}:std::vector<soul::ActorRecord>{er};
        soul::Player p(ps,maps,pr);soul::PlayerMeters m;soul::CombatWorld w(es,fx,maps,p,m,records,{10,10,0});w.loadFlyingEnemies(fly,records,{8,10,0});std::vector<soul::Pickup> loot;
        for(int f=0;f<96;++f){
            const unsigned held=s!="contact" && f<12?(s=="left"?4112:8256):0,pressed=s!="contact" && f>=8 && f<60 && f%6==2?65568:0;
            const auto previous=loot.size();w.updatePlayer(pressed,held,f);w.finishPlayer();
            for(const auto& d:w.loot)loot.push_back(soul::Pickup::enemyDrop(items,d.animation,d.x,d.y,d.flags));
            if(p.animation!=9)for(std::size_t i=0;i<previous;++i)loot[i].update(p,m);
            for(int phase=0;phase<2;++phase){
                if(phase==1){p.refreshBounds();p.advanceAnimation();w.advance();for(auto& item:loot)if(item.alive){item.refreshBounds();item.advanceAnimation();}}
                std::cout<<"P\t"<<s<<'\t'<<f<<'\t'<<phase;p.write(std::cout);std::cout<<'\t'<<m.health<<'\t'<<w.damage.invulnerability<<'\t'<<(p.deathComplete?12:8)<<'\n';
                enemyRow(w.flyingEnemies[0],"E",s,f,phase);if(s=="mixed")enemyRow(w.enemies[0],"E2",s,f,phase);
            }
        }
    }
}
void emitRemovedEnemyManifest(const std::filesystem::path&){
    for(const std::string s:{"normal","one-left","reverse","missing-source","missing-target","late-target","early-remove","index-zero","pause","hidden","frozen","fade-target","source-hidden","slot-b"}){
        soul::ActorRecord r{};r.count=19;r.parameters=s=="slot-b"?std::array<std::int16_t,20>{40,104,180,150,0,12,0,0,80,80,4,0,0,10,11,-1,20,21,-1}:std::array<std::int16_t,20>{40,54,180,150,0,12,0,0,80,80,4,0,0,10,11,-1,-1,20,21};soul::RemovedEnemyTrigger t(r);
        if(s=="hidden" || s=="frozen")t.flags|=s=="hidden"?1024:16384;
        bool a=s!="missing-source" && s!="early-remove",b=s!="early-remove";std::vector<bool> removed(32);if(s=="early-remove"){removed[5]=true;removed[6]=true;}
        int xFlags=2216,yFlags=2217,action=s=="fade-target"?5:0;
        for(int f=0;f<16;++f){
            if(s!="early-remove" && s!="index-zero"){
                if(f==4){if(s=="reverse"){b=false;removed[6]=true;}else{a=false;removed[5]=true;}}
                if(f==8 && s!="one-left"){if(s=="reverse"){a=false;removed[5]=true;}else{b=false;removed[6]=true;}}
            }
            const bool targets=s!="missing-target" && (s!="late-target" || f>=10);
            t.update([&](int uid)->std::optional<int>{if(uid==10 && a)return s=="index-zero"?0:5;if(uid==11 && b)return s=="index-zero"?0:6;return std::nullopt;},removed,[&](int uid)->soul::EnableEnemyTarget{if(!targets)return {};return uid==20?soul::EnableEnemyTarget{&xFlags,&action}:soul::EnableEnemyTarget{&yFlags,&action};},s=="pause" && f>=3 && f<10);
            std::cout<<"R\t"<<s<<'\t'<<f;t.write(std::cout);std::cout<<'\t'<<xFlags<<'\t'<<yFlags<<'\n';
        }
    }
}
