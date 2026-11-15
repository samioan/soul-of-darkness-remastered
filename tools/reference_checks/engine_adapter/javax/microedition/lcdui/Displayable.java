package javax.microedition.lcdui;

/** Oracle-only base class; the game never uses Displayable features itself. */
public abstract class Displayable {
    protected Displayable() {}
    public int getWidth() { return 240; }
    public int getHeight() { return 320; }
}
