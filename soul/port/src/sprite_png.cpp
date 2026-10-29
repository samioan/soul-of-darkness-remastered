#include "bsprite.h"
#include <windows.h>
#include <wincodec.h>
#include <wrl/client.h>
#include <algorithm>

namespace soul {
namespace {
void require(bool value, const char* message) { if (!value) throw FormatError(message); }
void check(HRESULT result, const char* message) { require(SUCCEEDED(result), message); }
std::uint32_t crc32(ByteView bytes) {
    std::uint32_t crc = 0xffffffffU;
    for (std::size_t i = 0; i < bytes.size(); ++i) {
        crc ^= bytes.at(i);
        for (int bit = 0; bit < 8; ++bit) crc = (crc >> 1) ^ ((crc & 1) ? 0xedb88320U : 0);
    }
    return crc ^ 0xffffffffU;
}
struct ComScope {
    HRESULT result = CoInitializeEx(nullptr, COINIT_MULTITHREADED);
    ComScope() { require(SUCCEEDED(result) || result == RPC_E_CHANGED_MODE, "PNG COM initialization failed"); }
    ~ComScope() { if (SUCCEEDED(result)) CoUninitialize(); }
};
}
SpriteImage decodeSpritePng(Bytes png) {
    BinaryReader reader{ByteView(png), "sprite PNG"};
    constexpr std::uint8_t signature[]{137,80,78,71,13,10,26,10};
    const auto magic = reader.take(8);
    require(std::equal(std::begin(signature), std::end(signature), magic.data()), "Invalid PNG signature");
    unsigned width = 0, height = 0, depth = 0;
    Pixels colors;
    Bytes alpha;
    bool header = false, palette = false, transparency = false, data = false, endedData = false;
    while (true) {
        const auto length = reader.u32be();
        const auto chunkStart = reader.position();
        const auto kind = reader.u32be();
        const auto payload = reader.take(length);
        const auto crc = reader.u32be();
        require(crc32(ByteView(png).subview(chunkStart, 4 + static_cast<std::size_t>(length))) == crc, "PNG chunk CRC mismatch");
        require(header || kind == 0x49484452U, "PNG must start with IHDR");
        if (data && kind != 0x49444154U) endedData = true;
        BinaryReader chunk(payload);
        if (kind == 0x49484452U) {
            require(!header && length == 13, "Invalid/duplicate PNG IHDR"); header = true;
            width = chunk.u32be(); height = chunk.u32be(); depth = chunk.u8();
            require(width && height && width <= 16000000U / height, "PNG dimensions exceed pixel budget");
            require((depth == 1 || depth == 2 || depth == 4 || depth == 8) && chunk.u8() == 3
                && chunk.u8() == 0 && chunk.u8() == 0 && chunk.u8() == 0, "Unsupported sprite PNG format");
        } else if (kind == 0x504c5445U) {
            require(!palette && !data && length && length % 3 == 0 && length / 3 <= (1U << depth), "Invalid PNG palette");
            palette = true;
            while (chunk.remaining()) {
                const auto red = chunk.u8(), green = chunk.u8(), blue = chunk.u8();
                colors.push_back(0xff000000U | (static_cast<unsigned>(red) << 16) | (static_cast<unsigned>(green) << 8) | blue);
            }
        } else if (kind == 0x74524e53U) {
            require(palette && !transparency && !data && length <= colors.size(), "Invalid PNG alpha table"); transparency = true;
            alpha.assign(payload.data(), payload.data() + payload.size());
        } else if (kind == 0x49444154U) {
            require(palette && !endedData, "Invalid PNG IDAT order"); data = true;
        } else if (kind == 0x49454e44U) {
            require(data && length == 0, "Invalid PNG IEND"); break;
        } else {
            require((kind & 0x20000000U) != 0, "Unknown critical PNG chunk");
        }
    }
    reader.done();
    for (std::size_t i = 0; i < alpha.size(); ++i) colors[i] = (colors[i] & 0xffffffU) | (static_cast<unsigned>(alpha[i]) << 24);
    ComScope com;
    using Microsoft::WRL::ComPtr;
    ComPtr<IWICImagingFactory> factory;
    check(CoCreateInstance(CLSID_WICImagingFactory, nullptr, CLSCTX_INPROC_SERVER, IID_PPV_ARGS(&factory)), "Cannot create WIC factory");
    ComPtr<IWICStream> stream; check(factory->CreateStream(&stream), "Cannot create PNG stream");
    require(png.size() <= MAXDWORD, "PNG stream too large");
    check(stream->InitializeFromMemory(png.data(), static_cast<DWORD>(png.size())), "Cannot initialize PNG stream");
    ComPtr<IWICBitmapDecoder> decoder;
    check(factory->CreateDecoder(GUID_ContainerFormatPng, &GUID_VendorMicrosoft, &decoder), "Cannot create Microsoft PNG decoder");
    check(decoder->Initialize(stream.Get(), WICDecodeMetadataCacheOnLoad), "Cannot initialize PNG decoder");
    ComPtr<IWICBitmapFrameDecode> frame; check(decoder->GetFrame(0, &frame), "Cannot read PNG frame");
    UINT actualWidth = 0, actualHeight = 0;
    check(frame->GetSize(&actualWidth, &actualHeight), "Cannot read PNG dimensions");
    require(width == actualWidth && height == actualHeight, "PNG decoder dimension mismatch");
    WICPixelFormatGUID format;
    check(frame->GetPixelFormat(&format), "Cannot read PNG pixel format");
    const auto expected = depth == 1 ? GUID_WICPixelFormat1bppIndexed : depth == 2 ? GUID_WICPixelFormat2bppIndexed
        : depth == 4 ? GUID_WICPixelFormat4bppIndexed : GUID_WICPixelFormat8bppIndexed;
    require(IsEqualGUID(format, expected), "PNG decoder did not preserve indexed format");
    const unsigned stride = (width * depth + 7) / 8;
    Bytes indices(static_cast<std::size_t>(stride) * height);
    check(frame->CopyPixels(nullptr, stride, static_cast<UINT>(indices.size()), indices.data()), "Cannot decode PNG scanlines");
    Pixels pixels; pixels.reserve(static_cast<std::size_t>(width) * height);
    for (unsigned y = 0; y < height; ++y) for (unsigned x = 0; x < width; ++x) {
        const unsigned index = (indices[static_cast<std::size_t>(y) * stride + x * depth / 8] >> (8 - depth - x * depth % 8)) & ((1U << depth) - 1);
        require(index < colors.size(), "PNG index outside palette");
        pixels.push_back(colors[index]);
    }
    return {width, height, std::move(pixels), std::move(png)};
}
std::uint64_t pixelFingerprint(const Pixels& pixels) {
    std::uint64_t hash = 14695981039346656037ULL;
    for (auto pixel : pixels) for (int shift : {24,16,8,0}) hash = (hash ^ ((pixel >> shift) & 255)) * 1099511628211ULL;
    return hash;
}
}
