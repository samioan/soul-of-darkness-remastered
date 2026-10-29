import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;

/** Real finishLevel, RMS serialization, world unload and completed Continue.
 * Uses only existing ImageIO/RMS adapters and an opaque tile-buffer sentinel. */
public final class FinishCheck {
    static String stats(ActorCheck.Access c)throws Exception{
        var w=new java.io.StringWriter();var o=new PrintWriter(w);o.print("\t"+c.get(null,"bK","activeWeapon"));
        for(String[] n:SaveCheck.METERS)o.print("\t"+c.global(n[0],n[1]));
        o.print("\t"+c.get(null,"bs","bs")+"\t"+c.get(null,"bu","bu"));return w.toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/finish-oracle.tsv");
        try(var out=new PrintWriter(Files.newBufferedWriter(file))){
            for(int level:new int[]{0,7,8})for(int unlocked:new int[]{level,9})for(boolean checkpoint:new boolean[]{false,true}){
                try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true)){
                    for(var c:List.of(a,b)){
                        LevelCheck.setup(c,assets,level+3);MenuCheck.seedBuffer(c);
                        ScriptCheck.call(c,"a","decodeScripts",new Class<?>[]{byte[].class},BehaviorCheck.resource(assets,level+3,1));ScriptCheck.call(c,"c","constructLevelActors",new Class<?>[]{boolean.class},false);
                        byte[] save=new byte[1313];SaveCheck.put16(save,16,checkpoint?12:0);SaveCheck.put16(save,18,148);SaveCheck.put16(save,20,259);c.global("cw","saveBuffer",save);
                        c.global("aH","maxUnlockedLevel",unlocked);c.global("aI","levelCompleted",false);c.global("x","state",15);c.global("df","df",9);
                        c.global("q","maxHealth",80);c.global("p","health",23);c.global("r","r",2);c.global("t","maxMagic",90);c.global("s","magic",17);c.global("u","u",1);c.global("v","v",12);
                        c.set(null,"bK","activeWeapon",1);c.set(null,"bs","bs",true);c.set(null,"bu","bu",true);c.set(null,"bC","bC",true);
                        if(checkpoint)ScriptCheck.call(c,"b","accessSaveRecord",new Class<?>[]{boolean.class},true);
                        ScriptCheck.call(c,"U","finishLevel",ActorCheck.NONE);
                        byte[] stored=LifecycleCheck.stored(c);
                        ActorCheck.equal(1313,stored.length,"finish writes full RMS record");ActorCheck.equal(level,(int)stored[14],"save retains completed level");ActorCheck.equal(Math.max(unlocked,level+1),(int)stored[15],"save preserves maximum unlock");ActorCheck.equal(1,SpriteCheck.u16(stored,24),"completion serialized");ActorCheck.equal((byte)0,stored[644],"paired flag cleared before writing");
                        ActorCheck.equal(false,c.get(null,"bC","bC"),"paired owner released");ActorCheck.equal(0,c.global("bt","actorCount"),"world actor slots cleared");ActorCheck.equal(null,c.global("bk","player"),"old player released");ActorCheck.equal(null,c.get(null,"aK","playerPlatform"),"borrowed rider link released");ActorCheck.equal(null,c.global("cb","scriptTracks"),"script ownership released");ActorCheck.equal(null,c.global("dL","collisionTiles"),"collision map released");ActorCheck.equal(19,c.global("x","state"),"finish selects level map");ActorCheck.equal(level==8?0:level+1,c.global("dh","dh"),"selection target advances/wraps");
                    }
                    ActorCheck.equal(LifecycleCheck.stored(a),LifecycleCheck.stored(b),"original/reference entire finish RMS record");
                    String af="F\t"+level+"\t"+unlocked+"\t"+checkpoint+"\t"+a.global("aG","levelIndex")+"\t"+a.global("aH","maxUnlockedLevel")+"\t"+a.global("aI","levelCompleted")+"\t"+a.global("dh","dh")+stats(a)+"\n";
                    String bf="F\t"+level+"\t"+unlocked+"\t"+checkpoint+"\t"+b.global("aG","levelIndex")+"\t"+b.global("aH","maxUnlockedLevel")+"\t"+b.global("aI","levelCompleted")+"\t"+b.global("dh","dh")+stats(b)+"\n";ActorCheck.equal(af,bf,"finish progress/stats");out.print(af);
                    for(var c:List.of(a,b)){
                        c.global("cw","saveBuffer",new byte[1313]);ScriptCheck.call(c,"b","accessSaveRecord",new Class<?>[]{boolean.class},false);
                        for(String[] n:SaveCheck.METERS)c.global(n[0],n[1],0);c.set(null,"bK","activeWeapon",0);c.set(null,"bs","bs",false);c.set(null,"bu","bu",false);c.global("x","state",2);
                        MenuCheck.press(c,2,65568);
                        ActorCheck.equal(level<8?level+1:level,c.global("aG","levelIndex"),"completed Continue target");ActorCheck.equal(level==8,c.global("dK","continueFromCheckpoint"),"completed Continue skips obsolete position below ending level");ActorCheck.equal(checkpoint?23:60,c.global("p","health"),"Continue saved/default health");ActorCheck.equal(checkpoint?17:60,c.global("s","magic"),"Continue saved/default magic");
                    }
                    String ac="C\t"+level+"\t"+unlocked+"\t"+checkpoint+"\t"+a.global("aG","levelIndex")+"\t"+a.global("aH","maxUnlockedLevel")+"\t"+a.global("aI","levelCompleted")+"\t"+a.global("dK","continueFromCheckpoint")+stats(a)+"\n";
                    String bc="C\t"+level+"\t"+unlocked+"\t"+checkpoint+"\t"+b.global("aG","levelIndex")+"\t"+b.global("aH","maxUnlockedLevel")+"\t"+b.global("aI","levelCompleted")+"\t"+b.global("dK","continueFromCheckpoint")+stats(b)+"\n";ActorCheck.equal(ac,bc,"completed Continue decision/stats");out.print(ac);
                }
            }
        }
        System.out.println("finish/progression: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
