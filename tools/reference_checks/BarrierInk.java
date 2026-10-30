/** Test-only draw-command sink; no Graphics or MIDP runtime emulation. */
public final class BarrierInk {
    public static int color;
    public static final java.util.List<int[]> lines=new java.util.ArrayList<>();
    public static void line(int x0,int y0,int x1,int y1){lines.add(new int[]{color,x0,y0,x1,y1});}
}
