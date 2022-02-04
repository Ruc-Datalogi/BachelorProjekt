package sample;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Algorithms {


    /**
     *
     * @param arr Array of strings separated by whitespace
     * @param binCapacity int capacity of a single bin
     * @return
     */
    public static ArrayList<Bin1D> firstFit(ArrayList<String> arr, int binCapacity) {
            ArrayList<Bin1D> bins = new ArrayList<>();
            bins.add(new Bin1D(binCapacity));
            Integer pointer = 0;

            for(String box : arr) {
                if(overCapacity(Integer.valueOf(box), bins.get(pointer))) {
                    bins.get(pointer).box.add(Integer.valueOf(box));
                } else {
                    bins.add(new Bin1D(binCapacity));
                    pointer++;
                    bins.get(pointer).box.add(Integer.valueOf(box));
                }
            }

            for(Bin1D bin : bins){
                System.out.println(bin.toString());
            }

            return bins;
    }

    private static boolean overCapacity (Integer boxSize, Bin1D b1d) {
        Integer sum = 0;
        for (Integer b : b1d.box) {
            sum += b;
        }
        return sum + boxSize < b1d.maxCapacity;
    }

}
