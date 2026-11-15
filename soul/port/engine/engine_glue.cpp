// Connects the generated engine (sod_gen.h) to the host-facing API. This is the only hand-written
// file that sees the game's own identifiers.
#include "engine_api.h"
#include "sod_gen.h"

// Game.run() has a 50 ms tick (66 ms while a cutscene script runs) fed to the game as the frame time,
// and a wall-clock watchdog in paint() that is neutralised so the simulation depends only on ticks.

namespace sod {
namespace {
sodapi::Options g_options;
Ref<Image> g_screen;
Ref<Graphics> g_graphics;
Ref<Image> g_interp;
bool g_showInterp = false;
bool g_quit = false;
bool g_started = false;
}

int hostScreenWidth() { return g_options.screenWidth; }
void hostQuit() { g_quit = true; }

void GloftSOFD::destroyApp(jboolean) {
    if (game != nullptr && Game::saveLoaded) Game::accessSaveRecord(true);
    notifyDestroyed();
}

} // namespace sod

namespace sodapi {
using namespace sod;

void configure(const Options& options) {
#ifdef SOD_PORT_BUILD
    if (g_started && options.screenWidth != g_options.screenWidth) PortConfig::resetView();
    PortConfig::screenWidth = options.screenWidth;
    PortConfig::directTiles = options.interpolation;
#endif
    if (!options.interpolation) recorderDrop();
    const bool menu = g_started ? g_options.optionsMenu : options.optionsMenu;
    g_options = options;
    g_options.optionsMenu = menu;
}

static bool worldState(int state) { return state == 8 || state == 12 || state == 14 || state == 15 || state == 32; }

static void ensureScreen() {
    if (g_screen == nullptr || g_screen->width != g_options.screenWidth) {
        g_screen = Image::createImage(g_options.screenWidth, 320);
        std::fill(g_screen->pixels.begin(), g_screen->pixels.end(), 0xff000000u);
        g_graphics = g_screen->getGraphics();
    }
}

void start(std::int64_t randomSeed, bool fixedSeed) {
    if (g_started) return;
    g_started = true;
#ifdef SOD_PORT_BUILD
    PortConfig::clinit();
    PortConfig::screenWidth = g_options.screenWidth;
    PortConfig::directTiles = g_options.interpolation;
#endif
    // Same order the JVM initializes them: Game on construction, the others on first use
    // (none of the static initializers read another class).
    ErrorReporter::clinit();
    Game::clinit();
    Actor::clinit();
    BSprite::clinit();
#ifdef SOD_PORT_BUILD
    PortMenu::clinit();
    if (g_options.optionsMenu) {
        Game::portMenus();
        PortConfig::desktopText = true;
    }
#endif
    GloftSOFD::instance = jnew<GloftSOFD>();
    GloftSOFD::game = jnew<Game>();
    if (fixedSeed) Game::random = jnew<Random>(static_cast<jlong>(randomSeed));
    ensureScreen();
}

void tick() {
    if (!g_started || g_quit) return;
    ensureScreen();
    g_showInterp = false;
    bool recording = false;
    try {
        if (Game::state == 11) {
            GloftSOFD::instance->destroyApp(true);
            return;
        }
        if (!Game::suspended) {
            // Game.run(): resume interrupted music a few frames after returning to the foreground.
            if (((Game::state != 14 && Game::state != 8 && Game::state != 9) || (Game::state == 8 && Game::modalActive))
                && (Game::state != 5 || Game::previousState != 14) && Game::soundResumeFrames > 0) {
                while (--Game::soundResumeFrames == 0) Game::playSound(Game::resumeSoundId, -1);
            }
            g_graphics->tx = g_graphics->ty = 0;
            g_graphics->setClip(0, 0, g_screen->width, g_screen->height);
            g_graphics->setColor(0);
            Game::paintComplete = false;
            Game::frameDeltaMs = Game::hasActiveBackgroundScript() ? 66 : 50; // Game.run(): time since the last frame
            Game::lastPaintMs = 0;                                            // no 2.5 s stall detection
            // Only the states that draw the world have motion worth interpolating.
            recording = g_options.interpolation && worldState(Game::state);
            if (recording) recorderStart(g_graphics.get());
            GloftSOFD::game->paint(g_graphics);
        }
    } catch (JavaThrow& e) {
        hostLog("uncaught: " + jutf8(e.ex->toString()));
    }
    Game::lastPaintMs = 0;
    if (recording && worldState(Game::state)) recorderFinish();
    else recorderDrop();
}

bool renderInterpolated(double alpha) {
    if (!g_started || !g_options.interpolation || !recorderHasFrame() || g_screen == nullptr) return false;
    if (g_interp == nullptr || g_interp->width != g_screen->width) g_interp = Image::createImage(g_screen->width, g_screen->height);
    Ref<Graphics> g = g_interp->getGraphics();
    if (!recorderReplay(alpha, *g.get())) return false;
    g_showInterp = true;
    return true;
}

Frame frame() {
    Frame f;
    const Ref<Image>& image = (g_showInterp && g_interp != nullptr) ? g_interp : g_screen;
    if (image != nullptr) {
        f.pixels = image->pixels.data();
        f.width = image->width;
        f.height = image->height;
    }
    return f;
}

void keyPressed(int key) { if (g_started) GloftSOFD::game->keyPressed(key); }
void keyReleased(int key) { if (g_started) GloftSOFD::game->keyReleased(key); }
void hideNotify() { if (g_started) { try { GloftSOFD::game->hideNotify(); } catch (JavaThrow&) {} } }
void showNotify() { if (g_started) { try { GloftSOFD::game->showNotify(); } catch (JavaThrow&) {} } }
bool finished() { return g_quit; }

void shutdown() {
    if (!g_started || g_quit) return;
    try {
        GloftSOFD::instance->destroyApp(true);
    } catch (JavaThrow&) {
    }
}

double tickSeconds() {
    return (g_started && Game::hasActiveBackgroundScript()) ? 0.066 : 0.050;
}

int gameState() { return g_started ? Game::state : -1; }
int levelIndex() { return g_started ? Game::levelIndex : -1; }
bool inGameplay() { return g_started && Game::state == 8; }
int inputContext() {
    if (!g_started || Game::state != 8) return 0;
    if (Game::hasActiveBackgroundScript() || Game::dialogText != nullptr || Game::modalActive) return 2;
    return 1;
}

static void capture(StateSink& sink) {
    sink.prefix = "e.";
    Game::portDumpStatics(sink);
    sink.prefix = "b.";
    Actor::portDumpStatics(sink);
    sink.prefix = "d.";
    BSprite::portDumpStatics(sink);
    if (Game::actors != nullptr) {
        for (jint i = 0; i < Game::actors.length(); ++i) {
            if (Game::actors[i] == nullptr) continue;
            sink.prefix = "actor[" + std::to_string(i) + "].";
            Game::actors[i]->portDumpFields(sink);
        }
    }
}

std::uint64_t stateHash() {
    StateSink sink;
    capture(sink);
    return sink.hash;
}

void dumpState(std::vector<std::string>& lines) {
    StateSink sink;
    sink.lines = &lines;
    capture(sink);
}

void debugUnlockLevels(int maxUnlockedLevel) { if (g_started) Game::maxUnlockedLevel = maxUnlockedLevel; }

std::string describeState() {
    if (!g_started) return "not started";
    std::string s = "state=" + std::to_string(Game::state) + " frame=" + std::to_string(Game::stateFrame)
        + " level=" + std::to_string(Game::levelIndex) + " actors=" + std::to_string(Game::actorCount)
        + " camera=" + std::to_string(Game::cameraX) + "," + std::to_string(Game::cameraY);
    if (Game::player != nullptr) s += " player=" + std::to_string(Game::player->positionX) + "," + std::to_string(Game::player->positionY)
        + " anim=" + std::to_string(Game::player->animationId);
    s += " hp=" + std::to_string(Game::health) + "/" + std::to_string(Game::maxHealth);
    return s;
}



} // namespace sodapi
