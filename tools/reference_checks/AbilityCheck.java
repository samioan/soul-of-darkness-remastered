import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;

/** Type-48 animation-2 handler; interaction completion is an explicit fixture input. */
public final class AbilityCheck {
    static final String[] CASES={"normal","left","offset","already","other-level","outside","pause","hidden","frozen","chase-range"};
    static String row(ActorCheck.Access c,Object item,Object player,String name,int f,int phase)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("A\t"+name+"\t"+f+"\t"+phase);
        for(String[] n:new String[][]{{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},{"G","velocityX"},{"H","velocityY"},{"I","accelerationX"},{"J","accelerationY"},{"c","flags"},{"h","animationFrame"},{"i","animationTick"},{"ao","actionState"},{"dd","dd"}})out.print("\t"+c.get(item,n[0],n[1]));
        boolean alive=DeathCheck.actors(c).contains(item);out.print("\t"+((boolean)c.get(item,"ak","facingLeft")?1:0)+"\t"+(alive?1:0));
        for(int n:alive?(int[])c.get(item,"j","collisionBounds"):new int[4])out.print("\t"+n);
        out.print("\t"+((boolean)c.get(null,"bu","bu")?1:0)+"\t"+c.global("s","magic")+"\t"+((boolean)c.global("aS","aS")?1:0)+"\t"+c.global("bc","bc")+"\t"+c.global("aT","aT")+"\t"+(c.global("aU","aU")!=null?1:0)+"\t"+c.global("F","F")+"\t"+c.global("G","G"));
        out.print("\t"+c.get(c.global("bo","bo"),"c","flags"));
        for(String[] n:new String[][]{{"f","animationId"},{"g","pendingAnimationId"},{"G","velocityX"},{"H","velocityY"},{"I","accelerationX"},{"J","accelerationY"},{"c","flags"}})out.print("\t"+c.get(player,n[0],n[1]));
        out.println();return w.toString();
    }
    static String chestRow(ActorCheck.Access c,Object chest,String name,int f)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("C\t"+name+"\t"+f);
        for(String[] n:new String[][]{{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},{"G","velocityX"},{"H","velocityY"},{"I","accelerationX"},{"J","accelerationY"},{"c","flags"},{"f","animationId"},{"g","pendingAnimationId"},{"h","animationFrame"},{"i","animationTick"},{"ao","actionState"},{"au","au"},{"aw","aw"}})out.print("\t"+c.get(chest,n[0],n[1]));
        boolean alive=DeathCheck.actors(c).contains(chest);out.print("\t"+((boolean)c.get(chest,"bB","grounded")?1:0)+"\t"+(c.get(chest,"w","w")!=null?1:0)+"\t"+(alive?1:0));
        for(int n:alive?(int[])c.get(chest,"j","collisionBounds"):new int[4])out.print("\t"+n);out.println();return w.toString();
    }
    static void chests(ActorCheck.Access a,ActorCheck.Access b,PrintWriter out)throws Exception{
        for(String name:new String[]{"ability-chest","repeat-chest"}){
            Object[] roots=new Object[2];
            for(var c:List.of(a,b)){
                short[] record={52,57,180,199,0,1,-2};Object chest=BehaviorCheck.fixture(c,new BehaviorCheck.Example(3,record));
                c.global("bH","bH",new short[30]);c.set(null,"bu","bu",name.equals("repeat-chest"));roots[c==a?0:1]=chest;
            }
            for(int f=0;f<100;++f){
                List<String> events=new ArrayList<>();
                for(var c:List.of(a,b)){
                    Object chest=roots[c==a?0:1],player=c.global("bk","player");
                    if(f==3 || f==6)c.set(player,"K","positionX",f==3?180:100);c.call(player,"g","refreshBounds",ActorCheck.NONE);c.input(f==5?65536:0,0);
                    var before=new HashSet<>(DeathCheck.actors(c));c.call(chest,"s","update",ActorCheck.NONE);
                    for(Object child:DeathCheck.actors(c))if(!before.contains(child)){
                        int type=(int)c.get(child,"am","type");if(type!=35 && type!=48)continue;
                        String event="D\t"+name+"\t"+f+"\t"+type+"\t"+(type==48?27:33);
                        for(String[] n:new String[][]{{"f","animationId"},{"K","positionX"},{"L","positionY"},{"c","flags"}})event+="\t"+c.get(child,n[0],n[1]);
                        events.add(event);
                    }
                    if(DeathCheck.actors(c).contains(chest)){c.call(chest,"g","refreshBounds",ActorCheck.NONE);c.call(chest,"f","advanceAnimation",ActorCheck.NONE);}
                }
                String r=chestRow(a,roots[0],name,f);ActorCheck.equal(r,chestRow(b,roots[1],name,f),"ability chest lifecycle");out.print(r);
                if(!events.isEmpty()){ActorCheck.equal(2,events.size(),"one reward in each implementation");ActorCheck.equal(events.get(0),events.get(1),"ability chest spawn");out.println(events.get(0));}
                if(!DeathCheck.actors(a).contains(roots[0]))break;
            }
        }
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/ability-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            chests(a,b,out);
            for(String name:CASES){
                Object[][] roots=new Object[2][];
                for(var c:List.of(a,b)){
                    c.clearWorld();Object player=c.player(name.equals("outside")?500:180,199);
                    c.set(null,"bu","bu",name.equals("already"));c.global("s","magic",30);c.global("aG","levelIndex",name.equals("other-level")?1:0);
                    c.global("aU","aU",null);c.global("bc","bc",-1);c.global("aT","aT",0);c.global("F","F",0);c.global("G","G",0);c.global("ci","ci",false);
                    if(name.equals("left")){c.set(player,"ak","facingLeft",true);c.set(player,"c","flags",1);c.set(player,"ao","actionState",2);}
                    Object parent=c.create();c.set(parent,"K","positionX",name.equals("offset")?190:name.equals("chase-range")?0:180);c.set(parent,"L","positionY",name.equals("offset")?180:199);
                    Object item=c.call(parent,"a","spawnActor",new Class<?>[]{int.class,int.class,int.class,int.class,int.class,int.class,boolean.class},48,27,2,0,0,308,false);
                    if(name.equals("chase-range")){c.set(item,"ao","actionState",102);c.set(item,"G","velocityX",256);}
                    if(name.equals("hidden") || name.equals("frozen"))c.set(item,"c","flags",name.equals("hidden")?1024:16384);
                    c.global("bo","bo",c.create());roots[c==a?0:1]=new Object[]{item,player};
                }
                for(int f=0;f<72;++f){
                    boolean paused=name.equals("pause") && f>=4 && f<11;
                    for(var c:List.of(a,b)){
                        c.global("x","state",paused?14:8);
                        if(f==60){c.global("aS","aS",false);c.global("bc","bc",-1);c.global("aU","aU",null);}
                        Object item=roots[c==a?0:1][0];if(DeathCheck.actors(c).contains(item))c.call(item,"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase){
                        if(phase==1)for(var c:List.of(a,b)){
                            Object item=roots[c==a?0:1][0];if(DeathCheck.actors(c).contains(item)){
                                c.call(item,"g","refreshBounds",ActorCheck.NONE);
                                if(!paused && ((int)c.get(item,"c","flags")&1024)==0)c.call(item,"f","advanceAnimation",ActorCheck.NONE);
                            }
                        }
                        String r=row(a,roots[0][0],roots[0][1],name,f,phase);ActorCheck.equal(r,row(b,roots[1][0],roots[1][1],name,f,phase),"magic ability lifecycle");out.print(r);
                    }
                    if(name.equals("normal") && f==0){ActorCheck.equal(true,a.get(null,"bu","bu"),"contact unlocks magic");ActorCheck.equal(60,a.global("s","magic"),"contact restores maximum magic");}
                }
            }
        }
        System.out.println("magic ability: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
