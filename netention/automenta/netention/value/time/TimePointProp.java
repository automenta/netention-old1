package automenta.netention.value.time;

import automenta.netention.Unit;
import automenta.netention.value.real.RealProp;


public class TimePointProp extends RealProp {

	public TimePointProp() {
		super();
	}
	
	public TimePointProp(String id, String name) {
		super(id, name, Unit.TimePoint);
	}

}
