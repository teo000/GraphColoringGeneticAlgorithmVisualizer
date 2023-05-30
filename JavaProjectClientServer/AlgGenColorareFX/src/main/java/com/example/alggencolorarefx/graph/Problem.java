package com.example.alggencolorarefx.graph;

import javafx.scene.paint.Color;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Problem {
    private Long id;
    private String name;
    private int nodesNo;
    private int edgesNo;
    private List<Edge> edges = new ArrayList<>();

    public Problem(String fileName, String filePath){
        this.name = fileName;
        try {
            FileReader fin = new FileReader(filePath);
            Scanner scanner = new Scanner(fin);
            this.nodesNo= scanner.nextInt();
            this.edgesNo = scanner.nextInt();


            for (int i = 0; i < edgesNo; i++) {
                int n1, n2;
                char c;
                c = scanner.next().charAt(0);
                n1 = scanner.nextInt();
                n2 = scanner.nextInt();
                edges.add(new Edge(i, n1, n2));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nodesNo=" + nodesNo +
                ", edgesNo=" + edgesNo +
                ", edges=" + edges +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getNodesNo() {
        return nodesNo;
    }

    public int getEdgesNo() {
        return edgesNo;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}
