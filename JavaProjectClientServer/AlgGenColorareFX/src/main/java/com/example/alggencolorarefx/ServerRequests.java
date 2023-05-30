package com.example.alggencolorarefx;

import com.example.alggencolorarefx.graph.Generation;
import com.example.alggencolorarefx.graph.Problem;
import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class ServerRequests {
    public static Problem getProblemInstance(long id){
        HttpResponse<JsonNode> apiResponse = Unirest.get("http://localhost:5000/problem/1").asJson();
        return new Gson().fromJson(apiResponse.getBody().toString(), Problem.class);
    }

    public static long startNewGeneticAlgorithm(long problem_id) {
        return Unirest.post("http://localhost:5000/problem/1").asObject(Long.class).getBody();
    }

    public static Generation getNextGeneration(long solution_id, int currentGeneration){
        HttpResponse<JsonNode> apiResponse = Unirest.get("http://localhost:5000/solution/" + solution_id + "/" + currentGeneration).asJson();
        return new Gson().fromJson(apiResponse.getBody().toString(), Generation.class);
    }
}
