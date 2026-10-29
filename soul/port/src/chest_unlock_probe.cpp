#include "player_probe.h"
#include "combat_world.h"
#include <iostream>
void emitChestUnlockManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto ps=soul::BSprite::parse(pack.resource(0).value()),es=soul::BSprite::parse(pack.resource(2).value()),fly=soul::BSprite::parse(pack.resource(3).value()),fx=soul::BSprite::parse(pack.resource(1).value()),cs=soul::BSprite::parse(pack.resource(34).value()),prompt=soul::BSprite::parse(pack.resource(18).value());
    for(const std::string s:{"normal","reverse","one-left","pause","hidden","frozen","left-target"}){
        soul::LevelMaps maps;soul::TileGrid grid;grid.width=40;grid.height=20;grid.cells.resize(800);for(int i=400;i<800;++i)grid.cells[i]=24;maps.collision=grid;maps.layers[1].width=40;maps.layers[1].height=20;
        soul::ActorRecord pr{},a{},b{},t{},x{},y{};pr.count=6;pr.parameters={0,186,100,199,0,0};a.count=b.count=7;t.count=19;x.count=y.count=13;
        a.parameters={52,56,180,199,0,static_cast<std::int16_t>(s=="left-target"?2217:2216),0};b.parameters={52,57,260,199,0,static_cast<std::int16_t>(s=="left-target"?2216:2217),-2};
        t.parameters={40,55,180,150,0,12,0,0,80,80,4,0,0,113,48,-1,-1,56,57};x.parameters={2,113,180,174,0,2056,-200,-200,400,400,4,0,-1};y.parameters={2,48,260,174,0,2057,-200,-200,400,400,4,0,-1};
        const std::vector<soul::ActorRecord> records{a,b,t,x,y};soul::Player p(ps,maps,pr);soul::PlayerMeters meters;soul::CombatWorld world(es,fx,maps,p,meters,records,{10,10,0});world.loadFlyingEnemies(fly,records,{8,10,0});
        std::vector<soul::Container> chests;chests.emplace_back(cs,prompt,maps,a);chests.emplace_back(cs,prompt,maps,b);world.bindContainers(chests);world.damage.invulnerability=60000;
        if(s=="hidden" || s=="frozen")world.removedEnemyTriggers[0].flags=140|(s=="hidden"?1024:16384);
        for(int f=0;f<40;++f){
            const bool paused=s=="pause" && f>=10 && f<16;world.updatePlayer(0,0,f,paused);
            const soul::EnemyTarget target{p.x,p.fixedX,p.collision,p.attack,p.fixedY};
            if(f==4)world.flyingEnemies[s=="reverse"?1:0].applyDamage(20,true,target,{},world.random);
            if(f==8 && s!="one-left")world.flyingEnemies[s=="reverse"?0:1].applyDamage(20,true,target,{},world.random);
            world.finishPlayer(paused);for(auto& c:chests)if(world.containerQueued(c.uid))c.update(p,0,paused,false,&world.random);world.finishContainerSources(chests,paused);
            for(int phase=0;phase<2;++phase){
                if(phase==1 && !paused){p.refreshBounds();p.advanceAnimation();for(auto& c:chests)c.advanceAnimation();world.advance();}
                std::cout<<"T\t"<<s<<'\t'<<f<<'\t'<<phase;world.removedEnemyTriggers[0].write(std::cout);for(const auto& c:chests)std::cout<<'\t'<<c.flags;std::cout<<'\n';
                for(std::size_t i=0;i<chests.size();++i){std::cout<<"C\t"<<s<<'-'<<i<<'\t'<<f<<'\t'<<phase;chests[i].write(std::cout);std::cout<<'\n';}
                int count=0;for(const auto& e:world.effects)if(e.alive && e.animation==28)++count;std::cout<<"V\t"<<s<<'\t'<<f<<'\t'<<phase<<'\t'<<count;
                for(const auto& e:world.effects)if(e.alive && e.animation==28)for(int n:{e.fixedX,e.fixedY,e.x,e.y,e.flags,e.step,e.tick,e.timer})std::cout<<'\t'<<n;std::cout<<'\n';
            }
        }
        std::cout<<"N\t"<<s<<'\t'<<world.random.nextInt()<<'\n';
    }
}
