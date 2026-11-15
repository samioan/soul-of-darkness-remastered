// Headless driver for the translated engine: scripted input, per-tick state fingerprints and
// frame captures. tools/check_engine.py compares its trace with the original class files
// (tools/reference_checks/EngineOracle.java).
#include "engine_api.h"
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <fstream>
#include <map>
#include <sstream>
#include <string>
#include <vector>

#ifdef _WIN32
#include <windows.h>
#include <dbghelp.h>
#pragma comment(lib, "dbghelp.lib")
namespace sod { extern void (*g_throwHook)(const char* kind); }
// --trace-throws: print where each Java exception is raised (needs a build with debug information).
static int g_traceBudget = 0;
static void traceThrow(const char* kind) {
    if (g_traceBudget <= 0) return;
    --g_traceBudget;
    static bool ready = false;
    HANDLE process = GetCurrentProcess();
    if (!ready) { SymSetOptions(SYMOPT_UNDNAME | SYMOPT_LOAD_LINES); SymInitialize(process, nullptr, TRUE); ready = true; }
    void* frames[24];
    USHORT n = CaptureStackBackTrace(2, 24, frames, nullptr);
    std::fprintf(stderr, "[throw] %s\n", kind);
    for (USHORT i = 0; i < n && i < 10; ++i) {
        char buffer[sizeof(SYMBOL_INFO) + 256] = {};
        SYMBOL_INFO* symbol = reinterpret_cast<SYMBOL_INFO*>(buffer);
        symbol->SizeOfStruct = sizeof(SYMBOL_INFO);
        symbol->MaxNameLen = 255;
        IMAGEHLP_LINE64 line = {};
        line.SizeOfStruct = sizeof(line);
        DWORD displacement = 0;
        const bool named = SymFromAddr(process, reinterpret_cast<DWORD64>(frames[i]), nullptr, symbol) != 0;
        const bool located = SymGetLineFromAddr64(process, reinterpret_cast<DWORD64>(frames[i]), &displacement, &line) != 0;
        std::fprintf(stderr, "    %s", named ? symbol->Name : "?");
        if (located) {
            const char* file = std::strrchr(line.FileName, '\\');
            std::fprintf(stderr, " (%s:%lu)", file ? file + 1 : line.FileName, line.LineNumber);
        }
        std::fprintf(stderr, "\n");
    }
}
#endif

namespace {
std::string g_data = "soul/extracted";
std::map<std::string, std::vector<std::uint8_t>> g_saves;
std::string g_saveIn, g_saveOut;
bool g_audio = false;
bool g_quiet = false;

bool readFile(const std::string& path, std::vector<std::uint8_t>& out) {
    std::ifstream in(path, std::ios::binary);
    if (!in) return false;
    out.assign(std::istreambuf_iterator<char>(in), std::istreambuf_iterator<char>());
    return true;
}
}

namespace sod {
bool hostReadResource(const std::string& name, std::vector<std::uint8_t>& out) { return readFile(g_data + "/" + name, out); }
void hostLog(const std::string& line) { if (!g_quiet) std::fprintf(stderr, "[game] %s\n", line.c_str()); }
bool hostLoadSave(const std::string& name, std::vector<std::uint8_t>& out) {
    auto it = g_saves.find(name);
    if (it == g_saves.end()) return false;
    out = it->second;
    return true;
}
bool hostStoreSave(const std::string& name, const std::vector<std::uint8_t>& data) {
    g_saves[name] = data;
    return true;
}
// The oracle's Player stays "started" until stopped; mirror that so both sides take the same branches.
void hostAudioStart(const std::vector<std::uint8_t>&, bool, int) { g_audio = true; }
void hostAudioStop() { g_audio = false; }
bool hostAudioPlaying() { return g_audio; }
// No host settings in the headless driver; the options menu shows empty pages.
int hostSettingCount(int) { return 0; }
std::string hostSettingName(int, int) { return std::string(); }
std::string hostSettingValue(int, int) { return std::string(); }
void hostSettingChange(int, int, int) {}
bool hostSettingCapturing() { return false; }
void hostSettingsClosed() {}
}

static void writePpm(const std::string& path) {
    sodapi::Frame f = sodapi::frame();
    std::ofstream out(path, std::ios::binary);
    out << "P6\n" << f.width << " " << f.height << "\n255\n";
    for (int i = 0; i < f.width * f.height; ++i) {
        std::uint32_t p = f.pixels[i];
        char rgb[3] = {static_cast<char>(p >> 16), static_cast<char>(p >> 8), static_cast<char>(p)};
        out.write(rgb, 3);
    }
}

int main(int argc, char** argv) {
    int ticks = 100, dumpTick = -1, width = 240;
    long long seed = 12345;
    std::string script, trace, dumpPath;
    std::multimap<int, std::string> shots;
    bool describe = false, english = false, interpolation = false, verifyReplay = false;
    std::multimap<int, std::string> midShots;
    long long replayChecked = 0, replayMismatch = 0, replayFrames = 0;
    for (int i = 1; i < argc; ++i) {
        std::string a = argv[i];
        auto next = [&]() -> std::string { if (i + 1 >= argc) { std::fprintf(stderr, "missing value for %s\n", a.c_str()); std::exit(2); } return argv[++i]; };
        if (a == "--data") g_data = next();
        else if (a == "--ticks") ticks = std::atoi(next().c_str());
        else if (a == "--seed") seed = std::atoll(next().c_str());
        else if (a == "--script") script = next();
        else if (a == "--trace") trace = next();
        else if (a == "--width") width = std::atoi(next().c_str());
        else if (a == "--dump") { dumpTick = std::atoi(next().c_str()); dumpPath = next(); }
        else if (a == "--shot") { int t = std::atoi(next().c_str()); shots.emplace(t, next()); }
        else if (a == "--save-in") g_saveIn = next();
        else if (a == "--save-out") g_saveOut = next();
        else if (a == "--describe") describe = true;
        else if (a == "--english") english = true;
#ifdef _WIN32
        else if (a == "--trace-throws") { g_traceBudget = std::atoi(next().c_str()); sod::g_throwHook = traceThrow; }
#endif
        else if (a == "--interpolation") interpolation = true;
        else if (a == "--verify-replay") { interpolation = true; verifyReplay = true; }
        else if (a == "--mid-shot") { int t = std::atoi(next().c_str()); midShots.emplace(t, next()); interpolation = true; }
        else if (a == "--quiet") g_quiet = true;
        else { std::fprintf(stderr, "unknown option %s\n", a.c_str()); return 2; }
    }
    if (!g_saveIn.empty()) {
        std::vector<std::uint8_t> bytes;
        if (!readFile(g_saveIn, bytes)) { std::fprintf(stderr, "cannot read %s\n", g_saveIn.c_str()); return 2; }
        g_saves["dvseed.sav"] = bytes;
    }
    // script lines: "<tick> <+|-> <midpKeyCode>" or "<tick> ! <maxUnlockedLevel>"
    std::multimap<int, std::pair<bool, int>> events;
    std::multimap<int, int> unlocks;
    if (!script.empty()) {
        std::ifstream in(script);
        if (!in) { std::fprintf(stderr, "cannot read %s\n", script.c_str()); return 2; }
        std::string line;
        while (std::getline(in, line)) {
            std::istringstream ls(line);
            int t, key;
            std::string op;
            if (ls >> t >> op >> key) {
                if (op == "!") unlocks.emplace(t, key);
                else events.emplace(t, std::make_pair(op == "+", key));
            }
        }
    }
    sodapi::Options options;
    options.screenWidth = width;
    options.english = english;
    options.interpolation = interpolation;
    sodapi::configure(options);
    sodapi::start(seed, true);
    std::ofstream traceOut;
    if (!trace.empty()) traceOut.open(trace, std::ios::binary);
    for (int t = 0; t < ticks && !sodapi::finished(); ++t) {
        auto unlockRange = unlocks.equal_range(t);
        for (auto it = unlockRange.first; it != unlockRange.second; ++it) sodapi::debugUnlockLevels(it->second);
        auto range = events.equal_range(t);
        for (auto it = range.first; it != range.second; ++it) {
            if (it->second.first) sodapi::keyPressed(it->second.second);
            else sodapi::keyReleased(it->second.second);
        }
        const bool exiting = sodapi::gameState() == 11; // Game.run() leaves its loop and calls destroyApp
        sodapi::tick();
        if (exiting) break;
        if (traceOut.is_open()) traceOut << t << ' ' << sodapi::stateHash() << ' ' << sodapi::gameState() << '\n';
        if (t == dumpTick) {
            std::vector<std::string> lines;
            sodapi::dumpState(lines);
            std::ofstream out(dumpPath, std::ios::binary);
            for (const auto& l : lines) out << l << '\n';
        }
        if (verifyReplay) {
            // Replaying a tick's recorded draw calls at alpha 1 must reproduce the tick's own frame exactly,
            // and every in-between frame must replay without error.
            sodapi::Frame tickFrame = sodapi::frame();
            std::vector<std::uint32_t> expected(tickFrame.pixels, tickFrame.pixels + tickFrame.width * tickFrame.height);
            for (double alpha : {0.25, 0.5, 0.75}) if (sodapi::renderInterpolated(alpha)) ++replayFrames;
            if (sodapi::renderInterpolated(1.0)) {
                sodapi::Frame f = sodapi::frame();
                ++replayChecked;
                if (std::memcmp(expected.data(), f.pixels, expected.size() * sizeof(std::uint32_t)) != 0) {
                    if (++replayMismatch <= 3) { std::fprintf(stderr, "replay mismatch at tick %d\n", t); writePpm("replay-mismatch-" + std::to_string(t) + ".ppm"); }
                }
            }
        }
        auto midRange = midShots.equal_range(t);
        for (auto it = midRange.first; it != midRange.second; ++it)
            if (sodapi::renderInterpolated(0.5)) writePpm(it->second);
        auto shotRange = shots.equal_range(t);
        for (auto it = shotRange.first; it != shotRange.second; ++it) writePpm(it->second);
        if (describe && (t % 50 == 0 || t == ticks - 1)) std::printf("%d %s\n", t, sodapi::describeState().c_str());
    }
    if (verifyReplay) {
        std::printf("replay: %lld world ticks reproduced exactly at alpha 1, %lld mismatches, %lld in-between frames\n",
                    replayChecked - replayMismatch, replayMismatch, replayFrames);
        if (replayMismatch || replayChecked == 0) return 1;
    }
    if (!g_saveOut.empty() && g_saves.count("dvseed.sav")) {
        std::ofstream out(g_saveOut, std::ios::binary);
        const auto& bytes = g_saves["dvseed.sav"];
        out.write(reinterpret_cast<const char*>(bytes.data()), static_cast<std::streamsize>(bytes.size()));
    }
    return 0;
}
