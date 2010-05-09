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
    private String id;
    private String name;
    private Mode mode;
    private List<String> patterns = new LinkedList();
    private List<PropertyValue> properties = new LinkedList();
    private String creator;
    private Date whenCreated;
    private Date whenModified;
    private String iconURL = null;

    public MemoryDetail() {
        this("");
    }

    public MemoryDetail(String name) {
        this(name, Mode.Unknown);
    }

    public MemoryDetail(String name, Mode mode, String... initialPatterns) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.mode = mode;
        this.creator = "Me";
        this.whenCreated = this.whenModified = new Date();

        for (String p : initialPatterns) {
            addPattern(p);
        }
    }

    @Override public void setName(String newName) {
        this.name = newName;
    }

    @Override public void setMode(Mode newMode) {
        this.mode = newMode;
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

    @Override
    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String u) {
        this.iconURL = u;
    }

    public Date getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Date whenCreated) {
        this.whenCreated = whenCreated;
    }

    public Date getWhenModified() {
        return whenModified;
    }

    public void setWhenModified(Date whenModified) {
        this.whenModified = whenModified;
    }


    

}
