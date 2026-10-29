#include "sprite_renderer.h"
#include <iostream>
#include <limits>
#include <type_traits>
static_assert(!std::is_constructible<soul::SpriteRenderer,soul::BSprite&&>::value, "Renderer cannot borrow a temporary sprite");

namespace {
int checks = 0;
void require(bool result, const char* message) { ++checks; if (!result) throw std::runtime_error(message); }
template<class Error = std::invalid_argument, class Function>
void rejects(Function function, const char* message) {
    bool failed = false; try { function(); } catch (const Error&) { failed = true; } require(failed,message);
}
void u16(soul::Bytes& bytes, unsigned value) { bytes.push_back(static_cast<std::uint8_t>(value)); bytes.push_back(static_cast<std::uint8_t>(value >> 8)); }
void u32(soul::Bytes& bytes, unsigned value) { u16(bytes,value & 65535); u16(bytes,value >> 16); }
soul::BSprite fixture() {
    soul::Bytes b; u16(b,991); u32(b,1|256|65536|16777216);
    u16(b,2); b.insert(b.end(),{2,2,1,1}); // 2x2 and 1x1 modules.
    u16(b,3); b.insert(b.end(),{0,255,255,0, 1,0,0,1, 0,3,1,17});
    u16(b,0); u16(b,2);
    b.insert(b.end(),{2,0}); u16(b,0); b.push_back(0);
    b.insert(b.end(),{1,0}); u16(b,2); b.push_back(0);
    b.insert(b.end(),{255,255,2,2, 0,0,5,5});
    u16(b,1); b.insert(b.end(),{1,2,254,3,2});
    u16(b,1); b.insert(b.end(),{1,0}); u16(b,0);
    u16(b,21781); b.insert(b.end(),{1,4});
    for (unsigned color : {0x001f,0xfc00,0x83e0,0x801f}) u16(b,color);
    u16(b,1024); u16(b,1); b.push_back(0x6c); u16(b,1); b.push_back(0x80);
    return soul::BSprite::parse(soul::ByteView(b));
}
void canvas() {
    soul::Canvas c(2,2,0xff0000ff);
    require(c.pixels().size() == 4, "canvas dimensions");
    c.blit({0x80ff0000},1,1,0,0);
    require(c.pixels()[0] == 0xff80007f, "half alpha over opaque destination");
    c.clear(0); c.blit({0x80ff0000},1,1,0,0);
    require(c.pixels()[0] == 0x80ff0000, "straight alpha over transparent destination");
    c.clear(0x800000ff); c.blit({0x80ff0000},1,1,0,0);
    require(c.pixels()[0] == 0xc0aa0055, "source-over rounding on translucent destination");
    c.blit({0x0000ff00},1,1,0,0);
    require(c.pixels()[0] == 0xc0aa0055, "alpha zero leaves destination unchanged");
    c.clear(0); c.blit({1,2,3,4},2,2,0,0); // All alpha zero, hidden RGB ignored.
    require(c.pixels() == soul::Pixels(4,0), "transparent RGB is not drawn");
    const soul::Pixels source{0xffff0000,0xff00ff00,0xff0000ff,0xffffffff};
    c.blit(source,2,2,-1,0,1);
    require(c.pixels() == soul::Pixels({0xffff0000,0,0xff0000ff,0}), "flipped source coordinates survive left clipping");
    c.clear(0); c.setClip(1,1,1,1); c.blit(source,2,2,0,0);
    require(c.pixels() == soul::Pixels({0,0,0,0xffffffff}), "half-open surface clip");
    c.intersectClip(0,0,1,1); c.blit(source,2,2,0,0);
    require(c.pixels() == soul::Pixels({0,0,0,0xffffffff}), "disjoint intersection is empty");
    c.clear(0); c.setClip(-1,-1,2,2); c.blit(source,2,2,0,0);
    require(c.pixels() == soul::Pixels({0xffff0000,0,0,0}), "negative clip clamps to canvas");
    c.setClip(0,0,-1,1); require(!c.visible(0,0,2,2), "negative clip size is empty");
    c.resetClip(); const auto before = c.checksum();
    for (auto x : {(std::numeric_limits<std::int64_t>::min)(),(std::numeric_limits<std::int64_t>::max)()}) c.blit(source,2,2,x,x);
    require(c.checksum() == before, "extreme destination coordinates do not overflow");
    c.setClip((std::numeric_limits<int>::max)(),0,(std::numeric_limits<int>::max)(),2);
    require(!c.visible(0,0,2,2), "overflow-sized clip endpoint handled in wide arithmetic");
    rejects([&] { c.blit(source,1,1,0,0); }, "bad source pixel count");
    rejects([&] { c.blit(source,2,2,0,0,4); }, "bad pixel transform");
    rejects([&] { c.blit(c.pixels(),2,2,0,0); }, "self-overlap rejected explicitly");
    rejects([&] { c.resize(0,2); }, "zero surface dimension");
    rejects([&] { c.resize(16000001,1); }, "oversized surface");
    require(c.width() == 2 && c.pixels().size() == 4, "invalid resize retains previous surface");
    c.resize(1,1,0xff0000ff); require(c.checksum() == 0xd2433660U, "canonical ARGB CRC32 vector");
    require(c.clip().right == 1 && c.clip().bottom == 1, "resize resets clip");
}
void sprites() {
    const auto sprite = fixture(); soul::SpriteRenderer renderer(sprite); soul::Canvas c(6,6);
    renderer.drawFrame(c,0,2,2);
    soul::Pixels expected(36,0);
    expected[7] = 0xfff80000; expected[8] = 0xff00f800; expected[13] = 0xff0000f8; expected[14] = 0xff00f800;
    require(c.pixels() == expected, "frame module order and origin");
    const auto checksum = c.checksum(); c.clear(0); renderer.drawFrame(c,0,2,2);
    require(c.checksum() == checksum, "cached second render is identical");
    require(sprite.animationFrames()[0].duration == 2 && sprite.animationFrames()[0].x == -2, "drawing preserves animation metadata");
    c.clear(0); renderer.drawFrame(c,0,2,2,{0,1,{}});
    expected.assign(36,0); expected[7] = 0xff00f800; expected[8] = 0xfff80000; expected[13] = 0xff00f800; expected[14] = 0xff0000f8;
    require(c.pixels() == expected, "parent flip shifts leaves by width");
    c.clear(0); renderer.drawFrame(c,1,1,1);
    expected.assign(36,0); expected[9] = 0xff00f800; expected[10] = 0xfff80000; expected[15] = 0xff00f800; expected[16] = 0xff0000f8;
    require(c.pixels() == expected, "nested draw uses enclosing offset and XOR flip");
    c.clear(0); renderer.drawAnimation(c,0,0,3,0);
    expected.assign(36,0); expected[9] = 0xff00f800; expected[10] = 0xff0000f8; expected[15] = 0xff00f800; expected[16] = 0xfff80000;
    require(c.pixels() == expected, "animation signed offsets and transform precede nested frame");
    c.clear(0); renderer.drawFrame(c,0,2,2,{0,0,{1,0}});
    expected.assign(36,0); expected[14] = 0xff00f800;
    require(c.pixels() == expected, "local module mask hides only slots equal to one");
    c.clear(0); renderer.drawFrame(c,1,1,1,{0,0,{1,0}});
    require(c.pixels() == soul::Pixels(36,0), "mask also applies at nested parent slot");
    c.setClip(1,1,2,2); const auto clip = c.clip(); renderer.drawModule(c,0,1,1);
    require(c.clip().left == clip.left && c.clip().right == clip.right, "render preserves caller clip");
    rejects([&] { renderer.drawFrame(c,0,1,1,{0,4,{}}); }, "invalid frame transform");
    rejects([&] { renderer.drawFrame(c,0,1,1,{0,0,{0}}); }, "short frame mask");
    rejects<std::out_of_range>([&] { renderer.drawFrame(c,2,1,1); }, "invalid frame index");
    rejects<std::out_of_range>([&] { renderer.drawAnimation(c,0,1,1,1); }, "invalid animation step");
    rejects<std::out_of_range>([&] { renderer.drawAnimation(c,1,0,1,1); }, "invalid animation index");
    rejects<std::out_of_range>([&] { renderer.drawModule(c,0,1,1,{1,0,{}}); }, "invalid palette even when clipped");
    for (int width : {240,569}) {
        soul::Canvas wide(width,320); renderer.drawFrame(wide,0,width-1,319);
        require(wide.pixels()[static_cast<std::size_t>(318)*width+width-2] == 0xfff80000, "right/bottom clipping at logical widths");
        require(wide.pixels().size() == static_cast<std::size_t>(width)*320, "logical-width surface allocation");
    }
}
}
int main() {
    try { canvas(); sprites(); std::cout << "PASS: " << checks << " native renderer assertions (synthetic sprites/surfaces; no game data)\n"; return 0; }
    catch (const std::exception& error) { std::cerr << error.what() << '\n'; return 1; }
}
