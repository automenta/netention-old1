package automenta.netention.value.real;

import automenta.netention.IndefiniteValue;
import automenta.netention.PropertyValue;


public class RealEquals extends PropertyValue  implements IndefiniteValue {

	protected double value;

	public RealEquals() {
		super();
	}

	public RealEquals(double value) {
		this();
		this.value = value;
	}


	public double getValue() {
		return value;
	}

	public void setValue(Double v) {
		this.value = v;
	}

	
}
