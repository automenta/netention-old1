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
import automenta.netention.linker.Linker;
import automenta.netention.linker.hueristic.DefaultHeuristicLinker;
import com.syncleus.dann.graph.MutableBidirectedGraph;
import com.syncleus.dann.graph.MutableDirectedAdjacencyGraph;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.*;
import java.util.*;
import org.apache.commons.collections15.IteratorUtils;

/**
 *
 * @author seh
 */
public class MemorySelf extends Self {


    private String id, name;
    /**
     * propertyID -> properties
     */
    public final Map<String, Property> properties = new HashMap();
    /**
     * patternID -> patterns
     */
    public final Map<String, Pattern> patterns = new HashMap();
    /**
     * detailID -> details
     */
    public final Map<String, Node> details = new HashMap();
    

    /*
     * detail -> detail link graph
     */
    //transient private DirectedSparseMultigraph<Detail, Link> links = new DirectedSparseMultigraph<Detail, Link>();
    private MutableBidirectedGraph<Node, ValueEdge<Node, Link>> graph;

    public MemorySelf() {
        super();
        clearGraph();
    }

    public MemorySelf(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Collection<String> getProperties() {
        return Collections.unmodifiableCollection(properties.keySet());
    }

    public Property getProperty(String propertyID) {
        return properties.get(propertyID);
    }

    public Pattern getPattern(String patternID) {
        return patterns.get(patternID);
    }

    public Collection<String> getPatterns() {
        return Collections.unmodifiableCollection(patterns.keySet());
    }

    public MutableBidirectedGraph<Node, ValueEdge<Node, Link>> getGraph() {
        return graph;
    }

    @Override
    public Detail getDetail(String id) {
        Node d = details.get(id);
        if ((d != null) && (d instanceof Detail))
            return (Detail)d;
//        } else {
//            for (SelfPlugin sp : plugins) {
//                if (sp instanceof DetailSource) {
//                    DetailSource ds = (DetailSource) sp;
//                    Detail e = ds.getDetail(id);
//                    if (e != null) {
//                        return e;
//                    }
//                }
//            }
//        }
        return null;
    }

    @Override
    public Iterator<Node> iterateNodes() {
        return details.values().iterator();
        
//        List<Iterator<? extends Node>> iList = new LinkedList();
//        iList.add(details.values().iterator());
////        if (plugins != null) {
////            for (SelfPlugin sp : plugins) {
////                if (sp instanceof DetailSource) {
////                    DetailSource ds = (DetailSource) sp;
////                    iList.add(ds.iterateDetails());
////                }
////            }
////        }
//        return IteratorUtils.chainedIterator(iList);
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
            Pattern pat = getPattern(patid);
            if (pat != null) {
                pat.properties.put(p.getID(), 1.0);
            }
        }
        return true;
    }

    public boolean addProperty(Property p, Collection<String> patterns) {
        //TODO do not allow adding existing pattern
        properties.put(p.getID(), p);
        if (patterns != null) {
            for (String patid : patterns) {
                Pattern pat = getPattern(patid);
                if (pat != null) {
                    pat.properties.put(p.getID(), 1.0);
                }
            }
        }
        return true;
    }

    public void addDetail(final Detail... de) {
        for (Detail d : de) {
//            Detail existing = details.get(d.getID());
//            if (existing != null) {
//                return false;
//            }

            details.put(d.getID(), d);
        }
        
        for (SelfListener sl : listeners)
            sl.onDetailsAdded(de);
        
    }

    @Override
    public void removeDetail(Detail... de) {
        for (Detail d : de)
            details.remove(d);

        for (SelfListener sl : listeners)
            sl.onDetailsRemoved(de);
            
        //return details.remove(d.getID()) != null;
    }

    public boolean containsProperty(Detail d, Property p) {
        for (PropertyValue pv : d.getValues()) {
            if (p.getID().equals(pv.getProperty())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void link(Linker l) {
        MutableBidirectedGraph<Node, ValueEdge<Node, Link>> g = l.run(IteratorUtils.toList(iterateNodes()));
        for (Node n : g.getNodes()) {
            graph.add(n);
        }
        for (ValueEdge<Node, Link> e : g.getEdges()) {
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

    
//
//    public static MemorySelf load(String path) throws Exception {
//        FileInputStream fout = new FileInputStream(path);
//        ObjectInputStream oos = new ObjectInputStream(fout);
//        MemorySelf ms = (MemorySelf) oos.readObject();
//        oos.close();
//        return ms;
//    }

    @Override
    public synchronized void updateLinks(Runnable whenFinished, Detail... details) {
        clearGraph();
        link(new DefaultHeuristicLinker(this));

        if (whenFinished != null) {
            whenFinished.run();
        }
    }

    public void addProperties(Pattern p, Property... properties) {
        addProperties(p.getID(), properties);
    }

    public void addProperties(String pattern, Property... properties) {
        for (Property p : properties) {
            addProperty(p, pattern);
        }
    }


    



    public void refactorPatternParent(String fromParent, String toParent) {
        
        for (String s : getPatterns()) {
            Pattern p = getPattern(s);
            if (p.getParents().contains(fromParent)) {
                p.removeParent(fromParent);
                p.addParent(toParent);
            }            
        }
    }

    
}
