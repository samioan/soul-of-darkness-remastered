#include "render_probe.h"
#include "sprite_renderer.h"
#include <algorithm>
#include <iomanip>
#include <iostream>
#include <sstream>

namespace {
std::string checksum(const soul::Canvas& target) {
    std::ostringstream out; out << std::hex << std::setfill('0') << std::setw(8) << target.checksum(); return out.str();
}
}
void emitRenderManifest(const std::filesystem::path& data) {
    for (int packId : {1,2,16}) {
        const auto pack = soul::ResourcePack::numeric(soul::readAssetFile(data/std::to_string(packId)));
        for (std::size_t resource = 0; resource < pack.size(); ++resource) {
            const auto bytes = pack.resource(resource); if (!bytes) continue;
            const auto sprite = soul::BSprite::parse(*bytes); soul::SpriteRenderer renderer(sprite);
            std::size_t palettes = 1, maskSize = 0;
            if (!sprite.modules().empty()) {
                palettes = sprite.paletteCount(0);
                for (std::size_t m = 1; m < sprite.modules().size(); ++m) palettes = std::min(palettes,sprite.paletteCount(m));
            }
            for (const auto& frame : sprite.frames()) maskSize = std::max(maskSize,static_cast<std::size_t>(frame.count));
            for (int width : {240,569}) {
                soul::Canvas target(width,320);
                auto prepare = [&](int variant) {
                    target.clear(0xff102030); target.resetClip();
                    if (variant == 1) target.setClip(13,17,width-31,273);
                    return std::pair<int,int>{variant == 1 ? 2 : variant == 2 ? width-3 : width/2, variant == 1 ? 2 : variant == 2 ? 319 : 160};
                };
                for (std::size_t frame = 0; frame < sprite.frames().size(); ++frame) for (std::size_t palette = 0; palette < palettes; ++palette) for (unsigned flip = 0; flip < 4; ++flip) {
                    for (int variant = 0; variant < 4; ++variant) {
                        if (variant && frame != 0 && frame+1 != sprite.frames().size()) continue;
                        const auto position = prepare(variant); soul::DrawOptions options{palette,flip,{}};
                        if (variant == 3) {
                            options.moduleMask.resize(maskSize);
                            for (std::size_t i = 0; i < maskSize; ++i) options.moduleMask[i] = i%2 == 0 ? 1 : 0;
                        }
                        renderer.drawFrame(target,frame,position.first,position.second,options);
                        std::cout << "F\t" << packId << '\t' << resource << '\t' << width << '\t' << frame << '\t' << palette << '\t' << flip << '\t' << variant << '\t' << checksum(target) << '\n';
                    }
                }
                for (std::size_t animation = 0; animation < sprite.animations().size(); ++animation) for (int step = 0; step < sprite.animations()[animation].count; ++step) for (unsigned flip = 0; flip < 4; ++flip) {
                    const auto position = prepare(0);
                    renderer.drawAnimation(target,animation,step,position.first,position.second,{0,flip,{}});
                    std::cout << "A\t" << packId << '\t' << resource << '\t' << width << '\t' << animation << '\t' << step << '\t' << flip << '\t' << checksum(target) << '\n';
                }
                if (sprite.frames().empty()) for (std::size_t group = 0; group < sprite.images().size(); ++group) for (std::size_t palette = 0; palette < sprite.images()[group].size(); ++palette) for (unsigned flip = 0; flip < 4; ++flip) {
                    for (int variant = 0; variant < 3; ++variant) {
                        const auto position = prepare(variant);
                        renderer.drawImage(target,group,palette,position.first,position.second,flip);
                        std::cout << "I\t" << packId << '\t' << resource << '\t' << width << '\t' << group << '\t' << palette << '\t' << flip << '\t' << variant << '\t' << checksum(target) << '\n';
                    }
                }
            }
        }
    }
}
