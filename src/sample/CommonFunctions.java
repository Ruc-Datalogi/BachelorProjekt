package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommonFunctions {

    protected CommonFunctions(){}

    public static ArrayList<Integer> randomIntegerList(int size) {
        ArrayList<Integer> tempList = new ArrayList<>(size);
        ArrayList<Integer> outputList = new ArrayList<>(size);
        Random r = new Random();
        for (int i = 1 ; i <= size ; i++){
            tempList.add(i);
        }
        while (tempList.size() > 0){
            int index = r.nextInt(tempList.size());
            outputList.add(tempList.get(index));
            tempList.remove(index);
        }
        return outputList;
    }

    public static List<Integer> getCommon(List<Integer> rightPosSlice, List<Integer> rightNegiSlice) {
        List<Integer> rightCommon = new ArrayList<>(rightPosSlice);
        rightCommon.retainAll(rightNegiSlice);
        return rightCommon;
    }
}
