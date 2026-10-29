import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Executes pure recovered methods against the original bytecode, not a MIDP emulator. */
public final class ReferenceCheck {
    private static int comparisons;
    private static final class GameAccess implements AutoCloseable {
        final URLClassLoader loader;
        final Class<?> game;
        final boolean reference;
        GameAccess(Path classes, Path assets, Path stubs, boolean reference) throws Exception {
            this.reference = reference;
            List<URL> urls = new ArrayList<>();
            urls.add(classes.toUri().toURL());
            urls.add(assets.toUri().toURL());
            for (String stub : List.of("midpapi20.jar", "cldcapi11.jar", "nokiaui.jar"))
                urls.add(stubs.resolve(stub).toUri().toURL());
            loader = new URLClassLoader(urls.toArray(new URL[0]), ClassLoader.getPlatformClassLoader());
            game = Class.forName(reference ? "Game" : "e", true, loader);
        }
        Object call(String old, String renamed, Class<?>[] types, Object... args) throws Exception {
            Method method = game.getDeclaredMethod(reference ? renamed : old, types);
            method.setAccessible(true);
            return method.invoke(null, args);
        }
        Field field(String old, String renamed) throws Exception {
            Field field = game.getDeclaredField(reference ? renamed : old);
            field.setAccessible(true);
            return field;
        }
        void set(String old, String renamed, int value) throws Exception { field(old, renamed).setInt(null, value); }
        int get(String old, String renamed) throws Exception { return field(old, renamed).getInt(null); }
        public void close() throws Exception { loader.close(); }
    }
    private static final Class<?>[] NONE = {};
    private static final Class<?>[] INT = {int.class};
    private static void equal(Object original, Object reference, String context) {
        ++comparisons;
        if (!original.equals(reference)) throw new AssertionError(context + ": original=" + original + ", reference=" + reference);
    }
    private static void input(GameAccess original, GameAccess reference) throws Exception {
        // Exhaustive supported keys and a surrounding unsupported range.
        for (int code = -128; code < 256; ++code)
            equal(original.call("Q", "mapKeyCode", INT, code), reference.call("Q", "mapKeyCode", INT, code), "key " + code);
        for (GameAccess game : List.of(original, reference)) game.call("C", "clearInput", NONE);
        for (int step = 0; step < 20; ++step) {
            int pressed = step == 0 ? 4 : step == 3 ? 16 : step == 8 ? 65536 : 0;
            int released = step == 2 ? 4 : step == 5 ? 16 : step == 12 ? 65536 : 0;
            for (GameAccess game : List.of(original, reference)) {
                game.set("cA", "pendingPressMask", pressed);
                game.set("cF", "pendingReleaseMask", released);
                // Feed held-state changes directly; constructing Canvas would invoke API stub code.
                int held = (game.get("cB", "heldMask") | pressed) & ~released;
                game.set("cB", "heldMask", held);
                game.set("cC", "frameHeldMask", held);
                game.call("A", "latchInput", NONE);
            }
            String[][] names = {{"cz", "pressedMask"}, {"cA", "pendingPressMask"}, {"cB", "heldMask"},
                {"cC", "frameHeldMask"}, {"cD", "heldFrameCount"}, {"cE", "releasedMask"},
                {"cF", "pendingReleaseMask"}, {"cG", "framesSincePress"}};
            for (String[] pair : names) equal(original.get(pair[0], pair[1]), reference.get(pair[0], pair[1]), pair[1]);
            for (int mask : new int[]{4, 16, 65536, 65568, 327712}) {
                equal(original.call("B", "isKeyHeld", INT, mask), reference.call("B", "isKeyHeld", INT, mask), "held");
                equal(original.call("C", "wasKeyPressed", INT, mask), reference.call("C", "wasKeyPressed", INT, mask), "press");
            }
            equal(original.call("B", "hasKeyPress", NONE), reference.call("B", "hasKeyPress", NONE), "any key");
        }
        for (GameAccess game : List.of(original, reference)) game.call("C", "clearInput", NONE);
        for (String[] pair : new String[][]{{"cz", "pressedMask"}, {"cA", "pendingPressMask"}, {"cB", "heldMask"},
                {"cC", "frameHeldMask"}, {"cE", "releasedMask"}, {"cF", "pendingReleaseMask"}}) {
            equal(0, original.get(pair[0], pair[1]), "original reset");
            equal(0, reference.get(pair[0], pair[1]), "reference reset");
        }
    }
    private static void primitives(GameAccess original, GameAccess reference) throws Exception {
        Class<?>[] read = {byte[].class, int.class};
        for (int value : new int[]{Integer.MIN_VALUE, -32769, -32768, -129, -128, -1, 0, 1, 127, 128, 255, 32767, 32768, Integer.MAX_VALUE}) {
            byte[] a = new byte[12], b = new byte[12];
            Class<?>[] writeInt = {byte[].class, int.class, int.class};
            equal(original.call("b", "writeI32LE", writeInt, a, 3, value), reference.call("b", "writeI32LE", writeInt, b, 3, value), "write offset");
            equal(Arrays.toString(a), Arrays.toString(b), "write bytes");
            equal(value, reference.call("c", "readI32LE", read, b, 3), "int round trip");
            equal(original.call("c", "readI32LE", read, a, 3), reference.call("c", "readI32LE", read, b, 3), "read int");
            Class<?>[] writeShort = {byte[].class, int.class, short.class};
            equal(original.call("a", "writeI16LE", writeShort, a, 1, (short)value), reference.call("a", "writeI16LE", writeShort, b, 1, (short)value), "short offset");
            equal(original.call("b", "readI16LE", read, a, 1), reference.call("b", "readI16LE", read, b, 1), "read signed short");
            equal((short)value, reference.call("b", "readI16LE", read, b, 1), "short sign extension");
            equal(original.call("a", "writeByte", writeInt, a, 0, value), reference.call("a", "writeByte", writeInt, b, 0, value), "byte offset");
            equal(original.call("a", "readSignedByte", read, a, 0), reference.call("a", "readSignedByte", read, b, 0), "read signed byte");
            equal((byte)value, reference.call("a", "readSignedByte", read, b, 0), "byte sign extension");
        }
        for (int level = -2; level <= 10; ++level)
            equal(original.call("F", "musicIdForLevel", INT, level), reference.call("F", "musicIdForLevel", INT, level), "level music");
    }
    private static void transitions(GameAccess original, GameAccess reference) throws Exception {
        // Exercise transitions that do not allocate MIDP images or touch native APIs.
        // No Game constructor is invoked: Canvas is an API stub, not a runtime.
        for (GameAccess game : List.of(original, reference))
            game.field("dk", "soundEnabled").setBoolean(null, false);
        int previous = 0;
        for (int state : new int[]{1, 5, 7, 8, 14, 8, 12, 15, 9, 8, 23, 18, 11}) {
            for (GameAccess game : List.of(original, reference)) {
                game.set("z", "stateFrame", 17);
                game.set("cz", "pressedMask", 327712);
                game.call("a", "changeState", INT, state);
            }
            equal(state, reference.get("x", "state"), "new state");
            equal(previous, reference.get("y", "previousState"), "previous state");
            equal(0, reference.get("z", "stateFrame"), "state frame reset");
            for (String[] names : new String[][]{{"x", "state"}, {"y", "previousState"}, {"z", "stateFrame"},
                    {"cz", "pressedMask"}, {"ea", "menuDefinition"}, {"en", "soundResumeFrames"}})
                equal(original.get(names[0], names[1]), reference.get(names[0], names[1]), "transition " + names[1]);
            previous = state;
        }
    }
    private static int u16(byte[] data, int offset) { return (data[offset]&255) | ((data[offset+1]&255)<<8); }
    private static int i32(byte[] data, int offset) {
        return u16(data, offset) | (u16(data, offset+2)<<16);
    }
    private static void strings(GameAccess original, GameAccess reference, Path assets) throws Exception {
        byte[] pack = Files.readAllBytes(assets.resolve("15"));
        int count = u16(pack, 0);
        for (int table = 0; table < count; ++table) {
            int start = i32(pack, 6+table*4), end = i32(pack, 10+table*4);
            if (start == end) continue;
            byte[] data = Arrays.copyOfRange(pack, start+1, end);
            Class<?>[] decode = {int.class, byte[].class};
            original.call("a", "decodeStringTable", decode, table, data);
            reference.call("a", "decodeStringTable", decode, table, data);
            try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(data))) {
                input.skipNBytes(6);
                int size = input.readUnsignedShort();
                for (int index = 0; index < size; ++index) {
                    String expected = input.readUTF().toUpperCase().trim();
                    Class<?>[] lookup = {int.class, int.class};
                    Object actual = reference.call("h", "getString", lookup, table, index);
                    equal(original.call("h", "getString", lookup, table, index), actual, "string lookup");
                    equal(expected, actual, "modified UTF string");
                }
                equal(0, input.available(), "string table consumed exactly");
            }
            original.call("n", "clearStringTable", INT, table);
            reference.call("n", "clearStringTable", INT, table);
        }
    }
    private static void trigonometry(GameAccess original, GameAccess reference, Path assets) throws Exception {
        byte[] pack = Files.readAllBytes(assets.resolve("17"));
        int offset = i32(pack, 6) + 1;
        int[] table = new int[256];
        for (int i = 0; i < table.length; ++i) table[i] = i32(pack, offset + 4*i);
        original.field("bX", "trigTable256").set(null, table.clone());
        reference.field("bX", "trigTable256").set(null, table.clone());
        for (int angle = -720; angle <= 720; ++angle) {
            equal(original.call("k", "cos256", INT, angle), reference.call("k", "cos256", INT, angle), "cosine");
            equal(original.call("l", "sin256", INT, angle), reference.call("l", "sin256", INT, angle), "sine");
        }
        for (int quadrant = 0; quadrant < 4; ++quadrant) {
            equal(new int[]{256, 0, -256, 0}[quadrant], reference.call("k", "cos256", INT, quadrant*90), "cosine quadrant");
            equal(new int[]{0, 256, 0, -256}[quadrant], reference.call("l", "sin256", INT, quadrant*90), "sine quadrant");
        }
    }
    public static void main(String[] args) throws Exception {
        Path assets = Path.of(args[0]), compiled = Path.of(args[1]), stubs = Path.of(args[2]);
        try (GameAccess original = new GameAccess(assets, assets, stubs, false);
             GameAccess reference = new GameAccess(compiled, assets, stubs, true)) {
            input(original, reference);
            primitives(original, reference);
            strings(original, reference, assets);
            transitions(original, reference);
            trigonometry(original, reference, assets);
        }
        System.out.println("reference behavior: OK (" + comparisons + " comparisons against original bytecode/format expectations)");
    }
}
