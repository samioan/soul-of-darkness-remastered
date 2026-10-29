import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/** Menu decisions and selected loading stages. No menu drawing or Canvas. */
public final class MenuCheck {
    static int checks;
    static final Class<?>[] NONE={};
    static void eq(Object expected,Object actual,String label) {
        ++checks;
        if(!java.util.Objects.deepEquals(expected,actual)) throw new AssertionError(label+": "+expected+" != "+actual);
    }
    static void call(ActorCheck.Access a,String old,String name,Object... args) throws Exception {
        ScriptCheck.call(a,old,name,args.length==0?NONE:new Class<?>[]{int.class},args);
    }
    static void press(ActorCheck.Access a,int action,int key) throws Exception {
        int definition=action==2?2:0;
        byte[] menu=((byte[][])a.global("eb","menuItems"))[definition];
        int index=-1; for(int i=0;i<menu.length;++i) if(menu[i]==action) index=i;
        if(index<0) throw new AssertionError("Missing menu action "+action);
        a.global("ca","selectedMenuItem",index); a.global("cz","pressedMask",key);
        call(a,"H","handleMenuAction",definition);
    }
    static void seedBuffer(ActorCheck.Access a) throws Exception {
        // ensureTileBuffer only checks these references before marking the buffer dirty.
        // Allocate an opaque Graphics sentinel without running its throwing API-stub
        // constructor. No Graphics method, mutable-image allocation or draw is invoked.
        Class<?> unsafeClass=Class.forName("sun.misc.Unsafe");
        var field=unsafeClass.getDeclaredField("theUnsafe"); field.setAccessible(true);
        Object unsafe=field.get(null);
        Class<?> graphics=Class.forName("javax.microedition.lcdui.Graphics",true,a.sprites.loader);
        Object sentinel=unsafeClass.getMethod("allocateInstance",Class.class).invoke(unsafe,graphics);
        a.global("dC","dC",sentinel);
        Class<?> image=Class.forName("javax.microedition.lcdui.Image",true,a.sprites.loader);
        a.global("dB","dB",image.getMethod("createRGBImage",int[].class,int.class,int.class,boolean.class)
            .invoke(null,new int[260*340],260,340,false));
    }
    static void compare(ActorCheck.Access a,ActorCheck.Access b,String label) throws Exception {
        for(String old:new String[]{"x","y","z","aG","aH","aI","dK","bP","p","q","r","s","t","u","v","cw","cz","ds","dD"}) {
            String name=BehaviorCheck.names.getOrDefault("e."+old,old);
            eq(BehaviorCheck.normalize(a,a.global(old,name)),BehaviorCheck.normalize(b,b.global(old,name)),label+" "+old);
        }
        eq(LifecycleCheck.stored(a),LifecycleCheck.stored(b),label+" RMS");
    }
    static void newGame(ActorCheck.Access a,ActorCheck.Access b) throws Exception {
        for(var access:List.of(a,b)) {
            access.clearWorld(); seedBuffer(access);
            byte[] dirty=new byte[1313]; Arrays.fill(dirty,(byte)0x5a); SaveCheck.put16(dirty,16,12);
            byte[] expectedDirty=dirty.clone();
            access.global("cw","saveBuffer",dirty); access.global("x","state",2);
            access.global("aG","levelIndex",7); access.global("aH","maxUnlockedLevel",8);
            press(access,1,65568);
            eq(true,access.global("ds","confirmationOpen"),"existing checkpoint asks to replace progress");
            eq(2,access.global("x","state"),"confirmation keeps menu state");
            eq(null,LifecycleCheck.stored(access),"confirmation does not write RMS");
            press(access,1,131072);
            eq(false,access.global("ds","confirmationOpen"),"cancel closes confirmation");
            eq(expectedDirty,access.global("cw","saveBuffer"),"cancel keeps progress bytes");
            press(access,1,65568); press(access,1,262144);
            byte[] expected=new byte[1313]; SaveCheck.put16(expected,32,1);
            for(int offset:new int[]{60,62,66,68}) SaveCheck.put16(expected,offset,60);
            eq(expected,access.global("cw","saveBuffer"),"confirmed new game resets entire save buffer");
            eq(expected,LifecycleCheck.stored(access),"new-game record bytes");
            eq(20,access.global("x","state"),"new game enters prologue");
            eq(0,access.global("aG","levelIndex"),"new game resets level");
            eq(0,access.global("aH","maxUnlockedLevel"),"new game resets unlocks");
            eq(60,access.global("p","health"),"new game health"); eq(60,access.global("s","magic"),"new game magic");
            eq(new int[]{1,0},access.get(null,"bN","bN"),"new-game initial weapon upgrade");
            eq(false,access.global("cx","saveLoaded"),"new-game write does not set loaded flag");
            eq(true,access.global("dD","dD"),"existing tile buffer marked dirty");
            // With no checkpoint, the menu action only transitions; it does not call startNewGame.
            access.global("x","state",2); ((byte[])access.global("cw","saveBuffer"))[100]=42;
            press(access,1,65568);
            eq(20,access.global("x","state"),"empty-save New Game enters prologue");
            eq((byte)42,((byte[])access.global("cw","saveBuffer"))[100],"empty-save branch does not reset buffer again");
            eq(1,LifecycleCheck.counter(access,"adds"),"single new-game record addition");
            eq(0,LifecycleCheck.counter(access,"sets"),"empty-save branch does not overwrite RMS");
        }
        compare(a,b,"new game");
    }
    static void seedPack(ActorCheck.Access a,Path assets,int pack) throws Exception {
        byte[] packed=Files.readAllBytes(assets.resolve(Integer.toString(pack)));
        int count=SpriteCheck.u16(packed,0); int[] offsets=new int[count+1];
        for(int i=0;i<offsets.length;++i) offsets[i]=SpriteCheck.i32(packed,6+4*i);
        a.global("dR","packPath","/"+pack); a.global("dS","packStream",new ByteArrayInputStream(packed));
        a.global("dT","packPosition",0); a.global("dU","packResourceCount",(short)count);
        a.global("dW","packPartCount",(short)1); a.global("dX","packPartFirstResource",new short[]{0});
        a.global("dY","packPartIndex",0); a.global("dV","packOffsets",offsets);
    }
    static void continueGame(ActorCheck.Access a,ActorCheck.Access b,Path assets,int level,boolean completed) throws Exception {
        int target=completed&&level<8?level+1:level;
        boolean restore=target==level;
        var rows=LevelCheck.records(BehaviorCheck.resource(assets,target+3,0));
        int playerX=0,playerY=0,checkpointUid=-1,checkpointIndex=-1,index=0;
        for(var row:rows) {
            if(row.values()[0]==0) { playerX=row.values()[2]&65535; playerY=row.values()[3]&65535; }
            else {
                if(checkpointUid==-1&&row.values()[0]==23) { checkpointUid=row.values()[1]; checkpointIndex=index; }
                ++index;
            }
        }
        for(var access:List.of(a,b)) {
            LevelCheck.setup(access,assets,target+3);
            ScriptCheck.call(access,"a","decodeScripts",new Class<?>[]{byte[].class},BehaviorCheck.resource(assets,target+3,1));
            access.global("x","state",2); access.global("bP","skipCheckpointPosition",false);
            byte[] save=new byte[1313];
            save[14]=(byte)level; save[15]=8;
            SaveCheck.put16(save,16,checkpointUid+1); SaveCheck.put16(save,18,40); SaveCheck.put16(save,20,100);
            SaveCheck.put16(save,22,1); SaveCheck.put16(save,24,completed?1:0);
            SaveCheck.put16(save,60,90); SaveCheck.put16(save,62,17);
            SaveCheck.put16(save,66,80); SaveCheck.put16(save,68,33);
            save[999+checkpointIndex]=(byte)-99;
            for(int slot=0;slot<3;++slot) SaveCheck.put16(save,664+slot*2,-1);
            SaveCheck.put16(save,731,-1);
            access.global("cw","saveBuffer",save);
            byte[] expectedSave=save.clone();
            press(access,2,65568);
            eq(target,access.global("aG","levelIndex"),"Continue target level");
            eq(8,access.global("aH","maxUnlockedLevel"),"Continue unlocks");
            eq(restore,access.global("dK","continueFromCheckpoint"),"Continue chooses checkpoint construction");
            eq(9,access.global("x","state"),"Continue enters loader");
            eq(17,access.global("p","health"),"Continue restores saved health before loading");
            eq(0,access.global("cz","pressedMask"),"Continue clears input edge");
            seedPack(access,assets,15);
            eq(false,ScriptCheck.call(access,"K","loadLevelStep",new Class<?>[]{int.class},1),"stage 1 is not completion");
            eq(false,access.global("aI","levelCompleted"),"stage 1 clears completion flag");
            eq(false,ScriptCheck.call(access,"K","loadLevelStep",new Class<?>[]{int.class},79),"stage 79 constructs but does not report completion");
            Object player=access.global("bk","player");
            eq(restore?40:playerX,access.get(player,"K","positionX"),"Continue position X");
            eq(restore?100-(target==5?20:0):playerY,access.get(player,"L","positionY"),"Continue position Y");
            eq(restore?17:90,access.global("p","health"),"checkpoint retains health; next level starts full");
            eq(restore?33:80,access.global("s","magic"),"checkpoint retains magic; next level starts full");
            eq(false,access.global("dK","continueFromCheckpoint"),"stage 79 consumes Continue flag");
            eq(restore&&completed,access.global("aI","levelCompleted"),"checkpoint path rereads saved completion flag");
            Object checkpoint=ScriptCheck.call(access,"g","findActorByUid",new Class<?>[]{int.class},checkpointUid);
            eq(true,checkpoint!=null,"Continue fresh construction recreates checkpoint despite -99");
            eq(new byte[250],access.global("bD","bD"),"Continue fresh construction clears persistence bytes");
            eq(expectedSave,access.global("cw","saveBuffer"),"Continue leaves serialized save untouched");
            eq(true,ScriptCheck.call(access,"K","loadLevelStep",new Class<?>[]{int.class},80),"stage 80 reports completion");
        }
        compare(a,b,"Continue level="+level+" completed="+completed);
        LevelCheck.compare(a,b,"Continue world");
    }
    static void selectionStats(ActorCheck.Access a,ActorCheck.Access b) throws Exception {
        for(var access:List.of(a,b)) {
            access.clearWorld();
            byte[] save=new byte[1313];
            SaveCheck.put16(save,60,90); SaveCheck.put16(save,62,17);
            SaveCheck.put16(save,66,80); SaveCheck.put16(save,68,33);
            access.global("cw","saveBuffer",save);
            access.global("x","state",2); press(access,2,65568);
            eq(60,access.global("p","health"),"Continue without checkpoint resets health");
            eq(60,access.global("s","magic"),"Continue without checkpoint resets magic");
            eq(true,access.global("dK","continueFromCheckpoint"),"Continue flag is set even without a checkpoint");
            for(boolean checkpoint:new boolean[]{false,true}) {
                SaveCheck.put16(save,16,checkpoint?12:0);
                access.global("dw","dw",true);
                call(access,"aa","prepareLevelSelectionStats");
                eq(checkpoint?17:60,access.global("p","health"),"level selection chooses saved/default stats");
                eq(true,access.global("bP","skipCheckpointPosition"),"level selection skips old checkpoint position");
                eq(false,access.global("dw","dw"),"level selection consumes selection flag");
            }
        }
        compare(a,b,"selection stats");
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),compiled=Path.of(args[1]),stubs=Path.of(args[2]);
        for(String raw:Files.readAllLines(Path.of(args[3]))) {
            String[] p=raw.strip().split("\\s+");
            if(p.length==3&&p[0].equals("field")) BehaviorCheck.names.put(p[1],p[2]);
        }
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);
            var b=new ActorCheck.Access(compiled,assets,stubs,true)) { newGame(a,b); }
        for(int level:new int[]{0,5,7,8}) for(boolean complete:new boolean[]{false,true}) {
            try(var a=new ActorCheck.Access(assets,assets,stubs,false);
                var b=new ActorCheck.Access(compiled,assets,stubs,true)) { continueGame(a,b,assets,level,complete); }
        }
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);
            var b=new ActorCheck.Access(compiled,assets,stubs,true)) { selectionStats(a,b); }
        System.out.println("new-game/continue: OK ("+checks+" expectations; selected loading stages only)");
    }
}
