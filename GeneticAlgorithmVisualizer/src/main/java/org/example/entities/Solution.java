package org.example.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "solutions")
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "mutation_prob")
    private double mutationProb;
    @Column(name = "crossover_prob")
    private double crossoverProb;
    @Column(name = "population_size")
    private int populationSize;
    @Column(name = "generations_no")
    private int generationsNo;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "problem_id")
    private Problem problemInstance;

    @OneToMany(targetEntity = Candidate.class, cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "solution_id", referencedColumnName = "id")
    private List<Candidate> candidates = new ArrayList<>();

    protected Solution() {
    }

    public Solution(Problem problemInstance, double mutationProb, double crossoverProb, int populationSize, int generationsNo ) {
        this.mutationProb = mutationProb;
        this.crossoverProb = crossoverProb;
        this.populationSize = populationSize;
        this.generationsNo = generationsNo;
        this.problemInstance = problemInstance;
    }
    public void addCandidate(Candidate candidate){
        candidates.add(candidate);
    }

}
