/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention;

import automenta.netention.linker.Linker;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import java.util.Collection;
import java.util.Map;

/**
 * A session managed by a certain user/agent.
 * @author seh
 */
public interface Self {

    /** all available properties */
    public Map<String, Property> getProperties();

    public Map<String, Detail> getDetails();
    
    /** all available patterns */
    public Map<String, Pattern> getPatterns();

    public DirectedSparseMultigraph<Detail, Link> getLinks();

    /** gets available properties that may be added to a detail */
    public Map<Property, Double> getAvailableProperties(Detail d, String... patternID);

    /** gets available patterns that may be added to a detail */
    public Collection<String> getAvailablePatterns(Detail d);

    public void link(Linker l);
    public void clearLinks();

    public boolean addPattern(Pattern p);

    public Property getProperty(String propertyID);
 
    public boolean acceptsAnotherProperty(Detail d, String propid);

}
