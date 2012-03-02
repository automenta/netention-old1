/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.survive;

import automenta.netention.Detail;
import automenta.netention.Node;
import automenta.netention.Self;
import automenta.netention.geo.Geo;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author SeH
 */
public class DefaultHeuristicSurvivalModel implements SurvivalModel {
    private final Self self;
    List<String> influencePatterns = new LinkedList(); //order matters
    HashMap<String, Influence> influences = new HashMap(); //could this be done with a LinkedHashMap?

    public DefaultHeuristicSurvivalModel(Self self) {
        this.self = self;
    }
    
    public void addInfluence(String pattern, Influence i) {
        influencePatterns.add(pattern);
        influences.put(pattern, i);
    }
    
    @Override
    public Influence getInfluence(final Detail d) {
        for (final String p : influencePatterns) {
            if (self.isInstance(p, d.getID())) {
                return influences.get(p);
            }
        }
        return null;
    }

    
    
     @Override
     public void get(final double lat, final double lng, final double[] result, String explanation) {
        double t = 0, b = 0;
        Iterator<Node> n = self.iterateNodes();
        while (n.hasNext()) {
            final Node i = n.next();
            if (i instanceof Detail) {
                final Detail d = (Detail) i;
                
                Influence ii = getInfluence(d);
                if (ii!=null) {
                    final double scale = ii.importance;
                    if (scale > 0) {
                        final double rad = ii.radius;
                        final double[] loc = Geo.getLocation(d);
                        final double dist = Geo.meters((float) lat, (float) lng, (float) loc[0], (float) loc[1]);
                        
                        final double distDenominator = (dist <= rad) ? 1.0 : (1.0 + dist-rad);
                        
                        final double v = scale / distDenominator;
                        
                        if (ii.affect == Affect.Threatens)
                            t += v;
                        else if (ii.affect == Affect.Benefits)
                            b += v;
                    }
                }
            }
        }
        
        result[0] = t;
        result[1] = b;
    }
    
}
