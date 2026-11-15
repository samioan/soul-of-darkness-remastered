#pragma once

// Turning "the .jar the user picked" into a working install.
//
// The game reads its JAR in place (soul_of_darkness.exe --jar), so unlike the
// Dawnstar launcher this one unpacks nothing. It checks the file with the
// game's own rules (engine/host_util.h: the packs and images the engine
// opens, and the SHA-256 of the one supported build) and copies it into the
// install's `game/` folder, so the install keeps working after the original
// download is moved or deleted.

#include <string>

namespace sod {
namespace launcher {

struct JarCheck {
    bool usable = false;    // a readable JAR with every resource the engine opens
    bool verified = false;  // and byte-identical to the supported 1.0.1 build
    std::string error;      // why it is not usable, for the user
};

// Reads and checks `path`. A usable but unverified JAR (another 240x320 build
// with the same layout) is the caller's to accept or refuse.
JarCheck CheckJar(const std::string& path);

// Copies `jarPath` to `destPath`, creating the folder. On failure returns
// false with a user-facing `error`.
bool InstallJar(const std::string& jarPath, const std::string& destPath, std::string& error);

// The supported build, for messages ("Soul of Darkness 1.0.1 for 240x320 ...").
const char* ExpectedJarName();

}  // namespace launcher
}  // namespace sod
