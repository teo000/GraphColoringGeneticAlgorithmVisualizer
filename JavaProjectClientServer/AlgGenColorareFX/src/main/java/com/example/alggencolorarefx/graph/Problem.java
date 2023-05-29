package com.example.alggencolorarefx.graph;

import java.util.List;

public class Problem {
    private Long id;
    private String name;
    private int nodesNo;
    private int edgesNo;
    private List<Edge> edges;

    @Override
    public String toString() {
        return "Problem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nodesNo=" + nodesNo +
                ", edgesNo=" + edgesNo +
                ", edges=" + edges +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getNodesNo() {
        return nodesNo;
    }

    public int getEdgesNo() {
        return edgesNo;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}
