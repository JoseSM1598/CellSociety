package CellTypes;

import Enumerations.States;
import Shapes.Hexagon;
import Shapes.Square;
import Shapes.Triangle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import Enumerations.GridShape;

import java.util.*;

/**
 * @author Jose San Martin (js665), Joyce Zhou (jyz11), Natalie Le (nl121)
 */
public abstract class Cell  {
    protected States currentState;
    protected Color myColor;
    protected States nextState;
    protected Color nextColor;
    protected double myXCoord;
    protected double myYCoord;
    public Polygon myCell;
    private boolean hasMoved;
    protected double cellWidth;
    protected GridShape gridShape;
    protected double foodPheromone;
    protected double homePheromone;

    protected Map<States, Color> colorMap;
    protected List<States> possibleStates;

    public Cell (States cellState, double x, double y, double height, double width, Map<States, Color> colorMap, GridShape shape) {
        cellWidth = width;
        myXCoord = x;
        myYCoord = y;
        currentState = cellState;
        hasMoved = false;
        gridShape = shape;
        this.colorMap = colorMap;

    }
    public Cell(States cellState, double x, double y,  double width, double fP, double hP){
        cellWidth = width;
        myXCoord = x;
        myYCoord = y;
        currentState = cellState;
        hasMoved = false;
        foodPheromone = fP;
        homePheromone = hP;
    }

    /**
     * Method that returns a default state if input is wrong
     */
    public States getRightState(States currentState, List<States> cellTypes){
        boolean rightState = false;
        for (States state:cellTypes
        ) {
            if(currentState.equals(state)){
                rightState = true;
            }
        }
        if(!rightState) {
            Collections.shuffle(cellTypes);
            return cellTypes.get(0);
        } else {
            return currentState;
        }
    }

    public void setInitialProps(){
        if (colorMap.keySet().contains(currentState)){
            myCell.setFill(colorMap.get(currentState));
            myColor = colorMap.get(currentState);
        }
        myCell.setStroke(Color.BLACK);
    }

    /**
     * Returns the current rowNum of the cell
     */

    public double getRowNum() {
        return myXCoord;
    }

    /**
     * Returns the current colNum of the cell
     */

    public double getColNum() {
        return myYCoord;
    }

    /**
     * Sets a new position for the cell (used with simulations that swap cells)
     */

    public void setNewPosition(double row, double col){
        this.myXCoord= row;
        this.myYCoord = col;
    }

    /**
     * Sets the next state of the cell
     */

    public void setNextState(States next) {
        this.nextState = next;
    }

    /**
     * Returns the next state of the cell
     */

    public States getNextState() {
        return nextState;
    }

    /**
     * Returns the current state of the cell
     */

    public States getCurrentState(){
        return currentState;
    }

    /**
     * Sets the current state of the cell
     */

    public void setCurrentState(States now){
        this.currentState = now;
    }

    /**
     * Returns the cell
     */

    public Polygon getCell(){
        return myCell;
    }

    public void setCell(Polygon newCell){
        myCell = newCell;
    }

    /**
     * Returns the property hasMoved of each cell
     */

    public boolean getHasMoved(){
        return hasMoved;
    }

    /**
     * Sets the property hasMoved of each cell
     */


    public void setHasMoved(boolean hasMoved){
        this.hasMoved = hasMoved;
    }

    /**
     * Sets the next color of the cell
     */

    public void setNextColor(Color color){
        nextColor = color;
    }

    /**
     * gets the next color of the cell
     */

    public Color getNextColor(){
        return nextColor;
    }

    /**
     * Sets the color of the cell
     */

    public void setColor(Color color){
        myCell.setFill(color);
        myColor = color;
    }

    /**
     * Gets the current color of the cell
     */
    public Color getCurrentColor(){
        return myColor;
    }


    /**
     * Foraging Ant Methods
     */
    public double getPheromone(String type){
        if(type == "FOOD"){
            return this.foodPheromone;
        }else{
            return this.homePheromone;
        }
    }
    public void setFoodPheromone(double value){
        this.foodPheromone = value;
    }
    public void setHomePheromone(double value){
        this.homePheromone = value;
    }
    public double getFoodPheromone(){
        return this.foodPheromone;
    }
    public double getHomePheromone(){ return this.homePheromone; }
    public void upFoodPher(double increaseAmount){
        this.foodPheromone = this.foodPheromone + increaseAmount;
    }
    public void upNestPher(double increaseAmount){
        this.homePheromone = this.homePheromone + increaseAmount;
    }

    /**
     * Returns default values of the game if something goes wrong
     */
    protected abstract void defaultSettings(Map<String, Double> rules, String rule, double defaulsetting);

    /**
     * Abstract method that creates the cell shape. Allows flexibility in terms of the shape required of each cell
     */

    public void createCellShape(double height, double width){

        if(gridShape.equals(GridShape.HEXAGON)){
            myCell = new Polygon(new Hexagon(width, this.getRowNum(), this.getColNum()).getPoints());
        } else if (gridShape.equals(GridShape.TRIANGLE)) {
            myCell = new Polygon(new Triangle(width, this.getRowNum(), this.getColNum()).getPoints());
        } else {
            myCell = new Polygon(new Square(width, this.getRowNum(), this.getColNum()).getPoints());
        }
        addCellListener();
    }

    protected void addCellListener(){
        myCell.setOnMouseClicked(event -> getNextState(possibleStates));
    }

    /**
     * Abstract method that allows each cell to update depending on the rules of the simulation being run
     */

    public abstract void update(Set<Cell> neighbors, Set<Cell> emptyCells);

    private void getNextState(List<States> states){
        int i = states.indexOf(currentState);
        if (i < states.size() - 1){
            currentState = states.get(i + 1);
        } else {
            currentState = states.get(0);
        }
        setInitialProps();
    }

}

