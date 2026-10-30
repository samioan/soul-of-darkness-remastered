#pragma once
#include "combat.h"
#include "player.h"
namespace soul {
struct LightningLine {std::uint32_t color;int x0,y0,x1,y1;};
std::vector<LightningLine> lightningLines(JavaRandom&,int top,int height,int x,int width=240);
class EnergyBarrier {
public:
    EnergyBarrier(const BSprite&,const ActorRecord&);
    EnergyBarrier(BSprite&&,const ActorRecord&)=delete;
    EnergyBarrier(const BSprite&&,const ActorRecord&)=delete;
    void update(bool paused=false);void advanceAnimation();void refreshBounds();
    void enable();void clearRespawn();void beginRelease();void resetForRegionExit();
    ContactBody body()const;void applyContact(const ContactBody&);
    void advanceVisual(bool paused=false);void draw(Canvas&,int,int);
    void write(std::ostream&)const;
    int uid=0,x=0,y=0,flags=0,animation=0,pending=-1,step=0,tick=0,action=0,height=0,bucket=0;
    bool alive=true,separates=false,enabled=false,pushLocked=false,touched=false;
    std::array<int,4> collision{},attack{},visual{},limits{};
private:
    const BSprite& sprite_;SpriteRenderer renderer_;ActorRecord source_;
    JavaRandom visualRandom_;std::vector<LightningLine> lines_;
    bool ending()const;void request(int);void initialize(bool fresh=true);
};
}
