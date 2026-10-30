#pragma once
#include "level_descriptor.h"
#include "actor_script.h"
#include <map>
namespace soul {
// Owned data for one selected level. No borrowed resource view survives load().
class LevelData {
public:
    static LevelData load(const std::filesystem::path&,int level);
    LevelData(const LevelData&)=delete;LevelData& operator=(const LevelData&)=delete;
    LevelData(LevelData&&)=default;LevelData& operator=(LevelData&&)=delete;
    const LevelDescriptor level;
    TileScene scene;
    std::vector<ActorRecord> records;
    Bytes scriptBytes;
    std::vector<ScriptData> scripts;
    std::vector<int> checkpointUids;
    std::array<bool,53> requiredSprites{};
    BSprite font;
    std::array<int,256> trig{};
    const BSprite& sprite(int id)const;
    const std::vector<int>& stats(int table)const;
private:
    LevelData(LevelDescriptor descriptor,TileScene map):level(descriptor),scene(std::move(map)){}
    std::map<int,BSprite> sprites_;
    std::vector<std::vector<int>> stats_;
};
std::vector<ActorRecord> loadLevelRecords(const std::filesystem::path&,const LevelDescriptor&);
std::array<bool,53> requiredLevelSprites(const std::vector<ActorRecord>&,int level);
}
