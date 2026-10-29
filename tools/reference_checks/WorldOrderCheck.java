import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.file.*;
import java.util.*;

/** Actual original world dispatch with live enemies and region records interleaved. */
public final class WorldOrderCheck {
    static final String[] CASES={"region-first","enemy-first","interleaved","overlap-first","overlap-last","gap","paused","no-region","edge","hidden-region","flags-region","distant"};
    static boolean overlap(String s){return s.startsWith("overlap");}
    static int px(String s,int f) {
        if(overlap(s))return 150;
        if(s.equals("gap"))return new int[]{100,180,200,220,221,300,100}[Math.min(6,f/6)];
        if(s.equals("edge"))return new int[]{1,200,201,400,0}[f/8];
        if(s.equals("distant"))return f<8?700:100;
        return f/8%2==0?100:350;
    }
    static List<short[]> records(String s) {
        short[] a={22,210,0,0,0,(short)(s.equals("hidden-region")?1032:s.equals("flags-region")?40:8),0,0,(short)(s.equals("gap")?180:overlap(s)?300:200),400,0,-1};
        short[] b={22,211,(short)(s.equals("gap")?220:overlap(s)?100:200),0,0,8,0,0,200,400,0,-1};
        short[] c={22,212,50,0,0,8,0,0,350,400,0,-1};
        short[] e={1,19,195,199,0,9,-100,-125,200,125,0,0,-1},f=e.clone();f[1]=20;f[2]=300;
        if(s.equals("no-region"))return List.of(e,f);
        if(s.equals("enemy-first"))return List.of(e,f,a,b);
        if(s.equals("interleaved") || s.equals("paused"))return List.of(e,a,f,b);
        if(s.equals("overlap-last"))return List.of(e,a,f,b,c);
        if(overlap(s))return List.of(a,b,c,e,f);
        return List.of(a,b,e,f);
    }
    static void buckets(ActorCheck.Access c) throws Exception {
        Object p=c.global("bk","player");int x=(int)c.get(p,"K","positionX"),y=(int)c.get(p,"L","positionY");
        int cx=Math.max(0,Math.min(560,x-120)),cy=Math.max(0,Math.min(80,y-220));
        for(Object e:DeathCheck.actors(c))c.set(e,"ai","cameraDistanceBucket",Math.abs((int)c.get(e,"K","positionX")-(cx+120))/120+Math.abs((int)c.get(e,"L","positionY")-(cy+160))/160);
    }
    static String rows(ActorCheck.Access c,Object[] roots,List<Object> regions,String s,int f,int phase,boolean cleanup)throws Exception {
        var w=new java.io.StringWriter();var out=new PrintWriter(w);
        out.print("P\t"+s+"\t"+f+"\t"+phase);
        for(String[] n:ActorCheck.SNAPSHOT){Object v=c.get(roots[0],n[0],n[1]);if(v instanceof int[] r)for(int x:r)out.print("\t"+x);else out.print("\t"+v);}out.println();
        for(int i=1;i<3;++i){var ew=new java.io.StringWriter();var eo=new PrintWriter(ew);EnemyCheck.row(c,roots[i],eo,s,f,phase);String er=ew.toString();out.print((i==1?"E":"E2")+er.substring(1));}
        out.print("W\t"+s+"\t"+f+"\t"+phase+"\t"+regions.indexOf(c.global("au","au"))+"\t"+regions.indexOf(c.global("av","av"))+"\t"+c.global("ai","ai")+"\t"+cleanup);
        for(String n:new String[]{"R","T","S","U"})out.print("\t"+c.global(n,n));
        out.print("\t"+c.global("p","health")+"\t"+c.get(null,"bE","playerInvulnerabilityMs"));
        int count=(int)c.global("bx","updateActorCount");out.print("\t"+count);
        for(int i=0;i<count;++i)out.print("\t"+c.get(Array.get(c.global("bw","updateActors"),i),"al","uid"));out.println();return w.toString();
    }
    public static void main(String[] args)throws Exception {
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/world-order-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))) {
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES) {
                Object[][] roots=new Object[2][3];List<List<Object>> regions=new ArrayList<>();
                for(var c:List.of(a,b)) {
                    CombatCheck.arena(c);Object p=c.player(px(s,0),199);c.set(p,"c","flags",16384);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);CombatCheck.weapons(c,0,1);
                    c.set(null,"bE","playerInvulnerabilityMs",60000);c.global("av","av",null);c.global("ai","ai",false);c.global("aj","cameraControllerActive",false);
                    for(String n:new String[]{"R","S","T","U"})c.global(n,n,0);
                    roots[c==a?0:1][0]=p;var rr=new ArrayList<Object>();var stream=new java.io.ByteArrayOutputStream();
                    for(short[] r:records(s)) {
                        stream.write(r.length);int offset=stream.size();for(short x:r){stream.write(x&255);stream.write((x>>8)&255);}c.global("bF","bF",stream.toByteArray());
                        Object e=c.create();c.call(e,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,offset,r.length);
                        BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},e);
                        if(r[0]==22)rr.add(e);else {roots[c==a?0:1][r[1]==19?1:2]=e;c.set(e,"ao","actionState",101);if(s.equals("distant"))c.set(e,"c","flags",(int)c.get(e,"c","flags")|64);}
                    }
                    regions.add(rr);buckets(c);
                }
                for(int f=0;f<40;++f) {
                    boolean paused=s.equals("paused") && f>=8 && f<12;boolean[] cleanup=new boolean[2];
                    for(var c:List.of(a,b)) {
                        int id=c==a?0:1;Object p=roots[id][0];int x=px(s,f);c.set(p,"K","positionX",x);c.set(p,"a","fixedX",x*256);c.call(p,"g","refreshBounds",ActorCheck.NONE);
                        int cx=Math.max(0,Math.min(560,x-120));c.global("aa","viewBounds",new int[]{cx,0,cx+240,320});c.global("x","state",paused?14:8);c.input(0,0);cleanup[id]=(boolean)c.global("ai","ai");
                        BehaviorCheck.gameCall(c,"W","updateWorldActors",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase) {
                        if(phase==1 && !paused)for(var c:List.of(a,b))LiveCombatCheck.paintTick(c);
                        String row=rows(a,roots[0],regions.get(0),s,f,phase,cleanup[0]);ActorCheck.equal(row,rows(b,roots[1],regions.get(1),s,f,phase,cleanup[1]),"ordered world tick");out.print(row);
                    }
                    if(!paused)for(var c:List.of(a,b))buckets(c);
                    if(f==8 && (s.equals("region-first") || s.equals("enemy-first")))ActorCheck.equal(s.equals("region-first")?256:0,a.get(roots[0][1],"G","velocityX"),"same-tick region/enemy order must affect clamp");
                    if(overlap(s))ActorCheck.equal(false,a.global("au","au")==regions.get(0).get(2),"first changed region suppresses later regions");
                }
            }
        }
        System.out.println("ordered world/regions: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
