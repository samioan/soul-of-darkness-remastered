#include "font_probe.h"
#include "bitmap_font.h"
#include <iomanip>
#include <iostream>
#include <sstream>

void emitFontManifest(const std::filesystem::path& root) {
    const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(root/"1"));
    const auto sprite=soul::BSprite::parse(pack.resource(0).value());
    soul::BitmapFont font(sprite);
    for (char16_t c=32;c<256;++c) std::cout << "G\t" << static_cast<int>(c) << '\t' << font.glyph(c) << '\n';
    const std::u16string texts[]{u"",u"SOUL OF DARKNESS",u"{SOUL} OF DARKNESS",u"~0 12345",u"HELLO^WORLD",
        u"ONE TWO THREE FOUR FIVE SIX SEVEN EIGHT NINE TEN",u"ABCDEFGHIJKLMNO",u"A^^B^",u"~0 ~1 ~2 ~3 ~4",u"{A B C} D E",u"lowercase"};
    for (const auto& text:texts) {
        std::ostringstream hex; for (auto c:text) hex << std::hex << std::setfill('0') << std::setw(4) << static_cast<int>(c);
        for (std::size_t palette:{0,5}) for (int width:{0,40,120,240,569}) {
            std::cout << "F\t" << hex.str() << '\t' << palette << '\t' << width << '\t' << font.measure(text);
            for (auto end:font.wrap(text,width,palette)) std::cout << '\t' << end;
            std::cout << '\n';
        }
    }
    const auto gameplay=soul::ResourcePack::numeric(soul::readAssetFile(root/"2"));
    // Game.ag opens pack 2 and calls loadSprite(33).
    const auto icons=soul::BSprite::parse(gameplay.resource(33).value());
    soul::SpriteRenderer iconRenderer(icons);
    for (int width:{240,569}) for (std::size_t palette:{0,1,2}) for (unsigned anchor:{0u,1u,8u,32u,3u})
        for (bool wrapped:{false,true}) for (int tick:{0,1,4,17}) {
            soul::Canvas target(width,320,0xff102030); target.setClip(7,11,width-19,288);
            const auto text=u"Soul {of} darkness ~0 ~1 ~2 ~3";
            if (wrapped) font.drawWrapped(target,text,width/2,80,width-32,anchor,palette,tick,&iconRenderer);
            else font.draw(target,text,width/2,80,anchor,palette,tick,&iconRenderer);
            std::cout << "R\t" << width << '\t' << palette << '\t' << anchor << '\t' << wrapped << '\t' << tick << '\t' << target.checksum() << '\n';
        }
}
