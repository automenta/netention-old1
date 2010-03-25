package automenta.netention.value.geo;

import automenta.netention.DefiniteValue;
import automenta.netention.IndefiniteValue;
import automenta.netention.PropertyValue;


public class GeoPointIs extends PropertyValue implements DefiniteValue /* ... */ {

	@Override public Object getValue() {
		return null;
	}

	@Override public double satisfies(IndefiniteValue i) {
		//TODO impl GeoPoint satisfy
		return 0;
	}
	
}
