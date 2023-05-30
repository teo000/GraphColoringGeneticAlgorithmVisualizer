package com.example.alggencolorarefx.graph;

public class Candidate {
    private long id;
    private int candidateIndex;
    private String candidate;

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", candidateIndex=" + candidateIndex +
                ", candidate='" + candidate + '\'' +
                '}';
    }
}
