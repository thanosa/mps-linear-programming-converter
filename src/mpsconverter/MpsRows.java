package mpsconverter;

import java.util.ArrayList;

public class MpsRows
{
    private final ArrayList<MpsRowsRecord> data = new ArrayList<>();

    public void addData(String rowId, String rowType, int rowIndex)
    {
        this.data.add(new MpsRowsRecord(rowId, rowType, rowIndex));
    }

    public int getSize()
    {
        return this.data.size();
    }

    public String getRowType(String rowId)
    {
        for (MpsRowsRecord rowsRecord : data)
        {
            if (rowsRecord.rowId.equals(rowId))
            {
                return rowsRecord.rowType;
            }
        }
        System.out.println("Unable to find record with rowId: " + rowId);
        System.exit(30);
        return "";
    }

    public int getRowIndex(String rowId)
    {
        for (MpsRowsRecord rowsRecord : data)
        {
            if (rowsRecord.rowId.equals(rowId))
            {
                return rowsRecord.rowIndex;
            }
        }
        System.out.println("Unable to find record with rowId: " + rowId);
        System.exit(31);
        return 0;
    }
}
