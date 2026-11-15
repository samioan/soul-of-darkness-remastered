#pragma once

// `launcher.cfg` -- where the launcher remembers what the user picked.
// Ported from tes-travels-decomp's Dawnstar launcher (itself from
// shadowkey-decomp); same plain `key=value` file:
//
//     jar=game/SoulOfDarkness.jar
//     scale=3
//     fullscreen=0
//
// Paths are stored relative to the install root when they live inside it
// (which, after the launcher copies the JAR in, they always do), so the whole
// folder can be moved or renamed without breaking the install. Unknown keys
// are ignored; a missing file yields the defaults, which is the first-run
// state.

#include <string>

namespace sod {
namespace launcher {

struct LauncherConfig {
    // The installed copy of the user's Soul of Darkness JAR, passed to
    // soul_of_darkness.exe as --jar. Empty until the user has picked one.
    std::string jar;
    // Window size as a multiple of the original 240x320 pixels (--scale).
    int scale = 3;
    // Fullscreen instead of a window (--fullscreen / --windowed); the game's
    // OPTIONS screen and F11 can still change it.
    bool fullscreen = false;

    static constexpr int kMinScale = 1;
    static constexpr int kMaxScale = 8;

    static constexpr const char* kFileName = "launcher.cfg";
};

// Both return false if the file could not be read/written at all. A file
// that exists but is empty or entirely unrecognised reads back as defaults.
bool LoadLauncherConfig(const std::string& path, LauncherConfig& out);
bool SaveLauncherConfig(const std::string& path, const LauncherConfig& config);

// Joins `root` and `relative` unless `relative` is already absolute, in
// which case it is returned unchanged.
std::string ResolveAgainst(const std::string& root, const std::string& relative);

// The inverse, used before saving: makes `path` relative to `root` when it
// is inside it, and returns it unchanged when it is not.
std::string RelativeToIfInside(const std::string& root, const std::string& path);

}  // namespace launcher
}  // namespace sod
