package com.example.alggencolorarefx.graph;

import java.util.List;

public class Generation {
    //private Long id;
    private String bestCandidate;
    private int bestScore;
    private int genNo;
    private boolean finalGen;
    private List<Candidate> candidates;

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public String getBestCandidate() {
        return bestCandidate;
    }

    public boolean isFinalGen() {
        return finalGen;
    }

    public int getBestScore() {
        return bestScore;
    }
}
