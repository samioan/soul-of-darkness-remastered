#include "resources.h"
#include <fstream>
#include <limits>
#include <utility>

namespace soul {
namespace {
void require(bool value, const std::string& message) { if (!value) throw FormatError(message); }
std::int32_t signed32(std::uint32_t value) {
    const auto wide = static_cast<std::int64_t>(value);
    return static_cast<std::int32_t>(value <= 0x7fffffffU ? wide : wide - 0x100000000LL);
}
std::int16_t signed16(std::uint16_t value) {
    return static_cast<std::int16_t>(value < 0x8000U ? static_cast<int>(value) : static_cast<int>(value) - 65536);
}
}
ByteView::ByteView(const std::uint8_t* data, std::size_t size) : data_(data), size_(size) {
    if (!data && size) throw std::invalid_argument("Nonempty byte view has no storage");
}
std::uint8_t ByteView::at(std::size_t index) const {
    if (index >= size_) throw FormatError("Byte view index out of bounds");
    return data_[index];
}
ByteView ByteView::subview(std::size_t offset, std::size_t count) const {
    if (offset > size_ || count > size_ - offset) throw FormatError("Byte view range out of bounds");
    return ByteView(data_ ? data_ + offset : nullptr, count);
}
ByteView BinaryReader::take(std::size_t count) {
    if (count > remaining()) throw FormatError(label_ + ": truncated at " + std::to_string(position_)
        + ", need " + std::to_string(count) + " bytes");
    const auto result = bytes_.subview(position_, count);
    position_ += count;
    return result;
}
void BinaryReader::seek(std::size_t offset) {
    require(offset <= bytes_.size(), label_ + ": seek out of bounds");
    position_ = offset;
}
void BinaryReader::done() const {
    require(remaining() == 0, label_ + ": trailing bytes at " + std::to_string(position_));
}
std::uint8_t BinaryReader::u8() { return take(1).at(0); }
std::int8_t BinaryReader::i8() {
    const auto value = u8();
    return static_cast<std::int8_t>(value < 128 ? static_cast<int>(value) : static_cast<int>(value) - 256);
}
std::uint16_t BinaryReader::u16le() {
    const auto b = take(2);
    return static_cast<std::uint16_t>(b.at(0) | (static_cast<unsigned>(b.at(1)) << 8));
}
std::int16_t BinaryReader::i16le() { return signed16(u16le()); }
std::uint32_t BinaryReader::u32le() {
    const auto b = take(4);
    return static_cast<std::uint32_t>(b.at(0)) | (static_cast<std::uint32_t>(b.at(1)) << 8)
        | (static_cast<std::uint32_t>(b.at(2)) << 16) | (static_cast<std::uint32_t>(b.at(3)) << 24);
}
std::int32_t BinaryReader::i32le() { return signed32(u32le()); }
std::uint16_t BinaryReader::u16be() {
    const auto b = take(2);
    return static_cast<std::uint16_t>((static_cast<unsigned>(b.at(0)) << 8) | b.at(1));
}
std::int16_t BinaryReader::i16be() { return signed16(u16be()); }
std::uint32_t BinaryReader::u32be() {
    const auto b = take(4);
    return (static_cast<std::uint32_t>(b.at(0)) << 24) | (static_cast<std::uint32_t>(b.at(1)) << 16)
        | (static_cast<std::uint32_t>(b.at(2)) << 8) | static_cast<std::uint32_t>(b.at(3));
}
std::int32_t BinaryReader::i32be() { return signed32(u32be()); }

ResourcePack ResourcePack::numeric(Bytes bytes, std::string label) {
    ResourcePack pack(std::move(bytes), std::move(label));
    BinaryReader reader(ByteView(pack.bytes_), pack.label_);
    const std::size_t count = reader.u16le();
    require(reader.u16le() == 1, pack.label_ + ": split packs not supported");
    require(reader.u16le() == 0, pack.label_ + ": invalid first resource index");
    require(count + 1 <= reader.remaining() / 4, pack.label_ + ": truncated offset table");
    std::vector<std::uint32_t> offsets;
    offsets.reserve(count + 1);
    for (std::size_t i = 0; i <= count; ++i) offsets.push_back(reader.u32le());
    require(offsets.front() == reader.position(), pack.label_ + ": gap/overlap before payload");
    require(offsets.back() == pack.bytes_.size(), pack.label_ + ": end offset does not match file size");
    for (std::size_t i = 0; i < count; ++i) {
        const std::size_t start = offsets[i], end = offsets[i + 1];
        require(start <= end && end <= pack.bytes_.size(), pack.label_ + ": invalid resource bounds at " + std::to_string(i));
        if (start == end) pack.slices_.push_back({start, 0, false});
        else {
            require(pack.bytes_[start] == 0, pack.label_ + ": unsupported prefix at resource " + std::to_string(i));
            pack.slices_.push_back({start + 1, end - start - 1, true});
        }
    }
    return pack;
}
ResourcePack ResourcePack::promotion(Bytes bytes, std::string label) {
    ResourcePack pack(std::move(bytes), std::move(label));
    BinaryReader reader(ByteView(pack.bytes_), pack.label_);
    const std::size_t count = reader.u16le();
    require(count >= 1 && count <= reader.remaining() / 4, pack.label_ + ": invalid/truncated offset table");
    std::vector<std::uint32_t> offsets;
    offsets.reserve(count);
    for (std::size_t i = 0; i < count; ++i) offsets.push_back(reader.u32le());
    const auto base = reader.position(), payload = reader.remaining();
    require(offsets.front() == 0 && offsets.back() == payload, pack.label_ + ": invalid payload bounds");
    for (std::size_t i = 0; i + 1 < count; ++i) {
        const std::size_t start = offsets[i], end = offsets[i + 1];
        require(start <= end && end <= payload, pack.label_ + ": invalid promotion range");
        pack.slices_.push_back({base + start, end - start, true});
    }
    return pack;
}
std::optional<ByteView> ResourcePack::resource(std::size_t index) const & {
    if (index >= size()) throw std::out_of_range(label_ + ": resource index out of bounds");
    const auto& slice = slices_[index];
    if (!slice.present) return std::nullopt;
    return ByteView(bytes_).subview(slice.offset, slice.size);
}
Bytes readAssetFile(const std::filesystem::path& path, std::size_t limit) {
    std::ifstream file(path, std::ios::binary | std::ios::ate);
    if (!file) throw FormatError("Cannot open asset: " + path.string());
    const auto end = file.tellg();
    if (end < 0 || static_cast<std::uintmax_t>(end) > limit
        || static_cast<std::uintmax_t>(end) > static_cast<std::uintmax_t>((std::numeric_limits<std::streamsize>::max)()))
        throw FormatError("Asset exceeds file allocation limit: " + path.string());
    Bytes bytes(static_cast<std::size_t>(end));
    file.seekg(0);
    if (!file || (!bytes.empty() && !file.read(reinterpret_cast<char*>(bytes.data()), static_cast<std::streamsize>(bytes.size()))))
        throw FormatError("Cannot read asset: " + path.string());
    // Detect growth between size discovery and reading instead of ignoring a suffix.
    if (file.peek() != std::char_traits<char>::eof() || file.bad())
        throw FormatError("Asset changed or read failed: " + path.string());
    return bytes;
}
std::uint64_t fingerprint(ByteView bytes) {
    std::uint64_t value = 14695981039346656037ULL;
    for (std::size_t i = 0; i < bytes.size(); ++i) value = (value ^ bytes.at(i)) * 1099511628211ULL;
    return value;
}
}
