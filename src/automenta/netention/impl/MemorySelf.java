/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.impl;

import automenta.netention.Detail;
import automenta.netention.Link;
import automenta.netention.Pattern;
import automenta.netention.Property;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.linker.Linker;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author seh
 */
public class MemorySelf implements Self, Serializable {

    public final String id, name;
    /** propertyID -> properties */
    public final Map<String, Property> properties = new HashMap();
    /** detailID -> details */
    public final LinkedHashMap<String, Detail> details = new LinkedHashMap();
    /** patternID -> patterns */
    public final Map<String, Pattern> patterns = new HashMap();
    public DirectedSparseMultigraph<Detail, Link> links = new DirectedSparseMultigraph<Detail, Link>();

    public MemorySelf(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    public Property getProperty(String propertyID) {
        return properties.get(propertyID);
    }

    public Map<String, Pattern> getPatterns() {
        return patterns;
    }

    public DirectedSparseMultigraph<Detail, Link> getLinks() {
        return links;
    }

    public Map<String, Detail> getDetails() {
        return details;
    }

    public boolean addPattern(Pattern p) {
        //TODO do not allow adding existing pattern
        patterns.put(p.getID(), p);
        return true;
    }

    
    public boolean addProperty(Property p, String... patterns) {
        //TODO do not allow adding existing pattern
        properties.put(p.getID(), p);
        for (String patid : patterns) {
            Pattern pat = getPatterns().get(patid);
            if (pat!=null) {
                pat.put(p.getID(), 1.0);
            }
        }
        return true;
    }

    public boolean addDetail(Detail d) {
        Detail existing = details.get(d.getID());
        if (existing != null) {
            return false;
        }

        details.put(d.getID(), d);
        return true;
    }

    public boolean removeDetail(Detail d) {
        return details.remove(d.getID()) != null;
    }

    public Collection<String> getAvailablePatterns(Detail d) {
        //TODO use dependency information to find all available patterns applicable for d
        //for now, just use all patterns minus existing patterns in 'd'

        Set<String> patterns = new HashSet<String>(getPatterns().keySet());
        for (String p : d.getPatterns()) {
            patterns.remove(p);
        }        

        return patterns;
    }

    public Map<Property, Double> getAvailableProperties(Detail d, String... patternID) {
        Map<Property, Double> a = new HashMap();
        for (String pid : patternID) {
            Pattern pat = patterns.get(pid);
            if (pat != null) {
                for (String propid : pat.keySet()) {
                    Property prop = properties.get(propid);
                    if (!containsProperty(d, prop)) {
                        Double propStrength = pat.get(propid);
                        a.put(prop, propStrength);
                    }
                }

            }
        }
        return a;
    }

    public boolean containsProperty(Detail d, Property p) {
        for (PropertyValue pv : d.getProperties()) {
            if (p.getID().equals(pv.getProperty())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void link(Linker l) {
        DirectedGraph<Detail, Link> g = l.run(getDetails().values());
        for (Detail d : g.getVertices()) {
            links.addVertex(d);
        }
        for (Link e : g.getEdges()) {
            Pair<Detail> ep = g.getEndpoints(e);
            links.addEdge(e, ep.getFirst(), ep.getSecond());
        }
    }

    @Override
    public void clearLinks() {
        links = new DirectedSparseMultigraph<Detail, Link>();
    }

    public void save(String path) throws Exception {
        FileOutputStream fout = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(this);
        oos.close();
    }

    public static MemorySelf load(String path) throws Exception {
        FileInputStream fout = new FileInputStream(path);
        ObjectInputStream oos = new ObjectInputStream(fout);
        MemorySelf ms = (MemorySelf)oos.readObject();
        oos.close();
        return ms;
    }

    @Override
    public boolean acceptsAnotherProperty(Detail d, String propid) {
        int existing = 0;
        for (PropertyValue v : d.getProperties()) {
            if (v.getProperty().equals(propid)) {
                existing++;
            }
        }

        //TODO consider the property's cardinality properties
        if (existing == 1)
            return false;

        return true;
    }

}
