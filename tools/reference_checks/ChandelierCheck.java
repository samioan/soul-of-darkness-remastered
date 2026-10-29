import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;

/** Script 160, trigger 161 and the ensuing real actor handlers against original bytecode. */
public final class ChandelierCheck {
    static final String[] CASES={"normal","leave","outside","edge","missing-source","pause","hidden","frozen","source-hidden","completion-hidden"};
    static String row(ActorCheck.Access c,Object[] r,String name,int frame,int phase,int index)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);
        out.print("S\t"+name+"\t"+frame+"\t"+phase+"\t"+((short[])c.global("cd","scriptTicks"))[index]+"\t"+c.global("cj","cj")+"\t"+c.get(r[2],"as","as")+"\t"+DeathCheck.actors(c).contains(r[2]));
        for(int n:((int[][])c.global("ce","scriptTrackOffsets"))[index])out.print("\t"+n);out.println();
        for(int i=0;i<2;++i){out.print("A\t"+name+"\t"+frame+"\t"+phase+"\t"+i);for(String[] n:ActorCheck.SNAPSHOT){Object v=c.get(r[i],n[0],n[1]);if(v instanceof int[] a)for(int x:a)out.print("\t"+x);else out.print("\t"+v);}out.println();}
        return w.toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/chandelier-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES){Object[][] roots=new Object[2][];int[] indices=new int[2];
                for(var c:List.of(a,b)){
                    c.clearWorld();Object p=c.player(s.equals("outside")?300:s.equals("edge")?158:100,1100);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);c.global("bD","bD",new byte[200]);
                    c.global("bQ","mapWidthTiles",91);c.global("bR","mapHeightTiles",84);c.global("bS","levelPixelWidth",1820);c.global("bT","levelPixelHeight",1680);
                    byte[] cells=BehaviorCheck.resource(assets,3,2).clone();for(int i=0;i<cells.length;++i)if(cells[i]==-1)cells[i]=0;c.global("dL","collisionTiles",cells);
                    c.global("L","cameraX",0);c.global("M","cameraY",880);c.global("aa","viewBounds",new int[]{0,880,240,1200});c.global("bc","bc",17);c.global("Z","Z",false);
                    BehaviorCheck.gameCall(c,"a","decodeScripts",new Class<?>[]{byte[].class},BehaviorCheck.resource(assets,3,1));var find=c.game.getDeclaredMethod(c.renamed?"findScriptIndex":"x",int.class);find.setAccessible(true);indices[c==a?0:1]=(int)find.invoke(null,160);
                    c.global("ck","ck",new int[]{-1,-1,-1});c.global("cl","cl",new byte[3]);c.global("cj","cj",0);
                    short[][] records={{1,205,78,1040,0,2056,-8,25,80,120,1,0,-1},{50,34,140,1041,5,0,-140,2,1,30,1,-1},{40,161,70,1060,0,8,0,0,80,120,1,1,0,186,-1,-1,160,-1,-1}};
                    if(s.equals("missing-source"))records[2][13]=999;
                    Object[] r=new Object[4];r[3]=p;for(int i=0;i<3;++i){r[i]=c.create();c.call(r[i],"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(records[i],20),true,0,records[i].length);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},r[i]);}
                    if(!s.equals("missing-source"))BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},p);
                    if(s.equals("source-hidden"))c.set(p,"c","flags",1024);
                    if(s.equals("hidden")||s.equals("frozen"))c.set(r[2],"c","flags",136|(s.equals("hidden")?1024:16384));
                    roots[c==a?0:1]=r;
                }
                for(int f=0;f<50;++f){boolean paused=s.equals("pause")&&f>=4&&f<10;
                    for(var c:List.of(a,b)){Object[] r=roots[c==a?0:1];c.global("x","state",paused?14:8);
                        if(s.equals("leave")&&f==2)c.set(r[3],"K","positionX",300);
                        if(s.equals("completion-hidden")&&f==5)c.set(r[2],"c","flags",1160);
                        if(s.equals("completion-hidden")&&f==16)c.set(r[2],"c","flags",136);
                        if(!paused)BehaviorCheck.gameCall(c,"y","updateSelectedScripts",ActorCheck.NONE);
                        c.call(r[0],"s","update",ActorCheck.NONE);c.call(r[1],"s","update",ActorCheck.NONE);if(DeathCheck.actors(c).contains(r[2]))c.call(r[2],"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase){if(phase==1&&!paused)for(var c:List.of(a,b))for(int i=0;i<2;++i){c.call(roots[c==a?0:1][i],"g","refreshBounds",ActorCheck.NONE);c.call(roots[c==a?0:1][i],"f","advanceAnimation",ActorCheck.NONE);}
                        String line=row(a,roots[0],s,f,phase,indices[0]);ActorCheck.equal(line,row(b,roots[1],s,f,phase,indices[1]),"chandelier timeline "+s+" "+f+"/"+phase);out.print(line);
                    }
                    if(s.equals("normal")&&f==0)ActorCheck.equal(0,(int)((short[])a.global("cd","scriptTicks"))[indices[0]],"trigger starts after selected update");
                    if(s.equals("normal")&&f==1)ActorCheck.equal(16384,(int)a.get(roots[0][0],"c","flags")&16384,"enemy is frozen before its handler");
                    if(s.equals("normal")&&f==9){ActorCheck.equal(0,a.global("cj","cj"),"completed selected slot released");ActorCheck.equal(false,DeathCheck.actors(a).contains(roots[0][2]),"completed trigger removed");ActorCheck.equal(102,a.get(roots[0][1],"ao","actionState"),"chandelier enters falling handler");}
                }
            }
        }
        System.out.println("chandelier: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
