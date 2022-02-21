package sample;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequencePairs extends Algorithm{
    ArrayList<Integer> positive = new ArrayList<>();
    ArrayList<Integer> negative = new ArrayList<>();
    ArrayList<Module> modules = new ArrayList<>();

    public SequencePairs(ArrayList<Integer> positive, ArrayList<Integer> negative, ArrayList<Module> modules) {
        this.positive = positive;
        this.negative = negative;
        this.modules = modules;
    }

    public void calculatePlacementTable(){

        for(Module mod : modules){

                int posiPosition = positive.indexOf(mod.id);
                int negiPositon = negative.indexOf(mod.id);
                List<Integer> leftPosSlice = positive.subList(0, posiPosition);

                List<Integer> rightPosSlice = positive.subList(posiPosition+1, positive.size() );
                List<Integer> leftNegiSlice = negative.subList(0, negiPositon);
                List<Integer> rightNegiSlice = negative.subList(negiPositon+1, negative.size() );
                /*
                System.out.println(leftPosSlice);
                System.out.println(rightPosSlice);
                System.out.println(leftNegiSlice);
                System.out.println(rightNegiSlice);

                 */
                List<Integer> rightCommon = new ArrayList<>(rightPosSlice);
                rightCommon.retainAll(rightNegiSlice);
                mod.RightOf = rightCommon;

                System.out.println(mod.id  + " right of list: " + mod.RightOf.toString());

        }
    }

    @Override
    void execute() {
        System.out.println(":)");
    }

}
class Module{
    int id;
    int width;
    int height;
    List<Integer> RightOf= new ArrayList<>();
    List<Integer> LeftOf= new ArrayList<>();
    List<Integer> Above= new ArrayList<>();
    List<Integer> Below= new ArrayList<>();

    public Module(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }
}