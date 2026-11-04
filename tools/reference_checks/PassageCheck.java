import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;

/** Actual type-59 handlers, including their deliberately stale player fixed/bounds state. */
public final class PassageCheck {
    static final String[] LEVEL1_CASES={"cross","wrong-facing","stationary","wrong-motion","outside","edge","paused","hidden","frozen","late","target-hidden"};
    static void levelOne(ActorCheck.Access a,ActorCheck.Access b,Path assets,PrintWriter out)throws Exception{
        var records=LevelCheck.records(BehaviorCheck.resource(assets,4,0)).stream().filter(r->r.values()[0]==59).toList();
        ActorCheck.equal(20,records.size(),"second-level passage count");
        // The passage handler never mutates tiles. Load assets/maps once per
        // class loader, then restore them after each isolated world reset.
        Object[][] maps=new Object[2][];
        for(var c:List.of(a,b)){
            LevelCheck.setup(c,assets,4);
            maps[c==a?0:1]=new Object[]{c.global("dL","collisionTiles"),c.global("bQ","mapWidthTiles"),c.global("bR","mapHeightTiles"),c.global("bS","levelPixelWidth"),c.global("bT","levelPixelHeight")};
        }
        for(var record:records)for(String mode:LEVEL1_CASES){
            short[] source=record.values(),target=records.stream().filter(r->r.values()[1]==source[10]).findFirst().orElseThrow().values();
            String name="level1-"+source[1]+"-"+mode;Object[][] roots=new Object[2][];
            int px=(source[2]&65535)+source[6]+source[8]/2,py=(source[3]&65535)+source[7]+source[9]/2;
            for(var c:List.of(a,b)){
                c.clearWorld();c.global("aG","levelIndex",1);Object[] map=maps[c==a?0:1];
                c.global("dL","collisionTiles",map[0]);c.global("bQ","mapWidthTiles",map[1]);c.global("bR","mapHeightTiles",map[2]);c.global("bS","levelPixelWidth",map[3]);c.global("bT","levelPixelHeight",map[4]);
                Object p=c.player(px,py);
                boolean left=(source[11]==1)^mode.equals("wrong-facing");
                c.set(p,"ak","facingLeft",left);c.set(p,"c","flags",left?1:0);
                c.set(p,"G","velocityX",mode.equals("stationary")?0:(source[11]==1?-2048:2048)*(mode.equals("wrong-motion")?-1:1));
                if(mode.equals("outside")||mode.equals("late")){
                    c.set(p,"K","positionX",-1000);c.set(p,"a","fixedX",-256000);
                }
                c.call(p,"g","refreshBounds",ActorCheck.NONE);
                int[] playerShape=((int[])c.get(p,"j","collisionBounds")).clone();
                if(mode.equals("edge"))c.set(p,"j","collisionBounds",new int[]{(source[2]&65535)+source[6]-2,py,(source[2]&65535)+source[6],py+1});
                Object s=c.create(),t=c.create();
                c.call(s,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(source,20),true,0,12);
                c.call(t,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(target,20),true,0,12);
                if(mode.equals("hidden")||mode.equals("frozen"))c.set(s,"c","flags",((int)c.get(s,"c","flags"))|(mode.equals("hidden")?1024:16384));
                if(mode.equals("target-hidden"))c.set(t,"c","flags",((int)c.get(t,"c","flags"))|1024);
                if(!mode.equals("late"))BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},t);
                c.global("aj","cameraControllerActive",true);c.global("am","activeCameraControllerUid",777);c.global("ai","ai",true);
                roots[c==a?0:1]=new Object[]{s,t,p,playerShape};
            }
            for(int f=0;f<8;++f){
                for(var c:List.of(a,b)){
                    Object[] r=roots[c==a?0:1];c.global("x","state",mode.equals("paused")&&f<3?14:8);
                    if(mode.equals("late")&&f==2){BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},r[1]);c.set(r[2],"K","positionX",px);c.set(r[2],"a","fixedX",px*256);c.call(r[2],"g","refreshBounds",ActorCheck.NONE);}
                    c.call(r[0],"s","update",ActorCheck.NONE);
                }
                if(f==0){
                    boolean crossed=mode.equals("cross")||mode.equals("edge")||mode.equals("target-hidden");
                    ActorCheck.equal(crossed?-1:777,a.global("am","activeCameraControllerUid"),name+" camera release");
                    if(crossed){
                        int bottom=(target[3]&65535)+target[7]+target[9];
                        ActorCheck.equal(bottom-(target[9]*(((source[3]&65535)+source[7]+source[9]-py)*256/source[9])>>8),a.get(roots[0][2],"L","positionY"),name+" mapped height");
                        ActorCheck.equal(px*256,a.get(roots[0][2],"a","fixedX"),name+" fixed position stays stale");
                        // Solve the inclusive exit boundary using the original player
                        // shape, independently of the handler's one-pixel loop.
                        int[] box=(int[])roots[0][3];int tx=target[2]&65535,ty=(target[3]&65535)+target[7];
                        int expectedX=tx;
                        if(box[1]<=ty+target[9] && box[3]>=ty && tx+box[0]-px<=tx+target[6]+target[8] && tx+box[2]-px>=tx+target[6])
                            expectedX=source[11]==1?tx+target[6]-(box[2]-px)-1:tx+target[6]+target[8]-(box[0]-px)+1;
                        ActorCheck.equal(expectedX,a.get(roots[0][2],"K","positionX"),name+" inclusive exit position");
                    }
                }
                if(mode.equals("late")&&f==2 || mode.equals("paused")&&f==3)
                    ActorCheck.equal(-1,a.global("am","activeCameraControllerUid"),name+" resumes transfer");
                for(int phase=0;phase<2;++phase){
                    if(phase==1)for(var c:List.of(a,b))c.call(roots[c==a?0:1][2],"g","refreshBounds",ActorCheck.NONE);
                    String value=row(a,roots[0][0],roots[0][2],name,f,phase);ActorCheck.equal(value,row(b,roots[1][0],roots[1][2],name,f,phase),name);out.print(value);
                }
            }
        }
    }
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
            levelOne(a,b,assets,out);
        }
        System.out.println("passages: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
