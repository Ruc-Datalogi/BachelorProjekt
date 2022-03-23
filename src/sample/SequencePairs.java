package sample;

import java.util.*;

public class SequencePairs extends Algorithm {
    ArrayList<Integer> positive = new ArrayList<>();
    ArrayList<Integer> negative = new ArrayList<>();
    ArrayList<Module> modules = new ArrayList<>();
    boolean rotate = false;
    int worstIdHorizontal;
    int worstIdVertical;
    int iterationsSinceBest = 0;
    int bestDist = Integer.MAX_VALUE;
    float bestOptimazitionFactor;
    public Bin2D bestBin = new Bin2D();
    Bin2D testBin;

    public SequencePairs(ArrayList<Integer> positive, ArrayList<Integer> negative, ArrayList<Module> modules) {
        this.positive = positive;
        this.negative = negative;
        this.modules = modules;
    }

    public void calculatePlacementTable(){
        Random r = new Random();
        int id = r.nextInt(modules.size() - 1);
        for(Module mod : modules){
            //TODO remove index of

            /*
            if(r.nextBoolean()) {
                if(mod.id == worstIdHorizontal) {
                    rotate = false;
                    mod.rotate();
                }
            } else {
                if(mod.id == worstIdVertical) {
                    rotate = false;
                    mod.rotate();
                }
            }
            */
            if(rotate && mod.id == worstIdHorizontal){
                rotate = false;
                mod.rotate();
            }



            int posiPosition = positive.indexOf(mod.id);
            mod.setPositiveIndex(posiPosition);
            int negiPositon = negative.indexOf(mod.id);
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

        int dist = Math.abs(DFS(sourceH));
        int dist2 = Math.abs(DFS(sourceV));
        worstIdHorizontal = worstRoute(targetH);
        worstIdVertical = worstRoute(targetV);
        super.optimizationFactor = dist2*dist; // the variable we optimise for.
        worstRoute(targetH);

        if (dist2*dist < super.optimizationFactor){
            bestOptimazitionFactor = dist2*dist;
        }


        if (optimizationFactor < bestDist) {
            //testBin=generateCoordinatesForModules(hcg,vcg,dist,dist2);
            testBin = TEMPgenerateCoordinatesForModules(thcg, tvcg, dist, dist2);
            bestBin = testBin;
            bestDist = (int) optimizationFactor;
            PrimaryWindow.changeDebugMessage("Best (" + dist + "," + dist2 +") = " + dist*dist2 +"\n" + "Hori " + thcg.toString() + "\n" + "Verti" + tvcg.toString());
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

                    Box2D currentBox = new Box2D(vx.maxDepth*scalar, vy.maxDepth*scalar , currentMod.width*scalar, currentMod.height*scalar);
                    currentBox.setId(vx.id);
                    //System.out.println("[" + x.id + "]: DFS: [" + DFS(x) + "," + DFS(y) +"]  corrected: " + (width-DFS(x))+ "," + (DFS(y)-currentMod.height)  + " w:" +currentMod.width + ", h: " + currentMod.height);
                    bin.addBox(currentBox);
                }
            }
        }
        return bin;
    }


    private int DFS(Vertex input) {
        return DFSExplore(input, 0, 0);
    }

    private int DFSExplore(Vertex input, int depth, int maxDepth) {
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

    private int DFSTArget(Vertex input, Vertex target) {
        return DFSTargetExplore(input, target, 0,0);
    }

    private int DFSTargetExplore(Vertex input, Vertex target, int depth, int maxDepth){
        if(input.id == target.id) return maxDepth;
        for(Vertex v: input.neighbors) {
            if(depth + v.weight > maxDepth && v.id == target.id ){
                maxDepth = depth;
            }
            maxDepth = DFSTargetExplore(v, target,depth + v.weight, maxDepth);
        }
        return maxDepth;
    }

    @Override
    void execute() {
        this.calculatePlacementTable(); //clean the table
        int swap1;
        Random random = new Random();
        if(random.nextBoolean()) {
            swap1 = worstIdHorizontal;
        } else {
            swap1 = worstIdVertical;
        }

        //int rng = random.nextInt(positive.size());
        int swap2  = random.nextInt(positive.size());

        int idP = positive.get(swap1 - 1);
        int idP2 = positive.get(swap2);


        if(bestOptimazitionFactor < super.optimizationFactor){
            iterationsSinceBest = 0;
        }

        switch (random.nextInt(0,4)) {
            case 0: // Dual swap
                Collections.swap(positive,swap1 - 1 ,swap2);
                Collections.swap(negative,negative.indexOf(idP),negative.indexOf(idP2));
            case 1: // Single Swap Positive
                Collections.swap(positive,swap1 - 1,swap2);
            case 2: // Single Swap Negative
                Collections.swap(negative,swap1 - 1,swap2);
            case 3: // Slicing swap Positive
                ArrayList<Integer> subList1 = new ArrayList<>(positive.subList(0, positive.indexOf(swap1)));
                ArrayList<Integer> subList2 = new ArrayList<>(positive.subList(positive.indexOf(swap1) + 1, positive.size()));
                subList2.add(positive.get(positive.indexOf(swap1)));
                subList1.addAll(subList2);
                positive = subList1;
            case 4: // Slicing swap Positive
                ArrayList<Integer> subLists1 = new ArrayList<>(negative.subList(0, negative.indexOf(swap1)));
                ArrayList<Integer> subLists2 = new ArrayList<>(negative.subList(negative.indexOf(swap1) + 1, negative.size()));
                subLists2.add(negative.get(negative.indexOf(swap1)));
                subLists1.addAll(subLists2);
                negative = subLists1;
            case 5: // Rotate
                rotate = true;
        }

        ArrayList<ArrayList<Integer>> solutions = new ArrayList<>();
        solutions.add(positive);
        solutions.add(negative);

        this.solution = solutions;
        iterationsSinceBest++;
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

    int getPositiveIndex(){
        return positiveIndex;

    }

    int getNegativeIndex(){
        return negativeIndex;
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