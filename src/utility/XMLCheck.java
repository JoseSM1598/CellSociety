package utility;


import Enumerations.EdgeTypes;
import Enumerations.SimulationType;
import Enumerations.States;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import Enumerations.GridShape;
import java.util.*;

/**
 * The purpose of this class is to double check everything that the XMLReader receives. This class makes sure that if
 * there are missing items in the XML file, to either return a default setting or to throw a new exception.
 * @author Joyce Zhou (jyz11)
 **/
public class XMLCheck {
    private XMLReader myReader;
    private ResourceBundle myErrors;

    public XMLCheck(XMLReader reader) {
        myReader = reader;
        myErrors = ResourceBundle.getBundle("resources/Errors");
    }

    public String getName() {
        try {
            try {
                SimulationType simulation = myReader.getName().contains(" ") ? SimulationType.GAME_OF_LIFE : SimulationType.valueOf(myReader.getName().toUpperCase());
            } catch (Exception e){
                new Alert(Alert.AlertType.ERROR, myErrors.getString("InvalidFileType")).showAndWait();
                throw new XMLException(myErrors.getString("InvalidFileType"));
            }
            return myReader.getName();
        } catch(Exception e) {
            new Alert(Alert.AlertType.ERROR, myErrors.getString("NoSimulationName")).showAndWait();
            throw new XMLException(myErrors.getString("NoSimulationName"));
        }
    }

    public String getDescription() {
        try {
            return myReader.getOriginalDescription();
        } catch (Exception e) {
            return myErrors.getString("NoDescription");
        }
    }

    public String getCellFormat() {
        try{
            String initial = myReader.getCellFormat().toUpperCase();
            if(initial.equals("PERCENT") || initial.equals("COUNT") || initial.equals("HARDCODE")) {
                return initial;
            }
            new Alert(Alert.AlertType.ERROR, myErrors.getString("WrongInitConfig")).showAndWait();
            throw new XMLException(myErrors.getString("SetInitialConfig"));
        } catch (Exception e){
            throw new XMLException(myErrors.getString("SetInitialConfig"));
        }
    }

    public int getDimension(int size) {
        if (size < 0) {
            return Math.abs(size);
        } else if (size < 1) {
            int dimension = 20;
            return dimension;
        }
        return size;
    }

    public int getWidth() {
        try {
            return getDimension(myReader.getWidth());
        } catch(Exception e) {
            new Alert(Alert.AlertType.ERROR, myErrors.getString("NoHeightWidth")).showAndWait();
            int dimension = 20;
            return dimension;
        }
    }

    public int getHeight() {
        try {
            return getDimension(myReader.getHeight());
        } catch(Exception e) {
            new Alert(Alert.AlertType.ERROR, myErrors.getString("NoHeightWidth")).showAndWait();
            int dimension = 20;
            return dimension;
        }
    }

    public GridShape getGridShape(){
        try {
            return GridShape.valueOf(myReader.getShape().toUpperCase());
        } catch (Exception e){
            return GridShape.RECTANGLE;
        }
    }

    public EdgeTypes getEdgetypes() {
        try {
            return EdgeTypes.valueOf(myReader.getEdge().toUpperCase());
        } catch(Exception e) {
            return EdgeTypes.FINITE;
        }
    }

    public boolean getGridOutline(){
        try {
            if(myReader.getOutline().toUpperCase().equals("FALSE")){
                return false;
            } else return true;
        } catch (Exception e){
            return true;
        }
    }

    public Map<States, Color> getCellColor() {
        try {
            Map<String, String> initialColor = myReader.getColors();
            Map<States, Color> colorMap = new HashMap<>();
            for (String cell: initialColor.keySet()){
                States state = States.valueOf(cell.toUpperCase());
                Color color = Color.valueOf(initialColor.get(cell).toUpperCase());
                colorMap.put(state, color);
            }
            return colorMap;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    /**
     *
     * @return ArrayList<String>: holds the order in which the cells will be initialized, and
     * the String is DEFINITELY an enum type
     */
    public ArrayList<String> getInitialParams() {
        if (getCellFormat().equals("COUNT")) {
            checkStateOfCell(myReader.getInitCells());
            boolean rightNumber = checkNumberofCells(myReader.getInitCells());
            return getCellsbyNumber(myReader.getInitCells(),rightNumber);
        } else if (getCellFormat().equals("PERCENT")) {
            checkStateOfCell(myReader.getInitPercent());
            boolean rightNumber = checkPercentageOfCells(myReader.getInitPercent());
            return getCellbyPercent(myReader.getInitPercent(),rightNumber);
        } else {
            ArrayList<String[]> some = getCellByPosition(myReader.getInitPosition());
            return checkPositionOfCells(some);
        }

    }

    public Map<String,Integer> getStates () {
        ArrayList<String> listOfCells = getInitialParams();
        Map<String,Integer> cellStates = new HashMap<>();
        for (String cellName:listOfCells) {
            if (cellStates.containsKey(cellName)){
                int plusCell = cellStates.get(cellName)+1;
                cellStates.put(cellName, plusCell);
            } else {
                cellStates.put(cellName, 1);
            }
        }
        return cellStates;
    }

    public ArrayList<String[]> getCellByPosition(ArrayList<String> cells) {
        ArrayList<String[]> cellRows = new ArrayList<>();
        for (String positions : cells) {
            String[] splited = positions.split(" ");
            cellRows.add(splited);
        }
        return cellRows;
    }

    public ArrayList<String> checkPositionOfCells(ArrayList<String[]> positions){
        ArrayList<String> allCells = new ArrayList<>();
        if (positions.size()> getHeight() || positions.size()< getHeight()) {
            new Alert(Alert.AlertType.ERROR, myErrors.getString("CellOutOfBounds")).showAndWait();
            throw new XMLException(myErrors.getString("CellOutOfBounds"));
        } else {
            for (String[] row:positions) {
                if (row.length > getWidth() || row.length<getWidth()) {
                    new Alert(Alert.AlertType.ERROR, myErrors.getString("CellOutOfBounds")).showAndWait();
                    throw new XMLException(myErrors.getString("CellOutOfBounds"));
                } else if (row.length == getWidth()) {
                    addCellPositions(row, allCells);
                }
            }
        }
        return allCells;
    }

    public void addCellPositions(String[] row, ArrayList<String> allCells){
        try {
            for (String cell : row) {
                States.valueOf(cell.toUpperCase());
                allCells.add(cell.toUpperCase());
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, myErrors.getString("InvalidNumberOfCells")).showAndWait();
            throw new XMLException(myErrors.getString("InvalidCellType"));
        }
    }

    public boolean checkNumberofCells(Map<String, Double> cellType) {
        int totalCells = getHeight()*getWidth();
        int countCells = 0;
        if(cellType.size() == 0) {
            return false;
        }
        for (String cell: cellType.keySet()) {
            if (Math.abs(cellType.get(cell))<1 || cellType.get(cell)<0 ||
                    cellType.get(cell)!=Math.round(cellType.get(cell))){
                return false;
            } else {
                countCells=countCells+cellType.get(cell).intValue();
            }
        }
        SimulationType simulation = myReader.getName().contains(" ") ? SimulationType.GAME_OF_LIFE : SimulationType.valueOf(myReader.getName().toUpperCase());
        if (countCells != totalCells && !simulation.equals(SimulationType.SUGARSCAPE)) {
            new Alert(Alert.AlertType.ERROR, myErrors.getString("InvalidNumberOfCells")).showAndWait();
            throw new XMLException(myErrors.getString("InvalidNumberOfCells"));
        }
        return true;
    }

    public ArrayList<String> getCellsbyNumber(Map<String, Double> cellType, boolean rightNumber) {
        int totalCells = getHeight()*getWidth();
        ArrayList<String> cellNames = new ArrayList<>();
        if (rightNumber){
            for(String cellName: cellType.keySet()){
                for(int i = 0; i<cellType.get(cellName);i++){
                    cellNames.add(cellName);
                }
            }
        } else {
            for (int i = 0;i<totalCells; i++){
                cellNames.add("RANDOM");
            }
        }
        Collections.shuffle(cellNames);
        return cellNames;
    }

    public boolean checkPercentageOfCells(Map<String, Double> cellType){
        double percentCount=0;
        if(cellType.size() == 0) {
            return false;
        }
        for (String cell: cellType.keySet()) {
            if (Math.abs(cellType.get(cell))>1 || cellType.get(cell)<0){
                return false;
            } else {
                percentCount=percentCount+cellType.get(cell);
            }
        }
        if (percentCount != 1) {
            new Alert(Alert.AlertType.ERROR, myErrors.getString("InvalidCellProbability")).showAndWait();
            throw new XMLException(myErrors.getString("InvalidCellProbability"));
        }
        return true;
    }

    public ArrayList<String> getCellbyPercent(Map<String, Double> cellType, boolean rightNumber) {
        int totalCells = getHeight()*getWidth();
        int cellCount = 0;
        ArrayList<String> cellNames = new ArrayList<>();
        if (rightNumber){
            for(String cellName: cellType.keySet()){
                int number = (int)(cellType.get(cellName)*totalCells);
                cellCount=cellCount+number;
                if(cellCount>totalCells){
                    number = totalCells - cellCount;
                }
                for(int i = 0; i<number;i++){
                    cellNames.add(cellName);
                }
            }
        } else {
            for (int i = 0;i<totalCells; i++){
                cellNames.add("RANDOM");
            }
        }
        Collections.shuffle(cellNames);
        return cellNames;
    }

    public void checkStateOfCell(Map<String, Double> cellType){
        try {
            for (String cellName : cellType.keySet()) {
                States.valueOf(cellName.toUpperCase());
            }
        }
        catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, myErrors.getString("InvalidNumberOfCells")).showAndWait();
            throw new XMLException(myErrors.getString("InvalidCellType"));
        }
    }

    public Map<String, Double> getThresholds() {
        Map<String, Double> rules = myReader.getThresholds();
        return rules;
    }
}
