package org.example.algorithms;

import jakarta.persistence.EntityManager;
import org.example.DefaultEntityManagerFactory;
import org.example.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    public static int[] graph;
    private static Solution solution;
    private static int nrNodes, maxDegree;
    private static int candidateLength;
    private static List<Candidate> candidates = new ArrayList<>();
    private static final EntityManager em = DefaultEntityManagerFactory.getInstance().emf.createEntityManager();

    public GeneticAlgorithm(Problem instance) {
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

    public static boolean checkEdgeExists(int a, int b) {
        if(graph[(a - 1) * nrNodes + b - 1] == 1)
            return true;
        return false;
    }

    public static void copy_sir(int[] a, int indexA, int[] b, int indexB, int n) {
        for (int i = 0; i < n; i++)
            a[indexA + i] = b[indexB + i];
    }

    public static void cross_over(int[] sir_A, int indexA, int[] sir_B, int indexB, int n) {
        int index = new Random().nextInt(n - 1);
        for (int i = 0; i < index; i++) {
            sir_A[indexA + i] = sir_B[indexB + i];
        }
        for (int i = index; i < n; i++) {
            sir_B[indexB + i] = sir_A[indexA + i];
        }
    }

    public static void mutate(int[] candidat, int indexCandidat, int n, int kMax) {
        int index = new Random().nextInt(n - 1);
        candidat[indexCandidat + index] = new Random().nextInt(kMax) + 1;
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

    public static void afis_color(int[] candidat, int indexCandidat, int n) {
        for (int i = 0; i < n; i++)
            System.out.print((i + 1) + ": " + candidat[indexCandidat + i] + "; ");
        System.out.println();
    }

    public static int evaluate_graf(int[] candidat, int indexCandidat, int n) {
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

    public static int evaluate_all_graf(int[] candidat, int dimensiune, int n) {
        int best = 100000;
        for (int i = 0; i < dimensiune; i++) {
            int penalty = evaluate_graf(candidat, i * n, n);
            if (penalty < best)
                best = penalty;
        }
        return best;
    }
    public static int alg_gen(int MAX, int dimensiune) {
        int kStart = maxDegree + 1;
        int minim_general = kStart;
        int lastThatActuallyWorked = minim_general;


        int population_size = dimensiune;

        int lgsir = candidateLength * dimensiune + 5;
        int[] sir_curent = new int[lgsir];
        int[] sir_next = new int[lgsir];
        random_color(sir_curent, dimensiune * candidateLength, kStart);

        int tNou = 0;
        boolean wentToFar = false;

        for (int t = 0; t < MAX; t++, tNou++) {
            if (tNou > 20 && wentToFar) {
                kStart++;
                restart_color(sir_curent, dimensiune * candidateLength, kStart);
                minim_general = kStart;
                tNou = 0;
                wentToFar = false;
            }
            int[] valori = new int[405];
            int[] indici = new int[405];
            for (int i = 0; i < population_size; i++) {
                valori[i] = evaluate_graf(sir_curent, i*candidateLength, candidateLength);
                indici[i] = i;
            }

            // elitism
            int nr_best_elitism = 20;
            for (int i = 0; i < nr_best_elitism; i++) {
                int val_min = valori[i];
                int indexMin = i;
                for (int j = i + 1; j < population_size; j++) {
                    if (valori[j] < val_min) {
                        indexMin = j;
                        val_min = valori[j];
                    }
                }
                int tempVal = valori[indexMin];
                valori[indexMin] = valori[i];
                valori[i] = tempVal;

                int tempInd = indici[indexMin];
                indici[indexMin] = indici[i];
                indici[i] = tempInd;

                if (t == MAX - 1 && i == 0) {
                    return lastThatActuallyWorked + 1;
                }
            }
            if (valori[0] == 0) {
                lastThatActuallyWorked = kStart;
                wentToFar = true;
                System.out.println(kStart + 1);

                kStart = kStart - 2;
                //afis_color(sir_curent, indici[0] * n, n);
                restart_color(sir_curent, dimensiune * candidateLength, kStart);
                minim_general = kStart;
                tNou = 0;
            }

            for (int i = 0; i < nr_best_elitism; i++) {
                copy_sir(sir_next, i * candidateLength, sir_curent, indici[i] * candidateLength, candidateLength);
            }

            double un_random = Math.random();
            int de_cate_ori_baga_elitisti = 1;
            if (un_random > 0.33333)
                de_cate_ori_baga_elitisti++;
            if (un_random > 0.53333)
                de_cate_ori_baga_elitisti++;
            if (un_random > 0.700001)
                de_cate_ori_baga_elitisti++;

            for (int i = 1; i < de_cate_ori_baga_elitisti; i++) {
                copy_sir(sir_next, nr_best_elitism * candidateLength * i, sir_next, 0, nr_best_elitism * candidateLength);
            }

            int index_to_copy = nr_best_elitism * de_cate_ori_baga_elitisti;

            // turneu
            int nr_random = 120;
            int nr_best_turneu = 60;
            if (de_cate_ori_baga_elitisti == 2)
                nr_best_turneu = 40;
            else if (de_cate_ori_baga_elitisti == 3)
                nr_best_turneu = 70;
            int cate_turnee = (population_size - nr_best_elitism * de_cate_ori_baga_elitisti) / nr_best_turneu;

            for (int k = 0; k < cate_turnee; k++) {
                // set up
                int[] indici_alesi = new int[405];
                int[] indici_random = new int[405];
                int nr_indici_random = population_size;
                for (int i = 0; i < population_size; i++)
                    indici_random[i] = i;

                // aleg nr random
                for (int i = 0; i < nr_random; i++) {
                    int index_random = (int) (Math.random() * nr_indici_random);
                    indici_alesi[i] = indici_random[index_random];
                    for (int j = index_random; j < nr_indici_random - 1; j++)
                        indici_random[j] = indici_random[j + 1];
                    nr_indici_random -= 1;

                    int valoare_i = evaluate_graf(sir_curent, indici_alesi[i] * candidateLength, candidateLength);
                    valori[i] = valoare_i;
                    indici[i] = indici_alesi[i];
                }

                // aleg cele mai bune din numerele random
                for (int i = 0; i < nr_best_turneu; i++) {
                    int val_min = valori[i];
                    int indexMin = i;
                    for (int j = i + 1; j < nr_random; j++) {
                        if (valori[j] < val_min) {
                            indexMin = j;
                            val_min = valori[j];
                        }
                    }
                    int tempVal = valori[indexMin];
                    valori[indexMin] = valori[i];
                    valori[i] = tempVal;

                    int tempInd = indici[indexMin];
                    indici[indexMin] = indici[i];
                    indici[i] = tempInd;
                }

                // adaug in nou sir
                for (int i = 0; i < nr_best_turneu; i++) {
                    copy_sir(sir_next, index_to_copy * candidateLength, sir_curent, indici[i] * candidateLength, candidateLength);
                    index_to_copy += 1;
                }
            }

            double mutation_probability = 2.0 / (double)(candidateLength * dimensiune);
            double cross_over_probability = 0.9;

            for (int i = 0; i < population_size - 1; i++) {
                double r = Math.random();
                if (r < cross_over_probability) {
                    int index_random = (int) (Math.random() * population_size);
                    cross_over(sir_next, i * candidateLength, sir_next, index_random * candidateLength, candidateLength);
                }
            }

            for (int i = 0; i < population_size; i++) {
                double r = Math.random();
                if (r < mutation_probability) {
                    mutate(sir_next, i * candidateLength, candidateLength, kStart);
                }
            }

            mutation_probability = (6.0 - de_cate_ori_baga_elitisti) / 10.0;
            for (int kk = 0; kk < 3; kk++) {
                for (int i = nr_best_elitism; i < population_size; i++) {
                    double r = Math.random();
                    if (r < mutation_probability) {
                        mutate(sir_next, i * candidateLength, candidateLength, kStart);
                        int index = (int) (Math.random() * candidateLength);
                        sir_next[i * candidateLength + index] = (int) (Math.random() * kStart) + 1;
                    }
                }
            }

            copy_sir(sir_curent, 0, sir_next, 0, population_size * candidateLength);
        }

        return lastThatActuallyWorked + 1;
    }

}
