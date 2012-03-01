package automenta.netention.value.geo;

import automenta.netention.geo.GeoPoint;
import automenta.netention.value.string.StringIs;


public class GeoPointIs extends StringIs {

    private double[] coords;

    public GeoPointIs() {
        super();
        this.coords = new double[] { 0, 0 };
    }

    public GeoPointIs(String string) throws java.lang.NumberFormatException {
        super(string);

        double lat = 0;
        double lng = 0;
        
        String x[] = string.trim().replace(",", " ").split(" ");
        
        if (x.length >= 2) {
            boolean c = false;
            for (int i= 0; i < x.length; i++) {
                if (x[i].length() > 0) {
                    double d = Double.parseDouble(x[i]);
                    if (!c) {
                        lat = d;
                        c = true;
                    }
                    else {
                        lng = d;
                        break;
                    }
                }
            }
        }
        
        this.coords = new double[] { lat, lng };        
    }
    
    public GeoPointIs(GeoPoint g) {
        this(g.lat, g.lng);
    }

    public GeoPointIs(double lat, double lng) {
        super(lat + "," + lng);
        this.coords = new double[] { lat, lng };
    }
    
    public GeoPointIs(double lat, double lng, String extra) {
        super(lat + "," + lng + "," + extra);
        this.coords = new double[] { lat, lng };
    }
    
    public double[] getCoordinates() {
        return coords;        
    }

    @Override
    public String toString() {
        return getValue();
    }
 
    public double getLat() { return getCoordinates()[0]; }
    public double getLng() { return getCoordinates()[1]; }
    
    
    
}
