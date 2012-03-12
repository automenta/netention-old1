/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.survive;

import java.util.List;

/**
 * Heuristics for survival map
 * @author SeH
 */
public interface SurvivalModel {

    /**
     * 
     * @param lat
     * @param lng
     * @param explanation possibly null, holds the accumulated explanation for the value determined at that geolocation
     * @param result 3 value array: 0=threat, 1=benefit, 2=certainty
     */
    public void get(final double lat, final double lng, final double[] result, String explanation);

    public List<Influence> getInfluences();
   
}
