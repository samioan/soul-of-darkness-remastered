// The launcher -- what someone who downloaded a release zip actually opens.
//
// Ported from tes-travels-decomp's Dawnstar launcher (itself from
// shadowkey-decomp's), same architecture throughout: owner-drawn buttons, an
// Impl-in-GWLP_USERDATA idiom, an artwork-sized window, a detached worker
// thread posting WM_APP_* messages back for the update check/install,
// IFileOpenDialog pickers, hidden-while-playing + MsgWaitForMultipleObjects.
//
// What differs for Soul of Darkness: the game reads its JAR in place, so
// "Choose file..." checks the picked .jar with the game's own rules and copies
// it into game/ rather than unpacking it (see jar_install.h); there is no font
// row; and the display row offers the window sizes that fit a 240x320 portrait
// game on a desktop screen.
//
// It draws its own text and its own buttons rather than using a dialog
// template: the artwork is a dark night scene, and the default Win32 controls
// on top of it look like a bug. Only the buttons are real windows (they need
// focus, keyboard activation and the accessibility that comes with them);
// every label is drawn in WM_PAINT.

#include <windows.h>

#include <commctrl.h>
#include <shlobj.h>
#include <shobjidl.h>

#include <algorithm>
#include <filesystem>
#include <memory>
#include <string>
#include <thread>
#include <vector>

#include "launcher/banner.h"
#include "launcher/jar_install.h"
#include "launcher/launcher_config.h"
#include "launcher/resource.h"
#include "launcher/update_check.h"
#include "launcher/updater.h"
#include "launcher/version.h"

namespace {

namespace fs = std::filesystem;
using sod::launcher::LauncherConfig;

// ---------------------------------------------------------------------
// Palette. The banner is a moonlit night in blue and violet with one fire
// (the orb in the hero's hand), so the panel continues the night -- icy blue
// captions like the SOUL lettering, a violet rule like DARKNESS -- and the one
// warm colour is kept for Play. SeamColorFor below samples the artwork's own
// last row so the panel actually blends into it.
// ---------------------------------------------------------------------
constexpr COLORREF kBackground = RGB(8, 8, 16);
constexpr COLORREF kPanel = RGB(13, 12, 22);
constexpr COLORREF kPanelHigh = RGB(28, 26, 46);
constexpr COLORREF kRule = RGB(104, 82, 160);   // the DARKNESS violet
constexpr COLORREF kText = RGB(226, 228, 240);
constexpr COLORREF kCaption = RGB(140, 176, 228); // the SOUL ice blue
constexpr COLORREF kMuted = RGB(124, 128, 152);
constexpr COLORREF kFire = RGB(236, 142, 52);    // the orb
constexpr COLORREF kGood = RGB(170, 200, 255);
constexpr COLORREF kBad = RGB(232, 112, 112);

// Layout in logical (96-dpi) pixels; every use goes through Impl::S().
//
// The header's height is derived from the artwork's own proportions, not
// hardcoded, so a differently-proportioned banner re-lays the window instead
// of slicing the wordmark off.
constexpr int kClientWidth = 760;
constexpr int kMinClientWidth = 520;
constexpr int kPanelHeight = 248;
constexpr int kFallbackHeaderHeight = 200;  // only if the artwork fails to decode

constexpr int kMargin = 36;
constexpr int kButtonWidth = 160;
constexpr int kButtonHeight = 30;
constexpr int kButtonRight = kMargin + kButtonWidth;  // from the window's right edge

// All offsets below are measured from the top of the panel, not the top of
// the window, since the header above them moves with the artwork.
constexpr int kRow1Caption = 20;
constexpr int kRow1Value = 40;
constexpr int kRow1Button = 32;
constexpr int kRow2Caption = 80;
constexpr int kScaleY = 100;
constexpr int kScaleWidth = 44;
constexpr int kFullscreenWidth = 104;
constexpr int kScaleHeight = 28;
constexpr int kPlayY = 84;
constexpr int kPlayHeight = 44;
constexpr int kStatusY = 142;
constexpr int kVersionY = 186;
constexpr int kUpdateY = 208;

enum ControlId : int {
    IDC_CHOOSE_DATA = 1001,
    IDC_PLAY = 1002,
    IDC_UPDATE = 1003,
    IDC_SCALE_FIRST = 1010,  // +0 => 2x, +1 => 3x windowed, +2 => fullscreen
};

// 240x320 at 3x is 720x960, the largest whole multiple that still fits a
// 1080p screen in portrait; the game's OPTIONS screen offers the rest.
constexpr int kScaleChoices[] = {2, 3};
constexpr int kScaleCount = 2;
constexpr int kDisplayButtons = kScaleCount + 1;

// Where the picked JAR is installed, relative to the launcher.
constexpr const char* kInstalledJar = "game/SoulOfDarkness.jar";

// What the update worker thread sends back. The thread never touches the
// launcher's state directly -- it posts a heap-allocated result the window
// procedure takes ownership of. That is what makes the worker safe to
// detach: if the window has gone, PostMessage fails and the worker frees
// its own result, and nothing is left pointing at a dead stack frame.
constexpr UINT WM_APP_UPDATE_CHECKED = WM_APP + 1;
constexpr UINT WM_APP_UPDATE_PROGRESS = WM_APP + 2;
constexpr UINT WM_APP_UPDATE_DONE = WM_APP + 3;

struct UpdateCheckResult {
    bool ok = false;
    sod::launcher::ReleaseInfo release;
    std::string error;
};

struct UpdateProgressMessage {
    sod::launcher::UpdateProgress progress;
};

struct UpdateDoneMessage {
    bool ok = false;
    std::string error;
    std::string tag;
};

enum class UpdateState {
    Disabled,   // a -dev build: never offers to replace a build tree
    Checking,
    UpToDate,
    Available,
    Installing,
    Failed,
};

std::wstring Widen(const std::string& text) {
    if (text.empty()) return std::wstring();
    const int size = MultiByteToWideChar(CP_UTF8, 0, text.c_str(), static_cast<int>(text.size()),
                                         nullptr, 0);
    std::wstring wide(static_cast<size_t>(size), L'\0');
    MultiByteToWideChar(CP_UTF8, 0, text.c_str(), static_cast<int>(text.size()), wide.data(), size);
    return wide;
}

std::string Narrow(const std::wstring& text) {
    if (text.empty()) return std::string();
    const int size = WideCharToMultiByte(CP_UTF8, 0, text.c_str(), static_cast<int>(text.size()),
                                         nullptr, 0, nullptr, nullptr);
    std::string narrow(static_cast<size_t>(size), '\0');
    WideCharToMultiByte(CP_UTF8, 0, text.c_str(), static_cast<int>(text.size()), narrow.data(),
                        size, nullptr, nullptr);
    return narrow;
}

// The folder this executable lives in, as UTF-8.
std::string ExecutableDirectory() {
    std::wstring path(MAX_PATH, L'\0');
    for (;;) {
        const DWORD length = GetModuleFileNameW(nullptr, path.data(), static_cast<DWORD>(path.size()));
        if (length == 0) return std::string(".");
        if (length < path.size()) {
            path.resize(length);
            break;
        }
        path.resize(path.size() * 2);
    }
    return Narrow(fs::path(path).parent_path().wstring());
}

// Long absolute paths are the normal case here (people keep downloads deep
// in Downloads), and the value row is only so wide. Middle-elision keeps
// both the drive and the leaf visible.
std::wstring ElidePath(const std::wstring& path, size_t limit) {
    if (path.size() <= limit || limit < 12) return path;
    const size_t tail = limit * 2 / 3;
    const size_t head = limit - tail - 3;
    return path.substr(0, head) + L"..." + path.substr(path.size() - tail);
}

// ---------------------------------------------------------------------
// Owner-drawn buttons.
// ---------------------------------------------------------------------
struct ButtonStyle {
    bool primary = false;
    bool selected = false;
};

// Hover is not something a plain BS_OWNERDRAW button reports, so each
// button is subclassed for the two messages that track it.
LRESULT CALLBACK ButtonSubclass(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam,
                                UINT_PTR idSubclass, DWORD_PTR refData) {
    (void)idSubclass;
    bool* hovered = reinterpret_cast<bool*>(refData);
    switch (message) {
        case WM_MOUSEMOVE:
            if (hovered && !*hovered) {
                *hovered = true;
                TRACKMOUSEEVENT track{sizeof(track), TME_LEAVE, hwnd, 0};
                TrackMouseEvent(&track);
                InvalidateRect(hwnd, nullptr, FALSE);
            }
            break;
        case WM_MOUSELEAVE:
            if (hovered && *hovered) {
                *hovered = false;
                InvalidateRect(hwnd, nullptr, FALSE);
            }
            break;
        default:
            break;
    }
    return DefSubclassProc(hwnd, message, wParam, lParam);
}

struct Button {
    HWND hwnd = nullptr;
    bool hovered = false;
    ButtonStyle style;
};

// ---------------------------------------------------------------------

struct Impl {
    HWND hwnd = nullptr;
    int dpi = 96;

    sod::launcher::Banner banner;
    std::string installRoot;   // where SoulOfDarkness.exe lives
    std::string configPath;
    LauncherConfig config;

    // Resolved absolute path and whether it currently checks out. Rechecked
    // at startup, after a pick and after each game session rather than cached
    // at pick time, so an install whose JAR was deleted behind our back says
    // so instead of failing at Play.
    std::string jarPath;
    bool jarOk = false;
    bool jarVerified = false;

    std::wstring status;
    COLORREF statusColor = kMuted;
    bool busy = false;

    Button chooseData;
    Button play;
    Button update;
    Button display[kDisplayButtons];  // 2x, 3x windowed, and fullscreen

    UpdateState updateState = UpdateState::Checking;
    sod::launcher::ReleaseInfo availableRelease;
    std::wstring updateNote;
    // Set while an install is in flight. The window refuses to close and
    // the game refuses to start during it -- replacing soul_of_darkness.exe
    // out from under a running game would be a genuinely bad time.
    bool installing = false;

    HFONT uiFont = nullptr;
    HFONT captionFont = nullptr;
    HFONT valueFont = nullptr;
    HFONT playFont = nullptr;

    // Both in device pixels, settled once at startup by SizeToArtwork().
    int clientWidth = 0;
    int panelTop = 0;
    // The artwork's own colour where the panel cuts it off, darkened.
    COLORREF seamColor = kPanel;

    int S(int logical) const { return MulDiv(logical, dpi, 96); }
    // Panel-relative y, in device pixels.
    int P(int logical) const { return panelTop + S(logical); }
    int ButtonX() const { return clientWidth - S(kButtonRight); }
};

// How tall the header is when the artwork is drawn across `width`.
int HeaderHeightFor(const sod::launcher::Banner& banner, int width, int fallback) {
    if (!banner.valid()) return fallback;
    return MulDiv(banner.height, width, banner.width);
}

// The inverse: the width at which the header comes out `header` tall. Used
// only when the preferred window would not fit on the screen.
int WidthForHeaderHeight(const sod::launcher::Banner& banner, int header) {
    if (!banner.valid() || header <= 0) return 0;
    return MulDiv(banner.width, header, banner.height);
}

COLORREF Blend(COLORREF from, COLORREF to, int amount /* 0..255 */) {
    const int inverse = 255 - amount;
    return RGB((GetRValue(from) * inverse + GetRValue(to) * amount) / 255,
               (GetGValue(from) * inverse + GetGValue(to) * amount) / 255,
               (GetBValue(from) * inverse + GetBValue(to) * amount) / 255);
}

// The average colour of the artwork's last row, darkened so the panel still
// reads as a panel.
COLORREF SeamColorFor(const sod::launcher::Banner& banner) {
    if (!banner.valid()) return kPanel;
    const int row = banner.height - 1;
    unsigned long long red = 0, green = 0, blue = 0;
    const uint32_t* pixels = banner.pixels.data() + static_cast<size_t>(row) * banner.width;
    for (int x = 0; x < banner.width; ++x) {
        const unsigned char* bgra = reinterpret_cast<const unsigned char*>(&pixels[x]);
        blue += bgra[0];
        green += bgra[1];
        red += bgra[2];
    }
    const int count = banner.width;
    const COLORREF average = RGB(static_cast<int>(red / count), static_cast<int>(green / count),
                                 static_cast<int>(blue / count));
    return Blend(average, kPanel, 150);
}

Impl* Self(HWND hwnd) {
    return reinterpret_cast<Impl*>(GetWindowLongPtrW(hwnd, GWLP_USERDATA));
}

// GetDpiForWindow is Win10 1607+; the manifest claims back to 7, so it is
// resolved at runtime with the classic device-caps reading as the fallback.
int QueryDpi(HWND hwnd) {
    using GetDpiForWindowFn = UINT(WINAPI*)(HWND);
    if (HMODULE user32 = GetModuleHandleW(L"user32.dll")) {
        auto getDpi =
            reinterpret_cast<GetDpiForWindowFn>(GetProcAddress(user32, "GetDpiForWindow"));
        if (getDpi) {
            const UINT dpi = getDpi(hwnd);
            if (dpi >= 72) return static_cast<int>(dpi);
        }
    }
    HDC dc = GetDC(hwnd);
    const int dpi = dc ? GetDeviceCaps(dc, LOGPIXELSX) : 96;
    if (dc) ReleaseDC(hwnd, dc);
    return dpi >= 72 ? dpi : 96;
}

HFONT MakeFont(int dpi, int pointSize, int weight) {
    return CreateFontW(-MulDiv(pointSize, dpi, 72), 0, 0, 0, weight, FALSE, FALSE, FALSE,
                       DEFAULT_CHARSET, OUT_TT_PRECIS, CLIP_DEFAULT_PRECIS, CLEARTYPE_QUALITY,
                       VARIABLE_PITCH | FF_SWISS, L"Segoe UI");
}

void SetStatus(Impl& impl, const std::wstring& text, COLORREF color) {
    impl.status = text;
    impl.statusColor = color;
    InvalidateRect(impl.hwnd, nullptr, FALSE);
}

// ---------------------------------------------------------------------
// State
// ---------------------------------------------------------------------

// Reads the configured JAR again (618 KB, so this is instant).
void RecheckJar(Impl& impl) {
    impl.jarPath = sod::launcher::ResolveAgainst(impl.installRoot, impl.config.jar);
    impl.jarOk = false;
    impl.jarVerified = false;
    if (impl.jarPath.empty()) return;
    const sod::launcher::JarCheck check = sod::launcher::CheckJar(impl.jarPath);
    impl.jarOk = check.usable;
    impl.jarVerified = check.verified;
}

void Refresh(Impl& impl) {
    const bool busy = impl.busy || impl.installing;
    EnableWindow(impl.play.hwnd, impl.jarOk && !busy);
    EnableWindow(impl.chooseData.hwnd, !busy);
    for (int i = 0; i < kDisplayButtons; ++i) {
        impl.display[i].style.selected =
            i == kScaleCount ? impl.config.fullscreen
                             : (!impl.config.fullscreen && kScaleChoices[i] == impl.config.scale);
        EnableWindow(impl.display[i].hwnd, !busy);
        InvalidateRect(impl.display[i].hwnd, nullptr, FALSE);
    }

    // The update button exists only when there is actually an update: a
    // permanently visible "check for updates" that usually says "no" is
    // noise, and this checks on its own at startup anyway.
    const bool offerUpdate = impl.updateState == UpdateState::Available && !busy;
    ShowWindow(impl.update.hwnd, offerUpdate ? SW_SHOW : SW_HIDE);
    EnableWindow(impl.update.hwnd, offerUpdate);

    InvalidateRect(impl.hwnd, nullptr, FALSE);
}

void Save(Impl& impl) { sod::launcher::SaveLauncherConfig(impl.configPath, impl.config); }

// ---------------------------------------------------------------------
// Updates
// ---------------------------------------------------------------------

std::wstring FormatSize(unsigned long long bytes) {
    if (bytes >= 1024ull * 1024) {
        wchar_t text[32];
        swprintf(text, 32, L"%.1f MB", static_cast<double>(bytes) / (1024.0 * 1024.0));
        return text;
    }
    return std::to_wstring(bytes / 1024) + L" KB";
}

// Both workers below are detached and own everything they touch. Nothing
// here reads or writes `Impl` -- see WM_APP_UPDATE_* for why.
void StartUpdateCheck(HWND hwnd) {
    std::thread([hwnd]() {
        auto* result = new UpdateCheckResult();
        result->ok = sod::launcher::FetchLatestRelease(SOD_UPDATE_OWNER, SOD_UPDATE_REPO,
                                                       SOD_UPDATE_TAG_PREFIX, result->release,
                                                       result->error);
        if (!PostMessageW(hwnd, WM_APP_UPDATE_CHECKED, 0, reinterpret_cast<LPARAM>(result))) {
            delete result;  // the window has gone; nobody will take it
        }
    }).detach();
}

void ForwardProgress(const sod::launcher::UpdateProgress& progress, void* context) {
    HWND hwnd = static_cast<HWND>(context);
    auto* message = new UpdateProgressMessage{progress};
    if (!PostMessageW(hwnd, WM_APP_UPDATE_PROGRESS, 0, reinterpret_cast<LPARAM>(message))) {
        delete message;
    }
}

void StartInstall(HWND hwnd, const std::string& installRoot,
                  const sod::launcher::ReleaseInfo& release) {
    std::thread([hwnd, installRoot, release]() {
        auto* done = new UpdateDoneMessage();
        done->tag = release.tag;
        done->ok = sod::launcher::InstallUpdate(installRoot, release, ForwardProgress, hwnd,
                                                done->error);
        if (!PostMessageW(hwnd, WM_APP_UPDATE_DONE, 0, reinterpret_cast<LPARAM>(done))) {
            delete done;
        }
    }).detach();
}

// ---------------------------------------------------------------------
// The game file
// ---------------------------------------------------------------------

// IFileOpenDialog rather than the classic common dialog: it has a modern
// address bar (pasting a path -- how most people navigate to a download --
// works in it).
std::wstring PickJar(HWND owner) {
    IFileOpenDialog* dialog = nullptr;
    if (FAILED(CoCreateInstance(CLSID_FileOpenDialog, nullptr, CLSCTX_INPROC_SERVER,
                                IID_PPV_ARGS(&dialog)))) {
        return std::wstring();
    }
    DWORD options = 0;
    if (SUCCEEDED(dialog->GetOptions(&options))) {
        dialog->SetOptions(options | FOS_FORCEFILESYSTEM | FOS_NOCHANGEDIR | FOS_FILEMUSTEXIST);
    }
    dialog->SetTitle(L"Select your Soul of Darkness .jar");
    const COMDLG_FILTERSPEC filter[] = {{L"J2ME game (*.jar)", L"*.jar"}, {L"All files", L"*.*"}};
    dialog->SetFileTypes(2, filter);

    std::wstring chosen;
    if (SUCCEEDED(dialog->Show(owner))) {
        IShellItem* item = nullptr;
        if (SUCCEEDED(dialog->GetResult(&item)) && item) {
            PWSTR path = nullptr;
            if (SUCCEEDED(item->GetDisplayName(SIGDN_FILESYSPATH, &path)) && path) {
                chosen = path;
                CoTaskMemFree(path);
            }
            item->Release();
        }
    }
    dialog->Release();
    return chosen;
}

// Checks `source` and copies it into game/. `ask` decides whether an
// unverified JAR is offered to the user (picked by hand) or skipped (found
// automatically). Returns true if a JAR was installed.
bool InstallJarFrom(Impl& impl, const std::string& source, bool ask) {
    const sod::launcher::JarCheck check = sod::launcher::CheckJar(source);
    if (!check.usable) {
        if (ask) SetStatus(impl, Widen(check.error), kBad);
        return false;
    }
    if (!check.verified) {
        if (!ask) return false;
        const std::wstring message =
            L"This JAR is not the verified build:\n" + Widen(sod::launcher::ExpectedJarName()) +
            L"\n\nIt has the expected resources, so it may still work, but it has not been "
            L"tested.\nUse it anyway?";
        if (MessageBoxW(impl.hwnd, message.c_str(), L"Soul of Darkness Remastered",
                        MB_YESNO | MB_ICONWARNING) != IDYES) {
            SetStatus(impl, L"Nothing changed.", kMuted);
            return false;
        }
    }

    const std::string destination = (fs::u8path(impl.installRoot) / fs::u8path(kInstalledJar)).u8string();
    std::string error;
    if (!sod::launcher::InstallJar(source, destination, error)) {
        SetStatus(impl, Widen(error), kBad);
        return false;
    }
    impl.config.jar = sod::launcher::RelativeToIfInside(impl.installRoot, destination);
    Save(impl);
    RecheckJar(impl);
    Refresh(impl);
    SetStatus(impl,
              check.verified ? L"Game file installed. Ready to play."
                             : L"Game file installed (an unverified build). Ready to play.",
              kGood);
    return true;
}

void ChooseGameData(Impl& impl) {
    const std::wstring picked = PickJar(impl.hwnd);
    if (picked.empty()) return;
    impl.busy = true;
    Refresh(impl);
    InstallJarFrom(impl, Narrow(picked), true);
    impl.busy = false;
    Refresh(impl);
}

// First run with no JAR configured: a verified JAR dropped beside the
// launcher is picked up without asking, so "unzip, add the JAR, play" needs
// no file dialog at all.
bool AutoInstallJar(Impl& impl) {
    std::error_code error;
    for (const fs::directory_entry& entry : fs::directory_iterator(fs::u8path(impl.installRoot), error)) {
        if (error) break;
        if (!entry.is_regular_file(error)) continue;
        std::wstring extension = entry.path().extension().wstring();
        std::transform(extension.begin(), extension.end(), extension.begin(), ::towlower);
        if (extension != L".jar") continue;
        if (InstallJarFrom(impl, entry.path().u8string(), false)) return true;
    }
    return false;
}

// ---------------------------------------------------------------------
// Play
// ---------------------------------------------------------------------

// `bin/` is the shipped layout; the bare name is where a developer's own
// build puts it, right beside the launcher in soul/port/build/<config>/.
std::string FindGameExecutable(const std::string& installRoot) {
    const fs::path candidates[] = {
        fs::u8path(installRoot) / "bin" / "soul_of_darkness.exe",
        fs::u8path(installRoot) / "soul_of_darkness.exe",
    };
    std::error_code error;
    for (const fs::path& candidate : candidates) {
        if (fs::is_regular_file(candidate, error) && !error) return candidate.u8string();
    }
    return std::string();
}

void Play(Impl& impl) {
    RecheckJar(impl);
    if (!impl.jarOk) {
        Refresh(impl);
        SetStatus(impl, L"The game file is missing or damaged. Choose your .jar again.", kBad);
        return;
    }
    const std::string exe = FindGameExecutable(impl.installRoot);
    if (exe.empty()) {
        SetStatus(impl, L"soul_of_darkness.exe is missing from this folder; the install "
                        L"looks incomplete. Re-extract the download.",
                  kBad);
        return;
    }

    // Window size or fullscreen; either way the game's OPTIONS screen (and
    // F11) can change it for the session. Settings and the save live where
    // the game always keeps them (%LOCALAPPDATA%\SoulOfDarkness101).
    std::wstring command = L"\"" + Widen(exe) + L"\" --jar \"" + Widen(impl.jarPath) + L"\"";
    command += L" --scale " + std::to_wstring(impl.config.scale);
    command += impl.config.fullscreen ? L" --fullscreen" : L" --windowed";

    STARTUPINFOW startup{};
    startup.cb = sizeof(startup);
    PROCESS_INFORMATION process{};
    std::vector<wchar_t> mutableCommand(command.begin(), command.end());
    mutableCommand.push_back(L'\0');

    const std::wstring workingDirectory = fs::path(Widen(exe)).parent_path().wstring();
    if (!CreateProcessW(nullptr, mutableCommand.data(), nullptr, nullptr, FALSE, 0, nullptr,
                        workingDirectory.c_str(), &startup, &process)) {
        SetStatus(impl, L"Couldn't start the game.", kBad);
        return;
    }
    CloseHandle(process.hThread);

    // Out of the way while the game runs, back when it exits -- and the
    // wait pumps messages so the hidden window still answers the system
    // rather than going "not responding".
    ShowWindow(impl.hwnd, SW_HIDE);
    for (;;) {
        const DWORD result =
            MsgWaitForMultipleObjects(1, &process.hProcess, FALSE, INFINITE, QS_ALLINPUT);
        if (result == WAIT_OBJECT_0) break;
        MSG message;
        while (PeekMessageW(&message, nullptr, 0, 0, PM_REMOVE)) {
            TranslateMessage(&message);
            DispatchMessageW(&message);
        }
    }
    DWORD exitCode = 0;
    GetExitCodeProcess(process.hProcess, &exitCode);
    CloseHandle(process.hProcess);

    ShowWindow(impl.hwnd, SW_SHOW);
    SetForegroundWindow(impl.hwnd);
    RecheckJar(impl);
    Refresh(impl);
    if (exitCode != 0) {
        SetStatus(impl, L"The game closed with an error (exit code " + std::to_wstring(exitCode) + L").", kBad);
    } else {
        SetStatus(impl, L"Welcome back.", kMuted);
    }
}

// ---------------------------------------------------------------------
// Painting
// ---------------------------------------------------------------------

void FillRectColor(HDC dc, const RECT& rect, COLORREF color) {
    HBRUSH brush = CreateSolidBrush(color);
    FillRect(dc, &rect, brush);
    DeleteObject(brush);
}

void DrawTextLine(HDC dc, HFONT font, COLORREF color, int x, int y, int width, int height,
                  const std::wstring& text, UINT format = DT_LEFT | DT_SINGLELINE | DT_VCENTER) {
    HGDIOBJ previous = SelectObject(dc, font);
    SetTextColor(dc, color);
    RECT rect{x, y, x + width, y + height};
    DrawTextW(dc, text.c_str(), -1, &rect, format | DT_NOPREFIX);
    SelectObject(dc, previous);
}

void PaintButton(Impl& impl, const Button& button, DRAWITEMSTRUCT* item) {
    const bool disabled = (item->itemState & ODS_DISABLED) != 0;
    const bool pressed = (item->itemState & ODS_SELECTED) != 0;
    const bool focused = (item->itemState & ODS_FOCUS) != 0;

    COLORREF face = kPanelHigh;
    COLORREF border = RGB(56, 50, 88);
    COLORREF label = kText;
    if (button.style.primary) {
        face = pressed ? RGB(184, 100, 30) : (button.hovered ? RGB(250, 166, 82) : kFire);
        border = RGB(255, 200, 140);
        label = RGB(30, 14, 4);
    } else if (button.style.selected) {
        face = pressed ? RGB(58, 50, 104) : RGB(48, 40, 88);
        border = kCaption;
        label = kGood;
    } else if (pressed) {
        face = RGB(40, 36, 66);
    } else if (button.hovered) {
        face = RGB(40, 36, 66);
        border = kRule;
    }
    if (disabled) {
        face = RGB(24, 22, 34);
        border = RGB(42, 40, 58);
        label = RGB(92, 92, 112);
    }

    HBRUSH faceBrush = CreateSolidBrush(face);
    HPEN borderPen = CreatePen(PS_SOLID, impl.S(1), border);
    HGDIOBJ oldBrush = SelectObject(item->hDC, faceBrush);
    HGDIOBJ oldPen = SelectObject(item->hDC, borderPen);
    const int radius = impl.S(6);
    RoundRect(item->hDC, item->rcItem.left, item->rcItem.top, item->rcItem.right,
              item->rcItem.bottom, radius, radius);
    SelectObject(item->hDC, oldBrush);
    SelectObject(item->hDC, oldPen);
    DeleteObject(faceBrush);
    DeleteObject(borderPen);

    wchar_t caption[64] = {};
    GetWindowTextW(item->hwndItem, caption, 63);
    HFONT font = button.style.primary ? impl.playFont : impl.uiFont;
    HGDIOBJ oldFont = SelectObject(item->hDC, font);
    SetBkMode(item->hDC, TRANSPARENT);
    SetTextColor(item->hDC, label);
    RECT textRect = item->rcItem;
    DrawTextW(item->hDC, caption, -1, &textRect,
              DT_CENTER | DT_SINGLELINE | DT_VCENTER | DT_NOPREFIX);
    SelectObject(item->hDC, oldFont);

    if (focused && !disabled) {
        RECT focusRect = item->rcItem;
        InflateRect(&focusRect, -impl.S(4), -impl.S(4));
        DrawFocusRect(item->hDC, &focusRect);
    }
}

void PaintWindow(Impl& impl, HDC target) {
    RECT client{};
    GetClientRect(impl.hwnd, &client);
    const int width = client.right;
    const int height = client.bottom;

    // Composed off-screen and blitted once: the header is a large stretched
    // bitmap, and painting it straight to the window flickers visibly every
    // time a button repaints.
    HDC dc = CreateCompatibleDC(target);
    HBITMAP surface = CreateCompatibleBitmap(target, width, height);
    HGDIOBJ oldBitmap = SelectObject(dc, surface);

    const int headerHeight = impl.panelTop;

    RECT headerRect{0, 0, width, headerHeight};
    FillRectColor(dc, headerRect, kBackground);
    if (impl.banner.valid()) {
        BITMAPINFO info{};
        info.bmiHeader.biSize = sizeof(info.bmiHeader);
        info.bmiHeader.biWidth = impl.banner.width;
        info.bmiHeader.biHeight = -impl.banner.height;  // top-down
        info.bmiHeader.biPlanes = 1;
        info.bmiHeader.biBitCount = 32;
        info.bmiHeader.biCompression = BI_RGB;

        const int drawnHeight = MulDiv(impl.banner.height, width, impl.banner.width);
        SetStretchBltMode(dc, HALFTONE);
        SetBrushOrgEx(dc, 0, 0, nullptr);
        IntersectClipRect(dc, 0, 0, width, headerHeight);
        StretchDIBits(dc, 0, 0, width, drawnHeight, 0, 0, impl.banner.width, impl.banner.height,
                      impl.banner.pixels.data(), &info, DIB_RGB_COLORS, SRCCOPY);
        SelectClipRgn(dc, nullptr);
    }

    RECT panelRect{0, headerHeight, width, height};
    FillRectColor(dc, panelRect, kPanel);

    // Fade the top of the panel out of the artwork's own colour instead of
    // cutting to full contrast.
    const int fade = impl.S(48);
    for (int i = 0; i < fade && headerHeight + i < height; ++i) {
        const COLORREF row = Blend(impl.seamColor, kPanel, i * 255 / fade);
        RECT rowRect{0, headerHeight + i, width, headerHeight + i + 1};
        FillRectColor(dc, rowRect, row);
    }

    RECT ruleRect{0, headerHeight, width, headerHeight + impl.S(1)};
    FillRectColor(dc, ruleRect, kRule);

    SetBkMode(dc, TRANSPARENT);
    const int margin = impl.S(kMargin);
    const int valueWidth = impl.ButtonX() - margin - impl.S(20);
    const int valueChars = (std::max)(24, valueWidth / (std::max)(1, impl.S(7)));

    // Row 1 -- the game file.
    DrawTextLine(dc, impl.captionFont, kCaption, margin, impl.P(kRow1Caption), impl.S(400),
                 impl.S(18), impl.jarOk && !impl.jarVerified ? L"GAME FILE   ·   UNVERIFIED BUILD"
                                                             : L"GAME FILE");
    if (impl.jarOk) {
        DrawTextLine(dc, impl.valueFont, kText, margin, impl.P(kRow1Value), valueWidth,
                     impl.S(22), ElidePath(Widen(impl.jarPath), valueChars));
    } else {
        DrawTextLine(dc, impl.valueFont, kBad, margin, impl.P(kRow1Value), valueWidth, impl.S(22),
                     L"Not set — choose your Soul of Darkness .jar to begin.");
    }

    // Row 2 -- the window size.
    DrawTextLine(dc, impl.captionFont, kCaption, margin, impl.P(kRow2Caption), impl.S(400),
                 impl.S(18), L"DISPLAY   ·   WINDOWED SIZE OR FULLSCREEN");

    // The status line, and the version in the corner.
    if (!impl.status.empty()) {
        DrawTextLine(dc, impl.uiFont, impl.statusColor, margin, impl.P(kStatusY),
                     width - 2 * margin, impl.S(38), impl.status,
                     DT_LEFT | DT_WORDBREAK | DT_EDITCONTROL);
    }
    DrawTextLine(dc, impl.captionFont, RGB(84, 82, 120), margin, impl.P(kVersionY),
                 width - 2 * margin, impl.S(18), L"Soul of Darkness Remastered " SOD_LAUNCHER_VERSION_W,
                 DT_LEFT | DT_SINGLELINE | DT_VCENTER);

    // The update note sits beside the version, left of the button. Only the
    // Available and Installing states have anything to say here; a
    // successful "you are up to date" is not news and stays quiet.
    if (!impl.updateNote.empty()) {
        const int noteWidth = impl.ButtonX() - margin - impl.S(20);
        const COLORREF noteColor = impl.updateState == UpdateState::Failed ? kMuted : kFire;
        DrawTextLine(dc, impl.uiFont, noteColor, margin, impl.P(kUpdateY), noteWidth,
                     impl.S(28), impl.updateNote, DT_LEFT | DT_SINGLELINE | DT_VCENTER);
    }

    BitBlt(target, 0, 0, width, height, dc, 0, 0, SRCCOPY);
    SelectObject(dc, oldBitmap);
    DeleteObject(surface);
    DeleteDC(dc);
}

// ---------------------------------------------------------------------

constexpr DWORD kWindowStyle = WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU | WS_MINIMIZEBOX;

// Settles the window's size, and with it impl.clientWidth/panelTop: the
// preferred width is kClientWidth and the header height follows the
// artwork's proportions, shrinking both together if the screen is too
// short, cropping the artwork only below kMinClientWidth. Centred in the
// work area so the bottom rows never end up under the taskbar.
void SizeToArtwork(Impl& impl, DWORD style) {
    RECT work{0, 0, 0, 0};
    if (!SystemParametersInfoW(SPI_GETWORKAREA, 0, &work, 0)) {
        work.right = GetSystemMetrics(SM_CXSCREEN);
        work.bottom = GetSystemMetrics(SM_CYSCREEN);
    }

    RECT frame{0, 0, 0, 0};
    AdjustWindowRect(&frame, style, FALSE);
    const int frameWidth = (frame.right - frame.left);
    const int frameHeight = (frame.bottom - frame.top);

    const int panelHeight = impl.S(kPanelHeight);
    const int maxClientWidth = (work.right - work.left) - frameWidth;
    const int maxClientHeight = (work.bottom - work.top) - frameHeight;

    int width = (std::min)(impl.S(kClientWidth), (std::max)(impl.S(kMinClientWidth), maxClientWidth));
    int header = HeaderHeightFor(impl.banner, width, impl.S(kFallbackHeaderHeight));

    if (header + panelHeight > maxClientHeight) {
        const int allowedHeader = (std::max)(impl.S(80), maxClientHeight - panelHeight);
        const int narrower = WidthForHeaderHeight(impl.banner, allowedHeader);
        if (narrower >= impl.S(kMinClientWidth) && narrower < width) {
            width = narrower;
            header = HeaderHeightFor(impl.banner, width, impl.S(kFallbackHeaderHeight));
        } else {
            header = allowedHeader;
        }
    }

    impl.clientWidth = width;
    impl.panelTop = header;

    RECT desired{0, 0, width, header + panelHeight};
    AdjustWindowRect(&desired, style, FALSE);
    const int windowWidth = desired.right - desired.left;
    const int windowHeight = desired.bottom - desired.top;
    const int workWidth = static_cast<int>(work.right - work.left);
    const int workHeight = static_cast<int>(work.bottom - work.top);
    const int x = static_cast<int>(work.left) + (std::max)(0, (workWidth - windowWidth) / 2);
    const int y = static_cast<int>(work.top) + (std::max)(0, (workHeight - windowHeight) / 2);
    SetWindowPos(impl.hwnd, nullptr, x, y, windowWidth, windowHeight, SWP_NOZORDER);
}

// `x` and `y` are device pixels (y already panel-relative via Impl::P);
// `w` and `h` are logical.
HWND MakeButton(Impl& impl, Button& button, const wchar_t* caption, int id, int x, int y, int w,
                int h, ButtonStyle style) {
    button.style = style;
    button.hwnd = CreateWindowExW(0, L"BUTTON", caption,
                                  WS_CHILD | WS_VISIBLE | WS_TABSTOP | BS_OWNERDRAW, x, y,
                                  impl.S(w), impl.S(h), impl.hwnd,
                                  reinterpret_cast<HMENU>(static_cast<INT_PTR>(id)),
                                  GetModuleHandleW(nullptr), nullptr);
    SetWindowSubclass(button.hwnd, ButtonSubclass, static_cast<UINT_PTR>(id),
                      reinterpret_cast<DWORD_PTR>(&button.hovered));
    return button.hwnd;
}

void CreateControls(Impl& impl) {
    const int buttonX = impl.ButtonX();
    MakeButton(impl, impl.chooseData, L"Choose file…", IDC_CHOOSE_DATA, buttonX,
               impl.P(kRow1Button), kButtonWidth, kButtonHeight, ButtonStyle{});
    MakeButton(impl, impl.play, L"Play", IDC_PLAY, buttonX, impl.P(kPlayY), kButtonWidth,
               kPlayHeight, ButtonStyle{true, false});
    MakeButton(impl, impl.update, L"Update", IDC_UPDATE, buttonX, impl.P(kUpdateY),
               kButtonWidth, kButtonHeight, ButtonStyle{});
    ShowWindow(impl.update.hwnd, SW_HIDE);  // only shown when one exists
    for (int i = 0; i < kDisplayButtons; ++i) {
        const std::wstring caption =
            i == kScaleCount ? std::wstring(L"Fullscreen") : std::to_wstring(kScaleChoices[i]) + L"×";
        MakeButton(impl, impl.display[i], caption.c_str(), IDC_SCALE_FIRST + i,
                   impl.S(kMargin + i * (kScaleWidth + 8)), impl.P(kScaleY),
                   i == kScaleCount ? kFullscreenWidth : kScaleWidth, kScaleHeight, ButtonStyle{});
    }
}

LRESULT CALLBACK WndProc(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam) {
    if (message == WM_NCCREATE) {
        auto* create = reinterpret_cast<CREATESTRUCTW*>(lParam);
        SetWindowLongPtrW(hwnd, GWLP_USERDATA, reinterpret_cast<LONG_PTR>(create->lpCreateParams));
        return DefWindowProcW(hwnd, message, wParam, lParam);
    }
    Impl* impl = Self(hwnd);
    if (!impl) return DefWindowProcW(hwnd, message, wParam, lParam);

    switch (message) {
        case WM_ERASEBKGND:
            return 1;  // PaintWindow covers every pixel; erasing first only flickers
        case WM_PAINT: {
            PAINTSTRUCT paint{};
            HDC dc = BeginPaint(hwnd, &paint);
            PaintWindow(*impl, dc);
            EndPaint(hwnd, &paint);
            return 0;
        }
        case WM_DRAWITEM: {
            auto* item = reinterpret_cast<DRAWITEMSTRUCT*>(lParam);
            const Button* button = nullptr;
            const int id = static_cast<int>(wParam);
            if (id == IDC_CHOOSE_DATA) button = &impl->chooseData;
            else if (id == IDC_PLAY) button = &impl->play;
            else if (id == IDC_UPDATE) button = &impl->update;
            else if (id >= IDC_SCALE_FIRST && id < IDC_SCALE_FIRST + kDisplayButtons)
                button = &impl->display[id - IDC_SCALE_FIRST];
            if (!button) return FALSE;
            PaintButton(*impl, *button, item);
            return TRUE;
        }
        case WM_COMMAND: {
            const int id = LOWORD(wParam);
            if (HIWORD(wParam) != BN_CLICKED) break;
            if (impl->installing) return 0;  // nothing is safe to start mid-swap
            if (id == IDC_CHOOSE_DATA) {
                ChooseGameData(*impl);
            } else if (id == IDC_PLAY) {
                Play(*impl);
            } else if (id == IDC_UPDATE) {
                impl->installing = true;
                impl->updateState = UpdateState::Installing;
                impl->updateNote = L"Starting…";
                Refresh(*impl);
                SetStatus(*impl, L"Updating to " + Widen(impl->availableRelease.tag) +
                                     L". Please don't close this window.",
                          kCaption);
                StartInstall(hwnd, impl->installRoot, impl->availableRelease);
            } else if (id >= IDC_SCALE_FIRST && id < IDC_SCALE_FIRST + kDisplayButtons) {
                if (id == IDC_SCALE_FIRST + kScaleCount) {
                    impl->config.fullscreen = true;
                } else {
                    impl->config.fullscreen = false;
                    impl->config.scale = kScaleChoices[id - IDC_SCALE_FIRST];
                }
                Save(*impl);
                Refresh(*impl);
            }
            return 0;
        }

        // --- the update worker's three messages -------------------------
        //
        // Each arrives with a heap-allocated payload this handler owns and
        // frees. The worker is detached and never touches `Impl`, so a
        // check still in flight when the window closes simply fails to post
        // and cleans up after itself.
        case WM_APP_UPDATE_CHECKED: {
            std::unique_ptr<UpdateCheckResult> result(reinterpret_cast<UpdateCheckResult*>(lParam));
            if (!result->ok) {
                // Not being able to reach GitHub is not an error worth
                // shouting about -- the launcher's job is to start a game.
                impl->updateState = UpdateState::Failed;
                impl->updateNote.clear();
                Refresh(*impl);
                return 0;
            }
            const int comparison =
                sod::launcher::CompareVersions(SOD_LAUNCHER_VERSION, result->release.version);
            if (comparison < 0) {
                impl->updateState = UpdateState::Available;
                impl->availableRelease = result->release;
                impl->updateNote = L"Version " + Widen(result->release.version) + L" is available";
            } else {
                impl->updateState = UpdateState::UpToDate;
                impl->updateNote.clear();
            }
            Refresh(*impl);
            return 0;
        }
        case WM_APP_UPDATE_PROGRESS: {
            std::unique_ptr<UpdateProgressMessage> progressMessage(
                reinterpret_cast<UpdateProgressMessage*>(lParam));
            const sod::launcher::UpdateProgress& progress = progressMessage->progress;
            if (progress.stage == sod::launcher::UpdateStage::Downloading && progress.bytesTotal > 0) {
                impl->updateNote = L"Downloading " + FormatSize(progress.bytesSoFar) + L" of " +
                                   FormatSize(progress.bytesTotal);
            } else if (!progress.message.empty()) {
                impl->updateNote = Widen(progress.message);
            }
            InvalidateRect(hwnd, nullptr, FALSE);
            return 0;
        }
        case WM_APP_UPDATE_DONE: {
            std::unique_ptr<UpdateDoneMessage> done(reinterpret_cast<UpdateDoneMessage*>(lParam));
            impl->installing = false;
            if (!done->ok) {
                impl->updateState = UpdateState::Failed;
                impl->updateNote.clear();
                Refresh(*impl);
                SetStatus(*impl, L"The update failed: " + Widen(done->error), kBad);
                return 0;
            }
            // This executable has just been renamed aside and replaced, so
            // the new one has to take over from here.
            if (sod::launcher::RelaunchLauncher(impl->installRoot)) {
                DestroyWindow(hwnd);
            } else {
                impl->updateState = UpdateState::UpToDate;
                impl->updateNote.clear();
                Refresh(*impl);
                SetStatus(*impl,
                          L"Updated to " + Widen(done->tag) +
                              L". Close and reopen Soul of Darkness to use it.",
                          kGood);
            }
            return 0;
        }

        case WM_CLOSE:
            if (impl->installing) {
                // Half-replaced installs are how people end up with a
                // folder that no longer starts.
                SetStatus(*impl, L"Please wait — files are being replaced right now.", kBad);
                return 0;
            }
            DestroyWindow(hwnd);
            return 0;
        case WM_DESTROY:
            PostQuitMessage(0);
            return 0;
        default:
            break;
    }
    return DefWindowProcW(hwnd, message, wParam, lParam);
}

}  // namespace

int WINAPI wWinMain(HINSTANCE instance, HINSTANCE, PWSTR, int showCommand) {
    // APARTMENTTHREADED because IFileOpenDialog requires an STA (and WIC,
    // which decodes the banner, needs COM too).
    const HRESULT comResult = CoInitializeEx(nullptr, COINIT_APARTMENTTHREADED);
    INITCOMMONCONTROLSEX controls{sizeof(controls), ICC_STANDARD_CLASSES};
    InitCommonControlsEx(&controls);

    Impl impl;
    impl.installRoot = ExecutableDirectory();
    impl.configPath = (fs::u8path(impl.installRoot) / LauncherConfig::kFileName).u8string();
    impl.banner = sod::launcher::LoadEmbeddedBanner();
    sod::launcher::LoadLauncherConfig(impl.configPath, impl.config);
    RecheckJar(impl);

    WNDCLASSEXW windowClass{};
    windowClass.cbSize = sizeof(windowClass);
    windowClass.lpfnWndProc = WndProc;
    windowClass.hInstance = instance;
    windowClass.hCursor = LoadCursorW(nullptr, IDC_ARROW);
    windowClass.hbrBackground = nullptr;  // WM_ERASEBKGND is handled
    windowClass.lpszClassName = L"SoulOfDarknessLauncherWindow";
    windowClass.hIcon = LoadIconW(instance, MAKEINTRESOURCEW(IDI_APPICON));
    windowClass.hIconSm = windowClass.hIcon;
    RegisterClassExW(&windowClass);

    // Created hidden at a nominal size first, because the DPI that decides
    // the real size is only knowable once there is a window to ask about.
    impl.hwnd = CreateWindowExW(0, windowClass.lpszClassName, L"Soul of Darkness Remastered",
                                kWindowStyle, CW_USEDEFAULT, CW_USEDEFAULT, kClientWidth,
                                kClientWidth, nullptr, nullptr, instance, &impl);
    if (!impl.hwnd) {
        if (SUCCEEDED(comResult)) CoUninitialize();
        return 1;
    }

    impl.dpi = QueryDpi(impl.hwnd);
    impl.uiFont = MakeFont(impl.dpi, 10, FW_NORMAL);
    impl.captionFont = MakeFont(impl.dpi, 8, FW_SEMIBOLD);
    impl.valueFont = MakeFont(impl.dpi, 10, FW_NORMAL);
    impl.playFont = MakeFont(impl.dpi, 13, FW_SEMIBOLD);

    SizeToArtwork(impl, kWindowStyle);
    impl.seamColor = SeamColorFor(impl.banner);

    CreateControls(impl);
    Refresh(impl);

    const bool autoInstalled = !impl.jarOk && AutoInstallJar(impl);

    // Clears out whatever the last update left behind, then asks GitHub
    // whether there is a newer build -- on a worker thread, because a slow
    // or unreachable network must never delay the window appearing.
    if (sod::launcher::UpdatesEnabledForThisBuild(SOD_LAUNCHER_VERSION)) {
        sod::launcher::CleanUpPreviousUpdate(impl.installRoot);
        impl.updateState = UpdateState::Checking;
        StartUpdateCheck(impl.hwnd);
    } else {
        impl.updateState = UpdateState::Disabled;
    }

    if (autoInstalled) {
        // InstallJarFrom's own status line says what it found -- keep it.
    } else if (impl.jarOk) {
        SetStatus(impl, L"Ready to play.", kMuted);
    } else {
        SetStatus(impl,
                  L"Soul of Darkness Remastered doesn't include the game itself. Point it at "
                  L"your own copy of the 1.0.1 (240x320) .jar to get started.",
                  kMuted);
    }

    ShowWindow(impl.hwnd, showCommand);
    UpdateWindow(impl.hwnd);

    MSG message;
    while (GetMessageW(&message, nullptr, 0, 0) > 0) {
        if (!IsDialogMessageW(impl.hwnd, &message)) {
            TranslateMessage(&message);
            DispatchMessageW(&message);
        }
    }

    if (impl.uiFont) DeleteObject(impl.uiFont);
    if (impl.captionFont) DeleteObject(impl.captionFont);
    if (impl.valueFont) DeleteObject(impl.valueFont);
    if (impl.playFont) DeleteObject(impl.playFont);
    if (SUCCEEDED(comResult)) CoUninitialize();
    return 0;
}
