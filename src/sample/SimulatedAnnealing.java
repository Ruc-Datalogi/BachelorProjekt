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

        float previousOptimizationFactor = initialOptimizationFactor;

        int i = 0;

        while (tCur > tMin){
            i++;
            a1.execute();
            ArrayList<Object> newSolution = new ArrayList<>(a1.solution);

            delta = a1.optimizationFactor - previousOptimizationFactor;
            previousOptimizationFactor = a1.optimizationFactor;
            System.out.println("Temperature is " + tCur + " min is: " + tMin);

            System.out.println("Delta " + delta);

            if(delta < 0){ //direction of < changes whether you want to minimize or maximize
                a1.solution = newSolution;                       //Choose the next solution as the current solution.
                a1.optimizationFactor = previousOptimizationFactor;
                //System.out.println("picked good");

                //TODO analyse the exponential seems very high
            } else if ( Math.exp((-delta)/tCur) > Math.random())  {

                System.out.println("e is " + Math.exp((-delta)/tCur));

                a1.solution = newSolution;                        //Choose the next solution as the current solution.
                a1.optimizationFactor = previousOptimizationFactor;
            }

            tCur *= coolingRate;
            //System.out.printf(String.valueOf(a1.solution));
            //System.out.println("Temp after step: " + tCur);
            energyList.add(String.valueOf(a1.optimizationFactor));
            iterList.add(i);
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
