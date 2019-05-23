package sample;

import Enumerations.EdgeTypes;
import Enumerations.SimulationType;
import Enumerations.GridShape;
import Enumerations.States;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import utility.XMLCheck;
import utility.XMLException;
import utility.XMLReader;

import java.io.File;
import java.util.*;

/**
 * @author Natalie Le (nl121)
 */

// TODO: ELIMINATE MAGIC VALUE IN PARAMETER

public class GUI {
    private final double SCREEN_HEIGHT;
    private final double SCREEN_WIDTH;
    private final double GRID_WIDTH = 450;
    private final int SIMULATION_TEXT_VERTICAL_SPACING = 30;
    private final int BUTTONS_VERTICAL_SPACING = 45;
    private final int BUTTONS_HORIZONTAL_SPACING = 75;
    private final int DEFAULT_MILLISECOND_DELAY = 100;
    private final int SIMULATION_TEXT_SPACING = 15;
    private final int SPEED_TOGGLE_AMOUNT = 25;
    private final int SPEED_TEXT_VERTICAL_SPACING = 25;
    private final int PARAM_HORIZONTAL_SPACING = 10;
    private final int PARAM_VERTICAL_SPACING = 30;
    private final int SPEED_TITLE_OFFSET = 315;
    private final int SLOW_BUTTON_OFFSET = 270;
    private final int FAST_BUTTON_OFFSET = 240;
    private int MILLISECOND_DELAY;

    private Group myRoot;
    private Grid myGrid;
    private final FileChooser myFileChooser;
    private Timeline animationTimeline;
    private Text simulationName;
    private Text simulationDescription;
    private Text xmlText;

    private Button myPlayButton;
    private ResourceBundle myResources;
    private ResourceBundle myErrors;

    private String myGameName;
    private String xmlContentText;
    private String myGameDescription;
    private boolean isPaused;
    private Set<Node> removableNodes;
    private SimulationType simulationType;
    private int cellsPerColumn;
    private int cellsPerRow;
    private Graph myGraph;
    public static final String fileName = "resources/GUItest";
    private ArrayList<String> myInitialParams;
    private Map<String, Double> myThresholds;
    private Map<String, Integer> myStates;
    private Map<States, Color> userColors;
    private Map<States, Color> currentColors;
    private GridShape myGridShape;
    private EdgeTypes myEdgeType;
    private static final String fileError = "resources/Errors";


    public GUI(Stage stage, int screenWidth, int screenHeight){
        SCREEN_WIDTH = screenWidth / 2;
        SCREEN_HEIGHT = screenHeight;
        myResources = ResourceBundle.getBundle(fileName);
        removableNodes = new HashSet<>();
        myErrors = ResourceBundle.getBundle(fileError);
        myRoot = new Group();
        isPaused = true;
        myFileChooser = new FileChooser();
        MILLISECOND_DELAY = DEFAULT_MILLISECOND_DELAY;
        addSimulationText();
        addPlayButtons(stage);
        addSpeedComponents(stage);
        setTimeline();
        Line panelDivider = new Line(GRID_WIDTH, 0, GRID_WIDTH, SCREEN_HEIGHT);
        myRoot.getChildren().add(panelDivider);
    }

    private void displaySimulationText(String simulationName, String simulationDescription){
        this.simulationName.setText( myResources.getString("simulationName") + simulationName);
        this.simulationDescription.setText(simulationDescription);
        xmlText.setX(this.simulationName.getLayoutBounds().getWidth() + this.simulationDescription.getX() + SIMULATION_TEXT_SPACING);
        xmlText.setText(myResources.getString("viewXML"));
        xmlText.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(myResources.getString("XMLContents"));
            alert.setContentText(xmlContentText);
            alert.showAndWait();
        });
    }

    private void step(){
        if (myGrid != null){
            myGrid.updateCells();
            myGraph.update(myGrid.getExistingCells());
        }
    }

    private void play(){
        if (myGameName == null) return;
        if (myPlayButton.getText().equals(myResources.getString("myPlayButton"))) {
            myPlayButton.setText(myResources.getString("myPauseButton"));
        } else {
            myPlayButton.setText(myResources.getString("myPlayButton"));
        }
        isPaused = !isPaused;
    }

    private void resetGame(){
        myRoot.getChildren().remove(myGrid.getGridRoot());
        myGrid = null;
        animationTimeline.stop();
        MILLISECOND_DELAY = DEFAULT_MILLISECOND_DELAY;
        setTimeline();
        displayGame();
    }

    private void uploadXMl(Stage stage){
        File file = myFileChooser.showOpenDialog(stage);
        if (file != null){
            XMLReader reader = new XMLReader(file);
            XMLCheck check = new XMLCheck(reader);
            myGameName = check.getName();
            xmlContentText = reader.getXMLText();
            myGameDescription = check.getDescription();
            myInitialParams = check.getInitialParams();
            myGridShape = check.getGridShape();
            userColors = check.getCellColor();
            myThresholds = check.getThresholds();
            myStates = check.getStates();
            cellsPerColumn = check.getHeight();
            cellsPerRow = check.getWidth();
            myEdgeType = check.getEdgetypes();
            simulationType = check.getName().contains(" ") ? SimulationType.GAME_OF_LIFE :
                    SimulationType.valueOf(reader.getName().toUpperCase());
            currentColors = getSimulationColors();
            displayGame();
        } else {
            new Alert(Alert.AlertType.ERROR, myErrors.getString("NoFile")).showAndWait();
            throw new XMLException(myErrors.getString("NoFile"));
        }
    }

    private void increaseSpeed(){
        animationTimeline.stop();
        if (MILLISECOND_DELAY > SPEED_TOGGLE_AMOUNT) MILLISECOND_DELAY -= SPEED_TOGGLE_AMOUNT;
        setTimeline();
    }

    private void decreaseSpeed(){
        animationTimeline.stop();
        MILLISECOND_DELAY += SPEED_TOGGLE_AMOUNT;
        setTimeline();
    }


    private void displayGame(){
        removeNodes();
        if (myEdgeType.equals(EdgeTypes.TOROIDAL)) {
            myGrid = new GridToroidal(myInitialParams, myThresholds, simulationType, myGridShape, cellsPerColumn, cellsPerRow, GRID_WIDTH, getSimulationColors());
        } else if (myEdgeType.equals(EdgeTypes.FINITE)){
            myGrid = new Grid(myInitialParams, myThresholds, simulationType, myGridShape, cellsPerColumn, cellsPerRow, GRID_WIDTH, getSimulationColors());
        }
        displaySimulationText(myGameName, myGameDescription);
        Parameter parameter = new Parameter(this, myStates, myThresholds, (int) (GRID_WIDTH),
                PARAM_HORIZONTAL_SPACING, PARAM_VERTICAL_SPACING, cellsPerColumn * cellsPerRow,
                (int) (SCREEN_WIDTH), (int) SCREEN_HEIGHT);
        removableNodes.add(parameter.getRoot());
        myRoot.getChildren().add(parameter.getRoot());
        isPaused = true;
        myPlayButton.setText(myResources.getString("myPlayButton"));
        removableNodes.add(myGrid.getGridRoot());
        myGraph = new Graph(200, 200, GRID_WIDTH + (2 * PARAM_HORIZONTAL_SPACING),
                SCREEN_HEIGHT - 0.25 * BUTTONS_VERTICAL_SPACING,
                cellsPerColumn * cellsPerRow, currentColors);
        removableNodes.add(myGraph.getRoot());
        myRoot.getChildren().add(myGrid.getGridRoot());
        myRoot.getChildren().add(myGraph.getRoot());
    }

    private void removeNodes(){
        for (Node node : removableNodes){
            myRoot.getChildren().remove(node);
        }
        removableNodes.clear();
    }

    private void setTimeline(){
        var animationFrame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> { if (!isPaused) step(); });
        animationTimeline = new Timeline();
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
        animationTimeline.getKeyFrames().add(animationFrame);
        animationTimeline.play();
    }

    private void addSimulationText(){
        simulationName = new Text(SIMULATION_TEXT_SPACING, GRID_WIDTH + 0.5 * SIMULATION_TEXT_VERTICAL_SPACING,
                myResources.getString("simulationName"));
        simulationDescription = new Text();
        simulationDescription.setX(SIMULATION_TEXT_SPACING);
        simulationDescription.setY(GRID_WIDTH + 2.5 * SIMULATION_TEXT_SPACING);
        xmlText = new Text();
        xmlText.setY(this.simulationName.getY());
        xmlText.setUnderline(true);
        xmlText.setFill(Color.BLUE);
        myRoot.getChildren().add(simulationName);
        myRoot.getChildren().add(simulationDescription);
        myRoot.getChildren().add(xmlText);
    }

    private void addPlayButtons(Stage stage){
        Button myUploadButton = new Button(myResources.getString("myUploadButton"));
        myUploadButton.setLayoutX(SIMULATION_TEXT_SPACING);
        myUploadButton.setLayoutY(SCREEN_HEIGHT - BUTTONS_VERTICAL_SPACING);
        myUploadButton.setOnAction(event -> uploadXMl(stage));

        myPlayButton = new Button(myResources.getString("myPlayButton"));
        myPlayButton.setLayoutX(GRID_WIDTH - 1 * BUTTONS_HORIZONTAL_SPACING);
        myPlayButton.setLayoutY(SCREEN_HEIGHT - BUTTONS_VERTICAL_SPACING);
        myPlayButton.setOnAction(event -> play());

        Button myStepButton = new Button(myResources.getString("myStepButton"));
        myStepButton.setLayoutX(GRID_WIDTH - 1.75 * BUTTONS_HORIZONTAL_SPACING);
        myStepButton.setLayoutY(SCREEN_HEIGHT - BUTTONS_VERTICAL_SPACING);
        myStepButton.setOnAction(event -> step());

        Button myResetButton = new Button(myResources.getString("myResetButton"));
        myResetButton.setLayoutX(GRID_WIDTH - 2.5 * BUTTONS_HORIZONTAL_SPACING);
        myResetButton.setLayoutY(SCREEN_HEIGHT - BUTTONS_VERTICAL_SPACING);
        myResetButton.setOnAction(event -> resetGame());

        myRoot.getChildren().add(myUploadButton);
        myRoot.getChildren().add(myPlayButton);
        myRoot.getChildren().add(myStepButton);
        myRoot.getChildren().add(myResetButton);
    }

    private void addSpeedComponents(Stage stage){
        Button mySpeedUpButton = new Button(myResources.getString("mySpeedUpButton"));
        mySpeedUpButton.setLayoutX(GRID_WIDTH - FAST_BUTTON_OFFSET);
        mySpeedUpButton.setLayoutY(SCREEN_HEIGHT - BUTTONS_VERTICAL_SPACING);
        mySpeedUpButton.setOnAction(event -> increaseSpeed());

        Button mySlowDownButton = new Button(myResources.getString("mySlowDownButton"));
        mySlowDownButton.setLayoutX(GRID_WIDTH - SLOW_BUTTON_OFFSET);
        mySlowDownButton.setLayoutY(SCREEN_HEIGHT - BUTTONS_VERTICAL_SPACING);
        mySlowDownButton.setOnAction(event -> decreaseSpeed());

        Text speedTitle = new Text(GRID_WIDTH - SPEED_TITLE_OFFSET, SCREEN_HEIGHT - SPEED_TEXT_VERTICAL_SPACING,
                myResources.getString("speedTitle"));

        myRoot.getChildren().add(mySpeedUpButton);
        myRoot.getChildren().add(mySlowDownButton);
        myRoot.getChildren().add(speedTitle);
    }

    public void customDisplayGame(ArrayList<String> initialParams, Map<String, Double> thresholds, Map<String, Integer> states){
        myInitialParams = initialParams;
        myThresholds = thresholds;
        myStates = states;
        displayGame();
    }

    private Map<States, Color> getSimulationColors() {
        Map<States,Color> lineColors = getDefaultColors();
        for (States cell: userColors.keySet()
             ) {
            if (!userColors.get(cell).equals(lineColors.get(cell))){
                lineColors.put(cell, userColors.get(cell));
            }
        }
        return lineColors;
    }

    private Map<States,Color> getDefaultColors(){
        Map<States,Color> lineColors = new HashMap<>();
        if (simulationType.equals(SimulationType.FIRE)){
            lineColors.put(States.EMPTY, Color.YELLOW);
            lineColors.put(States.TREE, Color.GREEN);
            lineColors.put(States.BURNING, Color.RED);
        } else if (simulationType.equals(SimulationType.SEGREGATION)){
            lineColors.put(States.X, Color.ORANGE);
            lineColors.put(States.O, Color.DARKCYAN);
            lineColors.put(States.EMPTY, Color.WHITE);
        } else if (simulationType.equals(SimulationType.WATOR)){
            lineColors.put(States.SHARK, Color.PURPLE);
            lineColors.put(States.FISH, Color.PINK);
            lineColors.put(States.WATER, Color.BLUE);
        } else if (simulationType.equals(SimulationType.GAME_OF_LIFE)){
            lineColors.put(States.ALIVE, Color.BLUE);
            lineColors.put(States.DEAD, Color.WHITE);
        } else if (simulationType.equals(SimulationType.RPS)){
            lineColors.put(States.ROCK, Color.RED);
            lineColors.put(States.SCISSOR, Color.BLUE);
            lineColors.put(States.PAPER, Color.GREEN);
        } else if (simulationType.equals(SimulationType.SUGARSCAPE)){
            lineColors.put(States.AGENT, Color.DARKBLUE);
            lineColors.put(States.PATCH, Color.rgb(255, 202, 0));
        }else if (simulationType.equals(SimulationType.FORAGING)){
            lineColors.put(States.NEST, Color.PURPLE);
            lineColors.put(States.EMPTY, Color.BLACK);
            lineColors.put(States.FOOD, Color.BLUE);
        }
        return lineColors;
    }

    public Group getRoot(){
        return myRoot;
    }
}