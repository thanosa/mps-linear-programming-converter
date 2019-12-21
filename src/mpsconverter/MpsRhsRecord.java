package mpsconverter;

public class MpsRhsRecord
{
    public float[] values = null;

    public MpsRhsRecord(int size)
    {
        this.values = new float[size];
        for (int i = 0; i < size; i++)
        {
            this.values[i] = 0;
        }
    }

    public void setRecord(int rowIndex, float value)
    {
        if (rowIndex <= this.values.length)
        {
            this.values[rowIndex] = value;
        }
        else
        {
            System.out.println("Wrong row index provided: " + rowIndex);
            System.out.println("Maximum array length: " + values.length);
            System.exit(40);
        }
    }
}
