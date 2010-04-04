/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.value.bool;

import automenta.netention.DefiniteValue;
import automenta.netention.IndefiniteValue;
import automenta.netention.PropertyValue;

/**
 *
 * @author seh
 */
public class BoolIs extends PropertyValue implements DefiniteValue<Boolean> {
    private boolean value;

    public BoolIs() {
        this(false);
    }


    public BoolIs(boolean b) {
        super();
        this.value = b;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public double satisfies(IndefiniteValue i) {
        if (i instanceof BoolEquals) {
            BoolEquals be = (BoolEquals)i;
            return (be.getValue() == getValue()) ? 1.0 : 0.0;
        }
        return 0.0;
    }

    public void setValue(boolean b) {
        this.value = b;
    }

}
