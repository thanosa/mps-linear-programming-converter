package mpsconverter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class Interfaces
{
    public final ArrayList<InterfacePair> paths = new ArrayList<>();

    Interfaces(String inputDir, String outputDir, String inputExtension, String outputExtension)
    {
        String[] inputFileNames = listFiles(inputDir, inputExtension);

        for (String inputFileName : inputFileNames)
        {
            // Calculates the output filename based on the input file name.
            String outFileName = replaceFileExtension(inputFileName, outputExtension);

            String inputPath = inputDir + "/" + inputFileName;
            String outputPath = outputDir + "/" + outFileName;

            InterfacePair interfacePair = new InterfacePair(inputPath, outputPath);

            this.paths.add(interfacePair);
        }
    }

    // Replaces the extension of a filename.
    private String replaceFileExtension(String fileName, String newExtension)
    {
        int dotIndex = fileName.lastIndexOf(".");
        String fileStun = fileName.substring(0, dotIndex);

        return fileStun + "." + newExtension;
    }

    // Lists the files within a directory of a specific extension.
    private String[] listFiles(String dir, final String extension)
    {
        File _inputDir = new File(dir);
        String[] fileList = _inputDir.list(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.toLowerCase().endsWith("." + extension);
            }
        });

        return fileList;
    }
}
