package sample;

import sample.Algorithm;
import sample.CSVWriter;
import sample.State;

import java.io.IOException;
import java.util.ArrayList;

public class BoundedAnnealing {
    ArrayList<?> finalSolution;
    ArrayList<String> energyList = new ArrayList<>();
    ArrayList<Integer> iterList = new ArrayList<>();
    float delta;


    void simulatedAnnealing(Algorithm a1, float tMax, float bound, float tMin, float initialOptimizationFactor, float coolingRate) throws IOException {
        float tCur = tMax; //tCur is the current temperature at a given step
        float currentOptimizationFactor;

        int i = 0;
        a1.execute();
        int timeSinceLastPick = 0;

        while (tCur > tMin){
            i++;
            currentOptimizationFactor = a1.optimizationFactor;
            ArrayList<Object> currentSolution = new ArrayList<>(a1.solution);
            a1.execute();

            delta = a1.optimizationFactor -  currentOptimizationFactor ;

            if(delta < (5/bound*tCur)){ //direction of < changes whether you want to minimize or maximize

                iterList.add(i);
                energyList.add(String.valueOf(a1.optimizationFactor));
                timeSinceLastPick = 0;

            } else { //if we dont pick a new solution in the delta block, and the temp block, we need to set the algorithms solution set to the old solution.
                a1.solution = currentSolution;
                a1.optimizationFactor = currentOptimizationFactor;
            }

            timeSinceLastPick++;
            tCur *= coolingRate;
            //energyList.add(String.valueOf(tCur));
            //energyList.add(String.valueOf(5/bound*tCur));
            //iterList.add(i);

            //System.out.printf(String.valueOf(a1.solution));
            //System.out.println("Temp after step: " + tCur);

            if (timeSinceLastPick > 200){
                tCur += 12500;
            }

            //  System.out.println(i);
            if(i > 50000) break;
        }

        System.out.println("New solution size " + a1.solution.size());
        System.out.println("Iterations " + i);
        int bucketSize = iterList.size() / 20;
        ArrayList<String> tempEnergyList = new ArrayList<>();
        ArrayList<Integer> tempIterList = new ArrayList<>();


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