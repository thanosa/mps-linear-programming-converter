package mpsconverter;

import java.util.ArrayList;


public class MpsColumns
{
    private final ArrayList<MpsColumnsRecord> data = new ArrayList<>();
    
    private MpsColumnsRecord tempRecord;
    
        
    public void setRecord(int rowIndex, float value)
    {
        this.tempRecord.setRecord(rowIndex, value);
    }
    
    public void initialize(int size)
    {        
        this.tempRecord = new MpsColumnsRecord(size);
    }
    
    public float[] getRecord(String rowId)
    {
        return null;
    }
    
    public void saveTempRecord()
    {
        this.data.add(this.tempRecord);
    }
    
    public float getValue(int columnIndex, int rowIndex)
    {
        return this.data.get(columnIndex).values[rowIndex];
    }
    
    public int getRecordCount()
    {
        return this.data.size();
    }
}
