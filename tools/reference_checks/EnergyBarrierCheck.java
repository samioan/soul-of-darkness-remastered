import java.io.*;
import java.nio.file.*;
import java.util.*;

/** Original type-80 construction, handler, linked operations and region reset. */
public final class EnergyBarrierCheck {
    static final String[] CASES={"idle","spawn","enable","release","respawn-release","cleared-release","pause","hidden","frozen","stopped","reset","cleared-reset","persistent-reset","sword","ice"};
    static short[] record(String s){return new short[]{80,32,180,100,(short)(s.equals("spawn")?15:14),(short)(s.contains("respawn")||s.contains("reset")||s.equals("cleared-release")?8224:32),60};}
    static String row(ActorCheck.Access c,Object a,String s,int f,int phase)throws Exception{
        var w=new StringWriter();var out=new PrintWriter(w);out.print("E\t"+s+"\t"+f+"\t"+phase);
        for(String[] n:new String[][]{{"al","uid"},{"K","positionX"},{"L","positionY"},{"c","flags"},{"f","animationId"},{"g","pendingAnimationId"},{"h","animationFrame"},{"i","animationTick"},{"ao","actionState"},{"ar","ar"}})out.print("\t"+c.get(a,n[0],n[1]));
        out.print("\t"+(DeathCheck.actors(c).contains(a)?1:0));for(String n:new String[]{"N","O","P"})out.print("\t"+((boolean)c.get(a,n,n)?1:0));
        for(String[] n:new String[][]{{"j","collisionBounds"},{"k","attackBounds"},{"l","visualBounds"},{"m","customBounds"}}){int[] r=(int[])c.get(a,n[0],n[1]);for(int v:r==null?new int[4]:r)out.print("\t"+v);}out.println();return w.toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/energy-barrier-oracle.tsv");int count=0;
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=Files.newBufferedWriter(file)){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES){Object[] roots=new Object[2];
                for(var c:List.of(a,b)){
                    c.clearWorld();c.player(100,199);short[] r=record(s);byte[] raw=new byte[15];raw[0]=7;for(int i=0;i<7;++i){raw[1+i*2]=(byte)r[i];raw[2+i*2]=(byte)(r[i]>>8);}c.global("bF","bF",raw);
                    Object root=c.create();c.call(root,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,1,7);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},root);
                    c.call(root,"g","refreshBounds",ActorCheck.NONE);if(s.equals("hidden"))c.set(root,"c","flags",1184);if(s.equals("frozen"))c.set(root,"c","flags",16544);if(s.equals("stopped"))c.set(root,"c","flags",224);roots[c==a?0:1]=root;
                }
                for(int f=0;f<80;++f){boolean paused=s.equals("pause")&&f>=15&&f<25;
                    for(var c:List.of(a,b)){Object root=roots[c==a?0:1];c.global("x","state",paused?14:8);
                        if(f==3&&!s.equals("idle")&&!s.equals("hidden")&&!s.equals("frozen")&&!s.equals("stopped"))c.call(root,"l","enableLinkedActor",new Class<?>[]{c.actor},root);
                        if(f==12&&(s.contains("release")||s.equals("pause"))){if(s.equals("cleared-release"))c.call(root,"j","clearRespawnFlag",new Class<?>[]{c.actor},root);c.call(root,"i","activateLinkedActorAnimation31",new Class<?>[]{c.actor},root);}
                        if(f==18&&s.contains("reset")){if(s.equals("cleared-reset"))c.call(root,"j","clearRespawnFlag",new Class<?>[]{c.actor},root);if(s.equals("persistent-reset"))c.set(root,"c","flags",(int)c.get(root,"c","flags")|512);c.call(root,"b","resetOrRemove",new Class<?>[]{boolean.class},false);}
                        if(f==3&&(s.equals("sword")||s.equals("ice"))){Object p=c.global("bk","player");c.set(p,"f","animationId",s.equals("sword")?13:19);c.set(p,"k","attackBounds",((int[])c.get(root,"j","collisionBounds")).clone());c.set(null,"bK","activeWeapon",s.equals("sword")?0:1);}
                        if(DeathCheck.actors(c).contains(root))c.call(root,"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase){if(phase==1&&!paused)for(var c:List.of(a,b)){Object root=roots[c==a?0:1];if(DeathCheck.actors(c).contains(root)&&((int)c.get(root,"c","flags")&1024)==0){c.call(root,"g","refreshBounds",ActorCheck.NONE);c.call(root,"f","advanceAnimation",ActorCheck.NONE);}}
                        String original=row(a,roots[0],s,f,phase);ActorCheck.equal(original,row(b,roots[1],s,f,phase),"energy barrier");out.write(original);++count;
                    }
                }
            }
            for(int level=0;level<9;++level)for(var r:LevelCheck.records(BehaviorCheck.resource(assets,level+3,0)))if(r.values()[0]==80){Object[] roots=new Object[2];for(var c:List.of(a,b)){c.clearWorld();Object root=c.create();c.call(root,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r.values(),20),true,r.offset(),r.values().length);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},root);c.call(root,"g","refreshBounds",ActorCheck.NONE);roots[c==a?0:1]=root;}String original=row(a,roots[0],"placed-"+level,r.values()[1],0);ActorCheck.equal(original,row(b,roots[1],"placed-"+level,r.values()[1],0),"placed energy barrier");out.write(original);++count;}
        }
        System.out.println("EnergyBarrierCheck: "+count+" original/reference rows; "+file);
    }
}
