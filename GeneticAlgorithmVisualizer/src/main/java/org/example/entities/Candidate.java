package org.example.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "candidates")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int generation;
    @Column(name = "candidate_index")
    private int candidateIndex;
    @OneToMany(targetEntity = Node.class, cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "candidate_id", referencedColumnName = "id")
    private List<Node> nodes;
    protected Candidate(){}

    public Candidate(int generation, int candidateIndex, List<Node> nodes) {
        this.generation = generation;
        this.candidateIndex = candidateIndex;
        this.nodes = nodes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
