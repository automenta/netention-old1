/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.impl;

import automenta.netention.Detail;
import automenta.netention.Link;
import automenta.netention.Node;
import automenta.netention.Pattern;
import automenta.netention.Property;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.graph.ValueEdge;
import automenta.netention.io.DetailSource;
import automenta.netention.io.SelfPlugin;
import automenta.netention.linker.Linker;
import automenta.netention.linker.hueristic.DefaultHeuristicLinker;
import com.syncleus.dann.graph.MutableBidirectedGraph;
import com.syncleus.dann.graph.MutableDirectedAdjacencyGraph;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections15.IteratorUtils;

/**
 *
 * @author seh
 */
public class MemorySelf implements Self, Serializable {

    private String id, name;

    /** propertyID -> properties */
    private Map<String, Property> properties = new HashMap();

    /** patternID -> patterns */
    private Map<String, Pattern> patterns = new HashMap();

    /** detailID -> details */
    private Map<String, Detail> details = new HashMap();

    /* detail -> detail link graph */
    //transient private DirectedSparseMultigraph<Detail, Link> links = new DirectedSparseMultigraph<Detail, Link>();
    private MutableBidirectedGraph<Node, ValueEdge<Node, Link>> graph;

    transient private List<SelfPlugin> plugins;
    
    public MemorySelf() {
        super();
        plugins = new LinkedList();
        clearGraph();
    }

    public MemorySelf(String id, String name) {
        this();
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

    public MutableBidirectedGraph<Node, ValueEdge<Node, Link>> getGraph() {
        return graph;
    }

    @Override
    public Detail getDetail(String id) {
        Detail d = details.get(id);
        if (d != null) {
            return d;
        }
        else {
            for (SelfPlugin sp : plugins) {
                if (sp instanceof DetailSource) {
                    DetailSource ds = (DetailSource) sp;
                    Detail e = ds.getDetail(id);
                    if (e!=null)
                        return e;
                }
            }
        }
        return null;
    }

    @Override public Iterator<Node> iterateDetails() {
        List<Iterator<? extends Node>> iList = new LinkedList();
        iList.add(details.values().iterator());
        if (plugins!=null) {
            for (SelfPlugin sp : plugins) {
                if (sp instanceof DetailSource) {
                    DetailSource ds = (DetailSource) sp;
                    iList.add(ds.iterateDetails());
                }
            }
        }
        return IteratorUtils.chainedIterator(iList);
    }

    public Pattern addPattern(Pattern p) {
        //TODO do not allow adding existing pattern
        patterns.put(p.getID(), p);
        return p;
    }

    @Override
    public boolean removePattern(Pattern pattern) {
        patterns.remove(pattern.getID());
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
        MutableBidirectedGraph<Node, ValueEdge<Node, Link>> g = l.run(IteratorUtils.toList(iterateDetails()));
        for (Node n : g.getNodes()) {
            graph.add(n);
        }
        for (ValueEdge<Node,Link> e : g.getEdges()) {
            graph.add(new ValueEdge(e.getValue(), e.getSourceNode(), e.getDestinationNode()));
        }
    }

    @Override
    public void clearGraph() {
        //links = new SimpleDynamicDirectedGraph<Node, Link>();
        
        //graph.clear();
        graph = new MutableDirectedAdjacencyGraph<Node, ValueEdge<Node, Link>>();
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

    @Override public void updateLinks(Runnable whenFinished, Detail... details) {
        clearGraph();
        link(new DefaultHeuristicLinker());
        
        if (whenFinished!=null)
            whenFinished.run();
    }
    
    public void addProperties(Pattern p, Property... properties) {
        addProperties(p.getID(), properties);        
    }

    public void addProperties(String pattern, Property... properties) {
        for (Property p : properties) {
            addProperty(p, pattern);
        }
    }

    public void addPlugin(SelfPlugin p) {
        if (plugins == null)
            plugins = new LinkedList();
        plugins.add(p);
    }

}
