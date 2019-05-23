package CellTypes;

import Enumerations.GridShape;
import Enumerations.States;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author Natalie Le (nl121)
 */

public class PatchCell extends Cell {

    private final double defaultMaxCapacity = 3;
    private double sugarAmount;
    private final double MAX_CAPACITY;

    public PatchCell(States state, Map<String, Double> rules, double x, double y, double height, double width,
                     Map<States, Color> colorMap, GridShape shape){
        super(state, x, y, height, width, colorMap, shape);
        possibleStates = Arrays.asList(States.AGENT, States.PATCH);
        defaultSettings(rules,"patchMaxCapacity", defaultMaxCapacity);
        MAX_CAPACITY = (Math.random() * (rules.get("patchMaxCapacity") + 1));
        sugarAmount = MAX_CAPACITY;
        createCellShape(height, width);
        setInitialProps();
    }

    public double getSugarAmount(){
        return sugarAmount;
    }

    public void setSugarAmount(double amount){
        sugarAmount = amount;
    }

    public Color getSugarColor(){
        Color color = colorMap.get(currentState);
        if ((color.getGreen() * 255 - sugarAmount * 20) < 0) return getCurrentColor();
        return Color.rgb((int) (color.getRed() * 255), (int) (color.getGreen() * 255 - sugarAmount * 20),
                (int) color.getBlue() * 255);
    }


    @Override
    public void setInitialProps(){
        if (colorMap.keySet().contains(currentState)){
            myColor = colorMap.get(currentState);
            myCell.setFill(getSugarColor());
            myColor = getSugarColor();
        }
        myCell.setStroke(Color.BLACK);
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
        this.setNextColor(getSugarColor());
    }
}
