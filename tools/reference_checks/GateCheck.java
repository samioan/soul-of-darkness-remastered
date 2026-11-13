import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
import java.util.zip.CRC32;

/** Linked gate handler, fixed-point clocks/bounds and shared collision writes. */
public final class GateCheck {
    static final String[] CASES={"idle","up","down","left","right","mode9","clear","hold","two-links","late","missing","pause","hidden","frozen","long","repeat"};
    static int direction(String s){return s.equals("left")?1:s.equals("right")?2:s.equals("down")?8:s.equals("mode9")?9:4;}
    static String row(ActorCheck.Access c,Object g,String s,int f,int phase)throws Exception {
        var w=new java.io.StringWriter();var o=new PrintWriter(w);o.print("G\t"+s+"\t"+f+"\t"+phase);
        for(String[] n:new String[][]{{"al","uid"},{"K","positionX"},{"L","positionY"},{"a","fixedX"},{"b","fixedY"},{"G","velocityX"},{"H","velocityY"},{"c","flags"},{"f","animationId"},{"g","pendingAnimationId"},{"h","animationFrame"},{"i","animationTick"},{"ao","actionState"},{"ay","ay"},{"aC","variantData"},{"dh","dh"},{"as","as"},{"av","av"},{"O","O"},{"P","P"}})o.print("\t"+c.get(g,n[0],n[1]));
        for(String[] n:new String[][]{{"j","collisionBounds"},{"m","customBounds"}})for(int x:(int[])c.get(g,n[0],n[1]))o.print("\t"+x);
        for(String n:new String[]{"v","w"})o.print("\t"+(c.get(g,n,n)==null?0:1));
        var crc=new CRC32();crc.update((byte[])c.global("dL","collisionTiles"));o.println("\t"+crc.getValue());return w.toString();
    }
    static final int[][] CHAINS={{72,71,-1},{128,275,277}};
    static boolean strike(int gate,String mode,int uid,int f){
        if(mode.equals("pause"))mode=gate==72?"hit":"same";
        return switch(mode){case "hit","miss"->uid==71&&f==2;case "repeat"->uid==71&&(f==2||f==15||f==25);case "one"->uid==275&&f==2;case "both"->uid==275&&f==2||uid==277&&f==6;case "late"->uid==275&&f==2||uid==277&&f==30;case "second"->uid==277&&f==2;case "same"->(uid==275||uid==277)&&f==2;default->false;};
    }
    static String switchRow(ActorCheck.Access c,Object a,String s,int f,int phase)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("T\t"+s+"\t"+f+"\t"+phase);
        for(String[] n:new String[][]{{"al","uid"},{"K","positionX"},{"L","positionY"},{"c","flags"},{"f","animationId"},{"g","pendingAnimationId"},{"h","animationFrame"},{"i","animationTick"},{"ao","actionState"},{"ay","ay"},{"aC","variantData"},{"au","au"},{"as","as"},{"aw","aw"}})out.print("\t"+c.get(a,n[0],n[1]));
        for(String[] n:new String[][]{{"j","collisionBounds"},{"k","attackBounds"},{"l","visualBounds"}})for(int v:(int[])c.get(a,n[0],n[1]))out.print("\t"+v);
        out.println();return w.toString();
    }
    /** Shipped second-level gates 72/128 with their real type-89 switches on the real pack-4 collision map. */
    static void chains(ActorCheck.Access a,ActorCheck.Access b,Path assets,PrintWriter out)throws Exception {
        var records=LevelCheck.records(BehaviorCheck.resource(assets,4,0));
        for(int[] chain:CHAINS)for(String mode:chain[0]==72?new String[]{"idle","hit","repeat","miss","pause"}:new String[]{"idle","one","both","late","second","same","pause"}) {
            String s="level1-"+chain[0]+"-"+mode;int n=chain[2]<0?2:3;
            Object[][] roots=new Object[2][];Object[] players=new Object[2];
            for(var c:List.of(a,b)) {
                LevelCheck.setup(c,assets,4);Object p=c.player(2200,200);c.set(null,"aM","aM",null);c.set(null,"aR","aR",null);c.set(null,"bK","activeWeapon",0);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);
                Object[] actors=new Object[n];
                for(int i=0;i<n;++i) {
                    int uid=chain[i];short[] r=records.stream().filter(x->x.values()[1]==uid).findFirst().orElseThrow().values();
                    actors[i]=c.create();c.call(actors[i],"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,0,r.length);
                    if(i>0)c.call(actors[i],"g","refreshBounds",ActorCheck.NONE);
                }
                for(Object x:actors)BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},x);
                roots[c==a?0:1]=actors;players[c==a?0:1]=p;
            }
            boolean opened=false,switched=false;int firstOpen=-1;
            for(int f=0;f<120;++f) {
                boolean paused=mode.equals("pause") && f>=5 && f<15;
                for(var c:List.of(a,b)) {
                    Object[] r=roots[c==a?0:1];Object p=players[c==a?0:1];c.global("x","state",paused?14:8);
                    for(int i=0;i<n;++i) {
                        if(i>0) {
                            boolean hit=strike(chain[0],mode,chain[i],f);int[] bounds=((int[])c.get(r[i],"j","collisionBounds")).clone();
                            if(mode.equals("miss"))bounds=new int[]{0,0,1,1};
                            c.set(p,"f","animationId",hit?13:0);c.set(p,"k","attackBounds",bounds);
                        }
                        c.call(r[i],"s","update",ActorCheck.NONE);
                    }
                }
                for(int phase=0;phase<2;++phase) {
                    if(phase==1 && !paused)for(var c:List.of(a,b))for(Object x:roots[c==a?0:1])if(((int)c.get(x,"c","flags")&1024)==0){c.call(x,"g","refreshBounds",ActorCheck.NONE);c.call(x,"f","advanceAnimation",ActorCheck.NONE);}
                    String row=row(a,roots[0][0],s,f,phase);ActorCheck.equal(row,row(b,roots[1][0],s,f,phase),"level-1 gate state/tiles");out.print(row);
                    for(int i=1;i<n;++i){row=switchRow(a,roots[0][i],s,f,phase);ActorCheck.equal(row,switchRow(b,roots[1][i],s,f,phase),"level-1 gate switch");out.print(row);}
                }
                if((int)a.get(roots[0][0],"ao","actionState")==102 && !opened){opened=true;firstOpen=f;}
                for(int i=1;i<n;++i)switched|=(int)a.get(roots[0][i],"ao","actionState")==101;
            }
            boolean expectOpen=mode.equals("hit") || mode.equals("repeat") || mode.equals("same") || mode.equals("pause");
            ActorCheck.equal(expectOpen,opened,s+" gate opens only when every linked switch is struck in one tick");
            ActorCheck.equal(expectOpen || mode.equals("one") || mode.equals("both") || mode.equals("late") || mode.equals("second"),switched,s+" switch strike registers");
            if(expectOpen)ActorCheck.equal(3,firstOpen,s+" gate activates the tick after the strike");
        }
    }
    public static void main(String[] args)throws Exception {
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/gate-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))) {
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES) {
                Object[][] roots=new Object[2][];
                for(var c:List.of(a,b)) {
                    c.clearWorld();c.player(100,199);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);
                    short[] record={31,154,208,120,(short)(s.equals("long")?5:4),0,(short)direction(s),25,(short)(s.equals("two-links")?177:-1)};
                    Object g=c.create();c.call(g,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(record,20),true,0,9);
                    if(s.equals("hidden"))c.set(g,"c","flags",1024);if(s.equals("frozen"))c.set(g,"c","flags",16384);
                    Object x=c.create(),y=c.create();c.set(x,"al","uid",25);c.set(y,"al","uid",177);c.set(x,"am","type",33);c.set(y,"am","type",33);
                    if(!s.equals("late") && !s.equals("missing"))for(Object target:new Object[]{x,y})BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},target);
                    roots[c==a?0:1]=new Object[]{g,x,y};
                }
                for(int f=0;f<72;++f) {
                    boolean paused=s.equals("pause") && f>=7 && f<14;
                    for(var c:List.of(a,b)) {
                        Object[] r=roots[c==a?0:1];c.global("x","state",paused?14:8);
                        int state=f>=2 && f<36 || s.equals("repeat") && f>=50?101:0;
                        if(s.equals("idle"))state=0;
                        c.set(r[1],"ao","actionState",state);c.set(r[2],"ao","actionState",f>=8 && f<36?101:0);
                        if(s.equals("late") && f==10)BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},r[1]);
                        if(f==2 && (s.equals("clear") || s.equals("hold")))c.set(r[0],"ao","actionState",s.equals("clear")?103:104);
                        if(s.equals("mode9") && (f==5 || f==20))c.set(r[0],"L","positionY",f==5?80:120);
                        c.call(r[0],"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase) {
                        if(phase==1 && !paused)for(var c:List.of(a,b))if(((int)c.get(roots[c==a?0:1][0],"c","flags")&1024)==0){c.call(roots[c==a?0:1][0],"g","refreshBounds",ActorCheck.NONE);c.call(roots[c==a?0:1][0],"f","advanceAnimation",ActorCheck.NONE);}
                        String row=row(a,roots[0][0],s,f,phase);ActorCheck.equal(row,row(b,roots[1][0],s,f,phase),"gate state/tiles");out.print(row);
                    }
                    if(s.equals("up") && f==2)ActorCheck.equal(0,a.get(roots[0][0],"H","velocityY"),"link activation waits one tick before setting velocity");
                    if(s.equals("up") && f==3)ActorCheck.equal(-1024,a.get(roots[0][0],"H","velocityY"),"four-pixel velocity starts after integration");
                    if(s.equals("up") && f==37)ActorCheck.equal(120,a.get(roots[0][0],"L","positionY"),"return snaps to placement origin");
                    if(s.equals("idle") && f==0 || s.equals("up") && f==23 || s.equals("clear") && f==2) {
                        int filled=0;for(byte tile:(byte[])a.global("dL","collisionTiles"))if(tile==24)++filled;
                        ActorCheck.equal(s.equals("idle")?4:0,filled,"aligned doorway fills four cells then opens/clears completely");
                    }
                }
            }
            chains(a,b,assets,out);
        }
        System.out.println("linked gates: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
