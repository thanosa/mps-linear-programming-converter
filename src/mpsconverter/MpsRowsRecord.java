package mpsconverter;

public class MpsRowsRecord
{
    public final String rowId;
    public final String rowType;
    public final int rowIndex;

    public MpsRowsRecord(String rowId, String rowType, int rowIndex)
    {
        this.rowId = rowId;
        this.rowType = rowType;
        this.rowIndex = rowIndex;
    }
}
