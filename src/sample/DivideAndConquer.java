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
    int bestArea = Integer.MAX_VALUE;
    int SAiterations = 40000;
    float SAcoolingRate = 0.8f;


    public DivideAndConquer (ArrayList<Integer> positive, ArrayList<Integer> negative, ArrayList<Module> modules) {
        this.positive = positive;
        this.negative = negative;
        this.modules = modules;
        for(Module mod : modules) {
            mod.realdId = mod.id;
        }
        this.bucketSize = (int) Math.sqrt(getClosestPerfectSquare(modules.size()));
    }

    public ArrayList<Bucket> generateBucketsBasedOnSize(ArrayList<Module> moduleArrayList) {
        ArrayList<Bucket> outPutBuckets = new ArrayList<>();
        this.bucketSize = (int) Math.sqrt(getClosestPerfectSquare(moduleArrayList.size()));
        int index = 1;
        Bucket currBucket = new Bucket(); // Fill up the buckets with the size of amount modules sqrt

        Comparator<Module> comparator = new Comparator<Module>() {
            @Override
            public int compare(Module o1, Module o2) {
                return Integer.compare(o1.width*o1.height , o2.width*o2.height);
            }
        };

        if(bucketSize != 1) {
            for(Module mod : moduleArrayList) {
                mod.id = index;
                currBucket.modules.add(mod);
                index++;
                if (currBucket.modules.size() == bucketSize) {
                    outPutBuckets.add(currBucket);
                    currBucket = new Bucket();
                    index = 1;
                }
            }
            if(currBucket.modules.size() != 0 ) outPutBuckets.add(currBucket);
        } else {
            currBucket.modules.addAll(moduleArrayList);
            outPutBuckets.add(currBucket);
        }

        return outPutBuckets;
    }

    public ArrayList<Bucket> generateBuckets(ArrayList<Module> moduleArrayList){
        ArrayList<Module> rectangles = new ArrayList<>();
        ArrayList<Module> squares = new ArrayList<>();
        ArrayList<Bucket> outPutBuckets = new ArrayList<>();
        this.bucketSize = (int) Math.sqrt(getClosestPerfectSquare(moduleArrayList.size()));
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
        sa.iterations = SAiterations;
        sa.simulatedAnnealing(p, 10000,0,SAcoolingRate);
        return p;
    }

    public ArrayList<Module> calculateSuperModules(ArrayList<SubProblem> subProblems) {
        ArrayList<Module> superModules = new ArrayList<>();
        int i = 1;
        for(SubProblem s : subProblems) {
            Module m = new Module(i, s.width, s.height);
            m.subProblem = s;
            m.subModules.addAll(s.bucket.modules);
            superModules.add(m);
            i++;
        }
        return superModules;
    }

    public SequencePairs calculatePlacement() throws IOException {
        Module superModule = new Module(-1,-1,-1);
        ArrayList<Bucket> buckets = new ArrayList<>();
        buckets.addAll(generateBuckets(modules));
        int size = Integer.MAX_VALUE;

        while(size != 1) {
            ArrayList<SubProblem> subProblems = new ArrayList<>();
            ArrayList<Module> superModules = new ArrayList<>();
            for (Bucket b : buckets) {
                subProblems.add(new SubProblem(b));
            }
            for (SubProblem subProblem : subProblems) {
                calculateSubProblem(subProblem);
            }
            superModules.addAll(calculateSuperModules(subProblems));
            buckets.clear();
            buckets.addAll(generateBuckets(superModules));
            size = superModules.size();
            if (size == 1) {
                superModule = superModules.get(0);
            }
        }
        bestArea = superModule.width * superModule.height;
        ArrayList<Integer> finalPositive = new ArrayList<>();
        ArrayList<Integer> finalNegative = new ArrayList<>();

        traverseTreePositive(superModule, finalPositive);
        traverseTreeNegative(superModule, finalNegative);
        for(Module mod : modules) {
            mod.id = mod.realdId;
        }

        SequencePairs sequencePairs = new SequencePairs(finalPositive, finalNegative, modules);
        sequencePairs.calculatePlacementTable();
        return sequencePairs;
    }

    private void traverseTreePositive(Module m, ArrayList<Integer> bestPostive) {
        for(Integer i : m.subProblem.bestPositive) {
            traverseTreePositive(findModule(m.subModules, i), bestPostive);
        }
        if(m.subProblem.bestPositive.size() == 0) {
            bestPostive.add(m.realdId);
        }
    }

    private void traverseTreeNegative(Module m, ArrayList<Integer> bestNegative) {
        for(Integer i : m.subProblem.bestNegative) {
            traverseTreeNegative(findModule(m.subModules, i), bestNegative);
        }
        if(m.subProblem.bestNegative.size() == 0) {
            bestNegative.add(m.realdId);
        }
    }

    private Module findModule(ArrayList<Module> modules, int ID) {
        for(Module mod : modules) {
            if(mod.id == ID) {
                return mod;
            }
        }
        return null;
    }


    private boolean isPerfect(int N) {
        if ((Math.sqrt(N) - Math.floor(Math.sqrt(N))) != 0)
            return false;
        return true;
    }

    private int getClosestPerfectSquare(int N) {
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
    int totalArea = 0;

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
    ArrayList<Integer> bestPositive = new ArrayList<>();
    ArrayList<Integer> bestNegative = new ArrayList<>();

    HashMap<Integer, Integer> mapPostive = new HashMap<>();
    HashMap<Integer, Integer> mapNegative = new HashMap<>();
    AdjanceyGraph thcg = new AdjanceyGraph();
    AdjanceyGraph tvcg = new AdjanceyGraph();
    HashSet<ArrayList<ArrayList<Integer>>> solutionSet = new HashSet<>();
    ArrayList<SubProblem> subProblems = new ArrayList<>();

    float bestOptimazitionFactor;
    int bestDist = Integer.MAX_VALUE;
    int width;
    int height;
    boolean updated = false;
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
            bestPositive.clear();
            bestNegative.clear();
            bestPositive.addAll(positive);
            bestNegative.addAll(negative);
            bestDist = (int) optimizationFactor;
        }
    }

    public void updateSequenceList() {
        updated = true;
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


