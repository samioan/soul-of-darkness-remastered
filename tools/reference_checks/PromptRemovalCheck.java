import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;

/** Actual first-level prompt-removal records; source motion isolated. */
public final class PromptRemovalCheck {
    static final String[] CASES={"normal","outside","edge","miss","source-hidden","pause","hidden","frozen","late-source","late-target","missing-source","missing-target","target-removed"};
    static short[] record(Path assets,int uid)throws Exception{
        var data=new java.io.DataInputStream(new java.io.ByteArrayInputStream(BehaviorCheck.resource(assets,3,0)));
        while(data.available()>0){int n=data.readUnsignedByte();short[] r=new short[n];for(int i=0;i<n;++i)r[i]=Short.reverseBytes(data.readShort());if(r[1]==uid)return r;}throw new Exception("Missing record");
    }
    static String row(ActorCheck.Access c,Object[] r,String s,int uid,int f)throws Exception{
        var w=new java.io.StringWriter();var o=new PrintWriter(w);o.print("T\t"+uid+"\t"+s+"\t"+f+"\t"+DeathCheck.actors(c).contains(r[0])+"\t"+c.get(r[0],"c","flags"));
        Object bounds=c.get(r[0],"j","collisionBounds");for(int n:bounds==null?new int[4]:(int[])bounds)o.print("\t"+n);
        for(String n:new String[]{"s","v"})o.print("\t"+(c.get(r[0],n,n)==null?0:1));
        o.print("\t"+DeathCheck.actors(c).contains(r[2]));bounds=c.get(r[2],"j","collisionBounds");for(int n:bounds==null?new int[4]:(int[])bounds)o.print("\t"+n);o.println();return w.toString();
    }
    static void worlds(ActorCheck.Access a,ActorCheck.Access b,PrintWriter out)throws Exception{
        for(String name:new String[]{"prompt-first","trigger-first","world-pause","disabled-target"}){
            Object[][] roots=new Object[2][];
            short[] region={22,210,0,0,0,8,0,0,800,400,0,-1},platform={50,34,500,199,5,16384,-140,2,1,30,1,-1},prompt={21,59,110,199,14,8,-60,0,120,120},trigger={40,61,100,199,0,8,0,0,80,120,1,0,5,34,-1,-1,59,-1,-1};
            if(name.equals("disabled-target"))prompt[5]|=32;
            for(var c:List.of(a,b)){
                CombatCheck.arena(c);Object p=c.player(100,199);c.set(p,"c","flags",16384);c.call(p,"g","refreshBounds",ActorCheck.NONE);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);c.global("bD","bD",new byte[300]);
                c.global("av","av",null);c.global("ai","ai",false);c.global("aj","cameraControllerActive",false);
                Object[] r={c.create(),c.create(),c.create(),c.create()};short[][] rs={trigger,platform,prompt,region};
                for(int i=0;i<4;++i)c.call(r[i],"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(rs[i],20),true,0,rs[i].length);
                for(int i:name.equals("trigger-first")?new int[]{3,0,2,1}:new int[]{3,2,0,1})BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},r[i]);
                c.global("au","au",r[3]);WorldOrderCheck.buckets(c);roots[c==a?0:1]=r;
            }
            for(int f=0;f<10;++f){String expected=null;
                for(var c:List.of(a,b)){Object[] r=roots[c==a?0:1];boolean paused=name.equals("world-pause")&&f>=3&&f<7;c.global("x","state",paused?14:8);
                    if(f==4){c.call(r[1],"g","refreshBounds",ActorCheck.NONE);int[] rect=(int[])c.get(r[1],"j","collisionBounds");c.set(r[1],"K","positionX",(int)c.get(r[1],"K","positionX")+100-rect[0]);c.set(r[1],"L","positionY",(int)c.get(r[1],"L","positionY")+199-rect[1]);}
                    c.input(0,0);BehaviorCheck.gameCall(c,"W","updateWorldActors",ActorCheck.NONE);
                    var w=new java.io.StringWriter();var o=new PrintWriter(w);o.print("W\t"+name+"\t"+f+"\t"+DeathCheck.actors(c).contains(r[0])+"\t"+DeathCheck.actors(c).contains(r[2])+"\t"+c.get(r[2],"aw","aw"));
                    int count=(int)c.global("bx","updateActorCount");o.print("\t"+count);for(int i=0;i<count;++i){Object actor=java.lang.reflect.Array.get(c.global("bw","updateActors"),i);o.print("\t"+(actor==null?-999:c.get(actor,"al","uid")));}o.println();
                    String value=w.toString();if(c==a)expected=value;else ActorCheck.equal(expected,value,"prompt removal world queue");
                }out.print(expected);
            }
        }
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/prompt-removal-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(int uid:new int[]{43,61})for(String s:CASES){
                short[] t=record(assets,uid);Object[][] roots=new Object[2][];
                for(var c:List.of(a,b)){
                    c.clearWorld();c.player(10,10);c.global("bD","bD",new byte[300]);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);
                    Object[] r={c.create(),c.create(),c.create()};short[][] rs={t,record(assets,t[13]),record(assets,t[16])};
                    for(int i=0;i<3;++i)c.call(r[i],"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(rs[i],20),true,0,rs[i].length);
                    BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},r[0]);
                    if(!s.equals("late-source")&&!s.equals("missing-source"))BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},r[1]);
                    if(!s.equals("late-target")&&!s.equals("missing-target"))BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},r[2]);
                    if(s.equals("hidden")||s.equals("frozen"))c.set(r[0],"c","flags",136|(s.equals("hidden")?1024:16384));
                    if(s.equals("source-hidden"))c.set(r[1],"c","flags",1024);
                    roots[c==a?0:1]=r;
                }
                for(int f=0;f<12;++f){
                    for(var c:List.of(a,b)){Object[] r=roots[c==a?0:1];
                        c.global("x","state",s.equals("pause")&&f>=3&&f<7?14:8);
                        if(f==6&&s.startsWith("late-"))BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},r[s.equals("late-source")?1:2]);
                        if(f==2&&s.equals("target-removed"))BehaviorCheck.gameCall(c,"c","removeActor",new Class<?>[]{c.actor},r[2]);
                        c.call(r[1],"g","refreshBounds",ActorCheck.NONE);int[] rect=(int[])c.get(r[1],"j","collisionBounds");
                        int dx=t[2]+t[6]-(s.equals("edge")||s.equals("miss")?rect[2]:rect[0]);
                        int dy=t[3]+t[7]-rect[1];
                        if(f<4||s.equals("outside"))dx+=500;if(s.equals("miss"))dx--;
                        c.set(r[1],"K","positionX",(int)c.get(r[1],"K","positionX")+dx);c.set(r[1],"L","positionY",(int)c.get(r[1],"L","positionY")+dy);
                        if(DeathCheck.actors(c).contains(r[0]))c.call(r[0],"s","update",ActorCheck.NONE);
                    }
                    String value=row(a,roots[0],s,uid,f);ActorCheck.equal(value,row(b,roots[1],s,uid,f),"prompt linked removal "+uid+"/"+s+"/"+f);out.print(value);
                    if(f==4&&(s.equals("normal")||s.equals("edge")||s.equals("source-hidden")))ActorCheck.equal(false,DeathCheck.actors(a).contains(roots[0][0]),"inclusive overlap removes trigger");
                    if(f==11&&(s.equals("outside")||s.equals("miss")||s.equals("missing-source")))ActorCheck.equal(true,DeathCheck.actors(a).contains(roots[0][0]),"non-overlap retains trigger");
                }
            }
            worlds(a,b,out);
        }
        System.out.println("prompt removal: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
