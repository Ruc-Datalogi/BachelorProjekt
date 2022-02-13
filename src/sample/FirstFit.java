package sample;

import java.util.ArrayList;

public class FirstFit extends Algorithm {

    int binCapacity;
    ArrayList<String> boxes;

    FirstFit(int binCapacity, ArrayList<String> boxes){
        this.binCapacity = binCapacity;
        this.boxes = boxes;
    }

    @Override
    void execute() {
        ArrayList<Bin1D> bins = new ArrayList<>();
        bins.add(new Bin1D(binCapacity));
        Integer pointer = 0;

        for(String box : boxes) {
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

        this.solution.addAll(bins);

        optimizationFactor = bins.size(); //we are optimizing the number of bins.
    }

    private static boolean overCapacity (Integer boxSize, Bin1D b1d) {
        Integer sum = 0;
        for (Integer b : b1d.box) {
            sum += b;
        }
        return sum + boxSize < b1d.maxCapacity;
    }
}
