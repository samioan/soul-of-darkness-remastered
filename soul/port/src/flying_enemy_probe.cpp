#include "player_probe.h"
#include "enemy_type2.h"
#include "pickup.h"
#include <iostream>
void emitFlyingEnemyManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));const auto sprite=soul::BSprite::parse(pack.resource(3).value()),ps=soul::BSprite::parse(pack.resource(0).value());
    for(const std::string s:{"idle","chase","attack","right","diagonal","zero","pause","hidden","frozen","clock-frozen","flag32","hurt","terminal","fade"}){
        soul::LevelMaps maps;soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int i=400;i<800;++i)g.cells[i]=24;maps.collision=g;maps.layers[1].width=40;maps.layers[1].height=20;
        soul::ActorRecord pr{},r{};pr.count=6;r.count=13;pr.parameters={0,186,static_cast<std::int16_t>(s=="idle"?700:s=="right"?300:100),199,0,0};
        const int flags=s=="hidden"?1032:s=="frozen"?16392:s=="clock-frozen"?72:s=="flag32"?40:s=="terminal"?2056:s=="fade"?136:8;
        r.parameters={2,113,static_cast<std::int16_t>(s=="zero"?100:s=="chase"?260:s=="attack"?150:180),static_cast<std::int16_t>(s=="diagonal"?119:174),0,static_cast<std::int16_t>(flags),-200,-200,400,400,4,0,-1};
        soul::Player p(ps,maps,pr);soul::PlayerMeters m;soul::PlayerDamage damage;damage.invulnerability=60000;soul::JavaRandom random;
        soul::EnemyType2 e(sprite,maps,r,{8,10,s=="fade"?1:0});int playerHits=0,enemyHits=0;std::vector<soul::CombatEffectSpawn> effects;soul::CombatExchange exchange{&p,&damage,&m,&random,&effects,&playerHits,&enemyHits};
        const soul::EnemyTarget target{p.x,p.fixedX,p.collision,p.attack,p.fixedY};
        for(int f=0;f<96;++f){
            const bool paused=s=="pause" && f>=6 && f<16;e.effects.clear();e.loot.clear();
            if((s=="hurt" || s=="terminal") && (f==10 || f==25))e.applyDamage(f==10?5:20,true,target,{},random);
            if(s=="fade" && f==50)e.applyDamage(20,true,target,{},random);
            if(e.alive)e.update(target,paused,&random,&m,&exchange);
            for(const auto& effect:e.effects)std::cout<<"L\t"<<s<<'\t'<<f<<"\t39\t"<<effect.animation<<'\t'<<effect.x<<'\t'<<effect.y<<'\t'<<effect.flags<<'\n';
            for(const auto& item:e.loot)std::cout<<"L\t"<<s<<'\t'<<f<<"\t35\t"<<item.animation<<'\t'<<item.x<<'\t'<<item.y<<'\t'<<(item.flags|128)<<'\n';
            for(int phase=0;phase<2;++phase){
                if(phase==1 && !paused && e.alive && !(e.flags&1024)){e.refreshBounds();e.advanceAnimation();}
                std::cout<<"F\t"<<s<<'\t'<<f<<'\t'<<phase;e.write(std::cout);std::cout<<'\t'<<e.distance<<'\t'<<e.originY<<'\t'<<e.respawnCount<<'\t'<<int(e.alive)<<'\n';
            }
        }
        std::cout<<"N\t"<<s<<'\t'<<random.nextInt()<<'\n';
    }
}
