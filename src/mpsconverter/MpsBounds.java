package mpsconverter;

import java.util.ArrayList;

public class MpsBounds
{
    private final ArrayList<MpsBoundsRecord> data = new ArrayList<>();

    public void addData(String rowId, String type, float value)
    {
        this.data.add(new MpsBoundsRecord(rowId, type, value));
    }

    public ArrayList<MpsBoundsRecord> getData()
    {
        return data;
    }
}
