#include "game_frontend.h"
#include "english_text.h"
#include <iostream>
#include <stdexcept>
void check(bool b,const char* message){if(!b)throw std::runtime_error(message);}
int main(){try{
    soul::FrontendState ui;ui.press(13);check(ui.state==1,"Logo cannot consume input into menu");
    for(int i=0;i<46;++i)ui.update(100);check(ui.state==1,"Logo ended early");
    ui.update(100);check(ui.state==18 && ui.frame==0,"Logo must end at tick 47");
    ui.press(13);check(ui.state==2 && !ui.startRequested,"Title key must not also start gameplay");
    ui.press(40);ui.press(13);check(ui.state==6 && ui.scrollY==266,"Credits entry");
    ui.update(100);check(ui.scrollY==265,"Credits scroll on simulation tick");
    ui.scrollY=-100;ui.update(100);check(ui.scrollY==-101,"Last credit line must leave canvas before looping");
    ui.update(100);check(ui.scrollY==266,"Credits loop after full text exits");
    ui.press(27);check(ui.state==2,"Credits back");
    ui.press(38);ui.press(13);check(ui.state==20 && ui.scrollY==240 && !ui.startRequested,"New Game enters original prologue at Y240");
    ui.update(100,500);check(ui.scrollY==239,"Story scrolls on ticks");
    ui.press(40);ui.press(38);ui.update(100,500);check(ui.scrollY==234,"Either held direction accelerates scroll once");
    ui.release(40);ui.update(100,500);check(ui.scrollY==229,"Releasing one direction keeps the other held");
    ui.release(38);ui.update(100,500);check(ui.scrollY==228,"Releasing both restores normal scroll");
    ui.scrollY=-501;ui.update(100,500);check(ui.scrollY==266,"Prologue loops instead of starting without input");
    ui.press(13);check(ui.state==19 && !ui.startRequested && !ui.scrollKeys,"Next enters selection without loading");
    ui.press(39);ui.press(37);check(ui.state==19,"Fresh selection cannot navigate to locked levels");
    ui.press(27);check(ui.state==2,"Selection back returns to menu");
    ui.press(13);check(ui.state==20 && ui.scrollY==240,"New Game resets story position");
    ui.press(27);check(ui.state==2,"Prologue back returns to menu");
    ui.press(13);ui.press(13);ui.press(13);check(ui.state==9 && !ui.startRequested,"Loading must precede game start");
    ui.update(100);check(ui.startRequested,"Load request on next tick");
    ui.startRequested=false;ui.enter(8);ui.press(27);check(ui.state==21 && ui.selection==0,"Gameplay Escape pauses");
    ui.press(27);check(ui.state==8 && !ui.startRequested,"Escape resumes without reloading");
    ui.press(27);ui.press(13);check(ui.state==8 && !ui.startRequested,"Resume preserves game");
    ui.press(27);ui.press(40);ui.press(13);check(ui.state==9 && !ui.startRequested,"Restart uses loading boundary");
    ui.update(100);check(ui.startRequested && ui.restartRequested,"Restart distinguishes the session reload from New Game");
    ui.startRequested=false;ui.enter(8);ui.press(27);ui.press(38);ui.press(13);check(ui.state==2 && ui.selection==0,"Pause main menu resets menu selection");
    std::vector<std::u16string> text(43,u"unused");text[28]=u"Verze: X.X.X^Info a podpora:";
    for(int i=0;i<20;++i)text[28]+=u"{heading}^CREDITED NAME^";
    soul::applyEnglishFrontendText(text);
    check(text[10]==u"PRESS ANY KEY" && text[41]==u"CHAPTER" && text[42]==u"THE CHALLENGE","English startup labels");
    check(text[40].find(u"Kale")!=std::u16string::npos,"English prologue");
    check(text[28].find(u"{Executive producers}^CREDITED NAME^")!=std::u16string::npos && text[28].find(u"{Special thanks}^CREDITED NAME^")!=std::u16string::npos,"Credit roles translate while names stay in order");
    ui.press(38);ui.press(13);check(ui.exitRequested,"Menu wraps to exit");
    soul::FrontendState chapters;chapters.maxUnlockedLevel=1;chapters.selectedLevel=1;chapters.enter(19);chapters.press(13);chapters.update(100);
    check(chapters.state==19 && !chapters.startRequested,"Unlocked unported chapter cannot start a world");
    chapters.press(37);check(chapters.selectedLevel==0,"Completed selection can return to first chapter");chapters.press(39);check(chapters.selectedLevel==1,"Selection can revisit unlocked next chapter");
    chapters.press(37);chapters.press(13);check(chapters.state==9,"First chapter remains replayable after completion");
    std::cout<<"PASS: frontend logo/title/menu/credits/prologue/selection/loading/back/exit transitions\n";return 0;
}catch(const std::exception& e){std::cerr<<e.what()<<'\n';return 1;}}
