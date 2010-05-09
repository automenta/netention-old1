package automenta.netention.swing.property;

import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.Property;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import automenta.netention.swing.util.JHyperLink;
import automenta.netention.swing.util.JScaledLabel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;


abstract public class PropertyOptionPanel extends JPanel {

	private JComboBox typeSelect;

	private JPanel editPanel;
	private PropertyValue value;

	private List<PropertyOption> options = new ArrayList();

	private PropertyOption currentOption;
    private final Self self;
    private final Detail detail;
    private final String propertyID;
    private final Property property;
    private final boolean editable;
    private final JLabel typeLabel;
    private JHyperLink nameButton = null;
    

	public PropertyOptionPanel(Self s, Detail d, PropertyValue v, boolean editable) {
        //super(new FlowLayout(FlowLayout.LEFT));
        super(new GridBagLayout());

        setOpaque(false);

        this.self = s;
        this.detail = d;
        this.propertyID = v.getProperty();
        this.property = s.getProperty(propertyID);
        this.value = v;
        this.editable = editable;

        typeLabel = new JLabel("");
        
        setValue(value);

	}

    protected void addOption(PropertyOption po) {
        options.add(po);
    }

	protected void refresh() {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 1;
        gc.gridy = 1;
        gc.weightx = 0.0;

        
		//super.initPropertyPanel();
        removeAll();
		
        //add(new JLabel(property.getName()));
        if (editable) {
            nameButton = new JHyperLink(property.getName(), "");
            add(nameButton, gc);
        }
        else {
            add(new JScaledLabel(property.getName() + " ", 1.0f), gc);
        }

        gc.gridx++;

		typeSelect = new JComboBox();
		for (PropertyOption po : options) {
			typeSelect.addItem(po.getName());			
		}
        typeSelect.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
				int x = typeSelect.getSelectedIndex();
				
				PropertyOption po = options.get(x);
				setCurrentOption(po);

                if (!po.accepts(getValue()))
                    setValue( po.newDefaultValue() );

				JPanel p = po.newEditPanel(value);
				
				editPanel.removeAll();
				editPanel.add(p);

                updateUI();
			}

		});

		add(typeSelect, gc);
        gc.gridx++;

        add(typeLabel, gc);
        gc.gridx++;
		
        editPanel = new JPanel(new GridLayout(1,1));
        editPanel.setOpaque(false);

        gc.weightx = 1.0;
        gc.fill = gc.HORIZONTAL;
		add(editPanel, gc);

		valueToWidget();

        updateUI();

	}

    public Mode getMode() {
        return getDetail().getMode();
    }
    
    protected void setReal() {
        if (editable)
            getDetail().setMode(Mode.Real);
    }
    protected void setImaginary() {
        if (editable)
            getDetail().setMode(Mode.Imaginary);
    }

    protected void setValue(PropertyValue val) {
        this.value = val;
    }

	/** load */
	private void valueToWidget() {
		if (value==null)
			return;

        if (options.size() >=2) {
            typeSelect.setVisible( true );
            typeLabel.setVisible( false );
        }
        else {
            typeSelect.setVisible( false );
            typeLabel.setVisible( true );
        }

		for (int i = 0; i < options.size(); i++) {
			PropertyOption po = options.get(i);
			if (po.accepts(value)) {

				typeSelect.setSelectedIndex(i);
                typeLabel.setText(typeSelect.getSelectedItem().toString());
				
				setCurrentOption(po);
				
				JPanel p = po.newEditPanel(value);
				editPanel.removeAll();
				editPanel.add(p);
				
				return;
			}
		}

        updateUI();

        //System.out.println("unknown option for: " + value);
	}

	private void setCurrentOption(PropertyOption po) {
		this.currentOption = po;		
	}

//	protected void setValue(PropertyValue newValue) {
//		PropertyValue oldValue = this.value;
//		this.value = newValue;
//
//		this.value.setProperty(getProperty());
//
//		//TODO replace old with new value, at original index
//		if (getNode()!=null) {
//			if (oldValue!=newValue) {
//				synchronized (getNode().getProperties()) {
//					getNode().getProperties().remove(oldValue);
//					getNode().getProperties().add(newValue);
//				}
//			}
//		}
//
//	}

//	@Override
//	public void setNode(DetailData node) {
//		super.setNode(node);
//		setValue(getValue());
//	}
	
	/** save */
	public void widgetToValue() {
        if (!editable)
            return;

		if (currentOption!=null) {
			//causes value to be updated by data presently in the widgets

			setValue(currentOption.getValue());
		}
	}

	public PropertyValue getValue() {
		return value;
	}

    public Property getProperty() {
        return property;
    }

//    private void setEditable(boolean editable) {
//        this.editable = editable;
//        refresh();
//    }

    public Detail getDetail() {
        return detail;
    }

    public void setPopup(JPopupMenu popup) {
        if (nameButton!=null) {
            nameButton.addPopup(popup);
        }
    }

//    public JHyperLink getNameLabel() {
//        return nameLabel;
//    }


    
}
