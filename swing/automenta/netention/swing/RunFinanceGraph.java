/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.Link;
import automenta.netention.Node;
import automenta.netention.graph.SimpleDynamicDirectedGraph;
import automenta.netention.node.TimePoint;
import automenta.netention.plugin.finance.FinanceGrapher;
import automenta.netention.plugin.finance.PublicBusiness;
import automenta.netention.plugin.finance.PublicBusiness.BusinessPerformance;
import automenta.netention.plugin.finance.PublicBusiness.IntervalType;
import automenta.netention.swing.util.SwingWindow;
import automenta.spacegraph.SGCanvas;
import automenta.spacegraph.SGPanel;
import automenta.spacegraph.gleem.linalg.Vec3f;
import automenta.spacegraph.shape.Rect;
import automenta.spacegraph.shape.WideIcon;
import com.syncleus.dann.graph.SimpleDirectedEdge;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javolution.context.ConcurrentContext;

/**
 *
 * @author seh
 */
public class RunFinanceGraph<N, E extends SimpleDirectedEdge<N>>  extends SGCanvas {


    public static void main(String[] args) {

        ConcurrentContext.setConcurrency(Runtime.getRuntime().availableProcessors());

        SimpleDynamicDirectedGraph<Node,Link> target = new SimpleDynamicDirectedGraph();

        List<PublicBusiness> businesses = new LinkedList();
        businesses.add(new PublicBusiness("GOOG"));
        businesses.add(new PublicBusiness("YHOO"));
        businesses.add(new PublicBusiness("MSFT"));
        businesses.add(new PublicBusiness("IBM"));
        businesses.add(new PublicBusiness("AAPL"));
        businesses.add(new PublicBusiness("INTC"));
        businesses.add(new PublicBusiness("NVDA"));
        
        for (PublicBusiness pb : businesses)
            pb.refreshLatestPerformance(IntervalType.Monthly);

        FinanceGrapher.run(businesses, target, 2009, 2010, false);

        System.out.println(target.getNodes().size() + " : " + target.getEdges().size() );
        
        JPanel j = new SGPanel(new GraphCanvas(target, 2) {

            @Override
            public Rect newNodeRect(Object n) {
                if (n instanceof BusinessPerformance) {
                    BusinessPerformance bp = (BusinessPerformance) n;
                    WideIcon i = new WideIcon(bp.toString(), getBPColor(bp), new Vec3f(Color.WHITE));
                    float s = getBPSize(bp);
                    i.getSize().set(s, s, s);
                    return i;
                }
                else if (n instanceof TimePoint) {
                    TimePoint ti = (TimePoint) n;
                    WideIcon i = new WideIcon("" + ti.date.getTime(), new Vec3f(Color.BLUE), new Vec3f(Color.WHITE));
                    return i;
                }
                else
                    return super.newNodeRect(n);
            }

            @Override
            protected void updateRect(Object n, Rect r) {
                if (n instanceof TimePoint) {
                    r.setBackgroundColor(new Vec3f(Color.BLUE));
                    r.getSize().set(0.2f, 0.2f, 0.2f);
                }
                else if (n instanceof BusinessPerformance) {
                    
                }
                else
                    super.updateRect(n, r);
            }

            public float getBPSize(BusinessPerformance bp) {
                float lowest = bp.getBusiness().getLow();
                float highest = bp.getBusiness().getHigh();
                float s = 0.05f + 0.25f * (bp.high - lowest) / (highest - lowest);
                return s;
            }

            public Vec3f getBPColor(BusinessPerformance bp) {
                float lowest = bp.getBusiness().getLow();
                float highest = bp.getBusiness().getHigh();
                
                float r = (bp.high - lowest) / (highest - lowest);
                float g = 0.1f;
                float b = 0.1f;
                Vec3f v = new Vec3f(r, g, b);
                return v;
            }


        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(j, BorderLayout.CENTER);
        panel.add(new JButton("X"), BorderLayout.SOUTH);

        SwingWindow sw = new SwingWindow(panel, 400, 400, true);

    }
}
