package CellTypes;

import Enumerations.States;
import javafx.scene.paint.Color;
import Enumerations.GridShape;


import java.util.*;


/**
 * @author Jose San Martin (js665), Joyce Zhou (jyz11)
 */

public class GameOfLifeCell extends Cell{
    private final double UNDERPOPULATION;
    private final double OVERPOPULATION;
    private final double REPRODUCE;

    public GameOfLifeCell(States state, Map<String, Double> rules, double x, double y, double height, double width,
                          Map<States, Color> colors, GridShape shape) {
        super(state, x, y, height, width, colors, shape);

        possibleStates = Arrays.asList(States.ALIVE, States.DEAD);
        currentState = getRightState(state, possibleStates);

        Map <String, Double> defaultRuleMap = defaultMap();
        for (String rule:defaultRuleMap.keySet()) {
            defaultSettings(rules,rule,defaultRuleMap.get(rule));
        }

        UNDERPOPULATION = rules.get("underpopulate");
        OVERPOPULATION = rules.get("overpopulate");
        REPRODUCE = rules.get("reproduce");
        createCellShape(height, width);
        setInitialProps();
    }

    public Map<String,Double> defaultMap() {
        double defaultUnderpopulation = 3;
        double defaultOverpopulation = 3;
        double defaultReproduce = 2;

        Map<String,Double> map = new HashMap<>();
        map.put("underpopulate",defaultUnderpopulation);
        map.put("overpopulate",defaultOverpopulation);
        map.put("reproduce",defaultReproduce);
        return map;
    }


    @Override
    public void defaultSettings(Map<String,Double> rules, String rule, double defaultsetting){
        if(!rules.containsKey(rule)) {
            rules.put(rule, defaultsetting);
        } else {
            int ruleNumber = rules.get(rule).intValue();
            if (ruleNumber==0) {
                rules.put(rule, defaultsetting);
            } else if (ruleNumber<0) {
                rules.put(rule, Math.abs(rules.get(rule)));
            }
        }
    }

    @Override
    public void update(Set<Cell> neighbors, Set<Cell> emptyCells) {
        //Get the number of cells alive
        int surroundingAlive = 0;

        for (Cell cell: neighbors) {
            if (cell.getCurrentState() == States.ALIVE) {
                surroundingAlive++;
            }
        }

        if(currentState == States.ALIVE && (surroundingAlive < UNDERPOPULATION || surroundingAlive > OVERPOPULATION)){
            //myCell.setFill(Color.WHITE);
            //setCurrentState(States.DEAD);
            setNextColor(colorMap.get(States.DEAD));
            setNextState(States.DEAD);
        } else if (currentState == States.DEAD && surroundingAlive == REPRODUCE){
           // myCell.setFill(Color.BLUE);
            //setCurrentState(States.ALIVE);
            setNextColor(colorMap.get(States.ALIVE));
            setNextState(States.ALIVE);
        }else{
            setNextState(getCurrentState());
            setNextColor(getCurrentColor());
        }

    }

}

