package sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SimulatedAnnealing {
    ArrayList<?> finalSolution;
    ArrayList<String> energyList = new ArrayList<>();
    ArrayList<Integer> iterList = new ArrayList<>();
    public int i = 0;

    float delta;

    <T> void simulatedAnnealing(Algorithm a1, float tMax, float tMin, float coolingRate) throws IOException {
        float tCur = tMax; //tCur is the current temperature at a given step
        float currentOptimizationFactor;

        float bestSoln = Integer.MAX_VALUE;
        ArrayList<?> bestSolution = new ArrayList<>();
        a1.execute();

        while (tCur > tMin){
            i++;
            currentOptimizationFactor = a1.optimizationFactor;
            ArrayList<Object> currentSolution = new ArrayList<>(a1.solution); //save old solution, so if we dont pick a new solution, we can revert to this
            a1.execute();

            delta = a1.optimizationFactor -  currentOptimizationFactor;


            if(delta < 0){ //direction of < changes whether you want to minimize or maximize. if better we pick solution
                iterList.add(i);
                energyList.add(String.valueOf(a1.optimizationFactor));
            } else if (delta != 0 && Math.exp((-delta)/tCur) > Math.random())  { //if lucky we should pick this
                //System.out.println(Math.exp((-delta)/tCur) + " " + Math.random());
                iterList.add(i);
                energyList.add(String.valueOf(a1.optimizationFactor));
            } else { //if we dont pick a new solution in the delta block, and the temp block, we need to set the algorithms solution set to the old solution.
                a1.solution = currentSolution;
                a1.optimizationFactor = currentOptimizationFactor;
            }

            tCur *= coolingRate;

            if (bestSoln > a1.optimizationFactor) {
                bestSoln = a1.optimizationFactor;
                bestSolution = a1.solution;
            }

            //System.out.println(a1.optimizationFactor);
            if(i > 65000) {
                //System.out.println("breaking");
                break;
            }
        }

        //TODO make sure we have bestsoln here.
        System.out.println("best soln found " + bestSoln);
        finalSolution = bestSolution;
    }
}