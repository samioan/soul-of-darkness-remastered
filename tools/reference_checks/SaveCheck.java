import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/** In-memory persistence oracle. Does not open RMS or construct a level. */
public final class SaveCheck {
    static final Class<?>[] NONE = {};
    static final String[] FLAGS = {"bl","bm","bn","bo","bp","bq","br","bs","bt","bu","bw"};
    static final String[][] METERS = {{"q","maxHealth"},{"p","health"},{"r","r"},
        {"t","maxMagic"},{"s","magic"},{"u","u"},{"v","v"}};
    static int checks;
    static void eq(Object expected,Object actual,String label) {
        ++checks;
        if (!java.util.Objects.deepEquals(expected,actual)) throw new AssertionError(label);
    }
    static void game(ActorCheck.Access a,String old,String name,Class<?>[] types,Object... args) throws Exception {
        ScriptCheck.call(a,old,name,types,args);
    }
    static void put16(byte[] bytes,int offset,int value) {
        bytes[offset]=(byte)value; bytes[offset+1]=(byte)(value>>8);
    }
    static void progress(ActorCheck.Access a,ActorCheck.Access b) throws Exception {
        Random random=new Random(0x50d);
        for(int sample=0;sample<32;++sample) {
            byte[] seed=new byte[1313]; random.nextBytes(seed);
            int[] meters=random.ints(7).toArray();
            int[][] upgrades=new int[4][2];
            for(int[] row:upgrades) for(int j=0;j<2;++j) row[j]=random.nextInt();
            short[][] removed=new short[9][30];
            for(short[] row:removed) for(int j=0;j<30;++j) row[j]=(short)random.nextInt();
            short[] counts={0,1,2,3,4,5,6,7,8};
            int level=sample%9, unlocked=sample%10;
            int flagBits=sample<11?1<<sample:(sample*73)&2047;
            byte[] expected=seed.clone();
            expected[14]=(byte)level;
            if(expected[15]<unlocked) expected[15]=(byte)unlocked;
            put16(expected,24,sample%2); put16(expected,26,sample%2);
            for(int i=0;i<4;++i) for(int j=0;j<2;++j) put16(expected,28+4*i+2*j,upgrades[i][j]);
            for(int i=0;i<6;++i) put16(expected,60+2*i,meters[i]);
            for(int i=0;i<4;++i) expected[72+i]=(byte)(meters[6]>>(8*i));
            for(int i=0;i<FLAGS.length;++i) expected[653+i]=(byte)((flagBits>>i)&1);
            counts[level]=17;
            for(int i=0;i<9;++i) {
                put16(expected,86+2*i,counts[i]);
                for(int j=0;j<30;++j) put16(expected,104+60*i+2*j,removed[i][j]);
            }
            for(var access:List.of(a,b)) {
                access.global("cw","saveBuffer",seed.clone());
                access.global("aG","levelIndex",level); access.global("aH","maxUnlockedLevel",unlocked);
                access.global("aI","levelCompleted",sample%2==1);
                access.set(null,"bK","activeWeapon",sample%2);
                for(int i=0;i<4;++i) access.set(null,new String[]{"bM","bN","bO","bP"}[i],new String[]{"bM","bN","bO","bP"}[i],upgrades[i].clone());
                for(int i=0;i<7;++i) access.global(METERS[i][0],METERS[i][1],meters[i]);
                for(int i=0;i<FLAGS.length;++i) access.set(null,FLAGS[i],FLAGS[i],((flagBits>>i)&1)!=0);
                access.global("bJ","bJ",counts.clone());
                short[][] copy=Arrays.stream(removed).map(short[]::clone).toArray(short[][]::new);
                access.global("bG","bG",copy); access.global("bI","bI",17);
                access.call(null,"p","serializeProgress",NONE);
                byte[] bytes=(byte[])access.global("cw","saveBuffer");
                eq(expected,bytes,"entire serialized buffer, including untouched bytes");
                // Clear values before restoring so a no-op reader cannot pass.
                for(int i=0;i<7;++i) access.global(METERS[i][0],METERS[i][1],0);
                for(String flag:FLAGS) access.set(null,flag,flag,false);
                game(access,"q","restoreProgress",NONE);
                for(int i=0;i<7;++i) eq(i<6?(int)(short)meters[i]:meters[i],access.global(METERS[i][0],METERS[i][1]),"restored signed meter");
                for(int i=0;i<4;++i) {
                    int[] restored=(int[])access.get(null,new String[]{"bM","bN","bO","bP"}[i],new String[]{"bM","bN","bO","bP"}[i]);
                    for(int j=0;j<2;++j) eq((int)(short)upgrades[i][j],restored[j],"restored upgrade");
                }
                for(int i=0;i<FLAGS.length;++i) {
                    int source=i==4?5:i==5?4:i;
                    eq(((flagBits>>source)&1)!=0,access.get(null,FLAGS[i],FLAGS[i]),"original flag ordering");
                }
                game(access,"p","restoreActorPersistence",NONE);
                eq(counts,access.global("bJ","bJ"),"removed counts");
                eq(removed,access.global("bG","bG"),"removed UID rows");
                eq(17,access.global("bI","bI"),"current count");
                eq(true,access.global("bH","bH")==((short[][])access.global("bG","bG"))[level],"current row aliases table");
                eq(Arrays.copyOfRange(expected,999,1249),access.global("bD","bD"),"signed persistence bytes");
            }
            eq(a.global("cw","saveBuffer"),b.global("cw","saveBuffer"),"original/reference buffer");
        }
    }
    static void checkpoints(ActorCheck.Access a,ActorCheck.Access b) throws Exception {
        for(var access:List.of(a,b)) for(int level:new int[]{0,3,5}) for(boolean skip:new boolean[]{false,true}) {
            byte[] bytes=new byte[1313];
            put16(bytes,18,-123); put16(bytes,20,234); put16(bytes,22,1); put16(bytes,24,1);
            bytes[644]=1; put16(bytes,645,-321); put16(bytes,647,432);
            bytes[649]=0x78; bytes[650]=0x56; bytes[651]=0x34; bytes[652]=0x12;
            access.global("cw","saveBuffer",bytes); access.global("aG","levelIndex",level);
            access.global("bP","skipCheckpointPosition",skip); access.global("aI","levelCompleted",false);
            Object player=access.create(),first=access.create(),second=access.create();
            access.global("bk","player",player); access.global("bm","bm",first); access.global("bn","bn",second);
            game(access,"ak","restoreCheckpoint",NONE);
            eq(skip?0:-123,access.get(player,"K","positionX"),"checkpoint X / skip");
            eq(skip?0:level==5?214:234,access.get(player,"L","positionY"),"level 5 checkpoint Y correction");
            eq(!skip,access.get(player,"ak","facingLeft"),"checkpoint facing");
            if(level==3) for(Object actor:List.of(first,second)) {
                eq(-321,access.get(actor,"K","positionX"),"paired checkpoint X");
                eq(432,access.get(actor,"L","positionY"),"paired checkpoint Y");
                eq(0x12345678,access.get(actor,"ao","actionState"),"paired checkpoint state");
            }
        }
    }
    static void snapshots(ActorCheck.Access a,ActorCheck.Access b) throws Exception {
        String[][] fields={{"f","animationId"},{"h","animationFrame"},{"K","positionX"},
            {"L","positionY"},{"ao","actionState"},{"c","flags"},{"aw","aw"}};
        int[] values={255,128,65535,32768,0x12345678,0x87654321,-1234567};
        for(var access:List.of(a,b)) for(int slot=0;slot<3;++slot) {
            byte[] buffer=new byte[54]; Arrays.fill(buffer,(byte)0x5a);
            access.set(null,"by","by",buffer);
            Object actor=access.create();
            for(int i=0;i<fields.length;++i) access.set(actor,fields[i][0],fields[i][1],values[i]);
            game(access,"a","storeLinkedActorSnapshot",new Class<?>[]{access.actor,int.class},actor,slot);
            for(int i=0;i<54;++i) if(i<slot*18||i>=slot*18+18) eq((byte)0x5a,buffer[i],"snapshot neighboring slot");
            for(String[] field:fields) access.set(actor,field[0],field[1],0);
            game(access,"b","restoreLinkedActorSnapshot",new Class<?>[]{access.actor,int.class},actor,slot);
            for(int i=0;i<fields.length;++i) eq(i<2?(int)(byte)values[i]:i<4?(int)(short)values[i]:values[i],access.get(actor,fields[i][0],fields[i][1]),"snapshot signed field");
        }
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),compiled=Path.of(args[1]),stubs=Path.of(args[2]);
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);
            var b=new ActorCheck.Access(compiled,assets,stubs,true)) {
            progress(a,b); checkpoints(a,b); snapshots(a,b);
        }
        System.out.println("save checks: OK ("+checks+" comparisons; in-memory only, no RMS)");
    }
}
