import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
/** Behavior-zero type-2 flight, combat reactions and lifetime against original bytecode. */
public final class FlyingEnemyCheck {
    static final String[] CASES={"idle","chase","attack","right","diagonal","zero","pause","hidden","frozen","clock-frozen","flag32","hurt","terminal","fade"};
    static String row(ActorCheck.Access c,Object e,String s,int f,int phase)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("F\t"+s+"\t"+f+"\t"+phase);
        for(String[] n:ActorCheck.SNAPSHOT){Object v=c.get(e,n[0],n[1]);if(v==null)for(int i=0;i<4;++i)out.print("\t0");else if(v instanceof int[] r)for(int x:r)out.print("\t"+x);else out.print("\t"+v);}
        for(String[] n:new String[][]{{"aw","aw"},{"ax","ax"},{"az","az"},{"ap","health"},{"ak","facingLeft"},{"Q","Q"}}){Object v=c.get(e,n[0],n[1]);out.print("\t"+(v instanceof Boolean b?(b?1:0):v));}
        for(String[] n:new String[][]{{"m","customBounds"},{"aE","interactionBounds"}})for(int x:(int[])c.get(e,n[0],n[1]))out.print("\t"+x);
        out.print("\t"+c.get(e,"dd","dd")+"\t"+c.get(e,"ad","ad")+"\t"+c.get(e,"di","di")+"\t"+(DeathCheck.actors(c).contains(e)?1:0));out.println();return w.toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/flying-enemy-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES){
                Object[] roots=new Object[2];boolean sawWindup=false,sawAttack=false;
                for(var c:List.of(a,b)){
                    CombatCheck.arena(c);c.player(s.equals("idle")?700:s.equals("right")?300:100,199);c.set(null,"bE","playerInvulnerabilityMs",60000);c.global("bD","bD",new byte[32]);c.global("bH","bH",new short[30]);
                    int[][] stats=(int[][])c.global("bY","actorStatTables");stats[2][2]=s.equals("fade")?1:0;
                    int flags=s.equals("hidden")?1032:s.equals("frozen")?16392:s.equals("clock-frozen")?72:s.equals("flag32")?40:s.equals("terminal")?2056:s.equals("fade")?136:8;
                    short[] r={2,113,(short)(s.equals("zero")?100:s.equals("chase")?260:s.equals("attack")?150:180),(short)(s.equals("diagonal")?119:174),0,(short)flags,-200,-200,400,400,4,0,-1};
                    byte[] stream=new byte[27];stream[0]=13;for(int i=0;i<13;++i){stream[1+2*i]=(byte)r[i];stream[2+2*i]=(byte)(r[i]>>>8);}c.global("bF","bF",stream);
                    Object e=c.create();c.call(e,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,1,13);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},e);c.set(e,"ae","persistenceIndex",3);roots[c==a?0:1]=e;
                }
                for(int f=0;f<96;++f){
                    boolean paused=s.equals("pause") && f>=6 && f<16;
                    List<String> events=new ArrayList<>();
                    for(var c:List.of(a,b)){
                        var before=new HashSet<>(DeathCheck.actors(c));
                        Object e=roots[c==a?0:1];c.global("x","state",paused?14:8);c.input(0,0);
                        if((s.equals("hurt") || s.equals("terminal")) && (f==10 || f==25))c.call(e,"p","applyDamageWithReaction",ActorCheck.INT,f==10?5:20);
                        if(s.equals("fade") && f==50)c.call(e,"p","applyDamageWithReaction",ActorCheck.INT,20);
                        if(DeathCheck.actors(c).contains(e))c.call(e,"s","update",ActorCheck.NONE);
                        var event=new StringBuilder();for(Object child:DeathCheck.actors(c))if(!before.contains(child)){
                            event.append("L\t").append(s).append('\t').append(f);
                            for(String[] n:new String[][]{{"am","type"},{"f","animationId"},{"K","positionX"},{"L","positionY"},{"c","flags"}})event.append('\t').append(c.get(child,n[0],n[1]));event.append('\n');
                        }events.add(event.toString());
                    }
                    ActorCheck.equal(events.get(0),events.get(1),"flying effect/loot birth events");out.print(events.get(0));
                    for(int phase=0;phase<2;++phase){
                        if(phase==1 && !paused)for(var c:List.of(a,b)){Object e=roots[c==a?0:1];if(DeathCheck.actors(c).contains(e) && ((int)c.get(e,"c","flags")&1024)==0){c.call(e,"g","refreshBounds",ActorCheck.NONE);c.call(e,"f","advanceAnimation",ActorCheck.NONE);}}
                        String r=row(a,roots[0],s,f,phase);ActorCheck.equal(r,row(b,roots[1],s,f,phase),"flying enemy AI/lifetime");out.print(r);
                    }
                    int state=(int)a.get(roots[0],"ao","actionState");sawWindup|=state==200;sawAttack|=state==102;
                }
                if(s.equals("attack"))ActorCheck.equal(true,sawWindup && sawAttack,"flying attack includes windup and strike states");
                int next=((Random)a.global("c","random")).nextInt();ActorCheck.equal(next,((Random)b.global("c","random")).nextInt(),"flying damage/loot random stream");out.println("N\t"+s+"\t"+next);
            }
        }
        System.out.println("flying enemy: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
