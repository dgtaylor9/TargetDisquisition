package disquisition.target;

import java.awt.Point;


/*
 This class computes various statistics given a target.  
 */
public class TargetStats {
    // group center
    private Point groupCenter = null;
    private double avgX = 0.0;
    private double avgY = 0.0;
    private int numShots = 0;
    private Target theTarget = null;
        
    //group location
    private double avgElevationError = 0.0;
    private double avgWindageError = 0.0;
    
    // group size
    private double stringMeasurement = 0.0;
    private double maxSpread = 0.0;
    private Point maxSpreadPointA = null;
    private Point maxSpreadPointB = null;
    private int vertSpread = 0;
    private int horzSpread = 0;
    private double avgGroupRadius = 0.0;
    private double avgVertError = 0.0;
    private double avgHorzError = 0.0;
    double standardDeviation = 0.0;

    // extreme shot data
    private double maxShotRadius = 0.0;
    private int maxY = 0;
    private int minY = 0;
    private int maxX = 0;
    private int minX = 0;
    
    
    public TargetStats(Target t) throws Exception{
        calculate(t);
    }
    
    
    public void calculate(Target t) throws Exception{
        if (t==null || t.pointOfAim==null || t.theHoles.size()==0)
            throw new Exception("No stats available.  Target is empty!");
        theTarget = t;
        
        //iterate through the group to find maximums and averages
        int xTotal = 0;
        int yTotal = 0;
        Point[] thePoints = (Point[]) t.theHoles.toArray(new Point[0]);
        numShots = thePoints.length;
        
        Point aPoint = thePoints[0];
        maxY = (int)aPoint.getY();
        minY = (int)aPoint.getY();
        maxX = (int)aPoint.getX();
        minX = (int)aPoint.getX();
        for (int i=0; i<numShots; i++) {
            aPoint = thePoints[i];
            xTotal += (int)aPoint.getX();
            yTotal += (int)aPoint.getY();
            
            if (aPoint.getX() > maxX) {
                maxX = (int)aPoint.getX();
            }
            if (aPoint.getX() < minX) {
                minX = (int)aPoint.getX();
            }
            if (aPoint.getY() > maxY) {
                maxY = (int)aPoint.getY();
            }
            if (aPoint.getY() < minY) {
                minY = (int)aPoint.getY();
            }
            stringMeasurement += aPoint.distance(t.pointOfAim);
        }
        avgX = xTotal/numShots;
        avgY = yTotal/numShots;
        groupCenter = new Point((int)Math.round(avgX), (int)Math.round(avgY));
        
        //Now we have the group center, so go through again to calc distances
        avgWindageError = Math.abs(avgX - t.pointOfAim.getX());
        avgElevationError = Math.abs(avgY - t.pointOfAim.getY());
        horzSpread = maxX - minX;
        vertSpread = maxY - minY;
        
        double radiusTotal = 0;
        double radiusSquaredTotal = 0;
        int yErrorTotal = 0;
        int xErrorTotal = 0;
        for (int i=0; i<numShots; i++) {
            aPoint = thePoints[i];
            double thisRadius = aPoint.distance(groupCenter);
            radiusTotal += thisRadius;
            radiusSquaredTotal += thisRadius * thisRadius;
            if (thisRadius > maxShotRadius) maxShotRadius = thisRadius;
            yErrorTotal += Math.abs(aPoint.getY() - avgY);
            xErrorTotal += Math.abs(aPoint.getX() - avgX);
        }
        avgGroupRadius = radiusTotal / numShots;
        avgVertError = yErrorTotal / numShots;
        avgHorzError = xErrorTotal / numShots;
        standardDeviation = StrictMath.sqrt(radiusSquaredTotal/numShots);
        
        //find the max spread by calculating the distance between all shots
        for (int i=0; i<numShots-1; i++) {
            Point a = thePoints[i];
            for (int j=i+1; j<numShots; j++) {
                Point b = thePoints[j];
                double dist = a.distance(b);
                if (dist > maxSpread) {
                    maxSpread = dist;
                    maxSpreadPointA = a;
                    maxSpreadPointB = b;
                }
            }
        }
        
    }
    
    
    public String toString() {
        StringBuffer s = new StringBuffer("TargetStats\n");
        String indent = "  ";
        
        s.append("GROUP LOCATION\n");
        s.append(indent);
        s.append("Average Elevation Error: ");
        s.append(avgVertError);
        s.append("\n");
        s.append(indent);
        s.append("Average Windage Error: ");
        s.append(avgHorzError);
        s.append("\n");
        
        s.append("\n");
        
        s.append("GROUP SIZE\n");
        s.append(indent);
        s.append("String Measurement: ");
        s.append(stringMeasurement);
        s.append("\n");
        s.append(indent);
        s.append("Maximum Spread: ");
        s.append(maxSpread);
        s.append("\n");
        s.append(indent +indent);
        s.append("Vertical Spread: ");
        s.append(vertSpread);
        s.append("\n");
        s.append(indent + indent);
        s.append("Horziontal Spread: ");
        s.append(horzSpread);
        s.append("\n");
        
        s.append(indent);
        s.append("Average Group Radius: ");
        s.append(avgGroupRadius);
        s.append("\n");
        s.append(indent + indent);
        s.append("Average Vertical Error: ");
        s.append(avgVertError);
        s.append("\n");
        s.append(indent + indent);
        s.append("Average Horziontal Error: ");
        s.append(avgHorzError);
        s.append("\n");
        
        s.append("\n");
        
        s.append("EXTREME SHOT DATA\n");
        s.append(indent);
        s.append("Maximum Shot Radius: ");
        s.append(maxShotRadius);
        s.append("\n");
        s.append(indent);
        s.append("Lowest Shot: ");
        s.append(maxY);
        s.append("\n");
        s.append(indent);
        s.append("Highest Shot: ");
        s.append(minY);
        s.append("\n");
        s.append(indent);
        s.append("Far Right Shot: ");
        s.append(maxX);
        s.append("\n");
        s.append(indent);
        s.append("Far Left Shot: ");
        s.append(minX);
        s.append("\n");
        
        return s.toString();
    }
    
    
    public Point getGroupCenter() { return groupCenter; }
    public double getAvgX() { return avgX; }
    public double getAvgY() { return avgY; }
    
    public int getNumShots() { return numShots; }
    public double getRange() { return theTarget.getRange(); }
    
    public String getAvgElevationErrorLabel() {
        return "Average Elevation Error"; 
    }
    public double getAvgElevationError() { return avgElevationError; }
    public double getAvgElevationErrorUnits() {
        return Units.pixels2Units(getAvgElevationError()); 
    }
    public double getAvgElevationErrorMoa() {
        return Units.pixels2Moa(getAvgElevationError(), getRange());
    }
    
    public String getAvgWindageErrorLabel() { return "Average Windage Error"; }
    public double getAvgWindageError() { return avgWindageError; }
    public double getAvgWindageErrorUnits() {
        return Units.pixels2Units(getAvgWindageError());
    }
    public double getAvgWindageErrorMoa() {
        return Units.pixels2Moa(getAvgWindageError(), getRange());
    }
    
    public String getStringMeasurementLabel() { return "String Measurement"; }
    public double getStringMeasurement() { return stringMeasurement; }
    public double getStringMeasurementUnits() {
        return Units.pixels2Units(getStringMeasurement());
    }
    public double getStringMeasurementMoa() {
        return Units.pixels2Moa(getStringMeasurement(), getRange());
    }
    
    public String getMaxSpreadLabel() { return "Maximum Spread"; }
    public double getMaxSpread() { return maxSpread; }
    public double getMaxSpreadUnits() {
        return Units.pixels2Units(getMaxSpread());
    }
    public double getMaxSpreadMoa() {
        return Units.pixels2Moa(getMaxSpread(), getRange());
    }
    
    public Point getMaxSpreadPointA() { return maxSpreadPointA; }
    public Point getMaxSpreadPointB() { return maxSpreadPointB; }
    
    public String getVertSpreadLabel() { return "Vertical Spread"; }
    public int getVertSpread() { return vertSpread; }
    public double getVertSpreadUnits() {
        return Units.pixels2Units(getVertSpread());
    }
    public double getVertSpreadMoa() {
        return Units.pixels2Moa(getVertSpread(), getRange());
    }
    
    public String getHorzSpreadLabel() { return "Horziontal Spread"; }
    public int getHorzSpread() { return horzSpread; }
    public double getHorzSpreadUnits() {
        return Units.pixels2Units(getHorzSpread());
    }
    public double getHorzSpreadMoa() {
        return Units.pixels2Moa(getHorzSpread(), getRange());
    }
    
    public String getAvgGroupRadiusLabel() { return "Average Group Radius"; }
    public double getAvgGroupRadius() { return avgGroupRadius; }
    public double getAvgGroupRadiusUnits() {
        return Units.pixels2Units(getAvgGroupRadius());
    }
    public double getAvgGroupRadiusMoa() {
        return Units.pixels2Moa(getAvgGroupRadius(), getRange());
    }
    
    public String getAvgVertErrorLabel() { return "Average Vertical Error"; }
    public double getAvgVertError() { return avgVertError; }
    public double getAvgVertErrorUnits() {
        return Units.pixels2Units(getAvgVertError());
    }
    public double getAvgVertErrorMoa() {
        return Units.pixels2Moa(getAvgVertError(), getRange());
    }
    
    public String getAvgHorzErrorLabel() { return "Average Horziontal Error"; }
    public double getAvgHorzError() { return avgHorzError; }
    public double getAvgHorzErrorUnits() {
        return Units.pixels2Units(getAvgHorzError());
    }
    public double getAvgHorzErrorMoa() {
        return Units.pixels2Moa(getAvgHorzError(), getRange());
    }
    
    public String getStandardDeviationLabel() { return "Standard Deviation"; }
    public double getStandardDeviation() { return standardDeviation; }
    public double getStandardDeviationUnits() {
        return Units.pixels2Units(getStandardDeviation());
    }
    public double getStandardDeviationMoa() {
        return Units.pixels2Moa(getStandardDeviation(), getRange());
    }
    
    public String getMaxShotRadiusLabel() { return "Maximum Shot Radius"; }
    public double getMaxShotRadius() { return maxShotRadius; }
    public double getMaxShotRadiusUnits() {
        return Units.pixels2Units(getMaxShotRadius());
    }
    public double getMaxShotRadiusMoa() {
        return Units.pixels2Moa(getMaxShotRadius(), getRange());
    }
    
    public String getMaxYOffsetLabel() { return "Lowest Shot"; }
    public int getMaxY() { return maxY; }
    public double getMaxYOffset() { return getAvgY() - getMaxY(); } //want negative
    public double getMaxYOffsetUnits() {
        return Units.pixels2Units(getMaxYOffset());
    }
    public double getMaxYOffsetMoa() {
        return Units.pixels2Moa(getMaxYOffset(), getRange());
    }
    
    public String getMinYOffsetLabel() { return "Highest Shot"; }
    public int getMinY() { return minY; }
    public double getMinYOffset() { return getAvgY() - getMinY(); }
    public double getMinYOffsetUnits() {
        return Units.pixels2Units(getMinYOffset());
    }
    public double getMinYOffsetMoa() {
        return Units.pixels2Moa(getMinYOffset(), getRange());
    }
    
    public String getMaxXOffsetLabel() { return "Far Right Shot"; }
    public int getMaxX() { return maxX; }
    public double getMaxXOffset() { return getMaxX() - getAvgX(); }
    public double getMaxXOffsetUnits() {
        return Units.pixels2Units(getMaxXOffset());
    }
    public double getMaxXOffsetMoa() {
        return Units.pixels2Moa(getMaxXOffset(), getRange());
    }
    
    public String getMinXOffsetLabel() { return "Far Left Shot"; }
    public int getMinX() { return minX; }
    public double getMinXOffset() { return getAvgX() - getMinX(); }
    public double getMinXOffsetUnits() {
        return Units.pixels2Units(getMinXOffset());
    }
    public double getMinXOffsetMoa() {
        return Units.pixels2Moa(getMinXOffset(), getRange());
    }
    
    
}//class TargetStats
