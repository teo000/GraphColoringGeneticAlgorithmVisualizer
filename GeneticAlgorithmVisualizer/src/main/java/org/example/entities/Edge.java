package org.example.entities;

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
    private int edgeNo;
    private int node1;
    private int node2;

    @Override
    public String toString() {
        return "Edge{" +
                "edge_no=" + edgeNo +
                ", node1=" + node1 +
                ", node2=" + node2 +
                '}';
    }
}
