#include "level_data.h"
#include "player_probe.h"
#include <iostream>
#include <iomanip>
#include <sstream>
void emitSelectedLevelManifest(const std::filesystem::path& root){
    // Emit each independently owned selected bundle.
    for(int index=0;index<9;++index){
        const auto d=soul::LevelData::load(root,index);const auto& l=d.level;const auto& m=d.scene.maps();
        std::cout<<"D\t"<<index<<'\t'<<l.pack<<'\t'<<l.stringTable<<'\t'<<l.mode<<'\t'<<l.background0<<'\t'<<l.background1<<'\t'<<m.width()<<'\t'<<m.height()<<'\t'<<m.collision->width<<'\t'<<m.collision->height<<'\n';
        std::cout<<"Q\t"<<index;for(bool v:d.requiredSprites)std::cout<<'\t'<<int(v);std::cout<<'\n';
        for(const auto& r:d.records){std::cout<<"R\t"<<index<<'\t'<<r.offset<<'\t'<<r.count;for(int v:r.parameters)std::cout<<'\t'<<v;std::cout<<'\n';}
        std::cout<<"K\t"<<index;for(int uid:d.checkpointUids)std::cout<<'\t'<<uid;std::cout<<'\n';
        for(const auto& s:d.scripts){std::cout<<"S\t"<<index<<'\t'<<s.uid;for(const auto& t:s.tracks){std::ostringstream hash;hash<<std::hex<<std::setfill('0')<<std::setw(16)<<soul::fingerprint(soul::ByteView(t.raw));std::cout<<'\t'<<t.raw.size()<<'\t'<<hash.str();}std::cout<<'\n';}
        soul::Player p(d.sprite(l.playerSprite),m,soul::playerRecord(d.records),index);
        std::cout<<"P\t"<<index<<'\t'<<p.uid<<'\t'<<p.x<<'\t'<<p.y<<'\t'<<p.flags<<'\t'<<p.animation<<'\n';
    }
    for(int index=8;index>=0;--index){const auto d=soul::LevelData::load(root,index);soul::Player p(d.sprite(d.level.playerSprite),d.scene.maps(),soul::playerRecord(d.records),index);if(p.level()!=index)throw soul::FormatError("Stale selected player");}
}
