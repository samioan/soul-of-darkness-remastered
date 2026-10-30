#include "combat_world.h"
#include <memory>
#include <iostream>
#include <sstream>
namespace {
int checks=0;void check(bool b,const char* text){++checks;if(!b)throw std::runtime_error(text);}
void u16(soul::Bytes& b,unsigned n){b.push_back(static_cast<std::uint8_t>(n));b.push_back(static_cast<std::uint8_t>(n>>8));}
void u32(soul::Bytes& b,unsigned n){u16(b,n&65535);u16(b,n>>16);}
soul::BSprite fixture(){soul::Bytes b;u16(b,991);u32(b,1|256|65536|16777216);u16(b,1);b.insert(b.end(),{2,2});u16(b,1);b.insert(b.end(),{0,0,0,0});u16(b,1);b.insert(b.end(),{247,197,18,59});u16(b,1);b.insert(b.end(),{1,0});u16(b,0);b.push_back(1);b.insert(b.end(),{247,197,18,59});u16(b,48*3);for(int i=0;i<48*3;++i)b.insert(b.end(),{0,2,0,0,0});u16(b,48);for(int i=0;i<48;++i){b.push_back(3);b.push_back(0);u16(b,i*3);}u16(b,21781);b.insert(b.end(),{1,2});u16(b,0);u16(b,0xffff);u16(b,1024);u16(b,1);b.push_back(0x55);return soul::BSprite::parse(soul::ByteView(b));}
}
int main(){try{
    const auto sprite=fixture();soul::LevelMaps maps;soul::TileGrid grid;grid.width=40;grid.height=20;grid.cells.resize(800);for(int i=400;i<800;++i)grid.cells[i]=24;maps.collision=grid;maps.layers[1].width=40;maps.layers[1].height=20;
    const soul::EnemyTarget far{700,700*256,{700,0,701,1},{700,0,701,1}};
    int cooldown=0;soul::ThrowAttachment holder(cooldown);soul::ThrownObjects objects(sprite,maps,1);auto& held=objects.spawnHeld(holder,100,150,false);
    check(holder.object==&held && held.hasHolder() && held.flags==272 && held.x==105 && held.y==119,"held birth establishes stable owner and original attachment flags/offsets");
    held.release(2048,-3584);check(!holder.object && !held.hasHolder() && held.flags==16 && held.action==102 && held.timer==800,"release detaches the holder and starts the flight timer");
    held.update(far);check(held.x==113 && held.y==105 && held.ax==-32 && held.ay==512,"flight integrates velocity before bounce acceleration");
    std::ostringstream before;held.write(before);held.update(far,{},{},true);std::ostringstream after;held.write(after);check(before.str()==after.str(),"pause preserves projectile state");
    auto& first=objects.spawnHeld(holder,100,150,false);auto& second=objects.spawnHeld(holder,100,150,true);check(!first.hasHolder() && !(first.flags&256) && holder.object==&second,"replacement clears the old attachment without destroying the old projectile");
    second.explode();for(int i=0;i<6 && second.alive;++i){second.update(far);second.advanceAnimation();}check(!second.alive && !holder.object && cooldown==1000,"held explosion clears the owner and applies original reacquisition cooldown");
    cooldown=55;objects.spawnHeld(holder,100,150,false);objects.clear();check(!holder.object && cooldown==55,"world cleanup detaches owners without a gameplay cooldown notification");
    auto temporary=std::make_unique<soul::ThrowAttachment>(cooldown);auto& orphan=objects.spawnHeld(*temporary,100,150,false);temporary.reset();check(orphan.alive && !orphan.hasHolder() && !(orphan.flags&256),"holder destruction leaves no dangling back-reference");
    orphan.update(far);orphan.advanceAnimation();soul::Canvas a(240,320,0xff101010),b(240,320,0xff101010);orphan.draw(a,0,0);orphan.draw(b,0,0);check(a.pixels()==b.pixels(),"projectile rendering is repeatable without animation mutation");
    auto& wide=objects.spawn(400,150);wide.update(far);wide.advanceAnimation();soul::Canvas narrow(240,320,0xff101010),expanded(569,320,0xff101010);wide.draw(narrow,0,0);wide.draw(expanded,0,0);check(narrow.pixels()==soul::Canvas(240,320,0xff101010).pixels() && expanded.pixels()!=soul::Canvas(569,320,0xff101010).pixels(),"widescreen reveals projectiles outside the portrait viewport");
    auto invalid=maps;invalid.collision->width=invalid.collision->height=invalid.layers[1].width=0;invalid.collision->cells.clear();bool rejected=false;try{soul::ThrownObject bad(sprite,invalid,1,100,100);}catch(const soul::FormatError&){rejected=true;}check(rejected,"empty collision maps fail before a projectile can enter its floor scan");
    soul::ActorRecord playerRecord{};playerRecord.count=6;playerRecord.parameters={0,186,700,199,0,0};soul::Player player(sprite,maps,playerRecord);player.x=300;player.fixedX=300*256;player.refreshBounds();player.flags=16384;soul::PlayerMeters meters;
    soul::ActorRecord er{};er.count=13;er.parameters={1,19,180,159,0,8,-10,-10,20,20,0,0,-1};const std::vector<soul::ActorRecord> records{er};const std::vector<int> stats{50,0,0};
    soul::CombatWorld world(sprite,sprite,maps,player,meters,records,stats);world.loadThrownObjects(sprite);auto& blast=world.thrownObjects->spawn(180,159);blast.explode();world.updatePlayer(0,0,0);world.finishPlayer();check(world.enemies[0].health==-450 && world.enemies[0].action==3 && world.kills==1,"world explosion dispatch applies 500 damage to a queued enemy");
    world.thrownObjects->spawnHeld(holder,100,150,false);world.detach();check(!world.thrownObjects && !holder.object,"world unload destroys projectile borrowers before assets and holders");
    soul::Player firePlayer(sprite,maps,playerRecord);firePlayer.flags=16384;soul::PlayerMeters fireMeters;soul::CombatWorld fireWorld(sprite,sprite,maps,firePlayer,fireMeters,{},stats);fireWorld.loadThrownObjects(sprite);const std::array<int,256> trig{};fireWorld.loadFireMagic(trig);
    auto& frozen=fireWorld.thrownObjects->spawn(700,177);frozen.action=103;frozen.timer=3000;frozen.flags|=16384;fireWorld.fireMagic->pulses.emplace_back(sprite,firePlayer,0,6);auto& pulse=fireWorld.fireMagic->pulses.back();pulse.animation=17;pulse.pending=-1;pulse.flags|=128;pulse.refreshBounds();
    fireWorld.updatePlayer(0,0,0);fireWorld.finishPlayer();check(frozen.action==104,"external fire can reflect a frozen projectile even when the pulse is hidden");
    fireWorld.detach();
    soul::ActorRecord r0{},r1{};r0.count=r1.count=12;r0.parameters={22,210,0,0,0,8,0,0,400,400,0,0};r1.parameters={22,211,400,0,0,8,0,0,400,400,0,0};
    soul::Player traveler(sprite,maps,playerRecord);traveler.x=100;traveler.refreshBounds();soul::PlayerMeters travelerMeters;soul::CombatWorld regions(sprite,sprite,maps,traveler,travelerMeters,{r0,r1},stats);regions.loadThrownObjects(sprite);regions.refreshRegionSelection();regions.applyRegionTransition();cooldown=55;regions.thrownObjects->spawnHeld(holder,100,150,false);traveler.x=600;traveler.refreshBounds();regions.refreshRegionSelection();regions.applyRegionTransition();check(regions.thrownObjects->objects.empty() && !holder.object && cooldown==55,"region cleanup removes dynamic type-16 owners and clears cached holder links");
    std::cout<<"PASS: "<<checks<<" thrown-object physics/ownership/world assertions\n";return 0;
}catch(const std::exception& e){std::cerr<<e.what()<<'\n';return 1;}}
