package javax.microedition.media;

import java.io.InputStream;

/** Oracle-only audio: a started player stays started until stopped, as in the native check runner. */
public final class Manager {
    public static Player createPlayer(InputStream stream, String type) {
        if (stream == null) throw new NullPointerException();
        return new Player() {
            int state = 100;
            public void realize() { if (state < 200) state = 200; }
            public void prefetch() { if (state < 300) state = 300; }
            public void start() { if (state == 0) throw new IllegalStateException(); state = 400; }
            public void stop() { if (state == 400) state = 300; }
            public void deallocate() { if (state == 300) state = 200; }
            public void close() { state = 0; }
            public int getState() { return state; }
            public void setLoopCount(int count) {}
        };
    }
}
