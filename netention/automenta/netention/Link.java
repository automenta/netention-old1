/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention;

import java.util.UUID;

/**
 *
 * @author seh
 */
public abstract class Link extends Node {

    public interface HasStrength {
        public double getStrength();
    }


    public Link(String id) {
        super(id + UUID.randomUUID().toString());
    }

    public Link(String id, String name) {
        super(id + UUID.randomUUID().toString(), name);
    }


}
