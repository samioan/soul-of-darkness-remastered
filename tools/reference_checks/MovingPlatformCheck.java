import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public final class MovingPlatformCheck {
    static final String[] CASES={"rail0","rail1","rail2","rail3","rail4","rail5","rail6","rail7","rider","crouch","left","right","jump","high","low","pause","wall","ceiling"};
    static short[] record(String s) {return new short[]{33,64,300,280,9,8,-120,-180,240,220,(short)(s.startsWith("rail")?s.charAt(4)-'0':8),5,20};}
    static int pressed(String s,int f){return s.equals("jump") && f==40?16388:0;}
    static int held(String s,int f){return s.equals("crouch") && f>=30 && f<75?33024:s.equals("left") && f>=30 && f<38?4112:s.equals("right") && f>=30 && f<38?8256:0;}
    static final String[] LEVEL1_CASES={"rider","crouch","left","right","jump","high","low","pause"};
    static void levelOne(ActorCheck.Access a,ActorCheck.Access b,Path assets,PrintWriter out)throws Exception{
        var records=LevelCheck.records(BehaviorCheck.resource(assets,4,0));
        for(int uid:new int[]{219,241,288})for(String mode:LEVEL1_CASES){
            short[] record=records.stream().filter(r->r.values()[1]==uid).findFirst().orElseThrow().values();
            String name="level1-"+uid+"-"+mode;Object[][] roots=new Object[2][];
            for(var c:List.of(a,b)){
                LevelCheck.setup(c,assets,4);
                Object p=c.player(record[2],mode.equals("high")?record[3]+record[7]-60:mode.equals("low")?record[3]+record[7]+record[9]+60:record[3]-80);
                c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);
                Object m=c.create();c.call(m,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(record,20),true,0,record.length);
                roots[c==a?0:1]=new Object[]{p,m};
            }
            boolean boarded=false,ascended=false,descended=false;
            for(int f=0;f<160;++f){boolean paused=mode.equals("pause")&&f>=30&&f<40;
                for(var c:List.of(a,b)){
                    Object[] r=roots[c==a?0:1];c.input(pressed(mode,f),held(mode,f));c.global("z","stateFrame",f);c.global("x","state",paused?14:8);
                    c.call(r[0],"s","update",ActorCheck.NONE);
                    if(c.get(null,"aK","playerPlatform")==r[1]&&(boolean)c.get(r[0],"bD","ceilingHit"))c.set(r[1],"H","velocityY",0);
                    c.call(r[1],"s","update",ActorCheck.NONE);
                }
                for(int phase=0;phase<2;++phase){
                    if(phase==1&&!paused)for(var c:List.of(a,b))for(Object r:roots[c==a?0:1]){c.call(r,"g","refreshBounds",ActorCheck.NONE);c.call(r,"f","advanceAnimation",ActorCheck.NONE);}
                    row(a,b,roots[0],roots[1],out,name,f,phase);
                }
                boarded|=a.get(null,"aK","playerPlatform")==roots[0][1];int vy=(int)a.get(roots[0][1],"H","velocityY");ascended|=vy<0;descended|=vy>0;
            }
            if(mode.equals("rider")||mode.equals("crouch")||mode.equals("jump")||mode.equals("pause"))ActorCheck.equal(true,boarded,name+" must board shipped platform");
            if(mode.equals("rider"))ActorCheck.equal(true,ascended,name+" must climb");
            if(mode.equals("crouch"))ActorCheck.equal(true,descended,name+" must descend");
        }
    }
    static void row(ActorCheck.Access a,ActorCheck.Access b,Object[] x,Object[] y,PrintWriter out,String s,int f,int phase) throws Exception {
        for(int who=0;who<2;++who) {
            ActorCheck.snapshot(a,b,x[who],y[who],s+" "+f+"/"+who);
            out.print((who==0?"P":"M")+"\t"+s+"\t"+f+"\t"+phase);
            for(String[] n:ActorCheck.SNAPSHOT){Object v=a.get(x[who],n[0],n[1]);if(v instanceof int[] r)for(int z:r)out.print("\t"+z);else out.print("\t"+v);}
            if(who==0){
                boolean link=a.get(null,"aK","playerPlatform")==x[1];ActorCheck.equal(link,b.get(null,"aK","playerPlatform")==y[1],"support");
                ActorCheck.equal(a.global("p","health"),b.global("p","health"),"health");
                out.println("\t"+(link?1:0)+"\t"+a.global("p","health"));
            }else{
                for(String n:new String[]{"ax","aw"}){Object v=a.get(x[1],n,n);ActorCheck.equal(v,b.get(y[1],n,n),n);out.print("\t"+v);}out.println();
            }
        }
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]);
        Path file=assets.getParent().resolve("analysis/moving-platform-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES){Object[][] roots=new Object[2][];
                for(var c:List.of(a,b)){
                    c.clearWorld();byte[] tiles=(byte[])c.global("dL","collisionTiles");Arrays.fill(tiles,720,800,(byte)24);
                    if(s.equals("wall"))for(int r=0;r<18;++r)tiles[r*40+16]=24;
                    if(s.equals("ceiling"))Arrays.fill(tiles,160,200,(byte)24);
                    Object p=c.player(s.startsWith("rail")?100:300,s.equals("high")?80:s.equals("low")?340:200);
                    c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);
                    Object m=c.create();c.call(m,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(record(s),20),true,0,13);
                    roots[c==a?0:1]=new Object[]{p,m};
                }
                boolean boarded=false;
                for(int f=0;f<160;++f){boolean paused=s.equals("pause") && f>=30 && f<40;
                    for(var c:List.of(a,b)){
                        Object[] r=roots[c==a?0:1];c.input(pressed(s,f),held(s,f));c.global("z","stateFrame",f);c.global("x","state",paused?14:8);
                        c.call(r[0],"s","update",ActorCheck.NONE);
                        if(c.get(null,"aK","playerPlatform")==r[1] && (boolean)c.get(r[0],"bD","ceilingHit"))c.set(r[1],"H","velocityY",0);
                        c.call(r[1],"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase){
                        if(phase==1 && !paused)for(var c:List.of(a,b))for(Object r:roots[c==a?0:1]){c.call(r,"g","refreshBounds",ActorCheck.NONE);c.call(r,"f","advanceAnimation",ActorCheck.NONE);}
                        row(a,b,roots[0],roots[1],out,s,f,phase);
                    }
                    boarded|=a.get(null,"aK","playerPlatform")==roots[0][1];
                }
                if(s.equals("rider") || s.equals("crouch") || s.equals("jump"))ActorCheck.equal(true,boarded,s+" must board");
            }
            levelOne(a,b,assets,out);
        }
        System.out.println("moving platforms: "+ActorCheck.comparisons+" original/reference comparisons; "+file);
    }
}
