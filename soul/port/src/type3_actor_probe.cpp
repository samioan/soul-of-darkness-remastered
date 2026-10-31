#include "enemy_type3.h"
#include "level_data.h"
#include "player_probe.h"
#include "pickup.h"
#include <algorithm>
#include <iostream>
#include <memory>
#include <sstream>
namespace {
struct Case { const char* name; int p10,flags,stat,ex,ey,link,px0,px1; };
// Keep in step with tools/reference_checks/Type3ActorCheck.java.
const Case cases[]={
    {"charger",7,9,0,400,100,-1,320,380},{"charger-left",7,8,0,400,100,-1,480,420},{"charger-touch",7,9,0,300,100,-1,285,285},{"charger-away",7,9,0,400,100,-1,700,700},
    {"thrower",9,9,0,500,100,-1,380,380},{"thrower-left",9,8,0,500,100,-1,620,620},{"thrower-near",9,9,0,500,100,-1,490,490},{"thrower-walk",9,9,0,500,100,-1,400,260},{"thrower-hurt",9,9,0,500,100,-1,380,380},
    {"thrower-fade",9,2185,4,500,100,-1,380,380},{"charger-fade",7,2185,4,400,100,-1,320,320},
    {"runner",24,2056,0,300,100,353,330,330},{"runner-kill",24,2056,0,300,100,353,330,330},{"runner-free",24,2056,0,300,100,-1,330,330},
    {"ledge-runner",24,2057,0,470,100,-1,500,500},{"ledge-charger",7,9,0,470,100,-1,500,560},{"ledge-thrower",9,9,0,470,100,-1,590,590},
    {"pause",9,9,0,500,100,-1,380,380},{"hidden",9,1033,0,500,100,-1,380,380},{"frozen",9,16393,0,500,100,-1,380,380},{"flag32",9,41,0,500,100,-1,380,380},
    {"slope",9,9,0,500,100,-1,380,380},{"thrower-reflect",9,9,0,500,100,-1,380,380},{"melee",9,9,0,500,100,-1,490,490}};
int damageAt(const std::string& s,int f){
    if(s=="thrower-hurt")return f==10?5:f==40?5:f==60?1000:0;
    if(s=="thrower-fade")return f==60?1000:0;
    if(s=="charger-fade")return f==30?1000:0;
    if(s=="runner-kill")return f==70?1000:0;
    if(s=="thrower-reflect")return f==30?1000:0;
    return 0;
}
bool queued(int flags){return (flags&16) || !(flags&(256|32));}
}
void emitType3ActorManifest(const std::filesystem::path& root){
    using namespace soul;
    const auto pack=ResourcePack::numeric(readAssetFile(root/"2"));
    const auto enemySprite=BSprite::parse(pack.resource(3).value()),shotSprite=BSprite::parse(pack.resource(4).value()),playerSprite=BSprite::parse(pack.resource(0).value());
    const auto chestSprite=BSprite::parse(pack.resource(34).value()),promptSprite=BSprite::parse(pack.resource(18).value());
    const auto data=LevelData::load(root,1);const auto& stats=data.stats(3);
    for(const auto& c:cases){
        const std::string s=c.name;
        LevelMaps maps;TileGrid grid;grid.width=40;grid.height=20;grid.cells.resize(800);for(int i=400;i<800;++i)grid.cells[i]=24;
        if(s.rfind("runner",0)==0)for(int y=0;y<10;++y)grid.cells[y*40+5]=24;
        if(s.rfind("ledge",0)==0)for(int y=10;y<15;++y)for(int x=25;x<40;++x)grid.cells[y*40+x]=0;
        if(s=="slope")for(int i=400;i<440;++i)grid.cells[i]=18;
        maps.collision=std::move(grid);maps.layers[1].width=40;maps.layers[1].height=20;
        ActorRecord pr{};pr.count=6;pr.parameters={0,186,static_cast<std::int16_t>(c.px0),199,0,0};
        Player player(playerSprite,maps,pr,1);PlayerMeters meters;PlayerDamage damage;damage.invulnerability=60000;JavaRandom random;std::vector<CombatEffectSpawn> playerEffects;
        ThrownObjects shots(shotSprite,maps,1);
        ActorRecord er{};er.count=13;er.parameters={3,48,static_cast<std::int16_t>(c.ex),static_cast<std::int16_t>(c.ey),9,static_cast<std::int16_t>(c.flags),-125,-125,250,175,static_cast<std::int16_t>(c.p10),static_cast<std::int16_t>(c.stat),static_cast<std::int16_t>(c.link)};
        if(s.rfind("runner",0)==0){er.parameters[6]=12;er.parameters[7]=-200;er.parameters[8]=40;er.parameters[9]=175;}
        EnemyType3 e(enemySprite,maps,er,stats,1);
        std::unique_ptr<Container> chest;
        if(c.link!=-1){ActorRecord cr{};cr.count=7;cr.parameters={52,static_cast<std::int16_t>(c.link),static_cast<std::int16_t>(c.ex),static_cast<std::int16_t>(c.ey-31),0,0,1};chest=std::make_unique<Container>(chestSprite,promptSprite,maps,cr);}
        int playerHits=0,enemyHits=0,kills=0;
        Type3Services services;services.shots=&shots;
        services.findChest=[&](int uid)->Container*{return chest && chest->uid==uid && chest->alive?chest.get():nullptr;};
        services.hitPlayer=[&](ThrownObject& shot){player.receiveBeamHit(15,shot.x,false,damage,meters.health,meters.maxHealth,random,playerEffects);};
        for(int f=0;f<140;++f){
            const bool paused=s=="pause" && f>=6 && f<16;const int dmg=damageAt(s,f);const int px=c.px0+(c.px1-c.px0)*f/140;
            e.effects.clear();e.loot.clear();
            for(auto& shot:shots.objects)shot.fresh=false;
            EnemyTarget target{px,px*256,{px-8,150,px+8,199},{10000,0,10001,1},199*256};
            if(s=="thrower-reflect" && f>=56 && f<=64)for(auto& shot:shots.objects)if(shot.alive && shot.action==103)target.attack=shot.collision;
            if(s=="melee" && (f==10 || f==11 || f==30 || f==31 || f==70 || f==71))target.attack=e.collision;
            player.collision=target.collision;
            if(dmg>0 && e.alive)e.applyDamage(dmg,true,target,{},random);
            // Original queue order: placed enemy, linked chest, then shots that existed at tick start.
            const bool enemyQueued=queued(e.flags),chestQueued=chest && chest->alive && queued(chest->flags);
            std::vector<ThrownObject*> shotQueue;for(auto& shot:shots.objects)if(shot.alive && queued(shot.flags))shotQueue.push_back(&shot);
            CombatExchange exchange{&player,&damage,&meters,&random,&playerEffects,&playerHits,&enemyHits};
            const int health=e.health;
            if(enemyQueued){e.update(target,paused,&random,&meters,&exchange,services);if(e.attachedType()!=0)e.dispatchAttached(target,paused,services);}
            if(health>0 && e.health<=0)++kills;
            if(chestQueued){chest->update(player,0,paused,false,&random,false);}
            for(auto* shot:shotQueue)if(shot->alive)shot->update(target,services.hitActors,services.hitPlayer,paused);
            std::vector<std::string> births;
            const auto birth=[&](int type,int animation,int x,int y,int flags){std::ostringstream row;row<<"L\t"<<s<<'\t'<<f<<'\t'<<type<<'\t'<<animation<<'\t'<<x<<'\t'<<y<<'\t'<<flags;births.push_back(row.str());};
            for(const auto& effect:e.effects)birth(39,effect.animation,effect.x,effect.y,effect.flags);
            for(const auto& item:e.loot)birth(35,item.animation,item.x,item.y,item.flags|128);
            for(const auto& shot:shots.objects)if(shot.fresh)birth(16,shot.animation,shot.x,shot.y,shot.flags);
            std::sort(births.begin(),births.end());for(const auto& row:births)std::cout<<row<<'\n';
            for(int phase=0;phase<2;++phase){
                if(phase==1 && !paused){
                    if(e.alive && !(e.flags&1024)){e.refreshBounds();e.advanceAnimation();}
                    for(auto& shot:shots.objects)if(shot.alive && !(shot.flags&1024))shot.advanceAnimation();
                    if(chest && chest->alive && !(chest->flags&1024) && !(chest->flags&32)){chest->refreshBounds();chest->advanceAnimation();}
                }
                std::cout<<"T\t"<<s<<'\t'<<f<<'\t'<<phase;e.write(std::cout);std::cout<<'\n';
                int index=0;for(const auto& shot:shots.objects)if(shot.alive){std::cout<<"S\t"<<s<<'\t'<<f<<'\t'<<phase<<'\t'<<index++;shot.write(std::cout);std::cout<<'\n';}
                if(chest){
                    std::cout<<"C\t"<<s<<'\t'<<f<<'\t'<<phase;
                    for(int n:{chest->fixedX,chest->fixedY,chest->x,chest->y,chest->vx,chest->vy,chest->ax,chest->ay,chest->flags,chest->animation,chest->pending,chest->step,chest->tick,chest->action,chest->timer,int(chest->grounded),int(chest->alive)})std::cout<<'\t'<<n;
                    for(int n:chest->collision)std::cout<<'\t'<<(phase==1 || (chest->flags&32)?n:0);std::cout<<'\n';
                }
                if(s=="melee")std::cout<<"P\t"<<s<<'\t'<<f<<'\t'<<phase<<'\t'<<meters.health<<'\t'<<damage.invulnerability<<'\n';
            }
            shots.objects.remove_if([](const auto& o){return !o.alive;});
        }
        std::cout<<"N\t"<<s<<'\t'<<random.nextInt()<<'\n';
        shots.clear();
    }
}
