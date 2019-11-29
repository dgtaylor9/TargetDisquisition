package disquisition.target;

import java.awt.BorderLayout;

import javax.swing.JFrame;
        

//Originally Created by Dave Taylor on Sun Nov 30 2003 using XCode on Mac OS X.
//Ported to pure Java on February 20, 2011, using Eclipse Helios
//This file based on "HelloWorldSwing" from the Swing tutorial

public class TargetDisquisition {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Target Disquisition");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TargetButtonPanel tbp = new TargetButtonPanel(frame);
        frame.getContentPane().add(tbp, BorderLayout.NORTH);
        TargetPanel tp = new TargetPanel();
        frame.getContentPane().add(tp, BorderLayout.CENTER);
        tp.setButtonPanel(tbp);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

