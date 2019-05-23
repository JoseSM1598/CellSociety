package CellTypes;

import Enumerations.States;
import javafx.collections.FXCollections;
import javafx.scene.paint.Color;
import Enumerations.GridShape;


import java.util.*;

/**
 * @author Natalie Le (nl121)
 */

public class SegregationCell extends Cell{
    private final double SATISFIED;

    private final double defaultSatisfied = 0.5;


    public SegregationCell(States state, Map<String, Double> rules, double x, double y, double height, double width,
                           Map<States, Color> colorMap, GridShape shape) {
        super(state, x, y, height, width, colorMap, shape);
        possibleStates = Arrays.asList(States.X, States.O, States.EMPTY);
        currentState = getRightState(state,possibleStates);
        defaultSettings(rules,"satisfied",defaultSatisfied);

        SATISFIED = rules.get("satisfied");
        createCellShape(height, width);
        setInitialProps();
    }

    @Override
    public void defaultSettings(Map<String, Double>rules, String rule, double defaultSettings) {
        if(!rules.containsKey(rule) || Math.abs(rules.get(rule))>1 || rules.get(rule)==0) {
            rules.put(rule,defaultSettings);
        } else if (rules.get(rule)<0) {
            rules.put(rule,Math.abs(rules.get(rule)));
        }
    }

    @Override
    public void update(Set<Cell> neighbors, Set<Cell> emptyCells) {
        if (this.getCurrentState().equals(States.EMPTY)) {return;}
        int surroundingAlike = 0;
        int totalNeighbors = 0;

        for (Cell cell: neighbors) {
            if (cell.getCurrentState().equals(this.getCurrentState())) {
                surroundingAlike++;
            }
            if (!cell.getCurrentState().equals(States.EMPTY)){
                totalNeighbors++;
            }
        }
        if (totalNeighbors == 0) {return;} // surrounded by empty cells
        if ((double) surroundingAlike / (double) totalNeighbors >= SATISFIED) {return;}
        Cell emptyCell = null;
        ArrayList<Cell> emptyCellList = new ArrayList<>(emptyCells);
        Collections.shuffle(emptyCellList);
        for (Cell cell : emptyCellList){
            if (!cell.getHasMoved()) {
                emptyCell = cell;
                break;
            }
        }
        if (emptyCell != null){
            swap(emptyCell);
        }
    }

    private void swap(Cell emptyCell){
        double currentRow = getRowNum();
        double currentColumn = getColNum();
        this.setNewPosition(emptyCell.getRowNum(), emptyCell.getColNum());
        emptyCell.setNewPosition(currentRow, currentColumn);
        var myCellPoints = FXCollections.observableArrayList(myCell.getPoints());
        var emptyPoints = emptyCell.getCell().getPoints();

        myCell.getPoints().setAll(emptyPoints);
        emptyCell.getCell().getPoints().setAll(myCellPoints);
        emptyCell.setHasMoved(true);
        this.setHasMoved(true);
    }

}

