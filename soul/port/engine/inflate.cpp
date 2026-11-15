// DEFLATE decoder (RFC 1951) used for the game's PNG images and, by the host and the launcher, for
// JAR/ZIP entries. Self-contained: no zlib.
#include "inflate.h"
#include <algorithm>

namespace sod {

namespace {
struct BitReader {
    const std::uint8_t* data;
    std::size_t size, pos = 0;
    std::uint32_t bits = 0;
    int count = 0;
    bool fail = false;
    int bit() {
        if (count == 0) {
            if (pos >= size) { fail = true; return 0; }
            bits = data[pos++];
            count = 8;
        }
        int b = bits & 1;
        bits >>= 1;
        --count;
        return b;
    }
    std::uint32_t take(int n) {
        std::uint32_t v = 0;
        for (int i = 0; i < n; ++i) v |= static_cast<std::uint32_t>(bit()) << i;
        return v;
    }
};
struct Huffman {
    std::uint16_t counts[16] = {};
    std::uint16_t symbols[288] = {};
    void build(const std::uint8_t* lengths, int n) {
        std::fill(counts, counts + 16, static_cast<std::uint16_t>(0));
        for (int i = 0; i < n; ++i) ++counts[lengths[i]];
        counts[0] = 0;
        std::uint16_t offsets[16];
        offsets[1] = 0;
        for (int i = 1; i < 15; ++i) offsets[i + 1] = static_cast<std::uint16_t>(offsets[i] + counts[i]);
        for (int i = 0; i < n; ++i) if (lengths[i]) symbols[offsets[lengths[i]]++] = static_cast<std::uint16_t>(i);
    }
    int decode(BitReader& r) const {
        int code = 0, first = 0, index = 0;
        for (int len = 1; len <= 15; ++len) {
            code |= r.bit();
            int count = counts[len];
            if (code - count < first) return symbols[index + (code - first)];
            index += count;
            first += count;
            first <<= 1;
            code <<= 1;
            if (r.fail) return -1;
        }
        return -1;
    }
};
bool inflate(const std::uint8_t* data, std::size_t size, std::vector<std::uint8_t>& out, bool zlibHeader) {
    static const std::uint16_t lengthBase[] = {3,4,5,6,7,8,9,10,11,13,15,17,19,23,27,31,35,43,51,59,67,83,99,115,131,163,195,227,258};
    static const std::uint16_t lengthExtra[] = {0,0,0,0,0,0,0,0,1,1,1,1,2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5,0};
    static const std::uint16_t distBase[] = {1,2,3,4,5,7,9,13,17,25,33,49,65,97,129,193,257,385,513,769,1025,1537,2049,3073,4097,6145,8193,12289,16385,24577};
    static const std::uint16_t distExtra[] = {0,0,0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11,12,12,13,13};
    const std::size_t skip = zlibHeader ? 2 : 0;
    if (size < skip) return false;
    BitReader r{data + skip, size - skip};
    int last;
    do {
        last = r.bit();
        int kind = static_cast<int>(r.take(2));
        if (kind == 0) {
            r.count = 0;
            if (r.pos + 4 > r.size) return false;
            std::size_t len = r.data[r.pos] | (r.data[r.pos + 1] << 8);
            r.pos += 4;
            if (r.pos + len > r.size) return false;
            out.insert(out.end(), r.data + r.pos, r.data + r.pos + len);
            r.pos += len;
        } else if (kind == 1 || kind == 2) {
            Huffman lit, dist;
            std::uint8_t lengths[320];
            if (kind == 1) {
                int i = 0;
                for (; i < 144; ++i) lengths[i] = 8;
                for (; i < 256; ++i) lengths[i] = 9;
                for (; i < 280; ++i) lengths[i] = 7;
                for (; i < 288; ++i) lengths[i] = 8;
                lit.build(lengths, 288);
                for (i = 0; i < 30; ++i) lengths[i] = 5;
                dist.build(lengths, 30);
            } else {
                static const std::uint8_t order[19] = {16,17,18,0,8,7,9,6,10,5,11,4,12,3,13,2,14,1,15};
                int nlen = static_cast<int>(r.take(5)) + 257, ndist = static_cast<int>(r.take(5)) + 1, ncode = static_cast<int>(r.take(4)) + 4;
                if (nlen > 286 || ndist > 30) return false;
                std::uint8_t codeLengths[19] = {};
                for (int i = 0; i < ncode; ++i) codeLengths[order[i]] = static_cast<std::uint8_t>(r.take(3));
                Huffman code;
                code.build(codeLengths, 19);
                int index = 0;
                while (index < nlen + ndist) {
                    int sym = code.decode(r);
                    if (sym < 0) return false;
                    if (sym < 16) lengths[index++] = static_cast<std::uint8_t>(sym);
                    else {
                        int repeat, value = 0;
                        if (sym == 16) {
                            if (index == 0) return false;
                            value = lengths[index - 1];
                            repeat = 3 + static_cast<int>(r.take(2));
                        } else if (sym == 17) repeat = 3 + static_cast<int>(r.take(3));
                        else repeat = 11 + static_cast<int>(r.take(7));
                        if (index + repeat > nlen + ndist) return false;
                        while (repeat--) lengths[index++] = static_cast<std::uint8_t>(value);
                    }
                }
                lit.build(lengths, nlen);
                dist.build(lengths + nlen, ndist);
            }
            for (;;) {
                int sym = lit.decode(r);
                if (sym < 0 || r.fail) return false;
                if (sym < 256) out.push_back(static_cast<std::uint8_t>(sym));
                else if (sym == 256) break;
                else {
                    sym -= 257;
                    if (sym >= 29) return false;
                    int len = lengthBase[sym] + static_cast<int>(r.take(lengthExtra[sym]));
                    int ds = dist.decode(r);
                    if (ds < 0 || ds >= 30) return false;
                    std::size_t d = distBase[ds] + r.take(distExtra[ds]);
                    if (d > out.size()) return false;
                    std::size_t from = out.size() - d;
                    for (int i = 0; i < len; ++i) out.push_back(out[from + static_cast<std::size_t>(i)]);
                }
            }
        } else {
            return false;
        }
        if (r.fail) return false;
    } while (!last);
    return true;
}
} // namespace

bool inflateRaw(const std::uint8_t* data, std::size_t size, std::vector<std::uint8_t>& out) { return inflate(data, size, out, false); }
bool inflateZlib(const std::uint8_t* data, std::size_t size, std::vector<std::uint8_t>& out) { return inflate(data, size, out, true); }

} // namespace sod
