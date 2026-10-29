import java.io.ByteArrayInputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Shipped level construction in isolated worlds; no ticks, drawing or RMS. */
public final class LevelCheck {
    record Record(int offset,short[] values) {}
    static int checks;
    static void eq(Object expected,Object actual,String label) {
        ++checks;
        if(!java.util.Objects.deepEquals(expected,actual))
            throw new AssertionError(label+": "+expected+" != "+actual);
    }
    static List<Record> records(byte[] bytes) {
        List<Record> records=new ArrayList<>();
        for(int pos=0;pos<bytes.length;) {
            int count=bytes[pos++]&255,offset=pos;
            short[] row=new short[count];
            for(int i=0;i<count;++i) { row[i]=(short)SpriteCheck.u16(bytes,pos); pos+=2; }
            records.add(new Record(offset,row));
        }
        return records;
    }
    static void setup(ActorCheck.Access a,Path assets,int pack) throws Exception {
        BehaviorCheck.commonAssets(a,assets); a.clearWorld();
        a.global("aG","levelIndex",pack-3);
        byte[] packed=Files.readAllBytes(assets.resolve(Integer.toString(pack)));
        int count=SpriteCheck.u16(packed,0); int[] offsets=new int[count+1];
        for(int i=0;i<offsets.length;++i) offsets[i]=SpriteCheck.i32(packed,6+4*i);
        a.global("dR","packPath","/"+pack); a.global("dS","packStream",new ByteArrayInputStream(packed));
        a.global("dT","packPosition",0); a.global("dU","packResourceCount",(short)count);
        a.global("dW","packPartCount",(short)1); a.global("dX","packPartFirstResource",new short[]{0});
        a.global("dY","packPartIndex",0); a.global("dV","packOffsets",offsets);
        ScriptCheck.call(a,"b","loadTileMaps",new Class<?>[]{int.class,boolean.class},pack-3,true);
        a.global("bF","bF",BehaviorCheck.resource(assets,pack,0));
        a.global("bH","bH",new short[30]); a.global("bI","bI",0);
    }
    static void compare(ActorCheck.Access a,ActorCheck.Access b,String label) throws Exception {
        var fields=Arrays.stream(a.actor.getDeclaredFields()).filter(f->!Modifier.isStatic(f.getModifiers())).toList();
        BehaviorCheck.compareWorld(a,b,null,null,fields,label);
        for(String old:new String[]{"bm","bn","bo"})
            eq(BehaviorCheck.actorState(a,a.global(old,old),fields),BehaviorCheck.actorState(b,b.global(old,old),fields),label+" dedicated "+old);
        for(String old:new String[]{"bm","bn","bo","bE","bD","bI","bH","bz","dL"}) {
            String name=BehaviorCheck.names.getOrDefault("e."+old,old);
            eq(BehaviorCheck.normalize(a,a.global(old,name)),BehaviorCheck.normalize(b,b.global(old,name)),label+" "+old);
        }
    }
    static void expected(ActorCheck.Access a,List<Record> records,boolean restore,boolean markAll,int removedUid) throws Exception {
        int ordinary=0,index=0,pairs=0,spawnCount=0;
        byte[] persistence=new byte[250];
        if(restore&&markAll) Arrays.fill(persistence,(byte)-99);
        List<Object> actors=new ArrayList<>();
        for(int i=0;i<(int)a.global("bt","actorCount");++i) actors.add(Array.get(a.global("bs","actors"),i));
        for(Record record:records) {
            int type=record.values[0],uid=record.values[1];
            boolean skip=uid==removedUid || restore&&markAll&&(type==35||type==23||type==48||type==82);
            if(skip) { ++index; continue; }
            Object actor;
            if(type==0) actor=a.global("bk","player");
            else if(type==69) actor=a.global(pairs++==0?"bm":"bn",pairs==1?"bm":"bn");
            else actor=actors.get(ordinary++);
            eq(uid,a.get(actor,"al","uid"),"record UID"); eq(type,a.get(actor,"am","type"),"record type");
            if(type!=0) {
                eq(type==69?0:index,a.get(actor,"ae","persistenceIndex"),"serialized persistence index");
                persistence[index]=0;
                ++index;
            }
            if((boolean)a.call(actor,"X","isRespawnable",new Class<?>[]{})) {
                var registry=(java.util.Map<?,?>)a.get(null,"af","spawnRecordsByUid");
                eq(new int[]{record.offset,record.values.length},registry.get(uid),"respawn source slice");
                ++spawnCount;
            }
        }
        eq(ordinary,actors.size(),"ordinary actor count from records");
        eq(spawnCount,a.get(null,"ah","spawnRecordCount"),"respawn registry count");
        short[] scratch=new short[20];
        for(Record record:records) System.arraycopy(record.values,0,scratch,0,record.values.length);
        eq(scratch,a.global("bE","bE"),"shared parameter buffer retains short-record tail");
        eq(persistence,a.global("bD","bD"),"restart persistence clears only reconstructed entries");
    }
    static void linked(ActorCheck.Access a,ActorCheck.Access b,Path assets) throws Exception {
        List<Record> rows=records(BehaviorCheck.resource(assets,8,0));
        int[] uids=rows.stream().filter(r->r.values[0]!=0&&r.values[0]!=69).limit(2).mapToInt(r->r.values[1]).toArray();
        for(var access:List.of(a,b)) {
            setup(access,assets,8);
            byte[] save=new byte[1313];
            for(int slot=0;slot<3;++slot) SaveCheck.put16(save,664+slot*2,slot<2?uids[slot]:-1);
            SaveCheck.put16(save,731,-1); // No camera-controller restore in this fixture.
            byte[] snapshots=new byte[54];
            access.set(null,"by","by",snapshots);
            for(int slot=0;slot<2;++slot) {
                Object source=access.create();
                access.set(source,"f","animationId",slot+1); access.set(source,"h","animationFrame",slot+2);
                access.set(source,"K","positionX",123+slot); access.set(source,"L","positionY",456+slot);
                access.set(source,"ao","actionState",100+slot); access.set(source,"c","flags",16);
                access.set(source,"aw","aw",9876+slot);
                ScriptCheck.call(access,"a","storeLinkedActorSnapshot",new Class<?>[]{access.actor,int.class},source,slot);
            }
            System.arraycopy(snapshots,0,save,677,54);
            access.global("cw","saveBuffer",save);
            Object stale=access.create(); Array.set(access.get(null,"cq","cq"),2,stale);
            ScriptCheck.call(access,"d","reconstructLevelActors",new Class<?>[]{boolean.class},true);
            Object links=access.get(null,"cq","cq");
            for(int slot=0;slot<2;++slot) {
                Object actor=Array.get(links,slot);
                eq(uids[slot],access.get(actor,"al","uid"),"relinked UID");
                Object found=ScriptCheck.call(access,"g","findActorByUid",new Class<?>[]{int.class},uids[slot]);
                eq(true,actor==found,"relinked actor is constructed world member");
                eq(123+slot,access.get(actor,"K","positionX"),"linked snapshot X");
                eq(456+slot,access.get(actor,"L","positionY"),"linked snapshot Y");
                eq(slot+1,access.get(actor,"f","animationId"),"linked snapshot animation");
                eq(slot+2,access.get(actor,"h","animationFrame"),"linked snapshot frame");
                eq(100+slot,access.get(actor,"ao","actionState"),"linked snapshot state");
                eq(16,access.get(actor,"c","flags"),"linked snapshot flags");
                eq(9876+slot,access.get(actor,"aw","aw"),"linked snapshot scratch");
            }
            eq(null,Array.get(links,2),"absent saved UID clears stale linked slot");
        }
        compare(a,b,"level 5 linked reconstruction");
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),compiled=Path.of(args[1]),stubs=Path.of(args[2]);
        for(String raw:Files.readAllLines(Path.of(args[3]))) {
            String[] p=raw.strip().split("\\s+");
            if(p.length==3&&p[0].equals("field")) BehaviorCheck.names.put(p[1],p[2]);
        }
        for(int pack=3;pack<=11;++pack) {
            List<Record> rows=records(BehaviorCheck.resource(assets,pack,0));
            for(int mode=0;mode<3;++mode) {
                boolean restore=mode!=0,markAll=mode==1;
                int removedUid=mode==2?rows.stream().filter(r->r.values[0]!=0&&r.values[0]!=69).findFirst().orElseThrow().values[1]:-1;
                try(var a=new ActorCheck.Access(assets,assets,stubs,false);
                    var b=new ActorCheck.Access(compiled,assets,stubs,true)) {
                    for(var access:List.of(a,b)) {
                        setup(access,assets,pack);
                        Arrays.fill((byte[])access.global("bD","bD"),mode==2?(byte)0:(byte)-99);
                        if(mode==2) ((short[])access.global("bH","bH"))[0]=(short)removedUid;
                        ScriptCheck.call(access,"c","constructLevelActors",new Class<?>[]{boolean.class},restore);
                        expected(access,rows,restore,markAll,removedUid);
                    }
                    compare(a,b,"pack "+pack+" mode="+mode);
                    System.out.println("level construction: pack="+pack+" mode="+mode+" OK");
                }
            }
        }
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);
            var b=new ActorCheck.Access(compiled,assets,stubs,true)) { linked(a,b,assets); }
        System.out.println("level construction: OK ("+checks+" expectations, "+BehaviorCheck.comparisons+" world comparisons; no gameplay ticks)");
    }
}
