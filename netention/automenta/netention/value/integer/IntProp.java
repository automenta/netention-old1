/**
 * 
 */
package automenta.netention.value.integer;

import automenta.netention.Mode;
import automenta.netention.PropertyValue;
import automenta.netention.value.real.RealProp;
import automenta.netention.value.integer.IntegerEquals;
import automenta.netention.value.integer.IntegerIs;



public class IntProp extends RealProp {

	public IntProp() {
		super();
	}
	
	public IntProp(String id, String name) {
		super(id, name);
	} 
	
	public boolean isInteger() { return true; }	

    @Override
    public PropertyValue newDefaultValue(Mode mode) {
        if (mode == Mode.Imaginary) {
            return new IntegerEquals(0);
        }
        else {
            return new IntegerIs(0);
        }
	}

}