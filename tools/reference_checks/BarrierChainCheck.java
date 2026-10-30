import java.io.*;
import java.nio.file.*;
import java.util.*;
/** Shipped second-level trigger chain with a controlled type-3 source signal. */
public final class BarrierChainCheck {
    static String row(ActorCheck.Access c,Object a,int f,int phase)throws Exception{
        var text=new StringWriter();var out=new PrintWriter(text);out.print("C\t"+f+"\t"+phase+"\t"+c.get(a,"al","uid")+"\t"+c.get(a,"c","flags")+"\t"+(DeathCheck.actors(c).contains(a)?1:0));int[] box=(int[])c.get(a,"j","collisionBounds");for(int n:box==null?new int[4]:box)out.print("\t"+n);for(String n:new String[]{"s","v","w","x"})out.print("\t"+(c.get(a,n,n)!=null?1:0));out.println();return text.toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]);var rows=LevelCheck.records(BehaviorCheck.resource(assets,4,0));
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=Files.newBufferedWriter(assets.getParent().resolve("analysis/barrier-chain-oracle.tsv"))){
            Object[][] roots=new Object[2][];
            for(var c:List.of(a,b)){
                BehaviorCheck.commonAssets(c,assets);c.clearWorld();c.player(100,199);Object source=ContactCheck.body(c,1800);c.set(source,"al","uid",352);c.set(source,"am","type",3);c.set(source,"L","positionY",1240);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},source);
                Object[] objects=new Object[5];objects[0]=source;int i=1;for(int uid:new int[]{354,124,356,32}){var r=rows.stream().filter(v->v.values()[1]==uid).findFirst().orElseThrow();Object root=c.create();c.call(root,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r.values(),20),true,r.offset(),r.values().length);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},root);c.call(root,"g","refreshBounds",ActorCheck.NONE);objects[i++]=root;}roots[c==a?0:1]=objects;
            }
            for(int f=0;f<80;++f){for(var c:List.of(a,b)){Object[] r=roots[c==a?0:1];if(f==5)c.set(r[0],"K","positionX",1520);if(f==25)c.set(r[0],"c","flags",1024);for(int i=1;i<5;++i)if(DeathCheck.actors(c).contains(r[i]))c.call(r[i],"s","update",ActorCheck.NONE);}
                for(int phase=0;phase<2;++phase){if(phase==1)for(var c:List.of(a,b)){Object barrier=roots[c==a?0:1][4];if(DeathCheck.actors(c).contains(barrier)&&((int)c.get(barrier,"c","flags")&1024)==0){c.call(barrier,"g","refreshBounds",ActorCheck.NONE);c.call(barrier,"f","advanceAnimation",ActorCheck.NONE);}}
                    for(int i=1;i<5;++i){String original=i==4?EnergyBarrierCheck.row(a,roots[0][i],"chain",f,phase):row(a,roots[0][i],f,phase);String reference=i==4?EnergyBarrierCheck.row(b,roots[1][i],"chain",f,phase):row(b,roots[1][i],f,phase);ActorCheck.equal(original,reference,"barrier chain");out.write(original);}
                }
            }
        }
        System.out.println("BarrierChainCheck: 640 original/reference trigger/barrier rows; type-3 AI not executed");
    }
}
