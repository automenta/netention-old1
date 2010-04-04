/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention;

import java.io.Serializable;
import java.util.HashMap;

/**
 * a pattern associates with a weighted set of properties (by ID)
 * @author seh
 */
public class Pattern extends HashMap<String, Double> implements Serializable {

    public final String id;
    private String description;
    private String iconURL;

    public Pattern(String id) {
        this.id = id;
    }

    public Pattern setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }    

    public String getID() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Pattern setIconURL(String iconURL) {
        this.iconURL = iconURL;
        return this;
    }

    public String getIconURL() {
        return iconURL;
    }

    

}
