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

public final class a implements Runnable, CommandListener {
   public static String a = "1.8";
   public static String b = "IGP-Signature=" + a;
   private static String g = "";
   private static String h;
   private static boolean i;
   private static Font j;
   private static int k = 16711680;
   private static int l;
   private static int m;
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
   private static int C = -1;
   private static int D;
   private static int[] E;
   private static byte[][] F;
   private static int G;
   private static byte[] H;
   private static int I;
   private static Image[] J;
   private static int K;
   private static int[] L;
   private static int M;
   private static int N;
   private static boolean O;
   private static final String[] P = new String[]{"URL-WN", "URL-BS", "URL"};
   private static String Q;
   private static boolean R;
   public static String[] c = new String[0];
   private static String[] S;
   private static String[] T;
   private static String[] U;
   private static int V;
   private static int W;
   private static String[] X;
   private static short[] Y;
   private static int Z;
   private static boolean aa;
   private static boolean ab;
   private static boolean ac = false;
   private static boolean ad = false;
   private static boolean ae = false;
   private static boolean af = false;
   private static long ag = 0L;
   private static boolean ah = true;
   private static MIDlet ai;
   private static Canvas aj;
   private static String ak;
   private static boolean al = false;
   private static CommandListener am = null;
   public static a d = null;
   private static boolean an = false;
   private static boolean ao = false;
   private static String ap = null;
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
   private static String bg;
   private static boolean bh;
   static boolean e = false;
   static boolean f = false;

   private static boolean e() {
      f();

      try {
         InputStream var0;
         D = (var0 = "a".getClass().getResourceAsStream("/dataIGP")).read() & 0xFF;
         D = D + ((var0.read() & 0xFF) << 8);
         E = new int[D];

         for (int var1 = 0; var1 < D; var1++) {
            E[var1] = var0.read() & 0xFF;
            E[var1] = E[var1] + ((var0.read() & 0xFF) << 8);
            E[var1] = E[var1] + ((var0.read() & 0xFF) << 16);
            E[var1] = E[var1] + ((var0.read() & 0xFF) << 24);
         }

         var0.close();
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   private static void f() {
      E = null;
      F = (byte[][])null;
      D = 0;
      System.gc();
   }

   private static byte[] b(int var0) {
      if (var0 >= 0 && var0 < D - 1) {
         int var1;
         if ((var1 = E[var0 + 1] - E[var0]) == 0) {
            return null;
         }

         if (F != null) {
            return F[var0];
         }

         byte[] var2 = null;

         try {
            InputStream var3 = null;
            (var3 = "a".getClass().getResourceAsStream("/dataIGP")).skip(2 + 4 * D + E[var0]);
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
      return (var0[G++] & 0xFF) + ((var0[G++] & 0xFF) << 8);
   }

   private static Image b(byte[] var0) {
      int var1 = a(var0);
      Image var2 = a(var0, G, var1);
      G += var1;
      return var2;
   }

   private static Image a(byte[] var0, int var1, int var2) {
      return Image.createImage(var0, var1, var2);
   }

   private static String c(int var0) {
      return "" + X[var0];
   }

   private static int a(int var0, int var1) {
      return H[(var0 << 2) + var1] & 0xFF;
   }

   private static void a(int var0, Graphics var1, int var2, int var3, int var4) {
      a(c(var0), var1, ba, var2, var3, var4);
   }

   private static void a(String var0, Graphics var1, int var2, int var3, int var4, int var5) {
      a(var0, var1, var2, var3, var4, var5, 10);
   }

   private static void a(String var0, Graphics var1, int var2, int var3, int var4, int var5, int var6) {
      String var7 = var0;
      L[0] = 0;
      N = 0;
      int var8 = 0;
      boolean var11 = false;
      boolean var12 = false;
      int var13 = var7.length();

      for (int var10 = 0; var10 < var13; var10++) {
         char var9;
         if ((var9 = var7.charAt(var10)) != '\n' || var8 >= 10) {
            if (var9 != '\\' || var7.charAt(++var10) != 'N') {
               if (var9 != 0 && var9 != 1) {
                  L[var8] = L[var8] + a(var9, 2) + -1;
               }
               continue;
            }
         }

         L[var8] = L[var8] - -1;
         if (L[var8] > N) {
            N = L[var8];
         }

         L[++var8] = 0;
      }

      L[var8] = L[var8] - -1;
      if (L[var8] > N) {
         N = L[var8];
      }

      M = (var8 + 1) * I + var8 * 0;
      if (O) {
         O = false;
      } else {
         var4 += 0 * var8 / 2;
         var8 = 0;
         if ((var5 & 32) != 0) {
            var4 -= M;
         } else if ((var5 & 2) != 0) {
            var4 -= M >> 1;
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
                  var14 -= L[var8];
               } else if ((var5 & 1) != 0) {
                  var14 -= L[var8] >> 1;
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
                     var14 -= a(var15, 2) + -1 >> 1;
                  }

                  if (var18 == ' ') {
                     var14 += a(var18, 2) + -1 >> 1;
                     continue;
                  }
               }

               b(var1, var14, var4, a(var18, 2), a(var18, 3));
               a(var1, J[K], a(var18, 0), a(var18, 1), a(var18, 2), a(var18, 3), var14, var4);
               var14 += a(var18, 2) + -1;
               continue;
            }

            var4 += I + 0 - 2;
            var8++;
            var11 = true;
            var12 = true;
         }

         b(var1, 0, 0, ba, bb);
         K = 0;
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
      if (ai == null) {
         if (var0 == null) {
            throw new RuntimeException("MIDlet instance can't be null");
         }

         if (var1 == null) {
            throw new RuntimeException("Canvas instance can't be null");
         }

         if (var4 != null) {
            al = true;
            if (d == null) {
               d = new a();
            }

            am = var4;
         }

         ai = var0;
         aj = var1;
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
         if (R) {
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
      if (!i) {
         String var7 = "";
         if (R) {
            try {
               var4 = a(var4);
               if (var0 != 2) {
                  var7 = Q;
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
                  if (!R) {
                     var9 = a(P[var0]);
                  } else if (aZ.length() > 0) {
                     var9 = a(a("IGP-CATEGORIES"), var1[var8], aZ + "&ctg=XXXX");
                  }
               } else if (R) {
                  if (var1[var8].compareTo("GLDT") == 0) {
                     var9 = a(var4, var1[var8], Q);
                  } else if (var1[var8].compareTo("CATALOG") == 0) {
                     var9 = aZ;
                  } else {
                     var9 = a(var4, var1[var8], var7);
                  }
               } else {
                  var9 = a(P[var0] + "-" + var1[var8]);
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
         var1[var2] = new String(var0, G, var3);
         G += var3;
      }

      return var1;
   }

   private static void a(String[] var0, boolean var1) {
      int var2 = (var0.length - 1 > 0 ? var0.length - 1 : 0) + 4;
      l = ++var2;
      m = l + 1;
      n = m + 1;
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
      B = A + S.length;
   }

   private static void g() {
      String[] var0;
      try {
         e();
         byte[] var1;
         a(var1 = b(0));
         c = c(var1);
         var0 = c(var1);
         S = c(var1);
         T = c(var1);
         U = c(var1);
         e = a(var1) == 1;
         f = a(var1) == 1;

         try {
            int var2 = a(var1);
            g = new String(var1, G, var2);
            g.equals(a);
         } catch (Exception var8) {
            aq = false;
         }

         f();
      } catch (Exception var9) {
         aq = false;
         return;
      }

      a(var0, e);
      V = var0.length;
      W = V + 5;
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
         if (!e && var17 == 1) {
            throw new RuntimeException(
               "Trying load Orange France but Resources are not present on dataIGP\nHave you checked include Orange France option at IGPDataConfig.properties file?"
            );
         }

         i = e && var17 == 1;
      } catch (Exception var12) {
      }

      try {
         Q = a("URL-TEMPLATE-GAME").trim();
         R = true;
      } catch (Exception var7) {
      }

      for (int var14 = 0; var14 < V; var14++) {
         a(var14, var0[var14], 7, "IGP-PROMOS", Q);
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

      if (!i && c(aZ, 7)) {
         a(V, "PROMO", 7, "IGP-CATEGORIES", aZ + "&ctg=XXXX");
      }

      a(0, S, V + 1, A, "IGP-WN");
      a(1, T, V + 2, B, "IGP-BS");
      if (!i && c(a(a("IGP-CATEGORIES"), "OP", aZ), 7)) {
         aJ[6] = aZ;
         if (c(aZ, 7)) {
            aK[6] = true;
         }
      }

      if (i) {
         try {
            if (c(aZ, 7)) {
               aJ[7] = aZ;
               aK[7] = true;
            }
         } catch (Exception var6) {
         }
      }

      try {
         bg = a("URL-GLIVE").trim();
         if ((bg.startsWith("http://") || bg.startsWith("https://")) && f) {
            bh = true;
         } else {
            bh = false;
         }
      } catch (Exception var10) {
         bh = false;
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
      an = false;
      d(var0, var1);
      au = 4 + V + 1 + 1 + 1 + 1 + 1 + 1;
      m();
      as = i();
      if (al) {
         aj.setCommandListener(d);
      }
   }

   private static void d(String var0, int var1) {
      if (var1 >= 0 && var1 < c.length) {
         aU = var1 <= c.length ? var1 : 0;
         aY = var0;
         k = 16711680;
         at = -1;
         ar = 0;
         as = 0;
         aG = 0;
         aH = 0;
         L = new int[10];
         ao = true;
         j = Font.getFont(0, 0, 8);
         new Thread(new a()).start();
      }
   }

   public static final void b(String var0, int var1) {
      d(var0, var1);
      k = 16737792;
      au = 5;
      n();
      an = true;
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

   private static int b(int var0, int var1) {
      if (var0 <= var1 - 1) {
         return var0 - 1;
      }

      if (var0 > W + var1 - 1) {
         return 11;
      }

      if ((var0 = var0 - var1) < V) {
         return 4;
      }

      int var3;
      return (var3 = var0 - V) == 0 ? 5 : 7 + var3 - 1;
   }

   private static void d(int var0) {
      byte[] var1 = null;
      G = 0;
      int var3;
      int var4;
      if (an) {
         var3 = var0;
         var4 = var0;
      } else {
         var3 = b(var0, 5);
         var4 = var0 - 1;
      }

      int var5 = var0 - 5;
      if (!an && var5 > 0 && var0 < W + 4) {
         if (var0 == 0 && !aK[var5]) {
            return;
         }

         aw[var5] = av[10];
      }

      switch (var3) {
         case -1:
            av = new Image[12];
            aw = new Image[W];
            ay = new Image[3][];
            ay[0] = new Image[S.length];
            ay[1] = new Image[T.length];
            ay[2] = new Image[U.length];
            return;
         case 0:
            e();
            return;
         case 1:
            var1 = b(var4);

            for (int var19 = 0; var19 < aU; var19++) {
               int var6 = a(var1);
               G += var6;
            }

            a(var1);
            int var22;
            X = new String[var22 = a(var1)];
            byte[] var7 = new byte[var22];
            System.arraycopy(var1, G, var7, 0, var22);
            G += var22;
            a(var1);
            int var8;
            Y = new short[var8 = var1[G++] & 0xFF | (var1[G++] & 255) << 8];

            for (int var20 = 0; var20 < var8 - 1; var20++) {
               Y[var20] = (short)((var1[G++] & 255) + ((var1[G++] & 255) << 8));
            }

            Y[var8 - 1] = (short)var22;

            for (int var21 = 0; var21 < var8; var21++) {
               int var23 = var21 == 0 ? 0 : Y[var21 - 1] & 65535;
               int var24;
               if ((var24 = (Y[var21] & '\uffff') - var23) != 0) {
                  try {
                     X[var21] = new String(var7, var23, var24, "UTF-8");
                  } catch (Exception var14) {
                     var14.printStackTrace();
                  }
               }
            }

            if (al) {
               be = new Command(c(p), 4, 1);
               bf = new Command(c(q), 2, 1);
               a(true, true);
               return;
            }
            break;
         case 2:
            J = new Image[2];
            int var9 = a(var1 = b(var4));
            G = 0;
            J[0] = b(var1);
            J[1] = b(var1, 2, var9, 1, 16711680);
            int var10 = a(var1);
            H = new byte[(a(var1) + 1) * 4];
            int var12 = var10 / 6;

            for (int var18 = 0; var18 < var12; var18++) {
               int var13 = a(var1) << 2;
               System.arraycopy(var1, G, H, var13, 4);
               G += 4;
            }

            I = H[131];
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
            if (an) {
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
            var5 = S.length - 1;
         case 8:
            if (var0 == 8) {
               var5 = T.length - 1;
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
            C = 4 + V;
            var1 = C;
            var3 = b(C);
            aw[var2] = a(var3, 2, var3.length - 2, 16724736, 22923);
            return;
         case 10:
            if (i) {
               int var7;
               var3 = b(var7 = D - 2);
               aw[var2] = b(var3);
               aV = b(var3);
            }
      }
   }

   private static void e(int var0) {
      switch (var0) {
         case 4:
            ax = b(b(D - 1 - (e ? 2 : 1)));
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

      for (int var3 = 0; var3 < W; var3++) {
         aw[var3] = null;
      }

      if (var0) {
         f();
         H = null;
         J = null;

         for (int var4 = 0; var4 < 12; var4++) {
            av[var4] = null;
         }

         av = null;
         ax = null;
         Y = null;
         X = null;
         L = null;
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
      if ((!an || d()) && (an || aq)) {
         switch (ar) {
            case 0:
               if (at >= au) {
                  ar = 1;
                  j();
                  if (!an) {
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

                     aa = true;
                  case 24:
                     if (aI > 1) {
                        if (!aa) {
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

                           ab = true;
                        }

                        aG = 0;
                        aH = 0;
                        j();
                     }

                     return false;
                  case 25:
                     String var1 = null;
                     if (an) {
                        var1 = bg;
                     } else {
                        var1 = aJ[as];
                     }

                     if (aO) {
                        var1 = az[aD][aH];
                     }

                     if (var1 != null && var1.length() > 0) {
                        ap = var1;
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
                     ap = ak;
                     return false;
                  case 26:
                     ar = 1;
                     ak = null;
                     return false;
                  default:
                     return false;
               }
            case 4:
               a(true);
               if (al) {
                  aj.setCommandListener(am);
                  l();
               }

               ao = false;
               an = false;
               return true;
            case 5:
         }

         return false;
      } else {
         return true;
      }
   }

   private static void j() {
      if (an) {
         aH = 0;
         aE = 0;
         aG = 0;
         aO = false;
         aM = true;
         aN = (byte)(aN | 3);
         aP = x;
         aL = x;
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
         aP = n;
         if (as == 0 || as == 1 || as == 2) {
            aP = o;
         }

         if (aO) {
            aE = aC[aD];
            aP = o;
            if (as == 6) {
               aM = true;
               aP = n;
            }
         }
      }
   }

   public static final void a(Graphics var0) {
      if ((!an || d()) && (an || aq)) {
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
                  var0.setFont(j);
                  var0.drawString(aY, bc, bd - 5, 33);
                  return;
               }
               break;
            case 1:
               k();
               if (an) {
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
               } else if (an) {
                  int var18 = ax.getHeight();
                  int var24 = 0;
                  O = true;
                  a(c(v), var1, ba, bc, 0, 17);
                  int var32 = M;
                  O = true;
                  a(c(w), var1, ba, bc, 0, 17);
                  int var35 = M;
                  int var46 = (bb - var18 - aR - var32 - var35) / 5;
                  var1.drawImage(ax, bc, 0, 17);
                  var24 = 0 + var18 + var46;
                  a(c(v), var1, ba, bc, var24, 17);
                  var24 += var32 + var46;
                  a(c(w), var1, ba, bc, var24, 17);
                  int var27;
                  aT = (var27 = var24 + var35 + var46) + (bb - var27) / 2 - (aR >> 1);
               } else if (as == 7) {
                  int var17 = ba * 9 / 100;
                  int var23 = bb - av[8].getHeight() - 3;
                  var1.drawImage(aV, ba - var17, var23, 40);
                  O = true;
                  a(l, null, 0, 0, 0);
                  int var31 = M;
                  int var39;
                  var39 = (var39 = Math.abs((aT - aw[as].getHeight() - var31) / 3)) < 3 ? 3 : var39;
                  a(c(l), var1, ba - (av[4].getWidth() + 8) * 2, av[4].getWidth() + 8, aw[as].getHeight() + var39, 20);
                  aT = aw[as].getHeight() + var39 + M + aR / 2 + var39;
                  var1.drawImage(aw[as], bc, 2, 17);
               } else if (as == 3) {
                  boolean var16 = bb >= 128;
                  boolean var22 = aX != null && aX.length() > 0;
                  int var30 = var16 ? av[10].getHeight() : 0;
                  int var34 = aW.getHeight();
                  int var38 = var22 ? I * 3 : 0;
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
                  int var9 = K;
                  K = 1;
                  a(c(aL), var1, aw[as].getWidth() / 2, bc, var8 + aw[as].getHeight() / 2, 3);
                  K = var9;
               } else {
                  int var15 = aw[as].getHeight();
                  int var21 = I * 2;
                  int var29 = var15 + var21;
                  int var6 = Math.max(0, bb - var29) / 4;
                  int var37;
                  int var43;
                  int var47 = var43 = var37 = Math.max(0, var6);
                  var1.drawImage(aw[as], bc, var47, 17);
                  var43 += var15 + var37;
                  int var48 = al ? ba : ba - (av[9].getWidth() + av[8].getWidth());
                  int var10 = bb - var43;
                  a(c(aL), var1, var48, bc, var43 + var10 / 2, 3);
               }

               if (!an && aI > 1) {
                  int var19 = Math.abs((int)(System.currentTimeMillis() / 80L % 8L) - 4);
                  byte var28 = 5;
                  byte var33 = 7;
                  byte var36 = 1;
                  byte var41 = 3;
                  if (aa) {
                     var28 = 4;
                     var36 = 0;
                     Z++;
                  }

                  var0.drawImage(av[var28], 1 + var19, bd, 6);
                  var0.drawImage(av[var36], 1 + var19 + 6, bd, 6);
                  if (ab) {
                     var33 = 6;
                     var41 = 2;
                     Z++;
                  }

                  var0.drawImage(av[var33], ba - 1 - var19, bd, 10);
                  var0.drawImage(av[var41], ba - 1 - var19 - (av[var33].getWidth() - av[var41].getWidth()) + 3, bd, 10);
                  if (Z > 4) {
                     aa = false;
                     ab = false;
                     Z = 0;
                  }
               }

               if (System.currentTimeMillis() % 1000L > 500L) {
                  c(var0);
                  return;
               }
               break;
            case 2:
               var0.setFont(j);
               int var2 = j.getHeight();
               var0.setColor(255);
               var0.fillRect(0, bd - var2 - 5, ba, var2 * 2);
               var0.setColor(16777215);
               var0.drawString(aY, bc, bd, 65);
               return;
            case 3:
               a(var0, 0, 0, ba, bb, 201756, 35031);
               a(z, var0, bc, bd, 33);
               d(var0);
            case 4:
            default:
               break;
            case 5:
               return;
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
      K = 1;
      a(aL, var0, bc, var2, 17);
      int var3 = Math.max(2 * I, ay[aD][aA[aD][0]].getHeight());
      O = true;
      a(aL, null, 0, 0, 0);
      int var4 = var2 + M;
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
         }

         if (aG + aF < aE) {
            a(var0, bc, var8 + aF * var3 + var6 + 1, var5 + 2, 65535, true, true);
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
            K = 1;
         }

         a(c(aB[aD][var15]), var0, var13, var12, var8, 6, 2);
         var8 += var3;
      }

      int var21 = aH - aG;
      int var22 = var10;
      int var16 = var19 + var3 * var21;
      int var17 = ba - var22 - var9;
      int var18 = var3;
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
      O = true;
      a(aP, null, 0, 0, 3);
      int var0 = a(32, 2) / 2;
      int var1 = a(32, 3) / 3;
      aQ = N + var0;
      aR = M + var1;
      if ((M + aR) % 2 != 0) {
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
         var0.setColor(k);
         K = 1;
         var0.fillRect(aS, aT, aQ, aR + 1);
         a(aP, var0, bc, aT + (aR >> 1), 3);
      }
   }

   private static void d(Graphics var0) {
      if (!al) {
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
         aj.removeCommand(bf);
      }

      if (be != null) {
         aj.removeCommand(be);
      }

      if (var0) {
         aj.addCommand(be);
      }

      if (var1) {
         aj.addCommand(bf);
      }
   }

   private static void l() {
      if (bf != null) {
         aj.removeCommand(bf);
         bf = null;
      }

      if (be != null) {
         aj.removeCommand(be);
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
      while (ao) {
         try {
            if (ap != null) {
               h = ap;
               o();
               ap = null;
            }

            Thread.sleep(1000L);
         } catch (Exception var2) {
         }
      }
   }

   private static void m() {
      ac = true;
      ad = true;
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
         if (ad) {
            return ac;
         }

         try {
            Object var0 = null;
            RecordStore.openRecordStore("igp16", false).closeRecordStore();
            ac = true;
         } catch (Exception var1) {
         }

         ad = true;
      }

      return ac;
   }

   private static void n() {
      ae = true;
      af = true;
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
      if (bh) {
         if (af) {
            return ae;
         }

         try {
            Object var0 = null;
            RecordStore.openRecordStore("glive", false).closeRecordStore();
            ae = true;
         } catch (Exception var1) {
         }

         af = true;
      }

      return ae;
   }

   private static void o() {
      if (h != null && h.length() > 0) {
         try {
            String var0 = h;
            h = null;
            ai.platformRequest(var0);
            ar = 4;
            Thread.sleep(200L);
         } catch (Exception var1) {
         }

         ai.notifyDestroyed();
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
                  if (System.currentTimeMillis() - ag > 800L) {
                     ah = !ah;
                     ag = System.currentTimeMillis();
                  }

                  if (ah) {
                     var0.drawImage(var1, var2, var3, var4);
                  }
               }
            }
         }
      }
   }

   public static final boolean d() {
      return bh;
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (al) {
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
      return ai.getAppProperty(var0);
   }
}
