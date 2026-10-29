import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.file.*;
import java.util.*;

/** Original type-40 region-overlap/force-update branch; target handlers isolated. */
public final class LinkedTriggerCheck {
    static final String[] CASES={"overlap","outside","left-edge","right-edge","corner","miss","paused","hidden","frozen","late","missing","preserved"};
    static int[] region(String s,int f) {
        if(s.equals("outside") || f>=8 && f<16 && !(s.endsWith("edge") || s.equals("corner") || s.equals("miss")))return new int[]{0,0,199,200};
        if(s.equals("left-edge"))return new int[]{0,0,200,200};
        if(s.equals("right-edge"))return new int[]{223,0,400,200};
        if(s.equals("corner"))return new int[]{223,223,400,400};
        if(s.equals("miss"))return new int[]{224,223,400,400};
        return new int[]{0,0,400,400};
    }
    static String row(ActorCheck.Access c,Object t,Object[] targets,String s,int f)throws Exception {
        var w=new java.io.StringWriter();var o=new PrintWriter(w);o.print("T\t"+s+"\t"+f);
        for(String[] n:new String[][]{{"al","uid"},{"K","positionX"},{"L","positionY"},{"c","flags"}})o.print("\t"+c.get(t,n[0],n[1]));
        for(int n:(int[])c.get(t,"j","collisionBounds"))o.print("\t"+n);
        for(String n:new String[]{"v","w","x"})o.print("\t"+(c.get(t,n,n)==null?0:1));
        for(Object target:targets)o.print("\t"+c.get(target,"c","flags"));o.println();return w.toString();
    }
    static List<short[]> worldRecords(String s) {
        short[] a={22,210,0,0,0,8,0,0,200,400,0,-1},b={22,211,400,0,0,8,0,0,400,400,0,-1};
        short[] t={40,182,100,200,0,8,0,0,23,23,0,0,2,-1,-1,-1,25,177,-1};
        short[] x={50,25,600,199,5,16416,-100,256,16,30,1,-1},y=x.clone();y[1]=177;y[2]=650;
        if(s.equals("trigger-first"))return List.of(t,a,b,x,y);
        if(s.equals("platform-first"))return List.of(x,y,a,b,t);
        return List.of(a,b,t,x,y);
    }
    static void worldChecks(ActorCheck.Access a,ActorCheck.Access b,PrintWriter out)throws Exception {
        for(String s:new String[]{"region-first","trigger-first","platform-first","world-paused"}) {
            Object[][] roots=new Object[2][5];
            for(var c:List.of(a,b)) {
                CombatCheck.arena(c);Object p=c.player(100,199);c.set(p,"c","flags",16384);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);
                c.global("ai","ai",false);c.global("av","av",null);c.global("aj","cameraControllerActive",false);
                for(short[] record:worldRecords(s)) {
                    Object e=c.create();c.call(e,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(record,20),true,0,record.length);
                    BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},e);
                    int slot=record[1]==210?0:record[1]==211?1:record[1]==182?2:record[1]==25?3:4;roots[c==a?0:1][slot]=e;
                }
                c.global("au","au",roots[c==a?0:1][0]);WorldOrderCheck.buckets(c);
            }
            for(int f=0;f<32;++f) {
                String expected=null;
                for(var c:List.of(a,b)) {
                    Object[] r=roots[c==a?0:1];Object p=c.global("bk","player");int px=f/8%2==0?100:600;
                    c.set(p,"K","positionX",px);c.set(p,"a","fixedX",px*256);c.call(p,"g","refreshBounds",ActorCheck.NONE);
                    c.global("x","state",s.equals("world-paused") && f>=8 && f<12?14:8);c.input(0,0);
                    BehaviorCheck.gameCall(c,"W","updateWorldActors",ActorCheck.NONE);
                    var w=new java.io.StringWriter();var o=new PrintWriter(w);o.print("Q\t"+s+"\t"+f+"\t"+(c.global("au","au")==r[0]?0:1)+"\t"+c.global("ai","ai"));
                    o.print("\t"+row(c,r[2],new Object[]{r[3],r[4]},s,f).split("\\t",4)[3].strip());
                    int count=(int)c.global("bx","updateActorCount");o.print("\t"+count);for(int i=0;i<count;++i)o.print("\t"+c.get(Array.get(c.global("bw","updateActors"),i),"al","uid"));o.println();
                    String value=w.toString();if(c==a)expected=value;else ActorCheck.equal(expected,value,"world trigger snapshot/queue ordering");
                }
                out.print(expected);
            }
        }
    }
    public static void main(String[] args)throws Exception {
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/linked-trigger-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))) {
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES) {
                Object[][] roots=new Object[2][];
                for(var c:List.of(a,b)) {
                    c.clearWorld();c.player(100,199);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);
                    Object r=c.create();c.set(r,"am","type",22);c.global("au","au",r);
                    short[] record={40,182,200,200,0,8,0,0,23,23,0,0,2,-1,-1,-1,25,177,-1};Object t=c.create();
                    c.call(t,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(record,20),true,0,19);
                    if(s.equals("hidden"))c.set(t,"c","flags",1160);if(s.equals("frozen"))c.set(t,"c","flags",16520);
                    Object x=c.create(),y=c.create();c.set(x,"al","uid",25);c.set(y,"al","uid",177);c.set(x,"am","type",50);c.set(y,"am","type",50);
                    c.set(x,"c","flags",s.equals("preserved")?48:160);c.set(y,"c","flags",17760);
                    if(!s.equals("late") && !s.equals("missing"))for(Object target:new Object[]{x,y})BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},target);
                    roots[c==a?0:1]=new Object[]{t,r,x,y};
                }
                for(int f=0;f<32;++f) {
                    for(var c:List.of(a,b)) {
                        Object[] r=roots[c==a?0:1];c.global("x","state",s.equals("paused") && f>=4 && f<12?14:8);
                        c.set(r[1],"j","collisionBounds",region(s,f));
                        if(s.equals("late") && f==10)for(int i=2;i<4;++i)BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},r[i]);
                        c.call(r[0],"s","update",ActorCheck.NONE);
                    }
                    String row=row(a,roots[0][0],new Object[]{roots[0][2],roots[0][3]},s,f);
                    ActorCheck.equal(row,row(b,roots[1][0],new Object[]{roots[1][2],roots[1][3]},s,f),"linked trigger flags/bounds/references");out.print(row);
                    if(s.equals("overlap") && (f==0 || f==8 || f==16))ActorCheck.equal(f==8?160:144,a.get(roots[0][2],"c","flags"),"region overlap toggles forced update, preserving hidden draw bit");
                }
            }
            worldChecks(a,b,out);
        }
        System.out.println("linked area triggers: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
