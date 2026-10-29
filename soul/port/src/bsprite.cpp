#include "bsprite.h"
#include <algorithm>
#include <functional>

namespace soul {
namespace {
void require(bool value, const std::string& message) { if (!value) throw FormatError(message); }
Bytes copy(ByteView view) { return view.size() ? Bytes(view.data(), view.data() + view.size()) : Bytes{}; }
BSprite::Rect readRect(BinaryReader& r) {
    const int x = r.i8(), y = r.i8(), w = r.u8(), h = r.u8();
    return {x,y,w,h};
}
Bytes patchPng(const Bytes& source, BinaryReader& reader, unsigned colors) {
    Bytes png = source;
    const std::size_t paletteSize = colors * 3 + 4, alphaStart = 49 + paletteSize, alphaSize = colors + 4;
    require(41 + paletteSize <= png.size() && alphaStart <= png.size() && alphaSize <= png.size() - alphaStart, "PNG replacement outside template");
    const auto palette = reader.take(paletteSize), alpha = reader.take(alphaSize);
    std::copy_n(palette.data(), paletteSize, png.begin() + 41);
    std::copy_n(alpha.data(), alphaSize, png.begin() + alphaStart);
    return png;
}
}
BSprite BSprite::parse(ByteView bytes, const std::string& label) {
    BinaryReader r(bytes, label);
    require(r.u16le() == 991, label + ": unsupported BSprite version");
    BSprite s; s.flags_ = r.u32le();
    constexpr std::uint32_t known = 1|2|4|256|1024|32768|65536|16777216|536870912;
    require((s.flags_ & ~known) == 0, label + ": unsupported sprite flags");
    require((s.flags_ & (16777216|536870912)) != (16777216|536870912), label + ": combined pixel/PNG storage unsupported");
    unsigned imageCount = 0;
    if (s.flags_ & 1) {
        imageCount = 1;
        const auto count = r.u16le();
        for (unsigned i = 0; i < count; ++i) {
            const int image = s.flags_ & 4 ? r.i8() : 0;
            require(image >= 0, label + ": negative image group");
            const int x = s.flags_ & 2 ? r.u8() : 0, y = s.flags_ & 2 ? r.u8() : 0;
            const int width = r.u8(), height = r.u8();
            s.modules_.push_back({width,height,x,y,image}); imageCount = std::max(imageCount, static_cast<unsigned>(image + 1));
        }
    }
    if (s.flags_ & 256) {
        const auto count = r.u16le();
        for (unsigned i = 0; i < count; ++i) {
            const int id = r.u8();
            const int x = s.flags_ & 1024 ? r.i16le() : r.i8(), y = s.flags_ & 1024 ? r.i16le() : r.i8();
            const int flags = r.u8(); s.frameModules_.push_back({id | ((flags & 192) << 2),x,y,flags});
        }
        const auto rects = r.u16le();
        for (unsigned i = 0; i < rects; ++i) s.collisionRects_.push_back(readRect(r));
        const auto frames = r.u16le(); int rectStart = 0;
        for (unsigned i = 0; i < frames; ++i) {
            const int modules = r.u8(); r.skip(1); const int start = r.u16le(), rectCount = r.i8();
            require(rectCount >= 0, label + ": negative collision count");
            s.frames_.push_back({start,modules,rectStart,rectCount,{0,0,0,0}}); rectStart += rectCount;
        }
        for (auto& frame : s.frames_) frame.visual = readRect(r);
        require(static_cast<std::size_t>(rectStart) == s.collisionRects_.size(), label + ": unused collision rectangles");
    }
    if (s.flags_ & 65536) {
        const auto count = r.u16le();
        for (unsigned i = 0; i < count; ++i) {
            const int frame = r.u8(), duration = r.u8(), x = r.i8(), y = r.i8(), flags = r.u8();
            s.animationFrames_.push_back({frame | ((flags & 192) << 2),duration,x,y,flags});
        }
        const auto animations = r.u16le();
        for (unsigned i = 0; i < animations; ++i) {
            const int frames = r.u8(); r.skip(1); const int start = r.u16le(); s.animations_.push_back({start,frames});
        }
    }
    if (s.flags_ & 16777216) {
        require(r.u16le() == 21781, label + ": unsupported palette encoding");
        for (unsigned group = 0; group < imageCount; ++group) {
            const auto count = r.u8(); const unsigned colorByte = r.u8(), colors = colorByte ? colorByte : 256;
            std::vector<Pixels> palettes;
            require(count > 0, label + ": empty palette group");
            for (unsigned p = 0; p < count; ++p) {
                Pixels palette;
                for (unsigned c = 0; c < colors; ++c) {
                    const unsigned color = r.u16le();
                    palette.push_back((color & 32768 ? 0xff000000U : 0) | ((color & 31744) << 9) | ((color & 992) << 6) | ((color & 31) << 3));
                }
                palettes.push_back(std::move(palette));
            }
            s.palettes_.push_back(std::move(palettes));
        }
        s.codec_ = r.u16le();
        // Only codecs present in this input have a native implementation yet.
        require(s.codec_ == 25840 || s.codec_ == 22258 || s.codec_ == 1024, label + ": unsupported native pixel codec");
        for (std::size_t i = 0; i < s.modules_.size(); ++i) { const auto length = r.u16le(); s.encoded_.push_back(copy(r.take(length))); }
    }
    std::size_t decodedPixels = 0;
    auto addImage = [&](std::vector<SpriteImage>& group, Bytes png) {
        auto image = decodeSpritePng(std::move(png));
        require(image.pixels.size() <= 16000000 - decodedPixels, label + ": sprite image budget exceeded");
        decodedPixels += image.pixels.size(); group.push_back(std::move(image));
    };
    if ((s.flags_ & 536870912) && !(s.flags_ & 4)) {
        const auto length = r.u16le(); const auto source = copy(r.take(length));
        const auto palettes = r.u8(); const unsigned colorByte = r.u8(), colors = colorByte ? colorByte : 256;
        require(palettes > 0, label + ": no PNG variants");
        std::vector<SpriteImage> group;
        for (unsigned p = 0; p < palettes; ++p) addImage(group, patchPng(source,r,colors));
        s.images_.push_back(std::move(group));
    }
    if ((s.flags_ & 4) && !(s.flags_ & 16777216)) {
        const auto groups = r.u8();
        for (unsigned g = 0; g < groups; ++g) {
            const auto length = r.u16le(); const auto source = copy(r.take(length)); const auto palettes = r.u8();
            std::vector<SpriteImage> group;
            if (!palettes) addImage(group,source);
            else {
                const unsigned colorByte = r.u8(), colors = colorByte ? colorByte : 256;
                for (unsigned p = 0; p < palettes; ++p) addImage(group,patchPng(source,r,colors));
            }
            s.images_.push_back(std::move(group));
        }
    }
    r.done();
    for (const auto& frame : s.frames_) require(static_cast<std::size_t>(frame.start + frame.count) <= s.frameModules_.size(), label + ": invalid frame module range");
    for (const auto& animation : s.animations_) require(static_cast<std::size_t>(animation.start + animation.count) <= s.animationFrames_.size(), label + ": invalid animation range");
    for (const auto& af : s.animationFrames_) require(static_cast<std::size_t>(af.frame) < s.frames_.size(), label + ": invalid animation frame");
    for (const auto& fm : s.frameModules_) require(static_cast<std::size_t>(fm.module) < (fm.flags & 16 ? s.frames_.size() : s.modules_.size()), label + ": invalid frame reference");
    std::vector<unsigned> visiting(s.frames_.size()), heights(s.frames_.size());
    std::function<void(std::size_t,unsigned)> visit = [&](std::size_t frame, unsigned depth) {
        require(depth < 64 && visiting[frame] != 1, label + ": cyclic/deep nested frame");
        if (visiting[frame] == 2) {
            require(depth + heights[frame] < 64, label + ": nested frame too deep"); return;
        }
        visiting[frame] = 1;
        const auto& f = s.frames_[frame];
        for (int i = 0; i < f.count; ++i) {
            const auto& fm = s.frameModules_[f.start + i];
            if (fm.flags & 16) { visit(fm.module,depth+1); heights[frame] = std::max(heights[frame],heights[fm.module]+1); }
        }
        visiting[frame] = 2;
    };
    for (std::size_t i = 0; i < s.frames_.size(); ++i) visit(i,0);
    for (std::size_t i = 0; i < s.modules_.size(); ++i) {
        const auto& module = s.modules_[i];
        if (!s.images_.empty()) {
            const auto group = s.flags_ & 4 ? module.image : 0;
            require(static_cast<std::size_t>(group) < s.images_.size(), label + ": missing PNG group");
            for (const auto& image : s.images_[group]) require(static_cast<unsigned>(module.x + module.width) <= image.width && static_cast<unsigned>(module.y + module.height) <= image.height, label + ": module outside PNG");
        }
        for (std::size_t p = 0; p < s.paletteCount(i); ++p) {
            const auto count = static_cast<std::size_t>(module.width) * module.height;
            require(count <= 16000000 - decodedPixels, label + ": sprite decode budget exceeded");
            decodedPixels += count; s.decodeModule(i,p);
        }
    }
    return s;
}
std::size_t BSprite::paletteCount(std::size_t module) const {
    const auto& m = modules_.at(module); const auto group = flags_ & 4 ? m.image : 0;
    if (!images_.empty()) return images_.at(group).size();
    require(static_cast<std::size_t>(group) < palettes_.size(), "Missing module image storage");
    return palettes_[group].size();
}
Pixels BSprite::decodeModule(std::size_t module, std::size_t palette, unsigned flips) const {
    require(flips < 4, "Unsupported module transform");
    const auto& m = modules_.at(module); const auto group = flags_ & 4 ? m.image : 0;
    const auto count = static_cast<std::size_t>(m.width) * m.height;
    Pixels pixels;
    if (!images_.empty()) {
        const auto& image = images_.at(group).at(palette); pixels.reserve(count);
        for (int y = 0; y < m.height; ++y) for (int x = 0; x < m.width; ++x) pixels.push_back(image.pixels[(m.y+y)*image.width+m.x+x]);
    } else {
        require(count <= 12288, "Module exceeds original pixel scratch buffer");
        const auto& colors = palettes_.at(group).at(palette); BinaryReader r{ByteView(encoded_.at(module)), "module pixels"};
        unsigned bits = 0; for (auto n = colors.size() - 1; n; n >>= 1) ++bits;
        auto append = [&](unsigned index, std::size_t run) {
            require(index < colors.size(), "Module palette index out of bounds");
            require(run <= count - pixels.size(), "Module run exceeds dimensions");
            pixels.insert(pixels.end(),run,colors[index]);
        };
        while (pixels.size() < count) {
            const unsigned value = r.u8();
            if (codec_ == 25840) append(value & ((1U << bits)-1), (value >> bits)+1);
            else if (codec_ == 22258) {
                if (value > 127) { for (unsigned i = 0; i < value - 128; ++i) append(r.u8(),1); }
                else { const auto index = r.u8(); append(index,value); }
            } else if (codec_ == 1024) {
                for (int shift : {6,4,2,0}) {
                    const unsigned index = (value >> shift) & 3;
                    require(index < colors.size(), "Packed palette index out of bounds");
                    if (pixels.size() < count) pixels.push_back(colors[index]);
                }
            } else throw FormatError("Unsupported native pixel codec");
        }
        r.done();
    }
    Pixels output(count);
    for (int y = 0; y < m.height; ++y) for (int x = 0; x < m.width; ++x)
        output[static_cast<std::size_t>(y)*m.width+x] = pixels[static_cast<std::size_t>(flips & 2 ? m.height-1-y : y)*m.width+(flips & 1 ? m.width-1-x : x)];
    return output;
}
std::array<int,4> BSprite::frameBounds(std::size_t frame, int rect, unsigned flips) const {
    require(rect >= -1 && flips < 4, "Unsupported bounds selector/transform");
    std::vector<std::optional<std::array<int,4>>> cache(frames_.size());
    return bounds(frame,rect,flips,0,cache);
}
std::array<int,4> BSprite::bounds(std::size_t frame, int rect, unsigned flips, unsigned depth,
    std::vector<std::optional<std::array<int,4>>>& cache) const {
    require(depth < 64, "Nested frame bounds too deep");
    const auto& f = frames_.at(frame); Rect r = f.visual;
    if (rect != -1) {
        if (rect >= f.rectCount) return {0,0,0,0};
        r = collisionRects_.at(f.rectStart + rect);
    } else if (cache[frame]) {
        const auto& box = *cache[frame]; r = {box[0],box[1],box[2]-box[0],box[3]-box[1]};
    } else if (flags_ & 1024) {
        int left = 0, top = 0, right = 0, bottom = 0;
        for (int i = 0; i < f.count; ++i) {
            const auto& fm = frameModules_[f.start+i]; std::array<int,4> box;
            if (fm.flags & 16) box = bounds(fm.module,-1,0,depth+1,cache);
            else { const auto& m = modules_[fm.module]; box = {fm.x,fm.y,fm.x+m.width,fm.y+m.height}; }
            left = std::min(left,box[0]); top = std::min(top,box[1]); right = std::max(right,box[2]); bottom = std::max(bottom,box[3]);
        }
        r = {left,top,right-left,bottom-top};
    }
    if (rect == -1) cache[frame] = {r.x,r.y,r.x+r.width,r.y+r.height};
    if (flips & 1) r.x = -r.x-r.width;
    if (flips & 2) r.y = -r.y-r.height;
    return {r.x,r.y,r.x+r.width,r.y+r.height};
}
}
