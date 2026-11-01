#include "combat_world.h"
#include "level_data.h"
#include "player_probe.h"
#include <algorithm>
#include <iostream>
// Level-1 intro (trigger 134 -> script 133) through a real CombatWorld: the shipped player spawn already overlaps the trigger, so the
// scene starts on the first world tick. Only the player, zombie 215, flyer 135 and the trigger are constructed; the host's dialogue
// acknowledgement and skip inputs are replayed explicitly. This is a controlled fixture, not New Game.
void emitLevelOneIntroWorldManifest(const std::filesystem::path& root){
    using namespace soul;
    for(const std::string name:{"normal","skip","paused","blocked-skip"}){
        auto data=LevelData::load(root,1);
        std::vector<ActorRecord> owned;
        for(const auto& r:data.records){const int type=r.parameters[0],uid=r.parameters[1];if(type==0 || uid==215 || uid==135 || uid==134)owned.push_back(r);}
        Player player(data.sprite(data.level.playerSprite),data.scene.maps(),playerRecord(data.records),1);PlayerMeters meters;
        CombatWorld world(data.sprite(data.level.enemySprite),data.sprite(data.level.effectSprite),data.scene.maps(),player,meters,owned,data.stats(0));
        world.loadFlyingEnemies(data.sprite(data.level.flyingSprite),owned,data.stats(2),&data.trig);
        world.loadIntro(ByteView(data.scriptBytes),owned);
        if(name=="blocked-skip"){world.introTriggers.at(0).skipAllowed=false;}
        const int startX=player.x,startY=player.y;
        world.damage.invulnerability=60000;world.advance();
        int start=-1,end=-1,dialogueFrames=0,lastDialogue=-1;int hold=0;
        for(int tick=0;tick<400;++tick){
            const bool paused=name=="paused" && tick<10;
            if(world.intro->active()){
                if(start<0)start=tick;
                unsigned pressed=0;
                if(world.intro->dialogue>=0){if(++hold>=8){pressed=65536;hold=0;}}else hold=0;
                if((name=="skip" || name=="blocked-skip") && world.intro->ticks>=20)pressed|=131072;
                world.updateIntro(pressed,1);world.animateIntro();
                if(world.introInputReset)world.introInputReset=false;
                lastDialogue=world.intro->dialogue;dialogueFrames+=lastDialogue>=0;
                if(!world.intro->active() && end<0)end=tick;
                continue;
            }
            world.refreshRegionSelection();world.updatePlayer(0,0,tick,paused);world.finishPlayer(paused);world.advance();
            if(end>=0 && tick>end+10)break;
            if(start<0 && tick>200)break;
        }
        const auto& zombie=world.enemies.at(0);const auto& flyer=world.flyingEnemies.at(0);
        std::cout<<"W\t"<<name<<'\t'<<start<<'\t'<<end<<'\t'<<world.intro->starts<<'\t'<<world.intro->finishes<<'\t'<<world.intro->skips<<'\t'<<world.intro->dialoguesOpened<<'\t'<<dialogueFrames
            <<'\t'<<int(world.introTriggers.at(0).alive)<<'\t'<<startX<<'\t'<<startY<<'\t'<<player.x<<'\t'<<player.y<<'\t'<<zombie.x<<'\t'<<zombie.y<<'\t'<<zombie.flags<<'\t'<<flyer.x<<'\t'<<flyer.y<<'\t'<<flyer.flags<<'\n';
        world.detach();
    }
}
