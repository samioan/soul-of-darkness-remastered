import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** Checkpoint collision, test RMS and repeated original restart calls on real levels. */
public final class LifecycleCheck {
    static int checks;
    static final Class<?>[] NONE={};
    static void eq(Object expected,Object actual,String label) {
        ++checks;
        if(!java.util.Objects.deepEquals(expected,actual)) throw new AssertionError(label+": "+expected+" != "+actual);
    }
    static Class<?> rms(ActorCheck.Access a) throws Exception {
        return Class.forName("javax.microedition.rms.RecordStore",true,a.sprites.loader);
    }
    static int counter(ActorCheck.Access a,String field) throws Exception { return rms(a).getField(field).getInt(null); }
    static byte[] stored(ActorCheck.Access a) throws Exception { return (byte[])rms(a).getMethod("testRecord").invoke(null); }
    static Object uid(ActorCheck.Access a,int uid) throws Exception {
        return ScriptCheck.call(a,"g","findActorByUid",new Class<?>[]{int.class},uid);
    }
    static void compare(ActorCheck.Access a,ActorCheck.Access b,String label) throws Exception {
        LevelCheck.compare(a,b,label);
        for(String old:new String[]{"L","M","N","O","aa","bG","bJ","cx","cz","cB","cC"}) {
            String name=BehaviorCheck.names.getOrDefault("e."+old,old);
            eq(BehaviorCheck.normalize(a,a.global(old,name)),BehaviorCheck.normalize(b,b.global(old,name)),label+" "+old);
        }
        eq(stored(a),stored(b),label+" RMS bytes");
    }
    static void run(ActorCheck.Access a,ActorCheck.Access b,Path assets,int pack) throws Exception {
        var rows=LevelCheck.records(BehaviorCheck.resource(assets,pack,0));
        int checkpointUid=rows.stream().filter(r->r.values()[0]==23).findFirst().orElseThrow().values()[1];
        int[] initialSpawns=new int[2];
        Object[] oldPlayers=new Object[2];
        int[] savedX=new int[2],savedY=new int[2],persistenceIndex=new int[2];
        for(var access:List.of(a,b)) {
            int side=access==a?0:1;
            LevelCheck.setup(access,assets,pack);
            ScriptCheck.call(access,"a","decodeScripts",new Class<?>[]{byte[].class},BehaviorCheck.resource(assets,pack,1));
            access.global("bH","bH",((short[][])access.global("bG","bG"))[pack-3]);
            ScriptCheck.call(access,"c","constructLevelActors",new Class<?>[]{boolean.class},false);
            initialSpawns[side]=(int)access.get(null,"ah","spawnRecordCount");
            ScriptCheck.call(access,"b","accessSaveRecord",new Class<?>[]{boolean.class},false);
            eq(null,stored(access),"empty RMS read does not add a record");
            eq(false,access.global("cx","saveLoaded"),"empty RMS read leaves loaded flag clear");
            Object checkpoint=uid(access,checkpointUid),player=access.global("bk","player");
            oldPlayers[side]=player;
            persistenceIndex[side]=(int)access.get(checkpoint,"ae","persistenceIndex");
            // A non-overlap must not serialize or remove the checkpoint.
            access.set(player,"j","collisionBounds",new int[]{-1000,-1000,-999,-999});
            access.call(checkpoint,"ao","updateCheckpoint",NONE);
            eq(0,counter(access,"adds"),"non-overlap does not write");
            eq(true,uid(access,checkpointUid)==checkpoint,"non-overlap keeps checkpoint");
            savedX[side]=(int)access.get(checkpoint,"K","positionX");
            savedY[side]=(int)access.get(checkpoint,"L","positionY");
            access.set(player,"K","positionX",savedX[side]); access.set(player,"L","positionY",savedY[side]);
            access.set(player,"ak","facingLeft",true); access.call(player,"g","refreshBounds",NONE);
            access.global("q","maxHealth",90); access.global("p","health",17);
            access.global("t","maxMagic",80); access.global("s","magic",33);
            access.global("bP","skipCheckpointPosition",true);
            if(pack==6) access.set(null,"bC","bC",true);
            access.call(checkpoint,"ao","updateCheckpoint",NONE);
            byte[] saved=stored(access);
            eq(1313,saved.length,"complete record written"); eq(1,counter(access,"adds"),"first checkpoint adds record 1");
            eq(false,access.global("cx","saveLoaded"),"writing does not set loaded flag");
            eq(checkpointUid+1,(int)(short)SpriteCheck.u16(saved,16),"checkpoint UID+1");
            eq((byte)-99,saved[999+persistenceIndex[side]],"checkpoint removal included in record");
            eq(null,uid(access,checkpointUid),"checkpoint removed from actor list");
            eq(false,access.global("bP","skipCheckpointPosition"),"checkpoint permits position restoration");
            eq(access.global("cw","saveBuffer"),saved,"RMS stores exact buffer");
            // Read through the original accessor into a different buffer.
            access.global("cw","saveBuffer",new byte[1313]);
            ScriptCheck.call(access,"b","accessSaveRecord",new Class<?>[]{boolean.class},false);
            eq(saved,access.global("cw","saveBuffer"),"record readback");
            eq(true,access.global("cx","saveLoaded"),"existing read sets loaded flag");
            // Exercise both addRecord and setRecord without changing the save bytes.
            ScriptCheck.call(access,"b","accessSaveRecord",new Class<?>[]{boolean.class},true);
            eq(1,counter(access,"sets"),"existing record overwritten");
            eq(saved,stored(access),"overwrite preserves bytes");
            ((int[])access.sprites.get(null,"L","cacheImageCounts"))[0]=3;
            access.sprites.call(null,"g","createImageCache",new Class<?>[]{int.class,int.class},0,3);
        }
        compare(a,b,"pack "+pack+" checkpoint");
        for(int cycle=0;cycle<2;++cycle) {
            for(var access:List.of(a,b)) {
                int side=access==a?0:1;
                access.global("x","state",14); access.global("p","health",1);
                access.global("cz","pressedMask",65568); access.global("cB","heldMask",65568);
                access.global("cC","frameHeldMask",65568);
                short[] ticks=(short[])access.global("cd","scriptTicks"); Arrays.fill(ticks,(short)123);
                access.global("cf","scriptsPaused",true);
                for(int[] offsets:(int[][])access.global("ce","scriptTrackOffsets")) Arrays.fill(offsets,99);
                Object cache=Array.get(access.sprites.get(null,"J","imageCaches"),0);
                Class<?> image=Class.forName("javax.microedition.lcdui.Image",true,access.sprites.loader);
                Object marker=image.getMethod("createRGBImage",int[].class,int.class,int.class,boolean.class)
                    .invoke(null,new int[]{-1},1,1,true);
                for(int i=0;i<3;++i) Array.set(cache,i,marker);
                ScriptCheck.call(access,"al","restartLevel",NONE);
                Object player=access.global("bk","player");
                eq(false,player==oldPlayers[side],"restart constructs new player"); oldPlayers[side]=player;
                eq(savedX[side],access.get(player,"K","positionX"),"restart checkpoint X");
                eq(savedY[side]-(pack==8?20:0),access.get(player,"L","positionY"),"restart checkpoint Y");
                eq(true,access.get(player,"ak","facingLeft"),"restart checkpoint facing");
                eq(90,access.global("p","health"),"restart fills saved maximum health");
                eq(33,access.global("s","magic"),"restart preserves saved magic");
                eq(null,uid(access,checkpointUid),"saved checkpoint stays removed");
                eq(initialSpawns[side],access.get(null,"ah","spawnRecordCount"),"respawn pool count resets before reconstruction");
                eq(8,access.global("x","state"),"restart enters gameplay");
                eq(0,access.global("cz","pressedMask"),"restart clears pressed input");
                eq(0,access.global("cB","heldMask"),"restart clears held input");
                eq(false,access.global("cf","scriptsPaused"),"restart unpauses scripts");
                for(short tick:(short[])access.global("cd","scriptTicks")) eq((short)-1,tick,"restart disables script clocks");
                byte[][][] tracks=(byte[][][])access.global("cb","scriptTracks");
                int[][] offsets=(int[][])access.global("ce","scriptTrackOffsets");
                for(int script=0;script<tracks.length;++script) for(int track=0;track<tracks[script].length;++track) {
                    byte[] data=tracks[script][track];
                    eq(data[data.length-2]&255,offsets[script][track],"restart rewinds shipped track offset");
                }
                for(int i=0;i<3;++i) eq(null,Array.get(cache,i),"restart clears occupied image cache");
                eq(access.global("cw","saveBuffer"),stored(access),"restart leaves stored checkpoint unchanged");
                eq(counter(access,"opens"),counter(access,"closes"),"RMS handles closed");
            }
            compare(a,b,"pack "+pack+" restart "+cycle);
        }
    }
    static void crossLevels(ActorCheck.Access a,ActorCheck.Access b,Path assets) throws Exception {
        int retained=0,mutated=0;
        Map<Integer,Integer> counts=new java.util.HashMap<>();
        for(int pack:new int[]{3,4,3,8,3}) {
            for(var access:List.of(a,b)) {
                @SuppressWarnings("unchecked")
                Map<Integer,int[]> registry=(Map<Integer,int[]>)access.get(null,"af","spawnRecordsByUid");
                Map<Integer,int[]> oldReferences=new java.util.HashMap<>(registry);
                Map<Integer,int[]> oldValues=new java.util.HashMap<>();
                registry.forEach((key,value)->oldValues.put(key,value.clone()));
                if(!oldReferences.isEmpty()) ScriptCheck.call(access,"n","clearActors",NONE);
                LevelCheck.setup(access,assets,pack);
                eq(0,access.get(null,"ah","spawnRecordCount"),"reset zeros pool cursor");
                eq(true,registry==access.get(null,"af","spawnRecordsByUid"),"reset retains UID table object");
                ScriptCheck.call(access,"c","constructLevelActors",new Class<?>[]{boolean.class},false);
                int count=(int)access.get(null,"ah","spawnRecordCount");
                Integer previous=counts.putIfAbsent(pack,count);
                if(previous!=null) eq(previous,count,"repeat load reuses pool without accumulating cursor");
                var currentUids=new java.util.HashSet<Integer>();
                for(var record:LevelCheck.records(BehaviorCheck.resource(assets,pack,0))) currentUids.add((int)record.values()[1]);
                for(var entry:oldReferences.entrySet()) if(!currentUids.contains(entry.getKey())) {
                    eq(true,registry.get(entry.getKey())==entry.getValue(),"absent old UID retains pool-array alias");
                    ++retained;
                    if(!Arrays.equals(oldValues.get(entry.getKey()),entry.getValue())) ++mutated;
                }
            }
            compare(a,b,"cross-level pack "+pack);
        }
        eq(true,retained>0,"cross-level scenario exercises stale UID entries");
        eq(true,mutated>0,"cross-level scenario exercises overwritten pool aliases");
        System.out.println("respawn pool lifecycle: "+retained+" retained UID observations, "+mutated+" changed slices");
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),compiled=Path.of(args[1]),stubs=Path.of(args[2]);
        for(String raw:Files.readAllLines(Path.of(args[3]))) {
            String[] p=raw.strip().split("\\s+");
            if(p.length==3&&p[0].equals("field")) BehaviorCheck.names.put(p[1],p[2]);
        }
        for(int pack=3;pack<=11;++pack) {
            try(var a=new ActorCheck.Access(assets,assets,stubs,false);
                var b=new ActorCheck.Access(compiled,assets,stubs,true)) { run(a,b,assets,pack); }
            System.out.println("checkpoint/restart: pack="+pack+" OK");
        }
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);
            var b=new ActorCheck.Access(compiled,assets,stubs,true)) { crossLevels(a,b,assets); }
        System.out.println("checkpoint/restart: OK ("+checks+" expectations, "+LevelCheck.checks+" level comparisons, "+BehaviorCheck.comparisons+" world comparisons; test RMS only)");
    }
}
