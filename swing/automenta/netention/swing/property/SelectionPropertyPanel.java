package automenta.netention.swing.property;

import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.Value;
import automenta.netention.swing.util.TransparentFlowPanel;
import automenta.netention.value.set.SelectionEquals;
import automenta.netention.value.set.SelectionIs;
import automenta.netention.value.set.SelectionProp;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class SelectionPropertyPanel extends PropertyOptionPanel {

    final static int stringCols = 16;
    private final SelectionProp prop;

    public SelectionPropertyPanel(Self s, Detail d, SelectionProp sp, PropertyValue v, boolean editable) {
        super(s, d, v, editable);

        this.prop = sp;

        if (getMode() == Mode.Real) {

            addOption(new PropertyOption<SelectionIs>("is") {

                JComboBox rta = new JComboBox();

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(SelectionIs.class);
                }

                @Override public SelectionIs newDefaultValue() {
                    return new SelectionIs("");
                }

                @Override public SelectionIs widgetToValue(SelectionIs r) {
                    //r.setValue(rta.getText());
                    return r;
                }

                @Override public JPanel newEditPanel(SelectionIs value) {
                    setValue(value);
                    setReal();

                    JPanel p = new TransparentFlowPanel();
                    for (String s : prop.getOptions()) {
                        rta.addItem(s);
                    }
                    p.add(rta);

                    return p;
                }
            });

        } else if (getMode() == Mode.Imaginary) {
            addOption(new PropertyOption<SelectionEquals>("exactly") {

                JComboBox rta = new JComboBox();

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(SelectionEquals.class);
                }

                @Override public SelectionEquals widgetToValue(SelectionEquals r) {
                    //r.setValue( eqBox.getText() );
                    return r;
                }

                @Override public SelectionEquals newDefaultValue() {
                    return new SelectionEquals("");
                }

                @Override public JPanel newEditPanel(SelectionEquals value) {
                    setValue(value);
                    setImaginary();


                    JPanel p = new TransparentFlowPanel();
                    for (String s : prop.getOptions()) {
                        rta.addItem(s);
                    }
                    p.add(rta);

                    return p;
                }
            });

        }

        refresh();

    }
}
