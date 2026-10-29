import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/** Original Game.updateWorldActors with reciprocal player/type-1 combat and real queues. */
public final class LiveCombatCheck {
    static final String[] CASES={"fight","contact","left","lethal","combo","air","pair","hazard2","hazard_head","hazard_mixed","hazard3","hazard_inv","hazard_lethal"};
    static void paintTick(ActorCheck.Access c) throws Exception {
        Object p=c.global("bk","player"); int inv=(int)c.get(null,"bE","playerInvulnerabilityMs"); if(inv>0)c.set(null,"bE","playerInvulnerabilityMs",inv-83);
        List<Object> all=DeathCheck.actors(c); all.add(p); all.sort(Comparator.comparingInt(e->{try{return (int)c.get(e,"an","paintPriority");}catch(Exception ex){throw new RuntimeException(ex);}}));
        for(Object e:all) if(((int)c.get(e,"c","flags")&1024)==0) { c.call(e,"g","refreshBounds",ActorCheck.NONE); c.call(e,"f","advanceAnimation",ActorCheck.NONE); }
    }
    static void row(ActorCheck.Access a,Object e,PrintWriter out,String kind,String s,int frame,int phase) throws Exception {
        out.print(kind+"\t"+s+"\t"+frame+"\t"+phase);
        for(String[] n:ActorCheck.SNAPSHOT) { Object v=a.get(e,n[0],n[1]); if(v instanceof int[] r) for(int x:r)out.print("\t"+x); else out.print("\t"+v); }
        if(kind.equals("P"))out.println("\t"+a.global("p","health")+"\t"+a.get(null,"bE","playerInvulnerabilityMs")+"\t"+a.global("x","state"));
        else out.println("\t"+a.get(e,"ap","health")+"\t"+a.get(e,"az","az"));
    }
    static String birthEffects(ActorCheck.Access c,String s,int frame,int phase) throws Exception {
        var fx=new ArrayList<Object>();
        for(Object e:DeathCheck.actors(c))if((int)c.get(e,"am","type")==39)fx.add(e);
        var row=new StringBuilder("H\t"+s+"\t"+frame+"\t"+phase+"\t"+fx.size());
        for(Object e:fx)for(String[] n:new String[][]{{"f","animationId"},{"c","flags"},{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},{"h","animationFrame"},{"i","animationTick"}})row.append('\t').append(c.get(e,n[0],n[1]));
        return row.toString();
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]); Path file=assets.getParent().resolve("analysis/live-combat-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))) {
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES) {
                boolean hazard=s.startsWith("hazard");
                Object[] roots=new Object[4]; Map<ActorCheck.Access,Object> extra=new HashMap<>(); boolean dead=false;int minHealth=60,minEnemy=10;
                for(var c:List.of(a,b)) {
                    CombatCheck.arena(c); Object p=c.player(s.equals("left")?240:100,199); c.set(null,"bm","bm",true); c.set(null,"bn","bn",true);c.set(null,"bu","bu",false);CombatCheck.weapons(c,0,1);
                    if(s.equals("lethal"))c.global("p","health",10);
                    if(hazard) {
                        byte[] tiles=(byte[])c.global("dL","collisionTiles");
                        int row=s.equals("hazard_head") || s.equals("hazard_mixed")?7:9;
                        byte value=(byte)(s.equals("hazard2") || row==7?2:3);
                        Arrays.fill(tiles,row*40,(row+1)*40,value);
                        if(s.equals("hazard_mixed"))Arrays.fill(tiles,360,400,(byte)3);
                        if(s.equals("hazard_inv"))c.set(null,"bE","playerInvulnerabilityMs",600);
                        if(s.equals("hazard_lethal"))c.global("p","health",1);
                    }
                    short[] r={1,19,180,199,0,9,-100,-125,200,125,0,0,-1};byte[] stream=new byte[27];stream[0]=13;for(int i=0;i<13;++i){stream[1+i*2]=(byte)r[i];stream[2+i*2]=(byte)(r[i]>>8);}c.global("bF","bF",stream);
                    Object e=c.create();c.call(e,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,1,13);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},e);c.set(e,"ai","cameraDistanceBucket",0);
                    if(s.equals("pair")) {
                        short[] r2=r.clone();r2[1]=20;r2[2]=220;byte[] extended=Arrays.copyOf(stream,54);extended[27]=13;
                        for(int i=0;i<13;++i){extended[28+i*2]=(byte)r2[i];extended[29+i*2]=(byte)(r2[i]>>8);}c.global("bF","bF",extended);
                        Object e2=c.create();c.call(e2,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r2,20),true,28,13);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},e2);c.set(e2,"ai","cameraDistanceBucket",0);extra.put(c,e2);
                    }
                    roots[c==a?0:2]=p;roots[c==a?1:3]=e;
                }
                for(int frame=0;frame<120;++frame) {
                    int held=(!hazard && !s.equals("contact") && !s.equals("lethal") && frame<12)?(s.equals("left")?4112:8256):0;
                    int pressed=(!hazard && !s.equals("contact") && !s.equals("lethal") && frame>=8 && frame<60 && frame%6==2)?65568:0;
                    if(s.equals("air") && frame==4)pressed=16388;
                    if(s.equals("combo") && frame==10)pressed=65568;
                    for(var c:List.of(a,b)) {
                        Object p=roots[c==a?0:2],e=roots[c==a?1:3]; c.input(pressed,held);c.global("z","stateFrame",frame);
                        int px=(int)c.get(p,"K","positionX"),py=(int)c.get(p,"L","positionY");int cx=Math.max(0,Math.min(560,px-120)),cy=Math.max(0,Math.min(80,py-220));c.global("aa","viewBounds",new int[]{cx,cy,cx+240,cy+320});
                        BehaviorCheck.gameCall(c,"W","updateWorldActors",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase) {
                        if(phase==1)for(var c:List.of(a,b))paintTick(c);
                        ActorCheck.snapshot(a,b,roots[0],roots[2],s+" player "+frame);ActorCheck.snapshot(a,b,roots[1],roots[3],s+" enemy "+frame);
                        ActorCheck.equal(a.global("p","health"),b.global("p","health"),s+" health");
                        ActorCheck.equal(a.get(null,"bE","playerInvulnerabilityMs"),b.get(null,"bE","playerInvulnerabilityMs"),s+" invulnerability");
                        ActorCheck.equal(a.global("x","state"),b.global("x","state"),s+" state");
                        row(a,roots[0],out,"P",s,frame,phase);row(a,roots[1],out,"E",s,frame,phase);
                        if(hazard && frame==0) { String fx=birthEffects(a,s,frame,phase);ActorCheck.equal(fx,birthEffects(b,s,frame,phase),"hazard birth effects");out.println(fx); }
                        if(s.equals("pair")){ActorCheck.snapshot(a,b,extra.get(a),extra.get(b),"second enemy");row(a,extra.get(a),out,"E2",s,frame,phase);}
                    }
                    for(var c:List.of(a,b)) {
                        Object p=roots[c==a?0:2];int px=(int)c.get(p,"K","positionX"),py=(int)c.get(p,"L","positionY");int cx=Math.max(0,Math.min(560,px-120)),cy=Math.max(0,Math.min(80,py-220));
                        for(Object actor:DeathCheck.actors(c)) c.set(actor,"ai","cameraDistanceBucket",Math.abs((int)c.get(actor,"K","positionX")-(cx+120))/120+Math.abs((int)c.get(actor,"L","positionY")-(cy+160))/160);
                    }
                    minHealth=Math.min(minHealth,(int)a.global("p","health"));minEnemy=Math.min(minEnemy,(int)a.get(roots[1],"ap","health"));
                    if((int)a.global("x","state")==12){dead=true;break;}
                }
                if(s.equals("lethal"))ActorCheck.equal(true,dead,"live player death must finish");
                if(s.equals("contact"))ActorCheck.equal(true,minHealth<60,"live enemy must damage player");
                if(s.equals("fight"))ActorCheck.equal(true,minEnemy<=0,"live player must kill enemy");
                if(s.equals("hazard2") || s.equals("hazard_head") || s.equals("hazard_mixed") || s.equals("hazard_lethal"))ActorCheck.equal(true,dead,"tile hazard death must finish");
                if(hazard)ActorCheck.equal(true,minHealth<60,"tile hazards must change health");
            }
        }
        System.out.println("live reciprocal combat: "+ActorCheck.comparisons+" original/reference comparisons; "+file);
    }
}
