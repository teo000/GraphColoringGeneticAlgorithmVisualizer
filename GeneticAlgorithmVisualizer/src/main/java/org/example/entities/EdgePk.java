package org.example.entities;

import java.io.Serializable;
import java.util.Objects;

public class EdgePk implements Serializable {
    private long problemId;
    private int edgeNo;

    public long getProblemId() {
        return problemId;
    }

    public int getEdgeNo() {
        return edgeNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgePk edgePk = (EdgePk) o;
        return problemId == edgePk.problemId && edgeNo == edgePk.edgeNo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(problemId, edgeNo);
    }
}
