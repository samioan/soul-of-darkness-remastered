import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
/** Condition-four's cache/missing-source quirks and one-shot type-2 enable. */
public final class RemovedEnemyCheck {
    static String row(ActorCheck.Access c,Object t,Object[] targets,String s,int f)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("R\t"+s+"\t"+f);
        for(String[] n:new String[][]{{"al","uid"},{"K","positionX"},{"L","positionY"},{"c","flags"}})out.print("\t"+c.get(t,n[0],n[1]));out.print("\t"+(DeathCheck.actors(c).contains(t)?1:0));
        int[] r=(int[])c.get(t,"j","collisionBounds");for(int n:r==null?new int[4]:r)out.print("\t"+n);
        for(String n:new String[]{"y","z","A","at","ar","av"})out.print("\t"+c.get(t,n,n));
        for(String n:new String[]{"s","t","u","v","w","x"})out.print("\t"+(c.get(t,n,n)!=null?1:0));
        for(Object target:targets)out.print("\t"+c.get(target,"c","flags"));out.println();return w.toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/removed-enemy-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            a.loadAssets(assets);b.loadAssets(assets);
            for(String s:new String[]{"normal","one-left","reverse","missing-source","missing-target","late-target","early-remove","index-zero","pause","hidden","frozen","fade-target","source-hidden"}){
                Object[][] roots=new Object[2][];
                for(var c:List.of(a,b)){
                    c.clearWorld();c.player(100,199);c.global("bD","bD",new byte[32]);Object[] r=new Object[5];
                    for(int i=0;i<2;++i){Object source=c.create();c.set(source,"al","uid",10+i);c.set(source,"am","type",1);if(s.equals("source-hidden"))c.set(source,"c","flags",1024);
                        if(!s.equals("missing-source") || i==1){BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},source);c.set(source,"ae","persistenceIndex",s.equals("index-zero")?0:5+i);}r[i]=source;
                        Object target=c.create();c.set(target,"al","uid",20+i);c.set(target,"am","type",2);c.set(target,"c","flags",2216+i);if(s.equals("fade-target"))c.set(target,"ao","actionState",5);
                        if(!s.equals("missing-target") && !s.equals("late-target"))BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},target);r[2+i]=target;
                    }
                    short[] record={40,54,180,150,0,12,0,0,80,80,4,0,0,10,11,-1,-1,20,21};Object t=c.create();c.call(t,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(record,20),true,0,19);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},t);r[4]=t;
                    if(s.equals("hidden") || s.equals("frozen"))c.set(t,"c","flags",140|(s.equals("hidden")?1024:16384));
                    if(s.equals("early-remove"))for(int i=0;i<2;++i)BehaviorCheck.gameCall(c,"c","removeActor",new Class<?>[]{c.actor},r[i]);roots[c==a?0:1]=r;
                }
                for(int f=0;f<16;++f){
                    for(var c:List.of(a,b)){
                        var r=roots[c==a?0:1];c.global("x","state",s.equals("pause") && f>=3 && f<10?14:8);
                        if(!s.equals("early-remove") && !s.equals("index-zero")){
                            if(f==4)BehaviorCheck.gameCall(c,"c","removeActor",new Class<?>[]{c.actor},r[s.equals("reverse")?1:0]);
                            if(f==8 && !s.equals("one-left"))BehaviorCheck.gameCall(c,"c","removeActor",new Class<?>[]{c.actor},r[s.equals("reverse")?0:1]);
                        }
                        if(f==10 && s.equals("late-target"))for(int i=2;i<4;++i)BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},r[i]);
                        if(DeathCheck.actors(c).contains(r[4]))c.call(r[4],"s","update",ActorCheck.NONE);
                    }
                    String r=row(a,roots[0][4],new Object[]{roots[0][2],roots[0][3]},s,f);ActorCheck.equal(r,row(b,roots[1][4],new Object[]{roots[1][2],roots[1][3]},s,f),"removed enemy source/enable");out.print(r);
                    if(s.equals("index-zero") && f==0)ActorCheck.equal(false,DeathCheck.actors(a).contains(roots[0][4]),"index-zero shortcut accepts both living sources");
                }
            }
        }
        System.out.println("removed enemy trigger: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
