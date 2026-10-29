#include "bitmap_font.h"
#include <iostream>
#include <limits>
#include <type_traits>
static_assert(!std::is_constructible<soul::BitmapFont,soul::BSprite&&>::value,"Font cannot borrow a temporary");
namespace {
int checks=0;
void require(bool result,const char* message) { ++checks; if (!result) throw std::runtime_error(message); }
template<class F> void rejects(F f,const char* message) {
    bool failed=false; try { f(); } catch (const std::invalid_argument&) { failed=true; } require(failed,message);
}
void u16(soul::Bytes& b,unsigned n) { b.push_back(static_cast<std::uint8_t>(n)); b.push_back(static_cast<std::uint8_t>(n>>8)); }
void u32(soul::Bytes& b,unsigned n) { u16(b,n&65535); u16(b,n>>16); }
soul::BSprite fixture() {
    soul::Bytes b; u16(b,991); u32(b,1|256|16777216);
    u16(b,2); b.insert(b.end(),{2,2,2,2});
    u16(b,76); for (int i=0;i<76;++i) b.insert(b.end(),{1,0,0,0});
    u16(b,0); u16(b,76);
    for (int i=0;i<76;++i) { b.insert(b.end(),{1,0}); u16(b,i); b.push_back(0); }
    for (int i=0;i<76;++i) b.insert(b.end(),{0,0,2,2});
    u16(b,21781); b.insert(b.end(),{6,2});
    for (int p=0;p<6;++p) { u16(b,0); u16(b,p==1 ? 0x83e0 : 0xfc00); }
    u16(b,1024); for (int i=0;i<2;++i) { u16(b,1); b.push_back(0x55); }
    return soul::BSprite::parse(soul::ByteView(b));
}
}
int main() {
    try {
        const auto sprite=fixture(); soul::BitmapFont font(sprite);
        require(font.glyph(u'A')==34,"ASCII glyph mapping");
        require(font.glyph(u'\u00c1')==61,"accent glyph mapping");
        require(font.glyph(u'\u2603')==31,"unknown glyph fallback");
        require(font.uppercase(u"az\u00e1\u00df")==u"AZ\u00c1SS","Latin uppercase and expansion");
        require(font.height()==2 && font.lineHeight()==7 && font.lineHeight(true)==10,"line spacing");
        require(font.measure(u"AB")==2,"glyph advance");
        require(font.measure(u"{A}B")==2,"palette controls have no width");
        require(font.measure(u"~0A")==5,"icon measurement differs from rendering advance");
        require(font.wrap(u"A^B",20)==std::vector<int>({1,3}),"explicit line separator");
        require(font.wrap(u"A A A A",2)!=font.wrap(u"A A A A",20),"width change recomputes wrapping");
        require(font.wrap(u"A A A A",2,5)!=font.wrap(u"A A A A",2,0),"palette 5 wrapping");
        require(font.wrap(u"",40)==std::vector<int>({0}),"empty line layout");
        soul::Canvas target(240,320); font.draw(target,u"a",10,10);
        require(target.pixels()[10*240+10]==0xfff80000,"lowercase text draws uppercase glyph");
        target.clear(0); font.draw(target,u"{A}B",10,10);
        require(target.pixels()[10*240+10]==0xff00f800 && target.pixels()[10*240+12]==0xfff80000,"palette switch and restoration");
        target.clear(0); font.draw(target,u"AB",10,10,8|32);
        require(target.pixels()[8*240+8]!=0 && target.pixels()[10*240+10]==0,"right/bottom anchoring");
        target.clear(0); target.setClip(11,11,1,1); font.draw(target,u"A",10,10);
        require(target.pixels()[11*240+11]!=0 && target.pixels()[10*240+10]==0,"font obeys canvas clip");
        target.resetClip(); const auto before=target.checksum(); font.draw(target,u"A",10,319+30);
        require(target.checksum()==before,"coarse vertical cull");
        target.clear(0); require(font.drawWrapped(target,u"A^B",10,10,200)==24,"wrapped return position");
        const auto first=target.checksum(); target.clear(0); font.drawWrapped(target,u"A^B",10,10,200);
        require(target.checksum()==first,"drawing repeat has no clock mutation");
        rejects([&]{font.measure(u"~");},"trailing icon rejected");
        rejects([&]{font.measure(u"~9");},"invalid icon rejected");
        rejects([&]{font.draw(target,u"~0",0,0);},"missing icon provider rejected");
        rejects([&]{font.wrap(std::u16string(201,u'^'),20);},"reference line capacity guarded");
        rejects([&]{font.measure(std::u16string(32769,u'A'));},"oversized text rejected");
        rejects([&]{font.draw(target,u"AB",(std::numeric_limits<int>::min)(),0,8);},"anchor overflow rejected");
        soul::Canvas wide(569,320); font.drawWrapped(wide,u"A A A A",284,10,537);
        require(wide.pixels()[10*569+284]==0xfff80000,"widescreen text starts at the requested logical coordinate");
        std::cout << "PASS: " << checks << " bitmap font assertions\n"; return 0;
    } catch(const std::exception& e) { std::cerr << e.what() << '\n'; return 1; }
}
