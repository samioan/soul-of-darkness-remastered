import java.util.Hashtable;
import javax.microedition.lcdui.Graphics;

// $VF: renamed from: b
public final class Actor {
   // $VF: renamed from: a int
   public int fixedX;
   // $VF: renamed from: b int
   public int fixedY;
   // $VF: renamed from: c int
   public int flags;
   // $VF: renamed from: d int
   public static int previousPlayerAnimation = -1;
   // $VF: renamed from: e int
   public static int queuedComboAnimation = -1;
   // $VF: renamed from: f int
   public int animationId = -1;
   // $VF: renamed from: g int
   public int pendingAnimationId = -1;
   // $VF: renamed from: h int
   public int animationFrame;
   // $VF: renamed from: i int
   public int animationTick;
   // $VF: renamed from: j int[]
   public int[] collisionBounds;
   // $VF: renamed from: k int[]
   public int[] attackBounds;
   // $VF: renamed from: l int[]
   public int[] visualBounds;
   // $VF: renamed from: m int[]
   public int[] customBounds;
   // $VF: renamed from: n int[]
   public int[] moduleMask;
   // $VF: renamed from: o boolean
   public boolean boundsDirty;
   // $VF: renamed from: p d
   public BSprite sprite;
   // $VF: renamed from: q b
   public Actor linkedEffect;
   // $VF: renamed from: r b
   public Actor attachedActor;
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
   // $VF: renamed from: E b
   public static Actor activeBoss;
   // $VF: renamed from: F int
   public static int bossInitialHealth;
   // $VF: renamed from: G int
   public int velocityX;
   // $VF: renamed from: H int
   public int velocityY;
   // $VF: renamed from: I int
   public int accelerationX;
   // $VF: renamed from: J int
   public int accelerationY;
   // $VF: renamed from: K int
   public int positionX;
   // $VF: renamed from: L int
   public int positionY;
   // $VF: renamed from: M boolean
   public boolean scrollWithWorld;
   public boolean N;
   public boolean O;
   public boolean P;
   public boolean Q;
   public boolean R;
   public boolean S;
   // $VF: renamed from: T b
   public Actor supportActor;
   // $VF: renamed from: U b
   public Actor supportedActor;
   public static boolean V;
   public static boolean W;
   public static int X;
   public static int Y;
   public static int Z;
   public static int aa;
   public static int ab;
   public int ac = 0;
   public int ad = 0;
   // $VF: renamed from: ae int
   public int persistenceIndex;
   // $VF: renamed from: af java.util.Hashtable
   public static Hashtable spawnRecordsByUid = new Hashtable();
   // $VF: renamed from: ag int[][]
   public static int[][] spawnRecordPool = new int[200][2];
   // $VF: renamed from: ah int
   public static int spawnRecordCount;
   // $VF: renamed from: ai int
   public int cameraDistanceBucket = 10;
   // $VF: renamed from: aj int
   public int activationRadius = 1;
   // $VF: renamed from: ak boolean
   public boolean facingLeft;
   // $VF: renamed from: al int
   public int uid;
   // $VF: renamed from: am int
   public int type;
   // $VF: renamed from: an int
   public int paintPriority;
   // $VF: renamed from: ao int
   public int actionState;
   // $VF: renamed from: ap int
   public int health;
   // $VF: renamed from: aq boolean
   public boolean updatedThisTick = false;
   public int ar;
   public int as;
   public int at;
   public int au;
   public int av;
   public int aw;
   public int ax;
   public int ay;
   public int az;
   public int aA;
   public int aB;
   // $VF: renamed from: aC int
   public int variantData;
   // $VF: renamed from: aD int
   public int statSet;
   // $VF: renamed from: aE int[]
   public int[] interactionBounds;
   public int aF;
   public int aG;
   public static boolean aH;
   public static boolean aI;
   // $VF: renamed from: aJ boolean
   public static boolean godMode;
   private static int[] cF = new int[4];
   // $VF: renamed from: aK b
   public static Actor playerPlatform;
   // $VF: renamed from: aL b
   public static Actor lastSpawnedActor;
   public static Actor[] aM;
   public static int aN;
   public static Actor[] aO;
   public static Actor[] aP;
   public static Actor[] aQ;
   public static Actor[] aR = new Actor[2];
   // $VF: renamed from: aS int
   public static int weaponSwitchCooldownMs;
   static int aT;
   static final int[] aU = new int[]{13, 14, 65568, 65568};
   static final int[] aV = new int[]{15, 65568};
   static final int[] aW = new int[]{19, 20, 65568, 65568};
   static final int[] aX = new int[]{13, 14, 13, 14, 15};
   static final int[] aY = new int[]{0, -20, 0, 500};
   static final int[] aZ = new int[]{22, 0, 0, 300, 0, 0, 20, 40, 22, 0, 0, 300, 0, 0, 20, 40, 22, 0, 0, 300, 0, 0, 0, 2000};
   public static boolean ba;
   public static int bb;
   public static int[] bc;
   public static boolean bd;
   public static int be;
   public static int[] bf;
   public static int bg;
   public static boolean bh;
   public static boolean bi;
   // $VF: renamed from: bj int[]
   public static final int[] spriteIdByType = new int[]{
      0,
      2,
      3,
      3,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      10,
      -1,
      -1,
      12,
      14,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      18,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      25,
      -1,
      36,
      28,
      28,
      28,
      28,
      33,
      28,
      29,
      28,
      1,
      -1,
      -1,
      -1,
      36,
      28,
      36,
      41,
      36,
      27,
      -1,
      28,
      28,
      34,
      28,
      28,
      34,
      28,
      36,
      28,
      -1,
      28,
      40,
      41,
      41,
      41,
      41,
      41,
      -1,
      -1,
      45,
      47,
      27,
      28,
      48,
      -1,
      1,
      48,
      -1,
      -1,
      49,
      1,
      -1,
      28,
      -1,
      -1,
      -1,
      1,
      -1,
      -1,
      28,
      15,
      1,
      -1,
      -1,
      -1,
      28,
      0,
      0,
      -1,
      -1,
      15,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1
   };
   // $VF: renamed from: bk javax.microedition.lcdui.Graphics
   public static Graphics graphics;
   public static boolean bl;
   public static boolean bm;
   public static boolean bn;
   public static boolean bo;
   public static boolean bp;
   public static boolean bq;
   public static boolean br;
   public static boolean bs;
   public static boolean bt;
   public static boolean bu;
   public static int bv;
   public static boolean bw;
   public static short[] bx = new short[]{-1, -1, -1};
   public static byte[] by = new byte[54];
   public static int bz;
   // $VF: renamed from: cG int
   private int boundsCenterX;
   // $VF: renamed from: cH int
   private int boundsCenterY;
   static int bA;
   // $VF: renamed from: cI int
   private int tileAtHead;
   // $VF: renamed from: cJ int
   private int tileAtFeet;
   // $VF: renamed from: cK int
   private int tileAtLeftSide;
   // $VF: renamed from: cL int
   private int tileAtRightSide;
   // $VF: renamed from: cM int
   private int tileBelowHead;
   // $VF: renamed from: cN int
   private int tileAboveFeet;
   // $VF: renamed from: cO int
   private int tileBelowFeet;
   // $VF: renamed from: cP int
   private int tileLeftOfFeet;
   // $VF: renamed from: cQ int
   private int tileRightOfFeet;
   // $VF: renamed from: bB boolean
   public boolean grounded;
   public static boolean bC;
   // $VF: renamed from: bD boolean
   public boolean ceilingHit;
   // $VF: renamed from: cR boolean
   private boolean wallOnLeft;
   // $VF: renamed from: cS boolean
   private boolean wallOnRight;
   // $VF: renamed from: cT boolean
   private boolean verticalProbeActive;
   // $VF: renamed from: cU int
   private static int baseInvulnerabilityMs = 600;
   // $VF: renamed from: bE int
   public static int playerInvulnerabilityMs;
   public static boolean bF;
   private static int cV;
   private static int cW;
   private static int cX;
   // $VF: renamed from: cY boolean
   private static boolean jumpAllowed;
   // $VF: renamed from: cZ boolean
   private static boolean diagonalJump;
   // $VF: renamed from: da boolean
   private static boolean attackAllowed;
   // $VF: renamed from: bG boolean
   static boolean shortJump;
   private static int db;
   private static boolean dc;
   public static boolean bH = false;
   public static boolean bI = false;
   public static boolean bJ;
   // $VF: renamed from: bK int
   public static int activeWeapon;
   public static final int[] bL = new int[]{15, 20, 25, 15, 20, 25};
   public static int[] bM = new int[]{0, 0};
   public static int[] bN = new int[]{1, 0};
   public static int[] bO = new int[]{0, 0};
   public static int[] bP = new int[]{0, 0};
   public static int[] bQ = new int[]{6, 9, 9};
   public static final int[] bR = new int[]{15, 15, 15};
   public static final int[] bS = new int[]{10, 13, 15, 20};
   public static final int[] bT = new int[]{10, 13, 15, 20};
   public boolean bU = false;
   public Actor bV;
   public static boolean bW = false;
   public static int bX;
   public static final int[] bY = new int[]{0, 80, 10, 10};
   public static final int[] bZ = new int[]{15, 10, -15, 0};
   public static final int[] ca = new int[]{0, 13, 10, 10};
   public static final int[] cb = new int[]{0, 50, 10, 10};
   public static final int[] cc = new int[]{0, 90, 60, 60};
   public static final int[] cd = new int[]{-65, 50, 15, 15};
   private int dd;
   public static int[] ce = new int[2];
   public static int cf = 8;
   public static int cg = 30;
   public static int ch = 30;
   public static int ci = 10;
   public static int[] cj = new int[ci];
   public static int[] ck = new int[ci];
   private static final int[] de = new int[]{16714752, 16777215, 16714752};
   public static boolean cl = false;
   // $VF: renamed from: df int
   private static int worldScrollPixelsPerTick = 0;
   public static int[] cm = new int[3];
   public static int cn;
   public static int co = 0;
   public static int cp;
   public static Actor[] cq = new Actor[3];
   public static int[] cr = new int[4];
   static int cs = 3;
   public static final int[] ct = new int[]{10, 20, 30, 40, 1};
   public static boolean cu;
   public static int cv;
   public static Actor cw;
   public static Actor[] cx = new Actor[3];
   private int dg;
   private int dh;
   private int di;
   private boolean dj;
   static int cy = 0;
   static int cz = 1;
   public static int cA = -1;
   public static int cB = -1;
   public static int cC = 0;
   public static final int[] cD = new int[]{-1149781248, -1720206592, 2004335360, 1433910016, 2004335360, -1720206592};
   static final int[] cE = new int[]{
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

   // $VF: renamed from: a () void
   final void releaseLinksAndBounds() {
      this.collisionBounds = null;
      this.attackBounds = null;
      this.visualBounds = null;
      this.linkedEffect = null;
      this.attachedActor = null;
   }

   public final void b() {
      this.flags |= 128;
      this.actionState = 106;
      this.az = 1162;
   }

   // $VF: renamed from: a (boolean) void
   public final void dropLoot(boolean var1) {
      int var2 = Game.health * 100 / Game.maxHealth;
      int var3 = 10000 + var2 * var2 - (var2 * 1500 >> 3);
      if (Game.randomRange(0, 10000) < var3) {
         this.spawnActor(35, 33, 0, Game.randomRange(-40, 40), Game.randomRange(-20, -100), 308, false);
         lastSpawnedActor.b();
      }

      if (Game.magic <= Game.maxMagic * 40 / 100 && bu) {
         var2 = 100 - Game.magic * 60 / Game.maxMagic;
         if (Game.randomRange(0, 100) < var2) {
            this.spawnActor(35, 33, 2, Game.randomRange(-40, 40), Game.randomRange(-20, -100), 308, false);
            lastSpawnedActor.b();
         }
      }
   }

   // $VF: renamed from: c () boolean
   public final boolean updateRespawnFade() {
      switch (this.actionState) {
         case 5:
            this.decrementTimerA();
            if (this.aw <= 800) {
               this.actionState = 6;
            }

            return true;
         case 6:
            this.spawnActor(39, 1, 28, 0, 0, 302, false);
            lastSpawnedActor.positionY = lastSpawnedActor.positionY - (this.collisionBounds[3] - this.collisionBounds[1] >> 1);
            this.actionState = 7;
            return true;
         case 7:
            this.decrementTimerA();
            if (this.aw <= 0) {
               if (this.customBounds != null) {
                  System.arraycopy(Game.player.collisionBounds, 0, this.customBounds, 0, 4);
               }

               this.changeActionState(0);
               this.flags &= -129;
            }

            return true;
         default:
            return false;
      }
   }

   // $VF: renamed from: b (boolean) void
   final void resetOrRemove(boolean var1) {
      this.resetOrRemoveInternal(var1, false);
   }

   // $VF: renamed from: a (boolean, boolean) void
   final void resetOrRemoveInternal(boolean var1, boolean var2) {
      if (var1) {
         this.dropLoot(var2);
      }

      this.O = false;
      this.supportActor = null;
      this.supportedActor = null;
      boolean var3 = false;
      int var4 = 0;
      if (var1) {
         this.di--;
         var4 = this.di;
         if (this.di < -1) {
            var4 = -1;
         }
      }

      if (((this.flags & 4) != 0 || var4 >= 0) && var1) {
         var3 = true;
      }

      this.actionState = 0;
      if (this.type == 31 && this.ay == 9) {
         this.setCollisionRectEmpty(this.collisionBounds, true);
      }

      this.velocityX = 0;
      this.velocityY = 0;
      this.accelerationX = 0;
      this.accelerationY = 0;
      boolean var5 = false;
      if ((this.flags & 512) != 0) {
         var5 = true;
      }

      if (this.uid >= 0 && this.isRespawnable()) {
         int[] var6;
         if ((var6 = (int[])spawnRecordsByUid.get(new Integer(this.uid))) != null) {
            int var7 = var6[0];

            for (int var8 = 0; var8 < var6[1]; var8++) {
               Game.bE[var8] = (short)((Game.bF[var7++] & 255) + (Game.bF[var7++] << 8));
            }

            this.initializeFromRecord(Game.bE, false, var6[0], var6[1]);
            if (var3) {
               this.flags &= -33;
               if (this.type == 70 && (this.variantData == 0 || this.variantData == 1)) {
                  this.snapDownToGround();
               }
            }

            if (var1) {
               this.di = var4;
            }
         }
      } else {
         if (var5) {
            this.flags |= 512;
         }

         Game.removeActor(this);
      }

      if (var5) {
         this.flags |= 512;
      }

      this.flags |= 1024;
      if ((this.flags & 2048) != 0 && var1 && !var3) {
         Game.removeActor(this);
      }

      if (var3) {
         this.beginRespawnFade();
      }
   }

   // $VF: renamed from: d () void
   public final void beginRespawnFade() {
      this.actionState = 5;
      this.flags &= -1025;
      this.flags |= 128;
      this.aw = 1500;
   }

   // $VF: renamed from: a (b) void
   final void setLinkedEffect(Actor var1) {
      if (this.linkedEffect != null) {
         this.linkedEffect = null;
      }

      if (var1 != null) {
         this.linkedEffect = var1;
      }
   }

   // $VF: renamed from: b (b) void
   final void setAttachedActor(Actor var1) {
      if (this.attachedActor != null) {
         this.attachedActor.flags &= -257;
         this.attachedActor = null;
      }

      if (var1 != null) {
         this.attachedActor = var1;
         var1.flags |= 256;
      }
   }

   // $VF: renamed from: a (int, boolean) void
   final void requestAnimationRestart(int var1, boolean var2) {
      if (var1 == this.animationId && var2) {
         if (this.type == 0) {
            previousPlayerAnimation = this.animationId;
         }

         if (this.animationId == -1) {
            this.animationId = var1;
            this.animationFrame = 0;
            this.animationTick = 0;
         } else {
            this.pendingAnimationId = var1;
         }

         this.flags &= -65;
         this.boundsDirty = true;
      }

      this.requestAnimation(var1);
   }

   // $VF: renamed from: a (int) void
   final void requestAnimation(int var1) {
      if (var1 >= 0 && (this.sprite == null || var1 < this.sprite.animationFrameCount.length)) {
         if (this.type == 0 && var1 == 4) {
            cV = this.positionY;
         }

         if (var1 != this.animationId) {
            if (this.type == 0) {
               previousPlayerAnimation = this.animationId;
            }

            if (this.animationId == -1) {
               this.animationId = var1;
               this.animationFrame = 0;
               this.animationTick = 0;
            } else {
               this.pendingAnimationId = var1;
            }

            this.flags &= -65;
            this.boundsDirty = true;
         }
      } else {
         System.out.println("ERROR: Anim out of range ! at UID:" + this.uid + ",AType:" + this.type);
      }
   }

   // $VF: renamed from: b (int) void
   final void setAnimationImmediate(int var1) {
      if (var1 != this.animationId) {
         if (this.type == 0) {
            previousPlayerAnimation = this.animationId;
         }

         this.animationId = var1;
         this.animationFrame = 0;
         this.animationTick = 0;
         this.pendingAnimationId = -1;
         this.flags &= -65;
      }
   }

   // $VF: renamed from: e () boolean
   final boolean isAnimationEnding() {
      int var1;
      return this.animationFrame != this.sprite.getAnimationFrameCount(this.animationId) - 1
         ? false
         : (var1 = this.sprite.getAnimationFrameDuration(this.animationId, this.animationFrame)) <= 1 || this.animationTick == var1 - 1;
   }

   // $VF: renamed from: a (int, int) void
   final void drawAtCamera(int var1, int var2) {
      if (this.sprite != null) {
         try {
            int var3 = this.positionX;
            int var4 = this.positionY;
            if (!Game.aJ) {
               int var5 = Game.stateFrame % 2 == 0 ? 1 : -1;
               if (this.type == 37 && this.aw > 50 && this.aw < 1000 && this.animationId == 1 || this.type == 35 || this.type == 75 && this.actionState == 109) {
                  var3 += var5;
               }

               if (this.type == 95 && this.actionState == 101) {
                  if (this.ar % 2 == 0) {
                     var3 += var5;
                  } else {
                     var4 += var5;
                  }
               }
            }

            if (this.animationTick >= 0) {
               this.sprite.drawAnimationFrame(graphics, this.animationId, this.animationFrame, var3, var4, this.flags, var1, var2);
            } else if (this.animationId >= 0) {
               this.sprite.drawModule(graphics, this.animationId, var3, var4, this.flags);
            } else if (this.animationFrame >= 0) {
               this.sprite.drawFrame(graphics, this.animationFrame, var3, var4, this.flags, var1, var2);
            }
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }
   }

   // $VF: renamed from: f () void
   final void advanceAnimation() {
      if (this.pendingAnimationId != -1) {
         this.animationId = this.pendingAnimationId;
         this.animationFrame = 0;
         this.animationTick = 0;
         this.pendingAnimationId = -1;
      } else if ((this.flags & 64) == 0 && this.sprite != null && this.animationTick >= 0) {
         int var1;
         if ((var1 = this.sprite.getAnimationFrameDuration(this.animationId, this.animationFrame)) != 0) {
            this.animationTick++;
            if (var1 <= this.animationTick) {
               this.animationTick = 0;
               this.animationFrame++;
               if (this.animationFrame >= this.sprite.getAnimationFrameCount(this.animationId)) {
                  this.animationFrame = 0;
               }
            }
         }
      }
   }

   // $VF: renamed from: g () int[]
   public final int[] refreshBounds() {
      this.boundsDirty = false;
      if (this.collisionBounds == null) {
         this.collisionBounds = new int[4];
         this.attackBounds = new int[4];
      }

      if (this.customBounds != null) {
         if (this.type == 22
            || this.type == 21
            || this.type == 40
            || this.type == 45
            || this.type == 49
            || this.type == 20
            || this.type == 23
            || this.type == 59
            || this.type == 68
            || this.type == 74
            || this.type == 87
            || this.type == 80) {
            this.collisionBounds[0] = this.customBounds[0];
            this.collisionBounds[1] = this.customBounds[1];
            this.collisionBounds[2] = this.customBounds[2];
            this.collisionBounds[3] = this.customBounds[3];
            return this.collisionBounds;
         }

         if (this.type == 78) {
            this.collisionBounds[0] = this.customBounds[0];
            this.collisionBounds[1] = this.customBounds[1] - this.ar;
            this.collisionBounds[2] = this.customBounds[2];
            this.collisionBounds[3] = this.customBounds[3];
            return this.collisionBounds;
         }
      }

      if (this.sprite != null) {
         if (this.animationTick >= 0) {
            this.sprite.getAnimationBounds(this.collisionBounds, this.animationId, this.animationFrame, 0, this.flags, 0, 0);
         } else if (this.animationId >= 0) {
            this.sprite.getModuleBounds(this.collisionBounds, this.animationId, this.flags);
         } else if (this.animationFrame >= 0) {
            this.sprite.getFrameBounds(this.collisionBounds, this.animationFrame, 0, this.flags, 0, 0);
         }

         if (this.animationTick >= 0) {
            this.sprite.getAnimationBounds(this.attackBounds, this.animationId, this.animationFrame, 1, this.flags, 0, 0);
         } else {
            this.sprite.getFrameBounds(this.attackBounds, this.animationId, 1, this.flags, 0, 0);
         }

         if (this.visualBounds == null) {
            this.visualBounds = new int[4];
         }

         if (this.animationTick >= 0) {
            this.sprite.getAnimationBounds(this.visualBounds, this.animationId, this.animationFrame, -1, this.flags, 0, 0);
         } else {
            this.sprite.getFrameBounds(this.visualBounds, this.animationId, -1, this.flags, 0, 0);
         }

         this.visualBounds[0] = this.visualBounds[0] + this.positionX;
         this.visualBounds[1] = this.visualBounds[1] + this.positionY;
         this.visualBounds[2] = this.visualBounds[2] + this.positionX;
         this.visualBounds[3] = this.visualBounds[3] + this.positionY;
      }

      this.collisionBounds[0] = this.collisionBounds[0] + this.positionX;
      this.collisionBounds[1] = this.collisionBounds[1] + this.positionY;
      this.collisionBounds[2] = this.collisionBounds[2] + this.positionX;
      this.collisionBounds[3] = this.collisionBounds[3] + this.positionY;
      if (this.collisionBounds[0] == this.collisionBounds[2] && this.collisionBounds[1] == this.collisionBounds[3]) {
         for (int var1 = 0; var1 < 4; var1++) {
            this.collisionBounds[var1] = this.visualBounds[var1];
         }
      }

      this.attackBounds[0] = this.attackBounds[0] + this.positionX;
      this.attackBounds[1] = this.attackBounds[1] + this.positionY;
      this.attackBounds[2] = this.attackBounds[2] + this.positionX;
      this.attackBounds[3] = this.attackBounds[3] + this.positionY;
      if (this.type == -101 && this.animationId == 1) {
         this.attackBounds[0] = this.attackBounds[0] - 30;
      }

      return this.collisionBounds;
   }

   // $VF: renamed from: a (int[], int[]) boolean
   static final boolean rectContainsRect(int[] var0, int[] var1) {
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

   // $VF: renamed from: b (int[], int[]) boolean
   static final boolean rectsOverlapInclusive(int[] var0, int[] var1) {
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

   // $VF: renamed from: c (int[], int[]) boolean
   static final boolean rectsOverlapStrict(int[] var0, int[] var1) {
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

   // $VF: renamed from: a (int, int, int[]) boolean
   static final boolean pointInRect(int var0, int var1, int[] var2) {
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
         if (cq[var2] == var1) {
            return true;
         }
      }

      return false;
   }

   // $VF: renamed from: ai () int
   private int resolveActorSeparation() {
      byte var2 = 0;

      for (int var3 = Game.updateActorCount - 1; var3 >= 0; var3--) {
         Actor var1;
         if ((var1 = Game.updateActors[var3]) != null && var1 != this && var1.N) {
            var1.refreshBounds();
            if (this.type == 0) {
               if (var1 != playerPlatform
                  && var1.type != 82
                  && rectsOverlapInclusive(var1.collisionBounds, this.collisionBounds)
                  && this.positionY > var1.collisionBounds[1]
                  && !var1.O) {
                  if (this.positionX <= var1.positionX && this.collisionBounds[2] - var1.collisionBounds[0] > 0) {
                     if (!this.wallOnLeft) {
                        this.positionX = this.positionX - (this.collisionBounds[2] - var1.collisionBounds[0]);
                     } else {
                        this.positionX = this.positionX + (var1.collisionBounds[2] - this.collisionBounds[0]);
                     }
                  } else if (this.positionX >= var1.positionX && var1.collisionBounds[2] - this.collisionBounds[0] > 0) {
                     if (!this.wallOnRight) {
                        this.positionX = this.positionX + (var1.collisionBounds[2] - this.collisionBounds[0]);
                     } else {
                        this.positionX = this.positionX - (this.collisionBounds[2] - var1.collisionBounds[0]);
                     }
                  }
               }
            } else if (var1.type == 37) {
               if (!var1.O || this.type != 0) {
                  if (this.positionX <= var1.positionX && this.collisionBounds[2] - var1.collisionBounds[0] > 0) {
                     if (!this.wallOnLeft) {
                        this.positionX = this.positionX - (this.collisionBounds[2] - var1.collisionBounds[0]);
                     } else {
                        this.positionX = this.positionX + (var1.collisionBounds[2] - this.collisionBounds[0]);
                     }
                  } else if (this.positionX >= var1.positionX && var1.collisionBounds[2] - this.collisionBounds[0] > 0) {
                     if (!this.wallOnRight) {
                        this.positionX = this.positionX + (var1.collisionBounds[2] - this.collisionBounds[0]);
                     } else {
                        this.positionX = this.positionX - (this.collisionBounds[2] - var1.collisionBounds[0]);
                     }
                  }
               }
            } else {
               if (this.type == 0
                  && var1 == playerPlatform
                  && (this.collisionBounds[0] > var1.collisionBounds[2] || this.collisionBounds[2] < var1.collisionBounds[0])
                  && playerPlatform.type != 50) {
                  playerPlatform = null;
                  this.startFalling(0);
               }

               if (this.canSupportActor() && var1.canStandOnActors() && var1.velocityY > 0 && var1.velocityY > this.velocityY) {
                  int var5 = var1.fixedY + var1.velocityY >> 8;
                  if (var1.positionY <= this.collisionBounds[1]
                     && var1.collisionBounds[2] > this.collisionBounds[0]
                     && var1.collisionBounds[0] < this.collisionBounds[2]
                     && var5 >= this.collisionBounds[1] + (this.velocityY >> 8)) {
                     var1.supportActor = this;
                     if (!this.c(this)) {
                        this.supportedActor = var1;
                     }

                     var1.velocityY = 0;
                  }
               } else if (var1.canSupportActor() && this.canStandOnActors() && this.velocityY > 0 && this.velocityY > var1.velocityY) {
                  int var4 = this.fixedY + this.velocityY >> 8;
                  if (this.positionY <= var1.collisionBounds[1]
                     && this.collisionBounds[2] > var1.collisionBounds[0]
                     && this.collisionBounds[0] < var1.collisionBounds[2]
                     && var4 >= var1.collisionBounds[1] + (var1.velocityY >> 8)) {
                     this.supportActor = var1;
                     if (!this.c(this)) {
                        var1.supportedActor = this;
                     }

                     this.velocityY = 0;
                  }
               }

               if (rectsOverlapInclusive(var1.collisionBounds, this.collisionBounds)
                  && var1.type != 82
                  && var1.supportActor != this
                  && this.supportActor != var1) {
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

   // $VF: renamed from: h () boolean
   public final boolean isVisible() {
      int var1 = this.positionX - (Game.cameraX + 120);
      int var2 = this.positionY - (Game.cameraY + 160);
      if (var1 < 0) {
         var1 = -var1;
      }

      if (var2 < 0) {
         var2 = -var2;
      }

      this.cameraDistanceBucket = var1 / 120 + var2 / 160;
      if (this.cameraDistanceBucket > this.activationRadius) {
         if (this.type == 10 && this.u != null) {
            this.u.refreshBounds();
            return rectsOverlapInclusive(Game.viewBounds, this.u.collisionBounds);
         } else {
            return false;
         }
      } else if ((this.type != 39 || this.animationId != 29) && this.type != -2) {
         if (this.type == 28) {
            return true;
         }

         if (this.type == -101) {
            this.refreshBounds();
            return true;
         }

         if (this.type == 21 && this.uid == -1) {
            return true;
         }

         this.refreshBounds();
         if (this.type != 21 && this.type != 22 && this.type != 23) {
            if (this.type == 45 || this.type == 49 || this.type == 78 || this.type == 80 || this.visualBounds == null) {
               return rectsOverlapInclusive(Game.viewBounds, this.collisionBounds);
            } else if (this.type == 50 || this.type == 53) {
               return rectsOverlapInclusive(this.customBounds, Game.viewBounds);
            } else if (this.type == 10) {
               return true;
            } else {
               return this.type == 70 && this.variantData == 3 && this.actionState == 114
                  ? rectsOverlapInclusive(Game.viewBounds, this.interactionBounds)
                  : rectsOverlapInclusive(Game.viewBounds, this.visualBounds);
            }
         } else if (this.collisionBounds == null) {
            return true;
         } else {
            return this.type != 21 || this.animationId != 17 && this.animationId != 18
               ? rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)
               : rectsOverlapInclusive(Game.viewBounds, this.collisionBounds);
         }
      } else {
         return true;
      }
   }

   // $VF: renamed from: h (int, int) void
   private void alignAnimationToBounds(int var1, int var2) {
      if ((var2 & 1) == 0) {
         this.requestAnimation(var1);
         this.refreshBounds();
      }

      if ((var2 & 2048) != 0) {
         this.positionX = this.collisionBounds[0];
      } else if ((var2 & 4096) != 0) {
         this.positionX = this.collisionBounds[2];
      } else if ((var2 & 4) != 0) {
         int var3 = this.collisionBounds[0] + this.collisionBounds[2] >> 1;
         this.positionX = this.positionX + (this.boundsCenterX - var3);
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
         this.positionY = this.positionY + (this.boundsCenterY - var4);
      } else if ((var2 & 1024) != 0) {
         this.positionY = this.positionY + (this.positionY - this.collisionBounds[1]);
      }

      if ((var2 & 16) != 0) {
         this.positionY = this.positionY - (this.positionY % 20 - 10);
      }

      if ((var2 & 1) != 0) {
         this.requestAnimation(var1);
      }
   }

   // $VF: renamed from: o (int) void
   private void startFalling(int var1) {
      this.alignAnimationToBounds(4, 32);
      this.velocityY = var1;
      this.accelerationY = 768;
   }

   // $VF: renamed from: c (int) boolean
   public static final boolean damagePlayer(int var0) {
      Game.k = true;
      if (playerInvulnerabilityMs > 0 && var0 < 500) {
         return false;
      }

      if (Game.player.bU) {
         playerInvulnerabilityMs = baseInvulnerabilityMs << 1;
      } else {
         playerInvulnerabilityMs = baseInvulnerabilityMs;
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

   // $VF: renamed from: d (int) boolean
   public static final boolean spendMagic(int var0) {
      Game.k = true;
      if (Game.magic >= var0) {
         if (godMode) {
            return true;
         }

         Game.magic -= var0;
         aT = 0;
         return true;
      } else {
         return false;
      }
   }

   // $VF: renamed from: i () void
   public static final void regenerateMagic() {
      if (!bu) {
         Game.magic = 0;
      } else {
         aT = aT + Game.tickDeltaMs;
         switch (Game.player.actionState) {
            case 0:
            case 2:
               if (Game.magic != Game.maxMagic && aT >= 5000) {
                  aT = 0;
                  if (Game.magic < Game.maxMagic) {
                     Game.magic += 10;
                     if (Game.magic > Game.maxMagic) {
                        Game.magic = Game.maxMagic;
                     }

                     Game.aR = true;
                     Game.k = true;
                  }
               }
         }
      }
   }

   public Actor() {
   }

   private static boolean b(int[] var0, int var1) {
      boolean var2 = false;
      int var3 = (var0.length + 1) / 2;

      for (int var4 = 0; var4 < Math.min(var3 - 1, var1); var4++) {
         if (Game.player.animationId == var0[var4] && Game.wasKeyPressed(var0[var4 + var3])) {
            queuedComboAnimation = var0[var4 + 1];
            return true;
         }
      }

      return false;
   }

   private static void aj() {
      if (queuedComboAnimation == -1) {
         if (activeWeapon == 0) {
            if (!Game.player.grounded && playerPlatform == null && !bC) {
               b(aV, 0);
            } else {
               b(aU, (aU.length >> 1) - 1);
            }
         } else if (activeWeapon != 1 || !b(aW, (aU.length >> 1) - 1)) {
            ;
         }
      }
   }

   public final void a(int[] var1) {
      bc = var1;
      ba = true;
      bb = 0;
      this.requestAnimation(bc[0]);
   }

   public static final void j() {
      bd = false;
      be = 0;
      bg = 0;
      bh = false;
      bi = false;
      ba = false;
      bb = 0;
   }

   public final void b(int[] var1) {
      bf = var1;
      bd = true;
      be = 0;
      this.l();
   }

   public final void k() {
      bg = bg - Game.tickDeltaMs;
      if (!bh && bg <= 0 || bh && this.isAnimationEnding()) {
         be++;
         if (be << 2 >= bf.length) {
            bd = false;
            this.stopMotion();
            return;
         }

         this.l();
      }
   }

   public final void l() {
      this.requestAnimation(bf[be * 4 + 0]);
      this.velocityX = bf[be * 4 + 1] << 8;
      this.velocityY = bf[be * 4 + 2] << 8;
      bg = bf[be * 4 + 3];
      this.W();
      if (bg <= 0) {
         bh = true;
      } else {
         bh = false;
      }

      if (bf[be * 4 + 0] == 22) {
         if (aR == null) {
            aR = new Actor[2];
         }

         if (aR[0] != null) {
            aR[1] = aR[0];
         }

         aR[0] = this.spawnActor(39, 1, 7, !this.facingLeft ? 10 : -10, -48, 302, false);
         aR[0].flags |= 16;
         aR[0].velocityX = aR[0].facingLeft ? -2560 : 2560;
      }
   }

   private boolean ak() {
      if (this.isAnimationEnding()) {
         bb++;
         if (bb >= bc.length) {
            ba = false;
            queuedComboAnimation = -1;
            return false;
         }

         this.requestAnimation(bc[bb]);
         queuedComboAnimation = bc[bb];
      }

      return true;
   }

   // $VF: renamed from: e (boolean) boolean
   private boolean tryReceivePlayerWeaponDamage(boolean var1) {
      if (this.actionState == 3 || this.az > 0 || (16384 & this.flags) != 0 || (1024 & this.flags) != 0) {
         return false;
      }

      if (!this.ab() && !this.e(this.collisionBounds)) {
         return false;
      }

      this.applyDamage(rollWeaponDamage() * weaponDamagePercent(activeWeapon, this.type) / 100, var1);
      return true;
   }

   // $VF: renamed from: al () boolean
   private boolean tryReceivePlayerWeaponDamageWithReaction() {
      return this.tryReceivePlayerWeaponDamage(true);
   }

   // $VF: renamed from: b (int, boolean) void
   private void applyDamage(int var1, boolean var2) {
      this.spawnActor(39, 1, 29, 0, -50, 302, false);
      lastSpawnedActor.aw = 1000;
      lastSpawnedActor.au = var1;
      byte var3 = 100;
      if (bM[activeWeapon] == 3) {
         var3 -= 10;
      }

      if (bN[activeWeapon] == 4) {
         var3 -= 15;
      }

      var1 = var1 * var3 / 100;
      this.health -= var1;
      if (this.type == 14 && this.animationId != 2 && this.animationId != 3) {
         this.spawnActor(18, 14, 6, 0, -Game.randomRange(10, 50), 302, Game.random.nextInt() % 2 == 0);
         lastSpawnedActor.actionState = 3;
      } else {
         this.spawnActor(39, 1, this.am(), 0, -Game.randomRange(10, 50), 302, true);
      }

      if (this.health > 0) {
         if (var2) {
            this.changeActionStateInternal(2, true);
         }

         this.az = 200;
      } else {
         if (this.type != 14 || this.actionState == 107 || this.animationId != 2 && this.animationId != 3) {
            this.changeActionState(3);
         } else {
            this.changeActionState(107);
         }

         if (this.type == 2 || this.type == 3) {
            this.n(0, 0);
         }
      }

      Game.playSoundOnce(-1);
   }

   private int am() {
      return 2;
   }

   // $VF: renamed from: p (int) void
   private void applyDamageWithReaction(int var1) {
      this.applyDamage(var1, true);
   }

   // $VF: renamed from: m () void
   public static final void resetWorldActorState() {
      cB = 0;
      bF = false;
      weaponSwitchCooldownMs = 0;
      playerPlatform = null;
      lastSpawnedActor = null;
      aH = false;
      aI = false;
      queuedComboAnimation = -1;
      playerInvulnerabilityMs = 0;
      Game.aw = 7;
      Game.ax = 34;
      Game.ay = 0;
      Game.player = null;
      activeBoss = null;
      bX = 0;
      j();
      cu = false;
      cw = null;
      spawnRecordCount = 0;
      Game.cj = 0;

      for (int var0 = 0; var0 < 3; var0++) {
         Game.ck[var0] = -1;
      }

      Game.shakeXTicks = 0;
      Game.shakeYTicks = 0;
   }

   public Actor(short[] var1, int var2, int var3) {
      this.initializeFromRecord(var1, true, var2, var3);
   }

   // $VF: renamed from: a (short[], boolean, int, int) void
   public final void initializeFromRecord(short[] var1, boolean var2, int var3, int var4) {
      if (var2) {
         this.uid = var1[1];
      }

      this.positionX = var1[2] & '\uffff';
      this.positionY = var1[3] & '\uffff';
      this.fixedX = this.positionX << 8;
      this.fixedY = this.positionY << 8;
      this.type = var1[0];
      this.facingLeft = (var1[5] & 1) != 0;
      this.flags = var1[5];
      if (var2 && this.isRespawnable()) {
         int[] var5;
         (var5 = spawnRecordPool[spawnRecordCount])[0] = var3;
         var5[1] = var4;
         spawnRecordCount++;
         spawnRecordsByUid.put(new Integer(this.uid), var5);
      }

      if (spriteIdByType[this.type] != -1) {
         this.sprite = Game.getSprite(spriteIdByType[this.type]);
      }

      this.setAnimationImmediate(var1[4]);
      if (this.type == 3) {
         this.snapDownToGround();
      }

      if ((this.flags & 8) != 0) {
         if (this.customBounds == null) {
            this.customBounds = new int[4];
         }

         this.customBounds[0] = var1[6] + this.positionX;
         this.customBounds[1] = var1[7] + this.positionY;
         this.customBounds[2] = this.customBounds[0] + var1[8];
         this.customBounds[3] = this.customBounds[1] + var1[9];
      }

      label276:
      switch (this.type) {
         case 0:
            this.paintPriority = 101;
            Game.health = Game.maxHealth;
            Game.magic = Game.maxMagic;
            break;
         case 1:
            this.ay = var1[10];
            this.variantData = cE[this.ay < 4 ? this.ay : this.ay - 4];
            if (this.ay >= 4) {
               this.flags |= 128;
            }

            this.statSet = var1[11];
            this.health = this.getStat(0);
            this.di = this.getStat(2);
            if (this.di != 0 && (this.flags & 128) != 0) {
               this.beginRespawnFade();
            }

            this.interactionBounds = new int[4];
            this.aB = var1[12];
            this.paintPriority = 81;
            if (this.ay < 4) {
               this.O = true;
            }
            break;
         case 2:
            this.variantData = cE[var1[10]];
            this.statSet = var1[11];
            this.health = this.getStat(0);
            this.di = this.getStat(2);
            if (this.di != 0 && (this.flags & 128) != 0) {
               this.beginRespawnFade();
            }

            this.ad = this.positionY;
            this.interactionBounds = new int[4];
            this.paintPriority = 81;
            this.dj = !this.facingLeft;
            this.requestAnimation(var1[4]);
            this.aB = var1[12];
            if (this.getBehaviorVariant() == 3 || this.getBehaviorVariant() == 2) {
               this.requestAnimation(1);
            }

            if (3 != Game.levelModes[Game.levelIndex] && this.getBehaviorVariant() != 5 && this.getBehaviorVariant() != 6) {
               this.O = true;
            }
            break;
         case 3:
            this.variantData = cE[var1[10]];
            this.statSet = var1[11];
            this.health = this.getStat(0);
            this.di = this.getStat(2);
            if (this.di != 0 && (this.flags & 128) != 0) {
               this.beginRespawnFade();
            }

            this.interactionBounds = new int[4];
            this.paintPriority = 2;
            this.aB = var1[12];
            this.dj = !this.facingLeft;
            if (3 != Game.levelModes[Game.levelIndex]) {
               this.O = true;
            }

            switch (this.getBehaviorVariant()) {
               case 0:
                  this.aG = 0;
                  break;
               case 1:
                  this.aG = 0;
            }
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 11:
         case 12:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 21:
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
         case 38:
         case 39:
         case 41:
         case 42:
         case 43:
         case 44:
         case 46:
         case 47:
         case 51:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 60:
         case 62:
         case 63:
         case 65:
         case 66:
         case 67:
         case 71:
         case 76:
         case 77:
         case 81:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 91:
         case 92:
         case 93:
         case 94:
         case 96:
         case 97:
         case 98:
         case 99:
         default:
            break;
         case 10:
            this.variantData = cE[var1[10]];
            this.statSet = var1[11];
            this.au = this.positionX + var1[12];
            this.ar = this.positionY + var1[13];
            this.ac = this.au;
            this.ad = this.ar;
            this.paintPriority = 81;
            if (var1[4] == 0) {
               this.aF = 8;
            } else if (var1[4] == 8) {
               this.aF = 4;
            } else if (var1[4] == 4) {
               this.aF = this.facingLeft ? 2 : 1;
            }
            break;
         case 13:
            this.variantData = cE[var1[10]];
            this.statSet = var1[11];
            this.health = this.getStat(0);
            this.paintPriority = 102;
            if (this.getBehaviorVariant() == 1 || this.getBehaviorVariant() == 3) {
               this.aG = 0;
            } else if (this.getBehaviorVariant() == 2) {
               this.aG = 0;
            }
            break;
         case 14:
            this.variantData = cE[var1[10]];
            this.statSet = var1[11];
            this.health = this.getStat(0);
            this.di = this.getStat(2);
            this.interactionBounds = new int[4];
            this.paintPriority = 102;
            this.O = true;
            if (this.animationId == 2) {
               this.actionState = 104;
            } else if (this.di != 0 && (this.flags & 128) != 0) {
               this.beginRespawnFade();
            }
            break;
         case 20:
            this.paintPriority = 300;
            this.flags |= 128;
            this.ax = var1[11];
            this.aw = var1[10];
            break;
         case 22:
            this.flags |= 16;
            this.aw = var1[10];
            this.ax = var1[11];
            break;
         case 28:
            this.paintPriority = 102;
            this.flags |= 128;
            break;
         case 31:
            this.refreshBounds();
            this.variantData = this.animationId != 4 && this.animationId != 14 ? 5 : 4;
            this.dh = 20 * this.variantData;
            this.ay = var1[6];
            this.av = this.positionY;
            this.as = this.positionX;
            if (var1[6] == 9) {
               this.actionState = 104;
               this.av = this.av + (this.collisionBounds[3] - this.collisionBounds[1]);
            }

            if (this.customBounds == null) {
               this.customBounds = new int[4];
            }

            this.customBounds[0] = this.collisionBounds[0];
            this.customBounds[1] = this.collisionBounds[1];
            this.customBounds[2] = this.collisionBounds[2];
            this.customBounds[3] = this.collisionBounds[3];
            this.B = var1[7];
            this.C = var1[8];
            this.setCollisionRectEmpty(this.customBounds, false);
            break;
         case 33:
            this.ax = var1[10];
            this.aw = this.as = var1[12];
            this.au = var1[11] << 8;
            this.paintPriority = 3;
            break;
         case 35:
            this.paintPriority = 75;
            this.au = 1;
            this.as = -1;
            this.aw = 1;
            break;
         case 37:
            this.ay = var1[6];
            this.dg = var1[7];
            this.dh = var1[8];
            this.di = var1[9];
            this.dj = true;
            this.positionY += 98;
            this.positionY = this.positionY - this.di;
            this.actionState = this.ay == cy ? 101 : 100;
            break;
         case 40:
            this.flags |= 128;
            this.activationRadius = -1;
            this.aw = var1[10];
            this.ax = var1[11];
            this.au = var1[12];
            this.as = -1;
            this.y = var1[13];
            this.z = var1[14];
            this.A = var1[15];
            this.B = var1[16];
            this.C = var1[17];
            this.D = var1[18];
            break;
         case 45:
            this.paintPriority = 2;
            this.activationRadius = 4;
            break;
         case 48:
            this.paintPriority = 99;
            break;
         case 49:
            this.aw = var1[10];
            this.aB = var1[11];
            this.ax = var1[12];
            this.au = var1[13];
            break;
         case 50:
            this.activationRadius = 4;
            this.as = this.positionX;
            this.ar = var1[6];
            this.av = this.positionY + this.ar;
            this.moduleMask = new int[12];
            this.moduleMask[5] = this.moduleMask[4] = var1[7];
            this.moduleMask[0] = this.moduleMask[1] = var1[9];
            this.moduleMask[3] = this.moduleMask[2] = var1[8];
            this.ay = var1[10];
            this.paintPriority = 2;
            if (this.ay == 1) {
               this.actionState = 101;
            }

            this.aw = 0;
            this.moduleMask[6] = this.ax = var1[11];
            this.au = 1;
            this.moduleMask[7] = this.at = this.moduleMask[4];
            this.n();
            break;
         case 52:
            this.activationRadius = 2;
            this.au = var1[6];
            if (this.au <= 1 && this.au >= 0 && this.animationId == 0) {
               this.flags |= 512;
            }

            this.paintPriority = 75;
            break;
         case 53:
            this.activationRadius = 2;
            this.ar = var1[6];
            this.av = this.positionY + this.ar;
            this.aB = var1[7];
            this.n();
            break;
         case 59:
            this.flags |= 128;
            this.aB = var1[10];
            this.aw = var1[11];
            break;
         case 61:
         case 79:
            this.activationRadius = 3;
            this.refreshBounds();
            this.au = this.collisionBounds[2] - this.collisionBounds[0];
            this.as = this.au / 20;
            this.ar = this.collisionBounds[3] - this.collisionBounds[1];
            this.moduleMask = new int[this.as * (this.ar / 20)];
            if (this.type == 61) {
               this.aw = var1[8];
               this.aB = var1[6];
               this.y = var1[7];
            }

            if (this.type == 61) {
               this.paintPriority = 76;
            } else {
               this.paintPriority = -2;
            }
            break;
         case 64:
            this.paintPriority = 5;
            break;
         case 68:
            this.variantData = cE[var1[10]];
            this.statSet = var1[12];
            this.as = var1[11];
            this.aw = var1[13];
            break;
         case 69:
            if (this.animationId == 0) {
               this.paintPriority = 100;
            } else if (this.animationId == 1) {
               this.paintPriority = 102;
            }

            this.flags |= 16;
            break;
         case 70:
            this.variantData = var1[10];
            this.dj = var1[11] == 1;
            this.dh = var1[12] * 20;
            if (this.variantData == 3 && this.dh == 0) {
               this.dh = 80;
            }

            this.statSet = var1[13];
            this.health = this.getStat(0);
            this.di = this.getStat(2);
            if (this.di != 0 && (this.flags & 128) != 0) {
               this.beginRespawnFade();
            }

            this.paintPriority = 81;
            switch (this.variantData) {
               case 0:
               default:
                  break label276;
               case 1:
                  this.setAnimationImmediate(0);
                  this.positionY = (this.positionY + 10) / 20 * 20;
                  break label276;
               case 2:
               case 3:
                  this.setAnimationImmediate(9);
                  this.positionY = this.positionY / 20 * 20;

                  while (Game.getCollisionTile(this.positionX / 20, this.positionY / 20) >= 15) {
                     this.positionY += 20;
                  }

                  while (true) {
                     if (Game.getCollisionTile(this.positionX / 20, this.positionY / 20 - 1) >= 15) {
                        break label276;
                     }

                     this.positionY -= 20;
                  }
            }
         case 72:
            this.aB = var1[6];
            this.N = true;
            this.refreshBounds();
            this.ar = this.positionY;
            this.av = this.positionY + this.collisionBounds[3] - this.collisionBounds[1];
            break;
         case 73:
            this.variantData = var1[10];
            this.at = var1[11] << 8;
            this.N = true;
            this.av = 0;
            this.paintPriority = 100;
            break;
         case 74:
            this.variantData = var1[12];
            this.at = var1[13] << 8;
            this.aF = var1[10];
            this.ax = var1[11];
            this.aB = var1[14];
            if (this.aB == -2) {
               this.aB = -1;
               this.ay = 1;
            }

            this.ar = var1[15];
            break;
         case 75:
            this.variantData = var1[6];
            break;
         case 78:
            this.paintPriority = 320;
            this.activationRadius = 14;
            this.aB = var1[12];
            this.dg = var1[11];
            break;
         case 80:
            this.ar = var1[6];
            this.customBounds = null;
            this.refreshBounds();
            if (this.customBounds == null) {
               this.customBounds = new int[4];
            }

            this.customBounds[0] = this.collisionBounds[0];
            this.customBounds[1] = this.collisionBounds[1];
            this.customBounds[2] = this.collisionBounds[2];
            this.customBounds[3] = this.collisionBounds[3] + this.ar;
            this.flags |= 128;
            break;
         case 82:
            this.N = true;
            this.paintPriority = 104;
            this.aw = var1[6];
            if (2 == this.aw) {
               this.refreshBounds();
               if (this.customBounds == null) {
                  this.customBounds = new int[4];
               }

               this.customBounds[0] = this.collisionBounds[0] + -40;
               this.customBounds[1] = this.collisionBounds[1] + -40;
               this.customBounds[2] = this.collisionBounds[2] + 40;
               this.customBounds[3] = this.collisionBounds[3] + -10;
               cq[2] = this;
            } else if (this.aw >= 7) {
               this.flags |= 128;
            } else if (this.aw == 1) {
               cq[0] = this;
            } else if (this.aw == 3) {
               cq[1] = this;
            }
            break;
         case 89:
            this.ay = var1[6];
            this.variantData = this.animationId == 25 ? 0 : 1;
            this.au = var1[7];
            this.as = var1[8];
            break;
         case 90:
            this.variantData = cE[var1[10]];
            this.statSet = var1[14];
            this.health = this.getStat(0);
            this.au = var1[11];
            this.as = var1[12];
            this.at = var1[13];
            this.interactionBounds = new int[4];
            this.O = true;
            this.P = true;
            break;
         case 95:
            this.setAnimationImmediate(0);
            this.ar = var1[4];
            this.av = this.ar % 2 == 0 ? this.positionY : this.positionX;
            this.au = var1[10];
            break;
         case 100:
            this.statSet = var1[10];
            this.health = this.getStat(0);
            this.O = true;
            this.P = true;
            this.paintPriority = 50;
      }

      this.refreshBounds();
   }

   public final void n() {
      this.refreshBounds();
      if (this.customBounds == null) {
         this.customBounds = new int[4];
      }

      this.customBounds[0] = this.collisionBounds[0] - 120;
      this.customBounds[1] = this.av;
      this.customBounds[2] = this.collisionBounds[2] + 120;
      this.customBounds[3] = this.collisionBounds[3];
   }

   // $VF: renamed from: an () void
   private void snapDownToGround() {
      while (true) {
         this.velocityY = 256;
         this.resolveTileCollisions(false, true);
         this.velocityY = 0;
         if (this.grounded) {
            return;
         }

         this.positionY += 20;
      }
   }

   // $VF: renamed from: a (short[]) void
   public final void finalizeRecordCollision(short[] var1) {
      if (this.type == 61 || this.type == 79) {
         this.setCollisionRectEmpty(this.collisionBounds, false);
      }
   }

   // $VF: renamed from: b (int, int) void
   final void applyScriptProperty(int var1, int var2) {
      switch (var1) {
         case 0:
            if (var2 != 0) {
               this.paintPriority += var2;
               return;
            }
            break;
         case 1:
            if (var2 == 1) {
               this.flags &= -33;
               return;
            }

            if (var2 == 0) {
               this.flags |= 32;
               return;
            }

            if (var2 == 2) {
               this.flags |= 16;
               return;
            }
            break;
         case 2:
            Game.removeActor(this);
      }
   }

   public final void a(int var1, int var2, int var3, int var4) {
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
         graphics.setColor(16711680);
         Game.setClip(0, 0, 240, 320);
         Game.fillRect(var9, var11 + 0, var10, var12);
         Game.sprites[48].currentPalette = 1;
         if (Game.state != 14) {
            this.at++;
         }

         int var13 = maxInt(var5, var9 - absInt(var5) % 40);
         int var14 = (this.at >> 1) % Game.sprites[48].getAnimationFrameCount(4);

         for (int var15 = var13; var15 < var9 + var10; var15 += 40) {
            if (var15 + 40 > var9 + var10) {
               Game.setClip(var15, 0, var9 + var10 - var15, 320);
            }

            Game.sprites[48].drawAnimationFrame(graphics, 4, var14, var15 + 20, var2, this.flags, 0, Game.cameraY);
            if (var15 + 40 > var9 + var10) {
               Game.setClip(0, 0, 240, 320);
            }
         }

         Game.setClip(0, 0, 240, 320);
      }
   }

   public final void b(int var1, int var2, int var3, int var4) {
      this.refreshBounds();
      graphics.setColor(8947848);
      Game.drawLine(var1 - Game.cameraX - 1, var2 - Game.cameraY, var3 - Game.cameraX - 1, var4 - Game.cameraY);
      Game.drawLine(var1 - Game.cameraX + 1, var2 - Game.cameraY, var3 - Game.cameraX + 1, var4 - Game.cameraY);
      graphics.setColor(16777215);
      Game.drawLine(var1 - Game.cameraX, var2 - Game.cameraY, var3 - Game.cameraX, var4 - Game.cameraY);
   }

   public final void a(int var1, int var2, int var3, Actor var4) {
      switch (var1) {
         case 4:
         case 42:
            if (Game.player.animationId == 9 || 3 == Game.levelModes[Game.levelIndex] && bF || Game.bc >= 6 && Game.bc <= 8) {
               return;
            }

            boolean var5;
            if (!(var5 = (this.animationId != 8 || aH) && damagePlayer(var2)) && (var4.type != 10 || var4.getStat(2) != 0 || var4.aF < 4)) {
               break;
            }

            if (var5) {
               Game.playSoundOnce(-1);
            }

            if (this.actionState == 0 || this.actionState == 2) {
               this.setAnimationImmediate(8);
               e(Game.bo);
            }

            if (var5) {
               this.spawnActor(39, 1, this.am(), 0, -Game.randomRange(10, 50), 302, Game.random.nextInt() % 2 == 0);
            }

            boolean var6 = false;
            this.facingLeft = var4.positionX < this.positionX;
            if (var4.type == 10 && var4.getBehaviorVariant() == 0 && var4.aF < 8) {
               if (var4.aF == 1) {
                  this.facingLeft = var4.positionX + 15 < this.positionX;
               } else {
                  this.facingLeft = var4.positionX - 15 < this.positionX;
               }
            }

            if (var3 == 0) {
               this.velocityX = this.facingLeft ? 3072 : -3072;
            } else {
               this.velocityX = this.facingLeft ? -var3 : var3;
            }

            if (this.isWallBehind()) {
               this.velocityX = 0;
            }

            this.accelerationX = 1024 * (this.velocityX > 0 ? -1 : 1);
            return;
         case 6:
            this.requestAnimation(var2);
            this.positionX = var3;
            this.positionY = var4.positionY;
            this.velocityX = this.velocityY = this.accelerationY = 0;
      }
   }

   public final boolean o() {
      if (3 != Game.levelModes[Game.levelIndex] || !bF) {
         return false;
      }

      if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
         if (this.type == 16) {
            this.changeActionState(105);
         } else {
            this.changeActionState(3);
         }

         return true;
      } else {
         return false;
      }
   }

   // $VF: renamed from: a (int, int, int, int, int, int, boolean) b
   public final Actor spawnActor(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      this.createActor(var1, var2, var3, var4, var5, var6, var7);
      Game.addActor(lastSpawnedActor);
      return lastSpawnedActor;
   }

   // $VF: renamed from: b (int, int, int, int, int, int, boolean) void
   public final void createActor(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      lastSpawnedActor = new Actor();
      lastSpawnedActor.uid = -1;
      lastSpawnedActor.aB = -1;
      lastSpawnedActor.cameraDistanceBucket = 1;
      lastSpawnedActor.type = var1;
      lastSpawnedActor.sprite = Game.getSprite(var2);
      lastSpawnedActor.setAnimationImmediate(var3);
      if (var1 == 35) {
         lastSpawnedActor.au = 6000;
         lastSpawnedActor.as = 0;
         if (var7) {
            lastSpawnedActor.actionState = 105;
            lastSpawnedActor.az = 500;
            lastSpawnedActor.velocityX = Game.randomRange(2, 4) << 8;
            if (Game.randomRange(0, 2) == 0) {
               lastSpawnedActor.velocityX = -lastSpawnedActor.velocityX;
            }

            lastSpawnedActor.velocityY = Game.randomRange(2, 4) << 8;
            if (Game.randomRange(0, 2) == 0) {
               lastSpawnedActor.velocityY = -lastSpawnedActor.velocityY;
            }
         }
      }

      lastSpawnedActor.paintPriority = var6;
      lastSpawnedActor.positionX = this.positionX + var4;
      lastSpawnedActor.positionY = this.positionY + var5;
      lastSpawnedActor.facingLeft = var7 ? !this.facingLeft : this.facingLeft;
      if (lastSpawnedActor.facingLeft) {
         lastSpawnedActor.flags |= 1;
      } else {
         lastSpawnedActor.flags &= -2;
      }

      lastSpawnedActor.refreshBounds();
   }

   // $VF: renamed from: p () void
   public static final void serializeProgress() {
      Game.writeByte(Game.saveBuffer, 14, Game.levelIndex);
      if (Game.readSignedByte(Game.saveBuffer, 15) < Game.maxUnlockedLevel) {
         Game.writeByte(Game.saveBuffer, 15, Game.maxUnlockedLevel);
      }

      Game.writeI16LE(Game.saveBuffer, 26, (short)activeWeapon);
      Game.writeI16LE(Game.saveBuffer, 24, (short)(Game.levelCompleted ? 1 : 0));

      for (int var0 = 0; var0 < 2; var0++) {
         Game.writeI16LE(Game.saveBuffer, 28 + 2 * var0, (short)bM[var0]);
         Game.writeI16LE(Game.saveBuffer, 32 + 2 * var0, (short)bN[var0]);
         Game.writeI16LE(Game.saveBuffer, 36 + 2 * var0, (short)bO[var0]);
         Game.writeI16LE(Game.saveBuffer, 40 + 2 * var0, (short)bP[var0]);
      }

      Game.writeI16LE(Game.saveBuffer, 60, (short)Game.maxHealth);
      Game.writeI16LE(Game.saveBuffer, 62, (short)Game.health);
      Game.writeI16LE(Game.saveBuffer, 64, (short)Game.r);
      Game.writeI16LE(Game.saveBuffer, 66, (short)Game.maxMagic);
      Game.writeI16LE(Game.saveBuffer, 68, (short)Game.magic);
      Game.writeI16LE(Game.saveBuffer, 70, (short)Game.u);
      Game.writeI32LE(Game.saveBuffer, 72, Game.v);
      Game.writeByte(Game.saveBuffer, 653, (byte)(bl ? 1 : 0));
      Game.writeByte(Game.saveBuffer, 654, (byte)(bm ? 1 : 0));
      Game.writeByte(Game.saveBuffer, 655, (byte)(bn ? 1 : 0));
      Game.writeByte(Game.saveBuffer, 656, (byte)(bo ? 1 : 0));
      Game.writeByte(Game.saveBuffer, 657, (byte)(bp ? 1 : 0));
      Game.writeByte(Game.saveBuffer, 658, (byte)(bq ? 1 : 0));
      Game.writeByte(Game.saveBuffer, 659, (byte)(br ? 1 : 0));
      Game.writeByte(Game.saveBuffer, 660, (byte)(bs ? 1 : 0));
      Game.writeByte(Game.saveBuffer, 661, (byte)(bt ? 1 : 0));
      Game.writeByte(Game.saveBuffer, 662, (byte)(bu ? 1 : 0));
      Game.writeByte(Game.saveBuffer, 663, (byte)(bw ? 1 : 0));
      serializeRemovedActorUids();
   }

   // $VF: renamed from: q () void
   public static final void serializeRemovedActorUids() {
      Game.bJ[Game.levelIndex] = (short)Game.bI;

      for (int var0 = 0; var0 < 9; var0++) {
         Game.writeI16LE(Game.saveBuffer, 86 + 2 * var0, Game.bJ[var0]);
      }

      for (int var2 = 0; var2 < 9; var2++) {
         for (int var1 = 0; var1 < 30; var1++) {
            Game.writeI16LE(Game.saveBuffer, 104 + var2 * 30 * 2 + var1 * 2, Game.bG[var2][var1]);
         }
      }
   }

   // $VF: renamed from: ao () void
   private void updateCheckpoint() {
      if (rectsOverlapInclusive(this.collisionBounds, Game.player.collisionBounds)) {
         Game.skipCheckpointPosition = false;
         Game.writeI16LE(Game.saveBuffer, 16, (short)(this.uid + 1));
         Game.writeI16LE(Game.saveBuffer, 18, (short)Game.player.positionX);
         Game.writeI16LE(Game.saveBuffer, 20, (short)Game.player.positionY);
         Game.writeI16LE(Game.saveBuffer, 22, (short)(Game.player.facingLeft ? 1 : 0));
         serializeProgress();
         Game.removeActor(this);

         for (int var1 = 0; var1 < Game.bp; var1++) {
            Game.writeByte(Game.saveBuffer, 999 + var1, Game.bD[var1]);
         }

         if (Game.levelIndex == 5) {
            for (int var2 = 0; var2 < 3; var2++) {
               if (cq[var2] != null) {
                  bx[var2] = (short)cq[var2].uid;
               } else {
                  bx[var2] = -1;
               }

               Game.writeI16LE(Game.saveBuffer, 664 + var2 * 2, bx[var2]);
               if (bx[var2] != -1) {
                  Game.storeLinkedActorSnapshot(cq[var2], var2);
               }
            }

            for (int var3 = 0; var3 < 54; var3++) {
               Game.writeByte(Game.saveBuffer, 677 + var3, by[var3]);
            }

            Game.writeI16LE(Game.saveBuffer, 731, (short)Game.activeCameraControllerUid);
            Game.writeI16LE(Game.saveBuffer, 733, (short)Game.cameraY);
            Game.writeI16LE(Game.saveBuffer, 735, (short)Game.hazardCameraOffsetY);
         } else if (Game.levelIndex == 3) {
            Game.writeByte(Game.saveBuffer, 644, (byte)(bC ? 1 : 0));
            Game.writeI16LE(Game.saveBuffer, 645, (short)Game.bm.positionX);
            Game.writeI16LE(Game.saveBuffer, 647, (short)Game.bm.positionY);
            Game.writeI32LE(Game.saveBuffer, 649, Game.bm.actionState);
         }

         Game.accessSaveRecord(true);
      }
   }

   // $VF: renamed from: r () boolean
   public final boolean isUpdatePaused() {
      boolean var1 = false;
      if (Game.state != 14 && Game.state != 32) {
         if (Game.aS) {
            switch (Game.bc) {
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
                  if (this.type != 0 && this.type != 48 && this.type != 39 && this.type != 22) {
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
                  if (this == Game.player || this == Game.bo || this == activeBoss) {
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

   // $VF: renamed from: s () void
   public final void update() {
      if (!this.isUpdatePaused()) {
         if (playerPlatform == this) {
            Game.player.positionX = Game.player.positionX + (this.velocityX >> 8);
            Game.player.positionY = Game.player.positionY + (this.velocityY >> 8);
         }

         if ((this.flags & 1024) == 0 && (this.flags & 16384) == 0) {
            this.updatedThisTick = true;
            this.fixedX = this.fixedX + (this.positionX - (this.fixedX >> 8) << 8);
            this.fixedY = this.fixedY + (this.positionY - (this.fixedY >> 8) << 8);
            if (this.scrollWithWorld) {
               bz = this.velocityX + (worldScrollPixelsPerTick << 8);
            } else {
               bz = this.velocityX;
            }

            this.fixedX = this.fixedX + bz;
            this.fixedY = this.fixedY + this.velocityY;
            this.velocityX = this.velocityX + this.accelerationX;
            this.velocityY = this.velocityY + this.accelerationY;
            this.accelerationX = 0;
            this.accelerationY = 0;
            this.positionX = this.fixedX >> 8;
            this.positionY = this.fixedY >> 8;
            if (this.az >= 0) {
               this.az = this.az - Game.tickDeltaMs;
            }

            switch (this.type) {
               case -102:
                  this.updateBossOrbitProjectile();
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
               case -53:
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
               case -41:
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
               case 4:
               case 5:
               case 6:
               case 7:
               case 8:
               case 9:
               case 11:
               case 12:
               case 15:
               case 17:
               case 24:
               case 25:
               case 26:
               case 27:
               case 29:
               case 30:
               case 32:
               case 34:
               case 36:
               case 38:
               case 41:
               case 42:
               case 43:
               case 44:
               case 46:
               case 47:
               case 51:
               case 53:
               case 54:
               case 55:
               case 56:
               case 57:
               case 58:
               case 60:
               case 62:
               case 63:
               case 65:
               case 66:
               case 67:
               case 71:
               case 76:
               case 77:
               case 81:
               case 83:
               case 84:
               case 85:
               case 86:
               case 87:
               case 88:
               case 91:
               case 92:
               case 93:
               case 94:
               case 96:
               case 97:
               case 99:
               default:
                  break;
               case -100:
                  this.updateDeceleratingProjectile();
                  break;
               case -40:
                  this.updateExplosionHitbox();
                  break;
               case -37:
                  this.updateHitProjectile();
                  break;
               case 0:
                  this.updatePlayer();
                  if (Game.aS && Game.bc != 16) {
                     this.stopMotion();
                  }
                  break;
               case 1:
                  this.updateEnemyType1();
                  break;
               case 2:
                  this.updateEnemyType2();
                  break;
               case 3:
                  this.updateEnemyType3();
                  break;
               case 10:
                  this.updateBeamEmitter();
                  break;
               case 13:
                  this.updateEnemyType13();
                  break;
               case 14:
                  this.updateEnemyType14();
                  break;
               case 16:
                  this.updateThrownObject();
                  break;
               case 18:
                  this.updateHomingProjectile();
                  break;
               case 19:
                  this.updateStraightProjectile();
                  break;
               case 20:
                  this.updateTrigger();
                  break;
               case 21:
                  this.updatePrompt();
                  break;
               case 22:
                  this.updateCameraRegion();
                  break;
               case 23:
                  this.updateCheckpoint();
                  break;
               case 28:
                  this.followPlayer();
                  break;
               case 31:
                  this.updateLinkedGate();
                  break;
               case 33:
                  this.updateMovingPlatform();
                  break;
               case 35:
                  this.updatePickup();
                  break;
               case 37:
                  this.updateGate37();
                  break;
               case 39:
                  this.updateEffect();
                  break;
               case 40:
                  this.updateLinkedTrigger();
                  break;
               case 45:
                  this.updateBreakableBridge();
                  break;
               case 48:
                  this.updateAbilityPickup();
                  break;
               case 49:
                  this.updateCameraController();
                  break;
               case 50:
                  this.updateSwingingPlatform();
                  break;
               case 52:
                  this.updateContainer();
                  break;
               case 59:
                  this.bt();
                  break;
               case 61:
                  if (this.animationId < 12) {
                     this.updateDestructible61();
                  } else if (this.animationId >= 15 && this.animationId <= 17) {
                     this.decrementTimerA();
                     this.l(0);
                     if (this.aw < 0) {
                        this.requestAnimation(0);
                     }
                  }
                  break;
               case 64:
                  this.updateSpringPlatform();
                  break;
               case 68:
                  this.updateWaveSpawner();
                  break;
               case 69:
                  this.updatePlayerScrollAttachment();
                  break;
               case 70:
                  this.updateEnemyType70();
                  break;
               case 72:
                  this.updateLinkedPlatform();
                  break;
               case 73:
                  this.updateHazardType73();
                  break;
               case 74:
                  this.updateHazardSpawner();
                  break;
               case 75:
                  this.updateTrap75();
                  break;
               case 78:
                  this.updateHazardArea78();
                  break;
               case 79:
                  this.updateDestructible79();
                  break;
               case 80:
                  this.updateCollapsingPlatform();
                  break;
               case 82:
                  this.updatePlatformGroup();
                  break;
               case 89:
                  this.updateTimedSwitch();
                  break;
               case 90:
                  this.updateBoss90();
                  break;
               case 95:
                  this.updateCrushingHazard();
                  break;
               case 98:
                  this.updateOneShotEffect();
                  break;
               case 100:
                  this.updateBoss100();
            }

            if (this.boundsDirty) {
               this.refreshBounds();
            }

            if (this.facingLeft) {
               this.flags |= 1;
            } else {
               this.flags &= -2;
            }

            if (3 != Game.levelModes[Game.levelIndex] && this != Game.player) {
               this.clampActorToActiveRegion();
            }
         }
      }
   }

   // $VF: renamed from: t () void
   public final void updateCrushingHazard() {
      switch (this.actionState) {
         case 0:
            if (rectsOverlapInclusive(Game.player.collisionBounds, this.customBounds) || rectContainsRect(this.customBounds, Game.player.collisionBounds)) {
               this.O = true;
               this.P = true;
               this.actionState = 101;
               this.aw = 800;
            }
            break;
         case 101:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.actionState = 102;
               this.as = 0;
               switch (this.ar) {
                  case 0:
                     this.velocityY = -2048;
                     break;
                  case 1:
                     this.velocityX = 2048;
                     break;
                  case 2:
                     this.velocityY = 2048;
                     break;
                  case 3:
                     this.velocityX = -2048;
               }
            }
            break;
         case 102:
            this.as += 2048;
            if (this.as >= this.au << 8) {
               switch (this.ar) {
                  case 0:
                     Game.startVerticalShake(12);
                     this.velocityY = 768;
                     break;
                  case 1:
                     Game.startCameraShake(12, 0);
                     this.velocityX = -768;
                     break;
                  case 2:
                     Game.startVerticalShake(12);
                     this.velocityY = -768;
                     break;
                  case 3:
                     Game.startCameraShake(12, 0);
                     this.velocityX = 768;
               }

               this.actionState = 103;
            }
            break;
         case 103:
            switch (this.ar) {
               case 0:
                  if (this.positionY >= this.av) {
                     this.positionY = this.av;
                     this.velocityY = 0;
                     this.actionState = 0;
                  }
                  break;
               case 1:
                  if (this.positionX <= this.av) {
                     this.positionX = this.av;
                     this.velocityX = 0;
                     this.actionState = 0;
                  }
                  break;
               case 2:
                  if (this.positionY <= this.av) {
                     this.positionY = this.av;
                     this.velocityY = 0;
                     this.actionState = 0;
                  }
                  break;
               case 3:
                  if (this.positionX >= this.av) {
                     this.positionX = this.av;
                     this.velocityX = 0;
                     this.actionState = 0;
                  }
            }
      }

      if (rectsOverlapInclusive(this.collisionBounds, Game.player.collisionBounds)) {
         Game.player.a(4, 10, 0, this);
      }

      for (int var1 = 0; var1 < Game.updateActorCount; var1++) {
         Actor var2;
         if ((var2 = Game.updateActors[var1]) != null
            && (var2.flags & 1024) == 0
            && var2.actionState != 3
            && var2.actionState != 7
            && var2.actionState != 5
            && var2.actionState != 6
            && (
               var2.isDamageable() && var2.collisionBounds != null && rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds)
                  || var2.type == 70 && rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds)
            )) {
            var2.applyDamage(10, true);
         }
      }
   }

   // $VF: renamed from: u () boolean
   public final boolean paint() {
      if (Game.player.actionState == 1 && Game.bl == this) {
         return true;
      }

      Game.cameraX -= 0;
      Game.cameraY -= 0;
      if (this.type == -2 && this.sprite == Game.sprites[1] && (this.animationId == 2 || this.animationId == 3 || this.animationId == 28)) {
         this.sprite.currentPalette = Game.bloodCensored ? 1 : 0;
      }

      if (this.type == 80 && (this.flags & 128) == 0) {
         Game.b(this.positionY - Game.cameraY, this.ar, this.positionX - Game.cameraX);
         this.positionY = this.positionY + this.ar;
         this.drawAtCamera(Game.cameraX, Game.cameraY);
         this.positionY = this.positionY - this.ar;
      }

      if (this.type == 0) {
         if (playerInvulnerabilityMs > 0) {
            playerInvulnerabilityMs = playerInvulnerabilityMs - Game.tickDeltaMs;
         }
      } else if (this.sprite == Game.sprites[16]) {
         if (this.animationId == 1) {
            this.refreshBounds();
            if (this.attackBounds != null) {
               Game.graphics.setColor(16777215);
               Game.fillRect(
                  this.attackBounds[0] - Game.cameraX,
                  this.attackBounds[1] - Game.cameraY,
                  this.attackBounds[2] - this.attackBounds[0],
                  this.attackBounds[3] - this.attackBounds[1]
               );
            }
         }
      } else if (this.type == 39) {
         if (this.animationId == 29) {
            if (this.au >= 100) {
               Game.cameraX += 0;
               Game.cameraY += 0;
               return true;
            }

            int var1 = this.positionX - Game.cameraX;
            Game.font.currentPalette = 0;
            Game.font.drawText(graphics, "-", var1, this.positionY - Game.cameraY, 0);
            if (this.au / 10 != 0) {
               var1 += 5;
               Game.font.drawText(graphics, "" + this.au / 10 + "", var1, this.positionY - Game.cameraY, 0);
            }

            Game.font.drawText(graphics, "" + this.au % 10 + "", var1 + 5, this.positionY - Game.cameraY, 0);
         } else if (this.animationId == 2 || this.animationId == 3 || this.animationId == 28) {
            this.sprite.currentPalette = Game.bloodCensored ? 1 : 0;
         }
      } else if (this.type == 2
         || this.type == 3
         || this.type == 1
         || this.type == 13
         || this.type == 73
         || this.type == 70
         || this.type == 98
         || this.type == 83
         || this.type == 84
         || this.type == 85) {
         this.sprite.currentPalette = this.aG;
      } else if (this.type == 35) {
         if (this.actionState == 100 || this.actionState == 101) {
            Game.cameraX += 0;
            Game.cameraY += 0;
            return true;
         }
      } else if (this.type == 48 && this.animationId == 1) {
         Game.cameraX += 0;
         Game.cameraY += 0;
         return true;
      }

      if (this.type == 78) {
         if ((this.flags & 128) == 0) {
            this.a(
               this.collisionBounds[0],
               this.collisionBounds[1],
               this.collisionBounds[2] - this.collisionBounds[0],
               this.collisionBounds[3] - this.collisionBounds[1]
            );
            Game.cameraX += 0;
            Game.cameraY += 0;
            return true;
         } else {
            Game.cameraX += 0;
            Game.cameraY += 0;
            return false;
         }
      } else if (this.type == 45) {
         if (101 == this.actionState) {
            Game.cameraX += 0;
            Game.cameraY += 0;
            return true;
         }

         int var12 = this.collisionBounds[0];
         int var15 = this.collisionBounds[2] - this.collisionBounds[0];

         for (int var17 = 0; var17 < var15 / 40; var17++) {
            if (this.actionState == 0) {
               Game.sprites[36]
                  .drawAnimationFrame(
                     graphics, this.animationId, 0, this.positionX - Game.cameraX + var17 * 40, this.positionY - Game.cameraY, this.flags, 0, 0
                  );
            } else {
               int var19 = var12 + 40 * var17;
               int var21 = Math.abs(this.au - var19) / 40;
               int var23;
               if ((var23 = this.aw / 83 >> 1) - var21 < 0) {
                  Game.sprites[36]
                     .drawAnimationFrame(
                        graphics, this.animationId, 0, this.positionX - Game.cameraX + var17 * 40, this.positionY - Game.cameraY, this.flags, 0, 0
                     );
               } else {
                  var23 -= var21;
               }

               int var24 = 0;
               if (var23 >= 4) {
                  var24 = 0 + (var23 - 4) * 20 + 100;
                  var23 = (var23 - 4) % 3 + 2;
               }

               Game.sprites[36]
                  .drawAnimationFrame(graphics, 1, var23, this.positionX - Game.cameraX + var17 * 40, this.positionY - Game.cameraY + var24, 0, 0, 0);
            }
         }

         Game.cameraX += 0;
         Game.cameraY += 0;
         return true;
      } else {
         if (this.type == 50) {
            int var8 = this.as - Game.cameraX;
            int var2 = this.positionX - Game.cameraX;
            int var3 = this.positionY - Game.cameraY - 1 + 0;
            int var4 = this.av - Game.cameraY;
            int var5 = this.aw >> 16;
            int var6 = var4 + Math.abs(this.ar >> 1);
            int var7 = (var4 << 8) + Math.abs(this.ar >> 1) * Game.cos256(var5);
            if (this.actionState >= 102) {
               var3 = var6;
               var2 = var8;
            }

            Game.a(var8, var4, var2, var3, var5, this.ay, var7);
            if (this.actionState == 104) {
               Game.cameraX += 0;
               Game.cameraY += 0;
               return true;
            }
         } else if (this.type == 53) {
            int var9 = this.positionX - Game.cameraX + 1;
            int var13 = this.positionX - Game.cameraX + 1;
            int var16 = this.positionY - Game.cameraY;
            int var18;
            int var20;
            int var22 = (var20 = (var18 = this.av - Game.cameraY) + Math.abs(this.ar >> 1)) << 8;
            if (this.actionState >= 101) {
               var16 = var20;
               var13 = var9;
            }

            Game.a(var9, var18, var13, var16, 0, 0, var22);
            if (this.actionState == 102 || this.aB == -2) {
               Game.cameraX += 0;
               Game.cameraY += 0;
               return true;
            }
         } else if (this.type == 31) {
            int var10 = this.ay;
            if (this.ay == 9) {
               var10 = 4;
            }

            int var14 = var10 <= 2 ? this.as : this.av;
            if (var10 == 8) {
               var14 += 20 * this.variantData;
            } else if (var10 == 2) {
               var14 += 20 * this.variantData;
            }

            this.l(var10, var14);
         } else if (this.type == 61) {
            if (this.animationId != 0 && this.animationId != 2 && this.animationId < 15) {
               this.sprite.frameModuleMask = this.moduleMask;
               this.sprite.moduleMaskEnabled = true;
            }
         } else if (this.type == 79) {
            if (this.animationId != 1) {
               this.sprite.frameModuleMask = this.moduleMask;
               this.sprite.moduleMaskEnabled = true;
            }
         } else if (this.type == -37) {
            this.sprite.currentPalette = 1;
         }

         if ((this.flags & 128) == 0) {
            if (this.type == 37) {
               this.l(8, this.positionY + this.di - (this.uid == -1 ? 115 : 98));
            } else if (this.type == 73) {
               this.l(8, this.aA);
            } else if (this.type == 72) {
               this.l(8, this.ar);
            } else if (this.type == 70 && this.variantData == 3 && (this.actionState == 115 || this.actionState == 114)) {
               this.b(this.as, this.av, this.positionX, this.collisionBounds[3] + this.collisionBounds[1] >> 1);
            }

            if (this.sprite == null) {
               Game.cameraX += 0;
               Game.cameraY += 0;
               return false;
            }

            if (this.animationFrame > this.sprite.getAnimationFrameCount(this.animationId)) {
               this.animationFrame = 0;
            }

            if (this.type == 0) {
               if (3 == Game.levelModes[Game.levelIndex] && bF) {
                  Game.a(false, 0);
                  Game.a(false, 1);
                  Game.a(false, 2);
               }

               this.drawAtCamera(Game.cameraX, Game.cameraY);
               if (3 == Game.levelModes[Game.levelIndex] && bF) {
                  Game.a(true, 0);
                  Game.a(true, 1);
                  Game.a(true, 2);
               }

               if (bX > 0) {
                  z();
               }
            } else {
               this.drawAtCamera(Game.cameraX, Game.cameraY);
            }
         }

         if (this.sprite == null) {
            Game.cameraX += 0;
            Game.cameraY += 0;
            return false;
         }

         this.sprite.boundaryClipMask = -1;
         this.sprite.boundaryClipX = -1;
         this.sprite.boundaryClipY = -1;
         this.sprite.moduleMaskEnabled = false;
         if (this.type == 39 && this.animationId >= 11 && this.animationId <= 12) {
            this.ac = this.animationFrame * 30 / this.sprite.getAnimationFrameCount(this.animationId);
            graphics.setColor(this.animationId == 11 ? 65280 : 255);
            Game.drawArc(this.positionX - Game.cameraX - (this.ac >> 1), this.positionY - Game.cameraY - (this.ac >> 1), this.ac, this.ac, 0, 360);
         }

         if (this.type == 100) {
            if (this.actionState == 102) {
               for (int var11 = 0; var11 < 4; var11++) {
                  this.a(aP[var11].positionX, aP[var11].positionY, aQ[var11].positionX, aQ[var11].positionY, 0, de, false);
               }
            }
         } else if (this.type == 10) {
            if (this.aA == 1) {
               this.a(this.positionX, this.positionY, this.ac, this.ad, 0, de, false);
            }
         } else if (this.type == 16) {
            if (this.actionState == 103) {
               Game.font.currentPalette = 1;
               Game.font.drawText(graphics, this.aw / 1000 + 1 + "", this.positionX - Game.cameraX, this.positionY - Game.cameraY + -28, 3);
            } else if (this.actionState == 105) {
               this.au = this.animationFrame * 70 / this.sprite.getAnimationFrameCount(this.animationId);
               graphics.setColor(16776960);
               Game.drawArc(this.positionX - Game.cameraX - (this.au >> 1), this.positionY - Game.cameraY - (this.au >> 1), this.au, this.au, 0, 360);
            }
         } else if (this.type == -40) {
            this.au = this.animationFrame * 70 / this.sprite.getAnimationFrameCount(this.animationId);
            graphics.setColor(16776960);
            Game.drawArc(this.positionX - Game.cameraX - (this.au >> 1), this.positionY - Game.cameraY - (this.au >> 1), this.au, this.au, 0, 360);
         }

         if (this.type == 0 || !Game.aJ) {
            this.refreshBounds();
            if (!this.isUpdatePaused()) {
               this.advanceAnimation();
            }
         }

         Game.cameraX += 0;
         Game.cameraY += 0;
         return true;
      }
   }

   // $VF: renamed from: ap () int
   private int sampleHeadTileLeft() {
      this.refreshBounds();
      int var2 = this.collisionBounds[1];
      int var1;
      return Game.getCollisionTile((var1 = this.positionX / 20) - 1, (var2 - 4) / 20);
   }

   // $VF: renamed from: aq () int
   private int sampleHeadTileRight() {
      this.refreshBounds();
      int var2 = this.collisionBounds[1];
      int var1;
      return Game.getCollisionTile((var1 = this.positionX / 20) + 1, (var2 - 4) / 20);
   }

   // $VF: renamed from: ar () int
   private int probeVerticalTiles() {
      this.refreshBounds();
      int var2 = 0;
      int var3 = this.collisionBounds[1];
      int var4 = this.collisionBounds[3] + 1;
      this.grounded = false;
      this.verticalProbeActive = this.velocityY != 0;
      int var1 = this.positionX / 20;
      this.tileAtHead = Game.getCollisionTile(var1, var3 / 20);
      this.tileBelowHead = Game.getCollisionTile(var1, var3 / 20 + 1);
      this.tileAtFeet = Game.getCollisionTile(var1, var4 / 20);
      this.tileAboveFeet = Game.getCollisionTile(var1, var4 / 20 - 1);
      this.tileBelowFeet = Game.getCollisionTile(var1, var4 / 20 + 1);
      this.tileLeftOfFeet = Game.getCollisionTile(var1 - 1, var4 / 20);
      this.tileRightOfFeet = Game.getCollisionTile(var1 + 1, var4 / 20);
      if (this.type == 82) {
         int var5 = 0;
         int var6 = 0;
         int var7 = 0;
         if (this.tileAtHead <= 15) {
            var5 = Game.getCollisionTile(var1 - 1, var3 / 20);
            var6 = Game.getCollisionTile(var1 + 1, var3 / 20);
            var7 = Game.getCollisionTile(var1 - 2, var3 / 20);
            if (var5 > 15) {
               this.tileAtHead = var5;
            } else if (var6 > 15) {
               this.tileAtHead = var6;
            } else {
               this.tileAtHead = var7;
            }
         }
      }

      if (this.velocityY <= -5120 && this.tileBelowHead > 15 && this.tileAtHead <= 15) {
         this.tileAtHead = this.tileBelowHead;
      }

      if (this.tileAtHead >= 15) {
         this.verticalProbeActive = true;
      }

      if (this.tileAtFeet >= 15
         || this.tileAtFeet == 15
         || this.tileAtFeet == 0 && this.tileAboveFeet >= 24 && var4 - var3 >= 20
         || this.collisionBounds[2] - this.collisionBounds[0] > 20 && this.tileLeftOfFeet >= 15 && this.tileRightOfFeet >= 15) {
         var2 = var4 % 20;
         if (this.tileAboveFeet >= 15) {
            var2 += 20;
            this.tileBelowFeet = this.tileAtFeet;
            this.tileAtFeet = this.tileAboveFeet;
         }

         if (var2 > 0) {
            this.verticalProbeActive = true;
         }

         if (this.tileAtHead >= 24 && this.tileBelowHead >= 24) {
            this.verticalProbeActive = false;
         }

         this.grounded = true;
         if (this.tileAtFeet >= 18 && this.tileAtFeet <= 21) {
            int var9 = this.positionX % 20 >> 1;
            if (this.tileAtFeet == 18) {
               var2 -= var9;
            } else if (this.tileAtFeet == 19) {
               var2 -= var9 + 10;
            } else if (this.tileAtFeet == 20) {
               var2 -= 10 - var9 + 10;
            } else if (this.tileAtFeet == 21) {
               var2 -= 10 - var9;
            }

            this.verticalProbeActive = true;
         } else if (this.tileAtFeet == 16) {
            var2 -= this.positionX % 20;
            this.verticalProbeActive = true;
         } else if (this.tileAtFeet == 17) {
            var2 -= 20 - this.positionX % 20;
            this.verticalProbeActive = true;
         }
      }

      return var2;
   }

   // $VF: renamed from: b (boolean, boolean) void
   public final void resolveTileCollisions(boolean var1, boolean var2) {
      int var3 = this.probeVerticalTiles();
      bA = this.tileAtHead;
      int var6 = this.collisionBounds[0] - 1;
      int var7 = this.collisionBounds[2] + 1;
      int var8 = this.collisionBounds[1];
      int var9;
      if ((var9 = this.collisionBounds[3] - 10) < var8) {
         var9 = var8;
      }

      this.wallOnLeft = false;
      this.wallOnRight = false;
      this.ceilingHit = false;
      this.tileAtLeftSide = 0;
      this.tileAtRightSide = 0;
      if (var2) {
         if (this.verticalProbeActive) {
            if (this.velocityY >= 0 && this.tileAtFeet >= 15) {
               this.positionY -= var3;
            } else if (this.tileAtHead >= 24) {
               this.ceilingHit = true;
               this.positionY += 20 - var8 % 20 + 1;
            }
         }

         this.refreshBounds();
         var6 = this.collisionBounds[0] - 1;
         var7 = this.collisionBounds[2] + 1;
         var8 = this.collisionBounds[1];
         var9 = this.collisionBounds[3] - 10;
      }

      if (var1) {
         int var10 = Game.getCollisionTile(var6 / 20 + 1, var9 / 20);
         int var11 = Game.getCollisionTile(var7 / 20 - 1, var9 / 20);

         for (int var4 = var8 / 20; var4 <= var9 / 20; var4++) {
            if (var6 < 0 && Game.levelModes[Game.levelIndex] != 2) {
               this.tileAtLeftSide = 24;
               this.wallOnLeft = true;
               break;
            }

            int var5;
            if ((var5 = Game.getCollisionTile(var6 / 20, var4)) > this.tileAtLeftSide) {
               this.tileAtLeftSide = var5;
               if (this.tileAtLeftSide >= 24) {
                  this.wallOnLeft = true;
                  if (var4 == var9 / 20 && (var10 == 16 || var10 == 18 || var10 == 18)) {
                     this.wallOnLeft = false;
                  }
               }
            }

            if ((var5 = Game.getCollisionTile(var7 / 20, var4)) > this.tileAtRightSide) {
               this.tileAtRightSide = var5;
               if (this.tileAtRightSide >= 24) {
                  this.wallOnRight = true;
                  if (var4 == var9 / 20 && (var11 == 17 || var11 == 20 || var11 == 21)) {
                     this.wallOnRight = false;
                  }
               }
            }

            if (this.wallOnLeft || this.wallOnRight) {
               break;
            }
         }

         this.refreshBounds();
         if (var1) {
            if (this.wallOnLeft == this.wallOnRight) {
               this.wallOnLeft = this.wallOnRight = false;
            } else if (this.velocityX <= 0) {
               if (this.wallOnLeft) {
                  this.positionX += 20 - (var6 + 20) % 20 - 1;
                  this.probeVerticalTiles();
               } else {
                  this.positionX -= var7 % 20;
                  this.probeVerticalTiles();
               }
            } else if (this.wallOnRight) {
               this.positionX -= var7 % 20;
               this.probeVerticalTiles();
            } else {
               this.positionX += 20 - (var6 + 20) % 20 - 1;
               this.probeVerticalTiles();
            }
         }

         this.refreshBounds();
         this.boundsCenterX = this.collisionBounds[0] + this.collisionBounds[2] >> 1;
         this.boundsCenterY = this.collisionBounds[1] + this.collisionBounds[3] >> 1;
      }
   }

   // $VF: renamed from: as () boolean
   private boolean isWallBehind() {
      return this.facingLeft ? this.wallOnRight : this.wallOnLeft;
   }

   private static boolean q(int var0) {
      return false;
   }

   // $VF: renamed from: at () boolean
   private static boolean isPlayerMeleeAnimation() {
      switch (Game.player.animationId) {
         case 13:
         case 14:
         case 15:
         case 19:
         case 20:
         case 21:
            return true;
         case 16:
         case 17:
         case 18:
         default:
            return false;
      }
   }

   // $VF: renamed from: au () boolean
   private static boolean isPlayerSpecialAnimation() {
      switch (Game.player.animationId) {
         case 16:
         case 18:
         case 22:
         case 23:
            return true;
         case 17:
         case 19:
         case 20:
         case 21:
         default:
            return false;
      }
   }

   // $VF: renamed from: av () void
   private void updatePlayerScrollAttachment() {
      switch (this.actionState) {
         case 0:
            if (this.animationId == 1) {
               Game.bm = this;
            } else {
               Game.bn = this;
            }

            if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds) && absInt(this.positionX - Game.player.positionX) <= 40) {
               Game.player.changePlayerMode(2);
               Game.player.requestAnimation(0);
               bC = true;
               Game.bm.actionState = 100;
               Game.bn.actionState = 100;
               Game.player.positionX = this.positionX;
               Game.player.positionY = this.positionY;
               return;
            }
            break;
         case 100:
            if (Game.player.actionState != 2) {
               Game.player.changePlayerMode(2);
               Game.player.requestAnimation(0);
               bC = true;
               Game.bm.actionState = 100;
               Game.bn.actionState = 100;
            }

            this.positionX = Game.player.positionX;
            this.positionY = Game.player.positionY;
      }
   }

   // $VF: renamed from: e (int) boolean
   public final boolean hasStandingHeadroom(int var1) {
      int var2 = this.collisionBounds[0] + 1;
      int var3 = this.collisionBounds[2] - 1;
      int var4 = 0;
      if ((var1 & 1) == 0) {
         var2 = this.positionX;
      } else if ((var1 & 2) == 0) {
         var3 = this.positionX;
      }

      for (int var5 = var2 / 20; var5 <= var3 / 20; var5++) {
         int var6;
         if ((var6 = Game.getCollisionTile(var5, this.collisionBounds[1] / 20 - 1)) > var4) {
            var4 = var6;
         }
      }

      return var4 <= 15;
   }

   // $VF: renamed from: aw () void
   private void updatePlayer() {
      if (cC > 0) {
         cC--;
      } else {
         cA = -1;
      }

      if (godMode) {
         bs = true;
         bu = true;
         Game.magic = Game.maxMagic;
         if (bH) {
            this.velocityX = this.velocityY = 0;
            if (!Game.wasKeyPressed(4112) && !Game.isKeyHeld(4112)) {
               if (!Game.wasKeyPressed(8256) && !Game.isKeyHeld(8256)) {
                  if (!Game.wasKeyPressed(16388) && !Game.isKeyHeld(16388)) {
                     if (Game.wasKeyPressed(33024) || Game.isKeyHeld(33024)) {
                        this.velocityY = 5120;
                     }

                     return;
                  }

                  this.velocityY = -5120;
                  return;
               }

               this.velocityX = 5120;
               this.facingLeft = false;
               return;
            }

            this.facingLeft = true;
            this.velocityX = -5120;
            return;
         }
      }

      if (weaponSwitchCooldownMs > 0) {
         weaponSwitchCooldownMs = weaponSwitchCooldownMs - Game.tickDeltaMs;
      }

      if (this.bU && this.bV != null && this.bV.animationId == 1) {
         this.a(4, 1, 0, this.bV);
      }

      if (Game.health <= 0 && !bI && this.animationId != 9) {
         this.changePlayerMode(0);
         this.setAnimationImmediate(9);
      }

      if (this.velocityY > 4608) {
         this.velocityY = 4608;
         cW++;
      } else {
         cW = 0;
      }

      if (!aH && this.animationId != 9) {
         this.resolvePlayerActorContacts();
      }

      this.resolveTileCollisions(true, true);
      jumpAllowed = false;
      attackAllowed = false;
      dc = false;
      if (this.tileAtFeet == 2 || this.tileAtHead == 2 || this.tileAboveFeet == 2) {
         this.setAnimationImmediate(9);
      } else if (this.tileAtFeet == 3 || this.tileAtHead == 3 || this.tileAboveFeet == 3) {
         this.a(4, 1, 0, this);
      }

      if (ba) {
         this.ak();
         this.a(this.animationId, this.animationFrame, this.animationTick);
      } else if (bd) {
         this.k();
         this.a(this.animationId, this.animationFrame, this.animationTick);
      } else if (!bi) {
         switch (this.actionState) {
            case 0:
            default:
               break;
            case 2:
               this.f(true);
         }

         regenerateMagic();
         if (this.actionState != 1) {
            if (aH && this.animationId != 8) {
               this.setAnimationImmediate(8);
            }

            switch (this.animationId) {
               case 1:
                  cX++;
               case 0:
               case 10:
               case 11:
               case 12:
                  if (this.actionState == 2) {
                     attackAllowed = true;
                  } else {
                     this.updateGroundInput();
                  }
                  break;
               case 2:
               case 3:
                  attackAllowed = true;
                  if (shortJump) {
                     this.accelerationY = 1536;
                     if (this.animationId == 2) {
                        this.velocityX = this.facingLeft ? -1536 : 1536;
                     }
                  } else {
                     this.accelerationY = 768;
                     if (this.animationId == 2) {
                        this.velocityX = this.facingLeft ? -2048 : 2048;
                     }
                  }

                  if (bA >= 24 && this.velocityY < 0) {
                     this.startFalling(0);
                  }

                  if (this.velocityY > 0 && (this.tileAtFeet >= 15 || playerPlatform != null || bC) && (playerPlatform == null || playerPlatform.type != 50)) {
                     this.landPlayer();
                  }

                  if (this.velocityX != 0 && this.animationId != 2) {
                     this.requestAnimation(2);
                  } else if (this.velocityX == 0 && this.animationId != 3) {
                     this.requestAnimation(3);
                  }

                  if (this.isAnimationEnding()) {
                     if (this.velocityY + this.accelerationY > 0) {
                        this.alignAnimationToBounds(4, 36);
                        this.resolveTileCollisions(true, true);
                     } else {
                        this.requestAnimation(this.animationId);
                     }
                  }
                  break;
               case 4:
               case 5:
                  if (this.animationId == 4 && this.isAnimationEnding()) {
                     int var1 = previousPlayerAnimation;
                     this.requestAnimation(5);
                     previousPlayerAnimation = var1;
                  }

                  attackAllowed = true;
                  if (previousPlayerAnimation == 8) {
                     this.accelerationX = 1024 * (this.velocityX > 0 ? -1 : 1);
                  }

                  if (this.tileAtFeet < 15 && playerPlatform == null && !bC) {
                     this.accelerationY = 768;
                  } else {
                     this.landPlayer();
                  }
                  break;
               case 6:
               case 7:
                  this.velocityX = 0;
                  this.velocityY = 0;
                  this.accelerationY = 0;
                  if (Game.wasKeyPressed(16388)) {
                     this.requestAnimation(3);
                  } else if (this.facingLeft && Game.wasKeyPressed(2) || !this.facingLeft && Game.wasKeyPressed(8)) {
                     this.requestAnimation(2);
                  }

                  if (Game.isKeyHeld(127358)) {
                     this.updateGroundInput();
                  } else if (this.isAnimationEnding()) {
                     this.requestAnimation(0);
                  }
                  break;
               case 8:
                  if (this.velocityX != 0) {
                     this.accelerationX = 1024 * (this.velocityX > 0 ? -1 : 1);
                  }

                  this.velocityY = 0;
                  this.accelerationY = 0;
                  if (!this.grounded && playerPlatform == null && !bC && !aH) {
                     this.startFalling(0);
                  }

                  if ((this.facingLeft ? this.tileAtRightSide : this.tileAtLeftSide) >= 24) {
                     this.velocityX = 0;
                  }

                  if (!aH && this.isAnimationEnding()) {
                     this.updateGroundInput();
                  }

                  if (this.velocityX > 0) {
                     if (this.collisionBounds[2] >= Game.viewBounds[2]) {
                        this.velocityX = 0;
                        this.positionX = this.positionX - (this.collisionBounds[2] - Game.viewBounds[2]);
                     }
                  } else if (this.velocityX < 0 && this.collisionBounds[0] <= Game.viewBounds[0]) {
                     this.velocityX = 0;
                     this.positionX = this.positionX + (Game.viewBounds[0] - this.collisionBounds[0]);
                  }
                  break;
               case 9:
                  if (this.animationFrame == 1 && this.animationTick == 0) {
                     Game.health = 0;
                  }

                  this.velocityX >>= 2;
                  this.velocityY = 0;
                  this.accelerationY = 0;
                  if (this.isAnimationEnding()) {
                     this.flags |= 64;
                     Game.changeState(12);
                  }

                  return;
               case 15:
               case 21:
                  if (this.isAnimationEnding()) {
                     db = 0;
                     this.startFalling(0);
                     this.accelerationY = 768;
                  } else {
                     this.velocityX = 0;
                     this.velocityY = 0;
                  }

                  if (this.grounded) {
                     this.landPlayer();
                     break;
                  }
               case 13:
               case 14:
               case 19:
               case 20:
                  this.decrementTimerA();
                  attackAllowed = false;
                  if (!this.bU) {
                     aj();
                  }

                  if ((this.grounded || playerPlatform != null) && !bC) {
                     if (queuedComboAnimation != -1) {
                        if (this.animationFrame >= this.sprite.getAnimationFrameCount(this.animationId) - 1) {
                           db = -1;
                           this.requestAnimation(queuedComboAnimation);
                           queuedComboAnimation = -1;
                        }
                     } else if (this.isAnimationEnding()) {
                        db = 0;
                        this.updateGroundInput();
                     }
                  } else if (this.animationFrame >= this.sprite.getAnimationFrameCount(this.animationId) - 1) {
                     db = 0;
                     if (queuedComboAnimation != -1) {
                        db = -1;
                        this.requestAnimation(queuedComboAnimation);
                        queuedComboAnimation = -1;
                     } else {
                        this.updateGroundInput();
                     }
                  }
                  break;
               case 16:
               case 18:
               case 22:
               case 23:
                  attackAllowed = false;
                  if (Game.H < 0 && !bi && this.isAnimationEnding()) {
                     this.updateGroundInput();
                  }

                  this.velocityX = 0;
                  this.W();
                  break;
               case 17:
               default:
                  if (this.isAnimationEnding()) {
                     this.updateGroundInput();
                  }
                  break;
               case 24:
                  if (!this.aC()) {
                     this.updateGroundInput();
                  }
                  break;
               case 25:
                  if (this.isAnimationEnding()) {
                     this.flags |= 64;
                     if (!Game.aS) {
                        this.requestAnimation(0);
                     }

                     Game.player.stopMotion();
                  }

                  return;
            }

            if (db != -1) {
               if (db < (this.animationId != 4 && this.animationId != 5 ? 3 : 8)) {
                  db++;
                  attackAllowed = false;
               } else {
                  db = -1;
                  attackAllowed = true;
               }
            }

            if (attackAllowed) {
               if (Game.wasKeyPressed(65568)) {
                  if (activeWeapon == 0) {
                     Game.playSoundOnce(-1);
                     if (this.actionState == 2) {
                        this.requestAnimation(13);
                     } else if (!this.grounded && playerPlatform == null) {
                        this.requestAnimation(15);
                        this.velocityX = 0;
                        this.velocityY = 0;
                     } else if (this.animationId == 10) {
                        if (this.hasStandingHeadroom(3)) {
                           this.requestAnimation(13);
                           this.velocityY = 0;
                           this.velocityX = 0;
                        } else {
                           Game.clearInput();
                        }
                     } else {
                        this.requestAnimation(13);
                        this.velocityY = 0;
                        this.velocityX = 0;
                     }
                  } else if (activeWeapon == 1) {
                     Game.playSoundOnce(-1);
                     if (this.actionState == 2) {
                        this.requestAnimation(19);
                     } else if (!this.grounded && playerPlatform == null) {
                        this.requestAnimation(21);
                        this.velocityX = 0;
                        this.velocityY = 0;
                     } else if (this.animationId == 10) {
                        if (this.hasStandingHeadroom(3)) {
                           this.requestAnimation(19);
                           this.velocityY = 0;
                           this.velocityX = 0;
                        } else {
                           Game.clearInput();
                        }
                     } else {
                        this.requestAnimation(19);
                        this.velocityY = 0;
                        this.velocityX = 0;
                     }
                  }
               } else if (Game.wasKeyPressed(1) && this.animationId != 10) {
                  if (activeWeapon == 1) {
                     if (this.grounded || playerPlatform != null || bC) {
                        if (Game.magic >= bR[bO[activeWeapon]]) {
                           this.requestAnimation(23);
                           this.velocityX = this.velocityY = 0;
                           Game.H = 0;
                        } else if (Game.w <= 0L) {
                           Game.w = 1000L;
                        }
                     }
                  } else if (activeWeapon == 0 && (aM == null || aM[0] == null) && (this.grounded || playerPlatform != null || bC)) {
                     if (Game.magic >= bR[bO[activeWeapon]]) {
                        this.requestAnimation(18);
                        this.velocityX = this.velocityY = 0;
                        Game.H = 0;
                     } else if (Game.w <= 0L) {
                        Game.w = 1000L;
                     }
                  }
               } else if (Game.wasKeyPressed(512) && weaponSwitchCooldownMs <= 0) {
                  cB = 1;
                  if (activeWeapon == 0) {
                     switchWeapon(1);
                     Game.k = true;
                  } else if (activeWeapon == 1) {
                     switchWeapon(0);
                     Game.k = true;
                  }
               }
            }

            if (jumpAllowed && this.actionState != 2 && Game.wasKeyPressed(16398)) {
               if (this.animationId == 10 && !this.hasStandingHeadroom(3)) {
                  return;
               }

               this.velocityY = 0;
               if (Game.wasKeyPressed(16388)) {
                  diagonalJump = false;
                  shortJump = false;
                  this.requestAnimation(3);
                  this.velocityX = 0;
                  if (this.bU) {
                     this.velocityY = -2560;
                  } else {
                     this.velocityY = -4608;
                  }
               } else if (Game.wasKeyPressed(10)) {
                  diagonalJump = true;
                  shortJump = false;
                  this.requestAnimation(2);
                  this.velocityX = Game.wasKeyPressed(2) ? -1536 : 1536;
                  if (this.bU) {
                     this.velocityY = -2560;
                  } else {
                     this.velocityY = -4608;
                  }
               }

               if (!Game.wasKeyPressed(16388)) {
                  if (Game.wasKeyPressed(8)) {
                     this.facingLeft = false;
                  } else {
                     this.facingLeft = true;
                  }
               }

               this.bU = false;
               playerPlatform = null;
            }

            if (dc && Game.wasKeyPressed(33024)) {
               this.startFalling(3584);
            }

            if ((Game.wasKeyPressed(12378) || Game.isKeyHeld(12378))
               && this.animationId != 15
               && this.animationId != 21
               && !isPlayerMeleeAnimation()
               && !this.grounded
               && playerPlatform == null
               && this.actionState != 2
               && !this.bU) {
               if (Game.wasKeyPressed(4114) || Game.isKeyHeld(4114)) {
                  this.velocityX = -2048;
                  if (!this.facingLeft) {
                     this.facingLeft = true;
                     diagonalJump = false;
                  }
               } else if (Game.wasKeyPressed(8264) || Game.isKeyHeld(8264)) {
                  this.velocityX = 2048;
                  if (this.facingLeft) {
                     this.facingLeft = false;
                     diagonalJump = false;
                  }
               }
            }

            if (!this.grounded && !diagonalJump && !Game.isKeyHeld(4114) && !Game.isKeyHeld(8264) && this.actionState != 2) {
               this.velocityX = 0;
            }

            this.a(this.animationId, this.animationFrame, this.animationTick);
            if (this.velocityX != 0 && !V) {
               Z = Game.player.positionX;
               V = true;
            }

            if (this.velocityX == 0 && V) {
               aa = Game.player.positionX;
               ab = Math.abs(aa - Z);
               V = false;
               int var2 = Game.player.facingLeft ? Game.player.positionX - 160 : Game.player.positionX - 80;
               if (ab > 20 && Math.abs(Game.cameraX - var2) <= 2) {
                  X = 0;
                  W = false;
               } else {
                  X++;
                  W = true;
               }
            }

            this.resolveActorSeparation();
            this.R = false;
            this.S = false;
            if (aI) {
               this.stopMotion();
               if (this.animationId == 10 || this.animationId == 12) {
                  this.requestAnimation(0);
               }
            }

            if (bJ && absInt(this.velocityX) >= 1280) {
               this.velocityX = this.facingLeft ? -1280 : 1280;
            }
         }
      }
   }

   // $VF: renamed from: v () int
   public static final int rollWeaponDamage() {
      int var0 = Game.randomRange(9, 12);
      if (activeWeapon == 0) {
         return bS[bM[activeWeapon]] * var0 / 10;
      } else {
         return activeWeapon == 1 ? bT[bM[activeWeapon]] * var0 / 10 : 10 * var0 / 10;
      }
   }

   // $VF: renamed from: c (int, int) int
   public static final int weaponDamagePercent(int var0, int var1) {
      byte var2 = 0;
      if (var0 == 0 && var1 == 14) {
         var2 = 80;
      }

      return 100 - var2;
   }

   // $VF: renamed from: f (int) boolean
   public static final boolean switchWeapon(int var0) {
      if (var0 == activeWeapon) {
         return false;
      }

      if (var0 == 0) {
         weaponSwitchCooldownMs = 450;
         Game.player.spawnActor(21, 18, 36, 0, -80, 300, false);
      } else if (var0 == 1) {
         if (!bs) {
            return false;
         }

         weaponSwitchCooldownMs = 450;
         Game.player.spawnActor(21, 18, 37, 0, -80, 300, false);
      }

      activeWeapon = var0;
      Game.aR = true;
      Game.k = true;
      queuedComboAnimation = -1;
      return true;
   }

   public static final void d(Actor var0) {
      if (var0 != null) {
         var0.animationFrame = 0;
         var0.animationTick = 0;
         var0.flags &= -129;
         var0.flags &= -1025;
         var0.positionX = Game.player.positionX;
         var0.positionY = Game.player.positionY;
         var0.facingLeft = Game.player.facingLeft;
      }
   }

   public final void a(int var1, int var2, int var3) {
      if (Game.bo != null) {
         if (var1 >= 13 && var1 <= 15) {
            if (var2 == 0 && var3 == 0) {
               if (0 == activeWeapon) {
                  Game.bo.sprite = Game.sprites[25];
                  Game.bo.setAnimationImmediate(var1 - 13);
                  d(Game.bo);
               }
            } else if (Game.bo.isAnimationEnding()) {
               e(Game.bo);
            }
         }

         if ((Game.bo.flags & 128) == 0 && Game.bo.isAnimationEnding()) {
            e(Game.bo);
         }
      }
   }

   public static final void e(Actor var0) {
      if (var0 != null) {
         var0.flags |= 128;
         var0.flags |= 1024;
      }
   }

   // $VF: renamed from: g (int) void
   public final void changePlayerMode(int var1) {
      if (var1 != this.actionState) {
         this.actionState = var1;
         switch (var1) {
            case 0:
               this.sprite = Game.getSprite(spriteIdByType[0]);
               this.flags &= -65;
               if (!this.grounded && playerPlatform == null) {
                  this.setAnimationImmediate(4);
               } else {
                  this.setAnimationImmediate(0);
               }

               this.spawnActor(39, 1, 3, 0, -25, 302, this.facingLeft);
               return;
            case 2:
               return;
            case 3:
               this.flags |= 128;
               this.stopMotion();
               Game.clearInput();
            case 1:
         }
      }
   }

   // $VF: renamed from: ax () void
   private void landPlayer() {
      this.positionY = (this.collisionBounds[3] + 1) / 20 * 20;
      this.positionY--;
      this.velocityY = 256;
      this.requestAnimation(Game.player.positionY - cV > 85 ? 7 : 6);
      diagonalJump = false;
      shortJump = false;
      this.velocityX = 0;
   }

   // $VF: renamed from: ay () boolean
   private boolean updateGroundInput() {
      jumpAllowed = true;
      attackAllowed = true;
      if (this.actionState == 2) {
         this.requestAnimation(0);
         return true;
      }

      if (this.animationId == 0) {
         this.velocityX = 0;
         this.velocityY = 0;
      }

      if (Game.isKeyHeld(4112)) {
         if (this.facingLeft) {
            if (this.animationId == 10 || this.animationId == 12) {
               this.velocityX = 0;
               if (!this.hasStandingHeadroom(1)) {
                  return false;
               }
            }

            return this.updateWalking();
         }

         this.facingLeft = true;
         this.dj = true;
         this.au = 0;
      } else if (Game.isKeyHeld(8256)) {
         if (!this.facingLeft) {
            if (this.animationId == 10 || this.animationId == 12) {
               this.velocityX = 0;
               if (!this.hasStandingHeadroom(2)) {
                  return false;
               }
            }

            return this.updateWalking();
         }

         this.facingLeft = false;
         this.dj = true;
         this.au = 0;
      } else if (this.animationId == 1 && cX >= 8 && this.animationId != 0) {
         this.requestAnimation(0);
         this.velocityX = 0;
         return true;
      }

      return this.updateIdleAndCrouch();
   }

   // $VF: renamed from: az () boolean
   private boolean updateIdleAndCrouch() {
      boolean var1 = false;
      if (!this.grounded && playerPlatform == null && !bC) {
         this.startFalling(0);
         jumpAllowed = false;
         attackAllowed = false;
         return false;
      }

      if (this.animationId == 10) {
         this.velocityX = 0;
         if (!Game.isKeyHeld(16388) && !Game.wasKeyPressed(16388)) {
            if ((Game.wasKeyPressed(33024) || Game.isKeyHeld(33024)) && (this.tileAtFeet == 15 || playerPlatform != null)) {
               if (playerPlatform == null
                  || playerPlatform.type != 50
                     && playerPlatform.type != 33
                     && playerPlatform.type != 82
                     && playerPlatform.type != 14
                     && playerPlatform.type != 72
                     && playerPlatform.type != 73) {
                  this.startFalling(0);
                  this.positionY += 20;
                  playerPlatform = null;
               }
            } else if (Game.isKeyHeld(4112)) {
            }
         } else {
            if (this.hasStandingHeadroom(3)) {
               this.requestAnimation(11);
            }

            Game.clearInput();
         }
      } else if (this.animationId == 12) {
         this.velocityX = 0;
         if (this.isAnimationEnding()) {
            this.requestAnimation(10);
         }
      } else if (this.animationId == 11) {
         this.velocityX = 0;
         if (this.isAnimationEnding()) {
            this.requestAnimation(0);
         }
      } else if (Game.isKeyHeld(33024) || Game.wasKeyPressed(33024)) {
         this.requestAnimation(12);
         Game.clearInput();
      } else if (this.animationId == 0 && Game.stateFrame % 100 == 0) {
         this.requestAnimation(0);
      } else if (this.animationId == 0) {
         if (this.velocityX != 0) {
            this.applySlopeVelocity();
         }

         if (this.isAnimationEnding()) {
            this.requestAnimation(0);
         }
      } else if (this.animationId != 0 || this.isAnimationEnding()) {
         this.requestAnimation(q(this.animationId) ? 10 : 0);
      }

      return true;
   }

   // $VF: renamed from: aA () boolean
   private boolean updateWalking() {
      this.velocityY = 0;
      if (!this.grounded && playerPlatform == null && !bC) {
         this.startFalling(0);
         jumpAllowed = false;
         attackAllowed = false;
         return false;
      }

      if (this.animationId != 1) {
         cX = 0;
      }

      this.requestAnimation(1);
      int var1;
      if ((var1 = Math.abs(this.velocityX)) < 1280) {
         var1 = 1280;
      } else if (var1 < 2560) {
         var1 += 320;
      }

      if (var1 > 2560) {
         var1 = 2560;
      }

      if (this.facingLeft) {
         this.velocityX = -var1;
      } else {
         this.velocityX = var1;
      }

      this.applySlopeVelocity();
      return true;
   }

   public final boolean c(boolean var1) {
      boolean var2 = false;
      if (this.tileAtFeet != 18 && this.tileAtFeet != 19 && this.tileAtFeet != 16) {
         if ((this.tileAtFeet == 21 || this.tileAtFeet == 20 || this.tileAtFeet == 17) && this.velocityX > 0) {
            var2 = true;
         }
      } else if (this.velocityX < 0) {
         var2 = true;
      }

      if (this.bV != null && 105 == this.bV.actionState) {
         var2 = true;
      }

      if (!var1) {
         var2 = !var2;
      }

      return var2;
   }

   // $VF: renamed from: w () int
   public final int updateActorBlockSides() {
      int var1 = 0;
      var1 = 0 | this.resolveActorSeparation();

      for (int var3 = Game.paintActorCount - 1; var3 >= 0; var3--) {
         Actor var2;
         if ((var2 = Game.paintActors[var3]) != null
            && var2 != this
            && var2.isPushBlockingEnemyType()
            && rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds)
            && var2.supportActor != this) {
            if (this.positionX < var2.positionX) {
               var1 |= 2;
            } else {
               var1 |= 1;
            }
         }
      }

      return var1;
   }

   // $VF: renamed from: f (b) void
   public final void pushActor(Actor var1) {
      if (var1 != playerPlatform) {
         int var2 = var1.updateActorBlockSides();
         if (this.positionX < var1.positionX) {
            if (this.velocityX >= 0) {
               if (var1.wallOnRight || (var2 & 2) != 0) {
                  this.positionX = this.positionX - (this.collisionBounds[2] - var1.collisionBounds[0]);
                  return;
               }

               var1.positionX = var1.positionX + (this.collisionBounds[2] - var1.collisionBounds[0]);
               if (var1.supportedActor != null) {
                  var1.supportedActor.positionX = var1.supportedActor.positionX + (this.collisionBounds[2] - var1.collisionBounds[0]);
               }

               if (!this.bU) {
                  bJ = true;
                  return;
               }
            }
         } else if (this.positionX >= var1.positionX && this.velocityX <= 0) {
            if (!var1.wallOnLeft && (var2 & 1) == 0) {
               var1.positionX = var1.positionX - (var1.collisionBounds[2] - this.collisionBounds[0]);
               if (var1.supportedActor != null) {
                  var1.supportedActor.positionX = var1.supportedActor.positionX - (var1.collisionBounds[2] - this.collisionBounds[0]);
               }

               if (!this.bU) {
                  bJ = true;
                  return;
               }
            } else if (!this.R) {
               this.positionX = this.positionX + (var1.collisionBounds[2] - this.collisionBounds[0]);
            }
         }
      }
   }

   // $VF: renamed from: x () void
   public final void resolvePlayerActorContacts() {
      this.refreshBounds();
      bJ = false;
      int var2 = minInt(9, 2560);
      int var3 = maxInt(9, 2560);

      for (int var4 = Game.updateActorCount - 1; var4 >= 0; var4--) {
         Actor var1;
         if ((var1 = Game.updateActors[var4]) != null && (var1.type == 37 || var1.type == 80) && (var1.flags & 1024) == 0 && var1.O) {
            this.handlePlayerActorContact(var1, var3);
         }
      }

      for (int var6 = Game.updateActorCount - 1; var6 >= 0; var6--) {
         Actor var5;
         if ((var5 = Game.updateActors[var6]) != null
            && var5 != this
            && var5.type != 37
            && var5.type != 80
            && (var5.flags & 1024) == 0
            && var5.O
            && var5.actionState != 5
            && var5.actionState != 6
            && var5.actionState != 7) {
            this.handlePlayerActorContact(var5, var2);
         }
      }
   }

   // $VF: renamed from: a (b, int) void
   public final void handlePlayerActorContact(Actor var1, int var2) {
      var1.refreshBounds();
      if (rectsOverlapInclusive(var1.collisionBounds, this.collisionBounds) && this.positionY > var1.collisionBounds[1]) {
         if (var1.type == 14 && (var1.animationId == 2 || var1.animationId == 3)) {
            this.pushActor(var1);
            return;
         }

         if (this.positionX < var1.positionX) {
            if (this.velocityX <= 0 && this.grounded && !var1.P) {
               if (!var1.wallOnRight) {
                  if (!var1.P) {
                     var1.positionX = var1.positionX + minInt(var2, this.collisionBounds[2] - var1.collisionBounds[0]);
                  } else if (this.wallOnLeft) {
                     this.positionX = this.positionX + minInt(var2, var1.collisionBounds[2] - this.collisionBounds[0]);
                  }
               } else if (!this.S) {
                  this.positionX = this.positionX - (this.collisionBounds[2] - var1.collisionBounds[0]);
               }
            } else if (!this.wallOnLeft && !this.R) {
               if (this.velocityX > 0 && var1.velocityX < 0 && !var1.P) {
                  this.positionX = this.positionX - (this.velocityX >> 8);
                  var1.positionX = var1.positionX - (var1.velocityX >> 8);
               } else {
                  this.positionX = this.positionX - minInt(var2, this.collisionBounds[2] - var1.collisionBounds[0]);
               }
            } else if (!var1.P) {
               var1.positionX = var1.positionX + minInt(var2, this.collisionBounds[2] - var1.collisionBounds[0]);
            } else if (this.wallOnLeft) {
               this.positionX = this.positionX + minInt(var2, var1.collisionBounds[2] - this.collisionBounds[0]);
            }

            this.S = true;
         } else if (this.positionX >= var1.positionX) {
            if (this.velocityX >= 0 && this.grounded && !var1.P) {
               if (!var1.wallOnLeft) {
                  if (!var1.P) {
                     var1.positionX = var1.positionX - minInt(var2, var1.collisionBounds[2] - this.collisionBounds[0]);
                  } else if (this.wallOnRight) {
                     this.positionX = this.positionX - minInt(var2, this.collisionBounds[2] - var1.collisionBounds[0]);
                  }
               } else if (!this.R) {
                  this.positionX = this.positionX + (var1.collisionBounds[2] - this.collisionBounds[0]);
               }
            } else if (!this.wallOnRight && !this.S) {
               if (this.velocityX > 0 && var1.velocityX < 0 && !var1.P) {
                  this.positionX = this.positionX - (this.velocityX >> 8);
                  var1.positionX = var1.positionX - (var1.velocityX >> 8);
               } else {
                  this.positionX = this.positionX + minInt(var2, var1.collisionBounds[2] - this.collisionBounds[0]);
               }
            } else if (!var1.P) {
               var1.positionX = var1.positionX - minInt(var2, var1.collisionBounds[2] - this.collisionBounds[0]);
            } else if (this.wallOnRight) {
               this.positionX = this.positionX - minInt(var2, this.collisionBounds[2] - var1.collisionBounds[0]);
            }

            this.R = true;
         }

         var1.Q = true;
      }
   }

   // $VF: renamed from: aB () void
   private void applySlopeVelocity() {
      if (this.tileAtFeet == 18 || this.tileAtFeet == 19) {
         this.velocityY = this.velocityX >> 1;
      } else if (this.tileAtFeet == 21 || this.tileAtFeet == 20) {
         this.velocityY = -this.velocityX >> 1;
      } else if (this.tileAtFeet == 16) {
         this.velocityY = this.velocityX;
      } else {
         if (this.tileAtFeet != 17) {
            return;
         }

         this.velocityY = -this.velocityX;
      }

      if (this.velocityY < 0 && this.type != 70) {
         this.velocityY = 0;
      }
   }

   private void f(boolean var1) {
      if (var1) {
         if (!isPlayerMeleeAnimation() && !isPlayerSpecialAnimation() && this.animationId != 25 && worldScrollPixelsPerTick >= 12) {
            this.requestAnimation(0);
         }

         int var2 = worldScrollPixelsPerTick << 8;
         this.facingLeft = false;
         if (Game.isKeyHeld(61780)) {
            if (Game.isKeyHeld(4112)) {
               this.velocityX = var2 - 2048;
               this.velocityY = 0;
               return;
            }

            if (Game.isKeyHeld(8256)) {
               this.velocityX = var2 + 2048;
               this.velocityY = 0;
               return;
            }

            if (Game.isKeyHeld(16388)) {
               this.velocityX = var2;
               this.velocityY = -2048;
               if (this.animationId == 25) {
                  Game.player.positionX = Game.bm.positionX;
                  Game.player.positionY = Game.bm.positionY;
                  this.velocityY = 0;
                  return;
               }
            } else if (Game.isKeyHeld(33024)) {
               this.velocityX = var2;
               this.velocityY = 2048;
               return;
            }
         } else {
            this.velocityX = var2;
            this.velocityY = 0;
         }
      }
   }

   private boolean aC() {
      jumpAllowed = true;
      attackAllowed = true;
      if (this.bU && this.bV != null) {
         this.requestAnimation(24);
         if (this.collisionBounds[1] >= this.bV.collisionBounds[1] + 10) {
            bW = true;
         } else if (!bW) {
            this.positionY = this.bV.collisionBounds[1] + 35;
         }

         if (!Game.wasKeyPressed(4112) && !Game.isKeyHeld(4112)) {
            if (!Game.wasKeyPressed(8256) && !Game.isKeyHeld(8256)) {
               this.velocityX = 0;
            } else if (!this.facingLeft) {
               this.velocityX = 768;
            } else {
               this.facingLeft = false;
            }
         } else if (this.facingLeft) {
            this.velocityX = -768;
         } else {
            this.facingLeft = true;
         }

         if (bW) {
            if (this.collisionBounds[1] < this.bV.collisionBounds[1]) {
               bW = false;
               this.positionY = this.bV.collisionBounds[1] + 30;
            } else if (Game.wasKeyPressed(16388) || Game.isKeyHeld(16388)) {
               this.velocityY = -1280;
            } else if (!Game.wasKeyPressed(33024) && !Game.isKeyHeld(33024)) {
               this.accelerationY = -256;
               if (absInt(this.velocityY) >= 1280) {
                  this.velocityY = -1280;
               }
            } else {
               this.velocityY = 1280;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean r(int var1) {
      if (Game.wasKeyPressed(var1)) {
         bX += 8;
      } else {
         bX--;
      }

      if (bX < 0) {
         bX = 0;
      }

      return bX >= 40;
   }

   // $VF: renamed from: aD () void
   private void updateCameraRegion() {
      if (Game.au == this && Game.levelModes[Game.levelIndex] == 3) {
         if (Game.player.positionY - 55 <= Game.viewBounds[1]) {
            Game.player.positionY = Game.viewBounds[1] + 55;
         } else if (Game.player.collisionBounds[3] > this.collisionBounds[3]) {
            Game.player.positionY = Game.viewBounds[3];
         }
      }

      if (Game.au == this && Game.levelModes[Game.levelIndex] == 2) {
         if (Game.player.positionX <= this.collisionBounds[0]) {
            Game.player.positionX = this.collisionBounds[0];
         } else if (Game.player.positionX > this.collisionBounds[2]) {
            Game.player.positionX = this.collisionBounds[2];
         }
      }

      if (pointInRect(Game.player.positionX, Game.player.positionY, this.collisionBounds)) {
         Game.a(this);
         if (!Game.cameraControllerActive) {
            Game.R = this.collisionBounds[0];
            Game.S = this.collisionBounds[2];
            Game.T = this.collisionBounds[1];
            Game.U = this.collisionBounds[3];
         }
      }
   }

   // $VF: renamed from: aE () void
   private void updateEnemyType2() {
      if (this.aB != -1 && this.attachedActor == null) {
         this.setAttachedActor(Game.findActorByUid(this.aB));
         if (this.attachedActor != null && (this.attachedActor.flags & 512) != 0) {
            this.flags |= 512;
         }
      }

      label327:
      switch (this.actionState) {
         case 0:
            int var1 = this.getBehaviorVariant();
            if (this.attachedActor == null && var1 == 2) {
               this.m(this.horizontalSpeedTowardPlayer(3), 6);
            }

            if (rectsOverlapInclusive(Game.player.collisionBounds, this.customBounds)) {
               switch (var1) {
                  case 0:
                     this.changeActionState(101);
                  case 1:
                  case 4:
                  case 5:
                  default:
                     break label327;
                  case 2:
                     this.changeActionState(112);
                     break label327;
                  case 3:
                     this.changeActionState(116);
                     break label327;
                  case 6:
                     this.changeActionState(138);
                     this.ac = this.positionX;
                     this.ad = this.positionY;
                     this.aF = this.facingLeft ? 2 : 1;
                     this.flags |= 16;
               }
            } else if (var1 == 10 && rectsOverlapInclusive(Game.viewBounds, this.customBounds)) {
               this.changeActionState(132);
            }
            break;
         case 2:
            this.decrementTimerA();
            if (this.isAnimationEnding()) {
               switch (this.getBehaviorVariant()) {
                  case 0:
                     this.changeActionState(101);
                  case 1:
                  case 4:
                  case 5:
                  default:
                     break;
                  case 2:
                     this.changeActionState(112);
                     break;
                  case 3:
                     this.changeActionState(116);
                     break;
                  case 6:
                     this.ac = this.positionX;
                     this.ad = this.positionY;
                     this.changeActionState(138);
               }
            }
            break;
         case 3:
            if (this.attachedActor != null && this.attachedActor.type == 52) {
               this.attachedActor.changeActionState(103);
               this.attachedActor.resolveTileCollisions(true, true);
               this.setAttachedActor(null);
            }

            this.resetOrRemove(true);
            if (this.uid < 0) {
               this.aF();
            }

            return;
         case 101:
            this.facePlayerFixed();
            this.dd = Game.e(Game.player.fixedX - this.fixedX, Game.player.fixedY - 6400 - this.fixedY);
            this.velocityX = this.dd == 0 ? 0 : (Game.player.fixedX - this.fixedX) * 768 / this.dd;
            this.velocityY = this.dd == 0 ? 0 : (Game.player.fixedY - 6400 - this.fixedY) * 768 / this.dd;
            this.buildInteractionBounds(bZ);
            if (rectsOverlapInclusive(Game.player.collisionBounds, this.interactionBounds)) {
               this.changeActionState(200);
            }
            break;
         case 102:
            if (this.isAnimationEnding()) {
               this.changeActionState(103);
            }
            break;
         case 103:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(101);
            }
            break;
         case 111:
            this.dd = Game.e(Game.player.fixedX - this.fixedX, Game.player.fixedY - 23040 - this.fixedY);
            if (this.dd > 768) {
               this.velocityX = (Game.player.fixedX - this.fixedX) * 768 / this.dd;
               this.velocityY = (Game.player.fixedY - 23040 - this.fixedY) * 768 / this.dd;
               this.facePlayerBeyondDistance(this.velocityX);
            } else {
               this.stopMotion();
            }

            this.decrementTimerA();
            if (this.aw <= 0 && this.attachedActor == null) {
               this.changeActionState(112);
            }
            break;
         case 112:
            this.dd = Game.e(Game.player.fixedX - this.fixedX, Game.player.fixedY - 23040 - this.fixedY);
            if (this.dd <= 768) {
               this.stopMotion();
            } else {
               if (!Game.player.grounded && playerPlatform == null) {
                  this.velocityX = this.horizontalSpeedTowardPlayerDeadZone(768);
                  this.velocityY = 0;
               } else {
                  this.velocityX = (Game.player.fixedX - this.fixedX) * 768 / this.dd;
                  this.velocityY = (Game.player.fixedY - 23040 - this.fixedY) * 768 / this.dd;
               }

               this.facePlayerBeyondDistance(this.velocityX);
            }

            this.decrementTimerB();
            if (this.attachedActor == null) {
               this.m(this.horizontalSpeedTowardPlayer(3), 6);
            }

            if (this.dd <= 1280 && this.ax <= 0) {
               this.changeActionState(113);
            }
            break;
         case 113:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(114);
            }
            break;
         case 114:
            if (this.isAnimationEnding()) {
               this.changeActionState(115);
            }
            break;
         case 115:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(111);
            }
            break;
         case 116:
            this.velocityX = this.dj ? -768 : 768;
            this.velocityY = -512;
            break;
         case 119:
            this.decrementTimerA();
            this.decrementTimerB();
            if (this.aw <= 0) {
               this.changeActionState(120);
            }
            break;
         case 120:
            this.decrementTimerB();
            if (this.ax <= 0) {
               this.changeActionState(121);
            }
            break;
         case 121:
            this.decrementTimerA();
            if (this.aw <= 0) {
               if (!this.dj) {
                  this.spawnStraightProjectile();
                  this.dj = true;
               }

               if (this.isAnimationEnding()) {
                  this.changeActionState(122);
               }
            }
            break;
         case 122:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(123);
            }
            break;
         case 123:
            if (!rectsOverlapInclusive(Game.viewBounds, this.collisionBounds)) {
               Game.removeActor(this);
            }

            return;
         case 124:
         default:
            break;
         case 125:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(126);
            }
            break;
         case 126:
            if (this.isAnimationEnding()) {
               this.changeActionState(127);
            }
            break;
         case 127:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(128);
            }
            break;
         case 128:
            if (!rectsOverlapInclusive(Game.viewBounds, this.collisionBounds)) {
               Game.removeActor(this);
               return;
            }
            break;
         case 129:
            this.decrementTimerA();
            this.decrementTimerB();
            if (this.aw <= 0) {
               this.changeActionState(130);
            }
            break;
         case 130:
            this.decrementTimerB();
            if (this.ax <= 0) {
               this.changeActionState(131);
            }
            break;
         case 131:
            if (!rectsOverlapInclusive(Game.viewBounds, this.collisionBounds)
               || this.positionX < -20
               || this.positionY < -20
               || this.positionY > Game.levelPixelHeight + 20) {
               Game.removeActor(this);
               return;
            }
            break;
         case 132:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(133);
            }
            break;
         case 133:
            if (!rectsOverlapInclusive(Game.viewBounds, this.collisionBounds)
               && (this.attachedActor == null || this.attachedActor != null && !rectsOverlapInclusive(Game.viewBounds, this.attachedActor.collisionBounds))) {
               if (this.attachedActor != null) {
                  Game.removeActor(this.attachedActor);
                  this.setAttachedActor(null);
               }

               this.resetOrRemove(false);
               return;
            }
            break;
         case 135:
            this.decrementTimerA();
            this.velocityX = this.aF == 1 ? 1280 : -1280;
            this.positionY = (30 * Game.sin256(2 * (Game.viewBounds[this.aF == 1 ? 0 : 2] - this.positionX)) >> 8) + this.ad;
            if (this.aw <= 0 && !rectsOverlapInclusive(Game.viewBounds, this.collisionBounds)) {
               Game.removeActor(this);
               return;
            }
            break;
         case 138:
         case 139:
            this.decrementTimerA();
            if (this.actionState == 138) {
               this.buildInteractionBounds(bZ);
               if (rectsOverlapInclusive(Game.player.collisionBounds, this.interactionBounds)) {
                  this.changeActionState(139);
               }
            } else if (this.actionState == 139 && this.isAnimationEnding()) {
               this.changeActionState(138);
            }

            this.velocityX = this.aF == 1 ? 768 : -768;
            this.positionY = (30 * Game.sin256(2 * (this.ac - this.positionX)) >> 8) + this.ad;
            if (!rectsOverlapInclusive(Game.au.collisionBounds, this.collisionBounds)) {
               this.flags &= -17;
               this.resetOrRemove(false);
               return;
            }
            break;
         case 200:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(102);
            }
      }

      if (!this.updateRespawnFade()) {
         int var2;
         if (3 == Game.levelModes[Game.levelIndex]
            && ((var2 = this.getBehaviorVariant()) >= 11 || var2 == 10 && this.actionState == 133 || var2 == 5 || var2 == 6)
            && activeBoss == null) {
            this.scrollWithWorld = true;
         }

         if (this.attachedActor != null) {
            if (this.attachedActor.type == 16 && this.attachedActor.actionState == 101) {
               this.positionChild(this.attachedActor, 3, 6);
            } else if (this.attachedActor.type == 52) {
               this.positionChild(this.attachedActor, 3, 21);
            }
         }

         if (!this.o()) {
            if (this.getBehaviorVariant() != 10) {
               this.tryReceivePlayerWeaponDamageWithReaction();
            }

            if ((this.getBehaviorVariant() == 0 || this.getBehaviorVariant() == 1 || this.getBehaviorVariant() == 5 || this.getBehaviorVariant() == 6)
                  && (
                     rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)
                        || rectsOverlapInclusive(Game.player.collisionBounds, this.attackBounds)
                  )
                  && !this.ab()
               || 3 == Game.levelModes[Game.levelIndex]
                  && this.getBehaviorVariant() != 10
                  && rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
               Game.player.a(4, this.getStat(1), 0, this);
            }

            if (this.uid < 0 && this.attachedActor != null && !this.attachedActor.updatedThisTick) {
               this.attachedActor.update();
            }
         }
      }
   }

   private void aF() {
      lastSpawnedActor = Game.d(this);
      if (lastSpawnedActor != null) {
         lastSpawnedActor.attachedActor = null;
         if (this.attachedActor != null) {
            this.attachedActor.flags &= -257;
         }
      }

      Game.removeActor(this);
   }

   // $VF: renamed from: aG () void
   private void updateEnemyType3() {
      if (this.aB != -1 && this.attachedActor == null) {
         this.setAttachedActor(Game.findActorByUid(this.aB));
         if (this.attachedActor != null && (this.attachedActor.flags & 512) != 0) {
            this.flags |= 512;
         }

         if (this.attachedActor != null) {
            this.attachedActor.flags |= 32;
         }
      }

      label184:
      switch (this.actionState) {
         case 0:
            if (this.attachedActor == null) {
               this.m(this.horizontalSpeedTowardPlayer(5), -31);
            }

            int var2 = this.getBehaviorVariant();
            if (rectsOverlapInclusive(Game.player.collisionBounds, this.customBounds)) {
               switch (var2) {
                  case 0:
                     this.changeActionState(101);
                     break label184;
                  case 1:
                     this.changeActionState(105);
                     break label184;
                  case 2:
                     this.changeActionState(108);
               }
            }
            break;
         case 1:
            this.accelerationY = 768;
            if (this.grounded) {
               switch (this.getBehaviorVariant()) {
                  case 0:
                     this.changeActionState(101);
                     break;
                  case 1:
                     if (this.attachedActor != null) {
                        this.changeActionState(105);
                     } else {
                        this.changeActionState(107);
                     }
                     break;
                  case 2:
                     this.changeActionState(108);
               }
            }
            break;
         case 2:
            this.decrementTimerA();
            if (this.isAnimationEnding()) {
               switch (this.getBehaviorVariant()) {
                  case 0:
                     this.changeActionState(101);
                     break;
                  case 1:
                     if (this.attachedActor != null) {
                        this.changeActionState(105);
                     } else {
                        this.changeActionState(107);
                     }
                     break;
                  case 2:
                     this.changeActionState(108);
               }
            }
            break;
         case 3:
            if (this.attachedActor != null) {
               if (this.attachedActor.type == 52) {
                  this.attachedActor.flags &= -33;
                  this.attachedActor.changeActionState(103);
                  this.attachedActor.resolveTileCollisions(true, true);
                  this.setAttachedActor(null);
               } else if (this.attachedActor.type == 16) {
                  this.n(0, 0);
               }
            }

            this.resetOrRemove(true);
            return;
         case 101:
            this.velocityX = this.horizontalSpeedTowardPlayerDeadZone(512);
            this.facePlayerBeyondDistance(512);
            this.buildInteractionBounds(bY);
            if (rectsOverlapInclusive(Game.player.collisionBounds, this.interactionBounds)) {
               this.changeActionState(102);
            }
            break;
         case 102:
            if (this.isAnimationEnding()) {
               this.requestAnimation(12);
            }

            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(103);
            }
            break;
         case 103:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(104);
            }
            break;
         case 104:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(101);
            }
            break;
         case 105:
            if (this.attachedActor == null) {
               this.m(this.horizontalSpeedTowardPlayer(5), -31);
            }

            this.facePlayerPixels();
            int var1 = this.a(
               this.fixedX + (this.horizontalSpeedTowardPlayerDeadZone(5) << 8), -this.fixedY - -7936, Game.player.fixedX, -Game.player.fixedY, 3584, -512
            );
            if (this.attachedActor != null && this.attachedActor.type == 16 && this.attachedActor.actionState == 101 && var1 != 0) {
               if (absInt(var1) > 2304) {
                  this.velocityX = this.horizontalSpeedTowardPlayer(512);
                  this.requestAnimation(10);
               } else if (absInt(var1) < 1024) {
                  this.velocityX = -this.horizontalSpeedTowardPlayer(512);
                  this.requestAnimation(11);
                  this.decrementTimerA();
                  if (this.aw <= 0) {
                     this.ax = this.facingLeft ? -1024 : 1024;
                     if (this.attachedActor != null && this.attachedActor.type == 16 && this.attachedActor.actionState == 101) {
                        this.changeActionState(106);
                     }
                  }
               } else {
                  this.ax = var1;
                  this.changeActionState(106);
               }
            }
            break;
         case 106:
            if (this.isAnimationEnding()) {
               this.n(this.ax, -3584);
               this.changeActionState(107);
            }
            break;
         case 107:
            this.facePlayerPixels();
            this.decrementTimerA();
            if (this.attachedActor == null && this.aw <= 0) {
               this.m(this.horizontalSpeedTowardPlayer(5), -31);
               this.changeActionState(105);
            }
            break;
         case 108:
            this.velocityX = this.dj ? -1024 : 1024;
            if (this.wallOnLeft || this.wallOnRight) {
               this.changeActionState(109);
            }
         case 109:
      }

      if (!this.updateRespawnFade()) {
         if (this.velocityX != 0) {
            this.resolveActorSeparation();
         }

         if (this.attachedActor != null) {
            if (this.attachedActor.type == 16 && this.attachedActor.actionState == 101) {
               this.positionChild(this.attachedActor, 5, -31);
            } else if (this.attachedActor.type == 52) {
               this.positionChild(this.attachedActor, 5, -31);
            }
         }

         if (this.actionState != 0 && this.getVariantFlagBit() == 1) {
            this.updateEnemyGravity(true);
         } else if (this.grounded) {
            this.updateEnemyGravity(false);
         } else {
            this.updateEnemyGravity(true);
         }

         if (this.getBehaviorVariant() == 0 && rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
            this.bI();
            this.resetOrRemove(true);
         } else if (!this.o()) {
            if (!this.tryReceivePlayerWeaponDamageWithReaction() && rectsOverlapInclusive(Game.player.collisionBounds, this.attackBounds)) {
               Game.player.a(4, this.getStat(1), 0, this);
            }
         }
      }
   }

   private void s(int var1) {
      if (Game.levelModes[Game.levelIndex] == 2) {
         if (this.isNullOrInView(this)) {
            Game.removeActor(this.attachedActor);
            this.attachedActor = null;
            return;
         }

         if (Game.player.positionY - this.positionY <= var1 && Game.player.positionY > this.positionY) {
            int var2 = Game.viewBounds[1] - this.positionY + 18;
            if (this.attachedActor == null) {
               Actor var3;
               (var3 = this.spawnActor(21, 18, 14, 0, var2, 100, false)).ay = 2;
               var3.ar = var3.positionY - Game.cameraY;
               this.setAttachedActor(var3);
            }
         }
      }
   }

   // $VF: renamed from: aH () void
   private void updateEnemyType1() {
      this.s(320);
      switch (this.actionState) {
         case 0:
            if (rectsOverlapInclusive(Game.player.collisionBounds, this.customBounds)) {
               this.changeActionState(101);
               this.paintPriority = 81;
            }
            break;
         case 1:
            this.accelerationY = 768;
            if (this.grounded) {
               this.velocityY = 0;
               this.changeActionState(101);
            }
            break;
         case 2:
            this.decrementTimerA();
            if (this.isAnimationEnding()) {
               this.changeActionState(101);
            }
            break;
         case 3:
            this.setAttachedActor(null);
            if (this.ay >= 4) {
               this.actionState = 5;
               this.flags &= -1025;
               this.flags |= 128;
               this.aw = 5000;
            }

            this.resetOrRemove(true);
            return;
         case 5:
            if (this.ay >= 4) {
               this.decrementTimerA();
               if (this.aw <= 0) {
                  this.changeActionState(0);
               }

               return;
            }
            break;
         case 101:
            this.facePlayerBeyondDistance(256);
            this.velocityX = this.horizontalSpeedTowardPlayerDeadZone(256);
            if (this.ay >= 4) {
               this.velocityX <<= 1;
            }

            this.buildInteractionBounds(this.getBehaviorVariant() == 1 ? cb : ca);
            if (rectsOverlapInclusive(Game.player.collisionBounds, this.interactionBounds)) {
               this.changeActionState(102);
            }
            break;
         case 102:
            if (this.isAnimationEnding()) {
               this.changeActionState(103);
            }
            break;
         case 103:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(101);
            }
      }

      if (this.ay >= 4 || !this.updateRespawnFade()) {
         if (this.actionState != 0) {
            this.ax = this.ax - Game.tickDeltaMs;
            if (this.attachedActor != null && this.ax < 0) {
               this.aG = this.getBehaviorVariant() == 0 ? 0 : 1;
            }
         }

         if (this.velocityX != 0 || this.velocityY != 0) {
            this.resolveActorSeparation();
         }

         if (this.Q) {
            this.velocityX = 0;
            this.Q = false;
         }

         if (this.ay < 4 || this.actionState != 0) {
            if (this.actionState != 0) {
               this.updateEnemyGravity(this.getVariantFlagBit() == 1);
            }

            if (this.o()) {
               return;
            }
         }

         if ((this.ay < 4 || this.actionState != 0)
            && !this.tryReceivePlayerWeaponDamageWithReaction()
            && rectsOverlapInclusive(Game.player.collisionBounds, this.attackBounds)) {
            Game.player.a(4, this.getStat(1), 0, this);
         }
      }
   }

   // $VF: renamed from: aI () void
   private void updateHitProjectile() {
      if (this.isAnimationEnding()) {
         Game.removeActor(this);
      } else {
         if (rectsOverlapInclusive(this.collisionBounds, Game.player.collisionBounds)) {
            Game.player.a(4, this.getStat(1), 0, this);
            Game.removeActor(this);
         }
      }
   }

   // $VF: renamed from: aJ () void
   private void updateExplosionHitbox() {
      if (this.isAnimationEnding()) {
         Game.removeActor(this);
      } else {
         if (rectsOverlapInclusive(this.collisionBounds, Game.player.collisionBounds)) {
            Game.player.a(4, this.getStat(1), 0, this);
         }

         for (int var1 = 0; var1 < Game.updateActorCount; var1++) {
            Actor var2;
            if ((var2 = Game.updateActors[var1]) != null
               && (var2.flags & 1024) == 0
               && var2.actionState != 3
               && var2.actionState != 7
               && var2.actionState != 5
               && var2.actionState != 6) {
               if (var2.isDamageable() && var2.collisionBounds != null && rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds)) {
                  if (var2.type == 70) {
                     var2.attachedActor = this;
                     var2.changeActionState(2);
                  } else {
                     var2.applyDamageWithReaction(500);
                  }
               } else if (var2.type == 73 && !var2.bU && var2.variantData == 0 && rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds)) {
                  var2.facingLeft = var2.positionX < this.positionX;
                  var2.changeActionState(105);
               } else if (var2.type == 79 && rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds)) {
                  var2.damageDestructibleCells(this.collisionBounds, false, 79);
               }
            }
         }
      }
   }

   // $VF: renamed from: aK () void
   private void updateEnemyType70() {
      if (this.dj) {
         this.aG = 1;
      } else {
         this.aG = 0;
      }

      label372:
      switch (this.actionState) {
         case 0:
            if (this.variantData == 1 || this.variantData == 0) {
               this.snapDownToGround();
            }

            if (rectsOverlapInclusive(Game.player.collisionBounds, this.customBounds) || rectContainsRect(this.customBounds, Game.player.collisionBounds)) {
               this.O = true;
               this.P = false;
               switch (this.variantData) {
                  case 0:
                     this.changeActionState(101);
                     break label372;
                  case 1:
                     this.changeActionState(105);
                     this.snapDownToGround();
                     break label372;
                  case 2:
                  case 3:
                     this.changeActionState(110);
               }
            }
            break;
         case 2:
            this.accelerationY = 768;
            this.resolveTileCollisions(true, true);
            switch (this.variantData) {
               case 0:
                  if (this.grounded) {
                     this.changeActionState(102);
                  }
                  break label372;
               case 1:
               case 2:
                  if (this.isAnimationEnding()) {
                     this.requestAnimation(5);
                     this.at = 1;
                  }

                  if (this.grounded) {
                     this.stopMotion();
                     if (this.at == 1) {
                        this.changeActionState(102);
                     }
                  }
               default:
                  break label372;
            }
         case 3:
            this.resetOrRemove(true);
            return;
         case 101:
            this.accelerationY = 512;
            this.resolveTileCollisions(true, true);
            if (this.grounded) {
               this.decrementTimerA();
               if (this.aw <= 0) {
                  if (++this.au > 1) {
                     this.velocityY = (this.velocityY >= 0 ? -1 : 1) * 3400 >> 1;
                     this.velocityX = 0;
                     if (this.au > 2) {
                        this.au = 0;
                     }
                  } else {
                     this.velocityY = (this.velocityY >= 0 ? -1 : 1) * 3400;
                     this.facePlayerBeyondDistance(1200);
                     this.velocityX = this.horizontalSpeedTowardPlayer(1200);
                  }

                  this.aw = 1000;
               }
            } else {
               this.aw = 0;
            }
            break;
         case 102:
            if (this.isAnimationEnding()) {
               this.changeActionState(103);
            } else {
               this.resolveTileCollisions(true, true);
            }
            break;
         case 103:
            this.resolveTileCollisions(true, true);
            if (this.dj) {
               this.decrementTimerB();
               if (this.ax <= 0) {
                  this.velocityX = 0;
               }

               if (this.tileAtFeet <= 1) {
                  this.accelerationY = 768;
               } else {
                  this.velocityY = 0;
               }

               if (this.dg < 3) {
                  this.decrementTimerA();
                  if (this.aw <= 400 && this.aw > 200) {
                     this.aG = 1;
                  } else if (this.aw <= 200 && this.aw > 0) {
                     this.aG = 2;
                  } else {
                     this.aG = 1;
                     this.aw = 400;
                     this.dg++;
                  }
               } else {
                  this.spawnActor(-40, 4, 3, 0, 0, 302, false);
                  lastSpawnedActor.setAnimationImmediate(3);
                  this.changeActionState(3);
                  Game.playSoundOnce(-1);
               }
            } else {
               this.decrementTimerA();
               if (this.aw <= 0) {
                  switch (this.variantData) {
                     case 0:
                        this.changeActionState(104);
                        break label372;
                     case 1:
                        this.changeActionState(104);
                        break label372;
                     case 2:
                        this.changeActionState(112);
                  }
               }
            }
            break;
         case 104:
            if (this.isAnimationEnding()) {
               switch (this.variantData) {
                  case 0:
                     this.changeActionState(101);
                     break;
                  case 1:
                     this.changeActionState(108);
               }
            }
            break;
         case 105:
            this.facePlayerFixed();
            this.velocityX = this.horizontalSpeedTowardPlayerDeadZone(400);
            this.resolveTileCollisions(true, false);
            if (this.tileAtFeet <= 1) {
               if (Game.getCollisionTile(this.positionX / 20, (this.positionY + 30) / 20) <= 1) {
                  this.accelerationY = 768;
               } else {
                  this.velocityY = 2304;
               }
            } else {
               this.velocityY = 0;
            }

            this.dd = Game.e(Game.player.fixedX - this.fixedX, Game.player.fixedY - this.fixedY) >> 8;
            if (this.dd < 65) {
               this.decrementTimerA();
               if (this.aw <= 0 && this.grounded) {
                  this.changeActionState(109);
               }
            }

            if ((
                  this.velocityX > 0 && this.tileRightOfFeet < 24 && this.tileRightOfFeet != 15
                     || this.velocityX < 0 && this.tileLeftOfFeet < 24 && this.tileLeftOfFeet != 15
                     || this.tileAtFeet < 24 && this.tileAtFeet != 15
                     || this.velocityX > 0 && this.tileAtRightSide < 24 && this.tileAtRightSide != 15 && this.tileAtRightSide > 1
                     || this.velocityX < 0 && this.tileAtLeftSide < 24 && this.tileAtLeftSide != 15 && this.tileAtLeftSide > 1
               )
               && this.tileAtFeet > 1) {
               this.changeActionState(107);
            }
            break;
         case 106:
            if (this.aw > 0) {
               this.decrementTimerA();
            }

            this.facePlayerFixed();
            this.velocityX = this.horizontalSpeedTowardPlayerDeadZone(400);
            this.resolveTileCollisions(true, true);
            this.applySlopeVelocity();
            if (this.tileAtFeet <= 1) {
               this.accelerationY = 768;
            }

            if ((this.tileRightOfFeet >= 24 || this.tileRightOfFeet == 15)
               && (this.tileLeftOfFeet >= 24 || this.tileLeftOfFeet == 15)
               && (this.tileAtFeet >= 24 || this.tileAtFeet == 15)
               && this.aw <= 0) {
               this.changeActionState(108);
            }
            break;
         case 107:
            if (this.isAnimationEnding()) {
               this.aw = 1000;
               this.changeActionState(106);
            }
            break;
         case 108:
            if (this.isAnimationEnding()) {
               this.changeActionState(105);
            }
            break;
         case 109:
            this.resolveTileCollisions(true, true);
            if (this.isAnimationEnding()) {
               this.aw = 2048;
               this.spawnActor(-37, 1, 33, (this.facingLeft ? -1 : 1) * 20, 0, 302, false);
               this.changeActionState(105);
            }
            break;
         case 110:
            this.resolveTileCollisions(true, true);
            this.facePlayerFixed();
            this.velocityX = this.horizontalSpeedTowardPlayerDeadZone(400);
            if (this.velocityX < 0 && this.tileAtLeftSide > 1
               || this.velocityX > 0 && this.tileAtRightSide > 1
               || this.velocityX < 0 && this.sampleHeadTileLeft() <= 1
               || this.velocityX > 0 && this.sampleHeadTileRight() <= 1) {
               this.velocityX = 0;
            }

            this.dd = absInt(Game.player.fixedX - this.fixedX) >> 8;
            if (this.variantData == 3) {
               if (this.dd < 195) {
                  this.changeActionState(115);
               }
            } else if (this.variantData == 2 && this.dd < 16 && Game.player.fixedY > this.fixedY) {
               this.changeActionState(111);
            }
            break;
         case 111:
            this.accelerationY = 768;
            if (this.at == 0 && this.isAnimationEnding()) {
               this.at = 1;
               this.requestAnimation(5);
            }

            this.resolveTileCollisions(true, true);
            this.decrementTimerA();
            if (this.aw <= 0 && this.tileAtFeet >= 16) {
               this.changeActionState(102);
            }
            break;
         case 112:
            if (this.at == 0 && this.isAnimationEnding()) {
               this.at = 1;
               this.requestAnimation(5);
            }

            this.resolveTileCollisions(true, true);
            if (this.ceilingHit) {
               this.changeActionState(113);
            }
            break;
         case 113:
            this.resolveTileCollisions(true, false);
            if (this.isAnimationEnding()) {
               this.changeActionState(110);
            }
            break;
         case 114:
            this.V();
            this.refreshBounds();
            int var1 = Math.abs(this.ar);
            int var2 = ((this.av << 8) + var1 * Game.cos256(this.aw >> 16) >> 8) + (this.collisionBounds[3] - this.collisionBounds[1] >> 1);
            int var3 = (this.as << 8) + var1 * Game.sin256(this.aw >> 16) >> 8;
            this.velocityY = var2 - this.positionY << 8;
            this.velocityX = var3 - this.positionX << 8;
            this.interactionBounds[3] = this.collisionBounds[3];
            this.interactionBounds[1] = this.collisionBounds[1] - this.dh;
            this.interactionBounds[0] = this.collisionBounds[0] - 240;
            this.interactionBounds[2] = this.collisionBounds[2] + 240;
            break;
         case 115:
            if (this.at == 0 && this.isAnimationEnding()) {
               this.at = 1;
               this.requestAnimation(11);
            }

            this.refreshBounds();
            if ((this.collisionBounds[3] + this.collisionBounds[1] >> 1) - this.av > this.dh) {
               this.ar = this.dh;
               this.positionY = this.positionY - ((this.collisionBounds[3] + this.collisionBounds[1] >> 1) - this.av - this.dh);
               this.velocityY = 0;
               if (this.interactionBounds == null) {
                  this.interactionBounds = new int[4];
                  this.refreshBounds();
                  this.interactionBounds[3] = this.collisionBounds[3];
                  this.interactionBounds[1] = this.collisionBounds[1] - this.dh;
                  this.interactionBounds[0] = this.collisionBounds[0] - 240;
                  this.interactionBounds[2] = this.collisionBounds[2] + 240;
               }

               this.changeActionState(114);
            }
      }

      if (this.velocityY > 4608) {
         this.velocityY = 4608;
      }

      if (!this.updateRespawnFade()) {
         this.resolveActorSeparation();
         if (this.variantData == 3) {
            if ((this.actionState == 114 || this.actionState == 115)
               && (
                  rectsOverlapInclusive(this.collisionBounds, Game.player.attackBounds)
                     || segmentCrossesRectEdges(
                        this.as, this.av, this.positionX, this.collisionBounds[3] + this.collisionBounds[1] >> 1, Game.player.attackBounds
                     )
               )) {
               this.moduleMask = null;
               this.interactionBounds = null;
               this.O = true;
               this.P = false;
               this.variantData = 1;
               this.changeActionState(2);
            }
         } else if (this.actionState != 3 && rectsOverlapInclusive(this.collisionBounds, Game.player.attackBounds)) {
            if (this.actionState != 103 && this.actionState != 102) {
               if (this.actionState == 110 || this.actionState == 113) {
                  this.changeActionState(111);
               } else if (this.actionState != 2) {
                  this.changeActionState(2);
               }
            } else if (!this.dj) {
               this.positionX = this.positionX + (this.fixedX - Game.player.fixedX > 0 ? 1 : -1) * 1;
               this.resolveTileCollisions(true, true);
               this.applySlopeVelocity();
               this.tryReceivePlayerWeaponDamage(false);
            } else {
               this.ax = 400;
               this.velocityX = -this.horizontalSpeedTowardPlayer(1000);
            }
         }

         if (this.actionState != 3
            && this.actionState != 103
            && this.actionState != 102
            && rectsOverlapInclusive(this.collisionBounds, Game.player.collisionBounds)) {
            Game.player.a(4, this.getStat(1), 0, this);
            if (this.variantData == 2 && this.actionState != 113 && this.actionState != 110) {
               this.changeActionState(112);
            }
         }
      }
   }

   public final void y() {
      byte var1 = 0;
      boolean var2 = false;
      if (this.type == 100) {
         var1 = -50;
      }

      this.decrementTimerA();
      if (Game.E == -2) {
         if (this.type == 100 && this.x != null) {
            this.x.positionX = this.positionX;
            this.x.positionY = this.positionY;
            this.x.snapDownToGround();
            this.x = null;
         }

         if (this.type != 83) {
            activeBoss = null;
            Game.playLevelMusic();
         }

         if (this.type != 83) {
            Game.removeActor(this);
         }

         Game.l();
      } else if (Game.E < cf) {
         int var3 = Game.E * 320 / cf;

         for (int var4 = 0; var4 < ci; var4++) {
            Game.directGraphics.setARGBColor(Integer.MAX_VALUE);
            Game.fillArc(this.positionX - Game.cameraX - var3 + 0, this.positionY - Game.cameraY - var3 + var1, var3 << 1, var3 << 1, cj[var4], ck[var4]);
         }

         Game.directGraphics.setARGBColor(2130706432);
         Game.fillArc(this.positionX - Game.cameraX - 320 + 0, this.positionY - Game.cameraY - 320 + var1, 640, 640, 0, 360);
      } else if (Game.E < cf + cg + ch) {
         if (Game.shakeYTicks <= 0) {
            Game.startVerticalShake(8);
         }

         boolean var7 = false;
         int var8 = (Game.E - cf) * cg;

         for (int var5 = 0; var5 < ci; var5++) {
            Game.directGraphics.setARGBColor(Integer.MAX_VALUE);
            Game.fillArc(this.positionX - Game.cameraX - 320 + 0, this.positionY - Game.cameraY - 320 + var1, 640, 640, var8 + cj[var5], ck[var5]);
         }

         int var6 = 0;
         int var9;
         if (Game.E < cf + cg) {
            var9 = (Game.E - cf) * 320 / cg;
            var6 = ((Game.E - cf) * 128 + 128) / cg << 24 | 16776960;
         } else {
            var9 = (Game.E - cf - cg) * 320 / ch;
            var6 = ((cf + cg + ch - Game.E) * 128 + 128) / ch << 24 | 0xFF0000;
         }

         Game.directGraphics.setARGBColor(var6);
         Game.fillArc(this.positionX - Game.cameraX - var9 + 0, this.positionY - Game.cameraY - var9 + var1, var9 << 1, var9 << 1, 0, 360);
         Game.directGraphics.setARGBColor(2130706432);
         Game.fillArc(this.positionX - Game.cameraX - 320 + 0, this.positionY - Game.cameraY - 320 + var1, 640, 640, 0, 360);
         if (!Game.ci) {
            if (Game.randomRange(0, 5) == 0) {
               this.c(3, this.collisionBounds);
            }

            this.spawnActor(39, 1, this.am(), -Game.randomRange(-20, 20), -Game.randomRange(10, 50), 302, Game.random.nextInt() % 2 == 0);
            lastSpawnedActor.flags |= 4096;
         }
      } else {
         Game.E = -3;
         Game.aP = true;
         if (Game.ci) {
            Game.ci = false;
         }
      }

      Game.E++;
   }

   private void c(int var1, int[] var2) {
      this.spawnActor(39, 1, var1, 0, 0, 302, false);
      lastSpawnedActor.positionX = Game.randomRange(this.collisionBounds[0], this.collisionBounds[2]);
      lastSpawnedActor.positionY = Game.randomRange(this.collisionBounds[1], this.collisionBounds[3]);
      lastSpawnedActor.flags |= 4096;
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
            graphics.setColor(var6[var10]);
         } else {
            graphics.setColor(var6[var10 >> 1]);
         }

         if (absInt(var1 - var3) > absInt(var2 - var4)) {
            Game.drawLine(var1 - Game.cameraX, var2 - Game.cameraY + (var5 == 1 ? 0 : var8), var3 - Game.cameraX, var4 - Game.cameraY + var8);
         } else {
            Game.drawLine(var1 - Game.cameraX + (var5 == 1 ? 0 : var8), var2 - Game.cameraY, var3 - Game.cameraX + var8, var4 - Game.cameraY);
         }

         var8++;
      }
   }

   public static final void z() {
      graphics.setColor(16777215);
      Game.drawRect(Game.player.collisionBounds[0] - Game.cameraX, Game.player.collisionBounds[1] - Game.cameraY - 20 + 0, 42, 5);
      graphics.setColor(16711680);
      Game.fillRect(Game.player.collisionBounds[0] - Game.cameraX + 1, Game.player.collisionBounds[1] - Game.cameraY - 19 + 0, bX * 40 / 40, 4);
   }

   // $VF: renamed from: aL () void
   private void updateBeamEmitter() {
      switch (this.actionState) {
         case 0:
            if (rectsOverlapInclusive(Game.player.collisionBounds, this.customBounds)) {
               if (this.getBehaviorVariant() == 0) {
                  this.changeActionState(101);
               } else {
                  this.changeActionState(102);
               }
            }
            break;
         case 101:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(102);
            }
            break;
         case 102:
            if (this.isAnimationEnding()) {
               this.changeActionState(103);
            }
            break;
         case 103:
            this.decrementTimerA();
            if (this.aw <= 0) {
               if (this.getBehaviorVariant() == 0) {
                  this.changeActionState(104);
               } else {
                  this.changeActionState(107);
                  this.aA = 1;
               }
            }
            break;
         case 104:
            if (this.getStat(2) > 0) {
               this.decrementTimerA();
               if (this.aw <= 0) {
                  this.changeActionState(105);
               }
            }
            break;
         case 105:
            if (this.isAnimationEnding()) {
               this.changeActionState(106);
            }
            break;
         case 106:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(102);
            }
            break;
         case 107:
         case 109:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.aw = 0;
            }

            int var1;
            if (this.actionState == 107) {
               var1 = this.getStat(3) * (this.getStat(1) - this.aw) / this.getStat(1);
            } else {
               var1 = this.getStat(3) * this.aw / this.getStat(1);
            }

            rotatePoint(this.positionX, this.positionY, this.au, this.ar, var1, ce);
            this.ac = ce[0];
            this.ad = ce[1];
            if (this.aw <= 0) {
               this.changeActionState(this.actionState == 107 ? 108 : 110);
            }
            break;
         case 108:
         case 110:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(this.actionState == 108 ? 109 : 107);
            }
      }

      if (this.aA == 1) {
         raycastTiles(this.positionX, this.positionY, this.ac, this.ad, ce);
         this.ac = ce[0];
         this.ad = ce[1];
         if (segmentCrossesRectEdges(this.positionX, this.positionY, this.ac, this.ad, Game.player.collisionBounds)) {
            Game.player.a(4, this.getStat(0), 0, this);
         }

         if (this.u == null) {
            this.spawnActor(39, 1, 13, 0, 0, 302, false);
            this.u = lastSpawnedActor;
         }

         this.u.positionX = this.ac;
         this.u.positionY = this.ad;
      } else {
         if (this.u != null) {
            Game.removeActor(this.u);
            this.u = null;
         }
      }
   }

   // $VF: renamed from: aM () void
   private void updateEnemyType13() {
      int var1;
      var1 = this.getBehaviorVariant();
      label106:
      switch (this.actionState) {
         case 0:
            if (var1 == 0) {
               this.ad = this.positionY;
               this.changeActionState(101);
            } else if (rectsOverlapInclusive(Game.player.collisionBounds, this.customBounds)) {
               switch (var1) {
                  case 1:
                     this.changeActionState(103);
                     break label106;
                  case 2:
                     this.changeActionState(102);
                     break label106;
                  case 3:
                     this.changeActionState(106);
                     this.flags |= 16;
                     this.ac = this.positionX;
                     this.ad = this.positionY;
                     this.aF = this.facingLeft ? 1 : 2;
               }
            }
            break;
         case 2:
            if (var1 == 0) {
               if (this.isAnimationEnding()) {
                  this.changeActionState(0);
               }
            } else if (var1 == 1) {
               this.decrementTimerA();
               if (this.isAnimationEnding()) {
                  this.requestAnimation(0);
               }

               if (this.aw <= 0) {
                  this.changeActionState(103);
               }
            }
            break;
         case 3:
            if (var1 != 1 && var1 != 3) {
               this.resetOrRemove(true);
            } else {
               for (int var2 = 0; var2 < this.au << 1; var2++) {
                  this.spawnActor(35, 33, 0, Game.randomRange(-20, 20), -Game.randomRange(-40, 10), 308, false);
               }

               for (int var3 = 0; var3 < this.as << 1; var3++) {
                  this.spawnActor(35, 33, 2, Game.randomRange(-20, 20), -Game.randomRange(-40, 10), 308, false);
               }

               this.resetOrRemove(true);
            }

            this.flags &= -17;
            this.cameraDistanceBucket = 10;
            return;
         case 101:
         default:
            break;
         case 102:
            if (this.isAnimationEnding()) {
               this.spawnFourWayProjectiles(1792, -256);
               this.changeActionState(104);
            }
            break;
         case 103:
            this.dd = Game.e(Game.player.boundsCenterX - this.positionX, Game.player.boundsCenterY - this.positionY);
            this.velocityX = this.dd == 0 ? 0 : (Game.player.boundsCenterX - this.positionX) * 256 / this.dd;
            this.velocityY = this.dd == 0 ? 0 : (Game.player.boundsCenterY - this.positionY) * 256 / this.dd;
            break;
         case 104:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(102);
            }
            break;
         case 105:
            if (this.isAnimationEnding()) {
               this.changeActionState(103);
            }
            break;
         case 107:
            if (this.isAnimationEnding()) {
               this.changeActionState(106);
            }
         case 106:
            this.decrementTimerA();
            this.velocityX = this.aF == 1 ? -512 : 512;
            this.positionY = (30 * Game.sin256(2 * (this.ac - this.positionX)) >> 8) + this.ad;
            if (!rectsOverlapInclusive(Game.au.collisionBounds, this.collisionBounds)) {
               this.flags &= -17;
               this.resetOrRemove(false);
               return;
            }
      }

      if (!this.o()) {
         if (var1 == 0) {
            this.tryReceivePlayerWeaponDamageWithReaction();
         } else {
            this.tryReceivePlayerWeaponDamage(false);
         }

         if (rectsOverlapInclusive(Game.player.collisionBounds, this.attackBounds) || rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
            Game.player.a(4, this.getStat(1), 0, this);
         }
      }
   }

   // $VF: renamed from: aN () void
   private void updateDeceleratingProjectile() {
      if (this.isAnimationEnding()) {
         Game.removeActor(this);
      } else {
         if (sameSignOrZero(this.velocityX, this.au)) {
            this.velocityX = 0;
         } else {
            this.accelerationX = this.au;
         }

         if (sameSignOrZero(this.velocityY, this.ar)) {
            this.velocityY = 0;
         } else {
            this.accelerationY = this.ar;
         }

         if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
            Game.player.a(4, this.ax, 0, this);
         }
      }
   }

   // $VF: renamed from: i (int, int) void
   private void spawnFourWayProjectiles(int var1, int var2) {
      for (int var3 = 0; var3 < 4; var3++) {
         this.spawnActor(-100, 13, 0, 0, 0, 302, this.facingLeft);
         lastSpawnedActor.positionX = (this.collisionBounds[0] + this.collisionBounds[2] >> 1)
            + (var3 / 2 == 1 ? 0 : ((var3 % 2 << 1) - 1) * (this.collisionBounds[0] - this.collisionBounds[2] >> 1));
         lastSpawnedActor.positionY = (this.collisionBounds[1] + this.collisionBounds[3] >> 1)
            + (var3 / 2 == 0 ? 0 : ((var3 % 2 << 1) - 1) * (this.collisionBounds[1] - this.collisionBounds[3] >> 1));
         lastSpawnedActor.velocityX = (var3 / 2 == 1 ? 0 : (var3 % 2 << 1) - 1) * -var1;
         lastSpawnedActor.velocityY = (var3 / 2 == 0 ? 0 : (var3 % 2 << 1) - 1) * -var1;
         lastSpawnedActor.au = (var3 / 2 == 1 ? 0 : (var3 % 2 << 1) - 1) * -var2;
         lastSpawnedActor.ar = (var3 / 2 == 0 ? 0 : (var3 % 2 << 1) - 1) * -var2;
         lastSpawnedActor.ax = this.getStat(2);
      }
   }

   // $VF: renamed from: aO () void
   private void updateEnemyType14() {
      if (this.actionState != 5 && this.actionState != 6 && this.actionState != 7 || this.bV == null) {
         this.bU = false;
         this.bV = null;
      }

      this.s(440);
      if (this.s == null) {
         this.s = Game.au;
      }

      this.decrementTimerB();
      switch (this.actionState) {
         case 0:
            if (rectsOverlapInclusive(Game.player.collisionBounds, this.customBounds)) {
               this.changeActionState(101);
            }
            break;
         case 1:
            this.accelerationY = 768;
            if (this.velocityY > 4608) {
               this.velocityY = 4608;
            }

            if (this.grounded) {
               this.stopMotion();
               if (this.animationId != 2) {
                  this.changeActionState(101);
               } else {
                  this.changeActionState(104);
               }
            }
            break;
         case 2:
            this.decrementTimerA();
            if (this.isAnimationEnding()) {
               if (this.animationId != 2 && this.animationId != 3) {
                  this.changeActionState(101);
               } else {
                  this.changeActionState(this.bU ? 105 : 104);
               }
            }
            break;
         case 3:
            if (this.supportedActor != null) {
               this.supportedActor.supportActor = null;
               this.supportedActor = null;
            }

            if (playerPlatform == this) {
               playerPlatform = null;
            }

            this.resetOrRemove(true);
            return;
         case 101:
            this.facePlayerBeyondDistance(256);
            this.velocityX = this.horizontalSpeedTowardPlayerDeadZone(256);
            this.buildInteractionBounds(cc);
            if (this.ax <= 0 && rectsOverlapInclusive(Game.player.collisionBounds, this.interactionBounds)) {
               this.changeActionState(102);
            }
            break;
         case 102:
            if (this.ax <= 0) {
               if (!this.dj) {
                  this.spawnHomingProjectile();
                  this.dj = true;
               }

               if (this.isAnimationEnding()) {
                  this.changeActionState(103);
                  this.ax = 3500;
               }
            }
            break;
         case 103:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(101);
            }
            break;
         case 104:
            if (this.grounded) {
               this.N = true;
            }

            if (this.bU) {
               this.changeActionState(105);
            }
            break;
         case 105:
            if (this.bV != null && this.positionY >= this.bV.collisionBounds[1] + 46) {
               this.positionY = this.bV.collisionBounds[1] + 46;
               this.changeActionState(106);
            }
            break;
         case 106:
            this.decrementTimerA();
            if (this.a(1, this.collisionBounds)) {
               this.velocityX = Game.player.positionX < this.positionX ? 768 : -768;
               this.au = this.velocityX > 0 ? -64 : 64;
            }

            if (sameSignOrZero(this.velocityX, this.au)) {
               this.velocityX = 0;
            } else {
               this.accelerationX = this.au;
            }

            if (this.bV != null) {
               this.positionY = this.bV.collisionBounds[1] + 46;
            }
            break;
         case 107:
            if (this.supportedActor != null) {
               this.supportedActor.supportActor = null;
               this.supportedActor = null;
            }

            if (playerPlatform == this) {
               playerPlatform = null;
            }

            this.resetOrRemove(true);
            return;
      }

      if (!this.updateRespawnFade()) {
         if (this.a(1, this.collisionBounds) && this.actionState != 104 && this.actionState != 105 && this.actionState != 106) {
            this.changeActionState(this.bU ? 105 : 104);
            this.az = 500;
         } else {
            this.tryReceivePlayerWeaponDamageWithReaction();
         }

         if (this.actionState != 107) {
            if (this.actionState == 104 || this.actionState == 105 || this.actionState == 106) {
               this.d(0, this.collisionBounds);
            }

            if (!this.bU && this.supportActor == null) {
               if (this.actionState != 0 && this.getVariantFlagBit() == 1) {
                  this.updateEnemyGravity(true);
               } else {
                  this.updateEnemyGravity(false);
               }
            } else if (!this.bU) {
               this.updateEnemyGravity(false);
            }

            this.resolveActorSeparation();
            this.A();
            if (this.s != Game.au) {
               this.resetOrRemove(false);
            }
         }
      }
   }

   public final void A() {
      this.refreshBounds();

      for (int var2 = 0; var2 < Game.updateActorCount; var2++) {
         Actor var1;
         if ((var1 = Game.updateActors[var2]) != null && var1 != this) {
            var1.refreshBounds();
            if (var1.supportActor == this) {
               var1.positionY = this.collisionBounds[1] - 1;
               var1.grounded = true;
               cr[1] = var1.collisionBounds[1];
               cr[0] = var1.collisionBounds[0];
               cr[3] = var1.collisionBounds[3] + 1;
               if (this.type == 14 && this.actionState == 106) {
                  cr[3]++;
               }

               cr[2] = var1.collisionBounds[2];
               if (!rectsOverlapInclusive(this.collisionBounds, cr)) {
                  var1.supportActor = null;
                  this.supportedActor = null;
               }
            }
         }
      }
   }

   // $VF: renamed from: aP () void
   private void updateWaveSpawner() {
      switch (this.actionState) {
         case 0:
            if (rectsOverlapInclusive(Game.viewBounds, this.customBounds)) {
               this.at = 0;
               this.changeActionState(101);
               return;
            }
            break;
         case 101:
            this.velocityX = worldScrollPixelsPerTick << 8;
            int var1 = this.getBehaviorVariant();
            this.aw = this.aw - Game.tickDeltaMs;
            if (this.aw < 0) {
               switch (var1) {
                  case 11:
                     this.s = this.j(this.variantData, this.as);
                     this.t = this.j(this.variantData, this.as);
                     this.u = this.j(this.variantData, this.as);
                     this.s.aw = 1750;
                     this.t.aw = 1100;
                     this.u.aw = 450;
                     this.s.ax = 2250;
                     this.t.ax = 2250;
                     this.u.ax = 2250;
                     this.changeActionState(105);
                     break;
                  case 12:
                  case 14:
                  case 15:
                     if (this.at == 0) {
                        this.s = this.j(this.variantData, this.as);
                     } else if (this.at == 1) {
                        this.t = this.j(this.variantData, this.as);
                     } else if (this.at == 2) {
                        this.u = this.j(this.variantData, this.as);
                     }

                     this.at++;
                     this.changeActionState(102);
                     break;
                  case 13:
                     this.s = this.j(this.variantData, this.as);
                     this.t = this.j(this.variantData, this.as);
                     this.u = this.j(this.variantData, this.as);
                     this.s.aw = 400;
                     this.t.aw = 400;
                     this.u.aw = 400;
                     this.s.ax = 900;
                     this.t.ax = 1300;
                     this.u.ax = 1700;
                     this.changeActionState(105);
               }

               return;
            }
            break;
         case 102:
            if (this.at >= 3) {
               this.changeActionState(103);
               return;
            }

            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(101);
               return;
            }
            break;
         case 103:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(104);
               return;
            }
            break;
         case 104:
            switch (this.getBehaviorVariant()) {
               case 11:
               case 13:
               default:
                  break;
               case 12:
                  if (this.s != null) {
                     this.s.changeActionState(125);
                  }

                  if (this.t != null) {
                     this.t.changeActionState(125);
                  }

                  if (this.u != null) {
                     this.u.changeActionState(125);
                  }

                  this.changeActionState(105);
                  break;
               case 14:
               case 15:
                  this.changeActionState(105);
            }

            return;
         case 105:
            if (this.attachedActor == null) {
               Game.removeActor(this);
               return;
            }

            if (this.isNullOrInView(this.s)
               || this.isNullOrInView(this.t)
               || this.isNullOrInView(this.u)
               || (this.s.flags & 1024) != 0
               || (this.t.flags & 1024) != 0
               || (this.u.flags & 1024) != 0) {
               this.removeSpawnerAndAttachment();
            }
      }
   }

   // $VF: renamed from: B () void
   public final void removeSpawnerAndAttachment() {
      Game.removeActor(this.attachedActor);
      this.setAttachedActor(null);
      Game.removeActor(this);
   }

   // $VF: renamed from: q (b) boolean
   private boolean isNullOrInView(Actor var1) {
      return var1 != null ? rectsOverlapInclusive(var1.collisionBounds, Game.viewBounds) : true;
   }

   private Actor j(int var1, int var2) {
      this.spawnActor(2, 3, 0, 0, 0, 0, false);
      lastSpawnedActor.variantData = var1;
      lastSpawnedActor.statSet = this.statSet;
      lastSpawnedActor.health = lastSpawnedActor.getStat(0);
      lastSpawnedActor.aF = var2;
      switch (var2) {
         case 1:
            lastSpawnedActor.facingLeft = false;
            lastSpawnedActor.positionX = Game.viewBounds[0] + lastSpawnedActor.collisionBounds[0] - lastSpawnedActor.collisionBounds[2];
            break;
         case 2:
            lastSpawnedActor.facingLeft = true;
            lastSpawnedActor.positionX = Game.viewBounds[2] + lastSpawnedActor.collisionBounds[2] - lastSpawnedActor.collisionBounds[0];
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            break;
         case 4:
            lastSpawnedActor.facingLeft = this.facingLeft;
            lastSpawnedActor.positionY = Game.viewBounds[1] + 4;
            break;
         case 8:
            lastSpawnedActor.facingLeft = this.facingLeft;
            lastSpawnedActor.positionY = Game.viewBounds[3] + lastSpawnedActor.collisionBounds[3] - lastSpawnedActor.collisionBounds[1] - 4;
      }

      lastSpawnedActor.paintPriority = 81;
      Actor var3 = lastSpawnedActor;
      switch (lastSpawnedActor.getBehaviorVariant()) {
         case 11:
            lastSpawnedActor.changeActionState(119);
            lastSpawnedActor.aG = 0;
            break;
         case 12:
            lastSpawnedActor.changeActionState(124);
            break;
         case 13:
            lastSpawnedActor.changeActionState(129);
            lastSpawnedActor.aG = 0;
         case 14:
         default:
            break;
         case 15:
            lastSpawnedActor.changeActionState(135);
      }

      return var3;
   }

   // $VF: renamed from: g (boolean) void
   private void updateEnemyGravity(boolean var1) {
      if (Game.player != this) {
         this.resolveTileCollisions(true, true);
      }

      boolean var2;
      if (!(var2 = this.tileAtFeet >= 16 && this.tileAtFeet <= 21) && this.velocityY > 0 && this.tileAtFeet >= 15 && this.supportActor != null) {
         this.snapToGround();
      }

      this.applySlopeVelocity();
      if (!var2 && (this.velocityX < 0 && this.tileLeftOfFeet < 15 || this.velocityX > 0 && this.tileRightOfFeet < 15) && this.supportActor == null) {
         if (!var1) {
            this.velocityX = 0;
         } else if (!this.grounded) {
            this.startEnemyFalling(this.velocityY);
         }
      }

      if (this.velocityY == 0 && !this.grounded && this.supportActor == null) {
         this.startEnemyFalling(this.velocityY);
      }
   }

   // $VF: renamed from: aQ () void
   private void snapToGround() {
      this.positionY = (this.collisionBounds[3] + 1) / 20 * 20;
      this.positionY--;
      if (this.tileAtFeet < 24 && this.tileAtFeet != 15 && this.tileBelowFeet >= 24) {
         this.positionY += 20;
      }

      this.velocityY = 0;
      this.velocityX = 0;
   }

   // $VF: renamed from: C () boolean
   public final boolean isBasicEnemyType() {
      return this.type == 2 || this.type == 3 || this.type == 1 || this.type == 14 || this.type == 70;
   }

   // $VF: renamed from: D () boolean
   public final boolean isDamageable() {
      if ((16384 & this.flags) != 0) {
         return false;
      } else if (this.actionState == 5 || this.actionState == 6 || this.actionState == 7 || this.actionState == 3) {
         return false;
      } else if (this.actionState == 0 && this.type == 1 && this.ay >= 4) {
         return false;
      } else if (this.type == 2 || this.type == 3 || this.type == 14 || this.type == 13) {
         return true;
      } else if (this.type == 1) {
         return true;
      } else if (this.type == 90 && this.actionState >= 105 && this.actionState < 124) {
         return true;
      } else {
         return this.type != 100 || this.actionState != 104 && this.actionState != 103 ? this.type == 70 && this.actionState == 103 && !this.dj : true;
      }
   }

   // $VF: renamed from: aR () boolean
   private boolean isPushBlockingEnemyType() {
      return this.type == 2 || this.type == 3 || this.type == 1 || this.type == 14 || this.type == 70;
   }

   // $VF: renamed from: t (int) void
   private void startEnemyFalling(int var1) {
      if (Game.player != this) {
         this.alignAnimationToBounds(this.animationId, 32);
      }

      this.velocityY = var1;
      this.accelerationY = 768;
      if (Game.player != this && this.actionState != 2) {
         this.changeActionState(1);
      }
   }

   // $VF: renamed from: aS () void
   private void clampActorToActiveRegion() {
      if (this.isBasicEnemyType() && this.collisionBounds != null || this.type == 0 && (this.animationId == 8 || this.animationId == 9)) {
         if (Game.au == null) {
            return;
         }

         if (this.type == 2 && (this.getBehaviorVariant() == 3 || this.getBehaviorVariant() == 5 || this.getBehaviorVariant() == 6)) {
            if (!rectsOverlapInclusive(this.collisionBounds, Game.au.collisionBounds)) {
               if (this.getBehaviorVariant() == 3) {
                  cl = true;
                  this.removeSpawnerAndAttachment();
                  cl = false;
                  return;
               }

               this.resetOrRemove(false);
            }

            return;
         }

         cr[0] = Game.au.collisionBounds[0] + 12;
         cr[2] = Game.au.collisionBounds[2] - 12;
         cr[1] = Game.au.collisionBounds[1] + 12;
         cr[3] = Game.au.collisionBounds[3] - 12;
         if (cr[0] > this.collisionBounds[0] && this.velocityX <= 0) {
            this.velocityX = 0;
         }

         if (cr[2] < this.collisionBounds[2] && this.velocityX >= 0) {
            this.velocityX = 0;
         }

         if (cr[1] > this.collisionBounds[1] && this.velocityY <= 0) {
            this.velocityY = 0;
         }

         if (cr[3] < this.collisionBounds[3] && this.velocityY >= 0) {
            this.velocityY = 0;
         }
      }
   }

   public final void E() {
      if (bd) {
         this.spawnActor(61, 40, 12 + this.at, 0, 0, 101, false);
         this.refreshBounds();
         lastSpawnedActor.positionX = this.positionX + 0;
         lastSpawnedActor.positionY = this.positionY + -4;
         lastSpawnedActor.moduleMask = new int[10];
         lastSpawnedActor.paintPriority = 319;
         this.at++;
         if (this.v != null) {
            Game.removeActor(this.v);
            this.v = null;
         }

         this.v = lastSpawnedActor;
      }
   }

   // $VF: renamed from: aT () void
   private void updateBoss90() {
      switch (this.actionState) {
         case 0:
            if (rectsOverlapInclusive(Game.viewBounds, this.customBounds)) {
               this.ar = 0;
               this.scrollWithWorld = true;
               switch (this.getBehaviorVariant()) {
                  case 0:
                     this.changeActionState(101);
                     break;
                  case 1:
                     this.changeActionState(105);
                     this.av = 0;
                     this.ac = this.positionX - Game.viewBounds[0];
                     this.ad = this.positionY;
                     activeBoss = this;
                     bossInitialHealth = this.health;
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
            Game.f(13);
            clearCombatEffects(false);
            break;
         case 101:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(102);
            }
            break;
         case 102:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.av = this.ar == 0 ? this.au : (this.ar == 1 ? this.as : this.at);
               this.c(8 + this.ar % 2 * 2, Game.viewBounds[0] + 160, this.av, 6);
               if (++this.ar >= 3) {
                  this.changeActionState(103);
               } else {
                  this.aw = 1000;
               }
            }
            break;
         case 103:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(104);
            }
            break;
         case 104:
            if (!rectsOverlapInclusive(Game.viewBounds, this.collisionBounds)) {
               Game.removeActor(this);
               return;
            }
            break;
         case 105:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(106);
            }
            break;
         case 106:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(107);
               this.s = this.j(cE[39], 4);
               this.t = this.j(cE[39], 8);
               this.s.facingLeft = true;
               this.t.facingLeft = true;
               this.s.statSet = this.getStat(2);
               this.t.statSet = this.getStat(2);
               this.s.aw = 400;
               this.t.aw = 400;
               this.s.ax = 900;
               this.t.ax = 900;
               this.s.flags |= 16;
               this.t.flags |= 16;
            }
            break;
         case 107:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(112);
            }
            break;
         case 108:
            this.velocityY = this.verticalSpeedTowardPlayerDeadZone(2560);
            if (this.velocityY < 0 && this.collisionBounds[1] < Game.au.collisionBounds[1] + 20) {
               this.velocityY = 0;
               this.positionY = Game.au.collisionBounds[1] + (this.collisionBounds[3] - this.collisionBounds[1]) + 20;
            } else if (this.velocityY > 0 && this.positionY > Game.au.collisionBounds[3] - 10) {
               this.velocityY = 0;
               this.positionY = Game.au.collisionBounds[3] - 10;
            }

            if (this.velocityY == 0) {
               this.spawnActor(-101, 16, 0, 0, 0, 302, false);
               this.setAttachedActor(lastSpawnedActor);
               this.changeActionState(109);
            }
            break;
         case 109:
            this.decrementTimerA();
            if (this.aw <= 0) {
               Game.removeActor(this.attachedActor);
               this.spawnActor(-101, 16, 1, 0, 0, 302, false);
               this.setAttachedActor(lastSpawnedActor);
               this.changeActionState(110);
            }
            break;
         case 110:
            this.velocityY = this.verticalSpeedTowardPlayerDeadZone(1280);
            if (this.velocityY < 0 && this.collisionBounds[1] < Game.au.collisionBounds[1] + 20) {
               this.velocityY = 0;
               this.positionY = Game.au.collisionBounds[1] + this.collisionBounds[3] - this.collisionBounds[1] + 20;
            } else if (this.velocityY > 0 && this.positionY > Game.au.collisionBounds[3] - 10) {
               this.velocityY = 0;
               this.positionY = Game.au.collisionBounds[3] - 10;
            }

            this.positionChild(this.v, 0, 0);
            if (this.attachedActor != null && this.attachedActor.isAnimationEnding()) {
               Game.removeActor(this.attachedActor);
               this.setAttachedActor(null);
               this.changeActionState(111);
            }
            break;
         case 111:
            this.decrementTimerA();
            if (this.aw <= 0) {
               if (++this.av >= 2) {
                  this.changeActionState(121);
               } else {
                  this.changeActionState(108);
               }
            }
            break;
         case 112:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(113);
               aO = new Actor[6];

               for (int var5 = 0; var5 < 6; var5++) {
                  aO[var5] = this.spawnActor(-102, 17, 0, 0, -37, 302, false);
                  aO[var5].flags |= 16;
                  aO[var5].ax = var5;
                  aO[var5].aw = 500;
                  aO[var5].au = 0;
                  aO[var5].ar = this.getStat(4);
                  aO[var5].changeActionState(101);
               }
            }
            break;
         case 113:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(114);
            }
            break;
         case 114:
            this.as = this.au + Game.viewBounds[0];
            this.dd = Game.e(this.as - this.positionX, this.ar - this.positionY);
            if (this.dd > 4) {
               this.velocityX = this.dd == 0 ? 0 : (this.as - this.positionX) * 1024 / this.dd;
               this.velocityY = this.dd == 0 ? 0 : (this.ar - this.positionY) * 1024 / this.dd;
            } else {
               this.changeActionState(115);
            }
            break;
         case 115:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(116);

               for (int var4 = 0; var4 < 6; var4++) {
                  aO[var4].changeActionState(102);
               }
            }
            break;
         case 116:
            this.decrementTimerA();
            this.positionChild(this.v, 0, 0);
            if (this.aw <= 0) {
               this.changeActionState(117);

               for (int var3 = 0; var3 < 6; var3++) {
                  aO[var3].changeActionState(103);
               }
            }
            break;
         case 117:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(118);
            }
            break;
         case 118:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(119);
            }
            break;
         case 119:
            this.as = this.ac + Game.viewBounds[0];
            this.dd = Game.e(this.as - this.positionX, this.ad - this.positionY);
            if (this.dd > 10) {
               this.velocityX = this.dd == 0 ? 0 : (this.as - this.positionX) * 2560 / this.dd;
               this.velocityY = this.dd == 0 ? 0 : (this.ad - this.positionY) * 2560 / this.dd;
            } else {
               this.stopMotion();
               this.changeActionState(120);

               for (int var2 = 0; var2 < 6; var2++) {
                  Game.removeActor(aO[var2]);
                  aO[var2] = null;
               }
            }
            break;
         case 120:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(108);
               this.av = 0;
            }
            break;
         case 121:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(122);
               this.removeLinkedEffect();
               Game.removeActor(this.x);
               this.x = null;
               this.showLinkedPrompt(27, this.positionX + -35, this.positionY + -90);
            } else {
               this.buildInteractionBounds(cd);
               if (rectContainsRect(this.interactionBounds, Game.player.collisionBounds) && Game.wasKeyPressed(65568)) {
                  Game.player.a(aX);
                  this.removeLinkedEffect();
                  Game.removeActor(this.x);
                  this.x = null;
                  this.showLinkedPrompt(26, this.positionX + -35, this.positionY + -90);
                  this.changeActionState(124);
                  switchWeapon(0);
                  Game.k = true;
               }
            }
            break;
         case 122:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(123);
            }
            break;
         case 123:
            this.as = this.ac + Game.viewBounds[0];
            this.dd = Game.e(this.as - this.positionX, this.ad - this.positionY);
            if (this.dd > 10) {
               this.velocityX = this.dd == 0 ? 0 : (this.as - this.positionX) * 2560 / this.dd;
               this.velocityY = this.dd == 0 ? 0 : (this.ad - this.positionY) * 2560 / this.dd;
            } else {
               this.stopMotion();
               this.changeActionState(105);
               this.av = 0;
            }
            break;
         case 124:
            if (!ba) {
               this.changeActionState(125);
               Game.player.b(aY);
            }
            break;
         case 125:
            if (!bd) {
               this.changeActionState(126);
               Game.player.velocityX = worldScrollPixelsPerTick << 8;
               switchWeapon(1);
            }
            break;
         case 126:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(122);
               this.removeLinkedEffect();
               bX = 0;
               bi = false;
               Game.player.requestAnimation(0);
            } else if (this.r(65568)) {
               bX = 0;
               bi = false;
               this.changeActionState(127);
               this.removeLinkedEffect();
               Game.k = true;
               Game.player.b(aZ);
            }
            break;
         case 127:
            if (!bd) {
               this.changeActionState(128);
               Game.player.velocityX = worldScrollPixelsPerTick << 8;
            }
            break;
         case 128:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(123);
            } else if (this.animationId == 8 && this.isAnimationEnding()) {
               this.requestAnimation(6);
               this.applyDamage(80, false);
            }
      }

      if (this.linkedEffect != null) {
         if (this.actionState == 126) {
            this.linkedEffect.positionX = Game.player.positionX + 35;
            this.linkedEffect.positionY = Game.player.positionY + -100;
         } else {
            this.positionChild(this.linkedEffect, -35, -90);
            if ((this.linkedEffect.animationId == 26 || this.linkedEffect.animationId == 27) && this.linkedEffect.isAnimationEnding()) {
               Game.removeActor(this.linkedEffect);
               this.linkedEffect = null;
            }
         }
      }

      if (this.x != null) {
         this.positionChild(this.x, -35, -10);
      }

      if (this.v != null) {
         this.positionChild(this.v, 0, -4);
      }

      if ((this.actionState == 109 || this.actionState == 110) && this.attachedActor != null) {
         this.positionChild(this.attachedActor, -152, -53);
         this.attachedActor.refreshBounds();
         if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)
            || rectsOverlapInclusive(Game.player.collisionBounds, this.attachedActor.collisionBounds)) {
            Game.player.a(4, this.getStat(3), 0, this);
         }
      }

      if (this.getBehaviorVariant() == 1) {
         if (this.actionState >= 124) {
            if (this.tryReceivePlayerWeaponDamage(false)) {
               this.requestAnimation(8);
            }

            if (this.animationId == 8 && this.isAnimationEnding()) {
               this.requestAnimation(6);
               return;
            }
         } else {
            this.tryReceivePlayerWeaponDamage(false);
         }
      }
   }

   // $VF: renamed from: F () void
   public final void updateBossOrbitProjectile() {
      this.decrementTimerA();
      int var1 = 0;
      if (this.actionState == 101) {
         if (this.aw >= 0) {
            var1 = (500 - this.aw) * 45 / 500;
            int var2 = this.ax * 60;
            this.positionX = activeBoss.positionX + (var1 * Game.cos256(var2) >> 8);
            this.positionY = activeBoss.positionY + (var1 * Game.sin256(var2) >> 8) - 37;
         } else {
            this.positionX = activeBoss.positionX + (45 * Game.cos256(this.ax * 60 + this.au) >> 8);
            this.positionY = activeBoss.positionY + (45 * Game.sin256(this.ax * 60 + this.au) >> 8) - 37;
            this.au += 10;
         }
      } else if (this.actionState == 103) {
         this.velocityX = 2048 * Game.cos256(this.ax * 60 + this.au) >> 8;
         this.velocityY = 2048 * Game.sin256(this.ax * 60 + this.au) >> 8;
         this.flags &= -17;
      } else if (this.actionState == 110 || this.actionState == 111 || this.actionState == 112) {
         if (this.actionState == 110) {
            if (this.aw >= 0) {
               var1 = 50 * (1000 - this.aw) / 1000;
            } else {
               this.actionState = 111;
               var1 = 50;
               activeBoss.requestAnimation(1);
            }
         } else if (this.actionState == 111) {
            var1 = 50;
            this.au += 5;
            this.as += 5;
            if (this.as >= 180) {
               this.actionState = 112;
               this.aw = 2000;
               activeBoss.requestAnimation(3);
               if (activeBoss.w == null) {
                  activeBoss.w = activeBoss.spawnActor(16, 4, 4, 0, 0, 302, false);
               }
            }
         } else if (this.aw > 0) {
            var1 = 50;
         } else {
            activeBoss.requestAnimation(4);
            Game.removeActor(activeBoss.w);
            activeBoss.w = null;
            var1 = 50 - this.aw / 5;
         }

         if (this.ax < 6) {
            this.positionX = activeBoss.positionX + (var1 * Game.cos256(this.ax * 60 + this.au) >> 8);
            this.positionY = activeBoss.positionY + (var1 * Game.sin256(this.ax * 60 + this.au) >> 8) - 40;
         }
      }

      if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
         Game.player.a(4, this.ar, 0, this);
      }
   }

   private void c(int var1, int var2, int var3, int var4) {
      this.spawnActor(61, 40, var1, 0, 0, 101, false);
      lastSpawnedActor.positionX = var2;
      lastSpawnedActor.positionY = var3;
      this.refreshBounds();
      lastSpawnedActor.au = lastSpawnedActor.collisionBounds[2] - lastSpawnedActor.collisionBounds[0];
      lastSpawnedActor.as = lastSpawnedActor.au / 20;
      lastSpawnedActor.ar = lastSpawnedActor.collisionBounds[3] - lastSpawnedActor.collisionBounds[1];
      lastSpawnedActor.moduleMask = new int[var4];
      lastSpawnedActor.paintPriority = 319;
   }

   // $VF: renamed from: aU () void
   private void spawnBossHazards() {
      int var1 = this.collisionBounds[0] + 25;
      int var2 = this.collisionBounds[1] - 25;
      int var3 = Game.randomRange(1, 6);
      int var4 = Game.randomRange(1, 6);
      int var5 = Game.randomRange(1, 6);

      while (var4 == var3) {
         var4 = Game.randomRange(1, 6);
      }

      while (var5 == var3 || var5 == var4) {
         var5 = Game.randomRange(1, 6);
      }

      for (int var6 = 0; var6 < 7; var6++) {
         this.spawnActor(73, 48, 2, 0, var2, 100, false);
         lastSpawnedActor.positionX = var1 + 51 * var6;
         if (var6 == var3) {
            lastSpawnedActor.positionY = var2;
         } else if (var6 == var4) {
            lastSpawnedActor.positionY = var2 - 100;
         } else if (var6 == var5) {
            lastSpawnedActor.positionY = var2 - 300;
         } else {
            lastSpawnedActor.positionY = var2 - 200;
         }

         lastSpawnedActor.flags |= 16;
         lastSpawnedActor.av = 1;
         lastSpawnedActor.changeActionState(102);
      }
   }

   // $VF: renamed from: aV () void
   private void updateBoss100() {
      this.decrementTimerA();
      switch (this.actionState) {
         case 0:
            if (rectsOverlapInclusive(Game.viewBounds, this.customBounds)) {
               activeBoss = this;
               bossInitialHealth = this.health;
               this.changeActionState(100);
            }
            break;
         case 3:
            Game.f(13);
            break;
         case 100:
            if (this.isAnimationEnding()) {
               this.positionX = Game.au.collisionBounds[2] + Game.au.collisionBounds[0] >> 1;
               this.positionY = (Game.au.collisionBounds[3] + Game.au.collisionBounds[1] >> 1) + 20;
               this.changeActionState(101);
            }
            break;
         case 101:
            if (this.animationId == 10 && this.isAnimationEnding()) {
               this.requestAnimation(3);
            }

            if (this.aw <= 0 && aP == null) {
               aP = new Actor[4];

               for (int var5 = 0; var5 < 4; var5++) {
                  aP[var5] = this.spawnActor(39, 1, 14, 0, 0, 302, false);
                  aP[var5].ax = var5;
                  aP[var5].aw = 1000;
                  aP[var5].au = 0;
                  aP[var5].ar = Game.player.positionX <= Game.au.collisionBounds[2] + Game.au.collisionBounds[0] >> 1 ? 1 : -1;
                  aP[var5].as = 0;
                  aP[var5].actionState = 101;
               }
            }
            break;
         case 102:
            for (int var4 = 0; var4 < 4; var4++) {
               if (segmentCrossesRectEdges(aP[var4].positionX, aP[var4].positionY, aQ[var4].positionX, aQ[var4].positionY, Game.player.collisionBounds)) {
                  Game.player.a(4, this.getStat(1), 0, this);
               }
            }
            break;
         case 103:
            this.accelerationY = 256;
            if (this.velocityY >= 1536) {
               this.velocityY = 1536;
            }

            this.resolveTileCollisions(false, true);
            if (this.grounded) {
               this.changeActionState(104);
            }
            break;
         case 104:
            if (this.animationId == 8 && this.isAnimationEnding()) {
               this.requestAnimation(6);
            }

            if (this.aw <= 0) {
               if (this.ay > 66) {
                  this.changeActionState(100);
               } else {
                  this.changeActionState(105);
               }
            }
            break;
         case 105:
            if (this.isAnimationEnding()) {
               this.positionX = Game.au.collisionBounds[2] + Game.au.collisionBounds[0] >> 1;
               this.positionY = (Game.au.collisionBounds[3] + Game.au.collisionBounds[1] >> 1) + 0;
               this.changeActionState(108);
            }
            break;
         case 108:
            this.velocityX = 0;
            if (this.aw >= 3000) {
               if (aP == null) {
                  this.requestAnimation(4);
                  aP = new Actor[6];
                  int var1 = Game.random.nextInt() % 2 == 0 ? 0 : 30;

                  for (int var2 = 0; var2 < 6; var2++) {
                     if (var2 < 6) {
                        aP[var2] = this.spawnActor(-102, 17, 0, 0, -40, 302, false);
                     }

                     aP[var2].flags |= 16;
                     aP[var2].ax = var2;
                     aP[var2].aw = 1000;
                     aP[var2].actionState = 110;
                     aP[var2].au = var1;
                     aP[var2].as = 0;
                     aP[var2].ar = this.getStat(2);
                  }
               }
            } else if (this.aw <= 0 && aP != null && aP[0].actionState == 112) {
               boolean var3 = false;

               for (int var6 = 0; var6 < 6; var6++) {
                  if (pointInRect(aP[var6].positionX, aP[var6].positionY, Game.au.collisionBounds)) {
                     var3 = true;
                     break;
                  }
               }

               if (!var3) {
                  if (this.ay > 33) {
                     this.changeActionState(100);
                  } else {
                     this.au = 0;
                     this.changeActionState(110);
                  }

                  for (int var7 = 0; var7 < 6; var7++) {
                     Game.removeActor(aP[var7]);
                     aP[var7] = null;
                  }

                  aP = null;
               }
            }
            break;
         case 110:
            if (this.isAnimationEnding()) {
               this.requestAnimation(10);
               this.positionX = Game.au.collisionBounds[2] + Game.au.collisionBounds[0] >> 1;
               this.positionY = Game.au.collisionBounds[1] + 60;
               this.changeActionState(111);
            }
            break;
         case 111:
            if (this.animationId == 10) {
               if (this.isAnimationEnding()) {
                  this.requestAnimation(3);
                  this.aw = 400;
               }
            } else if (this.animationId != 3 && this.animationId != 4) {
               if (this.aw <= 0) {
                  this.ax--;
                  if (this.ax > 0) {
                     this.changeActionStateInternal(111, true);
                     this.requestAnimation(3);
                     this.aw = 400;
                  } else {
                     this.changeActionState(100);
                  }
               }
            } else if (this.aw < 0) {
               if (this.animationId == 3) {
                  Game.au.spawnBossHazards();
                  this.requestAnimation(4);
                  this.aw = 400;
               } else {
                  this.requestAnimation(1);
                  if (this.ax == 2) {
                     this.aw = 5000;
                  } else {
                     this.aw = 6500;
                  }
               }
            }
      }

      if ((this.actionState == 103 || this.actionState == 104) && this.tryReceivePlayerWeaponDamage(false)) {
         this.requestAnimation(8);
      }

      if (this.actionState != 0) {
         this.ay = this.health * 100 / bossInitialHealth;
      }
   }

   // $VF: renamed from: d (boolean) void
   public static final void clearCombatEffects(boolean var0) {
      if (var0) {
         e(Game.bo);
      }

      if (cx != null) {
         for (int var1 = 0; var1 < 3; var1++) {
            if (cx[var1] != null) {
               Game.removeActor(cx[var1]);
            }
         }

         cx = null;
      }

      e(Game.bl);

      for (int var2 = 0; var2 < Game.updateActorCount; var2++) {
         Actor var3;
         if ((var3 = Game.updateActors[var2]) != null && var3 != Game.bl && (var3.type == 39 || var3.uid == -1 && var3.type != 35)) {
            Game.removeActor(var3);
         }
      }
   }

   // $VF: renamed from: aW () b
   private Actor spawnStraightProjectile() {
      this.spawnActor(19, 3, 17, this.facingLeft ? -12 : 12, -27, 302, false);
      lastSpawnedActor.facingLeft = this.facingLeft;
      lastSpawnedActor.changeActionState(101);
      lastSpawnedActor.scrollWithWorld = true;
      return lastSpawnedActor;
   }

   // $VF: renamed from: aX () void
   private void updateStraightProjectile() {
      switch (this.actionState) {
         case 3:
            Game.removeActor(this);
            break;
         case 101:
            if (!rectsOverlapInclusive(Game.viewBounds, this.collisionBounds)) {
               Game.removeActor(this);
            }
      }

      if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
         Game.player.a(4, 5, 0, this);
         this.changeActionState(3);
      }
   }

   // $VF: renamed from: aY () void
   private void aimHomingProjectile() {
      this.au = this.ac - this.positionX;
      this.ar = this.ad - this.positionY;
      this.dd = Game.e(this.au, this.ar);
      if (this.dd == 0) {
         this.dd = 1;
      }

      this.velocityX = this.dd == 1 ? 0 : this.au * 896 / this.dd;
      this.velocityY = this.dd == 1 ? 0 : this.ar * 896 / this.dd;
      this.au = this.au * -24 / this.dd;
      this.ar = this.ar * -24 / this.dd;
   }

   // $VF: renamed from: aZ () void
   private void updateHomingProjectile() {
      switch (this.actionState) {
         case 3:
            if (this.isAnimationEnding()) {
               Game.removeActor(this);
               return;
            }

            if (this.s == Game.player) {
               this.ba();
            }
            break;
         case 101:
            this.decrementTimerA();
            this.decrementTimerB();
            if (this.aw <= 0) {
               this.changeActionState(3);
               return;
            }

            if (this.t != null && (this.t.velocityX != 0 || this.t.velocityY != 0)) {
               this.ac = this.t.positionX;
               this.ad = this.t.positionY - 20;
               this.aimHomingProjectile();
            }

            if (this.s == Game.player) {
               this.ba();
               return;
            }

            if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
               Game.player.a(4, 5, 0, this);
               this.changeActionState(3);
               return;
            }

            if (this.f(this.collisionBounds)) {
               this.changeActionState(3);
               return;
            }
      }
   }

   private void ba() {
      Actor var1 = Game.player;
      if (Game.player.isDamageable() && rectsOverlapInclusive(var1.collisionBounds, this.collisionBounds)) {
         var1.applyDamageWithReaction(10);
         this.changeActionState(3);
         this.velocityX = 0;
      }
   }

   // $VF: renamed from: bb () b
   private Actor spawnHomingProjectile() {
      Actor var1;
      (var1 = this.spawnActor(18, 14, 5, this.facingLeft ? -27 : 27, -43, 302, !this.facingLeft)).ac = Game.player.boundsCenterX;
      var1.ad = Game.player.boundsCenterY;
      var1.changeActionState(101);
      var1.flags |= 16;
      return var1;
   }

   // $VF: renamed from: bc () void
   private void updateHazardArea78() {
      if (this.aB != -1 && this.s == null) {
         this.s = Game.findActorByUid(this.aB);
      }

      if (this.s != null && this.s.type == 49) {
         if ((this.s.flags & 16) != 0) {
            this.positionY = this.positionY - this.s.au;
            this.ar = this.ar + this.s.au;
            if (this.positionY >= Game.viewBounds[3] - 4) {
               int var1 = this.positionY - Game.viewBounds[3] + 4;
               this.ar += var1;
               this.positionY = Game.viewBounds[3] - 4;
            }

            if (Game.levelIndex == 5) {
               Game.hazardCameraOffsetY = this.positionY - Game.cameraY;
            }

            this.ac = this.s.au;
         }

         this.refreshBounds();
      }

      if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
         Game.player.a(4, 500, 0, this);
         Game.player.positionY = this.collisionBounds[1];
      }

      for (int var3 = 0; var3 < Game.updateActorCount; var3++) {
         Actor var2;
         if ((var2 = Game.updateActors[var3]) != null
            && (var2.flags & 1024) == 0
            && var2.actionState != 7
            && var2.actionState != 3
            && var2.actionState != 5
            && var2.actionState != 6) {
            if ((var2.isDamageable() || var2.type == 70) && var2.collisionBounds != null && rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds)) {
               var2.applyDamageWithReaction(500);
            } else if (var2.type == 61) {
               var2.damageDestructibleCells(this.collisionBounds, false, 61);
            }
         }
      }
   }

   // $VF: renamed from: bd () void
   private void updateCollapsingPlatform() {
      switch (this.actionState) {
         case 0:
            this.N = true;
            this.O = true;
            this.P = true;
            if (this.isAnimationEnding()) {
               this.actionState = 102;
               this.requestAnimation(14);
            }
            break;
         case 101:
            if (this.isAnimationEnding()) {
               Game.removeActor(this);
               this.N = false;
               this.O = false;
               this.P = false;
               return;
            }
         case 102:
      }
   }

   // $VF: renamed from: be () void
   private void updateLinkedGate() {
      if (this.B != -1 && this.v == null) {
         this.v = Game.findActorByUid(this.B);
      }

      if (this.C != -1 && this.w == null) {
         this.w = Game.findActorByUid(this.C);
      }

      switch (this.actionState) {
         case 0:
            if (this.v != null) {
               if (this.w == null) {
                  if (this.v.actionState == 101) {
                     this.actionState = 102;
                     return;
                  }
               } else if (this.v.actionState == 101 && this.w.actionState == 101) {
                  this.actionState = 102;
                  return;
               }
            }
            break;
         case 102:
            this.O = true;
            this.P = true;
            this.h(this.ay);
            if (this.dh > 0) {
               this.dh += -4;
               if (this.ay >= 4) {
                  this.velocityY = this.ay == 4 ? -1024 : 1024;
                  return;
               }

               this.velocityX = this.ay == 1 ? -1024 : 1024;
               return;
            }

            this.velocityX = this.velocityY = 0;
            this.actionState = 105;
            return;
         case 103:
            this.velocityY = 0;
            this.velocityX = 0;
            this.setCollisionRectEmpty(this.customBounds, true);
            return;
         case 104:
            this.velocityY = 0;
            this.velocityX = 0;
            this.h(this.ay);
            break;
         case 105:
            if (this.v != null) {
               if (this.w == null) {
                  if (this.v.actionState != 101) {
                     this.actionState = 106;
                     return;
                  }
               } else if (this.v.actionState != 101 || this.w.actionState != 101) {
                  this.actionState = 106;
                  return;
               }
            }
            break;
         case 106:
            if (this.ay >= 4) {
               if (this.positionY != this.av) {
                  this.positionY = this.av;
                  this.dh = 20 * this.variantData;
                  this.actionState = 107;
               }
            } else if (this.positionX != this.as) {
               this.positionX = this.as;
               this.dh = 20 * this.variantData;
               this.actionState = 107;
            }

            if (this.positionY == this.av && this.positionX == this.as) {
               this.actionState = 107;
            }

            this.h(this.ay);
            return;
         case 107:
            this.actionState = 0;
            this.O = false;
            return;
      }
   }

   public final void h(int var1) {
      this.refreshBounds();
      if (var1 == 1) {
         cF[0] = this.customBounds[0];
      } else {
         cF[0] = this.collisionBounds[0];
      }

      if (var1 == 2) {
         cF[2] = this.customBounds[2];
      } else {
         cF[2] = this.collisionBounds[2];
      }

      if (var1 == 4) {
         cF[1] = this.customBounds[1];
      } else {
         cF[1] = this.collisionBounds[1];
      }

      if (var1 == 8) {
         cF[3] = this.customBounds[3];
      } else {
         cF[3] = this.collisionBounds[3];
      }

      if (var1 != 9) {
         this.setCollisionRectEmpty(this.customBounds, true);
      }

      this.setCollisionRectEmpty(cF, false);
   }

   // $VF: renamed from: bf () void
   private void updateBreakableBridge() {
      switch (this.actionState) {
         case 0:
            this.ax = this.ax + Game.tickDeltaMs;
            if (this.ax >= 1660) {
               this.ax = 0;
            }

            this.setCollisionRectEmpty(this.collisionBounds, false);
            return;
         case 100:
            this.aw = this.aw + Game.tickDeltaMs;
            if (this.aw >= (this.animationId == 0 ? 498 : 1660)) {
               this.actionState = 101;
               return;
            }
            break;
         case 101:
            Game.removeActor(this);
      }
   }

   // $VF: renamed from: bg () void
   private void updateAbilityPickup() {
      if (this.animationId == 0 && rectsOverlapInclusive(this.collisionBounds, Game.player.collisionBounds)) {
         bF = true;
         if (!bw) {
            bw = true;
            b(0, 60, true);
         }

         Game.removeActor(this);
      } else if (this.animationId == 1) {
         if (Game.player.positionX >= this.positionX) {
            bF = false;
            Game.removeActor(this);
         }
      } else {
         switch (this.actionState) {
            case 0:
               if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
                  this.actionState = 102;
                  if (this.animationId == 35) {
                     if (!bs) {
                        bs = true;
                        b(0, 53, false);
                     }

                     Game.aT = 1;
                  } else if (this.animationId == 2) {
                     if (!bu && Game.levelIndex == 0) {
                        b(0, 61, false);
                        bu = true;
                        Game.magic = Game.maxMagic;
                     }

                     Game.aT = 1;
                  }

                  if (Game.player.actionState == 0 || Game.player.actionState == 2) {
                     Game.player.setAnimationImmediate(25);
                  }

                  Game.k = true;
                  e(Game.bo);
                  Game.player.stopMotion();
                  return;
               }
               break;
            case 100:
               this.velocityY = -2048;
               int var3 = Game.player.positionY - Game.player.collisionBounds[3] + Game.player.collisionBounds[1] - 15;
               if (this.positionY < var3) {
                  this.positionY = var3;
                  this.velocityY = 0;
               }

               if (!Game.aS) {
                  Game.removeActor(this);
                  return;
               }
               break;
            case 102:
               this.dd = Game.e(Game.player.fixedX - this.fixedX, Game.player.fixedY - this.fixedY);
               if (this.dd <= 40960) {
                  this.velocityX = this.dd == 0 ? 0 : (Game.player.fixedX - this.fixedX) * 2048 / this.dd;
                  this.velocityY = this.dd == 0 ? 0 : (Game.player.fixedY - this.fixedY) * 2048 / this.dd;
               }

               if (this.dd < 2048) {
                  this.positionX = Game.player.positionX;
                  this.positionY = Game.player.positionY;
                  this.velocityX = 0;
                  this.velocityY = 0;
                  this.facingLeft = Game.player.facingLeft;
                  Game.F = Game.player.positionX;
                  Game.G = Game.player.positionY;
                  Game.f(3);
                  this.actionState = 100;
                  e(Game.bo);
                  return;
               }
               break;
            case 103:
               this.velocityY = -2048;
               int var1 = Game.player.positionY - Game.player.collisionBounds[3] + Game.player.collisionBounds[1] - 15;
               if (this.positionY < var1) {
                  this.positionY = var1;
                  this.velocityY = 0;
                  Game.randomRange(8, 11);
                  Game.removeActor(this);
               }
            case 104:
         }
      }
   }

   public final boolean g(Actor var1) {
      if (var1 != null) {
         var1.refreshBounds();
         if (rectsOverlapInclusive(var1.collisionBounds, this.collisionBounds)) {
            return true;
         }
      }

      return false;
   }

   public final boolean h(Actor var1) {
      return var1 != null && (var1.flags & 1024) != 0;
   }

   public final boolean a(int var1, int var2, boolean var3) {
      if (var2 == -1) {
         return var3;
      } else {
         return var1 != -1 && Game.bD[var1] == -99 ? true : var1 == 0;
      }
   }

   // $VF: renamed from: bh () void
   private void updateLinkedTrigger() {
      if (this.y != -1 && this.s == null) {
         this.s = Game.findActorByUid(this.y);
         if (this.s != null) {
            this.at = this.s.persistenceIndex;
         } else {
            this.at = -1;
            if (this.aw == 4) {
               this.y = -1;
            }
         }
      } else if (this.s == null) {
         this.at = -1;
      }

      if (this.z != -1 && this.t == null) {
         this.t = Game.findActorByUid(this.z);
         if (this.t != null) {
            this.ar = this.t.persistenceIndex;
         } else {
            this.ar = -1;
         }
      } else if (this.t == null) {
         this.ar = -1;
      }

      if (this.A != -1 && this.u == null) {
         this.u = Game.findActorByUid(this.A);
         if (this.u != null) {
            this.av = this.u.persistenceIndex;
         } else {
            this.av = -1;
         }
      } else if (this.u == null) {
         this.av = -1;
      }

      if (this.ax == 0) {
         if (this.B != -1 && this.v == null) {
            this.v = Game.findActorByUid(this.B);
         }

         if (this.C != -1 && this.w == null) {
            this.w = Game.findActorByUid(this.C);
         }

         if (this.D != -1 && this.x == null) {
            this.x = Game.findActorByUid(this.D);
         }
      }

      boolean var1 = false;
      switch (this.aw) {
         case 0:
            if (rectsOverlapInclusive(this.collisionBounds, Game.au.collisionBounds)) {
               var1 = true;
               this.flags |= 16;
            } else {
               this.flags &= -17;
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
            if (this.a(this.at, this.y, false) || this.a(this.ar, this.z, false) || this.a(this.av, this.A, false)) {
               var1 = true;
            }
            break;
         case 4:
            if (this.a(this.at, this.y, true) && this.a(this.ar, this.z, true) && this.a(this.av, this.A, true)) {
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
         switch (this.au) {
            case 0:
               if (this.ax == 0) {
                  this.enableLinkedActor(this.v);
                  this.enableLinkedActor(this.w);
                  this.enableLinkedActor(this.x);
                  this.removeLinkedTrigger();
               } else {
                  this.a((byte)1);
                  this.H();
               }
               break;
            case 1:
               this.a((byte)0);
               break;
            case 2:
               this.forceLinkedActorUpdate(this.v);
               this.forceLinkedActorUpdate(this.w);
               this.forceLinkedActorUpdate(this.x);
               break;
            case 3:
               this.disableLinkedActor(this.v);
               this.disableLinkedActor(this.w);
               this.disableLinkedActor(this.x);
               this.removeLinkedTrigger();
               break;
            case 4:
               this.markLinkedActorInactive(this.v);
               this.markLinkedActorInactive(this.w);
               this.markLinkedActorInactive(this.x);
               this.removeLinkedTrigger();
               break;
            case 5:
               this.removeLinkedActor(this.v);
               this.removeLinkedActor(this.w);
               this.removeLinkedActor(this.x);
               this.removeLinkedTrigger();
               break;
            case 6:
               this.clearRespawnFlag(this.v);
               this.clearRespawnFlag(this.w);
               this.clearRespawnFlag(this.x);
               this.removeLinkedTrigger();
               break;
            case 7:
               this.activateLinkedActorAnimation31(this.v);
               this.activateLinkedActorAnimation31(this.w);
               this.activateLinkedActorAnimation31(this.x);
               break;
            case 8:
               this.o(this.B, this.C);
         }
      } else {
         switch (this.au) {
            case 0:
               this.H();
               return;
            case 1:
               if (this.ax != 0) {
                  this.b((byte)0);
                  return;
               }
               break;
            case 2:
               this.disableLinkedActor(this.v);
               this.disableLinkedActor(this.w);
               this.disableLinkedActor(this.x);
         }
      }
   }

   // $VF: renamed from: i (b) void
   public final void activateLinkedActorAnimation31(Actor var1) {
      if (var1 != null) {
         var1.actionState = 101;
         var1.requestAnimation(31);
      }
   }

   // $VF: renamed from: j (b) void
   public final void clearRespawnFlag(Actor var1) {
      if (var1 != null) {
         var1.flags &= -8193;
      }
   }

   // $VF: renamed from: G () void
   public final void removeLinkedTrigger() {
      this.s = null;
      this.t = null;
      this.u = null;
      this.v = null;
      this.w = null;
      this.x = null;
      Game.removeActor(this);
   }

   public final void a(byte var1) {
      if (this.B != -1 && this.as == -1) {
         int var2 = Game.findScriptIndex(this.B);
         Game.ck[Game.cj] = var2;
         Game.cl[Game.cj] = var1;
         Game.cj++;
         Game.startScript(var2);
         this.as = 1;
      }
   }

   public final void H() {
      if (this.as == 1) {
         int var1 = Game.findScriptIndex(this.B);
         if (Game.scriptTicks[var1] == -2) {
            Game.stopAndRewindScript(var1);
            this.c((byte)1);
         }
      }
   }

   public final void b(byte var1) {
      if (this.B != -1 && this.as == 1) {
         Game.stopAndRewindScript(Game.findScriptIndex(this.B));
         this.c(var1);
      }
   }

   public final void c(byte var1) {
      Game.cj--;
      this.as = -1;
      if (var1 == 1) {
         Game.removeActor(this);
      }
   }

   // $VF: renamed from: k (b) void
   public final void forceLinkedActorUpdate(Actor var1) {
      if (var1 != null) {
         var1.flags &= -33;
         if ((var1.flags & 16) == 0) {
            var1.flags |= 16;
         }
      }
   }

   // $VF: renamed from: l (b) void
   public final void enableLinkedActor(Actor var1) {
      if (var1 != null) {
         var1.flags &= -33;
         if (var1.actionState != 5) {
            var1.flags &= -129;
         }

         if (var1.type == 20) {
            var1.flags |= 16;
         }

         if (var1.type == 52) {
            var1.spawnActor(39, 1, 28, 0, 0, 302, false);
         }
      }
   }

   // $VF: renamed from: m (b) void
   public final void disableLinkedActor(Actor var1) {
      if (var1 != null) {
         if (var1.type == 49) {
            var1.removeCameraController();
         }

         var1.flags |= 32;
         var1.flags &= -17;
      }
   }

   // $VF: renamed from: n (b) void
   public final void markLinkedActorInactive(Actor var1) {
      if (var1 != null) {
         var1.flags |= 1024;
      }
   }

   // $VF: renamed from: o (b) void
   public final void removeLinkedActor(Actor var1) {
      if (var1 != null && var1.type == 49) {
         var1.removeCameraController();
      } else {
         Game.removeActor(var1);
      }
   }

   // $VF: renamed from: bi () void
   private void updateCameraController() {
      if (!Game.ai) {
         if (this.aB != -1 && this.v == null) {
            this.v = Game.findActorByUid(this.aB);
            if (this.v != null) {
               this.at = this.v.persistenceIndex;
            }
         }

         if (this.v != null && Game.bD[this.at] == -99) {
            this.removeCameraController();
         } else {
            if (rectsOverlapStrict(Game.player.collisionBounds, this.collisionBounds)) {
               Game.cameraControllerActive = true;
               this.flags |= 16;
            } else {
               if (Game.activeCameraControllerUid != this.uid && Game.cameraControllerActive) {
                  return;
               }

               Game.cameraControllerActive = false;
               if (Game.activeCameraControllerUid == this.uid) {
                  Game.activeCameraControllerUid = -1;
                  if (this.aw >= 4
                     && (
                        (this.ax & 1) != 0 && Game.player.collisionBounds[0] <= this.collisionBounds[0]
                           || (this.ax & 2) != 0 && Game.player.collisionBounds[2] >= this.collisionBounds[2]
                           || (this.ax & 4) != 0 && Game.player.collisionBounds[1] <= this.collisionBounds[1]
                           || (this.ax & 8) != 0 && Game.player.collisionBounds[3] <= this.collisionBounds[3]
                     )) {
                     this.removeCameraController();
                  }
               }

               this.flags &= -17;
            }

            Game.cameraControllerMode = this.aw;
            if (Game.cameraControllerMode == 0 && this.v == null) {
               Game.cameraControllerActive = false;
               Game.activeCameraControllerUid = -1;
            }

            if (!Game.cameraControllerActive) {
               Game.activeCameraControllerUid = -1;
            } else {
               switch (this.aw) {
                  case 0:
                  case 3:
                     this.v.refreshBounds();
                     int var1 = Game.player.positionX - 10 < this.v.positionX - 10 ? Game.player.positionX - 10 : this.v.positionX - 10;
                     int var2 = Game.player.positionX + 10 > this.v.positionX + 10 ? Game.player.positionX + 10 : this.v.positionX + 10;
                     int var3 = Game.player.positionY - 50 < this.v.positionY - 50 ? Game.player.positionY - 50 : this.v.positionY - 50;
                     int var4 = Game.player.positionY > this.v.positionY ? Game.player.positionY : this.v.positionY;
                     int var5 = var1 + (var2 - var1 >> 1);
                     int var6 = var3 + (var4 - var3 >> 1);
                     if (Game.player.velocityX != 0 || Game.player.velocityY != 0 || this.aw == 3) {
                        Game.cameraTargetX = var5 - 120;
                        Game.cameraTargetY = var6 - 160;
                     }

                     if (Game.cameraTargetX + 240 > Game.player.positionX + 10
                        && Game.cameraTargetX < Game.player.positionX - 10
                        && Game.cameraTargetY + 320 > Game.player.positionY
                        && Game.cameraTargetY < Game.player.positionY - 50) {
                        Game.activeCameraControllerUid = this.uid;
                     } else if (0 == this.aw) {
                        Game.cameraControllerActive = false;
                        Game.activeCameraControllerUid = -1;
                     } else {
                        Game.activeCameraControllerUid = this.uid;
                        if (Game.cameraTargetX + 240 <= Game.player.positionX + 10) {
                           Game.cameraTargetX = Game.player.positionX + 10 - 240;
                        } else if (Game.cameraTargetX >= Game.player.positionX - 10) {
                           Game.cameraTargetX = Game.player.positionX - 10;
                        } else if (Game.cameraTargetY + 320 <= Game.player.positionY) {
                           Game.cameraTargetY = Game.player.positionY - 320;
                        } else if (Game.cameraTargetY >= Game.player.positionY - 50) {
                           Game.cameraTargetY = Game.player.positionY - 50;
                        }
                     }
                     break;
                  case 1:
                     if (Game.player.velocityY > 0) {
                        Game.cameraTargetY = Game.player.positionY - 160;
                        Game.activeCameraControllerUid = this.uid;
                     } else {
                        Game.cameraControllerActive = false;
                        Game.activeCameraControllerUid = -1;
                     }
                  case 2:
                     if ((this.ax & 1) != 0) {
                        Game.R = this.collisionBounds[0];
                     }

                     if ((this.ax & 2) != 0) {
                        Game.S = this.collisionBounds[2];
                     }

                     if ((this.ax & 4) != 0) {
                        Game.T = this.collisionBounds[1];
                     }

                     if ((this.ax & 8) != 0) {
                        Game.U = this.collisionBounds[3];
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
                     Game.player.refreshBounds();
                     Game.activeCameraControllerUid = this.uid;
                     boolean var7 = false;
                     int var8 = this.au;
                     if (this.v != null) {
                        var8 = Math.abs(this.v.velocityX >> 8);
                     }

                     worldScrollPixelsPerTick = var8;
                     if (worldScrollPixelsPerTick == 12 && !isPlayerMeleeAnimation() && !isPlayerSpecialAnimation()) {
                        Game.player.requestAnimation(0);
                     }

                     if ((this.ax & 1) != 0) {
                        Game.cameraTargetX = Game.cameraX - var8;
                        if (Game.player.collisionBounds[0] > this.collisionBounds[0]) {
                           if (Game.player.positionX > Game.viewBounds[2] - 10) {
                              if (Game.player.tileAtLeftSide <= 21) {
                                 Game.player.positionX = Game.viewBounds[2] - 10;
                                 if (3 != Game.levelModes[Game.levelIndex] && Game.player.velocityX > 0) {
                                    Game.player.velocityX = 0;
                                 }
                              } else if (Game.player.collisionBounds[0] >= Game.viewBounds[2]) {
                                 var7 = true;
                              } else if (3 != Game.levelModes[Game.levelIndex]) {
                                 Game.player.velocityX = 0;
                              }
                           } else if (Game.player.positionX < Game.cameraX + 10) {
                              Game.player.positionX = Game.cameraX + 10;
                           }

                           Game.cameraScrollHorizontal = true;
                        }
                     }

                     if ((this.ax & 2) != 0 && Game.player.collisionBounds[2] < this.collisionBounds[2]) {
                        Game.cameraTargetX = Game.cameraX + var8;
                        if (Game.player.positionX < Game.viewBounds[0] + 30) {
                           if (Game.player.tileAtRightSide <= 21) {
                              Game.player.positionX = Game.viewBounds[0] + 30;
                              if (3 != Game.levelModes[Game.levelIndex] && Game.player.velocityX < 0) {
                                 Game.player.velocityX = 0;
                              }
                           } else if (Game.player.positionX <= Game.viewBounds[0]) {
                              var7 = true;
                           } else {
                              Game.player.velocityX = 0;
                           }
                        } else if (Game.player.positionX > Game.viewBounds[2] - 10) {
                           Game.player.positionX = Game.viewBounds[2] - 10;
                        }

                        Game.cameraScrollHorizontal = true;
                     }

                     if ((this.ax & 4) != 0 && Game.player.collisionBounds[1] > this.collisionBounds[1]) {
                        Game.cameraTargetY = Game.cameraY - var8;
                        if (Game.player.positionY > Game.viewBounds[3]) {
                           var7 = true;
                        } else if (Game.player.positionY < Game.viewBounds[1]) {
                           Game.player.positionY = Game.viewBounds[1];
                        }

                        Game.cameraScrollHorizontal = false;
                     }

                     if ((this.ax & 8) != 0 && Game.player.collisionBounds[3] < this.collisionBounds[3]) {
                        Game.cameraTargetY = Game.cameraY + var8;
                        if (Game.player.positionY < Game.viewBounds[1]) {
                           var7 = true;
                        } else if (Game.player.positionY > Game.viewBounds[3]) {
                           Game.player.positionY = Game.viewBounds[3];
                        }

                        Game.cameraScrollHorizontal = false;
                     }

                     if (var7) {
                        Game.player.a(4, 10000, 0, this);
                        Game.health = -100;
                        this.removeCameraController();
                     }
               }
            }
         }
      }
   }

   // $VF: renamed from: bj () void
   private void removeCameraController() {
      this.v = null;
      this.aB = -1;
      Game.removeActor(this);
      Game.cameraControllerActive = false;
      Game.activeCameraControllerUid = -1;
   }

   // $VF: renamed from: bk () void
   private void updateLinkedPlatform() {
      if (this.aB != -1 && this.attachedActor == null) {
         this.setAttachedActor(Game.findActorByUid(this.aB));
      }

      boolean var1 = false;
      this.d(0, this.collisionBounds);
      this.refreshBounds();

      for (int var3 = 0; var3 < Game.updateActorCount; var3++) {
         Actor var2;
         if ((var2 = Game.updateActors[var3]) != null && var2 != this) {
            var2.refreshBounds();
            if (var2.supportActor == this) {
               var2.positionY = this.collisionBounds[1] - 1;
               var2.grounded = true;
               cr[1] = var2.collisionBounds[1];
               cr[0] = var2.collisionBounds[0];
               cr[3] = var2.collisionBounds[3] + 2;
               cr[2] = var2.collisionBounds[2];
               if (!rectsOverlapInclusive(this.collisionBounds, cr)) {
                  var2.supportActor = null;
                  this.supportedActor = null;
               }

               if (var2.supportActor == this) {
                  var1 = true;
               }
            }
         }
      }

      if (playerPlatform == this || var1) {
         if (this.positionY < this.av - 7) {
            this.positionY++;
         }

         if (this.attachedActor.actionState != 103) {
            this.attachedActor.actionState = 102;
         }
      }

      if (playerPlatform != this && !var1) {
         if (this.positionY > this.ar) {
            this.positionY--;
         }

         this.attachedActor.actionState = 106;
      }
   }

   // $VF: renamed from: bl () void
   private void updateOneShotEffect() {
      if (this.isAnimationEnding()) {
         Game.removeActor(this);
      }
   }

   // $VF: renamed from: bm () void
   private void updateHazardType73() {
      this.bU = false;
      this.bV = null;

      for (int var2 = 0; var2 < Game.updateActorCount; var2++) {
         Actor var1;
         if ((var1 = Game.updateActors[var2]) != null && var1.type == 78 && rectsOverlapInclusive(this.collisionBounds, var1.collisionBounds)) {
            this.bU = true;
            this.bV = var1;
            this.grounded = true;
            break;
         }
      }

      label322:
      switch (this.actionState) {
         case 0:
            switch (this.variantData) {
               case 0:
                  if (this.bU) {
                     this.changeActionState(107);
                  } else if (this.variantData == 0) {
                     this.d(0, this.collisionBounds);
                  }
               default:
                  break label322;
            }
         case 100:
            if (this.variantData == 5 || this.variantData == 4 || this.variantData == 2) {
               this.positionX = Game.cameraX + this.ac;
               this.ad += -4;
               this.positionY = this.ad + Game.cameraY;
            }

            if (!rectsOverlapInclusive(Game.viewBounds, this.collisionBounds)) {
               this.changeActionState(101);
               this.stopMotion();
            }
            break;
         case 101:
            if (this.variantData == 5 || this.variantData == 4) {
               this.positionX = Game.cameraX + this.ac;
            } else if (this.variantData == 2) {
               this.positionX = this.ar;
            }

            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(102);
               this.positionY = Game.viewBounds[1] + this.collisionBounds[1] - this.collisionBounds[3];
               this.velocityY = 2048;
               if (this.variantData == 4) {
                  if (this.au == 0) {
                     int var3 = Game.randomRange(0, 3);
                     cm[var3] = Game.player.positionX;
                     if (var3 == 0) {
                        var3 = Game.randomRange(0, 2);
                        cm[1] = cm[0] + (var3 == 0 ? -100 : 100);
                        cm[2] = cm[0] + (var3 == 0 ? 100 : -100);
                     } else if (var3 == 1) {
                        var3 = Game.randomRange(0, 2);
                        cm[0] = cm[1] + (var3 == 0 ? -100 : 100);
                        cm[2] = cm[1] + (var3 == 0 ? 100 : -100);
                     } else if (var3 == 2) {
                        var3 = Game.randomRange(0, 2);
                        cm[0] = cm[2] + (var3 == 0 ? -100 : 100);
                        cm[1] = cm[2] + (var3 == 0 ? 100 : -100);
                     }
                  }

                  this.positionX = cm[this.au];
               }
            }
            break;
         case 102:
            this.accelerationY = 768;
            if (this.velocityY > 2048) {
               this.velocityY = 2048;
            }

            if (Game.levelIndex == 8) {
               if (this.collisionBounds[1] > Game.au.collisionBounds[1]) {
                  this.resolveTileCollisions(false, false);
               }
            } else {
               this.resolveTileCollisions(true, true);
            }

            if (this.av == 1) {
               if (Game.levelIndex == 8) {
                  if (this.velocityY > 0 && (this.tileAtFeet >= 15 && this.tileAtFeet != 15 || this.wallOnLeft || this.wallOnRight)) {
                     this.changeActionState(103);
                  }
               } else if (this.velocityY > 0 && (this.tileAtFeet >= 15 || this.wallOnLeft || this.wallOnRight)) {
                  this.changeActionState(103);
               }
            } else if (this.bU) {
               this.stopMotion();
               this.changeActionState(108);
            }
            break;
         case 103:
            Game.removeActor(this);
            return;
         case 104:
         case 105:
            if (!this.bU) {
               this.resolveTileCollisions(true, true);
               if (this.grounded) {
                  this.stopMotion();
                  this.decrementTimerA();
                  if (this.aw <= 0) {
                     this.velocityX = this.facingLeft ? -this.at : this.at;
                  }

                  this.applySlopeVelocity();
               } else {
                  this.velocityX = 0;
                  this.accelerationY = 768;
                  if (this.velocityY > 2048) {
                     this.velocityY = 2048;
                     this.accelerationY = 0;
                  }

                  this.aw = 100;
               }
            }

            if (this.ax < 0) {
               if (this.wallOnLeft) {
                  this.facingLeft = false;
               }

               if (this.wallOnRight) {
                  this.facingLeft = true;
               }

               if (!rectsOverlapInclusive(Game.au.collisionBounds, this.collisionBounds)) {
                  Game.removeActor(this.v);
                  this.v = null;
                  Game.removeActor(this);
                  return;
               }
            }

            if (this.supportActor == null) {
               if (aR != null
                  && aR[0] != null
                  && 2 != Game.levelModes[Game.levelIndex]
                  && aR[0].animationId == 9
                  && rectsOverlapInclusive(aR[0].collisionBounds, this.collisionBounds)
                  && this.velocityY > 0
                  && this.collisionBounds[3] - (this.velocityY >> 8) < aR[0].collisionBounds[1]
                  && (this.collisionBounds[2] >= aR[0].collisionBounds[0] || this.collisionBounds[0] <= aR[0].collisionBounds[2])) {
                  this.supportActor = aR[0];
                  this.positionY = this.supportActor.collisionBounds[1] + (this.collisionBounds[1] - this.collisionBounds[3] >> 1);
                  this.velocityY = 0;
                  this.accelerationY = 0;
                  this.velocityX = this.facingLeft ? -this.at : this.at;
                  aR[0].s = this;
               }
            } else {
               if (aR != null && aR[0] != null && aR[0].animationId != 9 && aR[0].s == this) {
                  this.supportActor.s = null;
                  this.supportActor = null;
               }

               if (this.supportActor != null && this.supportActor.type == 39) {
                  this.positionY = this.supportActor.collisionBounds[1] + (this.collisionBounds[1] - this.collisionBounds[3] >> 1);
                  this.velocityY = 0;
                  this.accelerationY = 0;
                  this.velocityX = this.facingLeft ? -this.at : this.at;
                  if ((aR == null || aR[0] == null || aR[0].animationId != 9 || !rectsOverlapInclusive(aR[0].collisionBounds, this.collisionBounds))
                     && this.supportActor == aR[0]) {
                     this.supportActor.s = null;
                     this.supportActor = null;
                  }
               }
            }

            this.decrementTimerB();
            break;
         case 107:
            this.decrementTimerA();
            if (this.bU && this.bV != null) {
               this.bn();
            }

            if (rectsOverlapInclusive(Game.player.collisionBounds, this.customBounds)) {
               this.changeActionState(108);
               this.velocityX = this.at;
            }
            break;
         case 108:
            this.decrementTimerA();
            if (this.bU && this.bV != null) {
               this.bn();
               if (this.wallOnLeft || this.wallOnRight) {
                  this.velocityX = -this.velocityX;
               }
            } else {
               this.changeActionState(102);
            }
      }

      if (this.av == 1 && this.bU && this.bV != null && this.collisionBounds[1] > this.bV.collisionBounds[1]) {
         Game.removeActor(this.v);
         this.v = null;
         Game.removeActor(this);
      } else {
         if (this.bU && this.bV != null) {
            this.aA = this.bV.collisionBounds[1];
         } else {
            this.aA = Game.U;
         }

         this.refreshBounds();
         if (this.actionState != 100 && this.actionState != 101 && (this.flags & 128) == 0) {
            if (this.av == 1 && rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
               if (this.variantData == 4 || this.variantData == 2) {
                  Game.player.a(4, 20, 0, this);
                  this.changeActionState(103);
                  Game.shakeXTicks = 0;
                  Game.shakeYTicks = 0;
                  return;
               }

               if (activeBoss != null && activeBoss.type == 100) {
                  Game.player.a(4, activeBoss.getStat(3), 0, this);
               } else {
                  Game.player.a(4, 500, 0, this);
               }
            }

            this.bo();
         }

         this.resolveActorSeparation();
      }
   }

   private void bn() {
      int var1;
      var1 = (var1 = -this.aw / 500 % 4) < 2 ? var1 : 4 - var1;
      this.positionY = this.bV.collisionBounds[1] + 0 + --var1;
      this.resolveTileCollisions(true, false);
      this.d(0, this.collisionBounds);
   }

   private void bo() {
      for (int var1 = 0; var1 < Game.updateActorCount; var1++) {
         Actor var2;
         if ((var2 = Game.updateActors[var1]) != null
            && var2 != this
            && (var2.flags & 1024) == 0
            && var2.actionState != 7
            && var2.actionState != 3
            && var2.actionState != 5
            && var2.actionState != 6) {
            if (this.av == 1
               && (var2.isDamageable() || var2.type == 70)
               && var2.collisionBounds != null
               && rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds)) {
               var2.applyDamageWithReaction(20);
            } else if (this.av == 1 && var2.type == 75 && var2.collisionBounds != null && rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds)) {
               if (var2.actionState == 0) {
                  var2.variantData = 5;
                  var2.activateTrap75();
               }
            } else if (this.av == 1
               && var2.type == 73
               && (this.actionState == 105 || this.actionState == 104)
               && (var2.actionState == 105 || var2.actionState == 104)
               && rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds)) {
               this.changeActionState(103);
               var2.changeActionState(103);
            } else if (this.av == 0
               && var2.type == 73
               && (this.actionState == 107 || this.actionState == 108)
               && (var2.actionState == 107 || var2.actionState == 108)
               && rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds)) {
               this.velocityX = -this.velocityX;
               var2.velocityX = -var2.velocityX;
            } else if (this.av == 1 && var2.type == 79) {
               cr[1] = this.collisionBounds[1] - 10;
               cr[3] = this.collisionBounds[3] + 10;
               cr[0] = this.collisionBounds[0] - 10;
               cr[2] = this.collisionBounds[2] + 10;
               if (rectsOverlapInclusive(var2.collisionBounds, cr)) {
                  var2.damageDestructibleCells(cr, false, 79);
               }
            }
         }
      }
   }

   // $VF: renamed from: bp () void
   private void updateHazardSpawner() {
      switch (this.actionState) {
         case 0:
            if (this.variantData == 5 || this.variantData == 4 || this.variantData == 2) {
               this.changeActionState(100);
               this.as = 0;
               return;
            }

            if (rectsOverlapInclusive(Game.player.collisionBounds, this.customBounds)) {
               this.changeActionState(100);
               this.as = 0;
               return;
            }
            break;
         case 100:
            if (this.variantData == 4) {
               this.k(this.aF, this.as);
               this.as++;
               if (this.as >= 3) {
                  this.aw = this.ax;
                  this.changeActionState(101);
                  return;
               }

               this.aw = 400;
               this.changeActionState(102);
               return;
            }

            this.u(this.aF);
            this.aw = this.ax;
            this.changeActionState(101);
            return;
         case 101:
            this.decrementTimerA();
            if (this.ax > 0 && this.aw <= 0) {
               this.changeActionState(0);
            }
            break;
         case 102:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(100);
               return;
            }
      }
   }

   private void k(int var1, int var2) {
      Actor var3;
      (var3 = this.u(var1)).au = var2;
      var3.ar = var2 * 500;
   }

   private Actor u(int var1) {
      Actor var2 = this.spawnActor(73, 48, 0, 0, 0, 100, false);
      if (this.variantData == 1) {
         var2.av = 0;
         var2.aG = 0;
      } else {
         var2.av = 1;
         var2.aG = 1;
      }

      var2.variantData = this.variantData;
      var2.at = this.at;
      var2.flags |= 16;
      if (var1 == 4) {
         if (this.variantData == 1) {
            var2.positionY = Game.viewBounds[1] + this.collisionBounds[1] - this.collisionBounds[3];
            var2.N = true;
            var2.changeActionState(102);
         } else if (this.variantData == 5 || this.variantData == 4 || this.variantData == 2) {
            var2.ar = this.ar;
            var2.positionX = var2.positionX + Game.cameraX * (Game.bU - 13) / (Game.mapWidthTiles - 13) + Game.cameraX;
            var2.positionY = var2.positionY + Game.cameraY * (Game.bW - 17) / (Game.mapHeightTiles - 17) + Game.cameraY;
            var2.ac = var2.positionX - Game.cameraX;
            var2.ad = var2.positionY - Game.cameraY;
            var2.changeActionState(100);
         }
      } else {
         var2.facingLeft = var1 == 2;
         var2.ay = this.ay;
         var2.changeActionState(104);
      }

      lastSpawnedActor = null;
      return var2;
   }

   // $VF: renamed from: I () void
   public final void updateTimedSwitch() {
      Actor var1 = Game.findActorByUid(this.as);
      this.decrementTimerA();
      switch (this.actionState) {
         case 0:
            if (this.isAnimationEnding() && this.animationId == 27 && this.variantData == 0) {
               this.requestAnimation(25);
            }

            if (this.a(0, this.collisionBounds)) {
               this.changeActionState(101);
               this.aw = this.au;
               return;
            }
            break;
         case 101:
            if (this.a(0, this.collisionBounds)) {
               if (this.variantData == 0) {
                  this.requestAnimation(26);
               }

               this.aw = this.au;
               return;
            }

            if (this.au == -1) {
               return;
            }

            if (this.ay == 0 || this.ay == 1 && (var1.actionState == 102 || var1.actionState == 105)) {
               if (this.aw <= 0) {
                  this.changeActionState(102);
                  return;
               }

               if (this.aw <= 1000) {
                  if (this.variantData == 0) {
                     this.requestAnimation(27);
                     return;
                  }
               } else if (this.isAnimationEnding() && this.variantData == 0) {
                  this.requestAnimation(26);
                  return;
               }
            } else {
               this.actionState = 0;
               if (this.variantData == 0) {
                  this.requestAnimation(27);
                  return;
               }
            }
            break;
         case 102:
            if (this.isAnimationEnding()) {
               if (this.variantData == 0) {
                  this.requestAnimation(25);
               }

               this.actionState = 0;
            }
      }
   }

   public static final boolean J() {
      for (int var0 = 0; var0 < 3; var0++) {
         if (cq[var0] != null && cq[var0].positionY - Game.cameraY > 200) {
            return true;
         }
      }

      return false;
   }

   public static final void p(Actor var0) {
      if (var0 != null && var0 != playerPlatform) {
         if (Game.player.positionX >= var0.collisionBounds[0] && Game.player.positionX <= var0.collisionBounds[2]) {
            playerPlatform = var0;
         }
      }
   }

   public final void K() {
      if (this.actionState == 101 || this.actionState == 0) {
         this.M();
      }
   }

   public final void d(int var1, int var2) {
      this.actionState = var1;
      this.velocityY = var2;

      for (int var3 = 0; var3 < 3; var3++) {
         if (cq[var3] != this && cq[var3] != null) {
            cq[var3].actionState = var1;
            cq[var3].positionY = this.positionY;
            cq[var3].velocityY = var2;
         }
      }
   }

   public final void i(int var1) {
      this.actionState = var1;

      for (int var2 = 0; var2 < 3; var2++) {
         if (cq[var2] != this && cq[var2] != null) {
            this.positionY = cq[var2].positionY;
            this.velocityY = cq[var2].velocityY;
            return;
         }
      }
   }

   public final void L() {
      boolean var1 = false;

      for (int var2 = 0; var2 < 3; var2++) {
         if (cq[var2] != null && this.positionY + 1 >= cq[var2].positionY && this.positionY - 5 < cq[var2].positionY) {
            var1 = true;
            break;
         }
      }

      if (var1) {
         switch (this.aw) {
            case 4:
               this.aw = 1;
               cq[0] = this;
               this.i(101);
               break;
            case 5:
               this.aw = 2;
               cq[2] = this;
               this.i(101);
               break;
            case 6:
               this.aw = 3;
               cq[1] = this;
               this.i(101);
               break;
            case 7:
               if (cq[0] != null) {
                  cq[0].actionState = 103;
                  if (playerPlatform == cq[0]) {
                     playerPlatform = null;
                  }

                  cq[0] = null;
               }
               break;
            case 8:
               if (cq[2] != null) {
                  cq[2].actionState = 103;
                  if (playerPlatform == cq[2]) {
                     playerPlatform = null;
                  }

                  cq[2] = null;
               }
               break;
            case 9:
               if (cq[1] != null) {
                  cq[1].actionState = 103;
                  if (playerPlatform == cq[2]) {
                     playerPlatform = null;
                  }

                  cq[1] = null;
               }
         }

         if (this.aw >= 7) {
            this.actionState = 104;
         }
      }
   }

   public final void M() {
      this.refreshBounds();

      for (int var2 = 0; var2 < Game.updateActorCount; var2++) {
         Actor var1;
         if ((var1 = Game.updateActors[var2]) != this && var1 != null && var1 != cq[0] && var1 != cq[2] && var1 != cq[1]) {
            var1.refreshBounds();
            if (var1.supportActor == this) {
               var1.positionY = this.collisionBounds[1] - 1;
               if (var1.type == 73) {
                  var1.positionY = this.collisionBounds[1] - 1 + (var1.collisionBounds[1] - var1.collisionBounds[3] >> 1);
                  var1.velocityY = 0;
                  var1.accelerationY = 0;
                  var1.velocityX = var1.facingLeft ? -var1.at : var1.at;
               }

               var1.grounded = true;
               var1.refreshBounds();
               cr[1] = var1.collisionBounds[1];
               cr[0] = var1.collisionBounds[0];
               cr[3] = var1.collisionBounds[3] + 1;
               cr[2] = var1.collisionBounds[2];
               cn += 64;
               cp++;
               this.P();
               if (!rectsOverlapInclusive(cF, cr)) {
                  var1.supportActor = null;
               } else if (cq[2] != null && this.c(this)) {
                  for (int var3 = 0; var3 < 3; var3++) {
                     if (cq[var3] != null && rectsOverlapInclusive(cq[var3].collisionBounds, cr)) {
                        var1.supportActor = cq[var3];
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   public final void N() {
      for (int var1 = 0; var1 < 3; var1++) {
         if (cq[var1] != null) {
            cq[var1].velocityY = 0;
         }
      }
   }

   public final void O() {
      int var1 = this.positionY;
      this.positionY = this.fixedY + this.velocityY >> 8;
      this.resolveTileCollisions(false, true);
      if (this.tileAtHead > 15) {
         this.N();
         this.d(101, 0);
         cn = 0;
      } else {
         if (Game.player.tileAtHead > 15) {
            this.N();
            this.d(101, 0);
            cn = 0;
            if (this == playerPlatform) {
               Game.player.velocityY = 0;
               return;
            }
         } else {
            this.positionY = var1;
         }
      }
   }

   public final void P() {
      this.refreshBounds();

      for (int var1 = 0; var1 < 4; var1++) {
         cF[var1] = this.collisionBounds[var1];
      }

      if (cq[2] != null && this.c(this)) {
         if (cq[0] != null) {
            cF[0] = cq[0].collisionBounds[0];
         } else {
            cF[0] = cq[2].collisionBounds[0];
         }

         if (cq[1] != null) {
            cF[2] = cq[1].collisionBounds[2];
            return;
         }

         cF[2] = cq[2].collisionBounds[2];
      }
   }

   public final void Q() {
      if (this.aw < 7) {
         this.P();
         this.d(1, cF);
      }
   }

   // $VF: renamed from: R () void
   public final void updatePlatformGroup() {
      this.Q();
      switch (this.actionState) {
         case 0:
            this.requestAnimation(20);
            if (this.aw == 2) {
               if (rectsOverlapInclusive(Game.player.collisionBounds, this.customBounds)) {
                  this.d(101, -512);
                  return;
               }
            } else if (this.aw > 3) {
               this.L();
               return;
            }
            break;
         case 101:
            this.velocityY = cn;
            this.O();
            if (this.velocityY > -512) {
               this.requestAnimation(21);
               return;
            }

            if (this.velocityY == 0) {
               this.requestAnimation(22);
               return;
            }

            this.requestAnimation(19);
            return;
         case 103:
            this.requestAnimation(20);
            this.velocityY = 0;
            if (this.positionY >= Game.cameraY + 320) {
               this.actionState = 104;
               return;
            }
            break;
         case 104:
            Game.removeActor(this);
      }
   }

   // $VF: renamed from: S () void
   public final void updateMovingPlatform() {
      this.d(0, this.collisionBounds);
      if (this.ax < 8) {
         switch (this.actionState) {
            case 0:
               this.aw = this.aw - Game.tickDeltaMs;
               if (this.aw <= 0) {
                  this.actionState = 105;
                  if (this.ax < 2) {
                     this.velocityX = this.ax * -2 * this.au + this.au;
                  } else if (this.ax < 4) {
                     this.velocityY = (3 - this.ax) * -2 * this.au + this.au;
                  } else if (this.ax < 6) {
                     this.velocityX = (this.ax - 4) * -2 * this.au + this.au;
                     this.velocityY = (this.au * (this.customBounds[2] - this.customBounds[0]) << 8) / (this.customBounds[3] - this.customBounds[1]);
                     this.velocityY = (this.ax - 4) * -2 * this.velocityY + this.velocityY;
                  } else {
                     this.velocityX = (7 - this.ax) * -2 * this.au + this.au;
                     this.velocityY = this.au * (this.customBounds[2] - this.customBounds[0]) / (this.customBounds[3] - this.customBounds[1]);
                     this.velocityY = (7 - this.ax) * -2 * this.velocityY + this.velocityY;
                  }
               }
               break;
            case 105:
               boolean var1 = false;
               if ((this.ax != 0 || this.collisionBounds[2] < this.customBounds[2])
                  && (this.ax != 1 || this.collisionBounds[0] > this.customBounds[0])
                  && (this.ax != 2 || this.collisionBounds[1] > this.customBounds[1])
                  && (this.ax != 3 || this.collisionBounds[3] < this.customBounds[3])) {
                  if (this.ax > 3
                     && (
                        this.ax == 4 && (this.collisionBounds[2] >= this.customBounds[2] || this.collisionBounds[3] >= this.customBounds[3])
                           || this.ax == 5 && (this.collisionBounds[0] <= this.customBounds[0] || this.collisionBounds[1] <= this.customBounds[1])
                           || this.ax == 6 && (this.collisionBounds[2] >= this.customBounds[2] || this.collisionBounds[1] <= this.customBounds[1])
                           || this.ax == 7 && (this.collisionBounds[0] <= this.customBounds[0] || this.collisionBounds[3] >= this.customBounds[3])
                     )) {
                     var1 = true;
                  }
               } else {
                  var1 = true;
               }

               if (var1) {
                  this.actionState = 0;
                  this.velocityX = this.velocityY = 0;
                  this.aw = this.as;
                  if (this.ax < 2) {
                     this.ax = 1 - this.ax;
                  } else if (this.ax < 4) {
                     this.ax = 2 - this.ax + 3;
                  } else if (this.ax < 6) {
                     this.ax = 5 - this.ax + 4;
                  } else {
                     this.ax = 7 - this.ax + 6;
                  }
               }
         }
      } else {
         this.resolveTileCollisions(true, true);
         this.decrementTimerA();
         if (playerPlatform == null) {
            this.requestAnimation(8);
            int var2 = this.collisionBounds[3] - this.collisionBounds[1];
            if (Game.player.positionY >= this.customBounds[1] + var2 && Game.player.positionY <= this.customBounds[3] - var2) {
               this.velocityY = Game.player.positionY - this.positionY + var2 + 10 << 4;
            } else if (Game.player.positionY < this.customBounds[1] + var2) {
               this.velocityX = 0;
               if (this.positionY > this.customBounds[1] + var2) {
                  this.velocityY = -512;
               } else {
                  this.velocityY = 0;
               }
            } else if (Game.player.positionY > this.customBounds[3] - var2) {
               this.velocityX = 0;
               if (this.positionY < this.customBounds[3]) {
                  this.velocityY = 512;
               } else {
                  this.velocityY = 0;
               }
            }
         } else if (playerPlatform == this) {
            switch (this.actionState) {
               case 0:
                  if (playerPlatform == this) {
                     this.actionState = 102;
                  }
                  break;
               case 102:
                  this.accelerationY = -256;
                  if (this.velocityY <= -this.au) {
                     this.velocityY = -this.au;
                  }

                  if (Game.player.animationId == 10) {
                     this.actionState = 104;
                  }
                  break;
               case 104:
                  this.accelerationY = 256;
                  if (this.velocityY >= this.au) {
                     this.velocityY = this.au;
                  }

                  if (Game.player.animationId != 10) {
                     this.actionState = 102;
                  }
            }
         }
      }

      if (this.ax >= 8 && (this.velocityX != 0 || this.velocityY != 0)) {
         if (this.collisionBounds[1] <= this.customBounds[1] && this.velocityY < 0) {
            this.velocityY = 0;
         } else if (this.collisionBounds[3] >= this.customBounds[3] && this.velocityY > 0) {
            this.velocityY = 0;
         }

         if (playerPlatform == this) {
            if (Game.player.positionX <= this.positionX - 10) {
               this.velocityX = -512;
               this.requestAnimation(10);
            } else if (Game.player.positionX >= this.positionX + 10) {
               this.velocityX = 512;
               this.requestAnimation(11);
            } else {
               this.velocityX = 0;
               this.requestAnimation(9);
            }
         }

         if ((this.collisionBounds[2] > this.customBounds[2] || this.wallOnRight) && this.velocityX > 0) {
            this.velocityX = 0;
         } else if ((this.collisionBounds[0] < this.customBounds[0] || this.wallOnLeft) && this.velocityX < 0) {
            this.velocityX = 0;
         }

         if (this.animationId == 10 || this.animationId == 11) {
            this.velocityY >>= 1;
         }
      }

      if (this.animationId == 0) {
         if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
            Game.player.a(4, 15, 0, this);
         }

         if (this.attachedActor != null) {
            if (this.attachedActor.facingLeft) {
               this.attachedActor.positionX = this.collisionBounds[2];
            } else {
               this.attachedActor.positionX = this.collisionBounds[0];
            }

            this.attachedActor.positionY = this.positionY + this.ar;
         }
      }

      this.d(0, this.collisionBounds);
   }

   private void d(int var1, int[] var2) {
      this.refreshBounds();
      if (this.collisionBounds != null) {
         if (playerPlatform == null) {
            int var3 = Game.player.positionY + (Game.player.velocityY >> 8);
            if (Game.player.velocityY >= 0
               && Game.player.positionY <= var2[1] + var1 + (this.type == 65 ? 0 : 5)
               && Game.player.positionX >= var2[0]
               && Game.player.positionX <= var2[2]
               && (var3 >= var2[1] + (this.velocityY >> 8) || var3 >= var2[1])) {
               if (this.type == 64) {
                  this.requestAnimation(1);
                  diagonalJump = false;
                  shortJump = false;
                  Game.player.requestAnimation(3);
                  Game.player.velocityX = 0;
                  Game.player.velocityY = -6656;
                  return;
               }

               playerPlatform = this;
               if (this.type == 82 && this.aw <= 3) {
                  for (int var4 = 0; var4 < 3; var4++) {
                     p(cq[var4]);
                  }
               }

               Game.player.requestAnimation(6);
               Game.player.positionY = var2[1] + 1;
               if (this.type == 50) {
                  this.moduleMask[11] = 0;
                  return;
               }
            }
         } else if (playerPlatform == this) {
            if (this.type == 82 && this.aw <= 3) {
               for (int var5 = 0; var5 < 3; var5++) {
                  p(cq[var5]);
               }
            }

            Game.player.refreshBounds();
            if (Game.player.animationId != 4
                  && Game.player.animationId != 5
                  && !rectsOverlapInclusive(Game.player.collisionBounds, var2)
                  && Game.player.velocityY - this.velocityY < 0
               || Game.player.positionX > var2[2]
               || Game.player.positionX < var2[0]) {
               playerPlatform = null;
               Game.player.velocityX = 0;
               Game.player.velocityY = 0;
               return;
            }

            if (playerPlatform.type == 33) {
               Game.player.positionY = var2[1] + 1;
               return;
            }

            if (playerPlatform.type == 50) {
               Game.player.positionY = var2[1];
               return;
            }

            if (playerPlatform.type == 39) {
               Game.player.positionY = var2[1] + 1;
               if (this.s != null) {
                  Game.player.velocityX = Game.player.velocityX + this.s.velocityX;
                  return;
               }
            } else {
               if (playerPlatform.type == 82) {
                  playerPlatform.O();
                  Game.player.velocityY = playerPlatform.velocityY;
                  Game.player.positionY = var2[1] + var1;
                  return;
               }

               Game.player.velocityY = this.velocityY;
               Game.player.positionY = var2[1] + var1;
            }
         }
      }
   }

   // $VF: renamed from: bq () void
   private void updateSpringPlatform() {
      this.refreshBounds();
      this.d(0, this.collisionBounds);
      switch (this.animationId) {
         case 0:
            return;
         case 1:
            if (this.isAnimationEnding()) {
               this.requestAnimation(0);
            }
      }
   }

   // $VF: renamed from: br () void
   private void updateSwingingPlatform() {
      if (this.actionState != 103 && this.actionState != 104) {
         this.d(1, this.collisionBounds);
      }

      switch (this.actionState) {
         case 0:
            if (playerPlatform == this && this.moduleMask[11] != 1) {
               this.actionState = 101;
               this.moduleMask[10] = 2000;
            }

            this.U();
            return;
         case 101:
            this.moduleMask[10] = this.moduleMask[10] - Game.tickDeltaMs;
            this.V();
            int var1 = Math.abs(this.ar);
            int var2 = (this.av << 8) + var1 * Game.cos256(this.aw >> 16) >> 8;
            int var3 = (this.as << 8) + var1 * Game.sin256(this.aw >> 16) >> 8;
            this.velocityY = var2 - this.positionY << 8;
            this.velocityX = var3 - this.positionX << 8;
            if (playerPlatform != this && this.aw == 0 && this.ay != 1) {
               this.T();
            }

            if (this.moduleMask[10] < 0 && this.aw == 0 && this.ay != 1) {
               this.T();
            }

            this.U();
            return;
         case 102:
            this.accelerationY = 768;
            if (this.velocityY >= 4608) {
               this.velocityY = 4608;
               this.accelerationY = 0;
            }

            if (this.aw != 0) {
               this.V();
            }

            for (int var4 = 0; var4 < Game.updateActorCount; var4++) {
               Actor var5;
               if ((var5 = Game.updateActors[var4]) != null && (var5.flags & 1024) == 0) {
                  if (var5.isDamageable() && rectsOverlapInclusive(var5.collisionBounds, this.collisionBounds) && this.positionY < var5.positionY - 25) {
                     var5.applyDamageWithReaction(100);
                     this.velocityX = 0;
                     break;
                  }

                  if (var5.type == 45 && var5.actionState == 0) {
                     int[] var6;
                     (var6 = new int[4])[0] = var5.collisionBounds[0];
                     var6[2] = var5.collisionBounds[2];
                     var6[1] = var5.collisionBounds[1] - 17;
                     var6[3] = var5.collisionBounds[3];
                     if (rectsOverlapInclusive(var6, this.collisionBounds)) {
                        var5.au = this.positionX;
                        this.setCollisionRectEmpty(var5.collisionBounds, true);
                        var5.actionState = 100;
                        Game.playSoundOnce(-1);
                        break;
                     }
                  }
               }
            }

            this.resolveTileCollisions(false, true);
            if (this.grounded) {
               this.actionState = 103;
               this.setAnimationImmediate(12);
               Game.playSoundOnce(-1);
               this.accelerationY = 0;
               this.velocityY = 0;
            }

            if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds) && this.positionY < Game.player.positionY - 25) {
               Game.player.a(4, this.getStat(1), 0, this);
               return;
            }
            break;
         case 103:
            playerPlatform = null;
            this.accelerationY = 0;
            this.velocityY = 0;
            if (this.isAnimationEnding()) {
               this.actionState = 104;
            }

            if (this.aw != 0) {
               this.V();
               return;
            }
            break;
         case 104:
            this.positionY = this.av;
      }
   }

   public final void T() {
      this.actionState = 0;
      this.moduleMask[0] = this.moduleMask[1];
      this.moduleMask[2] = this.moduleMask[3];
      this.moduleMask[4] = this.moduleMask[5];
      this.positionX = this.as;
      this.positionY = this.av - this.ar;
      this.au = 1;
      this.aw = 0;
      this.at = this.moduleMask[7];
      this.ax = this.moduleMask[6];
      this.moduleMask[11] = 1;
      this.stopMotion();
   }

   public final void j(int var1) {
      switch (var1) {
         case 0:
            cF[0] = this.positionX - 3;
            cF[2] = this.positionX + 3;
            cF[1] = this.av;
            cF[3] = this.positionY;
            return;
         case 1:
            cF[0] = this.collisionBounds[0];
            cF[2] = this.collisionBounds[2];
            cF[1] = this.collisionBounds[1] - 3;
            cF[3] = this.collisionBounds[3] - 3;
      }
   }

   public final void U() {
      this.j(0);
      if (this.ay == 0 && this.f(cF)) {
         this.actionState = 102;
      }
   }

   public final void V() {
      int var1 = this.at >= 0 ? this.at : 0;
      if (this.ax == 1) {
         this.aw += var1 << 8;
      } else {
         this.aw -= var1 << 8;
      }

      if (this.aw == 0 && this.moduleMask[8] != 1) {
         this.at = this.moduleMask[4];
         this.au = -this.au;
         this.moduleMask[9] = 1;
         this.moduleMask[8] = 1;
      }

      if (this.aw < this.moduleMask[0] << 16 && this.at > 0) {
         if (this.aw <= -(this.moduleMask[0] << 16) || this.at <= 0) {
            this.ax = -this.ax;
            this.au = -this.au;
            if (this.at > 0) {
               this.moduleMask[9] = 1;
            }

            this.moduleMask[8] = 0;
         }
      } else {
         this.ax = -this.ax;
         this.au = -this.au;
         if (this.at > 0) {
            this.moduleMask[9] = 1;
         }

         this.moduleMask[8] = 0;
      }

      if (this.moduleMask[9] != 1) {
         this.at = this.at - this.au * this.moduleMask[2];
      } else {
         this.moduleMask[9] = 0;
      }
   }

   private void bs() {
      int var1 = this.collisionBounds[3] - Game.player.positionY;
      int var2 = this.collisionBounds[3] - this.collisionBounds[1];
      int var3 = this.s.collisionBounds[3] - this.s.collisionBounds[1];
      int var4 = (var1 << 8) / var2;
      int var5 = var3 * var4 >> 8;
      Game.player.positionY = this.s.collisionBounds[3] - var5;
   }

   private void bt() {
      if (this.aB != -1 && this.s == null) {
         this.s = Game.findActorByUid(this.aB);
      }

      if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
         boolean var1 = false;
         switch (this.aw) {
            case 1:
               if (!Game.player.facingLeft || Game.player.velocityX >= 0) {
                  break;
               }

               Game.player.positionX = this.s.positionX;

               while (rectsOverlapInclusive(Game.player.refreshBounds(), this.s.collisionBounds)) {
                  Game.player.positionX--;
               }

               this.bs();
               var1 = true;
               break;
            case 2:
               if (!Game.player.facingLeft && Game.player.velocityX > 0) {
                  Game.player.positionX = this.s.positionX;

                  while (rectsOverlapInclusive(Game.player.refreshBounds(), this.s.collisionBounds)) {
                     Game.player.positionX++;
                  }

                  this.bs();
                  var1 = true;
               }
            case 3:
            case 5:
            case 6:
            case 7:
            default:
               break;
            case 4:
               if (Game.player.velocityY >= 0) {
                  break;
               }

               Game.player.positionX = this.s.collisionBounds[0] + (Game.player.positionX - this.collisionBounds[0]);
               Game.player.positionY = this.s.collisionBounds[3];

               while (rectsOverlapInclusive(Game.player.refreshBounds(), this.s.collisionBounds)) {
                  Game.player.positionY--;
               }

               var1 = true;
               break;
            case 8:
               if (Game.player.velocityY > 0) {
                  Game.player.positionX = this.s.collisionBounds[0] + (Game.player.positionX - this.collisionBounds[0]);
                  Game.player.positionY = this.s.collisionBounds[3];

                  while (rectsOverlapInclusive(Game.player.refreshBounds(), this.s.collisionBounds)) {
                     Game.player.positionY++;
                  }

                  var1 = true;
               }
         }

         if (var1) {
            Game.player.bU = false;
            Game.player.bV = null;
            Game.cameraControllerActive = false;
            Game.activeCameraControllerUid = -1;
            if (!Game.ai) {
               Game.refreshCameraRegionSelection();
            }
         }
      }
   }

   // $VF: renamed from: bu () void
   private void updateDestructible79() {
      if (this.animationId == 1 && this.isAnimationEnding()) {
         Game.removeActor(this);
      }
   }

   // $VF: renamed from: bv () void
   private void updateDestructible61() {
      if (this.animationId != 0 && this.animationId != 2) {
         if (this.aB != -1 && this.attachedActor == null) {
            this.setAttachedActor(Game.findActorByUid(this.aB));
         }

         if (this.y != -1 && this.s == null) {
            this.s = Game.findActorByUid(this.y);
         }

         if (rectsOverlapInclusive(Game.player.attackBounds, this.collisionBounds)) {
            this.damageDestructibleCells(Game.player.attackBounds, false, 61);
         } else if (3 == Game.levelModes[Game.levelIndex]) {
            cr[1] = Game.player.collisionBounds[1] - 20;
            cr[3] = Game.player.collisionBounds[3] + 10;
            cr[0] = Game.player.collisionBounds[0];
            cr[2] = Game.player.collisionBounds[2] + 20;
            if (rectsOverlapInclusive(cr, this.collisionBounds) && this.damageDestructibleCells(cr, false, 61)) {
               Game.player.a(4, 5, 0, this);
            }
         } else if (2 == Game.levelModes[Game.levelIndex]) {
            Game.player.refreshBounds();
            cr[1] = Game.player.collisionBounds[1] - 3;
            cr[3] = Game.player.collisionBounds[3];
            cr[0] = Game.player.collisionBounds[0];
            cr[2] = Game.player.collisionBounds[2];
            if (rectsOverlapStrict(cr, this.collisionBounds) && this.damageDestructibleCells(cr, false, 61)) {
               Game.player.a(4, 5, 0, this);
            }
         }

         if (this.attachedActor != null && !this.damageDestructibleCells(this.attachedActor.collisionBounds, true, 61)) {
            this.attachedActor.changeActionState(103);
            this.setAttachedActor(null);
            this.aB = -1;
         }

         if (this.s != null && !this.damageDestructibleCells(this.s.collisionBounds, true, 61)) {
            this.s = null;
            this.y = -1;
         }

         boolean var1 = true;

         for (int var2 = 0; var2 < this.moduleMask.length; var2++) {
            if (this.moduleMask[var2] == 0) {
               var1 = false;
            }
         }

         if (var1) {
            Game.removeActor(this);
         }

         if ((this.animationId == 8 || this.animationId == 10) && this.isAnimationEnding()) {
            this.requestAnimation(this.animationId + 1);
            return;
         }
      } else if (this.isAnimationEnding()) {
         Game.removeActor(this);
      }
   }

   // $VF: renamed from: bw () void
   private void activateTrap75() {
      if (this.animationId != 23 && this.animationId != 21) {
         if (this.animationId == 24) {
            this.actionState = 102;
            this.requestAnimation(20);
         }
      } else {
         this.actionState = 103;
         this.requestAnimation(25);
         this.facingLeft = Game.player.facingLeft;
      }
   }

   // $VF: renamed from: bx () void
   private void updateTrap75() {
      switch (this.actionState) {
         case 0:
            if (rectsOverlapInclusive(Game.player.attackBounds, this.collisionBounds)) {
               this.activateTrap75();
               return;
            }

            if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
               Game.player.a(4, 10, 0, this);
               this.activateTrap75();
               return;
            }

            if (this.variantData == 1) {
               if (absInt(Game.player.positionX - this.positionX) < 100 && Game.player.positionY > this.positionY) {
                  this.actionState = 109;
                  this.aw = 500;
                  return;
               }
            } else if (this.variantData == 2) {
               for (int var1 = 0; var1 < Game.updateActorCount; var1++) {
                  Actor var2;
                  if ((var2 = Game.updateActors[var1]) != null
                     && (var2.type == 83 || var2.type == 84 || var2.type == 85)
                     && rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds)) {
                     this.actionState = 109;
                  }
               }
            }
            break;
         case 102:
            this.spawnActor(39, 1, 26, 0, 0, 302, false);
            if (this.variantData == 2) {
               lastSpawnedActor.variantData = 2;
            }

            if (this.variantData == 4) {
               this.actionState = 110;
            } else {
               this.actionState = 106;
               this.aw = 2200;
            }

            this.requestAnimation(20);
            return;
         case 103:
            if (this.isAnimationEnding()) {
               this.requestAnimation(19);
               if (this.variantData != 3 && this.variantData != 5) {
                  this.actionState = 106;
                  this.aw = 2200;
                  return;
               }
            }
            break;
         case 104:
            if (this.isAnimationEnding()) {
               this.actionState = 0;
               if (this.animationId == 21) {
                  this.requestAnimation(23);
               } else {
                  this.requestAnimation(24);
               }

               this.facingLeft = false;
               return;
            }
            break;
         case 106:
            this.decrementTimerA();
            if (this.aw <= 0 && this.variantData != 2) {
               this.actionState = 104;
               if (this.animationId == 19) {
                  this.requestAnimation(21);
                  return;
               }

               this.requestAnimation(22);
            }
            break;
         case 109:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.actionState = 102;
               this.requestAnimation(20);
               return;
            }
      }
   }

   // $VF: renamed from: a (int[], boolean, int) boolean
   private boolean damageDestructibleCells(int[] var1, boolean var2, int var3) {
      if (this.moduleMask == null) {
         return false;
      }

      boolean var4 = false;

      for (int var5 = 0; var5 < this.moduleMask.length; var5++) {
         int var6 = var5 / this.as;
         int var7 = var5 % this.as;
         cF[0] = this.collisionBounds[0] + 20 * var7;
         cF[1] = this.collisionBounds[1] + 20 * var6;
         cF[2] = cF[0] + 20;
         cF[3] = cF[1] + 20;
         if (!var2) {
            this.setCollisionRectEmpty(cF, true);
            if (this.moduleMask[var5] == 0 && rectsOverlapInclusive(var1, cF)) {
               this.moduleMask[var5] = 1;
               if (var3 == 61) {
                  var4 = true;
               }

               if (var3 == 61) {
                  this.spawnActor(61, 40, 0, cF[0] - this.positionX + Game.randomRange(-10, 10), cF[1] - this.positionY + Game.randomRange(-10, 10), 302, false);
               } else {
                  this.spawnActor(79, 49, 1, cF[0] - this.positionX + Game.randomRange(-10, 10), cF[1] - this.positionY + Game.randomRange(-10, 10), 302, false);
                  Game.playSoundOnce(-1);
               }
            }

            if (this.moduleMask[var5] == 0) {
               this.setCollisionRectEmpty(cF, false);
            }
         } else if (rectsOverlapInclusive(var1, cF) && this.moduleMask[var5] == 0 && var3 == 61) {
            var4 = true;
            break;
         }
      }

      return var4;
   }

   // $VF: renamed from: a (int, int, int, int, int[]) void
   public static final void raycastTiles(int var0, int var1, int var2, int var3, int[] var4) {
      var4[0] = var2;
      var4[1] = var3;
      int var6 = var0 / 20;
      int var7 = var1 / 20;
      int var5;
      if ((var5 = Game.getCollisionTile(var6, var7)) >= 24 && var5 != 15) {
         var4[0] = var0;
         var4[1] = var1;
      } else {
         boolean var8 = var0 > var2;
         boolean var9 = var1 > var3;
         int var11 = 0;

         do {
            boolean var10 = false;
            if (var8) {
               if (intersectRaySegment(var0, var1, var2, var3, var6 * 20, var7 * 20, var6 * 20, (var7 + 1) * 20, var4)) {
                  var10 = true;
                  var5 = Game.getCollisionTile(--var6, var7);
               }
            } else if (intersectRaySegment(var0, var1, var2, var3, (var6 + 1) * 20, var7 * 20, (var6 + 1) * 20, (var7 + 1) * 20, var4)) {
               var10 = true;
               var5 = Game.getCollisionTile(++var6, var7);
            }

            if (!var10) {
               if (var9) {
                  if (intersectRaySegment(var0, var1, var2, var3, var6 * 20, var7 * 20, (var6 + 1) * 20, var7 * 20, var4)) {
                     var10 = true;
                     var5 = Game.getCollisionTile(var6, --var7);
                  }
               } else if (intersectRaySegment(var0, var1, var2, var3, var6 * 20, (var7 + 1) * 20, (var6 + 1) * 20, (var7 + 1) * 20, var4)) {
                  var10 = true;
                  var5 = Game.getCollisionTile(var6, ++var7);
               }
            }

            if (var5 >= 15 && var5 != 15 || !var10) {
               return;
            }
         } while (++var11 <= 56);
      }
   }

   // $VF: renamed from: b (int, int, int, int, int[]) boolean
   public static final boolean segmentCrossesRectEdges(int var0, int var1, int var2, int var3, int[] var4) {
      int var5 = var4[0];
      int var6 = var4[1];
      int var7 = var4[2];
      int var8 = var4[3];
      return segmentsIntersect(var0, var1, var2, var3, var5, var6, var7, var6)
         || segmentsIntersect(var0, var1, var2, var3, var5, var6, var5, var8)
         || segmentsIntersect(var0, var1, var2, var3, var7, var8, var7, var6)
         || segmentsIntersect(var0, var1, var2, var3, var7, var8, var5, var8);
   }

   // $VF: renamed from: a (int, int, int, int, int, int, int, int, int[]) boolean
   public static final boolean intersectRaySegment(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int[] var8) {
      int var9 = -(var2 - var0) * (var7 - var5) + (var3 - var1) * (var6 - var4);
      int var10 = -(var4 - var0) * (var7 - var5) + (var5 - var1) * (var6 - var4);
      int var11 = (var2 - var0) * (var5 - var1) - (var3 - var1) * (var4 - var0);
      if (!sameSignOrZero(var9, var10)) {
         return false;
      }

      if (!sameSignOrZero(var9, var11)) {
         return false;
      }

      if (absInt(var11) > absInt(var9)) {
         return false;
      }

      var8[0] = var0 + var10 * (var2 - var0) / var9;
      var8[1] = var1 + var10 * (var3 - var1) / var9;
      return true;
   }

   // $VF: renamed from: a (int, int, int, int, int, int, int, int) boolean
   public static final boolean segmentsIntersect(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8;
      if ((var8 = -(var2 - var0) * (var7 - var5) + (var3 - var1) * (var6 - var4)) == 0) {
         return false;
      } else {
         int var9 = -(var4 - var0) * (var7 - var5) + (var5 - var1) * (var6 - var4);
         int var10 = (var2 - var0) * (var5 - var1) - (var3 - var1) * (var4 - var0);
         if (!sameSignOrZero(var8, var9)) {
            return false;
         } else if (absInt(var9) > absInt(var8)) {
            return false;
         } else {
            return !sameSignOrZero(var8, var10) ? false : absInt(var10) <= absInt(var8);
         }
      }
   }

   public final void W() {
      if (3 == Game.levelModes[Game.levelIndex] && bC) {
         this.velocityX = this.velocityX + (worldScrollPixelsPerTick << 8);
      }
   }

   // $VF: renamed from: X () boolean
   public final boolean isRespawnable() {
      return this.type == 2 || this.type == 3 || this.type == 13 || this.type == 14 || this.type == 70 || (this.flags & 8192) != 0
         ? true
         : this.type == 1 && this.aB <= 0;
   }

   // $VF: renamed from: Y () boolean
   public final boolean canStandOnActors() {
      return this.type == 3 || this.type == 1 || this.type == 14 || this.type == 70 || this.type == 73;
   }

   // $VF: renamed from: Z () boolean
   public final boolean canSupportActor() {
      if (this.type == 72) {
         return true;
      } else {
         return this.type != 14 || this.animationId != 2 && this.animationId != 3 ? this.c(this) : true;
      }
   }

   // $VF: renamed from: g (int[]) void
   private void buildInteractionBounds(int[] var1) {
      if (this.facingLeft) {
         this.interactionBounds[2] = this.collisionBounds[0] - var1[0];
         this.interactionBounds[0] = this.interactionBounds[2] - var1[1];
      } else {
         this.interactionBounds[0] = this.collisionBounds[2] + var1[0];
         this.interactionBounds[2] = this.interactionBounds[0] + var1[1];
      }

      this.interactionBounds[1] = this.collisionBounds[1] - var1[2];
      this.interactionBounds[3] = this.collisionBounds[3] + var1[3];
   }

   // $VF: renamed from: by () void
   private void decrementTimerA() {
      this.aw = this.aw - Game.tickDeltaMs;
   }

   // $VF: renamed from: bz () void
   private void decrementTimerB() {
      this.ax = this.ax - Game.tickDeltaMs;
   }

   private void l(int var1, int var2) {
      this.sprite.boundaryClipMask = var1;
      switch (var1) {
         case 1:
         case 2:
            this.sprite.boundaryClipX = var2 - Game.cameraX;
            return;
         case 4:
         case 8:
            this.sprite.boundaryClipY = var2 - Game.cameraY;
         case 3:
         case 5:
         case 6:
         case 7:
      }
   }

   // $VF: renamed from: a (b, int, int) void
   private void positionChild(Actor var1, int var2, int var3) {
      if (var1 != null) {
         var1.facingLeft = this.facingLeft;
         var1.positionX = this.positionX + (this.facingLeft ? -var2 : var2);
         var1.positionY = this.positionY + var3;
      }
   }

   // $VF: renamed from: v (int) int
   private int getStat(int var1) {
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
         case 10:
            var2 = 9;
            var3 = 4;
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
         case 90:
            var2 = 15;
            var3 = 5;
            break;
         case 100:
            var2 = 16;
            var3 = 4;
      }

      return Game.actorStatTables[var2][var3 * this.statSet + var1];
   }

   // $VF: renamed from: bA () int
   private int getVariantFlagBit() {
      return this.variantData >> 0 & 1;
   }

   // $VF: renamed from: aa () int
   public final int getBehaviorVariant() {
      return this.variantData >> 1 & 15;
   }

   // $VF: renamed from: k (int) int
   public static final int absInt(int var0) {
      return var0 < 0 ? -var0 : var0;
   }

   // $VF: renamed from: e (int, int) int
   public static final int maxInt(int var0, int var1) {
      return var0 > var1 ? var0 : var1;
   }

   // $VF: renamed from: f (int, int) int
   public static final int minInt(int var0, int var1) {
      return var0 < var1 ? var0 : var1;
   }

   // $VF: renamed from: a (long) long
   public static final long integerSqrt(long var0) {
      long var2;
      if (var0 < 100L) {
         var2 = 9L;

         while (var0 < var2 * var2) {
            var2--;
         }
      } else {
         var2 = integerSqrt(var0 / 100L);
         long var4;
         long var6 = (var4 = var0 - var2 * var2 * 100L) / (var2 * 20L);

         while (var6 * (var2 * 20L + var6) > var4) {
            var6--;
         }

         var2 = var2 * 10L + var6;
      }

      return var2;
   }

   // $VF: renamed from: a (int, int, int, int, int, int[]) void
   public static final void rotatePoint(int var0, int var1, int var2, int var3, int var4, int[] var5) {
      int var6 = var2 - var0 << 8;
      int var7 = var3 - var1 << 8;
      var5[0] = (var6 * Game.cos256(var4) - var7 * Game.sin256(var4) >> 8) / 256;
      var5[1] = (var6 * Game.sin256(var4) + var7 * Game.cos256(var4) >> 8) / 256;
      var5[0] += var0;
      var5[1] += var1;
   }

   // $VF: renamed from: g (int, int) boolean
   public static final boolean sameSignOrZero(int var0, int var1) {
      return var0 >= 0 && var1 >= 0 || var0 <= 0 && var1 <= 0;
   }

   // $VF: renamed from: w (int) int
   private int horizontalSpeedTowardPlayer(int var1) {
      int var2;
      return (var2 = this.fixedX - Game.player.fixedX) < 0 ? var1 : -var1;
   }

   // $VF: renamed from: bB () void
   private void facePlayerFixed() {
      this.facingLeft = this.fixedX - Game.player.fixedX > 0;
   }

   // $VF: renamed from: x (int) int
   private int horizontalSpeedTowardPlayerDeadZone(int var1) {
      int var2;
      if (absInt(var2 = this.fixedX - Game.player.fixedX) > absInt(var1)) {
         return var2 < 0 ? var1 : -var1;
      } else {
         return 0;
      }
   }

   // $VF: renamed from: y (int) int
   private int verticalSpeedTowardPlayerDeadZone(int var1) {
      int var2;
      if (absInt(var2 = this.fixedY - Game.player.fixedY) > absInt(var1)) {
         return var2 < 0 ? var1 : -var1;
      } else {
         return 0;
      }
   }

   // $VF: renamed from: z (int) void
   private void facePlayerBeyondDistance(int var1) {
      if (absInt(Game.player.fixedX - this.fixedX) > absInt(var1)) {
         this.facingLeft = Game.player.positionX < this.positionX;
      }
   }

   // $VF: renamed from: bC () void
   private void stopMotion() {
      this.velocityX = this.velocityY = this.accelerationX = this.accelerationY = 0;
   }

   public final void l(int var1) {
      if (Game.levelModes[Game.levelIndex] == 2) {
         this.velocityY = var1 + cn;
      }
   }

   public final boolean c(int[] var1) {
      if (aM == null) {
         return false;
      }

      for (int var2 = 0; var2 < aM.length; var2++) {
         if (aM[var2] != null && rectsOverlapInclusive(aM[var2].collisionBounds, var1)) {
            return true;
         }
      }

      return false;
   }

   public final boolean d(int[] var1) {
      if (aR == null) {
         return false;
      }

      for (int var2 = 0; var2 < aR.length; var2++) {
         if (aR[var2] != null && rectsOverlapInclusive(aR[var2].attackBounds, var1)) {
            return true;
         }
      }

      return false;
   }

   public final boolean e(int[] var1) {
      if (cx == null) {
         return false;
      }

      for (int var2 = 0; var2 < cx.length; var2++) {
         if (cx[var2] != null && rectsOverlapInclusive(cx[var2].collisionBounds, var1)) {
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
      return this.c(var1) || this.d(var1) || this.b(activeWeapon, var1);
   }

   public final boolean ab() {
      return rectsOverlapInclusive(Game.player.attackBounds, this.collisionBounds);
   }

   public final boolean b(int var1, int[] var2) {
      return var1 == 0
            && activeWeapon == 0
            && Game.player.animationId >= 13
            && Game.player.animationId <= 15
            && rectsOverlapInclusive(Game.player.attackBounds, var2)
         ? true
         : var1 == 1
            && activeWeapon == 1
            && Game.player.animationId >= 19
            && Game.player.animationId <= 21
            && rectsOverlapInclusive(Game.player.attackBounds, var2);
   }

   // $VF: renamed from: bD () void
   private void facePlayerPixels() {
      this.facingLeft = Game.player.positionX < this.positionX;
   }

   // $VF: renamed from: b (int, int, int) void
   public final void showLinkedPrompt(int var1, int var2, int var3) {
      if (this.linkedEffect == null) {
         this.spawnActor(21, 18, var1, 0, 0, 302, false);
         this.setLinkedEffect(lastSpawnedActor);
         this.linkedEffect.positionX = var2;
         this.linkedEffect.positionY = var3;
         this.linkedEffect.facingLeft = false;
         this.linkedEffect.flags &= -2;
         this.linkedEffect.ax = 1;
         lastSpawnedActor = null;
      }
   }

   // $VF: renamed from: c (int, int, int) b
   public final Actor spawnPrompt(int var1, int var2, int var3) {
      this.spawnActor(21, 18, var1, 0, 0, 302, false);
      lastSpawnedActor.positionX = var2;
      lastSpawnedActor.positionY = var3;
      lastSpawnedActor.facingLeft = false;
      lastSpawnedActor.ax = 1;
      return lastSpawnedActor;
   }

   // $VF: renamed from: ac () void
   public final void removeLinkedEffect() {
      if (this.linkedEffect != null) {
         Game.removeActor(this.linkedEffect);
         this.linkedEffect = null;
      }
   }

   // $VF: renamed from: a (int[], int) void
   final void fillCollisionTiles(int[] var1, int var2) {
      int var3 = (var1[0] + 10) / 20;
      int var4 = (var1[2] - 10) / 20;
      int var5 = (var1[1] + 10) / 20;
      int var6 = (var1[3] - 10) / 20;

      for (int var7 = var3; var7 <= var4; var7++) {
         for (int var8 = var5; var8 <= var6; var8++) {
            Game.setCollisionTile(var7, var8, var2);
         }
      }
   }

   // $VF: renamed from: a (int[], boolean) void
   final void setCollisionRectEmpty(int[] var1, boolean var2) {
      if (var2) {
         this.fillCollisionTiles(var1, 0);
      } else {
         this.fillCollisionTiles(var1, 24);
      }
   }

   // $VF: renamed from: bE () boolean
   private boolean probeForwardSurface() {
      boolean var1 = false;
      int var2 = 0;
      if (this.facingLeft) {
         var2 = this.collisionBounds[0] - 1;
      } else {
         var2 = this.collisionBounds[2] + 1;
      }

      switch (Game.getCollisionTile(var2 / 20, this.positionY / 20)) {
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

   // $VF: renamed from: bF () void
   private void updatePickup() {
      this.bU = false;

      for (int var2 = 0; var2 < Game.paintActorCount; var2++) {
         Actor var1;
         if ((var1 = Game.paintActors[var2]).type == 13 && var1.actionState != 0 && (var1.getBehaviorVariant() == 1 || var1.getBehaviorVariant() == 3)) {
            this.dd = Game.e(var1.positionX - this.positionX, var1.positionY - this.positionY);
            if (this.dd < 100) {
               this.bU = true;
               this.bV = var1;
               break;
            }
         }
      }

      switch (this.actionState) {
         case 0:
            if (this.bU && (this.animationId == 0 || this.animationId == 2)) {
               this.dd = Game.e(this.bV.fixedX - this.fixedX, this.bV.fixedY - this.fixedY);
               this.velocityX = this.dd == 0 ? 0 : (this.bV.fixedX - this.fixedX) * 1536 / this.dd;
               this.velocityY = this.dd == 0 ? 0 : (this.bV.fixedY - this.fixedY) * 1536 / this.dd;
               if (rectsOverlapInclusive(this.collisionBounds, this.bV.collisionBounds)) {
                  if (this.bV.getBehaviorVariant() == 1) {
                     this.bV.changeActionState(105);
                  } else if (this.bV.getBehaviorVariant() == 3) {
                     this.bV.changeActionState(107);
                  }

                  if (this.animationId == 0 && this.bV.au < 8) {
                     this.bV.au++;
                  } else if (this.animationId == 2 && this.bV.as < 8) {
                     this.bV.as++;
                  }

                  this.ad();
               }
            } else {
               int var4 = Game.player.collisionBounds[3] - Game.player.collisionBounds[1] << 7;
               this.dd = Game.e(Game.player.fixedX - this.fixedX, Game.player.fixedY - this.fixedY - var4 - 1);
               if (this.dd <= (this.uid == -1 ? 40960 : 10240)) {
                  this.velocityX = this.dd == 0 ? 0 : (Game.player.fixedX - this.fixedX) * 1536 / this.dd;
                  this.velocityY = this.dd == 0 ? 0 : (Game.player.fixedY - var4 - this.fixedY) * 1536 / this.dd;
                  if (Game.player.velocityX != 0 && sameSignOrZero(Game.player.velocityX, Game.player.positionX - this.positionX)) {
                     this.velocityX = this.velocityX + Game.player.velocityX;
                  }

                  if (Game.player.velocityY != 0 && sameSignOrZero(Game.player.velocityY, Game.player.positionY - this.positionY)) {
                     this.velocityY = this.velocityY + Game.player.velocityY;
                  }
               }
            }

            if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
               if (this.animationId == 3 || this.animationId == 1) {
                  this.actionState = 103;
                  if (Game.player.actionState == 0 || Game.player.actionState == 2) {
                     Game.player.requestAnimation(25);
                  }

                  Game.player.stopMotion();
                  return;
               }

               this.ad();
            }

            if (this.as != -1) {
               this.au = this.au - Game.tickDeltaMs;
               if (this.au <= 0) {
                  this.ad();
                  return;
               }
            }
            break;
         case 100:
            this.ax = this.ax + Game.tickDeltaMs;
            if (this.ax < 830 && this.animationFrame != 1) {
               break;
            }

            if (this.au > 0) {
               Game.k = true;
               switch (this.animationId) {
                  case 0:
                     Game.health += 6;
                     if (Game.health > Game.maxHealth) {
                        Game.health = Game.maxHealth;
                     }

                     if (!bm) {
                        bm = true;
                        b(0, 54, true);
                     }
                  case 1:
                  case 3:
                  default:
                     break;
                  case 2:
                     Game.magic += 6;
                     if (Game.magic > Game.maxMagic) {
                        Game.magic = Game.maxMagic;
                     }

                     if (!bn) {
                        bn = true;
                        b(0, 55, true);
                     }
               }
            }

            this.aw--;
            if (this.aw <= 0) {
               this.actionState = 101;
               Game.aR = true;
               return;
            }

            this.actionState = 102;
            return;
         case 101:
            Game.aR = false;
            if (this.isAnimationEnding()) {
               Game.removeActor(this);
               return;
            }
            break;
         case 102:
            if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
               this.ad();
               this.ax = 0;
               return;
            }
            break;
         case 103:
            int var3 = Game.player.collisionBounds[3] - Game.player.collisionBounds[1];
            this.positionX = Game.player.positionX;
            this.positionY = Game.player.positionY - var3 - 20;
            Game.F = Game.player.positionX;
            Game.G = Game.player.positionY;
            Game.f(this.animationId == 1 ? 10 : 11);
            e(Game.bo);
            this.actionState = 104;
            return;
         case 104:
            if (!Game.aS) {
               this.ad();
               this.ax = 0;
               this.velocityX = 0;
               this.velocityY = 0;
            }
            break;
         case 105:
            this.az = this.az - Game.tickDeltaMs;
            if (this.az <= 0) {
               this.actionState = 0;
               return;
            }
            break;
         case 106:
            this.az = this.az - Game.tickDeltaMs;
            if (this.az <= 0) {
               this.actionState = 0;
               this.flags &= -129;
            }

            return;
      }
   }

   final void ad() {
      this.actionState = 100;
      int var1;
      if ((var1 = this.animationId >> 1) > 2) {
         var1 = 2;
      }

      this.spawnActor(39, 1, 11 + var1, 0, 0, 308, false);
   }

   public static final void b(int var0, int var1, boolean var2) {
      Game.aU = Game.getString(var0, var1);
      Game.font.wrapText(Game.aU, 210);
      Game.ba = 0;
      if (var2) {
         if (Game.levelIndex == 3) {
            Game.player.stopMotion();
            Game.bn.positionX = Game.bm.positionX = Game.player.positionX;
            Game.bn.positionY = Game.bm.positionY = Game.player.positionY;
         }

         Game.f(14);
      }
   }

   public static final void a(int var0, int var1, boolean var2, boolean var3) {
      Game.aU = Game.getString(var0, var1);
      Game.font.wrapText(Game.aU, var3 ? 175 : 210);
      Game.ba = 0;
      Game.E = 0;
      Game.aY = 0;
      Game.aZ = 1;
      Game.aW = Game.player.positionX - Game.cameraX;
      Game.aX = Game.player.positionY - Game.cameraY;
      if (var2) {
         Game.f(5);
      }
   }

   public final void ae() {
      if ((this.flags & 256) == 0) {
         if (this.animationId == 0) {
            this.requestAnimation(1);
         }

         this.actionState = 101;
      } else {
         if ((this.flags & 256) != 0 && this.au == 3) {
            if (this.animationId == 0) {
               this.requestAnimation(1);
            }

            this.actionState = 101;
            this.at = 5;
         }
      }
   }

   public final void af() {
      if (this.w != null && (!rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds) || Game.player.animationId > 1)) {
         Game.removeActor(this.w);
         this.w = null;
         this.actionState = 0;
      }
   }

   // $VF: renamed from: ag () void
   final void updateContainer() {
      this.refreshBounds();
      this.N = false;
      if ((this.flags & 256) != 0) {
         this.velocityY = 0;
      }

      if ((this.flags & 256) != 0 && (this.flags & 32) != 0) {
         this.changeActionState(0);
      } else {
         switch (this.actionState) {
            case 0:
               this.resolveTileCollisions(false, false);
               if ((this.flags & 256) == 0 && !this.grounded) {
                  this.actionState = 103;
               }

               if (this.animationId == 0 && rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds) && Game.player.animationId <= 1) {
                  if (this.w == null && (this.flags & 256) == 0) {
                     this.w = this.spawnActor(21, 18, 25, 0, -80, 0, true);
                     this.w.flags &= -2;
                     this.w.facingLeft = false;
                     this.w.paintPriority = 91;
                     this.w.ay = 3;
                  }

                  this.actionState = 106;
               }

               this.af();
               return;
            case 101:
               if (this.isAnimationEnding()) {
                  boolean var1 = false;
                  int var2;
                  if (this.au < 0 && ((var2 = -this.au) == 5 && bs || var2 == 2 && bu)) {
                     this.au = 0;
                     var1 = true;
                  }

                  if (this.au >= 0) {
                     boolean var6 = false;
                     if (this.animationId == 1 && this.au != 2) {
                        var6 = true;
                     }

                     int var3 = 1;
                     byte var4 = 0;
                     if (!var6) {
                        var3 = Game.randomRange(4, 7);
                     }

                     if (this.au == 2) {
                        var4 = 1;
                        if (Game.randomRange(0, 50) == 1) {
                           var3 = Game.randomRange(10, 30);
                        }
                     }

                     if (var1) {
                        var6 = false;
                        var4 = 0;
                     }

                     for (int var5 = 0; var5 < var3; var5++) {
                        this.spawnActor(
                           35,
                           33,
                           0 + (!var6 ? 0 : 1) + 2 * this.au + var4,
                           !var6 ? Game.randomRange(-40, 40) : 0,
                           !var6 ? Game.randomRange(-70, 0) : 0,
                           308,
                           false
                        );
                        lastSpawnedActor.facingLeft = false;
                        lastSpawnedActor.ay = cs;
                     }
                  } else if (-this.au == 2) {
                     this.spawnActor(48, 27, -this.au, 0, 0, 308, false);
                  } else {
                     this.spawnActor(48, 18, 35, 0, 0, 308, false);
                  }

                  if (this.animationId == 1) {
                     this.requestAnimation(2);
                  }

                  this.aw = 1000;
                  this.actionState = 102;
                  this.flags |= 16;
                  return;
               }
               break;
            case 102:
               this.decrementTimerA();
               if (this.animationId == 2) {
                  if (this.aw < 0) {
                     this.requestAnimation(3);
                     return;
                  }
               } else if (this.isAnimationEnding()) {
                  Game.removeActor(this);
                  return;
               }
               break;
            case 103:
               this.decrementTimerA();
               if (this.grounded) {
                  this.stopMotion();
                  if (this.aw <= 0) {
                     this.changeActionState(0);
                     return;
                  }
               } else {
                  this.accelerationY = 768;
                  this.resolveTileCollisions(false, false);
                  if (this.velocityY > 2560) {
                     this.velocityY = 2560;
                     this.accelerationY = 0;
                  }

                  if (this.velocityY > 0 && this.tileAtFeet >= 15) {
                     this.snapToGround();
                     this.stopMotion();
                  }
               }
               break;
            case 104:
               this.decrementTimerA();
               if (this.aw <= 0) {
                  this.actionState = 105;
                  this.requestAnimation(0);
                  return;
               }
               break;
            case 105:
               if (rectsOverlapInclusive(Game.player.attackBounds, this.collisionBounds)) {
                  this.requestAnimation(1);
                  this.actionState = 101;
                  return;
               }
               break;
            case 106:
               if (Game.wasKeyPressed(65568)) {
                  this.ae();
                  if (this.w != null) {
                     Game.removeActor(this.w);
                     this.w = null;
                  }
               }

               this.af();
               return;
         }
      }
   }

   // $VF: renamed from: r (b) void
   private void applyEffectDamage(Actor var1) {
      if (var1.type == 70) {
         if (var1.actionState != 103) {
            var1.changeActionState(2);
            return;
         }

         if (!this.dj) {
            var1.applyDamage(ct[m(this.animationId)], true);
            return;
         }
      } else {
         var1.applyDamage(ct[m(this.animationId)], true);
      }
   }

   // $VF: renamed from: A (int) void
   private void applyWeaponEffectHits(int var1) {
      if (this.collisionBounds != null) {
         for (int var3 = 0; var3 < Game.updateActorCount; var3++) {
            Actor var2;
            if ((var2 = Game.updateActors[var3]) != null && var2 != this) {
               if (var2.collisionBounds != null
                  && var2.actionState != 5
                  && var2.actionState != 6
                  && var2.actionState != 7
                  && (rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds) || rectsOverlapInclusive(var2.collisionBounds, this.attackBounds))) {
                  if ((var2.flags & 1024) != 0) {
                     continue;
                  }

                  if (var2.az <= 0 && var2.isDamageable()) {
                     if (var1 == 0 && (this.flags & 128) == 0) {
                        if (this.animationId == 16 || this.animationId == 17) {
                           if (this.dj) {
                              var2.applyDamage(bL[0], true);
                              this.stopMotion();
                              this.requestAnimation(17);
                              Game.playSoundOnce(-1);
                           } else if (var2.type != 90 && var2.type != 100) {
                              var2.applyDamage(bL[0 + bO[0]] * weaponDamagePercent(0, var2.type) / 100, true);
                           } else {
                              var2.applyDamage(bL[0 + bO[0]] * weaponDamagePercent(0, var2.type) / 100, false);
                           }
                        }
                     } else if (var1 == 1 && this.animationId == 7) {
                        if (var2.type != 90 && var2.type != 100) {
                           var2.applyDamage(bL[3 + bO[1]] * weaponDamagePercent(1, var2.type) / 100, true);
                        } else {
                           var2.applyDamage(bL[3 + bO[1]] * weaponDamagePercent(1, var2.type) / 100, false);
                        }

                        this.requestAnimation(1);
                        this.velocityX = 0;
                     }
                  } else if (var1 == 1 && var2.type == 90 && var2.actionState >= 126 && var2.actionState <= 128) {
                     var2.E();
                     this.requestAnimation(1);
                     this.velocityX = worldScrollPixelsPerTick << 8;
                  } else if (var2.type == 14) {
                     if (var1 == 1) {
                        this.requestAnimation(1);
                        this.velocityX = 0;
                        if (var2.actionState != 104 && var2.actionState != 105 && var2.actionState != 106) {
                           var2.changeActionState(var2.bU ? 105 : 104);
                        }
                     }
                  } else if (var2.type == 37) {
                     if (var1 == 1
                        && (rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds) || rectsOverlapInclusive(var2.attackBounds, this.collisionBounds))
                        )
                      {
                        if (var2.actionState != 100) {
                           var2.actionState = 100;
                           var2.requestAnimation(1);
                           var2.aw = 10000;
                           var2.ax = 500;
                        }

                        this.requestAnimation(1);
                        this.velocityX = 0;
                     }
                  } else if (var2.type == 75) {
                     if (var2.actionState == 0
                        && (rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds) || rectsOverlapInclusive(var2.attackBounds, this.collisionBounds))
                        )
                      {
                        var2.activateTrap75();
                     }
                  } else if (var2.type == 33 && var2.animationId == 0 && rectsOverlapInclusive(var2.collisionBounds, this.attackBounds) && var1 == 1) {
                     this.setAnimationImmediate(8);
                     var2.setAttachedActor(this);
                     this.s = var2;
                     if (this.facingLeft) {
                        this.positionX = var2.collisionBounds[2];
                     } else {
                        this.positionX = var2.collisionBounds[0];
                     }

                     this.ar = this.positionY - var2.positionY;
                     this.velocityX = 0;
                     this.velocityY = 0;
                  } else if (var2.type == 89) {
                     if (var1 == 0 && var2.actionState == 0) {
                        var2.changeActionState(101);
                        var2.aw = var2.au;
                     }
                  } else if (var2.type == 16 && var1 == 0) {
                     if (var2.actionState == 103 || var2.aw <= 0 && var2.actionState == 102) {
                        var2.changeActionState(104);
                     }
                  } else if (var2.type == 61 && var1 == 0 && (this.flags & 128) == 0) {
                     if (var2.animationId < 12 && var2.damageDestructibleCells(this.collisionBounds, false, 61)) {
                     }
                  } else if (var2.type == 70) {
                     if (var2.actionState != 2) {
                        if (var2.variantData == 2) {
                           if (var2.actionState == 111) {
                              var2.changeActionState(2);
                           } else if (var2.actionState == 110 || var2.actionState == 0) {
                              var2.changeActionState(111);
                           }
                        } else if (var2.variantData == 3) {
                           var2.moduleMask = null;
                           var2.O = true;
                           var2.variantData = 1;
                           var2.changeActionState(2);
                        } else {
                           var2.applyDamage(bL[5] * weaponDamagePercent(1, var2.type) / 100, true);
                        }
                     }

                     if (var1 == 1) {
                        this.requestAnimation(1);
                        this.velocityX = 0;
                     }
                  } else if (var2.type == 45 && var2.actionState == 0 && var2.animationId == 0) {
                     int[] var4;
                     (var4 = new int[4])[0] = var2.collisionBounds[0];
                     var4[2] = var2.collisionBounds[2];
                     var4[1] = var2.collisionBounds[1] - 3;
                     var4[3] = var2.collisionBounds[3];
                     if (rectsOverlapInclusive(var4, this.collisionBounds)) {
                        var2.au = this.positionX;
                        this.setCollisionRectEmpty(var2.collisionBounds, true);
                        var2.actionState = 100;
                        Game.playSoundOnce(-1);
                        if (this.dj) {
                           this.stopMotion();
                           this.requestAnimation(17);
                        }
                     }
                  }
               }

               if (var2.type == 70 && var2.variantData == 3 && (var2.actionState == 114 || var2.actionState == 115)) {
                  var2.refreshBounds();
                  if (segmentCrossesRectEdges(var2.as, var2.av, var2.positionX, var2.collisionBounds[3] + var2.collisionBounds[1] >> 1, this.collisionBounds)) {
                     var2.moduleMask = null;
                     var2.O = true;
                     var2.variantData = 1;
                     var2.changeActionState(2);
                  }
               }
            }
         }
      }
   }

   static final int m(int var0) {
      switch (var0) {
         case 7:
         case 16:
            return 4;
         default:
            return 0;
      }
   }

   // $VF: renamed from: bG () void
   private void updateEffect() {
      this.decrementTimerA();
      this.refreshBounds();
      switch (this.animationId) {
         case 2:
         case 27:
            this.l(0);
         case 3:
         case 4:
         case 5:
         case 6:
         case 11:
         case 12:
         case 15:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 28:
         default:
            if (this.isAnimationEnding()) {
               Game.removeActor(this);
            }
            break;
         case 7:
            this.applyWeaponEffectHits(1);
            if (!rectsOverlapInclusive(this.collisionBounds, Game.viewBounds)) {
               Game.removeActor(this);
               return;
            }

            if (this.collisionBounds[0] > Game.au.collisionBounds[0] + 1) {
               if (this.collisionBounds[2] >= Game.au.collisionBounds[2] - 1) {
                  return;
               }

               if (this.probeForwardSurface() || this.wallOnLeft && this.facingLeft || this.wallOnRight && !this.facingLeft) {
                  if (aR[0] == this) {
                     this.requestAnimation(8);
                     this.velocityX = 0;
                  } else {
                     Game.removeActor(this);
                  }
               }

               this.resolveTileCollisions(true, false);
               return;
            }
            break;
         case 8:
            if (this.isAnimationEnding()) {
               this.requestAnimation(9);
               this.aw = 10000;
               return;
            }
            break;
         case 9:
            if (this.aw < 0 || !this.probeForwardSurface() && (!this.wallOnLeft || !this.facingLeft) && (!this.wallOnRight || this.facingLeft)) {
               this.requestAnimation(10);
               this.aw = 1000;
            }

            this.d(0, this.collisionBounds);
            this.resolveTileCollisions(true, false);
            return;
         case 10:
            if (this.s != null) {
               this.s.supportActor = null;
               this.s.setAttachedActor(null);
               this.s = null;
            }

            if (playerPlatform == this) {
               playerPlatform = null;
            }

            if (this.aw < 0 && this.animationId == 10 && this.isAnimationEnding()) {
               Game.removeActor(this);
               return;
            }
            break;
         case 13:
            return;
         case 14:
            if (activeBoss != null) {
               if (activeBoss.type != 100) {
                  return;
               }

               if (this.actionState == 101) {
                  if (this.at < 30) {
                     this.at += 5;
                  } else {
                     if (activeBoss.actionState == 101) {
                        activeBoss.changeActionState(102);
                     }

                     this.actionState = 102;
                  }
               } else if (this.actionState == 102) {
                  this.at = 30;
                  if (this.aw <= 0) {
                     this.au = this.au + this.ar;
                     this.as = this.as + absInt(this.ar);
                  }

                  if (this.as >= (3 - Math.min(activeBoss.ay, 98) / 33) * 90) {
                     this.actionState = 103;
                     this.aw = 1000;
                  }
               } else if (this.actionState == 103 && this.aw <= 0) {
                  for (int var6 = 0; var6 < 4; var6++) {
                     Game.removeActor(aP[var6]);
                     Game.removeActor(aQ[var6]);
                  }

                  aP = null;
                  aQ = null;
                  activeBoss.changeActionState(103);
                  return;
               }

               this.positionX = activeBoss.positionX + (this.at * Game.cos256(this.ax * 360 / 4 + this.au) >> 8);
               this.positionY = activeBoss.positionY + (this.at * Game.sin256(this.ax * 360 / 4 + this.au) >> 8) - 40;
               if (aQ != null) {
                  raycastTiles(activeBoss.positionX, activeBoss.positionY - 40, this.positionX, this.positionY, ce);
                  aQ[this.ax].positionX = ce[0];
                  aQ[this.ax].positionY = ce[1];
                  return;
               }
            }
            break;
         case 16:
            if (this.dj) {
               this.ac = this.positionX;
               this.ad = this.positionY;
               this.resolveTileCollisions(false, false);
               this.positionX = this.ac;
               this.positionY = this.ad;
               if (this.grounded || this.ceilingHit || this.wallOnLeft || this.wallOnRight) {
                  this.requestAnimation(17);
                  Game.playSoundOnce(-1);
                  this.stopMotion();
                  return;
               }
            } else {
               if (this.at < 30) {
                  this.at = 30;
                  this.au += 10;
                  this.aw = 0;
               } else {
                  this.at += 3;
                  this.au += 3;
               }

               int var5 = this.ax * 360 / aN + this.au;
               this.positionX = Game.player.positionX + (this.at * Game.cos256(var5) >> 8);
               this.positionY = Game.player.positionY + (this.at * Game.sin256(var5) >> 8) - 22;
               if (this.aw <= 0) {
                  this.requestAnimation(17);
                  this.flags &= -129;
                  Game.playSoundOnce(-1);
               }
            }

            this.applyWeaponEffectHits(0);
            return;
         case 17:
            if (!this.dj) {
               if (!this.isAnimationEnding()) {
                  this.applyWeaponEffectHits(0);
                  return;
               }

               if (aM != null) {
                  for (int var4 = 0; var4 < aN; var4++) {
                     if (aM[var4] == this) {
                        aM[var4] = null;
                        break;
                     }
                  }
               }

               Game.removeActor(this);
               return;
            }

            if (this.isAnimationEnding() && this.animationId == 17) {
               Game.removeActor(this);
               return;
            }
            break;
         case 26:
            this.resolveTileCollisions(true, true);
            this.velocityY = 3584;

            for (int var2 = Game.paintActorCount; var2 >= 0; var2--) {
               Actor var1;
               if ((var1 = Game.paintActors[var2]) != null
                  && var1 != this
                  && var1.collisionBounds != null
                  && this.collisionBounds != null
                  && rectsOverlapInclusive(var1.collisionBounds, this.collisionBounds)) {
                  if (var1.isDamageable() && (var1.flags & 1024) == 0 && var1.az <= 0) {
                     this.applyEffectDamage(var1);
                     this.velocityY = 0;
                     this.setAnimationImmediate(27);
                  } else if (var1 == Game.player) {
                     Game.player.a(4, 10, 0, this);
                     this.velocityY = 0;
                     this.setAnimationImmediate(27);
                  }
               }
            }

            if (this.grounded) {
               this.velocityY = 0;
               if (this.variantData != 2) {
                  this.setAnimationImmediate(27);
                  Game.playSoundOnce(-1);
                  return;
               }

               if (this.f(this.collisionBounds)) {
                  this.setAnimationImmediate(27);
                  return;
               }

               for (int var7 = 0; var7 < Game.updateActorCount; var7++) {
                  Actor var3;
                  if ((var3 = Game.updateActors[var7]) != null
                     && (var3.type == 83 || var3.type == 84 || var3.type == 85)
                     && rectsOverlapInclusive(var3.collisionBounds, this.collisionBounds)) {
                     this.setAnimationImmediate(27);
                     return;
                  }
               }
            }
            break;
         case 29:
            this.decrementTimerA();
            this.velocityY = -1024;
            this.l(this.velocityY);
            if (this.aw < 0) {
               Game.removeActor(this);
               return;
            }
      }
   }

   // $VF: renamed from: bH () void
   private void updateThrownObject() {
      switch (this.actionState) {
         case 102:
            this.decrementTimerA();
            this.bJ();
            if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
               this.changeActionState(105);
            } else if (this.aw <= 0 && rectsOverlapInclusive(Game.player.attackBounds, this.collisionBounds)) {
               this.changeActionState(104);
            }
            break;
         case 103:
            this.decrementTimerA();
            if (this.aw <= 0) {
               this.changeActionState(105);
            } else if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
               this.changeActionState(105);
            } else if (rectsOverlapInclusive(Game.player.attackBounds, this.collisionBounds)) {
               this.changeActionState(104);
            }
            break;
         case 104:
            this.accelerationX = this.facingLeft ? 32 : -32;
            if (absInt(this.velocityY) < 3584) {
               this.accelerationY = 512;
            }

            this.bK();
            break;
         case 105:
            if (this.isAnimationEnding()) {
               this.B(1000);
            } else {
               if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
                  Game.player.a(4, 15, 0, this);
               }

               this.bK();
            }
      }

      if (!this.o()) {
         ;
      }
   }

   private void m(int var1, int var2) {
      this.spawnActor(16, 4, 0, 0, 0, 101, false);
      lastSpawnedActor.changeActionState(101);
      this.setAttachedActor(lastSpawnedActor);
      lastSpawnedActor.positionChild(this.attachedActor, var1, var2);
      this.flags &= -17;
      lastSpawnedActor.flags |= 16;
   }

   private void B(int var1) {
      lastSpawnedActor = Game.d(this);
      if (lastSpawnedActor != null) {
         lastSpawnedActor.ax = var1;
         lastSpawnedActor.attachedActor = null;
      }

      Game.removeActor(this);
   }

   private void n(int var1, int var2) {
      if (this.attachedActor != null && this.attachedActor.type == 16 && this.attachedActor.actionState == 101) {
         this.attachedActor.changeActionState(102);
         this.attachedActor.stopMotion();
         this.attachedActor.velocityX = var1;
         this.attachedActor.velocityY = var2;
         this.attachedActor.flags |= 16;
         this.setAttachedActor(null);
      }
   }

   private void bI() {
      if (this.attachedActor != null && this.attachedActor.type == 16 && this.attachedActor.actionState == 101) {
         this.attachedActor.changeActionState(105);
      }
   }

   private int a(int var1, int var2, int var3, int var4, int var5, int var6) {
      long var7 = var6 >> 1;
      long var9 = var5;
      long var11 = var2 - var4;
      if (var9 * var9 - 4L * var7 * var11 < 0L) {
         return 0;
      }

      long var13 = integerSqrt(var9 * var9 - 4L * var7 * var11);
      long var15 = (-var9 - var13) / (2L * var7);
      long var17 = 0L;
      long var19 = 0L;
      var19 = 0L;
      var19 = var15;
      return var15 == 0L ? 0 : (int)((var3 - var1) / var19);
   }

   private void bJ() {
      this.accelerationX = this.facingLeft ? 32 : -32;
      if (this.velocityY < 3584) {
         this.accelerationY = 512;
      } else {
         this.velocityY = 3584;
      }

      if (this.facingLeft) {
         if (this.velocityX > 0) {
            this.velocityX = 0;
         }
      } else if (this.velocityX < 0) {
         this.velocityX = 0;
      }

      if (this.velocityX > 0 && this.wallOnRight || this.velocityX < 0 && this.wallOnLeft) {
         this.facingLeft = !this.facingLeft;
         this.velocityX = -this.velocityX / 2;
      }

      this.resolveTileCollisions(true, false);
      if (this.velocityY < 0) {
         if (this.tileAtHead >= 24) {
            this.positionY = this.positionY + 20 - this.collisionBounds[1] % 20 + 1;
            this.velocityY = -this.velocityY / 2;
            return;
         }
      } else if (this.velocityY >= 0) {
         int var1 = Game.getCollisionTile(this.positionX / 20, this.positionY / 20);
         if (3 == Game.levelModes[Game.levelIndex] && this.positionY >= Game.levelPixelHeight) {
            var1 = 0;
         }

         if (var1 >= 15) {
            this.refreshBounds();
            this.positionY = (this.collisionBounds[3] + 1) / 20 * 20;
            this.positionY--;

            while (Game.getCollisionTile(this.positionX / 20, this.positionY / 20) >= 24) {
               this.positionY -= 20;
            }

            if (this.velocityY > 2048) {
               this.velocityY = -this.velocityY / 2;
            } else if (Game.approximateDistance(this.velocityX, this.velocityY) < 1280) {
               this.velocityX = this.velocityY = this.accelerationX = this.accelerationY = 0;
               this.grounded = true;
               if (this.type != 1) {
                  this.changeActionState(103);
               }
            } else {
               this.velocityY = 0;
            }

            if (Game.approximateDistance(this.velocityX, this.velocityY) < 1280) {
               int var2 = 0;
               int var3 = this.positionX % 20 >> 1;
               if (this.tileAtFeet == 18) {
                  var2 = 0 - var3;
               } else if (this.tileAtFeet == 19) {
                  var2 = 0 - (var3 + 10);
               } else if (this.tileAtFeet == 20) {
                  var2 = 0 - (10 - var3 + 10);
               } else if (this.tileAtFeet == 21) {
                  var2 = 0 - (10 - var3);
               }

               if (this.tileAtFeet == 16) {
                  var2 -= this.positionX % 20;
               } else if (this.tileAtFeet == 17) {
                  var2 -= 20 - this.positionX % 20;
               }

               this.positionY -= var2;
               this.velocityX = this.velocityY = this.accelerationX = this.accelerationY = 0;
               this.grounded = true;
               if (this.type != 1) {
                  this.changeActionState(103);
               }
            }
         }
      }
   }

   private void bK() {
      if (this.actionState != 105) {
         this.resolveTileCollisions(true, false);
         if (this.tileAtHead >= 24 || this.tileAtFeet >= 24 || this.tileAtLeftSide >= 24 || this.tileAtRightSide >= 24) {
            int var1 = this.probeVerticalTiles();
            if (this.velocityY >= 0 && this.tileAtFeet >= 15) {
               this.positionY -= var1;
            }

            this.changeActionState(105);
         }
      }

      for (int var3 = 0; var3 < Game.updateActorCount; var3++) {
         Actor var2;
         if ((var2 = Game.updateActors[var3]) != null
            && (var2.flags & 1024) == 0
            && var2.actionState != 7
            && var2.actionState != 3
            && var2.actionState != 5
            && var2.actionState != 6) {
            if (var2.isDamageable() && var2.collisionBounds != null && rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds)) {
               var2.applyDamageWithReaction(500);
               if (this.actionState != 105) {
                  this.changeActionState(105);
               }
            } else if (var2.type == 79 && rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds)) {
               var2.damageDestructibleCells(this.collisionBounds, false, 79);
               if (this.actionState != 105) {
                  this.changeActionState(105);
               }
            } else if (var2.type == 45 && var2.animationId == 0 && rectsOverlapInclusive(var2.collisionBounds, this.collisionBounds)) {
               this.setCollisionRectEmpty(var2.collisionBounds, true);
               var2.actionState = 100;
               Game.playSoundOnce(-1);
            }
         }
      }
   }

   // $VF: renamed from: n (int) void
   public final void changeActionState(int var1) {
      this.changeActionStateInternal(var1, false);
   }

   // $VF: renamed from: c (int, boolean) void
   private void changeActionStateInternal(int var1, boolean var2) {
      if (var2 || var1 != this.actionState) {
         label480:
         switch (this.type) {
            case 1:
               switch (var1) {
                  case 1:
                     this.requestAnimation(1);
                     break label480;
                  case 2:
                     this.requestAnimationRestart(3, true);
                     this.facePlayerPixels();
                     this.stopMotion();
                     this.velocityX = -this.horizontalSpeedTowardPlayerDeadZone(256);
                     break label480;
                  case 3:
                     this.spawnActor(39, 1, 3, 0, -20, 302, this.facingLeft);
                     break label480;
                  case 101:
                     this.requestAnimation(1);
                     break label480;
                  case 102:
                     if (this.getBehaviorVariant() == 0) {
                        this.requestAnimation(2);
                     }

                     this.velocityX = 0;
                     break label480;
                  case 103:
                     this.requestAnimation(0);
                     this.stopMotion();
                     if (this.getBehaviorVariant() == 1) {
                        this.aw = 500;
                     } else {
                        this.aw = 400;
                     }
                  default:
                     break label480;
               }
            case 2:
               switch (var1) {
                  case 2:
                     if (this.getBehaviorVariant() != 0 && this.getBehaviorVariant() != 1) {
                        this.requestAnimationRestart(8, true);
                     } else {
                        this.requestAnimationRestart(7, true);
                     }

                     this.facePlayerPixels();
                     this.stopMotion();
                     this.velocityX = -this.horizontalSpeedTowardPlayerDeadZone(256);
                     break label480;
                  case 3:
                     this.spawnActor(39, 1, 3, 0, -5, 302, this.facingLeft);
                     break label480;
                  case 101:
                     this.requestAnimation(2);
                     break label480;
                  case 102:
                     this.requestAnimation(4);
                     this.stopMotion();
                     break label480;
                  case 103:
                     this.requestAnimation(2);
                     this.aw = 500;
                     break label480;
                  case 111:
                     this.requestAnimation(6);
                     break label480;
                  case 112:
                     this.requestAnimation(1);
                     this.aw = 500;
                     break label480;
                  case 113:
                     this.requestAnimation(3);
                     this.stopMotion();
                     this.aw = 800;
                     break label480;
                  case 114:
                     cr[0] = this.collisionBounds[0];
                     cr[2] = this.collisionBounds[2];
                     cr[1] = this.collisionBounds[1];
                     cr[3] = this.collisionBounds[3];
                     this.collisionBounds[0] = this.attackBounds[0];
                     this.collisionBounds[2] = this.attackBounds[2];
                     this.collisionBounds[1] = this.attackBounds[1];
                     this.collisionBounds[3] = this.attackBounds[3];
                     this.resolveTileCollisions(true, false);
                     this.collisionBounds[0] = cr[0];
                     this.collisionBounds[2] = cr[2];
                     this.collisionBounds[1] = cr[1];
                     this.collisionBounds[3] = cr[3];
                     if (this.grounded || this.wallOnLeft || this.wallOnRight) {
                        this.changeActionStateInternal(112, true);
                        return;
                     }

                     if (this.attachedActor != null && this.attachedActor.actionState == 101) {
                        this.requestAnimation(6);
                        this.n(0, 0);
                        this.stopMotion();
                     }
                     break label480;
                  case 115:
                     this.requestAnimation(6);
                     this.aw = 2000;
                     this.stopMotion();
                     break label480;
                  case 116:
                     this.requestAnimation(3);
                     this.facingLeft = this.dj;
                     this.velocityX = this.dj ? -768 : 768;
                     break label480;
                  case 119:
                     this.setAnimationImmediate(2);
                     this.velocityY = this.aF == 4 ? 3072 : -3072;
                     break label480;
                  case 120:
                     this.stopMotion();
                     this.aw = 500;
                     break label480;
                  case 121:
                     this.requestAnimation(16);
                     this.dj = false;
                     this.aw = 600;
                     break label480;
                  case 122:
                     this.requestAnimation(2);
                     this.aw = 400;
                     break label480;
                  case 123:
                     this.velocityX = this.facingLeft ? 3072 : -3072;
                     break label480;
                  case 124:
                     this.setAnimationImmediate(3);
                     this.velocityX = this.aF == 1 ? 1536 : -1536;
                     if (this.attachedActor == null) {
                        this.m(this.horizontalSpeedTowardPlayer(3), 6);
                        lastSpawnedActor.scrollWithWorld = true;
                     }
                     break label480;
                  case 125:
                     this.stopMotion();
                     this.aw = 500;
                     break label480;
                  case 126:
                     if (this.attachedActor != null && this.attachedActor.actionState == 101) {
                        this.requestAnimation(6);
                        this.n(0, 0);
                        this.stopMotion();
                     }
                     break label480;
                  case 127:
                     this.aw = 400;
                     break label480;
                  case 128:
                     this.velocityX = this.facingLeft ? -2560 : 2560;
                     break label480;
                  case 129:
                     this.setAnimationImmediate(2);
                     this.velocityY = this.aF == 4 ? 1536 : -1536;
                     break label480;
                  case 130:
                     this.stopMotion();
                     break label480;
                  case 131:
                     this.setAnimationImmediate(5);
                     this.facePlayerPixels();
                     this.dd = Game.e(Game.player.boundsCenterX - this.positionX, Game.player.boundsCenterY - this.positionY);
                     this.velocityX = this.dd == 0 ? 0 : (Game.player.boundsCenterX - this.positionX) * 3072 / this.dd;
                     this.velocityY = this.dd == 0 ? 0 : (Game.player.boundsCenterY - this.positionY) * 3072 / this.dd;
                     break label480;
                  case 132:
                     this.aw = 1500;
                     break label480;
                  case 133:
                     this.facingLeft = !this.facingLeft;
                     this.velocityX = this.facingLeft ? -384 : 384;
                     this.velocityY = -384;
                     break label480;
                  case 135:
                     this.setAnimationImmediate(2);
                     this.ac = this.positionX;
                     this.ad = this.positionY;
                     this.aw = 2000;
                     break label480;
                  case 138:
                     this.setAnimationImmediate(2);
                     break label480;
                  case 139:
                     this.setAnimationImmediate(4);
                     break label480;
                  case 200:
                     this.requestAnimation(2);
                     this.stopMotion();
                     this.aw = 500;
                  default:
                     break label480;
               }
            case 3:
               switch (var1) {
                  case 2:
                     this.requestAnimationRestart(13, true);
                     this.facePlayerPixels();
                     this.stopMotion();
                     this.velocityX = -this.horizontalSpeedTowardPlayerDeadZone(256);
                     break label480;
                  case 3:
                     this.spawnActor(39, 1, 3, 0, -5, 302, this.facingLeft);
                     break label480;
                  case 101:
                     this.facePlayerFixed();
                     this.requestAnimation(10);
                     break label480;
                  case 102:
                     this.requestAnimation(15);
                     this.aw = 700;
                     this.stopMotion();
                     break label480;
                  case 103:
                     this.requestAnimation(12);
                     this.velocityX = this.facingLeft ? -2048 : 2048;
                     this.aw = 1000;
                     break label480;
                  case 104:
                     this.requestAnimation(9);
                     this.aw = 500;
                     this.stopMotion();
                     break label480;
                  case 105:
                     this.requestAnimation(10);
                     this.aw = 1500;
                     break label480;
                  case 106:
                     this.requestAnimation(14);
                     this.stopMotion();
                     break label480;
                  case 107:
                     this.aw = 1800;
                     this.requestAnimation(9);
                     this.stopMotion();
                     break label480;
                  case 108:
                     this.requestAnimation(12);
                     this.facingLeft = this.dj;
                     this.velocityX = this.dj ? -1024 : 1024;
                     break label480;
                  case 109:
                     this.requestAnimation(9);
                     this.facingLeft = !this.dj;
                     this.stopMotion();
                  default:
                     break label480;
               }
            case 10:
               switch (var1) {
                  case 101:
                     this.aw = this.getStat(3);
                     break label480;
                  case 102:
                     this.requestAnimation(this.aF == 8 ? 1 : (this.aF == 4 ? 9 : 5));
                     break label480;
                  case 103:
                     this.requestAnimation(this.aF == 8 ? 2 : (this.aF == 4 ? 10 : 6));
                     this.aw = 500;
                     break label480;
                  case 104:
                     this.requestAnimation(this.aF == 8 ? 3 : (this.aF == 4 ? 11 : 7));
                     this.aw = this.getStat(1);
                     this.aA = 1;
                     break label480;
                  case 105:
                     this.requestAnimation(this.aF == 8 ? 1 : (this.aF == 4 ? 9 : 5));
                     this.aA = 0;
                     break label480;
                  case 106:
                     this.requestAnimation(this.aF == 8 ? 0 : (this.aF == 4 ? 8 : 4));
                     this.aw = this.getStat(2);
                     break label480;
                  case 107:
                  case 109:
                     this.requestAnimation(this.aF == 8 ? 3 : (this.aF == 4 ? 11 : 7));
                     this.aw = this.getStat(1);
                     break label480;
                  case 108:
                  case 110:
                     this.aw = this.getStat(2);
                  default:
                     break label480;
               }
            case 13:
               switch (var1) {
                  case 2:
                     this.requestAnimation(2);
                     this.stopMotion();
                     this.spawnFourWayProjectiles(1536, -256);
                     if (this.getBehaviorVariant() == 1) {
                        this.aw = 1000;
                     }
                     break label480;
                  case 3:
                     this.requestAnimation(2);
                     this.spawnActor(39, 1, 3, 0, -20, 302, this.facingLeft);
                     break label480;
                  case 101:
                     this.requestAnimation(1);
                     break label480;
                  case 102:
                     this.requestAnimation(2);
                     this.aw = this.getStat(3);
                     break label480;
                  case 103:
                     this.requestAnimation(0);
                     break label480;
                  case 104:
                     this.requestAnimation(0);
                     break label480;
                  case 105:
                     this.requestAnimation(3);
                     break label480;
                  case 106:
                     this.requestAnimation(0);
                     break label480;
                  case 107:
                     this.requestAnimation(3);
                  default:
                     break label480;
               }
            case 14:
               switch (var1) {
                  case 2:
                     if (this.animationId != 2 && this.animationId != 3) {
                        this.requestAnimationRestart(8, true);
                     }

                     this.facePlayerPixels();
                     this.stopMotion();
                     this.velocityX = -this.horizontalSpeedTowardPlayerDeadZone(256);
                     break label480;
                  case 3:
                     this.N = false;
                     this.requestAnimation(7);
                     this.facePlayerPixels();
                     this.stopMotion();
                     break label480;
                  case 101:
                     this.requestAnimation(1);
                     break label480;
                  case 102:
                     this.requestAnimation(4);
                     this.stopMotion();
                     this.ax = 100;
                     this.dj = false;
                     break label480;
                  case 103:
                     this.requestAnimation(0);
                     this.aw = 800;
                     this.stopMotion();
                     break label480;
                  case 104:
                     this.requestAnimation(2);
                     this.stopMotion();
                     break label480;
                  case 105:
                     this.requestAnimation(3);
                     this.stopMotion();
                     this.velocityY = 512;
                     this.N = true;
                     break label480;
                  case 106:
                     this.stopMotion();
                     break label480;
                  case 107:
                     this.N = false;
                     this.spawnActor(61, 40, 2, 0, 0, 302, false);
                  default:
                     break label480;
               }
            case 16:
               switch (var1) {
                  case 102:
                     this.requestAnimation(1);
                     this.aw = 800;
                     break label480;
                  case 103:
                     this.requestAnimation(2);
                     this.aw = 3000;
                     break label480;
                  case 104:
                     this.requestAnimation(2);
                     this.velocityX = -this.horizontalSpeedTowardPlayer(3328);
                     this.velocityY = -3328;
                     break label480;
                  case 105:
                     this.setAnimationImmediate(3);
                     this.stopMotion();
                     this.au = 0;
                  default:
                     break label480;
               }
            case 18:
               switch (var1) {
                  case 3:
                     this.requestAnimation(6);
                     if (this.s == Game.player) {
                        this.s.t = null;
                     }

                     this.s = null;
                     this.t = null;
                     this.stopMotion();
                     break label480;
                  case 101:
                     this.requestAnimation(5);
                     this.aimHomingProjectile();
                     this.aw = 5000;
                  default:
                     break label480;
               }
            case 19:
               switch (var1) {
                  case 101:
                     this.velocityX = this.facingLeft ? -3584 : 3584;
                  default:
                     break label480;
               }
            case 52:
               switch (var1) {
                  case 103:
                     this.aw = 500;
                  default:
                     break label480;
               }
            case 68:
               switch (var1) {
                  case 101:
                     int var8 = 0;
                     int var5 = 0;
                     switch (this.as) {
                        case 1:
                           var8 = Game.viewBounds[0] - this.positionX + 18;
                           break;
                        case 2:
                           var8 = Game.viewBounds[2] - this.positionX - 36;
                        case 3:
                        case 5:
                        case 6:
                        case 7:
                        default:
                           break;
                        case 4:
                           var5 = Game.viewBounds[1] - this.positionY + 18;
                           break;
                        case 8:
                           var5 = Game.viewBounds[3] - this.positionY - 18;
                     }

                     if (this.attachedActor == null) {
                        Actor var6;
                        (var6 = this.spawnActor(21, 18, 14, var8, var5, 100, false)).ay = 1;
                        var6.au = var6.positionX - Game.cameraX;
                        var6.ar = var6.positionY - Game.cameraY;
                        this.setAttachedActor(var6);
                     }
                     break label480;
                  case 102:
                     switch (this.getBehaviorVariant()) {
                        case 11:
                        case 13:
                        default:
                           break label480;
                        case 12:
                           this.aw = 600;
                           break label480;
                        case 14:
                        case 15:
                           this.aw = 300;
                           break label480;
                     }
                  case 103:
                     switch (this.getBehaviorVariant()) {
                        case 11:
                        case 13:
                        default:
                           break label480;
                        case 12:
                           this.aw = 400;
                     }
                  case 104:
                  default:
                     break label480;
               }
            case 70:
               this.facePlayerFixed();
               switch (var1) {
                  case 0:
                     this.flags &= -129;
                     break label480;
                  case 2:
                     this.velocityY = -1700;
                     if (this.attachedActor == null) {
                        this.facePlayerBeyondDistance(1200);
                        this.velocityX = -this.horizontalSpeedTowardPlayer(1200);
                     } else {
                        if (absInt(this.attachedActor.fixedX - this.fixedX) > absInt(1200)) {
                           this.facingLeft = this.attachedActor.positionX < this.positionX;
                        }

                        this.velocityX = (this.fixedX - this.attachedActor.fixedX < 0 ? -1 : 1) * 1200;
                        this.attachedActor = null;
                     }

                     if (this.variantData == 1) {
                        if (this.actionState == 106) {
                           this.at = 1;
                        } else {
                           this.requestAnimation(1);
                           this.at = 0;
                        }
                     } else if (this.variantData == 3) {
                        this.interactionBounds = null;
                        this.O = true;
                        this.variantData = 1;
                     }
                     break label480;
                  case 101:
                     this.velocityX = this.horizontalSpeedTowardPlayerDeadZone(1200);
                     this.au = 0;
                     this.aw = 0;
                     this.requestAnimation(5);
                     break label480;
                  case 102:
                     this.positionY = (this.positionY + 10) / 20 * 20;
                     this.stopMotion();
                     this.requestAnimation(4);
                     break label480;
                  case 103:
                     this.positionY = (this.positionY + 10) / 20 * 20;
                     this.requestAnimation(7);
                     if (this.dj) {
                        this.aw = 400;
                        this.dg = 0;
                     } else {
                        this.aw = 2000;
                     }
                     break label480;
                  case 104:
                     this.requestAnimation(2);
                     break label480;
                  case 105:
                     this.aw = 1024;
                     this.positionY = (this.positionY + 10) / 20 * 20;
                     this.requestAnimation(0);
                     break label480;
                  case 106:
                     this.requestAnimation(5);
                     break label480;
                  case 107:
                     this.stopMotion();
                     this.requestAnimation(1);
                     break label480;
                  case 108:
                     this.stopMotion();
                     this.requestAnimation(3);
                     break label480;
                  case 109:
                     this.requestAnimation(8);
                     break label480;
                  case 110:
                     this.stopMotion();
                     this.positionY = (this.positionY + 10) / 20 * 20;
                     this.requestAnimation(9);
                     break label480;
                  case 111:
                     this.at = 0;
                     this.stopMotion();
                     this.aw = 200;
                     this.setAnimationImmediate(2);
                     this.positionY += 27;
                     break label480;
                  case 112:
                     this.stopMotion();
                     if (Game.getCollisionTile(this.positionX / 20, this.positionY / 20 - 1) >= 15) {
                        this.velocityY = -425;
                     } else {
                        this.velocityY = -3400;
                     }

                     if (this.actionState != 111) {
                        this.at = 0;
                        this.requestAnimation(2);
                     } else {
                        this.at = 1;
                     }
                     break label480;
                  case 113:
                     this.stopMotion();
                     this.setAnimationImmediate(10);
                     this.positionY -= 27;
                     break label480;
                  case 114:
                     this.requestAnimation(6);
                     this.stopMotion();
                     this.moduleMask = new int[10];
                     this.moduleMask[8] = 0;
                     this.moduleMask[5] = this.moduleMask[4] = 1024;
                     this.moduleMask[0] = this.moduleMask[1] = 45;
                     this.moduleMask[3] = this.moduleMask[2] = 32;
                     this.aw = 0;
                     this.moduleMask[6] = this.ax = 1;
                     this.au = 1;
                     this.moduleMask[7] = this.at = this.moduleMask[4];
                     break label480;
                  case 115:
                     this.stopMotion();
                     this.at = 0;
                     this.as = this.positionX;
                     this.av = this.positionY;
                     this.setAnimationImmediate(2);
                     this.velocityY = 850;
                     this.positionY += 27;
                     break label480;
                  default:
                     this.stopMotion();
                     break label480;
               }
            case 73:
               switch (var1) {
                  case 100:
                     this.flags |= 128;
                     break label480;
                  case 101:
                     this.aw = 2000 + this.ar;
                     this.flags |= 128;
                     break label480;
                  case 102:
                     this.setAnimationImmediate(2);
                     this.aG = 1;
                     this.flags &= -129;
                     this.N = true;
                     break label480;
                  case 103:
                     Game.playSoundOnce(-1);
                     this.spawnActor(98, 30, 0, 0, 0, -1, false);
                     lastSpawnedActor.aG = 0;
                     lastSpawnedActor.flags |= 16;
                     this.stopMotion();
                     Game.startCameraShake(12, 8);
                     Game.removeActor(this.v);
                     this.v = null;
                     break label480;
                  case 104:
                     this.setAnimationImmediate(3);
                     this.refreshBounds();
                     if (Game.levelModes[Game.levelIndex] != 2) {
                        if (rectsOverlapInclusive(this.collisionBounds, Game.viewBounds) && this.ay != 1) {
                           if (this.facingLeft) {
                              this.positionX = Game.viewBounds[2] + (this.collisionBounds[2] - this.collisionBounds[0] >> 1);
                           } else {
                              this.positionX = Game.viewBounds[0] - (this.collisionBounds[2] - this.collisionBounds[0] >> 1);
                           }
                        }

                        this.refreshBounds();
                        this.snapDownToGround();
                     }
                  case 105:
                     this.setAnimationImmediate(3);
                     this.av = 1;
                     this.aG = 1;
                     this.flags |= 16;
                  default:
                     break label480;
               }
            case 89:
               switch (var1) {
                  case 101:
                     if (this.variantData == 0) {
                        this.requestAnimation(26);
                     }
                     break label480;
                  case 102:
                     if (this.variantData == 0) {
                        this.requestAnimation(25);
                     }
                  default:
                     break label480;
               }
            case 90:
               switch (var1) {
                  case 2:
                     this.requestAnimation(8);
                     break label480;
                  case 3:
                     this.requestAnimation(0);
                     this.stopMotion();
                     this.aw = 2500;
                     this.removeLinkedEffect();
                     Game.removeActor(this.x);
                     this.x = null;
                     j();
                     break label480;
                  case 101:
                     this.requestAnimation(0);
                     this.aw = 500;
                     break label480;
                  case 102:
                     this.requestAnimation(5);
                     this.aw = 100;
                     break label480;
                  case 103:
                     this.requestAnimation(0);
                     this.aw = 1000;
                     break label480;
                  case 104:
                     this.requestAnimation(2);
                     this.velocityX = 2048;
                     break label480;
                  case 105:
                     this.requestAnimation(0);
                     this.aw = 500;
                     break label480;
                  case 106:
                     this.requestAnimation(5);
                     this.aw = 800;
                     break label480;
                  case 107:
                     this.requestAnimation(0);
                     this.aw = 2000;
                     break label480;
                  case 109:
                     this.requestAnimation(5);
                     this.aw = 800;
                     this.v = this.spawnActor(16, 4, 4, 0, 0, 302, false);
                     this.stopMotion();
                     break label480;
                  case 110:
                     this.requestAnimation(5);
                     this.stopMotion();
                     break label480;
                  case 111:
                     this.requestAnimation(0);
                     this.aw = 1000;
                     this.stopMotion();
                     Game.removeActor(this.v);
                     this.v = null;
                     break label480;
                  case 112:
                     this.requestAnimation(4);
                     this.aw = 600;
                     break label480;
                  case 113:
                     this.aw = 800;
                     break label480;
                  case 114:
                     this.requestAnimation(0);
                     this.au = 120;
                     this.ar = 197 + Game.randomRange(-1, 2) * 80;
                     break label480;
                  case 115:
                     this.aw = 500;
                     this.stopMotion();
                     break label480;
                  case 116:
                     this.requestAnimation(3);
                     this.aw = 1000;
                     this.v = this.spawnActor(16, 4, 4, 0, 0, 302, false);
                     break label480;
                  case 117:
                     this.requestAnimation(4);
                     this.aw = 500;
                     Game.removeActor(this.v);
                     this.v = null;
                     break label480;
                  case 118:
                     this.requestAnimation(0);
                     this.aw = 0;
                     break label480;
                  case 119:
                     this.requestAnimation(2);
                     break label480;
                  case 120:
                     this.requestAnimation(0);
                     this.aw = 1000;
                     break label480;
                  case 121:
                     this.requestAnimation(6);
                     this.aw = 3000;
                     this.removeLinkedEffect();
                     this.showLinkedPrompt(25, this.positionX + -35, this.positionY + -90);
                     this.x = this.spawnPrompt(14, this.positionX + -35, this.positionY + -10);
                     break label480;
                  case 122:
                     this.requestAnimation(7);
                     this.aw = 1000;
                     break label480;
                  case 123:
                     this.requestAnimation(0);
                     break label480;
                  case 124:
                     Game.player.positionX = this.positionX + -35;
                     Game.player.positionY = this.positionY + -10;
                     Game.player.stopMotion();
                     break label480;
                  case 126:
                     this.aw = 3000;
                     bi = true;
                     this.at = 0;
                     this.removeLinkedEffect();
                     this.showLinkedPrompt(25, Game.player.positionX + -35, Game.player.positionY + -90);
                     Game.player.requestAnimation(23);
                     break label480;
                  case 128:
                     this.requestAnimation(8);
                     this.aw = 1500;
                     Game.removeActor(this.v);
                     this.v = null;

                     for (int var7 = 0; var7 < 6; var7++) {
                        this.spawnActor(61, 40, 0, 0, 0, 101, false);
                        lastSpawnedActor.positionX = this.positionX + (var7 % 2 * 2 - 1) * 15 + Game.randomRange(-7, 7);
                        lastSpawnedActor.positionY = this.positionY - var7 / 2 * 30 + Game.randomRange(-15, 15);
                        lastSpawnedActor.paintPriority = 319;
                        lastSpawnedActor.velocityX = worldScrollPixelsPerTick << 8;
                     }
                  default:
                     break label480;
               }
            case 100:
               switch (var1) {
                  case 3:
                  default:
                     break;
                  case 100:
                     this.velocityX = this.velocityY = 0;
                     this.accelerationX = this.accelerationY = 0;
                     this.requestAnimation(9);
                     this.aw = 1000;
                     break;
                  case 101:
                     this.requestAnimation(10);
                     this.aw = 1000;
                     break;
                  case 102:
                     this.requestAnimation(4);
                     aQ = new Actor[4];

                     for (int var4 = 0; var4 < 4; var4++) {
                        aQ[var4] = this.spawnActor(39, 1, 13, 0, 0, 302, false);
                        raycastTiles(this.positionX, this.positionY - 40, aP[var4].positionX, aP[var4].positionY, ce);
                        aQ[var4].positionX = ce[0];
                        aQ[var4].positionY = ce[1];
                     }
                     break;
                  case 103:
                     this.aw = 1000;
                     this.requestAnimation(3);
                     break;
                  case 104:
                     this.velocityX = this.velocityY = 0;
                     this.accelerationX = this.accelerationY = 0;
                     this.requestAnimation(6);
                     this.aw = 4000;
                     break;
                  case 105:
                     this.requestAnimation(9);
                     this.aw = 1000;
                     break;
                  case 108:
                     this.requestAnimation(1);
                     if (this.v != null) {
                        this.v.requestAnimation(1);
                     }

                     this.aw = 6000;
                     if (this.actionState == 106) {
                        this.positionX -= 60;
                     }
                     break;
                  case 110:
                     this.ax = 2;
                     this.requestAnimation(9);
                     break;
                  case 111:
                     this.aw = 400;
               }
         }

         this.actionState = var1;
      }
   }

   // $VF: renamed from: bL () void
   private void updateTrigger() {
      switch (this.animationId) {
         case 0:
            if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
               boolean var1 = false;
               if ((this.aw & 32) != 0) {
                  this.aw |= 15;
                  var1 = true;
               }

               if (!var1) {
                  b(this.aw, this.ax, true);
               } else {
                  a(this.aw, this.ax, true, true);
               }

               Game.removeActor(this);
               return;
            }
         case 1:
         case 2:
         case 3:
         case 6:
         case 7:
         case 9:
         default:
            break;
         case 4:
            if (Game.levelIndex == 3) {
               if (this.aw == 0) {
                  this.aw = 50;
               }

               if (this.aw != 1) {
                  this.aw--;
                  return;
               }
            }

            if (Game.player.animationId != 9 && rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
               Game.changeState(15);
               return;
            }
            break;
         case 5:
            cu = false;
            cF[0] = this.collisionBounds[0];
            cF[2] = this.collisionBounds[0] + 240;
            cF[1] = this.collisionBounds[1];
            cF[3] = this.collisionBounds[3];
            if (rectsOverlapInclusive(Game.player.collisionBounds, cF)) {
               worldScrollPixelsPerTick = 0;
               this.flags |= 16;
               if (!cu) {
                  cu = true;
               }

               cv = cv + this.aw;
               Game.cameraX = this.collisionBounds[0];
               Game.viewBounds[0] = Game.cameraX;
               Game.viewBounds[2] = Game.cameraX + 240;
               Game.cameraTargetX = Game.cameraX;
               if (Game.player.positionX < cF[0] + 10) {
                  Game.player.positionX = cF[0] + 10;
               }

               if (Game.player.positionX > Game.viewBounds[2] - 10 - 20) {
                  Game.player.positionX = Game.viewBounds[2] - 10 - 20;
                  return;
               }
            } else {
               cv = 0;
               this.flags &= -17;
            }
            break;
         case 8:
            if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
               this.o(this.aw, this.ax);
               return;
            }
            break;
         case 10:
            Game.changeState(12);
            return;
      }
   }

   private void o(int var1, int var2) {
      Game.player.facingLeft = this.facingLeft;
      if (this.facingLeft) {
         Game.player.flags |= 1;
      } else {
         Game.player.flags &= -2;
      }

      if (var2 != 0 && var2 != -1) {
         Game.ch = false;
      } else {
         Game.ch = true;
      }

      Game.startCutscene(Game.findScriptIndex(var1));
      Game.player.velocityY = 0;
      Game.player.velocityX = 0;
      if (activeBoss != null) {
         activeBoss.velocityY = 0;
         activeBoss.velocityX = 0;
      }

      if (this.type != 100 && this.type != 90) {
         Game.removeActor(this);
      }

      if (Game.bo != null) {
         e(Game.bo);
      }
   }

   // $VF: renamed from: bM () void
   private void updateGate37() {
      this.decrementTimerA();
      this.decrementTimerB();
      this.refreshBounds();
      this.N = false;
      this.O = false;
      this.P = false;
      switch (this.actionState) {
         case 0:
         default:
            break;
         case 100:
            this.d(0, this.attackBounds);
            this.N = true;
            this.O = true;
            this.P = true;
            if (this.aw <= 0 && this.ay != cz || (this.b(activeWeapon, this.collisionBounds) || this.b(activeWeapon, this.attackBounds)) && this.ax <= 0) {
               this.ax = 500;
               this.requestAnimation(0);
               if (playerPlatform == this) {
                  playerPlatform = null;
                  Game.player.requestAnimation(4);
               }

               if (this.ay == cz) {
                  this.actionState = 102;
                  this.ay = cy;
                  this.aw = 1000;
               } else if (this.di > this.dh && this.di < this.dg) {
                  this.actionState = 101;
               } else {
                  this.actionState = 102;
                  this.aw = 1000;
               }
            }
            break;
         case 101:
            byte var1 = 2;
            if (!this.dj && this.uid == -1) {
               var1 = 8;
            }

            if (this.type == 37) {
               this.positionY = this.positionY + (this.dj ? -var1 : var1);
            }

            this.di = this.di + (this.dj ? var1 : -var1);
            if (this.di >= this.dg || this.di <= this.dh) {
               this.actionState = 102;
               this.aw = 1000;
            }
            break;
         case 102:
            if (this.ay != cz && this.dg != this.dh && this.aw < 0 && this.uid != -1) {
               this.actionState = 101;
               if (this.di <= this.dh) {
                  this.dj = true;
               } else if (this.di >= this.dg) {
                  this.dj = false;
               }
            }
      }

      if ((this.actionState == 101 || this.actionState == 102) && this.b(1, this.collisionBounds) && this.ax <= 0 && this.animationId == 0 && this.type == 37) {
         this.ax = 500;
         this.actionState = 100;
         this.requestAnimation(1);
         this.aw = 10000;
      }
   }

   // $VF: renamed from: ah () void
   final void followPlayer() {
      this.facingLeft = Game.player.facingLeft;
      this.positionX = Game.player.positionX;
      this.positionY = Game.player.positionY;
   }

   // $VF: renamed from: bN () void
   private void updatePrompt() {
      if (this.collisionBounds != null) {
         if (this.ay == 1) {
            this.positionX = this.au + Game.cameraX;
            this.positionY = this.ar + Game.cameraY;
         } else if (this.ay == 2) {
            this.positionY = this.ar + Game.cameraY;
         } else if (this.ay != 3) {
            if (this.animationId != 36 && this.animationId != 37) {
               if (rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds) && this.aw != 2) {
                  this.aw = 1;
               }

               if (this.ax == 0
                  && this.aw == 1
                  && !rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)
                  && this.animationId != 17
                  && this.animationId != 18
                  && this.animationId != 34) {
                  Game.removeActor(this);
               }

               if ((this.animationId == 17 && cA == 0 || this.animationId == 18 && cA == 1)
                  && (
                     rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)
                        || rectContainsRect(this.collisionBounds, Game.player.collisionBounds)
                  )) {
                  this.aw = 2;
               }

               if (this.aw == 2 && !rectsOverlapInclusive(Game.player.collisionBounds, this.collisionBounds)) {
                  Game.removeActor(this);
               }

               if (this.animationId == 34) {
                  if (cB == 1) {
                     Game.removeActor(this);
                     return;
                  }

                  cB = 0;
               }
            } else {
               this.positionX = Game.player.positionX;
               this.positionY = Game.player.positionY + -80;
               if (weaponSwitchCooldownMs <= 50) {
                  Game.removeActor(this);
               }
            }
         }
      }
   }

   public static final void a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      if (var7) {
         Game.directGraphics.setARGBColor(cD[Game.stateFrame % 6]);
      } else {
         Game.directGraphics.setARGBColor(cD[0]);
      }

      Game.drawArc(var2 - var5, var3 - var5, var5 * 2, var5 * 2, 0, 360);
      Game.drawArc(var0 - var4, var1 - var4, var4 * 2, var4 * 2, 0, 360);
      int[] var8 = new int[2];
      int var9 = var2;
      int var10 = var3 - var5;
      int var11 = var2 - (var5 * Game.sin256(60) >> 8);
      int var12 = var3 + (var5 >> 1);
      int var13 = var2 + (var5 * Game.sin256(60) >> 8);
      int var14 = var3 + (var5 >> 1);
      rotatePoint(var2, var3, var9, var10, var6, var8);
      var9 = var8[0];
      var10 = var8[1];
      rotatePoint(var2, var3, var11, var12, var6, var8);
      var11 = var8[0];
      var12 = var8[1];
      rotatePoint(var2, var3, var13, var14, var6, var8);
      var13 = var8[0];
      var14 = var8[1];
      int var15 = var2;
      int var16 = var3 + var5;
      int var17 = var2 - (var5 * Game.sin256(60) >> 8);
      int var18 = var3 - (var5 >> 1);
      int var19 = var2 + (var5 * Game.sin256(60) >> 8);
      int var20 = var3 - (var5 >> 1);
      rotatePoint(var2, var3, var15, var16, var6, var8);
      var15 = var8[0];
      var16 = var8[1];
      rotatePoint(var2, var3, var17, var18, var6, var8);
      var17 = var8[0];
      var18 = var8[1];
      rotatePoint(var2, var3, var19, var20, var6, var8);
      var19 = var8[0];
      var20 = var8[1];
      Game.drawLine(var9, var10, var11, var12);
      Game.drawLine(var9, var10, var13, var14);
      Game.drawLine(var11, var12, var13, var14);
      Game.drawLine(var15, var16, var17, var18);
      Game.drawLine(var15, var16, var19, var20);
      Game.drawLine(var17, var18, var19, var20);
   }

   static {
      int[] var10000 = new int[]{102, 136, 170};
      var10000 = new int[]{119, 153, 170};
      var10000 = new int[]{221, 238, 255};
      var10000 = new int[]{25, 30, 40, 50};
      var10000 = new int[]{2, 3, 0, 0};
      var10000 = new int[]{0, 100, 0, 0};
      var10000 = new int[]{-5, 10, 15, 0};
      var10000 = new int[]{-5, 240, 15, 0};
      var10000 = new int[]{0, 100, 0, 0};
      var10000 = new int[]{0, 100, 0, 0};
      var10000 = new int[]{16714752, 16736768, 16755968, 16777215, 16755968, 16736768, 16714752};
      var10000 = new int[]{1, 2, 3, 5};
   }
}
