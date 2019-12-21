package mpsconverter;

public class MpsSpecs
{
    public static enum LineType
    {
        SECTION, DATA, COMMENT, INVALID
    };
        
    public static enum Sections
    {
        NAME, ROWS, COLUMNS, RHS, RANGES, BOUNDS, SOS, ENDATA
    };
    
    public static String PROGRAM_NAME_SECTION_ID = "NAME";
    
    public static final String OBJECTIVE_FUNCTION_TYPE_ID = "N";
    
    public static final int MAX_LINE_WIDTH = 61;
    
    public static final int[][] COLUMN_RANGES = 
    {
        {1, 3}, 
        {4, 12}, 
        {14, 22}, 
        {24, 36}, 
        {39, 47}, 
        {49, 61}
    };
}
