import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;

/** Removed-source selected-script trigger, actual gate script and gate collision. */
public final class RemovedGateTriggerCheck {
    static final String[] CASES={"normal","missing","early-remove","index-zero","pause","hidden","frozen","source-hidden"};
    static String row(ActorCheck.Access c,Object t,String s,int f,int phase,int index,int sourceIndex)throws Exception {
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("T\t"+s+"\t"+f+"\t"+phase);
        for(String[] n:new String[][]{{"al","uid"},{"K","positionX"},{"L","positionY"},{"c","flags"},{"at","at"},{"as","as"}})out.print("\t"+c.get(t,n[0],n[1]));
        out.print("\t"+(c.get(t,"s","s")!=null)+"\t"+DeathCheck.actors(c).contains(t));int[] rect=(int[])c.get(t,"j","collisionBounds");for(int n:rect==null?new int[4]:rect)out.print("\t"+n);
        out.print("\t"+((short[])c.global("cd","scriptTicks"))[index]+"\t"+c.global("cj","cj"));for(int n:((int[][])c.global("ce","scriptTrackOffsets"))[index])out.print("\t"+n);
        out.println("\t"+(((byte[])c.global("bD","bD"))[sourceIndex]==-99));return w.toString();
    }
    public static void main(String[] args)throws Exception {
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/removed-gate-trigger-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))) {
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES) {
                Object[][] roots=new Object[2][];int[] indices=new int[2];int sourceIndex=s.equals("index-zero")?0:5;
                for(var c:List.of(a,b)) {
                    c.clearWorld();c.player(1400,1500);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);c.global("bD","bD",new byte[200]);
                    c.global("bQ","mapWidthTiles",91);c.global("bR","mapHeightTiles",84);c.global("bS","levelPixelWidth",1820);c.global("bT","levelPixelHeight",1680);
                    byte[] cells=BehaviorCheck.resource(assets,3,2).clone();for(int i=0;i<cells.length;++i)if(cells[i]==-1)cells[i]=0;c.global("dL","collisionTiles",cells);
                    c.global("L","cameraX",1280);c.global("M","cameraY",1280);c.global("aa","viewBounds",new int[]{1280,1280,1520,1600});c.global("bc","bc",17);c.global("Z","Z",false);
                    BehaviorCheck.gameCall(c,"a","decodeScripts",new Class<?>[]{byte[].class},BehaviorCheck.resource(assets,3,1));
                    var find=c.game.getDeclaredMethod(c.renamed?"findScriptIndex":"x",int.class);find.setAccessible(true);indices[c==a?0:1]=(int)find.invoke(null,165);
                    c.global("ck","ck",new int[]{-1,-1,-1});c.global("cl","cl",new byte[3]);c.global("cj","cj",0);
                    Object source=c.create();c.set(source,"al","uid",57);c.set(source,"am","type",52);c.set(source,"c","flags",s.equals("source-hidden")?1024:0);
                    if(!s.equals("missing")){BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},source);c.set(source,"ae","persistenceIndex",sourceIndex);}
                    Object g=c.create();short[] gr={31,154,1468,1500,4,0,4,-1,-1};c.call(g,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(gr,20),true,0,9);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},g);
                    Object t=c.create();short[] tr={40,166,1381,1402,0,12,0,0,80,80,3,1,0,57,-1,-1,165,-1,-1};c.call(t,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(tr,20),true,0,19);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},t);c.set(t,"ae","persistenceIndex",6);
                    if(s.equals("hidden") || s.equals("frozen"))c.set(t,"c","flags",140|(s.equals("hidden")?1024:16384));
                    if(s.equals("early-remove"))BehaviorCheck.gameCall(c,"c","removeActor",new Class<?>[]{c.actor},source);
                    roots[c==a?0:1]=new Object[]{source,g,t};
                }
                for(int f=0;f<48;++f) {
                    boolean paused=s.equals("pause") && f>=7 && f<13;
                    for(var c:List.of(a,b)) {
                        var r=roots[c==a?0:1];c.global("x","state",paused?14:8);
                        if(!paused)BehaviorCheck.gameCall(c,"y","updateSelectedScripts",ActorCheck.NONE);
                        if(f==4 && !s.equals("missing") && !s.equals("early-remove"))BehaviorCheck.gameCall(c,"c","removeActor",new Class<?>[]{c.actor},r[0]);
                        c.call(r[1],"s","update",ActorCheck.NONE);if(DeathCheck.actors(c).contains(r[2]))c.call(r[2],"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase) {
                        if(phase==1 && !paused)for(var c:List.of(a,b)){c.call(roots[c==a?0:1][1],"g","refreshBounds",ActorCheck.NONE);c.call(roots[c==a?0:1][1],"f","advanceAnimation",ActorCheck.NONE);}
                        String r=row(a,roots[0][2],s,f,phase,indices[0],sourceIndex);ActorCheck.equal(r,row(b,roots[1][2],s,f,phase,indices[1],sourceIndex),"removed-source trigger lifecycle");out.print(r);
                        r=GateCheck.row(a,roots[0][1],s,f,phase);ActorCheck.equal(r,GateCheck.row(b,roots[1][1],s,f,phase),"removed-source gate script/cells");out.print(r);
                    }
                    if(s.equals("normal") && f==4)ActorCheck.equal(0,(int)((short[])a.global("cd","scriptTicks"))[indices[0]],"removal starts the script after selected update, leaving key zero for next tick");
                    if(s.equals("normal") && f==30)ActorCheck.equal(0,a.global("cj","cj"),"completed source trigger releases its selected-script slot");
                    if(s.equals("index-zero") && f==0)ActorCheck.equal(1,a.global("cj","cj"),"original index-zero shortcut also accepts a live source");
                }
            }
        }
        System.out.println("removed gate trigger: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
