/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.Link;
import automenta.netention.Node;
import automenta.netention.Node.StringNode;
import automenta.netention.graph.MyHyperassociativeMap;
import automenta.netention.graph.ValueEdge;
import automenta.netention.link.Next;
import automenta.netention.node.TimePoint;
import automenta.netention.plugin.finance.PublicBusiness.BusinessPerformance;
import automenta.netention.swing.RunDemos.Demo;
import automenta.netention.swing.util.SwingWindow;
import automenta.spacegraph.DefaultSurface;
import automenta.spacegraph.control.FractalControl;
import automenta.spacegraph.impl.SGPanel;
import automenta.spacegraph.math.linalg.Vec4f;
import automenta.spacegraph.shape.Rect;
import automenta.spacegraph.shape.WideIcon;
import automenta.spacegraph.ui.GridRect;
import automenta.spacegraph.ui.PointerLayer;
import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.MutableBidirectedGraph;
import com.syncleus.dann.graph.MutableDirectedAdjacencyGraph;
import genifer.Fact;
import genifer.Genifer;
import genifer.GeniferLisp;
import genifer.Sexp;
import genifer.SimpleMemory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javolution.context.ConcurrentContext;
import org.armedbear.lisp.Cons;
import org.armedbear.lisp.LispObject;
import org.armedbear.lisp.Operator;
import org.armedbear.lisp.Symbol;

/**
 *
 * @author me
 */
public class RunGeniferGraph<N, E extends DirectedEdge<N>> extends DefaultSurface implements Demo {

    public static void main(String[] args) {
        SwingWindow sw = new SwingWindow(new RunGeniferGraph().newPanel(), 800, 600, true);

    }
    private MyHyperassociativeMap layout;

    public MutableBidirectedGraph getGeniferGraph(Genifer gen, int maxLevels) {
        MutableBidirectedGraph<Node, ValueEdge<Node, Link>> g = new MutableDirectedAdjacencyGraph<Node, ValueEdge<Node, Link>>();
        for (Fact f : gen.getMemory().getFacts()) {
            updateGeniferGraph(g, gen, f, maxLevels - 1);
        }
        return g;
    }

    protected void updateGeniferGraph(MutableBidirectedGraph<Node, ValueEdge<Node, Link>> g, Genifer gen, Fact f, int maxLevels) {
        Node n = new StringNode(f.toString());
        g.add(n);
        if (f.formula instanceof Sexp) {
            Sexp s = (Sexp) f.formula;
            Cons c = s.cons;
            updateGeniferGraph(g, gen, c, n, maxLevels - 1);

        }
    }

    protected void updateGeniferGraph(MutableBidirectedGraph<Node, ValueEdge<Node, Link>> g, Genifer gen, LispObject o, Node parent, int maxLevels) {
        if (maxLevels == 0)
            return;
        
        if (o instanceof Cons) {
            Cons c = (Cons) o;
            LispObject car = c.car;
            {
                String label = car.toString();
                StringNode carString = new StringNode(label);
                g.add(carString);
                g.add(new ValueEdge<Node, Link>(new Next(), parent, carString));
                updateGeniferGraph(g, gen, car, carString, maxLevels - 1);
            }
            LispObject cdr = c.cdr;
            {
                String label = cdr.toString();
                StringNode cdrString = new StringNode(label);
                g.add(cdrString);
                g.add(new ValueEdge<Node, Link>(new Next(), parent, cdrString));
                updateGeniferGraph(g, gen, cdr, cdrString, maxLevels - 1);
            }
        } else if (o instanceof Symbol) {
            Symbol a = (Symbol)o;
            StringNode oString = new StringNode(a.getName());
            g.add(oString);
            g.add(new ValueEdge<Node, Link>(new Next(), parent, oString));
        } else if (o instanceof Operator) {
            Operator a = (Operator)o;
            StringNode oString = new StringNode(o.toString());
            g.add(oString);
            g.add(new ValueEdge<Node, Link>(new Next(), parent, oString));
        } else {
            StringNode oString = new StringNode(o.toString());
            g.add(oString);
            g.add(new ValueEdge<Node, Link>(new Next(), parent, oString));
        }
    }

    public JPanel newPanel() {
        ConcurrentContext.setConcurrency(Runtime.getRuntime().availableProcessors());

        GeniferLisp genifer = new GeniferLisp(new SimpleMemory());
        genifer.execute("INIT-TEST-MEM", genifer.getMemory());

        MutableBidirectedGraph<Node, ValueEdge<Node, Link>> target = getGeniferGraph(genifer, 0);

        int numDimensions = 3;

        System.out.println(target.getNodes().size() + " : " + target.getEdges().size());

        layout = new MyHyperassociativeMap(target, numDimensions, 0.15, true);
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
                Vec4f v = new Vec4f(r, g, b, 1.0f);
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
        return "Genifer Logic Graphs";
    }

    @Override
    public String getDescription() {
        return "..";
    }
}
