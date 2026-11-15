package javax.microedition.rms;

/** Oracle-only single-record store kept in memory. */
public class RecordStore {
    public static byte[] stored;
    public static RecordStore openRecordStore(String name, boolean create) { return new RecordStore(); }
    public int getNumRecords() { return stored == null ? 0 : 1; }
    public int addRecord(byte[] data, int offset, int length) { setRecord(1, data, offset, length); return 1; }
    public void setRecord(int id, byte[] data, int offset, int length) { stored = java.util.Arrays.copyOfRange(data, offset, offset + length); }
    public int getRecord(int id, byte[] out, int offset) { System.arraycopy(stored, 0, out, offset, stored.length); return stored.length; }
    public byte[] getRecord(int id) { return stored.clone(); }
    public void closeRecordStore() {}
}
