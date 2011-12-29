/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget;

import automenta.netention.Property;
import automenta.netention.Self;
import automenta.netention.value.bool.BoolProp;
import automenta.netention.value.integer.IntProp;
import automenta.netention.value.real.RealProp;
import automenta.netention.value.string.StringProp;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * New Property: ID, Name, Type, Description, Default Value, Cardinality
 * @author seh
 */
abstract public class NewPropertyPanel extends AbstractNewPanel {

    private JTextField idField;
    private JTextField nameField;
    private JComboBox typeCombo;

    public NewPropertyPanel(Self self) {
        super(self);
    }

    @Override protected void create() {
        Property p = null;
        String id = idField.getText();
        String name = nameField.getText();
        switch (typeCombo.getSelectedIndex()) {
            case 0:
                p = new BoolProp(id, name);
                break;
            case 1:
                p = new IntProp(id, name);
                break;
            case 2:
                p = new RealProp(id, name);
                break;
            case 3:
                p = new StringProp(id, name);
                break;
        }

        if (p!=null) {
            self.addProperty(p, null);
            afterCreated(p);
        }
    }

    @Override protected void init(JPanel center) {
        center.setLayout(new GridLayout(3, 2));

        center.add(new JLabel("ID"));
        idField = new JTextField();
        center.add(idField);

        center.add(new JLabel("Name"));
        nameField = new JTextField();
        center.add(nameField);

        center.add(new JLabel("Type"));
        typeCombo = new JComboBox();
        {
            typeCombo.addItem("Boolean");
            typeCombo.addItem("Integer");
            typeCombo.addItem("Real");
            typeCombo.addItem("String");
            //typeCombo.addItem("Node");
        }
        center.add(typeCombo);
    }

    abstract protected void afterCreated(Property p);
}
