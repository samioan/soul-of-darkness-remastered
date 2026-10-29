import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/** Controlled Actor scenarios compared with the original bytecode, without Canvas/drawing. */
public final class ActorCheck {
    static long comparisons;
    static final Class<?>[] NONE = {}, INT = {int.class}, RECTS = {int[].class,int[].class};
    static final String[][] SNAPSHOT = {{"a","fixedX"},{"b","fixedY"},{"K","positionX"},{"L","positionY"},
        {"G","velocityX"},{"H","velocityY"},{"I","accelerationX"},{"J","accelerationY"},
        {"c","flags"},{"f","animationId"},{"g","pendingAnimationId"},{"h","animationFrame"},{"i","animationTick"},
        {"ao","actionState"},{"bB","grounded"},{"bD","ceilingHit"},{"cR","wallOnLeft"},{"cS","wallOnRight"},
        {"j","collisionBounds"},{"k","attackBounds"},{"l","visualBounds"}};
    static final class Access implements AutoCloseable {
        final SpriteCheck.Access sprites;
        final Class<?> actor, game;
        final boolean renamed;
        Access(Path classes,Path assets,Path stubs,boolean renamed) throws Exception {
            this.renamed=renamed;
            sprites=new SpriteCheck.Access(classes,assets,stubs,renamed);
            actor=Class.forName(renamed ? "Actor" : "b",true,sprites.loader);
            game=Class.forName(renamed ? "Game" : "e",true,sprites.loader);
        }
        Field field(Class<?> type,String old,String name) throws Exception {
            Field field=type.getDeclaredField(renamed ? name : old); field.setAccessible(true); return field;
        }
        Object get(Object target,String old,String name) throws Exception { return field(actor,old,name).get(target); }
        void set(Object target,String old,String name,Object value) throws Exception { field(actor,old,name).set(target,value); }
        Object global(String old,String name) throws Exception { return field(game,old,name).get(null); }
        void global(String old,String name,Object value) throws Exception { field(game,old,name).set(null,value); }
        Object call(Object target,String old,String name,Class<?>[] types,Object... args) throws Exception {
            Method method=actor.getDeclaredMethod(renamed ? name : old,types); method.setAccessible(true);
            try { return method.invoke(target,args); }
            catch (InvocationTargetException e) {
                if (e.getCause() instanceof Exception exception) throw exception;
                throw e;
            }
        }
        Object create() throws Exception { return actor.getDeclaredConstructor().newInstance(); }
        void clearWorld() throws Exception {
            // The original reset method also clears player/effects and scripted actor slots.
            call(null,"m","resetWorldActorState",NONE);
            global("x","state",8); global("aG","levelIndex",0); global("g","tickDeltaMs",83);
            global("p","health",60); global("q","maxHealth",60); global("s","magic",60); global("t","maxMagic",60);
            global("dk","soundEnabled",false); global("c","random",new Random(12345));
            global("bt","actorCount",0); global("bz","bz",0);
            global("bv","paintActorCount",0); global("bx","updateActorCount",0);
            global("aS","aS",false); global("au","au",null); global("bo","bo",null);
            global("dL","collisionTiles",new byte[40*20]);
            global("bQ","mapWidthTiles",40); global("bR","mapHeightTiles",20);
            global("bS","levelPixelWidth",800); global("bT","levelPixelHeight",400);
            set(null,"df","worldScrollPixelsPerTick",0);
        }
        void loadAssets(Path assets) throws Exception {
            Object slots=Array.newInstance(sprites.sprite,53);
            byte[] pack=Files.readAllBytes(assets.resolve("2"));
            for (int id=0;id<SpriteCheck.u16(pack,0);++id) {
                int start=SpriteCheck.i32(pack,6+4*id),end=SpriteCheck.i32(pack,10+4*id);
                if (start<end) Array.set(slots,id,sprites.load(Arrays.copyOfRange(pack,start+1,end)));
            }
            global("b","sprites",slots);
        }
        Object player(int x,int y) throws Exception {
            short[] record=new short[20];
            record[0]=0; record[1]=186; record[2]=(short)x; record[3]=(short)y;
            Object player=create();
            call(player,"a","initializeFromRecord",new Class<?>[]{short[].class,boolean.class,int.class,int.class},record,true,0,6);
            global("bk","player",player);
            return player;
        }
        void input(int pressed,int held) throws Exception {
            global("cz","pressedMask",pressed); global("cC","frameHeldMask",held);
        }
        public void close() throws Exception { sprites.close(); }
    }
    static void equal(Object original,Object reference,String context) {
        ++comparisons;
        if (!Objects.deepEquals(original,reference)) throw new AssertionError(context+": "+string(original)+" != "+string(reference));
    }
    static String string(Object object) { return object instanceof int[] a ? Arrays.toString(a) : String.valueOf(object); }
    static void snapshot(Access a,Access b,Object original,Object reference,String context) throws Exception {
        for (String[] names : SNAPSHOT) equal(a.get(original,names[0],names[1]),b.get(reference,names[0],names[1]),context+" "+names[1]);
    }
    static void geometry(Access a,Access b) throws Exception {
        int[][] boxes={{0,0,10,10},{10,0,20,10},{5,5,15,15},{0,0,0,0},{5,0,5,10},
            {-10,-10,0,0},{0,10,10,20},{1,1,9,9},{0,0,10,0}};
        for (int[] first : boxes) for (int[] second : boxes) {
            equal(a.call(null,"a","rectContainsRect",RECTS,first,second),b.call(null,"a","rectContainsRect",RECTS,first,second),"rect containment");
            equal(a.call(null,"b","rectsOverlapInclusive",RECTS,first,second),b.call(null,"b","rectsOverlapInclusive",RECTS,first,second),"inclusive overlap");
            equal(a.call(null,"c","rectsOverlapStrict",RECTS,first,second),b.call(null,"c","rectsOverlapStrict",RECTS,first,second),"strict overlap");
        }
        equal(true,b.call(null,"b","rectsOverlapInclusive",RECTS,boxes[0],boxes[1]),"touching edges inclusive");
        equal(false,b.call(null,"c","rectsOverlapStrict",RECTS,boxes[0],boxes[1]),"touching edges strict");
        Class<?>[] point={int.class,int.class,int[].class};
        for (int x=-1;x<=11;++x) for (int y=-1;y<=11;++y) {
            equal(a.call(null,"a","pointInRect",point,x,y,boxes[0]),b.call(null,"a","pointInRect",point,x,y,boxes[0]),"point bounds");
            equal(x>0 && x<=10 && y>0 && y<=10,b.call(null,"a","pointInRect",point,x,y,boxes[0]),"asymmetric point edges");
        }
        Class<?>[] segments={int.class,int.class,int.class,int.class,int.class,int.class,int.class,int.class};
        int[][] lines={{0,0,10,0},{5,-5,5,5},{20,-5,20,5},{0,0,10,10},{10,0,0,10},{0,0,0,0}};
        for (int[] first : lines) for (int[] second : lines) {
            Object[] args=new Object[8]; for(int i=0;i<4;++i) { args[i]=first[i]; args[i+4]=second[i]; }
            equal(a.call(null,"a","segmentsIntersect",segments,args),b.call(null,"a","segmentsIntersect",segments,args),"segment intersection");
        }
        Class<?>[] ray={int.class,int.class,int.class,int.class,int.class,int.class,int.class,int.class,int[].class};
        int[] original=new int[2],reference=new int[2];
        equal(a.call(null,"a","intersectRaySegment",ray,0,0,10,0,20,-5,20,5,original),
              b.call(null,"a","intersectRaySegment",ray,0,0,10,0,20,-5,20,5,reference),"unbounded ray result");
        equal(new int[]{20,0},reference,"ray extends beyond first segment");
        for (Access access : List.of(a,b)) {
            boolean threw=false;
            try { access.call(null,"a","intersectRaySegment",ray,0,0,10,0,0,0,10,0,new int[2]); }
            catch (ArithmeticException expected) { threw=true; }
            equal(true,threw,"original collinear division by zero preserved");
        }
        for (long value : new long[]{0,1,2,4,9,99,100,101,10000,1234567,2147395600L}) {
            equal(a.call(null,"a","integerSqrt",new Class<?>[]{long.class},value),
                  b.call(null,"a","integerSqrt",new Class<?>[]{long.class},value),"integer square root");
            equal((long)Math.sqrt(value),b.call(null,"a","integerSqrt",new Class<?>[]{long.class},value),"sqrt floor");
        }
    }
    static void integration(Access a,Access b) throws Exception {
        for (int initial : new int[]{-257,-1,0,127,255,256,Integer.MAX_VALUE-100}) {
            a.clearWorld(); b.clearWorld();
            Object original=a.create(),reference=b.create();
            for (Access access : List.of(a,b)) {
                Object actor=access==a ? original : reference;
                access.set(actor,"am","type",-1); // Dispatch default: only the common integrator runs.
                access.set(actor,"a","fixedX",initial); access.set(actor,"b","fixedY",initial);
                access.set(actor,"K","positionX",initial>>8); access.set(actor,"L","positionY",initial>>8);
                access.set(actor,"G","velocityX",129); access.set(actor,"H","velocityY",-321);
                access.set(actor,"I","accelerationX",17); access.set(actor,"J","accelerationY",33);
            }
            for (int frame=0;frame<12;++frame) {
                a.call(original,"s","update",NONE); b.call(reference,"s","update",NONE);
                snapshot(a,b,original,reference,"fixed-point "+initial+"/"+frame);
                if(frame==0) {
                    equal(initial+129,b.get(reference,"a","fixedX"),"position before acceleration");
                    equal(146,b.get(reference,"G","velocityX"),"velocity after acceleration");
                    equal(0,b.get(reference,"I","accelerationX"),"acceleration cleared");
                }
            }
        }
        for (int gate : new int[]{14,32,1024,16384}) {
            a.clearWorld(); b.clearWorld();
            Object original=a.create(),reference=b.create();
            for (Access access : List.of(a,b)) {
                Object actor=access==a ? original : reference;
                access.set(actor,"am","type",-1); access.set(actor,"a","fixedX",2560);
                access.set(actor,"K","positionX",10); access.set(actor,"G","velocityX",512);
                if(gate<100) access.global("x","state",gate);
                else access.set(actor,"c","flags",gate);
                access.call(actor,"s","update",NONE);
            }
            snapshot(a,b,original,reference,"update gate "+gate);
            equal(2560,b.get(reference,"a","fixedX"),"gate prevents movement");
        }
    }
    static void animations(Access a,Access b) throws Exception {
        a.clearWorld(); b.clearWorld();
        Object original=a.create(),reference=b.create();
        for (Access access : List.of(a,b)) {
            Object actor=access==a ? original : reference;
            access.set(actor,"am","type",-1);
            access.set(actor,"p","sprite",Array.get(access.global("b","sprites"),0));
        }
        Object sprite=Array.get(b.global("b","sprites"),0);
        byte[] frameCounts=(byte[])b.sprites.get(sprite,"n","animationFrameCount");
        for(int animation=0;animation<frameCounts.length;++animation) {
            a.call(original,"b","setAnimationImmediate",INT,animation);
            b.call(reference,"b","setAnimationImmediate",INT,animation);
            for(int tick=0;tick<50;++tick) {
                equal(a.call(original,"e","isAnimationEnding",NONE),b.call(reference,"e","isAnimationEnding",NONE),"animation end query");
                a.call(original,"f","advanceAnimation",NONE); b.call(reference,"f","advanceAnimation",NONE);
                snapshot(a,b,original,reference,"animation "+animation+" tick "+tick);
            }
        }
        a.call(original,"b","setAnimationImmediate",INT,0); b.call(reference,"b","setAnimationImmediate",INT,0);
        a.call(original,"a","requestAnimation",INT,1); b.call(reference,"a","requestAnimation",INT,1);
        equal(0,b.get(reference,"f","animationId"),"request waits until animation advance");
        equal(1,b.get(reference,"g","pendingAnimationId"),"request queues next animation");
        a.call(original,"f","advanceAnimation",NONE); b.call(reference,"f","advanceAnimation",NONE);
        equal(1,b.get(reference,"f","animationId"),"queued animation applied");
        equal(0,b.get(reference,"i","animationTick"),"queued animation resets clock");
        snapshot(a,b,original,reference,"queued animation");
    }
    static void slopes(Access a,Access b) throws Exception {
        for (int tile : new int[]{0,2,3,15,16,17,18,19,20,21,24}) for (int local=0;local<20;++local) {
            a.clearWorld(); b.clearWorld();
            Object original=a.player(200+local,206),reference=b.player(200+local,206);
            for (Access access : List.of(a,b)) {
                Object actor=access==a ? original : reference;
                byte[] tiles=(byte[])access.global("dL","collisionTiles");
                for(int x=0;x<40;++x) tiles[10*40+x]=(byte)tile;
                access.set(actor,"H","velocityY",256);
            }
            int correction=(int)a.call(original,"ar","probeVerticalTiles",NONE);
            equal(correction,b.call(reference,"ar","probeVerticalTiles",NONE),"tile vertical correction");
            int height=switch(tile) { case 16 -> local; case 17 -> 20-local;
                case 18 -> local/2; case 19 -> local/2+10; case 20 -> 20-local/2; case 21 -> 10-local/2; default -> 0; };
            equal(tile>=15 ? 7-height : 0,correction,"documented slope height");
            equal(tile>=15,b.get(reference,"bB","grounded"),"tile support threshold");
            snapshot(a,b,original,reference,"slope "+tile+"/"+local);
        }
    }
    static void coreRow(Access a,Object actor,PrintWriter out,String scenario,int tick,int phase) throws Exception {
        if(scenario.equals("melee")) return;
        out.print("P\t"+scenario+"\t"+tick+"\t"+phase);
        for(String[] names:SNAPSHOT) {
            Object value=a.get(actor,names[0],names[1]);
            if(value instanceof int[] rectangle) for(int n:rectangle) out.print("\t"+n);
            else out.print("\t"+value);
        }
        out.println();
    }
    static void player(Access a,Access b,PrintWriter trace,PrintWriter core) throws Exception {
        trace.println("scenario\tframe\tx\ty\tanimation\tanimationFrame\tflags\tvelocityX\tvelocityY\tstate");
        for (String scenario : List.of("walk","jump","crouch","melee","wall","pause","ceiling")) {
            a.clearWorld(); b.clearWorld();
            Object original=a.player(100,199),reference=b.player(100,199);
            for (Access access : List.of(a,b)) {
                byte[] tiles=(byte[])access.global("dL","collisionTiles");
                for (int y=10;y<20;++y) for (int x=0;x<40;++x) tiles[y*40+x]=24;
                if(scenario.equals("wall")) for(int y=0;y<10;++y) tiles[y*40+9]=24;
                if(scenario.equals("ceiling")) for(int x=0;x<40;++x) tiles[3*40+x]=24;
            }
            int minY=199,maxX=100;
            for (int frame=0;frame<100;++frame) {
                int held=0,pressed=0;
                if(scenario.equals("walk") || scenario.equals("wall")) held=frame<60 ? 8256 : 4112;
                if((scenario.equals("jump") || scenario.equals("ceiling")) && frame==4) pressed=16388;
                if(scenario.equals("crouch") && frame>=4 && frame<20) held=33024;
                if(scenario.equals("crouch") && frame==4) pressed=33024;
                if(scenario.equals("melee") && (frame==4 || frame==24 || frame==44)) pressed=65568;
                for (Access access : List.of(a,b)) {
                    Object actor=access==a ? original : reference;
                    access.input(pressed,held); access.global("z","stateFrame",frame);
                    if(scenario.equals("pause")) access.global("x","state",frame<50 ? 14 : 8);
                    access.call(actor,"s","update",NONE);
                }
                snapshot(a,b,original,reference,scenario+" before animation "+frame);
                coreRow(a,original,core,scenario,frame,0);
                trace.println(scenario+"\t"+frame+"\t"+b.get(reference,"K","positionX")+"\t"+b.get(reference,"L","positionY")+
                    "\t"+b.get(reference,"f","animationId")+"\t"+b.get(reference,"h","animationFrame")+"\t"+b.get(reference,"c","flags")+
                    "\t"+b.get(reference,"G","velocityX")+"\t"+b.get(reference,"H","velocityY")+"\t"+b.global("x","state"));
                for (Access access : List.of(a,b)) {
                    Object actor=access==a ? original : reference;
                    // The original advances animations in its paint path, after simulation.
                    if(!(boolean)access.call(actor,"r","isUpdatePaused",NONE)) access.call(actor,"f","advanceAnimation",NONE);
                }
                snapshot(a,b,original,reference,scenario+" frame "+frame);
                coreRow(a,original,core,scenario,frame,1);
                minY=Math.min(minY,(int)b.get(reference,"L","positionY"));
                maxX=Math.max(maxX,(int)b.get(reference,"K","positionX"));
            }
            if(scenario.equals("jump")) equal(true,minY<170,"jump must move upward");
            if(scenario.equals("walk")) equal(true,maxX>200,"walking must move horizontally");
            if(scenario.equals("wall")) equal(true,maxX<180,"solid wall must block movement");
            if(scenario.equals("ceiling")) equal(true,minY>118 && minY<199,"ceiling must limit jump height");
            System.out.println("actor scenario: "+scenario+" OK (minY="+minY+", maxX="+maxX+")");
        }
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),compiled=Path.of(args[1]),stubs=Path.of(args[2]);
        Path tracePath=assets.getParent().resolve("analysis/actor-trace.tsv");
        Files.createDirectories(tracePath.getParent());
        try (Access original=new Access(assets,assets,stubs,false); Access reference=new Access(compiled,assets,stubs,true);
             PrintWriter trace=new PrintWriter(Files.newBufferedWriter(tracePath));
             PrintWriter core=new PrintWriter(Files.newBufferedWriter(tracePath.resolveSibling("player-core-oracle.tsv")))) {
            geometry(original,reference);
            integration(original,reference);
            original.loadAssets(assets); reference.loadAssets(assets);
            animations(original,reference);
            slopes(original,reference);
            player(original,reference,trace,core);
            if(trace.checkError()) throw new AssertionError("Cannot write Actor trace");
        }
        System.out.println("actor behavior: OK ("+comparisons+" comparisons; controlled core scenarios, not a level play-through)");
    }
}
