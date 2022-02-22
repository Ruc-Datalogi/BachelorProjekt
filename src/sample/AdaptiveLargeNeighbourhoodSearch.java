package sample;

import java.io.Serial;
import java.util.*;

public class AdaptiveLargeNeighbourhoodSearch {

    Algorithm[] repairAlgorithms;
    Algorithm[] destroyAlgorithms;

    float weightNewBest = 0.1f;         //How much the probability of picking a given algorithm increases if we've found the best solution
    float weightBetterSol = 0.05f;      //if the solution we've found is better than the current one
    float weightAccept = 0.025f;        //if we accept the solution
    float weightDeny = 0.0125f;         //if we deny the solution

    boolean boolNewBest, boolNewBetterSol, boolAccept, boolDeny;


    AdaptiveLargeNeighbourhoodSearch(ArrayList<?> initialConfiguration, Algorithm[] repairAlgorithms, Algorithm[] destroyAlgorithms){
        this.destroyAlgorithms = destroyAlgorithms;
        this.repairAlgorithms  = repairAlgorithms;

        ArrayList<?> curConfiguration = initialConfiguration;
        float curOptimisationFactor   = 0;

        ArrayList<?> bestConfiguration = initialConfiguration;
        float bestOptimisationFactor   = Integer.MAX_VALUE;

        initProbabilities(repairAlgorithms,destroyAlgorithms); //equally distribute the chance of picking a given algorithm


        while(true){
            //select destroy and repair methods
            Algorithm randDestroyAlgorithm = (Algorithm) destroyProbabilityMap.next();
            Algorithm randRepairAlgorithm  = (Algorithm) repairProbabilityMap.next();

            ArrayList<?> newConfiguration = destroy(curConfiguration,randDestroyAlgorithm); //weighted-randomly calls a destory algorithm
                         newConfiguration = repair(curConfiguration,randRepairAlgorithm);   //calls the repair algorithm
            float newOptimisationFactor   = randRepairAlgorithm.optimizationFactor;

            if(accept(newConfiguration, newOptimisationFactor ,curConfiguration, curOptimisationFactor)){
                curConfiguration      = newConfiguration;
                curOptimisationFactor = newOptimisationFactor;

                boolAccept = true;
                boolDeny   = false;
            } else {
                boolDeny = true;
            }

            if(newOptimisationFactor < curOptimisationFactor){ //if the configuration that we've found so far is the best, we save the configuration.
                bestConfiguration      = newConfiguration;
                bestOptimisationFactor = newOptimisationFactor;

                boolNewBest = true;
            }

            updateWeights(randRepairAlgorithm,randDestroyAlgorithm); //here we will update the probabilities of which algorithms we pick
        }
    }

    ProbabilityMap<Algorithm> repairProbabilityMap;
    ProbabilityMap<Algorithm> destroyProbabilityMap;

    /**
     * Adds all the repair and destroy algorithms to a probability map with equally assigned probabilities
     * @param repairAlgorithms
     * @param destroyAlgorithms
     */
    void initProbabilities(Algorithm[] repairAlgorithms, Algorithm[] destroyAlgorithms){
        ProbabilityMap<Algorithm> pMap = new ProbabilityMap<>();

        for (Algorithm alg: repairAlgorithms) {
            pMap.put(1d/repairAlgorithms.length,alg);
        }

        repairProbabilityMap = pMap;
        pMap = new ProbabilityMap<>();

        for (Algorithm alg: destroyAlgorithms) {
            pMap.put(1d/repairAlgorithms.length,alg);
        }

        destroyProbabilityMap = pMap;
    }

    ArrayList<?> destroy (ArrayList<?> configuration,Algorithm pickedAlgorithm){

        pickedAlgorithm.solution = configuration;
        pickedAlgorithm.execute();

        return pickedAlgorithm.solution;
    }

    ArrayList<?> repair (ArrayList<?> configuration,Algorithm pickedAlgorithm){

        pickedAlgorithm.solution = configuration;
        pickedAlgorithm.execute();

        return pickedAlgorithm.solution;
    }


    /**
     * Updates the weights of the two picked algorithms based on the weight parameters.
     * @param repairAlgorithm
     * @param destroyAlgorithm
     */
    void updateWeights(Algorithm repairAlgorithm, Algorithm destroyAlgorithm){
        float weight = 0;
        if (boolDeny) weight = weightDeny;
        if (boolAccept) weight = weightAccept;
        if (boolNewBetterSol) weight = weightBetterSol;
        if (boolNewBest) weight = weightNewBest;

        repairProbabilityMap.clear();
        repairProbabilityMap.put(1d/repairAlgorithms.length+weight,repairAlgorithm);

        destroyProbabilityMap.clear();
        destroyProbabilityMap.put(1d/destroyAlgorithms.length+weight,destroyAlgorithm);
    }

    /***
     * Currently the accept algorithm only accepts improvements however we can explore other space if we also randomly take a bad solution.
     * @param newConfiguration
     * @param curConfiguration
     * @return
     */
    private boolean accept(ArrayList<?> newConfiguration, float newOptimisationFactor, ArrayList<?> curConfiguration, float curOptimisationFactor){
        return newOptimisationFactor < curOptimisationFactor;
    }


}

class ProbabilityMap<T> extends TreeMap<Double,T> {
    @Serial
    private static final long serialVersionUID = 1L;

    public static Random random = new Random();
    public double sumOfProbabilities;

    public Map.Entry<Double, T> next() {
        return ceilingEntry(random.nextDouble() * sumOfProbabilities);
    }

    @Override
    public T put(Double key, T value) {
        return super.put(sumOfProbabilities += key, value);
    }
}