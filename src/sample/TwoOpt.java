package sample;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TwoOpt extends Algorithm {

    ArrayList<Bin1D> configuration;

    TwoOpt(ArrayList<Bin1D> configuration){
        this.configuration = configuration;
    }


    @Override
    void execute() {
        Random r1 = new Random();

        int index1 = r1.nextInt(configuration.size());
        int index2 = r1.nextInt(configuration.size());

        Bin1D bin1 = configuration.get(index1);
        Bin1D bin2 = configuration.get(index2);

        int boxIndex1 = r1.nextInt(bin1.box.size());
        int boxIndex2 = r1.nextInt(bin2.box.size());

        int box1 = bin1.box.get(boxIndex1);
        int box2 = bin2.box.get(boxIndex2);

        int newCapBin1 = bin1.capacity - box1 + box2;
        int newCapBin2 = bin2.capacity - box2 + box1;

        if (bin1.capacity + box2 < bin1.maxCapacity && bin2.box.size() == 1) { //try to add box from 2 to 1,
            bin1.box.add(box2);
            bin2.box.remove(boxIndex2);
            bin1.capacity += box2;
            bin2.capacity -= box2;

            if (bin2.capacity == 0)  {
                configuration.remove(bin2);
            }
            return;
        }

        if (newCapBin1 < bin1.maxCapacity && newCapBin2 < bin2.maxCapacity) {
            bin1.box.add(box2);
            bin2.box.add(box1);
            bin1.capacity = newCapBin1;
            bin2.capacity = newCapBin2;

            bin1.box.remove(boxIndex1);
            bin2.box.remove(boxIndex2);
        }

        this.optimizationFactor = configuration.size();

        AtomicInteger sum = new AtomicInteger();
        configuration.forEach(bin1D -> sum.addAndGet(bin1D.capacity));

        System.out.println(sum);
    }
}
