import javax.microedition.lcdui.Graphics;

/**
 * In-game OPTIONS screen for the desktop port, drawn with the game's own font, framed panel and
 * softkey buttons so it reads as part of the original interface.
 *
 * It is reached through a menu entry (id 99) that Game.portMenus() adds to the main and pause menus
 * when the host enables it; the headless equivalence runs never install it, so the original menus
 * and their state are untouched there. The screen runs as game state 40 and returns to the state it
 * was opened from. Settings themselves live in the host (PortNative.setting*): this class only
 * lists, steps and activates them.
 *
 * Obfuscated members used: Game.a = font (BSprite; .z palette, .a(g, text, x, y, anchor) draw),
 * Game.a(x,y,w,h) setClip, Game.b(rgb) setColor, Game.b(x,y,w,h) fillRect, Game.g(x,y,w,h) framed
 * panel, Game.a(left, right) softkey buttons, Game.k(table, index) string, Game.G(mask) key pressed
 * this tick, Game.N() clear input.
 */
final class PortMenu {
   static final int STATE = 40;
   static final String TITLE = "OPTIONS";
   private static final String[] PAGES = new String[]{"DISPLAY", "AUDIO", "KEYBOARD", "GAMEPAD"};
   private static final int VISIBLE_ROWS = 12;
   private static final int ROW_HEIGHT = 19;

   static int returnState;
   static int page = -1;
   static int row;
   static int topRow;
   static int scroll;

   static void open() {
      returnState = Game.state;
      page = -1;
      row = 0;
      topRow = 0;
      scroll = 0;
      Game.state = STATE;
      Game.N();
   }

   private static void close() {
      PortNative.settingsClosed();
      Game.state = returnState;
      Game.N();
   }

   private static int count() {
      return page < 0 ? PAGES.length : PortNative.settingCount(page);
   }

   static void update() {
      Graphics g = Game.graphics;
      Game.a(0, 0, 240, 320);
      Game.b(0);
      Game.b(0, 0, 240, 320);
      Game.g(4, 6, 232, 286);
      BSprite font = Game.a;
      font.z = 0;
      font.a(g, page < 0 ? TITLE : PAGES[page], 120, 16, 17);
      int n = count();
      if (row >= n) {
         row = n > 0 ? n - 1 : 0;
      }

      if (row < scroll) {
         scroll = row;
      }

      if (row >= scroll + VISIBLE_ROWS) {
         scroll = row - VISIBLE_ROWS + 1;
      }

      for (int i = scroll; i < n && i < scroll + VISIBLE_ROWS; i++) {
         int y = 44 + (i - scroll) * ROW_HEIGHT;
         font.z = i == row ? 2 : 5;
         if (page < 0) {
            font.a(g, PAGES[i], 120, y, 17);
         } else {
            font.a(g, PortNative.settingName(page, i), 16, y, 20);
            font.a(g, PortNative.settingValue(page, i), 224, y, 24);
         }
      }

      // small arrows when the list continues beyond the panel
      font.z = 0;
      if (scroll > 0) {
         font.a(g, "+", 120, 32, 17);
      }

      if (scroll + VISIBLE_ROWS < n) {
         font.a(g, "+", 120, 44 + VISIBLE_ROWS * ROW_HEIGHT, 17);
      }

      boolean capturing = PortNative.settingCapturing();
      Game.a(capturing ? "" : Game.k(0, 18), capturing ? "" : Game.k(0, 21));
      if (capturing) {
         Game.N();
         return;
      }

      if (n > 0 && Game.G(16388)) {
         row = (row + n - 1) % n;
      } else if (n > 0 && Game.G(33024)) {
         row = (row + 1) % n;
      } else if (Game.G(131072)) {
         if (page < 0) {
            close();
         } else {
            page = -1;
            row = topRow;
            scroll = 0;
            Game.N();
         }
      } else if (page < 0) {
         if (Game.G(327712) || Game.G(8256)) {
            topRow = row;
            page = row;
            row = 0;
            scroll = 0;
            Game.N();
         }
      } else if (n > 0) {
         if (Game.G(4112)) {
            PortNative.settingChange(page, row, -1);
         } else if (Game.G(8256)) {
            PortNative.settingChange(page, row, 1);
         } else if (Game.G(327712)) {
            PortNative.settingChange(page, row, 0);
            Game.N();
         }
      }
   }
}
