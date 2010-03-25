/**
 * 
 */
package automenta.netention.swing.property;

import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.Value;
import automenta.netention.value.bool.BoolEquals;
import automenta.netention.value.bool.BoolIs;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BoolPropertyPanel extends PropertyOptionPanel {

    private JLabel unitLabel;

    public static class TrueFalseCombo extends JComboBox {

        public TrueFalseCombo(boolean v) {
            super();
            addItem("True");
            addItem("False");
            setSelectedIndex(v ? 0 : 1);
        }

        public boolean isTrue() {
            return (getSelectedIndex() == 0);
        }


    }

    public BoolPropertyPanel(Self s, Detail d, PropertyValue v, boolean editable) {
        super(s, d, v, editable);
    }

    @Override protected void initOptions(List<PropertyOption> options) {
        if (getMode() == Mode.Real) {
            options.add(new PropertyOption<BoolIs>("is") {

                private TrueFalseCombo combo;

                @Override public JPanel newEditPanel(BoolIs v) {
                    setValue(v);
                    setIs();

                    JPanel p = new TransparentFlowPanel();
                    combo = new TrueFalseCombo(v.getValue());
                    p.add(combo);
                    return p;
                }

                @Override public BoolIs widgetToValue(BoolIs r) {
                    r.setValue(combo.getSelectedIndex() == 0);
                    return r;
                }

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(BoolIs.class);
                }

                @Override public BoolIs newDefaultValue() {
                    return new BoolIs(false);
                }
            });

        } else if (getMode() == Mode.Imaginary) {

            options.add(new PropertyOption<BoolEquals>("will equal") {

                private TrueFalseCombo combo;

                @Override public JPanel newEditPanel(BoolEquals v) {
                    setValue(v);
                    setWillBe();

                    JPanel p = new TransparentFlowPanel();
                    combo = new TrueFalseCombo(v.getValue());
                    p.add(combo);
                    return p;
                }

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(BoolEquals.class);
                }

                @Override public BoolEquals widgetToValue(BoolEquals r) {
                    r.setValue(combo.getSelectedIndex() == 0);
                    return r;
                }

                @Override public BoolEquals newDefaultValue() {
                    return new BoolEquals(false);
                }
            });

        }

    }
}
