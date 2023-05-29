package com.example.tspgaserver.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "generations")
@NamedNativeQuery(name = "Generation.findBySolutionIdAndGenNO", query = "select * from generations g where g.solution_id = ?1 and g.generation = ?2", resultClass = Generation.class)
public class Generation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @OneToOne
//    @JoinColumn(name = "best_candidate_id")
    @Column(name = "best_candidate", columnDefinition = "TEXT")
    private String bestCandidate;
    @Column(name = "best_score")
    private int bestScore;
    @Column(name = "generation")
    private int genNo;
    @Column(name = "final_gen")
    private boolean finalGen = false;

//    @OneToMany(targetEntity = Candidate.class, cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY, orphanRemoval = true)
//    @JoinColumn(name = "generation_id", referencedColumnName = "id")
//    private List<Candidate> candidates = new ArrayList<>();

    protected Generation() {

    }
    public Generation(int genNo) {
        this.genNo = genNo;
    }

//    public void addCandidate(Candidate candidate){
//        this.candidates.add(candidate);
//    }

//    public void setBestSoFar(Candidate bestSoFar) {
//        this.bestSoFar = bestSoFar;
//    }


    public void setBestCandidate(String bestCandidate) {
        this.bestCandidate = bestCandidate;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public int getGenNo() {
        return genNo;
    }

    public String getBestCandidate() {
        return bestCandidate;
    }

    public void setFinalGen(boolean finalGen) {
        this.finalGen = finalGen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Generation that = (Generation) o;
        return bestScore == that.bestScore && genNo == that.genNo && Objects.equals(id, that.id) && Objects.equals(bestCandidate, that.bestCandidate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bestCandidate, bestScore, genNo);
    }
}
