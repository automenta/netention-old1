package automenta.netention.value.node;

import automenta.netention.PropertyValue;


public class NodeEquals extends PropertyValue {

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
