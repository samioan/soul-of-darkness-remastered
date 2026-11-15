import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Random;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.rms.RecordStore;

// $VF: renamed from: e
final class Game extends Canvas implements Runnable {
   static BSprite a;
   private static BSprite eA;
   private static BSprite eB;
   private static BSprite eC;
   private static BSprite eD;
   public static BSprite[] b;
   private static Image eE;
   private static Image eF;
   private static Image eG;
   // $VF: renamed from: c java.util.Random
   public static Random random;
   // $VF: renamed from: d javax.microedition.lcdui.Graphics
   public static Graphics graphics;
   // $VF: renamed from: e boolean
   public static boolean paintComplete;
   static int f;
   static int g;
   static int h;
   public static int[] i;
   public static Image j;
   // $VF: renamed from: k long
   public static long lastPaintMs;
   static boolean l = false;
   public static int m = 0;
   public static int n = 0;
   public static int o = -256;
   static boolean p = false;
   public static final int[] q = new int[]{-16711936, -16733696, -16261377, -16759862};
   public static int[] r = new int[]{
      73, 120, 120, 73, 167, 120, 120, 167, 73, 120, 74, 120, 120, 74, 166, 120, 120, 166, 74, 120, 75, 120, 120, 75, 165, 120, 120, 165, 75, 120
   };
   public static int[] s = new int[]{64, 120, 120, 64, 176, 120, 120, 176};
   public static boolean t = false;
   public static int u = -41;
   public static int v;
   public static int w = 5;
   // $VF: renamed from: x int
   public static int health = 60;
   // $VF: renamed from: y int
   public static int maxHealth = 60;
   public static int z = 0;
   // $VF: renamed from: A int
   public static int magic = 60;
   // $VF: renamed from: B int
   public static int maxMagic = 60;
   public static int C = 0;
   public static int D = 0;
   public static long E = 0L;
   private static int eH;
   private static int eI;
   private static boolean eJ;
   private static int eK = 0;
   private static int eL = 0;
   private static int eM = 5;
   // $VF: renamed from: F int
   public static int state;
   // $VF: renamed from: G int
   public static int previousState;
   public static final int[] H = new int[]{636, 420, 660, 480};
   // $VF: renamed from: I int
   public static int stateFrame;
   public static long J;
   public static long K;
   public static long L;
   // $VF: renamed from: M int
   public static int frameDeltaMs;
   public static boolean N = false;
   // $VF: renamed from: O boolean
   public static boolean suspended;
   static int P = -1;
   static int Q;
   static int R;
   public static int S = 120;
   public static int T = 160;
   public static int U = 0;
   static final int[] V = new int[]{16744192, 255, 65280};
   static final int[] W = new int[]{16760187, 8355839, 8388479};
   public static int X = 0;
   public static int Y;
   static final int[] Z = new int[]{-297151236, -297479947, -582956048, -868497173, -1153907488};
   // $VF: renamed from: aa int
   public static int cameraX;
   // $VF: renamed from: ab int
   public static int cameraY;
   public static int ac;
   public static int ad;
   public static int ae;
   public static int af;
   // $VF: renamed from: ag int
   public static int regionLeft;
   // $VF: renamed from: ah int
   public static int regionRight;
   public static int ai;
   public static int aj;
   public static int ak;
   public static int al = 0;
   public static int am = 0;
   public static int an = 0;
   public static boolean ao;
   // $VF: renamed from: ap int[]
   public static int[] viewBounds = new int[4];
   public static boolean aq = false;
   public static boolean ar;
   public static boolean as;
   public static boolean at;
   public static boolean au;
   public static boolean av;
   public static boolean aw;
   public static boolean ax;
   public static boolean ay;
   public static int az;
   public static boolean aA;
   public static int aB = -1;
   public static int aC = -1;
   public static boolean aD = true;
   public static int aE;
   public static int aF = -1;
   public static int aG = -1;
   public static int aH;
   public static final int[] aI = new int[]{4, 12, 12, 1, 4, 4};
   public static Actor aJ;
   public static Actor aK;
   static int[] aL = new int[500];
   static int aM = -1;
   static int aN;
   static int aO;
   private static boolean eN;
   private static boolean eO = false;
   private static Image eP;
   private static BSprite eQ;
   public static boolean aP = false;
   public static boolean aQ = false;
   public static boolean aR = false;
   public static boolean aS = false;
   static boolean aT;
   public static int aU;
   public static int aV;
   public static int aW;
   public static int aX = 0;
   public static int[] aY = new int[8];
   public static int aZ;
   public static final int[] ba = new int[]{
      500, 1000, 2500, 500, 1000, 3000, 500, 1000, 2500, 800, 2500, 2000, 500, 1000, 2500, 500, 1000, 3000, 500, 1000, 2500, 800, 2500, 2000
   };
   public static boolean bb;
   public static final int[] bc = new int[]{2631720, 6710886, 10066329, 9276813, 4737096, 3750201};
   public static final int[] bd = new int[]{12910828, 16711935, 16742911, 16735231, 16711935, 14221567};
   private static boolean eR;
   static final int[] be = new int[]{0, 0, 0, 0, 3, 0, 2, 0, 0, 3, 0, 3, 2, 2, 2, 0, 0};
   // $VF: renamed from: bf int
   public static int levelIndex;
   // $VF: renamed from: bg int
   public static int maxUnlockedLevel = 0;
   public static boolean bh;
   public static boolean bi;
   public static int[] bj = new int[12];
   public static int[] bk = new int[12];
   static boolean bl = false;
   static byte bm;
   static boolean bn = true;
   public static int bo = 0;
   public static final int[] bp = new int[]{15488768, 16746240, 16760953, -19877, 16746240, 16736768};
   private static int eS;
   public static boolean bq;
   public static int br;
   public static int bs;
   public static int bt = 2;
   public static boolean bu = false;
   public static boolean bv = false;
   public static boolean bw = false;
   // $VF: renamed from: bx boolean
   public static boolean modalActive = false;
   public static int by;
   // $VF: renamed from: bz java.lang.String
   public static String dialogText;
   public static String bA;
   public static int bB;
   public static int bC;
   public static int bD;
   public static int bE;
   public static int bF;
   public static int bG;
   public static int bH;
   public static int bI;
   public static int bJ;
   public static Actor bK;
   public static int[] bL = new int[80];
   public static int[] bM = new int[400];
   public static int bN;
   public static int[] bO = new int[60];
   public static int bP;
   public static Actor[] bQ = new Actor[40];
   public static final int[] bR = new int[]{2631720, 6710886, 10066329, 9276813, 4737096, 3750201};
   public static final int[] bS = new int[]{41472, 63488, 4783944, 6029147, 33617920, 41472};
   public static final int[] bT = new int[]{15305728, 13930240, 15269753, 14942043, 13930240, 15305728};
   public static final int[] bU = new int[]{15466503, 16720128, 16747129, 16740443, 16720128, 16711684};
   private static int[] eT = new int[]{300, 350, 400, 500, 600, 700, 800, 900, 1000, 1200};
   private static int[] eU = new int[]{16711680, 16777215, 11010064};
   private static Image eV;
   static byte[] bV = new byte[]{
      1,
      2,
      3,
      4,
      1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      5,
      6,
      7,
      8,
      5,
      6,
      7,
      8,
      1,
      2,
      15,
      16,
      1,
      2,
      15,
      16,
      9,
      10,
      11,
      12,
      9,
      10,
      11,
      12,
      9,
      10,
      11,
      12,
      -1,
      -1,
      19,
      20,
      1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      1,
      2,
      15,
      16,
      9,
      10,
      11,
      12,
      -1,
      -1,
      19,
      20,
      5,
      6,
      7,
      8
   };
   static byte[] bW = new byte[]{
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
      1,
      1,
      1,
      1,
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
      1,
      1,
      1,
      1
   };
   private static Image eW;
   private static Graphics eX;
   private static boolean eY;
   private static int eZ;
   private static int fa;
   private static int fb;
   private static int fc;
   // $VF: renamed from: bX b
   public static Actor player;
   public static Actor bY;
   public static Actor bZ;
   public static Actor ca;
   public static Actor cb;
   public static int cc = 550;
   public static int cd = 150;
   public static int ce = 200;
   // $VF: renamed from: cf b[]
   public static Actor[] actors = new Actor[cc];
   // $VF: renamed from: cg int
   public static int actorCount;
   public static Actor[] ch = new Actor[cd];
   public static int ci;
   public static Actor[] cj = new Actor[ce];
   public static int ck;
   public static int[] cl = new int[cc];
   public static int cm = 0;
   public static int cn = 20;
   public static Actor[] co = new Actor[cn];
   public static int cp;
   public static byte[] cq = new byte[cc];
   static short[] cr = new short[20];
   public static byte[] cs;
   private static String[] fd = new String[]{"/3", "/4", "/5", "/6", "/7", "/8", "/9", "/10", "/11", "/12", "/13", "/14", "/15", "/16", "/17", "/18", "/19"};
   private static int[] fe = new int[52];
   public static short[][] ct = new short[11][30];
   public static short[] cu;
   public static int cv;
   public static short[] cw = new short[11];
   private static boolean ff;
   public static boolean cx;
   private static boolean fg;
   public static int cy;
   public static int cz;
   // $VF: renamed from: cA int
   public static int levelPixelWidth;
   public static int cB;
   private static byte[] fh;
   private static byte[] fi;
   private static byte[] fj;
   private static byte[] fk;
   private static byte[] fl;
   private static byte[] fm;
   private static byte[] fn;
   private static byte[] fo;
   private static byte[] fp;
   public static int cC;
   public static int cD;
   public static int cE;
   public static int cF;
   private static final String[] fq = new String[]{
      "/3", "/3", "/5", "/5", "/5", "/3", "/3", "/10", "/10", "/10", "/3", "/14", "/15", "/14", "/17", "/18", "/19"
   };
   static int[] cG;
   public static int[][] cH;
   private static String[][] fr = new String[13][];
   public static final int[] cI = new int[]{107, 57, 69, 109, 73, 71};
   private static String fs;
   private static InputStream ft;
   private static int fu;
   private static short fv;
   private static int[] fw;
   private static short fx;
   private static short[] fy;
   private static int fz;
   private static byte[] fA;
   static byte[] cJ;
   static int cK;
   static long cL;
   static long cM;
   static byte[] cN;
   static int cO;
   private static short[] fB;
   private static int fC = 0;
   public static int cP;
   private static byte[][] fD = new byte[][]{{1, 4, 5, 6, 7, 8, 9}, {12, 13, 6, 7, 0, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9}};
   public static int cQ = 110;
   public static boolean cR = false;
   public static boolean cS = false;
   public static boolean cT = true;
   static int cU = 0;
   public static byte[][][] cV;
   public static short[] cW;
   public static short[] cX;
   public static int[][] cY;
   public static boolean cZ;
   public static boolean da;
   public static boolean db = true;
   public static boolean dc = false;
   public static int dd = -1;
   public static int de = 0;
   public static int df;
   public static int[] dg = new int[3];
   public static byte[] dh = new byte[3];
   public static boolean di = true;
   static final int[] dj = new int[]{6, 2, 3, 0, 2, 1, 1, 4, 4, 0, 0, 8, 1, 0, 4, 0, 4, 2, 0, 0, 1, 0};
   static int dk = 0;
   static boolean dl = false;
   static boolean dm = false;
   static int dn = 0;
   // $VF: renamed from: do int
   static int do_ = 0;
   static final int[] dp = new int[]{327712, 4112, 8256, 16388, 33024};
   static final int[] dq = new int[]{25, 22, 28, 31, 19};
   static boolean dr;
   static final int ds = 1127 + cc + 64;
   static byte[] dt = new byte[ds];
   // $VF: renamed from: du boolean
   public static boolean saveLoaded = false;
   public static int dv;
   static int dw;
   static int dx;
   static int dy;
   static int dz;
   static int dA;
   static int dB;
   static int dC;
   static int dD;
   static int dE;
   static int dF = 0;
   static boolean dG = false;
   static final int[] dH = new int[]{2, 1, 1, 32};
   static short[] dI = new short[30];
   static byte[] dJ = new byte[30];
   static byte[] dK = new byte[20];
   static byte[] dL = new byte[20];
   static short[] dM = new short[30];
   static byte[] dN = new byte[30];
   static byte[] dO = new byte[30];
   static byte[] dP = new byte[30];
   static boolean dQ = false;
   static int dR = 20;
   static boolean dS = false;
   static boolean dT = false;
   static int dU;
   static boolean dV = false;
   static final int[] dW = new int[]{-1996488705, -1426063361};
   public static final int[] dX = new int[]{-1428431909, -1145324613, 2004318071};
   public static int dY = 70;
   public static int dZ = 70;
   public static int ea = 0;
   public static int eb = 180;
   public static int[] ec = new int[]{872414976, 1728052992, -1711276288};
   public static int ed = 0;
   public static int ee = 0;
   public static int ef = 0;
   private static int fE = 0;
   private static boolean fF = false;
   public static int eg = 11;
   public static final int[] eh = new int[]{0, 2, 5, 7, 10, 11, 14};
   public static int ei;
   public static final byte[] ej = new byte[]{-67, -4, 35, -6, -79, -7, -16, -13, 50, -4, -79, -3, 50, -5, -59, -5, 7, 4, 70, -2, -16, -13};
   public static final byte[][] ek = new byte[][]{{4, 12, 18}, {3, 8, 13, 18}, {3, 13, 18}, {5, 10, 15, 18}, {8, 18}};
   private static final byte[][] fG = new byte[][]{
      {
            -118,
            1,
            -105,
            -6,
            -91,
            -9,
            -77,
            -6,
            -67,
            -4,
            -52,
            3,
            -38,
            -1,
            -27,
            -9,
            -14,
            -9,
            -3,
            -2,
            10,
            1,
            23,
            -5,
            35,
            -6,
            54,
            0,
            67,
            0,
            77,
            -8,
            88,
            -15,
            101,
            -14,
            113,
            -10
      },
      {
            -118,
            1,
            -105,
            -6,
            -91,
            -9,
            -79,
            -7,
            -52,
            3,
            -64,
            -1,
            -38,
            -1,
            -27,
            -9,
            -16,
            -13,
            -3,
            -2,
            10,
            1,
            24,
            -1,
            37,
            -4,
            50,
            -4,
            66,
            -3,
            77,
            -8,
            89,
            -2,
            101,
            3,
            114,
            5
      },
      {
            -118,
            -6,
            -105,
            1,
            -91,
            4,
            -79,
            -2,
            -64,
            -4,
            -52,
            -8,
            -38,
            -4,
            -27,
            4,
            -16,
            4,
            -3,
            -3,
            10,
            -6,
            24,
            -4,
            37,
            -1,
            50,
            -5,
            66,
            -2,
            77,
            3,
            89,
            -3,
            101,
            -8,
            114,
            -10
      },
      {
            -119,
            -10,
            -106,
            -8,
            -94,
            -3,
            -82,
            3,
            -71,
            -2,
            -59,
            -5,
            -42,
            -1,
            -29,
            -4,
            -15,
            -6,
            -2,
            -3,
            7,
            4,
            22,
            4,
            33,
            -4,
            47,
            -8,
            59,
            -4,
            70,
            -2,
            86,
            4,
            100,
            1,
            113,
            -6
      },
      {
            -118,
            1,
            -105,
            -6,
            -91,
            -9,
            -79,
            -7,
            -52,
            3,
            -64,
            -1,
            -38,
            -1,
            -27,
            -9,
            -16,
            -13,
            -3,
            -2,
            10,
            1,
            24,
            -1,
            37,
            -4,
            50,
            -4,
            66,
            -3,
            77,
            -8,
            89,
            -2,
            101,
            3,
            114,
            5
      }
   };
   private static int fH = 0;
   private static int fI = 0;
   private static int fJ = 2;
   private static boolean fK;
   private static int fL;
   private static Player fM;
   public static boolean el = true;
   private static int fN;
   // $VF: renamed from: fO int
   private static int resumeSoundId = -1;
   // $VF: renamed from: fP int
   private static int soundResumeFrames;
   private static int fQ = 18;
   private static byte[][] fR = new byte[18][];
   static byte[] em = new byte[]{-1, -1, -1, -1, 2, 4, 6, 8, -1, -1, -1, -1, 2, 4, 6, 8};
   static short[] en = new short[]{
      7,
      8,
      9,
      10,
      11,
      12,
      13,
      14,
      16,
      17,
      19,
      21,
      23,
      25,
      28,
      31,
      34,
      37,
      41,
      45,
      50,
      55,
      60,
      66,
      73,
      80,
      88,
      97,
      107,
      118,
      130,
      143,
      157,
      173,
      190,
      209,
      230,
      253,
      279,
      307,
      337,
      371,
      408,
      449,
      494,
      544,
      598,
      658,
      724,
      796,
      876,
      963,
      1060,
      1166,
      1282,
      1411,
      1552,
      1707,
      1878,
      2066,
      2272,
      2499,
      2749,
      3024,
      3327,
      3660,
      4026,
      4428,
      4871,
      5358,
      5894,
      6484,
      7132,
      7845,
      8630,
      9493,
      10442,
      11487,
      12635,
      13899,
      15289,
      16818,
      18500,
      20350,
      22385,
      24623,
      27086,
      29794,
      32767
   };
   protected static final byte[] eo = new byte[]{82, 73, 70, 70};
   protected static final byte[] ep = new byte[]{87, 65, 86, 69};
   protected static final byte[] eq = new byte[]{102, 109, 116, 32};
   protected static final byte[] er = new byte[]{100, 97, 116, 97};
   protected static final byte[] es = new byte[]{16, 0, 0, 0};
   protected static final byte[] et = new byte[]{1, 0};
   protected static final byte[] eu = new byte[]{1, 0};
   protected static final byte[] ev = new byte[]{64, 31, 0, 0};
   protected static final byte[] ew = new byte[]{-128, 62, 0, 0};
   protected static final byte[] ex = new byte[]{2, 0};
   protected static final byte[] ey = new byte[]{16, 0};
   public static Image[] ez;

   Game() {
      int[] var10000 = new int[]{0, 1};
      this.setFullScreenMode(true);
      suspended = false;
      random = new Random(System.currentTimeMillis());
      state = 0;
      previousState = -1;
      stateFrame = 0;
   }

   public final void paint(Graphics var1) {
      if (this.getWidth() > this.getHeight()) {
         l = true;
         var1.setColor(0);
         var1.setClip(0, 0, 320, 240);
         var1.fillRect(0, 0, 320, 240);
         var1.setColor(16777215);
         var1.setFont(Font.getFont(0, 0, 8));

         try {
            V(0);
         } catch (Exception var3) {
         }

         String var2 = k(0, 105);
         var1.drawString(var2, 160, 80, 17);
         if (!suspended) {
            T();
            this.e();
         }
      } else {
         if (l) {
            this.d();
            l = false;
         }

         if (System.currentTimeMillis() - lastPaintMs >= 2500L && lastPaintMs > 0L) {
            this.hideNotify();
            this.showNotify();
         }

         lastPaintMs = System.currentTimeMillis();

         try {
            graphics = var1;
            stateFrame++;
            h = Math.min(frameDeltaMs, 50);
            f = (h + 12) / 50;
            f = Math.min(f, 1);
            g = (h << 8) / 50;
            switch (state) {
               case 0:
                  au();
                  break;
               case 1:
                  ad();
                  break;
               case 2:
                  this.aK();
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
               default:
                  break;
               case 5:
                  ah();
                  break;
               case 6:
               case 20:
                  aQ();
                  if (state == 6) {
                     av();
                  }
                  break;
               case 7:
                  ai();
                  break;
               case 8:
                  Actor.bt = graphics;
                  ap();
                  break;
               case 9:
                  at();
                  break;
               case 12:
                  ag();
                  break;
               case 14:
                  am();
                  break;
               case 15:
                  av();
                  if (br == bs) {
                     this.Z();
                  }
                  break;
               case 18:
                  aL();
                  break;
               case 19:
                  aO();
                  break;
               case 23:
                  ae();
                  break;
               case 27:
                  p();
                  break;
               case 30:
                  af();
                  break;
               case 31:
                  ak();
                  break;
               case 32:
                  L(eI);
            }

            graphics = null;
            L();
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         paintComplete = true;
         this.b();
      }
   }

   protected final void pointerPressed(int var1, int var2) {
      m = var1;
      n = var2;
      if (state == 7) {
         InGamePromotion.a(var1, var2);
      }
   }

   protected final void pointerReleased(int var1, int var2) {
      m = -1000;
      n = -1000;
      this.keyReleased(o);
      o = -256;
   }

   private void W() {
      if (state == 8 && !modalActive) {
         if (m > 0 && n > 297 && m < 30 && n < 320) {
            this.keyPressed(-6);
         }

         if (m > 210 && n > 297 && m < 240 && n < 320) {
            this.keyPressed(-7);
            return;
         }
      } else {
         if (m > 0 && n > 297 && m < 65 && n < 320) {
            this.keyPressed(-6);
            return;
         }

         if (m > 175 && n > 297 && m < 240 && n < 320) {
            this.keyPressed(-7);
         }
      }
   }

   public final void a() {
      m = -1000;
      n = -1000;
   }

   public final void b() {
      this.W();
      label489:
      switch (state) {
         case 0:
         case 1:
         case 3:
         case 4:
         case 5:
         case 7:
         case 10:
         case 11:
         case 12:
         case 13:
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
         case 30:
         default:
            break;
         case 2:
            if (m < 57 && n > 235 && n < 273) {
               this.keyPressed(52);
            }

            if (m > 186 && n > 235 && n < 273) {
               this.keyPressed(54);
            }

            if (m < 186 && m > 57 && n > 235 && n < 273) {
               this.keyPressed(53);
            }
            break;
         case 6:
         case 20:
            if (n > 0 && n < 273) {
               this.keyPressed(53);
            }
            break;
         case 8:
            int var18 = player.positionX - cameraX;
            int var3 = player.positionY - cameraY;
            if (modalActive) {
               if (bJ == 1) {
                  if (m > 55 && m < 95 && n > 140 && n < 190) {
                     this.keyPressed(52);
                  }

                  if (m > 147 && m < 210 && n > 140 && n < 190) {
                     this.keyPressed(54);
                  }
               }

               if ((bJ == 14 || bJ == 5) && m > 0 && n < 243) {
                  this.keyPressed(-6);
               }
            } else if (m < 85 && m > 45 && n < 330 && n > 293) {
               this.keyPressed(53);
            } else if (m < 141 && m > 101 && n < 330 && n > 293) {
               this.keyPressed(48);
            } else if (m > 158 && m < 200 && n < 330 && n > 293) {
               this.keyPressed(57);
            } else if (player.aq == 1) {
               if (m < var18 + 15 && m > var18 - 15 && n > var3 && n < var3 - 30) {
                  this.keyPressed(53);
               } else if (m > var18 + 15 && n < var3 + 50 && n > var3 - 50) {
                  if (p) {
                     this.keyPressed(53);
                  } else {
                     this.keyPressed(54);
                  }
               } else if (m > var18 - 15 && m < var18 + 15 && n < var3 && n > var3 - 15) {
                  this.keyPressed(-5);
               } else if (m < var18 - 15 && m > 0 && n < var3 + 50 && n > var3 - 50) {
                  if (p) {
                     this.keyPressed(53);
                  } else {
                     this.keyPressed(52);
                  }
               } else if (m < var18 + 15 && m > var18 - 15 && n > var3) {
                  this.keyPressed(56);
               } else if (m < var18 + 15 && m > var18 - 15 && n < var3 - 15) {
                  this.keyPressed(50);
               } else if (m > 0 && m < var18 - 15 && n < var3 - 15) {
                  this.keyPressed(49);
               } else if (m > var18 + 15 && n < var3 - 15) {
                  this.keyPressed(51);
               }
            } else if (m > var18 + 20 && n < var3 + 75 && n > var3 - 60) {
               if (p) {
                  this.keyPressed(53);
               } else {
                  this.keyPressed(54);
               }
            } else if (m < var18 - 20 && m > 0 && n < var3 + 75 && n > var3 - 60) {
               if (p) {
                  this.keyPressed(53);
               } else {
                  this.keyPressed(52);
               }
            } else if (m > var18 - 20 && m < var18 + 20 && n < var3 + 5 && n > var3 - 60) {
               this.keyPressed(-5);
            } else if (m > var18 + 20 && n < var3 - 60) {
               this.keyPressed(51);
            } else if (m > 0 && m < var18 - 20 && n < var3 - 60) {
               this.keyPressed(49);
            } else if (m < var18 + 20 && m > var18 - 20 && n > var3) {
               this.keyPressed(56);
            } else if (m < var18 + 20 && m > var18 - 20 && n < var3 - 60) {
               this.keyPressed(50);
            }
            break;
         case 9:
            if (m > 0 && n > 0) {
               this.keyPressed(53);
            }
            break;
         case 14:
            int[] var10 = new int[]{35, 70, 105, 140, 175, 210, 245};

            for (int var17 = 0; var17 < 6; var17++) {
               if (n > var10[var17] && n < var10[var17 + 1]) {
                  cP = var17;
                  this.keyPressed(53);
                  break label489;
               }
            }
            break;
         case 18:
            if (m > 0 && n > 0) {
               this.keyPressed(53);
            }
            break;
         case 19:
            if (n > 194 && n < 232) {
               if (ei < 2) {
                  int[] var5 = new int[]{0, 106, 206};

                  for (int var12 = 0; var12 < 2; var12++) {
                     if (m > var5[var12] && m < var5[var12 + 1]) {
                        if (ei == var12) {
                           this.keyPressed(53);
                        } else if (var12 < maxUnlockedLevel) {
                           ei = var12;
                        }
                        break;
                     }
                  }
               }

               if (1 < ei && ei < 5) {
                  int[] var6 = new int[]{0, 0, 0, 72, 136, 220};

                  for (int var13 = 2; var13 < 5; var13++) {
                     if (m > var6[var13] && m < var6[var13 + 1]) {
                        if (ei == var13) {
                           this.keyPressed(53);
                        } else if (var13 < maxUnlockedLevel) {
                           ei = var13;
                        }
                        break;
                     }
                  }
               }

               if (4 < ei && ei < 7) {
                  int[] var7 = new int[]{0, 0, 0, 0, 0, 0, 106, 209};

                  for (int var14 = 5; var14 < 7; var14++) {
                     if (m > var7[var14] && m < var7[var14 + 1]) {
                        if (ei == var14) {
                           this.keyPressed(53);
                        } else if (var14 < maxUnlockedLevel) {
                           ei = var14;
                        }
                        break;
                     }
                  }
               }

               if (6 < ei && ei < 10) {
                  int[] var8 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 90, 156, 220};

                  for (int var15 = 7; var15 < 10; var15++) {
                     if (m > var8[var15] && m < var8[var15 + 1]) {
                        if (ei == var15) {
                           this.keyPressed(53);
                        } else if (var15 < maxUnlockedLevel) {
                           ei = var15;
                        }
                        break;
                     }
                  }
               }

               if (9 < ei) {
                  int[] var9 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 84, 121};

                  for (int var16 = 10; var16 < 11; var16++) {
                     if (m > var9[var16] && m < var9[var16 + 1]) {
                        if (ei == var16) {
                           this.keyPressed(53);
                        } else if (var16 < maxUnlockedLevel) {
                           ei = var16;
                        }
                        break;
                     }
                  }
               }
            }

            if (m > 0 && m < 65 && n > 234 && n < 275) {
               this.keyPressed(52);
            }

            if (m > 172 && m < 240 && n > 234 && n < 275) {
               this.keyPressed(54);
            }

            if (m > 65 && m < 172 && n > 234 && n < 275) {
               this.keyPressed(53);
            }
            break;
         case 31:
            if (m > 38 && m < 208 && n < 78 && n > 18) {
               this.keyPressed(54);
            }

            if (Actor.bD) {
               int[] var4 = new int[]{90, 125, 158, 190, 216};

               for (int var11 = 0; var11 < 4; var11++) {
                  if (n > var4[var11] && n < var4[var11 + 1]) {
                     aW = var11;
                     break;
                  }
               }
            } else {
               int[] var1 = new int[]{90, 125, 158, 190};

               for (int var2 = 0; var2 < 3; var2++) {
                  if (n > var1[var2] && n < var1[var2 + 1]) {
                     aW = var2;
                     break;
                  }
               }
            }

            if (m > 22 && n < 262 && n > 85) {
               this.keyPressed(53);
            }
            break;
         case 32:
            if (m > 0 && n > 0) {
               this.keyPressed(53);
            }
      }

      this.a();
   }

   private static void X() {
      if (player.animationId != 11 && (player.animationId < 16 || player.animationId > 33) && (player.animationId < 38 || player.animationId > 51) && !bu) {
         t = false;
      } else {
         t = true;
         v = 0;
         bu = false;
      }

      if (t && u < 0) {
         u += 5;
         if (u > 0) {
            u = 0;
         }
      }

      if (!t && u > -41) {
         v = v + h;
         if (v >= 4000) {
            u -= 5;
         } else if (u < 0) {
            u += 5;
            if (u > 0) {
               u = 0;
            }
         }

         if (u < -41) {
            u = -41;
         }
      }
   }

   private static int a(int var0, int var1, int var2, boolean var3) {
      String var4;
      int var5 = (var4 = var0 + "").length();
      int var6 = 0;
      if (!var3) {
         var1 -= 6 * var5;
      }

      for (int var8 = 0; var8 < var5; var8++) {
         int var7 = var4.charAt(var8) - '0';
         b[33].a(graphics, 2 + var7, var6 + var1, var2, 0, 0, 0);
         var6 += 6;
      }

      return var6;
   }

   public static final void a(int var0, int var1, int var2, int var3, int var4, int var5) {
      b(var0);
      b(var2, var3, var5 * 60 / 60, var4);
      b(var1);
      b(var2 + 1, var3 + 1, var5 * 60 / 60 - 2, var4 - 2);
      b(var0);
      if (var5 > 0) {
         for (int var6 = 0; var6 <= (var5 * 60 / 60 - 2) / w; var6++) {
            d(var2 + var6 * w, var3, var2 + var6 * w, var3 + var4 - 1);
         }
      }
   }

   private static void Y() {
      b[33].a(graphics, 0, 0, u, 0, 0, 0);
      b(15773760);
      c(51, 8 + u, maxHealth * 60 / 60 + 14, 8);
      b(0);
      b(65, 9 + u, maxHealth * 60 / 60, 7);
      a(1611800, 63488, 65, 10 + u, 5, health);
      b(8402944);
      c(65, 17 + u, maxHealth * 60 / 60, 1);
      b(15773760);
      c(51, 18 + u, maxMagic * 60 / 60 + 14, 8);
      b(0);
      b(65, 19 + u, maxMagic * 60 / 60, 7);
      b(2132128);
      b(65, 20 + u, magic * 60 / 60, 5);
      b(3725552);
      b(66, 21 + u, magic * 60 / 60 - 2, 3);
      a(2132128, 3725552, 65, 20 + u, 5, magic);
      b[33].a(graphics, 1, 0, 0 + u, 0, 0, 0);
      a(D, 67, 29 + u, true);
      b[33].a(graphics, 20 + Actor.ch, -1, u, 0, 0, 0);
      if (E > 0L) {
         E = E - h;
         if (E <= 0L) {
            N();
         }

         b[33].a(graphics, 4, stateFrame % 4 < 2 ? 0 : 1, player.positionX - cameraX, player.positionY - cameraY - 70, 0, 0, 0);
      }
   }

   public static final void c() {
      if (Actor.bE == 2) {
         b[16].a(graphics, 13, stateFrame % 4 >> 1, 170, 250, 1, 0, 0);
      }

      if (Actor.bE != 2 && di) {
         b[33].a(graphics, 35, 10, 307, 0, 0, 0);
      }

      b[33].a(graphics, 36, 230, 307, 0, 0, 0);
      if (aj() || Actor.bE == 2) {
         a(stateFrame % 8, 213, 297, 20, 20);
      }
   }

   private static void L(int var0) {
      as();
      b(-587202560);
      a(19, 30, 202, 252);
      b(19, 30, 202, 252);
      b[33].a(graphics, 25, 119, 166, 0, 0, 0);
      b(15692292);
      d(20, 50, 20, 262);
      d(219, 31, 219, 262);
      int var1 = var0 == 0 ? z : C;
      if (eJ) {
         var1 = 4;
      }

      eH++;
      if (!eJ && eH == 5) {
         if (var0 == 0) {
            if (var1 == 4) {
               z = 0;
               maxHealth += 10;
               if (maxHealth > 120) {
                  maxHealth = 120;
                  if (maxUnlockedLevel == 11) {
                     al();
                  }
               }

               health = maxHealth;
               eJ = true;
            } else {
               health += 10;
               if (health > maxHealth) {
                  health = maxHealth;
               }
            }
         } else if (var0 == 1) {
            if (var1 == 4) {
               C = 0;
               maxMagic += 10;
               if (maxMagic > 120) {
                  maxMagic = 120;
               }

               magic = maxMagic;
               eJ = true;
            } else {
               if (Actor.bD) {
                  magic += 10;
               }

               if (magic > maxMagic) {
                  magic = maxMagic;
               }
            }
         }
      }

      for (int var2 = -1; var2 <= 1; var2++) {
         b(q[(var2 == 0 ? 0 : 1) + var0 * 2]);
         c(55 + var2, 55 + var2, 65 - var2 << 1, 65 - var2 << 1, 0, 360);
         c(74 + var2, 74 + var2, 46 - var2 << 1, 46 - var2 << 1);
         c(74 + var2, 74 + var2, 46 - var2 << 1, 46 - var2 << 1, 0, 360);

         for (int var3 = 0; var3 < 4; var3++) {
            d(r[var3 * 2 + (var2 + 1) * 10], r[var3 * 2 + 1 + (var2 + 1) * 10], r[var3 * 2 + 2 + (var2 + 1) * 10], r[var3 * 2 + 3 + (var2 + 1) * 10]);
            b[33].a(graphics, 50, s[var3 * 2], s[var3 * 2 + 1], 0, 0, 0);
            if (var3 < var1) {
               b[31].z = 0;
               b[31].a(graphics, var0 == 0 ? 1 : 3, 0, s[var3 * 2], s[var3 * 2 + 1], 0, 0, 0);
            }
         }
      }

      a.z = 5;
      String var4;
      if (var1 < 4) {
         var4 = k(0, 60 + var0).replace('#', (char)(52 - var1));
      } else {
         var4 = k(0, 62 + var0);
      }

      a.a(graphics, var4, 120, 195, 17, 180, 0, -1);
      if (eH > 10) {
         if (stateFrame % 10 < 5) {
            a.a(graphics, k(0, 106), 120, 262, 17, 180, 0, -1);
         }

         if (M()) {
            state = 8;
            eJ = false;
            di = true;
            U();
         }
      }
   }

   protected final void hideNotify() {
      if (fN >= 0 && fM != null && fM.getState() == 400) {
         resumeSoundId = fN;
      }

      T();
      suspended = true;
   }

   protected final void showNotify() {
      suspended = false;
      if (state != 0 && state != 1 && state != 9 && state != 15) {
         stateFrame = 0;
      }

      if (state == 8 && !modalActive && !hasActiveBackgroundScript()) {
         cP = 0;
         a(14);
      }

      if (resumeSoundId >= 0 && state != 14) {
         soundResumeFrames = 3;
      }

      N();
   }

   private void Z() {
      as();
      g(0, 0, 240, 320);
      a("", k(0, 17));
      a(0, 5, 240, 285);
      if (eL == 0) {
         eL = 1;
      } else if (eL == 2) {
         if (eK == 0) {
            eL = 3;
         }

         int var1 = eK / 2;
         if (eK % 2 == 0) {
            var1 = -var1;
         }

         b[37].a(graphics, 5, 120, 160 + var1, 0, 0, 0);
         b[37].a(graphics, 6, 120, 160 + var1, 0, 0, 0);
         eK--;
      } else if (eL == 1) {
         b[37].a(graphics, 5, 120, 0 + eM, 0, 0, 0);
         b[37].a(graphics, 6, 120, 320 - eM, 0, 0, 0);
         eK++;
         if (0 + eM >= 160) {
            eK = 15;
            eL = 2;
         } else {
            eM = eM + eK * 5;
         }
      } else {
         b[37].a(graphics, 5, 120, 160, 0, 0, 0);
         b[37].a(graphics, 6, 120, 160, 0, 0, 0);
      }

      if (G(327712) && eL == 3) {
         eK = 0;
         eL = 0;
         eM = 5;
         bs = 0;
         br = 0;
         if (al()) {
            a(31);
            eR = true;
            return;
         }

         c(true);
      }
   }

   public static final void a(boolean var0) {
      ei = levelIndex + (var0 ? 1 : 0);
      if (levelIndex + (var0 ? 1 : 0) == 11) {
         ei = 0;
         eg = 11;
      } else {
         while (eh[eg - 11 + 1] <= ei) {
            eg++;
         }
      }
   }

   public static final void a(int var0) {
      if (var0 == 9) {
         T();
         eS = levelIndex == 0 ? 0 : i(0, 7);
         soundResumeFrames = 0;
      } else if (var0 == 12) {
         T();
         I(5);
      } else if (var0 == 15) {
         T();
         I(6);
         bs = 1;
         br = 160;
      } else if (var0 == 8 && state == 9) {
         soundResumeFrames = 0;
         if (levelIndex != 4 && levelIndex != 9 && levelIndex != 0 && levelIndex != 15 || fg) {
            U();
         }

         fg = false;
      }

      if (var0 == 20) {
         ei = 0;
         ed = 0;
         fL = 240;
         eg = 12;
         U = 12;
      }

      if (var0 == 19 || var0 == 20) {
         fC = 0;
         ee = 2;
         ef = 0;
         U = 12;
         ax();
      }

      if (var0 == 6) {
         fL = 280;
         String var1 = k(0, 28);
         I(7);
         String var2;
         if ((var2 = GloftSOFD.instance.getAppProperty("MIDlet-Version")) == null) {
            var2 = "0.0.0";
         }

         int var3;
         if ((var3 = var1.indexOf("X.X.X")) != -1) {
            fr[0][28] = var1.substring(0, var3) + " " + var2 + var1.substring(var3 + 5);
         }

         ee = 0;
      }

      if (var0 == 2) {
         cP = 0;
         if (b(dt, 16) == 0) {
            fC = 0;
         } else {
            fC = 2;
            cP = 1;
         }

         switch (state) {
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
               T();
            case 2:
            case 3:
            case 5:
            case 6:
            case 7:
            case 18:
            case 19:
            case 20:
            case 30:
               I(0);
         }
      }

      if (state == 2 && var0 != 2) {
         eG = null;
      } else if (var0 == 2 && state != 2) {
         try {
            if (eG == null) {
               eG = Image.createImage("/new.png");
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      if (var0 == 14) {
         fC = 1;
      }

      if (var0 == 14 || var0 == 7) {
         T();
      }

      if (var0 == 9) {
         eE = null;
      }

      bi = false;
      if (var0 == 13 || var0 == 12 || var0 == 14 || var0 == 31 || var0 == 15 || var0 == 32) {
         bi = true;
      }

      if (var0 == 8 && (state == 14 || state == 12)) {
         player.g();
         if (levelIndex != 9 || !Actor.b(player.collisionBounds, H)) {
            U();
         }
      }

      if (var0 == 31) {
         aV = Actor.ch;
      }

      stateFrame = 0;
      previousState = state;
      state = var0;
      N();
   }

   public final void run() {
      while (state != 11) {
         K = System.currentTimeMillis();
         frameDeltaMs = (int)(K - L);
         if (frameDeltaMs > 1000) {
            frameDeltaMs = 1000;
         }

         L = K;
         if (!suspended) {
            if ((state != 14 && state != 8 && state != 9 || state == 8 && modalActive) && (state != 5 || previousState != 14) && soundResumeFrames > 0) {
               while (--soundResumeFrames == 0) {
                  playSound(resumeSoundId, -1);
               }
            }

            paintComplete = false;
            this.repaint();

            while (!paintComplete) {
               try {
                  Thread.sleep(10L);
               } catch (Exception var4) {
               }
            }
         }

         K = Math.min(K, System.currentTimeMillis());
         J = System.currentTimeMillis() - K;
         boolean var1 = hasActiveBackgroundScript();

         try {
            Thread.sleep(Math.max(1L, (var1 ? 66 : 50) - J));
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      GloftSOFD.instance.destroyApp(true);
   }

   public final void d() {
      if (suspended) {
         suspended = false;
      }
   }

   public final void e() {
      suspended = true;
   }

   private static void j(int var0, int var1, int var2, int var3) {
      graphics.setClip(var0, var1, var2, var3);
   }

   public static final void a(int var0, int var1, int var2, int var3) {
      graphics.setClip(var0, var1, var2, var3);
   }

   public static final void b(int var0) {
      graphics.setColor(var0);
   }

   public static final void b(int var0, int var1, int var2, int var3) {
      graphics.fillRect(var0, var1, var2, var3);
   }

   public static final void c(int var0, int var1, int var2, int var3) {
      graphics.drawRect(var0, var1, var2, var3);
   }

   public static final void d(int var0, int var1, int var2, int var3) {
      graphics.drawLine(var0, var1, var2, var3);
   }

   public static final void a(Image var0, int var1, int var2, int var3) {
      graphics.drawImage(var0, var1, var2, var3);
   }

   public static final void b(int var0, int var1, int var2, int var3, int var4, int var5) {
      graphics.fillArc(var0, var1, var2, var3, var4, var5);
   }

   public static final void c(int var0, int var1, int var2, int var3, int var4, int var5) {
      graphics.drawArc(var0, var1, var2, var3, var4, var5);
   }

   static final void f() {
      if (dialogText != null) {
         c(0);
         P++;
         if (Actor.bE == 2 && G(131072)) {
            Actor.bE = 0;
            dialogText = null;
            s();
            a(31);
            return;
         }
      } else {
         s();
         if (Actor.bE == 1) {
            Actor.b(0, 57, true);
            Actor.bE = 2;
         }
      }
   }

   static final void a(int var0, boolean var1, int var2) {
      b(-2013265920);
      b(0, 0, 240, 320);
      int var3 = Q - cameraX + 0;
      int var4 = R - cameraY + 0;
      if (var1 || !modalActive) {
         P++;
      }

      if (P <= 8) {
         int var12 = (var4 << 8) / 8 * P >> 8;
         b(V[var0]);
         b(var3 - 15, var4 - var12, 30, var12);
         b(W[var0]);
         b(var3 - 15 - 16, var4 - var12, 16, var12);
         b(var3 + 15, var4 - var12, 16, var12);
      } else if (P <= 32 + var2) {
         int var9;
         if ((var9 = (P - 8 + var2 - 1) % 8) > 3) {
            var9 = 7 - var9;
         }

         int var11 = 3840 * var9 / 24 >> 8;
         int var14 = 2048 * var9 / 24 >> 8;
         b(V[var0]);
         b(var3 - 15 + var11, 0, (15 - var11) * 2, var4);
         b(W[var0]);
         b(var3 - 15 - (8 - var14) * 2 + var11, 0, (8 - var14) * 2, var4);
         b(var3 + 15 - var11, 0, (8 - var14) * 2, var4);
      } else if (P <= 56 + var2 * 2) {
         int var8 = P - 32 - 1 + var2 * 2;
         int var10 = 3840 * var8 / 24 >> 8;
         int var13 = 2048 * var8 / 24 >> 8;
         b(V[var0]);
         b(var3 - 15 + var10, 0, (15 - var10) * 2, var4);
         b(W[var0]);
         b(var3 - 15 - (8 - var13) * 2 + var10, 0, (8 - var13) * 2, var4);
         b(var3 + 15 - var10, 0, (8 - var13) * 2, var4);
      } else if (P <= 64 + var2 * 2) {
         int var5 = P - 56 + var2 * 2;
         int var6 = (var4 << 8) / 8;
         int var7 = var5 * var6 >> 8;
         b(V[var0]);
         b(var3 - 1, 0, 2, var4 - var7);
      } else {
         U();
         if (var1) {
            if (dialogText != null) {
               modalActive = false;
               player.c &= -4097;
               h(14);
            } else {
               s();
            }
         } else {
            dc = false;
            dd = -1;
            de = 0;
         }
      }
   }

   public static final void e(int var0, int var1, int var2, int var3) {
      b(-587202560);
      b(var0, var1, var2, var3);
      b(15700800);
      c(var0 + 1, var1, var2 - 3, var3);
      b(9781250);
      d(var0, var1 + 1, var0 + var2 - 1, var1 + 1);
      b(4196354);
      d(var0, var1 + 3, var0 + var2 - 1, var1 + 3);
   }

   public static final void c(int var0) {
      int var1;
      if ((var1 = P - var0) <= 8) {
         boolean var2 = false;
         boolean var3 = false;
         boolean var4 = false;
         e(120 - 15 * var1, 160 - 20 * var1, 15 * var1 * 2, 4 * var1);
      } else {
         e(0, 0, 240, 70);
         b[33].a(graphics, 23, 0, 0, 0, 0, 0);
         b[33].a(graphics, 23, 240, 0, 1, 0, 0);
         b[33].a(graphics, 24, 39, 0, 0, 0, 0);
         b[33].a(graphics, 24, 201, 0, 1, 0, 0);
         a.z = 5;
         a.a(graphics, dialogText, 25, 10, 20, 180, bH, 3);
         b[33].a(graphics, 6, stateFrame % 6 >> 1, 232, 55, 0, 0, 0);
         if (G(327712)) {
            if (bI > 3) {
               bI -= 3;
               bH += 3;
            } else if (Actor.bE != 2) {
               dialogText = null;
               N();
            }
         }
      }

      if (Actor.bE != 2) {
         a("", k(0, 17));
      }
   }

   public static final void g() {
      int var0 = X;
      j(0, 0, 240, 320);
      boolean var1 = false;
      boolean var2 = false;
      if (var0 <= 4) {
         int var3 = bC + (120 - bC) * var0 / 5;
         int var4 = bD + (35 - bD) * var0 / 5;
         int var5 = 24 * var0;
         int var6 = 7 * var0;
         e(var3 - var5, var4 - var6, var5 * 2, var6 * 2);
      } else {
         boolean var7 = bF != -1 && bF <= 2;
         e(0, 0, 240, 70);
         b[33].a(graphics, 24, 0, 0, 0, 0, 0);
         b[33].a(graphics, 23, 240, 3, 1, 0, 0);
         b[33].a(graphics, 24, 240, 0, 1, 0, 0);
         if (bG == 0) {
            if (var7) {
               b[42].a(graphics, bF, 3, 10, 0, 0, 0);
            }

            a.a(graphics, bA, var7 ? 55 : 5, 10, 20);
            a.z = 5;
            a.a(graphics, dialogText, var7 ? 55 : 5, 23, 20, var7 ? 175 : 180, bH, 2);
         } else {
            if (var7) {
               b[42].a(graphics, bF, 237, 10, 1, 0, 0);
            }

            a.a(graphics, bA, 15, 10, 20);
            a.z = 5;
            a.a(graphics, dialogText, 15, 23, 20, var7 ? 175 : 180, bH, 2);
         }

         b[33].a(graphics, 6, stateFrame % 6 >> 1, bG == 0 ? 230 : 190, 55, 0, 0, 0);
      }

      X++;
      if (state != 14 && G(327712)) {
         if (bI > 2) {
            bI -= 2;
            bH += 2;
            return;
         }

         dialogText = null;
         s();
         N();
      }
   }

   public static final void h() {
      int var0 = 320 - bE - 2;
      e(0, var0, 240, bE);
      b[33].a(graphics, 23, 0, var0, 0, 0, 0);
      b[33].a(graphics, 23, 240, var0, 1, 0, 0);
      b[33].a(graphics, 24, 39, var0, 0, 0, 0);
      b[33].a(graphics, 24, 201, var0, 1, 0, 0);
      a.z = 5;
      a.a(graphics, dialogText, 120, bB, 3, 180, 0, -1);
   }

   static final void i() {
      int var0 = 0;
      if (Y <= 10) {
         b(var0 = 0xFF000000 & 1677721 * Y << 8);
         b(0, 0, 240, 320);
      } else if (Y <= 100) {
         b(0);
         b(0, 0, 240, 320);
         if (Y <= 30) {
            var0 = 838860 * (Y - 10) >> 16;
         } else if (Y > 80) {
            var0 = 256 - (838860 * (Y - 80) >> 16);
         }

         if (Y <= 30 || Y > 80) {
            a.t = true;
            a.K = var0;
         }

         a.z = 5;
         a.a(graphics, fr[12][15], 120, 120, 17, 175, 0, -1);
         a.t = false;
      } else {
         b(0);
         b(0, 0, 240, 320);
         aC();
         a(6);
         bs = 160;
         br = 0;
      }

      Y++;
   }

   static final void j() {
      int var0;
      b(var0 = 16777215 | 3276 * Y << 16);
      b(0, 0, 240, 320);
      Y++;
      if (Y == 20) {
         s();
         c(false);
         dc = false;
         dd = -1;
         de = 0;
      }
   }

   static final void a(int var0, int var1, int var2, int var3, int var4) {
      for (int var5 = 0; var5 < var0; var5++) {
         b(Z[var5 >> 1]);
         c(var1 - var5, var2 - var5, var3 + 2 * var5, var4 + 2 * var5, 0, 360);
      }
   }

   static final void k() {
      if (U >= 240) {
         player.A();
      } else {
         U += 30;
         b(60928 - 54 * U << 16 & 0xFF000000);
         int var2 = S - 0 - U;
         int var3 = T - 0 - U;
         int var4 = 0 + 2 * U;
         int var5 = 0 + 2 * U;
         b(var2, var3, var4, var5, 0, 360);
         player.v();
         int var6 = U / 3;

         for (int var9 = 0; var9 < 20; var9++) {
            int var8 = i(0, var6 >> 2);
            int var7 = var6 + var8;
            b(-9150);
            b(S - var7, T - var7, var7 * 2, var7 * 2, 360 * var9 / 20 + 9, 2);
         }
      }
   }

   public static final void f(int var0, int var1, int var2, int var3) {
      if (U >= 0) {
         if (U <= 240) {
            U += var3;
            int var16 = 240 - U;
            b(60928 - 54 * U << 16 & 0xFF000000);
            int var7 = var1 - var16;
            int var8 = var2 - var16;
            int var9 = 0 + 2 * var16;
            int var10 = 0 + 2 * var16;
            if (var0 >= 0 && var0 <= 2) {
               b(var7, var8, var9, var10, 0, 360);
            }

            int var11 = var16 >> 2;
            if (var0 != 4 && var0 != 1) {
               b(16768066);
            } else {
               b(5882111);
            }

            a(0, 0, 240, 320);

            for (int var14 = 0; var14 < 20; var14++) {
               int var12 = i(0, var11 >> 1);
               int var13 = var11 + var12;
               b(var1 - var13, var2 - var13, var13 * 2, var13 * 2, 360 * var14 / 20 + 9, 2);
            }

            a(0, 0, 240, 320);
         } else {
            if (var0 == 0) {
               if (player.animationId == 38) {
                  player.a(39, 17, 9, player.facingLeft ? -30 : 30, -20, 302, false);
               }

               s();
            } else if (var0 == 1) {
               if (player.animationId == 44) {
                  if (Actor.ba == null) {
                     Actor.ba = new Actor[4];
                  }

                  if (Actor.ba[0] != null) {
                     Actor.ba[3] = Actor.ba[0];
                     Actor.ba[0] = null;
                     if (Actor.ba[3].animationId == 18 || Actor.ba[3].animationId == 19 || Actor.ba[3].animationId == 20) {
                        Actor.ba[3].a(21);
                     }
                  }

                  Actor.ba[0] = player.a(39, 17, 17, !player.facingLeft ? 10 : -10, -48, 302, false);
                  Actor.ba[0].G = Actor.ba[0].facingLeft ? -2560 : 2560;
                  Actor.ba[0].an();
               }

               s();
            } else if (var0 == 2) {
               player.a(39, 17, 67, player.facingLeft ? -30 : 30, -30, 302, false);
               s();
            } else if (var0 == 3) {
               if (Actor.e(Actor.co[Actor.cl[Actor.ch]])) {
                  Actor.eu = 0;
                  Actor.ew = 2;
                  player.b(24);
                  Actor.aW = Actor.cn[Actor.cl[0]];
                  Actor.aV = new Actor[Actor.aW];

                  for (int var4 = 0; var4 < Actor.aW; var4++) {
                     Actor.aV[var4] = player.a(39, 17, 35, 0, -22, 302, false);
                     Actor.aV[var4].c |= 4096;
                     Actor.aV[var4].aB = var4;
                     Actor.aV[var4].aA = 600;
                     Actor.aV[var4].ay = 0;
                     Actor.aV[var4].aw = Actor.cl[Actor.ch];
                     Actor.aV[var4].ax = 0;
                     Actor.aV[var4].an();
                  }
               }

               if (bJ != 14) {
                  s();
               }
            } else if (var0 == 4) {
               Actor.eu = 1;
               Actor.ew = 2;
               if (Actor.e(Actor.co[Actor.cl[Actor.ch]])) {
                  player.b(32);
                  if (Actor.ba == null) {
                     Actor.ba = new Actor[4];
                  }

                  if (Actor.ba[0] != null) {
                     Actor.ba[3] = Actor.ba[0];
                     if (Actor.ba[3].animationId == 18 || Actor.ba[3].animationId == 19 || Actor.ba[3].animationId == 20) {
                        Actor.ba[3].a(21);
                     }
                  }

                  if (Actor.cl[Actor.ch] == 1) {
                     int var15 = !player.facingLeft ? 10 : -10;
                     Actor.ba[0] = player.a(39, 17, 16, var15, -50, 302, false);
                     Actor.ba[1] = player.a(39, 17, 16, -var15, -70, 302, false);
                     Actor.ba[2] = player.a(39, 17, 16, -var15, -30, 302, false);

                     for (int var5 = 0; var5 < 3; var5++) {
                        Actor.ba[var5].c |= 16;
                        Actor.ba[var5].G = Actor.ba[var5].facingLeft ? -2560 : 2560;
                        Actor.ba[var5].an();
                     }
                  } else {
                     Actor.ba[0] = player.a(39, 17, Actor.cl[Actor.ch] == 2 ? 17 : 16, player.facingLeft ? -10 : 10, -50, 302, false);
                     Actor.ba[0].c |= 16;
                     Actor.ba[0].G = Actor.ba[0].facingLeft ? -2560 : 2560;
                     Actor.ba[0].an();
                  }
               }

               s();
            }

            U = -1;
         }
      }
   }

   public static final void a(int var0, int var1, int var2) {
      int var3 = 100 * (450 - Actor.bb) / 450;
      int var4 = 0;
      if (var0 == 1) {
         var4 = 5882111;
      } else {
         var4 = 16768066;
      }

      graphics.setColor(var4);

      for (int var7 = 0; var7 < 20; var7++) {
         int var5 = i(0, var3 >> 1);
         int var6 = var3 + var5;
         b(var1 - var6, var2 - var6, var6 * 2, var6 * 2, 360 * var7 / 20 + 9, 2);
      }
   }

   private static void aa() {
      if (aB == -1 || be[levelIndex] != 2) {
         if (aB == -1 || levelIndex != 9) {
            cameraY = 0;
            cameraX = 0;
            ad = 0;
            ac = 0;
            af = 0;
            ae = 0;
            ao = false;
            ak = 0;
            d(1);
         }
      }
   }

   public static final void d(int var0) {
      boolean var1 = false;
      if (hasActiveBackgroundScript()) {
         var1 = true;
      }

      if (player != null && bJ != 18 && bJ != 17) {
         ar = false;
         as = false;
         at = false;
         av = false;
         au = false;
         if (aB == -1 && !var1 && aJ != null) {
            if (cameraX + 240 > aJ.collisionBounds[2]) {
               at = true;
               ar = true;
            }

            if (cameraX < aJ.collisionBounds[0]) {
               as = true;
               ar = true;
            }

            if (cameraY + 320 > aJ.collisionBounds[3]) {
               av = true;
               ar = true;
            }

            if (cameraY < aJ.collisionBounds[1]) {
               au = true;
               ar = true;
            }
         }

         if (ar) {
            if (!ax) {
               if (aN == 0) {
                  ad = player.positionY - 220;
               }
            } else {
               ad = player.positionY - 220;
               if (!au && !av) {
                  if (player.G >= 0 && (player.G != 0 || !player.facingLeft)) {
                     ac = regionLeft;
                  } else {
                     ac = regionRight - 240;
                  }
               } else if (au) {
                  ad = ai;
               } else {
                  ad = aj - 320;
               }

               aw = true;
            }

            aE = ad + 100;
            if (as || at) {
               cameraX = ac;
            }

            if (au || av) {
               cameraY = ad;
            }

            if (aN <= 0 && aO <= 0) {
               n();
            }
         } else {
            aw = false;
         }

         if (!ao && !var1) {
            boolean var2 = false;
            int var3 = 0;
            boolean var4 = false;
            boolean var5 = false;
            if (aw) {
               var3 = 1;
               var4 = true;
            }

            if (aB != -1) {
               if (az == 0 || az == 3 || az >= 4 && aD) {
                  var3 = 1;
               }

               var4 = true;
            }

            if (levelIndex == 9) {
               if (aB != -1 && az >= 4 && aD) {
                  var3 = 1;
                  var4 = false;
               }

               if (aB != -1 && az >= 4 && (!aD || aA)) {
                  var3 = 1;
                  var4 = true;
                  aA = false;
               }
            }

            if (var3 == 0) {
               ac = player.facingLeft ? player.positionX - 160 : player.positionX - 80;
               an = am;
               am = 0;
               if (!player.bL && Actor.aT == null) {
                  if (Math.abs(ac - cameraX) <= 0) {
                     if (an != 2) {
                        am = 1;
                     } else {
                        am = 2;
                     }
                  } else {
                     am = 2;
                  }
               }
            }

            if (!var4) {
               aF = -1;
               player.g();
               if (player.collisionBounds[1] <= aE) {
                  aF = 0;
               }

               if (player.positionY >= aE + 90) {
                  aF = 1;
               }

               if ((player.H < 0 || player.d(true) || Actor.aT != null || player.cI) && aF == 0
                  || (player.H > 0 || player.d(false) || Actor.aT != null || player.cI) && aF == 1) {
                  if (aF == 0) {
                     aE = player.collisionBounds[1];
                  } else {
                     aE = player.collisionBounds[3] - 90;
                  }

                  ad = aE - 100;
               }
            }

            ak = 14;
            if ((var0 & 1) == 0 || aB != -1 && be[levelIndex] == 2 || aB != -1 && levelIndex == 9) {
               if (Actor.W && al != 1 && aB == -1) {
                  if (Actor.X == 1) {
                     if (Math.abs((var3 = player.facingLeft ? player.positionX - 160 : player.positionX - 80) - cameraX) < 40) {
                        if (ac != var3) {
                           ac = var3;
                           Actor.Y = 0;
                        } else {
                           Actor.X++;
                           Actor.W = false;
                        }
                     } else if (Math.abs(cameraX - player.positionX + 120) > 2) {
                        ac = player.positionX - 120;
                        Actor.Y = 1;
                     } else {
                        Actor.X++;
                        Actor.W = false;
                     }
                  } else if (Actor.X == 3) {
                     var3 = player.facingLeft ? player.positionX - 160 : player.positionX - 80;
                     if (Math.abs(cameraX - var3) > 2) {
                        ac = var3;
                        Actor.Y = 2;
                     } else {
                        Actor.X++;
                        Actor.X = 0;
                        Actor.W = false;
                     }
                  } else {
                     Actor.X = 0;
                     Actor.W = false;
                  }
               }

               if (player.G != 0
                  || (var0 & 4) != 0
                  || Actor.aT != null
                  || player.cI
                  || aB != -1
                  || aq
                  || Actor.W
                  || player.aq == 1 && Actor.ar == 2 && Actor.cv == 70) {
                  if (Actor.W && aB == -1) {
                     ae = k(ae, ac - cameraX, aI[Actor.Y], aI[Actor.Y + 3]);
                  } else if (am == 2) {
                     ae = k(ae, ac - cameraX, 12, 2);
                  } else {
                     ae = k(ae, ac - cameraX, ak, 2);
                  }
               } else {
                  ae = 0;
               }

               if (am == 1 || am == 0 && an == 1) {
                  ae = 0;
               }

               af = k(af, ad - cameraY, 32, 2);
               if (aB != -1 && az == 1) {
                  af = k(af, ad - cameraY, 40, 15);
               }

               if (!aw && !Actor.en) {
                  cameraX = cameraX + ae;
               }

               cameraY = cameraY + af;
               if (aB != -1 && az >= 4) {
                  if (aD) {
                     cameraX = ac;
                  } else {
                     cameraY = ad;
                  }
               }
            } else {
               ac = player.facingLeft ? player.positionX - 160 : player.positionX - 80;
               if (ac < 0) {
                  ac = 0;
               }

               if (be[levelIndex] == 3 && levelIndex != 9) {
                  ad = 0;
               } else {
                  ad = player.positionY - 220;
               }

               if (ad + 320 > cB) {
                  ad = cB - 320;
               }

               if (ad < 0) {
                  ad = 0;
               }

               if (aH == 0) {
                  cameraY = ad;
                  cameraX = ac;
                  ae = 0;
                  af = 0;
               } else {
                  ae = k(ae, ac - cameraX, ak, 2);
                  af = k(af, ad - cameraY, 32, 2);
               }

               aE = ad + 100;
            }
         }

         if (Actor.aP && Actor.bR) {
            cameraX = player.positionX - 120;
            cameraY = player.positionY - 160;
         }

         if (var1) {
            ab();
         } else {
            if ((var0 & 1) == 0 || aH == 1 || ax) {
               n();
               aH = 0;
            }

            l();
         }
      }
   }

   public static final void l() {
      viewBounds[0] = cameraX;
      viewBounds[1] = cameraY;
      viewBounds[2] = cameraX + 240;
      viewBounds[3] = cameraY + 320;
   }

   public static final void m() {
      if (regionLeft > 0 && cameraX < regionLeft) {
         cameraX = regionLeft;
      } else if (regionRight > 0 && cameraX > regionRight - 240) {
         cameraX = regionRight - 240;
      }

      if (ai > 0 && cameraY < ai) {
         cameraY = ai;
      } else {
         if (aj > 0 && cameraY > aj - 320) {
            cameraY = aj - 320;
         }
      }
   }

   public static final void n() {
      m();
      ab();
      if (player.positionX - 80 <= regionLeft && player.G > 0 && aB == -1 && aN <= 0) {
         cameraX = regionLeft;
      } else {
         if (player.positionX + 80 > regionRight && player.G < 0 && aB == -1 && aN <= 0) {
            cameraX = regionRight - 240;
         }
      }
   }

   private static void ab() {
      if (aO > 0) {
         int var0;
         var0 = (var0 = aO / 2) + aO % 2;
         if ((aO & 1) != 0) {
            var0 = -var0;
         }

         cameraX += var0;
         aO--;
      }

      if (aN > 0) {
         int var2;
         if ((var2 = aN / 2) > 10) {
            var2 = 10;
         }

         var2 += aO % 2;
         if ((aN & 1) != 0) {
            var2 = -var2;
         }

         cameraY += var2;
         aN--;
      }

      if (cameraX < 0) {
         cameraX = 0;
      } else if (cameraX > levelPixelWidth - 240) {
         cameraX = levelPixelWidth - 240;
      }

      if (cameraY < 0) {
         cameraY = 0;
      } else {
         if (cameraY > cB - 320) {
            cameraY = cB - 320;
         }
      }
   }

   private static int k(int var0, int var1, int var2, int var3) {
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

   public static final void a(Actor var0) {
      boolean var1 = false;
      ax = false;
      if (aJ != var0) {
         bs = 160;
         br = 0;
         aK = aJ;
         aJ = var0;
         ax = true;
         dS = aJ.aA == 4;
         bo = var0.aB;
         aG = 1;
         var0.g();
      }
   }

   static final void o() {
      while (aM > 0) {
         int var0 = aL[aM--];
         fj[var0] = (byte)aL[aM--];
      }
   }

   static final void e(int var0) {
      if (aN <= 0) {
         if (var0 > 0) {
            aN = var0;
            return;
         }

         aN = -var0;
      }
   }

   static final void a(int var0, int var1) {
      var0 = var0 >> 1 << 1;
      if (aO <= 0) {
         if (var0 > 0) {
            aO = var0;
         } else {
            aO = -var0;
         }
      }

      if (aN <= 0) {
         if (var1 > 0) {
            aN = var1;
            return;
         }

         aN = -var1;
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
         a(0, 300, 240, 20);
         if (!eO) {
            b(0);
         }

         b(15692292);
      } else {
         a(0, 300, 240, 20);
         b(0);
         b(0, 300, 240, 20);
      }

      a.z = 0;
      if (var0 != null && var0 != "") {
         b[33].a(graphics, 72, 71, 300, 1, 0, 0);
         if (aS) {
            b[33].a(graphics, 8, stateFrame % 4, 1, 300, 0, 0, 0);
            if (stateFrame % 4 == 3) {
               aS = false;
               aQ = true;
               cT = true;
            } else {
               cT = false;
            }
         }

         a.a(graphics, var0, 18, 305, 20);
      }

      if (var1 != null && var1 != "") {
         b[33].a(graphics, 65, 240, 300, 1, 0, 0);
         if (aR) {
            b[33].a(graphics, 8, stateFrame % 4, 172, 300, 0, 0, 0);
            if (stateFrame % 4 == 3) {
               aR = false;
               aP = true;
               cT = true;
            } else {
               cT = false;
            }
         }

         a.a(graphics, var1, 222, 305, 24);
      }
   }

   private static void ac() {
      b(-2013265920);
      a(0, 0, 240, 320);
      b(0, 0, 240, 320);
      e(0, 100, 240, 100);
      b[33].a(graphics, 23, 0, 100, 0, 0, 0);
      b[33].a(graphics, 23, 242, 100, 1, 0, 0);
      b[33].a(graphics, 24, 0, 100, 0, 0, 0);
      b[33].a(graphics, 24, 242, 100, 1, 0, 0);
      a.z = 5;
      switch (aU) {
         case 0:
         case 1:
         case 2:
         case 3:
            String var0 = k(0, 87 + aU);
            int var1 = c(aW, aV);
            if (aU == 0) {
               var0 = " " + k(0, 87);
            }

            if (aU == 1) {
               var0 = " " + k(0, 113);
            }

            if (aU == 2) {
               var0 = " " + k(0, 89);
            }

            if (aU == 3) {
               var0 = " " + k(0, 112);
            }

            char var2 = (char)(var1 + 1 + 48);
            var0 = var0.replace('#', var2);
            a.a(graphics, var0, 120, 150, 3, 210, 0, -1);
            break;
         case 4:
            a.a(graphics, k(0, 86), 120, 150, 3, 210, 0, -1);
            break;
         case 5:
            a.a(graphics, k(0, 95), 120, 150, 3, 210, 0, -1);
            break;
         case 6:
            a.a(graphics, k(0, 14), 120, 150, 3, 210, 0, -1);
            break;
         case 7:
            a.a(graphics, k(0, 37), 120, 150, 3, 210, 0, -1);
      }

      if (aU == 4) {
         a(k(0, 18), "");
      } else {
         a(k(0, 16), k(0, 15));
      }
   }

   private static void ad() throws Exception {
      a(0, 0, 240, 320);
      b(16777215);
      b(0, 0, 240, 320);

      try {
         if (stateFrame == 1 && eP == null) {
            eP = Image.createImage("/logo.png");
         }
      } catch (Exception var1) {
         var1.printStackTrace();
      }

      a(eP, 120, 160, 3);
      e(stateFrame, 47);
      if (stateFrame == 2) {
         ay();
      } else if (stateFrame == 7) {
         aR();
      } else if (stateFrame == 12) {
         az();
      } else {
         if (stateFrame == 17) {
            aA();
            InGamePromotion.a(GloftSOFD.instance, GloftSOFD.game, 240, 320);
            String var0;
            if ((var0 = GloftSOFD.instance.getAppProperty("GL-Censored")) != null) {
               N = var0.trim().toLowerCase().compareTo("true") == 0;
               return;
            }

            if ((var0 = GloftSOFD.instance.getAppProperty("BLOOD")) != null) {
               N = var0.trim().toLowerCase().compareTo("0") == 0;
               return;
            }
         } else {
            if (stateFrame == 22) {
               accessSaveRecord(false);
               return;
            }

            if (stateFrame == 27) {
               V(0);
               return;
            }

            if (stateFrame == 32) {
               aH();
               return;
            }

            if (stateFrame == 37) {
               aI();
               return;
            }

            if (stateFrame == 42) {
               return;
            }

            if (stateFrame >= 47) {
               eP = null;
               a(23);
               el = false;
               return;
            }

            System.gc();
         }
      }
   }

   private static void b(int var0, boolean var1) {
      try {
         a("/1");
         if (var1) {
            eQ = U(1);
            eQ.a(var0, 0, -1, -1);
            eE = eQ.H[var0][0];
         } else {
            eQ = U(2);
            eQ.a(0, 0, -1, -1);
            eF = eQ.H[0][0];
         }

         eQ.H[0][0] = null;
         B();
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   private static void ae() {
      a(0, 0, 240, 320);
      b(0);
      b(0, 0, 240, 320);
      a.a(graphics, k(0, 22), 120, 160, 3);
      a(k(0, 16), k(0, 15));
      if (G(131072)) {
         if (cT) {
            aR = true;
            stateFrame = -1;
            return;
         }
      } else {
         if (aP) {
            aP = false;
            el = false;
            a(18);
            return;
         }

         if (G(262144)) {
            if (cT) {
               aS = true;
               stateFrame = -1;
               return;
            }
         } else if (aQ) {
            aQ = false;
            el = true;
            a(18);
            I(0);
         }
      }
   }

   private static void af() {
      ei = 0;
      eg = 11;
      levelIndex = 0;
      maxUnlockedLevel = 0;
      a(20);

      for (int var0 = 0; var0 < dt.length; var0++) {
         a(dt, var0, 0);
      }

      aD();
      a(dt, 36, (short)1);
      a(dt, 64, (short)maxHealth);
      a(dt, 66, (short)health);
      a(dt, 68, (short)z);
      a(dt, 70, (short)maxMagic);
      a(dt, 72, (short)magic);
      a(dt, 74, (short)C);
      accessSaveRecord(true);
      aE();
      Actor.bu = false;
      Actor.bv = false;
      Actor.bw = false;
   }

   private static void ag() {
      as();
      a(0, 0, 240, 320);
      g(20, 20, 200, 248);
      a.z = 2;
      if (state == 12) {
         b[37].a(graphics, 2, 0, 120, 140, 0, 0, 0);
      }

      a.z = 0;
      a.a(graphics, k(0, 27), 120, 192, 3, 180, 0, -1);
      a(k(0, 16), k(0, 15));
      if (G(131072)) {
         if (cT) {
            aR = true;
            stateFrame = -1;
            return;
         }
      } else {
         if (aP) {
            aP = false;
            aC();
            accessSaveRecord(true);
            a(false);
            a(19);
            return;
         }

         if (G(262144)) {
            if (cT) {
               aS = true;
               stateFrame = -1;
               return;
            }
         } else if (aQ) {
            aQ = false;
            I(-1);
            aF();
         }
      }
   }

   private static void ah() {
      a(0, 0, 240, 320);
      g(0, 0, 240, 320);
      a.z = 1;
      a.a(graphics, k(0, 7), 120, 5, 17);
      a.z = 0;
      if (cP == 0) {
         a.a(graphics, k(0, 107), 120, 50, 17, 180, 0, 10);
      } else if (cP == 1) {
         a.a(graphics, k(0, 107), 120, 50, 17, 180, 10, 20);
      } else {
         a.a(graphics, k(0, 31 + cP - 1), 120, 160, 3, 180, 0, -1);
      }

      a(k(0, 18), k(0, 17));
      a.a(graphics, cP + 1 + "/" + 4, 120, 305, 17, 180, 0, -1);
      if (G(262144)) {
         if (cT) {
            aS = true;
            stateFrame = -1;
         }
      } else if (aQ || G(8256)) {
         aQ = false;
         cP = ++cP % 4;
         I(-1);
      }

      if (G(131072)) {
         if (cT) {
            aR = true;
            stateFrame = -1;
         }
      } else if (aP) {
         aP = false;
         a(previousState);
         cP = 0;
      }

      if (G(4112)) {
         cP = (--cP + 4) % 4;
         I(-1);
      }
   }

   public static final void p() {
      a(0, 0, 240, 320);
      b(0);
      b(0, 0, 240, 320);
      a.a(graphics, k(0, 36), 120, 160, 3, 180, 0, -1);
      a("", k(0, 17));
      if (G(327712)) {
         I(-1);
         if (InGamePromotion.a()) {
            a(7);
            boolean var0 = false;
            InGamePromotion.a(k(0, 25) + "...", 0);
            return;
         }

         a(2);
      }
   }

   private static void ai() {
      InGamePromotion.a(graphics);
      byte var0 = 0;
      if (G(16388)) {
         var0 = 21;
         aT = false;
      } else if (G(33024)) {
         var0 = 32;
         aT = false;
      } else if (G(4112)) {
         var0 = 23;
         aT = false;
      } else if (G(8256)) {
         var0 = 24;
         aT = false;
      } else if (G(131072)) {
         var0 = 26;
         aT = false;
      } else if (G(327712)) {
         var0 = 25;
         aT = true;
      }

      if (InGamePromotion.a(var0) && !aT) {
         a(2);
      }
   }

   public static final int b(int var0, int var1) {
      if (var1 == 0) {
         int var4;
         if ((var4 = var0 % 5) <= 1) {
            return 0;
         } else {
            return var4 <= 2 ? 1 : 2;
         }
      } else if (var1 == 1) {
         int var3;
         if ((var3 = var0 % 14) <= 9) {
            return 0;
         } else if (var3 <= 10) {
            return 1;
         } else if (var3 <= 11) {
            return 2;
         } else {
            return var3 <= 12 ? 3 : 4;
         }
      } else {
         if (var1 == 2) {
            int var2;
            if ((var2 = var0 % 16) <= 3) {
               return 0;
            }

            if (var2 > 3 && var2 < 8) {
               return 1;
            }

            if (var2 >= 8 && var2 < 12) {
               return 2;
            }

            if (var2 >= 12 && var2 < 17) {
               return 3;
            }
         }

         return 0;
      }
   }

   public static final int c(int var0, int var1) {
      int var2 = 0;
      switch (var0) {
         case 0:
            var2 = Actor.cj[var1];
            break;
         case 1:
            if (var1 == 0) {
               var2 = Actor.ck[var1] - 1;
            } else {
               var2 = Actor.ck[var1];
            }
            break;
         case 2:
            var2 = Actor.cm[var1];
            break;
         case 3:
            var2 = Actor.cl[var1];
      }

      return var2;
   }

   public static final boolean d(int var0, int var1) {
      switch (var0) {
         case 0:
            if (Actor.cj[var1] < 3) {
               return true;
            }

            return false;
         case 1:
            if (var1 == 0) {
               if (Actor.ck[var1] < 4) {
                  return true;
               }

               return false;
            } else {
               if (Actor.ck[var1] < 3) {
                  return true;
               }

               return false;
            }
         case 2:
            if (Actor.cm[var1] < 3) {
               return true;
            }

            return false;
         case 3:
            if (Actor.cl[var1] < 2) {
               return true;
            }

            return false;
         default:
            return false;
      }
   }

   public static final void f(int var0) {
      switch (var0) {
         case 0:
            Actor.cj[aV]++;
            return;
         case 1:
            Actor.ck[aV]++;
            return;
         case 2:
            Actor.cm[aV]++;
            return;
         case 3:
            Actor.cl[aV]++;
      }
   }

   public static final void g(int var0) {
      if (!eR) {
         as();
      }

      b(-587202560);
      b(19, 20, 203, 250);
      if (Actor.bB) {
         b[33].a(graphics, 0, b(aX, 0), 120, 167, 0, 0, 0);
      } else {
         b[33].a(graphics, 28, 120, 167, 0, 0, 0);
      }

      if (var0 == 0) {
         b[33].a(graphics, 20, 92, 45, 0, 0, 0);
      } else {
         b[33].a(graphics, 21, 92, 45, 0, 0, 0);
      }

      b(15692292);
      d(20, 40, 20, 254);
      d(220, 40, 220, 254);
   }

   public static final void q() {
      g(aV);

      for (int var0 = 0; var0 < 4; var0++) {
         if (Actor.bD || var0 != 3) {
            boolean var1 = !d(var0, aV);
            int var2 = c(var0, aV);
            var2 = aV * 12 + var0 * 3 + var2 + (var1 ? -1 : 0);
            a(k(0, 80 + var0), c(var0, aV), aY[var0 + aV * 4], ba[var2], 45, 104 + 32 * var0, 150, bc, bd, var1);
            if (aW == var0) {
               b[33].a(graphics, 1, b(stateFrame, 1), 32, 108 + var0 * 32, 0, 0, 0);
               b[33].a(graphics, 1, b(stateFrame, 1), 208, 108 + var0 * 32, 1, 0, 0);
            }
         }
      }

      b[31].a(graphics, 9, 0, 190, 232, 0, 0, 0);
      a(D, 170, 222, false);
      a.z = 5;
      if (aX % 9 <= 5 && !bb) {
         a.a(graphics, k(0, 111), 120, 252, 3, 180, 0, -1);
      }

      aX++;
      if (bb) {
         ac();
      } else {
         a(k(0, 18), "");
      }
   }

   public static final void a(String var0, int var1, int var2, int var3, int var4, int var5, int var6, int[] var7, int[] var8, boolean var9) {
      a.z = 5;
      a.a(graphics, var0 + " " + k(0, 84) + (var1 + 1), var4 - 1, var5 - 14, 20);
      int var10 = a(var3, var4 + var6, var5 - 12, false);
      var10 += 6;
      b[33].a(graphics, 22, var4 + var6 - var10, var5 - 12, 0, 0, 0);
      a(var2, var4 + var6 - var10, var5 - 12, false);
      int var11 = var6 * var2 / var3;
      a(var4, var5, var6, var11, var7, var8);
   }

   private static boolean aj() {
      for (int var2 = 0; var2 < 4; var2++) {
         if (d(var2, Actor.ch)) {
            int var0 = c(var2, Actor.ch);
            var0 = Actor.ch * 12 + var2 * 3 + var0;
            int var1 = ba[var0];
            if (D >= var1 - aY[Actor.ch * 4 + var2]) {
               return true;
            }
         }
      }

      return false;
   }

   private static void ak() {
      if (eR) {
         graphics.setColor(0);
         b(0, 0, 240, 320);
      }

      q();
      if (!bb) {
         if (Actor.bB && G(12368)) {
            if (aV == 0) {
               aV = 1;
            } else {
               aV = 0;
            }
         }

         if (G(16388)) {
            aW--;
            if (aW < 0) {
               aW = 3;
               if (!Actor.bD) {
                  aW--;
               }
            }
         } else if (G(33024)) {
            aW++;
            if (aW > (Actor.bD ? 3 : 2)) {
               aW = 0;
            }
         }
      }

      if ((F(65568) || Actor.aP && H(65568)) && !bb) {
         if (!d(aW, aV)) {
            bb = true;
            aU = 5;
            N();
            return;
         }

         if (D > 0) {
            boolean var0 = false;
            int var1 = c(aW, aV) + aV * 12 + aW * 3;
            if (Actor.aP && H(65568) && aY[aV * 4 + aW] + D >= ba[var1]) {
               aZ = ba[var1] - aY[aV * 4 + aW];
               f(aW);
               bb = true;
               aU = aW;
               D = D - aZ;
               if (!d(aW, aV)) {
                  aY[aV * 4 + aW] = ba[var1];
                  return;
               }

               aY[aV * 4 + aW] = 0;
               return;
            }

            if (aX % 5 == 0) {
               aZ++;
            }

            if (D - aZ <= 0) {
               aZ = D;
               bb = true;
               aU = 4;
            }

            if (aY[aV * 4 + aW] + aZ >= ba[var1]) {
               aZ = ba[var1] - aY[aV * 4 + aW];
               f(aW);
               var0 = true;
               bb = true;
               aU = aW;
            }

            D = D - aZ;
            aY[aV * 4 + aW] = aY[aV * 4 + aW] + aZ;
            if (var0 && d(aW, aV)) {
               aY[aV * 4 + aW] = 0;
            }

            b[17].z = 2;
            j(0, 0, 240, 320);
            b[17].a(graphics, 26, aX % 7, 35, 108 + aW * 32, 0, 0, 0);
            b[17].a(graphics, 26, aX % 7, 205, 108 + aW * 32, 0, 0, 0);
         } else {
            aZ = 0;
            bb = true;
            aU = 4;
            N();
         }
      } else {
         aZ = 0;
      }

      if (G(327712) && bb) {
         bb = false;
         N();
      }

      if (G(131072)) {
         if (cT) {
            aR = true;
            stateFrame = -1;
            return;
         }
      } else if (aP) {
         aP = false;
         if (eR) {
            c(true);
            eR = false;
            return;
         }

         a(8);
         Actor.ch = aV;
         Actor.g(Actor.ch);
         bb = false;
         N();
      }
   }

   private static boolean al() {
      boolean var0 = false;

      for (int var1 = 0; var1 < 2; var1++) {
         for (int var2 = 0; var2 < 4; var2++) {
            if (d(var2, var1)) {
               aV = var1;
               return true;
            }
         }
      }

      return false;
   }

   private static void am() {
      as();
      f(1, 120, 52);
      if (eN) {
         aU = 6;
         ac();
      }

      N(1);
      if (!eN) {
         d(fD[1].length, false);
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

   private static void c(boolean var0) {
      Actor.bM = false;
      a(dt, 772, (int)(byte)(Actor.bM ? 1 : 0));
      if (maxUnlockedLevel < levelIndex + 1) {
         maxUnlockedLevel = levelIndex + 1;
      }

      Actor.ej[0] = Actor.ej[1] = Actor.ej[2] = 0;
      bh = true;
      Actor.q();
      accessSaveRecord(true);
      aC();
      if (var0) {
         a(true);
      }

      if (var0) {
         a(19);
      } else {
         levelIndex = 16;
         a(9);
      }
   }

   private static void an() {
      j(0, 0, 240, 320);
      ci = 0;
      bv = false;

      for (int var1 = 0; var1 < actorCount; var1++) {
         if (actors[var1] != null && (actors[var1].c & 1024) == 0) {
            Actor var0;
            if ((var0 = actors[var1]).h()) {
               if (var0.type == 41) {
                  bL[var0.aB] = 1;
                  if ((var0.c & 4096) != 0) {
                     player.c |= 4096;
                     cb.c |= 4096;
                     bv = true;
                  }
               }

               if (var0.type != 22 && var0.type != 49 && var0.type != 20 && (var0.c & 32768) == 0 && var0.type != 40 && var0.type != 23 && var0.type != 59) {
                  j(var0);
               }
            } else if (var0.type == 41) {
               bL[var0.aB] = 0;
            } else if (var0.at) {
               var0.c(f);
            }
         }
      }

      if (!bv && !bw) {
         player.c &= -4097;
         if (cb != null) {
            cb.c &= -4097;
         }
      }

      if (hasActiveBackgroundScript()) {
         for (int var4 = 0; var4 < cp; var4++) {
            Actor var2;
            if (co[var4] != null && ((var2 = co[var4]).c & 128) == 0) {
               j(var2);
            }
         }
      }

      if ((player.c & 128) == 0) {
         j(player);
      }

      if (3 == be[levelIndex]) {
         if (bZ != null && (bZ.c & 128) == 0) {
            j(bZ);
         }

         if (ca != null && (ca.c & 128) == 0) {
            j(ca);
         }
      }

      for (int var5 = 0; var5 < ci; var5++) {
         Actor var3;
         if ((var3 = ch[var5]) != null && (var3.c & 4096) == 0 && var3.type != 41) {
            var3.v();
         }
      }
   }

   private static void j(Actor var0) {
      int var1 = 0;

      while (var1 < ci && ch[var1].ap <= var0.ap) {
         var1++;
      }

      if (ci >= cd) {
         for (int var2 = 0; var2 < cd; var2++) {
            System.out.println("paint out of bound: " + ch[var2].type);
         }
      }

      for (int var3 = ci; var3 > var1; var3--) {
         ch[var3] = ch[var3 - 1];
      }

      ch[var1] = var0;
      ci++;
   }

   private static void k(Actor var0) {
      if (ck >= ce) {
         for (int var1 = 0; var1 < ce; var1++) {
            System.out.println("update out of bound: " + cj[var1].type);
         }
      }

      cj[ck++] = var0;
   }

   private static boolean l(Actor var0) {
      byte var1 = 2;
      if ((var0.c & 32) != 0) {
         return false;
      }

      if ((var0.c & 16) != 0) {
         return true;
      }

      if (var0 == player || var0 == player.q) {
         return true;
      }

      if (var0 == cb) {
         return true;
      }

      if (aJ != null && var0.collisionBounds != null && !Actor.c(aJ.collisionBounds, var0.collisionBounds)) {
         return var0.type != 2 || var0.ar() != 3 && var0.ar() != 13 ? var0.type == 39 && var0.animationId == 58 : true;
      }

      if (aJ == null && var0.type == 22) {
         return true;
      }

      if (var0.type != 81 || var0.aq != 101 && (var0.n == null || var0.n[var0.ax - 1] <= 0 || var0.n[var0.ax - 1] > 7)) {
         if (var0.type == 40 || var0.type == 49) {
            var1 = 7;
            if (var0.type == 49 && Actor.b(var0.collisionBounds, player.collisionBounds)) {
               return true;
            }
         } else if (var0.type == 33) {
            var1 = 4;
         } else if (var0.type == 41) {
            var1 = 3;
         } else if (var0.type == 42) {
            var1 = 14;
         } else if (var0.type == 78) {
            var1 = 14;
         } else if (var0.type == 72) {
            var1 = 6;
         } else if (var0.type == 74) {
            var1 = 3;
         } else if (var0.type == 45) {
            var1 = 4;
         } else if (var0.T != null || var0.U != null) {
            var1 = 6;
         } else if (var0.type == 14) {
            var1 = 4;
         } else if (var0.type == 10) {
            var1 = 4;
         } else if (var0.type == 89) {
            var1 = 3;
         } else if (var0.type == 50) {
            var1 = 3;
         } else if (var0.type == 95) {
            var1 = 3;
         } else if (var0.type == 79) {
            var1 = 3;
         }

         return var0.ak < var1;
      } else {
         return true;
      }
   }

   public static final void r() {
      for (int var0 = 0; var0 < actorCount; var0++) {
         Actor var1;
         if ((var1 = actors[var0]) != null && var1.type == 22 && aJ != var1) {
            var1.t();
            if (ax) {
               return;
            }
         }
      }
   }

   private static void ao() {
      player.t();
      if (player.aq != 0 || player.animationId != 12) {
         if (Actor.aT != null && Actor.aT.type == 33 && player.bN) {
            Actor.aT.H = 0;
         }

         if (ax) {
            for (int var1 = 0; var1 < actorCount; var1++) {
               Actor var0;
               if ((var0 = actors[var1]) != null) {
                  if (var0.type == 74) {
                     if (var0.s == null && var0.aG != -1) {
                        var0.s = i(var0.aG);
                     }

                     if (var0.s != null) {
                        if (var0.s == aJ) {
                           var0.c |= 16;
                           var0.c &= -33;
                           var0.p(0);
                        } else {
                           var0.c &= -17;
                           var0.c |= 32;
                        }
                     }
                  }

                  if (var0.ao()) {
                     if (aJ != null && Actor.b(var0.collisionBounds, aJ.collisionBounds)) {
                        var0.c &= -1025;
                        if (var0.type == 5) {
                           var0.F();
                        }
                     } else if (aK != null && Actor.b(var0.collisionBounds, aK.collisionBounds)) {
                        var0.b(false);
                     }

                     if (actors[var1] != null) {
                        actors[var1].b((Actor)null);
                     }
                  } else if (var0.L()) {
                     if ((actors[var1].type != 11 || actors[var1].ar() != 1) && var0.aq != 0) {
                        var0.b(false);
                     }
                  } else if (bY == var0) {
                     var0.t();
                  } else if (var0 != null
                     && var0.an == -1
                     && var0.type != 8
                     && var0.type != 84
                     && var0.type != 85
                     && var0.type != -103
                     && (var0.type != 39 || var0.animationId != 29)) {
                     if (var0.type == 73) {
                        c(var0);
                     }

                     e(var0);
                  }
               }
            }

            ax = false;
         }

         for (int var5 = 0; var5 < actorCount; var5++) {
            Actor var2;
            if ((var2 = actors[var5]) != null) {
               var2.at = false;
            }
         }

         ck = 0;

         for (int var6 = 0; var6 < actorCount; var6++) {
            Actor var3;
            if ((var3 = actors[var6]) != null && var3.type != 0 && l(var3)) {
               if (var3.type == 23) {
                  var3.t();
               } else {
                  k(var3);
               }
            }
         }

         for (int var7 = 0; var7 < ck; var7++) {
            Actor var4;
            if ((var4 = cj[var7]) != null && Actor.dU[0] != var4 && Actor.dU[1] != var4 && Actor.dU[2] != var4 && (!ax || var4.type != 22)) {
               var4.t();
               if (var4 != null && var4.r != null && !var4.r.at) {
                  var4.r.t();
               }
            }
         }

         if (3 == be[levelIndex] && bZ != null && ca != null) {
            bZ.t();
            ca.t();
         } else if (2 == be[levelIndex] && !modalActive) {
            Actor.dR = -512;
            Actor.dT = 0;

            for (int var8 = 0; var8 < 3; var8++) {
               if (Actor.dU[var8] != null) {
                  Actor.dU[var8].X();
               }
            }

            if (Actor.dR >= 0) {
               Actor.dR = 0;
            }

            if (Actor.dT == 0) {
               if (Actor.W()) {
                  if (Actor.dR > -768) {
                     if (Actor.dS == 0) {
                        Actor.dS++;
                     }

                     Actor.dR = Actor.dR + -128 * Actor.dS;
                  } else {
                     Actor.dR = -768;
                     Actor.dS = 0;
                  }
               } else if (Actor.dR != 0) {
                  Actor.dR = -512;
               }
            }

            for (int var9 = 0; var9 < 3; var9++) {
               if (Actor.dU[var9] != null) {
                  Actor.dU[var9].t();
               }
            }
         }

         if (Actor.aQ && Actor.aR != -1) {
            Actor.bI++;
            if (Actor.bI >= Actor.aS) {
               Actor.as();
            }
         }
      }
   }

   private static void ap() {
      if (G(2048) && dG) {
         if (!Actor.aP) {
            Actor.aP = true;
            Actor.bS = true;
            D = 50000;
         } else {
            Actor.aP = false;
            Actor.bS = false;
         }

         if (!Actor.aP) {
            Actor.bR = false;
         }
      }

      if (Actor.aP && G(1024)) {
         Actor.bR = !Actor.bR;
      }

      if (bJ == 5) {
         cZ = true;
      }

      if (hasActiveBackgroundScript()) {
         I();
         if (Actor.ep != null) {
            Actor.ep.t();
         }
      } else {
         if (df > 0) {
            J();
         }

         ao();
      }

      if (da) {
         while (hasActiveBackgroundScript()) {
            I();
         }

         da = false;
      }

      as();
      if (G(262144) && !modalActive && !hasActiveBackgroundScript()) {
         cP = 0;
         a(14);
      } else if (G(131072) && !modalActive && !hasActiveBackgroundScript()) {
         a(31);
      } else {
         if (Actor.aP && dialogText == null) {
            a.z = 0;
            a.a(graphics, "is GOD MODULE!", 237, 20, 9);
         }

         if (aG != -1) {
            d(aG);
            aG = -1;
         } else {
            d(2);
         }
      }
   }

   private static int M(int var0) {
      return var0 / 1;
   }

   private static void d(boolean var0) {
      for (int var3 = 0; var3 < 3; var3++) {
         if (bj[var3 * 4 + 2] < 0) {
            int var1 = M(var3);
            int var2 = 40 + var1 * 60 / 3;
            bj[var3 * 4 + 2] = i(0, 2);
            if (var0) {
               bj[var3 * 4 + 0] = i(-60, 300) << 8;
            } else {
               bj[var3 * 4 + 0] = 240 + i(60, 240) << 8;
            }

            bj[var3 * 4 + 1] = i(var2, var2 + 20);
            bj[var3 * 4 + 3] = -(16 << var1);
         }
      }
   }

   private static void aq() {
      for (int var0 = 0; var0 < 3; var0++) {
         bj[var0 * 4 + 2] = -1;
         d(true);
      }
   }

   private static void ar() {
      if (R()) {
         for (int var0 = 0; var0 < 3; var0++) {
            if (bk[var0 * 4 + 2] >= 0) {
               b[Actor.bs[24]].z = 0;
               b[Actor.bs[24]].a(graphics, bk[var0 * 4 + 2], (bk[var0 * 4 + 0] >> 8) - cameraX, bk[var0 * 4 + 1] - cameraY, 0, 0, 0);
               if (state != 14 && state != 31) {
                  bk[var0 * 4 + 0] = bk[var0 * 4 + 0] + bk[var0 * 4 + 3];
                  if ((bk[var0 * 4 + 0] >> 8) - cameraX < -60
                     || (bk[var0 * 4 + 0] >> 8) - cameraX > 480
                     || bk[var0 * 4 + 1] - cameraY < -80
                     || bk[var0 * 4 + 1] - cameraY > 320) {
                     bk[var0 * 4 + 2] = -1;
                  }
               }
            } else {
               e(false);
            }
         }
      }
   }

   private static void e(boolean var0) {
      for (int var1 = 0; var1 < 3; var1++) {
         if (bk[var1 * 4 + 2] < 0) {
            bk[var1 * 4 + 2] = i(0, 2);
            if (var0) {
               bk[var1 * 4 + 0] = i(-60, 300) + cameraX << 8;
            } else {
               bk[var1 * 4 + 0] = 240 + i(60, 240) + cameraX << 8;
            }

            bk[var1 * 4 + 1] = i(240, 290) + cameraY;
            bk[var1 * 4 + 3] = -i(64, 256);
         }
      }
   }

   private static void as() {
      if (Actor.en) {
         a(cameraX + Actor.eo >> 6, 20, true);
         e((cameraX + Actor.eo >> 3) % (cD * 20), 20, 2);
         e(cameraX + Actor.eo % 240, cameraY, 1);
      } else if (be[levelIndex] == 3) {
         if (bo != -2) {
            a(cameraX >> 6, 20, true);
         }

         if (bo != -1) {
            e((cameraX >> 3) % (cD * 20), 20, 2);
         }

         if (levelIndex == 9) {
            j(0, 0, 240, 320);
            u();
            e(cameraX, cameraY < 0 ? 0 : cameraY, 1);
         } else {
            e(cameraX, cameraY < 0 ? 0 : cameraY, 1);
         }
      } else {
         if (bo != -3 && bo != -2) {
            a(cameraX * (cC - 13) / (cy - 13), cameraY * (cE - 17) / (cz - 17), true);
         } else {
            b(0);
            b(0, 0, 240, 320);
         }

         if (bo != -1 && bo != -3) {
            e((cameraX >> 3) % (cD * 20), (cameraY + bo) * (cF - 16) / (cz - 16), 2);
         }

         u();
         e(cameraX, cameraY < 0 ? 0 : cameraY, 1);
      }

      if (dc && dd <= 1) {
         a(2 - dd, false, de);
      }

      an();
      if (dc && dd > 1) {
         Actor.E.G();
      }

      for (int var0 = 0; var0 < bN; var0++) {
         if (bL[var0] == 1) {
            b(var0, cameraX, cameraY);
         }
      }

      a(0, 0, 240, 320);
      if (modalActive && (bJ == 9 || bJ == 12)) {
         h(Actor.dN, bJ, (Actor.dJ + Actor.dK >> 1) - cameraX, Actor.dH + 120 - cameraY);
      }

      if (!hasActiveBackgroundScript() && !Actor.bm && !Actor.br && (Actor.E == null || Actor.E.aq != 3)) {
         if (player.animationId == 25) {
            f(3, player.positionX - cameraX, player.positionY - cameraY - 30, 80);
         } else if (player.animationId == 33) {
            f(4, player.positionX - cameraX + (player.facingLeft ? -40 : 40), player.positionY - cameraY - 50, 80);
         }
      }

      if (modalActive) {
         if (bJ >= 6 && bJ <= 8) {
            f(bJ - 6, player.positionX - cameraX + (player.facingLeft ? -20 : 20), player.positionY - cameraY - 55, 40);
         } else if (bJ == 13) {
            Actor.E.G();
         } else if (bJ == 3) {
            a(by, true, 0);
         } else if (bJ == 1) {
            Actor.B();
         } else if (bJ == 2) {
            k();
         } else if (bJ == 10) {
            a(2, true, 0);
         } else if (bJ == 11) {
            a(1, true, 0);
         }
      }

      if (Actor.bb > 0) {
         a(Actor.ch, player.positionX - cameraX, player.positionY - cameraY + 0 + -80);
      }

      for (int var2 = 0; var2 < ci; var2++) {
         Actor var1;
         if ((var1 = ch[var2]) == null) {
            System.out.println("null actor in s_paintActors");
         }

         if ((var1.c & 4096) != 0 && var1.type != 41) {
            var1.v();
         }

         if (var1.q != null) {
            var1.q.h();
         }
      }

      ar();
      P();
      O();
      if (!hasActiveBackgroundScript() && bJ != 14 && state != 14 && state != 12 && state != 31 && state != 15 && !modalActive && state != 32) {
         graphics.setColor(0);
         b(0, 293, 320, 28);
         b(15692292);
         c(0, 294, 239, 26);
         b[16].a(graphics, 98, 45, 294, 0, 0, 0);
         if (Actor.bD) {
            b[16].a(graphics, 82, 100, 294, 0, 0, 0);
         } else {
            b[16].a(graphics, 117, 100, 294, 0, 0, 0);
         }

         if (F(65568) && bn) {
            if (!bl) {
               bl = true;
               stateFrame = 0;
            }

            bm = 0;
         }

         if (G(1) && Actor.bD && bn) {
            if (!bl) {
               bl = true;
               stateFrame = 0;
            }

            bm = 1;
         }

         if (G(512) && Actor.bB && bn && player.aq != 1) {
            if (!bl) {
               bl = true;
               stateFrame = 0;
            }

            bm = 2;
         } else if (Actor.ch == 1 && bn && Actor.bB) {
            b[16].a(graphics, 120, 158, 294, 0, 0, 0);
         } else if (Actor.ch == 0 && bn && Actor.bB) {
            b[16].a(graphics, 100, 158, 294, 0, 0, 0);
         } else if (Actor.ch == 1 && bn) {
            b[16].a(graphics, 120, 158, 294, 0, 0, 0);
         } else if (Actor.ch == 0 && bn) {
            b[16].a(graphics, 119, 158, 294, 0, 0, 0);
         }

         int var3 = 0;
         if (bl) {
            switch (bm) {
               case 0:
                  var3 = stateFrame % 3;
                  b[16].a(graphics, 35, var3, 45, 294, 0, 0, 0);
                  if (var3 == 2) {
                     bl = false;
                  }
                  break;
               case 1:
                  var3 = stateFrame % 3;
                  b[16].a(graphics, 36, var3, 100, 294, 0, 0, 0);
                  if (var3 == 2) {
                     bl = false;
                  }
                  break;
               case 2:
                  if (Actor.ch == 1) {
                     var3 = stateFrame % 7;
                     b[16].a(graphics, 37, var3, 158, 294, 0, 0, 0);
                  } else if (Actor.ch == 0) {
                     var3 = stateFrame % 7;
                     b[16].a(graphics, 38, var3, 158, 294, 0, 0, 0);
                  }

                  if (var3 == 6) {
                     bl = false;
                     bn = true;
                  } else {
                     bn = false;
                  }
            }
         }
      }

      if (modalActive && bJ == 14) {
         j(0, 0, 240, 320);
         f();
      }

      av();
      if (bJ == 5 && modalActive) {
         g();
      }

      if (modalActive) {
         if (bJ == 17) {
            j();
         } else if (bJ == 18) {
            i();
         }
      }

      if (hasActiveBackgroundScript()) {
         a(bJ != 17 && db ? k(0, 20) : "", bJ == 5 ? k(0, 17) : "", true);
      }

      if (player != null && player.aq == 1 && state != 15 && !modalActive && state == 8) {
         player.C();
      }

      if (!hasActiveBackgroundScript() && dialogText == null && bs <= 0 && !modalActive && state == 8 || Actor.bE == 2) {
         if (Actor.bE != 2 && di) {
            X();
            Y();
         }

         c();
      }

      if (Actor.E != null && (Actor.E.c & 128) == 0 && (Actor.E.c & 32) == 0 && !hasActiveBackgroundScript()) {
         f(Actor.E.as, Actor.F);
      }
   }

   static final void g(int var0, int var1, int var2, int var3) {
      e(var0, var1, var2, var3 - 1);
      b[33].a(graphics, 23, var0, var1, 0, 0, 0);
      b[33].a(graphics, 23, var0 + var2, var1, 1, 0, 0);
      b[33].a(graphics, 24, 39 + var0, var1, 0, 0, 0);
      b[33].a(graphics, 24, var0 + var2 - 39, var1, 1, 0, 0);
   }

   static final void e(int var0, int var1) {
      a(60, 280, 120, var0 * 120 / var1, bR, bp);
      if (a != null) {
         a.a(graphics, "" + var0 * 100 / var1 + "%", 120, 295, 17);
      }
   }

   private static void b(boolean var0, int var1) {
      a(0, 0, 240, 320);
      g(0, 0, 240, 320);
      Actor.a(120, 160, 120, 160, 60, 50, stateFrame * 5, true);
      a.z = 5;
      if (levelIndex != 16) {
         String var2 = k(0, 71 + eS);
         if (eS == 0) {
            var2 = k(0, 114);
         }

         if (eS == 6) {
            var2 = k(0, 110);
         }

         a.a(graphics, var2, 120, 20, 17, 180, 0, -1);
      }

      if (stateFrame <= var1) {
         e(stateFrame, var1);
         bq = false;
      } else {
         if (stateFrame % 10 < 5 && var0) {
            a.a(graphics, k(0, 106), 120, 280, 17);
         }
      }
   }

   private static void at() {
      b(true, 79);
      if (!bq && Q(stateFrame)) {
         bq = true;
      }

      if (bq && M()) {
         aa();
         bs = 160;
         br = 0;
         a(8);
         bt = 2;
      }

      System.gc();
   }

   private static void N(int var0) {
      if (eN) {
         if (G(131072)) {
            if (cT) {
               aR = true;
               stateFrame = -1;
               return;
            }
         } else {
            if (aP) {
               aP = false;
               eN = false;
               aU = 0;
               N();
               return;
            }

            if (G(262144)) {
               if (cT) {
                  aS = true;
                  stateFrame = -1;
                  return;
               }
            } else if (aQ) {
               aQ = false;
               if (fD[var0][cP] != 13) {
                  I(-1);
               }

               eN = false;
               if (fD[var0][cP] == 9) {
                  state = 11;
               } else if (fD[var0][cP] == 13) {
                  aF();
               } else if (fD[var0][cP] == 0) {
                  aC();
                  eE = null;
                  eQ = null;
                  a(2);
               } else if (fD[var0][cP] == 1) {
                  af();
               }

               N();
               return;
            }
         }
      } else if (G(131072)) {
         if (cT) {
            aR = true;
            stateFrame = -1;
            return;
         }
      } else {
         if (aP) {
            aP = false;
            if (var0 == 1) {
               a(8);
            }

            N();
            return;
         }

         if (G(327712)) {
            if (cT) {
               cR = true;
               stateFrame = -1;
               return;
            }
         } else if (cS) {
            cS = false;
            switch (fD[var0][cP]) {
               case 0:
                  eN = true;
                  break;
               case 1:
                  if (b(dt, 16) > 0) {
                     eN = true;
                     aU = 7;
                  } else {
                     a(20);
                  }
                  break;
               case 2:
                  eE = null;
                  levelIndex = a(dt, 14);
                  maxUnlockedLevel = a(dt, 15);
                  bh = b(dt, 24) == 1;
                  ff = true;
                  fg = true;
                  if (bh && levelIndex < 10) {
                     levelIndex++;
                     ff = false;
                     fg = false;
                  }

                  if (b(dt, 16) > 0) {
                     A();
                  } else {
                     aD();
                  }

                  a(9);
                  break;
               case 3:
                  levelIndex = a(dt, 14);

                  for (int var3 = 0; var3 < 3; var3++) {
                     Actor.ej[var3] = 0;
                  }

                  if (b(dt, 16) > 0) {
                     A();
                     cx = true;
                  } else {
                     aD();
                     cx = true;
                  }

                  eg = 11;
                  a(bh);
                  if (fF) {
                     maxUnlockedLevel = 11;
                  } else {
                     maxUnlockedLevel = a(dt, 15);
                  }

                  a(19);
                  break;
               case 4:
                  if (InGamePromotion.a()) {
                     E(16);
                     a(dt, 10, (int)(byte)dv);
                     accessSaveRecord(true);
                     a(7);
                     boolean var2 = false;
                     InGamePromotion.a(k(0, 25) + "...", 0);
                  }
                  break;
               case 5:
                  if (InGamePromotion.d()) {
                     E(32);
                     a(dt, 10, (int)(byte)dv);
                     accessSaveRecord(true);
                     a(7);
                     boolean var1 = false;
                     InGamePromotion.b(k(0, 25) + "...", 0);
                  }
                  break;
               case 6:
                  el = !el;
                  stateFrame = 0;
                  if (!el) {
                     T();
                  } else if (el) {
                     if (var0 == 1) {
                        I(9);
                     } else {
                        playSound(0, -1);
                     }
                  }
                  break;
               case 7:
                  cP = 0;
                  I(-1);
                  a(5);
                  break;
               case 8:
                  a(6);
                  break;
               case 9:
                  eN = true;
                  aU = 6;
               case 10:
               case 11:
               default:
                  break;
               case 12:
                  a(8);
                  break;
               case 13:
                  eN = true;
            }

            if (fD[var0][cP] != 12 && fD[var0][cP] != 6) {
               I(-1);
            }

            N();
         }
      }
   }

   private static void au() throws Exception {
      b = new BSprite[52];
      a(1);
   }

   private static void av() {
      if ((bt < 0 || modalActive) && br != bs) {
         int var0;
         if ((var0 = br - bs) > -20 && var0 < 20) {
            bs = br;
         } else {
            bs += var0 > 0 ? 20 : -20;
         }
      }

      if (bs > 0) {
         bt--;
         j(0, 0, 240, 320);
         b(-587202560);
         b(0, 0, 240, bs);
         b(0, 320 - bs, 240, bs);
      } else {
         bt = 2;
      }
   }

   public static final void h(int var0) {
      di = false;
      if (!modalActive) {
         modalActive = true;
         bJ = var0;
         if (!dc) {
            P = 0;
         }

         if (bK != null) {
            bK.c |= 4096;
         }

         if (var0 == 13 || var0 == 16) {
            for (int var1 = 0; var1 < 10; var1++) {
               Actor.dc[var1] = i(var1 * 36, var1 * 36 + 18);
               Actor.dd[var1] = i(5, 15);
            }

            player.c |= 4096;
            cb.c |= 4096;
            bw = true;
         }
      }
   }

   public static final void s() {
      modalActive = false;
      if (bJ != 5) {
         di = true;
      }

      if (bJ == 10 || bJ == 11) {
         state = 32;
         if (bJ == 10) {
            z++;
            eI = 0;
         } else {
            C++;
            eI = 1;
         }

         eH = 0;
         eJ = false;
         di = false;
      }

      bJ = -1;
      if (bK != null) {
         e(bK);
         bK = null;
      }

      if (bJ != 5) {
         player.c &= -4097;
         bw = false;
      }

      dialogText = null;
      cZ = false;
   }

   public static final void a(int var0, boolean var1) {
      bM[var0 * 5 + 4] = var1 ? 1 : 0;
   }

   public static final void t() {
      for (int var0 = 0; var0 < 80; var0++) {
         bM[var0 * 5 + 4] = 0;
      }
   }

   public static final void b(int var0, int var1, int var2) {
      b(-33554432);
      int var3 = bM[var0 * 5 + 0];
      int var4 = bM[var0 * 5 + 1];
      int var5 = bM[var0 * 5 + 2];
      int var6 = bM[var0 * 5 + 3];
      int var7 = bM[var0 * 5 + 4];
      if (var3 + var5 >= var1 && var4 + var6 >= var2 && var3 <= var1 + 240 && var4 <= var2 + 320) {
         int var8;
         if ((var8 = var3 - var1) < 0 && var7 == 0) {
            var8 = 0;
            var5 = var5 + var3 - var1;
         }

         int var9;
         if ((var9 = var4 - var2) < 0 && var7 == 0) {
            var9 = 0;
            var6 = var6 + var4 - var2;
         }

         if (var7 == 0) {
            b(var8, var9, var5, var6);
         } else if (var7 == 1) {
            b[29].a(graphics, 0, var8 + 70, var9 + 70, 0, 0, 0);
         } else if (var7 == 2) {
            b[36].a(graphics, 0, var8, var9, 0, 0, 0);
         } else {
            b[36].a(graphics, 0, var8 + 40, var9, 1, 0, 0);
         }
      }
   }

   public static final void u() {
      for (int var0 = 0; var0 < bP; var0++) {
         if (bQ[var0] != null && bQ[var0].h()) {
            bQ[var0].v();
            if (bQ[var0].q != null && bQ[var0].q.h() && (bQ[var0].q.c & 128) == 0) {
               bQ[var0].q.v();
            }
         }
      }
   }

   public static final void b(Actor var0) {
      var0.c |= 32768;
      int var1 = 0;

      while (var1 < bP && bQ[var1] != null && bQ[var1].ap < var0.ap) {
         var1++;
      }

      for (int var2 = bP; var2 > var1; var2--) {
         bQ[var2] = bQ[var2 - 1];
      }

      bQ[var1] = var0;
      bP++;
   }

   public static final void c(Actor var0) {
      var0.c &= -32769;

      for (int var1 = 0; var1 < bP; var1++) {
         if (bQ[var1] == var0) {
            bQ[var1] = null;
            bP--;
            return;
         }
      }
   }

   public static final void v() {
      for (int var0 = 0; var0 < bP; var0++) {
         bQ[var0] = null;
      }

      bP = 0;
   }

   public static final void h(int var0, int var1, int var2, int var3) {
      P++;
      int var4 = var1 == 9 ? P : 25 - P;
      if (var1 == 9 && var0 == 1 || var1 == 12 && var0 == 1) {
         var2 += Actor.dO;
         var3 += Actor.dP;
      }

      boolean var5;
      if ((var1 != 9 || var0 != 0) && (var1 != 12 || var0 != 1)) {
         var5 = false;
      } else {
         var5 = true;
      }

      if (P != 1) {
         if (P < 25) {
            int var6 = var4 * 700 / 25;
            b(var4 * 221 / 25 << 24 | (var5 ? 255 : 16711680));
            b(var2 - (var6 >> 1), var3 - (var6 >> 1), var6, var6, 0, 360);
         } else {
            s();
         }
      }
   }

   public static final void a(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = 6 * m(var4);
      int var8 = 6 * l(var4);
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
            if ((var19 = stateFrame % 14) <= 9) {
               b[24].a(graphics, 14, 0, var14, var15 + 3, 0, 0, 0);
            } else {
               b[24].a(graphics, 14, var19 - 9, var14, var15 + 3, 0, 0, 0);
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

   public static final void f(int var0, int var1) {
      if (bs <= 0) {
         int var2 = 11141120 / var1;
         int var3 = var0 * var2 >> 16;
         if (var0 == var1) {
            var3 = 170;
         }

         int[] var4 = bU;
         if (var3 >= 113) {
            var4 = bS;
         } else if (var3 >= 56) {
            var4 = bT;
         }

         a(35, 255, 170, var3, bR, var4);
      }
   }

   public static final void c(int var0, int var1, int var2) {
      int var3 = var0 + var1;
      if (var0 <= 320 && var3 >= 0 && var2 > 0 && var2 <= 240) {
         if (var0 < 0) {
            var0 = 0;
         }

         if (var3 > 320) {
            var3 = 320;
         }

         for (int var4 = 0; var4 < 3; var4++) {
            b(eU[i(0, 3)]);
            int var6 = i(0, 2);
            int var7 = i(5, 7);
            int var8 = i(2, 4);
            int var9 = var0;
            int var10 = var2 - i(0, 3);
            int var11 = 0;
            int var12 = var0;
            if (var6 == 0) {
               while (var9 < var3 - 5 || var12 < var3 - 5) {
                  var12 = var9 + var7 * eT[i(0, 10)] / 1000;
                  var11 = var2 - var8 + i(-1, 2);
                  d(var10, var9, var11, var12);
                  if (i(0, 4) == 0) {
                     d(var11, var12, var11, var12 + 1);
                     var12++;
                  }

                  var9 = var12 + var7 * eT[i(0, 10)] / 1000;
                  var10 = var2 + var8 + i(-1, 2);
                  d(var11, var12, var10, var9);
                  if (i(0, 4) == 0) {
                     d(var10, var9, var10, var9 + 1);
                     var9++;
                  }
               }
            } else {
               while (var9 < var3 - 5 || var12 < var3 - 5) {
                  var12 = var9 + var7 * eT[i(0, 10)] / 1000;
                  var11 = var2 + var8 + i(-1, 2);
                  d(var10, var9, var11, var12);
                  if (i(0, 4) == 0) {
                     d(var11, var12, var11, var12 + 1);
                     var12++;
                  }

                  var9 = var12 + var7 * eT[i(0, 10)] / 1000;
                  var10 = var2 - var8 + i(-1, 2);
                  d(var11, var12, var10, var9);
                  if (i(0, 4) == 0) {
                     d(var10, var9, var10, var9 + 1);
                     var9++;
                  }
               }
            }
         }
      }
   }

   private static void q(int var0, int var1) throws Exception {
      if (var0 == 0) {
         a("/21");
         if (!T(var1)) {
            eA = U(bV[var1 * 4 + 0]);
         }
      } else if (var0 == 1) {
         if (!T(var1)) {
            eB = U(bV[var1 * 4 + 1]);
         }
      } else if (var0 == 2) {
         eC = U(bV[var1 * 4 + 2]);
      } else if (var0 == 3) {
         eD = U(bV[var1 * 4 + 3]);
      } else if (var0 == 4) {
         eY = true;
         B();
      } else if (var0 == 5) {
         if (!T(var1)) {
            byte var2 = bW[var1 * 4 + 0];
            if (state == 20 && var1 == 12) {
               var2 = 1;
            }

            eA.z = var2;
            eA.a(var2, 0, -1, -1);
            eA.a();
            eV = eA.H[var2][0];
            eA = null;
         }
      } else if (var0 == 6) {
         if (!T(var1)) {
            byte var3 = bW[var1 * 4 + 1];
            if (state == 20 && var1 == 12) {
               var3 = 1;
            }

            eB.z = var3;
            eB.a(var3, 0, -1, -1);
            eB.a();
         }
      } else if (var0 == 7) {
         byte var4 = bW[var1 * 4 + 2];
         if (state == 20 && var1 == 12) {
            var4 = 1;
         }

         eC.z = var4;
         eC.a(var4, 0, -1, -1);
         eC.a();
      } else if (var0 == 8) {
         byte var5 = bW[var1 * 4 + 3];
         if (state == 20 && var1 == 12) {
            var5 = 1;
         }

         eD.z = var5;
         eD.a(var5, 0, -1, -1);
         eD.a();
      }

      System.gc();
   }

   private static void aw() {
      eV = null;
      System.gc();
   }

   private static void ax() {
      if (eW == null) {
         eW = Image.createImage(260, 340);
      }

      if (eX == null) {
         eX = eW.getGraphics();
      }

      eY = true;
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
      } else if (var3 > cC - 1 && !var2) {
         var3 = cC - 1;
      }

      if (var4 < 0) {
         var4 = 0;
      } else if (var4 > cE - 1) {
         var4 = cE - 1;
      }

      if (!eY && (var3 != eZ || var5 != fb)) {
         if (var5 < eZ || var3 > fb) {
            eY = true;
         } else if (var3 < eZ) {
            var7 = eZ;
            l(var3, var4, eZ - 1, var6);
         } else if (var5 > fb) {
            var8 = fb;
            l(fb + 1, var4, var5, var6);
         }
      }

      if (!eY && (var4 != fa || var6 != fc)) {
         if (var6 < fa || var4 > fc) {
            eY = true;
         } else if (var4 < fa) {
            l(var7, var4, var8, fa - 1);
         } else if (var6 > fc) {
            l(var7, fc + 1, var8, var6);
         }
      }

      if (eY) {
         eY = false;
         l(var3, var4, var5, var6);
      }

      eZ = var3;
      fa = var4;
      fb = var5;
      fc = var6;
      r(var0, var1);
   }

   private static void l(int var0, int var1, int var2, int var3) {
      for (int var6 = var1; var6 <= var3; var6++) {
         int var5 = var6 % 17;
         int var7 = var6;

         for (int var8 = var0; var8 <= var2; var8++) {
            int var4 = var8 % 13;
            a(eX, var4 * 20, var5 * 20, var8 % cC + var7 * cC);
         }
      }
   }

   private static void e(int var0, int var1, int var2) {
      if (var1 < 0) {
         var1 -= 20;
      }

      int var5 = var0 / 20;
      int var6 = var1 / 20;
      int var7 = (var0 + 240 - 1) / 20;
      int var8 = (var1 + 320 - 1) / 20;
      j(0, 0, 240, 320);
      if (var1 < 0) {
         var1 += 20;
      }

      int var3 = var5 * 20 - var0;

      for (int var9 = var5; var9 <= var7; var9++) {
         int var4 = var6 * 20 - var1 + 0;
         int var10 = var9;
         if (var2 == 2 && var9 >= cD) {
            var10 = var9 % cD;
         } else if (var2 == 1 && var9 >= cy) {
            var10 = var9 % cy;
         }

         for (int var11 = var6; var11 <= var8; var11++) {
            if (var2 != 0) {
               if (var2 == 1) {
                  a(graphics, fm, fn, eC, var3, var4, var10 + var11 * cy);
                  a(graphics, fo, fp, eD, var3, var4, var10 + var11 * cy);
               } else {
                  a(graphics, fh, fi, eB, var3, var4, var10 + var11 * cD);
               }
            }

            var4 += 20;
         }

         var3 += 20;
      }
   }

   private static void r(int var0, int var1) {
      if (var1 < 0) {
         var1 = var1 % 340 + 340;
      }

      int var2 = var0 % 260;
      int var4 = var1 % 340;
      int var3 = (var0 + 240) % 260;
      int var5 = (var1 + 320) % 340;
      if (var3 > var2) {
         if (var5 > var4) {
            d(var2, var4, 240, 320, 0, 0);
         } else {
            d(var2, var4, 240, 320 - var5, 0, 0);
            d(var2, 0, 240, var5, 0, 320 - var5);
         }
      } else if (var5 > var4) {
         d(var2, var4, 240 - var3, 320, 0, 0);
         d(0, var4, var3, 320, 240 - var3, 0);
      } else {
         d(var2, var4, 240 - var3, 320 - var5, 0, 0);
         d(var2, 0, 240 - var3, var5, 0, 320 - var5);
         d(0, var4, var3, 320 - var5, 240 - var3, 0);
         d(0, 0, var3, var5, 240 - var3, 320 - var5);
      }
   }

   private static void d(int var0, int var1, int var2, int var3, int var4, int var5) {
      j(var4, var5, var2, var3);
      a(eW, var4 - var0, var5 - var1, 0);
   }

   private static void a(Graphics var0, int var1, int var2, int var3) {
      if ((fk[var3] & 255) != 255) {
         int var6 = 0;
         int var4 = fk[var3] & 255;
         int var5;
         if ((var5 = (fl[var3 >> 2] & 255) >> (3 - (var3 & 3) << 1) & 3) != 0) {
            if ((var5 & 1) != 0) {
               var6 = 2;
            }

            if ((var5 & 2) != 0) {
               var6 |= 1;
            }
         }

         int var7 = var4 * 20 % eV.getWidth();
         int var8 = var4 * 20 / eV.getWidth() * 20;
         var0.drawRegion(eV, var7, var8, 20, 20, var6, var1, var2, 20);
      }
   }

   private static void a(Graphics var0, byte[] var1, byte[] var2, BSprite var3, int var4, int var5, int var6) {
      if (var6 < 0 || var6 >= var1.length) {
         System.out.println("invalid bottom map index : " + var6);
         state = 11;
      }

      if ((var1[var6] & 255) != 255) {
         int var9 = 0;
         int var7 = var1[var6] & 255;
         int var8;
         if ((var8 = (var2[var6 >> 2] & 255) >> (3 - (var6 & 3) << 1) & 3) != 0) {
            if ((var8 & 1) != 0) {
               var9 = 2;
            }

            if ((var8 & 2) != 0) {
               var9 |= 1;
            }
         }

         var0.drawRegion(var3.H[var3.z][var7], 0, 0, 20, 20, var9, var4, var5, 20);
      }
   }

   public static final void d(Actor var0) {
      var0.af = -98;
      if (cm > 0) {
         actors[cl[--cm]] = var0;
      } else if (actorCount < cc) {
         actors[actorCount++] = var0;
      } else {
         System.out.println("s_actors_num exceed maxnum" + cc);

         for (int var1 = 0; var1 < actorCount; var1++) {
            System.out.println(var1 + " actor == " + actors[var1].type + "   anim: " + actors[var1].animationId);
         }
      }
   }

   public static final void e(Actor var0) {
      if (var0 != null) {
         var0.b((Actor)null);
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
            if (Actor.aT == var0) {
               Actor.aT = null;
            }

            for (int var1 = 0; var1 < actorCount; var1++) {
               if (actors[var1] == var0) {
                  if (var0.af != -98) {
                     cq[var0.af] = -99;
                  }

                  if ((actors[var1].c & 512) != 0 && !Actor.do_) {
                     cu[cv++] = (short)actors[var1].an;
                  }

                  actors[var1].a();
                  actors[var1] = null;
                  cl[cm++] = var1;
                  break;
               }
            }

            for (int var2 = 0; var2 < ck; var2++) {
               if (cj[var2] == var0) {
                  cj[var2] = null;
                  break;
               }
            }

            if (Actor.ba != null) {
               for (int var3 = 0; var3 < Actor.ba.length; var3++) {
                  if (Actor.ba[var3] == var0) {
                     Actor.ba[var3] = null;
                     break;
                  }
               }
            }

            if (Actor.aV != null) {
               for (int var4 = 0; var4 < Actor.aV.length; var4++) {
                  if (Actor.aV[var4] == var0) {
                     Actor.aV[var4] = null;
                     break;
                  }
               }
            }

            if (Actor.er != null) {
               for (int var5 = 0; var5 < Actor.er.length; var5++) {
                  if (Actor.er[var5] == var0) {
                     Actor.er[var5] = null;
                     break;
                  }
               }
            }

            if (Actor.E == var0) {
               Actor.E = null;
            }
         }
      }
   }

   public static final Actor f(Actor var0) {
      if (var0 == null) {
         return null;
      }

      for (int var1 = 0; var1 < actorCount; var1++) {
         if (actors[var1] != null && actors[var1].r == var0) {
            return actors[var1];
         }
      }

      return null;
   }

   public static final Actor i(int var0) {
      if (player != null && player.an == var0) {
         return player;
      }

      for (int var1 = 0; var1 < actorCount; var1++) {
         if (actors[var1] != null && actors[var1].an == var0) {
            return actors[var1];
         }
      }

      if (bZ != null && var0 == bZ.an) {
         return bZ;
      } else {
         return ca != null && var0 == ca.an ? ca : null;
      }
   }

   public static final void g(Actor var0) {
      if (cp >= cn) {
         System.out.print("s_cine_actors_num exceed maxnum" + cn);
      } else {
         co[cp++] = var0;
      }
   }

   public static final void w() {
      for (int var0 = 0; var0 < cp; var0++) {
         co[var0] = null;
      }

      cp = 0;
   }

   public static final Actor j(int var0) {
      for (int var1 = 0; var1 < cp; var1++) {
         if (co[var1] != null && co[var1].an == var0) {
            return co[var1];
         }
      }

      return null;
   }

   public static final void a(Actor var0, int var1) {
      Actor.bH[var1 * 18] = (byte)var0.animationId;
      Actor.bH[var1 * 18 + 1] = (byte)var0.h;
      a(Actor.bH, var1 * 18 + 2, (short)var0.positionX);
      a(Actor.bH, var1 * 18 + 4, (short)var0.positionY);
      b(Actor.bH, var1 * 18 + 6, var0.aq);
      b(Actor.bH, var1 * 18 + 10, var0.c);
      b(Actor.bH, var1 * 18 + 14, var0.aA);
   }

   public static final void b(Actor var0, int var1) {
      var0.animationId = Actor.bH[var1 * 18];
      var0.h = Actor.bH[var1 * 18 + 1];
      var0.positionX = b(Actor.bH, var1 * 18 + 2);
      var0.positionY = b(Actor.bH, var1 * 18 + 4);
      var0.aq = c(Actor.bH, var1 * 18 + 6);
      var0.c = c(Actor.bH, var1 * 18 + 10);
      var0.aA = c(Actor.bH, var1 * 18 + 14);
   }

   public static final void h(Actor var0) {
      a(Actor.dy, 0, (short)var0.ay);
      a(Actor.dy, 2, (short)var0.aw);
      a(Actor.dy, 4, (short)var0.ax);
      a(Actor.dy, 6, (short)var0.av);
      a(Actor.dy, 8, (short)var0.az);
      a(Actor.dy, 10, (short)var0.ac);
      a(Actor.dy, 12, (short)var0.ad);
      a(Actor.dy, 14, (short)Actor.dz);
      a(Actor.dy, 16, (short)Actor.dA);
      a(Actor.dy, 18, (short)Actor.dB);
      a(Actor.dy, 20, (short)Actor.dC);
      a(Actor.dy, 22, (short)Actor.dD);
      a(Actor.dy, 24, (short)(Actor.dE ? 1 : 0));
      a(Actor.dy, 26, (short)Actor.dG);
   }

   public static final void i(Actor var0) {
      var0.ay = b(Actor.dy, 0);
      var0.aw = b(Actor.dy, 2);
      var0.ax = b(Actor.dy, 4);
      var0.av = b(Actor.dy, 6);
      var0.az = b(Actor.dy, 8);
      var0.ac = b(Actor.dy, 10);
      var0.ad = b(Actor.dy, 12);
      Actor.dz = b(Actor.dy, 14);
      Actor.dA = b(Actor.dy, 16);
      Actor.dB = b(Actor.dy, 18);
      Actor.dC = b(Actor.dy, 20);
      Actor.dD = b(Actor.dy, 22);
      Actor.dE = b(Actor.dy, 24) == 1;
      Actor.dG = b(Actor.dy, 26);
   }

   public static final void c(Actor var0, int var1) {
      a(Actor.dx, var1 * 20 + 0, (short)var0.positionX);
      a(Actor.dx, var1 * 20 + 2, (short)var0.positionY);
      b(Actor.dx, var1 * 20 + 4, (short)var0.G);
      b(Actor.dx, var1 * 20 + 8, (short)var0.H);
      b(Actor.dx, var1 * 20 + 12, var0.aq);
      b(Actor.dx, var1 * 20 + 16, var0.c);
   }

   public static final void d(Actor var0, int var1) {
      var0.positionX = b(Actor.dx, var1 * 20 + 0);
      var0.positionY = b(Actor.dx, var1 * 20 + 2);
      var0.G = c(Actor.dx, var1 * 20 + 4);
      var0.H = c(Actor.dx, var1 * 20 + 8);
      var0.aq = c(Actor.dx, var1 * 20 + 12);
      var0.c = c(Actor.dx, var1 * 20 + 16);
   }

   private static boolean O(int var0) {
      return var0 == 35 || var0 == 23 || var0 == 48 || var0 == 82;
   }

   private static void f(boolean var0) {
      int var2 = 0;
      if (!var0) {
         for (int var3 = 0; var3 < cc; var3++) {
            cq[var3] = 0;
         }
      }

      int var10 = 0;

      while (var2 < cs.length) {
         byte var1 = cs[var2++];
         int var4 = var2;

         for (int var5 = 0; var5 < var1; var5++) {
            cr[var5] = (short)((cs[var2++] & 255) + (cs[var2++] << 8));
         }

         boolean var11 = false;
         if (levelIndex != 16) {
            for (int var6 = 0; var6 <= cv; var6++) {
               if (cu[var6] == cr[1]) {
                  var11 = true;
                  break;
               }
            }
         }

         if (var11) {
            var10++;
         } else {
            Actor var12 = null;
            short var7 = cr[0];
            int var8 = 0;
            if (var7 != 0) {
               var8 = var10++;
               if (!var0) {
                  (var12 = new Actor(cr, var4, var1)).af = var8;
               } else {
                  if (cq[var10 - 1] == -99 && O(var7)) {
                     continue;
                  }

                  cq[var10 - 1] = 0;
                  (var12 = new Actor(cr, var4, var1)).af = var8;
               }
            }

            if (var7 == 0) {
               if (player == null) {
                  player = new Actor(cr, var4, var1);
               }
            } else if (var7 == 69) {
               if (bZ == null) {
                  bZ = new Actor(cr, var4, var1);
               } else if (ca == null) {
                  ca = new Actor(cr, var4, var1);
               }
            } else {
               d(var12);
               if (var12.type == 28 && cb == null) {
                  cb = var12;
               }

               actors[actorCount - 1].af = var10 - 1;
               var12.a(cr);
            }
         }
      }

      aq();
   }

   public static final void x() {
      for (int var0 = 0; var0 < actorCount; var0++) {
         if (actors[var0] != null) {
            actors[var0].a();
            actors[var0] = null;
         }
      }

      y();
      s();
      Actor.aX = null;
      Actor.aY = null;
      actorCount = 0;
      cm = 0;
      bN = 0;
      player = null;
      bZ = null;
      ca = null;
      bY = null;
      aJ = null;
      cb = null;
      Actor.er = null;
      Actor.aV = null;
      Actor.ba = null;
      aB = -1;
      ay = false;
      o();
   }

   private static void ay() throws Exception {
      a("/1");
      a = U(0);
      B();
   }

   private static void az() throws Exception {
      a("/2");
      b[16] = U(16);
      b[33] = U(33);
      b[31] = U(31);
      b[31].a(0, 0, -1, -1);
      b[31].a();
   }

   private static void aA() throws Exception {
      b[17] = U(17);
      B();
   }

   private static void P(int var0) throws Exception {
      a(fd[var0]);
      byte[] var1 = q(0);
      byte[] var2 = q(1);
      B();
      cs = var1;
      b(var2);
   }

   private static void aB() {
      for (int var0 = 0; var0 < 52; var0++) {
         fe[var0] = 0;
      }

      short[] var1 = new short[20];
      int var2 = 0;

      while (var2 < cs.length) {
         byte var4 = cs[var2++];

         for (int var3 = 0; var3 < var4; var3++) {
            var1[var3] = (short)((cs[var2++] & 255) + (cs[var2++] << 8));
         }

         if (var1[0] < Actor.bs.length && Actor.bs[var1[0]] != -1) {
            fe[Actor.bs[var1[0]]] = 1;
         }

         if (var1[0] == 42 || var1[0] == 78) {
            k(18);
            if (var1[0] == 78) {
               k(44);
            }
         } else if (var1[0] == 41) {
            k(29);
            k(30);
            k(36);
         } else if (var1[0] == 38) {
            k(20);
         }
      }

      k(17);
      k(50);
      if (levelIndex <= 1) {
         k(22);
      }

      k(31);
      k(37);
      k(16);
      k(38);
      k(51);
      k(42);
      if (levelIndex <= 1) {
         k(23);
      }

      if (levelIndex == 0) {
         k(35);
         k(49);
         k(13);
      } else if (levelIndex == 6) {
         k(48);
         k(49);
         k(13);
         k(14);
         k(11);
      } else if (levelIndex == 8) {
         k(48);
         k(49);
         k(13);
      } else if (levelIndex == 9) {
         k(48);
         k(49);
      } else if (levelIndex == 4) {
         k(49);
      } else if (levelIndex == 16) {
         k(39);
         k(49);
      } else if (levelIndex == 10) {
         k(44);
         k(26);
         k(48);
      }

      k(38);
      if (fe[Actor.bs[14]] == 1) {
         k(25);
      }

      if (fe[Actor.bs[73]] == 1 || fe[Actor.bs[83]] == 1 || levelIndex != 4 && levelIndex != 6) {
         k(19);
      }

      if (fe[Actor.bs[13]] == 1) {
         k(11);
      }

      if (fe[Actor.bs[90]] == 1) {
         k(14);
      }

      if (fe[Actor.bs[90]] == 1 || fe[Actor.bs[92]] == 1) {
         k(15);
      }

      if (fe[Actor.bs[83]] == 1) {
         k(19);
         k(44);
      }

      k(3);
   }

   public static final void k(int var0) {
      if (var0 >= 0) {
         fe[var0] = 1;
      }
   }

   private static boolean Q(int var0) {
      try {
         if (var0 == 1) {
            eF = null;
            bh = false;
            V(1 + (levelIndex == 16 ? 11 : levelIndex));
            if (levelIndex == 8 || levelIndex == 9) {
               bo = -1;
            }
         } else if (var0 == 3) {
            ax();
         } else if (var0 < 14) {
            int var1;
            q(var1 = var0 - 4, levelIndex);
         } else if (var0 < 15) {
            c(levelIndex, true);
            i = new int[19200];
            boolean var4 = false;

            for (int var2 = 0; var2 < 19200; var2++) {
               i[var2] = 1712008019;
            }

            j = Image.createRGBImage(i, 120, 160, true);
         } else if (var0 == 15) {
            P(levelIndex);
         } else if (var0 == 17) {
            a("/2");
            aB();
         } else if (var0 < 70 && var0 > 17) {
            int var5 = var0 - 18;
            if (fe[var5] == 1) {
               if (31 != var5 && 17 != var5) {
                  b[var5] = U(var5);
               }

               if (var5 == 20) {
                  BSprite.a(b[var5], 0, 16768066);
                  b[var5].a(0, 0, -1, -1);
                  b[var5].a();
               } else if (var5 == 30) {
                  b[29].a(b[var5], 0, 0, -1, -1);
                  b[29].a();
                  b[var5] = null;
               } else if (var5 == 36) {
                  BSprite.a(b[var5], 0, 0);
                  b[var5].a(0, 0, -1, -1);
                  b[var5].a();
               } else if (var5 == 22) {
                  BSprite.a(b[var5], 0, 16777215);
                  b[var5].a(0, 0, -1, -1);
                  b[var5].a();
               } else if (var5 == 38) {
                  BSprite.b(b[var5], 0, 221);
                  b[var5].a(0, 0, -1, -1);
                  b[var5].a();
               } else if (var5 == 50) {
                  BSprite.a(b[var5], 0, -16736257);
                  b[var5].a(0, 0, -1, -1);
                  b[var5].a();
               } else if (var5 == 6 && levelIndex == 0) {
                  b[var5].R = 10;
                  b[var5].b();
               } else if (var5 == 24 || var5 == 28 || var5 == 27 || var5 == 25) {
                  b[var5].a(0, 0, -1, -1);
                  b[var5].a();
               }
            }
         } else if (var0 == 70) {
            B();
            V();
         } else if (var0 != 75) {
            if (var0 == 76) {
               System.gc();
            } else if (var0 != 77) {
               if (var0 == 78) {
                  z();
                  if (ff) {
                     g(true);
                     aE();
                     r();
                     ff = false;
                  } else {
                     f(false);
                  }
               } else {
                  if (var0 > 78) {
                     Actor.g(Actor.ch);
                     return true;
                  }

                  System.gc();
               }
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return false;
   }

   private static void aC() {
      x();
      cs = null;
      dQ = false;
      Actor.m();
      v();
      H();
      aw();
      aG();
      eB = null;
      eC = null;
      eD = null;
      eA = null;
      i = null;
      j = null;

      for (int var0 = 0; var0 < 52; var0++) {
         if (var0 != 33 && var0 != 31 && var0 != 17 && var0 != 16) {
            b[var0] = null;
         }
      }

      if (BSprite.I != null) {
         for (int var1 = 0; var1 < BSprite.I.length; var1++) {
            BSprite.I[var1] = null;
         }

         BSprite.I = null;
      }

      for (int var2 = 1; var2 < 13; var2++) {
         W(var2);
      }

      System.gc();
   }

   public static final void y() {
      for (int var0 = 0; var0 < cd; var0++) {
         if (ch[var0] != null) {
            ch[var0].p = null;
            ch[var0] = null;
         }
      }

      ci = 0;
      ck = 0;

      for (int var1 = 0; var1 < ce; var1++) {
         if (cj[var1] != null) {
            cj[var1].p = null;
            cj[var1] = null;
         }
      }

      cp = 0;

      for (int var2 = 0; var2 < cn; var2++) {
         if (co[var2] != null) {
            co[var2].p = null;
            co[var2] = null;
         }
      }
   }

   public static final void z() {
      if (levelIndex != 16) {
         for (int var0 = 0; var0 < 11; var0++) {
            cw[var0] = b(dt, 90 + 2 * var0);
         }

         cv = cw[levelIndex];

         for (int var2 = 0; var2 < 11; var2++) {
            for (int var1 = 0; var1 < 30; var1++) {
               ct[var2][var1] = b(dt, 112 + var2 * 30 * 2 + var1 * 2);
            }
         }

         cu = ct[levelIndex];

         for (int var3 = 0; var3 < cc; var3++) {
            cq[var3] = a(dt, 1127 + var3);
         }
      }
   }

   private static void aD() {
      health = 60;
      maxHealth = 60;
      magic = 60;
      maxMagic = 60;
      z = 0;
      C = 0;
      D = 0;
   }

   private static void aE() {
      if (levelIndex == 4) {
         Actor.bM = a(dt, 772) == 1;
         if (bZ != null && Actor.bM) {
            ca.positionX = bZ.positionX = b(dt, 773);
            ca.positionY = bZ.positionY = b(dt, 775);
            ca.aq = bZ.aq = c(dt, 777);
         }
      }

      if (player != null && !cx) {
         player.positionX = b(dt, 18);
         player.positionY = b(dt, 20);
         player.facingLeft = b(dt, 22) == 1;
         bh = b(dt, 24) == 1;
         if (levelIndex == 6) {
            player.positionY -= 20;
         }

         if (levelIndex == 9) {
            player.positionY -= 20;
         }
      }

      A();
      Actor.cv = b(dt, 28);
      if (player != null && !cx) {
         short var0 = b(dt, 26);
         player.h(var0);
      }

      for (int var1 = 0; var1 < 3; var1++) {
         Actor.ej[var1] = a(dt, 802 + var1);
      }
   }

   public static final void A() {
      Actor.ch = b(dt, 30);

      for (int var0 = 0; var0 < 2; var0++) {
         Actor.cj[var0] = b(dt, 32 + 2 * var0);
         Actor.ck[var0] = b(dt, 36 + 2 * var0);
         Actor.cl[var0] = b(dt, 40 + 2 * var0);
         Actor.cm[var0] = b(dt, 44 + 2 * var0);
      }

      maxHealth = b(dt, 64);
      health = b(dt, 66);
      z = b(dt, 68);
      maxMagic = b(dt, 70);
      magic = b(dt, 72);
      C = b(dt, 74);
      D = c(dt, 76);

      for (int var1 = 0; var1 < 8; var1++) {
         aY[var1] = b(dt, 48 + var1 * 2);
      }

      Actor.bu = a(dt, 781) == 1;
      Actor.bv = a(dt, 782) == 1;
      Actor.bw = a(dt, 783) == 1;
      Actor.bx = a(dt, 784) == 1;
      Actor.bz = a(dt, 785) == 1;
      Actor.by = a(dt, 786) == 1;
      Actor.bA = a(dt, 787) == 1;
      Actor.bB = a(dt, 788) == 1;
      Actor.bC = a(dt, 789) == 1;
      Actor.bD = a(dt, 790) == 1;
      Actor.bF = a(dt, 791) == 1;
   }

   private static void g(boolean var0) {
      if (levelIndex == 6 && var0) {
         for (int var1 = 0; var1 < 3; var1++) {
            Actor.bG[var1] = b(dt, 792 + var1 * 2);
         }

         for (int var6 = 0; var6 < 54; var6++) {
            Actor.bH[var6] = a(dt, 805 + var6);
         }
      }

      if (levelIndex == 9 && var0) {
         Actor.dt = b(dt, 865);
         Actor.du = b(dt, 867);
         Actor.dv = b(dt, 869);
         Actor.dw = b(dt, 871);
         if (Actor.dt != 0) {
            ac = b(dt, 873);
            ad = b(dt, 875);
            cameraX = ac;
            cameraY = ad;
            viewBounds[0] = cameraX;
            viewBounds[2] = cameraX + 240;
            viewBounds[1] = cameraY;
            viewBounds[3] = cameraY + 320;
            aE = b(dt, 877);
         }

         for (int var7 = 0; var7 < 220; var7++) {
            Actor.dx[var7] = a(dt, 907 + var7);
         }

         for (int var8 = 0; var8 < Actor.dy.length; var8++) {
            Actor.dy[var8] = a(dt, 879 + var8);
         }
      }

      for (int var9 = 0; var9 < 3; var9++) {
         Actor.ej[var9] = a(dt, 802 + var9);
      }

      if (ff) {
         f(false);
      } else {
         f(var0);
      }

      if (levelIndex == 6 && var0) {
         aB = b(dt, 859);

         for (int var2 = 0; var2 < actorCount; var2++) {
            Actor var10 = actors[var2];

            for (int var3 = 0; var3 < 3; var3++) {
               if (Actor.bG[var3] != -1 && Actor.bG[var3] == var10.an) {
                  Actor.dU[var3] = var10;
                  b(Actor.dU[var3], var3);
               }
            }

            if (aB != -1) {
               if (aB == var10.an) {
                  var10.c |= 16;
                  var10.c &= -33;
               }

               if (var10.aG == aB) {
                  short var16 = b(dt, 861);
                  short var4 = b(dt, 863);
                  int var5 = var16 + var4;
                  viewBounds[1] = ad = cameraY = var16 - 2;
                  var10.av = var10.positionY - var5;
                  var10.positionY = var5;
                  viewBounds[3] = cameraY + 320;
                  var10.c |= 16;
                  var10.c &= -33;
                  az = 4;
               }
            }
         }

         for (int var12 = 0; var12 < 3; var12++) {
            if (Actor.bG[var12] == -1) {
               Actor.dU[var12] = null;
            }
         }
      }

      if (levelIndex == 9 && var0) {
         aB = b(dt, 859);

         for (int var13 = 0; var13 < actorCount; var13++) {
            Actor var11 = actors[var13];
            if (Actor.dt == var11.an) {
               Actor.R();
               Actor.dp[0] = var11;
               d(Actor.dp[0], 0);
            }

            if (aB != -1 && aB == var11.an) {
               var11.c |= 16;
               var11.c &= -33;
            }
         }

         i(Actor.dp[0]);
         if (Actor.dp != null) {
            Actor.dp[0].aH = Actor.dw;
            Actor.dp[0].Q();

            for (int var14 = 1; var14 < 11; var14++) {
               d(Actor.dp[var14], var14);
            }

            for (int var15 = 0; var15 < 10; var15++) {
               Actor.dp[var15].b(Actor.dp[var15 + 1].positionX, Actor.dp[var15 + 1].positionY, Actor.dp[var15 + 1].aK);
            }

            if (Actor.dw == 1) {
               Actor.dp[0].s = i(Actor.dv);
            } else {
               e(i(Actor.du));
            }

            if (Actor.dp[0].s != null) {
               Actor.dp[0].s.c |= 16;
               Actor.dp[0].s.c &= -33;
            }
         }
      }
   }

   private static void aF() {
      x();
      Actor.m();
      Actor.am();
      t();
      v();
      z();
      g(true);
      aE();
      Actor.g(Actor.ch);
      health = maxHealth;
      G();
      aa();
      bs = 0;
      br = 0;
      a(8);
   }

   private static byte[] R(int var0) throws Exception {
      byte[] var1 = q(var0);
      p(var0 + 1);
      cy = D();
      cz = D();
      return var1;
   }

   private static byte[] S(int var0) throws Exception {
      return q(var0);
   }

   private static boolean T(int var0) {
      return var0 == 10 || var0 == 15;
   }

   private static void c(int var0, boolean var1) throws Exception {
      if (!T(var0)) {
         a(fq[var0]);
         fh = R(!var1 ? 6 : 11);
         fi = S(!var1 ? 8 : 13);
         cD = cy;
         cF = cz;
         fk = R(!var1 ? 9 : 14);
         fl = S(!var1 ? 11 : 16);
         cC = cy;
         cE = cz;
         B();
      }

      a(fd[var0]);
      if (var1) {
         fj = R(2);
      }

      fm = R(var1 ? 5 : 0);
      fn = S(var1 ? 7 : 2);
      fo = R(var1 ? 8 : 3);
      fp = S(var1 ? 10 : 5);
      levelPixelWidth = cy * 20;
      cB = cz * 20;
      B();
      if (var1) {
         for (int var2 = 0; var2 < fj.length; var2++) {
            if (fj[var2] == -1) {
               fj[var2] = 0;
            }
         }
      }

      aM = -1;
   }

   private static void aG() {
      fh = null;
      fi = null;
      fm = null;
      fn = null;
      fo = null;
      fp = null;
      fj = null;
      fk = null;
      fl = null;
   }

   public static final int g(int var0, int var1) {
      if (var0 >= 0 && var0 < cy && var1 < cz) {
         if (be[levelIndex] == 3) {
            if (var1 < 0) {
               return 0;
            }
         } else if (var1 < 0) {
            return 24;
         }

         return fj[var0 + var1 * cy];
      } else {
         return be[levelIndex] == 2 ? 0 : 24;
      }
   }

   public static final void d(int var0, int var1, int var2) {
      if (var0 >= 0 && var1 >= 0 && var0 < cy && var1 < cz) {
         fj[var0 + var1 * cy] = (byte)var2;
      }
   }

   private static void aH() throws Exception {
      a("/22");
      p(0);
      cG = new int[256];

      for (int var0 = 0; var0 < 256; var0++) {
         cG[var0] = E();
      }

      B();
   }

   private static void aI() throws Exception {
      a("/23");
      byte[] var3 = q(0);
      B();
      ByteArrayInputStream var4 = new ByteArrayInputStream(var3);
      DataInputStream var5;
      cH = new int[(var5 = new DataInputStream(var4)).readInt()][];

      for (int var0 = 0; var0 < cH.length; var0++) {
         int var2 = var5.readInt();
         cH[var0] = new int[var2];

         for (int var1 = 0; var1 < var2; var1++) {
            cH[var0][var1] = var5.readInt();
         }
      }
   }

   public static final int h(int var0, int var1) {
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

   public static final int l(int var0) {
      while (var0 < 0) {
         var0 += 360;
      }

      return cG[var0 % 360 * 256 / 360];
   }

   public static final int m(int var0) {
      return l(90 - var0);
   }

   public static final int i(int var0, int var1) {
      if (var1 != var0) {
         int var2;
         if ((var2 = random.nextInt()) < 0) {
            var2 *= -1;
         }

         return var0 + var2 % (var1 - var0);
      } else {
         return var1;
      }
   }

   public static final int j(int var0, int var1) {
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

   private static BSprite U(int var0) throws Exception {
      BSprite var1 = new BSprite();
      byte[] var2 = q(var0);
      var1.a(var2, 0);
      return var1;
   }

   public static final BSprite n(int var0) {
      return b[var0];
   }

   private static void V(int var0) throws Exception {
      a("/20");
      byte[] var1 = q(var0);
      B();
      a(var0, var1);
      String var2 = fr[0][29];

      for (int var3 = 0; var3 < cI.length; var3++) {
         int var4 = cI[var3];
         fr[0][var4] = a(fr[0][var4], "#SK", var2);
      }

      fr[0][29] = null;
   }

   private static void W(int var0) {
      o(var0);
   }

   static final void a(int var0, byte[] var1) {
      if (var1 != null) {
         try {
            ByteArrayInputStream var2 = new ByteArrayInputStream(var1);
            DataInputStream var3;
            (var3 = new DataInputStream(var2)).skip(6L);
            short var4 = var3.readShort();
            fr[var0] = new String[var4];

            for (int var5 = 0; var5 < var4; var5++) {
               fr[var0][var5] = var3.readUTF().toUpperCase().trim();
            }

            var3.close();
            var2.close();
         } catch (Exception var6) {
         }
      }
   }

   static final void o(int var0) {
      fr[var0] = null;
   }

   static final String k(int var0, int var1) {
      return fr[var0][var1];
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
      if (fs == null || var0.compareTo(fs) != 0) {
         if (ft != null) {
            B();
         }

         fs = var0;
         ft = b(fs);
         fv = (short)D();
         fx = (short)D();
         fy = new short[fx];

         for (int var1 = 0; var1 < fx; var1++) {
            fy[var1] = (short)D();
         }

         fz = 0;
         aJ();
      }
   }

   private static void aJ() throws Exception {
      int var0;
      if (fz == fx - 1) {
         var0 = fv - fy[fz];
      } else {
         var0 = fy[fz + 1] - fy[fz];
      }

      fw = new int[var0 + 1];

      for (int var1 = 0; var1 < var0 + 1; var1++) {
         fw[var1] = E();
      }
   }

   static final void B() throws Exception {
      if (ft != null) {
         ft.close();
         ft = null;
      }

      fu = 0;
   }

   static final int p(int var0) throws Exception {
      int var1 = fx - 1;

      while (var1 >= 0 && fy[var1] > var0) {
         var1--;
      }

      if (fz != var1) {
         fz = var1;
         B();
         if (fz == 0) {
            ft = b(fs);
            D();
            D();

            for (int var2 = 0; var2 < fx; var2++) {
               D();
            }
         } else {
            ft = b(fs + "." + fz);
         }

         aJ();
      } else if (ft == null) {
         if (fz == 0) {
            ft = b(fs);
         } else {
            ft = b(fs + "." + fz);
         }
      }

      var0 -= fy[fz];
      int var5 = fw[var0];
      int var3 = fw[var0 + 1] - fw[var0] - 1;
      r(var5);
      C();
      return var3;
   }

   static final byte[] q(int var0) throws Exception {
      int var1;
      if ((var1 = p(var0)) < 0) {
         return null;
      }

      byte[] var2;
      c(var2 = new byte[var1], 0, var2.length);
      return var2;
   }

   static final void r(int var0) throws Exception {
      if (fu != var0) {
         if (fu > var0) {
            B();
            if (fz == 0) {
               ft = b(fs);
            } else {
               ft = b(fs + "." + fz);
            }
         } else {
            var0 -= fu;
         }

         s(var0);
      }
   }

   static final void s(int var0) throws Exception {
      if (var0 != 0) {
         if (fA == null) {
            fA = new byte[256];
         }

         while (var0 > 256) {
            c(fA, 0, 256);
            var0 -= 256;
         }

         if (var0 > 0) {
            c(fA, 0, var0);
         }
      }
   }

   static final int C() throws Exception {
      int var0;
      if ((var0 = ft.read()) >= 0) {
         fu++;
      }

      return var0;
   }

   static final int D() throws Exception {
      return C() & 0xFF | (C() & 0xFF) << 8;
   }

   static final int E() throws Exception {
      return C() & 0xFF | (C() & 0xFF) << 8 | (C() & 0xFF) << 16 | (C() & 0xFF) << 24;
   }

   static final int c(byte[] var0, int var1, int var2) throws Exception {
      int var3 = var1;
      int var4 = var2;
      int var5 = 0;

      while (var4 > 0) {
         var5 = ft.read(var0, var3, var4);
         var4 -= var5;
         var3 += var5;
      }

      fu += var2;
      return var2;
   }

   static final int F() {
      return cO == cK ? 255 : cJ[cO++] & 0xFF;
   }

   static final void d(byte[] var0, int var1) {
      cJ = var0;
      cK = var1;
      cO = 0;
      cM = 0L;
      cL = 4294967295L;

      for (int var2 = 0; var2 < 5; var2++) {
         cM = cM << 8 | F();
      }
   }

   static final int t(int var0) {
      long var1 = (cL >> 11) * fB[var0];
      if (cM < var1) {
         cL = var1;
         int var3 = 2048 - fB[var0];
         fB[var0] = (short)(fB[var0] + (var3 >> 5));
         if (cL < 16777216L) {
            cM = cM << 8 | F();
            cL <<= 8;
         }

         return 0;
      } else {
         cL -= var1;
         cM -= var1;
         fB[var0] = (short)(fB[var0] - (fB[var0] >> 5));
         if (cL < 16777216L) {
            cM = cM << 8 | F();
            cL <<= 8;
         }

         return 1;
      }
   }

   static final int a(int var0, byte var1) {
      int var2 = 1;

      do {
         int var4 = var1 >> 7 & 1;
         var1 = (byte)(var1 << 1);
         int var3 = t(var0 + (1 + var4 << 8) + var2);
         var2 = var2 << 1 | var3;
         if (var4 != var3) {
            while (var2 < 256) {
               var2 = var2 << 1 | t(var0 + var2);
            }
            break;
         }
      } while (var2 < 256);

      return var2;
   }

   static final int u(int var0) {
      int var1 = 1;

      while ((var1 = var1 << 1 | t(var0 + var1)) < 256) {
      }

      return var1;
   }

   static final int l(int var0, int var1) {
      int var2 = 1;

      for (int var3 = var1; var3 > 0; var3--) {
         var2 = (var2 << 1) + t(var0 + var2);
      }

      return var2 - (1 << var1);
   }

   static final int m(int var0, int var1) {
      if (t(var0 + 0) == 0) {
         return l(var0 + 2 + (var1 << 3), 3);
      } else {
         return t(var0 + 1) == 0 ? 8 + l(var0 + 130 + (var1 << 3), 3) : 16 + l(var0 + 258, 8);
      }
   }

   static final int n(int var0, int var1) {
      int var2 = 1;
      int var4 = 0;

      for (int var3 = 0; var3 < var1; var3++) {
         int var5 = t(var0 + var2);
         var2 = (var2 << 1) + var5;
         var4 |= var5 << var3;
      }

      return var4;
   }

   static final int v(int var0) {
      long var1 = cL;
      long var3 = cM;
      int var5 = 0;

      for (int var6 = var0; var6 > 0; var6--) {
         var1 >>= 1;
         var5 <<= 1;
         if (var3 >= var1) {
            var3 -= var1;
            var5 |= 1;
         }

         if (var1 < 16777216L) {
            var1 <<= 8;
            var3 = var3 << 8 | F();
         }
      }

      cL = var1;
      cM = var3;
      return var5;
   }

   static final void a(int var0, int var1, int var2, int var3, byte[] var4, int var5) {
      int var6 = var4.length;
      int var7 = 1846 + (768 << var1 + var2);
      short[] var8 = fB;
      int var10 = 0;
      boolean var11 = false;
      int var12 = 0;
      int var25 = 1;
      int var14 = 1;
      int var15 = 1;
      int var16 = 1;
      int var17 = 0;
      int var18 = (1 << var3) - 1;
      int var19 = (1 << var2) - 1;
      int var20 = 0;
      if (var0 >= var7 * 2) {
         for (int var9 = 0; var9 < var7; var9++) {
            var8[var9] = 1024;
         }

         d(var4, var6);

         while (var17 < var5) {
            int var21 = var17 & var18;
            if (t(0 + (var10 << 4) + var21) == 0) {
               int var30 = 1846 + 768 * (((var17 & var19) << var1) + ((var12 & 0xFF) >> 8 - var1));
               if (var10 < 4) {
                  var10 = 0;
               } else if (var10 < 10) {
                  var10 -= 3;
               } else {
                  var10 -= 6;
               }

               if (var11) {
                  byte var31 = cN[var17 - var25];
                  var12 = a(var30, var31) & 0xFF;
                  var11 = false;
               } else {
                  var12 = u(var30) & 0xFF;
               }

               cN[var17++] = (byte)var12;
            } else {
               var11 = true;
               if (t(192 + var10) == 1) {
                  if (t(204 + var10) == 0) {
                     if (t(240 + (var10 << 4) + var21) == 0) {
                        var10 = (byte)(var10 < 7 ? 9 : 11);
                        var12 = cN[var17 - var25] & 255;
                        cN[var17++] = (byte)var12;
                        continue;
                     }
                  } else {
                     int var22;
                     if (t(216 + var10) == 0) {
                        var22 = var14;
                     } else {
                        if (t(228 + var10) == 0) {
                           var22 = var15;
                        } else {
                           var22 = var16;
                           var16 = var15;
                        }

                        var15 = var14;
                     }

                     var14 = var25;
                     var25 = var22;
                  }

                  var20 = m(1332, var21);
                  var10 = (byte)(var10 < 7 ? 8 : 11);
               } else {
                  var16 = var15;
                  var15 = var14;
                  var14 = var25;
                  var10 = (byte)(var10 < 7 ? 7 : 10);
                  var20 = m(818, var21);
                  int var29;
                  if ((var29 = l(432 + ((var20 < 4 ? var20 : 3) << 6), 6)) >= 4) {
                     int var23 = (var29 >> 1) - 1;
                     var25 = (2 | var29 & 1) << var23;
                     if (var29 < 14) {
                        var25 += n(688 + var25 - var29 - 1, var23);
                     } else {
                        int var26;
                        var25 = (var26 = var25 + (v(var23 - 4) << 4)) + n(802, 4);
                     }
                  } else {
                     var25 = var29;
                  }

                  var25++;
               }

               var20 += 2;

               while (true) {
                  var12 = cN[var17 - var25] & 255;
                  cN[var17++] = (byte)var12;
                  if (--var20 <= 0 || var17 >= var5) {
                     break;
                  }
               }
            }
         }
      }
   }

   public static final void a(byte[] var0) throws Exception {
      byte[] var1 = new byte[13];
      byte[] var2 = new byte[var0.length - 13];
      System.arraycopy(var0, 0, var1, 0, 13);
      System.arraycopy(var0, 13, var2, 0, var2.length);
      int[] var3 = new int[5];

      for (int var4 = 0; var4 < 5; var4++) {
         var3[var4] = var1[var4] & 255;
      }

      int var11 = 0;

      for (int var6 = 0; var6 < 4; var6++) {
         int var5 = var1[var6 + 5] & 255;
         var11 += var5 << (var6 << 3);
      }

      int var12 = var3[0];
      int var9 = 0;

      while (var12 >= 45) {
         var9++;
         var12 -= 45;
      }

      int var8 = 0;

      while (var12 >= 9) {
         var8++;
         var12 -= 9;
      }

      int var7 = var12;
      int var10 = 1846 + (768 << var7 + var8);
      cN = new byte[var11];
      fB = new short[var10];
      a(var10 * 2, var7, var8, var9, var2, var11);
      fB = null;
      cJ = null;
      System.gc();
   }

   public static final InputStream b(String var0) {
      try {
         DataInputStream var1;
         byte[] var3 = new byte[(var1 = new DataInputStream(var0.getClass().getResourceAsStream(var0))).readInt()];
         var1.readFully(var3);
         var1.close();
         a(var3);
         ByteArrayInputStream var4 = new ByteArrayInputStream(cN);
         cN = null;
         return var4;
      } catch (Exception var5) {
         return null;
      }
   }

   private void aK() {
      aM();
      a(0, 0, 240, 320);
      if (eE == null) {
         b(0, true);
      }

      eQ.a(graphics, 0, 120, 127, 0, 0, 0);
      if (dG) {
         a.z = 0;
         a.a(graphics, "is GOD MODULE!", 237, 20, 9);
      }

      if (dG) {
         graphics.setColor(16777215);
         graphics.drawString("level :" + levelIndex, 0, 10, 0);
         if (G(1)) {
            levelIndex++;
         }

         if (levelIndex > 10) {
            levelIndex = 0;
         }

         if (G(2048)) {
            eE = null;
            a(9);
         }
      }

      a.z = 0;
      g(fC, 120, 255);
      a.z = 0;
      if (eN) {
         ac();
      } else {
         d(fD[fC].length, true);
      }

      N(fC);
   }

   private static String s(int var0, int var1) {
      return k(0, fD[var0][var1]);
   }

   private static void f(int var0, int var1, int var2) {
      if (var0 == 1) {
         cQ = 35 * cP - 26;

         for (int var3 = 0; var3 < 6; var3++) {
            b[33].a(graphics, 66, 60, 38 + var3 * 35, 0, 0, 0);
         }
      }

      if (cR) {
         int var8 = stateFrame % 4;
         b[33].a(graphics, 14, var8, 60, 64 + cQ, 0, 0, 0);
         if (var8 == 3) {
            cR = false;
            cS = true;
            cT = true;
         } else {
            cT = false;
         }
      }

      int var4 = 0;

      for (int var5 = 0; var5 < fD[var0].length; var5++) {
         if (var0 != 0 && var0 != 2 || (!InGamePromotion.a() ? fD[var0][var5] != 4 && fD[var0][var5] != 5 : InGamePromotion.d() || fD[var0][var5] != 5)) {
            int var6 = var1;
            String var7 = s(var0, var5);
            if ((var0 == 0 || var0 == 2) && InGamePromotion.a() && fD[var0][var5] == 4) {
               a.z = cP == var5 ? 4 : 2;
            } else if ((var0 == 0 || var0 == 2) && InGamePromotion.a() && InGamePromotion.d() && fD[var0][var5] == 5) {
               a.z = cP == var5 ? 4 : 2;
            } else if (var0 == 1) {
               a.z = cP == var5 ? 2 : 5;
            } else {
               a.z = cP == var5 ? 4 : 5;
            }

            if (fD[var0][var5] == 6) {
               a.a(graphics, var7 + ": " + (el ? k(0, 23) : k(0, 24)), var6, 35 * var4 + var2, 3);
            } else {
               a.a(graphics, var7, var6, 35 * var4 + var2, 3);
            }

            if (fD[var0][var5] == 4) {
               InGamePromotion.a(graphics, eG, var6 + BSprite.S / 2 + 5, 35 * var4 + var2, 6);
            } else if (fD[var0][var5] == 5) {
               InGamePromotion.b(graphics, eG, var6 + BSprite.S / 2 + 5, 35 * var4 + var2, 6);
            }

            var4++;
         }
      }

      aX++;
      a(var0 == 1 ? k(0, 18) : "", k(0, 21));
   }

   private static void g(int var0, int var1, int var2) {
      b(0);
      b(0, 300, 240, 20);
      b[33].a(graphics, 66, 60, 240, 0, 0, 0);
      int var3 = 0;
      if (cR) {
         var3 = stateFrame % 4;
         b[33].a(graphics, 14, var3, 60, 240, 0, 0, 0);
         if (var3 == 3) {
            cR = false;
            cS = true;
            cT = true;
         } else {
            cT = false;
         }
      }

      String var4 = s(var0, cP);
      if (fD[var0][cP] == 6) {
         var4 = var4 + ": " + (el ? k(0, 23) : k(0, 24));
      }

      if (fD[var0][cP] != 5 && fD[var0][cP] != 4) {
         a.z = 0;
      } else {
         a.z = 2;
      }

      a.a(graphics, var4, var1, var2, 3);
      if (fD[var0][cP] == 4) {
         InGamePromotion.a(graphics, eG, var1 + 5, var2 + 20, 3);
      } else if (fD[var0][cP] == 5) {
         InGamePromotion.b(graphics, eG, var1 + 5, var2 + 20, 3);
      }

      a.a(var4);
      boolean var5 = false;
      b[33].a(graphics, 16, cU % (G(4112) ? 2 : 1), 58, 242, 0, 0, 0);
      b[33].a(graphics, 12, cU % (G(8256) ? 2 : 1), 188, 242, 0, 0, 0);
      cU++;
      if (cU > 1) {
         cU = 1;
      }

      aX++;
      eO = true;
      a(var0 == 1 ? k(0, 18) : "", k(0, 21));
      eO = false;
   }

   private static void d(int var0, boolean var1) {
      if (var0 > 0) {
         if (G(var1 ? 4112 : 16388)) {
            I(-1);
            cP += var0 - 1;
            cP %= var0;
            if (fC == 0 || fC == 2) {
               if (var1) {
                  aX = 0;
               }

               if (fD[fC][cP] == 5) {
                  if (!InGamePromotion.a()) {
                     cP += var0 - 2;
                     cP %= var0;
                     return;
                  }

                  if (InGamePromotion.a() && !InGamePromotion.d()) {
                     cP += var0 - 1;
                     cP %= var0;
                     return;
                  }
               }
            }
         } else if (G(var1 ? 8256 : 33024)) {
            if (var1) {
               aX = 0;
            }

            I(-1);
            cP++;
            cP %= var0;
            if (fC == 0 || fC == 2) {
               if (fD[fC][cP] == 4) {
                  if (!InGamePromotion.a()) {
                     cP += 2;
                     cP %= var0;
                     return;
                  }
               } else if (fD[fC][cP] == 5 && InGamePromotion.a() && !InGamePromotion.d()) {
                  cP++;
                  cP %= var0;
               }
            }
         }
      }
   }

   public static final void b(byte[] var0) {
      int var19 = 0;
      var19++;
      short var2;
      cV = new byte[var2 = var0[0]][][];
      cW = new short[var2];
      cY = new int[var2][];

      for (int var3 = 0; var3 < var2; var3++) {
         cW[var3] = (short)((var0[var19] & 255) + ((var0[var19 + 1] & 255) << 8));
         var19 += 2;
         byte var4 = var0[var19++];
         var19 += 2;
         cV[var3] = new byte[var4][];
         cY[var3] = new int[var4];

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
                     var19 += D(var13);
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
            cV[var3][var5] = new byte[var21 + 2];
            cY[var3][var5] = var9 - var6;
            System.arraycopy(var0, var6, cV[var3][var5], 0, var21);
            cV[var3][var5][var21] = (byte)(cY[var3][var5] & 0xFF);
            cV[var3][var5][var21 + 1] = (byte)(cY[var3][var5] >> 8 & 0xFF);
         }
      }

      cX = new short[var2];
      G();
   }

   public static final void G() {
      cZ = false;

      for (int var0 = 0; var0 < cX.length; var0++) {
         cX[var0] = -1;
      }

      for (int var4 = 0; var4 < cW.length; var4++) {
         byte[][] var1 = cV[var4];

         for (int var2 = 0; var2 < var1.length; var2++) {
            byte[] var3 = var1[var2];
            cY[var4][var2] = (var3[var3.length - 2] & 255) + (var3[var3.length - 1] << 8 & 0xFF);
         }
      }
   }

   public static final void H() {
      cV = (byte[][][])null;
      cW = null;
      cX = null;
      cY = (int[][])null;
   }

   public static final void w(int var0) {
      di = false;
      ao = true;
      cX[var0] = 0;
      br = 35;
      N();
      Actor.bb = 0;
      Actor.e(bY);
   }

   public static final void x(int var0) {
      cX[var0] = 0;
   }

   public static final void y(int var0) {
      cX[var0] = -2;
      byte[][] var1 = cV[var0];

      for (int var2 = 0; var2 < var1.length; var2++) {
         byte[] var3 = var1[var2];
         cY[var0][var2] = (var3[var3.length - 2] & 255) + (var3[var3.length - 1] << 8 & 0xFF);
      }
   }

   public static final void z(int var0) {
      cX[var0] = 0;
      byte[][] var1 = cV[var0];

      for (int var2 = 0; var2 < var1.length; var2++) {
         byte[] var3 = var1[var2];
         cY[var0][var2] = (var3[var3.length - 2] & 255) + (var3[var3.length - 1] << 8 & 0xFF);
      }
   }

   public static final void A(int var0) {
      di = true;
      ao = false;
      cX[var0] = -2;
      if (state != 15) {
         br = 0;
      }

      if (!C(var0)) {
         aG = 1;
      }

      if (levelIndex != 9 && levelIndex != 4 && levelIndex != 1) {
         aH = 1;
      }

      db = true;
      dc = false;
      dd = -1;
      w();
      if (bJ != 17 && bJ != 18) {
         s();
      }

      Actor.d(bY);
   }

   public static final int B(int var0) {
      for (int var1 = 0; var1 < cW.length; var1++) {
         if (cW[var1] == var0) {
            return var1;
         }
      }

      return -1;
   }

   public static final void I() {
      if (G(131072) && db) {
         da = true;
         cZ = false;
         N();
      }

      if (!cZ) {
         for (int var0 = 0; var0 < cW.length; var0++) {
            boolean var1 = false;

            for (int var2 = 0; var2 < df; var2++) {
               if (var0 == dg[var2]) {
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

   public static final void J() {
      for (int var0 = 0; var0 < df; var0++) {
         b(dg[var0], true, dh[var0]);
      }
   }

   public static final boolean C(int var0) {
      boolean var1 = false;

      for (int var2 = 0; var2 < df; var2++) {
         if (var0 == dg[var2]) {
            var1 = true;
            break;
         }
      }

      return var1;
   }

   // $VF: renamed from: K () boolean
   public static final boolean hasActiveBackgroundScript() {
      if (cX != null) {
         for (int var0 = 0; var0 < cX.length; var0++) {
            if (cX[var0] >= 0) {
               boolean var1 = false;

               for (int var2 = 0; var2 < df; var2++) {
                  if (var0 == dg[var2]) {
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
      Actor var6 = null;
      if (cX[var0] >= 0) {
         if (Actor.aQ) {
            Actor.bI++;
            if (dl && M()) {
               if (!dm && player.q != null) {
                  if (G(dp[dk])) {
                     dm = true;
                  }

                  e(player.q);
                  player.q = null;
               }

               Actor.as();
            }

            if ((Actor.bI + 1) % Actor.aS != 1) {
               return;
            }
         }

         boolean var7 = true;
         byte[][] var8 = cV[var0];
         short var9 = cX[var0]++;

         for (int var10 = 0; var10 < var8.length; var10++) {
            Actor var11 = null;
            int var13 = 0;
            byte[] var14 = var8[var10];
            if (cY[var0][var10] < var14.length - 2) {
               byte var15 = var14[0];
               boolean var16 = false;
               switch (var15) {
                  case 0:
                  case 1:
                  default:
                     break;
                  case 2:
                     var11 = i((var14[2] & 255) + ((var14[3] & 255) << 8));
                     break;
                  case 3:
                     int var12;
                     short var17;
                     if ((var11 = j(var12 = ((var17 = (short)((var14[2] & 255) + ((var14[3] & 255) << 8))) << 16) + (var0 << 8) + var10)) == null) {
                        player.b(-2, var17, 0, 0, 0, 200, false);
                        var11 = Actor.aU;
                        if (var17 == 31) {
                           var11.c |= 4096;
                        }

                        var11.an = var12;
                        var11.facingLeft = false;
                        var11.c &= -2;
                        g(var11);
                     }
               }

               var13 = cY[var0][var10];
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
                                 } else if (var32 > levelPixelWidth - 240) {
                                    var32 = levelPixelWidth - 240;
                                 }

                                 if (var18 < 0) {
                                    var18 = 0;
                                 } else if (var18 > cB - 320) {
                                    var18 = cB - 320;
                                 }
                              }

                              var19 = var20;
                           }

                           if (var15 == 3 && var9 == 0 && var20 == 0) {
                              var11.positionX = var24;
                              var11.positionY = var25;
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
                              int var35;
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
                              var11.facingLeft = (var11.c & 1) != 0;
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

                           var11.facingLeft = (var11.c & 1) != 0;
                           var13 += 4;
                           break;
                        case 34:
                        case 35:
                        case 36:
                           int var27 = (var14[var13] & 255) + ((var14[var13 + 1] & 255) << 8);
                           var13 += 2;
                           var6 = i(var27);
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
                              i(var3, var4, var5, var26);
                           }
                     }
                  }
               }

               if (var20 == var9) {
                  cY[var0][var10] = var13;
               } else if (var20 < var9) {
                  cY[var0][var10] = var13;
               }

               if (cY[var0][var10] < var14.length - 2) {
                  var7 = false;
               } else if (var15 == 3) {
                  e(var11);
               }

               if (var19 >= 0) {
                  if (var15 == 1) {
                     cameraX = cameraX + (var32 - cameraX) / (var19 - var9 + 1);
                     cameraY = cameraY + (var18 - cameraY) / (var19 - var9 + 1);
                     l();
                  } else {
                     var11.positionX = var11.positionX + (var32 - var11.positionX) / (var19 - var9 + 1);
                     var11.positionY = var11.positionY + (var18 - var11.positionY) / (var19 - var9 + 1);
                     var11.h();
                     if (var11.type == 8 && var11.q != null) {
                        var11.q.ac = var11.positionX + 0;
                        var11.q.ad = var11.positionY + 0;
                        var11.q.positionX = var11.positionX;
                        var11.q.positionY = var11.positionY;
                     }
                  }
               }
            }
         }

         if (var7 || dr) {
            dr = false;
            if (var2 == 0) {
               z(var0);
               return;
            }

            A(var0);
            if (dl) {
               dl = false;
               db = false;
               Actor.as();
               e(player.q);
               player.q = null;
               if (dm) {
                  w(B(dn));
                  Actor.E.as = Actor.E.as - Actor.E.as / 5;
                  if (Actor.E.as <= 0) {
                     Actor.E.as = 1;
                     return;
                  }
               } else {
                  w(B(do_));
                  health = health - health / 3;
               }
            }
         }
      }
   }

   static final void i(int var0, int var1, int var2, int var3) {
      if (var3 != 1) {
         if (var3 == 2) {
            switch (var0) {
               case 0:
                  bs = 160;
                  br = 35;
                  return;
               case 1:
                  br = 35;
                  return;
               case 2:
                  br = 0;
                  return;
               case 3:
                  a(15);
            }
         }
      }
   }

   static final int D(int var0) {
      return dj[var0 - 100];
   }

   static final int a(int var0, byte[] var1, int var2, int var3, int var4, Actor var5) {
      int var7 = D(var0);
      if (var3 != var4) {
         return var7;
      }

      switch (var0) {
         case 100:
            short var32 = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            var2 += 2;
            short var34 = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            var2 += 2;
            short var31 = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            i(var32).c(var34, var31);
            break;
         case 101:
            short var30 = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            dr = true;
            w(B(var30));
            break;
         case 102:
            short var6 = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            var2 += 2;
            byte var8;
            if ((var8 = var1[var2]) == 0) {
               var8 = -1;
            }

            playSound(var6, var8);
            break;
         case 103:
            T();
            break;
         case 104:
            e((short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8)));
            break;
         case 105:
            int var33;
            if ((var33 = var1[var2] & 255) == 0) {
               Actor.as();
            } else {
               Actor.j(var33, 0);
            }
            break;
         case 106:
            int var9 = var1[var2] & 255;
            if (var5.L()) {
               var5.p(var9);
            } else {
               var5.aq = var9;
            }
            break;
         case 107:
            int var10 = (var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8) + ((var1[var2 + 2] & 255) << 16) + ((var1[var2 + 3] & 255) << 24);
            var5.c |= var10;
            if (var10 == 4096) {
               bw = true;
            }
            break;
         case 108:
            int var11 = (var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8) + ((var1[var2 + 2] & 255) << 16) + ((var1[var2 + 3] & 255) << 24);
            var5.c &= ~var11;
            break;
         case 109:
            var5.p.L = true;
            var5.p.K = 136;
            break;
         case 110:
            var5.p.L = false;
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
               var16 = var5.positionX;
               var17 = var5.collisionBounds[1];
            }

            if (dialogText == null) {
               dialogText = k(var12, var13);
               bA = k(0, var14 + 96);
               X = 0;
               bF = var14;
               bG = var15;
               boolean var35 = bF != -1 && bF <= 2;
               boolean var19 = false;
               a.z = 5;
               a.a('}' + dialogText, var35 ? 175 : 180);
               bH = 0;
               bC = var16 - cameraX;
               bD = var17 - cameraY;
               h(5);
            }
            break;
         case 112:
            int var18;
            if ((var18 = var1[var2] & 255) == 255) {
               var5.p.c();
            } else {
               var5.p.f(var18, 10);
            }
            break;
         case 113:
            e(var5);
            break;
         case 114:
            dk = i(0, 5);
            player.c(dq[dk], cameraX + 120, cameraY + 160);
            dn = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            var2 += 2;
            do_ = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            dl = true;
            dm = false;
            break;
         case 115:
            db = false;
            break;
         case 117:
            dc = true;
            dd = var1[var2] & 255;
            Q = var5.positionX;
            R = var5.positionY;
            P = 0;
            de = var1[++var2] & 255;
         case 118:
         default:
            break;
         case 119:
            h(17);
            Y = 0;
            BSprite.N = false;
            break;
         case 120:
            var5.aL = var1[var2] & 255;
            break;
         case 121:
            h(18);
            Y = 0;
      }

      return var7;
   }

   // $VF: renamed from: b (boolean) void
   public static final void accessSaveRecord(boolean var0) {
      try {
         RecordStore var1;
         if ((var1 = RecordStore.openRecordStore("/dvseed.sav", true)).getNumRecords() <= 0) {
            if (!var0) {
               var1.closeRecordStore();
               return;
            }

            var1.addRecord(dt, 0, ds);
         } else if (var0) {
            var1.setRecord(1, dt, 0, ds);
         } else {
            var1.getRecord(1, dt, 0);
            saveLoaded = true;
         }

         var1.closeRecordStore();
      } catch (Exception var2) {
         var2.toString();
      }
   }

   public static final void E(int var0) {
      dv |= var0 & 0xFF;
   }

   private static void aL() {
      a(0, 0, 240, 320);
      if (eE == null) {
         b(0, true);
      }

      eQ.a(graphics, 0, 120, 127, 0, 0, 0);
      a.z = 0;
      if (stateFrame % 10 > 5) {
         a.a(graphics, k(0, 106), 120, 300, 17);
      }

      if (M()) {
         a(2);
      }
   }

   private static int X(int var0) {
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

   private static boolean aM() {
      if (G(dH[dF])) {
         dF++;
         if (dF == dH.length) {
            dF = 0;
            I(-1);
            N();
            dG = true;
            return true;
         }
      } else if (M()) {
         dF = 0;
      }

      return false;
   }

   public static final void L() {
      if (dw != 0) {
         dD = dw;
         dE = 0;
      }

      if (dB != 0) {
         dA = -1;
      }

      dE++;
      if (dz != dy || dz == 0) {
         if (player != null && (player.animationId == 5 || player.animationId == 6) && dA >= 0) {
            if (dA <= 2) {
               Actor.bQ = true;
            } else {
               Actor.bQ = false;
            }
         }

         dA = -1;
      }

      if (dz != 0) {
         dA++;
      }

      dz = dy;
      dw = dx;
      dB = dC;
      dx = 0;
      dC = 0;
   }

   protected final void keyPressed(int var1) {
      o = var1;
      dx = dx | X(var1);
      dz = dz | dx;
      dy = dy | dx;
   }

   protected final void keyReleased(int var1) {
      dC = dC | X(var1);
      dy = dy & ~dC;
      dz = dz & ~dC;
   }

   public static final boolean F(int var0) {
      return (dz & var0) != 0;
   }

   public static final boolean G(int var0) {
      return (dw & var0) != 0;
   }

   public static final boolean H(int var0) {
      return (dw & var0) != 0 && dw == dD && dE < 5;
   }

   public static final boolean M() {
      return dw != 0;
   }

   public static final void N() {
      dy = 0;
      dz = 0;
      dw = 0;
      dB = 0;
      dx = 0;
      dC = 0;
   }

   public static final void O() {
      if (Q()) {
         if (!dQ) {
            for (int var0 = 0; var0 < 20; var0++) {
               dI[var0] = (short)(var0 * 16);
               dM[var0] = (short)(i(0, 100) * 320 / 100);
               dK[var0] = (byte)i(0, 5);
               dL[var0] = (byte)i(0, 5);
               dN[var0] = (byte)(1 + i(0, 4));
               dO[var0] = (byte)(0 + i(0, 5));
               if (dO[var0] > 2) {
                  dP[var0] = 0;
               } else {
                  dP[var0] = 4;
               }

               if (i(0, 2) == 0) {
                  dO[var0] = (byte)(-dO[var0]);
               }
            }

            dQ = true;
         }

         boolean var1 = false;
         if ((levelIndex == 7 || levelIndex == 9) && bo == -2) {
            var1 = true;
         }

         b[44].z = 0;

         for (int var3 = 0; var3 < 20; var3++) {
            if (state != 14 && state != 31) {
               dM[var3] = (short)(dM[var3] - dN[var3]);
               dI[var3] = (short)(dI[var3] + dO[var3]);
               if (i(0, 300) == 0) {
                  byte var2 = dO[var3];
                  dO[var3] = dP[var3];
                  dP[var3] = var2;
               }

               while (dM[var3] < 0) {
                  dM[var3] = (short)(dM[var3] + 320);
               }

               while (dM[var3] > 320) {
                  dM[var3] = (short)(dM[var3] - 320);
               }

               while (dI[var3] > 240) {
                  dI[var3] = (short)(dI[var3] - 240);
               }

               while (dI[var3] < 0) {
                  dI[var3] = (short)(dI[var3] + 240);
               }
            }

            b[44].a(graphics, (var1 ? 12 : 7) + dK[var3], 0, dI[var3], dM[var3], 0, 0, 0);
         }
      }
   }

   public static final void P() {
      if (S() && bJ != 1) {
         if (dS) {
            if (!dQ) {
               for (int var0 = 0; var0 < 30; var0++) {
                  dI[var0] = (short)(var0 * 8);
                  dM[var0] = (short)(i(0, 100) * 320 / 100);
                  dN[var0] = (byte)(10 + i(0, 8));
                  dO[var0] = (byte)(7 + i(0, 6));
                  dJ[var0] = (byte)i(0, 3);
               }

               dQ = true;
            }

            b(-1428431909);

            for (int var3 = 0; var3 < dR; var3++) {
               if (state != 14 && state != 31) {
                  dM[var3] = (short)(dM[var3] + dN[var3]);
                  dI[var3] = (short)(dI[var3] - dO[var3]);

                  while (dM[var3] < 0) {
                     dM[var3] = (short)(dM[var3] + 320);
                  }

                  while (dM[var3] > 320) {
                     dM[var3] = (short)(dM[var3] - 320);
                  }

                  while (dI[var3] > 240) {
                     dI[var3] = (short)(dI[var3] - 240);
                  }

                  while (dI[var3] < 0) {
                     dI[var3] = (short)(dI[var3] + 240);
                  }
               }

               b(dX[dJ[var3]]);
               d(dI[var3], dM[var3], dI[var3] - 7, dM[var3] + 13);
            }

            if (i(0, 500) == 40 && !dT) {
               dT = true;
               dU = 0;
               dV = i(0, 3) == 0;
            }

            if (dT && state != 14) {
               int var2 = dU++;
               if (dV) {
                  b(dW[0]);
                  b(0, 0, 240, 320);
                  dT = false;
                  return;
               }

               if (var2 <= 1) {
                  b(dW[var2]);
                  b(0, 0, 240, 320);
                  return;
               }

               dT = false;
            }
         }
      }
   }

   public static final void a(boolean var0, int var1) {
      b(872414976);
      short var2 = 0;
      int var3 = 0;
      if (var1 == 0) {
         for (int var11 = 0; var11 < 3; var11++) {
            var3 = (ea + var11 * 15) % 180;
            if (var0) {
               if (var3 < 90) {
                  dY = var3 * 70 / 90;
                  var2 = 270;
               } else {
                  dY = (180 - var3) * 70 / 90;
                  var2 = 90;
               }
            } else if (var3 < 90) {
               dY = var3 * 70 / 90;
               var2 = 90;
            } else {
               dY = (180 - var3) * 70 / 90;
               var2 = 270;
            }

            b(ec[var11]);
            c(player.positionX - dY / 2 - cameraX, player.positionY - 30 - 35 - cameraY, dY, 70, var2, 180);
            c(player.positionX - dY / 2 + 1 - cameraX, player.positionY - 30 - 35 - cameraY, dY, 70, var2, 180);
         }

         ea = (ea + 3) % 180;
      } else if (var1 == 1) {
         for (int var10 = 0; var10 < 3; var10++) {
            var3 = (eb + var10 * 15) % 180;
            if (var0) {
               if (var3 < 90) {
                  dZ = var3 * 70 / 90;
                  var2 = 180;
               } else {
                  dZ = (180 - var3) * 70 / 90;
                  var2 = 0;
               }
            } else if (var3 < 90) {
               dZ = var3 * 70 / 90;
               var2 = 0;
            } else {
               dZ = (180 - var3) * 70 / 90;
               var2 = 180;
            }

            b(ec[var10]);
            c(player.positionX - 35 - cameraX, player.positionY - 30 - dZ / 2 - cameraY, 70, dZ, var2, 180);
            c(player.positionX - 35 - cameraX, player.positionY - 30 - dZ / 2 + 1 - cameraY, 70, dZ, var2, 180);
         }

         eb = (eb + 4) % 180;
      } else {
         if (var1 == 888) {
            if (eb == 0 || eb == 180 || m(eb) == 0) {
               eb = (eb + 1) % 45;
               return;
            }

            int var4 = 8960 / m(eb);
            int var5 = 35 * l(eb) * m(45) / m(eb) >> 8;
            System.out.println("r2  " + var4);
            System.out.println("r3  " + var5);
            if (var0) {
               c(player.positionX + var5 - var4 - cameraX, player.positionY - 30 + var5 - var4 - cameraY, var4 * 2, var4 * 2, 135 - eb, eb * 2);
            } else {
               c(player.positionX - var5 - var4 - cameraX, player.positionY - 30 - var5 - var4 - cameraY, var4 * 2, var4 * 2, 315 - eb, eb * 2);
            }

            eb = (eb + 1) % 45;
         }
      }
   }

   public static final boolean Q() {
      return levelIndex == 6 || levelIndex == 9 || levelIndex == 8 || levelIndex == 7;
   }

   public static final boolean R() {
      return levelIndex == 0 || levelIndex == 1;
   }

   public static final boolean S() {
      return levelIndex == 2 || levelIndex == 3;
   }

   private static void t(int var0, int var1) {
      try {
         q(var1, var0);
      } catch (Exception var3) {
      }
   }

   private static void h(int var0, int var1, int var2) {
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

   private static void h(boolean var0) {
      if (ee != 2) {
         if (ei >= 7 && ei <= 9) {
            if (fk != null) {
               a(ed, 0, true);
               e(ed, 0, 2);
            }

            e(ed, 0, 1);
         } else {
            if (ei != 10 && fk != null) {
               a(ed >> 3, 0, true);
            }

            if (ei != 10 && fk != null) {
               e(ed >> 2, 0, 2);
            }

            e(ed, 0, 1);
         }

         if (G(2048) && var0 && dG) {
            fF = !fF;
            if (fF) {
               fE = maxUnlockedLevel;
               maxUnlockedLevel = 11;
            } else {
               maxUnlockedLevel = fE;
            }
         }
      }

      if (!var0 && ee < 2) {
         b(1426063360);
         b(0, 0, 240, 320);
      }

      b(0);
      b(0, 0, 240, 72);
      if (!var0) {
         b(0, 226, 240, 107);
      } else {
         b(0, 193, 240, 140);
      }

      if (eF == null) {
         b(0, false);
      }

      a(eF, 115, 10, 17);
      b[33].a(graphics, !var0 ? 38 : 37, 120, 160, 0, 0, 0);
      if (var0) {
         if (maxUnlockedLevel != 0) {
            b[33].a(graphics, 16, cU % (G(4112) ? 2 : 1), 65, 238, 0, 0, 0);
            b[33].a(graphics, 12, cU % (G(8256) ? 2 : 1), 173, 238, 0, 0, 0);
         }

         b[33].a(graphics, 69, 70, 235, 0, 0, 0);
         if (cR) {
            b[33].a(graphics, 9, stateFrame % 4, 70, 235, 0, 0, 0);
            if (stateFrame % 4 == 3) {
               cR = false;
               cS = true;
               cT = true;
               stateFrame = 0;
            } else {
               cT = false;
            }
         }

         a.z = 5;
         a.a(graphics, k(0, 43) + " " + (ei + 1), 120, 250, 3);
      }

      a.a(graphics, k(0, !var0 ? 41 : 44 + ei), 120, !var0 ? 260 : 283, 3);
      if (ee == 1) {
         h(var0 ? 123 : 162, U, 71);
         U++;
         if (U >= 11) {
            U = 12;
            ee = 2;
            ef = 0;
         }
      } else if (ee == 3) {
         U--;
         h(var0 ? 123 : 162, U, 71);
         if (U <= 0) {
            ee = 0;
         }
      } else if (ee == 2 || ee == 1 && U >= 9) {
         b(0, 71, 240, 123 + (var0 ? 0 : 33));
         Actor.a(120, var0 ? 120 : 145, 120, var0 ? 120 : 145, 40, 35, 3 * stateFrame, true);
         a.z = 5;
         a.a(graphics, k(0, 26), 120, 180 + (var0 ? 0 : 25), 17);
      }

      if (var0) {
         byte var1 = -1;
         int var2 = eg - 11;
         byte[] var3 = ek[var2];
         byte[] var4 = fG[var2];
         if (eh[var2 + 1] - 1 >= maxUnlockedLevel) {
            int var5 = maxUnlockedLevel - eh[var2];
            var1 = var3[var5];
         }

         int var8 = 0;
         if (maxUnlockedLevel != 11 && var2 == 0) {
            var8 = 4;
         }

         for (; var8 < var4.length >> 1 && (var1 <= 0 || var8 < var1); var8++) {
            int var6 = 120 + var4[var8 << 1];
            int var7 = 235 + var4[(var8 << 1) + 1];
            if (fJ != 2 && fH == var8) {
               a(var6 - 2, var7 - 2, 4, 4, true, 15395562, 15480832);
            } else {
               a(var6 - 1, var7 - 1, 2, 2, true, 13079364, 4196354);
            }
         }

         for (int var9 = 0; var9 < var3.length - 1; var9++) {
            b[33].a(graphics, 40, 120 + var4[var3[var9] << 1], 235 + var4[(var3[var9] << 1) + 1], 0, 0, 0);
         }

         if (fJ == 2) {
            b[33].a(graphics, 2, ed % 8, 120 + ej[ei << 1], 239 + ej[(ei << 1) + 1] - 4, 0, 0, 0);
         }
      }

      if (ee == 0 || !var0) {
         ed++;
      }
   }

   private static void aN() {
      int var0 = 0;
      int var1 = eg - 11;

      for (int var2 = 0; var2 < var1; var2++) {
         if (var2 != 0 && var2 != 2) {
            var0 += 3;
         } else {
            var0 += 2;
         }
      }

      int var3 = ei - var0;
      fH = ek[var1][var3];
      if (var3 == 0 && fK) {
         fI = 0;
      } else if (fK) {
         fI = ek[var1][var3 - 1];
      } else {
         fI = ek[var1][var3 + 1];
      }
   }

   private static void i(boolean var0) {
      if (var0) {
         ei--;
         if (ei < eh[eg - 11]) {
            eg--;
            if (eg < 11) {
               eg = 15;
            }

            ee = 1;
            if (ei < 0) {
               if (maxUnlockedLevel == 11) {
                  ei = 10;
                  return;
               }

               eg = 11;
               ei = 0;
               ee = 0;
               return;
            }
         }
      } else {
         ei++;
         if (ei >= eh[eg - 11 + 1]) {
            eg++;
            if (eg > 15) {
               eg = 11;
            }

            ee = 1;
            if (ei >= 11) {
               if (maxUnlockedLevel == 11) {
                  ei = 0;
                  return;
               }

               eg = 15;
               ei = 10;
               ee = 0;
            }
         }
      }
   }

   private static void aO() {
      h(true);
      if (ee != 2) {
         a(k(0, 18), k(0, 17));
      }

      if (fJ != 2) {
         if (fK) {
            fH--;
         } else {
            fH++;
         }

         if (fH == fI) {
            fJ++;
            if (fJ == 1) {
               if (fK) {
                  i(true);
               } else {
                  i(false);
               }

               if (ee == 1) {
                  if (fK) {
                     int var0 = ek[eg - 11].length;
                     fI = ek[eg - 11][var0 - 2];
                     fH = ek[eg - 11][var0 - 1];
                  } else {
                     fH = 0;
                     fI = ek[eg - 11][0];
                  }
               } else {
                  fJ = 2;
               }
            }
         }
      }

      if (fJ != 0) {
         if (ee == 0) {
            if (G(8256)) {
               if (maxUnlockedLevel <= ei) {
                  return;
               }

               fJ = 0;
               fK = false;
               aN();
            } else if (G(4112)) {
               if (ei == 0 && maxUnlockedLevel != 11) {
                  return;
               }

               fK = true;
               fJ = 0;
               aN();
            }

            if (G(327712)) {
               I(-1);
               if (cT) {
                  cR = true;
                  stateFrame = -1;
               }
            } else if (cS) {
               cS = false;
               levelIndex = ei;
               a(9);
               Actor.bM = false;
               a(dt, 772, (int)(byte)(Actor.bM ? 1 : 0));
            } else if (G(131072)) {
               if (cT) {
                  aR = true;
                  stateFrame = -1;
               }
            } else if (aP) {
               aP = false;
               eE = null;
               eQ = null;
               a(2);
            }
         }

         aP();
      }
   }

   private static void aP() {
      if (ee == 2) {
         try {
            if (ef >= 3) {
               t(eg, ef - 3);
            }

            if (ef == 11) {
               c(eg, false);
            }
         } catch (Exception var1) {
            var1.printStackTrace();
         }

         if (ef == 12) {
            ed = 0;
            ee = 3;
            ef = 0;
            return;
         }

         ef++;
      }
   }

   private static void aQ() {
      a(0, 0, 240, 320);
      if (state == 6) {
         g(0, 0, 240, 320);
      } else {
         h(false);
         aP();
      }

      if (state == 6) {
         a(0, 10, 240, 280);
      } else {
         a(0, 80, 240, 165);
      }

      String var0;
      if (state == 20) {
         var0 = k(0, 42);
      } else {
         var0 = k(0, 28);
      }

      a.z = state != 6 ? 0 : 5;
      if (ee == 0) {
         if (a.a(graphics, var0, 120, fL, 17, state == 20 ? 180 : 180, 0, -1) < 0) {
            fL = state == 20 ? 240 : 280;
         } else {
            fL--;
         }
      }

      if (previousState == 8) {
         a(k(0, 20), "");
      } else {
         a(k(0, 18), state == 6 ? "" : k(0, 17));
      }

      if (G(262144) && cT) {
         aS = true;
         stateFrame = -1;
      }

      if (aQ && state == 20) {
         aQ = false;
         a(19);
         eg = 11;
      }

      if (F(65568) || F(33024)) {
         fL -= 4;
      }

      if (G(131072)) {
         if (cT) {
            aR = true;
            stateFrame = -1;
            return;
         }
      } else if (aP) {
         aP = false;
         eE = null;
         eQ = null;
         a(2);
         eg = 11;
      }
   }

   private static void aR() {
      try {
         a("/24");

         for (int var2 = 0; var2 < 18; var2++) {
            byte[] var1;
            if ((var1 = q(var2)) != null) {
               if (var2 >= fQ) {
                  byte[] var0 = c(var1);
                  fR[var2] = var0;
               } else {
                  fR[var2] = var1;
               }
            }
         }

         B();
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   // $VF: renamed from: o (int, int) void
   public static final void playSound(int var0, int var1) {
      if (var0 != -1 && el) {
         if (fR[var0] != null) {
            if (var0 != fN || fM == null || fM.getState() != 400) {
               switch (var0) {
                  case 0:
                  case 1:
                  case 2:
                  case 3:
                  case 7:
                     fN = var0;
                  case 4:
                  case 5:
                  case 6:
                  default:
                     T();

                     try {
                        ByteArrayInputStream var2;
                        (var2 = new ByteArrayInputStream(fR[var0])).reset();
                        if (var0 < fQ) {
                           fM = Manager.createPlayer(var2, "audio/midi");
                        } else {
                           fM = Manager.createPlayer(var2, "audio/x-wav");
                        }

                        fM.realize();
                        var2.close();
                     } catch (Exception var6) {
                        var6.printStackTrace();
                     }

                     Player var3 = fM;
                     if (fM != null) {
                        try {
                           var3.setLoopCount(var1);
                           var3.start();
                           resumeSoundId = -1;
                        } catch (Exception var5) {
                           ErrorReporter.a(var5, "play");
                           var5.printStackTrace();
                        }
                     }
               }
            }
         }
      }
   }

   public static final void I(int var0) {
      playSound(var0, var0 == 0 ? -1 : 1);
   }

   public static final void T() {
      try {
         if (fM != null) {
            if (fM.getState() == 400) {
               fM.stop();
            }

            if (fM.getState() == 300) {
               fM.deallocate();
            }

            fM.close();
            fM = null;
         }
      } catch (Exception var1) {
      }
   }

   static final void a(byte[] var0, int var1, byte[] var2, int var3) {
      for (int var4 = 0; var4 < var1; var4++) {
         var2[var3++] = var0[var4];
      }
   }

   static final byte[] J(int var0) {
      int var2;
      byte[] var1 = new byte[var2 = var0 + 44];
      a(eo, 4, var1, 0);
      var2 -= 8;
      var1[4] = (byte)var2;
      var1[5] = (byte)(var2 >> 8);
      var1[6] = (byte)(var2 >> 16);
      var1[7] = (byte)(var2 >> 24);
      a(ep, 4, var1, 8);
      a(eq, 4, var1, 12);
      a(es, 4, var1, 16);
      a(et, 2, var1, 20);
      a(eu, 2, var1, 22);
      a(ev, 4, var1, 24);
      a(ew, 4, var1, 28);
      a(ex, 2, var1, 32);
      a(ey, 2, var1, 34);
      a(er, 4, var1, 36);
      var2 = var0;
      var1[40] = (byte)var2;
      var1[41] = (byte)(var2 >> 8);
      var1[42] = (byte)(var2 >> 16);
      var1[43] = (byte)(var2 >> 24);
      return var1;
   }

   static final void a(byte[] var0, byte[] var1, int var2, int var3) {
      int var7 = var0[var2] & 255 | var0[var2 + 1] << 8;
      int var9 = var0[var2 + 2];
      var1[var3++] = (byte)(var7 & 0xFF);
      var1[var3++] = (byte)(var7 >> 8);
      var2 += 4;
      boolean var11 = false;
      byte var10 = 0;
      short var6 = en[var9];

      for (int var12 = 504; var12 > 0; var12--) {
         int var5;
         if (var11) {
            var5 = var10 & 15;
         } else {
            var5 = (var10 = var0[var2++]) >> 4 & 15;
         }

         var11 = !var11;
         if ((var9 += em[var5]) < 0) {
            var9 = 0;
         }

         if (var9 > 88) {
            var9 = 88;
         }

         int var4 = var5 & 8;
         var5 &= 7;
         int var8 = var6 >> 3;
         if ((var5 & 4) != 0) {
            var8 += var6;
         }

         if ((var5 & 2) != 0) {
            var8 += var6 >> 1;
         }

         if ((var5 & 1) != 0) {
            var8 += var6 >> 2;
         }

         if (var4 != 0) {
            var7 -= var8;
         } else {
            var7 += var8;
         }

         if (var7 > 32767) {
            var7 = 32767;
         } else if (var7 < -32768) {
            var7 = -32768;
         }

         var6 = en[var9];
         var1[var3++] = (byte)(var7 & 0xFF);
         var1[var3++] = (byte)(var7 >> 8);
      }
   }

   static final byte[] c(byte[] var0) {
      int var7 = 0;
      int var8 = e(var0, 16);
      var7 = 16 + var8 + 4;
      var7 += 4;
      var8 = e(var0, var7);
      int var12;
      int var2 = (var12 = var7 + var8 + 4) + 8;
      var7 = var12 + 4;
      int var6 = e(var0, var7) >> 8;
      int var5;
      byte[] var1 = J(var5 = 1010 * var6);
      int var4 = 44;

      for (int var9 = 0; var9 < var6; var9++) {
         a(var0, var1, var2, var4);
         var2 += 256;
         var4 += 1010;
      }

      return var1;
   }

   static final int e(byte[] var0, int var1) {
      return var0[var1] & 0xFF | (var0[var1 + 1] & 0xFF) << 8 | (var0[var1 + 2] & 0xFF) << 16 | (var0[var1 + 3] & 0xFF) << 24;
   }

   public static final int K(int var0) {
      if (Actor.E != null) {
         return 7;
      }

      switch (var0) {
         case -1:
            return 0;
         case 0:
         case 1:
         case 5:
         case 6:
            return 1;
         case 2:
         case 3:
         case 4:
         case 16:
            return 2;
         case 7:
         case 8:
         case 9:
            return 3;
         case 10:
            return 7;
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         default:
            return -1;
      }
   }

   public static final void U() {
      if (fN == 7) {
         playSound(7, -1);
      } else {
         playSound(K(levelIndex), -1);
      }
   }

   static final void V() {
      ez = new Image[11];
      if (levelIndex <= 1 || levelIndex <= 6 && levelIndex >= 5) {
         int[] var0 = new int[4114];
         a(0, 0, 34, 74, 128, 6, 38, 53, var0, 34, 40959);
         ez[0] = Image.createRGBImage(var0, 34, 121, true);
         a(0, 0, 34, 74, 128, 6, 38, 53, var0, 34, 3325687);
         ez[2] = Image.createRGBImage(var0, 34, 121, true);
         var0 = new int[1533];
         a(0, 0, 21, 49, 81, 3, 26, 30, var0, 21, 40959);
         ez[1] = Image.createRGBImage(var0, 21, 73, true);
         a(0, 0, 21, 49, 81, 3, 26, 30, var0, 21, 3325687);
         ez[3] = Image.createRGBImage(var0, 21, 73, true);
      }

      if (levelIndex > 1 && levelIndex < 4 || levelIndex >= 5 && levelIndex < 9) {
         int[] var2 = new int[2040];
         a(0, 0, 0, 101, 0, 250, 20, 0, var2, 20, 127993);
         ez[4] = Image.createRGBImage(var2, 20, 102, true);
         var2 = new int[1632];
         a(0, 0, 0, 101, 0, 250, 16, 0, var2, 16, 127993);
         ez[5] = Image.createRGBImage(var2, 16, 102, true);
         var2 = new int[1224];
         a(0, 0, 0, 101, 0, 250, 12, 0, var2, 12, 127993);
         ez[6] = Image.createRGBImage(var2, 12, 102, true);
         var2 = new int[918];
         a(0, 0, 0, 101, 0, 250, 9, 0, var2, 9, 127993);
         ez[7] = Image.createRGBImage(var2, 9, 102, true);
         var2 = new int[612];
         a(0, 0, 0, 101, 0, 250, 6, 0, var2, 6, 127993);
         ez[8] = Image.createRGBImage(var2, 6, 102, true);
         var2 = new int[408];
         a(0, 0, 0, 101, 0, 250, 4, 0, var2, 4, 127993);
         ez[9] = Image.createRGBImage(var2, 4, 102, true);
         var2 = new int[204];
         a(0, 0, 0, 101, 0, 250, 2, 0, var2, 2, 127993);
         ez[10] = Image.createRGBImage(var2, 2, 102, true);
      }
   }

   static final void a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int[] var8, int var9, int var10) {
      int var11 = var2 - var0;
      int var12 = var3 - var1;
      int var13 = var7 - var6;
      if (var11 == 0) {
         int var14 = var1 * var9 + var0;
         var4 <<= 8;
         int var26;
         int var15 = ((var26 = var5 << 8) - var4) / var12;

         for (int var16 = 0; var16 < var12; var16++) {
            int var17 = var14;

            int var18;
            for (var18 = 0; var18 < 4; var18++) {
               int var19 = (var4 / 5 * (var18 + 1) & 0xFF00) << 16 | var10;
               var8[var17] = var19;
               var17++;
            }

            int var34 = (var4 & 0xFF00) << 16 | var10;

            while (var18 < var6 - 4) {
               var8[var17] = var34;
               var17++;
               var18++;
            }

            while (var18 < var6) {
               var34 = (var4 / 5 * (var6 - var18) & 0xFF00) << 16 | var10;
               var8[var17] = var34;
               var17++;
               var18++;
            }

            if (var16 > var12 * 2 / 3) {
               var4 -= 2 * var15;
            } else {
               var4 += var15;
            }

            var14 += var9;
         }
      } else {
         int var28 = (var12 << 8) / var11;
         int var29 = (var13 << 8) / var11;
         int var30 = var1 * var9 + var0;
         boolean var31 = false;
         var4 <<= 8;
         int var27;
         int var33 = ((var27 = var5 << 8) - var4) / var11;

         for (int var36 = 0; var36 < var11; var36++) {
            int var20 = var4;
            if (var36 == 0) {
               var20 = var27;
            }

            int var21 = var30;
            int var22 = var20 << 16 & 0xFF000000 | var10;
            int var23 = 0;

            for (int var32 = (var6 << 8) + var29 * var36 >> 8; var23 < var32; var23++) {
               var8[var21] = var22;
               var21 += var9;
            }

            var4 += var33;
            var30 = var0 + var36 + ((var1 << 8) + var28 * var36 >> 8) * var9;
         }
      }
   }

   static final int p(int var0, int var1) {
      return (int)((long)var0 * var1 + 128L >> 8);
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
      int[] var10000 = new int[]{16724804, 3453695};
      String[] var0 = new String[]{"Power", "COMBO", "CRITICAL", "MAGIC"};
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
      var10000 = new int[]{16317688, 16317688, 16317504, 16294936, 16252928};
      var10000 = new int[]{0, 4, 8, 13};
      short[] var5 = new short[]{2, 19, 36, 53, 70, 87, 104, 121, 138, 155, 172, 176};
      var5 = new short[]{34, 15, 76, 73, 147, 86, 165, 29, 218, 93, 117, 153, 36, 127, 58, 196, 184, 213, 208, 156, 48, 300, 135, 273};
      byte[] var7 = new byte[]{40, 4, 40, 4, 40, 4, 40, 4};
      String[] var8 = new String[]{"/3"};
      var7 = new byte[]{3, 5, 7, 8, 10};
      byte[][] var10 = new byte[][]{{44, 107, 68, 107, 26}, {34, 93, 52, 93, 26}};
   }
}
