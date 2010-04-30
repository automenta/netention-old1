/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.link;

import automenta.netention.Link.AbstractLink;

/**
 *
 * @author seh
 */
public class Satisfies extends AbstractLink {
    private final double strength;

    public Satisfies(double strength) {
        super("satisfies", "satisfies(" + strength + ")");
        this.strength = strength;
    }

    public double getStrength() {
        return strength;
    }        

    @Override
    public String toString() {
        return "satisfies";
    }


}
