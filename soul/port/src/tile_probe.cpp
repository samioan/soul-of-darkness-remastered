#include "tile_probe.h"
#include "tile_world.h"
#include <iomanip>
#include <iostream>
#include <sstream>
namespace {
std::string hash(const soul::Bytes& bytes) {
    std::ostringstream out; out << std::hex << std::setfill('0') << std::setw(16) << soul::fingerprint(soul::ByteView(bytes)); return out.str();
}
}
void emitTileManifest(const std::filesystem::path& root) {
    for (int pack=3;pack<=14;++pack) {
        const auto resources=soul::ResourcePack::numeric(soul::readAssetFile(root/std::to_string(pack)));
        const auto maps=soul::LevelMaps::load(resources,pack<=11);
        auto row=[&](int index,const soul::TileGrid& grid) {
            std::cout << "M\t" << pack << '\t' << index << '\t' << grid.width << '\t' << grid.height << '\t' << hash(grid.cells)
                << '\t' << (index==0?"-":hash(grid.transforms)) << '\n';
        };
        if (maps.collision) row(0,*maps.collision);
        row(1,maps.layers[0]); row(2,maps.layers[1]);
    }
    for (int level=0;level<12;++level) {
        const auto b=soul::tileBinding(level);
        std::cout << "B\t" << level << '\t' << b.resource0 << '\t' << b.palette0 << '\t' << b.resource1 << '\t' << b.palette1 << '\n';
    }
    for (int level=0;level<9;++level) {
        const auto scene=soul::TileScene::load(root,level);
        for (int width:{240,569}) for (int position=0;position<3;++position) for (int clipped:{0,1}) for (unsigned mask:{1u,2u,3u}) {
            int x=-13,y=-7;
            if (position==1) { x=scene.maps().width()/2-11; y=scene.maps().height()/2-9; }
            else if (position==2) { x=scene.maps().width()-width+17; y=scene.maps().height()-320+13; }
            soul::Canvas target(width,320,0xff102030);
            if (clipped) target.setClip(13,17,width-31,273);
            scene.draw(target,x,y,mask);
            std::cout << "R\t" << level << '\t' << width << '\t' << position << '\t' << clipped << '\t' << mask << '\t' << target.checksum() << '\n';
        }
    }
}
