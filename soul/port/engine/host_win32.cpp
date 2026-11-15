// Windows host for the translated Soul of Darkness engine: window, scaling, timing, keyboard and
// XInput gamepad input, MIDI music, save file, settings and JAR loading. The game itself lives in
// the generated sources behind engine_api.h; nothing here knows the game's rules.
#include "engine_api.h"
#include "host_util.h"

#include <windows.h>
#include <commdlg.h>
#include <mmsystem.h>
#include <shellapi.h>
#include <shlobj.h>

#include <algorithm>
#include <cmath>
#include <cstdio>
#include <cstdlib>
#include <cwchar>
#include <fstream>
#include <map>
#include <sstream>
#include <string>
#include <vector>

namespace {

// ---------------------------------------------------------------- paths and text

std::wstring widen(const std::string& s) {
    if (s.empty()) return L"";
    int n = MultiByteToWideChar(CP_UTF8, 0, s.data(), static_cast<int>(s.size()), nullptr, 0);
    std::wstring w(static_cast<std::size_t>(n), L'\0');
    MultiByteToWideChar(CP_UTF8, 0, s.data(), static_cast<int>(s.size()), &w[0], n);
    return w;
}
std::string narrow(const std::wstring& w) {
    if (w.empty()) return "";
    int n = WideCharToMultiByte(CP_UTF8, 0, w.data(), static_cast<int>(w.size()), nullptr, 0, nullptr, nullptr);
    std::string s(static_cast<std::size_t>(n), '\0');
    WideCharToMultiByte(CP_UTF8, 0, w.data(), static_cast<int>(w.size()), &s[0], n, nullptr, nullptr);
    return s;
}
bool readFile(const std::wstring& path, std::vector<std::uint8_t>& out) {
    FILE* f = _wfopen(path.c_str(), L"rb");
    if (!f) return false;
    std::fseek(f, 0, SEEK_END);
    long size = std::ftell(f);
    std::fseek(f, 0, SEEK_SET);
    out.resize(size > 0 ? static_cast<std::size_t>(size) : 0);
    std::size_t got = out.empty() ? 0 : std::fread(out.data(), 1, out.size(), f);
    std::fclose(f);
    return got == out.size();
}
bool writeFile(const std::wstring& path, const void* data, std::size_t size) {
    std::wstring tmp = path + L".tmp";
    FILE* f = _wfopen(tmp.c_str(), L"wb");
    if (!f) return false;
    std::size_t put = size ? std::fwrite(data, 1, size, f) : 0;
    bool ok = put == size && std::fflush(f) == 0;
    std::fclose(f);
    if (!ok) { DeleteFileW(tmp.c_str()); return false; }
    return MoveFileExW(tmp.c_str(), path.c_str(), MOVEFILE_REPLACE_EXISTING) != 0;
}
bool exists(const std::wstring& path) { return GetFileAttributesW(path.c_str()) != INVALID_FILE_ATTRIBUTES; }
bool isDirectory(const std::wstring& path) {
    DWORD a = GetFileAttributesW(path.c_str());
    return a != INVALID_FILE_ATTRIBUTES && (a & FILE_ATTRIBUTE_DIRECTORY);
}
std::wstring exeDir() {
    wchar_t buf[MAX_PATH * 4];
    DWORD n = GetModuleFileNameW(nullptr, buf, static_cast<DWORD>(std::size(buf)));
    std::wstring p(buf, n);
    std::size_t slash = p.find_last_of(L"\\/");
    return slash == std::wstring::npos ? L"." : p.substr(0, slash);
}
std::wstring userDir() {
    wchar_t* base = nullptr;
    std::wstring dir;
    if (SUCCEEDED(SHGetKnownFolderPath(FOLDERID_LocalAppData, 0, nullptr, &base))) {
        dir = std::wstring(base) + L"\\" + sodhost::kUserFolder;
        CoTaskMemFree(base);
    } else {
        dir = exeDir();
    }
    CreateDirectoryW(dir.c_str(), nullptr);
    return dir;
}

// ---------------------------------------------------------------- settings

enum Action { ActUp, ActDown, ActLeft, ActRight, ActJump, ActAttack, ActMagic, ActSwitch, ActConfirm, ActPause, ActBack, ActSoftLeft, ActCount };
const char* const kActionNames[ActCount] = {"up", "down", "left", "right", "jump", "attack", "magic", "switch_weapon", "confirm", "pause", "back", "menu"};

struct KeyName { const char* name; int vk; };
const KeyName kKeyNames[] = {
    {"LEFT", VK_LEFT}, {"RIGHT", VK_RIGHT}, {"UP", VK_UP}, {"DOWN", VK_DOWN}, {"SPACE", VK_SPACE}, {"ENTER", VK_RETURN},
    {"ESC", VK_ESCAPE}, {"TAB", VK_TAB}, {"BACKSPACE", VK_BACK}, {"SHIFT", VK_SHIFT}, {"CTRL", VK_CONTROL}, {"ALT", VK_MENU},
    {"LSHIFT", VK_LSHIFT}, {"RSHIFT", VK_RSHIFT}, {"LCTRL", VK_LCONTROL}, {"RCTRL", VK_RCONTROL},
    {"INSERT", VK_INSERT}, {"DELETE", VK_DELETE}, {"HOME", VK_HOME}, {"END", VK_END}, {"PAGEUP", VK_PRIOR}, {"PAGEDOWN", VK_NEXT},
    {"COMMA", VK_OEM_COMMA}, {"PERIOD", VK_OEM_PERIOD}, {"SLASH", VK_OEM_2}, {"SEMICOLON", VK_OEM_1}, {"QUOTE", VK_OEM_7},
    {"LBRACKET", VK_OEM_4}, {"RBRACKET", VK_OEM_6}, {"MINUS", VK_OEM_MINUS}, {"EQUALS", VK_OEM_PLUS},
};
enum PadButton : unsigned {
    PadA = 0x1000, PadB = 0x2000, PadX = 0x4000, PadY = 0x8000, PadLB = 0x0100, PadRB = 0x0200, PadStart = 0x0010, PadBack = 0x0020,
    PadLS = 0x0040, PadRS = 0x0080, PadLT = 0x10000, PadRT = 0x20000
};
struct PadName { const char* name; unsigned mask; };
const PadName kPadNames[] = {{"A", PadA}, {"B", PadB}, {"X", PadX}, {"Y", PadY}, {"LB", PadLB}, {"RB", PadRB}, {"START", PadStart},
                             {"BACK", PadBack}, {"LS", PadLS}, {"RS", PadRS}, {"LT", PadLT}, {"RT", PadRT}};

// Frame-rate setting that means "one frame per original tick": Game.run() ticks every 50 ms.
constexpr int kNativeFps = 20;

struct Settings {
    int width = 0;            // logical width; 0 = follow the window's aspect ratio
    int fps = -1;             // -1 = display refresh rate, 0 = unlimited, kNativeFps = one frame per tick, otherwise the cap
    bool fullscreen = false;
    bool interpolation = true;
    bool integerScale = false;
    bool smooth = false;      // bilinear-like stretch instead of nearest neighbour
    int volume = 80;
    bool mute = false;
    bool showFps = false;
    bool pauseUnfocused = true; // like the phone: losing focus suspends the game and opens its pause menu
    int windowScale = 3;
    std::wstring jar;         // last imported JAR
    std::vector<int> keys[ActCount];
    unsigned pad[ActCount] = {};
    int deadzone = 40;        // percent of stick travel
};

void defaultBindings(Settings& s) {
    s.keys[ActUp] = {VK_UP, 'W'};
    s.keys[ActDown] = {VK_DOWN, 'S'};
    s.keys[ActLeft] = {VK_LEFT, 'A'};
    s.keys[ActRight] = {VK_RIGHT, 'D'};
    s.keys[ActJump] = {VK_SPACE};
    s.keys[ActAttack] = {'J', 'E', 'Z'};
    s.keys[ActMagic] = {'K', 'X'};
    s.keys[ActSwitch] = {'Q', 'L', 'C'};
    s.keys[ActConfirm] = {VK_RETURN};
    s.keys[ActPause] = {VK_ESCAPE};
    s.keys[ActBack] = {VK_BACK, 'U'};
    s.keys[ActSoftLeft] = {VK_TAB};
    s.pad[ActJump] = PadA;
    s.pad[ActAttack] = PadX | PadRT;
    s.pad[ActMagic] = PadB;
    s.pad[ActSwitch] = PadY | PadLB | PadRB;
    s.pad[ActPause] = PadStart;
    s.pad[ActBack] = PadBack;
}

std::string trim(const std::string& s) {
    std::size_t a = s.find_first_not_of(" \t\r\n"), b = s.find_last_not_of(" \t\r\n");
    return a == std::string::npos ? "" : s.substr(a, b - a + 1);
}
std::string upper(std::string s) {
    for (auto& c : s) c = static_cast<char>(std::toupper(static_cast<unsigned char>(c)));
    return s;
}
int parseKey(const std::string& raw) {
    std::string n = upper(trim(raw));
    if (n.size() == 1 && ((n[0] >= 'A' && n[0] <= 'Z') || (n[0] >= '0' && n[0] <= '9'))) return n[0];
    if (n.size() >= 2 && n[0] == 'F' && std::isdigit(static_cast<unsigned char>(n[1]))) {
        int f = std::atoi(n.c_str() + 1);
        if (f >= 1 && f <= 24) return VK_F1 + f - 1;
    }
    if (n.rfind("NUMPAD", 0) == 0 && n.size() == 7 && std::isdigit(static_cast<unsigned char>(n[6]))) return VK_NUMPAD0 + (n[6] - '0');
    for (const auto& k : kKeyNames) if (n == k.name) return k.vk;
    return 0;
}
std::string keyName(int vk) {
    if ((vk >= 'A' && vk <= 'Z') || (vk >= '0' && vk <= '9')) return std::string(1, static_cast<char>(vk));
    if (vk >= VK_F1 && vk <= VK_F24) return "F" + std::to_string(vk - VK_F1 + 1);
    if (vk >= VK_NUMPAD0 && vk <= VK_NUMPAD9) return "NUMPAD" + std::to_string(vk - VK_NUMPAD0);
    for (const auto& k : kKeyNames) if (vk == k.vk) return k.name;
    return "";
}
std::vector<std::string> split(const std::string& s, char sep) {
    std::vector<std::string> out;
    std::stringstream ss(s);
    std::string item;
    while (std::getline(ss, item, sep)) if (!trim(item).empty()) out.push_back(trim(item));
    return out;
}

bool loadSettings(const std::wstring& path, Settings& s) {
    std::vector<std::uint8_t> bytes;
    if (!readFile(path, bytes)) return false;
    std::string text(bytes.begin(), bytes.end());
    std::stringstream in(text);
    std::string line, section;
    while (std::getline(in, line)) {
        line = trim(line);
        if (line.empty() || line[0] == ';' || line[0] == '#') continue;
        if (line[0] == '[') { section = upper(trim(line.substr(1, line.find(']') - 1))); continue; }
        std::size_t eq = line.find('=');
        if (eq == std::string::npos) continue;
        std::string key = trim(line.substr(0, eq)), value = trim(line.substr(eq + 1));
        std::string lv = upper(value);
        if (section == "KEYBOARD" || section == "GAMEPAD") {
            for (int a = 0; a < ActCount; ++a) {
                if (key != kActionNames[a]) continue;
                if (section == "KEYBOARD") {
                    s.keys[a].clear();
                    for (const auto& k : split(value, ',')) if (int vk = parseKey(k)) s.keys[a].push_back(vk);
                } else {
                    s.pad[a] = 0;
                    for (const auto& k : split(lv, ',')) for (const auto& p : kPadNames) if (k == p.name) s.pad[a] |= p.mask;
                }
            }
            if (section == "GAMEPAD" && key == "deadzone") s.deadzone = std::clamp(std::atoi(value.c_str()), 5, 95);
            continue;
        }
        if (key == "width") s.width = lv == "AUTO" ? 0 : std::clamp(std::atoi(value.c_str()), 240, 960);
        else if (key == "fps") s.fps = lv == "DISPLAY" ? -1 : lv == "UNLIMITED" ? 0 : lv == "NATIVE" ? kNativeFps : std::clamp(std::atoi(value.c_str()), 1, 1000);
        else if (key == "fullscreen") s.fullscreen = std::atoi(value.c_str()) != 0;
        else if (key == "interpolation") s.interpolation = std::atoi(value.c_str()) != 0;
        else if (key == "integer_scale") s.integerScale = std::atoi(value.c_str()) != 0;
        else if (key == "smooth") s.smooth = std::atoi(value.c_str()) != 0;
        else if (key == "volume") s.volume = std::clamp(std::atoi(value.c_str()), 0, 100);
        else if (key == "mute") s.mute = std::atoi(value.c_str()) != 0;
        else if (key == "show_fps") s.showFps = std::atoi(value.c_str()) != 0;
        else if (key == "pause_on_focus_loss") s.pauseUnfocused = std::atoi(value.c_str()) != 0;
        else if (key == "window_scale") s.windowScale = std::clamp(std::atoi(value.c_str()), 1, 8);
        else if (key == "jar") s.jar = widen(value);
    }
    return true;
}

bool saveSettings(const std::wstring& path, const Settings& s) {
    std::ostringstream out;
    out << "; Soul of Darkness desktop settings. Delete this file to restore defaults.\n[display]\n";
    out << "; width: logical width in original pixels (height is always 320). auto follows the window shape;\n";
    out << ";        240 = original portrait, 427 = 4:3, 569 = 16:9, 747 = 21:9.\n";
    out << "width=" << (s.width == 0 ? std::string("auto") : std::to_string(s.width)) << "\n";
    out << "; fps: display (monitor refresh rate), unlimited, native (one frame per original tick, 20 fps) or a number.\n";
    out << "fps=" << (s.fps < 0 ? std::string("display") : s.fps == 0 ? std::string("unlimited") : s.fps == kNativeFps ? std::string("native") : std::to_string(s.fps)) << "\n";
    out << "; interpolation: draw in-between frames; the simulation always runs at the original 50 ms tick (66 ms in cutscenes).\n";
    out << "interpolation=" << (s.interpolation ? 1 : 0) << "\nfullscreen=" << (s.fullscreen ? 1 : 0) << "\ninteger_scale=" << (s.integerScale ? 1 : 0)
        << "\nsmooth=" << (s.smooth ? 1 : 0) << "\nwindow_scale=" << s.windowScale << "\nshow_fps=" << (s.showFps ? 1 : 0) << "\n";
    out << "\n[audio]\nvolume=" << s.volume << "\nmute=" << (s.mute ? 1 : 0) << "\n";
    out << "\n[game]\npause_on_focus_loss=" << (s.pauseUnfocused ? 1 : 0) << "\n";
    if (!s.jar.empty()) out << "jar=" << narrow(s.jar) << "\n";
    out << "\n[keyboard]\n; Comma separated keys: letters, digits, LEFT RIGHT UP DOWN SPACE ENTER ESC TAB BACKSPACE SHIFT CTRL ALT F1..F12 NUMPAD0..9\n";
    out << "; Digits 0-9 and numpad always act as the phone keypad too (numpad * and / are the * and # keys).\n";
    for (int a = 0; a < ActCount; ++a) {
        out << kActionNames[a] << "=";
        for (std::size_t i = 0; i < s.keys[a].size(); ++i) out << (i ? "," : "") << keyName(s.keys[a][i]);
        out << "\n";
    }
    out << "\n[gamepad]\n; XInput buttons: A B X Y LB RB LT RT START BACK LS RS. The d-pad and left stick always steer.\n";
    for (int a = ActJump; a < ActCount; ++a) {
        out << kActionNames[a] << "=";
        bool first = true;
        for (const auto& p : kPadNames) if (s.pad[a] & p.mask) { out << (first ? "" : ",") << p.name; first = false; }
        out << "\n";
    }
    out << "deadzone=" << s.deadzone << "\n";
    std::string text = out.str();
    return writeFile(path, text.data(), text.size());
}

// ---------------------------------------------------------------- global host state

Settings g_settings;
int g_logicalWidth = 240;
std::wstring g_settingsPath;
std::wstring g_saveDir;
std::wstring g_dataDir;                                   // directory with extracted files, or empty
std::map<std::string, std::vector<std::uint8_t>> g_jar;   // or the JAR's entries in memory
HWND g_window = nullptr;
bool g_quit = false;
bool g_active = true;
std::string g_overlay;
double g_overlayUntil = 0;
std::vector<std::string> g_log;
std::multimap<long long, std::pair<bool, int>> g_autoplay; // tick -> (press, phone key): scripted input for automated runs
long long g_tickCount = 0;

double now() {
    static LARGE_INTEGER freq = [] { LARGE_INTEGER f; QueryPerformanceFrequency(&f); return f; }();
    LARGE_INTEGER t;
    QueryPerformanceCounter(&t);
    return static_cast<double>(t.QuadPart) / static_cast<double>(freq.QuadPart);
}
void overlay(const std::string& text) {
    g_overlay = text;
    g_overlayUntil = now() + 2.5;
}

// ---------------------------------------------------------------- music (MCI)

struct Music {
    bool open = false, playing = false;
    int loopsLeft = 0;
    std::wstring file;
    int serial = 0;
    void command(const std::wstring& c, bool notify = false) {
        mciSendStringW(c.c_str(), nullptr, 0, notify ? g_window : nullptr);
    }
    void applyVolume() {
        if (!open) return;
        int v = g_settings.mute ? 0 : g_settings.volume * 10;
        command(L"setaudio sodmusic volume to " + std::to_wstring(v));
    }
    void stop() {
        if (open) {
            command(L"stop sodmusic");
            command(L"close sodmusic");
        }
        open = playing = false;
        if (!file.empty()) DeleteFileW(file.c_str());
        file.clear();
    }
    void start(const std::vector<std::uint8_t>& data, bool midi, int loops) {
        stop();
        if (data.empty()) return;
        wchar_t temp[MAX_PATH];
        GetTempPathW(MAX_PATH, temp);
        file = std::wstring(temp) + L"sod_music_" + std::to_wstring(GetCurrentProcessId()) + L"_" + std::to_wstring(++serial) + (midi ? L".mid" : L".wav");
        if (!writeFile(file, data.data(), data.size())) { file.clear(); return; }
        // The MPEGVideo (DirectShow) device plays MIDI and honours "setaudio volume"; fall back to the sequencer.
        std::wstring openCmd = L"open \"" + file + L"\" type mpegvideo alias sodmusic";
        const char* device = "mpegvideo";
        if (mciSendStringW(openCmd.c_str(), nullptr, 0, nullptr) != 0) {
            device = midi ? "sequencer" : "waveaudio";
            openCmd = L"open \"" + file + (midi ? L"\" type sequencer alias sodmusic" : L"\" type waveaudio alias sodmusic");
            if (mciSendStringW(openCmd.c_str(), nullptr, 0, nullptr) != 0) {
                g_log.push_back("music: no MCI device could open the track");
                DeleteFileW(file.c_str());
                file.clear();
                return;
            }
        }
        g_log.push_back(std::string("music: playing ") + std::to_string(data.size()) + " bytes via MCI " + device + (loops < 0 ? " (looping)" : ""));
        open = true;
        loopsLeft = loops;
        applyVolume();
        command(L"play sodmusic notify", true);
        playing = true;
    }
    void finished() { // MM_MCINOTIFY
        if (!open) return;
        if (loopsLeft < 0 || --loopsLeft > 0) {
            command(L"seek sodmusic to start");
            command(L"play sodmusic notify", true);
        } else {
            playing = false;
        }
    }
} g_music;

} // namespace

// ---------------------------------------------------------------- engine host hooks

namespace sod {
bool hostReadResource(const std::string& name, std::vector<std::uint8_t>& out) {
    if (!g_jar.empty()) {
        auto it = g_jar.find(name);
        if (it == g_jar.end()) return false;
        out = it->second;
        return true;
    }
    return readFile(g_dataDir + L"\\" + widen(name), out);
}
void hostLog(const std::string& line) {
    if (g_log.size() < 2000) g_log.push_back(line);
    OutputDebugStringA(("[sod] " + line + "\n").c_str());
}
bool hostLoadSave(const std::string& name, std::vector<std::uint8_t>& out) { return readFile(g_saveDir + L"\\" + widen(name), out); }
bool hostStoreSave(const std::string& name, const std::vector<std::uint8_t>& data) {
    return writeFile(g_saveDir + L"\\" + widen(name), data.data(), data.size());
}
void hostAudioStart(const std::vector<std::uint8_t>& data, bool midi, int loops) { g_music.start(data, midi, loops); }
void hostAudioStop() { g_music.stop(); }
bool hostAudioPlaying() { return g_music.playing; }
}

namespace {

// ---------------------------------------------------------------- in-game options screen

const int kWidthChoices[] = {0, 240, 427, 569, 747};
const int kFpsChoices[] = {-1, kNativeFps, 30, 60, 120, 144, 240, 0};
const char* const kActionLabels[ActCount] = {"UP", "DOWN", "LEFT", "RIGHT", "JUMP", "ATTACK", "MAGIC", "SWITCH WEAPON",
                                             "CONFIRM", "PAUSE", "UPGRADE / BACK", "MENU KEY"};
struct Capture { bool active = false; int page = 0; int action = 0; } g_capture;
void applyLogicalWidth();
void reconfigure();
void setFullscreen(bool on);
int displayRefresh();

int stepChoice(const int* choices, int count, int current, int direction) {
    int i = 0;
    for (int k = 0; k < count; ++k) if (choices[k] == current) i = k;
    return choices[(i + (direction < 0 ? count - 1 : 1)) % count];
}

std::string onOff(bool v) { return v ? "ON" : "OFF"; }

} // namespace

namespace sod {
int hostSettingCount(int page) {
    switch (page) {
        case 0: return 8;
        case 1: return 2;
        case 2: return ActCount + 1;
        case 3: return (ActCount - ActJump) + 2;
        default: return 0;
    }
}
std::string hostSettingName(int page, int index) {
    static const char* const display[] = {"WIDTH", "FRAME RATE", "SMOOTH MOTION", "FULLSCREEN", "WHOLE PIXELS", "SOFT SCALING", "FPS COUNTER", "PAUSE IN BACKGROUND"};
    if (page == 0 && index >= 0 && index < 8) return display[index];
    if (page == 1) return index == 0 ? "MUSIC VOLUME" : "MUTE";
    if (page == 2) return index < ActCount ? kActionLabels[index] : "RESET KEYS";
    if (page == 3) {
        const int actions = ActCount - ActJump;
        if (index < actions) return kActionLabels[ActJump + index];
        return index == actions ? "STICK DEADZONE" : "RESET BUTTONS";
    }
    return std::string();
}
std::string hostSettingValue(int page, int index) {
    if (g_capture.active && g_capture.page == page && ((page == 2 && g_capture.action == index) || (page == 3 && g_capture.action == ActJump + index)))
        return page == 2 ? "PRESS A KEY" : "PRESS A BUTTON";
    if (page == 0) {
        switch (index) {
            case 0: return g_settings.width == 0 ? "AUTO " + std::to_string(g_logicalWidth) : std::to_string(g_settings.width);
            case 1: return g_settings.fps < 0 ? "DISPLAY " + std::to_string(displayRefresh()) : g_settings.fps == 0 ? "UNLIMITED"
                         : g_settings.fps == kNativeFps ? "ORIGINAL" : std::to_string(g_settings.fps);
            case 2: return onOff(g_settings.interpolation);
            case 3: return onOff(g_settings.fullscreen);
            case 4: return onOff(g_settings.integerScale);
            case 5: return onOff(g_settings.smooth);
            case 6: return onOff(g_settings.showFps);
            case 7: return onOff(g_settings.pauseUnfocused);
        }
    }
    if (page == 1) return index == 0 ? std::to_string(g_settings.volume) : onOff(g_settings.mute);
    if (page == 2 && index < ActCount) {
        std::string s;
        for (int vk : g_settings.keys[index]) { if (!s.empty()) s += " "; s += keyName(vk); }
        return s.empty() ? "-" : s;
    }
    if (page == 3) {
        const int actions = ActCount - ActJump;
        if (index < actions) {
            std::string s;
            for (const auto& b : kPadNames) if (g_settings.pad[ActJump + index] & b.mask) { if (!s.empty()) s += " "; s += b.name; }
            return s.empty() ? "-" : s;
        }
        if (index == actions) return std::to_string(g_settings.deadzone);
    }
    return std::string();
}
void hostSettingChange(int page, int index, int direction) {
    const int step = direction < 0 ? -1 : 1;
    if (page == 0) {
        switch (index) {
            case 0: g_settings.width = stepChoice(kWidthChoices, 5, g_settings.width, direction); applyLogicalWidth(); break;
            case 1: g_settings.fps = stepChoice(kFpsChoices, 8, g_settings.fps, direction); break;
            case 2: g_settings.interpolation = !g_settings.interpolation; reconfigure(); break;
            case 3: setFullscreen(!g_settings.fullscreen); break;
            case 4: g_settings.integerScale = !g_settings.integerScale; break;
            case 5: g_settings.smooth = !g_settings.smooth; break;
            case 6: g_settings.showFps = !g_settings.showFps; break;
            case 7: g_settings.pauseUnfocused = !g_settings.pauseUnfocused; break;
        }
    } else if (page == 1) {
        if (index == 0) g_settings.volume = std::clamp(g_settings.volume + (direction == 0 ? 10 : step * 10), 0, 100);
        else g_settings.mute = !g_settings.mute;
        if (index == 0 && direction == 0 && g_settings.volume == 100) g_settings.volume = 100;
        g_music.applyVolume();
    } else if (page == 2) {
        if (index < ActCount) {
            if (direction == 0) { g_capture.active = true; g_capture.page = 2; g_capture.action = index; }
        } else if (direction == 0) {
            Settings defaults;
            defaultBindings(defaults);
            for (int a = 0; a < ActCount; ++a) g_settings.keys[a] = defaults.keys[a];
        }
    } else if (page == 3) {
        const int actions = ActCount - ActJump;
        if (index < actions) {
            if (direction == 0) { g_capture.active = true; g_capture.page = 3; g_capture.action = ActJump + index; }
        } else if (index == actions) {
            g_settings.deadzone = std::clamp(g_settings.deadzone + (direction == 0 ? 5 : step * 5), 10, 90);
        } else if (direction == 0) {
            Settings defaults;
            defaultBindings(defaults);
            for (int a = 0; a < ActCount; ++a) g_settings.pad[a] = defaults.pad[a];
        }
    }
}
bool hostSettingCapturing() { return g_capture.active; }
void hostSettingsClosed() {
    g_capture.active = false;
    saveSettings(g_settingsPath, g_settings);
}
}

namespace {

// ---------------------------------------------------------------- game data

bool dataDirValid(const std::wstring& dir) {
    for (const auto& f : sodhost::requiredDataFiles()) if (!exists(dir + L"\\" + widen(f))) return false;
    return true;
}

// Loads a JAR into memory. Returns an empty string on success, otherwise a message for the user.
std::string loadJar(const std::wstring& path, bool interactive) {
    std::vector<std::uint8_t> bytes;
    if (!readFile(path, bytes)) return "The file could not be read.";
    std::vector<sodhost::ZipEntry> entries;
    std::string error;
    if (!sodhost::readZip(bytes, entries, error)) return "This is not a readable JAR (" + error + ").";
    std::map<std::string, std::vector<std::uint8_t>> files;
    for (auto& e : entries) {
        if (e.name.size() > 6 && e.name.compare(e.name.size() - 6, 6, ".class") == 0) continue;
        files[e.name] = std::move(e.data);
    }
    for (const auto& f : sodhost::requiredDataFiles())
        if (!files.count(f)) return std::string("This JAR is not ") + sodhost::kExpectedJarName + ": it has no '" + f + "' resource.";
    const std::string hash = sodhost::sha256Hex(bytes);
    if (hash != sodhost::kExpectedJarSha256) {
        if (!interactive) return "";
        std::wstring msg = L"This JAR is not the verified build:\n" + widen(sodhost::kExpectedJarName) + L"\n\nSHA-256: " + widen(hash)
            + L"\n\nIt has the expected resource layout, so it may still work, but it has not been tested.\nUse it anyway?";
        if (MessageBoxW(nullptr, msg.c_str(), L"Soul of Darkness", MB_YESNO | MB_ICONWARNING) != IDYES) return "Import cancelled.";
    }
    g_jar.swap(files);
    g_dataDir.clear();
    return "";
}

bool pickJar(std::wstring& path) {
    wchar_t file[MAX_PATH * 2] = L"";
    OPENFILENAMEW ofn = {};
    ofn.lStructSize = sizeof(ofn);
    ofn.lpstrFilter = L"J2ME game (*.jar)\0*.jar\0All files\0*.*\0";
    ofn.lpstrFile = file;
    ofn.nMaxFile = static_cast<DWORD>(std::size(file));
    ofn.lpstrTitle = L"Select your Soul of Darkness JAR";
    ofn.Flags = OFN_FILEMUSTEXIST | OFN_PATHMUSTEXIST | OFN_NOCHANGEDIR;
    if (!GetOpenFileNameW(&ofn)) return false;
    path = file;
    return true;
}

bool locateData(const std::wstring& explicitData, const std::wstring& explicitJar) {
    if (!explicitData.empty()) {
        if (isDirectory(explicitData) && dataDirValid(explicitData)) { g_dataDir = explicitData; return true; }
        if (!isDirectory(explicitData) && loadJar(explicitData, true).empty()) return true;
        MessageBoxW(nullptr, (L"No Soul of Darkness game data in:\n" + explicitData).c_str(), L"Soul of Darkness", MB_ICONERROR);
        return false;
    }
    if (!explicitJar.empty()) {
        std::string err = loadJar(explicitJar, true);
        if (err.empty()) { g_settings.jar = explicitJar; return true; }
        MessageBoxW(nullptr, widen(err).c_str(), L"Soul of Darkness", MB_ICONERROR);
        return false;
    }
    const std::wstring exe = exeDir();
    // 1. a JAR beside the executable, 2. the JAR chosen last time, 3. an extracted data folder (developer layouts)
    WIN32_FIND_DATAW fd;
    HANDLE h = FindFirstFileW((exe + L"\\*.jar").c_str(), &fd);
    if (h != INVALID_HANDLE_VALUE) {
        do {
            if (loadJar(exe + L"\\" + fd.cFileName, false).empty() && !g_jar.empty()) { FindClose(h); return true; }
        } while (FindNextFileW(h, &fd));
        FindClose(h);
    }
    if (!g_settings.jar.empty() && exists(g_settings.jar) && loadJar(g_settings.jar, false).empty() && !g_jar.empty()) return true;
    std::vector<std::wstring> dirs = {exe + L"\\data", g_saveDir + L"\\data"};
    std::wstring up = exe;
    for (int i = 0; i < 6; ++i) {
        dirs.push_back(up + L"\\soul\\extracted");
        std::size_t slash = up.find_last_of(L"\\/");
        if (slash == std::wstring::npos) break;
        up = up.substr(0, slash);
    }
    for (const auto& d : dirs) if (isDirectory(d) && dataDirValid(d)) { g_dataDir = d; return true; }
    int choice = MessageBoxW(nullptr,
        (std::wstring(L"Soul of Darkness needs the original game's data, which is not included.\n\nSelect your own copy of:\n")
         + widen(sodhost::kExpectedJarName) + L"\n\nYou can also simply place the JAR next to this program.").c_str(),
        L"Soul of Darkness", MB_OKCANCEL | MB_ICONINFORMATION);
    while (choice == IDOK) {
        std::wstring path;
        if (!pickJar(path)) return false;
        std::string err = loadJar(path, true);
        if (err.empty()) { g_settings.jar = path; saveSettings(g_settingsPath, g_settings); return true; }
        choice = MessageBoxW(nullptr, (widen(err) + L"\n\nChoose another file?").c_str(), L"Soul of Darkness", MB_OKCANCEL | MB_ICONWARNING);
    }
    return false;
}

// ---------------------------------------------------------------- input

std::map<int, int> g_codeHolds;       // MIDP key code -> number of physical sources holding it
std::map<int, int> g_keySent;         // virtual key -> MIDP code sent on press
std::map<int, int> g_padSent;         // pad source id -> MIDP code sent on press

void pressCode(int code) {
    if (code == 0) return;
    if (g_codeHolds[code]++ == 0) sodapi::keyPressed(code);
}
void releaseCode(int code) {
    if (code == 0) return;
    auto it = g_codeHolds.find(code);
    if (it == g_codeHolds.end() || it->second == 0) return;
    if (--it->second == 0) sodapi::keyReleased(code);
}
void releaseAll() {
    for (auto& kv : g_codeHolds) if (kv.second > 0) sodapi::keyReleased(kv.first);
    g_codeHolds.clear();
    g_keySent.clear();
    g_padSent.clear();
}

// Desktop action -> phone key, depending on what the game is showing (see sodapi::inputContext).
int codeFor(Action a) {
    const int ctx = sodapi::inputContext();
    switch (a) {
        case ActUp: return sodapi::KeyUp;
        case ActDown: return sodapi::KeyDown;
        case ActLeft: return sodapi::KeyLeft;
        case ActRight: return sodapi::KeyRight;
        case ActJump: return ctx == 1 ? sodapi::KeyUp : ctx == 2 ? sodapi::KeyFire : sodapi::KeySoftLeft;
        case ActAttack: return ctx == 0 ? sodapi::KeySoftLeft : sodapi::KeyFire;
        case ActConfirm: return ctx == 0 ? sodapi::KeySoftLeft : sodapi::KeyFire;
        case ActMagic: return ctx == 0 ? 0 : sodapi::Key0;
        case ActSwitch: return ctx == 0 ? 0 : sodapi::Key9;
        case ActPause: return ctx == 1 ? sodapi::KeySoftLeft : sodapi::KeySoftRight;
        case ActBack: return sodapi::KeySoftRight;
        case ActSoftLeft: return sodapi::KeySoftLeft;
        default: return 0;
    }
}

int keypadCode(int vk) {
    if (vk >= '0' && vk <= '9') return 48 + (vk - '0');
    if (vk >= VK_NUMPAD0 && vk <= VK_NUMPAD9) return 48 + (vk - VK_NUMPAD0);
    if (vk == VK_MULTIPLY) return sodapi::KeyStar;
    if (vk == VK_DIVIDE) return sodapi::KeyPound;
    return 0;
}

void keyDown(int vk) {
    if (g_capture.active) {
        if (g_capture.page == 2 && !keyName(vk).empty()) {
            if (vk != VK_ESCAPE || g_capture.action == ActPause) {
                for (int a = 0; a < ActCount; ++a) {
                    auto& keys = g_settings.keys[a];
                    keys.erase(std::remove(keys.begin(), keys.end(), vk), keys.end());
                }
                g_settings.keys[g_capture.action] = {vk};
            }
            g_capture.active = false; // Esc cancels (except when binding Pause itself)
        } else if (g_capture.page == 3 && vk == VK_ESCAPE) {
            g_capture.active = false;
        }
        return;
    }
    if (g_keySent.count(vk)) return;
    int code = 0;
    for (int a = 0; a < ActCount && !code; ++a)
        for (int bound : g_settings.keys[a]) if (bound == vk) { code = codeFor(static_cast<Action>(a)); break; }
    if (!code) code = keypadCode(vk);
    if (!code) return;
    g_keySent[vk] = code;
    pressCode(code);
}
void keyUp(int vk) {
    auto it = g_keySent.find(vk);
    if (it == g_keySent.end()) return;
    releaseCode(it->second);
    g_keySent.erase(it);
}

struct XInputState { DWORD packet; WORD buttons; BYTE lt, rt; SHORT lx, ly, rx, ry; };
typedef DWORD(WINAPI* XInputGetStateFn)(DWORD, XInputState*);
XInputGetStateFn g_xinput = nullptr;
unsigned g_padPrev = 0;
bool g_padConnected = false;
double g_padRetry = 0;

void loadXInput() {
    for (const wchar_t* dll : {L"xinput1_4.dll", L"xinput1_3.dll", L"xinput9_1_0.dll"}) {
        if (HMODULE m = LoadLibraryW(dll)) {
            g_xinput = reinterpret_cast<XInputGetStateFn>(GetProcAddress(m, "XInputGetState"));
            if (g_xinput) return;
        }
    }
}

void padSource(int id, bool down, Action a) {
    auto it = g_padSent.find(id);
    if (down && it == g_padSent.end()) {
        int code = codeFor(a);
        if (!code) return;
        g_padSent[id] = code;
        pressCode(code);
    } else if (!down && it != g_padSent.end()) {
        releaseCode(it->second);
        g_padSent.erase(it);
    }
}

void pollGamepad() {
    if (!g_xinput) return;
    if (!g_padConnected && now() < g_padRetry) return; // probing empty slots is slow; retry once a second
    XInputState st = {};
    bool found = false;
    for (DWORD i = 0; i < 4 && !found; ++i) found = g_xinput(i, &st) == ERROR_SUCCESS;
    if (!found) {
        if (g_padConnected) { for (int id = 0; id < 64; ++id) padSource(id, false, ActUp); g_padPrev = 0; }
        g_padConnected = false;
        g_padRetry = now() + 1.0;
        return;
    }
    if (!g_padConnected) overlay("Gamepad connected");
    g_padConnected = true;
    unsigned buttons = st.buttons;
    if (st.lt > 60) buttons |= PadLT;
    if (st.rt > 60) buttons |= PadRT;
    const int dead = 32767 * g_settings.deadzone / 100;
    const bool left = (st.buttons & 0x0004) || st.lx < -dead, right = (st.buttons & 0x0008) || st.lx > dead;
    const bool up = (st.buttons & 0x0001) || st.ly > dead, down = (st.buttons & 0x0002) || st.ly < -dead;
    if (g_capture.active && g_capture.page == 3) {
        const unsigned fresh = buttons & ~g_padPrev;
        for (const auto& b : kPadNames) {
            if (fresh & b.mask) {
                for (int a = 0; a < ActCount; ++a) g_settings.pad[a] &= ~b.mask;
                g_settings.pad[g_capture.action] = b.mask;
                g_capture.active = false;
                break;
            }
        }
        g_padPrev = buttons;
        return;
    }
    if (g_capture.active) { g_padPrev = buttons; return; }
    padSource(0, up, ActUp);
    padSource(1, down, ActDown);
    padSource(2, left, ActLeft);
    padSource(3, right, ActRight);
    for (int a = ActJump; a < ActCount; ++a) padSource(8 + a, (buttons & g_settings.pad[a]) != 0, static_cast<Action>(a));
    g_padPrev = buttons;
}

// ---------------------------------------------------------------- window and presentation

const int kWidthPresets[] = {0, 240, 427, 569, 747};
const int kFpsPresets[] = {-1, kNativeFps, 30, 60, 120, 144, 240, 0};
WINDOWPLACEMENT g_placement = {sizeof(WINDOWPLACEMENT)};
HDC g_backDc = nullptr;
HBITMAP g_backBitmap = nullptr;
int g_backW = 0, g_backH = 0;
double g_fpsShown = 0;

int displayRefresh() {
    DEVMODEW dm = {};
    dm.dmSize = sizeof(dm);
    if (EnumDisplaySettingsW(nullptr, ENUM_CURRENT_SETTINGS, &dm) && dm.dmDisplayFrequency > 1) return static_cast<int>(dm.dmDisplayFrequency);
    return 60;
}

int widthForClient(int cw, int ch) {
    if (g_settings.width != 0) return g_settings.width;
    if (cw <= 0 || ch <= 0) return 240;
    int w = static_cast<int>(std::lround(320.0 * cw / ch));
    w = std::clamp(w, 240, 747);
    return w & ~1;
}

void applyLogicalWidth() {
    RECT rc;
    GetClientRect(g_window, &rc);
    int w = widthForClient(rc.right, rc.bottom);
    if (w != g_logicalWidth) {
        g_logicalWidth = w;
        sodapi::Options o;
        o.screenWidth = w;
        o.interpolation = g_settings.interpolation;
        sodapi::configure(o);
    }
}

void reconfigure() {
    sodapi::Options o;
    o.screenWidth = g_logicalWidth;
    o.interpolation = g_settings.interpolation;
    sodapi::configure(o);
}

void setFullscreen(bool on) {
    DWORD style = static_cast<DWORD>(GetWindowLongPtrW(g_window, GWL_STYLE));
    if (on) {
        MONITORINFO mi = {sizeof(mi)};
        if (GetWindowPlacement(g_window, &g_placement) && GetMonitorInfoW(MonitorFromWindow(g_window, MONITOR_DEFAULTTOPRIMARY), &mi)) {
            SetWindowLongPtrW(g_window, GWL_STYLE, style & ~WS_OVERLAPPEDWINDOW);
            SetWindowPos(g_window, HWND_TOP, mi.rcMonitor.left, mi.rcMonitor.top, mi.rcMonitor.right - mi.rcMonitor.left,
                         mi.rcMonitor.bottom - mi.rcMonitor.top, SWP_NOOWNERZORDER | SWP_FRAMECHANGED);
        }
    } else {
        SetWindowLongPtrW(g_window, GWL_STYLE, style | WS_OVERLAPPEDWINDOW);
        SetWindowPlacement(g_window, &g_placement);
        SetWindowPos(g_window, nullptr, 0, 0, 0, 0, SWP_NOMOVE | SWP_NOSIZE | SWP_NOZORDER | SWP_NOOWNERZORDER | SWP_FRAMECHANGED);
    }
    g_settings.fullscreen = on;
    ShowCursor(on ? FALSE : TRUE);
}

void present(HDC dc) {
    RECT rc;
    GetClientRect(g_window, &rc);
    const int cw = rc.right, ch = rc.bottom;
    if (cw <= 0 || ch <= 0) return;
    if (!g_backDc || cw != g_backW || ch != g_backH) {
        if (g_backBitmap) DeleteObject(g_backBitmap);
        if (g_backDc) DeleteDC(g_backDc);
        g_backDc = CreateCompatibleDC(dc);
        g_backBitmap = CreateCompatibleBitmap(dc, cw, ch);
        SelectObject(g_backDc, g_backBitmap);
        g_backW = cw;
        g_backH = ch;
    }
    sodapi::Frame f = sodapi::frame();
    PatBlt(g_backDc, 0, 0, cw, ch, BLACKNESS);
    if (f.pixels && f.width > 0) {
        double scale = std::min(static_cast<double>(cw) / f.width, static_cast<double>(ch) / f.height);
        if (g_settings.integerScale && scale >= 1.0) scale = std::floor(scale);
        const int dw = std::max(1, static_cast<int>(f.width * scale + 0.5)), dh = std::max(1, static_cast<int>(f.height * scale + 0.5));
        const int dx = (cw - dw) / 2, dy = (ch - dh) / 2;
        BITMAPINFO bi = {};
        bi.bmiHeader.biSize = sizeof(bi.bmiHeader);
        bi.bmiHeader.biWidth = f.width;
        bi.bmiHeader.biHeight = -f.height;
        bi.bmiHeader.biPlanes = 1;
        bi.bmiHeader.biBitCount = 32;
        bi.bmiHeader.biCompression = BI_RGB;
        SetStretchBltMode(g_backDc, g_settings.smooth ? HALFTONE : COLORONCOLOR);
        if (g_settings.smooth) SetBrushOrgEx(g_backDc, 0, 0, nullptr);
        StretchDIBits(g_backDc, dx, dy, dw, dh, 0, 0, f.width, f.height, f.pixels, &bi, DIB_RGB_COLORS, SRCCOPY);
    }
    std::string text;
    if (now() < g_overlayUntil) text = g_overlay;
    if (g_settings.showFps) {
        char buf[64];
        std::snprintf(buf, sizeof(buf), "%.0f fps", g_fpsShown);
        text = text.empty() ? buf : text + "   " + buf;
    }
    if (!text.empty()) {
        std::wstring w = widen(text);
        SetBkMode(g_backDc, OPAQUE);
        SetBkColor(g_backDc, RGB(0, 0, 0));
        SetTextColor(g_backDc, RGB(255, 220, 120));
        RECT tr = {8, 8, cw - 8, ch - 8};
        DrawTextW(g_backDc, w.c_str(), static_cast<int>(w.size()), &tr, DT_LEFT | DT_TOP | DT_NOPREFIX | DT_WORDBREAK);
    }
    BitBlt(dc, 0, 0, cw, ch, g_backDc, 0, 0, SRCCOPY);
}

std::string describeFps() {
    if (g_settings.fps < 0) return "display refresh (" + std::to_string(displayRefresh()) + " Hz)";
    if (g_settings.fps == 0) return "unlimited";
    if (g_settings.fps == kNativeFps) return "native (one frame per tick, original cadence)";
    return std::to_string(g_settings.fps) + " fps";
}

void helpOverlay() {
    overlay("Settings are in the game: OPTIONS in the main menu and the pause menu (display, audio, keyboard, gamepad).\n"
            "Move: arrows / WASD   Jump: Space   Attack: J / E / Z / Enter   Magic: K / X   Switch weapon: Q / L / C\n"
            "Pause: Esc   Upgrade weapon / back / skip: U or Backspace   Phone keypad: 0-9\n"
            "Gamepad: A jump, X attack, B magic, Y switch, Start pause, Back upgrade / skip\n"
            "Shortcuts: F1 help  F8 width  F9 frame rate  F10 smooth motion  F11 fullscreen  F6 mute  F3 fps");
    g_overlayUntil = now() + 8.0;
}

void hostKey(int vk) {
    switch (vk) {
        case VK_F1: helpOverlay(); break;
        case VK_F3: g_settings.showFps = !g_settings.showFps; break;
        case VK_F5: overlay(saveSettings(g_settingsPath, g_settings) ? "Settings saved" : "Could not save settings"); break;
        case VK_F6:
            g_settings.mute = !g_settings.mute;
            g_music.applyVolume();
            overlay(g_settings.mute ? "Music muted" : "Music on");
            break;
        case VK_F8: {
            int i = 0;
            for (int k = 0; k < static_cast<int>(std::size(kWidthPresets)); ++k) if (kWidthPresets[k] == g_settings.width) i = k;
            g_settings.width = kWidthPresets[(i + 1) % std::size(kWidthPresets)];
            applyLogicalWidth();
            overlay(g_settings.width == 0 ? "Width: auto (" + std::to_string(g_logicalWidth) + ", follows the window)"
                                          : "Width: " + std::to_string(g_settings.width) + (g_settings.width == 240 ? " (original)" : ""));
            break;
        }
        case VK_F9: {
            int i = 0;
            for (int k = 0; k < static_cast<int>(std::size(kFpsPresets)); ++k) if (kFpsPresets[k] == g_settings.fps) i = k;
            g_settings.fps = kFpsPresets[(i + 1) % std::size(kFpsPresets)];
            overlay("Frame rate: " + describeFps());
            break;
        }
        case VK_F10:
            g_settings.interpolation = !g_settings.interpolation;
            reconfigure();
            overlay(g_settings.interpolation ? "Interpolation on" : "Interpolation off (original 20 fps motion)");
            break;
        case VK_F11: setFullscreen(!g_settings.fullscreen); break;
        default: break;
    }
}

LRESULT CALLBACK windowProc(HWND hwnd, UINT msg, WPARAM wp, LPARAM lp) {
    switch (msg) {
        case WM_CLOSE: g_quit = true; return 0;
        case WM_DESTROY: PostQuitMessage(0); return 0;
        case WM_ERASEBKGND: return 1;
        case WM_PAINT: {
            PAINTSTRUCT ps;
            HDC dc = BeginPaint(hwnd, &ps);
            present(dc);
            EndPaint(hwnd, &ps);
            return 0;
        }
        case WM_SIZE:
            if (g_window) applyLogicalWidth();
            return 0;
        case WM_ACTIVATE: {
            const bool active = !g_settings.pauseUnfocused || (LOWORD(wp) != WA_INACTIVE && !HIWORD(wp));
            if (LOWORD(wp) == WA_INACTIVE) releaseAll();
            if (active != g_active) {
                g_active = active;
                releaseAll();
                if (active) sodapi::showNotify();
                else sodapi::hideNotify();
            }
            return 0;
        }
        case WM_SYSKEYDOWN:
            if (wp == VK_RETURN && (lp & (1 << 29))) { setFullscreen(!g_settings.fullscreen); return 0; }
            if (wp == VK_F10) { hostKey(VK_F10); return 0; }
            break;
        case WM_KEYDOWN:
            if (lp & (1 << 30)) return 0; // auto-repeat
            if (wp >= VK_F1 && wp <= VK_F12) hostKey(static_cast<int>(wp));
            else keyDown(static_cast<int>(wp));
            return 0;
        case WM_KEYUP:
            keyUp(static_cast<int>(wp));
            return 0;
        case MM_MCINOTIFY:
            if (wp == MCI_NOTIFY_SUCCESSFUL) g_music.finished();
            return 0;
        case WM_GETMINMAXINFO: {
            MINMAXINFO* mm = reinterpret_cast<MINMAXINFO*>(lp);
            mm->ptMinTrackSize.x = 260;
            mm->ptMinTrackSize.y = 360;
            return 0;
        }
        default: break;
    }
    return DefWindowProcW(hwnd, msg, wp, lp);
}

} // namespace

int WINAPI wWinMain(HINSTANCE instance, HINSTANCE, PWSTR, int show) {
    int argc = 0;
    wchar_t** argv = CommandLineToArgvW(GetCommandLineW(), &argc);
    std::wstring dataArg, jarArg, configArg, logArg;
    defaultBindings(g_settings);
    g_saveDir = userDir();
    bool widthOverride = false, fpsOverride = false, fullscreenOverride = false, windowed = false, noFocusPause = false, noOptionsMenu = false;
    Settings cli;
    for (int i = 1; i < argc; ++i) {
        std::wstring a = argv[i];
        auto value = [&]() -> std::wstring { return i + 1 < argc ? argv[++i] : L""; };
        if (a == L"--data") dataArg = value();
        else if (a == L"--jar") jarArg = value();
        else if (a == L"--config") configArg = value();
        else if (a == L"--save-dir") { g_saveDir = value(); CreateDirectoryW(g_saveDir.c_str(), nullptr); }
        else if (a == L"--width") { std::wstring v = value(); cli.width = (v == L"auto") ? 0 : std::clamp(_wtoi(v.c_str()), 240, 960); widthOverride = true; }
        else if (a == L"--fps") { std::wstring v = value(); cli.fps = v == L"display" ? -1 : v == L"unlimited" ? 0 : v == L"native" ? kNativeFps : std::clamp(_wtoi(v.c_str()), 1, 1000); fpsOverride = true; }
        else if (a == L"--fullscreen") fullscreenOverride = true;
        else if (a == L"--windowed") windowed = true;
        else if (a == L"--no-focus-pause") noFocusPause = true;
        else if (a == L"--log") logArg = value();
        else if (a == L"--original-menus") noOptionsMenu = true;
        else if (a == L"--autoplay") {
            // "<tick> <+|-> <phone key code>" lines, the same format soul_engine_check uses
            std::vector<std::uint8_t> bytes;
            if (readFile(value(), bytes)) {
                std::stringstream in(std::string(bytes.begin(), bytes.end()));
                long long t; std::string op; int key;
                while (in >> t >> op >> key) g_autoplay.emplace(t, std::make_pair(op == "+", key));
            }
        }
        else if (a == L"--help" || a == L"-h" || a == L"/?") {
            MessageBoxW(nullptr,
                L"Soul of Darkness (desktop port)\n\n"
                L"--jar FILE        use this Soul of Darkness J2ME JAR\n"
                L"--data DIR        use an extracted data folder instead\n"
                L"--width N|auto    logical width: 240 original, 427, 569, 747 or auto\n"
                L"--fps N|display|unlimited|native\n"
                L"--fullscreen / --windowed\n"
                L"--no-focus-pause  keep running while another window has focus\n"
                L"--config FILE     settings file (default: settings.ini beside the program, else in %LOCALAPPDATA%\\SoulOfDarkness101)\n"
                L"--save-dir DIR    where the save record is kept\n\nPress F1 in the game for controls.",
                L"Soul of Darkness", MB_OK);
            return 0;
        }
    }
    g_settingsPath = !configArg.empty() ? configArg : exists(exeDir() + L"\\settings.ini") ? exeDir() + L"\\settings.ini" : g_saveDir + L"\\settings.ini";
    const bool hadSettings = loadSettings(g_settingsPath, g_settings);
    if (widthOverride) g_settings.width = cli.width;
    if (fpsOverride) g_settings.fps = cli.fps;
    if (fullscreenOverride) g_settings.fullscreen = true;
    if (windowed) g_settings.fullscreen = false;
    if (noFocusPause) g_settings.pauseUnfocused = false;

    if (!locateData(dataArg, jarArg)) return 1;
    if (!hadSettings) saveSettings(g_settingsPath, g_settings);

    WNDCLASSEXW wc = {};
    wc.cbSize = sizeof(wc);
    wc.lpfnWndProc = windowProc;
    wc.hInstance = instance;
    wc.hCursor = LoadCursorW(nullptr, IDC_ARROW);
    wc.hIcon = LoadIconW(nullptr, IDI_APPLICATION);
    wc.lpszClassName = L"SoulOfDarknessWindow";
    RegisterClassExW(&wc);
    const int startW = g_settings.width == 0 ? 569 : g_settings.width;
    RECT rc = {0, 0, startW * g_settings.windowScale, 320 * g_settings.windowScale};
    RECT work;
    SystemParametersInfoW(SPI_GETWORKAREA, 0, &work, 0);
    while ((rc.right > work.right - work.left - 40 || rc.bottom > work.bottom - work.top - 80) && rc.bottom > 320) {
        rc.right -= startW;
        rc.bottom -= 320;
    }
    AdjustWindowRect(&rc, WS_OVERLAPPEDWINDOW, FALSE);
    g_window = CreateWindowExW(0, wc.lpszClassName, L"Soul of Darkness", WS_OVERLAPPEDWINDOW, CW_USEDEFAULT, CW_USEDEFAULT,
                               rc.right - rc.left, rc.bottom - rc.top, nullptr, nullptr, instance, nullptr);
    if (!g_window) return 1;

    RECT client;
    GetClientRect(g_window, &client);
    g_logicalWidth = widthForClient(client.right, client.bottom);
    sodapi::Options options;
    options.screenWidth = g_logicalWidth;
    options.interpolation = g_settings.interpolation;
    options.optionsMenu = !noOptionsMenu;
    sodapi::configure(options);
    LARGE_INTEGER seed;
    QueryPerformanceCounter(&seed);
    sodapi::start(seed.QuadPart ^ static_cast<long long>(GetTickCount64()), false);

    ShowWindow(g_window, show);
    if (g_settings.fullscreen) setFullscreen(true);
    loadXInput();
    timeBeginPeriod(1);
    if (!hadSettings) helpOverlay();

    double kTick = sodapi::tickSeconds(); // Game.run(): the original loop period, re-read after every tick
    double last = now(), accumulator = 0, nextFrame = last, fpsWindowStart = last;
    int fpsFrames = 0;
    bool freshTick = true;
    while (!g_quit && !sodapi::finished()) {
        MSG msg;
        while (PeekMessageW(&msg, nullptr, 0, 0, PM_REMOVE)) {
            if (msg.message == WM_QUIT) g_quit = true;
            TranslateMessage(&msg);
            DispatchMessageW(&msg);
        }
        if (g_quit) break;
        pollGamepad();
        const double t = now();
        double dt = t - last;
        last = t;
        if (dt > 0.25) dt = 0.25; // window drags, breakpoints
        if (g_active) accumulator += dt;
        int ran = 0;
        while (accumulator >= kTick && ran < 3) {
            auto scripted = g_autoplay.equal_range(g_tickCount);
            for (auto it = scripted.first; it != scripted.second; ++it) {
                if (it->second.first) sodapi::keyPressed(it->second.second);
                else sodapi::keyReleased(it->second.second);
            }
            ++g_tickCount;
            sodapi::tick();
            accumulator -= kTick;
            kTick = sodapi::tickSeconds();
            ++ran;
            freshTick = true;
        }
        if (ran == 3) accumulator = 0;

        const bool interpolate = g_settings.interpolation && g_settings.fps != kNativeFps && g_active;
        bool draw = freshTick;
        if (interpolate) draw = sodapi::renderInterpolated(std::clamp(accumulator / kTick, 0.0, 1.0)) || freshTick;
        if (draw) {
            HDC dc = GetDC(g_window);
            present(dc);
            ReleaseDC(g_window, dc);
            freshTick = false;
            ++fpsFrames;
        }
        if (t - fpsWindowStart >= 0.5) {
            g_fpsShown = fpsFrames / (t - fpsWindowStart);
            fpsFrames = 0;
            fpsWindowStart = t;
        }
        // Pace presentation. Without interpolation there is nothing new to show between ticks.
        // Native shows each tick as it is produced (20 fps, 15 during cutscenes), so it needs no cap of its own.
        int cap = g_settings.fps < 0 ? displayRefresh() : g_settings.fps == kNativeFps ? 0 : g_settings.fps;
        double frameTime = cap > 0 ? 1.0 / cap : 0.0;
        if (!interpolate) frameTime = std::max(frameTime, 0.004);
        nextFrame += frameTime;
        double current = now();
        if (nextFrame < current - 0.1) nextFrame = current;
        double untilTick = kTick - accumulator;
        double wait = std::min(nextFrame - current, g_active ? untilTick : 0.05);
        if (!g_active) wait = 0.05;
        if (wait > 0.002) Sleep(static_cast<DWORD>((wait - 0.001) * 1000.0));
        else if (cap == 0 && interpolate) Sleep(0);
        while (cap > 0 && g_active && now() < std::min(nextFrame, last + untilTick)) YieldProcessor();
    }
    timeEndPeriod(1);
    sodapi::shutdown();
    g_music.stop();
    saveSettings(g_settingsPath, g_settings);
    if (!logArg.empty()) {
        std::string text;
        for (const auto& line : g_log) text += line + "\n";
        writeFile(logArg, text.data(), text.size());
    }
    DestroyWindow(g_window);
    return 0;
}
