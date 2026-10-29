#pragma once
#include "checkpoint.h"
#include <optional>
namespace soul {
// The base stores checkpoint coordinates and the latest serialized player stats.
// uid == -1 is allowed only for completion without a previous checkpoint.
struct GameSave : CheckpointSnapshot {
    GameSave()=default;
    GameSave(const CheckpointSnapshot& snapshot):CheckpointSnapshot(snapshot){}
    int level=0,maxUnlockedLevel=0;
    bool levelCompleted=false;
    int finishLevel();
    int continueLevel()const;
    bool continuesCheckpoint()const{return !levelCompleted || level>=8;}
    void prepareContinueStats(int runtimeWeapon=0,bool runtimeIce=false,bool runtimeMagic=false);
};
// Version 2 desktop progress; version 1 checkpoint files remain readable.
std::optional<GameSave> loadGameSave(const std::filesystem::path&);
void writeGameSave(const std::filesystem::path&,const GameSave&);
void writeGameSave(const std::filesystem::path&,const CheckpointSnapshot&);
void clearGameSave(const std::filesystem::path&);
}
