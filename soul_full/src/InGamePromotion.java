import java.io.InputStream;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;

// $VF: renamed from: a
public final class InGamePromotion implements Runnable, CommandListener {
   public static String a = "1.8";
   public static String b = "IGP-Signature=" + a;
   private static String i = "";
   private static String j;
   private static boolean k;
   private static Font l;
   private static int m = 16711680;
   private static int n;
   private static int o;
   private static int p;
   private static int q;
   private static int r;
   private static int s;
   private static int t;
   private static int u;
   private static int v;
   private static int w;
   private static int x;
   private static int y;
   private static int z;
   private static int A;
   private static int B;
   private static int C;
   private static int D;
   private static int E = -1;
   private static int F;
   private static int[] G;
   private static byte[][] H;
   private static int I;
   private static byte[] J;
   private static int K;
   private static Image[] L;
   private static int M;
   private static int[] N;
   private static int O;
   private static int P;
   private static boolean Q;
   private static final String[] R = new String[]{"URL-WN", "URL-BS", "URL"};
   private static String S;
   private static boolean T;
   public static String[] c = new String[0];
   private static String[] U;
   private static String[] V;
   private static String[] W;
   private static int X;
   private static int Y;
   private static String[] Z;
   private static short[] aa;
   private static int ab;
   private static boolean ac;
   private static boolean ad;
   private static boolean ae = false;
   private static boolean af = false;
   private static boolean ag = false;
   private static boolean ah = false;
   private static long ai = 0L;
   private static boolean aj = true;
   private static MIDlet ak;
   private static Canvas al;
   private static String am;
   private static boolean an = false;
   private static CommandListener ao = null;
   public static InGamePromotion d = null;
   private static boolean ap = false;
   private static boolean aq = false;
   private static int ar;
   private static int as;
   private static int at;
   private static int au;
   private static Image[] av;
   private static Image[] aw;
   private static Image ax;
   private static Image[][] ay;
   private static String[][] az;
   private static int[][] aA;
   private static int[][] aB;
   private static int[] aC;
   private static int aD;
   private static int aE;
   private static int aF;
   private static int aG;
   private static int aH;
   private static int aI = -1;
   private static String[] aJ;
   private static boolean[] aK;
   private static int aL;
   private static boolean aM;
   private static byte aN;
   private static boolean aO;
   private static int aP;
   private static int aQ;
   private static int aR;
   private static int aS;
   private static int aT;
   private static int aU;
   private static Image aV;
   private static Image aW;
   private static String aX;
   private static String aY;
   private static String aZ;
   private static int ba;
   private static int bb;
   private static int bc;
   private static int bd;
   private static Command be;
   private static Command bf;
   static int e = -1;
   static int f = -1;
   private static int bg;
   private static int bh;
   private static int bi;
   private static int bj;
   private static String bk;
   private static boolean bl;
   static boolean g = false;
   static boolean h = false;
   private static int bm = -1;
   private static int bn = -1;

   private static boolean e() {
      f();

      try {
         InputStream var0;
         F = (var0 = "a".getClass().getResourceAsStream("/dataIGP")).read() & 0xFF;
         F = F + ((var0.read() & 0xFF) << 8);
         G = new int[F];

         for (int var1 = 0; var1 < F; var1++) {
            G[var1] = var0.read() & 0xFF;
            G[var1] = G[var1] + ((var0.read() & 0xFF) << 8);
            G[var1] = G[var1] + ((var0.read() & 0xFF) << 16);
            G[var1] = G[var1] + ((var0.read() & 0xFF) << 24);
         }

         var0.close();
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   private static void f() {
      G = null;
      H = (byte[][])null;
      F = 0;
      System.gc();
   }

   private static byte[] b(int var0) {
      if (var0 >= 0 && var0 < F - 1) {
         int var1;
         if ((var1 = G[var0 + 1] - G[var0]) == 0) {
            return null;
         }

         if (H != null) {
            return H[var0];
         }

         byte[] var2 = null;

         try {
            InputStream var3 = null;
            (var3 = "a".getClass().getResourceAsStream("/dataIGP")).skip(2 + 4 * F + G[var0]);
            int var4 = (var2 = new byte[var1]).length;

            while (var4 > 0) {
               var4 -= var3.read(var2);
            }

            var3.close();
         } catch (Exception var5) {
         }

         return var2;
      } else {
         return null;
      }
   }

   private static int a(byte[] var0) {
      return (var0[I++] & 0xFF) + ((var0[I++] & 0xFF) << 8);
   }

   private static Image b(byte[] var0) {
      int var1 = a(var0);
      Image var2 = a(var0, I, var1);
      I += var1;
      return var2;
   }

   private static Image a(byte[] var0, int var1, int var2) {
      return Image.createImage(var0, var1, var2);
   }

   private static String c(int var0) {
      return "" + Z[var0];
   }

   private static int b(int var0, int var1) {
      return J[(var0 << 2) + var1] & 0xFF;
   }

   private static void a(int var0, Graphics var1, int var2, int var3, int var4) {
      a(c(var0), var1, ba, var2, var3, var4);
   }

   private static void a(String var0, Graphics var1, int var2, int var3, int var4, int var5) {
      a(var0, var1, var2, var3, var4, var5, 10);
   }

   private static void a(String var0, Graphics var1, int var2, int var3, int var4, int var5, int var6) {
      String var7 = var0;
      N[0] = 0;
      P = 0;
      int var8 = 0;
      boolean var11 = false;
      boolean var12 = false;
      int var13 = var7.length();

      for (int var10 = 0; var10 < var13; var10++) {
         char var9;
         if ((var9 = var7.charAt(var10)) != '\n' || var8 >= 10) {
            if (var9 != '\\' || var7.charAt(++var10) != 'N') {
               if (var9 != 0 && var9 != 1) {
                  N[var8] = N[var8] + b(var9, 2) + -1;
               }
               continue;
            }
         }

         N[var8] = N[var8] - -1;
         if (N[var8] > P) {
            P = N[var8];
         }

         N[++var8] = 0;
      }

      N[var8] = N[var8] - -1;
      if (N[var8] > P) {
         P = N[var8];
      }

      O = (var8 + 1) * K + var8 * 0;
      if (Q) {
         Q = false;
      } else {
         var4 += 0 * var8 / 2;
         var8 = 0;
         if ((var5 & 32) != 0) {
            var4 -= O;
         } else if ((var5 & 2) != 0) {
            var4 -= O >> 1;
         }

         int var14 = var3;
         var11 = true;
         var12 = false;
         b(var1, 0, 0, ba, bb);

         for (int var19 = 0; var19 < var13; var19++) {
            char var18 = var7.charAt(var19);
            if (var11) {
               var14 = var3;
               if ((var5 & 8) != 0) {
                  var14 -= N[var8];
               } else if ((var5 & 1) != 0) {
                  var14 -= N[var8] >> 1;
               }

               var11 = false;
            }

            label76:
            if (var18 != '\n' || var8 >= 10) {
               if (var18 == '\\') {
                  if (var7.charAt(++var19) == 'N') {
                     break label76;
                  }
               }

               if (var12) {
                  var12 = false;
                  char var15;
                  if ((var15 = var7.charAt(var19 - 2)) == ' ') {
                     var14 -= b(var15, 2) + -1 >> 1;
                  }

                  if (var18 == ' ') {
                     var14 += b(var18, 2) + -1 >> 1;
                     continue;
                  }
               }

               b(var1, var14, var4, b(var18, 2), b(var18, 3));
               a(var1, L[M], b(var18, 0), b(var18, 1), b(var18, 2), b(var18, 3), var14, var4);
               var14 += b(var18, 2) + -1;
               continue;
            }

            var4 += K + 0 - 2;
            var8++;
            var11 = true;
            var12 = true;
         }

         b(var1, 0, 0, ba, bb);
         M = 0;
      }
   }

   public static final void a(MIDlet var0, Canvas var1, int var2, int var3) {
      a(var0, var1, var2, var3, null);
   }

   public static final void a(MIDlet var0, Canvas var1, int var2, int var3, CommandListener var4) {
      ba = var2;
      bb = var3;
      bc = ba >> 1;
      bd = bb >> 1;
      if (ak == null) {
         if (var0 == null) {
            throw new RuntimeException("MIDlet instance can't be null");
         }

         if (var1 == null) {
            throw new RuntimeException("Canvas instance can't be null");
         }

         if (var4 != null) {
            an = true;
            if (d == null) {
               d = new InGamePromotion();
            }

            ao = var4;
         }

         ak = var0;
         al = var1;
         g();
         String var5 = b;
         new StringBuffer().append(var5).append("").toString();
      }
   }

   private static boolean c(String var0, int var1) {
      if (var0 == null) {
         return (var1 & 1) == 0;
      }

      var0 = var0.trim();
      return ((var1 & 1) == 0 || var0.length() != 0)
         && ((var1 & 2) == 0 || var0.toUpperCase().compareTo("DEL") != 0)
         && ((var1 & 4) == 0 || var0.toUpperCase().compareTo("NO") != 0 && var0.toUpperCase().compareTo("0") != 0);
   }

   private static String a(String var0, String var1, String var2) {
      String var3 = "";

      try {
         if (var2 != null && var0 != null && var1 != null) {
            int var4 = var0.indexOf(var1 + "=");
            var2 = var2.trim();
            if (var4 >= 0 && var2.length() > 0) {
               var4 += var1.length() + 1;
               int var5;
               if ((var5 = var0.indexOf(";", var4)) < 0) {
                  var5 = var0.length();
               }

               if ((var3 = var0.substring(var4, var5).trim()).length() == 0 || var3.compareTo("0") == 0 || var3.toUpperCase().compareTo("NO") == 0) {
                  var3 = "";
               } else if (var3.toUpperCase().compareTo("DEL") != 0 && var1.compareTo("OP") != 0) {
                  int var6 = var2.indexOf("XXXX");
                  var3 = var2.substring(0, var6) + var3 + var2.substring(var6 + "XXXX".length());
               }
            }
         }
      } catch (Exception var7) {
         var3 = "";
      }

      return var3;
   }

   private static void a(int var0, String var1, int var2, String var3, String var4) {
      try {
         String var5 = null;
         if (T) {
            var5 = a(a(var3), var1, var4);
         } else {
            var5 = a("URL-" + var1);
         }

         if (c(var5, var2) && (var5.toUpperCase().compareTo("NO") != 0 || var5.toUpperCase().compareTo("0") != 0)) {
            aJ[var0] = var5;
            aK[var0] = true;
         }
      } catch (Exception var7) {
      }
   }

   private static void a(int var0, String[] var1, int var2, int var3, String var4) {
      int var5 = var1.length;
      az[var0] = new String[var5];
      aA[var0] = new int[var5];
      aB[var0] = new int[var5];
      int var6 = 0;
      if (!k) {
         String var7 = "";
         if (T) {
            try {
               var4 = a(var4);
               if (var0 != 2) {
                  var7 = S;
               } else if (aZ.length() > 0) {
                  var7 = aZ + "&ctg=XXXX";
               }
            } catch (Exception var10) {
            }
         }

         for (int var8 = 0; var8 < var1.length; var8++) {
            try {
               String var9 = "";
               if (var0 != 2 && var8 == var5 - 1) {
                  if (!T) {
                     var9 = a(R[var0]);
                  } else if (aZ.length() > 0) {
                     var9 = a(a("IGP-CATEGORIES"), var1[var8], aZ + "&ctg=XXXX");
                  }
               } else if (T) {
                  if (var1[var8].compareTo("GLDT") == 0) {
                     var9 = a(var4, var1[var8], S);
                  } else if (var1[var8].compareTo("CATALOG") == 0) {
                     var9 = aZ;
                  } else {
                     var9 = a(var4, var1[var8], var7);
                  }
               } else {
                  var9 = a(R[var0] + "-" + var1[var8]);
               }

               if (c(var9, 7)) {
                  az[var0][var6] = var9;
                  aA[var0][var6++] = var8;
                  aB[var0][var8] = var3 + var8;
               }
            } catch (Exception var11) {
            }
         }

         if (var6 > 0) {
            aK[4 + var0] = true;
            aC[var0] = var6;
         }
      }
   }

   private static String[] c(byte[] var0) {
      String[] var1 = new String[a(var0)];

      for (int var2 = 0; var2 < var1.length; var2++) {
         int var3 = a(var0);
         var1[var2] = new String(var0, I, var3);
         I += var3;
      }

      return var1;
   }

   private static void a(String[] var0, boolean var1) {
      int var2 = (var0.length - 1 > 0 ? var0.length - 1 : 0) + 4;
      n = ++var2;
      o = n + 1;
      p = o + 1;
      q = p + 1;
      r = q + 1;
      s = r + 1;
      t = s + 1;
      u = t + 1;
      v = u + 1;
      w = v + 1;
      x = w + 1;
      y = x + 1;
      z = y + 1;
      A = z + 1;
      B = A + 1;
      C = B + 1;
      D = C + U.length;
   }

   private static void g() {
      String[] var0;
      try {
         e();
         byte[] var1;
         a(var1 = b(0));
         c = c(var1);
         var0 = c(var1);
         U = c(var1);
         V = c(var1);
         W = c(var1);
         g = a(var1) == 1;
         h = a(var1) == 1;

         try {
            int var2 = a(var1);
            i = new String(var1, I, var2);
            i.equals(a);
         } catch (Exception var8) {
            aq = false;
         }

         f();
      } catch (Exception var9) {
         aq = false;
         return;
      }

      a(var0, g);
      X = var0.length;
      Y = X + 5;
      aJ = new String[9];
      aK = new boolean[9];

      for (int var13 = 0; var13 < aK.length; var13++) {
         aK[var13] = false;
      }

      az = new String[3][];
      aA = new int[3][];
      aB = new int[3][];
      aC = new int[3];

      try {
         int var17 = Integer.parseInt(a("URL-ORANGE"));
         if (!g && var17 == 1) {
            throw new RuntimeException(
               "Trying load Orange France but Resources are not present on dataIGP\nHave you checked include Orange France option at IGPDataConfig.properties file?"
            );
         }

         k = g && var17 == 1;
      } catch (Exception var12) {
      }

      try {
         S = a("URL-TEMPLATE-GAME").trim();
         T = true;
      } catch (Exception var7) {
      }

      for (int var14 = 0; var14 < X; var14++) {
         a(var14, var0[var14], 7, "IGP-PROMOS", S);
      }

      Object var15 = null;

      try {
         if (c((String)(var15 = a("URL-OPERATOR")), 7)) {
            aZ = (String)var15;
         }

         String var18;
         if ((var18 = a("URL-PT")) != null) {
            aX = "";
            int var3 = var18.length();

            for (int var4 = 0; var4 < var3; var4++) {
               char var5 = var18.charAt(var4);
               if (var4 >= var3 - 1 || var5 != '\\' || var18.charAt(var4 + 1) != 'n' && var18.charAt(var4 + 1) != 'N') {
                  aX = aX + var5;
               } else {
                  aX = aX + '\n';
                  var4++;
               }
            }

            aX = aX.toUpperCase();
            if (!c(aX, 7)) {
               aX = null;
            }
         }
      } catch (Exception var11) {
      }

      if (!k && c(aZ, 7)) {
         a(X, "PROMO", 7, "IGP-CATEGORIES", aZ + "&ctg=XXXX");
      }

      a(0, U, X + 1, C, "IGP-WN");
      a(1, V, X + 2, D, "IGP-BS");
      if (!k && c(a(a("IGP-CATEGORIES"), "OP", aZ), 7)) {
         aJ[6] = aZ;
         if (c(aZ, 7)) {
            aK[6] = true;
         }
      }

      if (k) {
         try {
            if (c(aZ, 7)) {
               aJ[7] = aZ;
               aK[7] = true;
            }
         } catch (Exception var6) {
         }
      }

      try {
         bk = a("URL-GLIVE").trim();
         if ((bk.startsWith("http://") || bk.startsWith("https://")) && h) {
            bl = true;
         } else {
            bl = false;
         }
      } catch (Exception var10) {
         bl = false;
      }

      aI = h();
      if (aI > 0) {
         aq = true;
      }
   }

   public static final boolean a() {
      return aq;
   }

   public static final void a(String var0, int var1) {
      ap = false;
      d(var0, var1);
      au = 4 + X + 1 + 1 + 1 + 1 + 1 + 1;
      m();
      as = i();
      if (an) {
         al.setCommandListener(d);
      }
   }

   private static void d(String var0, int var1) {
      if (var1 >= 0 && var1 < c.length) {
         aU = var1 <= c.length ? var1 : 0;
         aY = var0;
         m = 16711680;
         bm = -1;
         bn = -1;
         at = -1;
         ar = 0;
         as = 0;
         aG = 0;
         aH = 0;
         N = new int[10];
         l = Font.getFont(0, 0, 8);
      }
   }

   public static final void b(String var0, int var1) {
      d(var0, var1);
      m = 16737792;
      au = 5;
      n();
      ap = true;
      as = 8;
   }

   private static int h() {
      int var0 = 0;

      for (int var1 = 0; var1 < aK.length; var1++) {
         if (aK[var1]) {
            var0++;
         }
      }

      return var0;
   }

   private static int i() {
      for (int var0 = 0; var0 < aK.length; var0++) {
         if (aK[var0]) {
            return var0;
         }
      }

      return -1;
   }

   private static int c(int var0, int var1) {
      if (var0 <= var1 - 1) {
         return var0 - 1;
      }

      if (var0 > Y + var1 - 1) {
         return 11;
      }

      if ((var0 = var0 - var1) < X) {
         return 4;
      }

      int var3;
      return (var3 = var0 - X) == 0 ? 5 : 7 + var3 - 1;
   }

   private static void d(int var0) {
      byte[] var1 = null;
      I = 0;
      int var3;
      int var4;
      if (ap) {
         var3 = var0;
         var4 = var0;
      } else {
         var3 = c(var0, 5);
         var4 = var0 - 1;
      }

      int var5 = var0 - 5;
      if (!ap && var5 > 0 && var0 < Y + 4) {
         if (var0 == 0 && !aK[var5]) {
            return;
         }

         aw[var5] = av[10];
      }

      switch (var3) {
         case -1:
            av = new Image[12];
            aw = new Image[Y];
            ay = new Image[3][];
            ay[0] = new Image[U.length];
            ay[1] = new Image[V.length];
            ay[2] = new Image[W.length];
            return;
         case 0:
            e();
            return;
         case 1:
            var1 = b(var4);

            for (int var19 = 0; var19 < aU; var19++) {
               int var6 = a(var1);
               I += var6;
            }

            a(var1);
            int var22;
            Z = new String[var22 = a(var1)];
            byte[] var7 = new byte[var22];
            System.arraycopy(var1, I, var7, 0, var22);
            I += var22;
            a(var1);
            int var8;
            aa = new short[var8 = var1[I++] & 0xFF | (var1[I++] & 255) << 8];

            for (int var20 = 0; var20 < var8 - 1; var20++) {
               aa[var20] = (short)((var1[I++] & 255) + ((var1[I++] & 255) << 8));
            }

            aa[var8 - 1] = (short)var22;

            for (int var21 = 0; var21 < var8; var21++) {
               int var23 = var21 == 0 ? 0 : aa[var21 - 1] & 65535;
               int var24;
               if ((var24 = (aa[var21] & '\uffff') - var23) != 0) {
                  try {
                     Z[var21] = new String(var7, var23, var24, "UTF-8");
                  } catch (Exception var14) {
                     var14.printStackTrace();
                  }
               }
            }

            if (an) {
               be = new Command(c(r), 4, 1);
               bf = new Command(c(s), 2, 1);
               a(true, true);
               return;
            }
            break;
         case 2:
            L = new Image[2];
            int var9 = a(var1 = b(var4));
            I = 0;
            L[0] = b(var1);
            L[1] = b(var1, 2, var9, 1, 16711680);
            int var10 = a(var1);
            J = new byte[(a(var1) + 1) * 4];
            int var12 = var10 / 6;

            for (int var18 = 0; var18 < var12; var18++) {
               int var13 = a(var1) << 2;
               System.arraycopy(var1, I, J, var13, 4);
               I += 4;
            }

            K = J[131];
            return;
         case 3:
            var1 = b(var4);

            for (int var2 = 0; var2 < 12; var2++) {
               av[var2] = b(var1);
            }
            break;
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         default:
            if (ap) {
               e(var3);
               return;
            }

            a(var3, var4, var5);
            break;
         case 11:
            f();
            return;
      }
   }

   private static void a(int var0, int var1, int var2) {
      byte[] var3 = null;
      int var4 = 0;
      int var5 = 0;
      switch (var0) {
         case 4:
         case 6:
            aw[var2] = b(b(var1));
            return;
         case 5:
            aW = b(b(var1));
            return;
         case 7:
            var5 = U.length - 1;
         case 8:
            if (var0 == 8) {
               var5 = V.length - 1;
            }

            var4 = var0 - 7;
            var3 = b(var1);

            int var6;
            for (var6 = 0; var6 < var5; var6++) {
               ay[var4][var6] = b(var3);
            }

            ay[var4][var6] = av[11];
            return;
         case 9:
            E = 4 + X;
            var1 = E;
            var3 = b(E);
            aw[var2] = a(var3, 2, var3.length - 2, 16724736, 22923);
            return;
         case 10:
            if (k) {
               int var7;
               var3 = b(var7 = F - 2);
               aw[var2] = b(var3);
               aV = b(var3);
            }
      }
   }

   private static void e(int var0) {
      switch (var0) {
         case 4:
            ax = b(b(F - 1 - (g ? 2 : 1)));
      }
   }

   private static void a(boolean var0) {
      aW = null;

      for (int var1 = 0; var1 < ay.length; var1++) {
         if (ay[var1] != null) {
            for (int var2 = 0; var2 < ay[var1].length; var2++) {
               ay[var1][var2] = null;
            }
         }
      }

      for (int var3 = 0; var3 < Y; var3++) {
         aw[var3] = null;
      }

      if (var0) {
         f();
         J = null;
         L = null;

         for (int var4 = 0; var4 < 12; var4++) {
            av[var4] = null;
         }

         av = null;
         ax = null;
         aa = null;
         Z = null;
         N = null;
         aY = null;
         aw = null;
         aV = null;
         ay = (Image[][])null;
      }

      System.gc();
   }

   private static int a(byte[] var0, int var1, int var2, String var3) {
      for (int var4 = var1; var4 < var2 - 4; var4++) {
         if ((var0[var4] & 255) == var3.charAt(0)
            && (var0[var4 + 1] & 255) == var3.charAt(1)
            && (var0[var4 + 2] & 255) == var3.charAt(2)
            && (var0[var4 + 3] & 255) == var3.charAt(3)) {
            return var4;
         }
      }

      return -1;
   }

   private static Image a(byte[] var0, int var1, int var2, int var3, int var4) {
      int var7 = a(var0, var1, var2, "PLTE");
      int var8 = a(var0, var1, var2, "tRNS");
      Image var9 = null;
      if (var7 > 0 && var8 > 0) {
         int var10 = (var0[var7 - 4] << 24 & 0xFF000000) + (var0[var7 - 3] << 16 & 0xFF0000) + (var0[var7 - 2] << 8 & 0xFF00) + (var0[var7 - 1] << 0 & 0xFF);

         for (int var11 = 0; var11 < var10; var11++) {
            if (var0[var8 + 4 + var11] != 0
               && (var0[var7 + 4 + 3 * var11] & 255) == ((var3 & 0xFF0000) >> 16 & 0xFF)
               && (var0[var7 + 4 + 3 * var11 + 1] & 255) == ((var3 & 0xFF00) >> 8 & 0xFF)
               && (var0[var7 + 4 + 3 * var11 + 2] & 255) == (var3 & 0xFF)) {
               int var12 = b(var0, var7 + 4 + 3 * var11, 3);
               a(var0, var7 + 4 + 3 * var11, 3, var4);
               byte[] var13 = new byte[var10 + 4];
               System.arraycopy(var0, var7, var13, 0, var10 + 4);
               long[] var14 = new long[256];

               for (int var15 = 0; var15 < 256; var15++) {
                  long var5 = var15;

                  for (int var16 = 0; var16 < 8; var16++) {
                     if ((var5 & 1L) == 1L) {
                        var5 = 3988292384L ^ var5 >> 1;
                     } else {
                        var5 >>= 1;
                     }
                  }

                  var14[var15] = var5;
               }

               long var17 = 4294967295L;

               for (int var19 = 0; var19 < var13.length; var19++) {
                  var17 = var14[(int)(var17 ^ var13[var19]) & 0xFF] ^ var17 >> 8;
               }

               var17 ^= 4294967295L;
               int var20 = b(var0, var7 + 4 + var10, 4);
               a(var0, var7 + 4 + var10, 4, (int)var17);
               System.gc();
               var9 = Image.createImage(var0, var1, var2);
               a(var0, var7 + 4 + 3 * var11, 3, var12);
               a(var0, var7 + 4 + var10, 4, var20);
               break;
            }
         }
      }

      return var9;
   }

   private static void a(byte[] var0, int var1, int var2, int var3) {
      for (int var4 = var2 - 1; var4 >= 0; var4--) {
         var0[var1 + var2 - 1 - var4] = (byte)((var3 & 255 << 8 * var4) >> 8 * var4);
      }
   }

   private static int b(byte[] var0, int var1, int var2) {
      int var3 = 0;

      for (int var4 = var2 - 1; var4 >= 0; var4--) {
         var3 += var0[var1 + var2 - 1 - var4] << 8 * var4 & 255 << 8 * var4;
      }

      return var3;
   }

   public static final boolean a(int var0) {
      if (var0 == 0) {
         var0 = d(bm, bn);
      }

      if ((!ap || d()) && (ap || aq)) {
         switch (ar) {
            case 0:
               if (at >= au) {
                  ar = 1;
                  j();
                  if (!ap) {
                     for (int var5 = 0; var5 < aw.length; var5++) {
                        if (aw != null && aw[var5] != null) {
                           if (aw[var5].getWidth() > ba) {
                              throw new RuntimeException("IGP::Page " + var5 + " image width bigger than Screen width(" + ba + ")");
                           }

                           if (aw[var5].getHeight() > bb) {
                              throw new RuntimeException("IGP::Page " + var5 + " image height bigger than Screen height(" + bb + ")");
                           }
                        }
                     }
                  }
               } else {
                  d(at);
               }

               at++;
               break;
            case 1:
               switch (var0) {
                  case 21:
                     if (aO && aH > 0) {
                        aH--;
                        if (aH - aG < 0) {
                           aG--;
                           return false;
                        }
                     }

                     return false;
                  case 22:
                  case 27:
                  case 28:
                  case 29:
                  case 30:
                  case 31:
                  default:
                     return false;
                  case 23:
                     if (aI <= 1) {
                        return false;
                     }

                     if (as == 0) {
                        as = 8;
                     } else {
                        as--;
                     }

                     while (!aK[as]) {
                        if (as == 0) {
                           as = 8;
                        } else {
                           as--;
                        }
                     }

                     ac = true;
                  case 24:
                     if (aI > 1) {
                        if (!ac) {
                           if (as == 8) {
                              as = 0;
                           } else {
                              as++;
                           }

                           while (!aK[as]) {
                              if (as == 8) {
                                 as = 0;
                              } else {
                                 as++;
                              }
                           }

                           ad = true;
                        }

                        aG = 0;
                        aH = 0;
                        j();
                     }

                     return false;
                  case 25:
                     String var1 = null;
                     if (ap) {
                        var1 = bk;
                     } else {
                        var1 = aJ[as];
                     }

                     if (aO) {
                        var1 = az[aD][aH];
                     }

                     if (var1 != null && var1.length() > 0) {
                        j = var1;
                     }

                     return false;
                  case 26:
                     ar = 4;
                     return false;
                  case 32:
                     if (aO && aH < aE - 1) {
                        aH++;
                        if (aH - aG >= aF) {
                           aG++;
                           return false;
                        }
                     }

                     return false;
               }
            case 2:
               a(false);
               at = 5 + as;
               d(at);
               if (aw != null && aw[as] != null) {
                  if (aw[as].getWidth() > ba) {
                     throw new RuntimeException("IGP::Page " + as + " image width bigger than Screen width(" + ba + ")");
                  }

                  if (aw[as].getHeight() > bb) {
                     throw new RuntimeException("IGP::Page " + as + " image height bigger than Screen height(" + bb + ")");
                  }
               }

               ar = 1;
               break;
            case 3:
               switch (var0) {
                  case 25:
                     j = am;
                     return false;
                  case 26:
                     ar = 1;
                     am = null;
                     return false;
                  default:
                     return false;
               }
            case 4:
               a(true);
               if (an) {
                  al.setCommandListener(ao);
                  l();
               }

               ap = false;
               return true;
            case 5:
         }

         return false;
      } else {
         return true;
      }
   }

   public static final void a(int var0, int var1) {
      bm = var0;
      bn = var1;
   }

   private static int d(int var0, int var1) {
      if (var0 < 1) {
         return 0;
      }

      if (var0 == e && var1 == f) {
         return 0;
      }

      if (ar == 0) {
         return 0;
      }

      e = var0;
      f = var1;
      if (aO) {
         int var2 = bb * 5 / 100;
         int var3 = 1 + var2 + av[5].getWidth();
         int var4 = ba * 1 / 100;
         int var5 = var3 + ay[aD][0].getWidth() + var4;
         boolean var6 = false;
         boolean var7 = var0 > var5 && var0 < var5 + (ba - 2 * (var5 - 2));

         for (int var8 = aG; var8 < aG + aF; var8++) {
            if (var7 && var1 > bg + bh * (var8 - aG) && var1 < bg + bh * (var8 - aG) + bh) {
               if (aH == var8) {
                  return 25;
               }

               aH = var8;
               return 0;
            }
         }

         if (aF < aE && var7) {
            if (aH > 0 && var1 > bi - (var5 - 2) / 2 && var1 < bi + (var5 - 2)) {
               return 21;
            }

            if (aH < aE - 1 && var1 > bj - (var5 - 2) / 2 && var1 < bj + (var5 - 2)) {
               return 32;
            }
         }
      } else if (var0 > aS && var0 < aS + aQ && var1 > aT && var1 < aT + aR) {
         return 25;
      }

      int var9 = ba - av[7].getWidth();
      int var10 = (bb >> 1) - av[7].getHeight();
      int var11 = av[7].getHeight() * 2;
      int var12 = av[7].getWidth() * 2;
      if (var1 > var10 && var1 < var10 + var11) {
         if (var0 > 0 && var0 < 0 + var12) {
            return 23;
         }

         if (var0 > var9 - var12 / 2 && var0 < var9 + var12) {
            return 24;
         }
      }

      if (an) {
         return 0;
      }

      if (var1 >= bb - av[8].getHeight() * 2 && var1 <= bb) {
         if (var0 > 0 && var0 < av[8].getWidth() * 2) {
            return 25;
         }

         if (var0 > ba - av[8].getWidth() * 2 && var0 < ba) {
            return 26;
         }
      }

      return 0;
   }

   private static void j() {
      if (ap) {
         aH = 0;
         aE = 0;
         aG = 0;
         aO = false;
         aM = true;
         aN = (byte)(aN | 3);
         aP = A;
         aL = A;
      } else {
         aN = 0;
         aL = as;
         aH = 0;
         aE = 0;
         aG = 0;
         aO = false;
         aM = aJ[as] != null && aJ[as].length() > 0 && aJ[as].compareTo("DEL") != 0;
         if (as == 4) {
            aD = 0;
            aO = true;
            aM = false;
         }

         if (as == 5) {
            aD = 1;
            aO = true;
            aM = false;
         }

         if (as == 6 || as == 7) {
            aM = true;
         }

         if (aM || aO) {
            aN = (byte)(aN | 1);
         }

         aN = (byte)(aN | 2);
         aP = t;
         if (as == 0 || as == 1 || as == 2) {
            aP = u;
         }

         if (aO) {
            aE = aC[aD];
            aP = u;
            if (as == 6) {
               aM = true;
               aP = t;
            }
         }
      }
   }

   public static final void a(Graphics var0) {
      if ((!ap || d()) && (ap || aq)) {
         if (j != null) {
            o();
         } else {
            Graphics var1 = var0;
            b(var0, 0, 0, ba, bb);
            b(var1, 0, 0, ba, bb);
            switch (ar) {
               case 0:
                  var0.setColor(0);
                  var0.fillRect(0, 0, ba, bb);
                  a(var0, bd, ba * 3 / 4, at, au);
                  if (aY != null) {
                     if (aY.trim().equals("")) {
                        return;
                     }

                     var0.setColor(16777215);
                     var0.setFont(l);
                     var0.drawString(aY, bc, bd - 5, 33);
                     return;
                  }
                  break;
               case 1:
                  k();
                  if (ap) {
                     char var12 = '駿';
                     var1.setColor(var12);
                     var1.fillRect(0, 0, ba, bb);
                  } else {
                     int var13 = 201756;
                     int var3 = 35031;
                     if (as == 3) {
                        var13 = 11167232;
                        var3 = 16052737;
                     }

                     if (as == 6) {
                        var13 = 11167232;
                        var3 = 16052737;
                     }

                     if (as == 7) {
                        var13 = 0;
                        var3 = 0;
                     }

                     a(var1, 0, 0, ba, bb, var13, var3);
                  }

                  d(var1);
                  if (aO) {
                     b(var1);
                  } else if (ap) {
                     int var18 = ax.getHeight();
                     int var24 = 0;
                     Q = true;
                     a(c(x), var1, ba, bc, 0, 17);
                     int var32 = O;
                     Q = true;
                     a(c(y), var1, ba, bc, 0, 17);
                     int var35 = O;
                     int var46 = (bb - var18 - aR - var32 - var35) / 5;
                     var1.drawImage(ax, bc, 0, 17);
                     var24 = 0 + var18 + var46;
                     a(c(x), var1, ba, bc, var24, 17);
                     var24 += var32 + var46;
                     a(c(y), var1, ba, bc, var24, 17);
                     int var27;
                     aT = (var27 = var24 + var35 + var46) + (bb - var27) / 2 - (aR >> 1);
                  } else if (as == 7) {
                     int var17 = ba * 9 / 100;
                     int var23 = bb - av[8].getHeight() - 3;
                     var1.drawImage(aV, ba - var17, var23, 40);
                     Q = true;
                     a(n, null, 0, 0, 0);
                     int var31 = O;
                     int var39;
                     var39 = (var39 = Math.abs((aT - aw[as].getHeight() - var31) / 3)) < 3 ? 3 : var39;
                     a(c(n), var1, ba - (av[4].getWidth() + 8) * 2, av[4].getWidth() + 8, aw[as].getHeight() + var39, 20);
                     aT = aw[as].getHeight() + var39 + O + aR / 2 + var39;
                     var1.drawImage(aw[as], bc, 2, 17);
                  } else if (as == 3) {
                     boolean var16 = bb >= 128;
                     boolean var22 = aX != null && aX.length() > 0;
                     int var30 = var16 ? av[10].getHeight() : 0;
                     int var34 = aW.getHeight();
                     int var38 = var22 ? K * 3 : 0;
                     int var45 = var30 + var34 + var38 + (bb - aT);
                     int var49 = Math.max(0, bb - var45) / 6;
                     int var50;
                     int var51 = var50 = Math.max(2, var49);
                     if (var16) {
                        var1.drawImage(av[10], bc, var51, 17);
                        var51 += var30 + var50;
                     }

                     var51 += aW.getHeight() / 2;
                     var1.drawImage(aW, bc - 1, var51, 3);
                     a(c(aL), var1, aW.getWidth() * 9 / 20, bc + 2, var51, 3);
                     var51 += aW.getHeight() / 2;
                     if (var22) {
                        int var11 = aT - var51 - av[8].getHeight();
                        a(aX, var1, ba, bc, var51 + var11 / 2, 3);
                     }
                  } else if (as == 6) {
                     boolean var14;
                     int var20 = (var14 = bb >= 128) ? av[10].getHeight() : 0;
                     int var4 = aw[as].getHeight();
                     int var5 = var20 + var4 + (bb - aT);
                     int var7 = Math.max(0, bb - var5) / (var14 ? 5 : 4);
                     int var42;
                     int var8 = var42 = Math.max(2, var7);
                     if (var14) {
                        var1.drawImage(av[10], bc, var8, 17);
                        var8 += var20 + var42;
                     }

                     var1.drawImage(aw[as], bc, var8, 17);
                     int var9 = M;
                     M = 1;
                     a(c(aL), var1, aw[as].getWidth() / 2, bc, var8 + aw[as].getHeight() / 2, 3);
                     M = var9;
                  } else {
                     int var15 = aw[as].getHeight();
                     int var21 = K * 2;
                     int var29 = var15 + var21;
                     int var6 = Math.max(0, bb - var29) / 4;
                     int var37;
                     int var43;
                     int var47 = var43 = var37 = Math.max(0, var6);
                     var1.drawImage(aw[as], bc, var47, 17);
                     var43 += var15 + var37;
                     int var48 = an ? ba : ba - (av[9].getWidth() + av[8].getWidth());
                     int var10 = bb - var43;
                     a(c(aL), var1, var48, bc, var43 + var10 / 2, 3);
                  }

                  if (!ap && aI > 1) {
                     int var19 = Math.abs((int)(System.currentTimeMillis() / 80L % 8L) - 4);
                     byte var28 = 5;
                     byte var33 = 7;
                     boolean var36 = false;
                     boolean var41 = false;
                     if (ac) {
                        var28 = 4;
                        ab++;
                     }

                     var0.drawImage(av[var28], 1 + var19, bd, 6);
                     if (ad) {
                        var33 = 6;
                        ab++;
                     }

                     var0.drawImage(av[var33], ba - 1 - var19, bd, 10);
                     if (ab > 4) {
                        ac = false;
                        ad = false;
                        ab = 0;
                     }
                  }

                  if (System.currentTimeMillis() % 1000L > 500L) {
                     c(var0);
                     return;
                  }
                  break;
               case 2:
                  var0.setFont(l);
                  int var2 = l.getHeight();
                  var0.setColor(255);
                  var0.fillRect(0, bd - var2 - 5, ba, var2 * 2);
                  var0.setColor(16777215);
                  var0.drawString(aY, bc, bd, 65);
                  return;
               case 3:
                  a(var0, 0, 0, ba, bb, 201756, 35031);
                  a(B, var0, bc, bd, 33);
                  d(var0);
               case 4:
               default:
                  break;
               case 5:
                  return;
            }
         }
      }
   }

   private static void b(Graphics var0) {
      int var1 = 2;
      if (bb > 128) {
         var0.drawImage(aw[as], bc, 2, 17);
         var1 = 2 + aw[as].getHeight();
      }

      int var2 = var1 + bb / 50;
      M = 1;
      a(aL, var0, bc, var2, 17);
      int var3 = Math.max(2 * K, ay[aD][aA[aD][0]].getHeight());
      Q = true;
      a(aL, null, 0, 0, 0);
      int var4 = var2 + O;
      int var5;
      int var6 = var5 = bb * 5 / 100;
      int var7 = aT - var4;
      aF = aE;
      if (var7 / var3 < aE) {
         aF = (var7 - 2 * var6) / var3;
      }

      int var19;
      int var8 = var19 = var4 + (var7 - aF * var3) / 2 + 1;
      if (aF < aE) {
         if (aG > 0) {
            a(var0, bc, var8 - var6 - 1, var5 + 2, 65535, true, false);
            bi = var8 - var6 - 1;
         }

         if (aG + aF < aE) {
            a(var0, bc, var8 + aF * var3 + var6 + 1, var5 + 2, 65535, true, true);
            bj = var8 + aF * var3;
         }
      }

      var8 = var19 + var3 / 2 + 1;
      int var9;
      int var10 = var9 = 1 + var5 + av[5].getWidth();
      int var11 = ba * 1 / 100;
      int var12 = var10 + ay[aD][0].getWidth() + var11;
      int var13 = ba - var12 - var9;

      for (int var14 = aG; var14 < aG + aF; var14++) {
         int var15 = aA[aD][var14];
         var0.drawImage(ay[aD][var15], var10, var8, 6);
         if (aD == 2 || var15 == aB[aD].length - 1) {
            M = 1;
         }

         a(c(aB[aD][var15]), var0, var13, var12, var8, 6, 2);
         var8 += var3;
      }

      int var21 = aH - aG;
      int var22 = var10;
      int var16 = var19 + var3 * var21;
      int var17 = ba - var22 - var9;
      int var18 = var3;
      bh = var3;
      bg = var19;
      var0.setColor(16777215);
      var0.drawRect(var22 - 2, var16, var17 + 2, var18);
   }

   private static void a(Graphics var0, int var1, int var2, int var3, int var4, boolean var5, boolean var6) {
      int var7 = var6 ? -1 : 1;
      if (var3 % 2 == 0) {
         var3--;
      }

      var0.setColor(16777215);
      if (var5) {
         b(var0, var1, var2, var1 - (var3 >> 1), var2 + var7 * (var3 >> 1), var1 + (var3 >> 1), var2 + var7 * (var3 >> 1));
      } else {
         b(var0, var1, var2, var1 - var7 * (var3 >> 1), var2 - (var3 >> 1), var1 - var7 * (var3 >> 1), var2 + (var3 >> 1));
      }

      var0.setColor(var4);
      if (var5) {
         b(var0, var1, var2 + var7, var1 - (var3 >> 1) + 2, var2 + var7 * (var3 >> 1) - var7, var1 + (var3 >> 1) - 2, var2 + var7 * (var3 >> 1) - var7);
      } else {
         b(var0, var1 - var7, var2, var1 - var7 * (var3 >> 1) + var7, var2 - (var3 >> 1) + 2, var1 - var7 * (var3 >> 1) + var7, var2 + (var3 >> 1) - 2);
      }
   }

   private static void k() {
      Q = true;
      a(aP, null, 0, 0, 3);
      int var0 = b(32, 2) / 2;
      int var1 = b(32, 3) / 3;
      aQ = P + var0;
      aR = O + var1;
      if ((O + aR) % 2 != 0) {
         aR++;
      }

      aS = bc - aQ / 2;
      aT = bd - aR / 2;
      if (as != 3 && as != 6) {
         if (as == 4 || as == 5) {
            aT = bb - aR / 2 - 2;
         }
      } else {
         aT = bb - aR - bb * 3 / 100;
      }
   }

   private static void c(Graphics var0) {
      if (aM) {
         var0.setColor(m);
         M = 1;
         var0.fillRect(aS, aT, aQ, aR + 1);
         a(aP, var0, bc, aT + (aR >> 1), 3);
      }
   }

   private static void d(Graphics var0) {
      if (!an) {
         if ((aN & 1) != 0) {
            var0.drawImage(av[9], 2, bb - 2, 36);
         }

         if ((aN & 2) != 0) {
            var0.drawImage(av[8], ba - 2, bb - 2, 40);
         }
      }
   }

   private static void a(boolean var0, boolean var1) {
      if (bf != null) {
         al.removeCommand(bf);
      }

      if (be != null) {
         al.removeCommand(be);
      }

      if (var0) {
         al.addCommand(be);
      }

      if (var1) {
         al.addCommand(bf);
      }
   }

   private static void l() {
      if (bf != null) {
         al.removeCommand(bf);
         bf = null;
      }

      if (be != null) {
         al.removeCommand(be);
         be = null;
      }
   }

   private static void a(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = var5 >> 16;
      int var8 = var5 >> 8 & 0xFF;
      int var9 = var5 & 0xFF;
      int var10 = var6 >> 16;
      int var11 = var6 >> 8 & 0xFF;
      int var12 = var6 & 0xFF;
      if (var2 + var4 > bb) {
         var4 = bb - var2;
      }

      if (var1 + var3 > ba) {
         var3 = ba - var1;
      }

      int var13 = var10 - var7;
      int var14 = var11 - var8;
      int var15 = var12 - var9;
      int var16 = var7;
      int var17 = var8;
      int var18 = var9;
      int var19 = 0;

      for (int var20 = var2; var20 < var2 + var4; var20++) {
         if (var20 < bb / 2) {
            var19 = var20;
         } else if (var20 == bb / 2) {
            var16 = var10;
            var17 = var11;
            var18 = var12;
            var19 = 0;
         } else {
            var19 = bb / 2 - var20;
         }

         var0.setColor(var16 + var13 * var19 / (bb / 2), var17 + var14 * var19 / (bb / 2), var18 + var15 * var19 / (bb / 2));
         var0.drawLine(var1, var20, var1 + var3, var20);
      }
   }

   private static Image b(byte[] var0, int var1, int var2, int var3, int var4) {
      int var5 = 0;

      for (int var6 = var1; var5 == 0 && var6 < var1 + var2; var6++) {
         if ((var0[var6] & 255) == 80 && (var0[var6 + 1] & 255) == 76 && (var0[var6 + 2] & 255) == 84 && (var0[var6 + 3] & 255) == 69) {
            var5 = var6;
         }
      }

      int var9 = (var0[var5 - 4] << 24 & 0xFF000000) + (var0[var5 - 3] << 16 & 0xFF0000) + (var0[var5 - 2] << 8 & 0xFF00) + (var0[var5 - 1] << 0 & 0xFF);
      var0[var5 + 4 + 3 * var3] = (byte)((var4 & 0xFF0000) >> 16);
      var0[var5 + 4 + 3 * var3 + 1] = (byte)((var4 & 0xFF00) >> 8);
      var0[var5 + 4 + 3 * var3 + 2] = (byte)(var4 & 0xFF);
      byte[] var10 = new byte[var9 + 4];
      System.arraycopy(var0, var5, var10, 0, var9 + 4);
      long[] var11 = new long[256];

      for (int var12 = 0; var12 < 256; var12++) {
         long var7 = var12;

         for (int var13 = 0; var13 < 8; var13++) {
            if ((var7 & 1L) == 1L) {
               var7 = 3988292384L ^ var7 >> 1;
            } else {
               var7 >>= 1;
            }
         }

         var11[var12] = var7;
      }

      long var14 = 4294967295L;

      for (int var16 = 0; var16 < var10.length; var16++) {
         var14 = var11[(int)(var14 ^ var10[var16]) & 0xFF] ^ var14 >> 8;
      }

      var14 ^= 4294967295L;
      var0[var5 + 4 + var9] = (byte)((var14 & -16777216L) >> 24);
      var0[var5 + 4 + var9 + 1] = (byte)((var14 & 16711680L) >> 16);
      var0[var5 + 4 + var9 + 2] = (byte)((var14 & 65280L) >> 8);
      var0[var5 + 4 + var9 + 3] = (byte)((var14 & 255L) >> 0);
      System.gc();
      return a(var0, var1, var2);
   }

   private static void a(Graphics var0, int var1, int var2, int var3, int var4) {
      if (var3 > var4) {
         var3 = var4;
      }

      int var5 = (ba - var2) / 2;
      var0.setColor(16777215);
      var0.drawRect(var5, var1, var2, 6);
      int var6 = (var2 - 2 - 2) * var3 / var4 + 1;
      var0.setColor(16711680);
      var0.fillRect(var5 + 1 + 1, var1 + 1 + 1, var6, 3);
   }

   private static void b(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      var0.fillTriangle(var1, var2, var3, var4, var5, var6);
   }

   private static void a(Graphics var0, Image var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      var0.drawRegion(var1, var2, var3, var4, var5, 0, var6, var7, 20);
   }

   private static void b(Graphics var0, int var1, int var2, int var3, int var4) {
      var1 = Math.max(var1, 0);
      var2 = Math.max(var2, 0);
      var3 = Math.min(var3, ba);
      var4 = Math.min(var4, bb);
      var0.setClip(var1, var2, var3, var4);
   }

   public final void run() {
   }

   private static void m() {
      ae = true;
      af = true;
      RecordStore var0 = null;

      try {
         var0 = RecordStore.openRecordStore("igp16", false);
      } catch (Exception var5) {
         try {
            var0 = RecordStore.openRecordStore("igp16", true);
         } catch (Exception var4) {
         }
      }

      try {
         if (var0 != null) {
            var0.closeRecordStore();
         }
      } catch (Exception var3) {
      }
   }

   public static final boolean b() {
      if (aq) {
         if (af) {
            return ae;
         }

         try {
            Object var0 = null;
            RecordStore.openRecordStore("igp16", false).closeRecordStore();
            ae = true;
         } catch (Exception var1) {
         }

         af = true;
      }

      return ae;
   }

   private static void n() {
      ag = true;
      ah = true;
      RecordStore var0 = null;

      try {
         var0 = RecordStore.openRecordStore("glive", false);
      } catch (Exception var5) {
         try {
            var0 = RecordStore.openRecordStore("glive", true);
         } catch (Exception var4) {
         }
      }

      try {
         if (var0 != null) {
            var0.closeRecordStore();
         }
      } catch (Exception var3) {
      }
   }

   public static final boolean c() {
      if (bl) {
         if (ah) {
            return ag;
         }

         try {
            Object var0 = null;
            RecordStore.openRecordStore("glive", false).closeRecordStore();
            ag = true;
         } catch (Exception var1) {
         }

         ah = true;
      }

      return ag;
   }

   private static void o() {
      if (j != null && j.length() > 0) {
         try {
            String var0 = j;
            j = null;
            ak.platformRequest(var0);
            ar = 4;
            Thread.sleep(200L);
         } catch (Exception var1) {
         }

         ak.notifyDestroyed();
      }
   }

   public static final void a(Graphics var0, Image var1, int var2, int var3, int var4) {
      a(var0, var1, var2, var3, var4, false);
   }

   public static final void b(Graphics var0, Image var1, int var2, int var3, int var4) {
      a(var0, var1, var2, var3, var4, true);
   }

   public static final void a(Graphics var0, Image var1, int var2, int var3, int var4, boolean var5) {
      if (!b() || var5) {
         if ((a() || var5) && var1 != null && var0 != null) {
            if (!c() || !var5) {
               if ((d() || !var5) && var1 != null && var0 != null) {
                  if (System.currentTimeMillis() - ai > 800L) {
                     aj = !aj;
                     ai = System.currentTimeMillis();
                  }

                  if (aj) {
                     var0.drawImage(var1, var2, var3, var4);
                  }
               }
            }
         }
      }
   }

   public static final boolean d() {
      return bl;
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (an) {
         if (var1 == be) {
            a(25);
            return;
         }

         if (var1 == bf) {
            a(26);
         }
      }
   }

   private static final String a(String var0) {
      return ak.getAppProperty(var0);
   }
}
