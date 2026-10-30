#pragma once
#include "resources.h"
namespace soul {
struct LevelDescriptor {
    int index,pack,stringTable,mode,background0,background1;
    int spritePack=2,fontPack=1,fontResource=0,statPack=18;
    int playerSprite=0,effectSprite=1,enemySprite=2,flyingSprite=3,beamSprite=10;
    int promptSprite=18,weaponSprite=25,abilitySprite=27,platformSprite=28;
    int pickupSprite=33,containerSprite=34,bridgeSprite=36;
    static LevelDescriptor get(int index);
    bool playable()const{return index==0;}
    bool openingScripts()const{return index==0;}
};
bool nativeLevelPlayable(int index);
}
