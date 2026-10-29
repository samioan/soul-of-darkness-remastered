import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.nio.file.*;
import java.util.*;
import jdk.internal.org.objectweb.asm.*;

/** Original beam/tutorial clock and input, with CrystalCheck's drawing-only hooks. */
public final class AbilityModalCheck {
    static void suppressSoftkeys(Path file,boolean renamed)throws Exception{
        var writer=new ClassWriter(0);final int[] found={0};
        new ClassReader(Files.readAllBytes(file)).accept(new ClassVisitor(Opcodes.ASM8,writer){
            public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                var v=super.visitMethod(access,name,desc,sig,exceptions);
                if(!name.equals(renamed?"drawSoftkeys":"a") || !desc.equals("(Ljava/lang/String;Ljava/lang/String;)V"))return v;
                ++found[0];v.visitCode();v.visitInsn(Opcodes.RETURN);v.visitMaxs(0,2);v.visitEnd();return null;
            }
        },0);
        if(found[0]!=1)throw new AssertionError("Softkey drawing hook mismatch");Files.write(file,writer.toByteArray());
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),work=Path.of(args[3]);
        Path original=CrystalCheck.copy(assets,work.resolve("original"),false),reference=CrystalCheck.copy(build,work.resolve("reference"),true);
        suppressSoftkeys(original.resolve("e.class"),false);suppressSoftkeys(reference.resolve("Game.class"),true);
        Path file=assets.getParent().resolve("analysis/ability-modal-oracle.tsv");
        try(var out=new PrintWriter(Files.newBufferedWriter(file))){
            for(String name:new String[]{"normal","early","held","no-text","pause","down","cancel"}){
                try(var a=new ActorCheck.Access(original,assets,stubs,false);var b=new ActorCheck.Access(reference,assets,stubs,true)){
                    for(var c:List.of(a,b)){
                        BehaviorCheck.commonAssets(c,assets);c.clearWorld();c.player(180,199);c.global("ci","ci",false);c.global("aT","aT",1);c.global("aU","aU",name.equals("no-text")?null:"MAGIC AWAKENED");c.global("bb","bb",0);c.global("bd","bd",null);
                        Class<?> dg=Class.forName("com.nokia.mid.ui.DirectGraphics",true,c.sprites.loader);
                        c.global("e","directGraphics",Proxy.newProxyInstance(c.sprites.loader,new Class<?>[]{dg},(obj,method,params)->null));
                        BehaviorCheck.gameCall(c,"f","f",ActorCheck.INT,3);
                    }
                    for(int f=1;f<=72;++f){
                        boolean paused=name.equals("pause") && f>=12 && f<18;
                        int press=name.equals("held")?0:f==(name.equals("early")?42:51)?(name.equals("down")?32768:65536):0;
                        for(var c:List.of(a,b)){
                            c.input(press,name.equals("held")?65536:0);
                            if(name.equals("cancel") && f==45)c.global("aU","aU",null);
                            if(!paused && (boolean)c.global("aS","aS")){
                                if((int)c.global("bc","bc")==3){
                                    BehaviorCheck.gameCall(c,"a","a",new Class<?>[]{int.class,boolean.class,int.class},1,true,0);
                                    if((int)c.global("bc","bc")==14)BehaviorCheck.gameCall(c,"d","d",ActorCheck.NONE);
                                }else if((int)c.global("bc","bc")==14)BehaviorCheck.gameCall(c,"d","d",ActorCheck.NONE);
                            }
                        }
                        out.print("M\t"+name+"\t"+f);
                        for(String[] n:new String[][]{{"aS","aS"},{"bc","bc"},{"E","E"}}){
                            Object x=a.global(n[0],n[1]),y=b.global(n[0],n[1]);ActorCheck.equal(x,y,"ability beam/tutorial");out.print("\t"+(x instanceof Boolean v?(v?1:0):x));
                        }
                        ActorCheck.equal(a.global("aU","aU")!=null,b.global("aU","aU")!=null,"tutorial lifetime");out.println("\t"+(a.global("aU","aU")!=null?1:0));
                    }
                }
            }
        }
        System.out.println("ability modal: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
