/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing;

import java.io.Serializable;
import org.openstreetmap.gui.jmapviewer.Coordinate;

/**
 *
 * @author seh
 */
public class SelfConfig implements Serializable {

    private String author;
    private Coordinate currentLocation;
    
    //avatar icon

    public SelfConfig() {
        author = "anonymous";
        currentLocation = new Coordinate(0,0);
    }

    public SelfConfig(String author, Coordinate currentLocation) {
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
    
    
    
    
    
}
