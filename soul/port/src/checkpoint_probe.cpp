#include "player_probe.h"
#include "checkpoint.h"
#include "container.h"
#include "tile_world.h"
#include <iostream>
#include <optional>
#include <algorithm>
void emitCheckpointManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2")),level=soul::ResourcePack::numeric(soul::readAssetFile(root/"3"));
    const auto ps=soul::BSprite::parse(pack.resource(0).value()),cs=soul::BSprite::parse(pack.resource(34).value()),prompt=soul::BSprite::parse(pack.resource(18).value());const auto records=soul::actorRecords(level.resource(0).value());auto scene=soul::TileScene::load(root,0);
    for(const std::string name:{"cp0","cp1","cp2","cp3","cp4","cp5","left","edge-left","edge-right","edge-top","edge-bottom","miss","point","paused","hidden","frozen","pickup","chest","enemy","persistent-enemy","transient-pickup","before-after","second"}){
        std::vector<soul::Checkpoint> cps;for(const auto& r:records)if(r.parameters[0]==23)cps.emplace_back(r);
        const int index=name.rfind("cp",0)==0?std::stoi(name.substr(2)):0;auto& cp=cps.at(index);const auto box=cp.collision;
        soul::Player p(ps,scene.maps(),soul::playerRecord(records));soul::PlayerMeters m;m.maxHealth=80;m.health=23;m.maxMagic=90;m.magic=17;m.healthFragments=2;m.magicFragments=1;m.iceUnlocked=m.magicUnlocked=true;p.activeWeapon=1;p.x=box[0]+5;p.y=box[1]+40;p.facingLeft=name=="left";p.refreshBounds();
        p.collision={box[0]+1,box[1]+1,box[0]+2,box[1]+2};
        if(name=="edge-left")p.collision={box[0]-2,box[1],box[0],box[1]+1};if(name=="edge-right")p.collision={box[2],box[1],box[2]+2,box[1]+1};if(name=="edge-top")p.collision={box[0],box[1]-2,box[0]+1,box[1]};if(name=="edge-bottom")p.collision={box[0],box[3],box[0]+1,box[3]+2};
        if(name=="miss")p.collision={box[0]-3,box[1],box[0]-1,box[1]+1};if(name=="point")p.collision={box[0],box[1],box[0],box[1]};if(name=="hidden" || name=="frozen")cp.flags=8|(name=="hidden"?1024:16384);
        std::vector<soul::RemovedCheckpointActor> removed;
        const auto remove=[&](int uid,bool persistent=false,bool transientActor=false){const auto r=std::find_if(records.begin(),records.end(),[&](const auto& r){return r.parameters[1]==uid;});int flags=r->parameters[5];if(r->parameters[0]==52){const soul::Container c(cs,prompt,scene.maps(),*r);flags=c.flags;}if(persistent)flags|=512;if(transientActor)flags|=8192;removed.push_back({uid,flags});};
        if(name=="pickup" || name=="before-after" || name=="second" || name=="transient-pickup")remove(6,false,name=="transient-pickup");if(name=="chest")remove(47);if(name=="enemy" || name=="persistent-enemy")remove(19,name=="persistent-enemy");
        std::optional<soul::CheckpointSnapshot> saved;if(cp.update(p,name=="paused")){removed.push_back({cp.uid,cp.flags});saved=soul::CheckpointSnapshot::capture(cp,p,m,removed);}
        if(name=="before-after"){remove(18);remove(47);}
        if(name=="second"){remove(18);auto& next=cps[1];p.x=next.collision[0]+5;p.y=next.collision[1]+40;p.collision=next.collision;if(next.update(p)){removed.push_back({next.uid,next.flags});saved=soul::CheckpointSnapshot::capture(next,p,m,removed);}}
        for(int phase=0;phase<(saved?3:1);++phase){if(phase)saved->restore(p,m);
            std::cout<<"C\t"<<name<<'\t'<<phase<<'\t'<<(saved?saved->uid+1:0)<<'\t'<<p.x<<'\t'<<p.y<<'\t'<<int(p.facingLeft)<<'\t'<<p.activeWeapon;
            for(int n:{m.health,m.maxHealth,m.magic,m.maxMagic,m.healthFragments,m.magicFragments,int(m.iceUnlocked),int(m.magicUnlocked)})std::cout<<'\t'<<n;std::cout<<'\n';
            for(const auto& r:records)if(r.parameters[0]!=0){const bool missing=phase?saved->skips(r):std::any_of(removed.begin(),removed.end(),[&](const auto& a){return a.uid==r.parameters[1] && !(a.flags&8192);});std::cout<<"A\t"<<name<<'\t'<<phase<<'\t'<<r.parameters[1]<<'\t'<<int(!missing)<<'\n';}
        }
    }
}
