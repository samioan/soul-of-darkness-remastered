#include "player_probe.h"
#include "enemy_type2.h"
#include "pickup.h"
#include "moving_platform.h"
#include <iostream>
// Type-2 behavior 6 (level-1 sine-wave flyers). Rows mirror tools/reference_checks/SineFlyerCheck.java.
namespace {
struct Case{const char* name;int flags;std::array<int,4> bounds;int enemyX,playerX,regionX,regionW;};
}
void emitSineFlyerManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));const auto sprite=soul::BSprite::parse(pack.resource(3).value()),ps=soul::BSprite::parse(pack.resource(0).value());
    const auto trig=soul::loadPlatformTrig(root);
    const std::vector<Case> cases{
        {"right",8,{0,-75,150,100},200,230,0,320},{"left",9,{-150,-75,150,60},500,470,300,400},{"left-wide",9,{-187,-75,150,60},500,400,0,700},
        {"attack",8,{0,-75,150,100},200,330,0,700},{"outside",8,{0,-75,150,100},200,700,0,320},{"far-region",8,{0,-75,150,100},200,230,0,700},
        {"hurt",8,{0,-75,150,100},200,230,0,700},{"terminal",2056,{0,-75,150,100},200,230,0,700},{"pause",8,{0,-75,150,100},200,230,0,700},
        {"hidden",1032,{0,-75,150,100},200,230,0,700},{"frozen",16392,{0,-75,150,100},200,230,0,700}};
    for(const auto& c:cases){
        const std::string s=c.name;
        soul::LevelMaps maps;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int i=400;i<800;++i)g.cells[i]=24;maps.collision=g;maps.layers[1].width=40;maps.layers[1].height=20;
        soul::ActorRecord pr{},r{};pr.count=6;r.count=13;pr.parameters={0,186,static_cast<std::int16_t>(c.playerX),199,0,0};
        r.parameters={2,113,static_cast<std::int16_t>(c.enemyX),174,0,static_cast<std::int16_t>(c.flags),static_cast<std::int16_t>(c.bounds[0]),static_cast<std::int16_t>(c.bounds[1]),static_cast<std::int16_t>(c.bounds[2]),static_cast<std::int16_t>(c.bounds[3]),49,0,-1};
        soul::Player p(ps,maps,pr);soul::PlayerMeters m;soul::PlayerDamage damage;damage.invulnerability=60000;soul::JavaRandom random;
        soul::EnemyType2 e(sprite,maps,r,{8,10,0},&trig);int playerHits=0,enemyHits=0;std::vector<soul::CombatEffectSpawn> effects;soul::CombatExchange exchange{&p,&damage,&m,&random,&effects,&playerHits,&enemyHits};
        const soul::EnemyTarget target{p.x,p.fixedX,p.collision,p.attack,p.fixedY};
        const std::array<int,4> region{c.regionX,0,c.regionX+c.regionW,400};bool selected=false;
        for(int f=0;f<120;++f){
            const bool paused=s=="pause" && f>=6 && f<16;e.effects.clear();e.loot.clear();
            if(!paused && !(f<0) && p.x>region[0] && p.x<=region[2] && p.y>region[1] && p.y<=region[3])selected=true;
            if((s=="hurt" || s=="terminal") && (f==12 || f==40))e.applyDamage(f==12?3:20,true,target,{},random);
            if(e.alive){
                const bool ran=!paused && !(e.flags&(1024|16384));e.activeRegion=selected?&region:nullptr;
                e.update(target,paused,&random,&m,&exchange);
                if(ran && e.alive && selected)e.clampToRegion(region);
            }
            for(const auto& effect:e.effects)std::cout<<"L\t"<<s<<'\t'<<f<<"\t39\t"<<effect.animation<<'\t'<<effect.x<<'\t'<<effect.y<<'\t'<<effect.flags<<'\n';
            for(const auto& item:e.loot)std::cout<<"L\t"<<s<<'\t'<<f<<"\t35\t"<<item.animation<<'\t'<<item.x<<'\t'<<item.y<<'\t'<<(item.flags|128)<<'\n';
            for(int phase=0;phase<2;++phase){
                if(phase==1 && !paused && e.alive && !(e.flags&1024)){e.refreshBounds();e.advanceAnimation();}
                std::cout<<"F\t"<<s<<'\t'<<f<<'\t'<<phase;e.write(std::cout);std::cout<<'\t'<<e.distance<<'\t'<<e.originY<<'\t'<<e.originX<<'\t'<<e.direction<<'\t'<<e.respawnCount<<'\t'<<int(e.alive)<<'\n';
            }
        }
        std::cout<<"N\t"<<s<<'\t'<<random.nextInt()<<'\n';
    }
}
#include "combat_world.h"
#include "level_data.h"
#include <algorithm>
// Controlled CombatWorld fixture on the real level-1 records: the player, region(s) containing flyer UID 181 and the flyer itself. The player is
// frozen and parked inside the flyer's custom bounds; this proves world wiring (active-region pointer, end-of-update clamp), not a level route.
void emitSineFlyerWorldManifest(const std::filesystem::path& root){
    using namespace soul;
    for(const std::string name:{"wave","dormant"}){
        auto data=LevelData::load(root,1);
        std::vector<ActorRecord> owned;const ActorRecord* flyer=nullptr;
        for(const auto& r:data.records)if(r.parameters[0]==2 && r.parameters[1]==181)flyer=&r;
        if(!flyer)throw FormatError("Missing level-1 sine flyer 181");
        const int fx=flyer->parameters[2],fy=flyer->parameters[3];
        for(const auto& r:data.records){
            const auto& p=r.parameters;
            if(p[0]==0 || (p[0]==2 && p[1]==181))owned.push_back(r);
            if(p[0]==22){const int x=static_cast<std::uint16_t>(p[2])+p[6],y=static_cast<std::uint16_t>(p[3])+p[7];if(fx>x && fx<=x+p[8] && fy>y && fy<=y+p[9])owned.push_back(r);}
        }
        Player player(data.sprite(data.level.playerSprite),data.scene.maps(),playerRecord(data.records),1);PlayerMeters meters;
        CombatWorld world(data.sprite(data.level.enemySprite),data.sprite(data.level.effectSprite),data.scene.maps(),player,meters,owned,data.stats(0));
        world.loadFlyingEnemies(data.sprite(data.level.flyingSprite),owned,data.stats(2),&data.trig);
        const auto& f=world.flyingEnemies.at(0);
        // Custom bounds are x+0..+150, y-75..+25 for UID 181; the frozen player stands inside them.
        player.x=fx+40;player.y=fy+20;if(name=="dormant"){player.x=fx-600;}
        player.fixedX=player.x*256;player.fixedY=player.y*256;player.flags=16384;player.refreshBounds();world.damage.invulnerability=60000;world.advance();
        int wave=-1,reset=-1,minY=fy,maxY=fy;int regions=0;for(const auto& r:owned)if(r.parameters[0]==22)++regions;
        for(int tick=0;tick<160;++tick){
            world.refreshRegionSelection();world.updatePlayer(0,0,tick,false);world.finishPlayer(false);world.advance();
            if((f.action==138 || f.action==139) && wave<0)wave=tick;
            if(wave>=0 && f.action==0 && (f.flags&1024) && reset<0)reset=tick;
            if(f.action==138 || f.action==139){minY=std::min(minY,f.y);maxY=std::max(maxY,f.y);}
        }
        std::cout<<"W\t"<<name<<'\t'<<regions<<'\t'<<wave<<'\t'<<reset<<'\t'<<minY-fy<<'\t'<<maxY-fy<<'\t'<<f.x-fx<<'\t'<<f.behavior()<<'\t'<<f.originX-fx<<'\n';
        world.detach();
    }
}
