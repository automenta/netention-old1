/**
 * 
 */
package automenta.netention.value;

import automenta.netention.PropertyValue;
import automenta.netention.value.integer.IntegerIs;



public class IntProp extends RealProp {

	public IntProp() {
		super();
	}
	
	public IntProp(String id, String name) {
		super(id, name);
	} 
	
	public boolean isInteger() { return true; }	

	@Override public PropertyValue newDefaultValue() {
		IntegerIs i = new IntegerIs(0);
		i.setProperty(getID());
		return i;
	}

}