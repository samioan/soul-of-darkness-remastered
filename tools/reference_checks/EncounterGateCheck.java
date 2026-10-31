import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;

/**
 * Second-level removed-source script trigger: condition 4 over two placed enemies (UIDs 98/102) starts selected
 * script 95, which drives free-standing gate 87 on the level-1 collision grid. Sources are placeholder actors so only
 * the original type-40 handler, selected-script bookkeeping and gate handler run.
 */
public final class EncounterGateCheck {
    static final String[] CASES={"normal","reverse","one-left","missing-first","missing-second","early-remove","index-zero","pause","hidden","frozen","source-hidden"};
    static String row(ActorCheck.Access c,Object t,String s,int f,int phase,int index,int[] sources)throws Exception {
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("T\t"+s+"\t"+f+"\t"+phase);
        for(String[] n:new String[][]{{"al","uid"},{"K","positionX"},{"L","positionY"},{"c","flags"},{"at","at"},{"ar","ar"},{"as","as"},{"y","y"},{"z","z"}})out.print("\t"+c.get(t,n[0],n[1]));
        out.print("\t"+(c.get(t,"s","s")!=null)+"\t"+(c.get(t,"t","t")!=null)+"\t"+DeathCheck.actors(c).contains(t));int[] rect=(int[])c.get(t,"j","collisionBounds");for(int n:rect==null?new int[4]:rect)out.print("\t"+n);
        out.print("\t"+((short[])c.global("cd","scriptTicks"))[index]+"\t"+c.global("cj","cj"));for(int n:((int[][])c.global("ce","scriptTrackOffsets"))[index])out.print("\t"+n);
        for(int source:sources)out.print("\t"+(((byte[])c.global("bD","bD"))[source]==-99));out.println();return w.toString();
    }
    public static void main(String[] args)throws Exception {
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/encounter-gate-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))) {
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES) {
                Object[][] roots=new Object[2][];int[] indices=new int[2];int[] sourceIndex=s.equals("index-zero")?new int[]{0,0}:new int[]{5,6};
                for(var c:List.of(a,b)) {
                    c.clearWorld();c.player(850,380);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);c.global("bD","bD",new byte[200]);
                    c.global("bQ","mapWidthTiles",118);c.global("bR","mapHeightTiles",67);c.global("bS","levelPixelWidth",2360);c.global("bT","levelPixelHeight",1340);
                    byte[] cells=BehaviorCheck.resource(assets,4,2).clone();for(int i=0;i<cells.length;++i)if(cells[i]==-1)cells[i]=0;c.global("dL","collisionTiles",cells);
                    c.global("L","cameraX",720);c.global("M","cameraY",240);c.global("aa","viewBounds",new int[]{720,240,960,560});c.global("bc","bc",17);c.global("Z","Z",false);
                    BehaviorCheck.gameCall(c,"a","decodeScripts",new Class<?>[]{byte[].class},BehaviorCheck.resource(assets,4,1));
                    var find=c.game.getDeclaredMethod(c.renamed?"findScriptIndex":"x",int.class);find.setAccessible(true);indices[c==a?0:1]=(int)find.invoke(null,95);
                    c.global("ck","ck",new int[]{-1,-1,-1});c.global("cl","cl",new byte[3]);c.global("cj","cj",0);
                    Object[] sources=new Object[2];int[] uids={98,102};
                    for(int i=0;i<2;++i) {
                        Object source=c.create();c.set(source,"al","uid",uids[i]);c.set(source,"am","type",2);c.set(source,"c","flags",s.equals("source-hidden")?1024:0);
                        boolean present=!(s.equals("missing-first") && i==0) && !(s.equals("missing-second") && i==1);
                        if(present){BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},source);c.set(source,"ae","persistenceIndex",sourceIndex[i]);}
                        sources[i]=source;
                    }
                    Object g=c.create();short[] gr={31,87,847,380,4,0,4,-1,-1};c.call(g,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(gr,20),true,0,9);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},g);
                    Object t=c.create();short[] tr={40,103,658,377,0,8,0,0,30,30,4,1,0,98,102,-1,95,-1,-1};c.call(t,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(tr,20),true,0,19);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},t);c.set(t,"ae","persistenceIndex",7);
                    if(s.equals("hidden") || s.equals("frozen"))c.set(t,"c","flags",140|(s.equals("hidden")?1024:16384));
                    if(s.equals("early-remove"))for(Object source:sources)BehaviorCheck.gameCall(c,"c","removeActor",new Class<?>[]{c.actor},source);
                    roots[c==a?0:1]=new Object[]{sources[0],sources[1],g,t};
                }
                for(int f=0;f<48;++f) {
                    boolean paused=s.equals("pause") && f>=3 && f<10;
                    for(var c:List.of(a,b)) {
                        var r=roots[c==a?0:1];c.global("x","state",paused?14:8);
                        if(!paused)BehaviorCheck.gameCall(c,"y","updateSelectedScripts",ActorCheck.NONE);
                        if(!s.equals("early-remove") && !s.equals("index-zero") && !s.equals("missing-first") && !s.equals("missing-second")) {
                            int first=s.equals("reverse")?1:0;
                            if(f==4)BehaviorCheck.gameCall(c,"c","removeActor",new Class<?>[]{c.actor},r[first]);
                            if(f==8 && !s.equals("one-left"))BehaviorCheck.gameCall(c,"c","removeActor",new Class<?>[]{c.actor},r[1-first]);
                        }
                        // The missing-source cases remove the one source that exists, then the sequence must still behave.
                        if(s.equals("missing-first") && f==4)BehaviorCheck.gameCall(c,"c","removeActor",new Class<?>[]{c.actor},r[1]);
                        if(s.equals("missing-second") && f==4)BehaviorCheck.gameCall(c,"c","removeActor",new Class<?>[]{c.actor},r[0]);
                        c.call(r[2],"s","update",ActorCheck.NONE);if(DeathCheck.actors(c).contains(r[3]))c.call(r[3],"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase) {
                        if(phase==1 && !paused)for(var c:List.of(a,b)){c.call(roots[c==a?0:1][2],"g","refreshBounds",ActorCheck.NONE);c.call(roots[c==a?0:1][2],"f","advanceAnimation",ActorCheck.NONE);}
                        String r=row(a,roots[0][3],s,f,phase,indices[0],sourceIndex);ActorCheck.equal(r,row(b,roots[1][3],s,f,phase,indices[1],sourceIndex),"encounter script trigger lifecycle");out.print(r);
                        r=GateCheck.row(a,roots[0][2],s,f,phase);ActorCheck.equal(r,GateCheck.row(b,roots[1][2],s,f,phase),"encounter gate script/cells");out.print(r);
                    }
                    if(s.equals("normal") && f==8)ActorCheck.equal(0,(int)((short[])a.global("cd","scriptTicks"))[indices[0]],"second removal starts the script after the selected update, leaving key zero for next tick");
                    if(s.equals("normal") && f==4)ActorCheck.equal(-1,(int)((short[])a.global("cd","scriptTicks"))[indices[0]],"the first removal alone cannot start condition 4");
                    if(s.equals("normal") && f==30)ActorCheck.equal(0,a.global("cj","cj"),"completed trigger releases its selected-script slot");
                    if(s.equals("index-zero") && f==0)ActorCheck.equal(1,a.global("cj","cj"),"index-zero shortcut accepts two live sources");
                    if(s.equals("missing-second") && f==47)ActorCheck.equal(-1,a.get(roots[0][3],"as","as"),"an unresolved second source blocks condition 4 forever");
                    if(s.equals("missing-first") && f==47)ActorCheck.equal(-1,a.get(roots[0][3],"y","y"),"condition 4 clears an unresolved first source UID and only waits for the second");
                }
            }
        }
        System.out.println("encounter gate trigger: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
