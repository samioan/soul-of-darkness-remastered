import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Original platform landing helper and player ticks with controlled surfaces. */
public final class PlatformCheck {
    static final int[] RECT={60,160,140,170};
    static Object surface(ActorCheck.Access c,int type) throws Exception {
        Object e=ContactCheck.body(c,100);c.set(e,"am","type",type);int[] mask=new int[12];mask[11]=1;c.set(e,"n","moduleMask",mask);return e;
    }
    static void contact(ActorCheck.Access c,Object surface,int[] rect,int offset) throws Exception {
        c.call(surface,"d","d",new Class<?>[]{int.class,int[].class},offset,rect);
    }
    static void row(ActorCheck.Access a,ActorCheck.Access b,Object[] left,Object[] right,PrintWriter out,String name,int frame,int phase) throws Exception {
        ActorCheck.snapshot(a,b,left[0],right[0],name);
        int link=-1;
        for(var c:List.of(a,b)) {
            Object[] roots=c==a?left:right;Object p=c.get(null,"aK","playerPlatform");int value=p==null?0:p==roots[1]?1:2;
            if(c==a)link=value;else ActorCheck.equal(link,value,"platform owner");
        }
        out.print("P\t"+name+"\t"+frame+"\t"+phase);
        for(String[] n:ActorCheck.SNAPSHOT) {Object v=a.get(left[0],n[0],n[1]);if(v instanceof int[] r)for(int x:r)out.print("\t"+x);else out.print("\t"+v);}
        int latch=((int[])a.get(left[1],"n","moduleMask"))[11];ActorCheck.equal(latch,((int[])b.get(right[1],"n","moduleMask"))[11],"landing latch");
        out.println("\t"+link+"\t"+latch);
    }
    static Object[] matrix(ActorCheck.Access c,int id) throws Exception {
        c.clearWorld();int bits=id;
        int type=bits%2==0?33:50;bits/=2;
        int x=new int[]{59,60,100,140,141}[bits%5];bits/=5;
        int y=new int[]{154,159,165,166}[bits%4];bits/=4;
        int vy=new int[]{-512,0,256,1536}[bits%4];bits/=4;
        int surfaceVy=new int[]{-512,0,512}[bits%3];bits/=3;
        int link=bits%3;bits/=3;int animation=new int[]{0,4,3}[bits%3];
        Object p=c.player(x,y),e=surface(c,type),other=surface(c,33);
        c.set(p,"H","velocityY",vy);c.set(e,"H","velocityY",surfaceVy);
        c.call(p,"b","setAnimationImmediate",ActorCheck.INT,animation);c.call(p,"g","refreshBounds",ActorCheck.NONE);
        c.set(null,"aK","playerPlatform",link==0?null:link==1?e:other);
        return new Object[]{p,e,other};
    }
    static Object[] arena(ActorCheck.Access c,String s) throws Exception {
        c.clearWorld();Object p=c.player(100,120),e=surface(c,s.startsWith("swing")?50:33);
        c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);
        return new Object[]{p,e};
    }
    static int[] rect(String s,int frame) {
        int dy=s.equals("rise")?-(frame/3)%30:s.equals("sink")?(frame/3)%30:0;
        int dx=s.equals("slide")?frame/2:0;
        return new int[]{60+dx,160+dy,140+dx,170+dy};
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]);
        Path output=assets.getParent().resolve("analysis/platform-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(output))) {
            a.loadAssets(assets);b.loadAssets(assets);
            for(int id=0;id<4320;++id) {
                Object[] left=matrix(a,id),right=matrix(b,id);
                for(int phase=0;phase<2;++phase) {
                    contact(a,left[1],RECT,id%3-1);contact(b,right[1],RECT,id%3-1);
                    row(a,b,left,right,out,"matrix",id,phase);
                }
            }
            for(String s:new String[]{"stand","jump","walk","crouch","melee","combo","rise","sink","slide","pause","swing","swing_jump"}) {
                Object[] left=arena(a,s),right=arena(b,s);boolean attached=false,detached=false;
                for(int frame=0;frame<96;++frame) {
                    boolean paused=s.equals("pause") && frame>=20 && frame<30;
                    int pressed=(s.equals("jump") || s.equals("swing_jump")) && frame==20?16388:
                        (s.equals("melee") || s.equals("combo")) && (frame==20 || s.equals("combo") && frame==22)?65568:0;
                    int held=s.equals("walk") && frame>=20 && frame<40?8256:s.equals("crouch") && frame>=20 && frame<40?33024:0;
                    for(var c:List.of(a,b)) {
                        Object[] roots=c==a?left:right;c.input(pressed,held);c.global("z","stateFrame",frame);c.global("x","state",paused?14:8);
                        c.call(roots[0],"s","update",ActorCheck.NONE);
                        if(!paused)contact(c,roots[1],rect(s,frame),s.startsWith("swing")?1:0);
                    }
                    for(int phase=0;phase<2;++phase) {
                        if(phase==1 && !paused)for(var c:List.of(a,b)) {
                            Object p=(c==a?left:right)[0];c.call(p,"g","refreshBounds",ActorCheck.NONE);c.call(p,"f","advanceAnimation",ActorCheck.NONE);
                        }
                        row(a,b,left,right,out,s,frame,phase);
                    }
                    boolean bound=a.get(null,"aK","playerPlatform")!=null;if(attached && !bound)detached=true;attached|=bound;
                }
                ActorCheck.equal(true,attached,s+" must land on controlled surface");
                if(s.equals("jump") || s.equals("walk") || s.equals("swing_jump"))ActorCheck.equal(true,detached,s+" must leave surface");
            }
        }
        System.out.println("platform contacts: "+ActorCheck.comparisons+" original/reference comparisons; "+output);
    }
}
