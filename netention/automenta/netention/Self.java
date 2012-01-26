package automenta.netention;

import automenta.netention.graph.ValueEdge;
import automenta.netention.linker.Linker;
import com.syncleus.dann.graph.MutableBidirectedGraph;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * A session managed by a certain user/agent.
 * @author seh
 */
public interface Self {

    /** all available properties */
    public Collection<String> getProperties();

    public boolean addDetail(Detail d);
    
    public Detail getDetail(String id);
    
    public Iterator<Node> iterateNodes();

    /** all available patterns */
    public Collection<String> getPatterns();

    public MutableBidirectedGraph<Node, ValueEdge<Node, Link>> getGraph();

    
    public Pattern addPattern(Pattern p);
    
    public boolean removePattern(Pattern pattern);

    //gets all properties including from a pattern's hierarchy (superclass properties)
    public Collection<String> getProperties(Pattern p);

    /** gets available properties that may be added to a detail.  if patternID == null or empty, uses patterns already present in 'd' */
    public Map<Property, Double> getAvailableProperties(Detail d, String... patternID);
    
    public boolean acceptsAnotherProperty(Detail d, String propid);
    public int moreValuesRequired(Detail d, String propid);

    public boolean addProperty(Property p, String... patterns);
    public boolean addProperty(Property p, Collection<String> patterns);
    
    public Property getProperty(String propertyID);

    /** signals that certain details have changed, causing the system to update the links for them */
    public void updateLinks(Runnable whenFinished, Detail... details);

    public void link(Linker l);

    public void clearGraph();

    public Pattern getPattern(String toString);

    public Collection<String> getSubPatterns(String pid);

    public boolean isSuperPattern(String possibleParent, String possibleChild);


}
