package automenta.netention.value.real;

import automenta.netention.DefiniteValue;
import automenta.netention.IndefiniteValue;
import automenta.netention.PropertyValue;


public class RealIs extends PropertyValue implements DefiniteValue<Double> {

	private double value;

	public RealIs() { super(); }
	
	public RealIs(double v) {
		this();
		this.value = v;		
	}
	
	@Override public Double getValue() {
		return value;
	}

	public void setValue(Double v) {
		this.value = v;
	}

	@Override public double satisfies(IndefiniteValue i) {
		if (i.getClass().equals( RealEquals.class) ) {
			return ((RealEquals)i).getValue() == getValue().doubleValue() ? 1.0 : 0.0; 
		}			
		//TODO impl RealMoreThan and LessThan inclusive/exclusive flag 
		else if (i.getClass().equals( RealMoreThan.class) ) {
			return ((RealMoreThan)i).getValue() < getValue().doubleValue() ? 1.0 : 0.0; 				
		}
		else if (i.getClass().equals( RealLessThan.class) ) {
			return ((RealLessThan)i).getValue() > getValue().doubleValue() ? 1.0 : 0.0; 								
		}
		else if (i.getClass().equals( RealBetween.class) ) {
			RealBetween rb = (RealBetween) i;
			double v = getValue().doubleValue();
			if (rb.isInclusive()) {
				return ((rb.getMin() <= v) && (rb.getMax() >= v)) ? 1.0 : 0.0;					
			}
			else {
				//TODO confirm this is correct logic
				return ((rb.getMin() < v) && (rb.getMax() > v)) ? 1.0 : 0.0;
			}
		}			
		else {
			return 0.0;
		}			

	}
	
}
