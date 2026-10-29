#include "player_probe.h"
#include "actor_script.h"
#include <iostream>
#include <iomanip>
namespace {
std::uint32_t crc(const soul::Bytes& b){std::uint32_t n=0xffffffff;for(auto v:b){n^=v;for(int j=0;j<8;++j)n=(n>>1)^((n&1)?0xedb88320:0);}return n^0xffffffff;}
}
void emitGateScriptManifest(const std::filesystem::path& root) {
    const auto sprites=soul::ResourcePack::numeric(soul::readAssetFile(root/"2")),pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"3"));
    const auto sprite=soul::BSprite::parse(sprites.resource(28).value());
    for(const std::string s:{"shipped","pause","restart","stop","start-again","loop","no-handler","animation"}) {
        soul::ScriptData data;for(auto& script:soul::decodeScripts(pack.resource(1).value()))if(script.uid==165)data=std::move(script);
        if(s=="animation")data.tracks[0].keys[0].commands[1].operands[0]=5;
        soul::GateScript script(std::move(data));auto maps=soul::LevelMaps::load(pack,true);
        soul::ActorRecord r{};r.count=9;r.parameters={31,154,1468,1500,4,0,4,-1,-1};soul::LinkedGate gate(sprite,maps,r);script.start();
        for(int f=0;f<48;++f) {
            const bool paused=s=="pause" && f>=7 && f<13;
            if(s=="restart" && f==32)script.restart();if(s=="stop" && f==8)script.stop();if(s=="start-again" && f==32)script.start();
            script.update([&](int uid)->soul::LinkedGate*{return uid==154?&gate:nullptr;},1280,1280,paused,s=="loop");
            if(s!="no-handler")gate.update([](int)->const int*{return nullptr;},paused);
            for(int phase=0;phase<2;++phase) {
                if(phase==1 && !paused)gate.advanceAnimation();
                std::cout<<"T\t"<<s<<'\t'<<f<<'\t'<<phase<<'\t'<<script.ticks();for(int n:script.offsets())std::cout<<'\t'<<n;std::cout<<'\n';
                std::cout<<"G\t"<<s<<'\t'<<f<<'\t'<<phase;gate.write(std::cout);std::cout<<'\t'<<crc(maps.collision->cells)<<'\n';
            }
        }
    }
}
void emitScriptDataManifest(const std::filesystem::path& root) {
    for(int p=3;p<=11;++p){const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/std::to_string(p)));const auto scripts=soul::decodeScripts(pack.resource(1).value());
        for(std::size_t i=0;i<scripts.size();++i){const auto& s=scripts[i];std::cout<<"S\t"<<p<<'\t'<<i<<'\t'<<s.uid<<'\t'<<int(s.metadata[0])<<'\t'<<int(s.metadata[1])<<'\t'<<s.tracks.size()<<'\n';
            for(std::size_t j=0;j<s.tracks.size();++j){const auto& t=s.tracks[j];std::cout<<"T\t"<<p<<'\t'<<i<<'\t'<<j<<'\t'<<t.kind<<'\t'<<t.flags<<'\t'<<t.target<<'\t'<<t.initial<<'\t';
                for(auto b:t.raw)std::cout<<std::hex<<std::setfill('0')<<std::setw(2)<<int(b);std::cout<<std::dec<<'\n';}
        }
    }
}
