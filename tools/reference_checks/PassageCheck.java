import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;

/** Actual type-59 handlers, including their deliberately stale player fixed/bounds state. */
public final class PassageCheck {
    static final String[] CASES={"right","left","scaled","wrong-facing","stationary","wrong-motion","outside","edge","paused","hidden","frozen","late","target-hidden"};
    static String row(ActorCheck.Access c,Object source,Object player,String name,int frame,int phase)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("P\t"+name+"\t"+frame+"\t"+phase);
        for(String[] n:new String[][]{{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},{"G","velocityX"},{"H","velocityY"},{"c","flags"},{"f","animationId"},{"h","animationFrame"},{"i","animationTick"}})out.print("\t"+c.get(player,n[0],n[1]));
        for(int n:(int[])c.get(player,"j","collisionBounds"))out.print("\t"+n);
        out.print("\t"+(c.get(source,"s","s")!=null));
        out.println("\t"+c.global("aj","cameraControllerActive")+"\t"+c.global("am","activeCameraControllerUid"));return w.toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/passage-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            a.loadAssets(assets);b.loadAssets(assets);
            List<short[]> records=new ArrayList<>();byte[] raw=BehaviorCheck.resource(assets,3,0);short[] shared=new short[20];
            for(int pos=0;pos<raw.length;){int count=raw[pos++]&255;for(int i=0;i<count;++i){shared[i]=(short)SpriteCheck.u16(raw,pos);pos+=2;}if(shared[0]==59)records.add(shared.clone());}
            List<String> cases=new ArrayList<>(List.of(CASES));for(short[] r:records)cases.add("level-"+r[1]);
            for(String name:cases){
                short[] placed=name.startsWith("level-")?records.stream().filter(r->("level-"+r[1]).equals(name)).findFirst().orElseThrow():null;
                short[] destination=placed==null?null:records.stream().filter(r->r[1]==placed[10]).findFirst().orElseThrow();
                Object[][] roots=new Object[2][];
                for(var c:List.of(a,b)){
                    c.clearWorld();Object player=c.player(name.equals("late")?100:name.equals("edge")?173:180,name.equals("outside")?80:199);
                    if(placed!=null){
                        c.set(player,"K","positionX",(placed[2]&65535)+placed[6]+placed[8]/2);c.set(player,"L","positionY",(placed[3]&65535)+placed[7]+placed[9]/2);
                        c.set(player,"a","fixedX",(int)c.get(player,"K","positionX")*256);c.set(player,"b","fixedY",(int)c.get(player,"L","positionY")*256);
                    }
                    c.set(player,"G","velocityX",name.equals("stationary")?0:name.equals("left") || name.equals("wrong-motion") || placed!=null && placed[11]==1?-2048:2048);
                    c.set(player,"ak","facingLeft",name.equals("left") || name.equals("wrong-facing") || placed!=null && placed[11]==1);c.set(player,"c","flags",name.equals("left") || name.equals("wrong-facing") || placed!=null && placed[11]==1?1:0);c.call(player,"g","refreshBounds",ActorCheck.NONE);
                    c.global("aj","cameraControllerActive",true);c.global("am","activeCameraControllerUid",777);c.global("ai","ai",true);
                    Object source=c.create(),target=c.create();short[] r={59,10,180,150,0,8,0,0,10,80,11,(short)(name.equals("left")?1:2)};
                    c.call(source,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(placed==null?r:placed,20),true,0,12);
                    r=new short[]{59,11,500,250,0,8,-3,-10,10,(short)(name.equals("scaled")?30:80),10,1};
                    c.call(target,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(destination==null?r:destination,20),true,0,12);
                    if(name.equals("hidden") || name.equals("frozen"))c.set(source,"c","flags",136|(name.equals("hidden")?1024:16384));
                    if(name.equals("target-hidden"))c.set(target,"c","flags",1160);
                    if(!name.equals("late"))BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},target);
                    roots[c==a?0:1]=new Object[]{source,target,player};
                }
                for(int f=0;f<8;++f){
                    boolean paused=name.equals("paused") && f<3;
                    for(var c:List.of(a,b)){
                        var r=roots[c==a?0:1];c.global("x","state",paused?14:8);
                        if(name.equals("late") && f==2){
                            BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},r[1]);
                            c.set(r[2],"K","positionX",180);c.set(r[2],"a","fixedX",180*256);c.call(r[2],"g","refreshBounds",ActorCheck.NONE);
                        }
                        c.call(r[0],"s","update",ActorCheck.NONE);
                    }
                    if(name.equals("level-81") && f==0){
                        ActorCheck.equal(1542,a.get(roots[0][2],"K","positionX"),"first passage jumps to next packed section");
                        ActorCheck.equal(140,a.get(roots[0][2],"L","positionY"),"first passage maps source-relative height");
                    }
                    for(int phase=0;phase<2;++phase){
                        if(phase==1)for(var c:List.of(a,b))c.call(roots[c==a?0:1][2],"g","refreshBounds",ActorCheck.NONE);
                        String r=row(a,roots[0][0],roots[0][2],name,f,phase);ActorCheck.equal(r,row(b,roots[1][0],roots[1][2],name,f,phase),"passage handler/player state");out.print(r);
                    }
                }
            }
        }
        System.out.println("passages: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
