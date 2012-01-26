package automenta.netention.value.node;

import automenta.netention.Mode;
import automenta.netention.Property;
import automenta.netention.PropertyValue;
import java.util.HashSet;
import java.util.Set;

public class NodeProp extends Property {

    private Set<String> ranges;

    public NodeProp() {
        super();
    }
    public NodeProp(String id, String name) {
        this(id, name, null);
    }

    public NodeProp(String id, String name, Set<String> ranges) {
        super(id, name);

        if (ranges != null) 
            this.ranges = ranges;
        else
            this.ranges = new HashSet(); //empty
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
        if (mode == Mode.Imaginary) {
            NodeEquals ni = new NodeEquals();
            ni.setProperty(getID());
            return ni;            
        }
        else  {
            NodeIs ni = new NodeIs();
            ni.setProperty(getID());
            return ni;            
        }
    }
}
