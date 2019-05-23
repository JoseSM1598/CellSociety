package CellTypes;

import java.util.HashSet;
import java.util.Set;

public class CellBucket {
    private Set<Cell> heldCells;

    public CellBucket(){
        heldCells = new HashSet<>();
    }

    public CellBucket(Cell cell){
        heldCells = new HashSet<>();
        heldCells.add(cell);
    }

    public void addCell(Cell cell){
        heldCells.add(cell);
    }

    public void removeCell(Cell cell){
        heldCells.remove(cell);
    }

    public Set<Cell> getHeldCells(){
        return heldCells;
    }

    public void emptyHeldCells(){
        heldCells.clear();
    }
}
