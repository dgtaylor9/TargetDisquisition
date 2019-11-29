package disquisition.target;

import java.util.Observable;
import java.util.Observer;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import javax.swing.table.AbstractTableModel;


@SuppressWarnings("serial")
public class StatsTableModel extends AbstractTableModel
                             implements Observer {
                                 
    protected TargetStats theStats = null;
    
    protected String[] methods = { "AvgElevationError", "AvgWindageError",
        "StringMeasurement", "MaxSpread", "VertSpread", "HorzSpread",
        "AvgGroupRadius", "AvgVertError", "AvgHorzError", "StandardDeviation",
        "MaxShotRadius", "MaxYOffset", "MinYOffset", "MaxXOffset",
        "MinXOffset" };
    
    protected String[] methodSuffixs = { "Label", "Units", "Moa" };
    
    protected String[] colNames =
           { "Measurement", null, "MOA" };
    
    protected int[] colSizes = null;
    
    protected Object[][] cache = null;    
    
    
    protected void initalizeCache() {
        cache = new Object[getRowCount()] [getColumnCount()];
        colSizes = null;
    }
    
    
    protected void setUnits() {
        colNames[1] = Units.getcurrentStandardUnitsAbbrev();
    }
    
    
    
    public StatsTableModel(TargetStats ts) {
        super();
        theStats = ts;
        initalizeCache();
        setUnits();
        Units.addObserve(this);
    }

    
    /** Observer interface.  Called when the system units are changed */
    public void update(Observable o, Object arg) {
        setUnits();
        initalizeCache();
        fireTableStructureChanged();
    }
    
    
    public int getRowCount() {
        return methods.length;
    }
    
    
    public int getColumnCount() {
        return methodSuffixs.length;
    }
    
    
    public Object getValueAt(int row, int column) {
        DecimalFormat nf = new DecimalFormat("##0.00");
        Object retVal = cache[row][column];
        if ( retVal == null ) {
            String methodName = "get" + methods[row] + methodSuffixs[column];
            try {
                Method m = theStats.getClass().getMethod(methodName, (Class<?>[])null);
                //System.out.println("Invoking: " + m.getName());
                retVal = m.invoke(theStats, (Object[])null);
                if (retVal instanceof Double) {
                    retVal = nf.format(((Double)retVal).doubleValue());
                }
                cache[row][column] = retVal;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return retVal;
    }
    
    
    public String getColumnName(int column) { return colNames[column]; }
    
    
    public String toString() {
        calcColSizes();
        StringBuffer sb = new StringBuffer();
        sb.append("Range: ");
        sb.append(theStats.getRange());
        sb.append(" ");
        sb.append(Units.getCurrentRangeUnits());
        sb.append("s\n\n");
        
        for (int i=0; i<colNames.length; i++) {
            String value = colNames[i];
            sb.append(value);
            sb.append(spaces(colSizes[i] - value.length()));
        }
        sb.append("\n");
        int cols = getColumnCount();
        for (int i=0; i<getRowCount(); i++) {
            for (int j=0; j<cols; j++) {
                String value = (String) getValueAt(i,j);
                if (value != null) {
                    sb.append(value);
                    sb.append(spaces(colSizes[j] - value.length()));
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    
    protected void calcColSizes() {
        if (colSizes == null) {
            int numCols = colNames.length;
            colSizes = new int[numCols];
            for (int i=0; i<numCols; i++) colSizes[i] = colNames[i].length();
            for (int i=0; i<getRowCount(); i++) {
                for (int j=0; j<numCols; j++) {
                    String value = (String)getValueAt(i,j);
                    if (value != null) {
                        int len = value.length();
                        if (len > colSizes[j])
                            colSizes[j] = len;
                    }
                }
            }
            for (int i=0; i<numCols; i++) colSizes[i] = colSizes[i] + 2;
        }
    }
    
    
    protected String spaces(int x) {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<x; i++)
            sb.append(" ");
        return sb.toString();
    }
    
    
}
