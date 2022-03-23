package sample;

import java.util.ArrayList;

public class SimulatedAnnealing {
    ArrayList<?> finalSolution;
    ArrayList<String> energyList = new ArrayList<>();
    ArrayList<Integer> iterList = new ArrayList<>();
    float delta;

    <T> void simulatedAnnealing(Algorithm a1, float tMax, float tMin, float initialOptimizationFactor, float coolingRate) {
        System.out.println("initial opt" + initialOptimizationFactor);

        float tCur = tMax; //tCur is the current temperature at a given step
        float currentOptimizationFactor = initialOptimizationFactor;
        float lastAcceptedOpti = initialOptimizationFactor;
        ArrayList<?> lastAcceptedSolution = new ArrayList<>(); // we have to save the old solution if we dont pick any in the if statements since .execute() always produces a new solution.

        int i = 0;

        while (tCur > tMin){
            i++;
            currentOptimizationFactor = a1.optimizationFactor;
            a1.solution = lastAcceptedSolution;
            a1.optimizationFactor = lastAcceptedOpti;
            a1.execute();
            ArrayList<Object> currentSolution = new ArrayList<>(a1.solution);

            delta = currentOptimizationFactor - a1.optimizationFactor;

            if(delta < 0){ //direction of < changes whether you want to minimize or maximize
                a1.solution = currentSolution;                       //Choose the next solution as the current solution.
                a1.optimizationFactor = currentOptimizationFactor;
                iterList.add(i);
                energyList.add(String.valueOf(a1.optimizationFactor));

                lastAcceptedSolution = a1.solution;
                lastAcceptedOpti = currentOptimizationFactor;
                //TODO analyse the exponential seems very high
            } else if ( Math.exp((-delta)/tCur) > Math.random())  {
                iterList.add(i);
                energyList.add(String.valueOf(a1.optimizationFactor));
                lastAcceptedSolution = currentSolution; //Choose the next solution as the current solution.
                lastAcceptedOpti = a1.optimizationFactor;
                a1.optimizationFactor = currentOptimizationFactor;
            }

            tCur *= coolingRate;
            //System.out.printf(String.valueOf(a1.solution));
            //System.out.println("Temp after step: " + tCur);

            if (tCur < 1){
                System.out.println("re heat");
                tCur += tMax;
            }

            if(i > 25000) break;
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
        finalSolution = a1.solution;
    }
}