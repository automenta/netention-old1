package automenta.netention.swing.property;

import automenta.netention.swing.util.TransparentFlowPanel;
import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.Value;
import automenta.netention.value.string.*;
import java.awt.event.*;
import javax.swing.*;
import net.atlanticbb.tantlinger.shef.HTMLEditorPane;

public class StringPropertyPanel extends PropertyOptionPanel {

    final static int stringCols = 40;
    int richRows = 4;
    int richCols = 80;

    public StringPropertyPanel(Self s, Detail d, PropertyValue v, boolean editable) {
        super(s, d, v, editable);

        if (getMode() != Mode.Imaginary) {

            addOption(new PropertyOption<StringIs>("is") {
                private JComboBox rta;
                //private JTextArea tta; //TODO use JTextPane or HTMLEditor
                private HTMLEditorPane hta;
                
                //private SuggestBox isBox;
                //private RichTextArea rta;
                //JTextField rta = new JTextField();

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(StringIs.class);
                }

                @Override public StringIs newDefaultValue() {
                    return new StringIs("");
                }

                @Override public StringIs widgetToValue(StringIs r) {
                    if (rta!=null)
                        r.setValue(rta.getSelectedItem().toString());
                    else if (hta!=null)
                        r.setValue(hta.getText());
                    
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
                    
                    if (((StringProp)getProperty()).isRich()) {
//                        tta = new JTextArea(value.getString(), richRows, richCols);
//                        tta.setLineWrap(true);
//                        tta.setWrapStyleWord(true);
//                        p.add(tta);
                        
                        hta = new HTMLEditorPane();

                        hta.setText(value.getValue());
                        
                        p.add(hta);
                        
                    }
                    else {
                        rta = getComboBox();                    
                        rta.setSelectedItem(value.getValue());                            
                        p.add(rta);
                        addSuggestButtons(p, rta);
                    }


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
        
        final JButton remember = new JButton("->");
        remember.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getProperty().addSuggestion(jp.getSelectedItem().toString());
            }            
        });
        remember.setToolTipText("Remember " + jp.getSelectedItem());
        
        remember.setVisible(false);

        p.add(remember);
        

        jp.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (jp.getSelectedItem()!=null) {
                    if (!getProperty().getSuggestions().contains(jp.getSelectedItem())) {
                        remember.setToolTipText("Remember " + jp.getSelectedItem());
                        remember.setVisible(true);
                        return;
                    }
                }
                remember.setVisible(false);
            }
            
        });
        
        
    }
    
    public JComboBox getComboBox() {
        JComboBox jb = new JComboBox();
        jb.setEditable(isEditable());
        for (String s : getProperty().getSuggestions()) {
            jb.addItem(s);
        }
        return jb;
    }
}
