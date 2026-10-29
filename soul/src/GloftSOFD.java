import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class GloftSOFD extends MIDlet {
   // $VF: renamed from: a GloftSOFD
   static GloftSOFD instance;
   // $VF: renamed from: b e
   static Game game = null;
   // $VF: renamed from: c javax.microedition.lcdui.Display
   static Display display = null;

   public GloftSOFD() {
      instance = this;
   }

   public final void startApp() {
      if (display == null) {
         display = Display.getDisplay(this);
      }

      if (game == null) {
         game = new Game();
         display.setCurrent(game);
         new Thread(game).start();
      } else {
         display.setCurrent(game);
         game.resumeLoop();
      }
   }

   public final void pauseApp() {
      this.notifyPaused();
      game.suspendLoop();
   }

   public final void destroyApp(boolean var1) {
      if (game != null && Game.saveLoaded) {
         Game.accessSaveRecord(true);
      }

      this.notifyDestroyed();
   }
}
