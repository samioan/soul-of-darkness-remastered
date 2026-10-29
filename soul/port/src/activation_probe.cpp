#include "player_probe.h"
#include "combat_world.h"
#include <iostream>
namespace {
soul::LevelMaps arena(){soul::LevelMaps m;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int i=400;i<800;++i)g.cells[i]=24;m.collision=g;m.layers[1].width=40;m.layers[1].height=20;return m;}
soul::ActorRecord playerRecord(int x){soul::ActorRecord r{};r.count=6;r.parameters={0,186,static_cast<std::int16_t>(x),199,0,0};return r;}
std::vector<soul::ActorRecord> worldRecords(const std::string& name){
    soul::ActorRecord r{},t{},x{},y{};r.count=12;t.count=19;x.count=13;
    r.parameters={22,214,0,0,0,8,0,0,800,400,0,-1};t.parameters={40,129,80,150,0,12,0,0,40,80,1,0,0,186,-1,-1,-1,114,117};
    x.parameters={1,114,180,199,0,static_cast<std::int16_t>(2088|(name=="forced"?16:0)),-100,-125,200,125,0,0,-1};y=x;y.parameters[1]=117;y.parameters[2]=260;
    return name=="enemy-first"?std::vector<soul::ActorRecord>{x,y,r,t}:std::vector<soul::ActorRecord>{r,t,x,y};
}
void triggerRow(const soul::PlayerEnableTrigger& t,int xFlags,int yFlags,const std::string& tag,const std::string& name,int f){
    std::cout<<tag<<'\t'<<name<<'\t'<<f;t.write(std::cout);std::cout<<'\t'<<xFlags<<'\t'<<yFlags<<'\n';
}
}
void emitActivationManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));const auto ps=soul::BSprite::parse(pack.resource(0).value()),es=soul::BSprite::parse(pack.resource(2).value()),fx=soul::BSprite::parse(pack.resource(1).value());
    for(const std::string name:{"overlap","outside","edge","miss","pause","hidden","frozen","missing-source","late-source","missing-target","late-target","fade-target","preserved"}){
        auto maps=arena();const auto pr=playerRecord(100);soul::Player p(ps,maps,pr);
        soul::ActorRecord r{};r.count=19;r.parameters={40,129,180,150,0,12,0,0,80,80,1,0,0,static_cast<std::int16_t>(name=="missing-source" || name=="late-source"?999:186),-1,-1,-1,114,117};soul::PlayerEnableTrigger t(r);
        if(name=="hidden" || name=="frozen")t.flags|=name=="hidden"?1024:16384;
        int xFlags=name=="preserved"?17848:2216,yFlags=2217,xAction=name=="fade-target"?5:0,yAction=0;
        for(int f=0;f<12;++f){
            p.x=name=="pause"?180:name=="outside"?100:name=="edge"?170:name=="miss"?169:f<4?100:180;p.fixedX=p.x*256;
            const bool source=name!="missing-source" && (name!="late-source" || f>=4),targets=name!="missing-target" && (name!="late-target" || f>=6);
            t.update(source?&p:nullptr,[&](int uid)->soul::EnableEnemyTarget{if(!targets)return {};return uid==114?soul::EnableEnemyTarget{&xFlags,&xAction}:uid==117?soul::EnableEnemyTarget{&yFlags,&yAction}:soul::EnableEnemyTarget{};},name=="pause" && f<4);
            triggerRow(t,xFlags,yFlags,"T",name,f);
        }
    }
    for(const std::string name:{"trigger-first","enemy-first","forced","enter-late"}){
        auto maps=arena();const auto pr=playerRecord(name=="enter-late"?300:100);soul::Player p(ps,maps,pr);p.flags=16384;soul::PlayerMeters m;
        soul::CombatWorld world(es,fx,maps,p,m,worldRecords(name),{10,10,0});world.damage.invulnerability=60000;world.refreshRegionSelection();world.applyRegionTransition();
        for(int f=0;f<32;++f){
            p.x=name=="enter-late" && f<4?300:100;p.fixedX=p.x*256;p.refreshBounds();world.updatePlayer(0,0,f);world.finishPlayer();
            for(int phase=0;phase<2;++phase){
                if(phase==1){p.refreshBounds();p.advanceAnimation();world.advance();}
                triggerRow(world.playerEnableTriggers[0],world.enemies[0].flags,world.enemies[1].flags,"W"+std::to_string(phase),name,f);
                for(std::size_t i=0;i<2;++i){std::cout<<(i==0?"E":"E2")<<'\t'<<name<<'\t'<<f<<'\t'<<phase;world.enemies[i].write(std::cout);std::cout<<'\n';}
                const auto q=world.updateQueueUids();std::cout<<"Q\t"<<name<<'\t'<<f<<'\t'<<phase<<'\t'<<q.size();for(int uid:q)std::cout<<'\t'<<uid;std::cout<<'\n';
            }
        }
    }
    for(const std::string name:{"terminal","count1","forced-fade","keep-flag","pause-death","live-2048"}){
        auto maps=arena();const auto pr=playerRecord(name=="live-2048"?100:600);soul::Player p(ps,maps,pr);soul::PlayerMeters m;m.health=30;
        const int px=name=="live-2048"?100:600;p.collision={px-5,100,px+5,101};
        soul::ActorRecord r{};r.count=13;r.parameters={1,19,180,199,0,static_cast<std::int16_t>(2056|(name=="forced-fade"?4:0)),-100,-125,200,125,0,0,-1};
        soul::EnemyType1 e(es,maps,r,{10,10,name=="count1"?1:0});soul::JavaRandom random;
        if(name!="live-2048"){e.action=3;e.health=-5;}if(name=="keep-flag")e.flags=2568;
        bool removed=false;const soul::EnemyTarget target{p.x,p.fixedX,p.collision,p.attack};
        for(int f=0;f<80;++f){
            const bool paused=name=="pause-death" && f<4;
            if(name=="count1" && f==40){e.action=3;e.health=-5;}
            e.effects.clear();if(e.alive)e.update(target,paused,&random,&m);removed|=!e.alive;
            for(int phase=0;phase<2;++phase){
                if(phase==1 && !paused && e.alive && !(e.flags&1024)){e.refreshBounds();e.advanceAnimation();}
                std::cout<<"D\t"<<name<<'\t'<<f<<'\t'<<phase;
                for(int n:{e.fixedX,e.fixedY,e.x,e.y,e.vx,e.vy,e.ax,e.ay,e.flags,e.animation,e.pending,e.step,e.tick,e.action,e.timer,e.paletteTimer,e.damageCooldown,e.health,e.respawnCount,int(e.enabled),int(e.alive),int(e.facingLeft)})std::cout<<'\t'<<n;
                for(const auto& rect:{e.collision,e.attack,e.visual,e.activation,e.interaction})for(int n:rect)std::cout<<'\t'<<n;std::cout<<'\t'<<int(removed)<<'\n';
            }
        }
        std::cout<<"N\t"<<name<<'\t'<<random.nextInt()<<'\n';
    }
}
