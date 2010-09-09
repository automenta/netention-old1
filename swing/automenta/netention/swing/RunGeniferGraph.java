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
import automenta.netention.plugin.finance.PublicBusiness.BusinessPerformance;
import automenta.netention.swing.RunDemos.Demo;
import automenta.netention.swing.util.SwingWindow;
import automenta.spacegraph.DefaultSurface;
import automenta.spacegraph.control.FractalControl;
import automenta.spacegraph.impl.SGPanel;
import automenta.spacegraph.math.linalg.Vec4f;
import automenta.spacegraph.shape.Rect;
import automenta.spacegraph.ui.GridRect;
import automenta.spacegraph.ui.PointerLayer;
import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.MutableBidirectedGraph;
import com.syncleus.dann.graph.MutableDirectedAdjacencyGraph;
import genifer.Fact;
import genifer.Formula;
import genifer.Genifer;
import genifer.GeniferLisp;
import genifer.Rule;
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

/**
 *
 * @author me
 */
public class RunGeniferGraph<N, E extends DirectedEdge<N>> extends DefaultSurface implements Demo {

    public static void main(String[] args) {
        SwingWindow sw = new SwingWindow(new RunGeniferGraph().newPanel(), 800, 600, true);

    }
    private MyHyperassociativeMap layout;
    int depth = 4;
    int numDimensions = 3;

    public static class FactNode extends StringNode {

        public final Fact fact;

        public FactNode(Fact f) {
            super(f.toString());
            this.fact = f;
        }

        public double getProbability() {
            return fact.truth.getProbability();
        }
    }

    public static class RuleNode extends StringNode {

        public final Rule rule;

        public RuleNode(Rule r) {
            super(r.toString());
            this.rule = r;
        }

        public double getW() {
            return rule.getW();
        }
    }

    public static class GeniferGraph extends MutableDirectedAdjacencyGraph<Node, ValueEdge<Node, Link>> {

        private final Genifer gen;

        public GeniferGraph(Genifer gen, int maxLevels) {
            super();
            this.gen = gen;
            update(maxLevels);
        }

        protected void update(int maxLevels) {
            clear();
            for (Fact f : gen.getMemory().getFacts()) {
                updateNode(f, maxLevels - 1);
            }
            for (Rule rule : gen.getMemory().getRules()) {
                updateNode(rule, maxLevels - 1);
            }
        }

        protected void updateNode(Rule r, int i) {
            if (i == 0) {
                return;
            }


            Formula formula = r.formula;
            if (formula instanceof Sexp) {
                Sexp s = (Sexp) formula;
                Node parent = new RuleNode(r);
                add(parent);
                updateLispObject(parent, s.cons.car, i - 1);
                updateLispObject(parent, s.cons.cdr, i - 1);
            }

        }

        protected void updateNode(Fact f, int i) {
            if (i == 0) {
                return;
            }

            Formula formula = f.formula;
            if (formula instanceof Sexp) {
                Sexp s = (Sexp) formula;
                Node parent = new FactNode(f);
                add(parent);
                updateLispObject(parent, s.cons.car, i - 1);
                updateLispObject(parent, s.cons.cdr, i - 1);
            }

        }

        protected void updateLispObject(Node parent, LispObject l, int i) {
            if (i == 0) {
                return;
            }
            if (l == null) {
                return;
            }

            Node lNode = getNode(l);
            add(lNode);
            add(new ValueEdge<Node, Link>(new Next(), parent, lNode));

            if (l instanceof Cons) {
                Cons c = (Cons) l;
                updateLispObject(lNode, c.car, i - 1);
                updateLispObject(lNode, c.cdr, i - 1);
            }
        }

        private Node getNode(LispObject l) {
            if (l instanceof Cons) {
                return new StringNode("cons-" + l.hashCode());
            }

            return new StringNode(l.writeToString());
        }
    }

    public MutableBidirectedGraph getGeniferGraph(Genifer gen, int maxLevels) {
        return new GeniferGraph(gen, maxLevels);
    }

    public JPanel newPanel() {
        ConcurrentContext.setConcurrency(Runtime.getRuntime().availableProcessors());

        GeniferLisp genifer = new GeniferLisp(new SimpleMemory());
        genifer.execute("INIT-TEST-MEM", genifer.getMemory());

        MutableBidirectedGraph<Node, ValueEdge<Node, Link>> target = getGeniferGraph(genifer, depth);


        System.out.println(target.getNodes().size() + " : " + target.getEdges().size());

        layout = new MyHyperassociativeMap(target, numDimensions, 0.15, true);
        final GraphSpace graphCanvas = new GraphSpace(target, layout) {

            @Override
            public Rect newNodeRect(Object n) {
                return super.newNodeRect(n);
            }

            @Override
            protected void updateRect(Object n, Rect r) {
                if (n instanceof FactNode) {
                    FactNode f = (FactNode) n;
                    float w = (float) f.getProbability() + 0.5f;
                    r.setBackgroundColor(new Vec4f(w / 2f, w, w / 4f, 0.75f));
                    r.scale(w, w);
                } else if (n instanceof RuleNode) {
                    RuleNode f = (RuleNode) n;
                    float w = (float) f.getW() / 100.0f + 0.5f;
                    r.setBackgroundColor(new Vec4f(w, w / 2f, w / 4f, 0.75f));
                    r.scale(w, w);

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
