#include "launcher/jar_install.h"

#include <filesystem>
#include <fstream>
#include <set>
#include <vector>

#include "engine/host_util.h"

namespace sod {
namespace launcher {

namespace {
namespace fs = std::filesystem;

bool ReadFileBytes(const fs::path& path, std::vector<std::uint8_t>& out) {
    std::ifstream file(path, std::ios::binary | std::ios::ate);
    if (!file) return false;
    const std::streamoff size = file.tellg();
    // The supported JAR is 618 KB; anything past 64 MB is not a phone game.
    if (size <= 0 || size > 64ll * 1024 * 1024) return false;
    out.resize(static_cast<std::size_t>(size));
    file.seekg(0);
    file.read(reinterpret_cast<char*>(out.data()), size);
    return static_cast<bool>(file);
}
}  // namespace

const char* ExpectedJarName() { return sodhost::kExpectedJarName; }

JarCheck CheckJar(const std::string& path) {
    JarCheck check;
    std::vector<std::uint8_t> bytes;
    if (!ReadFileBytes(fs::u8path(path), bytes)) {
        check.error = "That file could not be read.";
        return check;
    }
    std::vector<sodhost::ZipDirEntry> entries;
    std::string zipError;
    if (!sodhost::readZipDirectory(bytes, entries, zipError)) {
        check.error = "That is not a readable JAR (" + zipError + ").";
        return check;
    }
    std::set<std::string> names;
    for (const auto& entry : entries) names.insert(entry.name);
    for (const std::string& required : sodhost::requiredDataFiles()) {
        if (!names.count(required)) {
            check.error = "That JAR is not Soul of Darkness: it has no '" + required + "' resource.";
            return check;
        }
    }
    check.usable = true;
    check.verified = sodhost::sha256Hex(bytes) == sodhost::kExpectedJarSha256;
    return check;
}

bool InstallJar(const std::string& jarPath, const std::string& destPath, std::string& error) {
    std::error_code code;
    const fs::path source = fs::u8path(jarPath), dest = fs::u8path(destPath);
    if (fs::equivalent(source, dest, code) && !code) return true;  // re-picking the installed copy
    code.clear();
    fs::create_directories(dest.parent_path(), code);
    if (code) {
        error = "Could not create " + dest.parent_path().u8string() + ": " + code.message();
        return false;
    }
    // Copied beside the target first, then renamed over it, so a failed copy
    // never leaves a truncated JAR where a working one was.
    const fs::path staging = dest.string() + ".partial";
    fs::copy_file(source, staging, fs::copy_options::overwrite_existing, code);
    if (code) {
        error = "Could not copy the JAR: " + code.message();
        fs::remove(staging, code);
        return false;
    }
    fs::rename(staging, dest, code);
    if (code) {
        error = "Could not install the JAR: " + code.message();
        fs::remove(staging, code);
        return false;
    }
    return true;
}

}  // namespace launcher
}  // namespace sod
