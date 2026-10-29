#include "chandelier_script.h"
#include <algorithm>
#include <cstring>
#include <cstdlib>
namespace soul {
namespace {
int s16(const Bytes& b,int i){return static_cast<std::int16_t>(unsigned(b.at(i))+(unsigned(b.at(i+1))<<8));}
int i32(const Bytes& b){std::uint32_t u=unsigned(b.at(0))|(unsigned(b.at(1))<<8)|(unsigned(b.at(2))<<16)|(unsigned(b.at(3))<<24);std::int32_t n;std::memcpy(&n,&u,4);return n;}
bool overlap(const std::array<int,4>& a,const std::array<int,4>& b){return (a[0]!=a[2] || a[1]!=a[3]) && (b[0]!=b[2] || b[1]!=b[3]) && a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1];}
}
ChandelierScript::ChandelierScript(ScriptData data):data_(std::move(data)){
    if(data_.uid!=160 || data_.tracks.size()!=2)throw FormatError("Expected chandelier script 160");
    for(std::size_t i=0;i<data_.tracks.size();++i){const auto& t=data_.tracks[i];if(t.kind!=2 || t.target!=(i?34:205))throw FormatError("Unexpected chandelier script target");
        for(const auto& k:t.keys)for(const auto& c:k.commands)if(c.opcode!=21 && !(i==0 && (c.opcode==107 || c.opcode==108)) && !(i==1 && (c.opcode==22 || c.opcode==106)))throw FormatError("Unsupported chandelier script command");
        offsets_.push_back(t.initial);
    }
}
void ChandelierScript::start(){ticks=0;++starts;}
void ChandelierScript::stop(){ticks=-2;for(std::size_t i=0;i<offsets_.size();++i)offsets_[i]=data_.tracks[i].initial;}
void ChandelierScript::update(const std::function<ChandelierActor(int)>& resolve,int cx,int cy,bool paused){
    if(paused || ticks<0)return;
    std::vector<ChandelierActor> actors(data_.tracks.size());
    for(std::size_t i=0;i<actors.size();++i)if(offsets_[i]<static_cast<int>(data_.tracks[i].raw.size())-2){actors[i]=resolve(data_.tracks[i].target);if(!actors[i])throw FormatError("Chandelier script target unavailable");}
    const int tick=ticks;ticks=static_cast<std::int16_t>(static_cast<std::uint16_t>(ticks+1));bool finished=true;
    for(std::size_t i=0;i<actors.size();++i){if(!actors[i])continue;auto& a=actors[i];const auto& t=data_.tracks[i];
        const auto key=std::find_if(t.keys.begin(),t.keys.end(),[&](const auto& k){return k.end>offsets_[i];});if(key==t.keys.end())throw FormatError("Invalid chandelier track offset");int target=-1,tx=0,ty=0;
        for(const auto& c:key->commands){if(c.opcode==21 && tick<=key->tick){tx=s16(c.operands,0);ty=s16(c.operands,2);target=key->tick;}
            else if(c.opcode==22 && key->tick<=tick)a.animate(c.operands.at(0));
            else if(c.opcode==106 && key->tick==tick)*a.action=c.operands.at(0); // Type 50 is not a basic enemy.
            else if(c.opcode==107 && key->tick==tick)*a.flags|=i32(c.operands);
            else if(c.opcode==108 && key->tick==tick)*a.flags&=~i32(c.operands);
        }
        if(key->tick<=tick)offsets_[i]=key->end;if(offsets_[i]<static_cast<int>(t.raw.size())-2)finished=false;
        if(target>=0){*a.x+=(tx-*a.x)/(target-tick+1);*a.y+=(ty-*a.y)/(target-tick+1);*a.bucket=std::abs(*a.x-(cx+120))/120+std::abs(*a.y-(cy+160))/160;if(*a.bucket<=1)a.refresh();}
    }
    if(finished){ticks=-2;++finishes;}
}
bool ChandelierTrigger::supported(const ActorRecord& r){const auto& p=r.parameters;return r.count==19 && p[0]==40 && (p[5]&8) && !(p[5]&~(1|2|4|8|16|32|128|256|512|1024|16384)) && p[8]>0 && p[9]>0 && p[10]==1 && p[11]==1 && p[12]==0 && p[13]>=0 && p[14]==-1 && p[15]==-1 && p[16]==160 && p[17]==-1 && p[18]==-1;}
ChandelierTrigger::ChandelierTrigger(const ActorRecord& r){if(!supported(r))throw FormatError("Unsupported chandelier trigger");const auto& p=r.parameters;uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);flags=p[5]|128;sourceUid=p[13];collision={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};}
void ChandelierTrigger::update(Player* player,ChandelierScript& script,bool paused){
    if(!alive || paused || (flags&(1024|16384)))return;
    if(player){if(player->uid!=sourceUid)throw FormatError("Chandelier trigger needs its player source");player->refreshBounds();if(overlap(player->collision,collision) && latch==-1){script.start();latch=1;}}
    // Leaving the rectangle does not cancel an already selected script.
    if(latch==1 && script.ticks==-2){script.stop();latch=-1;alive=false;}
}
}
