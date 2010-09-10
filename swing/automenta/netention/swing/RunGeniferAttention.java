/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.ScalarMap;
import automenta.netention.plugin.genifer.GeniferGraph;
import automenta.netention.Link;
import automenta.netention.Node;
import automenta.netention.Node.StringNode;
import automenta.netention.graph.NotifyingDirectedGraph;
import automenta.netention.graph.SeHHyperassociativeMap;
import automenta.netention.graph.ValueEdge;
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
import com.syncleus.dann.graph.Graph;
import genifer.Fact;
import genifer.Genifer;
import genifer.GeniferLisp;
import genifer.Rule;
import genifer.SimpleMemory;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javolution.context.ConcurrentContext;

/**
 *
 * @author me
 */
public class RunGeniferAttention<N, E extends DirectedEdge<N>> extends DefaultSurface implements Demo {

    int depth = 4; //no more than 4 or 5
    int numDimensions = 3;

    public static void main(String[] args) {
        SwingWindow sw = new SwingWindow(new RunGeniferAttention().newPanel(), 800, 600, true);
    }

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

    public NotifyingDirectedGraph getGeniferGraph(Genifer gen, int maxLevels) {
        return new GeniferGraph(gen, maxLevels);
    }

    public JPanel newPanel() {
        ConcurrentContext.setConcurrency(Runtime.getRuntime().availableProcessors());

        GeniferLisp genifer = new GeniferLisp(new SimpleMemory());
        genifer.execute("INIT-TEST-MEM", genifer.getMemory());

        NotifyingDirectedGraph<Node, ValueEdge<Node, Link>> target = getGeniferGraph(genifer, depth);


        System.out.println(target.getNodes().size() + " : " + target.getEdges().size());
        final SeHHyperassociativeMap layout = new SeHHyperassociativeMap(target, numDimensions, 0.1, true);

        final ScalarMap<Node> attention = new ScalarMap();

        final GraphSpace<Node, ValueEdge<Node, Link>> graphCanvas = new GraphSpace<Node, ValueEdge<Node, Link>>(target, layout) {

            @Override
            public Rect newNodeRect(Node n) {
                return super.newNodeRect(n);
            }

            @Override
            protected void updateRect(Node n, Rect r) {
                float a = 0.1f;
                if (attention.get(n) != null) {
                    a = 0.1f + Math.min(attention.get(n).floatValue(), 0.9f);
                } else {
                    a = 0.1f;
                }

                if (n instanceof FactNode) {
                    FactNode f = (FactNode) n;
                    float w = (float) f.getProbability() + 0.5f + a;
                    r.setBackgroundColor(new Vec4f(a / 2f, a, a / 4f, 0.75f));
                    r.scale(w, w);
                } else if (n instanceof RuleNode) {
                    RuleNode f = (RuleNode) n;
                    float w = (float) f.getW() / 100.0f + 0.5f + a;
                    r.setBackgroundColor(new Vec4f(a, a / 2f, a / 4f, 0.75f));
                    r.scale(w, w);
                } else {
                    float w = (float) 0.5f + a;
                    r.setBackgroundColor(new Vec4f(a, a / 2f, a / 2f, 0.75f));
                    r.scale(w, w);
                    super.updateRect(n, r);
                }
            }
        };
        
        for (Node n : target.getNodes())
            attention.set(n, 0.1);

        new Thread(new Runnable() {

            protected void delay(long ms) {
                try {
                    Thread.sleep(ms);
                } catch (InterruptedException ex) {
                }
            }
            
            @Override            
            public void run() {
                while (true) {
                    delay(15);
                    attention.spikeRandomly(0.7);
                    attention.multiply(0.99);
                }                
            }
            
        }).start();
        
        SGPanel j = new SGPanel(graphCanvas);

        new FractalControl(j);
        new PointerLayer(this);

        graphCanvas.add(new GridRect(6, 6));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(j, BorderLayout.CENTER);

        j.getCanvas().addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("keys");
                layout.reset();
                layout.randomize(2.0);
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
            
        });
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
