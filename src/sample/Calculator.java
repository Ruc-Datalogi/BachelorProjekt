package sample;

import java.util.ArrayList;

public class Calculator {

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
                    bins.get(pointer).incrementCapacity(Integer.valueOf(box));
                    bins.get(pointer).box.add(Integer.valueOf(box));
                } else {
                    Bin1D bin1DTemp = new Bin1D(binCapacity);
                    bin1DTemp.box.add(Integer.valueOf(box));
                    bin1DTemp.incrementCapacity(Integer.valueOf(box));
                    bins.add(bin1DTemp);
                    pointer++;
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

    public static ArrayList<Bin1D> calculateOneDimension(Algorithms a, ArrayList<String> arr, int binCapacity) {
        ArrayList<Bin1D> output = new ArrayList<>();
        switch (a) {
            case FIRST_FIT:
                output = firstFit(arr, binCapacity);
                break;
            case WORST_FIT:
                System.out.println("i am worst");
                break;
        }
        return output;
    }

}
