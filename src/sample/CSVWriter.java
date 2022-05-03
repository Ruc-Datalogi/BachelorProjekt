package sample;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;

public class CSVWriter {

    BufferedWriter writer = new BufferedWriter(new FileWriter("src/Data/CurrentCSV.csv"));
    protected CSVWriter() throws IOException {

    }

    static private CSVWriter csvWriter;

    public static CSVWriter getCsvWriter() throws IOException {
        if (csvWriter == null) csvWriter = new CSVWriter();
        return csvWriter;
    }

    public void close () throws IOException {
        writer.close();
    }


    public void createAndWrite(String directory, String fileName, String csv) throws IOException {
        File outputFile = new File(directory + fileName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile.getAbsolutePath()));

        csv.lines().forEach(n -> {
            try {
                writer.write(n);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        writer.close();
    }
    public void writeLists(ArrayList a1, ArrayList a2) throws IOException {
        System.out.println(a1.size());
        for (int i = 0; i < a1.size(); i++) {
            writer.write(a1.get(i) + "," + a2.get(i));
            writer.newLine();
        }
        writer.close();
    }

    public void write(String input) throws IOException {
        System.out.println("writing " + input);
        writer.write(input);
        writer.newLine();
    }
}
