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
        int amntDelta = 0;
        int amntRand = 0;

        while (tCur > tMin){
            //System.out.println(tCur + " " + tMin);
            i++;
            currentOptimizationFactor = a1.optimizationFactor;
            ArrayList<Object> currentSolution = new ArrayList<>(a1.solution); //save old solution, so if we dont pick a new solution, we can revert to this
            a1.execute();


            delta = a1.optimizationFactor -  currentOptimizationFactor;


            if(delta < 0){ //direction of < changes whether you want to minimize or maximize. if better we pick solution
                iterList.add(i);
                energyList.add(String.valueOf(a1.optimizationFactor));
            } else if (Math.exp((-delta)/tCur) > Math.random())  { //if lucky we should pick this
                iterList.add(i);
                energyList.add(String.valueOf(a1.optimizationFactor));
            } else { //if we dont pick a new solution in the delta block, and the temp block, we need to set the algorithms solution set to the old solution.
                a1.solution = currentSolution;
                a1.optimizationFactor = currentOptimizationFactor;
            }


            tCur *= coolingRate;
            //System.out.printf(String.valueOf(a1.solution));
            //System.out.println("Temp after step: " + tCur);



            if (tCur < 0.01){
                tCur = 200000;
            }

            if(i > 65000) break;
        }

        System.out.println("New solution size " + a1.solution.size());
        System.out.println("Iterations " + i);
        /*
        int bucketSize = iterList.size() / 500;
        ArrayList<String> tempEnergyList = new ArrayList<>();
        ArrayList<Integer> tempIterList  = new ArrayList<>();

        System.out.println(iterList.size());
        for(int j = 0 ; j < iterList.size() -1 ; j += bucketSize) {
            tempEnergyList.add(energyList.get(j));
            tempIterList.add(iterList.get(j));
        }

         */

        State.getState().iterList   = iterList.toString();
        State.getState().energyList = energyList.toString();
        CSVWriter.getCsvWriter().writeLists(iterList,energyList);
        finalSolution = a1.solution;
    }
}