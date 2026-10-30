#include "timed_switch.h"
#include "combat_world.h"
#include <iostream>
namespace {
int checks=0;
void check(bool b,const char* text){++checks;if(!b)throw std::runtime_error(text);}
template<class F>void rejects(F f,const char* text){bool failed=false;try{f();}catch(const soul::FormatError&){failed=true;}check(failed,text);}
void u16(soul::Bytes& b,unsigned n){b.push_back(static_cast<std::uint8_t>(n));b.push_back(static_cast<std::uint8_t>(n>>8));}
void u32(soul::Bytes& b,unsigned n){u16(b,n&65535);u16(b,n>>16);}
soul::BSprite fixture(){
    soul::Bytes b;u16(b,991);u32(b,1|256|65536|16777216);
    u16(b,1);b.insert(b.end(),{2,2});u16(b,1);b.insert(b.end(),{0,0,0,0});u16(b,1);b.insert(b.end(),{247,197,18,59});
    u16(b,1);b.insert(b.end(),{1,0});u16(b,0);b.push_back(1);b.insert(b.end(),{247,197,18,59});
    u16(b,48*3);for(int i=0;i<48*3;++i)b.insert(b.end(),{0,2,0,0,0});
    u16(b,48);for(int i=0;i<48;++i){b.push_back(3);b.push_back(0);u16(b,i*3);}
    u16(b,21781);b.insert(b.end(),{1,2});u16(b,0);u16(b,0xffff);u16(b,1024);u16(b,1);b.push_back(0x55);
    return soul::BSprite::parse(soul::ByteView(b));
}
soul::ActorRecord record(){soul::ActorRecord r{};r.count=9;r.parameters={89,71,180,199,25,8,0,3000,72};return r;}
}
int main(){try{
    const auto sprite=fixture();auto r=record();soul::TimedSwitch sw(sprite,r);int target=102,resolutions=0;
    const auto resolve=[&](int uid)->const int*{++resolutions;return uid==72?&target:nullptr;};
    check(sw.action==0 && sw.variant==0 && sw.collision==std::array<int,4>{171,140,189,199},"switch construction uses sprite bounds despite flag eight");
    sw.update(true,resolve);check(sw.action==101 && sw.pending==26 && sw.timer==3000,"fire hit activates and loads timer");
    sw.update(false,resolve,true);check(sw.timer==3000 && resolutions==1,"pause prevents timer and lookup");
    sw.flags|=16384;sw.update(false,resolve);check(sw.timer==3000 && resolutions==1,"frozen switch skips handler");sw.flags&=~16384;
    sw.update(false,resolve);check(sw.timer==2917,"switch decrements by the 83 ms simulation tick");sw.update(true,resolve);check(sw.timer==3000,"repeated hit replenishes timer");
    r.parameters[7]=83;soul::TimedSwitch shortTimer(sprite,r);shortTimer.update(true,resolve);shortTimer.advanceAnimation();shortTimer.update(false,resolve);check(shortTimer.action==102 && shortTimer.pending==25 && shortTimer.timer==0,"expiry enters reset and queues idle pose");
    for(int i=0;i<6;++i)shortTimer.advanceAnimation();shortTimer.update(false,resolve);check(shortTimer.action==0,"reset waits for animation ending");
    r=record();r.parameters[6]=1;soul::TimedSwitch linked(sprite,r);linked.update(true,resolve);target=105;linked.update(false,resolve);check(linked.action==101,"mode one counts down while linked gate is open");
    target=0;linked.update(false,resolve);check(linked.action==0 && linked.pending==27,"closed linked gate cancels activation with warning pose");
    linked.update(true,resolve);rejects([&]{linked.update(false,[](int)->const int*{return nullptr;});},"active dependent switch rejects missing gate");
    r.parameters[7]=-1;soul::TimedSwitch infinite(sprite,r);infinite.update(true,resolve);infinite.update(false,[](int)->const int*{return nullptr;});check(infinite.action==101 && infinite.timer==-84,"infinite switch remains active without gate state");
    auto invalid=record();invalid.parameters[6]=2;rejects([&]{soul::TimedSwitch bad(sprite,invalid);},"unsupported switch mode rejected");
    soul::LevelMaps maps;soul::TileGrid grid;grid.width=40;grid.height=20;grid.cells.resize(800);maps.collision=grid;maps.layers[1].width=40;maps.layers[1].height=20;
    soul::ActorRecord pr{};pr.count=6;pr.parameters={0,186,100,199,0,0};soul::Player p(sprite,maps,pr);p.animation=13;p.attack=sw.collision;
    check(sw.struckBy(p),"fire sword can activate");p.activeWeapon=1;p.animation=19;check(!sw.struckBy(p),"ice sword cannot activate");
    std::array<int,256> trig{};soul::FireMagic fire(sprite,trig);fire.pulses.emplace_back(sprite,p,0,6);fire.pulses.back().collision=sw.collision;fire.pulses.back().flags=128;
    check(sw.struckBy(p,&fire),"present hidden fire pulse activates independently of equipped sword");fire.pulses.back().collision={180,199,180,199};check(!sw.struckBy(p,&fire),"point fire rectangle cannot activate");
    for(bool reversed:{false,true}){
        auto liveMap=maps;soul::Player player(sprite,liveMap,pr);player.flags=16384;soul::PlayerMeters meters;
        auto sr=record();soul::ActorRecord gate{};gate.count=9;gate.parameters={31,72,280,199,4,0,1,71,-1};
        const std::vector<soul::ActorRecord> records=reversed?std::vector<soul::ActorRecord>{gate,sr}:std::vector<soul::ActorRecord>{sr,gate};
        const std::vector<int> stats;soul::CombatWorld world(sprite,sprite,liveMap,player,meters,records,stats);world.loadGates(sprite,liveMap,records);world.loadTimedSwitches(sprite,records);
        world.updatePlayer(0,0,0);player.animation=13;player.attack=world.timedSwitches[0].collision;world.finishPlayer();
        check(world.timedSwitches[0].action==101 && world.gates.front().action==(reversed?0:102),"gate observes switch state in shipped record dispatch order");
        check(world.updateQueueUids()==(reversed?std::vector<int>{72,71}:std::vector<int>{71,72}),"timed switch occupies its original queue position");
        world.updatePlayer(0,0,1);player.animation=0;world.finishPlayer();check(world.gates.front().action==102,"later gate dispatch resolves stable switch action owner");
        world.advance();world.detach();check(world.timedSwitches.empty() && world.gates.empty() && world.updateQueueUids().empty(),"unload releases gate links and switch owners");
    }
    std::cout<<"PASS: "<<checks<<" timed switch attack/timer/link/world assertions\n";return 0;
}catch(const std::exception& e){std::cerr<<e.what()<<'\n';return 1;}}
