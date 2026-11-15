final class c {
   private static Exception a;

   static final void a(Exception var0, String var1) {
      if (a == null) {
         a = var0;
      }

      System.out.println("ERROR: " + var1);
      var0.printStackTrace();
   }
}
