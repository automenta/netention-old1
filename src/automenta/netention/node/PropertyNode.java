/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.node;

import automenta.netention.Node.AbstractNode;
import automenta.netention.PropertyValue;

/**
 * wraps a property value as a node
 */
public class PropertyNode extends AbstractNode {

    public PropertyNode(PropertyValue pv) {
        super(pv.getProperty());
    }


}
