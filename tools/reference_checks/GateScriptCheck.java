import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;

/** Script 165's real actor track, with isolated gate handlers and explicit paint clocks. */
public final class GateScriptCheck {
    static final String[] CASES={"shipped","pause","restart","stop","start-again","loop","no-handler","animation"};
    static String timer(ActorCheck.Access c,String s,int f,int phase,int index)throws Exception {
        String row="T\t"+s+"\t"+f+"\t"+phase+"\t"+((short[])c.global("cd","scriptTicks"))[index];
        for(int n:((int[][])c.global("ce","scriptTrackOffsets"))[index])row+="\t"+n;return row+"\n";
    }
    public static void main(String[] args)throws Exception {
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/gate-script-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))) {
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES) {
                Object[] roots=new Object[2];int[] indices=new int[2];
                for(var c:List.of(a,b)) {
                    c.clearWorld();c.player(1400,1500);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);
                    c.global("bQ","mapWidthTiles",91);c.global("bR","mapHeightTiles",84);c.global("bS","levelPixelWidth",1820);c.global("bT","levelPixelHeight",1680);
                    byte[] cells=BehaviorCheck.resource(assets,3,2).clone();for(int i=0;i<cells.length;++i)if(cells[i]==-1)cells[i]=0;c.global("dL","collisionTiles",cells);
                    c.global("L","cameraX",1280);c.global("M","cameraY",1280);c.global("aa","viewBounds",new int[]{1280,1280,1520,1600});
                    c.global("bc","bc",17);c.global("Z","Z",false);
                    BehaviorCheck.gameCall(c,"a","decodeScripts",new Class<?>[]{byte[].class},BehaviorCheck.resource(assets,3,1));
                    var find=c.game.getDeclaredMethod(c.renamed?"findScriptIndex":"x",int.class);find.setAccessible(true);int index=(int)find.invoke(null,165);indices[c==a?0:1]=index;
                    c.global("ck","ck",new int[]{index,0,0});c.global("cl","cl",new byte[]{1,0,0});c.global("cj","cj",1);
                    if(s.equals("animation"))((byte[][][])c.global("cb","scriptTracks"))[index][0][15]=5;
                    short[] record={31,154,1468,1500,4,0,4,-1,-1};Object g=c.create();c.call(g,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(record,20),true,0,9);
                    BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},g);roots[c==a?0:1]=g;
                    BehaviorCheck.gameCall(c,"t","startScript",new Class<?>[]{int.class},index);
                }
                for(int f=0;f<48;++f) {
                    boolean paused=s.equals("pause") && f>=7 && f<13;
                    for(var c:List.of(a,b)) {
                        int id=c==a?0:1,index=indices[id];c.global("x","state",paused?14:8);
                        if(s.equals("restart") && f==32)BehaviorCheck.gameCall(c,"v","restartScript",new Class<?>[]{int.class},index);
                        if(s.equals("stop") && f==8)BehaviorCheck.gameCall(c,"u","stopAndRewindScript",new Class<?>[]{int.class},index);
                        if(s.equals("start-again") && f==32)BehaviorCheck.gameCall(c,"t","startScript",new Class<?>[]{int.class},index);
                        if(!paused)BehaviorCheck.gameCall(c,"b","updateScript",new Class<?>[]{int.class,boolean.class,int.class},index,true,s.equals("loop")?0:1);
                        if(!s.equals("no-handler"))c.call(roots[id],"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase) {
                        if(phase==1 && !paused)for(var c:List.of(a,b)){c.call(roots[c==a?0:1],"g","refreshBounds",ActorCheck.NONE);c.call(roots[c==a?0:1],"f","advanceAnimation",ActorCheck.NONE);}
                        String row=timer(a,s,f,phase,indices[0]);ActorCheck.equal(row,timer(b,s,f,phase,indices[1]),"script gate timers/offsets");out.print(row);
                        row=GateCheck.row(a,roots[0],s,f,phase);ActorCheck.equal(row,GateCheck.row(b,roots[1],s,f,phase),"script gate state/cells");out.print(row);
                    }
                    if(s.equals("shipped") && f==1)ActorCheck.equal(1497,a.get(roots[0],"L","positionY"),"script uses old counter with truncating interpolation");
                    if(s.equals("shipped") && f==25){ActorCheck.equal(-2,(int)((short[])a.global("cd","scriptTicks"))[indices[0]],"one-shot finishes on key tick 25");ActorCheck.equal(1415,a.get(roots[0],"L","positionY"),"gate reaches exact script endpoint");int zero=0;byte[] cells=(byte[])a.global("dL","collisionTiles");for(int y=75;y<=78;++y)if(cells[y*91+73]==0)++zero;ActorCheck.equal(4,zero,"script action 103 clears the doorway");}
                }
            }
        }
        System.out.println("gate script: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
