package Shapes;

/**
 * @author Jose San Martin (js665)
 */

public class Square {
    double [] myPoints;
    double xOffset;
    double yOffset;

    public Square(double sideLength, double xCoord, double yCoord){
        myPoints = new double[8];
        xOffset = xCoord; // These will always be at least the x and y coordinates
        yOffset = yCoord;
        //     X                                            Y
        myPoints[0] = 0 +xOffset;                           myPoints[1] = 0+yOffset;
        myPoints[2] = 0+xOffset;                            myPoints[3] = sideLength+yOffset;
        myPoints[4] = sideLength+xOffset;                   myPoints[5] = sideLength+yOffset;
        myPoints[6] = sideLength+xOffset;                   myPoints[7] = 0+yOffset;

    }


    public double [] getPoints(){
        return myPoints;
    }
}
