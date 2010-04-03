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

    public SimilarTo(String source, String target, double strength) {
        super(source, target, strength);
    }
 
    @Override
    public String toString() {
        return "similar";
    }

}
