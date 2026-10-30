#include "tile_world.h"
#include "level_descriptor.h"
#include <algorithm>

namespace soul {
void LevelMaps::fillCollisionRect(const std::array<int,4>& r,std::uint8_t value) {
    if(!collision || collision->width!=layers[1].width || collision->height!=layers[1].height ||
        collision->cells.size()!=std::size_t(collision->width)*collision->height)
        throw FormatError("Mutable collision rectangle requires matching stored and logical grids");
    const int left=std::max(0,(r[0]+10)/20),right=std::min(collision->width-1,(r[2]-10)/20);
    const int top=std::max(0,(r[1]+10)/20),bottom=std::min(collision->height-1,(r[3]-10)/20);
    for(int x=left;x<=right;++x)for(int y=top;y<=bottom;++y)collision->cells[std::size_t(y)*collision->width+x]=value;
}
namespace {
void require(bool condition,const char* message) { if (!condition) throw FormatError(message); }
ByteView resource(const ResourcePack& pack,std::size_t id) {
    const auto view=pack.resource(id); require(view.has_value(),"Missing map resource"); return *view;
}
std::int64_t floorTile(std::int64_t value) { return value>=0 ? value/20 : (value-19)/20; }
}
TileGrid TileGrid::parse(ByteView cells,ByteView dimensions,ByteView packed,bool collision) {
    BinaryReader dims(dimensions,"map dimensions");
    TileGrid grid; grid.width=dims.u16le(); grid.height=dims.u16le(); dims.done();
    const auto count=static_cast<std::size_t>(grid.width)*grid.height;
    require(count>0 && count<=16000000,"Invalid/oversized tile grid");
    require(cells.size()==count,"Tile cell count differs from dimensions");
    require(packed.size()==(count+3)/4,"Packed tile transform size differs from dimensions");
    grid.cells.assign(cells.data(),cells.data()+cells.size());
    if (collision) { for (auto& cell:grid.cells) if (cell==255) cell=0; }
    else grid.transforms.assign(packed.data(),packed.data()+packed.size());
    return grid;
}
unsigned TileGrid::transform(std::size_t index) const {
    if (index>=cells.size() || transforms.empty()) throw std::out_of_range("Tile transform index");
    return (transforms.at(index/4) >> ((3-(index&3))*2))&3;
}
LevelMaps LevelMaps::load(const ResourcePack& pack,bool gameplay) {
    LevelMaps result;
    auto grid=[&](int id,bool collision) { return TileGrid::parse(resource(pack,id),resource(pack,id+1),resource(pack,id+2),collision); };
    if (gameplay) result.collision=grid(2,true);
    result.layers[0]=grid(gameplay?5:0,false); result.layers[1]=grid(gameplay?8:3,false);
    require(result.layers[0].width==result.layers[1].width && result.layers[0].height==result.layers[1].height,"Unequal visual map dimensions");
    return result;
}
TileBinding tileBinding(int level) {
    // Original Game.bi/bj, consumed by Game.j(stage,level) from pack 16.
    constexpr int previewIds[]{3,7,11};
    if (level<0 || level>=12) throw std::out_of_range("Tile binding level");
    if(level<9){const auto d=LevelDescriptor::get(level);return {d.background0,0,d.background1,0};}
    return {previewIds[level-9],0,previewIds[level-9]+1,0};
}
TileScene::TileScene(LevelMaps maps,SpriteImage first,SpriteImage second) : maps_(std::move(maps)) {
    // Also validate callers constructing grids directly, before any indexing.
    for (const auto& grid:maps_.layers) {
        require(grid.width>0 && grid.height>0 && grid.cells.size()<=16000000 &&
            static_cast<std::size_t>(grid.width)*grid.height==grid.cells.size(),"Invalid visual tile grid");
        require(grid.transforms.size()==(grid.cells.size()+3)/4,"Invalid visual tile transforms");
    }
    require(maps_.layers[0].width==maps_.layers[1].width && maps_.layers[0].height==maps_.layers[1].height,"Unequal visual map dimensions");
    const SpriteImage* images[]{&first,&second};
    for (int layer=0;layer<2;++layer) {
        const auto& image=*images[layer];
        require(image.width>0 && image.height>0 && image.width%20==0 && image.height%20==0 &&
            image.pixels.size()<=16000000 && static_cast<std::uint64_t>(image.width)*image.height==image.pixels.size(),"Invalid tile atlas");
        const unsigned count=std::min(255u,(image.width/20)*(image.height/20));
        for (unsigned id=0;id<count;++id) {
            const unsigned x=(id*20)%image.width,y=(id*20)/image.width*20;
            Pixels pixels; pixels.reserve(400);
            for (unsigned row=0;row<20;++row) {
                const auto start=image.pixels.begin()+(y+row)*image.width+x;
                pixels.insert(pixels.end(),start,start+20);
            }
            tiles_[layer].push_back(std::move(pixels));
        }
        for (auto id:maps_.layers[layer].cells) require(id==255 || id<count,"Map tile outside its atlas");
    }
}
TileScene TileScene::load(const std::filesystem::path& root,int level) {
    if (level<0 || level>8) throw std::out_of_range("Tile preview level must be 0..8");
    const auto descriptor=LevelDescriptor::get(level);
    const auto pack=ResourcePack::numeric(readAssetFile(root/std::to_string(descriptor.pack)));
    auto maps=LevelMaps::load(pack,true); const auto binding=tileBinding(level);
    const auto backgrounds=ResourcePack::numeric(readAssetFile(root/"16"));
    const auto first=BSprite::parse(resource(backgrounds,binding.resource0));
    const auto second=BSprite::parse(resource(backgrounds,binding.resource1));
    return TileScene(std::move(maps),first.images().at(0).at(binding.palette0),second.images().at(0).at(binding.palette1));
}
std::pair<int,int> TileScene::clampCamera(int x,int y,int width,int height) const {
    if (width<=0 || height<=0) throw std::invalid_argument("Invalid camera viewport");
    return {std::clamp(x,0,std::max(0,maps_.width()-width)),std::clamp(y,0,std::max(0,maps_.height()-height))};
}
void TileScene::draw(Canvas& target,int cameraX,int cameraY,unsigned mask) const {
    if (mask>3) throw std::invalid_argument("Invalid tile layer mask");
    const auto clip=target.clip(); if (clip.left>=clip.right || clip.top>=clip.bottom) return;
    const auto& grid=maps_.layers[1];
    const int left=static_cast<int>(std::clamp<std::int64_t>(floorTile(static_cast<std::int64_t>(cameraX)+clip.left),0,grid.width));
    const int right=static_cast<int>(std::clamp<std::int64_t>(floorTile(static_cast<std::int64_t>(cameraX)+clip.right-1)+1,0,grid.width));
    const int top=static_cast<int>(std::clamp<std::int64_t>(floorTile(static_cast<std::int64_t>(cameraY)+clip.top),0,grid.height));
    const int bottom=static_cast<int>(std::clamp<std::int64_t>(floorTile(static_cast<std::int64_t>(cameraY)+clip.bottom-1)+1,0,grid.height));
    for (int y=top;y<bottom;++y) for (int x=left;x<right;++x) {
        const auto index=static_cast<std::size_t>(y)*grid.width+x;
        // Original per-cell composition is layer 0 followed by layer 1.
        for (int layer=0;layer<2;++layer) if (mask&(1u<<layer)) {
            const auto& g=maps_.layers[layer]; const auto id=g.cells[index];
            if (id!=255) target.blit(tiles_[layer][id],20,20,static_cast<std::int64_t>(x)*20-cameraX,
                static_cast<std::int64_t>(y)*20-cameraY,g.transform(index));
        }
    }
}
}
