#include "save_game.h"
#include "game_frontend.h"
#include <fstream>
#include <iostream>
#include <stdexcept>
#include <chrono>
void check(bool b,const char* m){if(!b)throw std::runtime_error(m);}
int main(){const auto dir=std::filesystem::temp_directory_path()/("sod-save-test-"+std::to_string(std::chrono::steady_clock::now().time_since_epoch().count()));
try{
    const auto file=dir/"checkpoint.sav";check(!soul::loadGameSave(file),"Missing save");
    soul::CheckpointSnapshot s;s.uid=11;s.x=148;s.y=259;s.facingLeft=true;s.activeWeapon=1;s.meters={23,80,17,90,12,2,1,true,true};s.removed={{11,9},{47,512},{19,1},{267,8}};
    soul::writeGameSave(file,s);const auto r=soul::loadGameSave(file).value();
    check(r.uid==11 && r.x==148 && r.y==259 && r.facingLeft && r.activeWeapon==1,"Position/equipment roundtrip");
    check(r.meters.health==23 && r.meters.maxHealth==80 && r.meters.magic==17 && r.meters.maxMagic==90 && r.meters.collected==12 && r.meters.healthFragments==2 && r.meters.magicFragments==1 && r.meters.magicUnlocked && r.meters.iceUnlocked,"Progress roundtrip");
    check(r.removed.size()==4 && r.removed[1].uid==47 && r.removed[1].flags==512,"Removal roundtrip");
    const auto continued=r.forContinue();soul::ActorRecord cp{},chest{},enemy{};cp.parameters[0]=23;cp.parameters[1]=11;chest.parameters[0]=52;chest.parameters[1]=47;enemy.parameters[0]=1;enemy.parameters[1]=19;
    check(r.skips(cp) && !continued.skips(cp) && continued.skips(chest) && !continued.skips(enemy) && continued.meters.health==23,"Continue recreates persistence-only removals and retains saved HP");
    s.uid=128;s.meters.magic=42;soul::writeGameSave(file,s);check(soul::loadGameSave(file)->uid==128 && soul::loadGameSave(file)->meters.magic==42,"Atomic replacement");
    auto bad=s;bad.meters.health=81;bool rejected=false;try{soul::writeGameSave(file,bad);}catch(const std::exception&){rejected=true;}check(rejected && soul::loadGameSave(file)->uid==128,"Invalid write preserves prior save");
    bad=s;bad.removed.push_back(bad.removed[0]);rejected=false;try{soul::writeGameSave(file,bad);}catch(const std::exception&){rejected=true;}check(rejected,"Duplicate UID rejected");
    std::ifstream in(file,std::ios::binary);const std::string bytes((std::istreambuf_iterator<char>(in)),{});in.close();
    // Every truncation and single-byte corruption must fail, rather than partially restore.
    for(std::size_t i=0;i<bytes.size();++i){
        {std::ofstream out(file,std::ios::binary|std::ios::trunc);out.write(bytes.data(),static_cast<std::streamsize>(i));}
        rejected=false;try{soul::loadGameSave(file);}catch(const std::exception&){rejected=true;}check(rejected,"Truncation accepted");
        auto changed=bytes;changed[i]^=64;{std::ofstream out(file,std::ios::binary|std::ios::trunc);out<<changed;}
        rejected=false;try{soul::loadGameSave(file);}catch(const std::exception&){rejected=true;}check(rejected,"Corruption accepted");
    }
    const auto invalidPayload=[&](const std::string& payload){
        std::uint32_t hash=2166136261u;for(unsigned char c:payload){hash^=c;hash*=16777619u;}
        {std::ofstream out(file,std::ios::binary|std::ios::trunc);out<<"SODPC 1 "<<hash<<'\n'<<payload;}
        bool failed=false;try{soul::loadGameSave(file);}catch(const std::exception&){failed=true;}check(failed,"Invalid checksummed payload accepted");
    };
    invalidPayload("1 11 148 259 1 1 23 80 17 90 12 2 1 1 1 0\n"); // unported level
    invalidPayload("0 11 148 259 1 2 23 80 17 90 12 2 1 1 1 0\n"); // invalid boolean
    invalidPayload("0 11 148 259 1 1 81 80 17 90 12 2 1 1 1 0\n"); // HP exceeds max
    invalidPayload("0 11 148 259 1 1 23 80 17 90 12 2 1 1 0 0\n"); // equipped unowned ice
    invalidPayload("0 11 148 259 1 1 23 80 17 90 12 2 1 1 1 251\n"); // excessive count
    invalidPayload("0 11 148 259 1 1 23 80 17 90 12 2 1 1 1 2\n11 9\n11 512\n"); // duplicate UID
    invalidPayload("0 11 148 259 1 1 23 80 17 90 12 2 1 1 1 0\nextra");
    // A parent that is a file cannot be used as a save directory.
    rejected=false;try{soul::writeGameSave(file/"child.sav",s);}catch(const std::exception&){rejected=true;}check(rejected,"Storage failure must propagate");
    soul::writeGameSave(file,s);soul::clearGameSave(file);check(!soul::loadGameSave(file),"New Game clears save");soul::clearGameSave(file);
    soul::FrontendState ui;ui.hasSave=true;ui.enter(2);ui.press(40);ui.press(13);check(ui.state==9 && ui.continueRequested && !ui.restartRequested,"Continue loading boundary");
    ui.continueRequested=false;ui.enter(2);ui.selection=0;ui.press(13);check(ui.state==22 && !ui.newGameRequested,"New Game confirmation");ui.press(27);check(ui.state==2 && ui.hasSave && !ui.newGameRequested,"Cancel preserves save");
    ui.press(13);ui.press(13);check(ui.state==20 && ui.newGameRequested && !ui.continueRequested,"Confirmed reset before prologue");
    std::filesystem::remove_all(dir);std::cout<<"PASS: desktop save roundtrip, replacement, corruption, truncation, reset and Continue decisions\n";return 0;
}catch(const std::exception& e){std::filesystem::remove_all(dir);std::cerr<<e.what()<<'\n';return 1;}}
