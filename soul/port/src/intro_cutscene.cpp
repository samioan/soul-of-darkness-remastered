#include "intro_cutscene.h"
#include <algorithm>
#include <cstring>
namespace soul {
namespace {
int s16(const Bytes& b,int i){return static_cast<std::int16_t>(unsigned(b.at(i))+(unsigned(b.at(i+1))<<8));}
int i32(const Bytes& b){std::uint32_t u=unsigned(b.at(0))|(unsigned(b.at(1))<<8)|(unsigned(b.at(2))<<16)|(unsigned(b.at(3))<<24);std::int32_t n;std::memcpy(&n,&u,4);return n;}
bool overlap(const std::array<int,4>& a,const std::array<int,4>& b){return (a[0]!=a[2] || a[1]!=a[3]) && (b[0]!=b[2] || b[1]!=b[3]) && a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1];}
}
IntroTrigger::IntroTrigger(const ActorRecord& r){const auto& p=r.parameters;if(r.count!=12 || p[0]!=20 || p[4]!=8 || !(p[5]&8) || (p[5]&~(1|2|4|8|16|32|128|256|512|1024|16384)) || p[8]<=0 || p[9]<=0 || (p[10]!=76 && p[10]!=133))throw FormatError("Unsupported intro trigger");
    uid=p[1];scriptUid=p[10];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);flags=p[5]|128;skipAllowed=p[11]==0 || p[11]==-1;collision={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};}
bool IntroTrigger::update(const Player& p,bool paused){if(!alive || paused || (flags&(1024|16384)) || !overlap(p.collision,collision))return false;alive=false;return true;}
IntroCutscene::IntroCutscene(ScriptData data,int w,int h):data_(std::move(data)),width_(w),height_(h){
    // Script 76 (level 0): enemy 19, flyers 83/84, camera, player 186, prompt 267. Script 133 (level 1): camera, player 131, enemy 215, flyer 135.
    if((data_.uid!=76 && data_.uid!=133) || data_.tracks.size()!=(data_.uid==76?6u:4u) || w<240 || h<320)throw FormatError("Intro needs script 76 or 133 and a full gameplay map");
    constexpr int targets76[]{19,83,84,-1,186,267},targets133[]{-1,131,215,135};
    for(std::size_t i=0;i<data_.tracks.size();++i){const auto& t=data_.tracks[i];const int want=data_.uid==76?targets76[i]:targets133[i];if(t.kind!=(want==-1?1:2) || (want!=-1 && t.target!=want))throw FormatError("Unexpected intro track target");
        for(const auto& k:t.keys)for(const auto& c:k.commands)if(c.opcode!=12 && c.opcode!=21 && c.opcode!=22 && c.opcode!=23 && c.opcode!=24 && c.opcode!=113 && c.opcode!=116)throw FormatError("Unsupported intro command");offsets_.push_back(t.initial);
    }
}
void IntroCutscene::start(int cx,int cy,bool allow){ticks=0;cameraX=cx;cameraY=cy;skipAllowed=allow;dialogue=-1;dialogueFrames=page=0;++starts;}
void IntroCutscene::step(const std::function<IntroActor(int)>& resolve){advance(resolve,false);}
void IntroCutscene::advance(const std::function<IntroActor(int)>& resolve,bool ignoreDialogue){
    if(!active() || (dialogue>=0 && !ignoreDialogue))return;
    const int tick=ticks;ticks=static_cast<std::int16_t>(static_cast<std::uint16_t>(ticks+1));bool finished=true;
    for(std::size_t i=0;i<data_.tracks.size();++i){const auto& t=data_.tracks[i];if(offsets_[i]>=static_cast<int>(t.raw.size())-2)continue;
        IntroActor actor;if(t.kind==2){actor=resolve(t.target);if(!actor)throw FormatError("Intro target UID unavailable");}
        const auto key=std::find_if(t.keys.begin(),t.keys.end(),[&](const auto& k){return k.end>offsets_[i];});if(key==t.keys.end())throw FormatError("Invalid intro track offset");int target=-1,tx=0,ty=0;
        for(const auto& c:key->commands){const int op=c.opcode;
            if((op==12 || op==21) && tick<=key->tick){tx=s16(c.operands,0);ty=s16(c.operands,2);if(op==12){tx=std::clamp(tx-120,0,width_-240);ty=std::clamp(ty-160,0,height_-320);}target=key->tick;}
            else if(op==22 && key->tick<=tick)actor.animate(c.operands.at(0));
            else if((op==23 || op==24) && key->tick<=tick){int flags=i32(c.operands);if(flags&1)flags|=65536;const int mask=flags>>16;if(op==23)*actor.flags|=mask;else *actor.flags&=~mask;*actor.facing=(*actor.flags&1)!=0;}
            else if(op==113 && tick==key->tick)actor.remove();
            else if(op==116 && tick==key->tick && dialogue<0){
                // String table 2 entries 0..3 keep ids 0..3; table 3 entries 0..6 (level 1) use ids 4..10.
                const int table=c.operands.at(0),index=c.operands.at(1);if(!((table==2 && index<=3) || (table==3 && index<=6)))throw FormatError("Unsupported intro dialogue");
                dialogue=table==2?index:4+index;speaker=c.operands[2];anchorX=*actor.x-cameraX;anchorY=actor.top()-cameraY;dialogueFrames=page=0;++dialoguesOpened;
            }
            if(op==24)*actor.facing=(*actor.flags&1)!=0;
        }
        if(key->tick<=tick)offsets_[i]=key->end;if(offsets_[i]<static_cast<int>(t.raw.size())-2)finished=false;
        if(target>=0){if(t.kind==1){cameraX+=(tx-cameraX)/(target-tick+1);cameraY+=(ty-cameraY)/(target-tick+1);}else{*actor.x+=(tx-*actor.x)/(target-tick+1);*actor.y+=(ty-*actor.y)/(target-tick+1);actor.moved();}}
    }
    if(finished){ticks=-2;dismissDialogue();++finishes;}
}
void IntroCutscene::skip(const std::function<IntroActor(int)>& resolve){if(!active() || !skipAllowed)return;++skips;for(int i=0;active() && i<1024;++i)advance(resolve,true);if(active())throw FormatError("Intro skip exceeded bounded timeline");}
void IntroCutscene::presentationTick(){const int target=active()?35:0;if(bars<target)bars=std::min(target,bars+20);else if(bars>target)bars=std::max(target,bars-20);if(dialogue>=0)++dialogueFrames;}
std::pair<int,int> introView(int x,int y,int w,int mw,int mh){if(w<=0 || mw<=0 || mh<320)throw FormatError("Invalid intro viewport");return {std::clamp(x+120-w/2,0,std::max(0,mw-w)),std::clamp(y,0,mh-320)};}
std::u16string_view introText(int id){switch(id){
case 0:return u"So this is the castle the townspeople spoke of! I should explore it with the utmost caution.";
case 1:return u"Well! What luck! I was just wondering what to have for dinner! Fresh meat does the undead good.";
case 2:return u"(That monster reeks!) Where is your master? The undead always serve a vampire!";
case 3:return u"Humans and their illusions! Our master is all-powerful! You will not meet him while you live, so become my dinner. I will treat you gently. Do not be afraid.";
// Level-1 intro (table 3). Desktop English translation of the Czech resource; not an official localization.
case 4:return u"I am looking for your master, the lord of the vampires. Tell me where to find him and I will let you go.";
case 5:return u"I will tell you how great our bomb is!";
case 6:return u"Ha... Poor human! I will never tell you that our master travels through the Forest of Lament!";
case 7:return u"...You just told him! Honestly, you undead do not think!";
case 8:return u"Really? Sorry.";
case 9:return u"Thanks for the information! Everyone who stands against me gets what they deserve!";
case 10:return u"I will show you how our steel tastes!";
default:throw FormatError("Unknown intro dialogue");}}
std::u16string_view introSpeaker(int id){if(id==0)return u"KALE";if(id==3)return u"ZOMBIE";if(id==4)return u"RARACH";throw FormatError("Unknown intro speaker");}
}
