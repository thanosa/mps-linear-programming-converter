package mpsconverter;

public class MpsRhs
{
    private MpsRhsRecord data;
    private int size;

    public float getValue(int rowIndex)
    {
        return this.data.values[rowIndex];
    }

    public int getDataSize()
    {
        return this.size;
    }

    public void setRecord(int rowIndex, float value, int size)
    {   
        if (data == null)
        {
            this.size = size;
            this.data = new MpsRhsRecord(size);
        }

        this.data.setRecord(rowIndex, value);
    }
}
