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
        //this.geneticAlgorithm = geneticAlgorithm;
        //geneticAlgorithm.initGA();
        //geneticAlgorithm.updateSolution();
        //solutionRepository.save(geneticAlgorithm.getSolution());
        executor.execute(() -> runGA(solution.geneticAlgorithm));
//        geneticAlgorithm.saveSolution();
//        solutionRepository.save(geneticAlgorithm.getSolution());

//        for (int t = 0; t < geneticAlgorithm.MAX && geneticAlgorithm.running; t++, geneticAlgorithm.tNou++) {
//            geneticAlgorithm.alg_gen(t);
//            //          if(t%10 == 0) {
//            //               ga.updateSolution(t);
////            else
//            geneticAlgorithm.setBestSoFar(t);
//            solutionRepository.save(geneticAlgorithm.getSolution());
//            //           }
//        }
//        geneticAlgorithm.finalResult = geneticAlgorithm.lastThatActuallyWorked + 1;
//
//        geneticAlgorithm.updateSolution(2000);
//        solutionRepository.save(geneticAlgorithm.getSolution());

    }
    public void runGA(GeneticAlgorithm ga){

        for (int t = 0; t < ga.MAX && ga.running; t++, ga.tNou++) {
            ga.alg_gen(t);
            ga.setBestSoFar(t);
            solutionService.saveSolution(ga.getSolution());
        }
        ga.finalResult = ga.lastThatActuallyWorked + 1;

        ga.updateSolution(2000);
        solutionRepository.save(ga.getSolution());

    }
}
