package CellTypes;

import Enumerations.States;
import javafx.scene.paint.Color;
import Enumerations.GridShape;

import java.util.*;

public class GroundCell extends Cell {

    private static double PHEROMONE_INCREASE;

    private static final List<States> POSSILBE_CELL_TYPES = Arrays.asList(States.FOOD,
            States.EMPTY,
            States.NEST,
            States.ANT_WITHOUT_FOOD, States.ANT_WITH_FOOD);
    private int numAnts = 0;


    private static double MAX_ANTS;
    private static double DECAY_RATE;

    public GroundCell(States state, Map<String, Double> rules, double x, double y, double height, double width, Map<States, Color> colors, GridShape shape){
        super(state, x, y, width,width, colors, shape);
        MAX_ANTS = rules.get("AntsPerNest");
        DECAY_RATE = rules.get("DecayRate");
        PHEROMONE_INCREASE = rules.get("PherIncrease");
        currentState = getRightState(state,POSSILBE_CELL_TYPES);
        foodPheromone = 0;
        homePheromone = 0;
        if(currentState == States.NEST) setHomePheromone(100);
        if(currentState == States.FOOD) setFoodPheromone(100);


        createCellShape(height, width);
        setInitialProps();
        setHasMoved(false);
    }
    public GroundCell(States state, double x, double y, double width){
        super(state, x, y, width, 100,100);
    }

    @Override
    public void defaultSettings(Map<String, Double> rules, String rule, double defaultSettings) {
        if(!rules.containsKey(rule) || Math.abs(rules.get(rule))>1 || rules.get(rule)==0) {
            rules.put(rule,defaultSettings);
        } else if (rules.get(rule)<0) {
            rules.put(rule,Math.abs(rules.get(rule)));
        }
    }

    public void update(Set<Cell> neighbors, Set<Cell> emptyCells){
        if(currentState == States.EMPTY){
            setPheromoneStroke();
        }
        if(getFoodPheromone() > 0){
            setFoodPheromone(getFoodPheromone()-DECAY_RATE);
        }
        if(getHomePheromone() > 0){
            setHomePheromone(getHomePheromone() - DECAY_RATE);
        }

        if(currentState == States.NEST && numAnts < MAX_ANTS){ //create a new ant
            newAnt(neighbors);
        }
        else if(currentState == States.ANT_WITHOUT_FOOD){
            Cell cellToMove = getCellToMove(neighbors,"FOOD");
            moveHungry(cellToMove);
        }else if (currentState == States.ANT_WITH_FOOD){
            Cell cellToMove = getCellToMove(neighbors,"HOME");
            moveHome(cellToMove);
        }

        else if((currentState == States.EMPTY || currentState == States.FOOD) && !getHasMoved()){
            setNextState(getCurrentState());
            setNextColor(getCurrentColor());
        }else if(currentState == States.NEST && numAnts > MAX_ANTS){
            setNextState(getCurrentState());
            setNextColor(getCurrentColor());
        }

    }

    public void moveHome(Cell cellToMove){
        if(cellToMove.getCurrentState() == States.NEST){
            setNextState(States.EMPTY);
            setNextColor(Color.BLACK);
            cellToMove.setNextState(States.NEST);
            cellToMove.setNextColor(Color.PURPLE); //We switch the ant and the nest. The ant becomes the nest and the nest an empty cell
            cellToMove.setHasMoved(true);
            numAnts--;
        }else if(cellToMove.getCurrentState() == States.EMPTY){
            setNextState(cellToMove.getCurrentState());
            setNextColor(cellToMove.getCurrentColor());
            cellToMove.setNextState(getCurrentState());
            cellToMove.setNextColor(getCurrentColor());
            upFoodPher(PHEROMONE_INCREASE);
            cellToMove.setHasMoved(true);
        }else{
            setNextState(getCurrentState());
            setNextColor(getCurrentColor());
            //cellToMove.setNextState(cellToMove.getCurrentState());
            //cellToMove.setNextColor(cellToMove.getCurrentColor());
            //cellToMove.setHasMoved(true);
        }

    }

    public void moveHungry(Cell cellToMove){
        if(cellToMove.getCurrentState() == States.FOOD){
            setNextState(States.ANT_WITH_FOOD);
            setNextColor(Color.YELLOW);
            cellToMove.setNextState(States.EMPTY);
            cellToMove.setNextColor(Color.BLACK);
            cellToMove.setHasMoved(true);
        }else if(cellToMove.getCurrentState() == States.EMPTY){
            setNextState(cellToMove.getCurrentState());
            setNextColor(cellToMove.getCurrentColor());
            cellToMove.setNextState(getCurrentState());
            cellToMove.setNextColor(getCurrentColor());
            upNestPher(PHEROMONE_INCREASE);
            cellToMove.setHasMoved(true);
        }else{
            setNextState(getCurrentState());
            setNextColor(getCurrentColor());
            //cellToMove.setNextState(cellToMove.getCurrentState());
            //cellToMove.setNextColor(cellToMove.getCurrentColor());
            //cellToMove.setHasMoved(true);
        }

    }

    public Cell getCellToMove(Set<Cell> neighbors, String type){
        ArrayList<Cell> neighborsArr = new ArrayList<>(neighbors);
        Collections.shuffle(neighborsArr);
        double maxPheromone = 0;
        Cell cellToMove = null;
        for(Cell neighbor:neighborsArr){
            if(maxPheromone < neighbor.getPheromone(type) && neighbor.getCurrentState() != States.ANT_WITHOUT_FOOD && neighbor.getCurrentState() != States.ANT_WITH_FOOD && !neighbor.getHasMoved()){
                maxPheromone = neighbor.getPheromone(type);
                cellToMove = neighbor;
            }

        }
        if(maxPheromone == 0){
            for(Cell neighbor:neighborsArr){
                if(neighbor.getCurrentState() == States.EMPTY && !neighbor.getHasMoved()){
                    cellToMove = neighbor;
                    break;
                }
            }

        }
        return cellToMove;
    }

    public void newAnt(Set<Cell> neighbors) {
        for (Cell neighbor : neighbors) {
            if (neighbor.getCurrentState() == States.EMPTY) {
                neighbor.setNextState(States.ANT_WITHOUT_FOOD);
                neighbor.setNextColor(Color.RED);
                setNextState(getCurrentState());
                setNextColor(getCurrentColor());
                neighbor.setHasMoved(true);
                numAnts++;
                break;
            }
        }

    }

    public void setPheromoneStroke(){
        if(getFoodPheromone() > 0  && getHomePheromone() <= 0){
            this.getCell().setStroke(Color.BLUE);
        }else if(getHomePheromone() >0 && getFoodPheromone() <= 0){
            this.getCell().setStroke(Color.PURPLE);
        } else if (getHomePheromone() >0 && getFoodPheromone() > 0){
            this.getCell().setStroke(Color.CYAN);
        }else{
            this.getCell().setStroke(Color.BLACK);
        }
    }

}
