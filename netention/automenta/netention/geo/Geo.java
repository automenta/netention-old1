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
        
        final float earthRadius = 3958.75f;
        final float meterConversion = 1609f;
        
        final double dLat = Math.toRadians(lat2-lat1);
        final double dLng = Math.toRadians(lng2-lng1);
        
        final float mslt = (float)Math.sin(dLat/2.0);
        final float msgt = (float)Math.sin(dLng/2.0);
        
        final float a = mslt * mslt +
                         (float)Math.cos(Math.toRadians(lat1)) * (float)Math.cos(Math.toRadians(lat2)) *
                         msgt * msgt;
        
        final float c = 2f * (float)Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c * meterConversion;

    }

    
    
//    public static float meters2(final float lat1, final float lng1, final float lat2, final float lng2) {
//        double theta = lng1 - lng2;
//        
//        double rlat1 = Math.toRadians(lat1);
//        double rlat2 = Math.toRadians(lat2);
//        
//        double dist = Math.sin(rlat1)
//                * Math.sin(rlat2)
//                + Math.cos(rlat1)
//                * Math.cos(rlat2) * Math.cos(Math.toRadians(theta));
//        
//        dist = Math.acos(dist);
//        dist = Math.toRadians(dist);
//        dist = dist * 60.0 * 1.1515;
//        
//        return (float)(dist * 1.609344 * 1000.0);
//    }
//
//    public static void main(String[] args) {
//        float c = 40;
//        float d = 80;
//        float a = 60;
//        float b = 20;
//        System.out.println(meters(a, b, c, d) + " " + meters2(a,b,c,d));
//    }
    
}
