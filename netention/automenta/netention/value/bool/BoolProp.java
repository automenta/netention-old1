/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.value.bool;

import automenta.netention.Mode;
import automenta.netention.Property;
import automenta.netention.PropertyValue;
import automenta.netention.value.bool.BoolEquals;
import automenta.netention.value.bool.BoolIs;

/**
 *
 * @author seh
 */
public class BoolProp extends Property {

    public BoolProp() {
        super();
    }
    
    public BoolProp(String id, String name) {
        super(id, name);
	}


    @Override
    public PropertyValue newDefaultValue(Mode mode) {
        if (mode == Mode.Imaginary) {
            return new BoolEquals(true);
        }
        else {
            return new BoolIs(true);
        }
    }

}
