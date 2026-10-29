#pragma once
#include <algorithm>
#include <array>
#include <stdexcept>
namespace soul {
struct GameplayView {int x,y,left,top,right,bottom;};
// Presentation only. A wide camera reveals the active room, with centered margins
// when the complete room is narrower than the viewport. Adjacent packed rooms
// are separate spaces joined by type-59 passages.
inline GameplayView gameplayView(int playerX,int playerY,int width,int height,int mapWidth,int mapHeight,const std::array<int,4>* region){
    if(width<=0 || height<=0 || mapWidth<=0 || mapHeight<=0)throw std::invalid_argument("Invalid gameplay viewport");
    std::array<int,4> r{0,0,mapWidth,mapHeight};
    if(region && (*region)[0]<(*region)[2] && (*region)[1]<(*region)[3]){
        r={std::clamp((*region)[0],0,mapWidth),std::clamp((*region)[1],0,mapHeight),std::clamp((*region)[2],0,mapWidth),std::clamp((*region)[3],0,mapHeight)};
        if(r[0]>=r[2] || r[1]>=r[3])r={0,0,mapWidth,mapHeight};
    }
    const int x=r[2]-r[0]<width?r[0]-(width-(r[2]-r[0]))/2:std::clamp(playerX-width/2,r[0],r[2]-width);
    const int y=r[3]-r[1]<height?r[1]-(height-(r[3]-r[1]))/2:std::clamp(playerY-220,r[1],r[3]-height);
    return {x,y,std::max(0,r[0]-x),std::max(0,r[1]-y),std::min(width,r[2]-x),std::min(height,r[3]-y)};
}
}
