import java.io.*;
import java.nio.file.*;
import java.util.*;

/** Selected-level inputs, not later-level native gameplay coverage. */
public final class LevelSelectionCheck {
    static String rows(ActorCheck.Access a,Path assets,int level)throws Exception {
        LevelCheck.setup(a,assets,level+3);
        ScriptCheck.call(a,"a","decodeScripts",new Class<?>[]{byte[].class},BehaviorCheck.resource(assets,level+3,1));
        ScriptCheck.call(a,"ah","collectRequiredSprites",ActorCheck.NONE);
        var text=new StringWriter();var out=new PrintWriter(text);
        int[] modes=(int[])a.global("aF","levelModes");byte[] backgrounds=(byte[])a.global("bi","bi");
        byte[] dims=BehaviorCheck.resource(assets,level+3,3);
        out.println("D\t"+level+"\t"+(level+3)+"\t"+(level+2)+"\t"+modes[level]+"\t"+backgrounds[level*4+2]+"\t"+backgrounds[level*4+3]+"\t"+a.global("bS","levelPixelWidth")+"\t"+a.global("bT","levelPixelHeight")+"\t"+SpriteCheck.u16(dims,0)+"\t"+SpriteCheck.u16(dims,2));
        out.print("Q\t"+level);for(int v:(int[])a.global("dJ","dJ"))out.print("\t"+v);out.println();
        short[] shared=new short[20];var records=LevelCheck.records(BehaviorCheck.resource(assets,level+3,0));
        for(var r:records){System.arraycopy(r.values(),0,shared,0,r.values().length);out.print("R\t"+level+"\t"+r.offset()+"\t"+r.values().length);for(short v:shared)out.print("\t"+v);out.println();}
        out.print("K\t"+level);for(var r:records)if(r.values()[0]==23)out.print("\t"+r.values()[1]);out.println();
        byte[][][] scripts=(byte[][][])a.global("cb","scriptTracks");short[] ids=(short[])a.global("cc","scriptIds");
        if(scripts!=null)for(int i=0;i<scripts.length;++i){out.print("S\t"+level+"\t"+ids[i]);for(byte[] track:scripts[i])out.print("\t"+track.length+"\t"+ScriptCheck.fingerprint(track));out.println();}
        var r=records.stream().filter(v->v.values()[0]==0).findFirst().orElseThrow();Object p=a.create();
        shared=new short[20];System.arraycopy(r.values(),0,shared,0,r.values().length);
        a.call(p,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},shared,true,0,r.values().length);
        out.print("P\t"+level);for(String[] f:new String[][]{{"al","uid"},{"K","positionX"},{"L","positionY"},{"c","flags"},{"f","animationId"}})out.print("\t"+a.get(p,f[0],f[1]));out.println();
        return text.toString();
    }
    public static void main(String[] args)throws Exception {
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]);int rows=0;
        try(var out=Files.newBufferedWriter(assets.getParent().resolve("analysis/selected-level-oracle.tsv"))){
            for(int level=0;level<9;++level)try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true)){
                String original=rows(a,assets,level),reference=rows(b,assets,level);ActorCheck.equal(original,reference,"selected level "+level);out.write(original);rows+=original.lines().count();
            }
        }
        System.out.println("LevelSelectionCheck: "+rows+" original/reference rows");
    }
}
