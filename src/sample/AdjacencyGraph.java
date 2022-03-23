package sample;

import java.util.ArrayList;

public class AdjacencyGraph {
    ArrayList<Vertex> vertices;

    @Override
    public String toString() {
        return "AdjacencyGraph{" +
                "vertices=" + vertices.toString() +
                '}';
    }

    public AdjacencyGraph(){
        vertices= new ArrayList<>();
    }

    /*
    public  void PrintGraph(){
        for (int i=0;i<vertices.size();i++)
        {
            System.out.println(" From Vertex: "+ vertices.get(i).name);
            Vertex currentfrom=vertices.get(i);
            for(int j=0; j<currentfrom.OutEdges.size();j++){
                Edge currentEdge=currentfrom.OutEdges.get(j);
                System.out.println(" To: "+ currentEdge.to.name + " weight: "+currentEdge.weight);
            }
            System.out.println(" ");
        }
    }
    */

}

class TEMPAdjanceyGraph {
    ArrayList<TempVertex> vertices = new ArrayList<>();

    @Override
    public String toString() {
        return "AdjacencyGraph{"+ "\n" +
                "vertices=" + vertices.toString() +
                '}';
    }

}

class TempVertex {
    int weight;
    int id;
    int maxDepth = -1;
    TempVertex previousVertex;
    ArrayList<TempVertex> neighbors = new ArrayList<>();

    public TempVertex(int weight, int id){
        this.weight = weight;
        this.id = id;
    }

    public void addNeighbor(TempVertex v) {
        neighbors.add(v);
    }

    public void setPreviousVertex(TempVertex previousVertex) {
        this.previousVertex = previousVertex;
    }

    public void setMaxDepth(int dist) {
        this.maxDepth = dist;
    }

    private String getNeighbors() {
        String s = "";
        for(TempVertex v : neighbors) {
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

class Vertex implements Comparable<Vertex>{
    ArrayList<Edge> OutEdges;
    ArrayList<Vertex> OutVertices= new ArrayList<>();
    Integer dist = 0;
    boolean isVisited = false;
    int x; int y; int id;

    public Vertex(int x, int y, int id){
        this.id = id;
        this.x = x;
        this.y = y;
        OutEdges = new ArrayList<>();
    }

    public void addOutEdge(Edge e) {
        OutEdges.add(e);
    }

    @Override
    public int compareTo(Vertex o) {
        if (this.dist<o.dist)
            return -1;
        if (this.dist>o.dist)
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                + this.x  + " " + this.y +
                " id=" + this.id +
                ", OutEdges=" + OutEdges +
                ", dist=" + dist +
                ", isVisited=" + isVisited +
                '}'
                + "\n";
    }

    public void addDistanceToEdges(int distance) {
        OutEdges.forEach(edge -> edge.weight += distance);
    }
    public void addOutVerticesFromEdges(){
        OutEdges.forEach(edge -> OutVertices.add(edge.to));
    }
}

class Edge implements Comparable<Edge> {
    Integer weight;
    Vertex from;
    Vertex to;


    @Override
    public String toString() {
        return "Edge{" +
                "weight=" + weight +
                ", from=" + from.id +
                ", to=" + to.id +
                '}';
    }



    public Edge(Vertex from, Vertex to, Integer cost){
        this.from=from;
        this.to=to;
        this.weight=cost;
    }

    @Override
    public int compareTo(Edge o) {
        if (this.weight<o.weight)
            return -1;
        if (this.weight>o.weight)
            return 1;
        return 0;
    }
}