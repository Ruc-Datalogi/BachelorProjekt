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
    Vertex previousVertex;
    ArrayList<Vertex> neighbors = new ArrayList<>();

    public Vertex(int weight, int id){
        this.weight = weight;
        this.id = id;
    }

    public void addNeighbor(Vertex v) {
        neighbors.add(v);
    }

    public void setPreviousVertex(Vertex previousVertex) {
        this.previousVertex = previousVertex;
    }

    public void setMaxDepth(int dist) {
        this.maxDepth = dist;
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