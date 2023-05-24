package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

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
//        long problemId = problem.getId();


//        Problem problem1 =
//             em.find(Problem.class, problemId);
//        System.out.println(problem1.getName());
        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}
