/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.survive;

/**
 *
 * @author Sue
 */
/**
 * describes the parameters of an indicator's influence
 */
public class Influence {
    public final Affect affect;
    
    public int distanceExponent = 1; //default=1; if=2, then distance is square decay
    public double radius; //in meters
    public double importance; //importance scale

    public Influence(Affect affect) {
        this.affect = affect;
        this.radius = radius;
        this.importance = importance;
    }
    
    
}
