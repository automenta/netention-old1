package automenta.netention.value.geo;

import automenta.netention.Detail;
import automenta.netention.PropertyValue;
import automenta.netention.value.string.StringIs;


public class GeoPointIs extends StringIs {

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

    public GeoPointIs() {
    }

    public GeoPointIs(String string) {
        super(string);
    }

    public GeoPointIs(double lat, double lng) {
        this(lat + "," + lng);
    }
    public GeoPointIs(double lat, double lng, String extra) {
        this(lat + "," + lng + "," + extra);
    }
    
    public double[] getCoordinates() {
        double lat = 0;
        double lng = 0;
        
        String x[] = getValue().split(",| ");
        if (x.length >= 2) {
            lat = Double.parseDouble(x[0]);
            lng = Double.parseDouble(x[1]);
        }
        
        return new double[] { lat, lng };
    }

    @Override
    public String toString() {
        return getValue();
    }
    
    
    
}
