package automenta.netention;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

abstract public class Property implements Serializable {

    private String desc;
    private String id;
    private String name;
    private int cardinalityMax = 1;
    private int cardinalityMin = 0;
    private List<String> suggestions = new LinkedList();

    public Property() {
        super();
        this.desc = "";
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Property) {
            return ((Property)obj).id.equals(id);
        }
        return super.equals(obj);
    }
    
    
    
    

    public Property(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    public String getDescription() {
        return desc;
    }

    public String getID() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        if (name == null) {
            return getID();
        }
        return name;
    }

    @Override
    public String toString() {
        return "(" + getID() + ": " + getClass().getSimpleName() + ")";
    }

    public abstract PropertyValue newDefaultValue(Mode mode);

    /** max cardinality, -1 if unlimited */
    public int getCardinalityMax() {
        return cardinalityMax;
    }

    /** min cardinality, >= 0 */
    public int getCardinalityMin() {
        return cardinalityMin;
    }

    public Property setCardinalityMax(int cardinalityMax) {
        this.cardinalityMax = cardinalityMax;
        if (this.cardinalityMax!=-1)
            if (this.cardinalityMin > cardinalityMax)
                cardinalityMin = cardinalityMax;
        return this;
    }

    public Property setCardinalityMin(int cardinalityMin) {
        this.cardinalityMin = cardinalityMin;
        if (this.cardinalityMax!=-1)
            if (this.cardinalityMax < cardinalityMin)
                cardinalityMax = cardinalityMin;
        return this;
    }

    public Property setDescription(String description) {
        this.desc = description;
        return this;
    }

    public void addSuggestion(String s) {
        if (!suggestions.contains(s))
            suggestions.add(s);
    }

    public List<String> getSuggestions() {
        return suggestions;
    }
    
    
}
