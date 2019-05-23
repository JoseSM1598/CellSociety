package sample;


import CellTypes.*;
import Enumerations.GridShape;
import Enumerations.SimulationType;
import Enumerations.States;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import utility.XMLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.*;

/**
 * A UI class that creates the CellTypes.CellTypes[][] array and renders the array as a grid on our main GUI
 * @author José San Martin (js665), Natalie Le (nl121)
 *
 * Resource used: https://stackoverflow.com/questions/27870674/display-2d-array-as-grid-in-javafx
 */
public class Grid {
    private static final String fileName = "resources/Errors";
    private Map<States, Integer> existingCells;
    private SimulationType simulationType;
    private ResourceBundle myResources;
    protected CellBucket[][] gridCells;
    protected double GRID_LENGTH;
    protected double cellHeight;
    private GridShape gridShape;
    protected double cellWidth;
    private Group root;

    protected Map<Cell, HashSet<Cell>> allNeighbors = new HashMap<>();
    private Set<Cell> emptyCells = new HashSet<>();
    private HashSet<Cell> allCells = new HashSet<>();

    public Grid(List<String> parameters, Map<String, Double> rules, SimulationType simulationType, GridShape shape, int cellsPerColumn
            , int cellsPerRow, double gridLength, Map<States, Color> colors){
        root = new Group();
        GRID_LENGTH = gridLength;
        gridShape = shape;
        this.simulationType = simulationType;
        existingCells = new HashMap<>();
        for (States state : colors.keySet()){
            existingCells.put(state, 0);
        }
        //Obtain the height and width of each CELL (they have to all be the same)
        cellHeight = GRID_LENGTH / cellsPerColumn;
        cellWidth = GRID_LENGTH / cellsPerRow;
        gridCells = new CellBucket[cellsPerColumn][cellsPerRow]; //Change to reflect the cellType being asked of
        myResources = ResourceBundle.getBundle(fileName);

        if(cellsPerColumn*cellsPerRow != parameters.size() && !simulationType.equals(SimulationType.SUGARSCAPE)) {
            throw new XMLException(myResources.getString("InvalidNumberOfCells"));
        } else {
            int count = 0;
            if (!simulationType.equals(SimulationType.SUGARSCAPE)) {
                for (int i = 0; i < cellsPerColumn; i++) {
                    for (int j = 0; j < cellsPerRow; j++) {
                        if (count == parameters.size()) break;
                        Cell cell;
                        if (simulationType.equals(SimulationType.GAME_OF_LIFE)) {
                            cell = new GameOfLifeCell(States.valueOf(parameters.get(count)), rules, i * cellHeight, j * cellWidth, cellHeight, cellWidth, colors, gridShape);
                        } else if (simulationType.equals(SimulationType.SEGREGATION)) {
                            cell = new SegregationCell(States.valueOf(parameters.get(count)), rules, i * cellHeight, j * cellWidth, cellHeight, cellWidth, colors, gridShape);
                        } else if (simulationType.equals(SimulationType.FIRE)) {
                            cell = new FireCell(States.valueOf(parameters.get(count)), rules, i * cellHeight, j * cellWidth, cellHeight, cellWidth, colors, gridShape);
                        } else if (simulationType.equals(SimulationType.RPS)) {
                            cell = new RPSCell(States.valueOf(parameters.get(count)), rules, i * cellHeight, j * cellWidth, cellHeight, cellWidth, colors, gridShape);
                        } else if(simulationType.equals(SimulationType.FORAGING)){
                            cell = new GroundCell(States.valueOf(parameters.get(count)), rules, i * cellHeight, j * cellWidth, cellHeight, cellWidth,colors, gridShape);
                            existingCells.put(States.ANT_WITH_FOOD,0);
                            existingCells.put(States.ANT_WITHOUT_FOOD,0);
                        } else {
                            cell = new WatorCell(States.valueOf(parameters.get(count)), rules, i * cellHeight, j * cellWidth, cellHeight, cellWidth, colors, gridShape);
                        }
                        count++;
                        gridCells[i][j] = getCellBucket(gridCells[i][j], cell);
                        addToGrid(cell);
                    }
                }
            } else {
                for (int i = 0; i < cellsPerColumn; i++) {
                    for (int j = 0; j < cellsPerRow; j++) {
                        Cell cell = new PatchCell(States.PATCH, rules, i * cellHeight, j * cellWidth, cellHeight, cellWidth, colors, gridShape);
                        gridCells[i][j] = getCellBucket(gridCells[i][j], cell);
                        addToGrid(cell);
                    }
                }
                while (parameters.size() != cellsPerColumn * cellsPerRow){
                    int randomCell = (int) (Math.random() * ((cellsPerColumn * cellsPerRow) + 1));
                    if (parameters.get(randomCell).equals(States.PATCH.toString())){
                        parameters.remove(randomCell);
                    }
                }
                int c = 0;
                for (int i = 0; i < cellsPerColumn; i++) {
                    for (int j = 0; j < cellsPerRow; j++) {
                        if (!States.valueOf(parameters.get(c)).equals(States.AGENT)){
                            Cell cell = new AgentCell(States.AGENT, rules, i * cellHeight, j * cellWidth, cellHeight, cellWidth, colors, gridShape);
                            addToGrid(cell);
                        }
                        c++;
                    }
                }
            }
        }
    }

    private CellBucket getCellBucket(CellBucket cellBucketInGrid, Cell cell){
        CellBucket cellBucket;
        if (cellBucketInGrid == null) {
            cellBucket = new CellBucket(cell);
        } else {
            cellBucket = cellBucketInGrid;
            cellBucket.addCell(cell);
        }
        return cellBucket;
    }

    private void addToGrid(Cell cell){
        allCells.add(cell);
        if (existingCells.containsKey(cell.getCurrentState())){
            existingCells.put(cell.getCurrentState(), existingCells.get(cell.getCurrentState()) + 1);
        } else {
            existingCells.put(cell.getCurrentState(), 1);
        }
        addToGroup(root, cell);
    }

    /**
     * Updates the position of each cell in the gridCells array (used for simulations that require swapping)
     */

    public void updateGridCells(){
        for (Map.Entry<States, Integer> entry : existingCells.entrySet()){
            entry.setValue(0);
        }
        for (Cell cell : allCells){
            CellBucket cellBucket = gridCells[(int) (cell.getRowNum() / cellHeight)][(int) (cell.getColNum() / cellWidth)];
            cellBucket.emptyHeldCells();
            cellBucket.addCell(cell);
            if (cell.getCurrentState().equals(States.AGENT)){
                AgentCell agentCell = (AgentCell) cell;
                if (agentCell.getIsAlive()){
                    existingCells.put(cell.getCurrentState(), existingCells.get(cell.getCurrentState()) + 1);
                }
            } else {
                existingCells.put(cell.getCurrentState(), existingCells.get(cell.getCurrentState()) + 1);
            }
        }
    }

    /**
     * This is to create a HashMap that maps a cell to all of its neighbors. This map will only be initialized
     * once; if a cell were to move to a different location, a method "swap" can be called to switch the cells'
     * neighbors.
     */
    public void getAllNeighbors() {
        allNeighbors.clear();
        for( int i=0; i < GRID_LENGTH/cellHeight; i++) {
            for (int j = 0; j < GRID_LENGTH/cellWidth; j++) {
                int row = i; //these are the coordinates of the current cell
                int col = j;
                HashSet<Cell> neighbors = new HashSet<>();
                if(gridShape.equals(GridShape.TRIANGLE)){
                    neighbors = getTriangleNeighbors(row, col);
                }else if(gridShape.equals(GridShape.RECTANGLE)){
                    neighbors = getSquareNeighbors(row, col);
                }else if(gridShape.equals(GridShape.HEXAGON)){
                    neighbors = getHexagonNeighbors(row, col);
                }
                for (Cell cell : gridCells[i][j].getHeldCells()) {
                    allNeighbors.put(cell, neighbors);
                }
            }

        }
    }

    public HashSet<Cell> getSquareNeighbors(int row, int col){
        HashSet<Cell> neighbors = new HashSet<>();
        for(int k =-1; k<=1; k++) {
            for(int l = -1; l <= 1; l++){
                int a = k + row;
                int b = l + col;
                if (k==0 && l==0) { }
                else if (a <= GRID_LENGTH/cellHeight - 1 && a >= 0 && b>=0 && b<=GRID_LENGTH/cellWidth - 1) {
                    if (simulationType.equals(SimulationType.SUGARSCAPE) && gridCells[k + row][l + col].getHeldCells().size() > 1){ }
                    else {
                        for (Cell cell : gridCells[k + row][l + col].getHeldCells()){
                            neighbors.add(cell);
                        }
                    }
                }
            }
        }
        return neighbors;

    }


    public HashSet<Cell> getTriangleNeighbors(int row, int col){
        HashSet<Cell> neighbors = new HashSet<>();
        for(int k =-2; k<=2; k++) {
            for(int l = -1; l <= 1; l++){
                int a = k + row;
                int b = l + col;
                if (k==0 && l==0) {continue;}
                if((row%2==0 && col %2 == 0) || (row%2 != 0 && col%2 != 0)){//if even row && even col or odd row and odd col
                    if((k==-2 & l ==-1) | (k == 2 & l ==-1)){continue;}
                    if(a <= GRID_LENGTH/cellHeight - 1 && a >= 0 && b>=0 && b<=GRID_LENGTH/cellWidth -1){
                        for (Cell cell : gridCells[k + row][l + col].getHeldCells()){
                            neighbors.add(cell);
                        }
                    }
                }
                else{ //if odd row
                    if((k==-2 & l ==1) | (k == 2 & l ==1)){continue;}
                    if(a <= GRID_LENGTH/cellHeight - 1 && a >= 0 && b>=0 && b<=GRID_LENGTH/cellWidth -1){
                        for (Cell cell : gridCells[k + row][l + col].getHeldCells()){
                            neighbors.add(cell);
                        }
                    }
                }
            }
        }
        return neighbors;
    }

    public HashSet<Cell> getHexagonNeighbors(int row, int col){
        HashSet<Cell> neighbors = new HashSet<>();
        for(int k =-1; k<=1; k++) {
            for(int l = -1; l <= 1; l++){
                int a = k + row;
                int b = l + col;
                if (k==0 && l==0) {continue;}
                if(row%2==0){//if even row
                    if((k==-1 & l ==1) | (k == 1 & l ==1)){continue;}
                    else if(a <= GRID_LENGTH/cellHeight - 1 && a >= 0 && b>=0 && b<=GRID_LENGTH/cellWidth -1){
                        for (Cell cell : gridCells[k + row][l + col].getHeldCells()){
                            neighbors.add(cell);
                        }
                    }
                }
                else{ //if odd row
                    if((k==-1 & l ==-1) | (k == 1 & l ==-1)){continue;}
                    else if(a <= GRID_LENGTH/cellHeight - 1 && a >= 0 && b>=0 && b<=GRID_LENGTH/cellWidth -1){
                        for (Cell cell : gridCells[k + row][l + col].getHeldCells()){
                            neighbors.add(cell);

                        }
                    }
                }
            }
        }
        return neighbors;
    }



    /**
     * Returns a hashset of all of the cells that are currently marked as empty
     */

    public Set<Cell> getAllEmptyCells(){
        emptyCells.clear();
        for( int i=0; i < GRID_LENGTH/cellHeight; i++) {
            for (int j = 0; j < GRID_LENGTH/cellWidth; j++) {
                for (Cell cell : gridCells[i][j].getHeldCells()){
                    if (cell.getCurrentState() == States.EMPTY) {
                        emptyCells.add(cell);
                    }
                }
            }
        }
        return Collections.unmodifiableSet(emptyCells);
    }


    /**
     * Returns the map that contains all of the neighbors
     */
    public Map<Cell, HashSet<Cell>> getMap(){
        return allNeighbors;
    }

    /**
     * Adds the cell to the root, so that it can be displayed in a scene
     */
    public void addToGroup(Group root, Cell cell){
        root.getChildren().add(cell.getCell());
    }

    /**
     * Returns the root which contains all of the cells (rectangles)
     */

    public Group getGridRoot(){
        return root;
    }

    /**
     * Resets the property hasMoved of each cell
     */


    public void resetHasMoved(){
       for (Cell cell : allCells) { cell.setHasMoved(false); }
    }

    /**
     * Method that updates each cell currently in the grid. Updating a cell includes reseting its hasMoved property, obtaining a list of all the
     * empty cells, obtaining the new neighbors of each cell, and calling update on each respective cell
     */

    public void updateCells(){
        updateGridCells();
        resetHasMoved();
        getAllEmptyCells();
        getAllNeighbors();
        for (int j = 0; j < gridCells[0].length; j++) {
            for (int i = 0; i < gridCells.length; i++) {
                for (Cell cell : gridCells[i][j].getHeldCells()){
                    cell.update(allNeighbors.get(cell), emptyCells);
                }
            }
        }
        if (this.simulationType.equals(SimulationType.GAME_OF_LIFE) || this.simulationType.equals(SimulationType.FIRE) || this.simulationType.equals(SimulationType.RPS)||this.simulationType.equals(SimulationType.FORAGING) ){ implementUpdate(); }
    }


    /**
     * After every cell has been updated, we can implement the changes all at once
     */
    private void implementUpdate(){
        for (int j = 0; j < gridCells[0].length; j++) {
            for (int i = 0; i < gridCells.length; i++) {
                for (Cell cell : gridCells[i][j].getHeldCells()){
                    cell.setCurrentState(cell.getNextState());
                    cell.setColor(cell.getNextColor());
                }
            }
        }
    }

    public Map<States, Integer> getExistingCells(){
        return existingCells;
    }
}
