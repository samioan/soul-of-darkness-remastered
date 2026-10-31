import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Type-1 AI with an empty world queue and synthetic non-damaging player rectangles. */
public final class EnemyCheck {
    static final String[] CASES={"idle","pursue","right","attack0","attack1","attack2","attack3","fall","ledge0","ledge1","wall","slope16","slope17","slope18","slope19","slope20","slope21","paused","inactive","flag32","touched","fade"};
    static int variant(String s) { return s.startsWith("attack")?s.charAt(6)-'0':s.equals("ledge1")?1:0; }
    static void row(ActorCheck.Access a,Object e,PrintWriter out,String s,int f,int phase) throws Exception {
        out.print("E\t"+s+"\t"+f+"\t"+phase);
        for(String[] field:ActorCheck.SNAPSHOT) {
            Object value=a.get(e,field[0],field[1]);
            if(value instanceof int[] r) for(int n:r) out.print("\t"+n); else out.print("\t"+value);
        }
        for(String field:new String[]{"aw","ax","az","ap"}) out.print("\t"+a.get(e,field,field.equals("ap")?"health":field));
        out.print("\t"+((boolean)a.get(e,"ak","facingLeft")?1:0)+"\t"+((boolean)a.get(e,"Q","Q")?1:0));
        for(String[] field:new String[][]{{"m","customBounds"},{"aE","interactionBounds"}})
            for(int n:(int[])a.get(e,field[0],field[1])) out.print("\t"+n);
        out.println();
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),compiled=Path.of(args[1]),stubs=Path.of(args[2]);
        Path output=assets.getParent().resolve("analysis/enemy-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false); var b=new ActorCheck.Access(compiled,assets,stubs,true);
            var out=new PrintWriter(Files.newBufferedWriter(output))) {
            BehaviorCheck.commonAssets(a,assets); BehaviorCheck.commonAssets(b,assets);
            for(String scenario:CASES) {
                Object[] roots=new Object[4]; boolean sawAttack=false,sawRest=false,sawFall=false;
                for(var access:List.of(a,b)) {
                    access.clearWorld(); Object p=access.player(100,199),e=access.create();
                    short[] record={1,19,180,(short)(scenario.equals("fall")?119:199),0,9,-100,-125,200,125,(short)variant(scenario),0,-1};
                    if(scenario.equals("inactive")) record[5]|=1024;
                    if(scenario.equals("flag32")) record[5]|=32;
                    if(scenario.equals("fade")){record[5]|=128;record[11]=7;}
                    access.call(e,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},java.util.Arrays.copyOf(record,20),true,0,13);
                    byte[] tiles=(byte[])access.global("dL","collisionTiles");
                    for(int i=400;i<800;++i) tiles[i]=24;
                    if(scenario.startsWith("ledge")) for(int y=10;y<20;++y) for(int x=0;x<7;++x) tiles[y*40+x]=0;
                    if(scenario.equals("wall")) for(int y=0;y<10;++y) tiles[y*40+7]=24;
                    if(scenario.startsWith("slope")) for(int x=0;x<40;++x) tiles[400+x]=(byte)Integer.parseInt(scenario.substring(5));
                    int i=access==a?0:2; roots[i]=p; roots[i+1]=e;
                }
                for(int frame=0;frame<96;++frame) {
                    boolean paused=scenario.equals("paused") && frame>=6 && frame<20;
                    for(var access:List.of(a,b)) {
                        int i=access==a?0:2; Object p=roots[i],e=roots[i+1];
                        int px=scenario.equals("idle")?600:scenario.equals("right")?260:100;
                        int py=scenario.equals("fall")?100:130; int[] rect={px-5,py,px+5,py+1};
                        if(scenario.startsWith("attack") && frame>=12) {
                            int[] c=(int[])access.get(e,"j","collisionBounds");
                            px=(int)access.get(e,"K","positionX")-30;
                            rect=new int[]{c[0]-2,c[1]-9,c[0]-1,c[1]-8};
                        }
                        access.set(p,"K","positionX",px); access.set(p,"a","fixedX",px<<8);
                        access.set(p,"j","collisionBounds",rect); access.set(p,"k","attackBounds",new int[4]);
                        if(scenario.equals("touched") && frame==10) access.set(e,"Q","Q",true);
                        access.global("x","state",paused?14:8); access.global("z","stateFrame",frame);
                        access.call(e,"s","update",ActorCheck.NONE);
                        ActorCheck.equal(60,access.global("p","health"),"AI fixture must not apply player damage");
                        ActorCheck.equal(0,access.get(p,"f","animationId"),"AI fixture must not hit player");
                    }
                    for(int phase=0;phase<2;++phase) {
                        if(phase==1 && !paused) for(var access:List.of(a,b)) {
                            Object e=roots[access==a?1:3];
                            if(((int)access.get(e,"c","flags")&1024)==0) {
                                access.call(e,"g","refreshBounds",ActorCheck.NONE); access.call(e,"f","advanceAnimation",ActorCheck.NONE);
                            }
                        }
                        ActorCheck.snapshot(a,b,roots[1],roots[3],scenario+" "+frame+"/"+phase);
                        for(String[] field:new String[][]{{"aw","aw"},{"ax","ax"},{"az","az"},{"ap","health"},{"ak","facingLeft"},{"Q","Q"},{"m","customBounds"},{"aE","interactionBounds"}})
                            ActorCheck.equal(a.get(roots[1],field[0],field[1]),b.get(roots[3],field[0],field[1]),scenario+" "+field[1]);
                        row(a,roots[1],out,scenario,frame,phase);
                    }
                    int state=(int)a.get(roots[1],"ao","actionState"); sawAttack|=state==102; sawRest|=state==103; sawFall|=state==1;
                }
                if(scenario.startsWith("attack")) ActorCheck.equal(true,sawAttack && sawRest,"attack/recovery cycle "+scenario);
                if(scenario.equals("fall")) ActorCheck.equal(true,sawFall,"falling AI must execute");
            }
        }
        System.out.println("type-1 AI: 4224 snapshots; "+ActorCheck.comparisons+" original/reference comparisons; "+output);
    }
}
