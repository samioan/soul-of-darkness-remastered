#include "game_frontend.h"
#include "english_text.h"
#include <algorithm>
namespace soul {
namespace {
BSprite sprite(const std::filesystem::path& root,int resource){auto p=ResourcePack::numeric(readAssetFile(root/"1"));return BSprite::parse(p.resource(resource).value());}
std::vector<std::u16string> strings(const std::filesystem::path& root){
    const auto p=ResourcePack::numeric(readAssetFile(root/"15"));BinaryReader r(p.resource(0).value());r.skip(6);const int count=r.u16be();std::vector<std::u16string> out;
    for(int i=0;i<count;++i){const int length=r.u16be();BinaryReader utf(r.take(length));std::u16string s;
        while(utf.remaining()){
            unsigned c=utf.u8();
            if(c>=0xc0 && c<0xe0){const unsigned b=utf.u8();if((b&0xc0)!=0x80)throw FormatError("Invalid modified UTF-8");c=((c&31)<<6)|(b&63);}
            else if(c>=0xe0 && c<0xf0){const unsigned b=utf.u8(),d=utf.u8();if((b&0xc0)!=0x80 || (d&0xc0)!=0x80)throw FormatError("Invalid modified UTF-8");c=((c&15)<<12)|((b&63)<<6)|(d&63);}
            else if(c>=0x80)throw FormatError("Invalid modified UTF-8 lead");
            s.push_back(static_cast<char16_t>(c));
        }
        while(!s.empty() && s.back()<=32)s.pop_back();const auto first=s.find_first_not_of(u" \t\r\n");if(first!=std::u16string::npos)s.erase(0,first);
        out.push_back(s);
    }r.done();if(out.size()<=42)throw FormatError("Incomplete common strings");return out;
}
void box(Canvas& c,int x,int y,int w,int h,std::uint32_t color){c.blit(Pixels(static_cast<std::size_t>(w)*h,color),w,h,x,y);}
}
GameFrontend::GameFrontend(const std::filesystem::path& data):fontSprite_(sprite(data,0)),titleSprite_(sprite(data,1)),font_(fontSprite_),logo_(decodeSpritePng(readAssetFile(data/"logo.png"))),strings_(strings(data)){
    applyEnglishFrontendText(strings_);
    for(auto& text:strings_)text=BitmapFont::uppercase(text);
    auto& credits=strings_[28];const auto token=credits.find(u"X.X.X");if(token!=std::u16string::npos)credits.replace(token,5,u" 1.3.7");
    creditsHeight_=static_cast<int>(font_.wrap(std::u16string(u"}")+credits,230).size())*font_.lineHeight();
    storyHeight_=static_cast<int>(font_.wrap(std::u16string(u"}")+strings_[40],230).size())*font_.lineHeight(true);
}
void FrontendState::press(unsigned key){
    if(state==1)return;
    if(state==18){enter(2);return;}
    if(state==6 || state==20){
        if(key==38 || key=='W')scrollKeys|=1;
        if(key==40 || key=='S')scrollKeys|=2;
        if(key==27){enter(2);return;}
        if(key==13 || key==32 || key=='E')enter(state==20?19:2);
        return;
    }
    if(state==19){
        if(key==27)enter(2);
        else if(key==37 || key=='A')selectedLevel=std::max(0,selectedLevel-1);
        else if(key==39 || key=='D')selectedLevel=std::min(std::min(8,maxUnlockedLevel),selectedLevel+1);
        else if((key==13 || key==32 || key=='E') && selectedLevel==0)enter(9);
        return;
    }
    if(state==8){if(key==27){selection=0;enter(21);}return;}
    if(state==21){
        if(key==27){enter(8);return;}
        if(key==38 || key=='W')selection=(selection+2)%3;
        if(key==40 || key=='S')selection=(selection+1)%3;
        if(key==13 || key==32 || key=='E'){
            if(selection==0)enter(8);
            else if(selection==1){restartRequested=true;enter(9);}
            else {selection=0;enter(2);}
        }
        return;
    }
    if(state==22){
        if(key==27){enter(2);return;}
        if(key==13){newGameRequested=true;continueRequested=restartRequested=false;scrollY=240;enter(20);}return;
    }
    if(state!=2)return;
    const int count=hasSave?4:3;
    if(key==38 || key=='W')selection=(selection+count-1)%count;
    if(key==40 || key=='S')selection=(selection+1)%count;
    if(key==27){enter(18);return;}
    if(key==13 || key==32 || key=='E'){
        if(selection==0){if(hasSave){enter(22);return;}newGameRequested=true;continueRequested=restartRequested=false;scrollY=240;enter(20);}
        else if(hasSave && selection==1){continueRequested=true;restartRequested=false;enter(9);}
        else if(selection==(hasSave?2:1)){scrollY=266;enter(6);}
        else exitRequested=true;
    }
}
void FrontendState::release(unsigned key){
    if(key==38 || key=='W')scrollKeys&=~1u;
    if(key==40 || key=='S')scrollKeys&=~2u;
}
void FrontendState::update(int creditsHeight,int storyHeight){
    ++frame;
    if(state==1 && frame>=47)enter(18);
    if(state==9)startRequested=true;
    if(state==6 || state==20){
        const int height=state==20?storyHeight:creditsHeight;
        if(scrollY+height<0)scrollY=266;else --scrollY;
        if(scrollKeys)scrollY-=4;
    }
}
void GameFrontend::update(){FrontendState::update(creditsHeight_,storyHeight_);}
void GameFrontend::draw(Canvas& c){
    c.clear(state==1?0xffffffff:0xff000000);const int left=(c.width()-240)/2,cx=c.width()/2;
    if(state==1){c.blit(logo_.pixels,logo_.width,logo_.height,cx-static_cast<int>(logo_.width)/2,160-static_cast<int>(logo_.height)/2);return;}
    if(state==6 || state==20){
        const bool story=state==20;
        c.setClip(left,20,240,266);font_.drawWrapped(c,strings_[story?40:28],cx,scrollY,230,1,0,frame,nullptr,story);c.resetClip();
        font_.draw(c,story?u"ENTER - NEXT   ESC - BACK":u"ESC / ENTER - BACK",cx,300,1);return;
    }
    const auto& title=titleSprite_.images().at(0).at(0);c.blit(title.pixels,title.width,title.height,left,0);
    if(state==19){
        box(c,left+8,92,224,190,0xee000000);
        font_.draw(c,strings_[41]+u" "+std::u16string(1,char16_t('1'+selectedLevel)),cx,114,1);
        if(selectedLevel!=0){
            font_.drawWrapped(c,u"THIS CHAPTER IS NOT AVAILABLE YET.",cx,152,210,1);
            font_.draw(c,u"LEFT - PREVIOUS CHAPTER",cx,236,1);
            if(!saveStatus.empty())font_.drawWrapped(c,saveStatus,cx,270,230,1);
            font_.draw(c,u"ESC - BACK",cx,300,1);return;
        }
        font_.drawWrapped(c,strings_[42],cx,144,210,1,1);
        font_.draw(c,u"ENTER THE TOWN OF OMINOS",cx,206,1);
        font_.draw(c,u"ENTER - START",cx,244,1);
        if(maxUnlockedLevel>0)font_.draw(c,u"RIGHT - NEXT CHAPTER",cx,272,1);
        font_.draw(c,u"ESC - BACK",cx,300,1);return;
    }
    if(state==18){if(frame%10>5)font_.draw(c,strings_[10],cx,300,1);return;}
    if(state==22){
        box(c,left+8,110,224,150,0xee000000);
        font_.drawWrapped(c,u"START A NEW GAME? THE SAVED CHECKPOINT WILL BE ERASED.",cx,125,210,1);
        font_.draw(c,u"ENTER - NEW GAME",cx,210,1);font_.draw(c,u"ESC - CANCEL",cx,240,1);return;
    }
    box(c,left+10,110,220,hasSave?150:125,0xdd000000);
    if(state==9){font_.draw(c,u"LOADING FIRST LEVEL",cx,160,1);return;}
    const std::vector<std::u16string> labels=hasSave?std::vector<std::u16string>{u"NEW GAME",u"CONTINUE",u"ORIGINAL CREDITS",u"EXIT"}:std::vector<std::u16string>{u"NEW GAME",u"ORIGINAL CREDITS",u"EXIT"};
    for(int i=0;i<static_cast<int>(labels.size());++i){if(i==selection)box(c,left+14,126+i*28,212,22,0xff51323c);font_.draw(c,labels[i],cx,130+i*28,1);}
    font_.draw(c,u"WINDOWS PORT IN DEVELOPMENT",cx,268,1);
    if(!saveStatus.empty())font_.drawWrapped(c,saveStatus,cx,288,230,1);
}
}

namespace soul {
void GameFrontend::drawPause(Canvas& c){
    const int cx=c.width()/2;
    box(c,cx-108,95,216,138,0xee000000);
    font_.draw(c,u"PAUSED",cx,105,1);
    const std::u16string labels[]{u"RESUME",u"RESTART LEVEL",u"MAIN MENU"};
    for(int i=0;i<3;++i){if(i==selection)box(c,cx-100,133+i*28,200,22,0xff51323c);font_.draw(c,labels[i],cx,137+i*28,1);}
    font_.draw(c,u"A/D - MOVE   SPACE - JUMP",cx,260,1);
    font_.draw(c,u"E - ATTACK   K/0 - FIRE MAGIC",cx,277,1);
}
}
