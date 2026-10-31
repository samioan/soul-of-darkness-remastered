import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;

/**
 * Integrated type-3 actor against original bytecode: movement, held-shot birth,
 * parent/child dispatch, linked chest, damage, death/loot and respawn. The queue
 * emulates Game's per-tick queue for the actors involved, not the full world.
 */
public final class Type3ActorCheck {
    // name, variantData index, record flags, stat row, x, y, linked chest uid, player start/end x
    static final Object[][] CASES={
        {"charger",7,9,0,400,100,-1,320,380},{"charger-left",7,8,0,400,100,-1,480,420},{"charger-touch",7,9,0,300,100,-1,285,285},{"charger-away",7,9,0,400,100,-1,700,700},
        {"thrower",9,9,0,500,100,-1,380,380},{"thrower-left",9,8,0,500,100,-1,620,620},{"thrower-near",9,9,0,500,100,-1,490,490},{"thrower-walk",9,9,0,500,100,-1,400,260},{"thrower-hurt",9,9,0,500,100,-1,380,380},
        {"thrower-fade",9,2185,4,500,100,-1,380,380},{"charger-fade",7,2185,4,400,100,-1,320,320},
        {"runner",24,2056,0,300,100,353,330,330},{"runner-kill",24,2056,0,300,100,353,330,330},{"runner-free",24,2056,0,300,100,-1,330,330},
        {"ledge-runner",24,2057,0,470,100,-1,500,500},{"ledge-charger",7,9,0,470,100,-1,500,560},{"ledge-thrower",9,9,0,470,100,-1,590,590},
        {"pause",9,9,0,500,100,-1,380,380},{"hidden",9,1033,0,500,100,-1,380,380},{"frozen",9,16393,0,500,100,-1,380,380},{"flag32",9,41,0,500,100,-1,380,380},
        {"slope",9,9,0,500,100,-1,380,380},{"thrower-reflect",9,9,0,500,100,-1,380,380},{"melee",9,9,0,500,100,-1,490,490}};
    static final int FRAMES=140;
    static int damage(String s,int f){
        switch(s){
            case "thrower-hurt": return f==10?5:f==40?5:f==60?1000:0;
            case "thrower-fade": return f==60?1000:0;
            case "charger-fade": return f==30?1000:0;
            case "runner-kill": return f==70?1000:0;
            case "thrower-reflect": return f==30?1000:0;
            default: return 0;
        }
    }
    static Object spawnRecord(ActorCheck.Access c,short[] r,int offset,int count,int persistence)throws Exception{
        Object e=c.create();
        c.call(e,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,offset,count);
        BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},e);
        if(persistence>=0)c.set(e,"ae","persistenceIndex",persistence);
        return e;
    }
    static boolean live(ActorCheck.Access c,Object e)throws Exception{return DeathCheck.actors(c).contains(e);}
    static int type(ActorCheck.Access c,Object e)throws Exception{return (int)c.get(e,"am","type");}
    static String enemyRow(ActorCheck.Access c,Object e,String s,int f,int phase)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("T\t"+s+"\t"+f+"\t"+phase);
        for(String[] n:ActorCheck.SNAPSHOT){Object v=c.get(e,n[0],n[1]);if(v==null)for(int i=0;i<4;++i)out.print("\t0");else if(v instanceof int[] r)for(int x:r)out.print("\t"+x);else out.print("\t"+v);}
        for(String[] n:new String[][]{{"aw","aw"},{"ax","ax"},{"az","az"},{"ap","health"},{"ak","facingLeft"},{"Q","Q"}}){Object v=c.get(e,n[0],n[1]);out.print("\t"+(v instanceof Boolean b?(b?1:0):v));}
        for(String[] n:new String[][]{{"m","customBounds"},{"aE","interactionBounds"}})for(int x:(int[])c.get(e,n[0],n[1]))out.print("\t"+x);
        Object held=c.get(e,"r","attachedActor");
        out.print("\t"+c.get(e,"di","di")+"\t"+(held==null?0:type(c,held))+"\t"+(live(c,e)?1:0));out.println();return w.toString();
    }
    static String shotRow(ActorCheck.Access c,Object e,String s,int f,int phase,int index)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("S\t"+s+"\t"+f+"\t"+phase+"\t"+index);
        for(String[] n:new String[][]{{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},{"G","velocityX"},{"H","velocityY"},{"I","accelerationX"},{"J","accelerationY"},{"c","flags"},{"f","animationId"},{"g","pendingAnimationId"},{"h","animationFrame"},{"i","animationTick"},{"ao","actionState"},{"aw","aw"},{"au","au"},{"az","az"}})out.print("\t"+c.get(e,n[0],n[1]));
        out.print("\t1");for(String[] n:new String[][]{{"bB","grounded"},{"bD","ceilingHit"},{"cR","wallOnLeft"},{"cS","wallOnRight"},{"ak","facingLeft"}})out.print("\t"+((boolean)c.get(e,n[0],n[1])?1:0));
        for(String[] n:new String[][]{{"j","collisionBounds"},{"k","attackBounds"},{"l","visualBounds"}}){int[] r=(int[])c.get(e,n[0],n[1]);for(int v:r==null?new int[4]:r)out.print("\t"+v);}
        out.println();return w.toString();
    }
    static String chestRow(ActorCheck.Access c,Object e,String s,int f,int phase)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("C\t"+s+"\t"+f+"\t"+phase);
        for(String[] n:new String[][]{{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},{"G","velocityX"},{"H","velocityY"},{"I","accelerationX"},{"J","accelerationY"},{"c","flags"},{"f","animationId"},{"g","pendingAnimationId"},{"h","animationFrame"},{"i","animationTick"},{"ao","actionState"},{"aw","aw"}})out.print("\t"+c.get(e,n[0],n[1]));
        out.print("\t"+((boolean)c.get(e,"bB","grounded")?1:0)+"\t"+(live(c,e)?1:0));
        // Like the container oracle, a free chest's bounds are compared after the animation phase refreshes them.
        int[] r=(int[])c.get(e,"j","collisionBounds");boolean compare=phase==1 || ((int)c.get(e,"c","flags")&32)!=0;
        for(int v:r==null || !compare?new int[4]:r)out.print("\t"+v);
        out.println();return w.toString();
    }
    static String births(ActorCheck.Access c,Set<Object> before,String s,int f)throws Exception{
        List<String> rows=new ArrayList<>();
        for(Object e:DeathCheck.actors(c))if(!before.contains(e)){
            StringBuilder b=new StringBuilder("L\t"+s+"\t"+f);
            for(String[] n:new String[][]{{"am","type"},{"f","animationId"},{"K","positionX"},{"L","positionY"},{"c","flags"}})b.append('\t').append(c.get(e,n[0],n[1]));
            rows.add(b.toString());
        }
        Collections.sort(rows);StringBuilder text=new StringBuilder();for(String r:rows)text.append(r).append('\n');return text.toString();
    }
    static void update(ActorCheck.Access c,Object e)throws Exception{c.call(e,"s","update",ActorCheck.NONE);}
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/type3-actor-oracle.tsv");int count=0;
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(Object[] row:CASES){
                String s=(String)row[0];int p10=(int)row[1],flags=(int)row[2],stat=(int)row[3],ex=(int)row[4],ey=(int)row[5],link=(int)row[6],px0=(int)row[7],px1=(int)row[8];
                Object[] roots=new Object[2],chests=new Object[2];boolean sawShot=false,sawThrow=false,sawChest=false;
                for(var c:List.of(a,b)){
                    CombatCheck.arena(c);byte[] tiles=(byte[])c.global("dL","collisionTiles");
                    if(s.startsWith("runner"))for(int y=0;y<10;++y)tiles[y*40+5]=24;
                    if(s.startsWith("ledge"))for(int y=10;y<15;++y)for(int x=25;x<40;++x)tiles[y*40+x]=0;
                    if(s.equals("slope"))Arrays.fill(tiles,400,440,(byte)18);
                    c.player(px0,199);c.set(null,"bE","playerInvulnerabilityMs",60000);if(s.equals("melee"))CombatCheck.weapons(c,0,1);c.global("bD","bD",new byte[32]);c.global("bH","bH",new short[30]);
                    short[] r={3,48,(short)ex,(short)ey,9,(short)flags,-125,-125,250,175,(short)p10,(short)stat,(short)link};
                    if(s.startsWith("runner")){r[6]=12;r[7]=-200;r[8]=40;r[9]=175;}
                    byte[] stream=new byte[27];stream[0]=13;for(int i=0;i<13;++i){stream[1+2*i]=(byte)r[i];stream[2+2*i]=(byte)(r[i]>>>8);}c.global("bF","bF",stream);
                    roots[c==a?0:1]=spawnRecord(c,r,1,13,3);
                    if(link!=-1)chests[c==a?0:1]=spawnRecord(c,new short[]{52,(short)link,(short)ex,(short)(ey-31),0,0,1},0,7,-1);
                }
                for(int f=0;f<FRAMES;++f){
                    boolean paused=s.equals("pause") && f>=6 && f<16;int dmg=damage(s,f);
                    int px=px0+(px1-px0)*f/FRAMES;
                    List<String> events=new ArrayList<>();
                    for(var c:List.of(a,b)){
                        Object e=roots[c==a?0:1],player=c.global("bk","player");
                        c.global("x","state",paused?14:8);c.input(0,0);
                        c.set(player,"a","fixedX",px*256);c.set(player,"b","fixedY",199*256);c.set(player,"K","positionX",px);c.set(player,"L","positionY",199);
                        boolean reflect=s.equals("thrower-reflect");
                        c.set(player,"j","collisionBounds",new int[]{px-8,150,px+8,199});c.set(player,"k","attackBounds",new int[]{10000,0,10001,1});
                        if(reflect && f>=56 && f<=64)for(Object shot:DeathCheck.actors(c))if(type(c,shot)==16 && (int)c.get(shot,"ao","actionState")==103){int[] cb=(int[])c.get(shot,"j","collisionBounds");if(cb!=null)c.set(player,"k","attackBounds",cb.clone());}
                        if(s.equals("melee") && (f==10 || f==11 || f==30 || f==31 || f==70 || f==71)){int[] cb=(int[])c.get(e,"j","collisionBounds");if(cb!=null)c.set(player,"k","attackBounds",cb.clone());}
                        var before=new HashSet<>(DeathCheck.actors(c));
                        if(dmg>0 && live(c,e))c.call(e,"p","applyDamageWithReaction",ActorCheck.INT,dmg);
                        List<Object> queue=new ArrayList<>();
                        for(Object actor:DeathCheck.actors(c)){int t=type(c,actor);if(t!=3 && t!=16 && t!=52)continue;c.set(actor,"aq","updatedThisTick",false);int fl=(int)c.get(actor,"c","flags");if((fl&16)!=0 || (fl&(256|32))==0)queue.add(actor);}
                        for(Object actor:queue)if(live(c,actor)){
                            update(c,actor);Object held=c.get(actor,"r","attachedActor");
                            if(held!=null && live(c,held) && !(boolean)c.get(held,"aq","updatedThisTick"))update(c,held);
                        }
                        events.add(births(c,before,s,f));
                    }
                    ActorCheck.equal(events.get(0),events.get(1),"type-3 birth events");out.print(events.get(0));
                    for(int phase=0;phase<2;++phase){
                        if(phase==1 && !paused)for(var c:List.of(a,b)){
                            List<Object> all=new ArrayList<>();all.add(roots[c==a?0:1]);for(Object actor:DeathCheck.actors(c))if(type(c,actor)==16)all.add(actor);if(chests[c==a?0:1]!=null)all.add(chests[c==a?0:1]);
                            for(Object actor:all)if(live(c,actor) && ((int)c.get(actor,"c","flags")&1024)==0 && !(type(c,actor)==52 && ((int)c.get(actor,"c","flags")&32)!=0)){c.call(actor,"g","refreshBounds",ActorCheck.NONE);c.call(actor,"f","advanceAnimation",ActorCheck.NONE);}
                        }
                        StringBuilder[] text={new StringBuilder(),new StringBuilder()};
                        for(var c:List.of(a,b)){
                            StringBuilder t=text[c==a?0:1];t.append(enemyRow(c,roots[c==a?0:1],s,f,phase));
                            int index=0;for(Object actor:DeathCheck.actors(c))if(type(c,actor)==16)t.append(shotRow(c,actor,s,f,phase,index++));
                            if(chests[c==a?0:1]!=null)t.append(chestRow(c,chests[c==a?0:1],s,f,phase));
                            if(s.equals("melee"))t.append("P\t"+s+"\t"+f+"\t"+phase+"\t"+c.global("p","health")+"\t"+c.get(null,"bE","playerInvulnerabilityMs")+"\n");
                        }
                        ActorCheck.equal(text[0].toString(),text[1].toString(),"type-3 actor/child/chest rows");out.print(text[0]);++count;
                    }
                    for(Object shot:DeathCheck.actors(a))if(type(a,shot)==16){sawShot=true;int st=(int)a.get(shot,"ao","actionState");sawThrow|=st==102 || st==103;}
                    if(chests[0]!=null)sawChest|=((int)a.get(chests[0],"c","flags")&32)!=0;
                }
                if(s.equals("thrower"))ActorCheck.equal(true,sawShot && sawThrow,"thrower creates and throws a shot");
                if(s.equals("runner"))ActorCheck.equal(true,sawChest,"runner carries its linked chest");
                int next=((Random)a.global("c","random")).nextInt();ActorCheck.equal(next,((Random)b.global("c","random")).nextInt(),"type-3 random stream");out.println("N\t"+s+"\t"+next);
            }
        }
        System.out.println("type-3 actor: "+ActorCheck.comparisons+" original/reference expectations; "+file+"; "+count+" snapshot groups");
    }
}
