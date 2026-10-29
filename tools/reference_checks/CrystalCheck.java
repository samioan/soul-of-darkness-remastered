import java.nio.file.*;
import java.lang.reflect.Proxy;
import java.io.PrintWriter;
import java.util.*;
import jdk.internal.org.objectweb.asm.*;

/** Reward state oracle. Only named drawing entry points are replaced with no-ops;
 * Game.G reward/entry/input bytecode remains unchanged. Not a MIDP render test. */
public final class CrystalCheck {
    static final String GRAPH="Ljavax/microedition/lcdui/Graphics;";
    static void patch(Path file,boolean renamed,boolean game) throws Exception {
        byte[] bytes=Files.readAllBytes(file); ClassReader reader=new ClassReader(bytes); ClassWriter writer=new ClassWriter(0);
        Set<String> targets=new HashSet<>();
        if(game) {
            targets.add((renamed?"drawWorld":"Y")+"()V");
            String[] old={"a","b","b","c","d","d"},names={"setClip","setColor","fillRect","drawRect","drawLine","drawArc"};
            String[] desc={"(IIII)V","(I)V","(IIII)V","(IIII)V","(IIII)V","(IIIIII)V"};
            for(int i=0;i<old.length;++i) targets.add((renamed?names[i]:old[i])+desc[i]);
        } else {
            targets.add((renamed?"drawFrame":"a")+"("+GRAPH+"IIIIII)V");
            targets.add((renamed?"drawAnimationFrame":"a")+"("+GRAPH+"IIIIIII)V");
            targets.add((renamed?"drawWrappedText":"a")+"("+GRAPH+"Ljava/lang/String;IIIIII)I");
        }
        reader.accept(new ClassVisitor(Opcodes.ASM8,writer) {
            public MethodVisitor visitMethod(int access,String name,String descriptor,String signature,String[] exceptions) {
                MethodVisitor visitor=super.visitMethod(access,name,descriptor,signature,exceptions);
                if(!targets.remove(name+descriptor)) return visitor;
                visitor.visitCode(); boolean value=descriptor.endsWith("I");
                if(value) visitor.visitInsn(Opcodes.ICONST_0);
                visitor.visitInsn(value?Opcodes.IRETURN:Opcodes.RETURN);
                int locals=((access&Opcodes.ACC_STATIC)==0)?1:0;
                for(Type t:Type.getArgumentTypes(descriptor)) locals+=t.getSize();
                visitor.visitMaxs(value?1:0,locals); visitor.visitEnd(); return null;
            }
        },0);
        if(!targets.isEmpty()) throw new AssertionError("Unmatched graphics hooks: "+targets);
        Files.write(file,writer.toByteArray());
    }
    static Path copy(Path source,Path target,boolean renamed) throws Exception {
        Files.createDirectories(target);
        try(var stream=Files.list(source)) { for(Path p:stream.filter(p->p.toString().endsWith(".class")).toList()) Files.copy(p,target.resolve(p.getFileName()),StandardCopyOption.REPLACE_EXISTING); }
        patch(target.resolve(renamed?"Game.class":"e.class"),renamed,true);
        patch(target.resolve(renamed?"BSprite.class":"d.class"),renamed,false); return target;
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),compiled=Path.of(args[1]),stubs=Path.of(args[2]),work=Path.of(args[3]);
        Path original=copy(assets,work.resolve("original"),false),reference=copy(compiled,work.resolve("reference"),true);
        Path output=assets.getParent().resolve("analysis/crystal-oracle.tsv");
        try(var out=new PrintWriter(Files.newBufferedWriter(output))) {
            for(String scenario:new String[]{"health","fourth","cap","magic_locked","magic_unlocked","magic_fourth","early","held"}) {
                try(var a=new ActorCheck.Access(original,assets,stubs,false);var b=new ActorCheck.Access(reference,assets,stubs,true)) {
                    int kind=scenario.startsWith("magic")?1:0;
                    for(var access:List.of(a,b)) {
                        BehaviorCheck.commonAssets(access,assets); access.clearWorld();
                        access.global("p","health",30); access.global("s","magic",30);
                        access.global("r","r",scenario.equals("fourth")||scenario.equals("cap")?3:0);
                        access.global("u","u",scenario.equals("magic_fourth")?3:0);
                        if(scenario.equals("cap")) access.global("q","maxHealth",120);
                        access.set(null,"bu","bu",scenario.equals("magic_unlocked"));
                        Class<?> dg=Class.forName("com.nokia.mid.ui.DirectGraphics",true,access.sprites.loader);
                        access.global("e","directGraphics",Proxy.newProxyInstance(access.sprites.loader,new Class<?>[]{dg},(obj,method,params)->null));
                        BehaviorCheck.gameCall(access,"f","f",new Class<?>[]{int.class},10+kind);
                    }
                    for(int tick=1;tick<=56;++tick) {
                        for(var access:List.of(a,b)) {
                            int pressed=!scenario.equals("held") && tick==(scenario.equals("early")?42:51)?65536:0;
                            access.input(pressed,scenario.equals("held")?65536:0);
                            if((int)access.global("x","state")==8 && (boolean)access.global("aS","aS"))
                                BehaviorCheck.gameCall(access,"a","a",new Class<?>[]{int.class,boolean.class,int.class},0,true,0);
                            else if((int)access.global("x","state")==32)
                                BehaviorCheck.gameCall(access,"G","updateCrystalReward",new Class<?>[]{int.class},kind);
                        }
                        out.print("Q\t"+scenario+"\t"+tick);
                        for(String[] pair:new String[][]{{"x","state"},{"E","E"},{"dp","dp"},{"dq","dq"},{"r","r"},{"u","u"},{"p","health"},{"q","maxHealth"},{"s","magic"},{"t","maxMagic"},{"dr","dr"},{"aS","aS"}}) {
                            Object v=a.global(pair[0],pair[1]),w=b.global(pair[0],pair[1]);
                            if(!Objects.equals(v,w)) throw new AssertionError(scenario+"/"+tick+" "+pair[0]);
                            out.print("\t"+(v instanceof Boolean flag?(flag?1:0):v));
                        }
                        out.println();
                    }
                }
            }
        }
        System.out.println("crystal reward state: 448 original/reference snapshots; drawing hooks suppressed: "+output);
    }
}
