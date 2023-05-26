package org.example;

import jakarta.persistence.EntityManager;
import org.example.algorithms.GeneticAlgorithm;
import org.example.entities.Candidate;
import org.example.entities.Node;
import org.example.entities.Problem;
import org.example.entities.Solution;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManager em = DefaultEntityManagerFactory.getInstance().emf.createEntityManager();

        Problem problem =
                (Problem) em.createNamedQuery("Problem.findByName")
                .setParameter("name", "myciel5")
                .getSingleResult();
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(problem);
        int result = geneticAlgorithm.alg_gen(2000, 200);
        System.out.println("REZULTAT FINAL: " + result);
        em.close();
    }
}
