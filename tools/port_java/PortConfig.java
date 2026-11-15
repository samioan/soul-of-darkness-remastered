import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * Desktop-port state, consulted by tools/port_patches.py.
 *
 * Everything here is a no-op at screenWidth == 240 with directTiles off: in that configuration the
 * translated game must stay field-for-field identical to the original bytecode
 * (tools/check_engine.py), so no helper may touch game state, clips or translation unless the
 * view is wide or render recording is on.
 *
 * Widescreen model: the game's camera (Game.cameraX) keeps its original
 * meaning, the left edge of a 240 pixel window, and all original camera/region logic runs
 * unchanged. The wide view is derived from it: centred on that window, clamped to the room bounds
 * the camera obeys (Game.regionLeft/regionRight), centred with black bars when the room is narrower
 * than the screen. World drawing is translated by (cameraX - viewX), so "world - cameraX" draw code
 * is untouched; the three tile layers draw the extra columns. After the world, everything the game
 * draws in its 240-wide interface layout is centred.
 */
final class PortConfig {
   static int screenWidth = 240;
   /** Draw tile cells directly and with a one-cell margin (set while render interpolation is on). */
   static boolean directTiles;

   /** Use desktop key names in the few strings that mention phone softkeys (set with the options menu). */
   static boolean desktopText;

   static int viewX;
   static boolean viewValid;
   static int clipLeft;
   static int clipRight;
   static int viewCameraX;
   /** Translation currently applied for world drawing; 0 outside a world segment. */
   static int worldTx;

   static boolean wide() {
      return screenWidth > 240;
   }

   static int margin() {
      return (screenWidth - 240) / 2;
   }

   private static void computeView() {
      int w = screenWidth;
      int lo = Game.regionLeft > 0 ? Game.regionLeft : 0;
      int hi = Game.regionRight > 0 ? Game.regionRight : Game.levelPixelWidth;
      if (hi < lo + 240) {
         lo = 0;
         hi = Game.levelPixelWidth;
      }

      // The camera may still be scrolling into these bounds from a neighbouring room.
      if (Game.cameraX < lo) {
         lo = Game.cameraX;
      }

      if (Game.cameraX + 240 > hi) {
         hi = Game.cameraX + 240;
      }

      int target;
      if (hi - lo <= w) {
         target = lo - (w - (hi - lo)) / 2;
      } else {
         target = Game.cameraX - margin();
         if (target < lo) {
            target = lo;
         }

         if (target > hi - w) {
            target = hi - w;
         }
      }

      if (!viewValid || Math.abs(target - viewX) > w) {
         viewX = target;
         clipLeft = lo;
         clipRight = hi;
         viewValid = true;
      } else {
         int d = target - viewX;
         if (d > 48) {
            d = 48;
         } else if (d < -48) {
            d = -48;
         }

         viewX += d;
         if (viewX == target) {
            clipLeft = lo;
            clipRight = hi;
         } else {
            if (lo < clipLeft) {
               clipLeft = lo;
            }

            if (hi > clipRight) {
               clipRight = hi;
            }
         }
      }

      // The original 240 window always stays on screen.
      if (viewX > Game.cameraX) {
         viewX = Game.cameraX;
      }

      if (viewX < Game.cameraX + 240 - w) {
         viewX = Game.cameraX + 240 - w;
      }

      viewCameraX = Game.cameraX;
   }

   static int visibleLeft() {
      return viewX > clipLeft ? viewX : clipLeft;
   }

   static int visibleRight() {
      return viewX + screenWidth < clipRight ? viewX + screenWidth : clipRight;
   }

   /** Called wherever the original refreshes Game.viewBounds from the camera. */
   static void updateView() {
      if (screenWidth <= 240) {
         viewValid = false;
         return;
      }

      computeView();
      Game.viewBounds[0] = visibleLeft();
      Game.viewBounds[2] = visibleRight();
   }

   static void resetView() {
      viewValid = false;
      worldTx = 0;
   }

   static int viewCenterX() {
      if (screenWidth <= 240 || !viewValid) {
         return Game.cameraX + 120;
      }

      return (visibleLeft() + visibleRight()) / 2;
   }

   static int viewHalfWidth() {
      if (screenWidth <= 240 || !viewValid) {
         return 120;
      }

      int half = (visibleRight() - visibleLeft()) / 2;
      return half < 120 ? 120 : half;
   }

   /** Left/right limits of the visible world in the coordinates the game draws with (world - cameraX). */
   static int worldLeft() {
      if (screenWidth <= 240 || !viewValid) {
         return 0;
      }

      return visibleLeft() - Game.cameraX;
   }

   static int worldRight() {
      if (screenWidth <= 240 || !viewValid) {
         return 240;
      }

      return visibleRight() - Game.cameraX;
   }

   /** Screen x of the left edge in the current drawing coordinates (0 unless a translation is active). */
   static int screenLeft(Graphics g) {
      return screenWidth <= 240 ? 0 : -g.getTranslateX();
   }

   // ---------------------------------------------------------------- frame and segments

   private static void untranslate(Graphics g) {
      int tx = g.getTranslateX();
      if (tx != 0) {
         g.translate(-tx, 0);
      }

      worldTx = 0;
   }

   static void beginFrame(Graphics g, int state) {
      worldTx = 0;
      if (screenWidth <= 240) {
         return;
      }

      boolean world = state == 8 || state == 12 || state == 14 || state == 15 || state == 32;
      if (!world) {
         int m = margin();
         // State 31 (weapon upgrade) is drawn over the last gameplay frame, which stays on screen.
         if (state != 31) {
            g.setClip(0, 0, screenWidth, 320);
            g.setColor(0);
            g.fillRect(0, 0, m, 320);
            g.fillRect(m + 240, 0, screenWidth - m - 240, 320);
         }

         g.translate(m, 0);
         g.setClip(0, 0, 240, 320);
      }
   }

   static void endFrame(Graphics g) {
      if (screenWidth > 240) {
         untranslate(g);
      }
   }

   /** Start of the world layer: black out what lies outside the room, then translate and clip. */
   static void beginWorld(Graphics g) {
      if (screenWidth > 240) {
         untranslate(g);
         if (!viewValid || viewCameraX != Game.cameraX) {
            computeView();
         }

         g.setClip(0, 0, screenWidth, 320);
         g.setColor(0);
         int left = visibleLeft() - viewX;
         int right = visibleRight() - viewX;
         if (left > 0) {
            g.fillRect(0, 0, left, 320);
         }

         if (right < screenWidth) {
            g.fillRect(right, 0, screenWidth - right, 320);
         }
      }

      pushWorld(g);
   }

   static void pushWorld(Graphics g) {
      if (screenWidth > 240) {
         untranslate(g);
         worldTx = Game.cameraX - viewX;
         g.translate(worldTx, 0);
         clipWorld(g);
      }

      PortNative.layerWorld(worldTx - Game.cameraX, -Game.cameraY);
   }

   static void endWorld(Graphics g) {
      PortNative.layerScreen();
      if (screenWidth <= 240) {
         return;
      }

      untranslate(g);
      g.setClip(0, 0, screenWidth, 320);
   }

   /** End of the world layer before the weather: rain, snow and embers stay inside the visible room. */
   static void endWorldForWeather(Graphics g) {
      PortNative.layerScreen();
      if (screenWidth <= 240) {
         return;
      }

      untranslate(g);
      g.setClip(visibleLeft() - viewX, 0, visibleRight() - visibleLeft(), 320);
   }

   /**
    * Tutorial text names "the right soft key" (weapon upgrade). When the options menu is installed the
    * host supplies the key currently bound to that action instead.
    */
   static String softKeyName(String original) {
      if (!desktopText) {
         return original;
      }

      String key = PortNative.settingValue(2, 10);
      if (key == null || key.length() == 0 || key.equals("-")) {
         return original;
      }

      int space = key.indexOf(" ");
      if (space > 0) {
         key = key.substring(0, space);
      }

      return "{" + key + "} {KEY}";
   }

   /** Replaces the original full-screen clip inside world drawing. */
   static void clipWorld(Graphics g) {
      if (screenWidth <= 240 || !viewValid) {
         g.setClip(0, 0, 240, 320);
         return;
      }

      int left = visibleLeft() - viewX;
      int right = visibleRight() - viewX;
      g.setClip(left - worldTx, 0, right - left, 320);
   }

   /** Panels, dialogue, the icon bar and prompts keep their 240 pixel layout, centred on the wide screen. */
   static void beginUi(Graphics g) {
      PortNative.layerScreen();
      if (screenWidth <= 240) {
         return;
      }

      untranslate(g);
      g.translate(margin(), 0);
      g.setClip(0, 0, 240, 320);
   }

   /** The bottom icon bar: its black strip spans the screen, the icons stay centred. */
   static void beginUiBar(Graphics g) {
      if (screenWidth > 240) {
         untranslate(g);
         g.setClip(0, 0, screenWidth, 320);
         g.setColor(0);
         g.fillRect(0, 293, screenWidth, 28);
      }

      beginUi(g);
   }

   static void endUi(Graphics g) {
      if (screenWidth <= 240) {
         return;
      }

      untranslate(g);
      g.setClip(0, 0, screenWidth, 320);
   }

   // ---------------------------------------------------------------- tile layers

   /** Extra cells drawn around each tile layer so interpolated frames can slide them into view. */
   static int tileMargin() {
      return directTiles ? 1 : 0;
   }

   static int firstColumn(int origin) {
      if (screenWidth <= 240 && !directTiles) {
         return origin / 20;
      }

      return floorDiv(origin + worldLeft(), 20) - tileMargin();
   }

   static int lastColumn(int origin) {
      if (screenWidth <= 240 && !directTiles) {
         return (origin + 240 - 1) / 20;
      }

      return floorDiv(origin + worldRight() - 1, 20) + tileMargin();
   }

   /** Column index into a layer that repeats every `width` cells; the original only wrapped upwards. */
   static int wrapColumn(int column, int width) {
      int c = column % width;
      return c < 0 ? c + width : c;
   }

   /** Margin rows may fall outside the layer; the original rows never do. */
   static boolean rowInside(int row, int cells, int width) {
      return row >= 0 && (row + 1) * width <= cells;
   }

   /** Far background: wide replacement for the original 260x340 scroll buffer. */
   static void drawFar(Graphics g, int originX, int originY) {
      int left = worldLeft();
      int right = worldRight();
      PortNative.layerTiles(0, worldTx - originX, -originY);
      g.setColor(0);
      g.fillRect(left, 0, right - left, 320);
      int firstColumn = floorDiv(originX + left, 20) - tileMargin();
      int lastColumn = floorDiv(originX + right - 1, 20) + tileMargin();
      int firstRow = floorDiv(originY, 20) - tileMargin();
      int lastRow = floorDiv(originY + 319, 20) + tileMargin();
      if (firstRow < 0) {
         firstRow = 0;
      }

      if (lastRow > Game.cE - 1) {
         lastRow = Game.cE - 1;
      }

      for (int row = firstRow; row <= lastRow; row++) {
         for (int column = firstColumn; column <= lastColumn; column++) {
            Game.portTile(g, column * 20 - originX, row * 20 - originY, wrapColumn(column, Game.cC) + row * Game.cC);
         }
      }
   }

   /** The water tint image is 120x160 and was drawn four times to cover 240x320. */
   static void tileOverlay(Image image) {
      if (screenWidth <= 240 || !viewValid) {
         Game.a(image, 0, 0, 20);
         Game.a(image, 120, 160, 20);
         Game.a(image, 120, 0, 20);
         Game.a(image, 0, 160, 20);
         return;
      }

      for (int x = floorDiv(worldLeft(), 120) * 120; x < worldRight(); x += 120) {
         Game.a(image, x, 0, 20);
         Game.a(image, x, 160, 20);
      }
   }

   private static int floorDiv(int a, int b) {
      int q = a / b;
      if (a % b != 0 && (a < 0) != (b < 0)) {
         q--;
      }

      return q;
   }
}
