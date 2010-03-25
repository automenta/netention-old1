package automenta.netention.value;

import automenta.netention.Property;
import automenta.netention.PropertyValue;
import automenta.netention.value.real.RealIs;


public class GeoPointProp extends Property {

	public GeoPointProp() {
		super();
	}
	
	public GeoPointProp(String id, String name) {
		super(id, name);
	}

	@Override
	public PropertyValue newDefaultValue() {
		//TODO GeoPointIs..
		PropertyValue pv = new RealIs(0.0);
		pv.setProperty(getID());
		return pv;
	}
	
}
