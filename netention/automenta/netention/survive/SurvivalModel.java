/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.survive;

import automenta.netention.Detail;
import automenta.netention.Node;
import automenta.netention.Self;
import automenta.netention.geo.Geo;
import automenta.netention.survive.data.NuclearFacilities;
import java.util.Iterator;

/**
 * Heuristics for survival map
 * @author SeH
 */
public interface SurvivalModel {
    
    public double getBenefit(final double lat, final double lng, String explanation);
    public double getThreat(final double lat, final double lng, String explanation);
    
    public static class DefaultHeuristicSurvive implements SurvivalModel {
        private final Self self;

        public DefaultHeuristicSurvive(Self self) {
            this.self = self;
        }
        
        
        public double getBenefit(final double lat, final double lng, String explanation) {
            return 0;
        }

        public double getThreat(final double lat, final double lng, String explanation) {
            double t = 0;

            Iterator<Node> n = self.iterateNodes();
            while (n.hasNext()) {
                final Node i = n.next();
                if (i instanceof Detail) {
                    final Detail d = (Detail)i;

                    if (d.hasPatternOr(NuclearFacilities.NuclearFacility) || self.isInstance("Disaster", d.getID())) {
                        final double[] loc = Geo.getLocation(d);
                        final double dist = Geo.meters((float)lat, (float)lng, (float)loc[0], (float)loc[1]);
                        final double rad = 100 * 1000;
                        final double scale = 0.02;
                        t += scale * 1.0 / (dist/rad);
                    }
                }

            }

            if (t > 1.0) t = 1.0;

            return t;
        }
        
    }
    
}
