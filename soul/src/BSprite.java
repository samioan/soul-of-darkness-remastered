import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

// $VF: renamed from: d
final class BSprite {
   // $VF: renamed from: a int[]
   static int[] scratchPixels = new int[12288];
   // $VF: renamed from: b int
   int moduleCount;
   // $VF: renamed from: c byte[]
   byte[] moduleX;
   // $VF: renamed from: d byte[]
   byte[] moduleY;
   // $VF: renamed from: e byte[]
   byte[] moduleImageIndex;
   // $VF: renamed from: f byte[]
   byte[] moduleWidth;
   // $VF: renamed from: g byte[]
   byte[] moduleHeight;
   // $VF: renamed from: h byte[]
   byte[] frameModuleCount;
   // $VF: renamed from: i short[]
   short[] frameModuleStart;
   // $VF: renamed from: j short[]
   short[] frameCollisionStart;
   // $VF: renamed from: k byte[]
   byte[] collisionRects;
   // $VF: renamed from: l byte[]
   byte[] frameRects;
   // $VF: renamed from: m short[]
   short[] frameModules;
   // $VF: renamed from: n byte[]
   byte[] animationFrameCount;
   // $VF: renamed from: o short[]
   short[] animationFrameStart;
   // $VF: renamed from: p byte[]
   byte[] animationFrames;
   // $VF: renamed from: q int
   int boundaryClipMask = -1;
   // $VF: renamed from: r int
   int boundaryClipX = -1;
   // $VF: renamed from: s int
   int boundaryClipY = -1;
   // $VF: renamed from: t boolean
   boolean moduleMaskEnabled = false;
   // $VF: renamed from: u int[]
   int[] frameModuleMask;
   // $VF: renamed from: v int[][][]
   int[][][] palettes;
   // $VF: renamed from: w byte[]
   byte[] pngPaletteChunks;
   // $VF: renamed from: x int
   int paletteCount;
   // $VF: renamed from: y int
   int pngColorCount;
   // $VF: renamed from: z int
   public int currentPalette;
   // $VF: renamed from: A boolean
   boolean hasAlpha;
   // $VF: renamed from: B int
   int featureFlags;
   // $VF: renamed from: C short
   short pixelCodec;
   // $VF: renamed from: D int[]
   int[] packedColorMask;
   // $VF: renamed from: E int[]
   int[] packedColorBits;
   // $VF: renamed from: F byte[]
   byte[] imageData;
   // $VF: renamed from: G int[]
   int[] moduleDataOffset;
   // $VF: renamed from: H javax.microedition.lcdui.Image[][]
   Image[][] cachedImages;
   // $VF: renamed from: I d[]
   static BSprite[] registeredCacheSprites = new BSprite[23];
   // $VF: renamed from: J javax.microedition.lcdui.Image[][]
   static Image[][] imageCaches = new Image[4][];
   // $VF: renamed from: K short[][]
   static short[][] cacheEvictionSlots = new short[4][];
   // $VF: renamed from: L int[]
   static int[] cacheImageCounts = new int[4];
   // $VF: renamed from: M int[]
   static int[] cacheNextEviction = new int[4];
   // $VF: renamed from: N int[]
   static int[] cacheCapacity = new int[4];
   // $VF: renamed from: O int
   int cacheBase;
   // $VF: renamed from: P int
   int cacheIndex = -1;
   // $VF: renamed from: Q int
   static int textWidth;
   // $VF: renamed from: R int
   static int textHeight;
   // $VF: renamed from: ac int
   private int lineSpacing = 5;
   // $VF: renamed from: S int
   static int textStart = -1;
   // $VF: renamed from: T int
   static int textEnd = -1;
   // $VF: renamed from: U int[]
   public static final int[] inlineIconMetrics = new int[]{3, 3, 6, 16, 3, 3, 6, 16, 3, 2};
   // $VF: renamed from: V int[]
   public static final int[] inlineIconFrameCounts = new int[]{5, 1, 16, 1, 6};
   // $VF: renamed from: W int[]
   public static final int[] inlineIconXOffsets = new int[]{0, 5, 0, 8, 0};
   // $VF: renamed from: X int[]
   static int[] wrappedLineEnds = new int[200];
   // $VF: renamed from: Y int
   static int wrappedLineCount;
   // $VF: renamed from: Z java.lang.String
   static String wrappedText;
   // $VF: renamed from: aa int
   int textBasePalette;
   // $VF: renamed from: ab int
   int textLastPalette;

   // $VF: renamed from: a (byte[], int) void
   final void load(byte[] var1, int var2) {
      try {
         System.gc();
         if ((short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8)) != 991) {
            System.out.println("ERROR: Invalid BSprite version !");
         }

         this.featureFlags = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8) + ((var1[var2++] & 255) << 16) + ((var1[var2++] & 255) << 24);
         int var4 = 0;
         if ((this.featureFlags & 1) != 0) {
            this.moduleCount = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8);
            if (this.moduleCount > 0) {
               if ((this.featureFlags & 4) != 0) {
                  this.moduleImageIndex = new byte[this.moduleCount];
               }

               if ((this.featureFlags & 2) != 0 || (this.featureFlags & 4) != 0) {
                  this.moduleX = new byte[this.moduleCount];
                  this.moduleY = new byte[this.moduleCount];
               }

               this.moduleWidth = new byte[this.moduleCount];
               this.moduleHeight = new byte[this.moduleCount];

               for (int var5 = 0; var5 < this.moduleCount; var5++) {
                  if ((this.featureFlags & 4) != 0) {
                     this.moduleImageIndex[var5] = var1[var2++];
                     if (this.moduleImageIndex[var5] > var4) {
                        var4 = this.moduleImageIndex[var5];
                     }
                  }

                  if ((this.featureFlags & 2) != 0) {
                     this.moduleX[var5] = var1[var2++];
                     this.moduleY[var5] = var1[var2++];
                  }

                  this.moduleWidth[var5] = var1[var2++];
                  this.moduleHeight[var5] = var1[var2++];
               }
            }

            var4++;
         }

         if ((this.featureFlags & 256) != 0) {
            int var63;
            if ((var63 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8)) > 0) {
               this.frameModules = new short[var63 << 2];

               for (int var6 = 0; var6 < var63; var6++) {
                  this.frameModules[var6 * 4] = var1[var2++];
                  if ((this.featureFlags & 1024) != 0) {
                     this.frameModules[var6 * 4 + 1] = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8));
                     this.frameModules[var6 * 4 + 2] = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8));
                  } else {
                     this.frameModules[var6 * 4 + 1] = var1[var2++];
                     this.frameModules[var6 * 4 + 2] = var1[var2++];
                  }

                  this.frameModules[var6 * 4 + 3] = var1[var2++];
               }
            }

            int var68;
            if ((var68 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8)) > 0) {
               this.collisionRects = new byte[var68 << 2];
               System.arraycopy(var1, var2, this.collisionRects, 0, this.collisionRects.length);
               var2 += this.collisionRects.length;
            }

            if ((var68 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8)) > 0) {
               this.frameModuleCount = new byte[var68];
               this.frameModuleStart = new short[var68];
               short var7 = 0;
               this.frameCollisionStart = new short[var68 + 1];
               this.frameCollisionStart[0] = 0;

               for (int var8 = 0; var8 < var68; var8++) {
                  this.frameModuleCount[var8] = var1[var2++];
                  int var168 = ++var2;
                  var2++;
                  this.frameModuleStart[var8] = (short)((var1[var168] & 255) + ((var1[var2++] & 255) << 8));
                  var7 = (short)(var7 + var1[var2++]);
                  this.frameCollisionStart[var8 + 1] = var7;
               }

               this.frameRects = new byte[var68 << 2];
               System.arraycopy(var1, var2, this.frameRects, 0, this.frameRects.length);
               var2 += this.frameRects.length;
            }
         }

         if ((this.featureFlags & 65536) != 0) {
            int var64;
            if ((var64 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8)) > 0) {
               this.animationFrames = new byte[var64 * 5];
               System.arraycopy(var1, var2, this.animationFrames, 0, this.animationFrames.length);
               var2 += this.animationFrames.length;
            }

            int var70;
            if ((var70 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8)) > 0) {
               this.animationFrameCount = new byte[var70];
               this.animationFrameStart = new short[var70];

               for (int var74 = 0; var74 < var70; var74++) {
                  this.animationFrameCount[var74] = var1[var2++];
                  int var170 = ++var2;
                  var2++;
                  this.animationFrameStart[var74] = (short)((var1[var170] & 255) + ((var1[var2++] & 255) << 8));
               }
            }
         }

         if ((this.featureFlags & 16777216) != 0) {
            short var65 = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8));
            this.palettes = new int[var4][this.paletteCount][];
            int[] var71 = new int[var4];

            for (int var75 = 0; var75 < var4; var75++) {
               int var79 = var1[var2++] & 255;
               this.palettes[var75] = new int[var79][];
               if (this.paletteCount < var79) {
                  this.paletteCount = var79;
               }

               int var9;
               if ((var9 = var1[var2++] & 255) == 0) {
                  var9 = 256;
               }

               var71[var75] = var9;

               for (int var10 = 0; var10 < var79; var10++) {
                  this.palettes[var75][var10] = new int[var9];
                  if (var65 == 21781) {
                     for (int var11 = 0; var11 < var9; var11++) {
                        int var12 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8);
                        int var13 = -16777216;
                        if ((var12 & 32768) != 32768) {
                           var13 = 0;
                           this.hasAlpha = true;
                        }

                        this.palettes[var75][var10][var11] = var13 | (var12 & 31744) << 9 | (var12 & 992) << 6 | (var12 & 31) << 3;
                     }
                  }
               }
            }

            this.pixelCodec = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8));
            if (this.pixelCodec == 25840) {
               this.packedColorMask = new int[var4];
               this.packedColorBits = new int[var4];

               for (int var76 = 0; var76 < var4; var76++) {
                  int var80 = var71[var76] - 1;
                  this.packedColorMask[var76] = 1;

                  for (this.packedColorBits[var76] = 0; var80 != 0; this.packedColorBits[var76]++) {
                     var80 >>= 1;
                     this.packedColorMask[var76] = this.packedColorMask[var76] << 1;
                  }

                  this.packedColorMask[var76]--;
               }
            }

            if (this.moduleCount > 0) {
               this.moduleDataOffset = new int[this.moduleCount];
               int var77 = 0;
               int var81 = var2;

               for (int var85 = 0; var85 < this.moduleCount; var85++) {
                  int var88 = (var1[var81++] & 255) + ((var1[var81++] & 255) << 8);
                  this.moduleDataOffset[var85] = var77;
                  var81 += var88;
                  var77 += var88;
               }

               this.imageData = new byte[var77];

               for (int var86 = 0; var86 < this.moduleCount; var86++) {
                  int var89 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8);
                  System.arraycopy(var1, var2, this.imageData, this.moduleDataOffset[var86], var89);
                  var2 += var89;
               }
            }
         }

         if ((this.featureFlags & 536870912) != 0 && (this.featureFlags & 4) == 0) {
            int var66 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8);
            this.imageData = new byte[var66];
            System.arraycopy(var1, var2, this.imageData, 0, var66);
            var2 += var66;
            this.paletteCount = var1[var2++] & 255;
            this.pngColorCount = var1[var2++] & 255;
            if (this.pngColorCount == 0) {
               this.pngColorCount = 256;
            }

            int var72 = this.pngColorCount * 3 + 4 + this.pngColorCount + 4;
            this.pngPaletteChunks = new byte[this.paletteCount * var72];
            System.arraycopy(var1, var2, this.pngPaletteChunks, 0, this.paletteCount * var72);
            var2 += this.paletteCount * var72;
         }

         if ((this.featureFlags & 4) != 0 && (this.featureFlags & 16777216) == 0) {
            int var67 = var1[var2++] & 255;
            if (this.cachedImages == null) {
               this.cachedImages = new Image[var67][];
            }

            for (int var73 = 0; var73 < var67; var73++) {
               int var78;
               byte[] var84 = new byte[var78 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8)];
               System.arraycopy(var1, var2, var84, 0, var78);
               var2 += var78;
               int var87;
               if ((var87 = var1[var2++] & 255) == 0) {
                  this.cachedImages[var73] = new Image[1];
                  this.cachedImages[var73][0] = Image.createImage(var84, 0, var84.length);
               } else {
                  if (this.cachedImages[var73] == null) {
                     this.cachedImages[var73] = new Image[var87];
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
                     this.cachedImages[var73][var91] = Image.createImage(var84, 0, var84.length);
                  }
               }
            }
         }

         System.gc();
      } catch (Exception var15) {
         ErrorReporter.report(var15, "ASprite.Load()");
      }
   }

   // $VF: renamed from: a () void
   final void releaseImageData() {
      this.imageData = null;
      this.moduleDataOffset = null;
      if ((this.featureFlags & 536870912) != 0) {
         this.pngPaletteChunks = null;
      }

      System.gc();
   }

   // $VF: renamed from: a (int, int, int, int) void
   final void buildImages(int var1, int var2, int var3, int var4) {
      this.buildImagesInternal(null, var1, var2, var3, var4);
   }

   // $VF: renamed from: a (d, int, int, int, int) void
   final void buildImagesInternal(BSprite var1, int var2, int var3, int var4, int var5) {
      if (var4 == -1) {
         var4 = this.moduleCount - 1;
      }

      if (this.cachedImages == null) {
         this.cachedImages = new Image[this.paletteCount][];
      }

      if ((this.featureFlags & 16777216) != 0) {
         if (this.cachedImages[var2] == null) {
            this.cachedImages[var2] = new Image[this.moduleCount];
         }

         if (var5 >= 0) {
            for (int var6 = var3; var6 <= var4; var6++) {
               this.cachedImages[var2][var6] = this.cachedImages[var5][var6];
            }
         } else {
            int var19 = this.currentPalette;
            this.currentPalette = var2;
            boolean var7 = false;
            boolean var8 = false;
            System.gc();
            Runtime.getRuntime().freeMemory();
            Object var11 = null;

            for (int var12 = var3; var12 <= var4; var12++) {
               int var13 = this.moduleWidth[var12] & 255;
               int var14 = this.moduleHeight[var12] & 255;
               int[] var15;
               if (var13 > 0 && var14 > 0 && (var15 = this.decodeModule(var12, 0)) != null) {
                  boolean var16 = false;
                  int var17 = var13 * var14;

                  for (int var18 = 0; var18 < var17; var18++) {
                     if ((var15[var18] & 0xFF000000) != -16777216) {
                        var16 = true;
                        break;
                     }
                  }

                  this.cachedImages[var2][var12] = Image.createRGBImage(var15, var13, var14, var16);
               }
            }

            if (var1 != null) {
               var1.releaseImageData();
            }

            System.gc();
            this.currentPalette = var19;
         }
      }

      if ((this.featureFlags & 536870912) != 0) {
         if (this.cachedImages[var2] == null) {
            this.cachedImages[var2] = new Image[1];
         }

         int var20 = this.pngColorCount * 3 + 4;
         int var21 = this.pngColorCount + 4;
         System.arraycopy(this.pngPaletteChunks, var2 * (var20 + var21), this.imageData, 41, var20);
         System.arraycopy(this.pngPaletteChunks, var2 * (var20 + var21) + var20, this.imageData, 41 + var20 + 8, var21);
         this.cachedImages[var2][0] = Image.createImage(this.imageData, 0, this.imageData.length);
      }

      System.gc();
   }

   // $VF: renamed from: a (int, int) int
   final int getAnimationFrameDuration(int var1, int var2) {
      return this.animationFrames[(this.animationFrameStart[var1] + var2) * 5 + 1] & 0xFF;
   }

   // $VF: renamed from: a (int) int
   final int getAnimationFrameCount(int var1) {
      return this.animationFrameCount[var1] & 0xFF;
   }

   // $VF: renamed from: b (int) int
   final int getFrameWidth(int var1) {
      int var2 = var1 << 2;
      return this.frameRects[var2 + 2];
   }

   // $VF: renamed from: b (int, int) int
   final int getFrameModuleX(int var1, int var2) {
      int var3 = this.frameModuleStart[var1] + var2 << 2;
      return this.frameModules[var3 + 1];
   }

   // $VF: renamed from: c (int, int) int
   final int getFrameModuleY(int var1, int var2) {
      int var3 = this.frameModuleStart[var1] + var2 << 2;
      return this.frameModules[var3 + 2];
   }

   // $VF: renamed from: d (int, int) int
   final int getFrameModuleWidth(int var1, int var2) {
      int var3 = this.frameModuleStart[var1] + var2 << 2;
      int var4 = this.frameModules[var3] & 255 | (this.frameModules[var3 + 3] & 255 & 192) << 2;
      return this.moduleWidth[var4] & 0xFF;
   }

   // $VF: renamed from: e (int, int) int
   final int getFrameModuleHeight(int var1, int var2) {
      int var3 = this.frameModuleStart[var1] + var2 << 2;
      int var4 = this.frameModules[var3] & 255 | (this.frameModules[var3 + 3] & 255 & 192) << 2;
      return this.moduleHeight[var4] & 0xFF;
   }

   // $VF: renamed from: a (int[], int, int, int, int, int, int) void
   final void getAnimationBounds(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = (this.animationFrameStart[var2] + var3) * 5;
      int var9 = this.animationFrames[var8] & 255 | (this.animationFrames[var8 + 4] & 192) << 2;
      if ((var5 & 1) != 0) {
         var6 += this.animationFrames[var8 + 2];
      } else {
         var6 -= this.animationFrames[var8 + 2];
      }

      if ((var5 & 2) != 0) {
         var7 += this.animationFrames[var8 + 3];
      } else {
         var7 -= this.animationFrames[var8 + 3];
      }

      this.getFrameBounds(var1, var9, var4, var5 ^ this.animationFrames[var8 + 4] & 15, var6, var7);
   }

   // $VF: renamed from: a (int[], int, int, int, int, int) void
   final void getFrameBounds(int[] var1, int var2, int var3, int var4, int var5, int var6) {
      if (var3 != -1) {
         var3 += this.frameCollisionStart[var2];
         if (this.frameCollisionStart[var2 + 1] <= var3) {
            var1[0] = var1[1] = var1[2] = var1[3] = 0;
         } else {
            int var24 = var3 << 2;
            byte var29 = this.collisionRects[var24++];
            byte var31 = this.collisionRects[var24++];
            int var33 = this.collisionRects[var24++] & 255;
            int var35 = this.collisionRects[var24] & 255;
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
      } else if ((this.featureFlags & 1024) != 0) {
         int var23 = 0;
         int var28 = 0;
         int var30 = 0;
         int var32 = 0;
         int var34 = this.frameModuleCount[var2] & 255;

         for (int var12 = 0; var12 < var34; var12++) {
            this.getFrameModuleBounds(var1, var2, var12);
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
         byte var8 = this.frameRects[var7++];
         byte var9 = this.frameRects[var7++];
         int var10 = this.frameRects[var7++] & 255;
         int var11 = this.frameRects[var7] & 255;
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

   // $VF: renamed from: a (int[], int, int) void
   final void getFrameModuleBounds(int[] var1, int var2, int var3) {
      int var4 = this.frameModuleStart[var2] + var3 << 2;
      int var5 = this.frameModules[var4 + 3] & 255;
      int var6 = this.frameModules[var4] & 255 | (var5 & 192) << 2;
      if ((var5 & 16) != 0) {
         this.getFrameBounds(var1, var6, -1, 0, 0, 0);
      } else {
         var1[0] = this.getFrameModuleX(var2, var3);
         var1[1] = this.getFrameModuleY(var2, var3);
         var1[2] = var1[0] + this.getFrameModuleWidth(var2, var3);
         var1[3] = var1[1] + this.getFrameModuleHeight(var2, var3);
      }
   }

   // $VF: renamed from: b (int[], int, int) void
   final void getModuleBounds(int[] var1, int var2, int var3) {
      var1[0] = 0;
      var1[1] = 0;
      var1[2] = this.moduleWidth[var2] & 255;
      var1[3] = this.moduleHeight[var2] & 255;
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, int, int, int, int, int, int, int) void
   final void drawAnimationFrame(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      int var9 = (this.animationFrameStart[var2] + var3) * 5;
      int var10 = this.animationFrames[var9] & 255 | (this.animationFrames[var9 + 4] & 192) << 2;
      if ((var6 & 1) != 0) {
         var7 += this.animationFrames[var9 + 2];
      } else {
         var7 -= this.animationFrames[var9 + 2];
      }

      if ((var6 & 2) != 0) {
         var8 += this.animationFrames[var9 + 3];
      } else {
         var8 -= this.animationFrames[var9 + 3];
      }

      this.drawFrame(var1, var10, var4 - var7, var5 - var8, var6 ^ this.animationFrames[var9 + 4] & 15, var7, var8);
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, int, int, int, int, int, int) void
   final void drawFrame(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = this.frameModuleCount[var2] & 255;

      for (int var9 = 0; var9 < var8; var9++) {
         if (!this.moduleMaskEnabled || this.frameModuleMask[var9] != 1) {
            this.drawFrameModule(var1, var2, var9, var3, var4, var5, var6, var7);
         }
      }
   }

   // $VF: renamed from: b (javax.microedition.lcdui.Graphics, int, int, int, int, int, int, int) void
   final void drawFrameModule(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      int var9 = this.frameModuleStart[var2] + var3 << 2;
      int var10 = this.frameModules[var9 + 3] & 255;
      int var11 = this.frameModules[var9] & 255 | (var10 & 192) << 2;
      if ((var6 & 1) != 0) {
         var4 -= this.frameModules[var9 + 1];
      } else {
         var4 += this.frameModules[var9 + 1];
      }

      if ((var6 & 2) != 0) {
         var5 -= this.frameModules[var9 + 2];
      } else {
         var5 += this.frameModules[var9 + 2];
      }

      if ((var10 & 16) != 0) {
         this.drawFrame(var1, var11, var4, var5, var6 ^ var10 & 15, var7, var8);
      } else {
         if ((var6 & 1) != 0) {
            var4 -= this.moduleWidth[var11] & 255;
         }

         if ((var6 & 2) != 0) {
            var5 -= this.moduleHeight[var11] & 255;
         }

         this.drawModule(var1, var11, var4, var5, var6 ^ var10 & 15);
      }
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, int, int, int, int) void
   final void drawModule(Graphics var1, int var2, int var3, int var4, int var5) {
      int var6 = this.moduleWidth[var2] & 255;
      int var7 = this.moduleHeight[var2] & 255;
      if (var6 > 0 && var7 > 0) {
         Image var8 = null;
         if (this.cachedImages != null && this.cachedImages[this.currentPalette] != null) {
            if ((this.featureFlags & 4) != 0) {
               if (this.currentPalette >= this.cachedImages[this.moduleImageIndex[var2]].length) {
                  this.currentPalette = 0;
               }

               var8 = this.cachedImages[this.moduleImageIndex[var2]][this.currentPalette];
            } else if ((this.featureFlags & 536870912) != 0) {
               var8 = this.cachedImages[this.currentPalette][0];
            } else {
               var8 = this.cachedImages[this.currentPalette][var2];
            }
         } else if (this.cacheIndex >= 0) {
            int var9 = var2;
            int var14;
            var2 = (var14 = var2 + this.cacheBase) + this.currentPalette * this.moduleCount;
            if (imageCaches[this.cacheIndex][var2] == null) {
               int var10 = cacheNextEviction[this.cacheIndex];
               short var11;
               if ((var11 = cacheEvictionSlots[this.cacheIndex][var10]) >= 0) {
                  imageCaches[this.cacheIndex][var11] = null;
               }

               cacheEvictionSlots[this.cacheIndex][var10] = (short)var2;
               cacheNextEviction[this.cacheIndex] = (cacheNextEviction[this.cacheIndex] + 1) % cacheCapacity[this.cacheIndex];
               int[] var12;
               if ((var12 = this.decodeModule(var9, var5)) == null) {
                  System.out.println("DecodeImage() FAILED !");
                  return;
               }

               imageCaches[this.cacheIndex][var2] = Image.createRGBImage(var12, var6, var7, this.hasAlpha);
            }

            var8 = imageCaches[this.cacheIndex][var2];
         }

         if (this.boundaryClipMask == -1
            || ((1 & this.boundaryClipMask) == 0 || var3 + var6 > this.boundaryClipX)
               && ((2 & this.boundaryClipMask) == 0 || var3 < this.boundaryClipX)
               && ((4 & this.boundaryClipMask) == 0 || var4 + var7 > this.boundaryClipY)
               && ((8 & this.boundaryClipMask) == 0 || var4 < this.boundaryClipY)) {
            int var15 = 0;
            int var16 = 0;
            int[] var17 = null;
            int var18 = 0;
            int var13 = var6;
            if (var8 == null) {
               if ((var17 = this.decodeModule(var2, var5)) == null) {
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
               if ((this.featureFlags & 536870912) != 0) {
                  var15 = this.moduleX[var2] & 255;
                  var16 = this.moduleY[var2] & 255;
                  var6 = this.moduleWidth[var2] & 255;
                  var7 = this.moduleHeight[var2] & 255;
               }
            }

            if (this.boundaryClipMask != -1) {
               if (this.boundaryClipX != -1) {
                  if ((1 & this.boundaryClipMask) != 0 && var3 + var6 > this.boundaryClipX && var3 < this.boundaryClipX) {
                     var18 += this.boundaryClipX - var3;
                     var6 = var3 + var6 - this.boundaryClipX;
                     var3 = this.boundaryClipX;
                  }

                  if ((2 & this.boundaryClipMask) != 0 && var3 + var6 > this.boundaryClipX && var3 < this.boundaryClipX) {
                     var6 = this.boundaryClipX - var3;
                  }
               }

               if (this.boundaryClipY != -1) {
                  if ((8 & this.boundaryClipMask) != 0 && var4 + var7 > this.boundaryClipY && var4 < this.boundaryClipY) {
                     var7 = this.boundaryClipY - var4;
                  }

                  if ((4 & this.boundaryClipMask) != 0 && var4 + var7 > this.boundaryClipY && var4 < this.boundaryClipY) {
                     var18 = (this.boundaryClipY - var4) * var13 + (var13 - var6);
                     var7 -= this.boundaryClipY - var4;
                     var4 = this.boundaryClipY;
                  }
               }
            }

            if (var8 == null) {
               if (var6 + var3 > 0 && var7 + var4 > 0) {
                  var1.drawRGB(var17, var18, var13, var3, var4, var6, var7, this.hasAlpha);
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

   // $VF: renamed from: f (int, int) int[]
   final int[] decodeModule(int var1, int var2) {
      return this.decodeModuleInto(scratchPixels, var1, var2);
   }

   // $VF: renamed from: c (int[], int, int) int[]
   final int[] decodeModuleInto(int[] var1, int var2, int var3) {
      if (this.imageData != null && this.moduleDataOffset != null) {
         int var4 = this.moduleWidth[var2] & 255;
         int var5 = this.moduleHeight[var2] & 255;
         if (var4 * var5 > scratchPixels.length) {
            System.out.println("ERROR: sizeX x sizeY > temp.length (" + var4 + " x " + var5 + " = " + var4 * var5 + " > " + scratchPixels.length + ") !!!");
            return null;
         }

         int[] var6 = var1;
         byte var8 = 0;
         int[] var7;
         if ((this.featureFlags & 4) != 0) {
            var8 = this.moduleImageIndex[var2];
            var7 = this.palettes[var8][this.currentPalette];
         } else {
            var7 = this.palettes[0][this.currentPalette];
         }

         if (var7 == null) {
            return null;
         }

         byte[] var9 = this.imageData;
         int var10 = this.moduleDataOffset[var2];
         int var11 = 0;
         int var12 = var4 * var5;
         if (this.pixelCodec != 25840) {
            if (this.pixelCodec == 10225) {
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
            } else if (this.pixelCodec == 22258) {
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
            } else if (this.pixelCodec == 5632) {
               while (var11 < var12) {
                  var6[var11++] = var7[var9[var10] >> 4 & 15];
                  var6[var11++] = var7[var9[var10] & 15];
                  var10++;
               }
            } else if (this.pixelCodec == 1024) {
               while (var11 < var12) {
                  var6[var11++] = var7[var9[var10] >> 6 & 3];
                  var6[var11++] = var7[var9[var10] >> 4 & 3];
                  var6[var11++] = var7[var9[var10] >> 2 & 3];
                  var6[var11++] = var7[var9[var10] & 3];
                  var10++;
               }
            } else if (this.pixelCodec == 512) {
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
            } else if (this.pixelCodec == 22018) {
               while (var11 < var12) {
                  var6[var11++] = var7[var9[var10++] & 0xFF];
               }
            }
         } else {
            while (var11 < var12) {
               int var13 = var9[var10++] & 255;
               int var14 = var7[var13 & this.packedColorMask[var8]];
               var13 >>= this.packedColorBits[var8];

               while (var13-- >= 0) {
                  var6[var11++] = var14;
               }
            }
         }

         if (this.cacheIndex < 0) {
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

   // $VF: renamed from: g (int, int) void
   public static final void createImageCache(int var0, int var1) {
      if (var1 < 0) {
         var1 = cacheImageCounts[var0];
      }

      cacheCapacity[var0] = var1;
      cacheEvictionSlots[var0] = new short[var1];

      for (int var2 = 0; var2 < cacheEvictionSlots[var0].length; var2++) {
         cacheEvictionSlots[var0][var2] = -1;
      }

      imageCaches[var0] = new Image[cacheImageCounts[var0]];
   }

   // $VF: renamed from: c (int) void
   public static final void destroyImageCache(int var0) {
      cacheEvictionSlots[var0] = null;
      cacheNextEviction[var0] = 0;
      cacheCapacity[var0] = 0;
      imageCaches[var0] = null;
      cacheImageCounts[var0] = 0;
   }

   // $VF: renamed from: d (int) void
   final void registerImageCache(int var1) {
      this.cacheBase = cacheImageCounts[var1];
      cacheImageCounts[var1] = cacheImageCounts[var1] + this.paletteCount * this.moduleCount;
      this.cacheIndex = var1;
   }

   // $VF: renamed from: a (java.lang.String) int
   final int measureText(String var1) {
      textWidth = 0;
      textHeight = this.moduleHeight[1] & 255;
      int var9 = 0;
      int var3 = textStart >= 0 ? textStart : 0;
      int var4 = textEnd >= 0 ? textEnd : var1.length();

      for (int var5 = var3; var5 < var4; var5++) {
         char var6;
         if ((var6 = var1.charAt(var5)) != '{' && var6 != '}') {
            if (var6 == '~') {
               var5++;
               var9 += this.moduleWidth[0] * 2;
            } else {
               int var7;
               if ((var7 = this.getGlyphFrame(var6)) == -1) {
                  var9 += this.moduleWidth[0] & 255;
               } else {
                  int var8 = Game.font.getFrameWidth(var7);
                  var9 += var8;
               }

               var9--;
            }
         }
      }

      if (var9 > textWidth) {
         textWidth = var9;
      }

      return var9;
   }

   // $VF: renamed from: a (char) int
   public final int getGlyphFrame(char var1) {
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

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, java.lang.String, int, int, int) void
   final void drawText(Graphics var1, String var2, int var3, int var4, int var5) {
      int var6;
      int var7 = (var6 = var1.getClipY()) + var1.getClipHeight();
      if (var4 >= var6 - 20 && var4 <= var7) {
         var2 = var2.toUpperCase();
         if ((var5 & 43) != 0) {
            this.measureText(var2);
            if ((var5 & 8) != 0) {
               var3 -= textWidth;
            } else if ((var5 & 1) != 0) {
               var3 -= textWidth >> 1;
            }

            if ((var5 & 32) != 0) {
               var4 -= textHeight;
            } else if ((var5 & 2) != 0) {
               var4 -= textHeight >> 1;
            }
         }

         int var18 = var3;
         int var9 = var4;
         int var10 = textStart >= 0 ? textStart : 0;
         int var11 = textEnd >= 0 ? textEnd : var2.length();
         boolean var12 = false;

         for (int var13 = var10; var13 < var11; var13++) {
            char var14;
            if ((var14 = var2.charAt(var13)) == '{') {
               this.currentPalette = 1;
               this.textLastPalette = this.currentPalette;
            } else if (var14 == '}') {
               this.currentPalette = this.textBasePalette;
               this.textLastPalette = this.textBasePalette;
            } else if (var14 == '~') {
               var13++;
               int var15 = var2.charAt(var13) - '0';
               Game.sprites[33]
                  .drawAnimationFrame(
                     var1,
                     var15,
                     Game.stateFrame % inlineIconFrameCounts[var15],
                     var18 + inlineIconXOffsets[var15],
                     var9 + (this.moduleHeight[1] & 255) - inlineIconMetrics[(var15 << 1) + 1],
                     0,
                     0,
                     0
                  );
               var18 += this.moduleWidth[0] + inlineIconMetrics[var15 << 1] + inlineIconXOffsets[var15] / 2;
            } else {
               int var19;
               if ((var19 = this.getGlyphFrame(var14)) == -1) {
                  var18 += this.moduleWidth[0] & 255;
               }

               this.drawFrame(var1, var19, var18, var9, 0, 0, 0);
               int var16 = Game.font.getFrameWidth(var19);
               var18 += var16 & 0xFF;
               var18--;
            }
         }
      }
   }

   // $VF: renamed from: a (java.lang.String, int) void
   public final void wrapText(String var1, int var2) {
      if (wrappedText == null || !wrappedText.equals(var1)) {
         wrappedText = var1;
         wrappedLineCount = 0;
         int var3 = 0;
         int var4 = 0;
         int var12 = 0;
         int var6 = var1.length();

         for (int var7 = 0; var7 < var6; var7++) {
            char var8;
            if ((var8 = var1.charAt(var7)) == '^' && var12 <= var2) {
               wrappedLineEnds[var3++] = var7;
               var12 = 0;
               var4 = 0;
            } else if (var2 > 0 && (var8 == '^' || var8 == ' ' || var7 == var6 - 1) && var12 > var2) {
               do {
                  var7--;
               } while (var1.charAt(var7) != ' ' && var7 > (var3 > 0 ? wrappedLineEnds[var3 - 1] : 0));

               if (var7 == (var3 > 0 ? wrappedLineEnds[var3 - 1] : 0)) {
                  var7 = (var3 > 0 ? wrappedLineEnds[var3 - 1] : 0) + var4;
               }

               wrappedLineEnds[var3++] = var7;
               wrappedLineCount = var3;
               var4 = 0;
               var12 = 0;
            } else {
               var4++;
               if (var8 != '{' && var8 != '}') {
                  if (var8 == '~') {
                     var7++;
                     var12 += this.moduleWidth[0] * 2;
                  } else {
                     int var9;
                     if ((var9 = this.getGlyphFrame(var8)) == -1) {
                        var12 += this.moduleWidth[0] & 255;
                     } else {
                        int var10 = this.frameModules[var9 << 2] & 255;
                        var12 += (this.moduleWidth[var10] & 255) + this.frameModules[1];
                     }

                     if (this.currentPalette == 5) {
                        var12 -= 2;
                     } else {
                        var12--;
                     }
                  }
               }
            }
         }

         wrappedLineEnds[var3++] = var6;
         wrappedLineCount = var3;
         Game.bb = wrappedLineCount;
      }
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Graphics, java.lang.String, int, int, int, int, int, int) int
   final int drawWrappedText(Graphics var1, String var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      this.textLastPalette = this.currentPalette;
      this.textBasePalette = this.currentPalette;
      var2 = '}' + var2;
      this.wrapText(var2, var6);
      int var9 = this.lineSpacing + (this.moduleHeight[1] & 255);
      if (Game.state == 20) {
         var9 += 3;
      }

      if ((var5 & 32) != 0) {
         var4 -= var9 * (wrappedLineCount - 1);
      } else if ((var5 & 2) != 0) {
         var4 -= var9 * (wrappedLineCount - 1) >> 1;
      }

      for (int var10 = var7; var10 < wrappedLineCount; var10++) {
         textStart = var10 > 0 ? wrappedLineEnds[var10 - 1] + 1 : 0;
         textEnd = wrappedLineEnds[var10];
         this.drawText(var1, var2, var3, var4 + (var10 - var7) * var9, var5);
         if (var8 != -1 && var10 - var7 >= var8 - 1) {
            textStart = -1;
            textEnd = -1;
            return var4 + var10 * var9;
         }
      }

      this.currentPalette = this.textBasePalette;
      textStart = -1;
      textEnd = -1;
      return var4 + wrappedLineCount * var9;
   }

   static {
      " !\"#$%&'*+,_./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ()-ÄÖÜß¡¿ÁÀÃÈÉÊÎÏÍÓÔÕÚÙÑÇÒ©ĞÂÌŞİ".length();
   }
}
