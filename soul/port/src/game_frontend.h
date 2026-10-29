#pragma once
#include "bitmap_font.h"
namespace soul {
// Native startup UI using original art/text. Start currently enters the gameplay slice.
struct FrontendState {
public:
    int state=1,frame=0,selection=0,scrollY=266,selectedLevel=0,maxUnlockedLevel=0;
    bool exitRequested=false,startRequested=false,restartRequested=false,continueRequested=false,newGameRequested=false,hasSave=false;
    std::u16string saveStatus;
    unsigned scrollKeys=0;
    void press(unsigned key); void release(unsigned key); void update(int creditsHeight,int storyHeight=0);
    void enter(int next) { state=next;frame=0;scrollKeys=0; }
};
class GameFrontend : public FrontendState {
public:
    explicit GameFrontend(const std::filesystem::path& data);
    GameFrontend(const GameFrontend&)=delete; GameFrontend& operator=(const GameFrontend&)=delete;
    void update(); void draw(Canvas&); void drawPause(Canvas&);
    const std::u16string& text(std::size_t i) const { return strings_.at(i); }
private:
    BSprite fontSprite_,titleSprite_; BitmapFont font_;
    SpriteImage logo_; std::vector<std::u16string> strings_;
    int creditsHeight_=0,storyHeight_=0;
};
}
