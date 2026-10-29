#pragma once
#include "player.h"
#include <functional>
namespace soul {
struct CameraSource {
    const int* x=nullptr;
    const int* y=nullptr;
    const bool* alive=nullptr;
    std::function<void()> refresh;
    explicit operator bool()const{return x && y && alive;}
};
struct ControlledCamera {
    bool active=false;
    int uid=-1,mode=0,x=0,y=0,targetX=0,targetY=0,vx=0,vy=0;
    std::array<int,4> bounds{};
    // Selected Game.updateCamera(2) path, without shakes or background scripts.
    void advance(const Player&,int mapWidth,int mapHeight);
    void write(std::ostream&)const;
};
class CameraController {
public:
    static bool supported(const ActorRecord&);
    explicit CameraController(const ActorRecord&);
    void update(Player&,ControlledCamera&,const std::function<CameraSource(int)>&,bool regionChanged=false,bool paused=false);
    int uid=0,x=0,y=0,flags=0,bucket=0,mode=0,sourceUid=-1,mask=0;
    bool alive=true;
    std::array<int,4> collision{};
    void write(std::ostream&)const;
    bool eligible(const Player&)const;
private:
    CameraSource source_;
    void remove(ControlledCamera&);
};
}
