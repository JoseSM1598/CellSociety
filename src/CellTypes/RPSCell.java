package CellTypes;

import Enumerations.States;
import javafx.scene.paint.Color;
import Enumerations.GridShape;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author Jose San Martin (js665)
 */
public class RPSCell extends Cell{

    private double Threshold;


    public RPSCell(States state, Map<String, Double> rules, double x, double y, double height, double width,
                   Map<States, Color> colorMap, GridShape shape){
        super(state, x, y, height, width, colorMap, shape);
        possibleStates = Arrays.asList(States.ROCK, States.PAPER, States.SCISSOR);
        Threshold = rules.get("Threshold");
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
        int surroundingRock = 0;
        int surroundingScissor = 0;
        int surroundingPaper = 0;

        for (Cell cell: neighbors) {
            if (cell.getCurrentState() == States.ROCK) {
                surroundingRock++;
            }
            else if(cell.getCurrentState() == States.SCISSOR){
                surroundingScissor++;
            }else{
                surroundingPaper++;
            }
        }
        if(getCurrentState() == States.SCISSOR){
            updateScissor(surroundingRock);
        }
        if(getCurrentState() == States.ROCK){
            updateRock(surroundingPaper);
        }
        if(getCurrentState() == States.PAPER){
            updatePaper(surroundingScissor);
        }
    }

    public void updateScissor(int surroundingRock){
        if(surroundingRock > Threshold){
            setNextState(States.ROCK);
            setNextColor(Color.RED);
        }else{
            setNextState(getCurrentState());
            setNextColor(getCurrentColor());
        }
    }

    public void updatePaper(int surroundingScissor){
        if(surroundingScissor > Threshold){
            setNextState(States.SCISSOR);
            setNextColor(Color.BLUE);
        }else{
            setNextState(getCurrentState());
            setNextColor(getCurrentColor());
        }
    }

    public void updateRock(int surroundingPaper){
        if(surroundingPaper > Threshold){
            setNextState(States.PAPER);
            setNextColor(Color.GREEN);
        }else{
            setNextState(getCurrentState());
            setNextColor(getCurrentColor());
        }
    }
}
