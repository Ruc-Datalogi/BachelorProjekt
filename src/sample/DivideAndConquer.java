package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


// remember we dont save the init module id maybe do that
public class DivideAndConquer extends Algorithm{

    ArrayList<Integer> positive = new ArrayList<>();
    ArrayList<Integer> negative = new ArrayList<>();
    ArrayList<Module> modules = new ArrayList<>();
    int bucketSize;

    public DivideAndConquer (ArrayList<Integer> positive, ArrayList<Integer> negative, ArrayList<Module> modules) {
        this.positive = positive;
        this.negative = negative;
        this.modules = modules;
        this.bucketSize = (int) Math.sqrt(getClosestPerfectSquare(modules.size()));
    }

    public void calculatePlacement() {
        ArrayList<Module> rectangles = new ArrayList<>();
        ArrayList<Module> squares = new ArrayList<>();
        ArrayList<Bucket> buckets = new ArrayList<>();
        for (Module mod : modules) { // Check if module is a rectangle or a square
            if ((float) mod.height / (float) mod.width >= 2 || (float) mod.height / (float) mod.width <= 0.5) {
                rectangles.add(mod);
            } else {
                squares.add(mod);
            }
        }

        Bucket currBucket = new Bucket(); // Fill up the buckets with the size of amount modules sqrt
        currBucket.rectangular = true;
        int index = 1;
        for(Module mod : rectangles) {
            mod.id = index;
            currBucket.modules.add(mod);
            index++;
            if (currBucket.modules.size() == bucketSize) {
                buckets.add(currBucket);
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
                buckets.add(currBucket);
                currBucket = new Bucket();
                currBucket.rectangular = false;
                index = 1;
            }
        }
        if(currBucket.modules.size() != 0 ) buckets.add(currBucket); // Make sure we get every module in a bucket
        ArrayList<SubProblem> subProblems = new ArrayList<>();

        for(int i = 0 ; i < buckets.size() ; i++ ){
           subProblems.add(new SubProblem(buckets.get(i)));
        }

        for (SubProblem subProblem : subProblems) {
            System.out.println(subProblem);
        }
    }

    private boolean isPerfect(int N){
        if ((Math.sqrt(N) - Math.floor(Math.sqrt(N))) != 0)
            return false;
        System.out.println("ininzc");
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



    @Override
    void execute() {

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
    int bestVerticalDistance = Integer.MAX_VALUE;
    int bestHorizontalDistance = Integer.MAX_VALUE;
    float bestOptimazitionFactor;
    int bestDist = Integer.MAX_VALUE;

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
            int posiPosition = mapPostive.get(mod.id); // TODO REMEMBER TO CHANGE INDEX OF HASH MAPS
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

    public void calculatePlacementTable() {

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
            //testBin = TEMPgenerateCoordinatesForModules(thcg, tvcg, dist, dist2);
            //bestBin = testBin;
            bestDist = (int) optimizationFactor;
            PrimaryWindow.changeDebugMessage("Best (" + dist + "," + dist2 +") = " + dist*dist2 +"\n" + "Hori " + thcg.toString() + "\n" + "Verti" + tvcg.toString());
        }
    }

    @Override
    public String toString() {
        return "SubProblem{" +
                "bucket=" + bucket +
                ", thcg=" + thcg +
                ", tvcg=" + tvcg +
                '}';
    }

    @Override
    void execute() {

    }
}


