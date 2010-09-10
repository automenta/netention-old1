/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.link;

import automenta.netention.Link.AbstractLink;
import automenta.netention.Link.HasStrength;

/**
 *
 * @author seh
 */
public class Satisfies extends AbstractLink implements HasStrength {
    private final double strength;

    public Satisfies(double strength) {
        super("satisfies", "satisfies(" + strength + ")");
        this.strength = strength;
    }

    @Override
    public double getStrength() {
        return strength;
    }        

    @Override
    public String toString() {
        return "satisfies";
    }


}
