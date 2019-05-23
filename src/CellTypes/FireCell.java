package CellTypes;

import Enumerations.States;
import javafx.scene.paint.Color;
import Enumerations.GridShape;


import java.util.Map;
import java.util.*;

/**
 * @author Jose San Martin (js665)
 */
public class FireCell extends Cell{
    private final double PROBCATCH;
    private final double defaultProbCatch = 0.5;

    public FireCell(States state, Map<String, Double> rules, double x, double y, double height, double width, Map<States, Color> colorMap, GridShape shape) {
        super(state, x, y, height, width, colorMap, shape);

        possibleStates = Arrays.asList(States.TREE, States.BURNING, States.EMPTY);
        currentState = getRightState(state, possibleStates);
        defaultSettings(rules,"probCatch", defaultProbCatch);
        PROBCATCH= rules.get("probCatch");

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

    @Override
        public void update(Set<Cell> neighbors, Set<Cell> emptyCells) {
        int neighboringFire = 0;
        for (Cell cell: neighbors) {
            if (cell.getCurrentState().equals(States.BURNING)) {
                neighboringFire++;
            }
        }

        double catchFire = Math.random();
        if(currentState.equals(States.EMPTY)){
            setNextState(getCurrentState());
            setNextColor(Color.YELLOW);

        }else if(currentState.equals(States.BURNING)){
            setNextState(States.EMPTY);
            setNextColor(Color.YELLOW);
        }else if(currentState.equals(States.TREE) & catchFire < PROBCATCH & neighboringFire >= 1){
            setNextState(States.BURNING);
            setNextColor(Color.RED);
        }else{
            setNextState(States.TREE);
            setNextColor(Color.GREEN);
        }

    }

}
