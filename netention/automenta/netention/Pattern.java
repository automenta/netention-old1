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
public class Pattern extends HashMap<String, Double> implements Serializable, Node {

    public final String id;
    private String description;
    private String iconURL;
    private final String[] extending;

    public Pattern(String id, String... extending) {
        super();
        this.id = id;
        this.extending = extending;
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

    @Override
    public String getName() {
        return id;
    }

    public String[] getExtending() {
        return extending;
    }   

}
