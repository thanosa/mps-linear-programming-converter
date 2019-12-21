package mpsconverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MpsSections
{
    private final int EMPTY = -1;

    // The line indices are zero based.
    public int nameIndex = EMPTY;
    public int rowsStartIndex = EMPTY;
    public int rowsEndIndex = EMPTY;
    public int columnsStartIndex = EMPTY;
    public int columnsEndIndex = EMPTY;
    public int rhsStartIndex = EMPTY;
    public int rhsEndIndex = EMPTY;
    public int rangesStartIndex = EMPTY;
    public int rangesEndIndex = EMPTY;
    public int boundsStartIndex = EMPTY;
    public int boundsEndIndex = EMPTY;
    public int sosStartIndex = EMPTY;
    public int sosEndIndex = EMPTY;
    public int endDataIndex = EMPTY;

    public boolean hasRanges = false;
    public boolean hasBounds = false;
    public boolean hasSos = false;

    // Scan the file to locate the Sections based on the first word.
    public MpsSections(String inputFile)
    {
        try
        {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String lineText;
            int lineCounter = 0;
            while ((lineText = bufferedReader.readLine()) != null)
            {
                identifySection(lineText, lineCounter);
                lineCounter++;
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    // Parse a line to check where is section line.
    private void identifySection(String lineText, int currentLineIndex)
    {
        int nextLineIndex = currentLineIndex + 1;

        String firstWord = lineText.split(" ")[0];

        if (!firstWord.isEmpty())
        {
            switch (firstWord)
            {
                case "NAME":
                    this.nameIndex = currentLineIndex;
                    break;

                case "ROWS":
                    this.rowsStartIndex = nextLineIndex;
                    break;

                case "COLUMNS":
                    this.columnsStartIndex = nextLineIndex;
                    break;

                case "RHS":
                    this.rhsStartIndex = nextLineIndex;
                    break;

                case "RANGES":
                    this.hasRanges = true;
                    this.rangesStartIndex = nextLineIndex;
                    break;

                case "BOUNDS":
                    this.hasBounds = true;
                    this.boundsStartIndex = nextLineIndex;
                    break;

                case "SOS":
                    this.hasSos = true;
                    this.sosStartIndex = nextLineIndex;
                    break;

                case "ENDATA":
                    this.endDataIndex = currentLineIndex;

                    calcRowEndIndex();
                    calcColumnsEndIndex();
                    calcRhsEndIndex();
                    calcRangesEndIndex();
                    calcBoundsEndIndex();
                    calcSosEndIndex();
                    break;
            }
        }
    }

    private void calcColumnsEndIndex()
    {
        this.columnsEndIndex = this.rhsStartIndex - 2;
    }

    private void calcRowEndIndex()
    {
        this.rowsEndIndex = this.columnsStartIndex - 2;
    }

    private void calcRhsEndIndex()
    {
        if (this.hasRanges)
        {
            this.rhsEndIndex = this.rangesStartIndex - 2;
        }
        else if (this.hasBounds)
        {
            this.rhsEndIndex = this.boundsStartIndex - 2;
        }
        else if (this.hasSos)
        {
            this.rhsEndIndex = this.sosStartIndex - 2;
        }
        else
        {
            this.rhsEndIndex = this.endDataIndex - 1;
        }
    }

    private void calcRangesEndIndex()
    {
        if (this.hasRanges)
        {
            if (this.hasBounds)
            {
                this.rangesEndIndex = this.boundsStartIndex - 2;
            }
            else if (this.hasSos)
            {
                this.rangesEndIndex = this.sosStartIndex - 2;
            }
            else
            {
                this.rangesEndIndex = this.endDataIndex - 1;
            }
        }
    }

    private void calcBoundsEndIndex()
    {
        if (this.hasBounds)
        {
            if (this.hasSos)
            {
                this.boundsEndIndex = this.sosStartIndex - 2;
            }
            else
            {
                this.boundsEndIndex = this.endDataIndex - 1;
            }
        }
    }

    private void calcSosEndIndex()
    {
        if (this.hasSos)
        {
            this.sosEndIndex = this.endDataIndex - 1;
        }
    }

    // Returns the section depending on the line index.
    public MpsSpecs.Sections findSection(int lineIndex)
    {
        if (lineIndex == this.nameIndex)
        {
            return MpsSpecs.Sections.NAME;
        }
        else if (lineIndex >= this.rowsStartIndex
                && lineIndex <= this.rowsEndIndex)
        {
            return MpsSpecs.Sections.ROWS;
        }
        else if (lineIndex >= this.columnsStartIndex
                && lineIndex <= this.columnsEndIndex)
        {
            return MpsSpecs.Sections.COLUMNS;
        }
        else if (lineIndex >= this.rhsStartIndex
                && lineIndex <= this.rhsEndIndex)
        {
            return MpsSpecs.Sections.RHS;
        }
        else if (this.hasRanges
                && lineIndex >= this.rangesStartIndex
                && lineIndex <= this.rangesEndIndex)
        {
            return MpsSpecs.Sections.RANGES;
        }
        else if (this.hasBounds
                && lineIndex >= this.boundsStartIndex
                && lineIndex <= this.boundsEndIndex)
        {
            return MpsSpecs.Sections.BOUNDS;
        }
        else if (this.hasSos
                && lineIndex >= this.sosStartIndex
                && lineIndex <= this.sosEndIndex)
        {
            return MpsSpecs.Sections.SOS;
        }
        else if (lineIndex == this.endDataIndex)
        {
            return MpsSpecs.Sections.ENDATA;
        }
        else
        {
            return null;
        }
    }
}
