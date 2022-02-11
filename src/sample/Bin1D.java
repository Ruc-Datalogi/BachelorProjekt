package sample;

import java.util.ArrayList;

public class Bin1D {
    ArrayList<Integer> box = new ArrayList<>();
    int maxCapacity;
    int capacity = 0;
    public Bin1D(int maxC) {
        this.maxCapacity = maxC;
    }

    public String toString () {
        String s = "";
        s += "Capacity: " + capacity + " Max: " + maxCapacity + " " + box.toString();
        return s;
    }

    public void incrementCapacity(int input) {
        this.capacity += input;
    }

}
