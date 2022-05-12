package sample;

import java.util.*;

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

    public static void swapInMap(HashMap<Integer, Integer> map, int id1 , int id2) {
        Integer tempValue = map.get(id1);
        map.put(id1, map.get(id2));
        map.put(id2, tempValue);
    }

    public static List<Integer> getCommon(List<Integer> rightPosSlice, List<Integer> rightNegiSlice) {
        List<Integer> rightCommon = new ArrayList<>(rightPosSlice);
        rightCommon.retainAll(rightNegiSlice);
        return rightCommon;
    }
    public static List<Integer> getIntersect(List<Integer> rightPosSlice, List<Integer> rightNegiSlice,int includeValue) {
        List<Integer> rightCommon = new ArrayList<>();
        Iterator<Integer> rightIt = rightPosSlice.iterator();
        while(rightIt.hasNext()){
            int v= rightIt.next();
            if (v==includeValue || rightNegiSlice.contains(v)){
                rightCommon.add(v);
            }
        }
        return rightCommon;
    }

    public static double calculateSD(ArrayList<Integer> arrayList) {
        int sum = 0;
        int standardDeviation = 0;
        int length = arrayList.size();
        for (Integer i : arrayList) {
            sum += i;
        }

        int mean = sum / length;
        for (int i : arrayList) {
            standardDeviation += Math.pow(i - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }

}
