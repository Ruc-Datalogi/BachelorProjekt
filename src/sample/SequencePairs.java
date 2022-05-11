package sample;

import java.util.*;

public class SequencePairs extends Algorithm {
    ArrayList<Integer> positiveSequence;
    ArrayList<Integer> negative;
    ArrayList<Module> modules;
    HashMap<Integer, Integer> mapPostive = new HashMap<>();
    HashMap<Integer, Integer> mapNegative = new HashMap<>();
    boolean rotate = false;
    int worstIdHorizontal;
    int iterationsSinceBest = 0;
    int bestDist = Integer.MAX_VALUE;
    float bestOptimazitionFactor = Integer.MAX_VALUE;
    public Bin2D bestBin = new Bin2D();
    Bin2D testBin;
    HashSet<ArrayList<ArrayList<Integer>>> solutionSet = new HashSet<>();

    public SequencePairs(ArrayList<Integer> positive, ArrayList<Integer> negative, ArrayList<Module> modules) {
        this.positiveSequence = positive;
        this.negative = negative;
        this.modules = modules;
        for(int i = 0 ; i < positive.size() ; i++) {
            mapPostive.put(positive.get(i), i);
        }
        for(int i = 0 ; i < negative.size() ; i++) {
            mapNegative.put(negative.get(i), i);
        }
    }

    public void calculatePlacementTable(){
        for(Module mod : modules){
            if(mod.id == worstIdHorizontal){
                rotate = false;
                mod.rotate();
            }

            int posiPosition = mapPostive.get(mod.id);
            mod.setPositiveIndex(posiPosition);
            int negiPositon = mapNegative.get(mod.id);
            mod.setNegativeIndex(negiPositon);

            List<Integer> leftPosSlice   = positiveSequence.subList(0, posiPosition);
            List<Integer> rightPosSlice  = positiveSequence.subList(posiPosition+1, positiveSequence.size() );
            List<Integer> leftNegiSlice  = negative.subList(0, negiPositon);
            List<Integer> rightNegiSlice = negative.subList(negiPositon+1, negative.size() );

            mod.leftOf  = CommonFunctions.getCommon(leftPosSlice, leftNegiSlice);    //TODO this is slow
            mod.rightOf = CommonFunctions.getCommon(rightPosSlice, rightNegiSlice);
            mod.above   = CommonFunctions.getCommon(rightNegiSlice,leftPosSlice);
            mod.below   = CommonFunctions.getCommon(leftNegiSlice,rightPosSlice);
        }

        AdjanceyGraph thcg = new AdjanceyGraph();
        AdjanceyGraph tvcg = new AdjanceyGraph();

        Vertex sourceH = new Vertex(0,-1);
        Vertex targetH = new Vertex(0, -2);
        Vertex sourceV = new Vertex(0, -1);
        Vertex targetV = new Vertex(0, -2);

        //construct the graph according to the positive and negative sequences
        for(Module mod : modules){
            Vertex vertexH = new Vertex(mod.width, mod.id);
            thcg.vertices.add(vertexH);

            Vertex vertexV = new Vertex(mod.height, mod.id);
            tvcg.vertices.add(vertexV);
        }

        Collections.sort(modules); // Sort the modules to make sure the id matches with the index :)
        createTempGraph(thcg, modules, sourceH, targetH, true);
        createTempGraph(tvcg, modules, sourceV, targetV, false);

        thcg.vertices.add(sourceH);
        thcg.vertices.add(targetH);
        tvcg.vertices.add(sourceV);
        tvcg.vertices.add(targetV);

        int dist1=sourceH.DFS_New();
        int dist2 = sourceV.DFS_New();
        super.optimizationFactor = dist2*dist1; // the variable we optimise for.

        iterationsSinceBest++;

        if (optimizationFactor < bestDist) {
            testBin = TEMPgenerateCoordinatesForModules(thcg, tvcg, dist1, dist2);
            //bestBin = testBin;
            bestDist = (int) optimizationFactor;
            System.out.println("Best (" + dist1 + "," + dist2 +" iterations: " + iterationsSinceBest + ") = " + dist1 *dist2);
            iterationsSinceBest = 0;
            PrimaryWindow.changeDebugMessage("Best (" + dist1 + "," + dist2 +" iterations: " + iterationsSinceBest + ") = " + dist1 *dist2 +"\n" + "Hori " + thcg.toString() + "\n" + "Verti" + tvcg.toString());
        }
    }


    private int worstRoute(Vertex input) {
        Vertex biggest = input;
        Vertex currentVertex = input;
        ArrayList<Vertex> list = new ArrayList<>();
        Random r = new Random();
        while (currentVertex.previousVertex != null) {
            currentVertex = currentVertex.previousVertex;
            if(biggest.weight < currentVertex.weight && currentVertex.id > 0) {
                biggest = currentVertex;
                list.add(currentVertex);
            }
        }
        return list.get(r.nextInt(list.size())).id;
    }


    //TODO Slow
    private void createTempGraph(AdjanceyGraph graph, ArrayList<Module> modules, Vertex source, Vertex target, boolean isHorizontal) {
        for (Vertex v : graph.vertices) {
            Module thisMod = modules.get(v.id - 1);

            if (isHorizontal) {
                if (thisMod.leftOf.size() == 0) {
                    source.neighbors.add(v);
                }
                if (thisMod.rightOf.size() == 0) {
                    v.neighbors.add(target);
                } else {
                    thisMod.rightOf.forEach(index -> v.neighbors.add(graph.vertices.get(index - 1)));
                }
            } else {
                if (thisMod.below.size() == 0) {
                    source.neighbors.add(v);
                }
                if (thisMod.above.size() == 0) {
                    v.neighbors.add(target);
                } else {
                    thisMod.above.forEach(index -> v.neighbors.add(graph.vertices.get(index - 1)));
                }
            }
        }
    }

    Bin2D TEMPgenerateCoordinatesForModules(AdjanceyGraph horizontal, AdjanceyGraph vertical, int wH, int wV){
        Bin2D bin = new Bin2D(5000,5000); //TODO don't hardcode fucking values
        int scalar = 800/Math.max(wH, wV);

        for(Vertex vy : vertical.vertices){
            for(Vertex vx : horizontal.vertices){
                if(vx.id == vy.id && vx.id > 0){
                    Module currentMod = modules.get(vx.id-1);

                    Box2D currentBox = new Box2D((wH-vx.distToTarget-vx.weight)*scalar, (wV-vy.distToTarget-vy.weight)*scalar , currentMod.width*scalar, currentMod.height*scalar);
                    currentBox.setId(vx.id);
                    bin.addBox(currentBox);
                }
            }
        }
        return bin;
    }

    int DFSIt=0;
    private int DFS(Vertex input) {
        DFSIt=0;
        int dfsDist=DFSExplore(input, 0, 0);
        return dfsDist;
    }

    private int DFSExplore(Vertex input, int depth, int maxDepth) {
        DFSIt++;
        for(Vertex v : input.neighbors) {
            if (depth + v.weight > maxDepth) {
                maxDepth = depth + v.weight;
            }
            if (v.maxDepth < depth) {
                v.setPreviousVertex(input);
                v.setMaxDepth(depth);
            }
            maxDepth = DFSExplore(v, depth + v.weight, maxDepth);
        }
        return maxDepth;
    }

    private void swapInMap(HashMap<Integer, Integer> map, int id1 , int id2) {
        Integer tempValue = map.get(id1);
        map.put(id1, map.get(id2));
        map.put(id2, tempValue);
    }

    @Override
    void execute() {

        //int swap1;
        Random random = new Random();

        int randomIndex1 = random.nextInt(positiveSequence.size());
        int randomIndex2 = random.nextInt(positiveSequence.size());

        int id1 = positiveSequence.get(randomIndex1);
        int id2 = positiveSequence.get(randomIndex2);

        switch (random.nextInt(0, 2)) {
            case 0 -> { // Dual swap
                Collections.swap(positiveSequence, randomIndex1, randomIndex2);
                swapInMap(mapPostive, id1, id2);
                Collections.swap(negative, mapNegative.get(id1), mapNegative.get(id2));
                swapInMap(mapNegative, id1, id2);
            }
            case 1 -> { // Single Swap Positive
                Collections.swap(positiveSequence, randomIndex1, randomIndex2);
                swapInMap(mapPostive, id1, id2);
            }
            case 2 -> { // Single Swap Negative
                Collections.swap(negative, randomIndex1, randomIndex2);
                swapInMap(mapNegative, id1, id2);
            }
            case 5 -> // Rotate
                    rotate = true;
        }

        ArrayList<ArrayList<Integer>> solutions = new ArrayList<>();
        solutions.add(positiveSequence);
        solutions.add(negative);
        this.solution = solutions;

        if (solutionSet.add(solutions)) {
            this.calculatePlacementTable(); // clean the table //TODO figure out if it needs to be earlier
        }
    }
}

class Module implements Comparable<Module>{
    int id;
    int width;
    int height;
    int positiveIndex=-1;
    int negativeIndex=-1;

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", width=" + width +
                ", height=" + height +
                ", RightOf=" + rightOf +
                ", LeftOf=" + leftOf +
                ", Above=" + above +
                ", Below=" + below +
                '}';
    }

    List<Integer> rightOf = new ArrayList<>();
    List<Integer> leftOf  = new ArrayList<>();
    List<Integer> above   = new ArrayList<>();
    List<Integer> below   = new ArrayList<>();

    public Module(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public void setPositiveIndex(int positiveIndex) {
        this.positiveIndex = positiveIndex;
    }

    public void setNegativeIndex(int negativeIndex) {
        this.negativeIndex = negativeIndex;
    }

    public void rotate(){
        int temp = this.width;
        this.width = this.height;
        this.height = temp;
    }

    @Override
    public int compareTo(Module otherMod) {
        if(this.id>otherMod.id){
            return 1;
        }else if (this.id<otherMod.id){
            return -1;
        }
        return 0;
    }

}