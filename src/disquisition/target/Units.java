package disquisition.target;

import java.util.Observable;
import java.util.Observer;


/** this class is in charge of the screen calibration.  It also
  handles converting between English and Metric systems.  It converts
  pixel measurement into measurements in the current system. 
*/
public class Units extends Observable {
    private static Units theUnits = new Units();
    
    private static boolean system = false;
    private static double pixelsPerUnit = 0;
    private static double pixelsPerStandard = 0;
    
    public static final boolean METRIC = true;
    public static final boolean ENGLISH = false;
    
    private static final int ENGLISH_STANDARD = 4;  //use 4 inches for calibration
    private static final String ENGLISH_STANDARD_UNITS = "inch";
    private static final String ENGLISH_STANDARD_UNITS_ABBR = "in.";
    private static final String ENGLISH_SYSTEM_NAME = "English";
    
    private static final int METRIC_STANDARD = 10;  // use 10 cm for calibration
    private static final String METRIC_STANDARD_UNITS = "centimeter";
    private static final String METRIC_STANDARD_UNITS_ABBR = "cm";
    private static final String METRIC_SYSTEM_NAME = "Metric";
    
    private static final String METRIC_RANGE_UNITS = "meter";
    private static final String ENGLISH_RANGE_UNITS = "yard";
    

    
    private Units() {
        super();
    }
        
        
        
    public static boolean getCurrentSystem() { return system; }
    
    public static String getCurrentSystemName() {
        return getSystemName(system);
    }
    
    
    public static String getSystemName(boolean aSystem) { 
        return aSystem ? METRIC_SYSTEM_NAME : ENGLISH_SYSTEM_NAME;
    }
    
    
    public static void setEnglishSystem() {
        setCurrentSystem(ENGLISH);
    }
    
    public static void setMetricSystem() {
        setCurrentSystem(METRIC);
    }
    
    public static void setCurrentSystem(boolean newSystem) {
        if (newSystem == system) return;
        double conversionFactor;
        double standardFactor;
        if (system) {
            //convert metric to English
            conversionFactor = 2.54;  //2.54 cm per 1 inch
            standardFactor = ENGLISH_STANDARD;
        }
        else {
            //convert English to metric
            conversionFactor = 0.393701;  //0.393701 inch per 1 cm
            standardFactor = METRIC_STANDARD;
        }
        pixelsPerUnit = pixelsPerUnit * conversionFactor;
        pixelsPerStandard = pixelsPerUnit * standardFactor;
        system = newSystem;
        /* the conversions above introduces small errors... so
           recalculate the pixelsPerUnit to eliminate them */
        setPixelsPerStandard((int)Math.round(pixelsPerStandard));
    }
    
    
    /** @param range a range in the previous units
        @return the given range converted to the current unit system
        */
    public static double switchRangeUnits(double range) {
        double factor;
        if (system) {
            //convert English to Metric
            factor = 0.9144001;
        }
        else {
            //convert Metric to English
            factor = 1.0936131;
        }
        return range * factor;
    }
    
    
    /** 
        @param pixels the number of pixels contained in the current standard
        i.e. the number of pixels in 4 inches
        */
    public static void setPixelsPerStandard(int pixelsPerStandard) {
        //System.out.println("setPixelsPerStandard(" + pixelsPerStandard + ")");
        Units.pixelsPerStandard = pixelsPerStandard;
        double unitsPerStandard;
        if (system) {
            unitsPerStandard = METRIC_STANDARD;
        }
        else {
            unitsPerStandard = ENGLISH_STANDARD;
        }
        pixelsPerUnit = pixelsPerStandard / unitsPerStandard;
        printPixelsPerUnit();
        theUnits.setChanged();
        theUnits.notifyObservers();
    }
    
    
    /** return the number of units in the current standard
        */
    public static int getCurrentStandard() {
        return system ? METRIC_STANDARD : ENGLISH_STANDARD;
    }
     
    
    /** return name of the units used. i.e. inches or centimeters.
        */
    public static String getCurrentStandardUnits() {
        return system ? METRIC_STANDARD_UNITS : ENGLISH_STANDARD_UNITS;
    }
    
    
    public static String getcurrentStandardUnitsAbbrev() {
        return system ? METRIC_STANDARD_UNITS_ABBR : ENGLISH_STANDARD_UNITS_ABBR;
    }
    
    
    public static String getCurrentRangeUnits() {
        return system ? METRIC_RANGE_UNITS : ENGLISH_RANGE_UNITS;
    }
    
    
    public static double getPixelsPerStandard() {
        return pixelsPerStandard;
    }
    
    
    /** convert a size in pixels to a size in the current unit system
        */
    public static double pixels2Units(int pixels) {
        return pixels2Units((double)pixels);
    }
    
    public static double pixels2Units(double pixels) {
        return pixels/pixelsPerUnit;
    }
    
    
    public static double pixels2Moa(double p, double range) {
        return standardUnits2Moa(pixels2Units(p), range);
    }

    public static double standardUnits2Moa(double su, double range) {
        return rangeUnits2Moa(standardUnits2RangeUnits(su), range);
    }
    
    /** @param ru a distance you want a Minute of Angle (MOA) for.
        @param range how far away from ru?
     Note: ru and range must be in the same units i.e. both yards or both meters
        @return the size in Minutes of Angle of ru at the given range
        */
    public static double rangeUnits2Moa(double ru, double range) {
        return 60 * StrictMath.toDegrees(StrictMath.atan(ru/range));
    }
    
    public static double standardUnits2RangeUnits(double su) {
        int conversionFactor = system ? 100 : 36;
        return su/conversionFactor;
    }
    
    
    public static boolean isCalibrated() {
        return pixelsPerStandard==0 ? false : true;
    }
    
    public static String printPixelsPerUnit() {
        StringBuffer sb = new StringBuffer("Now using ");
        sb.append(pixelsPerUnit);
        sb.append(" pixels per ");
        sb.append(getCurrentStandardUnits());
        System.out.println(sb.toString());
        return sb.toString();
    }
    
    
    /** grr... can't override addObserver()... */
    public static void addObserve(Observer o) { theUnits.addObserver(o); }
 
}
