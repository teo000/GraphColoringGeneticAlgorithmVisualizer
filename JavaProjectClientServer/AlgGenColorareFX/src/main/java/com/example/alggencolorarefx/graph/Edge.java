package com.example.alggencolorarefx.graph;

public class Edge {
    private Long problemId;
    public int edgeNo;
    public int node1;
    public int node2;

    public Edge(int edgeNo, int node1, int node2) {
        this.edgeNo = edgeNo;
        this.node1 = node1;
        this.node2 = node2;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "edgeNo=" + edgeNo +
                ", node1=" + node1 +
                ", node2=" + node2 +
                '}';
    }
}
