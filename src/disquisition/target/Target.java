package disquisition.target;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;


/** This class represents a target with a set of holes shot through it.
   each hole has coordinates associated with it.
   Statistical functions compute various numbers associated with the grouping
*/
public class Target implements Observer {
    
    ArrayList<Point> theHoles = new ArrayList<Point>();
    Point pointOfAim = null;
    double range = 0;
    
    
    public Target() {
        super();
        Units.addObserve(this);
    }
    
    
    public String toString() {
        StringBuffer s = new StringBuffer("Target:\n");
        s.append("poa\n");
        s.append(pointOfAim.toString());
        int size = theHoles.size();
        s.append(size + " holes\n");
        for (int i=0; i<size; i++) {
            s.append(((Point)theHoles.get(i)).toString());
            s.append("\n");
        }
        return s.toString();
    }
    
    
    public void addHole(Point h) {
        theHoles.add(h);
    }
    
    
    public void addHole(int x, int y) {
        addHole(new Point(x, y));
    }
    
    
    public Point removeLastHole() {
        Point retval = null;
        if (theHoles.size() > 0) 
            retval = (Point) theHoles.remove(theHoles.size()-1);
        else if (pointOfAim != null ) {
            retval = pointOfAim;
            pointOfAim = null;
        }
        return retval;
    }
    
  
    public boolean removeHole(Point h) {
        return theHoles.remove(h);
    }
    
    
    public boolean removeHole(int x, int y) {
        return removeHole(new Point(x,y));
    }
    
    
    public void setPointOfAim(Point p) {
        pointOfAim = p;
    }
    
    
    public void setPointOfAim(int x, int y) {
        setPointOfAim(new Point(x,y));
    }
    
    
    public TargetStats getStats() throws Exception {
        return new TargetStats(this);
    }
    
    
    public void setRange(double r) {
        range = r;
        System.out.println("setRange("+r+")");
    }
    
    public double getRange() {
        return range;
    }
    
    
    /** observer interface */
    public void update(Observable o, Object arg) {
        setRange(Units.switchRangeUnits(range));
    }
    
    
    /** translate all coordinates of the target by the given x and y deltas
        (pixels)
        */
    public void translate(int dx, int dy) {
        pointOfAim.translate(dx, dy);
        Iterator<Point> it = theHoles.iterator();
        while (it.hasNext()) {
            Point aPoint = (Point) it.next();
            aPoint.translate(dx, dy);
        }
    }
    
    
    /** move the points of the target so that there isn't a bunch of wasted
        space on the screen
        */
    public void rightSize() {
        try {
            TargetStats ts = new TargetStats(this);
            // start with the minX and minY holes
            int minX = ts.getMinX();
            int minY = ts.getMinY();
            // make sure we have enough room to draw the max radius circle
            int maxRadius = (int) ts.getMaxShotRadius();
            minX = Math.min(minX, (int)ts.getAvgX() - maxRadius);
            minY = Math.min(minY, (int)ts.getAvgY() - maxRadius);
            // now make sure we include the point of aim
            minX = Math.min(minX, (int)pointOfAim.getX());
            minY = Math.min(minY, (int)pointOfAim.getY());
            // add (subtract) a bit of border
            minX -= 30;
            minY -= 30;
            // now translate up and left (negative) by the figured amounts
            translate(0 - minX, 0 - minY);
        }
        catch (Exception e) {
            System.out.println("problem in Target.rightSize(): ");
            e.printStackTrace();
        }
    }
  
    
    /**testing*/
    public static void main(String[] args) throws Exception {
        System.out.println("Hello");
        Target t = new Target();
        Point a = new Point(5,5);
        Point b = new Point(8,9);
        //t.addHole(15,16);
        t.setPointOfAim(10,10);
        t.addHole(a);
        t.addHole(b);
        System.out.println(t);
        t.removeHole(a);
        System.out.println(t);
        
        System.out.println("dist=" + a.distance(b));
        
        TargetStats ts = new TargetStats(t);
        System.out.println(ts);
    }
    
    
}//Target
