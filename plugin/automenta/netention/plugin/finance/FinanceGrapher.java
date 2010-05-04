/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.plugin.finance;

import automenta.netention.Link;
import automenta.netention.Node;
import automenta.netention.graph.SimpleDynamicDirectedGraph;
import automenta.netention.link.In;
import automenta.netention.link.Next;
import automenta.netention.node.TimePoint;
import automenta.netention.plugin.finance.PublicBusiness.BusinessPerformance;
import java.util.Collection;
import java.util.Date;
import org.apache.commons.collections15.MultiMap;
import org.apache.commons.collections15.multimap.MultiHashMap;

/**
 *
 * @author seh
 */
public class FinanceGrapher {


    public static void run(Collection<PublicBusiness> businesses, SimpleDynamicDirectedGraph<Node,Link> target, int yearFrom, int yearTo, boolean connectAllPointsToBusiness) {
        MultiMap<Date,BusinessPerformance> perfs = new MultiHashMap();


        for (PublicBusiness pb : businesses) {
            target.addNode(pb);
            
            BusinessPerformance lastBp = null;
            for (BusinessPerformance bp : pb.getPerformance()) {
                int y = bp.start.getYear() + 1900;
                if (!(y >= yearFrom) && (y <= yearTo))
                    continue;
                target.addNode(bp);
                
                if (lastBp != null)
                    target.addEdge(new Next(), lastBp, bp);

                if ((connectAllPointsToBusiness) || (lastBp == null))
                    target.addEdge(new In(), pb, bp);

                perfs.put(bp.start, bp);
                lastBp = bp;
            }
        }

         TimePoint lt = null;
        
        for (Date d : perfs.keySet()) {
            Collection<BusinessPerformance> lb = perfs.get(d);
            TimePoint t = new TimePoint(d);
            target.addNode(t);
            for (BusinessPerformance bp : lb) {
                target.addEdge(new In(), t, bp);
            }
//            if (lt!=null) {
//                target.addEdge(new Next(), lt)
//            }
            lt = t;
        }

        
    }

}
