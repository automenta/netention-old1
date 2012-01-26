package automenta.netention.value.node;

import automenta.netention.IndefiniteValue;
import automenta.netention.PropertyValue;


public class NodeEquals extends PropertyValue implements IndefiniteValue {

	private String id;

	public NodeEquals() {
		super();
	}

	public NodeEquals(String id) {
		super();
		this.id = id;
	}
	
	public String getNode() {
		return id;
	}
}
