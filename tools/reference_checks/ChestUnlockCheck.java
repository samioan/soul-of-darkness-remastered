import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
/** Actual world order: flying final removal, chest enable and reveal effect lifetime. */
public final class ChestUnlockCheck {
    static String effects(ActorCheck.Access c,String s,int f,int phase)throws Exception{
        var w=new StringBuilder("V\t"+s+"\t"+f+"\t"+phase);int count=0;var tail=new StringBuilder();
        for(Object e:DeathCheck.actors(c))if((int)c.get(e,"am","type")==39 && (int)c.get(e,"f","animationId")==28){
            ++count;for(String[] n:new String[][]{{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},{"c","flags"},{"h","animationFrame"},{"i","animationTick"},{"aw","aw"}})tail.append('\t').append(c.get(e,n[0],n[1]));
        }return w.append('\t').append(count).append(tail).append('\n').toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/chest-unlock-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:new String[]{"normal","reverse","one-left","pause","hidden","frozen","left-target"}){
                Object[][] roots=new Object[2][];
                for(var c:List.of(a,b)){
                    CombatCheck.arena(c);c.player(100,199);CombatCheck.weapons(c,0,1);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);c.set(null,"bu","bu",false);c.global("bD","bD",new byte[32]);c.global("bH","bH",new short[30]);c.set(null,"bE","playerInvulnerabilityMs",60000);
                    short[][] records={{52,56,180,199,0,(short)(s.equals("left-target")?2217:2216),0},{52,57,260,199,0,(short)(s.equals("left-target")?2216:2217),-2},{40,55,180,150,0,12,0,0,80,80,4,0,0,113,48,-1,-1,56,57},{2,113,180,174,0,2056,-200,-200,400,400,4,0,-1},{2,48,260,174,0,2057,-200,-200,400,400,4,0,-1}};
                    var bytes=new java.io.ByteArrayOutputStream();for(short[] r:records){bytes.write(r.length);for(short n:r){bytes.write(n);bytes.write(n>>>8);}}c.global("bF","bF",bytes.toByteArray());
                    Object[] all=new Object[5];int offset=1;for(int i=0;i<5;++i){short[] r=records[i];Object e=c.create();c.call(e,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,offset,r.length);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},e);c.set(e,"ai","cameraDistanceBucket",0);c.set(e,"ae","persistenceIndex",i);all[i]=e;offset+=1+2*r.length;}
                    if(s.equals("hidden") || s.equals("frozen"))c.set(all[2],"c","flags",140|(s.equals("hidden")?1024:16384));roots[c==a?0:1]=all;
                }
                for(int f=0;f<40;++f){
                    boolean paused=s.equals("pause") && f>=10 && f<16;
                    for(var c:List.of(a,b)){
                        var r=roots[c==a?0:1];c.global("x","state",paused?14:8);c.global("z","stateFrame",f);c.global("aa","viewBounds",new int[]{0,0,240,320});c.input(0,0);
                        if(f==4)c.call(r[s.equals("reverse")?4:3],"p","applyDamageWithReaction",ActorCheck.INT,20);
                        if(f==8 && !s.equals("one-left"))c.call(r[s.equals("reverse")?3:4],"p","applyDamageWithReaction",ActorCheck.INT,20);
                        BehaviorCheck.gameCall(c,"W","updateWorldActors",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase){
                        if(phase==1 && !paused)for(var c:List.of(a,b))LiveCombatCheck.paintTick(c);
                        var rows=new ArrayList<String>();for(var c:List.of(a,b)){
                            var r=roots[c==a?0:1];String text=RemovedEnemyCheck.row(c,r[2],new Object[]{r[0],r[1]},s,f).replace("R\t"+s+"\t"+f,"T\t"+s+"\t"+f+"\t"+phase);
                            for(int i=0;i<2;++i)text+=AbilityCheck.chestRow(c,r[i],s+"-"+i,f).replace("C\t"+s+"-"+i+"\t"+f,"C\t"+s+"-"+i+"\t"+f+"\t"+phase);
                            text+=effects(c,s,f,phase);rows.add(text);
                        }ActorCheck.equal(rows.get(0),rows.get(1),"chest enable world/effects");out.print(rows.get(0));
                    }
                    for(var c:List.of(a,b))for(Object e:DeathCheck.actors(c))c.set(e,"ai","cameraDistanceBucket",Math.abs((int)c.get(e,"K","positionX")-120)/120+Math.abs((int)c.get(e,"L","positionY")-160)/160);
                }
                int next=((Random)a.global("c","random")).nextInt();ActorCheck.equal(next,((Random)b.global("c","random")).nextInt(),"chest enable random stream");out.println("N\t"+s+"\t"+next);
                if(s.equals("normal"))ActorCheck.equal(2568,a.get(roots[0][0],"c","flags"),"first chest becomes visible without entering forced-update mode");
            }
        }System.out.println("chest unlock: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
