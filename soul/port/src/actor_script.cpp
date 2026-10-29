#include "actor_script.h"
#include <algorithm>
#include <cstdlib>
namespace soul {
namespace {
int size(int op) {
    static constexpr int extended[]{6,2,3,0,2,1,1,4,4,0,0,8,1,0,4,0,4,2,0,0,1,0};
    if(op>=100 && op<=121)return extended[op-100];
    if(op==11 || op==12 || op==21 || op==31 || op==41 || op==23 || op==24 || op==43 || op==44)return 4;
    if(op==22 || op==32 || op==42)return 1;
    if(op>=34 && op<=39)return ((op-34)%3+1)*2+(op<37?2:0);
    throw FormatError("Unsupported script opcode envelope");
}
int shortAt(const Bytes& b,std::size_t i){return static_cast<std::int16_t>(unsigned(b.at(i))+(unsigned(b.at(i+1))<<8));}
}
std::vector<ScriptData> decodeScripts(ByteView bytes) {
    BinaryReader r(bytes,"scripts");int count=r.i8();if(count<0)throw FormatError("Negative script count");std::vector<ScriptData> result;
    for(int i=0;i<count;++i) {
        ScriptData s;s.uid=r.i16le();int tracks=r.i8();if(tracks<0)throw FormatError("Negative script track count");s.metadata={r.u8(),r.u8()};
        for(int j=0;j<tracks;++j) {
            ScriptTrack t;const auto start=r.position();t.kind=r.i8();t.flags=r.u8();if(t.kind<0 || t.kind>3)throw FormatError("Invalid script track kind");
            if(t.kind>=2)t.target=r.i16le();int keys=r.i16le();if(keys<0)throw FormatError("Negative script key count");t.initial=static_cast<int>(r.position()-start);
            for(int k=0;k<keys;++k) {
                ScriptKey key;key.tick=r.i16le();int commands=r.i8();if(commands<0)throw FormatError("Negative script command count");
                for(int n=0;n<commands;++n){ScriptCommand command;command.opcode=r.i8();auto operands=r.take(size(command.opcode));command.operands.assign(operands.data(),operands.data()+operands.size());key.commands.push_back(std::move(command));}
                key.end=static_cast<int>(r.position()-start);t.keys.push_back(std::move(key));
            }
            auto raw=bytes.subview(start,r.position()-start);t.raw.assign(raw.data(),raw.data()+raw.size());t.raw.push_back(static_cast<std::uint8_t>(t.initial));t.raw.push_back(static_cast<std::uint8_t>(t.initial>>8));s.tracks.push_back(std::move(t));
        }
        result.push_back(std::move(s));
    }
    r.done();return result;
}
GateScript::GateScript(ScriptData data):data_(std::move(data)) {
    if(data_.tracks.empty())throw FormatError("Gate script needs an existing-actor track");
    for(const auto& t:data_.tracks){if(t.kind!=2)throw FormatError("Gate script cannot execute this track kind");for(const auto& key:t.keys)for(const auto& c:key.commands)if(c.opcode!=21 && c.opcode!=22 && c.opcode!=106)throw FormatError("Gate script cannot execute this command");}
    rewind();
}
void GateScript::rewind(){offsets_.clear();for(const auto& t:data_.tracks)offsets_.push_back(t.initial);}
void GateScript::start(){ticks_=0;}
void GateScript::restart(){ticks_=0;rewind();}
void GateScript::stop(){ticks_=-2;rewind();}
void GateScript::update(const std::function<LinkedGate*(int)>& resolve,int cx,int cy,bool paused,bool loop) {
    if(paused || ticks_<0)return;
    // Resolve all live tracks before changing any actor or clock.
    std::vector<LinkedGate*> actors(data_.tracks.size(),nullptr);
    for(std::size_t i=0;i<actors.size();++i)if(offsets_[i]<static_cast<int>(data_.tracks[i].raw.size())-2){actors[i]=resolve(data_.tracks[i].target);if(!actors[i])throw FormatError("Gate script target UID is unavailable");}
    const int tick=ticks_;ticks_=static_cast<std::int16_t>(static_cast<std::uint16_t>(ticks_+1));bool finished=true;
    for(std::size_t i=0;i<data_.tracks.size();++i) {
        const auto& track=data_.tracks[i];if(!actors[i])continue;auto& actor=*actors[i];
        auto key=std::find_if(track.keys.begin(),track.keys.end(),[&](const auto& k){return k.end>offsets_[i];});
        if(key==track.keys.end())throw FormatError("Invalid gate script track offset");
        int targetTick=-1,tx=0,ty=0;
        for(const auto& c:key->commands) {
            if(c.opcode==21 && tick<=key->tick){tx=shortAt(c.operands,0);ty=shortAt(c.operands,2);targetTick=key->tick;}
            else if(c.opcode==22 && key->tick<=tick)actor.immediate(c.operands.at(0));
            else if(c.opcode==106 && key->tick==tick)actor.action=c.operands.at(0);
        }
        if(key->tick<=tick)offsets_[i]=key->end;
        if(offsets_[i]<static_cast<int>(track.raw.size())-2)finished=false;
        if(targetTick>=0){actor.x+=(tx-actor.x)/(targetTick-tick+1);actor.y+=(ty-actor.y)/(targetTick-tick+1);actor.bucket=std::abs(actor.x-(cx+120))/120+std::abs(actor.y-(cy+160))/160;if(actor.bucket<=1)actor.refreshBounds();}
    }
    if(finished){if(loop)restart();else ticks_=-2;}
}
}
