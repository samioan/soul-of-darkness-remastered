#include "actor_contacts.h"
#include <iostream>
#include <limits>
namespace {
int checks=0;
void require(bool value,const char* message) { ++checks; if(!value) throw std::runtime_error(message); }
soul::ContactBody box(int x) {
    soul::ContactBody b; b.x=x; b.y=199; b.localBounds={-9,-59,9,0}; b.enabled=true; b.refreshBounds(); return b;
}
}
int main() {
    try {
        auto p=box(100),b=box(108); p.type=0; b.pushLocked=true;
        soul::resolvePlayerContacts(p,{&b});
        require(p.x==91 && b.x==108 && b.touched,"ordinary contact moves player by at most nine pixels");
        require(p.rightContact && !p.leftContact && p.collision[0]==91,"contact keeps pre-correction bounds until the owner refreshes them");
        p=box(100); b=box(108); p.grounded=true;
        soul::resolvePlayerContacts(p,{&b});
        require(p.x==100 && b.x==117 && b.touched,"stationary grounded player pushes an unlocked body");
        p=box(100); b=box(108); b.type=37; b.pushLocked=true; b.action=5;
        soul::resolvePlayerContacts(p,{&b});
        require(p.x==90 && b.touched,"priority gate uses full penetration even in state five");
        p=box(100); b=box(108); b.action=5;
        soul::resolvePlayerContacts(p,{&b});
        require(p.x==100 && !b.touched,"ordinary state five is excluded");
        p=box(100); b=box(108); p.vx=2560; b.vx=-768;
        soul::resolvePlayerContacts(p,{&b});
        require(p.x==90 && b.x==111,"opposing velocities undo each body's integer movement");
        p=box(108); b=box(100); p.vx=2560; b.vx=-768;
        soul::resolvePlayerContacts(p,{&b});
        require(p.x==98 && b.x==103 && p.leftContact,"right-side branch preserves original velocity asymmetry");
        p=box(100); b=box(108); p.wallLeft=true; b.pushLocked=true;
        soul::resolvePlayerContacts(p,{&b});
        require(p.x==109,"wall-pinned player can be displaced across the other side");
        p=box(100); b=box(108); b.flags=1024;
        soul::resolvePlayerContacts(p,{&b});
        require(p.x==100 && !b.touched,"inactive flag disables early contact");
        b.flags=128; b.pushLocked=true; soul::resolvePlayerContacts(p,{&b});
        require(p.x==91 && b.touched,"hidden flag alone does not disable contact");
        p=box(100); b=box(108); b.y=258;
        soul::resolvePlayerContacts(p,{&b});
        require(p.x==100 && !b.touched,"feet exactly at body top do not produce a side contact");
        p=box(100); b=box(105); b.y=189; b.localBounds={0,0,0,0};
        soul::resolvePlayerContacts(p,{&b});
        require(p.x==100 && !b.touched,"point rectangle rejected");
        b.y=199; b.localBounds={0,-20,0,0}; b.pushLocked=true;
        soul::resolvePlayerContacts(p,{&b});
        require(p.x==96 && b.touched,"line rectangle remains eligible");
        p=box(100); b=box(108); b.separates=true; b.enabled=false; b.flags=1024;
        soul::separatePlayerContacts(p,{&b});
        require(p.x==90,"late separation preserves its own filters and ignores inactive flag");
        p=box(100); b.type=82; soul::separatePlayerContacts(p,{&b});
        require(p.x==100,"grouped platform excluded from late side separation");
        b.type=1; soul::separatePlayerContacts(p,{&b},&b);
        require(p.x==100,"current support platform excluded from late separation");
        b.enabled=true; soul::separatePlayerContacts(p,{&b});
        require(p.x==100,"early-contact body excluded from late separation");
        p=box(100); soul::resolvePlayerContacts(p,{nullptr,&p});
        require(p.x==100 && !p.touched,"null queue slots and player identity skipped");
        p=box(100); b=box(108); b.type=14; b.animation=2;
        bool rejected=false; try { soul::resolvePlayerContacts(p,{&b}); } catch(const soul::FormatError&) { rejected=true; }
        require(rejected,"type 14 attached/platform path fails explicitly");
        b=box(108); b.absoluteBounds=true; b.localBounds={99,140,117,199}; b.refreshBounds();
        b.x=500; b.refreshBounds(); require(b.collision[0]==99,"absolute custom bounds stay in world coordinates");
        b=box(std::numeric_limits<int>::max());
        require(b.collision[2]==std::numeric_limits<int>::min()+8,"bounds addition preserves Java overflow");
        std::cout << "PASS: " << checks << " native side-contact assertions\n"; return 0;
    } catch(const std::exception& error) { std::cerr << error.what() << '\n'; return 1; }
}
