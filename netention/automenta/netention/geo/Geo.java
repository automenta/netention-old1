/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.geo;

import automenta.netention.Detail;
import automenta.netention.PropertyValue;
import automenta.netention.value.geo.GeoPointIs;

/**
 *
 * @author seh
 */
public class Geo {
    
    public static double[] getLocation(Detail d) {
        for (PropertyValue pv : d.getValues()) {
            if (pv instanceof GeoPointIs) {
                if (pv.getProperty().equals("currentLocation")) {
                    GeoPointIs g = (GeoPointIs)pv;
                    return g.getCoordinates();
                }
            }
        }
        return null;
    }
    
    //http://stackoverflow.com/questions/837872/calculate-distance-in-meters-when-you-know-longitude-and-latitude-in-java    
    public static float meters(final float lat1, final float lng1, final float lat2, final float lng2) {
        //TODO use more accurate constants
        
        final double earthRadius = 3958.75;
        final double dLat = Math.toRadians(lat2-lat1);
        final double dLng = Math.toRadians(lng2-lng1);
        
        double mslt = Math.sin(dLat/2);
        double msgt = Math.sin(dLng/2);
        
        final double a = mslt * mslt +
                         Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                         msgt * msgt;
        
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        final double dist = earthRadius * c;

        final float meterConversion = 1609f;

        return (float)(dist * meterConversion);
    }
    
}
