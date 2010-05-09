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
public interface Link extends Node {

    public interface HasStrength {
        public double getStrength();
    }

    public static abstract class AbstractLink extends AbstractNode implements Link {

        public AbstractLink(String id) {
            super(id + UUID.randomUUID().toString());
        }

        public AbstractLink(String id, String name) {
            super(id + UUID.randomUUID().toString(), name);
        }

    }

}
