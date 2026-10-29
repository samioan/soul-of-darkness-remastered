import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
/** First-level checkpoint activation and two original restart calls; isolated RMS adapter. */
public final class CheckpointCheck {
    static final String[] CASES={"cp0","cp1","cp2","cp3","cp4","cp5","left","edge-left","edge-right","edge-top","edge-bottom","miss","point","paused","hidden","frozen","pickup","chest","enemy","persistent-enemy","transient-pickup","before-after","second"};
    static String row(ActorCheck.Access c,List<LevelCheck.Record> records,String name,int phase)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);Object p=c.global("bk","player");byte[] save=(byte[])c.global("cw","saveBuffer");
        out.print("C\t"+name+"\t"+phase+"\t"+SpriteCheck.u16(save,16));
        out.print("\t"+c.get(p,"K","positionX")+"\t"+c.get(p,"L","positionY")+"\t"+((boolean)c.get(p,"ak","facingLeft")?1:0)+"\t"+c.get(null,"bK","activeWeapon"));
        for(String[] n:new String[][]{{"p","health"},{"q","maxHealth"},{"s","magic"},{"t","maxMagic"},{"r","r"},{"u","u"}})out.print("\t"+c.global(n[0],n[1]));
        out.println("\t"+((boolean)c.get(null,"bs","bs")?1:0)+"\t"+((boolean)c.get(null,"bu","bu")?1:0));
        for(var r:records)if(r.values()[0]!=0)out.println("A\t"+name+"\t"+phase+"\t"+r.values()[1]+"\t"+(LifecycleCheck.uid(c,r.values()[1])!=null?1:0));
        return w.toString();
    }
    static void remove(ActorCheck.Access c,int uid,boolean persistent,boolean transientActor)throws Exception{
        Object e=LifecycleCheck.uid(c,uid);if(e==null)throw new AssertionError("Missing seed actor");int flags=(int)c.get(e,"c","flags");if(persistent)flags|=512;if(transientActor)flags|=8192;c.set(e,"c","flags",flags);BehaviorCheck.gameCall(c,"c","removeActor",new Class<?>[]{c.actor},e);
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/checkpoint-oracle.tsv");
        var records=LevelCheck.records(BehaviorCheck.resource(assets,3,0));var cps=records.stream().filter(r->r.values()[0]==23).toList();
        try(var out=new PrintWriter(Files.newBufferedWriter(file))){for(String name:CASES){
            try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true)){
                for(var c:List.of(a,b)){
                    LevelCheck.setup(c,assets,3);ScriptCheck.call(c,"a","decodeScripts",new Class<?>[]{byte[].class},BehaviorCheck.resource(assets,3,1));c.global("bH","bH",((short[][])c.global("bG","bG"))[0]);ScriptCheck.call(c,"c","constructLevelActors",new Class<?>[]{boolean.class},false);
                    c.global("q","maxHealth",80);c.global("p","health",23);c.global("t","maxMagic",90);c.global("s","magic",17);c.global("r","r",2);c.global("u","u",1);c.set(null,"bs","bs",true);c.set(null,"bu","bu",true);c.set(null,"bK","activeWeapon",1);
                    int index=name.startsWith("cp")?Integer.parseInt(name.substring(2)):0;Object cp=LifecycleCheck.uid(c,cps.get(index).values()[1]),p=c.global("bk","player");int[] box=(int[])c.get(cp,"j","collisionBounds");int x=box[0]+5,y=box[1]+40;c.set(p,"K","positionX",x);c.set(p,"L","positionY",y);c.set(p,"ak","facingLeft",name.equals("left"));c.call(p,"g","refreshBounds",ActorCheck.NONE);
                    int[] rect={box[0]+1,box[1]+1,box[0]+2,box[1]+2};
                    if(name.equals("edge-left"))rect=new int[]{box[0]-2,box[1],box[0],box[1]+1};if(name.equals("edge-right"))rect=new int[]{box[2],box[1],box[2]+2,box[1]+1};if(name.equals("edge-top"))rect=new int[]{box[0],box[1]-2,box[0]+1,box[1]};if(name.equals("edge-bottom"))rect=new int[]{box[0],box[3],box[0]+1,box[3]+2};
                    if(name.equals("miss"))rect=new int[]{box[0]-3,box[1],box[0]-1,box[1]+1};if(name.equals("point"))rect=new int[]{box[0],box[1],box[0],box[1]};c.set(p,"j","collisionBounds",rect);
                    if(name.equals("paused"))c.global("x","state",14);if(name.equals("hidden") || name.equals("frozen"))c.set(cp,"c","flags",8|(name.equals("hidden")?1024:16384));
                    if(name.equals("pickup") || name.equals("before-after") || name.equals("second") || name.equals("transient-pickup"))remove(c,6,false,name.equals("transient-pickup"));if(name.equals("chest"))remove(c,47,false,false);if(name.equals("enemy") || name.equals("persistent-enemy"))remove(c,19,name.equals("persistent-enemy"),false);
                    c.call(cp,"s","update",ActorCheck.NONE);
                    if(name.equals("before-after")){remove(c,18,false,false);remove(c,47,false,false);}
                    if(name.equals("second")){remove(c,18,false,false);Object next=LifecycleCheck.uid(c,cps.get(1).values()[1]);int[] bounds=(int[])c.get(next,"j","collisionBounds");c.set(p,"K","positionX",bounds[0]+5);c.set(p,"L","positionY",bounds[1]+40);c.set(p,"j","collisionBounds",bounds.clone());c.call(next,"s","update",ActorCheck.NONE);}
                    ((int[])c.sprites.get(null,"L","cacheImageCounts"))[0]=3;c.sprites.call(null,"g","createImageCache",new Class<?>[]{int.class,int.class},0,3);
                }
                boolean captured=SpriteCheck.u16((byte[])a.global("cw","saveBuffer"),16)!=0;
                ActorCheck.equal(!List.of("miss","point","paused","hidden","frozen").contains(name),captured,"checkpoint gate");
                for(int phase=0;phase<(captured?3:1);++phase){if(phase>0)for(var c:List.of(a,b)){c.global("p","health",1);c.global("s","magic",0);ScriptCheck.call(c,"al","restartLevel",ActorCheck.NONE);}
                    String r=row(a,records,name,phase);ActorCheck.equal(r,row(b,records,name,phase),"checkpoint/restart subset");out.print(r);
                }
            }
        }}System.out.println("native checkpoint: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
