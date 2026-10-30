import java.io.*;
import java.lang.reflect.Proxy;
import java.nio.file.*;
import java.util.*;
import jdk.internal.org.objectweb.asm.*;

/** Preserve Game.b(III)'s generator bytecode; capture only drawLine commands. */
public final class LightningCheck {
    static Path copy(Path source,Path target,boolean renamed)throws Exception{
        Files.createDirectories(target);try(var files=Files.list(source)){for(Path f:files.filter(p->p.toString().endsWith(".class")).toList())Files.copy(f,target.resolve(f.getFileName()),StandardCopyOption.REPLACE_EXISTING);}
        Path game=target.resolve(renamed?"Game.class":"e.class");var reader=new ClassReader(Files.readAllBytes(game));var writer=new ClassWriter(0);boolean[] patched={false};
        reader.accept(new ClassVisitor(Opcodes.ASM8,writer){public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){var v=super.visitMethod(access,name,desc,sig,exceptions);if(!name.equals(renamed?"drawLine":"d")||!desc.equals("(IIII)V"))return v;patched[0]=true;v.visitCode();for(int i=0;i<4;++i)v.visitVarInsn(Opcodes.ILOAD,i);v.visitMethodInsn(Opcodes.INVOKESTATIC,"BarrierInk","line","(IIII)V",false);v.visitInsn(Opcodes.RETURN);v.visitMaxs(4,4);v.visitEnd();return null;}},0);
        if(!patched[0])throw new AssertionError("Missing drawLine hook");Files.write(game,writer.toByteArray());return target;
    }
    static String run(ActorCheck.Access c,int seed,int top,int height,int x)throws Exception{
        Class<?> ink=Class.forName("BarrierInk",true,c.sprites.loader);var lines=(List<?>)ink.getField("lines").get(null);lines.clear();
        Class<?> direct=Class.forName("com.nokia.mid.ui.DirectGraphics",true,c.sprites.loader);
        Object proxy=Proxy.newProxyInstance(c.sprites.loader,new Class<?>[]{direct},(p,m,args)->{if(m.getName().equals("setARGBColor"))ink.getField("color").setInt(null,(int)args[0]);return null;});
        c.global("e","directGraphics",proxy);var rng=new Random(seed);c.global("c","random",rng);
        ScriptCheck.call(c,"b","b",new Class<?>[]{int.class,int.class,int.class},top,height,x);
        var text=new StringWriter();var out=new PrintWriter(text);int i=0;
        for(Object row:lines){out.print("L\t"+seed+"\t"+top+"\t"+height+"\t"+x+"\t"+i++);for(int n:(int[])row)out.print("\t"+n);out.println();}
        out.println("R\t"+seed+"\t"+top+"\t"+height+"\t"+x+"\t"+rng.nextInt());return text.toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),work=Path.of(args[3]);
        Path original=copy(assets,work.resolve("original"),false),reference=copy(build,work.resolve("reference"),true);int count=0;
        try(var a=new ActorCheck.Access(original,assets,stubs,false);var b=new ActorCheck.Access(reference,assets,stubs,true);var out=Files.newBufferedWriter(assets.getParent().resolve("analysis/lightning-oracle.tsv"))){
            for(int seed:new int[]{0,1,12345})for(int[] p:new int[][]{{0,60,100},{-20,80,100},{290,80,100},{0,0,100},{321,60,100},{-100,60,100},{0,60,0},{0,60,240},{0,60,241}}){String s=run(a,seed,p[0],p[1],p[2]);ActorCheck.equal(s,run(b,seed,p[0],p[1],p[2]),"lightning commands/RNG");out.write(s);count+=s.lines().count();}
        }
        System.out.println("LightningCheck: "+count+" original/reference drawing-command and RNG rows");
    }
}
