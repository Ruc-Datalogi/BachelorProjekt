package sample;

import java.io.IOException;
import java.util.*;

import static sample.CommonFunctions.swapInMap;


// remember we dont save the init module id maybe do that
public class DivideAndConquer {

    ArrayList<Integer> positive = new ArrayList<>();
    ArrayList<Integer> negative = new ArrayList<>();
    ArrayList<Module> modules = new ArrayList<>();
    ArrayList<Module> finalModules = new ArrayList<>();
    SequencePairs finalFinalFinalTest;
    int depth = 0;
    int bucketSize;

    public DivideAndConquer (ArrayList<Integer> positive, ArrayList<Integer> negative, ArrayList<Module> modules) {
        this.positive = positive;
        this.negative = negative;
        this.modules = modules;
        System.out.println(modules.size());
        for(Module mod : modules) {
            mod.realdId = mod.id;
        }
        this.bucketSize = (int) Math.sqrt(getClosestPerfectSquare(modules.size()));
    }

    public ArrayList<Bucket> generateBuckets(ArrayList<Module> moduleArrayList){
        ArrayList<Module> rectangles = new ArrayList<>();
        ArrayList<Module> squares = new ArrayList<>();
        ArrayList<Bucket> outPutBuckets = new ArrayList<>();
        // bucketSize = (int) Math.sqrt(getClosestPerfectSquare(moduleArrayList.size()));
        int index = 1;
        if(bucketSize != 1 ) {
            for (Module mod : moduleArrayList) { // Check if module is a rectangle or a square
                if ((float) mod.height / (float) mod.width >= 2 || (float) mod.height / (float) mod.width <= 0.5) {
                    rectangles.add(mod);
                } else {
                    squares.add(mod);
                }
            }
            Bucket currBucket = new Bucket(); // Fill up the buckets with the size of amount modules sqrt
            currBucket.rectangular = true;
            for(Module mod : rectangles) {
                mod.id = index;
                currBucket.modules.add(mod);
                index++;
                if (currBucket.modules.size() == bucketSize) {
                    outPutBuckets.add(currBucket);
                    currBucket = new Bucket();
                    currBucket.rectangular = true;
                    index = 1;
                }
            }

            for(Module mod : squares) {
                mod.id = index;
                currBucket.modules.add(mod);
                index++;
                if (currBucket.modules.size() == bucketSize ) {
                    outPutBuckets.add(currBucket);
                    currBucket = new Bucket();
                    currBucket.rectangular = false;
                    index = 1;
                }
            }
            if(currBucket.modules.size() != 0 ) outPutBuckets.add(currBucket);
        } else {
            Bucket currBucket = new Bucket();
            currBucket.modules.addAll(moduleArrayList);
            outPutBuckets.add(currBucket);
        }

        return outPutBuckets;
    }

    public SubProblem calculateSubProblem(SubProblem p) throws IOException {
        SimulatedAnnealing sa = new SimulatedAnnealing();
        sa.simulatedAnnealing(p, 10000,0,0.9f);

        return p;
    }

    public ArrayList<Module> calculateSuperModules(ArrayList<SubProblem> subProblems) {
        ArrayList<Module> superModules = new ArrayList<>();
        int i = 1;
        depth++;
        for(SubProblem subProblem : subProblems) {
            Module m = new Module(i, subProblem.width, subProblem.height);
            m.subProblem = subProblem;
            m.depth = depth;
            m.subModules.addAll(subProblem.bucket.modules);
            superModules.add(m);
            i++;
        }
        return superModules;
    }

    int depthss = 0;
    public SequencePairs calculatePlacement() throws IOException {
        Module superModule = new Module(0,0,0);
        ArrayList<Bucket> buckets = new ArrayList<>();
        buckets.addAll(generateBuckets(modules));
        int size = Integer.MAX_VALUE;

        while(size != 1) {
            SubProblem currProblem = new SubProblem();
            ArrayList<SubProblem> subProblems = new ArrayList<>();
            for (Bucket b : buckets) {
                subProblems.add(new SubProblem(b));
            }

            for (SubProblem subProblem : subProblems) {
                calculateSubProblem(subProblem);
                currProblem.subProblems.add(subProblem);
            }

            ArrayList<Module> superModules = calculateSuperModules(subProblems); // TODO update bucket size :D
            buckets.clear();
            if (superModules.size() == 1)  System.out.println(superModules.get(0).width * superModules.get(0).height);
            buckets.addAll(generateBuckets(superModules));
            size = superModules.size();
            if (size == 1) superModule = superModules.get(0);
        }
        ArrayList<SubProblem> lastSubproblems = new ArrayList<>();
        ArrayList<Integer> finalPositive = new ArrayList<>();
        ArrayList<Integer> finalNegative = new ArrayList<>();
        ArrayList<Module> finalModules = new ArrayList<>();
        findSubproblems(superModule, lastSubproblems);

        for(SubProblem subProblem : lastSubproblems) {
            subProblem.updateSequenceList();
            finalPositive.addAll(subProblem.positive);
            finalNegative.addAll(subProblem.negative);
            finalModules.addAll(subProblem.bucket.modules);
        }
        for(Module mod : finalModules) {
            mod.id = mod.realdId;
            mod.above.clear();
            mod.below.clear();
            mod.rightOf.clear();
            mod.leftOf.clear();
        }

        System.out.println(finalPositive);
        System.out.println(finalNegative);
        System.out.println(finalModules);
        SequencePairs sequencePairs = new SequencePairs(finalPositive, finalNegative, finalModules);
        sequencePairs.calculatePlacementTable();

        return sequencePairs;
    }

    private ArrayList<SubProblem> findSubproblems(Module m, ArrayList<SubProblem> lastSubproblems) {
            for(Module mod : m.subModules) {
                if(mod.realdId > 0) {

                    lastSubproblems.add(m.subProblem);
                    break;
                }
            }
            for(Module mod : m.subModules) {
                findSubproblems(mod, lastSubproblems);
            }
        return lastSubproblems;
    }

    private boolean isPerfect(int N){
        if ((Math.sqrt(N) - Math.floor(Math.sqrt(N))) != 0)
            return false;
        return true;
    }

    private int getClosestPerfectSquare(int N)
    {
        if (isPerfect(N)) {
            return N;
        }
        int aboveN = -1, belowN = -1, n1;
        n1 = N + 1;
        while (true) {
            if (isPerfect(n1)) {
                aboveN = n1;
                break;
            }
            else
                n1++;
        }

        n1 = N - 1;
        while (true) {
            if (isPerfect(n1)) {
                belowN = n1;
                break;
            }
            else
                n1--;
        }
        // Variables to store the differences
        int diff1 = aboveN - N;
        int diff2 = N - belowN;

        if (diff1 > diff2)
            return belowN;
        else
            return aboveN;
    }

}

class Bucket {
    public ArrayList<Module> modules = new ArrayList<>();
    boolean rectangular;

    @Override
    public String toString() {
        return "Bucket{" +
                "modules=" + modules +
                ", rectangular=" + rectangular +
                '}';
    }

}


class SubProblem extends Algorithm{
    Bucket bucket;
    ArrayList<Integer> positive = new ArrayList<>();
    ArrayList<Integer> negative = new ArrayList<>();
    HashMap<Integer, Integer> mapPostive = new HashMap<>();
    HashMap<Integer, Integer> mapNegative = new HashMap<>();
    AdjanceyGraph thcg = new AdjanceyGraph();
    AdjanceyGraph tvcg = new AdjanceyGraph();
    HashSet<ArrayList<ArrayList<Integer>>> solutionSet = new HashSet<>();
    ArrayList<SubProblem> subProblems = new ArrayList<>();
    static int realId = 1;

    float bestOptimazitionFactor;
    int bestDist = Integer.MAX_VALUE;
    int width;
    int height;

    SubProblem () {

    }

    SubProblem (Bucket b) {
        this.bucket = b;
        for (Module mod : bucket.modules ) {
            positive.add(mod.id);
            negative.add(mod.id);
        }
        for(int i = 0 ; i < positive.size() ; i++) {
            mapPostive.put(positive.get(i), i);
        }
        for(int i = 0 ; i < negative.size() ; i++) {
            mapNegative.put(negative.get(i), i);
        }
        calculateSequencePlacements();
        calculateGraph();
    }

    private void calculateSequencePlacements() {
        for(Module mod : bucket.modules) {
            int posiPosition = mapPostive.get(mod.id);
            mod.setPositiveIndex(posiPosition);
            int negiPositon = mapNegative.get(mod.id);
            mod.setNegativeIndex(negiPositon);

            List<Integer> leftPosSlice   = positive.subList(0, posiPosition);
            List<Integer> rightPosSlice  = positive.subList(posiPosition+1, positive.size() );
            List<Integer> leftNegiSlice  = negative.subList(0, negiPositon);
            List<Integer> rightNegiSlice = negative.subList(negiPositon+1, negative.size() );

            mod.leftOf  = CommonFunctions.getCommon(leftPosSlice, leftNegiSlice);
            mod.rightOf = CommonFunctions.getCommon(rightPosSlice, rightNegiSlice);
            mod.above   = CommonFunctions.getCommon(rightNegiSlice,leftPosSlice);
            mod.below   = CommonFunctions.getCommon(leftNegiSlice,rightPosSlice);
        }
    }

    private void calculateGraph() {
        thcg.vertices.clear(); // Refresh the vertices of the graph
        tvcg.vertices.clear();
        Vertex sourceH = new Vertex(0,-1);
        Vertex targetH = new Vertex(0, -2);
        Vertex sourceV = new Vertex(0, -1);
        Vertex targetV = new Vertex(0, -2);

        for(Module mod : bucket.modules) {
            Vertex vertexH = new Vertex(mod.width, mod.id);
            thcg.vertices.add(vertexH);

            Vertex vertexV = new Vertex(mod.height, mod.id);
            tvcg.vertices.add(vertexV);
        }

        SequencePairs.createTempGraph(thcg, bucket.modules, sourceH, targetH, true);
        SequencePairs.createTempGraph(tvcg, bucket.modules, sourceV, targetV, false);

        thcg.vertices.add(sourceH);
        thcg.vertices.add(targetH);
        tvcg.vertices.add(sourceV);
        tvcg.vertices.add(targetV);

        int dist1=sourceH.DFS_New();
        int dist = dist1;
        int dist2 = sourceV.DFS_New();
        super.optimizationFactor = dist2*dist; // the variable we optimise for.


        if (dist2*dist < super.optimizationFactor){
            bestOptimazitionFactor = dist2*dist;
        }

        if (optimizationFactor < bestDist) {
            width = dist1;
            height = dist2;
            bestDist = (int) optimizationFactor;
        }
    }

    public void updateSequenceList() {
        for(int i = 1 ; i < bucket.modules.size() + 1 ; i++) {
            positive.set(positive.indexOf(i), bucket.modules.get(i-1).realdId);
            negative.set(negative.indexOf(i), bucket.modules.get(i-1).realdId);
        }
    }

    @Override
    public String toString() {
        return "SubProblem{" +
                "bucket=" + bucket +
                ", thcg=" + thcg +
                ", tvcg=" + tvcg +
                 +'}' ;
    }

    @Override
    void execute() {
        //int swap1;
        Random random = new Random();
        int randomIndex1 = random.nextInt(positive.size());
        int randomIndex2 = random.nextInt(negative.size());

        int id1 = positive.get(randomIndex1);
        int id2 = positive.get(randomIndex2);

        switch (random.nextInt(0, 2)) {
            case 0 -> { // Dual swap
                Collections.swap(positive, randomIndex1, randomIndex2);
                swapInMap(mapPostive, id1, id2);
                Collections.swap(negative, mapNegative.get(id1), mapNegative.get(id2));
                swapInMap(mapNegative, id1, id2);
            }
            case 1 -> { // Single Swap Positive
                Collections.swap(positive, randomIndex1, randomIndex2);
                swapInMap(mapPostive, id1, id2);
            }
            case 2 -> { // Single Swap Negative
                Collections.swap(negative, randomIndex1, randomIndex2);
                swapInMap(mapNegative, id1, id2);
            }
        }

        ArrayList<ArrayList<Integer>> solutions = new ArrayList<>();
        solutions.add(positive);
        solutions.add(negative);
        this.solution = solutions;

        if (solutionSet.add(solutions)) {
            this.calculateSequencePlacements();
            this.calculateGraph();
        }
    }
}


