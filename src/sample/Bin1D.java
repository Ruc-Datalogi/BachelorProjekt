package sample;

import java.util.ArrayList;

public class Bin1D {
    ArrayList<Integer> box = new ArrayList<>();
    int maxCapacity;

    public Bin1D(int maxC) {
        this.maxCapacity = maxC;
    }

    public String toString () {
        String s = "";
        s += "Max " + maxCapacity + " " + box.toString();
        return s;
    }

}
