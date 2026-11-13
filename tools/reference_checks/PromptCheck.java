import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
/** Placed tutorial prompt handlers (level-0 records plus the five shipped level-1 records) and visibility without a MIDP renderer. */
public final class PromptCheck {
    static final int[] LEVEL1_UIDS={495,499,75,85,84};
    static final String[] LEVEL1_MODES={"overlap","miss","pause","hidden","cast"};
    static final String[] EXTRA={"edge","point","miss","pause","hidden","frozen","delayed","magic0","magic1","magic-miss","magic-point","switch","switch-idle"};
    static boolean alive(ActorCheck.Access c,Object e)throws Exception{for(int i=0;i<(int)c.global("bt","actorCount");++i)if(java.lang.reflect.Array.get(c.global("bs","actors"),i)==e)return true;return false;}
    static String row(ActorCheck.Access c,Object e,String name,int frame,int phase)throws Exception{
        var s=new StringBuilder("P\t"+name+"\t"+frame+"\t"+phase);
        for(String[] n:new String[][]{{"aw","aw"},{"ax","ax"},{"K","positionX"},{"L","positionY"},{"f","animationId"},{"h","animationFrame"},{"i","animationTick"},{"c","flags"}})s.append('\t').append(c.get(e,n[0],n[1]));
        s.append('\t').append(alive(c,e)?1:0).append('\t').append(c.get(null,"cB","cB"));
        boolean visible=alive(c,e) && (boolean)c.call(e,"h","isVisible",ActorCheck.NONE) && (((int)c.get(e,"c","flags")&1024)==0);
        return s.append('\t').append(visible?1:0).toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/prompt-oracle.tsv");
        var records=LevelCheck.records(BehaviorCheck.resource(assets,3,0)).stream().filter(r->r.values()[0]==21).toList();
        var names=new ArrayList<String>();for(int i=0;i<records.size();++i)names.add("placed"+i);names.addAll(List.of(EXTRA));
        var records1=LevelCheck.records(BehaviorCheck.resource(assets,4,0)).stream().filter(r->r.values()[0]==21).toList();
        for(int uid:LEVEL1_UIDS)for(String m:LEVEL1_MODES)names.add("level1-"+uid+"-"+m);
        try(var out=new PrintWriter(Files.newBufferedWriter(file))){for(String name:names){
            try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true)){
                boolean l1=name.startsWith("level1-");int l1Uid=l1?Integer.parseInt(name.split("-")[1]):0;String mode=l1?name.split("-")[2]:name;
                short[] base=(l1?records1.stream().filter(x->x.values()[1]==l1Uid).findFirst().orElseThrow():records.get(name.startsWith("placed")?Integer.parseInt(name.substring(6)):0)).values();
                Object[] roots=new Object[2];int[][] boxes=new int[2][];int which=0;
                for(var c:List.of(a,b)){
                    LevelCheck.setup(c,assets,l1?4:3);c.player(0,100);short[] r=base.clone();
                    if(name.startsWith("magic"))r[4]=(short)(name.equals("magic1")?18:17);if(name.startsWith("switch"))r[4]=34;
                    Object root=c.create();c.call(root,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(r,20),true,0,r.length);BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},root);c.set(root,"ae","persistenceIndex",0);boxes[which]=((int[])c.get(root,"j","collisionBounds")).clone();roots[which++]=root;
                    c.global("L","cameraX",(int)c.get(root,"K","positionX")-120);c.global("M","cameraY",(int)c.get(root,"L","positionY")-160);int x=(int)c.global("L","cameraX"),y=(int)c.global("M","cameraY");c.global("aa","viewBounds",new int[]{x,y,x+240,y+320});
                    if(mode.equals("hidden"))c.set(root,"c","flags",((int)c.get(root,"c","flags"))|1024);if(name.equals("frozen"))c.set(root,"c","flags",((int)c.get(root,"c","flags"))|16384);if(name.equals("delayed"))c.set(root,"ax","ax",1);
                }
                for(int frame=0;frame<16;++frame){
                    for(int i=0;i<2;++i){var c=i==0?a:b;Object e=roots[i],p=c.global("bk","player");int[] box=boxes[i];int[] rect=frame>=2 && frame<=6?new int[]{box[0]+1,box[1]+1,box[0]+2,box[1]+2}:new int[]{-1000,-1000,-999,-999};
                        if(name.equals("edge") && frame>=2 && frame<=6)rect=new int[]{box[0]-2,box[1],box[0],box[1]+1};
                        if((name.equals("point")||name.equals("magic-point")) && frame>=2 && frame<=6)rect=new int[]{box[0]+1,box[1]+1,box[0]+1,box[1]+1};
                        if(mode.equals("miss"))rect=new int[]{-1000,-1000,-999,-999};c.set(p,"j","collisionBounds",rect);
                        c.set(null,"cA","cA",frame==4 && name.startsWith("magic")?(name.equals("magic1")||name.equals("magic-miss")?1:0):frame==4 && mode.equals("cast")?(base[4]==18?1:0):-1);c.set(null,"cB","cB",frame==4 && name.equals("switch")?1:0);
                        if(mode.equals("pause"))c.global("x","state",14);
                        if(alive(c,e))c.call(e,"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase){
                        if(phase==1)for(int i=0;i<2;++i){var c=i==0?a:b;Object e=roots[i];if(alive(c,e) && !mode.equals("pause") && !mode.equals("hidden"))c.call(e,"f","advanceAnimation",ActorCheck.NONE);}
                        String r=row(a,roots[0],name,frame,phase);ActorCheck.equal(r,row(b,roots[1],name,frame,phase),"placed prompt update/visibility");out.println(r);
                    }
                }
            }
        }}System.out.println("native prompts: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
