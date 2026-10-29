import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
/** Ice physical attack/combos and original weapon switch gates; projectile separate. */
public final class IceWeaponCheck {
    static final String[] CASES={"stand","combo","late_combo","held","walk","crouch","blocked","air","air_move","fall","paused","left","switch","switch-held","locked","switch-back","air-switch","priority"};
    static boolean switching(String s){return s.startsWith("switch") || s.equals("locked") || s.equals("air-switch") || s.equals("priority");}
    static String row(ActorCheck.Access c,Object p,Object weapon,String s,int f,int phase)throws Exception{
        var w=new java.io.StringWriter();MeleeCheck.row(c,p,weapon,new PrintWriter(w),s,f,phase);String base=w.toString().stripTrailing();int mask=0,x=(int)c.get(p,"K","positionX");for(int i=0;i<8;++i){int left=x-60+i*15;if((boolean)c.call(p,"b","b",new Class<?>[]{int.class,int[].class},1,new int[]{left,140,left+10,199}))mask|=1<<i;}
        return base+"\t"+c.get(null,"bK","activeWeapon")+"\t"+c.get(null,"aS","weaponSwitchCooldownMs")+"\t"+mask+"\n";
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/ice-weapon-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            a.loadAssets(assets);b.loadAssets(assets);
            for(String s:CASES){
                Object[][] roots=new Object[2][];boolean sawIce=false,sawCombo=false,sawAir=false;
                for(var c:List.of(a,b)){
                    c.clearWorld();c.set(null,"db","db",0);c.set(null,"e","queuedComboAnimation",-1);c.set(null,"bK","activeWeapon",switching(s) && !s.equals("switch-back")?0:1);c.set(null,"bs","bs",!s.equals("locked") && !s.equals("switch-back"));c.set(null,"aS","weaponSwitchCooldownMs",0);
                    Object p=c.player(100,s.equals("fall")?119:199),weapon=MeleeCheck.weapon(c);Arrays.fill((byte[])c.global("dL","collisionTiles"),400,800,(byte)24);
                    if(s.equals("blocked")){c.call(p,"b","setAnimationImmediate",ActorCheck.INT,10);c.call(p,"g","refreshBounds",ActorCheck.NONE);Arrays.fill((byte[])c.global("dL","collisionTiles"),280,320,(byte)24);}roots[c==a?0:1]=new Object[]{p,weapon};
                }
                for(int f=0;f<80;++f){
                    boolean paused=s.equals("paused") && f>=6 && f<16;int pressed=f==4?65568:0,held=0;
                    if(s.equals("combo") && f==6 || s.equals("late_combo") && f==11)pressed=65568;
                    if(s.equals("held") && f>=4 && f<30)held=65568;
                    if((s.equals("walk") || s.equals("air_move")) && f<25)held=8256;
                    if(s.equals("left") && f<4)held=4112;
                    if((s.equals("crouch") || s.equals("blocked")) && f<10)held=33024;
                    if(s.equals("crouch") || s.equals("blocked"))pressed=f==6?65568:0;
                    if(s.equals("air") || s.equals("air_move"))pressed=f==4?16388:f==7?65568:0;
                    if(s.equals("fall"))pressed=f==10?65568:0;
                    if(switching(s)){
                        pressed=(f==4 || (!s.equals("switch-held") && (f==6 || f==10 || f==12 || f==16)))?512:0;held=s.equals("switch-held") && f>=4 && f<30?512:0;
                        if(s.equals("air-switch") && f==2)pressed=16384;if(s.equals("air-switch") && f==7)pressed=65568;
                        if(s.equals("priority") && f==4)pressed=512|65568;
                    }
                    for(var c:List.of(a,b)){var r=roots[c==a?0:1];c.input(pressed,held);c.global("x","state",paused?14:8);c.global("z","stateFrame",f);c.call(r[0],"s","update",ActorCheck.NONE);c.call(r[1],"s","update",ActorCheck.NONE);}
                    for(int phase=0;phase<2;++phase){
                        if(phase==1 && !paused)for(var c:List.of(a,b)){var r=roots[c==a?0:1];c.call(r[0],"f","advanceAnimation",ActorCheck.NONE);if(((int)c.get(r[1],"c","flags")&(1024|16384))==0)c.call(r[1],"f","advanceAnimation",ActorCheck.NONE);}
                        String x=row(a,roots[0][0],roots[0][1],s,f,phase);ActorCheck.equal(x,row(b,roots[1][0],roots[1][1],s,f,phase),"ice melee/switch snapshots");out.print(x);
                    }
                    int pose=(int)a.get(roots[0][0],"f","animationId");sawIce|=pose==19;sawCombo|=pose==20;sawAir|=pose==21;
                }
                if(s.equals("stand"))ActorCheck.equal(true,sawIce,"ice standing strike");if(s.equals("combo"))ActorCheck.equal(true,sawCombo,"ice combo second strike");if(s.equals("air"))ActorCheck.equal(true,sawAir,"ice aerial strike");if(s.equals("blocked"))ActorCheck.equal(false,sawIce,"low ceiling blocks ice strike");if(s.equals("locked") || s.equals("switch-back"))ActorCheck.equal(0,a.get(null,"bK","activeWeapon"),"locked ice cannot be equipped");if(s.equals("switch-held"))ActorCheck.equal(1,a.get(null,"bK","activeWeapon"),"held switch does not repeat");
            }
        }System.out.println("ice melee/switch: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
