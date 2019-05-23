package Shapes;

/**
 * @author Jose San Martin (js665)
 */

public class Triangle {
    double [] myPoints;
    double xOffset;
    double yOffset;

    public Triangle(double sideLength, double xCoord, double yCoord){
        myPoints = new double[6];
        double xPos = xCoord / sideLength; //Literally get their x and y position on the grid
        double yPos = yCoord / sideLength;
        xOffset = xCoord - (sideLength/2)*xPos; // These will always be at least the x and y coordinates
        yOffset = yCoord;
        getOffsets(sideLength, xCoord, yCoord);
        if(yPos %2 == 0){
            if(xPos%2 == 0){
                getEvenCoords(xPos, sideLength);
            }else{
                getOddCoords(xPos, sideLength);
            }
        }
        else if(yPos%2 != 0){
            if(xPos%2 == 0){
                getOddCoords(xPos, sideLength);
            }else{
                getEvenCoords(xPos, sideLength);
            }
        }
    }

    public void getEvenCoords(double xPos, double sideLength){
        //     X                                            Y
        myPoints[0] = 0 +xOffset;                          myPoints[1] = sideLength+yOffset;
        myPoints[2] = sideLength +xOffset;                   myPoints[3] = sideLength+yOffset;
        myPoints[4] = sideLength/2+xOffset;                  myPoints[5] = 0+yOffset;

    }

    public void getOddCoords(double xPos, double sideLength){
        myPoints[0] = 0 +xOffset;                       myPoints[1] = 0+yOffset;
        myPoints[2] = sideLength/2 +xOffset;              myPoints[3] = sideLength+yOffset;
        myPoints[4] = sideLength+xOffset;               myPoints[5] =0 +yOffset;
    }

    private void getOffsets(double sideLength, double xCoord, double yCoord) {

    }

    public double [] getPoints(){
        return myPoints;
    }
}

