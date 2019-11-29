package disquisition.target;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class TargetButtonPanel extends JPanel implements Observer {
    JButton clearButton = new JButton("Clear");
    JButton inputStateButton = new JButton();
    JButton calibrateButton = new JButton("Calibrate");
    JButton preferencesButton = new JButton("Preferences");
    JButton exitButton = new JButton("Exit");
    JFrame theParent;


	public TargetButtonPanel(JFrame parentFrame) {
    	super(new FlowLayout(FlowLayout.LEFT));
        add(clearButton);
        add(inputStateButton);
        add(calibrateButton);
        add(preferencesButton);
        add(exitButton);
        
        Units.addObserve(this);
        
        /*
        clearButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputStateButton.setText(IDLE_STATE);
            }
        });
        
        
        inputStateButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (inputStateButton.getText().equals(IDLE_STATE)) {
                    inputStateButton.setText(INPUT_STATE);
                }
                else {
                    inputStateButton.setText(IDLE_STATE);
                }
            }
        });
        */
        calibrateButton.addActionListener ( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame f = new JFrame("Calibrate");
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                Container c = f.getContentPane();
                c.add(new CalibrationPanel(f));
                f.setSize(c.getPreferredSize());
            	f.setPreferredSize(f.getPreferredSize());
                f.setVisible(true);
            }
        });
        
        preferencesButton.addActionListener ( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame f = new PrefPane();
                f.setVisible(true);
            }
        });
        
        theParent = parentFrame;
        exitButton.addActionListener ( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                theParent.dispose();
            }
        });
    }
    
    /** observer interface -- watch for calibration */
    public void update(Observable o, Object arg) {
    	if ( Units.isCalibrated() ) {
    		inputStateButton.setEnabled(true);
    	}
    	else { inputStateButton.setEnabled(false); }
    }
    
}
