// $VF: renamed from: c
final class ErrorReporter {
   // $VF: renamed from: a java.lang.Exception
   private static Exception firstException;

   // $VF: renamed from: a (java.lang.Exception, java.lang.String) void
   static final void report(Exception var0, String var1) {
      if (firstException == null) {
         firstException = var0;
      }

      System.out.println("ERROR: " + var1);
      var0.printStackTrace();
   }
}
