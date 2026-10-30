#include "combat_world.h"
#include <iostream>
#include <sstream>
namespace {
int checks=0;void check(bool b,const char* text){++checks;if(!b)throw std::runtime_error(text);}
void u16(soul::Bytes& b,unsigned n){b.push_back(static_cast<std::uint8_t>(n));b.push_back(static_cast<std::uint8_t>(n>>8));}
void u32(soul::Bytes& b,unsigned n){u16(b,n&65535);u16(b,n>>16);}
soul::BSprite fixture(){soul::Bytes b;u16(b,991);u32(b,1|256|65536|16777216);u16(b,1);b.insert(b.end(),{2,2});u16(b,1);b.insert(b.end(),{0,0,0,0});u16(b,1);b.insert(b.end(),{247,197,18,59});u16(b,1);b.insert(b.end(),{1,0});u16(b,0);b.push_back(1);b.insert(b.end(),{247,197,18,59});u16(b,48*3);for(int i=0;i<48*3;++i)b.insert(b.end(),{0,2,0,0,0});u16(b,48);for(int i=0;i<48;++i){b.push_back(3);b.push_back(0);u16(b,i*3);}u16(b,21781);b.insert(b.end(),{1,2});u16(b,0);u16(b,0xffff);u16(b,1024);u16(b,1);b.push_back(0x55);return soul::BSprite::parse(soul::ByteView(b));}
soul::ActorRecord record(){soul::ActorRecord r{};r.count=7;r.parameters={80,32,180,100,14,8224,60};return r;}
}
int main(){try{
    const auto sprite=fixture();auto r=record();soul::EnergyBarrier b(sprite,r);
    check(b.flags==8352 && !b.enabled && b.collision==std::array<int,4>{171,41,189,160},"barrier starts hidden/disabled with extended sprite bounds");
    b.enable();b.update();check(b.flags==8192 && b.enabled && b.separates && b.pushLocked,"first handler enables priority collision without creating tile walls");
    auto p=soul::ContactBody{};p.type=0;p.x=172;p.y=159;p.localBounds={-9,-59,9,0};p.refreshBounds();auto body=b.body();soul::resolvePlayerContacts(p,{&body});check(p.x==162 && body.x==180,"barrier priority pass resolves full penetration against an immovable body");
    b.advanceVisual();soul::Canvas normal(240,320,0xff101010),repeat(240,320,0xff101010);std::ostringstream before;b.write(before);b.draw(normal,0,0);b.draw(repeat,0,0);std::ostringstream after;b.write(after);check(before.str()==after.str() && normal.checksum()==repeat.checksum(),"repeated renders preserve state and lightning geometry");
    b.beginRelease();check(b.action==101 && b.pending==31,"linked operation seven queues release pose");b.advanceAnimation();for(int i=0;i<6;++i){b.update();b.advanceAnimation();}check((b.flags&1024) && b.alive && !b.enabled,"respawn flag retains a disabled owner after release");
    b.resetForRegionExit();check(b.action==0 && !b.enabled && (b.flags&1024) && (b.flags&32),"region reset reloads original flags and disables contacts");
    b.flags&=~1024;b.enable();b.update();b.clearRespawn();b.beginRelease();b.advanceAnimation();for(int i=0;i<6;++i){b.update();b.advanceAnimation();}check(!b.alive && !b.enabled && b.collision==std::array<int,4>{},"cleared respawn flag permits final removal");
    soul::EnergyBarrier wide(sprite,r);wide.x=400;wide.enable();wide.update();wide.advanceVisual();soul::Canvas narrow(240,320,0xff101010),expanded(569,320,0xff101010);wide.draw(narrow,0,0);wide.draw(expanded,0,0);check(narrow.checksum()==soul::Canvas(240,320,0xff101010).checksum() && expanded.checksum()!=soul::Canvas(569,320,0xff101010).checksum(),"widescreen reveals the barrier beyond the original 240-wide view");
    soul::JavaRandom random(12345),copy(12345);soul::lightningLines(random,321,60,100);check(random.nextInt()==copy.nextInt(),"offscreen lightning consumes no generator RNG");
    soul::LevelMaps maps;soul::TileGrid grid;grid.width=40;grid.height=20;grid.cells.resize(800);for(int i=400;i<800;++i)grid.cells[i]=24;maps.collision=grid;maps.layers[1].width=40;maps.layers[1].height=20;
    soul::ActorRecord playerRecord{};playerRecord.count=6;playerRecord.parameters={0,186,172,199,0,0};soul::Player player(sprite,maps,playerRecord);soul::PlayerMeters meters;const std::vector<int> stats;auto placed=record();placed.parameters[3]=159;
    soul::CombatWorld world(sprite,sprite,maps,player,meters,{placed},stats);world.loadEnergyBarriers(sprite,{placed});world.energyBarriers[0].enable();world.updatePlayer(0,0,0);world.finishPlayer();world.advance();world.updatePlayer(0,0,1);world.finishPlayer();check(player.x==162,"world carries type-80 bodies through the player priority contact pass");
    check(world.updateQueueUids()==std::vector<int>{32},"barrier preserves placed queue identity");world.detach();check(world.energyBarriers.empty() && world.barrierTriggers.empty() && world.updateQueueUids().empty(),"unload removes barrier owners and cached trigger links");
    soul::Player quietPlayer(sprite,maps,playerRecord);quietPlayer.flags=16384;quietPlayer.x=100;quietPlayer.refreshBounds();soul::PlayerMeters quietMeters;
    soul::ActorRecord source{};source.count=13;source.parameters={1,352,180,159,0,16392,-100,-100,200,200,0,0,0};
    auto gate=record();gate.parameters[2]=300;
    soul::ActorRecord trigger{};trigger.count=19;trigger.parameters={40,354,170,100,0,8,0,0,40,100,1,0,0,352,-1,-1,32,-1,-1};
    const std::vector<soul::ActorRecord> encounter{source,trigger,gate};const std::vector<int> sourceStats{10,4,0};
    soul::CombatWorld linked(sprite,sprite,maps,quietPlayer,quietMeters,encounter,sourceStats);linked.loadEnergyBarriers(sprite,encounter);
    check(linked.barrierTriggers.size()==1 && linked.playerEnableTriggers.empty(),"barrier targets select their owner before the player-only trigger path");
    linked.updatePlayer(0,0,0);linked.finishPlayer();check(linked.energyBarriers[0].flags==8192 && !linked.energyBarriers[0].enabled,"linked enable changes flags without inserting a barrier into the frozen dispatch snapshot");
    linked.updatePlayer(0,0,1);linked.finishPlayer();check(linked.energyBarriers[0].enabled,"enabled barrier enters the following dispatch snapshot");
    linked.detach();check(linked.barrierTriggers.empty() && linked.energyBarriers.empty(),"world teardown clears cached source and target borrowers before owners");
    std::cout<<"PASS: "<<checks<<" energy barrier lifecycle/contact/render assertions\n";return 0;
}catch(const std::exception& e){std::cerr<<e.what()<<'\n';return 1;}}
