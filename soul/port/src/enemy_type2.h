#pragma once
#include "enemy_type1.h"
namespace soul {
// Variant-data index 4 / behavior 0, and index 49 / behavior 6 (sine-wave flyer, needs the trigonometry table).
// Shares verified enemy lifecycle and sprite bounds with type 1; flying movement never invokes the ground collision solver.
class EnemyType2 : public EnemyType1 {
public:
    EnemyType2(const BSprite& sprite,const LevelMaps& maps,const ActorRecord& record,const std::vector<int>& stats,const std::array<int,256>* trig=nullptr);
    EnemyType2(BSprite&&,const LevelMaps&,const ActorRecord&,const std::vector<int>&)=delete;
    EnemyType2(const BSprite&&,const LevelMaps&,const ActorRecord&,const std::vector<int>&)=delete;
    EnemyType2(const BSprite&,LevelMaps&&,const ActorRecord&,const std::vector<int>&)=delete;
    EnemyType2(const BSprite&,const LevelMaps&&,const ActorRecord&,const std::vector<int>&)=delete;
    void update(const EnemyTarget&,bool paused=false,JavaRandom* random=nullptr,const PlayerMeters* meters=nullptr,CombatExchange* exchange=nullptr);
    int distance=0,originY=0,originX=0,direction=0,bucket=0;
    int behavior()const{return behavior_;}
    // Game.au: the active camera region. The behavior-6 handler tests it; the world refreshes it before each update.
    const std::array<int,4>* activeRegion=nullptr;
    // Actor.clampActorToActiveRegion for a type-2 actor: behavior 6 resets outside the region, others stop outward motion at its 12-pixel margin.
    void clampToRegion(const std::array<int,4>& region);
protected:
    void finishReinit() override;
private:
    void changeFlying(int);
    int sine(int degrees)const;
    int behavior_=0;
    const std::array<int,256>* trig_=nullptr;
};
}
