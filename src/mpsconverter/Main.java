package mpsconverter;

public class Main
{
    public static void main(String[] args)
    {
        Interfaces interfaces = new Interfaces("input", "output", "mps", "tables");

        interfaces.paths.forEach((pair) ->
        {
            String inputPath = pair.inputPath;
            String outputPath = pair.outputPath;
            
            MpsData mpsData = new MpsReader(inputPath).mpsData;
            MpsWriter writer = new MpsWriter(mpsData, outputPath);

            System.out.println(" Input file: " + inputPath);
            System.out.println("Output file: " + outputPath);
            System.out.println("Conversion completed successfully.\n");
        });
    }
}

