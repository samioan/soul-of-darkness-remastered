import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;

/** Isolated Game.k selection and Game.W region-change pass, with actor updates paused. */
public final class RegionCheck {
    static final String[] CASES={"outgoing","incoming","straddle","unrelated","hidden","keepflag","sameanim","count"};
    static final int[] PATH={0,100,200,201,350,600,200,100,0,201,100};
    static String row(ActorCheck.Access c,Object e,Object[] regions,String key,int frame,int phase) throws Exception {
        var w=new java.io.StringWriter();var out=new PrintWriter(w);
        out.print("R\t"+key+"\t"+frame+"\t"+phase);
        for(String field:new String[]{"au","av"}) {Object v=c.global(field,field);out.print("\t"+(v==regions[0]?0:v==regions[1]?1:-1));}
        out.print("\t"+c.global("ai","ai"));
        for(String[] n:ActorCheck.SNAPSHOT) {Object v=c.get(e,n[0],n[1]);if(v instanceof int[] r)for(int x:r)out.print("\t"+x);else out.print("\t"+v);}
        for(String[] n:new String[][]{{"ap","health"},{"di","di"},{"O","O"},{"az","az"},{"aw","aw"},{"ak","facingLeft"},{"m","customBounds"},{"aE","interactionBounds"}}) {
            Object v=c.get(e,n[0],n[1]);if(v instanceof int[] r)for(int x:r)out.print("\t"+x);else out.print("\t"+v);
        }
        out.println();return w.toString();
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]);
        Path file=assets.getParent().resolve("analysis/region-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))) {
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String key:CASES) {
                Object[] enemies=new Object[2];Object[][] regions=new Object[2][];
                for(var c:List.of(a,b)) {
                    CombatCheck.arena(c);c.player(0,199);c.global("av","av",null);c.global("ai","ai",false);
                    c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);
                    ((int[][])c.global("bY","actorStatTables"))[0][2]=key.equals("count")?2:0;
                    Object[] rr=new Object[2];
                    for(int i=0;i<2;++i) {
                        short[] r={22,(short)(210+i),(short)(i*200),0,0,8,0,0,200,400,0,-1};rr[i]=c.create();
                        c.call(rr[i],"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,0,12);
                        BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},rr[i]);
                    }
                    int x=key.equals("incoming")?300:key.equals("straddle")?200:key.equals("unrelated")?600:100;
                    short[] r={1,19,(short)x,199,0,9,-100,-125,200,125,0,0,-1};byte[] stream=new byte[27];stream[0]=13;
                    for(int i=0;i<13;++i){stream[1+2*i]=(byte)r[i];stream[2+2*i]=(byte)(r[i]>>8);}c.global("bF","bF",stream);
                    Object e=c.create();c.call(e,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,1,13);
                    BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},e);
                    c.set(e,"ap","health",-5);c.set(e,"ao","actionState",101);c.set(e,"K","positionX",x+5);c.set(e,"G","velocityX",512);c.set(e,"H","velocityY",256);c.set(e,"I","accelerationX",12);c.set(e,"J","accelerationY",34);
                    c.set(e,"az","az",117);c.set(e,"aw","aw",913);
                    if(!key.equals("sameanim"))c.call(e,"b","setAnimationImmediate",ActorCheck.INT,1);
                    if(key.equals("hidden"))c.set(e,"c","flags",1033);
                    if(key.equals("keepflag"))c.set(e,"c","flags",521);
                    c.call(e,"g","refreshBounds",ActorCheck.NONE);
                    enemies[c==a?0:1]=e;regions[c==a?0:1]=rr;
                }
                for(int frame=0;frame<PATH.length;++frame)for(int phase=0;phase<2;++phase) {
                    for(var c:List.of(a,b)) {
                        if(phase==0) {
                            Object p=c.global("bk","player");c.set(p,"K","positionX",PATH[frame]);c.global("x","state",8);
                            BehaviorCheck.gameCall(c,"k","refreshCameraRegionSelection",ActorCheck.NONE);
                        } else {c.global("x","state",14);BehaviorCheck.gameCall(c,"W","updateWorldActors",ActorCheck.NONE);}
                    }
                    String r=row(a,enemies[0],regions[0],key,frame,phase);ActorCheck.equal(r,row(b,enemies[1],regions[1],key,frame,phase),"region boundary");out.print(r);
                    if(key.equals("outgoing") && frame==3 && phase==1) {ActorCheck.equal(0,a.get(enemies[0],"ao","actionState"),"exit resets action");ActorCheck.equal(true,((int)a.get(enemies[0],"c","flags")&1024)!=0,"exit hides actor");}
                    ActorCheck.equal(3,a.global("bt","actorCount"),"region reset spawns no loot/effects");
                }
            }
        }
        System.out.println("region lifecycle: "+ActorCheck.comparisons+" original/reference checks; "+file);
    }
}
