// The slice of MIDP 2.0 / Nokia UI that Soul of Darkness uses, drawn into software ARGB surfaces.
#pragma once
#include "jrt.h"

namespace sod {

struct Graphics;
struct Game;

struct Image : Object {
    jint width = 0, height = 0;
    bool mutableImage = false;
    bool hasAlpha = false;
    std::vector<std::uint32_t> pixels;
    static Ref<Image> createImage(const Ref<String>& resource);
    static Ref<Image> createImage(jint width, jint height);
    static Ref<Image> createImage(const Arr<jbyte>& data, jint offset, jint length);
    static Ref<Image> createRGBImage(const Arr<jint>& rgb, jint width, jint height, jboolean processAlpha);
    Ref<Graphics> getGraphics();
    jint getWidth() { return width; }
    jint getHeight() { return height; }
    void getRGB(const Arr<jint>& out, jint offset, jint scanLength, jint x, jint y, jint w, jint h);
};

struct Graphics : Object {
    Ref<Image> target;
    jint clipX = 0, clipY = 0, clipW = 0, clipH = 0;
    jint tx = 0, ty = 0;
    std::uint32_t color = 0xff000000u;
    explicit Graphics(const Ref<Image>& image);
    void setColor(jint rgb) { color = 0xff000000u | static_cast<std::uint32_t>(rgb); }
    void setColor(jint r, jint g, jint b) { setColor((r << 16) | (g << 8) | b); }
    jint getColor() { return static_cast<jint>(color & 0xffffffu); }
    void setClip(jint x, jint y, jint w, jint h);
    void clipRect(jint x, jint y, jint w, jint h);
    jint getClipX() { return clipX - tx; }
    jint getClipY() { return clipY - ty; }
    jint getClipWidth() { return clipW; }
    jint getClipHeight() { return clipH; }
    void translate(jint x, jint y) { tx += x; ty += y; }
    jint getTranslateX() { return tx; }
    jint getTranslateY() { return ty; }
    void fillRect(jint x, jint y, jint w, jint h);
    void drawRect(jint x, jint y, jint w, jint h);
    void drawLine(jint x1, jint y1, jint x2, jint y2);
    void fillArc(jint x, jint y, jint w, jint h, jint start, jint arc);
    void drawArc(jint x, jint y, jint w, jint h, jint start, jint arc);
    void fillTriangle(jint x1, jint y1, jint x2, jint y2, jint x3, jint y3);
    void drawImage(const Ref<Image>& image, jint x, jint y, jint anchor);
    void drawRegion(const Ref<Image>& image, jint sx, jint sy, jint w, jint h, jint transform, jint dx, jint dy, jint anchor);
    void drawRGB(const Arr<jint>& rgb, jint offset, jint scanLength, jint x, jint y, jint w, jint h, jboolean processAlpha);
    void drawString(const Ref<String>&, jint, jint, jint) {}
    void setFont(const Ref<Object>&) {}
    void plot(jint x, jint y);
    void span(jint x1, jint x2, jint y);
};

struct DirectGraphics : Object {
    Ref<Graphics> g;
    explicit DirectGraphics(const Ref<Graphics>& graphics) : g(graphics) {}
    void setARGBColor(jint argb) { g->color = static_cast<std::uint32_t>(argb); }
};

struct DirectUtils {
    static Ref<DirectGraphics> getDirectGraphics(const Ref<Graphics>& g) { return jnew<DirectGraphics>(g); }
};

struct Font : Object {
    static Ref<Font> getFont(jint, jint, jint) { return jnew<Font>(); }
};

// The device the game was built for: a 240x320 portrait canvas. Widescreen is a port feature layered
// on top (PortConfig), so the game itself always sees the original size.
struct Canvas : Object {
    virtual void paint(Ref<Graphics> g) = 0;
    virtual void keyPressed(jint) {}
    virtual void keyReleased(jint) {}
    virtual void keyRepeated(jint) {}
    virtual void pointerPressed(jint, jint) {}
    virtual void pointerReleased(jint, jint) {}
    virtual void pointerDragged(jint, jint) {}
    virtual void hideNotify() {}
    virtual void showNotify() {}
    virtual void sizeChanged(jint, jint) {}
    void repaint() {}
    void serviceRepaints() {}
    void setFullScreenMode(jboolean) {}
    jint getWidth() { return 240; }
    jint getHeight() { return 320; }
};
struct FullCanvas : Canvas {};

struct Player : Object {
    std::vector<std::uint8_t> data;
    bool midi = true;
    jint state = 100;
    jint loops = 1;
    void realize() { if (state < 200) state = 200; }
    void prefetch() { if (state < 300) state = 300; }
    void setLoopCount(jint n) { loops = n; }
    void start();
    void stop();
    void deallocate() { if (state == 300) state = 200; }
    void close();
    jint getState();
};

struct Manager {
    static Ref<Player> createPlayer(const Ref<InputStream>& stream, const Ref<String>& type);
};

struct RecordStore : Object {
    std::string name;
    std::vector<std::uint8_t> record;
    bool present = false;
    static Ref<RecordStore> openRecordStore(const Ref<String>& name, jboolean create);
    jint getNumRecords() { return present ? 1 : 0; }
    jint addRecord(const Arr<jbyte>& data, jint offset, jint length);
    void setRecord(jint id, const Arr<jbyte>& data, jint offset, jint length);
    jint getRecord(jint id, const Arr<jbyte>& out, jint offset);
    Arr<jbyte> getRecord(jint id);
    void closeRecordStore() {}
};

struct GloftSOFD : Object {
    static inline Ref<GloftSOFD> instance{};
    static inline Ref<Game> game{};
    Ref<String> getAppProperty(const Ref<String>& key);
    void destroyApp(jboolean unconditional);
    void notifyDestroyed();
};

// Gameloft's in-game advertising screens are intentionally absent from the port.
struct InGamePromotion {
    static jboolean a() { return false; }
    static jboolean d() { return false; }
    static jboolean a(jint) { return true; }
    static void a(jint, jint) {}
    static void a(const Ref<Graphics>&) {}
    static void a(const Ref<String>&, jint) {}
    static void b(const Ref<String>&, jint) {}
    static void a(const Ref<GloftSOFD>&, const Ref<Game>&, jint, jint) {}
    static void a(const Ref<Graphics>&, const Ref<Image>&, jint, jint, jint) {}
    static void b(const Ref<Graphics>&, const Ref<Image>&, jint, jint, jint) {}
};

// Layer markers the patched draw code emits for render interpolation (tools/port_java/PortNative.java
// is the compile-time stub). They only label draw calls; they never read or change game state.
struct PortNative {
    static void layerScreen();
    static void layerWorld(jint originX, jint originY);
    static void layerTiles(jint originX, jint originY) { layerTiles(0, originX, originY); }
    static void layerTiles(jint id, jint originX, jint originY);
    static void layerActor(const Ref<Object>& actor, jint screenX, jint screenY);
    // Widescreen: the sides of a level narrower than the screen, a blurred copy of what is drawn there.
    static void sides(const Ref<Graphics>& g, jint left, jint right, jint fade);
    // In-game options menu (full build): host settings by page and index.
    static jint settingCount(jint page);
    static Ref<String> settingName(jint page, jint index);
    static Ref<String> settingValue(jint page, jint index);
    static void settingChange(jint page, jint index, jint direction);
    static jboolean settingCapturing();
    static void settingsClosed();
};
void recorderStart(Graphics* target);
void recorderFinish();
void recorderDrop();
bool recorderHasFrame();
bool recorderReplay(double alpha, Graphics& out);

// Host hooks (implemented by the platform layer).
int hostSettingCount(int page);
std::string hostSettingName(int page, int index);
std::string hostSettingValue(int page, int index);
void hostSettingChange(int page, int index, int direction);
bool hostSettingCapturing();
void hostSettingsClosed();
int hostScreenWidth();
bool hostLoadSave(const std::string& name, std::vector<std::uint8_t>& out);
bool hostStoreSave(const std::string& name, const std::vector<std::uint8_t>& data);
void hostAudioStart(const std::vector<std::uint8_t>& data, bool midi, int loops);
void hostAudioStop();
bool hostAudioPlaying();
void hostQuit();
bool decodePng(const std::uint8_t* data, std::size_t size, int& width, int& height, std::vector<std::uint32_t>& argb, bool& alpha);

} // namespace sod
