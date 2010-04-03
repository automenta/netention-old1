/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.impl;

import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.PropertyValue;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author seh
 */
public class MemoryDetail implements Detail {
    private final String id;
    private final String name;
    private final Mode mode;
    private final List<String> patterns = new LinkedList();
    private final List<PropertyValue> properties = new LinkedList();
    private String creator;
    private Date whenCreated = null;
    private Date whenModified = null;

    public MemoryDetail(String name) {
        this(name, Mode.Unknown);
    }

    public MemoryDetail(String name, Mode mode, String... initialPatterns) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.mode = mode;
        this.creator = "Me";

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
        return name;
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
        return getName();
    }

    @Override
    public String getCreator() {
        return creator;
    }


}
