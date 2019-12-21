package mpsconverter;

import java.util.ArrayList;


public class MpsEqin
{
    private final ArrayList<MpsEqinRecord> data = new ArrayList<>();
   
    public void addData(String rowType)
    {
        this.data.add(new MpsEqinRecord(rowType));
    }
   
    public ArrayList<MpsEqinRecord> getData()
    {
        return this.data;
    }
}
