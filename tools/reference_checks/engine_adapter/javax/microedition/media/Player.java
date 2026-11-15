package javax.microedition.media;

public interface Player {
    void realize() throws MediaException;
    void prefetch() throws MediaException;
    void start() throws MediaException;
    void stop() throws MediaException;
    void deallocate();
    void close();
    int getState();
    void setLoopCount(int count);
}
