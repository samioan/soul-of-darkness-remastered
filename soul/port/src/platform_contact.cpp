#include "player.h"
#include <cstring>
namespace soul {
namespace {
int jint(std::uint32_t u) { std::int32_t s; std::memcpy(&s,&u,4); return s; }
int add(int a,int b) { return jint(std::uint32_t(a)+std::uint32_t(b)); }
int sub(int a,int b) { return jint(std::uint32_t(a)-std::uint32_t(b)); }
int sar8(int a) { return jint((std::uint32_t(a)>>8)|(a<0?0xff000000u:0)); }
bool overlap(const std::array<int,4>& a,const std::array<int,4>& b) {
    return a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1] &&
        (a[0]!=a[2] || a[1]!=a[3]) && (b[0]!=b[2] || b[1]!=b[3]);
}
}
void updatePlatformContact(ContactBody& b,Player& p,int offset) {
    if(b.type!=33 && b.type!=50 && !(b.type==39 && b.animation==9)) throw FormatError("Unsupported platform contact type");
    b.refreshBounds(); const auto& r=b.collision;
    if(!p.platform) {
        const int predicted=add(p.y,sar8(p.vy));
        if(p.vy>=0 && p.y<=add(add(r[1],offset),5) && p.x>=r[0] && p.x<=r[2] &&
            (predicted>=add(r[1],sar8(b.vy)) || predicted>=r[1])) {
            p.platform=&b; p.request(6); p.y=add(r[1],1);
            if(b.type==50)b.platformLatch=0;
        }
    } else if(p.platform==&b) {
        p.refreshBounds();
        if((p.animation!=4 && p.animation!=5 && !overlap(p.collision,r) && sub(p.vy,b.vy)<0) || p.x>r[2] || p.x<r[0]) {
            p.platform=nullptr; p.vx=p.vy=0; return;
        }
        p.y=add(r[1],b.type==33 || b.type==39?1:0);
    }
}
}
