import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.PrintWriter;
import java.util.List;

/** Original side-contact helpers plus controlled player ticks. No enemy AI/paint. */
public final class ContactCheck {
    static final Class<?>[] NONE={};
    static void set(ActorCheck.Access a,Object body,String field,Object value) throws Exception {
        String name=switch(field) {
            case "am" -> "type"; case "ao" -> "actionState"; case "K" -> "positionX"; case "L" -> "positionY";
            case "G" -> "velocityX"; case "c" -> "flags"; case "bB" -> "grounded";
            case "cR" -> "wallOnLeft"; case "cS" -> "wallOnRight"; default -> field;
        };
        a.set(body,field,name,value);
    }
    static Object body(ActorCheck.Access a,int x) throws Exception {
        Object b=a.create(); set(a,b,"am",1); set(a,b,"K",x); set(a,b,"L",199);
        a.set(b,"p","sprite",Array.get(a.global("b","sprites"),0));
        a.call(b,"b","setAnimationImmediate",ActorCheck.INT,0);
        a.call(b,"g","refreshBounds",NONE); return b;
    }
    static void queue(ActorCheck.Access a,Object... bodies) throws Exception {
        Object q=Array.newInstance(a.actor,70);
        for(int i=0;i<bodies.length;++i) Array.set(q,i,bodies[i]);
        a.global("bw","updateActors",q); a.global("bx","updateActorCount",bodies.length);
    }
    static Object[] pair(ActorCheck.Access a,int id) throws Exception {
        a.clearWorld(); Object p=a.player(100,199),b=body(a,108),c=body(a,92);
        set(a,b,"O",true); set(a,b,"N",true);
        if(id<6912) {
            int bits=id;
            set(a,p,"K",100+(bits%3-1)*8); bits/=3;
            set(a,p,"G",(bits%3-1)*2560); bits/=3;
            set(a,b,"G",(bits%3-1)*768); bits/=3;
            set(a,p,"bB",bits%2!=0); bits/=2;
            set(a,p,"cR",(bits&1)!=0); set(a,p,"cS",(bits&2)!=0); bits/=4;
            set(a,b,"cR",(bits&1)!=0); set(a,b,"cS",(bits&2)!=0); bits/=4;
            set(a,b,"P",bits%2!=0); bits/=2;
            set(a,p,"R",(bits&1)!=0); set(a,p,"S",(bits&2)!=0);
            set(a,b,"K",100);
        } else if(id<7040) {
            int bits=id-6912;
            set(a,b,"am",new int[]{1,37,80,82}[bits%4]);
            set(a,b,"c",(bits&4)!=0?1024:0); set(a,b,"O",(bits&8)==0);
            set(a,b,"ao",bits/16); set(a,b,"P",true);
        } else {
            int bits=id-7040;
            set(a,b,"am",(bits&1)!=0?37:1); set(a,c,"am",(bits&2)!=0?80:1);
            set(a,b,"P",(bits&4)!=0); set(a,c,"P",(bits&8)!=0);
            set(a,p,"bB",true); set(a,c,"O",true); set(a,c,"N",true);
        }
        for(Object actor:new Object[]{p,b,c}) a.call(actor,"g","refreshBounds",NONE);
        queue(a,b,p,null,c); return new Object[]{p,b,c};
    }
    static void row(ActorCheck.Access a,Object body,PrintWriter out,int id,int phase,int who) throws Exception {
        out.print("C\t"+id+"\t"+phase+"\t"+who);
        for(String[] field:new String[][]{{"K","positionX"},{"L","positionY"},{"G","velocityX"},
            {"Q","Q"},{"R","R"},{"S","S"},{"bB","grounded"},{"cR","wallOnLeft"},{"cS","wallOnRight"}}) {
            Object value=a.get(body,field[0],field[1]);
            out.print("\t"+(value instanceof Boolean b?(b?1:0):value));
        }
        for(int n:(int[])a.get(body,"j","collisionBounds")) out.print("\t"+n);
        out.println();
    }
    static void compare(ActorCheck.Access a,ActorCheck.Access b,Object[] first,Object[] second,String context) throws Exception {
        for(int i=0;i<first.length;++i) {
            ActorCheck.snapshot(a,b,first[i],second[i],context+" body "+i);
            for(String field:new String[]{"N","O","P","Q","R","S"})
                ActorCheck.equal(a.get(first[i],field,field),b.get(second[i],field,field),context+" "+field);
        }
    }
    static void ticks(ActorCheck.Access a,ActorCheck.Access b,PrintWriter out) throws Exception {
        for(String scenario:new String[]{"locked_right","locked_left","push_right","push_left","paused","air"}) {
            Object[] roots=new Object[4];
            for(var access:List.of(a,b)) {
                access.clearWorld(); Object p=access.player(100,199);
                Object body=body(access,scenario.endsWith("left")?70:130);
                set(access,body,"O",true); set(access,body,"P",!scenario.startsWith("push"));
                queue(access,body,p);
                byte[] tiles=(byte[])access.global("dL","collisionTiles");
                for(int i=400;i<800;++i) tiles[i]=24;
                int offset=access==a?0:2; roots[offset]=p; roots[offset+1]=body;
            }
            for(int tick=0;tick<64;++tick) {
                boolean paused=scenario.equals("paused") && tick<10;
                int held=tick<30?(scenario.endsWith("left")?4112:8256):0;
                int pressed=scenario.equals("air") && tick==5?16388:0;
                for(var access:List.of(a,b)) {
                    int offset=access==a?0:2; access.input(pressed,held);
                    access.global("x","state",paused?14:8); access.global("z","stateFrame",tick);
                    access.call(roots[offset],"s","update",NONE);
                }
                for(int phase=0;phase<2;++phase) {
                    if(phase==1 && !paused) { a.call(roots[0],"f","advanceAnimation",NONE); b.call(roots[2],"f","advanceAnimation",NONE); }
                    compare(a,b,new Object[]{roots[0],roots[1]},new Object[]{roots[2],roots[3]},scenario+"/"+tick+"/"+phase);
                    out.print("U\t"+scenario+"\t"+tick+"\t"+phase);
                    for(String[] field:ActorCheck.SNAPSHOT) {
                        Object value=a.get(roots[0],field[0],field[1]);
                        if(value instanceof int[] rect) for(int n:rect) out.print("\t"+n); else out.print("\t"+value);
                    }
                    out.println("\t"+a.get(roots[1],"K","positionX")+"\t"+((boolean)a.get(roots[1],"Q","Q")?1:0));
                }
            }
        }
    }
    static void geometry(ActorCheck.Access a,ActorCheck.Access b,PrintWriter out) throws Exception {
        for(int spriteId:new int[]{0,2,3}) {
            Object sprite=Array.get(a.global("b","sprites"),spriteId);
            int count=Array.getLength(a.sprites.get(sprite,"n","animationFrameCount"));
            for(int animation=0;animation<count;++animation) {
                int frames=(int)a.sprites.call(sprite,"a","getAnimationFrameCount",ActorCheck.INT,animation);
                for(int frame=0;frame<frames;++frame) for(int flags=0;flags<4;++flags) {
                    int[] result=null;
                    for(var access:List.of(a,b)) {
                        Object actor=body(access,0); set(access,actor,"L",0);
                        access.set(actor,"p","sprite",Array.get(access.global("b","sprites"),spriteId));
                        access.call(actor,"b","setAnimationImmediate",ActorCheck.INT,animation);
                        access.set(actor,"h","animationFrame",frame); set(access,actor,"c",flags);
                        access.call(actor,"g","refreshBounds",NONE);
                        int[] rect=(int[])access.get(actor,"j","collisionBounds");
                        if(access==a) result=rect; else ActorCheck.equal(result,rect,"contact bounds "+spriteId+"/"+animation+"/"+frame+"/"+flags);
                    }
                    out.print("B\t"+spriteId+"\t"+animation+"\t"+frame+"\t"+flags);
                    for(int n:result) out.print("\t"+n); out.println();
                }
            }
        }
    }
    public static void main(String[] args) throws Exception {
        Path assets=Path.of(args[0]),compiled=Path.of(args[1]),stubs=Path.of(args[2]);
        Path output=assets.getParent().resolve("analysis/contact-oracle.tsv");
        try(var a=new ActorCheck.Access(assets,assets,stubs,false);
            var b=new ActorCheck.Access(compiled,assets,stubs,true);
            var out=new PrintWriter(Files.newBufferedWriter(output))) {
            a.loadAssets(assets); b.loadAssets(assets);
            geometry(a,b,out);
            for(int id=0;id<7056;++id) {
                Object[] first=pair(a,id),second=pair(b,id);
                for(int phase=0;phase<2;++phase) {
                    for(var access:List.of(a,b)) {
                        Object p=(access==a?first:second)[0];
                        if(phase==0) access.call(p,"x","resolvePlayerActorContacts",NONE);
                        else { access.call(p,"g","refreshBounds",NONE); access.call(p,"ai","resolveActorSeparation",NONE); }
                    }
                    compare(a,b,first,second,"pair "+id+"/"+phase);
                    for(int who=0;who<3;++who) row(a,first[who],out,id,phase,who);
                }
            }
            ticks(a,b,out);
        }
        System.out.println("player side contacts: 7056 arenas + 384 controlled ticks, "+ActorCheck.comparisons+" original/reference comparisons; "+output);
    }
}
