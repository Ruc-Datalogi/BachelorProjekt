package sample;

import java.util.ArrayList;
import java.util.List;

public class DivideAndConquer extends Algorithm{

    ArrayList<Integer> positive = new ArrayList<>();
    ArrayList<Integer> negative = new ArrayList<>();
    ArrayList<Module> modules = new ArrayList<>();

    public DivideAndConquer (ArrayList<Integer> positive, ArrayList<Integer> negative, ArrayList<Module> modules) {
        this.positive = positive;
        this.negative = negative;
        this.modules = modules;
    }

    public void calculatePlacement() {
        ArrayList<Module> rectangles = new ArrayList<>();
        ArrayList<Module> squares = new ArrayList<>();
        ArrayList<ArrayList<Module>> buckets = new ArrayList<>();
        int bucketIndex = 0;
        int bucketSize = (int) Math.round(Math.sqrt(modules.size()));

        for (Module mod : modules) {
            if ((float) mod.height / (float) mod.width >= 2 || (float) mod.height / (float) mod.width <= 0.5) {
                rectangles.add(mod);
            } else {
                squares.add(mod);
            }
        }

        ArrayList<Module> tempList = new ArrayList<>(); // Fill up the buckets with the size of amount modules sqrt
        for(Module mod : rectangles) {
            tempList.add(mod);
            if (tempList.size() == bucketSize ) {
                buckets.add(new ArrayList<>(tempList));
                tempList.clear();
            }
        }
        for(Module mod : squares) {
            tempList.add(mod);
            if (tempList.size() == bucketSize ) {
                buckets.add(new ArrayList<>(tempList));
                tempList.clear();
            }
        }
        buckets.add(new ArrayList<>(tempList)); // Make sure we get every module in a bucket

        for(int i = 0 ; i < buckets.size(); i++ ) {
            ArrayList<Module> currentList = buckets.get(i);
            ArrayList<Integer> seqPositive = new ArrayList<>();
            ArrayList<Integer> seqNegative = new ArrayList<>();

            for(Module mod : currentList) {
                seqPositive.add(mod.id);
                seqNegative.add(mod.id);

                int posiPosition = seqPositive.indexOf(mod.id);
                mod.setPositiveIndex(posiPosition);
                int negiPositon = seqNegative.indexOf(mod.id);
                mod.setNegativeIndex(negiPositon);

                List<Integer> leftPosSlice   = seqPositive.subList(0, posiPosition);
                List<Integer> rightPosSlice  = seqPositive.subList(posiPosition+1, seqPositive.size() );
                List<Integer> leftNegiSlice  = seqNegative.subList(0, negiPositon);
                List<Integer> rightNegiSlice = seqNegative.subList(negiPositon+1, seqNegative.size() );

                mod.leftOf  = CommonFunctions.getCommon(leftPosSlice, leftNegiSlice);
                mod.rightOf = CommonFunctions.getCommon(rightPosSlice, rightNegiSlice);
                mod.above   = CommonFunctions.getCommon(rightNegiSlice,leftPosSlice);
                mod.below   = CommonFunctions.getCommon(leftNegiSlice,rightPosSlice);
            }


        }


        for(int i = 0 ; i < buckets.size() ; i++ ){
            System.out.println("hello");
            System.out.println(buckets.get(i));
        }
    }

    @Override
    void execute() {

    }
}
