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

class Vertex implements Comparable<Vertex>{
    ArrayList<Edge> OutEdges;
    Integer dist = 0;
    boolean isVisited = false;
    int x; int y; int id;

    public Vertex(int x,int y,int id){
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