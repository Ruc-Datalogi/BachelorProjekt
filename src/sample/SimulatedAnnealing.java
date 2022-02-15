package sample;

import javax.swing.plaf.basic.DefaultMenuLayout;
import java.util.ArrayList;

public class SimulatedAnnealing {
    ArrayList<?> finalSolution;
    ArrayList<String> energyList = new ArrayList<>();
    ArrayList<Integer> iterList = new ArrayList<>();

    <T> void simulatedAnnealing(Algorithm a1, float tMax, float tMin, float initialOptimizationFactor, float coolingRate) {
        float tCur = tMax; //tCur is the current temperature at a given step
        a1.optimizationFactor = initialOptimizationFactor;
        float delta;

        int i = 0;

        while (tCur > tMin){
            i++;
            a1.execute();
            float newOptimizationFactor = a1.optimizationFactor;
            ArrayList<Object> newSolution = new ArrayList<>(a1.solution);

            delta = a1.optimizationFactor - newOptimizationFactor;
            System.out.println(tCur);

            if(delta < 0){ //direction of < changes whether you want to minimize or maximize
                //System.out.println("Entered if statement 2");
                a1.solution = newSolution;                       //Choose the next solution as the current solution.
                a1.optimizationFactor = newOptimizationFactor;

            } else if ( Math.exp(delta/tCur) > Math.random())  {
                //System.out.println("Entered if statement 1");
                tCur += 10;
                a1.solution = newSolution;                        //Choose the next solution as the current solution.
                a1.optimizationFactor = newOptimizationFactor;
            }

            tCur *= coolingRate;
            //System.out.printf(String.valueOf(a1.solution));
            //System.out.println("Temp after step: " + tCur);
            energyList.add(String.valueOf(a1.optimizationFactor));
            iterList.add(i);

            if ( 500000 < i ) {
                break;
            }
        }
        System.out.println("New solution size " + a1.solution.size());
        System.out.println("Iterations " + i);
        int bucketSize = (int) iterList.size() / 500;
        ArrayList<String> tempEnergyList = new ArrayList<>();
        ArrayList<Integer> tempIterList = new ArrayList<>();
        for(int j = 0 ; j < iterList.size() -1 ; j += bucketSize) {
            tempEnergyList.add(energyList.get(j));
            tempIterList.add(iterList.get(j));
        }

        State.getState().iterList = tempIterList.toString();
        State.getState().energyList = tempEnergyList.toString();
        finalSolution = a1.solution;
    }
}
