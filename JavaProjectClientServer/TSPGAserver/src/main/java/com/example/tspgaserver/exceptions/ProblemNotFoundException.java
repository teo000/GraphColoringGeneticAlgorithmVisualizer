package com.example.tspgaserver.exceptions;

public class ProblemNotFoundException extends Exception{
    public ProblemNotFoundException(){
        super("Problem not found");
    }
}
