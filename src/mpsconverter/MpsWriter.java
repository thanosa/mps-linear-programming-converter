package mpsconverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class MpsWriter
{
    private final Map<String, String> rowTypeConversionMap = new HashMap<>()
    {
        {
            put("L", "1");
            put("G", "-1");
            put("E", "0");
            put("N", "N");
        }
    };

    private final String DELIMITER = ",";

    public MpsWriter(MpsData mpsData, String output)
    {
        createNewFile(output);
        exportProgramName(output, mpsData);
        exportMinMax(output, mpsData);
        exportA(output, mpsData);
        exportB(output, mpsData);
        exportC(output, mpsData);
        exportEqin(output, mpsData);
    }

    public void exportProgramName(String output, MpsData mpsData)
    {
        String exportString = mpsData.meta.programName;
        writeSection(output, exportString, "");
    }

    private void exportMinMax(String output, MpsData mpsData)
    {
        String exportString = mpsData.meta.programType;

        writeSection(output, exportString, "");
    }

    private void exportA(String output, MpsData mpsData)
    {
        int objectiveRowIndex = mpsData.meta.objectiveRowIndex;

        StringBuilder exportStringBuilder = new StringBuilder();
        for (int i = 0; i < mpsData.rows.getSize(); i++)
        {
            ArrayList<String> exportArray = new ArrayList<>();
            if (i != objectiveRowIndex)
            {
                for (int j = 0; j < mpsData.getColumnsRecordsCount(); j++)
                {
                    float value = mpsData.columns.getValue(j, i);
                    exportArray.add(String.valueOf(value));
                }
                exportStringBuilder.append(String.join(this.DELIMITER, exportArray));

                if (i < mpsData.rows.getSize() - 1)
                {
                    exportStringBuilder.append("\n");
                }
            }
        }

        writeSection(output, "A", exportStringBuilder.toString());
    }

    // Constants of the RHS
    private void exportB(String output, MpsData mpsData)
    {
        int objectiveRowIndex = mpsData.meta.objectiveRowIndex;

        ArrayList<String> exportArray = new ArrayList<>();
        for (int i = 0; i < mpsData.getRhsDataSize(); i++)
        {
            if (i != objectiveRowIndex)
            {
                float value = mpsData.rhs.getValue(i);
                exportArray.add(String.valueOf(value));
            }
        }

        String exportString = String.join(this.DELIMITER, exportArray);

        writeSection(output, "b", exportString);
    }

    // Constants of the objective function
    private void exportC(String output, MpsData mpsData)
    {
        int objectiveFunctionRowIndex = mpsData.meta.objectiveRowIndex;

        ArrayList<String> dataToExport = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < mpsData.getColumnsRecordsCount(); columnIndex++)
        {
            float value = mpsData.columns.getValue(columnIndex, objectiveFunctionRowIndex);
            dataToExport.add(String.valueOf(value));
        }

        String exportString = String.join(this.DELIMITER, dataToExport);

        writeSection(output, "c", exportString);
    }

    private void exportEqin(String output, MpsData mpsData)
    {
        ArrayList<String> dataToExport = new ArrayList<>();
        for (MpsEqinRecord eqinRecord : mpsData.eqin.getData())
        {
            String convertedRowType = this.rowTypeConversionMap.get(eqinRecord.rowType);
            dataToExport.add(convertedRowType);
        }

        String exportString = String.join(this.DELIMITER, dataToExport);

        writeSection(output, "EQIN", exportString);
    }

    private void writeSection(String output, String sectionTitle, String dataToExport)
    {
        try
        {
            File f = null;
            FileWriter fw = null;
            BufferedWriter bw = null;
            try
            {
                f = new File(output);
                fw = new FileWriter(f, true);
                bw = new BufferedWriter(fw);

                bw.write(sectionTitle + "\n");
                if (!dataToExport.isEmpty())
                {
                    bw.write(dataToExport + "\n");
                }
            }
            finally
            {
                bw.close();
                fw.close();
                f = null;
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            System.exit(50);
        }
    }

    // Creates a file and over-writes it if already exists.
    private void createNewFile(String fileToCreate)
    {
        try
        {
            File file = new File(fileToCreate);
            if (file.exists())
            {
                file.delete();
            }
            file.createNewFile();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
