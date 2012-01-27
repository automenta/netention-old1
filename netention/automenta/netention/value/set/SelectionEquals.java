/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.value.set;

import automenta.netention.IndefiniteValue;
import automenta.netention.PropertyValue;

/**
 *
 * @author seh
 */
public class SelectionEquals extends PropertyValue  implements IndefiniteValue  {

	private String string;

	public SelectionEquals() { super(); }

	public SelectionEquals(String string) {
		super();
		this.string = string;

	}

	public String getValue() {
		return string;
	}

	public void setValue(String text) {
		this.string = text;
	}



}
