package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entities.Candidate;
import org.example.entities.Node;
import org.example.entities.Problem;
import org.example.entities.Solution;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Problem problem =
                (Problem) em.createNamedQuery("Problem.findByName")
                .setParameter("name", "myciel5")
                .getSingleResult();
        System.out.println(problem.getEdges().get(0));
        Solution solution = new Solution(problem, 1.0, 0.5, 200, 2000);
        solution.addCandidate(new Candidate(1, 1, List.of(new Node(1, 13))));

        em.persist(solution);
        em.getTransaction().commit();
        em.close();
        emf.close();


    }
}
