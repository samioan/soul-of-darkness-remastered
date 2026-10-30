import java.io.*;
import java.nio.file.*;
import java.util.*;

/** Type-16 actor lifecycle, tile response and holder cooldown; no type-3 AI. */
public final class ThrownObjectCheck {
    static final String[] CASES={"held","flight","left","wall","ceiling","land","reflect","early-sword","player","blast-player","held-blast","pause","hidden","frozen","slope16","slope17","slope18","slope19","slope20","slope21","mode2","mode3","paired","enemy","enemy-frozen","bridge","bridge-hidden"};
    static boolean left(String s){return s.equals("left");}
    static int x(String s){return s.equals("wall")?650:s.startsWith("slope")?115:100;}
    static int y(String s){return s.equals("held")||s.equals("flight")||s.equals("left")||s.equals("mode2")||s.equals("mode3")||s.equals("paired")?100:190;}
    static int level(String s){return s.equals("mode2")?5:s.equals("mode3")||s.equals("paired")?3:1;}
    static int dx(String s){return s.equals("wall")?4096:s.equals("flight")?2048:s.equals("left")?-2048:0;}
    static int dy(String s){return s.equals("ceiling")?-6000:s.equals("flight")||s.equals("left")?-3584:0;}
    static String row(ActorCheck.Access c,Object root,Object holder,Object target,String name,int frame,int phase)throws Exception{
        var text=new StringWriter();var out=new PrintWriter(text);out.print("T\t"+name+"\t"+frame+"\t"+phase);
        for(String[] n:new String[][]{{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},{"G","velocityX"},{"H","velocityY"},{"I","accelerationX"},{"J","accelerationY"},{"c","flags"},{"f","animationId"},{"g","pendingAnimationId"},{"h","animationFrame"},{"i","animationTick"},{"ao","actionState"},{"aw","aw"},{"au","au"},{"az","az"}})out.print("\t"+c.get(root,n[0],n[1]));
        out.print("\t"+(DeathCheck.actors(c).contains(root)?1:0));for(String[] n:new String[][]{{"bB","grounded"},{"bD","ceilingHit"},{"cR","wallOnLeft"},{"cS","wallOnRight"},{"ak","facingLeft"}})out.print("\t"+((boolean)c.get(root,n[0],n[1])?1:0));
        for(String[] n:new String[][]{{"j","collisionBounds"},{"k","attackBounds"},{"l","visualBounds"}}){int[] r=(int[])c.get(root,n[0],n[1]);for(int v:r==null?new int[4]:r)out.print("\t"+v);}
        Object player=c.global("bk","player");out.print("\t"+c.get(holder,"ax","ax")+"\t"+(c.get(holder,"r","attachedActor")==root?1:0)+"\t"+c.global("p","health")+"\t"+c.get(null,"bE","playerInvulnerabilityMs"));
        for(String[] n:new String[][]{{"f","animationId"},{"G","velocityX"},{"I","accelerationX"},{"ak","facingLeft"}}){Object v=c.get(player,n[0],n[1]);out.print("\t"+(v instanceof Boolean b?b?1:0:v));}out.print("\t"+(target!=null&&(int)c.get(target,"am","type")==1?c.get(target,"ap","health"):-1)+"\t"+(target!=null&&(int)c.get(target,"am","type")==1?c.get(target,"ao","actionState"):-1)+"\t"+(target!=null&&(int)c.get(target,"am","type")==45?c.get(target,"ao","actionState"):-1));out.println();return text.toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]);int count=0;
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=Files.newBufferedWriter(assets.getParent().resolve("analysis/thrown-object-oracle.tsv"))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES){Object[][] roots=new Object[2][];
                for(var c:List.of(a,b)){
                    CombatCheck.arena(c);c.global("aG","levelIndex",level(s));c.player(700,199);c.set(null,"bE","playerInvulnerabilityMs",0);c.set(null,"bF","bF",s.equals("paired"));c.global("p","health",60);c.global("q","maxHealth",60);c.global("c","random",new Random(12345));
                    byte[] tiles=(byte[])c.global("dL","collisionTiles");if(s.equals("ceiling"))Arrays.fill(tiles,80,120,(byte)24);if(s.equals("wall"))for(int y=0;y<10;++y)tiles[y*40+35]=24;if(s.startsWith("slope"))Arrays.fill(tiles,400,440,(byte)Integer.parseInt(s.substring(5)));if(s.equals("mode2")||s.equals("mode3"))Arrays.fill(tiles,(byte)0);
                    Object holder=c.create();c.set(holder,"am","type",3);c.set(holder,"K","positionX",x(s));c.set(holder,"L","positionY",y(s));c.set(holder,"ak","facingLeft",left(s));BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},holder);
                    c.call(holder,"m","m",new Class<?>[]{int.class,int.class},5,-31);Object root=c.get(null,"aL","lastSpawnedActor");
                    if(s.equals("hidden"))c.set(root,"c","flags",(int)c.get(root,"c","flags")|1024);if(s.equals("frozen"))c.set(root,"c","flags",(int)c.get(root,"c","flags")|16384);Object target=null;
                    if(s.startsWith("enemy")){target=CombatCheck.enemy(c,105,159);if(s.equals("enemy-frozen"))c.set(target,"c","flags",(int)c.get(target,"c","flags")|16384);ContactCheck.queue(c,target);}
                    if(s.startsWith("bridge")){target=c.create();short[] record={45,42,100,159,0,8,0,0,40,40};c.call(target,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(record,20),true,0,10);c.call(target,"g","refreshBounds",ActorCheck.NONE);if(s.equals("bridge-hidden"))c.set(target,"c","flags",1032);ContactCheck.queue(c,target);}
                    roots[c==a?0:1]=new Object[]{root,holder,target};
                }
                for(int f=0;f<120;++f){boolean paused=s.equals("pause")&&f>=5&&f<15;
                    for(var c:List.of(a,b)){Object[] r=roots[c==a?0:1];Object root=r[0],holder=r[1],player=c.global("bk","player");c.global("x","state",paused?14:8);
                        if(f==0&&!s.equals("held")&&!s.equals("held-blast")&&!s.equals("hidden")&&!s.equals("frozen")&&!s.equals("paired"))c.call(holder,"n","n",new Class<?>[]{int.class,int.class},dx(s),dy(s));
                        if(f==0&&s.equals("blast-player")||f==3&&s.equals("held-blast"))c.call(root,"n","changeActionState",new Class<?>[]{int.class},105);
                        if(f==3&&s.startsWith("enemy"))c.call(root,"n","changeActionState",new Class<?>[]{int.class},104);
                        if(f==3&&s.startsWith("bridge"))c.call(root,"n","changeActionState",new Class<?>[]{int.class},105);
                        int[] bounds=(int[])c.get(root,"j","collisionBounds");boolean body=s.equals("player")&&f>=8&&f<20 || s.equals("blast-player")&&f<10 || s.equals("paired")&&f==0;
                        c.set(player,"j","collisionBounds",body&&bounds!=null?bounds.clone():new int[]{700,0,701,1});c.set(player,"k","attackBounds",(s.equals("reflect")&&f==15 || s.equals("early-sword")&&f==3)&&bounds!=null?bounds.clone():new int[]{700,0,701,1});
                        if(DeathCheck.actors(c).contains(root))c.call(root,"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase){if(phase==1&&!paused)for(var c:List.of(a,b)){Object root=roots[c==a?0:1][0];if(DeathCheck.actors(c).contains(root)&&((int)c.get(root,"c","flags")&1024)==0){c.call(root,"g","refreshBounds",ActorCheck.NONE);c.call(root,"f","advanceAnimation",ActorCheck.NONE);}}
                        String original=row(a,roots[0][0],roots[0][1],roots[0][2],s,f,phase);ActorCheck.equal(original,row(b,roots[1][0],roots[1][1],roots[1][2],s,f,phase),"thrown object");out.write(original);++count;
                    }
                }
                int first=((Random)a.global("c","random")).nextInt(),second=((Random)b.global("c","random")).nextInt();ActorCheck.equal(first,second,"thrown RNG");out.write("R\t"+s+"\t"+first+"\n");++count;
            }
        }
        System.out.println("ThrownObjectCheck: "+count+" original/reference rows; type-3 AI not executed");
    }
}
