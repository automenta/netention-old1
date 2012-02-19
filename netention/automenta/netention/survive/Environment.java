/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.survive;

import java.util.*;

/**
 *
 * @author seh
 */
public class Environment {
    
    public final Set<String> categories = new HashSet();
    public final Map<String, String> categoryIcon = new HashMap();
    protected final List<DataSource> sources = new LinkedList();
    //private final LocationsCache geo = new LocationsCache();

    public final String dataPath = "/work/survive/cache";
    
    public Environment() {
        super();
        
//        geo.load(getDataFile("geocache"));
        
//        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    geo.save(getDataFile("geocache"));
//                } catch (IOException ex) {
//                    Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }            
//        }));
        
//        addSource(new DataEmpty("radnetTotalIsotopes", "Nuclear Isotope Concentration", "Pollution", "atom.png", "Total Isotope Concentration, bQ/M^3"));
//        
//        addSource(new DataEmpty("earthquakesUSGS", "Earthquakes", "Natural Disasters", "quake.png", "Richter Magnitude"));
//
//        addSource(new DataEmpty("lifeexpectancyWorldBank", "Life Expectancy", "Health", "people.png", "Years"));
//
//        addSource(new HomicideRate(geo, getDataFile("UN_Homicide.csv") ));
//        
//        addSource(new NuclearFacilities(getDataFile("IAEA_Nuclear_Facilities.csv") ));
//        
//        addSource(new AmenitiesCSV("mexicoHospital", "Hospitals (Mexico)", "people.png", "Number", "/home/me/mexico_amenities.csv", "hospital"));
//        addSource(new AmenitiesCSV("mexicoPharmacy", "Pharmacy (Mexico)", "people.png", "Number", "/home/me/mexico_amenities.csv", "pharmacy"));
        
        /*
            addSource(new DataKML("/work/survive/cache/epa_airquality.kml", "Air Quality", "Pollution", "icon.xyz", "Units"));
        */
      
    }
    
    public String getDataFile(String filename) {
        return dataPath + "/" + filename;
    }
    
    public void addSource(DataSource s) {
        categories.add(s.category);
        sources.add(s);
        if (categoryIcon.get(s.category) == null)
            categoryIcon.put(s.category, s.iconURL);
    }
    
    public List<DataSource> getSources(String category) {
        List<DataSource> l = new LinkedList();
        for (DataSource ds : sources) {
            if (ds.category.equals(category))
                l.add(ds);
        }
        return l;
    }

    public List<DataSource> getSources() {
        return sources;
    }
    
    
}
