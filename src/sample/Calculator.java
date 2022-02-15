package sample;

import java.util.ArrayList;

public class Calculator {

    /**
     *
     * @param arr Array of strings separated by whitespace
     * @param binCapacity int capacity of a single bin
     * @return
     */

    public static ArrayList<Bin1D> nextFit(ArrayList<String> arr, int binCapacity) {
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

            return bins;
    }

    public static ArrayList<Bin1D> firstFit(ArrayList<String> arr, int binCapacity) {
        ArrayList<Bin1D> bins = new ArrayList<>();
        bins.add(new Bin1D(binCapacity));

        for (String box : arr) {
            for (Bin1D bin : bins) {
                if (overCapacity(Integer.valueOf(box), bin )) {
                    bin.incrementCapacity(Integer.valueOf(box));
                    bin.box.add(Integer.valueOf(box));
                    break;
                }
                if (bin == getLastElement(bins) && !overCapacity(Integer.valueOf(box), bin )) {
                    Bin1D tempBin = new Bin1D(binCapacity);
                    tempBin.box.add(Integer.valueOf(box));
                    tempBin.incrementCapacity(Integer.valueOf(box));
                    bins.add(tempBin);
                    break;
                }
            }
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
            case WORST_FIT:
                System.out.println("i am worst");
                break;
            case FIRST_FIT:
                output = firstFit(arr, binCapacity);
                break;
            case NEXT_FIT:
                output = nextFit(arr, binCapacity);
                break;
        }
        return output;
    }

    public static <E> E getLastElement(ArrayList<E> list)
    {
        int lastIdx = list.size() - 1;
        E lastElement = list.get(lastIdx);
        return lastElement;
    }

}
