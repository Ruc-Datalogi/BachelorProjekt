package sample;

import java.util.ArrayList;

public class AdjacencyGraph {
    ArrayList<Vertex> vertices;


    public AdjacencyGraph(){
        vertices= new ArrayList<>();
    }

    public void addVertex(Vertex v){
        vertices.add(v);
    }

    public void addEdge(Vertex f,Vertex t, Integer w){
        if(!(vertices.contains(f) && vertices.contains(t)) ) {
            System.out.println(" Vertex not in graph");
            return;
        }
        Edge e=new Edge(f, t,w);
    }

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
}

class Vertex implements Comparable<Vertex>{
    String name;
    ArrayList<Edge> OutEdges;
    Integer dist = Integer.MAX_VALUE;
    boolean isVisited = false;

    public Vertex(String id){
        name = id;
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
                "name='" + name + '\'' +
                ", OutEdges=" + OutEdges +
                ", dist=" + dist +
                ", isVisited=" + isVisited +
                '}';
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
                ", from=" + from.name +
                ", to=" + to.name +
                '}';
    }

    public Edge(Vertex from, Vertex to, Integer cost){
        this.from=from;
        this.to=to;
        this.weight=cost;
        this.from.addOutEdge(this);
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