package com.example.tspgaserver.algorithms;

import com.example.tspgaserver.entities.*;


import java.util.ArrayList;
import java.util.List;

public abstract class GraphColoringAlgorithm {
    protected Problem instance;
    protected int[] graph;
   // protected Solution solution ;
    protected int nrNodes, maxDegree;
    protected int candidateLength;
    protected List<Candidate> candidates = new ArrayList<>();

//    public Solution getSolution() {
//        return solution;
//    }
}
