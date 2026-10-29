import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
import java.lang.reflect.Array;

/** One-shot player enable and flag-2048 type-1 lifetime, not the later type-2 wave. */
public final class ActivationCheck {
    static final String[] CASES={"overlap","outside","edge","miss","pause","hidden","frozen","missing-source","late-source","missing-target","late-target","fade-target","preserved"};
    static String triggerRow(ActorCheck.Access c,Object t,Object[] targets,String name,int f)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("T\t"+name+"\t"+f);
        for(String[] n:new String[][]{{"al","uid"},{"K","positionX"},{"L","positionY"},{"c","flags"}})out.print("\t"+c.get(t,n[0],n[1]));
        out.print("\t"+(DeathCheck.actors(c).contains(t)?1:0));
        int[] bounds=(int[])c.get(t,"j","collisionBounds");for(int n:bounds==null?new int[4]:bounds)out.print("\t"+n);
        for(String n:new String[]{"s","v","w","x"})out.print("\t"+(c.get(t,n,n)!=null?1:0));
        for(Object target:targets)out.print("\t"+c.get(target,"c","flags"));out.println();return w.toString();
    }
    static void handlers(ActorCheck.Access a,ActorCheck.Access b,PrintWriter out)throws Exception{
        for(String name:CASES){
            Object[][] roots=new Object[2][];
            for(var c:List.of(a,b)){
                c.clearWorld();Object p=c.player(100,199);c.global("bD","bD",new byte[32]);
                short[] r={40,129,180,150,0,12,0,0,80,80,1,0,0,(short)(name.endsWith("source")?999:186),-1,-1,-1,114,117};
                Object t=c.create();c.call(t,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,0,19);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},t);
                if(name.equals("hidden") || name.equals("frozen"))c.set(t,"c","flags",140|(name.equals("hidden")?1024:16384));
                Object x=c.create(),y=c.create();c.set(x,"al","uid",114);c.set(y,"al","uid",117);c.set(x,"am","type",1);c.set(y,"am","type",1);
                c.set(x,"c","flags",name.equals("preserved")?17848:2216);c.set(y,"c","flags",2217);
                if(name.equals("fade-target"))c.set(x,"ao","actionState",5);
                if(!name.equals("missing-target") && !name.equals("late-target"))for(Object e:new Object[]{x,y})BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},e);
                roots[c==a?0:1]=new Object[]{t,p,x,y};
            }
            for(int f=0;f<12;++f){
                for(var c:List.of(a,b)){
                    var r=roots[c==a?0:1];c.global("x","state",name.equals("pause") && f<4?14:8);
                    int px=name.equals("pause")?180:name.equals("outside")?100:name.equals("edge")?170:name.equals("miss")?169:f<4?100:180;
                    c.set(r[1],"K","positionX",px);c.set(r[1],"a","fixedX",px*256);
                    if(name.equals("late-source") && f==4)c.set(r[1],"al","uid",999);
                    if(name.equals("late-target") && f==6)for(int i=2;i<4;++i)BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},r[i]);
                    if(DeathCheck.actors(c).contains(r[0]))c.call(r[0],"s","update",ActorCheck.NONE);
                }
                String r=triggerRow(a,roots[0][0],new Object[]{roots[0][2],roots[0][3]},name,f);ActorCheck.equal(r,triggerRow(b,roots[1][0],new Object[]{roots[1][2],roots[1][3]},name,f),"one-shot source/target enable");out.print(r);
                if(name.equals("overlap") && f==4){ActorCheck.equal(2056,a.get(roots[0][2],"c","flags"),"enable clears 32/128 while preserving 2048");ActorCheck.equal(false,DeathCheck.actors(a).contains(roots[0][0]),"enable trigger removes itself");}
            }
        }
    }
    static List<short[]> worldRecords(String name){
        short[] region={22,214,0,0,0,8,0,0,800,400,0,-1};
        short[] t={40,129,80,150,0,12,0,0,40,80,1,0,0,186,-1,-1,-1,114,117};
        short[] x={1,114,180,199,0,(short)(2088|(name.equals("forced")?16:0)),-100,-125,200,125,0,0,-1},y=x.clone();y[1]=117;y[2]=260;
        return name.equals("enemy-first")?List.of(x,y,region,t):List.of(region,t,x,y);
    }
    static String worldRows(ActorCheck.Access c,Object[] r,String name,int f,int phase)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print(triggerRow(c,r[0],new Object[]{r[1],r[2]},name,f).replaceFirst("T\\t","W"+phase+"\t"));
        for(int i=1;i<=2;++i){var ew=new java.io.StringWriter();EnemyCheck.row(c,r[i],new PrintWriter(ew),name,f,phase);out.print((i==1?"E":"E2")+ew.toString().substring(1));}
        out.print("Q\t"+name+"\t"+f+"\t"+phase);int count=(int)c.global("bx","updateActorCount");out.print("\t"+count);
        for(int i=0;i<count;++i){Object e=Array.get(c.global("bw","updateActors"),i);out.print("\t"+(e==null?-999:c.get(e,"al","uid")));}out.println();return w.toString();
    }
    static void worlds(ActorCheck.Access a,ActorCheck.Access b,PrintWriter out)throws Exception{
        for(String name:new String[]{"trigger-first","enemy-first","forced","enter-late"}){
            Object[][] roots=new Object[2][];
            for(var c:List.of(a,b)){
                CombatCheck.arena(c);Object p=c.player(name.equals("enter-late")?300:100,199);c.set(p,"c","flags",16384);c.set(null,"bE","playerInvulnerabilityMs",60000);c.global("bD","bD",new byte[32]);
                c.global("ai","ai",false);c.global("av","av",null);c.global("aj","cameraControllerActive",false);
                Object[] r=new Object[3];
                for(short[] record:worldRecords(name)){
                    Object e=c.create();c.call(e,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(record,20),true,0,record.length);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},e);
                    if(record[0]==22)c.global("au","au",e);else r[record[0]==40?0:record[1]==114?1:2]=e;
                }
                WorldOrderCheck.buckets(c);roots[c==a?0:1]=r;
            }
            for(int f=0;f<32;++f){
                for(var c:List.of(a,b)){
                    Object p=c.global("bk","player");int x=name.equals("enter-late") && f<4?300:100;c.set(p,"K","positionX",x);c.set(p,"a","fixedX",x*256);c.call(p,"g","refreshBounds",ActorCheck.NONE);int cx=Math.max(0,Math.min(560,x-120));c.global("L","cameraX",cx);c.global("aa","viewBounds",new int[]{cx,0,cx+240,320});c.input(0,0);
                    BehaviorCheck.gameCall(c,"W","updateWorldActors",ActorCheck.NONE);
                }
                for(int phase=0;phase<2;++phase){
                    if(phase==1)for(var c:List.of(a,b)){
                        for(Object e:DeathCheck.actors(c))if((int)c.get(e,"am","type")==1){boolean visible=(boolean)c.call(e,"h","isVisible",ActorCheck.NONE);if(visible || (boolean)c.get(e,"aq","updatedThisTick")){c.call(e,"g","refreshBounds",ActorCheck.NONE);c.call(e,"f","advanceAnimation",ActorCheck.NONE);}}
                        WorldOrderCheck.buckets(c);
                    }
                    String r=worldRows(a,roots[0],name,f,phase);ActorCheck.equal(r,worldRows(b,roots[1],name,f,phase),"actual world enable/next-queue dispatch");out.print(r);
                }
                if(name.equals("trigger-first") && f==0)ActorCheck.equal(0,a.get(roots[0][1],"ao","actionState"),"newly enabled enemy waits for next queue snapshot");
                if(name.equals("trigger-first") && f==1)ActorCheck.equal(101,a.get(roots[0][1],"ao","actionState"),"enabled enemy starts on next world tick");
            }
        }
    }
    static String deathRow(ActorCheck.Access c,Object e,String name,int f,int phase)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("D\t"+name+"\t"+f+"\t"+phase);
        for(String[] n:new String[][]{{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},{"G","velocityX"},{"H","velocityY"},{"I","accelerationX"},{"J","accelerationY"},{"c","flags"},{"f","animationId"},{"g","pendingAnimationId"},{"h","animationFrame"},{"i","animationTick"},{"ao","actionState"},{"aw","aw"},{"ax","ax"},{"az","az"},{"ap","health"},{"di","di"},{"O","O"}}){Object v=c.get(e,n[0],n[1]);out.print("\t"+(v instanceof Boolean x?(x?1:0):v));}
        out.print("\t"+(DeathCheck.actors(c).contains(e)?1:0)+"\t"+((boolean)c.get(e,"ak","facingLeft")?1:0));
        for(String[] n:new String[][]{{"j","collisionBounds"},{"k","attackBounds"},{"l","visualBounds"},{"m","customBounds"},{"aE","interactionBounds"}}){int[] r=(int[])c.get(e,n[0],n[1]);for(int x:r==null?new int[4]:r)out.print("\t"+x);}
        out.println("\t"+(((byte[])c.global("bD","bD"))[3]==-99?1:0));return w.toString();
    }
    static void deaths(ActorCheck.Access a,ActorCheck.Access b,PrintWriter out)throws Exception{
        for(String name:new String[]{"terminal","count1","forced-fade","keep-flag","pause-death","live-2048"}){
            Object[] roots=new Object[2];
            for(var c:List.of(a,b)){
                CombatCheck.arena(c);Object p=c.player(name.equals("live-2048")?100:600,199);c.set(p,"j","collisionBounds",new int[]{name.equals("live-2048")?95:595,100,name.equals("live-2048")?105:605,101});c.global("p","health",30);c.set(null,"bu","bu",false);c.global("bD","bD",new byte[32]);c.global("bH","bH",new short[30]);
                int[][] stats=(int[][])c.global("bY","actorStatTables");stats[0][2]=name.equals("count1")?1:0;
                short[] record={1,19,180,199,0,(short)(2056|(name.equals("forced-fade")?4:0)),-100,-125,200,125,0,0,-1};
                byte[] stream=new byte[27];stream[0]=13;for(int i=0;i<13;++i){stream[1+2*i]=(byte)record[i];stream[2+2*i]=(byte)(record[i]>>>8);}c.global("bF","bF",stream);
                Object e=c.create();c.call(e,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(record,20),true,1,13);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},e);c.set(e,"ae","persistenceIndex",3);
                if(!name.equals("live-2048")){c.set(e,"ao","actionState",3);c.set(e,"ap","health",-5);}if(name.equals("keep-flag"))c.set(e,"c","flags",2568);
                roots[c==a?0:1]=e;
            }
            for(int f=0;f<80;++f){
                boolean paused=name.equals("pause-death") && f<4;
                for(var c:List.of(a,b)){
                    Object e=roots[c==a?0:1];c.global("x","state",paused?14:8);
                    if(name.equals("count1") && f==40){c.set(e,"ao","actionState",3);c.set(e,"ap","health",-5);}
                    if(DeathCheck.actors(c).contains(e))c.call(e,"s","update",ActorCheck.NONE);
                }
                for(int phase=0;phase<2;++phase){
                    if(phase==1 && !paused)for(var c:List.of(a,b)){Object e=roots[c==a?0:1];if(DeathCheck.actors(c).contains(e) && ((int)c.get(e,"c","flags")&1024)==0){c.call(e,"g","refreshBounds",ActorCheck.NONE);c.call(e,"f","advanceAnimation",ActorCheck.NONE);}}
                    String r=deathRow(a,roots[0],name,f,phase);ActorCheck.equal(r,deathRow(b,roots[1],name,f,phase),"2048 terminal death/fade lifecycle");out.print(r);
                }
            }
            int next=((Random)a.global("c","random")).nextInt();ActorCheck.equal(next,((Random)b.global("c","random")).nextInt(),"terminal death random stream");out.println("N\t"+name+"\t"+next);
        }
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/activation-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);handlers(a,b,out);worlds(a,b,out);deaths(a,b,out);
        }
        System.out.println("enemy activation: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
