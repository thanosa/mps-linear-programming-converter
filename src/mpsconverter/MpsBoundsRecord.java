package mpsconverter;


public class MpsBoundsRecord
{
    public final String rowId;
    public final String rowType;
    public final float value;
    
    public MpsBoundsRecord(String rowId, String rowType, float value)
    {
        this.rowId = rowId;
        this.rowType = rowType;
        this.value = value;
    }
}
