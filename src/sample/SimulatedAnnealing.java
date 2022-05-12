package sample;

import java.io.IOException;
import java.util.ArrayList;

public class SimulatedAnnealing {
    ArrayList<?> finalSolution;
    ArrayList<String> energyList = new ArrayList<>();
    ArrayList<Integer> iterList = new ArrayList<>();
    public int i = 0;

    float delta;

    void simulatedAnnealing(Algorithm a1, float tMax, float tMin, float coolingRate) throws IOException {
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

            if (bestSoln > a1.optimizationFactor) { //memorize best solution
                bestSoln = a1.optimizationFactor;
                bestSolution = a1.solution;
            }

            if(delta < 0){ //direction of < changes whether you want to minimize or maximize. if better we pick solution
                iterList.add(i);
                energyList.add(String.valueOf(a1.optimizationFactor));
            } else if (delta != 0 && Math.exp((-delta)/tCur) > Math.random())  { //if lucky we should pick this
                iterList.add(i);
                energyList.add(String.valueOf(a1.optimizationFactor));

            } else { //if we dont pick a new solution in the delta block, and the temp block, we need to set the algorithms solution set to the old solution.
                a1.solution = currentSolution;
                a1.optimizationFactor = currentOptimizationFactor;
            }

            tCur *= coolingRate;
            if(tCur <= tMin){
                tCur+=tMax*((100000f-i)/100000f);
                //System.out.println("Adding " + tMax*((100000f-i)/100000f) + " to tcur i: " + i);
            }
            if(i>100000){
                //System.out.println("Breaking out of SA after 100k iterations");
                break;
            }

        }
        //TODO make sure we have bestsoln here. STOP SPAMMING MY CONSOLE @Mads
        //System.out.println("best soln found " + bestSoln);
        finalSolution = bestSolution;



    }
}