/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.node;

import automenta.netention.Detail;
import automenta.netention.Node.AbstractNode;

/**
 *
 * @author seh
 */
public class Creator extends AbstractNode {

    public Creator(Detail d) {
        this(d.getCreator());
    }
    
    public Creator(String creatorURI) {
        super(creatorURI);
    }


}
