import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.*;
import java.util.*;

/** Type-89 original handler and animation lifecycle; controlled attack sources. */
public final class TimedSwitchCheck {
    static final String[] CASES={"idle","hit","repeat","held","fire","fire-hidden","ice","edge","point","miss","pause","hidden","frozen","mode1","mode1-closed","mode1-replaced","sticky","short","variant","latehit","initial-warning","stopped","mode1-infinite"};
    static boolean hit(String s,int f){return !s.equals("idle") && (s.equals("latehit")?f==25:s.equals("held")?f>=2&&f<10:s.equals("repeat")?f==2||f==15||f==25:f==2);}
    static String row(ActorCheck.Access c,Object a,String s,int f,int phase)throws Exception{
        var w=new StringWriter();var out=new PrintWriter(w);out.print("T\t"+s+"\t"+f+"\t"+phase);
        for(String[] n:new String[][]{{"al","uid"},{"K","positionX"},{"L","positionY"},{"c","flags"},{"f","animationId"},{"g","pendingAnimationId"},{"h","animationFrame"},{"i","animationTick"},{"ao","actionState"},{"ay","ay"},{"aC","variantData"},{"au","au"},{"as","as"},{"aw","aw"}})out.print("\t"+c.get(a,n[0],n[1]));
        for(String[] n:new String[][]{{"j","collisionBounds"},{"k","attackBounds"},{"l","visualBounds"}})for(int v:(int[])c.get(a,n[0],n[1]))out.print("\t"+v);
        out.println();return w.toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/timed-switch-oracle.tsv");int count=0;
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=Files.newBufferedWriter(file)){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES){Object[][] roots=new Object[2][];
                for(var c:List.of(a,b)){
                    c.clearWorld();Object p=c.player(100,199);c.set(null,"aM","aM",null);c.set(null,"aR","aR",null);c.set(null,"bK","activeWeapon",0);
                    short[] r={89,71,180,199,(short)(s.equals("variant")?24:25),8,(short)(s.startsWith("mode1")?1:0),(short)(s.equals("sticky")||s.equals("mode1-infinite")?-1:s.equals("short")?83:3000),72};
                    Object sw=c.create();c.call(sw,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,0,9);c.call(sw,"g","refreshBounds",ActorCheck.NONE);
                    if(s.equals("hidden"))c.set(sw,"c","flags",1032);if(s.equals("frozen"))c.set(sw,"c","flags",16392);if(s.equals("stopped"))c.set(sw,"c","flags",72);
                    if(s.equals("initial-warning")){c.call(sw,"b","setAnimationImmediate",new Class<?>[]{int.class},27);c.call(sw,"g","refreshBounds",ActorCheck.NONE);}
                    Object target=c.create();c.set(target,"al","uid",72);c.set(target,"am","type",31);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},target);
                    roots[c==a?0:1]=new Object[]{sw,p,target};
                }
                for(int f=0;f<90;++f){boolean paused=s.equals("pause")&&f>=5&&f<15;
                    for(var c:List.of(a,b)){
                        Object[] r=roots[c==a?0:1];c.global("x","state",paused?14:8);c.set(r[2],"ao","actionState",s.equals("mode1-closed")||s.equals("mode1-replaced")&&f>=10?0:f>=20?105:102);
                        if(s.equals("mode1-replaced")&&f==10){BehaviorCheck.gameCall(c,"c","removeActor",new Class<?>[]{c.actor},r[2]);Object replacement=c.create();c.set(replacement,"al","uid",72);c.set(replacement,"am","type",31);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},replacement);r[2]=replacement;}
                        c.set(null,"bK","activeWeapon",s.equals("ice")?1:0);c.set(r[1],"f","animationId",hit(s,f)?s.equals("ice")?19:13:0);
                        int[] bounds=((int[])c.get(r[0],"j","collisionBounds")).clone();
                        if(s.equals("edge"))bounds=new int[]{bounds[0]-10,bounds[1],bounds[0],bounds[3]};
                        if(s.equals("point"))bounds=new int[]{bounds[0],bounds[1],bounds[0],bounds[1]};if(s.equals("miss"))bounds=new int[]{0,0,1,1};
                        c.set(r[1],"k","attackBounds",bounds);c.set(null,"aM","aM",null);
                        if(s.startsWith("fire")){c.set(r[1],"f","animationId",0);if(hit(s,f)){Object pulse=c.create();c.set(pulse,"j","collisionBounds",bounds);if(s.equals("fire-hidden"))c.set(pulse,"c","flags",128);Object slots=Array.newInstance(c.actor,1);Array.set(slots,0,pulse);c.set(null,"aM","aM",slots);}}
                        c.call(r[0],"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase){if(phase==1&&!paused)for(var c:List.of(a,b)){Object sw=roots[c==a?0:1][0];if(((int)c.get(sw,"c","flags")&1024)==0){c.call(sw,"g","refreshBounds",ActorCheck.NONE);c.call(sw,"f","advanceAnimation",ActorCheck.NONE);}}
                        String original=row(a,roots[0][0],s,f,phase);ActorCheck.equal(original,row(b,roots[1][0],s,f,phase),"timed switch");out.write(original);++count;
                    }
                }
            }
            for(int level:new int[]{1,4}){
                short[] shared=new short[20];
                for(var record:LevelCheck.records(BehaviorCheck.resource(assets,level+3,0))){
                    System.arraycopy(record.values(),0,shared,0,record.values().length);if(shared[0]!=89)continue;
                    Object[] placed=new Object[2];for(var c:List.of(a,b)){c.clearWorld();c.global("aG","levelIndex",level);Object sw=c.create();c.call(sw,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},shared,true,record.offset(),record.values().length);c.call(sw,"g","refreshBounds",ActorCheck.NONE);placed[c==a?0:1]=sw;}
                    String original=row(a,placed[0],"placed-"+level,shared[1],0);ActorCheck.equal(original,row(b,placed[1],"placed-"+level,shared[1],0),"shipped switch construction");out.write(original);++count;
                }
            }
        }
        System.out.println("TimedSwitchCheck: "+count+" original/reference rows; "+file);
    }
}
