#include "window_buffer.h"
#include <iostream>
#include <stdexcept>
#include <vector>

void require(bool condition, const char* message) {
    if (!condition) throw std::runtime_error(message);
}
int main() {
    try {
        HDC screen = GetDC(nullptr);
        require(screen != nullptr, "Screen DC unavailable");
        soul::WindowBuffer target, frame;
        // Exercise portrait/widescreen, letterboxing, repeated paints and resize.
        for (int logicalWidth : {240, 569, 240}) {
            const int width = logicalWidth * 2 + 40, height = 680;
            HDC destination = target.prepare(screen, width, height);
            HDC offscreen = frame.prepare(screen, width, height);
            require(destination && offscreen, "Cannot allocate window buffers");
            for (int paint = 0; paint < 30; ++paint) {
                const COLORREF previous = paint % 2 ? RGB(16, 96, 192) : RGB(192, 96, 16);
                const COLORREF next = paint % 2 ? RGB(192, 96, 16) : RGB(16, 96, 192);
                HBRUSH oldBrush = CreateSolidBrush(previous);
                RECT client{0, 0, width, height};
                FillRect(destination, &client, oldBrush); DeleteObject(oldBrush);
                FillRect(offscreen, &client, static_cast<HBRUSH>(GetStockObject(BLACK_BRUSH)));
                BITMAPINFO info{};
                info.bmiHeader.biSize = sizeof(BITMAPINFOHEADER);
                info.bmiHeader.biWidth = logicalWidth; info.bmiHeader.biHeight = -320;
                info.bmiHeader.biPlanes = 1; info.bmiHeader.biBitCount = 32;
                const unsigned pixel = paint % 2 ? 0xffc06010u : 0xff1060c0u;
                const std::vector<unsigned> pixels(static_cast<size_t>(logicalWidth) * 320, pixel);
                StretchDIBits(offscreen, 20, 20, logicalWidth * 2, 640, 0, 0,
                    logicalWidth, 320, pixels.data(), &info, DIB_RGB_COLORS, SRCCOPY);
                RECT overlay{30, 30, 40, 40};
                FillRect(offscreen, &overlay, static_cast<HBRUSH>(GetStockObject(WHITE_BRUSH)));
                require(GetPixel(destination, 0, 0) == previous && GetPixel(destination, 100, 100) == previous,
                    "Composing a frame changed the visible target before presentation");
                require(frame.present(destination), "Frame copy failed");
                require(GetPixel(destination, 0, 0) == RGB(0, 0, 0), "Letterbox not presented");
                require(GetPixel(destination, 100, 100) == next, "Game canvas not presented");
                require(GetPixel(destination, 35, 35) == RGB(255, 255, 255), "Overlay not presented with canvas");
            }
        }
        require(frame.prepare(screen, 0, 0) == nullptr, "Empty/minimized client must skip presentation");
        require(frame.prepare(screen, 240, 320) != nullptr, "Restore after empty client failed");
        ReleaseDC(nullptr, screen);
        std::cout << "PASS: 90 buffered GDI paints, canvas/bars/overlay, resize and minimized client\n";
        return 0;
    } catch (const std::exception& e) { std::cerr << e.what() << '\n'; return 1; }
}
