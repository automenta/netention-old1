package automenta.netention.value.real;

import automenta.netention.PropertyValue;


public class RealBetween extends PropertyValue {

	private boolean inclusive;
	private double min;
	private double max;

	public RealBetween(double min, double max, boolean isInclusive) {
		super();
		
		this.min = min;
		this.max = max;
		this.inclusive = isInclusive;
		
	}

	public boolean isInclusive() {
		return inclusive;
	}

	public double getMax() {
		return max;
	}
	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}
	public void setMax(double max) {
		this.max = max;
	}
	
	
}
