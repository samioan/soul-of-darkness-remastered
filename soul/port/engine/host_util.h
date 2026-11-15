// Small platform-independent helpers for the desktop host: SHA-256 and reading entries out of a JAR.
#pragma once
#include <cstdint>
#include <string>
#include <vector>

namespace sodhost {

std::string sha256Hex(const std::vector<std::uint8_t>& data);

struct ZipEntry {
    std::string name;
    std::vector<std::uint8_t> data;
};

// Reads every stored/deflated file entry from a ZIP/JAR image. Returns false on a malformed archive.
bool readZip(const std::vector<std::uint8_t>& archive, std::vector<ZipEntry>& out, std::string& error);

// Identity of the JAR this executable was built for. The two supported builds have different code,
// data packs and saves, so each executable accepts only its own.
#ifdef SOD_FULL
// Gameloft Soul of Darkness 1.0.1, 240x320 (HTC P3450 build): the full-featured game.
constexpr const char* kExpectedJarSha256 = "3da9ea7fe8dfaa2c090e8b3714eaed15127f51dc8b67ed9ef9fed51172e4bea4";
constexpr const char* kExpectedJarName = "Soul of Darkness 1.0.1 for 240x320 (HTC P3450 build, SoulOfDarkness_HTC_P3450_EN_IGPL_EU_101)";
constexpr int kPackCount = 24;
constexpr const wchar_t* kUserFolder = L"SoulOfDarkness101";
#else
// Gameloft Soul of Darkness 1.3.7, 240x320: the cut-down build.
constexpr const char* kExpectedJarSha256 = "9d839e59d6cb2f80575736fbf867babe0da852312652490dd9025d0a0654dd6b";
constexpr const char* kExpectedJarName = "Soul of Darkness 1.3.7 for 240x320 (cut-down build)";
constexpr int kPackCount = 19;
constexpr const wchar_t* kUserFolder = L"SoulOfDarkness137";
#endif

// Files the engine opens (pack "/1".."/19", images). Used to validate a data directory.
const std::vector<std::string>& requiredDataFiles();

} // namespace sodhost
