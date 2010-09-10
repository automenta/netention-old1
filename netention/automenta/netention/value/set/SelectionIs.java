/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.value.set;

import automenta.netention.DefiniteValue;
import automenta.netention.IndefiniteValue;
import automenta.netention.PropertyValue;
import automenta.netention.value.string.StringContains;
import automenta.netention.value.string.StringEquals;
import automenta.netention.value.string.StringNotContains;

/**
 *
 * @author seh
 */
public class SelectionIs extends PropertyValue implements DefiniteValue<String> {

	private String option;

	public SelectionIs() { }

	public SelectionIs(String option) {
		super();
		this.option = option;
	}

    public String getOption() {
        return option;
    }
    
	@Override public String getValue() {
		return option;
	}

	public void setValue(String s) {
		this.option = s;
	}

	@Override public double satisfies(IndefiniteValue i) {
		if (i.getClass().equals( SelectionEquals.class )) {
			//TODO implement string-difference-distance fall-off (ex: 0.05 * # of chars different)
			return ((SelectionEquals)i).getValue().equalsIgnoreCase(getValue()) ? 1.0 : 0.0;
		}
		return 0;
	}

}
