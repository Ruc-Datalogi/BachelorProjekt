package sample;

import java.util.ArrayList;

public class SimulatedAnnealing {
    ArrayList<?> finalSolution;

    <T> void simulatedAnnealing(Algorithm a1, float tMax, float tMin, ArrayList<T> initialConfiguration, float initialOptimizationFactor, float coolingRate) {
        float tCur = tMax; //tCur is the current temperature at a given step
        a1.optimizationFactor = initialOptimizationFactor;
        a1.solution = initialConfiguration;
        float delta;

        int i = 0;

        while (tCur > tMin){
            i++;
            a1.execute();
            float newOptimizationFactor = a1.optimizationFactor;
            ArrayList<Object> newSolution = new ArrayList<>(a1.solution);

            delta = a1.optimizationFactor - newOptimizationFactor;

            if(delta < 0){ //direction of < changes whether you want to minimize or maximize

                //System.out.println("Entered if statement 2");
                a1.solution = newSolution;                       //Choose the next solution as the current solution.
                a1.optimizationFactor = newOptimizationFactor;

            } else if ( Math.exp(delta/tCur) > Math.random())  {
                //System.out.println("Entered if statement 1");
                a1.solution = newSolution;                        //Choose the next solution as the current solution.
                a1.optimizationFactor = newOptimizationFactor;
            }

            tCur *= coolingRate;
            //System.out.printf(String.valueOf(a1.solution));
            //System.out.println("Temp after step: " + tCur);
        }
        System.out.println("New solution size " + a1.solution.size());
        System.out.println("Iterations " + i);
        finalSolution = a1.solution;
    }
}
