package sample;

import java.util.*;

public class SequencePairs extends Algorithm {
    ArrayList<Integer> positive = new ArrayList<>();
    ArrayList<Integer> negative = new ArrayList<>();
    ArrayList<Module> modules = new ArrayList<>();
    int worstIdHorizontal;
    int worstIdVertical;
    Bin2D testBin;
    int iterationsSinceBest = 0;
    float bestOptimazitionFactor;
    public SequencePairs(ArrayList<Integer> positive, ArrayList<Integer> negative, ArrayList<Module> modules) {
        this.positive = positive;
        this.negative = negative;
        this.modules = modules;
    }

    public void calculatePlacementTable(){
        for(Module mod : modules){
            //TODO remove index of

            if(mod.id == worstIdHorizontal) {
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

            mod.leftOf  = getCommon(leftPosSlice, leftNegiSlice);
            mod.rightOf = getCommon(rightPosSlice, rightNegiSlice);
            mod.above   = getCommon(rightNegiSlice,leftPosSlice);
            mod.below   = getCommon(leftNegiSlice,rightPosSlice);
            //System.out.println(mod);

            //System.out.println(mod);
        }

        AdjacencyGraph hcg = new AdjacencyGraph(); //horizontally constructed graph,
        AdjacencyGraph vcg = new AdjacencyGraph(); //Vertically constructed graph

        TEMPAdjanceyGraph thcg = new TEMPAdjanceyGraph();
        TEMPAdjanceyGraph tvcg = new TEMPAdjanceyGraph();

        TempVertex sourceH = new TempVertex(0,-1);
        TempVertex targetH = new TempVertex(0, -2);
        TempVertex sourceV = new TempVertex(0, -1);
        TempVertex targetV = new TempVertex(0, -2);

        Vertex sourceHorizontal = new Vertex(-1,-1,-1); //target node
        Vertex targetHorizontal = new Vertex(-2,-2,-2); //source node
        Vertex sourceVertical = new Vertex(-1,-1,-1); //target node
        Vertex targetVertical = new Vertex(-1,-1,-2); //target node

        //construct the graph according to the positive and negative sequences
        for(Module mod : modules){
            Vertex vertexH = new Vertex(mod.positiveIndex,mod.negativeIndex,mod.id);
            TempVertex tempVertexH = new TempVertex(mod.width, mod.id);
            thcg.vertices.add(tempVertexH);
            hcg.vertices.add(vertexH);

            Vertex vertexV = new Vertex(mod.positiveIndex,mod.negativeIndex,mod.id);
            TempVertex tempVertexV = new TempVertex(mod.height, mod.id);
            vcg.vertices.add(vertexV);
            tvcg.vertices.add(tempVertexV);
        }

        Collections.sort(modules); // Sort the modules to make sure the id matches with the index :)
        createGraph(hcg, modules, sourceHorizontal, targetHorizontal, true);
        createGraph(vcg, modules, sourceVertical, targetVertical, false);
        createTempGraph(thcg, modules, sourceH, targetH, true);
        createTempGraph(tvcg, modules, sourceV, targetV, false);

        hcg.vertices.add(sourceHorizontal);
        hcg.vertices.add(targetHorizontal);
        vcg.vertices.add(sourceVertical);
        vcg.vertices.add(targetVertical);

        thcg.vertices.add(sourceH);
        thcg.vertices.add(targetH);
        tvcg.vertices.add(sourceV);
        tvcg.vertices.add(targetV);

        int dist = Math.abs(TEMPDFS(sourceH));
        int dist2 = Math.abs(TEMPDFS(sourceV));
        worstIdHorizontal = worstRoute(targetH);
        worstIdVertical = worstRoute(targetV);
        super.optimizationFactor = dist2*dist; // the variable we optimise for.
        worstRoute(targetH);

        if (dist2*dist < super.optimizationFactor){
            bestOptimazitionFactor = dist2*dist;
        }

        PrimaryWindow.changeDebugMessage("Best (" + dist + "," + dist2 +") = " + dist*dist2 +"\n" + "Hori " + thcg.toString() + "\n" + "Verti" + tvcg.toString());

        if (optimizationFactor < bestDist) {
            //testBin=generateCoordinatesForModules(hcg,vcg,dist,dist2);
            testBin = TEMPgenerateCoordinatesForModules(thcg, tvcg, dist, dist2);
            bestBin = testBin;
            bestDist = (int) optimizationFactor;
        }

    }

    public Bin2D bestBin = new Bin2D();
    public int bestDist = Integer.MAX_VALUE;

    /**
     *
     * @param graph the graph to mutates
     * @param modules the list of the different modules
     * @param source the source vertex for the graph
     * @param target the target vertex for the graph
     * @param isHorizontal boolean to construct the graph differently
     */

    private void createGraph(AdjacencyGraph graph, ArrayList<Module> modules, Vertex source, Vertex target, boolean isHorizontal) {
        for(Vertex vH : graph.vertices){
            Module thisMod = modules.get(vH.id-1);
            if(isHorizontal) {
                if ( thisMod.leftOf.size() == 0 ){
                    source.addOutEdge(new Edge(source,vH,0 ));
                }
                if ( thisMod.rightOf.size()==0){
                    vH.addOutEdge(new Edge(vH,target, thisMod.width));
                } else {
                    thisMod.rightOf.forEach(i -> vH.addOutEdge(new Edge(vH,graph.vertices.get(i-1),thisMod.width)));
                }
            } else {
                if ( thisMod.below.size() == 0 ){
                    source.addOutEdge(new Edge(source,vH, 0));
                }
                if ( thisMod.above.size()==0){
                    vH.addOutEdge(new Edge(vH,target, thisMod.height));
                } else {
                    thisMod.above.forEach(i -> vH.addOutEdge(new Edge(vH,graph.vertices.get(i-1),thisMod.height)));
                }
            }
        }
    }

    private int worstRoute(TempVertex input) {
        TempVertex biggest = input;
        TempVertex currentVertex = input;
        ArrayList<TempVertex> list = new ArrayList<>();
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


    private void createTempGraph(TEMPAdjanceyGraph graph, ArrayList<Module> modules, TempVertex source, TempVertex target, boolean isHorizontal) {
        for (TempVertex v : graph.vertices) {
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

    Bin2D TEMPgenerateCoordinatesForModules(TEMPAdjanceyGraph horizontal, TEMPAdjanceyGraph vertical, int wH, int wV){
        Bin2D bin = new Bin2D(5000,5000); //TODO don't hardcode fucking values
        int scalar = 800/Math.max(wH, wV);

        for(TempVertex vy : vertical.vertices){
            for(TempVertex vx : horizontal.vertices){
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



    Bin2D generateCoordinatesForModules(AdjacencyGraph horizontal, AdjacencyGraph vertical,int width, int height){
        Bin2D bin = new Bin2D(5000,5000); //TODO don't hardcode fucking values
        int scalar = 50;
        for(Vertex x : horizontal.vertices){
            for(Vertex y : vertical.vertices){
                if(x.id>-1 && x.id==y.id){
                    Module currentMod = modules.get(x.id-1);

                    Box2D currentBox = new Box2D((width-DFS(x))*scalar, (DFS(y)-currentMod.height)*scalar,currentMod.width*scalar, currentMod.height*scalar);
                    currentBox.setId(x.id);
                    //System.out.println("[" + x.id + "]: DFS: [" + DFS(x) + "," + DFS(y) +"]  corrected: " + (width-DFS(x))+ "," + (DFS(y)-currentMod.height)  + " w:" +currentMod.width + ", h: " + currentMod.height);
                    bin.addBox(currentBox);

                    //boxes.add()
                }
            }
        }
        return bin;
    }

    private int DFSExplore(Vertex input,int depth,int maxDepth){
        //System.out.println("Id: " + input.id + ", depth: " + depth + ", max: " +maxDepth);
        input.addOutVerticesFromEdges();
        Iterator<Edge> i = input.OutEdges.listIterator();
        while(i.hasNext()){
            Edge tempEdge =i.next();
            if(depth+tempEdge.weight>maxDepth){
                maxDepth=depth+tempEdge.weight;
            }

            maxDepth = DFSExplore(tempEdge.to,depth+tempEdge.weight,maxDepth);

        }
        return maxDepth;
    }

    private int DFS(Vertex input){

        return DFSExplore(input,0,0);

    }

    private int TEMPDFS(TempVertex input) {
        return TEMPDFSExplore(input, 0, 0);
    }

    private int TEMPDFSExplore(TempVertex input, int depth, int maxDepth) {
        for(TempVertex v : input.neighbors) {
            if (depth + v.weight > maxDepth) {
                maxDepth = depth + v.weight;
            }
            if (v.maxDepth < depth) {
                v.setPreviousVertex(input);
                v.setMaxDepth(depth);
            }
            maxDepth = TEMPDFSExplore(v, depth + v.weight, maxDepth);
        }
        return maxDepth;
    }

    private int DFSTArget(TempVertex input, TempVertex target) {
        return DFSTargetExplore(input, target, 0,0);
    }

    private int DFSTargetExplore(TempVertex input, TempVertex target, int depth, int maxDepth){
        if(input.id == target.id) return maxDepth;
        for(TempVertex v: input.neighbors) {
            if(depth + v.weight > maxDepth && v.id == target.id ){
                maxDepth = depth;
            }
            maxDepth = DFSTargetExplore(v, target,depth + v.weight, maxDepth);
        }
        return maxDepth;
    }

    private List<Integer> getCommon(List<Integer> rightPosSlice, List<Integer> rightNegiSlice) {
        List<Integer> rightCommon = new ArrayList<>(rightPosSlice);
        rightCommon.retainAll(rightNegiSlice);
        return rightCommon;
    }

    @Override
    void execute() {
        this.calculatePlacementTable(); //clean the table
        int rng;
        Random random = new Random();
        if(random.nextBoolean()) {
            rng = worstIdHorizontal;
        } else {
            rng = worstIdVertical;
        }
        //int rng = random.nextInt(positive.size());
        int rng2 = random.nextInt(positive.size());

        int idP = positive.get(rng - 1);
        int idP2 = positive.get(rng2);


        if(bestOptimazitionFactor < super.optimizationFactor){
            iterationsSinceBest = 0;
        }

        //Swap 1
        if(iterationsSinceBest < 25) {
            Collections.swap(positive,rng - 1 ,rng2);
            Collections.swap(negative,negative.indexOf(idP),negative.indexOf(idP2));
        } else {
            //Single swap
            iterationsSinceBest = 0;
            rng = random.nextInt(positive.size());
            rng2 = random.nextInt(positive.size());
            if(random.nextBoolean()) {
                Collections.swap(positive,rng - 1,rng2);
            } else {
                Collections.swap(negative,rng - 1,rng2);
            }
        }
        if (random.nextBoolean()) {
            ArrayList<Integer> subList1 = new ArrayList<>(positive.subList(0, positive.indexOf(rng)));
            ArrayList<Integer> subList2 = new ArrayList<>(positive.subList(positive.indexOf(rng) + 1, positive.size()));
            subList2.add(positive.get(positive.indexOf(rng)));
            subList1.addAll(subList2);
            positive = subList1;
        } else {
            ArrayList<Integer> subList1 = new ArrayList<>(negative.subList(0, negative.indexOf(rng)));
            ArrayList<Integer> subList2 = new ArrayList<>(negative.subList(negative.indexOf(rng) + 1, negative.size()));
            subList2.add(negative.get(negative.indexOf(rng)));
            subList1.addAll(subList2);
            negative = subList1;
        }

        /*
        else {
            //Single swap
            rng = random.nextInt(positive.size());
            rng2 = random.nextInt(positive.size());
            Collections.swap(negative,rng,rng2);
        }
        */

        ArrayList<ArrayList<Integer>> solutions = new ArrayList<>();
        solutions.add(positive);
        solutions.add(negative);
        /*
        System.out.println("this = " + this);
        */

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