import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;

/** Original world region sweep in isolation; ordinary actor handlers are paused. */
public final class TransientCheck {
    public static void main(String[] args)throws Exception {
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/transient-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))) {
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String mode:new String[]{"change","same","death"})for(int type:new int[]{35,39,84,85})for(int uid:new int[]{-2,-1,19})for(int animation:new int[]{0,11,12,13})for(int flags:new int[]{0,32,1024,8192}) {
                String[] result=new String[2];
                for(var c:List.of(a,b)) {
                    CombatCheck.arena(c);Object p=c.player(100,199);c.global("x","state",14);c.global("p","health",30);c.global("av","av",null);
                    if(mode.equals("death"))c.set(p,"f","animationId",9);
                    Object region=c.create();c.set(region,"am","type",22);c.set(region,"j","collisionBounds",new int[]{0,0,800,400});c.global("au","au",region);c.global("ai","ai",!mode.equals("same"));
                    Object e=c.create();c.set(e,"am","type",type);c.set(e,"al","uid",uid);c.set(e,"f","animationId",animation);c.set(e,"c","flags",flags);c.set(e,"aw","aw",417);c.set(e,"j","collisionBounds",new int[]{200,100,220,120});
                    BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},e);
                    BehaviorCheck.gameCall(c,"W","updateWorldActors",ActorCheck.NONE);
                    boolean removed=!DeathCheck.actors(c).contains(e);
                    boolean expected=mode.equals("change") && (flags&8192)==0 && uid==-1 && type!=84 && type!=85 && (type!=39 || animation!=13);
                    ActorCheck.equal(expected,removed,"original transient removal rule");
                    ActorCheck.equal(30,c.global("p","health"),"sweep does not award health");
                    ActorCheck.equal(417,c.get(e,"aw","aw"),"sweep skips actor timer update");
                    result[c==a?0:1]="T\t"+mode+"\t"+type+"\t"+uid+"\t"+animation+"\t"+flags+"\t"+(removed?1:0);
                }
                ActorCheck.equal(result[0],result[1],"transient sweep original/reference");out.println(result[0]);
            }
        }
        System.out.println("world transient sweep: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
