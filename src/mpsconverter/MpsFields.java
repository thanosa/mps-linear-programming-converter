package mpsconverter;

public class MpsFields
{
    // Fields that each data row has. Data rows start with a space character.
    public String field0;
    public String field1;
    public String field2;
    public float field3;
    public String field4;
    public float field5;

    public MpsFields(String lineText)
    {
        if (lineText.substring(0, 1).equals(" "))
        {
            extractFieldValues(lineText);
        }
    }

    // Extracts the values for each of the field based on predefined columns.
    private void extractFieldValues(String lineText)
    {
        this.field0 = getColumn(lineText, 0);
        this.field1 = getColumn(lineText, 1);
        this.field2 = getColumn(lineText, 2);
        this.field3 = toFloat(getColumn(lineText, 3));
        this.field4 = getColumn(lineText, 4);
        this.field5 = toFloat(getColumn(lineText, 5));
    }

    // Retrieves data from a specific column within a line.
    private String getColumn(String lineText, int columnNumber)
    {
        int fromChar = MpsSpecs.COLUMN_RANGES[columnNumber][0];
        int toChar = MpsSpecs.COLUMN_RANGES[columnNumber][1];
        
        return lineText.substring(fromChar, toChar).trim();
    }

    // Converts a string into a float or 0 if it is empty.
    private float toFloat(String floatAsString)
    {
        try
        {
            String val = floatAsString.trim();
            if (!val.isEmpty())
            {
                return Float.parseFloat(val);
            }
            else
            {
                return 0;
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Unable to convert string to float: " + floatAsString);
            System.out.println(e.getMessage());
            return 0;
        }
    }
}
