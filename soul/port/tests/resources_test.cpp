#include "resources.h"
#include <fstream>
#include <iostream>
#include <limits>

namespace {
int checks = 0;
void require(bool result, const char* label) {
    ++checks;
    if (!result) throw std::runtime_error(label);
}
template<class Error = soul::FormatError, class Function>
void rejects(Function function, const char* label) {
    bool rejected = false;
    try { function(); } catch (const Error&) { rejected = true; }
    require(rejected, label);
}
void put32(soul::Bytes& bytes, std::size_t at, std::uint32_t value) {
    for (unsigned i = 0; i < 4; ++i) bytes.at(at + i) = static_cast<std::uint8_t>(value >> (8 * i));
}
const soul::Bytes numeric{3,0, 1,0, 0,0, 22,0,0,0, 25,0,0,0, 25,0,0,0, 26,0,0,0, 0,0xaa,0xbb, 0};
const soul::Bytes promotion{4,0, 0,0,0,0, 2,0,0,0, 2,0,0,0, 3,0,0,0, 0xaa,0xbb,0xcc};
void readers() {
    const soul::Bytes bytes{0x80,0xff,0x00,0x80,0xff,0xff,0xff,0x7f, 0,0,0,0x80, 0x80,0,0,0, 0xff,0xff,0xff,0xff};
    soul::BinaryReader r(soul::ByteView(bytes), "reader fixture");
    require(r.i8() == -128 && r.i8() == -1, "signed byte reads");
    require(r.i16le() == -32768, "signed LE short");
    require(r.i32le() == 2147483647, "positive signed LE int");
    require(r.i32le() == (std::numeric_limits<std::int32_t>::min)(), "negative signed LE int");
    require(r.i32be() == (std::numeric_limits<std::int32_t>::min)(), "negative signed BE int");
    require(r.u32be() == 0xffffffffU, "unsigned BE int"); r.done();
    r.seek(0); require(r.u16le() == 0xff80U, "unsigned LE short");
    r.seek(0); require(r.u16be() == 0x80ffU, "unsigned BE short");
    r.seek(0); require(r.i16be() == -32513, "signed BE short");
    r.seek(16); require(r.i32be() == -1, "negative BE one");
    r.seek(16); require(r.i32le() == -1, "negative LE one");
    r.seek(0); rejects([&] { r.done(); }, "trailing bytes rejected");
    rejects([&] { r.seek(bytes.size() + 1); }, "out of range seek rejected");
    require(r.position() == 0, "failed seek does not advance");
    for (std::size_t length = 0; length < 4; ++length) {
        soul::BinaryReader small(soul::ByteView(bytes).subview(0, length));
        rejects([&] { small.u32le(); }, "truncated int rejected");
        require(small.position() == 0, "failed numeric read is atomic");
        rejects([&] { small.take((std::numeric_limits<std::size_t>::max)()); }, "oversized read cannot wrap");
    }
    soul::ByteView empty;
    require(empty.subview(0, 0).size() == 0, "empty null view subrange");
    rejects([&] { empty.at(0); }, "empty view access rejected");
    rejects([&] { soul::ByteView(bytes).subview(1, (std::numeric_limits<std::size_t>::max)()); }, "subview overflow rejected");
    rejects<std::invalid_argument>([] { soul::ByteView(nullptr, 1); }, "null nonempty view rejected");
    soul::BinaryReader end(empty); end.skip(0); end.seek(0); end.done();
    require(soul::fingerprint(empty) == 14695981039346656037ULL, "empty fingerprint");
    const soul::Bytes hello{'h','e','l','l','o'};
    require(soul::fingerprint(soul::ByteView(hello)) == 0xa430d84680aabd0bULL, "known FNV-1a vector");
}
void numericPacks() {
    const auto pack = soul::ResourcePack::numeric(numeric);
    require(pack.size() == 3 && pack.fileSize() == 26, "numeric pack dimensions");
    const auto first = pack.resource(0);
    require(first && first->size() == 2 && first->at(0) == 0xaa && first->at(1) == 0xbb, "prefix stripped once");
    require(!pack.resource(1), "equal offsets are absent");
    require(pack.resource(2) && pack.resource(2)->size() == 0, "prefix-only entry is present and empty");
    rejects<std::out_of_range>([&] { pack.resource(3); }, "invalid resource index rejected");
    rejects<std::out_of_range>([&] { pack.resource((std::numeric_limits<std::size_t>::max)()); }, "oversized resource index rejected");
    for (std::size_t length = 0; length < numeric.size(); ++length) {
        soul::Bytes truncated(numeric.begin(), numeric.begin() + length);
        rejects([&] { soul::ResourcePack::numeric(truncated); }, "every numeric truncation rejected");
    }
    auto corrupt = [](std::size_t at, std::uint8_t value) {
        auto bytes = numeric; bytes.at(at) = value;
        rejects([&] { soul::ResourcePack::numeric(bytes); }, "invalid numeric metadata rejected");
    };
    corrupt(2, 0); corrupt(2, 2); corrupt(4, 1); corrupt(6, 21); corrupt(6, 23);
    corrupt(10, 21); corrupt(18, 27); corrupt(22, 1);
    auto huge = numeric; put32(huge, 10, 0xffffffffU);
    rejects([&] { soul::ResourcePack::numeric(huge); }, "large intermediate offset rejected");
    huge = numeric; huge[0] = 0xff; huge[1] = 0xff;
    rejects([&] { soul::ResourcePack::numeric(huge); }, "large count with short table rejected");
    auto trailing = numeric; trailing.push_back(0);
    rejects([&] { soul::ResourcePack::numeric(trailing); }, "numeric suffix rejected");
    const auto zero = soul::ResourcePack::numeric({0,0,1,0,0,0,10,0,0,0});
    require(zero.size() == 0, "zero-slot numeric pack has only a sentinel");
}
void promotionPacks() {
    const auto pack = soul::ResourcePack::promotion(promotion);
    require(pack.size() == 3, "promotion sentinel excluded from entries");
    require(pack.resource(0)->size() == 2 && pack.resource(0)->at(0) == 0xaa, "promotion does not strip a prefix");
    require(pack.resource(1) && pack.resource(1)->size() == 0, "empty promotion range is present");
    require(pack.resource(2)->at(0) == 0xcc, "promotion offsets relative to payload");
    for (std::size_t length = 0; length < promotion.size(); ++length) {
        soul::Bytes truncated(promotion.begin(), promotion.begin() + length);
        rejects([&] { soul::ResourcePack::promotion(truncated); }, "every promotion truncation rejected");
    }
    for (const auto& edit : {std::pair<std::size_t,std::uint32_t>{2,1}, {6,4}, {10,1}, {14,4}, {6,0xffffffffU}}) {
        auto bad = promotion; put32(bad, edit.first, edit.second);
        rejects([&] { soul::ResourcePack::promotion(bad); }, "invalid promotion offsets rejected");
    }
    auto trailing = promotion; trailing.push_back(0);
    rejects([&] { soul::ResourcePack::promotion(trailing); }, "promotion suffix rejected");
    rejects([] { soul::ResourcePack::promotion({0,0}); }, "promotion requires a sentinel");
    require(soul::ResourcePack::promotion({1,0,0,0,0,0}).size() == 0, "empty promotion container");
}
void files(const std::filesystem::path& directory) {
    std::filesystem::create_directories(directory);
    const auto path = directory / "reader-fixture.bin";
    { std::ofstream out(path, std::ios::binary); out.write("abc", 3); if (!out) throw std::runtime_error("Cannot write test fixture"); }
    require(soul::readAssetFile(path) == soul::Bytes({'a','b','c'}), "file bytes preserved");
    rejects([&] { soul::readAssetFile(path, 2); }, "file allocation cap enforced");
    std::filesystem::remove(path);
    rejects([&] { soul::readAssetFile(path); }, "missing file rejected");
    { std::ofstream out(path, std::ios::binary); if (!out) throw std::runtime_error("Cannot write empty fixture"); }
    require(soul::readAssetFile(path, 0).empty(), "empty file within zero limit");
    std::filesystem::remove(path);
}
}
int main(int argc, char** argv) {
    try {
        if (argc != 2) throw std::invalid_argument("Expected test fixture directory");
        readers(); numericPacks(); promotionPacks(); files(argv[1]);
        std::cout << "PASS: " << checks << " native resource assertions (synthetic fixtures, no game data)\n";
        return 0;
    } catch (const std::exception& error) { std::cerr << error.what() << '\n'; return 1; }
}
