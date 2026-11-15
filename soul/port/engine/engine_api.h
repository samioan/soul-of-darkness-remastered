// Host-facing interface of the translated engine. Deliberately free of generated headers so the
// platform layer (and <windows.h>) never meets the game's identifiers.
#pragma once
#include <cstdint>
#include <string>
#include <vector>

namespace sodapi {

// MIDP key codes understood by the original Game.mapKeyCode.
enum Key : int {
    KeyUp = -1, KeyDown = -2, KeyLeft = -3, KeyRight = -4, KeyFire = -5, KeySoftLeft = -6, KeySoftRight = -7,
    Key0 = 48, Key1, Key2, Key3, Key4, Key5, Key6, Key7, Key8, Key9, KeyStar = 42, KeyPound = 35
};

struct Frame {
    const std::uint32_t* pixels = nullptr; // 0xAARRGGBB, top-down
    int width = 0, height = 0;
};

// Values the port patches read (tools/port_java/PortConfig.java). Vanilla builds ignore them.
struct Options {
    int screenWidth = 240; // logical width; height is always 320
    bool interpolation = false; // record draw calls so renderInterpolated can present between ticks
    bool optionsMenu = false;   // add the OPTIONS entry to the game's menus (set before start)
};

void configure(const Options& options);
void start(std::int64_t randomSeed, bool fixedSeed);
void tick();                   // one original loop iteration: Game.paint and input latch
double tickSeconds();          // length of the next iteration: 50 ms, or 66 ms while a cutscene script runs
Frame frame();
void keyPressed(int midpKey);
void keyReleased(int midpKey);
void hideNotify();
void showNotify();
bool finished();               // the game reached its exit state
void shutdown();               // MIDlet.destroyApp: flush the save record

// Introspection used by the host UI, tests and the equivalence checker.
int gameState();
int levelIndex();
bool inGameplay();
// 0 = menus/prompts, 1 = free gameplay, 2 = gameplay with a dialogue, cutscene or reward panel open,
// and the upgrade screen (both hold FIRE to confirm).
int inputContext();
std::uint64_t stateHash();
std::string describeState();
void dumpState(std::vector<std::string>& lines);
void debugUnlockLevels(int maxUnlockedLevel); // test hook: Game.maxUnlockedLevel

// Render-only interpolation support (port builds).
bool renderInterpolated(double alpha);

} // namespace sodapi
