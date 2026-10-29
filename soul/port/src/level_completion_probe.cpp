#include "player_probe.h"
#include "level_completion.h"
#include "tile_world.h"
#include <algorithm>
#include <iostream>
void emitCompletionManifest(const std::filesystem::path& root){
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"2")),level=soul::ResourcePack::numeric(soul::readAssetFile(root/"3"));const auto ps=soul::BSprite::parse(pack.resource(0).value());auto scene=soul::TileScene::load(root,0);const auto records=soul::actorRecords(level.resource(0).value());const auto record=[&](int uid){return *std::find_if(records.begin(),records.end(),[&](const auto& r){return r.parameters[1]==uid;});};
    for(const std::string name:{"normal","edge","miss","point","outside","dead","zero-health","late-contact","pause","hidden","frozen","early","held","entry-pressed","boundary","down","cancel","softkey","five","wait","long-delay"}){
        soul::Player p(ps,scene.maps(),record(186));p.animation=name=="dead"?9:0;soul::ExitTrigger t(record(173));soul::LevelCompletion c;if(name=="long-delay")c.delay=5;
        for(int f=0;f<24;++f){const bool paused=name=="pause" && f<4;unsigned press=(name=="early"?f==2:name=="boundary"?f==10:name=="entry-pressed"?f==0:name!="held" && name!="wait" && f==16)?(name=="down"?32768:name=="cancel"?131072:name=="softkey"?262144:name=="five"?32:65536):0,held=name=="held"?65536:press;
            p.collision={1770,750,1790,780};if(name=="outside"||name=="late-contact" && f<4)p.collision={0,0,10,10};if(name=="edge")p.collision={1770,770,1780,780};if(name=="miss")p.collision={1769,770,1779,780};if(name=="point")p.collision={1781,771,1781,771};t.flags=136|((name=="hidden"||name=="frozen") && f<4?(name=="hidden"?1024:16384):0);
            const int before=c.state;if(t.update(p,c,paused))press=held=0;
            for(int phase=0;phase<2;++phase){if(phase==1){if(before==15)c.update(press);else if(c.active())c.entryFrame();}
                std::cout<<"T\t"<<name<<'\t'<<f<<'\t'<<phase;for(int n:{paused && c.state==8?14:c.state,c.bars,c.target,c.delay,c.starts,c.acknowledgements,t.flags})std::cout<<'\t'<<n;std::cout<<"\ttrue\t"<<press<<'\t'<<held<<'\n';
            }
        }
    }
}
