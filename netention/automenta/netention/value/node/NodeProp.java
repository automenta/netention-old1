package automenta.netention.value.node;

import automenta.netention.Mode;
import automenta.netention.Property;
import automenta.netention.PropertyValue;
import automenta.netention.value.node.NodeIs;


public class NodeProp extends Property {

	private String pattern;

	public NodeProp() {
		super();
	}
	
	public NodeProp(String id, String name, String pattern) {
		super(id, name);
		
		this.pattern = pattern;		
	}

    /** the pattern restriction */
	public String getPattern() {
		return pattern;
	}
	
	@Override
	public String toString() {
		return "(" + getID() + ": " + getName() + "<" + getPattern() + ">)";
	}
	
	
    @Override public PropertyValue newDefaultValue(Mode mode) {
		NodeIs ni = new NodeIs("");
		ni.setProperty(getID());
		return ni;
	}
	
}
