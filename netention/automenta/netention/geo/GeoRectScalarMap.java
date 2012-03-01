/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.geo;

/**
 * Generates a scalar heatmap across a rectangular "sector" of earth's surface, at various detail resolutions
 * @author SeH
 */
abstract public class GeoRectScalarMap {
    private final GeoPoint se, nw;
    
    public static class GeoPointValue extends GeoPoint {
        public final double value;

        public GeoPointValue(double lat, double lng, double value) {
            super(lat, lng);
            this.value = value;
        }
        
    }
    
    public GeoRectScalarMap(double NWlat, double NWlng, double SElat, double SElng) {
        this(new GeoPoint(NWlat, NWlng), new GeoPoint(SElat, SElng));
    }

    public GeoRectScalarMap(GeoPoint NW, GeoPoint SE) {
        this.nw = NW;
        this.se = SE;
    }
    
    
    public GeoPointValue[] get(int width, int height) {
        final int n = width * height;
        GeoPointValue[] g = new GeoPointValue[n];
        
        double dLat = (nw.lat - se.lat) / ((double)height);
        double dLng = (se.lng - nw.lng) / ((double)width);
        
        double lng = nw.lng + dLng/2.0;
        int c = 0;
        for (int w = 0; w < width; w++) {
            double lat = se.lat - dLat/2.0;
            for (int h = 0; h < height; h++) {
                lat += dLat;
                
                g[c++] = new GeoPointValue(lat, lng, value(lat, lng));                
            }
            
            lng += dLng;
        }
        
        return g;
    }
    
    abstract public double value(double lat, double lng);
}
