package disquisition.target;

import javax.swing.JTextArea;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;


/** this text area displays whatever an AbstraceTableModel.toString() returns.
   It also subscribes to the model's change events, and thus, stays in sync
   with the model.
*/
@SuppressWarnings("serial")
public class TableJTextArea extends JTextArea implements TableModelListener {
    
    public TableJTextArea(int rows, int columns) {
        super(rows, columns);
    }
    

    public void setModel(AbstractTableModel atm) {
        atm.addTableModelListener(this);
        initalize(atm);
    }
    
    
    public void tableChanged(TableModelEvent e) {
        initalize((AbstractTableModel)e.getSource());
    }
    
    
    private void initalize(AbstractTableModel atm) {
        setText(atm.toString());
    }
    
    
}
