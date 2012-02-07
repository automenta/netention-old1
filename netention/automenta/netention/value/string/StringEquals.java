package automenta.netention.value.string;

import automenta.netention.IndefiniteValue;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.html.DetailHTML;

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
        
    @Override
    public String toHTML(Self s, DetailHTML h) {
        return StringIs.toHTML(getProperty(), "equals", string, s);
    }
	
	
}
