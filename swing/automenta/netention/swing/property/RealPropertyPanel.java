/**
 * 
 */
package automenta.netention.swing.property;

import automenta.netention.swing.widget.TransparentFlowPanel;
import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.Unit;
import automenta.netention.Value;
import automenta.netention.value.RealProp;
import automenta.netention.value.real.RealBetween;
import automenta.netention.value.real.RealEquals;
import automenta.netention.value.real.RealIs;
import automenta.netention.value.real.RealLessThan;
import automenta.netention.value.real.RealMoreThan;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RealPropertyPanel extends PropertyOptionPanel {

    private JLabel unitLabel;

    final static int realColumns = 9;
    
    public RealPropertyPanel(Self s, Detail d, PropertyValue v, boolean editable) {
        super(s, d, v, editable);
    }

    @Override protected void initOptions(List<PropertyOption> options) {
        if (getMode() == Mode.Real) {

            options.add(new PropertyOption<RealIs>("is") {

                private JTextField isBox;

                @Override public JPanel newEditPanel(RealIs v) {
                    setValue(v);
                    setIs();
                    JPanel p = new TransparentFlowPanel();
                    isBox = new JTextField(Double.toString(v.getValue()));
                    isBox.setColumns(realColumns);
                    p.add(isBox);
                    return p;
                }

                @Override public RealIs widgetToValue(RealIs r) {
                    r.setValue(Double.valueOf(isBox.getText()));
                    return r;
                }

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(RealIs.class);
                }

                @Override public RealIs newDefaultValue() {
                    return new RealIs(0);
                }
            });
        } else if (getMode() == Mode.Imaginary) {

            options.add(new PropertyOption<RealEquals>("will equal") {

                private JTextField equalsBox;

                @Override public JPanel newEditPanel(RealEquals v) {
                    setValue(v);
                    setWillBe();

                    JPanel p = new TransparentFlowPanel();
                    equalsBox = new JTextField(Double.toString(v.getValue()));
                    equalsBox.setColumns(realColumns);
                    p.add(equalsBox);
                    return p;
                }

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(RealEquals.class);
                }

                @Override public RealEquals widgetToValue(RealEquals r) {
                    r.setValue(Double.valueOf(equalsBox.getText()));
                    return r;
                }

                @Override public RealEquals newDefaultValue() {
                    return new RealEquals(0);
                }
            });

            options.add(new PropertyOption<RealMoreThan>("will be greater than") {

                private JTextField moreThanBox;

                @Override public JPanel newEditPanel(RealMoreThan v) {
                    setValue(v);
                    setWillBe();

                    JPanel p = new TransparentFlowPanel();
                    moreThanBox = new JTextField(Double.toString(v.getValue()));
                    moreThanBox.setColumns(realColumns);
                    p.add(moreThanBox);
                    return p;
                }

                @Override public RealMoreThan widgetToValue(RealMoreThan r) {
                    r.setValue(Double.valueOf(moreThanBox.getText()));
                    return r;
                }

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(RealMoreThan.class);
                }

                @Override public RealMoreThan newDefaultValue() {
                    return new RealMoreThan(0);
                }
            });

            options.add(new PropertyOption<RealLessThan>("will be less than") {

                private JTextField lessThanBox;

                @Override public JPanel newEditPanel(RealLessThan v) {
                    setValue(v);
                    setWillBe();

                    JPanel p = new TransparentFlowPanel();
                    lessThanBox = new JTextField(Double.toString(v.getValue()));
                    lessThanBox.setColumns(realColumns);
                    p.add(lessThanBox);
                    return p;
                }

                @Override
                public RealLessThan widgetToValue(RealLessThan r) {
                    r.setValue(Double.valueOf(lessThanBox.getText()));
                    return r;
                }

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(RealLessThan.class);
                }

                @Override public RealLessThan newDefaultValue() {
                    return new RealLessThan(0);
                }
            });

            options.add(new PropertyOption<RealBetween>("will be between") {

                private JTextField minBox;
                private JTextField maxBox;

                //TODO add inclusive checkbox
                @Override public JPanel newEditPanel(RealBetween v) {
                    setValue(v);
                    setWillBe();

                    JPanel p = new TransparentFlowPanel();
                    minBox = new JTextField(Double.toString(v.getMin()));
                    minBox.setColumns(realColumns);
                    p.add(minBox);

                    JLabel l = new JLabel(" and ");
                    p.add(l);

                    maxBox = new JTextField(Double.toString(v.getMax()));
                    maxBox.setColumns(realColumns);
                    p.add(maxBox);

                    return p;
                }

                @Override
                public RealBetween widgetToValue(RealBetween r) {
                    r.setMin(Double.parseDouble(minBox.getText()));
                    r.setMax(Double.parseDouble(maxBox.getText()));
                    return r;
                }

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(RealBetween.class);
                }

                @Override public RealBetween newDefaultValue() {
                    return new RealBetween(0, 1, true);
                }
            });
        }

    }

    private String getUnitText(Unit unit) {
        if (unit == Unit.Distance) {
            return "meters";
        }
        if (unit == Unit.Mass) {
            return "kilograms";
        }
        if (unit == Unit.Speed) {
            return "meters/second";
        }
        if (unit == Unit.Volume) {
            return "cm^3";
        }
        if (unit == Unit.TimeDuration) {
            return "seconds";
        }
        if (unit == Unit.Currency) {
            return "dollars";
        }
        return "";
    }

    @Override
    protected void refresh() {
        super.refresh();

        unitLabel = new JLabel();

        if (getProperty() != null) {
            if (getProperty() instanceof RealProp) {
                RealProp rv = (RealProp) getProperty();
                Unit unit = rv.getUnit();
                if (unit != null) {
                    unitLabel.setText(getUnitText(unit));
                }
            }
        }

        add(unitLabel);

    }
}
