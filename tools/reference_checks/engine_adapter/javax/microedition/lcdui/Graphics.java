package javax.microedition.lcdui;

/** Oracle-only Graphics: tracks clip/translation like the native runtime and draws nothing. */
public class Graphics {
    private int clipX, clipY, clipW, clipH, tx, ty;
    public Graphics(int width, int height) { clipW = width; clipH = height; }
    public void setColor(int rgb) {}
    public void setColor(int r, int g, int b) {}
    public void setClip(int x, int y, int w, int h) { clipX = x + tx; clipY = y + ty; clipW = Math.max(0, w); clipH = Math.max(0, h); }
    public int getClipX() { return clipX - tx; }
    public int getClipY() { return clipY - ty; }
    public int getClipWidth() { return clipW; }
    public int getClipHeight() { return clipH; }
    public void translate(int x, int y) { tx += x; ty += y; }
    public void fillRect(int x, int y, int w, int h) {}
    public void drawRect(int x, int y, int w, int h) {}
    public void drawLine(int x1, int y1, int x2, int y2) {}
    public void fillArc(int x, int y, int w, int h, int s, int a) {}
    public void drawArc(int x, int y, int w, int h, int s, int a) {}
    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {}
    public void drawImage(Image image, int x, int y, int anchor) { if (image == null) throw new NullPointerException(); }
    public void drawRegion(Image image, int sx, int sy, int w, int h, int transform, int dx, int dy, int anchor) {
        if (image == null) throw new NullPointerException();
        if (w <= 0 || h <= 0) return;
        if (sx < 0 || sy < 0 || sx + w > image.getWidth() || sy + h > image.getHeight()) throw new IllegalArgumentException("drawRegion source");
    }
    public void drawRGB(int[] rgb, int offset, int scan, int x, int y, int w, int h, boolean alpha) {
        if (rgb == null) throw new NullPointerException();
    }
    public void drawString(String s, int x, int y, int anchor) {}
    public void setFont(Font font) {}
}
