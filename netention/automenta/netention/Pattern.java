/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * a pattern associates with a weighted set of properties (by ID)
 * @author seh
 */
public class Pattern implements Serializable, Node {

    public final String id;    
    private String name;
    private String description;
    private String iconURL;
    public final Set<String> parents = new HashSet();
    public final HashMap<String, Double> properties = new HashMap(); 
    private Date when;

    public Pattern(String id, String... superPatterns) {
        super();
        this.id = id;
        this.name = id;
        this.when = new Date();
        for (String p : superPatterns) parents.add(p);
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

    public void setName(String name) {
        this.name = name;
    }

    
    @Override
    public String getName() {
        return name;
    }

    public Set<String> getParents() {
        return parents;
    }
    
    public void addParent(String patternID) {
        parents.add(patternID);
    }

    @Override
    public Date getWhen() {
        return when;
    }

    
}
