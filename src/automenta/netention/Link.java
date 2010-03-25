/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention;

import java.io.Serializable;

/**
 *
 * @author seh
 */
public interface Link extends Serializable {
    /** target detailID */
    public String getSource();

    /** target detailID */
    public String getTarget();

    public double getStrength();
    
    public static abstract class AbstractLink implements Link {
        final public String source, target;
        private double strength;

        public AbstractLink(String source, String target, double strength) {
            this.source = source;
            this.target = target;
            this.strength = strength;
        }

        @Override
        public String getSource() {
            return source;
        }

        @Override
        public String getTarget() {
            return target;
        }



        @Override public double getStrength() {
            return strength;
        }    

    }

}
