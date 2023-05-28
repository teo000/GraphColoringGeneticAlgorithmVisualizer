package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class GraphApp extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    // Define the node positions
    private double[][] nodePositions = {
            {100, 100},
            {200, 200},
            {300, 350},
            {200, 400},
            {500, 500}
    };

    // Define the edges (connections between nodes)
    private int[][] edges = {
            {0, 1},
            {0, 2},
            {1, 3},
            {2, 4},
            {3, 4}
    };

    // Define the node colors
    private Color[] nodeColors = {
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.GREEN,
            Color.PURPLE
    };

    String infoAboutGeneticAlg = "Nr nodes: 5 Nr edges: 5 Best nr of colors so far:";

    private double getRandomDouble(double min, double max) {
        return min + Math.random() * (max - min);
    }

    private void resetNodesEdgesFromFile(String fileName){
        try {
            FileReader fin = new FileReader(fileName);
            Scanner scanner = new Scanner(fin);
            int nrNodes = scanner.nextInt();
            int nrEdges = scanner.nextInt();

            nodePositions = new double[nrNodes][2];
            nodeColors = new Color[nrNodes];
            for (int i = 0; i < nrNodes; i++) {
                nodePositions[i][0] = getRandomDouble(50, 750);
                nodePositions[i][1] = getRandomDouble(50, 450);

                float hue = 0; // Initial hue value
                float saturation = 1.0f; // Adjust saturation as desired
                float brightness = 1.0f; // Adjust brightness as desired
                //nodeColors[i] = Color.hsb((hue + i * 15) % 360, 1f, 1f);
                nodeColors[i] = Color.rgb((int)(getRandomDouble(20, 250)), (int)(getRandomDouble(20, 250)), (int)(getRandomDouble(20, 250)));
            }

            edges = new int[nrEdges][2];

            for (int i = 0; i < nrEdges; i++) {
                int n1, n2;
                char c;
                c = scanner.next().charAt(0);
                n1 = scanner.nextInt();
                n2 = scanner.nextInt();
                edges[i][0] = n1 - 1;
                edges[i][1] = n2 - 1;
            }

            infoAboutGeneticAlg = "Nr nodes: " + nrNodes + " Nr edges: " + nrEdges + " Best nr of colors so far:";
        }
        catch (IOException e) {
            infoLabel.setText("Error when opening file!");
        }
    }

    private List<Circle> nodes; // List to store the node circles
    private List<Line> edgesList;
    private Label infoLabel;
    private Label infoGeneticAlgLabel;

    private GraphPane graphPane;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        //root.setPadding(new Insets(10));

        graphPane = new GraphPane();
        HBox topNavBar = createNavBar();
        //HBox bottomNavBar = createNavBar();

        root.setCenter(graphPane);
        root.setTop(topNavBar);

        //root.setBottom(bottomNavBar);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org.example/styles.css").toString()); // Load external CSS file
        primaryStage.setTitle("Graph Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class GraphPane extends Pane {
        public GraphPane() {
            nodes = new ArrayList<>(); // Initialize the list for storing the node circles
            edgesList = new ArrayList<>();
            BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(210, 214, 217), CornerRadii.EMPTY, Insets.EMPTY);
            Background background = new Background(backgroundFill);
            setBackground(background);

            loadGraph();
        }

        public void loadGraph()
        {
            nodes.clear();
            edgesList.clear();
            getChildren().clear();

            HBox information = new HBox();

            infoGeneticAlgLabel = new Label(infoAboutGeneticAlg);
            infoGeneticAlgLabel.setPadding(new Insets(10));

            infoLabel = new Label("");
            infoLabel.setPadding(new Insets(10));
            infoLabel.setPadding(new Insets(10, 10, 10, 100));
            infoLabel.setStyle("-fx-text-fill: rgb(33, 102, 145);"); // Set font size and color

            information.getChildren().addAll(infoGeneticAlgLabel, infoLabel);
            getChildren().add(information);

            // Create nodes and edges
            createEdges(this);
            createNodes(this);
        }
    }


    private HBox createNavBar() {
        HBox navbar = new HBox(10);
        navbar.setPadding(new Insets(10));

        BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(60, 128, 171), CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        navbar.setBackground(background);

        Button loadButton = new Button("Load from file");
        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Perform action for Load from file button
                infoLabel.setText("Load button clicked");

                String filePath = "";

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select File");

                // Set the initial directory
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

                // Show the file chooser dialog
                File selectedFile = fileChooser.showOpenDialog(null);

                if (selectedFile != null) {
                    filePath = selectedFile.getAbsolutePath();
                    System.out.println(filePath);
                    infoLabel.setText("Selected file: " + filePath);
                } else {
                    infoLabel.setText("No file selected");
                }

                resetInfoLabelAfterDelay();
                if (filePath.compareTo("none") != 0)
                    resetNodesEdgesFromFile(filePath);
                graphPane.loadGraph();
            }
        });

        Button inputButton = new Button("Input manually");
        inputButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Perform action for Input manually button
                infoLabel.setText("Input button clicked");
                resetInfoLabelAfterDelay();
            }
        });

        TextField goToTextField = new TextField();
        goToTextField.setPromptText("Enter a number divisible by 50");

        Button goToButton = new Button("Go to");
        goToButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Perform action for Go to button
                String inputText = goToTextField.getText();
                try {
                    int number = Integer.parseInt(inputText);
                    if (number % 50 == 0) {
                        infoLabel.setText("Go to button clicked: " + number);
                        resetInfoLabelAfterDelay();
                    } else {
                        infoLabel.setText("Please enter a number divisible by 50");
                        resetInfoLabelAfterDelay();
                    }
                } catch (NumberFormatException e) {
                    infoLabel.setText("Please enter a valid number");
                    resetInfoLabelAfterDelay();
                }
            }
        });

        Button startButton = new Button("Start");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Perform action for Start button
                infoLabel.setText("Start button clicked");
                resetInfoLabelAfterDelay();
            }
        });

        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Perform action for Pause button
                infoLabel.setText("Pause button clicked");
                resetInfoLabelAfterDelay();
            }
        });

        navbar.getChildren().addAll(loadButton, inputButton, goToTextField, goToButton, startButton, pauseButton);
        return navbar;
    }

    private void resetInfoLabelAfterDelay() {
        Duration delay = Duration.seconds(3);

        KeyFrame keyFrame = new KeyFrame(delay, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                infoLabel.setText("");
            }
        });

        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    private void createNodes(Pane pane) {
        for (int i = 0; i < nodePositions.length; i++) {
            double x = nodePositions[i][0];
            double y = nodePositions[i][1];
            Circle node = createNode(x, y, nodeColors[i], i);
            pane.getChildren().add(node);
            nodes.add(node); // Add the circle to the list
        }
    }

    private Circle createNode(double x, double y, Color color, int indexNode) {
        Circle node = new Circle(x, y,10);
        node.setFill(color);
        node.setStroke(Color.BLACK);
        node.setStrokeWidth(1);

        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setStroke(Color.RED);
                node.setStrokeWidth(2);
                node.setRadius(12);
            }
        });

        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setStroke(Color.BLACK);
                node.setStrokeWidth(1);
                node.setRadius(10);
            }
        });

        // Add event handlers for dragging
        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Store the initial mouse coordinates
                node.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
            }
        });

        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double[] userData = (double[]) node.getUserData();
                double offsetX = event.getSceneX() - userData[0];
                double offsetY = event.getSceneY() - userData[1];

                // Update node position
                node.setCenterX(node.getCenterX() + offsetX);
                node.setCenterY(node.getCenterY() + offsetY);

                // Update user data with new mouse coordinates
                node.setUserData(new double[]{event.getSceneX(), event.getSceneY()});

                updateEdges(node, indexNode);
            }
        });

        return node;
    }

    private void updateEdges(Circle node, int indexNode) {
        int index = 0;
        for (int i = 0; i < edges.length; i++)
        {
            int nodeIndex1 = edges[i][0];
            int nodeIndex2 = edges[i][1];

            if (indexNode == nodeIndex1)
            {
                Line edge = edgesList.get(i);
                edge.setStartX(node.getCenterX());
                edge.setStartY(node.getCenterY());
            }
            else if (indexNode == nodeIndex2)
            {
                Line edge = edgesList.get(i);
                edge.setEndX(node.getCenterX());
                edge.setEndY(node.getCenterY());
            }
        }
    }


    private void createEdges(Pane pane) {
        for (int[] edge : edges) {
            int nodeIndex1 = edge[0];
            int nodeIndex2 = edge[1];

            double x1 = nodePositions[nodeIndex1][0];
            double y1 = nodePositions[nodeIndex1][1];
            double x2 = nodePositions[nodeIndex2][0];
            double y2 = nodePositions[nodeIndex2][1];
            Line line = new Line(x1, y1, x2, y2);
            if (nodeColors[nodeIndex1] == nodeColors[nodeIndex2])
                line.setStroke(Color.rgb(166, 58, 22));
            else
                line.setStroke(Color.rgb(100, 100, 100));
            pane.getChildren().add(line);

            edgesList.add(line);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}