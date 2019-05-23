package sample;

import CellTypes.Cell;
import Enumerations.GridShape;
import Enumerations.SimulationType;
import Enumerations.States;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author Joyce Zhou (jyz11)
 */

public class GridToroidal extends Grid {
    public GridToroidal(List<String> parameters, Map<String, Double> rules, SimulationType simulationType,GridShape gridShape,
                   int cellsPerColumn, int cellsPerRow, double gridLength, Map<States, Color> colorMap){
        super(parameters, rules, simulationType, gridShape, cellsPerColumn, cellsPerRow, gridLength, colorMap);
    }

    @Override
    public HashSet<Cell> getSquareNeighbors(int row, int col){
        HashSet<Cell> neighbors = new HashSet<>();
        for(int k =-1; k<=1; k++) {
            for(int l = -1; l <= 1; l++){
                int a = k + row;
                int b = l + col;
                if (k==0 && l==0) {}
                else {
                    a = getBorderCases(a,GRID_LENGTH/cellHeight);
                    b = getBorderCases(b,GRID_LENGTH/cellWidth);
                    for (Cell cell : gridCells[a][b].getHeldCells()) {
                        neighbors.add(cell);
                    }
                }
            }
        }
        return neighbors;
    }

    public int getBorderCases(int rowOrCol, double gridDimension) {
        if (rowOrCol==gridDimension) {
            return 0;
        } else if (rowOrCol == gridDimension+1) {
            return 1;
        } else if(rowOrCol==-1){
            return (int)(gridDimension - 1);
        } else if (rowOrCol==-2){
            return (int)(gridDimension - 2);
        }
        return rowOrCol;
    }


    @Override
    public HashSet<Cell> getHexagonNeighbors(int row, int col) {
        HashSet<Cell> neighbors = new HashSet<>();
        for(int k =-1; k<=1; k++) {
            for(int l = -1; l <= 1; l++){
                int a = k + row;
                int b = l + col;
                if (k==0 && l==0) {continue;}
                if(row%2==0){//if even row
                    if((k==-1 & l ==1) | (k == 1 & l ==1)){continue;}
                    a = getBorderCases(a,GRID_LENGTH/cellHeight);
                    b = getBorderCases(b,GRID_LENGTH/cellWidth);
                    for (Cell cell : gridCells[a][b].getHeldCells()) {
                        neighbors.add(cell);
                    }
                }
                else{ //if odd row
                    if((k==-1 & l ==-1) | (k == 1 & l ==-1)){continue;}
                    a = getBorderCases(a,GRID_LENGTH/cellHeight);
                    b = getBorderCases(b,GRID_LENGTH/cellWidth);
                    for (Cell cell : gridCells[a][b].getHeldCells()) {
                        neighbors.add(cell);
                    }
                }
            }
        }
        return neighbors;
    }

    @Override
    public HashSet<Cell> getTriangleNeighbors(int row, int col){
        HashSet<Cell> neighbors = new HashSet<>();
        for(int k =-2; k<=2; k++) {
            for(int l = -1; l <= 1; l++){
                int a = k + row;
                int b = l + col;
                if (k==0 && l==0) {continue;}
                if((row%2==0 && col %2 == 0) || (row%2 != 0 && col%2 != 0)){//if even row && even col or odd row and odd col
                    if((k==-2 & l ==-1) | (k == 2 & l ==-1)){continue;}
                    a = getBorderCases(a,GRID_LENGTH/cellHeight);
                    b = getBorderCases(b,GRID_LENGTH/cellWidth);
                    for (Cell cell : gridCells[a][b].getHeldCells()) {
                        neighbors.add(cell);
                    }
                }
                else{ //if odd row
                    if((k==-2 & l ==1) | (k == 2 & l ==1)){continue;}
                    a = getBorderCases(a,GRID_LENGTH/cellHeight);
                    b = getBorderCases(b,GRID_LENGTH/cellWidth);
                    for (Cell cell : gridCells[a][b].getHeldCells()) {
                        neighbors.add(cell);
                    }
                }
            }
        }
        return neighbors;
    }

}
