package disquisition.target;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class CalibrationPanel extends JPanel {

	JFrame parent;
	
    public CalibrationPanel(JFrame theParent) {
        super(new BorderLayout());
        configure();
        parent = theParent;
    }
    
    
    private void configure() {
        setPreferredSize(new Dimension(500,450));
        
        JPanel p = null;  //temp panel reference
        
        //set up the controls
        p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel("width:"));
        final JTextField theWidth = new JTextField(3);
        p.add(theWidth);
        p.add(new JLabel("height:"));
        final JTextField theHeight = new JTextField(3);
        p.add(theHeight);
        JButton b = new JButton("Set");
        p.add(b);
                
        add(p, BorderLayout.PAGE_START);
        StringBuffer sb = new StringBuffer("Screen Calibration:\n");
        sb.append("Click and drag the red corner so that\n");
        sb.append("the black outline on this box measures\n");
        sb.append(Units.getCurrentStandard());
        sb.append(" ");
        sb.append(Units.getcurrentStandardUnitsAbbrev());
        sb.append(" on each side.\n");
        sb.append("Hold shift or alt to move just\n");
        sb.append("one side or the other.\n");
        sb.append("Click the \"Set\" button when finished.");
        
        JTextArea jta = new JTextArea(sb.toString(),7,20);
        final JScrollPane jsp = new JScrollPane(jta,
                                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS) {
            /** need to override getPreferredSize so that when the parent
               container is resized, the scroll pane doesn't change size
            */
            public Dimension getPreferredSize() {
                return getSize();
            }
        };
        
        Dimension d;
        double pps = Units.getPixelsPerStandard();
        if (pps > 0) {
            d = new Dimension((int)Math.round(pps), (int)Math.round(pps));
        }
        else {
            d = new Dimension(400,300);
        }
        jsp.setSize(d);
        jsp.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
        
        // add the red corner
        p = new JPanel();
        p.setBackground(Color.RED);
        jsp.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, p);
        
        // add dragging behavior
        jsp.addMouseMotionListener( new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (e.isAltDown()) x = jsp.getWidth();
                if (e.isShiftDown()) y = jsp.getHeight();
                jsp.setSize(x, y);
                theWidth.setText("" + x);
                theHeight.setText("" + y);
                jsp.revalidate();
            }
        });
        
        // put the scroll pane exactly where we want it
        p = new JPanel(null);
        p.add(jsp);
        add(p, BorderLayout.CENTER);
        Insets i = getInsets();
        d = jsp.getPreferredSize();
        jsp.setBounds(i.left + 15, i.top + 5, d.width, d.height);
        
        //display the height & width
        theWidth.setText("" + jsp.getWidth());
        theHeight.setText(""+ jsp.getHeight());
        
        // update theWidth when the rectangle is resized
        theWidth.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Dimension d = jsp.getSize();
                try {
                    int x = Integer.parseInt(e.getActionCommand());
                    jsp.setSize(x, (int)d.getHeight());
                    jsp.revalidate();
                }
                catch (NumberFormatException nfe) {
                    theWidth.setText("" + (int)jsp.getSize().getWidth());
                }
            }
        });

        // update theHeight when the rectangle is resized
        theHeight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Dimension d = jsp.getSize();
                try {
                    int y = Integer.parseInt(e.getActionCommand());
                    jsp.setSize((int)d.getWidth(), y);
                    jsp.revalidate();
                }
                catch (NumberFormatException nfe) {
                    theHeight.setText("" + (int)d.getHeight());
                }
            }
        });
        
        //calibrate when the set button is pressed
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Units.setPixelsPerStandard((int)jsp.getSize().getWidth());
                parent.dispose();
            }
        });
    }

    
}
