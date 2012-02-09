/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing;

import java.io.Serializable;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author seh
 */
public class SelfSession implements Serializable {

    private String author;
    private Coordinate currentLocation;
    private transient Scheduler scheduler;
    
    //avatar icon

    public SelfSession() {
        super();
        author = "anonymous";
        currentLocation = new Coordinate(0,0);
        
 
        try {
            // Grab the Scheduler instance from the Factory 
            scheduler = StdSchedulerFactory.getDefaultScheduler();

            // and start it off
            scheduler.start();
            

        } catch (SchedulerException se) {
            se.printStackTrace();            
        }
        
    }

    public SelfSession(String author, Coordinate currentLocation) {
        this();
        this.author = author;
        this.currentLocation = currentLocation;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Coordinate getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Coordinate currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
    
    
    
}
