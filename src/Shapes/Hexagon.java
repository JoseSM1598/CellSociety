package Shapes;

/**
 * @author Jose San Martin (js665)
 */

public class Hexagon {
    double [] myPoints;
    double xOffset;
    double yOffset;

    public Hexagon(double sideLength, double xCoord, double yCoord){
        myPoints = new double[12];
        xOffset = xCoord; // These will always be at least the x and y coordinates
        yOffset = yCoord;
        getOffsets(sideLength, xCoord, yCoord);
        //     X                                            Y
        myPoints[0] = 0 +xOffset;                          myPoints[1] = sideLength/2+yOffset;
        myPoints[2] = sideLength/4+xOffset;                   myPoints[3] = sideLength+yOffset;
        myPoints[4] = 0.75*sideLength+xOffset;    myPoints[5] = sideLength+yOffset;
        myPoints[6] = sideLength+xOffset;                   myPoints[7] = sideLength/2+yOffset;
        myPoints[8] = 0.75*sideLength+xOffset;                          myPoints[9] = 0+yOffset;
        myPoints[10] = sideLength/4+xOffset;               myPoints[11] = 0+yOffset;

    }
    private void getOffsets(double sideLength, double xCoord, double yCoord){
        double xPos = xCoord / sideLength; //Literally get their x and y position on the grid
        double yPos = yCoord / sideLength;
        if(xPos == 1){
            xOffset = -sideLength/4 + xCoord;
            yOffset = sideLength/2 + yCoord;
            return;
        }
        if(xPos != 0 && xPos%2 != 0){
            xOffset = (-sideLength/4)*xPos + xCoord;
            yOffset = sideLength/2 + yCoord;
        }
        else if(xPos != 0 && xPos%2==0){
            xOffset = (-sideLength/4)*xPos+ xCoord;
        }
    }

    public double [] getPoints(){
        return myPoints;
    }
}
