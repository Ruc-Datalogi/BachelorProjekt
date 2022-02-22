package sample;

import java.util.*;

public class SequencePairs extends Algorithm{
    ArrayList<Integer> positive = new ArrayList<>();
    ArrayList<Integer> negative = new ArrayList<>();
    ArrayList<Module> modules = new ArrayList<>();

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
        }

        AdjacencyGraph hcg = new AdjacencyGraph(); //horizontally constructed graph,
        AdjacencyGraph vcg = new AdjacencyGraph(); //Vertically constructed graph

        Vertex sourceHorizontal = new Vertex(-1,-1,-1); //target node
        Vertex targetHorizontal = new Vertex(-2,-2,-2); //source node
        Vertex sourceVertical = new Vertex(-1,-1,-1); //target node
        Vertex targetVertical = new Vertex(-1,-1,-1); //target node

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
        int dist = getDist(sourceHorizontal);
        int dist2 = getDist(sourceVertical);
        System.out.println("Longest path (x,y): " + dist + " , " + dist2);


        System.out.println("\n" + hcg.vertices);
        System.out.println("\n" + vcg.vertices);

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
                    source.addOutEdge(new Edge(source,vH, thisMod.width));
                }
                if ( thisMod.rightOf.size()==0){
                    vH.addOutEdge(new Edge(vH,target, 0));
                } else {
                    thisMod.rightOf.forEach(i -> vH.addOutEdge(new Edge(vH,graph.vertices.get(i-1),modules.get(i-1).width)));
                }
            } else {
                if ( thisMod.below.size() == 0 ){
                    source.addOutEdge(new Edge(source,vH, thisMod.height));
                }
                if ( thisMod.above.size()==0){
                    vH.addOutEdge(new Edge(vH,target, 0));
                } else {
                    thisMod.above.forEach(i -> vH.addOutEdge(new Edge(vH,graph.vertices.get(i-1),modules.get(i-1).height)));
                }
            }
        }
    }



    private int getDist(Vertex inputVertex) {
        Queue<Edge> queue = new LinkedList<>(inputVertex.OutEdges);
        int dist = 0;

        while (!queue.isEmpty()) {
            Edge tempEdge = queue.remove();
            Vertex tempVertex = tempEdge.to;
            tempVertex.dist = tempEdge.weight;
            if(!tempVertex.isVisited) {
                tempVertex.isVisited = true;
                tempVertex.addDistanceToEdges(tempVertex.dist);
                queue.addAll(tempVertex.OutEdges);
            }
            if(tempEdge.weight > dist ) {
                dist = tempEdge.weight;
            }
        }
        return dist;
    }

    private List<Integer> getCommon(List<Integer> rightPosSlice, List<Integer> rightNegiSlice) {
        List<Integer> rightCommon = new ArrayList<>(rightPosSlice);
        rightCommon.retainAll(rightNegiSlice);
        return rightCommon;
    }

    @Override
    void execute() {
        System.out.println(":)");
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