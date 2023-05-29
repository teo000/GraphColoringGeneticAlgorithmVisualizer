package com.example.tspgaserver.entities;

import com.example.tspgaserver.algorithms.GeneticAlgorithm;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "solutions")
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    private int candidateLength;
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

    @Transient
    public
    GeneticAlgorithm geneticAlgorithm;

//    @OneToMany(targetEntity = Candidate.class, cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY, orphanRemoval = true)
//    @JoinColumn(name = "solution_id", referencedColumnName = "id")
//    private List<Candidate> candidates = new ArrayList<>();
    @OneToMany(targetEntity = Generation.class, cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "solution_id", referencedColumnName = "id")
    private List<Generation> generations = new ArrayList<>();

    protected Solution() {
    }

    public Solution(GeneticAlgorithm ga, Problem problemInstance, double mutationProb, double crossoverProb, int populationSize, int generationsNo, int candidateLength) {
        this.geneticAlgorithm = ga;
        this.mutationProb = mutationProb;
        this.crossoverProb = crossoverProb;
        this.populationSize = populationSize;
        this.generationsNo = generationsNo;
        this.problemInstance = problemInstance;
        this.candidateLength = candidateLength;
    }
    public void addGeneration(Generation generation){
        generations.add(generation);
    }

    public Long getId() {
        return id;
    }

    public int getGenerationsNo() {
        return generationsNo;
    }
}
