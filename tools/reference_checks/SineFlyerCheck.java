import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
/** Type-2 behavior 6 (variant index 49, the level-1 sine-wave flyers 181/136/137) against original bytecode. */
public final class SineFlyerCheck {
    // name, record flags, parameters 6..9 (custom bounds), enemy x, player x, region x, region width
    static final Object[][] CASES={
        {"right",8,new int[]{0,-75,150,100},200,230,0,320},{"left",9,new int[]{-150,-75,150,60},500,470,300,400},{"left-wide",9,new int[]{-187,-75,150,60},500,400,0,700},
        {"attack",8,new int[]{0,-75,150,100},200,330,0,700},{"outside",8,new int[]{0,-75,150,100},200,700,0,320},{"far-region",8,new int[]{0,-75,150,100},200,230,0,700},
        {"hurt",8,new int[]{0,-75,150,100},200,230,0,700},{"terminal",2056,new int[]{0,-75,150,100},200,230,0,700},{"pause",8,new int[]{0,-75,150,100},200,230,0,700},
        {"hidden",1032,new int[]{0,-75,150,100},200,230,0,700},{"frozen",16392,new int[]{0,-75,150,100},200,230,0,700}};
    static final int FRAMES=120;
    static String row(ActorCheck.Access c,Object e,String s,int f,int phase)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("F\t"+s+"\t"+f+"\t"+phase);
        for(String[] n:ActorCheck.SNAPSHOT){Object v=c.get(e,n[0],n[1]);if(v==null)for(int i=0;i<4;++i)out.print("\t0");else if(v instanceof int[] r)for(int x:r)out.print("\t"+x);else out.print("\t"+v);}
        for(String[] n:new String[][]{{"aw","aw"},{"ax","ax"},{"az","az"},{"ap","health"},{"ak","facingLeft"},{"Q","Q"}}){Object v=c.get(e,n[0],n[1]);out.print("\t"+(v instanceof Boolean b?(b?1:0):v));}
        for(String[] n:new String[][]{{"m","customBounds"},{"aE","interactionBounds"}})for(int x:(int[])c.get(e,n[0],n[1]))out.print("\t"+x);
        out.print("\t"+c.get(e,"dd","dd")+"\t"+c.get(e,"ad","ad")+"\t"+c.get(e,"ac","ac")+"\t"+c.get(e,"aF","aF")+"\t"+c.get(e,"di","di")+"\t"+(DeathCheck.actors(c).contains(e)?1:0));out.println();return w.toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/sine-flyer-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(Object[] cs:CASES){
                final String s=(String)cs[0];final int[] bounds=(int[])cs[2];Object[] roots=new Object[2],players=new Object[2],regions=new Object[2];boolean sawSine=false,sawAttack=false,sawReset=false;
                for(var c:List.of(a,b)){
                    CombatCheck.arena(c);Object p=c.player((int)cs[4],199);c.set(null,"bE","playerInvulnerabilityMs",60000);c.global("bD","bD",new byte[32]);c.global("bH","bH",new short[30]);
                    int[][] stats=(int[][])c.global("bY","actorStatTables");stats[2][2]=0;
                    short[] r={2,113,(short)(int)cs[3],174,0,(short)(int)cs[1],(short)bounds[0],(short)bounds[1],(short)bounds[2],(short)bounds[3],49,0,-1};
                    byte[] stream=new byte[27];stream[0]=13;for(int i=0;i<13;++i){stream[1+2*i]=(byte)r[i];stream[2+2*i]=(byte)(r[i]>>>8);}c.global("bF","bF",stream);
                    Object e=c.create();c.call(e,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,1,13);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},e);c.set(e,"ae","persistenceIndex",1);
                    roots[c==a?0:1]=e;players[c==a?0:1]=p;
                    short[] rr={22,210,(short)(int)cs[5],0,0,8,0,0,(short)(int)cs[6],400,0,-1};Object region=c.create();c.call(region,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(rr,20),true,0,12);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},region);regions[c==a?0:1]=region;
                }
                for(int f=0;f<FRAMES;++f){
                    boolean paused=s.equals("pause") && f>=6 && f<16;
                    List<String> events=new ArrayList<>();
                    for(var c:List.of(a,b)){
                        var before=new HashSet<>(DeathCheck.actors(c));
                        Object e=roots[c==a?0:1];c.global("x","state",paused?14:8);c.input(0,0);
                        if((s.equals("hurt") || s.equals("terminal")) && (f==12 || f==40))c.call(e,"p","applyDamageWithReaction",ActorCheck.INT,f==12?3:20);
                        c.call(regions[c==a?0:1],"s","update",ActorCheck.NONE);
                        if(DeathCheck.actors(c).contains(e))c.call(e,"s","update",ActorCheck.NONE);
                        var event=new StringBuilder();for(Object child:DeathCheck.actors(c))if(!before.contains(child)){
                            event.append("L\t").append(s).append('\t').append(f);
                            for(String[] n:new String[][]{{"am","type"},{"f","animationId"},{"K","positionX"},{"L","positionY"},{"c","flags"}})event.append('\t').append(c.get(child,n[0],n[1]));event.append('\n');
                        }events.add(event.toString());
                    }
                    ActorCheck.equal(events.get(0),events.get(1),"sine flyer effect/loot birth events");out.print(events.get(0));
                    for(int phase=0;phase<2;++phase){
                        if(phase==1 && !paused)for(var c:List.of(a,b)){Object e=roots[c==a?0:1];if(DeathCheck.actors(c).contains(e) && ((int)c.get(e,"c","flags")&1024)==0){c.call(e,"g","refreshBounds",ActorCheck.NONE);c.call(e,"f","advanceAnimation",ActorCheck.NONE);}}
                        String r=row(a,roots[0],s,f,phase);ActorCheck.equal(r,row(b,roots[1],s,f,phase),"sine flyer AI/lifetime "+s+" "+f+"/"+phase);out.print(r);
                    }
                    int state=(int)a.get(roots[0],"ao","actionState");sawSine|=state==138;sawAttack|=state==139;sawReset|=(((int)a.get(roots[0],"c","flags"))&1024)!=0 && state==0 && f>0;
                }
                if(s.equals("right")||s.equals("left")||s.equals("left-wide")||s.equals("hurt"))ActorCheck.equal(true,sawSine,"sine flyer enters its wave state "+s);
                if(s.equals("right")||s.equals("left"))ActorCheck.equal(true,sawReset,"leaving the player bounds resets the flyer "+s);
                if(s.equals("attack"))ActorCheck.equal(true,sawAttack,"sine flyer attacks inside its interaction bounds");
                if(s.equals("outside"))ActorCheck.equal(false,sawSine,"flyer stays dormant without overlap");
                int next=((Random)a.global("c","random")).nextInt();ActorCheck.equal(next,((Random)b.global("c","random")).nextInt(),"sine flyer random stream");out.println("N\t"+s+"\t"+next);
            }
        }
        System.out.println("sine flyer: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
