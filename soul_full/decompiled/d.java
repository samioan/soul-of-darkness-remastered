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
   short[] l;
   byte[] m;
   short[] n;
   byte[] o;
   int p = -1;
   int q = -1;
   int r = -1;
   boolean s = false;
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
   public static Image[] I;
   public static final int[] J = new int[]{6, 7, 8, 9, 12, 14};
   public int K;
   public boolean L;
   public boolean M;
   public static boolean N;
   int O;
   public boolean P;
   public int Q;
   public int R;
   static int S;
   static int T;
   private int ad = 4;
   static int U = -1;
   static int V = -1;
   public static final int[] W = new int[]{3, 3, 6, 9, 3, 3, 6, 9, 3, 2};
   public static final int[] X = new int[]{5, 1, 16, 1, 6};
   public static final int[] Y = new int[]{0, 5, 0, 8, 0};
   static int Z;
   static int[] aa = new int[200];
   static int ab;
   static String ac;

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
               this.l = new short[var63 << 2];

               for (int var6 = 0; var6 < var63; var6++) {
                  this.l[var6 * 4] = var1[var2++];
                  if ((this.B & 1024) != 0) {
                     this.l[var6 * 4 + 1] = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8));
                     this.l[var6 * 4 + 2] = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8));
                  } else {
                     this.l[var6 * 4 + 1] = var1[var2++];
                     this.l[var6 * 4 + 2] = var1[var2++];
                  }

                  this.l[var6 * 4 + 3] = var1[var2++];
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
            }
         }

         if ((this.B & 65536) != 0) {
            int var64;
            if ((var64 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8)) > 0) {
               this.o = new byte[var64 * 5];
               System.arraycopy(var1, var2, this.o, 0, this.o.length);
               var2 += this.o.length;
            }

            int var70;
            if ((var70 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8)) > 0) {
               this.m = new byte[var70];
               this.n = new short[var70];

               for (int var74 = 0; var74 < var70; var74++) {
                  this.m[var74] = var1[var2++];
                  int var170 = ++var2;
                  var2++;
                  this.n[var74] = (short)((var1[var170] & 255) + ((var1[var2++] & 255) << 8));
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

                     this.O = 16253176;
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
            int[] var11 = null;
            if (var1 != null) {
               var11 = new int[12288];
            }

            for (int var12 = var3; var12 <= var4; var12++) {
               int var13 = this.f[var12] & 255;
               int var14 = this.g[var12] & 255;
               if (var13 > 0 && var14 > 0) {
                  int[] var15 = this.e(var12, 0);
                  if (var1 != null) {
                     int[] var16 = var1.c(var11, var12, 0);

                     for (int var17 = 0; var17 < 12288; var17++) {
                        if (var15[var17] != this.O) {
                           var15[var17] = var15[var17] & 16777215 | var16[var17] << 24;
                        }
                     }
                  }

                  if (var15 != null) {
                     boolean var22 = false;
                     int var23 = var13 * var14;

                     for (int var18 = 0; var18 < var23; var18++) {
                        if ((var15[var18] & 0xFF000000) != -16777216) {
                           var22 = true;
                           break;
                        }
                     }

                     this.H[var2][var12] = Image.createRGBImage(var15, var13, var14, var22);
                  }
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

   final int a(int var1, int var2, int var3) {
      return (this.o[(this.n[var1] + var2) * 5 + 1] & 0xFF) << var3;
   }

   final int a(int var1) {
      return this.m[var1] & 0xFF;
   }

   final int a(int var1, int var2) {
      int var3 = this.i[var1] + var2 << 2;
      return this.l[var3 + 1];
   }

   final int b(int var1, int var2) {
      int var3 = this.i[var1] + var2 << 2;
      return this.l[var3 + 2];
   }

   final int c(int var1, int var2) {
      int var3 = this.i[var1] + var2 << 2;
      int var4 = this.l[var3] & 255 | (this.l[var3 + 3] & 255 & 192) << 2;
      return this.f[var4] & 0xFF;
   }

   final int d(int var1, int var2) {
      int var3 = this.i[var1] + var2 << 2;
      int var4 = this.l[var3] & 255 | (this.l[var3 + 3] & 255 & 192) << 2;
      return this.g[var4] & 0xFF;
   }

   final void a(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = (this.n[var2] + var3) * 5;
      int var9 = this.o[var8] & 255 | (this.o[var8 + 4] & 192) << 2;
      if ((var5 & 1) != 0) {
         var6 += this.o[var8 + 2];
      } else {
         var6 -= this.o[var8 + 2];
      }

      if ((var5 & 2) != 0) {
         var7 += this.o[var8 + 3];
      } else {
         var7 -= this.o[var8 + 3];
      }

      this.a(var1, var9, var4, var5 ^ this.o[var8 + 4] & 15, var6, var7);
   }

   final void a(int[] var1, int var2, int var3, int var4, int var5, int var6) {
      if (var3 != -1) {
         var3 += this.j[var2];
         if (this.j[var2 + 1] <= var3) {
            var1[0] = var1[1] = var1[2] = var1[3] = 0;
         } else {
            int var18 = var3 << 2;
            byte var22 = this.k[var18++];
            byte var23 = this.k[var18++];
            int var24 = this.k[var18++] & 255;
            int var25 = this.k[var18] & 255;
            if ((var4 & 1) != 0) {
               var5 += var22 + var24;
            } else {
               var5 -= var22;
            }

            if ((var4 & 2) != 0) {
               var6 += var23 + var25;
            } else {
               var6 -= var23;
            }

            var1[0] = -var5;
            var1[1] = -var6;
            var1[2] = var1[0] + var24;
            var1[3] = var1[1] + var25;
         }
      } else {
         int var7 = 0;
         int var8 = 0;
         int var9 = 0;
         int var10 = 0;
         int var11 = this.h[var2] & 255;

         for (int var12 = 0; var12 < var11; var12++) {
            this.a(var1, var2, var12);
            if (var1[0] < var7) {
               var9 = var7 + var9 - var1[0];
               var7 = var1[0];
            }

            if (var1[1] < var8) {
               var10 = var8 + var10 - var1[1];
               var8 = var1[1];
            }

            if (var1[2] > var7 + var9) {
               var9 = var1[2] - var7;
            }

            if (var1[3] > var8 + var10) {
               var10 = var1[3] - var8;
            }
         }

         if ((var4 & 1) != 0) {
            var5 += var7 + var9;
         } else {
            var5 -= var7;
         }

         if ((var4 & 2) != 0) {
            var6 += var8 + var10;
         } else {
            var6 -= var8;
         }

         var1[0] = -var5;
         var1[1] = -var6;
         var1[2] = var1[0] + var9;
         var1[3] = var1[1] + var10;
      }
   }

   final void a(int[] var1, int var2, int var3) {
      int var4 = this.i[var2] + var3 << 2;
      int var5 = this.l[var4 + 3] & 255;
      int var6 = this.l[var4] & 255 | (var5 & 192) << 2;
      if ((var5 & 16) != 0) {
         this.a(var1, var6, -1, 0, 0, 0);
      } else {
         var1[0] = this.a(var2, var3);
         var1[1] = this.b(var2, var3);
         var1[2] = var1[0] + this.c(var2, var3);
         var1[3] = var1[1] + this.d(var2, var3);
      }
   }

   final void b(int[] var1, int var2, int var3) {
      var1[0] = 0;
      var1[1] = 0;
      var1[2] = this.f[var2] & 255;
      var1[3] = this.g[var2] & 255;
   }

   final void a(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      int var9 = (this.n[var2] + var3) * 5;
      int var10 = this.o[var9] & 255 | (this.o[var9 + 4] & 192) << 2;
      if ((var6 & 1) != 0) {
         var7 += this.o[var9 + 2];
      } else {
         var7 -= this.o[var9 + 2];
      }

      if ((var6 & 2) != 0) {
         var8 += this.o[var9 + 3];
      } else {
         var8 -= this.o[var9 + 3];
      }

      this.a(var1, var10, var4 - var7, var5 - var8, var6 ^ this.o[var9 + 4] & 15, var7, var8);
   }

   final void a(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = this.h[var2] & 255;

      for (int var9 = 0; var9 < var8; var9++) {
         if (!this.s || this.u[var9] != 1) {
            this.b(var1, var2, var9, var3, var4, var5, var6, var7);
         }
      }
   }

   final void b(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      int var9 = this.i[var2] + var3 << 2;
      int var10 = this.l[var9 + 3] & 255;
      int var11 = this.l[var9] & 255 | (var10 & 192) << 2;
      if (this.P) {
         if (I == null) {
            if ((var6 & 1) != 0) {
               var4 -= this.l[var9 + 1] * this.Q / this.R;
            } else {
               var4 += this.l[var9 + 1] * this.Q / this.R;
            }

            if ((var6 & 2) != 0) {
               var5 -= this.l[var9 + 2] * this.Q / this.R;
            } else {
               var5 += this.l[var9 + 2] * this.Q / this.R;
            }
         } else {
            if ((var6 & 1) != 0) {
               var4 -= this.l[var9 + 1] * J[this.Q] / this.R;
            } else {
               var4 += this.l[var9 + 1] * J[this.Q] / this.R;
            }

            if ((var6 & 2) != 0) {
               var5 -= this.l[var9 + 2] * J[this.Q] / this.R;
            } else {
               var5 += this.l[var9 + 2] * J[this.Q] / this.R;
            }
         }
      } else {
         if ((var6 & 1) != 0) {
            var4 -= this.l[var9 + 1];
         } else {
            var4 += this.l[var9 + 1];
         }

         if ((var6 & 2) != 0) {
            var5 -= this.l[var9 + 2];
         } else {
            var5 += this.l[var9 + 2];
         }
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

   final void b() {
      Image var1 = null;
      if (this.z >= this.H[0].length) {
         this.z = 0;
      }

      int var2 = (var1 = this.H[0][this.z]).getWidth();
      int var3 = var1.getHeight();
      I = new Image[J.length];

      for (int var4 = 0; var4 < J.length; var4++) {
         I[var4] = a(var1, var2 * J[var4] / this.R, var3 * J[var4] / 10);
      }
   }

   final void a(Graphics var1, int var2, int var3, int var4, int var5) {
      int var6 = this.f[var2] & 255;
      int var7 = this.g[var2] & 255;
      if (var6 > 0 && var7 > 0) {
         Image var8 = null;
         if (this.H != null) {
            if ((this.B & 4) != 0) {
               if (this.z >= this.H[this.e[var2]].length) {
                  this.z = 0;
               }

               var8 = this.H[this.e[var2]][this.z];
            } else if (this.H[this.z] != null) {
               if ((this.B & 536870912) != 0) {
                  var8 = this.H[this.z][0];
               } else {
                  var8 = this.H[this.z][var2];
               }
            }
         }

         if (this.p == -1
            || ((1 & this.p) == 0 || var3 + var6 > this.q)
               && ((2 & this.p) == 0 || var3 < this.q)
               && ((4 & this.p) == 0 || var4 + var7 > this.r)
               && ((8 & this.p) == 0 || var4 < this.r)) {
            int var9 = 0;
            int var10 = 0;
            int[] var11 = null;
            int var12 = 0;
            int var13 = var6;
            if (var8 == null) {
               if ((var11 = this.e(var2, var5)) == null) {
                  System.out.println("DecodeImage() FAILED !");
                  return;
               }

               if (var6 + var3 > 0 && var7 + var4 > 0) {
                  if (var4 < 0) {
                     var12 = 0 - var4 * var6;
                     var7 += var4;
                     var4 = 0;
                  }

                  if (var3 < 0) {
                     var12 -= var3;
                     var6 += var3;
                     var3 = 0;
                  }
               }
            } else {
               var6 = var8.getWidth();
               var7 = var8.getHeight();
               if (this.P) {
                  if (I == null) {
                     var8 = a(var8, var6 * this.Q / this.R, var7 * this.Q / this.R);
                  } else {
                     var8 = I[this.Q];
                  }
               }

               if ((this.B & 536870912) != 0 || (this.B & 4) != 0) {
                  var9 = this.c[var2] & 255;
                  var10 = this.d[var2] & 255;
                  var6 = this.f[var2] & 255;
                  var7 = this.g[var2] & 255;
                  if (this.P) {
                     if (I == null) {
                        var9 = var9 * this.Q / this.R;
                        var10 = var10 * this.Q / this.R;
                     } else {
                        var9 = var9 * J[this.Q] / this.R;
                        var10 = var10 * J[this.Q] / this.R;
                     }
                  }
               }

               if (this.P) {
                  if (I == null) {
                     var6 = var6 * this.Q / this.R;
                     var7 = var7 * this.Q / this.R;
                  } else {
                     var6 = var6 * J[this.Q] / this.R;
                     var7 = var7 * J[this.Q] / this.R;
                  }
               }
            }

            if (this.p != -1) {
               if (this.q != -1) {
                  if ((1 & this.p) != 0 && var3 + var6 > this.q && var3 < this.q) {
                     var12 += this.q - var3;
                     var6 = var3 + var6 - this.q;
                     var3 = this.q;
                  }

                  if ((2 & this.p) != 0 && var3 + var6 > this.q && var3 < this.q) {
                     var6 = this.q - var3;
                  }
               }

               if (this.r != -1) {
                  if ((8 & this.p) != 0 && var4 + var7 > this.r && var4 < this.r) {
                     var7 = this.r - var4;
                  }

                  if ((4 & this.p) != 0 && var4 + var7 > this.r && var4 < this.r) {
                     var12 = (this.r - var4) * var13 + (var13 - var6);
                     var7 -= this.r - var4;
                     var4 = this.r;
                  }
               }
            }

            if (var8 == null) {
               if (var6 + var3 > 0 && var7 + var4 > 0) {
                  var1.drawRGB(var11, var12, var13, var3, var4, var6, var7, this.A);
               }
            } else if ((var5 & 1) != 0) {
               if ((var5 & 2) != 0) {
                  var1.drawRegion(var8, var9, var10, var6, var7, 3, var3, var4, 0);
               } else {
                  var1.drawRegion(var8, var9, var10, var6, var7, 2, var3, var4, 0);
               }
            } else if ((var5 & 2) != 0) {
               var1.drawRegion(var8, var9, var10, var6, var7, 1, var3, var4, 0);
            } else {
               var1.drawRegion(var8, var9, var10, var6, var7, 0, var3, var4, 0);
            }
         }
      }
   }

   final int[] e(int var1, int var2) {
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
         if (this.C == 25840) {
            if (!this.t) {
               if (this.L) {
                  while (var11 < var12) {
                     int var36 = var9[var10++] & 255;
                     int var54 = var7[var36 & this.D[var8]];
                     var36 >>= this.E[var8];

                     while (var36-- >= 0) {
                        if (var54 != this.O) {
                           var6[var11++] = var54 & 65535 | this.K << 16 | this.K << 24;
                        } else {
                           var6[var11++] = var54;
                        }
                     }
                  }
               } else if (this.M) {
                  while (var11 < var12) {
                     int var34 = var9[var10++] & 255;
                     int var53 = var7[var34 & this.D[var8]];
                     var34 >>= this.E[var8];

                     while (var34-- >= 0) {
                        if (var53 != this.O) {
                           var6[var11++] = var53 & 16777215 | 0xFF | this.K << 24;
                        } else {
                           var6[var11++] = var53;
                        }
                     }
                  }
               } else {
                  while (var11 < var12) {
                     int var32 = var9[var10++] & 255;
                     int var52 = var7[var32 & this.D[var8]];
                     var32 >>= this.E[var8];

                     while (var32-- >= 0) {
                        var6[var11++] = var52;
                     }
                  }
               }
            } else {
               while (var11 < var12) {
                  int var13 = var9[var10++] & 255;
                  int var14 = var7[var13 & this.D[var8]];
                  var13 >>= this.E[var8];

                  while (var13-- >= 0) {
                     var6[var11++] = var14 & -1426063361;
                  }
               }
            }
         } else if (this.C != 10225) {
            if (this.C == 22258) {
               if (N) {
                  while (var11 < var12) {
                     int var44;
                     if ((var44 = var9[var10++] & 255) > 127) {
                        var44 -= 128;

                        while (var44-- > 0) {
                           int var61;
                           if ((var61 = var7[var9[var10++] & 0xFF]) != this.O) {
                              var6[var11++] = var61 & 65535 | this.K << 16 | this.K << 24;
                           } else {
                              var6[var11++] = var61;
                           }
                        }
                     } else {
                        int var60 = var7[var9[var10++] & 0xFF];

                        while (var44-- > 0) {
                           if (var60 != this.O) {
                              var6[var11++] = var60 & 65535 | this.K << 16 | this.K << 24;
                           } else {
                              var6[var11++] = var60;
                           }
                        }
                     }
                  }
               } else if (this.L) {
                  while (var11 < var12) {
                     int var42;
                     if ((var42 = var9[var10++] & 255) > 127) {
                        var42 -= 128;

                        while (var42-- > 0) {
                           int var59;
                           if ((var59 = var7[var9[var10++] & 0xFF]) != this.O) {
                              var6[var11++] = var59 & 16777215 | this.K | this.K << 24;
                           } else {
                              var6[var11++] = var59;
                           }
                        }
                     } else {
                        int var58 = var7[var9[var10++] & 0xFF];

                        while (var42-- > 0) {
                           if (var58 != this.O) {
                              var6[var11++] = var58 & 16777215 | this.K | this.K << 24;
                           } else {
                              var6[var11++] = var58;
                           }
                        }
                     }
                  }
               } else {
                  while (var11 < var12) {
                     int var40;
                     if ((var40 = var9[var10++] & 255) > 127) {
                        var40 -= 128;

                        while (var40-- > 0) {
                           int var57 = var7[var9[var10++] & 0xFF];
                           var6[var11++] = var57;
                        }
                     } else {
                        int var56 = var7[var9[var10++] & 0xFF];

                        while (var40-- > 0) {
                           var6[var11++] = var56;
                        }
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
               if (this.t) {
                  for (; var11 < var12; var10++) {
                     int var46;
                     if ((var46 = var7[var9[var10] >> 6 & 3]) == this.O) {
                        var6[var11++] = var46;
                     } else {
                        var6[var11++] = var46 & 16777215 | this.K << 24;
                     }

                     if ((var46 = var7[var9[var10] >> 4 & 3]) == this.O) {
                        var6[var11++] = var46;
                     } else {
                        var6[var11++] = var46 & 16777215 | this.K << 24;
                     }

                     if ((var46 = var7[var9[var10] >> 2 & 3]) == this.O) {
                        var6[var11++] = var46;
                     } else {
                        var6[var11++] = var46 & 16777215 | this.K << 24;
                     }

                     if ((var46 = var7[var9[var10] & 3]) == this.O) {
                        var6[var11++] = var46;
                     } else {
                        var6[var11++] = var46 & 16777215 | this.K << 24;
                     }
                  }
               } else {
                  while (var11 < var12) {
                     var6[var11++] = var7[var9[var10] >> 6 & 3];
                     var6[var11++] = var7[var9[var10] >> 4 & 3];
                     var6[var11++] = var7[var9[var10] >> 2 & 3];
                     var6[var11++] = var7[var9[var10] & 3];
                     var10++;
                  }
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
               int var38;
               if ((var38 = var9[var10++] & 255) > 127) {
                  int var55 = var9[var10++] & 255;
                  int var15 = var7[var55];
                  var38 -= 128;

                  while (var38-- > 0) {
                     var6[var11++] = var15;
                  }
               } else {
                  var6[var11++] = var7[var38];
               }
            }
         }

         if ((var3 & 1) != 0) {
            int var62 = 0;

            for (int var64 = var4 - 1; var62 < var64; var64--) {
               for (int var16 = 0; var16 < var12; var16 += var4) {
                  int var50 = var6[var16 + var62];
                  var6[var16 + var62] = var6[var16 + var64];
                  var6[var16 + var64] = var50;
               }

               var62++;
            }
         }

         if ((var3 & 2) != 0) {
            int var63 = 0;

            for (int var65 = (var5 - 1) * var4; var63 < var65; var65 -= var4) {
               for (int var66 = 0; var66 < var4; var66++) {
                  int var51 = var6[var63 + var66];
                  var6[var63 + var66] = var6[var65 + var66];
                  var6[var65 + var66] = var51;
               }

               var63 += var4;
            }
         }

         return var6;
      } else {
         return null;
      }
   }

   static final int b(int var0, int var1, int var2) {
      return (var0 * var1 + (var2 >> 1)) / var2;
   }

   public static final Image a(Image var0, int var1, int var2) {
      if ((var1 | var2) < 0) {
         return Image.createImage(0, 0);
      }

      int var3 = var0.getWidth();
      int var4 = var0.getHeight();
      int[] var5 = new int[var3 * var4];
      int[] var6 = new int[var1 * var2];
      var0.getRGB(var5, 0, var3, 0, 0, var3, var4);
      int var7 = 0;
      int var8 = 0;

      for (int var9 = 0; var9 < var1; var9++) {
         for (int var10 = 0; var10 < var2; var10++) {
            if ((var7 = b(var10, var4, var2)) >= var4) {
               var7 = var4 - 1;
            }

            if ((var8 = b(var9, var3, var1)) >= var3) {
               var8 = var3 - 1;
            }

            var6[var10 * var1 + var9] = var5[var7 * var3 + var8];
         }
      }

      return Image.createRGBImage(var6, var1, var2, true);
   }

   public final void f(int var1, int var2) {
      this.Q = var1;
      this.R = var2;
      this.P = true;
   }

   public final void c() {
      this.P = false;
   }

   private static int a(char var0) {
      int var1;
      if (var0 >= 'A' && var0 <= 'Z') {
         var1 = var0 - 'A' + 31;
      } else if (var0 >= 'a' && var0 <= 'z') {
         var1 = var0 - 'a' + 31;
      } else if (var0 >= '0' && var0 <= '9') {
         var1 = var0 - '0' + 14;
      } else {
         var1 = " !\"#$%&'*+,_./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ()-ÄÖÜß¡¿ÁÀÃÈÉÊÎÏÍÓÔÕÚÙÑÇÒ©ĞÂÌŞİ".indexOf(var0);
      }

      if (var1 == -1) {
         var1 = " !\"#$%&'*+,_./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ()-ÄÖÜß¡¿ÁÀÃÈÉÊÎÏÍÓÔÕÚÙÑÇÒ©ĞÂÌŞİ".indexOf(var0 - ' ');
      }

      if (var1 == -1) {
         var1 = " !\"#$%&'*+,_./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ()-ÄÖÜß¡¿ÁÀÃÈÉÊÎÏÍÓÔÕÚÙÑÇÒ©ĞÂÌŞİ".indexOf(var0 - 1);
      }

      return var1;
   }

   final int a(String var1) {
      S = 0;
      T = this.g[1] & 255;
      int var2 = 0;
      int var3 = U >= 0 ? U : 0;
      int var4 = V >= 0 ? V : var1.length();

      for (int var5 = var3; var5 < var4; var5++) {
         char var6;
         if ((var6 = var1.charAt(var5)) != '{' && var6 != '}') {
            if (var6 == '~') {
               var5++;
               var2 += this.f[0] * 2;
            } else {
               int var7;
               if ((var7 = a(var6)) == -1) {
                  var2 += this.f[0] & 255;
               } else {
                  int var8 = this.l[var7 << 2] & 255;
                  var2 += (this.f[var8] & 255) + this.l[1];
               }
            }
         }
      }

      if (var2 > S) {
         S = var2;
      }

      return var2;
   }

   final void a(Graphics var1, String var2, int var3, int var4, int var5) {
      if (var4 >= -20 && var4 <= 320) {
         if ((var5 & 43) != 0) {
            this.a(var2);
            if ((var5 & 8) != 0) {
               var3 -= S;
            } else if ((var5 & 1) != 0) {
               var3 -= S >> 1;
            }

            if ((var5 & 32) != 0) {
               var4 -= T;
            } else if ((var5 & 2) != 0) {
               var4 -= T >> 1;
            }
         }

         int var6 = var3;
         int var7 = var4;
         int var8 = U >= 0 ? U : 0;
         int var9 = V >= 0 ? V : var2.length();

         for (int var10 = var8; var10 < var9; var10++) {
            char var11;
            if ((var11 = var2.charAt(var10)) == '{') {
               if (e.F == 8 || e.F == 6 || e.F == 31) {
                  this.z = 2;
               }
            } else if (var11 == '}') {
               this.z = Z;
            } else if (var11 == '~') {
               var10++;
               int var12 = var2.charAt(var10) - '0';
               e.b[31].a(var1, var12, e.I % X[var12], var6 + Y[var12], var7 + (this.g[1] & 255) - W[(var12 << 1) + 1], 0, 0, 0);
               var6 += this.f[0] + W[var12 << 1] + Y[var12] / 2;
            } else if (var11 == '$') {
               int var14;
               if ((var14 = var2.charAt(++var10) - '0') == 1) {
                  e.b[16].a(var1, 35, e.I % 3, var6, var7 - 7, 0, 0, 0);
                  var6 += 40;
               }

               if (var14 == 2) {
                  e.b[16].a(var1, 36, e.I % 3, var6 - 30, var7 - 7, 0, 0, 0);
                  var6 += 10;
               }

               if (var14 == 3) {
                  e.b[16].a(var1, 41, e.b(e.aX, 2), var6 - 30, var7 - 7, 0, 0, 0);
                  var7 += 10;
                  var6 += 5;
                  e.aX++;
               }
            } else {
               int var15;
               if ((var15 = a(var11)) == -1) {
                  var6 += this.f[0] & 255;
               } else {
                  this.b(var1, 0, var15, var6, var7, 0, 0, 0);
                  int var13 = this.l[var15 << 2] & 255;
                  var6 += this.f[var13] & 255;
               }
            }
         }
      }
   }

   public final void a(String var1, int var2) {
      if (ac == null || !ac.equals(var1)) {
         int var3 = var2;
         ac = var1;
         ab = 0;
         int var4 = 0;
         int var5 = 0;
         int var6 = 0;
         int var7 = var1.length();

         for (int var8 = 0; var8 <= var7; var8++) {
            char var9 = ' ';
            if (var8 < var7) {
               var9 = var1.charAt(var8);
            }

            if (var9 == '^' && var6 <= var3) {
               aa[var4++] = var8;
               var6 = 0;
               var5 = 0;
            } else if (var3 > 0 && (var9 == '^' || var9 == ' ' || var8 == var7 - 1) && var6 > var3) {
               do {
                  var8--;
               } while (var1.charAt(var8) != ' ' && var8 > (var4 > 0 ? aa[var4 - 1] : 0));

               if (var8 == (var4 > 0 ? aa[var4 - 1] : 0)) {
                  var8 = (var4 > 0 ? aa[var4 - 1] : 0) + var5;
               }

               aa[var4++] = var8;
               ab = var4;
               var5 = 0;
               var6 = 0;
            } else {
               var5++;
               if (var9 != '{' && var9 != '}') {
                  if (var9 == '~') {
                     var8++;
                     var6 += this.f[0] * 2;
                  } else {
                     int var10;
                     if ((var10 = a(var9)) == -1) {
                        var6 += this.f[0] & 255;
                     } else {
                        int var11 = this.l[var10 << 2] & 255;
                        var6 += this.f[var11] & 255;
                     }
                  }
               }
            }
         }

         aa[var4++] = var7;
         ab = var4;
         e.bI = ab;
      }
   }

   final int a(Graphics var1, String var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      var2 = '}' + var2;
      this.a(var2, var6);
      int var9 = this.ad + (this.g[1] & 255);
      if (e.F == 20) {
         var9 += 3;
      }

      if ((var5 & 32) != 0) {
         var4 -= var9 * (ab - 1);
      } else if ((var5 & 2) != 0) {
         var4 -= var9 * (ab - 1) >> 1;
      }

      Z = this.z;
      if (var7 > 0) {
         for (int var10 = 0; var10 < aa[var7 - 1]; var10++) {
            char var11;
            if ((var11 = var2.charAt(var10)) == '{') {
               this.z = 2;
            } else if (var11 == '}') {
               this.z = Z;
            }
         }
      }

      for (int var15 = var7; var15 < ab; var15++) {
         U = var15 > 0 ? aa[var15 - 1] + 1 : 0;
         V = aa[var15];
         int var16 = var4 + (var15 - var7) * var9;
         if (e.F == 20) {
            if (var16 < 100 && var16 >= 68) {
               e.a.t = true;
               int var17;
               int var18 = ((var17 = var16 - 68) << 8) / 32;
               e.a.K = 255 * var18 >> 8;
            } else if (var16 < 244 && var16 >= 210) {
               e.a.t = true;
               int var12;
               int var13 = ((var12 = var16 - 210) << 8) / 34;
               e.a.K = 65280 - 255 * var13 >> 8;
            }
         }

         this.a(var1, var2, var3, var16, var5);
         if (e.F == 20) {
            e.a.t = false;
         }

         if (var8 != -1 && var15 - var7 >= var8 - 1) {
            U = -1;
            V = -1;
            return var4 + var15 * var9;
         }
      }

      U = -1;
      V = -1;
      return var4 + ab * var9;
   }

   static final void a(d var0, int var1, int var2) {
      var0.A = true;
      var2 &= 16777215;

      for (int var3 = 0; var3 < var0.v[0][var1].length; var3++) {
         if ((var0.v[0][var1][var3] & 16777215) != 16711935) {
            var0.v[0][var1][var3] = var0.v[0][var1][var3] << 24 | var2;
         }
      }
   }

   static final void b(d var0, int var1, int var2) {
      var0.A = true;

      for (int var3 = 0; var3 < var0.v[0][var1].length; var3++) {
         if ((var0.v[0][var1][var3] & 16777215) != 16711935) {
            var0.v[0][var1][var3] = var0.v[0][var1][var3] & (var2 << 24 | 16777215);
         }
      }
   }

   static {
      " !\"#$%&'*+,_./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ()-ÄÖÜß¡¿ÁÀÃÈÉÊÎÏÍÓÔÕÚÙÑÇÒ©ĞÂÌŞİ".length();
   }
}
