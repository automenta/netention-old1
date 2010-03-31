/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.Pattern;
import automenta.netention.Self;
import automenta.netention.impl.MemoryDetail;
import automenta.netention.swing.Icons;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

/**
 * Choice of: Name, Mode, Initial Patterns
 * @author seh
 */
abstract public class NewDetailPanel extends AbstractNewPanel {
    private JTextField nameField;
    private JComboBox modeBox;
    private Map<String, JToggleButton> patternButtons;

    public NewDetailPanel(Self self) {
        super(self);        
    }

    protected void create() {
        MemoryDetail d = new MemoryDetail(getDetailName(), getDetailMode());
        for (String p : getDetailPatterns())
            d.addPattern(p);
        self.addDetail(d);
        afterCreated(d);
    }

    @Override
    protected void init(JPanel center) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = gc.gridy = 1;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.fill = gc.HORIZONTAL;

        nameField = new JTextField("Name");
        center.add(nameField, gc);

        gc.gridx++;
        gc.fill = gc.NONE;
        gc.weightx = 0.0;
        //real or imaginary
        modeBox = new JComboBox();
        modeBox.addItem("Real");
        modeBox.addItem("Imaginary");
        center.add(modeBox, gc);

        gc.fill = gc.BOTH;
        gc.gridx = 1;
        gc.gridwidth = 2;
        gc.gridy++;
        gc.weighty = 0.5;
        gc.weightx = 1.0;

        patternButtons = new HashMap();
        
        int rows = -1;
        int cols = 1;
        JPanel patterns = new JPanel(new GridLayout(rows, cols));
        //types
        for (String p : self.getPatterns().keySet()) {
            JToggleButton t = new JToggleButton(p, Icons.getObjectIcon(p));
            patternButtons.put(p, t);
            patterns.add(t);
        }
        center.add(new JScrollPane(patterns), gc);
    }


    abstract protected void afterCreated(Detail d);

    public String getDetailName() {
        return nameField.getText();
    }
    
    public Mode getDetailMode() {
        if (modeBox.getSelectedIndex() == 0)
            return Mode.Real;
        else
            return Mode.Imaginary;
    }
    
    public List<String> getDetailPatterns() {
        List<String> ll = new LinkedList();
        for (String  p : patternButtons.keySet()) {
            JToggleButton b = patternButtons.get(p);
            if (b.isSelected()) {
                ll.add(p);
            }
        }
        return ll;
    }
    

    
}
