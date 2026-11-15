import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Runs the ORIGINAL class files (e = Game, b = Actor, d = BSprite) tick by tick with scripted input and
 * prints a fingerprint of every static field and every live actor field after each Game.paint.
 * The native engine_check prints the same fingerprints from the translated C++; tools/check_engine.py
 * diffs them. Drawing is a no-op here (engine_adapter), so this compares game state, not pixels.
 *
 * Everything is placed on -Xbootclasspath/a because the game loads resources with
 * "".getClass().getResourceAsStream, i.e. through the bootstrap loader.
 *
 * usage: EngineOracle <script|-> <ticks> <seed> <trace> [dumpTick dumpFile] [saveIn]
 * Game.run()'s loop inputs are fed as the native glue feeds them: frame time M = 50 ms (66 ms while a
 * cutscene script runs), wall-clock stall watchdog k held at 0.
 */
public final class EngineOracle {
    static final long SEED = 1469598103934665603L, PRIME = 1099511628211L;
    static long hash;
    static PrintWriter lines;
    static String prefix;

    static long mix(long h, long v) { return (h ^ v) * PRIME; }

    static long value(Object v) {
        if (v == null) return 0;
        if (v instanceof Integer) return (Integer) v;
        if (v instanceof Long) return (Long) v;
        if (v instanceof Short) return (Short) v;
        if (v instanceof Byte) return (Byte) v;
        if (v instanceof Character) return (Character) v;
        if (v instanceof Boolean) return ((Boolean) v) ? 1 : 0;
        if (v instanceof String) {
            String s = (String) v;
            long h = mix(SEED, s.length());
            for (int i = 0; i < s.length(); i++) h = mix(h, s.charAt(i));
            return h;
        }
        if (v.getClass().isArray()) {
            int n = Array.getLength(v);
            long h = mix(SEED, n);
            if (v instanceof int[]) { for (int x : (int[]) v) h = mix(h, x); }
            else if (v instanceof byte[]) { for (byte x : (byte[]) v) h = mix(h, x); }
            else if (v instanceof short[]) { for (short x : (short[]) v) h = mix(h, x); }
            else if (v instanceof boolean[]) { for (boolean x : (boolean[]) v) h = mix(h, x ? 1 : 0); }
            else if (v instanceof char[]) { for (char x : (char[]) v) h = mix(h, x); }
            else if (v instanceof long[]) { for (long x : (long[]) v) h = mix(h, x); }
            else for (int i = 0; i < n; i++) h = mix(h, value(Array.get(v, i)));
            return h;
        }
        return 1;
    }

    static String elements(Object v) {
        if (v == null || !v.getClass().isArray() || !v.getClass().getComponentType().isPrimitive() || Array.getLength(v) > 4096) return "";
        StringBuilder sb = new StringBuilder(" [");
        for (int i = 0; i < Array.getLength(v); i++) {
            if (i > 0) sb.append(',');
            Object e = Array.get(v, i);
            sb.append(e instanceof Boolean ? ((Boolean) e ? 1 : 0) : e instanceof Character ? (int) (Character) e : ((Number) e).longValue());
        }
        return sb.append(']').toString();
    }

    static void put(Field f, Object owner) throws Exception {
        long x = value(f.get(owner));
        hash = mix(hash, x);
        if (lines != null) lines.println(prefix + f.getName() + "=" + Long.toUnsignedString(x) + elements(f.get(owner)));
    }

    static List<Field> fields(Class<?> c, boolean statics) {
        List<Field> out = new ArrayList<>();
        for (Field f : c.getDeclaredFields()) {
            if (Modifier.isStatic(f.getModifiers()) != statics) continue;
            f.setAccessible(true);
            out.add(f);
        }
        return out;
    }

    public static void main(String[] args) throws Exception {
        String script = args[0];
        int ticks = Integer.parseInt(args[1]);
        long seed = Long.parseLong(args[2]);
        int dumpTick = args.length > 5 ? Integer.parseInt(args[4]) : -1;
        String dumpFile = args.length > 5 ? args[5] : null;
        if (args.length > 6) javax.microedition.rms.RecordStore.stored = Files.readAllBytes(Paths.get(args[6]));

        List<int[]> events = new ArrayList<>();
        if (!script.equals("-"))
            for (String line : Files.readAllLines(Paths.get(script))) {
                String[] p = line.trim().split("\\s+");
                if (p.length == 3) events.add(new int[]{Integer.parseInt(p[0]), p[1].equals("+") ? 1 : p[1].equals("!") ? 2 : 0, Integer.parseInt(p[2])});
            }

        Class<?> game = Class.forName("e", true, null), actor = Class.forName("b", true, null), sprite = Class.forName("d", true, null);
        Class<?> midlet = Class.forName("GloftSOFD", true, null);
        Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
        Field theUnsafe = unsafeClass.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Method allocate = unsafeClass.getMethod("allocateInstance", Class.class);
        // The API stub constructors throw, so allocate without them and replay Game.<init>'s assignments.
        Object midletInstance = allocate.invoke(theUnsafe.get(null), midlet);
        Object canvas = allocate.invoke(theUnsafe.get(null), game);
        Field f;
        (f = midlet.getDeclaredField("a")).setAccessible(true); f.set(null, midletInstance);
        (f = midlet.getDeclaredField("b")).setAccessible(true); f.set(null, canvas);
        // obfuscated names: suspended, state, previousState, stateFrame, modal flag, soundResumeFrames,
        // resumeSoundId, actors, maxUnlockedLevel, playSound
        final String[] n = {"O", "F", "G", "I", "bx", "fP", "fO", "cf", "bg", "o"};
        (f = game.getDeclaredField(n[0])).setAccessible(true); f.setBoolean(null, false);
        (f = game.getDeclaredField("c")).setAccessible(true); f.set(null, new Random(seed));
        (f = game.getDeclaredField(n[1])).setAccessible(true); f.setInt(null, 0);
        Field state = f;
        (f = game.getDeclaredField(n[2])).setAccessible(true); f.setInt(null, -1);
        Field previousState = f;
        (f = game.getDeclaredField(n[3])).setAccessible(true); f.setInt(null, 0);
        Field suspended = game.getDeclaredField(n[0]); suspended.setAccessible(true);
        Field aS = game.getDeclaredField(n[4]); aS.setAccessible(true);
        Field soundResumeFrames = game.getDeclaredField(n[5]); soundResumeFrames.setAccessible(true);
        Field resumeSoundId = game.getDeclaredField(n[6]); resumeSoundId.setAccessible(true);
        Field actors = game.getDeclaredField(n[7]); actors.setAccessible(true);
        Field maxUnlocked = game.getDeclaredField(n[8]); maxUnlocked.setAccessible(true);
        Method playSound = game.getDeclaredMethod(n[9], int.class, int.class); playSound.setAccessible(true);
        Field frameDelta = game.getDeclaredField("M"); frameDelta.setAccessible(true);
        Field lastPaint = game.getDeclaredField("k"); lastPaint.setAccessible(true);
        Method scriptActive = game.getDeclaredMethod("K"); scriptActive.setAccessible(true);
        Method paint = game.getDeclaredMethod("paint", javax.microedition.lcdui.Graphics.class); paint.setAccessible(true);
        Method keyPressed = game.getDeclaredMethod("keyPressed", int.class); keyPressed.setAccessible(true);
        Method keyReleased = game.getDeclaredMethod("keyReleased", int.class); keyReleased.setAccessible(true);
        Method destroy = midlet.getDeclaredMethod("destroyApp", boolean.class); destroy.setAccessible(true);

        List<Field> gameStatics = fields(game, true), actorStatics = fields(actor, true), spriteStatics = fields(sprite, true);
        List<Field> actorFields = fields(actor, false);

        try (PrintWriter trace = new PrintWriter(Files.newBufferedWriter(Paths.get(args[3]), StandardCharsets.UTF_8))) {
            for (int t = 0; t < ticks; t++) {
                for (int[] e : events) if (e[0] == t && e[1] == 2) maxUnlocked.setInt(null, e[2]);
                for (int[] e : events) if (e[0] == t && e[1] != 2) (e[1] == 1 ? keyPressed : keyReleased).invoke(canvas, e[2]);
                int s = state.getInt(null);
                if (s == 11) { destroy.invoke(midletInstance, true); break; }
                if (!suspended.getBoolean(null)) {
                    // Game.run(): music resume countdown before each repaint
                    if ((s != 14 && s != 8 && s != 9 || s == 8 && aS.getBoolean(null))
                        && (s != 5 || previousState.getInt(null) != 14) && soundResumeFrames.getInt(null) > 0) {
                        for (;;) {
                            int left = soundResumeFrames.getInt(null) - 1;
                            soundResumeFrames.setInt(null, left);
                            if (left != 0) break;
                            playSound.invoke(null, resumeSoundId.getInt(null), -1);
                        }
                    }
                    frameDelta.setInt(null, ((Boolean) scriptActive.invoke(null)) ? 66 : 50);
                    lastPaint.setLong(null, 0L);
                    paint.invoke(canvas, new javax.microedition.lcdui.Graphics(240, 320));
                }
                lastPaint.setLong(null, 0L);
                boolean dump = t == dumpTick;
                if (dump) lines = new PrintWriter(Files.newBufferedWriter(Paths.get(dumpFile), StandardCharsets.UTF_8));
                hash = SEED;
                prefix = "e."; for (Field x : gameStatics) put(x, null);
                prefix = "b."; for (Field x : actorStatics) put(x, null);
                prefix = "d."; for (Field x : spriteStatics) put(x, null);
                Object[] list = (Object[]) actors.get(null);
                if (list != null)
                    for (int i = 0; i < list.length; i++) {
                        if (list[i] == null) continue;
                        prefix = "actor[" + i + "].";
                        for (Field x : actorFields) put(x, list[i]);
                    }
                if (dump) { lines.close(); lines = null; }
                trace.println(t + " " + Long.toUnsignedString(hash) + " " + state.getInt(null));
            }
        }
    }
}
