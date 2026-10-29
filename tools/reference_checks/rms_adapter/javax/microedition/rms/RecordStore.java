package javax.microedition.rms;

import java.util.Arrays;

/** Test-only single-store, single-record adapter; never writes to disk. */
public final class RecordStore {
    private static byte[] record;
    public static int opens, closes, reads, adds, sets;
    private boolean closed;
    private RecordStore() { ++opens; }
    public static RecordStore openRecordStore(String name,boolean create) {
        if(!"/dvseed.sav".equals(name)||!create) throw new AssertionError("Unexpected RMS store request");
        return new RecordStore();
    }
    private void requireOpen() { if(closed) throw new IllegalStateException("Closed RMS handle"); }
    private void requireRecord(int id) {
        requireOpen();
        if(id!=1||record==null) throw new IllegalStateException("Missing test record");
    }
    public int getNumRecords() { requireOpen(); return record==null?0:1; }
    public int addRecord(byte[] data,int offset,int length) {
        requireOpen();
        if(record!=null) throw new AssertionError("Second test record");
        record=Arrays.copyOfRange(data,offset,offset+length); ++adds; return 1;
    }
    public void setRecord(int id,byte[] data,int offset,int length) {
        requireRecord(id); record=Arrays.copyOfRange(data,offset,offset+length); ++sets;
    }
    public int getRecord(int id,byte[] data,int offset) {
        requireRecord(id); System.arraycopy(record,0,data,offset,record.length); ++reads; return record.length;
    }
    public void closeRecordStore() { requireOpen(); closed=true; ++closes; }
    public static byte[] testRecord() { return record==null?null:record.clone(); }
}
