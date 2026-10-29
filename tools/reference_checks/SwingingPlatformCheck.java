import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/** Actual original Actor.update, with isolated player/platform fixtures; no mutable MIDP runtime. */
public final class SwingingPlatformCheck {
    static final String[] CASES={"idle","rider","always-left","always-right","slow","jump","pause","timeout","reset","cut","crush","no-cut","enemy-kill","enemy-hurt","enemy-protected"};
    static short[] record(String s){return new short[]{50,3,300,280,5,0,-100,(short)(s.equals("slow")?3:256),(short)(s.equals("slow")?1:16),30,(short)(s.startsWith("always") || s.equals("no-cut")?1:0),(short)(s.equals("always-right")?1:-1)};}
    static boolean far(String s){return s.equals("idle") || s.startsWith("always") || s.startsWith("enemy") || s.equals("cut") || s.equals("crush") || s.equals("no-cut");}
    static void row(ActorCheck.Access a,ActorCheck.Access b,Object[] x,Object[] y,PrintWriter out,String s,int f,int phase)throws Exception{
        for(int who=0;who<2;++who){
            ActorCheck.snapshot(a,b,x[who],y[who],s+" "+f+"/"+who);
            out.print((who==0?"P":"S")+"\t"+s+"\t"+f+"\t"+phase);
            for(String[] n:ActorCheck.SNAPSHOT){Object v=a.get(x[who],n[0],n[1]);if(v instanceof int[] r)for(int z:r)out.print("\t"+z);else out.print("\t"+v);}
            if(who==0){
                boolean link=a.get(null,"aK","playerPlatform")==x[1];ActorCheck.equal(link,b.get(null,"aK","playerPlatform")==y[1],"support");
                ActorCheck.equal(a.global("p","health"),b.global("p","health"),"health");
                out.println("\t"+(link?1:0)+"\t"+a.global("p","health"));
            }else{
                for(String n:new String[]{"ar","as","at","au","av","aw","ax","ay"}){Object v=a.get(x[1],n,n);ActorCheck.equal(v,b.get(y[1],n,n),n);out.print("\t"+v);}
                int[] mask=(int[])a.get(x[1],"n","moduleMask");ActorCheck.equal(mask,b.get(y[1],"n","moduleMask"),"swing mask");for(int n:mask)out.print("\t"+n);out.println();
            }
        }
        if(x.length==3){
            ActorCheck.snapshot(a,b,x[2],y[2],"fall impact enemy");out.print("E\t"+s+"\t"+f+"\t"+phase);
            for(String[] n:ActorCheck.SNAPSHOT){Object v=a.get(x[2],n[0],n[1]);if(v instanceof int[] r)for(int z:r)out.print("\t"+z);else out.print("\t"+v);}
            for(String[] n:new String[][]{{"ap","health"},{"az","az"},{"ak","facingLeft"}}){Object v=a.get(x[2],n[0],n[1]);ActorCheck.equal(v,b.get(y[2],n[0],n[1]),"impact "+n[1]);out.print("\t"+v);}out.println();
        }
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/swinging-platform-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES){Object[][] roots=new Object[2][];
                for(var c:List.of(a,b)){
                    c.clearWorld();byte[] tiles=(byte[])c.global("dL","collisionTiles");Arrays.fill(tiles,720,800,(byte)24);
                    Object p=c.player(far(s)?100:300,200);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);
                    Object m=c.create();c.call(m,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(record(s),20),true,0,12);
                    roots[c==a?0:1]=new Object[]{p,m};
                    if(s.startsWith("enemy")){
                        CombatCheck.weapons(c,0,1);Object e=CombatCheck.enemy(c,300,359);c.set(e,"ap","health",s.equals("enemy-hurt")?150:50);
                        if(s.equals("enemy-protected"))c.set(e,"c","flags",16393);
                        java.lang.reflect.Array.set(c.global("bw","updateActors"),0,e);c.global("bx","updateActorCount",1);
                        roots[c==a?0:1]=new Object[]{p,m,e};
                    }
                }
                boolean boarded=false,fallen=false,broken=false,hurt=false;
                for(int f=0;f<240;++f){boolean paused=s.equals("pause") && f>=30 && f<40;
                    for(var c:List.of(a,b)){
                        Object[] r=roots[c==a?0:1];c.input(s.equals("jump") && f==40?16388:0,0);c.global("z","stateFrame",f);c.global("x","state",paused?14:8);
                        c.call(r[0],"s","update",ActorCheck.NONE);
                        if((s.equals("cut") || s.equals("crush") || s.equals("no-cut") || s.startsWith("enemy")) && f==10){
                            c.set(r[0],"f","animationId",13);c.set(r[0],"h","animationFrame",0);c.set(r[0],"i","animationTick",0);c.set(r[0],"k","attackBounds",new int[]{297,180,303,280});
                            if(s.equals("crush")){c.set(r[0],"K","positionX",300);c.set(r[0],"L","positionY",359);c.call(r[0],"g","refreshBounds",ActorCheck.NONE);c.set(r[0],"k","attackBounds",new int[]{297,180,303,280});}
                        }
                        if(s.equals("reset") && f==60)c.call(r[1],"T","T",ActorCheck.NONE);
                        if(s.equals("timeout") && f==60){
                            c.set(r[1],"ao","actionState",101);c.set(r[1],"aw","aw",0);c.set(r[1],"at","at",0);
                            int[] mask=(int[])c.get(r[1],"n","moduleMask");mask[8]=1;mask[2]=0;mask[10]=-1;c.set(null,"aK","playerPlatform",r[1]);
                        }
                        c.call(r[1],"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase){
                        if(phase==1 && !paused)for(var c:List.of(a,b))for(int who=0;who<2;++who){Object r=roots[c==a?0:1][who];c.call(r,"g","refreshBounds",ActorCheck.NONE);c.call(r,"f","advanceAnimation",ActorCheck.NONE);}
                        row(a,b,roots[0],roots[1],out,s,f,phase);
                    }
                    boarded|=a.get(null,"aK","playerPlatform")==roots[0][1];int action=(int)a.get(roots[0][1],"ao","actionState");fallen|=action==102;broken|=action==104;hurt|=(int)a.global("p","health")<60;
                }
                if(s.equals("rider") || s.equals("slow"))ActorCheck.equal(true,boarded,s+" must board");
                if(s.equals("cut") || s.equals("crush")){ActorCheck.equal(true,fallen,s+" must fall");ActorCheck.equal(true,broken,s+" must break");}
                if(s.equals("crush"))ActorCheck.equal(true,hurt,"fall must damage player");
                if(s.equals("no-cut"))ActorCheck.equal(false,fallen,"mode 1 ignores chain attacks");
                if(s.startsWith("enemy"))ActorCheck.equal(s.equals("enemy-protected")?50:s.equals("enemy-hurt")? -50:-50,a.get(roots[0][2],"ap","health"),"falling impact health");
            }
        }
        System.out.println("swinging platforms: "+ActorCheck.comparisons+" original/reference comparisons; "+file);
    }
}

