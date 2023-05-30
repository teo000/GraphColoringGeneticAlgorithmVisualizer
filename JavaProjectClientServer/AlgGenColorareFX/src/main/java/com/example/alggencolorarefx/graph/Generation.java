package com.example.alggencolorarefx.graph;

public class Generation {
    //private Long id;
    private String bestCandidate;
    private int bestScore;
    private int genNo;
    private boolean finalGen;

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
