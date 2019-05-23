package sample;

import Enumerations.SimulationType;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.shape.Line;
import utility.XMLException;

import java.util.*;

/**
 * @author Natalie Le (nl121)
 */

public class Parameter {
    private final int VERTICAL_SPACING;
    private final int HORIZONTAL_SPACING;
    private final int START_WIDTH;
    private Group myRoot;
    private GUI myGUI;
    private HashSet<Stat> myStats;
    private Button mySubmitButton;
    private int totalCells;
    private ResourceBundle myErrors;
    private String fileName = "resources/Errors";


    public Parameter(GUI myGUI, Map<String, Integer> states, Map<String, Double> rules, int xCoord,
                     int horizontalSpacing, int verticalSpacing, int totalCells, int width, int height){
        myErrors = ResourceBundle.getBundle(fileName);
        this.myGUI = myGUI;
        this.totalCells = totalCells;
        myRoot = new Group();
        HORIZONTAL_SPACING = horizontalSpacing;
        VERTICAL_SPACING = verticalSpacing;
        START_WIDTH = xCoord;
        myStats = new HashSet<>();
        mySubmitButton = new Button("Submit");
        mySubmitButton.setOnAction(event -> update());
        Line panelDivider = new Line(width, 0, width, height);
        myRoot.getChildren().add(panelDivider);
        makeStats(states, rules);
        for (Stat s : myStats) {
            addToRoot(s);
        }
    }

    private boolean isAddingUp(){ // need to rename
        int total = 0;
        for (Stat stat : myStats){
            if (!stat.getIsRule()) total += stat.getValue();
        }
        return total == totalCells;
    }

    public void update() { // design decision: assumes this game restarts
        if (!isAddingUp()) {
            new Alert(Alert.AlertType.ERROR, myErrors.getString("InvalidNumberOfCells")).showAndWait();
            throw new XMLException(myErrors.getString("InvalidNumberofCells"));
        }
        Map<String, Double> thresholds = new HashMap<>();
        Map<String, Integer> states = new HashMap<>();
        ArrayList<String> initialParams = new ArrayList<>();
        for (Stat stat : myStats){
            if (stat.getIsRule()){
                thresholds.put(stat.getTitle().getText(), stat.getValue());
            } else {
                int frequency = (int) (stat.getValue());
                states.put(stat.getTitle().getText(), frequency);
                for (int i = 0; i < frequency; i++){
                    initialParams.add(stat.getTitle().getText());
                }
            }
        }
        Collections.shuffle(initialParams);
        myGUI.customDisplayGame(initialParams, thresholds, states);
    }

    public Group getRoot() { return myRoot; }

    private void makeStats(Map<String, Integer> states, Map<String, Double> rules){
        int horizontalCount = 1;
        for (Map.Entry<String, Integer> state : states.entrySet()) {
            Stat stat = new Stat(state.getKey(), (double) state.getValue(), START_WIDTH + HORIZONTAL_SPACING,
                    (VERTICAL_SPACING * horizontalCount), false);
            myStats.add(stat);
            horizontalCount++;
        }
        for (Map.Entry<String, Double> rule : rules.entrySet()){
            Stat stat = new Stat(rule.getKey(), (double) rule.getValue(), START_WIDTH + HORIZONTAL_SPACING,
                    (horizontalCount * VERTICAL_SPACING), true);
            myStats.add(stat);
            horizontalCount++;
        }
        placeSubmitButton(horizontalCount * VERTICAL_SPACING);
    }

    private void placeSubmitButton(int yCoord){
        mySubmitButton.setLayoutX(START_WIDTH + HORIZONTAL_SPACING);
        mySubmitButton.setLayoutY(yCoord);
        myRoot.getChildren().add(mySubmitButton);
    }

    private void addToRoot(Stat stat){
        myRoot.getChildren().add(stat.getTitle());
        myRoot.getChildren().add(stat.getIncreaseButton());
        myRoot.getChildren().add(stat.getDescription());
        myRoot.getChildren().add(stat.getDecreaseButton());
    }
}