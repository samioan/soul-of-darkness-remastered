#include "player_probe.h"
#include "placed_prompt.h"
#include "tile_world.h"
#include <algorithm>
#include <iostream>
void emitPromptManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2")),level=soul::ResourcePack::numeric(soul::readAssetFile(root/"3"));const auto ps=soul::BSprite::parse(pack.resource(0).value()),sprite=soul::BSprite::parse(pack.resource(18).value());auto scene=soul::TileScene::load(root,0);const auto records=soul::actorRecords(level.resource(0).value());std::vector<soul::ActorRecord> rows;for(const auto& r:records)if(r.parameters[0]==21)rows.push_back(r);
    std::vector<std::string> names;for(std::size_t i=0;i<rows.size();++i)names.push_back("placed"+std::to_string(i));for(const auto* n:{"edge","point","miss","pause","hidden","frozen","delayed","magic0","magic1","magic-miss","magic-point","switch","switch-idle"})names.emplace_back(n);
    const auto level1=soul::ResourcePack::numeric(soul::readAssetFile(root/"4"));auto scene1=soul::TileScene::load(root,1);const auto records1=soul::actorRecords(level1.resource(0).value());
    for(int uid:{495,499,75,85,84})for(const auto* m:{"overlap","miss","pause","hidden","cast"})names.push_back("level1-"+std::to_string(uid)+"-"+m);
    for(const auto& name:names){const bool l1=name.rfind("level1-",0)==0;const int l1Uid=l1?std::stoi(name.substr(7)):0;const std::string mode=l1?name.substr(name.find_last_of('-')+1):name;
        auto r=l1?*std::find_if(records1.begin(),records1.end(),[&](const auto& x){return x.parameters[0]==21 && x.parameters[1]==l1Uid;}):rows.at(name.rfind("placed",0)==0?std::stoi(name.substr(6)):0);const int baseAnimation=r.parameters[4];const bool magic=name.rfind("magic",0)==0,sw=name.rfind("switch",0)==0;if(magic)r.parameters[4]=name=="magic1"?18:17;if(sw)r.parameters[4]=34;soul::PlacedPrompt e(r);if(mode=="hidden")e.flags|=1024;if(name=="frozen")e.flags|=16384;if(name=="delayed")e.delay=1;
        soul::Player player(ps,l1?scene1.maps():scene.maps(),soul::playerRecord(l1?records1:records),l1?1:0);const std::array<int,4> view{e.x-120,e.y-160,e.x+120,e.y+160};
        for(int frame=0;frame<16;++frame){const auto box=e.collision;player.collision=frame>=2 && frame<=6?std::array<int,4>{box[0]+1,box[1]+1,box[0]+2,box[1]+2}:std::array<int,4>{-1000,-1000,-999,-999};
            if(name=="edge" && frame>=2 && frame<=6)player.collision={box[0]-2,box[1],box[0],box[1]+1};if((name=="point" || name=="magic-point") && frame>=2 && frame<=6)player.collision={box[0]+1,box[1]+1,box[0]+1,box[1]+1};if(mode=="miss")player.collision={-1000,-1000,-999,-999};
            int switched=frame==4 && name=="switch"?1:0;const int spell=frame==4 && magic?(name=="magic1"||name=="magic-miss"?1:0):frame==4 && mode=="cast"?(baseAnimation==18?1:0):-1;e.update(player,spell,switched,mode=="pause");
            for(int phase=0;phase<2;++phase){if(phase && mode!="pause" && mode!="hidden")e.advanceAnimation(sprite);std::cout<<"P\t"<<name<<'\t'<<frame<<'\t'<<phase;for(int n:{e.status,e.delay,e.x,e.y,e.animation,e.step,e.tick,e.flags,int(e.alive),switched,int(e.visible(player,view))})std::cout<<'\t'<<n;std::cout<<'\n';}
        }
    }
}
