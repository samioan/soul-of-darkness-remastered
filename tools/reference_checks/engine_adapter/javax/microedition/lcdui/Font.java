package javax.microedition.lcdui;

public final class Font {
    private static final Font INSTANCE = new Font();
    private Font() {}
    public static Font getFont(int face, int style, int size) { return INSTANCE; }
}
