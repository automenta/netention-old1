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
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.*;
import java.util.*;
import org.apache.commons.collections15.IteratorUtils;

/**
 *
 * @author seh
 */
public class MemorySelf implements Self, Serializable {


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
    public final Map<String, Detail> details = new HashMap();
    
    private transient List<SelfListener> listeners = new LinkedList();

    /*
     * detail -> detail link graph
     */
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

    public Collection<String> getProperties() {
        return properties.keySet();
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
        Detail d = details.get(id);
        if (d != null) {
            return d;
        } else {
            for (SelfPlugin sp : plugins) {
                if (sp instanceof DetailSource) {
                    DetailSource ds = (DetailSource) sp;
                    Detail e = ds.getDetail(id);
                    if (e != null) {
                        return e;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Iterator<Node> iterateNodes() {
        List<Iterator<? extends Node>> iList = new LinkedList();
        iList.add(details.values().iterator());
        if (plugins != null) {
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

    public void removeDetail(Detail... de) {
        for (Detail d : de)
            details.remove(d);

        for (SelfListener sl : listeners)
            sl.onDetailsRemoved(de);
            
        //return details.remove(d.getID()) != null;
    }

    public void getProperties(Pattern p, Collection<String> c) {
        for (String s : p.properties.keySet()) {
            if (!c.contains(s)) {
                c.add(s);
            }
        }

        for (String pid : p.getParents()) {
            Pattern xp = getPattern(pid);
            if (xp != null) {
                getProperties(xp, c);
            }
        }

    }

    public Collection<String> getProperties(Pattern p) {
        List<String> l = new LinkedList();

        getProperties(p, l);

        return l;
    }

    public Map<Property, Double> getAvailableProperties(Detail d, String... patternID) {
        String[] lp = patternID;
        if (lp != null) {
            if (lp.length == 0) {
                lp = null;
            }
        }
        if (lp == null) {
            lp = new String[d.getPatterns().size()];
            d.getPatterns().toArray(lp);
        }

        Map<Property, Double> a = new HashMap();
        for (String pid : lp) {
            Pattern pat = patterns.get(pid);
            if (pat != null) {
                for (String propid : getProperties(pat)) {
                    Property prop = properties.get(propid);
                    if (acceptsAnotherProperty(d, propid)) {
                        //if (!containsProperty(d, prop)) {
                        Double propStrength = pat.properties.get(propid);
                        a.put(prop, propStrength);
                    }
                }

            }
        }
        return a;
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

    public static void saveJSON(MemorySelf self, String path, boolean includeDetails, boolean includeSchema) throws Exception {
        FileOutputStream fout = new FileOutputStream(path);
        PrintStream ps = new PrintStream(fout);
        String j = toJSON(self, includeDetails, includeSchema);
        ps.append(j);
        fout.close();
    }

    public static MemorySelf load(String path) throws Exception {
        FileInputStream fout = new FileInputStream(path);
        ObjectInputStream oos = new ObjectInputStream(fout);
        MemorySelf ms = (MemorySelf) oos.readObject();
        oos.close();
        return ms;
    }

    public static int getPropertyCount(Detail d, String propid) {
        int existing = 0;
        for (PropertyValue v : d.getValues()) {
            if (v.getProperty().equals(propid)) {
                existing++;
            }
        }
        return existing;
    }

    @Override
    public boolean acceptsAnotherProperty(Detail d, String propid) {
        Property p = getProperty(propid);
        if (p.getCardinalityMax() == -1) {
            return true;
        }

        int existing = getPropertyCount(d, propid);

        //TODO consider the property's cardinality properties
        if (existing >= p.getCardinalityMax()) {
            return false;
        }

        return true;
    }

    @Override
    public int moreValuesRequired(Detail d, String propid) {
        Property p = getProperty(propid);
        int existing = getPropertyCount(d, propid);
        int min = p.getCardinalityMin();
        if (existing < p.getCardinalityMin()) {
            return min - existing;
        }
        return 0;
    }

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

    public void addPlugin(SelfPlugin p) {
        if (plugins == null) {
            plugins = new LinkedList();
        }
        plugins.add(p);
    }

    @Override
    public Collection<String> getSubPatterns(final String pid) {
        List<String> s = new LinkedList();
        for (final String sp : getPatterns()) {
            final Pattern p = getPattern(sp);
            if (p.getParents().contains(pid)) {
                s.add(p.id);
            }
        }
        return s;
    }

    @Override
    public boolean isSuperPattern(final String possibleParent, final String possibleChild) {
        final Pattern c = getPattern(possibleChild);
        if (c != null) {
            for (final String a : c.getParents()) {
                if (a.equals(possibleParent)) {
                    return true;
                }
                if (isSuperPattern(possibleParent, a)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<Node> getDetailsByTime(Iterator<Node> iterateDetails, final boolean ascend) {
        List<Node> il = IteratorUtils.toList(iterateDetails);
        Collections.sort(il, new Comparator<Node>() {

            @Override
            public int compare(final Node o1, final Node o2) {
                final boolean b = o1.getWhen().before(o2.getWhen());
                if (!ascend) {
                    return b ? 1 : -1;
                } else {
                    return b ? -1 : 1;
                }
            }
        });
        return il;
    }

    public static String toJSON(Detail detail) {
        JSONSerializer serializer = new JSONSerializer();
        serializer.prettyPrint(true);
        return serializer.include("patterns", "values", "whenCreated", "whenModified").serialize(detail);
    }

    public static String toJSON(MemorySelf s, boolean includeDetails, boolean includeSchema) {
        JSONSerializer serializer = new JSONSerializer();
        serializer.prettyPrint(true);
        serializer.include("patterns", "values", "whenCreated", "whenModified");

        if (includeSchema)
            serializer.include("propertyList", "patternList");
        else
            serializer.exclude("propertyList", "patternList");
        
        if (includeDetails)
            serializer.include("detailList");
        else
            serializer.exclude("detailList");
        
        return serializer.deepSerialize(new MemorySelfData(s));
    }
    
    public static MemorySelfData loadJSON(String path) throws FileNotFoundException {
        JSONDeserializer<MemorySelfData> msd = new JSONDeserializer<MemorySelfData>();
        MemorySelfData m = msd.deserialize(new FileReader(path));
        return m;
    }
    
    public void mergeFrom(MemorySelfData md) {
        if (md.getDetailList()!=null)
            for (Detail d : md.getDetailList()) {
                mergeFromDetail(d);
            }
        
        if (md.getPropertyList()!=null)       
            for (Property r : md.getPropertyList()) {
                mergeFromProperty(r);
            }
        
        if (md.getPatternList()!=null)
            for (Pattern p : md.getPatternList()) {
                mergeFromPattern(p);
            }
    }

    private void mergeFromDetail(Detail d) {
        Detail existing = getDetail(d.getID());
        if (existing!=null) {
            existing.mergeFrom(d);
        }
        else {
            addDetail(d);
        }
    }
    
    private void mergeFromProperty(Property r) {
        //TODO impl
    }

    private void mergeFromPattern(Pattern p) {
        //TODO impl
    }

    @Override
    public void addListener(SelfListener s) {
        listeners.add(s);
    }

    @Override
    public void removeListener(SelfListener s) {
        listeners.remove(s);
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
