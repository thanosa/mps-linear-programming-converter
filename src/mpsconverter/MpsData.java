package mpsconverter;

public class MpsData
{
    public final MpsMeta meta = new MpsMeta();
    public final MpsRows rows = new MpsRows();
    public final MpsColumns columns = new MpsColumns();
    public final MpsRhs rhs = new MpsRhs();
    public final MpsEqin eqin = new MpsEqin();
    public final MpsRanges ranges = new MpsRanges();
    public final MpsBounds bounds = new MpsBounds();
    public final MpsSos sos = new MpsSos();

    public int getColumnsRecordsCount()
    {
        return this.columns.getRecordCount();
    }

    public void initTempColumn(int rowsCount)
    {
        this.columns.initialize(rowsCount);
    }

    public void putColumnsTemp(String rowId, float value)
    {
        if (!rowId.isEmpty())
        {
            int rowIndex = this.rows.getRowIndex(rowId);
            this.columns.setRecord(rowIndex, value);
        }
    }

    public void saveTempRecord()
    {
        this.columns.saveTempRecord();
    }

    public int getRhsDataSize()
    {
        return this.rhs.getDataSize();
    }

    public void putRhsData(String rowId, float value)
    {
        if (!rowId.isEmpty())
        {
            int rowIndex = this.rows.getRowIndex(rowId);
            this.rhs.setRecord(rowIndex, value, this.rows.getSize());
        }
    }
}
