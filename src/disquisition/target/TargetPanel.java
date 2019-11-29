package disquisition.target;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;



@SuppressWarnings("serial")
public class TargetPanel extends JPanel {
    protected Target theTarget = null;
    protected TargetStats ts = null;
    protected static int holeSize = 20;

	private MouseAdapter theMouseAdapter = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                pointClicked(e.getX(), e.getY(), !e.isShiftDown());
            }
        }
    };
    
    public TargetPanel() {
        super();
        theTarget = new Target();
        configure();
    }
    
    
    public TargetPanel(Target t) {
        super();
        theTarget = t;
        configure();
    }
    
    
    protected void configure() {
        setBackground(Color.black);
        setPreferredSize(new Dimension(500,500));
        setOpaque(true);
    }
    
    
    public void setButtonPanel(TargetButtonPanel tbp) {
        final TargetButtonPanel myTBP = tbp;  //saved a copy for inner class
        final String IDLE_STATE = "Shoot";
        final String INPUT_STATE = "Done";
        
        tbp.clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("clear!");
                removeMouseListener(theMouseAdapter);
                ts = null;
                theTarget = new Target();
                repaint();
            }
        });
        tbp.clearButton.setEnabled(false);

        tbp.inputStateButton.setAction(new AbstractAction(IDLE_STATE) {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals(INPUT_STATE)) {
                    //done entering shots
                    System.out.println("Done entering shots");
                    removeMouseListener(theMouseAdapter);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    putValue(NAME, IDLE_STATE);
                    myTBP.clearButton.setEnabled(true);
                    try {
                        ts = new TargetStats(theTarget);
                        repaint();
                        StatsPanel.showStatsPanel(ts);
                    }
                    catch (Exception ex) { ex.printStackTrace(); }
                }
                else {
                    //begin entering shots
                    try { 
                        if (theTarget.getRange() == 0.0) showRangeDialog();
                        System.out.println("Begin entering Shots");
                        addMouseListener(theMouseAdapter);
                        ts = null;
                        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                        putValue(NAME, INPUT_STATE);
                        myTBP.clearButton.setEnabled(false);
                        repaint();
                    }
                    catch (Exception ex) {
                    	if (!"Cancel".equals(ex.getMessage())) {
                    		ex.printStackTrace();
                    	}
                	}
                }
            }
        });
        tbp.inputStateButton.setEnabled(false);
    }
    
    
    protected void showRangeDialog() throws Exception {
        StringBuffer message = new StringBuffer("Enter range (");
        message.append(Units.getCurrentRangeUnits());
        message.append("s)");
        double newRange = 0.0;
        while (newRange <= 0) {
            String inputValue =
                 JOptionPane.showInputDialog(message.toString());
            try {
                if (inputValue == null) throw new Exception("Cancel");
                newRange = Double.parseDouble(inputValue); }
            catch (NumberFormatException e) {
                String error = "That wasn't a valid number!\n";
                if (message.indexOf(error) == -1) message.insert(0, error); 
            }
        }   
        theTarget.setRange(newRange);
    }
        
    
    void pointClicked(int x, int y, boolean addPoint) {
        if (addPoint) {
            // add a point
            if (theTarget.pointOfAim == null) {
                theTarget.setPointOfAim(x,y);
            }
            else {
                theTarget.addHole(x,y);
            }
            int size = holeSize+1;
            repaint(new Rectangle(recenter(x, size),recenter(y, size), 
                                  size, size));
        }
        else {
            //remove point
            theTarget.removeLastHole();
            repaint();
        }
    }
    
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (theTarget != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            if (theTarget.getRange() > 0.0) {
                drawRange(g2d);
            }
            if (theTarget.pointOfAim != null) {
                drawPointOfAim(g2d);
            }
            if (theTarget.theHoles.size() > 0) {
                drawTargetHoles(g2d);
                if (ts != null) {
                    drawTargetStats(g2d);
                } //has stats
            } //has holes
        } //has target
    } //paintComponent()
    
    
    protected void drawRange(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        StringBuffer sb = new StringBuffer("Range = ");
        sb.append(theTarget.getRange());
        sb.append(" ");
        sb.append(Units.getCurrentRangeUnits());
        sb.append("s");
        g2d.drawString(sb.toString(), 10, 12);
    }

    
    protected void drawPointOfAim(Graphics2D g2d) {
        Point poa = theTarget.pointOfAim;
        g2d.setColor(Color.GREEN);
        fillCompositeCenteredCircle(poa, g2d);
        g2d.setColor(Color.BLACK);
        g2d.drawRect((int)poa.getX(), (int)poa.getY(), 0, 0);        
    }
    
    
    protected void drawTargetHoles(Graphics2D g2d) {
        Iterator<Point> it = theTarget.theHoles.iterator();
        while (it.hasNext()) {
            Point p = (Point)it.next();
            g2d.setColor(Color.WHITE);
            fillCompositeCenteredCircle(p, g2d);
            g2d.setColor(Color.BLACK);
            // draw pip on center
            g2d.drawRect((int)p.getX(), (int)p.getY(), 0, 0);
        }
    }

    
    protected void drawTargetStats(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        // group statistical center
        drawCenteredCircle(ts.getGroupCenter(), 3, g2d);
        g2d.setColor(Color.ORANGE);
        // max shot radius circle
        drawCenteredCircle(ts.getGroupCenter(),
                           ts.getMaxShotRadius(),
                           g2d);
        // average group radius circle
        drawCenteredCircle(ts.getGroupCenter(),
                           ts.getAvgGroupRadius(),
                           g2d);
        g2d.setColor(Color.CYAN);
        // extreme shot rectangle
        g2d.drawRect(ts.getMinX(), ts.getMinY(),
                     ts.getMaxX() - ts.getMinX(),
                     ts.getMaxY() - ts.getMinY());
        // average horz/vert error rectangle
        g2d.drawRect(d2i(ts.getAvgX() - ts.getAvgHorzError()),
                     d2i(ts.getAvgY() - ts.getAvgVertError()),
                     d2i(ts.getAvgHorzError() * 2),
                     d2i(ts.getAvgVertError() * 2));
        // max spread
        if (ts.getMaxSpread() > 0) {
            g2d.setColor(Color.RED);
            Point a = ts.getMaxSpreadPointA();
            Point b = ts.getMaxSpreadPointB();
            g2d.drawLine(d2i(a.getX()), d2i(a.getY()),
                        d2i(b.getX()), d2i(b.getY()));
        }
    }
    
    
    /** convert a double to an int with rounding
        */
    protected int d2i(double d) {
        return (int) Math.round(d);
    }
    
        
    /** this method draws a (50% Alpha composite) circle centered
        on the given point, highlighted with a 100% outline,
        using the current hole size
    */
    protected void fillCompositeCenteredCircle(Point p, Graphics2D g2d) {
        Composite c = g2d.getComposite();  //save it
        int x = recenter(p.getX(), holeSize);
        int y = recenter(p.getY(), holeSize);
        
        g2d.setComposite(AlphaComposite.
                         getInstance(AlphaComposite.SRC_OVER, 0.5F));
        g2d.fillOval(x, y, holeSize, holeSize);
        g2d.setComposite(c);
        g2d.drawOval(x, y, holeSize, holeSize);
    }
    
    
    /** draw a circle with the given radius centered on the given point.
     */
    protected void drawCenteredCircle(Point p, double radius, Graphics2D g2d) {
        int diameter = (int)Math.round(radius*2);
        g2d.drawOval(recenter(p.getX(), diameter), recenter(p.getY(), diameter), 
                     diameter, diameter);
    }
    
    
    /** this method adjusts a top or left coordinate of an object to be
        centered on a point, based on the width or height of the object
        @param i The point to move closer to the origin
        @param d the dimension of the object
    */
    protected int recenter(double i, int d) {
        return (int)Math.round(i - (d/2));
    }
        
    
    static int getHoleSize() {
		return holeSize;
	}


	static void setHoleSize(int holeSize) {
		TargetPanel.holeSize = holeSize;
	}


        
}
