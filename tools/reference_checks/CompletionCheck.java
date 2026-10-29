import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
import jdk.internal.org.objectweb.asm.*;

/** Original exit/state-15 clocks/input. Draw hooks retain letterbox mutation;
 * finishLevel is an explicit counted boundary, not a progression/save oracle. */
public final class CompletionCheck {
    static final String[] CASES={"normal","edge","miss","point","outside","dead","zero-health","late-contact","pause","hidden","frozen","early","held","entry-pressed","boundary","down","cancel","softkey","five","wait","long-delay"};
    static short[] exitRecord(Path assets)throws Exception{
        byte[] bytes=BehaviorCheck.resource(assets,3,0);for(int pos=0;pos<bytes.length;){int count=bytes[pos++]&255;short[] r=new short[count];for(int i=0;i<count;++i){r[i]=(short)SpriteCheck.u16(bytes,pos);pos+=2;}if(r[1]==173)return r;}throw new AssertionError("Missing exit 173");
    }
    static Path copy(Path source,Path target,boolean renamed)throws Exception{
        CrystalCheck.copy(source,target,renamed);
        Path game=target.resolve(renamed?"Game.class":"e.class");String owner=renamed?"Game":"e";var writer=new ClassWriter(0);Set<String> found=new HashSet<>();
        new ClassReader(Files.readAllBytes(game)).accept(new ClassVisitor(Opcodes.ASM8,writer){
            public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){
                var v=super.visitMethod(access,name,desc,sig,exceptions);String key=name+desc;
                if(key.equals((renamed?"drawWorld":"Y")+"()V")){found.add("world");v.visitCode();v.visitMethodInsn(Opcodes.INVOKESTATIC,owner,renamed?"updateLetterbox":"ac","()V",false);v.visitInsn(Opcodes.RETURN);v.visitMaxs(0,0);v.visitEnd();return null;}
                if(key.equals((renamed?"finishLevel":"U")+"()V")){found.add("finish");v.visitCode();v.visitFieldInsn(Opcodes.GETSTATIC,owner,"completionAckCount","I");v.visitInsn(Opcodes.ICONST_1);v.visitInsn(Opcodes.IADD);v.visitFieldInsn(Opcodes.PUTSTATIC,owner,"completionAckCount","I");v.visitIntInsn(Opcodes.BIPUSH,19);v.visitFieldInsn(Opcodes.PUTSTATIC,owner,renamed?"state":"x","I");v.visitInsn(Opcodes.RETURN);v.visitMaxs(2,0);v.visitEnd();return null;}
                if(key.equals((renamed?"setClipInternal":"i")+"(IIII)V")||key.equals((renamed?"drawSoftkeys":"a")+"(Ljava/lang/String;Ljava/lang/String;)V")){found.add(key);v.visitCode();v.visitInsn(Opcodes.RETURN);v.visitMaxs(0,4);v.visitEnd();return null;}return v;
            }
            public void visitEnd(){super.visitField(Opcodes.ACC_PUBLIC|Opcodes.ACC_STATIC,"completionAckCount","I",null,0).visitEnd();super.visitEnd();}
        },0);
        if(found.size()!=4)throw new AssertionError("Completion hook mismatch "+found);Files.write(game,writer.toByteArray());
        Path sprite=target.resolve(renamed?"BSprite.class":"d.class");var sw=new ClassWriter(0);int[] count={0};new ClassReader(Files.readAllBytes(sprite)).accept(new ClassVisitor(Opcodes.ASM8,sw){
            public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] exceptions){var v=super.visitMethod(access,name,desc,sig,exceptions);if(!name.equals(renamed?"drawText":"a")||!desc.equals("(Ljavax/microedition/lcdui/Graphics;Ljava/lang/String;III)V"))return v;++count[0];v.visitCode();v.visitInsn(Opcodes.RETURN);v.visitMaxs(0,6);v.visitEnd();return null;}
        },0);if(count[0]!=1)throw new AssertionError("Completion text hook mismatch");Files.write(sprite,sw.toByteArray());return target;
    }
    static Object canvas(ActorCheck.Access c)throws Exception{Class<?> u=Class.forName("sun.misc.Unsafe");var f=u.getDeclaredField("theUnsafe");f.setAccessible(true);return u.getMethod("allocateInstance",Class.class).invoke(f.get(null),c.game);}
    static void complete(ActorCheck.Access c,Object game)throws Exception{var m=c.game.getDeclaredMethod(c.renamed?"updateLevelComplete":"J");m.setAccessible(true);m.invoke(game);}
    static String row(ActorCheck.Access c,Object t,String name,int f,int phase,int starts)throws Exception{
        var w=new java.io.StringWriter();var o=new PrintWriter(w);o.print("T\t"+name+"\t"+f+"\t"+phase);
        for(String[] n:new String[][]{{"x","state"},{"aN","aN"},{"aM","aM"},{"aQ","aQ"}})o.print("\t"+c.global(n[0],n[1]));
        o.print("\t"+starts+"\t"+c.global("completionAckCount","completionAckCount")+"\t"+c.get(t,"c","flags")+"\t"+DeathCheck.actors(c).contains(t)+"\t"+c.global("cz","pressedMask")+"\t"+c.global("cC","frameHeldMask"));o.println();return w.toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),work=Path.of(args[3]);Path original=copy(assets,work.resolve("original"),false),reference=copy(build,work.resolve("reference"),true);Path file=assets.getParent().resolve("analysis/completion-oracle.tsv");
        try(var a=new ActorCheck.Access(original,assets,stubs,false);var b=new ActorCheck.Access(reference,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String name:CASES){Object[][] roots=new Object[2][];int[] starts={0,0};
                for(var c:List.of(a,b)){c.clearWorld();c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);c.global("aS","aS",false);c.global("aO","aO",false);c.global("aP","aP",false);c.global("aN","aN",0);c.global("aM","aM",0);c.global("aQ","aQ",name.equals("long-delay")?5:2);c.global("completionAckCount","completionAckCount",0);
                    Object p=c.player(1780,780);c.set(p,"f","animationId",name.equals("dead")?9:0);if(name.equals("zero-health"))c.global("p","health",0);
                    Object t=c.create();short[] r=exitRecord(assets);c.call(t,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,0,r.length);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},t);roots[c==a?0:1]=new Object[]{t,p,canvas(c)};
                }
                for(int f=0;f<24;++f){int[] before=new int[2];
                    for(var c:List.of(a,b)){int side=c==a?0:1;Object[] r=roots[side];int state=(int)c.global("x","state");if(name.equals("pause")&&f<4)c.global("x","state",14);else if(state==14)c.global("x","state",8);
                        int press=(name.equals("early")?f==2:name.equals("boundary")?f==10:name.equals("entry-pressed")?f==0:!name.equals("held")&&!name.equals("wait")&&f==16)?(name.equals("down")?32768:name.equals("cancel")?131072:name.equals("softkey")?262144:name.equals("five")?32:65536):0;c.input(press,name.equals("held")?65536:press);
                        int[] body={1770,750,1790,780};if(name.equals("outside")||name.equals("late-contact")&&f<4)body=new int[]{0,0,10,10};if(name.equals("edge"))body=new int[]{1770,770,1780,780};if(name.equals("miss"))body=new int[]{1769,770,1779,780};if(name.equals("point"))body=new int[]{1781,771,1781,771};c.set(r[1],"j","collisionBounds",body);
                        c.set(r[0],"c","flags",136|((name.equals("hidden")||name.equals("frozen"))&&f<4?(name.equals("hidden")?1024:16384):0));
                        before[side]=(int)c.global("x","state");if(before[side]==8||before[side]==14)c.call(r[0],"s","update",ActorCheck.NONE);if(before[side]==8&&(int)c.global("x","state")==15)++starts[side];
                    }
                    for(int phase=0;phase<2;++phase){if(phase==1)for(var c:List.of(a,b)){int side=c==a?0:1;if((int)c.global("x","state")==15){BehaviorCheck.gameCall(c,"ac","updateLetterbox",ActorCheck.NONE);if(before[side]==15&&(int)c.global("aM","aM")== (int)c.global("aN","aN"))complete(c,roots[side][2]);}}
                        String value=row(a,roots[0][0],name,f,phase,starts[0]);ActorCheck.equal(value,row(b,roots[1][0],name,f,phase,starts[1]),"exit/completion "+name+"/"+f+"/"+phase);out.print(value);
                    }
                    if(f==0&&name.equals("edge"))ActorCheck.equal(15,a.global("x","state"),"inclusive exit edge activates");
                    if(f==23&&(name.equals("dead")||name.equals("point")||name.equals("miss")))ActorCheck.equal(0,starts[0],"death pose/point/miss cannot activate exit");
                }
            }
        }
        System.out.println("exit/completion: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
