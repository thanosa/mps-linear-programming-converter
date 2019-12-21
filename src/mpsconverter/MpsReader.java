package mpsconverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MpsReader
{   
    // Main data class
    public MpsData mpsData = new MpsData();
    
    // Class level variables
    private String columnIdToKeep = "";
    private String rhsIdToKeep = "";
    private int rowCounter = 0;
       
    public MpsReader (String inputFile)
    {   
        MpsSections mpsSections = new MpsSections(inputFile);
        
        try
        {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            String lineText;
            int lineCounter = 0;
            while ((lineText = bufferedReader.readLine()) != null)
            {
                // Pads spaces to avoid exceptions when processing short strings.
                lineText = rightPadCharacter(lineText, ' ', MpsSpecs.MAX_LINE_WIDTH);
                
                MpsSpecs.LineType lineType = identifyLineType(lineText);
                
                switch (lineType)
                {
                    case COMMENT:
                        if (hasMaximiazationKeyword(lineText))
                        {
                            mpsData.meta.programType = "MAX";
                        }
                        
                        System.out.println(lineText);
                        
                        break;
                    case DATA:
                    case SECTION:
                        parseDataLine(lineText, lineCounter, mpsSections);
                        break;
                    case INVALID:
                        System.out.println("Invalid data line detected: " + lineText);
                        System.exit(10);
                }

                lineCounter++;
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    private String rightPadCharacter(String text, char character, int desirableLength)
    {
        int missingLength = Math.max(desirableLength - text.length(), 0);

        return text + String.valueOf(character).repeat(missingLength);
    }
    
    // Identify a line type based on the first character or word.
    private MpsSpecs.LineType identifyLineType(String line)
    {
        /* According to MPS specs
            -Comment lines begin with stars *.
            -Data lines begin with a space
            -Section lines start with one of the following words:
               {NAME, ROWS, COLUMNS, RHS, RANGES, BOUNDS, SOS, ENDDATA}
        
           Note: 
             This is not used at the moment but it will be used 
             to extract information about the type of the program 
             such as {minimizing / maximing} 
         */
        String firstCharacter = line.substring(0, 1);
        String firstWord = line.split(" ")[0];

        if (firstCharacter.equals("*"))
        {
            return MpsSpecs.LineType.COMMENT;
        }
        else if (firstCharacter.equals(" "))
        {
            return MpsSpecs.LineType.DATA;
        }
        else if (firstWord.equals("NAME")
                || firstWord.equals("ROWS")
                || firstWord.equals("COLUMNS")
                || firstWord.equals("RHS")
                || firstWord.equals("RANGES")
                || firstWord.equals("BOUNDS")
                || firstWord.equals("SOS")
                || firstWord.equals("ENDATA"))
        {
            return MpsSpecs.LineType.SECTION;
        }
        else
        {
            return MpsSpecs.LineType.INVALID;
        }
    }
    
    private boolean hasMaximiazationKeyword(String lineText)
    {
        String[] inputWords = lineText.split(" ");
        
        String[] keywords = new String[]{"max", "maximization", "maximize"};
        for (String inputWord : inputWords)
        {
            String cleanInputWord = inputWord.replaceAll("[^a-zA-Z]", "");
            for (String keyword : keywords)
            {
                if (cleanInputWord.equalsIgnoreCase(keyword))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void parseDataLine(String lineText, int lineCounter, MpsSections mpsSections)
    {    
        MpsSpecs.Sections activeSection = mpsSections.findSection(lineCounter);
        
        if (activeSection != null)
        {
            switch (activeSection)
            {
                case NAME:
                    parseNameLine(lineText);
                    break;
                    
                case ROWS:
                    parseRowsLine(lineText, lineCounter, mpsSections);
                    break;
                    
                case COLUMNS:
                    parseColumnsLine(lineText, lineCounter, mpsSections.columnsEndIndex);
                    break;
                    
                case RHS:
                    parseRhsLine(lineText);
                    break;
                    
                case RANGES:
                    if (mpsSections.hasRanges)
                    {
                        parseRangesLine(lineText);
                    }
                    break;
                    
                case BOUNDS:
                    if (mpsSections.hasBounds)
                    {
                        parseBoundsLine(lineText);
                    }
                    break;

                case SOS:
                    if (mpsSections.hasSos)
                    {       
                        parseSosLine(lineText);
                    }
                    break;
                    
                case ENDATA:
                default:
                    break;
            }
        }
    }

    private void parseNameLine(String lineText)
    {
        // Replaces multiple spaces to single space
        //   e.g. from   "NAME          Nazareth   "
        //          to   "NAME Nazareth "
        // Removes the spaces from the begin and the end of the string
        //   e.g. from   "NAME Nazareth "
        //          to   "NAME Nazareth"
        // Creates the space word String into a words list.
        //   e.g. from   "NAME Nazareth"
        //          to   wordList[0] = "NAME"
        //               wordList[1] = "Nazareth"
        String wordList[] = lineText.replaceAll("\\s{2,}", " ").trim().split(" ");

        if (wordList.length > 1)
        {
            this.mpsData.meta.programName = wordList[1];
        }
        else
        {
            this.mpsData.meta.programName = "";
            System.out.println("Warning: Unable to retrieve program name");
        }
    }
    
    private void parseRowsLine(String lineText, int lineCounter, MpsSections mpsSections)
    {
        MpsFields fields = new MpsFields(lineText);
          
        String rowType = fields.field0;
        String rowId = fields.field1;
        
        this.mpsData.rows.addData(rowId, rowType, this.rowCounter);
        
        Boolean isObjectiveFunctionRow = rowType.equals(MpsSpecs.OBJECTIVE_FUNCTION_TYPE_ID);
        if (!isObjectiveFunctionRow)
        {
            this.mpsData.eqin.addData(rowType);
        }
        else
        {
            this.mpsData.meta.objectiveRowIndex = lineCounter - mpsSections.rowsStartIndex;
        }
        
        this.rowCounter++;
    }
    
    private void parseColumnsLine(String lineText, int lineCounter, int lastColumnsLine)
    {
        MpsFields fields = new MpsFields(lineText);
          
        String columnId = fields.field1;
        String rowId1 = fields.field2;
        float value1 = fields.field3;
        String rowId2 = fields.field4;
        float value2 = fields.field5;
        
        // Every time a new column variable cames in, 
        // an array is initialized with zeros that has size equal to rows count.
        if (!this.columnIdToKeep.equals(columnId))
        {
            if(!this.columnIdToKeep.isEmpty())
            {
                this.mpsData.saveTempRecord();
            }
            int rowsCount = this.mpsData.rows.getSize();
            this.mpsData.initTempColumn(rowsCount);
        }
        
        // Stores current variable in order to check in next loop.
        this.columnIdToKeep = columnId;
        
        this.mpsData.putColumnsTemp(rowId1, value1);
        this.mpsData.putColumnsTemp(rowId2, value2);
        
        // Saves the last row of the columns.
        if (lineCounter == lastColumnsLine)
        {
            this.mpsData.saveTempRecord();
        }
    }

    private void parseRhsLine(String lineText)
    {
        MpsFields fields = new MpsFields(lineText);
            
        String rhsId = fields.field1;
        String row1Id = fields.field2;
        float value1 = fields.field3;
        String row2Id = fields.field4;
        float value2 = fields.field5;
        
        // Some problems have more than one RHS defined.
        // In any case only the first RHS is kept and the rest are discarded.
        if (this.rhsIdToKeep.isEmpty() || rhsId.equals(this.rhsIdToKeep))
        {
            // Stores the current rhsId as only the first one will be kept.
            this.rhsIdToKeep = rhsId;
   
            this.mpsData.putRhsData(row1Id, value1);
            this.mpsData.putRhsData(row2Id, value2);
        }
    }

    private void parseRangesLine(String lineText)
    {
        MpsFields fields = new MpsFields(lineText);
        
        String rangeId = fields.field0;
        String row1Id = fields.field2;
        float value1 = fields.field3;
        String row2Id = fields.field4;
        float value2 = fields.field5;
        
        this.mpsData.ranges.addData(row1Id, value1);
        this.mpsData.ranges.addData(row2Id, value2);
    }
    
    private void parseBoundsLine(String lineText)
    {
        MpsFields fields = new MpsFields(lineText);

        String boundType = fields.field0;
        String boundId = fields.field1;
        String rowId = fields.field2;
        float value = fields.field3;

        this.mpsData.bounds.addData(rowId, boundType, value);
    }
    
    private void parseSosLine(String lineText)
    {
        MpsFields fields = new MpsFields(lineText);

        String sosType = fields.field0;
        String rhsId = fields.field1;
        String rowId = fields.field2;
        float value = fields.field3;
        
        this.mpsData.sos.addData(rowId, sosType, value);
    }
}
