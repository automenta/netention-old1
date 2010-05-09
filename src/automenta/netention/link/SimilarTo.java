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
public class SimilarTo extends AbstractLink implements HasStrength {
    private final double strength;

    public SimilarTo(double strength) {
        super("similar", "similar(" + strength + ")");
        this.strength = strength;
    }

    @Override
    public double getStrength() {
        return strength;
    }

}
