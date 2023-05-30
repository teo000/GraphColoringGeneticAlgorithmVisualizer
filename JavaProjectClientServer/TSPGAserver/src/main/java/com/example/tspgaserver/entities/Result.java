package com.example.tspgaserver.entities;

public class Result {
    private int finalResult;
    private String finalCandidate;
    private long timeMillis;

    public Result(String finalCandidate, int finalResult) {
        this.finalResult = finalResult;
        this.finalCandidate = finalCandidate;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public int getFinalResult() {
        return finalResult;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public String getFinalCandidate() {
        return finalCandidate;
    }
}
