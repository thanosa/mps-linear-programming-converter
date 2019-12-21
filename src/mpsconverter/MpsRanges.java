package mpsconverter;

import java.util.ArrayList;

public class MpsRanges
{
    private final ArrayList<MpsRangesRecord> data = new ArrayList<>();

    public void addData(String rowId, float value)
    {
        if (rowId != null && !rowId.isEmpty())
        {
            if (!this.containsKey(rowId))
            {
                this.data.add(new MpsRangesRecord(rowId, value));
            }
            else
            {
                getRecord(rowId).setValue2(value);
            }
        }
    }

    private boolean containsKey(String rowId)
    {
        for (MpsRangesRecord mpsRangesRecord : data)
        {
            if (mpsRangesRecord.rowId.equals(rowId))
            {
                return true;
            }
        }
        return false;
    }

    private MpsRangesRecord getRecord(String rowId)
    {
        for (MpsRangesRecord mpsRecordRanges : data)
        {
            if (mpsRecordRanges.rowId.equals(rowId))
            {
                return mpsRecordRanges;
            }
        }
        return null;
    }
}
