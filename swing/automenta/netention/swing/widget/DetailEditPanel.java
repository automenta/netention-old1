/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Property;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.swing.property.BoolPropertyPanel;
import automenta.netention.swing.property.IntPropertyPanel;
import automenta.netention.swing.property.RealPropertyPanel;
import automenta.netention.swing.property.StringPropertyPanel;
import automenta.netention.swing.util.JScaledLabel;
import automenta.netention.value.BoolProp;
import automenta.netention.value.IntProp;
import automenta.netention.value.RealProp;
import automenta.netention.value.StringProp;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Displays a detail and its links for view/edit
 *      Set Mode (real, imaginary)
 *      Add/Remove Patterns
 *      Add new Properties
 *      Remove existing Properties
 * @author seh
 */
public class DetailEditPanel extends JPanel {

    private final JPanel sentences;
    private Detail detail;
    private final Self self;
    private boolean editable;

    public DetailEditPanel(Self s, Detail d, boolean editable) {
        super(new GridBagLayout());

        this.self = s;

        setEditable(editable);

        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.fill = gc.HORIZONTAL;
        gc.anchor = gc.NORTHWEST;
        gc.gridx = gc.gridy = 1;

        JPanel header = new JPanel(new BorderLayout());
        header.add(new JScaledLabel(d.getName(), 2.5f), BorderLayout.NORTH);
        add(header, gc);

        gc.gridy++;
        gc.weighty = 1.0;
        gc.fill = gc.HORIZONTAL;

        sentences = new JPanel(new GridBagLayout());
        add(sentences, gc);

        setDetail(d);
    }

    protected void setDetail(Detail d) {
        this.detail = d;
        sentences.removeAll();

        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.fill = gc.HORIZONTAL;
        gc.anchor = gc.NORTHWEST;
        gc.gridx = 1;


        for (PropertyValue pv : detail.getProperties()) {
            gc.gridy++;
            JComponent nextLine = getLinePanel(pv);
            sentences.add(nextLine, gc);
            //gc.gridx+=1;
        }

        updateUI();

    }

    protected JComponent getLinePanel(PropertyValue pv) {
        Property prop = self.getProperty(pv.getProperty());
        if (prop instanceof IntProp) {
            //Int must be tested before Real because it is a subclass of it
            return new IntPropertyPanel(self, detail, pv, editable);
        } else if (prop instanceof RealProp) {
            return new RealPropertyPanel(self, detail, pv, editable);
        } else if (prop instanceof StringProp) {
            return new StringPropertyPanel(self, detail, pv, editable);
        } else if (prop instanceof BoolProp) {
            return new BoolPropertyPanel(self, detail, pv, editable);
        }
//        } else if (prop instanceof NodeProp) {
//            return new NodePropertyPanel(self, detail, pv, editable);
//        }

        return new JLabel(pv.toString());

    }

    protected void setEditable(boolean editable) {
        this.editable = editable;
    }
}
