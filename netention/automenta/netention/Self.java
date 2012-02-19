package automenta.netention;

import automenta.netention.graph.ValueEdge;
import automenta.netention.impl.MemorySelfData;
import automenta.netention.linker.Linker;
import com.syncleus.dann.graph.MutableBidirectedGraph;
import flexjson.JSONSerializer;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import org.apache.commons.collections15.IteratorUtils;

/**
 * A session managed by a certain user/agent.
 * @author seh
 */
abstract public class Self {
    
    protected final transient List<SelfListener> listeners = new LinkedList();

    public static interface SelfListener {
        public void onDetailsAdded(Detail... d);
        public void onDetailsRemoved(Detail... d);
    }

    
    /** all available properties */
    abstract public Collection<String> getProperties();

    abstract public void addDetail(Detail... d);
    abstract public void removeDetail(Detail... d);
    
    abstract public Detail getDetail(String id);
    
    abstract public Iterator<Node> iterateNodes();
    
    public static interface NodeVisitor {
        /**
         * @param n
         * @return whether to continue visiting: true=yes, false=no
         */
        public boolean onNode(Node n);
        public void onFinished();
    }
    
    /** asynchronous visitor */
    abstract public void foreachNode(NodeVisitor n);

    /** all available patterns */
    abstract public Collection<String> getPatterns();
    abstract public Pattern getPattern(String toString);
    abstract public Property getProperty(String propertyID);

    abstract public MutableBidirectedGraph<Node, ValueEdge<Node, Link>> getGraph();

    
    abstract public Pattern addPattern(Pattern p);
    
    abstract public boolean removePattern(Pattern pattern);

    

    abstract public boolean addProperty(Property p, String... patterns);
    abstract public boolean addProperty(Property p, Collection<String> patterns);
    

    /** signals that certain details have changed, causing the system to update the links for them */
    abstract public void updateLinks(Runnable whenFinished, Detail... details);

    abstract public void link(Linker l);

    abstract public void clearGraph();
    
    
    abstract public String getID();
    
    abstract public String getName();
    

    public void addListener(SelfListener s) {
        listeners.add(s);
    }

    public void removeListener(SelfListener s) {
        listeners.remove(s);
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

    //gets all properties including from a pattern's hierarchy (superclass properties)
    public Collection<String> getProperties(Pattern p) {
        List<String> l = new LinkedList();

        getProperties(p, l);

        return l;
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

    
    /** gets available properties that may be added to a detail.  if patternID == null or empty, uses patterns already present in 'd' */
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
            Pattern pat = getPattern(pid);
            if (pat != null) {
                for (String propid : getProperties(pat)) {
                    Property prop = getProperty(propid);
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

    public int moreValuesRequired(Detail d, String propid) {
        Property p = getProperty(propid);
        int existing = getPropertyCount(d, propid);
        int min = p.getCardinalityMin();
        if (existing < p.getCardinalityMin()) {
            return min - existing;
        }
        return 0;
    }



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

    public  void mergeFromDetail(Detail d) {
        Detail existing = getDetail(d.getID());
        if (existing!=null) {
            existing.mergeFrom(d);
        }
        else {
            addDetail(d);
        }
    }
    
    public void mergeFromProperty(Property r) {
        //TODO impl
    }

    public void mergeFromPattern(Pattern p) {
        //TODO impl
    }
    
    public boolean isInstance(final String patternID, final String detaiID) {
        Detail d = getDetail(detaiID);
        if (d != null) {
            for (final String pid : d.getPatterns()) {
                if (pid.equals(patternID))
                    return true;
                
                if (isSuperPattern(patternID, pid))
                    return true;
            }
        }
        return false;
    }


    public static String toJSON(Detail detail) {
        JSONSerializer serializer = new JSONSerializer();
        serializer.prettyPrint(true);
        return serializer.include("patterns", "values", "whenCreated", "whenModified").serialize(detail);
    }

    public static void saveJSON(Self self, String path, boolean includeDetails, boolean includeSchema) throws Exception {
        FileOutputStream fout = new FileOutputStream(path);
        PrintStream ps = new PrintStream(fout);
        String j = toJSON(self, includeDetails, includeSchema);
        ps.append(j);
        fout.close();
    }    

    public static String toJSON(Self s, boolean includeDetails, boolean includeSchema) {
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

}
