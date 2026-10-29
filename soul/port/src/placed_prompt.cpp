#include "placed_prompt.h"
namespace soul {
namespace {
bool overlap(const std::array<int,4>& a,const std::array<int,4>& b){return (a[0]!=a[2] || a[1]!=a[3]) && (b[0]!=b[2] || b[1]!=b[3]) && a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1];}
}
std::u16string_view desktopPromptText(int n){switch(n){case 0:case 12:return u"SPACE + LEFT";case 2:return u"SPACE + RIGHT";case 4:case 25:return u"E / J - ATTACK";case 6:return u"DOWN / S - CROUCH";case 8:return u"A / D - MOVE";case 10:return u"RIGHT / D";case 17:return u"K / 0 - FIRE";case 18:return u"K / 0 - ICE";case 34:return u"Q / 9 - SWITCH";default:return {};}}
PlacedPrompt::PlacedPrompt(const ActorRecord& r){
    const auto& p=r.parameters;
    if(r.count!=10 || p[0]!=21 || !(p[5]&8) || (p[5]&~(1|2|4|8|16|32|64|128|256|512|1024|16384)) || p[8]<=0 || p[9]<=0 || (p[4]!=14 && desktopPromptText(p[4]).empty()) || p[4]==25)throw FormatError("Unsupported placed prompt record");
    uid=p[1];x=static_cast<std::uint16_t>(p[2]);y=static_cast<std::uint16_t>(p[3]);animation=p[4];flags=animation?p[5]&~64:p[5];collision={x+p[6],y+p[7],x+p[6]+p[8],y+p[7]+p[9]};
}
bool PlacedPrompt::update(const Player& p,int magic,int& switched,bool paused){
    if(!alive || paused || (flags&(1024|16384)))return false;
    const bool touching=overlap(p.collision,collision);
    if(touching && status!=2)status=1;
    if(delay==0 && status==1 && !touching && animation!=17 && animation!=18 && animation!=34){alive=false;return true;}
    if((animation==17 && magic==0 || animation==18 && magic==1) && (touching || (collision[0]<=p.collision[0] && collision[1]<=p.collision[1] && collision[2]>=p.collision[2] && collision[3]>=p.collision[3])))status=2;
    if(status==2 && !touching){alive=false;return true;}
    if(animation==34){if(switched==1){alive=false;return true;}switched=0;}return false;
}
bool PlacedPrompt::visible(const Player& p,const std::array<int,4>& view)const{return alive && !(flags&1024) && overlap(animation==17 || animation==18?view:p.collision,collision);}
void PlacedPrompt::advanceAnimation(const BSprite& s){if(!alive || (flags&64))return;const auto& a=s.animations().at(animation);const int duration=s.animationFrames().at(a.start+step).duration;if(duration && ++tick>=duration){tick=0;if(++step>=a.count)step=0;}}
}
