import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
/** Original type-10 handlers on shipped maps; simulation only, no Graphics adapter. */
public final class BeamCheck {
    static String row(ActorCheck.Access c,Object e,String name,int frame,int phase)throws Exception{
        var s=new StringBuilder("B\t"+name+"\t"+frame+"\t"+phase);
        for(String[] n:new String[][]{{"ao","actionState"},{"aw","aw"},{"aA","aA"},{"ac","ac"},{"ad","ad"},{"f","animationId"},{"g","pendingAnimationId"},{"h","animationFrame"},{"i","animationTick"},{"c","flags"}})s.append('\t').append(c.get(e,n[0],n[1]));
        return s.append('\t').append(c.get(e,"u","u")!=null?1:0).toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/beam-oracle.tsv");
        var emitters=LevelCheck.records(BehaviorCheck.resource(assets,3,0)).stream().filter(r->r.values()[0]==10).toList();
        try(var out=new PrintWriter(Files.newBufferedWriter(file))){
            for(int scenario=0;scenario<12;++scenario){String name="beam"+scenario;
                try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true)){
                    Object[] roots=new Object[2];int which=0;
                    for(var c:List.of(a,b)){
                        LevelCheck.setup(c,assets,3);c.player(0,100);
                        short[] r=emitters.get(scenario<6?scenario:0).values().clone();
                        if(scenario>=6 && scenario<=9){r[4]=(short)(scenario==6?0:scenario==7?8:4);r[5]=(short)(8|(scenario==9?1:0));r[12]=(short)(scenario==8?125:scenario==9?-125:0);r[13]=(short)(scenario==6?125:scenario==7?-125:0);}
                        if(scenario==10){int[][] stats=(int[][])c.global("bY","actorStatTables");stats[9][r[11]*4+2]=0;}
                        Object root=c.create();c.call(root,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,0,r.length);roots[which++]=root;
                    }
                    var states=new HashSet<Integer>();
                    for(int frame=0;frame<360;++frame){boolean paused=scenario==11 && frame>=20 && frame<40;
                        for(int i=0;i<2;++i){var c=i==0?a:b;Object e=roots[i],p=c.global("bk","player");int[] box=(int[])c.get(e,"m","customBounds");c.set(p,"j","collisionBounds",frame==0?new int[]{box[0]+1,box[1]+1,box[0]+2,box[1]+2}:new int[]{-1000,-1000,-999,-999});if(!paused)c.call(e,"aL","updateBeamEmitter",ActorCheck.NONE);}
                        String r=row(a,roots[0],name,frame,0);ActorCheck.equal(r,row(b,roots[1],name,frame,0),"beam handler");out.println(r);states.add((int)a.get(roots[0],"ao","actionState"));
                        if(!paused)for(int i=0;i<2;++i)(i==0?a:b).call(roots[i],"f","advanceAnimation",ActorCheck.NONE);
                        r=row(a,roots[0],name,frame,1);ActorCheck.equal(r,row(b,roots[1],name,frame,1),"beam animation");out.println(r);
                    }
                    ActorCheck.equal(true,states.contains(104)||states.contains(107),"beam must become active");
                }
            }
            try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true)){
                for(var c:List.of(a,b))BehaviorCheck.commonAssets(c,assets);
                int id=0;
                for(int direction:new int[]{8,4,1,2})for(int variant=0;variant<2;++variant)for(int permanent=0;permanent<2;++permanent)for(int pose:new int[]{0,8,9})for(int immune=0;immune<2;++immune)for(int wall=0;wall<2;++wall){
                    String expected=null;
                    for(var c:List.of(a,b)){
                        c.clearWorld();Object player=c.player(100,199);c.global("p","health",23);c.global("q","maxHealth",80);c.set(null,"bE","playerInvulnerabilityMs",immune==0?0:500);
                        c.call(player,"b","setAnimationImmediate",ActorCheck.INT,pose);c.set(player,"cR","wallOnLeft",wall!=0);c.set(player,"cS","wallOnRight",wall!=0);
                        short[] r=emitters.get(0).values().clone();r[2]=(short)(direction==1?95:105);r[3]=199;r[4]=(short)(direction==8?0:direction==4?8:4);r[5]=(short)(8|(direction==2?1:0));r[10]=(short)(variant==0?22:23);
                        ((int[][])c.global("bY","actorStatTables"))[9][r[11]*4+2]=permanent==0?1000:0;
                        Object source=c.create();c.call(source,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,0,r.length);
                        c.call(player,"a","a",new Class<?>[]{int.class,int.class,int.class,c.actor},4,10,0,source);
                        var line=new StringBuilder("D\t"+id);
                        for(String[] n:new String[][]{{"f","animationId"},{"g","pendingAnimationId"},{"h","animationFrame"},{"i","animationTick"},{"c","flags"},{"G","velocityX"},{"I","accelerationX"}})line.append('\t').append(c.get(player,n[0],n[1]));
                        line.append('\t').append((boolean)c.get(player,"ak","facingLeft")?1:0).append('\t').append(c.global("p","health")).append('\t').append(c.get(null,"bE","playerInvulnerabilityMs"));
                        int count=(int)c.global("bt","actorCount");line.append('\t').append(count);
                        for(int k=0;k<count;++k){Object e=java.lang.reflect.Array.get(c.global("bs","actors"),k);for(String[] n:new String[][]{{"f","animationId"},{"K","positionX"},{"L","positionY"},{"c","flags"}})line.append('\t').append(c.get(e,n[0],n[1]));}
                        line.append('\t').append(((java.util.Random)c.global("c","random")).nextInt());
                        if(expected==null)expected=line.toString();else ActorCheck.equal(expected,line.toString(),"beam damage gates/push/effects/random");
                    }
                    out.println(expected);++id;
                }
            }
        }
        System.out.println("native beams: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}

