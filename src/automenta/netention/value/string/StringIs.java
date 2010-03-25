package automenta.netention.value.string;

import automenta.netention.DefiniteValue;
import automenta.netention.IndefiniteValue;
import automenta.netention.PropertyValue;


public class StringIs extends PropertyValue implements DefiniteValue<String> {

	private String string;

	public StringIs() { }
	
	public StringIs(String string) {
		super();
		this.string = string;
	}
	
	public String getString() {
		return string;
	}

	@Override public String getValue() {	
		return string;
	}

	public void setValue(String s) {
		this.string = s;		
	}

	@Override public double satisfies(IndefiniteValue i) {
		if (i.getClass().equals( StringEquals.class )) {
			//TODO implement string-difference-distance fall-off (ex: 0.05 * # of chars different)
			return ((StringEquals)i).getString().equalsIgnoreCase(getValue()) ? 1.0 : 0.0;
		}
		else if (i.getClass().equals( StringContains.class) ) {
			return getValue().toLowerCase().contains( ((StringContains)i).getString().toLowerCase() ) ? 1.0 : 0.0;
		}
		else if (i.getClass().equals( StringNotContains.class ) ) {
			return !(getValue().toLowerCase().contains( ((StringNotContains)i).getString().toLowerCase() )) ? 1.0 : 0.0;
		}
		// TODO impl remaining String satisfy's
		return 0;
	}
	
}
