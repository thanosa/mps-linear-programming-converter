package mpsconverter;

import java.util.ArrayList;

public class MpsSos
{
    private final ArrayList<MpsSosRecord> data = new ArrayList<>();

    public void addData(String rowId, String type, float value)
    {
        MpsSosRecord mpsSosRecord = new MpsSosRecord(rowId, type, value);

        this.data.add(mpsSosRecord);
    }

    public ArrayList<MpsSosRecord> getData()
    {
        return data;
    }
}
