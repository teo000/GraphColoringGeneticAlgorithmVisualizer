package com.example.tspgaserver.exceptions;

public class GenerationNotValidException extends Exception{
    public GenerationNotValidException(long gen) {
        super("Generation " + gen + " not valid.");
    }
}
