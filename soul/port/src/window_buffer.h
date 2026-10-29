#pragma once
#include <windows.h>

namespace soul {
// Compose the entire client (including bars and desktop text) before the one
// visible copy. Keep the GDI surface between paints; recreate it on resize.
class WindowBuffer {
public:
    WindowBuffer() = default;
    WindowBuffer(const WindowBuffer&) = delete;
    WindowBuffer& operator=(const WindowBuffer&) = delete;
    ~WindowBuffer() { reset(); }
    HDC prepare(HDC target, int width, int height) {
        if (width <= 0 || height <= 0) return nullptr;
        if (width != width_ || height != height_) {
            reset();
            dc_ = CreateCompatibleDC(target);
            if (!dc_) return nullptr;
            bitmap_ = CreateCompatibleBitmap(target, width, height);
            if (!bitmap_) { reset(); return nullptr; }
            original_ = SelectObject(dc_, bitmap_);
            if (!original_ || original_ == HGDI_ERROR) { original_ = nullptr; reset(); return nullptr; }
            width_ = width; height_ = height;
        }
        return dc_;
    }
    bool present(HDC target) const {
        return dc_ && BitBlt(target, 0, 0, width_, height_, dc_, 0, 0, SRCCOPY) != 0;
    }
private:
    HDC dc_ = nullptr;
    HBITMAP bitmap_ = nullptr;
    HGDIOBJ original_ = nullptr;
    int width_ = 0, height_ = 0;
    void reset() {
        if (original_) SelectObject(dc_, original_);
        if (bitmap_) DeleteObject(bitmap_);
        if (dc_) DeleteDC(dc_);
        dc_ = nullptr; bitmap_ = nullptr; original_ = nullptr;
        width_ = height_ = 0;
    }
};
}
