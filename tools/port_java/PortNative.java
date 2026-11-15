/**
 * Compile-time stand-in for the native layer markers (implemented in soul/port/engine/midp.cpp).
 * tools/gen_port.py does not translate this class. The markers label the draw calls that follow so
 * the host can interpolate presentation between simulation ticks; they never touch game state.
 */
final class PortNative {
   /** Following draws are fixed to the screen (HUD, panels, bars). */
   static void layerScreen() {
   }

   /** Following draws move with the world; (originX, originY) is where world (0,0) lands on screen. */
   static void layerWorld(int originX, int originY) {
   }

   /** The tile layer, which may scroll independently of the camera. */
   static void layerTiles(int originX, int originY) {
   }

   /** Following draws belong to one actor drawn at this screen position. */
   static void layerActor(Object actor, int screenX, int screenY) {
   }
}
