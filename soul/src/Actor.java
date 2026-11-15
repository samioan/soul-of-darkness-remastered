import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

// $VF: renamed from: b
public final class Actor {
   public int a;
   public int b;
   public int c;
   public static int d = -1;
   public static int e = -1;
   // $VF: renamed from: f int
   public int animationId = -1;
   public int g = -1;
   public int h;
   public int i;
   // $VF: renamed from: j int[]
   public int[] collisionBounds;
   public int[] k;
   public int[] l;
   public int[] m;
   public int[] n;
   public boolean o;
   public BSprite p;
   public Actor q;
   public Actor r;
   public Actor s;
   public Actor t;
   public Actor u;
   public Actor v;
   public Actor w;
   public Actor x;
   public int y;
   public int z;
   public int A;
   public int B;
   public int C;
   public int D;
   public static Actor E;
   public static int F;
   public int G;
   public int H;
   public int I;
   public int J;
   // $VF: renamed from: K int
   public int positionX;
   // $VF: renamed from: L int
   public int positionY;
   public boolean M;
   public boolean N;
   public boolean O;
   public boolean P;
   public boolean Q;
   public boolean R;
   public boolean S;
   public Actor T;
   public Actor U;
   public static boolean V;
   public static boolean W;
   public static int X;
   public static int Y;
   public static int Z;
   public static int aa;
   public static int ab;
   public int ac = 0;
   public int ad = 0;
   public boolean ae = false;
   public int af;
   public int ag;
   public static Hashtable ah = new Hashtable();
   public static int[][] ai = new int[200][2];
   public static int aj;
   public int ak = 10;
   public int al = 1;
   // $VF: renamed from: am boolean
   public boolean facingLeft;
   public int an;
   // $VF: renamed from: ao int
   public int type;
   public int ap;
   public int aq;
   public static int ar;
   public int as;
   public boolean at = false;
   public int au;
   public int av;
   public int aw;
   public int ax;
   public int ay;
   public int az;
   public int aA;
   public int aB;
   public int aC;
   public int aD;
   public static boolean aE;
   public int aF;
   public int aG;
   public int aH;
   public int aI;
   public int[] aJ;
   public int aK;
   public int aL;
   public static boolean aM;
   public static boolean aN;
   public static Actor aO;
   public static boolean aP;
   public static boolean aQ;
   public static int aR;
   public static int aS;
   private static int[] ey = new int[4];
   public static Actor aT;
   public static Actor aU;
   public static Actor[] aV;
   public static int aW;
   public static Actor[] aX;
   public static Actor[] aY;
   public static Actor[] aZ;
   public static Actor[] ba = new Actor[4];
   public static int bb;
   static int bc;
   static final int[] bd = new int[]{16, 17, 18, 19, 20, 65568, 65568, 65568, 65568, 65568};
   static final int[] be = new int[]{21, 22, 65568, 65568};
   static final int[] bf = new int[]{26, 27, 28, 29, 65568, 65568, 65568, 65568};
   static final int[] bg = new int[]{16, 17, 16, 17, 21};
   static final int[] bh = new int[]{1, -20, 0, 200};
   static final int[] bi = new int[]{32, 0, 0, 300, 1, 0, 12, 10, 32, 0, 0, 300, 1, 0, 12, 10, 32, 0, 0, 300, 1, 0, 0, 1000};
   public static boolean bj;
   public static int bk;
   public static int[] bl;
   public static boolean bm;
   public static int bn;
   public static int[] bo;
   public static int bp;
   public static boolean bq;
   public static boolean br;
   public static final byte[] bs = new byte[]{
      0,
      1,
      2,
      2,
      4,
      5,
      -1,
      -1,
      6,
      7,
      8,
      9,
      -1,
      10,
      12,
      40,
      -1,
      -1,
      -1,
      -1,
      -1,
      16,
      -1,
      -1,
      22,
      20,
      -1,
      -1,
      21,
      -1,
      34,
      24,
      24,
      24,
      24,
      31,
      24,
      25,
      24,
      24,
      -1,
      -1,
      -1,
      34,
      24,
      34,
      39,
      34,
      23,
      -1,
      24,
      28,
      32,
      24,
      27,
      32,
      19,
      34,
      26,
      -1,
      24,
      38,
      -1,
      39,
      39,
      39,
      39,
      -1,
      -1,
      41,
      43,
      23,
      24,
      44,
      -1,
      17,
      44,
      -1,
      -1,
      45,
      17,
      46,
      24,
      47,
      -1,
      -1,
      17,
      -1,
      46,
      24,
      13,
      17,
      4,
      -1,
      -1,
      24,
      51,
      -1,
      19,
      48,
      13,
      -1,
      -1,
      -1,
      -1,
      -1
   };
   public static Graphics bt;
   public static boolean bu;
   public static boolean bv;
   public static boolean bw;
   public static boolean bx;
   public static boolean by;
   public static boolean bz;
   public static boolean bA;
   public static boolean bB;
   public static boolean bC;
   public static boolean bD;
   public static int bE;
   public static boolean bF;
   public static short[] bG = new short[]{-1, -1, -1};
   public static byte[] bH = new byte[54];
   public static int bI;
   public static int bJ;
   private static boolean ez = false;
   private int eA;
   private int eB;
   static int bK;
   private int eC;
   private int eD;
   private int eE;
   private int eF;
   private int eG;
   private int eH;
   private int eI;
   private int eJ;
   private int eK;
   public boolean bL;
   public static boolean bM;
   public boolean bN;
   private boolean eL;
   private boolean eM;
   private boolean eN;
   private static int eO = 1000;
   public static int bO;
   public static boolean bP;
   private static int eP;
   private static int eQ;
   private static int eR;
   private static boolean eS;
   private static boolean eT;
   private static boolean eU;
   static boolean bQ;
   private static int eV;
   private static boolean eW;
   public static boolean bR = false;
   public static boolean bS = false;
   public static boolean bT;
   public static final int[] bU = new int[]{0, 0, 0, 0, 0};
   public static final int[] bV = new int[]{0, 20, 0, 0, 0};
   public static final int[] bW = new int[]{14, 14, 0, 0, 0, 0};
   public static final int[] bX = new int[]{6, 6, 6, 6, 3, 3, 3};
   public static final int[] bY = new int[]{0, 9, 9, 9, 9, 9, 9, 9, 12, 18, 12, 15};
   public static final int[] bZ = new int[]{0, 0, 0, 0, 0, 0, 0};
   public static final int[] ca = new int[]{0, 0, 0, 0, 0, 0};
   public static final int[] cb = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
   public static final int[] cc = new int[]{0, 0, 0, 0, 0, 0, 0};
   public static final int[] cd = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
   public static final int[] ce = new int[]{0, 0, 0, 0, 0, 0, 0, 5, 5, 8, 4, 6, 12, 4, 1, 1, 0};
   public static final int[] cf = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, -5, -11, -16};
   public static int[][] cg = new int[][]{bU, bV, bW, bX, bY, bZ, ca, cb, cc, cd, ce, cf};
   public static int ch;
   public static final int[] ci = new int[]{15, 20, 25, 15, 20, 25};
   public static int[] cj = new int[]{0, 0};
   public static int[] ck = new int[]{1, 0};
   public static int[] cl = new int[]{0, 0};
   public static int[] cm = new int[]{0, 0};
   public static int[] cn = new int[]{6, 9, 9};
   public static final int[] co = new int[]{15, 20, 25};
   public static final int[] cp = new int[]{15, 19, 25, 32};
   public static final int[] cq = new int[]{10, 13, 15, 20};
   public static final int[] cr = new int[]{10, 13, 15, 20};
   public static int cs = 0;
   public static int ct = 1;
   public static int cu = 2;
   static int cv = -1;
   static int cw = 50;
   public static int cx;
   public static int cy = 0;
   public static int cz = 1;
   public static int cA = 0;
   public static int cB = 0;
   public static int cC = 0;
   public static int cD = 0;
   public static int cE = 0;
   static int cF;
   static int cG;
   static int cH = 0;
   public boolean cI = false;
   public Actor cJ;
   public static boolean cK = false;
   public static int cL;
   public static final int[] cM = new int[]{0, 80, 10, 10};
   public static final int[] cN = new int[]{15, 10, -15, 0};
   public static final int[] cO = new int[]{0, 20, 10, 10};
   public static final int[] cP = new int[]{0, 50, 10, 10};
   public static final int[] cQ = new int[]{2, 3, 0, 0};
   public static final int[] cR = new int[]{-5, 240, 15, 0};
   public static final int[] cS = new int[]{0, 90, 60, 60};
   public static final int[] cT = new int[]{-65, 50, 15, 15};
   private int eX;
   public static int[] cU = new int[2];
   public static int cV;
   public static int cW;
   public static int cX;
   public static int cY = 8;
   public static int cZ = 30;
   public static int da = 30;
   public static int db = 10;
   public static int[] dc = new int[db];
   public static int[] dd = new int[db];
   private static final int[] eY = new int[]{16714752, 16736768, 16755968, 16777215, 16755968, 16736768, 16714752};
   private static final int[] eZ = new int[]{16714752, 16736768, 16777215, 16736768, 16714752};
   public static boolean de;
   public static boolean df;
   public static int dg;
   public static int dh;
   public static boolean di;
   public static int dj;
   public static Actor dk;
   public static int dl;
   public static boolean dm;
   public static int dn;
   // $VF: renamed from: do boolean
   public static boolean do_ = false;
   public static Actor[] dp = new Actor[11];
   public static Vector[] dq = new Vector[10];
   public static int[] dr = new int[11];
   public static int[] ds = new int[11];
   private static Actor[] fa = new Actor[11];
   public static int dt;
   public static int du;
   public static int dv;
   public static int dw;
   public static byte[] dx = new byte[220];
   public static byte[] dy = new byte[28];
   private static Actor[] fb = new Actor[7];
   private static int[] fc = new int[11];
   public static int dz;
   public static int dA;
   public static int dB;
   public static int dC;
   public static int dD;
   public static boolean dE;
   public static boolean dF;
   public static int dG;
   private static int fd;
   private static int fe;
   private static int ff;
   public static int dH;
   public static int dI;
   public static int dJ;
   public static int dK;
   public static Actor dL;
   public static Actor dM;
   private static boolean fg = false;
   private static int fh;
   private static int fi;
   private static int fj;
   private static int fk;
   private static int fl;
   private static int fm;
   private static int fn;
   public static int dN;
   private static int fo;
   public static int dO;
   public static int dP;
   private static int fp;
   private static int fq;
   private static int fr;
   private static int fs = 0;
   public static int[] dQ = new int[3];
   public static int dR;
   public static int dS = 0;
   public static int dT;
   public static Actor[] dU = new Actor[3];
   public static int[] dV = new int[4];
   public static int dW = 0;
   public static String dX;
   public static int dY = 0;
   public static boolean dZ = false;
   public static boolean ea = false;
   public static int eb = 0;
   public static int ec = 0;
   public static int ed = 0;
   static int ee = 1;
   static int ef = 2;
   static int eg = 3;
   static int[] eh = new int[]{1, 2, 3, 5};
   private boolean ft = false;
   public static final int[] ei = new int[]{10, 20, 30, 40, 1};
   public static byte[] ej = new byte[3];
   public static final byte[] ek = new byte[]{70, 14, 15};
   public static final byte[] el = new byte[]{7, 1, 6};
   public static final int[] em = new int[]{39, 38, 40};
   public static boolean en;
   public static int eo;
   public static Actor ep;
   public static int eq;
   public static Actor[] er = new Actor[3];
   private int fu;
   private int fv;
   private int fw;
   private boolean fx;
   static int es = 0;
   static int et = 1;
   public static int eu = -1;
   public static int ev = -1;
   public static int ew = 0;
   static final int[] ex = new int[]{
      0,
      1,
      2,
      3,
      0,
      2,
      4,
      0,
      1,
      2,
      3,
      0,
      0,
      2,
      1,
      0,
      0,
      2,
      0,
      2,
      4,
      6,
      0,
      2,
      5,
      6,
      8,
      2,
      0,
      0,
      2,
      0,
      1,
      0,
      2,
      4,
      4,
      22,
      24,
      26,
      20,
      28,
      30,
      0,
      2,
      6,
      4,
      5,
      10,
      12
   };

   final void a() {
      this.collisionBounds = null;
      this.k = null;
      this.l = null;
      this.q = null;
      this.r = null;
   }

   public final void b() {
      this.c |= 128;
      this.aq = 106;
      this.aD = 700;
   }

   public final void a(boolean var1) {
      int var2 = Game.health * 100 / Game.maxHealth;
      int var3 = 10000 + var2 * var2 - (var2 * 1500 >> 3);
      if (Game.i(0, 10000) < var3) {
         this.a(35, 31, 0, Game.i(-40, 40), Game.i(-20, -100), 308, false);
         aU.b();
      }

      if (Game.magic <= Game.maxMagic * 40 / 100 && bD) {
         var2 = 100 - Game.magic * 60 / Game.maxMagic;
         if (Game.i(0, 100) < var2) {
            this.a(35, 31, 2, Game.i(-40, 40), Game.i(-20, -100), 308, false);
            aU.b();
         }
      }

      var2 = Game.i(1, 3);

      for (int var6 = 0; var6 < a(this.B(0) * 10, 128) + var2; var6++) {
         this.a(35, 31, var1 ? 5 : 4, Game.i(-40, 40), Game.i(-20, -100), 308, false);
         aU.b();
      }
   }

   public static final int a(int var0, int var1) {
      return var0 / var1 + (var0 % var1 * 10 / var1 >= 5 ? 1 : 0);
   }

   public final boolean c() {
      switch (this.aq) {
         case 5:
            this.cV();
            if (this.aA <= 800) {
               this.aq = 6;
            }

            return true;
         case 6:
            if (this.type == 14) {
               this.I();
            }

            this.a(39, 17, 57, 0, 0, 302, false);
            aU.positionY = aU.positionY - (this.collisionBounds[3] - this.collisionBounds[1] >> 1);
            this.aq = 7;
            return true;
         case 7:
            if (this.type == 14) {
               this.I();
            }

            this.cV();
            if (this.aA <= 0) {
               if (this.m != null) {
                  System.arraycopy(Game.player.collisionBounds, 0, this.m, 0, 4);
               }

               this.p(0);
               this.c &= -129;
            }

            return true;
         default:
            return false;
      }
   }

   final void b(boolean var1) {
      this.a(var1, false);
   }

   final void a(boolean var1, boolean var2) {
      if (var1) {
         this.a(var2);
      }

      this.O = false;
      this.T = null;
      this.U = null;
      boolean var3 = false;
      int var4 = 0;
      if (var1) {
         this.fw--;
         var4 = this.fw;
         if (this.fw < -1) {
            var4 = -1;
         }
      }

      if (((this.c & 4) != 0 || var4 >= 0) && var1) {
         var3 = true;
      }

      this.aq = 0;
      if (this.type == 31 && this.aC == 9) {
         this.a(this.collisionBounds, true);
      }

      this.G = 0;
      this.H = 0;
      this.I = 0;
      this.J = 0;
      boolean var5 = false;
      if ((this.c & 512) != 0) {
         var5 = true;
      }

      if (this.an >= 0 && this.ao()) {
         int[] var6;
         if ((var6 = (int[])ah.get(new Integer(this.an))) != null) {
            int var7 = var6[0];

            for (int var8 = 0; var8 < var6[1]; var8++) {
               Game.cr[var8] = (short)((Game.cs[var7++] & 255) + (Game.cs[var7++] << 8));
            }

            this.a(Game.cr, false, var6[0], var6[1]);
            if (var3) {
               this.c &= -33;
               if (this.type == 70 && (this.aH == 0 || this.aH == 1)) {
                  this.aG();
               }
            }

            if (var1) {
               this.fw = var4;
            }
         }
      } else {
         if (var5) {
            this.c |= 512;
         }

         Game.e(this);
      }

      if (var5) {
         this.c |= 512;
      }

      this.c |= 1024;
      if ((this.c & 2048) != 0 && var1 && !var3) {
         Game.e(this);
      }

      if (var3) {
         this.d();
      }
   }

   public final void d() {
      this.aq = 5;
      this.c &= -1025;
      this.c |= 128;
      this.aA = 1500;
   }

   final void a(Actor var1) {
      if (this.q != null) {
         this.q = null;
      }

      if (var1 != null) {
         this.q = var1;
      }
   }

   final void b(Actor var1) {
      if (this.r != null) {
         this.r.c &= -257;
         this.r = null;
      }

      if (var1 != null) {
         this.r = var1;
         var1.c |= 256;
      }
   }

   final void a(int var1, boolean var2) {
      if (var1 == this.animationId && var2) {
         if (this.type == 0) {
            d = this.animationId;
         }

         if (this.animationId == -1) {
            this.animationId = var1;
            this.h = 0;
            this.i = 0;
         } else {
            this.g = var1;
         }

         this.c &= -65;
         this.o = true;
      }

      this.a(var1);
   }

   final void a(int var1) {
      if (var1 >= 0 && (this.p == null || var1 < this.p.m.length)) {
         if (this.type == 0 && var1 == 7) {
            eP = this.positionY;
         }

         if (var1 != this.animationId) {
            if (this.type == 0) {
               d = this.animationId;
            }

            if (this.animationId == -1) {
               this.animationId = var1;
               this.h = 0;
               this.i = 0;
            } else {
               this.g = var1;
            }

            this.c &= -65;
            this.o = true;
         }
      } else {
         System.out.println("ERROR: Anim out of range ! at UID:" + this.an + ",AType:" + this.type);
      }
   }

   final void b(int var1) {
      if (var1 != this.animationId) {
         if (this.type == 0) {
            d = this.animationId;
         }

         this.animationId = var1;
         this.h = 0;
         this.i = 0;
         this.g = -1;
         this.c &= -65;
      }
   }

   final boolean e() {
      int var1;
      return this.h != this.p.a(this.animationId) - 1 ? false : (var1 = this.p.a(this.animationId, this.h, this.f())) <= 1 || this.i == var1 - 1;
   }

   public final int f() {
      if (this.type == 0) {
         return 0;
      } else {
         return Game.cb == this ? 0 : 1;
      }
   }

   final void b(int var1, int var2) {
      if (this.p != null) {
         try {
            int var3 = this.positionX;
            int var4 = this.positionY;
            if (!Game.bi) {
               int var5 = Game.stateFrame % 2 == 0 ? 1 : -1;
               if (this.type == 37 && this.aA > 50 && this.aA < 1000 && this.animationId == 1
                  || this.type == 35 && this.animationId == 7
                  || this.type == 75 && this.aq == 109) {
                  var3 += var5;
               }

               if (this.type == 95 && this.aq == 101) {
                  if (this.av % 2 == 0) {
                     var3 += var5;
                  } else {
                     var4 += var5;
                  }
               }
            }

            if (this.i >= 0) {
               this.p.a(bt, this.animationId, this.h, var3, var4, this.c, var1, var2);
            } else if (this.animationId >= 0) {
               this.p.a(bt, this.animationId, var3, var4, this.c);
            } else if (this.h >= 0) {
               this.p.a(bt, this.h, var3, var4, this.c, var1, var2);
            }
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }
   }

   final void c(int var1) {
      if (this.g != -1) {
         this.animationId = this.g;
         this.h = 0;
         this.i = 0;
         this.g = -1;
      } else if ((this.c & 64) == 0 && this.p != null && this.i >= 0) {
         int var2;
         if ((var2 = this.p.a(this.animationId, this.h, this.f())) != 0) {
            if (this.h == this.p.a(this.animationId) - 1 && this.i + var1 >= var2) {
               var1 = 1;
            }

            this.i += var1;
            if (var2 <= this.i) {
               while (this.i >= var2) {
                  this.i -= var2;
                  if (this.h >= this.p.a(this.animationId)) {
                     break;
                  }

                  this.h++;
                  this.i = 0;
                  if (this.h >= this.p.a(this.animationId)) {
                     break;
                  }

                  var2 = this.p.a(this.animationId, this.h, this.f());
               }

               if (this.h >= this.p.a(this.animationId)) {
                  this.h = 0;
               }
            }
         }
      }
   }

   public final int[] g() {
      this.o = false;
      if (this.collisionBounds == null) {
         this.collisionBounds = new int[4];
         this.k = new int[4];
      }

      if (this.m != null) {
         if (this.type == 22
            || this.type == 21
            || this.type == 41
            || this.type == 40
            || this.type == 45
            || this.type == 49
            || this.type == 20
            || this.type == 23
            || this.type == 59
            || this.type == 62 && this.aA > 1
            || this.type == 68
            || this.type == 74
            || this.type == 87
            || this.type == 80) {
            this.collisionBounds[0] = this.m[0];
            this.collisionBounds[1] = this.m[1];
            this.collisionBounds[2] = this.m[2];
            this.collisionBounds[3] = this.m[3];
            return this.collisionBounds;
         }

         if (this.type == 42 || this.type == 78) {
            this.collisionBounds[0] = this.m[0];
            this.collisionBounds[1] = this.m[1] - this.av;
            this.collisionBounds[2] = this.m[2];
            this.collisionBounds[3] = this.m[3];
            return this.collisionBounds;
         }
      }

      if (this.p != null) {
         if (this.i >= 0) {
            this.p.a(this.collisionBounds, this.animationId, this.h, 0, this.c, 0, 0);
         } else if (this.animationId >= 0) {
            this.p.b(this.collisionBounds, this.animationId, this.c);
         } else if (this.h >= 0) {
            this.p.a(this.collisionBounds, this.h, 0, this.c, 0, 0);
         }

         if (this.i >= 0) {
            this.p.a(this.k, this.animationId, this.h, 1, this.c, 0, 0);
         } else {
            this.p.a(this.k, this.animationId, 1, this.c, 0, 0);
         }

         if (this.l == null) {
            this.l = new int[4];
         }

         if (this.i >= 0) {
            this.p.a(this.l, this.animationId, this.h, -1, this.c, 0, 0);
         } else {
            this.p.a(this.l, this.animationId, -1, this.c, 0, 0);
         }

         this.l[0] = this.l[0] + this.positionX;
         this.l[1] = this.l[1] + this.positionY;
         this.l[2] = this.l[2] + this.positionX;
         this.l[3] = this.l[3] + this.positionY;
      }

      if (this.type == 94) {
         this.k[0] = this.collisionBounds[0] = 0;
         this.k[1] = this.collisionBounds[1] = 0;
         this.k[2] = this.collisionBounds[2] = 60;
         this.k[3] = this.collisionBounds[3] = 80;
      }

      this.collisionBounds[0] = this.collisionBounds[0] + this.positionX;
      this.collisionBounds[1] = this.collisionBounds[1] + this.positionY;
      this.collisionBounds[2] = this.collisionBounds[2] + this.positionX;
      this.collisionBounds[3] = this.collisionBounds[3] + this.positionY;
      if (this.collisionBounds[0] == this.collisionBounds[2] && this.collisionBounds[1] == this.collisionBounds[3]) {
         for (int var1 = 0; var1 < 4; var1++) {
            this.collisionBounds[var1] = this.l[var1];
         }
      }

      this.k[0] = this.k[0] + this.positionX;
      this.k[1] = this.k[1] + this.positionY;
      this.k[2] = this.k[2] + this.positionX;
      this.k[3] = this.k[3] + this.positionY;
      if (this.type == -101 && this.animationId == 1) {
         this.k[0] = this.k[0] - 30;
      }

      return this.collisionBounds;
   }

   static final boolean a(int[] var0, int[] var1) {
      try {
         if (var0[0] > var1[0]) {
            return false;
         } else if (var0[2] < var1[2]) {
            return false;
         } else {
            return var0[1] > var1[1] ? false : var0[3] >= var1[3];
         }
      } catch (Exception var3) {
         var3.printStackTrace();
         return false;
      }
   }

   static final boolean b(int[] var0, int[] var1) {
      if (var0 != null && var1 != null) {
         try {
            if (var0[0] > var1[2]) {
               return false;
            } else if (var0[2] < var1[0]) {
               return false;
            } else if (var0[1] > var1[3]) {
               return false;
            } else if (var0[3] < var1[1]) {
               return false;
            } else {
               return var0[0] == var0[2] && var0[1] == var0[3] ? false : var1[0] != var1[2] || var1[1] != var1[3];
            }
         } catch (Exception var3) {
            var3.printStackTrace();
            return false;
         }
      } else {
         return false;
      }
   }

   static final boolean c(int[] var0, int[] var1) {
      if (var0 != null && var1 != null) {
         try {
            if (var0[0] >= var1[2]) {
               return false;
            } else if (var0[2] <= var1[0]) {
               return false;
            } else if (var0[1] >= var1[3]) {
               return false;
            } else if (var0[3] <= var1[1]) {
               return false;
            } else {
               return var0[0] == var0[2] && var0[1] == var0[3] ? false : var1[0] != var1[2] || var1[1] != var1[3];
            }
         } catch (Exception var3) {
            var3.printStackTrace();
            return false;
         }
      } else {
         return false;
      }
   }

   static final boolean a(int var0, int var1, int[] var2) {
      if (var0 <= var2[0]) {
         return false;
      } else if (var0 > var2[2]) {
         return false;
      } else {
         return var1 <= var2[1] ? false : var1 <= var2[3];
      }
   }

   public final boolean c(Actor var1) {
      for (int var2 = 0; var2 < 3; var2++) {
         if (dU[var2] == var1) {
            return true;
         }
      }

      return false;
   }

   private int aA() {
      int var2 = 0;

      for (int var3 = Game.ck - 1; var3 >= 0; var3--) {
         Actor var1;
         if ((var1 = Game.cj[var3]) != null && var1 != this && var1.N) {
            var1.g();
            if (this.type == 0) {
               if (var1 != aT && var1.type != 82 && b(var1.collisionBounds, this.collisionBounds) && this.positionY > var1.collisionBounds[1] && !var1.O) {
                  if (this.positionX <= var1.positionX && this.collisionBounds[2] - var1.collisionBounds[0] > 0) {
                     if (!this.eL) {
                        this.positionX = this.positionX - (this.collisionBounds[2] - var1.collisionBounds[0]);
                     } else {
                        this.positionX = this.positionX + (var1.collisionBounds[2] - this.collisionBounds[0]);
                     }
                  } else if (this.positionX >= var1.positionX && var1.collisionBounds[2] - this.collisionBounds[0] > 0) {
                     if (!this.eM) {
                        this.positionX = this.positionX + (var1.collisionBounds[2] - this.collisionBounds[0]);
                     } else {
                        this.positionX = this.positionX - (this.collisionBounds[2] - var1.collisionBounds[0]);
                     }
                  }
               }
            } else if (var1.type == 37) {
               if (!var1.O || this.type != 0) {
                  if (this.positionX <= var1.positionX && this.collisionBounds[2] - var1.collisionBounds[0] > 0) {
                     if (!this.eL) {
                        this.positionX = this.positionX - (this.collisionBounds[2] - var1.collisionBounds[0]);
                     } else {
                        this.positionX = this.positionX + (var1.collisionBounds[2] - this.collisionBounds[0]);
                     }
                  } else if (this.positionX >= var1.positionX && var1.collisionBounds[2] - this.collisionBounds[0] > 0) {
                     if (!this.eM) {
                        this.positionX = this.positionX + (var1.collisionBounds[2] - this.collisionBounds[0]);
                     } else {
                        this.positionX = this.positionX - (this.collisionBounds[2] - var1.collisionBounds[0]);
                     }
                  }
               }
            } else {
               if (this.type == 0
                  && var1 == aT
                  && (this.collisionBounds[0] > var1.collisionBounds[2] || this.collisionBounds[2] < var1.collisionBounds[0])
                  && aT.type != 50) {
                  aT = null;
                  this.q(0);
               }

               if (this.aq() && var1.ap() && var1.H > 0 && var1.H > this.H) {
                  int var5 = var1.b + var1.H >> 8;
                  if (var1.positionY <= this.collisionBounds[1]
                     && var1.collisionBounds[2] > this.collisionBounds[0]
                     && var1.collisionBounds[0] < this.collisionBounds[2]
                     && var5 >= this.collisionBounds[1] + (this.H >> 8)) {
                     var1.T = this;
                     if (!this.c(this)) {
                        this.U = var1;
                     }

                     var1.H = 0;
                  }
               } else if (var1.aq() && this.ap() && this.H > 0 && this.H > var1.H) {
                  int var4 = this.b + this.H >> 8;
                  if (this.positionY <= var1.collisionBounds[1]
                     && this.collisionBounds[2] > var1.collisionBounds[0]
                     && this.collisionBounds[0] < var1.collisionBounds[2]
                     && var4 >= var1.collisionBounds[1] + (var1.H >> 8)) {
                     this.T = var1;
                     if (!this.c(this)) {
                        var1.U = this;
                     }

                     this.H = 0;
                  }
               }

               if (b(var1.collisionBounds, this.collisionBounds) && var1.type != 82 && var1.T != this && this.T != var1) {
                  if (this.positionX < var1.positionX && this.collisionBounds[2] - var1.collisionBounds[0] >= 0) {
                     this.positionX = this.positionX - (this.collisionBounds[2] - var1.collisionBounds[0]);
                     var2 |= 2;
                  } else if (this.positionX > var1.positionX && var1.collisionBounds[2] - this.collisionBounds[0] >= 0) {
                     this.positionX = this.positionX + (var1.collisionBounds[2] - this.collisionBounds[0]);
                     var2 |= 1;
                  }
               }
            }
         }
      }

      return var2;
   }

   public final boolean h() {
      if ((this.type != 39 || this.animationId != 58) && this.type != -2) {
         if (this.type == 28) {
            return true;
         }

         if (this.type == 21 && this.an == -1) {
            return true;
         }

         if (this.type != 81 || this.aq != 101 && (this.n == null || this.n[this.ax - 1] <= 0 || this.n[this.ax - 1] > 7)) {
            if (this.type == 93) {
               ey[1] = this.positionY;
               ey[0] = this.positionX;
               if (this.aC != 18) {
                  ey[2] = this.positionX + this.ay + 40;
                  switch (this.ax) {
                     case 0:
                        ey[3] = this.positionY + 80;
                        break;
                     case 1:
                        ey[3] = this.positionY + 120;
                        break;
                     case 2:
                        ey[3] = this.positionY + 160;
                  }

                  ey[3] = ey[3] + 40;
               } else {
                  ey[3] = this.positionY + 20;
                  ey[2] = this.positionX + (this.av - 1) * this.az + 20;
               }

               return b(Game.viewBounds, ey);
            } else {
               int var1 = this.positionX - (Game.cameraX + 120);
               int var2 = this.positionY - (Game.cameraY + 160);
               if (var1 < 0) {
                  var1 = -var1;
               }

               if (var2 < 0) {
                  var2 = -var2;
               }

               this.ak = var1 / 120 + var2 / 160;
               if (this.ak <= this.al + 1) {
                  this.g();
                  if (this.type != 21 && this.type != 22 && this.type != 23) {
                     if (this.type == 41 || this.type == 42 || this.type == 45 || this.type == 49 || this.type == 78 || this.l == null) {
                        return b(Game.viewBounds, this.collisionBounds);
                     }

                     if (this.type != 50 && this.type != 53 && (this.type != 62 || this.aA <= 1)) {
                        if (this.type == 10) {
                           return true;
                        } else {
                           return this.type == 70 && this.aH == 3 && this.aq == 114 ? b(Game.viewBounds, this.aJ) : b(Game.viewBounds, this.l);
                        }
                     } else {
                        return b(this.m, Game.viewBounds);
                     }
                  } else if (this.collisionBounds == null) {
                     return true;
                  } else {
                     return this.type != 21 || this.animationId != 17 && this.animationId != 18
                        ? b(Game.player.collisionBounds, this.collisionBounds)
                        : b(Game.viewBounds, this.collisionBounds);
                  }
               } else if (this.type == 10 && this.u != null) {
                  this.u.g();
                  return b(Game.viewBounds, this.u.collisionBounds);
               } else {
                  return false;
               }
            }
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private void k(int var1, int var2) {
      if ((var2 & 1) == 0) {
         this.a(var1);
         this.g();
      }

      if ((var2 & 2048) != 0) {
         this.positionX = this.collisionBounds[0];
      } else if ((var2 & 4096) != 0) {
         this.positionX = this.collisionBounds[2];
      } else if ((var2 & 4) != 0) {
         int var3 = this.collisionBounds[0] + this.collisionBounds[2] >> 1;
         this.positionX = this.positionX + (this.eA - var3);
      } else if ((var2 & 8) != 0) {
         this.positionX = this.positionX - (this.positionX % 20 - 10);
      }

      if ((var2 & 64) != 0) {
         this.positionY = this.collisionBounds[1];
      } else if ((var2 & 128) != 0) {
         this.positionY = this.collisionBounds[3];
      } else if ((var2 & 8192) != 0) {
         this.positionY = this.collisionBounds[3] / 20 * 20 - 1;
      } else if ((var2 & 16384) != 0) {
         this.positionY = (this.collisionBounds[3] + 10) / 20 * 20 - 1;
      } else if ((var2 & 256) != 0) {
         this.positionY = this.collisionBounds[1] / 20 * 20 - 1;
      } else if ((var2 & 512) != 0) {
         this.positionY = (this.collisionBounds[1] / 20 + 1) * 20 - 1;
      } else if ((var2 & 32) != 0) {
         int var4 = this.collisionBounds[1] + this.collisionBounds[3] >> 1;
         this.positionY = this.positionY + (this.eB - var4);
      } else if ((var2 & 1024) != 0) {
         this.positionY = this.positionY + (this.positionY - this.collisionBounds[1]);
      }

      if ((var2 & 16) != 0) {
         this.positionY = this.positionY - (this.positionY % 20 - 10);
      }

      if ((var2 & 1) != 0) {
         this.a(var1);
      }
   }

   private void q(int var1) {
      this.k(7, 32);
      this.H = var1;
      this.J = 320;
   }

   public static final boolean d(int var0) {
      if (bO > 0 && var0 < 500) {
         return false;
      }

      if (Game.player.cI) {
         bO = eO << 1;
      } else {
         bO = eO;
      }

      if (Game.health > 0) {
         int var1;
         var0 = (var1 = var0 * ((Game.maxHealth - 60 << 7) / 60 + 256)) >> 8;
         Game.health -= var0;
         if (Game.health < 0) {
            Game.health = 0;
         }
      }

      return true;
   }

   public static final boolean e(int var0) {
      if (Game.magic >= var0) {
         if (aP) {
            return true;
         }

         Game.magic -= var0;
         bc = 0;
         return true;
      } else {
         return false;
      }
   }

   public static final void i() {
      if (!bD) {
         Game.magic = 0;
      } else {
         bc = bc + Game.h;
         switch (Game.player.aq) {
            case 0:
            case 2:
               if (Game.magic != Game.maxMagic && bc >= 5000) {
                  bc = 0;
                  if (Game.magic < Game.maxMagic) {
                     Game.magic += 10;
                     if (Game.magic > Game.maxMagic) {
                        Game.magic = Game.maxMagic;
                     }

                     Game.bu = true;
                  }
               }
            case 1:
         }
      }
   }

   public Actor() {
   }

   private static boolean b(int[] var0, int var1) {
      boolean var2 = false;
      int var3 = (var0.length + 1) / 2;

      for (int var4 = 0; var4 < Math.min(var3 - 1, var1); var4++) {
         if (Game.player.animationId == var0[var4] && Game.F(var0[var4 + var3])) {
            e = var0[var4 + 1];
            return true;
         }
      }

      return false;
   }

   private static void aB() {
      if (e == -1) {
         if (ch == 0) {
            if (!Game.player.bL && aT == null && !bM) {
               b(be, ck[ch]);
            } else {
               b(bd, ck[ch]);
            }
         } else if (ch != 1 || !b(bf, ck[ch])) {
            ;
         }
      }
   }

   public final void a(int[] var1) {
      bl = var1;
      bj = true;
      bk = 0;
      this.a(bl[0]);
   }

   public static final void j() {
      bm = false;
      bn = 0;
      bp = 0;
      bq = false;
      br = false;
      bj = false;
      bk = 0;
   }

   public final void b(int[] var1) {
      bo = var1;
      bm = true;
      bn = 0;
      this.l();
   }

   public final void k() {
      bp = bp - Game.h;
      if (!bq && bp <= 0 || bq && this.e()) {
         bn++;
         if (bn << 2 >= bo.length) {
            bm = false;
            this.db();
            return;
         }

         this.l();
      }
   }

   public final void l() {
      this.a(bo[bn * 4 + 0]);
      this.G = bo[bn * 4 + 1] << 8;
      this.H = bo[bn * 4 + 2] << 8;
      bp = bo[bn * 4 + 3];
      this.an();
      if (bp <= 0) {
         bq = true;
      } else {
         bq = false;
      }

      if (bo[bn * 4 + 0] == 32) {
         if (ba == null) {
            ba = new Actor[4];
         }

         if (ba[0] != null) {
            ba[3] = ba[0];
            if (ba[3].animationId == 18 || ba[3].animationId == 19 || ba[3].animationId == 20) {
               ba[3].a(21);
            }
         }

         ba[0] = this.a(39, 17, cl[ch] == 2 ? 17 : 16, !this.facingLeft ? 10 : -10, -48, 302, false);
         ba[0].c |= 16;
         ba[0].G = ba[0].facingLeft ? -2560 : 2560;
      }
   }

   private boolean aC() {
      if (this.e()) {
         bk++;
         if (bk >= bl.length) {
            bj = false;
            e = -1;
            return false;
         }

         this.a(bl[bk]);
         e = bl[bk];
      }

      return true;
   }

   private void aD() {
      if (aX()) {
         this.a(39, 17, 0 + ch == 0 ? Game.i(0, 2) : Game.i(2, 4), 0, -Game.i(10, 50), 302, false);
      }
   }

   private boolean e(boolean var1) {
      if (this.aq != 3
         && (this.aq != 4 || this.type == 11)
         && this.aq != 9
         && this.aD <= 0
         && (16384 & this.c) == 0
         && (1024 & this.c) == 0
         && !aQ
         && (this.type != 99 || this.aq != 106 && this.aq != 102 && this.aq != 103 && this.aq != 104)) {
         if (!this.at() && !this.e(this.collisionBounds)) {
            return false;
         }

         this.b(x() * e(ch, this.type) / 100, var1);
         this.aD();
         return true;
      } else {
         return false;
      }
   }

   private boolean aE() {
      return this.e(true);
   }

   private void b(int var1, boolean var2) {
      this.a(39, 17, 58, 0, -50, 302, false);
      aU.aA = 1000;
      aU.ay = var1;
      int var3 = 100;
      if (cj[ch] == 3) {
         var3 -= 10;
      }

      if (ck[ch] == 4) {
         var3 -= 15;
      }

      var1 = var1 * var3 / 100;
      this.as -= var1;
      if (this.type == 14 && this.animationId != 2 && this.animationId != 3) {
         this.a(18, 12, 6, 0, -Game.i(10, 50), 302, Game.random.nextInt() % 2 == 0);
         aU.aq = 3;
      } else {
         this.a(39, 17, this.aF(), 0, -Game.i(10, 50), 302, true);
      }

      if (this.as > 0) {
         if (var2) {
            this.c(2, true);
         }

         this.aD = 200;
      } else {
         if (!this.r(this.type)) {
            if (this.type != 14 || this.aq == 107 || this.animationId != 2 && this.animationId != 3) {
               if (this.type == 100) {
                  this.p(112);
               } else if (this.type == 90) {
                  this.p(129);
               } else {
                  this.p(3);
               }
            } else {
               this.p(107);
            }
         }

         if (this.type == 2 || this.type == 3) {
            this.w(0, 0);
         }
      }

      if (Game.modalActive) {
         j(3, 0);
      }
   }

   private int aF() {
      return 4 + Game.i(0, 3);
   }

   private boolean r(int var1) {
      if (Game.be[Game.levelIndex] == 3 || !bu) {
         return false;
      }

      if (E == this) {
         return false;
      }

      if (var1 == 4) {
         return false;
      }

      if (!b(this.collisionBounds, Game.player.k)) {
         return false;
      }

      if (Game.player.animationId >= 38 && Game.player.animationId <= 46) {
         return false;
      }

      if (!Game.player.bL && aT == null) {
         return false;
      }

      if (var1 == 11 && this.ar() != 1) {
         Game.h(6);
         e(Game.cb);
         Game.player.H = 0;
         this.p(4);
         Game.player.aA = 600;
         Game.U = 0;
         Game.player.a(40);
         Game.player.positionX = this.positionX;
         this.O = false;
         this.aA = 3000;
         aN = false;
         if (aO != null) {
            aO.b(47);
            dk = null;
            aO = null;
         }

         return true;
      } else {
         int var2 = Game.i(1, 101);
         if (Game.player.animationId != 46 && var2 <= y() / 2) {
            Game.h(8);
            this.p(9);
            this.G = 0;
            this.H = 0;
            if (var1 == 1) {
               this.b(8);
            }

            Game.U = 0;
            Game.player.b(46);
            Game.player.H = 0;
            return true;
         } else {
            if (var2 > y()) {
               return false;
            }

            if (ch == 0) {
               Game.h(6);
               e(Game.cb);
               Game.player.H = 0;
               this.p(4);
               Game.player.aA = 600;
               Game.U = 0;
               if (var1 == 1 && Game.player.animationId != 39) {
                  Game.player.a(39);
               } else if (var1 != 1) {
                  Game.player.a(38);
               }

               return true;
            } else if (ch == 1 && var1 != 14) {
               Game.h(7);
               e(Game.cb);
               this.p(8);
               Game.U = 0;
               Game.player.a(44);
               Game.player.H = 0;
               return true;
            } else {
               return false;
            }
         }
      }
   }

   private void s(int var1) {
      this.b(var1, true);
   }

   public static final void m() {
      aQ = false;
      aR = -1;
      aS = 1;
      ev = 0;
      bP = false;
      bb = 0;
      aT = null;
      aU = null;
      aM = false;
      aN = false;
      aO = null;
      ez = false;
      dW = 0;
      dX = "";
      dY = 0;
      dZ = false;
      ea = false;
      eb = 0;
      ec = 0;
      ed = 0;
      bO = 0;
      cv = -1;
      Game.player = null;
      E = null;
      cL = 0;
      j();
      en = false;
      ep = null;
      aj = 0;
      Game.bN = 0;
      Game.bw = false;
      Game.df = 0;

      for (int var0 = 0; var0 < 3; var0++) {
         Game.dg[var0] = -1;
      }

      Game.aO = 0;
      Game.aN = 0;
   }

   public Actor(short[] var1, int var2, int var3) {
      this.a(var1, true, var2, var3);
   }

   public final void a(short[] var1, boolean var2, int var3, int var4) {
      if (var2) {
         this.an = var1[1];
      }

      this.positionX = var1[2] & '\uffff';
      this.positionY = var1[3] & '\uffff';
      this.a = this.positionX << 8;
      this.b = this.positionY << 8;
      this.type = var1[0];
      this.facingLeft = (var1[5] & 1) != 0;
      this.c = var1[5];
      if (this.type == 11) {
         this.aH = ex[var1[10]];
      }

      if (this.type == 94) {
         this.c = var1[4];
      }

      if (var2 && this.ao()) {
         int[] var5;
         (var5 = ai[aj])[0] = var3;
         var5[1] = var4;
         aj++;
         ah.put(new Integer(this.an), var5);
      }

      if (bs[this.type] != -1) {
         this.p = Game.n(bs[this.type]);
      }

      this.b(var1[4]);
      if (this.type == 3) {
         this.aG();
      }

      if ((this.c & 8) != 0) {
         if (this.m == null) {
            this.m = new int[4];
         }

         this.m[0] = var1[6] + this.positionX;
         this.m[1] = var1[7] + this.positionY;
         this.m[2] = this.m[0] + var1[8];
         this.m[3] = this.m[1] + var1[9];
      }

      label466:
      switch (this.type) {
         case 0:
            this.ap = 101;
            Game.health = Game.maxHealth;
            Game.magic = Game.maxMagic;
            break;
         case 1:
            this.aC = var1[10];
            this.aH = ex[this.aC < 4 ? this.aC : this.aC - 4];
            if (this.aC >= 4) {
               this.c |= 128;
            }

            this.aI = var1[11];
            this.as = this.B(0);
            this.fw = this.B(2);
            if (this.fw != 0 && (this.c & 128) != 0) {
               this.d();
            }

            this.aJ = new int[4];
            this.aG = var1[12];
            if (this.aG != -1) {
               this.aL = 2;
               this.ap = 50;
            } else {
               this.ap = 81;
               if (this.aC < 4) {
                  this.O = true;
               }
            }
            break;
         case 2:
            this.aH = ex[var1[10]];
            this.aI = var1[11];
            this.as = this.B(0);
            this.fw = this.B(2);
            if (this.fw != 0 && (this.c & 128) != 0) {
               this.d();
            }

            this.ad = this.positionY;
            this.aJ = new int[4];
            this.ap = 81;
            this.fx = !this.facingLeft;
            this.a(var1[4]);
            this.aG = var1[12];
            if (this.ar() == 3 || this.ar() == 2) {
               this.a(1);
            }

            if (3 != Game.be[Game.levelIndex] && this.ar() != 5 && this.ar() != 6) {
               this.O = true;
            }
            break;
         case 3:
            this.aH = ex[var1[10]];
            this.aI = var1[11];
            this.as = this.B(0);
            this.fw = this.B(2);
            if (this.fw != 0 && (this.c & 128) != 0) {
               this.d();
            }

            this.aJ = new int[4];
            this.ap = 2;
            this.aG = var1[12];
            this.fx = !this.facingLeft;
            if (3 != Game.be[Game.levelIndex]) {
               this.O = true;
            }

            switch (this.ar()) {
               case 0:
                  this.aL = 2;
                  break label466;
               case 1:
                  this.aL = 0;
               default:
                  break label466;
            }
         case 4:
            this.aH = ex[var1[10]];
            this.aI = var1[11];
            this.c |= 4096;
            this.aJ = new int[4];
            this.ap = 100;
            break;
         case 5:
            this.aH = ex[var1[6]];
            this.aI = var1[7];
            this.ap = 100;
            this.ay = this.B(1);
            this.aw = this.B(2);
            this.g();
            if (var1[4] == 0) {
               this.aK = 8;
               if (this.ar() == 1) {
                  this.aB = this.collisionBounds[3];
                  this.ad = this.positionY;
                  this.positionY = this.positionY + (this.collisionBounds[3] - this.collisionBounds[1]);
                  this.av = this.positionY;
               }
            } else if (var1[4] == 4) {
               this.aK = 4;
               if (this.ar() == 1) {
                  this.aB = this.collisionBounds[1];
                  this.ad = this.positionY;
                  this.positionY = this.positionY + (this.collisionBounds[1] - this.collisionBounds[3]);
                  this.av = this.positionY;
               }
            } else if (var1[4] == 2) {
               this.aK = this.facingLeft ? 2 : 1;
               if (this.ar() == 1) {
                  this.aB = (this.facingLeft ? this.collisionBounds[2] : this.collisionBounds[0]) - 1;
                  this.ac = this.positionX;
                  this.positionX = this.positionX
                     + (this.facingLeft ? this.collisionBounds[2] - this.collisionBounds[0] : this.collisionBounds[0] - this.collisionBounds[2]);
                  this.ay = this.positionX;
               }
            }
         case 6:
         case 7:
         case 12:
         case 16:
         case 17:
         case 18:
         case 19:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 29:
         case 30:
         case 32:
         case 34:
         case 36:
         case 39:
         case 43:
         case 44:
         case 47:
         case 54:
         case 56:
         case 57:
         case 58:
         case 60:
         case 63:
         case 66:
         case 67:
         case 76:
         case 77:
         case 84:
         case 85:
         case 97:
         case 98:
         default:
            break;
         case 8:
            this.aH = ex[var1[6]];
            this.aI = var1[7];
            this.as = this.B(0);
            this.o(0, 0);
            this.ac = this.positionX;
            this.ad = this.positionY;
            if (this.ar() == 0) {
               this.aG = var1[8];
               Game.b(this);
            } else if (this.ar() == 1) {
               this.y = var1[8];
               this.z = var1[9];
               this.D = var1[10];
               this.g();
               this.aC = this.positionY;
               this.aG();
               this.aF = this.collisionBounds[3];
               this.positionY = this.aC;
            }

            this.ap = 99;
            break;
         case 9:
            this.aH = ex[var1[10]];
            this.aI = var1[11];
            this.aG = var1[12];
            this.a(var1[4]);
            this.ap = 99;
            this.c |= 16;
            if (this.ar() == 0) {
               this.aB = this.facingLeft ? 0 : Game.levelPixelWidth;
            }

            if (this.ar() == 1) {
               this.c |= 128;
            }

            if (this.ar() == 2 || this.ar() == 3) {
               this.ac = this.positionX;
               this.ad = this.positionY;
               this.av = this.positionY + -50;
               this.g();
               this.aB = this.collisionBounds[1];
            }
            break;
         case 10:
            this.aH = ex[var1[10]];
            this.aI = var1[11];
            this.ay = this.positionX + var1[12];
            this.av = this.positionY + var1[13];
            this.ac = this.ay;
            this.ad = this.av;
            this.ap = 81;
            if (var1[4] == 0) {
               this.aK = 8;
            } else if (var1[4] == 8) {
               this.aK = 4;
            } else if (var1[4] == 4) {
               this.aK = this.facingLeft ? 2 : 1;
            }
            break;
         case 11:
            this.aH = ex[var1[10]];
            this.aI = var1[11];
            this.D = var1[12];
            this.aJ = new int[4];
            this.as = this.B(0);
            this.ap = 98;
            if (this.ar() == 1) {
               this.c |= 16;
            }

            this.O = true;
            this.P = true;
            break;
         case 13:
            this.aH = ex[var1[10]];
            this.aI = var1[11];
            this.as = this.B(0);
            this.ap = 102;
            if (this.ar() == 1 || this.ar() == 3) {
               this.aL = 1;
            } else if (this.ar() == 2) {
               this.aL = 2;
            }
            break;
         case 14:
            this.aH = ex[var1[10]];
            this.aI = var1[11];
            this.as = this.B(0);
            this.fw = this.B(2);
            this.aJ = new int[4];
            this.ap = 102;
            this.O = true;
            if (this.animationId == 2) {
               this.aq = 104;
            } else if (this.fw != 0 && (this.c & 128) != 0) {
               this.d();
            }
            break;
         case 15:
            this.av = this.positionY;
            break;
         case 20:
            this.ap = 300;
            this.c |= 128;
            this.aB = var1[11];
            this.aA = var1[10];
            break;
         case 21:
            this.c |= 4096;
            break;
         case 22:
            this.c |= 16;
            this.aA = var1[10];
            this.aB = var1[11];
            break;
         case 28:
            this.ap = 102;
            this.c |= 128;
            break;
         case 31:
            this.g();
            this.aH = this.animationId != 4 && this.animationId != 15 ? 5 : 4;
            this.fv = 20 * this.aH;
            this.aC = var1[6];
            this.az = this.positionY;
            this.aw = this.positionX;
            if (var1[6] == 9) {
               this.aq = 104;
               this.az = this.az + (this.collisionBounds[3] - this.collisionBounds[1]);
            }

            if (this.m == null) {
               this.m = new int[4];
            }

            this.m[0] = this.collisionBounds[0];
            this.m[1] = this.collisionBounds[1];
            this.m[2] = this.collisionBounds[2];
            this.m[3] = this.collisionBounds[3];
            this.B = var1[7];
            this.C = var1[8];
            this.a(this.m, false);
            break;
         case 33:
            this.aB = var1[10];
            this.aA = this.aw = var1[12];
            this.ay = var1[11] << 8;
            this.ap = 3;
            break;
         case 35:
            this.ap = 75;
            this.ay = 1;
            this.aw = -1;
            if (this.animationId == 6) {
               this.aA = 2;
            } else {
               this.aA = 1;
            }
            break;
         case 37:
            this.aC = var1[6];
            this.fu = var1[7];
            this.fv = var1[8];
            this.fw = var1[9];
            this.fx = true;
            this.positionY += 98;
            this.positionY = this.positionY - this.fw;
            this.aq = this.aC == es ? 101 : 100;
            break;
         case 38:
            this.aG = var1[7];
            if (this.aG != -1) {
               this.c |= 4096;
            }

            this.aA = 3500;
            this.ap = 3;
            this.aH = var1[6];
            this.aC = this.animationId == 1 ? 0 : 1;
            break;
         case 40:
            this.c |= 128;
            this.al = -1;
            this.aA = var1[10];
            this.aB = var1[11];
            this.ay = var1[12];
            this.aw = -1;
            this.y = var1[13];
            this.z = var1[14];
            this.A = var1[15];
            this.B = var1[16];
            this.C = var1[17];
            this.D = var1[18];
            break;
         case 41:
            this.al = 5;
            if (var2) {
               Game.bM[Game.bN * 5 + 0] = this.m[0];
               Game.bM[Game.bN * 5 + 1] = this.m[1];
               Game.bM[Game.bN * 5 + 2] = var1[8];
               Game.bM[Game.bN * 5 + 3] = var1[9];
               if (var1[4] != 0) {
                  Game.bM[Game.bN * 5 + 4] = 2;
                  if (var1[4] == 2) {
                     Game.bM[Game.bN * 5 + 4] = 3;
                  }
               }

               this.aB = Game.bN++;
            }
            break;
         case 42:
            this.ap = 104;
            this.al = 14;
            this.y = var1[12];
            this.z = var1[13];
            this.A = var1[14];
            this.B = var1[15];
            this.C = var1[16];
            this.av = 0;
            this.aC = var1[10];
            if (this.m[3] - this.m[1] >= var1[11]) {
               this.aw = this.m[1];
               this.ax = this.m[3] - var1[11];
            } else {
               this.aw = this.m[3] - var1[11];
               this.ax = this.m[1];
            }

            if (this.aC == 1) {
               this.az = var1[9];
            }
            break;
         case 45:
            this.ap = 2;
            this.al = 4;
            break;
         case 46:
            this.av = 0;
            this.az = 0;
            break;
         case 48:
            this.ap = 99;
            break;
         case 49:
            this.aA = var1[10];
            this.aG = var1[11];
            this.aB = var1[12];
            this.ay = var1[13];
            break;
         case 50:
            this.al = 4;
            this.aw = this.positionX;
            this.av = var1[6];
            this.az = this.positionY + this.av;
            this.n = new int[12];
            this.n[5] = this.n[4] = var1[7];
            this.n[0] = this.n[1] = var1[9];
            this.n[3] = this.n[2] = var1[8];
            this.aC = var1[10];
            this.ap = 2;
            if (this.aC == 1) {
               this.aq = 101;
            }

            this.aA = 0;
            this.n[6] = this.aB = var1[11];
            this.ay = 1;
            this.n[7] = this.ax = this.n[4];
            this.o();
            break;
         case 51:
            this.al = 2;
            break;
         case 52:
            this.al = 2;
            this.ay = var1[6];
            if (this.ay <= 1 && this.ay >= 0 && this.animationId == 0) {
               this.c |= 512;
            }

            this.ap = 75;
            break;
         case 53:
            this.al = 2;
            this.av = var1[6];
            this.az = this.positionY + this.av;
            this.aG = var1[7];
            this.o();
            break;
         case 55:
            this.aA = var1[6];
            this.ay = var1[7];
            this.aB = var1[8];
            break;
         case 59:
            this.c |= 128;
            this.aG = var1[10];
            this.aA = var1[11];
            break;
         case 61:
         case 79:
            this.al = 3;
            this.g();
            this.ay = this.collisionBounds[2] - this.collisionBounds[0];
            this.aw = this.ay / 20;
            this.av = this.collisionBounds[3] - this.collisionBounds[1];
            this.n = new int[this.aw * (this.av / 20)];
            if (this.type == 61) {
               this.aA = var1[8];
               this.aG = var1[6];
               this.y = var1[7];
            }

            if (this.type == 61) {
               this.ap = 76;
            } else {
               this.ap = -2;
            }
            break;
         case 62:
            this.al = 4;
            this.c |= 32;
            this.ap = var1[8];
            this.p = Game.n(var1[9]);
            this.aA = var1[6];
            this.aB = var1[7];
            this.n();
            this.ay = this.m[2] - this.m[0];
            this.av = this.m[3] - this.m[1];
            if (this.aB == 0) {
               this.m[2] = this.m[2] + (this.aA - 1) * this.ay;
            } else if (this.aB == 1) {
               this.m[3] = this.m[3] + (this.aA - 1) * this.av;
            }

            if (var1[9] == 39 && this.animationId >= 10) {
               Game.b(this);
            }
            break;
         case 64:
            this.ap = 5;
            break;
         case 65:
            this.aC = var1[10];
            break;
         case 68:
            this.aH = ex[var1[10]];
            this.aI = var1[12];
            this.aw = var1[11];
            this.aA = var1[13];
            break;
         case 69:
            if (this.animationId == 0) {
               this.ap = 100;
            } else if (this.animationId == 1) {
               this.ap = 102;
            }

            this.c |= 16;
            break;
         case 70:
            this.aH = var1[10];
            this.fx = var1[11] == 1;
            this.fv = var1[12] * 20;
            if (this.aH == 3 && this.fv == 0) {
               this.fv = 80;
            }

            this.aI = var1[13];
            this.as = this.B(0);
            this.fw = this.B(2);
            if (this.fw != 0 && (this.c & 128) != 0) {
               this.d();
            }

            this.ap = 81;
            switch (this.aH) {
               case 0:
               default:
                  break label466;
               case 1:
                  this.b(0);
                  this.positionY = (this.positionY + 10) / 20 * 20;
                  break label466;
               case 2:
               case 3:
                  this.b(9);
                  this.positionY = this.positionY / 20 * 20;

                  while (Game.g(this.positionX / 20, this.positionY / 20) >= 15) {
                     this.positionY += 20;
                  }

                  while (true) {
                     if (Game.g(this.positionX / 20, this.positionY / 20 - 1) >= 15) {
                        break label466;
                     }

                     this.positionY -= 20;
                  }
            }
         case 71:
            this.aG = var1[6];
            this.aH = -this.aG;
            this.ap = 97;
            break;
         case 72:
            this.aG = var1[6];
            this.N = true;
            this.g();
            this.av = this.positionY;
            this.az = this.positionY + this.collisionBounds[3] - this.collisionBounds[1];
            break;
         case 73:
            this.aH = var1[10];
            this.ax = var1[11] << 8;
            this.N = true;
            this.az = 0;
            this.ap = 100;
            break;
         case 74:
            this.aH = var1[12];
            this.ax = var1[13] << 8;
            this.aK = var1[10];
            this.aB = var1[11];
            this.aG = var1[14];
            if (this.aG == -2) {
               this.aG = -1;
               this.aC = 1;
            }

            this.av = var1[15];
            break;
         case 75:
            this.aH = var1[6];
            break;
         case 78:
            this.ap = 320;
            this.al = 14;
            this.aG = var1[12];
            break;
         case 80:
            this.av = var1[6];
            this.m = null;
            this.g();
            if (this.m == null) {
               this.m = new int[4];
            }

            this.m[0] = this.collisionBounds[0];
            this.m[1] = this.collisionBounds[1];
            this.m[2] = this.collisionBounds[2];
            this.m[3] = this.collisionBounds[3] + this.av;
            this.c |= 128;
            break;
         case 81:
            this.aL = 0;
            this.aC = var1[4];
            this.B = var1[6];
            this.av = var1[7];
            this.ay = var1[8];
            this.ap = 3;
            break;
         case 82:
            this.N = true;
            this.ap = 104;
            this.aA = var1[6];
            if (2 == this.aA) {
               this.g();
               if (this.m == null) {
                  this.m = new int[4];
               }

               this.m[0] = this.collisionBounds[0] + -40;
               this.m[1] = this.collisionBounds[1] + -40;
               this.m[2] = this.collisionBounds[2] + 40;
               this.m[3] = this.collisionBounds[3] + -10;
               dU[2] = this;
            } else if (this.aA >= 7) {
               this.c |= 128;
            } else if (this.aA == 1) {
               dU[0] = this;
            } else if (this.aA == 3) {
               dU[1] = this;
            }
            break;
         case 83:
            this.as = this.B(0);
            this.B = var1[11];
            this.C = var1[12];
            this.aH = var1[13];
            this.c |= 16;
            this.av = this.positionY;
            this.ap = 50;
            break;
         case 86:
            this.av = var1[6];
            this.ay = var1[7];
            this.az = 384;
            this.aw = this.positionY;
            this.aA = this.ay;
            break;
         case 87:
            this.aH = var1[10];
            this.av = var1[11];
            this.az = var1[12];
            this.ay = var1[13];
            this.aw = var1[14];
            this.ax = var1[15];
            this.aG = var1[16];
            break;
         case 88:
            this.aC = var1[4];
            this.aH = this.aC;
            if (this.aC == 35) {
               this.a(27);
               this.aC = 27;
               this.aB = 1000;
            }
            break;
         case 89:
            this.aC = var1[6];
            this.aH = this.animationId == 25 ? 0 : 1;
            this.ay = var1[7];
            this.aw = var1[8];
            break;
         case 90:
            this.aH = ex[var1[10]];
            this.aI = var1[14];
            this.as = this.B(0);
            this.ay = var1[11];
            this.aw = var1[12];
            this.ax = var1[13];
            this.aJ = new int[4];
            this.O = true;
            this.P = true;
            break;
         case 91:
            this.aC = var1[4];
            this.av = var1[10];
            break;
         case 92:
            this.aH = var1[10];
            this.aI = var1[11];
            this.as = this.B(0);
            this.aL = 3;
            this.ap = 1;
            this.fw = this.B(2);
            if (this.fw != 0) {
               this.d();
            }
            break;
         case 93:
            this.aC = var1[4];
            this.ax = this.aC % 9 / 3;
            this.aw = this.aC % 3;
            this.ay = this.aC > 8 ? 70 : 60;
            this.fv = this.aC > 8 ? 1 : 3;
            this.fu = this.aC > 8 ? 0 : 2;
            this.ap = 0;
            this.av = var1[6];
            this.az = var1[7];
            if (this.aC != 18) {
               switch (this.ax) {
                  case 0:
                     this.n = new int[6];
                     this.n[0] = this.positionY + Game.i(10, 20);
                     this.n[1] = this.fv;
                     this.n[2] = this.n[0] + 26 + Game.i(-5, 5);
                     this.n[3] = this.fv;
                     this.n[4] = this.n[2] + 26 + Game.i(-5, 5);
                     if (this.n[4] > this.positionY + 80 - 26) {
                        this.n[4] = this.positionY + 80 - 26;
                     }

                     this.n[5] = this.fv;
                     break;
                  case 1:
                     this.n = new int[8];
                     this.n[0] = this.positionY + Game.i(10, 20);
                     this.n[1] = this.fv;
                     this.n[2] = this.n[0] + 26 - Game.i(5, 10) - 2;
                     this.n[3] = this.fu;
                     this.n[4] = this.n[2] + 38 - Game.i(5, 10) - 2;
                     this.n[5] = this.fv;
                     this.n[6] = this.n[4] + 26 + Game.i(5, 10);
                     if (this.n[6] > this.positionY + 120 - 26) {
                        this.n[6] = this.positionY + 120 - 26;
                     }

                     this.n[7] = this.fv;
                     break;
                  case 2:
                     this.n = new int[10];
                     this.n[0] = this.positionY + Game.i(10, 20);
                     this.n[1] = this.fu;
                     this.n[2] = this.n[0] + 38 - Game.i(5, 10);
                     this.n[3] = this.fu;
                     this.n[4] = this.n[2] + 38 + Game.i(5, 10);
                     this.n[5] = this.fv;
                     this.n[6] = this.n[4] + 26 + Game.i(5, 10);
                     if (this.n[6] > this.positionY + 160 - 38 - 26 + 5) {
                        this.n[6] = this.positionY + 160 - 38 - 26 + 5;
                     }

                     this.n[7] = this.fu;
                     this.n[8] = this.n[6] + 38 - Game.i(5, 10);
                     if (this.n[8] > this.positionY + 160 - 26) {
                        this.n[8] = this.positionY + 160 - 26;
                     }

                     this.n[9] = this.fv;
               }
            }
            break;
         case 94:
            this.av = var1[5];
            this.facingLeft = (this.c & 1) != 0;
            this.O = true;
            this.P = true;
            this.G = this.facingLeft ? -this.av : this.av;
            break;
         case 95:
            this.b(0);
            this.av = var1[4];
            this.az = this.av % 2 == 0 ? this.positionY : this.positionX;
            this.ay = var1[10];
            break;
         case 96:
            this.ap = 102;
            break;
         case 99:
            this.O = true;
            this.P = true;
            this.aI = var1[6];
            this.as = this.B(0);
            this.av = this.B(1);
            this.ay = this.positionX;
            this.az = this.positionY;
            this.fw = this.B(2);
            this.ap = 302;
            if (this.fw != 0) {
               this.d();
            }
            break;
         case 100:
            this.aI = var1[11];
            this.as = this.B(0);
            this.O = true;
            this.P = true;
            this.ap = 50;
      }

      if (this.type != 93) {
         this.g();
      }
   }

   public final void n() {
      if (this.m == null) {
         this.m = new int[4];
      }

      this.p.a(this.m, this.animationId, this.h, -1, this.c, 0, 0);
      this.m[0] = this.m[0] + this.positionX;
      this.m[1] = this.m[1] + this.positionY;
      this.m[2] = this.m[2] + this.positionX;
      this.m[3] = this.m[3] + this.positionY;
   }

   public final void o() {
      this.g();
      if (this.m == null) {
         this.m = new int[4];
      }

      this.m[0] = this.collisionBounds[0] - 120;
      this.m[1] = this.az;
      this.m[2] = this.collisionBounds[2] + 120;
      this.m[3] = this.collisionBounds[3];
   }

   private void aG() {
      while (true) {
         this.H = 256;
         this.b(false, true);
         this.H = 0;
         if (this.bL) {
            return;
         }

         this.positionY += 20;
      }
   }

   public final void a(short[] var1) {
      if (this.type == 61 || this.type == 79 || this.type == 47) {
         this.a(this.collisionBounds, false);
      }
   }

   final void c(int var1, int var2) {
      switch (var1) {
         case 0:
            if (var2 != 0) {
               this.ap += var2;
               return;
            }
            break;
         case 1:
            if (var2 == 1) {
               this.c &= -33;
               return;
            }

            if (var2 == 0) {
               this.c |= 32;
               return;
            }

            if (var2 == 2) {
               this.c |= 16;
               return;
            }
            break;
         case 2:
            Game.e(this);
      }
   }

   private void aH() {
      this.g();
      if (this.av == 0) {
         this.av = 1;
      } else {
         this.av = 0;
      }

      byte var3 = 0;
      byte var4;
      int var6;
      if (this.facingLeft) {
         var3 = 1;
         var6 = this.positionX + 40 - Game.cameraX;
         var4 = -20;
      } else {
         var6 = this.positionX + 20 - Game.cameraX;
         var4 = 20;
      }

      Game.b[40].z = 0;

      for (int var1 = 0; var1 < 2; var1++) {
         int var8 = this.positionY - Game.cameraY + 10 - var1 * 2 << 8;
         int var5 = 20480 / (4 + var1);

         for (int var2 = 0; var2 < 4 + var1; var2++) {
            int var7 = var8 >> 8;
            Game.b[40].a(bt, 1, (var1 + var2 + this.av) % 2, var6 + Game.i(-5, 5), var7, var3, 0, 0);
            var8 += var5;
         }

         var6 += var4;
      }
   }

   private void aI() {
      int var1 = 0;
      int var2 = 0;
      switch (this.aC % 4) {
         case 0:
            var1 = this.positionX;
            var2 = this.positionY;
            if (this.aC <= 3) {
               var2 -= 20;
            }
            break;
         case 1:
            var1 = this.positionX + 10;
            var2 = this.positionY - 4;
            if (this.aC <= 3) {
               var1 += 20;
            }
            break;
         case 2:
            var1 = this.positionX;
            var2 = this.positionY + 20;
            if (this.aC <= 3) {
               var2 += 40;
            }
            break;
         case 3:
            var1 = this.positionX - 10;
            var2 = this.positionY - 4;
            if (this.aC <= 3) {
               var1 -= 20;
            }
      }

      var1 -= Game.cameraX;
      var2 -= Game.cameraY;
      byte var4;
      byte var5;
      byte var6;
      if (this.aC % 2 == 0) {
         var4 = 26;
         var5 = 25;
         var6 = 39;
      } else {
         var4 = 23;
         var5 = 22;
         var6 = 29;
      }

      for (int var3 = 1; var3 <= this.ax; var3++) {
         if (var3 <= this.fv) {
            if (this.n[var3 - 1] <= 7) {
               Game.b[46].a(bt, var4, this.n[var3 - 1] - 1, var1, var2, 0, 0, 0);
               this.n[var3 - 1]++;
            }
         } else {
            if (var3 > this.fu) {
               break;
            }

            if (this.n[var3 - 1] <= 4) {
               Game.b[46].a(bt, var5, this.n[var3 - 1] - 1, var1, var2, 0, 0, 0);
               this.n[var3 - 1]++;
            } else {
               Game.b[46].a(bt, var6, var1, var2, 0, 0, 0);
            }
         }

         var1 += this.aw;
         var2 += this.az;
      }

      if (this.v != null) {
         this.v.p.z = this.v.aL;
         if (this.v.h > this.v.p.a(this.v.animationId)) {
            this.v.h = 0;
         }

         this.v.b(Game.cameraX, Game.cameraY);
      }
   }

   final void a(int var1, int var2, int var3, int var4) {
      int var5 = var1 - Game.cameraX;
      int var6 = var2 - Game.cameraY;
      int var7 = var5 + var3;
      int var8 = var6 + var4;
      int var9 = Math.max(var5, 0);
      int var10 = Math.min(240, var7) - var9;
      var10 = Math.min(240, var10);
      int var11 = Math.max(var6, 0);
      int var12 = Math.min(320, var8) - var11;
      var12 = Math.min(320, var12);
      if (var9 <= 240 && var11 <= 340 && var10 > 0 && var12 > 0) {
         boolean var14 = false;
         boolean var15 = false;
         if (1 != var12) {
            Game.a(var9, var11 + 1, var10, var12 - 1);
            Game.a(Game.j, 0, 0, 20);
            Game.a(Game.j, 120, 160, 20);
            Game.a(Game.j, 120, 0, 20);
            Game.a(Game.j, 0, 160, 20);
            Game.a(0, 0, 240, 320);
         }

         Game.graphics.setColor(-1140850689);
         int var16 = 0;

         while (var16 < var10 - 3) {
            int var17 = Game.i(1, 10);
            Game.d(var9 + var16, var11, var9 + var16 + var17, var11);
            var16 += var17 + 20;
         }

         for (int var20 = g(var5, var9 - l(var5) % 20); var20 < var9 + var10; var20 += 20) {
            if (var20 + 20 > var9 + var10) {
               Game.a(var20, 0, var9 + var10 - var20, 320);
            }

            Game.b[50].a(bt, 1, Game.stateFrame % 3, var20, var2, this.c, 0, Game.cameraY);
            if (var20 + 20 > var9 + var10) {
               Game.a(0, 0, 240, 320);
            }
         }
      }
   }

   public final void b(int var1, int var2, int var3, int var4) {
      int var5 = var1 - Game.cameraX;
      int var6 = var2 - Game.cameraY - 0;
      int var7 = var5 + var3;
      int var8 = var6 + var4;
      int var9 = Math.max(var5, 0);
      int var10 = Math.min(240, var7) - var9;
      var10 = Math.min(240, var10);
      int var11 = Math.max(var6, 0);
      int var12 = Math.min(320, var8) - var11;
      var12 = Math.min(320, var12);
      if (var9 <= 240 && var11 <= 340 && var10 > 0 && var12 > 0) {
         bt.setColor(16711680);
         Game.a(0, 0, 240, 320);
         bt.fillRect(var9, var11 + 0, var10, var12);
         Game.b[44].z = this.aL;
         if (Game.state != 14) {
            this.ax++;
         }

         int var13 = g(var5, var9 - l(var5) % 40);
         int var14 = (this.ax >> 1) % Game.b[44].a(4);

         for (int var15 = var13; var15 < var9 + var10; var15 += 40) {
            if (var15 + 40 > var9 + var10) {
               Game.a(var15, 0, var9 + var10 - var15, 320);
            }

            Game.b[44].a(bt, 4, var14, var15 + 20, var2, this.c, 0, Game.cameraY);
            if (var15 + 40 > var9 + var10) {
               Game.a(0, 0, 240, 320);
            }
         }

         Game.a(0, 0, 240, 320);
      }
   }

   public final void c(int var1, int var2, int var3, int var4) {
      this.g();
      bt.setColor(8947848);
      Game.d(var1 - Game.cameraX - 1, var2 - Game.cameraY, var3 - Game.cameraX - 1, var4 - Game.cameraY);
      Game.d(var1 - Game.cameraX + 1, var2 - Game.cameraY, var3 - Game.cameraX + 1, var4 - Game.cameraY);
      bt.setColor(16777215);
      Game.d(var1 - Game.cameraX, var2 - Game.cameraY, var3 - Game.cameraX, var4 - Game.cameraY);
   }

   public final void a(int var1, int var2, int var3, Actor var4) {
      switch (var1) {
         case 4:
         case 42:
            aN = false;
            if (aO != null && aO.type == 39 && aO.animationId >= 42 && aO.animationId <= 47) {
               aO.a(47);
               aO = null;
            }

            if (Game.player.animationId == 12
               || Game.player.animationId >= 38 && Game.player.animationId <= 46
               || Game.player.animationId == 33
               || Game.player.animationId == 32
               || Game.player.animationId == 25
               || Game.player.animationId == 24
               || 3 == Game.be[Game.levelIndex] && bP
               || Game.bJ >= 6 && Game.bJ <= 8) {
               return;
            }

            boolean var5;
            if ((var5 = (this.animationId != 11 || aM) && d(var2)) || var4.type == 10 && var4.B(2) == 0 && var4.aK >= 4) {
               if (this.aq != 0 && this.aq != 2) {
                  if (this.aq == 1) {
                     if (cv == 14) {
                        this.b(8);
                     } else if (cv == 2) {
                        this.b(7);
                     } else if (cv == 15) {
                        this.b(5);
                     } else if (cv == 70 && var4.type == 33 && this.q == var4) {
                        this.b(6);
                        this.g();
                        this.av = var4.collisionBounds[3];
                        this.az = 20;
                        this.positionY = this.av + this.az + (this.collisionBounds[3] - this.collisionBounds[1] >> 1);
                        this.ay = this.positionX;
                        cF = 0;
                        cG = 0;
                        ar = 2;
                        this.G = 0;
                        this.H = 0;
                     }
                  }

                  this.H = 0;
               } else if (var1 == 42) {
                  this.H = -768;
                  this.a(47);
               } else {
                  if (ez) {
                     this.a(51);
                  } else {
                     this.b(11);
                  }

                  e(Game.cb);
               }

               if (var5) {
                  this.a(39, 17, this.aF(), 0, -Game.i(10, 50), 302, Game.random.nextInt() % 2 == 0);
               }

               boolean var6 = false;
               if (var4.type != 42) {
                  if (var4.type == 94) {
                     this.facingLeft = var4.facingLeft;
                  } else if (var4.type == 11) {
                     this.facingLeft = !var4.facingLeft;
                  } else {
                     this.facingLeft = var4.positionX < this.positionX;
                  }

                  if (var4.type == 10 && var4.ar() == 0 && var4.aK < 8) {
                     if (var4.aK == 1) {
                        this.facingLeft = var4.positionX + 15 < this.positionX;
                     } else {
                        this.facingLeft = var4.positionX - 15 < this.positionX;
                     }
                  }

                  if (var3 == 0) {
                     this.G = this.facingLeft ? 3072 : -3072;
                  } else {
                     this.G = this.facingLeft ? -var3 : var3;
                  }

                  if (this.aW()) {
                     this.G = 0;
                  }
               }

               this.I = 1024 * (this.G > 0 ? -1 : 1);
               if (var4.type == 11) {
                  this.I = 1024 * (this.facingLeft ? -1 : 1);
               }

               if (var4.type == 99) {
                  this.G = 0;
                  this.I = 0;
                  return;
               }
            }
            break;
         case 6:
            this.a(var2);
            this.positionX = var3;
            this.positionY = var4.positionY;
            this.G = this.H = this.J = 0;
      }
   }

   public final boolean p() {
      if (3 != Game.be[Game.levelIndex] || !bP) {
         return false;
      }

      if (b(Game.player.collisionBounds, this.collisionBounds)) {
         if (this.type == 16) {
            this.p(105);
         } else {
            this.p(3);
         }

         return true;
      } else {
         return false;
      }
   }

   public final Actor a(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      this.b(var1, var2, var3, var4, var5, var6, var7);
      Game.d(aU);
      return aU;
   }

   public final void b(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      aU = new Actor();
      aU.an = -1;
      aU.aG = -1;
      aU.ak = 1;
      aU.type = var1;
      aU.p = Game.n(var2);
      aU.b(var3);
      if (var1 == 35) {
         aU.ay = 6000;
         aU.aw = 0;
         if ((Game.player.c & 4096) != 0) {
            aU.c |= 4096;
         }

         if (var7) {
            aU.aq = 105;
            aU.aD = 500;
            aU.G = Game.i(2, 4) << 8;
            if (Game.i(0, 2) == 0) {
               aU.G = -aU.G;
            }

            aU.H = Game.i(2, 4) << 8;
            if (Game.i(0, 2) == 0) {
               aU.H = -aU.H;
            }
         }
      }

      aU.ap = var6;
      aU.positionX = this.positionX + var4;
      aU.positionY = this.positionY + var5;
      aU.facingLeft = var7 ? !this.facingLeft : this.facingLeft;
      if (aU.facingLeft) {
         aU.c |= 1;
      } else {
         aU.c &= -2;
      }

      aU.g();
   }

   public static final void q() {
      Game.a(Game.dt, 14, Game.levelIndex);
      if (Game.a(Game.dt, 15) < Game.maxUnlockedLevel) {
         Game.a(Game.dt, 15, Game.maxUnlockedLevel);
      }

      Game.a(Game.dt, 30, (short)ch);
      Game.a(Game.dt, 24, (short)(Game.bh ? 1 : 0));

      for (int var0 = 0; var0 < 2; var0++) {
         Game.a(Game.dt, 32 + 2 * var0, (short)cj[var0]);
         Game.a(Game.dt, 36 + 2 * var0, (short)ck[var0]);
         Game.a(Game.dt, 40 + 2 * var0, (short)cl[var0]);
         Game.a(Game.dt, 44 + 2 * var0, (short)cm[var0]);
      }

      Game.a(Game.dt, 64, (short)Game.maxHealth);
      Game.a(Game.dt, 66, (short)Game.health);
      Game.a(Game.dt, 68, (short)Game.z);
      Game.a(Game.dt, 70, (short)Game.maxMagic);
      Game.a(Game.dt, 72, (short)Game.magic);
      Game.a(Game.dt, 74, (short)Game.C);
      Game.b(Game.dt, 76, Game.D);
      Game.a(Game.dt, 781, (int)(byte)(bu ? 1 : 0));
      Game.a(Game.dt, 782, (int)(byte)(bv ? 1 : 0));
      Game.a(Game.dt, 783, (int)(byte)(bw ? 1 : 0));
      Game.a(Game.dt, 784, (int)(byte)(bx ? 1 : 0));
      Game.a(Game.dt, 785, (int)(byte)(by ? 1 : 0));
      Game.a(Game.dt, 786, (int)(byte)(bz ? 1 : 0));
      Game.a(Game.dt, 787, (int)(byte)(bA ? 1 : 0));
      Game.a(Game.dt, 788, (int)(byte)(bB ? 1 : 0));
      Game.a(Game.dt, 789, (int)(byte)(bC ? 1 : 0));
      Game.a(Game.dt, 790, (int)(byte)(bD ? 1 : 0));
      Game.a(Game.dt, 791, (int)(byte)(bF ? 1 : 0));

      for (int var1 = 0; var1 < 8; var1++) {
         Game.a(Game.dt, 48 + var1 * 2, (short)Game.aY[var1]);
      }

      r();
   }

   public static final void r() {
      Game.cw[Game.levelIndex] = (short)Game.cv;

      for (int var0 = 0; var0 < 11; var0++) {
         Game.a(Game.dt, 90 + 2 * var0, Game.cw[var0]);
      }

      for (int var2 = 0; var2 < 11; var2++) {
         for (int var1 = 0; var1 < 30; var1++) {
            Game.a(Game.dt, 112 + var2 * 30 * 2 + var1 * 2, Game.ct[var2][var1]);
         }
      }
   }

   private void aJ() {
      if (b(this.collisionBounds, Game.player.collisionBounds)) {
         Game.cx = false;
         Game.a(Game.dt, 16, (short)(this.an + 1));
         Game.a(Game.dt, 18, (short)Game.player.positionX);
         Game.a(Game.dt, 20, (short)Game.player.positionY);
         Game.a(Game.dt, 22, (short)(Game.player.facingLeft ? 1 : 0));
         Game.a(Game.dt, 28, (short)cv);
         Game.a(Game.dt, 26, (short)Game.player.aq);
         q();
         Game.e(this);

         for (int var1 = 0; var1 < Game.cc; var1++) {
            Game.a(Game.dt, 1127 + var1, (int)Game.cq[var1]);
         }

         if (Game.levelIndex == 6) {
            for (int var2 = 0; var2 < 3; var2++) {
               if (dU[var2] != null) {
                  bG[var2] = (short)dU[var2].an;
               } else {
                  bG[var2] = -1;
               }

               Game.a(Game.dt, 792 + var2 * 2, bG[var2]);
               if (bG[var2] != -1) {
                  Game.a(dU[var2], var2);
               }
            }

            for (int var3 = 0; var3 < 54; var3++) {
               Game.a(Game.dt, 805 + var3, (int)bH[var3]);
            }

            Game.a(Game.dt, 859, (short)Game.aB);
            Game.a(Game.dt, 861, (short)Game.cameraY);
            Game.a(Game.dt, 863, (short)Game.aC);
         } else if (Game.levelIndex == 4) {
            Game.a(Game.dt, 772, (int)(byte)(bM ? 1 : 0));
            Game.a(Game.dt, 773, (short)Game.bZ.positionX);
            Game.a(Game.dt, 775, (short)Game.bZ.positionY);
            Game.b(Game.dt, 777, Game.bZ.aq);
         }

         if (Game.levelIndex == 9) {
            for (int var4 = 0; var4 < 11; var4++) {
               if (dp[var4] != null) {
                  Game.c(dp[var4], var4);
               }
            }

            if (dp[0] != null) {
               dt = (short)dp[0].an;
               Game.a(Game.dt, 865, (short)dt);
               dw = (short)dp[0].aH;
               Game.a(Game.dt, 871, (short)dw);
               Game.a(Game.dt, 867, (short)du);
               if (dp[0].s != null) {
                  dv = dp[0].s.an;
               }

               Game.a(Game.dt, 869, (short)dv);
               Game.a(Game.dt, 873, (short)Game.ac);
               Game.a(Game.dt, 875, (short)Game.ad);
               Game.a(Game.dt, 877, (short)Game.aE);
               Game.h(dp[0]);

               for (int var5 = 0; var5 < dy.length; var5++) {
                  Game.a(Game.dt, 879 + var5, (int)dy[var5]);
               }
            }

            for (int var6 = 0; var6 < 220; var6++) {
               Game.a(Game.dt, 907 + var6, (int)dx[var6]);
            }

            Game.a(Game.dt, 859, (short)Game.aB);
         }

         for (int var7 = 0; var7 < 3; var7++) {
            Game.a(Game.dt, 802 + var7, (int)ej[var7]);
         }

         Game.accessSaveRecord(true);
      }
   }

   private void aK() {
      int var1 = this.aq == 0 ? 0 : 1;
      if (b(Game.player.collisionBounds, this.m) || a(this.m, Game.player.collisionBounds)) {
         switch (this.aC) {
            case 59:
               Game.player.fu = 1 * var1;
               Game.player.I += 204 * var1;
               break;
            case 60:
               Game.player.fu = -1 * var1;
               Game.player.I += -204 * var1;
               break;
            case 61:
               Game.player.fv = -1 * var1;
               Game.player.J += 1024 * var1;
               break;
            case 62:
               Game.player.fv = 1 * var1;
               Game.player.J += -1024 * var1;
         }
      }

      switch (this.aq) {
         case 0:
            this.cV();
            if (this.aA <= 0) {
               this.aq = 101;
               this.c &= -129;
               this.c &= -65;
               this.h = 0;
               this.i = 0;
               return;
            }
            break;
         case 101:
            if (this.e()) {
               this.aq = 0;
               this.aA = this.av;
               this.c |= 128;
               this.c |= 64;
            }

            if (b(Game.player.collisionBounds, this.m) || a(this.m, Game.player.collisionBounds)) {
               if (l(Game.player.G) > 1024) {
                  Game.player.G = (Game.player.G > 0 ? 1 : -1) * 1024;
                  if ((Game.player.I ^ Game.player.G) < 0) {
                     Game.player.I = 0;
                  }
               }

               if (l(Game.player.H) > 768) {
                  Game.player.H = (Game.player.H > 0 ? 1 : -1) * 768;
               }
            }
      }
   }

   private void q(Actor var1) {
      this.aq = 101;
      this.a(39, 17, 35, 0, 0, 302, false);
      aU.c |= 16;
      this.aA = 5000;
      aU.q = this;
      if (var1 != null) {
         aU.r = var1.r;
      } else {
         aU.r = this;
      }

      switch ((this.aC - 27) % 4) {
         case 0:
            aU.G = 0;
            aU.H = -1280;
            aU.positionY -= 20;
            break;
         case 1:
            aU.G = 1280;
            aU.H = 0;
            aU.positionX += 20;
            break;
         case 2:
            aU.G = 0;
            aU.H = 1280;
            aU.positionY += 20;
            break;
         case 3:
            aU.G = -1280;
            aU.H = 0;
            aU.positionX -= 20;
      }

      aU.aC = 88;
      aU.fx = true;
   }

   private void aL() {
      switch (this.aq) {
         case 0:
            if (this.animationId >= 44 && this.animationId <= 47 && this.e()) {
               this.a(this.aC);
            }

            if (this.aH == 35) {
               this.cW();
               if (this.aB <= 0) {
                  this.aB = 1000;
                  this.aC++;
                  if (this.aC > 30) {
                     this.aC = 27;
                  }

                  this.a(this.aC);
               }
            }

            if (b(this.collisionBounds, Game.player.k) && this.aC >= 27 && this.aC <= 30 && ch == 0) {
               this.a(this.aC + 9);
               this.q(null);
               return;
            }
            break;
         case 101:
            this.cV();
            if (this.animationId >= 36 && this.animationId <= 39 && this.e()) {
               this.a(this.aC + 13);
            }

            if (this.aA <= 0) {
               this.aq = 0;
               if (this.animationId > 35) {
                  this.a(this.aC + 17);
               }
            }
      }
   }

   private void aM() {
      if (ey == null) {
         ey = new int[4];
      }

      switch (this.aC % 4) {
         case 0:
            ey[1] = this.positionY - 40;
            ey[3] = this.positionY - 20;
            ey[0] = this.positionX - 10;
            ey[2] = this.positionX + 10;
            if (this.aC > 3) {
               ey[1] = ey[1] + 20;
               ey[3] = this.positionY;
               return;
            }
            break;
         case 1:
            ey[1] = this.positionY;
            ey[3] = this.positionY + 10;
            ey[0] = this.positionX + 20;
            ey[2] = this.positionX + 40;
            if (this.aC > 3) {
               ey[0] = ey[0] - 20;
               ey[2] = ey[2] - 20;
               return;
            }
            break;
         case 2:
            ey[1] = this.positionY + 20;
            ey[3] = this.positionY + 40;
            ey[0] = this.positionX - 10;
            ey[2] = this.positionX + 10;
            if (this.aC > 3) {
               ey[1] = this.positionY;
               ey[3] = ey[3] - 20;
               return;
            }
            break;
         case 3:
            ey[1] = this.positionY;
            ey[3] = this.positionY + 10;
            ey[0] = this.positionX - 40;
            ey[2] = this.positionX - 20;
            if (this.aC > 3) {
               ey[0] = ey[0] + 20;
               ey[2] = this.positionX;
            }
      }
   }

   private void f(boolean var1) {
      if ((this.fu != 0 || this.fv != 0) && this.aq != 102 && this.n != null) {
         if (this.aq != 0) {
            this.aq = 102;
         }

         for (int var2 = this.fv - 1; var2 < this.fu; var2++) {
            if (var2 < this.ax && var2 >= 0) {
               this.n[var2] = 1;
               this.aM();
               ey[1] = ey[1] + var2 * this.az;
               ey[3] = ey[3] + var2 * this.az;
               ey[0] = ey[0] + var2 * this.aw;
               ey[2] = ey[2] + var2 * this.aw;
               this.a(ey, true);
            }
         }

         this.fv = this.fu;
         this.fu = 0;
      }

      if (var1 && this.v != null) {
         this.v.f(true);
      }
   }

   private void aN() {
      if (this.aC >= 0 && this.aC <= 3 && (this.aq == 0 || this.aq == 102)) {
         this.aq = 101;
         this.a(this.aC + 9);
         this.aA = 0;
         this.aB = this.ay;
         this.as = 0;
         this.fu = 0;
         this.fv = 0;
         this.f(true);
      }
   }

   private void aO() {
      if (this.v == null) {
         if (this.B != -1) {
            this.v = Game.i(this.B);
         }

         this.as = 0;
         if (this.aC < 8) {
            switch (this.aC % 4) {
               case 0:
                  this.aw = 0;
                  this.az = -20;
                  this.ax = (this.positionY - this.v.positionY) / 20;
                  if (this.aC <= 3) {
                     this.ax--;
                  }
                  break;
               case 1:
                  this.aw = 20;
                  this.az = 0;
                  this.ax = (this.v.positionX - this.positionX) / 20;
                  if (this.aC <= 3) {
                     this.ax--;
                  }

                  this.positionY += 4;
                  if (this.v != null && this.v.aC == 8) {
                     this.v.positionY += 4;
                  }
                  break;
               case 2:
                  this.aw = 0;
                  this.az = 20;
                  this.ax = (this.v.positionY - this.positionY) / 20;
                  if (this.aC <= 3) {
                     this.ax--;
                  }
                  break;
               case 3:
                  this.aw = -20;
                  this.az = 0;
                  this.ax = (this.positionX - this.v.positionX) / 20;
                  if (this.aC <= 3) {
                     this.ax--;
                  }

                  this.positionY += 4;
                  if (this.v != null && this.v.aC == 8) {
                     this.v.positionY += 4;
                  }
            }

            this.n = new int[this.ax];
         } else {
            this.aw = 0;
            this.az = 0;
            this.ax = 0;
         }
      }

      this.g();
      switch (this.aq) {
         case 0:
            this.fv = 1;
            this.fu = this.ax;
            this.f(false);
            this.fv = 0;
            this.fu = 0;
            this.aq = 102;
         case 102:
            if (this.animationId >= 17 && this.animationId <= 20 && this.e()) {
               this.a(this.aC);
            }

            if (this.n != null && this.n[this.ax - 1] == 6) {
               this.fu = 0;
               this.fv = 0;
            }

            if (this.f(this.collisionBounds) && ch == 1) {
               this.aN();
               return;
            }
            break;
         case 101:
            if (this.animationId >= 9 && this.animationId <= 12 && this.e()) {
               this.a(this.aC + 13);
            }

            if (this.aA <= 0) {
               this.aA = this.av;
               if (this.fu < this.ax) {
                  this.n[this.fu] = 1;
                  this.aM();
                  ey[1] = ey[1] + this.fu * this.az;
                  ey[3] = ey[3] + this.fu * this.az;
                  ey[0] = ey[0] + this.fu * this.aw;
                  ey[2] = ey[2] + this.fu * this.aw;
                  switch (this.aC % 2) {
                     case 0:
                        this.a(ey, false);
                        break;
                     case 1:
                        this.a(ey, 15);
                  }

                  this.fu++;
               } else if (this.fu == this.ax) {
                  this.fu++;
                  if (this.v != null) {
                     this.v.aq = 101;
                     this.v.aA = 0;
                     this.v.aB = this.ay;
                     this.v.fu = 0;
                     this.v.fv = 0;
                     this.v.as = 0;
                  }
               }

               if (this.aB <= 0 && this.as == 0 || this.as == 1) {
                  if (this.as == 0) {
                     this.as = 1;
                  }

                  if (this.fv < this.ax) {
                     this.n[this.fv] = 1;
                     this.aM();
                     ey[1] = ey[1] + this.fv * this.az;
                     ey[3] = ey[3] + this.fv * this.az;
                     ey[0] = ey[0] + this.fv * this.aw;
                     ey[2] = ey[2] + this.fv * this.aw;
                     this.a(ey, true);
                     this.fv++;
                  } else if (this.fv == this.ax) {
                     this.fv++;
                     this.aq = 102;
                     if (this.animationId > 8) {
                        this.a(this.aC + 17);
                     }
                  }
               }
            } else {
               this.cV();
            }

            if (this.aB > 0 && this.as == 0) {
               this.cW();
            }
      }
   }

   public final boolean s() {
      boolean var1 = false;
      if (Game.state != 14 && Game.state != 32) {
         if (Game.modalActive) {
            switch (Game.bJ) {
               case 5:
               case 16:
                  var1 = false;
                  break;
               case 6:
               case 7:
               case 8:
               case 10:
               case 11:
               default:
                  if (this.type != 0 && this.type != 48 && this.type != 39 && this.type != 22 && this.type != 62) {
                     var1 = true;
                  }
                  break;
               case 9:
               case 12:
                  var1 = true;
                  if (this.type == 83 || this.type == 84 || this.type == 85) {
                     var1 = false;
                  }
                  break;
               case 13:
                  if (this == Game.player || this == Game.cb || this == E) {
                     var1 = true;
                  }
                  break;
               case 14:
                  var1 = true;
                  break;
               case 17:
                  var1 = true;
               case 15:
                  if (this.type != 48 && this.type != 39 && this.type != 0 && this.type != 35 && this.type != 71) {
                     var1 = true;
                  }
            }
         }

         return var1;
      } else {
         return true;
      }
   }

   public final void t() {
      if (!this.s()) {
         if (aT == this) {
            Game.player.positionX = Game.player.positionX + (this.G >> 8);
            Game.player.positionY = Game.player.positionY + (this.H >> 8);
         }

         if (Game.player.aq == 1 && ar == 2 && cv == 70 && Game.player.q == this) {
            Game.player.ay = Game.player.ay + (this.G >> 8);
            Game.player.av = Game.player.av + (this.H >> 8);
            Game.player.positionX = Game.player.positionX + (this.G >> 8);
            Game.player.positionY = Game.player.positionY + (this.H >> 8);
         }

         if ((this.c & 1024) == 0 && (this.c & 16384) == 0) {
            this.at = true;
            if (aQ) {
               this.a = this.a + (this.positionX - (this.a >> 8) << 8);
               this.b = this.b + (this.positionY - (this.b >> 8) << 8);
               if (this.M) {
                  bJ = this.G + (fs << 8);
               } else {
                  bJ = this.G;
               }

               if (aR == 0) {
                  this.a = this.a + bJ;
                  this.b = this.b + this.H;
               } else {
                  this.a = this.a + bJ / aS;
                  this.b = this.b + this.H / aS;
               }

               if (bI >= 0 && bI % aS == aS - 1) {
                  this.G = this.G + this.I;
                  this.H = this.H + this.J;
               }

               this.I = 0;
               this.J = 0;
               this.positionX = this.a >> 8;
               this.positionY = this.b >> 8;
            } else {
               this.a = this.a + (this.positionX - (this.a >> 8) << 8);
               this.b = this.b + (this.positionY - (this.b >> 8) << 8);
               int var1 = 0;
               int var2 = 0;
               boolean var3 = true;
               if (this.type == 0) {
                  if (this.animationId == 5 || this.animationId == 6 || this.animationId == 7 || aX() || this.aq == 2 || Game.player.q != null || aT != null) {
                     var3 = false;
                  }
               } else if (aT == this || Game.player.q == this || this.type == 82) {
                  var3 = false;
               } else if (this.type == 83) {
                  var3 = false;
               } else if (this.type == 94) {
                  var3 = false;
               }

               if (var3) {
                  var1 = Game.p(this.G, Game.g);
                  var2 = Game.p(this.H, Game.g);
                  if (this.M) {
                     bJ = var1 + (fs << 8);
                  } else {
                     bJ = var1;
                  }

                  this.a = this.a + bJ;
                  this.b += var2;
               } else {
                  if (this.M) {
                     bJ = this.G + (fs << 8);
                  } else {
                     bJ = this.G;
                  }

                  this.a = this.a + bJ;
                  this.b = this.b + this.H;
               }

               if (var3) {
                  this.J = Game.p(Game.p(this.J, Game.g), Game.g);
                  this.I = Game.p(Game.p(this.I, Game.g), Game.g);
               }

               this.G = this.G + this.I;
               this.H = this.H + this.J;
               this.I = 0;
               this.J = 0;
               this.positionX = this.a >> 8;
               this.positionY = this.b >> 8;
            }

            if (this.aD >= 0) {
               if (aQ) {
                  this.aD = this.aD - Game.h / aS;
               } else {
                  this.aD = this.aD - Game.h;
               }
            }

            switch (this.type) {
               case -102:
                  this.O();
               case -101:
               case -99:
               case -98:
               case -97:
               case -96:
               case -95:
               case -94:
               case -93:
               case -92:
               case -91:
               case -90:
               case -89:
               case -88:
               case -87:
               case -86:
               case -85:
               case -84:
               case -83:
               case -82:
               case -81:
               case -80:
               case -79:
               case -78:
               case -77:
               case -76:
               case -75:
               case -74:
               case -73:
               case -72:
               case -71:
               case -70:
               case -69:
               case -68:
               case -67:
               case -66:
               case -65:
               case -64:
               case -63:
               case -62:
               case -61:
               case -60:
               case -59:
               case -58:
               case -57:
               case -56:
               case -55:
               case -54:
               case -52:
               case -51:
               case -50:
               case -49:
               case -48:
               case -47:
               case -46:
               case -45:
               case -44:
               case -43:
               case -42:
               case -39:
               case -38:
               case -36:
               case -35:
               case -34:
               case -33:
               case -32:
               case -31:
               case -30:
               case -29:
               case -28:
               case -27:
               case -26:
               case -25:
               case -24:
               case -23:
               case -22:
               case -21:
               case -20:
               case -19:
               case -18:
               case -17:
               case -16:
               case -15:
               case -14:
               case -13:
               case -12:
               case -11:
               case -10:
               case -9:
               case -8:
               case -7:
               case -6:
               case -5:
               case -4:
               case -3:
               case -2:
               case -1:
               case 6:
               case 7:
               case 12:
               case 17:
               case 24:
               case 25:
               case 26:
               case 27:
               case 29:
               case 32:
               case 34:
               case 36:
               case 43:
               case 44:
               case 47:
               case 57:
               case 60:
               case 62:
               case 63:
               case 66:
               case 67:
               case 76:
               case 77:
               case 84:
               case 85:
               case 87:
               case 93:
               case 97:
               default:
                  break;
               case -100:
                  this.bK();
                  break;
               case -53:
                  this.dj();
                  break;
               case -41:
                  this.ci();
                  break;
               case -40:
                  this.bx();
                  break;
               case -37:
                  this.bw();
                  break;
               case 0:
                  this.ba();
                  if (Game.modalActive && Game.bJ != 16) {
                     this.db();
                  }
                  break;
               case 1:
                  this.bv();
                  break;
               case 2:
                  this.bs();
                  break;
               case 3:
                  this.bu();
                  break;
               case 4:
                  this.bB();
                  break;
               case 5:
                  this.bC();
                  break;
               case 8:
                  this.bD();
                  break;
               case 9:
                  this.bE();
                  break;
               case 10:
                  this.bG();
                  break;
               case 11:
                  this.bH();
                  break;
               case 13:
                  this.bJ();
                  break;
               case 14:
                  this.bL();
                  break;
               case 15:
                  this.ch();
                  break;
               case 16:
                  this.dk();
                  break;
               case 18:
                  this.cn();
                  break;
               case 19:
                  this.cl();
                  break;
               case 20:
                  this.do_();
                  break;
               case 21:
                  this.dq();
                  break;
               case 22:
                  this.br();
                  break;
               case 23:
                  this.aJ();
                  break;
               case 28:
                  this.az();
                  break;
               case 30:
                  this.cq();
                  break;
               case 31:
                  this.ct();
                  break;
               case 33:
                  this.af();
                  break;
               case 35:
                  this.df();
                  break;
               case 37:
                  this.dp();
                  break;
               case 38:
                  this.cK();
                  break;
               case 39:
                  if ((this.animationId < 37 || this.animationId > 41) && (this.animationId < 42 || this.animationId > 47)) {
                     this.dh();
                  } else {
                     this.bI();
                  }
                  break;
               case 40:
                  this.cw();
                  break;
               case 41:
                  this.de();
                  break;
               case 42:
                  this.bg();
                  break;
               case 45:
                  this.cu();
                  break;
               case 46:
                  this.bh();
                  break;
               case 48:
                  this.cv();
                  break;
               case 49:
                  this.cx();
                  break;
               case 50:
                  this.cH();
                  break;
               case 51:
                  this.cR();
                  break;
               case 52:
                  this.ay();
                  break;
               case 53:
                  this.ai();
                  break;
               case 54:
                  this.cS();
                  break;
               case 55:
                  this.ak();
                  break;
               case 56:
                  this.cT();
                  break;
               case 58:
                  this.cU();
                  break;
               case 59:
                  this.cM();
                  break;
               case 61:
                  if (this.animationId < 13) {
                     this.cO();
                  } else if (this.animationId >= 16 && this.animationId <= 18) {
                     this.cV();
                     this.m(0);
                     if (this.aA < 0) {
                        this.a(0);
                     }
                  }
                  break;
               case 64:
                  this.cG();
                  break;
               case 65:
                  this.cF();
                  break;
               case 68:
                  this.bM();
                  break;
               case 69:
                  this.aZ();
                  break;
               case 70:
                  this.by();
                  break;
               case 71:
                  this.S();
                  break;
               case 72:
                  this.cz();
                  break;
               case 73:
                  this.cB();
                  break;
               case 74:
                  this.cE();
                  break;
               case 75:
                  this.cQ();
                  break;
               case 78:
                  this.cr();
                  break;
               case 79:
                  this.cN();
                  break;
               case 80:
                  this.cs();
                  break;
               case 81:
                  this.aO();
                  break;
               case 82:
                  this.ae();
                  break;
               case 83:
                  this.bT();
                  break;
               case 86:
                  this.bA();
                  break;
               case 88:
                  this.aL();
                  break;
               case 89:
                  this.V();
                  break;
               case 90:
                  this.bQ();
                  break;
               case 91:
                  this.aK();
                  break;
               case 92:
                  this.cj();
                  break;
               case 94:
                  this.aR();
                  break;
               case 95:
                  this.u();
                  break;
               case 96:
                  this.di();
                  break;
               case 98:
                  this.cA();
                  break;
               case 99:
                  this.aQ();
                  break;
               case 100:
                  this.bS();
            }

            if (this.o) {
               this.g();
            }

            if (this.facingLeft) {
               this.c |= 1;
            } else {
               this.c &= -2;
            }

            if (3 != Game.be[Game.levelIndex] && this != Game.player) {
               this.bP();
            }
         }
      }
   }

   private void aP() {
      int var5 = Game.player.positionX - Game.cameraX < 120 ? 1 : -1;
      int var1 = Game.player.positionX - 100;
      int var2 = Game.player.positionX + 100;
      int var3 = Game.player.positionY + 60;
      int var4 = Game.player.positionY - 60 - 60;
      this.fv = Game.aJ.collisionBounds[0] + 20;
      this.fu = Game.aJ.collisionBounds[2] - 20;
      if (var1 <= this.fv) {
         var1 = this.fv;
      }

      if (var2 >= this.fu) {
         var2 = this.fu;
      }

      this.fv = this.ay - 200;
      this.fu = this.ay + 200;
      if (var1 <= this.fv) {
         var1 = this.fv;
      }

      if (var2 >= this.fu) {
         var2 = this.fu;
      }

      this.fv = Game.aJ.collisionBounds[1];
      this.fu = Game.aJ.collisionBounds[3];
      if (var4 <= this.fv) {
         var4 = this.fv;
      }

      if (var3 >= this.fu) {
         var3 = this.fu;
      }

      int var6 = Game.player.positionX;
      int var7 = Game.player.positionY;
      if (var6 - var1 > var2 - var6) {
         var5 = -1;
      } else if (var2 - var6 > var6 - var1) {
         var5 = 1;
      }

      var1 /= 20;
      var2 /= 20;
      var6 /= 20;
      var4 /= 20;
      var3 /= 20;
      var7 /= 20;
      int var8 = var5 == 1 ? var2 : var1;
      int var9 = 0;
      int var11 = 0;

      for (int var10 = 0; var10 < 2; var10++) {
         var11 = 0;

         for (var9 = var7; var9 >= var4; var9--) {
            if (Game.g(var8, var9) < 15) {
               var11++;
            } else {
               var11 = 0;
            }

            if (var11 >= 3) {
               break;
            }
         }

         if (var11 >= 3) {
            break;
         }

         var11 = 0;

         for (var9 = var3; var9 >= var7 + 1; var9--) {
            if (Game.g(var8, var9) < 15) {
               var11++;
            } else {
               var11 = 0;
            }

            if (var11 >= 3) {
               break;
            }
         }

         if (var11 >= 3) {
            break;
         }

         var8 = var5 == -1 ? var2 : var1;
      }

      if (var11 >= 3) {
         this.positionX = var8 * 20 + 10;
         this.positionY = var9 * 20 + 20;
         this.aG();
      } else {
         int var13 = -1;
         int var14 = -1;
         var11 = 0;

         for (int var21 = -1; var21 < 2; var21 += 2) {
            for (int var12 = (var8 = var5 == var21 ? var1 : var2) == var1 ? 1 : -1; var8 != var6; var8 += var12) {
               var11 = 0;

               for (var9 = var7; var9 >= var4; var9--) {
                  if (Game.g(var8, var9) < 15) {
                     var11++;
                  } else {
                     var11 = 0;
                  }

                  if (var11 >= 3) {
                     break;
                  }
               }

               if (var11 >= 3) {
                  break;
               }

               var11 = 0;

               for (var9 = var3; var9 >= var7 + 1; var9--) {
                  if (Game.g(var8, var9) < 15) {
                     var11++;
                  } else {
                     var11 = 0;
                  }

                  if (var11 >= 3) {
                     break;
                  }
               }

               if (var11 >= 3) {
                  if (var21 == -1) {
                     var13 = var8;
                     var14 = var9;
                  }
                  break;
               }
            }
         }

         if (var11 >= 3) {
            if (l(var8 - var6) < l(var13 - var6)) {
               var8 = var13;
               var9 = var14;
            }

            this.positionX = var8 * 20 + 10;
            this.positionY = var9 * 20 + 20;
            this.aG();
         }
      }
   }

   private void aQ() {
      switch (this.aq) {
         case 0:
            if (l(this.positionX - Game.player.positionX) <= 140 && l(this.positionY - Game.player.positionY) <= 100) {
               this.p(101);
            }
            break;
         case 2:
            if (this.e() && this.aD < 0) {
               this.p(106);
            }
            break;
         case 3:
            this.a(39, 17, 7, 0, -30, 302, this.facingLeft);
            this.b(true);
            Game.e(this.r);
            this.r = null;
            break;
         case 9:
            this.a(39, 17, 7, 0, -30, 302, this.facingLeft);
            this.a(true, true);
            Game.e(this.r);
            this.r = null;
            break;
         case 101:
            if (this.e()) {
               this.p(102);
            }
            break;
         case 102:
            if (this.r.e()) {
               if (this.r.animationId == 72) {
                  this.r.a(73);
                  this.positionY = Game.player.positionY;
                  this.positionX = Game.player.positionX;
                  this.facingLeft = Game.player.facingLeft;
                  this.positionX = this.positionX + (Game.player.facingLeft ? 1 : -1) * 10;
                  this.fv = this.ay - 200;
                  this.fu = this.ay + 200;
                  if (this.positionX >= this.fv && this.positionX <= this.fu) {
                     this.aG();
                  } else {
                     this.positionX = this.ay;
                     this.positionY = this.az;
                     this.p(0);
                  }

                  this.r.positionX = this.positionX;
                  this.r.positionY = this.positionY;
                  this.r.facingLeft = this.facingLeft;
               } else {
                  this.p(103);
               }
            }
            break;
         case 103:
            if (this.e()) {
               this.g();
               if (!b(Game.player.collisionBounds, this.collisionBounds) && !a(this.collisionBounds, Game.player.collisionBounds)) {
                  this.p(105);
               } else {
                  this.p(104);
               }
            }
            break;
         case 104:
            Game.player.a(4, this.av, 0, this);
            Game.player.positionY = this.positionY;
            Game.player.positionX = this.positionX + (this.facingLeft ? -1 : 1) * 10;
            Game.player.facingLeft = !this.facingLeft;
            if (Game.G(65568)) {
               this.aB++;
            }

            if (this.aB >= 6 || Game.health <= 0) {
               this.p(106);
            }
            break;
         case 105:
            if (this.e()) {
               this.p(106);
            }
            break;
         case 106:
            if (this.r.e()) {
               if (this.r.animationId == 72) {
                  this.facingLeft = Game.player.facingLeft;
                  this.aP();
                  this.r.a(73);
                  this.r.positionX = this.positionX;
                  this.r.positionY = this.positionY;
                  this.r.facingLeft = this.facingLeft;
               } else {
                  this.p(0);
               }
            }
      }

      if (!this.c()) {
         this.aE();
      }
   }

   public final void u() {
      switch (this.aq) {
         case 0:
            if (b(Game.player.collisionBounds, this.m) || a(this.m, Game.player.collisionBounds)) {
               this.O = true;
               this.P = true;
               this.aq = 101;
               this.aA = 800;
            }
            break;
         case 101:
            this.cV();
            if (this.aA <= 0) {
               this.aq = 102;
               this.aw = 0;
               switch (this.av) {
                  case 0:
                     this.H = -2048;
                     break;
                  case 1:
                     this.G = 2048;
                     break;
                  case 2:
                     this.H = 2048;
                     break;
                  case 3:
                     this.G = -2048;
               }
            }
            break;
         case 102:
            this.aw += 2048;
            if (this.aw >= this.ay << 8) {
               switch (this.av) {
                  case 0:
                     Game.e(12);
                     this.H = 768;
                     break;
                  case 1:
                     Game.a(12, 0);
                     this.G = -768;
                     break;
                  case 2:
                     Game.e(12);
                     this.H = -768;
                     break;
                  case 3:
                     Game.a(12, 0);
                     this.G = 768;
               }

               this.aq = 103;
            }
            break;
         case 103:
            switch (this.av) {
               case 0:
                  if (this.positionY >= this.az) {
                     this.positionY = this.az;
                     this.H = 0;
                     this.aq = 0;
                  }
                  break;
               case 1:
                  if (this.positionX <= this.az) {
                     this.positionX = this.az;
                     this.G = 0;
                     this.aq = 0;
                  }
                  break;
               case 2:
                  if (this.positionY <= this.az) {
                     this.positionY = this.az;
                     this.H = 0;
                     this.aq = 0;
                  }
                  break;
               case 3:
                  if (this.positionX >= this.az) {
                     this.positionX = this.az;
                     this.G = 0;
                     this.aq = 0;
                  }
            }
      }

      if (b(this.collisionBounds, Game.player.collisionBounds)) {
         Game.player.a(4, 10, 0, this);
      }

      for (int var1 = 0; var1 < Game.ck; var1++) {
         Actor var2;
         if ((var2 = Game.cj[var1]) != null
            && (var2.c & 1024) == 0
            && var2.aq != 3
            && var2.aq != 7
            && var2.aq != 5
            && var2.aq != 6
            && (
               var2.M() && var2.collisionBounds != null && b(var2.collisionBounds, this.collisionBounds)
                  || var2.type == 70 && b(var2.collisionBounds, this.collisionBounds)
            )) {
            var2.b(10, true);
         }
      }
   }

   private void aR() {
      this.g();
      switch (this.aq) {
         case 0:
            this.aq = 101;
            this.O = true;
            this.P = true;
            break;
         case 3:
            this.b(false);
            return;
         case 101:
            if (this.facingLeft) {
               if (this.collisionBounds[2] < Game.cameraX) {
                  this.aq = 3;
               }
            } else if (this.collisionBounds[0] > Game.cameraX + 240) {
               this.aq = 3;
            }
      }

      if ((this.c & 128) == 0 && this.aq != 0 && this.aq != 3 && b(Game.player.collisionBounds, this.collisionBounds)) {
         Game.player.a(4, 25, -2560, this);
         if (Game.player.facingLeft ? Game.player.eL : Game.player.eM) {
            this.O = false;
            this.P = false;
         }
      }
   }

   public final boolean v() {
      if (Game.player.aq == 1 && Game.bY == this) {
         return true;
      }

      Game.cameraX -= 0;
      Game.cameraY -= 0;
      if (this.type == -2 && this.p == Game.b[17] && (this.animationId == 4 || this.animationId == 5 || this.animationId == 6 || this.animationId == 7)) {
         this.p.z = Game.N ? 1 : 0;
      }

      if (this.type == 8 && this.q != null) {
         if ((this.c & 128) != 0) {
            this.q.c |= 128;
         } else {
            this.q.c &= -129;
         }
      }

      if (this.type == 93) {
         this.bz();
         return true;
      }

      if (this.type == 62 && this.aA > 1) {
         int var1 = this.positionX;

         for (int var2 = 1; var2 < this.aA; var2++) {
            this.positionX = var1 + var2 * this.ay;
            if (this.positionX + 80 >= Game.cameraX && this.positionX <= Game.cameraX + 240) {
               this.b(Game.cameraX, Game.cameraY);
            }
         }

         this.positionX = var1;
      }

      if (this.type == 94 && (this.c & 128) == 0) {
         this.aH();
         return true;
      }

      if (this.type == 81) {
         this.aI();
      }

      if (this.type == 80 && (this.c & 128) == 0) {
         Game.c(this.positionY - Game.cameraY, this.av, this.positionX - Game.cameraX);
         this.positionY = this.positionY + this.av;
         this.b(Game.cameraX, Game.cameraY);
         this.positionY = this.positionY - this.av;
      } else if (this.type == 0 && this.aq != 1) {
         this.p.L = false;
      }

      if (this.type == 0 && bO > 0) {
         bO = bO - Game.h;
         if ((Game.stateFrame & 3) >= 2) {
            this.p.L = true;
            this.p.K = 79;
         } else {
            this.p.L = false;
         }
      }

      if (this.type == 0 && this.aq == 1 && cv == 15) {
         this.p.z = 1;
      } else if (this.type == 54) {
         bt.setColor(0);
         Game.b(this.positionX - Game.cameraX, this.positionY - Game.cameraY, 100, 80);
      } else if (this.p == Game.b[14]) {
         if (this.animationId == 1) {
            this.g();
            if (this.k != null) {
               Game.graphics.setColor(16777215);
               Game.b(this.k[0] - Game.cameraX, this.k[1] - Game.cameraY, this.k[2] - this.k[0], this.k[3] - this.k[1]);
            }
         }
      } else if (this.type == 39) {
         if (this.animationId >= 24 && this.animationId <= 26) {
            this.p.z = this.animationId - 24;
            if (this.aB > 0 && this.animationId == 26) {
               int var9 = this.av;
               if (Game.stateFrame % 2 == 0) {
                  this.av += 4;
               }

               if (this.aB == 10) {
                  Game.b[33].a(bt, 1, this.ax - 10 - 51, this.az - var9 - 29, 0, 0, 0);
                  Game.b[33].a(bt, 12, this.ax, this.az - var9, 0, 0, 0);
                  Game.b[33].a(bt, 3, this.ax + 6 + 1, this.az - var9, 0, 0, 0);
                  Game.b[33].a(bt, 2, this.ax + 12 + 1, this.az - var9, 0, 0, 0);
               } else {
                  Game.b[33].a(bt, 1, this.ax - 10 - 51, this.az - var9 - 29, 0, 0, 0);
                  Game.b[33].a(bt, 12, this.ax, this.az - var9, 0, 0, 0);
                  Game.b[33].a(bt, 7, this.ax + 6 + 1, this.az - var9, 0, 0, 0);
               }
            }
         } else if (this.animationId == 10) {
            this.u(8, this.aF);
         } else if (this.animationId == 58) {
            if (this.ay >= 100) {
               Game.cameraX += 0;
               Game.cameraY += 0;
               return true;
            }

            int var8 = this.positionX - Game.cameraX;
            Game.b[33].a(bt, 13, var8, this.positionY - Game.cameraY, 0, 0, 0);
            if (this.ay / 10 != 0) {
               var8 += 5;
               Game.b[33].a(bt, 2 + this.ay / 10, var8, this.positionY - Game.cameraY, 0, 0, 0);
            }

            Game.b[33].a(bt, 2 + this.ay % 10, var8 + 5, this.positionY - Game.cameraY, 0, 0, 0);
         } else if (this.animationId == 16) {
            Game.b[50].a(bt, 0, 0, this.positionX - Game.cameraX, this.positionY - Game.cameraY, this.c, 0, 0);
         } else if (this.animationId == 4) {
            this.p.z = Game.N ? 1 : 0;
         } else if (this.animationId == 4 || this.animationId == 5 || this.animationId == 6 || this.animationId == 7) {
            this.p.z = Game.N ? 1 : 0;
         }
      } else if (this.type == 71) {
         int var10 = (12 - this.ay) % 6;
         if (12 - this.ay >= 6 && 12 - this.ay < 12) {
            var10 = 6 - var10;
         }

         Game.a(Game.ez[var10 + 4], this.positionX - Game.cameraX - Game.ez[var10 + 4].getWidth() / 2, this.positionY - Game.cameraY + -95, 0);
      } else if (this.type == 38 && this.r == null) {
         if (this.aq != 0) {
            Game.b[20].a(bt, 0, this.positionX - Game.cameraX, this.positionY - Game.cameraY, 0, 0, 0);
         }
      } else if (this.type == 88) {
         this.p.z = 1;
      } else if (this.type == 2
         || this.type == 3
         || this.type == 1
         || this.type == 8
         || this.type == 13
         || this.type == 73
         || this.type == 70
         || this.type == 56
         || this.type == 98
         || this.type == 92
         || this.type == 81
         || this.type == 83
         || this.type == 84
         || this.type == 85
         || this.type == -53
         || this.p == Game.b[48] && this.p != null
         || this.type == 62) {
         this.p.z = this.aL;
      } else if (this.type == 4) {
         this.p.z = this.aB / 3 % 3;
      } else if (this.type == 35) {
         if ((this.aq == 100 || this.aq == 101) && this.animationId < 6) {
            Game.cameraX += 0;
            Game.cameraY += 0;
            return true;
         }
      } else if (this.type == 48 && this.animationId == 4) {
         Game.cameraX += 0;
         Game.cameraY += 0;
         return true;
      }

      if (this.type == 42) {
         this.a(
            this.collisionBounds[0],
            this.collisionBounds[1],
            this.collisionBounds[2] - this.collisionBounds[0],
            this.collisionBounds[3] - this.collisionBounds[1]
         );
         Game.cameraX += 0;
         Game.cameraY += 0;
         return true;
      }

      if (this.type == 78) {
         this.b(
            this.collisionBounds[0],
            this.collisionBounds[1],
            this.collisionBounds[2] - this.collisionBounds[0],
            this.collisionBounds[3] - this.collisionBounds[1]
         );
         Game.cameraX += 0;
         Game.cameraY += 0;
         return true;
      }

      if (this.type == 45) {
         if (101 == this.aq) {
            Game.cameraX += 0;
            Game.cameraY += 0;
            return true;
         }

         int var17 = this.collisionBounds[0];
         int var23 = this.collisionBounds[2] - this.collisionBounds[0];

         for (int var25 = 0; var25 < var23 / 40; var25++) {
            if (this.aq == 0) {
               Game.b[34].a(bt, this.animationId, 0, this.positionX - Game.cameraX + var25 * 40, this.positionY - Game.cameraY, this.c, 0, 0);
            } else {
               int var27 = var17 + 40 * var25;
               int var29 = Math.abs(this.ay - var27) / 40;
               int var31;
               if ((var31 = this.aA / 50 >> 1) - var29 < 0) {
                  Game.b[34].a(bt, this.animationId, 0, this.positionX - Game.cameraX + var25 * 40, this.positionY - Game.cameraY, this.c, 0, 0);
               } else {
                  var31 -= var29;
               }

               int var32 = 0;
               if (var31 >= 4) {
                  var32 = 0 + (var31 - 4) * 20 + 100;
                  var31 = (var31 - 4) % 3 + 2;
               }

               Game.b[34]
                  .a(bt, this.animationId != 7 ? 2 : 8, var31, this.positionX - Game.cameraX + var25 * 40, this.positionY - Game.cameraY + var32, 0, 0, 0);
            }
         }

         Game.cameraX += 0;
         Game.cameraY += 0;
         return true;
      } else {
         if (this.type == 50) {
            int var11 = this.aw - Game.cameraX;
            int var18 = this.positionX - Game.cameraX;
            int var3 = this.positionY - Game.cameraY - 1 + 0;
            int var4 = this.az - Game.cameraY;
            int var5 = this.aA >> 16;
            int var6 = var4 + Math.abs(this.av >> 1);
            int var7 = (var4 << 8) + Math.abs(this.av >> 1) * Game.l(var5);
            if (this.aq >= 102) {
               var3 = var6;
               var18 = var11;
            }

            Game.a(var11, var4, var18, var3, var5, this.aC, var7);
            if (this.aq == 104) {
               Game.cameraX += 0;
               Game.cameraY += 0;
               return true;
            }
         } else if (this.type == 53) {
            int var12 = this.positionX - Game.cameraX + 1;
            int var19 = this.positionX - Game.cameraX + 1;
            int var24 = this.positionY - Game.cameraY;
            int var26;
            int var28;
            int var30 = (var28 = (var26 = this.az - Game.cameraY) + Math.abs(this.av >> 1)) << 8;
            if (this.aq >= 101) {
               var24 = var28;
               var19 = var12;
            }

            Game.a(var12, var26, var19, var24, 0, 0, var30);
            if (this.aq == 102 || this.aG == -2) {
               Game.cameraX += 0;
               Game.cameraY += 0;
               return true;
            }
         } else if (this.type == 31) {
            int var13 = this.aC;
            if (this.aC == 9) {
               var13 = 4;
            }

            int var20 = var13 <= 2 ? this.aw : this.az;
            if (var13 == 8) {
               var20 += 20 * this.aH;
            } else if (var13 == 2) {
               var20 += 20 * this.aH;
            }

            this.u(var13, var20);
         } else if (this.type == 61) {
            if (this.animationId != 0 && this.animationId != 2 && this.animationId < 16) {
               this.p.u = this.n;
               this.p.s = true;
            }
         } else if (this.type == 79) {
            if (this.animationId != 1 && this.animationId != 3) {
               this.p.u = this.n;
               this.p.s = true;
            }
         } else if (this.type == -37) {
            this.p.z = 1;
         }

         if ((this.c & 128) == 0) {
            if (this.type == 5) {
               if (this.ar() == 1) {
                  this.l(this.aK, this.aB);
               } else {
                  this.p.p = -1;
               }
            } else if (this.type == 8) {
               if (this.animationId != 0 && this.ar() == 1) {
                  this.u(8, this.aF);
               }
            } else if (this.type == 9) {
               if (this.ar() == 2 || this.ar() == 3) {
                  this.u(8, this.aB);
               } else if (this.ar() == 0) {
                  this.u(this.facingLeft ? 2 : 1, this.aB);
               }
            } else if (this.type == 37) {
               this.u(8, this.positionY + this.fw - (this.an == -1 ? 115 : 98));
            } else if (this.type == 46 && this.aq == 103) {
               this.u(4, this.az);
            } else if (this.type == 72) {
               this.u(8, this.av);
            } else if (this.type != 70 || this.aH != 3 || this.aq != 115 && this.aq != 114) {
               if (this.type == 86 && this.aq == 0) {
                  return true;
               }

               if (this.type == 15) {
                  this.p.z = 0;
               }
            } else {
               this.c(this.aw, this.az, this.positionX, this.collisionBounds[3] + this.collisionBounds[1] >> 1);
            }

            if (this.p == null) {
               Game.cameraX += 0;
               Game.cameraY += 0;
               return false;
            }

            if (this.h > this.p.a(this.animationId)) {
               this.h = 0;
            }

            if (this.type == 0) {
               if (3 == Game.be[Game.levelIndex] && bP) {
                  Game.a(false, 0);
                  Game.a(false, 1);
                  Game.a(false, 2);
               } else if (this.aq == 1 && cv == 70) {
                  if (ar == 2 || ar == 4) {
                     this.c(this.ay, this.av, this.positionX, this.collisionBounds[3] + this.collisionBounds[1] >> 1);
                  }

                  this.p.z = 0;
               }

               this.b(Game.cameraX, Game.cameraY);
               if (3 == Game.be[Game.levelIndex] && bP) {
                  Game.a(true, 0);
                  Game.a(true, 1);
                  Game.a(true, 2);
               }

               if (cL > 0) {
                  H();
               }
            } else {
               this.b(Game.cameraX, Game.cameraY);
            }
         }

         if (this.type == 61) {
            if (this.r != null && this.r.ft) {
               this.r.p.t = true;
               this.r.b(Game.cameraX, Game.cameraY);
               this.r.p.t = false;
            }

            if (this.s != null && this.s.ft) {
               this.s.p.t = true;
               this.s.b(Game.cameraX, Game.cameraY);
               this.s.p.t = false;
            }
         }

         if (this.p == null) {
            Game.cameraX += 0;
            Game.cameraY += 0;
            return false;
         }

         this.p.p = -1;
         this.p.q = -1;
         this.p.r = -1;
         this.p.s = false;
         BSprite.N = false;
         this.p.M = false;
         if (this.type == 0) {
            this.p.L = false;
         }

         this.p.t = false;
         if (this.type == 39 && this.animationId >= 24 && this.animationId <= 26) {
            this.p.z = 0;
         }

         if (this.type == 8) {
            if (this.animationId != 0 && this.aq == 104) {
               this.a(this.q.positionX, this.q.positionY, this.ay, this.av, 1, eY, true);
            }
         } else if (this.type == 100) {
            if (this.aq == 102) {
               for (int var14 = 0; var14 < 4; var14++) {
                  this.a(aY[var14].positionX, aY[var14].positionY, aZ[var14].positionX, aZ[var14].positionY, 0, eY, false);
               }

               for (int var21 = 0; var21 < eY.length << 1; var21++) {
                  int var15 = (var21 - eY.length) / 2;
                  bt.setColor(eY[var21 / 2]);
                  Game.c(
                     E.positionX - aY[0].ax - var15 - Game.cameraX,
                     E.positionY - aY[0].ax - var15 - 40 - Game.cameraY,
                     aY[0].ax + var15 << 1,
                     aY[0].ax + var15 << 1,
                     0,
                     360
                  );
               }
            }
         } else if (this.type == 10) {
            if (this.aF == 1) {
               this.a(this.positionX, this.positionY, this.ac, this.ad, 0, eZ, false);
            }
         } else if (this.type == 16) {
            if (this.aq == 103) {
               Game.a.z = 2;
               Game.a.a(bt, this.aA / 1000 + 1 + "", this.positionX - Game.cameraX, this.positionY - Game.cameraY + -28, 3);
            }
         } else if (this.type == 0 && ea) {
            if (ed <= 10) {
               Game.b[33].a(bt, 1, eb - Game.cameraX - 10 - 51, ec - 29 - Game.cameraY, 0, 0, 0);
               Game.b[33].a(bt, 12, eb - Game.cameraX, ec - Game.cameraY, 0, 0, 0);

               for (int var16 = 0; var16 < dX.length(); var16++) {
                  int var22 = dX.charAt(var16) - '0';
                  Game.b[33].a(bt, 2 + var22, eb + 6 * (var16 + 1) + 1 - Game.cameraX, ec - Game.cameraY, 0, 0, 0);
               }

               if (!Game.bi) {
                  ed++;
                  ec -= 3;
               }
            } else {
               ed = 0;
               ea = false;
            }
         }

         if (aQ && (this.type == aR || (this.type == 28 || this.type == -2 || this.type == 11 && E == this) && aR == 0) && bI % aS != 0) {
            return true;
         }

         if (this.type == 0 || !Game.bi) {
            this.g();
            if (!this.s()) {
               this.c(Game.f);
            }
         }

         Game.cameraX += 0;
         Game.cameraY += 0;
         return true;
      }
   }

   private int aS() {
      this.g();
      int var2 = this.collisionBounds[1];
      int var1;
      return Game.g((var1 = this.positionX / 20) - 1, (var2 - 4) / 20);
   }

   private int aT() {
      this.g();
      int var2 = this.collisionBounds[1];
      int var1;
      return Game.g((var1 = this.positionX / 20) + 1, (var2 - 4) / 20);
   }

   private int aU() {
      this.g();
      int var2 = this.collisionBounds[1];
      int var1;
      return Game.g(var1 = this.positionX / 20, (var2 - 4) / 20);
   }

   private int aV() {
      this.g();
      int var2 = 0;
      int var3 = this.collisionBounds[1];
      int var4 = this.collisionBounds[3] + 1;
      this.bL = false;
      this.eN = this.H != 0;
      int var1 = this.positionX / 20;
      this.eC = Game.g(var1, var3 / 20);
      this.eG = Game.g(var1, var3 / 20 + 1);
      this.eD = Game.g(var1, var4 / 20);
      this.eH = Game.g(var1, var4 / 20 - 1);
      this.eI = Game.g(var1, var4 / 20 + 1);
      this.eJ = Game.g(var1 - 1, var4 / 20);
      this.eK = Game.g(var1 + 1, var4 / 20);
      if (this.type == 82) {
         int var5 = 0;
         int var6 = 0;
         int var7 = 0;
         if (this.eC <= 15) {
            var5 = Game.g(var1 - 1, var3 / 20);
            var6 = Game.g(var1 + 1, var3 / 20);
            var7 = Game.g(var1 - 2, var3 / 20);
            if (var5 > 15) {
               this.eC = var5;
            } else if (var6 > 15) {
               this.eC = var6;
            } else {
               this.eC = var7;
            }
         }
      }

      if (this.H <= -5120 && this.eG > 15 && this.eC <= 15) {
         this.eC = this.eG;
      }

      if (this.eC >= 15) {
         this.eN = true;
      }

      if (this.eD >= 15
         || this.eD == 15
         || this.eD == 0 && this.eH >= 24 && var4 - var3 >= 20
         || this.collisionBounds[2] - this.collisionBounds[0] > 20 && this.eJ >= 15 && this.eK >= 15) {
         var2 = var4 % 20;
         if (this.eH >= 15) {
            var2 += 20;
            this.eI = this.eD;
            this.eD = this.eH;
         }

         if (var2 > 0 && (this.type != 0 || this.aq != 1 || cv != 70)) {
            this.eN = true;
         }

         if (this.eC >= 24 && this.eG >= 24) {
            this.eN = false;
         }

         if (var2 > 0 && this.type == 0 && this.aq == 1 && cv == 70) {
            this.eN = true;
         }

         this.bL = true;
         if (this.eD >= 18 && this.eD <= 21) {
            int var9 = this.positionX % 20 >> 1;
            if (this.eD == 18) {
               var2 -= var9;
            } else if (this.eD == 19) {
               var2 -= var9 + 10;
            } else if (this.eD == 20) {
               var2 -= 10 - var9 + 10;
            } else if (this.eD == 21) {
               var2 -= 10 - var9;
            }

            this.eN = true;
         } else if (this.eD == 16) {
            var2 -= this.positionX % 20;
            this.eN = true;
         } else if (this.eD == 17) {
            var2 -= 20 - this.positionX % 20;
            this.eN = true;
         }
      }

      return var2;
   }

   public final void b(boolean var1, boolean var2) {
      int var3 = this.aV();
      bK = this.eC;
      int var6 = this.collisionBounds[0] - 1;
      int var7 = this.collisionBounds[2] + 1;
      int var8 = this.collisionBounds[1];
      int var9;
      if ((var9 = this.collisionBounds[3] - 10) < var8) {
         var9 = var8;
      }

      this.eL = false;
      this.eM = false;
      this.bN = false;
      this.eE = 0;
      this.eF = 0;
      if (var2) {
         if (this.eN) {
            if (this.H >= 0 && this.eD >= 15) {
               this.positionY -= var3;
            } else if (this.eC >= 24) {
               this.bN = true;
               this.positionY += 20 - var8 % 20 + 1;
            }
         }

         this.g();
         var6 = this.collisionBounds[0] - 1;
         var7 = this.collisionBounds[2] + 1;
         var8 = this.collisionBounds[1];
         var9 = this.collisionBounds[3] - 10;
      }

      if (var1) {
         int var10 = Game.g(var6 / 20 + 1, var9 / 20);
         int var11 = Game.g(var7 / 20 - 1, var9 / 20);

         for (int var4 = var8 / 20; var4 <= var9 / 20; var4++) {
            if (var6 < 0 && Game.be[Game.levelIndex] != 2) {
               this.eE = 24;
               this.eL = true;
               break;
            }

            int var5;
            if ((var5 = Game.g(var6 / 20, var4)) > this.eE) {
               this.eE = var5;
               if (this.eE >= 24) {
                  this.eL = true;
                  if (var4 == var9 / 20 && (var10 == 16 || var10 == 18 || var10 == 18)) {
                     this.eL = false;
                  }
               }
            }

            if ((var5 = Game.g(var7 / 20, var4)) > this.eF) {
               this.eF = var5;
               if (this.eF >= 24) {
                  this.eM = true;
                  if (var4 == var9 / 20 && (var11 == 17 || var11 == 20 || var11 == 21)) {
                     this.eM = false;
                  }
               }
            }

            if (this.eL || this.eM) {
               break;
            }
         }

         this.g();
         if (var1) {
            if (this.eL == this.eM) {
               if (this.eL && this.type == 39 && this.animationId == 13) {
                  if (this.facingLeft) {
                     this.positionX += 20;
                  } else {
                     this.positionX -= 20;
                  }
               }

               this.eL = this.eM = false;
            } else if (this.G <= 0) {
               if (this.eL) {
                  this.positionX += 20 - (var6 + 20) % 20 - 1;
                  this.aV();
               } else {
                  this.positionX -= var7 % 20;
                  this.aV();
               }
            } else if (this.eM) {
               this.positionX -= var7 % 20;
               this.aV();
            } else {
               this.positionX += 20 - (var6 + 20) % 20 - 1;
               this.aV();
            }
         }

         this.g();
         this.eA = this.collisionBounds[0] + this.collisionBounds[2] >> 1;
         this.eB = this.collisionBounds[1] + this.collisionBounds[3] >> 1;
      }
   }

   private boolean aW() {
      return this.facingLeft ? this.eM : this.eL;
   }

   private static boolean t(int var0) {
      switch (var0) {
         case 23:
         case 31:
            return true;
         default:
            return false;
      }
   }

   private static boolean aX() {
      switch (Game.player.animationId) {
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
            return true;
         case 24:
         case 25:
         default:
            return false;
      }
   }

   private static boolean aY() {
      switch (Game.player.animationId) {
         case 24:
         case 25:
         case 32:
         case 33:
            return true;
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         default:
            return false;
      }
   }

   private void aZ() {
      switch (this.aq) {
         case 0:
            if (this.animationId == 1) {
               Game.bZ = this;
            } else {
               Game.ca = this;
            }

            if (b(Game.player.collisionBounds, this.collisionBounds) && l(this.positionX - Game.player.positionX) <= 40) {
               Game.player.h(2);
               Game.player.a(1);
               bM = true;
               Game.bZ.aq = 100;
               Game.ca.aq = 100;
               Game.player.positionX = this.positionX;
               Game.player.positionY = this.positionY;
               return;
            }
            break;
         case 100:
            this.positionX = Game.player.positionX;
            this.positionY = Game.player.positionY;
      }
   }

   public final boolean w() {
      int var1 = this.collisionBounds[0] + 1;
      int var2 = this.collisionBounds[2] - 1;
      int var3 = 0;

      for (int var4 = var1 / 20; var4 <= var2 / 20; var4++) {
         int var5;
         if ((var5 = Game.g(var4, this.collisionBounds[1] / 20 - 1)) > var3) {
            var3 = var5;
         }
      }

      return var3 <= 15;
   }

   private void ba() {
      if (ew > 0) {
         ew--;
      } else {
         eu = -1;
      }

      if (aP) {
         bB = true;
         bD = true;
         Game.magic = Game.maxMagic;
         if (bR) {
            this.G = this.H = 0;
            if (!Game.G(4112) && !Game.F(4112)) {
               if (!Game.G(8256) && !Game.F(8256)) {
                  if (!Game.G(16388) && !Game.F(16388)) {
                     if (Game.G(33024) || Game.F(33024)) {
                        this.H = 5120;
                     }

                     return;
                  }

                  this.H = -5120;
                  return;
               }

               this.G = 5120;
               this.facingLeft = false;
               return;
            }

            this.facingLeft = true;
            this.G = -5120;
            return;
         }
      }

      if (dZ && dY >= 0) {
         dY = dY - Game.h;
      }

      if (dZ && dY < 0) {
         ea = true;
         dX = Integer.toString(dW);
         dW = 0;
         dZ = false;
         eb = this.positionX;
         ec = this.positionY - 60;
         dY = 0;
      }

      if (bb > 0) {
         bb = bb - Game.h;
      }

      if (this.cI && this.cJ != null && this.cJ.animationId == 1) {
         this.a(4, 1, 0, this.cJ);
      }

      if (Game.health <= 0 && !bS && this.animationId != 12) {
         this.h(0);
         this.b(12);
      }

      if (this.H > 4096) {
         this.H = 4096;
         eQ++;
      } else {
         eQ = 0;
      }

      if (!aM && this.animationId != 12) {
         this.E();
      }

      this.b(true, true);
      eS = false;
      eU = false;
      eW = false;
      if (this.eD == 2 || this.eC == 2 || this.eH == 2) {
         this.b(12);
      } else if (this.eD == 3 || this.eC == 3 || this.eH == 3) {
         this.a(4, 1, 0, this);
      }

      if (bj) {
         this.aC();
         this.a(this.animationId, this.h, this.i);
      } else if (bm) {
         this.k();
         this.a(this.animationId, this.h, this.i);
      } else if (!br) {
         switch (this.aq) {
            case 0:
            default:
               break;
            case 1:
               if (Game.modalActive) {
                  return;
               }

               switch (cv) {
                  case 2:
                     this.g(false);
                     break;
                  case 14:
                     this.bn();
                     break;
                  case 15:
                     this.bk();
                     break;
                  case 70:
                     this.bo();
               }

               this.R = false;
               this.S = false;
               return;
            case 2:
               this.g(true);
               break;
            case 3:
               this.ce();
               return;
         }

         i();
         if (this.aq != 1) {
            if (aM && this.animationId != 11) {
               if (ez) {
                  this.b(51);
               } else {
                  this.b(11);
               }
            }

            switch (this.animationId) {
               case 3:
                  eR++;
               case 0:
               case 1:
               case 2:
               case 4:
               case 13:
               case 14:
               case 15:
               case 37:
                  if (this.aq == 2) {
                     eU = true;
                  } else {
                     this.bd();
                  }
                  break;
               case 5:
               case 6:
                  eU = true;
                  if (bQ) {
                     this.J = 640;
                     if (this.animationId == 5) {
                        this.G = this.facingLeft ? -1024 : 1024;
                     }
                  } else {
                     this.J = 320;
                     if (this.animationId == 5) {
                        this.G = this.facingLeft ? -1280 : 1280;
                     }
                  }

                  if (bK >= 24 && this.H < 0) {
                     this.q(0);
                  }

                  if (this.H > 0 && (this.eD >= 15 || aT != null || bM) && (aT == null || aT.type != 50)) {
                     this.bc();
                  }

                  if (this.G != 0 && this.animationId != 5) {
                     this.a(5);
                  } else if (this.G == 0 && this.animationId != 6) {
                     this.a(6);
                  }

                  if (this.e()) {
                     if (this.H + this.J > 0) {
                        this.k(7, 36);
                        this.b(true, true);
                     } else {
                        this.a(this.animationId);
                     }
                  }
                  break;
               case 7:
                  eU = true;
                  if (d == 11) {
                     this.I = 1024 * (this.G > 0 ? -1 : 1);
                  }

                  if (this.eD < 15 && aT == null && !bM) {
                     this.J = 320;
                  } else {
                     this.bc();
                  }
                  break;
               case 8:
               case 43:
               case 45:
               default:
                  if (this.e()) {
                     this.bd();
                  }
                  break;
               case 9:
               case 10:
                  this.G = 0;
                  this.H = 0;
                  this.J = 0;
                  if (Game.G(16388)) {
                     this.a(6);
                  } else if (this.facingLeft && Game.G(2) || !this.facingLeft && Game.G(8)) {
                     this.a(5);
                  }

                  if (Game.F(127358)) {
                     this.bd();
                  } else if (this.e()) {
                     this.a(0);
                  }
                  break;
               case 11:
               case 51:
                  if (this.G != 0) {
                     this.I = 1024 * (this.G > 0 ? -1 : 1);
                  }

                  this.H = 0;
                  this.J = 0;
                  if (!this.bL && aT == null && !bM && !aM) {
                     this.q(0);
                  }

                  if ((this.facingLeft ? this.eF : this.eE) >= 24) {
                     this.G = 0;
                  }

                  if (!aM && this.e()) {
                     this.bd();
                  }

                  if (this.G > 0) {
                     if (this.collisionBounds[2] >= Game.viewBounds[2]) {
                        this.G = 0;
                        this.positionX = this.positionX - (this.collisionBounds[2] - Game.viewBounds[2]);
                     }
                  } else if (this.G < 0 && this.collisionBounds[0] <= Game.viewBounds[0]) {
                     this.G = 0;
                     this.positionX = this.positionX + (Game.viewBounds[0] - this.collisionBounds[0]);
                  }
                  break;
               case 12:
                  if (this.h == 1 && this.i == 0) {
                     Game.health = 0;
                  }

                  this.G >>= 2;
                  this.H = 0;
                  this.J = 0;
                  if (this.e()) {
                     this.c |= 64;
                     Game.a(12);
                  }

                  return;
               case 21:
               case 22:
               case 30:
                  if (this.e()) {
                     eV = 0;
                     this.q(0);
                     this.J = 320;
                  } else {
                     this.G = 0;
                     this.H = 0;
                  }

                  if (this.bL) {
                     this.bc();
                     break;
                  }
               case 16:
               case 17:
               case 18:
               case 19:
               case 20:
               case 26:
               case 27:
               case 28:
               case 29:
                  this.d(this.animationId, this.h);
                  this.cV();
                  eU = false;
                  if (!this.cI) {
                     aB();
                  }

                  if (29 == this.animationId) {
                     if (this.h == this.p.a(this.animationId) - 1 && this.i <= 0) {
                        if (er != null) {
                           for (int var3 = 0; var3 < 3; var3++) {
                              if (er[var3] != null) {
                                 Game.e(er[var3]);
                              }
                           }

                           er = null;
                        }

                        er = new Actor[3];
                        er[0] = this.a(39, 17, 13, this.facingLeft ? -10 : 10, 0, 303, false);
                        eq = 2;
                        er[0].an();
                        er[0].b(true, true);
                     } else if (this.e()) {
                        this.bd();
                     }
                  } else if (this.animationId == 20) {
                     if (this.e()) {
                        eV = -1;
                        this.bd();
                     }
                     break;
                  }

                  if ((this.bL || aT != null) && !bM) {
                     if (e != -1) {
                        if (this.h >= this.p.a(this.animationId) - 1) {
                           eV = -1;
                           this.a(e);
                           e = -1;
                        }
                     } else if (this.e()) {
                        eV = 0;
                        this.bd();
                     }
                  } else if (this.h >= this.p.a(this.animationId) - 1) {
                     eV = 0;
                     if (e != -1) {
                        eV = -1;
                        this.a(e);
                        e = -1;
                     } else {
                        this.bd();
                     }
                  }
                  break;
               case 23:
               case 31:
                  eU = false;
                  if (this.e()) {
                     eV = 0;
                     this.a(13);
                  }
                  break;
               case 24:
               case 25:
               case 32:
               case 33:
                  eU = false;
                  if (Game.U < 0 && !br && this.e()) {
                     this.bd();
                  }

                  this.G = 0;
                  this.an();
                  break;
               case 34:
                  this.bb();
                  if (this.e()) {
                     this.c |= 64;
                  }

                  return;
               case 35:
                  if (!this.bq()) {
                     this.bd();
                  }
                  break;
               case 36:
                  if (this.e()) {
                     this.c |= 64;
                     if (!Game.modalActive) {
                        this.a(1);
                     }
                  }

                  return;
               case 38:
               case 39:
               case 44:
               case 46:
                  this.cV();
                  this.d(this.animationId, this.h);
                  if (this.e()) {
                     if (this.animationId == 46) {
                        this.a(96, 51, 0, 0, -70, 300, false);
                        aU.facingLeft = false;
                        aU.c &= -2;
                     }

                     eV = 0;
                     this.bd();
                  }
                  break;
               case 40:
                  if (this.e()) {
                     this.a(41);
                     Game.player.c(25, this.positionX, this.positionY + -100);
                     br = true;
                  }
                  break;
               case 41:
                  eU = false;
                  eS = false;
                  break;
               case 42:
                  if (this.e()) {
                     this.a(39, 17, 1, this.facingLeft ? -20 : 20, -60, 302, false);

                     for (int var1 = 0; var1 < 4; var1++) {
                        this.a(39, 17, this.aF(), this.facingLeft ? -20 : 20, -60, 302, false);
                     }

                     if (this.k != null) {
                        this.positionX = this.positionX + (this.facingLeft ? this.k[0] - this.k[2] : this.k[2] - this.k[0]);
                        this.positionY = this.positionY + -(this.k[3] - this.k[1]);
                     }

                     eV = 0;
                     this.au();
                     this.b(21);
                     this.facingLeft = !this.facingLeft;
                  }
                  break;
               case 47:
                  if (this.facingLeft && this.eM || !this.facingLeft && this.eL) {
                     this.a(48);
                     this.G = 0;
                     this.H = 0;
                     this.a(56, 19, 10, this.facingLeft ? 20 : -20, 0, 302, true);
                     aU.p(102);
                     aU.aL = 0;
                     Game.a(0, 7);
                  }
                  break;
               case 48:
                  if (this.bL) {
                     this.a(49);
                  } else {
                     this.J = 256;
                     if (this.H >= 1280) {
                        this.J = 0;
                     }
                  }
                  break;
               case 49:
                  if (this.e()) {
                     this.a(50);
                  }
                  break;
               case 50:
                  if (this.e()) {
                     this.a(1);
                  }
            }

            if (eV != -1) {
               if (eV < (this.animationId == 7 ? 8 : 3)) {
                  eV++;
                  eU = false;
               } else {
                  eV = -1;
                  eU = true;
               }
            }

            if (!Game.modalActive) {
               for (int var4 = 0; var4 < 4; var4++) {
                  dV[var4] = Game.player.collisionBounds[var4];
               }

               if (this.facingLeft) {
                  dV[0] = dV[0] - 40;
               } else {
                  dV[2] = dV[2] + 40;
               }

               for (int var5 = 0; var5 < Game.ci; var5++) {
                  Actor var2 = Game.ch[var5];
                  if (b(dV, var2.collisionBounds)) {
                     if (var2 == null || (var2.c & 1024) != 0) {
                        Game.p = false;
                        break;
                     }

                     if (var2.type == 14) {
                        Game.p = false;
                        break;
                     }

                     if (var2.M()) {
                        Game.p = true;
                        break;
                     }

                     Game.p = false;
                  }
               }
            }

            if (eU) {
               if (!Game.F(65568) && !Game.p) {
                  if (Game.G(1) && this.animationId != 13 && bD) {
                     if (ch == 1) {
                        if (this.bL || aT != null || bM) {
                           if (Game.magic >= co[cl[ch]]) {
                              this.a(33);
                              this.G = this.H = 0;
                              Game.U = 0;
                           } else if (Game.E <= 0L) {
                              Game.E = 1000L;
                           }
                        }
                     } else if (ch == 0 && (aV == null || aV[0] == null) && (this.bL || aT != null || bM)) {
                        if (Game.magic >= co[cl[ch]]) {
                           this.a(25);
                           this.G = this.H = 0;
                           Game.U = 0;
                        } else if (Game.E <= 0L) {
                           Game.E = 1000L;
                        }
                     }
                  } else if (Game.G(512) && bb <= 0) {
                     ev = 1;
                     if (ch == 0) {
                        f(1);
                     } else if (ch == 1) {
                        f(0);
                     }
                  }
               } else if (ch == 0) {
                  if (this.aq == 2) {
                     this.a(16);
                  } else if (!this.bL && aT == null) {
                     this.a(21);
                     this.G = 0;
                     this.H = 0;
                  } else if (this.animationId == 13) {
                     this.a(23);
                  } else {
                     this.a(16);
                     this.H = 0;
                  }
               } else if (ch == 1) {
                  if (this.aq == 2) {
                     this.a(26);
                  } else if (!this.bL && aT == null) {
                     this.a(30);
                     this.G = 0;
                     this.H = 0;
                  } else if (this.animationId == 13) {
                     this.a(31);
                  } else {
                     this.a(26);
                     this.H = 0;
                  }
               }
            }

            if (eS && this.aq != 2) {
               if (this.animationId == 13 && !this.w()) {
                  return;
               }

               if (Game.G(16398)) {
                  this.H = 0;
                  if (Game.G(16388)) {
                     eT = false;
                     bQ = false;
                     this.a(6);
                     this.G = 0;
                     if (this.cI) {
                        this.H = -2560;
                     } else {
                        this.H = -3328;
                     }
                  } else if (Game.G(10)) {
                     eT = true;
                     bQ = false;
                     this.a(5);
                     this.G = Game.G(2) ? -1024 : 1024;
                     if (this.cI) {
                        this.H = -2560;
                     } else {
                        this.H = -3328;
                     }
                  }

                  if (!Game.G(16388)) {
                     if (Game.G(8)) {
                        this.facingLeft = false;
                     } else {
                        this.facingLeft = true;
                     }
                  }

                  this.cI = false;
                  g(ch);
                  aT = null;
               }
            }

            if (eW && Game.G(33024)) {
               this.q(2560);
            }

            if ((Game.G(12378) || Game.F(12378))
               && (this.animationId < 38 || this.animationId > 46)
               && this.animationId != 47
               && this.animationId != 48
               && this.animationId != 49
               && this.animationId != 50
               && this.animationId != 21
               && this.animationId != 30
               && !aX()
               && !this.bL
               && aT == null
               && this.aq != 2
               && !this.cI) {
               if (Game.G(4114) || Game.F(4114)) {
                  this.G = -1280;
                  if (!this.facingLeft) {
                     this.facingLeft = true;
                     eT = false;
                  }
               } else if (Game.G(8264) || Game.F(8264)) {
                  this.G = 1280;
                  if (this.facingLeft) {
                     this.facingLeft = false;
                     eT = false;
                  }
               }
            }

            if (!this.bL && !eT && !Game.F(4114) && !Game.F(8264) && this.aq != 2 && this.animationId != 39 && this.animationId != 47) {
               this.G = 0;
            }

            this.a(this.animationId, this.h, this.i);
            if (this.G != 0 && !V) {
               Z = Game.player.positionX;
               V = true;
            }

            if (this.G == 0 && V) {
               aa = Game.player.positionX;
               ab = Math.abs(aa - Z);
               V = false;
               int var6 = Game.player.facingLeft ? Game.player.positionX - 160 : Game.player.positionX - 80;
               if (ab > 20 && Math.abs(Game.cameraX - var6) <= 2) {
                  X = 0;
                  W = false;
               } else {
                  X++;
                  W = true;
               }
            }

            this.aA();
            this.R = false;
            this.S = false;
            if (aN) {
               this.db();
               if (this.animationId == 13 || this.animationId == 15) {
                  this.a(1);
               }
            }

            if (bT && l(this.G) >= 896) {
               this.G = this.facingLeft ? -896 : 896;
            }
         }
      }
   }

   public final void d(int var1, int var2) {
      if (Game.be[Game.levelIndex] == 3) {
         if (Game.levelIndex == 9) {
            this.G = 0;
         }
      } else if (!this.bL && aT == null) {
         this.G = 0;
      } else {
         this.G = 0;
         if (var1 >= 16 && var1 <= 19) {
            this.G = cg[var1 - 16][var2] << 8;
         } else if (var1 == 39) {
            this.G = cg[10][var2] << 8;
         } else if (var1 == 20) {
            this.G = cg[var1 - 16][var2] << 8;
            this.H = cg[11][var2] << 8;
            this.H /= 2;
         }

         this.G /= 2;
         if (this.facingLeft) {
            this.G = -this.G;
         }

         this.an();
      }
   }

   public static final int x() {
      int var0 = Game.i(9, 12);
      if (ch == 0) {
         return cq[cj[ch]] * var0 / 10;
      } else {
         return ch == 1 ? cr[cj[ch]] * var0 / 10 : 10 * var0 / 10;
      }
   }

   public static final int e(int var0, int var1) {
      byte var2 = 0;
      if (var0 == 0) {
         if (var1 == 14) {
            var2 = 80;
         }

         if (var1 == 85 && dN == 0) {
            var2 = 80;
         }
      } else if (var0 == 1 && var1 == 85 && dN == 1) {
         var2 = 80;
      }

      return 100 - var2;
   }

   public static final int y() {
      return aP ? 100 : cp[cm[ch]];
   }

   public static final boolean f(int var0) {
      if (var0 == ch) {
         return false;
      }

      if (var0 == 0) {
         g(cs);
         bb = 450;
         Game.player.a(21, 16, 39, 0, -80, 300, false);
      } else if (var0 == 1) {
         if (!bB) {
            return false;
         }

         g(ct);
         bb = 450;
         Game.player.a(21, 16, 40, 0, -80, 300, false);
      }

      ch = var0;
      Game.bu = true;
      return true;
   }

   public static final void g(int var0) {
      int var1 = 0;
      if (var0 == cs) {
         var1 = 28;
      } else if (var0 == ct) {
         var1 = 27;
      } else if (var0 == cu) {
         var1 = 74;
      } else {
         var1 = var0;
      }

      if (Game.bY == null) {
         Game.bY = Game.player.a(39, 17, var1, 0, 0, 303, false);
      } else {
         Game.bY.b(var1);
         Game.bY.positionX = Game.player.positionX;
         Game.bY.positionY = Game.player.positionY;
      }
   }

   public static final void d(Actor var0) {
      if (var0 != null) {
         var0.h = 0;
         var0.i = 0;
         var0.c &= -129;
         var0.c &= -1025;
         var0.positionX = Game.player.positionX;
         var0.positionY = Game.player.positionY;
         var0.facingLeft = Game.player.facingLeft;
      }
   }

   public final void a(int var1, int var2, int var3) {
      if (Game.cb != null) {
         if ((var1 >= 16 && var1 <= 23 || var1 >= 26 && var1 <= 31 || var1 == 39 || var1 == 42) && var1 != 29) {
            if (var2 == 0 && var3 == 0) {
               if (0 == ch) {
                  Game.cb.p = Game.b[21];
                  if (var1 == 39) {
                     Game.cb.animationId = 8;
                  } else if (var1 == 42) {
                     Game.cb.animationId = 9;
                  } else {
                     Game.cb.animationId = var1 - 16;
                  }
               } else {
                  Game.cb.p = Game.b[17];
                  Game.cb.animationId = var1 - 26 + 10;
               }

               d(Game.cb);
            } else if (Game.cb.e()) {
               e(Game.cb);
            }
         }

         if ((Game.cb.c & 128) == 0 && Game.cb.e()) {
            e(Game.cb);
         }
      }
   }

   public static final void e(Actor var0) {
      if (var0 != null) {
         var0.c |= 128;
         var0.c |= 1024;
      }
   }

   public final void h(int var1) {
      if (var1 != this.aq) {
         this.aq = var1;
         ar = 0;
         switch (var1) {
            case 0:
               this.p = Game.n(bs[0]);
               this.c &= -65;
               if (!this.bL && aT == null) {
                  this.b(7);
               } else {
                  this.b(1);
               }

               this.a(39, 17, 7, 0, -25, 302, this.facingLeft);
               return;
            case 1:
               e(Game.cb);
               bc = 0;
               this.p = Game.n(bs[cv]);
               Game.player.c &= -65;
               if (cv == 2) {
                  this.b(2);
                  cw = 50;
               } else if (cv == 14) {
                  this.b(0);
                  cw = 75;
               } else if (cv == 15) {
                  this.b(3);
                  cw = 25;
               } else if (cv == 70) {
                  this.b(0);
                  cw = 37;
               }

               Game.N();
               return;
            case 2:
               return;
            case 3:
               this.c |= 128;
               this.db();
               Game.N();
         }
      }
   }

   public final boolean z() {
      if (Game.magic <= 0) {
         return false;
      } else {
         c(true);
         if (cA > 0) {
            this.b(34);
            Game.N();
            this.aA = 2000;
            Game.h(1);
            Game.U = 0;
            Game.S = Game.player.eA - Game.cameraX;
            Game.T = Game.player.eB - Game.cameraY;
            Game.player.c |= 4096;
            return true;
         } else {
            return false;
         }
      }
   }

   public static final void c(boolean var0) {
      cA = 0;
      cC = 0;
      cy = 0;
      cB = 0;

      for (int var1 = 0; var1 < 3; var1++) {
         if (ej[var1] != 0) {
            cA++;
            if (u(ek[var1])) {
               cB++;
            }

            if (cA == 1 && var0) {
               cx = var1;
            }

            if (var1 < cx) {
               cC++;
            }
         }
      }

      if (cA != 0) {
         cD = 360 / cA;
         Game.dialogText = Game.k(0, em[cx]);
         Game.bB = 215;
         cz = 1;
      }
   }

   private void bb() {
      if (Game.G(8256)) {
         while (true) {
            if (--cx < 0) {
               cx = 2;
            }

            if (ej[cx] != 0) {
               c(false);
               cz = 1;
               Game.N();
               break;
            }
         }
      } else if (Game.G(4112)) {
         while (true) {
            if (++cx > 2) {
               cx = 0;
            }

            if (ej[cx] != 0) {
               c(false);
               cz = -1;
               Game.N();
               break;
            }
         }
      } else if (Game.G(327712) && (cy == cD * cz || cA <= 1)) {
         if (u(ek[cx])) {
            cv = ek[cx];
            Game.s();
            Game.h(2);
            if (Game.player.x != null) {
               Game.player.x.ay = 12;
               Game.e(Game.player.x.w);
               Game.player.x.w = null;
            }
         }

         Game.N();
      }

      if (Game.G(131072)) {
         if (!this.bL && aT == null) {
            Game.player.b(7);
         } else {
            Game.player.b(1);
         }

         Game.s();
         Game.player.x = null;
         Game.N();
      }
   }

   private static boolean u(int var0) {
      byte var1 = 0;
      switch (var0) {
         case 14:
            var1 = 1;
            break;
         case 15:
            var1 = 2;
            if (!Game.player.cI) {
               return false;
            }
            break;
         case 70:
            var1 = 0;
            if (Game.player.cI) {
               return false;
            }
      }

      return ej[var1] != 0;
   }

   public final void A() {
      this.h(1);
      this.a(39, 17, 7, 0, -25, 302, this.facingLeft);
      Game.s();
   }

   static final void B() {
      Game.g(20, 20, 200, 248);
      a(120, 110, 120, 110, 60, 50, 0, false);
      if (cA > 1) {
         int var0 = cD >> 2;
         cy = cy + var0 * cz;
         if (cy >= cD && cz == 1 || cz == -1 && cy <= -cD) {
            cy = cD * cz;
         }
      }

      int var6 = (cC - 1) * cD + cy;

      for (int var1 = 0; var1 < 3; var1++) {
         if (ej[var1] != 0) {
            int var2 = 120 + (60 * Game.m(var6) >> 8);
            int var3 = 110 + (60 * Game.l(var6) >> 8);
            byte var4 = el[var1];
            int var5 = Game.b[23].a(var4);
            Game.b[23].a(bt, var4, (Game.stateFrame >> 1) % var5, var2, var3, 0, 0, 0);
            var6 -= cD * cz;
         }
      }

      int var7 = 120 + (60 * Game.m(0) >> 8);
      int var8 = 110 + (60 * Game.l(0) >> 8);
      int var9;
      if ((var9 = Game.stateFrame % 22) >= 11) {
         var9 = 22 - var9;
      }

      if (cy == cD * cz % 360) {
         if (u(ek[cx])) {
            Game.a(var9 - 1, var7 - 22, var8 - 22, 44, 44);
         } else {
            Game.graphics.setColor(2004318071);
            if (Game.stateFrame % 8 <= 4) {
               Game.b[33].a(bt, 5, 0, 120, var8, 0, 0, 0);
            }
         }
      }

      Game.b[33].a(bt, 1, Game.b(Game.stateFrame, 1), 80, var8, 0, 0, 0);
      Game.b[33].a(bt, 1, Game.b(Game.stateFrame, 1), 160, var8, 1, 0, 0);
      Game.h();
      if (u(ek[cx])) {
         Game.a(Game.k(0, 18), Game.k(0, 21));
      } else {
         Game.a(Game.k(0, 18), "");
      }
   }

   public final void C() {
      int var1 = Game.health * 36 / Game.maxHealth;
      bt.setColor(15773760);
      Game.b(this.positionX - 20 - Game.cameraX, this.positionY - cw - Game.cameraY, 40, 9);
      bt.setColor(0);
      Game.b(this.positionX - 20 - Game.cameraX + 1, this.positionY - cw - Game.cameraY + 1, 38, 7);
      bt.setColor(1611800);
      Game.b(this.positionX - 20 - Game.cameraX + 2, this.positionY - cw - Game.cameraY + 2, var1, 5);
      bt.setColor(63488);
      Game.b(this.positionX - 20 - Game.cameraX + 3, this.positionY - cw - Game.cameraY + 3, var1 - 2, 3);
   }

   private void bc() {
      this.positionY = (this.collisionBounds[3] + 1) / 20 * 20;
      this.positionY--;
      this.H = 256;
      this.a(Game.player.positionY - eP > 85 ? 10 : 9);
      this.a(39, 17, 23, 0, 0, 302, false);
      eT = false;
      bQ = false;
      this.G = 0;
   }

   private boolean bd() {
      eS = true;
      eU = true;
      if (this.aq == 2) {
         if (fs >= 12) {
            this.a(37);
         } else {
            this.a(1);
         }

         return true;
      } else {
         if (this.animationId == 1 || this.animationId == 0 || this.animationId == 2) {
            this.G = 0;
            this.H = 0;
         }

         if (Game.F(4112)) {
            if (this.facingLeft) {
               if (this.animationId == 13 || this.animationId == 15) {
                  this.G = 0;
                  if (!this.w()) {
                     return false;
                  }
               }

               return this.bf();
            }

            this.facingLeft = true;
            this.fx = true;
            this.ay = 0;
         } else if (Game.F(8256)) {
            if (!this.facingLeft) {
               if (this.animationId == 13 || this.animationId == 15) {
                  this.G = 0;
                  if (!this.w()) {
                     return false;
                  }
               }

               return this.bf();
            }

            this.facingLeft = false;
            this.fx = true;
            this.ay = 0;
         } else if (this.animationId == 3 && eR >= 8 && this.animationId != 4) {
            this.a(39, 17, 22, 0, 0, 302, false);
            this.a(4);
            this.G = 0;
            return true;
         }

         return this.be();
      }
   }

   private boolean be() {
      boolean var1 = this.eD >= 16 && this.eD <= 21;
      if (!this.bL && aT == null && !bM) {
         this.q(0);
         eS = false;
         eU = false;
         return false;
      }

      if (this.animationId == 13) {
         this.G = 0;
         if (!Game.F(16388) && !Game.G(16388)) {
            if ((Game.G(33024) || Game.F(33024)) && (this.eD == 15 || aT != null)) {
               if (aT == null
                  || aT.type != 50
                     && aT.type != 33
                     && aT.type != 82
                     && aT.type != 14
                     && aT.type != 72
                     && aT.type != 73
                     && aT.type != 83
                     && aT.type != 84
                     && aT.type != 85) {
                  this.q(0);
                  this.positionY += 20;
                  aT = null;
               }
            } else if (Game.F(4112)) {
            }
         } else {
            if (this.w()) {
               this.a(14);
            }

            Game.N();
         }
      } else if (this.animationId == 15) {
         this.G = 0;
         if (this.e()) {
            this.a(13);
         }
      } else if (this.animationId == 14) {
         this.G = 0;
         if (this.e()) {
            this.a(var1 ? 2 : 1);
         }
      } else if (Game.F(33024) || Game.G(33024)) {
         this.a(15);
         Game.N();
      } else if (this.animationId == 0 && Game.stateFrame % 100 == 0) {
         this.a(1);
      } else if (this.animationId == 4) {
         if (this.G != 0) {
            this.bi();
         }

         if (this.e()) {
            this.a(0);
         }
      } else if (this.animationId != 1 || this.e()) {
         this.a(t(this.animationId) ? 13 : (var1 ? 2 : 0));
      }

      return true;
   }

   private boolean bf() {
      this.H = 0;
      if (!this.bL && aT == null && !bM) {
         this.q(0);
         eS = false;
         eU = false;
         return false;
      }

      if (this.animationId != 3) {
         eR = 0;
      }

      this.a(3);
      int var1;
      if ((var1 = Math.abs(this.G)) < 448) {
         var1 = 448;
      } else if (var1 < 1792) {
         var1 += 224;
      }

      if (var1 > 1792) {
         var1 = 1792;
      }

      if (this.facingLeft) {
         this.G = -var1;
      } else {
         this.G = var1;
      }

      this.bi();
      return true;
   }

   public final boolean d(boolean var1) {
      boolean var2 = false;
      if (this.eD != 18 && this.eD != 19 && this.eD != 16) {
         if ((this.eD == 21 || this.eD == 20 || this.eD == 17) && this.G > 0) {
            var2 = true;
         }
      } else if (this.G < 0) {
         var2 = true;
      }

      if (this.cJ != null && 105 == this.cJ.aq) {
         var2 = true;
      }

      if (this.aq == 1 && cv == 14 && this.u != null && this.u.fx) {
         var2 = true;
      }

      if (this.aq == 1 && cv == 70) {
         if (this.ax == 1) {
            var2 = true;
         } else if (this.ax == 2) {
            var2 = false;
         } else if (!var1) {
            var2 = true;
         }

         if (this.q != null) {
            if (this.q.H < 0) {
               var2 = true;
            } else if (this.q.H > 0) {
               var2 = false;
            }
         }
      }

      if (!var1) {
         var2 = !var2;
      }

      return var2;
   }

   public final int D() {
      int var1 = 0;
      var1 = 0 | this.aA();

      for (int var3 = Game.ci - 1; var3 >= 0; var3--) {
         Actor var2;
         if ((var2 = Game.ch[var3]) != null && var2 != this && var2.bO() && b(var2.collisionBounds, this.collisionBounds) && var2.T != this) {
            if (this.positionX < var2.positionX) {
               var1 |= 2;
            } else {
               var1 |= 1;
            }
         }
      }

      return var1;
   }

   public final void f(Actor var1) {
      if (var1 != aT) {
         int var2 = var1.D();
         if (this.positionX < var1.positionX) {
            if (this.G >= 0) {
               if (var1.eM || (var2 & 2) != 0) {
                  this.positionX = this.positionX - (this.collisionBounds[2] - var1.collisionBounds[0]);
                  return;
               }

               var1.positionX = var1.positionX + (this.collisionBounds[2] - var1.collisionBounds[0]);
               if (var1.U != null) {
                  var1.U.positionX = var1.U.positionX + (this.collisionBounds[2] - var1.collisionBounds[0]);
               }

               if (!this.cI) {
                  bT = true;
                  return;
               }
            }
         } else if (this.positionX >= var1.positionX && this.G <= 0) {
            if (!var1.eL && (var2 & 1) == 0) {
               var1.positionX = var1.positionX - (var1.collisionBounds[2] - this.collisionBounds[0]);
               if (var1.U != null) {
                  var1.U.positionX = var1.U.positionX - (var1.collisionBounds[2] - this.collisionBounds[0]);
               }

               if (!this.cI) {
                  bT = true;
                  return;
               }
            } else if (!this.R) {
               this.positionX = this.positionX + (var1.collisionBounds[2] - this.collisionBounds[0]);
            }
         }
      }
   }

   public final void E() {
      this.g();
      bT = false;
      int var2 = h(9, 1792);

      for (int var3 = Game.ck - 1; var3 >= 0; var3--) {
         Actor var1;
         if ((var1 = Game.cj[var3]) != null && (var1.type == 37 || var1.type == 80) && (var1.c & 1024) == 0 && var1.O) {
            this.a(var1, var2);
         }
      }

      for (int var5 = Game.ck - 1; var5 >= 0; var5--) {
         Actor var4;
         if ((var4 = Game.cj[var5]) != null
            && var4 != this
            && var4.type != 37
            && var4.type != 80
            && (var4.c & 1024) == 0
            && var4.O
            && var4.aq != 5
            && var4.aq != 6
            && var4.aq != 7) {
            this.a(var4, var2);
         }
      }
   }

   public final void a(Actor var1, int var2) {
      var1.g();
      if (b(var1.collisionBounds, this.collisionBounds) && this.positionY > var1.collisionBounds[1]) {
         if (var1.type == 14 && (var1.animationId == 2 || var1.animationId == 3)) {
            this.f(var1);
            return;
         }

         if (this.positionX < var1.positionX) {
            if (this.G <= 0 && this.bL && !var1.P) {
               if (!var1.eM) {
                  if (!var1.P) {
                     var1.positionX = var1.positionX + h(var2, this.collisionBounds[2] - var1.collisionBounds[0]);
                  } else if (this.eL) {
                     this.positionX = this.positionX + h(var2, var1.collisionBounds[2] - this.collisionBounds[0]);
                  }
               } else if (!this.S) {
                  this.positionX = this.positionX - (this.collisionBounds[2] - var1.collisionBounds[0]);
               }
            } else if (!this.eL && !this.R) {
               if (this.G > 0 && var1.G < 0 && !var1.P) {
                  this.positionX = this.positionX - (this.G >> 8);
                  var1.positionX = var1.positionX - (var1.G >> 8);
               } else {
                  this.positionX = this.positionX - h(var2, this.collisionBounds[2] - var1.collisionBounds[0]);
               }
            } else if (!var1.P) {
               var1.positionX = var1.positionX + h(var2, this.collisionBounds[2] - var1.collisionBounds[0]);
            } else if (this.eL) {
               this.positionX = this.positionX + h(var2, var1.collisionBounds[2] - this.collisionBounds[0]);
            }

            this.S = true;
         } else if (this.positionX >= var1.positionX) {
            if (this.G >= 0 && this.bL && !var1.P) {
               if (!var1.eL) {
                  if (!var1.P) {
                     var1.positionX = var1.positionX - h(var2, var1.collisionBounds[2] - this.collisionBounds[0]);
                  } else if (this.eM) {
                     this.positionX = this.positionX - h(var2, this.collisionBounds[2] - var1.collisionBounds[0]);
                  }
               } else if (!this.R) {
                  this.positionX = this.positionX + (var1.collisionBounds[2] - this.collisionBounds[0]);
               }
            } else if (!this.eM && !this.S) {
               if (this.G > 0 && var1.G < 0 && !var1.P) {
                  this.positionX = this.positionX - (this.G >> 8);
                  var1.positionX = var1.positionX - (var1.G >> 8);
               } else {
                  this.positionX = this.positionX + h(var2, var1.collisionBounds[2] - this.collisionBounds[0]);
               }
            } else if (!var1.P) {
               var1.positionX = var1.positionX - h(var2, var1.collisionBounds[2] - this.collisionBounds[0]);
            } else if (this.eM) {
               this.positionX = this.positionX - h(var2, this.collisionBounds[2] - var1.collisionBounds[0]);
            }

            this.R = true;
         }

         var1.Q = true;
      }
   }

   private void bg() {
      this.g();
      if (this.y > 0 && this.s == null) {
         this.s = Game.i(this.y);
         this.s.g();
         if (this.s.type == 46) {
            this.s.av = this.collisionBounds[1] - this.s.positionY;
         }
      }

      if (this.z > 0 && this.t == null) {
         this.t = Game.i(this.z);
         this.t.g();
         if (this.t.type == 46) {
            this.t.av = this.collisionBounds[1] - this.t.positionY;
         }
      }

      if (this.A > 0 && this.u == null) {
         this.u = Game.i(this.A);
         this.u.g();
         if (this.u.type == 46) {
            this.u.av = this.collisionBounds[1] - this.u.positionY;
         }
      }

      if (this.B > 0 && this.v == null) {
         this.v = Game.i(this.B);
      }

      if (this.C > 0 && this.w == null) {
         this.w = Game.i(this.C);
      }

      int var1 = this.av;
      if (this.s != null) {
         if (this.s.type == 46) {
            if (this.s.aq == 101) {
               this.aq = 0;
               this.av++;
               if (this.collisionBounds[1] < this.s.positionY) {
                  this.s.aq = 102;
               }
            } else if (this.s.aq == 102 && this.aq == 0) {
               this.aq = 104;
               this.aA = 5000;
               if (this.t != null && this.t.aq == 101) {
                  this.s.aq = 104;
                  Game.e(this.s.q);
                  this.s.q = null;
               }
            }
         } else if (this.s.type == 89 && this.s.aq == 101) {
            if (this.s.aC == 2 && this.aq != 103) {
               this.aq = 103;
            } else if (this.s.aC == 3 && this.aq != 105) {
               this.aq = 105;
            }
         }
      }

      if (this.t != null) {
         if (this.t.type == 46) {
            if (this.t.aq == 101) {
               this.aq = 0;
               this.av++;
               if (this.collisionBounds[1] < this.t.positionY) {
                  this.t.aq = 102;
               }
            } else if (this.t.aq == 102 && this.aq == 0) {
               this.aq = 104;
               this.aA = 5000;
               if (this.u != null && this.u.aq == 101) {
                  this.t.aq = 104;
                  Game.e(this.t.q);
                  this.t.q = null;
               }
            }
         } else if (this.t.type == 89 && this.t.aq == 101) {
            if (this.t.aC == 2 && this.aq != 103) {
               this.aq = 103;
            } else if (this.t.aC == 3 && this.aq != 105) {
               this.aq = 105;
            }
         }
      }

      if (this.u != null && this.u.type == 46) {
         if (this.u.aq == 101) {
            this.aq = 0;
            this.av++;
            if (this.collisionBounds[1] < this.u.positionY) {
               this.u.aq = 102;
            }
         } else if (this.u.aq == 102 && this.aq == 0) {
            this.aq = 104;
            this.aA = 5000;
         }
      }

      if (var1 != this.av) {
         if (this.s != null) {
            this.s.av = this.s.av - (this.av - var1);
            if (this.s.q != null) {
               this.s.q.positionY = this.s.q.positionY - (this.av - var1);
            }
         }

         if (this.t != null) {
            this.t.av = this.t.av - (this.av - var1);
            if (this.t.q != null) {
               this.t.q.positionY = this.t.q.positionY - (this.av - var1);
            }
         }

         if (this.u != null) {
            this.u.av = this.u.av - (this.av - var1);
            if (this.u.q != null) {
               this.u.q.positionY = this.u.q.positionY - (this.av - var1);
            }
         }
      }

      if (this.v != null && this.v.type == 65) {
         this.v.positionY = this.collisionBounds[1] + 2;
         if (this.v.aC == 1) {
            this.v.positionY = this.v.positionY + this.v.av;
         }

         if (aT == this.v) {
            if (Game.player.cJ != this) {
               Game.player.cJ = this;
            }

            Game.player.positionY = this.v.collisionBounds[1];
            if (this.aC == 2 && this.aq == 0) {
               this.aq = 103;
            } else if (this.aC == 3 && this.aq == 0) {
               this.aq = 105;
            }
         }
      }

      if (this.w != null && this.w.type == 65) {
         this.w.positionY = this.collisionBounds[1] + 2;
         if (this.w.aC == 1) {
            this.w.positionY = this.w.positionY + this.w.av;
         }

         if (aT == this.w) {
            if (Game.player.cJ != this) {
               Game.player.cJ = this;
            }

            Game.player.positionY = this.w.collisionBounds[1];
            if (this.aC == 2 && this.aq == 0) {
               this.aq = 103;
            } else if (this.aC == 3 && this.aq == 0) {
               this.aq = 105;
            }
         }
      }

      if (b(Game.player.collisionBounds, this.collisionBounds)
         && Game.player.H >= 0
         && (Game.player.aq == 0 && Game.player.positionY - this.collisionBounds[1] >= 35 || Game.player.aq == 1)) {
         if (Game.player.aq == 0 && Game.player.animationId != 35 && Game.player.animationId != 21 && Game.player.animationId != 30) {
            Game.player.a(35);
         }

         if (!Game.player.cI) {
            Game.player.a(39, 18, 0, 0, this.collisionBounds[1] - Game.player.positionY, 203, false);
         }

         Game.player.cI = true;
         Game.player.cJ = this;
      }

      if (Game.player.cJ == this && !b(Game.player.collisionBounds, this.collisionBounds)) {
         Game.player.cI = false;
         Game.player.cJ = null;
         if (Game.player.aq != 1) {
            cK = false;
            g(ch);
         }
      }

      switch (this.aq) {
         case 0:
            this.aA = 5000;
            if (this.s != null && this.s.type == 46 && this.s.aq == 0 && this.collisionBounds[1] < this.s.positionY) {
               this.s.aq = 104;
            }

            if (this.t != null && this.t.type == 46 && this.t.aq == 0 && this.collisionBounds[1] < this.t.positionY) {
               this.t.aq = 104;
            }

            if (this.u != null && this.u.type == 46 && this.u.aq == 0 && this.collisionBounds[1] < this.u.positionY) {
               this.u.aq = 104;
            }

            if (Game.player.cI && Game.player.cJ == this) {
               if (this.aC == 2) {
                  this.aq = 103;
                  return;
               }

               if (this.aC == 3) {
                  this.aq = 105;
                  return;
               }
            }
            break;
         case 103:
            this.av--;
            if (this.collisionBounds[1] >= this.ax) {
               this.aq = 104;
               return;
            }
            break;
         case 104:
            if (this.aC == 1) {
               this.cV();
               if (this.aA <= 0) {
                  this.aq = 106;
                  if (this.u != null && this.collisionBounds[1] < this.u.positionY && this.u.animationId == 4) {
                     this.u.a(7);
                     Game.e(this.u.q);
                     this.u.q = null;
                  }

                  if (this.t != null && this.collisionBounds[1] < this.t.positionY && this.t.animationId == 4) {
                     this.t.a(7);
                     Game.e(this.t.q);
                     this.t.q = null;
                  }

                  if (this.s != null && this.collisionBounds[1] < this.s.positionY && this.s.animationId == 4) {
                     this.s.a(7);
                     Game.e(this.s.q);
                     this.s.q = null;
                     return;
                  }
               }
            }
            break;
         case 105:
            this.av++;
            if (this.collisionBounds[1] <= this.aw) {
               this.aq = 104;
            }
            break;
         case 106:
            this.av--;
            if (this.t != null) {
               if (this.collisionBounds[1] > this.t.positionY) {
                  this.t.aq = 0;
                  this.t.a(3);
               } else if (this.t.animationId == 7 && this.t.e()) {
                  this.t.a(8);
               }

               this.t.av = this.collisionBounds[1] - this.t.positionY;
            }

            if (this.u != null) {
               if (this.collisionBounds[1] > this.u.positionY) {
                  this.u.aq = 0;
                  this.u.a(3);
               } else if (this.u.animationId == 7 && this.u.e()) {
                  this.u.a(8);
               }

               this.u.av = this.collisionBounds[1] - this.u.positionY;
            }

            if (this.s != null) {
               if (this.collisionBounds[1] > this.s.positionY) {
                  this.s.aq = 0;
                  this.s.a(3);
               } else if (this.s.animationId == 7 && this.s.e()) {
                  this.s.a(8);
               }

               this.s.av = this.collisionBounds[1] - this.s.positionY;
            }

            if (this.collisionBounds[3] - this.collisionBounds[1] <= this.az) {
               this.aq = 0;
               return;
            }
      }
   }

   private void bh() {
      switch (this.aq) {
         case 0:
            if (this.f(this.collisionBounds) && this.animationId == 3) {
               this.aq = 101;
               this.a(4);
               Actor var1;
               (var1 = this.a(46, 39, 5, 0, this.av, 3, false)).az = this.positionY;
               var1.aq = 103;
               this.a(var1);
               return;
            }
            break;
         case 101:
            return;
         case 102:
            return;
         case 103:
         default:
            break;
         case 104:
            if (this.animationId == 4 || this.animationId == 3) {
               this.a(7);
               return;
            }

            if (this.animationId == 7 && this.e()) {
               this.a(8);
            }
      }
   }

   private void bi() {
      if (this.eD == 18 || this.eD == 19) {
         this.H = this.G >> 1;
      } else if (this.eD == 21 || this.eD == 20) {
         this.H = -this.G >> 1;
      } else if (this.eD == 16) {
         this.H = this.G;
      } else {
         if (this.eD != 17) {
            return;
         }

         this.H = -this.G;
      }

      if (this.H < 0 && this.type != 70) {
         this.H = 0;
      }
   }

   private int bj() {
      int var1;
      int var2 = (var1 = this.collisionBounds[1]) % 20;
      int var3 = 500000;
      int var4 = 0;
      int var5 = 0;

      for (int var7 = Game.ci; var7 >= 0; var7--) {
         Actor var6;
         if ((var6 = Game.ch[var7]) != null && var6 != this && var6.type == 33) {
            var6.g();
            if (this.positionX >= var6.collisionBounds[0]
               && this.positionX <= var6.collisionBounds[2]
               && this.collisionBounds[1] > var6.collisionBounds[3]
               && this.collisionBounds[1] - var6.collisionBounds[3] < var3) {
               var3 = this.collisionBounds[1] - var6.collisionBounds[3];
               this.q = var6;
            }
         }
      }

      for (int var8 = var1 / 20 - 1; var8 >= 0 && var5 < 5 && (var4 = Game.g(this.positionX / 20, var8)) < 15; var5++) {
         var2 += 20;
         var8--;
      }

      if (var3 < var2) {
         return var3;
      }

      this.q = null;
      return var4 < 15 ? 0 : var2;
   }

   private void bk() {
      switch (this.animationId) {
         case 3:
            if (Game.F(12368) && this.cI) {
               if (Game.F(4112)) {
                  if (this.facingLeft) {
                     if (this.G > -1024) {
                        this.I = -128;
                     } else if (this.fu == 0) {
                        this.I = 0;
                     }
                  } else if (this.G <= 0) {
                     this.facingLeft = true;
                  } else {
                     this.I = -128;
                  }

                  if (this.fv == 0) {
                     this.H = 0;
                  }
               } else if (Game.F(8256)) {
                  if (!this.facingLeft) {
                     if (this.G < 1024) {
                        this.I = 128;
                     } else if (this.fu == 0) {
                        this.I = 0;
                     }
                  } else if (this.G >= 0) {
                     this.facingLeft = false;
                  } else {
                     this.I = 128;
                  }
               }
            } else if (this.G != 0) {
               if (this.facingLeft && this.G < 0) {
                  if (this.fu == 0) {
                     this.I = 128;
                  }
               } else if (!this.facingLeft && this.G > 0) {
                  if (this.fu == 0) {
                     this.I = -128;
                  }
               } else if (this.fu == 0) {
                  this.G = 0;
                  this.I = 0;
               }
            }

            if (Game.F(49412) && this.cI) {
               if (Game.F(16388)) {
                  this.J = -1024;
                  if (this.H < -768) {
                     this.H = -768;
                     this.J = 0;
                  }

                  this.ay = 2;
               } else if (Game.F(33024)) {
                  this.J = 768;
                  if (this.H > 768) {
                     this.J = 0;
                     this.H = 768;
                  }

                  this.ay = 1;
               }

               if (this.fv == 0) {
                  if (this.H < -768) {
                     this.H = -768;
                     this.J = 0;
                  }

                  if (this.H > 768) {
                     this.H = 768;
                     this.J = 0;
                  }
               }
            } else if (this.fv == 0 && this.H != 0) {
               if (this.ay == 2 && this.H < 0) {
                  this.J = 1024;
               } else if (this.ay == 1 && this.H > 0) {
                  this.J = -1024;
               } else {
                  this.H = 0;
                  this.ay = 0;
                  this.J = 0;
               }
            }

            if (Game.G(327712)) {
               this.aB = 500;
               this.b(4);
               if (this.facingLeft) {
                  this.G = -2048;
               } else {
                  this.G = 2048;
               }

               this.H = 0;
            }
            break;
         case 4:
            this.aB = this.aB - Game.h;
            if (this.aB <= 0) {
               if (this.facingLeft && this.G < 0) {
                  this.I = 256;
               } else if (!this.facingLeft && this.G > 0) {
                  this.I = -256;
               } else {
                  this.b(3);
                  if (this.fu == 0) {
                     this.I = 0;
                     this.G = 0;
                  }
               }
            }

            if (this.fv == 0) {
               this.H = 0;
            }
            break;
         case 5:
            if (this.G != 0) {
               this.I = 1024 * (this.G > 0 ? -1 : 1);
            }

            if (this.e()) {
               this.b(3);
            }
      }

      if (!this.cI) {
         this.H = 768;
      }

      if (this.bL && this.eD != 24) {
         if (this.facingLeft) {
            this.G = 1024;
         } else {
            this.G = -1024;
         }
      }

      this.aA();
      this.fu = 0;
      this.fv = 0;
      this.bl();
   }

   private void bl() {
      if (this.eD >= 24 && this.H > 0) {
         this.J = 0;
         this.H = 0;
      }
   }

   private void bm() {
      if (Game.F(12368)) {
         if (Game.F(4112)) {
            if (this.facingLeft) {
               this.G = -1280;
            } else {
               this.facingLeft = true;
            }
         } else if (Game.F(8256)) {
            if (this.facingLeft) {
               this.facingLeft = false;
            } else {
               this.G = 1280;
            }
         }

         this.b(1);
      } else {
         this.G = 0;
      }
   }

   private void bn() {
      switch (this.animationId) {
         case 0:
            this.bm();
            if (Game.G(65568) && !aM) {
               if (this.t != null) {
                  if (!b(Game.viewBounds, this.t.collisionBounds)) {
                     this.t.s = null;
                     Game.e(this.t);
                     this.t = null;
                  } else {
                     Actor var1;
                     (var1 = this.t.a(18, 12, 5, 0, 0, 302, false)).co();
                     var1.p(3);
                     var1.b(6);
                     Game.e(this.t);
                     this.t = null;
                  }
               }

               if (this.t == null || this.t != null && this.t.aB <= 0) {
                  this.a(4);
                  if (this.t != null) {
                     Game.e(this.t);
                  }

                  this.t = this.cp();
               }
            }

            if (this.bL || this.cI || this.u != null && this.u.cI) {
               this.H = 0;
            } else {
               this.J = 320;
            }
            break;
         case 1:
            this.bm();
            if (this.bL || this.cI || this.u != null && this.u.cI) {
               if (this.e()) {
                  this.H = 0;
                  this.J = 0;
                  this.a(0);
               }

               if (Game.G(65568)) {
                  if (this.t != null && !b(Game.viewBounds, this.t.collisionBounds)) {
                     this.t.s = null;
                     Game.e(this.t);
                     this.t = null;
                  }

                  if (this.t == null || this.t != null && this.t.aB <= 0) {
                     this.a(4);
                     if (this.t != null) {
                        Game.e(this.t);
                     }

                     this.t = this.cp();
                  }
               }
            } else {
               this.J = 320;
            }
         case 2:
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            break;
         case 4:
            this.G = 0;
            if (this.e()) {
               this.b(0);
            }
            break;
         case 8:
            if (!this.bL && !this.cI && (this.u == null || !this.u.cI)) {
               this.J = 320;
            }

            if (this.G != 0) {
               this.I = 1024 * (this.G > 0 ? -1 : 1);
            }

            if (this.e()) {
               this.b(0);
            }
      }

      if (this.cI && this.cJ != null) {
         this.positionY = this.cJ.collisionBounds[1];
         this.H = 0;
         this.J = 0;
         if (Game.G(16388)) {
            if (this.u != null && !b(Game.viewBounds, this.u.collisionBounds)) {
               Game.e(this.u);
               this.u = null;
            }

            if (this.u == null) {
               this.u = this.bp();
               this.w = this.cJ;
               this.u.az = this.w.av;
               this.b(0);
               cE = -1;
            }
         }
      } else {
         this.h(true);
      }

      if (this.u != null) {
         if (!b(Game.viewBounds, this.u.collisionBounds)) {
            this.u = null;
            this.w = null;
            return;
         }

         if (this.u.cI) {
            if ((Game.G(16388) && (!Game.F(16388) || Game.dA < 2) || cE == -1) && this.u.fw < this.u.fu) {
               this.u.fx = true;
               this.u.aq = 101;
               cE = 0;
            } else {
               if (this.u.fx && cE == 10) {
                  this.u.aq = 102;
               }

               if (cE < 20) {
                  cE++;
               }

               if (cE == 20 && this.u.fw > 0) {
                  this.u.fx = false;
                  this.u.aq = 101;
                  cE = 0;
               }
            }

            if (!b(this.collisionBounds, this.u.collisionBounds)) {
               this.u.cI = false;
            }

            if (!this.u.fx && Game.player.eD > 0) {
               Game.player.H = 0;
               this.u.cI = false;
               Game.player.b -= 256;
            }

            if (!this.u.cI) {
               int var2 = Game.player.b + Game.player.H >> 8;
               if (Game.player.H >= 0
                  && Game.player.positionY <= this.u.collisionBounds[1]
                  && Game.player.collisionBounds[2] > this.u.collisionBounds[0]
                  && Game.player.collisionBounds[0] < this.u.collisionBounds[2]
                  && var2 >= this.u.collisionBounds[1]) {
                  this.u.cI = true;
               }
            }
         } else {
            if (this.u.fx && cE == 10) {
               this.u.aq = 102;
            }

            if (cE < 20) {
               cE++;
            }

            if (cE == 20 && this.u.fw > 0) {
               this.u.fx = false;
               this.u.aq = 101;
               cE = 0;
            }
         }
      }

      this.aA();
   }

   private void bo() {
      if (this.cI) {
         this.h(0);
      } else {
         switch (ar) {
            case 0:
               if (this.eD <= 1) {
                  this.J = 320;
               } else {
                  this.H = 0;
               }

               if (Game.F(12368)) {
                  if (Game.F(4112)) {
                     if (this.facingLeft) {
                        this.G = -1280;
                     } else {
                        this.facingLeft = true;
                     }
                  } else if (Game.F(8256)) {
                     if (this.facingLeft) {
                        this.facingLeft = false;
                     } else {
                        this.G = 1280;
                     }
                  }
               } else if (Game.G(16388)) {
                  ar = 4;
                  this.G = 0;
                  this.H = 0;
                  this.a(1);
                  this.q = null;
                  this.av = this.positionY;
                  this.ay = this.positionX;
                  cF = 0;
                  cG = 0;
                  cH = 0;
                  this.az = 0;
               } else {
                  this.G = 0;
               }

               this.b(true, true);
               if (this.eD != 15 && this.eD != 24) {
                  if ((
                        this.G > 0 && this.eK < 24
                           || this.G < 0 && this.eJ < 24
                           || this.eD < 24
                           || this.G > 0 && this.eF < 24 && this.eF > 1
                           || this.G < 0 && this.eE < 24 && this.eE > 1
                     )
                     && this.eD > 1) {
                     if (Game.F(12368)) {
                        this.a(5);
                     } else {
                        this.a(6);
                     }
                  }
               } else {
                  this.a(0);
               }

               this.bi();
               return;
            case 1:
               this.J = 512;
               if (this.eD >= 15) {
                  ar = 0;
                  this.H = 0;
                  this.G = 0;
                  this.a(0);
               }

               if (this.eL && this.G < 0) {
                  this.G = 0;
               } else if (this.eM && this.G > 0) {
                  this.G = 0;
               }

               if (Game.G(16388)) {
                  int var3 = this.animationId;
                  this.b(6);
                  this.positionY -= 3;
                  this.g();
                  this.q = null;
                  this.av = this.collisionBounds[1] - this.bj();
                  if (this.av != this.collisionBounds[1]) {
                     this.ay = this.positionX;
                     cF = 0;
                     cH = 0;
                     this.az = (this.collisionBounds[1] + this.collisionBounds[3] >> 1) - this.av;
                     ar = 2;
                     this.H = 0;
                     return;
                  }

                  this.positionY += 3;
                  this.b(var3);
                  return;
               }
               break;
            case 2:
               this.b(true, true);
               this.G = 0;
               this.H = 0;
               if (Game.F(4112) && this.az > 40) {
                  if (cF >= 0 && cG <= 0) {
                     if (cF >= 0 && cF <= 1280) {
                        cF = 1280;
                     } else {
                        cF -= 256;
                     }

                     cG = cG - (15360 - cF) / 90;
                  }
               } else if (Game.F(8256) && this.az > 40) {
                  if (cF <= 0 && cG >= 0) {
                     if (cF <= 0 && cF >= -1280) {
                        cF = -1280;
                     } else {
                        cF += 256;
                     }

                     cG = cG + (15360 + cF) / 90;
                  }
               } else if (Game.F(16388)) {
                  if (this.az > 0) {
                     this.az -= 5;
                     if (cH == 0) {
                        this.positionY -= 5;
                     }

                     if (this.az < 0) {
                        this.az = 0;
                     }
                  }

                  this.a(11);
                  if (this.az < 20) {
                     cG = 0;
                     cF = 0;
                     cH = 0;
                  }

                  this.ax = 1;
               } else if (Game.F(33024)) {
                  this.az += 5;
                  if (cH == 0) {
                     this.positionY += 5;
                  }

                  this.a(11);
                  this.ax = 2;
               } else {
                  this.a(6);
                  this.ax = 0;
               }

               if (cF != 0 || cG != 0 || this.ay != this.positionX) {
                  if (this.eL || this.eM || cF != 0 || cG != 0) {
                     if (this.eL && cF < 0) {
                        cG = -cF >> 3;
                        cF = cF + cG;
                     } else if (this.eM && cF > 0) {
                        cG = -cF >> 3;
                        cF = cF + cG;
                     } else {
                        cF = cF + cG;
                        cG = cG - Game.m(cF >> 8);
                     }
                  }

                  int var2 = cH;
                  cH = cF > 0 ? 1 : -1;
                  if (var2 != cH) {
                     cG = cG - (cG >> 3);
                  }

                  this.positionX = this.ay + (this.az * Game.m(cF >> 8) >> 8);
                  this.positionY = this.av + (this.az * Game.l(cF >> 8) >> 8) + (this.collisionBounds[3] - this.collisionBounds[1] >> 1);
                  if (cF < -15360) {
                     cF = -15360;
                     cG = 0;
                  }

                  if (cF > 15360) {
                     cF = 15360;
                     cG = 0;
                  }

                  if ((cG & -128) == 0 && (cF & -128) == 0) {
                     cG = 0;
                     cF = 0;
                     cH = 0;
                  }
               }

               if (Game.G(65568)) {
                  if (cH != 0) {
                     this.G = 2048 * Game.l(cF >> 8) >> 8;
                     this.H = -Math.abs(2048 * Game.m(cF >> 8) >> 8);
                  }

                  if (cF < 0) {
                     this.G = -this.G;
                  }

                  ar = 1;
                  this.a(5);
               }

               if ((this.az <= 10 || this.bN) && this.q == null) {
                  this.b(9);
                  this.positionY = this.av;
                  this.az = 0;
                  cG = 0;
                  cF = 0;
                  cH = 0;
                  ar = 3;
                  this.q = null;
                  return;
               }

               if (this.eD >= 15) {
                  ar = 0;
                  this.G = 0;
                  this.H = 0;
                  this.a(0);
                  this.q = null;
                  return;
               }
               break;
            case 3:
               if (this.aU() < 15) {
                  ar = 1;
                  this.a(6);
                  return;
               }

               this.ay = this.positionX;
               this.av = this.positionY;
               if (Game.G(33024)) {
                  this.b(6);
                  this.g();
                  this.az = 20;
                  this.positionY = this.av + this.az + (this.collisionBounds[3] - this.collisionBounds[1] >> 1);
                  cF = 0;
                  cG = 0;
                  ar = 2;
                  this.G = 0;
                  this.H = 0;
                  return;
               }

               if (Game.F(12368)) {
                  if (Game.F(4112)) {
                     if (this.facingLeft) {
                        if (this.aS() >= 15) {
                           this.G = -1280;
                           return;
                        }

                        this.G = 0;
                        return;
                     }

                     this.facingLeft = true;
                     return;
                  }

                  if (Game.F(8256)) {
                     if (this.facingLeft) {
                        this.facingLeft = false;
                        return;
                     }

                     if (this.aT() >= 15) {
                        this.G = 1280;
                        return;
                     }

                     this.G = 0;
                     return;
                  }
               } else {
                  this.G = 0;
               }
               break;
            case 4:
               if (this.animationId == 1 && this.e()) {
                  this.a(6);
               }

               this.az += 20;
               int var1;
               if ((var1 = this.bj()) > 0) {
                  if (this.az >= var1) {
                     this.av = this.collisionBounds[1] - var1;
                     this.az = var1 - 20 + (this.collisionBounds[3] - this.collisionBounds[1] >> 1);
                     this.positionY -= 20;
                     this.g();
                     ar = 2;
                     cF = 0;
                     cH = 0;
                     cG = 0;
                     return;
                  }

                  this.av = (this.collisionBounds[3] + this.collisionBounds[1] >> 1) - 1 - this.az;
                  return;
               }

               this.av = (this.collisionBounds[3] + this.collisionBounds[1] >> 1) - this.az;
               if (this.az >= 100) {
                  ar = 0;
                  this.b(0);
                  return;
               }
         }
      }
   }

   private Actor bp() {
      this.a(37, 25, 0, 0, 115, 302, !this.facingLeft);
      aU.p(101);
      aU.fu = 115;
      aU.fv = 0;
      aU.fw = 0;
      aU.fx = true;
      aU.cI = true;
      aU.c |= 16;
      return aU;
   }

   private void g(boolean var1) {
      if (var1) {
         if (!aX() && !aY() && this.animationId != 36) {
            if (fs >= 12) {
               this.a(37);
            } else {
               this.a(1);
            }
         }

         int var2 = fs << 8;
         this.facingLeft = false;
         if (Game.F(61780)) {
            if (Game.F(4112)) {
               this.G = var2 - 2048;
               this.H = 0;
               return;
            }

            if (Game.F(8256)) {
               this.G = var2 + 2048;
               this.H = 0;
               return;
            }

            if (Game.F(16388)) {
               this.G = var2;
               this.H = -2048;
               if (this.animationId == 36) {
                  Game.player.positionX = Game.bZ.positionX;
                  Game.player.positionY = Game.bZ.positionY;
                  this.H = 0;
                  return;
               }
            } else if (Game.F(33024)) {
               this.G = var2;
               this.H = 2048;
               return;
            }
         } else {
            this.G = var2;
            this.H = 0;
         }
      } else {
         switch (this.animationId) {
            case 2:
               if (Game.F(12368)) {
                  if (Game.F(4112)) {
                     if (this.facingLeft) {
                        if (this.G > -1024) {
                           this.I = -128;
                        } else {
                           this.I = 0;
                        }
                     } else if (this.G <= 0) {
                        this.facingLeft = true;
                     } else {
                        this.I = -128;
                     }

                     this.H = 0;
                  } else if (Game.F(8256)) {
                     if (!this.facingLeft) {
                        if (this.G < 1024) {
                           this.I = 128;
                        } else {
                           this.I = 0;
                        }
                     } else if (this.G >= 0) {
                        this.facingLeft = false;
                     } else {
                        this.I = 128;
                     }
                  }
               } else if (this.G != 0) {
                  if (this.facingLeft && this.G < 0) {
                     this.I = 128;
                  } else if (!this.facingLeft && this.G > 0) {
                     this.I = -128;
                  } else {
                     this.G = 0;
                     this.I = 0;
                  }
               }

               if (Game.F(49412)) {
                  if (Game.F(16388)) {
                     this.J = -1024;
                     this.ay = 2;
                  } else if (Game.F(33024)) {
                     this.J = 768;
                     this.ay = 1;
                  }

                  if (this.H < -768) {
                     this.H = -768;
                  }

                  if (this.H > 768) {
                     this.H = 768;
                     return;
                  }
               } else if (this.H != 0) {
                  if (this.ay == 2 && this.H < 0) {
                     this.J = 1024;
                     return;
                  }

                  if (this.ay == 1 && this.H > 0) {
                     this.J = -1024;
                     return;
                  }

                  this.H = 0;
                  this.ay = 0;
                  return;
               }
               break;
            case 7:
               this.G = 0;
               if (this.e()) {
                  this.a(2);
               }
         }
      }
   }

   private boolean bq() {
      eS = true;
      eU = true;
      if (this.cI && this.cJ != null) {
         this.a(35);
         if (this.collisionBounds[1] >= this.cJ.collisionBounds[1] + 10) {
            cK = true;
         } else if (!cK) {
            this.positionY = this.cJ.collisionBounds[1] + 35;
         }

         if (!Game.G(4112) && !Game.F(4112)) {
            if (!Game.G(8256) && !Game.F(8256)) {
               this.G = 0;
            } else if (!this.facingLeft) {
               this.G = 768;
            } else {
               this.facingLeft = false;
            }
         } else if (this.facingLeft) {
            this.G = -768;
         } else {
            this.facingLeft = true;
         }

         if (this.G != 0) {
            g(cu);
         }

         if (cK) {
            if (this.collisionBounds[1] < this.cJ.collisionBounds[1]) {
               cK = false;
               this.positionY = this.cJ.collisionBounds[1] + 30;
            } else if (Game.G(16388) || Game.F(16388)) {
               this.H = -1280;
            } else if (!Game.G(33024) && !Game.F(33024)) {
               this.J = -256;
               if (l(this.H) >= 1280) {
                  this.H = -1280;
               }
            } else {
               this.H = 1280;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean v(int var1) {
      if (Game.G(var1)) {
         cL += 8;
      } else {
         cL--;
      }

      if (cL < 0) {
         cL = 0;
      }

      return cL >= 40;
   }

   private void br() {
      if (Game.aJ == this && Game.be[Game.levelIndex] == 3) {
         if (Game.player.positionY - 55 <= Game.viewBounds[1]) {
            Game.player.positionY = Game.viewBounds[1] + 55;
         } else if (Game.player.collisionBounds[3] > this.collisionBounds[3]) {
            Game.player.positionY = Game.viewBounds[3];
         }

         if (Game.levelIndex == 4) {
            if (Game.player.collisionBounds[3] > this.collisionBounds[3] - 30) {
               Game.player.positionY = Game.viewBounds[3] - 30;
            }

            if (Game.player.positionY > 320) {
               Game.ad = -1;
            }

            if (Game.player.positionY < 20) {
               Game.ad = 0;
            }
         }
      }

      if (Game.aJ == this && Game.be[Game.levelIndex] == 2) {
         if (Game.player.positionX <= this.collisionBounds[0]) {
            Game.player.positionX = this.collisionBounds[0];
         } else if (Game.player.positionX > this.collisionBounds[2]) {
            Game.player.positionX = this.collisionBounds[2];
         }
      }

      if (a(Game.player.positionX, Game.player.positionY, this.collisionBounds)) {
         Game.a(this);
         if (!Game.ay) {
            Game.regionLeft = this.collisionBounds[0];
            Game.regionRight = this.collisionBounds[2];
            Game.ai = this.collisionBounds[1];
            Game.aj = this.collisionBounds[3];
         }
      }
   }

   private void bs() {
      if (this.aG != -1 && this.r == null) {
         this.b(Game.i(this.aG));
         if (this.r != null && (this.r.c & 512) != 0) {
            this.c |= 512;
         }
      }

      switch (this.aq) {
         case 0:
            int var1 = this.ar();
            if (this.r == null && var1 == 2) {
               this.v(this.C(3), 6);
            }

            if (b(Game.player.collisionBounds, this.m)) {
               switch (var1) {
                  case 0:
                     this.p(101);
                     break;
                  case 1:
                     this.p(104);
                     break;
                  case 2:
                     this.p(112);
                     break;
                  case 3:
                     this.p(116);
                     break;
                  case 4:
                     this.p(117);
                     break;
                  case 5:
                     this.p(136);
                     this.aK = this.facingLeft ? 2 : 1;
                     this.ac = this.positionX;
                     this.ad = this.positionY;
                     this.c |= 16;
                     break;
                  case 6:
                     this.p(138);
                     this.ac = this.positionX;
                     this.ad = this.positionY;
                     this.aK = this.facingLeft ? 2 : 1;
                     this.c |= 16;
               }
            } else if (var1 == 10 && b(Game.viewBounds, this.m)) {
               this.p(132);
            }
         case 1:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
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
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         case 100:
         case 124:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 145:
         case 146:
         case 147:
         case 148:
         case 149:
         case 150:
         case 151:
         case 152:
         case 153:
         case 154:
         case 155:
         case 156:
         case 157:
         case 158:
         case 159:
         case 160:
         case 161:
         case 162:
         case 163:
         case 164:
         case 165:
         case 166:
         case 167:
         case 168:
         case 169:
         case 170:
         case 171:
         case 172:
         case 173:
         case 174:
         case 175:
         case 176:
         case 177:
         case 178:
         case 179:
         case 180:
         case 181:
         case 182:
         case 183:
         case 184:
         case 185:
         case 186:
         case 187:
         case 188:
         case 189:
         case 190:
         case 191:
         case 192:
         case 193:
         case 194:
         case 195:
         case 196:
         case 197:
         case 198:
         case 199:
         default:
            break;
         case 2:
            this.cV();
            if (this.e()) {
               switch (this.ar()) {
                  case 0:
                     this.p(101);
                     break;
                  case 1:
                     if (this.positionY > this.ad) {
                        this.p(108);
                     } else {
                        this.p(104);
                     }
                     break;
                  case 2:
                     this.p(112);
                     break;
                  case 3:
                     this.p(116);
                     break;
                  case 4:
                     this.p(117);
                     break;
                  case 5:
                     this.ac = this.positionX;
                     this.ad = this.positionY;
                     this.p(136);
                     break;
                  case 6:
                     this.ac = this.positionX;
                     this.ad = this.positionY;
                     this.p(138);
               }
            }
            break;
         case 3:
         case 9:
            if (this.r != null && this.r.type == 52) {
               this.r.p(103);
               this.r.b(true, true);
               this.b((Actor)null);
            }

            if (this.aq == 9) {
               this.a(true, true);
            } else {
               this.b(true);
            }

            if (this.an < 0) {
               this.bt();
            }

            return;
         case 101:
            this.da();
            this.eX = Game.h(Game.player.a - this.a, Game.player.b - 6400 - this.b);
            this.G = this.eX == 0 ? 0 : (Game.player.a - this.a) * 384 / this.eX;
            this.H = this.eX == 0 ? 0 : (Game.player.b - 6400 - this.b) * 384 / this.eX;
            this.g(cN);
            if (b(Game.player.collisionBounds, this.aJ)) {
               this.p(200);
            }
            break;
         case 102:
            if (this.e()) {
               this.p(103);
            }
            break;
         case 103:
            this.cV();
            if (this.aA <= 0) {
               this.p(101);
            }
            break;
         case 104:
            this.cV();
            if (!Game.player.bL || Game.player.b - this.b >= 20480 && Game.player.b - this.b <= 25600) {
               if (this.aA <= 0) {
                  if (this.cY() < 20480) {
                     this.p(106);
                  } else {
                     this.c(104, true);
                  }
               }
            } else {
               this.p(105);
            }
            break;
         case 105:
            this.dc();
            if (Game.player.b - this.b < 20480) {
               this.H = -2048;
            } else if (Game.player.b - this.b > 25600) {
               this.H = 2048;
            }

            if (this.H == 0 || l(Game.player.b - this.b - 23040) < 2048) {
               this.p(104);
            }
            break;
         case 106:
            this.cV();
            if (this.aA <= 0) {
               this.p(107);
            }
            break;
         case 107:
            this.cV();
            if (this.aA <= 0) {
               this.p(108);
            }
            break;
         case 108:
            this.I = this.facingLeft ? 51 : -51;
            if (this.facingLeft) {
               if (this.G > 0) {
                  this.G = 0;
               }
            } else if (this.G < 0) {
               this.G = 0;
            }

            this.H = this.H + this.ag;
            if (this.positionY < this.ad) {
               this.p(109);
            }
            break;
         case 109:
            this.cV();
            if (this.aA <= 0) {
               this.p(110);
            }
            break;
         case 110:
            this.cV();
            this.F(512);
            this.G = this.D(512);
            if (this.aA <= 0) {
               this.p(104);
            }
            break;
         case 111:
            this.eX = Game.h(Game.player.a - this.a, Game.player.b - 23040 - this.b);
            if (this.eX > 768) {
               this.G = (Game.player.a - this.a) * 768 / this.eX;
               this.H = (Game.player.b - 23040 - this.b) * 768 / this.eX;
               this.F(this.G);
            } else {
               this.db();
            }

            this.cV();
            if (this.aA <= 0 && this.r == null) {
               this.p(112);
            }
            break;
         case 112:
            this.eX = Game.h(Game.player.a - this.a, Game.player.b - 23040 - this.b);
            if (this.eX <= 768) {
               this.db();
            } else {
               if (!Game.player.bL && aT == null) {
                  this.G = this.D(768);
                  this.H = 0;
               } else {
                  this.G = (Game.player.a - this.a) * 768 / this.eX;
                  this.H = (Game.player.b - 23040 - this.b) * 768 / this.eX;
               }

               this.F(this.G);
            }

            this.cW();
            if (this.r == null) {
               this.v(this.C(3), 6);
            }

            if (this.eX <= 1280 && this.aB <= 0) {
               this.p(113);
            }
            break;
         case 113:
            this.cV();
            if (this.aA <= 0) {
               this.p(114);
            }
            break;
         case 114:
            if (this.e()) {
               this.p(115);
            }
            break;
         case 115:
            this.cV();
            if (this.aA <= 0) {
               this.p(111);
            }
            break;
         case 116:
            this.G = this.fx ? -384 : 384;
            this.H = -512;
            break;
         case 117:
            this.eX = Game.h(this.a - Game.player.a, this.b - Game.player.b);
            if (this.eX < 35840) {
               this.p(118);
            }
            break;
         case 118:
            this.cV();
            if (this.aA <= 0) {
               this.p(117);
            } else {
               boolean var2 = false;
               boolean var3 = false;
               if ((this.G > 0 || this.collisionBounds[0] > this.m[0] || this.aw >= 0) && (this.G < 0 || this.collisionBounds[2] < this.m[2] || this.aw <= 0)) {
                  this.G = this.aw * 1536 / this.ax * this.aA / 1700;
               } else {
                  this.G = 0;
                  var2 = true;
               }

               if ((this.H > 0 || this.collisionBounds[1] > this.m[1] || this.az >= 0) && (this.H < 0 || this.collisionBounds[3] <= this.m[3] || this.az <= 0)) {
                  this.H = this.az * 1536 / this.ax * this.aA / 1700;
               } else {
                  this.H = 0;
                  var3 = true;
               }

               if (var2 && var3) {
                  if (l(this.aw) > l(this.az)) {
                     this.az = -this.az;
                  } else {
                     this.aw = -this.aw;
                  }

                  this.aA = 1700;
               } else if (this.G == 0) {
                  this.H = 1536 * this.aA / 1700;
                  if (this.az < 0) {
                     this.H = -this.H;
                  }
               } else if (this.H == 0) {
                  this.G = 1536 * this.aA / 1700;
                  if (this.aw < 0) {
                     this.G = -this.G;
                  }
               }
            }
            break;
         case 119:
            this.cV();
            this.cW();
            if (this.aA <= 0) {
               this.p(120);
            }
            break;
         case 120:
            this.cW();
            if (this.aB <= 0) {
               this.p(121);
            }
            break;
         case 121:
            this.cV();
            if (this.aA <= 0) {
               if (!this.fx) {
                  this.ck();
                  this.fx = true;
               }

               if (this.e()) {
                  this.p(122);
               }
            }
            break;
         case 122:
            this.cV();
            if (this.aA <= 0) {
               this.p(123);
            }
            break;
         case 123:
            if (!b(Game.viewBounds, this.collisionBounds)) {
               Game.e(this);
            }

            return;
         case 125:
            this.cV();
            if (this.aA <= 0) {
               this.p(126);
            }
            break;
         case 126:
            if (this.e()) {
               this.p(127);
            }
            break;
         case 127:
            this.cV();
            if (this.aA <= 0) {
               this.p(128);
            }
            break;
         case 128:
            if (!b(Game.viewBounds, this.collisionBounds)) {
               Game.e(this);
               return;
            }
            break;
         case 129:
            this.cV();
            this.cW();
            if (this.aA <= 0) {
               this.p(130);
            }
            break;
         case 130:
            this.cW();
            if (this.aB <= 0) {
               this.p(131);
            }
            break;
         case 131:
            if (!b(Game.viewBounds, this.collisionBounds) || this.positionX < -20 || this.positionY < -20 || this.positionY > Game.cB + 20) {
               Game.e(this);
               return;
            }
            break;
         case 132:
            this.cV();
            if (this.aA <= 0) {
               this.p(133);
            }
            break;
         case 133:
            if (!b(Game.viewBounds, this.collisionBounds) && (this.r == null || this.r != null && !b(Game.viewBounds, this.r.collisionBounds))) {
               if (this.r != null) {
                  Game.e(this.r);
                  this.b((Actor)null);
               }

               this.b(false);
               return;
            }
            break;
         case 134:
            this.cV();
            if (this.aA <= 0 && !b(Game.viewBounds, this.collisionBounds)) {
               Game.e(this);
               return;
            }
            break;
         case 135:
            this.cV();
            this.G = this.aK == 1 ? 1280 : -1280;
            this.positionY = (30 * Game.m(2 * (Game.viewBounds[this.aK == 1 ? 0 : 2] - this.positionX)) >> 8) + this.ad;
            if (this.aA <= 0 && !b(Game.viewBounds, this.collisionBounds)) {
               Game.e(this);
               return;
            }
            break;
         case 136:
         case 137:
            this.cV();
            if (this.aq == 136) {
               this.g(cN);
               if (b(Game.player.collisionBounds, this.aJ)) {
                  this.p(137);
               }
            } else if (this.aq == 137 && this.e()) {
               this.p(136);
            }

            this.G = this.aK == 1 ? 1024 : -1024;
            this.positionY = (5 * Game.m(4 * (this.ac - this.positionX)) >> 8) + this.ad;
            if (!b(Game.aJ.collisionBounds, this.collisionBounds)) {
               this.c &= -17;
               this.b(false);
               return;
            }
            break;
         case 138:
         case 139:
            this.cV();
            if (this.aq == 138) {
               this.g(cN);
               if (b(Game.player.collisionBounds, this.aJ)) {
                  this.p(139);
               }
            } else if (this.aq == 139 && this.e()) {
               this.p(138);
            }

            this.G = this.aK == 1 ? 768 : -768;
            this.positionY = (30 * Game.m(2 * (this.ac - this.positionX)) >> 8) + this.ad;
            if (!b(Game.aJ.collisionBounds, this.collisionBounds)) {
               this.c &= -17;
               this.b(false);
               return;
            }
            break;
         case 200:
            this.cV();
            if (this.aA <= 0) {
               this.p(102);
            }
      }

      if (!this.c()) {
         int var4;
         if (3 == Game.be[Game.levelIndex] && ((var4 = this.ar()) >= 11 || var4 == 10 && this.aq == 133 || var4 == 5 || var4 == 6) && E == null) {
            this.M = true;
         }

         if (this.r != null) {
            if (this.r.type == 16 && this.r.aq == 101) {
               this.a(this.r, 3, 6);
            } else if (this.r.type == 52) {
               this.a(this.r, 3, 21);
            }
         }

         if (!this.p()) {
            if (this.ar() != 10) {
               this.aE();
            }

            if ((this.ar() == 0 || this.ar() == 1 || this.ar() == 5 || this.ar() == 6)
                  && (b(Game.player.collisionBounds, this.collisionBounds) || b(Game.player.collisionBounds, this.k))
                  && !this.at()
               || 3 == Game.be[Game.levelIndex] && this.ar() != 10 && b(Game.player.collisionBounds, this.collisionBounds)) {
               Game.player.a(4, this.B(1), 0, this);
            }

            if (this.an < 0 && this.r != null && !this.r.at) {
               this.r.t();
            }
         }
      }
   }

   private void bt() {
      aU = Game.f(this);
      if (aU != null) {
         aU.r = null;
         if (this.r != null) {
            this.r.c &= -257;
         }
      }

      Game.e(this);
   }

   private void bu() {
      if (this.aG != -1 && this.r == null) {
         this.b(Game.i(this.aG));
         if (this.r != null && (this.r.c & 512) != 0) {
            this.c |= 512;
         }

         if (this.r != null) {
            this.r.c |= 32;
         }
      }

      label190:
      switch (this.aq) {
         case 0:
            if (this.r == null) {
               this.v(this.C(5), -31);
            }

            int var2 = this.ar();
            if (b(Game.player.collisionBounds, this.m)) {
               switch (var2) {
                  case 0:
                     this.p(101);
                     break label190;
                  case 1:
                     this.p(105);
                     break label190;
                  case 2:
                     this.p(108);
               }
            }
            break;
         case 1:
            this.J = 320;
            if (this.bL) {
               switch (this.ar()) {
                  case 0:
                     this.p(101);
                     break;
                  case 1:
                     if (this.r != null) {
                        this.p(105);
                     } else {
                        this.p(107);
                     }
                     break;
                  case 2:
                     this.p(108);
               }
            }
            break;
         case 2:
            this.cV();
            if (this.e()) {
               switch (this.ar()) {
                  case 0:
                     this.p(101);
                     break;
                  case 1:
                     if (this.r != null) {
                        this.p(105);
                     } else {
                        this.p(107);
                     }
                     break;
                  case 2:
                     this.p(108);
               }
            }
            break;
         case 3:
         case 9:
            if (this.r != null) {
               if (this.r.type == 52) {
                  this.r.c &= -33;
                  this.r.p(103);
                  this.r.b(true, true);
                  this.b((Actor)null);
               } else if (this.r.type == 16) {
                  this.w(0, 0);
               }
            }

            if (this.aq == 9) {
               this.a(true, true);
               return;
            }

            this.b(true);
            return;
         case 4:
         case 8:
         case 109:
         default:
            break;
         case 101:
            this.G = this.D(512);
            this.F(512);
            this.g(cM);
            if (b(Game.player.collisionBounds, this.aJ)) {
               this.p(102);
            }
            break;
         case 102:
            if (this.e()) {
               this.a(12);
            }

            this.cV();
            if (this.aA <= 0) {
               this.p(103);
            }
            break;
         case 103:
            this.cV();
            if (this.aA <= 0) {
               this.p(104);
            }
            break;
         case 104:
            this.cV();
            if (this.aA <= 0) {
               this.p(101);
            }
            break;
         case 105:
            if (this.r == null) {
               this.v(this.C(5), -31);
            }

            this.dc();
            int var1 = this.a(this.a + (this.D(5) << 8), -this.b - -7936, Game.player.a, -Game.player.b, 3584, -512);
            if (this.r != null && this.r.type == 16 && this.r.aq == 101 && var1 != 0) {
               if (l(var1) > 2304) {
                  this.G = this.C(512);
                  this.a(10);
               } else if (l(var1) < 1024) {
                  this.G = -this.C(512);
                  this.a(11);
                  this.cV();
                  if (this.aA <= 0) {
                     this.aB = this.facingLeft ? -1024 : 1024;
                     if (this.r != null && this.r.type == 16 && this.r.aq == 101) {
                        this.p(106);
                     }
                  }
               } else {
                  this.aB = var1;
                  this.p(106);
               }
            }
            break;
         case 106:
            if (this.e()) {
               this.w(this.aB, -3584);
               this.p(107);
            }
            break;
         case 107:
            this.dc();
            this.cV();
            if (this.r == null && this.aA <= 0) {
               this.v(this.C(5), -31);
               this.p(105);
            }
            break;
         case 108:
            this.G = this.fx ? -384 : 384;
            if (this.eL || this.eM) {
               this.p(109);
            }
      }

      if (!this.c()) {
         if (this.G != 0) {
            this.aA();
         }

         if (this.r != null) {
            if (this.r.type == 16 && this.r.aq == 101) {
               this.a(this.r, 5, -31);
            } else if (this.r.type == 52) {
               this.a(this.r, 5, -31);
            }
         }

         if (this.aq != 0 && this.cX() == 1) {
            this.h(true);
         } else if (this.bL) {
            this.h(false);
         } else {
            this.h(true);
         }

         if (this.ar() == 0 && b(Game.player.collisionBounds, this.collisionBounds)) {
            this.dl();
            this.b(true);
         } else if (!this.p()) {
            if (!this.aE() && b(Game.player.collisionBounds, this.k)) {
               Game.player.a(4, this.B(1), 0, this);
            }
         }
      }
   }

   private void w(int var1) {
      if (Game.be[Game.levelIndex] == 2) {
         if (this.r(this)) {
            Game.e(this.r);
            this.r = null;
            return;
         }

         if (Game.player.positionY - this.positionY <= var1 && Game.player.positionY > this.positionY) {
            int var2 = Game.viewBounds[1] - this.positionY + 18;
            if (this.r == null) {
               Actor var3;
               (var3 = this.a(21, 16, 14, 0, var2, 100, false)).aC = 2;
               var3.av = var3.positionY - Game.cameraY;
               this.b(var3);
            }
         }
      }
   }

   private void bv() {
      if (this.animationId == 6) {
         this.dm();
         if (this.H == 0 && this.e()) {
            Game.e(this);
         }
      } else {
         if (this.aG != -1 && this.r == null) {
            this.b(Game.i(this.aG));
            this.aL = 2;
         } else if (this.r == null) {
            this.aL = this.ar() == 0 ? 0 : 1;
         }

         this.w(297);
         switch (this.aq) {
            case 0:
               if (b(Game.player.collisionBounds, this.m)) {
                  if (this.aC >= 4) {
                     this.p(104);
                  } else {
                     this.p(101);
                  }

                  this.ap = 81;
                  if (this.r != null && this.r.type == 51) {
                     this.r.aq = 100;
                     this.aB = 300;
                     this.O = true;
                  }
               }
               break;
            case 1:
               this.J = 320;
               if (this.bL) {
                  this.H = 0;
                  this.p(101);
               }
               break;
            case 2:
               this.cV();
               if (this.e()) {
                  this.p(101);
               }
               break;
            case 3:
            case 9:
               this.b((Actor)null);
               if (this.aC >= 4) {
                  this.aq = 5;
                  this.c &= -1025;
                  this.c |= 128;
                  this.aA = 5000;
               }

               if (this.aq == 9) {
                  this.a(true, true);
                  return;
               }

               this.b(true);
               return;
            case 4:
               this.G = 0;
               this.aB++;
               if (this.animationId == 5) {
                  if (this.e() || Game.player.animationId != 39) {
                     this.a(96, 51, 0, 0, -70, 300, false);
                     aU.facingLeft = false;
                     aU.c &= -2;
                     this.p(9);
                  }
               } else if (Game.player.animationId == 39
                  && Game.player.k != null
                  && Game.player.k[3] != Game.player.k[1]
                  && Game.player.k[2] != Game.player.k[0]) {
                  this.a(5);
                  Actor var1;
                  (var1 = this.a(1, 1, 6, 0, -60, 302, false)).facingLeft = Game.player.facingLeft;
                  var1.G = Game.player.facingLeft ? -1280 : 1280;
                  this.a(39, 17, 1, 0, -30, 302, this.facingLeft);
                  Game.a(5, 0);
               }

               int var2 = this.aF();
               if (this.aB % Game.b[17].a(var2) == 0) {
                  this.a(39, 17, var2, 0, -30, 302, true);
               }
               break;
            case 5:
               if (this.aC >= 4) {
                  this.cV();
                  if (this.aA <= 0) {
                     this.p(0);
                  }

                  return;
               }
            case 8:
            default:
               break;
            case 101:
               this.F(256);
               this.G = this.D(256);
               if (this.aC >= 4) {
                  this.G <<= 1;
               }

               this.g(this.ar() == 1 ? cP : cO);
               if (b(Game.player.collisionBounds, this.aJ)) {
                  this.p(102);
               }
               break;
            case 102:
               if (this.e()) {
                  this.p(103);
               }
               break;
            case 103:
               this.cV();
               if (this.aA <= 0) {
                  this.p(101);
               }
               break;
            case 104:
               if (this.s.aq == 102) {
                  this.p(105);
                  this.c &= -129;
               }
               break;
            case 105:
               if (this.e()) {
                  this.p(101);
                  this.s = null;
               }
         }

         if (this.aC >= 4 || !this.c()) {
            if (this.aq != 0 && this.aq != 4) {
               this.aB = this.aB - Game.h;
               if (this.r != null && this.aB < 0) {
                  this.aL = this.ar() == 0 ? 0 : 1;
               }
            }

            if ((this.G != 0 || this.H != 0) && this.aq != 104) {
               this.aA();
            }

            if (this.Q) {
               this.G = 0;
               this.Q = false;
            }

            if (this.aq != 104 && this.aq != 105 && (this.aC < 4 || this.aq != 0)) {
               if (this.aq != 0) {
                  this.h(this.cX() == 1);
               }

               if (this.p()) {
                  return;
               }
            }

            if (this.aL != 2 && this.aq != 104 && (this.aC < 4 || this.aq != 0) && !this.aE() && b(Game.player.collisionBounds, this.k)) {
               Game.player.a(4, this.B(1), 0, this);
            }
         }
      }
   }

   private void bw() {
      if (this.e()) {
         Game.e(this);
      } else {
         if (b(this.collisionBounds, Game.player.collisionBounds)) {
            Game.player.a(4, this.B(1), 0, this);
            Game.e(this);
         }
      }
   }

   private void bx() {
      if (this.e()) {
         Game.e(this);
      } else {
         if (b(this.collisionBounds, Game.player.collisionBounds)) {
            Game.player.a(4, this.B(1), 0, this);
         }

         for (int var1 = 0; var1 < Game.ck; var1++) {
            Actor var2;
            if ((var2 = Game.cj[var1]) != null && (var2.c & 1024) == 0 && var2.aq != 3 && var2.aq != 7 && var2.aq != 5 && var2.aq != 6) {
               if (var2.M() && var2.collisionBounds != null && b(var2.collisionBounds, this.collisionBounds)) {
                  if (var2.type == 70) {
                     var2.r = this;
                     var2.p(2);
                  } else {
                     var2.s(500);
                  }
               } else if (var2.type == 73 && !var2.cI && var2.aH == 0 && b(var2.collisionBounds, this.collisionBounds)) {
                  var2.facingLeft = var2.positionX < this.positionX;
                  var2.p(105);
               } else if (var2.type == 79 && b(var2.collisionBounds, this.collisionBounds)) {
                  var2.a(this.collisionBounds, false, 79);
               }
            }
         }
      }
   }

   private void by() {
      if (this.fx) {
         this.aL = 1;
      } else {
         this.aL = 0;
      }

      label375:
      switch (this.aq) {
         case 0:
            if (this.aH == 1 || this.aH == 0) {
               this.aG();
            }

            if (b(Game.player.collisionBounds, this.m) || a(this.m, Game.player.collisionBounds)) {
               this.O = true;
               this.P = false;
               switch (this.aH) {
                  case 0:
                     this.p(101);
                     break label375;
                  case 1:
                     this.p(105);
                     this.aG();
                     break label375;
                  case 2:
                  case 3:
                     this.p(110);
               }
            }
            break;
         case 2:
            this.J = 320;
            this.b(true, true);
            switch (this.aH) {
               case 0:
                  if (this.bL) {
                     this.p(102);
                  }
                  break label375;
               case 1:
               case 2:
                  if (this.e()) {
                     this.a(5);
                     this.ax = 1;
                  }

                  if (this.bL) {
                     this.db();
                     if (this.ax == 1) {
                        this.p(102);
                     }
                  }
               default:
                  break label375;
            }
         case 3:
            this.b(true);
            return;
         case 4:
         case 8:
         default:
            break;
         case 9:
            this.a(true, true);
            break;
         case 101:
            this.J = 213;
            this.b(true, true);
            if (this.bL) {
               this.cV();
               if (this.aA <= 0) {
                  if (++this.ay > 1) {
                     this.H = (this.H >= 0 ? -1 : 1) * 3400 >> 1;
                     this.G = 0;
                     if (this.ay > 2) {
                        this.ay = 0;
                     }
                  } else {
                     this.H = (this.H >= 0 ? -1 : 1) * 3400;
                     this.F(1200);
                     this.G = this.C(1200);
                  }

                  this.aA = 1000;
               }
            } else {
               this.aA = 0;
            }
            break;
         case 102:
            if (this.e()) {
               this.p(103);
            } else {
               this.b(true, true);
            }
            break;
         case 103:
            this.b(true, true);
            if (this.fx) {
               this.cW();
               if (this.aB <= 0) {
                  this.G = 0;
               }

               if (this.eD <= 1) {
                  this.J = 320;
               } else {
                  this.H = 0;
               }

               if (this.fu < 3) {
                  this.cV();
                  if (this.aA <= 400 && this.aA > 200) {
                     this.aL = 1;
                  } else if (this.aA <= 200 && this.aA > 0) {
                     this.aL = 2;
                  } else {
                     this.aL = 1;
                     this.aA = 400;
                     this.fu++;
                  }
               } else {
                  this.a(-40, 3, 3, 0, 0, 302, false);
                  aU.b(3);
                  this.p(3);
                  Game.I(-1);
               }
            } else {
               this.cV();
               if (this.aA <= 0) {
                  switch (this.aH) {
                     case 0:
                        this.p(104);
                        break label375;
                     case 1:
                        this.p(104);
                        break label375;
                     case 2:
                        this.p(112);
                  }
               }
            }
            break;
         case 104:
            if (this.e()) {
               switch (this.aH) {
                  case 0:
                     this.p(101);
                     break;
                  case 1:
                     this.p(108);
               }
            }
            break;
         case 105:
            this.da();
            this.G = this.D(400);
            this.b(true, false);
            if (this.eD <= 1) {
               if (Game.g(this.positionX / 20, (this.positionY + 30) / 20) <= 1) {
                  this.J = 320;
               } else {
                  this.H = 2048;
               }
            } else {
               this.H = 0;
            }

            this.eX = Game.h(Game.player.a - this.a, Game.player.b - this.b) >> 8;
            if (this.eX < 65) {
               this.cV();
               if (this.aA <= 0 && this.bL) {
                  this.p(109);
               }
            }

            if ((
                  this.G > 0 && this.eK < 24 && this.eK != 15
                     || this.G < 0 && this.eJ < 24 && this.eJ != 15
                     || this.eD < 24 && this.eD != 15
                     || this.G > 0 && this.eF < 24 && this.eF != 15 && this.eF > 1
                     || this.G < 0 && this.eE < 24 && this.eE != 15 && this.eE > 1
               )
               && this.eD > 1) {
               this.p(107);
            }
            break;
         case 106:
            if (this.aA > 0) {
               this.cV();
            }

            this.da();
            this.G = this.D(400);
            this.b(true, true);
            this.bi();
            if (this.eD <= 1) {
               this.J = 320;
            }

            if ((this.eK >= 24 || this.eK == 15) && (this.eJ >= 24 || this.eJ == 15) && (this.eD >= 24 || this.eD == 15) && this.aA <= 0) {
               this.p(108);
            }
            break;
         case 107:
            if (this.e()) {
               this.aA = 1000;
               this.p(106);
            }
            break;
         case 108:
            if (this.e()) {
               this.p(105);
            }
            break;
         case 109:
            this.b(true, true);
            if (this.e()) {
               this.aA = 2048;
               this.a(-37, 17, 77, (this.facingLeft ? -1 : 1) * 20, 0, 302, false);
               this.p(105);
            }
            break;
         case 110:
            this.b(true, true);
            this.da();
            this.G = this.D(400);
            if (this.G < 0 && this.eE > 1 || this.G > 0 && this.eF > 1 || this.G < 0 && this.aS() <= 1 || this.G > 0 && this.aT() <= 1) {
               this.G = 0;
            }

            this.eX = l(Game.player.a - this.a) >> 8;
            if (this.aH == 3) {
               if (this.eX < 195) {
                  this.p(115);
               }
            } else if (this.aH == 2 && this.eX < 16 && Game.player.b > this.b) {
               this.p(111);
            }
            break;
         case 111:
            this.J = 320;
            if (this.ax == 0 && this.e()) {
               this.ax = 1;
               this.a(5);
            }

            this.b(true, true);
            this.cV();
            if (this.aA <= 0 && this.eD >= 16) {
               this.p(102);
            }
            break;
         case 112:
            if (this.ax == 0 && this.e()) {
               this.ax = 1;
               this.a(5);
            }

            this.b(true, true);
            if (this.bN) {
               this.p(113);
            }
            break;
         case 113:
            this.b(true, false);
            if (this.e()) {
               this.p(110);
            }
            break;
         case 114:
            this.aj();
            this.g();
            int var1 = Math.abs(this.av);
            int var2 = ((this.az << 8) + var1 * Game.l(this.aA >> 16) >> 8) + (this.collisionBounds[3] - this.collisionBounds[1] >> 1);
            int var3 = (this.aw << 8) + var1 * Game.m(this.aA >> 16) >> 8;
            this.H = var2 - this.positionY << 8;
            this.G = var3 - this.positionX << 8;
            this.aJ[3] = this.collisionBounds[3];
            this.aJ[1] = this.collisionBounds[1] - this.fv;
            this.aJ[0] = this.collisionBounds[0] - 240;
            this.aJ[2] = this.collisionBounds[2] + 240;
            break;
         case 115:
            if (this.ax == 0 && this.e()) {
               this.ax = 1;
               this.a(11);
            }

            this.g();
            if ((this.collisionBounds[3] + this.collisionBounds[1] >> 1) - this.az > this.fv) {
               this.av = this.fv;
               this.positionY = this.positionY - ((this.collisionBounds[3] + this.collisionBounds[1] >> 1) - this.az - this.fv);
               this.H = 0;
               if (this.aJ == null) {
                  this.aJ = new int[4];
                  this.g();
                  this.aJ[3] = this.collisionBounds[3];
                  this.aJ[1] = this.collisionBounds[1] - this.fv;
                  this.aJ[0] = this.collisionBounds[0] - 240;
                  this.aJ[2] = this.collisionBounds[2] + 240;
               }

               this.p(114);
            }
      }

      if (this.H > 4096) {
         this.H = 4096;
      }

      if (!this.c()) {
         this.aA();
         if (this.aH == 3) {
            if ((this.aq == 114 || this.aq == 115)
               && (
                  b(this.collisionBounds, Game.player.k)
                     || b(this.aw, this.az, this.positionX, this.collisionBounds[3] + this.collisionBounds[1] >> 1, Game.player.k)
               )) {
               this.n = null;
               this.aJ = null;
               this.O = true;
               this.P = false;
               this.aH = 1;
               this.p(2);
            }
         } else if (this.aq != 3 && b(this.collisionBounds, Game.player.k)) {
            if (this.aq != 103 && this.aq != 102) {
               if (this.aq == 110 || this.aq == 113) {
                  this.p(111);
               } else if (this.aq != 2) {
                  this.p(2);
               }
            } else if (!this.fx) {
               this.positionX = this.positionX + (this.a - Game.player.a > 0 ? 1 : -1) * 1;
               this.b(true, true);
               this.bi();
               this.e(false);
            } else {
               this.aB = 400;
               this.G = -this.C(1000);
            }
         }

         if (this.aq != 3 && this.aq != 103 && this.aq != 102 && b(this.collisionBounds, Game.player.collisionBounds)) {
            Game.player.a(4, this.B(1), 0, this);
            if (this.aH == 2 && this.aq != 113 && this.aq != 110) {
               this.p(112);
            }
         }
      }
   }

   private void bz() {
      for (int var1 = 0; var1 < this.av; var1++) {
         int var2;
         if ((var2 = this.positionX + var1 * this.az) - Game.cameraX >= -80 && var2 - Game.cameraX <= 320) {
            if (this.aC != 18) {
               for (int var3 = 0; var3 < this.n.length; var3 += 2) {
                  if (this.aw < 2) {
                     Game.a(Game.ez[this.n[var3 + 1]], this.positionX - Game.cameraX, this.n[var3] - Game.cameraY + Game.i(-1, 2), 0);
                  }

                  if (this.aw != 1) {
                     Game.a(Game.ez[this.n[var3 + 1]], this.positionX + this.ay - Game.cameraX, this.n[var3] - Game.cameraY + Game.i(-1, 2), 0);
                  }
               }
            } else {
               Game.b[20].a(bt, 0, Game.stateFrame % 2, var2 - Game.cameraX, this.positionY - Game.cameraY, 0, 0, 0);
            }
         }
      }
   }

   private void bA() {
      switch (this.aq) {
         case 0:
            this.cV();
            if (this.aA <= 0) {
               this.aA = this.ay;
               this.aq = 101;
               this.H = 0;
               this.b(81);
               return;
            }
            break;
         case 101:
            if (this.e()) {
               this.H = -this.az;
               this.a(79);
               this.aq = 102;
               return;
            }
            break;
         case 102:
            if (this.aw - this.positionY >= this.av) {
               this.a(80);
               this.H = 0;
               this.aq = 103;
               return;
            }
            break;
         case 103:
            if (this.e()) {
               this.H = 0;
               this.positionY = this.aw;
               this.a(79);
               this.aq = 0;
               this.aA = this.ay;
            }
      }
   }

   private void bB() {
      this.aB++;
      switch (this.aq) {
         case 0:
            if (b(Game.player.collisionBounds, this.m)) {
               this.p(101);
            }
            break;
         case 2:
            this.cV();
            if (this.aA <= 0) {
               this.p(104);
            }
            break;
         case 3:
            if (this.e()) {
               this.b(true);
            }

            return;
         case 101:
            this.da();
            this.eX = Game.h(Game.player.a - this.a, Game.player.b - 7680 - this.b);
            if (this.eX < 40960) {
               this.G = this.eX == 0 ? 0 : (Game.player.a - this.a) * 256 / this.eX;
               this.H = this.eX == 0 ? 0 : (Game.player.b - 7680 - this.b) * 256 / this.eX;
               this.g(cQ);
               if (b(Game.player.collisionBounds, this.aJ)) {
                  this.p(103);
               }
            } else {
               this.p(102);
            }
            break;
         case 102:
            this.eX = Game.h(Game.player.a - this.a, Game.player.b - 7680 - this.b);
            if (this.eX < 25600) {
               this.p(101);
            }
            break;
         case 103:
            if (this.e()) {
               this.p(104);
            }
            break;
         case 104:
            this.cV();
            if (this.aA <= 0) {
               this.p(101);
            }
      }

      if ((this.c & 1024) == 0 && this.aq != 3 && this.cJ()) {
         this.p(3);
      } else if (this.aq != 2 && this.aq != 3 && b(this.collisionBounds, Game.player.k)) {
         this.p(2);
      } else {
         if (this.aq != 3 && (this.c & 1024) == 0 && b(Game.player.collisionBounds, this.k)) {
            Game.player.a(4, this.B(0), 0, this);
         }
      }
   }

   private void bC() {
      switch (this.aq) {
         case 0:
            this.p(101);
            if (this.ar() == 1) {
               if (this.B(3) == 0) {
                  this.p(103);
               } else {
                  this.p(102);
               }
            }
            break;
         case 3:
            if (this.e()) {
               this.F();
            }
         case 101:
         default:
            break;
         case 102:
         case 106:
            this.cV();
            if (this.aA <= 0) {
               this.p(103);
            }
            break;
         case 103:
            this.cV();
            if (this.n(this.aK, 103)) {
               this.p(104);
            }
            break;
         case 104:
            this.cV();
            if (this.aA <= 0) {
               this.p(105);
            }
            break;
         case 105:
            this.cV();
            if (this.n(this.aK, 105)) {
               this.p(106);
            }
      }

      if (this.cJ()) {
         this.p(3);
      } else {
         if (this.aq != 3) {
            if (this.ar() == 1) {
               if ((this.c & 128) == 0 && this.a(this.aK, Game.player.collisionBounds, this.ac, this.ad)) {
                  Game.player.a(4, this.B(0), 0, this);
                  return;
               }
            } else if (b(Game.player.collisionBounds, this.collisionBounds)) {
               Game.player.a(4, this.B(0), 0, this);
            }
         }
      }
   }

   public final void F() {
      byte var1 = 0;
      if (this.aK == 8) {
         var1 = 0;
      } else if (this.aK == 4) {
         var1 = 4;
      } else {
         var1 = 2;
      }

      this.a((int)var1);
      this.c |= 128;
      if (!this.cJ()) {
         this.p(0);
         this.c &= -129;
      } else {
         if (this.ar() == 1) {
            if (this.aK == 8 || this.aK == 4) {
               this.positionY = this.av;
               return;
            }

            this.positionX = this.ay;
         }
      }
   }

   private void l(int var1, int var2) {
      this.u(var1, var2);
   }

   private void m(int var1, int var2) {
      switch (var1) {
         case 1:
            this.G = var2 == 103 ? 768 : -768;
            return;
         case 2:
            this.G = var2 == 103 ? -768 : 768;
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            return;
         case 4:
            this.H = var2 == 103 ? 768 : -768;
            return;
         case 8:
            this.H = var2 == 103 ? -768 : 768;
      }
   }

   private boolean n(int var1, int var2) {
      if (var2 == 103) {
         switch (var1) {
            case 1:
               if (this.positionX >= this.ac) {
                  this.positionX = this.ac;
                  return true;
               }
               break;
            case 2:
               if (this.positionX <= this.ac) {
                  this.positionX = this.ac;
                  return true;
               }
            case 3:
            case 5:
            case 6:
            case 7:
            default:
               break;
            case 4:
               if (this.positionY >= this.ad) {
                  this.positionY = this.ad;
                  return true;
               }
               break;
            case 8:
               if (this.positionY <= this.ad) {
                  this.positionY = this.ad;
                  return true;
               }
         }
      } else {
         switch (var1) {
            case 1:
               if (this.positionX <= this.ay) {
                  this.positionX = this.ay;
                  return true;
               }
               break;
            case 2:
               if (this.positionX >= this.ay) {
                  this.positionX = this.ay;
                  return true;
               }
            case 3:
            case 5:
            case 6:
            case 7:
            default:
               break;
            case 4:
               if (this.positionY <= this.av) {
                  this.positionY = this.av;
                  return true;
               }
               break;
            case 8:
               if (this.positionY >= this.av) {
                  this.positionY = this.av;
                  return true;
               }
         }
      }

      return false;
   }

   private void bD() {
      if (this.animationId != 0) {
         if (this.aG != -1 && this.r != null) {
            this.b(Game.i(this.aG));
         }

         if (this.y > 0 && this.z > 0 && this.s == null && this.t == null) {
            this.s = Game.i(this.y);
            this.t = Game.i(this.z);
            this.s.p(106);
            this.t.p(106);
            cV = this.s.positionX;
            cX = this.t.positionX;
            cW = cX + cV >> 1;
            this.x = Game.i(this.D);
         }

         switch (this.aq) {
            case 0:
               if (this.ar() == 0) {
                  this.p.c();
                  this.p(101);
               } else {
                  this.p(102);
                  E = this;
                  F = this.as;
               }
               break;
            case 2:
               this.cV();
               this.aL = this.aD % 140 >= 70 ? 1 : 0;
               if (this.aD <= 0) {
                  this.aL = 0;
                  if (this.aA <= 0) {
                     this.p(110);
                  } else {
                     this.aq = 109;
                  }
               }
               break;
            case 3:
               this.aL = this.aA % 140 >= 70 ? 1 : 0;
               Game.h(13);
            case 101:
            default:
               break;
            case 102:
               this.cV();
               if (this.aA <= 0) {
                  if (this.as >= F * 2 / 3) {
                     this.p(106);
                  } else {
                     this.p(103);
                  }
               }
               break;
            case 103:
               this.cV();
               if (this.aA <= 0) {
                  this.p(104);
               }

               this.v.positionX = this.q.positionX;
               this.v.positionY = this.q.positionY;
               break;
            case 104:
               if (this.aA <= 1300 && this.aA >= 300) {
                  this.ay = this.ay + this.ax;
               }

               a(this.q.positionX, this.q.positionY, this.ay, this.av, cU);
               this.ay = cU[0];
               this.av = cU[1];
               this.u.positionX = this.ay;
               this.u.positionY = this.av;
               this.v.positionX = this.q.positionX;
               this.v.positionY = this.q.positionY;
               if (b(this.q.positionX, this.q.positionY, this.ay, this.av, Game.player.collisionBounds)) {
                  Game.player.a(4, this.B(1), 0, this);
               }

               this.cV();
               if (this.aA <= 0) {
                  this.p(105);
               }
               break;
            case 105:
               this.cV();
               if (this.aA <= 0) {
                  this.db();
                  this.p(113);
               }
               break;
            case 106:
               this.G = this.D(512);
               this.b(false, false);
               this.cV();
               this.cW();
               if (this.aA <= 0
                  && (
                     this.G == 0
                        || this.eL
                        || this.eM
                        || l(this.positionX - Game.player.positionX) < this.collisionBounds[2] - this.collisionBounds[0] >> 1 && !i(this.G, Game.player.G)
                  )) {
                  this.p(107);
               }

               if (this.aB <= 0) {
                  this.aB += 2000;
               }

               this.positionY = this.av + (Game.m(this.aB * 360 / 2000) * 10 >> 8);
               break;
            case 107:
               this.cV();
               if (this.aA <= 0) {
                  this.p(108);
               } else {
                  this.positionX = this.ay + Game.i(-2, 3);
                  this.positionY = this.av + Game.i(-1, 2);
               }
               break;
            case 108:
               this.b(false, true);
               if (b(Game.player.collisionBounds, this.collisionBounds)) {
                  Game.player.a(4, this.B(2), 0, this);
                  this.az = 1;
               }

               if (this.bL) {
                  Game.e(6);
                  if (this.az == 0) {
                     this.p(109);
                  } else {
                     this.p(110);
                  }

                  this.a(39, 17, 22, this.collisionBounds[0] - this.collisionBounds[2] >> 1, this.collisionBounds[3] - this.collisionBounds[1] >> 1, 302, false);
                  this.a(39, 17, 22, this.collisionBounds[2] - this.collisionBounds[0] >> 1, this.collisionBounds[3] - this.collisionBounds[1] >> 1, 302, true);
               }
               break;
            case 109:
               this.cV();
               if (this.aA <= 0) {
                  this.p(110);
               } else {
                  this.aE();
               }
               break;
            case 110:
               if (this.e()) {
                  this.a(1);
               }

               if (this.as < F * 2 / 3) {
                  if (this.fu >= 84) {
                     this.p.f(0, 10);
                     this.H = -768;
                  } else if (this.fu >= 63) {
                     this.p.f(1, 10);
                  } else if (this.fu >= 42) {
                     this.p.f(2, 10);
                  } else if (this.fu >= 21) {
                     this.p.f(3, 10);
                  }

                  int var1 = this.n[0] - this.ay;
                  int var2 = this.n[1] - this.av;
                  int var3 = Game.l(this.fu * -this.aw);
                  int var4 = Game.m(this.fu * -this.aw);
                  int var5 = var1 * var3 - var2 * var4 >> 8;
                  int var6 = var1 * var4 + var2 * var3 >> 8;
                  this.positionX = this.ay + var5;
                  this.positionY = this.av + var6;
                  this.fu = this.fu + (this.aB >> 1);
                  if (this.aB >> 1 > 2) {
                     this.aB--;
                  }

                  if (this.fu > 120) {
                     this.positionY = this.ad;
                     this.positionX = this.ac;
                     this.n = null;
                     if (this.as < F / 3) {
                        this.p(112);
                     } else {
                        this.p(102);
                     }
                  }
               } else if (this.positionY <= this.ad) {
                  this.positionY = this.ad;
                  this.p(106);
               }
               break;
            case 112:
               if (this.s.aq == 106 && this.t.aq == 106) {
                  this.p(102);
               }
               break;
            case 113:
               if (this.fu >= 240) {
                  this.p.c();
               } else if (this.fu >= 220) {
                  this.p.f(4, 10);
               } else if (this.fu >= 170) {
                  Game.e(10);
                  this.p.f(5, 10);
               } else if (this.fu >= 140) {
                  this.p.f(4, 10);
               } else if (this.fu >= 110) {
                  this.p.c();
               } else if (this.fu >= 80) {
                  this.p.f(3, 10);
               } else if (this.fu >= 50) {
                  this.p.f(2, 10);
               } else if (this.fu >= 20) {
                  this.p.f(1, 10);
               }

               if (this.fu >= 180 && this.n[0] != this.ay) {
                  this.ay = this.n[0];
                  this.fv = this.n[1];
               }

               this.positionX = this.ay - (Game.l(this.fu) * this.aw * this.fv >> 8);
               this.positionY = this.av - (Game.m(this.fu) * this.fv >> 8);
               if (this.fu >= 360) {
                  this.n = null;
                  this.p(106);
               }

               this.fu = this.fu + (this.aB >> 1);
         }

         if (this.q != null) {
            this.q.ac = this.positionX + 0;
            this.q.ad = this.positionY + 0;
            this.q.positionX = this.positionX;
            this.q.positionY = this.positionY;
         }

         this.a(this.q, Game.player);
      }
   }

   private void o(int var1, int var2) {
      this.a(8, 6, 0, 0, 0, 1, false);
      if (this.q == null) {
         this.a(aU);
         this.q.a(0);
         this.q.ac = this.positionX + var1;
         this.q.ad = this.positionY + var2;
         this.q.ap = 100;
      }
   }

   private void a(Actor var1, Actor var2) {
      if (var1 != null) {
         this.eX = Game.h(var2.positionX - var1.ac, var2.positionY - var1.ad);
         int var3 = (this.eX == 0 ? 0 : (var2.positionX - var1.ac) * 7 / this.eX) * h(this.eX, 110) / 110;
         int var4 = (this.eX == 0 ? 0 : (var2.positionY - var1.ad) * 7 / this.eX) * h(this.eX, 110) / 110;
         var1.positionX = var1.ac + var3;
         var1.positionY = var1.ad + var4;
         if (this.aq == 2) {
            var1.positionX = var1.positionX + Game.i(-3, 4);
            var1.positionY = var1.positionY + Game.i(-2, 3);
         }
      }
   }

   public final void G() {
      int var1 = 0;
      int var2 = 0;
      if (this.type == 8) {
         var1 = 5;
      } else if (this.type == 11) {
         var1 = -50;
      } else if (this.type == 100) {
         var1 = -50;
      } else if (this.type == 83 && dp != null && dp[10] != null) {
         var1 = dp[10].positionY - this.positionY;
         var2 = dp[10].positionX - this.positionX;
      }

      this.cV();
      if (Game.P == -2) {
         if (this.type == 8 || this.type == 11 || this.type == 100 || this.type == 83) {
            if (this.x != null) {
               this.x.positionX = this.positionX;
               this.x.positionY = this.positionY;
               this.x.aG();
               this.x = null;
            }

            if (this.type == 8) {
               Game.e(this.q);
               this.q = null;
            } else if (this.type == 83) {
               this.p(156);
            } else {
               de = false;
               dg = 1;
               di = false;
            }
         }

         if (this.type != 83) {
            E = null;
            Game.U();
         }

         for (int var3 = 0; var3 < db; var3++) {
            this.a(35, 31, 5, Game.i(-20, 20) + var2, -Game.i(-40, 10) + var1, 308, false);
            if (this.type == 8) {
               aU.aC = ee;
            } else if (this.type == 11) {
               aU.aC = ef;
            } else if (this.type == 83) {
               aU.aC = eg;
            }
         }

         if (this.type != 83) {
            Game.e(this);
            Game.c(this);
         }

         Game.s();
      } else if (Game.P < cY) {
         int var7 = Game.P * 320 / cY;

         for (int var4 = 0; var4 < db; var4++) {
            Game.graphics.setColor(16777215);
            Game.b(this.positionX - Game.cameraX - var7 + var2, this.positionY - Game.cameraY - var7 + var1, var7 << 1, var7 << 1, dc[var4], dd[var4]);
         }

         Game.graphics.setColor(0);
         Game.b(this.positionX - Game.cameraX - 320 + var2, this.positionY - Game.cameraY - 320 + var1, 640, 640, 0, 360);
      } else if (Game.P < cY + cZ + da) {
         if (Game.aN <= 0) {
            Game.e(8);
         }

         boolean var8 = false;
         int var9 = (Game.P - cY) * cZ;

         for (int var5 = 0; var5 < db; var5++) {
            Game.graphics.setColor(16777215);
            Game.b(this.positionX - Game.cameraX - 320 + var2, this.positionY - Game.cameraY - 320 + var1, 640, 640, var9 + dc[var5], dd[var5]);
         }

         int var6 = 0;
         int var10;
         if (Game.P < cY + cZ) {
            var10 = (Game.P - cY) * 320 / cZ;
            var6 = ((var6 = ((Game.P - cY) * 128 + 128) / cZ) << 16 | 16776960) + (var6 << 8 | 16776960);
         } else {
            var10 = (Game.P - cY - cZ) * 320 / da;
            var6 = ((cY + cZ + da - Game.P) * 128 + 128) / da << 16 | 0xFF0000;
         }

         Game.graphics.setColor(var6);
         Game.b(this.positionX - Game.cameraX - var10 + var2, this.positionY - Game.cameraY - var10 + var1, var10 << 1, var10 << 1, 0, 360);
         if (!Game.dc) {
            if (Game.i(0, 5) == 0) {
               this.c(7, this.collisionBounds);
               if (this.type == 83 && dp != null && dp[10] != null) {
                  aU.positionX += var2;
                  aU.positionY += var1;
               }
            }

            this.a(39, 17, this.aF(), -Game.i(-20, 20), -Game.i(10, 50), 302, Game.random.nextInt() % 2 == 0);
            if (this.type == 83 && dp != null && dp[10] != null) {
               aU.positionX += var2;
               aU.positionY += var1;
            }

            aU.c |= 4096;
         }
      } else {
         Game.P = -3;
         if (Game.dc) {
            Game.dc = false;
            Game.dd = -1;
         }
      }

      Game.P++;
   }

   private void c(int var1, int[] var2) {
      this.a(39, 17, var1, 0, 0, 302, false);
      aU.positionX = Game.i(this.collisionBounds[0], this.collisionBounds[2]);
      aU.positionY = Game.i(this.collisionBounds[1], this.collisionBounds[3]);
      aU.c |= 4096;
   }

   private void a(int var1, int var2, int var3, int var4, int var5, int[] var6, boolean var7) {
      int var8;
      int var9;
      if (!var7) {
         var8 = -(var6.length >> 1);
         var9 = var6.length;
      } else {
         var8 = -var6.length;
         var9 = var6.length << 1;
      }

      for (int var10 = 0; var10 < var9; var10++) {
         if (!var7) {
            bt.setColor(var6[var10]);
         } else {
            bt.setColor(var6[var10 >> 1]);
         }

         if (l(var1 - var3) > l(var2 - var4)) {
            Game.d(var1 - Game.cameraX, var2 - Game.cameraY + (var5 == 1 ? 0 : var8), var3 - Game.cameraX, var4 - Game.cameraY + var8);
         } else {
            Game.d(var1 - Game.cameraX + (var5 == 1 ? 0 : var8), var2 - Game.cameraY, var3 - Game.cameraX + var8, var4 - Game.cameraY);
         }

         var8++;
      }
   }

   public static final void H() {
      bt.setColor(16777215);
      Game.c(Game.player.collisionBounds[0] - Game.cameraX, Game.player.collisionBounds[1] - Game.cameraY - 20 + 0, 42, 5);
      bt.setColor(16711680);
      Game.b(Game.player.collisionBounds[0] - Game.cameraX + 1, Game.player.collisionBounds[1] - Game.cameraY - 19 + 0, cL * 40 / 40, 4);
   }

   private void bE() {
      if (this.aG != -1 && this.r == null) {
         this.b(Game.i(this.aG));
      }

      switch (this.aq) {
         case 0:
            if (b(Game.player.collisionBounds, this.m)) {
               if (this.ar() == 0) {
                  if (this.r != null && this.r.type == 51) {
                     this.aB = this.r.positionX
                        + (
                           (this.facingLeft ? this.r.collisionBounds[2] - this.r.collisionBounds[0] : this.r.collisionBounds[0] - this.r.collisionBounds[2])
                              >> 1
                        );
                  }

                  this.p(101);
               } else if (this.ar() == 1) {
                  this.p(102);
               } else if (this.ar() == 2) {
                  this.p(107);
               }
            }
            break;
         case 3:
            this.b((Actor)null);
            Game.e(this);
            return;
         case 101:
            this.cV();
            if (this.aA <= 0) {
               this.p(3);
            }
            break;
         case 102:
            if (this.r != null && this.r.type == 54 && this.r.aq == 101) {
               this.p(103);
            }
            break;
         case 103:
            this.cV();
            if (this.aA <= 0) {
               this.p(104);
            }
            break;
         case 104:
            this.p(105);
            break;
         case 105:
            if (this.e()) {
               this.p(3);
            }
         case 106:
         default:
            break;
         case 107:
            if (this.s.aq == 102) {
               this.p(108);
            }
            break;
         case 108:
            if (this.positionY <= this.av) {
               this.positionY = this.av;
               this.p(109);
            }
            break;
         case 109:
            this.cV();
            if (this.B(1) > 0 && this.aA <= 0) {
               this.p(110);
            }
            break;
         case 110:
            if (this.positionY >= this.ad) {
               this.positionY = this.ad;
               if (this.ar() == 2) {
                  if (this.B(1) > 0) {
                     this.p(111);
                  } else {
                     this.p(3);
                  }
               } else if (this.ar() == 3) {
                  this.p(106);
               }
            }
            break;
         case 111:
            this.cV();
            if (this.aA <= 0) {
               if (b(Game.player.collisionBounds, this.m)) {
                  this.p(107);
               } else {
                  this.p(0);
               }
            }
            break;
         case 112:
            this.cV();
            if (this.aA <= 0) {
               this.p(110);
               this.a(false);
            }

            if (this.aA / 250 < this.ax) {
               this.ax--;
               this.c(33, this.collisionBounds);
            }
      }

      if ((this.c & 128) == 0 && this.aq != 0 && this.aq != 111 && this.aq != 110 && this.aq != 112 && this.aq != 3) {
         if (b(Game.player.collisionBounds, this.collisionBounds)) {
            Game.player.a(4, this.B(0), -2560, this);
         }

         this.bF();
      }
   }

   private void bF() {
      for (int var1 = 0; var1 < Game.ck; var1++) {
         Actor var2;
         if ((var2 = Game.cj[var1]) != null && (var2.c & 1024) == 0 && var2.aq != 7 && var2.aq != 3 && var2.aq != 5 && var2.aq != 6) {
            if (var2.M() && var2.collisionBounds != null && b(var2.collisionBounds, this.collisionBounds)) {
               var2.s(3);
            } else if (var2.type == 30 && b(var2.collisionBounds, this.collisionBounds) && var2.aq != 103 && var2.aq != 102) {
               var2.facingLeft = this.positionX > var2.positionX;
               var2.aq = 102;
               if (var2.aF == 0) {
                  var2.a(6);
               }
            }
         }
      }

      if (b(Game.player.collisionBounds, this.collisionBounds)) {
         Game.player.a(4, this.B(0), this.ar() == 0 ? -2560 : 0, this);
      }
   }

   private void bG() {
      switch (this.aq) {
         case 0:
            if (b(Game.player.collisionBounds, this.m)) {
               if (this.ar() == 0) {
                  this.p(101);
               } else {
                  this.p(102);
               }
            }
            break;
         case 101:
            this.cV();
            if (this.aA <= 0) {
               this.p(102);
            }
            break;
         case 102:
            if (this.e()) {
               this.p(103);
            }
            break;
         case 103:
            this.cV();
            if (this.aA <= 0) {
               if (this.ar() == 0) {
                  this.p(104);
               } else {
                  this.p(107);
                  this.aF = 1;
               }
            }
            break;
         case 104:
            if (this.B(2) > 0) {
               this.cV();
               if (this.aA <= 0) {
                  this.p(105);
               }
            }
            break;
         case 105:
            if (this.e()) {
               this.p(106);
            }
            break;
         case 106:
            this.cV();
            if (this.aA <= 0) {
               this.p(102);
            }
            break;
         case 107:
         case 109:
            this.cV();
            if (this.aA <= 0) {
               this.aA = 0;
            }

            int var1;
            if (this.aq == 107) {
               var1 = this.B(3) * (this.B(1) - this.aA) / this.B(1);
            } else {
               var1 = this.B(3) * this.aA / this.B(1);
            }

            a(this.positionX, this.positionY, this.ay, this.av, var1, cU);
            this.ac = cU[0];
            this.ad = cU[1];
            if (this.aA <= 0) {
               this.p(this.aq == 107 ? 108 : 110);
            }
            break;
         case 108:
         case 110:
            this.cV();
            if (this.aA <= 0) {
               this.p(this.aq == 108 ? 109 : 107);
            }
      }

      if (this.aF == 1) {
         a(this.positionX, this.positionY, this.ac, this.ad, cU);
         this.ac = cU[0];
         this.ad = cU[1];
         if (b(this.positionX, this.positionY, this.ac, this.ad, Game.player.collisionBounds)) {
            Game.player.a(4, this.B(0), 0, this);
         }

         if (this.u == null) {
            this.a(39, 17, 29, 0, 0, 302, false);
            this.u = aU;
         }

         this.u.positionX = this.ac;
         this.u.positionY = this.ad;
      } else {
         if (this.u != null) {
            Game.e(this.u);
            this.u = null;
         }
      }
   }

   private void bH() {
      if (this.D != -1 && this.x == null) {
         this.x = Game.i(this.D);
      }

      switch (this.aq) {
         case 0:
            if (b(Game.player.collisionBounds, this.m)) {
               if (this.ar() == 1) {
                  this.p(101);
                  dg = 1;
                  dh = 1;
                  de = false;
                  dk = null;
                  E = this;
                  F = this.as;
                  dn = 103;
               } else {
                  this.p(154);
               }
            }
            break;
         case 1:
            this.J = 320;
            if (this.H > 4096) {
               this.H = 4096;
            }

            if (this.bL) {
               if (this.ar() == 2) {
                  this.p(154);
               } else {
                  this.p(101);
               }
            }
            break;
         case 2:
            this.cV();
            this.dc();
            if (!this.bL) {
               this.p(1);
            } else if (this.az >= 2 && this.ar() == 1) {
               this.p(118);
               this.az = 0;
            } else if (this.e()) {
               if (this.ar() == 2) {
                  this.p(155);
               } else if (this.ar() == 0) {
                  this.p(155);
               } else if (this.ar() == 1) {
                  this.p(101);
               }
            }
            break;
         case 3:
            this.cV();
            if (this.ar() != 1) {
               this.b(true);
            } else {
               Game.h(13);
               df = false;
            }
            break;
         case 4:
            this.cV();
            this.G = 0;
            this.O = false;
            if (this.animationId == 29) {
               if (this.e()) {
                  this.p(9);
               }
            } else if (this.aA <= 0 && this.animationId != 29) {
               this.p(159);
               Game.player.a(1);
               Game.player.au();
               cL = 0;
               br = false;
            } else if (this.v(65568)) {
               this.a(29);
               Game.player.a(42);
               Game.player.au();
               cL = 0;
               br = false;
            }
         case 5:
         case 6:
         case 7:
         case 8:
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
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         case 100:
         case 131:
         case 132:
         case 133:
         case 134:
         case 135:
         case 136:
         case 137:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 145:
         case 146:
         case 147:
         case 148:
         case 149:
         default:
            break;
         case 9:
            this.a(true, true);
            break;
         case 101:
            this.dc();
            if (!this.bL) {
               this.p(1);
            }

            if (this.as <= F * (3 - dg) / 3 && this.ar() == 1) {
               de = true;
               dg++;
               df = false;
               this.p(118);
            } else if (dn == 110) {
               if (!this.facingLeft && Game.g(this.collisionBounds[0] / 20 - 1, this.positionY / 20) == 24
                  || this.facingLeft && Game.g(this.collisionBounds[2] / 20 + 1, this.positionY / 20) == 24) {
                  if (dg > 1 && !dm) {
                     dm = true;
                     this.p(115);
                  } else {
                     this.p(108);
                  }
               } else {
                  this.p(118);
               }
            } else if (dn == 103) {
               if (l(this.positionX - Game.player.positionX) <= 140) {
                  this.p(102);
               } else {
                  dn = 110;
                  dm = false;
               }
            } else if (dn == 125) {
               dn = 103;
               this.p(125);
            }
            break;
         case 102:
            if (this.e()) {
               this.p(103);
            }
            break;
         case 103:
            this.cV();
            if (this.aA <= 0) {
               this.p(104);
            } else if (b(Game.player.collisionBounds, this.k)) {
               this.p(106);
            } else if (this.G < 0 && this.eL || this.G > 0 && this.eM) {
               this.p(104);
            }
            break;
         case 104:
            if (this.e()) {
               this.p(105);
            }
            break;
         case 105:
            this.cV();
            if (this.aA <= 0) {
               this.p(101);
            }
            break;
         case 106:
            if (this.e()) {
               this.p(107);
            }
            break;
         case 107:
            this.cV();
            if (this.aA <= 0) {
               this.p(101);
               Game.player.a(4, this.B(1), 0, this);
               di = true;
               aM = false;
               this.b((Actor)null);
               this.ae = false;
            } else {
               if (Game.G(65568)) {
                  this.aB++;
               }

               if (this.aB >= 8) {
                  this.p(119);
                  aM = false;
                  this.b((Actor)null);
                  this.ae = false;
               } else if (4500 - this.aA > this.ax * 1500) {
                  this.ax++;
                  Game.player.a(4, this.B(1), 0, this);
                  di = true;
               }

               if (this.e()) {
                  this.a(39, 17, this.aF(), 0, -Game.i(10, 50), 302, Game.random.nextInt() % 2 == 0);
               }
            }
            break;
         case 108:
            if (this.e()) {
               this.p(109);
            }
            break;
         case 109:
            if (this.e()) {
               this.p(110);
            }
            break;
         case 110:
            if (b(Game.player.collisionBounds, this.k)) {
               Game.player.a(42, this.B(1), -3072, this);
               di = true;
               this.p(111);
               Game.a(0, 5);
            } else if (this.G < 0 && this.eL || this.G > 0 && this.eM) {
               this.p(113);
               Game.a(7, 0);
            }
            break;
         case 111:
            this.cV();
            if (this.aA <= 0) {
               this.O = true;
               this.p(112);
            }
            break;
         case 112:
            this.cV();
            if (this.aA <= 0) {
               this.p(101);
               this.P = true;
               Game.c(this);
            }
            break;
         case 113:
            this.cV();
            this.cW();
            dl = dl - Game.h;
            if (this.aA <= 0) {
               this.p(114);
            }

            if (this.aB <= 0) {
               this.aB = 3500;
               this.a(56, 19, 11, this.facingLeft ? -10 : 10, 0, 302, false);
               aU.p(102);
               aU.aL = 0;
            }

            if (dl <= 0) {
               dl = 3500;
               this.a(56, 19, 11, this.facingLeft ? -10 : 10, 0, 302, false);
               aU.p(102);
               aU.aL = 0;
            }
            break;
         case 114:
            this.I = this.facingLeft ? -256 : 256;
            if (this.facingLeft && this.G <= 0 || !this.facingLeft && this.G >= 0) {
               this.p(101);
               Game.c(this);
            }
            break;
         case 115:
            if (this.e()) {
               this.p(116);
            }
            break;
         case 116:
            this.cV();
            if (this.aA <= 0) {
               if ((this.ar() == 0 || dk == null) && !this.fx) {
                  this.fx = true;
                  this.a(39, 17, 42, this.facingLeft ? -40 : 40, 0, 302, false);
                  aU.G = this.facingLeft ? -2048 : 2048;
                  if (this.ar() == 1) {
                     dk = aU;
                  }

                  this.aF = 1;
               }

               if (this.e()) {
                  this.p(117);
               }
            }
            break;
         case 117:
            this.cV();
            if (this.aA <= 0) {
               if (this.ar() == 0) {
                  this.p(150);
               } else {
                  this.p(108);
               }
            }
            break;
         case 118:
            if (this.e()) {
               this.p(119);
            }
            break;
         case 119:
            this.J = 320;
            if (this.H >= 0) {
               this.p(120);
            }
            break;
         case 120:
            this.J = 320;
            if (this.bL) {
               this.p(121);
            }
            break;
         case 121:
            if (this.e()) {
               if (de) {
                  this.p(122);
               } else if (this.ar() == 0) {
                  this.p(101);
               } else {
                  this.p(101);
               }
            }
            break;
         case 122:
            if (Game.aN <= 0) {
               Game.e(3);
            }

            if (Game.player.bL) {
               this.p(123);
               aN = true;
               Game.player.db();
               Game.e(this.s);
               this.s = null;
            }
            break;
         case 123:
            if (Game.aN <= 0) {
               Game.e(3);
            }

            this.cV();
            if (this.aA <= 0) {
               if (this.s == null) {
                  Game.e(this.s);
                  this.s = null;
                  Game.player.a(39, 17, 37, 0, 0, 302, false);
                  this.s = aU;
                  aM = false;
               }

               if (this.e()) {
                  this.p(124);
                  Game.player.c(25, this.ac + 0, this.ad + -75);
               }
            }
            break;
         case 124:
            this.cV();
            if (this.aA <= 0) {
               this.p(125);
               Game.player.au();
               Game.player.c(14, this.ac + 0, this.ad + -75);
            }
            break;
         case 125:
            if (this.e()) {
               this.p(126);
            }
            break;
         case 126:
            if (this.e()) {
               this.p(127);
            }
            break;
         case 127:
            if (this.positionY > this.av || (this.G > 0 || !this.eL) && (this.G < 0 || !this.eM)) {
               this.G = this.facingLeft ? 2048 : -2048;
               this.H = -1792;
            } else {
               this.p(128);
            }
            break;
         case 128:
            this.b(true, true);
            if (this.e()) {
               this.p(129);
            }
            break;
         case 129:
            if (b(Game.player.collisionBounds, this.collisionBounds)) {
               Game.player.a(4, this.B(1), -3072, this);
               di = true;
            }

            if (this.bL) {
               Game.a(0, 5);
               this.p(130);
               Game.player.au();
               if (this.s != null && this.s.animationId != 41) {
                  this.s.a(41);
               }
            }
            break;
         case 130:
            this.cV();
            if (this.aA <= 0) {
               de = false;
               this.p(1);
            }
            break;
         case 150:
            if (this.e()) {
               this.p(151);
            }
            break;
         case 151:
            this.cV();
            this.G = this.facingLeft ? -1792 : 1792;
            if (this.aA <= 0) {
               this.p(152);
            }

            if (b(Game.player.collisionBounds, this.k)) {
               Game.player.a(4, this.B(1), -3072, this);
               this.p(152);
               this.aF = 2;
            }
            break;
         case 152:
            if (this.e()) {
               this.p(153);
               this.aB = 2000;
               if (this.aF == 2) {
                  this.aA = 500;
                  this.aB = 0;
               }

               this.aF = 0;
            }

            if (b(Game.player.collisionBounds, this.k)) {
               Game.player.a(4, this.B(1), -3072, this);
            }
            break;
         case 153:
            this.cV();
            if (this.aA <= 0) {
               this.p(154);
            }
            break;
         case 154:
            this.dc();
            this.g(cR);
            if (b(Game.player.collisionBounds, this.aJ)) {
               if (this.ar() == 0) {
                  if (this.aF == 0) {
                     this.p(115);
                  } else {
                     this.p(150);
                  }

                  this.aF = 0;
               } else {
                  this.p(150);
               }
            }
            break;
         case 155:
            if (this.e()) {
               this.p(156);
            }
            break;
         case 156:
            this.J = 320;
            if (this.H >= 0) {
               this.p(157);
            }
            break;
         case 157:
            this.J = 320;
            if (this.bL) {
               this.p(158);
            }
            break;
         case 158:
            if (this.e()) {
               this.p(154);
            }
            break;
         case 159:
            if (this.e()) {
               this.facingLeft = !this.facingLeft;
               this.O = true;
               if (this.aB > 0) {
                  this.p(153);
               } else {
                  this.p(155);
               }
            }
      }

      if (this.ar() != 2) {
         if (this.aq != 0 && this.aq != 128) {
            this.h(true);
         }
      } else {
         this.h(false);
      }

      dj = dj - Game.h;
      if (this.ar() != 1) {
         this.cW();
      }

      if (this.r != null && this.r.type == 0) {
         this.b(this.r, 19, 0);
         this.r.db();
      }

      if (this.ar() != 1) {
         if (this.aq == 153 || this.aq == 2 || this.aq == 117) {
            this.e(true);
         }
      } else if ((this.aq == 105 || this.aq == 113 || this.aq == 112 || this.aq == 2 || this.aq == 117) && !de) {
         this.e(aE);
      }

      if (this.ar() == 1) {
         if (dg == 1 && this.as <= F / 3) {
            this.as = F / 3 + 1;
            df = true;
         } else if (dg == 2 && this.as <= 0) {
            this.as = 10;
            df = true;
         }
      }

      boolean var1 = b(Game.player.collisionBounds, this.k);
      boolean var2 = b(Game.player.collisionBounds, this.collisionBounds);
      if ((var1 || var2) && this.aq != 3 && this.aq == 4 && this.animationId == 30 && var1) {
         Game.player.a(4, this.B(1), 0, this);
      }

      if (di && dk != null && dk.animationId >= 44 && dk.animationId <= 46) {
         dk.a(47);
         dk = null;
         aN = false;
      }

      this.e(25, -30, -75);
      if (this.s != null && aM && this.aq != 122 && this.aq != 123) {
         this.s = null;
         aN = false;
         if (Game.player.q != null && Game.player.q.animationId == 25) {
            Game.player.au();
         }
      }
   }

   private void bI() {
      switch (this.animationId) {
         case 37:
            if (this.e()) {
               this.a(38);
               return;
            }
            break;
         case 38:
            if (this.a(0, this.collisionBounds)) {
               this.a(39);
               return;
            }
            break;
         case 39:
            if (this.e()) {
               this.a(40);
               return;
            }
            break;
         case 40:
            if (this.a(0, this.collisionBounds)) {
               this.a(41);
               return;
            }
            break;
         case 41:
            if (this.e()) {
               aN = false;
               Game.e(this);
               return;
            }
            break;
         case 42:
            this.b(true, false);
            if (this.facingLeft ? this.eL : this.eM) {
               Game.e(this);
               dk = null;
               return;
            }

            if (this.a(0, this.collisionBounds)) {
               this.a(43);
               this.db();
               return;
            }

            if (b(this.collisionBounds, Game.player.collisionBounds)) {
               aN = true;
               if (aO != null) {
                  Game.e(aO);
               }

               aO = this;
               Game.player.db();
               Game.player.aG();
               this.a(44);
               this.positionX = Game.player.positionX;
               this.db();
               return;
            }
            break;
         case 43:
            if (this.e()) {
               Game.e(this);
               dk = null;
               return;
            }
            break;
         case 44:
            if (this.a(0, this.collisionBounds)) {
               this.a(46);
               return;
            }
            break;
         case 45:
            if (this.e()) {
               this.a(46);
               return;
            }
            break;
         case 46:
            if (this.a(0, this.collisionBounds)) {
               this.a(47);
               dk = null;
               aN = false;
               aO = null;
               return;
            }
            break;
         case 47:
            if (this.e()) {
               Game.e(this);
            }
      }
   }

   private void e(int var1, int var2, int var3) {
      if (this.ae) {
         if (this.q == null) {
            this.a(21, 16, var1, this.facingLeft ? var2 : -var2, var3, 302, this.facingLeft);
            this.a(aU);
            return;
         }
      } else if (this.q != null) {
         Game.e(this.q);
         this.a((Actor)null);
      }
   }

   private void bJ() {
      int var1;
      var1 = this.ar();
      label127:
      switch (this.aq) {
         case 0:
            if (var1 == 0) {
               this.ad = this.positionY;
               this.p(101);
            } else if (b(Game.player.collisionBounds, this.m)) {
               switch (var1) {
                  case 1:
                     this.p(103);
                     break label127;
                  case 2:
                     this.p(102);
                     break label127;
                  case 3:
                     this.p(106);
                     this.c |= 16;
                     this.ac = this.positionX;
                     this.ad = this.positionY;
                     this.aK = this.facingLeft ? 1 : 2;
               }
            }
            break;
         case 2:
            if (var1 == 0) {
               if (this.e()) {
                  this.p(0);
               }
            } else if (var1 == 1) {
               this.cV();
               if (this.e()) {
                  this.a(0);
               }

               if (this.aA <= 0) {
                  this.p(103);
               }
            }
            break;
         case 3:
            if (var1 != 1 && var1 != 3) {
               this.b(true);
            } else {
               for (int var2 = 0; var2 < this.ay << 1; var2++) {
                  this.a(35, 31, 0, Game.i(-20, 20), -Game.i(-40, 10), 308, false);
               }

               for (int var3 = 0; var3 < this.aw << 1; var3++) {
                  this.a(35, 31, 2, Game.i(-20, 20), -Game.i(-40, 10), 308, false);
               }

               for (int var4 = 0; var4 < this.ax << 1; var4++) {
                  this.a(35, 31, 4, Game.i(-20, 20), -Game.i(-40, 10), 308, false);
               }

               for (int var5 = 0; var5 < this.az << 1; var5++) {
                  this.a(35, 31, 5, Game.i(-20, 20), -Game.i(-40, 10), 308, false);
               }

               this.b(true);
            }

            this.c &= -17;
            this.ak = 10;
            return;
         case 4:
         case 8:
         case 101:
         default:
            break;
         case 9:
            this.a(true, true);
            break;
         case 102:
            if (this.e()) {
               this.p(1792, -256);
               this.p(104);
            }
            break;
         case 103:
            this.eX = Game.h(Game.player.eA - this.positionX, Game.player.eB - this.positionY);
            this.G = this.eX == 0 ? 0 : (Game.player.eA - this.positionX) * 256 / this.eX;
            this.H = this.eX == 0 ? 0 : (Game.player.eB - this.positionY) * 256 / this.eX;
            break;
         case 104:
            this.cV();
            if (this.aA <= 0) {
               this.p(102);
            }
            break;
         case 105:
            if (this.e()) {
               this.p(103);
            }
            break;
         case 107:
            if (this.e()) {
               this.p(106);
            }
         case 106:
            this.cV();
            this.G = this.aK == 1 ? -512 : 512;
            this.positionY = (30 * Game.m(2 * (this.ac - this.positionX)) >> 8) + this.ad;
            if (!b(Game.aJ.collisionBounds, this.collisionBounds)) {
               this.c &= -17;
               this.b(false);
               return;
            }
      }

      if (!this.p()) {
         if (var1 == 0) {
            this.aE();
         } else {
            this.e(false);
         }

         if (b(Game.player.collisionBounds, this.k) || b(Game.player.collisionBounds, this.collisionBounds)) {
            Game.player.a(4, this.B(1), 0, this);
         }
      }
   }

   private void bK() {
      if (this.e()) {
         Game.e(this);
      } else {
         if (i(this.G, this.ay)) {
            this.G = 0;
         } else {
            this.I = this.ay;
         }

         if (i(this.H, this.av)) {
            this.H = 0;
         } else {
            this.J = this.av;
         }

         if (b(Game.player.collisionBounds, this.collisionBounds)) {
            Game.player.a(4, this.aB, 0, this);
         }
      }
   }

   private void p(int var1, int var2) {
      for (int var3 = 0; var3 < 4; var3++) {
         this.a(-100, 11, 0, 0, 0, 302, this.facingLeft);
         aU.positionX = (this.collisionBounds[0] + this.collisionBounds[2] >> 1)
            + (var3 / 2 == 1 ? 0 : ((var3 % 2 << 1) - 1) * (this.collisionBounds[0] - this.collisionBounds[2] >> 1));
         aU.positionY = (this.collisionBounds[1] + this.collisionBounds[3] >> 1)
            + (var3 / 2 == 0 ? 0 : ((var3 % 2 << 1) - 1) * (this.collisionBounds[1] - this.collisionBounds[3] >> 1));
         aU.G = (var3 / 2 == 1 ? 0 : (var3 % 2 << 1) - 1) * -var1;
         aU.H = (var3 / 2 == 0 ? 0 : (var3 % 2 << 1) - 1) * -var1;
         aU.ay = (var3 / 2 == 1 ? 0 : (var3 % 2 << 1) - 1) * -var2;
         aU.av = (var3 / 2 == 0 ? 0 : (var3 % 2 << 1) - 1) * -var2;
         aU.aB = this.B(2);
      }
   }

   private void bL() {
      if (this.aq != 5 && this.aq != 6 && this.aq != 7 || this.cJ == null) {
         this.cI = false;
         this.cJ = null;

         for (int var2 = 0; var2 < Game.ck; var2++) {
            Actor var1;
            if ((var1 = Game.cj[var2]) != null && var1.type == 42) {
               ey[0] = var1.collisionBounds[0];
               ey[2] = var1.collisionBounds[2];
               ey[1] = var1.collisionBounds[1] - 2;
               ey[3] = var1.collisionBounds[3];
               if (b(this.collisionBounds, ey)) {
                  this.cI = true;
                  this.cJ = var1;
                  this.bL = true;
                  break;
               }
            }
         }
      }

      this.w(440);
      if (this.s == null) {
         this.s = Game.aJ;
      }

      this.cW();
      switch (this.aq) {
         case 0:
            if (b(Game.player.collisionBounds, this.m)) {
               this.p(101);
            }
            break;
         case 1:
            this.J = 320;
            if (this.H > 4096) {
               this.H = 4096;
            }

            if (this.bL) {
               this.db();
               if (this.animationId != 2) {
                  this.p(101);
               } else {
                  this.p(104);
               }
            }
            break;
         case 2:
            this.cV();
            if (this.e()) {
               if (this.animationId != 2 && this.animationId != 3) {
                  this.p(101);
               } else {
                  this.p(this.cI ? 105 : 104);
               }
            }
            break;
         case 3:
            if (this.e()) {
               if (this.U != null) {
                  this.U.T = null;
                  this.U = null;
               }

               if (aT == this) {
                  aT = null;
               }

               this.b(true);
            }

            return;
         case 9:
            if (this.e()) {
               this.a(true, true);
            }

            return;
         case 101:
            this.F(256);
            this.G = this.D(256);
            this.g(cS);
            if (this.aB <= 0 && b(Game.player.collisionBounds, this.aJ)) {
               this.p(102);
            }
            break;
         case 102:
            if (this.aB <= 0) {
               if (!this.fx) {
                  this.cp();
                  this.fx = true;
               }

               if (this.e()) {
                  this.p(103);
                  this.aB = 3500;
               }
            }
            break;
         case 103:
            this.cV();
            if (this.aA <= 0) {
               this.p(101);
            }
            break;
         case 104:
            if (this.bL) {
               this.N = true;
            }

            if (this.cI) {
               this.p(105);
            }
            break;
         case 105:
            if (this.cJ != null && this.positionY >= this.cJ.collisionBounds[1] + 46) {
               this.positionY = this.cJ.collisionBounds[1] + 46;
               this.p(106);
            }
            break;
         case 106:
            this.cV();
            if (this.a(1, this.collisionBounds)) {
               this.G = Game.player.positionX < this.positionX ? 768 : -768;
               this.ay = this.G > 0 ? -64 : 64;
            }

            if (i(this.G, this.ay)) {
               this.G = 0;
            } else {
               this.I = this.ay;
            }

            if (this.cJ != null) {
               this.positionY = this.cJ.collisionBounds[1] + 46;
            }
            break;
         case 107:
            if (this.U != null) {
               this.U.T = null;
               this.U = null;
            }

            if (aT == this) {
               aT = null;
            }

            this.b(true);
            return;
      }

      if (!this.c()) {
         if (this.a(1, this.collisionBounds) && this.aq != 104 && this.aq != 105 && this.aq != 106) {
            this.p(this.cI ? 105 : 104);
            this.aD = 500;
         } else {
            this.aE();
         }

         if (this.aq != 107) {
            this.I();
            if (this.aq == 104 || this.aq == 105 || this.aq == 106) {
               this.d(0, this.collisionBounds);
            }

            if (!this.cI && this.T == null) {
               if (this.aq != 0 && this.cX() == 1) {
                  this.h(true);
               } else {
                  this.h(false);
               }
            } else if (!this.cI) {
               this.h(false);
            }

            this.aA();
            this.J();
            if (this.s != Game.aJ) {
               this.b(false);
            }
         }
      }
   }

   public final void I() {
      if (this.cI) {
         if (this.aq == 106) {
            int var1;
            var1 = (var1 = -this.aA / 500 % 4) < 2 ? var1 : 4 - var1;
            this.positionY = this.cJ.collisionBounds[1] + 46 + --var1;
         } else if (this.aq != 105 && this.aq != 2 && this.aq != 107) {
            this.positionY = this.cJ.collisionBounds[1];
         }

         this.b(true, false);
      }
   }

   public final void J() {
      this.g();

      for (int var2 = 0; var2 < Game.ck; var2++) {
         Actor var1;
         if ((var1 = Game.cj[var2]) != null && var1 != this) {
            var1.g();
            if (var1.T == this) {
               var1.positionY = this.collisionBounds[1] - 1;
               var1.bL = true;
               dV[1] = var1.collisionBounds[1];
               dV[0] = var1.collisionBounds[0];
               dV[3] = var1.collisionBounds[3] + 1;
               if (this.type == 14 && this.aq == 106) {
                  dV[3]++;
               }

               dV[2] = var1.collisionBounds[2];
               if (!b(this.collisionBounds, dV)) {
                  var1.T = null;
                  this.U = null;
               }
            }
         }
      }
   }

   private void bM() {
      switch (this.aq) {
         case 0:
            if (b(Game.viewBounds, this.m)) {
               this.ax = 0;
               this.p(101);
               return;
            }
            break;
         case 101:
            this.G = fs << 8;
            int var1 = this.ar();
            this.aA = this.aA - Game.h;
            if (this.aA < 0) {
               switch (var1) {
                  case 11:
                     this.s = this.q(this.aH, this.aw);
                     this.t = this.q(this.aH, this.aw);
                     this.u = this.q(this.aH, this.aw);
                     this.s.aA = 1100;
                     this.t.aA = 700;
                     this.u.aA = 300;
                     this.s.aB = 1600;
                     this.t.aB = 1600;
                     this.u.aB = 1600;
                     this.p(105);
                     break;
                  case 12:
                  case 14:
                  case 15:
                     if (this.ax == 0) {
                        this.s = this.q(this.aH, this.aw);
                     } else if (this.ax == 1) {
                        this.t = this.q(this.aH, this.aw);
                     } else if (this.ax == 2) {
                        this.u = this.q(this.aH, this.aw);
                     }

                     this.ax++;
                     this.p(102);
                     break;
                  case 13:
                     this.s = this.q(this.aH, this.aw);
                     this.t = this.q(this.aH, this.aw);
                     this.u = this.q(this.aH, this.aw);
                     this.s.aA = 400;
                     this.t.aA = 400;
                     this.u.aA = 400;
                     this.s.aB = 900;
                     this.t.aB = 1300;
                     this.u.aB = 1700;
                     this.p(105);
               }

               return;
            }
            break;
         case 102:
            if (this.ax >= 3) {
               this.p(103);
               return;
            }

            this.cV();
            if (this.aA <= 0) {
               this.p(101);
               return;
            }
            break;
         case 103:
            this.cV();
            if (this.aA <= 0) {
               this.p(104);
               return;
            }
            break;
         case 104:
            switch (this.ar()) {
               case 11:
               case 13:
               default:
                  break;
               case 12:
                  if (this.s != null) {
                     this.s.p(125);
                  }

                  if (this.t != null) {
                     this.t.p(125);
                  }

                  if (this.u != null) {
                     this.u.p(125);
                  }

                  this.p(105);
                  break;
               case 14:
               case 15:
                  this.p(105);
            }

            return;
         case 105:
            if (this.r == null) {
               Game.e(this);
               return;
            }

            if (this.r(this.s) || this.r(this.t) || this.r(this.u) || (this.s.c & 1024) != 0 || (this.t.c & 1024) != 0 || (this.u.c & 1024) != 0) {
               this.K();
            }
      }
   }

   public final void K() {
      Game.e(this.r);
      this.b((Actor)null);
      Game.e(this);
   }

   private boolean r(Actor var1) {
      return var1 != null ? b(var1.collisionBounds, Game.viewBounds) : true;
   }

   private Actor q(int var1, int var2) {
      this.a(2, 2, 0, 0, 0, 0, false);
      aU.aH = var1;
      aU.aI = this.aI;
      aU.as = aU.B(0);
      aU.aK = var2;
      switch (var2) {
         case 1:
            aU.facingLeft = false;
            aU.positionX = Game.viewBounds[0] + aU.collisionBounds[0] - aU.collisionBounds[2];
            break;
         case 2:
            aU.facingLeft = true;
            aU.positionX = Game.viewBounds[2] + aU.collisionBounds[2] - aU.collisionBounds[0];
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            break;
         case 4:
            aU.facingLeft = this.facingLeft;
            aU.positionY = Game.viewBounds[1] + 4;
            break;
         case 8:
            aU.facingLeft = this.facingLeft;
            aU.positionY = Game.viewBounds[3] + aU.collisionBounds[3] - aU.collisionBounds[1] - 4;
      }

      aU.ap = 81;
      Actor var3 = aU;
      switch (aU.ar()) {
         case 11:
            aU.p(119);
            aU.aL = 1;
            break;
         case 12:
            aU.p(124);
            break;
         case 13:
            aU.p(129);
            aU.aL = 2;
            break;
         case 14:
            aU.p(134);
            break;
         case 15:
            aU.p(135);
      }

      return var3;
   }

   private void h(boolean var1) {
      if (Game.player != this) {
         this.b(true, true);
      }

      boolean var2;
      if (!(var2 = this.eD >= 16 && this.eD <= 21) && this.H > 0 && this.eD >= 15 && this.T != null) {
         this.bN();
      }

      this.bi();
      if (!var2 && (this.G < 0 && this.eJ < 15 || this.G > 0 && this.eK < 15) && this.T == null) {
         if (!var1) {
            this.G = 0;
         } else if (!this.bL) {
            this.x(this.H);
         }
      }

      if (this.H == 0 && !this.bL && this.T == null) {
         this.x(this.H);
      }
   }

   private void bN() {
      this.positionY = (this.collisionBounds[3] + 1) / 20 * 20;
      this.positionY--;
      if (this.eD < 24 && this.eD != 15 && this.eI >= 24) {
         this.positionY += 20;
      }

      this.H = 0;
      this.G = 0;
   }

   public final boolean L() {
      return this.type == 2
         || this.type == 3
         || this.type == 1
         || this.type == 4
         || this.type == 5
         || this.type == 14
         || this.type == 11
         || this.type == 70
         || this.type == 15
         || this.type == 92
         || this.type == 99;
   }

   public final boolean M() {
      if ((16384 & this.c) != 0) {
         return false;
      }

      if (this.aq != 5 && this.aq != 6 && this.aq != 7 && this.aq != 3 && this.aq != 9) {
         if ((this.aq == 0 || this.aq == 104) && this.type == 1 && this.aC >= 4) {
            return false;
         }

         if (this.type == 2 || this.type == 3 || this.type == 14 || this.type == 15 || this.type == 92 || this.type == 13) {
            return true;
         }

         if (this.type == 99 && this.aq != 106 && this.aq != 102 && this.aq != 103 && this.aq != 104) {
            return true;
         }

         if (this.type == 1) {
            if (this.aL != 2) {
               return true;
            }
         } else {
            if (this.type == 11) {
               if (this.ar() == 1 || this.aq != 153 && this.aq != 2 && this.aq != 117) {
                  if (this.ar() == 1 && !de && (this.aq == 105 || this.aq == 113 || this.aq == 112 || this.aq == 2 || this.aq == 117) && !df) {
                     return true;
                  }

                  return false;
               }

               return true;
            }

            if (this.type == 90 && this.aq >= 105 && this.aq < 124) {
               return true;
            }

            if (this.type == 100 && (this.aq == 104 || this.aq == 103)) {
               return true;
            }

            if (this.type == 85 && this.aH == 0) {
               return true;
            }

            if (this.type == 70 && this.aq == 103 && !this.fx) {
               return true;
            }

            if (this.type == 8 && this.aq == 109) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean bO() {
      return this.type == 2 || this.type == 3 || this.type == 1 || this.type == 14 || this.type == 70 || this.type == 11;
   }

   private void x(int var1) {
      if (Game.player != this) {
         this.k(this.animationId, 32);
      }

      this.H = var1;
      this.J = 320;
      if (this.type != 11 && Game.player != this && this.aq != 2) {
         this.p(1);
      }
   }

   private void bP() {
      if (this.L() && this.collisionBounds != null && this.type != 5 || this.type == 0 && (this.animationId == 11 || this.animationId == 12)) {
         if (Game.aJ == null) {
            return;
         }

         if (this.type == 11 && this.ar() == 1) {
            return;
         }

         if (this.type == 2 && (this.ar() == 3 || this.ar() == 5 || this.ar() == 6)) {
            if (!b(this.collisionBounds, Game.aJ.collisionBounds)) {
               if (this.ar() == 3) {
                  do_ = true;
                  this.K();
                  do_ = false;
                  return;
               }

               this.b(false);
            }

            return;
         }

         dV[0] = Game.aJ.collisionBounds[0] + 12;
         dV[2] = Game.aJ.collisionBounds[2] - 12;
         dV[1] = Game.aJ.collisionBounds[1] + 12;
         dV[3] = Game.aJ.collisionBounds[3] - 12;
         if (dV[0] > this.collisionBounds[0] && this.G <= 0) {
            this.G = 0;
         }

         if (dV[2] < this.collisionBounds[2] && this.G >= 0) {
            this.G = 0;
         }

         if (dV[1] > this.collisionBounds[1] && this.H <= 0) {
            this.H = 0;
         }

         if (dV[3] < this.collisionBounds[3] && this.H >= 0) {
            this.H = 0;
         }
      }
   }

   public final void N() {
      if (bm) {
         this.a(61, 38, 13 + this.ax, 0, 0, 101, false);
         this.g();
         aU.positionX = this.positionX + 0;
         aU.positionY = this.positionY + -4;
         aU.n = new int[10];
         aU.ap = 319;
         this.ax++;
         if (this.v != null) {
            Game.e(this.v);
            this.v = null;
         }

         this.v = aU;
      }
   }

   private void bQ() {
      switch (this.aq) {
         case 0:
            if (b(Game.viewBounds, this.m)) {
               this.av = 0;
               this.M = true;
               switch (this.ar()) {
                  case 0:
                     this.p(101);
                     break;
                  case 1:
                     this.p(105);
                     this.az = 0;
                     this.ac = this.positionX - Game.viewBounds[0];
                     this.ad = this.positionY;
                     E = this;
                     F = this.as;
               }
            }
         case 1:
         case 2:
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
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         case 100:
         default:
            break;
         case 3:
            if (!Game.hasActiveBackgroundScript()) {
               Game.player.c &= -129;
               Game.e(this);
            }
            break;
         case 101:
            this.cV();
            if (this.aA <= 0) {
               this.p(102);
            }
            break;
         case 102:
            this.cV();
            if (this.aA <= 0) {
               this.az = this.av == 0 ? this.ay : (this.av == 1 ? this.aw : this.ax);
               this.d(9 + this.av % 2 * 2, Game.viewBounds[0] + 160, this.az, 6);
               if (++this.av >= 3) {
                  this.p(103);
               } else {
                  this.aA = 1000;
               }
            }
            break;
         case 103:
            this.cV();
            if (this.aA <= 0) {
               this.p(104);
            }
            break;
         case 104:
            if (!b(Game.viewBounds, this.collisionBounds)) {
               Game.e(this);
               return;
            }
            break;
         case 105:
            this.cV();
            if (this.aA <= 0) {
               this.p(106);
            }
            break;
         case 106:
            this.cV();
            if (this.aA <= 0) {
               this.p(107);
               this.s = this.q(ex[39], 4);
               this.t = this.q(ex[39], 8);
               this.s.facingLeft = true;
               this.t.facingLeft = true;
               this.s.aI = this.B(2);
               this.t.aI = this.B(2);
               this.s.aA = 400;
               this.t.aA = 400;
               this.s.aB = 900;
               this.t.aB = 900;
               this.s.c |= 16;
               this.t.c |= 16;
            }
            break;
         case 107:
            this.cV();
            if (this.aA <= 0) {
               this.p(112);
            }
            break;
         case 108:
            this.H = this.E(2560);
            if (this.H < 0 && this.collisionBounds[1] < Game.aJ.collisionBounds[1] + 20) {
               this.H = 0;
               this.positionY = Game.aJ.collisionBounds[1] + (this.collisionBounds[3] - this.collisionBounds[1]) + 20;
            } else if (this.H > 0 && this.positionY > Game.aJ.collisionBounds[3] - 10) {
               this.H = 0;
               this.positionY = Game.aJ.collisionBounds[3] - 10;
            }

            if (this.H == 0) {
               this.a(-101, 14, 0, 0, 0, 302, false);
               this.b(aU);
               this.p(109);
            }
            break;
         case 109:
            this.cV();
            if (this.aA <= 0) {
               Game.e(this.r);
               this.a(-101, 14, 1, 0, 0, 302, false);
               this.b(aU);
               this.p(110);
            }
            break;
         case 110:
            this.H = this.E(1280);
            if (this.H < 0 && this.collisionBounds[1] < Game.aJ.collisionBounds[1] + 20) {
               this.H = 0;
               this.positionY = Game.aJ.collisionBounds[1] + this.collisionBounds[3] - this.collisionBounds[1] + 20;
            } else if (this.H > 0 && this.positionY > Game.aJ.collisionBounds[3] - 10) {
               this.H = 0;
               this.positionY = Game.aJ.collisionBounds[3] - 10;
            }

            this.a(this.v, 0, 0);
            if (this.r != null && this.r.e()) {
               Game.e(this.r);
               this.b((Actor)null);
               this.p(111);
            }
            break;
         case 111:
            this.cV();
            if (this.aA <= 0) {
               if (++this.az >= 2) {
                  this.p(121);
               } else {
                  this.p(108);
               }
            }
            break;
         case 112:
            this.cV();
            if (this.aA <= 0) {
               this.p(113);
               aX = new Actor[6];

               for (int var5 = 0; var5 < 6; var5++) {
                  aX[var5] = this.a(-102, 15, 0, 0, -37, 302, false);
                  aX[var5].c |= 16;
                  aX[var5].aB = var5;
                  aX[var5].aA = 500;
                  aX[var5].ay = 0;
                  aX[var5].av = this.B(4);
                  aX[var5].p(101);
               }
            }
            break;
         case 113:
            this.cV();
            if (this.aA <= 0) {
               this.p(114);
            }
            break;
         case 114:
            this.aw = this.ay + Game.viewBounds[0];
            this.eX = Game.h(this.aw - this.positionX, this.av - this.positionY);
            if (this.eX > 4) {
               this.G = this.eX == 0 ? 0 : (this.aw - this.positionX) * 1024 / this.eX;
               this.H = this.eX == 0 ? 0 : (this.av - this.positionY) * 1024 / this.eX;
            } else {
               this.p(115);
            }
            break;
         case 115:
            this.cV();
            if (this.aA <= 0) {
               this.p(116);

               for (int var4 = 0; var4 < 6; var4++) {
                  aX[var4].p(102);
               }
            }
            break;
         case 116:
            this.cV();
            this.a(this.v, 0, 0);
            if (this.aA <= 0) {
               this.p(117);

               for (int var3 = 0; var3 < 6; var3++) {
                  aX[var3].p(103);
               }
            }
            break;
         case 117:
            this.cV();
            if (this.aA <= 0) {
               this.p(118);
            }
            break;
         case 118:
            this.cV();
            if (this.aA <= 0) {
               this.p(119);
            }
            break;
         case 119:
            this.aw = this.ac + Game.viewBounds[0];
            this.eX = Game.h(this.aw - this.positionX, this.ad - this.positionY);
            if (this.eX > 10) {
               this.G = this.eX == 0 ? 0 : (this.aw - this.positionX) * 2560 / this.eX;
               this.H = this.eX == 0 ? 0 : (this.ad - this.positionY) * 2560 / this.eX;
            } else {
               this.db();
               this.p(120);

               for (int var2 = 0; var2 < 6; var2++) {
                  Game.e(aX[var2]);
                  aX[var2] = null;
               }
            }
            break;
         case 120:
            this.cV();
            if (this.aA <= 0) {
               this.p(108);
               this.az = 0;
            }
            break;
         case 121:
            this.cV();
            if (this.aA <= 0) {
               this.p(122);
               this.au();
               Game.e(this.x);
               this.x = null;
               this.c(27, this.positionX + -35, this.positionY + -90);
            } else {
               this.g(cT);
               if (a(this.aJ, Game.player.collisionBounds) && Game.G(65568)) {
                  Game.player.a(bg);
                  this.au();
                  Game.e(this.x);
                  this.x = null;
                  this.c(26, this.positionX + -35, this.positionY + -90);
                  this.p(124);
                  f(0);
               }
            }
            break;
         case 122:
            this.cV();
            if (this.aA <= 0) {
               this.p(123);
            }
            break;
         case 123:
            this.aw = this.ac + Game.viewBounds[0];
            this.eX = Game.h(this.aw - this.positionX, this.ad - this.positionY);
            if (this.eX > 10) {
               this.G = this.eX == 0 ? 0 : (this.aw - this.positionX) * 2560 / this.eX;
               this.H = this.eX == 0 ? 0 : (this.ad - this.positionY) * 2560 / this.eX;
            } else {
               this.db();
               this.p(105);
               this.az = 0;
            }
            break;
         case 124:
            if (!bj) {
               this.p(125);
               Game.player.b(bh);
            }
            break;
         case 125:
            if (!bm) {
               this.p(126);
               Game.player.G = fs << 8;
               f(1);
            }
            break;
         case 126:
            this.cV();
            if (this.aA <= 0) {
               this.p(122);
               this.au();
               cL = 0;
               br = false;
               Game.player.a(1);
            } else if (this.v(65568)) {
               cL = 0;
               br = false;
               this.p(127);
               this.au();
               Game.player.b(bi);
            }
            break;
         case 127:
            if (!bm) {
               this.p(128);
               Game.player.G = fs << 8;
            }
            break;
         case 128:
            this.cV();
            if (this.aA <= 0) {
               this.p(123);
            } else if (this.animationId == 8 && this.e()) {
               this.a(6);
               this.b(80, false);
            }
            break;
         case 129:
            this.cV();
            if (this.aA <= 0) {
               this.x(328, 0);
               this.p(3);
               P();
               Game.N();
            }
      }

      if (this.q != null) {
         if (this.aq == 126) {
            this.q.positionX = Game.player.positionX + 35;
            this.q.positionY = Game.player.positionY + -100;
         } else {
            this.a(this.q, -35, -90);
            if ((this.q.animationId == 26 || this.q.animationId == 27) && this.q.e()) {
               Game.e(this.q);
               this.q = null;
            }
         }
      }

      if (this.x != null) {
         this.a(this.x, -35, -10);
      }

      if (this.v != null) {
         this.a(this.v, 0, -4);
      }

      if ((this.aq == 109 || this.aq == 110) && this.r != null) {
         this.a(this.r, -152, -53);
         this.r.g();
         if (b(Game.player.collisionBounds, this.collisionBounds) || b(Game.player.collisionBounds, this.r.collisionBounds)) {
            Game.player.a(4, this.B(3), 0, this);
         }
      }

      if (this.ar() == 1) {
         if (this.aq >= 124) {
            if (this.e(false)) {
               this.a(8);
            }

            if (this.animationId == 8 && this.e()) {
               this.a(6);
               return;
            }
         } else {
            this.e(false);
         }
      }
   }

   public final void O() {
      this.cV();
      int var1 = 0;
      if (this.aq == 101) {
         if (this.aA >= 0) {
            var1 = (500 - this.aA) * 45 / 500;
            int var2 = this.aB * 60;
            this.positionX = E.positionX + (var1 * Game.l(var2) >> 8);
            this.positionY = E.positionY + (var1 * Game.m(var2) >> 8) - 37;
         } else {
            this.positionX = E.positionX + (45 * Game.l(this.aB * 60 + this.ay) >> 8);
            this.positionY = E.positionY + (45 * Game.m(this.aB * 60 + this.ay) >> 8) - 37;
            this.ay += 10;
         }
      } else if (this.aq == 103) {
         this.G = 2048 * Game.l(this.aB * 60 + this.ay) >> 8;
         this.H = 2048 * Game.m(this.aB * 60 + this.ay) >> 8;
         this.c &= -17;
      } else if (this.aq == 110 || this.aq == 111 || this.aq == 112) {
         if (this.aq == 110) {
            if (this.aA >= 0) {
               var1 = 50 * (1000 - this.aA) / 1000;
            } else {
               this.aq = 111;
               var1 = 50;
               E.a(1);
               E.v.a(1);
            }
         } else if (this.aq == 111) {
            var1 = 50;
            this.ay += 5;
            this.aw += 5;
            if (this.aw >= 180) {
               this.aq = 112;
               this.aA = 2000;
               E.a(3);
               if (E.w == null) {
                  E.w = E.a(16, 3, 4, 0, 0, 302, false);
               }

               E.v.a(3);
               if (E.v.w == null) {
                  E.v.w = E.v.a(16, 3, 4, 0, 0, 302, false);
               }
            }
         } else if (this.aA > 0) {
            var1 = 50;
         } else {
            E.a(4);
            Game.e(E.w);
            E.w = null;
            E.v.a(4);
            Game.e(E.v.w);
            E.v.w = null;
            var1 = 50 - this.aA / 5;
         }

         if (this.aB < 6) {
            this.positionX = E.positionX + (var1 * Game.l(this.aB * 60 + this.ay) >> 8);
            this.positionY = E.positionY + (var1 * Game.m(this.aB * 60 + this.ay) >> 8) - 40;
         } else {
            this.positionX = E.v.positionX + (var1 * Game.l(this.aB * 60 + this.ay) >> 8);
            this.positionY = E.v.positionY + (var1 * Game.m(this.aB * 60 + this.ay) >> 8) - 40;
         }
      }

      if (b(Game.player.collisionBounds, this.collisionBounds)) {
         Game.player.a(4, this.av, 0, this);
      }
   }

   private void d(int var1, int var2, int var3, int var4) {
      this.a(61, 38, var1, 0, 0, 101, false);
      aU.positionX = var2;
      aU.positionY = var3;
      this.g();
      aU.ay = aU.collisionBounds[2] - aU.collisionBounds[0];
      aU.aw = aU.ay / 20;
      aU.av = aU.collisionBounds[3] - aU.collisionBounds[1];
      aU.n = new int[var4];
      aU.ap = 319;
   }

   private void bR() {
      int var1 = this.collisionBounds[0] + 25;
      int var2 = this.collisionBounds[1] - 25;
      int var3 = Game.i(1, 6);
      int var4 = Game.i(1, 6);
      int var5 = Game.i(1, 6);

      while (var4 == var3) {
         var4 = Game.i(1, 6);
      }

      while (var5 == var3 || var5 == var4) {
         var5 = Game.i(1, 6);
      }

      for (int var6 = 0; var6 < 7; var6++) {
         this.a(73, 44, 2, 0, var2, 100, false);
         aU.positionX = var1 + 51 * var6;
         if (var6 == var3) {
            aU.positionY = var2;
         } else if (var6 == var4) {
            aU.positionY = var2 - 100;
         } else if (var6 == var5) {
            aU.positionY = var2 - 300;
         } else {
            aU.positionY = var2 - 200;
         }

         aU.c |= 16;
         aU.az = 1;
         aU.p(102);
      }
   }

   private void bS() {
      this.cV();
      switch (this.aq) {
         case 0:
            if (b(Game.viewBounds, this.m)) {
               E = this;
               F = this.as;
               this.p(100);
            }
            break;
         case 3:
            if (!Game.hasActiveBackgroundScript()) {
               Game.player.c &= -129;
               Game.e(this);
            }
            break;
         case 100:
            if (this.e()) {
               this.positionX = Game.aJ.collisionBounds[2] + Game.aJ.collisionBounds[0] >> 1;
               this.positionY = (Game.aJ.collisionBounds[3] + Game.aJ.collisionBounds[1] >> 1) + 20;
               this.p(101);
            }
            break;
         case 101:
            if (this.animationId == 10 && this.e()) {
               this.a(3);
            }

            if (this.aA <= 0 && aY == null) {
               aY = new Actor[4];

               for (int var6 = 0; var6 < 4; var6++) {
                  aY[var6] = this.a(39, 17, 30, 0, 0, 302, false);
                  aY[var6].aB = var6;
                  aY[var6].aA = 1000;
                  aY[var6].ay = 0;
                  aY[var6].av = Game.player.positionX <= Game.aJ.collisionBounds[2] + Game.aJ.collisionBounds[0] >> 1 ? 1 : -1;
                  aY[var6].aw = 0;
                  aY[var6].aq = 101;
               }
            }
            break;
         case 102:
            for (int var5 = 0; var5 < 4; var5++) {
               if (b(aY[var5].positionX, aY[var5].positionY, aZ[var5].positionX, aZ[var5].positionY, Game.player.collisionBounds)) {
                  Game.player.a(4, this.B(1), 0, this);
               }
            }
            break;
         case 103:
            this.J = 256;
            if (this.H >= 1536) {
               this.H = 1536;
            }

            this.b(false, true);
            if (this.bL) {
               this.p(104);
            }
            break;
         case 104:
            if (this.animationId == 8 && this.e()) {
               this.a(6);
            }

            if (this.aA <= 0) {
               if (this.aC > 66) {
                  this.p(100);
               } else {
                  this.p(105);
               }
            }
            break;
         case 105:
            if (this.e()) {
               this.positionX = Game.aJ.collisionBounds[2] + Game.aJ.collisionBounds[0] >> 1;
               this.positionY = (Game.aJ.collisionBounds[3] + Game.aJ.collisionBounds[1] >> 1) + 0;
               this.p(106);
            }
            break;
         case 106:
            if (this.aA <= 0) {
               this.a(100, 13, 1, 60, 0, 300, false);
               this.v = aU;
               this.p(108);
            }
            break;
         case 107:
            if (this.aA <= 0) {
               this.p(108);
            }
            break;
         case 108:
            this.G = 0;
            if (this.aA >= 3000) {
               if (aY == null) {
                  this.a(4);
                  this.v.a(4);
                  aY = new Actor[12];
                  int var3 = Game.random.nextInt() % 2 == 0 ? 0 : 30;

                  for (int var2 = 0; var2 < 12; var2++) {
                     if (var2 < 6) {
                        aY[var2] = this.a(-102, 15, 0, 0, -40, 302, false);
                     } else {
                        aY[var2] = this.v.a(-102, 15, 0, 0, -40, 302, false);
                     }

                     aY[var2].c |= 16;
                     aY[var2].aB = var2;
                     aY[var2].aA = 1000;
                     aY[var2].aq = 110;
                     aY[var2].ay = var3;
                     aY[var2].aw = 0;
                     aY[var2].av = this.B(2);
                  }
               }
            } else if (this.aA <= 0 && aY != null && aY[0].aq == 112) {
               boolean var4 = false;

               for (int var7 = 0; var7 < 12; var7++) {
                  if (a(aY[var7].positionX, aY[var7].positionY, Game.aJ.collisionBounds)) {
                     var4 = true;
                     break;
                  }
               }

               if (!var4) {
                  if (this.ay == 0) {
                     if (this.aC <= 33) {
                        this.p(109);
                     } else {
                        this.p(100);
                        Game.e(this.v);
                        this.v = null;
                     }
                  } else {
                     this.ay = 0;
                     this.p(110);
                     Game.e(this.v);
                     this.v = null;
                  }

                  for (int var8 = 0; var8 < 12; var8++) {
                     Game.e(aY[var8]);
                     aY[var8] = null;
                  }

                  aY = null;
               }
            }
            break;
         case 109:
            this.ay += 5;
            this.positionX = this.v.positionX = Game.aJ.collisionBounds[2] + Game.aJ.collisionBounds[0] >> 1;
            this.positionY = this.v.positionY = Game.aJ.collisionBounds[3] + Game.aJ.collisionBounds[1] >> 1;
            this.positionX = this.positionX + (60 * Game.l(this.ay) >> 8);
            this.positionY = this.positionY - (60 * Game.m(this.ay) >> 8);
            this.v.positionX = this.v.positionX - (60 * Game.l(this.ay) >> 8);
            this.v.positionY = this.v.positionY + (60 * Game.m(this.ay) >> 8);
            if (this.ay >= 90) {
               this.p(107);
            }
            break;
         case 110:
            if (this.e()) {
               this.a(10);
               this.positionX = Game.aJ.collisionBounds[2] + Game.aJ.collisionBounds[0] >> 1;
               this.positionY = Game.aJ.collisionBounds[1] + 60;
               this.p(111);
            }
            break;
         case 111:
            if (this.animationId == 10) {
               if (this.e()) {
                  this.a(3);
                  this.aA = 400;
               }
            } else if (this.animationId != 3 && this.animationId != 4) {
               if (this.aA <= 0) {
                  this.aB--;
                  if (this.aB > 0) {
                     this.c(111, true);
                     this.a(3);
                     this.aA = 400;
                  } else {
                     this.p(100);
                  }
               }
            } else if (this.aA < 0) {
               if (this.animationId == 3) {
                  Game.aJ.bR();
                  this.a(4);
                  this.aA = 400;
               } else {
                  this.a(1);
                  if (this.aB == 2) {
                     this.aA = 3000;
                  } else {
                     this.aA = 4000;
                  }
               }
            }
            break;
         case 112:
            if (this.aA <= 0) {
               this.x(19, 8);

               for (int var1 = 0; var1 < 10; var1++) {
                  dc[var1] = Game.i(var1 * 36, var1 * 36 + 18);
                  dd[var1] = Game.i(5, 15);
               }

               this.p(3);
               P();
               Game.N();
            }
      }

      if ((this.aq == 103 || this.aq == 104) && this.e(false)) {
         this.a(8);
      }

      if (this.aq != 0) {
         this.aC = this.as * 100 / F;
      }
   }

   public static final void P() {
      e(Game.cb);
      if (er != null) {
         for (int var0 = 0; var0 < 3; var0++) {
            if (er[var0] != null) {
               Game.e(er[var0]);
            }
         }

         er = null;
      }

      e(Game.bY);

      for (int var1 = 0; var1 < Game.ck; var1++) {
         Actor var2;
         if ((var2 = Game.cj[var1]) != null && var2 != Game.bY && (var2.type == 39 || var2.an == -1 && var2.type != 35)) {
            Game.e(var2);
         }
      }
   }

   private void bT() {
      if (this.B != -1 && this.v == null) {
         if (this.aH == 0) {
            this.v = Game.i(this.B);
            fd = this.v.positionY;
         } else if (this.aH == 1 && this.s == null) {
            this.v = Game.i(this.B);
            this.s = this.v;
         }
      }

      dr[this.aF] = this.positionX;
      ds[this.aF] = this.positionY;
      switch (this.aq) {
         case 0:
            if (b(Game.player.collisionBounds, this.m)) {
               this.aK = this.facingLeft ? 1 : 2;
               this.Q();
               if (this.aH == 0) {
                  dH = Game.aJ.collisionBounds[1];
                  dI = Game.aJ.collisionBounds[3];
                  dJ = Game.aJ.collisionBounds[0];
                  dK = Game.aJ.collisionBounds[2];
                  this.p(101);
                  this.ay = dJ + dK >> 1;
                  this.aw = this.ay - 60;
                  this.ax = this.ay + 60;
                  this.av = dI + 40;
                  if (this.C != -1 && this.w == null) {
                     this.w = Game.i(this.C);
                     dO = this.w.positionX - dJ;
                     dP = this.w.positionY - dH;
                     dL = Game.aJ;
                     dM = this.w;
                  }

                  fh = dH + dP;
                  fi = dI + dP;
                  fj = dJ + dO;
                  fk = dK + dO;
                  fm = dH + 120;
                  fn = fh + 120;
                  fl = this.positionY;
                  dN = 0;
                  fo = this.as;
                  E = this;
                  F = this.as;
                  fg = false;
               } else if (this.aH == 1) {
                  this.p(150);
               }
            }
         case 1:
         case 2:
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
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         case 100:
         case 142:
         case 143:
         case 144:
         case 145:
         case 146:
         case 147:
         case 148:
         case 149:
         case 155:
         default:
            break;
         case 3:
            this.p(155);
            Game.h(13);
            break;
         case 101:
            this.cV();
            if (this.aA <= 0) {
               this.p(102);
            }
            break;
         case 102:
            this.cV();
            if (!this.fx && Game.g((this.aK == 1 ? this.collisionBounds[0] : this.collisionBounds[2]) / 20, this.positionY / 20) != 24) {
               this.a(98, 19, 10, -10, 30, 302, true);
               aU.p(102);
               aU.aL = 2;
               this.fx = true;
            }

            if (this.aK == 1 && this.positionX <= dK - 30 || this.aK == 2 && this.positionX >= dJ + 30) {
               this.positionX = this.facingLeft ? dK - 30 : dJ + 30;
               this.p(103);
            }
            break;
         case 103:
            this.cV();
            if (this.aA <= 0) {
               this.p(104);
            }
            break;
         case 104:
            this.G = this.aK == 1 ? -this.y(1280) : this.y(1280);
            this.positionY = (7 * Game.m(2 * (this.ac - this.positionX)) >> 8) + this.ad;
            if (this.aB > 0) {
               if (this.aK == 1 && this.positionX <= dJ - 0 || this.aK == 2 && this.positionX >= dK + 0) {
                  this.p(105);
               }
            } else if (this.aK == 1 && this.positionX <= dK - 20 || this.aK == 2 && this.positionX >= dJ + 20) {
               this.positionX = this.aK == 1 ? dK - 20 : dJ + 20;
               this.p(106);
            }
            break;
         case 105:
            dD = dD + (dE ? dC : -dC);
            dG = dG + dC;
            if (dG >= 180) {
               dD = dD + (dE ? -(dG - 180) : dG - 180);
            }

            this.positionX = dz + (dB * Game.l(dD) >> 8);
            this.positionY = dA + (dB * Game.m(dD) >> 8);
            if (dG >= 180) {
               this.p(104);
            }

            fr = dD % 360;
            if (fr < 0) {
               fr += 360;
            }

            if (fr >= 180) {
               this.aK = dE ? 2 : 1;
            } else {
               this.aK = dE ? 1 : 2;
            }
            break;
         case 106:
            this.cV();
            if (this.aA <= 0) {
               this.p(107);
            }
            break;
         case 107:
            this.cV();
            if (this.v != null && this.v.type == 78) {
               if (this.v.positionY > fd - 70) {
                  this.v.positionY--;
                  this.v.av++;
               } else {
                  this.p(108);
               }

               this.c(this.v.collisionBounds[0] + 30, this.v.collisionBounds[2] - 30, true);
            }
            break;
         case 108:
            this.cV();
            this.c(this.v.collisionBounds[0] + 30, this.v.collisionBounds[2] - 30, false);
            if (this.aA <= 0) {
               this.p(109);
            }
            break;
         case 109:
            if (this.v != null && this.v.type == 78) {
               if (this.v.positionY < fd) {
                  this.v.positionY++;
                  this.v.av--;
               } else {
                  this.p(110);
               }
            }
            break;
         case 110:
            this.cV();
            if (this.aA <= 0) {
               this.p(111);
            }
            break;
         case 111:
            this.G = this.aK == 1 ? -this.y(1536) : this.y(1536);
            this.positionY = (7 * Game.m(2 * (this.ac - this.positionX)) >> 8) + this.ad;
            if (this.aK == 1 && this.positionX <= dJ - 40 || this.aK == 2 && this.positionX >= dK + 40) {
               this.p(112);
            }
            break;
         case 112:
            if (this.positionY < this.av) {
               this.H = 2048;
            } else {
               this.H = 0;
               this.positionY = this.av;
               if (this.aK == 1) {
                  if (this.positionX < this.ay) {
                     this.G = 2048;
                  } else {
                     this.positionX = this.ay;
                     this.p(113);
                  }
               } else if (this.aK == 2) {
                  if (this.positionX > this.ay) {
                     this.G = -2048;
                  } else {
                     this.positionX = this.ay;
                     this.p(113);
                  }
               }
            }
            break;
         case 113:
            this.cV();
            if (this.aA <= 0) {
               this.p(114);
            }
            break;
         case 114:
            this.cV();
            this.c(fe - 30, fe + 30, false);
            if (this.aA <= 0) {
               this.p(115);
            }
            break;
         case 115:
            if (this.aB <= 0) {
               if (this.aB == 0) {
                  if (this.positionY <= dH - 150) {
                     this.p(116);

                     for (int var2 = 0; var2 < 11; var2++) {
                        if (fa[var2] != null) {
                           fa[var2].c |= 128;
                        }
                     }
                  }
               } else if (this.positionY >= fm) {
                  this.positionY = fm;
                  this.p(117);
               }
            } else if (this.aK == 4 && this.positionY <= dH - -40 || this.aK == 8 && this.positionY >= dI + -75) {
               this.p(116);
            }

            this.bY();
            break;
         case 116:
            dD = dD + (dE ? dC : -dC);
            dG = dG + dC;
            if (dG >= 180) {
               dD = dD + (dE ? -(dG - 180) : dG - 180);
            }

            this.positionX = dz + (dB * Game.l(dD) >> 8);
            this.positionY = dA + (dB * Game.m(dD) >> 8);
            if (dG >= 180) {
               this.p(115);
            }

            fr = dD % 360;
            if (fr < 0) {
               fr += 360;
            }

            if (fr >= 90 && fr < 270) {
               this.aK = dE ? 4 : 8;
            } else {
               this.aK = dE ? 8 : 4;
            }

            this.bY();
            break;
         case 117:
            this.positionX = dz + (dB * Game.l(dD) >> 8);
            this.positionY = dA + (dB * Game.m(dD) >> 8);
            dD = dD + (dE ? dC : -dC);
            dG = dG + dC;
            dC = 5 + dG * 45 / 1080;
            if (dG >= 1080) {
               this.p(118);
            }

            fr = dD % 360;
            if (fr < 0) {
               fr += 360;
            }

            if (fr >= 180) {
               this.aK = dE ? 2 : 1;
            } else {
               this.aK = dE ? 1 : 2;
            }
            break;
         case 118:
            if (Game.P >= 24) {
               dN = 1 - dN;
               this.p(119);
            }
            break;
         case 119:
            this.p(120);
            break;
         case 120:
            if (Game.P >= 24) {
               if (dN == 1) {
                  this.p(121);
               } else {
                  this.p(140);
               }
            }
            break;
         case 121:
            if (this.aK == 1 && this.positionX <= fj - 120 || this.aK == 2 && this.positionX >= fk + 120) {
               this.H = -this.y(1536);
               if (this.positionY <= fh + 30) {
                  this.positionY = fh + 30;
                  this.p(122);
                  this.cb();
               }
            }
            break;
         case 122:
            if (this.aK == 1 && this.positionX <= fj - 40 || this.aK == 2 && this.positionX >= fk + 40) {
               this.p(123);
            }
            break;
         case 123:
            this.H = -3072;
            if (this.positionY <= fh + -40) {
               this.positionY = fh + -40;
               this.G = this.aK == 1 ? -this.y(1536) : this.y(1536);
               if (this.aK == 1 && this.positionX <= fj + fk >> 1 || this.aK == 2 && this.positionX >= fj + fk >> 1) {
                  this.positionX = fj + fk >> 1;
                  this.p(124);
               }
            }
            break;
         case 124:
            if (this.aK == 1 && this.positionX <= fj - 40 || this.aK == 2 && this.positionX >= fk + 40) {
               this.db();
               this.H = 3072;
               if (this.positionY >= fh + 155) {
                  this.positionY = fh + 155;
                  this.p(125);
               }
            }
            break;
         case 125:
            if (this.aK == 1 && this.positionX <= fk - 30 || this.aK == 2 && this.positionX >= fj + 30) {
               this.positionX = this.aK == 1 ? fk - 30 : fj + 30;
               this.p(126);
            }
            break;
         case 126:
            this.cV();
            if (this.aA <= 0) {
               this.p(127);
            }
            break;
         case 127:
            if (this.aB > 0) {
               if (this.aK == 1 && this.positionX <= fj - -50 || this.aK == 2 && this.positionX >= fk + -50) {
                  this.p(128);
               }
            } else if (this.aK == 1 && this.positionX <= fk - 20 || this.aK == 2 && this.positionX >= fj + 20) {
               this.p(129);
            }
            break;
         case 128:
            dD = dD + (dE ? dC : -dC);
            dG = dG + dC;
            if (dG >= 180) {
               dD = dD + (dE ? -(dG - 180) : dG - 180);
            }

            this.positionX = dz + (dB * Game.l(dD) >> 8);
            this.positionY = dA + (dB * Game.m(dD) >> 8);
            if (dG >= 180) {
               this.p(127);
            }

            fr = dD % 360;
            if (fr < 0) {
               fr += 360;
            }

            if (fr >= 180) {
               this.aK = dE ? 2 : 1;
            } else {
               this.aK = dE ? 1 : 2;
            }
            break;
         case 129:
            this.cV();
            if (this.aA <= 0) {
               this.p(130);
            }
            break;
         case 130:
            this.cV();
            if (this.aA <= 428 * this.aB) {
               this.aB--;
               if (this.aB >= 0) {
                  fb[this.aB].aq = 109;
                  fb[this.aB].aA = 500;
               }
            }

            if (this.aA <= 0) {
               this.p(131);
            }

            if (Game.aN <= 0) {
               fq = fq - Game.h;
               if (fq <= 0) {
                  Game.e(Game.i(2, 5));
                  fq = Game.i(80, 600);
               }
            }
            break;
         case 131:
            this.cV();
            if (this.aA <= 0) {
               this.p(132);
            }
            break;
         case 132:
            if (this.aK == 1 && this.positionX <= fj + fk >> 1 || this.aK == 2 && this.positionX >= fj + fk >> 1) {
               this.positionX = fj + fk >> 1;
               this.p(133);
            }
            break;
         case 133:
            dD = dD + (dE ? dC : -dC);
            dG = dG + dC;
            if (dG >= 360) {
               dD = dD + (dE ? -(dG - 360) : dG - 360);
            }

            this.positionX = dz + (dB * Game.l(dD) >> 8);
            this.positionY = dA + (dB * Game.m(dD) >> 8);
            fr = dD % 360;
            if (fr < 0) {
               fr += 360;
            }

            if (fr >= 180) {
               this.aK = dE ? 2 : 1;
            } else {
               this.aK = dE ? 1 : 2;
            }

            if (dG >= 360) {
               this.positionX = dz;
               this.positionY = dA + dB;
               this.p(134);
            }
            break;
         case 134:
            this.b(false, true);
            if (this.bL) {
               this.p(135);
            }
            break;
         case 135:
            this.cV();
            if (this.aA <= 0) {
               this.p(136);
            }
            break;
         case 136:
            this.cV();
            if (this.aA <= 0) {
               this.p(137);
            }
            break;
         case 137:
            this.H = 2048;
            if (this.positionY >= fi + 40) {
               this.positionY = fi + 40;
               this.G = 2048;
               if (this.positionX >= fk + 40) {
                  this.positionX = fk + 40;
                  this.p(138);
               }
            }
            break;
         case 138:
            this.H = -3072;
            if (this.positionY <= fh - 40) {
               this.positionY = fh - 40;
               this.G = -3072;
               if (this.positionX <= (fj + fk >> 1) - 60) {
                  this.positionX = (fj + fk >> 1) - 60;
                  this.p(139);
               }
            }
            break;
         case 139:
            this.H = this.y(2560);
            if (this.positionY >= fn) {
               this.positionY = fn;
               this.p(117);
            }
            break;
         case 140:
            this.H = -this.y(2560);
            if (this.positionY <= dH - 40) {
               this.positionY = dH - 40;
               this.p(141);
            }
            break;
         case 141:
            if (this.aK == 1 && this.positionX <= dJ - 40 || this.aK == 2 && this.positionX >= dK + 40) {
               this.db();
               this.H = 3072;
               if (this.positionY >= fl) {
                  this.positionY = fl;
                  this.aK = this.aK == 1 ? 2 : 1;
                  this.p(101);
               }
            }
            break;
         case 150:
            this.aK = this.G < 0 ? 1 : 2;
            break;
         case 151:
         case 154:
            this.aK = this.G < 0 ? 1 : 2;
            this.G = this.av << 8;
            this.positionY = (this.ay * Game.m((this.aw * (this.ac - this.positionX) >> 8) + this.ax) >> 8) + this.ad;
            break;
         case 152:
            dD = dD + (dE ? dC : -dC);
            dG = dG + dC;
            if (dG < 180) {
               if (dG >= 180) {
                  dD = dD + (dE ? -(dG - 180) : dG - 180);
               }

               this.positionX = dz + (dB * Game.l(dD) >> 8);
               this.positionY = dA + (dB * Game.m(dD) >> 8);
               fr = dD % 360;
               if (fr < 0) {
                  fr += 360;
               }

               if (fr >= 90 && fr < 270) {
                  this.aK = dE ? 4 : 8;
               } else {
                  this.aK = dE ? 8 : 4;
               }
            }
            break;
         case 153:
            this.aK = this.H < 0 ? 4 : 8;
            break;
         case 156:
            this.cV();
            if (this.aA <= 0) {
               for (int var1 = 0; var1 < 11; var1++) {
                  Game.c(dp[var1]);
                  Game.e(dp[var1]);
                  dp[var1] = null;
               }

               Game.e(this);
               E = null;
               Game.U();
            }

            return;
      }

      if (this.aq != 155 && this.aq != 156) {
         if ((this.aH == 0 && this.aq != 117 || this.aH == 1 && (this.aq == 152 || this.aq == 153 || this.aq == 154))
            && b(Game.player.collisionBounds, this.collisionBounds)) {
            Game.player.a(4, this.B(1), 0, this);
         }

         this.bZ();
         this.b(this.positionX, this.positionY, this.aK);
         if (this.r != null) {
            this.r.bU();
         }

         if (this.aH == 1 && this.s != null && this.s.type == 87) {
            this.s.cd();
         }

         if (this.aq == 107 || this.aq == 130) {
            this.bV();
         }

         if (this.aq != 0 && dp != null && dp[0] != null && dp[0].aH == 1) {
            for (int var3 = 0; var3 < 11; var3++) {
               if (dp[0].aq != 152 && dp[0].aq != 153 && dp[0].aq != 154) {
                  dp[var3].r(0, dr[var3]);
               }

               dp[var3].bW();
            }

            this.bX();
         }
      }
   }

   private void bU() {
      dr[this.aF] = this.positionX;
      ds[this.aF] = this.positionY;
      this.ca();
      if (this.r != null) {
         this.b(this.positionX, this.positionY, this.aK);
         this.r.bU();
         if ((dp[0].aH == 0 && dp[0].aq != 117 || dp[0].aH == 1 && (dp[0].aq == 152 || dp[0].aq == 153))
            && b(Game.player.collisionBounds, this.collisionBounds)) {
            Game.player.a(4, dp[0].B(1), 0, this);
         }
      } else if (dp[0].aH == 0 && dp[0].aq != 117) {
         this.aq = 0;
         this.as = dp[0].as;
         this.e(false);
         dp[0].as = this.as;
         if (dp[0].as <= 0) {
            dp[0].p(3);
         }
      }

      if (this.r != null && fa[this.aF] != null) {
         if (dN == 0) {
            if (dp[this.aF].aK == 1 || dp[this.aF].aK == 2) {
               fa[this.aF].facingLeft = !this.facingLeft;
               fa[this.aF].positionX = this.positionX + (this.facingLeft ? 20 : -20);
               fa[this.aF].positionY = this.positionY - 14;
               if (fa[this.aF].animationId != 12) {
                  fa[this.aF].b(12);
                  fa[this.aF].i = Game.i(0, fa[this.aF].p.a(12));
                  return;
               }
            } else if (dp[this.aF].aK == 4) {
               fa[this.aF].positionX = this.positionX;
               fa[this.aF].positionY = this.positionY - 12;
               if (fa[this.aF].animationId != 13) {
                  fa[this.aF].b(13);
                  fa[this.aF].i = Game.i(0, fa[this.aF].p.a(13));
                  return;
               }
            } else if (dp[this.aF].aK == 8) {
               fa[this.aF].positionX = this.positionX;
               fa[this.aF].positionY = this.positionY + 12;
               if (fa[this.aF].animationId != 14) {
                  fa[this.aF].b(14);
                  fa[this.aF].i = Game.i(0, fa[this.aF].p.a(14));
                  return;
               }
            }
         } else if (dN == 1) {
            if (dp[this.aF].aK == 1 || dp[this.aF].aK == 2) {
               fa[this.aF].facingLeft = !this.facingLeft;
               fa[this.aF].positionX = this.positionX + (this.facingLeft ? 10 : -10);
               fa[this.aF].positionY = this.positionY - 8;
               fa[this.aF].b(68);
               return;
            }

            if (dp[this.aF].aK == 4 || dp[this.aF].aK == 8) {
               fa[this.aF].positionX = this.positionX;
               fa[this.aF].positionY = this.positionY + (dp[this.aF].aK == 4 ? -12 : 12);
               fa[this.aF].b(69);
            }
         }
      }
   }

   private int y(int var1) {
      return var1 + var1 * 30 / 100 * (fo - this.as) / fo;
   }

   private void r(int var1, int var2) {
      boolean var3 = false;
      if (this.collisionBounds != null) {
         this.g();
         if (aT == null) {
            int var4 = Game.player.b + Game.player.H >> 8;
            if (Game.player.H >= 0
               && Game.player.positionY <= this.collisionBounds[1] + var1
               && Game.player.collisionBounds[2] > this.collisionBounds[0]
               && Game.player.collisionBounds[0] < this.collisionBounds[2]
               && (var4 >= this.collisionBounds[1] + (this.H >> 8) || var4 >= this.collisionBounds[1])) {
               aT = this;
               Game.player.a(9);
               Game.player.positionY = this.collisionBounds[1] + var1;
               Game.player.G = aT.G;
               Game.player.H = aT.H;
               return;
            }
         } else if (aT == this) {
            Game.player.g();
            if ((Game.player.animationId == 7 || b(Game.player.collisionBounds, this.collisionBounds) || Game.player.H - this.H >= 0)
               && Game.player.positionX <= this.collisionBounds[2]
               && Game.player.positionX >= this.collisionBounds[0]) {
               Game.player.positionX = Game.player.positionX + (this.positionX - var2);
               Game.player.positionY = this.collisionBounds[1] - 1;
            } else {
               if (Game.player.positionX > this.collisionBounds[2]) {
                  for (int var6 = this.aF - 1; var6 <= this.aF + 1; var6 += 2) {
                     if (var6 >= 0 && var6 < 11 && dp[var6].positionX > this.positionX) {
                        aT = dp[var6];
                        var3 = true;
                        break;
                     }
                  }
               } else if (Game.player.positionX < this.collisionBounds[0]) {
                  for (int var5 = this.aF - 1; var5 <= this.aF + 1; var5 += 2) {
                     if (var5 >= 0 && var5 < 11 && dp[var5].positionX < this.positionX) {
                        aT = dp[var5];
                        var3 = true;
                        break;
                     }
                  }
               }

               if (!var3) {
                  aT = null;
                  Game.player.G = 0;
                  Game.player.H = 0;
                  return;
               }
            }
         }
      }
   }

   public final void Q() {
      dp[0] = this;
      this.aF = 0;

      for (int var1 = 1; var1 < 11; var1++) {
         if (dp[var1] != null) {
            Game.c(dp[var1]);
            Game.e(dp[var1]);
         }

         if (var1 == 10) {
            dp[var1] = this.a(85, 47, 2, 0, 0, this.ap - var1, false);
            dp[var1].positionX = this.positionX + 25 + 20 + 192;
         } else {
            dp[var1] = this.a(84, 47, 1, 0, 0, this.ap - var1, false);
            dp[var1].positionX = this.positionX + 25 + (var1 - 1) * 24;
         }

         dp[var1].aF = var1;
         dp[var1].aH = this.aH;
         dp[var1 - 1].b(dp[var1]);
         dq[var1 - 1] = new Vector();
         dp[var1].eX = dp[var1].positionX - dp[var1 - 1].positionX;
         dp[var1].aK = this.aK;
         dp[var1].bZ();
      }

      for (int var2 = 0; var2 < 10; var2++) {
         dp[var2].b(dp[var2 + 1].positionX, dp[var2 + 1].positionY, this.aK);
      }

      for (int var3 = 1; var3 < 10; var3++) {
         fa[var3] = this.a(-103, 21, 12, 0, 0, this.ap - var3 + 11 + 5, false);
         if (this.aH == 0) {
            fa[var3].i = Game.i(0, fa[var3].p.a(12));
         } else {
            fa[var3].c |= 128;
         }
      }
   }

   private void z(int var1) {
      int var2 = var1 == 0 ? -dO : dO;
      int var3 = var1 == 0 ? -dP : dP;

      for (int var8 = 0; var8 < 11; var8++) {
         dp[var8].aL = var1 == 0 ? 0 : 1;
         dp[var8].positionX += var2;
         dp[var8].positionY += var3;
         Vector var7;
         if (var8 < 10 && (var7 = dq[var8]) != null) {
            int var4 = var7.size();

            for (int var9 = 0; var9 < var4 / 3; var9++) {
               int var5 = this.a(var7.elementAt(var9 * 3 + 0));
               int var6 = this.a(var7.elementAt(var9 * 3 + 1));
               var5 += var2;
               var6 += var3;
               var7.removeElementAt(var9 * 3 + 0);
               var7.insertElementAt(new Integer(var5), var9 * 3 + 0);
               var7.removeElementAt(var9 * 3 + 1);
               var7.insertElementAt(new Integer(var6), var9 * 3 + 1);
            }
         }
      }

      for (int var12 = 1; var12 < 10; var12++) {
         Game.e(fa[var12]);
         if (fa[var12] != null) {
            Game.c(fa[var12]);
         }

         fa[var12] = null;
         if (var1 == 0) {
            fa[var12] = this.a(-103, 21, 12, 0, 0, this.ap - var12 + 11, false);
            fa[var12].i = Game.i(0, fa[var12].p.a(12));
         } else if (var1 == 1 && var12 % 2 == 1) {
            fa[var12] = this.a(-103, 17, 68, 0, 0, this.ap - var12 + 11, false);
         }

         if (fg && fa[var12] != null) {
            Game.b(fa[var12]);
         }
      }

      Game.player.positionX += var2;
      Game.player.positionY += var3;
   }

   private void bV() {
      int var1;
      if ((var1 = l(this.aA % 500)) >= 250) {
         var1 = 500 - var1;
      }

      for (int var3 = 1; var3 <= 4; var3++) {
         int var2 = 9 * (4 - var3 + 1) / 4;
         dp[11 - var3].positionY -= var2 - var1 * var2 / 125;
      }
   }

   private void i(boolean var1) {
      if (var1) {
         if (fg) {
            return;
         }

         fg = true;
      } else {
         fg = false;
      }

      for (int var2 = 0; var2 < 11; var2++) {
         if (var1) {
            Game.b(dp[var2]);
            if (fa[var2] != null) {
               Game.b(fa[var2]);
            }
         } else {
            Game.c(dp[var2]);
            if (fa[var2] != null) {
               Game.c(fa[var2]);
            }
         }
      }
   }

   private void bW() {
      for (int var1 = 0; var1 < Game.ck; var1++) {
         Actor var2;
         if ((var2 = Game.cj[var1]) != null && var2.type == 79) {
            dV[1] = this.collisionBounds[1] - 10;
            dV[3] = this.collisionBounds[3] + 10;
            dV[0] = this.collisionBounds[0] - 10;
            dV[2] = this.collisionBounds[2] + 10;
            if (b(var2.collisionBounds, dV)) {
               var2.a(dV, false, 79);
            }
         }
      }
   }

   private void bX() {
      for (int var1 = 0; var1 < Game.ck; var1++) {
         Actor var2;
         if ((var2 = Game.cj[var1]) != null && var2.type == 78) {
            dV[1] = var2.collisionBounds[1];
            dV[3] = dV[1] + 1;
            dV[0] = var2.collisionBounds[0];
            dV[2] = var2.collisionBounds[2];

            for (int var3 = 0; var3 < 11; var3++) {
               if (b(dp[var3].collisionBounds, dV)) {
                  this.a(var2, this.aF, dp[var3].collisionBounds[0], dp[var3].collisionBounds[2]);
               }
            }
         }
      }
   }

   private void a(Actor var1, int var2, int var3, int var4) {
      fc[var2] = fc[var2] - Game.h;
      if (fc[var2] <= 0) {
         this.a(-53, 18, 2, 0, 0, 308, false);
         aU.positionX = Game.i(var3, var4);
         aU.positionY = var1.collisionBounds[1];
         aU.aL = 1;
         aU.v = var1;
         fc[var2] = 1000;
      }
   }

   private void bY() {
      dV[1] = this.v.collisionBounds[1];
      dV[3] = dV[1] + 1;
      dV[0] = this.v.collisionBounds[0];
      dV[2] = this.v.collisionBounds[2];

      for (int var1 = 0; var1 < 11; var1++) {
         if (b(dp[var1].collisionBounds, dV)) {
            this.c(dp[var1].collisionBounds[0], dp[var1].collisionBounds[2], false);
            return;
         }
      }
   }

   private void c(int var1, int var2, boolean var3) {
      fp = fp - Game.h;
      if (fp <= 0) {
         this.a(-53, 18, 2, 0, 0, 308, false);
         aU.positionX = Game.i(var1, var2);
         aU.positionY = this.v.collisionBounds[1];
         aU.aL = 1;
         aU.v = this.v;
         fp = Game.i(80, 600);
      }

      if (var3 && Game.aN <= 0) {
         fq = fq - Game.h;
         if (fq <= 0) {
            Game.e(Game.i(2, 5));
            fq = Game.i(80, 600);
         }
      }
   }

   private void bZ() {
      int var1 = this.aF == 0 ? 0 : (this.aF == 10 ? 2 : 1);
      if (this.aK == 1 || this.aK == 2) {
         this.facingLeft = this.aK == 1;
         this.a(var1 == 0 ? 0 : (var1 == 1 ? 1 : 2));
      } else if (this.aK == 4) {
         this.a(var1 == 0 ? 3 : (var1 == 1 ? 4 : 5));
      } else {
         if (this.aK == 8) {
            this.a(var1 == 0 ? 6 : (var1 == 1 ? 7 : 8));
         }
      }
   }

   private void ca() {
      Vector var1;
      int var2 = (var1 = dq[this.aF - 1]).size() / 3;
      int var8 = 0;

      for (int var9 = 0; var9 < var2 - 1; var9++) {
         int var3 = this.a(var1.elementAt(var9 * 3 + 0));
         int var4 = this.a(var1.elementAt(var9 * 3 + 1));
         int var5 = this.a(var1.elementAt(var9 * 3 + 3));
         int var6 = this.a(var1.elementAt(var9 * 3 + 4));
         int var7 = this.a(var1.elementAt(var9 * 3 + 5));
         int var10 = Game.h(var5 - var3, var6 - var4);
         if ((var8 += var10) >= this.eX) {
            int var11 = var10 - (var8 - this.eX);
            this.positionX = var3 + (var5 - var3) * var11 / var10;
            this.positionY = var4 + (var6 - var4) * var11 / var10;
            this.aK = var7;
            this.bZ();

            for (int var12 = var2 - 1; var12 > var9 + 2; var12--) {
               var1.removeElementAt(var12 * 3 + 2);
               var1.removeElementAt(var12 * 3 + 1);
               var1.removeElementAt(var12 * 3);
            }
            break;
         }
      }
   }

   public final void b(int var1, int var2, int var3) {
      Vector var5;
      if ((var5 = dq[this.aF]) != null
         && (var5.size() == 0 || this.a(var5.elementAt(0)) != var1 || this.a(var5.elementAt(1)) != var2 || this.a(var5.elementAt(2)) != this.aK)) {
         var5.insertElementAt(new Integer(var1), 0);
         var5.insertElementAt(new Integer(var2), 1);
         var5.insertElementAt(new Integer(this.aK), 2);
      }
   }

   private int a(Object var1) {
      return ((Integer)var1).intValue();
   }

   private void cb() {
      int var1 = fj + fk >> 1;
      int var2 = fh;

      for (int var3 = 0; var3 < 7; var3++) {
         this.a(75, 17, 49, 0, 0, 302, false);
         aU.positionX = var1 - (var3 % 2 == 0 ? 1 : -1) * (var3 + 1 >> 1) * 25 - 0;
         aU.positionY = var2;
         aU.aH = 2;
         aU.aq = 104;
         fb[var3] = aU;
      }
   }

   private void cc() {
      int var1 = fj + fk >> 1;
      int var2 = this.collisionBounds[3];

      for (int var3 = 0; var3 < 4; var3++) {
         this.a(75, 17, 48, 0, 0, 302, false);
         aU.positionX = var1 - (var3 % 2 == 0 ? 1 : -1) * (var3 >> 1) * 30 - (var3 % 2 == 0 ? 1 : -1) * 50;
         aU.positionY = var2;
         aU.aH = 3;
         aU.aq = 106;
         aU.aA = 0;
      }
   }

   private void cd() {
      if (dp != null && dp[0] != null && dp[0].aq != 0 && Game.player.aq != 3) {
         this.g();
         if (a(dp[0].positionX, dp[0].positionY, this.collisionBounds)) {
            switch (this.aH) {
               case 0:
                  dp[0].db();
                  dp[0].G = this.av << 8;
                  dp[0].H = this.az << 8;
                  dp[0].c(150, true);
                  break;
               case 1:
               case 6:
                  dp[0].db();
                  dp[0].ay = this.ay;
                  dp[0].aw = this.aw;
                  dp[0].ax = this.ax;
                  dp[0].av = this.av;
                  dp[0].az = this.az;
                  if (this.aH == 1) {
                     dp[0].c(151, true);
                  } else if (this.aH == 6) {
                     dp[0].c(154, true);
                  }
                  break;
               case 2:
                  dp[0].db();
                  dp[0].ay = this.ay;
                  dp[0].av = this.av;
                  dp[0].az = this.az;
                  dp[0].c(152, true);
                  break;
               case 3:
                  dp[0].positionX -= 120;
                  dp[0].G = this.av << 8;
                  dp[0].ac = dp[0].positionX;
                  dp[0].ad = dp[0].positionY;
                  Game.player.h(3);
                  break;
               case 4:
                  dp[0].db();
                  dp[0].H = this.az << 8;
                  dp[0].c(153, true);
                  break;
               case 5:
                  du = dp[0].an;
                  R();
                  if (this.aG != -1) {
                     this.s = Game.i(this.aG);
                     this.s.c &= -33;
                     dp[0] = this.s;
                     this.s.Q();
                  }

                  Game.e(this);
                  return;
            }

            if (this.aG != -1) {
               dp[0].s = Game.i(this.aG);
            } else {
               dp[0].s = null;
            }

            Game.e(this);
         }
      }
   }

   private void ce() {
      if (Game.F(16388)) {
         dG = 90;
         dp[0].H = -1024;
         dp[0].ac = dp[0].positionX;
         dp[0].ad = dp[0].positionY + (8 * Game.m(-dG) >> 8);
      } else if (Game.F(33024)) {
         dG = -90;
         dp[0].H = 1024;
         dp[0].ac = dp[0].positionX;
         dp[0].ad = dp[0].positionY + (8 * Game.m(-dG) >> 8);
      } else {
         dp[0].H = 0;
         dp[0].positionY = (8 * Game.m(2 * (dp[0].ac - dp[0].positionX) + dG) >> 8) + dp[0].ad;
      }

      this.positionX = dp[0].positionX;
      this.positionY = dp[0].positionY;

      for (int var1 = 0; var1 < dp.length; var1++) {
         dp[var1].cf();
      }
   }

   private void cf() {
      for (int var1 = 0; var1 < Game.ck; var1++) {
         Actor var2;
         if ((var2 = Game.cj[var1]) != null
            && (var2.c & 1024) == 0
            && var2.aq != 7
            && var2.aq != 3
            && var2.aq != 5
            && var2.aq != 6
            && var2.M()
            && var2.collisionBounds != null
            && b(var2.collisionBounds, this.collisionBounds)) {
            if (var2.type == 16) {
               var2.p(105);
            } else {
               var2.p(3);
            }
         }
      }
   }

   public static final void R() {
      for (int var0 = 0; var0 < 11; var0++) {
         if (dp[var0] != null) {
            Game.c(dp[var0]);
            Game.e(dp[var0]);
         }

         if (fa[var0] != null) {
            Game.c(fa[var0]);
            Game.e(fa[var0]);
         }

         fa[var0] = null;
         dp[var0] = null;
         dr[var0] = 0;
         ds[var0] = 0;
         fc[var0] = 0;
         if (var0 < dq.length) {
            dq[var0] = null;
         }
      }
   }

   private void cg() {
      if (this.facingLeft) {
         this.G = -512;
      } else {
         this.G = 512;
      }

      if (this.G > 0) {
         if (this.collisionBounds[2] >= this.m[2]) {
            this.G = 0;
            this.aq = 105;
            this.aB = 1000;
            return;
         }
      } else if (this.collisionBounds[0] <= this.m[0]) {
         this.G = 0;
         this.aq = 106;
         this.aB = 1000;
      }
   }

   private void ch() {
      this.cV();
      this.cW();
      switch (this.aq) {
         case 0:
            this.a(0);
            if (l(this.positionX - Game.player.positionX) > 160 || !this.cZ()) {
               this.cg();
            } else if (Game.player.cI) {
               if ((this.facingLeft && Game.player.positionX < this.positionX || !this.facingLeft && Game.player.positionX > this.positionX)
                  && this.positionY <= Game.player.collisionBounds[3]
                  && this.positionY >= Game.player.collisionBounds[1]) {
                  this.p(104);
                  this.aA = 1000;
               } else {
                  this.cg();
               }
            } else {
               this.cg();
            }
            break;
         case 3:
            if (this.aA < 0) {
               this.a(39, 17, 7, 0, -5, 302, this.facingLeft);
               this.b(true);
               return;
            }
            break;
         case 104:
            this.G = this.facingLeft ? -1536 : 1536;
            if (this.aA < 0) {
               this.aq = 0;
            }

            if (this.G > 0) {
               if (this.collisionBounds[2] >= this.m[2] - 30) {
                  this.G = 0;
                  this.aq = 0;
                  this.da();
               }
            } else if (this.collisionBounds[0] <= this.m[0] + 30) {
               this.G = 0;
               this.aq = 0;
               this.da();
            }
            break;
         case 105:
         case 106:
            if (this.aB < 0) {
               this.facingLeft = this.aq == 105;
               this.aq = 0;
            }
      }

      if (this.G > 0) {
         if (this.collisionBounds[2] >= this.m[2]) {
            this.G = 0;
         }
      } else if (this.collisionBounds[0] <= this.m[0]) {
         this.G = 0;
      }

      if (this.aq != 0 && this.aq != 3 && l(this.positionX - Game.player.positionX) > 160) {
         this.aq = 0;
      }

      if (this.at()) {
         this.b(x(), false);
      } else {
         if (b(this.collisionBounds, Game.player.collisionBounds) && this.aq != 0) {
            Game.player.a(4, this.B(1), 0, this);
         }
      }
   }

   private void ci() {
      if (b(this.collisionBounds, Game.player.collisionBounds)) {
         Game.player.a(4, this.ay, 0, this);
         Game.e(this);
      }

      this.eX = Game.h(Game.player.a - this.a, Game.player.b - this.b) >> 8;
      if (this.eX >= 640) {
         Game.e(this);
      }
   }

   private void cj() {
      switch (this.aq) {
         case 0:
            if (b(Game.player.collisionBounds, this.m)) {
               this.fv = Game.aJ.collisionBounds[0] + 20;
               this.fu = Game.aJ.collisionBounds[2] - 20;
               if (this.aH == 2) {
                  this.p(105);
                  this.aB = 0;
                  this.av = this.positionY;
               } else {
                  this.p(101);
               }
            }
            break;
         case 2:
            if (this.e()) {
               if (this.aH == 2) {
                  this.p(106);
               } else {
                  this.p(103);
               }
            }
            break;
         case 3:
            if (this.r != null) {
               Game.e(this.r);
               this.r = null;
            }

            if (this.e()) {
               this.b(true);
            }
         case 4:
         case 8:
         default:
            break;
         case 9:
            if (this.r != null) {
               Game.e(this.r);
               this.r = null;
            }

            if (this.e()) {
               this.a(true, true);
            }
            break;
         case 101:
            if (this.aH == 1 && l(Game.player.a - this.a) >> 8 <= 80) {
               this.p(104);
            }

            int var1 = Game.player.positionY - this.positionY;
            if (l(Game.player.a - this.a) >> 8 <= 200 && var1 > -20 && var1 < 60) {
               this.r = this.a(-41, 15, 0, this.facingLeft ? -10 : 10, -10, 302, false);
               this.r.c |= 16;
               this.r.ay = this.B(1);
               this.p(102);
            }
            break;
         case 102:
            if (this.e()) {
               if (this.aH == 2) {
                  this.eX = Game.h(this.r.aw, this.r.az);
                  int var2 = this.eX / 1024;
                  this.r.G = this.r.aw / var2;
                  this.r.H = this.r.az / var2;
                  this.p(106);
               } else {
                  this.r.G = this.facingLeft ? -768 : 768;
                  this.p(103);
               }

               this.r = null;
            }
            break;
         case 103:
            this.cV();
            if (this.aH == 1 && l(Game.player.a - this.a) >> 8 <= 80) {
               this.p(104);
            }

            if (this.aA <= 0) {
               this.p(101);
            }
            break;
         case 104:
            if (l(this.b - Game.player.b) >= 512) {
               this.H = (Game.player.b - this.b > 10 ? 1 : -1) * 512;
            } else {
               this.H = 0;
            }

            this.b(true, true);
            if (l(Game.player.a - this.a) >> 8 > 80) {
               this.p(101);
            }
            break;
         case 105:
            this.G = this.D(1792);
            this.da();
            if (l(Game.player.a - this.a >> 8) <= 80) {
               this.G = 0;
            }

            if (this.aB <= 0) {
               this.aB += 1000;
            }

            this.positionY = this.av + (Game.m(this.aB * 360 / 1000) * 5 >> 8);
            if (this.aA <= 0) {
               if (l(Game.player.a - this.a >> 8) <= 80) {
                  this.r = this.a(-41, 15, 0, this.facingLeft ? -10 : 10, -10, 302, false);
                  this.r.c |= 16;
                  this.r.ay = this.B(1);
                  this.r.aw = Game.player.a - (this.r.positionX << 8);
                  this.r.az = Game.player.b - (this.r.positionY << 8);
                  this.p(102);
               }
            } else {
               this.cV();
            }

            if (this.positionX <= this.fv && this.G < 0) {
               this.G = 0;
               this.positionX = this.fv;
            }

            if (this.positionX >= this.fu && this.G > 0) {
               this.G = 0;
               this.positionX = this.fu;
            }
            break;
         case 106:
            this.cV();
            this.cW();
            if (this.aB <= 0) {
               this.aB += 1000;
            }

            this.positionY = this.av + (Game.m(this.aB * 360 / 1000) * 5 >> 8);
            if (l(this.aw - this.a >> 8) >= 80 && (this.aw - this.a > 0 ? 1 : -1) == this.ay || this.fv >= this.positionX || this.fu <= this.positionX) {
               this.p(105);
            }
      }

      if (!this.c()) {
         if (this.e(true) && this.r != null) {
            Game.e(this.r);
            this.r = null;
         }
      }
   }

   private Actor ck() {
      this.a(19, 2, 17, this.facingLeft ? -12 : 12, -27, 302, false);
      aU.facingLeft = this.facingLeft;
      aU.p(101);
      aU.M = true;
      return aU;
   }

   private void cl() {
      switch (this.aq) {
         case 3:
            Game.e(this);
            break;
         case 101:
            if (!b(Game.viewBounds, this.collisionBounds)) {
               Game.e(this);
            }
      }

      if (b(Game.player.collisionBounds, this.collisionBounds)) {
         Game.player.a(4, 5, 0, this);
         this.p(3);
      }
   }

   private void cm() {
      this.ay = this.ac - this.positionX;
      this.av = this.ad - this.positionY;
      this.eX = Game.h(this.ay, this.av);
      if (this.eX == 0) {
         this.eX = 1;
      }

      this.G = this.eX == 1 ? 0 : this.ay * 896 / this.eX;
      this.H = this.eX == 1 ? 0 : this.av * 896 / this.eX;
      this.ay = this.ay * -24 / this.eX;
      this.av = this.av * -24 / this.eX;
   }

   private void cn() {
      switch (this.aq) {
         case 3:
            if (this.e()) {
               Game.e(this);
               return;
            }

            if (this.s == Game.player) {
               this.co();
            }
            break;
         case 101:
            this.cV();
            this.cW();
            if (this.aA <= 0) {
               this.p(3);
               return;
            }

            if (this.t != null && (this.t.G != 0 || this.t.H != 0)) {
               this.ac = this.t.positionX;
               this.ad = this.t.positionY - 20;
               this.cm();
            }

            if (i(this.G, this.ay)) {
               this.G = 0;
            } else {
               this.I = this.ay;
            }

            if (i(this.H, this.av)) {
               this.H = 0;
            } else {
               this.J = this.av;
            }

            if (this.s == Game.player) {
               this.co();
               return;
            }

            if (b(Game.player.collisionBounds, this.collisionBounds)) {
               Game.player.a(4, 5, 0, this);
               this.p(3);
               return;
            }

            if (this.f(this.collisionBounds)) {
               this.p(3);
               return;
            }
      }
   }

   private void co() {
      for (int var1 = 0; var1 < Game.ci; var1++) {
         Actor var2;
         if ((var2 = Game.ch[var1]) != null && (var2.c & 1024) == 0 && (var2.M() || this.s(var2)) && b(var2.collisionBounds, this.collisionBounds)) {
            if (this.s(var2)) {
               if (var2.aq == 0 || var2.aq == 102) {
                  var2.av();
               }
            } else {
               var2.s(10);
            }

            this.p(3);
            this.G = 0;
         }
      }
   }

   private Actor cp() {
      Actor var1;
      (var1 = this.a(18, 12, 5, this.facingLeft ? -27 : 27, -43, 302, !this.facingLeft)).s = this;
      if (this != Game.player) {
         var1.ac = Game.player.eA;
         var1.ad = Game.player.eB;
      } else {
         Actor var2;
         if ((var2 = this.j(true)) != null) {
            var1.ac = var2.positionX;
            var1.ad = var2.positionY;
            var1.t = var2;
         } else {
            var1.ac = var1.positionX + (this.facingLeft ? -10 : 10);
            var1.ad = var1.positionY;
         }
      }

      var1.p(101);
      var1.c |= 16;
      return var1;
   }

   private boolean s(Actor var1) {
      return var1.type == 35 && (var1.animationId == 9 || var1.animationId == 6);
   }

   private Actor j(boolean var1) {
      int var2 = 200000;
      Actor var3 = null;

      for (int var4 = 0; var4 < Game.ci; var4++) {
         Actor var5;
         int var6;
         if ((var5 = Game.ch[var4]) != null
            && (var5.c & 1024) == 0
            && (var5.M() || this.s(var5))
            && (!var1 || (!this.facingLeft || this.positionX >= var5.positionX) && (this.facingLeft || this.positionX <= var5.positionX))
            && (
                  var6 = (var5.positionX - this.positionX) * (var5.positionX - this.positionX)
                     + (var5.positionY - this.positionY) * (var5.positionY - this.positionY)
               )
               < var2) {
            var3 = var5;
            var2 = var6;
         }
      }

      return var3;
   }

   private void cq() {
      switch (this.aq) {
         case 0:
            this.N = true;
            if (this.animationId == 3) {
               this.aF = 0;
            }

            if (this.at()) {
               this.facingLeft = Game.player.positionX > this.positionX;
               this.aq = 102;
               if (this.aF == 0) {
                  this.a(4);
                  return;
               }
            }
            break;
         case 101:
            if (this.at()) {
               this.aq = 102;
               if (this.aF == 0) {
                  this.facingLeft = Game.player.positionX > this.positionX;
                  if (this.animationId == 5) {
                     this.a(6);
                     this.N = false;
                     return;
                  }
               }
            }
            break;
         case 102:
            if ((this.e() || this.aF == 0 && this.animationId == 4) && this.aF == 0) {
               if (this.animationId == 4) {
                  this.aq = 101;
                  this.a(5);
                  return;
               }

               if (this.animationId == 6) {
                  this.aq = 103;
                  return;
               }
            }
            break;
         case 103:
            this.N = false;
            Game.e(this);
      }
   }

   private void cr() {
      if (this.aG != -1 && this.s == null) {
         this.s = Game.i(this.aG);
      }

      if (this.s != null && this.s.type == 49) {
         if ((this.s.c & 16) != 0) {
            this.positionY = this.positionY - this.s.ay;
            this.av = this.av + this.s.ay;
            if (this.positionY >= Game.viewBounds[3] - 4) {
               int var1 = this.positionY - Game.viewBounds[3] + 4;
               this.av += var1;
               this.positionY = Game.viewBounds[3] - 4;
            }

            if (Game.levelIndex == 6) {
               Game.aC = this.positionY - Game.cameraY;
            }

            this.ac = this.s.ay;
         }

         this.g();
      }

      if (b(Game.player.collisionBounds, this.collisionBounds)) {
         Game.player.a(4, 500, 0, this);
         Game.player.positionY = this.collisionBounds[1];
      }

      for (int var3 = 0; var3 < Game.ck; var3++) {
         Actor var2;
         if ((var2 = Game.cj[var3]) != null && (var2.c & 1024) == 0 && var2.aq != 7 && var2.aq != 3 && var2.aq != 5 && var2.aq != 6) {
            if ((var2.M() || var2.type == 70) && var2.type != 85 && var2.collisionBounds != null && b(var2.collisionBounds, this.collisionBounds)) {
               var2.s(500);
            } else if (var2.type == 61) {
               var2.a(this.collisionBounds, false, 61);
            }
         }
      }
   }

   private void cs() {
      switch (this.aq) {
         case 0:
            this.N = true;
            this.O = true;
            this.P = true;
            if (this.e()) {
               this.aq = 102;
               this.a(30);
            }
            break;
         case 101:
            if (this.e()) {
               Game.e(this);
               this.N = false;
               this.O = false;
               this.P = false;
               return;
            }
         case 102:
      }
   }

   private void ct() {
      if (this.B != -1 && this.v == null) {
         this.v = Game.i(this.B);
      }

      if (this.C != -1 && this.w == null) {
         this.w = Game.i(this.C);
      }

      switch (this.aq) {
         case 0:
            if (this.v != null) {
               if (this.w == null) {
                  if (this.v.aq == 101) {
                     this.aq = 102;
                     return;
                  }
               } else if (this.v.aq == 101 && this.w.aq == 101) {
                  this.aq = 102;
                  return;
               }
            }
            break;
         case 102:
            this.O = true;
            this.P = true;
            this.i(this.aC);
            if (this.fv != 0) {
               this.fv += -4;
               if (this.aC >= 4) {
                  this.H = this.aC == 4 ? -1024 : 1024;
                  return;
               }

               this.G = this.aC == 1 ? -1024 : 1024;
               return;
            }

            this.G = this.H = 0;
            this.aq = 105;
            return;
         case 103:
            this.H = 0;
            this.G = 0;
            this.a(this.m, true);
            return;
         case 104:
            this.H = 0;
            this.G = 0;
            this.i(this.aC);
            break;
         case 105:
            if (this.v != null) {
               if (this.w == null) {
                  if (this.v.aq != 101) {
                     this.aq = 106;
                     return;
                  }
               } else if (this.v.aq != 101 || this.w.aq != 101) {
                  this.aq = 106;
                  return;
               }
            }
            break;
         case 106:
            if (this.aC >= 4) {
               if (this.positionY != this.az) {
                  this.positionY = this.az;
                  this.fv = 20 * this.aH;
                  this.aq = 107;
               }
            } else if (this.positionX != this.aw) {
               this.positionX = this.aw;
               this.fv = 20 * this.aH;
               this.aq = 107;
            }

            if (this.positionY == this.az && this.positionX == this.aw) {
               this.aq = 107;
            }

            this.i(this.aC);
            return;
         case 107:
            this.aq = 0;
            this.O = false;
            return;
      }
   }

   public final void i(int var1) {
      this.g();
      if (var1 == 1) {
         ey[0] = this.m[0];
      } else {
         ey[0] = this.collisionBounds[0];
      }

      if (var1 == 2) {
         ey[2] = this.m[2];
      } else {
         ey[2] = this.collisionBounds[2];
      }

      if (var1 == 4) {
         ey[1] = this.m[1];
      } else {
         ey[1] = this.collisionBounds[1];
      }

      if (var1 == 8) {
         ey[3] = this.m[3];
      } else {
         ey[3] = this.collisionBounds[3];
      }

      if (var1 != 9) {
         this.a(this.m, true);
      }

      this.a(ey, false);
   }

   private void cu() {
      switch (this.aq) {
         case 0:
            this.aB = this.aB + Game.h;
            if (this.aB >= 1000) {
               this.aB = 0;
            }

            if (this.animationId == 1) {
               int var1 = Game.player.b + Game.player.H >> 8;
               if (Game.player.positionY <= this.collisionBounds[1] + 10
                  && Game.player.collisionBounds[2] > this.collisionBounds[0]
                  && Game.player.collisionBounds[0] < this.collisionBounds[2]
                  && var1 >= this.collisionBounds[1]) {
                  this.ay = Game.player.positionX;
                  this.a(this.collisionBounds, true);
                  this.aq = 100;
                  this.aA = 0;
                  return;
               }
            } else {
               if (this.animationId != 9) {
                  this.a(this.collisionBounds, false);
                  return;
               }

               this.a(this.collisionBounds, false);
               if (E != null && E.aq == 130) {
                  this.ay = E.positionX;
                  this.a(this.collisionBounds, true);
                  this.aq = 100;
                  this.aA = 0;
                  return;
               }
            }
            break;
         case 100:
            this.aA = this.aA + Game.h;
            if (this.aA >= (this.animationId == 7 ? 300 : 1000)) {
               this.aq = 101;
               return;
            }
            break;
         case 101:
            Game.e(this);
      }
   }

   private void cv() {
      if (this.animationId == 3 && b(this.collisionBounds, Game.player.collisionBounds)) {
         bP = true;
         if (!bF) {
            bF = true;
            b(0, 68, true);
         }

         Game.e(this);
      } else if (this.animationId == 4) {
         if (Game.player.positionX >= this.positionX) {
            bP = false;
            Game.e(this);
         }
      } else {
         switch (this.aq) {
            case 0:
               if (b(Game.player.collisionBounds, this.collisionBounds)) {
                  this.aq = 102;
                  if (this.animationId == 5) {
                     if (!bB) {
                        bB = true;
                        b(0, 108, false);
                     }

                     Game.by = 1;
                  } else if (this.animationId == 1) {
                     ej[1] = 1;
                     cv = 14;
                     Game.by = 1;
                     if (!bz) {
                        bz = true;
                        b(0, 66, false);
                     }
                  } else if (this.animationId == 8) {
                     if (!bD && Game.levelIndex == 0) {
                        b(0, 109, false);
                        bD = true;
                        Game.magic = Game.maxMagic;
                     }

                     Game.by = 1;
                  } else if (this.animationId == 6) {
                     ej[2] = 1;
                     Game.by = 1;
                     if (!by) {
                        by = true;
                        b(0, 64, false);
                     }
                  } else if (this.animationId == 7) {
                     ej[0] = 1;
                     if (!bA) {
                        bA = true;
                        b(0, 65, false);
                     }

                     Game.by = 0;
                  }

                  if (Game.player.aq == 0 || Game.player.aq == 2) {
                     Game.player.b(36);
                  }

                  Game.player.db();
                  return;
               }
               break;
            case 100:
               this.H = -2048;
               int var4 = Game.player.positionY - Game.player.collisionBounds[3] + Game.player.collisionBounds[1] - 15;
               if (this.positionY < var4) {
                  this.positionY = var4;
                  this.H = 0;
               }

               if (!Game.modalActive) {
                  Game.player.c &= -4097;
                  this.c &= -4097;
                  Game.bw = false;
                  Game.e(this);
                  return;
               }
               break;
            case 102:
               this.eX = Game.h(Game.player.a - this.a, Game.player.b - this.b);
               if (this.eX <= 40960) {
                  this.G = this.eX == 0 ? 0 : (Game.player.a - this.a) * 2048 / this.eX;
                  this.H = this.eX == 0 ? 0 : (Game.player.b - this.b) * 2048 / this.eX;
               }

               if (this.eX < 2048) {
                  this.positionX = Game.player.positionX;
                  this.positionY = Game.player.positionY;
                  this.G = 0;
                  this.H = 0;
                  this.facingLeft = Game.player.facingLeft;
                  Game.Q = Game.player.positionX;
                  Game.R = Game.player.positionY;
                  Game.T();
                  Game.I(4);
                  Game.h(3);
                  Game.player.c |= 4096;
                  Game.bw = true;
                  this.c |= 4096;
                  this.aq = 100;
                  e(Game.cb);
                  return;
               }
               break;
            case 103:
               this.H = -2048;
               int var1 = Game.player.positionY - Game.player.collisionBounds[3] + Game.player.collisionBounds[1] - 15;
               if (this.positionY < var1) {
                  this.positionY = var1;
                  this.H = 0;
                  int var2 = Game.i(8, 11);

                  for (int var3 = 0; var3 < var2; var3++) {
                     this.a(35, 31, 5, Game.i(-20, 20), -Game.i(-40, 10), 308, false);
                  }

                  Game.e(this);
               }
            case 104:
         }
      }
   }

   public final void S() {
      if (this.aH == 1) {
         boolean var1 = false;
         if (ej[1] == 1) {
            var1 = true;
         } else if (ej[2] == 1) {
            var1 = true;
         } else if (ej[0] == 1) {
            var1 = true;
         }

         if (b(Game.player.collisionBounds, this.collisionBounds) && (Game.player.aq == 0 || cv != 14 || Game.player.u == null)) {
            if (this.w == null && this.ay == 0 && var1) {
               this.w = this.a(21, 16, 25, 0, -100, 0, false);
               aU = null;
            }

            if (Game.G(65568) && this.w != null) {
               if (Game.player.aq != 0) {
                  Game.player.h(0);
                  Game.e(this.w);
                  this.w = null;
                  this.ay = 12;
               } else if (Game.player.z()) {
                  Game.player.x = this;
               }
            }
         } else if (this.w != null) {
            Game.e(this.w);
            this.w = null;
         }

         if (this.ay != 0 && !Game.modalActive && Game.stateFrame % 2 == 0) {
            this.ay--;
         }
      }
   }

   public final boolean g(Actor var1) {
      if (var1 != null) {
         var1.g();
         if (b(var1.collisionBounds, this.collisionBounds)) {
            return true;
         }
      }

      return false;
   }

   public final boolean h(Actor var1) {
      return var1 != null && (var1.c & 1024) != 0;
   }

   public final boolean a(int var1, int var2, boolean var3) {
      if (var2 == -1) {
         return var3;
      } else {
         return var1 != -1 && Game.cq[var1] == -99 ? true : var1 == 0;
      }
   }

   private void cw() {
      if (this.y != -1 && this.s == null) {
         this.s = Game.i(this.y);
         if (this.s != null) {
            this.ax = this.s.af;
         } else {
            this.ax = -1;
            if (this.aA == 4) {
               this.y = -1;
            }
         }
      } else if (this.s == null) {
         this.ax = -1;
      }

      if (this.z != -1 && this.t == null) {
         this.t = Game.i(this.z);
         if (this.t != null) {
            this.av = this.t.af;
         } else {
            this.av = -1;
         }
      } else if (this.t == null) {
         this.av = -1;
      }

      if (this.A != -1 && this.u == null) {
         this.u = Game.i(this.A);
         if (this.u != null) {
            this.az = this.u.af;
         } else {
            this.az = -1;
         }
      } else if (this.u == null) {
         this.az = -1;
      }

      if (this.aB == 0) {
         if (this.B != -1 && this.v == null) {
            this.v = Game.i(this.B);
         }

         if (this.C != -1 && this.w == null) {
            this.w = Game.i(this.C);
         }

         if (this.D != -1 && this.x == null) {
            this.x = Game.i(this.D);
         }
      }

      boolean var1 = false;
      switch (this.aA) {
         case 0:
            if (b(this.collisionBounds, Game.aJ.collisionBounds)) {
               var1 = true;
               this.c |= 16;
            } else {
               this.c &= -17;
            }
            break;
         case 1:
            if (this.g(this.s) || this.g(this.t) || this.g(this.u)) {
               var1 = true;
            }
            break;
         case 2:
            if (this.g(this.s) && this.g(this.t) && this.g(this.u)) {
               var1 = true;
            }
            break;
         case 3:
            if (this.a(this.ax, this.y, false) || this.a(this.av, this.z, false) || this.a(this.az, this.A, false)) {
               var1 = true;
            }
            break;
         case 4:
            if (this.a(this.ax, this.y, true) && this.a(this.av, this.z, true) && this.a(this.az, this.A, true)) {
               var1 = true;
            }
            break;
         case 5:
            if (this.h(this.s) || this.h(this.t) || this.h(this.u)) {
               var1 = true;
            }
            break;
         case 6:
            if (this.h(this.s) && this.h(this.t) && this.h(this.u)) {
               var1 = true;
            }
      }

      if (var1) {
         switch (this.ay) {
            case 0:
               if (this.aB == 0) {
                  this.l(this.v);
                  this.l(this.w);
                  this.l(this.x);
                  this.T();
               } else {
                  this.a((byte)1);
                  this.U();
               }
               break;
            case 1:
               this.a((byte)0);
               break;
            case 2:
               this.k(this.v);
               this.k(this.w);
               this.k(this.x);
               break;
            case 3:
               this.m(this.v);
               this.m(this.w);
               this.m(this.x);
               this.T();
               break;
            case 4:
               this.n(this.v);
               this.n(this.w);
               this.n(this.x);
               this.T();
               break;
            case 5:
               this.o(this.v);
               this.o(this.w);
               this.o(this.x);
               this.T();
               break;
            case 6:
               this.j(this.v);
               this.j(this.w);
               this.j(this.x);
               this.T();
               break;
            case 7:
               this.i(this.v);
               this.i(this.w);
               this.i(this.x);
               break;
            case 8:
               this.x(this.B, this.C);
         }
      } else {
         switch (this.ay) {
            case 0:
               this.U();
               return;
            case 1:
               if (this.aB != 0) {
                  this.b((byte)0);
                  return;
               }
               break;
            case 2:
               this.m(this.v);
               this.m(this.w);
               this.m(this.x);
         }
      }
   }

   public final void i(Actor var1) {
      if (var1 != null) {
         var1.aq = 101;
         var1.a(64);
      }
   }

   public final void j(Actor var1) {
      if (var1 != null) {
         var1.c &= -8193;
      }
   }

   public final void T() {
      this.s = null;
      this.t = null;
      this.u = null;
      this.v = null;
      this.w = null;
      this.x = null;
      Game.e(this);
   }

   public final void a(byte var1) {
      if (this.B != -1 && this.aw == -1) {
         int var2 = Game.B(this.B);
         Game.dg[Game.df] = var2;
         Game.dh[Game.df] = var1;
         Game.df++;
         Game.x(var2);
         this.aw = 1;
      }
   }

   public final void U() {
      if (this.aw == 1) {
         int var1 = Game.B(this.B);
         if (Game.cX[var1] == -2) {
            Game.y(var1);
            this.c((byte)1);
         }
      }
   }

   public final void b(byte var1) {
      if (this.B != -1 && this.aw == 1) {
         Game.y(Game.B(this.B));
         this.c(var1);
      }
   }

   public final void c(byte var1) {
      Game.df--;
      this.aw = -1;
      if (var1 == 1) {
         Game.e(this);
      }
   }

   public final void k(Actor var1) {
      if (var1 != null) {
         var1.c &= -33;
         if ((var1.c & 16) == 0) {
            var1.c |= 16;
         }
      }
   }

   public final void l(Actor var1) {
      if (var1 != null) {
         var1.c &= -33;
         if (var1.aq != 5) {
            var1.c &= -129;
         }

         if (var1.type == 20) {
            var1.c |= 16;
         }

         if (var1.type == 52) {
            var1.a(39, 17, 57, 0, 0, 302, false);
         }
      }
   }

   public final void m(Actor var1) {
      if (var1 != null) {
         if (var1.type == 49) {
            var1.cy();
         }

         var1.c |= 32;
         var1.c &= -17;
      }
   }

   public final void n(Actor var1) {
      if (var1 != null) {
         var1.c |= 1024;
      }
   }

   public final void o(Actor var1) {
      if (var1 != null && var1.type == 49) {
         var1.cy();
      } else {
         Game.e(var1);
      }
   }

   private void cx() {
      if (!Game.ax) {
         if (this.aG != -1 && this.v == null) {
            this.v = Game.i(this.aG);
            if (this.v != null) {
               this.ax = this.v.af;
            }
         }

         if (this.v != null) {
            if (Game.cq[this.ax] == -99) {
               this.cy();
               return;
            }

            if (this.v.type == 9 && this.v.G == 0) {
               return;
            }
         }

         if (c(Game.player.collisionBounds, this.collisionBounds)) {
            Game.ay = true;
            this.c |= 16;
         } else {
            if (Game.aB != this.an && Game.ay) {
               return;
            }

            Game.ay = false;
            if (Game.aB == this.an) {
               Game.aB = -1;
               if (this.aA >= 4
                  && (
                     (this.aB & 1) != 0 && Game.player.collisionBounds[0] <= this.collisionBounds[0]
                        || (this.aB & 2) != 0 && Game.player.collisionBounds[2] >= this.collisionBounds[2]
                        || (this.aB & 4) != 0 && Game.player.collisionBounds[1] <= this.collisionBounds[1]
                        || (this.aB & 8) != 0 && Game.player.collisionBounds[3] <= this.collisionBounds[3]
                  )) {
                  this.cy();
               }
            }

            this.c &= -17;
         }

         Game.az = this.aA;
         if (Game.az == 0 && this.v == null) {
            Game.ay = false;
            Game.aB = -1;
         }

         if (!Game.ay) {
            Game.aB = -1;
         } else {
            switch (this.aA) {
               case 0:
               case 3:
                  this.v.g();
                  int var1 = Game.player.positionX - 10 < this.v.positionX - 10 ? Game.player.positionX - 10 : this.v.positionX - 10;
                  int var2 = Game.player.positionX + 10 > this.v.positionX + 10 ? Game.player.positionX + 10 : this.v.positionX + 10;
                  int var3 = Game.player.positionY - 50 < this.v.positionY - 50 ? Game.player.positionY - 50 : this.v.positionY - 50;
                  int var4 = Game.player.positionY > this.v.positionY ? Game.player.positionY : this.v.positionY;
                  int var5 = var1 + (var2 - var1 >> 1);
                  int var6 = var3 + (var4 - var3 >> 1);
                  if (Game.player.G != 0 || Game.player.H != 0 || this.aA == 3) {
                     Game.ac = var5 - 120;
                     Game.ad = var6 - 160;
                  }

                  if (Game.ac + 240 > Game.player.positionX + 10
                     && Game.ac < Game.player.positionX - 10
                     && Game.ad + 320 > Game.player.positionY
                     && Game.ad < Game.player.positionY - 50) {
                     Game.aB = this.an;
                  } else if (0 == this.aA) {
                     Game.ay = false;
                     Game.aB = -1;
                  } else {
                     Game.aB = this.an;
                     if (Game.ac + 240 <= Game.player.positionX + 10) {
                        Game.ac = Game.player.positionX + 10 - 240;
                     } else if (Game.ac >= Game.player.positionX - 10) {
                        Game.ac = Game.player.positionX - 10;
                     } else if (Game.ad + 320 <= Game.player.positionY) {
                        Game.ad = Game.player.positionY - 320;
                     } else if (Game.ad >= Game.player.positionY - 50) {
                        Game.ad = Game.player.positionY - 50;
                     }
                  }
                  break;
               case 1:
                  if (Game.player.H > 0) {
                     Game.ad = Game.player.positionY - 160;
                     Game.aB = this.an;
                  } else {
                     Game.ay = false;
                     Game.aB = -1;
                  }
               case 2:
                  if ((this.aB & 1) != 0) {
                     Game.regionLeft = this.collisionBounds[0];
                  }

                  if ((this.aB & 2) != 0) {
                     Game.regionRight = this.collisionBounds[2];
                  }

                  if ((this.aB & 4) != 0) {
                     Game.ai = this.collisionBounds[1];
                  }

                  if ((this.aB & 8) != 0) {
                     Game.aj = this.collisionBounds[3];
                  }

                  if (Game.player.positionX > this.collisionBounds[2]) {
                     Game.player.positionX = this.collisionBounds[2];
                  }

                  if (Game.player.positionX < this.collisionBounds[0]) {
                     Game.player.positionX = this.collisionBounds[0];
                  }
                  break;
               case 4:
               case 5:
                  Game.player.g();
                  Game.aB = this.an;
                  boolean var7 = false;
                  int var8 = this.ay;
                  if (this.v != null) {
                     var8 = Math.abs(this.v.G >> 8);
                  }

                  fs = var8;
                  if (fs == 12 && !aX() && !aY()) {
                     Game.player.a(37);
                  }

                  if ((this.aB & 1) != 0) {
                     Game.ac = Game.cameraX - var8;
                     if (Game.player.collisionBounds[0] > this.collisionBounds[0]) {
                        if (Game.player.positionX > Game.viewBounds[2] - 10) {
                           if (Game.player.eE <= 21) {
                              Game.player.positionX = Game.viewBounds[2] - 10;
                              if (3 != Game.be[Game.levelIndex] && Game.player.G > 0) {
                                 Game.player.G = 0;
                              }
                           } else if (Game.player.collisionBounds[0] >= Game.viewBounds[2]) {
                              var7 = true;
                           } else if (3 != Game.be[Game.levelIndex]) {
                              Game.player.G = 0;
                           }
                        } else if (Game.player.positionX < Game.cameraX + 10) {
                           Game.player.positionX = Game.cameraX + 10;
                        }

                        Game.aD = true;
                     }
                  }

                  if ((this.aB & 2) != 0 && Game.player.collisionBounds[2] < this.collisionBounds[2]) {
                     Game.ac = Game.cameraX + var8;
                     if (Game.player.positionX < Game.viewBounds[0] + 30) {
                        if (Game.player.eF <= 21) {
                           Game.player.positionX = Game.viewBounds[0] + 30;
                           if (3 != Game.be[Game.levelIndex] && Game.player.G < 0) {
                              Game.player.G = 0;
                           }
                        } else if (Game.player.positionX <= Game.viewBounds[0]) {
                           var7 = true;
                        } else {
                           Game.player.G = 0;
                        }
                     } else if (Game.player.positionX > Game.viewBounds[2] - 10) {
                        Game.player.positionX = Game.viewBounds[2] - 10;
                     }

                     Game.aD = true;
                  }

                  if ((this.aB & 4) != 0) {
                     if (Game.player.collisionBounds[1] > this.collisionBounds[1]) {
                        Game.ad = Game.cameraY - var8;
                        if (Game.player.positionY > Game.viewBounds[3]) {
                           var7 = true;
                        } else if (Game.player.positionY < Game.viewBounds[1]) {
                           Game.player.positionY = Game.viewBounds[1];
                        }

                        Game.aD = false;
                     }

                     if (5 == this.aA) {
                        if (Game.player.positionX > Game.cameraX + 240 - 10) {
                           Game.player.positionX = Game.cameraX + 240 - 10;
                        }

                        if (Game.player.positionX < Game.cameraX + 10) {
                           Game.player.positionX = Game.cameraX + 10;
                        }
                     }
                  }

                  if ((this.aB & 8) != 0 && Game.player.collisionBounds[3] < this.collisionBounds[3]) {
                     Game.ad = Game.cameraY + var8;
                     if (Game.player.positionY < Game.viewBounds[1]) {
                        var7 = true;
                     } else if (Game.player.positionY > Game.viewBounds[3]) {
                        Game.player.positionY = Game.viewBounds[3];
                     }

                     Game.aD = false;
                  }

                  if (var7) {
                     Game.player.a(4, 10000, 0, this);
                     Game.health = -100;
                     this.cy();
                  }
            }
         }
      }
   }

   private void cy() {
      this.v = null;
      this.aG = -1;
      Game.e(this);
      Game.ay = false;
      Game.aB = -1;
   }

   private void cz() {
      if (this.aG != -1 && this.r == null) {
         this.b(Game.i(this.aG));
      }

      boolean var1 = false;
      this.d(0, this.collisionBounds);
      this.g();

      for (int var3 = 0; var3 < Game.ck; var3++) {
         Actor var2;
         if ((var2 = Game.cj[var3]) != null && var2 != this) {
            var2.g();
            if (var2.T == this) {
               var2.positionY = this.collisionBounds[1] - 1;
               var2.bL = true;
               dV[1] = var2.collisionBounds[1];
               dV[0] = var2.collisionBounds[0];
               dV[3] = var2.collisionBounds[3] + 2;
               dV[2] = var2.collisionBounds[2];
               if (!b(this.collisionBounds, dV)) {
                  var2.T = null;
                  this.U = null;
               }

               if (var2.T == this) {
                  var1 = true;
               }
            }
         }
      }

      if (aT == this || var1) {
         if (this.positionY < this.az - 7) {
            this.positionY++;
         }

         if (this.r.aq != 103) {
            this.r.aq = 102;
            this.r.fv = 10;
         }
      }

      if (aT != this && !var1) {
         if (this.positionY > this.av) {
            this.positionY--;
         }

         this.r.aq = 106;
      }
   }

   private void cA() {
      if (this.e()) {
         Game.e(this);
      }
   }

   private void cB() {
      this.cI = false;
      this.cJ = null;

      for (int var2 = 0; var2 < Game.ck; var2++) {
         Actor var1;
         if ((var1 = Game.cj[var2]) != null && var1.type == 78 && b(this.collisionBounds, var1.collisionBounds)) {
            this.cI = true;
            this.cJ = var1;
            this.bL = true;
            break;
         }
      }

      label303:
      switch (this.aq) {
         case 0:
            switch (this.aH) {
               case 0:
                  if (this.cI) {
                     this.p(107);
                  } else if (this.aH == 0) {
                     this.d(0, this.collisionBounds);
                  }
               default:
                  break label303;
            }
         case 100:
            if (this.aH == 5 || this.aH == 4 || this.aH == 2) {
               this.positionX = Game.cameraX + this.ac;
               this.ad += -4;
               this.positionY = this.ad + Game.cameraY;
            }

            if (!b(Game.viewBounds, this.collisionBounds)) {
               this.p(101);
               this.db();
            }
            break;
         case 101:
            if (this.aH == 5 || this.aH == 4) {
               this.positionX = Game.cameraX + this.ac;
            } else if (this.aH == 2) {
               this.positionX = this.av;
            }

            this.cV();
            if (this.aA <= 0) {
               this.p(102);
               this.positionY = Game.viewBounds[1] + this.collisionBounds[1] - this.collisionBounds[3];
               this.H = 1536;
               if (this.aH == 4) {
                  if (this.ay == 0) {
                     int var3 = Game.i(0, 3);
                     dQ[var3] = Game.player.positionX;
                     if (var3 == 0) {
                        var3 = Game.i(0, 2);
                        dQ[1] = dQ[0] + (var3 == 0 ? -100 : 100);
                        dQ[2] = dQ[0] + (var3 == 0 ? 100 : -100);
                     } else if (var3 == 1) {
                        var3 = Game.i(0, 2);
                        dQ[0] = dQ[1] + (var3 == 0 ? -100 : 100);
                        dQ[2] = dQ[1] + (var3 == 0 ? 100 : -100);
                     } else if (var3 == 2) {
                        var3 = Game.i(0, 2);
                        dQ[0] = dQ[2] + (var3 == 0 ? -100 : 100);
                        dQ[1] = dQ[2] + (var3 == 0 ? 100 : -100);
                     }
                  }

                  this.positionX = dQ[this.ay];
               }
            }
            break;
         case 102:
            this.J = 320;
            if (this.H > 1536) {
               this.H = 1536;
            }

            if (E != null && E.type == 100) {
               if (this.collisionBounds[1] > Game.aJ.collisionBounds[1]) {
                  this.b(false, true);
               }
            } else {
               this.b(false, true);
            }

            if (this.az == 1) {
               if (this.H > 0 && (this.eD >= 15 || this.eL || this.eM)) {
                  this.p(103);
               }
            } else if (this.cI) {
               this.db();
               this.p(108);
            }
            break;
         case 103:
            Game.e(this);
            return;
         case 104:
         case 105:
            if (!this.cI) {
               this.b(true, true);
               if (this.bL) {
                  this.db();
                  this.cV();
                  if (this.aA <= 0) {
                     this.G = this.facingLeft ? -this.ax : this.ax;
                  }

                  this.bi();
               } else {
                  this.G = 0;
                  this.J = 320;
                  this.aA = 100;
               }
            }

            if (this.aB < 0) {
               if (this.eL) {
                  this.facingLeft = false;
               }

               if (this.eM) {
                  this.facingLeft = true;
               }

               if (!b(Game.aJ.collisionBounds, this.collisionBounds)) {
                  Game.e(this.v);
                  this.v = null;
                  Game.e(this);
                  return;
               }
            }

            if (this.T == null) {
               if (ba != null
                  && ba[0] != null
                  && 2 != Game.be[Game.levelIndex]
                  && ba[0].animationId == 19
                  && b(ba[0].collisionBounds, this.collisionBounds)
                  && this.H > 0
                  && this.collisionBounds[3] - (this.H >> 8) < ba[0].collisionBounds[1]
                  && this.positionX >= ba[0].collisionBounds[0]
                  && this.positionX <= ba[0].collisionBounds[2]) {
                  this.T = ba[0];
                  this.positionY = this.T.collisionBounds[1] + (this.collisionBounds[1] - this.collisionBounds[3] >> 1);
                  this.H = 0;
                  this.J = 0;
                  this.G = this.facingLeft ? -this.ax : this.ax;
                  ba[0].s = this;
               }
            } else {
               if (ba != null && ba[0] != null && ba[0].animationId != 19 && ba[0].s == this) {
                  this.T.s = null;
                  this.T = null;
               }

               if (this.T != null && this.T.type == 39) {
                  this.positionY = this.T.collisionBounds[1] + (this.collisionBounds[1] - this.collisionBounds[3] >> 1);
                  this.H = 0;
                  this.J = 0;
                  this.G = this.facingLeft ? -this.ax : this.ax;
                  if ((ba == null || ba[0] == null || ba[0].animationId != 19 || !b(ba[0].collisionBounds, this.collisionBounds)) && this.T == ba[0]) {
                     this.T.s = null;
                     this.T = null;
                  }
               }
            }

            this.cW();
            break;
         case 107:
            this.cV();
            if (this.cI && this.cJ != null) {
               this.cC();
            }

            if (b(Game.player.collisionBounds, this.m)) {
               this.p(108);
               this.G = this.ax;
            }
            break;
         case 108:
            this.cV();
            if (this.cI && this.cJ != null) {
               this.cC();
               if (this.eL || this.eM) {
                  this.G = -this.G;
               }
            } else {
               this.p(102);
            }
      }

      if (this.az == 1 && this.cI && this.cJ != null) {
         if (!this.fx) {
            this.a(-53, 18, 1, 0, 0, 308, false);
            aU.positionY = this.cJ.collisionBounds[1];
            aU.aL = 1;
            this.fx = true;
         }

         if (this.collisionBounds[1] > this.cJ.collisionBounds[1]) {
            Game.e(this.v);
            this.v = null;
            Game.e(this);
            return;
         }
      }

      if (this.v != null) {
         this.a(this.v, 0, 0);
         if (this.cI && this.cJ != null) {
            this.v.aF = this.cJ.collisionBounds[1];
         } else {
            this.v.aF = Game.aj;
         }
      }

      this.g();
      if (this.aq != 100 && this.aq != 101 && (this.c & 128) == 0) {
         if (this.az == 1 && b(Game.player.collisionBounds, this.collisionBounds)) {
            if (this.aH == 4 || this.aH == 2) {
               Game.player.a(4, 20, 0, this);
               this.p(103);
               Game.aO = 0;
               Game.aN = 0;
               return;
            }

            if (E != null && E.type == 100) {
               Game.player.a(4, E.B(3), 0, this);
            } else {
               Game.player.a(4, 500, 0, this);
            }
         }

         this.cD();
      }

      this.aA();
   }

   private void cC() {
      int var1;
      var1 = (var1 = -this.aA / 500 % 4) < 2 ? var1 : 4 - var1;
      this.positionY = this.cJ.collisionBounds[1] + 0 + --var1;
      this.b(true, false);
      this.d(0, this.collisionBounds);
   }

   private void cD() {
      for (int var1 = 0; var1 < Game.ck; var1++) {
         Actor var2;
         if ((var2 = Game.cj[var1]) != null && var2 != this && (var2.c & 1024) == 0 && var2.aq != 7 && var2.aq != 3 && var2.aq != 5 && var2.aq != 6) {
            if (this.az == 1 && (var2.M() || var2.type == 70) && var2.collisionBounds != null && b(var2.collisionBounds, this.collisionBounds)) {
               var2.s(20);
            } else if (this.az == 1 && var2.type == 75 && var2.collisionBounds != null && b(var2.collisionBounds, this.collisionBounds)) {
               if (var2.aq == 0) {
                  var2.aH = 5;
                  var2.cP();
               }
            } else if (this.az == 1
               && var2.type == 73
               && (this.aq == 105 || this.aq == 104)
               && (var2.aq == 105 || var2.aq == 104)
               && b(var2.collisionBounds, this.collisionBounds)) {
               this.p(103);
               var2.p(103);
            } else if (this.az == 0
               && var2.type == 73
               && (this.aq == 107 || this.aq == 108)
               && (var2.aq == 107 || var2.aq == 108)
               && b(var2.collisionBounds, this.collisionBounds)) {
               this.G = -this.G;
               var2.G = -var2.G;
            } else if (this.az == 1 && var2.type == 79) {
               dV[1] = this.collisionBounds[1] - 10;
               dV[3] = this.collisionBounds[3] + 10;
               dV[0] = this.collisionBounds[0] - 10;
               dV[2] = this.collisionBounds[2] + 10;
               if (b(var2.collisionBounds, dV)) {
                  var2.a(dV, false, 79);
               }
            }
         }
      }
   }

   private void cE() {
      switch (this.aq) {
         case 0:
            if (this.aH == 5 || this.aH == 4 || this.aH == 2) {
               this.p(100);
               this.aw = 0;
               return;
            }

            if (b(Game.player.collisionBounds, this.m)) {
               this.p(100);
               this.aw = 0;
               return;
            }
            break;
         case 100:
            if (this.aH == 4) {
               this.s(this.aK, this.aw);
               this.aw++;
               if (this.aw >= 3) {
                  this.aA = this.aB;
                  this.p(101);
                  return;
               }

               this.aA = 400;
               this.p(102);
               return;
            }

            this.A(this.aK);
            this.aA = this.aB;
            this.p(101);
            return;
         case 101:
            this.cV();
            if (this.aB > 0 && this.aA <= 0) {
               this.p(0);
            }
            break;
         case 102:
            this.cV();
            if (this.aA <= 0) {
               this.p(100);
               return;
            }
      }
   }

   private void s(int var1, int var2) {
      Actor var3;
      (var3 = this.A(var1)).ay = var2;
      var3.av = var2 * 500;
   }

   private Actor A(int var1) {
      Actor var2 = this.a(73, 44, 0, 0, 0, 100, false);
      if (this.aH == 1) {
         var2.az = 0;
         var2.aL = 0;
      } else {
         var2.az = 1;
         var2.aL = 1;
      }

      var2.aH = this.aH;
      var2.ax = this.ax;
      var2.c |= 16;
      if (var1 == 4) {
         if (this.aH == 1) {
            var2.positionY = Game.viewBounds[1] + this.collisionBounds[1] - this.collisionBounds[3];
            var2.N = true;
            var2.p(102);
         } else if (this.aH == 5 || this.aH == 4 || this.aH == 2) {
            var2.av = this.av;
            var2.positionX = var2.positionX + Game.cameraX * (Game.cC - 13) / (Game.cy - 13) + Game.cameraX;
            var2.positionY = var2.positionY + Game.cameraY * (Game.cE - 17) / (Game.cz - 17) + Game.cameraY;
            var2.ac = var2.positionX - Game.cameraX;
            var2.ad = var2.positionY - Game.cameraY;
            var2.p(100);
         }
      } else {
         var2.facingLeft = var1 == 2;
         var2.aC = this.aC;
         var2.p(104);
      }

      aU = null;
      return var2;
   }

   public final void V() {
      Actor var1 = Game.i(this.aw);
      this.cV();
      switch (this.aq) {
         case 0:
            if (this.e() && (this.animationId == 27 || this.animationId == 30)) {
               if (this.aH == 0) {
                  this.a(25);
               } else {
                  this.a(28);
               }
            }

            if (this.a(0, this.collisionBounds)) {
               this.p(101);
               this.aA = this.ay;
               return;
            }
            break;
         case 101:
            if (this.a(0, this.collisionBounds)) {
               if (this.aH == 0) {
                  this.a(26);
               } else {
                  this.a(29);
               }

               this.aA = this.ay;
               return;
            }

            if (this.ay == -1) {
               return;
            }

            if (this.aC != 0 && (this.aC != 1 || var1.aq != 102 && var1.aq != 105)) {
               this.aq = 0;
               if (this.aH == 0) {
                  this.a(27);
                  return;
               }

               this.a(30);
               return;
            }

            if (this.aA <= 0) {
               this.p(102);
               return;
            }

            if (this.aA <= 1000) {
               if (this.aH == 0) {
                  this.a(27);
                  return;
               }

               this.a(30);
               return;
            }

            if (this.e()) {
               if (this.aH == 0) {
                  this.a(26);
                  return;
               }

               this.a(29);
               return;
            }
            break;
         case 102:
            if (this.e()) {
               if (this.aH == 0) {
                  this.a(25);
               } else {
                  this.a(28);
               }

               this.aq = 0;
            }
      }
   }

   public static final boolean W() {
      for (int var0 = 0; var0 < 3; var0++) {
         if (dU[var0] != null && dU[var0].positionY - Game.cameraY > 200) {
            return true;
         }
      }

      return false;
   }

   public static final void p(Actor var0) {
      if (var0 != null && var0 != aT) {
         if (Game.player.positionX >= var0.collisionBounds[0] && Game.player.positionX <= var0.collisionBounds[2]) {
            aT = var0;
         }
      }
   }

   public final void X() {
      if (this.aq == 101 || this.aq == 0) {
         this.Z();
      }
   }

   public final void f(int var1, int var2) {
      this.aq = var1;
      this.H = var2;

      for (int var3 = 0; var3 < 3; var3++) {
         if (dU[var3] != this && dU[var3] != null) {
            dU[var3].aq = var1;
            dU[var3].positionY = this.positionY;
            dU[var3].H = var2;
         }
      }
   }

   public final void j(int var1) {
      this.aq = var1;

      for (int var2 = 0; var2 < 3; var2++) {
         if (dU[var2] != this && dU[var2] != null) {
            this.positionY = dU[var2].positionY;
            this.H = dU[var2].H;
            return;
         }
      }
   }

   public final void Y() {
      boolean var1 = false;

      for (int var2 = 0; var2 < 3; var2++) {
         if (dU[var2] != null && this.positionY + 1 >= dU[var2].positionY && this.positionY - 5 < dU[var2].positionY) {
            var1 = true;
            break;
         }
      }

      if (var1) {
         switch (this.aA) {
            case 4:
               this.aA = 1;
               dU[0] = this;
               this.j(101);
               break;
            case 5:
               this.aA = 2;
               dU[2] = this;
               this.j(101);
               break;
            case 6:
               this.aA = 3;
               dU[1] = this;
               this.j(101);
               break;
            case 7:
               if (dU[0] != null) {
                  dU[0].aq = 103;
                  if (aT == dU[0]) {
                     aT = null;
                  }

                  dU[0] = null;
               }
               break;
            case 8:
               if (dU[2] != null) {
                  dU[2].aq = 103;
                  if (aT == dU[2]) {
                     aT = null;
                  }

                  dU[2] = null;
               }
               break;
            case 9:
               if (dU[1] != null) {
                  dU[1].aq = 103;
                  if (aT == dU[2]) {
                     aT = null;
                  }

                  dU[1] = null;
               }
         }

         if (this.aA >= 7) {
            this.aq = 104;
         }
      }
   }

   public final void Z() {
      this.g();

      for (int var2 = 0; var2 < Game.ck; var2++) {
         Actor var1;
         if ((var1 = Game.cj[var2]) != this && var1 != null && var1 != dU[0] && var1 != dU[2] && var1 != dU[1]) {
            var1.g();
            if (var1.T == this) {
               var1.positionY = this.collisionBounds[1] - 1;
               if (var1.type == 73) {
                  var1.positionY = this.collisionBounds[1] - 1 + (var1.collisionBounds[1] - var1.collisionBounds[3] >> 1);
                  var1.H = 0;
                  var1.J = 0;
                  var1.G = var1.facingLeft ? -var1.ax : var1.ax;
               }

               var1.bL = true;
               var1.g();
               dV[1] = var1.collisionBounds[1];
               dV[0] = var1.collisionBounds[0];
               dV[3] = var1.collisionBounds[3] + 1;
               dV[2] = var1.collisionBounds[2];
               dR += 64;
               dT++;
               this.ac();
               if (!b(ey, dV)) {
                  var1.T = null;
               } else if (dU[2] != null && this.c(this)) {
                  for (int var3 = 0; var3 < 3; var3++) {
                     if (dU[var3] != null && b(dU[var3].collisionBounds, dV)) {
                        var1.T = dU[var3];
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   public final void aa() {
      for (int var1 = 0; var1 < 3; var1++) {
         if (dU[var1] != null) {
            dU[var1].H = 0;
         }
      }
   }

   public final void ab() {
      int var1 = this.positionY;
      this.positionY = this.b + this.H >> 8;
      this.b(false, true);
      if (this.eC > 15) {
         this.aa();
         this.f(101, 0);
         dR = 0;
      } else {
         if (Game.player.eC > 15) {
            this.aa();
            this.f(101, 0);
            dR = 0;
            if (this == aT) {
               Game.player.H = 0;
               return;
            }
         } else {
            this.positionY = var1;
         }
      }
   }

   public final void ac() {
      this.g();

      for (int var1 = 0; var1 < 4; var1++) {
         ey[var1] = this.collisionBounds[var1];
      }

      if (dU[2] != null && this.c(this)) {
         if (dU[0] != null) {
            ey[0] = dU[0].collisionBounds[0];
         } else {
            ey[0] = dU[2].collisionBounds[0];
         }

         if (dU[1] != null) {
            ey[2] = dU[1].collisionBounds[2];
            return;
         }

         ey[2] = dU[2].collisionBounds[2];
      }
   }

   public final void ad() {
      if (this.aA < 7) {
         this.ac();
         this.d(1, ey);
      }
   }

   public final void ae() {
      this.ad();
      switch (this.aq) {
         case 0:
            this.a(22);
            if (this.aA == 2) {
               if (b(Game.player.collisionBounds, this.m)) {
                  this.f(101, -512);
                  return;
               }
            } else if (this.aA > 3) {
               this.Y();
               return;
            }
            break;
         case 101:
            this.H = dR;
            this.ab();
            if (this.H > -512) {
               this.a(23);
               return;
            }

            if (this.H == 0) {
               this.a(24);
               return;
            }

            this.a(21);
            return;
         case 103:
            this.a(22);
            this.H = 0;
            if (this.positionY >= Game.cameraY + 320) {
               this.aq = 104;
               return;
            }
            break;
         case 104:
            Game.e(this);
      }
   }

   public final void af() {
      this.d(0, this.collisionBounds);
      if (this.aB < 8) {
         switch (this.aq) {
            case 0:
               this.aA = this.aA - Game.h;
               if (this.aA <= 0) {
                  this.aq = 105;
                  if (this.aB < 2) {
                     this.G = this.aB * -2 * this.ay + this.ay;
                  } else if (this.aB < 4) {
                     this.H = (3 - this.aB) * -2 * this.ay + this.ay;
                  } else if (this.aB < 6) {
                     this.G = (this.aB - 4) * -2 * this.ay + this.ay;
                     this.H = (this.ay * (this.m[2] - this.m[0]) << 8) / (this.m[3] - this.m[1]);
                     this.H = (this.aB - 4) * -2 * this.H + this.H;
                  } else {
                     this.G = (7 - this.aB) * -2 * this.ay + this.ay;
                     this.H = this.ay * (this.m[2] - this.m[0]) / (this.m[3] - this.m[1]);
                     this.H = (7 - this.aB) * -2 * this.H + this.H;
                  }
               }
               break;
            case 105:
               boolean var1 = false;
               if ((this.aB != 0 || this.collisionBounds[2] < this.m[2])
                  && (this.aB != 1 || this.collisionBounds[0] > this.m[0])
                  && (this.aB != 2 || this.collisionBounds[1] > this.m[1])
                  && (this.aB != 3 || this.collisionBounds[3] < this.m[3])) {
                  if (this.aB > 3
                     && (
                        this.aB == 4 && (this.collisionBounds[2] >= this.m[2] || this.collisionBounds[3] >= this.m[3])
                           || this.aB == 5 && (this.collisionBounds[0] <= this.m[0] || this.collisionBounds[1] <= this.m[1])
                           || this.aB == 6 && (this.collisionBounds[2] >= this.m[2] || this.collisionBounds[1] <= this.m[1])
                           || this.aB == 7 && (this.collisionBounds[0] <= this.m[0] || this.collisionBounds[3] >= this.m[3])
                     )) {
                     var1 = true;
                  }
               } else {
                  var1 = true;
               }

               if (var1) {
                  this.aq = 0;
                  this.G = this.H = 0;
                  this.aA = this.aw;
                  if (this.aB < 2) {
                     this.aB = 1 - this.aB;
                  } else if (this.aB < 4) {
                     this.aB = 2 - this.aB + 3;
                  } else if (this.aB < 6) {
                     this.aB = 5 - this.aB + 4;
                  } else {
                     this.aB = 7 - this.aB + 6;
                  }
               }
         }
      } else {
         this.b(true, true);
         this.cV();
         if (aT == null) {
            this.a(9);
            int var2 = this.collisionBounds[3] - this.collisionBounds[1];
            if (Game.player.positionY >= this.m[1] + var2 && Game.player.positionY <= this.m[3] - var2) {
               this.H = Game.player.positionY - this.positionY + var2 + 10 << 4;
            } else if (Game.player.positionY < this.m[1] + var2) {
               this.G = 0;
               if (this.positionY > this.m[1] + var2) {
                  this.H = -512;
               } else {
                  this.H = 0;
               }
            } else if (Game.player.positionY > this.m[3] - var2) {
               this.G = 0;
               if (this.positionY < this.m[3]) {
                  this.H = 512;
               } else {
                  this.H = 0;
               }
            }
         } else if (aT == this) {
            switch (this.aq) {
               case 0:
                  if (aT == this) {
                     this.aq = 102;
                  }
                  break;
               case 102:
                  this.J = -256;
                  if (this.H <= -this.ay) {
                     this.H = -this.ay;
                  }

                  if (Game.player.animationId == 13 || Game.player.animationId == 23 || Game.player.animationId == 31) {
                     this.aq = 104;
                  }
                  break;
               case 104:
                  this.J = 256;
                  if (this.H >= this.ay) {
                     this.H = this.ay;
                  }

                  if (Game.player.animationId != 13 && Game.player.animationId != 23 && Game.player.animationId != 31) {
                     this.aq = 102;
                  }
            }
         }
      }

      if (this.aB >= 8 && (this.G != 0 || this.H != 0)) {
         if (this.collisionBounds[1] <= this.m[1] && this.H < 0) {
            this.H = 0;
         } else if (this.collisionBounds[3] >= this.m[3] && this.H > 0) {
            this.H = 0;
         }

         if (aT == this) {
            if (Game.player.positionX <= this.positionX - 10) {
               this.G = -512;
               this.a(11);
            } else if (Game.player.positionX >= this.positionX + 10) {
               this.G = 512;
               this.a(12);
            } else {
               this.G = 0;
               this.a(10);
            }
         }

         if ((this.collisionBounds[2] > this.m[2] || this.eM) && this.G > 0) {
            this.G = 0;
         } else if ((this.collisionBounds[0] < this.m[0] || this.eL) && this.G < 0) {
            this.G = 0;
         }

         if (this.animationId == 11 || this.animationId == 12) {
            this.H >>= 1;
         }
      }

      if (this.animationId == 0) {
         if (b(Game.player.collisionBounds, this.collisionBounds)) {
            Game.player.a(4, 15, 0, this);
         }

         if (this.r != null) {
            if (this.r.facingLeft) {
               this.r.positionX = this.collisionBounds[2];
            } else {
               this.r.positionX = this.collisionBounds[0];
            }

            this.r.positionY = this.positionY + this.av;
         }
      }

      this.d(0, this.collisionBounds);
   }

   private void cF() {
      if (Game.player.aq != 1) {
         this.d(0, this.collisionBounds);
      }

      this.g();
      if (aT == this) {
         if (this.av < 35) {
            this.av++;
         } else {
            this.av = 33;
            aT = null;
            cK = true;
            Game.player.cI = true;
         }

         if (Game.player.eC == 24) {
            Game.player.positionY += 60;
            Game.player.q(0);
            aT = null;
            cK = true;
            Game.player.cI = true;
            return;
         }

         if (Game.player.eD == 24) {
            return;
         }
      } else {
         if (this.aC == 1) {
            if (this.av > 0) {
               this.av -= 3;
            } else {
               this.av = 0;
            }
         }

         this.G = 0;
         this.a(2);
      }
   }

   private void d(int var1, int[] var2) {
      this.g();
      if (this.collisionBounds != null) {
         if (aT == null) {
            int var3 = Game.player.positionY + (Game.player.H >> 8);
            if (Game.player.H >= 0
               && Game.player.positionY <= var2[1] + var1 + (this.type == 65 ? 0 : 5)
               && Game.player.positionX >= var2[0]
               && Game.player.positionX <= var2[2]
               && (var3 >= var2[1] + (this.H >> 8) || var3 >= var2[1])) {
               if (this.type == 64) {
                  this.a(1);
                  eT = false;
                  bQ = false;
                  if (Game.player.aq != 1) {
                     Game.player.a(6);
                  }

                  Game.player.G = 0;
                  Game.player.H = -5120;
                  return;
               }

               aT = this;
               if (this.type == 82 && this.aA <= 3) {
                  for (int var4 = 0; var4 < 3; var4++) {
                     p(dU[var4]);
                  }
               }

               if (Game.player.aq != 1) {
                  Game.player.a(9);
               }

               Game.player.positionY = var2[1] + 1;
               if (this.type == 50) {
                  this.n[11] = 0;
                  return;
               }
            }
         } else if (aT == this) {
            if (this.type == 82 && this.aA <= 3) {
               for (int var5 = 0; var5 < 3; var5++) {
                  p(dU[var5]);
               }
            }

            Game.player.g();
            if (Game.player.animationId != 7 && !b(Game.player.collisionBounds, var2) && Game.player.H - this.H < 0
               || Game.player.positionX > var2[2]
               || Game.player.positionX < var2[0]) {
               aT = null;
               Game.player.G = 0;
               Game.player.H = 0;
               return;
            }

            if (aT.type == 33) {
               if (aT.animationId != 12 && aT.animationId != 11) {
                  Game.player.positionY = var2[1] + 1;
                  return;
               }

               Game.player.positionY = var2[1] + Math.abs(Game.player.positionX - aT.positionX) / 3;
               return;
            }

            if (aT.type == 50) {
               Game.player.positionY = var2[1];
               return;
            }

            if (aT.type == 65) {
               Game.player.positionY = var2[1];
               return;
            }

            if (aT.type == 39) {
               Game.player.positionY = var2[1] + 1;
               if (this.s != null) {
                  Game.player.G = Game.player.G + this.s.G;
                  return;
               }
            } else {
               if (aT.type == 82) {
                  aT.ab();
                  Game.player.H = aT.H;
                  Game.player.positionY = var2[1] + var1;
                  return;
               }

               Game.player.H = this.H;
               Game.player.positionY = var2[1] + var1;
            }
         }
      }
   }

   private void cG() {
      this.g();
      this.d(0, this.collisionBounds);
      switch (this.animationId) {
         case 0:
            return;
         case 1:
            if (this.e()) {
               this.a(0);
            }
      }
   }

   private void cH() {
      if (this.aq != 103 && this.aq != 104) {
         this.d(1, this.collisionBounds);
      }

      switch (this.aq) {
         case 0:
            if (aT == this && this.n[11] != 1) {
               this.aq = 101;
               this.n[10] = 2000;
            }

            this.ah();
            return;
         case 101:
            this.n[10] = this.n[10] - Game.h;
            this.aj();
            int var1 = Math.abs(this.av);
            int var2 = (this.az << 8) + var1 * Game.l(this.aA >> 16) >> 8;
            int var3 = (this.aw << 8) + var1 * Game.m(this.aA >> 16) >> 8;
            this.H = var2 - this.positionY << 8;
            this.G = var3 - this.positionX << 8;
            if (aT != this && this.aA == 0 && this.aC != 1) {
               this.ag();
            }

            if (this.n[10] < 0 && this.aA == 0 && this.aC != 1) {
               this.ag();
            }

            this.ah();
            return;
         case 102:
            this.J = 320;
            if (this.H >= 4096) {
               this.H = 4096;
               this.J = 0;
            }

            if (this.aA != 0) {
               this.aj();
            }

            for (int var4 = 0; var4 < Game.ck; var4++) {
               Actor var5;
               if ((var5 = Game.cj[var4]) != null && (var5.c & 1024) == 0) {
                  if (var5.M() && b(var5.collisionBounds, this.collisionBounds) && this.positionY < var5.positionY - 25) {
                     var5.s(100);
                     this.G = 0;
                     break;
                  }

                  if (var5.type == 45 && var5.aq == 0) {
                     int[] var6;
                     (var6 = new int[4])[0] = var5.collisionBounds[0];
                     var6[2] = var5.collisionBounds[2];
                     var6[1] = var5.collisionBounds[1] - 17;
                     var6[3] = var5.collisionBounds[3];
                     if (b(var6, this.collisionBounds)) {
                        var5.ay = this.positionX;
                        this.a(var5.collisionBounds, true);
                        var5.aq = 100;
                        break;
                     }
                  }
               }
            }

            this.b(false, true);
            if (this.bL) {
               this.aq = 103;
               this.b(13);
               Game.I(-1);
               this.J = 0;
               this.H = 0;
            }

            if (b(Game.player.collisionBounds, this.collisionBounds) && this.positionY < Game.player.positionY - 25) {
               Game.player.a(4, this.B(1), 0, this);
               return;
            }
            break;
         case 103:
            aT = null;
            this.J = 0;
            this.H = 0;
            if (this.e()) {
               this.aq = 104;
            }

            if (this.aA != 0) {
               this.aj();
               return;
            }
            break;
         case 104:
            this.positionY = this.az;
      }
   }

   public final void ag() {
      this.aq = 0;
      this.n[0] = this.n[1];
      this.n[2] = this.n[3];
      this.n[4] = this.n[5];
      this.positionX = this.aw;
      this.positionY = this.az - this.av;
      this.ay = 1;
      this.aA = 0;
      this.ax = this.n[7];
      this.aB = this.n[6];
      this.n[11] = 1;
      this.db();
   }

   public final void k(int var1) {
      switch (var1) {
         case 0:
            ey[0] = this.positionX - 3;
            ey[2] = this.positionX + 3;
            ey[1] = this.az;
            ey[3] = this.positionY;
            return;
         case 1:
            ey[0] = this.collisionBounds[0];
            ey[2] = this.collisionBounds[2];
            ey[1] = this.collisionBounds[1] - 3;
            ey[3] = this.collisionBounds[3] - 3;
      }
   }

   public final void ah() {
      this.k(0);
      if (this.aC == 0 && this.f(ey)) {
         this.aq = 102;
      }
   }

   public final void ai() {
      if (this.aG != -2) {
         if (this.aG != -1 && this.r == null) {
            this.b(Game.i(this.aG));
         }

         switch (this.aq) {
            case 0:
               this.k(0);
               if (b(Game.player.k, ey)) {
                  this.aq = 101;
                  return;
               }
               break;
            case 101:
               this.J = 320;
               if (this.r != null && b(this.r.collisionBounds, this.collisionBounds)) {
                  this.aq = 102;
                  if (this.r.type == 38) {
                     this.r.aq = 100;
                     this.r.a(2);
                     this.r.aA = 3500;
                     if (this.r.aG != -1) {
                        Game.a(this.r.r.aB, true);
                     }

                     this.r.al();
                     this.r.c &= -257;
                     return;
                  }

                  if (this.r.type == 9) {
                     this.aq = 102;
                     this.r.p(112);
                     return;
                  }
               }
               break;
            case 102:
               this.J = 0;
               this.H = 0;
               this.positionY = this.az;
               this.b((Actor)null);
         }
      }
   }

   public final void aj() {
      int var1 = this.ax >= 0 ? this.ax : 0;
      if (this.aB == 1) {
         this.aA += var1 << 8;
      } else {
         this.aA -= var1 << 8;
      }

      if (this.aA == 0 && this.n[8] != 1) {
         this.ax = this.n[4];
         this.ay = -this.ay;
         this.n[9] = 1;
         this.n[8] = 1;
      }

      if (this.aA < this.n[0] << 16 && this.ax > 0) {
         if (this.aA <= -(this.n[0] << 16) || this.ax <= 0) {
            this.aB = -this.aB;
            this.ay = -this.ay;
            if (this.ax > 0) {
               this.n[9] = 1;
            }

            this.n[8] = 0;
         }
      } else {
         this.aB = -this.aB;
         this.ay = -this.ay;
         if (this.ax > 0) {
            this.n[9] = 1;
         }

         this.n[8] = 0;
      }

      if (this.n[9] != 1) {
         this.ax = this.ax - this.ay * this.n[2];
      } else {
         this.n[9] = 0;
      }
   }

   public final void ak() {
      switch (this.aq) {
         case 0:
            this.eX = Game.h(Game.player.a - this.a, Game.player.b - this.b);
            if (this.eX <= 40960) {
               this.aq = 101;
               return;
            }
            break;
         case 101:
            this.a(5);
            this.eX = Game.h(Game.player.a - this.a, Game.player.b - this.b);
            if (this.eX < 20480) {
               this.aq = 102;
               this.a(6);
               return;
            }

            if (this.eX > 40960) {
               this.aq = 0;
               this.a(4);
               return;
            }
            break;
         case 102:
            if (this.e()) {
               this.a(7);
               this.aq = 103;
               return;
            }

            if (this.h == 1 && this.aA == 1) {
               this.t(0, 0);
            }
         case 103:
      }
   }

   private void t(int var1, int var2) {
      this.a(1, 1, 0, var1, var2, 81, false);
      aU.facingLeft = this.facingLeft;
      aU.aH = this.ay;
      aU.aI = this.aB;
      aU.as = this.B(0);
      aU.aL = this.ay > 1 ? 1 : 0;
      aU.p(101);
      aU.ad = aU.positionY;
      aU.aJ = new int[4];
      aU.aG = -1;
      aU.O = true;
   }

   public final void al() {
      for (int var2 = 0; var2 < 20; var2 += 3) {
         if (Game.bO[var2 + 0] == this.an) {
            return;
         }
      }

      for (int var1 = 0; var1 < 20; var1 += 3) {
         if (Game.bO[var1 + 0] == 0) {
            Game.bO[var1 + 0] = this.an;
            Game.bO[var1 + 1] = this.positionX;
            Game.bO[var1 + 2] = this.positionY;
            return;
         }
      }
   }

   private void cI() {
      for (int var1 = 0; var1 < 20; var1 += 3) {
         if (Game.bO[var1 + 0] == this.an) {
            Game.bO[var1 + 0] = 0;
            return;
         }
      }
   }

   private boolean cJ() {
      for (int var1 = 0; var1 < 20; var1 += 3) {
         if (Game.bO[var1 + 0] != 0
            && Game.h(
                  Game.bO[var1 + 1] - (this.collisionBounds[0] + this.collisionBounds[2] >> 1),
                  Game.bO[var1 + 2] - (this.collisionBounds[1] + this.collisionBounds[3] >> 1)
               )
               < 75) {
            return true;
         }
      }

      return false;
   }

   public static final void am() {
      for (int var0 = 0; var0 < 20; var0 += 3) {
         Game.bO[var0] = 0;
      }
   }

   private void cK() {
      this.cV();
      this.cW();
      if (this.aG != -1 && this.r == null) {
         this.b(Game.i(this.aG));
         this.c |= 4096;
      }

      switch (this.aq) {
         case 0:
            if (this.b(0, this.collisionBounds)) {
               this.aq = 100;
               this.c |= 16;
               this.a(this.aC == 0 ? 2 : 18);
               this.aA = 3500;
               if (this.aG != -1) {
                  Game.a(this.r.aB, true);
               }

               this.al();
               return;
            }
            break;
         case 100:
            if (this.aA <= 0 && this.aH == 0) {
               this.a(this.aC == 0 ? 3 : 19);
               this.aq = 101;
               this.aA = 1500;
               return;
            }
            break;
         case 101:
            if (this.aA <= 0) {
               this.aq = 0;
               this.c &= -17;
               this.a(this.aC == 0 ? 1 : 17);
               this.aA = 3500;
               if (this.aG != -1) {
                  Game.a(this.r.aB, false);
               }

               this.cI();
            }
      }
   }

   private void cL() {
      int var1 = this.collisionBounds[3] - Game.player.positionY;
      int var2 = this.collisionBounds[3] - this.collisionBounds[1];
      int var3 = this.s.collisionBounds[3] - this.s.collisionBounds[1];
      int var4 = (var1 << 8) / var2;
      int var5 = var3 * var4 >> 8;
      Game.player.positionY = this.s.collisionBounds[3] - var5;
   }

   private void cM() {
      if (this.aG != -1 && this.s == null) {
         this.s = Game.i(this.aG);
      }

      if (b(Game.player.collisionBounds, this.collisionBounds)) {
         boolean var1 = false;
         switch (this.aA) {
            case 1:
               if (!Game.player.facingLeft || Game.player.G >= 0) {
                  break;
               }

               Game.player.positionX = this.s.positionX;

               while (b(Game.player.g(), this.s.collisionBounds)) {
                  Game.player.positionX--;
               }

               this.cL();
               var1 = true;
               break;
            case 2:
               if (!Game.player.facingLeft && Game.player.G > 0) {
                  Game.player.positionX = this.s.positionX;

                  while (b(Game.player.g(), this.s.collisionBounds)) {
                     Game.player.positionX++;
                  }

                  this.cL();
                  var1 = true;
               }
            case 3:
            case 5:
            case 6:
            case 7:
            default:
               break;
            case 4:
               if (Game.player.H >= 0) {
                  break;
               }

               Game.player.positionX = this.s.collisionBounds[0] + (Game.player.positionX - this.collisionBounds[0]);
               Game.player.positionY = this.s.collisionBounds[3];

               while (b(Game.player.g(), this.s.collisionBounds)) {
                  Game.player.positionY--;
               }

               var1 = true;
               break;
            case 8:
               if (Game.player.H > 0) {
                  Game.player.positionX = this.s.collisionBounds[0] + (Game.player.positionX - this.collisionBounds[0]);
                  Game.player.positionY = this.s.collisionBounds[3];

                  while (b(Game.player.g(), this.s.collisionBounds)) {
                     Game.player.positionY++;
                  }

                  var1 = true;
               }
         }

         if (var1) {
            Game.player.cI = false;
            Game.player.cJ = null;
            Game.ay = false;
            Game.aB = -1;
            if (!Game.ax) {
               Game.r();
            }
         }
      }
   }

   private void cN() {
      if (this.animationId == 1 && this.e()) {
         Game.e(this);
      }
   }

   private void cO() {
      if (this.animationId != 0 && this.animationId != 2) {
         if (this.aG != -1 && this.r == null) {
            this.b(Game.i(this.aG));
         }

         if (this.y != -1 && this.s == null) {
            this.s = Game.i(this.y);
         }

         if (b(Game.player.k, this.collisionBounds)) {
            this.a(Game.player.k, false, 61);
         } else if (3 == Game.be[Game.levelIndex]) {
            dV[1] = Game.player.collisionBounds[1] - 20;
            dV[3] = Game.player.collisionBounds[3] + 10;
            dV[0] = Game.player.collisionBounds[0];
            dV[2] = Game.player.collisionBounds[2] + 20;
            if (b(dV, this.collisionBounds) && this.a(dV, false, 61)) {
               Game.player.a(4, 5, 0, this);
            }
         } else if (2 == Game.be[Game.levelIndex]) {
            Game.player.g();
            dV[1] = Game.player.collisionBounds[1] - 3;
            dV[3] = Game.player.collisionBounds[3];
            dV[0] = Game.player.collisionBounds[0];
            dV[2] = Game.player.collisionBounds[2];
            if (c(dV, this.collisionBounds) && this.a(dV, false, 61)) {
               Game.player.a(4, 5, 0, this);
            }
         }

         if (this.r != null) {
            if (this.a(this.r.collisionBounds, true, 61)) {
               this.r.ft = true;
            } else {
               this.r.ft = false;
               this.r.p(103);
               this.b((Actor)null);
               this.aG = -1;
            }
         }

         if (this.s != null) {
            if (this.a(this.s.collisionBounds, true, 61)) {
               this.s.ft = true;
            } else {
               this.s.ft = false;
               this.s = null;
               this.y = -1;
            }
         }

         boolean var1 = true;

         for (int var2 = 0; var2 < this.n.length; var2++) {
            if (this.n[var2] == 0) {
               var1 = false;
            }
         }

         if (var1) {
            Game.e(this);
         }

         if ((this.animationId == 9 || this.animationId == 11) && this.e()) {
            this.a(this.animationId + 1);
            return;
         }
      } else if (this.e()) {
         Game.e(this);
      }
   }

   private void cP() {
      if (this.animationId != 52 && this.animationId != 50) {
         if (this.animationId == 53) {
            this.aq = 102;
            this.a(49);
         }
      } else {
         this.aq = 103;
         this.a(54);
         this.facingLeft = Game.player.facingLeft;
      }
   }

   private void cQ() {
      switch (this.aq) {
         case 0:
            if (b(Game.player.k, this.collisionBounds)) {
               this.cP();
               return;
            }

            if (b(Game.player.collisionBounds, this.collisionBounds)) {
               Game.player.a(4, 10, 0, this);
               this.cP();
               return;
            }

            if (this.aH == 1) {
               if (l(Game.player.positionX - this.positionX) < 100 && Game.player.positionY > this.positionY) {
                  this.aq = 109;
                  this.aA = 500;
                  return;
               }
            } else if (this.aH == 2) {
               for (int var1 = 0; var1 < Game.ck; var1++) {
                  Actor var2;
                  if ((var2 = Game.cj[var1]) != null
                     && (var2.type == 83 || var2.type == 84 || var2.type == 85)
                     && b(var2.collisionBounds, this.collisionBounds)) {
                     this.aq = 109;
                  }
               }
            }
            break;
         case 102:
            this.a(39, 17, 55, 0, 0, 302, false);
            if (this.aH == 2) {
               aU.aH = 2;
            }

            if (this.aH == 4) {
               this.aq = 110;
            } else {
               this.aq = 106;
               this.aA = 2200;
            }

            this.a(49);
            return;
         case 103:
            if (this.e()) {
               this.a(48);
               if (this.aH != 3 && this.aH != 5) {
                  this.aq = 106;
                  this.aA = 2200;
                  return;
               }
            }
            break;
         case 104:
            if (this.e()) {
               this.aq = 0;
               if (this.animationId == 50) {
                  this.a(52);
               } else {
                  this.a(53);
               }

               this.facingLeft = false;
               return;
            }
            break;
         case 106:
            this.cV();
            if (this.aA <= 0 && this.aH != 2) {
               this.aq = 104;
               if (this.animationId == 48) {
                  this.a(50);
                  return;
               }

               this.a(51);
            }
            break;
         case 109:
            this.cV();
            if (this.aA <= 0) {
               this.aq = 102;
               this.a(49);
               return;
            }
      }
   }

   private boolean a(int[] var1, boolean var2, int var3) {
      if (this.n == null) {
         return false;
      }

      boolean var4 = false;

      for (int var5 = 0; var5 < this.n.length; var5++) {
         int var6 = var5 / this.aw;
         int var7 = var5 % this.aw;
         ey[0] = this.collisionBounds[0] + 20 * var7;
         ey[1] = this.collisionBounds[1] + 20 * var6;
         ey[2] = ey[0] + 20;
         ey[3] = ey[1] + 20;
         if (!var2) {
            this.a(ey, true);
            if (this.n[var5] == 0 && b(var1, ey)) {
               this.n[var5] = 1;
               if (var3 == 61) {
                  var4 = true;
               }

               if (var3 == 61) {
                  this.a(61, 38, 0, ey[0] - this.positionX + Game.i(-10, 10), ey[1] - this.positionY + Game.i(-10, 10), 302, false);
               } else {
                  this.a(79, 45, 1, ey[0] - this.positionX + Game.i(-10, 10), ey[1] - this.positionY + Game.i(-10, 10), 302, false);
               }
            }

            if (this.n[var5] == 0) {
               this.a(ey, false);
            }
         } else if (b(var1, ey) && this.n[var5] == 0 && var3 == 61) {
            var4 = true;
            break;
         }
      }

      return var4;
   }

   private void cR() {
      switch (this.aq) {
         case 100:
            if (this.animationId != 1) {
               this.a(1);
               return;
            } else if (this.e()) {
               this.aq = 101;
               this.a(2);
            }
      }
   }

   private void cS() {
      switch (this.aq) {
         case 100:
            if (this.e()) {
               this.p(101);
            }

            this.aA = this.aA + Game.h;
            if (this.aA > this.aB) {
               this.aB += 300;
               this.c(32, this.collisionBounds);
               return;
            }
            break;
         case 101:
            if (this.e()) {
               this.p(102);
            }
      }
   }

   private void cT() {
      this.aL = 0;
      if (Game.levelIndex >= 7 && Game.levelIndex <= 9) {
         if (Game.bo == -2) {
            this.aL = 1;
         } else {
            this.aL = 2;
         }
      }

      switch (this.aq) {
         case 100:
            if (this.aC == 1) {
               if (this.animationId != 3) {
                  this.a(3);
               }
            } else if (this.animationId != 0) {
               this.a(0);
            }

            if (this.e()) {
               this.aA = 500;
               this.aq = 101;
               this.a(this.aC == 1 ? 4 : 1);
               return;
            }
            break;
         case 101:
            this.cV();
            if (this.aA <= 0) {
               this.aq = 102;
               this.a(this.aC == 1 ? 5 : 2);
               Game.e(6);
               return;
            }
            break;
         case 102:
            if (this.e()) {
               this.aq = 103;
               return;
            }
            break;
         case 103:
            Game.e(this);
      }
   }

   private void cU() {
      this.cV();
      this.cW();
      if (this.animationId != 1 && this.animationId != 3 && this.animationId != 5) {
         switch (this.aq) {
            case 0:
               if (this.animationId == 0 && this.f(this.collisionBounds)) {
                  this.aD();
                  this.a(2);
                  this.aq = 103;
                  this.a(58, 26, 1, 0, 0, 300, false);
                  aU.facingLeft = Game.player.facingLeft;
                  this.aA = 200;
                  return;
               }
               break;
            case 103:
               if (this.f(this.collisionBounds) && this.aA < 0) {
                  this.aD();
                  if (this.animationId == 2) {
                     this.a(4);
                     this.a(58, 26, 3, 0, 0, 300, false);
                     aU.facingLeft = Game.player.facingLeft;
                     this.aA = 200;
                     return;
                  }

                  if (this.animationId == 4) {
                     this.a(58, 26, 5, 0, 0, 300, false);
                     aU.facingLeft = Game.player.facingLeft;
                     this.aA = 200;

                     for (int var1 = 0; var1 < 5; var1++) {
                        this.a(35, 31, 5, Game.i(-20, 20), Game.i(-40, -80), 308, true);
                     }

                     Game.e(this);
                  }
               }
         }
      } else {
         if (this.e()) {
            Game.e(this);
         }
      }
   }

   public static final void a(int var0, int var1, int var2, int var3, int[] var4) {
      var4[0] = var2;
      var4[1] = var3;
      int var6 = var0 / 20;
      int var7 = var1 / 20;
      int var5;
      if ((var5 = Game.g(var6, var7)) >= 24 && var5 != 15) {
         var4[0] = var0;
         var4[1] = var1;
      } else {
         boolean var8 = var0 > var2;
         boolean var9 = var1 > var3;
         int var11 = 0;
         Actor var12 = null;
         Actor var13 = null;

         for (int var14 = 0; var14 < Game.ck; var14++) {
            Actor var15;
            if ((var15 = Game.cj[var14]) != null && (var15.c & 1024) == 0 && var15.type == 14 && var15.animationId == 2) {
               if (var12 != null) {
                  var13 = var15;
                  break;
               }

               var12 = var15;
            }
         }

         do {
            boolean var10 = false;
            if (var8) {
               if (a(var0, var1, var2, var3, var6 * 20, var7 * 20, var6 * 20, (var7 + 1) * 20, var4)) {
                  var10 = true;
                  var5 = Game.g(--var6, var7);
               }

               if (var12 != null && var12.collisionBounds[2] <= (var10 ? var6 + 1 : var6) * 20 && var12.collisionBounds[2] >= (var10 ? var6 : var6 - 1) * 20) {
                  if (a(var0, var1, var2, var3, var12.collisionBounds[2], var12.collisionBounds[1], var12.collisionBounds[2], var12.collisionBounds[3], var4)) {
                     var10 = true;
                     var5 = 24;
                  }
               } else if (var13 != null
                  && var13.collisionBounds[2] <= (var10 ? var6 + 1 : var6) * 20
                  && var13.collisionBounds[2] >= (var10 ? var6 : var6 - 1) * 20
                  && a(var0, var1, var2, var3, var13.collisionBounds[2], var13.collisionBounds[1], var13.collisionBounds[2], var13.collisionBounds[3], var4)) {
                  var10 = true;
                  var5 = 24;
               }
            } else {
               if (a(var0, var1, var2, var3, (var6 + 1) * 20, var7 * 20, (var6 + 1) * 20, (var7 + 1) * 20, var4)) {
                  var10 = true;
                  var5 = Game.g(++var6, var7);
               }

               if (var12 != null && var12.collisionBounds[0] >= (var10 ? var6 - 1 : var6) * 20 && var12.collisionBounds[0] <= (var10 ? var6 : var6 + 1) * 20) {
                  if (a(var0, var1, var2, var3, var12.collisionBounds[2], var12.collisionBounds[1], var12.collisionBounds[2], var12.collisionBounds[3], var4)) {
                     var10 = true;
                     var5 = 24;
                  }
               } else if (var13 != null
                  && var13.collisionBounds[0] >= (var10 ? var6 - 1 : var6) * 20
                  && var13.collisionBounds[0] <= (var10 ? var6 : var6 + 1) * 20
                  && a(var0, var1, var2, var3, var13.collisionBounds[2], var13.collisionBounds[1], var13.collisionBounds[2], var13.collisionBounds[3], var4)) {
                  var10 = true;
                  var5 = 24;
               }
            }

            if (!var10) {
               if (var9) {
                  if (a(var0, var1, var2, var3, var6 * 20, var7 * 20, (var6 + 1) * 20, var7 * 20, var4)) {
                     var10 = true;
                     var5 = Game.g(var6, --var7);
                  }

                  if (var12 != null && var12.collisionBounds[3] >= (var7 + (var10 ? 1 : 0)) * 20 && var12.collisionBounds[3] <= (var7 + (var10 ? 2 : 1)) * 20) {
                     if (a(var0, var1, var2, var3, var12.collisionBounds[0], var12.collisionBounds[3], var12.collisionBounds[2], var12.collisionBounds[3], var4)
                        )
                      {
                        var10 = true;
                        var5 = 24;
                     }
                  } else if (var13 != null
                     && var13.collisionBounds[3] >= (var7 + (var10 ? 1 : 0)) * 20
                     && var13.collisionBounds[3] <= (var7 + (var10 ? 2 : 1)) * 20
                     && a(var0, var1, var2, var3, var13.collisionBounds[0], var13.collisionBounds[3], var13.collisionBounds[2], var13.collisionBounds[3], var4)
                     )
                   {
                     var10 = true;
                     var5 = 24;
                  }
               } else {
                  if (a(var0, var1, var2, var3, var6 * 20, (var7 + 1) * 20, (var6 + 1) * 20, (var7 + 1) * 20, var4)) {
                     var10 = true;
                     var5 = Game.g(var6, ++var7);
                  }

                  if (var12 != null && var12.collisionBounds[1] < (var7 + (var10 ? 0 : 1)) * 20 && var12.collisionBounds[1] > (var7 + (var10 ? -1 : 0)) * 20) {
                     if (a(var0, var1, var2, var3, var12.collisionBounds[0], var12.collisionBounds[1], var12.collisionBounds[2], var12.collisionBounds[1], var4)
                        )
                      {
                        var10 = true;
                        var5 = 24;
                     }
                  } else if (var13 != null
                     && var13.collisionBounds[1] < (var7 + (var10 ? 0 : 1)) * 20
                     && var13.collisionBounds[1] > (var7 + (var10 ? -1 : 0)) * 20
                     && a(var0, var1, var2, var3, var13.collisionBounds[0], var13.collisionBounds[1], var13.collisionBounds[2], var13.collisionBounds[1], var4)
                     )
                   {
                     var10 = true;
                     var5 = 24;
                  }
               }
            }

            if (var5 >= 15 || !var10) {
               return;
            }
         } while (++var11 <= 56);
      }
   }

   public static final boolean b(int var0, int var1, int var2, int var3, int[] var4) {
      int var5 = var4[0];
      int var6 = var4[1];
      int var7 = var4[2];
      int var8 = var4[3];
      return a(var0, var1, var2, var3, var5, var6, var7, var6)
         || a(var0, var1, var2, var3, var5, var6, var5, var8)
         || a(var0, var1, var2, var3, var7, var8, var7, var6)
         || a(var0, var1, var2, var3, var7, var8, var5, var8);
   }

   public static final boolean a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int[] var8) {
      int var9 = -(var2 - var0) * (var7 - var5) + (var3 - var1) * (var6 - var4);
      int var10 = -(var4 - var0) * (var7 - var5) + (var5 - var1) * (var6 - var4);
      int var11 = (var2 - var0) * (var5 - var1) - (var3 - var1) * (var4 - var0);
      if (!i(var9, var10)) {
         return false;
      }

      if (!i(var9, var11)) {
         return false;
      }

      if (l(var11) > l(var9)) {
         return false;
      }

      var8[0] = var0 + var10 * (var2 - var0) / var9;
      var8[1] = var1 + var10 * (var3 - var1) / var9;
      return true;
   }

   public static final boolean a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8;
      if ((var8 = -(var2 - var0) * (var7 - var5) + (var3 - var1) * (var6 - var4)) == 0) {
         return false;
      } else {
         int var9 = -(var4 - var0) * (var7 - var5) + (var5 - var1) * (var6 - var4);
         int var10 = (var2 - var0) * (var5 - var1) - (var3 - var1) * (var4 - var0);
         if (!i(var8, var9)) {
            return false;
         } else if (l(var9) > l(var8)) {
            return false;
         } else {
            return !i(var8, var10) ? false : l(var10) <= l(var8);
         }
      }
   }

   public final void an() {
      if (3 == Game.be[Game.levelIndex] && bM) {
         this.G = this.G + (fs << 8);
      }
   }

   public final boolean ao() {
      if (this.type == 2
         || this.type == 3
         || this.type == 4
         || this.type == 5
         || this.type == 13
         || this.type == 14
         || this.type == 15
         || this.type == 70
         || this.type == 92
         || this.type == 99
         || (this.c & 8192) != 0) {
         return true;
      } else {
         return this.type == 1 && this.aG <= 0 ? true : this.type == 11 && (this.ar() == 2 || this.ar() == 0);
      }
   }

   public final boolean ap() {
      return this.type == 3 || this.type == 1 || this.type == 14 || this.type == 70 || this.type == 11 || this.type == 73;
   }

   public final boolean aq() {
      if (this.type == 72) {
         return true;
      } else {
         return this.type != 14 || this.animationId != 2 && this.animationId != 3 ? this.c(this) : true;
      }
   }

   private void g(int[] var1) {
      if (this.facingLeft) {
         this.aJ[2] = this.collisionBounds[0] - var1[0];
         this.aJ[0] = this.aJ[2] - var1[1];
      } else {
         this.aJ[0] = this.collisionBounds[2] + var1[0];
         this.aJ[2] = this.aJ[0] + var1[1];
      }

      this.aJ[1] = this.collisionBounds[1] - var1[2];
      this.aJ[3] = this.collisionBounds[3] + var1[3];
   }

   private void cV() {
      this.aA = this.aA - Game.h;
   }

   private void cW() {
      this.aB = this.aB - Game.h;
   }

   private void u(int var1, int var2) {
      this.p.p = var1;
      switch (var1) {
         case 1:
         case 2:
            this.p.q = var2 - Game.cameraX;
            return;
         case 4:
         case 8:
            this.p.r = var2 - Game.cameraY;
         case 3:
         case 5:
         case 6:
         case 7:
      }
   }

   private boolean a(int var1, int[] var2, int var3, int var4) {
      dV[0] = this.collisionBounds[0];
      dV[2] = this.collisionBounds[2];
      dV[1] = this.collisionBounds[1];
      dV[3] = this.collisionBounds[3];
      switch (var1) {
         case 1:
            dV[0] = var3;
            break;
         case 2:
            dV[2] = var3;
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            break;
         case 4:
            dV[1] = var4;
            break;
         case 8:
            dV[3] = var4;
      }

      return b(var2, dV);
   }

   private void a(Actor var1, int var2, int var3) {
      if (var1 != null) {
         var1.facingLeft = this.facingLeft;
         var1.positionX = this.positionX + (this.facingLeft ? -var2 : var2);
         var1.positionY = this.positionY + var3;
      }
   }

   private void b(Actor var1, int var2, int var3) {
      this.a(var1, var2, var3);
      if (var1 != null) {
         var1.facingLeft = !this.facingLeft;
      }
   }

   private int B(int var1) {
      byte var2 = 0;
      byte var3 = 0;
      switch (this.type) {
         case 1:
            var2 = 0;
            var3 = 3;
            break;
         case 2:
            var2 = 2;
            var3 = 3;
            break;
         case 3:
            var2 = 3;
            var3 = 3;
            break;
         case 4:
            var2 = 4;
            var3 = 1;
            break;
         case 5:
            var2 = 1;
            var3 = 4;
            break;
         case 8:
            var2 = 7;
            var3 = 3;
            break;
         case 9:
            var2 = 8;
            var3 = 3;
            break;
         case 10:
            var2 = 9;
            var3 = 4;
            break;
         case 11:
            var2 = 10;
            var3 = 2;
            break;
         case 13:
            var2 = 14;
            var3 = 4;
            break;
         case 14:
            var2 = 12;
            var3 = 3;
            break;
         case 70:
            var2 = 17;
            var3 = 3;
            break;
         case 83:
            var2 = 18;
            var3 = 2;
            break;
         case 90:
            var2 = 15;
            var3 = 5;
            break;
         case 92:
            var2 = 19;
            var3 = 3;
            break;
         case 99:
            var2 = 20;
            var3 = 3;
            break;
         case 100:
            var2 = 16;
            var3 = 4;
      }

      return Game.cH[var2][var3 * this.aI + var1];
   }

   private int cX() {
      return this.aH >> 0 & 1;
   }

   public final int ar() {
      return this.aH >> 1 & 15;
   }

   public static final int l(int var0) {
      return var0 < 0 ? -var0 : var0;
   }

   public static final int g(int var0, int var1) {
      return var0 > var1 ? var0 : var1;
   }

   public static final int h(int var0, int var1) {
      return var0 < var1 ? var0 : var1;
   }

   public static final long a(long var0) {
      long var2;
      if (var0 < 100L) {
         var2 = 9L;

         while (var0 < var2 * var2) {
            var2--;
         }
      } else {
         var2 = a(var0 / 100L);
         long var4;
         long var6 = (var4 = var0 - var2 * var2 * 100L) / (var2 * 20L);

         while (var6 * (var2 * 20L + var6) > var4) {
            var6--;
         }

         var2 = var2 * 10L + var6;
      }

      return var2;
   }

   public static final void a(int var0, int var1, int var2, int var3, int var4, int[] var5) {
      int var6 = var2 - var0 << 8;
      int var7 = var3 - var1 << 8;
      var5[0] = (var6 * Game.l(var4) - var7 * Game.m(var4) >> 8) / 256;
      var5[1] = (var6 * Game.m(var4) + var7 * Game.l(var4) >> 8) / 256;
      var5[0] += var0;
      var5[1] += var1;
   }

   public static final boolean i(int var0, int var1) {
      return var0 >= 0 && var1 >= 0 || var0 <= 0 && var1 <= 0;
   }

   private int cY() {
      int var1;
      return (var1 = this.a - Game.player.a) < 0 ? -var1 : var1;
   }

   private int C(int var1) {
      int var2;
      return (var2 = this.a - Game.player.a) < 0 ? var1 : -var1;
   }

   private boolean cZ() {
      return this.facingLeft && Game.player.positionX < this.positionX || !this.facingLeft && Game.player.positionX > this.positionX;
   }

   private void da() {
      this.facingLeft = this.a - Game.player.a > 0;
   }

   private int D(int var1) {
      int var2;
      if (l(var2 = this.a - Game.player.a) > l(var1)) {
         return var2 < 0 ? var1 : -var1;
      } else {
         return 0;
      }
   }

   private int E(int var1) {
      int var2;
      if (l(var2 = this.b - Game.player.b) > l(var1)) {
         return var2 < 0 ? var1 : -var1;
      } else {
         return 0;
      }
   }

   private void F(int var1) {
      if (l(Game.player.a - this.a) > l(var1)) {
         this.facingLeft = Game.player.positionX < this.positionX;
      }
   }

   private void db() {
      this.G = this.H = this.I = this.J = 0;
   }

   public static final void j(int var0, int var1) {
      aQ = true;
      aR = var1;
      aS = var0;
      bI = -1;
   }

   public static final void as() {
      aQ = false;
      aR = -1;
   }

   public final void m(int var1) {
      if (Game.be[Game.levelIndex] == 2) {
         this.H = var1 + dR;
      }
   }

   public final boolean c(int[] var1) {
      if (aV == null) {
         return false;
      }

      for (int var2 = 0; var2 < aV.length; var2++) {
         if (aV[var2] != null && b(aV[var2].collisionBounds, var1)) {
            return true;
         }
      }

      return false;
   }

   public final boolean d(int[] var1) {
      if (ba == null) {
         return false;
      }

      for (int var2 = 0; var2 < ba.length; var2++) {
         if (ba[var2] != null && b(ba[var2].k, var1)) {
            return true;
         }
      }

      return false;
   }

   public final boolean e(int[] var1) {
      if (er == null) {
         return false;
      }

      for (int var2 = 0; var2 < er.length; var2++) {
         if (er[var2] != null && b(er[var2].collisionBounds, var1)) {
            return true;
         }
      }

      return false;
   }

   public final boolean a(int var1, int[] var2) {
      if (var1 == 0) {
         return this.b(var1, var2) || this.c(var2);
      } else {
         return var1 == 1 ? this.b(var1, var2) || this.d(var2) : false;
      }
   }

   public final boolean f(int[] var1) {
      return this.c(var1) || this.d(var1) || this.b(ch, var1)
         ? true
         : Game.player.aq == 1 && cv == 15 && Game.player.animationId == 4 && b(Game.player.collisionBounds, this.collisionBounds);
   }

   public final boolean at() {
      if (b(Game.player.k, this.collisionBounds)) {
         return true;
      } else if (Game.player.aq == 1 && cv == 15 && Game.player.animationId == 4 && b(Game.player.collisionBounds, this.collisionBounds)) {
         Game.player.a(3);
         return true;
      } else {
         return false;
      }
   }

   public final boolean b(int var1, int[] var2) {
      return var1 == 0 && ch == 0 && Game.player.animationId >= 16 && Game.player.animationId <= 23 && b(Game.player.k, var2)
         ? true
         : var1 == 1 && ch == 1 && Game.player.animationId >= 26 && Game.player.animationId <= 31 && b(Game.player.k, var2);
   }

   private void dc() {
      this.facingLeft = Game.player.positionX < this.positionX;
   }

   public final void c(int var1, int var2, int var3) {
      if (this.q == null) {
         this.a(21, 16, var1, 0, 0, 302, false);
         this.a(aU);
         this.q.positionX = var2;
         this.q.positionY = var3;
         this.q.facingLeft = false;
         this.q.c &= -2;
         this.q.aB = 1;
         aU = null;
      }
   }

   public final Actor d(int var1, int var2, int var3) {
      this.a(21, 16, var1, 0, 0, 302, false);
      aU.positionX = var2;
      aU.positionY = var3;
      aU.facingLeft = false;
      aU.aB = 1;
      return aU;
   }

   public final void au() {
      if (this.q != null) {
         Game.e(this.q);
         this.q = null;
      }
   }

   final void a(int[] var1, int var2) {
      int var3 = (var1[0] + 10) / 20;
      int var4 = (var1[2] - 10) / 20;
      int var5 = (var1[1] + 10) / 20;
      int var6 = (var1[3] - 10) / 20;

      for (int var7 = var3; var7 <= var4; var7++) {
         for (int var8 = var5; var8 <= var6; var8++) {
            Game.d(var7, var8, var2);
         }
      }
   }

   final void a(int[] var1, boolean var2) {
      if (var2) {
         this.a(var1, 0);
      } else {
         this.a(var1, 24);
      }
   }

   private boolean dd() {
      boolean var1 = false;
      int var2 = 0;
      if (this.facingLeft) {
         var2 = this.collisionBounds[0] - 1;
      } else {
         var2 = this.collisionBounds[2] + 1;
      }

      switch (Game.g(var2 / 20, this.positionY / 20)) {
         case 15:
         case 24:
            return true;
         case 16:
            this.positionX = var2 / 20 * 20 + this.positionY % 20;
            return true;
         case 17:
            this.positionX = var2 / 20 * 20 + (20 - this.positionY % 20);
            return true;
         case 18:
            this.positionX = var2 / 20 * 20 + this.positionY % 20 * 2;
            return true;
         case 19:
            if (this.positionY % 20 > 10) {
               return false;
            }

            this.positionX = var2 / 20 * 20 + (this.positionY % 20 - 10) * 2;
            return true;
         case 20:
            if (this.positionY % 20 < 10) {
               return false;
            }

            this.positionX = var2 / 20 * 20 + (20 - this.positionY % 20) * 2;
            return true;
         case 21:
            this.positionX = var2 / 20 * 20 + (10 - this.positionY % 20) * 2;
            return true;
         case 22:
         case 23:
         default:
            return false;
      }
   }

   private void de() {
      this.g();
      if (b(Game.player.collisionBounds, this.collisionBounds)) {
         this.c |= 4096;
      } else {
         this.c &= -4097;
      }
   }

   private void df() {
      this.cI = false;

      for (int var2 = 0; var2 < Game.ci; var2++) {
         Actor var1;
         if ((var1 = Game.ch[var2]).type == 13 && var1.aq != 0 && (var1.ar() == 1 || var1.ar() == 3)) {
            this.eX = Game.h(var1.positionX - this.positionX, var1.positionY - this.positionY);
            if (this.eX < 100) {
               this.cI = true;
               this.cJ = var1;
               break;
            }
         }
      }

      switch (this.aq) {
         case 0:
            if (this.ft) {
               return;
            }

            if (this.animationId < 6) {
               if (this.cI && (this.animationId == 0 || this.animationId == 2 || this.animationId == 4 || this.animationId == 5)) {
                  this.eX = Game.h(this.cJ.a - this.a, this.cJ.b - this.b);
                  this.G = this.eX == 0 ? 0 : (this.cJ.a - this.a) * 1536 / this.eX;
                  this.H = this.eX == 0 ? 0 : (this.cJ.b - this.b) * 1536 / this.eX;
                  if (b(this.collisionBounds, this.cJ.collisionBounds)) {
                     if (this.cJ.ar() == 1) {
                        this.cJ.p(105);
                     } else if (this.cJ.ar() == 3) {
                        this.cJ.p(107);
                     }

                     if (this.animationId == 0 && this.cJ.ay < 8) {
                        this.cJ.ay++;
                     } else if (this.animationId == 2 && this.cJ.aw < 8) {
                        this.cJ.aw++;
                     } else if (this.animationId == 4 && this.cJ.ax < 8) {
                        this.cJ.ax++;
                     } else if (this.animationId == 5 && this.cJ.ax < 8) {
                        this.cJ.az++;
                     }

                     this.av();
                  }
               } else {
                  int var5 = Game.player.collisionBounds[3] - Game.player.collisionBounds[1] << 7;
                  this.eX = Game.h(Game.player.a - this.a, Game.player.b - this.b - var5 - 1);
                  if (this.eX <= (this.an == -1 ? 40960 : 10240)) {
                     this.G = this.eX == 0 ? 0 : (Game.player.a - this.a) * 1536 / this.eX;
                     this.H = this.eX == 0 ? 0 : (Game.player.b - var5 - this.b) * 1536 / this.eX;
                     if (Game.player.G != 0 && i(Game.player.G, Game.player.positionX - this.positionX)) {
                        this.G = this.G + Game.player.G;
                     }

                     if (Game.player.H != 0 && i(Game.player.H, Game.player.positionY - this.positionY)) {
                        this.H = this.H + Game.player.H;
                     }
                  }
               }
            }

            if (this.animationId < 6 ? b(Game.player.collisionBounds, this.collisionBounds) : this.at()) {
               if (this.animationId == 3 || this.animationId == 1) {
                  this.aq = 103;
                  if (Game.player.aq == 0 || Game.player.aq == 2) {
                     Game.player.a(36);
                  }

                  Game.player.db();
                  return;
               }

               this.av();
            }

            if (this.aw != -1) {
               this.ay = this.ay - Game.h;
               if (this.ay <= 0) {
                  this.av();
                  return;
               }
            }
            break;
         case 100:
            this.aB = this.aB + Game.h;
            if (this.animationId < 6 && this.aB >= 500 || (this.animationId == 7 || this.animationId == 10) && this.h == 1) {
               if (this.ay > 0) {
                  switch (this.animationId) {
                     case 0:
                        Game.health += 6;
                        if (Game.health > Game.maxHealth) {
                           Game.health = Game.maxHealth;
                        }

                        if (!bv) {
                           bv = true;
                           b(0, 58, true);
                        }
                     case 1:
                     case 3:
                     case 6:
                     default:
                        break;
                     case 2:
                        Game.magic += 6;
                        if (Game.magic > Game.maxMagic) {
                           Game.magic = Game.maxMagic;
                        }

                        if (!bw) {
                           bw = true;
                           b(0, 59, true);
                        }
                        break;
                     case 4:
                        Game.D += 5;
                        if (!bu) {
                           bu = true;
                           b(0, 56, true);
                           bE = 1;
                        }
                        break;
                     case 5:
                        Game.D = Game.D + 10 * eh[this.aC];
                        if (!bu) {
                           bu = true;
                           b(0, 56, true);
                           bE = 1;
                        }
                        break;
                     case 7:
                        if (this.aA == 1) {
                           for (int var4 = 0; var4 < 20; var4++) {
                              this.a(35, 31, 5, 0, 10, 308, true);
                           }
                        }
                  }
               }

               this.aA--;
               if (this.aA > 0) {
                  this.aq = 102;
                  this.animationId = 6;
                  return;
               }

               this.aq = 101;
               Game.bu = true;
               if (this.animationId == 7) {
                  this.a(8);
                  Game.I(-1);
                  return;
               }
            }
            break;
         case 101:
            Game.bu = false;
            if (this.e() || this.animationId < 6) {
               Game.e(this);
               return;
            }
            break;
         case 102:
            if (this.animationId < 6 ? b(Game.player.collisionBounds, this.collisionBounds) : this.at()) {
               this.av();
               this.aB = 0;
               return;
            }
            break;
         case 103:
            int var3 = Game.player.collisionBounds[3] - Game.player.collisionBounds[1];
            this.positionX = Game.player.positionX;
            this.positionY = Game.player.positionY - var3 - 20;
            Game.Q = Game.player.positionX;
            Game.R = Game.player.positionY;
            Game.bK = this.a(39, 17, 34, 0, var3 + 20, 308, false);
            Game.h(this.animationId == 1 ? 10 : 11);
            Game.T();
            Game.I(4);
            e(Game.cb);
            Game.player.c |= 4096;
            Game.bw = true;
            this.c |= 4096;
            this.aq = 104;
            return;
         case 104:
            if (!Game.modalActive) {
               this.av();
               Game.player.c &= -4097;
               this.c &= -4097;
               Game.bw = false;
               this.aB = 0;
               this.G = 0;
               this.H = 0;
            }
            break;
         case 105:
            this.aD = this.aD - Game.h;
            if (this.aD <= 0) {
               this.aq = 0;
               return;
            }
            break;
         case 106:
            this.aD = this.aD - Game.h;
            if (this.aD <= 0) {
               this.aq = 0;
               this.c &= -129;
            }

            return;
      }
   }

   final void av() {
      this.aq = 100;
      if (this.animationId < 6) {
         int var2;
         if ((var2 = this.animationId >> 1) > 2) {
            var2 = 2;
         }

         this.a(39, 17, 24 + var2, 0, 0, 308, false);
         if (var2 == 2) {
            aU.ax = Game.player.positionX - Game.cameraX + Game.i(-10, 10);
            aU.az = Game.player.positionY - Game.cameraY + -55;
            aU.av = 0;
            if (!this.cI && this.cJ == null) {
               if (this.animationId == 5) {
                  aU.aB = 10 * eh[this.aC];
               } else {
                  aU.aB = 5;
               }

               if (this.an == -1) {
                  if (!dZ) {
                     dZ = true;
                  }

                  dW = dW + aU.aB;
                  if (dY >= 0) {
                     dY = 350;
                     aU.aB = -1;
                  }
               }
            } else {
               aU.aB = -1;
            }
         }

         aU.c |= 4096;
      } else if (this.animationId == 6) {
         this.b(7);
      } else {
         Game.I(-1);
         this.b(10);

         for (int var1 = 0; var1 < 20; var1++) {
            this.a(35, 31, 4, 0, 10, 308, true);
         }
      }
   }

   public static final void b(int var0, int var1, boolean var2) {
      Game.dialogText = Game.k(var0, var1);
      if (var1 >= 64 && var1 <= 66) {
         Game.dialogText = Game.dialogText + " " + Game.k(0, 67);
      }

      Game.a.a(Game.dialogText, 180);
      Game.bH = 0;
      if (var2) {
         if (Game.levelIndex == 4) {
            Game.player.db();
            Game.ca.positionX = Game.bZ.positionX = Game.player.positionX;
            Game.ca.positionY = Game.bZ.positionY = Game.player.positionY;
         }

         Game.h(14);
      }
   }

   public static final void a(int var0, int var1, boolean var2, boolean var3) {
      Game.dialogText = Game.k(var0, var1);
      Game.a.a(Game.dialogText, var3 ? 175 : 180);
      Game.bH = 0;
      Game.P = 0;
      Game.bF = 0;
      Game.bG = 1;
      Game.bC = Game.player.positionX - Game.cameraX;
      Game.bD = Game.player.positionY - Game.cameraY;
      if (var2) {
         Game.h(5);
      }
   }

   public final void aw() {
      if ((this.c & 256) == 0) {
         if (this.animationId == 0) {
            this.a(1);
         } else if (this.animationId == 4) {
            this.a(6);
         }

         this.aq = 101;
      } else {
         if ((this.c & 256) != 0 && this.ay == 3) {
            if (this.animationId == 0) {
               this.a(1);
            }

            this.aq = 101;
            this.ax = 5;
         }
      }
   }

   public final void ax() {
      if (this.w != null && (!b(Game.player.collisionBounds, this.collisionBounds) || Game.player.animationId > 4)) {
         Game.e(this.w);
         this.w = null;
         this.aq = 0;
      }
   }

   final void ay() {
      this.g();
      this.N = false;
      if ((this.c & 256) != 0) {
         this.H = 0;
      }

      if ((this.c & 256) != 0 && (this.c & 32) != 0) {
         this.p(0);
      } else {
         switch (this.aq) {
            case 0:
               this.b(false, false);
               if ((this.c & 256) == 0 && !this.bL) {
                  this.aq = 103;
               }

               if (this.at() && this.animationId == 4) {
                  this.aw();
               }

               if (this.animationId == 0 && b(Game.player.collisionBounds, this.collisionBounds) && (Game.player.animationId <= 4 || Game.player.aq == 1)) {
                  if (this.w == null && (this.c & 256) == 0) {
                     this.w = this.a(21, 16, 25, 0, Game.player.aq == 1 && cv == 14 ? -90 : -80, 0, true);
                     this.w.c &= -2;
                     this.w.facingLeft = false;
                     this.w.ap = 91;
                     this.w.aC = 3;
                  }

                  this.aq = 106;
               }

               this.ax();
               return;
            case 101:
               if (this.e()) {
                  boolean var1 = false;
                  if (this.ay == 3) {
                     this.av = Game.i(0, 100);
                     if (this.av < 5) {
                        this.az = 1;
                     } else if (this.av < 10) {
                        this.az = 3;
                     } else {
                        this.az = 0 + Game.i(0, 3) * 2;
                     }

                     this.a(35, 31, this.az, 0, 0, 308, false);
                     if (--this.ax > 0) {
                        this.a(2);
                        this.aq = 104;
                        this.aA = 200;
                        return;
                     }

                     this.a(2);
                     this.aA = 1000;
                     this.aq = 102;
                     this.c |= 16;
                     return;
                  }

                  int var2;
                  if (this.ay < 0 && ((var2 = -this.ay) == 5 && bB || var2 == 8 && bD)) {
                     this.ay = 2;
                  }

                  if (this.ay >= 0) {
                     boolean var6 = false;
                     if (this.animationId == 1 && this.ay != 2) {
                        var6 = true;
                     }

                     int var3 = 1;
                     byte var4 = 0;
                     if (!var6) {
                        var3 = Game.i(4, 7);
                     }

                     if (this.ay == 2) {
                        var4 = 1;
                        if (Game.i(0, 50) == 1) {
                           var3 = Game.i(10, 30);
                        }
                     }

                     for (int var5 = 0; var5 < var3; var5++) {
                        this.a(35, 31, 0 + (!var6 ? 0 : 1) + 2 * this.ay + var4, !var6 ? Game.i(-40, 40) : 0, !var6 ? Game.i(-70, 0) : 0, 308, false);
                        aU.facingLeft = false;
                     }
                  } else {
                     this.a(48, 23, -this.ay, 0, 0, 308, false);
                  }

                  if (this.animationId == 1) {
                     this.a(2);
                  } else if (this.animationId == 6) {
                     this.a(7);
                  }

                  this.aA = 1000;
                  this.aq = 102;
                  this.c |= 16;
                  return;
               }
               break;
            case 102:
               this.cV();
               if (this.animationId == 2) {
                  if (this.aA < 0) {
                     this.a(3);
                     return;
                  }
               } else if (this.e()) {
                  Game.e(this);
                  return;
               }
               break;
            case 103:
               this.cV();
               if (this.bL) {
                  this.db();
                  if (this.aA <= 0) {
                     this.p(0);
                     return;
                  }
               } else {
                  this.J = 320;
                  this.b(false, false);
                  if (this.H > 2560) {
                     this.H = 2560;
                     this.J = 0;
                  }

                  if (this.H > 0 && this.eD >= 15) {
                     this.bN();
                     this.db();
                     this.a(39, 17, 22, this.collisionBounds[0] - this.collisionBounds[2] >> 1, 0, 302, false);
                     this.a(39, 17, 22, this.collisionBounds[2] - this.collisionBounds[0] >> 1, 0, 302, true);
                  }
               }
               break;
            case 104:
               this.cV();
               if (this.aA <= 0) {
                  this.aq = 105;
                  this.a(0);
                  return;
               }
               break;
            case 105:
               if (b(Game.player.k, this.collisionBounds)) {
                  this.a(1);
                  this.aq = 101;
                  return;
               }
               break;
            case 106:
               if (Game.G(65568)) {
                  this.aw();
                  if (this.w != null) {
                     Game.e(this.w);
                     this.w = null;
                  }
               }

               this.ax();
               return;
         }
      }
   }

   private void t(Actor var1) {
      if (var1.type == 70) {
         if (var1.aq != 103) {
            var1.p(2);
            return;
         }

         if (!this.fx) {
            var1.b(ei[o(this.animationId)], true);
            return;
         }
      } else {
         var1.b(ei[o(this.animationId)], true);
      }
   }

   private void G(int var1) {
      if (this.collisionBounds != null) {
         this.c &= -4097;

         for (int var3 = 0; var3 < Game.ck; var3++) {
            Actor var2;
            if ((var2 = Game.cj[var3]) != null && var2 != this) {
               if (var2.collisionBounds != null && var2.aq != 5 && var2.aq != 6 && var2.aq != 7 && b(var2.collisionBounds, this.k)) {
                  if ((var2.c & 1024) != 0) {
                     continue;
                  }

                  if (var2.aD <= 0 && (var2.M() || var2.type == 70 && (var1 == 1 && this.animationId == 17 || var1 == 0 && this.animationId == 9))) {
                     if (var1 == 0 && (this.c & 128) == 0) {
                        if (this.animationId == 9) {
                           if (var2.aq == 4) {
                              for (int var5 = 0; var5 < 4; var5++) {
                                 var2.a(39, 17, 1, Game.i(-40, 40), Game.i(-40, 40), 302, var2.facingLeft);
                              }

                              this.a(96, 51, 0, 0, -70, 300, false);
                              aU.facingLeft = false;
                              aU.c &= -2;
                              Game.a(6, 0);
                              var2.c(9, true);
                           } else {
                              var2.b(ci[0 + cl[0]] * e(0, var2.type) / 100, true);
                           }
                        } else if (this.animationId == 35 || this.animationId == 36) {
                           this.a(39, 17, 1, 0, 0, 302, var2.facingLeft);
                           if (this.aC == 88 && this.fx) {
                              var2.b(ci[0], true);
                              this.db();
                              this.a(36);
                              Game.I(-1);
                           } else if (var2.type == 11) {
                              if (var2.ar() != 1) {
                                 var2.b(ci[0 + cl[0]] * e(0, var2.type) / 100, true);
                              } else {
                                 var2.b(ci[0 + cl[0]] * e(0, var2.type) / 100, aE);
                              }
                           } else if (var2.type == 90 || var2.type == 100) {
                              var2.b(ci[0 + cl[0]] * e(0, var2.type) / 100, false);
                           } else if (var2.type == 85) {
                              var2.as = dp[0].as;
                              var2.b(ci[0 + cl[0]] * e(0, var2.type) / 100, false);
                              dp[0].as = var2.as;
                              if (dp[0].as <= 0) {
                                 dp[0].p(3);
                              }
                           } else {
                              var2.b(ci[0 + cl[0]] * e(0, var2.type) / 100, true);
                           }
                        }
                     } else if (var1 == 1) {
                        if (this.animationId == 16) {
                           if (var2.type == 11) {
                              if (var2.ar() != 1) {
                                 var2.b(ci[3 + cl[1]] * e(1, var2.type) / 100, true);
                              } else {
                                 var2.b(ci[3 + cl[1]] * e(1, var2.type) / 100, aE);
                              }
                           } else if (var2.type == 90 || var2.type == 100) {
                              var2.b(ci[3 + cl[1]] * e(1, var2.type) / 100, false);
                           } else if (var2.type == 85) {
                              var2.as = dp[0].as;
                              var2.b(ci[3 + cl[0]] * e(1, var2.type) / 100, false);
                              dp[0].as = var2.as;
                              if (dp[0].as <= 0) {
                                 dp[0].p(3);
                              }
                           } else {
                              var2.b(ci[3 + cl[1]] * e(1, var2.type) / 100, true);
                           }

                           this.a(2);
                           this.G = 0;
                        } else if (this.animationId == 13) {
                           if (var2.type == 90 || var2.type == 100) {
                              var2.b(ci[3 + cl[1]] * e(1, var2.type) / 100, false);
                           } else if (var2.type == 11) {
                              if (var2.ar() != 1) {
                                 var2.b(x() * e(ch, var2.type) / 100, true);
                              } else {
                                 var2.b(x() * e(ch, var2.type) / 100, aE);
                              }
                           } else if (var2.type == 85) {
                              var2.as = dp[0].as;
                              var2.b(x() * e(ch, var2.type) / 100, false);
                              dp[0].as = var2.as;
                              if (dp[0].as <= 0) {
                                 dp[0].p(3);
                              }
                           } else {
                              var2.b(x() * e(ch, var2.type) / 100, true);
                           }
                        } else if (this.animationId == 17) {
                           if (var2.type == 90 || var2.type == 100) {
                              var2.b(ci[3 + cl[1]] * e(1, var2.type) / 100, false);
                           } else if (var2.aq == 8) {
                              var2.c(9, true);
                              this.a(96, 51, 0, 0, -30, 300, false);
                              aU.facingLeft = false;
                              aU.c &= -2;
                              var2.a(61, 38, n(var2.type), 0, 0, 302, false);
                              aU.aA = 100;
                              Game.a(6, 0);
                           } else if (var2.type == 11) {
                              if (var2.ar() != 1) {
                                 var2.b(ci[5] * e(1, var2.type) / 100, true);
                              } else {
                                 var2.b(ci[5] * e(1, var2.type) / 100, aE);
                              }
                           } else if (var2.type == 85) {
                              var2.as = dp[0].as;
                              var2.b(ci[5] * e(1, var2.type) / 100, false);
                              dp[0].as = var2.as;
                              if (dp[0].as <= 0) {
                                 dp[0].p(3);
                              }
                           } else {
                              var2.b(ci[5] * e(1, var2.type) / 100, true);
                           }
                        }
                     }
                  } else if (var1 == 1 && var2.type == 90 && var2.aq >= 126 && var2.aq <= 128) {
                     var2.N();
                     this.a(2);
                     this.G = fs << 8;
                  } else if (var2.type == 14) {
                     if (var1 == 1) {
                        if (this.animationId != 13) {
                           this.a(2);
                        }

                        this.G = 0;
                        if (var2.aq != 104 && var2.aq != 105 && var2.aq != 106) {
                           var2.p(var2.cI ? 105 : 104);
                        }
                     }
                  } else if (var2.type == 37) {
                     if (var1 == 1 && (b(var2.collisionBounds, this.k) || b(var2.k, this.k))) {
                        if (var2.aq != 100) {
                           var2.aq = 100;
                           var2.a(1);
                           var2.aA = 10000;
                           var2.aB = 500;
                        }

                        if (this.animationId != 13) {
                           this.a(2);
                        }

                        this.G = 0;
                     }
                  } else if (var2.type == 75) {
                     if (var2.aq == 0 && (b(var2.collisionBounds, this.collisionBounds) || b(var2.k, this.collisionBounds))) {
                        var2.cP();
                     }
                  } else if (var2.type == 38 && var1 == 0) {
                     if (var2.aq == 0) {
                        var2.aq = 100;
                        var2.aA = 3500;
                        var2.a(2);
                        if (var2.aG != -1) {
                           Game.a(var2.r.aB, true);
                        }

                        var2.al();
                     }

                     if (var2.aB <= 0) {
                        this.a(39, 17, 1, 0, 0, 302, var2.facingLeft);
                        var2.aB = 500;
                     }
                  } else if (var2.type == 41 && var1 == 0) {
                     this.c |= 4096;
                  } else if (var2.type == 33 && var2.animationId == 0 && b(var2.collisionBounds, this.k) && var1 == 1) {
                     if (this.animationId != 13) {
                        this.b(18);
                     }

                     var2.b(this);
                     this.s = var2;
                     if (this.facingLeft) {
                        this.positionX = var2.collisionBounds[2];
                     } else {
                        this.positionX = var2.collisionBounds[0];
                     }

                     this.av = this.positionY - var2.positionY;
                     this.G = 0;
                     this.H = 0;
                  } else if (var2.type == 89) {
                     if (var1 == 0 && var2.aq == 0) {
                        var2.p(101);
                        var2.aA = var2.ay;
                     }
                  } else if (var2.type == 16 && var1 == 0) {
                     if (var2.aq == 103 || var2.aA <= 0 && var2.aq == 102) {
                        var2.p(104);
                     }
                  } else if (var2.type == 61 && var1 == 0 && (this.c & 128) == 0) {
                     if (var2.animationId < 13 && var2.a(this.collisionBounds, false, 61)) {
                        this.a(39, 17, 1, 0, 0, 302, var2.facingLeft);
                     }
                  } else if (var2.type == 4 && var1 == 0) {
                     if (var2.aq != 2 && var2.aq != 3) {
                        var2.p(2);
                     }
                  } else if (this.s(var2)) {
                     if (var2.aq == 0 || var2.aq == 102) {
                        var2.av();
                     }
                  } else if (var2.type != 70 || var1 == 1 && (var1 != 1 || this.animationId == 17)) {
                     if (var2.type == 88 && this.aC == 88 && this.fx && this.q != var2) {
                        if (var2.aC >= 31 && var2.aC <= 34) {
                           var2.q(this);
                           this.r = null;
                        }

                        this.db();
                        this.a(36);
                     } else if (var2.type == 81 && var1 == 1) {
                        var2.aN();
                     } else if (var2.type == 30 && var2.aF == 0) {
                        if (var2.aq == 0) {
                           var2.facingLeft = Game.player.positionX > var2.positionX;
                           var2.aq = 102;
                           var2.a(4);
                           if (var1 == 1) {
                              if (this.animationId != 13) {
                                 this.a(2);
                              }

                              this.G = 0;
                           }

                           if (this.aC == 88 && this.fx) {
                              this.db();
                              this.a(36);
                           }
                        } else if (var2.aq == 101) {
                           var2.aq = 102;
                           var2.facingLeft = Game.player.positionX > var2.positionX;
                           var2.a(6);
                           if (var1 == 1) {
                              if (this.animationId != 13) {
                                 this.a(2);
                              }

                              this.G = 0;
                           }

                           if (this.aC == 88 && this.fx) {
                              this.db();
                              this.a(36);
                           }
                        }
                     } else if (var2.type == 45 && var2.aq == 0 && var2.animationId == 7) {
                        int[] var4;
                        (var4 = new int[4])[0] = var2.collisionBounds[0];
                        var4[2] = var2.collisionBounds[2];
                        var4[1] = var2.collisionBounds[1] - 3;
                        var4[3] = var2.collisionBounds[3];
                        if (b(var4, this.collisionBounds)) {
                           var2.ay = this.positionX;
                           this.a(var2.collisionBounds, true);
                           var2.aq = 100;
                           if (this.aC == 88 && this.fx) {
                              this.db();
                              this.a(36);
                           }
                        }
                     }
                  } else {
                     if (var2.aq != 2) {
                        if (var2.aH == 2) {
                           if (var2.aq == 111) {
                              var2.p(2);
                           } else if (var2.aq == 110 || var2.aq == 0) {
                              var2.p(111);
                           }
                        } else if (var2.aH == 3) {
                           var2.n = null;
                           var2.O = true;
                           var2.aH = 1;
                           var2.p(2);
                        } else {
                           var2.b(ci[5] * e(1, var2.type) / 100, true);
                        }
                     }

                     if (var1 == 1) {
                        if (this.animationId != 13) {
                           this.a(2);
                        }

                        this.G = 0;
                     }
                  }
               }

               if (var2.type == 70 && var2.aH == 3 && (var2.aq == 114 || var2.aq == 115)) {
                  var2.g();
                  if (b(var2.aw, var2.az, var2.positionX, var2.collisionBounds[3] + var2.collisionBounds[1] >> 1, this.collisionBounds)) {
                     var2.n = null;
                     var2.O = true;
                     var2.aH = 1;
                     var2.p(2);
                  }
               }
            }
         }
      }
   }

   static final int n(int var0) {
      switch (var0) {
         case 1:
         case 3:
            return 17;
         case 11:
            return 18;
         case 13:
         case 70:
         default:
            return 16;
      }
   }

   static final int o(int var0) {
      switch (var0) {
         case 9:
         case 16:
         case 17:
            return 3;
         case 35:
            return 4;
         default:
            return 0;
      }
   }

   private boolean dg() {
      int var1 = this.positionX / 20 + (this.facingLeft ? -1 : 1);

      for (int var2 = 1; var2 <= 3; var2++) {
         if (Game.g(var1, this.positionY / 20 - var2) >= 24) {
            return true;
         }
      }

      return false;
   }

   private void dh() {
      this.cV();
      this.g();
      switch (this.animationId) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 22:
         case 23:
         case 56:
            this.m(0);
         case 7:
         case 8:
         case 10:
         case 11:
         case 12:
         case 14:
         case 15:
         case 24:
         case 25:
         case 26:
         case 31:
         case 32:
         case 33:
         case 34:
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
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 57:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         default:
            if (this.e()) {
               Game.e(this);
            }
            break;
         case 9:
            this.G = this.facingLeft ? -2048 : 2048;
            this.G(0);
            if (this.dg()) {
               Game.e(this);
               return;
            }
            break;
         case 13:
            if (!bM) {
               this.b(true, true);
            }

            if (this.h == this.p.a(this.animationId) - 3) {
               if (eq > 0 && er[3 - eq - 1] == this) {
                  this.a(39, 17, 13, this.facingLeft ? -20 : 20, 0, 303, false);
                  aU.an();
                  er[3 - eq] = aU;
                  if (!bM) {
                     er[3 - eq].b(true, true);
                  }

                  eq--;
               }
            } else if (this.e()) {
               Game.e(this);
               return;
            }

            this.G(1);
            return;
         case 16:
         case 17:
            this.G(1);
            if (!b(this.collisionBounds, Game.viewBounds)) {
               Game.e(this);
               return;
            }

            if (this.collisionBounds[0] > Game.aJ.collisionBounds[0] + 1) {
               if (this.collisionBounds[2] >= Game.aJ.collisionBounds[2] - 1) {
                  return;
               }

               if (this.dd() || this.eL && this.facingLeft || this.eM && !this.facingLeft) {
                  if (ba[0] == this) {
                     this.a(18);
                     this.G = 0;
                  } else {
                     Game.e(this);
                  }
               }

               this.b(true, false);
               return;
            }
            break;
         case 18:
            if (this.e()) {
               this.a(19);
               this.aA = 10000;
               return;
            }
            break;
         case 19:
            if (this.s != null) {
               this.s.T = null;
               this.s.b((Actor)null);
               this.s = null;
            }

            if (this.aA < 0 || !this.dd() && (!this.eL || !this.facingLeft) && (!this.eM || this.facingLeft)) {
               this.a(20);
               this.aA = 1000;
            }

            this.d(0, this.collisionBounds);
            this.b(true, false);
            return;
         case 20:
            if (this.aA < 0 || !this.dd() && (!this.eL || !this.facingLeft) && (!this.eM || this.facingLeft)) {
               this.a(21);
            }

            this.d(0, this.collisionBounds);
            this.b(true, false);
            return;
         case 21:
            if (aT == this) {
               aT = null;
            }

            if (this.s != null && this.s.type == 73) {
               this.s.T = null;
               this.s = null;
            }

            if (this.e()) {
               if (this.s != null) {
                  this.s.T = null;
                  this.s.b((Actor)null);
                  this.s = null;
               }

               Game.e(this);
               return;
            }
            break;
         case 27:
         case 28:
            if ((Game.player.c & 4096) != 0) {
               this.c |= 4096;
            } else {
               this.c &= -4097;
            }

            this.positionX = Game.player.positionX;
            this.positionY = Game.player.positionY;
            if (Game.player.animationId >= 0 && Game.player.animationId <= 2) {
               this.c &= -129;
               return;
            }

            this.c |= 128;
            return;
         case 29:
            return;
         case 30:
            if (E != null) {
               if (E.type != 100) {
                  return;
               }

               if (this.aq == 101) {
                  if (this.ax < 30) {
                     this.ax += 5;
                  } else {
                     if (E.aq == 101) {
                        E.p(102);
                     }

                     this.aq = 102;
                  }
               } else if (this.aq == 102) {
                  this.ax = 30;
                  if (this.aA <= 0) {
                     this.ay = this.ay + this.av;
                     this.aw = this.aw + l(this.av);
                  }

                  if (this.aw >= (3 - Math.min(E.aC, 98) / 33) * 90) {
                     this.aq = 103;
                     this.aA = 1000;
                  }
               } else if (this.aq == 103 && this.aA <= 0) {
                  for (int var6 = 0; var6 < 4; var6++) {
                     Game.e(aY[var6]);
                     Game.e(aZ[var6]);
                  }

                  aY = null;
                  aZ = null;
                  E.p(103);
                  return;
               }

               this.positionX = E.positionX + (this.ax * Game.l(this.aB * 360 / 4 + this.ay) >> 8);
               this.positionY = E.positionY + (this.ax * Game.m(this.aB * 360 / 4 + this.ay) >> 8) - 40;
               if (aZ != null) {
                  a(E.positionX, E.positionY - 40, this.positionX, this.positionY, cU);
                  aZ[this.aB].positionX = cU[0];
                  aZ[this.aB].positionY = cU[1];
                  return;
               }
            }
            break;
         case 35:
            if (this.aC == 88 && this.fx) {
               this.ac = this.positionX;
               this.ad = this.positionY;
               this.b(false, false);
               this.positionX = this.ac;
               this.positionY = this.ad;
               if (this.bL || this.bN || this.eL || this.eM) {
                  this.a(36);
                  Game.I(-1);
                  this.db();
                  return;
               }
            } else {
               if (this.ax < 30) {
                  this.ax = 30;
                  this.ay += 10;
                  this.aA = 0;
               } else {
                  this.ax += 3;
                  this.ay += 3;
               }

               int var5 = this.aB * 360 / aW + this.ay;
               this.positionX = Game.player.positionX + (this.ax * Game.l(var5) >> 8);
               this.positionY = Game.player.positionY + (this.ax * Game.m(var5) >> 8) - 22;
               if (this.aA <= 0) {
                  if (this.aw == 0) {
                     this.a(36);
                     this.c &= -129;
                  } else {
                     this.aw--;
                     this.aA = 220;
                     this.c |= 128;
                     aV[this.aB] = this.a(39, 17, 36, 0, 0, 302, false);
                     aU.c |= 4096;
                     aU.aw = 0;
                  }

                  Game.I(-1);
               }
            }

            this.G(0);
            return;
         case 36:
            if (this.aC == 88 && this.fx) {
               if (this.e() && this.animationId == 36) {
                  if (this.r != null && this.r.aq != 0) {
                     this.r.aq = 0;
                     if (this.r.animationId > 35) {
                        this.r.a(this.r.aC + 17);
                     }
                  }

                  Game.e(this);
                  return;
               }
               break;
            }

            if (!this.e()) {
               this.G(0);
               return;
            }

            if (aV != null) {
               for (int var4 = 0; var4 < aW; var4++) {
                  if (aV[var4] == this) {
                     aV[var4] = null;
                     break;
                  }
               }
            }

            Game.e(this);
            return;
         case 42:
            this.b(true, true);
            return;
         case 55:
            this.b(true, true);
            this.H = 2560;

            for (int var2 = Game.ci; var2 >= 0; var2--) {
               Actor var1;
               if ((var1 = Game.ch[var2]) != null
                  && var1 != this
                  && var1.collisionBounds != null
                  && this.collisionBounds != null
                  && b(var1.collisionBounds, this.collisionBounds)) {
                  if (var1.M() && (var1.c & 1024) == 0 && var1.aD <= 0) {
                     this.t(var1);
                     this.H = 0;
                     this.b(56);
                  } else if (var1 == Game.player) {
                     Game.player.a(4, 10, 0, this);
                     this.H = 0;
                     this.b(56);
                  }
               }
            }

            if (this.bL) {
               this.H = 0;
               if (this.aH != 2) {
                  this.b(56);
                  return;
               }

               if (this.f(this.collisionBounds)) {
                  this.b(56);
                  return;
               }

               for (int var7 = 0; var7 < Game.ck; var7++) {
                  Actor var3;
                  if ((var3 = Game.cj[var7]) != null
                     && (var3.type == 83 || var3.type == 84 || var3.type == 85)
                     && b(var3.collisionBounds, this.collisionBounds)) {
                     this.b(56);
                     return;
                  }
               }
            }
            break;
         case 58:
            this.cV();
            this.H = -1024;
            this.m(this.H);
            if (this.aA < 0) {
               Game.e(this);
               return;
            }
            break;
         case 67:
            if (this.e()) {
               Game.e(this);
               return;
            }
            break;
         case 74:
            this.positionX = Game.player.positionX;
            this.positionY = Game.player.positionY - 35;
            this.facingLeft = Game.player.facingLeft;
            if (Game.player.aq == 0 && Game.player.cI && Game.player.G != 0) {
               this.c &= -129;
               return;
            }

            this.c |= 128;
            return;
      }
   }

   private void di() {
      switch (this.animationId) {
         case 0:
            if (this.e()) {
               if (!bC && bu) {
                  b(0, 69, true);
                  bC = true;
               }

               Game.e(this);
            }
      }
   }

   private void dj() {
      this.aL = 0;
      if (this.animationId >= 1) {
         this.aL = 1;
         if (this.v != null) {
            this.positionY = this.v.collisionBounds[1];
         }
      }

      if (this.e()) {
         Game.e(this);
      }
   }

   private void dk() {
      switch (this.aq) {
         case 102:
            this.cV();
            this.dm();
            if (b(Game.player.collisionBounds, this.collisionBounds)) {
               this.p(105);
            } else if (this.aA <= 0 && b(Game.player.k, this.collisionBounds)) {
               this.p(104);
            }
            break;
         case 103:
            this.cV();
            if (this.aA <= 0) {
               this.p(105);
            } else if (b(Game.player.collisionBounds, this.collisionBounds)) {
               this.p(105);
            } else if (b(Game.player.k, this.collisionBounds)) {
               this.p(104);
            }
            break;
         case 104:
            this.I = this.facingLeft ? 32 : -32;
            if (l(this.H) < 3584) {
               this.J = 512;
            }

            this.dn();
            break;
         case 105:
            if (this.e()) {
               this.H(1000);
            } else {
               if (b(Game.player.collisionBounds, this.collisionBounds)) {
                  Game.player.a(4, 15, 0, this);
               }

               this.dn();
            }
      }

      if (!this.p()) {
         ;
      }
   }

   private void v(int var1, int var2) {
      this.a(16, 3, 0, 0, 0, 101, false);
      aU.p(101);
      this.b(aU);
      aU.a(this.r, var1, var2);
      this.c &= -17;
      aU.c |= 16;
   }

   private void H(int var1) {
      aU = Game.f(this);
      if (aU != null) {
         aU.aB = var1;
         aU.r = null;
      }

      Game.e(this);
   }

   private void w(int var1, int var2) {
      if (this.r != null && this.r.type == 16 && this.r.aq == 101) {
         this.r.p(102);
         this.r.db();
         this.r.G = var1;
         this.r.H = var2;
         this.r.c |= 16;
         this.b((Actor)null);
      }
   }

   private void dl() {
      if (this.r != null && this.r.type == 16 && this.r.aq == 101) {
         this.r.p(105);
      }
   }

   private int a(int var1, int var2, int var3, int var4, int var5, int var6) {
      long var7 = var6 >> 1;
      long var9 = var5;
      long var11 = var2 - var4;
      if (var9 * var9 - 4L * var7 * var11 < 0L) {
         return 0;
      }

      long var13 = a(var9 * var9 - 4L * var7 * var11);
      long var15 = (-var9 - var13) / (2L * var7);
      long var17 = 0L;
      long var19 = 0L;
      var19 = 0L;
      var19 = var15;
      return var15 == 0L ? 0 : (int)((var3 - var1) / var19);
   }

   private void dm() {
      this.I = this.facingLeft ? 32 : -32;
      if (this.H < 3584) {
         this.J = 512;
      } else {
         this.H = 3584;
      }

      if (this.facingLeft) {
         if (this.G > 0) {
            this.G = 0;
         }
      } else if (this.G < 0) {
         this.G = 0;
      }

      if (this.G > 0 && this.eM || this.G < 0 && this.eL) {
         this.facingLeft = !this.facingLeft;
         this.G = -this.G / 2;
      }

      this.b(true, false);
      if (this.H < 0) {
         if (this.eC >= 24) {
            this.positionY = this.positionY + 20 - this.collisionBounds[1] % 20 + 1;
            this.H = -this.H / 2;
            return;
         }
      } else if (this.H >= 0) {
         int var1 = Game.g(this.positionX / 20, this.positionY / 20);
         if (3 == Game.be[Game.levelIndex] && this.positionY >= Game.cB) {
            var1 = 0;
         }

         if (var1 >= 15) {
            this.g();
            this.positionY = (this.collisionBounds[3] + 1) / 20 * 20;
            this.positionY--;

            while (Game.g(this.positionX / 20, this.positionY / 20) >= 24) {
               this.positionY -= 20;
            }

            if (this.H > 2048) {
               this.H = -this.H / 2;
            } else if (Game.j(this.G, this.H) < 1280) {
               this.G = this.H = this.I = this.J = 0;
               this.bL = true;
               if (this.type != 1) {
                  this.p(103);
               }
            } else {
               this.H = 0;
            }

            if (Game.j(this.G, this.H) < 1280) {
               int var2 = 0;
               int var3 = this.positionX % 20 >> 1;
               if (this.eD == 18) {
                  var2 = 0 - var3;
               } else if (this.eD == 19) {
                  var2 = 0 - (var3 + 10);
               } else if (this.eD == 20) {
                  var2 = 0 - (10 - var3 + 10);
               } else if (this.eD == 21) {
                  var2 = 0 - (10 - var3);
               }

               if (this.eD == 16) {
                  var2 -= this.positionX % 20;
               } else if (this.eD == 17) {
                  var2 -= 20 - this.positionX % 20;
               }

               this.positionY -= var2;
               this.G = this.H = this.I = this.J = 0;
               this.bL = true;
               if (this.type != 1) {
                  this.p(103);
               }
            }
         }
      }
   }

   private void dn() {
      if (this.aq != 105) {
         this.b(true, false);
         if (this.eC >= 24 || this.eD >= 24 || this.eE >= 24 || this.eF >= 24) {
            int var1 = this.aV();
            if (this.H >= 0 && this.eD >= 15) {
               this.positionY -= var1;
            }

            this.p(105);
         }
      }

      for (int var3 = 0; var3 < Game.ck; var3++) {
         Actor var2;
         if ((var2 = Game.cj[var3]) != null && (var2.c & 1024) == 0 && var2.aq != 7 && var2.aq != 3 && var2.aq != 5 && var2.aq != 6) {
            if (var2.M() && var2.collisionBounds != null && b(var2.collisionBounds, this.collisionBounds)) {
               var2.s(500);
               if (this.aq != 105) {
                  this.p(105);
               }
            } else if (var2.type == 79 && b(var2.collisionBounds, this.collisionBounds)) {
               var2.a(this.collisionBounds, false, 79);
               if (this.aq != 105) {
                  this.p(105);
               }
            } else if (var2.type == 45 && var2.animationId == 7 && b(var2.collisionBounds, this.collisionBounds)) {
               this.a(var2.collisionBounds, true);
               var2.aq = 100;
            }
         }
      }
   }

   public final void p(int var1) {
      this.c(var1, false);
   }

   private void c(int var1, boolean var2) {
      if (var2 || var1 != this.aq) {
         label1204:
         switch (this.type) {
            case 1:
               switch (var1) {
                  case 1:
                     this.a(1);
                     break label1204;
                  case 2:
                     this.a(4, true);
                     this.dc();
                     this.db();
                     this.G = -this.D(256);
                     break label1204;
                  case 3:
                  case 9:
                     this.a(39, 17, 7, 0, -20, 302, this.facingLeft);
                     break label1204;
                  case 4:
                     this.aB = 0;
                  case 8:
                  default:
                     break label1204;
                  case 101:
                     this.a(1);
                     break label1204;
                  case 102:
                     if (this.ar() == 0) {
                        this.a(2);
                     } else {
                        this.a(3);
                     }

                     this.G = 0;
                     break label1204;
                  case 103:
                     this.a(0);
                     this.db();
                     if (this.ar() == 1) {
                        this.aA = 500;
                     } else {
                        this.aA = 400;
                     }
                     break label1204;
                  case 104:
                     this.a(56, 19, 3, 0, 0, -1, false);
                     aU.p(100);
                     aU.aA = 0;
                     aU.aC = 1;
                     this.s = aU;
                     break label1204;
                  case 105:
                     this.b(7);
                     this.dc();
                     break label1204;
               }
            case 2:
               switch (var1) {
                  case 2:
                     if (this.ar() != 0 && this.ar() != 1) {
                        this.a(8, true);
                     } else {
                        this.a(7, true);
                     }

                     this.dc();
                     this.db();
                     this.G = -this.D(256);
                     break label1204;
                  case 3:
                  case 9:
                     this.a(39, 17, 7, 0, -5, 302, this.facingLeft);
                  case 4:
                  case 5:
                  case 6:
                  case 7:
                  case 8:
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
                  case 35:
                  case 36:
                  case 37:
                  case 38:
                  case 39:
                  case 40:
                  case 41:
                  case 42:
                  case 43:
                  case 44:
                  case 45:
                  case 46:
                  case 47:
                  case 48:
                  case 49:
                  case 50:
                  case 51:
                  case 52:
                  case 53:
                  case 54:
                  case 55:
                  case 56:
                  case 57:
                  case 58:
                  case 59:
                  case 60:
                  case 61:
                  case 62:
                  case 63:
                  case 64:
                  case 65:
                  case 66:
                  case 67:
                  case 68:
                  case 69:
                  case 70:
                  case 71:
                  case 72:
                  case 73:
                  case 74:
                  case 75:
                  case 76:
                  case 77:
                  case 78:
                  case 79:
                  case 80:
                  case 81:
                  case 82:
                  case 83:
                  case 84:
                  case 85:
                  case 86:
                  case 87:
                  case 88:
                  case 89:
                  case 90:
                  case 91:
                  case 92:
                  case 93:
                  case 94:
                  case 95:
                  case 96:
                  case 97:
                  case 98:
                  case 99:
                  case 100:
                  case 140:
                  case 141:
                  case 142:
                  case 143:
                  case 144:
                  case 145:
                  case 146:
                  case 147:
                  case 148:
                  case 149:
                  case 150:
                  case 151:
                  case 152:
                  case 153:
                  case 154:
                  case 155:
                  case 156:
                  case 157:
                  case 158:
                  case 159:
                  case 160:
                  case 161:
                  case 162:
                  case 163:
                  case 164:
                  case 165:
                  case 166:
                  case 167:
                  case 168:
                  case 169:
                  case 170:
                  case 171:
                  case 172:
                  case 173:
                  case 174:
                  case 175:
                  case 176:
                  case 177:
                  case 178:
                  case 179:
                  case 180:
                  case 181:
                  case 182:
                  case 183:
                  case 184:
                  case 185:
                  case 186:
                  case 187:
                  case 188:
                  case 189:
                  case 190:
                  case 191:
                  case 192:
                  case 193:
                  case 194:
                  case 195:
                  case 196:
                  case 197:
                  case 198:
                  case 199:
                  default:
                     break label1204;
                  case 101:
                     this.a(2);
                     break label1204;
                  case 102:
                     this.a(4);
                     this.db();
                     break label1204;
                  case 103:
                     this.a(2);
                     this.aA = 500;
                     break label1204;
                  case 104:
                     this.da();
                     this.a(0);
                     this.db();
                     if (this.cY() < 10240) {
                        this.G = -this.C(1024);
                     } else if (this.cY() > 20480) {
                        this.G = this.C(1024);
                     } else {
                        this.G = this.C(512);
                     }

                     this.aA = 400;
                     break label1204;
                  case 105:
                     this.da();
                     this.a(0);
                     this.db();
                     break label1204;
                  case 106:
                     this.da();
                     this.a(2);
                     this.aA = 700;
                     this.ay = Game.player.a;
                     this.aw = Game.player.b;
                     this.db();
                     break label1204;
                  case 107:
                     this.a(5);
                     this.aA = 600;
                     this.G = (this.ay - this.a) * 50 / 600;
                     this.H = (this.aw - this.b) * 50 / 600;
                     this.ad = this.positionY;
                     break label1204;
                  case 108:
                     this.da();
                     this.a(2);
                     this.aA = 1000;
                     this.ag = ((this.ad << 8) - this.b << 1) * 50 * 50 / 1000000;
                     this.H = 0;
                     break label1204;
                  case 109:
                     this.a(0);
                     this.aA = 500;
                     this.positionY = this.ad;
                     this.ag = this.H = 0;
                     break label1204;
                  case 110:
                     this.a(0);
                     this.aA = 3000;
                     break label1204;
                  case 111:
                     this.a(6);
                     break label1204;
                  case 112:
                     this.a(1);
                     this.aA = 500;
                     break label1204;
                  case 113:
                     this.a(3);
                     this.db();
                     this.aA = 800;
                     break label1204;
                  case 114:
                     dV[0] = this.collisionBounds[0];
                     dV[2] = this.collisionBounds[2];
                     dV[1] = this.collisionBounds[1];
                     dV[3] = this.collisionBounds[3];
                     this.collisionBounds[0] = this.k[0];
                     this.collisionBounds[2] = this.k[2];
                     this.collisionBounds[1] = this.k[1];
                     this.collisionBounds[3] = this.k[3];
                     this.b(true, false);
                     this.collisionBounds[0] = dV[0];
                     this.collisionBounds[2] = dV[2];
                     this.collisionBounds[1] = dV[1];
                     this.collisionBounds[3] = dV[3];
                     if (this.bL || this.eL || this.eM) {
                        this.c(112, true);
                        return;
                     }

                     if (this.r != null && this.r.aq == 101) {
                        this.a(6);
                        this.w(0, 0);
                        this.db();
                     }
                     break label1204;
                  case 115:
                     this.a(6);
                     this.aA = 2000;
                     this.db();
                     break label1204;
                  case 116:
                     this.a(3);
                     this.facingLeft = this.fx;
                     this.G = this.fx ? -384 : 384;
                     break label1204;
                  case 117:
                     this.a(1);
                     this.dc();
                     this.db();
                     break label1204;
                  case 118:
                     this.a(3);
                     this.aA = 1700;
                     this.eX = Game.h(this.a - Game.player.a, this.b - Game.player.b);
                     this.aw = this.eX == 0 ? 0 : this.a - Game.player.a;
                     this.az = this.eX == 0 ? 0 : this.b - Game.player.b;
                     this.ax = this.eX;
                     break label1204;
                  case 119:
                     this.b(2);
                     this.H = this.aK == 4 ? 3072 : -3072;
                     break label1204;
                  case 120:
                     this.db();
                     this.aA = 500;
                     break label1204;
                  case 121:
                     this.a(16);
                     this.fx = false;
                     this.aA = 600;
                     break label1204;
                  case 122:
                     this.a(2);
                     this.aA = 400;
                     break label1204;
                  case 123:
                     this.G = this.facingLeft ? 3072 : -3072;
                     break label1204;
                  case 124:
                     this.b(3);
                     this.G = this.aK == 1 ? 1536 : -1536;
                     if (this.r == null) {
                        this.v(this.C(3), 6);
                        aU.M = true;
                     }
                     break label1204;
                  case 125:
                     this.db();
                     this.aA = 500;
                     break label1204;
                  case 126:
                     if (this.r != null && this.r.aq == 101) {
                        this.a(6);
                        this.w(0, 0);
                        this.db();
                     }
                     break label1204;
                  case 127:
                     this.aA = 400;
                     break label1204;
                  case 128:
                     this.G = this.facingLeft ? -2560 : 2560;
                     break label1204;
                  case 129:
                     this.b(2);
                     this.H = this.aK == 4 ? 1024 : -1024;
                     break label1204;
                  case 130:
                     this.db();
                     break label1204;
                  case 131:
                     this.b(5);
                     this.dc();
                     this.eX = Game.h(Game.player.eA - this.positionX, Game.player.eB - this.positionY);
                     this.G = this.eX == 0 ? 0 : (Game.player.eA - this.positionX) * 2560 / this.eX;
                     this.H = this.eX == 0 ? 0 : (Game.player.eB - this.positionY) * 2560 / this.eX;
                     break label1204;
                  case 132:
                     this.aA = 1500;
                     break label1204;
                  case 133:
                     this.facingLeft = !this.facingLeft;
                     this.G = this.facingLeft ? -384 : 384;
                     this.H = -384;
                     break label1204;
                  case 134:
                     this.b(2);
                     this.G = this.aK == 1 ? 1536 : -1536;
                     this.aA = 2000;
                     break label1204;
                  case 135:
                     this.b(2);
                     this.ac = this.positionX;
                     this.ad = this.positionY;
                     this.aA = 2000;
                     break label1204;
                  case 136:
                     this.b(2);
                     break label1204;
                  case 137:
                     this.b(4);
                     break label1204;
                  case 138:
                     this.b(2);
                     break label1204;
                  case 139:
                     this.b(4);
                     break label1204;
                  case 200:
                     this.a(2);
                     this.db();
                     this.aA = 500;
                     break label1204;
               }
            case 3:
               switch (var1) {
                  case 2:
                  case 4:
                     this.a(13, true);
                     this.dc();
                     this.db();
                     this.G = -this.D(256);
                     break label1204;
                  case 3:
                  case 9:
                     this.a(39, 17, 7, 0, -5, 302, this.facingLeft);
                  case 8:
                  default:
                     break label1204;
                  case 101:
                     this.da();
                     this.a(10);
                     break label1204;
                  case 102:
                     this.a(15);
                     this.aA = 700;
                     this.db();
                     break label1204;
                  case 103:
                     this.a(12);
                     this.G = this.facingLeft ? -2048 : 2048;
                     this.aA = 1000;
                     break label1204;
                  case 104:
                     this.a(9);
                     this.aA = 500;
                     this.db();
                     break label1204;
                  case 105:
                     this.a(10);
                     this.aA = 1500;
                     break label1204;
                  case 106:
                     this.a(14);
                     this.db();
                     break label1204;
                  case 107:
                     this.aA = 1800;
                     this.a(9);
                     this.db();
                     break label1204;
                  case 108:
                     this.a(12);
                     this.facingLeft = this.fx;
                     this.G = this.fx ? -384 : 384;
                     break label1204;
                  case 109:
                     this.a(9);
                     this.facingLeft = !this.fx;
                     this.db();
                     break label1204;
               }
            case 4:
               switch (var1) {
                  case 2:
                     this.a(3);
                     this.dc();
                     this.db();
                     this.G = -this.D(1024);
                     this.aA = 500;
                     break label1204;
                  case 3:
                     this.a(4);
                     this.db();
                     break label1204;
                  case 101:
                     this.a(1);
                     break label1204;
                  case 102:
                     this.a(0);
                     this.db();
                     break label1204;
                  case 103:
                     this.a(2);
                     this.db();
                     break label1204;
                  case 104:
                     this.a(1);
                     this.aA = 700;
                     this.db();
                  default:
                     break label1204;
               }
            case 5:
               byte var3 = 0;
               if (this.aK == 8) {
                  var3 = 0;
               } else if (this.aK == 4) {
                  var3 = 4;
               } else {
                  var3 = 2;
               }

               switch (var1) {
                  case 3:
                     if (this.aK == 8) {
                        this.a(1);
                     } else if (this.aK == 4) {
                        this.a(5);
                     } else {
                        this.a(3);
                     }

                     this.db();
                     break;
                  case 101:
                     this.a((int)var3);
                     this.h = Game.i(0, this.p.a(this.animationId) - 1);
                     break;
                  case 102:
                     this.aA = this.B(3);
                     this.c |= 128;
                     break;
                  case 103:
                     this.a((int)var3);
                     this.m(this.aK, 103);
                     this.c &= -129;
                     break;
                  case 104:
                     this.aA = this.ay;
                     this.db();
                     break;
                  case 105:
                     this.a((int)var3);
                     this.m(this.aK, 105);
                     break;
                  case 106:
                     this.aA = this.aw;
                     this.c |= 128;
                     this.db();
               }
            case 6:
            case 7:
            case 12:
            case 17:
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
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 53:
            case 55:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 69:
            case 71:
            case 72:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 91:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            default:
               break;
            case 8:
               switch (var1) {
                  case 3:
                     this.a(1);
                     this.aA = 2500;
                     this.a(
                        39,
                        17,
                        7,
                        Game.i(this.collisionBounds[0] - this.collisionBounds[2] >> 1, this.collisionBounds[2] - this.collisionBounds[0] >> 1),
                        Game.i(this.collisionBounds[1] - this.collisionBounds[3] >> 1, this.collisionBounds[3] - this.collisionBounds[1] >> 1),
                        302,
                        this.facingLeft
                     );
                     break label1204;
                  case 102:
                     this.aA = 2000;
                     this.db();
                     break label1204;
                  case 103:
                     this.aA = 700;
                     this.ay = Game.player.eA;
                     this.av = Game.player.eB;
                     this.a(39, 17, 30, 0, 0, 302, false);
                     this.v = aU;
                     break label1204;
                  case 104:
                     this.aA = 1500;
                     this.ax = this.ay == Game.player.eA ? -this.C(5) : (this.ay > Game.player.eA ? -5 : 5);
                     this.a(39, 17, 29, 0, 0, 302, false);
                     this.u = aU;
                     break label1204;
                  case 105:
                     this.fu = 0;
                     this.aA = 1000;
                     Game.e(this.u);
                     this.u = null;
                     Game.e(this.v);
                     this.v = null;
                     break label1204;
                  case 106:
                     this.aA = 1000;
                     this.av = this.positionY;
                     this.aB = 0;
                     break label1204;
                  case 107:
                     this.a(2);
                     this.db();
                     this.aA = 1000;
                     this.ay = this.positionX;
                     this.av = this.positionY;
                     break label1204;
                  case 108:
                     this.a(4);
                     this.az = 0;
                     this.H = 5120;
                     break label1204;
                  case 109:
                     this.a(5);
                     this.aA = 1500;
                     this.db();
                     this.O = true;
                     this.P = true;
                     break label1204;
                  case 110:
                     this.a(3);
                     this.H = -1536;
                     this.O = false;
                     this.P = false;
                     if (this.as < F * 2 / 3) {
                        this.n = new int[2];
                        this.n[0] = this.positionX;
                        this.n[1] = this.positionY;
                        this.aw = this.positionX - this.ac > 0 ? -1 : 1;
                        this.H = 0;
                        this.fu = 0;
                        this.ay = (this.ac - this.positionX) * 1000 / 1732;
                        this.av = (this.ad - this.positionY) * 1000 / 1732;
                        int var4 = Game.l(30);
                        int var15 = Game.m(30 * -this.aw);
                        int var16 = this.ay * var4 - this.av * var15 >> 8;
                        int var17 = this.ay * var15 + this.av * var4 >> 8;
                        this.ay = var16;
                        this.av = var17;
                        this.ay = this.ay + this.positionX;
                        this.av = this.av + this.positionY;
                        this.aB = 20;
                     }
                     break label1204;
                  case 112:
                     this.db();
                     this.s.positionX = Game.i(0, 2) == 0 ? cV : cW;
                     this.t.positionX = this.s.positionX == cV ? (Game.i(0, 2) == 0 ? cW : cX) : cX;
                     this.s.p(107);
                     this.t.p(107);
                     break label1204;
                  case 113:
                     this.db();
                     this.fu = 0;
                     this.n = new int[2];
                     this.aw = this.positionX - Game.player.positionX > 0 ? 1 : -1;
                     this.fv = 30;
                     this.ay = this.positionX + this.aw * this.fv;
                     this.av = this.positionY;
                     this.n[0] = this.ay + this.aw * this.fv + Game.player.positionX >> 1;
                     this.n[1] = l(Game.player.positionX - this.n[0]);
                     this.aB = 20;
                  default:
                     break label1204;
               }
            case 9:
               switch (var1) {
                  case 101:
                     this.a(0);
                     this.G = this.facingLeft ? -(this.B(2) << 8) : this.B(2) << 8;
                     this.aA = this.B(1);
                     if (this.r != null && this.r.type == 51) {
                        Game.a(5, 5);
                        this.r.aq = 100;
                     }
                     break label1204;
                  case 102:
                     this.a(1);
                     if (this.r != null && this.r.type == 54) {
                        this.r.p(100);
                        Game.a(5, 5);
                     }
                     break label1204;
                  case 103:
                     this.aA = 0;
                     break label1204;
                  case 104:
                     this.a(1);
                     this.c &= -129;
                     break label1204;
                  case 105:
                     this.a(2);
                     break label1204;
                  case 106:
                     this.db();
                     break label1204;
                  case 107:
                     this.a(56, 19, 0, 0, this.aB - this.collisionBounds[3] + -7, -1, false);
                     aU.p(100);
                     aU.aA = 0;
                     this.s = aU;
                     this.c |= 128;
                     break label1204;
                  case 108:
                     this.H = -5120;
                     this.c &= -129;
                     Game.a(5, 5);
                     break label1204;
                  case 109:
                     this.db();
                     this.aA = 800;
                     break label1204;
                  case 110:
                     this.a(1);
                     this.H = 2560;
                     break label1204;
                  case 111:
                     this.db();
                     this.aA = this.B(1);
                     break label1204;
                  case 112:
                     this.a(5);
                     this.db();
                     this.aA = 1500;
                     this.ax = 6;
                  default:
                     break label1204;
               }
            case 10:
               switch (var1) {
                  case 101:
                     this.aA = this.B(3);
                     break label1204;
                  case 102:
                     this.a(this.aK == 8 ? 1 : (this.aK == 4 ? 9 : 5));
                     break label1204;
                  case 103:
                     this.a(this.aK == 8 ? 2 : (this.aK == 4 ? 10 : 6));
                     this.aA = 500;
                     break label1204;
                  case 104:
                     this.a(this.aK == 8 ? 3 : (this.aK == 4 ? 11 : 7));
                     this.aA = this.B(1);
                     this.aF = 1;
                     break label1204;
                  case 105:
                     this.a(this.aK == 8 ? 1 : (this.aK == 4 ? 9 : 5));
                     this.aF = 0;
                     break label1204;
                  case 106:
                     this.a(this.aK == 8 ? 0 : (this.aK == 4 ? 8 : 4));
                     this.aA = this.B(2);
                     break label1204;
                  case 107:
                  case 109:
                     this.a(this.aK == 8 ? 3 : (this.aK == 4 ? 11 : 7));
                     this.aA = this.B(1);
                     break label1204;
                  case 108:
                  case 110:
                     this.aA = this.B(2);
                  default:
                     break label1204;
               }
            case 11:
               switch (var1) {
                  case 1:
                     this.a(24);
                     break;
                  case 2:
                     this.a(3, true);
                     this.db();
                     this.aA = 300;
                     this.az++;
                     this.G = -this.D(256);
                     break;
                  case 3:
                  case 9:
                     if (dh < 3 && this.ar() == 1) {
                        return;
                     }

                     this.a(3);
                     this.db();
                     this.aA = 2500;
                     this.a(
                        39,
                        17,
                        7,
                        Game.i(this.collisionBounds[0] - this.collisionBounds[2] >> 1, this.collisionBounds[2] - this.collisionBounds[0] >> 1),
                        Game.i(this.collisionBounds[1] - this.collisionBounds[3] >> 1, this.collisionBounds[3] - this.collisionBounds[1] >> 1),
                        302,
                        this.facingLeft
                     );
                     Game.player.au();
                     break;
                  case 4:
                     this.a(28);
                  case 5:
                  case 6:
                  case 7:
                  case 8:
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
                  case 35:
                  case 36:
                  case 37:
                  case 38:
                  case 39:
                  case 40:
                  case 41:
                  case 42:
                  case 43:
                  case 44:
                  case 45:
                  case 46:
                  case 47:
                  case 48:
                  case 49:
                  case 50:
                  case 51:
                  case 52:
                  case 53:
                  case 54:
                  case 55:
                  case 56:
                  case 57:
                  case 58:
                  case 59:
                  case 60:
                  case 61:
                  case 62:
                  case 63:
                  case 64:
                  case 65:
                  case 66:
                  case 67:
                  case 68:
                  case 69:
                  case 70:
                  case 71:
                  case 72:
                  case 73:
                  case 74:
                  case 75:
                  case 76:
                  case 77:
                  case 78:
                  case 79:
                  case 80:
                  case 81:
                  case 82:
                  case 83:
                  case 84:
                  case 85:
                  case 86:
                  case 87:
                  case 88:
                  case 89:
                  case 90:
                  case 91:
                  case 92:
                  case 93:
                  case 94:
                  case 95:
                  case 96:
                  case 97:
                  case 98:
                  case 99:
                  case 100:
                  case 112:
                  case 131:
                  case 132:
                  case 133:
                  case 134:
                  case 135:
                  case 136:
                  case 137:
                  case 138:
                  case 139:
                  case 140:
                  case 141:
                  case 142:
                  case 143:
                  case 144:
                  case 145:
                  case 146:
                  case 147:
                  case 148:
                  case 149:
                  default:
                     break;
                  case 101:
                     this.aA = 400;
                     this.b(0);
                     this.db();
                     di = false;
                     break;
                  case 102:
                     this.db();
                     this.a(14);
                     dn = 110;
                     dm = false;
                     break;
                  case 103:
                     this.a(5);
                     this.G = this.facingLeft ? -1792 : 1792;
                     this.aA = 500;
                     break;
                  case 104:
                     this.a(8);
                     this.db();
                     break;
                  case 105:
                     this.a(9);
                     this.aA = 1000;
                     break;
                  case 106:
                     this.a(6);
                     this.db();
                     if (Game.player.animationId != 12) {
                        this.b(Game.player);
                        this.r.b(11);
                        aM = true;
                        ez = false;
                        this.ae = true;
                     }
                     break;
                  case 107:
                     this.a(7);
                     this.aA = 4500;
                     this.aB = 0;
                     this.ax = 0;
                     break;
                  case 108:
                     this.a(4);
                     if (dg == 3) {
                        dn = 125;
                     } else {
                        dn = 103;
                     }
                     break;
                  case 109:
                     this.a(10);
                     break;
                  case 110:
                     this.a(11);
                     this.G = this.facingLeft ? -3328 : 3328;
                     this.O = false;
                     Game.b(this);
                     break;
                  case 111:
                     this.db();
                     this.aA = 1000;
                     this.P = false;
                     break;
                  case 113:
                     this.a(12);
                     this.db();
                     this.aA = 3500;
                     this.aB = 1500;
                     dl = 2500;
                     this.a(56, 19, 10, -10, 0, 302, false);
                     aU.p(102);
                     aU.aL = 0;
                     this.O = true;
                     break;
                  case 114:
                     this.a(13);
                     this.G = this.facingLeft ? 768 : -768;
                     break;
                  case 115:
                     this.a(16);
                     break;
                  case 116:
                     if (this.ar() != 0 && dk != null) {
                        this.p(101);
                        return;
                     }

                     this.a(17);
                     this.aA = 100;
                     this.fx = false;
                     break;
                  case 117:
                     this.a(0);
                     this.aA = 1000;
                     break;
                  case 118:
                     this.a(22);
                     break;
                  case 119:
                     this.a(23);
                     this.G = this.facingLeft ? 2560 : -2560;
                     this.H = -1024;
                     break;
                  case 120:
                     this.a(24);
                     break;
                  case 121:
                     this.a(25);
                     this.db();
                     break;
                  case 122:
                     this.a(0);
                     break;
                  case 123:
                     this.a(17);
                     this.aA = 100;
                     break;
                  case 124:
                     this.a(4);
                     this.aA = 1000;
                     dh++;
                     break;
                  case 125:
                     this.a(22);
                     this.db();
                     this.av = this.positionY + -130;
                     this.ac = Game.player.positionX;
                     this.ad = Game.player.positionY;
                     break;
                  case 126:
                     this.a(23);
                     this.G = this.facingLeft ? 2048 : -2048;
                     this.H = -1792;
                     break;
                  case 127:
                     this.a(26);
                     break;
                  case 128:
                     this.a(18);
                     this.db();
                     this.G = this.facingLeft ? 2048 : -2048;
                     break;
                  case 129:
                     this.a(19);
                     this.H = 2048;
                     this.G = this.a(this.a, -this.b, this.ac << 8, -(this.ad << 8), -this.H, -320);
                     if (l(this.G) > 4864) {
                        this.G = l(this.G) / this.G * 4864;
                     }

                     di = false;
                     break;
                  case 130:
                     this.a(20);
                     this.db();
                     this.aA = 100;
                     break;
                  case 150:
                     this.db();
                     this.a(44);
                     this.dc();
                     break;
                  case 151:
                     this.a(5);
                     this.aA = 500;
                     this.G = this.facingLeft ? -1792 : 1792;
                     break;
                  case 152:
                     this.a(15);
                     this.db();
                     break;
                  case 153:
                     this.db();
                     this.a(39);
                     this.aA = 2000;
                     break;
                  case 154:
                     this.a(0);
                     break;
                  case 155:
                     this.a(22);
                     break;
                  case 156:
                     this.a(23);
                     this.G = this.facingLeft ? 1536 : -1536;
                     this.H = -768;
                     break;
                  case 157:
                     this.a(24);
                     break;
                  case 158:
                     this.db();
                     this.a(25);
                     break;
                  case 159:
                     this.a(30);
               }

               if (var1 == 113) {
                  aE = false;
               } else {
                  aE = true;
               }
               break;
            case 13:
               switch (var1) {
                  case 2:
                     this.a(2);
                     this.db();
                     this.p(1536, -256);
                     if (this.ar() == 1) {
                        this.aA = 1000;
                     }
                     break label1204;
                  case 3:
                  case 9:
                     this.a(2);
                     this.a(39, 17, 7, 0, -20, 302, this.facingLeft);
                  case 8:
                  default:
                     break label1204;
                  case 101:
                     this.a(1);
                     break label1204;
                  case 102:
                     this.a(2);
                     this.aA = this.B(3);
                     break label1204;
                  case 103:
                     this.a(0);
                     break label1204;
                  case 104:
                     this.a(0);
                     break label1204;
                  case 105:
                     this.a(3);
                     break label1204;
                  case 106:
                     this.a(0);
                     break label1204;
                  case 107:
                     this.a(3);
                     break label1204;
               }
            case 14:
               switch (var1) {
                  case 2:
                     if (this.animationId != 2 && this.animationId != 3) {
                        this.a(8, true);
                     }

                     this.dc();
                     this.db();
                     this.G = -this.D(256);
                     break label1204;
                  case 3:
                     this.N = false;
                     this.a(7);
                     this.dc();
                     this.db();
                     break label1204;
                  case 101:
                     this.a(1);
                     break label1204;
                  case 102:
                     this.a(4);
                     this.db();
                     this.aB = 100;
                     this.fx = false;
                     break label1204;
                  case 103:
                     this.a(0);
                     this.aA = 800;
                     this.db();
                     break label1204;
                  case 104:
                     this.a(2);
                     this.db();
                     break label1204;
                  case 105:
                     this.a(3);
                     this.db();
                     this.H = 512;
                     this.N = true;
                     break label1204;
                  case 106:
                     this.db();
                     break label1204;
                  case 107:
                     this.N = false;
                     this.a(61, 38, 2, 0, 0, 302, false);
                  default:
                     break label1204;
               }
            case 15:
               switch (var1) {
                  case 104:
                     this.a(1);
                  default:
                     break label1204;
               }
            case 16:
               switch (var1) {
                  case 102:
                     this.a(1);
                     this.aA = 800;
                     break label1204;
                  case 103:
                     this.a(2);
                     this.aA = 3000;
                     break label1204;
                  case 104:
                     this.a(2);
                     this.G = -this.C(3328);
                     this.H = -3328;
                     break label1204;
                  case 105:
                     this.b(3);
                     this.db();
                  default:
                     break label1204;
               }
            case 18:
               switch (var1) {
                  case 3:
                     this.a(6);
                     if (this.s == Game.player) {
                        this.s.t = null;
                     }

                     this.s = null;
                     this.t = null;
                     this.db();
                     break label1204;
                  case 101:
                     this.a(5);
                     this.cm();
                     this.aA = 5000;
                  default:
                     break label1204;
               }
            case 19:
               switch (var1) {
                  case 101:
                     this.G = this.facingLeft ? -3584 : 3584;
                  default:
                     break label1204;
               }
            case 52:
               switch (var1) {
                  case 103:
                     this.aA = 500;
                  default:
                     break label1204;
               }
            case 54:
               switch (var1) {
                  case 100:
                     this.b(1);
                     this.aA = 0;
                     this.aB = 0;
                     break label1204;
                  case 101:
                     Game.I(-1);
                     this.a(2);
                     break label1204;
                  case 102:
                     this.a(3);
                  default:
                     break label1204;
               }
            case 56:
               switch (var1) {
                  case 100:
                     Game.e(2);
                  default:
                     break label1204;
               }
            case 68:
               switch (var1) {
                  case 101:
                     int var14 = 0;
                     int var6 = 0;
                     switch (this.aw) {
                        case 1:
                           var14 = Game.viewBounds[0] - this.positionX + 18;
                           break;
                        case 2:
                           var14 = Game.viewBounds[2] - this.positionX - 36;
                        case 3:
                        case 5:
                        case 6:
                        case 7:
                        default:
                           break;
                        case 4:
                           var6 = Game.viewBounds[1] - this.positionY + 18;
                           break;
                        case 8:
                           var6 = Game.viewBounds[3] - this.positionY - 18;
                     }

                     if (this.r == null) {
                        Actor var7;
                        (var7 = this.a(21, 16, 14, var14, var6, 100, false)).aC = 1;
                        var7.ay = var7.positionX - Game.cameraX;
                        var7.av = var7.positionY - Game.cameraY;
                        this.b(var7);
                     }
                     break label1204;
                  case 102:
                     switch (this.ar()) {
                        case 11:
                        case 13:
                        default:
                           break label1204;
                        case 12:
                           this.aA = 600;
                           break label1204;
                        case 14:
                        case 15:
                           this.aA = 300;
                           break label1204;
                     }
                  case 103:
                     switch (this.ar()) {
                        case 11:
                        case 13:
                        default:
                           break label1204;
                        case 12:
                           this.aA = 400;
                     }
                  case 104:
                  default:
                     break label1204;
               }
            case 70:
               this.da();
               switch (var1) {
                  case 0:
                     this.c &= -129;
                     break label1204;
                  case 2:
                     this.H = -1700;
                     if (this.r == null) {
                        this.F(1200);
                        this.G = -this.C(1200);
                     } else {
                        if (l(this.r.a - this.a) > l(1200)) {
                           this.facingLeft = this.r.positionX < this.positionX;
                        }

                        this.G = (this.a - this.r.a < 0 ? -1 : 1) * 1200;
                        this.r = null;
                     }

                     if (this.aH == 1) {
                        if (this.aq == 106) {
                           this.ax = 1;
                        } else {
                           this.a(1);
                           this.ax = 0;
                        }
                     } else if (this.aH == 3) {
                        this.aJ = null;
                        this.O = true;
                        this.aH = 1;
                     }
                     break label1204;
                  case 101:
                     this.G = this.D(1200);
                     this.ay = 0;
                     this.aA = 0;
                     this.a(5);
                     break label1204;
                  case 102:
                     this.positionY = (this.positionY + 10) / 20 * 20;
                     this.db();
                     this.a(4);
                     break label1204;
                  case 103:
                     this.positionY = (this.positionY + 10) / 20 * 20;
                     this.a(7);
                     if (this.fx) {
                        this.aA = 400;
                        this.fu = 0;
                     } else {
                        this.aA = 2000;
                     }
                     break label1204;
                  case 104:
                     this.a(2);
                     break label1204;
                  case 105:
                     this.aA = 1024;
                     this.positionY = (this.positionY + 10) / 20 * 20;
                     this.a(0);
                     break label1204;
                  case 106:
                     this.a(5);
                     break label1204;
                  case 107:
                     this.db();
                     this.a(1);
                     break label1204;
                  case 108:
                     this.db();
                     this.a(3);
                     break label1204;
                  case 109:
                     this.a(8);
                     break label1204;
                  case 110:
                     this.db();
                     this.positionY = (this.positionY + 10) / 20 * 20;
                     this.a(9);
                     break label1204;
                  case 111:
                     this.ax = 0;
                     this.db();
                     this.aA = 200;
                     this.b(2);
                     this.positionY += 27;
                     break label1204;
                  case 112:
                     this.db();
                     if (Game.g(this.positionX / 20, this.positionY / 20 - 1) >= 15) {
                        this.H = -425;
                     } else {
                        this.H = -3400;
                     }

                     if (this.aq != 111) {
                        this.ax = 0;
                        this.a(2);
                     } else {
                        this.ax = 1;
                     }
                     break label1204;
                  case 113:
                     this.db();
                     this.b(10);
                     this.positionY -= 27;
                     break label1204;
                  case 114:
                     this.a(6);
                     this.db();
                     this.n = new int[10];
                     this.n[8] = 0;
                     this.n[5] = this.n[4] = 1024;
                     this.n[0] = this.n[1] = 45;
                     this.n[3] = this.n[2] = 32;
                     this.aA = 0;
                     this.n[6] = this.aB = 1;
                     this.ay = 1;
                     this.n[7] = this.ax = this.n[4];
                     break label1204;
                  case 115:
                     this.db();
                     this.ax = 0;
                     this.aw = this.positionX;
                     this.az = this.positionY;
                     this.b(2);
                     this.H = 850;
                     this.positionY += 27;
                     break label1204;
                  default:
                     this.db();
                     break label1204;
               }
            case 73:
               switch (var1) {
                  case 100:
                     this.b(6);
                     this.aL = 0;
                     Game.b(this);
                     break label1204;
                  case 101:
                     this.aA = 2000 + this.av;
                     this.c |= 128;
                     break label1204;
                  case 102:
                     this.b(2);
                     this.aL = 1;
                     this.c &= -129;
                     if (E == null || E.type != 100) {
                        Game.c(this);
                     }

                     this.N = true;
                     if (this.az == 1) {
                        this.v = this.a(-3, 21, 10, 0, 0, 0, false);
                     }
                     break label1204;
                  case 103:
                     Game.I(-1);
                     this.a(98, 19, 9, 0, 0, -1, false);
                     aU.aL = 2;
                     aU.c |= 16;
                     this.db();

                     for (int var13 = 0; var13 < 4; var13++) {
                        this.a(39, 17, 1, 0, 0, 302, this.facingLeft);
                        aU.positionX = (this.collisionBounds[0] + this.collisionBounds[2] >> 1)
                           + ((var13 % 2 << 1) + -1) * Game.i(0, this.collisionBounds[0] - this.collisionBounds[2] >> 1);
                        aU.positionY = (this.collisionBounds[1] + this.collisionBounds[3] >> 1)
                           + ((var13 % 2 << 1) + -1) * Game.i(0, this.collisionBounds[1] - this.collisionBounds[3] >> 1);
                     }

                     Game.a(12, 8);
                     Game.e(this.v);
                     this.v = null;
                     break label1204;
                  case 104:
                     this.b(3);
                     this.g();
                     if (Game.be[Game.levelIndex] != 2) {
                        if (b(this.collisionBounds, Game.viewBounds) && this.aC != 1) {
                           if (this.facingLeft) {
                              this.positionX = Game.viewBounds[2] + (this.collisionBounds[2] - this.collisionBounds[0] >> 1);
                           } else {
                              this.positionX = Game.viewBounds[0] - (this.collisionBounds[2] - this.collisionBounds[0] >> 1);
                           }
                        }

                        this.g();
                        this.aG();
                     }
                  case 105:
                     this.b(3);
                     this.az = 1;
                     this.aL = 1;
                     this.v = this.a(-3, 21, 11, 0, 0, this.ap + 1, false);
                     this.c |= 16;
                  default:
                     break label1204;
               }
            case 83:
               switch (var1) {
                  case 101:
                     this.db();
                     this.aA = 1000;
                     this.a(98, 19, 10, -50, 30, 302, true);
                     aU.positionX = this.aK == 1 ? dK - 15 : dJ + 15;
                     aU.aL = 2;
                     Game.a(4, 0);
                     break;
                  case 102:
                     this.G = this.facingLeft ? -2048 : 2048;
                     this.fx = false;
                     this.i(true);
                     Game.a(6, 0);
                     break;
                  case 103:
                     this.db();
                     this.aA = 1000;
                     this.aB = 3;
                     dF = this.aK == 2;
                     break;
                  case 104:
                     this.db();
                     this.ac = this.positionX;
                     this.ad = this.positionY;
                     if (this.aK == 1 || this.aK == 2) {
                        this.G = this.aK == 1 ? -this.y(1280) : this.y(1280);
                     }

                     this.aB--;
                     break;
                  case 105:
                     dB = 42;
                     dC = this.y(8);
                     dE = dF;
                     dG = 0;
                     if (this.aK == 1) {
                        dz = this.positionX;
                        dA = this.positionY + (dE ? -dB : dB);
                        dD = dE ? 90 : -90;
                     } else if (this.aK == 2) {
                        dz = this.positionX;
                        dA = this.positionY - (dE ? -dB : dB);
                        dD = dE ? -90 : 90;
                     }
                     break;
                  case 106:
                     this.aA = 1000;
                     this.db();
                  case 107:
                  case 109:
                  case 120:
                  case 142:
                  case 143:
                  case 144:
                  case 145:
                  case 146:
                  case 147:
                  case 148:
                  case 149:
                  case 150:
                  case 153:
                  case 155:
                  default:
                     break;
                  case 108:
                     this.aA = 1000;
                     break;
                  case 110:
                     this.aA = 100;
                     break;
                  case 111:
                     this.ac = this.positionX;
                     this.ad = this.positionY;
                     break;
                  case 112:
                     this.db();
                     break;
                  case 113:
                     this.aA = 500;
                     this.db();
                     this.aK = 4;
                     this.bZ();
                     break;
                  case 114:
                     this.aA = 1500;
                     fe = Game.player.positionX <= this.aw + 30 ? this.aw : (Game.player.positionX >= this.ax - 30 ? this.ax : this.ay);
                     ff = Game.player.positionX <= this.aw + 30 ? 0 : (Game.player.positionX >= this.ax - 30 ? 2 : 1);
                     this.positionX = fe;
                     this.aB = 3;
                     this.i(false);
                     break;
                  case 115:
                     this.H = this.aK == 4 ? -this.y(2560) : this.y(2560);
                     this.aB--;
                     break;
                  case 116:
                     if (this.aB == 0) {
                        dB = 60;
                        dE = this.positionX < this.ay;
                     } else if (ff == 0 || ff == 2) {
                        dB = 30;
                        if (ff == 0) {
                           dE = this.aB % 2 == 0;
                        } else {
                           dE = this.aB % 2 == 1;
                        }
                     } else if (ff == 1) {
                        dB = this.aB == 1 ? 60 : 30;
                        if (this.aB == 2) {
                           dE = Game.player.positionX > this.ay;
                        }
                     }

                     if (this.aK == 4) {
                        dD = dE ? 180 : 0;
                        dz = this.positionX + (dE ? 1 : -1) * dB;
                     } else if (this.aK == 8) {
                        dD = dE ? 0 : 180;
                        dz = this.positionX - (dE ? 1 : -1) * dB;
                     }

                     dC = this.y(15);
                     dG = 0;
                     dA = this.positionY;
                     break;
                  case 117:
                     this.db();
                     dB = 60;
                     if (dN == 0) {
                        dE = this.positionX > this.ay;
                     } else {
                        dE = false;
                     }

                     dD = dE ? 0 : 180;
                     dz = this.positionX - (dE ? 1 : -1) * dB;
                     dC = 5;
                     dG = 0;
                     dA = this.positionY;
                     break;
                  case 118:
                     this.i(true);
                     Game.h(9);
                     break;
                  case 119:
                     this.z(dN);
                     if (dN == 0) {
                        dL.br();
                     } else {
                        dM.br();
                     }

                     Game.h(12);
                     break;
                  case 121:
                     this.G = this.aK == 1 ? -this.y(1536) : 1536;
                     break;
                  case 122:
                     this.db();
                     this.aK = this.aK == 1 ? 2 : 1;
                     this.G = this.aK == 1 ? -1536 : 1536;
                     break;
                  case 123:
                     this.db();
                     this.aK = this.aK == 1 ? 2 : 1;
                     break;
                  case 124:
                     this.db();
                     this.aK = this.positionX <= Game.player.positionX ? 1 : 2;
                     this.G = this.aK == 1 ? -3072 : 3072;
                     break;
                  case 125:
                     this.db();
                     this.aK = this.aK == 1 ? 2 : 1;
                     this.G = this.aK == 1 ? -2048 : 2048;
                     Game.a(6, 0);
                     break;
                  case 126:
                     this.db();
                     this.aA = 1000;
                     this.aB = 3;
                     break;
                  case 127:
                     this.db();
                     this.ac = (this.aK == 1 ? fj - -50 : fk + -50) - this.positionX;
                     this.ad = 110;
                     this.G = this.aK == 1 ? -this.y(1536) : this.y(1536);
                     this.H = this.G * this.ad / this.ac;
                     this.aB--;
                     break;
                  case 128:
                     dB = 55;
                     dC = this.y(8);
                     dE = this.aK == 1;
                     dG = 0;
                     dz = this.positionX;
                     dA = this.positionY - dB;
                     dD = 90;
                     break;
                  case 129:
                     this.db();
                     this.aA = 1000;

                     for (int var11 = 0; var11 < 7; var11++) {
                        fb[var11].aH = 4;
                        fb[var11].aq = 106;
                        fb[var11].aA = 1;
                     }
                     break;
                  case 130:
                     this.aA = 3000;
                     this.aB = 7;
                     break;
                  case 131:
                     this.aA = 1000;
                     break;
                  case 132:
                     this.G = this.aK == 1 ? -this.y(1536) : this.y(1536);
                     break;
                  case 133:
                     dB = 45;
                     dC = this.y(8);
                     dE = this.aK == 1;
                     dG = 0;
                     dz = this.positionX;
                     dA = this.positionY - dB;
                     dD = 90;
                     break;
                  case 134:
                     this.db();
                     this.H = this.y(2048);
                     this.aK = 8;
                     break;
                  case 135:
                     this.db();
                     this.aA = 0;
                     this.positionY += 10;
                     Game.e(6);
                     this.cc();
                     break;
                  case 136:
                     this.aA = 3000;
                     break;
                  case 137:
                     this.db();
                     break;
                  case 138:
                     this.db();
                     break;
                  case 139:
                     this.db();
                     break;
                  case 140:
                     this.aK = 4;
                     break;
                  case 141:
                     this.db();
                     this.aK = Game.i(0, 2) == 0 ? 1 : 2;
                     this.G = this.aK == 1 ? -2048 : 2048;
                     break;
                  case 151:
                  case 154:
                     this.ac = this.positionX;
                     this.ad = this.positionY + (this.ay * Game.m(-this.ax) >> 8);
                     break;
                  case 152:
                     dB = this.ay;
                     dC = this.av;
                     dE = this.az == 0;
                     if (this.aK == 4) {
                        dD = dE ? 180 : 0;
                        dz = this.positionX + (dE ? 1 : -1) * dB;
                     } else if (this.aK == 8) {
                        dD = dE ? 0 : 180;
                        dz = this.positionX - (dE ? 1 : -1) * dB;
                     }

                     dG = 0;
                     dA = this.positionY;
                     break;
                  case 156:
                     this.aA = 2500;

                     for (int var10 = 0; var10 < 11; var10++) {
                        if (dp[var10] != null) {
                           dp[var10].c |= 128;
                        }

                        if (fa[var10] != null) {
                           fa[var10].c |= 128;
                        }
                     }
               }

               if (this.aH == 1) {
                  for (int var12 = 0; var12 < 11; var12++) {
                     if (fa[var12] != null) {
                        if (var1 != 152 && var1 != 153 && var1 != 154) {
                           fa[var12].c |= 128;
                        } else {
                           fa[var12].c &= -129;
                        }
                     }
                  }
               }
               break;
            case 89:
               switch (var1) {
                  case 101:
                     if (this.aH == 0) {
                        this.a(26);
                     } else {
                        this.a(29);
                     }
                     break label1204;
                  case 102:
                     if (this.aH == 0) {
                        this.a(25);
                     } else {
                        this.a(28);
                     }
                  default:
                     break label1204;
               }
            case 90:
               switch (var1) {
                  case 2:
                     this.a(8);
                     break label1204;
                  case 3:
                     this.a(0);
                     this.db();
                     this.aA = 2500;
                     this.au();
                     Game.e(this.x);
                     this.x = null;
                     j();
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
                  case 35:
                  case 36:
                  case 37:
                  case 38:
                  case 39:
                  case 40:
                  case 41:
                  case 42:
                  case 43:
                  case 44:
                  case 45:
                  case 46:
                  case 47:
                  case 48:
                  case 49:
                  case 50:
                  case 51:
                  case 52:
                  case 53:
                  case 54:
                  case 55:
                  case 56:
                  case 57:
                  case 58:
                  case 59:
                  case 60:
                  case 61:
                  case 62:
                  case 63:
                  case 64:
                  case 65:
                  case 66:
                  case 67:
                  case 68:
                  case 69:
                  case 70:
                  case 71:
                  case 72:
                  case 73:
                  case 74:
                  case 75:
                  case 76:
                  case 77:
                  case 78:
                  case 79:
                  case 80:
                  case 81:
                  case 82:
                  case 83:
                  case 84:
                  case 85:
                  case 86:
                  case 87:
                  case 88:
                  case 89:
                  case 90:
                  case 91:
                  case 92:
                  case 93:
                  case 94:
                  case 95:
                  case 96:
                  case 97:
                  case 98:
                  case 99:
                  case 100:
                  case 108:
                  case 125:
                  case 127:
                  default:
                     break label1204;
                  case 101:
                     this.a(0);
                     this.aA = 500;
                     break label1204;
                  case 102:
                     this.a(5);
                     this.aA = 100;
                     break label1204;
                  case 103:
                     this.a(0);
                     this.aA = 1000;
                     break label1204;
                  case 104:
                     this.a(2);
                     this.G = 2048;
                     break label1204;
                  case 105:
                     this.a(0);
                     this.aA = 800;
                     break label1204;
                  case 106:
                     this.a(5);
                     this.aA = 1000;
                     break label1204;
                  case 107:
                     this.a(0);
                     this.aA = 2300;
                     break label1204;
                  case 109:
                     this.a(5);
                     this.aA = 1000;
                     this.v = this.a(16, 3, 4, 0, 0, 302, false);
                     this.db();
                     break label1204;
                  case 110:
                     this.a(5);
                     this.db();
                     break label1204;
                  case 111:
                     this.a(0);
                     this.aA = 1200;
                     this.db();
                     Game.e(this.v);
                     this.v = null;
                     break label1204;
                  case 112:
                     this.a(4);
                     this.aA = 600;
                     break label1204;
                  case 113:
                     this.aA = 800;
                     break label1204;
                  case 114:
                     this.a(0);
                     this.ay = 120;
                     this.av = 197 + Game.i(-1, 2) * 80;
                     break label1204;
                  case 115:
                     this.aA = 500;
                     this.db();
                     break label1204;
                  case 116:
                     this.a(3);
                     this.aA = 1000;
                     this.v = this.a(16, 3, 4, 0, 0, 302, false);
                     break label1204;
                  case 117:
                     this.a(4);
                     this.aA = 500;
                     Game.e(this.v);
                     this.v = null;
                     break label1204;
                  case 118:
                     this.a(0);
                     this.aA = 0;
                     break label1204;
                  case 119:
                     this.a(2);
                     break label1204;
                  case 120:
                     this.a(0);
                     this.aA = 1000;
                     break label1204;
                  case 121:
                     this.a(6);
                     this.aA = 3000;
                     this.au();
                     this.c(25, this.positionX + -35, this.positionY + -90);
                     this.x = this.d(14, this.positionX + -35, this.positionY + -10);
                     break label1204;
                  case 122:
                     this.a(7);
                     this.aA = 1000;
                     break label1204;
                  case 123:
                     this.a(0);
                     break label1204;
                  case 124:
                     Game.player.positionX = this.positionX + -35;
                     Game.player.positionY = this.positionY + -10;
                     Game.player.db();
                     break label1204;
                  case 126:
                     this.aA = 3000;
                     br = true;
                     this.ax = 0;
                     this.au();
                     this.c(25, Game.player.positionX + -35, Game.player.positionY + -90);
                     Game.player.a(33);
                     break label1204;
                  case 128:
                     this.a(8);
                     this.aA = 1500;
                     Game.e(this.v);
                     this.v = null;
                     int var9 = 0;

                     while (true) {
                        if (var9 >= 6) {
                           break label1204;
                        }

                        this.a(61, 38, 0, 0, 0, 101, false);
                        aU.positionX = this.positionX + (var9 % 2 * 2 - 1) * 15 + Game.i(-7, 7);
                        aU.positionY = this.positionY - var9 / 2 * 30 + Game.i(-15, 15);
                        aU.ap = 319;
                        aU.G = fs << 8;
                        var9++;
                     }
                  case 129:
                     this.aA = 1000;
                     break label1204;
               }
            case 92:
               this.db();
               this.da();
               switch (var1) {
                  case 0:
                     this.c &= -129;
                     break label1204;
                  case 2:
                     this.a(3);
                     break label1204;
                  case 3:
                  case 9:
                     this.a(4);
                     break label1204;
                  case 102:
                     this.a(5);
                     break label1204;
                  case 103:
                     this.aA = 2000;
                     this.a(1);
                     break label1204;
                  case 104:
                     this.a(3);
                     this.G = -this.C(512);
                     break label1204;
                  case 105:
                     this.G = this.D(1792);
                     break label1204;
                  case 106:
                     this.a(1);
                     this.aA = 2000;
                     this.G = this.C(1792);
                     this.ay = this.G > 0 ? -1 : 1;
                     this.aw = Game.player.a;
                  default:
                     break label1204;
               }
            case 99:
               this.da();
               switch (var1) {
                  case 0:
                     this.O = true;
                     this.P = true;
                     if (this.r != null) {
                        this.r.c |= 128;
                     }

                     this.c &= -129;
                     this.a(0);
                     break label1204;
                  case 2:
                     Game.player.au();
                     this.a(6);
                     break label1204;
                  case 101:
                     this.a(1);
                     break label1204;
                  case 103:
                     this.r.c |= 128;
                     this.c &= -129;
                     this.a(3);
                     break label1204;
                  case 104:
                     this.a(4);
                     Game.player.c(25, this.positionX, this.positionY - 80);
                     aM = true;
                     ez = true;
                     Game.player.db();
                     this.aB = 0;
                     break label1204;
                  case 105:
                     this.O = true;
                     this.P = true;
                     this.a(5);
                     break label1204;
                  case 106:
                     aM = false;
                     ez = false;
                     Game.player.au();
                  case 102:
                     this.O = false;
                     this.P = false;
                     this.a(0);
                     if (this.r == null) {
                        this.r = this.a(-50, 17, 72, 0, 0, this.ap + 1, this.facingLeft);
                        this.r.c |= 32;
                     } else {
                        this.r.a(72);
                        this.r.facingLeft = this.facingLeft;
                        this.r.positionX = this.positionX;
                        this.r.positionY = this.positionY;
                        this.r.c &= -129;
                     }

                     this.c |= 128;
                  default:
                     break label1204;
               }
            case 100:
               switch (var1) {
                  case 3:
                  default:
                     break;
                  case 100:
                     this.G = this.H = 0;
                     this.I = this.J = 0;
                     this.a(9);
                     this.aA = 1000;
                     break;
                  case 101:
                     this.a(10);
                     this.aA = 1000;
                     break;
                  case 102:
                     this.a(4);
                     aZ = new Actor[4];

                     for (int var5 = 0; var5 < 4; var5++) {
                        aZ[var5] = this.a(39, 17, 29, 0, 0, 302, false);
                        a(this.positionX, this.positionY - 40, aY[var5].positionX, aY[var5].positionY, cU);
                        aZ[var5].positionX = cU[0];
                        aZ[var5].positionY = cU[1];
                     }
                     break;
                  case 103:
                     this.aA = 1000;
                     this.a(3);
                     break;
                  case 104:
                     this.G = this.H = 0;
                     this.I = this.J = 0;
                     this.a(6);
                     this.aA = 4000;
                     break;
                  case 105:
                     this.a(9);
                     this.aA = 1000;
                     break;
                  case 106:
                     this.a(10);
                     this.aA = 1000;
                     break;
                  case 107:
                     this.a(3);
                     this.v.a(3);
                     this.aA = 1000;
                     break;
                  case 108:
                     this.a(1);
                     if (this.v != null) {
                        this.v.a(1);
                     }

                     this.aA = 6000;
                     if (this.aq == 106) {
                        this.positionX -= 60;
                     }
                     break;
                  case 109:
                     this.a(0);
                     this.v.a(0);
                     this.v.ay = this.ay = 0;
                     break;
                  case 110:
                     this.aB = 2;
                     this.a(9);
                     break;
                  case 111:
                     this.aA = 400;
                     break;
                  case 112:
                     this.db();
                     this.aA = 1000;
               }
         }

         this.aq = var1;
      }
   }

   // $VF: renamed from: do () void
   private void do_() {
      switch (this.animationId) {
         case 0:
            if (b(Game.player.collisionBounds, this.collisionBounds)) {
               boolean var3 = false;
               if ((this.aA & 32) != 0) {
                  this.aA |= 15;
                  var3 = true;
               }

               if (!var3) {
                  b(this.aA, this.aB, true);
               } else {
                  a(this.aA, this.aB, true, true);
               }

               Game.e(this);
               return;
            }
         case 1:
         case 2:
         case 6:
         case 7:
         case 9:
         case 11:
         case 12:
         default:
            break;
         case 3:
            if (aQ) {
               if (--this.au <= 0) {
                  as();
                  this.c |= 32;
                  Game.e(this);
                  return;
               }
            } else if (b(Game.player.collisionBounds, this.collisionBounds)) {
               j(this.aB, 0);
               if (aS <= 0) {
                  aS = 1;
               }

               this.au = this.aA / 50;
               return;
            }
            break;
         case 4:
            if (Game.levelIndex == 4) {
               if (this.aA == 0) {
                  this.aA = 50;
               }

               if (this.aA != 1) {
                  this.aA--;
                  return;
               }
            }

            if (Game.player.animationId != 12 && b(Game.player.collisionBounds, this.collisionBounds)) {
               Game.a(15);
               return;
            }
            break;
         case 5:
            en = false;
            ey[0] = this.collisionBounds[0];
            ey[2] = this.collisionBounds[0] + 240;
            ey[1] = this.collisionBounds[1];
            ey[3] = this.collisionBounds[3];
            if (!b(Game.player.collisionBounds, ey)) {
               eo = 0;
               this.c &= -17;
               return;
            }

            fs = 0;
            this.c |= 16;
            if (!en) {
               en = true;
               if (Game.player.animationId == 37) {
                  Game.player.b(1);
                  Game.bs = 160;
                  Game.br = 0;
                  ep = this;
               }
            }

            eo = eo + this.aA;
            Game.cameraX = this.collisionBounds[0];
            Game.viewBounds[0] = Game.cameraX;
            Game.viewBounds[2] = Game.cameraX + 240;
            Game.ac = Game.cameraX;
            if (Game.player.positionX < ey[0] + 10) {
               Game.player.positionX = ey[0] + 10;
            }

            if (Game.player.positionX > Game.viewBounds[2] - 10) {
               Game.player.positionX = Game.viewBounds[2] - 10;
               return;
            }
            break;
         case 8:
            if (b(Game.player.collisionBounds, this.collisionBounds)) {
               this.x(this.aA, this.aB);
               return;
            }
            break;
         case 10:
            Game.a(12);
            return;
         case 13:
         case 14:
            if (this.aA != -1 && this.s == null) {
               this.s = Game.i(this.aA);
            }

            if (this.aB != -1 && this.v == null) {
               this.v = Game.i(this.aB);
            }

            if (Game.cameraX >= this.v.positionX && 13 == this.animationId || Game.cameraY <= this.v.positionY && 14 == this.animationId) {
               if (14 == this.animationId) {
                  for (int var1 = 0; var1 < Game.ci; var1++) {
                     Actor var2;
                     if ((var2 = Game.ch[var1]) != null && (var2.c & 1024) == 0 && var2.type == 78 && var2.s == this.s) {
                        var2.s = null;
                        var2.aG = -1;
                     }
                  }
               }

               this.s.cy();
               if (13 == this.animationId) {
                  Game.cameraX = this.v.positionX;
                  Game.ac = this.v.positionX;
               } else {
                  Game.cameraY = this.v.positionY;
                  Game.ad = this.v.positionY;
               }

               this.v.c &= -33;
               this.v.t();
               Game.e(this);
               return;
            }
            break;
         case 15:
            if (this.aA != -1 && this.s == null) {
               this.s = Game.i(this.aA);
            }

            if (Game.cameraX + 240 <= this.collisionBounds[2]) {
               this.s.cy();
               this.s = null;
               Game.e(this);
            }
      }
   }

   private void x(int var1, int var2) {
      Game.player.facingLeft = this.facingLeft;
      if (this.facingLeft) {
         Game.player.c |= 1;
      } else {
         Game.player.c &= -2;
      }

      if (var2 != 0 && var2 != -1) {
         Game.db = false;
      } else {
         Game.db = true;
      }

      Game.w(Game.B(var1));
      Game.player.H = 0;
      Game.player.G = 0;
      if (E != null) {
         E.H = 0;
         E.G = 0;
      }

      if (this.type != 100 && this.type != 90) {
         Game.e(this);
      }

      if (Game.cb != null) {
         e(Game.cb);
      }
   }

   private void dp() {
      this.cV();
      this.cW();
      this.g();
      this.N = false;
      this.O = false;
      this.P = false;
      switch (this.aq) {
         case 0:
         default:
            break;
         case 100:
            this.d(0, this.k);
            this.N = true;
            this.O = true;
            this.P = true;
            if (this.aA <= 0 && this.aC != et || (this.b(ch, this.collisionBounds) || this.b(ch, this.k)) && this.aB <= 0) {
               this.aB = 500;
               this.a(0);
               if (aT == this) {
                  aT = null;
                  Game.player.a(7);
               }

               if (this.aC == et) {
                  this.aq = 102;
                  this.aC = es;
                  this.aA = 1000;
               } else if (this.fw > this.fv && this.fw < this.fu) {
                  this.aq = 101;
               } else {
                  this.aq = 102;
                  this.aA = 1000;
               }
            }
            break;
         case 101:
            byte var1 = 2;
            if (!this.fx && this.an == -1) {
               var1 = 8;
            }

            if (this.type == 37) {
               this.positionY = this.positionY + (this.fx ? -var1 : var1);
            }

            this.fw = this.fw + (this.fx ? var1 : -var1);
            if (this.fw >= this.fu || this.fw <= this.fv) {
               this.aq = 102;
               this.aA = 1000;
            }

            if (this.an == -1) {
               if (Game.player.w != null) {
                  this.positionY = this.positionY + (this.az - Game.player.w.av);
                  this.az = Game.player.w.av;
               }

               if (this.cI && Game.player.aq == 1) {
                  Game.player.positionY = this.positionY - 115;
               }

               if (this.fw < this.fv) {
                  Game.player.u = null;
                  Game.player.w = null;
                  Game.e(this);
               }
            }
            break;
         case 102:
            if (this.aC != et && this.fu != this.fv) {
               if (this.aA < 0 && this.an != -1) {
                  this.aq = 101;
                  if (this.fw <= this.fv) {
                     this.fx = true;
                  } else if (this.fw >= this.fu) {
                     this.fx = false;
                  }
               }

               if (this.an == -1) {
                  if (Game.player.w != null) {
                     this.positionY = this.positionY + (this.az - Game.player.w.av);
                     this.az = Game.player.w.av;
                  }

                  if (Game.player.u == this && this.cI && Game.player.aq == 1) {
                     Game.player.positionY = this.positionY - 115;
                  } else {
                     this.aq = 101;
                     this.fx = false;
                  }
               }
            }
      }

      if ((this.aq == 101 || this.aq == 102) && this.b(1, this.collisionBounds) && this.aB <= 0 && this.animationId == 0 && this.type == 37) {
         this.aB = 500;
         this.aq = 100;
         this.a(1);
         this.aA = 10000;
      }
   }

   final void az() {
      this.facingLeft = Game.player.facingLeft;
      this.positionX = Game.player.positionX;
      this.positionY = Game.player.positionY;
   }

   private void dq() {
      if (this.collisionBounds != null) {
         if (this.aC == 1) {
            this.positionX = this.ay + Game.cameraX;
            this.positionY = this.av + Game.cameraY;
         } else if (this.aC == 2) {
            this.positionY = this.av + Game.cameraY;
         } else if (this.aC != 3) {
            if (this.animationId == 31 && Game.player.aq == 1 && b(Game.player.collisionBounds, this.collisionBounds)) {
               this.c &= -129;
            }

            if (this.animationId != 39 && this.animationId != 40) {
               if (b(Game.player.collisionBounds, this.collisionBounds) && this.aA != 2) {
                  this.aA = 1;
               }

               if (this.aB == 0
                  && this.aA == 1
                  && !b(Game.player.collisionBounds, this.collisionBounds)
                  && this.animationId != 17
                  && this.animationId != 18
                  && this.animationId != 34) {
                  Game.e(this);
               }

               if ((this.animationId == 17 && eu == 0 || this.animationId == 18 && eu == 1)
                  && (b(Game.player.collisionBounds, this.collisionBounds) || a(this.collisionBounds, Game.player.collisionBounds))) {
                  this.aA = 2;
               }

               if (this.aA == 2 && !b(Game.player.collisionBounds, this.collisionBounds)) {
                  Game.e(this);
               }

               if (this.animationId == 34) {
                  if (ev == 1) {
                     Game.e(this);
                     return;
                  }

                  ev = 0;
               }
            } else {
               this.positionX = Game.player.positionX;
               this.positionY = Game.player.positionY + -80;
               if (bb <= 50) {
                  Game.e(this);
               }
            }
         }
      }
   }

   public static final void a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      Game.graphics.setColor(4215580);
      Game.c(var2 - var5, var3 - var5, var5 * 2, var5 * 2, 0, 360);
      Game.c(var0 - var4, var1 - var4, var4 * 2, var4 * 2, 0, 360);
      int[] var8 = new int[2];
      int var9 = var2;
      int var10 = var3 - var5;
      int var11 = var2 - (var5 * Game.m(60) >> 8);
      int var12 = var3 + (var5 >> 1);
      int var13 = var2 + (var5 * Game.m(60) >> 8);
      int var14 = var3 + (var5 >> 1);
      a(var2, var3, var9, var10, var6, var8);
      var9 = var8[0];
      var10 = var8[1];
      a(var2, var3, var11, var12, var6, var8);
      var11 = var8[0];
      var12 = var8[1];
      a(var2, var3, var13, var14, var6, var8);
      var13 = var8[0];
      var14 = var8[1];
      int var15 = var2;
      int var16 = var3 + var5;
      int var17 = var2 - (var5 * Game.m(60) >> 8);
      int var18 = var3 - (var5 >> 1);
      int var19 = var2 + (var5 * Game.m(60) >> 8);
      int var20 = var3 - (var5 >> 1);
      a(var2, var3, var15, var16, var6, var8);
      var15 = var8[0];
      var16 = var8[1];
      a(var2, var3, var17, var18, var6, var8);
      var17 = var8[0];
      var18 = var8[1];
      a(var2, var3, var19, var20, var6, var8);
      var19 = var8[0];
      var20 = var8[1];
      Game.d(var9, var10, var11, var12);
      Game.d(var9, var10, var13, var14);
      Game.d(var11, var12, var13, var14);
      Game.d(var15, var16, var17, var18);
      Game.d(var15, var16, var19, var20);
      Game.d(var17, var18, var19, var20);
   }

   static {
      int[] var10000 = new int[]{102, 136, 170};
      var10000 = new int[]{119, 153, 170};
      var10000 = new int[]{221, 238, 255};
      var10000 = new int[]{0, 100, 0, 0};
      var10000 = new int[]{-5, 10, 15, 0};
      var10000 = new int[]{0, 100, 0, 0};
      var10000 = new int[]{0, 100, 0, 0};
      var10000 = new int[]{4215580, 4215580, 4215580, 4215580, 4215580, 4215580};
   }
}
