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

// $VF: renamed from: e
final class Game extends FullCanvas implements Runnable {
   // $VF: renamed from: a d
   static BSprite font;
   private static BSprite dl;
   private static BSprite dm;
   // $VF: renamed from: b d[]
   public static BSprite[] sprites;
   // $VF: renamed from: dn javax.microedition.lcdui.Image
   private static Image titleImage;
   // $VF: renamed from: do javax.microedition.lcdui.Image
   private static Image menuNewBadge;
   // $VF: renamed from: c java.util.Random
   public static Random random;
   // $VF: renamed from: d javax.microedition.lcdui.Graphics
   public static Graphics graphics;
   // $VF: renamed from: e com.nokia.mid.ui.DirectGraphics
   public static DirectGraphics directGraphics;
   // $VF: renamed from: f boolean
   public static boolean paintComplete;
   // $VF: renamed from: g int
   static int tickDeltaMs;
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
   // $VF: renamed from: p int
   public static int health = 60;
   // $VF: renamed from: q int
   public static int maxHealth = 60;
   public static int r = 0;
   // $VF: renamed from: s int
   public static int magic = 60;
   // $VF: renamed from: t int
   public static int maxMagic = 60;
   public static int u = 0;
   public static int v = 0;
   public static long w = 0L;
   private static int dp;
   private static int dq;
   private static boolean dr;
   // $VF: renamed from: x int
   public static int state;
   // $VF: renamed from: y int
   public static int previousState;
   // $VF: renamed from: z int
   public static int stateFrame;
   // $VF: renamed from: A long
   public static long frameElapsedMs;
   // $VF: renamed from: B long
   public static long frameStartMs;
   // $VF: renamed from: C boolean
   public static boolean bloodCensored = false;
   // $VF: renamed from: D boolean
   public static boolean suspended;
   static int E = -1;
   static int F;
   static int G;
   public static int H = 0;
   static final int[] I = new int[]{-1996488960, -2013224961, -1996554240};
   static final int[] J = new int[]{1157627648, 1149292543, 1157562368};
   public static int K = 0;
   // $VF: renamed from: L int
   public static int cameraX;
   // $VF: renamed from: M int
   public static int cameraY;
   // $VF: renamed from: N int
   public static int cameraTargetX;
   // $VF: renamed from: O int
   public static int cameraTargetY;
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
   // $VF: renamed from: aa int[]
   public static int[] viewBounds = new int[4];
   public static boolean ab = false;
   public static boolean ac;
   public static boolean ad;
   public static boolean ae;
   public static boolean af;
   public static boolean ag;
   public static boolean ah;
   public static boolean ai;
   // $VF: renamed from: aj boolean
   public static boolean cameraControllerActive;
   // $VF: renamed from: ak int
   public static int cameraControllerMode;
   public static boolean al;
   // $VF: renamed from: am int
   public static int activeCameraControllerUid = -1;
   // $VF: renamed from: an int
   public static int hazardCameraOffsetY = -1;
   // $VF: renamed from: ao boolean
   public static boolean cameraScrollHorizontal = true;
   public static int ap;
   public static int aq = -1;
   public static int ar = -1;
   public static int as;
   public static final int[] at = new int[]{4, 12, 12, 1, 4, 4};
   public static Actor au;
   public static Actor av;
   public static int aw = 0;
   public static int ax = 0;
   public static int ay = 0;
   static int[] az = new int[500];
   static int aA = -1;
   // $VF: renamed from: aB int
   static int shakeYTicks;
   // $VF: renamed from: aC int
   static int shakeXTicks;
   // $VF: renamed from: ds boolean
   private static boolean confirmationOpen;
   // $VF: renamed from: dt javax.microedition.lcdui.Image
   private static Image logoImage;
   // $VF: renamed from: du d
   private static BSprite titleSprite;
   static boolean aD;
   // $VF: renamed from: aE int
   public static int confirmationKind;
   // $VF: renamed from: aF int[]
   static final int[] levelModes = new int[]{0, 0, 0, 3, 0, 2, 0, 0, 0};
   // $VF: renamed from: aG int
   public static int levelIndex;
   // $VF: renamed from: aH int
   public static int maxUnlockedLevel = 0;
   // $VF: renamed from: aI boolean
   public static boolean levelCompleted;
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
   public static Actor bd;
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
   // $VF: renamed from: bk b
   public static Actor player;
   public static Actor bl;
   public static Actor bm;
   public static Actor bn;
   public static Actor bo;
   public static int bp = 250;
   public static int bq = 50;
   public static int br = 70;
   // $VF: renamed from: bs b[]
   public static Actor[] actors = new Actor[bp];
   // $VF: renamed from: bt int
   public static int actorCount;
   // $VF: renamed from: bu b[]
   public static Actor[] paintActors = new Actor[bq];
   // $VF: renamed from: bv int
   public static int paintActorCount;
   // $VF: renamed from: bw b[]
   public static Actor[] updateActors = new Actor[br];
   // $VF: renamed from: bx int
   public static int updateActorCount;
   public static int[] by = new int[bp];
   public static int bz = 0;
   public static int bA = 20;
   public static Actor[] bB = new Actor[bA];
   public static int bC;
   public static byte[] bD = new byte[bp];
   static short[] bE = new short[20];
   public static byte[] bF;
   // $VF: renamed from: dI java.lang.String[]
   private static String[] levelPackPaths = new String[]{"/3", "/4", "/5", "/6", "/7", "/8", "/9", "/10", "/11", "/12", "/13", "/14"};
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
   // $VF: renamed from: dK boolean
   private static boolean continueFromCheckpoint;
   // $VF: renamed from: bP boolean
   public static boolean skipCheckpointPosition;
   // $VF: renamed from: bQ int
   public static int mapWidthTiles;
   // $VF: renamed from: bR int
   public static int mapHeightTiles;
   // $VF: renamed from: bS int
   public static int levelPixelWidth;
   // $VF: renamed from: bT int
   public static int levelPixelHeight;
   // $VF: renamed from: dL byte[]
   private static byte[] collisionTiles;
   // $VF: renamed from: dM byte[]
   private static byte[] tileLayer0;
   // $VF: renamed from: dN byte[]
   private static byte[] tileLayer0Transforms;
   // $VF: renamed from: dO byte[]
   private static byte[] tileLayer1;
   // $VF: renamed from: dP byte[]
   private static byte[] tileLayer1Transforms;
   public static int bU;
   public static int bV;
   public static int bW;
   // $VF: renamed from: bX int[]
   static int[] trigTable256;
   // $VF: renamed from: bY int[][]
   public static int[][] actorStatTables;
   // $VF: renamed from: dQ java.lang.String[][]
   private static String[][] stringTables = new String[11][];
   public static final int[] bZ = new int[]{31, 61, 64, 62};
   // $VF: renamed from: dR java.lang.String
   private static String packPath;
   // $VF: renamed from: dS java.io.InputStream
   private static InputStream packStream;
   // $VF: renamed from: dT int
   private static int packPosition;
   // $VF: renamed from: dU short
   private static short packResourceCount;
   // $VF: renamed from: dV int[]
   private static int[] packOffsets;
   // $VF: renamed from: dW short
   private static short packPartCount;
   // $VF: renamed from: dX short[]
   private static short[] packPartFirstResource;
   // $VF: renamed from: dY int
   private static int packPartIndex;
   // $VF: renamed from: dZ byte[]
   private static byte[] packSkipBuffer;
   // $VF: renamed from: ea int
   private static int menuDefinition = 0;
   // $VF: renamed from: ca int
   public static int selectedMenuItem;
   // $VF: renamed from: eb byte[][]
   private static byte[][] menuItems = new byte[][]{{1, 4, 5, 6, 7, 8, 9}, {12, 13, 6, 7, 0, 9}, {1, 2, 3, 4, 5, 6, 7, 8, 9}};
   // $VF: renamed from: cb byte[][][]
   public static byte[][][] scriptTracks;
   // $VF: renamed from: cc short[]
   public static short[] scriptIds;
   // $VF: renamed from: cd short[]
   public static short[] scriptTicks;
   // $VF: renamed from: ce int[][]
   public static int[][] scriptTrackOffsets;
   // $VF: renamed from: cf boolean
   public static boolean scriptsPaused;
   public static boolean cg;
   public static boolean ch = true;
   public static boolean ci = false;
   public static int cj;
   public static int[] ck = new int[3];
   public static byte[] cl = new byte[3];
   public static boolean cm = true;
   // $VF: renamed from: cn int[]
   static final int[] scriptOpcodeSizes = new int[]{6, 2, 3, 0, 2, 1, 1, 4, 4, 0, 0, 8, 1, 0, 4, 0, 4, 2, 0, 0, 1, 0};
   static int co = 0;
   static boolean cp = false;
   static boolean cq = false;
   static int cr = 0;
   static int cs = 0;
   static final int[] ct = new int[]{25, 22, 28, 31, 19};
   static boolean cu;
   // $VF: renamed from: cv int
   static final int saveBufferSize = 999 + bp + 64;
   // $VF: renamed from: cw byte[]
   static byte[] saveBuffer = new byte[saveBufferSize];
   // $VF: renamed from: cx boolean
   public static boolean saveLoaded = false;
   public static int cy;
   // $VF: renamed from: cz int
   static int pressedMask;
   // $VF: renamed from: cA int
   static int pendingPressMask;
   // $VF: renamed from: cB int
   static int heldMask;
   // $VF: renamed from: cC int
   static int frameHeldMask;
   // $VF: renamed from: cD int
   static int heldFrameCount;
   // $VF: renamed from: cE int
   static int releasedMask;
   // $VF: renamed from: cF int
   static int pendingReleaseMask;
   // $VF: renamed from: cG int
   static int framesSincePress;
   // $VF: renamed from: cH int
   static int cheatProgress = 0;
   // $VF: renamed from: cI boolean
   static boolean cheatsUnlocked = false;
   // $VF: renamed from: cJ int[]
   static final int[] cheatSequence = new int[]{2, 1, 1, 32};
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
   // $VF: renamed from: ej int
   private static int scrollingTextY;
   // $VF: renamed from: ek javax.microedition.media.Player
   private static Player soundPlayer;
   // $VF: renamed from: dk boolean
   public static boolean soundEnabled = true;
   // $VF: renamed from: el int
   private static int rememberedMusicId;
   // $VF: renamed from: em int
   private static int resumeSoundId = -1;
   // $VF: renamed from: en int
   private static int soundResumeFrames;
   // $VF: renamed from: eo int
   private static int wavIdThreshold = 20;
   // $VF: renamed from: ep byte[]
   private static byte[] soundData;

   Game() {
      int[] var10000 = new int[]{0, 1};
      suspended = false;
      random = new Random(System.currentTimeMillis());
      state = 0;
      previousState = -1;
      stateFrame = 0;
   }

   public final void paint(Graphics var1) {
      try {
         graphics = var1;
         directGraphics = DirectUtils.getDirectGraphics(graphics);
         stateFrame++;
         tickDeltaMs = 83;
         switch (state) {
            case 0:
               initialize();
               break;
            case 1:
               updateLogoLoading();
               break;
            case 2:
               this.updateMainMenu();
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
               updateHelp();
               break;
            case 6:
            case 20:
               updateScrollingText();
               break;
            case 7:
               updatePromotion();
               break;
            case 8:
               Actor.graphics = graphics;
               updateGameplay();
               break;
            case 9:
               updateLevelLoading();
               break;
            case 12:
               updateGameOver();
               break;
            case 14:
               updatePauseMenu();
               break;
            case 15:
               updateLetterbox();
               if (aM == aN) {
                  this.updateLevelComplete();
               }
               break;
            case 18:
               updateTitle();
               break;
            case 19:
               updateLevelSelection();
               break;
            case 23:
               updateSoundPrompt();
               break;
            case 27:
               updatePromotionPrompt();
               break;
            case 30:
               startNewGame();
               break;
            case 32:
               updateCrystalReward(dq);
         }

         graphics = null;
         latchInput();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      paintComplete = true;
   }

   // $VF: renamed from: H () void
   private static void updateHudSlide() {
      if (player.animationId != 8 && (player.animationId < 13 || player.animationId > 25) && !aR) {
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
         n = n + tickDeltaMs;
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

   // $VF: renamed from: a (int, int, int, int, int, int) void
   public static final void drawSegmentedBar(int var0, int var1, int var2, int var3, int var4, int var5) {
      setColor(var0);
      fillRect(var2, var3, var5 * 61 / 60, var4);
      setColor(var1);
      fillRect(var2 + 1, var3 + 1, var5 * 61 / 60 - 2, var4 - 2);
      setColor(var0);
      if (var5 > 0) {
         for (int var6 = 0; var6 <= (var5 * 61 / 60 - 2) / o; var6++) {
            drawLine(var2 + var6 * o, var3, var2 + var6 * o, var3 + var4 - 1);
         }
      }
   }

   // $VF: renamed from: I () void
   private static void drawHud() {
      k = false;
      sprites[35].drawFrame(graphics, 0, 0, m, 0, 0, 0);
      setColor(15773760);
      drawRect(56, 8 + m, maxHealth * 61 / 60 + 14, 8);
      setColor(0);
      fillRect(70, 9 + m, maxHealth * 61 / 60, 7);
      drawSegmentedBar(1611800, 63488, 70, 10 + m, 5, health);
      setColor(8402944);
      drawRect(70, 17 + m, maxHealth * 61 / 60, 1);
      setColor(15773760);
      drawRect(56, 18 + m, maxMagic * 61 / 60 + 14, 8);
      setColor(0);
      fillRect(70, 19 + m, maxMagic * 61 / 60, 7);
      setColor(2132128);
      fillRect(70, 20 + m, magic * 61 / 60, 5);
      setColor(3725552);
      fillRect(71, 21 + m, magic * 61 / 60 - 2, 3);
      drawSegmentedBar(2132128, 3725552, 70, 20 + m, 5, magic);
      sprites[35].drawFrame(graphics, 2 + Actor.activeWeapon, -1, m, 0, 0, 0);
   }

   public static final void a() {
      if (Actor.bv != 2 && cm && aN < 5) {
         sprites[35].drawFrame(graphics, 5, 10, 307, 0, 0, 0);
      }
   }

   // $VF: renamed from: G (int) void
   private static void updateCrystalReward(int var0) {
      drawWorld();
      k = true;
      directGraphics.setARGBColor(-587202560);
      setClip(9, 30, 222, 252);
      fillRect(9, 30, 222, 252);
      setColor(15692292);
      drawRect(9, 30, 221, 251);
      int var1 = var0 == 0 ? r : u;
      if (dr) {
         var1 = 4;
      }

      dp++;
      if (!dr && dp == 5) {
         if (var0 == 0) {
            if (var1 == 4) {
               r = 0;
               maxHealth += 10;
               if (maxHealth > 120) {
                  maxHealth = 120;
               }

               health = maxHealth;
               dr = true;
               k = true;
            } else {
               health += 10;
               if (health > maxHealth) {
                  health = maxHealth;
               }
            }
         } else if (var0 == 1) {
            if (var1 == 4) {
               u = 0;
               maxMagic += 10;
               if (maxMagic > 120) {
                  maxMagic = 120;
               }

               magic = maxMagic;
               dr = true;
               k = true;
            } else {
               if (Actor.bu) {
                  magic += 10;
               }

               if (magic > maxMagic) {
                  magic = maxMagic;
               }
            }
         }
      }

      for (int var2 = -1; var2 <= 1; var2++) {
         directGraphics.setARGBColor(h[(var2 == 0 ? 0 : 1) + var0 * 2]);
         drawArc(55 + var2, 55 + var2, 65 - var2 << 1, 65 - var2 << 1, 0, 360);
         drawRect(74 + var2, 74 + var2, 46 - var2 << 1, 46 - var2 << 1);
         drawArc(74 + var2, 74 + var2, 46 - var2 << 1, 46 - var2 << 1, 0, 360);

         for (int var3 = 0; var3 < 4; var3++) {
            drawLine(i[var3 * 2 + (var2 + 1) * 10], i[var3 * 2 + 1 + (var2 + 1) * 10], i[var3 * 2 + 2 + (var2 + 1) * 10], i[var3 * 2 + 3 + (var2 + 1) * 10]);
            sprites[35].drawFrame(graphics, 17, j[var3 * 2], j[var3 * 2 + 1], 0, 0, 0);
            if (var3 < var1) {
               sprites[33].currentPalette = 0;
               sprites[33].drawAnimationFrame(graphics, var0 == 0 ? 1 : 3, 0, j[var3 * 2], j[var3 * 2 + 1], 0, 0, 0);
            }
         }
      }

      font.currentPalette = 0;
      String var4;
      if (var1 < 4) {
         var4 = getString(0, 56 + var0).replace('#', (char)(52 - var1));
      } else {
         var4 = getString(0, 58 + var0);
      }

      font.drawWrappedText(graphics, var4, 120, 195, 17, 200, 0, -1);
      if (dp > 10) {
         if (stateFrame % 10 < 5) {
            font.drawWrappedText(graphics, getString(0, 10), 120, 262, 17, 230, 0, -1);
         }

         if (hasKeyPress()) {
            state = 8;
            k = true;
            dr = false;
            cm = true;
         }
      }
   }

   protected final void hideNotify() {
      if (rememberedMusicId >= 0 && soundPlayer != null && soundPlayer.getState() == 400) {
         resumeSoundId = rememberedMusicId;
      }

      stopSound();
      suspended = true;
   }

   protected final void showNotify() {
      k = true;
      suspended = false;
      if (state != 0 && state != 1 && state != 9 && state != 15) {
         stateFrame = 0;
      }

      if (state == 8 && !aS && !hasActiveBackgroundScript()) {
         selectedMenuItem = 0;
         changeState(14);
      }

      if (resumeSoundId >= 0 && state != 14) {
         soundResumeFrames = 3;
      }

      clearInput();
   }

   // $VF: renamed from: J () void
   private void updateLevelComplete() {
      k = true;
      drawWorld();
      g(0, 0, 240, 320);
      drawSoftkeys("", getString(0, 17));
      setClip(0, 0, 240, 320);
      font.drawText(graphics, getString(0, 85), 120, 160, 17);
      if (wasKeyPressed(327712)) {
         aN = 0;
         aM = 0;
         finishLevel();
      }
   }

   public static final void a(boolean var0) {
      dh = levelIndex + (var0 ? 1 : 0);
      if (levelIndex + (var0 ? 1 : 0) == 9) {
         dh = 0;
         df = 9;
      } else {
         while (dg[df - 9 + 1] <= dh) {
            df++;
         }
      }
   }

   // $VF: renamed from: a (int) void
   public static final void changeState(int var0) {
      if (var0 == 9) {
         stopSound();
         dv = levelIndex == 0 ? 0 : randomRange(0, 7);
         soundResumeFrames = 0;
      } else if (var0 == 12) {
         stopSound();
         playSoundOnce(-1);
      } else if (var0 == 15) {
         stopSound();
         playSoundOnce(-1);
         aN = 1;
         aM = 160;
      } else if (var0 == 8 && state == 9) {
         soundResumeFrames = 0;
         playLevelMusic();
      }

      if (var0 == 20) {
         dh = 0;
         dc = 0;
         scrollingTextY = 240;
         df = 10;
         H = 12;
      }

      if (var0 == 19 || var0 == 20) {
         menuDefinition = 0;
         dd = 2;
         de = 0;
         H = 12;
         ensureTileBuffer();
      }

      if (var0 == 6) {
         scrollingTextY = 266;
         String var1 = getString(0, 28);
         String var2;
         if ((var2 = GloftSOFD.instance.getAppProperty("MIDlet-Version")) == null) {
            var2 = "0.0.0";
         }

         int var3;
         if ((var3 = var1.indexOf("X.X.X")) != -1) {
            stringTables[0][28] = var1.substring(0, var3) + " " + var2 + var1.substring(var3 + 5);
         }

         dd = 0;
      }

      if (var0 == 2) {
         selectedMenuItem = 0;
         if (readI16LE(saveBuffer, 16) == 0) {
            menuDefinition = 0;
         } else {
            menuDefinition = 2;
            selectedMenuItem = 1;
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
               stopSound();
            case 2:
            case 3:
            case 5:
            case 6:
            case 7:
            case 18:
            case 19:
            case 20:
            case 30:
               loadSoundData(-1);
               playSoundOnce(0);
         }
      }

      if (state == 2 && var0 != 2) {
         menuNewBadge = null;
      } else if (var0 == 2 && state != 2) {
         try {
            if (menuNewBadge == null) {
               menuNewBadge = Image.createImage("/new.png");
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      if (var0 == 14) {
         menuDefinition = 1;
      }

      if (var0 == 14 || var0 == 7) {
         stopSound();
      }

      if (var0 == 9) {
         titleImage = null;
      }

      aJ = false;
      if (var0 == 13 || var0 == 12 || var0 == 14 || var0 == 31 || var0 == 15 || var0 == 32) {
         aJ = true;
      }

      if (var0 == 8 && (state == 14 || state == 12)) {
         playLevelMusic();
      }

      stateFrame = 0;
      previousState = state;
      state = var0;
      clearInput();
   }

   public final void run() {
      while (state != 11) {
         frameStartMs = System.currentTimeMillis();
         if (!suspended) {
            if ((state != 14 && state != 8 && state != 9 || state == 8 && aS) && (state != 5 || previousState != 14) && soundResumeFrames > 0) {
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

         frameStartMs = Math.min(frameStartMs, System.currentTimeMillis());
         frameElapsedMs = System.currentTimeMillis() - frameStartMs;
         boolean var1 = hasActiveBackgroundScript();

         try {
            Thread.sleep(Math.max(1L, (var1 ? 83 : 83) - frameElapsedMs));
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      GloftSOFD.instance.destroyApp(true);
   }

   // $VF: renamed from: b () void
   public final void resumeLoop() {
      if (suspended) {
         suspended = false;
      }
   }

   // $VF: renamed from: c () void
   public final void suspendLoop() {
      suspended = true;
   }

   // $VF: renamed from: i (int, int, int, int) void
   private static void setClipInternal(int var0, int var1, int var2, int var3) {
      graphics.setClip(var0, var1, var2, var3);
   }

   // $VF: renamed from: a (int, int, int, int) void
   public static final void setClip(int var0, int var1, int var2, int var3) {
      graphics.setClip(var0, var1, var2, var3);
   }

   // $VF: renamed from: b (int) void
   public static final void setColor(int var0) {
      graphics.setColor(var0);
   }

   // $VF: renamed from: b (int, int, int, int) void
   public static final void fillRect(int var0, int var1, int var2, int var3) {
      graphics.fillRect(var0, var1, var2, var3);
   }

   // $VF: renamed from: c (int, int, int, int) void
   public static final void drawRect(int var0, int var1, int var2, int var3) {
      graphics.drawRect(var0, var1, var2, var3);
   }

   // $VF: renamed from: d (int, int, int, int) void
   public static final void drawLine(int var0, int var1, int var2, int var3) {
      graphics.drawLine(var0, var1, var2, var3);
   }

   // $VF: renamed from: a (javax.microedition.lcdui.Image, int, int, int) void
   public static final void drawImage(Image var0, int var1, int var2, int var3) {
      graphics.drawImage(var0, var1, var2, var3);
   }

   // $VF: renamed from: b (int, int, int, int, int, int) void
   public static final void fillArc(int var0, int var1, int var2, int var3, int var4, int var5) {
      graphics.fillArc(var0, var1, var2, var3, var4, var5);
   }

   // $VF: renamed from: c (int, int, int, int, int, int) void
   public static final void fillTriangle(int var0, int var1, int var2, int var3, int var4, int var5) {
      graphics.fillTriangle(var0, var1, var2, var3, var4, var5);
   }

   // $VF: renamed from: d (int, int, int, int, int, int) void
   public static final void drawArc(int var0, int var1, int var2, int var3, int var4, int var5) {
      graphics.drawArc(var0, var1, var2, var3, var4, var5);
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
      int var3 = F - cameraX + 0;
      int var4 = G - cameraY + 0;
      if (var1 || !aS) {
         E++;
      }

      if (E <= 5) {
         int var12 = (var4 << 8) / 5 * E >> 8;
         directGraphics.setARGBColor(I[var0]);
         fillRect(var3 - 15, var4 - var12, 30, var12);
         directGraphics.setARGBColor(J[var0]);
         fillRect(var3 - 15 - 16, var4 - var12, 16, var12);
         fillRect(var3 + 15, var4 - var12, 16, var12);
      } else if (E <= 19 + var2) {
         int var9;
         if ((var9 = (E - 5 - var2 - 1) % 8) > 3) {
            var9 = 7 - var9;
         }

         int var11 = 3840 * var9 / 14 >> 8;
         int var14 = 2048 * var9 / 14 >> 8;
         directGraphics.setARGBColor(I[var0]);
         fillRect(var3 - 15 + var11, 0, (15 - var11) * 2, var4);
         directGraphics.setARGBColor(J[var0]);
         fillRect(var3 - 15 - (8 - var14) * 2 + var11, 0, (8 - var14) * 2, var4);
         fillRect(var3 + 15 - var11, 0, (8 - var14) * 2, var4);
      } else if (E <= 33 + var2 * 2) {
         int var8 = E - 19 - 1 - var2 * 2;
         int var10 = 3840 * var8 / 14 >> 8;
         int var13 = 2048 * var8 / 14 >> 8;
         directGraphics.setARGBColor(I[var0]);
         fillRect(var3 - 15 + var10, 0, (15 - var10) * 2, var4);
         directGraphics.setARGBColor(J[var0]);
         fillRect(var3 - 15 - (8 - var13) * 2 + var10, 0, (8 - var13) * 2, var4);
         fillRect(var3 + 15 - var10, 0, (8 - var13) * 2, var4);
      } else if (E <= 38 + var2 * 2) {
         int var5 = E - 33 - var2 * 2;
         int var6 = (var4 << 8) / 5;
         int var7 = var5 * var6 >> 8;
         directGraphics.setARGBColor(I[var0]);
         fillRect(var3 - 1, 0, 2, var4 - var7);
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
      setColor(0);
      fillRect(var0, var1, var2, var3);
      setColor(15244571);
      drawRect(var0, var1, var2 - 1, var3 - 1);
      setColor(0);
      drawRect(var0 + 1, var1 + 1, var2 - 2 - 1, var3 - 2 - 1);
      setColor(10374921);
      drawRect(var0 + 2, var1 + 2, var2 - 4 - 1, var3 - 4 - 1);
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
         font.currentPalette = 0;
         font.drawWrappedText(graphics, aU, 15, 7, 20, 210, ba, 3);
         sprites[35].drawAnimationFrame(graphics, 4, stateFrame % 6 >> 1, 228, 63, 0, 0, 0);
         if (wasKeyPressed(327712) || wasKeyPressed(33024)) {
            if (bb > 3) {
               bb -= 3;
               ba += 3;
            } else {
               aU = null;
               clearInput();
            }
         }
      }

      drawSoftkeys("", getString(0, 17));
   }

   public static final void e() {
      int var0 = K;
      setClipInternal(0, 0, 240, 320);
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
            font.drawText(graphics, aV, var7 ? 50 : 5, 7, 20);
            font.currentPalette = 0;
            font.drawWrappedText(graphics, aU, var7 ? 50 : 5, 23, 20, var7 ? 175 : 210, ba, 3);
         } else {
            if (var7) {
               sprites[-1].drawFrame(graphics, aY, 237, 4, 1, 0, 0);
            }

            font.drawText(graphics, aV, 5, 7, 20);
            font.currentPalette = 0;
            font.drawWrappedText(graphics, aU, 5, 23, 20, var7 ? 175 : 210, ba, 3);
         }

         sprites[35].drawAnimationFrame(graphics, 4, stateFrame % 6 >> 1, aZ == 0 ? 230 : 190, 63, 0, 0, 0);
      }

      K++;
      if (state != 14 && (wasKeyPressed(327712) || wasKeyPressed(33024))) {
         if (bb > 3) {
            bb -= 3;
            ba += 3;
            return;
         }

         aU = null;
         l();
         clearInput();
      }
   }

   public static final void f(int var0, int var1, int var2, int var3) {
      if (H >= 0) {
         if (H <= 240) {
            H += var3;
            int var15 = 240 - H;
            int var6 = 60928 - 54 * H << 16 & 0xFF000000;
            directGraphics.setARGBColor(var6);
            int var7 = var1 - var15;
            int var8 = var2 - var15;
            int var9 = 0 + 2 * var15;
            int var10 = 0 + 2 * var15;
            if (var0 >= 0 && var0 <= 2) {
               fillArc(var7, var8, var9, var10, 0, 360);
            }

            int var11 = var15 >> 2;
            if (var0 != 4 && var0 != 1) {
               directGraphics.setARGBColor(-9150);
            } else {
               directGraphics.setARGBColor(-10895105);
            }

            setClip(0, 0, 240, 320);

            for (int var14 = 0; var14 < 20; var14++) {
               int var12 = randomRange(0, var11 >> 1);
               int var13 = var11 + var12;
               fillArc(var1 - var13, var2 - var13, var13 * 2, var13 * 2, 360 * var14 / 20 + 9, 2);
            }

            setClip(0, 0, 240, 320);
         } else {
            if (var0 == 3) {
               if (Actor.spendMagic(Actor.bR[Actor.bO[Actor.activeWeapon]])) {
                  Actor.cA = 0;
                  Actor.cC = 2;
                  player.setAnimationImmediate(16);
                  Actor.aN = Actor.bQ[Actor.bO[0]];
                  Actor.aM = new Actor[Actor.aN];

                  for (int var4 = 0; var4 < Actor.aN; var4++) {
                     Actor.aM[var4] = player.spawnActor(39, 1, 16, 0, -22, 302, false);
                     Actor.aM[var4].flags |= 4096;
                     Actor.aM[var4].ax = var4;
                     Actor.aM[var4].aw = 600;
                     Actor.aM[var4].au = 0;
                     Actor.aM[var4].as = Actor.bO[Actor.activeWeapon];
                     Actor.aM[var4].at = 0;
                     Actor.aM[var4].W();
                     Actor.aM[var4].flags |= 16;
                  }
               }

               if (bc != 14) {
                  l();
               }
            } else if (var0 == 4) {
               Actor.cA = 1;
               Actor.cC = 2;
               if (Actor.spendMagic(Actor.bR[Actor.bO[Actor.activeWeapon]])) {
                  player.setAnimationImmediate(22);
                  if (Actor.aR == null) {
                     Actor.aR = new Actor[2];
                  }

                  if (Actor.aR[0] != null) {
                     Actor.aR[1] = Actor.aR[0];
                     if (Actor.aR[1].animationId == 8 || Actor.aR[1].animationId == 9 || Actor.aR[1].animationId == 10) {
                        Actor.aR[1].requestAnimation(10);
                     }

                     Actor.aR[1].aw = 1000;
                  }

                  Actor.aR[0] = player.spawnActor(39, 1, 7, player.facingLeft ? 10 : -10, -50, 302, false);
                  Actor.aR[0].flags |= 16;
                  Actor.aR[0].velocityX = Actor.aR[0].facingLeft ? -2560 : 2560;
                  Actor.aR[0].W();
               }

               l();
            }

            H = -1;
         }
      }
   }

   public static final void a(int var0, int var1, int var2) {
      int var3 = 100 * (450 - Actor.weaponSwitchCooldownMs) / 450;
      int var4 = 0;
      if (var0 == 1) {
         var4 = 5882111;
      } else {
         var4 = 16768066;
      }

      if (Actor.weaponSwitchCooldownMs > 225) {
         var4 |= 255 * (450 - Actor.weaponSwitchCooldownMs) / 225 << 24;
      } else {
         var4 |= 255 * (Actor.weaponSwitchCooldownMs - 225) / 225 << 24;
      }

      directGraphics.setARGBColor(var4);

      for (int var7 = 0; var7 < 20; var7++) {
         int var5 = randomRange(0, var3 >> 1);
         int var6 = var3 + var5;
         fillArc(var1 - var6, var2 - var6, var6 * 2, var6 * 2, 360 * var7 / 20 + 9, 2);
      }
   }

   // $VF: renamed from: K () void
   private static void initializeCamera() {
      if (activeCameraControllerUid == -1 || levelModes[levelIndex] != 2) {
         cameraY = 0;
         cameraX = 0;
         cameraTargetY = 0;
         cameraTargetX = 0;
         Q = 0;
         P = 0;
         Z = false;
         V = 0;
         updateCamera(1);
      }
   }

   // $VF: renamed from: d (int) void
   public static final void updateCamera(int var0) {
      boolean var1 = false;
      if (hasActiveBackgroundScript()) {
         var1 = true;
      }

      if (player != null) {
         ac = false;
         ad = false;
         ae = false;
         ag = false;
         af = false;
         if (activeCameraControllerUid == -1 && !var1 && au != null) {
            if (cameraX + 240 > au.collisionBounds[2]) {
               ae = true;
               ac = true;
            }

            if (cameraX < au.collisionBounds[0]) {
               ad = true;
               ac = true;
            }

            if (cameraY + 320 > au.collisionBounds[3]) {
               ag = true;
               ac = true;
            }

            if (cameraY < au.collisionBounds[1]) {
               af = true;
               ac = true;
            }
         }

         if (ac) {
            if (!ai) {
               if (shakeYTicks == 0) {
                  cameraTargetY = player.positionY - 220;
               }
            } else {
               cameraTargetY = player.positionY - 220;
               if (!af && !ag) {
                  if (player.velocityX >= 0 && (player.velocityX != 0 || !player.facingLeft)) {
                     cameraTargetX = R;
                  } else {
                     cameraTargetX = S - 240;
                  }
               } else if (af) {
                  cameraTargetY = T;
               } else {
                  cameraTargetY = U - 320;
               }

               ah = true;
            }

            ap = cameraTargetY + 100;
            if (ad || ae) {
               cameraX = cameraTargetX;
            }

            if (af || ag) {
               cameraY = cameraTargetY;
            }

            if (shakeYTicks <= 0 && shakeXTicks <= 0) {
               applyCameraConstraints();
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

            if (activeCameraControllerUid != -1) {
               if (cameraControllerMode == 0 || cameraControllerMode == 3 || cameraControllerMode >= 4 && cameraScrollHorizontal) {
                  var3 = 1;
               }

               if (levelIndex != 3) {
                  var4 = true;
               }
            }

            if (levelIndex == 9) {
               if (activeCameraControllerUid != -1 && cameraControllerMode >= 4 && cameraScrollHorizontal) {
                  var3 = 1;
                  var4 = false;
               }

               if (activeCameraControllerUid != -1 && cameraControllerMode >= 4 && (!cameraScrollHorizontal || al)) {
                  var3 = 1;
                  var4 = true;
                  al = false;
               }
            }

            if (var3 == 0) {
               cameraTargetX = player.facingLeft ? player.positionX - 160 : player.positionX - 80;
               Y = X;
               X = 0;
               if (!player.grounded && Actor.playerPlatform == null) {
                  if (Math.abs(cameraTargetX - cameraX) <= 0) {
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
               player.refreshBounds();
               if (player.collisionBounds[1] <= ap) {
                  aq = 0;
               }

               if (player.positionY >= ap + 120) {
                  aq = 1;
               }

               if ((player.velocityY < 0 || player.c(true) || Actor.playerPlatform != null || player.bU) && aq == 0
                  || (player.velocityY > 0 || player.c(false) || Actor.playerPlatform != null || player.bU) && aq == 1) {
                  if (aq == 0) {
                     ap = player.collisionBounds[1];
                  } else {
                     ap = player.collisionBounds[3] - 120;
                  }

                  cameraTargetY = ap - 100;
               }
            }

            V = 20;
            if ((var0 & 1) == 0 || activeCameraControllerUid != -1 && levelModes[levelIndex] == 2) {
               if (Actor.W && W != 1 && activeCameraControllerUid == -1) {
                  if (Actor.X == 1) {
                     if (Math.abs((var3 = player.facingLeft ? player.positionX - 160 : player.positionX - 80) - cameraX) < 40) {
                        if (cameraTargetX != var3) {
                           cameraTargetX = var3;
                           Actor.Y = 0;
                        } else {
                           Actor.X++;
                           Actor.W = false;
                        }
                     } else if (Math.abs(cameraX - player.positionX + 120) > 2) {
                        cameraTargetX = player.positionX - 120;
                        Actor.Y = 1;
                     } else {
                        Actor.X++;
                        Actor.W = false;
                     }
                  } else if (Actor.X == 3) {
                     var3 = player.facingLeft ? player.positionX - 160 : player.positionX - 80;
                     if (Math.abs(cameraX - var3) > 2) {
                        cameraTargetX = var3;
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

               if (player.velocityX == 0 && (var0 & 4) == 0 && Actor.playerPlatform == null && !player.bU && activeCameraControllerUid == -1 && !ab && !Actor.W
                  )
                {
                  P = 0;
               } else if (Actor.W && activeCameraControllerUid == -1) {
                  P = j(P, cameraTargetX - cameraX, at[Actor.Y], at[Actor.Y + 3]);
               } else if (X == 2) {
                  P = j(P, cameraTargetX - cameraX, 12, 2);
               } else {
                  P = j(P, cameraTargetX - cameraX, V, 2);
               }

               if (X == 1 || X == 0 && Y == 1) {
                  P = 0;
               }

               Q = j(Q, cameraTargetY - cameraY, 32, 2);
               if (activeCameraControllerUid != -1 && cameraControllerMode == 1) {
                  Q = j(Q, cameraTargetY - cameraY, 40, 15);
               }

               if (!ah && !Actor.cu) {
                  cameraX = cameraX + P;
               }

               cameraY = cameraY + Q;
               if (activeCameraControllerUid != -1 && cameraControllerMode >= 4) {
                  if (cameraScrollHorizontal) {
                     cameraX = cameraTargetX;
                  } else {
                     cameraY = cameraTargetY;
                  }
               }
            } else {
               cameraTargetX = player.facingLeft ? player.positionX - 160 : player.positionX - 80;
               if (cameraTargetX < 0) {
                  cameraTargetX = 0;
               }

               if (levelModes[levelIndex] == 3 && levelIndex != 9) {
                  cameraTargetY = 0;
               } else {
                  cameraTargetY = player.positionY - 220;
               }

               if (cameraTargetY + 320 > levelPixelHeight) {
                  cameraTargetY = levelPixelHeight - 320;
               }

               if (cameraTargetY < 0) {
                  cameraTargetY = 0;
               }

               if (as == 0) {
                  cameraY = cameraTargetY;
                  cameraX = cameraTargetX;
                  P = 0;
                  Q = 0;
               } else {
                  P = j(P, cameraTargetX - cameraX, V, 2);
                  Q = j(Q, cameraTargetY - cameraY, 32, 2);
               }

               ap = cameraTargetY + 100;
            }
         }

         if (Actor.godMode && Actor.bH) {
            cameraX = player.positionX - 120;
            cameraY = player.positionY - 160;
         }

         if (var1) {
            applyShakeAndClampCamera();
         } else {
            if ((var0 & 1) == 0 || as == 1 || ai) {
               applyCameraConstraints();
               as = 0;
            } else if (shakeXTicks <= 0 && shakeYTicks <= 0 && au != null) {
               clampCameraToScriptBounds();
            }

            updateViewBounds();
         }
      }
   }

   // $VF: renamed from: f () void
   public static final void updateViewBounds() {
      viewBounds[0] = cameraX;
      viewBounds[1] = cameraY;
      viewBounds[2] = cameraX + 240;
      viewBounds[3] = cameraY + 320;
   }

   // $VF: renamed from: g () void
   public static final void clampCameraToScriptBounds() {
      if (R > 0 && cameraX < R) {
         cameraX = R;
      } else if (S > 0 && cameraX > S - 240) {
         cameraX = S - 240;
      }

      if (T > 0 && cameraY < T) {
         cameraY = T;
      } else {
         if (U > 0 && cameraY > U - 320) {
            cameraY = U - 320;
         }
      }
   }

   // $VF: renamed from: h () void
   public static final void applyCameraConstraints() {
      clampCameraToScriptBounds();
      applyShakeAndClampCamera();
      if (player.positionX - 80 <= R && player.velocityX > 0 && activeCameraControllerUid == -1 && shakeYTicks <= 0) {
         cameraX = R;
      } else {
         if (player.positionX + 80 > S && player.velocityX < 0 && activeCameraControllerUid == -1 && shakeYTicks <= 0) {
            cameraX = S - 240;
         }
      }
   }

   // $VF: renamed from: L () void
   private static void applyShakeAndClampCamera() {
      if (shakeXTicks > 0) {
         int var0;
         var0 = (var0 = shakeXTicks / 2) + shakeXTicks % 2;
         if ((shakeXTicks & 1) != 0) {
            var0 = -var0;
         }

         cameraX += var0;
         shakeXTicks--;
      }

      if (shakeYTicks > 0) {
         int var2;
         if ((var2 = shakeYTicks / 2) > 10) {
            var2 = 10;
         }

         var2 += shakeXTicks % 2;
         if ((shakeYTicks & 1) != 0) {
            var2 = -var2;
         }

         cameraY += var2;
         shakeYTicks--;
      }

      if (cameraX < 0) {
         cameraX = 0;
      } else if (cameraX > levelPixelWidth - 240) {
         cameraX = levelPixelWidth - 240;
      }

      if (cameraY < 0) {
         cameraY = 0;
      } else {
         if (cameraY > levelPixelHeight - 320) {
            cameraY = levelPixelHeight - 320;
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

   public static final void a(Actor var0) {
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
         var0.refreshBounds();
      }
   }

   static final void i() {
      while (aA > 0) {
         int var0 = az[aA--];
         collisionTiles[var0] = (byte)az[aA--];
      }
   }

   // $VF: renamed from: e (int) void
   static final void startVerticalShake(int var0) {
      if (shakeYTicks <= 0) {
         if (var0 > 0) {
            shakeYTicks = var0;
            return;
         }

         shakeYTicks = -var0;
      }
   }

   // $VF: renamed from: a (int, int) void
   static final void startCameraShake(int var0, int var1) {
      var0 = var0 >> 1 << 1;
      if (shakeXTicks <= 0) {
         if (var0 > 0) {
            shakeXTicks = var0;
         } else {
            shakeXTicks = -var0;
         }
      }

      if (shakeYTicks <= 0) {
         if (var1 > 0) {
            shakeYTicks = var1;
            return;
         }

         shakeYTicks = -var1;
      }
   }

   // $VF: renamed from: a (java.lang.String, java.lang.String) void
   static final void drawSoftkeys(String var0, String var1) {
      drawSoftkeyBar(var0, var1, false);
   }

   // $VF: renamed from: a (java.lang.String, java.lang.String, boolean) void
   static final void drawSoftkeyBar(String var0, String var1, boolean var2) {
      String var3 = var0;
      var0 = var1;
      var1 = var3;
      if (!var2) {
         boolean var4 = false;
         setClip(0, 296, 240, 24);
         setColor(0);
         fillRect(0, 296, 240, 24);
         setColor(15692292);
         drawRect(0, 296, 239, 22);
      } else {
         setClip(0, 296, 240, 24);
         setColor(0);
         fillRect(0, 296, 240, 24);
      }

      font.currentPalette = 0;
      if (var0 != null && var0 != "") {
         font.drawText(graphics, var0, 2, 304, 20);
      }

      if (var1 != null && var1 != "") {
         font.drawText(graphics, var1, 238, 304, 24);
      }
   }

   // $VF: renamed from: M () void
   private static void drawConfirmation() {
      directGraphics.setARGBColor(-2013265920);
      setClip(0, 0, 240, 320);
      fillRect(0, 0, 240, 320);
      e(0, 100, 240, 100);
      font.currentPalette = 0;
      switch (confirmationKind) {
         case 6:
            font.drawWrappedText(graphics, getString(0, 14), 120, 150, 3, 210, 0, -1);
            break;
         case 7:
            font.drawWrappedText(graphics, getString(0, 38), 120, 150, 3, 210, 0, -1);
      }

      drawSoftkeys(getString(0, 16), getString(0, 15));
   }

   // $VF: renamed from: N () void
   private static void updateLogoLoading() throws Exception {
      setClip(0, 0, 240, 320);
      setColor(16777215);
      fillRect(0, 0, 240, 320);

      try {
         if (stateFrame == 1 && logoImage == null) {
            logoImage = Image.createImage("/logo.png");
         }
      } catch (Exception var1) {
         var1.printStackTrace();
      }

      drawImage(logoImage, 120, 160, 3);
      b(stateFrame, 47);
      if (stateFrame == 2) {
         af();
      } else if (stateFrame == 7) {
         preloadMenuSound();
      } else if (stateFrame == 12) {
         ag();
      } else {
         if (stateFrame == 17) {
            InGamePromotion.a(GloftSOFD.instance, GloftSOFD.game, 240, 320);
            String var0;
            if ((var0 = GloftSOFD.instance.getAppProperty("GL-Censored")) != null) {
               bloodCensored = var0.trim().toLowerCase().compareTo("true") == 0;
               return;
            }

            if ((var0 = GloftSOFD.instance.getAppProperty("BLOOD")) != null) {
               bloodCensored = var0.trim().toLowerCase().compareTo("0") == 0;
               return;
            }
         } else {
            if (stateFrame == 22) {
               accessSaveRecord(false);
               return;
            }

            if (stateFrame == 27) {
               loadStringTable(0);
               return;
            }

            if (stateFrame == 32) {
               an();
               return;
            }

            if (stateFrame == 37) {
               ao();
               return;
            }

            if (stateFrame == 42) {
               return;
            }

            if (stateFrame >= 47) {
               logoImage = null;
               changeState(23);
               soundEnabled = false;
               return;
            }

            System.gc();
         }
      }
   }

   private static void a(int var0, boolean var1) {
      try {
         openPack("/1");
         if (var1) {
            titleSprite = loadSprite(1);
            titleSprite.buildImages(var0, 0, -1, -1);
            titleImage = titleSprite.cachedImages[var0][0];
         }

         titleSprite.cachedImages[0][0] = null;
         titleSprite = null;
         closePack();
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   // $VF: renamed from: O () void
   private static void updateSoundPrompt() {
      setClip(0, 0, 240, 320);
      setColor(0);
      fillRect(0, 0, 240, 320);
      font.drawText(graphics, getString(0, 22), 120, 160, 3);
      drawSoftkeys(getString(0, 16), getString(0, 15));
      if (wasKeyPressed(131072)) {
         soundEnabled = false;
         changeState(18);
      } else {
         if (wasKeyPressed(262144)) {
            soundEnabled = true;
            changeState(18);
            loadSoundData(-1);
            playSoundOnce(0);
         }
      }
   }

   // $VF: renamed from: P () void
   private static void startNewGame() {
      dh = 0;
      df = 9;
      levelIndex = 0;
      maxUnlockedLevel = 0;
      changeState(20);

      for (int var0 = 0; var0 < saveBuffer.length; var0++) {
         writeByte(saveBuffer, var0, 0);
      }

      resetPlayerMeters();
      writeI16LE(saveBuffer, 32, (short)1);
      writeI16LE(saveBuffer, 60, (short)maxHealth);
      writeI16LE(saveBuffer, 62, (short)health);
      writeI16LE(saveBuffer, 64, (short)r);
      writeI16LE(saveBuffer, 66, (short)maxMagic);
      writeI16LE(saveBuffer, 68, (short)magic);
      writeI16LE(saveBuffer, 70, (short)u);
      accessSaveRecord(true);
      restoreCheckpoint();
      Actor.bl = false;
      Actor.bm = false;
      Actor.bn = false;
   }

   // $VF: renamed from: Q () void
   private static void updateGameOver() {
      k = true;
      drawWorld();
      setClip(0, 0, 240, 320);
      g(7, 20, 226, 265);
      font.currentPalette = 1;
      if (state == 12) {
         font.drawText(graphics, getString(0, 84), 120, 160, 3);
      }

      font.currentPalette = 0;
      font.drawWrappedText(graphics, getString(0, 27), 120, 192, 3, 230, 0, -1);
      drawSoftkeys(getString(0, 16), getString(0, 15));
      if (wasKeyPressed(131072)) {
         unloadLevel();
         accessSaveRecord(true);
         a(false);
         changeState(19);
      } else {
         if (wasKeyPressed(262144)) {
            playSoundOnce(-1);
            restartLevel();
         }
      }
   }

   // $VF: renamed from: R () void
   private static void updateHelp() {
      setClip(0, 0, 240, 320);
      g(0, 0, 240, 320);
      font.currentPalette = 0;
      font.drawText(graphics, getString(0, 7), 120, 25, 17);
      font.currentPalette = 0;
      font.drawWrappedText(graphics, getString(0, 31 + selectedMenuItem), 120, 160, 3, 200, 0, -1);
      drawSoftkeys(getString(0, 18), getString(0, 17));
      font.drawWrappedText(graphics, selectedMenuItem + 1 + "/" + 4, 120, 304, 17, 230, 0, -1);
      if (wasKeyPressed(270400)) {
         selectedMenuItem = ++selectedMenuItem % 4;
         playSoundOnce(-1);
      }

      if (wasKeyPressed(131072)) {
         changeState(previousState);
         selectedMenuItem = 0;
      }

      if (wasKeyPressed(4112)) {
         selectedMenuItem = (--selectedMenuItem + 4) % 4;
         playSoundOnce(-1);
      }
   }

   // $VF: renamed from: j () void
   public static final void updatePromotionPrompt() {
      setClip(0, 0, 240, 320);
      setColor(0);
      fillRect(0, 0, 240, 320);
      font.drawWrappedText(graphics, getString(0, 37), 120, 160, 3, 230, 0, -1);
      drawSoftkeys("", getString(0, 17));
      if (wasKeyPressed(327712)) {
         playSoundOnce(-1);
         if (InGamePromotion.a()) {
            changeState(7);
            boolean var0 = false;
            InGamePromotion.a(getString(0, 25) + "...", 0);
            return;
         }

         changeState(2);
      }
   }

   // $VF: renamed from: S () void
   private static void updatePromotion() {
      InGamePromotion.a(graphics);
      byte var0 = 0;
      if (wasKeyPressed(16388)) {
         var0 = 21;
         aD = false;
      } else if (wasKeyPressed(33024)) {
         var0 = 32;
         aD = false;
      } else if (wasKeyPressed(4112)) {
         var0 = 23;
         aD = false;
      } else if (wasKeyPressed(8256)) {
         var0 = 24;
         aD = false;
      } else if (wasKeyPressed(131072)) {
         var0 = 26;
         aD = false;
      } else if (wasKeyPressed(327712)) {
         var0 = 25;
         aD = true;
      }

      if (InGamePromotion.a(var0) && !aD) {
         changeState(2);
      }
   }

   public static final void a(int var0, int var1, int var2, int var3, int var4) {
      setClip(0, 0, 240, 320);
      directGraphics.setARGBColor(-587202560);
      fillRect(var0, var1, var2, var3);
      setColor(15692292);
      drawRect(var0 - 1, var1 - 1, var2 + 1, var3 + 1);
   }

   // $VF: renamed from: T () void
   private static void updatePauseMenu() {
      drawWorld();
      a(53, 80, 134, 157, 187);
      drawMenuItems(1, 120, 110);
      if (confirmationOpen) {
         confirmationKind = 6;
         drawConfirmation();
      }

      handleMenuAction(1);
      if (!confirmationOpen) {
         navigateMenu(menuItems[1].length, false);
      }
   }

   public static final void a(int var0, int var1, int var2, int var3, int[] var4, int[] var5) {
      setColor(0);
      fillRect(var0, var1 + 1, var2 + 1, 9);
      setColor(9794829);
      drawLine(var0, var1, var0 + var2, var1);
      drawLine(var0 - 1, var1 + 1, var0 - 1, var1 + 7 + 1);
      drawLine(var0 + var2 + 1, var1 + 1, var0 + var2 + 1, var1 + 7 + 1);
      drawLine(var0, var1 + 7 + 2, var0 + var2, var1 + 7 + 2);
      a(var0 + 1, var1 + 2, var2 - 2, var4);
      if (var3 > 2) {
         a(var0 + 1, var1 + 2, var3 - 2, var5);
      }
   }

   public static final void a(int var0, int var1, int var2, int[] var3) {
      for (int var4 = 0; var4 < var3.length; var4++) {
         setColor(var3[var4]);
         drawLine(var0, var1 + var4, var0 + var2, var1 + var4);
      }
   }

   // $VF: renamed from: U () void
   private static void finishLevel() {
      Actor.bC = false;
      writeByte(saveBuffer, 644, (byte)(Actor.bC ? 1 : 0));
      if (maxUnlockedLevel < levelIndex + 1) {
         maxUnlockedLevel = levelIndex + 1;
      }

      levelCompleted = true;
      Actor.serializeProgress();
      accessSaveRecord(true);
      unloadLevel();
      a(true);
      changeState(19);
   }

   // $VF: renamed from: V () void
   private static void drawActors() {
      setClipInternal(0, 0, 240, 320);
      paintActorCount = 0;

      for (int var1 = 0; var1 < actorCount; var1++) {
         if (actors[var1] != null && (actors[var1].flags & 1024) == 0) {
            Actor var0;
            if ((var0 = actors[var1]).isVisible()) {
               if (actors[var1].type != 22
                  && actors[var1].type != 49
                  && actors[var1].type != 20
                  && actors[var1].type != 40
                  && actors[var1].type != 23
                  && actors[var1].type != 59
                  && (actors[var1].flags & 32768) == 0) {
                  enqueuePaintActor(var0);
               }
            } else if (actors[var1].updatedThisTick) {
               actors[var1].advanceAnimation();
            }
         }
      }

      if (hasActiveBackgroundScript()) {
         for (int var4 = 0; var4 < bC; var4++) {
            Actor var2;
            if (bB[var4] != null && ((var2 = bB[var4]).flags & 128) == 0) {
               enqueuePaintActor(var2);
            }
         }
      }

      if ((player.flags & 128) == 0) {
         enqueuePaintActor(player);
      }

      if (3 == levelModes[levelIndex]) {
         if (bm != null && (bm.flags & 128) == 0) {
            enqueuePaintActor(bm);
         }

         if (bn != null && (bn.flags & 128) == 0) {
            enqueuePaintActor(bn);
         }
      }

      for (int var5 = 0; var5 < paintActorCount; var5++) {
         Actor var3;
         if ((var3 = paintActors[var5]) != null) {
            var3.paint();
         }
      }
   }

   // $VF: renamed from: f (b) void
   private static void enqueuePaintActor(Actor var0) {
      int var1 = 0;

      while (var1 < paintActorCount && paintActors[var1].paintPriority <= var0.paintPriority) {
         var1++;
      }

      if (paintActorCount >= bq) {
         for (int var2 = 0; var2 < bq; var2++) {
            System.out.println("paint out of bound: " + paintActors[var2].type);
         }
      }

      for (int var3 = paintActorCount; var3 > var1; var3--) {
         paintActors[var3] = paintActors[var3 - 1];
      }

      paintActors[var1] = var0;
      paintActorCount++;
   }

   // $VF: renamed from: g (b) void
   private static void enqueueUpdateActor(Actor var0) {
      if (updateActorCount >= br) {
         for (int var1 = 0; var1 < br; var1++) {
            System.out.println("update out of bound: " + updateActors[var1].type);
         }
      }

      updateActors[updateActorCount++] = var0;
   }

   // $VF: renamed from: h (b) boolean
   private static boolean shouldUpdateActor(Actor var0) {
      if ((var0.flags & 16) != 0) {
         return true;
      }

      if ((var0.flags & 256) != 0 || (var0.flags & 32) != 0) {
         return false;
      }

      if (au == null && var0.type == 22) {
         return true;
      }

      byte var1 = 2;
      if (var0.type == 40 || var0.type == 49) {
         var1 = 7;
         if (var0.type == 49 && Actor.rectsOverlapInclusive(var0.collisionBounds, player.collisionBounds)) {
            return true;
         }
      } else if (var0.type == 78) {
         var1 = 14;
      } else if (var0.type == 45) {
         var1 = 4;
      } else if (var0.type == 10) {
         var1 = 4;
      } else if (var0.type == 37) {
         var1 = 3;
      } else if (var0.type == 89) {
         var1 = 3;
      }

      return var0.cameraDistanceBucket < var1;
   }

   // $VF: renamed from: k () void
   public static final void refreshCameraRegionSelection() {
      for (int var0 = 0; var0 < actorCount; var0++) {
         Actor var1;
         if ((var1 = actors[var0]) != null && var1.type == 22 && au != var1) {
            var1.update();
            if (ai) {
               return;
            }
         }
      }
   }

   // $VF: renamed from: W () void
   private static void updateWorldActors() {
      player.update();
      if (player.actionState != 0 || player.animationId != 9) {
         if (Actor.playerPlatform != null && Actor.playerPlatform.type == 33 && player.ceilingHit) {
            Actor.playerPlatform.velocityY = 0;
         }

         if (ai) {
            for (int var1 = 0; var1 < actorCount; var1++) {
               Actor var0;
               if ((var0 = actors[var1]) != null) {
                  if (var0.type == 74) {
                     if (var0.s == null && var0.aB != -1) {
                        var0.s = findActorByUid(var0.aB);
                     }

                     if (var0.s != null) {
                        if (var0.s == au) {
                           var0.flags |= 16;
                           var0.flags &= -33;
                           var0.changeActionState(0);
                        } else {
                           var0.flags &= -17;
                           var0.flags |= 32;
                        }
                     }
                  }

                  if (var0.isRespawnable()) {
                     if (au != null && Actor.rectsOverlapInclusive(var0.collisionBounds, au.collisionBounds)) {
                        var0.flags &= -1025;
                     } else if (av != null && Actor.rectsOverlapInclusive(var0.collisionBounds, av.collisionBounds)) {
                        var0.resetOrRemove(false);
                     }

                     if (actors[var1] != null) {
                        actors[var1].setAttachedActor(null);
                     }
                  } else if (var0.isBasicEnemyType()) {
                     if (var0.actionState != 0) {
                        var0.resetOrRemove(false);
                     }
                  } else if (var0 != null && var0.uid == -1 && var0.type != 84 && var0.type != 85 && (var0.type != 39 || var0.animationId != 13)) {
                     removeActor(var0);
                  }
               }
            }

            ai = false;
         }

         updateActorCount = 0;

         for (int var4 = 0; var4 < actorCount; var4++) {
            Actor var2;
            if ((var2 = actors[var4]) != null && (!ai || var2.type != 22) && var2.type != 0) {
               var2.updatedThisTick = false;
               if (shouldUpdateActor(var2)) {
                  enqueueUpdateActor(var2);
               }
            }
         }

         for (int var5 = 0; var5 < updateActorCount; var5++) {
            Actor var3;
            if ((var3 = updateActors[var5]) != null && Actor.cq[0] != var3 && Actor.cq[1] != var3 && Actor.cq[2] != var3 && (!ai || var3.type != 22)) {
               var3.update();
               if (var3 != null && var3.attachedActor != null && !var3.attachedActor.updatedThisTick) {
                  var3.attachedActor.update();
               }
            }
         }

         if (3 == levelModes[levelIndex] && bm != null && bn != null) {
            bm.update();
            bn.update();
         } else {
            if (2 == levelModes[levelIndex] && !aS) {
               Actor.cn = -512;
               Actor.cp = 0;

               for (int var6 = 0; var6 < 3; var6++) {
                  if (Actor.cq[var6] != null) {
                     Actor.cq[var6].K();
                  }
               }

               if (Actor.cn >= 0) {
                  Actor.cn = 0;
               }

               if (Actor.cp == 0) {
                  if (Actor.J()) {
                     if (Actor.cn > -768) {
                        if (Actor.co == 0) {
                           Actor.co++;
                        }

                        Actor.cn = Actor.cn + -128 * Actor.co;
                     } else {
                        Actor.cn = -768;
                        Actor.co = 0;
                     }
                  } else if (Actor.cn != 0) {
                     Actor.cn = -512;
                  }
               }

               for (int var7 = 0; var7 < 3; var7++) {
                  if (Actor.cq[var7] != null) {
                     Actor.cq[var7].update();
                  }
               }
            }
         }
      }
   }

   // $VF: renamed from: X () void
   private static void updateGameplay() {
      if (wasKeyPressed(2048) && cheatsUnlocked) {
         if (!Actor.godMode) {
            Actor.godMode = true;
            Actor.bI = true;
            v = 50000;
         } else {
            Actor.godMode = false;
            Actor.bI = false;
         }

         if (!Actor.godMode) {
            Actor.bH = false;
         }
      }

      if (Actor.godMode && wasKeyPressed(1024)) {
         Actor.bH = !Actor.bH;
      }

      if (bc == 5) {
         scriptsPaused = true;
      }

      if (hasActiveBackgroundScript()) {
         updateBackgroundScripts();
         if (Actor.cw != null) {
            Actor.cw.update();
         }

         if (Actor.bC && bm != null && bn != null && levelIndex == 3) {
            bm.positionX = bn.positionX = player.positionX;
            bm.positionY = bn.positionY = player.positionY;
         }
      } else {
         if (cj > 0) {
            updateSelectedScripts();
         }

         updateWorldActors();
      }

      if (cg) {
         for (; hasActiveBackgroundScript(); updateBackgroundScripts()) {
            if (Actor.bC && bm != null && bn != null && levelIndex == 3) {
               bm.positionX = bn.positionX = player.positionX;
               bm.positionY = bn.positionY = player.positionY;
            }
         }

         cg = false;
      }

      drawWorld();
      if (wasKeyPressed(262144) && !aS && !hasActiveBackgroundScript()) {
         selectedMenuItem = 0;
         changeState(14);
      } else {
         if (Actor.godMode && aU == null) {
            font.currentPalette = 0;
            font.drawText(graphics, "is GOD MODULE!", 237, 20, 9);
         }

         if (ar != -1) {
            updateCamera(ar);
            ar = -1;
         } else {
            updateCamera(2);
         }
      }
   }

   // $VF: renamed from: Y () void
   private static void drawWorld() {
      if (Actor.cu) {
         a(cameraX + Actor.cv % 240, 0, true);
         d(cameraX + Actor.cv % 240, 0, 2);
      } else if (levelModes[levelIndex] == 3) {
         a(cameraX, cameraY < 0 ? 0 : cameraY, true);
         d((cameraX >> 3) % (bV * 20), cameraY < 0 ? 0 : cameraY, 2);
      } else {
         a(cameraX, cameraY, true);
         d(cameraX, cameraY < 0 ? 0 : cameraY, 1);
      }

      setClip(0, 0, 240, 320);
      if (!hasActiveBackgroundScript() && !Actor.bd && !Actor.bi && (Actor.activeBoss == null || Actor.activeBoss.actionState != 3)) {
         if (player.animationId == 18) {
            f(3, player.positionX - cameraX, player.positionY - cameraY - 30, 80);
         } else if (player.animationId == 23) {
            f(4, player.positionX - cameraX + (player.facingLeft ? -40 : 40), player.positionY - cameraY - 50, 80);
         }
      }

      if (aS) {
         if (bc >= 6 && bc <= 8) {
            f(bc - 6, player.positionX - cameraX + (player.facingLeft ? -20 : 20), player.positionY - cameraY - 55, 40);
         } else if (bc == 13) {
            Actor.activeBoss.y();
         } else if (bc == 3) {
            a(aT, true, 0);
         } else if (bc == 10 || bc == 11) {
            a(0, true, 0);
         }
      }

      drawActors();
      if (Actor.weaponSwitchCooldownMs > 0) {
         a(Actor.activeWeapon, player.positionX - cameraX, player.positionY - cameraY + 0 + -80);
      }

      for (int var0 = 0; var0 < paintActorCount; var0++) {
         Actor var1;
         if ((var1 = paintActors[var0]) == null) {
            System.out.println("null actor in s_paintActors");
         }

         if (var1.linkedEffect != null) {
            var1.linkedEffect.isVisible();
         }
      }

      D();
      if (aS && bc == 14) {
         setClipInternal(0, 0, 240, 320);
         d();
      }

      if (w > 0L) {
         k = true;
         w = w - tickDeltaMs;
         if (w <= 0L) {
            clearInput();
         }

         sprites[35].drawAnimationFrame(graphics, 2, stateFrame % 4 < 2 ? 0 : 1, player.positionX - cameraX - 0, player.positionY - cameraY - 0, 0, 0, 0);
      }

      updateLetterbox();
      if (bc == 5 && aS) {
         e();
      }

      if (aS && bc == 18) {
         levelCompleted = true;
         health = maxHealth;
         Actor.serializeProgress();
         accessSaveRecord(true);
         unloadLevel();
         scriptTicks = null;
         stopSound();
         changeState(6);
         l();
      }

      if (hasActiveBackgroundScript()) {
         drawSoftkeyBar(bc != 17 && ch ? getString(0, 20) : "", bc == 5 ? getString(0, 17) : "", true);
      }

      if (aw > 0) {
         if (ay != 0) {
            ay--;
            directGraphics.setARGBColor(-16777216);
         } else {
            directGraphics.setARGBColor(aw * ax << 24);
         }

         fillRect(0, 0, 240, 320);
         aw--;
      }

      if (!hasActiveBackgroundScript() && aU == null && aN <= 0 && !aS && state == 8) {
         if (cm) {
            updateHudSlide();
            drawHud();
         }

         a();
      } else {
         if (k && cm) {
            drawHud();
         }

         a();
      }

      if (!hasActiveBackgroundScript()) {
         updateLetterbox();
      }

      if (!hasActiveBackgroundScript() && Actor.activeBoss != null && (Actor.activeBoss.flags & 128) == 0 && (Actor.activeBoss.flags & 32) == 0) {
         c(Actor.activeBoss.health, Actor.bossInitialHealth);
      }
   }

   static final void g(int var0, int var1, int var2, int var3) {
      e(var0, var1, var2, var3);
   }

   static final void b(int var0, int var1) {
      a(60, 265, 120, var0 * 120 / var1, be, aK);
      if (font != null) {
         font.drawText(graphics, "" + var0 * 100 / var1 + "%", 120, 280, 17);
      }
   }

   private static void b(boolean var0, int var1) {
      setClip(0, 0, 240, 320);
      g(0, 0, 240, 320);
      Actor.a(120, 160, 120, 160, 60, 50, stateFrame * 5, true);
      font.currentPalette = 0;
      if (levelIndex != 16) {
         String var2 = getString(0, 62 + dv);
         font.drawWrappedText(graphics, var2, 120, 30, 17, 230, 0, -1);
      }

      if (stateFrame <= var1) {
         b(stateFrame, var1);
         aL = false;
      } else {
         if (stateFrame % 10 < 5 && var0) {
            font.drawText(graphics, getString(0, 10), 120, 265, 17);
         }
      }
   }

   // $VF: renamed from: Z () void
   private static void updateLevelLoading() {
      b(true, 80);
      if (!aL && loadLevelStep(stateFrame)) {
         aL = true;
      }

      if (aL && hasKeyPress()) {
         initializeCamera();
         aN = 160;
         aM = 0;
         changeState(8);
         k = true;
         aQ = 2;
      }

      System.gc();
   }

   // $VF: renamed from: aa () void
   private static void prepareLevelSelectionStats() {
      if (readI16LE(saveBuffer, 16) > 0) {
         restoreProgress();
         skipCheckpointPosition = true;
      } else {
         resetPlayerMeters();
         skipCheckpointPosition = true;
      }

      dw = false;
   }

   // $VF: renamed from: H (int) void
   private static void handleMenuAction(int var0) {
      if (confirmationOpen) {
         if (wasKeyPressed(131072)) {
            confirmationOpen = false;
            stateFrame = 0;
            clearInput();
            return;
         }

         if (wasKeyPressed(262144)) {
            if (menuItems[var0][selectedMenuItem] != 13) {
               playSoundOnce(-1);
            }

            confirmationOpen = false;
            if (menuItems[var0][selectedMenuItem] == 9) {
               state = 11;
            } else if (menuItems[var0][selectedMenuItem] == 13) {
               restartLevel();
            } else if (menuItems[var0][selectedMenuItem] == 0) {
               unloadLevel();
               changeState(2);
            } else if (menuItems[var0][selectedMenuItem] == 1) {
               startNewGame();
            }

            clearInput();
            return;
         }
      } else {
         if (wasKeyPressed(131072)) {
            if (var0 == 1) {
               changeState(8);
               k = true;
            }

            clearInput();
            return;
         }

         if (wasKeyPressed(327712)) {
            switch (menuItems[var0][selectedMenuItem]) {
               case 0:
                  confirmationOpen = true;
                  break;
               case 1:
                  if (readI16LE(saveBuffer, 16) > 0) {
                     confirmationOpen = true;
                     confirmationKind = 7;
                  } else {
                     changeState(20);
                  }
                  break;
               case 2:
                  titleImage = null;
                  levelIndex = readSignedByte(saveBuffer, 14);
                  maxUnlockedLevel = readSignedByte(saveBuffer, 15);
                  levelCompleted = readI16LE(saveBuffer, 24) == 1;
                  continueFromCheckpoint = true;
                  if (levelCompleted && levelIndex < 8) {
                     levelIndex++;
                     continueFromCheckpoint = false;
                  }

                  if (readI16LE(saveBuffer, 16) > 0) {
                     restoreProgress();
                  } else {
                     resetPlayerMeters();
                  }

                  changeState(9);
                  break;
               case 3:
                  levelIndex = readSignedByte(saveBuffer, 14);
                  df = 9;
                  dw = true;
                  a(levelCompleted);
                  if (ed) {
                     maxUnlockedLevel = 9;
                  } else {
                     maxUnlockedLevel = readSignedByte(saveBuffer, 15);
                  }

                  changeState(19);
                  break;
               case 4:
                  if (InGamePromotion.a()) {
                     A(16);
                     writeByte(saveBuffer, 10, (byte)cy);
                     accessSaveRecord(true);
                     changeState(7);
                     boolean var2 = false;
                     InGamePromotion.a(getString(0, 25) + "...", 0);
                  }
                  break;
               case 5:
                  if (InGamePromotion.d()) {
                     A(32);
                     writeByte(saveBuffer, 10, (byte)cy);
                     accessSaveRecord(true);
                     changeState(7);
                     boolean var1 = false;
                     InGamePromotion.b(getString(0, 25) + "...", 0);
                  }
                  break;
               case 6:
                  soundEnabled = !soundEnabled;
                  stateFrame = 0;
                  if (!soundEnabled) {
                     stopSound();
                  } else if (soundEnabled) {
                     if (var0 == 1) {
                        playLevelMusic();
                     } else {
                        playSound(0, -1);
                     }
                  }
                  break;
               case 7:
                  selectedMenuItem = 0;
                  playSoundOnce(-1);
                  changeState(5);
                  break;
               case 8:
                  changeState(6);
                  break;
               case 9:
                  confirmationOpen = true;
                  confirmationKind = 6;
               case 10:
               case 11:
               default:
                  break;
               case 12:
                  changeState(8);
                  k = true;
                  break;
               case 13:
                  confirmationOpen = true;
            }

            if (menuItems[var0][selectedMenuItem] != 12 && menuItems[var0][selectedMenuItem] != 6) {
               playSoundOnce(-1);
            }

            clearInput();
         }
      }
   }

   // $VF: renamed from: ab () void
   private static void initialize() throws Exception {
      sprites = new BSprite[53];
      changeState(1);
   }

   // $VF: renamed from: ac () void
   private static void updateLetterbox() {
      if (aO && hasActiveBackgroundScript()) {
         fillRect(0, 0, 240, aN);
         fillRect(0, 320 - aN, 240, aN);
         aN += 20;
      } else if (levelIndex == 8 && aP) {
         fillRect(0, 0, 240, aN);
         fillRect(0, 320 - aN, 240, aN);
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
            setClipInternal(0, 0, 240, 320);
            setColor(-587202560);
            fillRect(0, 0, 240, aN);
            fillRect(0, 320 - aN, 240, aN);
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
               Actor.cj[var1] = randomRange(var1 * 36, var1 * 36 + 18);
               Actor.ck[var1] = randomRange(5, 15);
            }

            player.flags |= 4096;
            bo.flags |= 4096;
         }
      }
   }

   public static final void l() {
      aS = false;
      if (bc != 5) {
         cm = true;
      }

      if (bc == 10 || bc == 11) {
         state = 32;
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
         removeActor(bd);
         bd = null;
      }

      aU = null;
      scriptsPaused = false;
   }

   public static final void a(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = 6 * sin256(var4);
      int var8 = 6 * cos256(var4);
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
               sprites[28].drawAnimationFrame(graphics, 13, 0, var14, var15 + 3, 0, 0, 0);
            } else {
               sprites[28].drawAnimationFrame(graphics, 13, var19 - 9, var14, var15 + 3, 0, 0, 0);
            }

            var11 = true;
            var13++;
         } else {
            setColor(16777215);
            drawLine(var14, var15, var16, var17);
            setColor(13027014);
            drawLine(var14 - 2, var15 + 2, var16 - 2, var17 + 2);
            drawLine(var14 + 1, var15 + 2, var16 + 1, var17 + 2);
            setColor(7631988);
            drawLine(var14 - 1, var15, var16 - 1, var17);
            setColor(6579300);
            drawLine(var14 - 3, var15 + 2, var16 - 3, var17 + 2);
            drawLine(var14 + 2, var15 + 2, var16 + 2, var17 + 2);
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
            int var5 = dy[randomRange(0, 3)];
            directGraphics.setARGBColor(var5);
            int var6 = randomRange(0, 2);
            int var7 = randomRange(5, 7);
            int var8 = randomRange(2, 4);
            int var9 = var0;
            int var10 = var2 - randomRange(0, 3);
            int var11 = 0;
            int var12 = var0;
            if (var6 == 0) {
               while (var9 < var3 - 5 || var12 < var3 - 5) {
                  var12 = var9 + var7 * dx[randomRange(0, 10)] / 1000;
                  var11 = var2 - var8 + randomRange(-1, 2);
                  drawLine(var10, var9, var11, var12);
                  if (randomRange(0, 4) == 0) {
                     drawLine(var11, var12, var11, var12 + 1);
                     var12++;
                  }

                  var9 = var12 + var7 * dx[randomRange(0, 10)] / 1000;
                  var10 = var2 + var8 + randomRange(-1, 2);
                  drawLine(var11, var12, var10, var9);
                  if (randomRange(0, 4) == 0) {
                     drawLine(var10, var9, var10, var9 + 1);
                     var9++;
                  }
               }
            } else {
               while (var9 < var3 - 5 || var12 < var3 - 5) {
                  var12 = var9 + var7 * dx[randomRange(0, 10)] / 1000;
                  var11 = var2 + var8 + randomRange(-1, 2);
                  drawLine(var10, var9, var11, var12);
                  if (randomRange(0, 4) == 0) {
                     drawLine(var11, var12, var11, var12 + 1);
                     var12++;
                  }

                  var9 = var12 + var7 * dx[randomRange(0, 10)] / 1000;
                  var10 = var2 - var8 + randomRange(-1, 2);
                  drawLine(var11, var12, var10, var9);
                  if (randomRange(0, 4) == 0) {
                     drawLine(var10, var9, var10, var9 + 1);
                     var9++;
                  }
               }
            }
         }
      }
   }

   private static void j(int var0, int var1) throws Exception {
      if (var0 == 0) {
         openPack("/16");
      } else if (var0 != 1) {
         if (var0 == 2) {
            dl = loadSprite(bi[var1 * 4 + 2]);
         } else if (var0 == 3) {
            dm = loadSprite(bi[var1 * 4 + 3]);
         } else if (var0 == 4) {
            dD = true;
            closePack();
         } else if (var0 != 5 && var0 != 6) {
            if (var0 == 7) {
               byte var2 = bj[var1 * 4 + 2];
               dl.currentPalette = var2;
               dl.buildImages(var2, 0, -1, -1);
               dl.releaseImageData();
               dz = dl.cachedImages[var2][0];
            } else if (var0 == 8) {
               byte var3 = bj[var1 * 4 + 3];
               dm.currentPalette = var3;
               dm.buildImages(var3, 0, -1, -1);
               dm.releaseImageData();
               dA = dm.cachedImages[var3][0];
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

   // $VF: renamed from: ae () void
   private static void ensureTileBuffer() {
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
      setClipInternal(0, 0, 240, 320);
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
      setClipInternal(var4, var5, var2, var3);
      drawImage(dB, var4 - var0, var5 - var1, 0);
   }

   private static void a(Graphics var0, int var1, int var2, int var3) {
      if ((tileLayer0[var3] & 255) != 255) {
         byte var6 = 0;
         int var4 = tileLayer0[var3] & 255;
         int var5;
         if ((var5 = (tileLayer0Transforms[var3 >> 2] & 255) >> (3 - (var3 & 3) << 1) & 3) != 0) {
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

      if ((tileLayer1[var3] & 255) != 255) {
         byte var11 = 0;
         int var9 = tileLayer1[var3] & 255;
         int var10;
         if ((var10 = (tileLayer1Transforms[var3 >> 2] & 255) >> (3 - (var3 & 3) << 1) & 3) != 0) {
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

   // $VF: renamed from: b (b) void
   public static final void addActor(Actor var0) {
      var0.persistenceIndex = -98;
      if (bz > 0) {
         actors[by[--bz]] = var0;
      } else if (actorCount < bp) {
         actors[actorCount++] = var0;
      } else {
         System.out.println("s_actors_num exceed maxnum" + bp);

         for (int var1 = 0; var1 < actorCount; var1++) {
            System.out.println(var1 + " actor == " + actors[var1].type + "   anim: " + actors[var1].animationId);
         }
      }
   }

   // $VF: renamed from: c (b) void
   public static final void removeActor(Actor var0) {
      if (var0 != null) {
         var0.setAttachedActor(null);
         var0.s = null;
         var0.t = null;
         var0.u = null;
         var0.v = null;
         var0.w = null;
         var0.x = null;
         var0.supportActor = null;
         var0.supportedActor = null;
         if ((var0.flags & 8192) != 0) {
            var0.flags |= 1024;
         } else {
            if (Actor.playerPlatform == var0) {
               Actor.playerPlatform = null;
            }

            for (int var1 = 0; var1 < actorCount; var1++) {
               if (actors[var1] == var0) {
                  if (var0.persistenceIndex != -98) {
                     bD[var0.persistenceIndex] = -99;
                  }

                  if ((actors[var1].flags & 512) != 0 && !Actor.cl) {
                     bH[bI++] = (short)actors[var1].uid;
                  }

                  actors[var1].releaseLinksAndBounds();
                  actors[var1] = null;
                  by[bz++] = var1;
                  break;
               }
            }

            for (int var2 = 0; var2 < updateActorCount; var2++) {
               if (updateActors[var2] == var0) {
                  updateActors[var2] = null;
                  break;
               }
            }

            if (Actor.aR != null) {
               for (int var3 = 0; var3 < Actor.aR.length; var3++) {
                  if (Actor.aR[var3] == var0) {
                     Actor.aR[var3] = null;
                     break;
                  }
               }
            }

            if (Actor.aM != null) {
               for (int var4 = 0; var4 < Actor.aM.length; var4++) {
                  if (Actor.aM[var4] == var0) {
                     Actor.aM[var4] = null;
                     break;
                  }
               }
            }

            if (Actor.cx != null) {
               for (int var5 = 0; var5 < Actor.cx.length; var5++) {
                  if (Actor.cx[var5] == var0) {
                     Actor.cx[var5] = null;
                     break;
                  }
               }
            }

            if (Actor.activeBoss == var0) {
               Actor.activeBoss = null;
            }
         }
      }
   }

   public static final Actor d(Actor var0) {
      if (var0 == null) {
         return null;
      }

      for (int var1 = 0; var1 < actorCount; var1++) {
         if (actors[var1] != null && actors[var1].attachedActor == var0) {
            return actors[var1];
         }
      }

      return null;
   }

   // $VF: renamed from: g (int) b
   public static final Actor findActorByUid(int var0) {
      if (player != null && player.uid == var0) {
         return player;
      }

      for (int var1 = 0; var1 < actorCount; var1++) {
         if (actors[var1] != null && actors[var1].uid == var0) {
            return actors[var1];
         }
      }

      if (bm != null && var0 == bm.uid) {
         return bm;
      } else {
         return bn != null && var0 == bn.uid ? bn : null;
      }
   }

   // $VF: renamed from: e (b) void
   public static final void addCinematicActor(Actor var0) {
      if (bC >= bA) {
         System.out.print("s_cine_actors_num exceed maxnum" + bA);
      } else {
         bB[bC++] = var0;
      }
   }

   // $VF: renamed from: m () void
   public static final void clearCinematicActors() {
      for (int var0 = 0; var0 < bC; var0++) {
         bB[var0] = null;
      }

      bC = 0;
   }

   // $VF: renamed from: h (int) b
   public static final Actor findCinematicActor(int var0) {
      for (int var1 = 0; var1 < bC; var1++) {
         if (bB[var1] != null && bB[var1].uid == var0) {
            return bB[var1];
         }
      }

      return null;
   }

   // $VF: renamed from: a (b, int) void
   public static final void storeLinkedActorSnapshot(Actor var0, int var1) {
      Actor.by[var1 * 18] = (byte)var0.animationId;
      Actor.by[var1 * 18 + 1] = (byte)var0.animationFrame;
      writeI16LE(Actor.by, var1 * 18 + 2, (short)var0.positionX);
      writeI16LE(Actor.by, var1 * 18 + 4, (short)var0.positionY);
      writeI32LE(Actor.by, var1 * 18 + 6, var0.actionState);
      writeI32LE(Actor.by, var1 * 18 + 10, var0.flags);
      writeI32LE(Actor.by, var1 * 18 + 14, var0.aw);
   }

   // $VF: renamed from: b (b, int) void
   public static final void restoreLinkedActorSnapshot(Actor var0, int var1) {
      var0.animationId = Actor.by[var1 * 18];
      var0.animationFrame = Actor.by[var1 * 18 + 1];
      var0.positionX = readI16LE(Actor.by, var1 * 18 + 2);
      var0.positionY = readI16LE(Actor.by, var1 * 18 + 4);
      var0.actionState = readI32LE(Actor.by, var1 * 18 + 6);
      var0.flags = readI32LE(Actor.by, var1 * 18 + 10);
      var0.aw = readI32LE(Actor.by, var1 * 18 + 14);
   }

   // $VF: renamed from: I (int) boolean
   private static boolean staysRemovedOnRestart(int var0) {
      return var0 == 35 || var0 == 23 || var0 == 48 || var0 == 82;
   }

   // $VF: renamed from: c (boolean) void
   private static void constructLevelActors(boolean var0) {
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
            Actor var12 = null;
            short var7 = bE[0];
            int var8 = 0;
            if (var7 != 0) {
               var8 = var10++;
               if (!var0) {
                  (var12 = new Actor(bE, var4, var1)).persistenceIndex = var8;
               } else {
                  if (bD[var10 - 1] == -99 && staysRemovedOnRestart(var7)) {
                     continue;
                  }

                  bD[var10 - 1] = 0;
                  (var12 = new Actor(bE, var4, var1)).persistenceIndex = var8;
               }
            }

            if (var7 == 0) {
               if (player == null) {
                  player = new Actor(bE, var4, var1);
               }
            } else if (var7 == 69) {
               if (bm == null) {
                  bm = new Actor(bE, var4, var1);
               } else if (bn == null) {
                  bn = new Actor(bE, var4, var1);
               }
            } else {
               addActor(var12);
               if (var12.type == 28 && bo == null) {
                  bo = var12;
               }

               actors[actorCount - 1].persistenceIndex = var10 - 1;
               var12.finalizeRecordCollision(bE);
            }
         }
      }
   }

   // $VF: renamed from: n () void
   public static final void clearActors() {
      for (int var0 = 0; var0 < actorCount; var0++) {
         if (actors[var0] != null) {
            actors[var0].releaseLinksAndBounds();
            actors[var0] = null;
         }
      }

      clearActorLists();
      l();
      Actor.aO = null;
      Actor.aP = null;
      actorCount = 0;
      bz = 0;
      player = null;
      bm = null;
      bn = null;
      bl = null;
      au = null;
      bo = null;
      Actor.cx = null;
      Actor.aM = null;
      Actor.aR = null;
      activeCameraControllerUid = -1;
      cameraControllerActive = false;
      i();
   }

   private static void af() throws Exception {
      openPack("/1");
      font = loadSprite(0);
      font.registerImageCache(2);
      BSprite.createImageCache(2, 30);
      closePack();
   }

   private static void ag() throws Exception {
      openPack("/2");
      sprites[35] = loadSprite(35);
      sprites[33] = loadSprite(33);
      BSprite.registeredCacheSprites[0] = sprites[33];
      BSprite.registeredCacheSprites[0].registerImageCache(0);
      sprites[35].registerImageCache(3);
      BSprite.createImageCache(3, 10);
      closePack();
   }

   // $VF: renamed from: J (int) void
   private static void loadObjectsAndScripts(int var0) throws Exception {
      openPack(levelPackPaths[var0]);
      byte[] var1 = readResource(0);
      byte[] var2 = readResource(1);
      closePack();
      bF = var1;
      decodeScripts(var2);
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
            if (var1[var2] == levelIndex) {
               return true;
            }
         }
      }

      return false;
   }

   // $VF: renamed from: ah () void
   private static void collectRequiredSprites() {
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

         if (var1[0] < Actor.spriteIdByType.length && Actor.spriteIdByType[var1[0]] != -1) {
            dJ[Actor.spriteIdByType[var1[0]]] = 1;
         }
      }

      requireSprite(1);
      requireSprite(33);
      requireSprite(-1);
      requireSprite(18);
      requireSprite(40);
      requireSprite(-1);
      requireSprite(40);
      if (dJ[Actor.spriteIdByType[73]] == 1) {
         requireSprite(30);
      }

      if (dJ[Actor.spriteIdByType[13]] == 1) {
         requireSprite(13);
      }

      if (dJ[Actor.spriteIdByType[90]] == 1) {
         requireSprite(16);
      }

      if (dJ[Actor.spriteIdByType[90]] == 1) {
         requireSprite(17);
      }

      if (i(27)) {
         requireSprite(27);
      }

      if (i(4)) {
         requireSprite(4);
      }

      if (i(28)) {
         requireSprite(28);
      }

      if (i(41)) {
         requireSprite(41);
      }

      if (i(48)) {
         requireSprite(48);
         requireSprite(30);
      }
   }

   // $VF: renamed from: j (int) void
   public static final void requireSprite(int var0) {
      if (var0 >= 0) {
         dJ[var0] = 1;
      }
   }

   // $VF: renamed from: K (int) boolean
   private static boolean loadLevelStep(int var0) {
      try {
         if (var0 == 1) {
            levelCompleted = false;
            loadStringTable(2 + levelIndex);
            if (levelIndex != 6 && levelIndex == 9) {
            }
         } else if (var0 == 3) {
            loadSoundData(levelIndex);
            ensureTileBuffer();
         } else if (var0 < 14) {
            int var1;
            j(var1 = var0 - 4, levelIndex);
         } else if (var0 < 15) {
            loadTileMaps(levelIndex, true);
         } else if (var0 == 15) {
            loadObjectsAndScripts(levelIndex);
         } else if (var0 == 17) {
            openPack("/2");
            collectRequiredSprites();
         } else if (var0 < 71 && var0 > 17) {
            int var5 = var0 - 18;
            if (dJ[var5] == 1) {
               sprites[var5] = loadSprite(var5);
               if (var5 == 0 || var5 == 25) {
                  try {
                     sprites[var5].buildImages(0, 0, -1, -1);
                     sprites[var5].releaseImageData();
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

                  BSprite.registeredCacheSprites[var2] = sprites[var5];
                  BSprite.registeredCacheSprites[var2].registerImageCache(0);
               }
            }
         } else if (var0 == 71) {
            BSprite.createImageCache(0, 40);
            System.gc();
            closePack();
         } else if (var0 != 76) {
            if (var0 == 77) {
               System.gc();
            } else if (var0 != 78) {
               if (var0 == 79) {
                  restoreActorPersistence();
                  if (continueFromCheckpoint) {
                     reconstructLevelActors(true);
                     restoreCheckpoint();
                     refreshCameraRegionSelection();
                     continueFromCheckpoint = false;
                  } else {
                     constructLevelActors(false);
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

   // $VF: renamed from: ai () void
   private static void unloadLevel() {
      clearActors();
      bF = null;
      cP = false;
      Actor.resetWorldActorState();
      releaseScripts();
      ad();
      releaseTileMaps();
      dl = null;
      dm = null;

      for (int var0 = 0; var0 < 53; var0++) {
         if (var0 != 35 && var0 != 33) {
            sprites[var0] = null;
         }
      }

      for (int var1 = 2; var1 < 11; var1++) {
         releaseStringTable(var1);
      }

      for (int var2 = 0; var2 < 23; var2++) {
         BSprite.registeredCacheSprites[var2] = null;
      }

      BSprite.destroyImageCache(0);
      BSprite.destroyImageCache(1);
      System.gc();
   }

   // $VF: renamed from: o () void
   public static final void clearActorLists() {
      for (int var0 = 0; var0 < bq; var0++) {
         if (paintActors[var0] != null) {
            paintActors[var0].sprite = null;
            paintActors[var0] = null;
         }
      }

      paintActorCount = 0;
      updateActorCount = 0;

      for (int var1 = 0; var1 < br; var1++) {
         if (updateActors[var1] != null) {
            updateActors[var1].sprite = null;
            updateActors[var1] = null;
         }
      }

      bC = 0;

      for (int var2 = 0; var2 < bA; var2++) {
         if (bB[var2] != null) {
            bB[var2].sprite = null;
            bB[var2] = null;
         }
      }
   }

   // $VF: renamed from: p () void
   public static final void restoreActorPersistence() {
      for (int var0 = 0; var0 < 9; var0++) {
         bJ[var0] = readI16LE(saveBuffer, 86 + 2 * var0);
      }

      bI = bJ[levelIndex];

      for (int var2 = 0; var2 < 9; var2++) {
         for (int var1 = 0; var1 < 30; var1++) {
            bG[var2][var1] = readI16LE(saveBuffer, 104 + var2 * 30 * 2 + var1 * 2);
         }
      }

      bH = bG[levelIndex];

      for (int var3 = 0; var3 < bp; var3++) {
         bD[var3] = readSignedByte(saveBuffer, 999 + var3);
      }
   }

   // $VF: renamed from: aj () void
   private static void resetPlayerMeters() {
      health = 60;
      maxHealth = 60;
      magic = 60;
      maxMagic = 60;
      r = 0;
      u = 0;
      v = 0;
   }

   // $VF: renamed from: ak () void
   private static void restoreCheckpoint() {
      if (levelIndex == 3) {
         Actor.bC = readSignedByte(saveBuffer, 644) == 1;
         if (bm != null && Actor.bC) {
            bn.positionX = bm.positionX = readI16LE(saveBuffer, 645);
            bn.positionY = bm.positionY = readI16LE(saveBuffer, 647);
            bn.actionState = bm.actionState = readI32LE(saveBuffer, 649);
         }
      }

      if (player != null && !skipCheckpointPosition) {
         player.positionX = readI16LE(saveBuffer, 18);
         player.positionY = readI16LE(saveBuffer, 20);
         player.facingLeft = readI16LE(saveBuffer, 22) == 1;
         levelCompleted = readI16LE(saveBuffer, 24) == 1;
         if (levelIndex == 5) {
            player.positionY -= 20;
         }
      }

      restoreProgress();
   }

   // $VF: renamed from: q () void
   public static final void restoreProgress() {
      Actor.activeWeapon = readI16LE(saveBuffer, 26);

      for (int var0 = 0; var0 < 2; var0++) {
         Actor.bM[var0] = readI16LE(saveBuffer, 28 + 2 * var0);
         Actor.bN[var0] = readI16LE(saveBuffer, 32 + 2 * var0);
         Actor.bO[var0] = readI16LE(saveBuffer, 36 + 2 * var0);
         Actor.bP[var0] = readI16LE(saveBuffer, 40 + 2 * var0);
      }

      maxHealth = readI16LE(saveBuffer, 60);
      health = readI16LE(saveBuffer, 62);
      r = readI16LE(saveBuffer, 64);
      maxMagic = readI16LE(saveBuffer, 66);
      magic = readI16LE(saveBuffer, 68);
      u = readI16LE(saveBuffer, 70);
      v = readI32LE(saveBuffer, 72);
      Actor.bl = readSignedByte(saveBuffer, 653) == 1;
      Actor.bm = readSignedByte(saveBuffer, 654) == 1;
      Actor.bn = readSignedByte(saveBuffer, 655) == 1;
      Actor.bo = readSignedByte(saveBuffer, 656) == 1;
      Actor.bq = readSignedByte(saveBuffer, 657) == 1;
      Actor.bp = readSignedByte(saveBuffer, 658) == 1;
      Actor.br = readSignedByte(saveBuffer, 659) == 1;
      Actor.bs = readSignedByte(saveBuffer, 660) == 1;
      Actor.bt = readSignedByte(saveBuffer, 661) == 1;
      Actor.bu = readSignedByte(saveBuffer, 662) == 1;
      Actor.bw = readSignedByte(saveBuffer, 663) == 1;
   }

   // $VF: renamed from: d (boolean) void
   private static void reconstructLevelActors(boolean var0) {
      if (levelIndex == 5 && var0) {
         for (int var1 = 0; var1 < 3; var1++) {
            Actor.bx[var1] = readI16LE(saveBuffer, 664 + var1 * 2);
         }

         for (int var6 = 0; var6 < 54; var6++) {
            Actor.by[var6] = readSignedByte(saveBuffer, 677 + var6);
         }
      }

      if (continueFromCheckpoint) {
         constructLevelActors(false);
      } else {
         constructLevelActors(var0);
      }

      if (levelIndex == 5 && var0) {
         activeCameraControllerUid = readI16LE(saveBuffer, 731);

         for (int var2 = 0; var2 < actorCount; var2++) {
            Actor var7 = actors[var2];

            for (int var3 = 0; var3 < 3; var3++) {
               if (Actor.bx[var3] != -1 && Actor.bx[var3] == var7.uid) {
                  Actor.cq[var3] = var7;
                  restoreLinkedActorSnapshot(Actor.cq[var3], var3);
               }
            }

            if (activeCameraControllerUid != -1) {
               if (activeCameraControllerUid == var7.uid) {
                  var7.flags |= 16;
                  var7.flags &= -33;
               }

               if (var7.aB == activeCameraControllerUid) {
                  short var9 = readI16LE(saveBuffer, 733);
                  short var4 = readI16LE(saveBuffer, 735);
                  int var5 = var9 + var4;
                  viewBounds[1] = cameraTargetY = cameraY = var9 - 2;
                  var7.ar = var7.positionY - var5;
                  var7.positionY = var5;
                  viewBounds[3] = cameraY + 320;
                  var7.flags |= 16;
                  var7.flags &= -33;
                  cameraControllerMode = 4;
               }
            }
         }

         for (int var8 = 0; var8 < 3; var8++) {
            if (Actor.bx[var8] == -1) {
               Actor.cq[var8] = null;
            }
         }
      }
   }

   // $VF: renamed from: al () void
   private static void restartLevel() {
      clearActors();
      Actor.resetWorldActorState();
      restoreActorPersistence();
      reconstructLevelActors(true);
      restoreCheckpoint();
      health = maxHealth;
      resetScripts();
      initializeCamera();
      aN = 0;
      aM = 0;

      for (int var0 = 0; var0 < BSprite.imageCaches[0].length; var0++) {
         BSprite.imageCaches[0][var0] = null;
      }

      changeState(8);
      k = true;
   }

   // $VF: renamed from: L (int) byte[]
   private static byte[] loadMapCellsAndDimensions(int var0) throws Exception {
      byte[] var1 = readResource(var0);
      selectResource(var0 + 1);
      mapWidthTiles = readPackU16LE();
      mapHeightTiles = readPackU16LE();
      return var1;
   }

   // $VF: renamed from: M (int) byte[]
   private static byte[] loadMapTransforms(int var0) throws Exception {
      return readResource(var0);
   }

   // $VF: renamed from: b (int, boolean) void
   private static void loadTileMaps(int var0, boolean var1) throws Exception {
      if (var0 == 11) {
         openPack(levelPackPaths[var0 - 2]);
      } else if (var0 == 12) {
         openPack(levelPackPaths[var0 - 1]);
      } else if (var0 == 13) {
         openPack(levelPackPaths[var0 - 4]);
      } else {
         openPack(levelPackPaths[var0]);
      }

      if (var1) {
         collisionTiles = loadMapCellsAndDimensions(2);
      }

      tileLayer0 = loadMapCellsAndDimensions(var1 ? 5 : 0);
      tileLayer0Transforms = loadMapTransforms(var1 ? 7 : 2);
      tileLayer1 = loadMapCellsAndDimensions(var1 ? 8 : 3);
      tileLayer1Transforms = loadMapTransforms(var1 ? 10 : 5);
      bV = mapWidthTiles;
      bU = mapWidthTiles;
      bW = mapHeightTiles;
      levelPixelWidth = mapWidthTiles * 20;
      levelPixelHeight = mapHeightTiles * 20;
      closePack();
      if (var1) {
         for (int var2 = 0; var2 < collisionTiles.length; var2++) {
            if (collisionTiles[var2] == -1) {
               collisionTiles[var2] = 0;
            }
         }
      }

      aA = -1;
   }

   // $VF: renamed from: am () void
   private static void releaseTileMaps() {
      tileLayer0 = null;
      tileLayer0Transforms = null;
      tileLayer1 = null;
      tileLayer1Transforms = null;
      collisionTiles = null;
   }

   // $VF: renamed from: d (int, int) int
   public static final int getCollisionTile(int var0, int var1) {
      if (var0 >= 0 && var0 < mapWidthTiles && var1 < mapHeightTiles) {
         if (levelModes[levelIndex] == 3) {
            if (var1 < 0) {
               return 0;
            }
         } else if (var1 < 0) {
            return 24;
         }

         return collisionTiles[var0 + var1 * mapWidthTiles];
      } else {
         return levelModes[levelIndex] == 2 ? 0 : 24;
      }
   }

   // $VF: renamed from: c (int, int, int) void
   public static final void setCollisionTile(int var0, int var1, int var2) {
      if (var0 >= 0 && var1 >= 0 && var0 < mapWidthTiles && var1 < mapHeightTiles) {
         collisionTiles[var0 + var1 * mapWidthTiles] = (byte)var2;
      }
   }

   private static void an() throws Exception {
      openPack("/17");
      selectResource(0);
      trigTable256 = new int[256];

      for (int var0 = 0; var0 < 256; var0++) {
         trigTable256[var0] = readPackI32LE();
      }

      closePack();
   }

   private static void ao() throws Exception {
      openPack("/18");
      byte[] var3 = readResource(0);
      closePack();
      ByteArrayInputStream var4 = new ByteArrayInputStream(var3);
      DataInputStream var5;
      actorStatTables = new int[(var5 = new DataInputStream(var4)).readInt()][];

      for (int var0 = 0; var0 < actorStatTables.length; var0++) {
         int var2 = var5.readInt();
         actorStatTables[var0] = new int[var2];

         for (int var1 = 0; var1 < var2; var1++) {
            actorStatTables[var0][var1] = var5.readInt();
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

   // $VF: renamed from: k (int) int
   public static final int cos256(int var0) {
      while (var0 < 0) {
         var0 += 360;
      }

      return trigTable256[var0 % 360 * 256 / 360];
   }

   // $VF: renamed from: l (int) int
   public static final int sin256(int var0) {
      return cos256(90 - var0);
   }

   // $VF: renamed from: f (int, int) int
   public static final int randomRange(int var0, int var1) {
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

   // $VF: renamed from: g (int, int) int
   public static final int approximateDistance(int var0, int var1) {
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

   // $VF: renamed from: N (int) d
   private static BSprite loadSprite(int var0) throws Exception {
      BSprite var1 = new BSprite();
      byte[] var2 = readResource(var0);
      var1.load(var2, 0);
      return var1;
   }

   // $VF: renamed from: m (int) d
   public static final BSprite getSprite(int var0) {
      return sprites[var0];
   }

   // $VF: renamed from: O (int) void
   private static void loadStringTable(int var0) throws Exception {
      openPack("/15");
      byte[] var1 = readResource(var0);
      closePack();
      decodeStringTable(var0, var1);
      String var2 = stringTables[0][29];

      for (int var3 = 0; var3 < bZ.length; var3++) {
         int var4 = bZ[var3];
         stringTables[0][var4] = replaceToken(stringTables[0][var4], "#SK", var2);
      }

      stringTables[0][29] = null;
   }

   // $VF: renamed from: P (int) void
   private static void releaseStringTable(int var0) {
      clearStringTable(var0);
   }

   // $VF: renamed from: a (int, byte[]) void
   static final void decodeStringTable(int var0, byte[] var1) {
      if (var1 != null) {
         try {
            ByteArrayInputStream var2 = new ByteArrayInputStream(var1);
            DataInputStream var3;
            (var3 = new DataInputStream(var2)).skip(6L);
            short var4 = var3.readShort();
            stringTables[var0] = new String[var4];

            for (int var5 = 0; var5 < var4; var5++) {
               stringTables[var0][var5] = var3.readUTF().toUpperCase().trim();
            }

            var3.close();
            var2.close();
         } catch (Exception var6) {
         }
      }
   }

   // $VF: renamed from: n (int) void
   static final void clearStringTable(int var0) {
      stringTables[var0] = null;
   }

   // $VF: renamed from: h (int, int) java.lang.String
   static final String getString(int var0, int var1) {
      return stringTables[var0][var1];
   }

   // $VF: renamed from: a (byte[], int, int) int
   static final int writeByte(byte[] var0, int var1, int var2) {
      var0[var1++] = (byte)var2;
      return var1;
   }

   // $VF: renamed from: a (byte[], int, short) int
   static final int writeI16LE(byte[] var0, int var1, short var2) {
      var0[var1++] = (byte)(var2 & 0xFF);
      var0[var1++] = (byte)(var2 >>> 8 & 0xFF);
      return var1;
   }

   // $VF: renamed from: b (byte[], int, int) int
   static final int writeI32LE(byte[] var0, int var1, int var2) {
      var0[var1++] = (byte)(var2 & 0xFF);
      var0[var1++] = (byte)(var2 >>> 8 & 0xFF);
      var0[var1++] = (byte)(var2 >>> 16 & 0xFF);
      var0[var1++] = (byte)(var2 >>> 24 & 0xFF);
      return var1;
   }

   // $VF: renamed from: a (byte[], int) byte
   static final byte readSignedByte(byte[] var0, int var1) {
      return var0[var1];
   }

   // $VF: renamed from: b (byte[], int) short
   static final short readI16LE(byte[] var0, int var1) {
      return (short)(var0[var1++] & 0xFF | (var0[var1] & 255) << 8);
   }

   // $VF: renamed from: c (byte[], int) int
   static final int readI32LE(byte[] var0, int var1) {
      return var0[var1++] & 0xFF | (var0[var1++] & 0xFF) << 8 | (var0[var1++] & 0xFF) << 16 | (var0[var1] & 0xFF) << 24;
   }

   // $VF: renamed from: a (java.lang.String) void
   static final void openPack(String var0) throws Exception {
      if (packPath == null || var0.compareTo(packPath) != 0) {
         if (packStream != null) {
            closePack();
         }

         packPath = var0;
         packStream = packPath.getClass().getResourceAsStream(packPath);
         packResourceCount = (short)readPackU16LE();
         packPartCount = (short)readPackU16LE();
         packPartFirstResource = new short[packPartCount];

         for (int var1 = 0; var1 < packPartCount; var1++) {
            packPartFirstResource[var1] = (short)readPackU16LE();
         }

         packPartIndex = 0;
         readPackOffsetTable();
      }
   }

   // $VF: renamed from: ap () void
   private static void readPackOffsetTable() throws Exception {
      int var0;
      if (packPartIndex == packPartCount - 1) {
         var0 = packResourceCount - packPartFirstResource[packPartIndex];
      } else {
         var0 = packPartFirstResource[packPartIndex + 1] - packPartFirstResource[packPartIndex];
      }

      packOffsets = new int[var0 + 1];

      for (int var1 = 0; var1 < var0 + 1; var1++) {
         packOffsets[var1] = readPackI32LE();
      }
   }

   // $VF: renamed from: r () void
   static final void closePack() throws Exception {
      if (packStream != null) {
         packStream.close();
         packStream = null;
      }

      packPosition = 0;
   }

   // $VF: renamed from: o (int) int
   static final int selectResource(int var0) throws Exception {
      int var1 = packPartCount - 1;

      while (var1 >= 0 && packPartFirstResource[var1] > var0) {
         var1--;
      }

      if (packPartIndex != var1) {
         packPartIndex = var1;
         closePack();
         if (packPartIndex == 0) {
            packStream = packPath.getClass().getResourceAsStream(packPath);
            readPackU16LE();
            readPackU16LE();

            for (int var2 = 0; var2 < packPartCount; var2++) {
               readPackU16LE();
            }
         } else {
            packStream = packPath.getClass().getResourceAsStream(packPath + "." + packPartIndex);
         }

         readPackOffsetTable();
      } else if (packStream == null) {
         if (packPartIndex == 0) {
            packStream = packPath.getClass().getResourceAsStream(packPath);
         } else {
            packStream = packPath.getClass().getResourceAsStream(packPath + "." + packPartIndex);
         }
      }

      var0 -= packPartFirstResource[packPartIndex];
      int var5 = packOffsets[var0];
      int var3 = packOffsets[var0 + 1] - packOffsets[var0] - 1;
      seekPack(var5);
      readPackByte();
      return var3;
   }

   // $VF: renamed from: p (int) byte[]
   static final byte[] readResource(int var0) throws Exception {
      int var1;
      if ((var1 = selectResource(var0)) < 0) {
         return null;
      }

      byte[] var2;
      readPackFully(var2 = new byte[var1], 0, var2.length);
      return var2;
   }

   // $VF: renamed from: q (int) void
   static final void seekPack(int var0) throws Exception {
      if (packPosition != var0) {
         if (packPosition > var0) {
            closePack();
            if (packPartIndex == 0) {
               packStream = packPath.getClass().getResourceAsStream(packPath);
            } else {
               packStream = packPath.getClass().getResourceAsStream(packPath + "." + packPartIndex);
            }
         } else {
            var0 -= packPosition;
         }

         skipPackBytes(var0);
      }
   }

   // $VF: renamed from: r (int) void
   static final void skipPackBytes(int var0) throws Exception {
      if (var0 != 0) {
         if (packSkipBuffer == null) {
            packSkipBuffer = new byte[256];
         }

         while (var0 > 256) {
            readPackFully(packSkipBuffer, 0, 256);
            var0 -= 256;
         }

         if (var0 > 0) {
            readPackFully(packSkipBuffer, 0, var0);
         }
      }
   }

   // $VF: renamed from: s () int
   static final int readPackByte() throws Exception {
      int var0;
      if ((var0 = packStream.read()) >= 0) {
         packPosition++;
      }

      return var0;
   }

   // $VF: renamed from: t () int
   static final int readPackU16LE() throws Exception {
      return readPackByte() & 0xFF | (readPackByte() & 0xFF) << 8;
   }

   // $VF: renamed from: u () int
   static final int readPackI32LE() throws Exception {
      return readPackByte() & 0xFF | (readPackByte() & 0xFF) << 8 | (readPackByte() & 0xFF) << 16 | (readPackByte() & 0xFF) << 24;
   }

   // $VF: renamed from: c (byte[], int, int) int
   static final int readPackFully(byte[] var0, int var1, int var2) throws Exception {
      int var3 = var1;
      int var4 = var2;
      int var5 = 0;

      while (var4 > 0) {
         var5 = packStream.read(var0, var3, var4);
         var4 -= var5;
         var3 += var5;
      }

      packPosition += var2;
      return var2;
   }

   // $VF: renamed from: aq () void
   private void updateMainMenu() {
      updateCheatSequence();
      setClip(0, 0, 240, 320);
      if (titleImage == null) {
         a(0, true);
      }

      drawImage(titleImage, 0, 0, 0);
      a(23, 110, 194, 177, 217);
      if (cheatsUnlocked) {
         font.currentPalette = 0;
         font.drawText(graphics, "is GOD MODULE!", 237, 20, 9);
      }

      if (cheatsUnlocked) {
         graphics.setColor(16777215);
         graphics.drawString("level :" + levelIndex, 0, 10, 0);
         if (wasKeyPressed(1)) {
            levelIndex++;
         }

         if (levelIndex > 8) {
            levelIndex = 0;
         }

         if (wasKeyPressed(2048)) {
            titleImage = null;
            changeState(9);
         }
      }

      font.currentPalette = 0;
      drawMenuItems(menuDefinition, 120, 125 + countHiddenMenuItems() * 9);
      font.currentPalette = 0;
      if (confirmationOpen) {
         drawConfirmation();
      } else {
         navigateMenu(menuItems[menuDefinition].length, false);
      }

      handleMenuAction(menuDefinition);
   }

   // $VF: renamed from: l (int, int) java.lang.String
   private static String getMenuItemText(int var0, int var1) {
      return getString(0, menuItems[var0][var1]);
   }

   // $VF: renamed from: ar () int
   private static int countHiddenMenuItems() {
      int var0 = 0;
      if (menuDefinition == 0) {
         var0 += 2;
      }

      if (!InGamePromotion.a()) {
         var0++;
      }

      if (!InGamePromotion.d()) {
         var0++;
      }

      return var0;
   }

   // $VF: renamed from: e (int, int, int) void
   private static void drawMenuItems(int var0, int var1, int var2) {
      int var3 = 0;

      for (int var4 = 0; var4 < menuItems[var0].length; var4++) {
         if (var0 != 0 && var0 != 2
            || (!InGamePromotion.a() ? menuItems[var0][var4] != 4 && menuItems[var0][var4] != 5 : InGamePromotion.d() || menuItems[var0][var4] != 5)) {
            int var5 = var1;
            String var6 = getMenuItemText(var0, var4);
            if ((var0 == 0 || var0 == 2) && InGamePromotion.a() && menuItems[var0][var4] == 4) {
               font.currentPalette = selectedMenuItem == var4 ? 2 : 1;
            } else if ((var0 == 0 || var0 == 2) && InGamePromotion.a() && InGamePromotion.d() && menuItems[var0][var4] == 5) {
               font.currentPalette = selectedMenuItem == var4 ? 2 : 1;
            } else if (var0 == 1) {
               font.currentPalette = selectedMenuItem == var4 ? 1 : 0;
            } else {
               font.currentPalette = selectedMenuItem == var4 ? 2 : 0;
            }

            if (menuItems[var0][var4] == 6) {
               font.drawText(graphics, var6 + ": " + (soundEnabled ? getString(0, 23) : getString(0, 24)), var5, 18 * var3 + var2, 3);
            } else {
               font.drawText(graphics, var6, var5, 18 * var3 + var2, 3);
            }

            if (menuItems[var0][var4] == 4) {
               InGamePromotion.a(graphics, menuNewBadge, var5 + BSprite.textWidth / 2 + 0, 18 * var3 + var2, 6);
            } else if (menuItems[var0][var4] == 5) {
               InGamePromotion.b(graphics, menuNewBadge, var5 + BSprite.textWidth / 2 + 0, 18 * var3 + var2, 6);
            }

            if (selectedMenuItem == var4) {
               sprites[35].drawAnimationFrame(graphics, 5, stateFrame % 6 >> 1, state == 14 ? 60 : 45, var2 + var3 * 18, 1, 0, 0);
            }

            var3++;
         }
      }

      drawSoftkeys(var0 == 1 ? getString(0, 18) : "", getString(0, 21));
   }

   // $VF: renamed from: c (int, boolean) void
   private static void navigateMenu(int var0, boolean var1) {
      if (var0 > 0) {
         if (wasKeyPressed(var1 ? 4112 : 16388)) {
            playSoundOnce(-1);
            selectedMenuItem += var0 - 1;
            selectedMenuItem %= var0;
            if ((menuDefinition == 0 || menuDefinition == 2) && menuItems[menuDefinition][selectedMenuItem] == 5) {
               if (!InGamePromotion.a()) {
                  selectedMenuItem += var0 - 2;
                  selectedMenuItem %= var0;
                  return;
               }

               if (InGamePromotion.a() && !InGamePromotion.d()) {
                  selectedMenuItem += var0 - 1;
                  selectedMenuItem %= var0;
                  return;
               }
            }
         } else if (wasKeyPressed(var1 ? 8256 : 33024)) {
            playSoundOnce(-1);
            selectedMenuItem++;
            selectedMenuItem %= var0;
            if (menuDefinition == 0 || menuDefinition == 2) {
               if (menuItems[menuDefinition][selectedMenuItem] == 4) {
                  if (!InGamePromotion.a()) {
                     selectedMenuItem += 2;
                     selectedMenuItem %= var0;
                     return;
                  }
               } else if (menuItems[menuDefinition][selectedMenuItem] == 5 && InGamePromotion.a() && !InGamePromotion.d()) {
                  selectedMenuItem++;
                  selectedMenuItem %= var0;
               }
            }
         }
      }
   }

   // $VF: renamed from: a (byte[]) void
   public static final void decodeScripts(byte[] var0) {
      int var19 = 0;
      var19++;
      short var2;
      scriptTracks = new byte[var2 = var0[0]][][];
      scriptIds = new short[var2];
      scriptTrackOffsets = new int[var2][];

      for (int var3 = 0; var3 < var2; var3++) {
         scriptIds[var3] = (short)((var0[var19] & 255) + ((var0[var19 + 1] & 255) << 8));
         var19 += 2;
         byte var4 = var0[var19++];
         var19 += 2;
         scriptTracks[var3] = new byte[var4][];
         scriptTrackOffsets[var3] = new int[var4];

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
                     var19 += getScriptOpcodeSize(var13);
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
            scriptTracks[var3][var5] = new byte[var21 + 2];
            scriptTrackOffsets[var3][var5] = var9 - var6;
            System.arraycopy(var0, var6, scriptTracks[var3][var5], 0, var21);
            scriptTracks[var3][var5][var21] = (byte)(scriptTrackOffsets[var3][var5] & 0xFF);
            scriptTracks[var3][var5][var21 + 1] = (byte)(scriptTrackOffsets[var3][var5] >> 8 & 0xFF);
         }
      }

      scriptTicks = new short[var2];
      resetScripts();
   }

   // $VF: renamed from: v () void
   public static final void resetScripts() {
      scriptsPaused = false;

      for (int var0 = 0; var0 < scriptTicks.length; var0++) {
         scriptTicks[var0] = -1;
      }

      for (int var4 = 0; var4 < scriptIds.length; var4++) {
         byte[][] var1 = scriptTracks[var4];

         for (int var2 = 0; var2 < var1.length; var2++) {
            byte[] var3 = var1[var2];
            scriptTrackOffsets[var4][var2] = (var3[var3.length - 2] & 255) + (var3[var3.length - 1] << 8 & 0xFF);
         }
      }
   }

   // $VF: renamed from: w () void
   public static final void releaseScripts() {
      scriptTracks = (byte[][][])null;
      scriptIds = null;
      scriptTicks = null;
      scriptTrackOffsets = (int[][])null;
   }

   // $VF: renamed from: s (int) void
   public static final void startCutscene(int var0) {
      cm = false;
      Z = true;
      scriptTicks[var0] = 0;
      aM = 35;
      clearInput();
      Actor.weaponSwitchCooldownMs = 0;
      Actor.clearCombatEffects(true);
   }

   // $VF: renamed from: t (int) void
   public static final void startScript(int var0) {
      scriptTicks[var0] = 0;
   }

   // $VF: renamed from: u (int) void
   public static final void stopAndRewindScript(int var0) {
      scriptTicks[var0] = -2;
      byte[][] var1 = scriptTracks[var0];

      for (int var2 = 0; var2 < var1.length; var2++) {
         byte[] var3 = var1[var2];
         scriptTrackOffsets[var0][var2] = (var3[var3.length - 2] & 255) + (var3[var3.length - 1] << 8 & 0xFF);
      }
   }

   // $VF: renamed from: v (int) void
   public static final void restartScript(int var0) {
      scriptTicks[var0] = 0;
      byte[][] var1 = scriptTracks[var0];

      for (int var2 = 0; var2 < var1.length; var2++) {
         byte[] var3 = var1[var2];
         scriptTrackOffsets[var0][var2] = (var3[var3.length - 2] & 255) + (var3[var3.length - 1] << 8 & 0xFF);
      }
   }

   // $VF: renamed from: w (int) void
   public static final void finishCutscene(int var0) {
      cm = true;
      Z = false;
      scriptTicks[var0] = -2;
      if (state != 15) {
         aM = 0;
      }

      if (!isSelectedScript(var0)) {
         ar = 1;
      }

      if (levelIndex != 9 && levelIndex != 3) {
         as = 1;
      }

      ch = true;
      ci = false;
      clearCinematicActors();
      if (bc != 17 && bc != 18) {
         l();
      }

      aO = false;
   }

   // $VF: renamed from: x (int) int
   public static final int findScriptIndex(int var0) {
      for (int var1 = 0; var1 < scriptIds.length; var1++) {
         if (scriptIds[var1] == var0) {
            return var1;
         }
      }

      return -1;
   }

   // $VF: renamed from: x () void
   public static final void updateBackgroundScripts() {
      if (wasKeyPressed(131072) && ch) {
         cg = true;
         scriptsPaused = false;
         clearInput();
      }

      if (!scriptsPaused) {
         for (int var0 = 0; var0 < scriptIds.length; var0++) {
            boolean var1 = false;

            for (int var2 = 0; var2 < cj; var2++) {
               if (var0 == ck[var2]) {
                  var1 = true;
                  break;
               }
            }

            if (!var1) {
               updateScript(var0, var1, 1);
            }
         }
      }
   }

   // $VF: renamed from: y () void
   public static final void updateSelectedScripts() {
      for (int var0 = 0; var0 < cj; var0++) {
         updateScript(ck[var0], true, cl[var0]);
      }
   }

   // $VF: renamed from: y (int) boolean
   public static final boolean isSelectedScript(int var0) {
      boolean var1 = false;

      for (int var2 = 0; var2 < cj; var2++) {
         if (var0 == ck[var2]) {
            var1 = true;
            break;
         }
      }

      return var1;
   }

   // $VF: renamed from: z () boolean
   public static final boolean hasActiveBackgroundScript() {
      if (scriptTicks != null) {
         for (int var0 = 0; var0 < scriptTicks.length; var0++) {
            if (scriptTicks[var0] >= 0) {
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

   // $VF: renamed from: b (int, boolean, int) void
   public static final void updateScript(int var0, boolean var1, int var2) {
      Actor var6 = null;
      if (scriptTicks[var0] >= 0) {
         boolean var7 = true;
         byte[][] var8 = scriptTracks[var0];
         short var9 = scriptTicks[var0]++;

         for (int var10 = 0; var10 < var8.length; var10++) {
            Actor var11 = null;
            int var13 = 0;
            byte[] var14 = var8[var10];
            if (scriptTrackOffsets[var0][var10] < var14.length - 2) {
               byte var15 = var14[0];
               boolean var16 = false;
               switch (var15) {
                  case 0:
                  case 1:
                  default:
                     break;
                  case 2:
                     var11 = findActorByUid((var14[2] & 255) + ((var14[3] & 255) << 8));
                     break;
                  case 3:
                     int var12;
                     short var17;
                     if ((var11 = findCinematicActor(var12 = ((var17 = (short)((var14[2] & 255) + ((var14[3] & 255) << 8))) << 16) + (var0 << 8) + var10))
                        == null) {
                        player.createActor(-2, var17, 0, 0, 0, 200, false);
                        var11 = Actor.lastSpawnedActor;
                        Actor.lastSpawnedActor.uid = var12;
                        var11.facingLeft = false;
                        var11.flags &= -2;
                        addCinematicActor(var11);
                     }
               }

               var13 = scriptTrackOffsets[var0][var10];
               int var32 = 0;
               int var18 = 0;
               short var19 = -1;
               short var20 = (short)((var14[var13] & 255) + ((var14[var13 + 1] & 255) << 8));
               var13 += 2;
               byte var21 = var14[var13++];

               for (int var22 = 0; var22 < var21; var22++) {
                  byte var23;
                  if ((var23 = var14[var13++]) >= 100) {
                     var13 += executeScriptOpcode(var23, var14, var13, var9, var20, var11);
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
                                 } else if (var18 > levelPixelHeight - 320) {
                                    var18 = levelPixelHeight - 320;
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
                              short var35;
                              if ((var35 = var14[var13]) < 0) {
                                 var35 += 256;
                              }

                              var11.setAnimationImmediate(var35);
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

                              var11.flags |= var34 >> 16;
                              var11.facingLeft = (var11.flags & 1) != 0;
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

                              var11.flags &= ~(var33 >> 16);
                           }

                           var11.facingLeft = (var11.flags & 1) != 0;
                           var13 += 4;
                           break;
                        case 34:
                        case 35:
                        case 36:
                           int var27 = (var14[var13] & 255) + ((var14[var13 + 1] & 255) << 8);
                           var13 += 2;
                           var6 = findActorByUid(var27);
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
                              executeScriptGlobalCommand(var3, var4, var5, var26);
                           }
                     }
                  }
               }

               if (var20 == var9) {
                  scriptTrackOffsets[var0][var10] = var13;
               } else if (var20 < var9) {
                  scriptTrackOffsets[var0][var10] = var13;
               }

               if (scriptTrackOffsets[var0][var10] < var14.length - 2) {
                  var7 = false;
               } else if (var15 == 3) {
                  removeActor(var11);
               }

               if (var19 >= 0) {
                  if (var15 == 1) {
                     cameraX = cameraX + (var32 - cameraX) / (var19 - var9 + 1);
                     cameraY = cameraY + (var18 - cameraY) / (var19 - var9 + 1);
                     updateViewBounds();
                  } else {
                     var11.positionX = var11.positionX + (var32 - var11.positionX) / (var19 - var9 + 1);
                     var11.positionY = var11.positionY + (var18 - var11.positionY) / (var19 - var9 + 1);
                     var11.isVisible();
                  }
               }
            }
         }

         if (var7 || cu) {
            cu = false;
            if (var2 == 0) {
               restartScript(var0);
               return;
            }

            finishCutscene(var0);
            if (cp) {
               cp = false;
               ch = false;
               removeActor(player.linkedEffect);
               player.linkedEffect = null;
               if (cq) {
                  startCutscene(findScriptIndex(cr));
                  Actor.activeBoss.health = Actor.activeBoss.health - Actor.activeBoss.health / 5;
                  if (Actor.activeBoss.health <= 0) {
                     Actor.activeBoss.health = 1;
                     return;
                  }
               } else {
                  startCutscene(findScriptIndex(cs));
                  health = health - health / 3;
               }
            }
         }
      }
   }

   // $VF: renamed from: h (int, int, int, int) void
   static final void executeScriptGlobalCommand(int var0, int var1, int var2, int var3) {
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
                  changeState(15);
            }
         }
      }
   }

   // $VF: renamed from: z (int) int
   static final int getScriptOpcodeSize(int var0) {
      return scriptOpcodeSizes[var0 - 100];
   }

   // $VF: renamed from: a (int, byte[], int, int, int, b) int
   static final int executeScriptOpcode(int var0, byte[] var1, int var2, int var3, int var4, Actor var5) {
      int var7 = getScriptOpcodeSize(var0);
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
            findActorByUid(var31).applyScriptProperty(var32, var30);
            break;
         case 101:
            short var29 = (short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8));
            cu = true;
            startCutscene(findScriptIndex(var29));
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
            stopSound();
            break;
         case 104:
            startVerticalShake((short)((var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8)));
         case 105:
         case 109:
         case 110:
         case 112:
         case 118:
         default:
            break;
         case 106:
            int var9 = var1[var2] & 255;
            if (var5.isBasicEnemyType()) {
               var5.changeActionState(var9);
            } else {
               var5.actionState = var9;
            }
            break;
         case 107:
            int var10 = (var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8) + ((var1[var2 + 2] & 255) << 16) + ((var1[var2 + 3] & 255) << 24);
            var5.flags |= var10;
            if (var10 == 4096) {
            }
            break;
         case 108:
            int var11 = (var1[var2] & 255) + ((var1[var2 + 1] & 255) << 8) + ((var1[var2 + 2] & 255) << 16) + ((var1[var2 + 3] & 255) << 24);
            var5.flags &= ~var11;
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
               var5.refreshBounds();
               var16 = var5.positionX;
               var17 = var5.collisionBounds[1];
            }

            if (aU == null) {
               aU = getString(var12, var13);
               aV = getString(0, var14 + 77);
               K = 0;
               aY = var14;
               aZ = var15;
               aY = -1;
               boolean var18 = aY != -1 && aY <= 2;
               boolean var19 = false;
               font.currentPalette = 0;
               font.wrapText('}' + aU, var18 ? 175 : 210);
               ba = 0;
               aW = var16 - cameraX;
               aX = var17 - cameraY;
               f(5);
            }
            break;
         case 113:
            removeActor(var5);
            break;
         case 114:
            co = randomRange(0, 5);
            player.showLinkedPrompt(ct[co], cameraX + 120, cameraY + 160);
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
            F = var5.positionX;
            G = var5.positionY;
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

   // $VF: renamed from: b (boolean) void
   public static final void accessSaveRecord(boolean var0) {
      try {
         RecordStore var1;
         if ((var1 = RecordStore.openRecordStore("/dvseed.sav", true)).getNumRecords() <= 0) {
            if (!var0) {
               var1.closeRecordStore();
               return;
            }

            var1.addRecord(saveBuffer, 0, saveBufferSize);
         } else if (var0) {
            var1.setRecord(1, saveBuffer, 0, saveBufferSize);
         } else {
            var1.getRecord(1, saveBuffer, 0);
            saveLoaded = true;
         }

         var1.closeRecordStore();
      } catch (Exception var2) {
         var2.toString();
      }
   }

   public static final void A(int var0) {
      cy |= var0 & 0xFF;
   }

   // $VF: renamed from: as () void
   private static void updateTitle() {
      setClip(0, 0, 240, 320);
      if (titleImage == null) {
         a(0, true);
      }

      drawImage(titleImage, 0, 0, 0);
      font.currentPalette = 0;
      if (stateFrame % 10 > 5) {
         font.drawText(graphics, getString(0, 10), 120, 300, 17);
      }

      if (hasKeyPress()) {
         changeState(2);
      }
   }

   // $VF: renamed from: Q (int) int
   private static int mapKeyCode(int var0) {
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

   // $VF: renamed from: at () boolean
   private static boolean updateCheatSequence() {
      if (wasKeyPressed(cheatSequence[cheatProgress])) {
         cheatProgress++;
         if (cheatProgress == cheatSequence.length) {
            cheatProgress = 0;
            playSoundOnce(-1);
            clearInput();
            cheatsUnlocked = true;
            return true;
         }
      } else if (hasKeyPress()) {
         cheatProgress = 0;
      }

      return false;
   }

   // $VF: renamed from: A () void
   public static final void latchInput() {
      if (pressedMask != 0) {
         framesSincePress = 0;
      }

      if (releasedMask != 0) {
         heldFrameCount = -1;
      }

      framesSincePress++;
      if (frameHeldMask != heldMask || frameHeldMask == 0) {
         if (player != null && (player.animationId == 2 || player.animationId == 3) && heldFrameCount >= 0) {
            if (heldFrameCount <= 2) {
               Actor.shortJump = true;
            } else {
               Actor.shortJump = false;
            }
         }

         heldFrameCount = -1;
      }

      if (frameHeldMask != 0) {
         heldFrameCount++;
      }

      frameHeldMask = heldMask;
      pressedMask = pendingPressMask;
      releasedMask = pendingReleaseMask;
      pendingPressMask = 0;
      pendingReleaseMask = 0;
   }

   protected final void keyPressed(int var1) {
      pendingPressMask = pendingPressMask | mapKeyCode(var1);
      int var2 = mapKeyCode(var1);
      pendingPressMask |= var2;
      if ((var2 & 4112) != 0) {
         pendingPressMask &= -8257;
         pendingPressMask &= -16389;
         pendingPressMask &= -33025;
      }

      if ((var2 & 8256) != 0) {
         pendingPressMask &= -4113;
         pendingPressMask &= -16389;
         pendingPressMask &= -33025;
      }

      if ((var2 & 16388) != 0) {
         pendingPressMask &= -4113;
         pendingPressMask &= -8257;
         pendingPressMask &= -33025;
      }

      if ((var2 & 33024) != 0) {
         pendingPressMask &= -4113;
         pendingPressMask &= -8257;
         pendingPressMask &= -16389;
      }

      if ((var2 & 65568) != 0) {
         pendingPressMask &= -4113;
         pendingPressMask &= -8257;
         pendingPressMask &= -33025;
         pendingPressMask &= -16389;
      }

      frameHeldMask = frameHeldMask | pendingPressMask;
      heldMask = heldMask | pendingPressMask;
   }

   protected final void keyReleased(int var1) {
      pendingReleaseMask = pendingReleaseMask | mapKeyCode(var1);
      heldMask = heldMask & ~pendingReleaseMask;
      frameHeldMask = frameHeldMask & ~pendingReleaseMask;
   }

   // $VF: renamed from: B (int) boolean
   public static final boolean isKeyHeld(int var0) {
      return (frameHeldMask & var0) != 0;
   }

   // $VF: renamed from: C (int) boolean
   public static final boolean wasKeyPressed(int var0) {
      return (pressedMask & var0) != 0;
   }

   // $VF: renamed from: B () boolean
   public static final boolean hasKeyPress() {
      return pressedMask != 0;
   }

   // $VF: renamed from: C () void
   public static final void clearInput() {
      heldMask = 0;
      frameHeldMask = 0;
      pressedMask = 0;
      releasedMask = 0;
      pendingPressMask = 0;
      pendingReleaseMask = 0;
   }

   public static final void D() {
      if (E() && bc != 1) {
         if (cR) {
            if (!cP) {
               for (int var0 = 0; var0 < 30; var0++) {
                  cK[var0] = (short)(var0 * 8);
                  cM[var0] = (short)(randomRange(0, 100) * 320 / 100);
                  cN[var0] = (byte)(10 + randomRange(0, 8));
                  cO[var0] = (byte)(7 + randomRange(0, 6));
                  cL[var0] = (byte)randomRange(0, 3);
               }

               cP = true;
            }

            setColor(-1428431909);

            for (int var3 = 0; var3 < cQ; var3++) {
               if (state != 14 && state != 31) {
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

               setColor(cW[cL[var3]]);
               drawLine(cK[var3], cM[var3], cK[var3] - 7, cM[var3] + 13);
            }

            if (randomRange(0, 500) == 40 && !cS) {
               cS = true;
               cT = 0;
               cU = randomRange(0, 3) == 0;
            }

            if (cS && state != 14) {
               int var2 = cT++;
               if (cU) {
                  directGraphics.setARGBColor(cV[0]);
                  fillRect(0, 0, 240, 320);
                  cS = false;
                  return;
               }

               if (var2 <= 1) {
                  directGraphics.setARGBColor(cV[var2]);
                  fillRect(0, 0, 240, 320);
                  return;
               }

               cS = false;
            }
         }
      }
   }

   public static final void a(boolean var0, int var1) {
      directGraphics.setARGBColor(872414976);
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

            directGraphics.setARGBColor(db[var11]);
            drawArc(player.positionX - cX / 2 - cameraX, player.positionY - 30 - 35 - cameraY, cX, 70, var2, 180);
            drawArc(player.positionX - cX / 2 + 1 - cameraX, player.positionY - 30 - 35 - cameraY, cX, 70, var2, 180);
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

            directGraphics.setARGBColor(db[var10]);
            drawArc(player.positionX - 35 - cameraX, player.positionY - 30 - cY / 2 - cameraY, 70, cY, var2, 180);
            drawArc(player.positionX - 35 - cameraX, player.positionY - 30 - cY / 2 + 1 - cameraY, 70, cY, var2, 180);
         }

         da = (da + 4) % 180;
      } else {
         if (var1 == 888) {
            if (da == 0 || da == 180 || sin256(da) == 0) {
               da = (da + 1) % 45;
               return;
            }

            int var4 = 8960 / sin256(da);
            int var5 = 35 * cos256(da) * sin256(45) / sin256(da) >> 8;
            System.out.println("r2  " + var4);
            System.out.println("r3  " + var5);
            if (var0) {
               drawArc(player.positionX + var5 - var4 - cameraX, player.positionY - 30 + var5 - var4 - cameraY, var4 * 2, var4 * 2, 135 - da, da * 2);
            } else {
               drawArc(player.positionX - var5 - var4 - cameraX, player.positionY - 30 - var5 - var4 - cameraY, var4 * 2, var4 * 2, 315 - da, da * 2);
            }

            da = (da + 1) % 45;
         }
      }
   }

   public static final boolean E() {
      return levelIndex == 2 || levelIndex == 3;
   }

   private static void R(int var0) {
      try {
         loadTileMaps(var0, false);
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
            drawLine(0, var3 + var2, var1 * 24, var3 + var2);
         } else {
            drawLine(240 - var1 * 24, var3 + var2, 240, var3 + var2);
         }
      }
   }

   private static void a(int var0, int var1, int var2, int var3, boolean var4, int var5, int var6) {
      if (var4) {
         setColor(var6);
         fillRect(var0, var1, var2 + 1, var3 + 2);
      }

      setColor(var5);
      drawLine(var0, var1, var0 + var2, var1);
      drawLine(var0 - 1, var1 + 1, var0 - 1, var1 + 1 + var3);
      drawLine(var0, var1 + var3 + 2, var0 + var2, var1 + var3 + 2);
      drawLine(var0 + var2 + 1, var1 + 1, var0 + var2 + 1, var1 + 1 + var3);
   }

   private static void e(boolean var0) {
      if (dd != 2) {
         a(dc, 0, true);
         setClip(0, 0, 240, 320);
         if (wasKeyPressed(2048) && var0 && cheatsUnlocked) {
            ed = !ed;
            if (ed) {
               ec = maxUnlockedLevel;
               maxUnlockedLevel = 9;
            } else {
               maxUnlockedLevel = ec;
            }
         }
      }

      setColor(0);
      fillRect(0, 0, 240, 20);
      fillRect(0, 200, 240, 140);
      if (var0) {
         font.currentPalette = 0;
         font.drawText(graphics, getString(0, 41) + " " + (dh + 1), 120, 260, 3);
      }

      font.currentPalette = 1;
      font.drawText(graphics, getString(0, 42 + dh), 120, 280, 3);
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
         fillRect(0, 16, 240, 186 + (var0 ? 0 : 50));
         Actor.a(120, var0 ? 120 : 145, 120, var0 ? 120 : 145, 40, 35, 3 * stateFrame, true);
         font.currentPalette = 0;
         font.drawText(graphics, getString(0, 26), 120, 180 + (var0 ? 0 : 25), 17);
      }

      if (var0) {
         byte var1 = -1;
         int var2 = df - 9;
         byte[] var3 = dj[var2];
         byte[] var4 = ee[var2];
         if (dg[var2 + 1] - 1 >= maxUnlockedLevel) {
            int var5 = maxUnlockedLevel - dg[var2];
            var1 = var3[var5];
         }

         int var8 = 0;
         if (maxUnlockedLevel != 9 && var2 == 0) {
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
            sprites[35].drawFrame(graphics, 7, 120 + var4[var3[var9] << 1], 235 + var4[(var3[var9] << 1) + 1], 0, 0, 0);
         }

         if (eh == 2) {
            sprites[35].drawAnimationFrame(graphics, 0, dc % 8, 120 + di[dh << 1], 239 + di[(dh << 1) + 1] - 4, 0, 0, 0);
         }
      }

      if (dd == 0) {
         dc++;
      }

      setColor(15244571);
      if (dh > 0 || maxUnlockedLevel == 9) {
         fillTriangle(8, 223, 18, 218, 18, 228);
      }

      if (dh < maxUnlockedLevel || maxUnlockedLevel == 9) {
         fillTriangle(232, 223, 222, 218, 222, 228);
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
               if (maxUnlockedLevel == 9) {
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
               if (maxUnlockedLevel == 9) {
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

   // $VF: renamed from: av () void
   private static void updateLevelSelection() {
      e(true);
      if (dd != 2) {
         drawSoftkeys(getString(0, 18), getString(0, 17));
      } else {
         setClip(0, 296, 240, 24);
         setColor(0);
         fillRect(0, 296, 240, 24);
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
            if (wasKeyPressed(8256)) {
               if (maxUnlockedLevel <= dh) {
                  return;
               }

               eh = 0;
               ei = false;
               au();
            } else if (wasKeyPressed(4112)) {
               if (dh == 0 && maxUnlockedLevel != 9) {
                  return;
               }

               ei = true;
               eh = 0;
               au();
            }

            if (wasKeyPressed(327712)) {
               levelIndex = dh;
               changeState(9);
               if (dw) {
                  prepareLevelSelectionStats();
               }

               Actor.bC = false;
               writeByte(saveBuffer, 644, (byte)(Actor.bC ? 1 : 0));
            } else if (wasKeyPressed(131072)) {
               changeState(2);
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
               loadTileMaps(df, false);
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

   // $VF: renamed from: ax () void
   private static void updateScrollingText() {
      setClip(0, 0, 240, 320);
      g(0, 0, 240, 320);
      setClip(0, 20, 240, 266);
      String var0 = null;
      if (state == 20) {
         var0 = getString(0, 40);
      } else {
         var0 = getString(0, 28);
      }

      font.currentPalette = state != 6 ? 0 : 0;
      if (font.drawWrappedText(graphics, var0, 120, scrollingTextY, 17, 230, 0, -1) < 0) {
         scrollingTextY = 266;
      } else {
         scrollingTextY--;
      }

      if (previousState == 8) {
         drawSoftkeys(getString(0, 20), "");
      } else {
         drawSoftkeys(getString(0, 18), state == 6 ? "" : getString(0, 17));
      }

      if (wasKeyPressed(262144) && state == 20) {
         changeState(19);
         skipCheckpointPosition = true;
         df = 9;
      }

      if (!isKeyHeld(65568) && !isKeyHeld(33024)) {
         if (wasKeyPressed(131072)) {
            if (previousState == 8 && InGamePromotion.a()) {
               changeState(27);
               return;
            }

            changeState(2);
            df = 9;
         }
      } else {
         scrollingTextY -= 4;
      }
   }

   // $VF: renamed from: ay () void
   private static void preloadMenuSound() {
      try {
         openPack("/19");
         loadSoundData(-1);
         closePack();
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   // $VF: renamed from: D (int) void
   public static final void loadSoundData(int var0) {
      try {
         openPack("/19");
         byte[] var1;
         if ((var1 = readResource(musicIdForLevel(var0))) != null) {
            soundData = null;
            soundData = var1;
            closePack();
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   // $VF: renamed from: i (int, int) void
   public static final void playSound(int var0, int var1) {
      if (var0 != -1 && soundEnabled) {
         if (var0 != rememberedMusicId || soundPlayer == null || soundPlayer.getState() != 400) {
            switch (var0) {
               case 0:
               case 1:
               case 2:
               case 3:
                  rememberedMusicId = var0;
               default:
                  stopSound();

                  try {
                     ByteArrayInputStream var2;
                     (var2 = new ByteArrayInputStream(soundData)).reset();
                     if (var0 < wavIdThreshold) {
                        soundPlayer = Manager.createPlayer(var2, "audio/midi");
                     } else {
                        soundPlayer = Manager.createPlayer(var2, "audio/x-wav");
                     }

                     soundPlayer.realize();
                     var2.close();
                  } catch (Exception var6) {
                     var6.printStackTrace();
                  }

                  Player var3 = soundPlayer;
                  if (soundPlayer != null) {
                     try {
                        var3.setLoopCount(var1);
                        var3.start();
                        resumeSoundId = -1;
                     } catch (Exception var5) {
                        ErrorReporter.report(var5, "play");
                        var5.printStackTrace();
                     }
                  }
            }
         }
      }
   }

   // $VF: renamed from: E (int) void
   public static final void playSoundOnce(int var0) {
      playSound(var0, 1);
   }

   // $VF: renamed from: F () void
   public static final void stopSound() {
      try {
         if (soundPlayer != null) {
            if (soundPlayer.getState() == 400) {
               soundPlayer.stop();
            }

            if (soundPlayer.getState() == 300) {
               soundPlayer.deallocate();
            }

            soundPlayer.close();
            soundPlayer = null;
         }
      } catch (Exception var1) {
      }
   }

   // $VF: renamed from: F (int) int
   public static final int musicIdForLevel(int var0) {
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

   // $VF: renamed from: G () void
   public static final void playLevelMusic() {
      if (musicIdForLevel(levelIndex) != rememberedMusicId || soundPlayer == null || soundPlayer.getState() != 400) {
         playSound(musicIdForLevel(levelIndex), -1);
      }
   }

   // $VF: renamed from: a (java.lang.String, java.lang.String, java.lang.String) java.lang.String
   public static final String replaceToken(String var0, String var1, String var2) {
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
