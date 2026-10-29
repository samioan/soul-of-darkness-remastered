import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/** Selected enemy/object scenarios with real parameters/stats and controlled placement. */
public final class BehaviorCheck {
    static long comparisons;
    static Map<String,String> names = new TreeMap<>();
    static final Class<?>[] NONE = {}, INT = {int.class};
    record Example(int pack,short[] values) {}
    static byte[] resource(Path assets,int pack,int id) throws Exception {
        byte[] data=Files.readAllBytes(assets.resolve(Integer.toString(pack)));
        int start=SpriteCheck.i32(data,6+4*id),end=SpriteCheck.i32(data,10+4*id);
        if(start==end) return null;
        return Arrays.copyOfRange(data,start+1,end);
    }
    static Map<Integer,Example> examples(Path assets) throws Exception {
        Map<Integer,Example> records=new LinkedHashMap<>();
        for(int pack=3;pack<=11;++pack) {
            byte[] data=resource(assets,pack,0);
            for(int pos=0;pos<data.length;) {
                int count=data[pos++]&255;
                short[] row=new short[count];
                for(int i=0;i<count;++i) { row[i]=(short)SpriteCheck.u16(data,pos); pos+=2; }
                records.putIfAbsent((int)row[0],new Example(pack,row));
            }
        }
        return records;
    }
    static void gameCall(ActorCheck.Access access,String old,String name,Class<?>[] types,Object... args) throws Exception {
        var method=access.game.getDeclaredMethod(access.renamed ? name : old,types);
        method.setAccessible(true); method.invoke(null,args);
    }
    static void commonAssets(ActorCheck.Access access,Path assets) throws Exception {
        access.loadAssets(assets);
        access.global("a","font",access.sprites.load(resource(assets,1,0)));
        for(int table=0;table<11;++table) {
            byte[] bytes=resource(assets,15,table);
            if(bytes!=null) gameCall(access,"a","decodeStringTable",new Class<?>[]{int.class,byte[].class},table,bytes);
        }
        try(DataInputStream data=new DataInputStream(new ByteArrayInputStream(resource(assets,18,0)))) {
            int[][] stats=new int[data.readInt()][];
            for(int row=0;row<stats.length;++row) {
                stats[row]=new int[data.readInt()];
                for(int i=0;i<stats[row].length;++i) stats[row][i]=data.readInt();
            }
            if(data.available()!=0) throw new AssertionError("stat table trailing data");
            access.global("bY","actorStatTables",stats);
        }
        byte[] bytes=resource(assets,17,0);
        int[] trig=new int[256];
        for(int i=0;i<trig.length;++i) trig[i]=SpriteCheck.i32(bytes,i*4);
        access.global("bX","trigTable256",trig);
    }
    static Object fixture(ActorCheck.Access access,Example example) throws Exception {
        access.clearWorld();
        access.global("aa","viewBounds",new int[]{0,0,240,320});
        access.global("L","cameraX",0); access.global("M","cameraY",0);
        Object player=access.player(100,199);
        Object region=access.create();
        access.set(region,"am","type",22); access.set(region,"al","uid",-999);
        access.set(region,"m","customBounds",new int[]{0,0,240,320});
        access.call(region,"g","refreshBounds",NONE); access.global("au","au",region);
        byte[] tiles=(byte[])access.global("dL","collisionTiles");
        for(int y=10;y<20;++y) for(int x=0;x<40;++x) tiles[y*40+x]=24;
        short[] row=example.values.clone();
        row[2]=180; row[3]=199; // Explicitly controlled arena placement; parameters otherwise unchanged.
        byte[] stream=new byte[1+2*row.length]; stream[0]=(byte)row.length;
        for(int i=0;i<row.length;++i) { stream[1+2*i]=(byte)row[i]; stream[2+2*i]=(byte)(row[i]>>>8); }
        access.global("bF","bF",stream);
        Object target=access.create();
        access.call(target,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},
            Arrays.copyOf(row,20),true,1,row.length);
        gameCall(access,"b","addActor",new Class<?>[]{access.actor},target);
        access.set(target,"ae","persistenceIndex",0);
        // Base item effects can open tutorial overlays; load the font/text but do not render them.
        access.call(player,"g","refreshBounds",NONE);
        return target;
    }
    static Object normalize(ActorCheck.Access access,Object value) throws Exception {
        if(value==null || value instanceof Number || value instanceof Boolean || value instanceof String) return value;
        if(access.actor.isInstance(value)) {
            if(value==access.global("bk","player")) return "player";
            if(value==access.global("au","au")) return "region";
            Object actors=access.global("bs","actors");
            for(int i=0;i<(int)access.global("bt","actorCount");++i) if(Array.get(actors,i)==value) return "actor["+i+"]";
            return "unlisted:"+access.get(value,"al","uid")+":"+access.get(value,"am","type")+":"+access.get(value,"ao","actionState");
        }
        if(access.sprites.sprite.isInstance(value)) {
            Object sprites=access.global("b","sprites");
            for(int i=0;i<Array.getLength(sprites);++i) if(Array.get(sprites,i)==value) return "sprite["+i+"]";
            return "other-sprite";
        }
        if(value.getClass().isArray()) {
            List<Object> list=new ArrayList<>();
            for(int i=0;i<Array.getLength(value);++i) list.add(normalize(access,Array.get(value,i)));
            return list;
        }
        if(value instanceof Map<?,?> map) {
            Map<String,Object> out=new TreeMap<>();
            for(var entry:map.entrySet()) out.put(entry.getKey().toString(),normalize(access,entry.getValue()));
            return out;
        }
        throw new AssertionError("Uncompared fixture object: "+value.getClass());
    }
    static Object actorState(ActorCheck.Access access,Object actor,List<Field> fields) throws Exception {
        if(actor==null) return null;
        Map<String,Object> state=new TreeMap<>();
        for(Field original:fields) {
            String old=original.getName(),name=names.getOrDefault("b."+old,old);
            state.put(old,normalize(access,access.get(actor,old,name)));
        }
        return state;
    }
    static void equal(Object a,Object b,String context) {
        ++comparisons;
        if(!Objects.equals(a,b)) throw new AssertionError(context+"\noriginal="+a+"\nreference="+b);
    }
    static void compareWorld(ActorCheck.Access a,ActorCheck.Access b,Object rootA,Object rootB,List<Field> fields,String context) throws Exception {
        equal(actorState(a,rootA,fields),actorState(b,rootB,fields),context+" root");
        equal(actorState(a,a.global("bk","player"),fields),actorState(b,b.global("bk","player"),fields),context+" player");
        int count=(int)a.global("bt","actorCount");
        equal(count,b.global("bt","actorCount"),context+" actor count");
        for(int i=0;i<count;++i)
            equal(actorState(a,Array.get(a.global("bs","actors"),i),fields),
                  actorState(b,Array.get(b.global("bs","actors"),i),fields),context+" actor "+i);
        for(Field field:a.actor.getDeclaredFields()) if(Modifier.isStatic(field.getModifiers())) {
            String old=field.getName(),name=names.getOrDefault("b."+old,old);
            equal(normalize(a,a.get(null,old,name)),normalize(b,b.get(null,old,name)),context+" static Actor."+old);
        }
        for(String old:new String[]{"p","q","s","t","x","aS","bc","aI","bD","cw","aH","aG","L","M","E","aP","az"}) {
            String name=names.getOrDefault("e."+old,old);
            // az is a rollback array; it and save bytes capture non-rendering side effects.
            equal(normalize(a,a.global(old,name)),normalize(b,b.global(old,name)),context+" Game."+old);
        }
    }
    static void step(ActorCheck.Access access,Object root,int type,int tick) throws Exception {
        access.input(type==52 && tick==5 ? 65568 : 0,0);
        access.global("z","stateFrame",tick);
        if((type==52 || type==10 || type==13 || type==35) && tick==3) {
            Object player=access.global("bk","player");
            access.set(player,"K","positionX",180); access.call(player,"g","refreshBounds",NONE);
        }
        if(type==45 && tick==3) access.call(root,"n","changeActionState",INT,100);
        if(type==75 && tick==3) {
            Object player=access.global("bk","player");
            access.set(player,"k","attackBounds",((int[])access.get(root,"j","collisionBounds")).clone());
        }
        // Snapshot the update queue: new actors wait until the following step.
        // The player is deliberately stationary in these controlled AI fixtures.
        int count=(int)access.global("bt","actorCount");
        List<Object> queue=new ArrayList<>();
        for(int i=0;i<count;++i) {
            Object actor=Array.get(access.global("bs","actors"),i);
            if(actor!=null) queue.add(actor);
        }
        Object updateQueue=Array.newInstance(access.actor,Math.max(70,queue.size()));
        for(int i=0;i<queue.size();++i) { Array.set(updateQueue,i,queue.get(i)); access.set(queue.get(i),"aq","updatedThisTick",false); }
        access.global("bw","updateActors",updateQueue); access.global("bx","updateActorCount",queue.size());
        for(Object actor:queue) access.call(actor,"s","update",NONE);
        for(int i=0;i<(int)access.global("bt","actorCount");++i) {
            Object actor=Array.get(access.global("bs","actors"),i);
            if(actor!=null) {
                access.call(actor,"g","refreshBounds",NONE);
                if(!(boolean)access.call(actor,"r","isUpdatePaused",NONE)) access.call(actor,"f","advanceAnimation",NONE);
            }
        }
    }
    static void pickupOracle(Path assets,Path compiled,Path stubs,Example example) throws Exception {
        Path output=assets.getParent().resolve("analysis/pickup-oracle.tsv");
        try(var out=new java.io.PrintWriter(Files.newBufferedWriter(output))) {
            for(String scenario:new String[]{"health","magic","cap","far","moving","paused","near","dropped","frozen"}) {
                try(var a=new ActorCheck.Access(assets,assets,stubs,false);
                    var b=new ActorCheck.Access(compiled,assets,stubs,true)) {
                    commonAssets(a,assets); commonAssets(b,assets);
                    short[] params=example.values.clone(); params[4]=(short)(scenario.equals("magic")?2:0);
                    Object first=fixture(a,new Example(example.pack,params)),second=fixture(b,new Example(example.pack,params));
                    for(var access:List.of(a,b)) {
                        access.set(null,"bm","bm",true); access.set(null,"bn","bn",true); // Tutorial already seen.
                        access.global("p","health",scenario.equals("cap")?59:30); access.global("s","magic",30);
                    }
                    for(var access:List.of(a,b)) {
                        Object root=access==a?first:second;
                        if(scenario.equals("near") || scenario.equals("moving")) {
                            access.set(root,"K","positionX",120); access.set(root,"L","positionY",180);
                            access.set(root,"a","fixedX",120<<8); access.set(root,"b","fixedY",180<<8);
                            access.call(root,"g","refreshBounds",NONE);
                        }
                        if(scenario.equals("dropped")) access.set(root,"al","uid",-1);
                        if(scenario.equals("frozen")) access.set(root,"c","flags",64);
                    }
                    List<Field> fields=Arrays.stream(a.actor.getDeclaredFields()).filter(f->!Modifier.isStatic(f.getModifiers())).toList();
                    for(int frame=0;frame<80;++frame) {
                        for(var access:List.of(a,b)) {
                            Object root=access==a?first:second,player=access.global("bk","player");
                            if(!scenario.equals("far") && frame==3) {
                                access.set(player,"K","positionX",180); access.call(player,"g","refreshBounds",NONE);
                            }
                            if(scenario.equals("moving")) access.set(player,"G","velocityX",1280);
                            access.global("x","state",scenario.equals("paused") && frame<10?14:8);
                            access.call(root,"s","update",NONE);
                            boolean present=false;
                            for(int i=0;i<(int)access.global("bt","actorCount");++i) if(Array.get(access.global("bs","actors"),i)==root) present=true;
                            if(present) {
                                access.call(root,"g","refreshBounds",NONE);
                                if(!(boolean)access.call(root,"r","isUpdatePaused",NONE)) access.call(root,"f","advanceAnimation",NONE);
                            }
                        }
                        compareWorld(a,b,first,second,fields,"pickup "+scenario+"/"+frame);
                        boolean present=false;
                        for(int i=0;i<(int)a.global("bt","actorCount");++i) if(Array.get(a.global("bs","actors"),i)==first) present=true;
                        out.print("K\t"+scenario+"\t"+frame);
                        for(String old:new String[]{"a","b","K","L","G","H","c","f","h","i","ao","ax","dd"})
                            out.print("\t"+a.get(first,old,names.getOrDefault("b."+old,old)));
                        out.print("\t"+(present?1:0));
                        int[] collision=present?(int[])a.get(first,"j","collisionBounds"):new int[4];
                        for(int n:collision) out.print("\t"+n);
                        out.print("\t"+a.global("p","health")+"\t"+a.global("s","magic"));
                        out.println(); if(!present) break;
                    }
                }
            }
        }
        System.out.println("native pickup oracle: "+output);
    }
    static void spawnedPickupOracle(Path assets,Path compiled,Path stubs,java.io.PrintWriter out) throws Exception {
        for(String scenario:new String[]{"health_drop","magic_drop","health_crystal","magic_crystal","expired"}) {
            try(var a=new ActorCheck.Access(assets,assets,stubs,false);
                var b=new ActorCheck.Access(compiled,assets,stubs,true)) {
                commonAssets(a,assets); commonAssets(b,assets);
                List<Object> roots=new ArrayList<>();
                int animation=switch(scenario) { case "magic_drop" -> 2; case "health_crystal" -> 1; case "magic_crystal" -> 3; default -> 0; };
                for(var access:List.of(a,b)) {
                    access.clearWorld(); access.player(scenario.equals("expired")?600:100,199);
                    access.global("p","health",30); access.global("s","magic",30);
                    access.set(null,"bm","bm",true); access.set(null,"bn","bn",true);
                    Object source=access.create(); access.set(source,"K","positionX",180); access.set(source,"L","positionY",199);
                    access.set(source,"ak","facingLeft",true);
                    Object root=access.call(source,"a","spawnActor",new Class<?>[]{int.class,int.class,int.class,int.class,int.class,int.class,boolean.class},35,33,animation,0,0,308,false);
                    access.set(root,"ak","facingLeft",false); roots.add(root);
                }
                Object first=roots.get(0),second=roots.get(1);
                List<Field> fields=Arrays.stream(a.actor.getDeclaredFields()).filter(f->!Modifier.isStatic(f.getModifiers())).toList();
                for(int frame=0;frame<120;++frame) {
                    boolean present=true;
                    for(var access:List.of(a,b)) {
                        Object root=access==a?first:second;
                        access.call(root,"s","update",NONE);
                        present=false;
                        for(int i=0;i<(int)access.global("bt","actorCount");++i) if(Array.get(access.global("bs","actors"),i)==root) present=true;
                        if(present) { access.call(root,"g","refreshBounds",NONE); access.call(root,"f","advanceAnimation",NONE); }
                    }
                    compareWorld(a,b,first,second,fields,"spawned pickup "+scenario+"/"+frame);
                    out.print("S\t"+scenario+"\t"+frame);
                    for(String old:new String[]{"a","b","K","L","G","H","c","f","h","i","ao","ax","dd"})
                        out.print("\t"+a.get(first,old,names.getOrDefault("b."+old,old)));
                    out.print("\t"+(present?1:0));
                    int[] collision=present?(int[])a.get(first,"j","collisionBounds"):new int[4];
                    for(int n:collision) out.print("\t"+n);
                    out.print("\t"+a.get(first,"au","au")+"\t"+a.global("p","health")+"\t"+a.global("s","magic"));
                    Object player=a.global("bk","player");
                    for(String old:new String[]{"g","G","H"}) out.print("\t"+a.get(player,old,names.getOrDefault("b."+old,old)));
                    out.println();
                    if(!present || (int)a.get(first,"ao","actionState")==103) break;
                }
            }
        }
    }
    static void containerOracle(Path assets,Path compiled,Path stubs,Example example) throws Exception {
        Path output=assets.getParent().resolve("analysis/container-oracle.tsv");
        try(var out=new java.io.PrintWriter(Files.newBufferedWriter(output))) {
            for(String scenario:new String[]{"closed","open","leave","magic","fall","paused","early","locked"}) {
                try(var a=new ActorCheck.Access(assets,assets,stubs,false);
                    var b=new ActorCheck.Access(compiled,assets,stubs,true)) {
                    commonAssets(a,assets); commonAssets(b,assets);
                    short[] params=example.values.clone(); if(scenario.equals("magic")) params[6]=1;
                    if(scenario.equals("locked")) params[5]=2216;
                    Object first=fixture(a,new Example(example.pack,params)),second=fixture(b,new Example(example.pack,params));
                    for(var access:List.of(a,b)) access.global("bH","bH",new short[30]);
                    if(scenario.equals("fall")) for(var access:List.of(a,b)) {
                        Object root=access==a?first:second;
                        access.set(root,"L","positionY",119); access.set(root,"b","fixedY",119<<8);
                        access.call(root,"g","refreshBounds",NONE);
                    }
                    List<Field> fields=Arrays.stream(a.actor.getDeclaredFields()).filter(f->!Modifier.isStatic(f.getModifiers())).toList();
                    for(int frame=0;frame<100;++frame) {
                        List<String> events=new ArrayList<>(); boolean present=true;
                        for(var access:List.of(a,b)) {
                            Object root=access==a?first:second,player=access.global("bk","player");
                            if(!scenario.equals("closed") && !scenario.equals("fall") && frame==3) access.set(player,"K","positionX",180);
                            if((scenario.equals("leave") && frame==4) || frame==6) access.set(player,"K","positionX",100);
                            access.call(player,"g","refreshBounds",NONE);
                            access.input(frame==(scenario.equals("early")?3:5)?65536:0,0);
                            boolean paused=scenario.equals("paused") && frame<10;
                            access.global("x","state",paused?14:8);
                            Set<Object> before=new java.util.HashSet<>();
                            for(int i=0;i<(int)access.global("bt","actorCount");++i) before.add(Array.get(access.global("bs","actors"),i));
                            if(!scenario.equals("locked")) access.call(root,"s","update",NONE);
                            for(int i=0;i<(int)access.global("bt","actorCount");++i) {
                                Object child=Array.get(access.global("bs","actors"),i);
                                if(child!=null && !before.contains(child) && (int)access.get(child,"am","type")==35 && access==a) {
                                    String event="D\t"+scenario+"\t"+frame;
                                    for(String old:new String[]{"f","K","L","c"}) event+="\t"+access.get(child,old,names.getOrDefault("b."+old,old));
                                    events.add(event);
                                }
                            }
                            present=false;
                            for(int i=0;i<(int)access.global("bt","actorCount");++i) if(Array.get(access.global("bs","actors"),i)==root) present=true;
                            if(present && !scenario.equals("locked")) {
                                access.call(root,"g","refreshBounds",NONE);
                                if(!paused) access.call(root,"f","advanceAnimation",NONE);
                            }
                        }
                        compareWorld(a,b,first,second,fields,"container "+scenario+"/"+frame);
                        out.print("C\t"+scenario+"\t"+frame);
                        for(String old:new String[]{"a","b","K","L","G","H","I","J","c","f","g","h","i","ao","au","aw"})
                            out.print("\t"+a.get(first,old,names.getOrDefault("b."+old,old)));
                        out.print("\t"+((boolean)a.get(first,"bB","grounded")?1:0));
                        out.print("\t"+(a.get(first,"w","w")!=null?1:0)+"\t"+(present?1:0));
                        int[] collision=present?(int[])a.get(first,"j","collisionBounds"):new int[4];
                        for(int n:collision) out.print("\t"+n); out.println();
                        for(String event:events) out.println(event);
                        if(!present) break;
                    }
                }
            }
            spawnedPickupOracle(assets,compiled,stubs,out);
        }
        System.out.println("native container oracle: "+output);
    }
    static void effectOracle(Path assets,Path compiled,Path stubs) throws Exception {
        Path output=assets.getParent().resolve("analysis/effect-oracle.tsv");
        int rows=0;
        try(var out=new java.io.PrintWriter(Files.newBufferedWriter(output))) {
            for(int source=0;source<4;++source) for(int left=0;left<2;++left)
                for(String scenario:new String[]{"normal","paused","frozen"}) {
                    try(var a=new ActorCheck.Access(assets,assets,stubs,false);
                        var b=new ActorCheck.Access(compiled,assets,stubs,true)) {
                        List<Object> effects=new ArrayList<>();
                        for(var access:List.of(a,b)) {
                            commonAssets(access,assets); access.clearWorld(); access.player(100,199);
                            Object item=access.create();
                            access.set(item,"K","positionX",100); access.set(item,"L","positionY",199);
                            access.set(item,"f","animationId",source); access.set(item,"ak","facingLeft",left!=0);
                            access.call(item,"ad","ad",NONE);
                            Object effect=access.get(null,"aL","lastSpawnedActor");
                            if(scenario.equals("frozen")) access.set(effect,"c","flags",left|64);
                            effects.add(effect);
                        }
                        List<Field> fields=Arrays.stream(a.actor.getDeclaredFields()).filter(f->!Modifier.isStatic(f.getModifiers())).toList();
                        for(int frame=-1;frame<40;++frame) {
                            boolean present=true;
                            for(var access:List.of(a,b)) {
                                Object effect=effects.get(access==a?0:1);
                                boolean paused=scenario.equals("paused") && frame>=0 && frame<5;
                                access.global("x","state",paused?32:8);
                                if(frame>=0) access.call(effect,"s","update",NONE);
                                present=false;
                                for(int i=0;i<(int)access.global("bt","actorCount");++i)
                                    if(Array.get(access.global("bs","actors"),i)==effect) present=true;
                                if(present && !paused) access.call(effect,"f","advanceAnimation",NONE);
                            }
                            Object first=effects.get(0);
                            compareWorld(a,b,first,effects.get(1),fields,"effect "+source+"/"+left+"/"+scenario+"/"+frame);
                            out.print("E\t"+source+"\t"+left+"\t"+scenario+"\t"+frame);
                            for(String old:new String[]{"a","b","K","L","c","f","h","i","aw"})
                                out.print("\t"+a.get(first,old,names.getOrDefault("b."+old,old)));
                            int step=(int)a.get(first,"h","animationFrame");
                            Object sprite=a.get(first,"p","sprite");
                            // Same paint formula, evaluated without invoking mutable Actor.paint.
                            var countMethod=a.sprites.sprite.getDeclaredMethod("a",int.class); countMethod.setAccessible(true);
                            int count=(int)countMethod.invoke(sprite,11+(source>>1));
                            out.println("\t"+(present?1:0)+"\t"+(step*30/count)); ++rows;
                            if(!present) break;
                        }
                    }
                }
        }
        System.out.println("native collection effect oracle: "+rows+" rows, "+output);
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),compiled=Path.of(args[1]),stubs=Path.of(args[2]);
        for(String raw:Files.readAllLines(Path.of(args[3]))) {
            String[] parts=raw.strip().split("\\s+");
            if(parts.length==3 && parts[0].equals("field")) names.put(parts[1],parts[2]);
        }
        Map<Integer,Example> examples=examples(assets);
        if(args.length==5 && args[4].equals("--effects-only")) { effectOracle(assets,compiled,stubs); return; }
        if(args.length==5 && args[4].equals("--containers-only")) { containerOracle(assets,compiled,stubs,examples.get(52)); return; }
        int cases=0;
        for(int type:new int[]{1,2,3,10,13,14,70,35,52,37,45,64,75,89,90,100}) {
            try(ActorCheck.Access a=new ActorCheck.Access(assets,assets,stubs,false);
                ActorCheck.Access b=new ActorCheck.Access(compiled,assets,stubs,true)) {
                commonAssets(a,assets); commonAssets(b,assets);
                Object rootA=fixture(a,examples.get(type)),rootB=fixture(b,examples.get(type));
                List<Field> fields=Arrays.stream(a.actor.getDeclaredFields()).filter(f->!Modifier.isStatic(f.getModifiers())).toList();
                compareWorld(a,b,rootA,rootB,fields,"type "+type+" initialized");
                Set<Integer> states=new TreeSet<>();
                int ticks=(type==90 || type==100) ? 45 : 160;
                for(int tick=0;tick<ticks;++tick) {
                    try { step(a,rootA,type,tick); step(b,rootB,type,tick); }
                    catch(Exception e) { throw new Exception("fixture type="+type+" tick="+tick,e); }
                    compareWorld(a,b,rootA,rootB,fields,"type "+type+" tick "+tick);
                    states.add((int)a.get(rootA,"ao","actionState"));
                    boolean present=false;
                    for(int i=0;i<(int)a.global("bt","actorCount");++i) if(Array.get(a.global("bs","actors"),i)==rootA) present=true;
                    if(!present || ((int)a.get(rootA,"c","flags")&1024)!=0) break;
                }
                equal(((java.util.Random)a.global("c","random")).nextLong(),((java.util.Random)b.global("c","random")).nextLong(),"random stream");
                int[] required=switch(type) {
                    case 1,14 -> new int[]{101,102,103};
                    case 2 -> new int[]{101,102,103,200};
                    case 3 -> new int[]{105,106,107};
                    case 70 -> new int[]{105,109};
                    case 52 -> new int[]{101,102,106};
                    case 37 -> new int[]{101,102};
                    case 35 -> new int[]{100,101};
                    case 45 -> new int[]{100,101};
                    case 75 -> new int[]{102,104,106};
                    case 90 -> new int[]{105,106,107,112};
                    case 100 -> new int[]{100,101,102};
                    default -> new int[0];
                };
                for(int expected:required) if(!states.contains(expected))
                    throw new AssertionError("type "+type+" did not reach expected state "+expected+": "+states);
                ++cases;
                System.out.println("behavior fixture: type="+type+" states="+states+" OK");
            }
        }
        pickupOracle(assets,compiled,stubs,examples.get(35));
        containerOracle(assets,compiled,stubs,examples.get(52));
        effectOracle(assets,compiled,stubs);
        System.out.println("enemy/object behavior: OK ("+cases+" fixtures, "+comparisons+" state/world comparisons; controlled arenas)");
    }
}
