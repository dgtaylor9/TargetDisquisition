package disquisition.target;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class PrefPane extends JFrame {
    protected JButton okButton;
    protected JLabel prefsText;

    public PrefPane() {
        super();

        this.getContentPane().setLayout(new GridLayout(4,1));
        
        //label
        prefsText = new JLabel ("TargetDisquisition Preferences...");
        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        textPanel.add(prefsText);
        this.getContentPane().add (textPanel);
		
        //Units block
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel("System units:"));
        JRadioButton m = new JRadioButton(Units.getSystemName(Units.METRIC));
        m.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Units.setMetricSystem();
            }
        });
        JRadioButton e = new JRadioButton(Units.getSystemName(Units.ENGLISH));
        e.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Units.setEnglishSystem();
            }
        });
        if (Units.getCurrentSystem() == Units.METRIC) {
            m.setSelected(true);
        }
        else {
            e.setSelected(true);
        }
        ButtonGroup bg = new ButtonGroup();
        bg.add(m);
        bg.add(e);
        p.add(m);
        p.add(e);
        p.setPreferredSize(new Dimension(250, 70));
        this.getContentPane().add(p);
        
        
        //Hole size block
        p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel("Hole size (pixels): "));
        final JTextField holeText = new JTextField(4);
        holeText.setText(Integer.toString(TargetPanel.getHoleSize()));
        p.add(holeText);
        this.getContentPane().add(p);
        
        okButton = new JButton("OK");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add (okButton);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent newEvent) {
            	try {
            		int newSize = Integer.parseInt(holeText.getText());
            		if (newSize < 1) throw new NumberFormatException();
            		TargetPanel.setHoleSize(newSize);
            	}
            	catch (NumberFormatException e) {
            		JOptionPane.showMessageDialog(holeText, "Invalid hole size.  Ignored");
            	}
                setVisible(false);
            }	
        });
        this.getContentPane().add(buttonPanel);
        
        
        //setSize(250,120);
        setSize(getPreferredSize());
        setLocation(20, 40);
    }
    
}
