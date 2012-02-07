/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.demo;

import automenta.netention.survive.Environment;
import automenta.netention.survive.NullDataSource;

/**
 *
 * @author seh
 */
public class SeedEnvironment extends Environment {

    public SeedEnvironment() {
        super();

        addSource(new NullDataSource("nuclearFacilities", "Nuclear Facilities", "Pollution", "atom.png", "Number of Reactors"));
        addSource(new NullDataSource("radnetTotalIsotopes", "Nuclear Isotope Concentration", "Pollution", "atom.png", "Total Isotope Concentration, bQ/M^3"));
        
        addSource(new NullDataSource("earthquakesUSGS", "Earthquakes", "Natural Disasters", "quake.png", "Richter Magnitude"));

        addSource(new NullDataSource("lifeexpectancyWorldBank", "Life Expectancy", "Health", "people.png", "Years"));

        addSource(new NullDataSource("murderRateUN", "UN Murder Rate", "Crime", "gun.png", "Murders per 100,000 People"));
        
        
        addSource(new NullDataSource("mexicoHospital", "Hospitals", "Health", "people.png", "Number"));
        addSource(new NullDataSource("mexicoPharmacy", "Pharmacies", "Health", "people.png", "Number"));
        
        
        addSource(new NullDataSource("airQualityEPA", "Air Quality", "Pollution", "icon.xyz", "Units"));
        
        
    }
 
    
}
