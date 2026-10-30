#include "level_data.h"
#include "moving_platform.h"
#include <set>
namespace soul {
namespace {
ByteView required(const ResourcePack& pack,int id){const auto r=pack.resource(id);if(!r)throw FormatError("Missing selected-level resource");return *r;}
}
std::vector<ActorRecord> loadLevelRecords(const std::filesystem::path& root,const LevelDescriptor& level){
    const auto pack=ResourcePack::numeric(readAssetFile(root/std::to_string(level.pack)));
    return actorRecords(required(pack,0));
}
std::array<bool,53> requiredLevelSprites(const std::vector<ActorRecord>& records,int index){
    LevelDescriptor::get(index);
    // Original Actor.spriteIdByType[0..100]; all shipped placed types are in this range.
    constexpr int ids[]{0,2,3,3,-1,-1,-1,-1,-1,-1,10,-1,-1,12,14,-1,-1,-1,-1,-1,-1,18,-1,-1,-1,-1,-1,-1,25,-1,36,28,28,28,28,33,28,29,28,1,-1,-1,-1,36,28,36,41,36,27,-1,28,28,34,28,28,34,28,36,28,-1,28,40,41,41,41,41,41,-1,-1,45,47,27,28,48,-1,1,48,-1,-1,49,1,-1,28,-1,-1,-1,1,-1,-1,28,15,1,-1,-1,-1,28,0,0,-1,-1,15};
    std::array<bool,53> flags{};
    const auto add=[&](int id){if(id>=0)flags.at(id)=true;};
    for(const auto& r:records){const int type=r.parameters[0];if(type<0 || type>100)throw FormatError("Unsupported placed sprite type");add(ids[type]);}
    for(int id:{1,33,18,40,4})add(id);
    if(flags[ids[73]])add(30);
    if(flags[ids[13]])add(13);
    if(flags[ids[90]]){add(16);add(17);}
    if(index==0)add(27);
    if(index<8)add(28);
    if(index==2 || index==4)add(41);
    if(index==5 || index==6 || index==7 || index==8){add(48);add(30);}
    return flags;
}
LevelData LevelData::load(const std::filesystem::path& root,int index){
    const auto level=LevelDescriptor::get(index);
    LevelData result(level,TileScene::load(root,index));
    const auto pack=ResourcePack::numeric(readAssetFile(root/std::to_string(level.pack)));
    result.records=actorRecords(required(pack,0));playerRecord(result.records);
    std::set<int> uids;
    for(const auto& r:result.records){if(!uids.insert(r.parameters[1]).second)throw FormatError("Duplicate selected-level UID");if(r.parameters[0]==23)result.checkpointUids.push_back(r.parameters[1]);}
    const auto raw=required(pack,1);result.scriptBytes.assign(raw.data(),raw.data()+raw.size());result.scripts=decodeScripts(ByteView(result.scriptBytes));
    result.requiredSprites=requiredLevelSprites(result.records,index);
    const auto sprites=ResourcePack::numeric(readAssetFile(root/std::to_string(level.spritePack)));
    for(int id=0;id<53;++id)if(result.requiredSprites[id] || id==35)result.sprites_.emplace(id,BSprite::parse(required(sprites,id)));
    const auto fonts=ResourcePack::numeric(readAssetFile(root/std::to_string(level.fontPack)));
    result.font=BSprite::parse(required(fonts,level.fontResource));
    const auto stats=ResourcePack::numeric(readAssetFile(root/std::to_string(level.statPack)));
    BinaryReader r(required(stats,0));const int count=r.i32be();if(count!=20)throw FormatError("Invalid selected-level stat table count");
    for(int i=0;i<count;++i){const int size=r.i32be();if(size<0 || size>10000)throw FormatError("Invalid selected-level stat row");std::vector<int> values;for(int n=0;n<size;++n)values.push_back(r.i32be());result.stats_.push_back(std::move(values));}r.done();
    result.trig=loadPlatformTrig(root);
    return result;
}
const BSprite& LevelData::sprite(int id)const{const auto p=sprites_.find(id);if(p==sprites_.end())throw FormatError("Sprite not selected for this level");return p->second;}
const std::vector<int>& LevelData::stats(int table)const{if(table<0 || std::size_t(table)>=stats_.size())throw FormatError("Stat table not selected");return stats_[table];}
}
