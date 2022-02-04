package sample;

import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files

public class DataImporter {
    File textFile;
    ArrayList<String> bins1D = new ArrayList<>();

    public void loadFile(String path) {
        try {
            textFile = new File(path);
            Scanner fileScanner = new Scanner(textFile);
            while (fileScanner.hasNextLine()){
                String line = fileScanner.nextLine();
                bins1D.add(line);
            }
        } catch (FileNotFoundException e){
            System.out.println(e);
        }

    }

}
