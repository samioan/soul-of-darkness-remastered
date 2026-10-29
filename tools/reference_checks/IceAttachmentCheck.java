import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
import java.lang.reflect.Array;
/** Original type-33/ice reciprocal links, parent following and melt cleanup. */
public final class IceAttachmentCheck {
    static final String[] CASES={"natural","natural-left","natural-hidden","natural-locked","natural-pose","natural-late","linked","linked-left","linked-vertical","linked-late","linked-paused","linked-melt","linked-replace","linked-rider","linked-expiry","linked-offset","linked-hidden"};
    static String row(ActorCheck.Access c,Object[] r,String s,int f,int phase)throws Exception{
        var w=new java.io.StringWriter();var out=new PrintWriter(w);
        for(int i=0;i<3;++i){Object e=r[i];out.print((i==0?"P":i==1?"M":"I")+"\t"+s+"\t"+f+"\t"+phase);
            for(String[] n:ActorCheck.SNAPSHOT){Object v=c.get(e,n[0],n[1]);if(v instanceof int[] a)for(int x:a)out.print("\t"+x);else out.print("\t"+v);}
            out.print("\t"+c.get(e,"aw","aw")+"\t"+c.get(e,"ar","ar"));
            out.print("\t"+(c.get(r[1],"r","attachedActor")==r[2]?1:0));
            out.print("\t"+(c.get(r[2],"s","s")==r[1]?1:0));
            out.print("\t"+(c.get(null,"aK","playerPlatform")==e?1:0));
            out.println("\t"+(DeathCheck.actors(c).contains(e)?1:0));
        }return w.toString().replace("\tnull","\t0\t0\t0\t0");
    }
    public static void main(String[] args)throws Exception{
        Path assets=Path.of(args[0]),build=Path.of(args[1]),stubs=Path.of(args[2]),file=assets.getParent().resolve("analysis/ice-attachment-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);var b=new ActorCheck.Access(build,assets,stubs,true);var out=new PrintWriter(Files.newBufferedWriter(file))){
            for(String s:CASES){Object[][] roots=new Object[2][];boolean sawLink=false,sawDetach=false;
                for(var c:List.of(a,b)){
                    BehaviorCheck.commonAssets(c,assets);CombatCheck.arena(c);Object p=c.player(s.endsWith("left")?220:100,199);c.set(null,"bE","playerInvulnerabilityMs",60000);if(s.endsWith("left")){c.set(p,"ak","facingLeft",true);c.set(p,"c","flags",1);}
                    c.set(null,"bm","bm",true);c.set(null,"bn","bn",true);c.set(null,"aT","aT",0);c.global("H","H",-1);
                    Object region=c.create();c.set(region,"j","collisionBounds",new int[]{0,0,800,400});c.global("au","au",region);
                    short[] rec={33,64,160,(short)(s.endsWith("offset")?159:149),(short)(s.endsWith("pose")?9:0),(short)(8|(s.equals("natural-hidden")?1024:s.equals("natural-locked")?32:0)),-60,-60,120,120,(short)(s.endsWith("vertical")?3:0),1,20};
                    Object m=c.create();c.call(m,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},Arrays.copyOf(rec,20),true,0,13);
                    c.set(m,"ai","cameraDistanceBucket",0);if(!s.endsWith("late"))BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},m);
                    Object e=c.call(p,"a","spawnActor",new Class<?>[]{int.class,int.class,int.class,int.class,int.class,int.class,boolean.class},39,1,7,s.endsWith("left")?10:-10,-50,302,false);c.set(e,"c","flags",16+(s.endsWith("left")?1:0));c.set(e,"G","velocityX",s.endsWith("left")?-2560:2560);
                    if(s.endsWith("late"))BehaviorCheck.gameCall(c,"b","addActor",new Class<?>[]{c.actor},m);
                    Object slots=Array.newInstance(c.actor,2);Array.set(slots,0,e);c.set(null,"aR","aR",slots);
                    if(s.startsWith("linked")){int[] box=(int[])c.get(m,"j","collisionBounds");c.set(e,"j","collisionBounds",box.clone());c.set(e,"k","attackBounds",box.clone());Object queue=Array.newInstance(c.actor,300);Array.set(queue,0,m);c.global("bw","updateActors",queue);c.global("bx","updateActorCount",1);c.call(e,"A","applyWeaponEffectHits",ActorCheck.INT,1);}
                    roots[c==a?0:1]=new Object[]{p,m,e};
                }
                String birth=row(a,roots[0],s,-1,0);ActorCheck.equal(birth,row(b,roots[1],s,-1,0),"ice link birth");out.print(birth);sawLink=a.get(roots[0][1],"r","attachedActor")==roots[0][2];
                for(int f=0;f<180;++f){boolean paused=s.endsWith("paused") && f>=12 && f<22;
                    for(var c:List.of(a,b)){Object[] r=roots[c==a?0:1];Object p=r[0],e=r[2];
                        if(s.equals("linked-hidden") && f==20)c.set(r[1],"c","flags",1032);
                        if(s.endsWith("melt") && f==4){c.call(e,"a","requestAnimation",ActorCheck.INT,10);c.set(e,"aw","aw",1000);}
                        if(s.endsWith("replace") && f==4){Object next=c.call(p,"a","spawnActor",new Class<?>[]{int.class,int.class,int.class,int.class,int.class,int.class,boolean.class},39,1,7,-10,-100,302,false);c.set(next,"c","flags",16);c.set(next,"G","velocityX",2560);Object slots=c.get(null,"aR","aR");Array.set(slots,1,e);Array.set(slots,0,next);c.call(e,"a","requestAnimation",ActorCheck.INT,10);c.set(e,"aw","aw",1000);}
                        if(s.endsWith("rider") && f==6){int[] box=(int[])c.get(e,"j","collisionBounds");int x=(int)c.get(e,"K","positionX"),y=box[1]-2;c.set(p,"K","positionX",x);c.set(p,"L","positionY",y);c.set(p,"a","fixedX",x*256);c.set(p,"b","fixedY",y*256);c.set(p,"H","velocityY",512);c.call(p,"g","refreshBounds",ActorCheck.NONE);}
                        c.global("x","state",paused?14:8);c.global("z","stateFrame",f);c.input(0,0);BehaviorCheck.gameCall(c,"W","updateWorldActors",ActorCheck.NONE);
                    }
                    for(int phase=0;phase<2;++phase){if(phase==1 && !paused)for(var c:List.of(a,b))LiveCombatCheck.paintTick(c);String x=row(a,roots[0],s,f,phase);ActorCheck.equal(x,row(b,roots[1],s,f,phase),"ice linked platform");out.print(x);}
                    boolean linked=a.get(roots[0][1],"r","attachedActor")==roots[0][2];sawLink|=linked;if(sawLink && !linked)sawDetach=true;
                }
                ActorCheck.equal(s.startsWith("linked"),sawLink,s+" attachment eligibility");
                if(s.endsWith("melt") || s.endsWith("replace") || s.endsWith("expiry"))ActorCheck.equal(true,sawDetach,s+" detaches");
            }
        }System.out.println("ice attachment: "+ActorCheck.comparisons+" original/reference expectations; "+file);
    }
}
