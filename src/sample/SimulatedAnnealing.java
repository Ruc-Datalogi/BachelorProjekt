package sample;

import java.io.IOException;
import java.util.ArrayList;

public class SimulatedAnnealing {
    ArrayList<?> finalSolution;
    ArrayList<String> energyList = new ArrayList<>();
    ArrayList<Integer> iterList = new ArrayList<>();
    float delta;

    <T> void simulatedAnnealing(Algorithm a1, float tMax, float tMin, float initialOptimizationFactor, float coolingRate) throws IOException {
        System.out.println("initial opt" + initialOptimizationFactor);

        float tCur = tMax; //tCur is the current temperature at a given step
        float currentOptimizationFactor;

        int i = 0;
        a1.execute();

        while (tCur > tMin){
            i++;
            currentOptimizationFactor = a1.optimizationFactor;
            ArrayList<Object> currentSolution = new ArrayList<>(a1.solution);
            a1.execute();

            delta = a1.optimizationFactor -  currentOptimizationFactor ;

            if(delta < 0){ //direction of < changes whether you want to minimize or maximize
                iterList.add(i);
                energyList.add(String.valueOf(a1.optimizationFactor));

            } else if ( Math.exp((-delta)/tCur) > Math.random())  {
                iterList.add(i);
                energyList.add(String.valueOf(a1.optimizationFactor));
                a1.optimizationFactor = currentOptimizationFactor;


            } else { //if we dont pick a new solution in the delta block, and the temp block, we need to set the algorithms solution set to the old solution.
                a1.solution = currentSolution;
                a1.optimizationFactor = currentOptimizationFactor;
            }



            tCur *= coolingRate;
            //System.out.printf(String.valueOf(a1.solution));
            //System.out.println("Temp after step: " + tCur);


            if (tCur < 1){
                tCur = 200000;
            }
            System.out.println(i);
            if(i > 200000) break;
        }




        System.out.println("New solution size " + a1.solution.size());
        System.out.println("Iterations " + i);
        int bucketSize = iterList.size() / 500;
        ArrayList<String> tempEnergyList = new ArrayList<>();
        ArrayList<Integer> tempIterList = new ArrayList<>();

        System.out.println(iterList.size());
        for(int j = 0 ; j < iterList.size() -1 ; j += bucketSize) {
            tempEnergyList.add(energyList.get(j));
            tempIterList.add(iterList.get(j));
        }

        State.getState().iterList = tempIterList.toString();
        State.getState().energyList = tempEnergyList.toString();
        CSVWriter.getCsvWriter().writeLists(iterList,energyList);
        finalSolution = a1.solution;
    }
}