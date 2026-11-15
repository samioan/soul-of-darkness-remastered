#pragma once

// The launcher's zip reader: unpacking an update `.zip` downloaded from a
// GitHub release. (The player's JAR is never unpacked -- the game reads it in
// place; see jar_install.h.)
//
// A real reader rather than a shell-out, and a small one: the two things it
// must handle are the only two `Compress-Archive`/`zipfile` ever emit --
// stored (method 0) and deflate (method 8) -- and the inflater is the game's
// own (soul/port/engine/inflate.cpp). Shelling out to PowerShell's
// Expand-Archive or tar.exe would mean a visible console window and a
// dependency on execution policy. Ported from tes-travels-decomp's Dawnstar
// launcher (itself from shadowkey-decomp), with puff replaced by that
// inflater; nothing in a zip reader is specific to any one game.
//
// What it deliberately refuses:
//
//   * **Paths that escape the destination.** An entry named `..\..\evil.exe`
//     is how a zip overwrites something it was never given access to. Every
//     entry's resolved path is required to stay under the destination.
//   * **Absolute paths and drive letters**, for the same reason.
//   * **Encrypted entries**, which it cannot read and must not half-write.
//
// It does not implement zip64, so it handles archives up to 4 GB -- a
// release `.zip` is a few MB.

#include <string>
#include <vector>

namespace sod {
namespace launcher {

struct ZipEntry {
    std::string path;              // as stored, normalised to forward slashes
    unsigned long long size = 0;   // uncompressed
    bool isDirectory = false;
};

// Lists what an archive contains without writing anything.
bool ReadZipIndex(const std::string& zipPath, std::vector<ZipEntry>& entries, std::string& error);

// Extracts every entry under `destDir`, creating directories as needed and
// overwriting existing files. On failure returns false with `error` set;
// partial output may exist, which is why both callers extract to a staging
// directory rather than straight into a live install.
bool ExtractZip(const std::string& zipPath, const std::string& destDir, std::string& error);

// Exposed for the smoke test: true if `relative` stays inside a directory
// once appended to it. Rejects absolute paths, drive letters and any `..`
// component.
bool IsSafeRelativePath(const std::string& relative);

}  // namespace launcher
}  // namespace sod
