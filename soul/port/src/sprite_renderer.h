#pragma once
#include "bsprite.h"
#include <map>

namespace soul {
// Straight ARGB software surface with half-open clips and source-over blending.
class Canvas {
public:
    struct Clip { int left, top, right, bottom; };
    Canvas(int width, int height, std::uint32_t color = 0) { resize(width,height,color); }
    void resize(int width, int height, std::uint32_t color = 0);
    int width() const { return width_; }
    int height() const { return height_; }
    const Pixels& pixels() const { return pixels_; }
    Pixels& pixels() { return pixels_; }
    void clear(std::uint32_t color);
    void resetClip() { clip_ = {0,0,width_,height_}; }
    void setClip(int x, int y, int width, int height);
    void intersectClip(int x, int y, int width, int height);
    Clip clip() const { return clip_; }
    bool visible(std::int64_t x, std::int64_t y, int width, int height) const;
    void blit(const Pixels& source, int width, int height, std::int64_t x, std::int64_t y, unsigned flips = 0);
    // Canonical big-endian ARGB CRC32 for render comparisons, not authenticity.
    std::uint32_t checksum() const;
private:
    int width_ = 0, height_ = 0;
    Pixels pixels_;
    Clip clip_{};
};
struct DrawOptions {
    std::size_t palette = 0;
    unsigned flips = 0;
    // Empty disables masking; 1 hides a local frame-module slot, as in BSprite.
    std::vector<std::uint8_t> moduleMask;
};

// Sprite must outlive this renderer. Cache entries are unflipped pixel data.
// Drawing never changes the sprite, animation step, surface clip or game state.
class SpriteRenderer {
public:
    explicit SpriteRenderer(const BSprite& sprite) : sprite_(sprite) {}
    SpriteRenderer(BSprite&&) = delete;
    SpriteRenderer(const BSprite&&) = delete;
    void drawModule(Canvas& target, std::size_t module, int x, int y, const DrawOptions& options = {});
    void drawFrame(Canvas& target, std::size_t frame, int x, int y, const DrawOptions& options = {});
    void drawAnimation(Canvas& target, std::size_t animation, std::size_t step, int x, int y, const DrawOptions& options = {});
    void drawImage(Canvas& target, std::size_t group, std::size_t palette, int x, int y, unsigned flips = 0) const;
private:
    void module(Canvas& target, std::size_t index, std::int64_t x, std::int64_t y, std::size_t palette, unsigned flips);
    void frame(Canvas& target, std::size_t index, std::int64_t x, std::int64_t y, unsigned flips,
        const DrawOptions& options, unsigned depth, std::size_t& visits);
    const BSprite& sprite_;
    std::map<std::pair<std::size_t,std::size_t>,Pixels> cache_;
};
}
