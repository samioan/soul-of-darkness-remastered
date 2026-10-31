#include "combat_world.h"
#include "enemy_type3.h"
#include <iostream>
#include <memory>
#include <sstream>
namespace {
int checks=0;void check(bool b,const char* text){++checks;if(!b)throw std::runtime_error(text);}
void u16(soul::Bytes& b,unsigned n){b.push_back(static_cast<std::uint8_t>(n));b.push_back(static_cast<std::uint8_t>(n>>8));}
void u32(soul::Bytes& b,unsigned n){u16(b,n&65535);u16(b,n>>16);}
// 48 one-frame animations sharing one wide module.
soul::BSprite fixture(){soul::Bytes b;u16(b,991);u32(b,1|256|65536|16777216);u16(b,1);b.insert(b.end(),{2,2});u16(b,1);b.insert(b.end(),{0,0,0,0});u16(b,1);b.insert(b.end(),{247,197,18,59});u16(b,1);b.insert(b.end(),{1,0});u16(b,0);b.push_back(1);b.insert(b.end(),{247,197,18,59});u16(b,48*3);for(int i=0;i<48*3;++i)b.insert(b.end(),{0,2,0,0,0});u16(b,48);for(int i=0;i<48;++i){b.push_back(3);b.push_back(0);u16(b,i*3);}u16(b,21781);b.insert(b.end(),{1,2});u16(b,0);u16(b,0xffff);u16(b,1024);u16(b,1);b.push_back(0x55);return soul::BSprite::parse(soul::ByteView(b));}
soul::ActorRecord record(int variant,int flags,int x,int y,int stat=0,int link=-1){soul::ActorRecord r{};r.count=13;r.parameters={3,48,static_cast<std::int16_t>(x),static_cast<std::int16_t>(y),9,static_cast<std::int16_t>(flags),-125,-125,250,175,static_cast<std::int16_t>(variant),static_cast<std::int16_t>(stat),static_cast<std::int16_t>(link)};return r;}
soul::LevelMaps arena(){soul::LevelMaps maps;soul::TileGrid grid;grid.width=40;grid.height=20;grid.cells.resize(800);for(int i=400;i<800;++i)grid.cells[i]=24;maps.collision=grid;maps.layers[1].width=40;maps.layers[1].height=20;return maps;}
soul::EnemyTarget target(int x){return {x,x*256,{x-8,150,x+8,199},{10000,0,10001,1},199*256};}
soul::ActorRecord chestRecord(){soul::ActorRecord c{};c.count=7;c.parameters={52,353,300,69,0,0,1};return c;}
}
int main(){try{
    using namespace soul;
    const auto sprite=fixture();const auto maps=arena();const std::vector<int> stats{10,3,0, 20,3,1, 10,3,0, 10,3,0, 20,3,1};
    // Construction: ground snap keeps the record's fixed coordinates and rebuilds the activation box from the snapped y.
    EnemyType3 thrower(sprite,maps,record(9,9,500,100),stats,1);
    check(thrower.y==199 && thrower.fixedY==100*256 && thrower.activation[1]==199-125 && thrower.behavior()==1 && thrower.action==0,"construction snaps down to the floor and defers fixed coordinates");
    bool rejected=false;try{EnemyType3 paired(sprite,maps,record(9,9,500,100),stats,3);}catch(const FormatError&){rejected=true;}check(rejected,"paired-player level mode is rejected");
    rejected=false;try{EnemyType3 odd(sprite,maps,record(5,9,500,100),stats,1);}catch(const FormatError&){rejected=true;}check(rejected,"unshipped variant slots are rejected");
    // Held shot birth: the first tick of an active thrower spawns it, the parent loses flag 16 and the child waits for the next tick.
    int hits=0,enemyHits=0;JavaRandom random;PlayerMeters meters;PlayerDamage damage;damage.invulnerability=60000;
    ActorRecord pr{};pr.count=6;pr.parameters={0,186,380,199,0,0};Player player(sprite,maps,pr,1);std::vector<CombatEffectSpawn> effects;CombatExchange exchange{&player,&damage,&meters,&random,&effects,&hits,&enemyHits};
    ThrownObjects shots(sprite,maps,1);Type3Services services;services.shots=&shots;
    const auto near=target(380);thrower.update(near,false,&random,&meters,&exchange,services);
    check(shots.objects.size()==1 && shots.objects.front().fresh && thrower.attachedType()==16 && !(thrower.flags&16) && thrower.action==105,"active thrower spawns one held shot that is not yet queued");
    check(shots.objects.front().flags==273 && shots.objects.front().y==199-31,"held shot keeps the original 16|256 flags and -31 child offset");
    thrower.dispatchAttached(near,false,services);thrower.update(near,false,&random,&meters,&exchange,services);
    check(thrower.action==106 && thrower.throwX!=0 && shots.objects.size()==1,"aim accepts a throw and starts the release animation");
    for(int i=0;i<8 && thrower.action==106;++i){thrower.advanceAnimation();thrower.update(near,false,&random,&meters,&exchange,services);}
    check(thrower.action==107 && thrower.attachedType()==0 && thrower.timer==1800-83*0,"release starts the 1800 ms recovery");
    check(shots.objects.front().action==102 && shots.objects.front().vy==-3584 && !shots.objects.front().hasHolder(),"released shot is detached with the original upward speed");
    // Death while holding a shot releases it with zero velocity before cleanup.
    EnemyType3 victim(sprite,maps,record(9,9,500,100),stats,1);ThrownObjects victimShots(sprite,maps,1);Type3Services victimServices;victimServices.shots=&victimShots;
    victim.update(near,false,&random,&meters,&exchange,victimServices);auto& held=victimShots.objects.front();check(held.action==101 && victim.attachedType()==16,"victim holds a shot");
    victim.applyDamage(1000,true,near,{},random);check(victim.action==3 && held.action==102 && held.vx==0 && held.vy==0 && !held.hasHolder(),"lethal damage releases the held shot with no velocity");
    victim.update(near,false,&random,&meters,&exchange,victimServices);check(victim.action==0 && (victim.flags&1024) && victim.alive,"death cleanup resets and hides a respawnable actor");
    // The charger carries a bomb from its first tick.
    EnemyType3 charger(sprite,maps,record(7,9,300,100),stats,1);ThrownObjects bombs(sprite,maps,1);Type3Services bombServices;bombServices.shots=&bombs;
    const auto touching=target(300);charger.update(touching,false,&random,&meters,&exchange,bombServices);charger.dispatchAttached(touching,false,bombServices);
    check(charger.behavior()==0 && bombs.objects.size()==1,"charger carries a held bomb from its first tick");
    // Runner links its chest, carries it and drops it on death.
    Container chest(sprite,sprite,maps,chestRecord());
    EnemyType3 runner(sprite,maps,record(24,2056,300,100,0,353),stats,1);Type3Services runnerServices;runnerServices.shots=&shots;runnerServices.findChest=[&](int uid)->Container*{return uid==353 && chest.alive?&chest:nullptr;};
    const auto close=target(330);runner.update(close,false,&random,&meters,&exchange,runnerServices);runner.dispatchAttached(close,false,runnerServices);
    check(runner.attachedType()==52 && (chest.flags&(32|256))==(32|256) && runner.action==108 && runner.facingLeft && chest.x==runner.x-5 && chest.y==runner.y-31,"runner links, freezes and positions its chest");
    runner.applyDamage(1000,true,close,{},random);runner.update(close,false,&random,&meters,&exchange,runnerServices);
    check(runner.attachedType()==0 && !(chest.flags&(32|256)) && chest.action==103 && chest.timer==500,"runner death frees the chest into its falling state");
    // Removal clears links without a cooldown notification.
    EnemyType3 bombHolder(sprite,maps,record(9,9,500,100),stats,1);ThrownObjects removalShots(sprite,maps,1);Type3Services removalServices;removalServices.shots=&removalShots;
    bombHolder.update(near,false,&random,&meters,&exchange,removalServices);bombHolder.throwX=77;bombHolder.removeAttachments();
    check(bombHolder.attachedType()==0 && !(removalShots.objects.front().flags&256) && bombHolder.throwX==77,"removal detaches the child without cooldown notification");
    // World integration: type-3 actors are queued, born shots join the next tick and teardown detaches links.
    Player worldPlayer(sprite,maps,pr,1);worldPlayer.flags=16384;worldPlayer.x=380;worldPlayer.fixedX=380*256;worldPlayer.refreshBounds();PlayerMeters worldMeters;
    const std::vector<ActorRecord> records{record(9,9,500,100)};
    CombatWorld world(sprite,sprite,maps,worldPlayer,worldMeters,records,stats);world.loadThrownObjects(sprite);world.loadType3Enemies(sprite,records,stats);
    world.damage.invulnerability=60000;world.updatePlayer(0,0,0);world.finishPlayer();
    check(world.type3Enemies.size()==1 && world.updateQueueUids()==std::vector<int>{48} && world.thrownObjects->objects.size()==1 && world.type3Enemies[0].attachedType()==16,"world queues the actor and its held shot is attached");
    auto& late=world.thrownObjects->spawn(100,100);late.release(2560,0);late.fresh=true;const int bornX=late.x;
    world.advance();world.updatePlayer(0,0,1);world.finishPlayer();check(late.x==bornX && !late.fresh,"a shot born this tick is skipped by the ordinary shot queue");
    world.advance();world.updatePlayer(0,0,2);world.finishPlayer();check(late.x>bornX,"the born shot is dispatched from the next tick");
    bool loadedTwice=false;try{world.loadType3Enemies(sprite,records,stats);}catch(const FormatError&){loadedTwice=true;}check(loadedTwice,"type-3 enemies load once per world");
    world.detach();check(world.type3Enemies.empty() && !world.thrownObjects,"world unload destroys the actors before borrowed assets");
    std::cout<<"PASS: "<<checks<<" type-3 actor/world assertions\n";return 0;
}catch(const std::exception& e){std::cerr<<e.what()<<'\n';return 1;}}
