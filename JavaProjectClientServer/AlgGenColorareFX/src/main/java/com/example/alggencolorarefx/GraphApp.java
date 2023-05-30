package com.example.alggencolorarefx;

import com.example.alggencolorarefx.graph.Generation;
import com.example.alggencolorarefx.graph.Problem;
import com.example.alggencolorarefx.graph.Result;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.google.gson.Gson;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;


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
            Color.ORANGE,
            Color.PURPLE,
            Color.CORNFLOWERBLUE,
            Color.DARKSEAGREEN
    };

    private Color[] presetNodeColors = {
            Color.rgb(217, 66, 20),
            Color.rgb(237, 181, 28),
            Color.rgb(44, 124, 199),
            Color.rgb(52, 140, 84),
            Color.rgb(227, 130, 61),
            Color.rgb(35, 36, 120),
            Color.rgb(115, 45, 156)
    };

    String infoAboutGeneticAlg = "Nr nodes: 5 Nr edges: 5 Best nr of colors so far:";
    long solution_id;
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

    private void resetNodesEdgesFromProblemInstance(Problem problem){
        int nrNodes = problem.getNodesNo();
        int nrEdges = problem.getEdgesNo();

        nodePositions = new double[nrNodes][2];
        nodeColors = new Color[nrNodes];
        for (int i = 0; i < nrNodes; i++) {
            nodePositions[i][0] = getRandomDouble(50, 750);
            nodePositions[i][1] = getRandomDouble(50, 450);

            if ( i >= presetNodeColors.length)
                nodeColors[i] = Color.rgb((int)(getRandomDouble(20, 250)), (int)(getRandomDouble(20, 250)), (int)(getRandomDouble(20, 250)));
            else
                nodeColors[i] = presetNodeColors[i];
        }

        edges = new int[nrEdges][2];

        for (int i = 0; i < nrEdges; i++) {
            int n1, n2;
            n1 = problem.getEdges().get(i).node1;
            n2 = problem.getEdges().get(i).node2;
            edges[i][0] = n1 - 1;
            edges[i][1] = n2 - 1;
        }

        infoAboutGeneticAlg = "Nr nodes: " + nrNodes + " Nr edges: " + nrEdges + " Best nr of colors so far:";

    }

    private List<Circle> nodes; // List to store the node circles
    private List<Line> edgesList;
    private Label infoLabel;
    private Label infoGeneticAlgLabel;

    private ComboBox<String> compareTypeComboBox;
    private ComboBox<String> firstComboBox;
    private ComboBox<String> secondComboBox;
    private Button startComparisonButton;


    private GraphPane graphPane;

    private Problem instance;
    private int currentGeneration = 0 ;

    private String chosenProblem;

    private ScheduledExecutorService executorService;

    private List<String> problemNames = new ArrayList<>();

    @Override
    public void init() {
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();
        //root.setPadding(new Insets(10));
        problemNames = ServerRequests.getProblemNames();
        System.out.println(problemNames);

        graphPane = new GraphPane();
        HBox topNavBar = createNavBar();
        HBox bottomNavBar = createBottomBar();

        root.setCenter(graphPane);
        root.setTop(topNavBar);
        root.setBottom(bottomNavBar);

        //root.setBottom(bottomNavBar);


        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("styles.css").toString()); // Load external CSS file
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

    private HBox createBottomBar() {
        HBox bottomBar = new HBox(10);
        bottomBar.setPadding(new Insets(10));

        BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(60, 128, 171), CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        bottomBar.setBackground(background);

        Label infoComparison = new Label("Compare");
        infoComparison.setPadding(new Insets(10));

        compareTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(
                "Two problems", "Two algorithms same problem", "One problem on a number of runs")
        );
        compareTypeComboBox.setPromptText("Choose type of comparison");
        compareTypeComboBox.setOnAction(event -> updateComboBoxOptions());

        firstComboBox = new ComboBox<>();
        secondComboBox = new ComboBox<>();
        firstComboBox.setVisible(false);
        secondComboBox.setVisible(false);

        startComparisonButton = new Button("Start comparison");
        startComparisonButton.setVisible(false);

        startComparisonButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                infoLabel.setText("Start comparison button clicked");
                resetInfoLabelAfterDelay();
            }
        });

        bottomBar.getChildren().addAll(infoComparison, compareTypeComboBox, firstComboBox, secondComboBox, startComparisonButton);
        return bottomBar;
    }

    private void updateComboBoxOptions() {
        String selectedOption = compareTypeComboBox.getValue();
        String[] possibleProblems = {"5", "6", "7"};

        if (selectedOption != null) {
            switch (selectedOption) {
                case "Two problems":
                    // aici setezi gen posibilele instante de probleme
                    firstComboBox.setItems(FXCollections.observableArrayList(
                            possibleProblems));
                    secondComboBox.setItems(FXCollections.observableArrayList(
                            possibleProblems));
                    firstComboBox.setPromptText("Choose first problem");
                    firstComboBox.setVisible(true);
                    secondComboBox.setPromptText("Choose second problem");
                    secondComboBox.setVisible(true);
                    break;
                case "Two algorithms same problem":
                    firstComboBox.setItems(FXCollections.observableArrayList(
                            possibleProblems));
                    firstComboBox.setPromptText("Choose problem");
                    firstComboBox.setVisible(true);
                    secondComboBox.setVisible(false);
                    break;
                case "One problem on a number of runs":
                    firstComboBox.setItems(FXCollections.observableArrayList(
                            possibleProblems));
                    secondComboBox.setItems(FXCollections.observableArrayList(
                            "5", "10", "15", "20", "30"));
                    firstComboBox.setPromptText("Choose problem");
                    firstComboBox.setVisible(true);
                    secondComboBox.setPromptText("Choose number of runs");
                    secondComboBox.setVisible(true);
                    break;
                default:
                    secondComboBox.setItems(FXCollections.emptyObservableList());
                    break;
            }
            startComparisonButton.setVisible(true);
        } else {
            firstComboBox.setVisible(false);
            secondComboBox.setVisible(false);
            startComparisonButton.setVisible(false);
        }
    }
    ComboBox<String> selectDropdown = new ComboBox<>(FXCollections.observableArrayList(
            //"myciel5", "myciel6", "Option 3", "Option 4")
            problemNames)
    );

    ComboBox<String> chooseAlgorithmDropdwon = new ComboBox<>(FXCollections.observableArrayList(
            "Genetic Algorithm", "Simulated Annealing")
    );

    private HBox createNavBar() {
        HBox navbar = new HBox(10);
        navbar.setPadding(new Insets(10));

        BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(60, 128, 171), CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        navbar.setBackground(background);

        Button loadButton = new Button("Load from file");

        selectDropdown.setItems(FXCollections.observableArrayList(
                problemNames)
        );

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
                String fileName = null;

                if (selectedFile != null) {
                    filePath = selectedFile.getAbsolutePath();
                    fileName = selectedFile.getName();
                    System.out.println(filePath);
                    infoLabel.setText("Selected file: " + filePath);

                } else {
                    infoLabel.setText("No file selected");
                }

                resetInfoLabelAfterDelay();
                if (filePath.compareTo("none") != 0) {
                   // resetNodesEdgesFromFile(filePath);
                    instance = new Problem(fileName, filePath);
                    resetNodesEdgesFromProblemInstance(instance);
                    String algorithmType = "";
                    if (chooseAlgorithmDropdwon.getValue() != null) {
                        if (chooseAlgorithmDropdwon.getValue() == "Simulated Annealing")
                            algorithmType = "/SA";
                    }
                    ServerRequests.addProblemInstance(instance, algorithmType);
                    chosenProblem = instance.getName();
                    problemNames.add(chosenProblem);
                    currentGeneration = 0;

                    selectDropdown.setItems(FXCollections.observableArrayList(
                            problemNames)
                    );
                    // System.out.println(instance);
                }
                graphPane.loadGraph();
            }
        });

//        ComboBox<String> selectDropdown = new ComboBox<>(FXCollections.observableArrayList(
//                //"myciel5", "myciel6", "Option 3", "Option 4")
//                problemNames)
//        );
        selectDropdown.setPromptText("Choose problem");
        chooseAlgorithmDropdwon.setPromptText("Choose algorithm");


        Button loadProblem1 = new Button("Load problem");
        loadProblem1.setOnAction (new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
//                resetNodesEdgesFromProblemInstance(problem);
//                graphPane.loadGraph();
//                instance = problem;
//                System.out.println(problem);

                if (selectDropdown.getValue() != null) {
                    String value = selectDropdown.getValue();
                    chosenProblem = value;

                    infoLabel.setText("Loading problem " + value);
                    resetInfoLabelAfterDelay();

                    String algorithmType = "";
                    if (chooseAlgorithmDropdwon.getValue() != null) {
                        if (chooseAlgorithmDropdwon.getValue() == "Simulated Annealing")
                            algorithmType = "/SA";
                    }

                    Problem problem = ServerRequests.getProblemInstance(value, algorithmType);

                    //HttpResponse<JsonNode> apiResponse = Unirest.get("http://localhost:5000/problem/" + value).asJson();
                   // Problem problem = new Gson().fromJson(apiResponse.getBody().toString(), Problem.class);
                    resetNodesEdgesFromProblemInstance(problem);
                    graphPane.loadGraph();
                    instance = problem;
                    System.out.println(problem);
                    currentGeneration = 0;
                }
                else {
                    infoLabel.setText("You have to choose a problem first!");
                    resetInfoLabelAfterDelay();
                }

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

                        Generation generation = ServerRequests.loadGeneration(solution_id, number);
                        currentGeneration = number;

                        infoAboutGeneticAlg = "Nr nodes: " + instance.getNodesNo() + " Nr edges: " + instance.getEdgesNo() + " Best nr of colors so far:" + generation.getBestScore();
                        infoGeneticAlgLabel.setText(infoAboutGeneticAlg);

                        System.out.println(generation.getBestCandidate());
                        updateNodes(generation.getBestCandidate());

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
                String algorithmType = "";
                if (chooseAlgorithmDropdwon.getValue() != null) {
                    if (chooseAlgorithmDropdwon.getValue() == "Simulated Annealing")
                        algorithmType = "/SA";
                }
                solution_id = ServerRequests.startNewGeneticAlgorithm(chosenProblem, algorithmType);
                System.out.println(solution_id);

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                resetInfoLabelAfterDelay();
            }
        });

        Button startAutomatButton = new Button("RunAutomat");
        startAutomatButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Perform action for Start button
                infoLabel.setText("startAutomat Button clicked");
                if (currentGeneration == 0) {
                    String algorithmType = "";
                    if (chooseAlgorithmDropdwon.getValue() != null) {
                        if (chooseAlgorithmDropdwon.getValue() == "Simulated Annealing")
                            algorithmType = "/SA";
                    }
                    solution_id = ServerRequests.startNewGeneticAlgorithm(chosenProblem, algorithmType);
                }

                System.out.println(solution_id);

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                startFetchingData();
                resetInfoLabelAfterDelay();
            }
        });

        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                executorService.shutdown();

                infoLabel.setText("Pause button clicked");

                resetInfoLabelAfterDelay();
            }
        });

        Button updateButton = new Button("Update");
        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                    //String string = Unirest.get("http://localhost:5000/solution/" + solution_id + "/" + currentGeneration).asString().getBody();
                    String algorithmType = "";
                    if (chooseAlgorithmDropdwon.getValue() != null) {
                        if (chooseAlgorithmDropdwon.getValue() == "Simulated Annealing")
                            algorithmType = "/SA";
                    }
                    Generation generation = ServerRequests.getNextGeneration(solution_id, currentGeneration, algorithmType);
                    infoAboutGeneticAlg = "Nr nodes: " + instance.getNodesNo() + " Nr edges: " + instance.getEdgesNo() + " Best nr of colors so far:" + generation.getBestScore();
                    infoGeneticAlgLabel.setText(infoAboutGeneticAlg);

                    System.out.println(generation.getBestCandidate());
                    infoLabel.setText("Update button clicked");
                    updateNodes(generation.getBestCandidate());
                    currentGeneration++;

                resetInfoLabelAfterDelay();
            }
        });

        Button getFastButton = new Button("GetFast");
        getFastButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                infoLabel.setText("Get Fast button clicked");
                String algorithmType = "";
                if (chooseAlgorithmDropdwon.getValue() != null) {
                    if (chooseAlgorithmDropdwon.getValue() == "Simulated Annealing")
                        algorithmType = "/SA";
                }
                Result result = ServerRequests.getFastResult(chosenProblem, algorithmType);
                System.out.println(result);

                infoAboutGeneticAlg = "Nr nodes: " + instance.getNodesNo() + " Nr edges: " + instance.getEdgesNo() + " Best nr of colors: " + result.getFinalResult()
                        + " Execution time: " + result.getTimeAsSeconds();
                infoGeneticAlgLabel.setText(infoAboutGeneticAlg);

                updateNodes(result.getFinalCandidate());

                resetInfoLabelAfterDelay();
            }
        });



        navbar.getChildren().addAll(loadButton, chooseAlgorithmDropdwon, selectDropdown, loadProblem1, inputButton, goToTextField, goToButton, startButton, startAutomatButton, pauseButton, updateButton, getFastButton);
        return navbar;
    }

    private void startFetchingData() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::fetchDataFromServer, 0, 20, TimeUnit.MILLISECONDS);
    }

    private void fetchDataFromServer() {
        try {
            //String string = Unirest.get("http://localhost:5000/solution/" + solution_id + "/" + currentGeneration).asString().getBody();
            String algorithmType = "";
            if (chooseAlgorithmDropdwon.getValue() != null) {
                if (chooseAlgorithmDropdwon.getValue() == "Simulated Annealing")
                    algorithmType = "/SA";
            }
            Generation generation = ServerRequests.getNextGeneration(solution_id, currentGeneration, algorithmType);
            System.out.println(generation.getBestCandidate());
            System.out.println(generation.getCandidates());
            infoAboutGeneticAlg = "Nr nodes: " + instance.getNodesNo() + " Nr edges: " + instance.getEdgesNo() + " Best nr of colors so far:" + generation.getBestScore();
            Platform.runLater(() -> infoGeneticAlgLabel.setText(infoAboutGeneticAlg));
            //infoGeneticAlgLabel.setText(infoAboutGeneticAlg);
            if (!generation.isFinalGen()) {
                updateNodes(generation.getBestCandidate());
                currentGeneration++;
            }
            else {
                executorService.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                for (int i = 0; i < edges.length; i++)
                {
                    int nodeIndex1 = edges[i][0];
                    int nodeIndex2 = edges[i][1];

                    if (indexNode == nodeIndex1)
                    {
                        Line edge = edgesList.get(i);
                        edge.setStroke(Color.RED);

                        nodes.get(nodeIndex2).setStroke(Color.RED);
                        nodes.get(nodeIndex2).setStrokeWidth(2);
                    }
                    else if (indexNode == nodeIndex2)
                    {
                        Line edge = edgesList.get(i);
                        edge.setStroke(Color.RED);

                        nodes.get(nodeIndex1).setStroke(Color.RED);
                        nodes.get(nodeIndex2).setStrokeWidth(2);
                    }
                    else {
                        Line edge = edgesList.get(i);
                        edge.setStroke(Color.rgb(180, 180, 180));
                    }
                }
            }
        });

        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setStroke(Color.BLACK);
                node.setStrokeWidth(1);
                node.setRadius(10);

                for (int i = 0; i < edges.length; i++)
                {
                    int nodeIndex1 = edges[i][0];
                    int nodeIndex2 = edges[i][1];

                    if (indexNode == nodeIndex1)
                    {
                        Line edge = edgesList.get(i);
                        edge.setStroke(Color.rgb(100, 100, 100));

                        nodes.get(nodeIndex2).setStroke(Color.BLACK);
                        nodes.get(nodeIndex2).setStrokeWidth(1);
                    }
                    else if (indexNode == nodeIndex2)
                    {
                        Line edge = edgesList.get(i);
                        edge.setStroke(Color.rgb(100, 100, 100));

                        nodes.get(nodeIndex1).setStroke(Color.BLACK);
                        nodes.get(nodeIndex2).setStrokeWidth(1);
                    }
                    else {
                        Line edge = edgesList.get(i);
                        edge.setStroke(Color.rgb(100, 100, 100));
                    }
                }
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

            if (nodeColors[nodeIndex1] == nodeColors[nodeIndex2])
                edgesList.get(i).setStroke(Color.rgb(166, 58, 22));

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

    private void updateNodes(String string){

        String[] split = string.split(",");
        for(int i = 0 ; i< instance.getNodesNo(); i++) {
            int colorIndex = Integer.parseInt(split[i]);
            Color color = nodeColors[colorIndex];
            nodes.get(i).setFill(color);
            //System.out.println(split[i]);
        }

        for (int i = 0; i < instance.getNodesNo(); i++) {
            updateEdges(nodes.get(i), i);
        }
    }

}