import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Weapon-0 player poses, combos, anchor lifecycle and attack geometry; no enemy AI. */
public final class MeleeCheck {
    static final String[] CASES={"stand","combo","late_combo","held","walk","crouch","blocked","air","air_move","fall","paused","left"};
    static Object weapon(ActorCheck.Access a) throws Exception {
        Object w=a.create(); short[] record=new short[20];
        record[0]=28; record[1]=191; record[2]=407; record[3]=308; record[5]=16;
        a.call(w,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},record,true,0,6);
        a.global("bo","bo",w); return w;
    }
    static void row(ActorCheck.Access a,Object p,Object w,PrintWriter out,String scenario,int frame,int phase) throws Exception {
        out.print("M\t"+scenario+"\t"+frame+"\t"+phase);
        for(String[] field:ActorCheck.SNAPSHOT) {
            Object value=a.get(p,field[0],field[1]);
            if(value instanceof int[] rect) for(int n:rect) out.print("\t"+n); else out.print("\t"+value);
        }
        out.print("\t"+a.get(null,"e","queuedComboAnimation")+"\t"+a.get(null,"db","db")+"\t"+a.get(p,"aw","aw"));
        for(String[] field:new String[][]{{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},{"c","flags"},{"f","animationId"},{"h","animationFrame"},{"i","animationTick"}})
            out.print("\t"+a.get(w,field[0],field[1]));
        int mask=0,x=(int)a.get(p,"K","positionX");
        for(int i=0;i<8;++i) {
            int left=x-60+i*15;
            if((boolean)a.call(p,"b","b",new Class<?>[]{int.class,int[].class},0,new int[]{left,140,left+10,199})) mask|=1<<i;
        }
        out.println("\t"+mask);
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),compiled=Path.of(args[1]),stubs=Path.of(args[2]);
        Path output=assets.getParent().resolve("analysis/melee-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);
            var b=new ActorCheck.Access(compiled,assets,stubs,true);
            var out=new PrintWriter(Files.newBufferedWriter(output))) {
            a.loadAssets(assets); b.loadAssets(assets);
            for(String scenario:CASES) {
                Object[] roots=new Object[4];
                for(var access:List.of(a,b)) {
                    access.clearWorld(); access.set(null,"db","db",0); access.set(null,"bK","activeWeapon",0);
                    Object p=access.player(100,scenario.equals("fall")?119:199),w=weapon(access);
                    if(scenario.equals("blocked")) { access.call(p,"b","setAnimationImmediate",ActorCheck.INT,10); access.call(p,"g","refreshBounds",ActorCheck.NONE); }
                    byte[] tiles=(byte[])access.global("dL","collisionTiles");
                    for(int i=400;i<800;++i) tiles[i]=24;
                    if(scenario.equals("blocked")) for(int i=280;i<320;++i) tiles[i]=24;
                    int offset=access==a?0:2; roots[offset]=p; roots[offset+1]=w;
                }
                boolean sawStrike=false,sawCombo=false,sawAir=false,sawWeapon=false;
                for(int frame=0;frame<80;++frame) {
                    boolean paused=scenario.equals("paused") && frame>=6 && frame<16;
                    int pressed=frame==4?65568:0,held=0;
                    if(scenario.equals("combo") && frame==6 || scenario.equals("late_combo") && frame==11) pressed=65568;
                    if(scenario.equals("held") && frame>=4 && frame<30) held=65568;
                    if(scenario.equals("walk") && frame<25 || scenario.equals("air_move") && frame<25) held=8256;
                    if(scenario.equals("left") && frame<4) held=4112;
                    if((scenario.equals("crouch") || scenario.equals("blocked")) && frame<10) held=33024;
                    if(scenario.equals("crouch") || scenario.equals("blocked")) pressed=frame==6?65568:0;
                    if(scenario.equals("air") || scenario.equals("air_move")) pressed=frame==4?16388:frame==7?65568:0;
                    if(scenario.equals("fall")) pressed=frame==10?65568:0;
                    for(var access:List.of(a,b)) {
                        int offset=access==a?0:2; access.input(pressed,held);
                        access.global("x","state",paused?14:8); access.global("z","stateFrame",frame);
                        access.call(roots[offset],"s","update",ActorCheck.NONE);
                        access.call(roots[offset+1],"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase) {
                        if(phase==1 && !paused) for(var access:List.of(a,b)) {
                            int offset=access==a?0:2;
                            access.call(roots[offset],"f","advanceAnimation",ActorCheck.NONE);
                            if(((int)access.get(roots[offset+1],"c","flags")&(1024|16384))==0)
                                access.call(roots[offset+1],"f","advanceAnimation",ActorCheck.NONE);
                        }
                        ActorCheck.snapshot(a,b,roots[0],roots[2],scenario+" player "+frame+"/"+phase);
                        ActorCheck.snapshot(a,b,roots[1],roots[3],scenario+" weapon "+frame+"/"+phase);
                        ActorCheck.equal(a.get(null,"e","queuedComboAnimation"),b.get(null,"e","queuedComboAnimation"),"combo");
                        ActorCheck.equal(a.get(null,"db","db"),b.get(null,"db","db"),"recovery");
                        ActorCheck.equal(a.get(roots[0],"aw","aw"),b.get(roots[2],"aw","aw"),"melee timer");
                        row(a,roots[0],roots[1],out,scenario,frame,phase);
                    }
                    int pose=(int)a.get(roots[0],"f","animationId");
                    sawStrike|=pose==13; sawCombo|=pose==14; sawAir|=pose==15;
                    sawWeapon|=((int)a.get(roots[1],"c","flags")&128)==0;
                }
                if(scenario.equals("stand")) ActorCheck.equal(true,sawStrike && sawWeapon,"standing strike and visible weapon");
                if(scenario.equals("combo")) ActorCheck.equal(true,sawCombo,"combo must reach second strike");
                if(scenario.equals("air")) ActorCheck.equal(true,sawAir,"jump attack must reach aerial pose");
                if(scenario.equals("blocked")) ActorCheck.equal(false,sawStrike,"low ceiling prevents standing attack");
            }
        }
        System.out.println("weapon-0 melee: 1920 snapshots, "+ActorCheck.comparisons+" original/reference comparisons; "+output);
    }
}
