// Small platform-independent helpers shared by the game host and the launcher: SHA-256, the JAR's
// central directory, and the identity of the supported JAR. Nothing here depends on the engine.
#pragma once
#include <cstddef>
#include <cstdint>
#include <string>
#include <vector>

namespace sodhost {

std::string sha256Hex(const std::vector<std::uint8_t>& data);

struct ZipEntry {
    std::string name;
    std::vector<std::uint8_t> data;
};

// One file in a ZIP/JAR's central directory, with where its (possibly compressed) bytes start.
struct ZipDirEntry {
    std::string name;
    unsigned method = 0;            // 0 stored, 8 deflated
    std::size_t compressed = 0, uncompressed = 0, dataStart = 0;
};

// Lists the file entries (not directories) of a ZIP/JAR image, checking every offset against its size.
bool readZipDirectory(const std::vector<std::uint8_t>& archive, std::vector<ZipDirEntry>& out, std::string& error);

// Reads every stored/deflated file entry from a ZIP/JAR image (host_zip.cpp, with inflate.cpp
// inflater). Returns false on a malformed archive.
bool readZip(const std::vector<std::uint8_t>& archive, std::vector<ZipEntry>& out, std::string& error);

// Identity of the JAR this executable was built for: Gameloft Soul of Darkness 1.0.1, 240x320
// (HTC P3450 build).
constexpr const char* kExpectedJarSha256 = "3da9ea7fe8dfaa2c090e8b3714eaed15127f51dc8b67ed9ef9fed51172e4bea4";
constexpr const char* kExpectedJarName = "Soul of Darkness 1.0.1 for 240x320 (HTC P3450 build, SoulOfDarkness_HTC_P3450_EN_IGPL_EU_101)";
constexpr int kPackCount = 24;
constexpr const wchar_t* kUserFolder = L"SoulOfDarkness101";

// Files the engine opens (packs "/1".."/24", images). Used to validate a data directory.
const std::vector<std::string>& requiredDataFiles();

} // namespace sodhost
