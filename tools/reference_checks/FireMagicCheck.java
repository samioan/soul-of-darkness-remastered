import java.io.PrintWriter;
import java.nio.file.*;
import java.lang.reflect.*;
import java.util.*;
import jdk.internal.org.objectweb.asm.*;
/** Original player/world/fire burst. Only named drawing methods are suppressed. */
public final class FireMagicCheck {
    static void arcs(Path path,boolean renamed)throws Exception{
        var writer=new ClassWriter(0);final int[] found={0};
        new ClassReader(Files.readAllBytes(path)).accept(new ClassVisitor(Opcodes.ASM8,writer){public MethodVisitor visitMethod(int access,String name,String desc,String sig,String[] ex){var v=super.visitMethod(access,name,desc,sig,ex);if(!name.equals(renamed?"fillArc":"b") || !desc.equals("(IIIIII)V"))return v;++found[0];v.visitCode();v.visitInsn(Opcodes.RETURN);v.visitMaxs(0,6);v.visitEnd();return null;}},0);
        if(found[0]!=1)throw new AssertionError("Fire drawing hook mismatch");Files.write(path,writer.toByteArray());
    }
    static String row(ActorCheck.Access c,Object p,String s,int f,int phase,Object enemy)throws Exception{
        var writer=new java.io.StringWriter();var out=new PrintWriter(writer);out.print("P\t"+s+"\t"+f+"\t"+phase);
        for(String[] n:ActorCheck.SNAPSHOT){Object v=c.get(p,n[0],n[1]);if(v instanceof int[] r)for(int x:r)out.print("\t"+x);else out.print("\t"+v);}
        out.println("\t"+c.global("s","magic")+"\t"+c.global("H","H")+"\t"+c.get(null,"aT","aT"));
        Object array=c.get(null,"aM","aM");if(array!=null)for(int i=0;i<Array.getLength(array);++i){Object e=Array.get(array,i);if(e==null)continue;out.print("B\t"+s+"\t"+f+"\t"+phase+"\t"+i);
            for(String[] n:new String[][]{{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},{"c","flags"},{"f","animationId"},{"g","pendingAnimationId"},{"h","animationFrame"},{"i","animationTick"},{"aw","aw"},{"ax","ax"},{"at","at"},{"au","au"}})out.print("\t"+c.get(e,n[0],n[1]));out.print("\t1");for(String[] n:new String[][]{{"j","collisionBounds"},{"k","attackBounds"},{"l","visualBounds"}})for(int x:(int[])c.get(e,n[0],n[1]))out.print("\t"+x);out.println();
        }
        if(enemy!=null)LiveCombatCheck.row(c,enemy,out,"E",s,f,phase);return writer.toString().replace("\tnull","\t0\t0\t0\t0");
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),work=Path.of(args[3]);Path original=CrystalCheck.copy(assets,work.resolve("original"),false),reference=CrystalCheck.copy(build,work.resolve("reference"),true);arcs(original.resolve("e.class"),false);arcs(reference.resolve("Game.class"),true);
        Path file=assets.getParent().resolve("analysis/fire-magic-oracle.tsv");
        try(var out=new PrintWriter(Files.newBufferedWriter(file))){
            for(String s:new String[]{"standing","left","held","recast","low","locked","crouch","air","pause","regen","hit-ground","hit-fly","rank1","rank2"}){
                try(var a=new ActorCheck.Access(original,assets,stubs,false);var b=new ActorCheck.Access(reference,assets,stubs,true)){
                    Object[][] roots=new Object[2][];
                    for(var c:List.of(a,b)){
                        BehaviorCheck.commonAssets(c,assets);CombatCheck.arena(c);Object p=c.player(100,199);CombatCheck.weapons(c,0,1);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);c.set(null,"bu","bu",!s.equals("locked"));c.set(null,"aT","aT",0);c.set(null,"aM","aM",null);c.set(null,"bO","bO",new int[]{s.equals("rank1")?1:s.equals("rank2")?2:0,0});c.global("H","H",-1);c.global("s","magic",s.equals("low")?14:s.equals("regen")?30:60);c.global("aS","aS",false);c.global("bc","bc",-1);c.global("bd","bd",null);c.set(null,"bE","playerInvulnerabilityMs",60000);
                        if(s.equals("left")){c.set(p,"ak","facingLeft",true);c.set(p,"c","flags",1);}
                        Class<?> dg=Class.forName("com.nokia.mid.ui.DirectGraphics",true,c.sprites.loader);c.global("e","directGraphics",Proxy.newProxyInstance(c.sprites.loader,new Class<?>[]{dg},(obj,method,params)->null));
                        Object enemy=null;if(s.startsWith("hit-")){
                            int type=s.equals("hit-fly")?2:1;int[][] stats=(int[][])c.global("bY","actorStatTables");stats[type==2?2:0][0]=80;stats[type==2?2:0][2]=0;
                            short[] r={(short)type,19,140,(short)(type==2?174:199),0,8,0,0,0,0,(short)(type==2?4:0),0,-1};byte[] stream=new byte[27];stream[0]=13;for(int i=0;i<13;++i){stream[1+2*i]=(byte)r[i];stream[2+2*i]=(byte)(r[i]>>>8);}c.global("bF","bF",stream);enemy=c.create();c.call(enemy,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,1,13);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},enemy);c.set(enemy,"ai","cameraDistanceBucket",0);
                        }roots[c==a?0:1]=new Object[]{p,enemy};
                    }
                    boolean sawBurst=false;
                    for(int f=0;f<120;++f){boolean paused=s.equals("pause") && f>=8 && f<15;
                        int pressed=!s.equals("regen") && (f==4 || (s.equals("recast") && (f==10 || f==40)) || (s.equals("low") && f==74))?1:0;
                        int held=s.equals("held") && f>=4 && f<70?1:s.equals("crouch") && f>=2 && f<30?32768:0;if(s.equals("air") && f==2)pressed=16384;
                        for(var c:List.of(a,b)){c.global("x","state",paused?14:8);c.global("z","stateFrame",f);c.input(pressed,held);BehaviorCheck.gameCall(c,"W","updateWorldActors",ActorCheck.NONE);}
                        for(int phase=0;phase<2;++phase){
                            if(phase==1 && !paused)for(var c:List.of(a,b)){
                                Object p=roots[c==a?0:1][0];if((int)c.get(p,"f","animationId")==18)BehaviorCheck.gameCall(c,"f","f",new Class<?>[]{int.class,int.class,int.class,int.class},3,100,169,80);LiveCombatCheck.paintTick(c);
                            }
                            String x=row(a,roots[0][0],s,f,phase,roots[0][1]),y=row(b,roots[1][0],s,f,phase,roots[1][1]);ActorCheck.equal(x,y,"fire world/player/pulses");out.print(x);
                        }
                        Object pulses=a.get(null,"aM","aM");sawBurst|=pulses!=null;
                        for(var c:List.of(a,b))for(Object e:DeathCheck.actors(c))c.set(e,"ai","cameraDistanceBucket",Math.abs((int)c.get(e,"K","positionX")-120)/120+Math.abs((int)c.get(e,"L","positionY")-160)/160);
                    }
                    if(s.equals("standing") || s.equals("left") || s.equals("rank1"))ActorCheck.equal(true,sawBurst,"cast produces fire pulse array");
                    if(s.equals("locked") || s.equals("crouch") || s.equals("air"))ActorCheck.equal(false,sawBurst,"cast gate rejects locked/crouched/airborne input");
                    int next=((Random)a.global("c","random")).nextInt();ActorCheck.equal(next,((Random)b.global("c","random")).nextInt(),"fire charge/hit random stream");out.println("N\t"+s+"\t"+next);
                }
            }
        }System.out.println("fire magic: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
