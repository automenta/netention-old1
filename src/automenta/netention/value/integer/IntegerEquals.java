package automenta.netention.value.integer;

import automenta.netention.PropertyValue;


public class IntegerEquals extends PropertyValue {

	private int value;

	public IntegerEquals() {
		super();
	}
	
	public IntegerEquals(int i) {
		super();		
		this.value = i;
	}
	
	public void setValue(Integer i) {
		this.value = i;		
	}

	public int getValue() {
		return value;
	}
	
	

}
