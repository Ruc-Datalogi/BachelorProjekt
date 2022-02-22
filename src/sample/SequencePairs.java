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
        HashMap<Integer,Vertex> idToVertexMap = new HashMap<>(); //given a module id we return a vertex

        Vertex sourceHorizontal = new Vertex(-1,-1,-1); //target node
        Vertex targetHorizontal = new Vertex(-2,-2,-2); //source node

        //construct the graph according to the positive and negative sequences
        for(Module mod : modules){
            Vertex vertexH = new Vertex(mod.positiveIndex,mod.negativeIndex,mod.id);

            hcg.vertices.add(vertexH);



            Vertex vertexV = new Vertex(mod.positiveIndex,mod.negativeIndex,mod.id);
            vcg.vertices.add(vertexV);
        }
        System.out.println(modules);
        Collections.sort(modules);
        System.out.println(modules);

        for(Vertex vH : hcg.vertices){
            Module thisMod = modules.get(vH.id-1);
            if(thisMod.leftOf.size()==0){
                sourceHorizontal.addOutEdge(new Edge(sourceHorizontal,vH, thisMod.width));
            }
        }

        //System.out.println(hcg.vertices.toString());
        /*
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
         */

        /*for (Vertex fromVertex : hcg.vertices){
            for (Integer id : modules.get(fromVertex.id-1).rightOf) {
                Vertex toVertex = idToVertexMap.get(id);
                fromVertex.addOutEdge(new Edge(fromVertex,toVertex,modules.get(fromVertex.id-1).width));
            }
        }*/

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