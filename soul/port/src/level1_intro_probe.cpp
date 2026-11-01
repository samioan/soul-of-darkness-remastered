#include "player_probe.h"
#include "intro_cutscene.h"
#include "level_data.h"
#include <iostream>
#include <array>
#include <algorithm>
namespace {
struct Actor {int uid=0,type=0,x=0,y=0,flags=0,animation=0;bool facing=false,alive=true;std::array<int,4> custom{};const soul::BSprite* sprite=nullptr;
    void animate(int n){if(n!=animation){animation=n;flags&=~64;}}
    int top()const{if(type==21)return custom[1];const auto& a=sprite->animations().at(animation);const auto& f=sprite->animationFrames().at(a.start);auto box=sprite->frameBounds(f.frame,0,(flags^f.flags)&3);if(box[0]==box[2] && box[1]==box[3])box=sprite->frameBounds(f.frame,-1,(flags^f.flags)&3);return box[1]+y+((flags&2)?-f.y:f.y);}
};
}
// Level-1 intro (script 133, trigger 134). Rows mirror tools/reference_checks/LevelOneIntroCheck.java.
void emitLevelOneIntroManifest(const std::filesystem::path& root){
    auto data=soul::LevelData::load(root,1);
    const auto& playerSprite=data.sprite(data.level.playerSprite);const auto& enemySprite=data.sprite(data.level.enemySprite);const auto& flyingSprite=data.sprite(data.level.flyingSprite);
    const auto data133=std::find_if(data.scripts.begin(),data.scripts.end(),[](const auto& s){return s.uid==133;});
    for(const std::string name:{"normal","pause","skip0","skip20","skip-dialogue","skip-late","no-skip"}){
        std::array<Actor,3> actors;const int uids[]{131,215,135};
        for(int i=0;i<3;++i){const auto r=std::find_if(data.records.begin(),data.records.end(),[&](const auto& r){return r.parameters[1]==uids[i];});const auto& p=r->parameters;auto& a=actors[i];a.uid=p[1];a.type=p[0];a.x=static_cast<std::uint16_t>(p[2]);a.y=static_cast<std::uint16_t>(p[3]);a.flags=p[4]?p[5]&~64:p[5];a.animation=p[4];a.facing=(a.flags&1)!=0;a.custom={a.x+p[6],a.y+p[7],a.x+p[6]+p[8],a.y+p[7]+p[9]};a.sprite=a.type==0?&playerSprite:a.type==1?&enemySprite:&flyingSprite;}
        soul::IntroCutscene script(*data133,data.scene.maps().width(),data.scene.maps().height());script.start(0,0,name!="no-skip");
        const auto resolve=[&](int uid)->soul::IntroActor{for(auto& a:actors)if(a.uid==uid && a.alive)return {&a.x,&a.y,&a.flags,&a.facing,[&a](int n){a.animate(n);},[&a]{return a.top();},[]{},[&a]{a.alive=false;}};return {};};
        int hold=0;
        for(int frame=0;frame<170;++frame){const int current=script.dialogue;const bool ack=current>=0 && ++hold>=4;if(current<0)hold=0;if(ack){hold=0;script.dismissDialogue();}
            const bool skip=name=="skip0"&&frame==0 || name=="skip20"&&script.ticks==20 || name=="skip-dialogue"&&current==4 || name=="skip-late"&&current==9 || name=="no-skip"&&frame==10;const bool paused=name=="pause"&&frame>=10&&frame<15;
            if(skip && script.skipAllowed)script.skip(resolve);else if(!paused)script.step(resolve);
            std::cout<<"S\t"<<name<<'\t'<<frame;for(int n:{script.ticks,script.dialogue,script.cameraX,script.cameraY,script.anchorX,script.anchorY})std::cout<<'\t'<<n;for(int n:script.offsets())std::cout<<'\t'<<n;std::cout<<'\n';
            for(const auto& a:actors){std::cout<<"A\t"<<name<<'\t'<<frame;for(int n:{a.uid,int(a.alive),a.x,a.y,a.flags,a.animation,int(a.facing)})std::cout<<'\t'<<n;std::cout<<'\n';}
        }
    }
}
