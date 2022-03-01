package sample;

import java.util.*;

public class SequencePairs extends Algorithm {
    ArrayList<Integer> positive = new ArrayList<>();
    ArrayList<Integer> negative = new ArrayList<>();
    ArrayList<Module> modules = new ArrayList<>();
    Bin2D testBin;

    public SequencePairs(ArrayList<Integer> positive, ArrayList<Integer> negative, ArrayList<Module> modules) {
        this.positive = positive;
        this.negative = negative;
        this.modules = modules;
    }

    public void calculatePlacementTable(){
        for(Module mod : modules){

            //TODO remove index of
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

        Vertex sourceHorizontal = new Vertex(-1,-1,-1); //target node
        Vertex targetHorizontal = new Vertex(-2,-2,-2); //source node
        Vertex sourceVertical = new Vertex(-1,-1,-1); //target node
        Vertex targetVertical = new Vertex(-1,-1,-2); //target node

        //construct the graph according to the positive and negative sequences
        for(Module mod : modules){
            Vertex vertexH = new Vertex(mod.positiveIndex,mod.negativeIndex,mod.id);
            hcg.vertices.add(vertexH);
            Vertex vertexV = new Vertex(mod.positiveIndex,mod.negativeIndex,mod.id);
            vcg.vertices.add(vertexV);
        }

        Collections.sort(modules); // Sort the modules to make sure the id matches with the index :)
        createGraph(hcg, modules, sourceHorizontal, targetHorizontal, true);
        createGraph(vcg, modules, sourceVertical, targetVertical, false);

        hcg.vertices.add(sourceHorizontal);
        hcg.vertices.add(targetHorizontal);
        vcg.vertices.add(sourceVertical);
        vcg.vertices.add(targetVertical);
        //DFS(vcg.vertices.get(4));

        int dist = DFS(sourceHorizontal);
        int dist2 = DFS(sourceVertical);
        optimizationFactor = dist2*dist; //the variable we optimise for.

        //int dist = getDist(sourceHorizontal);
        //int dist2 = getDist(sourceVertical);
        System.out.println("Longest path (x,y): " + dist + " , " + dist2);
        System.out.println(positive);
        System.out.println(negative);

        if (optimizationFactor < 26) System.exit(2);


        testBin=generateCoordinatesForModules(hcg,vcg,dist,dist2);


        //System.out.println("\n" + hcg.vertices);
        //System.out.println("\n" + vcg.vertices);*/

    }

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




    Bin2D generateCoordinatesForModules(AdjacencyGraph horizontal, AdjacencyGraph vertical,int width, int height){
        Bin2D bin = new Bin2D(500,500); //TODO don't hardcode fucking values
        for(Vertex x : horizontal.vertices){
            for(Vertex y : vertical.vertices){
                if(x.id>-1 && x.id==y.id){
                    Module currentMod = modules.get(x.id-1);

                    Box2D currentBox = new Box2D((width-DFS(x))*50, (DFS(y)-currentMod.height)*50,currentMod.width*50, currentMod.height*50);
                    //System.out.println("[" + x.id + "]: DFS: [" + DFS(x) + "," + DFS(y) +"]  corrected: " + (width-DFS(x))+ "," + (DFS(y)-currentMod.height)  + " w:" +currentMod.width + ", h: " + currentMod.height);
                    bin.addBox(currentBox);

                    //boxes.add()
                }
            }
        }
        return bin;
    }

    private int DFSExplore(Vertex input,int depth, boolean visted[],int maxDepth){
            //System.out.println("Id: " + input.id + ", depth: " + depth + ", max: " +maxDepth);


            input.addOutVerticesFromEdges();
            Iterator<Edge> i = input.OutEdges.listIterator();
            while(i.hasNext()){
                Edge tempEdge =i.next();
                if(depth+tempEdge.weight>maxDepth){
                    maxDepth=depth+tempEdge.weight;
                }
                if(tempEdge.to.id<0){
                    maxDepth = DFSExplore(tempEdge.to,depth+tempEdge.weight,visted,maxDepth);
                }else if(!visted[tempEdge.to.id-1]){
                    visted[tempEdge.to.id-1]=true;
                    maxDepth =DFSExplore(tempEdge.to,depth+tempEdge.weight,visted,maxDepth);
                }
            }
            return maxDepth;
        }

        private int DFS(Vertex input){
            boolean [] visted = new boolean[5];
            return DFSExplore(input,0,visted,0);

    }


    private List<Integer> getCommon(List<Integer> rightPosSlice, List<Integer> rightNegiSlice) {
        List<Integer> rightCommon = new ArrayList<>(rightPosSlice);
        rightCommon.retainAll(rightNegiSlice);
        return rightCommon;
    }

    @Override
    void execute() {
        this.calculatePlacementTable(); //clean the table

        //Swap 1
        Random random = new Random();
        int rng = random.nextInt(positive.size());
        int rng2 = random.nextInt(negative.size());

        int idP = positive.get(rng);
        int idP2 = positive.get(rng2);

        Collections.swap(positive,rng,rng2);
        Collections.swap(negative,negative.indexOf(idP),negative.indexOf(idP2));
        //

        ArrayList<ArrayList<Integer>> solutions = new ArrayList<>();
        solutions.add(positive);
        solutions.add(negative);

        this.solution = solutions;
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