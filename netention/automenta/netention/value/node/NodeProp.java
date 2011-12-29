package automenta.netention.value.node;

import automenta.netention.Mode;
import automenta.netention.Property;
import automenta.netention.PropertyValue;
import java.util.Set;

public class NodeProp extends Property {

    private Set<String> ranges;

    public NodeProp() {
        super();
    }

    public NodeProp(String id, String name, Set<String> ranges) {
        super(id, name);

        this.ranges = ranges;
    }

    public Set<String> getRanges() {
        return ranges;
    }

    @Override
    public String toString() {
        return "(" + getID() + ": " + getName() + "<" + getRanges() + ">)";
    }

    @Override
    public PropertyValue newDefaultValue(Mode mode) {
        NodeIs ni = new NodeIs("");
        ni.setProperty(getID());
        return ni;
    }
}
