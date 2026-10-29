#include "tile_world.h"
#include <iostream>
#include <limits>
namespace {
int checks=0;
void require(bool b,const char* message) { ++checks; if (!b) throw std::runtime_error(message); }
template<class F> void rejects(F f,const char* message) {
    bool failed=false; try { f(); } catch(const soul::FormatError&) { failed=true; } require(failed,message);
}
template<class F> void range(F f,const char* message) {
    bool failed=false; try { f(); } catch(const std::out_of_range&) { failed=true; } require(failed,message);
}
soul::TileGrid grid(soul::Bytes cells,soul::Bytes dimensions,soul::Bytes packed,bool collision=false) {
    return soul::TileGrid::parse(soul::ByteView(cells),soul::ByteView(dimensions),soul::ByteView(packed),collision);
}
soul::SpriteImage image() {
    soul::SpriteImage result; result.width=result.height=20; result.pixels.resize(400);
    for (unsigned i=0;i<400;++i) result.pixels[i]=0xff000000|i;
    return result;
}
soul::LevelMaps maps() {
    soul::LevelMaps result;
    result.layers[0]=grid({0,0,0,0},{4,0,1,0},{0x1b});
    result.layers[1]=grid({255,255,255,255},{4,0,1,0},{0});
    result.collision=grid({255,24},{2,0,1,0},{0},true); return result;
}
}
int main() {
    try {
        auto m=maps();
        require(m.width()==80 && m.height()==20,"visual pixel dimensions");
        require(m.collision->cells==soul::Bytes({0,24}),"collision 255 normalized to zero");
        require(m.collision->width==2 && m.layers[0].width==4,"collision dimension mismatch preserved");
        for (unsigned i=0;i<4;++i) require(m.layers[0].transform(i)==i,"packed transforms highest pair first");
        range([&]{m.layers[0].transform(4);},"transform index checked");
        range([&]{m.collision->transform(0);},"unused collision companion not exposed as transforms");
        rejects([&]{grid({0},{0,0,1,0},{0});},"empty dimensions rejected");
        rejects([&]{grid({0},{1,0,1,0,0},{0});},"dimension trailer rejected");
        rejects([&]{grid({0,1},{1,0,1,0},{0});},"excess cells rejected");
        rejects([&]{grid({0},{2,0,1,0},{0});},"truncated cells rejected");
        rejects([&]{grid({0},{1,0,1,0},{});},"missing transforms rejected");
        rejects([&]{grid({0},{1,0,1,0},{0,0});},"excess transforms rejected");
        rejects([&]{grid({0},{255,255,255,255},{0});},"oversized grid rejected");
        soul::TileScene scene(m,image(),image()); soul::Canvas target(80,20);
        scene.draw(target,0,0);
        for (unsigned i=0;i<4;++i) {
            const unsigned sx=i&1 ? 19 : 0,sy=i&2 ? 19 : 0;
            require(target.pixels()[i*20]==(0xff000000|(sy*20+sx)),"tile transform corner");
        }
        target.clear(0); scene.draw(target,3,7);
        require(target.pixels()[0]==(0xff000000|143),"non-tile-aligned camera");
        target.clear(0); scene.draw(target,-13,-7);
        require(target.pixels()[7*80+13]==0xff000000 && target.pixels()[0]==0,"negative camera floor and offset");
        target.clear(0); target.setClip(2,3,1,1); scene.draw(target,0,0);
        require(target.pixels()[3*80+2]==(0xff000000|62) && target.pixels()[0]==0,"tile draw respects clip");
        target.resetClip(); target.clear(0); scene.draw(target,0,0,2);
        require(target.pixels()==soul::Pixels(1600,0),"255 is empty tile");
        for (int camera:{(std::numeric_limits<int>::min)(),(std::numeric_limits<int>::max)()}) scene.draw(target,camera,camera);
        require(target.pixels()==soul::Pixels(1600,0),"extreme camera safely culled");
        require(scene.clampCamera(100,100,569,320)==std::pair<int,int>{0,0},"small map clamps wide viewport to zero");
        require(scene.clampCamera(-1,100,40,10)==std::pair<int,int>{0,10},"camera edges clamp independently");
        auto overlay=image(); overlay.pixels[0]=0; overlay.pixels[1]=0xfff80000;
        m.layers[1].cells[0]=0; soul::TileScene composed(m,image(),overlay); target.clear(0); composed.draw(target,0,0);
        require(target.pixels()[0]==0xff000000 && target.pixels()[1]==0xfff80000,"layer order and transparency");
        target.clear(0); composed.draw(target,0,0,1);
        require(target.pixels()[1]==0xff000001,"layer mask selects lower layer");
        m.layers[1].cells[0]=1; rejects([&]{soul::TileScene invalid(m,image(),image());},"atlas tile range validated");
        m=maps(); m.layers[1].width=3; rejects([&]{soul::TileScene invalid(m,image(),image());},"invalid direct grid rejected");
        auto bad=image(); bad.width=19; rejects([&]{soul::TileScene invalid(maps(),bad,image());},"unaligned atlas rejected");
        bad=image(); bad.pixels.pop_back(); rejects([&]{soul::TileScene invalid(maps(),bad,image());},"atlas pixel count checked");
        range([&]{soul::tileBinding(-1);},"negative binding rejected");
        range([&]{soul::tileBinding(12);},"binding range rejected");
        require(soul::tileBinding(6).resource0==11 && soul::tileBinding(6).resource1==12,"level 6 atlas binding");
        std::cout << "PASS: " << checks << " tile assertions\n"; return 0;
    } catch(const std::exception& e) { std::cerr << e.what() << '\n'; return 1; }
}
