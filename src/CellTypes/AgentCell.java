package CellTypes;

import Enumerations.GridShape;
import Enumerations.States;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * @author Natalie Le (nl121)
 */

public class AgentCell extends Cell {

    private final double defaultSugarMetabolism = 3;
    private final double defaultVision = 2;
    private final double defaultInitSugar = 2;

    private double sugarAmount;
    private final double SUGAR_METABOLISM;
    private final double VISION;
    private boolean isAlive;
    private double sideLength;

    public AgentCell(States state, Map<String, Double> rules, double x, double y, double height, double width,
                     Map<States, Color> colorMap, GridShape shape){
        super(state, x, y, height, width, colorMap, shape);
        possibleStates = Arrays.asList(States.AGENT, States.PATCH);
        defaultSettings(rules,"sugarMetabolism", defaultSugarMetabolism);
        SUGAR_METABOLISM = rules.get("sugarMetabolism");
        defaultSettings(rules,"vision", defaultVision);
        VISION = rules.get("vision");
        defaultSettings(rules,"initSugar", defaultInitSugar);
        sugarAmount = rules.get("initSugar");
        createCellShape(height / 2, width / 2);
        setInitialProps();
        isAlive = true;
    }

    public boolean getIsAlive(){
        return isAlive;
    }

    @Override
    public void defaultSettings(Map<String, Double> rules, String rule, double defaultSetting){
        if(!rules.containsKey(rule) || Math.abs(rules.get(rule))>1 || rules.get(rule)==0) {
            rules.put(rule, defaultSetting);
        } else if (rules.get(rule)<0) {
            rules.put(rule,Math.abs(rules.get(rule)));
        }
    }

    @Override
    public void update(Set<Cell> neighbors, Set<Cell> emptyCells){
        this.setNextState(States.PATCH);
        PatchCell highestSugarCell = null;
        for (Cell cell : neighbors){
            if (cell.getCurrentState().equals(States.PATCH)) {
                PatchCell current = (PatchCell) cell;
                if (highestSugarCell == null || current.getSugarAmount() > highestSugarCell.getSugarAmount()) {
                    highestSugarCell = current;
                }
            }
        }
        if (highestSugarCell == null) return;;
        this.setNewPosition(highestSugarCell.getRowNum(), highestSugarCell.getColNum());
        /*double firstX = highestSugarCell.getCell().getPoints().get(0);
        double firstY = highestSugarCell.getCell().getPoints().get(1);
        List<Double> points = new ArrayList<>();
        points.add(firstX);
        points.add(firstY);
        points.add(firstX);
        points.add(firstY + sideLength);
        points.add(firstX + sideLength);
        points.add(firstY);
        points.add(firstX + sideLength);
        points.add(firstY + sideLength);
        myCell.getPoints().setAll(points);*/
        myCell.getPoints().setAll(highestSugarCell.getCell().getPoints());
        sugarAmount += highestSugarCell.getSugarAmount();
        highestSugarCell.setSugarAmount(0);
        sugarAmount -= SUGAR_METABOLISM;
        if (sugarAmount <= 0){
            isAlive = false;
        }
    }

    private void swap(Cell sugarCell){
        double currentRow = getRowNum();
        double currentColumn = getColNum();
        this.setNewPosition(sugarCell.getRowNum(), sugarCell.getColNum());
        sugarCell.setNewPosition(currentRow, currentColumn);
        var myCellPoints = FXCollections.observableArrayList(myCell.getPoints());
        var emptyPoints = sugarCell.getCell().getPoints();

        myCell.getPoints().setAll(emptyPoints);
        sugarCell.getCell().getPoints().setAll(myCellPoints);
        sugarCell.setHasMoved(true);
        this.setHasMoved(true);
    }
}
