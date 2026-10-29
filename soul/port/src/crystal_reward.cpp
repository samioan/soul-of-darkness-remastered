#include "crystal_reward.h"
#include <algorithm>
namespace soul {
void CrystalReward::begin(PlayerMeters& m,int type) {
    if(active || type<0 || type>1) throw FormatError("Invalid crystal reward entry");
    int& count=type==0?m.healthFragments:m.magicFragments;
    if(count<0 || count>3) throw FormatError("Invalid crystal fragment count");
    ++count; kind=type; frames=0; upgraded=false; active=true;
}
bool CrystalReward::update(PlayerMeters& m,bool pressed) {
    if(!active) return false;
    ++frames;
    if(!upgraded && frames==5) {
        int& count=kind==0?m.healthFragments:m.magicFragments;
        int& value=kind==0?m.health:m.magic; int& maximum=kind==0?m.maxHealth:m.maxMagic;
        if(count==4) { count=0; maximum=std::min(maximum+10,120); value=maximum; upgraded=true; }
        else if(kind==0 || m.magicUnlocked) value=std::min(value+10,maximum);
        else value=std::min(value,maximum);
    }
    if(frames>10 && pressed) { active=false; upgraded=false; return true; }
    return false;
}
int CrystalReward::displayed(const PlayerMeters& m) const { return upgraded?4:kind==0?m.healthFragments:m.magicFragments; }
}
