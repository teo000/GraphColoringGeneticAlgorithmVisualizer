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

        gen = geneticAlgorithm;
        Solution solution = new Solution(problem, 2.0, 0.9, 100, 2000, problem.getNodesNo());

        solutionService.saveSolution(solution);
        //Solution solution = geneticAlgorithm.getSolution();
        //geneticAlgorithmRunner.startGA(solution);
        return solution.getId();
    }

    @RequestMapping("/solution/{id}/{genNo}")
    public String getState(@PathVariable long id, @PathVariable int genNo){
        Solution solution = solutionService.findById(id);
        Generation generation = null;

        if (genNo < gen.MAX)
        {
            gen.alg_gen(genNo);
            generation = gen.setBestSoFar(genNo);
            solution.addGeneration(generation);
            solutionService.saveSolution(solution);
            gen.tNou++;
        }
        else
        {
            gen.finalResult = gen.lastThatActuallyWorked + 1;

        }

        //solutionRepository.save(ga.getSolution());

//        generation = solutionService.findGeneration(id, genNo);
        if (generation == null)
            return null;
        return generation.getBestCandidate();
    }
}
