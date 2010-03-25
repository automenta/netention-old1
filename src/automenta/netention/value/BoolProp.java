/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.value;

import automenta.netention.Property;
import automenta.netention.PropertyValue;
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
    public PropertyValue newDefaultValue() {
        return new BoolIs(true);
    }

}
