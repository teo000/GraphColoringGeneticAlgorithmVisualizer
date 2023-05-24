package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "edges")
public class Edge {
    @Id
    private Long id;
    @Column (name = "problem_id")
    private Long problemId;
    private int edge_no;
    private int node1;
    private int node2;

    @Override
    public String toString() {
        return "Edge{" +
                ", edge_no=" + edge_no +
                ", node1=" + node1 +
                ", node2=" + node2 +
                '}';
    }
}
