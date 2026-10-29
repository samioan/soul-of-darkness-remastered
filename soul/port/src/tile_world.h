#pragma once
#include "sprite_renderer.h"

namespace soul {
struct TileGrid {
    int width=0,height=0;
    Bytes cells,transforms;
    static TileGrid parse(ByteView cells,ByteView dimensions,ByteView transforms,bool collision=false);
    unsigned transform(std::size_t index) const;
};
struct LevelMaps {
    std::optional<TileGrid> collision;
    std::array<TileGrid,2> layers;
    static LevelMaps load(const ResourcePack& pack,bool gameplay);
    int width() const { return layers[1].width*20; }
    int height() const { return layers[1].height*20; }
    // Actor.fillCollisionTiles: cell centres inside the rectangle, clipped to grid.
    void fillCollisionRect(const std::array<int,4>&,std::uint8_t value);
};
struct TileBinding { int resource0,palette0,resource1,palette1; };
TileBinding tileBinding(int level);
// Visual layers stay immutable; gameplay owners can mutate the retained collision
// grid. Separate stored dimensions preserve the pack-9 mismatch.
class TileScene {
public:
    TileScene(LevelMaps maps,SpriteImage layer0,SpriteImage layer1);
    static TileScene load(const std::filesystem::path& root,int level);
    const LevelMaps& maps() const { return maps_; }
    LevelMaps& mutableMaps() { return maps_; }
    std::pair<int,int> clampCamera(int x,int y,int viewportWidth,int viewportHeight) const;
    // Camera is in world pixels; negative/unclamped positions are supported.
    // layerMask bit 0/1 selects layers; no clip or simulation state is changed.
    void draw(Canvas& target,int cameraX,int cameraY,unsigned layerMask=3) const;
private:
    LevelMaps maps_;
    std::array<std::vector<Pixels>,2> tiles_;
};
}
