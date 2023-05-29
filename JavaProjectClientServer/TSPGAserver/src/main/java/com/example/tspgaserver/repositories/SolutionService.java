package com.example.tspgaserver.repositories;

import com.example.tspgaserver.entities.Generation;
import com.example.tspgaserver.entities.Solution;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolutionService {
    @PersistenceContext
    EntityManager manager;
    @Autowired
    SolutionRepository solutionRepository;

    public Solution saveSolution(Solution solution){
//        List<Generation> generations = manager.createNamedQuery("Generation.findBySolutionIdAndGenNO", Generation.class)
//                .setParameter(1, solution.getId())
//                .setParameter(2, generation)
//                .getResultList();
//        if(generations.isEmpty())
            return solutionRepository.save(solution);
//        return null;
    }

    public Generation findGeneration(long solution_id, int generation){
        List<Generation> generations = manager.createNamedQuery("Generation.findBySolutionIdAndGenNO", Generation.class)
                .setParameter(1, solution_id)
                .setParameter(2, generation)
                .getResultList();
        if(!generations.isEmpty())
            return generations.get(0);
        else return null;
    }

    public Solution findById(long id){
        return solutionRepository.findById(id).orElse(null);
    }
}
