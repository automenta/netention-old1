/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.link;

import automenta.netention.Detail;
import automenta.netention.Link.AbstractLink;

/**
 *
 * @author seh
 */
public class SimilarTo extends AbstractLink {
    private final double strength;

    public SimilarTo(double strength) {
        super("similar", "similar(" + strength + ")");
        this.strength = strength;
    }

    public double getStrength() {
        return strength;
    }

}
