import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;

/**
 * Level-1 ambush scripts 23/24/114 (triggers 115/118/123, enemies 92/93/94 and zero-excursion chandeliers 89/90/91)
 * against original bytecode, on the real level-1 collision grid. Selected slots follow the original cj/ck bookkeeping.
 * Like ChandelierCheck this runs the real enemy/chandelier/trigger handlers, not a MIDP runtime or the full world queue.
 */
public final class AmbushCheck {
    // name, scripts (trigger uid:enemy:chandelier), player x, y, frames
    static final Object[][] CASES={
        {"s23","115:92:89",150,200},{"s24","118:93:90",450,200},{"s114","123:94:91",450,200},
        {"pair","118:93:90,123:94:91",450,200},{"outside","115:92:89",700,200},{"edge","115:92:89",276,200},{"edge-out","115:92:89",277,200},
        {"pause","115:92:89",150,200},{"hidden","118:93:90",450,200},{"leave","123:94:91",450,200},{"source-hidden","115:92:89",150,200},
        {"late-enemy-hidden","115:92:89",150,200}};
    static final int FRAMES=100;
    static final String[] OPS={"cj","ck"};
    static Map<Integer,short[]> records(byte[] data){
        var map=new LinkedHashMap<Integer,short[]>();int p=0;
        while(p<data.length){int n=data[p++]&255;short[] r=new short[n];for(int i=0;i<n;++i){r[i]=(short)((data[p]&255)|((data[p+1]&255)<<8));p+=2;}map.putIfAbsent((int)r[1],r);}
        return map;
    }
    static String row(ActorCheck.Access c,Object[] actors,int[] scriptIndices,int[] scriptUids,Object[] triggers,String name,int frame,int phase)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);
        out.print("S\t"+name+"\t"+frame+"\t"+phase+"\t"+c.global("cj","cj"));
        for(int n:(int[])c.global("ck","ck"))out.print("\t"+n);out.println();
        for(int i=0;i<scriptIndices.length;++i){
            out.print("T\t"+name+"\t"+frame+"\t"+phase+"\t"+scriptUids[i]+"\t"+((short[])c.global("cd","scriptTicks"))[scriptIndices[i]]);
            for(int n:((int[][])c.global("ce","scriptTrackOffsets"))[scriptIndices[i]])out.print("\t"+n);out.println();
        }
        for(int i=0;i<triggers.length;++i)out.println("R\t"+name+"\t"+frame+"\t"+phase+"\t"+i+"\t"+c.get(triggers[i],"as","as")+"\t"+DeathCheck.actors(c).contains(triggers[i]));
        for(int i=0;i<actors.length;++i){
            out.print("A\t"+name+"\t"+frame+"\t"+phase+"\t"+i);
            for(String[] n:ActorCheck.SNAPSHOT){Object v=c.get(actors[i],n[0],n[1]);if(v instanceof int[] a)for(int x:a)out.print("\t"+x);else out.print("\t"+v);}
            out.print("\t"+((boolean)c.get(actors[i],"ak","facingLeft")?1:0));out.println();
        }
        return w.toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/ambush-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            final var recs=records(BehaviorCheck.resource(assets,4,0));
            int pausedRows=0;
            for(Object[] cs:CASES){
                final String s=(String)cs[0];final String[] specs=((String)cs[1]).split(",");final int px=(int)cs[2],py=(int)cs[3];
                final int n=specs.length;final int[] trig=new int[n],enemy=new int[n],chand=new int[n],uids=new int[n],indices=new int[2*n];
                for(int i=0;i<n;++i){String[] f=specs[i].split(":");trig[i]=Integer.parseInt(f[0]);enemy[i]=Integer.parseInt(f[1]);chand[i]=Integer.parseInt(f[2]);uids[i]=(int)recs.get(trig[i])[16];}
                // Reference camera follows the player with the original 240x320 view, clamped to the level.
                final int cx=Math.max(0,Math.min(2360-240,px-120)),cy=Math.max(0,Math.min(1340-320,py-160));
                Object[][] actors=new Object[2][],triggers=new Object[2][];int[][] sidx=new int[2][];Object[] players=new Object[2];
                for(var c:List.of(a,b)){
                    final int k=c==a?0:1;
                    c.clearWorld();c.global("aG","levelIndex",1);Object p=c.player(px,py);c.set(p,"al","uid",131);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);c.global("bD","bD",new byte[200]);
                    c.global("bQ","mapWidthTiles",118);c.global("bR","mapHeightTiles",67);c.global("bS","levelPixelWidth",2360);c.global("bT","levelPixelHeight",1340);
                    byte[] cells=BehaviorCheck.resource(assets,4,2).clone();for(int i=0;i<cells.length;++i)if(cells[i]==-1)cells[i]=0;c.global("dL","collisionTiles",cells);
                    c.global("L","cameraX",cx);c.global("M","cameraY",cy);c.global("aa","viewBounds",new int[]{cx,cy,cx+240,cy+320});c.global("bc","bc",17);c.global("Z","Z",false);
                    BehaviorCheck.gameCall(c,"a","decodeScripts",new Class<?>[]{byte[].class},BehaviorCheck.resource(assets,4,1));
                    var find=c.game.getDeclaredMethod(c.renamed?"findScriptIndex":"x",int.class);find.setAccessible(true);
                    sidx[k]=new int[n];for(int i=0;i<n;++i)sidx[k][i]=(int)find.invoke(null,uids[i]);
                    c.global("ck","ck",new int[]{-1,-1,-1});c.global("cl","cl",new byte[3]);c.global("cj","cj",0);
                    List<Object> list=new ArrayList<>();Object[] en=new Object[n],ch=new Object[n],tr=new Object[n];
                    for(int i=0;i<n;++i){en[i]=c.create();short[] r=recs.get(enemy[i]);c.call(en[i],"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,0,r.length);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},en[i]);}
                    for(int i=0;i<n;++i){ch[i]=c.create();short[] r=recs.get(chand[i]);c.call(ch[i],"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,0,r.length);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},ch[i]);}
                    for(int i=0;i<n;++i){tr[i]=c.create();short[] r=recs.get(trig[i]).clone();c.call(tr[i],"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,0,r.length);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},tr[i]);}
                    BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},p);
                    if(s.equals("source-hidden"))c.set(p,"c","flags",1024);
                    if(s.equals("hidden"))c.set(tr[0],"c","flags",136|1024);
                    if(s.equals("late-enemy-hidden"))c.set(en[0],"c","flags",2056|1024);
                    Object[] all=new Object[2*n];System.arraycopy(en,0,all,0,n);System.arraycopy(ch,0,all,n,n);
                    actors[k]=all;triggers[k]=tr;players[k]=p;
                }
                int[] idx=sidx[0];
                for(int f=0;f<FRAMES;++f){
                    final boolean paused=s.equals("pause")&&f>=4&&f<10;if(paused)++pausedRows;
                    for(var c:List.of(a,b)){final int k=c==a?0:1;Object[] all=actors[k];Object[] tr=triggers[k];
                        c.global("x","state",paused?14:8);
                        if(s.equals("leave")&&f==2)c.set(players[k],"K","positionX",900);
                        if(!paused)BehaviorCheck.gameCall(c,"y","updateSelectedScripts",ActorCheck.NONE);
                        // Level order: enemies, chandeliers, triggers, as shipped in the object stream.
                        for(int i=0;i<n;++i)if(DeathCheck.actors(c).contains(all[i]))c.call(all[i],"s","update",ActorCheck.NONE);
                        for(int i=0;i<n;++i)if(DeathCheck.actors(c).contains(all[n+i]))c.call(all[n+i],"s","update",ActorCheck.NONE);
                        for(int i=0;i<n;++i)if(DeathCheck.actors(c).contains(tr[i]))c.call(tr[i],"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase){
                        if(phase==1&&!paused)for(var c:List.of(a,b))for(Object o:actors[c==a?0:1]){c.call(o,"g","refreshBounds",ActorCheck.NONE);c.call(o,"f","advanceAnimation",ActorCheck.NONE);}
                        String line=row(a,actors[0],sidx[0],uids,triggers[0],s,f,phase);
                        ActorCheck.equal(line,row(b,actors[1],sidx[1],uids,triggers[1],s,f,phase),"ambush timeline "+s+" "+f+"/"+phase);out.print(line);
                    }
                    if(s.equals("s23")||s.equals("source-hidden")){
                        if(f==0)ActorCheck.equal(0,(int)((short[])a.global("cd","scriptTicks"))[idx[0]],"trigger starts after selected update");
                        if(f==1)ActorCheck.equal(16384,(int)a.get(actors[0][0],"c","flags")&16384,"enemy is frozen before its handler");
                        if(f==12){ActorCheck.equal(0,a.global("cj","cj"),"completed selected slot released");ActorCheck.equal(false,DeathCheck.actors(a).contains(triggers[0][0]),"completed trigger removed");
                            ActorCheck.equal(102,a.get(actors[0][1],"ao","actionState"),"chandelier enters falling handler");ActorCheck.equal(0,(int)a.get(actors[0][0],"c","flags")&16384,"enemy released");}
                    }
                    if(s.equals("pair")){
                        if(f==0)ActorCheck.equal(2,a.global("cj","cj"),"both triggers select a slot in one tick");
                        if(f==12)ActorCheck.equal(0,a.global("cj","cj"),"both slots released");
                    }
                    if((s.equals("outside")||s.equals("edge-out")||s.equals("hidden"))&&f==FRAMES-1){ActorCheck.equal(0,a.global("cj","cj"),"no slot was selected");ActorCheck.equal(-1,(int)((short[])a.global("cd","scriptTicks"))[idx[0]],"script never started");}
                    if(s.equals("edge")&&f==12)ActorCheck.equal(0,a.global("cj","cj"),"edge overlap runs the script to completion");
                }
            }
            if(pausedRows!=6)throw new AssertionError("pause fixture length");
        }
        System.out.println("ambush: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
