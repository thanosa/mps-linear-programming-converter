package mpsconverter;

public class MpsSosRecord
{
    public final String rowId;
    public final String rowType;
    public final float value;

    public MpsSosRecord(String rowId, String rowType, float value)
    {
        this.rowId = rowId;
        this.rowType = rowType;
        this.value = value;
    }
}
