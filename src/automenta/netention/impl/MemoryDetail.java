/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.impl;

import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.PropertyValue;
import automenta.netention.value.integer.IntegerIs;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author seh
 */
public class MemoryDetail implements Detail {
    private final String id;
    private final Mode mode;
    private final List<String> patterns = new LinkedList();
    private final List<PropertyValue> properties = new LinkedList();

    public MemoryDetail(String id) {
        this(id, Mode.Unknown);
    }

    public MemoryDetail(String id, Mode mode, String... initialPatterns) {
        this.id = id;
        this.mode = mode;

        for (String p : initialPatterns) {
            addPattern(p);
        }
    }

    @Override
    public Mode getMode() {
        return mode;
    }

    @Override
    public List<String> getPatterns() {
        return patterns;
    }

    @Override
    public List<PropertyValue> getProperties() {
        return properties;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return id;
    }

    public boolean addProperty(String propID, PropertyValue p) {
        p.setProperty(propID);
        return getProperties().add(p);
    }

    //TODO impl
//    public boolean removeProperty(String propID) {
//        return false;
//    }
    
    public boolean addPattern(String patternID) {
        return getPatterns().add(patternID);
    }
    public boolean removePattern(String patternID) {
        return getPatterns().remove(patternID);
    }

    @Override
    public String toString() {
        return id;
    }


}
