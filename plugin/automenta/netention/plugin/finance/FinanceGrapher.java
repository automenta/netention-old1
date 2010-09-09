/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.plugin.finance;

import automenta.netention.Link;
import automenta.netention.Node;
import automenta.netention.Node.StringNode;
import automenta.netention.graph.NotifyingDirectedGraph;
import automenta.netention.graph.ValueEdge;
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


    public static void run(Collection<PublicBusiness> businesses, NotifyingDirectedGraph<Node,ValueEdge<Node, Link>> target, int yearFrom, int yearTo, boolean connectAllPointsToBusiness) {
        MultiMap<Date,BusinessPerformance> perfs = new MultiHashMap();


        for (PublicBusiness pb : businesses) {
            target.add(pb);
            
            float highestHigh = 0.0f;
            BusinessPerformance highest = null;
            
            BusinessPerformance lastBp = null;
            for (BusinessPerformance bp : pb.getPerformance()) {
                int y = bp.start.getYear() + 1900;
                if (!(y >= yearFrom) && (y <= yearTo))
                    continue;
                
                target.add(bp);
                if (bp.high > highestHigh) {
                    highestHigh = bp.high;
                    highest = bp;
                }
                
                if (lastBp != null) {
                    target.add(lastBp);
                    target.add(new ValueEdge(new Next(), lastBp, bp));
                }

                if ((connectAllPointsToBusiness) || (lastBp == null)) {
                    target.add(new ValueEdge(new In(), pb, bp));
                }

                perfs.put(bp.start, bp);
                lastBp = bp;
            }
            
            if (highest!=null) {
                StringNode high = new StringNode(pb.getID() + ".High");
                target.add(high);
                target.add(new ValueEdge(new Next(), highest, high));
            }
        }

         TimePoint lt = null;
        
        for (Date d : perfs.keySet()) {
            Collection<BusinessPerformance> lb = perfs.get(d);
            TimePoint t = new TimePoint(d);
            target.add(t);
            
            for (BusinessPerformance bp : lb) {
                target.add(new ValueEdge(new In(), t, bp));
            }
//            if (lt!=null) {
//                target.addEdge(new Next(), lt)
//            }
            lt = t;
        }

        
    }

}
