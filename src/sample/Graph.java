package sample;

import Enumerations.States;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.Map;

/**
 * @author Natalie Le (nl121)
 */

public class Graph {
    private Group myRoot;
    private Map<States, Color> lineColors;
    private final double verticalScale;
    private final double horizontalScale;
    private double currentX;
    private double currentY;
    private double maximumX;

    public Graph(int length, int height, double xCoord, double yCoord, int totalCells, Map<States, Color> lineColors){
        myRoot = new Group();
        Line xAxis = new Line(xCoord, yCoord, xCoord + length, yCoord);
        Line yAxis = new Line(xCoord, yCoord, xCoord, yCoord - height);
        this.lineColors = lineColors;
        this.verticalScale = (double) height / (double) totalCells;
        this.horizontalScale = 1;
        this.currentX = xCoord;
        this.currentY = yCoord;
        this.maximumX = xCoord + length;
        Text yLabel = new Text(xCoord,yCoord - height, "# cells");
        Text xLabel = new Text(xCoord + length, yCoord, "t");
        myRoot.getChildren().add(xAxis);
        myRoot.getChildren().add(yAxis);
        myRoot.getChildren().add(xLabel);
        myRoot.getChildren().add(yLabel);
    }

    public void update(Map<States, Integer> existingCellTypes){
        if (this.currentX >= this.maximumX) return;
        this.currentX += this.horizontalScale;
        for (States cellType : existingCellTypes.keySet()){
            Circle dot = new Circle(this.currentX,
                    this.currentY - (this.verticalScale * existingCellTypes.get(cellType)), 2,
                    lineColors.get(cellType));
            myRoot.getChildren().add(dot);
        }
    }

    public Group getRoot(){
        return myRoot;
    }
}
