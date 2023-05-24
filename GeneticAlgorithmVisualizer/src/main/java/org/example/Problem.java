package org.example;

import jakarta.persistence.*;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import java.util.List;

@Entity
@Table(name = "problem_instances")
@NamedQueries({
        @NamedQuery(name = "Problem.findByName",
                query = "SELECT p FROM Problem p WHERE p.name=:name")})
public class Problem {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    @OneToMany(targetEntity = Edge.class, cascade = CascadeType.ALL,
                fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    private List<Edge> edges;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}