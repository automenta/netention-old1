/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.Link;
import automenta.netention.Node;
import automenta.netention.graph.MyHyperassociativeMap;
import automenta.netention.graph.ValueEdge;
import automenta.netention.node.TimePoint;
import automenta.netention.plugin.finance.FinanceGrapher;
import automenta.netention.plugin.finance.PublicBusiness;
import automenta.netention.plugin.finance.PublicBusiness.BusinessPerformance;
import automenta.netention.plugin.finance.PublicBusiness.IntervalType;
import automenta.netention.swing.RunDemos.Demo;
import automenta.netention.swing.util.SwingWindow;
import automenta.spacegraph.DefaultSurface;
import automenta.spacegraph.control.FractalControl;
import automenta.spacegraph.impl.SGPanel;
import automenta.spacegraph.ui.GridRect;
import automenta.spacegraph.math.linalg.Vec4f;
import automenta.spacegraph.shape.Rect;
import automenta.spacegraph.shape.WideIcon;
import automenta.spacegraph.ui.PointerLayer;
import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.MutableBidirectedGraph;
import com.syncleus.dann.graph.MutableDirectedAdjacencyGraph;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javolution.context.ConcurrentContext;

/**
 *
 * @author seh
 */
public class RunFinanceGraph<N, E extends DirectedEdge<N>> extends DefaultSurface implements Demo {

    public static void main(String[] args) {


        SwingWindow sw = new SwingWindow(new RunFinanceGraph().newPanel(), 800, 600, true);

    }
    private MyHyperassociativeMap layout;

    public JPanel newPanel() {
        ConcurrentContext.setConcurrency(Runtime.getRuntime().availableProcessors());

        MutableBidirectedGraph<Node, ValueEdge<Node, Link>> target = new MutableDirectedAdjacencyGraph<Node, ValueEdge<Node, Link>>();

        List<PublicBusiness> businesses = new LinkedList();
        businesses.add(new PublicBusiness("GOOG"));
        businesses.add(new PublicBusiness("YHOO"));
        businesses.add(new PublicBusiness("MSFT"));
        businesses.add(new PublicBusiness("IBM"));
        //businesses.add(new PublicBusiness("AAPL"));
        //businesses.add(new PublicBusiness("INTC"));
        //businesses.add(new PublicBusiness("NVDA"));

        for (PublicBusiness pb : businesses) {
            pb.refreshLatestPerformance(IntervalType.Monthly);
        }

        FinanceGrapher.run(businesses, target, 2009, 2010, false);

        int numDimensions = 2;

        System.out.println(target.getNodes().size() + " : " + target.getEdges().size());
        
        layout = new MyHyperassociativeMap(target, numDimensions, 0.01, true);
        final GraphSpace graphCanvas = new GraphSpace(target, layout) {

            @Override
            public Rect newNodeRect(Object n) {
                if (n instanceof BusinessPerformance) {
                    BusinessPerformance bp = (BusinessPerformance) n;
                    WideIcon i = new WideIcon("" /*bp.toString()*/, getBPColor(bp), new Vec4f(Color.WHITE));
                    float s = getBPSize(bp);
                    i.getSize().set(s, s, s);
                    return i;
                } else if (n instanceof TimePoint) {
                    TimePoint ti = (TimePoint) n;
                    WideIcon i = new WideIcon("" /*+ ti.date.getTime()*/, new Vec4f(Color.BLUE), new Vec4f(Color.WHITE));
                    return i;
                } else {
                    return super.newNodeRect(n);
                }
            }

            @Override
            protected void updateRect(Object n, Rect r) {
                if (n instanceof TimePoint) {
                    r.setBackgroundColor(new Vec4f(Color.BLUE));
                    r.getSize().set(0.2F, 0.2F, 0.2F);
                } else if (n instanceof BusinessPerformance) {
                } else {
                    super.updateRect(n, r);
                }
            }

            public float getBPSize(BusinessPerformance bp) {
                float lowest = bp.getBusiness().getLow();
                float highest = bp.getBusiness().getHigh();
                float s = 0.05F + 0.25F * (bp.high - lowest) / (highest - lowest);
                return s;
            }

            public Vec4f getBPColor(BusinessPerformance bp) {
                float lowest = bp.getBusiness().getLow();
                float highest = bp.getBusiness().getHigh();
                float r = (bp.high - lowest) / (highest - lowest);
                float g = 0.1F;
                float b = 0.1F;
                Vec4f v = new Vec4f(r, g, b, 0.75f);
                return v;
            }
        };

        SGPanel j = new SGPanel(graphCanvas);

        new FractalControl(j);
        new PointerLayer(this);

        graphCanvas.add(new GridRect(6, 6));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(j, BorderLayout.CENTER);

        JButton pb = new JButton("+");
        pb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                double n = layout.getEquilibriumDistance() * 1.1;
                layout.resetLearning();
                layout.setEquilibriumDistance(n);
            }
        });
        JButton mb = new JButton("-");
        mb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                double n = layout.getEquilibriumDistance() * 0.9;
                layout.resetLearning();
                layout.setEquilibriumDistance(n);
            }
        });

        JPanel px = new JPanel(new FlowLayout());
        px.add(mb);
        px.add(pb);

        panel.add(px, BorderLayout.SOUTH);

        return panel;
    }

    @Override
    public String getName() {
        return "Hyperassociative Finance";
    }

    @Override
    public String getDescription() {
        return "..";
    }
}
