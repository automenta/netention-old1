/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.survive;

import automenta.netention.Detail;
import automenta.netention.Node;
import automenta.netention.Self;
import automenta.netention.geo.Geo;
import automenta.netention.survive.data.NuclearFacilities;
import java.util.Iterator;

/**
 * Heuristics for survival map
 * @author SeH
 */
public interface SurvivalModel {
    
    /**
     * 
     * @param d
     * @return if null, then 'd' has no influence
     */
    public Influence getInfluence(Detail d);
    
    /**
     * 
     * @param lat
     * @param lng
     * @param explanation possibly null, holds the accumulated explanation for the value determined at that geolocation
     * @param result 2 value array: 0=threat, 1=benefit
     */
    public void get(final double lat, final double lng, final double[] result, String explanation);    
   
}
