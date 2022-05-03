package sample;

import java.util.ArrayList;

class AdjanceyGraph {
    ArrayList<Vertex> vertices = new ArrayList<>();

    @Override
    public String toString() {
        return "AdjacencyGraph{"+ "\n" +
                "vertices=" + vertices.toString() +
                '}';
    }

}

class Vertex {
    int weight;
    int id;
    int maxDepth = -1;
    private int distToTarget=-1;
    private int explorationIteration=0;
    Vertex previousVertex;
    ArrayList<Vertex> neighbors = new ArrayList<>(); //TODO change structure?

    public Vertex(int weight, int id){
        this.weight = weight;
        this.id = id;
    }

    public void setPreviousVertex(Vertex previousVertex) {
        this.previousVertex = previousVertex;
    }

    public void setMaxDepth(int dist) {
        this.maxDepth = dist;
    }

    public int DFS_Test() {
        int dist=DFSExplore_Test(this, 0, 0);
        System.out.println("New explore iteration: " + explorationIteration);
        return dist;

    }
    private int DFSExplore_Test(Vertex input, int depth, int maxDepth) {

        explorationIteration++;
        for(Vertex v : input.neighbors) {
            if(v.distToTarget>-1){
                if (depth + v.distToTarget > maxDepth) {
                    maxDepth = depth + v.distToTarget;
                }

            }else {
                if (depth + v.weight > maxDepth) {
                    maxDepth = depth + v.weight;
                }
                maxDepth = DFSExplore_Test(v, depth + v.weight, maxDepth);
            }
        }
        input.distToTarget=maxDepth-depth;
        //System.out.println("DFSExplore_Test() yielded " + maxDepth + ", for " +input.id );
        return maxDepth;
    }

    private String getNeighbors() {
        String s = "";
        for(Vertex v : neighbors) {
            s += v.id + ",";
        }
        return s;
    }

    @Override
    public String toString() {
        return "TempVertex{" +
                "weight=" + weight +
                ", id=" + id +
                ", neighbors=" + getNeighbors() +
                " prev =" + printPrev() +
                "dist=" + maxDepth +
                "}\n";
    }

    public String printPrev(){
        if(previousVertex != null) {
            return String.valueOf(previousVertex.id) + " ";
        } return "No prev ";
    }
}