/**
 * 
 */
package automenta.netention.swing.property;

import automenta.netention.PropertyValue;
import automenta.netention.Value;
import javax.swing.JPanel;

abstract public class PropertyOption<V extends PropertyValue> {
	
	private String name;
	private V value;

	public PropertyOption(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public V getValue() {
		return widgetToValue(value);
	}
	
	
	public void setValue(V newValue) {
		this.value = newValue;	
	}
	
	abstract public V widgetToValue(V value);
	
	/** note: does not setProperty(), caller's responsible for setProperty() otherwise it will be null */
	abstract public V newDefaultValue();

	abstract public JPanel newEditPanel(V value);

	abstract public boolean accepts(Value v);
	
	//abstract public Panel newReadPanel();		
}