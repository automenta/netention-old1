/**
 * 
 */
package automenta.netention.value.real;

import automenta.netention.Mode;
import automenta.netention.Property;
import automenta.netention.PropertyValue;
import automenta.netention.Unit;
import automenta.netention.value.real.RealEquals;
import automenta.netention.value.real.RealIs;



public class RealProp extends Property {

	private Unit unit;
	
	public RealProp() {
		super();
	}
	
	public RealProp(String id, String name) {
		this(id, name, Unit.Number);
	}
	
	public RealProp(String id, String name, Unit unit) {
		super(id, name);
		this.unit = unit;	
	} 
	
	public Unit getUnit() {
		return unit;
	}
	
	public boolean isInteger() { return false; }	

    @Override public PropertyValue newDefaultValue(Mode mode) {
        if (mode == Mode.Imaginary)
            return new RealEquals(0.0);
        else
            return new RealIs(0.0);
	}

	public static Unit getUnit(String s) {
		if (s == null)
			return null;
		
		if (s.equalsIgnoreCase("mass")) return Unit.Mass;
		if (s.equalsIgnoreCase("volume")) return Unit.Volume;
		if (s.equalsIgnoreCase("area")) return Unit.Area;
		if (s.equalsIgnoreCase("distance")) return Unit.Distance;
		if (s.equalsIgnoreCase("currency")) return Unit.Currency;
		if (s.equalsIgnoreCase("number")) return Unit.Number;
		if (s.equalsIgnoreCase("speed")) return Unit.Speed;
		if (s.equalsIgnoreCase("timeduration")) return Unit.TimeDuration;
		if (s.equalsIgnoreCase("timepoint")) return Unit.TimePoint;
		return null;
	}
	
}