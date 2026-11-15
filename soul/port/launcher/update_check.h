#pragma once

// Finding out whether a newer build exists. Ported verbatim from
// tes-travels-decomp's Dawnstar launcher (itself from shadowkey-decomp):
// nothing in the HTTP/JSON/version-compare machinery is game-specific. The
// tag-prefix filter is kept even though this repository publishes one game:
// releases are tagged "vX.Y.Z", and the prefix "v" is what is stripped to
// get the version.
//
// Two halves, deliberately separated: the parsing and comparing are pure
// functions over strings, so the smoke test can exercise every branch
// without a network; only FetchLatestRelease() talks to GitHub.
//
// **Not `/releases/latest`.** That endpoint excludes pre-releases (this
// project's releases are pre-releases while the major version is 0). The
// list endpoint, paged, finds them.
//
// The request is unauthenticated, so it only sees the releases of a public
// repository: for a private one GitHub answers 404 and the launcher simply
// offers no update.

#include <string>

namespace sod {
namespace launcher {

struct ReleaseInfo {
    std::string tag;          // "v0.1.0"
    std::string version;      // "0.1.0" -- the tag with the "v" prefix removed
    std::string assetName;    // "SoulOfDarknessRemastered-v0.1.0-win64.zip"
    std::string downloadUrl;  // the asset's browser_download_url
    bool prerelease = false;

    bool valid() const { return !version.empty() && !downloadUrl.empty(); }
};

// Compares two version strings the way a person would: numerically
// component by component, so 0.15.0 beats 0.9.0 (which a string compare
// gets backwards). A `-suffix` marks a pre-release and sorts *before* the
// same numbers without one, so 1.0.0 beats 1.0.0-beta1.
//
// Returns <0 if `left` is older, 0 if equal, >0 if newer.
int CompareVersions(const std::string& left, const std::string& right);

// Pulls the fields we need out of the JSON array the releases endpoint
// returns, taking the first release object whose "tag_name" starts with
// `tagPrefix` (here "v") and has a downloadable .zip asset.
// `out.version` has `tagPrefix` stripped from the front of the tag.
// Returns false if no release in the payload matches.
bool ParseReleaseList(const std::string& json, const std::string& tagPrefix, ReleaseInfo& out);

// The GET plus the tag-prefix filter above. Returns false and fills
// `error` with something a user can read on any failure -- no network,
// DNS, a rate-limit, a non-200, or no matching release. Blocking, so call
// it off the UI thread.
bool FetchLatestRelease(const std::string& owner, const std::string& repo,
                        const std::string& tagPrefix, ReleaseInfo& out, std::string& error);

// Downloads `url` to `destPath`. `onProgress` may be null; when the server
// sends a content-length it is called with (bytesSoFar, totalBytes),
// otherwise totalBytes is 0. Returns false and fills `error` on failure,
// leaving no partial file behind.
bool DownloadFile(const std::string& url, const std::string& destPath,
                  void (*onProgress)(unsigned long long, unsigned long long, void*),
                  void* progressContext, std::string& error);

}  // namespace launcher
}  // namespace sod
