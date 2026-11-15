package javax.microedition.lcdui;

/** Oracle-only Canvas: the fixed 240x320 portrait screen both builds target; never repaints itself. */
public abstract class Canvas extends Displayable {
    protected Canvas() {}
    protected abstract void paint(Graphics g);
    public int getWidth() { return 240; }
    public int getHeight() { return 320; }
    public void setFullScreenMode(boolean mode) {}
    public final void repaint() {}
    public final void serviceRepaints() {}
    protected void keyPressed(int key) {}
    protected void keyReleased(int key) {}
    protected void keyRepeated(int key) {}
    protected void pointerPressed(int x, int y) {}
    protected void pointerReleased(int x, int y) {}
    protected void pointerDragged(int x, int y) {}
    protected void hideNotify() {}
    protected void showNotify() {}
}
