/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
    public Map<String, Property> getProperties();

    public boolean addDetail(Detail d);
    public Detail getDetail(String id);
    public Iterator<Node> iterateDetails();
    //public Map<String, Detail> getDetails();

    /** all available patterns */
    public Map<String, Pattern> getPatterns();

    public MutableBidirectedGraph<Node, ValueEdge<Node, Link>> getGraph();


    /** gets available patterns that may be added to a detail */
    public Collection<String> getAvailablePatterns(Detail d);
    public Pattern addPattern(Pattern p);
    public boolean removePattern(Pattern pattern);

    /** gets available properties that may be added to a detail */
    public Map<Property, Double> getAvailableProperties(Detail d, String... patternID);
    public boolean acceptsAnotherProperty(Detail d, String propid);

    public boolean addProperty(Property p, String... patterns);
    public Property getProperty(String propertyID);

    /** signals that certain details have changed, causing the system to update the links for them */
    public void updateLinks(Runnable whenFinished, Detail... details);

    public void link(Linker l);

    public void clearGraph();

}
