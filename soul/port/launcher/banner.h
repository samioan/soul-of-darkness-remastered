#pragma once

// The launcher's banner artwork.
//
// Ported from shadowkey-decomp's port/src/launcher/banner.h. The image is a
// JPEG embedded as an RCDATA resource (assets/banner.jpg, made from
// assets/banner_source.jpg by tools/make_banner.py) and decoded at startup
// through WIC, the system image decoder, so nothing is vendored for it.

#include <cstddef>
#include <cstdint>
#include <vector>

namespace sod {
namespace launcher {

// Decoded pixels in the layout `StretchDIBits` wants for a top-down 32bpp
// DIB: one `uint32_t` per pixel, bytes in B,G,R,X order, rows top to
// bottom, no row padding.
struct Banner {
    int width = 0;
    int height = 0;
    std::vector<uint32_t> pixels;

    bool valid() const {
        return width > 0 && height > 0 &&
               pixels.size() == static_cast<size_t>(width) * static_cast<size_t>(height);
    }
};

// A sanity bound on the decoded dimensions, so a corrupt image header cannot
// ask for a multi-gigabyte allocation.
constexpr int kMaxBannerDimension = 8192;

// Decodes a JPEG/PNG from memory (COM must be initialised). Returns false and
// leaves `out` empty on anything that goes wrong; the launcher then draws a
// flat background rather than refusing to start.
bool DecodeBanner(const void* data, size_t size, Banner& out);

// Same, reading the image from the executable's own RCDATA resource.
Banner LoadEmbeddedBanner();

}  // namespace launcher
}  // namespace sod
