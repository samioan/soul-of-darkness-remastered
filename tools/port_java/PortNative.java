import javax.microedition.lcdui.Graphics;

/**
 * Compile-time stand-in for native hooks (implemented in soul/port/engine). tools/gen_port.py does
 * not translate this class.
 *
 * Layer markers label the draw calls that follow so the host can interpolate presentation between
 * simulation ticks; they never touch game state. The settings calls let the in-game options menu
 * (PortMenu) read and change host settings.
 */
final class PortNative {
   /** Following draws are fixed to the screen (HUD, panels, bars). */
   static void layerScreen() {
   }

   /** Following draws move with the world; (originX, originY) is where world (0,0) lands on screen. */
   static void layerWorld(int originX, int originY) {
   }

   /** A tile layer with its own scroll rate; id distinguishes far (0), middle (2) and main (1). */
   static void layerTiles(int id, int originX, int originY) {
   }

   /** Following draws belong to one actor drawn at this screen position. */
   static void layerActor(Object actor, int screenX, int screenY) {
   }

   /**
    * The screen outside columns [left, right) (a level narrower than the wide screen): a blurred,
    * darkened copy of the level, scaled by fade / 256. Works on the target's pixels; recorded, so
    * replayed frames redo it.
    */
   static void sides(Graphics g, int left, int right, int fade) {
   }

   /** Number of entries on a settings page (0 display, 1 audio, 2 keyboard, 3 gamepad). */
   static int settingCount(int page) {
      return 0;
   }

   static String settingName(int page, int index) {
      return null;
   }

   static String settingValue(int page, int index) {
      return null;
   }

   /** direction: -1 / +1 to step a value, 0 to activate (toggle, or start capturing a key or button). */
   static void settingChange(int page, int index, int direction) {
   }

   /** True while the host is waiting for the key or button to bind. */
   static boolean settingCapturing() {
      return false;
   }

   static void settingsClosed() {
   }
}
