#pragma once
#include <cstddef>
#include <cstdint>
#include <filesystem>
#include <optional>
#include <stdexcept>
#include <string>
#include <utility>
#include <vector>

namespace soul {
using Bytes = std::vector<std::uint8_t>;
class FormatError : public std::runtime_error {
public:
    using std::runtime_error::runtime_error;
};

// Borrowed, immutable bytes. The owner must outlive the view and its readers.
class ByteView {
public:
    ByteView() = default;
    ByteView(const std::uint8_t* data, std::size_t size);
    explicit ByteView(const Bytes& bytes) : ByteView(bytes.data(), bytes.size()) {}
    ByteView(Bytes&&) = delete;
    const std::uint8_t* data() const { return data_; }
    std::size_t size() const { return size_; }
    std::uint8_t at(std::size_t index) const;
    ByteView subview(std::size_t offset, std::size_t count) const;
private:
    const std::uint8_t* data_ = nullptr;
    std::size_t size_ = 0;
};

class BinaryReader {
public:
    explicit BinaryReader(ByteView bytes, std::string label = "data")
        : bytes_(bytes), label_(std::move(label)) {}
    std::size_t position() const { return position_; }
    std::size_t remaining() const { return bytes_.size() - position_; }
    ByteView take(std::size_t count);
    void skip(std::size_t count) { take(count); }
    void seek(std::size_t offset);
    void done() const;
    std::uint8_t u8();
    std::int8_t i8();
    std::uint16_t u16le();
    std::int16_t i16le();
    std::uint32_t u32le();
    std::int32_t i32le();
    std::uint16_t u16be();
    std::int16_t i16be();
    std::uint32_t u32be();
    std::int32_t i32be();
private:
    ByteView bytes_;
    std::string label_;
    std::size_t position_ = 0;
};

// Owns the complete container. Resource views borrow from this immutable owner.
class ResourcePack {
public:
    static ResourcePack numeric(Bytes bytes, std::string label = "pack");
    static ResourcePack promotion(Bytes bytes, std::string label = "dataIGP");
    std::size_t size() const { return slices_.size(); }
    std::size_t fileSize() const { return bytes_.size(); }
    // Numeric absent slots return nullopt; a prefix-only entry is present/empty.
    // Promotion ranges are always present, including zero-length ranges.
    std::optional<ByteView> resource(std::size_t index) const &;
    std::optional<ByteView> resource(std::size_t) const && = delete;
private:
    struct Slice { std::size_t offset, size; bool present; };
    ResourcePack(Bytes bytes, std::string label) : bytes_(std::move(bytes)), label_(std::move(label)) {}
    Bytes bytes_;
    std::string label_;
    std::vector<Slice> slices_;
};

// A tooling allocation limit, not a claim about the game's file format.
constexpr std::size_t kMaxAssetFileBytes = 64 * 1024 * 1024;
Bytes readAssetFile(const std::filesystem::path& path, std::size_t limit = kMaxAssetFileBytes);
// Noncryptographic manifest fingerprint, independent of presentation/game state.
std::uint64_t fingerprint(ByteView bytes);
}
