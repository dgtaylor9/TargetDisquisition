package disquisition.target;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

/** contains a tabbed pane for displaying target stats:
  one pane is plain text, the other is a table
*/
@SuppressWarnings("serial")
public class StatsPanel extends JPanel {
    
    private TableJTextArea theTextArea = null;
    private JTable theTable = null;
    
    static void  showStatsPanel(TargetStats ts) {
        JFrame aFrame = new JFrame("Stats");
        StatsPanel sp = new StatsPanel();
        sp.setStats(ts);
        Container c = aFrame.getContentPane();
        c.setLayout(new BorderLayout());
        c.add(sp, BorderLayout.CENTER);
        aFrame.setSize(aFrame.getPreferredSize());
        aFrame.setVisible(true);
    }
    
    public StatsPanel() {
        super();
        configure();
    }
    
    
    private void configure() {
        //configure the text pane
        theTextArea = new TableJTextArea(21, 25);
        theTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        theTable = new JTable();
        JTabbedPane jtp = new JTabbedPane();
        jtp.add("Text", new JScrollPane(theTextArea));
        jtp.add("Table", new JScrollPane(theTable));
        setLayout(new BorderLayout());
        add(jtp, BorderLayout.CENTER);
    }
    
    
    public void setStats(TargetStats ts) {
        StatsTableModel stm = new StatsTableModel(ts);
        theTextArea.setModel(stm);
        theTable.setModel(stm);
        revalidate();
    }
    
    
}
