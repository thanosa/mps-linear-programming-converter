package mpsconverter;

public class MpsRangesRecord
{
    public final String rowId;
    public final float value1;
    public float value2;

    public MpsRangesRecord(String rowId, float value1)
    {
        this.rowId = rowId;
        this.value1 = value1;
    }

    public void setValue2(float value2)
    {
        this.value2 = value2;
    }
}
