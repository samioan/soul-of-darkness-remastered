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

   /**
    * Fixes for flaws of the original game itself (set with the options menu, like desktopText). Off in
    * the configuration tools/check_engine.py compares with the original bytecode.
    */
   static boolean desktopFixes;

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

      // Only a level narrower than the screen leaves sides to fill (drawSides). Inside a level that
      // scrolls horizontally, a room narrower than the screen shows the level around it: the bounds
      // grow to the screen width, centred on the room and kept inside the level.
      if (hi - lo < w && Game.levelPixelWidth > w) {
         lo -= (w - (hi - lo)) / 2;
         hi = lo + w;
         if (lo < 0) {
            lo = 0;
            hi = w;
         } else if (hi > Game.levelPixelWidth) {
            hi = Game.levelPixelWidth;
            lo = hi - w;
         }
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

   /**
    * Origin of a parallax layer that the original scrolls at camera >> shift, in the coordinates the
    * world layer draws with. The wide view does not always move with the camera: in a room narrower
    * than the screen, and near a room edge, it stands still while the camera moves inside it. A
    * camera-based origin then slides the layer at almost the camera's speed in the wrong direction.
    * The layer follows the camera that would centre the original window in the view instead; while
    * the view tracks the camera that is the camera itself, and the result equals the original.
    */
   static int parallax(int camera, int shift) {
      if (screenWidth <= 240 || !viewValid) {
         return camera >> shift;
      }

      int centred = viewX + margin();
      return Game.cameraX - centred + (camera - Game.cameraX + centred >> shift);
   }

   // ---------------------------------------------------------------- levels narrower than the screen
   //
   // Where the whole level is narrower than the wide screen (the 400 pixel level 6 tower and level 10
   // arena), the sides are not left black: they show a blurred, darkened copy of the level as drawn
   // this frame (PortNative.sides), fading in from black when they first appear. Actors, collision and
   // activation still stop at the level edge. Narrow rooms inside wider levels show the level around
   // them instead (computeView).

   static final int SIDE_FADE_TICKS = 12;
   static int sideFade;
   static boolean sidesShown;

   /** Called once per painted world frame, after the world and before the weather and interface. */
   private static void drawSides(Graphics g) {
      int left = visibleLeft() - viewX;
      int right = visibleRight() - viewX;
      if (!viewValid || left <= 0 && right >= screenWidth) {
         sidesShown = false;
         return;
      }

      if (!sidesShown) {
         sidesShown = true;
         sideFade = 0;
      } else if (sideFade < SIDE_FADE_TICKS) {
         sideFade++;
      }

      // MIDP colours are opaque (Graphics.setColor drops alpha), so the blur is a native pixel pass.
      g.setClip(0, 0, screenWidth, 320);
      PortNative.sides(g, left, right, sideFade * 256 / SIDE_FADE_TICKS);
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
      sidesShown = false;
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
         // Screens without the world (menus, the level map) keep the 240 layout: their tile layers
         // must not use the wide view the last level left behind.
         viewValid = false;
         int m = margin();
         // State 31 (weapon upgrade) is drawn over the last gameplay frame, which stays on screen.
         if (state != 31) {
            g.setClip(0, 0, screenWidth, 320);
            // The Gameloft logo (state 1) sits on a white screen.
            g.setColor(state == 1 ? 0xFFFFFF : 0);
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
      drawSides(g);
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

   /**
    * Replaces a fill of the original 240x320 screen in an interface layout (translated to the centred
    * 240 window): the fill covers the whole wide screen, then the centred clip is restored.
    */
   static void fillScreen(Graphics g) {
      if (screenWidth <= 240) {
         g.fillRect(0, 0, 240, 320);
         return;
      }

      int tx = g.getTranslateX();
      g.setClip(-tx, 0, screenWidth, 320);
      g.fillRect(-tx, 0, screenWidth, 320);
      g.setClip(0, 0, 240, 320);
   }

   /** Replaces a fill of the original screen inside world drawing: the visible part of the level. */
   static void fillWorld(Graphics g) {
      g.fillRect(worldLeft(), 0, worldRight() - worldLeft(), 320);
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

   /** Far-layer picture scaled for the wide view, and what it was built from. */
   private static Image fitImage;
   private static Object fitKey;
   private static int fitScreenWidth;

   /**
    * Far background of the levels whose far layer is a single picture panned across the level
    * (origin = camera * (layer - window) / (level - window)) rather than a strip that repeats. On the
    * phone the 240x320 window moves about 20 pixels over it; a wide view would show it two or three
    * times side by side. Instead the picture is scaled up uniformly (nearest neighbour) until the
    * screen fits inside it with the same 20 pixel slack, and panned across the level the same way.
    */
   static void drawFarFit(Graphics g, int originX, int originY) {
      int farWidth = Game.cC * 20;
      int farHeight = Game.cE * 20;
      // scale = max(screenWidth / (farWidth - 20), 320 / (farHeight - 20)), as the fraction num / den
      int num = screenWidth;
      int den = farWidth - 20;
      if (320 * den > num * (farHeight - 20)) {
         num = 320;
         den = farHeight - 20;
      }

      if (den <= 0 || num <= den) {
         drawFar(g, originX, originY);
         return;
      }

      Object key = Game.portFarKey();
      if (fitImage == null || fitKey != key || fitScreenWidth != screenWidth) {
         fitImage = null;
         Image source = Image.createImage(farWidth, farHeight);
         Graphics sg = source.getGraphics();
         sg.setColor(0);
         sg.fillRect(0, 0, farWidth, farHeight);
         for (int row = 0; row < Game.cE; row++) {
            for (int column = 0; column < Game.cC; column++) {
               Game.portTile(sg, column * 20, row * 20, column + row * Game.cC);
            }
         }

         int[] pixels = new int[farWidth * farHeight];
         source.getRGB(pixels, 0, farWidth, 0, 0, farWidth, farHeight);
         int scaledWidth = (farWidth * num + den - 1) / den;
         int scaledHeight = (farHeight * num + den - 1) / den;
         int[] scaled = new int[scaledWidth * scaledHeight];
         for (int y = 0; y < scaledHeight; y++) {
            int sy = y * den / num;
            if (sy >= farHeight) {
               sy = farHeight - 1;
            }

            for (int x = 0; x < scaledWidth; x++) {
               int sx = x * den / num;
               if (sx >= farWidth) {
                  sx = farWidth - 1;
               }

               scaled[y * scaledWidth + x] = pixels[sy * farWidth + sx];
            }
         }

         fitImage = Image.createRGBImage(scaled, scaledWidth, scaledHeight, false);
         fitKey = key;
         fitScreenWidth = screenWidth;
      }

      int left = worldLeft();
      int right = worldRight();
      int visible = right - left;
      int x = 0;
      int travelX = fitImage.getWidth() - visible;
      int levelX = Game.levelPixelWidth - visible;
      if (travelX > 0 && levelX > 0) {
         int from = left + Game.cameraX;
         if (from < 0) {
            from = 0;
         }

         if (from > levelX) {
            from = levelX;
         }

         x = (int)((long)from * travelX / levelX);
      }

      int y = 0;
      int travelY = fitImage.getHeight() - 320;
      int levelY = Game.cB - 320;
      if (travelY > 0 && levelY > 0) {
         int from = Game.cameraY;
         if (from < 0) {
            from = 0;
         }

         if (from > levelY) {
            from = levelY;
         }

         y = (int)((long)from * travelY / levelY);
      }

      PortNative.layerTiles(0, worldTx + left - x, -y);
      g.setColor(0);
      g.fillRect(left, 0, visible, 320);
      g.drawImage(fitImage, left - x, -y, 20);
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
