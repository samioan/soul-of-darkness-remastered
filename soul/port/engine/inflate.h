// DEFLATE decoding (inflate.cpp). Appends the decoded bytes to `out`; false on malformed input.
#pragma once
#include <cstddef>
#include <cstdint>
#include <vector>

namespace sod {

// A raw DEFLATE stream, as stored in a ZIP/JAR entry.
bool inflateRaw(const std::uint8_t* data, std::size_t size, std::vector<std::uint8_t>& out);
// A zlib stream (2-byte header, no checksum check), as in a PNG's IDAT chunks.
bool inflateZlib(const std::uint8_t* data, std::size_t size, std::vector<std::uint8_t>& out);

} // namespace sod
