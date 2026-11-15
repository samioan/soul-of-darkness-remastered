#include "host_util.h"
#include <cstring>

namespace sodhost {

namespace {
inline std::uint32_t rotr(std::uint32_t x, int n) { return (x >> n) | (x << (32 - n)); }
const std::uint32_t K[64] = {
    0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5, 0xd807aa98, 0x12835b01, 0x243185be,
    0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174, 0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa,
    0x5cb0a9dc, 0x76f988da, 0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967, 0x27b70a85,
    0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85, 0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3,
    0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070, 0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f,
    0x682e6ff3, 0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2};
std::uint32_t le32(const std::uint8_t* p) { return p[0] | (p[1] << 8) | (p[2] << 16) | (static_cast<std::uint32_t>(p[3]) << 24); }
std::uint16_t le16(const std::uint8_t* p) { return static_cast<std::uint16_t>(p[0] | (p[1] << 8)); }
}

std::string sha256Hex(const std::vector<std::uint8_t>& data) {
    std::uint32_t h[8] = {0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a, 0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19};
    std::vector<std::uint8_t> msg = data;
    const std::uint64_t bits = static_cast<std::uint64_t>(data.size()) * 8;
    msg.push_back(0x80);
    while (msg.size() % 64 != 56) msg.push_back(0);
    for (int i = 7; i >= 0; --i) msg.push_back(static_cast<std::uint8_t>(bits >> (i * 8)));
    for (std::size_t off = 0; off < msg.size(); off += 64) {
        std::uint32_t w[64];
        for (int i = 0; i < 16; ++i)
            w[i] = (static_cast<std::uint32_t>(msg[off + i * 4]) << 24) | (msg[off + i * 4 + 1] << 16) | (msg[off + i * 4 + 2] << 8) | msg[off + i * 4 + 3];
        for (int i = 16; i < 64; ++i) {
            std::uint32_t s0 = rotr(w[i - 15], 7) ^ rotr(w[i - 15], 18) ^ (w[i - 15] >> 3);
            std::uint32_t s1 = rotr(w[i - 2], 17) ^ rotr(w[i - 2], 19) ^ (w[i - 2] >> 10);
            w[i] = w[i - 16] + s0 + w[i - 7] + s1;
        }
        std::uint32_t a = h[0], b = h[1], c = h[2], d = h[3], e = h[4], f = h[5], g = h[6], hh = h[7];
        for (int i = 0; i < 64; ++i) {
            std::uint32_t t1 = hh + (rotr(e, 6) ^ rotr(e, 11) ^ rotr(e, 25)) + ((e & f) ^ (~e & g)) + K[i] + w[i];
            std::uint32_t t2 = (rotr(a, 2) ^ rotr(a, 13) ^ rotr(a, 22)) + ((a & b) ^ (a & c) ^ (b & c));
            hh = g; g = f; f = e; e = d + t1; d = c; c = b; b = a; a = t1 + t2;
        }
        h[0] += a; h[1] += b; h[2] += c; h[3] += d; h[4] += e; h[5] += f; h[6] += g; h[7] += hh;
    }
    static const char* hex = "0123456789abcdef";
    std::string out;
    for (std::uint32_t v : h)
        for (int i = 28; i >= 0; i -= 4) out.push_back(hex[(v >> i) & 15]);
    return out;
}

bool readZipDirectory(const std::vector<std::uint8_t>& z, std::vector<ZipDirEntry>& out, std::string& error) {
    if (z.size() < 22) { error = "file is too small to be a JAR"; return false; }
    std::size_t eocd = std::string::npos;
    for (std::size_t i = z.size() - 22;; --i) {
        if (le32(&z[i]) == 0x06054b50u) { eocd = i; break; }
        if (i == 0 || z.size() - i > 66000) break;
    }
    if (eocd == std::string::npos) { error = "not a ZIP/JAR archive"; return false; }
    const std::size_t count = le16(&z[eocd + 10]);
    std::size_t pos = le32(&z[eocd + 16]);
    for (std::size_t n = 0; n < count; ++n) {
        if (pos + 46 > z.size() || le32(&z[pos]) != 0x02014b50u) { error = "corrupt central directory"; return false; }
        ZipDirEntry entry;
        entry.method = le16(&z[pos + 10]);
        entry.compressed = le32(&z[pos + 20]);
        entry.uncompressed = le32(&z[pos + 24]);
        const std::size_t nameLen = le16(&z[pos + 28]), extraLen = le16(&z[pos + 30]), commentLen = le16(&z[pos + 32]);
        const std::size_t local = le32(&z[pos + 42]);
        if (pos + 46 + nameLen > z.size()) { error = "corrupt entry name"; return false; }
        entry.name.assign(reinterpret_cast<const char*>(&z[pos + 46]), nameLen);
        pos += 46 + nameLen + extraLen + commentLen;
        if (entry.name.empty() || entry.name.back() == '/') continue;
        if (local + 30 > z.size() || le32(&z[local]) != 0x04034b50u) { error = "corrupt local header"; return false; }
        entry.dataStart = local + 30 + le16(&z[local + 26]) + le16(&z[local + 28]);
        if (entry.dataStart > z.size() || entry.compressed > z.size() - entry.dataStart) { error = "truncated entry " + entry.name; return false; }
        out.push_back(std::move(entry));
    }
    return true;
}

const std::vector<std::string>& requiredDataFiles() {
    static const std::vector<std::string> files = [] {
        std::vector<std::string> list;
        for (int i = 1; i <= kPackCount; ++i) list.push_back(std::to_string(i));
        list.push_back("logo.png");
        list.push_back("new.png");
        return list;
    }();
    return files;
}

} // namespace sodhost
