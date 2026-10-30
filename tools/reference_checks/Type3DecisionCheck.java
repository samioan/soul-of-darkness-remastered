import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.*;
import java.util.*;

/** Type-3 decisions only: no integrated actor movement or child update queue. */
public final class Type3DecisionCheck {
    static final int[] ACTIONS={0,1,2,3,101,102,103,104,105,106,107,108,109};
    static final int[] DISTANCES={-600,-200,-80,-2,0,2,80,200,600};
    static Object actor(ActorCheck.Access c,int action,int side,int variant)throws Exception{
        CombatCheck.arena(c);c.player(700,199);
        Object root=c.create();short[] record={3,48,100,199,9,8,-125,-125,250,175,(short)(variant==0?7:variant==1?9:24),0,-1};
        c.call(root,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(record,20),true,0,13);
        c.set(root,"ao","actionState",action);c.set(root,"aw","aw",123);c.set(root,"ax","ax",333);c.set(root,"ak","facingLeft",side!=0);c.set(root,"dj","dj",side!=0);
        c.set(root,"f","animationId",9);c.set(root,"g","pendingAnimationId",-1);c.set(root,"h","animationFrame",0);c.set(root,"i","animationTick",0);c.set(root,"c","flags",64);
        return root;
    }
    static String state(ActorCheck.Access c,Object root)throws Exception{
        StringBuilder s=new StringBuilder();
        for(String[] n:new String[][]{{"ao","actionState"},{"aw","aw"},{"ax","ax"},{"G","velocityX"},{"H","velocityY"},{"I","accelerationX"},{"J","accelerationY"},{"ak","facingLeft"},{"f","animationId"},{"g","pendingAnimationId"},{"c","flags"}}){Object v=c.get(root,n[0],n[1]);s.append('\t').append(v instanceof Boolean b?b?1:0:v);}
        return s.toString();
    }
    static void emit(PrintWriter out,String a,String b)throws Exception{ActorCheck.equal(a,b,"type-3 decision");out.println(a);}
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),compiled=Path.of(args[1]),stubs=Path.of(args[2]);int count=0;
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(compiled,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(assets.getParent().resolve("analysis/type3-decision-oracle.tsv")))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            Class<?>[] six={int.class,int.class,int.class,int.class,int.class,int.class};
            for(int sx:new int[]{0,25600,Integer.MAX_VALUE,Integer.MIN_VALUE})for(int sy:new int[]{0,-43008,Integer.MAX_VALUE,Integer.MIN_VALUE})for(int dx:new int[]{-200000,-64000,-28672,-14336,-1,0,1,14336,28672,64000,200000,Integer.MIN_VALUE,Integer.MAX_VALUE})for(int dy:new int[]{-200000,-7936,0,7936,200000,Integer.MIN_VALUE,Integer.MAX_VALUE}){
                String prefix="P\t"+sx+"\t"+sy+"\t"+dx+"\t"+dy;Object ra=a.create(),rb=b.create();
                emit(out,prefix+"\t"+a.call(ra,"a","a",six,sx,sy,sx+dx,sy+dy,3584,-512),prefix+"\t"+b.call(rb,"a","a",six,sx,sy,sx+dx,sy+dy,3584,-512));++count;
            }
            for(int current:ACTIONS)for(int next:ACTIONS)for(int side=0;side<2;++side)for(int distance:new int[]{-200,-1,0,1,200}){
                String[] rows=new String[2];for(var c:List.of(a,b)){
                    Object root=actor(c,current,side,0),player=c.global("bk","player");c.set(player,"a","fixedX",25600+distance);c.set(player,"K","positionX",100+Integer.signum(distance));
                    c.set(root,"G","velocityX",103);c.set(root,"H","velocityY",-203);c.set(root,"I","accelerationX",17);c.set(root,"J","accelerationY",19);
                    c.call(root,"n","changeActionState",ActorCheck.INT,next);
                    rows[c==a?0:1]="E\t"+current+"\t"+next+"\t"+side+"\t"+distance+state(c,root)+"\t"+DeathCheck.actors(c).stream().filter(v->{try{return (int)c.get(v,"am","type")==39;}catch(Exception e){throw new RuntimeException(e);}}).count();
                }emit(out,rows[0],rows[1]);++count;
            }
            // One decision at a time on a supported flat floor. Keep body/weapon
            // contacts remote; actor physics/lifecycle remain outside this oracle.
            for(int variant=0;variant<3;++variant)for(int action:ACTIONS){if(action==3)continue;
            for(int side=0;side<2;++side)for(int distance:DISTANCES)for(int timer:new int[]{83,84})for(int mode=0;mode<6;++mode){
                String[] rows=new String[2];for(var c:List.of(a,b)){
                    Object root=actor(c,action,side,variant),player=c.global("bk","player");c.set(root,"aw","aw",timer);
                    c.set(player,"a","fixedX",25600+distance*256);c.set(player,"b","fixedY",199*256);c.set(player,"K","positionX",100+distance);
                    c.set(player,"j","collisionBounds",new int[]{10000,0,10001,1});c.set(player,"k","attackBounds",new int[]{10000,0,10001,1});
                    c.set(root,"m","customBounds",mode==4?new int[]{0,0,1,1}:new int[]{-1000,0,11000,400});
                    if(mode==5){boolean face=distance < -2 || Math.abs(distance)<=2 && side!=0;int x=face?60:140;c.set(player,"j","collisionBounds",new int[]{x,170,x+1,180});}
                    Object child=null;
                    if(mode!=0 && mode!=4){child=c.create();c.set(child,"am","type",mode==3?52:16);c.set(child,"ao","actionState",mode==2?102:101);c.call(root,"b","setAttachedActor",new Class<?>[]{c.actor},child);}
                    if(mode==1 || mode==3){Object sprite=c.get(root,"p","sprite");int last=(int)c.sprites.call(sprite,"a","getAnimationFrameCount",ActorCheck.INT,9)-1;int duration=(int)c.sprites.call(sprite,"a","getAnimationFrameDuration",new Class<?>[]{int.class,int.class},9,last);c.set(root,"h","animationFrame",last);c.set(root,"i","animationTick",duration-1);}
                    c.set(root,"cR","wallOnLeft",mode==3);
                    c.call(root,"aG","updateEnemyType3",ActorCheck.NONE);
                    // Capture decision fields only. Physics has its own oracle.
                    rows[c==a?0:1]="D\t"+variant+"\t"+action+"\t"+side+"\t"+distance+"\t"+timer+"\t"+mode;
                    for(String[] n:new String[][]{{"ao","actionState"},{"aw","aw"},{"ax","ax"},{"G","velocityX"},{"ak","facingLeft"},{"g","pendingAnimationId"}}){Object v=c.get(root,n[0],n[1]);rows[c==a?0:1]+="\t"+(v instanceof Boolean q?q?1:0:v);}
                    Object held=c.get(root,"r","attachedActor");rows[c==a?0:1]+="\t"+(held==null?0:1);
                    if(child==null)child=held;
                    for(String[] n:new String[][]{{"ao","actionState"},{"G","velocityX"},{"H","velocityY"}})rows[c==a?0:1]+="\t"+(child==null?-1:c.get(child,n[0],n[1]));
                }emit(out,rows[0],rows[1]);++count;
            }}
        }
        System.out.println("Type3DecisionCheck: "+count+" original/reference rows; full native actor integration pending");
    }
}
