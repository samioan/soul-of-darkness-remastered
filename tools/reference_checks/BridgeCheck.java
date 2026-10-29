import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.file.*;
import java.util.*;
import java.util.zip.CRC32;

/** Type-45 timers, mutable collision cells and type-50 impact, using original handlers. */
public final class BridgeCheck {
    static final String[] CASES={"idle","activate","late","pause","long","fall","fall-before","fall-long","miss","edge","hidden"};
    static boolean falling(String s){return s.startsWith("fall") || s.equals("miss") || s.equals("edge") || s.equals("hidden");}
    static String bridgeRow(ActorCheck.Access c,Object b,String s,int f,int phase)throws Exception {
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("B\t"+s+"\t"+f+"\t"+phase);
        for(String[] n:new String[][]{{"al","uid"},{"K","positionX"},{"L","positionY"},{"c","flags"},{"f","animationId"},{"g","pendingAnimationId"},{"h","animationFrame"},{"i","animationTick"},{"ao","actionState"},{"aw","aw"},{"ax","ax"},{"au","au"}})out.print("\t"+c.get(b,n[0],n[1]));
        out.print("\t"+(DeathCheck.actors(c).contains(b)?1:0));int[] bounds=(int[])c.get(b,"j","collisionBounds");for(int n:bounds==null?new int[4]:bounds)out.print("\t"+n);
        var crc=new CRC32();crc.update((byte[])c.global("dL","collisionTiles"));out.println("\t"+crc.getValue());return w.toString();
    }
    static String swingRow(ActorCheck.Access c,Object m,String s,int f,int phase)throws Exception {
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("S\t"+s+"\t"+f+"\t"+phase);
        for(String[] n:ActorCheck.SNAPSHOT){Object v=c.get(m,n[0],n[1]);if(v instanceof int[] r)for(int x:r)out.print("\t"+x);else out.print("\t"+v);}
        for(String n:new String[]{"ar","as","at","au","av","aw","ax","ay"})out.print("\t"+c.get(m,n,n));
        for(int n:(int[])c.get(m,"n","moduleMask"))out.print("\t"+n);out.println();return w.toString();
    }
    public static void main(String[] args)throws Exception {
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/bridge-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))) {
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES) {
                Object[][] roots=new Object[2][];
                for(var c:List.of(a,b)) {
                    c.clearWorld();Arrays.fill((byte[])c.global("dL","collisionTiles"),720,800,(byte)24);c.player(100,199);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);
                    short[] r={45,42,200,320,(short)(s.equals("long") || s.equals("fall-long")?1:0),8,0,0,200,20};Object bridge=c.create();
                    c.call(bridge,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,0,10);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},bridge);
                    Object m=null;
                    if(falling(s)) {
                        short[] mr={50,3,(short)(s.equals("miss")?500:s.equals("edge")?400:300),280,5,0,-100,256,16,30,0,-1};m=c.create();
                        c.call(m,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(mr,20),true,0,12);
                        Array.set(c.global("bw","updateActors"),0,bridge);c.global("bx","updateActorCount",1);
                    }
                    if(s.equals("hidden"))c.set(bridge,"c","flags",1032);
                    roots[c==a?0:1]=new Object[]{bridge,m};
                }
                boolean collapsed=false;
                for(int f=0;f<60;++f) {
                    boolean paused=s.equals("pause") && f>=4 && f<10;
                    for(var c:List.of(a,b)) {
                        Object bridge=roots[c==a?0:1][0],m=roots[c==a?0:1][1];c.global("x","state",paused?14:8);
                        if(f==(s.equals("late")?25:3) && !s.equals("idle")) {
                            if(m!=null)c.set(m,"ao","actionState",102);
                            else {c.set(bridge,"au","au",300);c.call(bridge,"a","setCollisionRectEmpty",new Class<?>[]{int[].class,boolean.class},c.get(bridge,"j","collisionBounds"),true);c.set(bridge,"ao","actionState",100);}
                        }
                        if(m!=null && !s.equals("fall-before"))c.call(m,"s","update",ActorCheck.NONE);
                        if(DeathCheck.actors(c).contains(bridge))c.call(bridge,"s","update",ActorCheck.NONE);
                        if(m!=null && s.equals("fall-before"))c.call(m,"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase) {
                        if(phase==1 && !paused)for(var c:List.of(a,b))for(Object r:roots[c==a?0:1])if(r!=null && (r==roots[c==a?0:1][1] || DeathCheck.actors(c).contains(r)) && ((int)c.get(r,"c","flags")&1024)==0){c.call(r,"g","refreshBounds",ActorCheck.NONE);c.call(r,"f","advanceAnimation",ActorCheck.NONE);}
                        String row=bridgeRow(a,roots[0][0],s,f,phase);ActorCheck.equal(row,bridgeRow(b,roots[1][0],s,f,phase),"bridge state/cells");out.print(row);
                        if(roots[0][1]!=null){row=swingRow(a,roots[0][1],s,f,phase);ActorCheck.equal(row,swingRow(b,roots[1][1],s,f,phase),"bridge impact motion");out.print(row);}
                    }
                    collapsed|=!DeathCheck.actors(a).contains(roots[0][0]);
                }
                ActorCheck.equal(!s.equals("idle") && !s.equals("miss") && !s.equals("hidden"),collapsed,"bridge must reach expected removal");
            }
        }
        System.out.println("breakable bridges: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
