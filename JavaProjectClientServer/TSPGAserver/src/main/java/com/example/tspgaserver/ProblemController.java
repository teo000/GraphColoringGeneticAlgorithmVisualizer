package com.example.tspgaserver;

import com.example.tspgaserver.algorithms.GeneticAlgorithm;
import com.example.tspgaserver.entities.Generation;
import com.example.tspgaserver.entities.Problem;
import com.example.tspgaserver.entities.Solution;
import com.example.tspgaserver.exceptions.GenerationNotValidException;
import com.example.tspgaserver.exceptions.ProblemNotFoundException;
import com.example.tspgaserver.repositories.ProblemService;
import com.example.tspgaserver.repositories.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProblemController {
    @Autowired
    ProblemService problemService;
    @Autowired
    SolutionService solutionService;
    Map<Long, GeneticAlgorithm> GAs = new HashMap<>();
    GeneticAlgorithm gen;

    @RequestMapping("/problem/{id}")
    public Problem fetchProblem(@PathVariable long id) throws ProblemNotFoundException {
        System.out.println("hei");
        Problem problem = problemService.findById(id);
        if(problem == null)
            throw new ProblemNotFoundException();
        return problem;
    }

    @PostMapping("/problem/{id}")
    public long startAlg(@PathVariable long id) throws ProblemNotFoundException {
        System.out.println("start Alg");
        Problem problem = problemService.findById(id);
        if(problem == null)
            throw new ProblemNotFoundException();

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(problem);
        geneticAlgorithm.initGA();
        Solution solution = new Solution(problem, 2.0, 0.9, 100, 2000, problem.getNodesNo());


        solutionService.saveSolution(solution);
        GAs.put(solution.getId(), geneticAlgorithm);

        //Solution solution = geneticAlgorithm.getSolution();
        //geneticAlgorithmRunner.startGA(solution);
        return solution.getId();
    }

    @RequestMapping("/solution/{id}/{genNo}")
    public Generation getState(@PathVariable long id, @PathVariable int genNo) throws GenerationNotValidException {
        Solution solution = solutionService.findById(id);
        GeneticAlgorithm ga = GAs.get(solution.getId());

        if (genNo > ga.MAX) {
            throw new GenerationNotValidException(genNo);
        }
        Generation generation = null;

        ga.alg_gen(genNo);
        generation = ga.setBestSoFar(genNo);
        solution.addGeneration(generation);
        ga.tNou++;

        if(genNo == ga.MAX){
            ga.finalResult = ga.lastThatActuallyWorked + 1;
            solution.setOverallBestScore(ga.finalResult);
            solution.setOverallBestCandidate(generation.getBestCandidate());
            generation.setFinalGen(true);
        }

        solutionService.saveSolution(solution);

        return generation;


//        if (genNo > ga.MAX) {
//            return "STOP";
//        }
//        else {
//            Generation generation = null;
//
//            if (genNo < ga.MAX) {
//                ga.alg_gen(genNo);
//                generation = ga.setBestSoFar(genNo);
//                solution.addGeneration(generation);
//                solutionService.saveSolution(solution);
//                ga.tNou++;
//            } else {
//                ga.finalResult = ga.lastThatActuallyWorked + 1;
//
//            }
//
//            if (generation == null)
//                return null;
//            return generation.getBestCandidate();
//        }

    }

//    @RequestMapping("/problem/{id}/getResult")
//    public String getFast(@PathVariable long id){
//
//    }

}
