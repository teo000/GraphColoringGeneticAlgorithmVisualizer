package com.example.tspgaserver;

import com.example.tspgaserver.algorithms.GeneticAlgorithm;
import com.example.tspgaserver.entities.Solution;
import com.example.tspgaserver.repositories.SolutionRepository;
import com.example.tspgaserver.repositories.SolutionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class GeneticAlgorithmRunner {
    @Autowired
    SolutionService solutionService;
    @Autowired
    SolutionRepository solutionRepository;

    ExecutorService executor = Executors.newFixedThreadPool(4);

    public void startGA(Solution solution){

        GeneticAlgorithm ga = solution.geneticAlgorithm;
        for (int t = 0; t < ga.MAX && ga.running; t++, ga.tNou++) {
            ga.alg_gen(t);
            ga.setBestSoFar(t);
            solutionService.saveSolution(ga.getSolution());
        }
        ga.finalResult = ga.lastThatActuallyWorked + 1;

        solutionRepository.save(ga.getSolution());
        /*
        //executor.execute(() -> runGA(solution.geneticAlgorithm));
        executor.submit(() -> {
            runGA(solution.geneticAlgorithm);
        });
        */
    }
    public void runGA(GeneticAlgorithm ga){

        for (int t = 0; t < ga.MAX && ga.running; t++, ga.tNou++) {
            ga.alg_gen(t);
            ga.setBestSoFar(t);
            solutionService.saveSolution(ga.getSolution());
        }
        ga.finalResult = ga.lastThatActuallyWorked + 1;

        //ga.updateSolution(2000);
        //solutionRepository.save(ga.getSolution());

    }
}
