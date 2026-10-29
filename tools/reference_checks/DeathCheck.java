import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/** Ordinary type-1 death cleanup/record reuse and type-35 enemy drop delay. */
public final class DeathCheck {
    static List<Object> actors(ActorCheck.Access a) throws Exception {
        List<Object> result=new ArrayList<>(); Object list=a.global("bs","actors");
        for(int i=0;i<(int)a.global("bt","actorCount");++i) { Object e=Array.get(list,i); if(e!=null) result.add(e); } return result;
    }
    static String newActors(ActorCheck.Access a,List<Object> before,String key,int frame) throws Exception {
        var text=new java.io.StringWriter(); var out=new PrintWriter(text); int i=0;
        for(Object e:actors(a)) if(!before.contains(e)) {
            out.print("L\t"+key+"\t"+frame+"\t"+i++);
            for(String[] n:new String[][]{{"am","type"},{"f","animationId"},{"K","positionX"},{"L","positionY"},{"c","flags"},{"ao","actionState"},{"az","az"},{"au","au"},{"an","paintPriority"}}) out.print("\t"+a.get(e,n[0],n[1])); out.println();
        }
        return text.toString();
    }
    static void mainChecks(ActorCheck.Access a,ActorCheck.Access b,PrintWriter out) throws Exception {
        int index=0;
        for(int health:new int[]{0,30,60}) for(int magic:new int[]{0,24,25,60}) for(boolean unlocked:new boolean[]{false,true}) for(boolean left:new boolean[]{false,true}) for(int seed:new int[]{0,1,12345}) {
            String key="loot"+index++; String events="";
            for(var c:List.of(a,b)) {
                c.clearWorld(); c.player(600,199); c.global("p","health",health); c.global("s","magic",magic); c.set(null,"bu","bu",unlocked); c.global("c","random",new Random(seed));
                Object e=c.create(); c.set(e,"K","positionX",180); c.set(e,"L","positionY",199); c.set(e,"ak","facingLeft",left);
                c.call(e,"a","dropLoot",new Class<?>[]{boolean.class},false);
                String v=newActors(c,List.of(),key,-1); if(c==a) events=v; else ActorCheck.equal(events,v,"loot selection");
            }
            out.print(events); int next=((Random)a.global("c","random")).nextInt(); ActorCheck.equal(next,((Random)b.global("c","random")).nextInt(),"loot random stream"); out.println("N\t"+key+"\t"+next);
        }
        for(String key:new String[]{"placed","spawned","linked","repeat","count1","count2","keepflag","flag32","sameanim","moving","paused"}) {
            Object[] roots=new Object[4];
            for(var c:List.of(a,b)) {
                CombatCheck.arena(c); Object p=c.player(600,199); c.set(p,"j","collisionBounds",new int[]{595,100,605,101}); c.set(null,"bu","bu",false);
                int[][] stats=(int[][])c.global("bY","actorStatTables"); stats[0][2]=key.equals("count1")?1:key.equals("count2")?2:0;
                short[] r={1,(short)(key.equals("spawned")?-1:19),180,199,(short)(key.equals("sameanim")?1:0),(short)(key.equals("repeat")?13:key.equals("flag32")?45:9),-100,-125,200,125,0,0,(short)(key.equals("linked")?7:-1)};
                byte[] stream=new byte[27]; stream[0]=13; for(int i=0;i<13;++i) { stream[1+i*2]=(byte)r[i]; stream[2+i*2]=(byte)(r[i]>>8); } c.global("bF","bF",stream);
                Object e=c.create(); c.call(e,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,1,13);
                BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},e);
                c.set(e,"ao","actionState",3); c.set(e,"ap","health",-5);
                if(key.equals("keepflag")) c.set(e,"c","flags",521);
                if(key.equals("sameanim")) { c.set(e,"h","animationFrame",1); c.set(e,"g","pendingAnimationId",2); }
                if(key.equals("moving")) { c.set(e,"K","positionX",187); c.set(e,"G","velocityX",512); c.set(e,"H","velocityY",256); c.set(e,"I","accelerationX",12); c.set(e,"J","accelerationY",34); }
                c.call(e,"g","refreshBounds",ActorCheck.NONE);
                roots[c==a?0:2]=p; roots[c==a?1:3]=e;
            }
            boolean sawFade=false,sawActive=false;
            for(int frame=0;frame<80;++frame) {
                boolean paused=key.equals("paused") && frame<5; String events="";
                for(var c:List.of(a,b)) {
                    Object e=roots[c==a?1:3]; List<Object> before=actors(c); c.global("x","state",paused?14:8);
                    if(frame==40 && (key.equals("repeat") || key.equals("count1") || key.equals("count2"))) { c.set(e,"ao","actionState",3); c.set(e,"ap","health",-5); }
                    if(actors(c).contains(e)) c.call(e,"s","update",ActorCheck.NONE);
                    String v=newActors(c,before,key,frame); if(c==a) events=v; else ActorCheck.equal(events,v,"death/fade spawns");
                }
                out.print(events);
                for(int phase=0;phase<2;++phase) {
                    if(phase==1 && !paused) for(var c:List.of(a,b)) { Object e=roots[c==a?1:3]; if(actors(c).contains(e) && ((int)c.get(e,"c","flags")&1024)==0) { c.call(e,"g","refreshBounds",ActorCheck.NONE); c.call(e,"f","advanceAnimation",ActorCheck.NONE); } }
                    Object e=roots[1]; ActorCheck.snapshot(a,b,e,roots[3],key+frame);
                    out.print("E\t"+key+"\t"+frame+"\t"+phase);
                    for(String[] n:ActorCheck.SNAPSHOT) { Object v=a.get(e,n[0],n[1]); if(v==null) for(int i=0;i<4;++i) out.print("\t0"); else if(v instanceof int[] r) for(int x:r) out.print("\t"+x); else out.print("\t"+v); }
                    for(String[] n:new String[][]{{"aw","aw"},{"ax","ax"},{"az","az"},{"ap","health"},{"di","di"},{"O","O"},{"ak","facingLeft"},{"m","customBounds"},{"aE","interactionBounds"}}) {
                        Object v=a.get(e,n[0],n[1]); ActorCheck.equal(v,b.get(roots[3],n[0],n[1]),key+n[1]); if(v instanceof int[] r) for(int x:r) out.print("\t"+x); else out.print("\t"+v);
                    }
                    out.println("\t"+(actors(a).contains(e)?1:0));
                }
                int state=(int)a.get(roots[1],"ao","actionState"); sawFade|=state==5 || state==6 || state==7; sawActive|=state==101;
            }
            if(key.equals("repeat") || key.startsWith("count")) ActorCheck.equal(true,sawFade && sawActive,"respawn cycle must complete");
            int next=((Random)a.global("c","random")).nextInt(); ActorCheck.equal(next,((Random)b.global("c","random")).nextInt(),"death random stream"); out.println("N\t"+key+"\t"+next);
        }
    }
    static void pickups(ActorCheck.Access a,ActorCheck.Access b,PrintWriter out) throws Exception {
        for(String key:new String[]{"health","magic","left","negative","paused","frozen","collect"}) {
            Object[] roots=new Object[4];
            for(var c:List.of(a,b)) {
                CombatCheck.arena(c); Object p=c.player(key.equals("collect")?180:600,199); c.global("p","health",30); c.global("s","magic",30); c.set(null,"bm","bm",true); c.set(null,"bn","bn",true);
                Object source=c.create(); c.set(source,"K","positionX",key.equals("negative")?-10:180); c.set(source,"L","positionY",key.equals("negative")?-20:199); c.set(source,"ak","facingLeft",key.equals("left"));
                Object e=c.call(source,"a","spawnActor",new Class<?>[]{int.class,int.class,int.class,int.class,int.class,int.class,boolean.class},35,33,key.equals("magic")?2:0,0,0,308,false); c.call(e,"b","b",ActorCheck.NONE);
                if(key.equals("frozen")) c.set(e,"c","flags",192);
                roots[c==a?0:2]=p; roots[c==a?1:3]=e;
            }
            for(int frame=-1;frame<95;++frame) {
                boolean paused=key.equals("paused") && frame>=0 && frame<5; String events="";
                for(var c:List.of(a,b)) {
                    Object e=roots[c==a?1:3]; List<Object> before=actors(c); c.global("x","state",paused?14:8);
                    if(frame>=0 && actors(c).contains(e)) c.call(e,"s","update",ActorCheck.NONE);
                    if(frame>=0 && actors(c).contains(e) && !paused) { c.call(e,"g","refreshBounds",ActorCheck.NONE); c.call(e,"f","advanceAnimation",ActorCheck.NONE); }
                    String v=newActors(c,before,"drop_"+key,frame); if(c==a) events=v; else ActorCheck.equal(events,v,"drop collection spawns");
                }
                out.print(events); Object e=roots[1]; ActorCheck.snapshot(a,b,e,roots[3],"drop "+key+frame);
                out.print("K\t"+key+"\t"+frame);
                for(String[] n:new String[][]{{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},{"G","velocityX"},{"H","velocityY"},{"c","flags"},{"f","animationId"},{"h","animationFrame"},{"i","animationTick"},{"ao","actionState"},{"ax","ax"},{"dd","dd"}}) out.print("\t"+a.get(e,n[0],n[1]));
                boolean alive=actors(a).contains(e); out.print("\t"+(alive?1:0)); int[] rect=alive?(int[])a.get(e,"j","collisionBounds"):new int[4]; for(int x:rect) out.print("\t"+x);
                for(String[] n:new String[][]{{"az","az"},{"au","au"}}) out.print("\t"+a.get(e,n[0],n[1])); out.println("\t"+a.global("p","health")+"\t"+a.global("s","magic")); if(!alive) break;
            }
        }
    }
    static void respawnEffect(ActorCheck.Access a,ActorCheck.Access b,PrintWriter out) throws Exception {
        for(String key:new String[]{"normal","paused","frozen"}) {
            Object[] roots=new Object[2];
            for(var c:List.of(a,b)) {
                CombatCheck.arena(c); Object p=c.player(100,199);
                Object e=c.call(p,"a","spawnActor",new Class<?>[]{int.class,int.class,int.class,int.class,int.class,int.class,boolean.class},39,1,28,0,-20,302,false);
                if(key.equals("frozen")) c.set(e,"c","flags",64); roots[c==a?0:1]=e;
            }
            for(int frame=-1;frame<40;++frame) {
                boolean paused=key.equals("paused") && frame>=0 && frame<5;
                for(var c:List.of(a,b)) {
                    Object e=roots[c==a?0:1]; c.global("x","state",paused?14:8); if(frame>=0 && actors(c).contains(e)) c.call(e,"s","update",ActorCheck.NONE);
                    if(frame>=0 && actors(c).contains(e) && !paused) c.call(e,"f","advanceAnimation",ActorCheck.NONE);
                }
                Object e=roots[0]; ActorCheck.snapshot(a,b,e,roots[1],"respawn effect");
                out.print("F\t"+key+"\t"+frame);
                for(String[] n:new String[][]{{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},{"H","velocityY"},{"c","flags"},{"f","animationId"},{"h","animationFrame"},{"i","animationTick"},{"aw","aw"},{"au","au"}}) out.print("\t"+a.get(e,n[0],n[1]));
                boolean alive=actors(a).contains(e); out.println("\t"+(alive?1:0)); if(!alive) break;
            }
        }
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]); Path file=assets.getParent().resolve("analysis/death-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false); var b=new ActorCheck.Access(build,assets,stubs,true); var out=new PrintWriter(Files.newBufferedWriter(file))) {
            BehaviorCheck.commonAssets(a,assets); BehaviorCheck.commonAssets(b,assets); mainChecks(a,b,out); pickups(a,b,out); respawnEffect(a,b,out);
        }
        System.out.println("enemy death/loot: "+ActorCheck.comparisons+" original/reference comparisons; "+file);
    }
}
