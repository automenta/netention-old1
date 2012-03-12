package automenta.netention;

import automenta.netention.action.Action;
import automenta.netention.action.DetailAction;
import automenta.netention.graph.ValueEdge;
import automenta.netention.impl.MemorySelfData;
import automenta.netention.linker.Linker;
import com.syncleus.dann.graph.MutableBidirectedGraph;
import flexjson.JSONSerializer;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections15.IteratorUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;


/**
 * A session managed by a certain user/agent.
 * @author seh
 */
abstract public class Self {
    
    protected final transient List<SelfListener> listeners = new LinkedList();
    protected final transient List<Action> actions = new LinkedList();
    protected Scheduler scheduler;

    public static long getTimeBetween(final long l, final long e) {
        return l - e;
    }

    public static long getTimeBetween(final Date later, final Date earlier) {
        return later.getTime() - earlier.getTime();
    }

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

    public Self() {
        super();
        
//        try {
//            scheduler = StdSchedulerFactory.getDefaultScheduler();
//
//            scheduler.start();
//        } catch (SchedulerException ex) {
//            Logger.getLogger(Self.class.getName()).log(Level.SEVERE, null, ex);
//            System.exit(1);
//        }
    }
    
    public void stop() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException ex) {
            Logger.getLogger(Self.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static class RunnableJob implements Job {

        public RunnableJob() {
        }

        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap data = context.getMergedJobDataMap();
            Runnable r = (Runnable)data.get(Runnable.class.toString());
            r.run();
        }

    }

    public void queue(Runnable r) {

        JobDataMap m = new JobDataMap();
        m.put(Runnable.class.toString(), r);
        JobDetail job = newJob(RunnableJob.class).usingJobData(m).build();

    
        Trigger trigger = newTrigger().startNow().build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException ex) {
            Logger.getLogger(Self.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
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
                final long x = o1.getWhen().getTime();
                final long y = o2.getWhen().getTime();
                
                if (x == y) return 0;
                if (x < y) return -1;
                /*if (x > y)*/ return 1;
                
//                final boolean b = o1.getWhen().before(o2.getWhen());
//                if (!ascend) {
//                    return b ? 1 : -1;
//                } else {
//                    return b ? -1 : 1;
//                }
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


    public boolean containsProperty(Detail d, Property p) {
        for (PropertyValue pv : d.getValues()) {
            if (p.getID().equals(pv.getProperty())) {
                return true;
            }
        }
        return false;
    }
    
    public void addAction(Action a) {
        actions.add(a);
        a.onActionEnabled(this);
    }
    
    public List<DetailAction> getDetailActions(Detail detail) {
        List<DetailAction> ll = new LinkedList();
        for (Action a : actions) {
            if (a instanceof DetailAction) {
                DetailAction d = (DetailAction)a;
                if (d.applies(detail) > 0) {
                    ll.add(d);
                }
            }
        }
        return ll;
    }
    
}
