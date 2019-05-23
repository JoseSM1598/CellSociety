package CellTypes;

import Enumerations.States;
import javafx.collections.FXCollections;
import javafx.scene.paint.Color;
import Enumerations.GridShape;

import java.util.*;

public class AntCell extends Cell{

    private static final List<States> POSSIBLE_CELL_TYPES = Arrays.asList(States.ANT_WITH_FOOD,
            States.ANT_WITHOUT_FOOD);
    private static final double INCREASE_FOOD = 1;



    public AntCell(States state, double x, double y, double height, double width, Map<States, Color> colors, GridShape shape) {
        super(state, x, y,height, width, colors, shape);
        currentState = getRightState(state, POSSIBLE_CELL_TYPES);
        createCellShape(height, width);
        setInitialProps();
    }



    @Override
    public void defaultSettings(Map<String, Double> rules, String rule, double defaultSettings) {
        if(!rules.containsKey(rule) || Math.abs(rules.get(rule))>1 || rules.get(rule)==0) {
            rules.put(rule,defaultSettings);
        } else if (rules.get(rule)<0) {
            rules.put(rule,Math.abs(rules.get(rule)));
        }
    }
    public void swap(Cell neighbor){
        var myCellPoints = FXCollections.observableArrayList(myCell.getPoints());
        var emptyPoints = neighbor.getCell().getPoints();

        myCell.getPoints().setAll(emptyPoints);
        neighbor.getCell().getPoints().setAll(myCellPoints);
    }

    @Override
    public void update(Set<Cell> neighbors, Set<Cell> emptyCells){
        if(getCurrentState().equals(States.ANT_WITHOUT_FOOD)){
            //Cell cellToMove = getCellToMove( neighbors, "FOOD"); //follow food pheromone
            //moveHungry(cellToMove);

        }else{
            //Cell cellToMove = getCellToMove(neighbors, "HOME"); //follow nest pheromone
            //moveHome(cellToMove);
        }

    }
    public void updateAnt(Set<Cell> neighbors, Set<Cell> emptyCells){
        return;

    }

    public void moveHome(Cell cellToMove){
        if(cellToMove.getCurrentState() == States.NEST){
            setNextState(States.NEST);
            setNextColor(Color.PURPLE);
            cellToMove.setNextState(States.EMPTY);
            cellToMove.setNextColor(Color.BLACK); //We switch the ant and the nest. The ant becomes the nest and the nest an empty cell
        }else if(cellToMove.getCurrentState() == States.EMPTY){
            setNextState(cellToMove.getCurrentState());
            setNextColor(cellToMove.getCurrentColor());
            cellToMove.setNextState(getCurrentState());
            cellToMove.setNextColor(getCurrentColor());
            cellToMove.upFoodPher(INCREASE_FOOD);
        }else{
            setNextState(getCurrentState());
            setNextColor(getCurrentColor());
            cellToMove.setNextState(cellToMove.getCurrentState());
            cellToMove.setNextColor(cellToMove.getCurrentColor());
        }
        swap(cellToMove);

    }

    public void moveHungry(Cell cellToMove){
        if(cellToMove.getCurrentState() == States.FOOD){
            setNextState(States.ANT_WITH_FOOD);
            setNextColor(Color.YELLOW);
            cellToMove.setNextState(States.EMPTY);
            cellToMove.setNextColor(Color.BLACK);
            //swap(cellToMove);
        }else if(cellToMove.getCurrentState() == States.EMPTY){
            setNextState(cellToMove.getCurrentState());
            setNextColor(cellToMove.getCurrentColor());
            cellToMove.setNextState(getCurrentState());
            cellToMove.setNextColor(getCurrentColor());
            cellToMove.upNestPher(INCREASE_FOOD);
            //swap(cellToMove);
        }else{
            setNextState(getCurrentState());
            setNextColor(getCurrentColor());
            cellToMove.setNextState(cellToMove.getCurrentState());
            cellToMove.setNextColor(cellToMove.getCurrentColor());
         }

    }
    //public Cell getCellToMove(Set<Cell> neighbors, String type){
    //    ArrayList<Cell> neighborsArr = new ArrayList<>(neighbors);
    //    Collections.shuffle(neighborsArr);
    //    double maxPheromone = 0;
    //    Cell cellToMove = null;
    //    for(Cell neighbor:neighborsArr){
    //        if(maxPheromone < neighbor.getPheromone(type)){
    //            maxPheromone = neighbor.getPheromone(type);
    //            cellToMove = neighbor;
    //        }
//
    //    }
    //    if(maxPheromone == 0){cellToMove = neighborsArr.get(0);}
    //    return cellToMove;
    //}
}
