/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.value.bool;

import automenta.netention.IndefiniteValue;
import automenta.netention.PropertyValue;

/**
 *
 * @author seh
 */
public class BoolEquals extends PropertyValue  implements IndefiniteValue  {
    private boolean value;

    public BoolEquals() {
        this(false);
    }


    public BoolEquals(boolean b) {
        super();
        this.value = b;
    }

    public boolean getValue() { return value; }

    public void setValue(boolean b) {
        this.value = b;
    }
    
}
