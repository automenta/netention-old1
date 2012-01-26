package automenta.netention.value.node;

import automenta.netention.IndefiniteValue;
import automenta.netention.PropertyValue;

public class NodeEquals extends PropertyValue implements IndefiniteValue {

    private String node;

    public NodeEquals() {
        super();
    }

    public NodeEquals(String id) {
        super();
        this.node = id;
    }

    public String getNode() {
        return node;
    }

    public void setValue(String id) {
        this.node = id;
    }
}
