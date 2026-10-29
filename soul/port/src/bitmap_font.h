#pragma once
#include "sprite_renderer.h"
#include <string_view>

namespace soul {
// Borrows the shipped font sprite (pack 1, resource 0). No global layout cache.
class BitmapFont {
public:
    explicit BitmapFont(const BSprite& sprite);
    BitmapFont(BSprite&&) = delete;
    BitmapFont(const BSprite&&) = delete;
    static int glyph(char16_t character);
    static std::u16string uppercase(std::u16string_view text);
    int height() const { return sprite_.modules()[1].height; }
    int lineHeight(bool prologue = false) const { return height()+5+(prologue ? 3 : 0); }
    // Reference metrics consume raw UTF-16; draw uppercases first.
    int measure(std::u16string_view text) const;
    std::vector<int> wrap(std::u16string_view text, int width, std::size_t palette = 0) const;
    // MIDP anchors: HCENTER=1, VCENTER=2, RIGHT=8, BOTTOM=32.
    // stateFrame is a simulation counter, never a presentation-frame counter.
    void draw(Canvas& target, std::u16string_view text, int x, int y,
        unsigned anchor = 0, std::size_t palette = 0, std::uint64_t stateFrame = 0,
        SpriteRenderer* icons = nullptr);
    int drawWrapped(Canvas& target, std::u16string_view text, int x, int y, int width,
        unsigned anchor = 0, std::size_t palette = 0, std::uint64_t stateFrame = 0,
        SpriteRenderer* icons = nullptr, bool prologue = false);
private:
    void drawLine(Canvas&, std::u16string_view, int, int, unsigned, std::size_t,
        std::size_t&, std::uint64_t, SpriteRenderer*);
    const BSprite& sprite_;
    SpriteRenderer renderer_;
};
}
