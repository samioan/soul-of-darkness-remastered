import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;

/** Loader oracle and controlled interpreter timelines; no Canvas, audio or RMS. */
public final class ScriptCheck {
    static int comparisons;
    static final Class<?>[] NONE={}, INT={int.class};
    static Object call(ActorCheck.Access a,String old,String name,Class<?>[] types,Object... args) throws Exception {
        var m=a.game.getDeclaredMethod(a.renamed?name:old,types); m.setAccessible(true); return m.invoke(null,args);
    }
    static void equal(Object a,Object b,String label) {
        ++comparisons;
        if(!Objects.deepEquals(a,b)) throw new AssertionError(label+": "+
            Arrays.deepToString(new Object[]{a})+" != "+Arrays.deepToString(new Object[]{b}));
    }
    static String hash(byte[] bytes) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
    }
    static final String[][] SCRIPT_FIELDS={{"cb","scriptTracks"},{"cc","scriptIds"},
        {"cd","scriptTicks"},{"ce","scriptTrackOffsets"},{"cf","scriptsPaused"}};
    static void compare(ActorCheck.Access a,ActorCheck.Access b,String label) throws Exception {
        for(String[] f:SCRIPT_FIELDS) equal(a.global(f[0],f[1]),b.global(f[0],f[1]),label+" "+f[1]);
    }
    static String fingerprint(byte[] bytes) {
        long value=0xcbf29ce484222325L;
        for(byte b:bytes) { value^=b&255; value*=0x100000001b3L; }
        return String.format("%016x",value);
    }
    static void loaders(ActorCheck.Access a,ActorCheck.Access b,Path assets,PrintWriter out,PrintWriter maps) throws Exception {
        for(int pack=3;pack<=14;++pack) {
            boolean gameplay=pack<=11;
            // String.class resource lookup cannot see application assets on desktop
            // Java. Seed the checked single-part pack stream without patching Game.
            byte[] packed=Files.readAllBytes(assets.resolve(Integer.toString(pack)));
            int count=SpriteCheck.u16(packed,0);
            int[] packOffsets=new int[count+1];
            for(int i=0;i<packOffsets.length;++i) packOffsets[i]=SpriteCheck.i32(packed,6+i*4);
            for(var access:List.of(a,b)) {
                call(access,"am","releaseTileMaps",NONE);
                access.global("dR","packPath","/"+pack);
                access.global("dS","packStream",new ByteArrayInputStream(packed));
                access.global("dT","packPosition",0);
                access.global("dU","packResourceCount",(short)count);
                access.global("dW","packPartCount",(short)1);
                access.global("dX","packPartFirstResource",new short[]{0});
                access.global("dY","packPartIndex",0);
                access.global("dV","packOffsets",packOffsets);
            }
            int level=pack==14?12:pack-3; // The loader remaps indices 11..13.
            for(var access:List.of(a,b)) call(access,"b","loadTileMaps",new Class<?>[]{int.class,boolean.class},level,gameplay);
            for(String[] f:new String[][]{{"dM","tileLayer0"},{"dN","tileLayer0Transforms"},
                    {"dO","tileLayer1"},{"dP","tileLayer1Transforms"},{"dL","collisionTiles"},
                    {"bQ","mapWidthTiles"},{"bR","mapHeightTiles"},{"bS","levelPixelWidth"},{"bT","levelPixelHeight"}})
                equal(a.global(f[0],f[1]),b.global(f[0],f[1]),"map "+pack+" "+f[1]);
            int cellsId=gameplay?8:3;
            byte[] dims=BehaviorCheck.resource(assets,pack,cellsId+1);
            equal(SpriteCheck.u16(dims,0),a.global("bQ","mapWidthTiles"),"loader uses last width");
            equal(SpriteCheck.u16(dims,2),a.global("bR","mapHeightTiles"),"loader uses last height");
            for(int grid=gameplay?0:1;grid<3;++grid) {
                int resource=gameplay?2+grid*3:(grid-1)*3;
                byte[] dimensions=BehaviorCheck.resource(assets,pack,resource+1);
                byte[] cells=(byte[])a.global(new String[]{"dL","dM","dO"}[grid],new String[]{"collisionTiles","tileLayer0","tileLayer1"}[grid]);
                String transforms=grid==0 ? "-" : fingerprint((byte[])a.global(grid==1?"dN":"dP",grid==1?"tileLayer0Transforms":"tileLayer1Transforms"));
                maps.printf("M\t%d\t%d\t%d\t%d\t%s\t%s%n",pack,grid,SpriteCheck.u16(dimensions,0),SpriteCheck.u16(dimensions,2),fingerprint(cells),transforms);
            }
            if(gameplay) {
                byte[] expected=BehaviorCheck.resource(assets,pack,2);
                for(int i=0;i<expected.length;++i) if(expected[i]==-1) expected[i]=0;
                equal(expected,a.global("dL","collisionTiles"),"collision normalization");
                for(var access:List.of(a,b)) call(access,"a","decodeScripts",new Class<?>[]{byte[].class},BehaviorCheck.resource(assets,pack,1));
                compare(a,b,"decode pack "+pack);
                short[] ids=(short[])a.global("cc","scriptIds");
                byte[][][] tracks=(byte[][][])a.global("cb","scriptTracks");
                int[][] offsets=(int[][])a.global("ce","scriptTrackOffsets");
                for(int s=0;s<ids.length;++s) {
                    out.printf("S\t%d\t%d\t%d\t%d%n",pack,s,ids[s],tracks[s].length);
                    equal(s,call(a,"x","findScriptIndex",INT,(int)ids[s]),"script ID lookup");
                    equal(s,call(b,"x","findScriptIndex",INT,(int)ids[s]),"reference ID lookup");
                    for(int t=0;t<tracks[s].length;++t) {
                        byte[] bytes=tracks[s][t];
                        out.printf("T\t%d\t%d\t%d\t%d\t%d\t%s%n",pack,s,t,offsets[s][t],bytes.length,hash(bytes));
                        equal((int)(short)-1, (int)((short[])a.global("cd","scriptTicks"))[s],"initial script disabled");
                    }
                    for(String[] op:new String[][]{{"t","startScript"},{"u","stopAndRewindScript"},{"v","restartScript"}}) {
                        call(a,op[0],op[1],INT,s); call(b,op[0],op[1],INT,s); compare(a,b,"lifecycle "+op[1]);
                        equal(op[0].equals("u")?-2:0,(int)((short[])a.global("cd","scriptTicks"))[s],"lifecycle tick");
                    }
                }
                call(a,"v","resetScripts",NONE); call(b,"v","resetScripts",NONE); compare(a,b,"reset");
            }
        }
        call(a,"w","releaseScripts",NONE); call(b,"w","releaseScripts",NONE); compare(a,b,"release");
        byte[] ids=(byte[])a.global("bi","bi"),palettes=(byte[])a.global("bj","bj");
        equal(ids,b.global("bi","bi"),"tile atlas resource table"); equal(palettes,b.global("bj","bj"),"tile atlas palette table");
        for(int level=0;level<12;++level) maps.printf("B\t%d\t%d\t%d\t%d\t%d%n",level,ids[level*4+2],palettes[level*4+2],ids[level*4+3],palettes[level*4+3]);
    }
    static void le16(DataOutputStream d,int n) throws Exception { d.writeByte(n); d.writeByte(n>>8); }
    static void le32(DataOutputStream d,int n) throws Exception { le16(d,n); le16(d,n>>16); }
    static byte[] timeline() throws Exception {
        var bytes=new ByteArrayOutputStream(); var d=new DataOutputStream(bytes);
        d.writeByte(1); le16(d,701); d.writeByte(1); le16(d,0); // script header
        d.writeByte(2); d.writeByte(0); le16(d,186); le16(d,3); // actor track
        le16(d,2); d.writeByte(2);
        d.writeByte(107); le32(d,0x20000);
        d.writeByte(100); le16(d,186); le16(d,0); le16(d,5); // priority += 5
        le16(d,4); d.writeByte(4);
        d.writeByte(21); le16(d,40); le16(d,199);
        d.writeByte(22); d.writeByte(1);
        d.writeByte(106); d.writeByte(123);
        d.writeByte(108); le32(d,0x20000);
        le16(d,100); d.writeByte(0); // keeps script active, no completion side effects
        return bytes.toByteArray();
    }
    static void interpreter(ActorCheck.Access a,ActorCheck.Access b,Path assets) throws Exception {
        Object pa=null,pb=null;
        for(var access:List.of(a,b)) {
            access.loadAssets(assets); access.clearWorld();
            Object player=access.player(0,199);
            access.global("aa","viewBounds",new int[]{0,0,240,320});
            if(access==a) pa=player; else pb=player;
            call(access,"a","decodeScripts",new Class<?>[]{byte[].class},timeline());
            call(access,"t","startScript",INT,0);
        }
        int priority=(int)a.get(pa,"an","paintPriority");
        for(int tick=1;tick<=8;++tick) {
            int frame=tick-1; // Original uses the pre-increment short counter.
            for(var access:List.of(a,b)) call(access,"b","updateScript",new Class<?>[]{int.class,boolean.class,int.class},0,false,1);
            compare(a,b,"timeline "+tick);
            ActorCheck.snapshot(a,b,pa,pb,"timeline "+tick);
            equal(frame<3?0:frame==3?20:40,a.get(pa,"K","positionX"),"linear target interpolation");
            equal(frame>=2 && frame<4,((int)a.get(pa,"c","flags")&0x20000)!=0,"timed flag operation");
            equal(priority+(frame>=2?5:0),a.get(pa,"an","paintPriority"),"property executes once");
            if(frame>=4) {
                equal(1,a.get(pa,"f","animationId"),"timed animation");
                equal(123,a.get(pa,"ao","actionState"),"timed action");
            }
        }
        for(var access:List.of(a,b)) {
            access.global("cf","scriptsPaused",true);
            call(access,"x","updateBackgroundScripts",NONE);
        }
        compare(a,b,"paused"); equal(8,(int)((short[])a.global("cd","scriptTicks"))[0],"pause prevents update");
        int[] sizes={6,2,3,0,2,1,1,4,4,0,0,8,1,0,4,0,4,2,0,0,1,0};
        for(int op=100;op<=121;++op) for(var access:List.of(a,b)) {
            equal(sizes[op-100],call(access,"z","getScriptOpcodeSize",INT,op),"opcode size");
            equal(sizes[op-100],call(access,"a","executeScriptOpcode",
                new Class<?>[]{int.class,byte[].class,int.class,int.class,int.class,access.actor},
                op,new byte[8],0,1,2,null),"off-tick opcode only skips");
        }
        var bytes=new ByteArrayOutputStream(); var d=new DataOutputStream(bytes);
        d.writeByte(1); le16(d,702); d.writeByte(1); le16(d,0);
        d.writeByte(1); d.writeByte(0); le16(d,2); // camera track, two keys
        le16(d,4); d.writeByte(1); d.writeByte(12); le16(d,900); le16(d,500);
        le16(d,100); d.writeByte(0);
        for(var access:List.of(a,b)) {
            access.global("L","cameraX",0); access.global("M","cameraY",0);
            call(access,"a","decodeScripts",new Class<?>[]{byte[].class},bytes.toByteArray());
            call(access,"t","startScript",INT,0);
        }
        for(int i=1;i<=5;++i) {
            for(var access:List.of(a,b)) call(access,"b","updateScript",new Class<?>[]{int.class,boolean.class,int.class},0,false,1);
            compare(a,b,"camera timeline "+i);
            equal(i*112,a.global("L","cameraX"),"centered camera target clamps to level width");
            equal(i*16,a.global("M","cameraY"),"centered camera target clamps to level height");
            equal(new int[]{i*112,i*16,i*112+240,i*16+320},a.global("aa","viewBounds"),"camera bounds");
            equal(a.global("aa","viewBounds"),b.global("aa","viewBounds"),"reference camera bounds");
        }
        // No-track script finishes immediately; distinguish looping from one-shot.
        byte[] empty={1,(byte)0xbf,2,0,0,0};
        for(var access:List.of(a,b)) {
            call(access,"a","decodeScripts",new Class<?>[]{byte[].class},empty);
            call(access,"t","startScript",INT,0);
        }
        for(int mode:new int[]{0,0,1}) {
            for(var access:List.of(a,b)) call(access,"b","updateScript",new Class<?>[]{int.class,boolean.class,int.class},0,false,mode);
            compare(a,b,"completion mode "+mode);
            equal(mode==0?0:-2,(int)((short[])a.global("cd","scriptTicks"))[0],"loop / one-shot completion");
        }
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),compiled=Path.of(args[1]),stubs=Path.of(args[2]),oracle=Path.of(args[3]);
        Files.createDirectories(oracle.toAbsolutePath().getParent());
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);
            var b=new ActorCheck.Access(compiled,assets,stubs,true);
            var out=new PrintWriter(Files.newBufferedWriter(oracle));
            var maps=new PrintWriter(Files.newBufferedWriter(oracle.resolveSibling("map-oracle.tsv")))) {
            loaders(a,b,assets,out,maps); interpreter(a,b,assets);
        }
        System.out.println("script/map behavior: OK ("+comparisons+" comparisons); oracle: "+oracle);
    }
}
