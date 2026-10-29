package javax.microedition.lcdui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/** Test-only PNG/RGB bridge. Not a MIDP runtime; no Canvas or Graphics emulation. */
public final class Image {
    private final BufferedImage image;
    private Image(BufferedImage image) { this.image = image; }
    public static Image createImage(byte[] data, int offset, int length) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(data, offset, length));
        if (image == null) throw new IOException("ImageIO could not decode sprite PNG");
        return new Image(image);
    }
    public static Image createRGBImage(int[] pixels, int width, int height, boolean alpha) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] data = pixels.clone();
        if (!alpha) for (int i = 0; i < width*height; ++i) data[i] |= 0xff000000;
        image.setRGB(0, 0, width, height, data, 0, width);
        return new Image(image);
    }
    public int getWidth() { return image.getWidth(); }
    public int getHeight() { return image.getHeight(); }
    public void getRGB(int[] pixels, int offset, int scanlength, int x, int y, int width, int height) {
        image.getRGB(x, y, width, height, pixels, offset, scanlength);
    }
}
