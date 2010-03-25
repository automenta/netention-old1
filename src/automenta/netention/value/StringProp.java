/**
 * 
 */
package automenta.netention.value;

import automenta.netention.Property;
import automenta.netention.PropertyValue;
import automenta.netention.value.string.StringIs;
import java.util.List;



public class StringProp extends Property {


	private List<String> exampleValues;
	private boolean rich;

	public StringProp() {
		super();
	}
	
	public StringProp(String id, String name) {
		super(id, name);
	}

	public StringProp(String id, String name, List<String> exampleValues) {
		this(id, name);
		this.exampleValues = exampleValues;
	}

	@Override public PropertyValue newDefaultValue() {
		PropertyValue s = new StringIs("");
		s.setProperty(getID());
		return s;
	}

	public List<String> getExampleValues() {
		return exampleValues;
	}
	
	public boolean isRich() {
		return rich;
	}
	public void setRich(boolean rich) {
		this.rich = rich;
	}

	
	
}