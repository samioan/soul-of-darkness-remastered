#include "player_probe.h"
#include "removed_script_trigger.h"
#include <iostream>
namespace {
std::uint32_t crc(const soul::Bytes& b){std::uint32_t n=0xffffffff;for(auto v:b){n^=v;for(int j=0;j<8;++j)n=(n>>1)^((n&1)?0xedb88320:0);}return n^0xffffffff;}
}
void emitRemovedGateTriggerManifest(const std::filesystem::path& root) {
    const auto sprites=soul::ResourcePack::numeric(soul::readAssetFile(root/"2")),pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"3"));const auto sprite=soul::BSprite::parse(sprites.resource(28).value());
    for(const std::string s:{"normal","missing","early-remove","index-zero","pause","hidden","frozen","source-hidden"}) {
        soul::ScriptData data;for(auto& script:soul::decodeScripts(pack.resource(1).value()))if(script.uid==165)data=std::move(script);soul::GateScript script(std::move(data));
        auto maps=soul::LevelMaps::load(pack,true);soul::ActorRecord gr{},tr{};gr.count=9;gr.parameters={31,154,1468,1500,4,0,4,-1,-1};soul::LinkedGate gate(sprite,maps,gr);
        tr.count=19;tr.parameters={40,166,1381,1402,0,12,0,0,80,80,3,1,0,57,-1,-1,165,-1,-1};soul::RemovedScriptTrigger trigger(tr);
        if(s=="hidden" || s=="frozen")trigger.flags=140|(s=="hidden"?1024:16384);
        const int sourceIndex=s=="index-zero"?0:5;std::vector<bool> removed(200);bool sourceAlive=s!="missing" && s!="early-remove";if(s=="early-remove")removed[sourceIndex]=true;
        for(int f=0;f<48;++f) {
            const bool paused=s=="pause" && f>=7 && f<13;
            script.update([&](int uid)->soul::LinkedGate*{return uid==154?&gate:nullptr;},1280,1280,paused);
            if(f==4 && s!="missing" && s!="early-remove"){sourceAlive=false;removed[sourceIndex]=true;}
            gate.update([](int)->const int*{return nullptr;},paused);trigger.update(sourceAlive?std::optional<int>(sourceIndex):std::nullopt,removed,script,paused);
            for(int phase=0;phase<2;++phase) {
                if(phase==1 && !paused)gate.advanceAnimation();
                std::cout<<"T\t"<<s<<'\t'<<f<<'\t'<<phase;trigger.write(std::cout);std::cout<<'\t'<<script.ticks()<<'\t'<<(trigger.latch==1?1:0);for(int n:script.offsets())std::cout<<'\t'<<n;std::cout<<'\t'<<std::boolalpha<<removed[sourceIndex]<<'\n';
                std::cout<<"G\t"<<s<<'\t'<<f<<'\t'<<phase;gate.write(std::cout);std::cout<<'\t'<<crc(maps.collision->cells)<<'\n';
            }
        }
    }
}
