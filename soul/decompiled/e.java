import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import com.nokia.mid.ui.FullCanvas;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Random;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.rms.RecordStore;

final class e extends FullCanvas implements Runnable {
   static d a;
   private static d dl;
   private static d dm;
   public static d[] b;
   private static Image dn;
   private static Image do;
   public static Random c;
   public static Graphics d;
   public static DirectGraphics e;
   public static boolean f;
   static int g;
   public static final int[] h = new int[]{-16711936, -16733696, -16261377, -16759862};
   public static int[] i = new int[]{
      73, 120, 120, 73, 167, 120, 120, 167, 73, 120, 74, 120, 120, 74, 166, 120, 120, 166, 74, 120, 75, 120, 120, 75, 165, 120, 120, 165, 75, 120
   };
   public static int[] j = new int[]{64, 120, 120, 64, 176, 120, 120, 176};
   public static boolean k = true;
   public static boolean l = false;
   public static int m = -41;
   public static int n;
   public static int o = 5;
   public static int p = 60;
   public static int q = 60;
   public static int r = 0;
   public static int s = 60;
   public static int t = 60;
   public static int u = 0;
   public static int v = 0;
   public static long w = 0L;
   private static int dp;
   private static int dq;
   private static boolean dr;
   public static int x;
   public static int y;
   public static int z;
   public static long A;
   public static long B;
   public static boolean C = false;
   public static boolean D;
   static int E = -1;
   static int F;
   static int G;
   public static int H = 0;
   static final int[] I = new int[]{-1996488960, -2013224961, -1996554240};
   static final int[] J = new int[]{1157627648, 1149292543, 1157562368};
   public static int K = 0;
   public static int L;
   public static int M;
   public static int N;
   public static int O;
   public static int P;
   public static int Q;
   public static int R;
   public static int S;
   public static int T;
   public static int U;
   public static int V;
   public static int W = 0;
   public static int X = 0;
   public static int Y = 0;
   public static boolean Z;
   public static int[] aa = new int[4];
   public static boolean ab = false;
   public static boolean ac;
   public static boolean ad;
   public static boolean ae;
   public static boolean af;
   public static boolean ag;
   public static boolean ah;
   public static boolean ai;
   public static boolean aj;
   public static int ak;
   public static boolean al;
   public static int am = -1;
   public static int an = -1;
   public static boolean ao = true;
   public static int ap;
   public static int aq = -1;
   public static int ar = -1;
   public static int as;
   public static final int[] at = new int[]{4, 12, 12, 1, 4, 4};
   public static b au;
   public static b av;
   public static int aw = 0;
   public static int ax = 0;
   public static int ay = 0;
   static int[] az = new int[500];
   static int aA = -1;
   static int aB;
   static int aC;
   private static boolean ds;
   private static Image dt;
   private static d du;
   static boolean aD;
   public static int aE;
   static final int[] aF = new int[]{0, 0, 0, 3, 0, 2, 0, 0, 0};
   public static int aG;
   public static int aH = 0;
   public static boolean aI;
   public static boolean aJ;
   public static final int[] aK = new int[]{15488768, 16746240, 16760953, -19877, 16746240, 16736768};
   private static int dv;
   public static boolean aL;
   private static boolean dw;
   public static int aM;
   public static int aN;
   public static boolean aO;
   public static boolean aP = false;
   public static int aQ = 2;
   public static boolean aR = false;
   public static boolean aS = false;
   public static int aT;
   public static String aU;
   public static String aV;
   public static int aW;
   public static int aX;
   public static int aY;
   public static int aZ;
   public static int ba;
   public static int bb;
   public static int bc;
   public static b bd;
   public static final int[] be = new int[]{2631720, 6710886, 10066329, 9276813, 4737096, 3750201};
   public static final int[] bf = new int[]{41472, 63488, 4783944, 6029147, 33617920, 41472};
   public static final int[] bg = new int[]{15305728, 13930240, 15269753, 14942043, 13930240, 15305728};
   public static final int[] bh = new int[]{15466503, 16720128, 16747129, 16740443, 16720128, 16711684};
   private static int[] dx = new int[]{300, 350, 400, 500, 600, 700, 800, 900, 1000, 1200};
   private static int[] dy = new int[]{-65536, -570425345, -576192496};
   private static Image dz;
   private static Image dA;
   static byte[] bi = new byte[]{
      -1,
      -1,
      3,
      4,
      -1,
      -1,
      3,
      4,
      -1,
      -1,
      7,
      8,
      -1,
      -1,
      7,
      8,
      -1,
      -1,
      7,
      8,
      -1,
      -1,
      3,
      4,
      -1,
      -1,
      11,
      12,
      -1,
      -1,
      11,
      12,
      -1,
      -1,
      3,
      4,
      -1,
      -1,
      3,
      4,
      -1,
      -1,
      7,
      8,
      -1,
      -1,
      11,
      12
   };
   static byte[] bj = new byte[]{
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0
   };
   private static Image dB;
   private static Graphics dC;
   private static boolean dD;
   private static int dE;
   private static int dF;
   private static int dG;
   private static int dH;
   public static b bk;
   public static b bl;
   public static b bm;
   public static b bn;
   public static b bo;
   public static int bp = 250;
   public static int bq = 50;
   public static int br = 70;
   public static b[] bs = new b[bp];
   public static int bt;
   public static b[] bu = new b[bq];
   public static int bv;
   public static b[] bw = new b[br];
   public static int bx;
   public static int[] by = new int[bp];
   public static int bz = 0;
   public static int bA = 20;
   public static b[] bB = new b[bA];
   public static int bC;
   public static byte[] bD = new byte[bp];
   static short[] bE = new short[20];
   public static byte[] bF;
   private static String[] dI = new String[]{"/3", "/4", "/5", "/6", "/7", "/8", "/9", "/10", "/11", "/12", "/13", "/14"};
   private static int[] dJ = new int[53];
   public static short[][] bG = new short[9][30];
   public static short[] bH;
   public static int bI;
   public static short[] bJ = new short[9];
   public static int[] bK = new int[]{0};
   public static int[] bL = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
   public static int[] bM = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
   public static int[] bN = new int[]{2, 4};
   public static int[] bO = new int[]{6, 7, 5, 8};
   private static boolean dK;
   public static boolean bP;
   public static int bQ;
   public static int bR;
   public static int bS;
   public static int bT;
   private static byte[] dL;
   private static byte[] dM;
   private static byte[] dN;
   private static byte[] dO;
   private static byte[] dP;
   public static int bU;
   public static int bV;
   public static int bW;
   static int[] bX;
   public static int[][] bY;
   private static String[][] dQ = new String[11][];
   public static final int[] bZ = new int[]{31, 61, 64, 62};
   private static String dR;
   private static InputStream dS;
   private static int dT;
   private static short dU;
   private static int[] dV;
   private static short dW;
   private static short[] dX;
   private static int dY;
   private static byte[] dZ;
   private static int ea = 0;
   public static int ca;
   private static byte[][] eb = new byte[][]{{1, 4, 5, 6, 7, 8, 9}, {12, 13, 6, 7, 0, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9}};
   public static byte[][][] cb;
   public static short[] cc;
   public static short[] cd;
   public static int[][] ce;
   public static boolean cf;
   public static boolean cg;
   public static boolean ch = true;
   public static boolean ci = false;
   public static int cj;
   public static int[] ck = new int[3];
   public static byte[] cl = new byte[3];
   public static boolean cm = true;
   static final int[] cn = new int[]{6, 2, 3, 0, 2, 1, 1, 4, 4, 0, 0, 8, 1, 0, 4, 0, 4, 2, 0, 0, 1, 0};
   static int co = 0;
   static boolean cp = false;
   static boolean cq = false;
   static int cr = 0;
   static int cs = 0;
   static final int[] ct = new int[]{25, 22, 28, 31, 19};
   static boolean cu;
   static final int cv = 999 + bp + 64;
   static byte[] cw = new byte[cv];
   public static boolean cx = false;
   public static int cy;
   static int cz;
   static int cA;
   static int cB;
   static int cC;
   static int cD;
   static int cE;
   static int cF;
   static int cG;
   static int cH = 0;
   static boolean cI = false;
   static final int[] cJ = new int[]{2, 1, 1, 32};
   static short[] cK = new short[30];
   static byte[] cL = new byte[30];
   static short[] cM = new short[30];
   static byte[] cN = new byte[30];
   static byte[] cO = new byte[30];
   static boolean cP = false;
   static int cQ = 20;
   static boolean cR = false;
   static boolean cS = false;
   static int cT;
   static boolean cU = false;
   static final int[] cV = new int[]{-1996488705, -1426063361};
   public static final int[] cW = new int[]{-1428431909, -1145324613, 2004318071};
   public static int cX = 70;
   public static int cY = 70;
   public static int cZ = 0;
   public static int da = 180;
   public static int[] db = new int[]{872414976, 1728052992, -1711276288};
   public static int dc = 0;
   public static int dd = 0;
   public static int de = 0;
   private static int ec = 0;
   private static boolean ed = false;
   public static int df = 9;
   public static final int[] dg = new int[]{0, 2, 5, 6, 8, 9, 11};
   public static int dh;
   public static final byte[] di = new byte[]{-30, 0, 30, 0, -43, 0, 6, 0, 54, 0, 1, 0, -29, 0, 37, 0, 1, 0};
   public static final byte[][] dj = new byte[][]{{2, 6, 8}, {1, 4, 7, 9}, {4, 8}, {2, 6, 8}, {4, 8}};
   private static final byte[][] ee = new byte[][]{
      {-61, 0, -46, 0, -30, 0, -15, 0, 1, 0, 15, 0, 30, 0, 45, 0, 60, 0},
      {-58, 0, -43, 0, -29, 0, -8, 0, 6, 0, 22, 0, 37, 0, 54, 0, 62, 0},
      {-61, 0, -46, 0, -30, 0, -15, 0, 1, 0, 15, 0, 30, 0, 45, 0, 60, 0},
      {-58, 0, -43, 0, -29, 0, -8, 0, 6, 0, 22, 0, 37, 0, 54, 0, 62, 0},
      {-61, 0, -46, 0, -30, 0, -15, 0, 1, 0, 15, 0, 30, 0, 45, 0, 60, 0}
   };
   private static int ef = 0;
   private static int eg = 0;
   private static int eh = 2;
   private static boolean ei;
   private static int ej;
   private static Player ek;
   public static boolean dk = true;
   private static int el;
   private static int em = -1;
   private static int en;
   private static int eo = 20;
   private static byte[] ep;

   e() {
      int[] var10000 = new int[]{0, 1};
      D = false;
      c = new Random(System.currentTimeMillis());
      x = 0;
      y = -1;
      z = 0;
   }

   public final void paint(Graphics var1) {
      try {
         d = var1;
         e = DirectUtils.getDirectGraphics(d);
         z++;
         g = 83;
         switch (x) {
            case 0:
               ab();
               break;
            case 1:
               N();
               break;
            case 2:
               this.aq();
            case 3:
            case 4:
            case 10:
            case 11:
            case 13:
            case 16:
            case 17:
            case 21:
            case 22:
            case 24:
            case 25:
            case 26:
            case 28:
            case 29:
            case 31:
            default:
               break;
            case 5:
               R();
               break;
            case 6:
            case 20:
               ax();
               break;
            case 7:
               S();
               break;
            case 8:
               b.bk = d;
               X();
               break;
            case 9:
               Z();
               break;
            case 12:
               Q();
               break;
            case 14:
               T();
               break;
            case 15:
               ac();
               if (aM == aN) {
                  this.J();
               }
               break;
            case 18:
               as();
               break;
            case 19:
               av();
               break;
            case 23:
               O();
               break;
            case 27:
               j();
               break;
            case 30:
               P();
               break;
            case 32:
               G(dq);
         }

         d = null;
         A();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      f = true;
   }

   private static void H() {
      if (bk.f != 8 && (bk.f < 13 || bk.f > 25) && !aR) {
         l = false;
      } else {
         l = true;
         n = 0;
         aR = false;
      }

      if (l && m < 0) {
         m += 5;
         if (m > 0) {
            m = 0;
         }
      }

      if (!l && m > -41) {
         n = n + g;
         if (n >= 4000) {
            m -= 5;
         } else if (m < 0) {
            m += 5;
            if (m > 0) {
               m = 0;
            }
         }

         if (m < -41) {
            m = -41;
         }
      }
   }

   public static final void a(int var0, int var1, int var2, int var3, int var4, int var5) {
      b(var0);
      b(var2, var3, var5 * 61 / 60, var4);
      b(var1);
      b(var2 + 1, var3 + 1, var5 * 61 / 60 - 2, var4 - 2);
      b(var0);
      if (var5 > 0) {
         for (int var6 = 0; var6 <= (var5 * 61 / 60 - 2) / o; var6++) {
            d(var2 + var6 * o, var3, var2 + var6 * o, var3 + var4 - 1);
         }
      }
   }

   private static void I() {
      k = false;
      b[35].a(d, 0, 0, m, 0, 0, 0);
      b(15773760);
      c(56, 8 + m, q * 61 / 60 + 14, 8);
      b(0);
      b(70, 9 + m, q * 61 / 60, 7);
      a(1611800, 63488, 70, 10 + m, 5, p);
      b(8402944);
      c(70, 17 + m, q * 61 / 60, 1);
      b(15773760);
      c(56, 18 + m, t * 61 / 60 + 14, 8);
      b(0);
      b(70, 19 + m, t * 61 / 60, 7);
      b(2132128);
      b(70, 20 + m, s * 61 / 60, 5);
      b(3725552);
      b(71, 21 + m, s * 61 / 60 - 2, 3);
      a(2132128, 3725552, 70, 20 + m, 5, s);
      b[35].a(d, 2 + b.bK, -1, m, 0, 0, 0);
   }

   public static final void a() {
      if (b.bv != 2 && cm && aN < 5) {
         b[35].a(d, 5, 10, 307, 0, 0, 0);
      }
   }

   private static void G(int var0) {
      Y();
      k = true;
      e.setARGBColor(-587202560);
      a(9, 30, 222, 252);
      b(9, 30, 222, 252);
      b(15692292);
      c(9, 30, 221, 251);
      int var1 = var0 == 0 ? r : u;
      if (dr) {
         var1 = 4;
      }

      dp++;
      if (!dr && dp == 5) {
         if (var0 == 0) {
            if (var1 == 4) {
               r = 0;
               q += 10;
               if (q > 120) {
                  q = 120;
               }

               p = q;
               dr = true;
               k = true;
            } else {
               p += 10;
               if (p > q) {
                  p = q;
               }
            }
         } else if (var0 == 1) {
            if (var1 == 4) {
               u = 0;
               t += 10;
               if (t > 120) {
                  t = 120;
               }

               s = t;
               dr = true;
               k = true;
            } else {
               if (b.bu) {
                  s += 10;
               }

               if (s > t) {
                  s = t;
               }
            }
         }
      }

      for (int var2 = -1; var2 <= 1; var2++) {
         e.setARGBColor(h[(var2 == 0 ? 0 : 1) + var0 * 2]);
         d(55 + var2, 55 + var2, 65 - var2 << 1, 65 - var2 << 1, 0, 360);
         c(74 + var2, 74 + var2, 46 - var2 << 1, 46 - var2 << 1);
         d(74 + var2, 74 + var2, 46 - var2 << 1, 46 - var2 << 1, 0, 360);

         for (int var3 = 0; var3 < 4; var3++) {
            d(i[var3 * 2 + (var2 + 1) * 10], i[var3 * 2 + 1 + (var2 + 1) * 10], i[var3 * 2 + 2 + (var2 + 1) * 10], i[var3 * 2 + 3 + (var2 + 1) * 10]);
            b[35].a(d, 17, j[var3 * 2], j[var3 * 2 + 1], 0, 0, 0);
            if (var3 < var1) {
               b[33].z = 0;
               b[33].a(d, var0 == 0 ? 1 : 3, 0, j[var3 * 2], j[var3 * 2 + 1], 0, 0, 0);
            }
         }
      }

      a.z = 0;
      String var4;
      if (var1 < 4) {
         var4 = h(0, 56 + var0).replace('#', (char)(52 - var1));
      } else {
         var4 = h(0, 58 + var0);
      }

      a.a(d, var4, 120, 195, 17, 200, 0, -1);
      if (dp > 10) {
         if (z % 10 < 5) {
            a.a(d, h(0, 10), 120, 262, 17, 230, 0, -1);
         }

         if (B()) {
            x = 8;
            k = true;
            dr = false;
            cm = true;
         }
      }
   }

   protected final void hideNotify() {
      if (el >= 0 && ek != null && ek.getState() == 400) {
         em = el;
      }

      F();
      D = true;
   }

   protected final void showNotify() {
      k = true;
      D = false;
      if (x != 0 && x != 1 && x != 9 && x != 15) {
         z = 0;
      }

      if (x == 8 && !aS && !z()) {
         ca = 0;
         a(14);
      }

      if (em >= 0 && x != 14) {
         en = 3;
      }

      C();
   }

   private void J() {
      k = true;
      Y();
      g(0, 0, 240, 320);
      a("", h(0, 17));
      a(0, 0, 240, 320);
      a.a(d, h(0, 85), 120, 160, 17);
      if (C(327712)) {
         aN = 0;
         aM = 0;
         U();
      }
   }

   public static final void a(boolean var0) {
      dh = aG + (var0 ? 1 : 0);
      if (aG + (var0 ? 1 : 0) == 9) {
         dh = 0;
         df = 9;
      } else {
         while (dg[df - 9 + 1] <= dh) {
            df++;
         }
      }
   }

   public static final void a(int var0) {
      if (var0 == 9) {
         F();
         dv = aG == 0 ? 0 : f(0, 7);
         en = 0;
      } else if (var0 == 12) {
         F();
         E(-1);
      } else if (var0 == 15) {
         F();
         E(-1);
         aN = 1;
         aM = 160;
      } else if (var0 == 8 && x == 9) {
         en = 0;
         G();
      }

      if (var0 == 20) {
         dh = 0;
         dc = 0;
         ej = 240;
         df = 10;
         H = 12;
      }

      if (var0 == 19 || var0 == 20) {
         ea = 0;
         dd = 2;
         de = 0;
         H = 12;
         ae();
      }

      if (var0 == 6) {
         ej = 266;
         String var1 = h(0, 28);
         String var2;
         if ((var2 = GloftSOFD.a.getAppProperty("MIDlet-Version")) == null) {
            var2 = "0.0.0";
         }

         int var3;
         if ((var3 = var1.indexOf("X.X.X")) != -1) {
            dQ[0][28] = var1.substring(0, var3) + " " + var2 + var1.substring(var3 + 5);
         }

         dd = 0;
      }

      if (var0 == 2) {
         ca = 0;
         if (b(cw, 16) == 0) {
            ea = 0;
         } else {
            ea = 2;
            ca = 1;
         }

         switch (x) {
            case 4:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            default:
               F();
            case 2:
            case 3:
            case 5:
            case 6:
            case 7:
            case 18:
            case 19:
            case 20:
            case 30:
               D(-1);
               E(0);
         }
      }

      if (x == 2 && var0 != 2) {
         do = null;
      } else if (var0 == 2 && x != 2) {
         try {
            if (do == null) {
               do = Image.createImage("/new.png");
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      if (var0 == 14) {
         ea = 1;
      }

      if (var0 == 14 || var0 == 7) {
         F();
      }

      if (var0 == 9) {
         dn = null;
      }

      aJ = false;
      if (var0 == 13 || var0 == 12 || var0 == 14 || var0 == 31 || var0 == 15 || var0 == 32) {
         aJ = true;
      }

      if (var0 == 8 && (x == 14 || x == 12)) {
         G();
      }

      z = 0;
      y = x;
      x = var0;
      C();
   }

   public final void run() {
      while (x != 11) {
         B = System.currentTimeMillis();
         if (!D) {
            if ((x != 14 && x != 8 && x != 9 || x == 8 && aS) && (x != 5 || y != 14) && en > 0) {
               while (--en == 0) {
                  i(em, -1);
               }
            }

            f = false;
            this.repaint();

            while (!f) {
               try {
                  Thread.sleep(10L);
               } catch (Exception var4) {
               }
            }
         }

         B = Math.min(B, System.currentTimeMillis());
         A = System.currentTimeMillis() - B;
         boolean var1 = z();

         try {
            Thread.sleep(Math.max(1L, (var1 ? 83 : 83) - A));
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      GloftSOFD.a.destroyApp(true);
   }

   public final void b() {
      if (D) {
         D = false;
      }
   }

   public final void c() {
      D = true;
   }

   private static void i(int var0, int var1, int var2, int var3) {
      d.setClip(var0, var1, var2, var3);
   }

   public static final void a(int var0, int var1, int var2, int var3) {
      d.setClip(var0, var1, var2, var3);
   }

   public static final void b(int var0) {
      d.setColor(var0);
   }

   public static final void b(int var0, int var1, int var2, int var3) {
      d.fillRect(var0, var1, var2, var3);
   }

   public static final void c(int var0, int var1, int var2, int var3) {
      d.drawRect(var0, var1, var2, var3);
   }

   public static final void d(int var0, int var1, int var2, int var3) {
      d.drawLine(var0, var1, var2, var3);
   }

   public static final void a(Image var0, int var1, int var2, int var3) {
      d.drawImage(var0, var1, var2, var3);
   }

   public static final void b(int var0, int var1, int var2, int var3, int var4, int var5) {
      d.fillArc(var0, var1, var2, var3, var4, var5);
   }

   public static final void c(int var0, int var1, int var2, int var3, int var4, int var5) {
      d.fillTriangle(var0, var1, var2, var3, var4, var5);
   }

   public static final void d(int var0, int var1, int var2, int var3, int var4, int var5) {
      d.drawArc(var0, var1, var2, var3, var4, var5);
   }

   static final void d() {
      if (aU != null) {
         c(0);
         E++;
      } else {
         l();
      }
   }

   static final void a(int var0, boolean var1, int var2) {
      k = true;
      int var3 = F - L + 0;
      int var4 = G - M + 0;
      if (var1 || !aS) {
         E++;
      }

      if (E <= 5) {
         int var12 = (var4 << 8) / 5 * E >> 8;
         e.setARGBColor(I[var0]);
         b(var3 - 15, var4 - var12, 30, var12);
         e.setARGBColor(J[var0]);
         b(var3 - 15 - 16, var4 - var12, 16, var12);
         b(var3 + 15, var4 - var12, 16, var12);
      } else if (E <= 19 + var2) {
         int var9;
         if ((var9 = (E - 5 - var2 - 1) % 8) > 3) {
            var9 = 7 - var9;
         }

         int var11 = 3840 * var9 / 14 >> 8;
         int var14 = 2048 * var9 / 14 >> 8;
         e.setARGBColor(I[var0]);
         b(var3 - 15 + var11, 0, (15 - var11) * 2, var4);
         e.setARGBColor(J[var0]);
         b(var3 - 15 - (8 - var14) * 2 + var11, 0, (8 - var14) * 2, var4);
         b(var3 + 15 - var11, 0, (8 - var14) * 2, var4);
      } else if (E <= 33 + var2 * 2) {
         int var8 = E - 19 - 1 - var2 * 2;
         int var10 = 3840 * var8 / 14 >> 8;
         int var13 = 2048 * var8 / 14 >> 8;
         e.setARGBColor(I[var0]);
         b(var3 - 15 + var10, 0, (15 - var10) * 2, var4);
         e.setARGBColor(J[var0]);
         b(var3 - 15 - (8 - var13) * 2 + var10, 0, (8 - var13) * 2, var4);
         b(var3 + 15 - var10, 0, (8 - var13) * 2, var4);
      } else if (E <= 38 + var2 * 2) {
         int var5 = E - 33 - var2 * 2;
         int var6 = (var4 << 8) / 5;
         int var7 = var5 * var6 >> 8;
         e.setARGBColor(I[var0]);
         b(var3 - 1, 0, 2, var4 - var7);
      } else if (var1) {
         if (aU != null) {
            aS = false;
            f(14);
         } else {
            l();
         }
      } else {
         ci = false;
      }
   }

   public static final void e(int var0, int var1, int var2, int var3) {
      b(0);
      b(var0, var1, var2, var3);
      b(15244571);
      c(var0, var1, var2 - 1, var3 - 1);
      b(0);
      c(var0 + 1, var1 + 1, var2 - 2 - 1, var3 - 2 - 1);
      b(10374921);
      c(var0 + 2, var1 + 2, var2 - 4 - 1, var3 - 4 - 1);
   }

   public static final void c(int var0) {
      int var1;
      if ((var1 = E - var0) <= 8) {
         boolean var2 = false;
         boolean var3 = false;
         boolean var4 = false;
         e(120 - 15 * var1, 160 - 20 * var1, 15 * var1 * 2, 4 * var1);
      } else {
         e(0, 0, 240, 75);
         a.z = 0;
         a.a(d, aU, 15, 7, 20, 210, ba, 3);
         b[35].a(d, 4, z % 6 >> 1, 228, 63, 0, 0, 0);
         if (C(327712) || C(33024)) {
            if (bb > 3) {
               bb -= 3;
               ba += 3;
            } else {
               aU = null;
               C();
            }
         }
      }

      a("", h(0, 17));
   }

   public static final void e() {
      int var0 = K;
      i(0, 0, 240, 320);
      boolean var1 = false;
      boolean var2 = false;
      if (var0 <= 4) {
         int var3 = aW + (120 - aW) * var0 / 5;
         int var4 = aX + (37 - aX) * var0 / 5;
         int var5 = 24 * var0;
         int var6 = 7 * var0;
         e(var3 - var5, var4 - var6, var5 * 2, var6 * 2);
      } else {
         boolean var7 = aY != -1 && aY <= 2;
         e(0, 0, 240, 75);
         if (aZ == 0) {
            a.a(d, aV, var7 ? 50 : 5, 7, 20);
            a.z = 0;
            a.a(d, aU, var7 ? 50 : 5, 23, 20, var7 ? 175 : 210, ba, 3);
         } else {
            if (var7) {
               b[-1].a(d, aY, 237, 4, 1, 0, 0);
            }

            a.a(d, aV, 5, 7, 20);
            a.z = 0;
            a.a(d, aU, 5, 23, 20, var7 ? 175 : 210, ba, 3);
         }

         b[35].a(d, 4, z % 6 >> 1, aZ == 0 ? 230 : 190, 63, 0, 0, 0);
      }

      K++;
      if (x != 14 && (C(327712) || C(33024))) {
         if (bb > 3) {
            bb -= 3;
            ba += 3;
            return;
         }

         aU = null;
         l();
         C();
      }
   }

   public static final void f(int var0, int var1, int var2, int var3) {
      if (H >= 0) {
         if (H <= 240) {
            H += var3;
            int var15 = 240 - H;
            int var6 = 60928 - 54 * H << 16 & 0xFF000000;
            e.setARGBColor(var6);
            int var7 = var1 - var15;
            int var8 = var2 - var15;
            int var9 = 0 + 2 * var15;
            int var10 = 0 + 2 * var15;
            if (var0 >= 0 && var0 <= 2) {
               b(var7, var8, var9, var10, 0, 360);
            }

            int var11 = var15 >> 2;
            if (var0 != 4 && var0 != 1) {
               e.setARGBColor(-9150);
            } else {
               e.setARGBColor(-10895105);
            }

            a(0, 0, 240, 320);

            for (int var14 = 0; var14 < 20; var14++) {
               int var12 = f(0, var11 >> 1);
               int var13 = var11 + var12;
               b(var1 - var13, var2 - var13, var13 * 2, var13 * 2, 360 * var14 / 20 + 9, 2);
            }

            a(0, 0, 240, 320);
         } else {
            if (var0 == 3) {
               if (b.d(b.bR[b.bO[b.bK]])) {
                  b.cA = 0;
                  b.cC = 2;
                  bk.b(16);
                  b.aN = b.bQ[b.bO[0]];
                  b.aM = new b[b.aN];

                  for (int var4 = 0; var4 < b.aN; var4++) {
                     b.aM[var4] = bk.a(39, 1, 16, 0, -22, 302, false);
                     b.aM[var4].c |= 4096;
                     b.aM[var4].ax = var4;
                     b.aM[var4].aw = 600;
                     b.aM[var4].au = 0;
                     b.aM[var4].as = b.bO[b.bK];
                     b.aM[var4].at = 0;
                     b.aM[var4].W();
                     b.aM[var4].c |= 16;
                  }
               }

               if (bc != 14) {
                  l();
               }
            } else if (var0 == 4) {
               b.cA = 1;
               b.cC = 2;
               if (b.d(b.bR[b.bO[b.bK]])) {
                  bk.b(22);
                  if (b.aR == null) {
                     b.aR = new b[2];
                  }

                  if (b.aR[0] != null) {
                     b.aR[1] = b.aR[0];
                     if (b.aR[1].f == 8 || b.aR[1].f == 9 || b.aR[1].f == 10) {
                        b.aR[1].a(10);
                     }

                     b.aR[1].aw = 1000;
                  }

                  b.aR[0] = bk.a(39, 1, 7, bk.ak ? 10 : -10, -50, 302, false);
                  b.aR[0].c |= 16;
                  b.aR[0].G = b.aR[0].ak ? -2560 : 2560;
                  b.aR[0].W();
               }

               l();
            }

            H = -1;
         }
      }
   }

   public static final void a(int var0, int var1, int var2) {
      int var3 = 100 * (450 - b.aS) / 450;
      int var4 = 0;
      if (var0 == 1) {
         var4 = 5882111;
      } else {
         var4 = 16768066;
      }

      if (b.aS > 225) {
         var4 |= 255 * (450 - b.aS) / 225 << 24;
      } else {
         var4 |= 255 * (b.aS - 225) / 225 << 24;
      }

      e.setARGBColor(var4);

      for (int var7 = 0; var7 < 20; var7++) {
         int var5 = f(0, var3 >> 1);
         int var6 = var3 + var5;
         b(var1 - var6, var2 - var6, var6 * 2, var6 * 2, 360 * var7 / 20 + 9, 2);
      }
   }

   private static void K() {
      if (am == -1 || aF[aG] != 2) {
         M = 0;
         L = 0;
         O = 0;
         N = 0;
         Q = 0;
         P = 0;
         Z = false;
         V = 0;
         d(1);
      }
   }

   public static final void d(int var0) {
      boolean var1 = false;
      if (z()) {
         var1 = true;
      }

      if (bk != null) {
         ac = false;
         ad = false;
         ae = false;
         ag = false;
         af = false;
         if (am == -1 && !var1 && au != null) {
            if (L + 240 > au.j[2]) {
               ae = true;
               ac = true;
            }

            if (L < au.j[0]) {
               ad = true;
               ac = true;
            }

            if (M + 320 > au.j[3]) {
               ag = true;
               ac = true;
            }

            if (M < au.j[1]) {
               af = true;
               ac = true;
            }
         }

         if (ac) {
            if (!ai) {
               if (aB == 0) {
                  O = bk.L - 220;
               }
            } else {
               O = bk.L - 220;
               if (!af && !ag) {
                  if (bk.G >= 0 && (bk.G != 0 || !bk.ak)) {
                     N = R;
                  } else {
                     N = S - 240;
                  }
               } else if (af) {
                  O = T;
               } else {
                  O = U - 320;
               }

               ah = true;
            }

            ap = O + 100;
            if (ad || ae) {
               L = N;
            }

            if (af || ag) {
               M = O;
            }

            if (aB <= 0 && aC <= 0) {
               h();
            }
         } else {
            ah = false;
         }

         if (!Z && !var1) {
            boolean var2 = false;
            int var3 = 0;
            boolean var4 = false;
            boolean var5 = false;
            if (ah) {
               var3 = 1;
               var4 = true;
            }

            if (am != -1) {
               if (ak == 0 || ak == 3 || ak >= 4 && ao) {
                  var3 = 1;
               }

               if (aG != 3) {
                  var4 = true;
               }
            }

            if (aG == 9) {
               if (am != -1 && ak >= 4 && ao) {
                  var3 = 1;
                  var4 = false;
               }

               if (am != -1 && ak >= 4 && (!ao || al)) {
                  var3 = 1;
                  var4 = true;
                  al = false;
               }
            }

            if (!var3) {
               N = bk.ak ? bk.K - 160 : bk.K - 80;
               Y = X;
               X = 0;
               if (!bk.bB && b.aK == null) {
                  if (Math.abs(N - L) <= 0) {
                     if (Y != 2) {
                        X = 1;
                     } else {
                        X = 2;
                     }
                  } else {
                     X = 2;
                  }
               }
            }

            if (!var4) {
               aq = -1;
               bk.g();
               if (bk.j[1] <= ap) {
                  aq = 0;
               }

               if (bk.L >= ap + 120) {
                  aq = 1;
               }

               if ((bk.H < 0 || bk.c(true) || b.aK != null || bk.bU) && aq == 0 || (bk.H > 0 || bk.c(false) || b.aK != null || bk.bU) && aq == 1) {
                  if (aq == 0) {
                     ap = bk.j[1];
                  } else {
                     ap = bk.j[3] - 120;
                  }

                  O = ap - 100;
               }
            }

            V = 20;
            if ((var0 & 1) == 0 || am != -1 && aF[aG] == 2) {
               if (b.W && W != 1 && am == -1) {
                  if (b.X == 1) {
                     if (Math.abs((var3 = bk.ak ? bk.K - 160 : bk.K - 80) - L) < 40) {
                        if (N != var3) {
                           N = var3;
                           b.Y = 0;
                        } else {
                           b.X++;
                           b.W = false;
                        }
                     } else if (Math.abs(L - bk.K + 120) > 2) {
                        N = bk.K - 120;
                        b.Y = 1;
                     } else {
                        b.X++;
                        b.W = false;
                     }
                  } else if (b.X == 3) {
                     var3 = bk.ak ? bk.K - 160 : bk.K - 80;
                     if (Math.abs(L - var3) > 2) {
                        N = var3;
                        b.Y = 2;
                     } else {
                        b.X++;
                        b.X = 0;
                        b.W = false;
                     }
                  } else {
                     b.X = 0;
                     b.W = false;
                  }
               }

               if (bk.G == 0 && (var0 & 4) == 0 && b.aK == null && !bk.bU && am == -1 && !ab && !b.W) {
                  P = 0;
               } else if (b.W && am == -1) {
                  P = j(P, N - L, at[b.Y], at[b.Y + 3]);
               } else if (X == 2) {
                  P = j(P, N - L, 12, 2);
               } else {
                  P = j(P, N - L, V, 2);
               }

               if (X == 1 || X == 0 && Y == 1) {
                  P = 0;
               }

               Q = j(Q, O - M, 32, 2);
               if (am != -1 && ak == 1) {
                  Q = j(Q, O - M, 40, 15);
               }

               if (!ah && !b.cu) {
                  L = L + P;
               }

               M = M + Q;
               if (am != -1 && ak >= 4) {
                  if (ao) {
                     L = N;
                  } else {
                     M = O;
                  }
               }
            } else {
               N = bk.ak ? bk.K - 160 : bk.K - 80;
               if (N < 0) {
                  N = 0;
               }

               if (aF[aG] == 3 && aG != 9) {
                  O = 0;
               } else {
                  O = bk.L - 220;
               }

               if (O + 320 > bT) {
                  O = bT - 320;
               }

               if (O < 0) {
                  O = 0;
               }

               if (as == 0) {
                  M = O;
                  L = N;
                  P = 0;
                  Q = 0;
               } else {
                  P = j(P, N - L, V, 2);
                  Q = j(Q, O - M, 32, 2);
               }

               ap = O + 100;
            }
         }

         if (b.aJ && b.bH) {
            L = bk.K - 120;
            M = bk.L - 160;
         }

         if (var1) {
            L();
         } else {
            if ((var0 & 1) == 0 || as == 1 || ai) {
               h();
               as = 0;
            } else if (aC <= 0 && aB <= 0 && au != null) {
               g();
            }

            f();
         }
      }
   }

   public static final void f() {
      aa[0] = L;
      aa[1] = M;
      aa[2] = L + 240;
      aa[3] = M + 320;
   }

   public static final void g() {
      if (R > 0 && L < R) {
         L = R;
      } else if (S > 0 && L > S - 240) {
         L = S - 240;
      }

      if (T > 0 && M < T) {
         M = T;
      } else {
         if (U > 0 && M > U - 320) {
            M = U - 320;
         }
      }
   }

   public static final void h() {
      g();
      L();
      if (bk.K - 80 <= R && bk.G > 0 && am == -1 && aB <= 0) {
         L = R;
      } else {
         if (bk.K + 80 > S && bk.G < 0 && am == -1 && aB <= 0) {
            L = S - 240;
         }
      }
   }

   private static void L() {
      if (aC > 0) {
         int var0;
         var0 = (var0 = aC / 2) + aC % 2;
         if ((aC & 1) != 0) {
            var0 = -var0;
         }

         L += var0;
         aC--;
      }

      if (aB > 0) {
         int var2;
         if ((var2 = aB / 2) > 10) {
            var2 = 10;
         }

         var2 += aC % 2;
         if ((aB & 1) != 0) {
            var2 = -var2;
         }

         M += var2;
         aB--;
      }

      if (L < 0) {
         L = 0;
      } else if (L > bS - 240) {
         L = bS - 240;
      }

      if (M < 0) {
         M = 0;
      } else {
         if (M > bT - 320) {
            M = bT - 320;
         }
      }
   }

   private static int j(int var0, int var1, int var2, int var3) {
      if (var1 < 0) {
         var0 -= var3;
      } else {
         var0 += var3;
      }

      var1 /= 2;
      if (Math.abs(var0) < Math.abs(var1)) {
         var1 = var0;
      }

      if (var1 > var2) {
         var1 = var2;
      } else if (var1 < -var2) {
         var1 = -var2;
      }

      return var1;
   }

   public static final void a(b var0) {
      ai = false;
      if (au != var0) {
         aN = 160;
         aM = 0;
         av = au;
         au = var0;
         ai = true;
         aw = 7;
         ax = 34;
         ay = 2;
         cR = au.aw == 4;
         ar = 1;
         var0.g();
      }
   }

   static final void i() {
      while (aA > 0) {
         int var0 = az[aA--];
         dL[var0] = (byte)az[aA--];
      }
   }

   static final void e(int var0) {
      if (aB <= 0) {
         if (var0 > 0) {
            aB = var0;
            return;
         }

         aB = -var0;
      }
   }

   static final void a(int var0, int var1) {
      var0 = var0 >> 1 << 1;
      if (aC <= 0) {
         if (var0 > 0) {
            aC = var0;
         } else {
            aC = -var0;
         }
      }

      if (aB <= 0) {
         if (var1 > 0) {
            aB = var1;
            return;
         }

         aB = -var1;
      }
   }

   static final void a(String var0, String var1) {
      a(var0, var1, false);
   }

   static final void a(String var0, String var1, boolean var2) {
      String var3 = var0;
      var0 = var1;
      var1 = var3;
      if (!var2) {
         boolean var4 = false;
         a(0, 296, 240, 24);
         b(0);
         b(0, 296, 240, 24);
         b(15692292);
         c(0, 296, 239, 22);
      } else {
         a(0, 296, 240, 24);
         b(0);
         b(0, 296, 240, 24);
      }

      a.z = 0;
      if (var0 != null && var0 != "") {
         a.a(d, var0, 2, 304, 20);
      }

      if (var1 != null && var1 != "") {
         a.a(d, var1, 238, 304, 24);
      }
   }

   private static void M() {
      e.setARGBColor(-2013265920);
      a(0, 0, 240, 320);
      b(0, 0, 240, 320);
      e(0, 100, 240, 100);
      a.z = 0;
      switch (aE) {
         case 6:
            a.a(d, h(0, 14), 120, 150, 3, 210, 0, -1);
            break;
         case 7:
            a.a(d, h(0, 38), 120, 150, 3, 210, 0, -1);
      }

      a(h(0, 16), h(0, 15));
   }

   private static void N() throws Exception {
      a(0, 0, 240, 320);
      b(16777215);
      b(0, 0, 240, 320);

      try {
         if (z == 1 && dt == null) {
            dt = Image.createImage("/logo.png");
         }
      } catch (Exception var1) {
         var1.printStackTrace();
      }

      a(dt, 120, 160, 3);
      b(z, 47);
      if (z == 2) {
         af();
      } else if (z == 7) {
         ay();
      } else if (z == 12) {
         ag();
      } else {
         if (z == 17) {
            a.a(GloftSOFD.a, GloftSOFD.b, 240, 320);
            String var0;
            if ((var0 = GloftSOFD.a.getAppProperty("GL-Censored")) != null) {
               C = var0.trim().toLowerCase().compareTo("true") == 0;
               return;
            }

            if ((var0 = GloftSOFD.a.getAppProperty("BLOOD")) != null) {
               C = var0.trim().toLowerCase().compareTo("0") == 0;
               return;
            }
         } else {
            if (z == 22) {
               b(false);
               return;
            }

            if (z == 27) {
               O(0);
               return;
            }

            if (z == 32) {
               an();
               return;
            }

            if (z == 37) {
               ao();
               return;
            }

            if (z == 42) {
               return;
            }

            if (z >= 47) {
               dt = null;
               a(23);
               dk = false;
               return;
            }

            System.gc();
         }
      }
   }

   private static void a(int var0, boolean var1) {
      try {
         a("/1");
         if (var1) {
            du = N(1);
            du.a(var0, 0, -1, -1);
            dn = du.H[var0][0];
         }

         du.H[0][0] = null;
         du = null;
         r();
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   private static void O() {
      a(0, 0, 240, 320);
      b(0);
      b(0, 0, 240, 320);
      a.a(d, h(0, 22), 120, 160, 3);
      a(h(0, 16), h(0, 15));
      if (C(131072)) {
         dk = false;
         a(18);
      } else {
         if (C(262144)) {
            dk = true;
            a(18);
            D(-1);
            E(0);
         }
      }
   }

   private static void P() {
      dh = 0;
      df = 9;
      aG = 0;
      aH = 0;
      a(20);

      for (int var0 = 0; var0 < cw.length; var0++) {
         a(cw, var0, 0);
      }

      aj();
      a(cw, 32, (short)1);
      a(cw, 60, (short)q);
      a(cw, 62, (short)p);
      a(cw, 64, (short)r);
      a(cw, 66, (short)t);
      a(cw, 68, (short)s);
      a(cw, 70, (short)u);
      b(true);
      ak();
      b.bl = false;
      b.bm = false;
      b.bn = false;
   }

   private static void Q() {
      k = true;
      Y();
      a(0, 0, 240, 320);
      g(7, 20, 226, 265);
      a.z = 1;
      if (x == 12) {
         a.a(d, h(0, 84), 120, 160, 3);
      }

      a.z = 0;
      a.a(d, h(0, 27), 120, 192, 3, 230, 0, -1);
      a(h(0, 16), h(0, 15));
      if (C(131072)) {
         ai();
         b(true);
         a(false);
         a(19);
      } else {
         if (C(262144)) {
            E(-1);
            al();
         }
      }
   }

   private static void R() {
      a(0, 0, 240, 320);
      g(0, 0, 240, 320);
      a.z = 0;
      a.a(d, h(0, 7), 120, 25, 17);
      a.z = 0;
      a.a(d, h(0, 31 + ca), 120, 160, 3, 200, 0, -1);
      a(h(0, 18), h(0, 17));
      a.a(d, ca + 1 + "/" + 4, 120, 304, 17, 230, 0, -1);
      if (C(270400)) {
         ca = ++ca % 4;
         E(-1);
      }

      if (C(131072)) {
         a(y);
         ca = 0;
      }

      if (C(4112)) {
         ca = (--ca + 4) % 4;
         E(-1);
      }
   }

   public static final void j() {
      a(0, 0, 240, 320);
      b(0);
      b(0, 0, 240, 320);
      a.a(d, h(0, 37), 120, 160, 3, 230, 0, -1);
      a("", h(0, 17));
      if (C(327712)) {
         E(-1);
         if (a.a()) {
            a(7);
            boolean var0 = false;
            a.a(h(0, 25) + "...", 0);
            return;
         }

         a(2);
      }
   }

   private static void S() {
      a.a(d);
      byte var0 = 0;
      if (C(16388)) {
         var0 = 21;
         aD = false;
      } else if (C(33024)) {
         var0 = 32;
         aD = false;
      } else if (C(4112)) {
         var0 = 23;
         aD = false;
      } else if (C(8256)) {
         var0 = 24;
         aD = false;
      } else if (C(131072)) {
         var0 = 26;
         aD = false;
      } else if (C(327712)) {
         var0 = 25;
         aD = true;
      }

      if (a.a(var0) && !aD) {
         a(2);
      }
   }

   public static final void a(int var0, int var1, int var2, int var3, int var4) {
      a(0, 0, 240, 320);
      e.setARGBColor(-587202560);
      b(var0, var1, var2, var3);
      b(15692292);
      c(var0 - 1, var1 - 1, var2 + 1, var3 + 1);
   }

   private static void T() {
      Y();
      a(53, 80, 134, 157, 187);
      e(1, 120, 110);
      if (ds) {
         aE = 6;
         M();
      }

      H(1);
      if (!ds) {
         c(eb[1].length, false);
      }
   }

   public static final void a(int var0, int var1, int var2, int var3, int[] var4, int[] var5) {
      b(0);
      b(var0, var1 + 1, var2 + 1, 9);
      b(9794829);
      d(var0, var1, var0 + var2, var1);
      d(var0 - 1, var1 + 1, var0 - 1, var1 + 7 + 1);
      d(var0 + var2 + 1, var1 + 1, var0 + var2 + 1, var1 + 7 + 1);
      d(var0, var1 + 7 + 2, var0 + var2, var1 + 7 + 2);
      a(var0 + 1, var1 + 2, var2 - 2, var4);
      if (var3 > 2) {
         a(var0 + 1, var1 + 2, var3 - 2, var5);
      }
   }

   public static final void a(int var0, int var1, int var2, int[] var3) {
      for (int var4 = 0; var4 < var3.length; var4++) {
         b(var3[var4]);
         d(var0, var1 + var4, var0 + var2, var1 + var4);
      }
   }

   private static void U() {
      b.bC = false;
      a(cw, 644, (byte)(b.bC ? 1 : 0));
      if (aH < aG + 1) {
         aH = aG + 1;
      }

      aI = true;
      b.p();
      b(true);
      ai();
      a(true);
      a(19);
   }

   private static void V() {
      i(0, 0, 240, 320);
      bv = 0;

      for (int var1 = 0; var1 < bt; var1++) {
         if (bs[var1] != null && (bs[var1].c & 1024) == 0) {
            b var0;
            if ((var0 = bs[var1]).h()) {
               if (bs[var1].am != 22
                  && bs[var1].am != 49
                  && bs[var1].am != 20
                  && bs[var1].am != 40
                  && bs[var1].am != 23
                  && bs[var1].am != 59
                  && (bs[var1].c & 32768) == 0) {
                  f(var0);
               }
            } else if (bs[var1].aq) {
               bs[var1].f();
            }
         }
      }

      if (z()) {
         for (int var4 = 0; var4 < bC; var4++) {
            b var2;
            if (bB[var4] != null && ((var2 = bB[var4]).c & 128) == 0) {
               f(var2);
            }
         }
      }

      if ((bk.c & 128) == 0) {
         f(bk);
      }

      if (3 == aF[aG]) {
         if (bm != null && (bm.c & 128) == 0) {
            f(bm);
         }

         if (bn != null && (bn.c & 128) == 0) {
            f(bn);
         }
      }

      for (int var5 = 0; var5 < bv; var5++) {
         b var3;
         if ((var3 = bu[var5]) != null) {
            var3.u();
         }
      }
   }

   private static void f(b var0) {
      int var1 = 0;

      while (var1 < bv && bu[var1].an <= var0.an) {
         var1++;
      }

      if (bv >= bq) {
         for (int var2 = 0; var2 < bq; var2++) {
            System.out.println("paint out of bound: " + bu[var2].am);
         }
      }

      for (int var3 = bv; var3 > var1; var3--) {
         bu[var3] = bu[var3 - 1];
      }

      bu[var1] = var0;
      bv++;
   }

   private static void g(b var0) {
      if (bx >= br) {
         for (int var1 = 0; var1 < br; var1++) {
            System.out.println("update out of bound: " + bw[var1].am);
         }
      }

      bw[bx++] = var0;
   }

   private static boolean h(b var0) {
      if ((var0.c & 16) != 0) {
         return true;
      }

      if ((var0.c & 256) != 0 || (var0.c & 32) != 0) {
         return false;
      }

      if (au == null && var0.am == 22) {
         return true;
      }

      byte var1 = 2;
      if (var0.am == 40 || var0.am == 49) {
         var1 = 7;
         if (var0.am == 49 && b.b(var0.j, bk.j)) {
            return true;
         }
      } else if (var0.am == 78) {
         var1 = 14;
      } else if (var0.am == 45) {
         var1 = 4;
      } else if (var0.am == 10) {
         var1 = 4;
      } else if (var0.am == 37) {
         var1 = 3;
      } else if (var0.am == 89) {
         var1 = 3;
      }

      return var0.ai < var1;
   }

   public static final void k() {
      for (int var0 = 0; var0 < bt; var0++) {
         b var1;
         if ((var1 = bs[var0]) != null && var1.am == 22 && au != var1) {
            var1.s();
            if (ai) {
               return;
            }
         }
      }
   }

   private static void W() {
      bk.s();
      if (bk.ao != 0 || bk.f != 9) {
         if (b.aK != null && b.aK.am == 33 && bk.bD) {
            b.aK.H = 0;
         }

         if (ai) {
            for (int var1 = 0; var1 < bt; var1++) {
               b var0;
               if ((var0 = bs[var1]) != null) {
                  if (var0.am == 74) {
                     if (var0.s == null && var0.aB != -1) {
                        var0.s = g(var0.aB);
                     }

                     if (var0.s != null) {
                        if (var0.s == au) {
                           var0.c |= 16;
                           var0.c &= -33;
                           var0.n(0);
                        } else {
                           var0.c &= -17;
                           var0.c |= 32;
                        }
                     }
                  }

                  if (var0.X()) {
                     if (au != null && b.b(var0.j, au.j)) {
                        var0.c &= -1025;
                     } else if (av != null && b.b(var0.j, av.j)) {
                        var0.b(false);
                     }

                     if (bs[var1] != null) {
                        bs[var1].b((b)null);
                     }
                  } else if (var0.C()) {
                     if (var0.ao != 0) {
                        var0.b(false);
                     }
                  } else if (var0 != null && var0.al == -1 && var0.am != 84 && var0.am != 85 && (var0.am != 39 || var0.f != 13)) {
                     c(var0);
                  }
               }
            }

            ai = false;
         }

         bx = 0;

         for (int var4 = 0; var4 < bt; var4++) {
            b var2;
            if ((var2 = bs[var4]) != null && (!ai || var2.am != 22) && var2.am != 0) {
               var2.aq = false;
               if (h(var2)) {
                  g(var2);
               }
            }
         }

         for (int var5 = 0; var5 < bx; var5++) {
            b var3;
            if ((var3 = bw[var5]) != null && b.cq[0] != var3 && b.cq[1] != var3 && b.cq[2] != var3 && (!ai || var3.am != 22)) {
               var3.s();
               if (var3 != null && var3.r != null && !var3.r.aq) {
                  var3.r.s();
               }
            }
         }

         if (3 == aF[aG] && bm != null && bn != null) {
            bm.s();
            bn.s();
         } else {
            if (2 == aF[aG] && !aS) {
               b.cn = -512;
               b.cp = 0;

               for (int var6 = 0; var6 < 3; var6++) {
                  if (b.cq[var6] != null) {
                     b.cq[var6].K();
                  }
               }

               if (b.cn >= 0) {
                  b.cn = 0;
               }

               if (b.cp == 0) {
                  if (b.J()) {
                     if (b.cn > -768) {
                        if (b.co == 0) {
                           b.co++;
                        }

                        b.cn = b.cn + -128 * b.co;
                     } else {
                        b.cn = -768;
                        b.co = 0;
                     }
                  } else if (b.cn != 0) {
                     b.cn = -512;
                  }
               }

               for (int var7 = 0; var7 < 3; var7++) {
                  if (b.cq[var7] != null) {
                     b.cq[var7].s();
                  }
               }
            }
         }
      }
   }

   private static void X() {
      if (C(2048) && cI) {
         if (!b.aJ) {
            b.aJ = true;
            b.bI = true;
            v = 50000;
         } else {
            b.aJ = false;
            b.bI = false;
         }

         if (!b.aJ) {
            b.bH = false;
         }
      }

      if (b.aJ && C(1024)) {
         b.bH = !b.bH;
      }

      if (bc == 5) {
         cf = true;
      }

      if (z()) {
         x();
         if (b.cw != null) {
            b.cw.s();
         }

         if (b.bC && bm != null && bn != null && aG == 3) {
            bm.K = bn.K = bk.K;
            bm.L = bn.L = bk.L;
         }
      } else {
         if (cj > 0) {
            y();
         }

         W();
      }

      if (cg) {
         for (; z(); x()) {
            if (b.bC && bm != null && bn != null && aG == 3) {
               bm.K = bn.K = bk.K;
               bm.L = bn.L = bk.L;
            }
         }

         cg = false;
      }

      Y();
      if (C(262144) && !aS && !z()) {
         ca = 0;
         a(14);
      } else {
         if (b.aJ && aU == null) {
            a.z = 0;
            a.a(d, "is GOD MODULE!", 237, 20, 9);
         }

         if (ar != -1) {
            d(ar);
            ar = -1;
         } else {
            d(2);
         }
      }
   }

   private static void Y() {
      if (b.cu) {
         a(L + b.cv % 240, 0, true);
         d(L + b.cv % 240, 0, 2);
      } else if (aF[aG] == 3) {
         a(L, M < 0 ? 0 : M, true);
         d((L >> 3) % (bV * 20), M < 0 ? 0 : M, 2);
      } else {
         a(L, M, true);
         d(L, M < 0 ? 0 : M, 1);
      }

      a(0, 0, 240, 320);
      if (!z() && !b.bd && !b.bi && (b.E == null || b.E.ao != 3)) {
         if (bk.f == 18) {
            f(3, bk.K - L, bk.L - M - 30, 80);
         } else if (bk.f == 23) {
            f(4, bk.K - L + (bk.ak ? -40 : 40), bk.L - M - 50, 80);
         }
      }

      if (aS) {
         if (bc >= 6 && bc <= 8) {
            f(bc - 6, bk.K - L + (bk.ak ? -20 : 20), bk.L - M - 55, 40);
         } else if (bc == 13) {
            b.E.y();
         } else if (bc == 3) {
            a(aT, true, 0);
         } else if (bc == 10 || bc == 11) {
            a(0, true, 0);
         }
      }

      V();
      if (b.aS > 0) {
         a(b.bK, bk.K - L, bk.L - M + 0 + -80);
      }

      for (int var0 = 0; var0 < bv; var0++) {
         b var1;
         if ((var1 = bu[var0]) == null) {
            System.out.println("null actor in s_paintActors");
         }

         if (var1.q != null) {
            var1.q.h();
         }
      }

      D();
      if (aS && bc == 14) {
         i(0, 0, 240, 320);
         d();
      }

      if (w > 0L) {
         k = true;
         w = w - g;
         if (w <= 0L) {
            C();
         }

         b[35].a(d, 2, z % 4 < 2 ? 0 : 1, bk.K - L - 0, bk.L - M - 0, 0, 0, 0);
      }

      ac();
      if (bc == 5 && aS) {
         e();
      }

      if (aS && bc == 18) {
         aI = true;
         p = q;
         b.p();
         b(true);
         ai();
         cd = null;
         F();
         a(6);
         l();
      }

      if (z()) {
         a(bc != 17 && ch ? h(0, 20) : "", bc == 5 ? h(0, 17) : "", true);
      }

      if (aw > 0) {
         if (ay != 0) {
            ay--;
            e.setARGBColor(-16777216);
         } else {
            e.setARGBColor(aw * ax << 24);
         }

         b(0, 0, 240, 320);
         aw--;
      }

      if (!z() && aU == null && aN <= 0 && !aS && x == 8) {
         if (cm) {
            H();
            I();
         }

         a();
      } else {
         if (k && cm) {
            I();
         }

         a();
      }

      if (!z()) {
         ac();
      }

      if (!z() && b.E != null && (b.E.c & 128) == 0 && (b.E.c & 32) == 0) {
         c(b.E.ap, b.F);
      }
   }

   static final void g(int var0, int var1, int var2, int var3) {
      e(var0, var1, var2, var3);
   }

   static final void b(int var0, int var1) {
      a(60, 265, 120, var0 * 120 / var1, be, aK);
      if (a != null) {
         a.a(d, "" + var0 * 100 / var1 + "%", 120, 280, 17);
      }
   }

   private static void b(boolean var0, int var1) {
      a(0, 0, 240, 320);
      g(0, 0, 240, 320);
      b.a(120, 160, 120, 160, 60, 50, z * 5, true);
      a.z = 0;
      if (aG != 16) {
         String var2 = h(0, 62 + dv);
         a.a(d, var2, 120, 30, 17, 230, 0, -1);
      }

      if (z <= var1) {
         b(z, var1);
         aL = false;
      } else {
         if (z % 10 < 5 && var0) {
            a.a(d, h(0, 10), 120, 265, 17);
         }
      }
   }

   private static void Z() {
      b(true, 80);
      if (!aL && K(z)) {
         aL = true;
      }

      if (aL && B()) {
         K();
         aN = 160;
         aM = 0;
         a(8);
         k = true;
         aQ = 2;
      }

      System.gc();
   }

   private static void aa() {
      if (b(cw, 16) > 0) {
         q();
         bP = true;
      } else {
         aj();
         bP = true;
      }

      dw = false;
   }

   private static void H(int var0) {
      if (ds) {
         if (C(131072)) {
            ds = false;
            z = 0;
            C();
            return;
         }

         if (C(262144)) {
            if (eb[var0][ca] != 13) {
               E(-1);
            }

            ds = false;
            if (eb[var0][ca] == 9) {
               x = 11;
            } else if (eb[var0][ca] == 13) {
               al();
            } else if (eb[var0][ca] == 0) {
               ai();
               a(2);
            } else if (eb[var0][ca] == 1) {
               P();
            }

            C();
            return;
         }
      } else {
         if (C(131072)) {
            if (var0 == 1) {
               a(8);
               k = true;
            }

            C();
            return;
         }

         if (C(327712)) {
            switch (eb[var0][ca]) {
               case 0:
                  ds = true;
                  break;
               case 1:
                  if (b(cw, 16) > 0) {
                     ds = true;
                     aE = 7;
                  } else {
                     a(20);
                  }
                  break;
               case 2:
                  dn = null;
                  aG = a(cw, 14);
                  aH = a(cw, 15);
                  aI = b(cw, 24) == 1;
                  dK = true;
                  if (aI && aG < 8) {
                     aG++;
                     dK = false;
                  }

                  if (b(cw, 16) > 0) {
                     q();
                  } else {
                     aj();
                  }

                  a(9);
                  break;
               case 3:
                  aG = a(cw, 14);
                  df = 9;
                  dw = true;
                  a(aI);
                  if (ed) {
                     aH = 9;
                  } else {
                     aH = a(cw, 15);
                  }

                  a(19);
                  break;
               case 4:
                  if (a.a()) {
                     A(16);
                     a(cw, 10, (byte)cy);
                     b(true);
                     a(7);
                     boolean var2 = false;
                     a.a(h(0, 25) + "...", 0);
                  }
                  break;
               case 5:
                  if (a.d()) {
                     A(32);
                     a(cw, 10, (byte)cy);
                     b(true);
                     a(7);
                     boolean var1 = false;
                     a.b(h(0, 25) + "...", 0);
                  }
                  break;
               case 6:
                  dk = !dk;
                  z = 0;
                  if (!dk) {
                     F();
                  } else if (dk) {
                     if (var0 == 1) {
                        G();
                     } else {
                        i(0, -1);
                     }
                  }
                  break;
               case 7:
                  ca = 0;
                  E(-1);
                  a(5);
                  break;
               case 8:
                  a(6);
                  break;
               case 9:
                  ds = true;
                  aE = 6;
               case 10:
               case 11:
               default:
                  break;
               case 12:
                  a(8);
                  k = true;
                  break;
               case 13:
                  ds = true;
            }

            if (eb[var0][ca] != 12 && eb[var0][ca] != 6) {
               E(-1);
            }

            C();
         }
      }
   }

   private static void ab() throws Exception {
      b = new d[53];
      a(1);
   }

   private static void ac() {
      if (aO && z()) {
         b(0, 0, 240, aN);
         b(0, 320 - aN, 240, aN);
         aN += 20;
      } else if (aG == 8 && aP) {
         b(0, 0, 240, aN);
         b(0, 320 - aN, 240, aN);
         aN += 20;
      } else {
         if ((aQ < 0 || aS) && aM != aN) {
            int var0;
            if ((var0 = aM - aN) > -20 && var0 < 20) {
               aN = aM;
            } else {
               aN += var0 > 0 ? 20 : -20;
            }
         }

         if (aN > 0) {
            aQ--;
            i(0, 0, 240, 320);
            b(-587202560);
            b(0, 0, 240, aN);
            b(0, 320 - aN, 240, aN);
         } else {
            aQ = 2;
         }
      }
   }

   public static final void f(int var0) {
      cm = false;
      if (var0 == 18) {
         aS = true;
         bc = var0;
      }

      if (!aS) {
         aS = true;
         bc = var0;
         if (!ci) {
            E = 0;
         }

         if (var0 == 13 || var0 == 16) {
            for (int var1 = 0; var1 < 10; var1++) {
               b.cj[var1] = f(var1 * 36, var1 * 36 + 18);
               b.ck[var1] = f(5, 15);
            }

            bk.c |= 4096;
            bo.c |= 4096;
         }
      }
   }

   public static final void l() {
      aS = false;
      if (bc != 5) {
         cm = true;
      }

      if (bc == 10 || bc == 11) {
         x = 32;
         if (bc == 10) {
            r++;
            dq = 0;
         } else {
            u++;
            dq = 1;
         }

         dp = 0;
         dr = false;
         cm = false;
      }

      bc = -1;
      if (bd != null) {
         c(bd);
         bd = null;
      }

      aU = null;
      cf = false;
   }

   public static final void a(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = 6 * l(var4);
      int var8 = 6 * k(var4);
      int var9 = var0 << 8;
      int var10 = var1 << 8;
      boolean var11 = false;

      while (var10 < var3 << 8) {
         int var12 = var9 + var7;
         int var13 = var10 + var8;
         int var14 = var9 >> 8;
         int var15 = var10 >> 8;
         int var16 = var14;
         int var17 = var15 + 4;
         boolean var18 = false;
         if (var5 == 0 && var10 >= var6 && !var11) {
            var18 = true;
         }

         if (var18) {
            int var19;
            if ((var19 = z % 14) <= 9) {
               b[28].a(d, 13, 0, var14, var15 + 3, 0, 0, 0);
            } else {
               b[28].a(d, 13, var19 - 9, var14, var15 + 3, 0, 0, 0);
            }

            var11 = true;
            var13++;
         } else {
            b(16777215);
            d(var14, var15, var16, var17);
            b(13027014);
            d(var14 - 2, var15 + 2, var16 - 2, var17 + 2);
            d(var14 + 1, var15 + 2, var16 + 1, var17 + 2);
            b(7631988);
            d(var14 - 1, var15, var16 - 1, var17);
            b(6579300);
            d(var14 - 3, var15 + 2, var16 - 3, var17 + 2);
            d(var14 + 2, var15 + 2, var16 + 2, var17 + 2);
         }

         var9 = var12;
         var10 = var13;
      }
   }

   public static final void c(int var0, int var1) {
      if (aN <= 0) {
         int var2 = 13107200 / var1;
         int var3 = var0 * var2 >> 16;
         if (var0 == var1) {
            var3 = 200;
         }

         int[] var4 = bh;
         if (var3 >= 133) {
            var4 = bf;
         } else if (var3 >= 66) {
            var4 = bg;
         }

         a(20, 305, 200, var3, be, var4);
      }
   }

   public static final void b(int var0, int var1, int var2) {
      int var3 = var0 + var1;
      if (var0 <= 320 && var3 >= 0 && var2 > 0 && var2 <= 240) {
         if (var0 < 0) {
            var0 = 0;
         }

         if (var3 > 320) {
            var3 = 320;
         }

         for (int var4 = 0; var4 < 3; var4++) {
            int var5 = dy[f(0, 3)];
            e.setARGBColor(var5);
            int var6 = f(0, 2);
            int var7 = f(5, 7);
            int var8 = f(2, 4);
            int var9 = var0;
            int var10 = var2 - f(0, 3);
            int var11 = 0;
            int var12 = var0;
            if (var6 == 0) {
               while (var9 < var3 - 5 || var12 < var3 - 5) {
                  var12 = var9 + var7 * dx[f(0, 10)] / 1000;
                  var11 = var2 - var8 + f(-1, 2);
                  d(var10, var9, var11, var12);
                  if (f(0, 4) == 0) {
                     d(var11, var12, var11, var12 + 1);
                     var12++;
                  }

                  var9 = var12 + var7 * dx[f(0, 10)] / 1000;
                  var10 = var2 + var8 + f(-1, 2);
                  d(var11, var12, var10, var9);
                  if (f(0, 4) == 0) {
                     d(var10, var9, var10, var9 + 1);
                     var9++;
                  }
               }
            } else {
               while (var9 < var3 - 5 || var12 < var3 - 5) {
                  var12 = var9 + var7 * dx[f(0, 10)] / 1000;
                  var11 = var2 + var8 + f(-1, 2);
                  d(var10, var9, var11, var12);
                  if (f(0, 4) == 0) {
                     d(var11, var12, var11, var12 + 1);
                     var12++;
                  }

                  var9 = var12 + var7 * dx[f(0, 10)] / 1000;
                  var10 = var2 - var8 + f(-1, 2);
                  d(var11, var12, var10, var9);
                  if (f(0, 4) == 0) {
                     d(var10, var9, var10, var9 + 1);
                     var9++;
                  }
               }
            }
         }
      }
   }

   private static void j(int var0, int var1) throws Exception {
      if (var0 == 0) {
         a("/16");
      } else if (var0 != 1) {
         if (var0 == 2) {
            dl = N(bi[var1 * 4 + 2]);
         } else if (var0 == 3) {
            dm = N(bi[var1 * 4 + 3]);
         } else if (var0 == 4) {
            dD = true;
            r();
         } else if (var0 != 5 && var0 != 6) {
            if (var0 == 7) {
               byte var2 = bj[var1 * 4 + 2];
               dl.z = var2;
               dl.a(var2, 0, -1, -1);
               dl.a();
               dz = dl.H[var2][0];
            } else if (var0 == 8) {
               byte var3 = bj[var1 * 4 + 3];
               dm.z = var3;
               dm.a(var3, 0, -1, -1);
               dm.a();
               dA = dm.H[var3][0];
            }
         }
      }

      System.gc();
   }

   private static void ad() {
      dz = null;
      dA = null;
      System.gc();
   }

   private static void ae() {
      if (dB == null) {
         dB = Image.createImage(260, 340);
      }

      if (dC == null) {
         dC = dB.getGraphics();
      }

      dD = true;
   }

   private static void a(int var0, int var1, boolean var2) {
      if (var1 < 0) {
         var1 -= 20;
      }

      int var3;
      int var7 = var3 = var0 / 20;
      int var5;
      int var8 = var5 = (var0 + 240 - 1) / 20;
      int var4 = var1 / 20;
      int var6 = (var1 + 320 - 1) / 20;
      if (var1 < 0) {
         var1 += 20;
      }

      if (var3 < 0) {
         var3 = 0;
         var7 = 0;
      } else if (var3 > bU - 1 && !var2) {
         var3 = bU - 1;
      }

      if (var4 < 0) {
         var4 = 0;
      } else if (var4 > bW - 1) {
         var4 = bW - 1;
      }

      if (!dD && (var3 != dE || var5 != dG)) {
         if (var5 < dE || var3 > dG) {
            dD = true;
         } else if (var3 < dE) {
            var7 = dE;
            k(var3, var4, dE - 1, var6);
         } else if (var5 > dG) {
            var8 = dG;
            k(dG + 1, var4, var5, var6);
         }
      }

      if (!dD && (var4 != dF || var6 != dH)) {
         if (var6 < dF || var4 > dH) {
            dD = true;
         } else if (var4 < dF) {
            k(var7, var4, var8, dF - 1);
         } else if (var6 > dH) {
            k(var7, dH + 1, var8, var6);
         }
      }

      if (dD) {
         dD = false;
         k(var3, var4, var5, var6);
      }

      dE = var3;
      dF = var4;
      dG = var5;
      dH = var6;
      k(var0, var1);
   }

   private static void k(int var0, int var1, int var2, int var3) {
      for (int var6 = var1; var6 <= var3; var6++) {
         int var5 = var6 % 17;
         int var7 = var6;

         for (int var8 = var0; var8 <= var2; var8++) {
            int var4 = var8 % 13;
            a(dC, var4 * 20, var5 * 20, var8 % bU + var7 * bU);
         }
      }
   }

   private static void d(int var0, int var1, int var2) {
      boolean var5 = false;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      i(0, 0, 240, 320);
      boolean var3 = false;
      boolean var9 = false;
      boolean var4 = false;
      boolean var10 = false;
      boolean var11 = false;
   }

   private static void k(int var0, int var1) {
      if (var1 < 0) {
         var1 = var1 % 340 + 340;
      }

      int var2 = var0 % 260;
      int var4 = var1 % 340;
      int var3 = (var0 + 240) % 260;
      int var5 = (var1 + 320) % 340;
      if (var3 > var2) {
         if (var5 > var4) {
            e(var2, var4, 240, 320, 0, 0);
         } else {
            e(var2, var4, 240, 320 - var5, 0, 0);
            e(var2, 0, 240, var5, 0, 320 - var5);
         }
      } else if (var5 > var4) {
         e(var2, var4, 240 - var3, 320, 0, 0);
         e(0, var4, var3, 320, 240 - var3, 0);
      } else {
         e(var2, var4, 240 - var3, 320 - var5, 0, 0);
         e(var2, 0, 240 - var3, var5, 0, 320 - var5);
         e(0, var4, var3, 320 - var5, 240 - var3, 0);
         e(0, 0, var3, var5, 240 - var3, 320 - var5);
      }
   }

   private static void e(int var0, int var1, int var2, int var3, int var4, int var5) {
      i(var4, var5, var2, var3);
      a(dB, var4 - var0, var5 - var1, 0);
   }

   private static void a(Graphics var0, int var1, int var2, int var3) {
      if ((dM[var3] & 255) != 255) {
         byte var6 = 0;
         int var4 = dM[var3] & 255;
         int var5;
         if ((var5 = (dN[var3 >> 2] & 255) >> (3 - (var3 & 3) << 1) & 3) != 0) {
            if ((var5 & 1) != 0) {
               var6 = 2;
            }

            if ((var5 & 2) != 0) {
               var6 |= 1;
            }
         }

         int var7 = var4 * 20 % dz.getWidth();
         int var8 = var4 * 20 / dz.getWidth() * 20;
         var0.drawRegion(dz, var7, var8, 20, 20, var6, var1, var2, 20);
      }

      if ((dO[var3] & 255) != 255) {
         byte var11 = 0;
         int var9 = dO[var3] & 255;
         int var10;
         if ((var10 = (dP[var3 >> 2] & 255) >> (3 - (var3 & 3) << 1) & 3) != 0) {
            if ((var10 & 1) != 0) {
               var11 = 2;
            }

            if ((var10 & 2) != 0) {
               var11 |= 1;
            }
         }

         int var12 = var9 * 20 % dA.getWidth();
         int var13 = var9 * 20 / dA.getWidth() * 20;
         var0.drawRegion(dA, var12, var13, 20, 20, var11, var1, var2, 20);
      }
   }

   public static final void b(b var0) {
      var0.ae = -98;
      if (bz > 0) {
         bs[by[--bz]] = var0;
      } else if (bt < bp) {
         bs[bt++] = var0;
      } else {
         System.out.println("s_actors_num exceed maxnum" + bp);

         for (int var1 = 0; var1 < bt; var1++) {
            System.out.println(var1 + " actor == " + bs[var1].am + "   anim: " + bs[var1].f);
         }
      }
   }

   public static final void c(b var0) {
      if (var0 != null) {
         var0.b((b)null);
         var0.s = null;
         var0.t = null;
         var0.u = null;
         var0.v = null;
         var0.w = null;
         var0.x = null;
         var0.T = null;
         var0.U = null;
         if ((var0.c & 8192) != 0) {
            var0.c |= 1024;
         } else {
            if (b.aK == var0) {
               b.aK = null;
            }

            for (int var1 = 0; var1 < bt; var1++) {
               if (bs[var1] == var0) {
                  if (var0.ae != -98) {
                     bD[var0.ae] = -99;
                  }

                  if ((bs[var1].c & 512) != 0 && !b.cl) {
                     bH[bI++] = (short)bs[var1].al;
                  }

                  bs[var1].a();
                  bs[var1] = null;
                  by[bz++] = var1;
                  break;
               }
            }

            for (int var2 = 0; var2 < bx; var2++) {
               if (bw[var2] == var0) {
                  bw[var2] = null;
                  break;
               }
            }

            if (b.aR != null) {
               for (int var3 = 0; var3 < b.aR.length; var3++) {
                  if (b.aR[var3] == var0) {
                     b.aR[var3] = null;
                     break;
                  }
               }
            }

            if (b.aM != null) {
               for (int var4 = 0; var4 < b.aM.length; var4++) {
                  if (b.aM[var4] == var0) {
                     b.aM[var4] = null;
                     break;
                  }
               }
            }

            if (b.cx != null) {
               for (int var5 = 0; var5 < b.cx.length; var5++) {
                  if (b.cx[var5] == var0) {
                     b.cx[var5] = null;
                     break;
                  }
               }
            }

            if (b.E == var0) {
               b.E = null;
            }
         }
      }
   }

   public static final b d(b var0) {
      if (var0 == null) {
         return null;
      }

      for (int var1 = 0; var1 < bt; var1++) {
         if (bs[var1] != null && bs[var1].r == var0) {
            return bs[var1];
         }
      }

      return null;
   }

   public static final b g(int var0) {
      if (bk != null && bk.al == var0) {
         return bk;
      }

      for (int var1 = 0; var1 < bt; var1++) {
         if (bs[var1] != null && bs[var1].al == var0) {
            return bs[var1];
         }
      }

      if (bm != null && var0 == bm.al) {
         return bm;
      } else {
         return bn != null && var0 == bn.al ? bn : null;
      }
   }

   public static final void e(b var0) {
      if (bC >= bA) {
         System.out.print("s_cine_actors_num exceed maxnum" + bA);
      } else {
         bB[bC++] = var0;
      }
   }

   public static final void m() {
      for (int var0 = 0; var0 < bC; var0++) {
         bB[var0] = null;
      }

      bC = 0;
   }

   public static final b h(int var0) {
      for (int var1 = 0; var1 < bC; var1++) {
         if (bB[var1] != null && bB[var1].al == var0) {
            return bB[var1];
         }
      }

      return null;
   }

   public static final void a(b var0, int var1) {
      b.by[var1 * 18] = (byte)var0.f;
      b.by[var1 * 18 + 1] = (byte)var0.h;
      a(b.by, var1 * 18 + 2, (short)var0.K);
      a(b.by, var1 * 18 + 4, (short)var0.L);
      b(b.by, var1 * 18 + 6, var0.ao);
      b(b.by, var1 * 18 + 10, var0.c);
      b(b.by, var1 * 18 + 14, var0.aw);
   }

   public static final void b(b var0, int var1) {
      var0.f = b.by[var1 * 18];
      var0.h = b.by[var1 * 18 + 1];
      var0.K = b(b.by, var1 * 18 + 2);
      var0.L = b(b.by, var1 * 18 + 4);
      var0.ao = c(b.by, var1 * 18 + 6);
      var0.c = c(b.by, var1 * 18 + 10);
      var0.aw = c(b.by, var1 * 18 + 14);
   }

   private static boolean I(int var0) {
      return var0 == 35 || var0 == 23 || var0 == 48 || var0 == 82;
   }

   private static void c(boolean var0) {
      int var2 = 0;
      if (!var0) {
         for (int var3 = 0; var3 < bp; var3++) {
            bD[var3] = 0;
         }
      }

      int var10 = 0;

      while (var2 < bF.length) {
         byte var1 = bF[var2++];
         int var4 = var2;

         for (int var5 = 0; var5 < var1; var5++) {
            bE[var5] = (short)((bF[var2++] & 255) + (bF[var2++] << 8));
         }

         boolean var11 = false;

         for (int var6 = 0; var6 <= bI; var6++) {
            if (bH[var6] == bE[1]) {
               var11 = true;
               break;
            }
         }

         if (var11) {
            var10++;
         } else {
            b var12 = null;
            short var7 = bE[0];
            int var8 = 0;
            if (var7 != 0) {
               var8 = var10++;
               if (!var0) {
                  (var12 = new b(bE, var4, var1)).ae = var8;
               } else {
                  if (bD[var10 - 1] == -99 && I(var7)) {
                     continue;
                  }

                  bD[var10 - 1] = 0;
                  (var12 = new b(bE, var4, var1)).ae = var8;
               }
            }

            if (var7 == 0) {
               if (bk == null) {
                  bk = new b(bE, var4, var1);
               }
            } else if (var7 == 69) {
               if (bm == null) {
                  bm = new b(bE, var4, var1);
               } else if (bn == null) {
                  bn = new b(bE, var4, var1);
               }
            } else {
               b(var12);
               if (var12.am == 28 && bo == null) {
                  bo = var12;
               }

               bs[bt - 1].ae = var10 - 1;
               var12.a(bE);
            }
         }
      }
   }

   public static final void n() {
      for (int var0 = 0; var0 < bt; var0++) {
         if (bs[var0] != null) {
            bs[var0].a();
            bs[var0] = null;
         }
      }

      o();
      l();
      b.aO = null;
      b.aP = null;
      bt = 0;
      bz = 0;
      bk = null;
      bm = null;
      bn = null;
      bl = null;
      au = null;
      bo = null;
      b.cx = null;
      b.aM = null;
      b.aR = null;
      am = -1;
      aj = false;
      i();
   }

   private static void af() throws Exception {
      a("/1");
      a = N(0);
      a.d(2);
      d.g(2, 30);
      r();
   }

   private static void ag() throws Exception {
      a("/2");
      b[35] = N(35);
      b[33] = N(33);
      d.I[0] = b[33];
      d.I[0].d(0);
      b[35].d(3);
      d.g(3, 10);
      r();
   }

   private static void J(int var0) throws Exception {
      a(dI[var0]);
      byte[] var1 = p(0);
      byte[] var2 = p(1);
      r();
      bF = var1;
      a(var2);
   }

   public static final boolean i(int var0) {
      int[] var1;
      switch (var0) {
         case 4:
            var1 = bL;
            break;
         case 27:
            var1 = bK;
            break;
         case 28:
            var1 = bM;
            break;
         case 41:
            var1 = bN;
            break;
         case 48:
            var1 = bO;
            break;
         default:
            var1 = null;
      }

      if (var1 != null) {
         for (int var2 = 0; var2 < var1.length; var2++) {
            if (var1[var2] == aG) {
               return true;
            }
         }
      }

      return false;
   }

   private static void ah() {
      for (int var0 = 0; var0 < 53; var0++) {
         dJ[var0] = 0;
      }

      short[] var1 = new short[20];
      int var2 = 0;

      while (var2 < bF.length) {
         byte var4 = bF[var2++];

         for (int var3 = 0; var3 < var4; var3++) {
            var1[var3] = (short)((bF[var2++] & 255) + (bF[var2++] << 8));
         }

         if (var1[0] < b.bj.length && b.bj[var1[0]] != -1) {
            dJ[b.bj[var1[0]]] = 1;
         }
      }

      j(1);
      j(33);
      j(-1);
      j(18);
      j(40);
      j(-1);
      j(40);
      if (dJ[b.bj[73]] == 1) {
         j(30);
      }

      if (dJ[b.bj[13]] == 1) {
         j(13);
      }

      if (dJ[b.bj[90]] == 1) {
         j(16);
      }

      if (dJ[b.bj[90]] == 1) {
         j(17);
      }

      if (i(27)) {
         j(27);
      }

      if (i(4)) {
         j(4);
      }

      if (i(28)) {
         j(28);
      }

      if (i(41)) {
         j(41);
      }

      if (i(48)) {
         j(48);
         j(30);
      }
   }

   public static final void j(int var0) {
      if (var0 >= 0) {
         dJ[var0] = 1;
      }
   }

   private static boolean K(int var0) {
      try {
         if (var0 == 1) {
            aI = false;
            O(2 + aG);
            if (aG != 6 && aG == 9) {
            }
         } else if (var0 == 3) {
            D(aG);
            ae();
         } else if (var0 < 14) {
            int var1;
            j(var1 = var0 - 4, aG);
         } else if (var0 < 15) {
            b(aG, true);
         } else if (var0 == 15) {
            J(aG);
         } else if (var0 == 17) {
            a("/2");
            ah();
         } else if (var0 < 71 && var0 > 17) {
            int var5 = var0 - 18;
            if (dJ[var5] == 1) {
               b[var5] = N(var5);
               if (var5 == 0 || var5 == 25) {
                  try {
                     b[var5].a(0, 0, -1, -1);
                     b[var5].a();
                  } catch (Exception var3) {
                     System.out.println("s_sprites[idx]----error=====  " + var5);
                  }
               } else if (var5 == 2
                  || var5 == 4
                  || var5 == 3
                  || var5 == 10
                  || var5 == 18
                  || var5 == 28
                  || var5 == 34
                  || var5 == 36
                  || var5 == 14
                  || var5 == 29
                  || var5 == 41
                  || var5 == 12
                  || var5 == 13
                  || var5 == 15
                  || var5 == 27
                  || var5 == 40
                  || var5 == 47
                  || var5 == 16
                  || var5 == 17
                  || var5 == 30
                  || var5 == 49
                  || var5 == 48
                  || var5 == -1) {
                  byte var2 = -1;
                  switch (var5) {
                     case -1:
                        var2 = 22;
                     case 0:
                     case 1:
                     case 5:
                     case 6:
                     case 7:
                     case 8:
                     case 9:
                     case 11:
                     case 19:
                     case 20:
                     case 21:
                     case 22:
                     case 23:
                     case 24:
                     case 25:
                     case 26:
                     case 31:
                     case 32:
                     case 33:
                     case 35:
                     case 37:
                     case 38:
                     case 39:
                     case 42:
                     case 43:
                     case 44:
                     case 45:
                     case 46:
                     default:
                        break;
                     case 2:
                        var2 = 1;
                        break;
                     case 3:
                        var2 = 3;
                        break;
                     case 4:
                        var2 = 2;
                        break;
                     case 10:
                        var2 = 4;
                        break;
                     case 12:
                        var2 = 11;
                        break;
                     case 13:
                        var2 = 12;
                        break;
                     case 14:
                        var2 = 8;
                        break;
                     case 15:
                        var2 = 13;
                        break;
                     case 16:
                        var2 = 17;
                        break;
                     case 17:
                        var2 = 18;
                        break;
                     case 18:
                        var2 = 5;
                        break;
                     case 27:
                        var2 = 14;
                        break;
                     case 28:
                        var2 = 6;
                        break;
                     case 29:
                        var2 = 9;
                        break;
                     case 30:
                        var2 = 19;
                        break;
                     case 34:
                        var2 = 0;
                        break;
                     case 36:
                        var2 = 7;
                        break;
                     case 40:
                        var2 = 15;
                        break;
                     case 41:
                        var2 = 10;
                        break;
                     case 47:
                        var2 = 16;
                        break;
                     case 48:
                        var2 = 21;
                        break;
                     case 49:
                        var2 = 20;
                  }

                  d.I[var2] = b[var5];
                  d.I[var2].d(0);
               }
            }
         } else if (var0 == 71) {
            d.g(0, 40);
            System.gc();
            r();
         } else if (var0 != 76) {
            if (var0 == 77) {
               System.gc();
            } else if (var0 != 78) {
               if (var0 == 79) {
                  p();
                  if (dK) {
                     d(true);
                     ak();
                     k();
                     dK = false;
                  } else {
                     c(false);
                  }
               } else {
                  if (var0 > 79) {
                     return true;
                  }

                  System.gc();
               }
            }
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return false;
   }

   private static void ai() {
      n();
      bF = null;
      cP = false;
      b.m();
      w();
      ad();
      am();
      dl = null;
      dm = null;

      for (int var0 = 0; var0 < 53; var0++) {
         if (var0 != 35 && var0 != 33) {
            b[var0] = null;
         }
      }

      for (int var1 = 2; var1 < 11; var1++) {
         P(var1);
      }

      for (int var2 = 0; var2 < 23; var2++) {
         d.I[var2] = null;
      }

      d.c(0);
      d.c(1);
      System.gc();
   }

   public static final void o() {
      for (int var0 = 0; var0 < bq; var0++) {
         if (bu[var0] != null) {
            bu[var0].p = null;
            bu[var0] = null;
         }
      }

      bv = 0;
      bx = 0;

      for (int var1 = 0; var1 < br; var1++) {
         if (bw[var1] != null) {
            bw[var1].p = null;
            bw[var1] = null;
         }
      }

      bC = 0;

      for (int var2 = 0; var2 < bA; var2++) {
         if (bB[var2] != null) {
            bB[var2].p = null;
            bB[var2] = null;
         }
      }
   }

   public static final void p() {
      for (int var0 = 0; var0 < 9; var0++) {
         bJ[var0] = b(cw, 86 + 2 * var0);
      }

      bI = bJ[aG];

      for (int var2 = 0; var2 < 9; var2++) {
         for (int var1 = 0; var1 < 30; var1++) {
            bG[var2][var1] = b(cw, 104 + var2 * 30 * 2 + var1 * 2);
         }
      }

      bH = bG[aG];

      for (int var3 = 0; var3 < bp; var3++) {
         bD[var3] = a(cw, 999 + var3);
      }
   }

   private static void aj() {
      p = 60;
      q = 60;
      s = 60;
      t = 60;
      r = 0;
      u = 0;
      v = 0;
   }

   private static void ak() {
      if (aG == 3) {
         b.bC = a(cw, 644) == 1;
         if (bm != null && b.bC) {
            bn.K = bm.K = b(cw, 645);
            bn.L = bm.L = b(cw, 647);
            bn.ao = bm.ao = c(cw, 649);
         }
      }

      if (bk != null && !bP) {
         bk.K = b(cw, 18);
         bk.L = b(cw, 20);
         bk.ak = b(cw, 22) == 1;
         aI = b(cw, 24) == 1;
         if (aG == 5) {
            bk.L -= 20;
         }
      }

      q();
   }

   public static final void q() {
      b.bK = b(cw, 26);

      for (int var0 = 0; var0 < 2; var0++) {
         b.bM[var0] = b(cw, 28 + 2 * var0);
         b.bN[var0] = b(cw, 32 + 2 * var0);
         b.bO[var0] = b(cw, 36 + 2 * var0);
         b.bP[var0] = b(cw, 40 + 2 * var0);
      }

      q = b(cw, 60);
      p = b(cw, 62);
      r = b(cw, 64);
      t = b(cw, 66);
      s = b(cw, 68);
      u = b(cw, 70);
      v = c(cw, 72);
      b.bl = a(cw, 653) == 1;
      b.bm = a(cw, 654) == 1;
      b.bn = a(cw, 655) == 1;
      b.bo = a(cw, 656) == 1;
      b.bq = a(cw, 657) == 1;
      b.bp = a(cw, 658) == 1;
      b.br = a(cw, 659) == 1;
      b.bs = a(cw, 660) == 1;
      b.bt = a(cw, 661) == 1;
      b.bu = a(cw, 662) == 1;
      b.bw = a(cw, 663) == 1;
   }

   private static void d(boolean var0) {
      if (aG == 5 && var0) {
         for (int var1 = 0; var1 < 3; var1++) {
            b.bx[var1] = b(cw, 664 + var1 * 2);
         }

         for (int var6 = 0; var6 < 54; var6++) {
            b.by[var6] = a(cw, 677 + var6);
         }
      }

      if (dK) {
         c(false);
      } else {
         c(var0);
      }

      if (aG == 5 && var0) {
         am = b(cw, 731);

         for (int var2 = 0; var2 < bt; var2++) {
            b var7 = bs[var2];

            for (int var3 = 0; var3 < 3; var3++) {
               if (b.bx[var3] != -1 && b.bx[var3] == var7.al) {
                  b.cq[var3] = var7;
                  b(b.cq[var3], var3);
               }
            }

            if (am != -1) {
               if (am == var7.al) {
                  var7.c |= 16;
                  var7.c &= -33;
               }

               if (var7.aB == am) {
                  short var9 = b(cw, 733);
                  short var4 = b(cw, 735);
                  int var5 = var9 + var4;
                  aa[1] = O = M = var9 - 2;
                  var7.ar = var7.L - var5;
                  var7.L = var5;
                  aa[3] = M + 320;
                  var7.c |= 16;
                  var7.c &= -33;
                  ak = 4;
               }
            }
         }

         for (int var8 = 0; var8 < 3; var8++) {
            if (b.bx[var8] == -1) {
               b.cq[var8] = null;
            }
         }
      }
   }

   private static void al() {
      n();
      b.m();
      p();
      d(true);
      ak();
      p = q;
      v();
      K();
      aN = 0;
      aM = 0;

      for (int var0 = 0; var0 < d.J[0].length; var0++) {
         d.J[0][var0] = null;
      }

      a(8);
      k = true;
   }

   private static byte[] L(int var0) throws Exception {
      byte[] var1 = p(var0);
      o(var0 + 1);
      bQ = t();
      bR = t();
      return var1;
   }

   private static byte[] M(int var0) throws Exception {
      return p(var0);
   }

   private static void b(int var0, boolean var1) throws Exception {
      if (var0 == 11) {
         a(dI[var0 - 2]);
      } else if (var0 == 12) {
         a(dI[var0 - 1]);
      } else if (var0 == 13) {
         a(dI[var0 - 4]);
      } else {
         a(dI[var0]);
      }

      if (var1) {
         dL = L(2);
      }

      dM = L(var1 ? 5 : 0);
      dN = M(var1 ? 7 : 2);
      dO = L(var1 ? 8 : 3);
      dP = M(var1 ? 10 : 5);
      bV = bQ;
      bU = bQ;
      bW = bR;
      bS = bQ * 20;
      bT = bR * 20;
      r();
      if (var1) {
         for (int var2 = 0; var2 < dL.length; var2++) {
            if (dL[var2] == -1) {
               dL[var2] = 0;
            }
         }
      }

      aA = -1;
   }

   private static void am() {
      dM = null;
      dN = null;
      dO = null;
      dP = null;
      dL = null;
   }

   public static final int d(int var0, int var1) {
      if (var0 >= 0 && var0 < bQ && var1 < bR) {
         if (aF[aG] == 3) {
            if (var1 < 0) {
               return 0;
            }
         } else if (var1 < 0) {
            return 24;
         }

         return dL[var0 + var1 * bQ];
      } else {
         return aF[aG] == 2 ? 0 : 24;
      }
   }

   public static final void c(int var0, int var1, int var2) {
      if (var0 >= 0 && var1 >= 0 && var0 < bQ && var1 < bR) {
         dL[var0 + var1 * bQ] = (byte)var2;
      }
   }

   private static void an() throws Exception {
      a("/17");
      o(0);
      bX = new int[256];

      for (int var0 = 0; var0 < 256; var0++) {
         bX[var0] = u();
      }

      r();
   }

   private static void ao() throws Exception {
      a("/18");
      byte[] var3 = p(0);
      r();
      ByteArrayInputStream var4 = new ByteArrayInputStream(var3);
      DataInputStream var5;
      bY = new int[(var5 = new DataInputStream(var4)).readInt()][];

      for (int var0 = 0; var0 < bY.length; var0++) {
         int var2 = var5.readInt();
         bY[var0] = new int[var2];

         for (int var1 = 0; var1 < var2; var1++) {
            bY[var0][var1] = var5.readInt();
         }
      }
   }

   public static final int e(int var0, int var1) {
      if (var0 < 0) {
         var0 = -var0;
      }

      if (var1 < 0) {
         var1 = -var1;
      }

      int var2;
      int var3;
      if (var0 < var1) {
         var2 = var0;
         var3 = var1;
      } else {
         var2 = var1;
         var3 = var0;
      }

      int var4 = var3 * 1007 + var2 * 441;
      if (var3 < var2 << 4) {
         var4 -= var3 * 40;
      }

      return var4 + 512 >> 10;
   }

   public static final int k(int var0) {
      while (var0 < 0) {
         var0 += 360;
      }

      return bX[var0 % 360 * 256 / 360];
   }

   public static final int l(int var0) {
      return k(90 - var0);
   }

   public static final int f(int var0, int var1) {
      if (var1 != var0) {
         int var2;
         if ((var2 = c.nextInt()) < 0) {
            var2 *= -1;
         }

         return var0 + var2 % (var1 - var0);
      } else {
         return var1;
      }
   }

   public static final int g(int var0, int var1) {
      if (var0 == 0 && var1 == 0) {
         return 0;
      }

      if (var0 < 0) {
         var0 = -var0;
      }

      if (var1 < 0) {
         var1 = -var1;
      }

      int var2 = var0 > var1 ? var1 : var0;
      return var0 + var1 - (var2 >> 1) - (var2 >> 2) + (var2 >> 3);
   }

   private static d N(int var0) throws Exception {
      d var1 = new d();
      byte[] var2 = p(var0);
      var1.a(var2, 0);
      return var1;
   }

   public static final d m(int var0) {
      return b[var0];
   }

   private static void O(int var0) throws Exception {
      a("/15");
      byte[] var1 = p(var0);
      r();
      a(var0, var1);
      String var2 = dQ[0][29];

      for (int var3 = 0; var3 < bZ.length; var3++) {
         int var4 = bZ[var3];
         dQ[0][var4] = a(dQ[0][var4], "#SK", var2);
      }

      dQ[0][29] = null;
   }

   private static void P(int var0) {
      n(var0);
   }

   static final void a(int var0, byte[] var1) {
      if (var1 != null) {
         try {
            ByteArrayInputStream var2 = new ByteArrayInputStream(var1);
            DataInputStream var3;
            (var3 = new DataInputStream(var2)).skip(6L);
            short var4 = var3.readShort();
            dQ[var0] = new String[var4];

            for (int var5 = 0; var5 < var4; var5++) {
               dQ[var0][var5] = var3.readUTF().toUpperCase().trim();
            }

            var3.close();
            var2.close();
         } catch (Exception var6) {
         }
      }
   }

   static final void n(int var0) {
      dQ[var0] = null;
   }

   static final String h(int var0, int var1) {
      return dQ[var0][var1];
   }

   static final int a(byte[] var0, int var1, int var2) {
      var0[var1++] = (byte)var2;
      return var1;
   }

   static final int a(byte[] var0, int var1, short var2) {
      var0[var1++] = (byte)(var2 & 0xFF);
      var0[var1++] = (byte)(var2 >>> 8 & 0xFF);
      return var1;
   }

   static final int b(byte[] var0, int var1, int var2) {
      var0[var1++] = (byte)(var2 & 0xFF);
      var0[var1++] = (byte)(var2 >>> 8 & 0xFF);
      var0[var1++] = (byte)(var2 >>> 16 & 0xFF);
      var0[var1++] = (byte)(var2 >>> 24 & 0xFF);
      return var1;
   }

   static final byte a(byte[] var0, int var1) {
      return var0[var1];
   }

   static final short b(byte[] var0, int var1) {
      return (short)(var0[var1++] & 0xFF | (var0[var1] & 255) << 8);
   }

   static final int c(byte[] var0, int var1) {
      return var0[var1++] & 0xFF | (var0[var1++] & 0xFF) << 8 | (var0[var1++] & 0xFF) << 16 | (var0[var1] & 0xFF) << 24;
   }

   static final void a(String var0) throws Exception {
      if (dR == null || var0.compareTo(dR) != 0) {
         if (dS != null) {
            r();
         }

         dR = var0;
         dS = dR.getClass().getResourceAsStream(dR);
         dU = (short)t();
         dW = (short)t();
         dX = new short[dW];

         for (int var1 = 0; var1 < dW; var1++) {
            dX[var1] = (short)t();
         }

         dY = 0;
         ap();
      }
   }

   private static void ap() throws Exception {
      int var0;
      if (dY == dW - 1) {
         var0 = dU - dX[dY];
      } else {
         var0 = dX[dY + 1] - dX[dY];
      }

      dV = new int[var0 + 1];

      for (int var1 = 0; var1 < var0 + 1; var1++) {
         dV[var1] = u();
      }
   }

   static final void r() throws Exception {
      if (dS != null) {
         dS.close();
         dS = null;
      }

      dT = 0;
   }

   static final int o(int var0) throws Exception {
      int var1 = dW - 1;

      while (var1 >= 0 && dX[var1] > var0) {
         var1--;
      }

      if (dY != var1) {
         dY = var1;
         r();
         if (dY == 0) {
            dS = dR.getClass().getResourceAsStream(dR);
            t();
            t();

            for (int var2 = 0; var2 < dW; var2++) {
               t();
            }
         } else {
            dS = dR.getClass().getResourceAsStream(dR + "." + dY);
         }

         ap();
      } else if (dS == null) {
         if (dY == 0) {
            dS = dR.getClass().getResourceAsStream(dR);
         } else {
            dS = dR.getClass().getResourceAsStream(dR + "." + dY);
         }
      }

      var0 -= dX[dY];
      int var5 = dV[var0];
      int var3 = dV[var0 + 1] - dV[var0] - 1;
      q(var5);
      s();
      return var3;
   }

   static final byte[] p(int var0) throws Exception {
      int var1;
      if ((var1 = o(var0)) < 0) {
         return null;
      }

      byte[] var2;
      c(var2 = new byte[var1], 0, var2.length);
      return var2;
   }

   static final void q(int var0) throws Exception {
      if (dT != var0) {
         if (dT > var0) {
            r();
            if (dY == 0) {
               dS = dR.getClass().getResourceAsStream(dR);
            } else {
               dS = dR.getClass().getResourceAsStream(dR + "." + dY);
            }
         } else {
            var0 -= dT;
         }

         r(var0);
      }
   }

   static final void r(int var0) throws Exception {
      if (var0 != 0) {
         if (dZ == null) {
            dZ = new byte[256];
         }

         while (var0 > 256) {
            c(dZ, 0, 256);
            var0 -= 256;
         }

         if (var0 > 0) {
            c(dZ, 0, var0);
         }
      }
   }

   static final int s() throws Exception {
      int var0;
      if ((var0 = dS.read()) >= 0) {
         dT++;
      }

      return var0;
   }

   static final int t() throws Exception {
      return s() & 0xFF | (s() & 0xFF) << 8;
   }

   static final int u() throws Exception {
      return s() & 0xFF | (s() & 0xFF) << 8 | (s() & 0xFF) << 16 | (s() & 0xFF) << 24;
   }

   static final int c(byte[] var0, int var1, int var2) throws Exception {
      int var3 = var1;
      int var4 = var2;
      int var5 = 0;

      while (var4 > 0) {
         var5 = dS.read(var0, var3, var4);
         var4 -= var5;
         var3 += var5;
      }

      dT += var2;
      return var2;
   }

   private void aq() {
      at();
      a(0, 0, 240, 320);
      if (dn == null) {
         a(0, true);
      }

      a(dn, 0, 0, 0);
      a(23, 110, 194, 177, 217);
      if (cI) {
         a.z = 0;
         a.a(d, "is GOD MODULE!", 237, 20, 9);
      }

      if (cI) {
         d.setColor(16777215);
         d.drawString("level :" + aG, 0, 10, 0);
         if (C(1)) {
            aG++;
         }

         if (aG > 8) {
            aG = 0;
         }

         if (C(2048)) {
            dn = null;
            a(9);
         }
      }

      a.z = 0;
      e(ea, 120, 125 + ar() * 9);
      a.z = 0;
      if (ds) {
         M();
      } else {
         c(eb[ea].length, false);
      }

      H(ea);
   }

   private static String l(int var0, int var1) {
      return h(0, eb[var0][var1]);
   }

   private static int ar() {
      int var0 = 0;
      if (ea == 0) {
         var0 += 2;
      }

      if (!a.a()) {
         var0++;
      }

      if (!a.d()) {
         var0++;
      }

      return var0;
   }

   private static void e(int var0, int var1, int var2) {
      int var3 = 0;

      for (int var4 = 0; var4 < eb[var0].length; var4++) {
         if (var0 != 0 && var0 != 2 || (!a.a() ? eb[var0][var4] != 4 && eb[var0][var4] != 5 : a.d() || eb[var0][var4] != 5)) {
            int var5 = var1;
            String var6 = l(var0, var4);
            if ((var0 == 0 || var0 == 2) && a.a() && eb[var0][var4] == 4) {
               a.z = ca == var4 ? 2 : 1;
            } else if ((var0 == 0 || var0 == 2) && a.a() && a.d() && eb[var0][var4] == 5) {
               a.z = ca == var4 ? 2 : 1;
            } else if (var0 == 1) {
               a.z = ca == var4 ? 1 : 0;
            } else {
               a.z = ca == var4 ? 2 : 0;
            }

            if (eb[var0][var4] == 6) {
               a.a(d, var6 + ": " + (dk ? h(0, 23) : h(0, 24)), var5, 18 * var3 + var2, 3);
            } else {
               a.a(d, var6, var5, 18 * var3 + var2, 3);
            }

            if (eb[var0][var4] == 4) {
               a.a(d, do, var5 + d.Q / 2 + 0, 18 * var3 + var2, 6);
            } else if (eb[var0][var4] == 5) {
               a.b(d, do, var5 + d.Q / 2 + 0, 18 * var3 + var2, 6);
            }

            if (ca == var4) {
               b[35].a(d, 5, z % 6 >> 1, x == 14 ? 60 : 45, var2 + var3 * 18, 1, 0, 0);
            }

            var3++;
         }
      }

      a(var0 == 1 ? h(0, 18) : "", h(0, 21));
   }

   private static void c(int var0, boolean var1) {
      if (var0 > 0) {
         if (C(var1 ? 4112 : 16388)) {
            E(-1);
            ca += var0 - 1;
            ca %= var0;
            if ((ea == 0 || ea == 2) && eb[ea][ca] == 5) {
               if (!a.a()) {
                  ca += var0 - 2;
                  ca %= var0;
                  return;
               }

               if (a.a() && !a.d()) {
                  ca += var0 - 1;
                  ca %= var0;
                  return;
               }
            }
         } else if (C(var1 ? 8256 : 33024)) {
            E(-1);
            ca++;
            ca %= var0;
            if (ea == 0 || ea == 2) {
               if (eb[ea][ca] == 4) {
                  if (!a.a()) {
                     ca += 2;
                     ca %= var0;
                     return;
                  }
               } else if (eb[ea][ca] == 5 && a.a() && !a.d()) {
                  ca++;
                  ca %= var0;
               }
            }
         }
      }
   }

   public static final void a(byte[] var0) {
      int var19 = 0;
      var19++;
      short var2;
      cb = new byte[var2 = var0[0]][][];
      cc = new short[var2];
      ce = new int[var2][];

      for (int var3 = 0; var3 < var2; var3++) {
         cc[var3] = (short)((var0[var19] & 255) + ((var0[var19 + 1] & 255) << 8));
         var19 += 2;
         byte var4 = var0[var19++];
         var19 += 2;
         cb[var3] = new byte[var4][];
         ce[var3] = new int[var4];

         for (int var5 = 0; var5 < var4; var5++) {
            int var6 = var19;
            byte var7 = var0[var19++];
            var19++;
            switch (var7) {
               case 0:
               case 1:
               default:
                  break;
               case 2:
                  var19 += 2;
                  break;
               case 3:
                  var19 += 2;
            }

            short var8 = (short)((var0[var19] & 255) + ((var0[var19 + 1] & 255) << 8));
            var19 += 2;
            int var9 = var19;

            for (int var10 = 0; var10 < var8; var10++) {
               var19 += 2;
               byte var11 = var0[var19++];

               for (int var12 = 0; var12 < var11; var12++) {
                  byte var13;
                  if ((var13 = var0[var19++]) >= 100) {
                     var19 += z(var13);
                  } else {
                     switch (var13) {
                        case 11:
                        case 12:
                        case 21:
                        case 31:
                        case 41:
                           var19 += 2;
                           var19 += 2;
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 25:
                        case 26:
                        case 27:
                        case 28:
                        case 29:
                        case 30:
                        case 33:
                        case 40:
                        default:
                           break;
                        case 22:
                        case 32:
                        case 42:
                           var19++;
                           break;
                        case 23:
                        case 24:
                        case 43:
                        case 44:
                           var19 += 4;
                           break;
                        case 34:
                        case 35:
                        case 36:
                           var19 += ((var13 - 34) % 3 + 1 << 1) + 2;
                           break;
                        case 37:
                        case 38:
                        case 39:
                           var19 += (var13 - 34) % 3 + 1 << 1;
                     }
                  }
               }
            }

            int var21 = var19 - var6;
            cb[var3][var5] = new byte[var21 + 2];
            ce[var3][var5] = var9 - var6;
            System.arraycopy(var0, var6, cb[var3][var5], 0, var21);
            cb[var3][var5][var21] = (byte)(ce[var3][var5] & 0xFF);
            cb[var3][var5][var21 + 1] = (byte)(ce[var3][var5] >> 8 & 0xFF);
         }
      }

      cd = new short[var2];
      v();
   }

   public static final void v() {
      cf = false;

      for (int var0 = 0; var0 < cd.length; var0++) {
         cd[var0] = -1;
      }

      for (int var4 = 0; var4 < cc.length; var4++) {
         byte[][] var1 = cb[var4];

         for (int var2 = 0; var2 < var1.length; var2++) {
            byte[] var3 = var1[var2];
            ce[var4][var2] = (var3[var3.length - 2] & 255) + (var3[var3.length - 1] << 8 & 0xFF);
         }
      }
   }

   public static final void w() {
      cb = (byte[][][])null;
      cc = null;
      cd = null;
      ce = (int[][])null;
   }

   public static final void s(int var0) {
      cm = false;
      Z = true;
      cd[var0] = 0;
      aM = 35;
      C();
      b.aS = 0;
      b.d(true);
   }

   public static final void t(int var0) {
      cd[var0] = 0;
   }

   public static final void u(int var0) {
      cd[var0] = -2;
      byte[][] var1 = cb[var0];

      for (int var2 = 0; var2 < var1.length; var2++) {
         byte[] var3 = var1[var2];
         ce[var0][var2] = (var3[var3.length - 2] & 255) + (var3[var3.length - 1] << 8 & 0xFF);
      }
   }

   public static final void v(int var0) {
      cd[var0] = 0;
      byte[][] var1 = cb[var0];

      for (int var2 = 0; var2 < var1.length; var2++) {
         byte[] var3 = var1[var2];
         ce[var0][var2] = (var3[var3.length - 2] & 255) + (var3[var3.length - 1] << 8 & 0xFF);
      }
   }

   public static final void w(int var0) {
      cm = true;
      Z = false;
      cd[var0] = -2;
      if (x != 15) {
         aM = 0;
      }

      if (!y(var0)) {
         ar = 1;
      }

      if (aG != 9 && aG != 3) {
         as = 1;
      }

      ch = true;
      ci = false;
      m();
      if (bc != 17 && bc != 18) {
         l();
      }

      aO = false;
   }

   public static final int x(int var0) {
      for (int var1 = 0; var1 < cc.length; var1++) {
         if (cc[var1] == var0) {
            return var1;
         }
      }

      return -1;
   }

   public static final void x() {
      if (C(131072) && ch) {
         cg = true;
         cf = false;
         C();
      }

      if (!cf) {
         for (int var0 = 0; var0 < cc.length; var0++) {
            boolean var1 = false;

            for (int var2 = 0; var2 < cj; var2++) {
               if (var0 == ck[var2]) {
                  var1 = true;
                  break;
               }
            }

            if (!var1) {
               b(var0, var1, 1);
            }
         }
      }
   }

   public static final void y() {
      for (int var0 = 0; var0 < cj; var0++) {
         b(ck[var0], true, cl[var0]);
      }
   }

   public static final boolean y(int var0) {
      boolean var1 = false;

      for (int var2 = 0; var2 < cj; var2++) {
         if (var0 == ck[var2]) {
            var1 = true;
            break;
         }
      }

      return var1;
   }

   public static final boolean z() {
      if (cd != null) {
         for (int var0 = 0; var0 < cd.length; var0++) {
            if (cd[var0] >= 0) {
               boolean var1 = false;

               for (int var2 = 0; var2 < cj; var2++) {
                  if (var0 == ck[var2]) {
                     var1 = true;
                     break;
                  }
               }

               if (!var1) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static final void b(int var0, boolean var1, int var2) {
      b var6 = null;
      if (cd[var0] >= 0) {
         boolean var7 = true;
         byte[][] var8 = cb[var0];
         cd[var0]++;
         short var9 = cd[var0];

         for (int var10 = 0; var10 < var8.length; var10++) {
            b var11 = null;
            int var13 = 0;
            byte[] var14 = var8[var10];
            if (ce[var0][var10] < var14.length - 2) {
               byte var15 = var14[0];
               boolean var16 = false;
               switch (var15) {
                  case 0:
                  case 1:
                  default:
                     break;
                  case 2:
                     var11 = g((var14[2] & 255) + ((var14[3] & 255) << 8));
                     break;
                  case 3:
                     int var12;
                     short var17;
                     if ((var11 = h(var12 = ((var17 = (short)((var14[2] & 255) + ((var14[3] & 255) << 8))) << 16) + (var0 << 8) + var10)) == null) {
                        bk.b(-2, var17, 0, 0, 0, 200, false);
                        var11 = b.aL;
                        b.aL.al = var12;
                        var11.ak = false;
                        var11.c &= -2;
                        e(var11);
                     }
               }

               var13 = ce[var0][var10];
               int var32 = 0;
               int var18 = 0;
               short var19 = -1;
               short var20 = (short)((var14[var13] & 255) + ((var14[var13 + 1] & 255) << 8));
               var13 += 2;
               byte var21 = var14[var13++];

               for (int var22 = 0; var22 < var21; var22++) {
                  byte var23;
                  if ((var23 = var14[var13++]) >= 100) {
                     var13 += a(var23, var14, var13, var9, var20, var11);
                  } else {
                     switch (var23) {
                        case 11:
                        case 12:
                        case 21:
                        case 31:
                        case 41:
                           short var24 = (short)((var14[var13] & 255) + ((var14[var13 + 1] & 255) << 8));
                           var13 += 2;
                           short var25 = (short)((var14[var13] & 255) + ((var14[var13 + 1] & 255) << 8));
                           var13 += 2;
                           if (var9 <= var20) {
                              var32 = var24;
                              var18 = var25;
                              if (var23 == 12) {
                                 var32 -= 120;
                                 var18 -= 160;
                              }

                              if (var23 == 11 || var23 == 12) {
                                 if (var32 < 0) {
                                    var32 = 0;
                                 } else if (var32 > bS - 240) {
                                    var32 = bS - 240;
                                 }

                                 if (var18 < 0) {
                                    var18 = 0;
                                 } else if (var18 > bT - 320) {
                                    var18 = bT - 320;
                                 }
                              }

                              var19 = var20;
                           }

                           if (var15 == 3 && var9 == 0 && var20 == 0) {
                              var11.K = var24;
                              var11.L = var25;
                           }
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 25:
                        case 26:
                        case 27:
                        case 28:
                        case 29:
                        case 30:
                        case 33:
                        case 40:
                        default:
                           break;
                        case 22:
                        case 32:
                        case 42:
                           if (var20 <= var9) {
                              short var35;
                              if ((var35 = var14[var13]) < 0) {
                                 var35 += 256;
                              }

                              var11.b(var35);
                           }

                           var13++;
                           break;
                        case 23:
                        case 43:
                           if (var20 <= var9) {
                              int var34;
                              if ((
                                    (
                                          var34 = (var14[var13] & 255)
                                             + ((var14[var13 + 1] & 255) << 8)
                                             + ((var14[var13 + 2] & 255) << 16)
                                             + ((var14[var13 + 3] & 255) << 24)
                                       )
                                       & 1
                                 )
                                 != 0) {
                                 var34 |= 65536;
                              }

                              var11.c |= var34 >> 16;
                              var11.ak = (var11.c & 1) != 0;
                           }

                           var13 += 4;
                           break;
                        case 24:
                        case 44:
                           if (var20 <= var9) {
                              int var33;
                              if ((
                                    (
                                          var33 = (var14[var13] & 255)
                                             + ((var14[var13 + 1] & 255) << 8)
                                             + ((var14[var13 + 2] & 255) << 16)
                                             + ((var14[var13 + 3] & 255) << 24)
                                       )
                                       & 1
                                 )
                                 != 0) {
                                 var33 |= 65536;
                              }

                              var11.c &= ~(var33 >> 16);
                           }

                           var11.ak = (var11.c & 1) != 0;
                           var13 += 4;
                           break;
                        case 34:
                        case 35:
                        case 36:
                           int var27 = (var14[var13] & 255) + ((var14[var13 + 1] & 255) << 8);
                           var13 += 2;
                           var6 = g(var27);
                        case 37:
                        case 38:
                        case 39:
                           int var26 = (var23 - 34) % 3 + 1;
                           int var5 = -1;
                           int var4 = -1;
                           int var3 = -1;
                           if (var26 >= 1) {
                              var3 = (var14[var13] & 255) + ((var14[var13 + 1] & 255) << 8);
                              var13 += 2;
                           }

                           if (var26 >= 2) {
                              var4 = (var14[var13] & 255) + ((var14[var13 + 1] & 255) << 8);
                              var13 += 2;
                           }

                           if (var26 >= 3) {
                              var5 = (var14[var13] & 255) + ((var14[var13 + 1] & 255) << 8);
                              var13 += 2;
                           }

                           if (var6 != null) {
                              var6 = null;
                           } else if (var20 == var9) {
                              h(var3, var4, var5, var26);
                           }
                     }
                  }
               }

               if (var20 == var9) {
                  ce[var0][var10] = var13;
               } else if (var20 < var9) {
                  ce[var0][var10] = var13;
               }

               if (ce[var0][var10] < var14.length - 2) {
                  var7 = false;
               } else if (var15 == 3) {
                  c(var11);
               }

               if (var19 >= 0) {
                  if (var15 == 1) {
                     L = L + (var32 - L) / (var19 - var9 + 1);
                     M = M + (var18 - M) / (var19 - var9 + 1);
                     f();
                  } else {
                     var11.K = var11.K + (var32 - var11.K) / (var19 - var9 + 1);
                     var11.L = var11.L + (var18 - var11.L) / (var19 - var9 + 1);
                     var11.h();
                  }
               }
            }
         }

         if (var7 || cu) {
            cu = false;
            if (var2 == 0) {
               v(var0);
               return;
            }

            w(var0);
            if (cp) {
               cp = false;
               ch = false;
               c(bk.q);
               bk.q = null;
               if (cq) {
                  s(x(cr));
                  b.E.ap = b.E.ap - b.E.ap / 5;
                  if (b.E.ap <= 0) {
                     b.E.ap = 1;
                     return;
                  }
               } else {
                  s(x(cs));
                  p = p - p / 3;
               }
            }
         }
      }
   }

   static final void h(int var0, int var1, int var2, int var3) {
      if (var3 != 1) {
         if (var3 == 2) {
            switch (var0) {
               case 0:
                  aw = 14;
                  ax = 17;
                  ay = 2;
                  aO = false;
                  aP = false;
                  return;
               case 1:
                  aO = true;
                  aM = 35;
                  return;
               case 2:
                  aM = 0;
                  return;
               case 3:
                  a(15);
            }
         }
      }
   }

   static final int z(int var0) {
      return cn[var0 - 100];
   }

   static final int a(int var0, byte[] var1, int var2, int var3, int var4, b var5) {
      int var7 = z(var0);
      if (var3 != var4) {
         return var7;
      }

      switch (var0) {
         case 100:
            short var31 = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            var2 += 2;
            short var32 = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            var2 += 2;
            short var30 = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            g(var31).b(var32, var30);
            break;
         case 101:
            short var29 = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            cu = true;
            s(x(var29));
            break;
         case 102:
            short var6 = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            var2 += 2;
            byte var8;
            if ((var8 = var1[var2]) == 0) {
               var8 = -1;
            }

            i(var6, var8);
            break;
         case 103:
            F();
            break;
         case 104:
            e((short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8)));
         case 105:
         case 109:
         case 110:
         case 112:
         case 118:
         default:
            break;
         case 106:
            int var9 = var1[var2] & 255;
            if (var5.C()) {
               var5.n(var9);
            } else {
               var5.ao = var9;
            }
            break;
         case 107:
            int var10 = (var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8) + ((var1[var2 + 2] & 255) << 16) + ((var1[var2 + 3] & 255) << 24);
            var5.c |= var10;
            if (var10 == 4096) {
            }
            break;
         case 108:
            int var11 = (var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8) + ((var1[var2 + 2] & 255) << 16) + ((var1[var2 + 3] & 255) << 24);
            var5.c &= ~var11;
            break;
         case 111:
         case 116:
            int var12 = var1[var2] & 255;
            int var13 = var1[++var2] & 255;
            int var14 = var1[++var2] & 255;
            int var15 = var1[++var2] & 255;
            var2++;
            int var16;
            int var17;
            if (var0 == 111) {
               var16 = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
               var2 += 2;
               var17 = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            } else {
               var5.g();
               var16 = var5.K;
               var17 = var5.j[1];
            }

            if (aU == null) {
               aU = h(var12, var13);
               aV = h(0, var14 + 77);
               K = 0;
               aY = var14;
               aZ = var15;
               aY = -1;
               boolean var18 = aY != -1 && aY <= 2;
               boolean var19 = false;
               a.z = 0;
               a.a('}' + aU, var18 ? 175 : 210);
               ba = 0;
               aW = var16 - L;
               aX = var17 - M;
               f(5);
            }
            break;
         case 113:
            c(var5);
            break;
         case 114:
            co = f(0, 5);
            bk.b(ct[co], L + 120, M + 160);
            cr = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            var2 += 2;
            cs = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            cp = true;
            cq = false;
            break;
         case 115:
            ch = false;
            break;
         case 117:
            ci = true;
            F = var5.K;
            G = var5.L;
            E = 0;
            break;
         case 119:
            f(17);
            break;
         case 120:
            var5.aG = var1[var2] & 255;
            break;
         case 121:
            f(18);
      }

      return var7;
   }

   public static final void b(boolean var0) {
      try {
         RecordStore var1;
         if ((var1 = RecordStore.openRecordStore("/dvseed.sav", true)).getNumRecords() <= 0) {
            if (!var0) {
               var1.closeRecordStore();
               return;
            }

            var1.addRecord(cw, 0, cv);
         } else if (var0) {
            var1.setRecord(1, cw, 0, cv);
         } else {
            var1.getRecord(1, cw, 0);
            cx = true;
         }

         var1.closeRecordStore();
      } catch (Exception var2) {
         var2.toString();
      }
   }

   public static final void A(int var0) {
      cy |= var0 & 0xFF;
   }

   private static void as() {
      a(0, 0, 240, 320);
      if (dn == null) {
         a(0, true);
      }

      a(dn, 0, 0, 0);
      a.z = 0;
      if (z % 10 > 5) {
         a.a(d, h(0, 10), 120, 300, 17);
      }

      if (B()) {
         a(2);
      }
   }

   private static int Q(int var0) {
      switch (var0) {
         case -7:
            return 131072;
         case -6:
            return 262144;
         case -5:
            return 65536;
         case -4:
            return 8192;
         case -3:
            return 4096;
         case -2:
            return 32768;
         case -1:
            return 16384;
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         default:
            return 0;
         case 35:
            return 1024;
         case 42:
            return 2048;
         case 48:
            return 1;
         case 49:
            return 2;
         case 50:
            return 4;
         case 51:
            return 8;
         case 52:
            return 16;
         case 53:
            return 32;
         case 54:
            return 64;
         case 55:
            return 128;
         case 56:
            return 256;
         case 57:
            return 512;
      }
   }

   private static boolean at() {
      if (C(cJ[cH])) {
         cH++;
         if (cH == cJ.length) {
            cH = 0;
            E(-1);
            C();
            cI = true;
            return true;
         }
      } else if (B()) {
         cH = 0;
      }

      return false;
   }

   public static final void A() {
      if (cz != 0) {
         cG = 0;
      }

      if (cE != 0) {
         cD = -1;
      }

      cG++;
      if (cC != cB || cC == 0) {
         if (bk != null && (bk.f == 2 || bk.f == 3) && cD >= 0) {
            if (cD <= 2) {
               b.bG = true;
            } else {
               b.bG = false;
            }
         }

         cD = -1;
      }

      if (cC != 0) {
         cD++;
      }

      cC = cB;
      cz = cA;
      cE = cF;
      cA = 0;
      cF = 0;
   }

   protected final void keyPressed(int var1) {
      cA = cA | Q(var1);
      int var2 = Q(var1);
      cA |= var2;
      if ((var2 & 4112) != 0) {
         cA &= -8257;
         cA &= -16389;
         cA &= -33025;
      }

      if ((var2 & 8256) != 0) {
         cA &= -4113;
         cA &= -16389;
         cA &= -33025;
      }

      if ((var2 & 16388) != 0) {
         cA &= -4113;
         cA &= -8257;
         cA &= -33025;
      }

      if ((var2 & 33024) != 0) {
         cA &= -4113;
         cA &= -8257;
         cA &= -16389;
      }

      if ((var2 & 65568) != 0) {
         cA &= -4113;
         cA &= -8257;
         cA &= -33025;
         cA &= -16389;
      }

      cC = cC | cA;
      cB = cB | cA;
   }

   protected final void keyReleased(int var1) {
      cF = cF | Q(var1);
      cB = cB & ~cF;
      cC = cC & ~cF;
   }

   public static final boolean B(int var0) {
      return (cC & var0) != 0;
   }

   public static final boolean C(int var0) {
      return (cz & var0) != 0;
   }

   public static final boolean B() {
      return cz != 0;
   }

   public static final void C() {
      cB = 0;
      cC = 0;
      cz = 0;
      cE = 0;
      cA = 0;
      cF = 0;
   }

   public static final void D() {
      if (E() && bc != 1) {
         if (cR) {
            if (!cP) {
               for (int var0 = 0; var0 < 30; var0++) {
                  cK[var0] = (short)(var0 * 8);
                  cM[var0] = (short)(f(0, 100) * 320 / 100);
                  cN[var0] = (byte)(10 + f(0, 8));
                  cO[var0] = (byte)(7 + f(0, 6));
                  cL[var0] = (byte)f(0, 3);
               }

               cP = true;
            }

            b(-1428431909);

            for (int var3 = 0; var3 < cQ; var3++) {
               if (x != 14 && x != 31) {
                  cM[var3] = (short)(cM[var3] + cN[var3]);
                  cK[var3] = (short)(cK[var3] - cO[var3]);

                  while (cM[var3] < 0) {
                     cM[var3] = (short)(cM[var3] + 320);
                  }

                  while (cM[var3] > 320) {
                     cM[var3] = (short)(cM[var3] - 320);
                  }

                  while (cK[var3] > 240) {
                     cK[var3] = (short)(cK[var3] - 240);
                  }

                  while (cK[var3] < 0) {
                     cK[var3] = (short)(cK[var3] + 240);
                  }
               }

               b(cW[cL[var3]]);
               d(cK[var3], cM[var3], cK[var3] - 7, cM[var3] + 13);
            }

            if (f(0, 500) == 40 && !cS) {
               cS = true;
               cT = 0;
               cU = f(0, 3) == 0;
            }

            if (cS && x != 14) {
               int var2 = cT++;
               if (cU) {
                  e.setARGBColor(cV[0]);
                  b(0, 0, 240, 320);
                  cS = false;
                  return;
               }

               if (var2 <= 1) {
                  e.setARGBColor(cV[var2]);
                  b(0, 0, 240, 320);
                  return;
               }

               cS = false;
            }
         }
      }
   }

   public static final void a(boolean var0, int var1) {
      e.setARGBColor(872414976);
      short var2 = 0;
      int var3 = 0;
      if (var1 == 0) {
         for (int var11 = 0; var11 < 3; var11++) {
            var3 = (cZ + var11 * 15) % 180;
            if (var0) {
               if (var3 < 90) {
                  cX = var3 * 70 / 90;
                  var2 = 270;
               } else {
                  cX = (180 - var3) * 70 / 90;
                  var2 = 90;
               }
            } else if (var3 < 90) {
               cX = var3 * 70 / 90;
               var2 = 90;
            } else {
               cX = (180 - var3) * 70 / 90;
               var2 = 270;
            }

            e.setARGBColor(db[var11]);
            d(bk.K - cX / 2 - L, bk.L - 30 - 35 - M, cX, 70, var2, 180);
            d(bk.K - cX / 2 + 1 - L, bk.L - 30 - 35 - M, cX, 70, var2, 180);
         }

         cZ = (cZ + 3) % 180;
      } else if (var1 == 1) {
         for (int var10 = 0; var10 < 3; var10++) {
            var3 = (da + var10 * 15) % 180;
            if (var0) {
               if (var3 < 90) {
                  cY = var3 * 70 / 90;
                  var2 = 180;
               } else {
                  cY = (180 - var3) * 70 / 90;
                  var2 = 0;
               }
            } else if (var3 < 90) {
               cY = var3 * 70 / 90;
               var2 = 0;
            } else {
               cY = (180 - var3) * 70 / 90;
               var2 = 180;
            }

            e.setARGBColor(db[var10]);
            d(bk.K - 35 - L, bk.L - 30 - cY / 2 - M, 70, cY, var2, 180);
            d(bk.K - 35 - L, bk.L - 30 - cY / 2 + 1 - M, 70, cY, var2, 180);
         }

         da = (da + 4) % 180;
      } else {
         if (var1 == 888) {
            if (da == 0 || da == 180 || l(da) == 0) {
               da = (da + 1) % 45;
               return;
            }

            int var4 = 8960 / l(da);
            int var5 = 35 * k(da) * l(45) / l(da) >> 8;
            System.out.println("r2  " + var4);
            System.out.println("r3  " + var5);
            if (var0) {
               d(bk.K + var5 - var4 - L, bk.L - 30 + var5 - var4 - M, var4 * 2, var4 * 2, 135 - da, da * 2);
            } else {
               d(bk.K - var5 - var4 - L, bk.L - 30 - var5 - var4 - M, var4 * 2, var4 * 2, 315 - da, da * 2);
            }

            da = (da + 1) % 45;
         }
      }
   }

   public static final boolean E() {
      return aG == 2 || aG == 3;
   }

   private static void R(int var0) {
      try {
         b(var0, false);
      } catch (Exception var2) {
      }
   }

   private static void S(int var0) {
      for (int var1 = 0; var1 < 9; var1++) {
         m(var0, var1);
      }
   }

   private static void m(int var0, int var1) {
      try {
         if (var0 == 11) {
            j(var1, var0 - 2);
         } else if (var0 == 12) {
            j(var1, var0 - 1);
         } else if (var0 == 13) {
            j(var1, var0 - 4);
         } else {
            j(var1, var0);
         }
      } catch (Exception var3) {
      }
   }

   private static void f(int var0, int var1, int var2) {
      if (var1 > 10) {
         var1 = 10;
      }

      for (int var3 = 0; var3 < var0; var3++) {
         if (var3 % 2 == 0) {
            d(0, var3 + var2, var1 * 24, var3 + var2);
         } else {
            d(240 - var1 * 24, var3 + var2, 240, var3 + var2);
         }
      }
   }

   private static void a(int var0, int var1, int var2, int var3, boolean var4, int var5, int var6) {
      if (var4) {
         b(var6);
         b(var0, var1, var2 + 1, var3 + 2);
      }

      b(var5);
      d(var0, var1, var0 + var2, var1);
      d(var0 - 1, var1 + 1, var0 - 1, var1 + 1 + var3);
      d(var0, var1 + var3 + 2, var0 + var2, var1 + var3 + 2);
      d(var0 + var2 + 1, var1 + 1, var0 + var2 + 1, var1 + 1 + var3);
   }

   private static void e(boolean var0) {
      if (dd != 2) {
         a(dc, 0, true);
         a(0, 0, 240, 320);
         if (C(2048) && var0 && cI) {
            ed = !ed;
            if (ed) {
               ec = aH;
               aH = 9;
            } else {
               aH = ec;
            }
         }
      }

      b(0);
      b(0, 0, 240, 20);
      b(0, 200, 240, 140);
      if (var0) {
         a.z = 0;
         a.a(d, h(0, 41) + " " + (dh + 1), 120, 260, 3);
      }

      a.z = 1;
      a.a(d, h(0, 42 + dh), 120, 280, 3);
      if (dd == 1) {
         f(var0 ? 186 : 191, H, 16);
         H++;
         if (H >= 11) {
            H = 12;
            dd = 2;
            de = 0;
         }
      } else if (dd == 3) {
         H--;
         f(var0 ? 186 : 191, H, 16);
         if (H <= 0) {
            dd = 0;
         }
      } else if (dd == 2 || dd == 1 && H >= 9) {
         b(0, 16, 240, 186 + (var0 ? 0 : 50));
         b.a(120, var0 ? 120 : 145, 120, var0 ? 120 : 145, 40, 35, 3 * z, true);
         a.z = 0;
         a.a(d, h(0, 26), 120, 180 + (var0 ? 0 : 25), 17);
      }

      if (var0) {
         byte var1 = -1;
         int var2 = df - 9;
         byte[] var3 = dj[var2];
         byte[] var4 = ee[var2];
         if (dg[var2 + 1] - 1 >= aH) {
            int var5 = aH - dg[var2];
            var1 = var3[var5];
         }

         int var8 = 0;
         if (aH != 9 && var2 == 0) {
            var8 = 2;
         }

         for (; var8 < var4.length >> 1 && (var1 <= 0 || var8 < var1); var8++) {
            int var6 = 120 + var4[var8 << 1];
            int var7 = 235 + var4[(var8 << 1) + 1];
            if (eh != 2 && ef == var8) {
               a(var6 - 2, var7 - 2, 3, 3, true, 0, 16777215);
            } else {
               a(var6 - 1, var7 - 1, 1, 1, true, 0, 16777215);
            }
         }

         for (int var9 = 0; var9 < var3.length - 1; var9++) {
            b[35].a(d, 7, 120 + var4[var3[var9] << 1], 235 + var4[(var3[var9] << 1) + 1], 0, 0, 0);
         }

         if (eh == 2) {
            b[35].a(d, 0, dc % 8, 120 + di[dh << 1], 239 + di[(dh << 1) + 1] - 4, 0, 0, 0);
         }
      }

      if (dd == 0) {
         dc++;
      }

      b(15244571);
      if (dh > 0 || aH == 9) {
         c(8, 223, 18, 218, 18, 228);
      }

      if (dh < aH || aH == 9) {
         c(232, 223, 222, 218, 222, 228);
      }
   }

   private static void au() {
      int var0 = 0;
      int var1 = df - 9;

      for (int var2 = 0; var2 < var1; var2++) {
         if (var2 == 0 || var2 == 3) {
            var0 += 2;
         } else if (var2 != 2 && var2 != 4) {
            var0 += 3;
         } else {
            var0++;
         }
      }

      int var3 = dh - var0;
      ef = dj[var1][var3];
      if (var3 == 0 && ei) {
         eg = 0;
      } else if (ei) {
         eg = dj[var1][var3 - 1];
      } else {
         eg = dj[var1][var3 + 1];
      }
   }

   private static void f(boolean var0) {
      if (var0) {
         dh--;
         if (dh < dg[df - 9]) {
            df--;
            if (df < 9) {
               df = 13;
            }

            dd = 1;
            if (dh < 0) {
               if (aH == 9) {
                  dh = 8;
                  return;
               }

               df = 9;
               dh = 0;
               dd = 0;
               return;
            }
         }
      } else {
         dh++;
         if (dh >= dg[df - 9 + 1]) {
            df++;
            if (df > 13) {
               df = 9;
            }

            dd = 1;
            if (dh >= 9) {
               if (aH == 9) {
                  dh = 0;
                  return;
               }

               df = 13;
               dh = 8;
               dd = 0;
            }
         }
      }
   }

   private static void av() {
      e(true);
      if (dd != 2) {
         a(h(0, 18), h(0, 17));
      } else {
         a(0, 296, 240, 24);
         b(0);
         b(0, 296, 240, 24);
      }

      if (eh != 2) {
         if (ei) {
            ef--;
         } else {
            ef++;
         }

         if (ef == eg) {
            eh++;
            if (eh == 1) {
               if (ei) {
                  f(true);
               } else {
                  f(false);
               }

               if (dd == 1) {
                  if (ei) {
                     int var0 = dj[df - 9].length;
                     eg = dj[df - 9][var0 - 2];
                     ef = dj[df - 9][var0 - 1];
                  } else {
                     ef = 0;
                     eg = dj[df - 9][0];
                  }
               } else {
                  eh = 2;
               }
            }
         }
      }

      if (eh != 0) {
         if (dd == 0) {
            if (C(8256)) {
               if (aH <= dh) {
                  return;
               }

               eh = 0;
               ei = false;
               au();
            } else if (C(4112)) {
               if (dh == 0 && aH != 9) {
                  return;
               }

               ei = true;
               eh = 0;
               au();
            }

            if (C(327712)) {
               aG = dh;
               a(9);
               if (dw) {
                  aa();
               }

               b.bC = false;
               a(cw, 644, (byte)(b.bC ? 1 : 0));
            } else if (C(131072)) {
               a(2);
               dw = false;
            }
         }

         aw();
      }
   }

   private static void aw() {
      if (dd == 2) {
         try {
            if (de == 2) {
               S(df);
            } else if (de == 5) {
               R(df);
            } else if (de == 8) {
               b(df, false);
            } else if (de == 8) {
               m(df, de);
            }
         } catch (Exception var1) {
            var1.printStackTrace();
         }

         if (de == 12) {
            dc = 0;
            dd = 3;
            de = 0;
            return;
         }

         de++;
      }
   }

   private static void ax() {
      a(0, 0, 240, 320);
      g(0, 0, 240, 320);
      a(0, 20, 240, 266);
      String var0 = null;
      if (x == 20) {
         var0 = h(0, 40);
      } else {
         var0 = h(0, 28);
      }

      a.z = x != 6 ? 0 : 0;
      if (a.a(d, var0, 120, ej, 17, 230, 0, -1) < 0) {
         ej = 266;
      } else {
         ej--;
      }

      if (y == 8) {
         a(h(0, 20), "");
      } else {
         a(h(0, 18), x == 6 ? "" : h(0, 17));
      }

      if (C(262144) && x == 20) {
         a(19);
         bP = true;
         df = 9;
      }

      if (!B(65568) && !B(33024)) {
         if (C(131072)) {
            if (y == 8 && a.a()) {
               a(27);
               return;
            }

            a(2);
            df = 9;
         }
      } else {
         ej -= 4;
      }
   }

   private static void ay() {
      try {
         a("/19");
         D(-1);
         r();
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   public static final void D(int var0) {
      try {
         a("/19");
         byte[] var1;
         if ((var1 = p(F(var0))) != null) {
            ep = null;
            ep = var1;
            r();
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   public static final void i(int var0, int var1) {
      if (var0 != -1 && dk) {
         if (var0 != el || ek == null || ek.getState() != 400) {
            switch (var0) {
               case 0:
               case 1:
               case 2:
               case 3:
                  el = var0;
               default:
                  F();

                  try {
                     ByteArrayInputStream var2;
                     (var2 = new ByteArrayInputStream(ep)).reset();
                     if (var0 < eo) {
                        ek = Manager.createPlayer(var2, "audio/midi");
                     } else {
                        ek = Manager.createPlayer(var2, "audio/x-wav");
                     }

                     ek.realize();
                     var2.close();
                  } catch (Exception var6) {
                     var6.printStackTrace();
                  }

                  Player var3 = ek;
                  if (ek != null) {
                     try {
                        var3.setLoopCount(var1);
                        var3.start();
                        em = -1;
                     } catch (Exception var5) {
                        c.a(var5, "play");
                        var5.printStackTrace();
                     }
                  }
            }
         }
      }
   }

   public static final void E(int var0) {
      i(var0, 1);
   }

   public static final void F() {
      try {
         if (ek != null) {
            if (ek.getState() == 400) {
               ek.stop();
            }

            if (ek.getState() == 300) {
               ek.deallocate();
            }

            ek.close();
            ek = null;
         }
      } catch (Exception var1) {
      }
   }

   public static final int F(int var0) {
      switch (var0) {
         case -1:
            return 0;
         case 0:
         case 1:
         case 5:
            return 1;
         case 2:
         case 3:
         case 4:
            return 2;
         case 6:
         case 7:
            return 3;
         case 8:
            return 4;
         default:
            return -1;
      }
   }

   public static final void G() {
      if (F(aG) != el || ek == null || ek.getState() != 400) {
         i(F(aG), -1);
      }
   }

   public static final String a(String var0, String var1, String var2) {
      StringBuffer var3 = new StringBuffer(var0);
      int var4 = var1.length();
      int[] var5 = new int[15];

      for (int var6 = 0; (var5[var6] = var3.toString().indexOf(var1)) != -1; var6++) {
         for (int var7 = 0; var7 < var4; var7++) {
            var3.deleteCharAt(var5[var6]);
         }
      }

      int var8 = 0;

      while (var5[var8] != -1) {
         var3.insert(var5[var8++], var2).toString();
         if (var5[var8] != -1) {
            var5[var8] += var2.length() * var8;
         }
      }

      return var5[0] != -1 ? var3.toString() : var0;
   }

   static {
      int[] var10000 = new int[]{636, 420, 660, 480};
      var10000 = new int[]{16724804, 3453695};
      var10000 = new int[]{-297151236, -297479947, -582956048, -868497173, -1153907488};
      var10000 = new int[]{
         0,
         3,
         4,
         5,
         6,
         8,
         9,
         14,
         -1,
         -1,
         -1,
         -1,
         15,
         20,
         -1,
         -1,
         21,
         22,
         23,
         26,
         27,
         28,
         29,
         32,
         -1,
         -1,
         35,
         37,
         -1,
         -1,
         -1,
         -1,
         38,
         43,
         -1,
         -1,
         44,
         46,
         47,
         47,
         48,
         48,
         49,
         52,
         -1,
         -1,
         -1,
         -1,
         -1,
         -1,
         -1,
         -1
      };
      var10000 = new int[]{-1441722095, -1155390942, -869059789, -582728636};
      var10000 = new int[]{327712, 4112, 8256, 16388, 33024};
      var10000 = new int[]{16317688, 16317688, 16317504, 16294936, 16252928};
      var10000 = new int[]{0, 4, 8, 13};
      short[] var7 = new short[]{2, 19, 36, 53, 70, 87, 104, 121, 138, 155, 172, 176};
      var7 = new short[]{34, 15, 76, 73, 147, 86, 165, 29, 218, 93, 117, 153, 36, 127, 58, 196, 184, 213, 208, 156, 48, 300, 135, 273};
      byte[] var9 = new byte[]{40, 4, 40, 4, 40, 4, 40, 4};
      String[] var10 = new String[]{"/3"};
      var9 = new byte[]{3, 5, 7, 8, 10};
   }
}
