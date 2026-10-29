import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

final class d {
   static int[] a = new int[12288];
   int b;
   byte[] c;
   byte[] d;
   byte[] e;
   byte[] f;
   byte[] g;
   byte[] h;
   short[] i;
   short[] j;
   byte[] k;
   byte[] l;
   short[] m;
   byte[] n;
   short[] o;
   byte[] p;
   int q = -1;
   int r = -1;
   int s = -1;
   boolean t = false;
   int[] u;
   int[][][] v;
   byte[] w;
   int x;
   int y;
   public int z;
   boolean A;
   int B;
   short C;
   int[] D;
   int[] E;
   byte[] F;
   int[] G;
   Image[][] H;
   static d[] I = new d[23];
   static Image[][] J = new Image[4][];
   static short[][] K = new short[4][];
   static int[] L = new int[4];
   static int[] M = new int[4];
   static int[] N = new int[4];
   int O;
   int P = -1;
   static int Q;
   static int R;
   private int ac = 5;
   static int S = -1;
   static int T = -1;
   public static final int[] U = new int[]{3, 3, 6, 16, 3, 3, 6, 16, 3, 2};
   public static final int[] V = new int[]{5, 1, 16, 1, 6};
   public static final int[] W = new int[]{0, 5, 0, 8, 0};
   static int[] X = new int[200];
   static int Y;
   static String Z;
   int aa;
   int ab;

   final void a(byte[] var1, int var2) {
      try {
         System.gc();
         if ((short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8)) != 991) {
            System.out.println("ERROR: Invalid BSprite version !");
         }

         this.B = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8) + ((var1[var2++] & 255) << 16) + ((var1[var2++] & 255) << 24);
         int var4 = 0;
         if ((this.B & 1) != 0) {
            this.b = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8);
            if (this.b > 0) {
               if ((this.B & 4) != 0) {
                  this.e = new byte[this.b];
               }

               if ((this.B & 2) != 0 || (this.B & 4) != 0) {
                  this.c = new byte[this.b];
                  this.d = new byte[this.b];
               }

               this.f = new byte[this.b];
               this.g = new byte[this.b];

               for (int var5 = 0; var5 < this.b; var5++) {
                  if ((this.B & 4) != 0) {
                     this.e[var5] = var1[var2++];
                     if (this.e[var5] > var4) {
                        var4 = this.e[var5];
                     }
                  }

                  if ((this.B & 2) != 0) {
                     this.c[var5] = var1[var2++];
                     this.d[var5] = var1[var2++];
                  }

                  this.f[var5] = var1[var2++];
                  this.g[var5] = var1[var2++];
               }
            }

            var4++;
         }

         if ((this.B & 256) != 0) {
            int var63;
            if ((var63 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8)) > 0) {
               this.m = new short[var63 << 2];

               for (int var6 = 0; var6 < var63; var6++) {
                  this.m[var6 * 4] = var1[var2++];
                  if ((this.B & 1024) != 0) {
                     this.m[var6 * 4 + 1] = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8));
                     this.m[var6 * 4 + 2] = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8));
                  } else {
                     this.m[var6 * 4 + 1] = var1[var2++];
                     this.m[var6 * 4 + 2] = var1[var2++];
                  }

                  this.m[var6 * 4 + 3] = var1[var2++];
               }
            }

            int var68;
            if ((var68 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8)) > 0) {
               this.k = new byte[var68 << 2];
               System.arraycopy(var1, var2, this.k, 0, this.k.length);
               var2 += this.k.length;
            }

            if ((var68 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8)) > 0) {
               this.h = new byte[var68];
               this.i = new short[var68];
               short var7 = 0;
               this.j = new short[var68 + 1];
               this.j[0] = 0;

               for (int var8 = 0; var8 < var68; var8++) {
                  this.h[var8] = var1[var2++];
                  int var168 = ++var2;
                  var2++;
                  this.i[var8] = (short)((var1[var168] & 255) + ((var1[var2++] & 255) << 8));
                  var7 = (short)(var7 + var1[var2++]);
                  this.j[var8 + 1] = var7;
               }

               this.l = new byte[var68 << 2];
               System.arraycopy(var1, var2, this.l, 0, this.l.length);
               var2 += this.l.length;
            }
         }

         if ((this.B & 65536) != 0) {
            int var64;
            if ((var64 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8)) > 0) {
               this.p = new byte[var64 * 5];
               System.arraycopy(var1, var2, this.p, 0, this.p.length);
               var2 += this.p.length;
            }

            int var70;
            if ((var70 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8)) > 0) {
               this.n = new byte[var70];
               this.o = new short[var70];

               for (int var74 = 0; var74 < var70; var74++) {
                  this.n[var74] = var1[var2++];
                  int var170 = ++var2;
                  var2++;
                  this.o[var74] = (short)((var1[var170] & 255) + ((var1[var2++] & 255) << 8));
               }
            }
         }

         if ((this.B & 16777216) != 0) {
            short var65 = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8));
            this.v = new int[var4][this.x][];
            int[] var71 = new int[var4];

            for (int var75 = 0; var75 < var4; var75++) {
               int var79 = var1[var2++] & 255;
               this.v[var75] = new int[var79][];
               if (this.x < var79) {
                  this.x = var79;
               }

               int var9;
               if ((var9 = var1[var2++] & 255) == 0) {
                  var9 = 256;
               }

               var71[var75] = var9;

               for (int var10 = 0; var10 < var79; var10++) {
                  this.v[var75][var10] = new int[var9];
                  if (var65 == 21781) {
                     for (int var11 = 0; var11 < var9; var11++) {
                        int var12 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8);
                        int var13 = -16777216;
                        if ((var12 & 32768) != 32768) {
                           var13 = 0;
                           this.A = true;
                        }

                        this.v[var75][var10][var11] = var13 | (var12 & 31744) << 9 | (var12 & 992) << 6 | (var12 & 31) << 3;
                     }
                  }
               }
            }

            this.C = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8));
            if (this.C == 25840) {
               this.D = new int[var4];
               this.E = new int[var4];

               for (int var76 = 0; var76 < var4; var76++) {
                  int var80 = var71[var76] - 1;
                  this.D[var76] = 1;

                  for (this.E[var76] = 0; var80 != 0; this.E[var76]++) {
                     var80 >>= 1;
                     this.D[var76] = this.D[var76] << 1;
                  }

                  this.D[var76]--;
               }
            }

            if (this.b > 0) {
               this.G = new int[this.b];
               int var77 = 0;
               int var81 = var2;

               for (int var85 = 0; var85 < this.b; var85++) {
                  int var88 = (var1[var81++] & 255) + ((var1[var81++] & 255) << 8);
                  this.G[var85] = var77;
                  var81 += var88;
                  var77 += var88;
               }

               this.F = new byte[var77];

               for (int var86 = 0; var86 < this.b; var86++) {
                  int var89 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8);
                  System.arraycopy(var1, var2, this.F, this.G[var86], var89);
                  var2 += var89;
               }
            }
         }

         if ((this.B & 536870912) != 0 && (this.B & 4) == 0) {
            int var66 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8);
            this.F = new byte[var66];
            System.arraycopy(var1, var2, this.F, 0, var66);
            var2 += var66;
            this.x = var1[var2++] & 255;
            this.y = var1[var2++] & 255;
            if (this.y == 0) {
               this.y = 256;
            }

            int var72 = this.y * 3 + 4 + this.y + 4;
            this.w = new byte[this.x * var72];
            System.arraycopy(var1, var2, this.w, 0, this.x * var72);
            var2 += this.x * var72;
         }

         if ((this.B & 4) != 0 && (this.B & 16777216) == 0) {
            int var67 = var1[var2++] & 255;
            if (this.H == null) {
               this.H = new Image[var67][];
            }

            for (int var73 = 0; var73 < var67; var73++) {
               int var78;
               byte[] var84 = new byte[var78 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8)];
               System.arraycopy(var1, var2, var84, 0, var78);
               var2 += var78;
               int var87;
               if ((var87 = var1[var2++] & 255) == 0) {
                  this.H[var73] = new Image[1];
                  this.H[var73][0] = Image.createImage(var84, 0, var84.length);
               } else {
                  if (this.H[var73] == null) {
                     this.H[var73] = new Image[var87];
                  }

                  int var90;
                  if ((var90 = var1[var2++] & 255) == 0) {
                     var90 = 256;
                  }

                  for (int var91 = 0; var91 < var87; var91++) {
                     System.gc();
                     int var92;
                     byte[] var93 = new byte[var92 = var90 * 3 + 4];
                     System.arraycopy(var1, var2, var93, 0, var92);
                     var2 += var92;
                     System.arraycopy(var93, 0, var84, 41, var92);
                     byte[] var14 = new byte[var90 + 4];
                     System.arraycopy(var1, var2, var14, 0, var90 + 4);
                     var2 += var90 + 4;
                     System.arraycopy(var14, 0, var84, 49 + var92, var14.length);
                     this.H[var73][var91] = Image.createImage(var84, 0, var84.length);
                  }
               }
            }
         }

         System.gc();
      } catch (Exception var15) {
         c.a(var15, "ASprite.Load()");
      }
   }

   final void a() {
      this.F = null;
      this.G = null;
      if ((this.B & 536870912) != 0) {
         this.w = null;
      }

      System.gc();
   }

   final void a(int var1, int var2, int var3, int var4) {
      this.a((d)null, var1, var2, var3, var4);
   }

   final void a(d var1, int var2, int var3, int var4, int var5) {
      if (var4 == -1) {
         var4 = this.b - 1;
      }

      if (this.H == null) {
         this.H = new Image[this.x][];
      }

      if ((this.B & 16777216) != 0) {
         if (this.H[var2] == null) {
            this.H[var2] = new Image[this.b];
         }

         if (var5 >= 0) {
            for (int var6 = var3; var6 <= var4; var6++) {
               this.H[var2][var6] = this.H[var5][var6];
            }
         } else {
            int var19 = this.z;
            this.z = var2;
            boolean var7 = false;
            boolean var8 = false;
            System.gc();
            Runtime.getRuntime().freeMemory();
            Object var11 = null;

            for (int var12 = var3; var12 <= var4; var12++) {
               int var13 = this.f[var12] & 255;
               int var14 = this.g[var12] & 255;
               int[] var15;
               if (var13 > 0 && var14 > 0 && (var15 = this.f(var12, 0)) != null) {
                  boolean var16 = false;
                  int var17 = var13 * var14;

                  for (int var18 = 0; var18 < var17; var18++) {
                     if ((var15[var18] & 0xFF000000) != -16777216) {
                        var16 = true;
                        break;
                     }
                  }

                  this.H[var2][var12] = Image.createRGBImage(var15, var13, var14, var16);
               }
            }

            if (var1 != null) {
               var1.a();
            }

            System.gc();
            this.z = var19;
         }
      }

      if ((this.B & 536870912) != 0) {
         if (this.H[var2] == null) {
            this.H[var2] = new Image[1];
         }

         int var20 = this.y * 3 + 4;
         int var21 = this.y + 4;
         System.arraycopy(this.w, var2 * (var20 + var21), this.F, 41, var20);
         System.arraycopy(this.w, var2 * (var20 + var21) + var20, this.F, 41 + var20 + 8, var21);
         this.H[var2][0] = Image.createImage(this.F, 0, this.F.length);
      }

      System.gc();
   }

   final int a(int var1, int var2) {
      return this.p[(this.o[var1] + var2) * 5 + 1] & 0xFF;
   }

   final int a(int var1) {
      return this.n[var1] & 0xFF;
   }

   final int b(int var1) {
      int var2 = var1 << 2;
      return this.l[var2 + 2];
   }

   final int b(int var1, int var2) {
      int var3 = this.i[var1] + var2 << 2;
      return this.m[var3 + 1];
   }

   final int c(int var1, int var2) {
      int var3 = this.i[var1] + var2 << 2;
      return this.m[var3 + 2];
   }

   final int d(int var1, int var2) {
      int var3 = this.i[var1] + var2 << 2;
      int var4 = this.m[var3] & 255 | (this.m[var3 + 3] & 255 & 192) << 2;
      return this.f[var4] & 0xFF;
   }

   final int e(int var1, int var2) {
      int var3 = this.i[var1] + var2 << 2;
      int var4 = this.m[var3] & 255 | (this.m[var3 + 3] & 255 & 192) << 2;
      return this.g[var4] & 0xFF;
   }

   final void a(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = (this.o[var2] + var3) * 5;
      int var9 = this.p[var8] & 255 | (this.p[var8 + 4] & 192) << 2;
      if ((var5 & 1) != 0) {
         var6 += this.p[var8 + 2];
      } else {
         var6 -= this.p[var8 + 2];
      }

      if ((var5 & 2) != 0) {
         var7 += this.p[var8 + 3];
      } else {
         var7 -= this.p[var8 + 3];
      }

      this.a(var1, var9, var4, var5 ^ this.p[var8 + 4] & 15, var6, var7);
   }

   final void a(int[] var1, int var2, int var3, int var4, int var5, int var6) {
      if (var3 != -1) {
         var3 += this.j[var2];
         if (this.j[var2 + 1] <= var3) {
            var1[0] = var1[1] = var1[2] = var1[3] = 0;
         } else {
            int var24 = var3 << 2;
            byte var29 = this.k[var24++];
            byte var31 = this.k[var24++];
            int var33 = this.k[var24++] & 255;
            int var35 = this.k[var24] & 255;
            if ((var4 & 1) != 0) {
               var5 += var29 + var33;
            } else {
               var5 -= var29;
            }

            if ((var4 & 2) != 0) {
               var6 += var31 + var35;
            } else {
               var6 -= var31;
            }

            var1[0] = -var5;
            var1[1] = -var6;
            var1[2] = var1[0] + var33;
            var1[3] = var1[1] + var35;
         }
      } else if ((this.B & 1024) != 0) {
         int var23 = 0;
         int var28 = 0;
         int var30 = 0;
         int var32 = 0;
         int var34 = this.h[var2] & 255;

         for (int var12 = 0; var12 < var34; var12++) {
            this.a(var1, var2, var12);
            if (var1[0] < var23) {
               var30 = var23 + var30 - var1[0];
               var23 = var1[0];
            }

            if (var1[1] < var28) {
               var32 = var28 + var32 - var1[1];
               var28 = var1[1];
            }

            if (var1[2] > var23 + var30) {
               var30 = var1[2] - var23;
            }

            if (var1[3] > var28 + var32) {
               var32 = var1[3] - var28;
            }
         }

         if ((var4 & 1) != 0) {
            var5 += var23 + var30;
         } else {
            var5 -= var23;
         }

         if ((var4 & 2) != 0) {
            var6 += var28 + var32;
         } else {
            var6 -= var28;
         }

         var1[0] = -var5;
         var1[1] = -var6;
         var1[2] = var1[0] + var30;
         var1[3] = var1[1] + var32;
      } else {
         int var7 = var2 << 2;
         byte var8 = this.l[var7++];
         byte var9 = this.l[var7++];
         int var10 = this.l[var7++] & 255;
         int var11 = this.l[var7] & 255;
         if ((var4 & 1) != 0) {
            var5 += var8 + var10;
         } else {
            var5 -= var8;
         }

         if ((var4 & 2) != 0) {
            var6 += var9 + var11;
         } else {
            var6 -= var9;
         }

         var1[0] = -var5;
         var1[1] = -var6;
         var1[2] = var1[0] + var10;
         var1[3] = var1[1] + var11;
      }
   }

   final void a(int[] var1, int var2, int var3) {
      int var4 = this.i[var2] + var3 << 2;
      int var5 = this.m[var4 + 3] & 255;
      int var6 = this.m[var4] & 255 | (var5 & 192) << 2;
      if ((var5 & 16) != 0) {
         this.a(var1, var6, -1, 0, 0, 0);
      } else {
         var1[0] = this.b(var2, var3);
         var1[1] = this.c(var2, var3);
         var1[2] = var1[0] + this.d(var2, var3);
         var1[3] = var1[1] + this.e(var2, var3);
      }
   }

   final void b(int[] var1, int var2, int var3) {
      var1[0] = 0;
      var1[1] = 0;
      var1[2] = this.f[var2] & 255;
      var1[3] = this.g[var2] & 255;
   }

   final void a(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      int var9 = (this.o[var2] + var3) * 5;
      int var10 = this.p[var9] & 255 | (this.p[var9 + 4] & 192) << 2;
      if ((var6 & 1) != 0) {
         var7 += this.p[var9 + 2];
      } else {
         var7 -= this.p[var9 + 2];
      }

      if ((var6 & 2) != 0) {
         var8 += this.p[var9 + 3];
      } else {
         var8 -= this.p[var9 + 3];
      }

      this.a(var1, var10, var4 - var7, var5 - var8, var6 ^ this.p[var9 + 4] & 15, var7, var8);
   }

   final void a(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = this.h[var2] & 255;

      for (int var9 = 0; var9 < var8; var9++) {
         if (!this.t || this.u[var9] != 1) {
            this.b(var1, var2, var9, var3, var4, var5, var6, var7);
         }
      }
   }

   final void b(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      int var9 = this.i[var2] + var3 << 2;
      int var10 = this.m[var9 + 3] & 255;
      int var11 = this.m[var9] & 255 | (var10 & 192) << 2;
      if ((var6 & 1) != 0) {
         var4 -= this.m[var9 + 1];
      } else {
         var4 += this.m[var9 + 1];
      }

      if ((var6 & 2) != 0) {
         var5 -= this.m[var9 + 2];
      } else {
         var5 += this.m[var9 + 2];
      }

      if ((var10 & 16) != 0) {
         this.a(var1, var11, var4, var5, var6 ^ var10 & 15, var7, var8);
      } else {
         if ((var6 & 1) != 0) {
            var4 -= this.f[var11] & 255;
         }

         if ((var6 & 2) != 0) {
            var5 -= this.g[var11] & 255;
         }

         this.a(var1, var11, var4, var5, var6 ^ var10 & 15);
      }
   }

   final void a(Graphics var1, int var2, int var3, int var4, int var5) {
      int var6 = this.f[var2] & 255;
      int var7 = this.g[var2] & 255;
      if (var6 > 0 && var7 > 0) {
         Image var8 = null;
         if (this.H != null && this.H[this.z] != null) {
            if ((this.B & 4) != 0) {
               if (this.z >= this.H[this.e[var2]].length) {
                  this.z = 0;
               }

               var8 = this.H[this.e[var2]][this.z];
            } else if ((this.B & 536870912) != 0) {
               var8 = this.H[this.z][0];
            } else {
               var8 = this.H[this.z][var2];
            }
         } else if (this.P >= 0) {
            int var9 = var2;
            int var14;
            var2 = (var14 = var2 + this.O) + this.z * this.b;
            if (J[this.P][var2] == null) {
               int var10 = M[this.P];
               short var11;
               if ((var11 = K[this.P][var10]) >= 0) {
                  J[this.P][var11] = null;
               }

               K[this.P][var10] = (short)var2;
               M[this.P] = (M[this.P] + 1) % N[this.P];
               int[] var12;
               if ((var12 = this.f(var9, var5)) == null) {
                  System.out.println("DecodeImage() FAILED !");
                  return;
               }

               J[this.P][var2] = Image.createRGBImage(var12, var6, var7, this.A);
            }

            var8 = J[this.P][var2];
         }

         if (this.q == -1
            || ((1 & this.q) == 0 || var3 + var6 > this.r)
               && ((2 & this.q) == 0 || var3 < this.r)
               && ((4 & this.q) == 0 || var4 + var7 > this.s)
               && ((8 & this.q) == 0 || var4 < this.s)) {
            int var15 = 0;
            int var16 = 0;
            int[] var17 = null;
            int var18 = 0;
            int var13 = var6;
            if (var8 == null) {
               if ((var17 = this.f(var2, var5)) == null) {
                  System.out.println("DecodeImage() FAILED !");
                  return;
               }

               if (var6 + var3 > 0 && var7 + var4 > 0) {
                  if (var4 < 0) {
                     var18 = 0 - var4 * var6;
                     var7 += var4;
                     var4 = 0;
                  }

                  if (var3 < 0) {
                     var18 -= var3;
                     var6 += var3;
                     var3 = 0;
                  }
               }
            } else {
               var6 = var8.getWidth();
               var7 = var8.getHeight();
               if ((this.B & 536870912) != 0) {
                  var15 = this.c[var2] & 255;
                  var16 = this.d[var2] & 255;
                  var6 = this.f[var2] & 255;
                  var7 = this.g[var2] & 255;
               }
            }

            if (this.q != -1) {
               if (this.r != -1) {
                  if ((1 & this.q) != 0 && var3 + var6 > this.r && var3 < this.r) {
                     var18 += this.r - var3;
                     var6 = var3 + var6 - this.r;
                     var3 = this.r;
                  }

                  if ((2 & this.q) != 0 && var3 + var6 > this.r && var3 < this.r) {
                     var6 = this.r - var3;
                  }
               }

               if (this.s != -1) {
                  if ((8 & this.q) != 0 && var4 + var7 > this.s && var4 < this.s) {
                     var7 = this.s - var4;
                  }

                  if ((4 & this.q) != 0 && var4 + var7 > this.s && var4 < this.s) {
                     var18 = (this.s - var4) * var13 + (var13 - var6);
                     var7 -= this.s - var4;
                     var4 = this.s;
                  }
               }
            }

            if (var8 == null) {
               if (var6 + var3 > 0 && var7 + var4 > 0) {
                  var1.drawRGB(var17, var18, var13, var3, var4, var6, var7, this.A);
               }
            } else if ((var5 & 1) != 0) {
               if ((var5 & 2) != 0) {
                  var1.drawRegion(var8, var15, var16, var6, var7, 3, var3, var4, 0);
               } else {
                  var1.drawRegion(var8, var15, var16, var6, var7, 2, var3, var4, 0);
               }
            } else if ((var5 & 2) != 0) {
               var1.drawRegion(var8, var15, var16, var6, var7, 1, var3, var4, 0);
            } else {
               var1.drawRegion(var8, var15, var16, var6, var7, 0, var3, var4, 0);
            }
         }
      }
   }

   final int[] f(int var1, int var2) {
      return this.c(a, var1, var2);
   }

   final int[] c(int[] var1, int var2, int var3) {
      if (this.F != null && this.G != null) {
         int var4 = this.f[var2] & 255;
         int var5 = this.g[var2] & 255;
         if (var4 * var5 > a.length) {
            System.out.println("ERROR: sizeX x sizeY > temp.length (" + var4 + " x " + var5 + " = " + var4 * var5 + " > " + a.length + ") !!!");
            return null;
         }

         int[] var6 = var1;
         byte var8 = 0;
         int[] var7;
         if ((this.B & 4) != 0) {
            var8 = this.e[var2];
            var7 = this.v[var8][this.z];
         } else {
            var7 = this.v[0][this.z];
         }

         if (var7 == null) {
            return null;
         }

         byte[] var9 = this.F;
         int var10 = this.G[var2];
         int var11 = 0;
         int var12 = var4 * var5;
         if (this.C != 25840) {
            if (this.C == 10225) {
               while (var11 < var12) {
                  int var31;
                  if ((var31 = var9[var10++] & 255) > 127) {
                     int var37 = var9[var10++] & 255;
                     int var15 = var7[var37];
                     var31 -= 128;

                     while (var31-- > 0) {
                        var6[var11++] = var15;
                     }
                  } else {
                     var6[var11++] = var7[var31];
                  }
               }
            } else if (this.C == 22258) {
               while (var11 < var12) {
                  int var29;
                  if ((var29 = var9[var10++] & 255) > 127) {
                     var29 -= 128;

                     while (var29-- > 0) {
                        int var36 = var7[var9[var10++] & 0xFF];
                        var6[var11++] = var36;
                     }
                  } else {
                     int var35 = var7[var9[var10++] & 0xFF];

                     while (var29-- > 0) {
                        var6[var11++] = var35;
                     }
                  }
               }
            } else if (this.C == 5632) {
               while (var11 < var12) {
                  var6[var11++] = var7[var9[var10] >> 4 & 15];
                  var6[var11++] = var7[var9[var10] & 15];
                  var10++;
               }
            } else if (this.C == 1024) {
               while (var11 < var12) {
                  var6[var11++] = var7[var9[var10] >> 6 & 3];
                  var6[var11++] = var7[var9[var10] >> 4 & 3];
                  var6[var11++] = var7[var9[var10] >> 2 & 3];
                  var6[var11++] = var7[var9[var10] & 3];
                  var10++;
               }
            } else if (this.C == 512) {
               while (var11 < var12) {
                  var6[var11++] = var7[var9[var10] >> 7 & 1];
                  var6[var11++] = var7[var9[var10] >> 6 & 1];
                  var6[var11++] = var7[var9[var10] >> 5 & 1];
                  var6[var11++] = var7[var9[var10] >> 4 & 1];
                  var6[var11++] = var7[var9[var10] >> 3 & 1];
                  var6[var11++] = var7[var9[var10] >> 2 & 1];
                  var6[var11++] = var7[var9[var10] >> 1 & 1];
                  var6[var11++] = var7[var9[var10] & 1];
                  var10++;
               }
            } else if (this.C == 22018) {
               while (var11 < var12) {
                  var6[var11++] = var7[var9[var10++] & 0xFF];
               }
            }
         } else {
            while (var11 < var12) {
               int var13 = var9[var10++] & 255;
               int var14 = var7[var13 & this.D[var8]];
               var13 >>= this.E[var8];

               while (var13-- >= 0) {
                  var6[var11++] = var14;
               }
            }
         }

         if (this.P < 0) {
            if ((var3 & 1) != 0) {
               int var38 = 0;

               for (int var40 = var4 - 1; var38 < var40; var40--) {
                  for (int var16 = 0; var16 < var12; var16 += var4) {
                     int var33 = var6[var16 + var38];
                     var6[var16 + var38] = var6[var16 + var40];
                     var6[var16 + var40] = var33;
                  }

                  var38++;
               }
            }

            if ((var3 & 2) != 0) {
               int var39 = 0;

               for (int var41 = (var5 - 1) * var4; var39 < var41; var41 -= var4) {
                  for (int var42 = 0; var42 < var4; var42++) {
                     int var34 = var6[var39 + var42];
                     var6[var39 + var42] = var6[var41 + var42];
                     var6[var41 + var42] = var34;
                  }

                  var39 += var4;
               }
            }
         }

         return var6;
      } else {
         return null;
      }
   }

   public static final void g(int var0, int var1) {
      if (var1 < 0) {
         var1 = L[var0];
      }

      N[var0] = var1;
      K[var0] = new short[var1];

      for (int var2 = 0; var2 < K[var0].length; var2++) {
         K[var0][var2] = -1;
      }

      J[var0] = new Image[L[var0]];
   }

   public static final void c(int var0) {
      K[var0] = null;
      M[var0] = 0;
      N[var0] = 0;
      J[var0] = null;
      L[var0] = 0;
   }

   final void d(int var1) {
      this.O = L[var1];
      L[var1] = L[var1] + this.x * this.b;
      this.P = var1;
   }

   final int a(String var1) {
      Q = 0;
      R = this.g[1] & 255;
      int var9 = 0;
      int var3 = S >= 0 ? S : 0;
      int var4 = T >= 0 ? T : var1.length();

      for (int var5 = var3; var5 < var4; var5++) {
         char var6;
         if ((var6 = var1.charAt(var5)) != '{' && var6 != '}') {
            if (var6 == '~') {
               var5++;
               var9 += this.f[0] * 2;
            } else {
               int var7;
               if ((var7 = this.a(var6)) == -1) {
                  var9 += this.f[0] & 255;
               } else {
                  int var8 = e.a.b(var7);
                  var9 += var8;
               }

               var9--;
            }
         }
      }

      if (var9 > Q) {
         Q = var9;
      }

      return var9;
   }

   public final int a(char var1) {
      try {
         int var2;
         if ((var2 = " !\"#$%&'()*+,-./0123456789:;<=>?©@ABCDEFGHIJKLMNOPQRSTUVWXYZ_ÁÉÍÓÚÝÇÐÊÑ®Ü¦Û£".indexOf(var1)) == -1) {
            var2 = 31;
            System.out.println(".... unknown char: " + var1);
         }

         return var2;
      } catch (Exception var3) {
         System.out.println(var3.getMessage() + " getModule char");
         return 31;
      }
   }

   final void a(Graphics var1, String var2, int var3, int var4, int var5) {
      int var6;
      int var7 = (var6 = var1.getClipY()) + var1.getClipHeight();
      if (var4 >= var6 - 20 && var4 <= var7) {
         var2 = var2.toUpperCase();
         if ((var5 & 43) != 0) {
            this.a(var2);
            if ((var5 & 8) != 0) {
               var3 -= Q;
            } else if ((var5 & 1) != 0) {
               var3 -= Q >> 1;
            }

            if ((var5 & 32) != 0) {
               var4 -= R;
            } else if ((var5 & 2) != 0) {
               var4 -= R >> 1;
            }
         }

         int var18 = var3;
         int var9 = var4;
         int var10 = S >= 0 ? S : 0;
         int var11 = T >= 0 ? T : var2.length();
         boolean var12 = false;

         for (int var13 = var10; var13 < var11; var13++) {
            char var14;
            if ((var14 = var2.charAt(var13)) == '{') {
               this.z = 1;
               this.ab = this.z;
            } else if (var14 == '}') {
               this.z = this.aa;
               this.ab = this.aa;
            } else if (var14 == '~') {
               var13++;
               int var15 = var2.charAt(var13) - '0';
               e.b[33].a(var1, var15, e.z % V[var15], var18 + W[var15], var9 + (this.g[1] & 255) - U[(var15 << 1) + 1], 0, 0, 0);
               var18 += this.f[0] + U[var15 << 1] + W[var15] / 2;
            } else {
               int var19;
               if ((var19 = this.a(var14)) == -1) {
                  var18 += this.f[0] & 255;
               }

               this.a(var1, var19, var18, var9, 0, 0, 0);
               int var16 = e.a.b(var19);
               var18 += var16 & 0xFF;
               var18--;
            }
         }
      }
   }

   public final void a(String var1, int var2) {
      if (Z == null || !Z.equals(var1)) {
         Z = var1;
         Y = 0;
         int var3 = 0;
         int var4 = 0;
         int var12 = 0;
         int var6 = var1.length();

         for (int var7 = 0; var7 < var6; var7++) {
            char var8;
            if ((var8 = var1.charAt(var7)) == '^' && var12 <= var2) {
               X[var3++] = var7;
               var12 = 0;
               var4 = 0;
            } else if (var2 > 0 && (var8 == '^' || var8 == ' ' || var7 == var6 - 1) && var12 > var2) {
               do {
                  var7--;
               } while (var1.charAt(var7) != ' ' && var7 > (var3 > 0 ? X[var3 - 1] : 0));

               if (var7 == (var3 > 0 ? X[var3 - 1] : 0)) {
                  var7 = (var3 > 0 ? X[var3 - 1] : 0) + var4;
               }

               X[var3++] = var7;
               Y = var3;
               var4 = 0;
               var12 = 0;
            } else {
               var4++;
               if (var8 != '{' && var8 != '}') {
                  if (var8 == '~') {
                     var7++;
                     var12 += this.f[0] * 2;
                  } else {
                     int var9;
                     if ((var9 = this.a(var8)) == -1) {
                        var12 += this.f[0] & 255;
                     } else {
                        int var10 = this.m[var9 << 2] & 255;
                        var12 += (this.f[var10] & 255) + this.m[1];
                     }

                     if (this.z == 5) {
                        var12 -= 2;
                     } else {
                        var12--;
                     }
                  }
               }
            }
         }

         X[var3++] = var6;
         Y = var3;
         e.bb = Y;
      }
   }

   final int a(Graphics var1, String var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      this.ab = this.z;
      this.aa = this.z;
      var2 = '}' + var2;
      this.a(var2, var6);
      int var9 = this.ac + (this.g[1] & 255);
      if (e.x == 20) {
         var9 += 3;
      }

      if ((var5 & 32) != 0) {
         var4 -= var9 * (Y - 1);
      } else if ((var5 & 2) != 0) {
         var4 -= var9 * (Y - 1) >> 1;
      }

      for (int var10 = var7; var10 < Y; var10++) {
         S = var10 > 0 ? X[var10 - 1] + 1 : 0;
         T = X[var10];
         this.a(var1, var2, var3, var4 + (var10 - var7) * var9, var5);
         if (var8 != -1 && var10 - var7 >= var8 - 1) {
            S = -1;
            T = -1;
            return var4 + var10 * var9;
         }
      }

      this.z = this.aa;
      S = -1;
      T = -1;
      return var4 + Y * var9;
   }

   static {
      " !\"#$%&'*+,_./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ()-ÄÖÜß¡¿ÁÀÃÈÉÊÎÏÍÓÔÕÚÙÑÇÒ©ĞÂÌŞİ".length();
   }
}
