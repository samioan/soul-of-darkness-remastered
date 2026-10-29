#pragma once
#include "resources.h"
#include <array>

namespace soul {
using Pixels = std::vector<std::uint32_t>;
struct SpriteImage { unsigned width = 0, height = 0; Pixels pixels; Bytes png; };
// CRC/format checks followed by Microsoft's WIC indexed scanline decoding.
SpriteImage decodeSpritePng(Bytes png);
std::uint64_t pixelFingerprint(const Pixels& pixels);

class BSprite {
public:
    struct Module { int width, height, x, y, image; };
    struct FrameModule { int module, x, y, flags; };
    struct Rect { int x, y, width, height; };
    struct Frame { int start, count, rectStart, rectCount; Rect visual; };
    struct AnimationFrame { int frame, duration, x, y, flags; };
    struct Animation { int start, count; };
    static BSprite parse(ByteView bytes, const std::string& label = "sprite");
    std::uint32_t flags() const { return flags_; }
    unsigned codec() const { return codec_; }
    const auto& modules() const { return modules_; }
    const auto& frameModules() const { return frameModules_; }
    const auto& collisionRects() const { return collisionRects_; }
    const auto& frames() const { return frames_; }
    const auto& animationFrames() const { return animationFrames_; }
    const auto& animations() const { return animations_; }
    const auto& palettes() const { return palettes_; }
    const auto& images() const { return images_; }
    std::size_t paletteCount(std::size_t module) const;
    Pixels decodeModule(std::size_t module, std::size_t palette = 0, unsigned flips = 0) const;
    std::array<int,4> frameBounds(std::size_t frame, int rect = -1, unsigned flips = 0) const;
private:
    std::array<int,4> bounds(std::size_t frame, int rect, unsigned flips, unsigned depth,
        std::vector<std::optional<std::array<int,4>>>& cache) const;
    std::uint32_t flags_ = 0;
    unsigned codec_ = 0;
    std::vector<Module> modules_;
    std::vector<FrameModule> frameModules_;
    std::vector<Rect> collisionRects_;
    std::vector<Frame> frames_;
    std::vector<AnimationFrame> animationFrames_;
    std::vector<Animation> animations_;
    std::vector<std::vector<Pixels>> palettes_;
    std::vector<Bytes> encoded_;
    std::vector<std::vector<SpriteImage>> images_;
};
}
