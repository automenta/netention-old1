/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.survive;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A collection of events provided by indicator or sensor data source for modeling the environment
 * @author seh
 */
abstract public class DataSource {
    
    public final String id;
    public final String name;
    //String description;

    public final String category;
    public final String iconURL;
    public final String unit;
    
    private Map<Need, Affect> needsAffected = new HashMap();
    
    public DataSource(String name, String category, String iconURL, String unit) {
        this(null, name, category, iconURL, unit);
    }    

    public DataSource(String id, String name, String category, String iconURL, String unit) {
        super();
        
        if (id == null)
            this.id = getClass().getName();
        else
            this.id = id;
        
        this.name = name;
        this.category = category;
        this.iconURL = iconURL;
        this.unit = unit;
    }
    

    public Map<Need, Affect> getNeedsAffected() {
        return Collections.unmodifiableMap(needsAffected);
    }
    
    abstract public double getMinMeasurement();
    abstract public double getMaxMeasurement();

//    public double getNormalizedMeasurement(Event e) {        
//        double min = getMinMeasurement();
//        double max = getMaxMeasurement();
//        
//        if (min == max)
//            return 0.5;
//        
//        return (e.getMeasurement()- min) / (max - min);
//    }
    
}
