// Headless checks of the launcher's logic: no window, no network, no game data.
//   launcher_smoke <path to launcher/assets/banner.jpg>
#include <windows.h>
#include <objbase.h>

#include <cstdio>
#include <cstring>
#include <filesystem>
#include <fstream>
#include <string>
#include <vector>

#include "engine/host_util.h"
#include "launcher/banner.h"
#include "launcher/jar_install.h"
#include "launcher/launcher_config.h"
#include "launcher/update_check.h"
#include "launcher/updater.h"
#include "launcher/zip_reader.h"

namespace fs = std::filesystem;
using namespace sod::launcher;

static int g_failures = 0;
#define CHECK(cond)                                                          \
    do {                                                                     \
        if (!(cond)) {                                                       \
            std::fprintf(stderr, "FAIL %s:%d: %s\n", __FILE__, __LINE__, #cond); \
            ++g_failures;                                                    \
        }                                                                    \
    } while (0)

// A minimal stored (uncompressed) zip, enough for the directory parsers.
static std::vector<std::uint8_t> MakeZip(const std::vector<std::string>& names) {
    std::vector<std::uint8_t> out, central;
    auto u16 = [](std::vector<std::uint8_t>& v, unsigned x) { v.push_back(x & 255); v.push_back((x >> 8) & 255); };
    auto u32 = [&](std::vector<std::uint8_t>& v, unsigned x) { u16(v, x & 0xffff); u16(v, x >> 16); };
    for (const std::string& name : names) {
        const unsigned offset = static_cast<unsigned>(out.size());
        const std::string body = "x";
        u32(out, 0x04034b50u); u16(out, 20); u16(out, 0); u16(out, 0); u16(out, 0); u16(out, 0);
        u32(out, 0); u32(out, 1); u32(out, 1); u16(out, static_cast<unsigned>(name.size())); u16(out, 0);
        out.insert(out.end(), name.begin(), name.end());
        out.insert(out.end(), body.begin(), body.end());
        u32(central, 0x02014b50u); u16(central, 20); u16(central, 20); u16(central, 0); u16(central, 0);
        u16(central, 0); u16(central, 0); u32(central, 0); u32(central, 1); u32(central, 1);
        u16(central, static_cast<unsigned>(name.size())); u16(central, 0); u16(central, 0); u16(central, 0);
        u16(central, 0); u32(central, 0); u32(central, offset);
        central.insert(central.end(), name.begin(), name.end());
    }
    const unsigned centralAt = static_cast<unsigned>(out.size());
    out.insert(out.end(), central.begin(), central.end());
    u32(out, 0x06054b50u); u16(out, 0); u16(out, 0);
    u16(out, static_cast<unsigned>(names.size())); u16(out, static_cast<unsigned>(names.size()));
    u32(out, static_cast<unsigned>(central.size())); u32(out, centralAt); u16(out, 0);
    return out;
}

static void WriteBytes(const fs::path& path, const std::vector<std::uint8_t>& bytes) {
    std::ofstream file(path, std::ios::binary | std::ios::trunc);
    file.write(reinterpret_cast<const char*>(bytes.data()), static_cast<std::streamsize>(bytes.size()));
}

int main(int argc, char** argv) {
    // Versions: numeric per component, a -suffix before the release.
    CHECK(CompareVersions("0.9.0", "0.15.0") < 0);
    CHECK(CompareVersions("1.0.0", "1.0.0-beta1") > 0);
    CHECK(CompareVersions("0.1.0", "0.1.0") == 0);
    CHECK(!UpdatesEnabledForThisBuild("0.1.0-dev"));
    CHECK(UpdatesEnabledForThisBuild("0.1.0"));

    // Release list: the first "v" tag with a .zip asset; the prefix is stripped.
    const std::string json =
        R"([{"tag_name":"v0.2.0","prerelease":true,"assets":[{"name":"notes.txt","browser_download_url":"https://example.invalid/notes.txt"}]},)"
        R"({"tag_name":"v0.1.1","prerelease":true,"assets":[{"name":"SoulOfDarknessRemastered-v0.1.1-win64.zip","browser_download_url":"https://example.invalid/SoulOfDarknessRemastered-v0.1.1-win64.zip"}]}])";
    ReleaseInfo release;
    CHECK(ParseReleaseList(json, "v", release));
    CHECK(release.tag == "v0.1.1");
    CHECK(release.version == "0.1.1");
    CHECK(release.prerelease);
    CHECK(!ParseReleaseList("[]", "v", release));

    // Package shape: launcher at the top, game in bin/.
    CHECK(LooksLikeSoulOfDarknessPackage({"SoulOfDarkness.exe", "bin/soul_of_darkness.exe", "README.txt"}));
    CHECK(!LooksLikeSoulOfDarknessPackage({"SoulOfDarkness.exe", "soul_of_darkness.exe"}));
    CHECK(!LooksLikeSoulOfDarknessPackage({"bin/soul_of_darkness.exe"}));

    // Zip paths that would escape the install are refused.
    CHECK(IsSafeRelativePath("bin/soul_of_darkness.exe"));
    CHECK(!IsSafeRelativePath("../evil.exe"));
    CHECK(!IsSafeRelativePath("bin/../../evil.exe"));
    CHECK(!IsSafeRelativePath("C:/Windows/evil.exe"));
    CHECK(!IsSafeRelativePath("/evil.exe"));

    const fs::path scratch = fs::temp_directory_path() / ("sod_launcher_smoke_" + std::to_string(GetCurrentProcessId()));
    fs::create_directories(scratch);

    // Config round trip, with the install-relative JAR path.
    {
        LauncherConfig config;
        config.jar = RelativeToIfInside(scratch.u8string(), (scratch / "game" / "SoulOfDarkness.jar").u8string());
        CHECK(config.jar == (fs::path("game") / "SoulOfDarkness.jar").u8string());
        config.scale = 2;
        config.fullscreen = true;
        const std::string path = (scratch / "launcher.cfg").u8string();
        CHECK(SaveLauncherConfig(path, config));
        LauncherConfig back;
        CHECK(LoadLauncherConfig(path, back));
        CHECK(back.scale == 2 && back.fullscreen);
        CHECK(ResolveAgainst(scratch.u8string(), back.jar) == (scratch / "game" / "SoulOfDarkness.jar").make_preferred().u8string());
        CHECK(RelativeToIfInside(scratch.u8string(), "D:/elsewhere/x.jar") == "D:/elsewhere/x.jar");
    }

    // JAR checks: unreadable, not a zip, missing resources, usable but unverified; then the install copy.
    {
        CHECK(!CheckJar((scratch / "missing.jar").u8string()).usable);
        WriteBytes(scratch / "text.jar", {'h', 'e', 'l', 'l', 'o'});
        CHECK(!CheckJar((scratch / "text.jar").u8string()).usable);
        WriteBytes(scratch / "partial.jar", MakeZip({"1", "2", "logo.png"}));
        const JarCheck partial = CheckJar((scratch / "partial.jar").u8string());
        CHECK(!partial.usable && partial.error.find("'3'") != std::string::npos);
        WriteBytes(scratch / "layout.jar", MakeZip(sodhost::requiredDataFiles()));
        const JarCheck layout = CheckJar((scratch / "layout.jar").u8string());
        CHECK(layout.usable && !layout.verified);
        std::string error;
        const fs::path installed = scratch / "game" / "SoulOfDarkness.jar";
        CHECK(InstallJar((scratch / "layout.jar").u8string(), installed.u8string(), error));
        CHECK(fs::file_size(installed) == fs::file_size(scratch / "layout.jar"));
        CHECK(InstallJar(installed.u8string(), installed.u8string(), error));  // re-picking the installed copy

        // The same archive through the update zip reader.
        std::vector<ZipEntry> index;
        CHECK(ReadZipIndex((scratch / "layout.jar").u8string(), index, error));
        CHECK(index.size() == sodhost::requiredDataFiles().size());
        CHECK(ExtractZip((scratch / "layout.jar").u8string(), (scratch / "unpacked").u8string(), error));
        CHECK(fs::file_size(scratch / "unpacked" / "logo.png") == 1);
    }

    // The banner artwork decodes through WIC.
    if (argc > 1) {
        const HRESULT com = CoInitializeEx(nullptr, COINIT_APARTMENTTHREADED);
        std::ifstream file(argv[1], std::ios::binary);
        std::vector<char> bytes((std::istreambuf_iterator<char>(file)), std::istreambuf_iterator<char>());
        Banner banner;
        CHECK(!bytes.empty() && DecodeBanner(bytes.data(), bytes.size(), banner));
        CHECK(banner.valid() && banner.width == 1200 && banner.height == 896);
        Banner bad;
        CHECK(!DecodeBanner("not an image", 12, bad) && !bad.valid());
        if (SUCCEEDED(com)) CoUninitialize();
    }

    std::error_code ignored;
    fs::remove_all(scratch, ignored);
    if (g_failures) {
        std::fprintf(stderr, "%d check(s) failed\n", g_failures);
        return 1;
    }
    std::printf("launcher smoke: all checks passed\n");
    return 0;
}
