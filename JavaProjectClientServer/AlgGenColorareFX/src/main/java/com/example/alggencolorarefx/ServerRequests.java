package com.example.alggencolorarefx;

import com.example.alggencolorarefx.graph.Candidate;
import com.example.alggencolorarefx.graph.Generation;
import com.example.alggencolorarefx.graph.Problem;
import com.example.alggencolorarefx.graph.Result;
import com.google.gson.Gson;
import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.util.List;

public class ServerRequests {
    public static List<String> getProblemNames(){
        //HttpResponse<String[]> apiResponse = Unirest.get("http://localhost:5000/problem").asObject(List.class);
        //HttpResponse<Item[]> itemResponse = Unirest.get("http://localhost:8080/item").asObject(Item[].class);
        return Unirest.get("http://localhost:5000/problem")
                .asObject(new GenericType<List<String>>(){})
                .getBody();
    }

    public static void addProblemInstance(Problem problem, String algorithmName){
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:5000/problem")
                .header("Content-Type", "application/json")
                .body(problem)
                .asJson();
    }
    public static Problem getProblemInstance(String problem, String algorithmName){
        HttpResponse<JsonNode> apiResponse = Unirest.get("http://localhost:5000/problem/"+problem).asJson();
        //HttpResponse<JsonNode> apiResponse = Unirest.get("http://localhost:5000/problem/1").asJson();
        return new Gson().fromJson(apiResponse.getBody().toString(), Problem.class);
    }

    public static long startNewGeneticAlgorithm(String problem, String algorithmName) {
       // return Unirest.post("http://localhost:5000/problem/1").asObject(Long.class).getBody();
        return Unirest.post("http://localhost:5000/problem/" + problem + "/start" + algorithmName).asObject(Long.class).getBody();
    }

    public static Generation getNextGeneration(long solution_id, int currentGeneration, String algorithmName){
        HttpResponse<JsonNode> apiResponse = Unirest.get("http://localhost:5000/solution/" + solution_id + "/" + currentGeneration + algorithmName).asJson();
        return new Gson().fromJson(apiResponse.getBody().toString(), Generation.class);
    }

    public static Result getFastResult(String problemName, String algorithmName){
        HttpResponse<JsonNode> apiResponse = Unirest.get("http://localhost:5000/problem/"+problemName +"/getResult" + algorithmName).asJson();
        return new Gson().fromJson(apiResponse.getBody().toString(), Result.class);
    }

    public static Generation loadGeneration(long solution_id, int genNo){
        HttpResponse<JsonNode> apiResponse =  Unirest.get("http://localhost:5000/solution/" + solution_id + "/" + genNo + "/load").asJson();
        return new Gson().fromJson(apiResponse.getBody().toString(), Generation.class);
    }
}
