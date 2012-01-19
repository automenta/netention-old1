package automenta.netention.swing.property;

import automenta.netention.swing.util.TransparentFlowPanel;
import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.Value;
import automenta.netention.value.string.StringContains;
import automenta.netention.value.string.StringEquals;
import automenta.netention.value.string.StringIs;
import automenta.netention.value.string.StringNotContains;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StringPropertyPanel extends PropertyOptionPanel {

    final static int stringCols = 16;

    public StringPropertyPanel(Self s, Detail d, PropertyValue v, boolean editable) {
        super(s, d, v, editable);

        if (getMode() != Mode.Imaginary) {

            addOption(new PropertyOption<StringIs>("is") {

                //private SuggestBox isBox;
                //private RichTextArea rta;
                //JTextField rta = new JTextField();
                JComboBox rta = getComboBox();

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(StringIs.class);
                }

                @Override public StringIs newDefaultValue() {
                    return new StringIs("");
                }

                @Override public StringIs widgetToValue(StringIs r) {
                    r.setValue(rta.getSelectedItem().toString());
                    
                    //				if (rta !=null) {
                    //					r.setValue( rta.getText() );
                    //				}
                    //				else {
                    //					r.setValue( isBox.getText() );
                    //				}
                    return r;
                }

                @Override public JPanel newEditPanel(StringIs value) {
                    setValue(value);
                    setReal();

                    JPanel p = new TransparentFlowPanel();
                    rta.setSelectedItem(value.getString());                            
                    p.add(rta);

                    addSuggestButtons(p, rta);

                    //StringVar sv = (StringVar) getPropertyData();

                    //				if (sv.isRich()) {
                    //					rta = new RichTextArea();
                    //					rta.setText(value.getValue());
                    //					p.add(rta);
                    //				}
                    //				else {
                    //					MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
                    //
                    //					if (sv.getExampleValues()!=null) {
                    //						oracle.setDefaultSuggestionsFromText(sv.getExampleValues());
                    //					}
                    //
                    //					isBox = new SuggestBox(oracle);
                    //
                    //					isBox.setText(value.getValue());
                    //					p.add(isBox);

                    //				}

                    return p;
                }
            });

        } 
        if (getMode() != Mode.Real) {
            addOption(new PropertyOption<StringEquals>("exactly") {

                //private TextBox eqBox;
                private JTextField eqBox;

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(StringEquals.class);
                }

                @Override public StringEquals widgetToValue(StringEquals r) {
                    r.setValue(eqBox.getText());
                    return r;
                }

                @Override public StringEquals newDefaultValue() {
                    return new StringEquals("");
                }

                @Override public JPanel newEditPanel(StringEquals value) {
                    setValue(value);
                    setImaginary();

                    JPanel p = new TransparentFlowPanel();
                    eqBox = new JTextField();
                    eqBox.setColumns(stringCols);
                    eqBox.setText(value.getString());
                    p.add(eqBox);

                    //addSuggestButtons(p, rta);
                    
                    return p;
                }
            });

            addOption(new PropertyOption<StringContains>("containing") {

                private JTextField eqBox;

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(StringContains.class);
                }

                @Override public StringContains newDefaultValue() {
                    return new StringContains("");
                }

                @Override public StringContains widgetToValue(StringContains r) {
                    r.setValue(eqBox.getText());
                    return r;
                }

                @Override public JPanel newEditPanel(StringContains value) {
                    setValue(value);
                    setImaginary();

                    JPanel p = new TransparentFlowPanel();
                    eqBox = new JTextField();
                    eqBox.setColumns(stringCols);
                    eqBox.setText(value.getString());
                    p.add(eqBox);

                    //addSuggestButtons(p);
                    
                    return p;
                }
            });

            addOption(new PropertyOption<StringNotContains>("not containing") {

                private JTextField eqBox;

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(StringNotContains.class);
                }

                @Override public StringNotContains widgetToValue(StringNotContains r) {
                    r.setValue(eqBox.getText());
                    return r;
                }

                @Override public StringNotContains newDefaultValue() {
                    return new StringNotContains("");
                }

                @Override public JPanel newEditPanel(StringNotContains value) {
                    setValue(value);
                    setImaginary();

                    JPanel p = new TransparentFlowPanel();
                    eqBox = new JTextField();
                    eqBox.setColumns(stringCols);
                    eqBox.setText(value.getString());
                    p.add(eqBox);

                    //addSuggestButtons(p);
                    
                    return p;
                }
            });
        }

        refresh();

    }
    
    public void addSuggestButtons(JPanel p, final JComboBox jp) {
        JButton remember = new JButton("->");
        remember.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getProperty().addSuggestion(jp.getSelectedItem().toString());
            }            
        });
        remember.setToolTipText("Remember");
        p.add(remember);
        
        
        
    }
    
    public JComboBox getComboBox() {
        JComboBox jb = new JComboBox();
        jb.setEditable(true);
        for (String s : getProperty().getSuggestions()) {
            jb.addItem(s);
        }
        return jb;
    }
}
