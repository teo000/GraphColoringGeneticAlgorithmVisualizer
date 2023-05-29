package com.example.tspgaserver.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "edges")
@IdClass(EdgePk.class)
public class Edge {
    @Id
    @Column (name = "problem_id")
    private Long problemId;
    @Id
    @Column(name = "edge_no")
    public int edgeNo;
    public int node1;
    public int node2;

    @Override
    public String toString() {
        return "Edge{" +
                "edge_no=" + edgeNo +
                ", node1=" + node1 +
                ", node2=" + node2 +
                '}';
    }
}
