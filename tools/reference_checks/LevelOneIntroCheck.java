import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
/** Level-1 script 133 and trigger 134 (player 131, zombie 215, flyer 135); original interpreter, no MIDP rendering/runtime. */
public final class LevelOneIntroCheck {
    static final int[] UIDS={131,215,135};
    static boolean alive(ActorCheck.Access c,Object e)throws Exception{if(e==c.global("bk","player"))return true;for(int i=0;i<(int)c.global("bt","actorCount");++i)if(java.lang.reflect.Array.get(c.global("bs","actors"),i)==e)return true;return false;}
    static int dialogue(ActorCheck.Access c)throws Exception{Object text=c.global("aU","aU");if(text==null)return -1;for(int i=0;i<7;++i)if(text.equals(ScriptCheck.call(c,"h","getString",new Class<?>[]{int.class,int.class},3,i)))return 4+i;throw new AssertionError("Unknown intro text");}
    static String row(ActorCheck.Access c,Object[] actors,String name,int frame)throws Exception{
        int tick=((short[])c.global("cd","scriptTicks"))[0];var s=new StringBuilder("S\t"+name+"\t"+frame+"\t"+tick+"\t"+dialogue(c)+"\t"+c.global("L","cameraX")+"\t"+c.global("M","cameraY")+"\t"+c.global("aW","aW")+"\t"+c.global("aX","aX"));
        for(int n:((int[][])c.global("ce","scriptTrackOffsets"))[0])s.append('\t').append(n);s.append('\n');
        for(int i=0;i<actors.length;++i){Object e=actors[i];s.append("A\t").append(name).append('\t').append(frame).append('\t').append(UIDS[i]).append('\t').append(alive(c,e)?1:0);
            for(String[] n:new String[][]{{"K","positionX"},{"L","positionY"},{"c","flags"},{"f","animationId"},{"ak","facingLeft"}}){Object v=c.get(e,n[0],n[1]);s.append('\t').append(v instanceof Boolean b?(b?1:0):v);}s.append('\n');}
        return s.toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/level1-intro-oracle.tsv");
        try(var out=new PrintWriter(Files.newBufferedWriter(file))){for(String name:new String[]{"normal","pause","skip0","skip20","skip-dialogue","skip-late","no-skip"}){
            try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true)){
                Object[][] actors=new Object[2][];int which=0;
                for(var c:List.of(a,b)){
                    LevelCheck.setup(c,assets,4);ScriptCheck.call(c,"a","decodeScripts",new Class<?>[]{byte[].class},BehaviorCheck.resource(assets,4,1));c.global("bH","bH",((short[][])c.global("bG","bG"))[1]);ScriptCheck.call(c,"c","constructLevelActors",new Class<?>[]{boolean.class},false);
                    actors[which++]=Arrays.stream(UIDS).mapToObj(uid->{try{return LifecycleCheck.uid(c,uid);}catch(Exception e){throw new RuntimeException(e);}}).toArray();
                    c.input(8192,8192);Object trigger=LifecycleCheck.uid(c,134);c.call(trigger,"s","update",ActorCheck.NONE);
                    ActorCheck.equal(null,LifecycleCheck.uid(c,134),"intro trigger removed");ActorCheck.equal((short)0,((short[])c.global("cd","scriptTicks"))[0],"intro starts script 133");ActorCheck.equal(0,c.global("cz","pressedMask"),"intro clears input");
                    if(name.equals("no-skip"))c.global("ch","ch",false);
                }
                int hold=0,seen=0;
                for(int frame=0;frame<170;++frame){int current=dialogue(a);boolean acknowledged=current>=0 && ++hold>=4; if(current<0)hold=0;if(acknowledged){hold=0;++seen;}
                    int tick=((short[])a.global("cd","scriptTicks"))[0];boolean skip=name.equals("skip0")&&frame==0 || name.equals("skip20")&&tick==20 || name.equals("skip-dialogue")&&current==4 || name.equals("skip-late")&&current==9 || name.equals("no-skip")&&frame==10;
                    boolean paused=name.equals("pause") && frame>=10 && frame<15;
                    for(var c:List.of(a,b)){
                        if(acknowledged)ScriptCheck.call(c,"l","l",ActorCheck.NONE);
                        c.global("cf","scriptsPaused",paused || dialogue(c)>=0);c.input(skip?131072:0,0);ScriptCheck.call(c,"x","updateBackgroundScripts",ActorCheck.NONE);
                        if((boolean)c.global("cg","cg")){int guard=0;while((boolean)ScriptCheck.call(c,"z","hasActiveBackgroundScript",ActorCheck.NONE)){ScriptCheck.call(c,"x","updateBackgroundScripts",ActorCheck.NONE);if(++guard>1024)throw new AssertionError("skip loop stuck");}c.global("cg","cg",false);}
                    }
                    String r=row(a,actors[0],name,frame);ActorCheck.equal(r,row(b,actors[1],name,frame),"intro timeline and actors");out.print(r);
                }
                ActorCheck.equal((short)-2,((short[])a.global("cd","scriptTicks"))[0],"intro completes");ActorCheck.equal(false,a.global("Z","Z"),"cutscene camera/input flag released");
                if(name.equals("normal")||name.equals("pause")||name.equals("no-skip"))ActorCheck.equal(6,seen,"six dialogue acknowledgements");
            }
        }}System.out.println("level-1 intro: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
