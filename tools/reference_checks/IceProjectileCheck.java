import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
/** Original isolated ice shot, tile/slope collision and temporary platform contacts. */
public final class IceProjectileCheck {
    static final String[] CASES={"flight","left","wall","left-wall","old-wall","pause","hidden","frozen","remove-wall","ride","walkoff","jump","slope16","slope17","slope18","slope19","slope20","slope21","edge"};
    static String row(ActorCheck.Access c,Object e,Object p,String s,int f,int phase)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);out.print("I\t"+s+"\t"+f+"\t"+phase);
        for(String[] n:new String[][]{{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},{"G","velocityX"},{"c","flags"},{"f","animationId"},{"g","pendingAnimationId"},{"h","animationFrame"},{"i","animationTick"},{"aw","aw"}})out.print("\t"+c.get(e,n[0],n[1]));
        out.print("\t"+(DeathCheck.actors(c).contains(e)?1:0));for(String[] n:new String[][]{{"bB","grounded"},{"bD","ceilingHit"},{"cR","wallOnLeft"},{"cS","wallOnRight"}})out.print("\t"+((boolean)c.get(e,n[0],n[1])?1:0));
        for(String[] n:new String[][]{{"j","collisionBounds"},{"k","attackBounds"},{"l","visualBounds"}}){int[] r=(int[])c.get(e,n[0],n[1]);for(int v:r==null?new int[4]:r)out.print("\t"+v);}
        out.print("\t"+c.get(p,"K","positionX")+"\t"+c.get(p,"L","positionY")+"\t"+c.get(p,"G","velocityX")+"\t"+c.get(p,"H","velocityY")+"\t"+c.get(p,"g","pendingAnimationId")+"\t"+(c.get(null,"aK","playerPlatform")==e?1:0));out.println();return w.toString();
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/ice-projectile-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            BehaviorCheck.commonAssets(a,assets);BehaviorCheck.commonAssets(b,assets);
            for(String s:CASES){Object[][] roots=new Object[2][];boolean sawPlatform=false,sawSupport=false;
                for(var c:List.of(a,b)){
                    CombatCheck.arena(c);Object p=c.player(s.startsWith("left")?180:100,199);if(s.startsWith("left")){c.set(p,"ak","facingLeft",true);c.set(p,"c","flags",1);}Object region=c.create();c.set(region,"j","collisionBounds",new int[]{0,0,800,400});c.global("au","au",region);
                    byte[] tiles=(byte[])c.global("dL","collisionTiles");if(!s.equals("flight") && !s.equals("left") && !s.equals("edge")){int col=s.equals("left-wall")?3:10,value=s.startsWith("slope")?Integer.parseInt(s.substring(5)):24;for(int row=0;row<20;++row)tiles[row*40+col]=(byte)value;}
                    Object e=c.call(p,"a","spawnActor",new Class<?>[]{int.class,int.class,int.class,int.class,int.class,int.class,boolean.class},39,1,7,s.startsWith("left")?10:-10,-50,302,false);c.set(e,"c","flags",16+(s.startsWith("left")?1:0));c.set(e,"G","velocityX",s.startsWith("left")?-2560:2560);
                    Object slots=java.lang.reflect.Array.newInstance(c.actor,2);java.lang.reflect.Array.set(slots,0,s.equals("old-wall")?c.create():e);if(s.equals("old-wall"))java.lang.reflect.Array.set(slots,1,e);c.set(null,"aR","aR",slots);
                    if(s.equals("hidden") || s.equals("frozen"))c.set(e,"c","flags",16|(s.equals("hidden")?1024:16384));if(s.equals("edge"))c.global("au","au",region);roots[c==a?0:1]=new Object[]{p,e};
                }
                for(int f=0;f<180;++f){boolean paused=s.equals("pause") && f>=8 && f<18;
                    for(var c:List.of(a,b)){var r=roots[c==a?0:1];Object p=r[0],e=r[1];c.global("x","state",paused?14:8);
                        if(s.equals("remove-wall") && f==40){byte[] tiles=(byte[])c.global("dL","collisionTiles");for(int row=0;row<20;++row)tiles[row*40+10]=0;}
                        if((s.equals("ride") || s.equals("walkoff") || s.equals("jump")) && f==20){int[] box=(int[])c.get(e,"j","collisionBounds");c.set(p,"K","positionX",(int)c.get(e,"K","positionX"));c.set(p,"L","positionY",box[1]-2);c.set(p,"H","velocityY",512);c.call(p,"g","refreshBounds",ActorCheck.NONE);}
                        if(s.equals("walkoff") && f==30)c.set(p,"K","positionX",(int)c.get(p,"K","positionX")+100);
                        if(s.equals("jump") && f==30){c.set(p,"L","positionY",(int)c.get(p,"L","positionY")-20);c.set(p,"H","velocityY",-4608);}
                        if(DeathCheck.actors(c).contains(e))c.call(e,"s","update",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase){if(phase==1 && !paused)for(var c:List.of(a,b)){Object e=roots[c==a?0:1][1];if(DeathCheck.actors(c).contains(e) && ((int)c.get(e,"c","flags")&1024)==0){c.call(e,"g","refreshBounds",ActorCheck.NONE);c.call(e,"f","advanceAnimation",ActorCheck.NONE);}}
                        String r=row(a,roots[0][1],roots[0][0],s,f,phase);ActorCheck.equal(r,row(b,roots[1][1],roots[1][0],s,f,phase),"ice projectile/platform");out.print(r);
                    }
                    sawPlatform|=(int)a.get(roots[0][1],"f","animationId")==9;sawSupport|=a.get(null,"aK","playerPlatform")==roots[0][1];
                }
                if(s.equals("wall"))ActorCheck.equal(true,sawPlatform,"wall impact creates ice platform");if(s.equals("ride"))ActorCheck.equal(true,sawSupport,"player can land on ice platform");
            }
        }System.out.println("ice projectile: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
