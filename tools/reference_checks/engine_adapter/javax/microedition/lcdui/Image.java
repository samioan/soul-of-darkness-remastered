package javax.microedition.lcdui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/** Oracle-only Image: sizes come from ImageIO; pixels are never inspected by the game logic. */
public final class Image {
    private final int width, height;
    private final boolean mutable;
    private BufferedImage pixels;
    private int[] rgb;
    private Image(int width, int height, boolean mutable) { this.width = width; this.height = height; this.mutable = mutable; }
    private static Image decode(InputStream in) throws IOException {
        if (in == null) throw new IOException("missing image resource");
        BufferedImage image = ImageIO.read(in);
        if (image == null) throw new IOException("ImageIO could not decode PNG");
        Image result = new Image(image.getWidth(), image.getHeight(), false);
        result.pixels = image;
        return result;
    }
    public void getRGB(int[] out, int offset, int scan, int x, int y, int w, int h) {
        if (x < 0 || y < 0 || w < 0 || h < 0 || x + w > width || y + h > height) throw new IllegalArgumentException("getRGB area");
        for (int row = 0; row < h; row++)
            for (int col = 0; col < w; col++) {
                int v = pixels != null ? pixels.getRGB(x + col, y + row) : rgb != null ? rgb[(y + row) * width + x + col] : 0xffffffff;
                out[offset + row * scan + col] = v;
            }
    }
    public static Image createImage(String resource) throws IOException { return decode(Image.class.getResourceAsStream(resource)); }
    public static Image createImage(byte[] data, int offset, int length) {
        try { return decode(new ByteArrayInputStream(data, offset, length)); }
        catch (IOException e) { throw new IllegalArgumentException(e.getMessage()); }
    }
    public static Image createImage(int width, int height) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException();
        return new Image(width, height, true);
    }
    public static Image createRGBImage(int[] pixels, int width, int height, boolean alpha) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException();
        if ((long) width * height > pixels.length) throw new ArrayIndexOutOfBoundsException();
        Image result = new Image(width, height, false);
        result.rgb = new int[width * height];
        for (int i = 0; i < result.rgb.length; i++) result.rgb[i] = alpha ? pixels[i] : (pixels[i] | 0xff000000);
        return result;
    }
    public Graphics getGraphics() {
        if (!mutable) throw new IllegalStateException();
        return new Graphics(width, height);
    }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
