import javax.microedition.lcdui.Graphics;

/**
 * Desktop-port state that the patched reference sources consult (see tools/port_patches.py).
 *
 * Everything here is a no-op at screenWidth == 240 with english == false: in that configuration the
 * translated game must stay field-for-field identical to the original bytecode (tools/check_engine.py
 * verifies it), so no helper may touch game state, clips or translation unless the view is wide.
 *
 * Widescreen model: the game's own camera (Game.cameraX) keeps its original meaning, the left edge
 * of a 240 pixel window, and all original camera/region logic runs unchanged. The wide view is a
 * function of that camera: centred on the 240 window, clamped to the same room bounds the camera
 * obeys (Game.R/S), and centred with black bars when the room is narrower than the screen. World
 * drawing is translated by (cameraX - viewX), so the original "world - cameraX" draw code lands in
 * the right place without touching each call. Game.viewBounds, which the game uses for visibility,
 * edge spawns and off-screen removal, is widened to the visible range.
 */
final class PortConfig {
   static int screenWidth = 240;
   static boolean english = false;

   /** World x at the left edge of the screen (wide mode only). */
   static int viewX;
   static boolean viewValid;
   /** Visible world range [clipLeft, clipRight): the current room, or two rooms while scrolling between them. */
   static int clipLeft;
   static int clipRight;
   static int viewCameraX;
   /** Translation currently applied for world drawing; 0 outside a world segment. */
   static int worldTx;
   static boolean uiFrame;

   /** Draw visible tile cells directly even at 240 (set while render interpolation is on). */
   static boolean directTiles;

   static boolean wide() {
      return screenWidth > 240;
   }

   static int margin() {
      return (screenWidth - 240) / 2;
   }

   private static void computeView() {
      int w = screenWidth;
      int lo = Game.R > 0 ? Game.R : 0;
      int hi = Game.S > 0 ? Game.S : Game.levelPixelWidth;
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

   /** A level was loaded or the display width changed: forget the smoothed view. */
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

   // ---------------------------------------------------------------- drawing segments

   static void beginFrame(Graphics g, int state) {
      uiFrame = false;
      worldTx = 0;
      if (screenWidth <= 240) {
         return;
      }

      boolean world = state == 8 || state == 12 || state == 14 || state == 15 || state == 32;
      if (!world) {
         int m = margin();
         g.setClip(0, 0, screenWidth, 320);
         g.setColor(0);
         g.fillRect(0, 0, m, 320);
         g.fillRect(m + 240, 0, screenWidth - m - 240, 320);
         g.translate(m, 0);
         g.setClip(0, 0, 240, 320);
         uiFrame = true;
      }
   }

   static void endFrame(Graphics g) {
      if (uiFrame) {
         g.translate(-margin(), 0);
         uiFrame = false;
      }
   }

   /** Start of the world layer: black out what lies outside the room, then translate and clip. */
   static void beginWorld(Graphics g) {
      if (screenWidth <= 240) {
         pushWorld(g);
         return;
      }

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

      pushWorld(g);
   }

   static void pushWorld(Graphics g) {
      if (screenWidth > 240) {
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

      g.translate(-worldTx, 0);
      worldTx = 0;
      g.setClip(0, 0, screenWidth, 320);
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

   /** Panels, dialogue boxes and prompts keep their 240 pixel layout, centred on the wide screen. */
   static void beginUi(Graphics g) {
      if (screenWidth <= 240) {
         return;
      }

      g.translate(margin(), 0);
      g.setClip(0, 0, 240, 320);
   }

   static void beginUiOpaque(Graphics g) {
      if (screenWidth <= 240) {
         return;
      }

      g.setClip(0, 0, screenWidth, 320);
      g.setColor(0);
      g.fillRect(0, 0, screenWidth, 320);
      beginUi(g);
   }

   static void endUi(Graphics g) {
      if (screenWidth <= 240) {
         return;
      }

      g.translate(-margin(), 0);
      g.setClip(0, 0, screenWidth, 320);
   }

   /** Wide replacement for the original 260x340 scrolling tile buffer: draw the visible cells directly. */
   static void drawTiles(Graphics g, int cameraX, int cameraY) {
      // Local drawing coordinates are (world - Game.cameraX); cameraX here may carry a scroll offset.
      int left = worldLeft();
      int right = worldRight();
      g.setColor(0);
      g.fillRect(left, 0, right - left, 320);
      PortNative.layerTiles(worldTx - cameraX, -cameraY);
      // One extra cell on every side: the clip hides it now, interpolated frames may slide it into view.
      int firstColumn = floorDiv(left + cameraX, 20) - 1;
      int lastColumn = floorDiv(right - 1 + cameraX, 20) + 1;
      int firstRow = floorDiv(cameraY, 20) - 1;
      int lastRow = floorDiv(cameraY + 319, 20) + 1;
      if (firstRow < 0) {
         firstRow = 0;
      }

      if (lastRow > Game.bW - 1) {
         lastRow = Game.bW - 1;
      }

      for (int row = firstRow; row <= lastRow; row++) {
         for (int column = firstColumn; column <= lastColumn; column++) {
            int wrapped = column % Game.bU;
            if (wrapped < 0) {
               wrapped += Game.bU;
            }

            Game.portTile(g, column * 20 - cameraX, row * 20 - cameraY, wrapped + row * Game.bU);
         }
      }
   }

   private static int floorDiv(int a, int b) {
      int q = a / b;
      if (a % b != 0 && (a < 0) != (b < 0)) {
         q--;
      }

      return q;
   }

   // ---------------------------------------------------------------- text

   static String text(int table, int index, String original) {
      if (!english) {
         return original;
      }

      String s = PortEnglish.get(table, index, original);
      return s != null ? s : original;
   }
}
