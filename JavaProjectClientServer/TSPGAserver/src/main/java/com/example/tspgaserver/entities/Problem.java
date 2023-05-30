package com.example.tspgaserver.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "problem_instances")
public class Problem {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    @Column(name = "nodes_no")
    private int nodesNo;
    @Column(name = "edges_no")
    private int edgesNo;
    @OneToMany(targetEntity = Edge.class, cascade = CascadeType.ALL,
                fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    private List<Edge> edges;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int getNodesNo() {
        return nodesNo;
    }

    public int getEdgesNo() {
        return edgesNo;
    }
}