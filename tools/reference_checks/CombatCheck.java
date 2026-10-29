import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

/** Controlled weapon-0 damage, type-1 hurt/death entries, player hits and effects. */
public final class CombatCheck {
    static void weapons(ActorCheck.Access a,int rank,int secondary) throws Exception {
        a.set(null,"bK","activeWeapon",0); a.set(null,"bM","bM",new int[]{rank,0}); a.set(null,"bN","bN",new int[]{secondary,0});
    }
    static Object enemy(ActorCheck.Access a,int x,int y) throws Exception {
        Object e=a.create(); short[] r={1,19,(short)x,(short)y,0,9,-100,-125,200,125,0,0,-1};
        a.call(e,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},java.util.Arrays.copyOf(r,20),true,0,13);
        a.set(e,"ap","health",50); return e;
    }
    static void arena(ActorCheck.Access a) throws Exception {
        a.clearWorld(); a.global("aa","viewBounds",new int[]{0,0,240,320}); a.set(null,"db","db",0);
        byte[] tiles=(byte[])a.global("dL","collisionTiles"); for(int i=400;i<800;++i) tiles[i]=24;
    }
    static String spawns(ActorCheck.Access a,int start,String scenario,int frame) throws Exception {
        var out=new java.io.StringWriter(); var p=new PrintWriter(out);
        for(int i=start;i<(int)a.global("bt","actorCount");++i) {
            Object e=Array.get(a.global("bs","actors"),i);
            ActorCheck.equal(39,a.get(e,"am","type"),"combat spawn type"); ActorCheck.equal(302,a.get(e,"an","paintPriority"),"effect priority");
            p.print("S\t"+scenario+"\t"+frame+"\t"+(i-start));
            for(String[] field:new String[][]{{"f","animationId"},{"K","positionX"},{"L","positionY"},{"c","flags"},{"aw","aw"},{"au","au"}}) p.print("\t"+a.get(e,field[0],field[1]));
            p.println();
        }
        return out.toString();
    }
    static void enemyRow(ActorCheck.Access a,ActorCheck.Access b,Object e,Object f,PrintWriter out,String s,int frame,int phase) throws Exception {
        ActorCheck.snapshot(a,b,e,f,s);
        for(String[] n:new String[][]{{"aw","aw"},{"ax","ax"},{"az","az"},{"ap","health"},{"ak","facingLeft"}}) ActorCheck.equal(a.get(e,n[0],n[1]),b.get(f,n[0],n[1]),s+n[1]);
        EnemyCheck.row(a,e,out,s,frame,phase);
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),compiled=Path.of(args[1]),stubs=Path.of(args[2]);
        Path output=assets.getParent().resolve("analysis/combat-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false); var b=new ActorCheck.Access(compiled,assets,stubs,true); var out=new PrintWriter(Files.newBufferedWriter(output))) {
            BehaviorCheck.commonAssets(a,assets); BehaviorCheck.commonAssets(b,assets);
            for(int rank=0;rank<4;++rank) for(int seed:new int[]{0,12345}) {
                for(var c:List.of(a,b)) { weapons(c,rank,1); c.global("c","random",new Random(seed)); }
                for(int i=0;i<32;++i) {
                    Object v=a.call(null,"v","rollWeaponDamage",ActorCheck.NONE); ActorCheck.equal(v,b.call(null,"v","rollWeaponDamage",ActorCheck.NONE),"weapon roll");
                    out.println("W\t"+rank+"\t"+seed+"\t"+i+"\t"+v);
                }
            }
            for(long seed:new long[]{0,12345,247775869193412L}) {
                for(var c:List.of(a,b)) c.global("c","random",new Random(seed));
                int[][] ranges={{9,12},{10,50},{-40,40},{-20,-100},{7,7},{Integer.MIN_VALUE,Integer.MAX_VALUE}};
                for(int i=0;i<60;++i) {
                    int[] r=ranges[i%ranges.length]; Object value=null;
                    for(var c:List.of(a,b)) {
                        var method=c.game.getDeclaredMethod(c.renamed?"randomRange":"f",int.class,int.class); method.setAccessible(true);
                        Object v=method.invoke(null,r[0],r[1]); if(c==a) value=v; else ActorCheck.equal(value,v,"random range");
                    }
                    out.println("R\t"+seed+"\t"+i+"\t"+value);
                }
            }
            int index=0;
            for(int maximum:new int[]{60,90,120}) for(int health:new int[]{0,1,60}) for(int inv:new int[]{0,1,600}) for(int damage:new int[]{-1,0,1,499,500,1000}) for(boolean shield:new boolean[]{false,true}) {
                Object accepted=null;
                for(var c:List.of(a,b)) {
                    c.clearWorld(); Object p=c.player(100,199); c.set(p,"bU","bU",shield); c.global("p","health",health); c.global("q","maxHealth",maximum); c.set(null,"bE","playerInvulnerabilityMs",inv);
                    Object r=c.call(null,"c","damagePlayer",ActorCheck.INT,damage); if(c==a) accepted=r; else ActorCheck.equal(accepted,r,"player damage gate");
                }
                ActorCheck.equal(a.global("p","health"),b.global("p","health"),"scaled health"); ActorCheck.equal(a.get(null,"bE","playerInvulnerabilityMs"),b.get(null,"bE","playerInvulnerabilityMs"),"invulnerability");
                out.println("D\t"+(index++)+"\t"+accepted+"\t"+a.global("p","health")+"\t"+a.get(null,"bE","playerInvulnerabilityMs"));
            }
            index=0;
            for(int amount:new int[]{-1,0,1,9,10,100}) for(int rank:new int[]{0,3}) for(int secondary:new int[]{1,4}) for(boolean reaction:new boolean[]{false,true}) for(boolean left:new boolean[]{false,true}) {
                Object[] e=new Object[2]; String s="direct"+index++;
                for(var c:List.of(a,b)) {
                    arena(c); c.player(100,199); weapons(c,rank,secondary); Object root=enemy(c,180,199); c.set(root,"ak","facingLeft",left);
                    c.call(root,"b","applyDamage",new Class<?>[]{int.class,boolean.class},amount,reaction); e[c==a?0:1]=root;
                }
                enemyRow(a,b,e[0],e[1],out,s,-1,0);
                String events=spawns(a,0,s,-1); ActorCheck.equal(events,spawns(b,0,s,-1),"enemy effect spawns"); out.print(events);
                int next=((Random)a.global("c","random")).nextInt(); ActorCheck.equal(next,((Random)b.global("c","random")).nextInt(),"enemy random stream"); out.println("N\t"+s+"\t"+next);
            }
            index=0;
            for(int flags:new int[]{0,1024,16384}) for(int cooldown:new int[]{-1,0,1,200}) for(int action:new int[]{0,3}) for(boolean overlap:new boolean[]{false,true}) {
                Object[] roots=new Object[2]; Object accepted=null; String s="guard"+index++;
                for(var c:List.of(a,b)) {
                    arena(c); Object p=c.player(100,199),e=enemy(c,180,199); weapons(c,0,1); c.set(e,"c","flags",flags|8); c.set(e,"az","az",cooldown); c.set(e,"ao","actionState",action);
                    c.set(p,"k","attackBounds",overlap?new int[]{170,150,190,199}:new int[4]);
                    Object v=c.call(e,"e","tryReceivePlayerWeaponDamage",new Class<?>[]{boolean.class},true); if(c==a) accepted=v; else ActorCheck.equal(accepted,v,"weapon damage gate"); roots[c==a?0:1]=e;
                }
                out.println("G\t"+s+"\t"+accepted); enemyRow(a,b,roots[0],roots[1],out,s,-1,0);
                String events=spawns(a,0,s,-1); ActorCheck.equal(events,spawns(b,0,s,-1),"guard effect spawns"); out.print(events);
                int next=((Random)a.global("c","random")).nextInt(); ActorCheck.equal(next,((Random)b.global("c","random")).nextInt(),"guard random stream"); out.println("N\t"+s+"\t"+next);
            }
            for(String s:new String[]{"hurt","repeat","air_hurt"}) {
                Object[] roots=new Object[4];
                for(var c:List.of(a,b)) { arena(c); Object p=c.player(100,199),e=enemy(c,180,s.equals("air_hurt")?119:199); weapons(c,0,1); roots[c==a?0:2]=p; roots[c==a?1:3]=e; }
                boolean sawHurt=false,sawRecovery=false;
                for(int frame=0;frame<60;++frame) {
                    String events="";
                    for(var c:List.of(a,b)) {
                        Object p=roots[c==a?0:2],e=roots[c==a?1:3]; c.set(p,"j","collisionBounds",new int[]{95,100,105,101});
                        int start=(int)c.global("bt","actorCount");
                        if(frame==4 || s.equals("repeat") && frame==5) c.call(e,"b","applyDamage",new Class<?>[]{int.class,boolean.class},1,true);
                        c.call(e,"s","update",ActorCheck.NONE);
                        String v=spawns(c,start,s,frame); if(c==a) events=v; else ActorCheck.equal(events,v,"hurt spawns");
                    }
                    out.print(events);
                    for(int phase=0;phase<2;++phase) {
                        if(phase==1) for(var c:List.of(a,b)) { Object e=roots[c==a?1:3]; c.call(e,"g","refreshBounds",ActorCheck.NONE); c.call(e,"f","advanceAnimation",ActorCheck.NONE); }
                        enemyRow(a,b,roots[1],roots[3],out,s,frame,phase);
                    }
                    int state=(int)a.get(roots[1],"ao","actionState"); sawHurt|=state==2; if(sawHurt && state==101) sawRecovery=true;
                }
                ActorCheck.equal(true,sawHurt && sawRecovery,"hurt/recovery must execute");
            }
            for(String s:new String[]{"player_hit","player_left","player_air","player_wall","player_death","player_repeat"}) {
                Object[] roots=new Object[4];
                for(var c:List.of(a,b)) {
                    arena(c); Object p=c.player(100,s.equals("player_air")?119:199),e=enemy(c,s.equals("player_left")?60:180,199); roots[c==a?0:2]=p; roots[c==a?1:3]=e;
                    if(s.equals("player_wall")) c.set(p,"cR","wallOnLeft",true);
                }
                for(int frame=0;frame<60;++frame) {
                    String events="";
                    for(var c:List.of(a,b)) {
                        Object p=roots[c==a?0:2],e=roots[c==a?1:3]; c.global("z","stateFrame",frame);
                        int start=(int)c.global("bt","actorCount");
                        if(frame==4 || s.equals("player_repeat") && (frame==5 || frame==12)) c.call(p,"a","a",new Class<?>[]{int.class,int.class,int.class,c.actor},4,s.equals("player_death")?100:4,0,e);
                        c.call(p,"s","update",ActorCheck.NONE);
                        String v=spawns(c,start,s,frame); if(c==a) events=v; else ActorCheck.equal(events,v,"player hit spawns");
                    }
                    out.print(events);
                    for(int phase=0;phase<2;++phase) {
                        if(phase==1) for(var c:List.of(a,b)) {
                            int inv=(int)c.get(null,"bE","playerInvulnerabilityMs"); if(inv>0) c.set(null,"bE","playerInvulnerabilityMs",inv-83);
                            Object p=roots[c==a?0:2]; c.call(p,"g","refreshBounds",ActorCheck.NONE); c.call(p,"f","advanceAnimation",ActorCheck.NONE);
                        }
                        ActorCheck.snapshot(a,b,roots[0],roots[2],s+frame);
                        out.print("P\t"+s+"\t"+frame+"\t"+phase);
                        for(String[] n:ActorCheck.SNAPSHOT) { Object v=a.get(roots[0],n[0],n[1]); if(v instanceof int[] r) for(int k:r) out.print("\t"+k); else out.print("\t"+v); }
                        out.println("\t"+a.global("p","health")+"\t"+a.get(null,"bE","playerInvulnerabilityMs")+"\t"+a.global("x","state"));
                    }
                    if((int)a.global("x","state")==12) break;
                }
            }
            for(int animation:new int[]{2,3,29}) for(int left=0;left<2;++left) for(String s:new String[]{"normal","paused","frozen"}) {
                Object[] effects=new Object[2];
                for(var c:List.of(a,b)) {
                    arena(c); Object p=c.player(100,199); c.set(p,"ak","facingLeft",left!=0);
                    Object e=c.call(p,"a","spawnActor",new Class<?>[]{int.class,int.class,int.class,int.class,int.class,int.class,boolean.class},39,1,animation,0,-20,302,false);
                    c.set(e,"aw","aw",animation==29?1000:0); c.set(e,"au","au",12); if(s.equals("frozen")) c.set(e,"c","flags",left|64); effects[c==a?0:1]=e;
                }
                String key=animation+"_"+left+"_"+s;
                for(int frame=-1;frame<40;++frame) {
                    boolean present=false;
                    for(var c:List.of(a,b)) {
                        Object e=effects[c==a?0:1]; boolean paused=s.equals("paused") && frame>=0 && frame<5; c.global("x","state",paused?14:8);
                        if(frame>=0) c.call(e,"s","update",ActorCheck.NONE);
                        present=false; for(int i=0;i<(int)c.global("bt","actorCount");++i) if(Array.get(c.global("bs","actors"),i)==e) present=true;
                        if(present && !paused && frame>=0) c.call(e,"f","advanceAnimation",ActorCheck.NONE);
                    }
                    Object e=effects[0]; ActorCheck.snapshot(a,b,e,effects[1],"effect "+key);
                    out.print("F\t"+key+"\t"+frame);
                    for(String[] n:new String[][]{{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},{"H","velocityY"},{"c","flags"},{"f","animationId"},{"h","animationFrame"},{"i","animationTick"},{"aw","aw"},{"au","au"}}) out.print("\t"+a.get(e,n[0],n[1]));
                    out.println("\t"+(present?1:0)); if(!present) break;
                }
            }
        }
        System.out.println("combat: "+ActorCheck.comparisons+" original/reference comparisons; "+output);
    }
}
