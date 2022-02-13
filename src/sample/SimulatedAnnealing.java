package sample;

import java.util.ArrayList;

public class SimulatedAnnealing {

    //e represents energy and is what we either want to minimize or maximize.
    private float e;
    private float deltaE;
    private float coolingRate; //how much does tCur change per step?

    void simulatedAnnealing(Algorithm a1, int tMax, int tMin, ArrayList<Object> initialConfiguration, float initialEnergy) {
        int tCur = tMax; //tCur is the current temperature at a given step.
        ArrayList<Object> c = initialConfiguration;
        e = initialEnergy;

        while (tCur > tMin){
            e = a1.optimizationFactor; //is executed before we calculate the new energy

            a1.execute();
            ArrayList<Object> nextConfiguration = a1.solution;
            float newE = a1.optimizationFactor;

            deltaE = e - newE;

            if(deltaE > 0){


            }

        }
    }
}
