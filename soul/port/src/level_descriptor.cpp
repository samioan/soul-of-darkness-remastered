#include "level_descriptor.h"
namespace soul {
LevelDescriptor LevelDescriptor::get(int index){
    if(index<0 || index>8)throw FormatError("Gameplay level must be 0..8");
    constexpr int modes[]{0,0,0,3,0,2,0,0,0},tiles[]{3,3,7,7,7,3,11,11,3};
    return {index,index+3,index+2,modes[index],tiles[index],tiles[index]+1};
}
bool nativeLevelPlayable(int index){return index>=0 && index<=8 && LevelDescriptor::get(index).playable();}
}
