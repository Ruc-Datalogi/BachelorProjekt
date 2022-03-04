package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner; // Import the Scanner class to read text files

public class DataImporter {
    File textFile;
    ArrayList<String> bins1D = new ArrayList<>();

    public void loadFile1D(String path) {
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

    public void loadFile2D(String path) {
        try {
            textFile = new File(path);
            Scanner fileScanner = new Scanner(textFile);
            while (fileScanner.hasNextLine()){
                String line = fileScanner.nextLine();
                List<String> list = Arrays.asList(line.split(","));
                List<Integer> int_list = new ArrayList<>();
                for(String s : list) {
                    int_list.add(Integer.valueOf(s));
                }
                State.getState().modules.add(new Module(int_list.get(0), int_list.get(1), int_list.get(2)));
            }
        } catch (FileNotFoundException e){
            System.out.println(e);
        }


    }


}
