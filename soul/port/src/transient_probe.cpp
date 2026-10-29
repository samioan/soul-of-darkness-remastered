#include "player_probe.h"
#include "world_transients.h"
#include <iostream>
void emitTransientManifest(const std::filesystem::path&) {
    for(const std::string mode:{"change","same","death"})for(int type:{35,39,84,85})for(int uid:{-2,-1,19})for(int animation:{0,11,12,13})for(int flags:{0,32,1024,8192}) {
        const bool removed=mode=="change" && soul::removeTransientOnRegionChange(type,uid,animation,(flags&8192)!=0);
        std::cout<<"T\t"<<mode<<'\t'<<type<<'\t'<<uid<<'\t'<<animation<<'\t'<<flags<<'\t'<<int(removed)<<'\n';
    }
}
