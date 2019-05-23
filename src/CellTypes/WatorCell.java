package CellTypes;


import Enumerations.States;
import javafx.collections.FXCollections;
import javafx.scene.paint.Color;
import Enumerations.GridShape;

//hello

import java.util.*;

/**
 * @author  Joyce Zhou (jyz11)
 */

public class WatorCell extends Cell {
    private final double FISHREPRODUCE;
    private final double SHARKREPRODUCE;
    private final double ENERGYFROMEAT;
    private double lifePoints;

    private double timeAlive = 0;
    private int life;



    public WatorCell(States state, Map<String, Double> rules, double x, double y, double height, double width, Map<States, Color> colorMap, GridShape gridShape) {
        super(state, x, y, height, width, colorMap, gridShape);

        possibleStates = Arrays.asList(States.SHARK, States.FISH, States.WATER);;
        currentState = getRightState(state, possibleStates);


        Map <String, Double> defaultRuleMap = defaultMap();
        for (String rule:defaultRuleMap.keySet()) {
            defaultSettings(rules,rule,defaultRuleMap.get(rule));
        }
        createCellShape(height, width);
        setInitialProps();

        FISHREPRODUCE = rules.get("fish reproduce");
        SHARKREPRODUCE = rules.get("shark reproduce");
        ENERGYFROMEAT = rules.get("energy eat");
        lifePoints = rules.get("life points");
        life = (int)lifePoints;

    }

    public Map <String, Double> defaultMap () {
        double defaultFishReproduce = 4;
        double defaultSharkReproduce = 20;
        double defaultEnergyFromEat = 0.5;
        double defaultLifePoints = 5;
        Map<String,Double> map = new HashMap<>();
        map.put("fish reproduce",defaultFishReproduce);
        map.put("shark reproduce",defaultSharkReproduce);
        map.put("energy eat",defaultEnergyFromEat);
        map.put("life points",defaultLifePoints);

        return map;
    }

    @Override
    public void defaultSettings(Map<String,Double> rules, String rule, double defaultSettings) {
        if(!rules.containsKey(rule) || rules.get(rule)==0) {
            rules.put(rule, defaultSettings);
        } else if (rules.get(rule)<0){
            rules.put(rule, Math.abs(rules.get(rule)));
        }
    }

    public void swap (Cell neighbor) {
        double currentRow = getRowNum();
        double currentColumn = getColNum();
        neighbor.setHasMoved(true);
        setHasMoved(true);

        var myCellPoints = FXCollections.observableArrayList(myCell.getPoints());
        var emptyPoints = neighbor.getCell().getPoints();
        myCell.getPoints().setAll(emptyPoints);
        neighbor.getCell().getPoints().setAll(myCellPoints);


        this.setNewPosition(neighbor.getRowNum(), neighbor.getColNum());
        neighbor.setNewPosition(currentRow, currentColumn);
    }

    public void die(Cell cell) {
        WatorCell waterCell = (WatorCell) cell;
        waterCell.setCurrentState(States.WATER);
        waterCell.setColor(colorMap.get(States.WATER));
        waterCell.life = 0;
        waterCell.timeAlive = 0;
    }

    public void swapWithWater(List<Cell> random){
        for (Cell neighbor : random) {
            if (neighbor.getCurrentState().equals(States.WATER) && !neighbor.getHasMoved()) {
                swap(neighbor);
            }
        }
    }

    public void setLifePoints(){
        life = (int)lifePoints;
    }

    public void reproduce(Cell neighbor, States state, Color color, double reproduce) {
        if (timeAlive >= reproduce) {
            WatorCell watorNeighbor = (WatorCell) neighbor;
            watorNeighbor.setCurrentState(state);
            watorNeighbor.setColor(color);

            watorNeighbor.setLifePoints();
            watorNeighbor.timeAlive = 0;
            this.timeAlive = 0;
        }
    }

    public List<Cell> radomizeNeighbors (Set<Cell> neighbors) {
        List<Cell> random = new ArrayList<>();
        for (Cell cell: neighbors) {
            random.add(cell);
        }
        Collections.shuffle(random);
        return random;
    }

    public void updateFish(Set<Cell> neighbors) {
        List<Cell> random = radomizeNeighbors(neighbors);

        for (Cell neighbor : random) {
            if (neighbor.getCurrentState().equals(States.WATER) && !neighbor.getHasMoved() && !getHasMoved()) {
                swap(neighbor);
                reproduce(neighbor, States.FISH, colorMap.get(States.FISH), FISHREPRODUCE);
                break;
            }
        }
    }

    public void updateShark (Set<Cell> neighbors) {
        List<Cell> random = radomizeNeighbors(neighbors);
        //this case is if there are any sharks around the fish
        boolean foundFish = false;
        for (Cell neighbor : random) {
            if (neighbor.getCurrentState().equals(States.FISH)) {
                foundFish = true;
                life = life + (int) ENERGYFROMEAT;

                //fish neighbor dies :(
                die(neighbor);
                this.swap(neighbor);


                if (timeAlive >= SHARKREPRODUCE) {
                    this.reproduce(neighbor, States.SHARK, colorMap.get(States.SHARK), SHARKREPRODUCE);
                }
                this.setHasMoved(true);
                setHasMoved(true);
                break;
            }
        }
        if (!foundFish) { // if (!foundFish && life <= 0_
            life--;
                if (life <= 0) {
                    die(this);
                } else {
                    swapWithWater(random);
                }
            }
        }


    @Override
    public void update(Set<Cell> neighbors, Set<Cell> emptyCells) {
        timeAlive++;
        if (currentState.equals(States.SHARK)) {
            updateShark(neighbors);
        }
        else if (currentState.equals(States.FISH)) {
            updateFish(neighbors);
        }
    }
}