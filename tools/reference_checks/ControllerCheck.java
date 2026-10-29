import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Level-5 controller/hazard activation and checkpoint restoration with live links. */
public final class ControllerCheck {
    static int checks;
    static final Class<?>[] NONE={};
    static final String[][] FIELDS={{"f","animationId"},{"h","animationFrame"},{"K","positionX"},
        {"L","positionY"},{"ao","actionState"},{"c","flags"},{"aw","aw"}};
    static void eq(Object expected,Object actual,String label) {
        ++checks;
        if(!java.util.Objects.deepEquals(expected,actual)) throw new AssertionError(label+": "+expected+" != "+actual);
    }
    static void compare(ActorCheck.Access a,ActorCheck.Access b,String label) throws Exception {
        LifecycleCheck.compare(a,b,label);
        for(String[] f:new String[][]{{"am","activeCameraControllerUid"},{"an","hazardCameraOffsetY"},
                {"ak","cameraControllerMode"},{"aj","cameraControllerActive"},{"ao","cameraScrollHorizontal"}})
            eq(a.global(f[0],f[1]),b.global(f[0],f[1]),label+" "+f[1]);
    }
    static void setup(ActorCheck.Access a,Path assets) throws Exception {
        LevelCheck.setup(a,assets,8);
        ScriptCheck.call(a,"a","decodeScripts",new Class<?>[]{byte[].class},BehaviorCheck.resource(assets,8,1));
        a.global("bH","bH",((short[][])a.global("bG","bG"))[5]);
        ScriptCheck.call(a,"c","constructLevelActors",new Class<?>[]{boolean.class},false);
    }
    static void activate(ActorCheck.Access a) throws Exception {
        Object player=a.global("bk","player");
        a.set(player,"K","positionX",60); a.set(player,"L","positionY",5802);
        a.call(player,"g","refreshBounds",NONE);
        a.global("aa","viewBounds",new int[]{0,5600,240,5920});
        a.global("L","cameraX",0); a.global("M","cameraY",5600);
        a.global("N","cameraTargetX",0); a.global("O","cameraTargetY",5600);
        a.call(LifecycleCheck.uid(a,9),"bi","updateCameraController",NONE);
        eq(9,a.global("am","activeCameraControllerUid"),"real controller activates");
        eq(4,a.global("ak","cameraControllerMode"),"real controller mode");
        eq(true,a.global("aj","cameraControllerActive"),"controller overlap activates camera");
        eq(false,a.global("ao","cameraScrollHorizontal"),"controller selects vertical scrolling");
        eq(5598,a.global("O","cameraTargetY"),"controller target moves two pixels upward");
        a.call(LifecycleCheck.uid(a,21),"bc","updateHazardArea78",NONE);
        eq(5916,a.get(LifecycleCheck.uid(a,21),"L","positionY"),"hazard clamps above viewport bottom");
        eq(316,a.global("an","hazardCameraOffsetY"),"hazard publishes camera-relative offset");
    }
    static void live(ActorCheck.Access a,ActorCheck.Access b,Path assets,boolean continuing) throws Exception {
        int[][][] snapshots=new int[2][3][FIELDS.length];
        int[][] uids=new int[2][3];
        Object[][] oldActors=new Object[2][3];
        byte[][] saves=new byte[2][];
        for(var access:List.of(a,b)) {
            int side=access==a?0:1;
            setup(access,assets); activate(access);
            Object links=access.get(null,"cq","cq");
            for(int slot=0;slot<3;++slot) {
                Object actor=Array.get(links,slot); oldActors[side][slot]=actor;
                uids[side][slot]=(int)access.get(actor,"al","uid");
                eq(new int[]{10,12,13}[slot],uids[side][slot],"constructor establishes shipped platform slot");
                access.set(actor,"K","positionX",70+slot*60); access.set(actor,"L","positionY",5780+slot);
                access.set(actor,"ao","actionState",101);
                access.set(actor,"c","flags",(int)access.get(actor,"c","flags")|16);
                for(int f=0;f<FIELDS.length;++f) snapshots[side][slot][f]=(int)access.get(actor,FIELDS[f][0],FIELDS[f][1]);
            }
            access.call(LifecycleCheck.uid(access,32),"ao","updateCheckpoint",NONE);
            saves[side]=LifecycleCheck.stored(access);
            eq(9,(int)(short)SpriteCheck.u16(saves[side],731),"checkpoint saves active controller UID");
            eq(5600,(int)(short)SpriteCheck.u16(saves[side],733),"checkpoint saves current camera, not target");
            eq(316,(int)(short)SpriteCheck.u16(saves[side],735),"checkpoint saves hazard offset");
            for(int slot=0;slot<3;++slot)
                eq(uids[side][slot],(int)(short)SpriteCheck.u16(saves[side],664+2*slot),"checkpoint saves live linked UID");
            // Restore uses the saved offset to place the hazard but does not assign
            // the global offset itself. A later hazard update republishes it.
            access.global("an","hazardCameraOffsetY",-777);
            if(continuing) {
                ScriptCheck.call(access,"n","clearActors",NONE);
                access.call(null,"m","resetWorldActorState",NONE);
                access.global("x","state",2); MenuCheck.press(access,2,65568);
                eq(false,ScriptCheck.call(access,"K","loadLevelStep",new Class<?>[]{int.class},79),"Continue stage constructs");
                ScriptCheck.call(access,"K","initializeCamera",NONE);
            } else {
                access.sprites.call(null,"g","createImageCache",new Class<?>[]{int.class,int.class},0,3);
                access.global("x","state",14);
                ScriptCheck.call(access,"al","restartLevel",NONE);
            }
            eq(9,access.global("am","activeCameraControllerUid"),"restored controller UID");
            eq(4,access.global("ak","cameraControllerMode"),"restored camera mode");
            eq(5598,access.global("M","cameraY"),"restored camera subtracts two pixels");
            eq(5598,access.global("O","cameraTargetY"),"restored camera target");
            eq(5598,((int[])access.global("aa","viewBounds"))[1],"restored visible top");
            eq(5918,((int[])access.global("aa","viewBounds"))[3],"restored visible bottom");
            eq(-777,access.global("an","hazardCameraOffsetY"),"restore leaves global offset untouched");
            Object hazard=LifecycleCheck.uid(access,21),controller=LifecycleCheck.uid(access,9);
            eq(5916,access.get(hazard,"L","positionY"),"hazard restores saved camera plus offset");
            eq(764,access.get(hazard,"ar","ar"),"hazard extent correction from original spawn");
            for(Object actor:List.of(hazard,controller)) {
                eq(16,(int)access.get(actor,"c","flags")&16,"restore forces controller/hazard update");
                eq(0,(int)access.get(actor,"c","flags")&32,"restore clears controller/hazard disabled flag");
            }
            Object restoredLinks=access.get(null,"cq","cq");
            for(int slot=0;slot<3;++slot) {
                Object actor=Array.get(restoredLinks,slot);
                eq(false,actor==oldActors[side][slot],"restored platform is a new actor");
                eq(true,actor==LifecycleCheck.uid(access,uids[side][slot]),"slot points to reconstructed world actor");
                for(int f=0;f<FIELDS.length;++f)
                    eq(snapshots[side][slot][f],access.get(actor,FIELDS[f][0],FIELDS[f][1]),"checkpoint round-trip snapshot field");
            }
            eq(continuing,LifecycleCheck.uid(access,32)!=null,"Continue and Restart retain distinct checkpoint removal rules");
            eq(saves[side],access.global("cw","saveBuffer"),"reconstruction keeps saved bytes");
        }
        compare(a,b,"live save restore continuing="+continuing);
        for(var access:List.of(a,b)) {
            access.call(LifecycleCheck.uid(access,21),"bc","updateHazardArea78",NONE);
            eq(5914,access.get(LifecycleCheck.uid(access,21),"L","positionY"),"resumed hazard moves from restored position");
            eq(316,access.global("an","hazardCameraOffsetY"),"resumed hazard refreshes offset");
        }
        compare(a,b,"resumed hazard continuing="+continuing);
        for(var access:List.of(a,b)) {
            Object hazard=LifecycleCheck.uid(access,21);
            access.call(hazard,"s","update",NONE);
            eq(5912,access.get(hazard,"L","positionY"),"common update continues hazard motion");
            eq(5914<<8,access.get(hazard,"b","fixedY"),"common update synchronizes fixed point before handler movement");
            eq(5912,((int[])access.get(hazard,"j","collisionBounds"))[1],"resumed update rebuilds hazard collision top");
            eq(314,access.global("an","hazardCameraOffsetY"),"hazard offset follows movement relative to fixed camera");
        }
        compare(a,b,"common hazard update continuing="+continuing);
    }
    static void signedRestore(ActorCheck.Access a,ActorCheck.Access b,Path assets,int camera,int offset) throws Exception {
        for(var access:List.of(a,b)) {
            LevelCheck.setup(access,assets,8);
            byte[] save=new byte[1313];
            SaveCheck.put16(save,731,9); SaveCheck.put16(save,733,camera); SaveCheck.put16(save,735,offset);
            for(int slot=0;slot<3;++slot) SaveCheck.put16(save,664+2*slot,-1);
            access.global("cw","saveBuffer",save);
            ScriptCheck.call(access,"d","reconstructLevelActors",new Class<?>[]{boolean.class},true);
            eq(camera-2,access.global("M","cameraY"),"signed camera short");
            eq(camera+offset,access.get(LifecycleCheck.uid(access,21),"L","positionY"),"signed short sum uses int arithmetic");
            eq(6680-camera-offset,access.get(LifecycleCheck.uid(access,21),"ar","ar"),"signed extent correction");
            eq(camera+318,((int[])access.global("aa","viewBounds"))[3],"signed viewport bottom");
        }
        compare(a,b,"signed restore camera="+camera+" offset="+offset);
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),compiled=Path.of(args[1]),stubs=Path.of(args[2]);
        for(String raw:Files.readAllLines(Path.of(args[3]))) {
            String[] p=raw.strip().split("\\s+");
            if(p.length==3&&p[0].equals("field")) BehaviorCheck.names.put(p[1],p[2]);
        }
        for(boolean continuing:new boolean[]{false,true}) {
            try(var a=new ActorCheck.Access(assets,assets,stubs,false);
                var b=new ActorCheck.Access(compiled,assets,stubs,true)) { live(a,b,assets,continuing); }
        }
        for(int[] values:new int[][]{{5600,316},{-32768,-32768},{32767,32767},{-1,0},{0,-1}}) {
            try(var a=new ActorCheck.Access(assets,assets,stubs,false);
                var b=new ActorCheck.Access(compiled,assets,stubs,true)) { signedRestore(a,b,assets,values[0],values[1]); }
        }
        System.out.println("level-5 controller: OK ("+checks+" expectations, "+LevelCheck.checks+" level comparisons, "+BehaviorCheck.comparisons+" world comparisons; controlled handlers and test RMS)");
    }
}
