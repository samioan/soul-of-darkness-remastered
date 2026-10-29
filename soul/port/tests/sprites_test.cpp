#include "bsprite.h"
#include <iostream>

namespace {
int checks = 0;
void require(bool value, const char* label) { ++checks; if (!value) throw std::runtime_error(label); }
template<class Error = soul::FormatError, class Function>
void rejects(Function fn, const char* label) {
    bool rejected = false; try { fn(); } catch (const Error&) { rejected = true; } require(rejected,label);
}
void u16(soul::Bytes& b, unsigned v) { b.push_back(static_cast<std::uint8_t>(v)); b.push_back(static_cast<std::uint8_t>(v >> 8)); }
void u32(soul::Bytes& b, std::uint32_t v) { u16(b,v & 65535); u16(b,v >> 16); }
struct Fixture { soul::Bytes bytes; std::size_t reference, frameStart, rectCount; };
Fixture custom(unsigned codec, soul::Bytes encoded, bool wide = false, unsigned height = 1, bool nested = false) {
    Fixture f;
    auto& b = f.bytes;
    u16(b,991); u32(b,1|256|65536|16777216|(wide?1024:0));
    u16(b,1); b.insert(b.end(),{3,static_cast<std::uint8_t>(height)}); // One 3-wide module.
    u16(b,nested?2:1); f.reference = b.size(); b.push_back(0);
    if (wide) { u16(b,static_cast<unsigned>(-300)&65535); u16(b,12); }
    else b.insert(b.end(),{254,255});
    b.push_back(0); // Frame module flags.
    if (nested) { b.push_back(1); u16(b,100); u16(b,200); b.push_back(16); }
    u16(b,1); b.insert(b.end(),{255,255,2,1}); // Collision rect.
    u16(b,nested?2:1); b.insert(b.end(),{1,0}); f.frameStart = b.size(); u16(b,nested?1:0);
    f.rectCount = b.size(); b.push_back(1);
    if (nested) { b.insert(b.end(),{1,0}); u16(b,0); b.push_back(0); }
    b.insert(b.end(),{254,255,3,static_cast<std::uint8_t>(height)});
    if (nested) b.insert(b.end(),{254,255,3,static_cast<std::uint8_t>(height)});
    u16(b,1); b.insert(b.end(),{0,2,253,4,1}); // Animation frame.
    u16(b,1); b.insert(b.end(),{1,0}); u16(b,0);
    u16(b,21781); b.insert(b.end(),{2,4});
    for (unsigned color : {0x8000,0xfc00,0x83e0,0x001f, 0xffff,0x8001,0x83e0,0x001f}) u16(b,color);
    u16(b,codec); u16(b,static_cast<unsigned>(encoded.size())); b.insert(b.end(),encoded.begin(),encoded.end());
    return f;
}
soul::BSprite parse(const Fixture& f) { return soul::BSprite::parse(soul::ByteView(f.bytes)); }
void customSprites() {
    const auto f = custom(1024,{0x1b}); const auto s = parse(f);
    require(s.modules().size() == 1 && s.frames().size() == 1 && s.animations().size() == 1, "metadata counts");
    require(s.decodeModule(0) == soul::Pixels({0xff000000,0xfff80000,0xff00f800}), "packed pixels and original RGB expansion");
    require(s.decodeModule(0,0,1) == soul::Pixels({0xff00f800,0xfff80000,0xff000000}), "horizontal flip");
    require(s.decodeModule(0,1)[0] == 0xfff8f8f8, "palette selection");
    require(s.frameBounds(0) == std::array<int,4>{-2,-1,1,0}, "stored visual bounds");
    require(s.frameBounds(0,0) == std::array<int,4>{-1,-1,1,0}, "collision bounds");
    require(s.frameBounds(0,1) == std::array<int,4>{0,0,0,0}, "missing collision rect");
    require(s.frameBounds(0,-1,3) == std::array<int,4>{-1,0,2,1}, "bounds flips");
    require(s.animationFrames()[0].duration == 2 && s.animationFrames()[0].x == -3, "animation ticks/signed offsets");
    require(s.animations()[0].start == 0 && s.animations()[0].count == 1, "animation slice");
    const auto wide = parse(custom(1024,{0x1b},true));
    require(wide.frameBounds(0) == std::array<int,4>{-300,0,0,13}, "wide bounds union includes origin");
    require(wide.frameBounds(0,-1,1) == std::array<int,4>{0,0,300,13}, "wide bounds flip");
    require(parse(custom(1024,{0x1b},true,1,true)).frameBounds(0) == wide.frameBounds(0), "nested bounds omit enclosing offset like original");
    const auto twoRows = parse(custom(1024,{0x1b,0x40},false,2));
    require(twoRows.decodeModule(0) == soul::Pixels({0xff000000,0xfff80000,0xff00f800,0x000000f8,0xfff80000,0xff000000}), "transparent RGB retained and packed padding ignored");
    require(twoRows.decodeModule(0,0,3) == soul::Pixels({0xff000000,0xfff80000,0x000000f8,0xff00f800,0xfff80000,0xff000000}), "both pixel flips");
    require(parse(custom(25840,{0,1,2})).decodeModule(0) == s.decodeModule(0), "packed run codec");
    require(parse(custom(25840,{8})).decodeModule(0) == soul::Pixels(3,0xff000000), "packed repeat count");
    require(parse(custom(22258,{131,0,1,2})).decodeModule(0) == s.decodeModule(0), "literal run codec");
    require(parse(custom(22258,{3,1})).decodeModule(0) == soul::Pixels(3,0xfff80000), "repeat run codec");
    require(parse(custom(22258,{0,0,3,1})).decodeModule(0) == soul::Pixels(3,0xfff80000), "zero run consumes input without pixels");
    for (std::size_t size = 0; size < f.bytes.size(); ++size)
        rejects([&] { soul::BSprite::parse(soul::ByteView(f.bytes).subview(0,size)); }, "every sprite truncation rejected");
    auto bad = f; bad.bytes[0] = 0; rejects([&] { parse(bad); }, "bad version");
    bad = f; bad.bytes[2] |= 8; rejects([&] { parse(bad); }, "unknown feature bit");
    bad = f; bad.bytes[bad.reference] = 1; rejects([&] { parse(bad); }, "invalid module reference");
    bad = f; bad.bytes[bad.reference+3] = 16; rejects([&] { parse(bad); }, "cyclic nested frame");
    bad = f; bad.bytes[bad.frameStart] = 2; rejects([&] { parse(bad); }, "invalid frame slice");
    bad = f; bad.bytes[bad.rectCount] = 128; rejects([&] { parse(bad); }, "negative collision count");
    bad = f; bad.bytes.push_back(0); rejects([&] { parse(bad); }, "sprite suffix");
    rejects([&] { parse(custom(25840,{12})); }, "RLE overrun");
    rejects([&] { parse(custom(25840,{0,1})); }, "RLE underrun");
    rejects([&] { parse(custom(22258,{4,0})); }, "repeat overrun");
    rejects([&] { parse(custom(22258,{131,0,1,4})); }, "invalid palette index");
    rejects([&] { parse(custom(1024,{0x1b,0})); }, "unused encoded pixels");
    rejects([&] { parse(custom(512,{0})); }, "unimplemented codec rejected");
    rejects([&] { s.decodeModule(0,0,4); }, "unsupported transform rejected");
    rejects<std::out_of_range>([&] { s.decodeModule(1); }, "invalid module selection");
    rejects<std::out_of_range>([&] { s.decodeModule(0,2); }, "invalid palette selection");
    rejects<std::out_of_range>([&] { s.frameBounds(1); }, "invalid frame selection");
}
void nestedFrames() {
    auto graph = [](unsigned count, unsigned branches) {
        soul::Bytes b; u16(b,991); u32(b,256|1024);
        u16(b,(count-1)*branches);
        for (unsigned i = 0; i + 1 < count; ++i) for (unsigned j = 0; j < branches; ++j) {
            b.push_back(static_cast<std::uint8_t>(i+1)); u16(b,0); u16(b,0); b.push_back(16);
        }
        u16(b,0); u16(b,count);
        for (unsigned i = 0; i < count; ++i) {
            b.push_back(static_cast<std::uint8_t>(i+1<count?branches:0)); b.push_back(0); u16(b,i*branches); b.push_back(0);
        }
        for (unsigned i = 0; i < count; ++i) b.insert(b.end(),{0,0,0,0});
        return b;
    };
    const auto valid = graph(64,1), deep = graph(65,1), shared = graph(24,2);
    require(soul::BSprite::parse(soul::ByteView(valid)).frameBounds(0) == std::array<int,4>{0,0,0,0}, "64-level nested bounds accepted");
    rejects([&] { soul::BSprite::parse(soul::ByteView(deep)); }, "excessive nesting rejected");
    require(soul::BSprite::parse(soul::ByteView(shared)).frameBounds(0) == std::array<int,4>{0,0,0,0}, "shared nested references use bounded traversal");
}
void be32(soul::Bytes& bytes, std::uint32_t value) {
    for (int shift : {24,16,8,0}) bytes.push_back(static_cast<std::uint8_t>(value >> shift));
}
void chunk(soul::Bytes& png, const char* type, const soul::Bytes& data) {
    be32(png,static_cast<std::uint32_t>(data.size())); const auto start = png.size();
    for (int i = 0; i < 4; ++i) png.push_back(static_cast<std::uint8_t>(type[i]));
    png.insert(png.end(),data.begin(),data.end());
    std::uint32_t crc = 0xffffffffU;
    for (std::size_t i = start; i < png.size(); ++i) {
        crc ^= png[i]; for (int bit = 0; bit < 8; ++bit) crc = (crc >> 1) ^ ((crc & 1) ? 0xedb88320U : 0);
    }
    be32(png,crc ^ 0xffffffffU);
}
soul::Bytes png(unsigned width = 2, bool badIndex = false) {
    soul::Bytes bytes{137,80,78,71,13,10,26,10}, header; be32(header,width); be32(header,1);
    header.insert(header.end(),{8,3,0,0,0}); chunk(bytes,"IHDR",header);
    chunk(bytes,"PLTE",{255,0,0,0,255,0}); chunk(bytes,"tRNS",{255,0});
    // zlib-compressed unfiltered scanline: 0,0,1 (or out-of-palette index 2).
    chunk(bytes,"IDAT",badIndex ? soul::Bytes{120,156,99,96,96,2,0,0,5,0,3} : soul::Bytes{120,156,99,96,96,4,0,0,4,0,2});
    chunk(bytes,"IEND",{}); return bytes;
}
void pngSprites() {
    const auto bytes = png(); const auto image = soul::decodeSpritePng(bytes);
    require(image.width == 2 && image.height == 1, "PNG dimensions");
    require(image.pixels == soul::Pixels({0xffff0000,0x0000ff00}), "indexed PNG preserves transparent RGB");
    for (std::size_t size = 0; size < bytes.size(); ++size) {
        soul::Bytes truncated(bytes.begin(),bytes.begin()+size);
        rejects([&] { soul::decodeSpritePng(truncated); }, "every PNG truncation rejected");
    }
    auto bad = bytes; bad[42] ^= 1; rejects([&] { soul::decodeSpritePng(bad); }, "PNG CRC corruption");
    bad = bytes; bad.push_back(0); rejects([&] { soul::decodeSpritePng(bad); }, "PNG suffix");
    rejects([] { soul::decodeSpritePng(png(0)); }, "zero PNG dimension");
    rejects([] { soul::decodeSpritePng(png(16000001)); }, "oversized PNG dimension");
    rejects([] { soul::decodeSpritePng(png(2,true)); }, "PNG palette index out of bounds");
    soul::Bytes sprite; u16(sprite,991); u32(sprite,536870912); u16(sprite,static_cast<unsigned>(bytes.size()));
    sprite.insert(sprite.end(),bytes.begin(),bytes.end()); sprite.insert(sprite.end(),{1,2});
    sprite.insert(sprite.end(),bytes.begin()+41,bytes.begin()+51); sprite.insert(sprite.end(),bytes.begin()+59,bytes.begin()+65);
    const auto s = soul::BSprite::parse(soul::ByteView(sprite));
    require(s.images().size() == 1 && s.images()[0][0].pixels == image.pixels, "palette-template PNG replacement");
    sprite.back() ^= 1; rejects([&] { soul::BSprite::parse(soul::ByteView(sprite)); }, "replacement CRC checked");
    soul::Bytes atlas; u16(atlas,991); u32(atlas,1|2|4|536870912); u16(atlas,1);
    atlas.insert(atlas.end(),{0,1,0,1,1}); // Group 0, atlas x=1/y=0, 1x1 module.
    atlas.push_back(1); u16(atlas,static_cast<unsigned>(bytes.size())); atlas.insert(atlas.end(),bytes.begin(),bytes.end()); atlas.push_back(0);
    const auto a = soul::BSprite::parse(soul::ByteView(atlas));
    require(a.decodeModule(0) == soul::Pixels({0x0000ff00}), "PNG atlas subrectangle");
    atlas[11] = 2; rejects([&] { soul::BSprite::parse(soul::ByteView(atlas)); }, "PNG atlas bounds");
}
}
int main() {
    try { customSprites(); nestedFrames(); pngSprites(); std::cout << "PASS: " << checks << " native sprite assertions (synthetic sprites/PNGs; no game data)\n"; return 0; }
    catch (const std::exception& error) { std::cerr << error.what() << '\n'; return 1; }
}
