#include "player_probe.h"
#include "combat_world.h"
#include <iostream>
namespace {
void enemyRow(const soul::EnemyType1& e,const std::string& kind,const std::string& s,int frame,int phase) {
    std::cout << kind << "\t" << s << '\t' << frame << '\t' << phase;
    for(int n:{e.fixedX,e.fixedY,e.x,e.y,e.vx,e.vy,e.ax,e.ay,e.flags,e.animation,e.pending,e.step,e.tick,e.action})std::cout << '\t' << n;
    for(bool b:{e.grounded,e.ceiling,e.wallLeft,e.wallRight})std::cout << '\t' << (b?"true":"false");
    for(const auto& r:{e.collision,e.attack,e.visual})for(int n:r)std::cout << '\t' << n;
    std::cout << '\t' << e.health << '\t' << e.damageCooldown << '\n';
}
void row(const soul::Player& p,const soul::EnemyType1& e,const soul::CombatWorld& w,const soul::PlayerMeters& m,const std::string& s,int frame,int phase) {
    std::cout << "P\t" << s << '\t' << frame << '\t' << phase; p.write(std::cout);
    std::cout << '\t' << m.health << '\t' << w.damage.invulnerability << '\t' << (p.deathComplete?12:8) << '\n';
    enemyRow(e,"E",s,frame,phase);
}
void birthEffects(const soul::CombatWorld& w,const std::string& s,int frame,int phase) {
    std::cout << "H\t" << s << '\t' << frame << '\t' << phase << '\t' << w.effects.size();
    for(const auto& e:w.effects)for(int n:{e.animation,e.flags,e.fixedX,e.fixedY,e.x,e.y,e.step,e.tick})std::cout << '\t' << n;
    std::cout << '\n';
}
}
void emitLiveCombatManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto ps=soul::BSprite::parse(pack.resource(0).value()),es=soul::BSprite::parse(pack.resource(2).value()),fx=soul::BSprite::parse(pack.resource(1).value()),items=soul::BSprite::parse(pack.resource(33).value());
    for(const std::string s:{"fight","contact","left","lethal","combo","air","pair","hazard2","hazard_head","hazard_mixed","hazard3","hazard_inv","hazard_lethal"}) {
        soul::LevelMaps maps; soul::TileGrid g;g.width=40;g.height=20;g.cells.resize(800);for(int i=400;i<800;++i)g.cells[i]=24;maps.collision=g;maps.layers[1].width=40;maps.layers[1].height=20;
        const bool hazard=s.rfind("hazard",0)==0;
        if(hazard) {
            const int row=s=="hazard_head" || s=="hazard_mixed"?7:9;
            const int value=s=="hazard2" || row==7?2:3;
            for(int i=row*40;i<(row+1)*40;++i)maps.collision->cells[i]=static_cast<std::uint8_t>(value);
            if(s=="hazard_mixed")for(int i=360;i<400;++i)maps.collision->cells[i]=3;
        }
        soul::ActorRecord pr{};pr.count=6;pr.parameters={0,186,static_cast<std::int16_t>(s=="left"?240:100),199,0,0};
        soul::ActorRecord er{};er.count=13;er.parameters={1,19,180,199,0,9,-100,-125,200,125,0,0,-1};
        soul::Player p(ps,maps,pr);soul::PlayerMeters m;if(s=="lethal")m.health=10;
        if(s=="hazard_lethal")m.health=1;
        std::vector<soul::ActorRecord> records{er};if(s=="pair"){auto second=er;second.parameters[1]=20;second.parameters[2]=220;records.push_back(second);}
        soul::CombatWorld w(es,fx,maps,p,m,records,{10,10,0});std::vector<soul::Pickup> loot;
        if(s=="hazard_inv")w.damage.invulnerability=600;
        for(int frame=0;frame<120;++frame) {
            const unsigned held=!hazard && s!="contact" && s!="lethal" && frame<12?(s=="left"?4112:8256):0;
            unsigned pressed=!hazard && s!="contact" && s!="lethal" && frame>=8 && frame<60 && frame%6==2?65568:0;
            if(s=="air" && frame==4)pressed=16388;if(s=="combo" && frame==10)pressed=65568;
            const auto previous=loot.size();w.updatePlayer(pressed,held,frame);w.finishPlayer();
            for(const auto& drop:w.loot)loot.push_back(soul::Pickup::enemyDrop(items,drop.animation,drop.x,drop.y,drop.flags));
            if(p.animation!=9)for(std::size_t i=0;i<previous;++i)loot[i].update(p,m);
            row(p,w.enemies[0],w,m,s,frame,0);if(s=="pair")enemyRow(w.enemies[1],"E2",s,frame,0);
            if(hazard && frame==0)birthEffects(w,s,frame,0);
            p.refreshBounds();p.advanceAnimation();w.advance();for(auto& item:loot)if(item.alive){item.refreshBounds();item.advanceAnimation();}
            row(p,w.enemies[0],w,m,s,frame,1);if(s=="pair")enemyRow(w.enemies[1],"E2",s,frame,1);
            if(hazard && frame==0)birthEffects(w,s,frame,1);
            if(p.deathComplete)break;
        }
    }
}
