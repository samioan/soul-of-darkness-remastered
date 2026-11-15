package com.nokia.mid.ui;

import javax.microedition.lcdui.Graphics;

public class DirectUtils {
    private static final DirectGraphics INSTANCE = new DirectGraphics() { public void setARGBColor(int argb) {} };
    public static DirectGraphics getDirectGraphics(Graphics g) { return INSTANCE; }
}
