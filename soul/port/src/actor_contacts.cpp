#include "actor_contacts.h"
#include <algorithm>
#include <cstring>
namespace soul {
namespace {
int jint(std::uint32_t value) { std::int32_t result; std::memcpy(&result,&value,4); return result; }
int add(int a,int b) { return jint(std::uint32_t(a)+std::uint32_t(b)); }
int sub(int a,int b) { return jint(std::uint32_t(a)-std::uint32_t(b)); }
int sar8(int a) { return jint((std::uint32_t(a)>>8)|(a<0?0xff000000u:0)); }
bool overlaps(const std::array<int,4>& a,const std::array<int,4>& b) {
    return a[0]<=b[2] && a[2]>=b[0] && a[1]<=b[3] && a[3]>=b[1] &&
        (a[0]!=a[2] || a[1]!=a[3]) && (b[0]!=b[2] || b[1]!=b[3]);
}
void handle(ContactBody& p,ContactBody& b,int cap) {
    b.refreshBounds();
    if(!overlaps(b.collision,p.collision) || p.y<=b.collision[1]) return;
    if(b.type==14 && (b.animation==2 || b.animation==3))
        throw FormatError("Type 14 platform contact is not ported");
    const int rightPenetration=sub(p.collision[2],b.collision[0]);
    const int leftPenetration=sub(b.collision[2],p.collision[0]);
    if(p.x<b.x) {
        if(p.vx<=0 && p.grounded && !b.pushLocked) {
            if(!b.wallRight) b.x=add(b.x,std::min(cap,rightPenetration));
            else if(!p.rightContact) p.x=sub(p.x,rightPenetration);
        } else if(!p.wallLeft && !p.leftContact) {
            if(p.vx>0 && b.vx<0 && !b.pushLocked) {
                p.x=sub(p.x,sar8(p.vx)); b.x=sub(b.x,sar8(b.vx));
            } else p.x=sub(p.x,std::min(cap,rightPenetration));
        } else if(!b.pushLocked) b.x=add(b.x,std::min(cap,rightPenetration));
        else if(p.wallLeft) p.x=add(p.x,std::min(cap,leftPenetration));
        p.rightContact=true;
    } else {
        if(p.vx>=0 && p.grounded && !b.pushLocked) {
            if(!b.wallLeft) b.x=sub(b.x,std::min(cap,leftPenetration));
            else if(!p.leftContact) p.x=add(p.x,leftPenetration);
        } else if(!p.wallRight && !p.rightContact) {
            // Original asymmetry: this branch also checks player vx > 0.
            if(p.vx>0 && b.vx<0 && !b.pushLocked) {
                p.x=sub(p.x,sar8(p.vx)); b.x=sub(b.x,sar8(b.vx));
            } else p.x=add(p.x,std::min(cap,leftPenetration));
        } else if(!b.pushLocked) b.x=sub(b.x,std::min(cap,leftPenetration));
        else if(p.wallRight) p.x=sub(p.x,std::min(cap,rightPenetration));
        p.leftContact=true;
    }
    b.touched=true;
}
}
void ContactBody::refreshBounds() {
    collision=localBounds;
    if(!absoluteBounds) for(int i=0;i<4;++i) collision[i]=add(collision[i],i%2?y:x);
}
std::array<int,4> contactBounds(const BSprite& sprite,int animation,int step,int flags) {
    const auto& a=sprite.animations().at(animation); const auto& af=sprite.animationFrames().at(a.start+step);
    const auto& frame=sprite.frames().at(af.frame);
    int rect=frame.rectCount?0:-1;
    auto result=sprite.frameBounds(af.frame,rect,(flags^af.flags)&3);
    if(result[0]==result[2] && result[1]==result[3]) result=sprite.frameBounds(af.frame,-1,(flags^af.flags)&3);
    for(int i=0;i<4;++i) result[i]=add(result[i],i%2?((flags&2)?-af.y:af.y):((flags&1)?-af.x:af.x));
    return result;
}
void resolvePlayerContacts(ContactBody& p,const std::vector<ContactBody*>& queue) {
    p.refreshBounds();
    for(auto it=queue.rbegin();it!=queue.rend();++it) {
        auto* b=*it;
        if(b && (b->type==37 || b->type==80) && !(b->flags&1024) && b->enabled) handle(p,*b,2560);
    }
    for(auto it=queue.rbegin();it!=queue.rend();++it) {
        auto* b=*it;
        if(b && b!=&p && b->type!=37 && b->type!=80 && !(b->flags&1024) && b->enabled &&
            b->action!=5 && b->action!=6 && b->action!=7) handle(p,*b,9);
    }
}
void separatePlayerContacts(ContactBody& p,const std::vector<ContactBody*>& queue,const ContactBody* platform) {
    for(auto it=queue.rbegin();it!=queue.rend();++it) {
        auto* b=*it;
        if(!b || b==&p || !b->separates) continue;
        b->refreshBounds();
        if(b==platform || b->type==82 || !overlaps(b->collision,p.collision) || p.y<=b->collision[1] || b->enabled) continue;
        const int right=sub(p.collision[2],b->collision[0]),left=sub(b->collision[2],p.collision[0]);
        if(p.x<=b->x && right>0) p.x=p.wallLeft?add(p.x,left):sub(p.x,right);
        else if(p.x>=b->x && left>0) p.x=p.wallRight?sub(p.x,right):add(p.x,left);
    }
}
}
