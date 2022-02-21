package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            int negiPositon = negative.indexOf(mod.id);

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
        HashMap<Integer,Vertex> idToVertexMap = new HashMap<>(); //given a module id we return a vertex

        Vertex source = new Vertex(-1,-1,-1); //target node
        Vertex target = new Vertex(-2,-2,-2); //source node

        //construct the graph according to the positive and negative sequences
        for (int i = 0; i < positive.size(); i++) {
            for (int j = 0; j < negative.size(); j++) {
                if (positive.get(i) == negative.get(j)){
                    Vertex vertex = new Vertex(i,j,positive.get(i)); //whenever a node in the positive and negative sequence have the same module
                    hcg.vertices.add(vertex);                        //we save its coordinates in the graph.
                    System.out.println(vertex.toString());
                    idToVertexMap.put(positive.get(i), vertex);
                    break; //we can break out of the forloop if we have already found the single module corresponding to the coordinate.
                }
            }
        }

        for (Vertex fromVertex : hcg.vertices){
            for (Integer id : modules.get(fromVertex.id-1).rightOf) {
                Vertex toVertex = idToVertexMap.get(id);
                fromVertex.addOutEdge(new Edge(fromVertex,toVertex,modules.get(fromVertex.id-1).width));
            }
        }

        System.out.println(hcg.toString());
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
class Module{
    int id;
    int width;
    int height;

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
}