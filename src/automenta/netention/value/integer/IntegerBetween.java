package automenta.netention.value.integer;

import automenta.netention.PropertyValue;



public class IntegerBetween extends PropertyValue {

	private int min;
	private int max;
	private boolean inclusive;

	public IntegerBetween() {
		super();
	}
	
	public IntegerBetween(int min, int max, boolean inclusive) {
		super();
		this.min = min;
		this.max = max;
		this.inclusive = inclusive;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public boolean isInclusive() {
		return inclusive;
	}

	public void setInclusive(boolean inclusive) {
		this.inclusive = inclusive;
	}

	
}
