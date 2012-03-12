/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.survive;

import automenta.netention.Detail;
import automenta.netention.Node;
import automenta.netention.Pattern;
import automenta.netention.Self;
import automenta.netention.geo.Geo;
import automenta.netention.survive.data.EDIS;
import automenta.netention.survive.data.IntentionalCommunities;
import automenta.netention.survive.data.NuclearFacilities;
import automenta.netention.value.real.RealIs;
import java.util.*;

/**
 *
 * @author SeH
 */
public class DefaultHeuristicSurvivalModel implements SurvivalModel {
    private final Self self;
    final Date now = new Date();
    
    List<Influence> influences = new LinkedList(); //order matters
    
//    List<String> influencePatterns = new LinkedList(); //order matters
//    Map<String, Affect> affects = new HashMap();
//    HashMap<String, Influence> influences = new HashMap(); //could this be done with a LinkedHashMap?

    public DefaultHeuristicSurvivalModel(Self self) {
        this.self = self;
        
        addInfluence(NuclearFacilities.NuclearFacility, Affect.Threatens);
        addInfluence(EDIS.NUCLEAR_EVENT, Affect.Threatens);
        addInfluence(EDIS.EARTHQUAKE, Affect.Threatens);
        addInfluence(EDIS.VOLCANO_ACTIVITY, Affect.Threatens);
        addInfluence(EDIS.TORNADO, Affect.Threatens);
        addInfluence(EDIS.BIOLOGICAL_HAZARD, Affect.Threatens);
        addInfluence(EDIS.CHEMICAL_HAZARD, Affect.Threatens);
        addInfluence(EDIS.EPIDEMIC_HAZARD, Affect.Threatens);
        addInfluence(EDIS.EXTREME_WEATHER, Affect.Threatens);
        addInfluence("Disaster", Affect.Threatens);
        
        addInfluence(IntentionalCommunities.IntentionalCommunity, Affect.Benefits);
        
    }

    public List<Influence> getInfluences() {
        return influences;
    }
        
    public void addInfluence(String patternID, Affect affect) {
        Pattern p = self.getPattern(patternID);
        if (p!=null) {
            Influence i = new Influence(patternID, p.getName(), p.getID(), affect);
            influences.add(i);
        }
    }
    
    
    //TODO improve by using a hashmap, and ability to return multiple influences per detail
    public Influence getInfluence(final Detail d) {
        for (final Influence i : influences) {
            if (self.isInstance(i.patternID, d.getID())) {
                return i;
            }
        }
        return null;
    }

    
    public double getScaleFactor(final Detail d) {
        if (self.isInstance(EDIS.EARTHQUAKE, d.getID())) {
            final double MAX_EARTHQUAKE_MAG = 10.0;
            final double HOUR_DECAY = 2.0;            
            
            double magnitude = d.getValue(RealIs.class, EDIS.earthquakeMagnitude).getValue();
            if (magnitude > MAX_EARTHQUAKE_MAG)
                magnitude = MAX_EARTHQUAKE_MAG;
            
            final double age = self.getTimeBetween(now, d.getWhen());
            
            double factor = (magnitude / MAX_EARTHQUAKE_MAG) * Math.exp(-age / (HOUR_DECAY * 60.0*60.0));
            return factor;
        }
        return 1.0;
    }
    public double getRadiusFactor(final Detail d) {
        return 1.0;
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
                    double scale = ii.importance;
                                        
                    if (scale > 0) {
                                                
                        scale *= getScaleFactor(d);
                        
                        final double rad = ii.radius * getRadiusFactor(d);
                        
                        
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
