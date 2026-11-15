#include "midp.h"
#include "inflate.h"
#include <algorithm>
#include <cmath>

namespace sod {

// ---------------------------------------------------------------- PNG

namespace {
std::uint32_t be32(const std::uint8_t* p) { return (static_cast<std::uint32_t>(p[0]) << 24) | (p[1] << 16) | (p[2] << 8) | p[3]; }
int paeth(int a, int b, int c) {
    int p = a + b - c, pa = std::abs(p - a), pb = std::abs(p - b), pc = std::abs(p - c);
    return (pa <= pb && pa <= pc) ? a : (pb <= pc ? b : c);
}
} // namespace

bool decodePng(const std::uint8_t* data, std::size_t size, int& width, int& height, std::vector<std::uint32_t>& argb, bool& alpha) {
    static const std::uint8_t signature[8] = {137, 80, 78, 71, 13, 10, 26, 10};
    if (size < 8 || std::memcmp(data, signature, 8) != 0) return false;
    std::size_t pos = 8;
    int depth = 0, colorType = 0, interlace = 0;
    std::vector<std::uint8_t> idat, palette, trns;
    bool header = false;
    while (pos + 8 <= size) {
        std::uint32_t length = be32(data + pos);
        std::uint32_t kind = be32(data + pos + 4);
        const std::uint8_t* body = data + pos + 8;
        if (pos + 12 + static_cast<std::size_t>(length) > size) break;
        if (kind == 0x49484452u && length >= 13) {
            width = static_cast<int>(be32(body));
            height = static_cast<int>(be32(body + 4));
            depth = body[8];
            colorType = body[9];
            interlace = body[12];
            header = true;
        } else if (kind == 0x504c5445u) palette.assign(body, body + length);
        else if (kind == 0x74524e53u) trns.assign(body, body + length);
        else if (kind == 0x49444154u) idat.insert(idat.end(), body, body + length);
        else if (kind == 0x49454e44u) break;
        pos += 12 + static_cast<std::size_t>(length);
    }
    if (!header || width <= 0 || height <= 0 || width > 8192 || height > 8192 || interlace != 0) return false;
    int channels = colorType == 0 ? 1 : colorType == 2 ? 3 : colorType == 3 ? 1 : colorType == 4 ? 2 : colorType == 6 ? 4 : 0;
    if (!channels || (depth != 1 && depth != 2 && depth != 4 && depth != 8 && depth != 16)) return false;
    std::vector<std::uint8_t> raw;
    if (!inflateZlib(idat.data(), idat.size(), raw)) return false;
    std::size_t bpp = static_cast<std::size_t>(std::max(1, channels * depth / 8));
    std::size_t stride = (static_cast<std::size_t>(width) * static_cast<std::size_t>(channels) * static_cast<std::size_t>(depth) + 7) / 8;
    if (raw.size() < (stride + 1) * static_cast<std::size_t>(height)) return false;
    std::vector<std::uint8_t> prev(stride, 0), cur(stride);
    argb.assign(static_cast<std::size_t>(width) * static_cast<std::size_t>(height), 0);
    alpha = colorType == 4 || colorType == 6 || !trns.empty();
    for (int y = 0; y < height; ++y) {
        const std::uint8_t* line = raw.data() + (stride + 1) * static_cast<std::size_t>(y);
        int filter = line[0];
        for (std::size_t i = 0; i < stride; ++i) {
            int a = i >= bpp ? cur[i - bpp] : 0, b = prev[i], c = i >= bpp ? prev[i - bpp] : 0, x = line[1 + i];
            switch (filter) {
                case 1: x += a; break;
                case 2: x += b; break;
                case 3: x += (a + b) / 2; break;
                case 4: x += paeth(a, b, c); break;
                default: break;
            }
            cur[i] = static_cast<std::uint8_t>(x);
        }
        std::uint32_t* dst = argb.data() + static_cast<std::size_t>(y) * static_cast<std::size_t>(width);
        for (int x = 0; x < width; ++x) {
            auto sample = [&](int channel) -> int {
                if (depth == 8) return cur[static_cast<std::size_t>(x * channels + channel)];
                if (depth == 16) return cur[static_cast<std::size_t>((x * channels + channel) * 2)];
                int bit = x * depth;
                int v = (cur[static_cast<std::size_t>(bit / 8)] >> (8 - depth - (bit % 8))) & ((1 << depth) - 1);
                return v;
            };
            std::uint32_t px = 0;
            if (colorType == 3) {
                std::size_t index = static_cast<std::size_t>(sample(0));
                std::uint32_t a = index < trns.size() ? trns[index] : 255;
                if (index * 3 + 2 < palette.size()) px = (a << 24) | (palette[index * 3] << 16) | (palette[index * 3 + 1] << 8) | palette[index * 3 + 2];
                else px = a << 24;
            } else if (colorType == 0) {
                int v = sample(0);
                int scaled = depth >= 8 ? v : v * 255 / ((1 << depth) - 1);
                std::uint32_t a = 255;
                if (trns.size() >= 2 && ((trns[0] << 8) | trns[1]) == (depth == 16 ? (v << 8) | cur[static_cast<std::size_t>(x * 2 + 1)] : v)) a = 0;
                px = (a << 24) | (scaled << 16) | (scaled << 8) | scaled;
            } else if (colorType == 2) {
                int r = sample(0), g = sample(1), b = sample(2);
                std::uint32_t a = 255;
                if (trns.size() >= 6 && depth == 8 && trns[1] == r && trns[3] == g && trns[5] == b) a = 0;
                px = (a << 24) | (r << 16) | (g << 8) | b;
            } else if (colorType == 4) {
                int v = sample(0);
                px = (static_cast<std::uint32_t>(sample(1)) << 24) | (v << 16) | (v << 8) | v;
            } else {
                px = (static_cast<std::uint32_t>(sample(3)) << 24) | (sample(0) << 16) | (sample(1) << 8) | sample(2);
            }
            dst[x] = px;
        }
        prev.swap(cur);
    }
    return true;
}

// ---------------------------------------------------------------- Image

static Ref<Image> imageFromPng(const std::uint8_t* data, std::size_t size) {
    Ref<Image> image = jnew<Image>();
    int w = 0, h = 0;
    bool alpha = false;
    if (!decodePng(data, size, w, h, image->pixels, alpha)) throwJava("java.io.IOException", "PNG decode failed");
    image->width = w;
    image->height = h;
    image->hasAlpha = alpha;
    return image;
}

Ref<Image> Image::createImage(const Ref<String>& resource) {
    if (!resource.p) throwNullPointer();
    std::string path = jutf8(resource);
    while (!path.empty() && path[0] == '/') path.erase(0, 1);
    std::vector<std::uint8_t> bytes;
    if (!hostReadResource(path, bytes)) throwJava("java.io.IOException", "missing resource " + path);
    return imageFromPng(bytes.data(), bytes.size());
}

Ref<Image> Image::createImage(jint width, jint height) {
    if (width <= 0 || height <= 0) throwJava("java.lang.IllegalArgumentException", "image size");
    Ref<Image> image = jnew<Image>();
    image->width = width;
    image->height = height;
    image->mutableImage = true;
    image->pixels.assign(static_cast<std::size_t>(width) * static_cast<std::size_t>(height), 0xffffffffu);
    return image;
}

Ref<Image> Image::createImage(const Arr<jbyte>& data, jint offset, jint length) {
    if (!data.p) throwNullPointer();
    if (offset < 0 || length < 0 || length > data.length() - offset) throwIndex(offset, data.length());
    return imageFromPng(reinterpret_cast<const std::uint8_t*>(data.p->data) + offset, static_cast<std::size_t>(length));
}

Ref<Image> Image::createRGBImage(const Arr<jint>& rgb, jint width, jint height, jboolean processAlpha) {
    if (!rgb.p) throwNullPointer();
    if (width <= 0 || height <= 0) throwJava("java.lang.IllegalArgumentException", "image size");
    if (static_cast<jlong>(width) * height > rgb.length()) throwIndex(width * height, rgb.length());
    Ref<Image> image = jnew<Image>();
    image->width = width;
    image->height = height;
    image->hasAlpha = processAlpha;
    image->pixels.resize(static_cast<std::size_t>(width) * static_cast<std::size_t>(height));
    for (std::size_t i = 0; i < image->pixels.size(); ++i) {
        std::uint32_t v = static_cast<std::uint32_t>(rgb.p->data[i]);
        image->pixels[i] = processAlpha ? v : (v | 0xff000000u);
    }
    return image;
}

Ref<Graphics> Image::getGraphics() {
    if (!mutableImage) throwJava("java.lang.IllegalStateException", "immutable image");
    return jnew<Graphics>(Ref<Image>(this));
}

void Image::getRGB(const Arr<jint>& out, jint offset, jint scanLength, jint x, jint y, jint w, jint h) {
    if (!out.p) throwNullPointer();
    if (x < 0 || y < 0 || w < 0 || h < 0 || x + w > width || y + h > height) throwJava("java.lang.IllegalArgumentException", "getRGB area");
    for (jint row = 0; row < h; ++row)
        for (jint col = 0; col < w; ++col)
            out[offset + row * scanLength + col] = static_cast<jint>(pixels[static_cast<std::size_t>((y + row) * width + x + col)]);
}

// ---------------------------------------------------------------- draw recording (render interpolation)

// One simulation tick of draw calls, tagged by layer. Between ticks the host replays them with each
// layer moved part of the way back towards where it was drawn on the previous tick. Nothing in the
// game is re-run, so collision, animation, timers and random numbers stay on the original tick.
namespace {
enum OpKind { OpFillRect, OpDrawRect, OpDrawLine, OpFillArc, OpDrawArc, OpFillTriangle, OpImage, OpRgb };
struct DrawOp {
    OpKind kind;
    int layer;
    jint a, b, c, d, e, f;      // already translated to surface coordinates
    jint sx, sy, transform;
    jint clipX, clipY, clipW, clipH;
    std::uint32_t color;
    Ref<Image> image;
    std::vector<std::uint32_t> pixels;
    bool alpha;
};
struct Layer {
    int kind;                   // 0 screen, 1 world, 2 tiles, 3 actor
    int id;                     // which tile layer (they scroll at different rates)
    const Object* key;
    Ref<Object> hold;           // keeps an actor alive (and its address unique) while it is a key
    jint x, y;                  // where the anchor of the layer was drawn
    jint clipX, clipY, clipW, clipH;
};
struct Recording {
    std::vector<DrawOp> ops;
    std::vector<Layer> layers;
    bool valid = false;
};
struct Recorder {
    Graphics* target = nullptr;
    bool replaying = false;
    Recording work, current, previous;
} g_rec;

inline bool recordingTo(const Graphics* g) { return g_rec.target == g && !g_rec.replaying; }

void pushLayer(int kind, const Ref<Object>& key, jint x, jint y, int id = 0) {
    if (!g_rec.target) return;
    Layer l;
    l.kind = kind;
    l.id = id;
    l.key = key.p;
    l.hold = key;
    l.x = x;
    l.y = y;
    l.clipX = g_rec.target->clipX;
    l.clipY = g_rec.target->clipY;
    l.clipW = g_rec.target->clipW;
    l.clipH = g_rec.target->clipH;
    if (kind == 3) {
        // An actor inherits the clip of the world layer it is drawn in, whatever the previous actor left behind.
        for (auto it = g_rec.work.layers.rbegin(); it != g_rec.work.layers.rend(); ++it)
            if (it->kind == 1) { l.clipX = it->clipX; l.clipY = it->clipY; l.clipW = it->clipW; l.clipH = it->clipH; break; }
    }
    g_rec.work.layers.push_back(l);
}

DrawOp& newOp(const Graphics& g, OpKind kind) {
    if (g_rec.work.layers.empty()) pushLayer(0, nullptr, 0, 0);
    g_rec.work.ops.emplace_back();
    DrawOp& op = g_rec.work.ops.back();
    op.kind = kind;
    op.layer = static_cast<int>(g_rec.work.layers.size()) - 1;
    op.clipX = g.clipX;
    op.clipY = g.clipY;
    op.clipW = g.clipW;
    op.clipH = g.clipH;
    op.color = g.color;
    op.sx = op.sy = op.transform = 0;
    op.alpha = false;
    return op;
}
void recordOp(const Graphics& g, OpKind kind, jint a, jint b, jint c, jint d, jint e, jint f) {
    DrawOp& op = newOp(g, kind);
    op.a = a; op.b = b; op.c = c; op.d = d; op.e = e; op.f = f;
}
void recordImageOp(const Graphics& g, const Ref<Image>& image, jint sx, jint sy, jint w, jint h, jint transform, jint dx, jint dy) {
    DrawOp& op = newOp(g, OpImage);
    op.image = image;
    op.sx = sx; op.sy = sy; op.transform = transform;
    op.a = dx; op.b = dy; op.c = w; op.d = h; op.e = op.f = 0;
}
void recordRgbOp(const Graphics& g, const Arr<jint>& rgb, jint offset, jint scan, jint x, jint y, jint w, jint h, bool alpha) {
    DrawOp& op = newOp(g, OpRgb);
    op.a = x; op.b = y; op.c = w; op.d = h; op.e = op.f = 0;
    op.alpha = alpha;
    op.pixels.resize(static_cast<std::size_t>(w) * static_cast<std::size_t>(h));
    for (jint row = 0; row < h; ++row)
        for (jint col = 0; col < w; ++col)
            op.pixels[static_cast<std::size_t>(row * w + col)] = static_cast<std::uint32_t>(rgb[offset + row * scan + col]);
}
} // namespace

void PortNative::layerScreen() { pushLayer(0, nullptr, 0, 0); }
void PortNative::layerWorld(jint originX, jint originY) { pushLayer(1, nullptr, originX, originY); }
void PortNative::layerTiles(jint id, jint originX, jint originY) { pushLayer(2, nullptr, originX, originY, id); }
jint PortNative::settingCount(jint page) { return hostSettingCount(page); }
Ref<String> PortNative::settingName(jint page, jint index) { return jstr(hostSettingName(page, index)); }
Ref<String> PortNative::settingValue(jint page, jint index) { return jstr(hostSettingValue(page, index)); }
void PortNative::settingChange(jint page, jint index, jint direction) { hostSettingChange(page, index, direction); }
jboolean PortNative::settingCapturing() { return hostSettingCapturing(); }
void PortNative::settingsClosed() { hostSettingsClosed(); }
void PortNative::layerActor(const Ref<Object>& actor, jint screenX, jint screenY) { pushLayer(3, actor, screenX, screenY); }

void recorderStart(Graphics* target) {
    g_rec.target = target;
    g_rec.work.ops.clear();
    g_rec.work.layers.clear();
    g_rec.work.valid = false;
}
void recorderFinish() {
    if (!g_rec.target) return;
    g_rec.target = nullptr;
    g_rec.work.valid = true;
    std::swap(g_rec.previous, g_rec.current);
    std::swap(g_rec.current, g_rec.work);
}
void recorderDrop() {
    g_rec.target = nullptr;
    g_rec.work = Recording();
    g_rec.current = Recording();
    g_rec.previous = Recording();
}
bool recorderHasFrame() { return g_rec.current.valid; }

bool recorderReplay(double alpha, Graphics& out) {
    if (!g_rec.current.valid) return false;
    const Recording& cur = g_rec.current;
    const Recording& prev = g_rec.previous;
    const double back = alpha >= 1.0 ? 0.0 : alpha <= 0.0 ? 1.0 : 1.0 - alpha;
    // How far each layer moved on screen since the previous tick; large jumps (passages, restarts,
    // scene cuts, respawns) are shown at their new place immediately.
    std::vector<std::pair<jint, jint>> shift(cur.layers.size(), std::make_pair(0, 0));
    if (prev.valid && back > 0.0) {
        for (std::size_t i = 0; i < cur.layers.size(); ++i) {
            const Layer& l = cur.layers[i];
            if (l.kind == 0) continue;
            for (const Layer& p : prev.layers) {
                if (p.kind != l.kind || p.key != l.key || p.id != l.id) continue;
                const jint dx = p.x - l.x, dy = p.y - l.y;
                const jint limit = l.kind == 3 ? 48 : 64;
                if (dx >= -limit && dx <= limit && dy >= -limit && dy <= limit)
                    shift[i] = std::make_pair(static_cast<jint>(std::lround(dx * back)), static_cast<jint>(std::lround(dy * back)));
                break;
            }
        }
    }
    g_rec.replaying = true;
    out.tx = out.ty = 0;
    for (const DrawOp& op : cur.ops) {
        const Layer& l = cur.layers[static_cast<std::size_t>(op.layer)];
        const jint dx = shift[static_cast<std::size_t>(op.layer)].first, dy = shift[static_cast<std::size_t>(op.layer)].second;
        // A clip the layer inherited (the room/screen) stays put; a clip an actor set for itself moves with it.
        const bool ownClip = op.clipX != l.clipX || op.clipY != l.clipY || op.clipW != l.clipW || op.clipH != l.clipH;
        if (ownClip && (dx || dy)) {
            out.setClip(op.clipX + dx, op.clipY + dy, op.clipW, op.clipH);
            out.clipRect(l.clipX, l.clipY, l.clipW, l.clipH);
        } else {
            out.setClip(op.clipX, op.clipY, op.clipW, op.clipH);
        }
        out.color = op.color;
        switch (op.kind) {
            case OpFillRect: out.fillRect(op.a + dx, op.b + dy, op.c, op.d); break;
            case OpDrawRect: out.drawRect(op.a + dx, op.b + dy, op.c, op.d); break;
            case OpDrawLine: out.drawLine(op.a + dx, op.b + dy, op.c + dx, op.d + dy); break;
            case OpFillArc: out.fillArc(op.a + dx, op.b + dy, op.c, op.d, op.e, op.f); break;
            case OpDrawArc: out.drawArc(op.a + dx, op.b + dy, op.c, op.d, op.e, op.f); break;
            case OpFillTriangle: out.fillTriangle(op.a + dx, op.b + dy, op.c + dx, op.d + dy, op.e + dx, op.f + dy); break;
            case OpImage: out.drawRegion(op.image, op.sx, op.sy, op.c, op.d, op.transform, op.a + dx, op.b + dy, 0); break;
            case OpRgb: {
                Image* dst = out.target.get();
                for (jint row = 0; row < op.d; ++row) {
                    const jint py = op.b + dy + row;
                    if (py < std::max(0, out.clipY) || py >= std::min(dst->height, out.clipY + out.clipH)) continue;
                    for (jint col = 0; col < op.c; ++col) {
                        const jint px = op.a + dx + col;
                        if (px < std::max(0, out.clipX) || px >= std::min(dst->width, out.clipX + out.clipW)) continue;
                        const std::uint32_t src = op.pixels[static_cast<std::size_t>(row * op.c + col)];
                        std::uint32_t& d = dst->pixels[static_cast<std::size_t>(py * dst->width + px)];
                        const std::uint32_t a = op.alpha ? (src >> 24) : 255;
                        if (a == 255) d = src | 0xff000000u;
                        else if (a) {
                            const std::uint32_t inv = 255 - a;
                            d = 0xff000000u | ((((src >> 16) & 0xff) * a + ((d >> 16) & 0xff) * inv + 127) / 255 << 16)
                                | ((((src >> 8) & 0xff) * a + ((d >> 8) & 0xff) * inv + 127) / 255 << 8)
                                | (((src & 0xff) * a + (d & 0xff) * inv + 127) / 255);
                        }
                    }
                }
                break;
            }
        }
    }
    g_rec.replaying = false;
    return true;
}

// ---------------------------------------------------------------- Graphics

static inline void blend(std::uint32_t& dst, std::uint32_t src) {
    std::uint32_t a = src >> 24;
    if (a == 255) { dst = src; return; }
    if (a == 0) return;
    std::uint32_t inv = 255 - a;
    std::uint32_t r = (((src >> 16) & 0xff) * a + ((dst >> 16) & 0xff) * inv + 127) / 255;
    std::uint32_t g = (((src >> 8) & 0xff) * a + ((dst >> 8) & 0xff) * inv + 127) / 255;
    std::uint32_t b = ((src & 0xff) * a + (dst & 0xff) * inv + 127) / 255;
    dst = 0xff000000u | (r << 16) | (g << 8) | b;
}

Graphics::Graphics(const Ref<Image>& image) : target(image) {
    clipW = image->width;
    clipH = image->height;
}

void Graphics::setClip(jint x, jint y, jint w, jint h) {
    clipX = x + tx;
    clipY = y + ty;
    clipW = w < 0 ? 0 : w;
    clipH = h < 0 ? 0 : h;
}

void Graphics::clipRect(jint x, jint y, jint w, jint h) {
    jint x1 = std::max(clipX, x + tx), y1 = std::max(clipY, y + ty);
    jint x2 = std::min(clipX + clipW, x + tx + w), y2 = std::min(clipY + clipH, y + ty + h);
    clipX = x1;
    clipY = y1;
    clipW = std::max(0, x2 - x1);
    clipH = std::max(0, y2 - y1);
}

namespace {
struct Bounds { jint x1, y1, x2, y2; };
inline Bounds bounds(const Graphics& g) {
    Image* t = g.target.get();
    Bounds b;
    b.x1 = std::max(0, g.clipX);
    b.y1 = std::max(0, g.clipY);
    b.x2 = std::min(t->width, g.clipX + g.clipW);
    b.y2 = std::min(t->height, g.clipY + g.clipH);
    return b;
}
}

void Graphics::plot(jint x, jint y) {
    Bounds b = bounds(*this);
    if (x < b.x1 || y < b.y1 || x >= b.x2 || y >= b.y2) return;
    blend(target.get()->pixels[static_cast<std::size_t>(y * target.get()->width + x)], color);
}

void Graphics::span(jint x1, jint x2, jint y) {
    Bounds b = bounds(*this);
    if (y < b.y1 || y >= b.y2) return;
    x1 = std::max(x1, b.x1);
    x2 = std::min(x2, b.x2 - 1);
    if (x1 > x2) return;
    std::uint32_t* row = target.get()->pixels.data() + static_cast<std::size_t>(y) * static_cast<std::size_t>(target.get()->width);
    if ((color >> 24) == 255) std::fill(row + x1, row + x2 + 1, color);
    else for (jint x = x1; x <= x2; ++x) blend(row[x], color);
}

void Graphics::fillRect(jint x, jint y, jint w, jint h) {
    if (w <= 0 || h <= 0) return;
    x += tx;
    y += ty;
    if (recordingTo(this)) recordOp(*this, OpFillRect, x, y, w, h, 0, 0);
    for (jint row = y; row < y + h; ++row) span(x, x + w - 1, row);
}

void Graphics::drawRect(jint x, jint y, jint w, jint h) {
    if (w < 0 || h < 0) return;
    x += tx;
    y += ty;
    if (recordingTo(this)) recordOp(*this, OpDrawRect, x, y, w, h, 0, 0);
    span(x, x + w, y);
    if (h > 0) span(x, x + w, y + h);
    for (jint row = y + 1; row < y + h; ++row) {
        plot(x, row);
        if (w > 0) plot(x + w, row);
    }
}

void Graphics::drawLine(jint x1, jint y1, jint x2, jint y2) {
    x1 += tx; x2 += tx; y1 += ty; y2 += ty;
    if (recordingTo(this)) recordOp(*this, OpDrawLine, x1, y1, x2, y2, 0, 0);
    if (y1 == y2) { span(std::min(x1, x2), std::max(x1, x2), y1); return; }
    jint dx = std::abs(x2 - x1), dy = -std::abs(y2 - y1);
    jint sx = x1 < x2 ? 1 : -1, sy = y1 < y2 ? 1 : -1;
    jint err = dx + dy;
    for (jint guard = 0; guard < 100000; ++guard) {
        plot(x1, y1);
        if (x1 == x2 && y1 == y2) break;
        jint e2 = 2 * err;
        if (e2 >= dy) { err += dy; x1 += sx; }
        if (e2 <= dx) { err += dx; y1 += sy; }
    }
}

static bool angleInside(double degrees, jint start, jint arc) {
    if (arc >= 360 || arc <= -360) return true;
    double a = std::fmod(degrees - start, 360.0);
    if (a < 0) a += 360.0;
    return arc >= 0 ? a <= arc : a >= 360.0 + arc || a == 0;
}

void Graphics::fillArc(jint x, jint y, jint w, jint h, jint start, jint arc) {
    if (w <= 0 || h <= 0 || arc == 0) return;
    x += tx;
    y += ty;
    if (recordingTo(this)) recordOp(*this, OpFillArc, x, y, w, h, start, arc);
    const double rx = w / 2.0, ry = h / 2.0, cx = x + rx, cy = y + ry;
    for (jint py = y; py < y + h; ++py) {
        jint runStart = 0;
        bool inRun = false;
        for (jint px = x; px <= x + w; ++px) {
            bool inside = false;
            if (px < x + w) {
                double dx = (px + 0.5 - cx) / rx, dy = (py + 0.5 - cy) / ry;
                if (dx * dx + dy * dy <= 1.0) inside = angleInside(std::atan2(-dy, dx) * 180.0 / 3.14159265358979323846, start, arc);
            }
            if (inside && !inRun) { runStart = px; inRun = true; }
            else if (!inside && inRun) { span(runStart, px - 1, py); inRun = false; }
        }
    }
}

void Graphics::drawArc(jint x, jint y, jint w, jint h, jint start, jint arc) {
    if (w < 0 || h < 0 || arc == 0) return;
    x += tx;
    y += ty;
    if (recordingTo(this)) recordOp(*this, OpDrawArc, x, y, w, h, start, arc);
    const double rx = w / 2.0, ry = h / 2.0, cx = x + rx, cy = y + ry;
    if (arc > 360) arc = 360;
    if (arc < -360) arc = -360;
    const int steps = std::max(16, (w + h) * 4);
    jint lastX = INT32_MIN, lastY = INT32_MIN, firstX = INT32_MIN, firstY = INT32_MIN;
    for (int i = 0; i <= steps; ++i) {
        double angle = (start + static_cast<double>(arc) * i / steps) * 3.14159265358979323846 / 180.0;
        jint px = static_cast<jint>(std::floor(cx + rx * std::cos(angle) + 0.5));
        jint py = static_cast<jint>(std::floor(cy - ry * std::sin(angle) + 0.5));
        if (px == lastX && py == lastY) continue;
        if (i > 0 && px == firstX && py == firstY) continue;
        if (firstX == INT32_MIN) { firstX = px; firstY = py; }
        lastX = px;
        lastY = py;
        plot(px, py);
    }
}

void Graphics::fillTriangle(jint x1, jint y1, jint x2, jint y2, jint x3, jint y3) {
    x1 += tx; x2 += tx; x3 += tx; y1 += ty; y2 += ty; y3 += ty;
    if (recordingTo(this)) recordOp(*this, OpFillTriangle, x1, y1, x2, y2, x3, y3);
    if (y1 > y2) { std::swap(x1, x2); std::swap(y1, y2); }
    if (y2 > y3) { std::swap(x2, x3); std::swap(y2, y3); }
    if (y1 > y2) { std::swap(x1, x2); std::swap(y1, y2); }
    auto edge = [](jint xa, jint ya, jint xb, jint yb, jint y) -> double {
        if (yb == ya) return xa;
        return xa + static_cast<double>(xb - xa) * (y - ya) / (yb - ya);
    };
    for (jint y = y1; y <= y3; ++y) {
        double a = edge(x1, y1, x3, y3, y);
        double lo = a, hi = a;
        if (y <= y2 && y2 != y1) { double b = edge(x1, y1, x2, y2, y); lo = std::min(lo, b); hi = std::max(hi, b); }
        if (y >= y2 && y3 != y2) { double b = edge(x2, y2, x3, y3, y); lo = std::min(lo, b); hi = std::max(hi, b); }
        if (y == y2) { lo = std::min(lo, static_cast<double>(x2)); hi = std::max(hi, static_cast<double>(x2)); }
        span(static_cast<jint>(std::floor(lo + 0.5)), static_cast<jint>(std::floor(hi + 0.5)), y);
    }
}

void Graphics::drawImage(const Ref<Image>& image, jint x, jint y, jint anchor) {
    if (!image.p) throwNullPointer();
    drawRegion(image, 0, 0, image.get()->width, image.get()->height, 0, x, y, anchor);
}

void Graphics::drawRegion(const Ref<Image>& image, jint sx, jint sy, jint w, jint h, jint transform, jint dx, jint dy, jint anchor) {
    if (!image.p) throwNullPointer();
    Image* src = image.get();
    if (w <= 0 || h <= 0) return;
    if (sx < 0 || sy < 0 || sx + w > src->width || sy + h > src->height) throwJava("java.lang.IllegalArgumentException", "drawRegion source");
    const bool swap = transform >= 4;
    const jint dw = swap ? h : w, dh = swap ? w : h;
    if (anchor & 1) dx -= dw / 2;
    else if (anchor & 8) dx -= dw;
    if (anchor & 2) dy -= dh / 2;
    else if (anchor & 32) dy -= dh;
    dx += tx;
    dy += ty;
    if (recordingTo(this)) recordImageOp(*this, image, sx, sy, w, h, transform, dx, dy);
    Bounds b = bounds(*this);
    jint x1 = std::max(dx, b.x1), y1 = std::max(dy, b.y1), x2 = std::min(dx + dw, b.x2), y2 = std::min(dy + dh, b.y2);
    if (x1 >= x2 || y1 >= y2) return;
    Image* dst = target.get();
    const bool alpha = src->hasAlpha;
    for (jint py = y1; py < y2; ++py) {
        std::uint32_t* row = dst->pixels.data() + static_cast<std::size_t>(py) * static_cast<std::size_t>(dst->width);
        const jint oy = py - dy;
        for (jint px = x1; px < x2; ++px) {
            const jint ox = px - dx;
            jint ux, uy;
            switch (transform) {
                case 2: ux = w - 1 - ox; uy = oy; break;
                case 1: ux = ox; uy = h - 1 - oy; break;
                case 3: ux = w - 1 - ox; uy = h - 1 - oy; break;
                case 5: ux = oy; uy = h - 1 - ox; break;
                case 6: ux = w - 1 - oy; uy = ox; break;
                case 7: ux = w - 1 - oy; uy = h - 1 - ox; break;
                case 4: ux = oy; uy = ox; break;
                default: ux = ox; uy = oy; break;
            }
            std::uint32_t s = src->pixels[static_cast<std::size_t>((sy + uy) * src->width + sx + ux)];
            if (alpha) blend(row[px], s);
            else row[px] = s | 0xff000000u;
        }
    }
}

void Graphics::drawRGB(const Arr<jint>& rgb, jint offset, jint scanLength, jint x, jint y, jint w, jint h, jboolean processAlpha) {
    if (!rgb.p) throwNullPointer();
    if (w <= 0 || h <= 0) return;
    x += tx;
    y += ty;
    if (recordingTo(this)) recordRgbOp(*this, rgb, offset, scanLength, x, y, w, h, processAlpha);
    Bounds b = bounds(*this);
    Image* dst = target.get();
    for (jint row = 0; row < h; ++row) {
        jint py = y + row;
        if (py < b.y1 || py >= b.y2) continue;
        for (jint col = 0; col < w; ++col) {
            jint px = x + col;
            if (px < b.x1 || px >= b.x2) continue;
            std::uint32_t s = static_cast<std::uint32_t>(rgb[offset + row * scanLength + col]);
            std::uint32_t& d = dst->pixels[static_cast<std::size_t>(py * dst->width + px)];
            if (processAlpha) blend(d, s);
            else d = s | 0xff000000u;
        }
    }
}


// ---------------------------------------------------------------- audio and saves

void Player::start() {
    if (state == 0) throwJava("java.lang.IllegalStateException", "closed player");
    hostAudioStart(data, midi, loops);
    state = 400;
}
void Player::stop() {
    if (state == 400) {
        hostAudioStop();
        state = 300;
    }
}
void Player::close() {
    if (state == 400) hostAudioStop();
    state = 0;
}
jint Player::getState() {
    if (state == 400 && !hostAudioPlaying()) state = 300;
    return state;
}

Ref<Player> Manager::createPlayer(const Ref<InputStream>& stream, const Ref<String>& type) {
    if (!stream.p) throwNullPointer();
    Ref<Player> player = jnew<Player>();
    for (;;) {
        jint b = stream->read();
        if (b < 0) break;
        player->data.push_back(static_cast<std::uint8_t>(b));
    }
    player->midi = !type.p || type.get()->s.find(u"wav") == std::u16string::npos;
    return player;
}

Ref<RecordStore> RecordStore::openRecordStore(const Ref<String>& name, jboolean) {
    Ref<RecordStore> store = jnew<RecordStore>();
    store->name = jutf8(name);
    while (!store->name.empty() && store->name[0] == '/') store->name.erase(0, 1);
    store->present = hostLoadSave(store->name, store->record);
    return store;
}
jint RecordStore::addRecord(const Arr<jbyte>& data, jint offset, jint length) {
    setRecord(1, data, offset, length);
    return 1;
}
void RecordStore::setRecord(jint, const Arr<jbyte>& data, jint offset, jint length) {
    if (!data.p) throwNullPointer();
    if (offset < 0 || length < 0 || length > data.length() - offset) throwIndex(offset, data.length());
    record.assign(reinterpret_cast<const std::uint8_t*>(data.p->data) + offset, reinterpret_cast<const std::uint8_t*>(data.p->data) + offset + length);
    present = true;
    if (!hostStoreSave(name, record)) throwJava("javax.microedition.rms.RecordStoreException", "save failed");
}
jint RecordStore::getRecord(jint, const Arr<jbyte>& out, jint offset) {
    if (!present) throwJava("javax.microedition.rms.InvalidRecordIDException", "");
    if (!out.p) throwNullPointer();
    if (offset < 0 || static_cast<jint>(record.size()) > out.length() - offset) throwIndex(offset, out.length());
    if (!record.empty()) std::memcpy(out.p->data + offset, record.data(), record.size());
    return static_cast<jint>(record.size());
}
Arr<jbyte> RecordStore::getRecord(jint) {
    if (!present) throwJava("javax.microedition.rms.InvalidRecordIDException", "");
    Arr<jbyte> a = Arr<jbyte>::make(static_cast<jint>(record.size()));
    if (!record.empty()) std::memcpy(a.p->data, record.data(), record.size());
    return a;
}

Ref<String> GloftSOFD::getAppProperty(const Ref<String>& key) {
    if (key.p && key.get()->s == u"MIDlet-Version") return jstr(u"1.0.1");
    return nullptr;
}
void GloftSOFD::notifyDestroyed() { hostQuit(); }

} // namespace sod
