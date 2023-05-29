package com.example.alggencolorarefx.graph;

public class Edge {
    private Long problemId;
    public int edgeNo;
    public int node1;
    public int node2;

    @Override
    public String toString() {
        return "Edge{" +
                "edgeNo=" + edgeNo +
                ", node1=" + node1 +
                ", node2=" + node2 +
                '}';
    }
}
