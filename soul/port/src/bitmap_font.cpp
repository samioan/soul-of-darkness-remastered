#include "bitmap_font.h"
#include <limits>

namespace soul {
namespace {
constexpr std::u16string_view glyphs = u" !\"#$%&'()*+,-./0123456789:;<=>?\u00a9@ABCDEFGHIJKLMNOPQRSTUVWXYZ_\u00c1\u00c9\u00cd\u00d3\u00da\u00dd\u00c7\u00d0\u00ca\u00d1\u00ae\u00dc\u00a6\u00db\u00a3";
constexpr int iconWidths[]{3,6,3,6,3}, iconHeights[]{3,16,3,16,2};
constexpr int iconOffsets[]{0,5,0,8,0}, iconCounts[]{5,1,16,1,6};
void validate(std::u16string_view text) {
    if (text.size() > 32768) throw std::invalid_argument("Bitmap text exceeds 32768 UTF-16 units");
    for (std::size_t i=0; i<text.size(); ++i) if (text[i]==u'~') {
        if (++i == text.size() || text[i]<u'0' || text[i]>u'4')
            throw std::invalid_argument("Inline icon must be ~0 through ~4");
    }
}
int coordinate(std::int64_t value) {
    if (value < (std::numeric_limits<int>::min)() || value > (std::numeric_limits<int>::max)())
        throw std::invalid_argument("Bitmap text coordinate overflow");
    return static_cast<int>(value);
}
}
BitmapFont::BitmapFont(const BSprite& sprite) : sprite_(sprite),renderer_(sprite) {
    if (sprite.modules().size()<2 || sprite.frames().size()<glyphs.size() || sprite.frameModules().size()<glyphs.size())
        throw std::invalid_argument("Incomplete bitmap font tables");
    // The reference wrapper indexes the flat frame-module table by glyph*4,
    // rather than following frame starts (even for multi-module glyphs).
    for (std::size_t i=0; i<glyphs.size(); ++i) {
        const auto& m=sprite.frameModules()[i];
        if ((m.flags & 0x10) || m.module>255)
            throw std::invalid_argument("Unsupported bitmap font frame layout");
    }
}
int BitmapFont::glyph(char16_t c) {
    const auto found=glyphs.find(c); return found==glyphs.npos ? 31 : static_cast<int>(found);
}
std::u16string BitmapFont::uppercase(std::u16string_view text) {
    std::u16string result;
    for (auto c:text) {
        if ((c>=u'a' && c<=u'z') || (c>=0x00e0 && c<=0x00fe && c!=0x00f7)) c-=32;
        if (c==0x00df) result+=u"SS";
        else { if (c==0x00ff) c=0x0178; result+=c; }
    }
    return result;
}
int BitmapFont::measure(std::u16string_view text) const {
    validate(text); int width=0;
    for (std::size_t i=0; i<text.size(); ++i) {
        const auto c=text[i]; if (c==u'{' || c==u'}') continue;
        if (c==u'~') { ++i; width+=static_cast<std::int8_t>(sprite_.modules()[0].width)*2; }
        else width+=static_cast<std::int8_t>(sprite_.frames()[glyph(c)].visual.width)-1;
    }
    return width;
}
std::vector<int> BitmapFont::wrap(std::u16string_view text, int width, std::size_t palette) const {
    validate(text); std::vector<int> ends; int count=0, advance=0;
    auto add=[&](int end) { if (ends.size()>=200) throw std::invalid_argument("Bitmap text exceeds 200 lines"); ends.push_back(end); };
    const int length=static_cast<int>(text.size());
    for (int i=0; i<length; ++i) {
        const auto c=text[i];
        if (c==u'^' && advance<=width) { add(i); advance=count=0; }
        else if (width>0 && (c==u'^' || c==u' ' || i==length-1) && advance>width) {
            const int previous=ends.empty() ? 0 : ends.back();
            do { --i; } while (i>previous && text[i]!=u' ');
            if (i==previous) i=previous+count;
            if (i<0 || i>=length) throw std::invalid_argument("Invalid reference wrap boundary");
            add(i); count=advance=0;
        } else {
            ++count;
            if (c==u'{' || c==u'}') continue;
            if (c==u'~') { ++i; advance+=static_cast<std::int8_t>(sprite_.modules()[0].width)*2; }
            else {
                advance+=sprite_.modules()[sprite_.frameModules()[glyph(c)].module].width+sprite_.frameModules()[0].x;
                advance-=palette==5 ? 2 : 1;
            }
        }
    }
    add(length); return ends;
}
void BitmapFont::drawLine(Canvas& target, std::u16string_view text, int x, int y, unsigned anchor,
    std::size_t base, std::size_t& palette, std::uint64_t stateFrame, SpriteRenderer* icons) {
    validate(text);
    // Same coarse vertical early-out as the reference, including skipped controls.
    const auto clip=target.clip(); if (static_cast<std::int64_t>(y)<static_cast<std::int64_t>(clip.top)-20 || y>clip.bottom) return;
    if (anchor&8) x=coordinate(static_cast<std::int64_t>(x)-measure(text));
    else if (anchor&1) x=coordinate(static_cast<std::int64_t>(x)-(measure(text)>>1));
    if (anchor&32) y=coordinate(static_cast<std::int64_t>(y)-height());
    else if (anchor&2) y=coordinate(static_cast<std::int64_t>(y)-(height()>>1));
    for (std::size_t i=0; i<text.size(); ++i) {
        const auto c=text[i];
        if (c==u'{') palette=1;
        else if (c==u'}') palette=base;
        else if (c==u'~') {
            const int id=text[++i]-u'0';
            if (!icons) throw std::invalid_argument("Inline text icons require sprite 33 renderer");
            icons->drawAnimation(target,id,stateFrame%iconCounts[id],coordinate(static_cast<std::int64_t>(x)+iconOffsets[id]),
                coordinate(static_cast<std::int64_t>(y)+height()-iconHeights[id]));
            x=coordinate(static_cast<std::int64_t>(x)+static_cast<std::int8_t>(sprite_.modules()[0].width)+iconWidths[id]+iconOffsets[id]/2);
        } else {
            const int frame=glyph(c); renderer_.drawFrame(target,frame,x,y,{palette,0,{}});
            x=coordinate(static_cast<std::int64_t>(x)+sprite_.frames()[frame].visual.width-1);
        }
    }
}
void BitmapFont::draw(Canvas& target, std::u16string_view text, int x, int y, unsigned anchor,
    std::size_t palette, std::uint64_t stateFrame, SpriteRenderer* icons) {
    const auto upper=uppercase(text); validate(upper);
    drawLine(target,upper,x,y,anchor,palette,palette,stateFrame,icons);
}
int BitmapFont::drawWrapped(Canvas& target, std::u16string_view text, int x, int y, int width, unsigned anchor,
    std::size_t palette, std::uint64_t stateFrame, SpriteRenderer* icons, bool prologue) {
    const auto raw=std::u16string(u"}")+std::u16string(text);
    const auto ends=wrap(raw,width,palette); const auto upper=uppercase(raw);
    const int spacing=lineHeight(prologue);
    const auto span=static_cast<std::int64_t>(spacing)*static_cast<std::int64_t>(ends.size()-1);
    if (anchor&32) y=coordinate(static_cast<std::int64_t>(y)-span);
    else if (anchor&2) y=coordinate(static_cast<std::int64_t>(y)-(span>>1));
    std::size_t current=palette, start=0;
    for (std::size_t i=0; i<ends.size(); ++i) {
        const auto end=static_cast<std::size_t>(ends[i]);
        if (end<start) throw std::invalid_argument("Reversed text line");
        drawLine(target,std::u16string_view(upper).substr(start,end-start),x,
            coordinate(static_cast<std::int64_t>(y)+static_cast<std::int64_t>(i)*spacing),anchor,palette,current,stateFrame,icons);
        start=end+1;
    }
    return coordinate(static_cast<std::int64_t>(y)+static_cast<std::int64_t>(ends.size())*spacing);
}
}
