package com.example.tspgaserver;

import com.example.tspgaserver.algorithms.GeneticAlgorithm;
import com.example.tspgaserver.algorithms.GraphColoringAlgorithm;
import com.example.tspgaserver.algorithms.SimulatedAnnealing;
import com.example.tspgaserver.entities.Generation;
import com.example.tspgaserver.entities.Problem;
import com.example.tspgaserver.entities.Result;
import com.example.tspgaserver.entities.Solution;
import com.example.tspgaserver.exceptions.GenerationNotValidException;
import com.example.tspgaserver.exceptions.ProblemNotFoundException;
import com.example.tspgaserver.repositories.ProblemService;
import com.example.tspgaserver.repositories.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProblemController {
    @Autowired
    ProblemService problemService;
    @Autowired
    SolutionService solutionService;
    Map<Long, GraphColoringAlgorithm> GAs = new HashMap<>();
    GeneticAlgorithm gen;


    @RequestMapping("/problem/{name}")
    public Problem fetchProblem(@PathVariable String name) throws ProblemNotFoundException {
        System.out.println("hei");
        Problem problem = problemService.findByName(name);
        if(problem == null)
            throw new ProblemNotFoundException();
        return problem;
    }

    @RequestMapping("/problem")
    public List<String> fetchProblemList() throws ProblemNotFoundException {
        System.out.println("heihei");
        return problemService.findAll().stream().map(Problem::getName).toList();
    }
    @PostMapping(value = "/problem", consumes = "application/json")
    public Problem addProblemTodDB(@RequestBody Problem problem){
        System.out.println(problem);
        return problemService.saveInstance(problem);
    }

    @PostMapping("/problem/{name}/start")
    public long startAlg(@PathVariable String name) throws ProblemNotFoundException {
        System.out.println("start Alg");
        Problem problem = problemService.findByName(name);
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

    @PostMapping("/problem/{name}/start/SA")
    public long startAlgSA(@PathVariable String name) throws ProblemNotFoundException {
        System.out.println("start Alg SA");
        Problem problem = problemService.findByName(name);
        if(problem == null)
            throw new ProblemNotFoundException();

        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(problem);
        simulatedAnnealing.initGA();
        Solution solution = new Solution(problem, 2.0, 0.9, 100, 2000, problem.getNodesNo());


        solutionService.saveSolution(solution);
        GAs.put(solution.getId(), simulatedAnnealing);

        return solution.getId();
    }



    @RequestMapping("/solution/{id}/{genNo}")
    public Generation getState(@PathVariable long id, @PathVariable int genNo) throws GenerationNotValidException {
        Solution solution = solutionService.findById(id);
        GeneticAlgorithm ga = (GeneticAlgorithm)GAs.get(solution.getId());

        if (genNo > ga.MAX) {
            throw new GenerationNotValidException(genNo);
        }
        Generation generation = null;

        ga.alg_gen(genNo);
        generation = ga.setBestSoFar(genNo);
        ga.tNou++;

        if(genNo % 50 == 0)
            generation.getCandidates().addAll(ga.getAllCandidates());

        solution.addGeneration(generation);


        if(genNo == ga.MAX){
            ga.finalResult = ga.lastThatActuallyWorked + 1;
            solution.setOverallBestScore(ga.finalResult);
            solution.setOverallBestCandidate(generation.getBestCandidate());
            generation.setFinalGen(true);
        }

        solutionService.saveSolution(solution);

        return generation;
    }

    @RequestMapping("/solution/{id}/{genNo}/SA")
    public Generation getStateSA(@PathVariable long id, @PathVariable int genNo) throws GenerationNotValidException {
        Solution solution = solutionService.findById(id);
        SimulatedAnnealing sa = (SimulatedAnnealing) GAs.get(solution.getId());

        Generation generation = null;

        sa.run(genNo);

        generation = sa.setBestSoFar(genNo);
        solution.addGeneration(generation);

        if(sa.T <= 0.00001){
            sa.finalResult = sa.best;
            solution.setOverallBestScore(sa.finalResult);
            solution.setOverallBestCandidate(generation.getBestCandidate());
            generation.setFinalGen(true);
        }

        solutionService.saveSolution(solution);

        return generation;
    }

    @RequestMapping("/problem/{name}/getResult")
    public Result getFast(@PathVariable String name) throws ProblemNotFoundException {
        Problem problem = problemService.findByName(name);
        if(problem == null)
            throw new ProblemNotFoundException();

        long startTime = System.currentTimeMillis();

        GeneticAlgorithm ga = new GeneticAlgorithm(problem);
        ga.initGA();

        for (int t = 0; t < ga.MAX && ga.running; t++, ga.tNou++)
            ga.alg_gen(t);

        Result result = ga.getFinalResult();
        result.setTimeMillis(System.currentTimeMillis() - startTime);
        return result;
    }

    @RequestMapping("/solution/{id}/{genNo}/load")
    public Generation load(@PathVariable long id, @PathVariable int genNo) throws GenerationNotValidException {
        Solution solution = solutionService.findById(id);
        GeneticAlgorithm ga = (GeneticAlgorithm) GAs.get(solution.getId());

        if (genNo > ga.MAX) {
            throw new GenerationNotValidException(genNo);
        }
        ga.tNou = genNo + 1;

        Generation generation = solution.getGenerations().stream().filter(g -> g.getGenNo() == genNo).findFirst().orElse(null);

        ga.updateSirCurent(generation.getCandidates());

        return solution.getGenerations().stream().filter(g -> g.getGenNo() == genNo).findFirst().orElse(null);
    }


    @RequestMapping("/problem/{name}/getResult/SA")
    public Result getFastSA(@PathVariable String name) throws ProblemNotFoundException {
        Problem problem = problemService.findByName(name);
        if(problem == null)
            throw new ProblemNotFoundException();

        long startTime = System.currentTimeMillis();

        SimulatedAnnealing sa = new SimulatedAnnealing(problem);
        sa.initGA();

        for (int t = 0; sa.T > 0.00001; t++)
            sa.run(t);

        Result result = sa.getFinalResult();
        result.setTimeMillis(System.currentTimeMillis() - startTime);
        return result;
    }



}
