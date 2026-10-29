import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
/** Actual world dispatch with flying/type-1 bodies, player damage and melee. */
public final class IceWorldCheck {
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/ice-world-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:new String[]{"fight","contact","left","mixed"}){
                Object[][] roots=new Object[2][];int minimum=60;
                for(var c:List.of(a,b)){
                    CombatCheck.arena(c);Object p=c.player(s.equals("left")?240:100,199);c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);c.set(null,"bu","bu",false);CombatCheck.weapons(c,0,1);c.set(null,"bK","activeWeapon",1);c.set(null,"bs","bs",true);c.set(null,"aS","weaponSwitchCooldownMs",0);
                    short[] r={2,19,180,174,0,8,-200,-200,400,400,4,0,-1};byte[] stream=new byte[27];stream[0]=13;for(int i=0;i<13;++i){stream[1+2*i]=(byte)r[i];stream[2+2*i]=(byte)(r[i]>>>8);}c.global("bF","bF",stream);
                    Object e=c.create();c.call(e,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,1,13);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},e);c.set(e,"ai","cameraDistanceBucket",0);
                    Object other=null;if(s.equals("mixed")){
                        short[] r2={1,20,220,199,0,9,-100,-125,200,125,0,0,7};byte[] extended=Arrays.copyOf(stream,54);extended[27]=13;for(int i=0;i<13;++i){extended[28+2*i]=(byte)r2[i];extended[29+2*i]=(byte)(r2[i]>>>8);}c.global("bF","bF",extended);
                        other=c.create();c.call(other,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r2,20),true,28,13);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},other);c.set(other,"ai","cameraDistanceBucket",0);
                    }
                    roots[c==a?0:1]=new Object[]{p,e,other};
                }
                for(int f=0;f<96;++f){
                    int held=!s.equals("contact") && f<12?(s.equals("left")?4112:8256):0;
                    int pressed=!s.equals("contact") && f>=8 && f<60 && f%6==2?65568:0;
                    for(var c:List.of(a,b)){
                        Object p=roots[c==a?0:1][0];c.input(pressed,held);c.global("z","stateFrame",f);
                        int x=(int)c.get(p,"K","positionX"),y=(int)c.get(p,"L","positionY");int cx=Math.max(0,Math.min(560,x-120)),cy=Math.max(0,Math.min(80,y-220));c.global("aa","viewBounds",new int[]{cx,cy,cx+240,cy+320});
                        BehaviorCheck.gameCall(c,"W","updateWorldActors",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase){
                        if(phase==1)for(var c:List.of(a,b))LiveCombatCheck.paintTick(c);
                        for(int i=0;i<(s.equals("mixed")?3:2);++i){
                            var x=new java.io.StringWriter();var y=new java.io.StringWriter();LiveCombatCheck.row(a,roots[0][i],new PrintWriter(x),i==0?"P":i==1?"E":"E2",s,f,phase);LiveCombatCheck.row(b,roots[1][i],new PrintWriter(y),i==0?"P":i==1?"E":"E2",s,f,phase);
                            ActorCheck.equal(x.toString(),y.toString(),"flying reciprocal world snapshots");out.print(x.toString().replace("\tnull","\t0\t0\t0\t0"));
                        }
                    }
                    for(var c:List.of(a,b)){
                        Object p=c.global("bk","player");int px=(int)c.get(p,"K","positionX"),py=(int)c.get(p,"L","positionY");int cx=Math.max(0,Math.min(560,px-120)),cy=Math.max(0,Math.min(80,py-220));
                        for(Object actor:DeathCheck.actors(c))c.set(actor,"ai","cameraDistanceBucket",Math.abs((int)c.get(actor,"K","positionX")-(cx+120))/120+Math.abs((int)c.get(actor,"L","positionY")-(cy+160))/160);
                    }
                    minimum=Math.min(minimum,(int)a.global("p","health"));
                }
                if(s.equals("contact"))ActorCheck.equal(true,minimum<60,"flying body contact damages player");
            }
        }
        System.out.println("ice world: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
