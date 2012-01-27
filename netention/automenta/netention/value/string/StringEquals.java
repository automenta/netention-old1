package automenta.netention.value.string;

import automenta.netention.IndefiniteValue;
import automenta.netention.PropertyValue;

public class StringEquals extends PropertyValue implements IndefiniteValue {

	private String string;

	public StringEquals() { super(); }
	
	public StringEquals(String string) {
		super();
		this.string = string;
		
	}
	
	public String getString() {
		return string;
	}
	
	public void setValue(String text) {
		this.string = text;		
	}
	
	
}
