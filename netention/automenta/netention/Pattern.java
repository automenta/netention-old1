/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention;

import java.util.*;

/**
 * a pattern associates with a weighted set of properties (by ID)
 * @author seh
 */
public class Pattern extends Node  {

    private String description;
    private String iconURL;
    public final Set<String> parents = new HashSet();
    public final HashMap<String, Double> properties = new HashMap(); 
    private List<PropertyValue> defaultValues = new LinkedList();

    public Pattern() {
        super();
    }

    public Pattern(String id, String... superPatterns) {
        super(id);        
        for (String p : superPatterns) parents.add(p);
    }

    public Pattern setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }    


    public Pattern withName(String n) {
        setName(n);
        return this;
    }
    
    public Pattern setIconURL(String iconURL) {
        this.iconURL = iconURL;
        return this;
    }

    public String getIconURL() {
        return iconURL;
    }


    public Set<String> getParents() {
        return parents;
    }
    
    public void addParent(String patternID) {
        parents.add(patternID);
    }
    public boolean removeParent(String patternID) {
        return parents.remove(patternID);
    }


    public List<PropertyValue> getDefaultValues() {
        return defaultValues;
    }

    public void addDefaultValue(PropertyValue pv) {
        defaultValues.add(pv);
    }

    
}
