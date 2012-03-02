package automenta.netention;

import automenta.netention.value.Comment;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public class Detail implements Node {
    
    private String id;
    private String name;
    private Mode mode;
    private List<String> patterns = new LinkedList();
    private List<PropertyValue> values = new LinkedList();
    private String creator;
    private Date whenCreated;
    private Date whenModified;
    private String iconURL = null;

    public Detail() {
        this("");
    }

    public Detail(String name) {
        this(name, Mode.Unknown);
    }

    /**
     * 
     * @param name
     * @param mode
     * @param initialPatterns  the order of these should indicate the most important patterns first.  the primary icon used is the icon of the first pattern
     */
    public Detail(String name, Mode mode, String... initialPatterns) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.mode = mode;
        this.creator = "Me";
        this.whenCreated = this.whenModified = new Date();

        for (String p : initialPatterns) {
            addPattern(p);
        }
    }

    public void setID(String id) {
        this.id = id;
    }        

    public void setName(String newName) {
        this.name = newName;
    }

    public void setMode(Mode newMode) {
        this.mode = newMode;
    }
    
    public Date getWhen() {
        return whenModified;
    }
    
    public Mode getMode() {
        return mode;
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public List<PropertyValue> getValues() {
        return values;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public <X extends PropertyValue> X getValue(Class<? extends X> c, String propID) {
        for (PropertyValue pv : getValues())
            if (pv.getProperty().equals(propID))
                if (c.isAssignableFrom(pv.getClass()))
                    return (X)pv;
        return null;
    }
    
    public boolean add(String propID, PropertyValue p) {
        p.setProperty(propID);
        return getValues().add(p);
    }

    public void add(Comment c) {
        getValues().add(c);
    }
    
    //TODO impl
//    public boolean removeProperty(String propID) {
//        return false;
//    }
    
    public boolean addPattern(String patternID) {
        if (!getPatterns().contains(patternID))
            return getPatterns().add(patternID);
        return false;
    }
    public boolean removePattern(String patternID) {
        return getPatterns().remove(patternID);
    }

    public String toString() {
        return getName();
    }

    public String getCreator() {
        return creator;
    }

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


    public void removeAllValues(String propID) {
        List<PropertyValue> toRemove = new LinkedList();
        
        for (PropertyValue v : values) {
            if (v.getProperty().equals(propID))
                toRemove.add(v);
        }
        
        values.removeAll(toRemove);
    }

    public void mergeFrom(Detail d) {
        for (String p : d.getPatterns())
            addPattern(p);
        
        for (PropertyValue v : d.getValues()) {
            add(v.getProperty(), v);
        }
    }

    public boolean hasPatternOr(final String... a) {
        for (final String p : getPatterns()) {
            for (final String x : a) {
                if (x.equals(p))
                    return true;
            }
        }
        return false;
    }

    

}
