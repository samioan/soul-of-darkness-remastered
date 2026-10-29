#include <windows.h>
#include <mmsystem.h>
#include <algorithm>
#include <chrono>
#include <filesystem>
#include <fstream>
#include <iostream>
#include <memory>
#include <sstream>
#include <string>
#include <vector>
#include <list>
#include <optional>
#include "timing.h"
#include "sprite_renderer.h"
#include "bitmap_font.h"
#include "tile_world.h"
#include "player.h"
#include "pickup.h"
#include "world_transients.h"
#include "container.h"
#include "crystal_reward.h"
#include "melee_weapon.h"
#include "combat_world.h"
#include "game_frontend.h"
#include "window_buffer.h"
#include "gameplay_view.h"
#include "ability_pickup.h"
#include "save_game.h"
#include <shlobj.h>

namespace {
struct Settings {
    int width = 569;
    double fps = 60;
    bool fullscreen = false, interpolation = true;
};
Settings settings;
std::unique_ptr<soul::GameFrontend> frontend;
bool gameMode=false,calibrationRequested=false,gameScript=false,gamePassageScript=false,gameAbilityScript=false,gameActivationScript=false,gameSecondWaveScript=false,gameArenaScript=false,gameFireScript=false,gameFireAfterArena=false,gameIceScript=false,iceProjectileFixture=false,gameCheckpointScript=false,gameBeamScript=false,gamePromptScript=false,gameIntroScript=false,introSkipFixture=false;
std::optional<soul::CheckpointSnapshot> sessionCheckpoint;int checkpointActivations=0,checkpointRestarts=0,checkpointMenuRestarts=0,checkpointFixtureStage=0;bool restoringCheckpoint=false;
std::filesystem::path gameSavePath;
std::optional<soul::CheckpointSnapshot> diskCheckpoint;
bool automatedRun=false,continueOnLaunch=false;
void saveFailure(const std::exception& e){
    std::cerr<<"Save: "<<e.what()<<'\n';
    if(frontend)frontend->saveStatus=u"SAVE UNAVAILABLE - SEE ERROR";
    if(!automatedRun)MessageBoxA(nullptr,e.what(),"Soul of Darkness save",MB_OK|MB_ICONERROR);
}
int frontendScriptStage=0;
void loadPreview();
void restartGameplay();
soul::Clock clockState;
soul::Canvas framebuffer(569,soul::kHeight);
auto& pixels = framebuffer.pixels();
struct PreviewOptions {
    bool requested = false, extra = false, font = false;
    bool beamScript=false,promptScript=false,introScript=false;
    int pack = 0, resource = 0, frame = -1, animation = -1, palette = 0, flip = 0;
    int level=-1,cameraX=0,cameraY=0;
    bool cameraRequested=false;
    bool movement=false,movementScript=false,containerScript=false,meleeScript=false,combatScript=false,defeatScript=false,platformScript=false,swingScript=false,bridgeScript=false,regionScript=false,gateScript=false,gateTriggerScript=false,passageScript=false,abilityScript=false,activationScript=false,secondWaveScript=false,arenaScript=false,fireScript=false,iceScript=false,checkpointScript=false;
    std::filesystem::path data;
} previewOptions;
struct Preview {
    soul::BSprite sprite;
    soul::SpriteRenderer renderer;
    soul::DrawOptions style;
    int frame = -1, animation = -1;
    std::size_t step = 0;
    unsigned tick = 0;
    explicit Preview(soul::BSprite loaded) : sprite(std::move(loaded)), renderer(sprite) {}
    void advance() {
        if (animation < 0) return;
        const auto& a = sprite.animations()[animation];
        const auto duration = sprite.animationFrames()[a.start+step].duration;
        if (duration > 0 && ++tick >= static_cast<unsigned>(duration)) { tick = 0; step = (step+1)%a.count; }
    }
    void draw(soul::Canvas& target) {
        if (animation >= 0) renderer.drawAnimation(target,animation,step,target.width()/2,220,style);
        else if (frame >= 0) renderer.drawFrame(target,frame,target.width()/2,220,style);
        else renderer.drawImage(target,0,style.palette,0,0,style.flips);
    }
};
std::unique_ptr<Preview> preview;
struct FontPreview {
    soul::BSprite sprite, icons;
    soul::BitmapFont font;
    soul::SpriteRenderer iconRenderer;
    std::uint64_t stateFrame=0;
    FontPreview(soul::BSprite f,soul::BSprite i) : sprite(std::move(f)),icons(std::move(i)),font(sprite),iconRenderer(icons) {}
    void draw(soul::Canvas& target) {
        font.draw(target,u"Soul of Darkness",target.width()/2,28,1,previewOptions.palette);
        font.drawWrapped(target,u"Native bitmap font preview. {Palette controls} and inline icons: ~0 ~1 ~2 ~3^The viewport width determines where these words wrap.",
            16,65,target.width()-32,0,previewOptions.palette,stateFrame,&iconRenderer);
        font.draw(target,u"ASSET VIEWER - GAMEPLAY NOT PORTED",target.width()/2,280,1);
        font.draw(target,u"F8 WIDTH  F9 FPS  ESC EXIT",target.width()/2,300,1);
    }
};
std::unique_ptr<FontPreview> fontPreview;
std::unique_ptr<soul::TileScene> tilePreview;
int tileCameraX=0,tileCameraY=0;
bool panLeft=false,panRight=false,panUp=false,panDown=false;
void fill(soul::Canvas& canvas,int x,int y,int width,int height,std::uint32_t color) {
    const auto clip=canvas.clip(); const int left=std::max(x,clip.left),top=std::max(y,clip.top);
    const int right=std::min(x+width,clip.right),bottom=std::min(y+height,clip.bottom);
    if(right>left && bottom>top) canvas.blit(soul::Pixels(static_cast<std::size_t>(right-left)*(bottom-top),color),right-left,bottom-top,left,top);
}
extern std::array<bool,256> movementKeys;
struct MovementPreview {
    soul::TileScene scene; soul::BSprite sprite; std::vector<soul::ActorRecord> records; soul::Player player;
    soul::BSprite pickupSprite,containerSprite,promptSprite; std::vector<soul::Pickup> pickups;
    soul::BSprite effectSprite,weaponSprite; std::list<soul::CollectionEffect> effects; int effectsSpawned=0;
    std::unique_ptr<soul::MeleeWeapon> weapon;
    int meleeAttacks=0,meleePoses=0,iceMeleeAttacks=0,iceMeleePoses=0,iceFixtureStage=0,iceFixtureTicks=0;
    int iceShotStage=0,iceShotTicks=0,iceLandings=0,iceDetachments=0,iceRidingTicks=0,iceRideY=-1;bool wasIceRiding=false;
    int regionPickupsRemoved=0,regionEffectsRemoved=0;
    soul::BSprite enemySprite,flyingSprite,platformSprite,bridgeSprite,beamSprite; std::unique_ptr<soul::CombatWorld> combat;
    std::vector<soul::Container> containers; soul::PlayerMeters meters; int placedPickups=0,openedContainers=0;
    soul::BSprite abilitySprite; std::vector<soul::AbilityPickup> abilities; soul::AbilityInteraction abilityUi; int abilityUnlocks=0,abilityCompleted=0,regionAbilitiesRemoved=0;
    soul::BSprite fontSprite; soul::BitmapFont font; soul::SpriteRenderer crystalRenderer;
    soul::CrystalReward reward; int beamTicks=0,rewardsCompleted=0,resumeTicks=0; std::size_t crystalIndex=0;
    bool restartRequested=false;int introControlTicks=0;
    soul::MovementInput input; int ticks=0,cameraX=0,cameraY=0;
    MovementPreview(soul::TileScene world,soul::BSprite image,std::vector<soul::ActorRecord> rows,soul::BSprite items,soul::BSprite chests,soul::BSprite prompts,soul::BSprite text,soul::BSprite effectImage,soul::BSprite sword,soul::BSprite enemies,soul::BSprite platformImage,soul::BSprite bridgeImage,const std::vector<int>& stats,const std::vector<int>& flyingStats,const std::vector<int>& beamStats)
        : scene(std::move(world)),sprite(std::move(image)),records(std::move(rows)),player(sprite,scene.maps(),soul::playerRecord(records)),pickupSprite(std::move(items)),containerSprite(std::move(chests)),promptSprite(std::move(prompts)),fontSprite(std::move(text)),font(fontSprite),crystalRenderer(pickupSprite) {
        effectSprite=std::move(effectImage);
        weaponSprite=std::move(sword); enemySprite=std::move(enemies); platformSprite=std::move(platformImage); bridgeSprite=std::move(bridgeImage);
        combat=std::make_unique<soul::CombatWorld>(enemySprite,effectSprite,scene.maps(),player,meters,records,stats);
        const auto flyingPack=soul::ResourcePack::numeric(soul::readAssetFile(previewOptions.data/"2"));flyingSprite=soul::BSprite::parse(flyingPack.resource(3).value());combat->loadFlyingEnemies(flyingSprite,records,flyingStats);
        const auto trig=soul::loadPlatformTrig(previewOptions.data);
        combat->loadFireMagic(trig);
        if(gamePromptScript)for(auto& r:records)if(r.parameters[0]==21 && r.parameters[1]==8){r.parameters[2]=440;r.parameters[3]=200;}
        combat->loadPrompts(promptSprite,records);
        beamSprite=soul::BSprite::parse(flyingPack.resource(10).value());
        combat->loadBeamEmitters(beamSprite,records,beamStats,trig);
        combat->loadPlatforms(platformSprite,records,&trig,stats.at(1));
        combat->loadBridges(bridgeSprite,scene.mutableMaps(),records);
        combat->loadGates(platformSprite,scene.mutableMaps(),records);
        const auto abilityPack=soul::ResourcePack::numeric(soul::readAssetFile(previewOptions.data/"2"));
        abilitySprite=soul::BSprite::parse(abilityPack.resource(27).value());
        const auto levelPack=soul::ResourcePack::numeric(soul::readAssetFile(previewOptions.data/"3"));
        combat->loadGateScripts(levelPack.resource(1).value());
        for(const auto& r:records) {
            if(r.parameters[0]==35) pickups.emplace_back(pickupSprite,r);
            if(r.parameters[0]==52) containers.emplace_back(containerSprite,promptSprite,scene.maps(),r);
            if(r.parameters[0]==28) {
                if(weapon) throw soul::FormatError("Duplicate player weapon anchor");
                weapon=std::make_unique<soul::MeleeWeapon>(weaponSprite,r);
            }
        }
        if(previewOptions.iceScript || gameIceScript){
            const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(previewOptions.data/"4"));const auto placements=soul::actorRecords(pack.resource(0).value());
            const auto chest=std::find_if(placements.begin(),placements.end(),[](const auto& r){return r.parameters[0]==52 && r.parameters[1]==59 && r.parameters[6]==-5;});
            if(chest==placements.end())throw soul::FormatError("Missing original second-level ice chest");if(std::any_of(records.begin(),records.end(),[](const auto& r){return r.parameters[1]==1000;}))throw soul::FormatError("Ice fixture UID collides with a placed actor");auto fixture=*chest;fixture.parameters[1]=1000;fixture.parameters[2]=340;fixture.parameters[3]=262;fixture.parameters[5]&=~(32|128);containers.emplace_back(containerSprite,promptSprite,scene.maps(),fixture);
            player.x=340;player.y=259;player.fixedX=player.x*256;player.fixedY=player.y*256;player.refreshBounds();meters.magicUnlocked=true;meters.magic=iceProjectileFixture?60:30;combat->damage.invulnerability=60000;
        }
        combat->bindContainers(containers);
        if(!weapon) throw soul::FormatError("Missing player weapon anchor");
        placedPickups=static_cast<int>(pickups.size());
        if(previewOptions.activationScript || gameActivationScript || previewOptions.secondWaveScript || gameSecondWaveScript || previewOptions.arenaScript || gameArenaScript){
            player.x=1400;player.y=1579;player.fixedX=player.x*256;player.fixedY=player.y*256;player.refreshBounds();combat->damage.invulnerability=60000;
        }
        if(previewOptions.abilityScript || gameAbilityScript){
            for(auto& chest:containers)if(chest.uid==57){chest.flags&=~(32|128);player.x=chest.x;player.y=1599;}
            player.fixedX=player.x*256;player.fixedY=player.y*256;player.refreshBounds();meters.magic=30;combat->damage.invulnerability=60000;
        }
        if(previewOptions.passageScript || gamePassageScript){
            player.x=1220;player.y=119;player.fixedX=player.x*256;player.fixedY=player.y*256;player.vx=player.vy=player.ax=player.ay=0;player.refreshBounds();combat->damage.invulnerability=60000;
        }
        if(previewOptions.gateScript || previewOptions.gateTriggerScript){player.x=1400;player.y=1500;player.fixedX=player.x*256;player.fixedY=player.y*256;player.flags|=16384;player.refreshBounds();combat->damage.invulnerability=60000;if(previewOptions.gateScript)combat->startGateScript(165);}
        if(previewOptions.fireScript || gameFireScript){
            player.x=440;player.y=259;player.fixedX=player.x*256;player.fixedY=player.y*256;player.refreshBounds();meters.magicUnlocked=true;meters.magic=60;combat->damage.invulnerability=60000;
        }
        if(previewOptions.combatScript || previewOptions.defeatScript) {
            // Controlled starting position beside UID 19, on the actual level floor.
            player.x=440; player.y=259; player.fixedX=player.x*256; player.fixedY=player.y*256;
            player.refreshBounds();
            if(previewOptions.defeatScript) meters.health=10;
        }
        if(previewOptions.platformScript){
            auto& platform=combat->platforms.front();player.x=platform.x;player.y=platform.collision[1]-25;player.fixedX=player.x*256;player.fixedY=player.y*256;player.refreshBounds();
        }
        if(previewOptions.bridgeScript){
            for(auto& platform:combat->platforms)if(platform.uid==29 && platform.type==50)platform.action=102;
            player.x=900;player.y=1099;player.fixedX=player.x*256;player.fixedY=player.y*256;player.refreshBounds();
        }
        if(previewOptions.swingScript){
            const auto platform=std::find_if(combat->platforms.begin(),combat->platforms.end(),[](const auto& p){return p.type==50 && p.uid==4;});
            if(platform==combat->platforms.end())throw soul::FormatError("Missing swing fixture UID 4");
            player.x=platform->x;player.y=platform->collision[1]-25;player.fixedX=player.x*256;player.fixedY=player.y*256;player.refreshBounds();
        }
        if(previewOptions.promptScript || gamePromptScript){
            combat->damage.invulnerability=60000;
            if(gamePromptScript){player.x=440;player.y=259;player.fixedX=player.x*256;player.fixedY=player.y*256;player.refreshBounds();meters.magicUnlocked=true;meters.magic=60;}
        }
        if(previewOptions.beamScript || gameBeamScript){player.x=gameBeamScript?600:167;player.y=gameBeamScript?1599:1559;player.fixedX=player.x*256;player.fixedY=player.y*256;player.refreshBounds();}
        // Deliberately controlled host fixture, not an input recording from spawn.
        if(previewOptions.containerScript) {
            const auto& chest=containers.front(); player.x=chest.x; player.y=chest.y;
            player.fixedX=player.x*256; player.fixedY=player.y*256;
        }
        if((previewOptions.checkpointScript || gameCheckpointScript) && !restoringCheckpoint){
            player.x=160;player.y=259;player.fixedX=160*256;player.fixedY=259*256;player.facingLeft=true;player.flags=1;player.refreshBounds();
            meters.maxHealth=80;meters.health=23;meters.maxMagic=90;meters.magic=17;meters.magicUnlocked=true;meters.iceUnlocked=true;meters.healthFragments=2;meters.magicFragments=1;player.activeWeapon=1;
        }
        combat->loadCheckpoints(records,[this](const soul::Checkpoint& checkpoint){
            auto removed=combat->removedCheckpointActors();
            for(const auto& p:pickups)if(!p.alive && p.uid>=0)removed.push_back({p.uid,p.flags});
            for(const auto& c:containers)if(!c.alive)removed.push_back({c.uid,c.flags});
            sessionCheckpoint=soul::CheckpointSnapshot::capture(checkpoint,player,meters,std::move(removed));++checkpointActivations;
            if(!gameSavePath.empty())try{soul::writeGameSave(gameSavePath,*sessionCheckpoint);diskCheckpoint=sessionCheckpoint;if(frontend){frontend->hasSave=true;frontend->saveStatus.clear();}}catch(const std::exception& e){saveFailure(e);}
        });
        const bool controlled=previewOptions.movementScript || previewOptions.containerScript || previewOptions.meleeScript || previewOptions.combatScript || previewOptions.defeatScript || previewOptions.platformScript || previewOptions.swingScript || previewOptions.bridgeScript || previewOptions.regionScript || previewOptions.gateScript || previewOptions.gateTriggerScript || previewOptions.passageScript || previewOptions.abilityScript || previewOptions.activationScript || previewOptions.secondWaveScript || previewOptions.arenaScript || previewOptions.fireScript || previewOptions.iceScript || previewOptions.checkpointScript || previewOptions.beamScript || previewOptions.promptScript || gameCheckpointScript || gameBeamScript || gamePromptScript || gamePassageScript || gameAbilityScript || gameActivationScript || gameSecondWaveScript || gameArenaScript || gameFireScript || gameIceScript;
        if(previewOptions.introScript || gameIntroScript || (frontend && !controlled))combat->loadIntro(levelPack.resource(1).value(),records);
    }
    void restoreCheckpoint(const soul::CheckpointSnapshot& saved){
        saved.restore(player,meters);
        const auto skips=[&](int uid){const auto r=std::find_if(records.begin(),records.end(),[&](const auto& row){return row.parameters[1]==uid;});return r!=records.end() && saved.skips(*r);};
        for(auto& p:pickups)if(skips(p.uid))p.alive=false;
        for(auto& c:containers)if(skips(c.uid))c.alive=false;
        combat->restoreCheckpointActors(saved,records);input.clear();
        const auto camera=scene.clampCamera(player.x-settings.width/2,player.y-220,settings.width,soul::kHeight);cameraX=camera.first;cameraY=camera.second;
    }
    bool crystalBoundary() const { return std::any_of(pickups.begin(),pickups.end(),[](const auto& p){return p.alive && p.upgradeBoundary;}); }
    void update() {
        if(combat->intro && combat->intro->active()){
            auto& sceneIntro=*combat->intro;
            if(previewOptions.introScript || gameIntroScript){if(sceneIntro.ticks>=10 && sceneIntro.ticks<15)input.press(8192);else input.release(8192);}
            if(previewOptions.introScript || gameIntroScript){
                if(introSkipFixture && sceneIntro.ticks>=20)input.press(131072);
                else if(sceneIntro.dialogue>=0 && sceneIntro.dialogueFrames>=8 && !input.held)input.press(65536);
                else input.release(65536|131072);
            }
            const int pages=sceneIntro.dialogue<0?1:std::max(1,(static_cast<int>(font.wrap(soul::introText(sceneIntro.dialogue),210).size())+2)/3);
            combat->updateIntro(input.pressed,pages);combat->animateIntro();weapon->hide();
            if(combat->introInputReset){input.clear();movementKeys.fill(false);combat->introInputReset=false;}
            input.latch(player);++ticks;return;
        }
        if((previewOptions.introScript || gameIntroScript) && combat->intro && combat->intro->finishes && introControlTicks<20){
            if(introControlTicks<6){input.release(4096);input.press(8192);}else if(introControlTicks>=12 && introControlTicks<18){input.release(8192);input.press(4096);}else input.release(8192|4096);++introControlTicks;
        }
        if(previewOptions.promptScript){if(ticks<35){input.release(4096);input.press(8192);}else if(ticks<70){input.release(8192);input.press(4096);}else input.release(4096|8192);}
        if(gamePromptScript){if(ticks==8)input.press(1);if(ticks==9)input.release(1);if(ticks>=30 && ticks<55)input.press(8192);else input.release(8192);}
        if(previewOptions.beamScript || gameBeamScript){if(ticks>=35 && ticks<100)input.press(8192);else input.release(8192);}
        if(previewOptions.checkpointScript || gameCheckpointScript){
            if(checkpointFixtureStage==0){if(sessionCheckpoint){checkpointFixtureStage=1;input.release(4096);input.press(8192);}else input.press(4096);}
            if(checkpointFixtureStage==1 && player.x>=200){input.release(8192);meters.health=0;checkpointFixtureStage=2;}
            if(checkpointFixtureStage==2 && player.deathComplete){restartRequested=true;checkpointFixtureStage=3;return;}
            if(checkpointFixtureStage==3 && checkpointRestarts==1 && ticks>=5){if(frontend){frontend->press(27);frontend->press(40);frontend->press(13);}else restartRequested=true;checkpointFixtureStage=4;return;}
        }
        if(player.deathComplete) { input.latch(player); ++ticks; return; }
        if(abilityUi.active){
            if(abilityUi.mode==3){
                player.updateCrystalPose(true);
                for(auto& a:abilities){a.update(player,meters,abilityUi);a.refreshBounds();a.advanceAnimation();}
                for(auto& e:effects){e.update();e.advanceAnimation();}
                for(auto& e:combat->effects){e.update();e.advanceAnimation();}
                player.refreshBounds();player.advanceAnimation();
            }
            if((previewOptions.abilityScript || gameAbilityScript || previewOptions.arenaScript || gameArenaScript || previewOptions.iceScript || gameIceScript) && abilityUi.mode==14 && abilityUi.tutorialFrames>8 && !input.held)input.press(65536);
            abilityUi.advance(input.pressed);
            if(!abilityUi.active){++abilityCompleted;input.clear();}
            input.latch(player);++ticks;return;
        }
        if(abilityUi.inputBlocked)input.clear();
        if(crystalBoundary()) {
            if(beamTicks==0) {
                for(std::size_t i=0;i<pickups.size();++i) if(pickups[i].alive && pickups[i].upgradeBoundary) { crystalIndex=i; break; }
                pickups[crystalIndex].beginCrystalDisplay(player);
                weapon->hide();
            }
            if(!reward.active && beamTicks<39) {
                player.updateCrystalPose(true);
                for(auto& effect:effects) { effect.update(); effect.advanceAnimation(); }
                if(++beamTicks==39) { reward.begin(meters,pickups[crystalIndex].animation==1?0:1); ++meters.collected; }
                else player.advanceAnimation();
            } else if(reward.active) {
                if(previewOptions.containerScript && reward.frames>=10 && !input.held) input.press(65536);
                if(reward.update(meters,input.pressed!=0)) {
                    pickups[crystalIndex].finishCrystal(); ++rewardsCompleted; beamTicks=0;
                }
            }
            if(previewOptions.containerScript && input.held && !reward.active) input.release(65536);
            if(combat->introInputReset){input.clear();movementKeys.fill(false);combat->introInputReset=false;}
        if(combat->intro)combat->intro->presentationTick();
        input.latch(player); ++ticks; return;
        }
        if (previewOptions.movementScript) {
            if (ticks==0) input.press(8192);
            if (ticks==8) input.press(16384);
            if (ticks==11) input.release(16384);
            if (ticks==40) input.release(8192);
        }
        if(previewOptions.iceScript || gameIceScript){
            if(!meters.iceUnlocked){
                const bool prompt=std::any_of(containers.begin(),containers.end(),[](const auto& c){return c.uid==1000 && c.prompt;});
                if(prompt && !input.held)input.press(65536);else if(input.held && !prompt)input.release(65536);
            }else if(abilityCompleted){
                if(iceFixtureStage==0){input.release(65536|8192);input.press(512);iceFixtureStage=1;}
                else if(iceFixtureStage==1 && player.activeWeapon==1){
                    if(iceProjectileFixture){
                        input.release(512);
                        if(iceShotStage==0){if(combat->iceMagic->casts>0){input.release(1);iceShotStage=1;}else if(!input.held && player.animation==0)input.press(1);else input.release(1);}
                        else if(iceShotStage==1 && combat->kills>0){input.release(1);iceShotStage=2;}
                        else if(iceShotStage==2){if(player.x>140)input.press(4096);else{input.release(4096);if(player.animation==0){iceShotStage=3;}}}
                        else if(iceShotStage==3){
                            const auto* shot=combat->iceMagic->slots[0];
                            if(combat->iceMagic->casts>=2 && shot && shot->animation==9){input.release(1);input.press(16384);iceShotStage=4;iceShotTicks=0;}
                            else if(combat->iceMagic->casts<2 && player.animation==0){if(!input.held)input.press(1);else input.release(1);}else input.release(1);
                        }else if(iceShotStage==4){const int f=iceShotTicks++;if(f==0)input.release(16384);if(f==2)input.press(4096);if(f==5)input.release(4096);if(player.platform && player.platform->type==39){iceShotStage=5;iceShotTicks=0;}}
                        else if(iceShotStage==5){if(++iceShotTicks>=20)iceShotStage=6;}
                        else if(iceShotStage==6){if(combat->iceMagic->casts>=3){input.release(1);iceShotStage=7;}else if(player.animation==0 && !input.held)input.press(1);else input.release(1);}
                        else if(iceShotStage==7)input.release(1|4096|8192|16384);
                    }else{
                    input.release(512);const int f=iceFixtureTicks++;
                    const soul::EnemyType1* target=nullptr;for(const auto& e:combat->enemies)if(e.uid==19 && e.alive && !(e.flags&1024) && e.health>0)target=&e;
                    if(f%12==0){input.release(65536|4096|8192);if(target){const int delta=target->x-player.x;if(std::abs(delta)>30 || player.facingLeft!=(delta<0))input.press(delta<0?4096:8192);}}
                    if(f%12==2)input.release(4096|8192);
                    if((f%12==3 && target) || (f%12==5 && player.animation==19))input.press(65536);
                    if(f%12==4 || f%12==6)input.release(65536);
                    if(combat->kills>0 && player.animation==0){input.release(65536|4096|8192);if(player.weaponSwitchCooldown<=0){input.press(512);iceFixtureStage=2;}}
                    }
                }else if(iceFixtureStage==2){if(player.activeWeapon==1 && !input.held)input.press(512);else input.release(512);}
            }
        }
        if(previewOptions.fireScript || gameFireScript){
            if(ticks==4 || ticks==34 || ticks==64 || ticks==94 || ticks==124 || ticks==230)input.press(1);
            if(ticks==5 || ticks==35 || ticks==65 || ticks==95 || ticks==125 || ticks==231)input.release(1);
        }
        if(gameFireAfterArena && abilityCompleted && combat->gateScriptTicks()==-2 && combat->fireMagic->casts==0 && player.animation<=1){
            if(!input.held)input.press(1);else input.release(1);
        }
        if(gameFireAfterArena && combat->fireMagic->casts>0)input.release(1);
        if(previewOptions.meleeScript) {
            if(ticks==4 || ticks==6 || ticks==24) input.press(65536);
            if(ticks==5 || ticks==7 || ticks==25) input.release(65536);
            if(ticks==20) input.press(16384);
            if(ticks==23) input.release(16384);
        }
        if(previewOptions.containerScript && rewardsCompleted) {
            if(resumeTicks++==1) input.press(8192);
            if(resumeTicks==12) input.release(8192);
        }
        if(previewOptions.containerScript && !rewardsCompleted) {
            if(containers.front().prompt && !input.held) input.press(65536);
            else if(input.held && !containers.front().prompt) input.release(65536);
        }
        if(previewOptions.platformScript){
            if(ticks==24)input.press(32768);
            if(ticks==60){input.release(32768);input.press(16384);}
            if(ticks==61)input.release(16384);
        }
        if(previewOptions.combatScript) {
            if(ticks==0) input.press(8192);
            if(ticks==2) input.release(8192);
            if(ticks>=4 && ticks<70 && ticks%6==4) input.press(65536);
            if(ticks>=5 && ticks<71 && ticks%6==5) input.release(65536);
        }
        if(previewOptions.regionScript) {
            if(ticks==4) {
                pickups.push_back(soul::Pickup::enemyDrop(pickupSprite,0,600,200,0));
                pickups.push_back(soul::Pickup::spawned(pickupSprite,3,600,200,0));
            }
            if(ticks==5) {player.x=1400;player.y=259;player.fixedX=player.x*256;player.fixedY=player.y*256;player.vx=player.vy=player.ax=player.ay=0;player.refreshBounds();}
            if(ticks==6)effects.emplace_back(effectSprite,soul::CollectionEffectSpawn{11,600,200,0});
        }
        if((previewOptions.activationScript || gameActivationScript || previewOptions.secondWaveScript || gameSecondWaveScript || previewOptions.arenaScript || gameArenaScript) && ticks>=3){
            const soul::EnemyType1* target=nullptr;
            for(const auto& e:combat->enemies)if((e.uid==114 || e.uid==117) && e.alive && !(e.flags&(32|1024|128)) && e.health>0 && (!target || std::abs(e.x-player.x)<std::abs(target->x-player.x)))target=&e;
            if(previewOptions.secondWaveScript || gameSecondWaveScript || previewOptions.arenaScript || gameArenaScript)for(const auto& e:combat->flyingEnemies)if((e.uid==113 || e.uid==48) && e.alive && !(e.flags&(32|1024|128)) && e.health>0 && (!target || std::abs(e.x-player.x)<std::abs(target->x-player.x)))target=&e;
            if(ticks%6==0){
                input.release(4096|8192|65536);
                if(target){const int delta=target->x-player.x;if(std::abs(delta)>30 || player.facingLeft!=(delta<0))input.press(delta<0?4096:8192);}
            }
            if(ticks%6==2)input.release(4096|8192);
            if(ticks%6==3 && target)input.press(65536);
            if(ticks%6==4)input.release(65536);
        }
        if((previewOptions.arenaScript || gameArenaScript) && !meters.magicUnlocked){
            const bool waveAlive=std::any_of(combat->flyingEnemies.begin(),combat->flyingEnemies.end(),[](const auto& e){return (e.uid==113 || e.uid==48) && e.alive;});
            if(!waveAlive)for(const auto& c:containers)if(c.uid==57 && c.alive && !(c.flags&32)){
                input.release(4096|8192);
                if(c.prompt && !input.held)input.press(65536);
                else if(!c.prompt){input.release(65536);if(c.action==0 || c.action==103){const int delta=c.x-player.x;if(std::abs(delta)>5)input.press(delta<0?4096:8192);}}
            }
        }
        if((previewOptions.abilityScript || gameAbilityScript) && !meters.magicUnlocked){
            const bool prompt=std::any_of(containers.begin(),containers.end(),[](const auto& c){return c.uid==57 && c.prompt;});
            if(prompt && !input.held)input.press(65536);else if(input.held && !prompt)input.release(65536);
        }
        if(previewOptions.passageScript || gamePassageScript){if(ticks==0)input.press(8192);if(ticks==5)input.release(8192);}
        if(previewOptions.gateTriggerScript && ticks==4)for(auto& c:containers)if(c.uid==57)c.alive=false;
        const int previousHits=combat->playerHits;
        combat->updatePlayer(input.pressed,input.frameHeld,ticks);
        weapon->syncPlayer(player);
        if(combat->finishPlayer()) {
            const auto removed=soul::discardRegionTransients(pickups,effects);
            regionPickupsRemoved+=removed.pickups;regionEffectsRemoved+=removed.collectionEffects;
            for(const auto& a:abilities)if(a.alive)++regionAbilitiesRemoved;
            abilities.clear();abilityUi={};
        }

        if(combat->playerHits!=previousHits) weapon->hide();
        if (player.clearInputRequested) input.clear();
        if (!player.stoppedByHazard()) {
            if(player.animation!=8 && player.animation!=9 && (!combat->intro || !combat->intro->active())) { weapon->update(player); }
            else weapon->hide();
            const auto previousItems=pickups.size(),previousAbilities=abilities.size();
            for(const auto& drop:combat->loot) pickups.push_back(soul::Pickup::enemyDrop(pickupSprite,drop.animation,drop.x,drop.y,drop.flags));
            if(previewOptions.regionScript && ticks==6) {
                // Birth after the sweep enters the next tick's pickup queue.
                pickups.push_back(soul::Pickup::enemyDrop(pickupSprite,2,600,200,0));
            }
            for(auto& effect:effects) effect.update();
            if(player.animation!=9) for(auto& chest:containers) {
                chest.drops.clear();
                if(combat->containerQueued(chest.uid))chest.update(player,input.pressed,false,meters.magicUnlocked,&combat->random,meters.iceUnlocked);
                for(const auto& drop:chest.drops){
                    if(drop.type==48)abilities.emplace_back(drop.sprite==18?promptSprite:abilitySprite,drop.x,drop.y,drop.flags!=0,drop.animation);
                    else pickups.push_back(soul::Pickup::spawned(pickupSprite,drop.animation,drop.x,drop.y,drop.flags));
                    ++openedContainers;
                }
            }
            combat->finishContainerSources(containers,player.animation==9);
            if(player.animation!=9) for(std::size_t i=0;i<previousItems;++i) {
                pickups[i].update(player,meters);
                for(const auto& spawn:pickups[i].effects) { effects.emplace_back(effectSprite,spawn); ++effectsSpawned; }
                if(crystalBoundary()) break;
            }
            if(player.animation!=9)for(std::size_t i=0;i<previousAbilities;++i){
                const bool unlocked=meters.magicUnlocked;abilities[i].update(player,meters,abilityUi);
                if(!unlocked && meters.magicUnlocked)++abilityUnlocks;
            }
            if(abilityUi.inputBlocked){weapon->hide();input.clear();}
            for(auto& a:abilities){a.refreshBounds();a.advanceAnimation();}
            if(abilityUi.active)abilityUi.advance(0);
            for(auto& chest:containers) chest.advanceAnimation();
            for(auto& item:pickups) item.advanceAnimation();
            combat->chargeFireMagic();
            player.refreshBounds(); player.advanceAnimation();
            if(player.animation>=13 && player.animation<=15) {
                meleePoses|=1<<(player.animation-13);
                if(player.step==0 && player.animationTick==0) ++meleeAttacks;
            }
            if(player.animation>=19 && player.animation<=21){iceMeleePoses|=1<<(player.animation-19);if(player.step==0 && player.animationTick==0)++iceMeleeAttacks;}
            weapon->advanceAnimation();
            combat->advance();
            for(auto& effect:effects) effect.advanceAnimation();
            effects.remove_if([](const auto& e){return !e.alive;});
        }
        const bool riding=player.platform && player.platform->type==39;
        if(riding){++iceRidingTicks;if(!wasIceRiding){++iceLandings;iceRideY=player.y;}}else if(wasIceRiding)++iceDetachments;
        wasIceRiding=riding;
        if(combat->introInputReset){input.clear();movementKeys.fill(false);combat->introInputReset=false;}
        if(combat->intro)combat->intro->presentationTick();
        input.latch(player); ++ticks;
        const auto camera=scene.clampCamera(player.x-settings.width/2,player.y-220,settings.width,soul::kHeight);
        cameraX=camera.first; cameraY=camera.second;
    }
    void drawIntro(soul::Canvas& c){
        if(!combat->intro)return;const auto& intro=*combat->intro;const int cx=c.width()/2,left=cx-120;
        if(intro.bars>0){fill(c,0,0,c.width(),intro.bars,0xdd000000);fill(c,0,320-intro.bars,c.width(),intro.bars,0xdd000000);}
        if(intro.dialogue>=0){
            const int age=std::min(5,intro.dialogueFrames);const auto camera=soul::introView(intro.cameraX,intro.cameraY,c.width(),scene.maps().width(),scene.maps().height());
            if(age<5){const int ax=intro.anchorX+intro.cameraX-camera.first,ay=intro.anchorY;const int x=ax+(cx-ax)*age/5,y=ay+(37-ay)*age/5;fill(c,x-24*age,y-7*age,48*age,14*age,0xee101018);}
            else{fill(c,left,0,240,75,0xff101018);fill(c,left,74,240,1,0xffe8a81b);font.draw(c,soul::introSpeaker(intro.speaker),left+5,7);
                const auto old=c.clip();c.setClip(left+5,23,230,44);font.drawWrapped(c,soul::introText(intro.dialogue),left+5,23-intro.page*3*font.lineHeight(),210);c.setClip(old.left,old.top,old.right-old.left,old.bottom-old.top);

            }
        }
        if(intro.dialogue>=0)font.draw(c,u"ENTER - NEXT",cx,286,1);
        if(intro.active() && intro.skipAllowed)font.draw(c,u"BACKSPACE - SKIP",cx,305,1);
    }
    void drawHud(soul::Canvas& target) {
        const auto number=[](int n){const auto v=std::to_string(n);return std::u16string(v.begin(),v.end());};
        fill(target,4,4,114,34,0xdd101018);
        const auto bar=[&](int y,int value,int maximum,std::uint32_t color){
            fill(target,8,y,106,6,0xff37343e);
            fill(target,9,y+1,std::clamp(value,0,maximum)*104/std::max(1,maximum),4,color);
        };
        bar(8,meters.health,meters.maxHealth,0xffc6464a);
        bar(17,meters.magic,meters.maxMagic,meters.magicUnlocked?0xff509de0:0xff555a65);
        font.draw(target,u"HP "+number(meters.health)+u"  MP "+number(meters.magic),8,26);
        font.draw(target,u"ESC - PAUSE",target.width()-6,8,8);
        if(meters.iceUnlocked){font.draw(target,player.activeWeapon==1?u"ICE BLADE":u"FIRE BLADE",8,56);font.draw(target,u"Q/9 - SWITCH",8,70);}
        if(meters.magicUnlocked){font.draw(target,player.activeWeapon==1?u"K/0 - ICE":u"K/0 - FIRE",8,42);if(combat->fireMagic->warningMs>0)font.draw(target,u"LOW MP",122,26);}
    }
    void draw(soul::Canvas& target) {
        auto camera=scene.clampCamera(player.x-target.width()/2,player.y-220,target.width(),target.height());
        target.clear(0xff102030);
        if(frontend){
            const auto view=soul::gameplayView(player.x,player.y,target.width(),target.height(),scene.maps().width(),scene.maps().height(),combat->currentRegion()<0?nullptr:&combat->regionCameraBounds());
            camera={view.x,view.y};target.setClip(view.left,view.top,view.right-view.left,view.bottom-view.top);
        }
        if(combat->intro && combat->intro->active()){
            target.resetClip();camera=soul::introView(combat->intro->cameraX,combat->intro->cameraY,target.width(),scene.maps().width(),scene.maps().height());
            if(combat->currentRegion()>=0){const auto& r=combat->regionCameraBounds();const int l=std::max(0,r[0]-camera.first),t=std::max(0,r[1]-camera.second);target.setClip(l,t,std::max(0,std::min(target.width(),r[2]-camera.first)-l),std::max(0,std::min(target.height(),r[3]-camera.second)-t));}
        }
        scene.draw(target,camera.first,camera.second);
        const int beam=abilityUi.active && abilityUi.mode==3?abilityUi.beamTicks:beamTicks;
        if(beam>0 && beam<=38) {
            const int x=player.x-camera.first,y=player.y-camera.second,e=beam;
            const std::uint32_t inner=abilityUi.active?0x88009fffu:0x89000000u,outer=abilityUi.active?0x4480cfffu:0x45000000u;
            if(e<=5) { const int h=((y*256)/5*e)/256; fill(target,x-15,y-h,30,h,inner); fill(target,x-31,y-h,16,h,outer); fill(target,x+15,y-h,16,h,outer); }
            else if(e<=33) {
                int n=e<=19?(e-6)%8:e-20; if(e<=19 && n>3) n=7-n;
                const int a=(3840*n/14)/256,b=(2048*n/14)/256;
                fill(target,x-15+a,0,(15-a)*2,y,inner);
                fill(target,x-15-(8-b)*2+a,0,(8-b)*2,y,outer); fill(target,x+15-a,0,(8-b)*2,y,outer);
            } else fill(target,x-1,0,2,y-((e-33)*(y*256/5))/256,inner);
        }

        if(combat->fireMagic)combat->fireMagic->drawCharge(target,player,camera.first,camera.second);
        combat->drawPlatforms(target,camera.first,camera.second);
        for(auto& chest:containers) chest.draw(target,camera.first,camera.second);
        for(int i=0;i<placedPickups;++i) pickups[i].draw(target,camera.first,camera.second,std::max(0,ticks-1));
        for(auto& chest:containers) chest.drawPrompt(target,camera.first,camera.second);
        combat->drawEnemies(target,camera.first,camera.second);
        player.draw(target,camera.first,camera.second);
        weapon->draw(target,camera.first,camera.second);
        combat->drawPrompts(target,camera.first,camera.second,font);
        combat->drawEffects(target,camera.first,camera.second,font);
        for(std::size_t i=placedPickups;i<pickups.size();++i) pickups[i].draw(target,camera.first,camera.second,std::max(0,ticks-1));
        for(auto& effect:effects) effect.draw(target,camera.first,camera.second);
        for(auto& a:abilities)a.draw(target,camera.first,camera.second);
        target.resetClip();
        if(abilityUi.active && abilityUi.mode==14 && abilityUi.tutorial>=0){
            const int cx=target.width()/2;fill(target,cx-112,70,224,130,0xee102030);
            font.draw(target,abilityUi.tutorial==53?u"ICE BLADE ACQUIRED":u"MAGIC AWAKENED",cx,84,1);
            font.drawWrapped(target,abilityUi.tutorial==53?u"PRESS Q OR 9 TO SWITCH BETWEEN FIRE AND ICE.":u"PRESS K OR 0 TO CAST FIRE. MAGIC RECOVERS OVER TIME.",cx,112,208,1);
            if(abilityUi.tutorialFrames>8)font.draw(target,u"ENTER - CONTINUE",cx,177,1);
        }
        if(player.deathComplete) { fill(target,target.width()/2-100,120,200,70,0xdd102030); font.draw(target,u"DEFEATED",target.width()/2,132,1); font.draw(target,u"R TO RESTART",target.width()/2,165,1); }
        if(reward.active) {
            const int cx=target.width()/2;
            fill(target,0,0,target.width(),target.height(),0x99000000);
            fill(target,cx-111,30,222,252,0xff102030);
            for(int side:{-111,110}) fill(target,cx+side,30,1,252,0xffeed104);
            fill(target,cx-111,30,222,1,0xffeed104); fill(target,cx-111,281,222,1,0xffeed104);
            font.draw(target,reward.kind==0?u"HEALTH CRYSTAL":u"MAGIC CRYSTAL",cx,55,1);
            const int count=reward.displayed(meters);
            for(int i=0;i<4;++i) {
                const int x=cx-60+i*40;
                fill(target,x-12,107,24,26,0xff756440); fill(target,x-10,109,20,22,0xff102030);
                if(i<count) crystalRenderer.drawAnimation(target,reward.kind==0?1:3,0,x,127);
            }
            if(reward.upgraded) font.drawWrapped(target,u"MAXIMUM INCREASED",cx,163,200,1);
            else {
                const auto n=std::to_string(4-count); std::u16string text(n.begin(),n.end()); text+=u" MORE FOR A MAXIMUM INCREASE";
                font.drawWrapped(target,text,cx,163,200,1);
            }
            if(reward.frames>10 && ticks%10<5) font.draw(target,u"PRESS ANY GAME KEY",cx,253,1);
        }

    }
};
std::unique_ptr<MovementPreview> movementPreview;
std::array<bool,256> movementKeys{};
unsigned movementKey(WPARAM key) {
    if (key==VK_BACK) return 131072;
    if (key==VK_LEFT || key=='A') return 4096;
    if (key==VK_RIGHT || key=='D') return 8192;
    if (key==VK_UP || key=='W' || key==VK_SPACE) return 16384;
    if (key==VK_DOWN || key=='S') return 32768;
    if (key=='E' || key=='J' || key==VK_RETURN) return 65536;
    if (key=='K' || key=='0' || key==VK_NUMPAD0) return 1;
    if (key=='Q' || key=='9' || key==VK_NUMPAD9) return 512;
    return 0;
}
void loadPreview();
double previousX = 0, currentX = 0;
bool running = true;
bool fullscreen = false;
RECT savedRect{};
std::filesystem::path configPath;

int integer(const std::string& s) {
    size_t end = 0;
    int value = std::stoi(s, &end);
    if (end != s.size()) throw std::runtime_error("Invalid integer: " + s);
    return value;
}
void option(Settings& s, const std::string& key, const std::string& value) {
    if (key == "width") s.width = integer(value);
    else if (key == "fps") {
        if (value == "native") s.fps = 1.0 / soul::kLogicStep;
        else s.fps = integer(value);
    } else if (key == "fullscreen" || key == "interpolation") {
        if (value != "0" && value != "1") throw std::runtime_error(key + " must be 0 or 1");
        (key == "fullscreen" ? s.fullscreen : s.interpolation) = value == "1";
    } else throw std::runtime_error("Unknown setting: " + key);
    if (s.width < 240 || s.width > 2048) throw std::runtime_error("width must be 240..2048");
    if (s.fps < 1 || s.fps > 360) throw std::runtime_error("fps must be native or 1..360");
}
void loadConfig(const std::filesystem::path& path, bool required) {
    std::ifstream in(path);
    if (!in && required) throw std::runtime_error("Cannot open config: " + path.string());
    std::string line;
    while (std::getline(in, line)) {
        if (line.empty() || line[0] == ';' || line[0] == '#') continue;
        auto split = line.find('=');
        if (split == std::string::npos) throw std::runtime_error("Expected key=value in config");
        option(settings, line.substr(0, split), line.substr(split + 1));
    }
}
void saveConfig() {
    std::ofstream out(configPath);
    if (!out) throw std::runtime_error("Cannot save config: " + configPath.string());
    out << "width=" << settings.width << "\nfps=";
    if (std::abs(settings.fps - 1.0 / soul::kLogicStep) < 0.001) out << "native";
    else out << static_cast<int>(settings.fps);
    out << "\nfullscreen=" << settings.fullscreen
        << "\ninterpolation=" << settings.interpolation << '\n';
}
void update() {
    if(frontend) {
        if(gameScript) {
            if(frontendScriptStage==0 && frontend->state==18){frontend->press(13);frontendScriptStage=1;}
            else if(frontendScriptStage==1 && frontend->state==2){frontend->press(40);if(frontend->hasSave)frontend->press(40);frontend->press(13);frontendScriptStage=2;}
            else if(frontendScriptStage==2 && frontend->state==6 && frontend->frame>=30){frontend->press(27);frontendScriptStage=3;}
            else if(frontendScriptStage==3 && frontend->state==2){frontend->press(38);if(frontend->hasSave)frontend->press(38);frontend->press(13);if(frontend->state==22)frontend->press(13);frontendScriptStage=4;}
            else if(frontendScriptStage==4 && frontend->state==20 && frontend->frame>=30){frontend->press(13);frontendScriptStage=5;}
            else if(frontendScriptStage==5 && frontend->state==19 && frontend->frame>=5){frontend->press(13);frontendScriptStage=6;}
            else if(frontendScriptStage==6 && frontend->state==8 && frontend->frame>=12){frontend->press(27);frontendScriptStage=7;}
            else if(frontendScriptStage==7 && frontend->state==21 && frontend->frame>=6){frontend->press(27);frontendScriptStage=8;}
        }
        if(frontend->newGameRequested){
            frontend->newGameRequested=false;
            try{if(!gameSavePath.empty())soul::clearGameSave(gameSavePath);diskCheckpoint.reset();sessionCheckpoint.reset();frontend->hasSave=false;frontend->saveStatus.clear();}
            catch(const std::exception& e){frontend->enter(2);saveFailure(e);}
        }
        frontend->update();
        if(frontend->exitRequested){running=false;return;}
        if(frontend->startRequested){
            frontend->startRequested=false;previewOptions.movement=true;
            if(frontend->restartRequested){frontend->restartRequested=false;++checkpointMenuRestarts;restartGameplay();}
            else if(frontend->continueRequested){
                frontend->continueRequested=false;
                if(!diskCheckpoint){frontend->enter(2);return;}
                sessionCheckpoint=diskCheckpoint;movementKeys.fill(false);movementPreview.reset();restoringCheckpoint=true;
                try{loadPreview();restoringCheckpoint=false;}catch(...){restoringCheckpoint=false;throw;}
                const auto continued=diskCheckpoint->forContinue();
                movementPreview->restoreCheckpoint(continued);movementPreview->meters.health=continued.meters.health;frontend->enter(8);
            }
            else{
                sessionCheckpoint.reset();checkpointActivations=checkpointRestarts=checkpointMenuRestarts=checkpointFixtureStage=0;movementKeys.fill(false);movementPreview.reset();loadPreview();frontend->enter(8);
            }
        }
        if(frontend->state!=8)return;
    }
    previousX = currentX;
    currentX += 4; // Calibration marker, replaced by Game::update during the port.
    if (preview) preview->advance();
    if (fontPreview) ++fontPreview->stateFrame;
    if (movementPreview) {movementPreview->update();if(movementPreview->restartRequested)restartGameplay();}
    if (tilePreview) {
        const auto camera=tilePreview->clampCamera(tileCameraX+(panRight-panLeft)*20,tileCameraY+(panDown-panUp)*20,settings.width,soul::kHeight);
        tileCameraX=camera.first; tileCameraY=camera.second;
    }
}
void render() {
    if (framebuffer.width() != settings.width) framebuffer.resize(settings.width,soul::kHeight);
    framebuffer.resetClip();
    if(frontend && frontend->state!=8 && frontend->state!=21){frontend->draw(framebuffer);return;}
    if (movementPreview) {
        movementPreview->draw(framebuffer);
        if(frontend){
            if(!movementPreview->combat->intro || !movementPreview->combat->intro->active())movementPreview->drawHud(framebuffer);
            movementPreview->drawIntro(framebuffer);
            if(frontend->state==21)frontend->drawPause(framebuffer);
        }
        return;
    }
    if (tilePreview) {
        const auto camera=tilePreview->clampCamera(tileCameraX,tileCameraY,settings.width,soul::kHeight);
        framebuffer.clear(0xff102030); tilePreview->draw(framebuffer,camera.first,camera.second); return;
    }
    const double x = settings.interpolation
        ? previousX + (currentX - previousX) * clockState.alpha() : currentX;
    const int marker = static_cast<int>(x) % settings.width;
    for (int y = 0; y < soul::kHeight; ++y) {
        for (int col = 0; col < settings.width; ++col) {
            unsigned int color = (col % 32 == 0 || y % 32 == 0) ? 0xff28323f : 0xff101820;
            if (!preview && !fontPreview && col >= marker && col < marker + 8 && y >= 240 && y < 264) color = 0xffc85c63;
            pixels[static_cast<size_t>(y) * settings.width + col] = color;
        }
    }
    if (preview) preview->draw(framebuffer);
    if (fontPreview) fontPreview->draw(framebuffer);
}
void composeWindow(HDC dc, const RECT& client) {
    FillRect(dc, &client, static_cast<HBRUSH>(GetStockObject(BLACK_BRUSH)));
    int w = client.right, h = client.bottom;
    double scale = std::min(static_cast<double>(w) / settings.width, static_cast<double>(h) / soul::kHeight);
    int dw = static_cast<int>(settings.width * scale), dh = static_cast<int>(soul::kHeight * scale);
    BITMAPINFO info{};
    info.bmiHeader.biSize = sizeof(BITMAPINFOHEADER);
    info.bmiHeader.biWidth = settings.width;
    info.bmiHeader.biHeight = -soul::kHeight;
    info.bmiHeader.biPlanes = 1;
    info.bmiHeader.biBitCount = 32;
    info.bmiHeader.biCompression = BI_RGB;
    if (!pixels.empty()) StretchDIBits(dc, (w-dw)/2, (h-dh)/2, dw, dh, 0, 0,
        settings.width, soul::kHeight, pixels.data(), &info, DIB_RGB_COLORS, SRCCOPY);
    if (fontPreview || tilePreview || frontend) return;
    if (movementPreview) {
        SetBkMode(dc,TRANSPARENT); SetTextColor(dc,RGB(255,255,255));
        RECT status{12,std::max(0,h-44),std::max(13,w-12),h};
        std::wstring meterText=L"Health "+std::to_wstring(movementPreview->meters.health)+L"/"+std::to_wstring(movementPreview->meters.maxHealth)+L"   Magic "+std::to_wstring(movementPreview->meters.magic)+L"/"+std::to_wstring(movementPreview->meters.maxMagic)+L"   Pickups "+std::to_wstring(movementPreview->meters.collected);
        TextOutW(dc,12,12,meterText.c_str(),static_cast<int>(meterText.size()));
        DrawTextW(dc,movementPreview->crystalBoundary() ? L"Crystal reward: press a game key when prompted. R resets." : L"Movement prototype: A/D or arrows, Space jump, Down crouch, E attacks/opens chests. R resets. Enemies and tile hazards active.",-1,&status,DT_LEFT|DT_WORDBREAK);
        return;
    }
    SetBkMode(dc, TRANSPARENT);
    SetTextColor(dc, RGB(232, 232, 240));
    RECT text{24, 24, std::max(25, w-24), std::max(25, h-24)};
    std::wostringstream label;
    if (preview || fontPreview) label << L"Soul of Darkness - asset preview\n\nAsset viewer only; gameplay is not ported.\n\n";
    else label << L"Soul of Darkness - Windows port scaffold\n\n"
          << L"Use --game --data EXTRACTED_DIRECTORY for startup and gameplay preview.\n"
          << L"The moving marker checks frame pacing and interpolation.\n\n";
    label << L"Canvas: " << settings.width << L" x 320 | Display: " << settings.fps
          << L" FPS | Logic: 83 ms\n"
          << L"F8: aspect | F9: FPS | F10: interpolation | F11: fullscreen\n"
          << L"F5: save display.ini | Esc: exit";
    DrawTextW(dc, label.str().c_str(), -1, &text, DT_LEFT | DT_WORDBREAK);
}
void present(HWND window) {
    static soul::WindowBuffer buffer;
    PAINTSTRUCT ps{};
    HDC target = BeginPaint(window, &ps);
    RECT client{};
    GetClientRect(window, &client);
    if (HDC offscreen = buffer.prepare(target, client.right, client.bottom)) {
        composeWindow(offscreen, client);
        buffer.present(target);
    }
    EndPaint(window, &ps);
}
void setFullscreen(HWND window, bool enabled) {
    if (enabled == fullscreen) return;
    if (enabled) {
        GetWindowRect(window, &savedRect);
        MONITORINFO monitor{sizeof(MONITORINFO)};
        GetMonitorInfoW(MonitorFromWindow(window, MONITOR_DEFAULTTONEAREST), &monitor);
        SetWindowLongPtrW(window, GWL_STYLE, WS_POPUP | WS_VISIBLE);
        const RECT& r = monitor.rcMonitor;
        SetWindowPos(window, HWND_TOP, r.left, r.top, r.right-r.left, r.bottom-r.top, SWP_FRAMECHANGED);
    } else {
        SetWindowLongPtrW(window, GWL_STYLE, WS_OVERLAPPEDWINDOW | WS_VISIBLE);
        SetWindowPos(window, nullptr, savedRect.left, savedRect.top,
            savedRect.right-savedRect.left, savedRect.bottom-savedRect.top, SWP_FRAMECHANGED | SWP_NOZORDER);
    }
    fullscreen = settings.fullscreen = enabled;
}
LRESULT CALLBACK windowProc(HWND window, UINT message, WPARAM wparam, LPARAM lparam) {
    if(frontend && message==WM_KEYUP)frontend->release(static_cast<unsigned>(wparam));
    if(frontend && message==WM_KILLFOCUS){
        frontend->scrollKeys=0;
        if(frontend->state==8){frontend->selection=0;frontend->enter(21);}
    }
    if(frontend && message==WM_KEYDOWN && wparam<VK_F1 && !(lparam&(1LL<<30))) {
        const int before=frontend->state;frontend->press(static_cast<unsigned>(wparam));
        if(before!=8 || frontend->state!=8) {
            if(movementPreview){movementPreview->input.clear();movementKeys.fill(false);}
            return 0;
        }
    }
    if(frontend && frontend->state!=8 && (message==WM_KEYDOWN || message==WM_KEYUP) && wparam<VK_F1)return 0;
    if (message==WM_KILLFOCUS) panLeft=panRight=panUp=panDown=false;
    if (message==WM_KILLFOCUS && movementPreview) { movementKeys.fill(false); movementPreview->input.clear(); }
    if (movementPreview && (message==WM_KEYDOWN || message==WM_KEYUP) && wparam<movementKeys.size()) {
        const unsigned bit=movementKey(wparam); const bool down=message==WM_KEYDOWN;
        if (bit && movementKeys[wparam]!=down) {
            movementKeys[wparam]=down;
            if (down) movementPreview->input.press(bit);
            else {
                bool aliasHeld=false;
                for (std::size_t key=0;key<movementKeys.size();++key) if (movementKeys[key] && movementKey(key)==bit) aliasHeld=true;
                if (!aliasHeld) movementPreview->input.release(bit);
            }
        }
        if (wparam=='R' && down && !(lparam&(1LL<<30))) {
            restartGameplay(); render(); InvalidateRect(window,nullptr,FALSE);
        }
    }
    if (tilePreview && (message==WM_KEYDOWN || message==WM_KEYUP)) {
        const bool down=message==WM_KEYDOWN;
        if (wparam==VK_LEFT) panLeft=down;
        if (wparam==VK_RIGHT) panRight=down;
        if (wparam==VK_UP) panUp=down;
        if (wparam==VK_DOWN) panDown=down;
    }
    if (message == WM_DESTROY) { running = false; PostQuitMessage(0); return 0; }
    if (message == WM_ERASEBKGND) return 1;
    if (message == WM_PAINT) { present(window); return 0; }
    if (message == WM_KEYDOWN && !(lparam & (1LL << 30))) {
        if (wparam == VK_ESCAPE) DestroyWindow(window);
        if (wparam == VK_F11) setFullscreen(window, !fullscreen);
        if (wparam == VK_F10) settings.interpolation = !settings.interpolation;
        if (wparam == VK_F8) {
            constexpr int widths[] = {240, 427, 569, 747};
            auto it = std::find(std::begin(widths), std::end(widths), settings.width);
            settings.width = it == std::end(widths) || ++it == std::end(widths) ? widths[0] : *it;
            render();
        }
        if (wparam == VK_F9) {
            const double rates[] = {1.0/soul::kLogicStep, 30, 60, 120, 144};
            auto it = std::find(std::begin(rates), std::end(rates), settings.fps);
            settings.fps = it == std::end(rates) || ++it == std::end(rates) ? rates[0] : *it;
        }
        if (wparam == VK_F5) {
            try { saveConfig(); }
            catch (const std::exception& e) { MessageBoxA(window, e.what(), "Save failed", MB_OK | MB_ICONERROR); }
        }
        return 0;
    }
    return DefWindowProcW(window, message, wparam, lparam);
}
void require(bool result, const char* message) {
    if (!result) throw std::runtime_error(message);
}
void restartGameplay(){
    const auto saved=sessionCheckpoint;movementKeys.fill(false);movementPreview.reset();restoringCheckpoint=bool(saved);
    try{loadPreview();restoringCheckpoint=false;}catch(...){restoringCheckpoint=false;throw;}
    if(saved && movementPreview){movementPreview->restoreCheckpoint(*saved);++checkpointRestarts;}
    if(frontend)frontend->enter(8);
}
void loadPreview() {
    require(!previewOptions.containerScript || previewOptions.movement,"--container-script requires --movement-preview");
    require(int(previewOptions.containerScript)+int(previewOptions.movementScript)+int(previewOptions.meleeScript)+int(previewOptions.combatScript)+int(previewOptions.defeatScript)+int(previewOptions.platformScript)+int(previewOptions.swingScript)+int(previewOptions.bridgeScript)+int(previewOptions.regionScript)+int(previewOptions.gateScript)+int(previewOptions.gateTriggerScript)+int(previewOptions.passageScript)+int(previewOptions.abilityScript)+int(previewOptions.activationScript)+int(previewOptions.secondWaveScript)+int(previewOptions.arenaScript)+int(previewOptions.fireScript)+int(previewOptions.iceScript)+int(previewOptions.checkpointScript)+int(previewOptions.beamScript)+int(previewOptions.promptScript)+int(previewOptions.introScript)<=1,"Choose one validation script");
    require(!previewOptions.platformScript || previewOptions.movement,"--platform-script requires --movement-preview");
    require(!previewOptions.introScript || previewOptions.movement,"--intro-script requires --movement-preview");
    require(!previewOptions.promptScript || previewOptions.movement,"--prompt-script requires --movement-preview");
    require(!previewOptions.beamScript || previewOptions.movement,"--beam-script requires --movement-preview");
    require(!previewOptions.checkpointScript || previewOptions.movement,"--checkpoint-script requires --movement-preview");
    require(!previewOptions.iceScript || previewOptions.movement,"--ice-script requires --movement-preview");
    require(!previewOptions.fireScript || previewOptions.movement,"--fire-script requires --movement-preview");
    require(!previewOptions.arenaScript || previewOptions.movement,"--arena-script requires --movement-preview");
    require(!previewOptions.secondWaveScript || previewOptions.movement,"--second-wave-script requires --movement-preview");
    require(!previewOptions.activationScript || previewOptions.movement,"--activation-script requires --movement-preview");
    require(!previewOptions.abilityScript || previewOptions.movement,"--ability-script requires --movement-preview");
    require(!previewOptions.passageScript || previewOptions.movement,"--passage-script requires --movement-preview");
    require(!previewOptions.gateTriggerScript || previewOptions.movement,"--gate-trigger-script requires --movement-preview");
    require(!previewOptions.gateScript || previewOptions.movement,"--gate-script requires --movement-preview");
    require(!previewOptions.regionScript || previewOptions.movement,"--region-script requires --movement-preview");
    require(!previewOptions.bridgeScript || previewOptions.movement,"--bridge-script requires --movement-preview");
    require(!previewOptions.swingScript || previewOptions.movement,"--swing-script requires --movement-preview");
    require(!(previewOptions.combatScript || previewOptions.defeatScript) || previewOptions.movement,"Combat scripts require --movement-preview");
    require(!previewOptions.meleeScript || previewOptions.movement,"--melee-script requires --movement-preview");
    require(!previewOptions.movementScript || previewOptions.movement,"--movement-script requires --movement-preview");
    if (previewOptions.movement) {
        require(previewOptions.level<0 && !previewOptions.requested && !previewOptions.font,"Movement and asset previews are mutually exclusive");
        require(!previewOptions.cameraRequested,"Movement prototype uses its own following camera");
        require(!previewOptions.data.empty(),"--movement-preview requires --data EXTRACTED_DIRECTORY");
        require(previewOptions.frame<0 && previewOptions.animation<0 && previewOptions.palette==0 && previewOptions.flip==0,"Movement prototype does not use sprite selectors");
        auto world=soul::TileScene::load(previewOptions.data,0);
        const auto pack=soul::ResourcePack::numeric(soul::readAssetFile(previewOptions.data/"3"));
        auto records=soul::actorRecords(pack.resource(0).value());
        const auto sprites=soul::ResourcePack::numeric(soul::readAssetFile(previewOptions.data/"2"));
        const auto textPack=soul::ResourcePack::numeric(soul::readAssetFile(previewOptions.data/"1"));
        const auto statsPack=soul::ResourcePack::numeric(soul::readAssetFile(previewOptions.data/"18"));
        soul::BinaryReader statsReader(statsPack.resource(0).value());
        const int tableCount=statsReader.i32be(); require(tableCount==20,"Invalid stat table count"); std::vector<int> enemyStats,flyingStats,beamStats;
        for(int table=0;table<tableCount;++table) { const int count=statsReader.i32be(); require(count>=0 && count<10000,"Invalid stat row"); for(int i=0;i<count;++i) { const int value=statsReader.i32be(); if(table==0) enemyStats.push_back(value);if(table==2)flyingStats.push_back(value);if(table==9)beamStats.push_back(value); } } statsReader.done();
        movementPreview=std::make_unique<MovementPreview>(std::move(world),soul::BSprite::parse(sprites.resource(0).value()),std::move(records),soul::BSprite::parse(sprites.resource(33).value()),soul::BSprite::parse(sprites.resource(34).value()),soul::BSprite::parse(sprites.resource(18).value()),soul::BSprite::parse(textPack.resource(0).value()),soul::BSprite::parse(sprites.resource(1).value()),soul::BSprite::parse(sprites.resource(25).value()),soul::BSprite::parse(sprites.resource(2).value()),soul::BSprite::parse(sprites.resource(28).value()),soul::BSprite::parse(sprites.resource(36).value()),enemyStats,flyingStats,beamStats);
        return;
    }
    if (previewOptions.level>=0) {
        require(!previewOptions.requested && !previewOptions.font,"Level, font and sprite previews are mutually exclusive");
        require(!previewOptions.data.empty(),"--level-preview requires --data EXTRACTED_DIRECTORY");
        require(previewOptions.frame<0 && previewOptions.animation<0 && previewOptions.flip==0 && previewOptions.palette==0,"Tile preview does not use sprite selectors");
        tilePreview=std::make_unique<soul::TileScene>(soul::TileScene::load(previewOptions.data,previewOptions.level));
        const auto camera=tilePreview->clampCamera(previewOptions.cameraX,previewOptions.cameraY,settings.width,soul::kHeight);
        tileCameraX=camera.first; tileCameraY=camera.second; return;
    }
    require(!previewOptions.cameraRequested,"Camera coordinates require --level-preview");
    if (previewOptions.font) {
        require(!previewOptions.requested,"--font-preview and --sprite are mutually exclusive");
        require(!previewOptions.data.empty(),"--font-preview requires --data EXTRACTED_DIRECTORY");
        require(previewOptions.frame<0 && previewOptions.animation<0 && previewOptions.flip==0,"Font preview does not accept frame/animation/flip selectors");
        require(previewOptions.palette<3,"Font preview palette must be 0..2");
        const auto fonts=soul::ResourcePack::numeric(soul::readAssetFile(previewOptions.data/"1"));
        const auto icons=soul::ResourcePack::numeric(soul::readAssetFile(previewOptions.data/"2"));
        fontPreview=std::make_unique<FontPreview>(soul::BSprite::parse(fonts.resource(0).value()),soul::BSprite::parse(icons.resource(33).value()));
        return;
    }
    if (!previewOptions.requested) {
        require(!previewOptions.extra,"Asset preview options require --sprite"); return;
    }
    require(!previewOptions.data.empty(),"--sprite requires --data EXTRACTED_DIRECTORY");
    require(previewOptions.frame < 0 || previewOptions.animation < 0,"--frame and --animation are mutually exclusive");
    const auto pack = soul::ResourcePack::numeric(soul::readAssetFile(previewOptions.data/std::to_string(previewOptions.pack)));
    const auto resource = pack.resource(previewOptions.resource);
    require(resource.has_value(),"Selected sprite resource is absent");
    auto loaded = std::make_unique<Preview>(soul::BSprite::parse(*resource));
    loaded->style.palette = previewOptions.palette; loaded->style.flips = previewOptions.flip;
    // A preview palette must work on every module; the original mutable
    // image-cache palette fallback is intentionally not part of this viewer.
    for (std::size_t m = 0; m < loaded->sprite.modules().size(); ++m)
        require(loaded->style.palette < loaded->sprite.paletteCount(m),"Preview palette is unavailable for one or more modules");
    if (previewOptions.frame >= 0) {
        require(static_cast<std::size_t>(previewOptions.frame) < loaded->sprite.frames().size(),"Preview frame out of range");
        loaded->frame = previewOptions.frame;
    } else if (!loaded->sprite.animations().empty() || previewOptions.animation >= 0) {
        loaded->animation = previewOptions.animation >= 0 ? previewOptions.animation : 0;
        require(static_cast<std::size_t>(loaded->animation) < loaded->sprite.animations().size(),"Preview animation out of range");
        require(loaded->sprite.animations()[loaded->animation].count > 0,"Preview animation is empty");
    } else if (!loaded->sprite.frames().empty()) loaded->frame = 0;
    else {
        require(!loaded->sprite.images().empty(),"Sprite has no previewable image/frame");
        require(loaded->style.palette < loaded->sprite.images()[0].size(),"Preview image palette out of range");
    }
    preview = std::move(loaded);
}
void selfTest() {
    for (int rate : {30, 60, 120, 144, 240}) {
        soul::Clock clock;
        int updates = 0;
        for (int frame = 0; frame < 83 * rate; ++frame) clock.advance(1.0/rate, [&] { ++updates; });
        require(updates == 1000, "Display FPS altered simulation speed");
        require(clock.alpha() >= 0 && clock.alpha() < 1, "Interpolation alpha out of range");
    }
    soul::Clock stalled;
    stalled.advance(10, [] {});
    require(stalled.ticks == 3, "Stall catch-up is not bounded");
    Settings s;
    option(s, "width", "240"); option(s, "fps", "native");
    require(s.width == 240 && std::abs(s.fps*soul::kLogicStep - 1) < 1e-10, "Native settings failed");
    for (const auto& pair : {std::pair{"fps", "0"}, {"width", "239"}, {"fps", "60junk"}, {"fullscreen", "2"}}) {
        bool rejected = false;
        try { option(s, pair.first, pair.second); } catch (const std::exception&) { rejected = true; }
        require(rejected, "Invalid option accepted");
    }
    std::cout << "PASS: fixed timing at 30/60/120/144/240 FPS, interpolation, stalls, native settings, invalid options\n";
}
}

int main(int argc, char** argv) {
    try {
        wchar_t exe[MAX_PATH];
        GetModuleFileNameW(nullptr, exe, MAX_PATH);
        configPath = std::filesystem::path(exe).parent_path() / "display.ini";
        bool explicitConfig = false;
        for (int i = 1; i < argc; ++i) if (std::string(argv[i]) == "--config") {
            if (++i == argc) throw std::runtime_error("--config requires a path");
            configPath = argv[i]; explicitConfig = true;
        }
        // Automated runs ignore user settings unless explicitly supplied.
        bool automated = false;
        for (int i = 1; i < argc; ++i)
            if (std::string(argv[i]) == "--headless" || std::string(argv[i]) == "--self-test") automated = true;
        if (!automated || explicitConfig) loadConfig(configPath, explicitConfig);
        automatedRun=automated;
        bool headless = false;
        int frames = 120;
        std::string dump;
        for (int i = 1; i < argc; ++i) {
            std::string arg = argv[i];
            auto next = [&]() -> std::string {
                if (++i == argc) throw std::runtime_error("Missing value for " + arg);
                return argv[i];
            };
            if (arg == "--self-test") { selfTest(); return 0; }
            else if (arg == "--headless") headless = true;
            else if (arg == "--game") gameMode=true;
            else if (arg == "--save-file") gameSavePath=next();
            else if (arg == "--continue") continueOnLaunch=true;
            else if (arg == "--platform-script") previewOptions.platformScript=true;
            else if (arg == "--ice-projectile-script") {previewOptions.iceScript=true;iceProjectileFixture=true;}
            else if (arg == "--game-ice-projectile-script") {gameScript=true;gameIceScript=true;iceProjectileFixture=true;}
            else if (arg == "--intro-script") previewOptions.introScript=true;
            else if (arg == "--intro-skip-script") {previewOptions.introScript=true;introSkipFixture=true;}
            else if (arg == "--game-intro-script") {gameScript=true;gameIntroScript=true;}
            else if (arg == "--game-intro-skip-script") {gameScript=true;gameIntroScript=true;introSkipFixture=true;}
            else if (arg == "--prompt-script") previewOptions.promptScript=true;
            else if (arg == "--game-prompt-script") {gameScript=true;gamePromptScript=true;}
            else if (arg == "--beam-script") previewOptions.beamScript=true;
            else if (arg == "--game-beam-script") {gameScript=true;gameBeamScript=true;}
            else if (arg == "--checkpoint-script") previewOptions.checkpointScript=true;
            else if (arg == "--game-checkpoint-script") {gameScript=true;gameCheckpointScript=true;}
            else if (arg == "--ice-script") previewOptions.iceScript=true;
            else if (arg == "--game-ice-script") {gameScript=true;gameIceScript=true;}
            else if (arg == "--fire-script") previewOptions.fireScript=true;
            else if (arg == "--game-fire-script") {gameScript=true;gameFireScript=true;}
            else if (arg == "--game-arena-fire-script") {gameScript=true;gameArenaScript=true;gameFireAfterArena=true;}
            else if (arg == "--arena-script") previewOptions.arenaScript=true;
            else if (arg == "--game-arena-script") {gameScript=true;gameArenaScript=true;}
            else if (arg == "--second-wave-script") previewOptions.secondWaveScript=true;
            else if (arg == "--game-second-wave-script") {gameScript=true;gameSecondWaveScript=true;}
            else if (arg == "--activation-script") previewOptions.activationScript=true;
            else if (arg == "--ability-script") previewOptions.abilityScript=true;
            else if (arg == "--passage-script") previewOptions.passageScript=true;
            else if (arg == "--gate-trigger-script") previewOptions.gateTriggerScript=true;
            else if (arg == "--gate-script") previewOptions.gateScript=true;
            else if (arg == "--region-script") previewOptions.regionScript=true;
            else if (arg == "--bridge-script") previewOptions.bridgeScript=true;
            else if (arg == "--swing-script") previewOptions.swingScript=true;
            else if (arg == "--game-activation-script") {gameScript=true;gameActivationScript=true;}
            else if (arg == "--game-ability-script") {gameScript=true;gameAbilityScript=true;}
            else if (arg == "--game-passage-script") {gameScript=true;gamePassageScript=true;}
            else if (arg == "--game-script") gameScript=true;
            else if (arg == "--calibration") calibrationRequested=true;
            else if (arg == "--width" || arg == "--fps") option(settings, arg.substr(2), next());
            else if (arg == "--frames") { frames = integer(next()); require(frames > 0, "frames must be positive"); }
            else if (arg == "--dump") dump = next();
            else if (arg == "--config") next();
            else if (arg == "--sprite") {
                const auto id = next(); const auto split = id.find(':');
                require(split != std::string::npos,"--sprite requires PACK:RESOURCE");
                previewOptions.pack = integer(id.substr(0,split)); previewOptions.resource = integer(id.substr(split+1));
                require(previewOptions.pack >= 1 && previewOptions.pack <= 19 && previewOptions.resource >= 0,"Invalid sprite pack/resource");
                previewOptions.requested = true;
            }
            else if (arg == "--font-preview") previewOptions.font = true;
            else if (arg == "--movement-preview") previewOptions.movement = true;
            else if (arg == "--movement-script") previewOptions.movementScript = true;
            else if (arg == "--container-script") previewOptions.containerScript = true;
            else if (arg == "--combat-script") previewOptions.combatScript = true;
            else if (arg == "--defeat-script") previewOptions.defeatScript = true;
            else if (arg == "--melee-script") previewOptions.meleeScript = true;
            else if (arg == "--level-preview") {
                previewOptions.level=integer(next()); require(previewOptions.level>=0 && previewOptions.level<=8,"--level-preview must be 0..8");
            }
            else if (arg == "--camera-x" || arg == "--camera-y") {
                const int value=integer(next());
                if (arg=="--camera-x") previewOptions.cameraX=value; else previewOptions.cameraY=value;
                previewOptions.cameraRequested=true;
            }
            else if (arg == "--data") { previewOptions.data = next(); previewOptions.extra = true; }
            else if (arg == "--frame" || arg == "--animation" || arg == "--palette" || arg == "--flip") {
                const int value = integer(next()); require(value >= 0,"Preview selectors must be nonnegative");
                if (arg == "--frame") previewOptions.frame = value;
                else if (arg == "--animation") previewOptions.animation = value;
                else if (arg == "--palette") previewOptions.palette = value;
                else { require(value < 4,"--flip must be 0..3"); previewOptions.flip = value; }
                previewOptions.extra = true;
            }
            else if (arg == "--fullscreen") settings.fullscreen = true;
            else if (arg == "--windowed") settings.fullscreen = false;
            else if (arg == "--no-interpolation") settings.interpolation = false;
            else if (arg == "--help") {
                std::cout << "soul_port [--width 240..2048] [--fps native|1..360] [--fullscreen|--windowed]\n"
                             "  [--config PATH] [--no-interpolation] [--headless --frames N --dump image.ppm]\n"
                             "  [--self-test]\nNative startup and first-level preview; full gameplay is not ported.\n";
                std::cout << "  [--game --data EXTRACTED_DIRECTORY] [--save-file PATH] [--continue] [--calibration]\n"
                             "  [--sprite PACK:RESOURCE --data EXTRACTED_DIRECTORY]\n"
                             "  [--font-preview --data EXTRACTED_DIRECTORY] [--palette 0..2]\n"
                             "  [--level-preview 0..8 --data EXTRACTED_DIRECTORY] [--camera-x X --camera-y Y]\n"
                             "  Level preview: arrows pan, F8 changes width; tiles only, no gameplay.\n"
                             "  [--movement-preview --data EXTRACTED_DIRECTORY] [--movement-script|--container-script|--melee-script|--combat-script|--defeat-script|--platform-script|--swing-script|--bridge-script|--region-script|--gate-script|--gate-trigger-script|--passage-script|--ability-script|--activation-script|--second-wave-script|--arena-script|--fire-script|--ice-script|--ice-projectile-script|--checkpoint-script|--beam-script|--prompt-script|--intro-script|--intro-skip-script]\n"
                             "  Player prototype: arrows/A/D, Space jump, Down crouch, E/J/Enter attacks/opens chests, K/0 casts equipped magic, Q/9 switches weapons, R restarts at the session checkpoint; ordinary enemies and combat.\n"
                             "  [--animation ID|--frame ID] [--palette ID] [--flip 0..3]\n"
                             "Interactive first-level combat preview is available; the complete game is not ported.\n";
                return 0;
            } else throw std::runtime_error("Unknown argument: " + arg);
        }
        require(!previewOptions.platformScript || headless,"--platform-script is a headless validation option");
        require(!previewOptions.introScript || headless,"--intro-script is a headless validation option");
        require(!previewOptions.promptScript || headless,"--prompt-script is a headless validation option");
        require(!previewOptions.beamScript || headless,"--beam-script is a headless validation option");
        require(!previewOptions.checkpointScript || headless,"--checkpoint-script is a headless validation option");
        require(!previewOptions.iceScript || headless,"--ice-script is a headless validation option");
        require(!previewOptions.fireScript || headless,"--fire-script is a headless validation option");
        require(!previewOptions.arenaScript || headless,"--arena-script is a headless validation option");
        require(!previewOptions.secondWaveScript || headless,"--second-wave-script is a headless validation option");
        require(!previewOptions.activationScript || headless,"--activation-script is a headless validation option");
        require(!previewOptions.abilityScript || headless,"--ability-script is a headless validation option");
        require(!previewOptions.passageScript || headless,"--passage-script is a headless validation option");
        require(!previewOptions.gateTriggerScript || headless,"--gate-trigger-script is a headless validation option");
        require(!previewOptions.gateScript || headless,"--gate-script is a headless validation option");
        require(!previewOptions.regionScript || headless,"--region-script is a headless validation option");
        require(!previewOptions.bridgeScript || headless,"--bridge-script is a headless validation option");
        require(!previewOptions.swingScript || headless,"--swing-script is a headless validation option");
        require(dump.empty() || headless, "--dump requires --headless");
        require(!previewOptions.containerScript || headless,"--container-script is a headless validation option");
        require(!(previewOptions.combatScript || previewOptions.defeatScript) || headless,"Combat scripts are headless validation options");
        require(!previewOptions.meleeScript || headless,"--melee-script is a headless validation option");
        require(!previewOptions.movementScript || headless,"--movement-script is a headless validation option");
        if(!headless && !calibrationRequested && !previewOptions.movement && !previewOptions.font && !previewOptions.requested && previewOptions.level<0) {
            if(previewOptions.data.empty()) {
                wchar_t executable[MAX_PATH]{};GetModuleFileNameW(nullptr,executable,MAX_PATH);
                const auto executableDir=std::filesystem::path(executable).parent_path();
                for(const auto& candidate:{std::filesystem::current_path()/"soul/extracted",executableDir/"data",executableDir/"../../extracted",executableDir/"../../../extracted"})
                    if(std::filesystem::exists(candidate/"1") && std::filesystem::exists(candidate/"logo.png")){previewOptions.data=std::filesystem::weakly_canonical(candidate);break;}
            }
            if(previewOptions.data.empty()){
                const char* message="Game data was not found. Run setup.ps1 in the workspace, place your extracted game files in a data folder beside soul_port.exe, or launch with --game --data PATH. Use --calibration for the frame test.";
                MessageBoxA(nullptr,message,"Soul of Darkness - Game data required",MB_OK|MB_ICONERROR);
                throw std::runtime_error(message);
            }
            gameMode=true;
        }
        require(!(gameMode && calibrationRequested),"Choose --game or --calibration");
        require(int(gameAbilityScript)+int(gamePassageScript)+int(gameActivationScript)+int(gameSecondWaveScript)+int(gameArenaScript)+int(gameFireScript)+int(gameIceScript)+int(gameCheckpointScript)+int(gameBeamScript)+int(gamePromptScript)+int(gameIntroScript)<=1,"Choose one game validation fixture");
        require(!gameScript || (gameMode && headless),"--game-script requires --game --headless");
        require(!continueOnLaunch || (gameMode && !gameScript),"--continue requires --game without validation scripts");
        if(gameMode) {
            if(gameSavePath.empty() && !automated){
                PWSTR local=nullptr;
                if(SUCCEEDED(SHGetKnownFolderPath(FOLDERID_LocalAppData,0,nullptr,&local))){gameSavePath=std::filesystem::path(local)/"SoulOfDarkness"/"checkpoint.sav";CoTaskMemFree(local);}
                else throw std::runtime_error("Cannot locate LocalAppData for game saves");
            }
            require(!previewOptions.data.empty(),"--game requires --data EXTRACTED_DIRECTORY");
            require(!previewOptions.movement && !previewOptions.font && !previewOptions.requested && previewOptions.level<0,"--game cannot be combined with preview modes");
            require(!(previewOptions.movementScript || previewOptions.containerScript || previewOptions.meleeScript || previewOptions.combatScript || previewOptions.defeatScript || previewOptions.platformScript || previewOptions.regionScript || previewOptions.gateScript || previewOptions.gateTriggerScript || previewOptions.passageScript || previewOptions.abilityScript || previewOptions.activationScript || previewOptions.secondWaveScript || previewOptions.arenaScript || previewOptions.fireScript || previewOptions.iceScript || previewOptions.checkpointScript || previewOptions.beamScript || previewOptions.promptScript || previewOptions.introScript || previewOptions.bridgeScript || previewOptions.swingScript),"--game cannot use movement validation scripts");
            frontend=std::make_unique<soul::GameFrontend>(previewOptions.data);
            if(!gameSavePath.empty())try{diskCheckpoint=soul::loadGameSave(gameSavePath);frontend->hasSave=diskCheckpoint.has_value();}catch(const std::exception& e){saveFailure(e);}
            if(continueOnLaunch){require(bool(diskCheckpoint),"No valid checkpoint to continue");frontend->enter(2);frontend->selection=1;frontend->press(13);}
        } else loadPreview();
        if (headless) {
            for (int i = 0; i < frames; ++i) clockState.advance(1.0/settings.fps, update);
            render();
            if (!dump.empty()) {
                std::ofstream out(dump, std::ios::binary);
                require(static_cast<bool>(out), "Cannot open dump");
                out << "P6\n" << settings.width << " 320\n255\n";
                for (unsigned int pixel : pixels) {
                    const char rgb[]{static_cast<char>(pixel >> 16), static_cast<char>(pixel >> 8), static_cast<char>(pixel)};
                    out.write(rgb, 3);
                }
                require(static_cast<bool>(out), "Cannot write dump");
            }
            std::cout << "frames=" << frames << " ticks=" << clockState.ticks << " width=" << settings.width << " disk_checkpoint=" << (diskCheckpoint?diskCheckpoint->uid:-1) << " continue_available=" << (frontend && frontend->hasSave);
            if (preview) std::cout << " preview_step=" << preview->step << " preview_tick=" << preview->tick << " preview_crc=" << framebuffer.checksum();
            if (fontPreview) std::cout << " font_tick=" << fontPreview->stateFrame << " font_crc=" << framebuffer.checksum();
            if (tilePreview) std::cout << " camera_x=" << tileCameraX << " camera_y=" << tileCameraY << " tile_crc=" << framebuffer.checksum();
            if (movementPreview) {
                const auto& p=movementPreview->player;
                const auto swing=std::find_if(movementPreview->combat->platforms.begin(),movementPreview->combat->platforms.end(),[](const auto& v){return v.type==50 && v.uid==4;});
                const auto support=std::find_if(movementPreview->combat->platforms.begin(),movementPreview->combat->platforms.end(),[&](const auto& v){return &v.surface==p.platform;});
                if(swing!=movementPreview->combat->platforms.end())std::cout<<" swing_x="<<swing->x<<" swing_y="<<swing->y<<" swing_angle="<<swing->angle<<" swing_state="<<swing->action<<" support_uid="<<(support==movementPreview->combat->platforms.end()?-1:support->uid);
                std::cout << " player_x=" << p.x << " player_y=" << p.y << " player_vx=" << p.vx << " player_vy=" << p.vy
                    << " player_animation=" << p.animation << " player_step=" << p.step << " player_tick=" << p.animationTick
                    << " intro_trigger_alive=" << std::count_if(movementPreview->combat->introTriggers.begin(),movementPreview->combat->introTriggers.end(),[](const auto& t){return t.alive;}) << " intro_bird83_alive=" << std::any_of(movementPreview->combat->flyingEnemies.begin(),movementPreview->combat->flyingEnemies.end(),[](const auto& e){return e.uid==83 && e.alive;}) << " intro_prompt_hidden=" << std::any_of(movementPreview->combat->prompts.begin(),movementPreview->combat->prompts.end(),[](const auto& p){return p.uid==267 && (p.flags&128);})
                    << " intro_control_ticks=" << movementPreview->introControlTicks
                    << " intro_active=" << (movementPreview->combat->intro && movementPreview->combat->intro->active()) << " intro_tick=" << (movementPreview->combat->intro?movementPreview->combat->intro->ticks:-1) << " intro_dialogue=" << (movementPreview->combat->intro?movementPreview->combat->intro->dialogue:-1) << " intro_page=" << (movementPreview->combat->intro?movementPreview->combat->intro->page:0) << " intro_starts=" << (movementPreview->combat->intro?movementPreview->combat->intro->starts:0) << " intro_finishes=" << (movementPreview->combat->intro?movementPreview->combat->intro->finishes:0) << " intro_skips=" << (movementPreview->combat->intro?movementPreview->combat->intro->skips:0) << " intro_dialogues_opened=" << (movementPreview->combat->intro?movementPreview->combat->intro->dialoguesOpened:0)
                    << " fire_prompt_alive=" << std::any_of(movementPreview->combat->prompts.begin(),movementPreview->combat->prompts.end(),[](const auto& p){return p.uid==8 && p.alive;}) << " fire_prompt_status=" << ([&]{for(const auto& p:movementPreview->combat->prompts)if(p.uid==8)return p.status;return -1;}())
                    << " placed_prompts=" << movementPreview->combat->prompts.size() << " prompts_active=" << std::count_if(movementPreview->combat->prompts.begin(),movementPreview->combat->prompts.end(),[](const auto& p){return p.alive;}) << " prompts_visible=" << movementPreview->combat->visiblePrompts() << " prompts_removed=" << movementPreview->combat->promptsRemoved << " prompt_magic=" << movementPreview->combat->recentPromptMagic
                    << " beam_emitters=" << movementPreview->combat->beamEmitters.size() << " beams_active=" << std::count_if(movementPreview->combat->beamEmitters.begin(),movementPreview->combat->beamEmitters.end(),[](const auto& e){return e.active;}) << " beam_hits=" << movementPreview->combat->beamHits
                    << " checkpoint_uid=" << (sessionCheckpoint?sessionCheckpoint->uid:-1) << " checkpoint_activations=" << checkpointActivations << " checkpoint_restarts=" << checkpointRestarts << " checkpoint_menu_restarts=" << checkpointMenuRestarts << " checkpoints_active=" << std::count_if(movementPreview->combat->checkpoints.begin(),movementPreview->combat->checkpoints.end(),[](const auto& c){return c.alive;}) << " checkpoint_magic=" << (sessionCheckpoint?sessionCheckpoint->meters.magic:-1) << " checkpoint_fixture_stage=" << checkpointFixtureStage
                    << " region_pickups_removed=" << movementPreview->regionPickupsRemoved << " region_effects_removed=" << movementPreview->regionEffectsRemoved
                    << " spawned_pickups_active=" << std::count_if(movementPreview->pickups.begin(),movementPreview->pickups.end(),[](const auto& item){return item.alive && item.uid==-1;})
                    << " linked_area_triggers=" << movementPreview->combat->linkedAreaTriggers.size()
                    << " ice_shots=" << movementPreview->combat->iceMagic->casts << " ice_hits=" << movementPreview->combat->iceMagic->hits << " ice_platforms=" << movementPreview->combat->iceMagic->platformsCreated << " ice_projectiles_active=" << movementPreview->combat->iceMagic->projectiles.size() << " ice_landings=" << movementPreview->iceLandings << " ice_detachments=" << movementPreview->iceDetachments << " ice_riding_ticks=" << movementPreview->iceRidingTicks << " ice_ride_y=" << movementPreview->iceRideY << " ice_fixture_stage=" << movementPreview->iceShotStage << " active_weapon=" << movementPreview->player.activeWeapon << " ice_unlocked=" << movementPreview->meters.iceUnlocked << " weapon_switches=" << movementPreview->combat->weaponSwitches << " weapon_switch_cooldown=" << movementPreview->player.weaponSwitchCooldown << " ice_attacks=" << movementPreview->iceMeleeAttacks << " ice_poses=" << movementPreview->iceMeleePoses << " fire_casts=" << movementPreview->combat->fireMagic->casts << " fire_hits=" << movementPreview->combat->fireMagic->hits << " fire_active=" << movementPreview->combat->fireMagic->pulses.size() << " fire_warning=" << movementPreview->combat->fireMagic->warningMs << " fire_regen=" << movementPreview->combat->fireMagic->regenerationMs << " fire_charge=" << movementPreview->player.magicCharge << " chest_enables=" << movementPreview->combat->containerEnables << " arena_chests_locked=" << std::count_if(movementPreview->containers.begin(),movementPreview->containers.end(),[](const auto& c){return (c.uid==56 || c.uid==57) && (c.flags&32);}) << " flying_enemies=" << movementPreview->combat->flyingEnemies.size() << " wave2_alive=" << std::count_if(movementPreview->combat->flyingEnemies.begin(),movementPreview->combat->flyingEnemies.end(),[](const auto& e){return (e.uid==113 || e.uid==48) && e.alive;}) << " wave2_locked=" << std::count_if(movementPreview->combat->flyingEnemies.begin(),movementPreview->combat->flyingEnemies.end(),[](const auto& e){return (e.uid==113 || e.uid==48) && e.alive && (e.flags&32);}) << " removed_enemy_activations=" << movementPreview->combat->removedEnemyActivations
                    << " player_enable_triggers=" << movementPreview->combat->playerEnableTriggers.size() << " player_enable_active=" << std::count_if(movementPreview->combat->playerEnableTriggers.begin(),movementPreview->combat->playerEnableTriggers.end(),[](const auto& t){return t.alive;}) << " player_enable_activations=" << movementPreview->combat->playerEnableActivations << " wave1_alive=" << std::count_if(movementPreview->combat->enemies.begin(),movementPreview->combat->enemies.end(),[](const auto& e){return (e.uid==114 || e.uid==117) && e.alive;}) << " wave1_locked=" << std::count_if(movementPreview->combat->enemies.begin(),movementPreview->combat->enemies.end(),[](const auto& e){return (e.uid==114 || e.uid==117) && e.alive && (e.flags&32);})
                    << " abilities=" << movementPreview->abilities.size() << " abilities_active=" << std::count_if(movementPreview->abilities.begin(),movementPreview->abilities.end(),[](const auto& a){return a.alive;}) << " magic_unlocked=" << movementPreview->meters.magicUnlocked << " ability_unlocks=" << movementPreview->abilityUnlocks << " ability_completed=" << movementPreview->abilityCompleted << " ability_modal=" << movementPreview->abilityUi.mode << " ability_beam=" << movementPreview->abilityUi.beamTicks << " ability_tutorial_frame=" << movementPreview->abilityUi.tutorialFrames << " region_abilities_removed=" << movementPreview->regionAbilitiesRemoved
                    << " passages=" << movementPreview->combat->passages.size() << " passage_transfers=" << movementPreview->combat->passageTransfers << " last_passage=" << movementPreview->combat->lastPassageUid << " region=" << movementPreview->combat->currentRegion()
                    << " gates=" << movementPreview->combat->gates.size()
                    << " gate_script_loaded=" << movementPreview->combat->gateScriptLoaded() << " gate_script_tick=" << movementPreview->combat->gateScriptTicks()
                    << " gate_triggers=" << movementPreview->combat->removedScriptTriggers.size() << " gate_triggers_active=" << std::count_if(movementPreview->combat->removedScriptTriggers.begin(),movementPreview->combat->removedScriptTriggers.end(),[](const auto& t){return t.alive;})
                    << " gate_y=" << (movementPreview->combat->gates.empty()?-1:movementPreview->combat->gates.front().y) << " gate_action=" << (movementPreview->combat->gates.empty()?-1:movementPreview->combat->gates.front().action)
                    << " bridges=" << movementPreview->combat->bridges.size() << " bridges_active=" << std::count_if(movementPreview->combat->bridges.begin(),movementPreview->combat->bridges.end(),[](const auto& b){return b.alive;})
                    << " platforms=" << movementPreview->combat->platforms.size() << " supported=" << (p.platform!=nullptr) << " platform_y=" << movementPreview->combat->platforms.front().y << " platform_vy=" << movementPreview->combat->platforms.front().vy << " enemies=" << movementPreview->combat->enemies.size() << " dormant_enemies=" << movementPreview->combat->dormant << " enemy_hits=" << movementPreview->combat->enemyHits << " player_hits=" << movementPreview->combat->playerHits << " enemy_kills=" << movementPreview->combat->kills << " loot_spawned=" << movementPreview->combat->lootSpawned << " combat_effects=" << movementPreview->combat->effectsSpawned << " invulnerability=" << movementPreview->combat->damage.invulnerability << " defeated=" << p.deathComplete << " melee_attacks=" << movementPreview->meleeAttacks << " melee_poses=" << movementPreview->meleePoses << " weapon_visible=" << movementPreview->weapon->visible() << " weapon_animation=" << movementPreview->weapon->animation << " weapon_step=" << movementPreview->weapon->step << " weapon_tick=" << movementPreview->weapon->tick << " player_health=" << movementPreview->meters.health << " player_magic=" << movementPreview->meters.magic
                    << " collection_effects=" << movementPreview->effectsSpawned << " active_effects=" << movementPreview->effects.size() << " player_collected=" << movementPreview->meters.collected << " opened_containers=" << movementPreview->openedContainers << " crystal_boundary=" << movementPreview->crystalBoundary() << " reward_active=" << movementPreview->reward.active << " rewards_completed=" << movementPreview->rewardsCompleted << " health_fragments=" << movementPreview->meters.healthFragments << " magic_fragments=" << movementPreview->meters.magicFragments << " player_hazard=" << p.stoppedByHazard() << " movement_crc=" << framebuffer.checksum();
            }
            if(frontend)std::cout << " frontend_state=" << frontend->state << " frontend_frame=" << frontend->frame << " frontend_crc=" << framebuffer.checksum();
            std::cout << '\n';
            return 0;
        }
        HINSTANCE instance = GetModuleHandleW(nullptr);
        WNDCLASSW wc{};
        wc.lpfnWndProc = windowProc; wc.hInstance = instance;
        wc.lpszClassName = L"SoulPort"; wc.hCursor = LoadCursorW(nullptr, IDC_ARROW);
        require(RegisterClassW(&wc) != 0, "Cannot register window class");
        RECT initial{0, 0, settings.width * 2, soul::kHeight * 2};
        AdjustWindowRect(&initial, WS_OVERLAPPEDWINDOW, FALSE);
        HWND window = CreateWindowW(wc.lpszClassName, frontend ? L"Soul of Darkness - Windows port" : movementPreview ? L"Soul of Darkness - First-level combat preview" : tilePreview ? L"Soul of Darkness - Tiles only, no gameplay - Arrows pan, F8 width" : (preview || fontPreview) ? L"Soul of Darkness - Asset preview" : L"Soul of Darkness - Port scaffold", WS_OVERLAPPEDWINDOW,
            CW_USEDEFAULT, CW_USEDEFAULT, initial.right-initial.left, initial.bottom-initial.top,
            nullptr, nullptr, instance, nullptr);
        require(window != nullptr, "Cannot create window");
        render();
        ShowWindow(window, SW_SHOW);
        setFullscreen(window, settings.fullscreen);
        timeBeginPeriod(1);
        using Time = std::chrono::steady_clock;
        auto last = Time::now(), nextFrame = last;
        while (running) {
            MSG message;
            while (PeekMessageW(&message, nullptr, 0, 0, PM_REMOVE)) {
                if (message.message == WM_QUIT) running = false;
                TranslateMessage(&message); DispatchMessageW(&message);
            }
            if (!running) break;
            auto now = Time::now();
            if (now < nextFrame) { Sleep(1); continue; }
            double dt = std::chrono::duration<double>(now-last).count();
            last = now;
            if (GetForegroundWindow() == window && !IsIconic(window)) clockState.advance(dt, update);
            render();
            InvalidateRect(window, nullptr, FALSE); UpdateWindow(window);
            nextFrame += std::chrono::duration_cast<Time::duration>(std::chrono::duration<double>(1.0/settings.fps));
            if (nextFrame < now) nextFrame = now;
        }
        timeEndPeriod(1);
        return 0;
    } catch (const std::exception& e) { std::cerr << e.what() << '\n'; return 1; }
}
