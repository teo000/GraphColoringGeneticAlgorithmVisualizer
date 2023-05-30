package com.example.tspgaserver.repositories;

import com.example.tspgaserver.entities.Generation;
import com.example.tspgaserver.entities.Problem;
import com.example.tspgaserver.exceptions.ProblemNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemService {

    @Autowired
    ProblemRepository problemRepository;

    public Problem findById(long id){
        return problemRepository.findById(id).orElse(null);
    }
    public List<Problem> findAll(){return problemRepository.findAll();}
    public Problem findByName(String name){
        return problemRepository.findByName(name);}
}
