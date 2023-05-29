package com.example.tspgaserver.entities;

import jakarta.persistence.*;

@Entity
@Table(name ="nodes")
public class Node {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int node;
    private int color;
    protected Node() {}
    public Node(int node, int color) {
        this.node = node;
        this.color = color;
    }


}
