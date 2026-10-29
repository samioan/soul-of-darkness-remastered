#include "player_probe.h"
#include "save_game.h"
#include <iostream>
namespace {
void stats(const soul::GameSave& s){const auto& m=s.meters;for(int n:{s.activeWeapon,m.maxHealth,m.health,m.healthFragments,m.maxMagic,m.magic,m.magicFragments,m.collected})std::cout<<'\t'<<n;std::cout<<'\t'<<std::boolalpha<<m.iceUnlocked<<'\t'<<m.magicUnlocked;}
}
void emitFinishManifest(const std::filesystem::path&){
    for(int level:{0,7,8})for(int unlocked:{level,9})for(bool checkpoint:{false,true}){
        soul::GameSave s;s.level=level;s.maxUnlockedLevel=unlocked;s.uid=checkpoint?11:-1;s.meters={23,80,17,90,12,2,1,true,true};s.activeWeapon=1;const int selected=s.finishLevel();
        std::cout<<"F\t"<<level<<'\t'<<unlocked<<'\t'<<std::boolalpha<<checkpoint<<'\t'<<s.level<<'\t'<<s.maxUnlockedLevel<<'\t'<<s.levelCompleted<<'\t'<<selected;stats(s);std::cout<<'\n';
        const int target=s.continueLevel();const bool restore=s.continuesCheckpoint();s.prepareContinueStats();
        std::cout<<"C\t"<<level<<'\t'<<unlocked<<'\t'<<std::boolalpha<<checkpoint<<'\t'<<target<<'\t'<<s.maxUnlockedLevel<<'\t'<<s.levelCompleted<<'\t'<<restore;stats(s);std::cout<<'\n';
    }
}
