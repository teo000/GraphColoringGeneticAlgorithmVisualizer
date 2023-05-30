package com.example.tspgaserver.algorithms;

import com.example.tspgaserver.entities.*;
import com.example.tspgaserver.exceptions.ProblemNotFoundException;

import java.util.Random;

public class SimulatedAnnealing extends GraphColoringAlgorithm{
    public static void copy_sir(int[] a, int indexA, int[] b, int indexB, int n) {
        for (int i = 0; i < n; i++)
            a[indexA + i] = b[indexB + i];
    }
    public boolean checkEdgeExists(int a, int b) {
        if(graph[(a - 1) * nrNodes + b - 1] == 1)
            return true;
        return false;
    }
    public static void random_color(int[] candidat, int n, int kMax) {
        Random random = new Random();
        for (int i = 0; i < n; i++)
            candidat[i] = random.nextInt(kMax);
    }
    public static void restart_color(int[] candidat, int n, int kMax) {
        Random random = new Random();
        for (int i = 0; i < n; i++)
            if (candidat[i] > kMax)
                candidat[i] = random.nextInt(kMax);
        for (int i = n / 2; i < n; i++)
            candidat[i] = random.nextInt(kMax);
    }

    public int evaluate_graf(int[] candidat, int indexCandidat, int n) {
        int penalty = 0;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                if (candidat[indexCandidat + i] == candidat[indexCandidat + j]) {
                    if (checkEdgeExists(i + 1, j + 1))
                        penalty++;
                }
            }
        return penalty;
    }

    public SimulatedAnnealing(Problem instance) {
        this.instance = instance;
        nrNodes = instance.getNodesNo();
        candidateLength = nrNodes;

        graph = new int[nrNodes * nrNodes + 5];

        for(Edge edge : instance.getEdges()){
            graph[(edge.node1 - 1) * nrNodes + edge.node2 - 1] = 1;
            graph[(edge.node2 - 1) * nrNodes + edge.node1 - 1] = 1;
        }

        for (int i = 0; i < nrNodes; i++) {
            int currDegree = 0;
            for (int j = 0; j < nrNodes; j++) {
                if (checkEdgeExists(i + 1, j + 1)) {
                    currDegree++;
                }
            }
            if (currDegree > maxDegree)
                maxDegree = currDegree;
        }
    }

    private int MAX, kStart, minim_general, lgsir;
    public double eps, T;
    private int[] sir_curent;
    private int[] sir_best;

    private int valoare_current;
    public int best;

    public int finalResult;


    public void initGA(){
        T = 100;
        eps = 0.995;
        MAX = 500;
        kStart = maxDegree + 1;
        minim_general = kStart;

        lgsir = nrNodes;

        sir_curent = new int[lgsir];
        sir_best = new int[lgsir];
        random_color(sir_curent, nrNodes, kStart);

        valoare_current = evaluate_graf(sir_curent, 0, nrNodes);
    }

    public void run(int generatie) {
        if (T > 0.00001)
        {
            for (int i = 0; i < MAX; i++) {
                int index = new Random().nextInt(lgsir);
                int oldvalue = sir_curent[index];

                sir_curent[index] = new Random().nextInt(kStart) + 1;

                int valoare_nou = evaluate_graf(sir_curent, 0, nrNodes);

                if (valoare_nou == 0) {
                    best = kStart;
                    copy_sir(sir_best, 0, sir_curent, 0, candidateLength);
                    kStart--;
                    restart_color(sir_curent, nrNodes, kStart);
                }
                if (valoare_nou < valoare_current)
                    valoare_current = valoare_nou;
                else if ((new Random().nextInt(1000) * 1.0 / 1000) < Math.exp(-(valoare_nou - valoare_current) / T))
                    valoare_current = valoare_nou;
                else
                    sir_curent[index] = oldvalue;
            }
            System.out.println("BEST SO FAR " + best);
            T *= eps;
        }
        else {
            finalResult = best;
        }
    }

    public Generation setBestSoFar(int genNo){
        System.out.println("setBestSoFar, genNo = " + genNo);
        Generation generation = new Generation(genNo);
        StringBuilder sb = new StringBuilder(candidateLength);

        for(int nodeIndex = 0; nodeIndex < candidateLength ;nodeIndex ++) {
            sb.append(Integer.toString(sir_best[nodeIndex]));
            sb.append(',');
        }

        generation.setBestCandidate(sb.toString());
        generation.setBestScore(best);

        return generation;
    }

    public Result getFinalResult(){
        StringBuilder sb = new StringBuilder(candidateLength);

        for(int nodeIndex = 0; nodeIndex < candidateLength ;nodeIndex ++) {
            sb.append(Integer.toString(sir_best[nodeIndex]));
            sb.append(',');
        }

        return new Result(sb.toString(), best);
    }
}
