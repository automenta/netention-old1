/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.geo;

/**
 *
 * @author seh
 */
public class Geo {
    
    //http://stackoverflow.com/questions/837872/calculate-distance-in-meters-when-you-know-longitude-and-latitude-in-java    
    public static float meters(final float lat1, final float lng1, final float lat2, final float lng2) {
        //TODO use more accurate constants
        
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        float meterConversion = 1609f;

        return (float)(dist * meterConversion);
    }
    
}
