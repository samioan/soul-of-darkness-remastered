import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class GloftSOFD extends MIDlet {
   static GloftSOFD a;
   static e b = null;
   static Display c = null;

   public GloftSOFD() {
      a = this;
   }

   public final void startApp() {
      if (c == null) {
         c = Display.getDisplay(this);
      }

      if (b == null) {
         b = new e();
         c.setCurrent(b);
         new Thread(b).start();
      } else {
         c.setCurrent(b);
         b.d();
      }
   }

   public final void pauseApp() {
      this.notifyPaused();
      b.e();
   }

   public final void destroyApp(boolean var1) {
      if (b != null && e.du) {
         e.b(true);
      }

      this.notifyDestroyed();
   }
}
