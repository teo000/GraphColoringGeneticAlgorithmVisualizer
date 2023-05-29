package com.example.tspgaserver.repositories;

import com.example.tspgaserver.entities.Generation;
import com.example.tspgaserver.entities.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
}
