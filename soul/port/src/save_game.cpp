#include "save_game.h"
#define NOMINMAX
#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#include <fstream>
#include <sstream>
#include <set>
namespace soul {
namespace {
void validate(const CheckpointSnapshot& s){
    const auto& m=s.meters;
    if(s.uid<0 || s.uid>249 || s.x<0 || s.x>2048 || s.y<0 || s.y>2048 || s.activeWeapon<0 || s.activeWeapon>1 || (s.activeWeapon && !m.iceUnlocked) || m.maxHealth<1 || m.maxHealth>32767 || m.health<0 || m.health>m.maxHealth || m.maxMagic<1 || m.maxMagic>32767 || m.magic<0 || m.magic>m.maxMagic || m.collected<0 || m.healthFragments<0 || m.healthFragments>32767 || m.magicFragments<0 || m.magicFragments>32767 || s.removed.size()>250)throw FormatError("Invalid desktop save values");
    std::set<int> seen;
    for(const auto& r:s.removed)if(r.uid<0 || r.uid>32767 || r.flags<0 || r.flags>65535 || !seen.insert(r.uid).second)throw FormatError("Invalid desktop save removals");
}
std::uint32_t checksum(const std::string& bytes){std::uint32_t n=2166136261u;for(unsigned char c:bytes){n^=c;n*=16777619u;}return n;}
}
std::optional<CheckpointSnapshot> loadGameSave(const std::filesystem::path& path){
    if(!std::filesystem::exists(path))return std::nullopt;
    if(std::filesystem::file_size(path)>16384)throw FormatError("Desktop save too large");
    std::ifstream file(path,std::ios::binary);if(!file)throw FormatError("Cannot read desktop save");
    std::string tag;unsigned version=0;std::uint32_t hash=0;
    if(!(file>>tag>>version>>hash) || tag!="SODPC" || version!=1 || file.get()!='\n')throw FormatError("Unsupported desktop save header");
    const std::string payload((std::istreambuf_iterator<char>(file)),{});
    if(checksum(payload)!=hash)throw FormatError("Desktop save checksum mismatch");
    std::istringstream in(payload);CheckpointSnapshot s;auto& m=s.meters;int level=-1,face=-1,magic=-1,ice=-1,count=-1;
    if(!(in>>level>>s.uid>>s.x>>s.y>>s.activeWeapon>>face>>m.health>>m.maxHealth>>m.magic>>m.maxMagic>>m.collected>>m.healthFragments>>m.magicFragments>>magic>>ice>>count) || level!=0 || face<0 || face>1 || magic<0 || magic>1 || ice<0 || ice>1 || count<0 || count>250)throw FormatError("Invalid desktop save payload");
    s.facingLeft=face!=0;m.magicUnlocked=magic!=0;m.iceUnlocked=ice!=0;
    for(int i=0;i<count;++i){RemovedCheckpointActor r{};if(!(in>>r.uid>>r.flags))throw FormatError("Truncated desktop save");s.removed.push_back(r);}
    in>>std::ws;if(!in.eof())throw FormatError("Trailing desktop save data");validate(s);return s;
}
void writeGameSave(const std::filesystem::path& path,const CheckpointSnapshot& s){
    validate(s);const auto& m=s.meters;std::ostringstream data;
    data<<0<<' '<<s.uid<<' '<<s.x<<' '<<s.y<<' '<<s.activeWeapon<<' '<<s.facingLeft<<' '<<m.health<<' '<<m.maxHealth<<' '<<m.magic<<' '<<m.maxMagic<<' '<<m.collected<<' '<<m.healthFragments<<' '<<m.magicFragments<<' '<<m.magicUnlocked<<' '<<m.iceUnlocked<<' '<<s.removed.size()<<'\n';
    for(const auto& r:s.removed)data<<r.uid<<' '<<r.flags<<'\n';
    if(!path.parent_path().empty())std::filesystem::create_directories(path.parent_path());
    auto temp=path;temp+=L".tmp";
    {std::ofstream out(temp,std::ios::binary|std::ios::trunc);out<<"SODPC 1 "<<checksum(data.str())<<'\n'<<data.str();out.flush();if(!out)throw FormatError("Cannot write desktop save");out.close();if(!out)throw FormatError("Cannot close desktop save");}
    if(!MoveFileExW(temp.c_str(),path.c_str(),MOVEFILE_REPLACE_EXISTING|MOVEFILE_WRITE_THROUGH)){std::filesystem::remove(temp);throw FormatError("Cannot replace desktop save");}
}
void clearGameSave(const std::filesystem::path& path){std::filesystem::remove(path);}
}
