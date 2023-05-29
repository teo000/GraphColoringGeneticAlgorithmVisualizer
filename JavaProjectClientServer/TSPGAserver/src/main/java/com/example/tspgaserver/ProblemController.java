package com.example.tspgaserver;

import com.example.tspgaserver.algorithms.GeneticAlgorithm;
import com.example.tspgaserver.entities.Generation;
import com.example.tspgaserver.entities.Problem;
import com.example.tspgaserver.entities.Solution;
import com.example.tspgaserver.exceptions.ProblemNotFoundException;
import com.example.tspgaserver.repositories.ProblemService;
import com.example.tspgaserver.repositories.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProblemController {
    @Autowired
    ProblemService problemService;
    @Autowired
    SolutionService solutionService;
    @Autowired
    GeneticAlgorithmRunner geneticAlgorithmRunner;

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

        solutionService.saveSolution(geneticAlgorithm.getSolution());
        Solution solution = geneticAlgorithm.getSolution();
        geneticAlgorithmRunner.startGA(solution);
        return solution.getId();
    }

    @RequestMapping("/solution/{id}/{genNo}")
    public String getState(@PathVariable long id, @PathVariable int genNo){
        Generation generation = solutionService.findGeneration(id, genNo);
        if (generation == null)
            return null;
        return generation.getBestCandidate();
    }
}
