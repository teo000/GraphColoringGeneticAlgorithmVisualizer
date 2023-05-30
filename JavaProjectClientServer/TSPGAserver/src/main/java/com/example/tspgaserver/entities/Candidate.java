package com.example.tspgaserver.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "candidates")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "candidate_index")
    private int candidateIndex;

    @Column(columnDefinition = "TEXT")
    private String candidate;


//    @OneToMany(targetEntity = Node.class, cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY, orphanRemoval = true)
//    @JoinColumn(name = "candidate_id", referencedColumnName = "id")
//    private List<Node> nodes;
    protected Candidate(){}

    public Candidate(int candidateIndex, String candidate) {
        this.candidateIndex = candidateIndex;
        this.candidate = candidate;
    }

    public Long getId() {
        return id;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
