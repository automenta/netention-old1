package automenta.netention.value.integer;

import automenta.netention.DefiniteValue;
import automenta.netention.IndefiniteValue;
import automenta.netention.PropertyValue;



public class IntegerIs extends PropertyValue implements DefiniteValue<Integer> {

	private int value;

	public IntegerIs() { }
	
	public IntegerIs(int i) {
		this();
		
		this.value = i;		
	}
	
	@Override public Integer getValue() {
		return value;
	}

	public void setValue(int i) {
		this.value = i;		
	}

	@Override public double satisfies(IndefiniteValue i) {
		if (i.getClass().equals(IntegerEquals.class) ) {
			return (((IntegerEquals)i).getValue() == getValue().intValue()) ? 1.0 : 0.0; 
		}
		//TODO impl IntegerMoreThan and LessThan inclusive/exclusive flag 
		else if (i.getClass().equals(IntegerLessThan.class) ) {
			return (((IntegerLessThan)i).getValue() > getValue().intValue()) ? 1.0 : 0.0; 
		}
		else if (i.getClass().equals(IntegerMoreThan.class) ) {
			return (((IntegerMoreThan)i).getValue() < getValue().intValue()) ? 1.0 : 0.0; 
		}
		else if (i.getClass().equals(IntegerBetween.class) ) {
			int v = getValue().intValue();
			if (((IntegerBetween)i).isInclusive()) {
				return ( (((IntegerBetween)i).getMin() <= v) && (((IntegerBetween)i).getMax() >= v) ) ? 1.0 : 0.0;				
			}
			else {
				return ( (((IntegerBetween)i).getMin() < v) && (((IntegerBetween)i).getMax() > v) ) ? 1.0 : 0.0;
			}
		}
		
		//TODO implement Integer indefinites
//		else if (i instanceof IntegerMoreThan) {
//			
//		}
//		else if (i instanceof IntegerLessThan) {
//			
//		}
//		else if (i instanceof IntegerBetween) {
//			
//		}
		
		else {
			return 0;
		}			
	}

}
