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
    public int distToTarget=-1;
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
        //System.out.println("New explore iteration: " + explorationIteration);
        return dist;

    }
    public int DFS_New(){
        DFS_ExploreNew(this);
        return this.distToTarget;
    }
    private int DFS_ExploreNew(Vertex input){
        int delta=0;
        if (input.distToTarget>-1){
            return input.weight+input.distToTarget;
        }
        for(Vertex v: input.neighbors){
            if(v.distToTarget>-1 && delta<v.distToTarget){ //If we have already calculated the distance from v to target we use this value as the delta+ it's weight
                delta=v.distToTarget+v.weight;
            }else { //If we don't have the calculated value from V to target we explore DFS from the point V
                int subDFS= DFS_ExploreNew(v);
                if (subDFS> delta) { //If our subDFS for this neighbour is longer than other neighbours we save the delta
                    delta = subDFS; //We don't need to add the weight of the v here since it's included in the recursive return loop.

                }
            }
        }
        //System.out.println("For Vertex: " + (char)(input.id+64) + "delta was set to: " +delta);
        //We found the furthest distance for the point input and set the distance for this point to be accesible later.
        input.distToTarget=delta;

        return input.weight+delta;

    }
    private int DFSExplore_Test(Vertex input, int depth, int maxDepth) {
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