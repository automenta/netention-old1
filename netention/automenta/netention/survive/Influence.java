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
    
    public boolean enabled;
    
    public int distanceExponent = 1; //default=1; if=2, then distance is square decay
    public double radius; //in meters
    public double importance; //importance scale
    public final String label;
    public final String icon;
    public final String patternID;

    
    public Influence(String patternID, String label, String icon, Affect affect) {
        this.patternID = patternID;
        this.label = label;
        this.icon = icon;
        this.affect = affect;
        this.radius = 0;
        this.importance = 0;
        this.enabled = false;
    }
    
    
}
