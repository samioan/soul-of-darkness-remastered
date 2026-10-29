#include "player_probe.h"
#include "melee_weapon.h"
#include <iostream>
void emitIceWeaponManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    const auto sprite=soul::BSprite::parse(pack.resource(0).value());
    const auto weaponSprite=soul::BSprite::parse(pack.resource(25).value());
    for(const std::string scenario:{"stand","combo","late_combo","held","walk","crouch","blocked","air","air_move","fall","paused","left","switch","switch-held","locked","switch-back","air-switch","priority"}) {
        soul::LevelMaps maps; soul::TileGrid grid; grid.width=40; grid.height=20; grid.cells.resize(800);
        for(int i=400;i<800;++i) grid.cells[i]=24;
        if(scenario=="blocked") for(int i=280;i<320;++i) grid.cells[i]=24;
        maps.collision=grid; maps.layers[1].width=40; maps.layers[1].height=20;
        soul::ActorRecord r{}; r.count=6; r.parameters[1]=186; r.parameters[2]=100; r.parameters[3]=scenario=="fall"?119:199;
        if(scenario=="blocked") r.parameters[4]=10;
        soul::Player player(sprite,maps,r);
        const bool switching=scenario.rfind("switch",0)==0 || scenario=="locked" || scenario=="air-switch" || scenario=="priority";
        player.activeWeapon=switching && scenario!="switch-back"?0:1;player.iceOwned=scenario!="locked" && scenario!="switch-back";
        r.parameters={28,191,407,308,0,16}; soul::MeleeWeapon weapon(weaponSprite,r);
        for(int frame=0;frame<80;++frame) {
            const bool paused=scenario=="paused" && frame>=6 && frame<16;
            unsigned pressed=frame==4?65568:0,held=0;
            if((scenario=="combo" && frame==6) || (scenario=="late_combo" && frame==11)) pressed=65568;
            if(scenario=="held" && frame>=4 && frame<30) held=65568;
            if((scenario=="walk" || scenario=="air_move") && frame<25) held=8256;
            if(scenario=="left" && frame<4) held=4112;
            if((scenario=="crouch" || scenario=="blocked") && frame<10) held=33024;
            if(scenario=="crouch" || scenario=="blocked") pressed=frame==6?65568:0;
            if(scenario=="air" || scenario=="air_move") pressed=frame==4?16388:frame==7?65568:0;
            if(scenario=="fall") pressed=frame==10?65568:0;
            if(switching){
                pressed=(frame==4 || (scenario!="switch-held" && (frame==6 || frame==10 || frame==12 || frame==16)))?512:0;
                held=scenario=="switch-held" && frame>=4 && frame<30?512:0;
                if(scenario=="air-switch" && frame==2)pressed=16384;if(scenario=="air-switch" && frame==7)pressed=65568;
                if(scenario=="priority" && frame==4)pressed=512|65568;
            }
            player.update(pressed,held,frame,paused);
            if(!paused) weapon.syncPlayer(player);
            weapon.update(player,paused);
            for(int phase=0;phase<2;++phase) {
                if(phase==1 && !paused) { player.advanceAnimation(); weapon.advanceAnimation(); }
                std::cout << "M\t" << scenario << '\t' << frame << '\t' << phase; player.write(std::cout);
                std::cout << '\t' << player.queuedCombo << '\t' << player.meleeRecovery << '\t' << player.meleeTimer;
                weapon.write(std::cout); int mask=0;
                for(int i=0;i<8;++i) { const int left=player.x-60+i*15; if(player.activeWeapon==0 && player.meleeHits({left,140,left+10,199})) mask|=1<<i; }
                std::cout << '\t' << mask << '\t' << player.activeWeapon << '\t' << player.weaponSwitchCooldown;mask=0;for(int i=0;i<8;++i){const int left=player.x-60+i*15;if(player.activeWeapon==1 && player.meleeHits({left,140,left+10,199}))mask|=1<<i;}std::cout << '\t' << mask << '\n';
            }
        }
    }
}
