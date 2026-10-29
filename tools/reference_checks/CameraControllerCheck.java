import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;

/** Level-zero type-49 modes 0/1 and the selected active camera easing path. */
public final class CameraControllerCheck {
    static final String[] CASES={"normal","stationary","outside","edge","point","missing","late","removed","source-hidden","pause","hidden","frozen","region-change","other-active","far-source","upward"};
    static short[] record(Path assets,int uid)throws Exception{return PromptRemovalCheck.record(assets,uid);}
    static String row(ActorCheck.Access c,Object[] r,int uid,String name,int f,int phase)throws Exception{
        var w=new java.io.StringWriter();var o=new PrintWriter(w);o.print("C\t"+uid+"\t"+name+"\t"+f+"\t"+phase+"\t"+DeathCheck.actors(c).contains(r[0])+"\t"+c.get(r[0],"c","flags")+"\t"+c.get(r[0],"aB","aB")+"\t"+(c.get(r[0],"v","v")==null?0:1));
        int[] rect=(int[])c.get(r[0],"j","collisionBounds");for(int n:rect==null?new int[4]:rect)o.print("\t"+n);
        for(String[] n:new String[][]{{"aj","cameraControllerActive"},{"am","activeCameraControllerUid"},{"ak","cameraControllerMode"},{"L","cameraX"},{"M","cameraY"},{"N","cameraTargetX"},{"O","cameraTargetY"},{"P","P"},{"Q","Q"},{"R","R"},{"T","T"},{"S","S"},{"U","U"}})o.print("\t"+c.global(n[0],n[1]));
        o.print("\t"+c.get(r[1],"K","positionX"));o.println();return w.toString();
    }
    static void worlds(ActorCheck.Access a,ActorCheck.Access b,PrintWriter out)throws Exception{
        for(String name:new String[]{"linked-first","falling-first","world-pause","source-removed"}){
            Object[][] roots=new Object[2][];
            short[] region={22,210,0,0,0,8,0,0,800,400,0,-1},source={1,202,200,199,0,16392,-50,-75,80,60,0,0,-1},linked={49,63,90,190,0,8,0,0,200,80,0,202,0,0},falling={49,167,90,190,0,8,0,0,200,80,1,-1,0,0};
            for(var c:List.of(a,b)){
                CombatCheck.arena(c);Object p=c.player(150,199);c.set(p,"c","flags",16384);c.set(p,"G","velocityX",256);c.set(p,"H","velocityY",512);c.call(p,"g","refreshBounds",ActorCheck.NONE);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);c.global("bD","bD",new byte[300]);
                c.global("av","av",null);c.global("ai","ai",false);c.global("aj","cameraControllerActive",false);c.global("am","activeCameraControllerUid",-1);c.global("ak","cameraControllerMode",0);
                for(String n:new String[]{"M","O","P","Q","R","T"})c.global(n,n.equals("M")?"cameraY":n.equals("O")?"cameraTargetY":n,0);
                c.global("L","cameraX",30);c.global("N","cameraTargetX",30);c.global("S","S",800);c.global("U","U",400);BehaviorCheck.gameCall(c,"f","updateViewBounds",ActorCheck.NONE);
                Object[] r={c.create(),c.create(),c.create(),c.create()};short[][] rs={linked,falling,source,region};
                for(int n=0;n<4;++n)c.call(r[n],"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(rs[n],20),true,0,rs[n].length);
                for(int n:name.equals("falling-first")?new int[]{3,1,0,2}:new int[]{3,0,1,2})BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},r[n]);
                c.set(r[2],"ae","persistenceIndex",1);c.set(r[0],"ae","persistenceIndex",2);c.set(r[1],"ae","persistenceIndex",3);c.global("au","au",r[3]);WorldOrderCheck.buckets(c);roots[c==a?0:1]=r;
            }
            for(int f=0;f<10;++f){boolean paused=name.equals("world-pause")&&f>=3&&f<7;
                for(var c:List.of(a,b)){c.global("x","state",paused?14:8);if(f==4&&name.equals("source-removed"))BehaviorCheck.gameCall(c,"c","removeActor",new Class<?>[]{c.actor},roots[c==a?0:1][2]);c.input(0,0);BehaviorCheck.gameCall(c,"W","updateWorldActors",ActorCheck.NONE);}
                for(int phase=0;phase<2;++phase){String expected=null;
                    for(var c:List.of(a,b)){if(phase==1&&!paused)BehaviorCheck.gameCall(c,"d","updateCamera",new Class<?>[]{int.class},2);
                        var w=new java.io.StringWriter();var o=new PrintWriter(w);o.print("W\t"+name+"\t"+f+"\t"+phase);Object[] r=roots[c==a?0:1];for(int n=0;n<2;++n)o.print("\t"+DeathCheck.actors(c).contains(r[n])+"\t"+c.get(r[n],"c","flags"));
                        for(String[] n:new String[][]{{"aj","cameraControllerActive"},{"am","activeCameraControllerUid"},{"ak","cameraControllerMode"},{"L","cameraX"},{"M","cameraY"},{"N","cameraTargetX"},{"O","cameraTargetY"},{"P","P"},{"Q","Q"},{"R","R"},{"T","T"},{"S","S"},{"U","U"}})o.print("\t"+c.global(n[0],n[1]));
                        int count=(int)c.global("bx","updateActorCount");o.print("\t"+count);for(int n=0;n<count;++n){Object actor=java.lang.reflect.Array.get(c.global("bw","updateActors"),n);o.print("\t"+(actor==null?-999:c.get(actor,"al","uid")));}o.println();String value=w.toString();if(c==a)expected=value;else ActorCheck.equal(expected,value,"camera world selection/order");
                    }out.print(expected);
                }
            }
        }
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/camera-controller-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(int uid:new int[]{63,167})for(String name:CASES){short[] tr=record(assets,uid);Object[][] roots=new Object[2][];
                for(var c:List.of(a,b)){
                    c.clearWorld();c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);c.global("bD","bD",new byte[300]);
                    c.global("bQ","mapWidthTiles",91);c.global("bR","mapHeightTiles",84);c.global("bS","levelPixelWidth",1820);c.global("bT","levelPixelHeight",1680);c.global("bc","bc",17);c.global("Z","Z",false);c.global("ai","ai",false);
                    c.global("aj","cameraControllerActive",name.equals("other-active"));c.global("am","activeCameraControllerUid",name.equals("other-active")?999:-1);c.global("ak","cameraControllerMode",0);
                    int px=uid==63?1650:1300,py=uid==63?335:800;Object p=c.player(px,py);c.call(p,"g","refreshBounds",ActorCheck.NONE);
                    c.global("L","cameraX",px-120);c.global("M","cameraY",py-220);c.global("N","cameraTargetX",px-120);c.global("O","cameraTargetY",py-220);c.global("P","P",0);c.global("Q","Q",0);
                    c.global("R","R",0);c.global("T","T",0);c.global("S","S",1820);c.global("U","U",1680);BehaviorCheck.gameCall(c,"f","updateViewBounds",ActorCheck.NONE);
                    Object t=c.create();c.call(t,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(tr,20),true,0,tr.length);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},t);
                    Object source=c.create();short[] sr=record(assets,202);c.call(source,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(sr,20),true,0,sr.length);
                    c.set(t,"ae","persistenceIndex",0);c.set(source,"ae","persistenceIndex",1);
                    if(!name.equals("missing")&&!name.equals("late"))BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},source);
                    c.set(source,"ae","persistenceIndex",1);
                    if(name.equals("source-hidden"))c.set(source,"c","flags",1033);if(name.equals("far-source")){c.set(source,"K","positionX",100);c.set(source,"L","positionY",1500);}
                    roots[c==a?0:1]=new Object[]{t,p,source};
                }
                for(int f=0;f<20;++f){
                    boolean paused=name.equals("pause")&&f>=3&&f<7,changed=name.equals("region-change")&&f<7;
                    for(var c:List.of(a,b)){Object[] r=roots[c==a?0:1];c.global("x","state",paused?14:8);c.global("ai","ai",changed);
                        if(f==4&&name.equals("late")){BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},r[2]);c.set(r[2],"ae","persistenceIndex",1);}
                        if(f==6&&name.equals("removed"))BehaviorCheck.gameCall(c,"c","removeActor",new Class<?>[]{c.actor},r[2]);
                        int px=uid==63?1650:1300,py=(uid==63?335:800)+f*2;
                        if(name.equals("outside")||name.equals("other-active")||name.equals("normal")&&f>=10&&f<14)px=500;
                        c.set(r[1],"K","positionX",px);c.set(r[1],"L","positionY",py);c.set(r[1],"G","velocityX",name.equals("stationary")?0:256);c.set(r[1],"H","velocityY",name.equals("stationary")?0:name.equals("upward")&&f<10?-512:512);
                        c.call(r[1],"g","refreshBounds",ActorCheck.NONE);
                        if(name.equals("edge"))c.set(r[1],"j","collisionBounds",new int[]{tr[2]-10,tr[3],tr[2],tr[3]+5});
                        if(name.equals("point"))c.set(r[1],"j","collisionBounds",new int[]{tr[2]+1,tr[3]+1,tr[2]+1,tr[3]+1});
                        if(name.equals("hidden")||name.equals("frozen"))c.set(r[0],"c","flags",8|(f<7?(name.equals("hidden")?1024:16384):0));
                        if(DeathCheck.actors(c).contains(r[0]))c.call(r[0],"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase){
                        if(phase==1&&!paused)for(var c:List.of(a,b))if((int)c.global("am","activeCameraControllerUid")!=-1)BehaviorCheck.gameCall(c,"d","updateCamera",new Class<?>[]{int.class},2);
                        String value=row(a,roots[0],uid,name,f,phase);ActorCheck.equal(value,row(b,roots[1],uid,name,f,phase),"camera controller "+uid+"/"+name+"/"+f+"/"+phase);out.print(value);
                    }
                    if(f==0&&(name.equals("edge")||name.equals("point")))ActorCheck.equal(-1,a.global("am","activeCameraControllerUid"),"strict overlap excludes edge/point");
                    if(uid==63&&f==6&&name.equals("removed"))ActorCheck.equal(false,DeathCheck.actors(a).contains(roots[0][0]),"removed linked source removes controller");
                }
            }
            worlds(a,b,out);
        }
        System.out.println("camera controllers: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
