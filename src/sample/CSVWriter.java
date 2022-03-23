package sample;

import java.io.*;
import java.nio.Buffer;

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

    public void write(String input) throws IOException {
        System.out.println("writing " + input);
        writer.write(input);
        writer.newLine();
    }
}
