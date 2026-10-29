#include "sprite_renderer.h"
#include <algorithm>

namespace soul {
namespace {
void require(bool result, const char* label) { if (!result) throw std::invalid_argument(label); }
std::uint32_t blend(std::uint32_t source, std::uint32_t destination) {
    const unsigned alpha = source >> 24;
    if (!alpha) return destination;
    if (alpha == 255) return source;
    const unsigned da = destination >> 24, inverse = 255-alpha;
    const unsigned denominator = alpha*255 + da*inverse;
    std::uint32_t result = ((denominator+127)/255) << 24;
    for (int shift : {16,8,0}) {
        const unsigned numerator = ((source >> shift)&255)*alpha*255 + ((destination >> shift)&255)*da*inverse;
        result |= ((numerator+denominator/2)/denominator) << shift;
    }
    return result;
}
}
void Canvas::resize(int width, int height, std::uint32_t color) {
    require(width > 0 && height > 0 && width <= 16000000 / height, "Invalid/oversized canvas");
    Pixels replacement(static_cast<std::size_t>(width)*height,color);
    pixels_.swap(replacement); width_ = width; height_ = height; resetClip();
}
void Canvas::clear(std::uint32_t color) { std::fill(pixels_.begin(),pixels_.end(),color); }
void Canvas::setClip(int x, int y, int width, int height) {
    if (width <= 0 || height <= 0) { clip_ = {0,0,0,0}; return; }
    auto clamp = [](std::int64_t value, int maximum) { return static_cast<int>(std::clamp<std::int64_t>(value,0,maximum)); };
    clip_ = {clamp(x,width_),clamp(y,height_),clamp(static_cast<std::int64_t>(x)+width,width_),clamp(static_cast<std::int64_t>(y)+height,height_)};
}
void Canvas::intersectClip(int x, int y, int width, int height) {
    const auto previous = clip_; setClip(x,y,width,height);
    clip_ = {std::max(previous.left,clip_.left),std::max(previous.top,clip_.top),std::min(previous.right,clip_.right),std::min(previous.bottom,clip_.bottom)};
    if (clip_.left >= clip_.right || clip_.top >= clip_.bottom) clip_ = {0,0,0,0};
}
bool Canvas::visible(std::int64_t x, std::int64_t y, int width, int height) const {
    return width > 0 && height > 0 && clip_.left < clip_.right && clip_.top < clip_.bottom
        && x < clip_.right && y < clip_.bottom
        && x > static_cast<std::int64_t>(clip_.left)-width && y > static_cast<std::int64_t>(clip_.top)-height;
}
void Canvas::blit(const Pixels& source, int width, int height, std::int64_t x, std::int64_t y, unsigned flips) {
    require(width >= 0 && height >= 0 && flips < 4, "Invalid blit dimensions/transform");
    require(static_cast<std::uint64_t>(width)*height == source.size(), "Blit pixel count differs from dimensions");
    // Self-overlapping blits would otherwise depend on traversal order.
    require(&source != &pixels_, "Aliased surface blit is unsupported");
    if (!visible(x,y,width,height)) return;
    const int left = static_cast<int>(std::max<std::int64_t>(clip_.left,x));
    const int top = static_cast<int>(std::max<std::int64_t>(clip_.top,y));
    const int right = static_cast<int>(std::min<std::int64_t>(clip_.right,x+width));
    const int bottom = static_cast<int>(std::min<std::int64_t>(clip_.bottom,y+height));
    for (int row = top; row < bottom; ++row) {
        const int sy = static_cast<int>(row-y);
        const auto sourceRow = static_cast<std::size_t>(flips & 2 ? height-1-sy : sy)*width;
        for (int col = left; col < right; ++col) {
            const int sx = static_cast<int>(col-x);
            const auto pixel = source[sourceRow+(flips & 1 ? width-1-sx : sx)];
            auto& destination = pixels_[static_cast<std::size_t>(row)*width_+col];
            destination = blend(pixel,destination);
        }
    }
}
std::uint32_t Canvas::checksum() const {
    static const auto table = [] {
        std::array<std::uint32_t,256> values{};
        for (unsigned i = 0; i < 256; ++i) {
            auto value = i;
            for (int bit = 0; bit < 8; ++bit) value = (value >> 1) ^ ((value & 1) ? 0xedb88320U : 0);
            values[i] = value;
        }
        return values;
    }();
    std::uint32_t crc = 0xffffffffU;
    for (auto pixel : pixels_) for (int shift : {24,16,8,0}) crc = (crc >> 8) ^ table[(crc ^ (pixel >> shift)) & 255];
    return crc ^ 0xffffffffU;
}
void SpriteRenderer::module(Canvas& target, std::size_t index, std::int64_t x, std::int64_t y, std::size_t palette, unsigned flips) {
    const auto& m = sprite_.modules().at(index);
    if (palette >= sprite_.paletteCount(index)) throw std::out_of_range("Invalid sprite palette");
    if (!target.visible(x,y,m.width,m.height)) return;
    const auto key = std::make_pair(index,palette);
    auto found = cache_.find(key);
    if (found == cache_.end()) found = cache_.emplace(key,sprite_.decodeModule(index,palette)).first;
    target.blit(found->second,m.width,m.height,x,y,flips);
}
void SpriteRenderer::drawModule(Canvas& target, std::size_t index, int x, int y, const DrawOptions& options) {
    require(options.flips < 4, "Invalid sprite transform");
    module(target,index,x,y,options.palette,options.flips);
}
void SpriteRenderer::frame(Canvas& target, std::size_t index, std::int64_t x, std::int64_t y, unsigned flips,
    const DrawOptions& options, unsigned depth, std::size_t& visits) {
    if (depth >= 64 || ++visits > 1000000) throw FormatError("Sprite drawing recursion/work budget exceeded");
    const auto& f = sprite_.frames().at(index);
    require(options.moduleMask.empty() || options.moduleMask.size() >= static_cast<std::size_t>(f.count), "Frame module mask too short");
    for (int slot = 0; slot < f.count; ++slot) {
        if (!options.moduleMask.empty() && options.moduleMask[slot] == 1) continue;
        const auto& fm = sprite_.frameModules()[f.start+slot];
        auto px = x + (flips & 1 ? -fm.x : fm.x), py = y + (flips & 2 ? -fm.y : fm.y);
        const auto childFlips = flips ^ (static_cast<unsigned>(fm.flags)&3);
        if (fm.flags & 16) frame(target,fm.module,px,py,childFlips,options,depth+1,visits);
        else {
            const auto& m = sprite_.modules()[fm.module];
            if (flips & 1) px -= m.width;
            if (flips & 2) py -= m.height;
            module(target,fm.module,px,py,options.palette,childFlips);
        }
    }
}
void SpriteRenderer::drawFrame(Canvas& target, std::size_t index, int x, int y, const DrawOptions& options) {
    require(options.flips < 4, "Invalid sprite transform");
    std::size_t visits = 0; frame(target,index,x,y,options.flips,options,0,visits);
}
void SpriteRenderer::drawAnimation(Canvas& target, std::size_t animation, std::size_t step, int x, int y, const DrawOptions& options) {
    require(options.flips < 4, "Invalid sprite transform");
    const auto& a = sprite_.animations().at(animation);
    if (step >= static_cast<std::size_t>(a.count)) throw std::out_of_range("Invalid animation step");
    const auto& af = sprite_.animationFrames()[a.start+step];
    const auto px = static_cast<std::int64_t>(x)+(options.flips & 1 ? -af.x : af.x);
    const auto py = static_cast<std::int64_t>(y)+(options.flips & 2 ? -af.y : af.y);
    std::size_t visits = 0; frame(target,af.frame,px,py,options.flips^(static_cast<unsigned>(af.flags)&3),options,0,visits);
}
void SpriteRenderer::drawImage(Canvas& target, std::size_t group, std::size_t palette, int x, int y, unsigned flips) const {
    const auto& image = sprite_.images().at(group).at(palette);
    target.blit(image.pixels,static_cast<int>(image.width),static_cast<int>(image.height),x,y,flips);
}
}
