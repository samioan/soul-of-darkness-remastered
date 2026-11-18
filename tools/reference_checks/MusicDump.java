import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Writes the game's music tracks as the original code decodes them: Game.a("/24") opens the sound
 * pack and Game.q(i) returns entry i (the same calls as Game.aR, the sound loader).
 * Files are named t<i>.mid or t<i>.wav. Used by tools/music_levels.py.
 *
 * usage: MusicDump <outDir>   (original classes and pack data on the boot path, see music_levels.py)
 */
public final class MusicDump {
    public static void main(String[] args) throws Exception {
        Class<?> game = Class.forName("e");
        Method open = game.getDeclaredMethod("a", String.class);
        Method entry = game.getDeclaredMethod("q", int.class);
        open.setAccessible(true);
        entry.setAccessible(true);
        open.invoke(null, "/24");
        for (int i = 0; i < 18; i++) {
            byte[] b = (byte[]) entry.invoke(null, i);
            if (b == null) continue;
            boolean wav = b.length >= 4 && b[0] == 'R' && b[1] == 'I' && b[2] == 'F' && b[3] == 'F';
            Files.write(Paths.get(args[0], "t" + i + (wav ? ".wav" : ".mid")), b);
        }
    }
}
