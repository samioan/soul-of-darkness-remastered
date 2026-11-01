#include "chandelier_script.h"
#include "combat_world.h"
#include "level_data.h"
#include "moving_platform.h"
#include "pickup.h"
#include "player_probe.h"
#include <algorithm>
#include <iostream>
// Level-1 ambush scripts 23/24/114: real level-1 records, enemies, zero-excursion chandeliers and triggers driven in the
// original handler order (selected scripts, enemies, chandeliers, triggers). Rows mirror tools/reference_checks/AmbushCheck.java.
namespace {
struct Case{const char* name;std::vector<std::array<int,3>> chains;int px,py;};
template<class A> void row(const A& a,int facing){
    for(int n:{a.fixedX,a.fixedY,a.x,a.y,a.vx,a.vy,a.ax,a.ay,a.flags,a.animation,a.pending,a.step,a.tick,a.action})std::cout<<'\t'<<n;
    for(bool b:{a.grounded,a.ceiling,a.wallLeft,a.wallRight})std::cout<<'\t'<<std::boolalpha<<b;
    for(const auto& r:{a.collision,a.attack,a.visual})for(int n:r)std::cout<<'\t'<<n;
    std::cout<<'\t'<<facing<<'\n';
}
}
void emitAmbushManifest(const std::filesystem::path& root){
    using namespace soul;
    const std::vector<Case> cases{
        {"s23",{{115,92,89}},150,200},{"s24",{{118,93,90}},450,200},{"s114",{{123,94,91}},450,200},
        {"pair",{{118,93,90},{123,94,91}},450,200},{"outside",{{115,92,89}},700,200},{"edge",{{115,92,89}},276,200},{"edge-out",{{115,92,89}},277,200},
        {"pause",{{115,92,89}},150,200},{"hidden",{{118,93,90}},450,200},{"leave",{{123,94,91}},450,200},{"source-hidden",{{115,92,89}},150,200},
        {"late-enemy-hidden",{{115,92,89}},150,200}};
    for(const auto& c:cases){
        auto data=LevelData::load(root,1);
        auto record=[&](int uid){const auto it=std::find_if(data.records.begin(),data.records.end(),[&](const auto& r){return r.parameters[1]==uid;});if(it==data.records.end())throw FormatError("Missing level-1 ambush record");return *it;};
        auto pr=playerRecord(data.records);pr.parameters[2]=c.px;pr.parameters[3]=c.py;
        Player p(data.sprite(data.level.playerSprite),data.scene.maps(),pr,1);
        const int n=int(c.chains.size());
        const int cx=std::max(0,std::min(2360-240,c.px-120)),cy=std::max(0,std::min(1340-320,c.py-160));
        std::vector<std::unique_ptr<EnemyType1>> enemies;std::vector<std::unique_ptr<MovingPlatform>> chandeliers;std::vector<ChandelierTrigger> triggers;std::vector<std::unique_ptr<ChandelierScript>> scripts;std::vector<int> scriptIndex,buckets(n,0);
        const auto decoded=decodeScripts(ByteView(data.scriptBytes));
        for(const auto& chain:c.chains){
            auto tr=record(chain[0]);
            enemies.push_back(std::make_unique<EnemyType1>(data.sprite(data.level.enemySprite),data.scene.maps(),record(chain[1]),data.stats(0)));
            chandeliers.push_back(std::make_unique<MovingPlatform>(data.sprite(data.level.platformSprite),data.scene.maps(),record(chain[2]),&data.trig));
            triggers.emplace_back(tr);
            const int uid=tr.parameters[16];const auto it=std::find_if(decoded.begin(),decoded.end(),[&](const auto& d){return d.uid==uid;});
            scripts.push_back(std::make_unique<ChandelierScript>(*it));scriptIndex.push_back(int(it-decoded.begin()));
        }
        if(c.name==std::string("source-hidden"))p.flags=1024;
        if(c.name==std::string("hidden"))triggers[0].flags=136|1024;
        if(c.name==std::string("late-enemy-hidden"))enemies[0]->flags=2056|1024;
        PlayerDamage damage;PlayerMeters meters;JavaRandom random(12345);std::vector<CombatEffectSpawn> effects;int hits=0,enemyHits=0;CombatExchange combat{&p,&damage,&meters,&random,&effects,&hits,&enemyHits};
        for(auto& m:chandeliers)m->contactDamage=data.stats(0).at(1);
        std::vector<FallingTarget> targets;for(auto& e:enemies)targets.emplace_back(e.get());
        SelectedScriptSlots slots;std::array<int,3> indexSlots{-1,-1,-1};
        const auto resolve=[&](int uid)->ChandelierActor{
            for(int i=0;i<n;++i){auto& e=*enemies[i];if(e.uid==uid)return {&e.x,&e.y,&e.flags,&e.action,&buckets[i],[&e](int v){e.scriptAnimation(v);},[&e]{e.refreshBounds();},[&e](int v){e.scriptAction(v);},&e.facingLeft};}
            for(auto& m:chandeliers)if(m->uid==uid)return {&m->x,&m->y,&m->flags,&m->action,&m->bucket,[&m](int v){m->scriptAnimation(v);},[&m]{m->refreshBounds();}};
            return {};
        };
        for(int f=0;f<100;++f){
            const bool paused=std::string(c.name)=="pause" && f>=4 && f<10;
            if(std::string(c.name)=="leave" && f==2)p.x=900;
            if(!paused)for(int s=0;s<slots.count;++s){for(auto& script:scripts)if(script->uid()==slots.uids[s])script->update(resolve,cx,cy,paused);}
            for(int i=0;i<n;++i)enemies[i]->update({p.x,p.fixedX,p.collision,p.attack,p.fixedY},paused,&random,&meters,&combat);
            for(int i=0;i<n;++i)chandeliers[i]->update(p,paused,&combat,targets);
            for(int i=0;i<n;++i){
                auto& t=triggers[i];ChandelierScript* script=nullptr;for(auto& s:scripts)if(s->uid()==t.scriptUid)script=s.get();
                const bool wasAlive=t.alive;const int starts=script->starts;t.update(&p,*script,paused);
                if(script->starts!=starts){slots.select(script->uid());indexSlots[slots.count-1]=scriptIndex[std::find_if(scripts.begin(),scripts.end(),[&](const auto& s){return s.get()==script;})-scripts.begin()];}
                if(wasAlive && !t.alive)slots.release();
            }
            for(int phase=0;phase<2;++phase){
                if(phase==1 && !paused)for(int i=0;i<n;++i){enemies[i]->refreshBounds();enemies[i]->advanceAnimation();chandeliers[i]->refreshBounds();chandeliers[i]->advanceAnimation();}
                std::cout<<"S\t"<<c.name<<'\t'<<f<<'\t'<<phase<<'\t'<<slots.count;for(int v:indexSlots)std::cout<<'\t'<<v;std::cout<<'\n';
                for(int i=0;i<n;++i){std::cout<<"T\t"<<c.name<<'\t'<<f<<'\t'<<phase<<'\t'<<scripts[i]->uid()<<'\t'<<scripts[i]->ticks;for(int o:scripts[i]->offsets())std::cout<<'\t'<<o;std::cout<<'\n';}
                for(int i=0;i<n;++i)std::cout<<"R\t"<<c.name<<'\t'<<f<<'\t'<<phase<<'\t'<<i<<'\t'<<triggers[i].latch<<'\t'<<std::boolalpha<<triggers[i].alive<<'\n';
                for(int i=0;i<n;++i){std::cout<<"A\t"<<c.name<<'\t'<<f<<'\t'<<phase<<'\t'<<i;row(*enemies[i],enemies[i]->facingLeft?1:0);}
                for(int i=0;i<n;++i){std::cout<<"A\t"<<c.name<<'\t'<<f<<'\t'<<phase<<'\t'<<n+i;row(*chandeliers[i],0);}
            }
        }
    }
}
namespace {
struct WorldCase{const char* name;std::vector<int> triggers;int px,py,pausedTicks;bool stagger;int ticks;};
}
// Controlled CombatWorld fixtures: only the ambush records (player, enemies 92/93/94, chandeliers 89/90/91, triggers 115/118/123) are
// constructed; the player is frozen so overlap is positioned explicitly. These are not a level route.
void emitAmbushWorldManifest(const std::filesystem::path& root){
    using namespace soul;
    const std::vector<WorldCase> cases{
        {"s23",{115},150,200,0,false,120},{"s24",{118},450,200,0,false,120},{"s114",{123},450,200,0,false,120},
        {"pair",{118,123},450,200,0,false,120},{"paused",{115},150,200,30,false,150},{"stagger",{118,123},317,200,0,true,120},
        {"no-overlap",{115,118,123},700,200,0,false,120}};
    for(const auto& sc:cases){
        auto data=LevelData::load(root,1);
        std::vector<ActorRecord> owned;
        for(const auto& r:data.records){
            const int type=r.parameters[0],uid=r.parameters[1];
            if(type==0 || uid==115 || uid==118 || uid==123 || uid==92 || uid==93 || uid==94 || uid==89 || uid==90 || uid==91)owned.push_back(r);
        }
        // Keep only the triggers of this scenario, with their enemies/chandeliers (other chains stay inert but present).
        owned.erase(std::remove_if(owned.begin(),owned.end(),[&](const ActorRecord& r){return r.parameters[0]==40 && std::find(sc.triggers.begin(),sc.triggers.end(),r.parameters[1])==sc.triggers.end();}),owned.end());
        Player player(data.sprite(data.level.playerSprite),data.scene.maps(),playerRecord(data.records),1);PlayerMeters meters;
        CombatWorld world(data.sprite(data.level.enemySprite),data.sprite(data.level.effectSprite),data.scene.maps(),player,meters,owned,data.stats(0));
        world.loadPlatforms(data.sprite(data.level.platformSprite),owned,&data.trig,data.stats(0).at(1));
        world.loadChandelierScript(ByteView(data.scriptBytes),owned);
        const std::size_t triggerCount=world.chandelierTriggers.size(),scriptCount=world.chandelierScripts.size();
        player.x=sc.px;player.y=sc.py;player.fixedX=player.x*256;player.fixedY=player.y*256;player.flags=16384;player.refreshBounds();world.damage.invulnerability=60000;world.advance();
        std::vector<int> starts(2,-1),ends(2,-1);int thrown=-1;std::string message;int maxSlots=0;
        for(int tick=0;tick<sc.ticks && thrown<0;++tick){
            const bool paused=tick<sc.pausedTicks;
            if(sc.stagger && tick==1){player.x=318;player.fixedX=player.x*256;player.refreshBounds();}
            world.refreshRegionSelection();
            try{world.updatePlayer(0,0,tick,paused);world.finishPlayer(paused);world.advance();}
            catch(const FormatError& e){thrown=tick;message=e.what();break;}
            maxSlots=std::max(maxSlots,world.selectedScriptCount());
            for(std::size_t i=0;i<world.chandelierScripts.size() && i<2;++i){const int t=world.chandelierScripts[i]->ticks;if(t>=0 && starts[i]<0)starts[i]=tick;if(t==-2 && starts[i]>=0 && ends[i]<0)ends[i]=tick;}
        }
        std::cout<<"W\t"<<sc.name<<'\t'<<triggerCount<<'\t'<<scriptCount<<'\t'<<thrown<<'\t'<<(message.find("background")!=std::string::npos?1:0)<<'\t'<<maxSlots<<'\t'<<world.selectedScriptCount();
        for(int chain=0;chain<2;++chain){
            if(chain>=int(world.chandelierScripts.size())){for(int pad=0;pad<10;++pad)std::cout<<"\t-9";continue;}
            auto& script=*world.chandelierScripts[chain];
            const auto enemy=std::find_if(world.enemies.begin(),world.enemies.end(),[&](const auto& e){return e.uid==script.enemyUid();});
            const auto chandelier=std::find_if(world.platforms.begin(),world.platforms.end(),[&](const auto& m){return m.uid==script.chandelierUid();});
            std::cout<<'\t'<<script.uid()<<'\t'<<starts[chain]<<'\t'<<ends[chain]<<'\t'<<script.starts<<'\t'<<script.finishes<<'\t'<<(enemy->flags&16384?1:0)<<'\t'<<enemy->action<<'\t'<<chandelier->action;
            std::cout<<'\t'<<chandelier->y<<'\t'<<int(world.chandelierTriggers[chain].alive);
        }
        std::cout<<'\n';
        world.detach();
    }
}
