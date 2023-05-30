package com.example.alggencolorarefx.graph;

public class Result {
    private int finalResult;
    private String finalCandidate;
    private long timeMillis;

    public int getFinalResult() {
        return finalResult;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public String getFinalCandidate() {
        return finalCandidate;
    }

    @Override
    public String toString() {
        return "Result{" +
                "finalResult=" + finalResult +
                ", finalCandidate='" + finalCandidate + '\'' +
                ", timeMillis=" + timeMillis +
                '}';
    }

    public float getTimeAsSeconds(){
        return (float) timeMillis /1000;
    }
}
